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
package org.eclipse.ice.tests.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jcraft.jsch.JSchException;

/**
 * Test for class {@link org.eclipse.ice.commands.ConnectionManager}. Note that
 * as currently implemented, the ssh connection is a dummy account set up within
 * gitlab that will be used in CI for testing. The test class reads from a txt
 * file the username, password, and hostname of the dummy account.
 * 
 * @author Joe Osborn
 *
 */
public class ConnectionManagerTest {

	/**
	 * A boolean indicating whether or not the prompt should require the user to
	 * input the password or just read from a dummy text file
	 */
	static boolean require_password = false;

	/**
	 * A dummy connection to perform a few tests with
	 */
	static Connection connect = null;

	/**
	 * A name for the dummy connection
	 */
	static String connectionName = "TestConnection";

	/**
	 * A connection configuration to test with
	 */
	static ConnectionConfiguration configuration = new ConnectionConfiguration();

	/**
	 * This function makes a test connection with which to play with
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// Read in a dummy configuration file that contains credentials
		File file = new File("/tmp/ice-remote-creds.txt");
		Scanner scanner = new Scanner(file);

		// Scan line by line
		scanner.useDelimiter("\n");

		// Get the credentials for the dummy remote account
		String username = scanner.next();
		String password = scanner.next();
		String hostname = scanner.next();

		// Set up the configuration with the necessary credentials
		configuration.setHostname(hostname);
		configuration.setUsername(username);
		if (!require_password)
			configuration.setPassword(password);
		configuration.setName(connectionName);

	}

	/**
	 * Function that performs all of the tests below to ensure that they are
	 * performed in order, since e.g. one can't close a connection if it isn't
	 * opened in the first place
	 */
	@Test
	public void test() {
		testOpenConnection();

		testGetConnection();

		testCloseConnection();

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.ConnectionManager#OpenConnection(String)}
	 */
	public void testOpenConnection() {

		// Try to open a connection
		try {
			connect = ConnectionManager.openConnection(configuration);
		} catch (JSchException e) {
			e.printStackTrace();
		}
		// If connect is not null and there was no error thrown, it was established
		// correctly
		assert (connect != null);

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.ConnectionManager#GetConnection(String)}
	 */
	public void testGetConnection() {
		Connection testConnection = ConnectionManager.getConnection(connectionName);

		assert (testConnection != null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.ConnectionManager#CloseConnection(String)}
	 */
	public void testCloseConnection() {
		// disconnect the session
		ConnectionManager.closeConnection(connectionName);

		assert (!ConnectionManager.getConnection(connectionName).getSession().isConnected());
		
		// Remove the connection from the connection manager
		ConnectionManager.removeConnection(connectionName);
		
		assert(ConnectionManager.getConnection(connectionName) == null);
	}

	/**
	 * This tests the functionality of having multiple connections open at once
	 */
	@Test
	public void testMultipleConnections() {

		// Read in a dummy configuration file that contains credentials
		File file = new File("/tmp/ice-remote-creds.txt");
		Scanner scanner = null;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		// Scan line by line
		scanner.useDelimiter("\n");

		// Get the credentials for the dummy remote account
		String username = scanner.next();
		String password = scanner.next();
		String hostname = scanner.next();

		System.out.println(username + " " + password + " " + hostname);

		// Set the credentials since they were deleted after closing the previous
		// connection
		configuration.setUsername(username);
		configuration.setPassword(password);
		configuration.setHostname(hostname);
		configuration.setName("FirstConnection");

		// Make another one
		ConnectionConfiguration conf2 = new ConnectionConfiguration();
		conf2.setUsername(username);
		conf2.setPassword(password);
		conf2.setHostname(hostname);
		conf2.setName("someOtherConnection");

		// Make another one
		ConnectionConfiguration conf3 = new ConnectionConfiguration();
		conf3.setUsername(username);
		conf3.setPassword("Badpassword");
		conf3.setHostname(hostname);
		conf3.setName("someThirdConnection");

		Connection conn1 = null, conn2 = null, conn3 = null;
		// Open some configurations
		try {
			conn1 = ConnectionManager.openConnection(configuration);

			conn2 = ConnectionManager.openConnection(conf2);

			conn3 = ConnectionManager.openConnection(conf3);

		} catch (JSchException e) {
			e.printStackTrace();
		}

		// Get the connection list from the manager to test some things
		ArrayList<Connection> connections = new ArrayList<Connection>();
		connections = ConnectionManager.getConnectionList();

		// Expect only two connections since one of the connections is not good (i.e.
		// conn3 has a bad password)
		assert (connections.size() == 2);

		assert (connections.get(1).getConfiguration().getName().equals("someOtherConnection"));

		Connection connection = ConnectionManager.getConnection("FirstConnection");
		assert (ConnectionManager.getConnection("FirstConnection").getConfiguration().getName()
				.equals("FirstConnection"));

		assert (ConnectionManager.isConnectionOpen("FirstConnection"));

	}

}
