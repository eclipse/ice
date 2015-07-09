/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.viz.service.connections.ConnectionAdapter;
import org.eclipse.ice.viz.service.connections.ConnectionState;
import org.eclipse.ice.viz.service.connections.IConnectionAdapter;
import org.eclipse.ice.viz.service.connections.IConnectionClient;
import org.junit.Test;

/**
 * This class tests the basic implementation of {@link IConnectionAdapter}
 * provided by {@link ConnectionAdapter}.
 * 
 * @author Jordan Deyton
 *
 */
public class ConnectionAdapterTester {

	/**
	 * This test checks the default state of a {@code ConnectionAdapter} after
	 * being constructed.
	 */
	@Test
	public void checkConstruction() {

		// Set up the adapter to test.
		ConnectionAdapter<FakeConnection> adapter;
		FakeConnectionAdapter castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;

		// Check all of the default values after construction.

		// Check the properties inherited from ICEObject.
		assertEquals("ICE Object", adapter.getDescription());
		assertEquals("ICE Object", adapter.getName());
		assertEquals(1, adapter.getId());
		// Check the basic adapter properties.
		assertEquals("ICE Object", adapter.getKey());
		assertEquals("localhost", adapter.getHost());
		assertEquals(-1, adapter.getPort());
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		// Check the connection and connection properties.
		assertNull(adapter.getConnection());
		assertNull(adapter.getConnectionProperty("host"));
		assertNull(adapter.getConnectionProperty("port"));
		assertNull(adapter.getConnectionProperty("path"));
		assertFalse(adapter.isRemote());

		return;
	}

