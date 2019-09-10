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
import java.util.concurrent.atomic.AtomicReference;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;

/**
 * This factory class manages remote connections, and as such interfaces with
 * all classes that are associated with remote connections.
 * 
 * @author Joe Osborn
 *
 */
public class ConnectionManager {

	/**
	 * An ArrayList of available Connections to the ConnectionManager
	 */
	protected static ArrayList<Connection> connectionList = new ArrayList<Connection>();

	/**
	 * Default Constructor
	 */
	public ConnectionManager() {

	}

	/**
	 * Opens, and thus begins, a connection to a remote system
	 * 
	 * @param connection - connection to be opened
	 * @return Connection - returns connection
	 */
	public static Connection openConnection(ConnectionConfiguration config) throws JSchException {
		AtomicReference<ConnectionConfiguration> atomicConfig = new AtomicReference<ConnectionConfiguration>(config);

		// The new connection to be opened
		Connection newConnection = new Connection(atomicConfig);

		// Create the shell
		newConnection.setJShellSession(new JSch());

		// Get the information necessary to open the connection
		if (newConnection.getConfiguration() != null) {
			String username = newConnection.getConfiguration().getUsername();
			String hostname = newConnection.getConfiguration().getHostname();

			// Try go get and open the new session
			try {
				newConnection.setSession(newConnection.getJShellSession().getSession(username, hostname));
			} catch (JSchException e) {
				throw new JSchException();
			}
			// Set the password
			newConnection.getSession().setPassword(config.getPassword());

			// Set the authentication requirements
			newConnection.getSession().setConfig("StrictHostKeyChecking", "no");
			newConnection.getSession().setConfig("PreferredAuthentications", "password");

			// Connect the session
			try {
				newConnection.getSession().connect();
			} catch (JSchException e) {
				throw new JSchException();
			}

			// Add the connection to the list
			connectionList.add(newConnection);

			// Upon success, return the opened connection
			return newConnection;
		}
		// If the connectionConfiguration was not properly specified, return null
		else
			return null;
	}

	/**
	 * This function finds the particular connection requested by name in the list
	 * of all connections and returns it.
	 * 
	 * @param connectionName - name of connection to search for
	 * @return
	 */
	public static Connection getConnection(String connectionName) {
		Connection returnConnection = null;
		for (int i = 0; i < connectionList.size(); i++) {
			if (connectionList.get(i).getConfiguration().getName().equals(connectionName))
				returnConnection = connectionList.get(i);
		}
		return returnConnection;
	}

	/**
	 * Closes a particular connection as specified
	 * 
	 * @param connection - Connection to be closed
	 * @return boolean - returns true if connection was successfully closed,
	 *         otherwise false
	 */
	public static void closeConnection(String connectionName) {

		// Get the connection that was passed
		Connection connection = getConnection(connectionName);

		// Disconnect the session. If the session was not connected in the first place,
		// it does nothing
		connection.getSession().disconnect();
		return;
	}

	/**
	 * Closes all connections that remain open.
	 * 
	 * @return - true if successfully closed all connections, otherwise false
	 */
	public static void closeAllConnections() {
		for (int i = 0; i < connectionList.size(); i++) {
			connectionList.get(i).getSession().disconnect();
		}
		return;
	}

}
