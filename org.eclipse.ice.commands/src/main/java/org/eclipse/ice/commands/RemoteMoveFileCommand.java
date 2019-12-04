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
	private HandleType moveType;

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
		// Create a RemoteTransferExecution
		RemoteTransferExecution transfer = new RemoteTransferExecution();
		// Tell it that this is a move
		transfer.isMove(true);
		// Do the transfer
		status = transfer.executeTransfer(getConnection(), source, destination, permissions, moveType);

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
	 * Set the move type variable
	 * 
	 * @param type - HandleType for this file transfer
	 */
	public void setMoveType(HandleType moveType) {
		this.moveType = moveType;
	}

	/**
	 * Set the permissions for a chmod during file transfer
	 * 
	 * @param permissions - file permissions for new file upon transfer
	 */
	protected void setPermissions(int permissions) {
		this.permissions = permissions;
	}
}
