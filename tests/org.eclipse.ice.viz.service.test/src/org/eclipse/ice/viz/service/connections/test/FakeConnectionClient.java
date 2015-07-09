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

import static org.junit.Assert.fail;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.ice.datastructures.ICEObject.IUpdateable;
import org.eclipse.ice.datastructures.ICEObject.IUpdateableListener;
import org.eclipse.ice.viz.service.connections.IConnectionAdapter;
import org.eclipse.ice.viz.service.connections.IConnectionClient;

/**
 * This class provides an {@link IConnectionClient} for a {@link FakeConnection}
 * .
 * <p>
 * As an implementation of {@link IUpdateableListener}, this class provides
 * getters for <i>whether</i> it was notified/updated by the associated
 * {@link IUpdateable} (this is the {@link #adapter}) and <i>how many times</i>
 * it was updated, as well as methods to reset these flags.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class FakeConnectionClient implements IConnectionClient<FakeConnection> {

	/**
	 * The current connection adapter. This value should <i>only</i> be set via
	 * {@link #setConnectionAdapter(IConnectionAdapter)}, and it may be
	 * {@code null}.
	 */
	private IConnectionAdapter<FakeConnection> adapter;

	/**
	 * Whether or not {@link #update(IUpdateable)} was called with the
	 * {@link #adapter} as the argument.
	 * <p>
	 * This can be reset automatically when calling one of the
	 * {@code wasUpdated(...)} methods or manually by calling {@link #reset()}.
	 * </p>
	 */
	private final AtomicBoolean updated = new AtomicBoolean();

	/**
	 * How many times {@link #update(IUpdateable)} was called with the
	 * {@link #adapter} as the argument.
	 * <p>
	 * This can be reset manually by calling {@link #resetCount()}.
	 * </p>
	 */
	private final AtomicInteger updateCount = new AtomicInteger();

	/**
	 * How long to wait for an update, in ms. If less than or equal to zero,
	 * there is no wait time, meaning the current value of the {@link #updated}
	 * flag will be returned.
	 * <p>
	 * The default value is 1000 ms.
	 * </p>
	 */
	public long waitTime = 1000;

	/**
	 * How long to wait between each update check, in ms. If less than or equal
	 * to zero, then the checking will continue indefinitely until the
	 * {@link #updated} flag is set to true.
	 * <p>
	 * The default value is 50 ms.
	 * </p>
	 */
	public long waitInterval = 50;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.IUpdateableListener#update(org
	 * .eclipse.ice.datastructures.ICEObject.IUpdateable)
	 */
	@Override
	public void update(IUpdateable component) {
		if (component == adapter) {
			updated.set(true);
			updateCount.incrementAndGet();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.IConnectionClient#
	 * setConnectionAdapter
	 * (org.eclipse.ice.viz.service.connections.IConnectionAdapter)
	 */
	@Override
	public void setConnectionAdapter(IConnectionAdapter<FakeConnection> adapter) {
		// Unregister from the old adapter if possible.
		if (this.adapter != null) {
			this.adapter.unregister(this);
		}

		// Set the reference to the adapter.
		this.adapter = adapter;

		// Register with the new adapter if possible.
		if (adapter != null) {
			adapter.register(this);
		}

		return;
	}

	/**
	 * Gets the current {@link #adapter} associated with this client.
	 * 
	 * @return The adapter. This will be null if the previous argument to
	 *         {@link #setConnectionAdapter(IConnectionAdapter)} was null.
	 */
	public IConnectionAdapter<FakeConnection> getAdapter() {
		return adapter;
	}

	/**
	 * Determines whether this client was updated within the current
	 * {@link #waitTime}, provided the {@link #waitInterval} is positive.
	 * <p>
	 * This also resets the {@link #updated} flag before returning.
	 * </p>
	 * <p>
	 * This is the same as calling {@link #wasUpdated(long, boolean)} with the
	 * <i>default</i> wait time and with the reset flag {@code true}.
	 * </p>
	 * 
	 * @return True if updated by the adapter within the time limit, false
	 *         otherwise.
	 */
	public boolean wasUpdated() {
		return wasUpdated(waitTime, true);
	}

	/**
	 * Determines whether this client was updated within the current
	 * {@link #waitTime}, provided the {@link #waitInterval} is positive.
	 * <p>
	 * This also resets the {@link #updated} flag before returning.
	 * </p>
	 * <p>
	 * This is the same as calling {@link #wasUpdated(long, boolean)} with the
	 * <i>specified</i> wait time and with the reset flag {@code true}.
	 * </p>
	 * 
	 * @param waitTime
	 *            The maximum time to keep checking whether the client was
	 *            updated.
	 * @return True if updated by the adapter within the time limit, false
	 *         otherwise.
	 */
	public boolean wasUpdated(long waitTime) {
		return wasUpdated(waitTime, true);
	}

	/**
	 * Determines whether this client was updated within the current
	 * {@link #waitTime}, provided the {@link #waitInterval} is positive.
	 * <p>
	 * This is the same as calling {@link #wasUpdated(long, boolean)} with the
	 * <i>default</i> wait time and with the <i>specified</i> reset flag.
	 * </p>
	 * 
	 * @param reset
	 *            Whether or not to reset the {@link #updated} flag before
	 *            returning.
	 * @return True if updated by the adapter within the time limit, false
	 *         otherwise.
	 */
	public boolean wasUpdated(boolean reset) {
		return wasUpdated(waitTime, reset);
	}

	/**
	 * Determines whether this client was updated within the specified wait
	 * time, provided the {@link #waitInterval} is positive.
	 * 
	 * @param waitTime
	 *            The maximum time to keep checking whether the client was
	 *            updated.
	 * @param reset
	 *            Whether or not to reset the {@link #updated} flag before
	 *            returning.
	 * @return True if updated by the adapter within the time limit, false
	 *         otherwise.
	 */
	public boolean wasUpdated(long waitTime, boolean reset) {
		boolean wasUpdated;

		long sleepTime = 0;
		while ((wasUpdated = updated.get()) == false && sleepTime < waitTime) {
			if (waitInterval > 0) {
				try {
					Thread.sleep(waitInterval);
					sleepTime += waitInterval;
				} catch (InterruptedException e) {
					e.printStackTrace();
					fail("FakeConnectionClient error: "
							+ "Notification listener thread interrupted.");
				}
			}
		}

		if (reset) {
			reset();
		}

		return wasUpdated;
	}

	/**
	 * Resets the {@link #updated} flag to {@code false}.
	 */
	public void reset() {
		updated.set(false);
	}

	/**
	 * Resets the update count to zero.
	 */
	public void resetCount() {
		updateCount.set(0);
	}

	/**
	 * Gets the current number of times that this client was updated.
	 * 
	 * @return The current update count.
	 */
	public int getUpdateCount() {
		return updateCount.get();
	}
}
