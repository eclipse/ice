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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
	private AtomicReference<Connection> connection = new AtomicReference<Connection>(new Connection());

	/**
	 * An additional connection that is used for multi-hop connections, where a user
	 * connects to an intermediary machine (with connection, above) and then uses
	 * that machine to connect to a second machine. TODO - Implement multi-hop
	 * connections with secondConnection
	 */
	private Connection secondConnection = new Connection();

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
	 * @param - CommandConfiguration which corresponds to the particular command
	 * @param - ConnectionConfiguration connectConfig which corresponds to the
	 *          particular connection
	 * 
	 * @param - ConnectionConfiguration extraConnection which corresponds to an
	 *          additional connection, if the command is meant to multi-hop where
	 *          one remote host is used to execute a job on another remote host
	 */
	public RemoteCommand(CommandConfiguration _commandConfig, ConnectionConfiguration connectConfig,
			ConnectionConfiguration extraConnection) {
		// Set the command and connection configurations
		commandConfig = _commandConfig;
		connectionConfig = connectConfig;

		// Get the connection manager to open a new connection
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

		// Open and set the connection(s)
		try {
			if (manager.getConnection(connectConfig.getName()) == null) {
				connection.set(manager.openConnection(connectConfig));
			} else {
				connection.set(manager.getConnection(connectConfig.getName()));
				if (connection.get().getChannel() != null)
					connection.get().getChannel().disconnect();
			}

			// Set the commandConfig hostname to that of the connectionConfig - only used
			// for output logging info
			commandConfig.setHostname(connectConfig.getAuthorization().getHostname());

			// If there is an extra connection so that we are multi-hopping, then open it
			// too
			// TODO - the multi-hop API isn't implemented yet - need to work on it
			if (extraConnection != null) {
				secondConnection = manager.openConnection(extraConnection);
				// Set the commandConfig hostname to be the extra connection, since this is
				// really where the job will run
				commandConfig.setHostname(extraConnection.getAuthorization().getHostname());
			}
		} catch (JSchException e) {
			// If the connection(s) can't be opened, we can't be expected to execute a job
			// remotely!
			status = CommandStatus.INFOERROR;
			logger.error("There was a connection failure in the construction of Remote Command!");
			logger.error("Returning info error", e);
			return;
		}

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
		} catch (SftpException | JSchException | IOException e) {
			logger.error("File transfer error, could not complete file transfers to remote host. Exiting.");
			logger.error("Returning info error", e);
			return CommandStatus.INFOERROR;
		}

		// Check the status to ensure file transfer was successful
		if (!checkStatus(status))
			return CommandStatus.INFOERROR;

		// Execute the commands on the remote host
		status = processJob();

		// Check the status to ensure nothing failed
		if (!checkStatus(status))
			return CommandStatus.FAILED;

		// Monitor the job to check its exit value and ensure it finishes correctly
		status = monitorJob();

		// Check the status to ensure job finished successfully
		if (!checkStatus(status))
			return CommandStatus.FAILED;

		// Finish the job by cleaning up the remote directories created
		status = finishJob();

		return status;
	}

	/**
	 * This function deletes the remote working directory if the user so desires and
	 * then disconnects the remote channel to finish up the job processing.
	 * 
	 * See also {@link org.eclipse.ice.commands.Command#finishJob()}
	 * 
	 * @return CommandStatus
	 */
	@Override
	protected CommandStatus finishJob() {
		// If the user would like to delete the remote working directory, delete it
		if (connection.get().getConfiguration().deleteWorkingDirectory()) {
			logger.info("Removing remote working directory");
			try {
				// Open an sftp channel so that we can ls the contents of the path
				
				ChannelSftp channel = (ChannelSftp) connection.get().getSession().openChannel("sftp");
				channel.connect();
				// Delete the directory and all of the contents
				deleteRemoteDirectory(channel, commandConfig.getRemoteWorkingDirectory());
				channel.disconnect();
				} catch (JSchException | SftpException e) {
				// This exception just needs to be logged, since it is not harmful to
				// the job processing in any way
				logger.warn("Unable to delete remote directory tree.");
			}
		}

		// Disconnect the channel and return success
		connection.get().getChannel().disconnect();
		// Set the channel to null. This is important for running several jobs over one
		// session, since the channel has been changed to an exec channel and the next
		// job needs it as an sftp channel for file transfer. So let the next job take
		// care of the channel delegation.
		connection.get().setChannel(null);

		// Don't disconnect the session in the event that a user wants to run multiple
		// jobs over the same session. Let session management be handled by the job
		// running and the connection manager

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

		while (exitValue != 0) {
			try {
				// Give it a second to finish up
				Thread.currentThread().sleep(1000);
			} catch (InterruptedException e) {
				// Just log this exception, see if thread can wait next iteration
				logger.error("Thread couldn't wait for another second while monitoring job...", e);
			}
			// Query the exit status. 0 is normal completion, everything else is abnormal
			exitValue = ((ChannelExec) connection.get().getChannel()).getExitStatus();

			// If the connection was closed and the job didn't finish, something bad
			// happened...
			if (connection.get().getChannel().isClosed() && exitValue != 0) {
				logger.error("Connection is closed with exit value " + exitValue + ", failed ");
				return CommandStatus.FAILED;
			}
		}

		// If the job returns anything other than 0, then the job failed. Otherwise
		// success
		if (exitValue != 0)
			return CommandStatus.FAILED;

		return CommandStatus.SUCCESS;
	}

	/**
	 * This function loops over the various commands to be run on the JSch
	 * connection. Since they can't be run all together they have to be run
	 * individually.
	 * 
	 * See also {@link org.eclipse.ice.commands.Command#processJob()}
	 * 
	 * @return CommandStatus
	 */
	@Override
	protected CommandStatus processJob() {

		// Setup the list of all of the commands that will be launched. JSch can not
		// launch multiple commands at once, so we need to take the splitCommand and
		// cd to the correct working directory in front and then actually perform
		// the execution. The commands are then split by the semi-colon
		ArrayList<String> completeCommands = new ArrayList<String>();
		for (String i : commandConfig.getSplitCommand()) {
			String completeCommand = "cd " + commandConfig.getRemoteWorkingDirectory() + "; ";
			// If launched from windows, we need to remove the dos carriage returns ^M from
			// the bash script. thanks a lot dos.
			if(commandConfig.getOS().toLowerCase().contains("win"))
				completeCommand += "sed -i -e 's/\\r//' " + commandConfig.getExecutable() + "; ";
			completeCommand += i;
			completeCommands.add(completeCommand);
		}

		// Now loop over all commands and run them via JSch
		for (int i = 0; i < completeCommands.size(); i++) {
			// Open the channel for the executable to be run on
			try {
				connection.get().setChannel(connection.get().getSession().openChannel("exec"));
			} catch (JSchException e) {
				logger.error("Execution channel could not be opened over JSch... Returning failed.", e);
				// If it can't be opened, fail
				return CommandStatus.FAILED;
			}

			// Give the command to the channel connection
			String thisCommand = completeCommands.get(i);
			((ChannelExec) connection.get().getChannel()).setCommand(thisCommand);

			logger.info("Executing command: " + thisCommand + " remotely in the working directory "
					+ commandConfig.getRemoteWorkingDirectory());

			// Set up the input stream
			connection.get().getChannel().setInputStream(null);
			try {
				// Set the input stream for the connection object
				connection.get().setInputStream(connection.get().getChannel().getInputStream());
			} catch (IOException e) {
				logger.error("Input stream could not be set in JSch... Returning failed.", e);
				// If we can't set the input stream, fail
				return CommandStatus.FAILED;
			}

			// Setup the output streams and pass them to the connection channel
			try {
				stdErrStream = new FileOutputStream(commandConfig.getErrFileName(), true);
				stdOutStream = new FileOutputStream(commandConfig.getOutFileName(), true);
				BufferedOutputStream stdOutBufferedStream = new BufferedOutputStream(stdOutStream);
				// Give the streams to the channel now that their names are appropriately set
				connection.get().getChannel().setOutputStream(stdOutBufferedStream);
				((ChannelExec) connection.get().getChannel()).setErrStream(stdErrStream);
			} catch (FileNotFoundException e) {
				logger.error("Logging streams could not be set in JSch... Returning failed.", e);
				// If logging streams can't be set, return failed since we won't be
				// able to see if job was successful or not
				return CommandStatus.FAILED;
			}

			// Make sure the channel is connected
			try {
				// Connect and run the executable
				connection.get().getChannel().connect();

				// Log the output and error streams
				logOutput(connection.get().getChannel().getInputStream(),
						((ChannelExec) connection.get().getChannel()).getErrStream());
			} catch (JSchException e) {
				logger.error("Couldn't connect the channel to run the executable! Returning failed.", e);
				return CommandStatus.FAILED;
			} catch (IOException e) {
				logger.error("Couldn't log the output! Returning failed.", e);
				return CommandStatus.FAILED;
			}
		}

		return CommandStatus.RUNNING;
	}

	/**
	 * This function is responsible for transferring the files to the remote host.
	 * It utilizes the file handling API in this package
	 * 
	 * @return - CommandStatus indicating that the transfer was successful (or not)
	 * @throws SftpException
	 * @throws JSchException
	 * @throws IOException
	 */
	protected CommandStatus transferFiles() throws SftpException, JSchException, IOException {

		// Set up a remote file handler to transfer the files
		RemoteFileHandler handler = new RemoteFileHandler();
		// Give the handler the same connection as this command
		handler.setConnectionConfiguration(connectionConfig);

		// Get the directories where files live/should go
		String remoteWorkingDirectory = commandConfig.getRemoteWorkingDirectory();
		String workingDirectory = commandConfig.getWorkingDirectory();

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

		// Build the source and destination paths
		String source = workingDirectory + shortExecName;
		String destination = remoteWorkingDirectory + shortExecName;

		CommandStatus fileTransfer = null;
		// Check if the source file exists. If the executable is a script, then it will
		// transfer it to the host. If it is just a command (e.g. ls) then it will skip
		if (handler.exists(source)) {
			// Change the permission of the executable so that it can be executed. Give user
			// read write execute permissions, all other users no permissions
			// We give write permissions also so that the file can be
			// deleted at the end of processing if desired, or e.g. overwritten if the job
			// fails for whatever reason and needs to be run again.
			handler.setPermissions("700");
			fileTransfer = handler.copy(source, destination);
			if (fileTransfer != CommandStatus.SUCCESS) {
				logger.error("Couldn't transfer executable to remote host!");
				throw new IOException();
			}
		}

		// Now move the input files after moving the executable file
		HashMap<String, String> inputFiles = commandConfig.getInputFileList();

		// Give the input files permissions of reading/writing
		handler.setPermissions("600");
		// Iterate over each input file
		for (Map.Entry<String, String> entry : inputFiles.entrySet()) {
			// Get the filepath
			String shortInputName = entry.getValue();

			// Fix the inputFile name for remote machines to remove any possible slashes
			if (shortInputName.contains("/"))
				shortInputName = shortInputName.substring(shortInputName.lastIndexOf("/") + 1);
			else if (shortInputName.contains("\\"))
				shortInputName = shortInputName.substring(shortInputName.lastIndexOf("\\") + 1);

			// Now have the full paths, so transfer the files per the logger messages
			// Put the inputfile to the remote directory. Use a null object for receiving
			// notifications about
			// the progress of the transfer and use 0 to overwrite the files if they exist
			// there already
			source = workingDirectory + shortInputName;
			destination = remoteWorkingDirectory + shortInputName;
			fileTransfer = handler.copy(source, destination);
			if (fileTransfer != CommandStatus.SUCCESS) {
				logger.error("Couldn't transfer " + source + " to remote host!");
				throw new IOException();
			}
		}

		// Return that the job is running now
		return CommandStatus.RUNNING;

	}

	/**
	 * Recursive function that deletes a remote directory and its contents
	 * 
	 * @param sftpChannel - channel with which to use to delete the remote
	 *                    directories
	 * @param path        - top directory to delete
	 * @throws SftpException
	 */
	private void deleteRemoteDirectory(ChannelSftp sftpChannel, String path) throws SftpException {

		// Get the path's directory structure
		Collection<ChannelSftp.LsEntry> fileList = sftpChannel.ls(path);

		// Iterate through the list to get the file/directory names
		for (ChannelSftp.LsEntry file : fileList) {
			// If it isn't a directory delete it
			if (!file.getAttrs().isDir()) {
				sftpChannel.rm(path + "/" + file.getFilename());
			} else if (!(".".equals(file.getFilename()) || "..".equals(file.getFilename()))) { // If it is a subdir.
				// Otherwise its a subdirectory, so try deleting it
				try {
					// remove the sub directory
					sftpChannel.rmdir(path + "/" + file.getFilename());
				} catch (Exception e) {
					// If the subdirectory is not empty, then iterate with this
					// subdirectory to remove the contents
					deleteRemoteDirectory(sftpChannel, path + "/" + file.getFilename());
				}
			}
		}
		sftpChannel.rmdir(path); // delete the parent directory after empty
	}

	/**
	 * Set a particular connection for a particular RemoteCommand
	 * 
	 * @param connection - the connection for this command
	 */
	public void setConnection(Connection connection) {
		this.connection = new AtomicReference<Connection>(connection);
	}

	/**
	 * Return the connection associated to this RemoteCommand
	 * 
	 * @return - {@link org.eclipse.ice.commands.RemoteCommand#connection}
	 */
	public Connection getConnection() {

		return connection.get();
	}

}
