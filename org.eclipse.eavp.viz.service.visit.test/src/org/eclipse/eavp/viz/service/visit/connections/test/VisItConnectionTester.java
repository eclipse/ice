/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service.visit.connections.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.eclipse.eavp.viz.service.visit.connections.VisItConnection;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link VisItConnection}'s implementation of {@link VizConnection}.
 * 
 * @author Jordan Deyton
 *
 */
public class VisItConnectionTester {

	/**
	 * The connection that is tested in each test.
	 */
	private VisItConnection connection;

	/**
	 * Initializes the class variables used in each test.
	 */
	@Before
	public void beforeEachTest() {
		connection = new VisItConnection();
	}

	/**
	 * Checks the default connection properties for VisIt connections.
	 */
	@Test
	public void checkDefaultProperties() {

		// Check the default values for the basic connection properties.
		assertEquals("Connection1", connection.getName());
		assertNull(connection.getDescription());
		assertEquals("localhost", connection.getHost());
		assertEquals(9600, connection.getPort());
		assertEquals("", connection.getPath());

		// Check that they can be set.
		String name = "newname";
		String host = "newhost";
		int port = 9601;
		String path = "/some/path";

		assertTrue(connection.setName(name));
		assertEquals(name, connection.getName());
		assertTrue(connection.setHost(host));
		assertEquals(host, connection.getHost());
		assertTrue(connection.setPort(port));
		assertEquals(port, connection.getPort());
		assertTrue(connection.setPath(path));
		assertEquals(path, connection.getPath());

		// The description cannot be set.
		assertFalse(connection.setDescription("VisIt"));
		assertNull(connection.getDescription());

		return;
	}

	/**
	 * Checks that the property names for the default connection properties have
	 * been changed to new, VisIt-specific property names.
	 */
	@Test
	public void checkDefaultPropertyNames() {

		// Check that the default property names are no longer valid.
		assertNull(connection.getProperty("Name"));
		assertNull(connection.getProperty("Description"));
		assertNull(connection.getProperty("Host"));
		assertNull(connection.getProperty("Port"));
		assertNull(connection.getProperty("Path"));

		// Check the new values for the property names.
		assertEquals("Connection1", connection.getProperty("connId"));
		assertEquals("localhost", connection.getProperty("url"));
		assertEquals("9600", connection.getProperty("port"));
		assertEquals("", connection.getProperty("visDir"));

		// Check that they can be set.
		String name = "newname";
		String host = "newhost";
		String port = "9601";
		String path = "/some/path";

		assertTrue(connection.setProperty("connId", name));
		assertEquals(name, connection.getProperty("connId"));
		assertTrue(connection.setProperty("url", host));
		assertEquals(host, connection.getProperty("url"));
		assertTrue(connection.setProperty("port", port));
		assertEquals(port, connection.getProperty("port"));
		assertTrue(connection.setProperty("visDir", path));
		assertEquals(path, connection.getProperty("visDir"));

		// This should also affect the getters for these default properties.
		assertEquals(name, connection.getName());
		assertEquals(host, connection.getHost());
		assertEquals(Integer.parseInt(port), connection.getPort());
		assertEquals(path, connection.getPath());

		// Trying to set the same values should fail.
		assertFalse(connection.setProperty("connId", name));
		assertFalse(connection.setProperty("url", host));
		assertFalse(connection.setProperty("port", port));
		assertFalse(connection.setProperty("visDir", path));

		// We should not be able to set the port to an invalid port.
		port = "-1";
		assertFalse(connection.setProperty("localGatewayPort", port));
		port = "66000";
		assertFalse(connection.setProperty("localGatewayPort", port));

		return;
	}

