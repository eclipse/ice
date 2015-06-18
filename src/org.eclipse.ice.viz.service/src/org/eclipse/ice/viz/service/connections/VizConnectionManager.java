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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;

/**
 * This class manages multiple {@link IVizConnection}s and provides them to viz
 * services to distribute to appropriate plot instances (depending on their
 * URIs).
 * <p>
 * Furthermore, this manager also coordinates connection updates based on the
 * settings stored in the Eclipse preferences. At startup, it creates
 * connections for stored preferences and launches them in the background. These
 * connections can then be consumed by client code in the viz services.
 * </p>
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the underlying connection widget.
 */
public abstract class VizConnectionManager<T> {

	/**
	 * The listener that handles adding, updating, and removing viz connections
	 * to this manager. This needs to be registered with the current preference
	 * store.
	 */
	private final IPropertyChangeListener preferenceListener;

	/**
	 * A map of the viz connections keyed on their names, which should all be
	 * unique.
	 */
	private final Map<String, VizConnection<T>> connectionsByName;
	/**
	 * A map of the viz connection names keyed on the hosts. Multiple
	 * connections can be configured per host.
	 */
	private final Map<String, Set<String>> connectionsByHost;

	/**
	 * The current preference store associated with this manager. Connections
	 * should be loaded and updated based on the contents of this store.
	 */
	private CustomScopedPreferenceStore preferenceStore;

	/**
	 * The default constructor.
	 */
	public VizConnectionManager() {
		// Create the two maps.
		connectionsByName = new HashMap<String, VizConnection<T>>();
		connectionsByHost = new HashMap<String, Set<String>>();

		// Create the preference listener.
		preferenceListener = createPreferenceListener();
	}

	/**
	 * Gets the names of all available connections.
	 * 
	 * @return A lexicographically ordered set of available connection names.
	 */
	public Set<String> getConnections() {
		return null;
	}

	/**
	 * Gets all connections available for the specified host.
	 * 
	 * @param host
	 *            The host machine. Must not be {@code null}, but may be
	 *            "localhost" or otherwise a resolvable hostname.
	 * @return A lexicographically ordered set of connection names associated
	 *         with the specified host. This set may be empty if there are no
	 *         connections to the host.
	 * @throws NullPointerException
	 *             If the specified host is {@code null}.
	 */
	public Set<String> getConnectionsForHost(String host)
			throws NullPointerException {
		return null;
	}

	/**
	 * Gets the viz connection with the specified name. Names should be
	 * retrieved from either {@link #getConnections()} or
	 * {@link #getConnectionsForHost(String)}.
	 * 
	 * @param name
	 *            The name of the connection to acquire.
	 * @return The associated viz connection.
	 * @throws IllegalArgumentException
	 *             If there is no connection for the specified name.
	 */
	public IVizConnection<T> getConnection(String name)
			throws IllegalArgumentException {
		return null;
	}

	/**
	 * Sets the preference store used by the manager. This will first cause any
	 * existing connections to be terminated. If any connections can be loaded
	 * from the store, the manager will attempt to connect to them.
	 * 
	 * @param store
	 *            The new store. Should not be {@code null}.
	 * @param preferenceNodeId
	 *            The ID of the preference node. Connection preferences will be
	 *            found under this node. Should not be {@code null}.
	 * @throws NullPointerException
	 *             If either the store or the preference node ID are null.
	 */
	public void setPreferenceStore(CustomScopedPreferenceStore store,
			String preferenceNodeId) throws NullPointerException {
	}

	/**
	 * Adds a new connection based on the specified name and preference value.
	 * The connection will attempt to connect.
	 * 
	 * @param name
	 *            The name of the connection (a preference name in the store).
	 * @param preferences
	 *            The preference value for the connection. This value should
	 *            come straight from the {@link #preferenceStore}.
	 */
	private void addConnection(String name, String preferences) {

	}

	/**
	 * Removes a connection based on the specified name. The connection will be
	 * disconnected.
	 * 
	 * @param name
	 *            The name of the connection to remove.
	 */
	private void removeConnection(String name) {

	}

	/**
	 * Updates the properties for a connection based on the specified name and
	 * preference value. If necessary, the connection may be reset.
	 * 
	 * @param name
	 *            The name of the connection (a preference name in the store).
	 * @param preferences
	 *            The <i>new</i> preference value for the connection. This value
	 *            should come straight from the {@link #preferenceStore}.
	 */
	private void updateConnection(String name, String preferences) {

	}

	/**
	 * Creates a listener that appropriately adds, updates, or removes
	 * connections based on the values in the {@link #preferenceStore}.
	 * 
	 * @return A new property change listener that can be registered with the
	 *         preference store.
	 */
	private IPropertyChangeListener createPreferenceListener() {
		return null;
	}

	/**
	 * Creates a viz connection instance based on the name and the preference
	 * value from the preference store.
	 * 
	 * @param name
	 *            The name of the connection.
	 * @param preferences
	 *            The preference string from the store.
	 * @return A new viz connection instance using the provided name and
	 *         preferences.
	 */
	protected abstract VizConnection<T> createConnection(String name,
			String preferences);
}
