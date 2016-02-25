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
package org.eclipse.eavp.viz.service.connections.paraview.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.eclipse.eavp.viz.service.paraview.connections.ParaViewConnection;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests {@link ParaViewConnection}'s implementation of {@link VizConnection}.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewConnectionTester {

	/**
	 * The connection that is tested in each test.
	 */
	private ParaViewConnection connection;

	/**
	 * Initializes the class variables used in each test.
	 */
	@Before
	public void beforeEachTest() {
		connection = new ParaViewConnection();
	}

	/**
	 * Checks the default connection properties for ParaView connections.
	 */
	@Test
	public void checkDefaultProperties() {

		// Check the default values for the basic connection properties.
		assertEquals("Connection1", connection.getName());
		assertEquals("", connection.getDescription());
		assertEquals("localhost", connection.getHost());
		assertEquals(50000, connection.getPort());
		assertEquals("", connection.getPath());

		// Check that they can be set.
		String name = "newname";
		String desc = "some description";
		String host = "newhost";
		int port = 50001;
		String path = "/some/path";

		assertTrue(connection.setName(name));
		assertEquals(name, connection.getName());
		assertTrue(connection.setDescription(desc));
		assertEquals(desc, connection.getDescription());
		assertTrue(connection.setHost(host));
		assertEquals(host, connection.getHost());
		assertTrue(connection.setPort(port));
		assertEquals(port, connection.getPort());
		assertTrue(connection.setPath(path));
		assertEquals(path, connection.getPath());

		return;
	}

	/**
	 * Checks the default connection properties for ParaView connections when
	 * accessed by their actual property names via
	 * {@link IVizConnection#getProperty(String)} and
	 * {@link VizConnection#setProperty(String, String)}.
	 */
	@Test
	public void checkDefaultPropertyNames() {

		// Check the default values for the basic connection properties.
		assertEquals("Connection1", connection.getProperty("Name"));
		assertEquals("", connection.getProperty("Description"));
		assertEquals("localhost", connection.getProperty("Host"));
		assertEquals("50000", connection.getProperty("Port"));
		assertEquals("", connection.getProperty("Path"));

		// Check that they can be set.
		String name = "newname";
		String desc = "some description";
		String host = "newhost";
		String port = "50001";
		String path = "/some/path";

		assertTrue(connection.setProperty("Name", name));
		assertEquals(name, connection.getProperty("Name"));
		assertTrue(connection.setProperty("Description", desc));
		assertEquals(desc, connection.getProperty("Description"));
		assertTrue(connection.setProperty("Host", host));
		assertEquals(host, connection.getProperty("Host"));
		assertTrue(connection.setProperty("Port", port));
		assertEquals(port, connection.getProperty("Port"));
		assertTrue(connection.setProperty("Path", path));
		assertEquals(path, connection.getProperty("Path"));

		return;
	}
}
