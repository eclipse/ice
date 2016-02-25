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

import java.net.URI;
import java.util.Set;

import org.eclipse.eavp.viz.service.AbstractVizService;
import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.preferences.CustomScopedPreferenceStore;

/**
 * This class provides a base class for connection-based viz services. It
 * primarily serves to simplify the code required when providing such a viz
 * service that uses a specific implementation of {@link IVizConnection}.
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the connection object.
 */
public abstract class ConnectionVizService<T> extends AbstractVizService {

	/**
	 * The associated connection manager. Its concrete type is irrelevant, as it
	 * only needs access to the underlying {@link IVizConnection}s, which will
	 * be passed to created {@link ConnectionPlot}s.
	 */
	private final IVizConnectionManager<T> manager;

	/**
	 * The default constructor.
	 */
	public ConnectionVizService() {
		// Create the manager for the connections.
		manager = createConnectionManager();

		// Associate the manager with the preference store.
		CustomScopedPreferenceStore store;
		store = (CustomScopedPreferenceStore) getPreferenceStore();
		String connectionNodeId = getConnectionPreferencesNodeId();
		manager.setPreferenceStore(store, connectionNodeId);

		return;
	}

	/**
	 * Creates the connection manager used by this service to manage viz
	 * connections.
	 * 
	 * @return The connection manager for this viz service.
	 */
	protected abstract IVizConnectionManager<T> createConnectionManager();

	/**
	 * Creates a new, empty plot that can handle a viz connection.
	 * 
	 * @return A new, empty connection plot.
	 */
	protected abstract ConnectionPlot<T> createConnectionPlot();

	/**
	 * Overrides the default behavior from {@link AbstractVizService} to find a
	 * viz connection that can handle the URI based on its host (a {@code null}
	 * host is assumed to be local).
	 */
	public IPlot createPlot(URI uri) throws Exception {
		// Check for a null URI and an unsupported extension.
		super.createPlot(uri);

		// TODO Change this to use the IP address for the lookup.
		// Get the host from the URI.
		String host = uri.getHost();
		if (host == null) {
			host = "localhost";
		}

		// Get the next available connection for the URI's host.
		Set<String> availableConnections = manager.getConnectionsForHost(host);
		if (availableConnections.isEmpty()) {
			throw new Exception("ConnectionVizService error: "
					+ "No configured connection to host \"" + host + "\".");
		}
		String name = availableConnections.iterator().next();
		IVizConnection<T> connection = manager.getConnection(name);

		// Create a plot using the sub-class' implementation.
		ConnectionPlot<T> plot = createConnectionPlot();

		// Set its connection and data source.
		plot.setConnection(connection);
		plot.setDataSource(uri);

		return plot;
	}

	/**
	 * Gets the ID of the preferences node under which all connections will be
	 * stored.
	 * 
	 * @return The preference node ID for persisted connection information.
	 */
	protected abstract String getConnectionPreferencesNodeId();

}
