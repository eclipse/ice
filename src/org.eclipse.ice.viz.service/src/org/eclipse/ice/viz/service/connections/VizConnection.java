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
package org.eclipse.ice.viz.service.connections;

import java.util.Map;
import java.util.concurrent.Future;

/**
 * This abstract class provides a base implementation for the core functionality
 * inherent in all viz connections. Instead of directly implementing the
 * {@link IVizConnection} interface, classes should inherit from this class.
 * <p>
 * This class provides additional functionality intended for management of viz
 * connections. Instances of this class should not be passed around outside a
 * {@link VizConnectionManager} unless you intend to expose the connect and
 * disconnect operations as well as connection properties.
 * </p>
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the underlying connection widget.
 */
public abstract class VizConnection<T> implements IVizConnection<T> {

	// TODO Implement the methods.

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public ConnectionState getState() {
		return null;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public String getStatusMessage() {
		return null;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public T getWidget() {
		return null;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public String getName() {
		return null;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public String getDescription() {
		return null;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public String getHost() {
		return null;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public int getPort() {
		return 0;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public String getPath() {
		return null;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public Map<String, String> getProperties() {
		return null;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public boolean addListener(IVizConnectionListener<T> listener) {
		return false;
	}

	/*
	 * Implements a method from IVizConnection.
	 */
	@Override
	public boolean removeListener(IVizConnectionListener<T> listener) {
		return false;
	}

	/**
	 * Attempts to establish the connection on a separate worker thread.
	 * 
	 * @return A future wrapping the connection's state when the connection
	 *         operation completes. To wait for the connection to connect (or
	 *         fail), use the future's {@code get()} method.
	 */
	public Future<ConnectionState> connect() {
		return null;
	}

	/**
	 * 
	 * @return True if the widget is connected at the end of the operation,
	 *         false otherwise.
	 */
	/**
	 * Attempts to establish the connection to the connection widget. This
	 * method will be called from a separate worker thread.
	 * 
	 * @return The new connection widget, or {@code null} if the connection
	 *         widget could not be created and connected.
	 */
	protected abstract T connectToWidget();

	/**
	 * Attempts to disconnect from the connection widget on a separate worker
	 * thread.
	 * 
	 * @return A future wrapping the connection's state when the disconnect
	 *         operation completes. To wait for the connection to disconnect (or
	 *         fail), use the future's {@code get()} method.
	 */
	public Future<ConnectionState> disconnect() {
		return null;
	}

	/**
	 * Attempts to disconnect from the connection widget. This method will be
	 * called from a separate worker thread.
	 * 
	 * @param widget
	 *            The widget used for the connection.
	 * @return True if the widget is disconnected at the end of the operation,
	 *         false otherwise.
	 */
	protected abstract boolean disconnectFromWidget(T widget);

	/**
	 * Notifies registered {@link IVizConnectionListener}s using the specified
	 * connection state and status message.
	 * 
	 * @param state
	 *            The connection state to send to the listeners.
	 * @param message
	 *            The message to send to the listeners.
	 */
	private void notifyListeners(ConnectionState state, String message) {

	}

	/**
	 * Sets the specified property to the new value, provided the value is both
	 * allowed and new.
	 * 
	 * @param name
	 *            The name of the property to update.
	 * @param value
	 *            The new value of the property.
	 * @return True if the property specified by the name was updated to a new
	 *         value, false otherwise.
	 */
	public boolean setProperty(String name, String value) {
		return false;
	}

	/**
	 * Sets the name of the connection. This is a convenient method that can be
	 * used instead of {@link #setProperty(String, String)}.
	 * 
	 * @param name
	 *            The new name of the property.
	 * @return True if the name was changed, false otherwise.
	 */
	public boolean setName(String name) {
		return false;
	}

	/**
	 * Sets the description of the connection. This is a convenient method that
	 * can be used instead of {@link #setProperty(String, String)}.
	 * 
	 * @param description
	 *            The new description of the property.
	 * @return True if the description was changed, false otherwise.
	 */
	public boolean setDescription(String description) {
		return false;
	}

	/**
	 * Sets the host for the connection. This is a convenient method that can be
	 * used instead of {@link #setProperty(String, String)}.
	 * 
	 * @param host
	 *            The host name of the property.
	 * @return True if the host was changed, false otherwise.
	 */
	public boolean setHost(String host) {
		return false;
	}

	/**
	 * Sets the port of the connection. This is a convenient method that can be
	 * used instead of {@link #setProperty(String, String)}.
	 * 
	 * @param port
	 *            The new port of the property.
	 * @return True if the port was changed, false otherwise.
	 */
	public boolean setPort(int port) {
		return false;
	}

	/**
	 * Sets the path of the connection. This is a convenient method that can be
	 * used instead of {@link #setProperty(String, String)}.
	 * 
	 * @param path
	 *            The new path of the property.
	 * @return True if the path was changed, false otherwise.
	 */
	public boolean setPath(String path) {
		return false;
	}

}
