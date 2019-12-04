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
	private HandleType copyType;

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
		// Create a RemoteTransferExecution
		RemoteTransferExecution transfer = new RemoteTransferExecution();
		// Tell it that this is a copy
		transfer.isMove(false);
		// Do the transfer
		status = transfer.executeTransfer(getConnection(), source, destination, permissions, copyType);

		return status;
	}

	/**
	 * Get the source file string
	 * 
	 * @return - string of the source path
	 */
	public String getSource() {
		return source;
	}

	/**
	 * Get the destination file string
	 * 
	 * @return - string of the destination path
	 */
	public String getDestination() {
		return destination;
	}

	/**
	 * Set the copy type variable
	 * 
	 * @param type - HandleType for this file transfer
	 */
	public void setCopyType(HandleType copyType) {
		this.copyType = copyType;
	}

	/**
	 * Set the permissions for a chmod during file transfer
	 * 
	 * @param permissions - file permissions to be set upon file 
	 * 						transfer
	 */
	protected void setPermissions(int permissions) {
		this.permissions = permissions;
	}
}
