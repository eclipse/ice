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

import com.jcraft.jsch.JSchException;

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
	public void setConfiguration(String src, String dest, ConnectionConfiguration config) {
		source = src;
		destination = dest;
		try {
			ConnectionManager.openConnection(config);
		} catch (JSchException e) {
			status = CommandStatus.INFOERROR;
			e.printStackTrace();
			return;
		}
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
		
		// Determine if the source file is on the local machine or on the remote machine
		
		
		return null;
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
	 * Set the source file string
	 * 
	 * @param src
	 */
	public void setSource(String src) {
		source = src;
	}

	/**
	 * Set the destination file string
	 * 
	 * @param dest
	 */
	public void setDestination(String dest) {
		destination = dest;
	}
}
