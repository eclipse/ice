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
package org.eclipse.eavp.viz.service.connections.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.eavp.viz.service.connections.ConnectionState;
import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.connections.IVizConnectionListener;
import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is used to test the basic implementation of {@link IVizConnection}
 * provided by {@link VizConnection} as well as its added functionality.
 * 
 * @author Jordan Deyton
 *
 */
public class VizConnectionTester {

	/**
	 * The main connection that will be tested in each test method.
	 */
	private VizConnection<FakeClient> connection;
	/**
	 * The main test {@link #connection} conveniently cast as its original type.
	 */
	private FakeVizConnection fakeConnection;

	/**
	 * A fake listener. This will already be added to the {@link #connection} at
	 * the start of each test.
	 */
	private FakeVizConnectionListener fakeListener1;
	/**
	 * A fake listener. This will <i>not</i> have been added to the
	 * {@link #connection}.
	 */
	private FakeVizConnectionListener fakeListener2;

	/**
	 * A queue containing the arguments posted to
	 * {@link IVizConnectionListener#connectionStateChanged(IVizConnection, ConnectionState, String)}
	 * for the {@link #fakeListener1}. .
	 */
	private Queue<Object> notificationQueue;
	/**
	 * A lock used to control access to the {@link #notificationQueue} from
	 * multiple threads.
	 */
	private Lock notificationLock;

	/**
	 * Initializes the viz connection that is tested as well as any other class
	 * variables frequently used to test the connection.
	 */
	@Before
	public void beforeEachTest() {

		// Set up the test connection.
		fakeConnection = new FakeVizConnection();
		connection = fakeConnection;

		// Set up the queue and lock for notifications.
		notificationQueue = new LinkedList<Object>();
		notificationLock = new ReentrantLock(true);

		// Customize the first fake listener to post its notification content to
		// the queue.
		fakeListener1 = new FakeVizConnectionListener() {
			@Override
			public void connectionStateChanged(
					IVizConnection<FakeClient> connection,
					ConnectionState state, String message) {
				notificationLock.lock();
				try {
					notificationQueue.add(connection);
					notificationQueue.add(state);
					notificationQueue.add(message);
				} finally {
					notificationLock.unlock();
				}
				super.connectionStateChanged(connection, state, message);
			}
		};

		// For the second fake listener, just create a plain one. We aren't
		// interested in its notification content.
		fakeListener2 = new FakeVizConnectionListener();

		// Add only the first listener.
		connection.addListener(fakeListener1);

		return;
	}

	/**
	 * Checks all of the default values for {@link IVizConnection} getters.
	 */
	@Test
	public void checkDefaults() {

		// Check the default getters.
		assertEquals(ConnectionState.Disconnected, connection.getState());
		assertEquals("The connection has not been configured.",
				connection.getStatusMessage());
		assertNull(connection.getWidget());
		// Check the convenient getters.
		assertEquals("Connection1", connection.getName());
		assertEquals("", connection.getDescription());
		assertEquals("localhost", connection.getHost());
		assertEquals(50000, connection.getPort());
		assertEquals("", connection.getPath());

		// Check the property map.
		Map<String, String> properties = connection.getProperties();
		assertNotNull(connection.getProperties());
		assertEquals(5, connection.getProperties().size());
		// Check the property map contents. These should match the convenient
		// getter values.
		assertEquals("Connection1", properties.get("Name"));
		assertEquals("", properties.get("Description"));
		assertEquals("localhost", properties.get("Host"));
		assertEquals("50000", properties.get("Port"));
		assertEquals("", properties.get("Path"));

		return;
	}

	/**
	 * Checks that the property map cannot be modified from outside the class.
	 */
	@Test
	public void checkProperties() {

		// Get a reference to the connection properties. It should not be empty.
		Map<String, String> properties = connection.getProperties();
		assertNotNull(properties);
		assertFalse(properties.isEmpty());

		// Subsequent requests should return a new map.
		assertFalse(properties == connection.getProperties());
		assertFalse(properties == connection.getProperties());

		// Clearing the returned map should have no effect on the connection's
		// properties.
		properties.clear();
		assertNotNull(connection.getProperties());
		assertFalse(connection.getProperties().isEmpty());

		return;
	}

