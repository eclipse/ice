/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtConnectionManager;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.osgi.framework.FrameworkUtil;

/**
 * This is an implementation of the IVizService interface for the VisIt
 * visualization tool.
 * 
 * @author Jay Jay Billings
 * 
 */
public class VisItVizService implements IVizService {

	// TODO Address multiple connections associated with various plots. For now,
	// we just have a single, default connection.

	/**
	 * This enum defines the possible states of a connection to a VisIt
	 * instance. This is intended for use with the plots so they can keep users
	 * updated based on the state of the associated VisIt connection.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	protected enum ConnectionState {
		/**
		 * The connection is being established.
		 */
		Connecting,
		/**
		 * The connection is opened.
		 */
		Connected,
		/**
		 * The connection failed to open.
		 */
		Failed,
		/**
		 * The connection is closed.
		 */
		Disconnected;
	}

	/**
	 * The current state of the default connection.
	 */
	private ConnectionState state;

	/**
	 * A reference to the associated preference page's {@link IPreferenceStore}.
	 * If this has been determined previously, then it should be returned in
	 * {@link #getPreferenceStore()}.
	 */
	private IPreferenceStore preferenceStore;
	
	/**
	 * A list of all currently-drawn plots.
	 */
	private final List<VisItPlot> plots;
	
	/**
	 * The default constructor.
	 */
 	public VisItVizService() {
 		
 		// Initialize the final class collections.
 		plots = new ArrayList<VisItPlot>();
 		
 		// Initially, the service is disconnected.
 		state = ConnectionState.Disconnected;
 		
 		// FIXME Remove this.
 		instance = this;
	}
 	
 	// TODO Remove these three attributes/operations.
 	private static VisItVizService instance;
 	public static VisItVizService getInstance() {
 		return instance;
 	}
 	public void preferencesChanged() {
 		Thread thread = new Thread() {
 			@Override
			public void run() {
				closeDefaultConnection(true);
				openDefaultConnection(true);
			}
 		};
		thread.start();
		// FIXME We should probably use a property change listener registered
		// with the preference store, but that fires once per changed property.
 	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#getName()
	 */
	@Override
	public String getName() {
		return "VisIt";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#getVersion()
	 */
	@Override
	public String getVersion() {
		return "2.8.2";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * hasConnectionProperties()
	 */
	@Override
	public boolean hasConnectionProperties() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * getConnectionProperties()
	 */
	@Override
	public Map<String, String> getConnectionProperties() {
		Map<String, String> preferences = new TreeMap<String, String>();
		IPreferenceStore store = getPreferenceStore();

		for (ConnectionPreference p : ConnectionPreference.values()) {
			String preferenceId = p.toString();
			preferences.put(preferenceId, store.getString(preferenceId));
		}

		return preferences;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * setConnectionProperties(java.util.Map)
	 */
	@Override
	public void setConnectionProperties(Map<String, String> props) {
		if (props != null) {
			IPreferenceStore store = getPreferenceStore();

			for (Entry<String, String> prop : props.entrySet()) {
				String preferenceId = prop.getKey();
				try {
					ConnectionPreference.valueOf(preferenceId);
					store.setValue(preferenceId, prop.getValue());
				} catch (IllegalArgumentException e) {
					// Could not process the current property.
				}
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#connect()
	 */
	@Override
	public boolean connect() {
		// TODO We need to create any connections that are supposed to be open
		// by default.
		// If we are trying to connect, don't try connecting again.
		return state != ConnectionState.Connecting
				&& openDefaultConnection(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#disconnect()
	 */
	@Override
	public boolean disconnect() {
		// TODO We need to disconnect any open connections.
		return closeDefaultConnection(false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizService#createPlot(java
	 * .net.URI)
	 */
	@Override
	public IPlot createPlot(URI file) throws Exception {
		VisItPlot plot = null;
		if (file != null) {
			plot = new VisItPlot(file, this);
			plot.setProperties(getDefaultPlotProperties());

			// Keep track of the existing plots.
			plots.add(plot);
		}
		return plot;
	}

	/**
	 * Gets the current state of the default connection.
	 * 
	 * @return The current state of the VizService's default connection.
	 */
	protected ConnectionState getConnectionState() {
		return state;
	}

	/**
	 * Gets the {@link IPreferenceStore} for the associated preference page.
	 * 
	 * @return The {@code IPreferenceStore} whose defaults should be set.
	 */
	private IPreferenceStore getPreferenceStore() {
		if (preferenceStore == null) {
			// Get the PreferenceStore for the bundle.
			String id = FrameworkUtil.getBundle(getClass()).getSymbolicName();
			preferenceStore = new ScopedPreferenceStore(InstanceScope.INSTANCE,
					id);
		}
		return preferenceStore;
	}

	/**
	 * Gets the default VisIt plot properties.
	 * 
	 * @return The default VisIt plot properties, stored as a map of strings
	 *         keyed on strings.
	 */
	private Map<String, String> getDefaultPlotProperties() {
		// TODO
		return new HashMap<String, String>();
	}
	
	// ---- Connection Management Methods ---- //
	/**
	 * Gets the VisIt connection ID associated with the default connection.
	 * 
	 * @return The default connection ID.
	 */
	private String getDefaultConnectionId() {
		return getPreferenceStore().getString(
				ConnectionPreference.ConnectionID.toString());
	}

	/**
	 * Determines whether or not a VisIt connection with the specified ID exists
	 * and is connected.
	 * 
	 * @param id
	 *            The VisIt ID for the connection.
	 * @return True if the associated connection is established, false
	 *         otherwise.
	 */
	private boolean hasConnection(String id) {
		boolean found = VisItSwtConnectionManager.hasConnection(id);
		System.out.println("VisItVizService message: "
				+ "Connection with id \"" + id + "\" " + (found ? "" : "not ")
				+ "found.");
		return found;
	}

//	/**
//	 * Determines whether or not the default VisIt connection exists and is
//	 * connected. This is a convenience method.
//	 * 
//	 * @return True if the default connection is established, false otherwise.
//	 */
//	private boolean hasDefaultConnection() {
//		return hasConnection(getDefaultConnectionId());
//	}

	/**
	 * Opens the connection with the specified VisIt ID.
	 * 
	 * @param id
	 *            The VisIt ID of the connection.
	 * @param block
	 *            Whether or not to block the caller while the connection is
	 *            opened.
	 * @return True if the connection is open by the time this method returns,
	 *         false otherwise.
	 */
	private boolean openConnection(String id, boolean block) {
		boolean open = true;

		if (!hasConnection(id)) {
			System.out.println("VisItVizService message: "
					+ "Opening connection \"" + id + "\".");
			// THe connection is closed and needs to be opened.
			open = false;

			// Create a new thread to open the connection.
			final String connId = id;
			Thread thread = new Thread() {
				@Override
				public void run() {

					state = ConnectionState.Connecting;

					// Notify all plots that the connection is opening.
					for (VisItPlot plot : plots) {
						plot.updateConnection(connId,
								ConnectionState.Connecting);
					}

					// Get the PreferenceStore. All VisIt connection preferences
					// are stored here.
					IPreferenceStore store = getPreferenceStore();

					// Construct the input map required to start VisIt.
					Map<String, String> visitPreferences = new HashMap<String, String>();
					for (ConnectionPreference p : ConnectionPreference.values()) {
						visitPreferences.put(p.getID(),
								store.getString(p.toString()));
						// TODO Remove this output.
						System.out.println(p.getID() + ": "
								+ visitPreferences.get(p.getID()));
					}

					// FIXME This should be cleaned up or handled elsewhere.
					// Local connections should have the "useTunneling" flag set
					// to "false". Remote connections should have it set to
					// "true".
					if ("localhost".equals(visitPreferences
							.get(ConnectionPreference.Host.getID()))) {
						visitPreferences.put(
								ConnectionPreference.UseTunneling.getID(),
								"false");
					} else {
						visitPreferences.put(
								ConnectionPreference.UseTunneling.getID(),
								"true");
					}

					// TODO The VisItSwtConnection shouldn't need a Shell until
					// a VisItSwtWidget is created. When the VisIt SWT code is
					// fixed to only use UI resources when a UI component is
					// created, we will need to remove the below logic.
					// The VisItSwtConnection requires a Shell. To get a shell,
					// we must have a Display! Since this code is frequently
					// called near the initial startup, we need to get the
					// default Display and keep trying until we get one. Only
					// then can we get a new Shell.
					final AtomicReference<Display> display = new AtomicReference<Display>();
					final AtomicReference<Shell> shell = new AtomicReference<Shell>();
					while (display.compareAndSet(null, Display.getDefault())) {
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					display.get().syncExec(new Runnable() {
						@Override
						public void run() {
							shell.set(new Shell(display.get()));
						}
					});

					// Try to open the connection.
					try {
						VisItSwtConnection connection = VisItSwtConnectionManager
								.createConnection(connId, shell.get(),
										visitPreferences);
						// FIXME We should just interpret the return value.
						if (connection == null) {
							throw new Exception();
						}
						state = ConnectionState.Connected;

						// Connection successful!
						System.out.println("VisItVizService message: "
								+ "Connection \"" + connId
								+ "\" has been opened.");

						// Notify all plots that the connection opened.
						for (VisItPlot plot : plots) {
							plot.updateConnection(connId,
									ConnectionState.Connected);
						}
					} catch (Exception e) {
						// Connection failed!
						System.err.println("VisItVizService error: "
								+ "Failed to create connection \"" + connId
								+ "\".");
						state = ConnectionState.Failed;

						// Notify all plots that the connection opened.
						for (VisItPlot plot : plots) {
							plot.updateConnection(connId,
									ConnectionState.Failed);
						}
					}

					return;
				}
			};

			// Normally, we do not need to block. Launch the thread and let the
			// connection open.
			if (!block) {
				// It doesn't matter yet whether or not the thread is a daemon
				// thread... if ICE stops before the connection has been
				// established, closing the connection gracefully is difficult.
				thread.start();
			}
			// Otherwise, we need to block the caller until the connection is
			// opened.
			else {
				thread.start();
				try {
					thread.join();
					// The connection is now open.
					open = (state == ConnectionState.Connected);
				} catch (InterruptedException e) {
					// In the event the thread has an exception, show an error
					// and carry on.
					System.err.println("VisItVizService error: "
							+ "Thread exception while opening connection \""
							+ id + "\".");
					e.printStackTrace();
				}
			}
		} else {
			System.out.println("VisItVizService message: " + "Connection \""
					+ id + "\" is already established.");
		}

		return open;
	}

	/**
	 * Opens the default VisIt connection. This is a convenience method.
	 * 
	 * @param block
	 *            Whether or not to block the caller while the connection is
	 *            opened.
	 * @return True if the default connection is open by the time this method
	 *         returns, false otherwise.
	 */
	private boolean openDefaultConnection(boolean block) {
		return openConnection(getDefaultConnectionId(), block);
	}

	/**
	 * Gets the connection with the specified VisIt ID.
	 * 
	 * @param id
	 *            The VisIt ID of the connection.
	 * @return The connection if it exists and is open. Null otherwise.
	 */
	private VisItSwtConnection getConnection(String id) {
		VisItSwtConnection connection = null;

		// In this case, we only need to fetch the connection. Do not attempt to
		// open it!
		if (hasConnection(id)) {
			connection = VisItSwtConnectionManager.getConnection(id);
		}

		return connection;
	}

	/**
	 * Gets the default VisIt connection. This is a convenience method.
	 * 
	 * @return The default connection if it exists and is open. Null otherwise.
	 */
	protected VisItSwtConnection getDefaultConnection() {
		return getConnection(getDefaultConnectionId());
	}

	/**
	 * Closes the connection with the specified VisIt ID.
	 * 
	 * @param id
	 *            The VisIt ID of the connection.
	 * @param block
	 *            Whether or not to block the caller while the connection is
	 *            closed.
	 * @return True if the connection is closed by the time this method returns,
	 *         false otherwise.
	 */
	private boolean closeConnection(String id, boolean block) {
		boolean closed = true;

		if (hasConnection(id)) {
			// The connection is open and needs to be closed.
			closed = false;

			// Create a new thread to close the connection.
			final String connId = id;
			Thread thread = new Thread() {
				@Override
				public void run() {
					VisItSwtConnectionManager.getConnection(connId).close();
					System.out.println("VisItVizService message: "
							+ "Connection \"" + connId + "\" has been closed.");

					state = ConnectionState.Disconnected;

					// Notify all plots that the connection closed.
					for (VisItPlot plot : plots) {
						plot.updateConnection(connId,
								ConnectionState.Disconnected);
					}
				}
			};

			// Normally, we do not need to block. Launch the thread and let the
			// connection close.
			if (!block) {
				// We do not want a daemon thread when closing... we want the
				// connection to close gracefully!
				thread.setDaemon(false);
				thread.start();
			}
			// Otherwise, we need to block the caller until the connection is
			// closed.
			else {
				thread.start();
				try {
					thread.join();
					// The connection is now closed.
					closed = (state == ConnectionState.Disconnected);
				} catch (InterruptedException e) {
					// In the event the thread has an exception, show an error
					// and carry on.
					System.err.println("VisItVizService error: "
							+ "Thread exception while closing connection \""
							+ id + "\".");
					e.printStackTrace();
				}
			}
		}

		return closed;
	}

	/**
	 * Closes the default VisIt connection. This is a convenience method.
	 * 
	 * @param block
	 *            Whether or not to block the caller while the connection is
	 *            closed.
	 * @return True if the default connection is closed by the time this method
	 *         returns, false otherwise.
	 */
	private boolean closeDefaultConnection(boolean block) {
		return closeConnection(getDefaultConnectionId(), block);
	}

	// --------------------------------------- //
}
