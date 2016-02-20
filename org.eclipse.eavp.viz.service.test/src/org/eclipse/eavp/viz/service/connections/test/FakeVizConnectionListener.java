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

import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.eavp.viz.service.connections.ConnectionState;
import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.connections.IVizConnectionListener;
import org.eclipse.eavp.viz.service.connections.VizConnection;

/**
 * A fake viz connection listener for testing the listener methods provided by
 * {@link VizConnection}.
 * 
 * @author Jordan
 *
 */
public class FakeVizConnectionListener implements IVizConnectionListener<FakeClient> {

	/**
	 * True if the client's
	 * {@link #connectionStateChanged(IVizConnection, ConnectionState, String)}
	 * method was notified, false otherwise.
	 */
	private final AtomicBoolean wasNotified = new AtomicBoolean();

	/**
	 * The number of times that
	 * {@link #connectionStateChanged(IVizConnection, ConnectionState, String)}
	 * was called.
	 */
	private final AtomicInteger notifyCount = new AtomicInteger();

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.IVizConnectionListener#connectionStateChanged(org.eclipse.eavp.viz.service.connections.IVizConnection, org.eclipse.eavp.viz.service.connections.ConnectionState, java.lang.String)
	 */
	@Override
	public void connectionStateChanged(IVizConnection<FakeClient> connection, ConnectionState state, String message) {
		notifyCount.incrementAndGet();
		wasNotified.set(true);
	}

	/**
	 * Determines if the listener was notified of a connection state change
	 * since the last call to this method.
	 * <p>
	 * This will wait for a certain amount of time until either the flag is true
	 * or a timeout occurs.
	 * </p>
	 * 
	 * @return True if the listener was notified, false otherwise.
	 */
	public boolean wasNotified() {
		boolean notified;

		long threshold = 3000; // The timeout.
		long interval = 50; // The time between checks.
		long time = 0; // The time spent asleep.
		while (!(notified = wasNotified.getAndSet(false)) && time < threshold) {
			try {
				Thread.sleep(interval);
				time += threshold;
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail("VizConnection error: " + "Failure while waiting on listener notification.");
			}
		}
		return notified;
	}

	/**
	 * Determines if the listener was notified of a connection state change a
	 * certain number of times.
	 * <p>
	 * This will wait for a certain amount of time until either the count is
	 * reached or a timeout occurs.
	 * </p>
	 * 
	 * @param expectedCount
	 *            The number of times the listener is expected to be updated.
	 * @return True if the listener was updated the expected number of times,
	 *         false otherwise.
	 */
	public boolean wasNotified(int expectedCount) {

		long threshold = 3000; // The timeout.
		long interval = 50; // The time between checks.
		long time = 0; // The time spent asleep.
		while (notifyCount.get() < expectedCount && time < threshold) {
			try {
				Thread.sleep(interval);
				time += threshold;
			} catch (InterruptedException e) {
				e.printStackTrace();
				fail("VizConnection error: " + "Failure while waiting on listener notification.");
			}
		}
		return notifyCount.get() >= expectedCount;
	}

	/**
	 * Resets the flag that indicates this listener was notified.
	 */
	public void reset() {
		wasNotified.set(false);
	}
}