	/**
	 * Tests the connect operations and that the sub-class implementation is
	 * correctly called when connecting.
	 */
	@Test
	public void checkConnect() {

		// Set up the adapter to test.
		ConnectionAdapter<FakeConnection> adapter;
		FakeConnectionAdapter castAdapter;

		long sleepTime;
		final long threshold = 1000;
		final long interval = 50;

		// Reset the adapter.
		castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;
		castAdapter.delay = 0;
		castAdapter.success = true;

		// ---- Try the default connection operation. ---- //
		// There's no guarantee that the adapter will have finished this
		// operation even if the underlying connection is "instant" because the
		// attempt is performed on a separate worker thread. Thus, we may need
		// to wait until the attempt completes.
		if (!adapter.connect()) {
			sleepTime = 0;
			while (adapter.getState() == ConnectionState.Connecting
					&& sleepTime < threshold) {
				try {
					Thread.sleep(interval);
					sleepTime += interval;
				} catch (InterruptedException e) {
					fail("ConnectionAdapterTester error: "
							+ "Thread interrupted while checking the connection state.");
				}
			}
		}
		// It should be connected by the end of the loop.
		assertEquals(ConnectionState.Connected, adapter.getState());
		// At this point, any call to connect should return true because it is
		// already connected.
		assertTrue(adapter.connect());
		assertTrue(adapter.connect(false));
		assertTrue(adapter.connect(true));
		// ----------------------------------------------- //

		// Reset the adapter.
		castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;
		castAdapter.delay = 0;
		castAdapter.success = true;

		// ---- Try the non-blocking connection operation. ---- //
		// This should actually be the same as the previous test above.

		// There's no guarantee that the adapter will have finished this
		// operation even if the underlying connection is "instant" because the
		// attempt is performed on a separate worker thread. Thus, we may need
		// to wait until the attempt completes.
		if (!adapter.connect(false)) {
			sleepTime = 0;
			while (adapter.getState() == ConnectionState.Connecting
					&& sleepTime < threshold) {
				try {
					Thread.sleep(interval);
					sleepTime += interval;
				} catch (InterruptedException e) {
					fail("ConnectionAdapterTester error: "
							+ "Thread interrupted while checking the connection state.");
				}
			}
		}
		// It should be connected by the end of the loop.
		assertEquals(ConnectionState.Connected, adapter.getState());
		// At this point, any call to connect should return true because it is
		// already connected.
		assertTrue(adapter.connect());
		assertTrue(adapter.connect(false));
		assertTrue(adapter.connect(true));
		// ---------------------------------------------------- //

		// Reset the adapter.
		castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;
		castAdapter.delay = 0;
		castAdapter.success = true;

		// ---- Try the blocking connection operation. ---- //
		// This test is much simpler because we are guaranteed the connection
		// will have succeeded or failed by the time it returns.
		assertTrue(adapter.connect(true));
		assertEquals(ConnectionState.Connected, adapter.getState());
		// At this point, any call to connect should return true because it is
		// already connected.
		assertTrue(adapter.connect());
		assertTrue(adapter.connect(false));
		assertTrue(adapter.connect(true));
		// ------------------------------------------------ //

		// Reset the adapter.
		castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;
		castAdapter.delay = 0;
		castAdapter.success = false;

		// ---- Try the default connection operation with a failure. ---- //
		assertFalse(adapter.connect());
		// There's no guarantee that the adapter will have finished this
		// operation even if the underlying connection is "instant" because the
		// attempt is performed on a separate worker thread. Thus, we may need
		// to wait until the attempt completes.
		sleepTime = 0;
		while (adapter.getState() == ConnectionState.Connecting
				&& sleepTime < threshold) {
			try {
				Thread.sleep(interval);
				sleepTime += interval;
			} catch (InterruptedException e) {
				fail("ConnectionAdapterTester error: "
						+ "Thread interrupted while checking the connection state.");
			}
		}
		// The connection should have failed by this point.
		assertEquals(ConnectionState.Failed, adapter.getState());
		// -------------------------------------------------------------- //

		// There is no need to reset as we can re-attempt the failed connection.

		// --- Try the non-blocking connection operation with a failure. ---- //
		assertFalse(adapter.connect(false));
		// There's no guarantee that the adapter will have finished this
		// operation even if the underlying connection is "instant" because the
		// attempt is performed on a separate worker thread. Thus, we may need
		// to wait until the attempt completes.
		sleepTime = 0;
		while (adapter.getState() == ConnectionState.Connecting
				&& sleepTime < threshold) {
			try {
				Thread.sleep(interval);
				sleepTime += interval;
			} catch (InterruptedException e) {
				fail("ConnectionAdapterTester error: "
						+ "Thread interrupted while checking the connection state.");
			}
		}
		// The connection should have failed by this point.
		assertEquals(ConnectionState.Failed, adapter.getState());
		// ------------------------------------------------------------------ //

		// There is no need to reset as we can re-attempt the failed connection.

		// --- Try the blocking connection operation with a failure. ---- //
		assertFalse(adapter.connect(true));
		assertEquals(ConnectionState.Failed, adapter.getState());
		// -------------------------------------------------------------- //

		// Reset the adapter.
		castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;
		castAdapter.delay = 10000; // Use a long connection delay.
		castAdapter.success = true;

		// ---- Test for interference with connection worker thread. ---- //

		// Connecting with the non-blocking operation should start the
		// connection process. Subsequent attempts while the connection process
		// is still running shouldn't re-attempt the connection and should just
		// return false.
		assertFalse(adapter.connect(false));
		assertEquals(ConnectionState.Connecting, adapter.getState());
		assertFalse(adapter.connect());
		assertFalse(adapter.connect(false));
		assertFalse(adapter.connect(true));
		assertEquals(ConnectionState.Connecting, adapter.getState());

		// Reset the adapter.
		castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;
		castAdapter.delay = 10000; // Use a long connection delay.
		castAdapter.success = false;

		// The same should be true when the adapter fails to connect.

		// Connecting with the non-blocking operation should start the
		// connection process. Subsequent attempts while the connection process
		// is still running shouldn't re-attempt the connection and should just
		// return false.
		assertFalse(adapter.connect(false));
		assertEquals(ConnectionState.Connecting, adapter.getState());
		assertFalse(adapter.connect());
		assertFalse(adapter.connect(false));
		assertFalse(adapter.connect(true));
		assertEquals(ConnectionState.Connecting, adapter.getState());
		// -------------------------------------------------------------- //

		return;
	}

