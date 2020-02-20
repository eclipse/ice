/*******************************************************************************
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.apache.sshd.client.channel.ClientChannelEvent;
import org.apache.sshd.client.subsystem.sftp.SftpClient;
import org.apache.sshd.client.subsystem.sftp.SftpClient.DirEntry;
import org.apache.sshd.client.subsystem.sftp.SftpClientFactory;
import org.apache.sshd.common.subsystem.sftp.SftpException;

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
	private AtomicReference<Connection> secondConnection = new AtomicReference<Connection>(new Connection());

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
	public RemoteCommand(CommandConfiguration commandConfig, ConnectionConfiguration connectConfig,
			ConnectionConfiguration extraConnection) {
		// Set the command and connection configurations
		this.commandConfig = commandConfig;
		this.connectionConfig = connectConfig;
		this.secondConnection.get().setConfiguration(extraConnection);
		openAndSetConnection();

		status = CommandStatus.PROCESSING;
	}

	/**
	 * Opens and sets a connection based on what was passed in the constructor. This
	 * function first checks if a connection with the same name is already available
	 * in the connection manager, and if so, grabs it. Otherwise, it opens a new
	 * connection with the provided information. Function is public so that if a
	 * user wants to reset the connection for a particular command, they have the
	 * option to.
	 */
	public void openAndSetConnection() {
		// Open and set the connection(s)
		try {
			if (manager.getConnection(connectionConfig.getName()) == null) {
				connection.set(manager.openConnection(connectionConfig));
			} else {
				if (connection.get().getSession() != null && connection.get().getSession().isOpen()) {
					connection.set(manager.getConnection(connectionConfig.getName()));
					// Make sure the connections are starting fresh from scratch
					if (connection.get().getExecChannel() != null)
						connection.get().getExecChannel().close();
					if (connection.get().getSftpChannel() != null)
						connection.get().getSftpChannel().close();
				} else {
					connection.set(manager.openConnection(connectionConfig));
				}
			}

			// Set the commandConfig hostname to that of the connectionConfig - only used
			// for output logging info
			commandConfig.setHostname(connectionConfig.getAuthorization().getHostname());

			// If there is an extra connection so that we are multi-hopping, then open it
			// too
			ConnectionConfiguration secondConfig = secondConnection.get().getConfiguration();
			if (secondConfig != null) {
				secondConnection.set(manager.openForwardingConnection(connection.get(),secondConfig));
				// Set the commandConfig hostname to be the extra connection, since this is
				// really where the job will run
				commandConfig.setHostname(secondConnection.get().getConfiguration().getAuthorization().getHostname());
			}
		} catch (IOException e) {
			// If the connection(s) can't be opened, we can't be expected to execute a job
			// remotely!
			status = CommandStatus.INFOERROR;
			logger.error("There was a connection failure in the construction of Remote Command!");
			logger.error("Returning info error", e);
			return;
		}
		
		return;
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
		} catch (IOException e) {
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
	 */
	@Override
	protected CommandStatus finishJob() {
		// If the user would like to delete the remote working directory, delete it
		if (connection.get().getConfiguration().deleteWorkingDirectory()) {
			logger.info("Removing remote working directory");
			try {
				// Open an sftp channel so that we can ls the contents of the path

				SftpClient client = SftpClientFactory.instance().createSftpClient(connection.get().getSession());
				// Delete the directory and all of the contents
				deleteRemoteDirectory(client, commandConfig.getRemoteWorkingDirectory());
				client.close();
			} catch (IOException e) {
				// This exception just needs to be logged, since it is not harmful to
				// the job processing in any way
				logger.warn("Unable to delete remote directory tree.");
			}
		}

		// Disconnect the channels and return success
		try {
			connection.get().getExecChannel().close();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
		try {
			connection.get().getSftpChannel().close();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}

		// Don't disconnect the session in the event that a user wants to run multiple
		// jobs over the same session. Let session management be handled by the
		// connection manager

		/**
		 * Note that output doesn't have to explicitly be logged - Mina takes care of
		 * this for you in {@link org.eclipse.ice.commands.RemoteCommand#loopCommands}
		 * However we set the strings in CommandConfiguration here since Mina only logs
		 * the output to the files
		 */
		String stdOutFileName = commandConfig.getOutFileName();
		String stdErrFileName = commandConfig.getErrFileName();

		File outfile = new File(stdOutFileName);
		File errfile = new File(stdErrFileName);
		try {
			FileReader outreader = new FileReader(outfile);
			BufferedReader outbr = new BufferedReader(outreader);
			String line;
			// Read in each line
			while ((line = outbr.readLine()) != null) {
				// Commented lines by definition begin with #, so skip these
				if (!line.startsWith("#"))
					commandConfig.addToStdOutputString(line);
			}
			// Close the reader
			outbr.close();

			// Repeat the same process for the error file
			FileReader errreader = new FileReader(errfile);
			BufferedReader errbr = new BufferedReader(errreader);
			line = null;
			while ((line = errbr.readLine()) != null) {
				if (!line.startsWith("#"))
					commandConfig.addToStdOutputString(line);
			}
			errbr.close();

		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}

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
			// First make sure the job hasn't been canceled
			if (status == CommandStatus.CANCELED)
				return CommandStatus.CANCELED;

			Collection<ClientChannelEvent> waitMask = connection.get().getExecChannel()
					.waitFor(EnumSet.of(ClientChannelEvent.CLOSED), 1000L);
			if (waitMask.contains(ClientChannelEvent.TIMEOUT)) {
				// Just log this exception, see if thread can wait next iteration
				logger.error("Thread couldn't wait for another second while monitoring job...");
			}
			
			// If the exit status is null, wait for a minute to see if it can process
			if(connection.get().getExecChannel().getExitStatus() == null) {
				continue;
			}
			// Query the exit status. 0 is normal completion, everything else is abnormal
			exitValue = connection.get().getExecChannel().getExitStatus();

			// If the connection was closed and the job didn't finish, something bad
			// happened...
			if (connection.get().getExecChannel().isClosed() && exitValue != 0) {
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
			if (commandConfig.getOS().toLowerCase().contains("win"))
				completeCommand += "sed -i -e 's/\\r//' " + commandConfig.getExecutable() + "; ";
			completeCommand += i;
			completeCommands.add(completeCommand);
		}

		// Now loop over all commands and run them via JSch
		for (int i = 0; i < completeCommands.size(); i++) {
			// Give the command to the channel connection
			String thisCommand = completeCommands.get(i);

			try {
				connection.get().setExecChannel(connection.get().getSession().createExecChannel(thisCommand));
			} catch (IOException e) {
				logger.error("Execution channel could not be opened... Returning failed.", e);
				// If it can't be opened, fail
				return CommandStatus.FAILED;
			}

			logger.info("Executing command: " + thisCommand + " remotely in the working directory "
					+ commandConfig.getRemoteWorkingDirectory());

			connection.get().getExecChannel().setIn(null);
			connection.get().setInputStream(connection.get().getExecChannel().getIn());

			// Setup the output streams and pass them to the connection channel
			try {
				stdErrStream = new FileOutputStream(commandConfig.getErrFileName(), true);
				stdOutStream = new FileOutputStream(commandConfig.getOutFileName(), true);
				BufferedOutputStream stdOutBufferedStream = new BufferedOutputStream(stdOutStream);
				// Give the streams to the channel now that their names are appropriately set
				connection.get().getExecChannel().setOut(stdOutBufferedStream);
				connection.get().getExecChannel().setErr(stdErrStream);
			} catch (IOException e) {
				logger.error(e.getLocalizedMessage());
				return CommandStatus.FAILED;
			}

			try {
				connection.get().getExecChannel().open().verify();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return CommandStatus.RUNNING;
	}

	/**
	 * This function is responsible for setting up the file transfer logic,
	 * depending on whether or not this is a regular remote command or a jump host
	 * command. It is called from run and executes the logic in
	 * {@link org.eclipse.ice.commands.RemoteCommand#transferFiles(ConnectionConfiguration, String, String)}
	 * 
	 * @return - CommandStatus indicating whether or not the transfer(s) were
	 *         successful
	 * @throws IOException
	 * @throws SftpException
	 * @throws JSchException
	 */
	protected CommandStatus transferFiles() throws IOException {
		/**
		 * If we have a jump host connection then we need to transfer the files from the
		 * intermediary host to the local host first, and then transfer them from the
		 * local host through the forwarded connection. In other words, the file
		 * transferring goes from (in the case of System A --> System B --> System C
		 * where B is the jump host and C is the destination host):
		 * 
		 * System B --> System A - transfer the files to a local temp directory, System A
		 * --> System C - transfer the files from the local temp directory through the
		 * forwarded connection
		 */
		// So first we will transfer from the jump host
		if (secondConnection.get().getConfiguration() != null) {
			// Create a temporary local directory to move files from the jump host to
			// the local host
			Path tempLocalDir = Files.createTempDirectory("tempJumpDir");

			status = transferFiles(connectionConfig, commandConfig.getWorkingDirectory(), tempLocalDir.toString());
			// Check that this transfer completed successfully before moving to
			// transfer from A --> C
			if (!checkStatus(status))
				return CommandStatus.FAILED;
			// Set the working directory to be the temporary local directory that
			// was created, so that the rest of the command operates as normal
			// from a local host to a remote host with this new temporary directory
			status = transferFiles(secondConnection.get().getConfiguration(),
					tempLocalDir.toString(),
					commandConfig.getRemoteWorkingDirectory());

			// Delete the local temp file once finished moving to the destination host
			File localTempFile = new File(tempLocalDir.toString());
			boolean delete = deleteLocalDirectory(localTempFile);
			if (!delete) {
				logger.warn("Temporary directory at " + tempLocalDir.toString() + " couldn't be completely deleted.");
			}
			/**
			 * In order for the rest of the code base to use the forwarded connection as the
			 * job processing connection, we need to set the main connection of the class,
			 * i.e. {@link org.eclipse.ice.commands.Command#connection} as the forwarded
			 * connection
			 */
			connection = secondConnection;

		} else {
			// If this is not a jump host case, then just move the files from the local
			// working directory to the remote host like normal
			status = transferFiles(connectionConfig, commandConfig.getWorkingDirectory(),
					commandConfig.getRemoteWorkingDirectory());
		}

		return status;
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
	protected CommandStatus transferFiles(ConnectionConfiguration config, String sourceDir, String destDir) throws IOException {

		String sourceSep = "/";
		String destSep = "/";

		// Figure out what the separators are, depending on windows/*nix
		if(sourceDir.contains("\\"))
			sourceSep = "\\";
		if(destDir.contains("\\"))
			destSep = "\\";
		
		// Set up a remote file handler to transfer the files
		RemoteFileHandler handler = new RemoteFileHandler();
		// Give the handler the same connection as this command
		handler.setConnectionConfiguration(config);

		// Get the executable to concatenate
		String shortExecName = commandConfig.getExecutable();
		// Get the executable filename only by removing the all the junk in front of it
		// e.g. directory names, slashes, etc.
		if (shortExecName.contains("/"))
			shortExecName = shortExecName.substring(shortExecName.lastIndexOf("/") + 1);
		else if (shortExecName.contains("\\"))
			shortExecName = shortExecName.substring(shortExecName.lastIndexOf("\\") + 1);

		// Check that the source directory ends with the right separator
		if(!sourceDir.endsWith(sourceSep))
			sourceDir += sourceSep;
		
		// Do the same for the destination
		if (!destDir.endsWith(destSep))
			destDir += destSep;

		// Build the source and destination paths
		String source = sourceDir + shortExecName;
		String destination = destDir + shortExecName;

		logger.info("Trying to transfer " + source + " to " + destination +
				" over connection " + config.getName());
		
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
			// notifications about the progress of the transfer and use 0 to overwrite 
			// the files if they exist there already
			source = sourceDir + shortInputName;
			destination = destDir + shortInputName;
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
	private void deleteRemoteDirectory(SftpClient sftpChannel, String path) throws IOException {

		// Iterate through the list to get the file/directory names
		for (DirEntry file : sftpChannel.readDir(path)) {
			// If it isn't a directory delete it
			if (!file.getAttributes().isDirectory()) {
				sftpChannel.remove(path + "/" + file.getFilename());
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
	 * Recursive function that deletes a local directory and its contents
	 * 
	 * @param directory - directory to be deleted
	 * @return boolean - true if directory was deleted, false otherwise
	 */
	private boolean deleteLocalDirectory(File directory) {
		// Get the file list of the directory
		File[] contents = directory.listFiles();
		// If there are files/subdirectories in the directory, recursively iterate over
		// them to
		// delete them so that the directory can be deleted
		if (contents != null) {
			for (File file : contents) {
				deleteLocalDirectory(file);
			}
		}
		// Return whether or not the directory was deleted
		return directory.delete();
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
