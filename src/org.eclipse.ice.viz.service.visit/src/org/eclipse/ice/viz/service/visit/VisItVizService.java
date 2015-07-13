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
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.AbstractVizService;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.connections.IVizConnection;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;
import org.eclipse.ice.viz.service.visit.connections.VisItConnectionManager;

import gov.lbnl.visit.swt.VisItSwtConnection;

/**
 * This is an implementation of the IVizService interface for the VisIt
 * visualization tool.
 * 
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItVizService extends AbstractVizService {

	/**
	 * The manager for all of the VisIt connections. It syncs with the Eclipse
	 * preferences automatically.
	 */
	private final VisItConnectionManager manager;

	/**
	 * The ID of the preferences node under which all connections will be added.
	 */
	public static final String CONNECTIONS_NODE_ID = "org.eclipse.ice.viz.visit.connections";

	/**
	 * The default constructor.
	 * <p>
	 * <b>Note:</b> Only OSGi should call this method!
	 * </p>
	 */
	public VisItVizService() {

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

		// Set up the manager and hook it into the preferences.
		manager = new VisItConnectionManager();
		manager.setPreferenceStore((CustomScopedPreferenceStore) getPreferenceStore(), CONNECTIONS_NODE_ID);

		return;
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public String getName() {
		return "VisIt";
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public String getVersion() {
		return "1.0";
	}

	// TODO REMOVE THESE LINES ----------------------------------
	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public boolean hasConnectionProperties() {
		// Do nothing yet.
		return false;
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public Map<String, String> getConnectionProperties() {
		// Do nothing yet.
		return null;
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public void setConnectionProperties(Map<String, String> props) {
		// Do nothing yet.
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public boolean connect() {
		return false;
		// return connections.connect();
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public boolean disconnect() {
		return false;
	}
	// ----------------------------------------------------------

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public IPlot createPlot(URI uri) throws Exception {
		// Call the super method to validate the URI's extension.
		super.createPlot(uri);

		// Get a connection for the file.
		IVizConnection<VisItSwtConnection> connection = getConnection(uri);

		// Create the plot with the connection and URI.
		VisItPlot plot = new VisItPlot(this);
		plot.setConnection(connection);
		plot.setDataSource(uri);

		return plot;
	}

	// TODO Move the below method to a super class so it can be re-used between
	// ParaView and VisIt.
	/**
	 * Gets a valid connection for the file the associated connection manager
	 * 
	 * @param uri
	 *            The URI of the file that needs a connection to open.
	 * @return A valid connection. This should never be {@code null}. If a
	 *         connection could not be found, an exception should be thrown.
	 * @throws Exception
	 *             If there is not connection available for the URI.
	 */
	private IVizConnection<VisItSwtConnection> getConnection(URI uri) throws Exception {

		// Get the host from the URI.
		String host = uri.getHost();
		if (host == null) {
			host = "localhost";
		}

		// Get the next available connection for the URI's host.
		Set<String> availableConnections = manager.getConnectionsForHost(host);
		if (availableConnections.isEmpty()) {
			throw new Exception("VisItVizService error: " + "No configured VisIt connection to host \"" + host + "\".");
		}
		String name = availableConnections.iterator().next();
		// Return the next available connection.
		return manager.getConnection(name);
	}

}
