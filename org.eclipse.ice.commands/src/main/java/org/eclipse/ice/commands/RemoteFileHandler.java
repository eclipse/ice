/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.commands;

import java.io.IOException;
import java.util.HashMap;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;

/**
 * This class inherits from FileHandler and handles the processing of remote
 * file transfer commands. Remote file transfers can be from a local-to-remote
 * (or vice versa) or remote-to-remote system.
 * 
 * @author Joe Osborn
 *
 */

public class RemoteFileHandler extends FileHandler {

	/**
	 * Map keys for identifying what kind of "remote" file move is happening
	 */
	HashMap<String, Integer> handleType = new HashMap<String, Integer>();

	/**
	 * An integer to determine what the actual handle type is to set for the command
	 */
	private int HANDLE_TYPE = 0;
	/**
	 * This is a boolean that indicates whether or not the source file is located on
	 * the local machine or the remote machine. True if local, false if remote.
	 */
	protected boolean localSrc;

	/**
	 * This is a boolean that indicates whether or not the destination directory is
	 * located on the local machine or the remote machine. True if local, false if
	 * remote.
	 */
	protected boolean localDest;

	/**
	 * A connection configuration that is associated with this remote file handler
	 */
	private ConnectionConfiguration connectionConfig;

	/**
	 * Default constructor
	 */
	public RemoteFileHandler() {

	}