	/**
	 * Tests the disconnect operations and that the sub-class implementation is
	 * correctly called when connecting.
	 */
	@Test
	public void checkDisconnect() {

		// Set up the adapter to test.
		ConnectionAdapter<FakeConnection> adapter;
		FakeConnectionAdapter castAdapter;

		long sleepTime;
		final long threshold = 1000;
		final long interval = 50;

		// Reset the adapter.
		castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;
		castAdapter.delay = 0;
		castAdapter.success = false;

		// If the current state of the connection is: disconnected.
		// disconnect() and disconnect(false) return: true
		// disconnect(true) returns: true
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		assertTrue(adapter.disconnect());
		assertTrue(adapter.disconnect(false));
		assertTrue(adapter.disconnect(true));
		// Make sure the state doesn't change.
		assertEquals(ConnectionState.Disconnected, adapter.getState());

		// If the current state of the connection is: failed.
		// disconnect() and disconnect(false) return: true
		// disconnect(true) returns: true
		adapter.connect(true);
		assertEquals(ConnectionState.Failed, adapter.getState());
		assertTrue(adapter.disconnect());
		assertTrue(adapter.disconnect(false));
		assertTrue(adapter.disconnect(true));
		// Make sure the state doesn't change.
		assertEquals(ConnectionState.Disconnected, adapter.getState());

		// If the current state of the connection is: connecting.
		// disconnect() and disconnect(false) return: false
		// disconnect(true) returns: false
		castAdapter.delay = 10000; // Make sure there's a long delay.
		adapter.connect(false);
		assertEquals(ConnectionState.Connecting, adapter.getState());
		assertFalse(adapter.disconnect());
		assertFalse(adapter.disconnect(false));
		assertFalse(adapter.disconnect(true));
		// Make sure the state doesn't change.
		assertEquals(ConnectionState.Connecting, adapter.getState());

		// If the current state of the connection is: connected.
		// disconnect() and disconnect(false) return: false
		// disconnect(true) returns: true

		// While testing this, we need to also make sure the adapter is actually
		// disconnected.

		// ---- Test disconnecting from an established connection. ---- //

		// Reset the adapter.
		castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;
		castAdapter.delay = 0;
		castAdapter.success = true;

		// Try the default, non-blocking disconnect operation.
		adapter.connect(true); // Make sure we're connected.
		assertEquals(ConnectionState.Connected, adapter.getState());
		// There's no guarantee that the adapter will have finished this
		// operation even if the underlying connection is "instant" because the
		// attempt is performed on a separate worker thread. Thus, we may need
		// to wait until the attempt completes.
		if (!adapter.disconnect()) {
			sleepTime = 0;
			while (adapter.getState() == ConnectionState.Connected
					&& sleepTime < threshold) {
				try {
					Thread.sleep(interval);
					sleepTime += interval;
				} catch (InterruptedException e) {
					fail("ConnectionAdapterTester error: "
							+ "Thread interrupted while checking the connection state.");
				}
			}
		}
		// The connection should have disconnected by this point.
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		// Attempting to disconnect again shouldn't change anything.
		assertTrue(adapter.disconnect());
		assertTrue(adapter.disconnect(false));
		assertTrue(adapter.disconnect(true));
		// Make sure the state doesn't change.
		assertEquals(ConnectionState.Disconnected, adapter.getState());

		// Try the non-blocking disconnect operation. The results should be the
		// same as above.
		adapter.connect(true); // Make sure we're connected.
		assertEquals(ConnectionState.Connected, adapter.getState());
		// There's no guarantee that the adapter will have finished this
		// operation even if the underlying connection is "instant" because the
		// attempt is performed on a separate worker thread. Thus, we may need
		// to wait until the attempt completes.
		if (!adapter.disconnect(false)) {
			sleepTime = 0;
			while (adapter.getState() == ConnectionState.Connected
					&& sleepTime < threshold) {
				try {
					Thread.sleep(interval);
					sleepTime += interval;
				} catch (InterruptedException e) {
					fail("ConnectionAdapterTester error: "
							+ "Thread interrupted while checking the connection state.");
				}
			}
		}
		// The connection should have disconnected by this point.
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		// Attempting to disconnect again shouldn't change anything.
		assertTrue(adapter.disconnect());
		assertTrue(adapter.disconnect(false));
		assertTrue(adapter.disconnect(true));
		// Make sure the state doesn't change.
		assertEquals(ConnectionState.Disconnected, adapter.getState());

		// Try the blocking disconnect operation. The connection MUST be
		// disconnected by the time it returns.
		adapter.connect(true); // Make sure we're connected.
		assertEquals(ConnectionState.Connected, adapter.getState());
		assertTrue(adapter.disconnect(true));
		// The connection should have failed by this point.
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		// Attempting to disconnect again shouldn't change anything.
		assertTrue(adapter.disconnect());
		assertTrue(adapter.disconnect(false));
		assertTrue(adapter.disconnect(true));
		// Make sure the state doesn't change.
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		// ------------------------------------------------------------ //

		// Now we need to check what happens when the connection fails to
		// disconnect.

		// If the current state of the connection is: connected.
		// disconnect() and disconnect(false) return: false
		// disconnect(true) returns: false

		// ---- Try a failing disconnect call. ---- //
		// Reset the adapter.
		castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;
		castAdapter.delay = 0;

		// Make sure we're connected. This should never change for this section
		// of tests.
		castAdapter.success = true;
		assertTrue(adapter.connect(true)); // Make sure we're connected.
		assertEquals(ConnectionState.Connected, adapter.getState());

		// Set the flag so that the below disconnect operations will fail.
		castAdapter.success = false;

		// Try the blocking disconnect operation.
		assertFalse(adapter.disconnect(true));
		assertEquals(ConnectionState.Connected, adapter.getState());

		// Try the default, non-blocking disconnect operation. (There's no need
		// to reset the adapter as it's already connected.)
		assertFalse(adapter.disconnect());
		assertEquals(ConnectionState.Connected, adapter.getState());

		// Try the non-blocking disconnect operation. (There's no need to reset
		// the adapter as it's already connected.)
		assertFalse(adapter.disconnect(false));
		assertEquals(ConnectionState.Connected, adapter.getState());

		// It shouldn't disconnect at all due to any of those calls.
		sleepTime = 0;
		while (sleepTime < 250) {
			try {
				Thread.sleep(interval);
				sleepTime += interval;
			} catch (InterruptedException e) {
				fail("ConnectionAdapterTester error: "
						+ "Thread interrupted while checking the connection state.");
			}
			assertEquals(ConnectionState.Connected, adapter.getState());
		}
		// ---------------------------------------- //

		return;
	}

