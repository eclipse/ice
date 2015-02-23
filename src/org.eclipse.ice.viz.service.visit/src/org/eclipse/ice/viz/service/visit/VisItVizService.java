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

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.viz.service.AbstractVizService;
import org.eclipse.ice.viz.service.connections.ConnectionState;
import org.eclipse.ice.viz.service.connections.visit.VisItConnectionAdapter;
import org.eclipse.ice.viz.service.connections.visit.VisItConnectionManager;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.ice.viz.service.preferences.TableComponentPreferenceAdapter;

import visit.java.client.FileInfo;
import visit.java.client.ViewerMethods;

/**
 * This is an implementation of the IVizService interface for the VisIt
 * visualization tool.
 * 
 * @author Jay Jay Billings
 * 
 */
public class VisItVizService extends AbstractVizService {

	// TODO Address multiple connections associated with various plots. For now,
	// we just have a single, default connection.

	/**
	 * The current instance of the viz service. This instance was created when
	 * the OSGi viz service was instantiated.
	 */
	private static VisItVizService instance;

	/**
	 * This manages the {@code VisItVizService} connection preferences and
	 * should be updated from the preference store when the preferences change.
	 */
	private final VisItConnectionManager connectionManager;

	/**
	 * This manages the default connection to VisIt and its settings. It handles
	 * the implementation for connecting and disconnecting from the underlyign
	 * VisIt connection.
	 */
	private VisItConnectionAdapter adapter;

	/**
	 * A list of existing plots. This is used to dispose of individual plots
	 * when they are no longer required by client code.
	 */
	private final List<VisItPlot> plots;

	/**
	 * The default constructor.
	 */
	public VisItVizService() {
		// Update the instance to point to this viz service (there should be
		// only one).
		instance = this;

		// Initialize the list of created plots.
		plots = new ArrayList<VisItPlot>();

		// Initialize the ConnectionManager based on the stored preferences.
		connectionManager = new VisItConnectionManager();
		TableComponentPreferenceAdapter tableAdapter;
		tableAdapter = new TableComponentPreferenceAdapter();
		tableAdapter.toTableComponent(
				(CustomScopedPreferenceStore) getPreferenceStore(),
				connectionManager);

		// Create an adapter based on the current properties. Do not connect
		// yet.
		updateAdapter(false);

		return;
	}

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
				adapter = new VisItConnectionAdapter();

				// Notify the plots that the connection has been configured.
				for (VisItPlot plot : plots) {
					plot.setConnection(adapter);
				}
			}
			// Set the adapter's properties. If a change occurred and the method
			// parameter says we need to attempt a connection, re-establish the
			// adapter's connection.
			List<Entry> row = connectionManager.getConnection(key);
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
			for (VisItPlot plot : plots) {
				plot.setConnection(adapter);
			}
		}

		return;
	}

	/**
	 * Gets the current instance of the viz service. This instance was created
	 * by OSGi.
	 * <p>
	 * <b>Note:</b> This method is only intended to be used by the preference
	 * page to notify the service when the preferences have changed.
	 * </p>
	 * 
	 * @return The current instance of the viz service.
	 */
	protected static VisItVizService getInstance() {
		return instance;
	}

	/**
	 * This method notifies the service that the preferences have changed. Any
	 * connections that have changed should be reset.
	 */
	protected void preferencesChanged(Map<String, String> changedKeys,
			Set<String> addedKeys, Set<String> removedKeys) {

		// Clear the old connection preferences.
		for (int i = 0; i < connectionManager.numberOfRows(); i++) {
			connectionManager.deleteRow(0);
		}

		// Update the connection preferences based on the stored preferences.
		TableComponentPreferenceAdapter tableAdapter;
		tableAdapter = new TableComponentPreferenceAdapter();
		tableAdapter.toTableComponent(
				(CustomScopedPreferenceStore) getPreferenceStore(),
				connectionManager);

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
	 * Gets the key ("connection ID") associated with the default connection.
	 * This can be set on the preference page.
	 * 
	 * @return The key for the default connection, or null if one is not set.
	 */
	private String getDefaultConnectionKey() {
		int id = getPreferenceStore().getInt("defaultConnection");
		List<String> connectionNames = connectionManager.getConnectionNames();
		return (id < connectionNames.size() ? connectionNames.get(id) : null);
	}

	/**
	 * Gets the default {@link #adapter}.
	 * 
	 * @return The default adapter for the VisIt connection.
	 */
	protected VisItConnectionAdapter getAdapter() {
		return adapter;
	}

	// ---- Implements IVizService ---- //
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
		// TODO
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * hasConnectionProperties()
	 */
	@Override
	public boolean hasConnectionProperties() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * getConnectionProperties()
	 */
	@Override
	public Map<String, String> getConnectionProperties() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * setConnectionProperties(java.util.Map)
	 */
	@Override
	public void setConnectionProperties(Map<String, String> props) {
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#connect()
	 */
	@Override
	public boolean connect() {
		return (adapter != null ? adapter.connect(false) : false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#disconnect()
	 */
	@Override
	public boolean disconnect() {
		return (adapter != null ? adapter.disconnect(false) : false);
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
		if (canOpenFile(file)) {
			plot = new VisItPlot(this, file);
			plots.add(plot);
			plot.setConnection(adapter);
		}
		return plot;
	}

	// -------------------------------- //

	/**
	 * Deletes the specified plot, if it was created by this service. After this
	 * call, the plot's UI contributions will be destroyed.
	 * 
	 * @param plot
	 *            The plot to delete. This should be a plot that was created by
	 *            {@link #createPlot(URI)}.
	 */
	public void deletePlot(IPlot plot) {
		// Find the plot in the list of created plots.
		int size = plots.size();
		VisItPlot visItPlot;
		for (int i = 0; i < size; i++) {
			visItPlot = plots.get(i);
			// If found, dispose the plot, remove it from the list, and break.
			if (plot == visItPlot) {
				visItPlot.dispose();
				plots.remove(i);
				i = size;
			}
		}
		return;
	}

	/**
	 * Determines whether or not the file can be opened in VisIt.
	 * 
	 * @param file
	 *            The file to test. May be {@code null}.
	 * @return True if the file can be opened in VisIt, false otherwise.
	 */
	private boolean canOpenFile(URI file) {
		boolean canOpen = false;

		// FIXME We should be able to support remote connections...

		// If the file is not null, the adapter is connected, and the adapter is
		// a local connection, we can try to plot the file.
		if (file != null && adapter.getState() == ConnectionState.Connected
				&& "localhost".equals(adapter.getConnectionProperty("url"))) {

			// Get the VisIt-friendly path for the URI.
			String path = VisItPlot.getSourcePath(file);
			if (path != null) {

				// Determine the VisIt FileInfo for the file.
				ViewerMethods methods = adapter.getConnection()
						.getViewerMethods();
				methods.openDatabase(path);
				FileInfo info = methods.getDatabaseInfo();

				// If the FileInfo is "empty", we can't plot the file.
				canOpen = !(info.getMeshes().isEmpty()
						&& info.getMaterials().isEmpty()
						&& info.getScalars().isEmpty() && info.getVectors()
						.isEmpty());

				// Close the file.
				methods.closeDatabase(path);
			}
		}

		return canOpen;
	}

}
