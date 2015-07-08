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
package org.eclipse.ice.viz.service.connections.paraview.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.eclipse.ice.viz.service.connections.VizConnection;
import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnection;
import org.eclipse.ice.viz.service.paraview.web.HttpParaViewWebClient;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests {@link ParaViewConnection}'s implementation of {@link VizConnection}.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewConnectionTester {

	/**
	 * Whether or not the fake client widget will successfully disconnect.
	 */
	private boolean succeed;

	/**
	 * Same as {@link #connection}, only cast as its actual type.
	 */
	private FakeConnection fakeConnection;

	/**
	 * A fake client to be used when testing disconnecting.
	 */
	private IParaViewWebClient fakeClient;

	@Before
	public void beforeEachTest() {
		// Create a fake connection so we can access its implemented methods.
		fakeConnection = new FakeConnection();
		// Whether or not the fake client widget will successfully disconnect.
		succeed = false;

		// Create a fake IParaViewWebClient that returns the class variable
		// "succeed" when it is queried.
		fakeClient = new HttpParaViewWebClient() {
			@Override
			public Future<Boolean> disconnect() {
				return new Future<Boolean>() {
					@Override
					public boolean cancel(boolean mayInterruptIfRunning) {
						return false;
					}

					@Override
					public boolean isCancelled() {
						return false;
					}

					@Override
					public boolean isDone() {
						return true;
					}

					@Override
					public Boolean get() throws InterruptedException, ExecutionException {
						return succeed;
					}

					@Override
					public Boolean get(long timeout, TimeUnit unit)
							throws InterruptedException, ExecutionException, TimeoutException {
						return succeed;
					}
				};
			}

		};

		return;
	}

	/**
	 * Checks that the implementation successfully connects to a widget and
	 * returns it.
	 */
	@Ignore
	@Test
	public void checkSuccessfullConnect() {
		// TODO Figure out how to set up a valid connection.
		fail("Not implemented.");
		FakeConnection connection = new FakeConnection();
		connection.setHost("localhost");
		connection.setPort(59000);
		connection.setPath("some valid path");
		assertNotNull(connection.connectToWidget());
	}

	/**
	 * Checks that the implementation successfully disconnects if the provided
	 * widget is valid.
	 */
	@Test
	public void checkSuccessfullDisconnect() {
		// Should return true when the widget successfully to disconnects.
		succeed = true;
		assertTrue(fakeConnection.disconnectFromWidget(fakeClient));
	}

	/**
	 * Checks that the implementation fails to connect if the configuration is
	 * invalid.
	 */
	@Test
	public void checkFailedToConnect() {
		assertNull(new FakeConnection().connectToWidget());
	}

	/**
	 * Checks that the implementation fails to disconnect if the provided widget
	 * is invalid.
	 */
	@Test
	public void checkFailedToDisconnect() {

		// Should return false when the widget is null.
		IParaViewWebClient nullClient = null;
		assertFalse(fakeConnection.disconnectFromWidget(nullClient));

		// Should return false when the widget fails to disconnect.
		succeed = false;
		assertFalse(fakeConnection.disconnectFromWidget(fakeClient));
	}

	/**
	 * A subclass of {@link ParaViewConnection} to expose its implemented
	 * operations from its super class.
	 * 
	 * @author Jordan
	 *
	 */
	private class FakeConnection extends ParaViewConnection {
		/*
		 * Overrides a method from ParaViewConnection.
		 */
		@Override
		protected IParaViewWebClient connectToWidget() {
			return super.connectToWidget();
		}

		/*
		 * Overrides a method from ParaViewConnection.
		 */
		@Override
		protected boolean disconnectFromWidget(IParaViewWebClient widget) {
			return super.disconnectFromWidget(widget);
		}
	}
}