	/**
	 * Checks that {@link IConnectionClient}s can be registered and correctly
	 * notified of updates when the connection state changes.
	 */
	@Test
	public void checkClients() {

		// Set up the adapter to test.
		ConnectionAdapter<FakeConnection> adapter;
		FakeConnectionAdapter castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;

		int count = 0;
		long sleepTime;
		final long threshold = 1000;
		final long interval = 50;
		long waitTime;

		// Set up the client to test.
		IConnectionClient<FakeConnection> client;
		FakeConnectionClient castClient = new FakeConnectionClient();
		client = castClient;

		// A client should be updated any time the connection's state changes.

		// There's no need to check intermediate connection states as it has
		// been checked exhaustively in the connect and disconnect tests.
		castAdapter.delay = 0;

		// Register the listener.
		client.setConnectionAdapter(adapter);

		// ------------------------------------------------------------------ //
		// When connecting successfully, this should happen like so:
		// disconnected -> connecting -> connected
		castAdapter.success = true;
		count += 2;
		// Connect and check the number of client updates.
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		assertTrue(adapter.connect(true));
		assertEquals(ConnectionState.Connected, adapter.getState());
		assertTrue(castClient.wasUpdated());
		// Since the notifications are performed on worker threads and we're
		// expecting more than one notification, we need to wait a little.
		sleepTime = 0;
		while (castClient.getUpdateCount() < count && sleepTime < threshold) {
			try {
				Thread.sleep(interval);
				sleepTime += interval;
			} catch (InterruptedException e) {
				fail("ConnectionAdapterTester error: "
						+ "Thread interrupted while checking the client notifications.");
			}
		}
		// Reset the client's update flag in case the second notification
		// arrived later than expected.
		castClient.reset();
		assertEquals(count, castClient.getUpdateCount());

		// ------------------------------------------------------------------ //
		// When disconnecting unsuccessfully, this should happen like so:
		// connected -> connected (no notification)
		castAdapter.success = false;
		// Attempt to disconnect and check the number of client updates.
		assertEquals(ConnectionState.Connected, adapter.getState());
		assertFalse(adapter.disconnect(true));
		assertEquals(ConnectionState.Connected, adapter.getState());
		// We can reduce the wait time here because we don't expect an update.
		// If an update is erroneously received, it should mess with the next
		// test sections below.
		waitTime = castClient.waitTime;
		castClient.waitTime = 250;
		assertFalse(castClient.wasUpdated());
		assertEquals(count, castClient.getUpdateCount());
		// Reset the wait time for update notifications.
		castClient.waitTime = waitTime;

		// ------------------------------------------------------------------ //
		// When disconnecting unsuccessfully, this should happen like so:
		// connected -> disconnected
		castAdapter.success = true;
		count += 1;
		// Attempt to disconnect and check the number of client updates.
		assertEquals(ConnectionState.Connected, adapter.getState());
		assertTrue(adapter.disconnect(true));
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		assertTrue(castClient.wasUpdated());
		assertEquals(count, castClient.getUpdateCount());

		// ------------------------------------------------------------------ //
		// When connecting unsuccessfully, this should happen like so:
		// disconnected -> connecting -> failed
		castAdapter.success = false;
		count += 2;
		// Attempt to disconnect and check the number of client updates.
		assertEquals(ConnectionState.Disconnected, adapter.getState());
		assertFalse(adapter.connect(true));
		assertEquals(ConnectionState.Failed, adapter.getState());
		assertTrue(castClient.wasUpdated());
		// Since the notifications are performed on worker threads and we're
		// expecting more than one notification, we need to wait a little.
		sleepTime = 0;
		while (castClient.getUpdateCount() < count && sleepTime < threshold) {
			try {
				Thread.sleep(interval);
				sleepTime += interval;
			} catch (InterruptedException e) {
				fail("ConnectionAdapterTester error: "
						+ "Thread interrupted while checking the client notifications.");
			}
		}
		// Reset the client's update flag in case the second notification
		// arrived later than expected.
		castClient.reset();
		assertEquals(count, castClient.getUpdateCount());

		// Make sure there aren't any extra notifications.
		assertFalse(castClient.wasUpdated());
		assertEquals(count, castClient.getUpdateCount());

		return;
	}

