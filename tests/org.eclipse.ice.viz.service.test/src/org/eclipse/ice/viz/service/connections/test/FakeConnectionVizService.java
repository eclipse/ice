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
package org.eclipse.ice.viz.service.connections.test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.ice.viz.service.connections.ConnectionVizService;
import org.eclipse.ice.viz.service.connections.IVizConnectionManager;
import org.eclipse.ice.viz.service.connections.VizConnection;
import org.eclipse.ice.viz.service.connections.VizConnectionManager;

/**
 * This class provides a fake concrete class for testing
 * {@link ConnectionVizService}.
 * 
 * @author Jordan Deyton
 *
 */
public class FakeConnectionVizService extends ConnectionVizService<FakeClient> {

	/**
	 * The name of the service. Defaults to "Fake Connection Viz Service".
	 */
	public String name = "Fake Connection Viz Service";
	/**
	 * The version of the service. Defaults to "pi".
	 */
	public String version = "pi";
	/**
	 * The set of supported extensions. Empty by default.
	 */
	public final Set<String> supportedExtensions = new HashSet<String>();

	/**
	 * Whether or not {@link #createConnectionPlot()} was called.
	 */
	public final AtomicBoolean plotCreated = new AtomicBoolean();

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * Implements a method from IVizService.
	 */
	@Override
	public String getVersion() {
		return version;
	}

	/*
	 * Implements an abstract method from ConnectionVizService.
	 */
	@Override
	protected IVizConnectionManager<FakeClient> createConnectionManager() {
		return new VizConnectionManager<FakeClient>() {
			@Override
			protected VizConnection<FakeClient> createConnection(String name,
					String preferences) {
				return new FakeVizConnection();
			}
		};
	}

	/*
	 * Implements an abstract method from ConnectionVizService.
	 */
	@Override
	protected ConnectionPlot<FakeClient> createConnectionPlot() {
		return new FakeConnectionPlot(this);
	}

	/*
	 * Implements an abstract method from ConnectionVizService.
	 */
	@Override
	protected String getConnectionPreferencesNodeId() {
		return "org.eclipse.ice.viz.service.connections.test.connections";
	}

	/*
	 * Implements an abstract method from AbstractVizService.
	 */
	@Override
	protected Set<String> findSupportedExtensions() {
		return supportedExtensions;
	}

}
