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
import java.util.HashMap;
import java.util.Scanner;

import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.junit.AfterClass;
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
	 * input the password or just read from a dummy text file. Default to false to
	 * use the dummy ssh connection
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
	 * A connection manager to deal with the dummy test connections
	 */
	ConnectionManager manager = new ConnectionManager();

	/**
	 * This function makes a test connection with which to play with
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// Set up the configuration with the necessary credentials
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		ConnectionAuthorizationHandler auth = null;
		String credFile = "/tmp/ice-remote-creds.txt";
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			credFile = "C:\\Users\\Administrator\\ice-remote-creds.txt";
		
		auth = authFactory.getConnectionAuthorizationHandler("text", credFile);
		
		// Set it
		configuration.setAuthorization(auth);
		configuration.setName(connectionName);
		ConnectionManagerFactory.getConnectionManager().setRequireStrictHostKeyChecking(false);
		
	}

	/**
	 * This function deletes all of the connections in the connection manager once
	 * the tests have run and completed.
	 * 
	 * @throws Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

		// Test removing all connections from the list in ConnectionManager
		manager.removeAllConnections();

		// Assert that there are no more connections in the list
		assert (manager.getConnectionList().size() == 0);

	}


	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.ConnectionManager#OpenConnection(String)}
	 */
	public void testOpenConnection() {
		// Set the name of the configuration, in case it was overwritten by other test
		configuration.setName(connectionName);
		// Try to open a connection
		try {
			connect = manager.openConnection(configuration);
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
		Connection testConnection = null;
		testConnection = manager.getConnection(connectionName);
		assert (testConnection != null);

		// test a bad connection that doesn't exist in the connection list
		Connection badConnection = manager.getConnection("nonexistent_connection");
		assert (badConnection == null);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.ConnectionManager#CloseConnection(String)}
	 */
	public void testCloseConnection() {
		// disconnect the session
		manager.closeConnection(connectionName);

		assert (!manager.getConnection(connectionName).getSession().isConnected());

		// Remove the connection from the connection manager
		manager.removeConnection(connectionName);

		// Assert that this connection no longer exists, so when you try to get it it is
		// null
		assert (manager.getConnection(connectionName) == null);
	}

	/**
	 * This tests the functionality of having multiple connections open at once
	 */
	@Test
	public void testMultipleConnections() {

		// Read in a dummy configuration file that contains credentials
		File file = null;
		String credFile = "/tmp/ice-remote-creds.txt";
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			credFile = "C:\\Users\\Administrator\\ice-remote-creds.txt";

		file = new File(credFile);
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

		// Set the credentials since they were deleted after closing the previous
		// connection
		// Get a factory which determines the type of authorization
		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("text", credFile);
		// Set it
		configuration.setAuthorization(auth);
		configuration.setName("FirstConnection");

		// Make another one
		ConnectionConfiguration conf2 = new ConnectionConfiguration();
		ConnectionAuthorizationHandler auth2 = authFactory.getConnectionAuthorizationHandler("text", credFile);
		conf2.setAuthorization(auth2);
		conf2.setName("someOtherConnection");

		Connection conn1 = null, conn2 = null;
		// Open some configurations
		try {
			conn1 = manager.openConnection(configuration);
			conn2 = manager.openConnection(conf2);

		} catch (JSchException e) {
			e.printStackTrace();
		}

		// Get the connection list from the manager to test some things
		HashMap<String, Connection> connections = new HashMap<String, Connection>();
		connections = manager.getConnectionList();

		// Expect only two connections since one of the connections is not good (i.e.
		// conn3 has a bad password, therefore it isn't added to the list)
		assert (connections.size() == 2);

		// List all available connections to the console screen
		manager.listAllConnections();

		// Check that the name returns the appropriate connection from ConnectionManager
		assert (manager.getConnection("FirstConnection").getConfiguration().getName().equals("FirstConnection"));

		// Check that the connection is actually open, since FirstConnection is a good
		// connection
		assert (manager.isConnectionOpen("FirstConnection"));

		// Test closing all of the connections
		manager.closeAllConnections();

		// Check that the connection is closed
		assert (!manager.isConnectionOpen("FirstConnection"));

	}

	/**
	 * Function that performs all of the tests below to ensure that they are
	 * performed in order, since e.g. one can't close a connection if it isn't
	 * opened in the first place. This is to test a valid opening and closing of a
	 * real connection, in the case where no exceptions are to be thrown.
	 */
	@Test
	public void testValidConnection() {
		System.out.println("Testing valid connection");
		testOpenConnection();

		testGetConnection();

		testCloseConnection();
	}
}
