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
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;

import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.eclipse.ice.tests.util.data.TestDataPath;
import org.junit.Test;

/**
 * This class tests
 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory}
 *
 * @author Joe Osborn
 *
 */
public class ConnectionAuthorizationHandlerFactoryTest {

	/**
	 * A factory for generating the authorizations
	 */
	private ConnectionAuthorizationHandlerFactory factory = new ConnectionAuthorizationHandlerFactory();

	/**
	 * A TDP for collecting config files
	 */
	private TestDataPath dataPath = new TestDataPath();

	/**
	 * Tests local authorization of
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory#getConnectionAuthorizationHandler(String)}
	 */
	@Test
	public void testLocalAuthorization() {
		// Get a local authorization;
		ConnectionAuthorizationHandler local = factory.getConnectionAuthorizationHandler("local");
		// Get the hostname for your local computer
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		// Assert that the username and hostname are that of the local computer
		assertEquals(addr.getHostName(), local.getHostname());
		assertEquals(local.getUsername(), System.getProperty("user.name"));

	}

	/**
	 * Tests text file authorization of
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory#getConnectionAuthorizationHandler(String)}
	 *
	 * @throws JSchException
	 */
	@Test
	public void testTextAuthorization() throws IOException {

		String credFile = dataPath.resolve("commands/ice-remote-creds.txt").toString();

		// Get a text file authorization handler
		ConnectionAuthorizationHandler text = factory.getConnectionAuthorizationHandler("text", credFile);

		// Assert that the hostname and username are whatever was put in
		File file = new File(credFile);
		try (Scanner scanner = new Scanner(file)) {
			String username = scanner.next();
			char[] pwd = scanner.next().toCharArray();
			// delete the password since we don't need it here
			Arrays.fill(pwd, Character.MIN_VALUE);
			String hostname = scanner.next();

			assertEquals(username, text.getUsername());
			assertEquals(hostname, text.getHostname());

			// Create a connection configuration to actually try and open the connection
			ConnectionConfiguration config = new ConnectionConfiguration();
			config.setName("Text");
			config.setAuthorization(text);

			// Try to open the connection
			ConnectionManagerFactory.getConnectionManager().openConnection(config);
			// Assert that it was correctly opened
			assertTrue(ConnectionManagerFactory.getConnectionManager().isConnectionOpen("Text"));
			// Close it since we are done with it
			ConnectionManagerFactory.getConnectionManager().removeAllConnections();
		}
	}

	/**
	 * Tests key path authorization of
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory#getConnectionAuthorizationHandler(String)}
	 *
	 * @throws JSchException
	 */
	@Test
	public void testKeyPathAuthorization() throws IOException {
		// Filepath to the dummy host key
		String keyPath = System.getProperty("user.home") + "/.ssh/dummyhostkey";

		// Create a text file credential path to get the same username/hostname
		// as the key, in the event someone is using a host that is not the dummy
		// server
		String credFile = dataPath.resolve("commands/ice-remote-creds.txt").toString();

		// Get a text file authorization handler
		ConnectionAuthorizationHandler text = factory.getConnectionAuthorizationHandler("text", credFile);

		// Create a connection authorization handler for a keypath
		ConnectionAuthorizationHandler auth = factory.getConnectionAuthorizationHandler("keypath", keyPath);
		auth.setHostname(text.getHostname());
		auth.setUsername(text.getUsername());
		// Make a connection configuration with the key information
		ConnectionConfiguration config = new ConnectionConfiguration();
		config.setName("keyPath");
		config.setAuthorization(auth);

		// Try to open the connection
		ConnectionManagerFactory.getConnectionManager().openConnection(config);
		// Assert that it was correctly opened
		assertTrue(ConnectionManagerFactory.getConnectionManager().isConnectionOpen("keyPath"));
		// Close it since we are done with it
		ConnectionManagerFactory.getConnectionManager().removeAllConnections();

	}

}
