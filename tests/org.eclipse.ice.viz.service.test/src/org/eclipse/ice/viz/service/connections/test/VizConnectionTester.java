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
package org.eclipse.ice.viz.service.connections.test;

import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.viz.service.connections.ConnectionState;
import org.eclipse.ice.viz.service.connections.IVizConnection;
import org.eclipse.ice.viz.service.connections.IVizConnectionListener;
import org.eclipse.ice.viz.service.connections.VizConnection;
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
	 * Initializes the viz connection that is tested as well as any other class
	 * variables frequently used to test the connection.
	 */
	@Before
	public void beforeEachTest() {

		// Set up the test connection.
		fakeConnection = new FakeVizConnection();
		connection = fakeConnection;

		// Add only the first listener.
		connection.addListener(fakeListener1);

		return;
	}

	/**
	 * Checks all of the default values for {@link IVizConnection} getters.
	 */
	@Test
	public void checkDefaults() {
		fail("Not implemented");
	}

	/**
	 * Checks that listeners can be added and removed, as well as checking that
	 * they are actually notified.
	 */
	@Test
	public void checkListeners() {
		fail("Not implemented");
	}

	/**
	 * Checks that supported properties can be set.
	 */
	@Test
	public void checkSetProperty() {
		fail("Not implemented");
	}

	/**
	 * Checks the name setter properly sets the name property.
	 */
	@Test
	public void checkSetName() {
		fail("Not implemented");
	}

	/**
	 * Checks the description setter properly sets the description property.
	 */
	@Test
	public void checkSetDescription() {
		fail("Not implemented");
	}

	/**
	 * Checks the host setter properly sets the host property.
	 */
	@Test
	public void checkSetHost() {
		fail("Not implemented");
	}

	/**
	 * Checks the port setter properly sets the port property.
	 */
	@Test
	public void checkSetPort() {
		fail("Not implemented");
	}

	/**
	 * Checks the path setter properly sets the path property.
	 */
	@Test
	public void checkSetPath() {
		fail("Not implemented");
	}

	/**
	 * Checks the return value for the connect operation.
	 */
	@Test
	public void checkConnect() {
		fail("Not implemented");
	}

	/**
	 * Checks that the sub-class implementation is called when connecting.
	 */
	@Test
	public void checkConnectToWidget() {
		fail("Not implemented");
	}

	/**
	 * Checks the return value for the disconnect operation.
	 */
	@Test
	public void checkDisconnect() {
		fail("Not implemented");
	}

	/**
	 * Checks that the sub-class implementation is called when disconnecting.
	 */
	@Test
	public void checkDisconnectFromWidget() {
		fail("Not implemented");
	}

	/**
	 * This class provides a fake client of some sort as a type .
	 * 
	 * @author Jordan
	 *
	 */
	private class FakeClient {

	}

	/**
	 * A fake viz connection for testing the {@link VizConnection}'s base
	 * implementation.
	 * 
	 * @author Jordan
	 *
	 */
	private class FakeVizConnection extends VizConnection<FakeClient> {

		/**
		 * If true, then {@link #connectToWidget(FakeClient)} and
		 * {@link #disconnectFromWidget(FakeClient)} will return false as if
		 * they failed to connect/disconnect.
		 */
		public boolean failOperation = false;

		/**
		 * Whether the sub-class' implementation for connecting to the actual
		 * client was called.
		 */
		private final AtomicBoolean connectToWidgetCalled = new AtomicBoolean();

		/**
		 * Whether the sub-class' implementation for disconnecting from the
		 * actual client was called.
		 */
		private final AtomicBoolean disconnectFromWidgetCalled = new AtomicBoolean();

		/**
		 * Sets {@link #connectToWidgetCalled} to {@code true} and returns
		 * {@link #failOperation}.
		 */
		@Override
		protected boolean connectToWidget(FakeClient widget) {
			connectToWidgetCalled.set(true);
			return !failOperation;
		}

		/**
		 * Sets {@link #disconnectFromWidgetCalled} to {@code true} and returns
		 * {@link #failOperation}.
		 */
		@Override
		protected boolean disconnectFromWidget(FakeClient widget) {
			disconnectFromWidgetCalled.set(true);
			return !failOperation;
		}

		/**
		 * Gets whether the sub-class' implementation of
		 * {@link #connectToWidget(FakeClient)} was called.
		 * 
		 * @return True if the sub-class implementation was called, false
		 *         otherwise.
		 */
		public boolean connectToWidgetCalled() {
			return connectToWidgetCalled.getAndSet(false);
		}

		/**
		 * Gets whether the sub-class' implementation of
		 * {@link #disconnectFromWidget(FakeClient)} was called.
		 * 
		 * @return True if the sub-class implementation was called, false
		 *         otherwise.
		 */
		public boolean disconnectFromWidgetCalled() {
			return disconnectFromWidgetCalled.getAndSet(false);
		}
	}

	/**
	 * A fake viz connection listener for testing the listener methods provided
	 * by {@link VizConnection}.
	 * 
	 * @author Jordan
	 *
	 */
	private class FakeVizConnectionListener implements
			IVizConnectionListener<FakeClient> {

		/**
		 * True if the client's
		 * {@link #connectionStateChanged(IVizConnection, ConnectionState, String)}
		 * method was notified, false otherwise.
		 */
		private final AtomicBoolean wasNotified = new AtomicBoolean();

		/**
		 * The connection posted to the listener method.
		 */
		private IVizConnection<FakeClient> connection;
		/**
		 * The state posted to the listener method.
		 */
		private ConnectionState state;
		/**
		 * The message posted to the listener method.
		 */
		private String message;

		/*
		 * Implements a method from IVizConnectionListener.
		 */
		@Override
		public void connectionStateChanged(
				IVizConnection<FakeClient> connection, ConnectionState state,
				String message) {
			this.connection = connection;
			this.state = state;
			this.message = message;
			wasNotified.set(true);
		}

		/**
		 * Determines if the listener was notified of a connection state change
		 * since the last call to this method.
		 * <p>
		 * This will wait for a certain amount of time until either the flag is
		 * true or a timeout occurs.
		 * </p>
		 * 
		 * @return True if the listener was notified, false otherwise.
		 */
		public boolean wasNotified() {
			boolean notified;

			long threshold = 3000; // The timeout.
			long interval = 50; // The time between checks.
			long time = 0; // The time spent asleep.
			while (!(notified = wasNotified.getAndSet(false))
					&& time < threshold) {
				try {
					Thread.sleep(interval);
					time += threshold;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return notified;
		}

		/**
		 * Gets and resets (to {@code null}) the connection posted to the
		 * client.
		 */
		public IVizConnection<FakeClient> getConnection() {
			IVizConnection<FakeClient> retVal = connection;
			connection = null;
			return retVal;
		}

		/**
		 * Gets and resets (to {@code null}) the connection state posted to the
		 * client.
		 */
		public ConnectionState getState() {
			ConnectionState retVal = state;
			state = null;
			return retVal;
		}

		/**
		 * Gets and resets (to {@code null}) the message posted to the client.
		 */
		public String getMessage() {
			String retVal = message;
			message = null;
			return retVal;
		}

		/**
		 * Resets the listener's properties to appear as if it has not been
		 * notified.
		 */
		public void reset() {
			wasNotified.set(false);
			connection = null;
			state = null;
			message = null;
		}
	}
}
