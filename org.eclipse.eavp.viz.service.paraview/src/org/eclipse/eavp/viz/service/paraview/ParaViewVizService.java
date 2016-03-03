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
package org.eclipse.eavp.viz.service.paraview;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.eavp.viz.service.connections.ConnectionPlot;
import org.eclipse.eavp.viz.service.connections.ConnectionVizService;
import org.eclipse.eavp.viz.service.connections.IVizConnectionManager;
import org.eclipse.eavp.viz.service.connections.VizConnection;
import org.eclipse.eavp.viz.service.connections.VizConnectionManager;
import org.eclipse.eavp.viz.service.modeling.IControllerProviderFactory;
import org.eclipse.eavp.viz.service.paraview.connections.ParaViewConnection;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyBuilder;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxyFactory;
import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

/**
 * This class is responsible for providing a service to connect to (or launch)
 * either local or remote instances of ParaView. Additionally, it can be used to
 * create {@link ParaViewPlot}s that can render files supported by ParaView
 * within an SWT-based application.
 * 
 * @see ParaViewPlot
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewVizService
		extends ConnectionVizService<IParaViewWebClient> {

	/**
	 * The ID of the preferences node under which all connections will be added.
	 */
	public static final String CONNECTIONS_NODE_ID = "org.eclipse.eavp.viz.paraview.connections";

	/**
	 * The ID of the preferences page.
	 */
	public static final String PREFERENCE_PAGE_ID = "org.eclipse.eavp.viz.service.paraview.preferences";

	/**
	 * The factory of builders used to get {@link IParaViewProxy}s for
	 * manipulating and rendering files with ParaView.
	 */
	private IParaViewProxyFactory proxyFactory;

	/**
	 * The default constructor.
	 * <p>
	 * <b>Note:</b> Only OSGi should call this method!
	 * </p>
	 */
	public ParaViewVizService() {
		// Nothing to do.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionVizService#
	 * createConnectionManager()
	 */
	@Override
	protected IVizConnectionManager<IParaViewWebClient> createConnectionManager() {
		// Return a new connection manager that can be used to create a
		// ParaViewConnection.
		return new VizConnectionManager<IParaViewWebClient>() {
			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.eavp.viz.service.connections.VizConnectionManager#
			 * createConnection(java.lang.String, java.lang.String)
			 */
			@Override
			protected VizConnection<IParaViewWebClient> createConnection(
					String name, String preferences) {
				return new ParaViewConnection();
			}
		};
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionVizService#
	 * createConnectionPlot()
	 */
	@Override
	protected ConnectionPlot<IParaViewWebClient> createConnectionPlot() {
		return new ParaViewPlot(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.AbstractVizService#findSupportedExtensions()
	 */
	@Override
	protected Set<String> findSupportedExtensions() {
		return proxyFactory != null ? proxyFactory.getExtensions()
				: new HashSet<String>(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionVizService#
	 * getConnectionPreferencesNodeId()
	 */
	@Override
	protected String getConnectionPreferencesNodeId() {
		return CONNECTIONS_NODE_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizService#getName()
	 */
	@Override
	public String getName() {
		return "ParaView";
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizService#getVersion()
	 */
	@Override
	public String getVersion() {
		return "";
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
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizService#getFactory()
	 */
	@Override
	public IControllerProviderFactory getFactory() {
		// The ParaView visualization service does not make use of the model
		// framework, so it has no factory
		return null;
	}
}