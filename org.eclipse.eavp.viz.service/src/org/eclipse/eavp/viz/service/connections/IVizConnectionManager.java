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

import java.util.Set;

import org.eclipse.eavp.viz.service.preferences.CustomScopedPreferenceStore;

/**
 * An implementation of this interface manages multiple {@link IVizConnection}s
 * and provides them to viz services to distribute to appropriate plot instances
 * (depending on their URIs).
 * <p>
 * Furthermore, it should also coordinates connection updates based on the
 * settings stored in the Eclipse preferences. At startup, it creates
 * connections for stored preferences and launches them in the background. These
 * connections can then be consumed by client code in a viz service.
 * </p>
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the underlying connection widget.
 */
public interface IVizConnectionManager<T> {

	/**
	 * The default delimiter for connection preferences when they are
	 * saved/loaded to Eclipse preferences.
	 */
	public static final String DEFAULT_CONNECTION_PREFERENCE_DELIMITER = ",";

	/**
	 * Gets the viz connection with the specified name. Names should be
	 * retrieved from either {@link #getConnections()} or
	 * {@link #getConnectionsForHost(String)}.
	 * 
	 * @param name
	 *            The name of the connection to acquire.
	 * @return The associated viz connection, or {@code null} if there is no
	 *         connection for the specified name.
	 */
	public IVizConnection<T> getConnection(String name);

	/**
	 * Gets the names of all available connections.
	 * 
	 * @return A lexicographically ordered set of available connection names.
	 */
	public Set<String> getConnections();

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
	public Set<String> getConnectionsForHost(String host) throws NullPointerException;

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
	 *             If the preference node ID is {@code null} and the store is
	 *             not.
	 */
	public void setPreferenceStore(CustomScopedPreferenceStore store, String preferenceNodeId)
			throws NullPointerException;

}