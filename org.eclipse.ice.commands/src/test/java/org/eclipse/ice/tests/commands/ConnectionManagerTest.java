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
	 * A dummy connection to perform a few tests with
	 */
	static Connection connect = null;

	/**
	 * A name for the dummy connection
	 */
	static String connectionName = "TestConnection";

	/**
	 * This function makes a test connection with which to play with
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ConnectionConfiguration configuration = new ConnectionConfiguration();

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
		configuration.setPassword(password);
		configuration.setName(connectionName);

		// Try to open a connection
		try {
			connect = ConnectionManager.openConnection(configuration);
		} catch (JSchException e) {
			e.printStackTrace();
		}

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
	}

}
