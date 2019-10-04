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
	private static final int LOCAL_REMOTE  = 0;
	private static final int REMOTE_LOCAL  = 1;
	private static final int REMOTE_REMOTE = 2;
	private int HANDLE_TYPE;
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
	public RemoteFileHandler(ConnectionConfiguration config) {
		// Command needs to be instantiated in the constructor so that the connection
		// can be set and thus established while avoiding a null pointer
		command = new RemoteCommand();
		command.setConnectionConfiguration(config);
		
		// Get the connection manager and open the connection in constructor so that it 
		// is only performed once, thus the connection isn't constantly re-requiring 
		// password authentication
		ConnectionManager manager = ((RemoteCommand)command).getConnectionManager();
		// Open the connection
		try {
			((RemoteCommand)command).setConnection(manager.openConnection(command.getConnectionConfiguration()));
		} catch (JSchException e) {
			logger.error("Connection could not be established.");
			e.printStackTrace();
		}
	}

	/**
	 * See {@link org.eclipse.ice.commands.FileHandler#exists(String)}
	 */
	@Override
	public boolean exists(String file) throws IOException {
		ChannelSftp sftpChannel = getConnection();
		try {
			sftpChannel.connect();
		} catch (JSchException e) {
			logger.error("Couldn't connect to remote host, exiting.");
			e.printStackTrace();
			return false;
		}
		// Try to lstat the path. If an exception is thrown, it means it does not exist
		try {
			SftpATTRS attrs = sftpChannel.lstat(file);
		} catch (SftpException e) {
			logger.error("Path doesn't exist on the remote host, exiting.");
			e.printStackTrace();
			sftpChannel.disconnect();
			return false;
		}
		
		sftpChannel.disconnect();
		// If an exception is not thrown when lstat is performed, the path exists
		return true;
		
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#checkExistence(String, String)}.
	 * 
	 * In the case of remote moves, this function checks the file existence and also
	 * determines what kind of remote move it is, i.e. which direction the move is
	 * going (local --> remote, remote --> local, remote --> remote, etc.)
	 * @throws JSchException 
	 */
	@Override
	public void checkExistence(String source, String destination) throws IOException {
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
		// paths. Therefore this file handle will fail.
		if (localSrc == false && localDest == false) {
			logger.error("Can't find the source and/or destination file! Exiting.");
			command.setStatus(CommandStatus.INFOERROR);
			throw new IOException();
		}
		
		// Set the type of move it is, which will determine how the command classes
		// actually execute the file transfer
		else if(localSrc == false && localDest == true)
			HANDLE_TYPE = REMOTE_LOCAL;
		else if(localSrc == true && localDest == false)
			HANDLE_TYPE = LOCAL_REMOTE;
		else
			HANDLE_TYPE = REMOTE_REMOTE;
		
	
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#configureMoveCommand(String, String)}
	 */
	@Override
	protected void configureMoveCommand(String source, String destination) {
		// Cast the command as a remote command
		((RemoteMoveFileCommand) command).setConfiguration(source, destination);
		((RemoteMoveFileCommand) command).setMoveType(HANDLE_TYPE);
	}

	/**
	 * See
	 * {@link org.eclipse.ice.commands.FileHandler#configureCopyCommand(String, String)}
	 */
	@Override
	protected void configureCopyCommand(String source, String destination) {
		// Cast the command as a remote command
		((RemoteCopyFileCommand) command).setConfiguration(source, destination);
		((RemoteCopyFileCommand) command).setCopyType(HANDLE_TYPE);
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
	private ChannelSftp getConnection() {
		try {
			Connection connection = ((RemoteCommand)command).getConnection();
			((RemoteCommand)command).setConnection(connection);
			// Get the sftp channel to check existence
			ChannelSftp sftpChannel = (ChannelSftp) ((RemoteCommand) command).getConnection().getSession()
					.openChannel("sftp");
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

}