	/**
	 * Tests the connection properties and that they can be fetched and
	 * retrieved.
	 */
	@Test
	public void checkProperties() {

		// Set up the adapter to test.
		ConnectionAdapter<FakeConnection> adapter;
		FakeConnectionAdapter castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;

		final String nullString = null;

		// Check the default values.
		assertNull(adapter.getConnectionProperty("host"));
		assertNull(adapter.getConnectionProperty("port"));
		assertNull(adapter.getConnectionProperty("id"));
		assertNull(adapter.getConnectionProperty("key"));

		// Set the values (sub-classes normally convert a list of Entries into
		// the properties map, so we just expose the otherwise protected method
		// used below to update the properties).
		castAdapter.setConnectionProperty("host", "the thing");
		castAdapter.setConnectionProperty("port", "2000");
		castAdapter.setConnectionProperty("id", "4011");

		// Check the new values of the properties.
		assertEquals("the thing", adapter.getConnectionProperty("host"));
		assertEquals("2000", adapter.getConnectionProperty("port"));
		assertEquals("4011", adapter.getConnectionProperty("id"));
		assertNull(adapter.getConnectionProperty("key"));

		// We should be able to nullify any of those values.
		castAdapter.setConnectionProperty("host", nullString);
		castAdapter.setConnectionProperty("port", nullString);
		castAdapter.setConnectionProperty("id", nullString);

		// They should now all be null.
		assertNull(adapter.getConnectionProperty("host"));
		assertNull(adapter.getConnectionProperty("port"));
		assertNull(adapter.getConnectionProperty("id"));
		assertNull(adapter.getConnectionProperty("key"));

		return;
	}

