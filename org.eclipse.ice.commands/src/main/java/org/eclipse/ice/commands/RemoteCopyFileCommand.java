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
 * Child class for copying a file remotely over some connection.
 * 
 * @author Joe Osborn
 *
 */
public class RemoteCopyFileCommand extends RemoteCommand {
	

	/**
	 * The path to the source file which is to be copied
	 */
	String source;

	/**
	 * The path of the destination for which the source file will be copied to
	 */
	String destination;

	
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
	
	@Override
	public CommandStatus execute() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected CommandStatus run() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommandStatus cancel() {
		// TODO Auto-generated method stub
		return null;
	}

}
