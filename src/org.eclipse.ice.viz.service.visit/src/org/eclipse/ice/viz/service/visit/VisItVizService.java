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
package org.eclipse.ice.viz.service.visit;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.ice.viz.service.connections.ConnectionVizService;
import org.eclipse.ice.viz.service.connections.IVizConnectionManager;
import org.eclipse.ice.viz.service.visit.connections.VisItConnectionManager;

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
	public static final String CONNECTIONS_NODE_ID = "org.eclipse.ice.viz.visit.connections";

	/**
	 * The ID of the preferences page.
	 */
	public static final String PREFERENCE_PAGE_ID = "org.eclipse.ice.viz.service.visit.preferences";

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
	 * Implements an abstract method from ConnectionVizService.
	 */
	@Override
	protected IVizConnectionManager<VisItSwtConnection> createConnectionManager() {
		return new VisItConnectionManager();
	}

	/*
	 * Implements an abstract method from ConnectionVizService.
	 */
	@Override
	protected ConnectionPlot<VisItSwtConnection> createConnectionPlot() {
		return new VisItPlot();
	}

	/*
	 * Implements an abstract method from AbstractVizService.
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
		// Add supported Silo file format extensions.
		extensions.add("silo");
		return extensions;
	}

	/*
	 * Implements an abstract method from ConnectionVizService.
	 */
	@Override
	protected String getConnectionPreferencesNodeId() {
		return CONNECTIONS_NODE_ID;
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

}
