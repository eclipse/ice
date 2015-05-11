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

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.viz.service.AbstractVizService;
import org.eclipse.ice.viz.service.connections.ConnectionManager;
import org.eclipse.ice.viz.service.connections.ConnectionTable;
import org.eclipse.ice.viz.service.connections.IConnectionAdapter;
import org.eclipse.ice.viz.service.connections.visit.VisItConnectionAdapter;
import org.eclipse.ice.viz.service.connections.visit.VisItConnectionTable;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;

/**
 * This is an implementation of the IVizService interface for the VisIt
 * visualization tool.
 * 
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItVizService extends AbstractVizService {

	/**
	 * The current instance of the viz service. This instance was created when
	 * the OSGi viz service was instantiated.
	 */
	private static VisItVizService instance;

	/**
	 * The manager for all of the VisIt connections.
	 */
	private final ConnectionManager<VisItSwtConnection> connections;

	/**
	 * The default constructor.
	 * <p>
	 * <b>Note:</b> Only OSGi should call this method!
	 * </p>
	 */
	public VisItVizService() {
		// Update the instance to point to this viz service (there should be
		// only one).
		instance = this;

		// Set up the connection manager. The connection preference table is a
		// little more complicated for VisIt, so we need to use the
		// VisItConnectionTable rather than the default.
		connections = new ConnectionManager<VisItSwtConnection>() {
			@Override
			protected CustomScopedPreferenceStore getPreferenceStore() {
				return (CustomScopedPreferenceStore) VisItVizService.this
						.getPreferenceStore();
			}

			@Override
			protected ConnectionTable createConnectionTable() {
				return new VisItConnectionTable();
			}

			@Override
			protected IConnectionAdapter<VisItSwtConnection> createConnectionAdapter() {
				return new VisItConnectionAdapter();
			}
		};

		// Add supported ExodusII file format extensions.
		supportedExtensions.add("ex");
		supportedExtensions.add("e");
		supportedExtensions.add("exo");
		supportedExtensions.add("ex2");
		supportedExtensions.add("exii");
		supportedExtensions.add("gen");
		supportedExtensions.add("exodus");
		supportedExtensions.add("nemesis");
		// Add supported Silo file format extensions.
		supportedExtensions.add("silo");

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
		connections.preferencesChanged(changedKeys, addedKeys, removedKeys);
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
		return "1.0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 * hasConnectionProperties()
	 */
	@Override
	public boolean hasConnectionProperties() {
		// Do nothing yet.
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
		// Do nothing yet.
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
		// Do nothing yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#connect()
	 */
	@Override
	public boolean connect() {
		return connections.connect();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#disconnect()
	 */
	@Override
	public boolean disconnect() {
		return connections.disconnect();
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
		// Call the super method to validate the URI's extension.
		super.createPlot(file);
		
		VisItPlot plot = null;

		// Create the plot.
		plot = new VisItPlot(this);
		// Associate the plot with the connection.
		connections.addClient(plot);
		// Set teh data source for the file.
		plot.setDataSource(file);

		return plot;
	}

}
