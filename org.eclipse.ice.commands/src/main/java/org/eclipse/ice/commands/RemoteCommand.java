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
import java.io.InputStream;
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
	protected Connection connection = new Connection();

	/**
	 * A file output stream for error messages to be remotely logged to
	 */
	FileOutputStream stdErrFile = null;

	/**
	 * A file output stream for output messages to be remotely logged to
	 */
	FileOutputStream stdOutStream;

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
			status = CommandStatus.INFOERROR;
			e.printStackTrace();
			return;
		}

		// Set the commandConfig hostname to that of the connectionConfig - only used
		// for output info
		commandConfig.setHostname(connectConfig.getHostname());
		status = CommandStatus.PROCESSING;
	}

	/**
	 * Method that overrides Commmand:Execute and actually implements the particular
	 * RemoteCommand to be executed.
	 */
	@Override
	public CommandStatus execute() {
		// Check that the commandConfig and connectConfig were properly instantiated in
		// the constructor
		try {
			checkStatus(status);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Configure the command to be ready to run.
		status = setConfiguration();

		// Ensure that the command was properly configured
		try {
			checkStatus(status);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Now that all of the prerequisites have been set, start the job running
		status = run();

		// Confirm the job finished with some status
		logger.info("The job finished with status: " + status);
		return status;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#run()}
	 */
	@Override
	protected CommandStatus run() {

		logger.info("Transferring files to remote host");
		try {
			status = transferFiles();
		} catch (SftpException e) {
			logger.error("Could not upload the input file to the remote host");
			e.printStackTrace();
		} catch (JSchException e) {
			logger.error("Session disconnected and could not upload the input file to the remote host");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			logger.error("Input file not found! Could not upload to the remote host");
			e.printStackTrace();
		}

		status = loopCommands();

		status = monitorJob();

		status = cleanUpJob();

		return status;
	}

	/**
	 * This function gets the output streams, logs them into the filenames, and then
	 * disconnects the remote channel to finish up the job processing.
	 * 
	 * @return
	 */
	protected CommandStatus cleanUpJob() {
		InputStream input = null;
		InputStream err = null;
		try {
			input = connection.getChannel().getInputStream();
			err = ((ChannelExec) connection.getChannel()).getErrStream();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (logOutput(input, err) == false) {
			logger.error("Couldn't log output, marking job as failed");
			return CommandStatus.FAILED;
		}

		// Disconnect the session and return success
		connection.getChannel().disconnect();
		connection.getSession().disconnect();

		return CommandStatus.SUCCESS;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#monitorJob()}
	 */
	@Override
	protected CommandStatus monitorJob() {
		// Poll until the command is complete. If it isn't finished, give it a second
		// to try and finish up
		int exitValue = -1;
		while (exitValue != 0) {
			try {
				// Give it a second to finish up
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			exitValue = ((ChannelExec) connection.getChannel()).getExitStatus();

			if (connection.getChannel().isClosed() && exitValue != 0) {
				logger.error("Connection was closed before job was finished, failed");
				return CommandStatus.FAILED;
			}
		}
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
	protected CommandStatus loopCommands() {

		// Setup the list of all of the commands that will be launched. JSch can not
		// launch multiple commands at once, so we need to take the splitCommand and
		// cd to the correct working directory in front and then actually perform
		// the execution. The commands are then split by the semi-colon
		ArrayList<String> completeCommands = new ArrayList<String>();
		for (String i : commandConfig.getSplitCommand()) {
			completeCommands.add("cd " + commandConfig.getWorkingDirectory() + "; " + i);
		}

		// Now loop over all commands and run them via JSch
		for (int i = 0; i < completeCommands.size(); i++) {
			// Open the channel for the executable to be run on
			try {
				connection.setChannel(connection.getSession().openChannel("exec"));
			} catch (JSchException e) {
				e.printStackTrace();
			}

			// Give the command to the channel connection
			String thisCommand = completeCommands.get(i);
			((ChannelExec) connection.getChannel()).setCommand(thisCommand);

			logger.info("Executing command: " + thisCommand + " remotely in the working direcotry "
					+ commandConfig.getWorkingDirectory());

			// Set up the input stream
			connection.getChannel().setInputStream(null);
			try {
				connection.setInputStream(connection.getChannel().getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Setup the output streams and pass them to the connection channel
			try {
				stdErrFile = new FileOutputStream(commandConfig.getErrFileName(), true);
				stdOutStream = new FileOutputStream(commandConfig.getOutFileName(), true);
				BufferedOutputStream stdOutBufferedStream = new BufferedOutputStream(stdOutStream);
				connection.getChannel().setOutputStream(stdOutBufferedStream);
				((ChannelExec) connection.getChannel()).setErrStream(stdErrFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			// Make sure the channel is connected
			if (!connection.getChannel().isConnected()) {
				try {
					connection.getChannel().connect();
				} catch (JSchException e) {
					e.printStackTrace();
				}
			} else {
				logger.error("Channel is not connected! Can't execute remotely...");
				return CommandStatus.FAILED;
			}

		}
		return CommandStatus.RUNNING;
	}

	/**
	 * This function is responsible for transferring the files to the remote host
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

		// Get the working directory to run everything in on the remote connection
		String remoteWorkingDirectory = connection.getConfiguration().getWorkingDirectory();

		logger.info("Make the working directory at: " + remoteWorkingDirectory);

		// Try to cd to the directory if it already exists
		try {
			sftpChannel.cd(remoteWorkingDirectory);
		} catch (SftpException e) {
			// If we can't, try making the directory and then cd-ing. If can't again,
			// exception will be thrown
			sftpChannel.mkdir(remoteWorkingDirectory);
			sftpChannel.cd(remoteWorkingDirectory);
		}

		// Fix the inputFile name for remote machines
		String shortInputName = commandConfig.getInputFile();
		if (shortInputName.contains("/"))
			shortInputName = shortInputName.substring(shortInputName.lastIndexOf("/") + 1);
		else if (shortInputName.contains("\\"))
			shortInputName = shortInputName.substring(shortInputName.lastIndexOf("\\") + 1);

		String localSrc = commandConfig.getWorkingDirectory();
		String exec = commandConfig.getExecutableName();

		// If the working directory doesn't have a / at the end of it, add it
		if (!localSrc.endsWith("/"))
			localSrc += "/";
		// Do the same for the destination
		if (!remoteWorkingDirectory.endsWith("/"))
			remoteWorkingDirectory += "/";
		
		// Now have the full paths, so transfer the files per the logger messages
		logger.info("Putting input file: " + localSrc + shortInputName + " in directory " + remoteWorkingDirectory + shortInputName);
		sftpChannel.put(localSrc + shortInputName, remoteWorkingDirectory + shortInputName);
		logger.info("Putting executable file: " + localSrc + exec + " in directory " + remoteWorkingDirectory + exec);
		sftpChannel.put(localSrc + exec, remoteWorkingDirectory + exec);
		
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
