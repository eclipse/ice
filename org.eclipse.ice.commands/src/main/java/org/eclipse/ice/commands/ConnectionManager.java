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
import java.util.ArrayList;

/**
 * This factory class manages remote connections, and as such interfaces with all classes
 * that are associated with remote connections.
 * @author Joe Osborn
 *
 */
public class ConnectionManager {

	
	/**
	 * An ArrayList of available Connections to the ConnectionManager
	 */
	protected ArrayList<Connection> connectionList = new ArrayList<Connection>();
	
	
	
	/**
	 * Default Constructor
	 */
	public ConnectionManager() {
		
	}
	
	/**
	 * Opens, and thus begins, a connection to either a remote system or process
	 * @param connection - connection to be opened
	 * @return Connection - returns connection
	 */
	public static Connection openConnection(String connection) {
		return null;
	}

	/**
	 * Gets the connection from an already opened connection.
	 * @param connection - connection to be returned having already been instantiated
	 * @return Connection - returns connection asked for
	 */
	public static Connection getConnection(String connection) {
		return null;
	}
	
	/**
	 * Closes a particular connection as specified
	 * @param connection - Connection to be closed
	 * @return boolean - returns true if connection was successfully closed, otherwise false
	 */
	public boolean closeConnection(Connection connection) {
		
		return false;
	}
	
	/**
	 * Closes all connections that remain open.
	 * @return - true if successfully closed all connections, otherwise false
	 */
	public boolean closeAllConnections() {
		return false;
	}
	
	
	
}
