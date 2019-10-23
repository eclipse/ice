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
 * Child class for copying a file remotely over some connection.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteCopyFileCommand extends RemoteCommand {

	/**
	 * The path to the source file which is to be copied
	 */
	private String source;

	/**
	 * The path of the destination for which the source file will be copied to
	 */
	private String destination;

	/**
	 * An int which determines what kind of file handle type it is, e.g.
	 * local->remote, remote->local, or remote->remote
	 */
	private int copyType;

	/**
	 * See {@link org.eclipse.ice.commands.RemoteFileHandler#setPermissions(String)}
	 */
	private int permissions = -999;

	/**
	 * Default constructor
	 */
	public RemoteCopyFileCommand() {
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
		ChannelSftp channel = null;
		try {
			// Open the channel and connect it
			channel = (ChannelSftp) getConnection().getChannel();

			// Determine how to proceed given what kind of copy it is
			if (copyType == 1) { // If move type is local -> remote, use put
				channel.put(source, destination);
			} else if (copyType == 2) { // if move type is remote -> local, use get
				channel.get(source, destination);
			} else if (copyType == 3) { // if move type is remote -> remote, call function
				copyRemoteToRemote();
			} else {
				logger.info("Unknown handle type...");
				status = CommandStatus.FAILED;
				return status;
			}

			// If permissions was actually instantiated and isn't the default, then perform
			// a chmod
			if (permissions != -999)
				channel.chmod(permissions, destination);

		} catch (JSchException | SftpException e) {
			logger.error("Failed to connect to remote host. Returning failed.");
			status = CommandStatus.FAILED;
			return status;
		}

		// Set the status to success and return
		status = CommandStatus.SUCCESS;
		return status;
	}

	/**
	 * This is a function that executes a copy command on the remote host to copy a
	 * file from one location on the remote host to another location on the remote
	 * host.
	 * 
	 * @throws JSchException
	 */
	private void copyRemoteToRemote() throws JSchException {
		// Open an execution channel
		ChannelExec execChannel = (ChannelExec) getConnection().getSession().openChannel("exec");
		// TODO - test with windows, cp probably won't work
		// Make a copy command to execute
		String command = "cp " + source + " " + destination;
		// Set the command for the JSch connection
		execChannel.setCommand(command);
		// If the channel isn't connected, connect and run the command
		try {
			execChannel.connect();
		} catch (JSchException e) {
			logger.error("Channel isn't connected and can't copy remote to remote...");
			throw e;
		}
		// Disconnect extra channel when finished
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
	 * Set the copy type variable
	 * 
	 * @param type
	 */
	public void setCopyType(int copyType) {
		this.copyType = copyType;
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
