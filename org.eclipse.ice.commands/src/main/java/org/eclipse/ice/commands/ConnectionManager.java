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

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	 * A HashMap of available Connections to the ConnectionManager, organized by the
	 * name of the connection and the connection itself.
	 * 
	 */
	private AtomicReference<HashMap<String, Connection>> connectionList = new AtomicReference<HashMap<String, Connection>>();

	/**
	 * Logger for handling event messages and other information.
	 */
	private final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

	/**
	 * Default Constructor
	 */
	public ConnectionManager() {
		// Set the atomic reference hashmap to a default <String, Connection> hashmap
		HashMap<String, Connection> dummyMap = new HashMap<String, Connection>();
		connectionList.set(dummyMap);
	}

	/**
	 * Opens, and thus begins, a connection to a remote system. Also adds the
	 * connection to the list of connections given in
	 * {@link org.eclipse.ice.commands.ConnectionManager#connectionList}
	 * 
	 * @param config - ConnectionConfiguration to be used to open connection
	 * @return Connection - returns connection if successful, null otherwise
	 */
	public Connection openConnection(ConnectionConfiguration config) throws JSchException {

		// The new connection to be opened
		Connection newConnection = new Connection(config);

		// Create the shell
		newConnection.setJShellSession(new JSch());

		logger.info("Trying to open the connection");

		// Get the information necessary to open the connection
		if (newConnection.getConfiguration() != null) {
			ConnectionAuthorizationHandler auth = newConnection.getConfiguration().getAuthorization();

			// Get the password first. If authorization is a text file, then
			// username and hostname will be set. Otherwise user must set them
			char[] pwd = auth.getPassword();

			String username = auth.getUsername();
			String hostname = auth.getHostname();

			// Try go get and open the new session
			try {
				newConnection.setSession(newConnection.getJShellSession().getSession(username, hostname));
			} catch (JSchException e) {
				logger.error("Couldn't open session with given username and hostname. Exiting.");
				throw new JSchException();
			}

			// Pass it to the session
			newConnection.getSession().setPassword(String.valueOf(pwd));

			// Erase contents of pwd and fill with null
			Arrays.fill(pwd, Character.MIN_VALUE);

			// Set the authentication requirements
			newConnection.getSession().setConfig("StrictHostKeyChecking", "no");
			newConnection.getSession().setConfig("PreferredAuthentications", "password");

			// Connect the session
			try {
				newConnection.getSession().connect();
			} catch (JSchException e) {
				logger.error("Couldn't connect to session with given username and/or password. Exiting.");
				throw new JSchException();
			}

			// Add the connection to the list since it was successfully created
			connectionList.get().put(newConnection.getConfiguration().getName(), newConnection);

			logger.info("Connection at " + username + "@" + hostname + " established successfully");
			
			// Upon success, return the opened connection
			return newConnection;
		}

		// If the connectionConfiguration was not properly specified, or an error
		// occurred, return null
		return null;
	}

	/**
	 * This function finds the particular connection requested by name in the list
	 * of all connections and returns it.
	 * 
	 * @param connectionName - name of connection to search for
	 * @return - Connection instance which was requested
	 */
	public Connection getConnection(String connectionName) {
		// Find the hashmap instance, and return it
		Connection returnConnection = connectionList.get().get(connectionName);
		if (returnConnection == null) {
			logger.warn("The connection is null! Couldn't find a connection with the name " + connectionName);
		}
		return returnConnection;
	}

	/**
	 * Closes a particular connection as specified
	 * 
	 * @param connectionName - name of connection to be closed
	 */
	public void closeConnection(String connectionName) {

		// Get the connection that was passed
		Connection connection = getConnection(connectionName);

		// Disconnect the session. If the session was not connected in the first place,
		// it does nothing
		connection.getSession().disconnect();
		// Confirm with the logger
		logger.debug("Connection " + connectionName + "@" + connection.getConfiguration().getAuthorization().getHostname() + " closed");

		return;
	}

	/**
	 * This function removes a particular connection from the manager's connection
	 * list
	 * 
	 * @param connectionName - name of connection to remove
	 */
	public void removeConnection(String connectionName) {
		// Remove it from the list of connections
		connectionList.get().remove(connectionName);
	}

	/**
	 * This function removes all particular connections from the connection list
	 */
	public void removeAllConnections() {
		// First make sure all connections have been disconnected
		closeAllConnections();
		// Remove all of the items from the hashmap
		connectionList.get().clear();
	}

	/**
	 * Closes all connections that remain open.
	 */
	public void closeAllConnections() {
		// Iterate over all available connections in the list and disconnect
		for (Connection connection : connectionList.get().values()) {
			connection.getSession().disconnect();
		}

	}

	/**
	 * This function lists all the connections (and their statuses, i.e. if open or
	 * not) to the logger, if so desired. Useful for checking the connections and
	 * their statuses.
	 */
	public void listAllConnections() {
		// Iterate over all available connections
		for (String name : connectionList.get().keySet()) {
			// Build a message
			String msg = null;
			// Get the host for the connection
			String host = connectionList.get().get(name).getConfiguration().getAuthorization().getHostname();
			// Get the username for the connection
			String username = connectionList.get().get(name).getConfiguration().getAuthorization().getUsername();
			// Check the status. If it is open or closed (i.e. connected or disconnected)
			String status = "";
			if (isConnectionOpen(name))
				status = "open";
			else
				status = "closed";

			// Build a message to send to the logger
			msg = "Connection " + name + ": " + username + "@" + host + " is " + status;

			logger.info(msg);
		}

	}

	/**
	 * This function checks whether or not a particular connection is currently
	 * connected.
	 * 
	 * @param connectionName - name of connection to check
	 * @return - boolean indicating whether or not it is connected (true) or not
	 *         (false)
	 */
	public boolean isConnectionOpen(String connectionName) {
		Connection connection = getConnection(connectionName);
		return connection.getSession().isConnected();
	}

	/**
	 * Setter function for
	 * {@link org.eclipse.ice.commands.ConnectionManager#connectionList}
	 * 
	 * @param connections
	 */
	public void setConnectionList(HashMap<String, Connection> connectionList) {
		this.connectionList = new AtomicReference<HashMap<String, Connection>>(connectionList);
	}

	/**
	 * Getter function for
	 * {@link org.eclipse.ice.commands.ConnectionManager#connectionList}
	 * 
	 * @return
	 */
	public HashMap<String, Connection> getConnectionList() {
		return connectionList.get();
	}

}
