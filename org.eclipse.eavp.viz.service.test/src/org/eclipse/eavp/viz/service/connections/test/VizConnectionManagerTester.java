/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton (UT-Battelle, LLC.) - Initial API and implementation and/or 
 *     initial documentation
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.eavp.viz.service.connections.ConnectionState;
import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.connections.IVizConnectionManager;
import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.eclipse.eavp.viz.service.connections.VizConnectionManager;
import org.eclipse.eavp.viz.service.preferences.CustomScopedPreferenceStore;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the basic functionality provided by
 * {@link VizConnectionManager}.
 * 
 * @author Jordan Deyton
 *
 */
public class VizConnectionManagerTester {

	/**
	 * The preference store for the test. The manager will pull preferences from
	 * this.
	 */
	private CustomScopedPreferenceStore store;

	/**
	 * The connection manager that will be tested.
	 */
	private IVizConnectionManager<FakeClient> manager;

	/**
	 * The fake connections (which wrap the custom connection API) created by
	 * the connection manager.
	 */
	private Map<String, FakeVizConnection> fakeConnections;

	/**
	 * A string containing the characters "localhost".
	 */
	private static final String LOCALHOST = "localhost";

	/**
	 * The ID of the preference node under which connection preferences will be
	 * stored.
	 */
	private static final String NODE_ID = "org.eclipse.eavp.viz.service.connections.test";

	/**
	 * Initializes the viz connection manager that is tested as well as any
	 * other class variables frequently used to test the connection.
	 */
	@Before
	public void beforeEachTest() {
		// Initialize the map of created viz connections.
		fakeConnections = new HashMap<String, FakeVizConnection>();

		// Initialize the connection manager.
		manager = new VizConnectionManager<FakeClient>() {
			@Override
			protected VizConnection<FakeClient> createConnection(String name,
					String preferences) {
				// Create a new fake connection and store it in the map.
				FakeVizConnection fakeConnection = new FakeVizConnection();
				fakeConnections.put(name, fakeConnection);
				return fakeConnection;
			}
		};

		// Create a new, empty preference store.
		store = new CustomScopedPreferenceStore(getClass());
		store.removeNode(NODE_ID);

		return;
	}
	
	/**
	 * Checks the default connections (empty).
	 */
	@Test
	public void checkDefaults() {

		// Check the connection names.
		assertNotNull(manager.getConnections());
		assertTrue(manager.getConnections().isEmpty());

		// Check connections for localhost.
		assertNotNull(manager.getConnectionsForHost(LOCALHOST));
		assertTrue(manager.getConnectionsForHost(LOCALHOST).isEmpty());

		// Getting a non-existent connection should return null.
		assertNull(manager.getConnection("arcade high"));

		return;
	}