	/**
	 * Checks the "username" and "password" properties (these are for the VisIt
	 * session).
	 */
	@Test
	public void checkUsernameProperties() {

		assertNull(connection.getProperty("username"));
		assertEquals("notused", connection.getProperty("password"));

		String username = "user";
		String password = "blah";

		// We should be able to set the username, but not the password.
		assertTrue(connection.setProperty("username", username));
		assertEquals(username, connection.getProperty("username"));
		assertFalse(connection.setProperty("password", password));
		assertEquals("notused", connection.getProperty("password"));

		// Trying to set the same values should fail.
		assertFalse(connection.setProperty("username", username));
		assertFalse(connection.setProperty("password", password));

		return;
	}

	/**
	 * Checks the "gateway" and "localGatewayPort" properties (these are for a
	 * proxy host and port for the connection).
	 */
	@Test
	public void checkGatewayProperties() {

		assertNull(connection.getProperty("gateway"));
		assertEquals("22", connection.getProperty("localGatewayPort"));

		String gateway = "eclipse";
		String gatewayPort = "9700";

		// We should be able to set both.
		assertTrue(connection.setProperty("gateway", gateway));
		assertEquals(gateway, connection.getProperty("gateway"));
		assertTrue(connection.setProperty("localGatewayPort", gatewayPort));
		assertEquals(gatewayPort, connection.getProperty("localGatewayPort"));

		// Trying to set the same values should fail.
		assertFalse(connection.setProperty("gateway", gateway));
		assertFalse(connection.setProperty("localGatewayPort", gatewayPort));

		// We should not be able to set the gateway port to invalid ports.
		gatewayPort = "-1";
		assertFalse(connection.setProperty("localGatewayPort", gatewayPort));
		gatewayPort = "bob";
		assertFalse(connection.setProperty("localGatewayPort", gatewayPort));

		return;
	}

	/**
	 * Checks the "windowWidth", "windowHeight", and "windowId" properties.
	 */
	@Test
	public void checkWindowProperties() {

		assertEquals("1340", connection.getProperty("windowWidth"));
		assertEquals("1020", connection.getProperty("windowHeight"));
		assertEquals("1", connection.getProperty("windowId"));

		String windowWidth = "800";
		String windowHeight = "600";
		String windowId = "2";

		// We should be able to all of them.
		assertTrue(connection.setProperty("windowWidth", windowWidth));
		assertEquals(windowWidth, connection.getProperty("windowWidth"));
		assertTrue(connection.setProperty("windowHeight", windowHeight));
		assertEquals(windowHeight, connection.getProperty("windowHeight"));
		assertTrue(connection.setProperty("windowId", windowId));
		assertEquals(windowId, connection.getProperty("windowId"));

		// Trying to set the same values should fail.
		assertFalse(connection.setProperty("windowWidth", windowWidth));
		assertFalse(connection.setProperty("windowHeight", windowHeight));
		assertFalse(connection.setProperty("windowId", windowId));

		// Check input validation for the window dimensions.
		windowWidth = "0";
		assertFalse(connection.setProperty("windowWidth", windowWidth));
		windowWidth = "alice";
		assertFalse(connection.setProperty("windowWidth", windowWidth));
		windowHeight = "-1";
		assertFalse(connection.setProperty("windowHeight", windowHeight));
		windowHeight = "carol";
		assertFalse(connection.setProperty("windowHeight", windowHeight));

		// Check validation for the window ID (must be -1, or 1 through 16).
		windowId = "-1";
		assertTrue(connection.setProperty("windowId", windowId));
		assertEquals(windowId, connection.getProperty("windowId"));
		for (int i = 1; i <= 16; i++) {
			windowId = Integer.toString(i);
			assertTrue(connection.setProperty("windowId", windowId));
			assertEquals(windowId, connection.getProperty("windowId"));
		}
		assertFalse(connection.setProperty("windowId", "-2"));
		assertFalse(connection.setProperty("windowId", "0"));
		assertFalse(connection.setProperty("windowId", "17"));
		assertFalse(connection.setProperty("windowId", "charlie"));

		return;

	}

}
