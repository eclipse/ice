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

import static org.junit.Assert.*;

import org.eclipse.ice.viz.service.IVizService;
import org.eclipse.ice.viz.service.connections.ConnectionManager;
import org.eclipse.ice.viz.service.connections.ConnectionState;
import org.eclipse.ice.viz.service.connections.IConnectionAdapter;
import org.eclipse.ice.viz.service.connections.IConnectionClient;
import org.eclipse.ice.viz.service.connections.preferences.ConnectionTable;
import org.junit.Test;

/**
 * This class tests the {@link ConnectionManager} that is used by some
 * {@link IVizService}s to map their connections (stored as
 * {@link IConnectionAdapter}s) to their plots (stored as
 * {@link IConnectionClient}s).
 * 
 * @author Jordan Deyton
 *
 */
public class ConectionManagerTester {

	// TODO These tests will need to be updated when the ConnectionManager
	// handles multiple connections rather than a single default.

	/**
	 * This test checks the default state of a {@code ConnectionAdapter} after
	 * construction.
	 */
	@Test
	public void checkConstruction() {

		FakeConnectionManager fakeManager;

		// When constructed, a ConnectionManager should attempt to load any
		// connections from the preference store.

		// If no default connection preference is available, then no adapter
		// will be set.
		fakeManager = new FakeConnectionManager();
		assertTrue(fakeManager.adapters.isEmpty());

		// If a default connection preference is available, the adapter will be
		// set.
		fakeManager = new FakeConnectionManager() {
			@Override
			protected ConnectionTable createConnectionTable() {
				ConnectionTable table = super.createConnectionTable();
				// Insert a default connection.
				table.addRow();
				return table;
			}
		};
		assertEquals(1, fakeManager.adapters.size());

		return;
	}

	/**
	 * This checks that the adapters managed by this class can be properly
	 * connected.
	 */
	@Test
	public void checkConnect() {

		ConnectionManager<FakeConnection> manager;
		FakeConnectionManager castManager;
		FakeConnectionAdapter adapter;

		long sleepTime;
		final long threshold = 1000;
		final long interval = 50;

		// ---- Try the connect() with a successful connection. ---- //
		// Set up the manager and adapter.
		castManager = new FakeConnectionManager() {
			@Override
			protected ConnectionTable createConnectionTable() {
				ConnectionTable table = super.createConnectionTable();
				// Insert a default connection.
				table.addRow();
				return table;
			}
		};
		manager = castManager;
		adapter = castManager.adapters.get(0);
		adapter.delay = 0;
		adapter.success = true;

		// Initially, the adapter should be disabled.
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		// Attempt to connect via the ConnectionManager. This should have caused
		// the associated adapter(s) to be connected.
		if (!manager.connect()) {
			sleepTime = 0;
			while (adapter.getState() == ConnectionState.Connecting && sleepTime < threshold) {
				try {
					Thread.sleep(interval);
					sleepTime += interval;
				} catch (InterruptedException e) {
					fail("ConnectionManagerTester error: " + "Thread interrupted while checking the connection state.");
				}
			}
		}
		assertEquals(ConnectionState.Connected, adapter.getState());
		// --------------------------------------------------------- //

		// ---- Try the connect() with an already open connection. ---- //
		// Trying to connect again should just return true since the connection
		// was already established.
		assertTrue(manager.connect());
		// ------------------------------------------------------------ //

		// ---- Try the connect() with an unsuccessful connection. ---- //
		// Reset the manager and adapter.
		castManager = new FakeConnectionManager() {
			@Override
			protected ConnectionTable createConnectionTable() {
				ConnectionTable table = super.createConnectionTable();
				// Insert a default connection.
				table.addRow();
				return table;
			}
		};
		manager = castManager;
		adapter = castManager.adapters.get(0);
		adapter.delay = 0;
		adapter.success = false;

		// The connect call should return false.
		assertFalse(manager.connect());
		// The adapter should attempt (and fail) to connect.
		sleepTime = 0;
		while (adapter.getState() == ConnectionState.Connecting && sleepTime < threshold) {
			try {
				Thread.sleep(interval);
				sleepTime += interval;
			} catch (InterruptedException e) {
				fail("ConnectionAdapterTester error: " + "Thread interrupted while checking the connection state.");
			}
		}
		// The connection should have failed by this point.
		assertEquals(ConnectionState.Failed, adapter.getState());
		// ------------------------------------------------------------ //

		// ---- Try the connect() with a null connection. ---- //
		// Reset the manager and adapter.
		castManager = new FakeConnectionManager();
		manager = castManager;

		// The connect call should just return false.
		assertFalse(manager.connect());
		// --------------------------------------------------- //

		return;
	}

