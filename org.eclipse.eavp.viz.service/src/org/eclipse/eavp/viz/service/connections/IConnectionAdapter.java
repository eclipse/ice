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
package org.eclipse.eavp.viz.service.connections;

import java.util.List;

import org.eclipse.eavp.viz.service.connections.preferences.ConnectionTable;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateable;

/**
 * This is an interface for adapters that wrap any sort of local or remote
 * connection. If provides feedback based on the current state of the connection
 * to registered {@link IUpdateableListener}s (see also:
 * {@link IConnectionClient}).
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the connection object.
 */
public interface IConnectionAdapter<T> extends IVizUpdateable {

	/**
	 * Connects to the associated connection if not already connected or
	 * connecting.
	 * <p>
	 * <b>Note:</b>This method will not block the caller thread, so it is the
	 * same as calling {@link #connect(boolean) connect(false)}.
	 * </p>
	 * 
	 * @return True if the connection is established upon returning, false
	 *         otherwise.
	 */
	boolean connect();

	/**
	 * Connects to the associated connection if not already connected or
	 * connecting.
	 *
	 * @param block
	 *            If true, then the calling thread will be blocked until the
	 *            connection succeeds or fails. If false, then the connection
	 *            process will not block the calling thread.
	 * @return True if the connection is established upon returning, false
	 *         otherwise.
	 */
	boolean connect(boolean block);

	/**
	 * Disconnects the associated connection if not already disconnected.
	 * <p>
	 * <b>Note:</b>This method will not block the caller thread, so it is the
	 * same as calling {@link #disconnect(boolean) disconnect(false)}.
	 * </p>
	 * 
	 * @return True if the connection is closed upon returning, false otherwise.
	 */
	boolean disconnect();

	/**
	 * Disconnects the associated connection if not already disconnected.
	 * 
	 * @param block
	 *            If true, then the calling thread will be blocked until the
	 *            disconnection succeeds or fails. If false, then the
	 *            disconnection process will not block the calling thread.
	 * @return True if the connection is closed upon returning, false otherwise.
	 */
	boolean disconnect(boolean block);

	/**
	 * Gets the connection managed by this adapter.
	 * 
	 * @return The associated connection.
	 */
	T getConnection();

	/**
	 * Gets the connection property corresponding to the specified key.
	 * 
	 * @param key
	 *            The key or ID of the required connection property.
	 * @return The value of the connection property, or {@code null} if the key
	 *         did not exist.
	 */
	String getConnectionProperty(String key);

	/**
	 * Gets the key currently associated with this connection. The value is
	 * usually maintained by a connection manager.
	 * 
	 * @return The connection key.
	 */
	String getKey();

	/**
	 * Gets the current state of the associated connection.
	 * 
	 * @return The current state of the associated connection.
	 */
	ConnectionState getState();

	/**
	 * Sets the connection's required properties based on the provided list of
	 * {@code Entry}s (usually a row from a {@link ConnectionTable}).
	 * <p>
	 * The associated connection should <i>not</i> be reset if the connection
	 * properties have changed.
	 * </p>
	 * 
	 * @param properties
	 *            The list of new connection properties.
	 * @return True if the new connection properties were valid and a change
	 *         occurred, false otherwise.
	 */
	boolean setConnectionProperties(List<VizEntry> properties);

	/**
	 * A convenience method to get the host for the connection. This will be
	 * either "localhost" or some other hostname/IP address.
	 * 
	 * @return The hostname.
	 */
	String getHost();

	/**
	 * A convenience method to get the port for the connection. This should be a
	 * valid port value.
	 * 
	 * @return The port for the connection, or -1 if the port is not required.
	 */
	int getPort();

	/**
	 * A convenience method to determine whether the associated connection is to
	 * a remote device.
	 * 
	 * @return True if the connection is remote, false otherwise (regardless of
	 *         connection state).
	 */
	boolean isRemote();
}