	/**
	 * Checks that supported properties can be set.
	 */
	@Test
	public void checkSetProperty() {

		String propertyName = "property";
		String value = null;

		final String nullString = null;

		// Initially, the propery's "value" should be null, because it is not
		// set.
		assertNull(connection.getProperties().get(propertyName));

		// Setting the initial property value should return false if the value
		// is null.
		assertFalse(connection.setProperty(propertyName, value));
		assertNull(connection.getProperties().get(propertyName));

		// Setting it to a new value should return true.
		value = "derp";
		assertTrue(connection.setProperty(propertyName, value));
		assertEquals(value, connection.getProperties().get(propertyName));

		// Setting it to the same value should return false.
		assertFalse(connection.setProperty(propertyName, value));
		assertEquals(value, connection.getProperties().get(propertyName));

		// Unsetting it should return true.
		assertTrue(connection.setProperty(propertyName, nullString));
		assertNull(connection.getProperties().get(propertyName));

		return;
	}

	/**
	 * Checks the name setter properly sets the name property.
	 */
	@Test
	public void checkSetName() {

		String propertyName = "Name";
		String name = null;

		final String emptyString = "";
		final String nullString = null;

		// Trying to set to the existing name shouldn't succeed.
		name = connection.getName();
		assertFalse(connection.setName(name));
		assertFalse(connection.setProperty(propertyName, name));
		assertEquals(name, connection.getName());

		// Trying to set to a new, valid name should succeed.
		name = "derp1";
		assertTrue(connection.setName(name));
		assertEquals(name, connection.getName());
		assertEquals(name, connection.getProperties().get(propertyName));

		// Try the property-based setter, too.
		name = "derp2";
		assertTrue(connection.setProperty(propertyName, name));
		assertEquals(name, connection.getName());
		assertEquals(name, connection.getProperties().get(propertyName));

		// Trying to set an invalid (null) name should fail.
		assertFalse(connection.setName(nullString));
		assertFalse(connection.setProperty(propertyName, nullString));
		assertEquals(name, connection.getName());
		assertEquals(name, connection.getProperties().get(propertyName));

		// Trying to set an invalid (non-null) name should fail.
		assertFalse(connection.setName(emptyString));
		assertFalse(connection.setProperty(propertyName, emptyString));
		assertEquals(name, connection.getName());
		assertEquals(name, connection.getProperties().get(propertyName));

		return;
	}

	/**
	 * Checks the description setter properly sets the description property.
	 */
	@Test
	public void checkSetDescription() {

		String propertyName = "Description";
		String desc = null;

		final String nullString = null;

		// Trying to set to the existing desc shouldn't succeed.
		desc = connection.getDescription();
		assertFalse(connection.setDescription(desc));
		assertFalse(connection.setProperty(propertyName, desc));
		assertEquals(desc, connection.getDescription());

		// Trying to set to a new, valid desc should succeed.
		desc = "derp1";
		assertTrue(connection.setDescription(desc));
		assertEquals(desc, connection.getDescription());
		assertEquals(desc, connection.getProperties().get(propertyName));

		// Try the property-based setter, too.
		desc = "derp2";
		assertTrue(connection.setProperty(propertyName, desc));
		assertEquals(desc, connection.getDescription());
		assertEquals(desc, connection.getProperties().get(propertyName));

		// Trying to set an invalid (null) desc should fail.
		assertFalse(connection.setDescription(nullString));
		assertFalse(connection.setProperty(propertyName, nullString));
		// The name should be unchanged.
		assertEquals(desc, connection.getDescription());
		assertEquals(desc, connection.getProperties().get(propertyName));

		return;
	}

