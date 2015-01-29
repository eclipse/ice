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
package org.eclipse.ice.viz.service.visit.preferences;

import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;

/**
 * Instances of this class act as an adapter for converting between
 * {@link IPreferenceStore} preferences and VisIt {@link ConnectionManager}s.
 * Loading or storing preferences for {@link Connection}s should be handled by
 * this class.
 * 
 * @author Jordan Deyton
 *
 */
public class ConnectionPreferenceAdapter {

	// TODO Update this class to just store/pull TableComponents.
	
	/**
	 * Each preference (e.g. host, port, etc.) is stored for all connections as
	 * a delimited string (e.g. host1;host2;host3). This is the separator used
	 * to separate the preferences for each connection.
	 */
	private static final String SEPARATOR = ";";

//	// TODO Test this class.
//
//	/**
//	 * Pulls all connection preferences from the specified
//	 * {@link IPreferenceStore} and loads them into the specified
//	 * {@link ConnectionManager}.
//	 * <p>
//	 * <b>Note:</b>All existing connections in the manager will be removed.
//	 * </p>
//	 * 
//	 * @param store
//	 *            The source preference store persisted by the Eclipse platform.
//	 * @param manager
//	 *            The destination manager for VisIt connections.
//	 */
//	public void toConnectionManager(IPreferenceStore store,
//			ConnectionManager manager) {
//		if (store == null || manager == null) {
//			throw new NullPointerException(
//					"ConnectionPreferenceAdapter error: "
//							+ "Null arguments not allowed.");
//		}
//
//		// Remove all previous connections from the manager.
//		manager.clear();
//
//		// Get all of the preferences. Each one is stored as a delimited string
//		// of values, one value per connection.
//		String prefix = "connection.";
//		String[] id = store.getString(prefix + "id").split(SEPARATOR);
//		String[] host = store.getString(prefix + "host").split(SEPARATOR);
//		String[] hostPort = store.getString(prefix + "hostPort").split(
//				SEPARATOR);
//		String[] path = store.getString(prefix + "path").split(SEPARATOR);
//		String[] proxy = store.getString(prefix + "proxy").split(SEPARATOR);
//		String[] proxyPort = store.getString(prefix + "proxyPort").split(
//				SEPARATOR);
//		String[] user = store.getString(prefix + "user").split(SEPARATOR);
//
//		// Convert each "column" of preferences to a Connection and add each
//		// Connection to the manager.
//		for (int i = 0; i < id.length; i++) {
//			// Create and update the connection.
//			Connection connection = new Connection();
//			connection.setId(id[i]);
//			connection.setHost(host[i]);
//			try {
//				connection.setHostPort(Integer.parseInt(hostPort[i]));
//			} catch (NumberFormatException e) {
//				System.err
//						.println("ConnectionPreferenceAdapter warning: "
//								+ "Bad host port found in preferences for connection \""
//								+ id + "\"");
//			}
//			connection.setPath(path[i]);
//			connection.setProxy(proxy[i]);
//			try {
//				connection.setProxyPort(Integer.parseInt(proxyPort[i]));
//			} catch (NumberFormatException e) {
//				System.err
//						.println("ConnectionPreferenceAdapter warning: "
//								+ "Bad proxy port found in preferences for connection \""
//								+ id + "\"");
//			}
//			connection.setUser(user[i]);
//			// Add the connection to the manager.
//			manager.addConnection(connection);
//		}
//
//		return;
//	}
//
//	/**
//	 * Pulls all connection preferences from the specified
//	 * {@link ConnectionManager} and loads them into the specified
//	 * {@link IPreferenceStore}.
//	 * <p>
//	 * <b>Note:</b>All existing connection preferences in the store will be
//	 * replaced.
//	 * </p>
//	 * 
//	 * @param manager
//	 *            The source manager for VisIt connections.
//	 * @param store
//	 *            The destination preference store persisted by the Eclipse
//	 *            platform.
//	 */
//	public void toPreferenceStore(ConnectionManager manager,
//			IPreferenceStore store) {
//		if (store == null || manager == null) {
//			throw new NullPointerException(
//					"ConnectionPreferenceAdapter error: "
//							+ "Null arguments not allowed.");
//		}
//
//		// Each preference needs to be stored as a delimited string of values.
//		// Start with an empty string, and append the preferences for each
//		// connection.
//		String prefix = "connection.";
//		String id = "";
//		String host = "";
//		String hostPort = "";
//		String path = "";
//		String proxy = "";
//		String proxyPort = "";
//		String user = "";
//
//		// Get the connections from the manager.
//		List<Connection> connections = manager.getConnections();
//
//		// For each connection, add each preference to the respective delimited
//		// preference string.
//		String delimit = "";
//		for (Connection connection : connections) {
//			id += delimit + connection.getId();
//			host += delimit + connection.getHost();
//			hostPort += delimit + Integer.toString(connection.getHostPort());
//			path += delimit + connection.getPath();
//			proxy += delimit + connection.getProxy();
//			proxyPort += delimit + Integer.toString(connection.getProxyPort());
//			user += delimit + connection.getUser();
//			// Set the delimiter. This only changes after the first iteration.
//			delimit = SEPARATOR;
//		}
//
//		// Store the delimited preference strings in the IPreferenceStore.
//		store.setValue(prefix + "id", id);
//		store.setValue(prefix + "host", host);
//		store.setValue(prefix + "hostPort", hostPort);
//		store.setValue(prefix + "path", path);
//		store.setValue(prefix + "proxy", proxy);
//		store.setValue(prefix + "proxyPort", proxyPort);
//		store.setValue(prefix + "user", user);
//
//		return;
//	}

}