	/**
	 * Checks that calling
	 * {@link VizConnectionManager#getConnectionsForHost(String)} throws
	 * exceptions when certain invalid input is provided.
	 */
	@Test
	public void checkGetConnectionsForHostExceptions() {

		// An NPE should be thrown when requesting connections for a host and
		// the host argument is null.
		final String nullString = null;
		try {
			manager.getConnectionsForHost(nullString);
			fail("VizConnectionManagerTester failure: "
					+ "NullPointerException not thrown when requesting "
					+ "connections for a null host.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		return;
	}

	/**
	 * Checks that connections can be added successfully by a property change
	 * event.
	 */
	@Test
	public void checkAddConnection() {
		// Set the empty preference store.
		manager.setPreferenceStore(store, NODE_ID);
		IEclipsePreferences node = store.getNode(NODE_ID);

		String name;
		String host;
		int port;
		String path;
		IVizConnection<FakeClient> connection;

		// Add a new connection. The property name is the connection name, while
		// the value is a delimited string containing its properties.
		name = "magic sword";
		host = "electrodungeon";
		port = 9000;
		path = "/home/music";
		node.put(name, host + "," + port + "," + path);

		// Make sure that the connection was added to the manager.
		// Check getConnections()
		assertEquals(1, manager.getConnections().size());
		assertTrue(manager.getConnections().contains(name));
		// Check getConnectionsForHost(String)
		assertEquals(1, manager.getConnectionsForHost(host).size());
		assertTrue(manager.getConnectionsForHost(host).contains(name));
		// Check getConnection(String)
		assertNotNull(manager.getConnection(name));

		// Check the new connection's properties.
		connection = manager.getConnection(name);
		assertEquals(name, connection.getName());
		assertEquals(host, connection.getHost());
		assertEquals(port, connection.getPort());
		assertEquals(path, connection.getPath());

		// The connection should have been initialized. However, this is done on
		// a separate thread. We should give the CPU some leeway to establish
		// the connection.
		long sleepTime = 0;
		long threshold = 2000;
		long interval = 50;
		while (connection.getState() != ConnectionState.Connected
				&& sleepTime < threshold) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sleepTime += interval;
		}
		assertEquals(ConnectionState.Connected, connection.getState());

		// Add another connection. The property name is the connection name,
		// while the value is a delimited string containing its properties.
		name = "trevor something";
		port = 9001;
		path = "";
		node.put(name, host + "," + port + "," + path);

		// Make sure that the connection was added to the manager.
		// Check getConnections()
		assertEquals(2, manager.getConnections().size());
		assertTrue(manager.getConnections().contains(name));
		// Check getConnectionsForHost(String)
		assertEquals(2, manager.getConnectionsForHost(host).size());
		assertTrue(manager.getConnectionsForHost(host).contains(name));
		// Check getConnection(String)
		assertNotNull(manager.getConnection(name));

		// Check the new connection's properties.
		connection = manager.getConnection(name);
		assertEquals(name, connection.getName());
		assertEquals(host, connection.getHost());
		assertEquals(port, connection.getPort());
		assertEquals(path, connection.getPath());

		// The connection should have been initialized. However, this is done on
		// a separate thread. We should give the CPU some leeway to establish
		// the connection.
		sleepTime = 0;
		threshold = 2000;
		interval = 50;
		while (connection.getState() != ConnectionState.Connected
				&& sleepTime < threshold) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sleepTime += interval;
		}
		assertEquals(ConnectionState.Connected, connection.getState());

		return;
	}

	/**
	 * Checks that connections can be removed successfully by a property change
	 * event.
	 */
	@Test
	public void checkRemoveConnection() {
		// Set the empty preference store.
		manager.setPreferenceStore(store, NODE_ID);
		IEclipsePreferences node = store.getNode(NODE_ID);

		String connection1;
		String connection2;
		String host;
		int port;
		String path;

		// Add two connections.
		connection1 = "magic sword";
		host = "electrodungeon";
		port = 9000;
		path = "/home/music";
		node.put(connection1, host + "," + port + "," + path);
		// Add the second connection.
		connection2 = "trevor something";
		port = 9001;
		path = "/home/music/different/path";
		node.put(connection2, host + "," + port + "," + path);

		// Both have been added.
		assertNotNull(manager.getConnection(connection1));
		assertNotNull(manager.getConnection(connection2));
		assertEquals(2, manager.getConnections().size());
		assertTrue(manager.getConnections().contains(connection1));
		assertTrue(manager.getConnections().contains(connection2));
		assertEquals(2, manager.getConnectionsForHost(host).size());
		assertTrue(manager.getConnectionsForHost(host).contains(connection1));
		assertTrue(manager.getConnectionsForHost(host).contains(connection2));

		// Remove the first connection from the store.
		node.remove(connection1);
		// There can be only one.
		assertNull(manager.getConnection(connection1));
		assertNotNull(manager.getConnection(connection2));
		assertEquals(1, manager.getConnections().size());
		assertFalse(manager.getConnections().contains(connection1));
		assertTrue(manager.getConnections().contains(connection2));
		assertEquals(1, manager.getConnectionsForHost(host).size());
		assertFalse(manager.getConnectionsForHost(host).contains(connection1));
		assertTrue(manager.getConnectionsForHost(host).contains(connection2));

		// Remove the second connection from the store.
		node.remove(connection2);
		// There can be only none.
		assertNull(manager.getConnection(connection1));
		assertNull(manager.getConnection(connection2));
		assertEquals(0, manager.getConnections().size());
		assertFalse(manager.getConnections().contains(connection1));
		assertFalse(manager.getConnections().contains(connection2));
		assertEquals(0, manager.getConnectionsForHost(host).size());
		assertFalse(manager.getConnectionsForHost(host).contains(connection1));
		assertFalse(manager.getConnectionsForHost(host).contains(connection2));

		return;
	}

