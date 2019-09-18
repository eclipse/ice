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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

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
	 * An ArrayList of available Connections to the ConnectionManager
	 */
	private static ArrayList<Connection> connectionList = new ArrayList<Connection>();

	/**
	 * Logger for handling event messages and other information.
	 */
	protected static final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

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

		// The new connection to be opened
		Connection newConnection = new Connection(config);

		// Create the shell
		newConnection.setJShellSession(new JSch());

		logger.info("Trying to open the connection");
		// Get the information necessary to open the connection
		if (newConnection.getConfiguration() != null) {
			String username = newConnection.getConfiguration().getUsername();
			String hostname = newConnection.getConfiguration().getHostname();

			// Try go get and open the new session
			try {
				newConnection.setSession(newConnection.getJShellSession().getSession(username, hostname));
			} catch (JSchException e) {
				logger.error("Couldn't open session with given username and hostname. Exiting.");
				throw new JSchException();
			}

			// Get the password
			char[] pwd = null;
			if (newConnection.getConfiguration().getPassword().equals("")) {
				pwd = getPassword();
			} else {
				// The password is only stored for unit tests to the dummy ssh connection
				pwd = newConnection.getConfiguration().getPassword().toCharArray();
				// Delete it since it is no longer needed
				newConnection.getConfiguration().setPassword("");
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
				logger.error("Couldn't connect to session with given password. Exiting.");
				throw new JSchException();
			}

			// Add the connection to the list
			connectionList.add(newConnection);

			logger.info("Connection at " + username + "@" + hostname + " established successfully");
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

	public static boolean isConnectionOpen(String connectionName) {
		Connection connection = getConnection(connectionName);
		return connection.getSession().isConnected();
	}

	/**
	 * This function gets the password from the user as a prompt. It uses the
	 * {@link org.eclipse.ice.commands.ConsoleEraser#run} method to "erase" the
	 * characters at the console prompt as they are typed in, so that the password
	 * isn't shown. The prompt is terminated by a carriage return.
	 * 
	 * @return - char array of password chars
	 */
	private static final char[] getPassword() {

		String password = "";
		ConsoleEraser eraser = new ConsoleEraser();

		logger.info("Please enter your password: ");

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		eraser.start();
		try {
			password = in.readLine();
			in.close();
		} catch (IOException e) {
			logger.error("Couldn't read the password...");
			return null;
		}

		eraser.stopErasing();
		System.out.print("\b");

		return password.toCharArray();
	}

	/**
	 * Setter function for
	 * {@link org.eclipse.ice.commands.ConnectionManager#connectionList}
	 * 
	 * @param connections
	 */
	public static void setConnectionList(ArrayList<Connection> connections) {
		connectionList = connections;
	}

	/**
	 * Getter function for
	 * {@link org.eclipse.ice.commands.ConnectionManager#connectionList}
	 * 
	 * @return
	 */
	public static ArrayList<Connection> getConnectionList() {
		return connectionList;
	}

}
