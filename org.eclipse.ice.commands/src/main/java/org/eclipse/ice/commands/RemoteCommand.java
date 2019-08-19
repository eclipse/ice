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
 * This class inherits from Command and gives available functionality for remote commands.
 * These could be ssh connections or a remote process.
 * @author Joe Osborn
 *
 */
public class RemoteCommand extends Command{

	
	/**
	 * The particular connection associated to a particular RemoteCommand.
	 * Declare this up front since by definition a RemoteCommand must have a connection.
	 */
	Connection connection = new Connection();
	
	
	/**
	 * Default constructor
	 */
	public RemoteCommand() {
		
	}
	
	/**
	 * Set a particular connection for a particular RemoteCommand
	 * @param connection - the connection for this command
	 */
	public void SetConnection(String connection) {
		
	}
	
	/**
	 * Return the connection associated to this RemoteCommand
	 * @return - the connection for this command
	 */
	public Connection GetConnection() {
		
		return connection;
	}

}
