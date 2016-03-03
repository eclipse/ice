/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Jordan Deyton
 *   Jordan Deyton - bug 469997
 *   Jordan Deyton - viz series refactor
 *******************************************************************************/
package org.eclipse.eavp.viz.service.visit;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.eavp.viz.service.connections.ConnectionPlot;
import org.eclipse.eavp.viz.service.connections.ConnectionVizService;
import org.eclipse.eavp.viz.service.connections.IVizConnectionManager;
import org.eclipse.eavp.viz.service.modeling.IControllerProviderFactory;
import org.eclipse.eavp.viz.service.visit.connections.VisItConnectionManager;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

import gov.lbnl.visit.swt.VisItSwtConnection;

/**
 * This is an implementation of the IVizService interface for the VisIt
 * visualization tool.
 * 
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItVizService extends ConnectionVizService<VisItSwtConnection> {

	/**
	 * The ID of the preferences node under which all connections will be added.
	 */
	public static final String CONNECTIONS_NODE_ID = "org.eclipse.eavp.viz.visit.connections";

	/**
	 * The ID of the preferences page.
	 */
	public static final String PREFERENCE_PAGE_ID = "org.eclipse.eavp.viz.service.visit.preferences";

	/**
	 * The default constructor.
	 * <p>
	 * <b>Note:</b> Only OSGi should call this method!
	 * </p>
	 */
	public VisItVizService() {
		// Nothing to do.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionVizService#
	 * createConnectionManager()
	 */
	@Override
	protected IVizConnectionManager<VisItSwtConnection> createConnectionManager() {
		return new VisItConnectionManager();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionVizService#
	 * createConnectionPlot()
	 */
	@Override
	protected ConnectionPlot<VisItSwtConnection> createConnectionPlot() {
		return new VisItPlot();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.AbstractVizService#findSupportedExtensions()
	 */
	@Override
	protected Set<String> findSupportedExtensions() {
		Set<String> extensions = new HashSet<String>();
		// Add supported ExodusII file format extensions.
		extensions.add("ex");
		extensions.add("e");
		extensions.add("exo");
		extensions.add("ex2");
		extensions.add("exii");
		extensions.add("gen");
		extensions.add("exodus");
		extensions.add("nemesis");
		extensions.add("visit");
		// Add supported Silo file format extensions.
		extensions.add("silo");
		return extensions;
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
		return "VisIt";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizService#getVersion()
	 */
	@Override
	public String getVersion() {
		return "1.0";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizService#getFactory()
	 */
	@Override
	public IControllerProviderFactory getFactory() {
		// The VisIt visualization service does not make use of the model
		// framework, so it has no factory
		return null;
	}
}
