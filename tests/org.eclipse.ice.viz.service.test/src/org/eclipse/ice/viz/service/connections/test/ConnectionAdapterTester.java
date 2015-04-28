package org.eclipse.ice.viz.service.connections.test;

import org.eclipse.ice.viz.service.connections.ConnectionAdapter;
import org.eclipse.ice.viz.service.connections.IConnectionAdapter;
import org.eclipse.ice.viz.service.connections.IConnectionClient;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class tests the basic implementation of {@link IConnectionAdapter}
 * provided by {@link ConnectionAdapter}.
 * 
 * @author Jordan Deyton
 *
 */
@Ignore
public class ConnectionAdapterTester {
	// TODO Implement these tests.

	/**
	 * This test checks the default state of a {@code ConnectionAdapter} after
	 * being constructed.
	 */
	@Test
	public void checkConstruction() {

	}

	/**
	 * Tests the connect operations and that the sub-class implementation is
	 * correctly called when connecting.
	 */
	@Test
	public void checkConnect() {

	}

	/**
	 * Tests the disconnect operations and that the sub-class implementation is
	 * correctly called when connecting.
	 */
	@Test
	public void checkDisconnect() {

	}

	/**
	 * Tests that the adapter's state is appropriately defined based on calls to
	 * connect/disconnect.
	 */
	@Test
	public void checkState() {

	}

	/**
	 * Checks that {@link IConnectionClient}s can be registered and correctly
	 * notified of updates when the connection state changes.
	 */
	@Test
	public void checkClients() {

	}

	/**
	 * Tests the connection properties and that they can be fetched and
	 * retrieved.
	 */
	@Test
	public void checkProperties() {

	}

	/**
	 * Checks that the host info can be retrieved by the public getters.
	 */
	@Test
	public void checkHostInfo() {

	}

	/**
	 * Checks that certain properties cannot be set via the {@code ICEObject}
	 * setters, e.g., the ID, name, and description must be set via the
	 * properties.
	 */
	@Test
	public void checkIdentifiableOverrides() {

	}
}
