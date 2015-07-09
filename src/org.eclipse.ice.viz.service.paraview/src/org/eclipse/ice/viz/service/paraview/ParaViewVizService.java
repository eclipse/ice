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
package org.eclipse.ice.viz.service.paraview;

import java.net.URI;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.AbstractVizService;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.connections.IVizConnection;
import org.eclipse.ice.viz.service.paraview.connections.ParaViewConnectionManager;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyBuilder;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactory;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.ice.viz.service.preferences.CustomScopedPreferenceStore;

/**
 * This class is responsible for providing a service to connect to (or launch)
 * either local or remote instances of ParaView. Additionally, it can be used to
 * create {@link ParaViewPlot}s that can render files supported by ParaView
 * within an SWT-based application.
 * 
 * @see ParaViewPlot
 * @see #createPlot(URI)
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewVizService extends AbstractVizService {

	/**
	 * The manager for all of the paraview connections.
	 */
	private final ParaViewConnectionManager manager;

	/**
	 * The factory of builders used to get {@link IParaViewProxy}s for
	 * manipulating and rendering files with ParaView.
	 */
	private IParaViewProxyFactory proxyFactory;

	/**
	 * The ID of the preferences node under which all connections will be added.
	 */
	public static final String CONNECTIONS_NODE_ID = "org.eclipse.ice.viz.paraview.connections";

	/**
	 * The default constructor.
	 * <p>
	 * <b>Note:</b> Only OSGi should call this method!
	 * </p>
	 */
	public ParaViewVizService() {

		// Set up the manager and hook it into the preferences.
		manager = new ParaViewConnectionManager();
		manager.setPreferenceStore((CustomScopedPreferenceStore) getPreferenceStore(), CONNECTIONS_NODE_ID);

		return;
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public String getName() {
		return "ParaView";
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public String getVersion() {
		return "";
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
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizService#createPlot(java
	 * .net.URI)
	 */
	@Override
	public IPlot createPlot(URI uri) throws Exception {
		// Check the URI. It should be non-null and have a valid extension.
		super.createPlot(uri);

		// Get the host from the URI.
		String host = uri.getHost();
		if (host == null) {
			host = "localhost";
		}

		// Get the next available connection for the URI's host.
		Set<String> availableConnections = manager.getConnectionsForHost(host);
		if (availableConnections.isEmpty()) {
			throw new Exception(
					"ParaViewVizService error: " + "No configured ParaView connection to host \"" + host + "\".");
		}
		String name = availableConnections.iterator().next();
		IVizConnection<IParaViewWebClient> connection;
		connection = manager.getConnection(name);

		// Create the plot with the connection and URI.
		ParaViewPlot plot = new ParaViewPlot(this);
		plot.setConnection(connection);
		plot.setDataSource(uri);

		return plot;
	}

	/**
	 * Sets the factory. This factory should be used to create an
	 * {@link IParaViewProxy} when performing operations on a supported file
	 * type.
	 * <p>
	 * <b>Note:</b> This method should only be called by OSGi!
	 * </p>
	 * 
	 * @param factory
	 *            The new factory.
	 */
	protected void setProxyFactory(IParaViewProxyFactory factory) {
		if (factory != null && factory != proxyFactory) {
			proxyFactory = factory;
			// Update the supported file types.
			supportedExtensions.clear();
			supportedExtensions.addAll(factory.getExtensions());
		}
		return;
	}

	/**
	 * Unsets the {@link IParaViewProxyBuilder} factory if the argument matches.
	 * <p>
	 * <b>Note:</b> This method should only be called by OSGi!
	 * </p>
	 * 
	 * @param factory
	 *            The old factory.
	 */
	protected void unsetProxyFactory(IParaViewProxyFactory factory) {
		if (factory == proxyFactory) {
			proxyFactory = null;
			// The file types are no longer supported.
			supportedExtensions.clear();
		}
		return;
	}

	/**
	 * Gets the factory of builders used to get {@link IParaViewProxy}s for
	 * manipulating and rendering files with ParaView.
	 * 
	 * @return The factory, or {@code null} if it was never set (via OSGi).
	 */
	protected IParaViewProxyFactory getProxyFactory() {
		return proxyFactory;
	}
}
