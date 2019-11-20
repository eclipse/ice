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

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.eclipse.ice.commands.ConnectionAuthorizationHandler;
import org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory;
import org.eclipse.ice.commands.ConnectionConfiguration;
import org.eclipse.ice.commands.ConnectionManagerFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.jcraft.jsch.JSchException;

/**
 * This class tests
 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory}
 * 
 * @author Joe Osborn
 *
 */
public class ConnectionAuthorizationHandlerFactoryTest {

	private ConnectionAuthorizationHandlerFactory factory = new ConnectionAuthorizationHandlerFactory();

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
		assert (local.getHostname() == addr.getHostName());
		assert (local.getUsername() == System.getProperty("user.name"));

	}

	/**
	 * Tests text file authorization of
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory#getConnectionAuthorizationHandler(String)}
	 * 
	 * @throws JSchException
	 */
	@Test
	public void testTextAuthorization() throws JSchException {
		String credFile = "/tmp/ice-remote-creds.txt";
		if (System.getProperty("os.name").toLowerCase().contains("win"))
			credFile = "C:\\Users\\Administrator\\ice-remote-creds.txt";

		// Get a text file authorization handler
		ConnectionAuthorizationHandler text = factory.getConnectionAuthorizationHandler("text", credFile);

		// Assert that the hostname and username are the dummy test host
		assert (text.getHostname().equals("osbornjd-ice-host.ornl.gov"));
		assert (text.getUsername().equals("dummy"));

		// Create a connection configuration to actually try and open the connection
		ConnectionConfiguration config = new ConnectionConfiguration();
		config.setName("Text");
		config.setAuthorization(text);

		// Try to open the connection
		ConnectionManagerFactory.getConnectionManager().openConnection(config);
		// Assert that it was correctly opened
		assert (ConnectionManagerFactory.getConnectionManager().isConnectionOpen("Text"));
		// Close it since we are done with it
		ConnectionManagerFactory.getConnectionManager().removeAllConnections();
	}

	/**
	 * Tests key path authorization of
	 * {@link org.eclipse.ice.commands.ConnectionAuthorizationHandlerFactory#getConnectionAuthorizationHandler(String)}
	 * 
	 * @throws JSchException
	 */
	@Test
	public void testKeyPathAuthorization() throws JSchException {
		// Filepath to the dummy host key
		String keyPath = "~/.ssh/dummyhostkey";
		// Create a connection authorization handler for a keypath
		ConnectionAuthorizationHandler auth = factory.getConnectionAuthorizationHandler("keypath", keyPath);
		auth.setHostname("osbornjd-ice-host.ornl.gov");
		auth.setUsername("dummy");
		// Make a connection configuration with the key information
		ConnectionConfiguration config = new ConnectionConfiguration();
		config.setName("keyPath");
		config.setAuthorization(auth);

		// Try to open the connection
		ConnectionManagerFactory.getConnectionManager().openConnection(config);
		// Assert that it was correctly opened
		assert (ConnectionManagerFactory.getConnectionManager().isConnectionOpen("keyPath"));
		// Close it since we are done with it
		ConnectionManagerFactory.getConnectionManager().removeAllConnections();

	}

}