	/**
	 * Checks that connections can be updated successfully by a property change
	 * event.
	 */
	@Test
	public void checkUpdateConnection() {
		// Set the empty preference store.
		manager.setPreferenceStore(store, NODE_ID);
		IEclipsePreferences node = store.getNode(NODE_ID);

		String connection1;
		String connection2;
		String host;
		int port;
		String path;
		IVizConnection<FakeClient> connection;

		final Queue<ConnectionState> states = new ConcurrentLinkedQueue<ConnectionState>();
		FakeVizConnectionListener listener = new FakeVizConnectionListener() {
			@Override
			public void connectionStateChanged(
					IVizConnection<FakeClient> connection,
					ConnectionState state, String message) {
				states.add(state);
				super.connectionStateChanged(connection, state, message);
			}
		};

		// Add two connections.
		connection1 = "magic sword";
		host = "electrodungeon";
		port = 9000;
		path = "/home/music";
		node.put(connection1, host + "," + port + "," + path);

		// Check the first connection's properties.
		connection = manager.getConnection(connection1);
		assertEquals(connection1, connection.getName());
		assertEquals(host, connection.getHost());
		assertEquals(port, connection.getPort());
		assertEquals(path, connection.getPath());

		// Make sure the connection is connected.
		long sleepTime = 0;
		long threshold = 2000;
		long interval = 50;
		while (connection.getState() != ConnectionState.Connected
				&& sleepTime < threshold) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sleepTime += interval;
		}
		assertEquals(ConnectionState.Connected, connection.getState());

		// Add a listener to the first connection.
		connection.addListener(listener);

		// Add the second connection.
		connection2 = "trevor something";
		port = 9001;
		path = "";
		node.put(connection2, host + "," + port + "," + path);

		// Both have been added.
		assertNotNull(manager.getConnection(connection1));
		assertNotNull(manager.getConnection(connection2));
		assertEquals(2, manager.getConnections().size());
		assertTrue(manager.getConnections().contains(connection1));
		assertTrue(manager.getConnections().contains(connection2));
		assertEquals(2, manager.getConnectionsForHost(host).size());
		assertTrue(manager.getConnectionsForHost(host).contains(connection1));
		assertTrue(manager.getConnectionsForHost(host).contains(connection2));

		// Update the first connection.
		host = "electrodungeonmaster";
		node.put(connection1, host + "," + port + "," + path);

		// Check the first connections properties. They should have changed.
		connection = manager.getConnection(connection1);
		assertEquals(connection1, connection.getName());
		assertEquals(host, connection.getHost());
		assertEquals(port, connection.getPort());
		assertEquals(path, connection.getPath());

		// Check querying connections by host.
		// "electrodungeon" should now only have one connection.
		assertEquals(1, manager.getConnectionsForHost("electrodungeon").size());
		assertTrue(manager.getConnectionsForHost("electrodungeon")
				.contains(connection2));
		// "electrodungeonmaster" should only have one connection.
		assertEquals(1,
				manager.getConnectionsForHost("electrodungeonmaster").size());
		assertTrue(manager.getConnectionsForHost("electrodungeonmaster")
				.contains(connection1));

		// The connection should have been reset. To check this, check that the
		// listener was notified of the changes.
		assertTrue(listener.wasNotified(3));
		assertEquals(ConnectionState.Disconnected, states.poll());
		assertEquals(ConnectionState.Connecting, states.poll());
		assertEquals(ConnectionState.Connected, states.poll());

