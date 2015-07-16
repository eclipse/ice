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

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.swt.widgets.Composite;

/**
 * This class is responsible for embedding ParaView-supported graphics inside
 * client {@link Composite}s.
 * <p>
 * Instances of this class should not be created manually. Instead, a plot
 * should be created via {@link ParaViewVizService#createPlot(URI)}.
 * </p>
 *
 * @see ParaViewVizService
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewPlot extends ConnectionPlot<IParaViewWebClient> {

	/**
	 * A reference to the viz service conveniently cast to its actual type.
	 */
	private final ParaViewVizService vizService;

	/**
	 * The proxy associated with the current URI. It handles messages concerning
	 * the file going to and from the remote ParaView server.
	 * <p>
	 * This should be re-created whenever the data source URI is set. If it
	 * cannot be created, then the URI cannot be rendered via ParaView.
	 * </p>
	 */
	private IParaViewProxy proxy;

	/**
	 * The default constructor.
	 * 
	 * @param service
	 *            The visualization service responsible for this plot.
	 */
	public ParaViewPlot(ParaViewVizService vizService) {
		super(vizService);

		this.vizService = vizService;
	}

	/*
	 * Overrides a method from ConnectionPlot.
	 */
	@Override
	public void setDataSource(URI uri) throws NullPointerException, IOException, IllegalArgumentException, Exception {

		// Attempt to create the IParaViewProxy. This will throw an exception if
		// the URI is null or its extension is invalid.
		IParaViewProxy proxy = vizService.getProxyFactory().createProxy(uri);
		// Attempt to open the file. Wait until the process completes.
		if (proxy.open(getConnection()).get()) {
			this.proxy = proxy;
		} else {
			throw new IllegalArgumentException("ParaViewPlot error: " + "Cannot open the file \"" + uri.getPath()
					+ "\" using the existing connection.");
		}

		super.setDataSource(uri);
	}

	/*
	 * Implements an abstract method from ConnectionPlot.
	 */
	@Override
	protected ConnectionPlotRender<IParaViewWebClient> createConnectionPlotRender(Composite parent) {
		return new ParaViewPlotRender(parent, this);
	}

	/*
	 * Implements an abstract method from MultiPlot.
	 */
	@Override
	protected Map<String, String[]> findPlotTypes(URI uri) throws IOException, Exception {
		// Throw an exception in case the proxy was not created.
		if (proxy == null) {
			throw new IllegalStateException(
					"ParaViewPlot error: " + "A proxy was not created before finding the plot types.");
		}

		// Set up the default return value.
		Map<String, String[]> plotTypes = new HashMap<String, String[]>();

		// Add all categories and features to the map.
		for (String category : proxy.getFeatureCategories()) {
			Set<String> features = proxy.getFeatures(category);
			String[] types = new String[features.size()];
			plotTypes.put(category, features.toArray(types));
		}

		return plotTypes;
	}

	/**
	 * Gets the proxy associated with the current URI. It handles messages
	 * concerning the file going to and from the remote ParaView server.
	 * <p>
	 * This is re-created whenever the data source URI is changed. If it cannot
	 * be created, then the URI cannot be rendered via ParaView.
	 * </p>
	 * 
	 * @return The current proxy, or {@code null} if the current URI/connection
	 *         cannot be used to create a proxy.
	 */
	protected IParaViewProxy getParaViewProxy() {
		return proxy;
	}
}
