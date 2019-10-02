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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;

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
	 * Default constructor
	 */
	public RemoteFileHandler() {
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#exists(String)}
	 */
	@Override
	public boolean exists(String file) throws IOException {
		return false;
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#checkExistence(String, String)}.
	 * 
	 * In the case of remote moves, this function checks the file existence and also
	 * determines what kind of remote move it is, i.e. which direction the move is
	 * going (local --> remote, remote --> local, remote --> remote, etc.)
	 */
	@Override
	public void checkExistence(String source, String destination) throws IOException {
		// Open the connection, since it hasn't been opened yet as this is the first
		// step in the file handle
		openConnection();

		// We need to determine what kind of remote move this is. So check the
		// existence of the source and destination to determine where they live.
		localSrc = isLocal(source);
		// If the file doesn't exist locally, check if it exists remotely
		if (localSrc == false) {
			localSrc = exists(source);
		}

		// Do the same for the destination
		localDest = isLocal(destination);
		// If the destination doesn't exist locally
		if (localDest == false) {
			localDest = exists(destination);
		}

		// If either the localSrc or localDest is still false, we can't find one of the
		// paths
		// Therefore this file handle will fail.
		if (localSrc == false && localDest == false) {
			logger.error("Can't find the source and/or destination file! Exiting.");
			command.setStatus(CommandStatus.INFOERROR);
			throw new IOException();
		}
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#configureMoveCommand(String, String)}
	 */
	@Override
	protected void configureMoveCommand(String source, String destination) {
		command = new RemoteMoveFileCommand();
		// Cast the command as a remote command
		((RemoteMoveFileCommand) command).setConfiguration(source, destination);

	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#configureCopyCommand(String, String)}
	 */
	@Override
	protected void configureCopyCommand(String source, String destination) {
		command = new RemoteCopyFileCommand();
		// Cast the command as a remote command
		((RemoteCopyFileCommand) command).setConfiguration(source, destination);

	}

	/**
	 * Function to determine whether or not a given string is located on the local
	 * machine
	 * 
	 * @param file
	 * @return
	 */
	private boolean isLocal(String file) {
		// Get the path
		Path path = Paths.get(file);
		// Return whether or not it exists on the local machine
		return Files.exists(path);

	}

	/**
	 * This function opens the connection, and keeps this code condensed and out of
	 * the way of the main "workhorse" functions
	 */
	private void openConnection() {
		try {
			// Open the connection
			ConnectionManager.openConnection(command.getConnectionConfiguration());
			// Get the sftp channel to check existence
			ChannelSftp sftpChannel = (ChannelSftp) ((RemoteCommand) command).getConnection().getSession()
					.openChannel("sftp");
			// Connect the channel
			sftpChannel.connect();
		} catch (JSchException e) {
			// If there was a Jsch connection problem, puke
			logger.error("Couldn't connect to the remote ssh connection!");
			command.setStatus(CommandStatus.INFOERROR);
			e.printStackTrace();
		}
	}

}
