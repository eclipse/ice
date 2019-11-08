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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.HostKey;
import com.jcraft.jsch.HostKeyRepository;
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
	private HashMap<String, Connection> connectionList = new HashMap<String, Connection>();

	/**
	 * Logger for handling event messages and other information.
	 */
	private final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

	/**
	 * A boolean that the user can set to disable ssh StrictHostKeyChecking.
	 * Set to true by default since this is the most secure way.
	 */
	private boolean requireStrictHostKeyChecking = true;
	
	/**
	 * Default Constructor
	 */
	public ConnectionManager() {
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
		JSch jsch = new JSch();
		// TODO check for windows, wherever ~/.ssh is located in windows
		jsch.setKnownHosts(System.getProperty("user.home") + "/.ssh/known_hosts");
		logger.info(System.getProperty("user.home") + "/.ssh/known_hosts");
		newConnection.setJShellSession(jsch);

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
				logger.error("Couldn't open session with given username and hostname. Exiting.", e);
				throw new JSchException();
			}

			// Pass it to the session
			newConnection.getSession().setPassword(String.valueOf(pwd));

			// Erase contents of pwd and fill with null
			Arrays.fill(pwd, Character.MIN_VALUE);

			// JSch default requests ssh-rsa host checking, but some keys
			// request ecdsa-sha2-nistp256. So loop through the available
			// host keys that were grabbed from known_hosts and check what
			// type the user-given hostname needs
			HostKeyRepository hkr = jsch.getHostKeyRepository();
			for (HostKey hk : hkr.getHostKey()) {
				// If this hostkey contains the hostname that was supplied by
				// the user
				if (hk.getHost().contains(hostname)) {
					String type = hk.getType();
					// Set the session configuration key type to that hosts type
					newConnection.getSession().setConfig("server_host_key", type);
				}
			}

			// Set the authentication requirements
			newConnection.getSession().setConfig("PreferredAuthentications", "publickey,password");

			// If the user wants to disable StrictHostKeyChecking, add it to the
			// session configuration
			if(!requireStrictHostKeyChecking)
				newConnection.getSession().setConfig("StrictHostKeyChecking", "no");
			
			// Connect the session
			try {
				newConnection.getSession().connect();
			} catch (JSchException e) {
				logger.error("Couldn't connect to session with given username and/or password. Exiting.", e);
				throw new JSchException();
			}

			// Add the connection to the list since it was successfully created
			connectionList.put(newConnection.getConfiguration().getName(), newConnection);

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
		Connection returnConnection = connectionList.get(connectionName);
		if (returnConnection == null) {
			logger.warn("Couldn't find an existing connection with the name " + connectionName + ".");
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

		// Check the channels first
		if (connection.getExecChannel() != null) {
			if (connection.getExecChannel().isConnected()) {
				connection.getExecChannel().disconnect();
			}
		}
		if (connection.getSftpChannel() != null) {
			if (connection.getSftpChannel().isConnected()) {
				connection.getSftpChannel().disconnect();
			}
		}
		// Disconnect the session. If the session was not connected in the first place,
		// it does nothing
		connection.getSession().disconnect();
		// Confirm with the logger
		logger.debug("Connection " + connectionName + "@"
				+ connection.getConfiguration().getAuthorization().getHostname() + " closed");

		return;
	}

	/**
	 * This function closes/disconnects and removes a particular connection from the
	 * manager's connection list
	 * 
	 * @param connectionName - name of connection to remove
	 */
	public void removeConnection(String connectionName) {
		// Close the connection first
		if (!isConnectionOpen(connectionName))
			closeConnection(connectionName);
		// Remove it from the list of connections
		connectionList.remove(connectionName);
	}

	/**
	 * This function removes all particular connections from the connection list
	 */
	public void removeAllConnections() {
		// First make sure all connections have been disconnected
		closeAllConnections();
		// Remove all of the items from the hashmap
		connectionList.clear();
	}

	/**
	 * Closes all connections that remain open.
	 */
	public void closeAllConnections() {
		// Iterate over all available connections in the list and disconnect
		for (Connection connection : connectionList.values()) {
			if (connection.getExecChannel() != null) {
				if (connection.getExecChannel().isConnected()) {
					connection.getExecChannel().disconnect();
				}
			}
			if (connection.getSftpChannel() != null) {
				if (connection.getSftpChannel().isConnected()) {
					connection.getSftpChannel().disconnect();
				}
			}
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
		for (String name : connectionList.keySet()) {
			// Build a message
			String msg = null;
			// Get the host for the connection
			String host = connectionList.get(name).getConfiguration().getAuthorization().getHostname();
			// Get the username for the connection
			String username = connectionList.get(name).getConfiguration().getAuthorization().getUsername();
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
		this.connectionList = connectionList;
	}

	/**
	 * Getter function for
	 * {@link org.eclipse.ice.commands.ConnectionManager#connectionList}
	 * 
	 * @return
	 */
	public HashMap<String, Connection> getConnectionList() {
		return connectionList;
	}
	
	/**
	 * Setter for whether or not connections should be open with the requirement
	 * of StrictHostKeyChecking
	 * @param requireStrictHostKeyChecking
	 */
	public void setrequireStrictHostKeyChecking(boolean requireStrictHostKeyChecking) {
		this.requireStrictHostKeyChecking = requireStrictHostKeyChecking;
	}

}
