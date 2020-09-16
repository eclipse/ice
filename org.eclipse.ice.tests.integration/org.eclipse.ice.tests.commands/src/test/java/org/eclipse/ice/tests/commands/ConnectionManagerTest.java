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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.ice.commands.Connection;
import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManager;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.commands.KeyPathConnectionAuthorizationHandler;
import org.eclipse.ice.tests.util.data.TestDataPath;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

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
	 * A TDP for collecting config files
	 */
	static TestDataPath dataPath = new TestDataPath();

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
		String credFile = dataPath.resolve("commands/ice-remote-creds.txt").toString();

		auth = authFactory.getConnectionAuthorizationHandler("text", credFile);

		// Set it
		configuration.setAuthorization(auth);
		configuration.setName(connectionName);

	}

	/**
	 * Clear out the connections formed after each test so that each test starts
	 * fresh with a clean slated connection manager
	 *
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		// Clear out the connection manager so we start fresh with each test
		ConnectionManagerFactory.getConnectionManager().removeAllConnections();
		// Reset the known hosts directory, for after the test with the
		// expected JSch exception due to nonexistent known_hosts
		ConnectionManagerFactory.getConnectionManager()
				.setKnownHosts(System.getProperty("user.home") + "/.ssh/known_hosts");

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
		assertEquals(0, manager.getConnectionList().size());

		// Make sure the known hosts are reset to the default directory
		manager.setKnownHosts(System.getProperty("user.home") + "/.ssh/known_hosts");

	}

	/**
	 * This function tests opening a connection with an already generated key path
	 *
	 * @throws JSchException
	 */
	@Test
	public void testOpenConnectionKeyPath() throws IOException {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
		System.out.println("Testing keypath open connection");

		// Make a connection configuration for using a key path
		ConnectionConfiguration keyConfiguration = new ConnectionConfiguration();
		keyConfiguration.setName("keypath");
		KeyPathConnectionAuthorizationHandler auth = new KeyPathConnectionAuthorizationHandler();
		// Set the authorization information that is needed
		// Use the same hostname/username information from the text file, in the event
		// that the dummy host is not being used
		auth.setHostname(configuration.getAuthorization().getHostname());
		auth.setUsername(configuration.getAuthorization().getUsername());
		String keyPath = System.getProperty("user.home") + "/.ssh/dummyhostkey";
		auth.setOption(keyPath);
		keyConfiguration.setAuthorization(auth);
		// Open the connection
		manager.openConnection(keyConfiguration);

		// assert that it was properly opened
		assert (manager.isConnectionOpen("keypath"));

		ConnectionManagerFactory.getConnectionManager()
		.setKnownHosts(System.getProperty("user.home") + "/.ssh/known_hosts");

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.ConnectionManager#OpenConnection(ConnectionConfiguration)}
	 */
	public void testOpenConnection() {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
		// Set the name of the configuration, in case it was overwritten by other test
		configuration.setName(connectionName);
		// Try to open a connection
		try {
			connect = manager.openConnection(configuration);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// If connect is not null and there was no error thrown, it was established
		// correctly
		assertNotNull(connect);

	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.ConnectionManager#GetConnection(String)}
	 */
	public void testGetConnection() {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
		Connection testConnection = null;
		testConnection = manager.getConnection(connectionName);
		assertNotNull(testConnection);

		// test a bad connection that doesn't exist in the connection list
		Connection badConnection = manager.getConnection("nonexistent_connection");
		assertNull(badConnection);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.ConnectionManager#CloseConnection(String)}
	 */
	public void testCloseConnection() {
		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();
		// disconnect the session
		manager.closeConnection(connectionName);

		assertFalse(manager.getConnection(connectionName).getSession().isOpen());

		// Remove the connection from the connection manager
		manager.removeConnection(connectionName);

		// Assert that this connection no longer exists, so when you try to get it it is
		// null
		assertNull(manager.getConnection(connectionName));
	}

	/**
	 * This tests the functionality of having multiple connections open at once
	 */
	@Test
	public void testMultipleConnections() {

		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

		// Read in a dummy configuration file that contains credentials
		String credFile = dataPath.resolve("commands/ice-remote-creds.txt").toString();

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

		// Open some configurations
		try {
			manager.openConnection(configuration);
			manager.openConnection(conf2);

		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get the connection list from the manager to test some things
		HashMap<String, Connection> connections = new HashMap<String, Connection>();
		connections = manager.getConnectionList();

		// Expect only two connections since one of the connections is not good (i.e.
		// conn3 has a bad password, therefore it isn't added to the list)

		assertEquals(2, connections.size());

		// List all available connections to the console screen
		manager.listAllConnections();

		// Check that the name returns the appropriate connection from ConnectionManager
		assertEquals("FirstConnection", manager.getConnection("FirstConnection").getConfiguration().getName());

		// Check that the connection is actually open, since FirstConnection is a good
		// connection
		assertTrue(manager.isConnectionOpen("FirstConnection"));

		// Test closing all of the connections
		manager.closeAllConnections();

		// Check that the connection is closed
		assertFalse(manager.isConnectionOpen("FirstConnection"));

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

	/**
	 * This tests failure if the known host does not exist in the known_hosts ssh
	 * file
	 *
	 * @throws JSchException
	 */
	@Test(expected = IOException.class)
	public void testNoKnownHost() throws IOException {
		// Set the known hosts to something random, where we know the ssh fingerprint
		// doesn't exist
		System.out.println("Testing with no known host file, exception expected");
		ConnectionManagerFactory.getConnectionManager().setKnownHosts("/tmp/knownhosts");
		// Try to open a connection
		// Should throw a IOException since the host fingerprint won't match
		connect = ConnectionManagerFactory.getConnectionManager().openConnection(configuration);

	}

	/**
	 * This tests the method openForwardingConnection, which creates a connection
	 * between three systems by porting the connection through an intermediary host
	 * @throws IOException
	 */
	@Test
	@Ignore // ignore until second dummy host is setup
	public void testForwardConnection() throws IOException {

		ConnectionManager manager = ConnectionManagerFactory.getConnectionManager();

		// Read in a dummy configuration file that contains credentials
		String credFile = dataPath.resolve("commands/ice-remote-creds.txt").toString();

		ConnectionAuthorizationHandlerFactory authFactory = new ConnectionAuthorizationHandlerFactory();
		// Request a ConnectionAuthorization of type text file which contains the
		// credentials
		String keyPath = System.getProperty("user.home") + "/.ssh/somekey";
		ConnectionAuthorizationHandler auth = authFactory.getConnectionAuthorizationHandler("keypath",
				keyPath);
		auth.setHostname("hostname");
		auth.setUsername("password");
		ConnectionConfiguration config = new ConnectionConfiguration();
		config.setAuthorization(auth);
		config.setName("forwardConnection");
		Connection firstConnection = manager.openConnection(config);

		// Make sure the connection was opened properly
		assert (manager.isConnectionOpen(firstConnection.getConfiguration().getName()));

		// Now get the final host authorization
		ConnectionAuthorizationHandler intermauth = authFactory.getConnectionAuthorizationHandler("text",
				credFile);

		// Setup the configuration
		ConnectionConfiguration secondConn = new ConnectionConfiguration();
		secondConn.setAuthorization(intermauth);
		secondConn.setName("executeConnection");
		secondConn.deleteWorkingDirectory(false);
		System.out.println("\n\n\n\nOpening forwarding connection");
		// Try to open it
		Connection forwardConnection = manager.openForwardingConnection(firstConnection, secondConn);

		// Assert that it is open
		assert(manager.isConnectionOpen(forwardConnection.getConfiguration().getName()));
	}
}
