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

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.viz.service.IVizService;
import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provides a fake {@link ConnectionPlot}.
 * 
 * @author Jordan Deyton
 *
 */
public class FakeConnectionPlot extends ConnectionPlot<FakeClient> {

	/**
	 * The plot types to use for this fake plot. Defaults to an empty map.
	 */
	public final Map<String, String[]> plotTypes = new HashMap<String, String[]>();

	/**
	 * The default constructor.
	 * 
	 * @param vizService
	 *            The visualization service responsible for this plot.
	 */
	public FakeConnectionPlot(IVizService vizService) {
		super(vizService);
	}

	/*
	 * Implements an abstract method from ConnectionPlot.
	 */
	@Override
	protected ConnectionPlotRender<FakeClient> createConnectionPlotRender(
			Composite parent) {
		return new FakeConnectionPlotRender(parent, this);
	}

	/*
	 * Implements an abstract method from ConnectionPlot.
	 */
	@Override
	protected Map<String, String[]> findPlotTypes(URI file)
			throws IOException, Exception {
		return plotTypes;
	}

}
