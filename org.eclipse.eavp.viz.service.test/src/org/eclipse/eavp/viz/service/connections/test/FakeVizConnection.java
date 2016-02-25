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

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.eavp.viz.service.connections.VizConnection;

/**
 * A fake viz connection for testing the {@link VizConnection}'s base
 * implementation.
 * 
 * @author Jordan
 *
 */
public class FakeVizConnection extends VizConnection<FakeClient> {

	/**
	 * If true, then {@link #connectToWidget(FakeClient)} and
	 * {@link #disconnectFromWidget(FakeClient)} will return false as if they
	 * failed to connect/disconnect.
	 */
	public boolean failOperation = false;

	/**
	 * Whether the sub-class' implementation for connecting to the actual client
	 * was called.
	 */
	private final AtomicBoolean connectToWidgetCalled = new AtomicBoolean();

	/**
	 * Whether the sub-class' implementation for disconnecting from the actual
	 * client was called.
	 */
	private final AtomicBoolean disconnectFromWidgetCalled = new AtomicBoolean();

	/**
	 * The fake connection wiidget.
	 */
	private final AtomicReference<FakeClient> connectionWidget = new AtomicReference<FakeClient>();

	/**
	 * Gets the connection widget created by this fake connection
	 * implementation. It should match the widget returned by
	 * {@link VizConnection#getWidget()}.
	 * 
	 * @return The created connection widget, or {@code null} if it has not been
	 *         created yet.
	 */
	public FakeClient getFakeWidget() {
		return connectionWidget.get();
	}

	/**
	 * Sets {@link #connectToWidgetCalled} to {@code true} and either
	 * initializes or unsets {@link #connectionWidget} depending on whether
	 * {@link #failOperation} is true.
	 */
	@Override
	protected FakeClient connectToWidget() {
		connectToWidgetCalled.set(true);
		connectionWidget.set(failOperation ? null : new FakeClient());
		return connectionWidget.get();
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
	 * @return True if the sub-class implementation was called, false otherwise.
	 */
	public boolean connectToWidgetCalled() {
		return connectToWidgetCalled.getAndSet(false);
	}

	/**
	 * Gets whether the sub-class' implementation of
	 * {@link #disconnectFromWidget(FakeClient)} was called.
	 * 
	 * @return True if the sub-class implementation was called, false otherwise.
	 */
	public boolean disconnectFromWidgetCalled() {
		return disconnectFromWidgetCalled.getAndSet(false);
	}
}
