/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.ice.viz.service.preferences.TableComponentPreferenceAdapter;

/**
 * A {@code ConnectionManager} maps {@link IConnectionAdapter}s to
 * {@link IConnectionClient}s. This class manages the following aspects of the
 * connection-client lifecycle:
 * <ul>
 * <li>the set of connections, connecting and disconnecting them</li>
 * <li>synchronizing the connections with what is specified in the preference
 * store</li>
 * <li>associating multiple clients with a single connection</li>
 * </ul>
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the connection object.
 */
public abstract class ConnectionManager<T> {

	// TODO Implement support for multiple connections rather than a single
	// default connection.

	/**
	 * The table of connection properties. Its content is based on what is in
	 * the preference store.
	 */
	private final ConnectionTable table;

	/**
	 * The default connection adapter. It interfaces with the underlying
	 * connection utilities.
	 */
	private IConnectionAdapter<T> adapter;

	/**
	 * A list of connection clients associated with the default {@link #adapter}
	 * .
	 */
	private final List<IConnectionClient<T>> clients;

	/**
	 * The default constructor. This should only be called by sub-classes.
	 */
	protected ConnectionManager() {

		// Initialize the list of created plots.
		clients = new ArrayList<IConnectionClient<T>>();

		// Initialize the connection manager based on the stored preferences.
		table = createConnectionTable();
		TableComponentPreferenceAdapter tableAdapter;
		tableAdapter = new TableComponentPreferenceAdapter();
		tableAdapter.toTableComponent(
				(CustomScopedPreferenceStore) getPreferenceStore(), table);

		// Create the default adapter based on the current properties. Do not
		// connect yet.
		updateAdapter(false);

		return;
	}

	/**
	 * Gets the preference store in which the connection properties for all
	 * adapters are stored.
	 * 
	 * @return The preference store. This value should not be {@code null}.
	 */
	protected abstract CustomScopedPreferenceStore getPreferenceStore();

	/**
	 * Creates an empty, default {@link #table} of connection preferences.
	 * 
	 * @return A new table of connection properties. Its content is based on
	 *         what is in the preference store.
	 */
	protected abstract ConnectionTable createConnectionTable();

	/**
	 * Creates a disconnected, default connection {@link #adapter}.
	 * 
	 * @return A new connection adapter.
	 */
	protected abstract IConnectionAdapter<T> createConnectionAdapter();

	/**
	 * Updates the default adapter based on the current preferences. If there is
	 * no default connection configured in the preference page, then after this
	 * call, the {@link #adapter} will be {@code null}.
	 * 
	 * @param connect
	 *            Whether or not to attempt connecting to the default adapter if
	 *            a change occurred.
	 */
	private void updateAdapter(boolean connect) {

		// If the default connection exists, load it into an adapter.
		String key = getDefaultConnectionKey();
		if (key != null) {
			// If the adapter does not exist, create it.
			if (adapter == null) {
				adapter = createConnectionAdapter();

				// Notify the plots that the connection has been configured.
				for (IConnectionClient<T> client : clients) {
					client.setConnectionAdapter(adapter);
				}
			}
			// Set the adapter's properties. If a change occurred and the method
			// parameter says we need to attempt a connection, re-establish the
			// adapter's connection.
			List<Entry> row = table.getConnection(key);
			if (adapter.setConnectionProperties(row) && connect) {
				Thread thread = new Thread() {
					@Override
					public void run() {
						// Disconnect, then immediately reconnect.
						adapter.disconnect(true);
						adapter.connect(false);
					}
				};
				thread.start();
			}
		}
		// Otherwise, if the adapter already exists, destroy it..
		else if (adapter != null) {
			ConnectionState state = adapter.getState();
			// Disconnect if necessary.
			if (state == ConnectionState.Connected) {
				adapter.disconnect(false);
			}
			adapter = null;

			// Notify the plots that the connection has been closed.
			for (IConnectionClient<T> client : clients) {
				client.setConnectionAdapter(adapter);
			}
		}

		return;
	}

	/**
	 * This method notifies the manager that the preferences have changed. Any
	 * connections that have changed should be reset.
	 */
	public void preferencesChanged(Map<String, String> changedKeys,
			Set<String> addedKeys, Set<String> removedKeys) {

		// Clear the old connection preferences.
		for (int i = 0; i < table.numberOfRows(); i++) {
			table.deleteRow(0);
		}

		// Update the connection preferences based on the stored preferences.
		TableComponentPreferenceAdapter tableAdapter;
		tableAdapter = new TableComponentPreferenceAdapter();
		tableAdapter.toTableComponent(
				(CustomScopedPreferenceStore) getPreferenceStore(), table);

		// TODO When we have multiple connections, we will need to do the
		// following, then send the new connection adapters to the plots.
		// Remove all old connections.
		// Update all existing connections.
		// Add all new connections.

		// Update the default adapter and connect if possible.
		updateAdapter(true);

		return;
	}

	/**
	 * Connects to the default {@link #adapter}. This operation does not block
	 * the caller if the connection needs to be established.
	 * 
	 * @return True if the connection is established, false otherwise.
	 */
	public boolean connect() {
		return (adapter != null ? adapter.connect(false) : false);
	}

	/**
	 * Disconnects from the default {@link #adapter}. This operation does not
	 * block the caller if the connection needs to be closed.
	 * 
	 * @return True if the connection is disconnected, false otherwise.
	 */
	public boolean disconnect() {
		return (adapter != null ? adapter.disconnect(false) : false);
	}

	/**
	 * Adds a new connection client to the default {@link #adapter}. This method
	 * automatically sets the client's connection adapter.
	 * 
	 * @param client
	 *            The client to add.
	 * @return True if the added client was not null and was not already in the
	 *         list, false otherwise (in which case nothing is added).
	 */
	public boolean addClient(IConnectionClient<T> client) {
		boolean added = false;
		if (client != null && !clients.contains(client)) {
			clients.add(client);
			client.setConnectionAdapter(adapter);
			added = true;
		}
		return added;
	}

	/**
	 * Removes a connection client from the default {@link #adapter}. This
	 * method automatically unsets the client's connection adapter.
	 * 
	 * @param client
	 *            The client to remove.
	 * @return True if the added client was not null and was already in the
	 *         list, false otherwise (in which case nothing was removed).
	 */
	public boolean removeClient(IConnectionClient<T> client) {
		boolean removed = clients.remove(client);
		if (removed) {
			client.setConnectionAdapter(null);
		}
		return removed;
	}

	/**
	 * Gets the key ("connection ID") associated with the default connection.
	 * This can be set on the preference page.
	 * 
	 * @return The key for the default connection, or null if one is not set.
	 */
	private String getDefaultConnectionKey() {
		int id = getPreferenceStore().getInt("defaultConnection");
		List<String> connectionNames = table.getConnectionNames();
		return (id < connectionNames.size() ? connectionNames.get(id) : null);
	}

}