		return;
	}

	/**
	 * Checks that the preference store can be set and loaded.
	 */
	@Test
	public void checkSetPreferenceStore() {

		// Set up a preference node for a first connection.
		final String nodeId1 = NODE_ID;
		IEclipsePreferences node1 = store.getNode(nodeId1);
		IVizConnection<FakeClient> connection1;
		String connection1Name;
		String connection1Host;
		int connection1Port;
		String connection1Path;
		// Set up a preference node for a second connection.
		final String nodeId2 = NODE_ID + "2";
		IEclipsePreferences node2 = store.getNode(nodeId2);
		IVizConnection<FakeClient> connection2;
		String connection2Name;
		String connection2Host;
		int connection2Port;
		String connection2Path;

		// Use a fake listener to determine that the first connection won't be
		// disconnected even though it is removed from the manager.
		FakeVizConnectionListener listener = new FakeVizConnectionListener();

		// Add a connection to the first preference node.
		connection1Name = "magic sword";
		connection1Host = "electrodungeon";
		connection1Port = 9000;
		connection1Path = "/home/music";
		node1.put(connection1Name, connection1Host + "," + connection1Port + ","
				+ connection1Path);

		// Add a connection to the second preference node.
		connection2Name = "trevor something";
		connection2Host = "electrodungeon";
		connection2Port = 9001;
		connection2Path = "/home/music/different/path";
		node2.put(connection2Name, connection2Host + "," + connection2Port + ","
				+ connection2Path);

		// Set the preference store using the first node.
		manager.setPreferenceStore(store, nodeId1);

		// Make sure that the first connection was added to the manager.
		// Check getConnections()
		assertEquals(1, manager.getConnections().size());
		assertTrue(manager.getConnections().contains(connection1Name));
		// Check getConnectionsForHost(String)
		assertEquals(1, manager.getConnectionsForHost(connection1Host).size());
		assertTrue(manager.getConnectionsForHost(connection1Host)
				.contains(connection1Name));
		// Check getConnection(String)
		assertNotNull(manager.getConnection(connection1Name));

		// Check the first connection's properties.
		connection1 = manager.getConnection(connection1Name);
		assertEquals(connection1Name, connection1.getName());
		assertEquals(connection1Host, connection1.getHost());
		assertEquals(connection1Port, connection1.getPort());
		assertEquals(connection1Path, connection1.getPath());

		// The second connection should not yet exist.
		assertNull(manager.getConnection(connection2Name));

		// The connection should have been initialized. However, this is done on
		// a separate thread. We should give the CPU some leeway to establish
		// the connection.
		long sleepTime = 0;
		long threshold = 2000;
		long interval = 50;
		while (connection1.getState() != ConnectionState.Connected
				&& sleepTime < threshold) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sleepTime += interval;
		}
		assertEquals(ConnectionState.Connected, connection1.getState());

		// Add the listener to the first connection.
		connection1.addListener(listener);

		// Switch to the second preference store that has a different
		// connection.
		manager.setPreferenceStore(store, nodeId2);

		// Make sure that the first connection was added to the manager.
		// Check getConnections()
		assertEquals(1, manager.getConnections().size());
		assertTrue(manager.getConnections().contains(connection2Name));
		// Check getConnectionsForHost(String)
		assertEquals(1, manager.getConnectionsForHost(connection2Host).size());
		assertTrue(manager.getConnectionsForHost(connection2Host)
				.contains(connection2Name));
		// Check getConnection(String)
		assertNotNull(manager.getConnection(connection2Name));

		// Check the second connection's properties.
		connection2 = manager.getConnection(connection2Name);
		assertEquals(connection2Name, connection2.getName());
		assertEquals(connection2Host, connection2.getHost());
		assertEquals(connection2Port, connection2.getPort());
		assertEquals(connection2Path, connection2.getPath());

		// The first connection should not exist in the manager.
		assertNull(manager.getConnection(connection1Name));

		// The connection should have been initialized. However, this is done on
		// a separate thread. We should give the CPU some leeway to establish
		// the connection.
		sleepTime = 0;
		threshold = 2000;
		interval = 50;
		while (connection2.getState() != ConnectionState.Connected
				&& sleepTime < threshold) {
			try {
				Thread.sleep(interval);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			sleepTime += interval;
		}
		assertEquals(ConnectionState.Connected, connection2.getState());

		// The first connection should still be connected.
		assertFalse(listener.wasNotified());
		assertEquals(ConnectionState.Connected, connection1.getState());

		// Remove the second preference node. The first one will be removed
		// automatically since its ID is the default one.
		store.removeNode(nodeId2);

		return;
	}

	/**
	 * Checks that the proper exceptions are thrown when the preference store is
	 * set with invalid values.
	 */
	@Test
	public void checkSetPreferenceStoreExceptions() {

		// The normal call should not fail.
		manager.setPreferenceStore(store, NODE_ID);
		// Passing both null should not fail.
		manager.setPreferenceStore(null, null);

		// We cannot send a non-null preference store with a null ID for the
		// connection preference node.
		try {
			manager.setPreferenceStore(store, null);
			fail("VizConnectionManagerTester failure: "
					+ "NullPointerException not thrown when preference node ID "
					+ "is null and store is not null.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		}

		return;
	}
}