	/**
	 * Checks the host setter properly sets the host property.
	 */
	@Test
	public void checkSetHost() {

		String propertyName = "Host";
		String host = null;

		final String emptyString = "";
		final String nullString = null;

		// Trying to set to the existing host shouldn't succeed.
		host = connection.getHost();
		assertFalse(connection.setHost(host));
		assertFalse(connection.setProperty(propertyName, host));
		assertEquals(host, connection.getHost());

		// Trying to set to a new, valid host should succeed.
		host = "derp1";
		assertTrue(connection.setHost(host));
		assertEquals(host, connection.getHost());
		assertEquals(host, connection.getProperties().get(propertyName));

		// Try the property-based setter, too.
		host = "derp2";
		assertTrue(connection.setProperty(propertyName, host));
		assertEquals(host, connection.getHost());
		assertEquals(host, connection.getProperties().get(propertyName));

		// Trying to set an invalid (null) host should fail.
		assertFalse(connection.setHost(nullString));
		assertFalse(connection.setProperty(propertyName, nullString));
		assertEquals(host, connection.getHost());
		assertEquals(host, connection.getProperties().get(propertyName));

		// Trying to set an invalid (non-null) host should fail.
		assertFalse(connection.setHost(emptyString));
		assertFalse(connection.setProperty(propertyName, emptyString));
		assertEquals(host, connection.getHost());
		assertEquals(host, connection.getProperties().get(propertyName));

		return;
	}

	/**
	 * Checks the port setter properly sets the port property.
	 */
	@Test
	public void checkSetPort() {

		String propertyName = "Port";
		int port = 0;
		String portString = null;

		final String emptyString = "";
		final String nullString = null;

		// Trying to set to the existing port shouldn't succeed.
		port = connection.getPort();
		portString = Integer.toString(port);
		assertFalse(connection.setPort(port));
		assertFalse(connection.setProperty(propertyName, portString));
		assertEquals(port, connection.getPort());
		assertEquals(portString, connection.getProperties().get(propertyName));

		// Trying to set to a new, valid port should succeed.
		port = 60000;
		portString = Integer.toString(port);
		assertTrue(connection.setPort(port));
		assertEquals(port, connection.getPort());
		assertEquals(portString, connection.getProperties().get(propertyName));

		// Try the property-based setter, too.
		port = 60001;
		portString = Integer.toString(port);
		assertTrue(connection.setProperty(propertyName, portString));
		assertEquals(port, connection.getPort());
		assertEquals(portString, connection.getProperties().get(propertyName));

		// Trying to set an invalid (null) port should fail.
		assertFalse(connection.setProperty(propertyName, nullString));
		assertEquals(port, connection.getPort());
		assertEquals(portString, connection.getProperties().get(propertyName));

		// Trying to set an invalid (non-null) port should fail.
		assertFalse(connection.setProperty(propertyName, emptyString));
		assertEquals(port, connection.getPort());
		assertEquals(portString, connection.getProperties().get(propertyName));

		// Trying to set to a port that is too low.
		assertFalse(connection.setPort(-1));
		assertFalse(connection.setProperty(propertyName, "-1"));
		assertEquals(port, connection.getPort());
		assertEquals(portString, connection.getProperties().get(propertyName));

		// Trying to set to a port that is too high.
		assertFalse(connection.setPort(65536));
		assertFalse(connection.setProperty(propertyName, "65536"));
		assertEquals(port, connection.getPort());
		assertEquals(portString, connection.getProperties().get(propertyName));

		return;
	}

	/**
	 * Checks the path setter properly sets the path property.
	 */
	@Test
	public void checkSetPath() {

		String propertyName = "Path";
		String path = null;

		final String nullString = null;

		// Trying to set to the existing desc shouldn't succeed.
		path = connection.getPath();
		assertFalse(connection.setPath(path));
		assertFalse(connection.setProperty(propertyName, path));
		assertEquals(path, connection.getPath());

		// Trying to set to a new, valid desc should succeed.
		path = "derp1";
		assertTrue(connection.setPath(path));
		assertEquals(path, connection.getPath());
		assertEquals(path, connection.getProperties().get(propertyName));

		// Try the property-based setter, too.
		path = "derp2";
		assertTrue(connection.setProperty(propertyName, path));
		assertEquals(path, connection.getPath());
		assertEquals(path, connection.getProperties().get(propertyName));

		// Trying to set an invalid (null) desc should fail.
		assertFalse(connection.setPath(nullString));
		assertFalse(connection.setProperty(propertyName, nullString));
		assertEquals(path, connection.getPath());
		assertEquals(path, connection.getProperties().get(propertyName));

		return;
	}