	public void setConnectionConfiguration(ConnectionConfiguration config) {
		// Get the connection manager and open the connection in constructor so that it
		// is only performed once, thus the connection isn't constantly re-requiring
		// password authentication
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
		// Open the connection
		try {
			manager.openConnection(config);
		} catch (JSchException e) {
			logger.error("Connection could not be established.");
			e.printStackTrace();
		}

		// Set the member variable for access later
		connectionConfig = config;
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#exists(String)}
	 */
	@Override
	public boolean exists(String file) throws IOException {
		ChannelSftp sftpChannel = getConnection();
		try {
			sftpChannel.connect();
			// Try to lstat the path. If an exception is thrown, it means it does not exist
			SftpATTRS attrs = sftpChannel.lstat(file);
		} catch (JSchException | SftpException e) {
			logger.info("Couldn't find " + file + " remotely. Seeing if it exists locally.");
			if (isLocal(file)) {
				logger.info("File " + file + " exists locally.");
				return true;
			}
			e.printStackTrace();
			return false;
		}

		sftpChannel.disconnect();
		// If an exception is not thrown when lstat is performed, the path exists
		return true;

	}

	/**
	 * This is a helper function for exists which attempts to make a remote
	 * directory. It is called if the remote directory doesn't initially exist on
	 * the remote host.
	 * 
	 * @param file - file path to try and make
	 * @return - boolean indicating whether or not directory was successfully
	 *         created (true) or false if otherwise
	 */
	private boolean makeRemoteDirectory(String file) {
		logger.warn("Path doesn't exist on the remote host, trying to make it.");
		ChannelSftp sftpChannel = getConnection();
		try {
			// Connect the channel to try making the directory
			sftpChannel.connect();
			// Try to make the directory on the remote host
			sftpChannel.mkdir(file);
		} catch (JSchException | SftpException e) {
			logger.error("Couldn't make nonexistent remote directory, exiting.");
			e.printStackTrace();
			return false;
		}
		// Disconnect the channel when finished
		sftpChannel.disconnect();
		// If the try was successful, then directory was made
		return true;
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#checkExistence(String, String)}.
	 * 
	 * In the case of remote moves, this function checks the file existence and also
	 * determines what kind of remote move it is, i.e. which direction the move is
	 * going (local --> remote, remote --> local, remote --> remote, etc.)
	 * 
	 * @throws JSchException
	 */
	@Override
	public void checkExistence(String source, String destination) throws IOException {
		// Set the hash map for the different type of file handles
		handleType.put("localRemote", 1);
		handleType.put("remoteLocal", 2);
		handleType.put("remoteRemote", 3);

		// We need to determine what kind of remote move this is, i.e. if it is moving
		// from the remote host to the local host, vice versa, or moving on the remote
		// host to a new place on the remote host

		// The destination could have a full path plus a new file name, so we need
		// to get just the path for several existence checks
		String destinationPath = destination.substring(0, destination.lastIndexOf("/"));
		logger.info("Checking existence of source " + source);
		logger.info("Checking existence of destination path " + destinationPath);
		// If the source is local, then we know it must be a local --> remote handle
		if (isLocal(source)) {
			// Now check that the destination exists at the remote host
			if (exists(destinationPath)) {
				HANDLE_TYPE = handleType.get("localRemote");
			}
			// If remote directory doesn't exist, try to make it
			else {
				// If we can't, throw an error
				if (!makeRemoteDirectory(destinationPath)) {
					logger.error("Couldn't make remote destination, exiting.");
					command.setStatus(CommandStatus.FAILED);
					throw new IOException();
				}
			}

		}
		// Otherwise the source must be remote, so we need to determine if it is a
		// remote --> local or remote --> remote handle
		else {
			// First check if the remote source exists
			if (!exists(source)) {
				// If it doesn't, the source doesn't exist locally or remotely,
				// so there is an issue
				logger.error("The source file is remote, but can't be found!");
				command.setStatus(CommandStatus.FAILED);
				throw new IOException();
			}
			// Confirmed the remote source exists, so
			// now check if the destination is local or remote
			if (isLocal(destinationPath)) {
				// If the destination path exists locally, then it we'll download it
				HANDLE_TYPE = handleType.get("remoteLocal");
			} else {
				// If the local destination path doesn't exist, then it is a remote --> remote
				// move so check to make sure the remote destination exists
				if (!exists(destinationPath)) {
					// Try and make the destination path. If we can't, throw an error
					if (!makeRemoteDirectory(destinationPath)) {
						logger.error("The destination is remote, and doesn't exist and couldn't be made.");
						command.setStatus(CommandStatus.FAILED);
						throw new IOException();
					}
				}
				// Otherwise the destination was made and we are ready to move
				HANDLE_TYPE = handleType.get("remoteRemote");
			}
		}

		// If the handle type is still 0, then it was never set and thus couldn't be
		// found
		if (HANDLE_TYPE == 0) {
			logger.error("Can't find the source and/or destination file! Exiting.");
			command.setStatus(CommandStatus.INFOERROR);
			throw new IOException();
		}

		// Print out the determined handle type for informational purposes
		String handle = "";
		// Loop over the entries in the map and find the one that matches
		for (String type : handleType.keySet()) {
			if (handleType.get(type) == HANDLE_TYPE) {
				handle = type;
			}
		}

		logger.info(
				"FileHandler is moving/copying " + source + " to " + destination + " with the handle type " + handle);

	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#configureMoveCommand(String, String)}
	 */
	@Override
	protected void configureMoveCommand(String source, String destination) {
		Connection connection = getHandlerConnection();
		// Now instantiate the command as a RemoteMoveFileCommand
		command = new RemoteMoveFileCommand();

		// Set the command to have this connection and connection configuration
		command.setConnectionConfiguration(connection.getConfiguration());
		((RemoteCommand) command).setConnection(connection);

		// Cast the command as a remote move file command
		((RemoteMoveFileCommand) command).setConfiguration(source, destination);
		((RemoteMoveFileCommand) command).setMoveType(HANDLE_TYPE);
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#configureCopyCommand(String, String)}
	 */
	@Override
	protected void configureCopyCommand(String source, String destination) {
		Connection connection = getHandlerConnection();
		// Now instantiate the command as a RemoteMoveFileCommand
		command = new RemoteCopyFileCommand();

		// Set the command to have this connection and connection configuration
		command.setConnectionConfiguration(connection.getConfiguration());
		((RemoteCommand) command).setConnection(connection);
		// Cast the command as a remote command
		((RemoteCopyFileCommand) command).setConfiguration(source, destination);
		((RemoteCopyFileCommand) command).setCopyType(HANDLE_TYPE);
	}

	/**
	 * This function gets the connection information from the file handler for the
	 * command to be processed. It is important to do this up front in the command
	 * configuration, since for remote file handling the connection must be
	 * established.
	 * 
	 * @return - A connection corresponding to the connection configuration
	 *         associated to this remote file handler
	 */
	private Connection getHandlerConnection() {
		// We need to get the connection information with this file handler
		// and pass this information to the remote command in order for the
		// command to be run on the remote host.
		// First get the manager to get the connection
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
		String connectionName = connectionConfig.getName();
		Connection connection = manager.getConnection(connectionName);

		// Log some information
		logger.info("Configuring handle connection with name " + connectionName);

		return connection;
	}

	/**
	 * This function opens the connection, and keeps this code condensed and out of
	 * the way of the main "workhorse" functions
	 */
	private ChannelSftp getConnection() {
		try {
			// Get the connection associated with this configuration
			Connection connection = ConnectionManagerFactory.getConnectionManager()
					.getConnection(connectionConfig.getName());

			// Get the sftp channel to check existence
			ChannelSftp sftpChannel = (ChannelSftp) connection.getSession().openChannel("sftp");
			// Connect the channel
			return sftpChannel;
		} catch (JSchException e) {
			// If there was a Jsch connection problem, puke
			logger.error("Couldn't connect to the remote ssh connection!");
			command.setStatus(CommandStatus.INFOERROR);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * A getter function to return the list of possible remote file transfers
	 * 
	 * @return
	 */
	public HashMap<String, Integer> getHandleType() {
		return handleType;
	}

}
