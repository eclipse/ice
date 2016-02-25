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
package org.eclipse.eavp.viz.service.connections;

import java.util.Map;

/**
 * This is an interface for connections intended solely for <i>consumption</i>
 * by viz services or their plots. It provides access to a connection's current
 * state as well as access to the underlying widget for service- or
 * plot-specific functionality.
 * <p>
 * Instead of directly implementing this interface, classes should inherit from
 * {@link VizConnection}.
 * </p>
 * <p>
 * Instances of this class will usually be <i>created by and acquired from</i> a
 * {@link VizConnectionManager}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the underlying connection widget.
 */
public interface IVizConnection<T> {
	/**
	 * Adds a listener for state changes to this connection. A listener can only
	 * be registered once, and will be notified on a separate thread when the
	 * connection state changes.
	 * 
	 * @param listener
	 *            The listener to add. If {@code null}, no listener will be
	 *            added.
	 * @return True if the specified listener was added, false otherwise.
	 */
	public boolean addListener(IVizConnectionListener<T> listener);

	/**
	 * Gets a description of the connection. This should be a string that can be
	 * displayed to the user.
	 * 
	 * @return A description of the connection. Should not be {@code null}, but
	 *         may be an empty string.
	 */
	public String getDescription();

	/**
	 * Gets the current host associated with the connection. For local
	 * connections, this will be "localhost".
	 * 
	 * @return The current host. This value should never be {@code null}.
	 */
	public String getHost();

	/**
	 * Gets the name of the connection. This name should be unique within its
	 * associated {@link VizConnectionManager}.
	 * 
	 * @return The (unique) name of the connection.
	 */
	public String getName();

	/**
	 * Gets the installation path to the executable required to run the
	 * associated connection widget. This should be a <i>full</i> path formatted
	 * to the host machine's operating system.
	 * 
	 * @return The installation path to the visualization engine on the host
	 *         machine.
	 */
	public String getPath();

	/**
	 * Gets the port associated with the connection. This is the port at which
	 * the associated connection widget hosts the viz rendering service.
	 * 
	 * @return The connection's port.
	 */
	public int getPort();

	/**
	 * Gets the current properties for this connection. This should never be
	 * {@code null} and should always contain the name, description, host, port,
	 * and path, as well as any other properties required by the implementing
	 * class.
	 * 
	 * @return The connection's properties.
	 */
	public Map<String, String> getProperties();

	/**
	 * Gets the value of the property with the specified name.
	 * 
	 * @param name
	 *            The name of the property to get.
	 * @return The current value of the property with the specified name, or
	 *         {@code null} if it has not been set.
	 */
	public String getProperty(String name);

	/**
	 * Gets the current state of the connection.
	 * <p>
	 * This may change at any moment. If code needs to listen for the current
	 * state, then a listener should be registered via
	 * {@link #addListener(IVizConnectionListener)}.
	 * </p>
	 * 
	 * @return The current connection state.
	 */
	public ConnectionState getState();

	/**
	 * Gets a descriptive message for the current state of the connection.
	 * <p>
	 * This may change at any moment. If code needs to listen for the current
	 * state, then a listener should be registered via
	 * {@link #addListener(IVizConnectionListener)}.
	 * </p>
	 * 
	 * @return The current status message.
	 */
	public String getStatusMessage();

	/**
	 * Gets the underlying connection widget managed by this viz connection
	 * instance.
	 * 
	 * @return The current connection widget. May be {@code null} if the
	 *         connection is currently disconnected or failed.
	 */
	public T getWidget();

	/**
	 * Removes a listener from state change notifications.
	 * 
	 * @param listener
	 *            The listener to remove.
	 * @return True if the listener was removed, false otherwise (including if
	 *         the listener was not already listening).
	 */
	public boolean removeListener(IVizConnectionListener<T> listener);
}
