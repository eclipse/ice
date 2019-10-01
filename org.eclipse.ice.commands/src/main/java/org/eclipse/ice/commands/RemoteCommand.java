/**
 * /*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * This class inherits from Command and gives available functionality for remote
 * commands. These could be ssh connections or a remote process.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteCommand extends Command {

	/**
	 * The particular connection associated to a particular RemoteCommand. Declare
	 * this up front since by definition a RemoteCommand must have a connection.
	 */
	private Connection connection = new Connection();

	/**
	 * A file output stream for error messages to be remotely logged to
	 */
	private FileOutputStream stdErrStream = null;

	/**
	 * A file output stream for output messages to be remotely logged to
	 */
	private FileOutputStream stdOutStream = null;

	/**
	 * Default constructor
	 */
	public RemoteCommand() {

	}

	/**
	 * Constructor to instantiate the remote command with a particular
	 * CommandConfiguration and ConnectionConfiguration.
	 * 
	 * @param - ConnectionConfiguration which corresponds to the particular
	 *          connection
	 * @param - CommandConfiguration which corresponds to the particular command
	 */
	public RemoteCommand(ConnectionConfiguration connectConfig, CommandConfiguration _commandConfig) {
		commandConfig = _commandConfig;
		// Open and set the connection
		try {
			connection = ConnectionManager.openConnection(connectConfig);
		} catch (JSchException e) {
			// If the connection can't be opened, we can't be expected to execute a job
			// remotely!
			status = CommandStatus.INFOERROR;
			e.printStackTrace();
			return;
		}

		// Set the commandConfig hostname to that of the connectionConfig - only used
		// for output logging info
		commandConfig.setHostname(connectConfig.getHostname());

		status = CommandStatus.PROCESSING;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#run()}
	 */
	@Override
	protected CommandStatus run() {

		// Transfer the necessary files to the remote host
		// If the transfer fails for some reason, print stack trace and return an info
		// error
		try {
			status = transferFiles();
		} catch (SftpException | FileNotFoundException | JSchException e) {
			logger.error("File transfer error, could not complete file transfers to remote host. Exiting.");
			e.printStackTrace();
			return CommandStatus.INFOERROR;
		}

		// Check the status to ensure file transfer was successful
		try {
			checkStatus(status);
		} catch (IOException e) {
			// If it wasn't successful, return
			e.printStackTrace();
			return CommandStatus.INFOERROR;
		}

		// Execute the commands on the remote host
		status = processJob();

		// Check the status to ensure nothing failed
		try {
			checkStatus(status);
		} catch (IOException e) {
			// If it failed, return so
			e.printStackTrace();
			return CommandStatus.FAILED;
		}

		// Monitor the job to check its exit value and ensure it finishes correctly
		status = monitorJob();

		// Check the status to ensure job finished successfully
		try {
			checkStatus(status);
		} catch (IOException e) {
			// If it failed, return so
			e.printStackTrace();
			return CommandStatus.FAILED;
		}

		// Finish the job by cleaning up the remote directories created
		status = finishJob();

		return status;
	}

	/**
	 * This function deletes the remote working directory if the user so desires and
	 * then disconnects the remote channel to finish up the job processing.
	 * 
	 * @return
	 */
	@Override
	protected CommandStatus finishJob() {
		// If the user would like to delete the remote working directory, delete it
		if (connection.getConfiguration().getDeleteWorkingDirectory()) {
			logger.info("Removing remote working directory");
			// Set a command to force remove the directory
			((ChannelExec) connection.getChannel()).setCommand("rm -rf " + commandConfig.getRemoteWorkingDirectory());
			// Connect the channel to execute the removal
			try {
				connection.getChannel().connect();
			} catch (JSchException e) {
				logger.error("Could not delete the remote working directory after completion!");
				e.printStackTrace();
			}
		}

		// Disconnect the session and return success
		connection.getChannel().disconnect();
		connection.getSession().disconnect();

		/**
		 * Note that output doesn't have to explicitly be logged - JSch takes care of
		 * this for you in {@link org.eclipse.ice.commands.RemoteCommand#loopCommands}
		 */

		return CommandStatus.SUCCESS;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#monitorJob()}
	 */
	@Override
	protected CommandStatus monitorJob() {
		// Poll until the command is complete. If it isn't finished, give it a second
		// to try and finish up
		// Set exitValue to an arbitrary number indicating job not finished
		int exitValue = -1;
		while (exitValue != 0) {
			try {
				// Give it a second to finish up
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// Query the exit status. 0 is normal completion, everything else is abnormal
			exitValue = ((ChannelExec) connection.getChannel()).getExitStatus();

			// If the connection was closed and the job didn't finish, something bad
			// happened...
			if (connection.getChannel().isClosed() && exitValue != 0) {
				logger.error("Connection was closed before job was finished, failed " + exitValue);
				return CommandStatus.FAILED;
			}
		}

		// If the job finished correctly, return success. Otherwise return failure
		if (exitValue == 0)
			return CommandStatus.SUCCESS;
		else
			return CommandStatus.FAILED;
	}

	/**
	 * This function loops over the various commands to be run on the JSch
	 * connection. Since they can't be run all together they have to be run
	 * individually.
	 * 
	 * @return
	 */
	@Override
	protected CommandStatus processJob() {

		// Setup the list of all of the commands that will be launched. JSch can not
		// launch multiple commands at once, so we need to take the splitCommand and
		// cd to the correct working directory in front and then actually perform
		// the execution. The commands are then split by the semi-colon
		ArrayList<String> completeCommands = new ArrayList<String>();
		for (String i : commandConfig.getSplitCommand()) {
			completeCommands.add("cd " + commandConfig.getRemoteWorkingDirectory() + "; " + i);
		}

		// Now loop over all commands and run them via JSch
		for (int i = 0; i < completeCommands.size(); i++) {
			// Open the channel for the executable to be run on
			try {
				connection.setChannel(connection.getSession().openChannel("exec"));
			} catch (JSchException e) {
				// If it can't be opened, puke
				e.printStackTrace();
			}

			// Give the command to the channel connection
			String thisCommand = completeCommands.get(i);
			((ChannelExec) connection.getChannel()).setCommand(thisCommand);

			logger.info("Executing command: " + thisCommand + " remotely in the working direcotry "
					+ commandConfig.getRemoteWorkingDirectory());

			// Set up the input stream
			connection.getChannel().setInputStream(null);
			try {
				// Set the input stream for the connection object
				connection.setInputStream(connection.getChannel().getInputStream());
			} catch (IOException e) {
				// If we can't set the input stream, puke
				e.printStackTrace();
			}

			// Setup the output streams and pass them to the connection channel
			try {
				stdErrStream = new FileOutputStream(commandConfig.getErrFileName(), true);
				stdOutStream = new FileOutputStream(commandConfig.getOutFileName(), true);
				BufferedOutputStream stdOutBufferedStream = new BufferedOutputStream(stdOutStream);
				// Give the streams to the channel now that their names are appropriately set
				connection.getChannel().setOutputStream(stdOutBufferedStream);
				((ChannelExec) connection.getChannel()).setErrStream(stdErrStream);
			} catch (FileNotFoundException e) {
				// If logging streams can't be set, puke
				e.printStackTrace();
			}

			// Make sure the channel is connected
			if (!connection.getChannel().isConnected()) {
				try {
					// Connect and run the executable
					connection.getChannel().connect();

					// Log the output and error streams
					logOutput(connection.getChannel().getInputStream(),
							((ChannelExec) connection.getChannel()).getErrStream());
				} catch (JSchException e) {
					logger.error("Couldn't connect the channel to run the executable!");
					e.printStackTrace();
					return CommandStatus.FAILED;
				} catch (IOException e) {
					logger.error("Couldn't log the output!");
					e.printStackTrace();
					return CommandStatus.FAILED;
				}
			} else {
				logger.error("Channel is not connected! Can't execute remotely...");
				return CommandStatus.FAILED;
			}

		}
		return CommandStatus.RUNNING;
	}

	/**
	 * This function is responsible for transferring the files to the remote host.
	 * //TODO Change this to use FileHandler functionality once implemented?
	 * 
	 * @return - CommandStatus indicating that the transfer was successful (or not)
	 * @throws SftpException
	 * @throws JSchException
	 * @throws FileNotFoundException
	 */
	protected CommandStatus transferFiles() throws SftpException, JSchException, FileNotFoundException {
		// Open the sftp channel to transfer the files
		ChannelSftp sftpChannel = (ChannelSftp) connection.getSession().openChannel("sftp");
		sftpChannel.connect();

		// Get the remote working directory to move files to
		// This way there is a clean remote directory with which to operate in
		String remoteWorkingDirectory = commandConfig.getRemoteWorkingDirectory();
		logger.info("Make the working directory at: " + remoteWorkingDirectory);

		// Try to cd to the directory if it already exists
		try {
			sftpChannel.cd(remoteWorkingDirectory);
		} catch (SftpException e) {
			// If we can't, try making the directory and then cd-ing. If can't again,
			// exception will be thrown
			sftpChannel.mkdir(remoteWorkingDirectory);
			try {
				sftpChannel.cd(remoteWorkingDirectory);
			} catch (SftpException e1) {
				logger.error("Tried making remote directory but couldn't. Bailing");
				e1.printStackTrace();
				return CommandStatus.FAILED;
			}
		}

		String workingDirectory = commandConfig.getWorkingDirectory();

		// Fix the inputFile name for remote machines to remove any possible slashes
		String shortInputName = commandConfig.getInputFile();

		if (shortInputName.contains("/"))
			shortInputName = shortInputName.substring(shortInputName.lastIndexOf("/") + 1);
		else if (shortInputName.contains("\\"))
			shortInputName = shortInputName.substring(shortInputName.lastIndexOf("\\") + 1);

		// Get the executable to concatenate
		String shortExecName = commandConfig.getExecutable();
		// Get the executable filename only by removing the all the junk in front of it
		// e.g. directory names, slashes, etc.
		if (shortExecName.contains("/"))
			shortExecName = shortExecName.substring(shortExecName.lastIndexOf("/") + 1);
		else if (shortExecName.contains("\\"))
			shortExecName = shortExecName.substring(shortExecName.lastIndexOf("\\") + 1);

		// Do the same for the destination
		if (!remoteWorkingDirectory.endsWith("/"))
			remoteWorkingDirectory += "/";

		// Now have the full paths, so transfer the files per the logger messages
		logger.info("Putting input file: " + workingDirectory + shortInputName + " in directory "
				+ remoteWorkingDirectory + shortInputName);
		// Put the inputfile to the remote directory. Use a null object for receiving
		// notifications about
		// the progress of the transfer and use 0 to overwrite the files if they exist
		// there already
		sftpChannel.put(workingDirectory + shortInputName, remoteWorkingDirectory + shortInputName);

		logger.info("Putting executable file: " + workingDirectory + shortExecName + " in directory "
				+ remoteWorkingDirectory + shortExecName);
		sftpChannel.put(workingDirectory + shortExecName, remoteWorkingDirectory + shortExecName);

		/**
		 * Change the permission of the executable so that it can be executed. Give user
		 * read write execute permissions, all other users no permissions NOTE: JSch
		 * takes a decimal number, not an octal number like one would normally expect
		 * with chmod. So 448 here in decimal corresponds to 700 in octal, i.e.
		 * -rwx------ We give write permissions also so that the file can be deleted at
		 * the end of processing if desired, or e.g. overwritten if the job fails for
		 * whatever reason and needs to be run again.
		 */
		sftpChannel.chmod(448, remoteWorkingDirectory + shortExecName);

		// Disconnect the sftp channel to stop moving files
		sftpChannel.disconnect();

		return CommandStatus.RUNNING;
	}

	/**
	 * Set a particular connection for a particular RemoteCommand
	 * 
	 * @param connection - the connection for this command
	 */
	public void setConnection(Connection _connection) {
		connection = _connection;
	}

	/**
	 * Return the connection associated to this RemoteCommand
	 * 
	 * @return - {@link org.eclipse.ice.commands.RemoteCommand#connection}
	 */
	public Connection getConnection() {

		return connection;
	}

}
