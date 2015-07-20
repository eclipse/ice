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
package org.eclipse.ice.viz.service.connections.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.ice.viz.service.connections.ConnectionVizService;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the connection management functionality provided by
 * {@link ConnectionVizService}.
 * 
 * @author Jordan Deyton
 *
 */
public class ConnectionVizServiceTester {

	/**
	 * The preference store for the test. The manager will pull preferences from
	 * this.
	 */
	private CustomScopedPreferenceStore store;

	/**
	 * The viz service that will be tested.
	 */
	private ConnectionVizService<FakeClient> service;
	/**
	 * A handle to the viz service that will be tested, but cast to its actual
	 * type.
	 */
	private FakeConnectionVizService fakeService;

	/**
	 * A string containing the characters "localhost".
	 */
	private static final String LOCALHOST = "localhost";

	/**
	 * The ID of the preference node under which connection preferences will be
	 * stored.
	 */
	private static final String NODE_ID = "org.eclipse.ice.viz.service.connections.test";

	/**
	 * Initializes the viz connection manager that is tested as well as any
	 * other class variables frequently used to test the connection.
	 */
	@Before
	public void beforeEachTest() {

		String name;
		String host;
		int port;
		String path;

		// Create a new, empty preference store.
		store = new CustomScopedPreferenceStore(getClass());
		store.removeNode(NODE_ID);
		IEclipsePreferences node = store.getNode(NODE_ID);

		// Add a local connection before creating the service.
		name = "trevor something";
		host = LOCALHOST;
		port = 9001;
		path = "";
		node.put(name, host + "," + port + "," + path);

		// Create the fake viz service.
		fakeService = new FakeConnectionVizService() {
			@Override
			protected ConnectionPlot<FakeClient> createConnectionPlot() {
				FakeConnectionPlot plot = (FakeConnectionPlot) super.createConnectionPlot();
				plot.plotTypes.put("track", new String[] { "1", "2" });
				return plot;
			}

			@Override
			protected String getConnectionPreferencesNodeId() {
				return NODE_ID;
			}
		};
		fakeService.supportedExtensions.add("csv");
		service = fakeService;

		// Add a remote connection after creating the service.
		name = "magic sword";
		host = "electrodungeon";
		port = 9000;
		path = "/home/music";
		node.put(name, host + "," + port + "," + path);

		return;
	}

	/**
	 * This checks that a plot can only be created if the viz service has a
	 * connection configured for the host embedded in the URI. It also checks
	 * that the default checks for a {@code null} URI or one with a bad
	 * extension remain in place.
	 */
	@Test
	public void checkCreatePlot() {

		String host;
		String path;
		URI uri = null;
		IPlot plot = null;
		String s = System.getProperty("file.separator");
		File localTestFile = new File(System.getProperty("user.home") + s
				+ "ICETests" + s + "CSVVizService" + s + "fib8.csv");

		// ---- Try a valid local URI (no host specified). ---- //
		// Set up a URI pointing to an existing file on the localhost.
		host = "";
		path = localTestFile.toURI().getPath();
		try {
			uri = new URI("file://" + host + path);
		} catch (URISyntaxException e) {
			System.err.println("bad");
		}

		// Creating a plot with a valid URI should work (since the fake
		// infrastructure does not do any more validation on the file).
		try {
			plot = service.createPlot(uri);
		} catch (Exception e) {
			fail("ConnectionVizServiceTester error: "
					+ "Unexpected exception when creating plot. "
					+ "See stacktrace below:");
			e.printStackTrace();
		}
		assertNotNull(plot);
		// ---------------------------------------------------- //

		// ---- Try a valid remote URI. ---- //
		// Set up the URI.
		host = "electrodungeon";
		path = "/some_file.csv";
		try {
			uri = new URI("file://" + host + path);
		} catch (URISyntaxException e) {
			fail("ConnectionVizServiceTester error: " + "Invalid URI.");
			e.printStackTrace();
		}

		// Creating a plot with a valid URI should work (since the fake
		// infrastructure does not do any more validation on the file).
		try {
			plot = service.createPlot(uri);
		} catch (Exception e) {
			fail("ConnectionVizServiceTester error: "
					+ "Unexpected exception when creating plot. "
					+ "See stacktrace below:");
			e.printStackTrace();
		}
		assertNotNull(plot);
		// --------------------------------- //

		// ---- Try a URI for an unknown host. ---- //
		// Set up the URI.
		host = "megadrive";
		path = "/some_file.csv";
		try {
			uri = new URI("file://" + host + path);
		} catch (URISyntaxException e) {
			fail("ConnectionVizServiceTester error: " + "Invalid URI.");
			e.printStackTrace();
		}

		// Creating a plot with an unknown host should throw an exception.
		try {
			plot = service.createPlot(uri);
			fail("ConnectionVizServiceTester error: "
					+ "Exception not thrown for URI whose host has no "
					+ "configured viz connection.");
		} catch (Exception e) {
			// Exception expected.
		}
		// ---------------------------------------- //

		// Update an existing connection for the unknown host.
		String name = "magic sword";
		host = "megadrive";
		String port = "9000";
		path = "/home/music";
		store.getNode(NODE_ID).put(name, host + "," + port + "," + path);

		// ---- Try a URI for the new host. ---- //
		try {
			plot = service.createPlot(uri);
		} catch (Exception e) {
			fail("ConnectionVizServiceTester error: "
					+ "Unexpected exception when creating plot. "
					+ "See stacktrace below:");
			e.printStackTrace();
		}
		assertNotNull(plot);
		// ----------------------------------- //

		// ---- Try a local path with an explicit localhost. ---- //
		// Set up a URI pointing to an existing file on the localhost.
		host = LOCALHOST;
		path = localTestFile.toURI().getPath();
		try {
			uri = new URI("file://" + host + path);
		} catch (URISyntaxException e) {
			System.err.println("bad");
		}

		// Creating a plot with a valid URI should work (since the fake
		// infrastructure does not do any more validation on the file).
		try {
			plot = service.createPlot(uri);
		} catch (Exception e) {
			fail("ConnectionVizServiceTester error: "
					+ "Unexpected exception when creating plot. "
					+ "See stacktrace below:");
			e.printStackTrace();
		}
		assertNotNull(plot);
		// ------------------------------------------------------ //

		return;
	}

}