	/**
	 * Checks that the host info can be retrieved by the public getters.
	 */
	@Test
	public void checkHostInfo() {

		// Set up the adapter to test.
		ConnectionAdapter<FakeConnection> adapter;
		FakeConnectionAdapter castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;

		final String nullString = null;

		// Check the default values.
		assertNull(adapter.getConnectionProperty("host"));
		assertNull(adapter.getConnectionProperty("port"));
		assertEquals("localhost", adapter.getHost());
		assertEquals(-1, adapter.getPort());
		assertFalse(adapter.isRemote());

		// Set the values (sub-classes normally convert a list of Entries into
		// the properties map, so we just expose the otherwise protected method
		// used below to update the properties).
		castAdapter.setConnectionProperty("host", "the thing");
		castAdapter.setConnectionProperty("port", "2000");

		// Check the new values of the properties.
		assertEquals("the thing", adapter.getConnectionProperty("host"));
		assertEquals("2000", adapter.getConnectionProperty("port"));
		assertEquals("the thing", adapter.getHost());
		assertEquals(2000, adapter.getPort());
		assertTrue(adapter.isRemote());

		// We should be able to nullify any of those values.
		castAdapter.setConnectionProperty("host", nullString);
		castAdapter.setConnectionProperty("port", nullString);

		// They should now all be null or reset to the default.
		assertNull(adapter.getConnectionProperty("host"));
		assertNull(adapter.getConnectionProperty("port"));
		assertEquals("localhost", adapter.getHost());
		assertEquals(-1, adapter.getPort());
		assertFalse(adapter.isRemote());

		return;
	}

	/**
	 * Checks that certain properties cannot be set via the {@code ICEObject}
	 * setters, e.g., the ID, name, and description must be set via the
	 * properties.
	 */
	@Test
	public void checkIdentifiableOverrides() {

		// Set up the adapter to test.
		ConnectionAdapter<FakeConnection> adapter;
		FakeConnectionAdapter castAdapter = new FakeConnectionAdapter();
		adapter = castAdapter;
		castAdapter.delay = 0;

		// Check that the ID cannot be changed through the ICEObject method.
		final int id = adapter.getId();
		adapter.setId(10);
		assertEquals(id, adapter.getId());

		// Check that the name cannot be changed through the ICEObject method.
		final String name = adapter.getName();
		adapter.setName("no name");
		assertEquals(name, adapter.getName());

		// Check that the desc cannot be changed through the ICEObject method.
		final String description = adapter.getDescription();
		adapter.setDescription("The man with no name.");
		assertEquals(description, adapter.getDescription());

		// ---- Check that listeners cannot be registered twice. ---- //
		// Set up the listener.
		IConnectionClient<FakeConnection> client;
		FakeConnectionClient castClient = new FakeConnectionClient();
		client = castClient;

		// Register the listener more than once.
		client.setConnectionAdapter(adapter);
		adapter.register(client);
		adapter.register(client);
		adapter.register(client);

		// Perform an operation that would cause an update.
		castAdapter.postUpdate();
		assertTrue(castClient.wasUpdated());
		// We should only have gotten exactly one update.
		assertEquals(1, castClient.getUpdateCount());

		// Perform another operation that would cause an update.
		castAdapter.postUpdate();
		assertTrue(castClient.wasUpdated());
		// We should now have gotten exactly two total updates.
		assertEquals(2, castClient.getUpdateCount());
		// ---------------------------------------------------------- //

		return;
	}

}
