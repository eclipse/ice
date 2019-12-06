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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * This class performs the remote transfer execution for a remote copy or move
 * command It is called from RemoteCopyFileCommand or RemoteMoveFileCommand,
 * after the RemoteFileTransfer configures this command.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteTransferExecution {
	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(RemoteTransferExecution.class);

	/**
	 * A status to track the status of the execution
	 */
	private CommandStatus status;

	/**
	 * A boolean indicating whether or not the transfer is a move or a copy. True
	 * for move, false for copy
	 */
	private boolean isMove;

	/**
	 * Default constructor
	 */
	public RemoteTransferExecution() {
	}

	/**
	 * This function actually performs the logic to transfer a file over an sftp
	 * channel to or from a remote host, given a particular handle type.
	 * 
	 * @param connection   - Connection over which to transfer files
	 * @param source       - source path
	 * @param destination  - destination path
	 * @param permissions  - permissions for the remote file (if the file
	 *                     destination is remote)
	 * @param transferType - the HandleType indicating what kind of transfer it is
	 * @return - CommandStatus indicating whether or not the transfer was successful
	 */
	protected CommandStatus executeTransfer(Connection connection, String source, String destination, int permissions,
			HandleType transferType) {
		ChannelSftp channel = null;
		try {
			channel = connection.getSftpChannel();
			// Determine how to proceed given what kind of copy it is
			if (transferType == HandleType.localRemote) { // If move type is local -> remote, use put
				channel.put(source, destination);
			} else if (transferType == HandleType.remoteLocal) { // if move type is remote -> local, use get
				channel.get(source, destination);
			} else if (transferType == HandleType.remoteRemote) { // if move type is remote -> remote, call function
				transferRemoteToRemote(connection, source, destination);
			} else {
				logger.info("Unknown handle type...");
				status = CommandStatus.FAILED;
				return status;
			}
			logger.info("DESTINATION IS " + destination);
			// If permissions was actually instantiated and isn't the default, then perform
			// a chmod
			if (permissions != -999 && transferType != HandleType.remoteLocal)
				channel.chmod(permissions, destination);

		} catch (JSchException | SftpException e) {
			logger.error("Failed to connect or obtain file to/from remote host. Returning failed.", e);
			status = CommandStatus.FAILED;
			return status;
		}

		// Set the status to success and return
		status = CommandStatus.SUCCESS;
		return status;
	}

	/**
	 * This is a function that executes a transfer command on the remote host to
	 * copy a file from one location on the remote host to another location on the
	 * remote host.
	 * 
	 * @param connection  - Connection with which to transfer
	 * @param source      - source path
	 * @param destination - destination path
	 * @throws JSchException
	 */
	private void transferRemoteToRemote(Connection connection, String source, String destination) throws JSchException {
		// Open an execution channel
		ChannelExec execChannel = (ChannelExec) connection.getSession().openChannel("exec");
		// Make a transfer command to execute
		// First check if it is a move or copy
		String moveType = "cp ";
		if (isMove)
			moveType = "mv ";

		// Build the command to execute
		String command = moveType + source + " " + destination;
		// Set the command for the JSch connection
		execChannel.setCommand(command);
		// If the channel isn't connected, connect and run the command
		try {
			execChannel.connect();
		} catch (JSchException e) {
			logger.error("Channel isn't connected and can't copy remote to remote...", e);
			throw e;
		}
		// Disconnect extra channel when finished
		execChannel.disconnect();
	}

	/**
	 * Setter function to tell the class whether or not the transfer is a move or a
	 * copy
	 * 
	 * @param isMove - true if it is a move, false if it is a copy
	 */
	protected void isMove(boolean isMove) {
		this.isMove = isMove;
	}

}
