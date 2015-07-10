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
package org.eclipse.ice.viz.service.visit.connections.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ice.viz.service.connections.IVizConnection;
import org.eclipse.ice.viz.service.connections.VizConnectionManager;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.ice.viz.service.visit.connections.VisItConnectionManager;
import org.junit.Test;

import gov.lbnl.visit.swt.VisItSwtConnection;

/**
 * This class tests {@link VisItConnectionManager}'s implementation of
 * {@link VizConnectionManager}.
 * 
 * @author Jordan Deyton
 *
 */
public class VisItConnectionManagerTester {

	/**
	 * The preference store for the test. The manager will pull preferences from
	 * this.
	 */
	private CustomScopedPreferenceStore store;
	/**
	 * The ID of the preference node under which connection preferences will be
	 * stored.
	 */
	private static final String NODE_ID = "org.eclipse.ice.viz.service.connections.paraview.test";

	/**
	 * Checks that, when a connection is added to the preferences, an
	 * {@link IVizConnection} that supports a {@link IVisItWebClient} is
	 * initialized and returned.
	 */
	@Test
	public void checkCreateConnection() {

		// Create a new empty manager.
		VisItConnectionManager manager = new VisItConnectionManager();

		// Create a new, empty preference store.
		store = new CustomScopedPreferenceStore(getClass());
		manager.setPreferenceStore(store, NODE_ID);

		// Add a host.
		IEclipsePreferences node = store.getNode(NODE_ID);

		String name;
		String host;
		int port;
		String path;
		IVizConnection<VisItSwtConnection> connection;

		// Add a new connection. The property name is the connection name, while
		// the value is a delimited string containing its properties.
		name = "magic sword";
		host = "electrodungeon";
		port = 9000;
		path = "/home/music";
		// TODO Add additional preferences.
		node.put(name, host + "," + port + "," + path);

		// Check the new connection's properties.
		connection = manager.getConnection(name);
		assertNotNull(connection);
		assertEquals(name, connection.getName());
		assertEquals(host, connection.getHost());
		assertEquals(port, connection.getPort());
		assertEquals(path, connection.getPath());

		// Check for additional properties as they are added.

		// Clean up the store.
		store.removeNode(NODE_ID);

		return;
	}
	
}