	/**
	 * Checks the return value for the connect operation.
	 */
	@Test
	public void checkConnect() {

		ConnectionState state = null;

		// Try to connect, but fail. This should return the "failed" state.
		fakeConnection.failOperation = true;
		try {
			state = connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// The state should be "failed" and the widget should still be null.
		assertEquals(ConnectionState.Failed, state);
		assertNull(connection.getWidget());

		// Try to connect, this time successfully.
		fakeConnection.failOperation = false;
		try {
			state = connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// The state should be "connected" and the widget should be set.
		assertEquals(ConnectionState.Connected, state);
		assertSame(fakeConnection.getFakeWidget(), connection.getWidget());

		// Trying to connect again should just return the "connected" state.
		try {
			state = connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// The state should *still* be "connected" and the widget should be set.
		assertEquals(ConnectionState.Connected, state);
		assertSame(fakeConnection.getFakeWidget(), connection.getWidget());

		return;
	}

	/**
	 * Checks that the sub-class implementation is called when connecting.
	 */
	@Test
	public void checkConnectToWidget() {

		// Try to connect. This should call the widget implementation but still
		// fail to connect.
		fakeConnection.failOperation = true;
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// Check that the client's implementation was called and that the
		// connection's widget was set to null.
		assertTrue(fakeConnection.connectToWidgetCalled());
		assertNull(connection.getWidget());

		// Try to connect, this time successfully.
		fakeConnection.failOperation = false;
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// Check that the client's implementation was called and that the
		// connection's widget was set to the returned widget.
		assertTrue(fakeConnection.connectToWidgetCalled());
		assertSame(fakeConnection.getFakeWidget(), connection.getWidget());

		// Trying to connect shouldn't call the sub-class operation because the
		// connection was already connected.
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// Check that the client's implementation was *not* called and that the
		// connection's widget is the same as the returned widget.
		assertFalse(fakeConnection.connectToWidgetCalled());
		assertSame(fakeConnection.getFakeWidget(), connection.getWidget());

		return;
	}

	/**
	 * Checks the return value for the disconnect operation.
	 */
	@Test
	public void checkDisconnect() {

		ConnectionState state = null;

		// Trying to disconnect when not connected should just return the
		// "disconnected" state.
		try {
			state = connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}
		// The state returned should be "disconnected" and the widget should
		// still be unset.
		assertEquals(ConnectionState.Disconnected, state);
		assertNull(connection.getWidget());

		// Connect.
		try {
			state = connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// The state should be "connected".
		assertEquals(ConnectionState.Connected, state);

		// Try to disconnect, but fail.
		fakeConnection.failOperation = true;
		try {
			state = connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}
		// The state returned should be "failed", and the widget should remain
		// the same.
		assertEquals(ConnectionState.Failed, state);
		assertSame(fakeConnection.getFakeWidget(), connection.getWidget());

		// Disconnect successfully.
		fakeConnection.failOperation = false;
		try {
			state = connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}
		// The state returned should be "disconnected", and the widget should be
		// unset.
		assertEquals(ConnectionState.Disconnected, state);
		assertNull(connection.getWidget());

		// Trying to disconnect when not connected should just return the
		// "disconnected" state.
		try {
			state = connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}
		// The state returned should be "disconnected" and the widget should
		// still be unset.
		assertEquals(ConnectionState.Disconnected, state);
		assertNull(connection.getWidget());

		return;
	}

	/**
	 * Checks that the sub-class implementation is called when disconnecting.
	 */
	@Test
	public void checkDisconnectFromWidget() {

		// Trying to disconnect when not connected shouldn't call the sub-class
		// implementation.
		try {
			connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}
		// Check that the client's implementation was *not* called and that the
		// connection's widget is still null.
		assertFalse(fakeConnection.disconnectFromWidgetCalled());
		assertNull(connection.getWidget());

		// Connect.
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// The connection's widget should not be null.
		assertNotNull(connection.getWidget());

		// Try to disconnect, but fail.
		fakeConnection.failOperation = true;
		try {
			connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}
		// Check that the client's implementation was called but the connection
		// widget was not unset (for diagnostic purposes).
		assertTrue(fakeConnection.disconnectFromWidgetCalled());
		assertSame(fakeConnection.getFakeWidget(), connection.getWidget());

		// Disconnect successfully.
		fakeConnection.failOperation = false;
		try {
			connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}
		// Check that the client's implementation was called and the connection
		// widget was unset.
		assertTrue(fakeConnection.disconnectFromWidgetCalled());
		assertNull(connection.getWidget());

		// Trying to disconnect when not connected shouldn't call the sub-class
		// implementation.
		try {
			connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}
		// Check that the client's implementation was *not* called and that the
		// connection's widget is still null.
		assertFalse(fakeConnection.disconnectFromWidgetCalled());
		assertNull(connection.getWidget());

		return;
	}

	/**
	 * Checks that listeners can be added and removed, as well as checking that
	 * they are actually notified.
	 */
	@Test
	public void checkListeners() {

		// Connect the connection.
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// Only the first listener will be notified.
		assertFalse(fakeListener2.wasNotified());
		assertTrue(fakeListener1.wasNotified());

		// Register a new listener.
		assertTrue(connection.addListener(fakeListener2));
		// We can't add the same listener twice.
		assertFalse(connection.addListener(fakeListener2));

		// Disconnect the connection.
		try {
			connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}
		// Both listeners should be notified.
		assertTrue(fakeListener1.wasNotified());
		assertTrue(fakeListener2.wasNotified());

		// Check the messages sent to the notified listeners.

		// Remove the listener.
		assertTrue(connection.removeListener(fakeListener2));
		// We can't remove the same listener twice.
		assertFalse(connection.removeListener(fakeListener2));

		// Connect the connection.
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// Only the first listener will be notified.
		assertFalse(fakeListener2.wasNotified());
		assertTrue(fakeListener1.wasNotified());

		return;
	}

	/**
	 * Checks that the proper sequence of notifications are posted to registered
	 * listeners when the {@link VizConnection#connect()} operation is
	 * successful.
	 */
	@Test
	public void checkNotificationsForSuccessfulConnect() {

		int size;
		fakeConnection.failOperation = false;

		// With a successful connection, we expect the following state changes:
		// disconnected > connecting > connected
		// resulting in 2 notifications

		// Connect.
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// Check the "connecting" status update.
		assertTrue(fakeListener1.wasNotified());
		notificationLock.lock();
		try {
			assertSame(connection, notificationQueue.poll());
			assertEquals(ConnectionState.Connecting, notificationQueue.poll());
			assertEquals("The connection is being established.",
					notificationQueue.poll());
			size = notificationQueue.size();
		} finally {
			notificationLock.unlock();
		}

		// If the second notification wasn't received yet, wait for it.
		if (size == 0) {
			assertTrue(fakeListener1.wasNotified());
		}

		// Check the "connected" status update.
		notificationLock.lock();
		try {
			assertSame(connection, notificationQueue.poll());
			assertEquals(ConnectionState.Connected, notificationQueue.poll());
			assertEquals("The connection is established.",
					notificationQueue.poll());
			assertTrue(notificationQueue.isEmpty());
		} finally {
			notificationLock.unlock();
		}

		return;
	}

	/**
	 * Checks that the proper sequence of notifications are posted to registered
	 * listeners when the {@link VizConnection#connect()} operation <i>fails</i>
	 * .
	 */
	@Test
	public void checkNotificationsForFailedConnect() {

		int size;
		fakeConnection.failOperation = true;

		// With a failed connection, we expect the following state changes:
		// disconnected > connecting > failed
		// resulting in 2 notifications

		// Connect.
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}
		// Check the "connecting" status update.
		assertTrue(fakeListener1.wasNotified());
		notificationLock.lock();
		try {
			assertSame(connection, notificationQueue.poll());
			assertEquals(ConnectionState.Connecting, notificationQueue.poll());
			assertEquals("The connection is being established.",
					notificationQueue.poll());
			size = notificationQueue.size();
		} finally {
			notificationLock.unlock();
		}

		// If the second notification wasn't received yet, wait for it.
		if (size == 0) {
			assertTrue(fakeListener1.wasNotified());
		}

		// Check the "failed" status update.
		notificationLock.lock();
		try {
			assertSame(connection, notificationQueue.poll());
			assertEquals(ConnectionState.Failed, notificationQueue.poll());
			assertEquals("The connection failed to connect.",
					notificationQueue.poll());
			assertTrue(notificationQueue.isEmpty());
		} finally {
			notificationLock.unlock();
		}

		return;
	}

	/**
	 * Checks that the proper sequence of notifications are posted to registered
	 * listeners when the {@link VizConnection#disconnect()} operation is
	 * successful.
	 */
	@Test
	public void checkNotificationsForSuccessfulDisconnect() {

		fakeConnection.failOperation = false;

		// With a successful disconnect, we expect the following state changes:
		// connected > disconnected
		// resulting in 1 notification

		// Connect.
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}

		// ---- Ignore the Connecting and Connected notifications. ---- //
		assertTrue(fakeListener1.wasNotified(2));
		notificationLock.lock();
		try {
			assertEquals(6, notificationQueue.size());
			notificationQueue.clear();
		} finally {
			notificationLock.unlock();
		}
		// Reset the notified flag.
		fakeListener1.reset();
		// ------------------------------------------------------------ //

		// Disconnect.
		try {
			connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}

		// Wait for the listener to be notified.
		assertTrue(fakeListener1.wasNotified());

		// Check the "disconnected" status update.
		notificationLock.lock();
		try {
			assertSame(connection, notificationQueue.poll());
			assertEquals(ConnectionState.Disconnected,
					notificationQueue.poll());
			assertEquals("The connection is closed.", notificationQueue.poll());
			assertTrue(notificationQueue.isEmpty());
		} finally {
			notificationLock.unlock();
		}

		return;
	}

	/**
	 * Checks that the proper sequence of notifications are posted to registered
	 * listeners when the {@link VizConnection#disconnect()} operation fails.
	 */
	@Test
	public void checkNotificationsForFailedDisconnect() {

		fakeConnection.failOperation = false;

		// With a failed disconnect, we expect the following state changes:
		// connected > failed
		// resulting in 1 notification

		// Connect.
		try {
			connection.connect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing connect operation.");
		}

		// ---- Ignore the Connecting and Connected notifications. ---- //
		assertTrue(fakeListener1.wasNotified(2));
		notificationLock.lock();
		try {
			assertEquals(6, notificationQueue.size());
			notificationQueue.clear();
		} finally {
			notificationLock.unlock();
		}
		// Reset the notified flag.
		fakeListener1.reset();
		// ------------------------------------------------------------ //

		// Set the disconnect operation to fail.
		fakeConnection.failOperation = true;

		// Disconnect.
		try {
			connection.disconnect().get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			fail("VizConnection error: "
					+ "Failure while performing disconnect operation.");
		}

		// Wait for the listener to be notified.
		assertTrue(fakeListener1.wasNotified());

		// Check the "failed" status update.
		notificationLock.lock();
		try {
			assertSame(connection, notificationQueue.poll());
			assertEquals(ConnectionState.Failed, notificationQueue.poll());
			assertEquals("The connection failed while disconnecting.",
					notificationQueue.poll());
			assertTrue(notificationQueue.isEmpty());
		} finally {
			notificationLock.unlock();
		}

		return;
	}
}
