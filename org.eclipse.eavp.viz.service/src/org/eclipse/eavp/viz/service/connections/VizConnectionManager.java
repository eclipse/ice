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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.IPreferenceChangeListener;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.eavp.viz.service.preferences.CustomScopedPreferenceStore;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a basic implementation of {@link IVizConnectionManager},
 * which is responsible for synchronizing {@link IVizConnection}s with Eclipse
 * preferences.
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the underlying connection widget.
 */
public abstract class VizConnectionManager<T>
		implements IVizConnectionManager<T> {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(VizConnectionManager.class);

	/**
	 * The listener that handles adding, updating, and removing viz connections
	 * to this manager. This needs to be registered with the current preference
	 * store's node under which connection preferences are stored.
	 */
	private final IPreferenceChangeListener preferenceListener;

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
	 * The ID of the preference node under which connection information will be
	 * stored.
	 */
	private String connectionsNodeId;

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
		logger.debug("VizConnectionManager message: " + "Adding connection \""
				+ name + "\" using the preference string \"" + preferences
				+ "\".");

		VizConnection<T> connection = createConnection(name, preferences);

		// Split the string using the delimiter. The -1 is necessary to include
		// empty values from the split.
		String[] split = preferences.split(getConnectionPreferenceDelimiter(),
				-1);

		try {
			// Ensure the connection's basic preferences are set.
			connection.setName(name);
			connection.setHost(split[0]);
			connection.setPort(Integer.parseInt(split[1]));
			connection.setPath(split[2]);

			// Add the connection to the map of connections by name.
			connectionsByName.put(name, connection);

			// Add the connection to the map of connections by host.
			String host = connection.getHost();
			Set<String> connections = connectionsByHost.get(host);
			// If necessary, create a new set for the connection's host.
			if (connections == null) {
				connections = new HashSet<String>();
				connectionsByHost.put(host, connections);
			}
			connections.add(name);

			// Try to connect.
			connection.connect();

		} catch (IndexOutOfBoundsException | NullPointerException
				| NumberFormatException e) {
			// Cannot add the connection.
		}

		return;
	}

	/**
	 * Creates a viz connection instance based on the name and the preference
	 * value from the preference store.
	 * <p>
	 * The name and preferences are provided as a convenience in case additional
	 * preferences besides the name, host, port, and path are required. After
	 * this method is called, the host, port, and path will be pulled from the
	 * preference string using the delimiter provided by
	 * {@link #getConnectionPreferenceDelimiter()} , if possible. Additional
	 * preferences should be located in the preference string <i>after</i> these
	 * three required preferences.
	 * </p>
	 * 
	 * @param name
	 *            The name of the connection.
	 * @param preferences
	 *            The preference string from the store.
	 * @return A new viz connection instance using the provided name and
	 *         preferences, or {@code null} if the properties could not be
	 *         sufficiently read from the string to create a connection.
	 */
	protected abstract VizConnection<T> createConnection(String name,
			String preferences);

	/**
	 * Creates a listener that appropriately adds, updates, or removes
	 * connections based on the values in the {@link #preferenceStore}.
	 * 
	 * @return A new property change listener that can be registered with the
	 *         preference store.
	 */
	private IPreferenceChangeListener createPreferenceListener() {
		return new IPreferenceChangeListener() {
			@Override
			public void preferenceChange(PreferenceChangeEvent event) {
				String name = event.getKey();
				Object oldValue = event.getOldValue();
				Object newValue = event.getNewValue();

				// Add, update, or remove depending on whether the old/new
				// values are null.
				if (oldValue != null) {
					if (newValue != null) {
						updateConnection(name, newValue.toString());
					} else {
						removeConnection(name);
					}
				} else if (newValue != null) {
					addConnection(name, newValue.toString());
				}

				return;
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.IVizConnectionManager#getConnection(java.lang.String)
	 */
	@Override
	public IVizConnection<T> getConnection(String name) {
		return connectionsByName.get(name);
	}

	/**
	 * Gets the delimiter used to separate a connection's individual
	 * preferences.
	 * 
	 * @return The string delimiter for connection preferences.
	 */
	protected String getConnectionPreferenceDelimiter() {
		return DEFAULT_CONNECTION_PREFERENCE_DELIMITER;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.IVizConnectionManager#getConnections()
	 */
	@Override
	public Set<String> getConnections() {
		return new TreeSet<String>(connectionsByName.keySet());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.IVizConnectionManager#getConnectionsForHost(java.lang.String)
	 */
	@Override
	public Set<String> getConnectionsForHost(String host)
			throws NullPointerException {
		// Throw an exception if the specified host name is null.
		if (host == null) {
			throw new NullPointerException("VizConnectionManager error: "
					+ "Cannot find connections for null host name.");
		}
		// Get the associated connection names. If the host is not recognized,
		// return an empty set.
		Set<String> connections = connectionsByHost.get(host);
		return connections != null ? new TreeSet<String>(connections)
				: new TreeSet<String>();
	}

	/**
	 * Removes a connection based on the specified name. The connection will be
	 * disconnected.
	 * 
	 * @param name
	 *            The name of the connection to remove.
	 */
	private void removeConnection(String name) {
		logger.debug("VizConnectionManager message: " + "Removing connection \""
				+ name + "\".");

		// Remove the associated connection from the map of connections by name.
		VizConnection<T> connection = connectionsByName.remove(name);

		// Remove the connection from the map of connections by host.
		String host = connection.getHost();
		Set<String> connections = connectionsByHost.get(host);
		connections.remove(name);
		// If there are no more connections for the host, remove the host.
		if (connections.isEmpty()) {
			connectionsByHost.remove(host);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.IVizConnectionManager#setPreferenceStore(org.eclipse.eavp.viz.service.preferences.CustomScopedPreferenceStore, java.lang.String)
	 */
	@Override
	public void setPreferenceStore(CustomScopedPreferenceStore store,
			String preferenceNodeId) throws NullPointerException {
		// Throw an exception if the preference node ID is null. We must have a
		// valid node ID if we have a store.
		if (store != null && preferenceNodeId == null) {
			throw new NullPointerException("VizConnectionManager error: "
					+ "Preference node ID cannot be null.");
		}

		if (store != preferenceStore
				|| !preferenceNodeId.equals(connectionsNodeId)) {
			// If the old store/node ID is valid, unregister the preferences
			// listener and remove all current connections from the manager.
			if (preferenceStore != null) {
				preferenceStore.getNode(connectionsNodeId)
						.removePreferenceChangeListener(preferenceListener);
				preferenceStore = null;
				connectionsNodeId = null;

				// Remove all current connections.
				connectionsByName.clear();
				connectionsByHost.clear();
			}

			// If the new store/node ID is valid, add all new connections, then
			// register the preferences listener.
			if (store != null) {
				// Get the node under which the connections will be stored.
				IEclipsePreferences node = store.getNode(preferenceNodeId);
				// Add all connections in the new preference store.
				try {
					String[] connectionNames = node.keys();
					for (String connection : connectionNames) {
						String preferences = node.get(connection, null);
						addConnection(connection, preferences);
					}
				} catch (BackingStoreException e) {
					e.printStackTrace();
				}

				// Register with the new store.
				node.addPreferenceChangeListener(preferenceListener);
			}

			// Update the references to the store and the ID.
			preferenceStore = store;
			connectionsNodeId = preferenceNodeId;
		}

		return;
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
		logger.debug("VizConnectionManager message: " + "Updating connection \""
				+ name + "\" using the preference string \"" + preferences
				+ "\".");

		final VizConnection<T> connection = connectionsByName.get(name);

		// Get the current host for the connection.
		String oldHost = connection.getHost();

		// Update the connection's preferences.
		boolean requiresReset = updateConnectionPreferences(connection,
				preferences);

		// If the host changed, we need to update the connections-by-host map.
		String newHost = connection.getHost();
		if (!oldHost.equals(newHost)) {
			// Dissociate the connection from the old host, deleting the map
			// entry for the old host if it has no more associated connections.
			Set<String> hosts = connectionsByHost.get(oldHost);
			hosts.remove(name);
			if (hosts.isEmpty()) {
				connectionsByHost.remove(oldHost);
			}
			// Associate the connection with the new host, creating the map
			// entry for the new host if it had no associated connections.
			hosts = connectionsByHost.get(newHost);
			if (hosts == null) {
				hosts = new HashSet<String>();
				connectionsByHost.put(newHost, hosts);
			}
			hosts.add(name);
		}

		// If the update requires a reset, reset the connection.
		if (requiresReset) {
			final Future<ConnectionState> disconnectRequest = connection
					.disconnect();
			final ExecutorService executor = Executors
					.newSingleThreadExecutor();
			executor.submit(new Runnable() {
				@Override
				public void run() {
					// Wait for the disconnect request to complete or fail.
					try {
						disconnectRequest.get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					// Try to re-connect.
					connection.connect();

					// Stop the executor service.
					executor.shutdown();
				}
			});
		}

		return;
	}

	/**
	 * Gets each connection property from the string of preferences, if
	 * possible, and updates the connection based on them.
	 * <p>
	 * <b>Note:</b> If overridden, it is recommended to call the super method so
	 * that the host, port, and path will be updated. This method will return
	 * true if any of those three properties change.
	 * </p>
	 * 
	 * @param connection
	 *            The connection whose preferences are being updated.
	 * @param preferences
	 *            The serialized string of preferences.
	 * 
	 * @return True if one of the properties changed and a reset of the
	 *         connection is required, false if a reset is <i>not</i> required.
	 */
	protected boolean updateConnectionPreferences(VizConnection<T> connection,
			String preferences) {
		boolean requiresReset = false;

		// Split the string using the delimiter. The -1 is necessary to include
		// empty values from the split.
		String[] split = preferences.split(getConnectionPreferenceDelimiter(),
				-1);

		try {
			// Get the host, port, and path, if possible.
			String host = split[0];
			int port = Integer.parseInt(split[1]);
			String path = split[2];

			// If any of these change, the connection will need to be reset.
			requiresReset |= connection.setHost(host);
			requiresReset |= connection.setPort(port);
			requiresReset |= connection.setPath(path);
		} catch (IndexOutOfBoundsException | NullPointerException
				| NumberFormatException e) {
			// Cannot update the connection.
			requiresReset = false;
		}

		return requiresReset;
	}
}
