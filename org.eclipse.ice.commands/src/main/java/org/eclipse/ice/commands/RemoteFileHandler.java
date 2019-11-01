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
	 * An integer with the permissions for a new file to be changed with chmod, if
	 * desired. Can be set via a setter and then the moved or copied file will also
	 * have its permissions modified. Default to -999 so that we can easily check if
	 * it was instantiated by the user or not.
	 */
	private int permissions = -999;

	/**
	 * Default constructor
	 */
	public RemoteFileHandler() {
		// Define and set the hash map for the different type of file handles
		handleType.put("localRemote", 1);
		handleType.put("remoteLocal", 2);
		handleType.put("remoteRemote", 3);
	}

	/**
	 * This function sets the connection configuration for the file handler. It
	 * checks if the connection is already open, and if it is not it calls the
	 * connection manager to open the connection
	 * 
	 * @param config
	 */
	public void setConnectionConfiguration(ConnectionConfiguration config) {
		// Get the connection manager and open the connection in constructor so that it
		// is only performed once, thus the connection isn't constantly re-requiring
		// password authentication
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

		// First check if there is already an existing connection open with these
		// details
		if (manager.getConnection(config.getName()) == null) {
			// If there isn't one open, try to open the connection
			try {
				logger.info("Manager is opening a connection");
				connection = manager.openConnection(config);
			} catch (JSchException e) {
				logger.error("Connection could not be established. Transfer will fail.");
			}
		} else {
			// Get the connection if it is already available
			connection = manager.getConnection(config.getName());
		}
		// Open an sftp channel for this remote file handler to use
		try {
			// Set it for the connection
			if (connection.getChannel() == null) {
				connection.setChannel(connection.getSession().openChannel("sftp"));
				connection.getChannel().connect();
			}
		} catch (JSchException e) {
			logger.error(
					"Connection seems to have an unopened channel, but there was a failure when trying to open the channel.");
		}
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#exists(String)}
	 */
	@Override
	public boolean exists(String file) throws IOException {

		ChannelSftp sftpChannel = null;
		try {
			// Get the sftp channel to check existence
			sftpChannel = (ChannelSftp) connection.getChannel();

			// Try to lstat the path. If an exception is thrown, it means it does not exist
			SftpATTRS attrs = sftpChannel.lstat(file);
		} catch (SftpException e) {
			if (isLocal(file)) {
				// If the file can be found locally, return true since we found it.
				// Up to checkExistence to determine what kind of move this is (e.g. local->remote
				// or vice versa)
				return true;
			} else {
				return false;
			}
		}

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
		ChannelSftp sftpChannel = null;
		try {

			// Get the sftp channel to check existence
			sftpChannel = (ChannelSftp) connection.getChannel();
			// Try to make the directory on the remote host
			// Could be many directories, so we need to iterate over each piece
			// of the path and see if it exists. If it doesn't, then make it.
			String[] directories = file.split("/");
			String directory = "";
			for (int i = 0; i < directories.length; i++) {
				// Add the next directory to the full path name
				directory += "/" + directories[i];
				// Try to ls the directory. If it throws an exception, then
				// it doesn't exist, so we should make it
				try {
					SftpATTRS attrs = sftpChannel.lstat(directory);
				} catch (SftpException e) {
					sftpChannel.mkdir(directory);
				}
			}

			logger.info("Made new remote directory");
		} catch (SftpException e) {
			logger.error("Couldn't make nonexistent remote directory, exiting.");
			return false;
		} finally {

		}
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

		// We need to determine what kind of remote move this is, i.e. if it is moving
		// from the remote host to the local host, vice versa, or moving on the remote
		// host to a new place on the remote host

		// The destination could have a full path plus a new file name, so we need
		// to get just the path for several existence checks
		String destinationPath = "";
		try{
			// Check for *nix based systems
			destinationPath = destination.substring(0, destination.lastIndexOf("/"));
		} catch(StringIndexOutOfBoundsException e) {
			// If that throws an exception, try for a windows type system
			destinationPath = destination.substring(0, destination.lastIndexOf("\\"));
		}

		// If the source is local, then we know it must be a local --> remote handle
		if (isLocal(source)) {
			// Now check that the destination exists at the remote host
			if (exists(destinationPath)) {
				HANDLE_TYPE = handleType.get("localRemote");
			}
			// If remote directory doesn't exist, try to make it
			else {
				// If we can make it, great
				if (makeRemoteDirectory(destinationPath)) {
					HANDLE_TYPE = handleType.get("localRemote");
				} else {
					// If we can't make the directory, throw an error
					logger.error("Couldn't make remote destination, exiting.");
					command.get().setStatus(CommandStatus.FAILED);
					throw new IOException();
				}
			}

		} else {
			// Otherwise the source must be remote, so we need to determine if it is a
			// remote --> local or remote --> remote handle
			// First check if the remote source exists
			if (!exists(source)) {
				// If it doesn't, the source doesn't exist locally or remotely,
				// so there is an issue
				logger.error("The source file is remote, but can't be found!");
				command.get().setStatus(CommandStatus.FAILED);
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
						command.get().setStatus(CommandStatus.FAILED);
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
			command.get().setStatus(CommandStatus.INFOERROR);
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
		// Now instantiate the command as a RemoteMoveFileCommand
		RemoteMoveFileCommand cmd = new RemoteMoveFileCommand();
		// Configure the command
		cmd.setConfiguration(source, destination);
		cmd.setMoveType(HANDLE_TYPE);
		cmd.setPermissions(permissions);
		// Set the command to have this connection and connection configuration
		cmd.setConnectionConfiguration(connection.getConfiguration());
		cmd.setConnection(connection);
		
		// Now set the member variable of type Command
		command.set(cmd);
	
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#configureCopyCommand(String, String)}
	 */
	@Override
	protected void configureCopyCommand(String source, String destination) {
		// Now instantiate the command as a RemoteMoveFileCommand
		RemoteCopyFileCommand cmd = new RemoteCopyFileCommand();
		// Configure the command
		cmd.setConfiguration(source, destination);
		cmd.setCopyType(HANDLE_TYPE);
		cmd.setPermissions(permissions);
		// Set the command to have this connection and connection configuration
		cmd.setConnectionConfiguration(connection.getConfiguration());
		cmd.setConnection(connection);
		command.set(cmd);

	}

	public void disconnect() {
		connection.getChannel().disconnect();
		connection.getSession().disconnect();
	}

	/**
	 * A getter function to return the list of possible remote file transfers
	 * 
	 * @return
	 */
	public HashMap<String, Integer> getHandleType() {
		return handleType;
	}

	/**
	 * A setter for the value of the permission to change a particular file once
	 * transferred. See also
	 * {@link org.eclipse.ice.commands.RemoteFileHandler#permissions}. Takes a
	 * string of octal type and converts it to an integer of decimal type, since
	 * JSch takes decimal type.
	 * 
	 * NOTE: JSch takes a decimal number, not an octal number like one would
	 * normally expect with chmod. This function takes in an octal number, like
	 * normal chmod, and changes it to decimal.
	 * 
	 * 
	 * @param permissions
	 */
	public void setPermissions(String permissions) {
		this.permissions = Integer.parseInt(permissions, 8);
	}

}
