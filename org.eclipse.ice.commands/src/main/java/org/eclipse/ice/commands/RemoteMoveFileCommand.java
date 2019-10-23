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

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * Child class for remotely moving a file over some connection
 * 
 * @author Joe Osborn
 *
 */
public class RemoteMoveFileCommand extends RemoteCommand {

	/**
	 * The path to the source file which is to be copied
	 */
	private String source;

	/**
	 * The path of the destination for which the source file will be copied to
	 */
	private String destination;

	/**
	 * The type of move being performed, i.e. local-->remote, remote-->local, or
	 * remote-->remote
	 */
	private int moveType;

	/**
	 * See {@link org.eclipse.ice.commands.RemoteFileHandler#setPermissions(String)}
	 */
	private int permissions = -999;

	/**
	 * Default constructor
	 */
	public RemoteMoveFileCommand() {
	}

	/**
	 * Function which sets the two paths, source and destination, to those given by
	 * the arguments. The ConnectionConfiguration also gives the remote connection
	 * configuration for setting up the ssh and sftp channels.
	 * 
	 * @param src  - source file to be moved
	 * @param dest - destination for source file to be moved to
	 */
	public void setConfiguration(String src, String dest) {
		source = src;
		destination = dest;

	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#execute()}
	 */
	@Override
	public CommandStatus execute() {
		status = run();
		return status;
	}

	/**
	 * See {@link org.eclipse.ice.commands.Command#run()}
	 */
	@Override
	protected CommandStatus run() {
		// Try to open the channel to transfer the file first
		ChannelSftp channel = null;
		try {
			// Open the channel and connect it
			channel = (ChannelSftp) getConnection().getChannel();

			// Determine the move type and, thus, how to move the file
			if (moveType == 1) { // If move type is local -> remote, use put
				channel.put(source, destination);
			} else if (moveType == 2) { // if move type is remote -> local, use get
				channel.get(source, destination);
			} else if (moveType == 3) { // if move type is remote -> remote, call function
				moveRemoteToRemote();
			} else {
				logger.error("Unknown move type...");
				status = CommandStatus.FAILED;
				return status;
			}
			// If permissions was actually instantiated and isn't the default, then perform
			// a chmod
			if (permissions != -999)
				channel.chmod(permissions, destination);

		} catch (JSchException | SftpException e) {
			logger.error("Remote move failed. Returning failed.");
			status = CommandStatus.FAILED;
			return status;
		}

		// Set status to completed and successful
		status = CommandStatus.SUCCESS;
		return status;
	}

	/**
	 * This is a function that contains the logic to move a file from a remote
	 * destination to another remote destination, where the remote destination is
	 * the same
	 * 
	 * @throws JSchException
	 */
	private void moveRemoteToRemote() throws JSchException {
		// Open an executable channel
		ChannelExec execChannel = (ChannelExec) getConnection().getSession().openChannel("exec");
		// TODO - test with windows, mv probably won't work
		// Make a move command
		String command = "mv " + source + " " + destination;
		// Set the command
		execChannel.setCommand(command);
		// If the channel isn't connected, connect and run the command
		try {
			execChannel.connect();
		} catch (JSchException e) {
			logger.error("Channel isn't connected and can't copy remote to remote...");
			throw e;
		}

		// Disconnect extra channel once finished
		execChannel.disconnect();

	}

	/**
	 * Get the source file string
	 * 
	 * @return
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Get the destination file string
	 * 
	 * @return
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Set the move type variable
	 * 
	 * @param type
	 */
	public void setMoveType(int moveType) {
		this.moveType = moveType;
	}

	/**
	 * Set the permissions for a chmod during file transfer
	 * 
	 * @param permissions
	 */
	protected void setPermissions(int permissions) {
		this.permissions = permissions;
	}
}