	/**
	 * This checks that the adapters managed by this class can be properly
	 * disconnected.
	 */
	@Test
	public void checkDisconnect() {

		ConnectionManager<FakeConnection> manager;
		FakeConnectionManager castManager;
		FakeConnectionAdapter adapter;

		long sleepTime;
		final long threshold = 1000;
		final long interval = 50;

		// Set up the manager and get its adapter.
		castManager = new FakeConnectionManager() {
			@Override
			protected ConnectionTable createConnectionTable() {
				ConnectionTable table = super.createConnectionTable();
				// Insert a default connection.
				table.addRow();
				return table;
			}
		};
		manager = castManager;
		adapter = castManager.adapters.get(0);
		adapter.delay = 0;

		// ---- Try disconnect() when its adapter fails. ---- //
		// Make sure it's connected first.
		adapter.success = true;
		adapter.connect(true);
		assertEquals(ConnectionState.Connected, adapter.getState());

		// Now try disconnect() when the adapter fails to disconnect.
		adapter.success = false;
		assertFalse(manager.disconnect());
		// It should still be connected.
		assertEquals(ConnectionState.Connected, adapter.getState());

		// It shouldn't disconnect at all.
		sleepTime = 0;
		while (sleepTime < 250) { // Only checks for 250ms...
			try {
				Thread.sleep(interval);
				sleepTime += interval;
			} catch (InterruptedException e) {
				fail("ConnectionManagerTester error: " + "Thread interrupted while checking the connection state.");
			}
			assertEquals(ConnectionState.Connected, adapter.getState());
		}
		// -------------------------------------------------- //

		// ---- Try disconnect() (successful) when connected. ---- //
		adapter.success = true;
		// It should still be connected at this point.
		assertEquals(ConnectionState.Connected, adapter.getState());

		// Now try to disconnect. Make sure the adapter is successfully
		// disconnected.
		if (!manager.disconnect()) {
			sleepTime = 0;
			while (adapter.getState() == ConnectionState.Connected && sleepTime < threshold) {
				try {
					Thread.sleep(interval);
					sleepTime += interval;
				} catch (InterruptedException e) {
					fail("ConnectionManagerTester error: " + "Thread interrupted while checking the connection state.");
				}
			}
		}
		// The connection should have disconnected by this point.
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		// ------------------------------------------------------- //

		// ---- Try disconnect() when already disconnected. ---- //
		// Subsequent calls to disconnect shouldn't change anything.
		assertTrue(manager.disconnect());
		assertTrue(manager.disconnect());
		// ----------------------------------------------------- //

		// ---- Try disconnect() with a null connection. ---- //
		// Create a ConnectionManager without an adapter.
		castManager = new FakeConnectionManager();
		manager = castManager;

		// The disconnect call should just return false.
		assertFalse(manager.disconnect());
		// -------------------------------------------------- //

		return;
	}

	/**
	 * This checks that connection clients can be added or removed and that they
	 * are properly registered with the associated connection adapter.
	 */
	@Test
	public void checkClients() {

		ConnectionManager<FakeConnection> manager;
		FakeConnectionManager castManager;
		FakeConnectionAdapter adapter;

		// To test adding and removing clients, we need to check that valid
		// clients can be successfully added/removed while invalid clients
		// cannot. To test that a client was successfully added, its
		// setConnectionAdapter(...) method should be called (which, of course,
		// we can check by getting the client's adapter).
		FakeConnectionClient client1 = new FakeConnectionClient();
		FakeConnectionClient client2 = new FakeConnectionClient();
		final IConnectionClient<FakeConnection> nullClient = null;

		// Create the manager to test and get its default adapter.
		castManager = new FakeConnectionManager() {
			@Override
			protected ConnectionTable createConnectionTable() {
				ConnectionTable table = super.createConnectionTable();
				// Insert a default connection.
				table.addRow();
				return table;
			}
		};
		manager = castManager;
		adapter = castManager.adapters.get(0);

		// Make sure the clients have no adapter beforehand.
		assertNull(client1.getAdapter());
		assertNull(client2.getAdapter());

		// ---- Add the clients. ---- //
		// These clients are new, so adding should return true.

		// Add the first client.
		assertTrue(manager.addClient(client1));
		assertSame(adapter, client1.getAdapter());
		assertNull(client2.getAdapter());

		// Add the second client.
		assertTrue(manager.addClient(client2));
		assertSame(adapter, client1.getAdapter());
		assertSame(adapter, client2.getAdapter());
		// -------------------------- //

		// ---- Try adding the same clients again. ---- //
		// These clients are already there, so adding should return false.
		assertFalse(manager.addClient(client1));
		assertFalse(manager.addClient(client2));

		// Their adapters should still be the same.
		assertSame(adapter, client1.getAdapter());
		assertSame(adapter, client2.getAdapter());
		// -------------------------------------------- //

		// ---- Try adding and removing invalid clients. ---- //
		// Try adding a null client.
		assertFalse(manager.addClient(nullClient));

		// The other clients shouldn't have been touched.
		assertSame(adapter, client1.getAdapter());
		assertSame(adapter, client2.getAdapter());

		// Try removing a null client.
		assertFalse(manager.removeClient(nullClient));

		// The other clients shouldn't have been touched.
		assertSame(adapter, client1.getAdapter());
		assertSame(adapter, client2.getAdapter());
		// -------------------------------------------------- //

		// ---- Remove the clients. ---- //
		// These clients are there, so removing should return true.

		// Remove the first client.
		assertTrue(manager.removeClient(client1));
		assertNull(client1.getAdapter());
		assertSame(adapter, client2.getAdapter());

		// Remove the second client.
		assertTrue(manager.removeClient(client2));
		assertNull(client1.getAdapter());
		assertNull(client2.getAdapter());
		// ----------------------------- //

		// ---- Try removing the same clients again. ---- //
		// These clients are not there, so removing should return false.
		assertFalse(manager.removeClient(client1));
		assertFalse(manager.removeClient(client2));

		// Check the clients' adapters.
		assertNull(client1.getAdapter());
		assertNull(client2.getAdapter());
		// ---------------------------------------------- //

		return;
	}

	/**
	 * This method checks that the underlying adapter is properly updated based
	 * on the changed preferences.
	 */
	@Test
	public void checkPreferences() {

		// TODO Figure out how to test this...

		return;
	}
}
