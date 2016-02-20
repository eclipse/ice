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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.eavp.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.eavp.viz.service.datastructures.VizObject.VizObject;

/**
 * This class provides an adapter that wraps a connection. It provides feedback
 * based on the current {@link #state} of the connection.
 * <p>
 * There are certain properties of a connection that are managed by this class:
 * </p>
 * <ol>
 * <li>{@code key} - The key or name associated with the connection.</li>
 * <li>{@link #connection} - The connection object.</li>
 * <li>{@link #connectionProperties properties} - A map of properties required
 * to open or maintain a connection. This is handled by sub-classes.</li>
 * <li>{@link #state} - The current state of the connection.</li>
 * <li>{@code listeners} - Listeners registered with the adapter are notified
 * when the connection state changes.</li>
 * </ol>
 * <p>
 * This class sub-classes {@link ICEObject}, which manages the key and the
 * listeners. However, the key is only ever set via
 * {@link #setConnectionProperties(List)}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of the connection object.
 */
public abstract class ConnectionAdapter<T> extends VizObject implements
		IConnectionAdapter<T> {
	
	/**
	 * The current connection managed by this adapter.
	 */
	private T connection;
	/**
	 * The map of properties required to establish the {@link #connection}.
	 */
	private final Map<String, String> connectionProperties;
	/**
	 * The current state of the connection.
	 */
	private ConnectionState state;

	/**
	 * ConnectionJob is a subclass of the Eclipse Job class that 
	 * creates a new VizService connection in a manner that is more visible to 
	 * the end-user. It will launch itself as part of the Eclipse Jobs API 
	 * and publish vital information about its connecting status to the 
	 * Status Bar Progress bar and Progress View. If the Job fails, 
	 * a modal error dialog will be displayed so that the user knows 
	 * the VisIt tools will not work and can diagnose the problem with the 
	 * provided connection properties.  
	 * 
	 * @author Alex McCaskey
	 *
	 */
	protected abstract class ConnectionJob extends Job {

		/**
		 *  Reference to the connection object
		 */
		protected T connection;

		/**
		 * The Constructor
		 * 
		 * @param title
		 */
		public ConnectionJob(String title) {
			super(title);
		}

		/**
		 * This method let's the ConnectionAdapter grab the 
		 * created VizService connection instance. 
		 * 
		 * @return
		 */
		public T getConnection() {
			return connection;

		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.core.runtime.jobs.Job#run(org.eclipse.core.runtime.IProgressMonitor)
		 */
		@Override
		protected abstract IStatus run(IProgressMonitor monitor);
	}
	
	/**
	 * The default constructor.
	 */
	public ConnectionAdapter() {
		// Every connection starts off disconnected.
		state = ConnectionState.Disconnected;

		// Initialize the map of connection properties.
		connectionProperties = new HashMap<String, String>();
	}

	// ---- Connect, Disconnect, and implementations. ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.IConnectionAdapter#connect()
	 */
	@Override
	public boolean connect() {
		return connect(false);
	}

	/**
	 * Connects to the associated {@link #connection} if not already connected
	 * or connecting.
	 * <p>
	 * <b>Note:</b> This method is <i>not</i> intended to be overridden by
	 * sub-classes.
	 * </p>
	 *
	 * @param block
	 *            If true, then the calling thread will be blocked until the
	 *            connection succeeds or fails. If false, then the connection
	 *            process will not block the calling thread.
	 * @return True if the connection is established upon returning, false
	 *         otherwise.
	 */
	@Override
	public boolean connect(boolean block) {
		boolean connected = false;

		String key = getKey();

		logger.info("ConnectionAdapter message: "
				+ "Attempting to connect to \"" + key
				+ "\". The calling thread will " + (block ? "" : "not ")
				+ "be blocked.");

		if (state == ConnectionState.Connected) {
			connected = true;

			logger.info("ConnectionAdapter message: " + "Connection \""
					+ key + "\" is already connected.");

		} else if (state != ConnectionState.Connecting) {

			logger.info("ConnectionAdapter message: " + "Connection \""
					+ key + "\" is not connected.");

			// Create a new thread to open the connection.
			Thread thread = new Thread() {
				@Override
				public void run() {
					// Update the state.
					setState(ConnectionState.Connecting);

					// Try to open the connection.
					connection = openConnection();

					// Whether successful or not, update the state.
					if (connection != null) {
						setState(ConnectionState.Connected);
					} else {
						setState(ConnectionState.Failed);
					}
				}
			};
			thread.start();

			// If required, we must wait until the thread finishes. When that
			// happens, the connection should be open.
			if (block) {
				try {
					thread.join();
					// The connection is now open.
					connected = (state == ConnectionState.Connected);
				} catch (InterruptedException e) {
					// In the event the thread has an exception, show an error
					// and carry on.
					System.err.println("ConnectionAdapter error: "
							+ "Thread exception while opening connection \""
							+ key + "\".");
					logger.error(getClass().getName() + " Exception!",e);
				}
			}
		}

		return connected;
	}

	/**
	 * Opens a connection based on current settings in the adapter.
	 * <p>
	 * Implementations may assume that this thread is called from a worker
	 * thread.
	 * </p>
	 *
	 * @return The newly-opened connection, or {@code null} if the connection
	 *         could not be opened.
	 */
	protected abstract T openConnection();

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.connections.IConnectionAdapter#disconnect()
	 */
	@Override
	public boolean disconnect() {
		return disconnect(false);
	}

	/**
	 * Disconnects the associated {@link #connection} if not already
	 * disconnected.
	 * <p>
	 * <b>Note:</b> This method is <i>not</i> intended to be overridden by
	 * sub-classes.
	 * </p>
	 *
	 * @param block
	 *            If true, then the calling thread will be blocked until the
	 *            disconnection succeeds or fails. If false, then the
	 *            disconnection process will not block the calling thread.
	 * @return True if the connection is closed upon returning, false otherwise.
	 */
	@Override
	public boolean disconnect(boolean block) {
		boolean connected = false;

		String key = getKey();

		logger.info("ConnectionAdapter message: "
				+ "Attempting to disconnect from \"" + key
				+ "\". The calling thread will " + (block ? "" : "not ")
				+ "be blocked.");

		if (state == ConnectionState.Connected) {
			connected = true;

			logger.info("ConnectionAdapter message: " + "Connection \""
					+ key + "\" is connected. It will be disconnected.");

			// Create a new thread to close the connection.
			Thread thread = new Thread() {
				@Override
				public void run() {
					// Try to close the connection. If successful, update the
					// state.
					if (closeConnection(connection)) {
						setState(ConnectionState.Disconnected);
						connection = null;
					}
				}
			};
			// We do not want a daemon thread when closing... we want the
			// connection to close gracefully!
			thread.setDaemon(false);
			thread.start();

			// If required, we must wait until the thread finishes. When that
			// happens, the connection should be closed.
			if (block) {
				try {
					thread.join();
					// The connection is now closed.
					connected = (state == ConnectionState.Disconnected);
				} catch (InterruptedException e) {
					// In the event the thread has an exception, show an error
					// and carry on.
					System.err.println("ConnectionAdapter error: "
							+ "Thread exception while closing connection \""
							+ key + "\".");
					logger.error(getClass().getName() + " Exception!",e);
				}
			}
		} else {
			logger.info("ConnectionAdapter message: " + "Connection \""
					+ key + "\" is already disconnected.");
		}

		return !connected;
	}

	/**
	 * Closes the connection passed in as an argument. The adapter's current
	 * {@link #connection} will automatically be set to {@code null} after this
	 * operation completes.
	 * <p>
	 * Implementations may assume that this thread is called from a worker
	 * thread.
	 * </p>
	 *
	 * @param connection
	 *            The current connection to close.
	 * @return True if the connection was closed, false otherwise.
	 */
	protected abstract boolean closeConnection(T connection);

	// --------------------------------------------------- //

	// ---- Protected Methods (intended only for sub-classes) ---- //

	/**
	 * Gets the map of required connection properties. This should be used to
	 * update or read the properties when updating via
	 * {@link #setConnectionProperties(List)} or connecting via
	 * {@link #openConnection()}.
	 * 
	 * @return The map of required connection properties.
	 */
	protected Map<String, String> getConnectionProperties() {
		return connectionProperties;
	}

	/**
	 * This is a convenience method that updates the property stored in the
	 * {@link #connectionProperties} map and returns true if the value changed.
	 * 
	 * @param key
	 *            The key or ID of the property to change.
	 * @param value
	 *            The new value of the property.
	 * @return True if the value changed, false otherwise.
	 */
	protected boolean setConnectionProperty(String key, String value) {
		String oldValue = connectionProperties.put(key, value);
		return !(value != null ? value.equals(oldValue) : oldValue == null);
	}

	// ----------------------------------------------------------- //

	/**
	 * Sets the current connection state and notifies listeners of these
	 * changes.
	 * 
	 * @param state
	 *            The new connection state. If the same as before, nothing is
	 *            done.
	 */
	private void setState(ConnectionState state) {
		if (state != this.state) {
			logger.info("ConnectionAdapter message: " + "Connection \""
					+ getKey() + "\" is now " + state + ".");
			this.state = state;
			notifyListeners();
		}
	}

	// ---- Public Methods ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.connections.IConnectionAdapter#getConnection
	 * ()
	 */
	@Override
	public T getConnection() {
		return connection;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.IConnectionAdapter#
	 * getConnectionProperty(java.lang.String)
	 */
	@Override
	public String getConnectionProperty(String key) {
		return connectionProperties.get(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.IConnectionAdapter#getKey()
	 */
	@Override
	public String getKey() {
		return getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.connections.IConnectionAdapter#getState()
	 */
	@Override
	public ConnectionState getState() {
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.IConnectionAdapter#
	 * setConnectionProperties(java.util.List)
	 */
	@Override
	public abstract boolean setConnectionProperties(List<VizEntry> properties);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.IConnectionAdapter#getHost()
	 */
	@Override
	public String getHost() {
		return getConnectionProperty("host");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.IConnectionAdapter#getPort()
	 */
	@Override
	public int getPort() {
		String port = getConnectionProperty("port");
		return (port != null ? Integer.parseInt(port) : -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.connections.IConnectionAdapter#isRemote()
	 */
	@Override
	public boolean isRemote() {
		return !("localhost".equals(getHost()));
	}

	// ------------------------ //

	// ---- Extends ICEObject ---- //
	// Certain functionality of ICEObject should be overridden. The adapter's
	// properties (ID, name, description) should not be set except through
	// setting the connection properties. Also, listeners should not be
	// registered more than once.
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.datastructures.ICEObject.ICEObject#register(org.eclipse
	 * .ice.datastructures.ICEObject.IUpdateableListener)
	 */
	@Override
	public void register(IVizUpdateableListener listener) {
		// Don't allow double registration.
		if (!listeners.contains(listener)) {
			super.register(listener);
		}
	}

	/**
	 * Does nothing. The ID should be set as a connection property via
	 * {@link #setConnectionProperties(List)}.
	 */
	@Override
	public void setId(int id) {
		return;
	}

	/**
	 * Does nothing. The name (or key) should be set as a connection property
	 * via {@link #setConnectionProperties(List)}.
	 */
	@Override
	public void setName(String name) {
		return;
	}

	/**
	 * Does nothing. The description should be set as a connection property via
	 * {@link #setConnectionProperties(List)}.
	 */
	@Override
	public void setDescription(String description) {
		return;
	}
	// --------------------------- //
}
