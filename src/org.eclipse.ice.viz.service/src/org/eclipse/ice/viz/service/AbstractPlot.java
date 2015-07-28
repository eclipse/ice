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
package org.eclipse.ice.viz.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;

/**
 * This class provides a basic, limited implementation of certain features of
 * {@link IPlot}. This class does not manage the data source, categories, or
 * dependent series. It also does not handle drawing the plot.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractPlot implements IPlot {

	/**
	 * The current independent series for the plot.
	 */
	private ISeries independentSeries = null;

	/**
	 * The title for the plot.
	 */
	private String title = null;

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {
		throw new UnsupportedOperationException(
				"IPlot error: " + "This plot cannot be drawn.");
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public int getNumberOfAxes() {
		return 0;
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public Map<String, String> getProperties() {
		return new HashMap<String, String>();
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// Nothing to do.
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public URI getDataSource() {
		return null;
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public String getSourceHost() {
		String host = null;
		URI source = getDataSource();
		if (source != null) {
			host = source.getHost();
			if (host == null) {
				host = "localhost";
			}
		}
		return host;
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public boolean isSourceRemote() {
		String host = getSourceHost();
		return host != null ? !"localhost".equals(host) : false;
	}

	/*
	 * Implements a method from IVizCanvas.
	 */
	@Override
	public void redraw() {
		// Nothing to do.
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public void setPlotTitle(String title) {
		this.title = title;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public String getPlotTitle() {
		return title;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public void setIndependentSeries(ISeries series) {
		independentSeries = series;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public ISeries getIndependentSeries() {
		return independentSeries;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public List<ISeries> getAllDependentSeries(String category) {
		return null;
	}

	/*
	 * Implements a method from IPlot.
	 */
	@Override
	public List<String> getCategories() {
		return new ArrayList<String>();
	}

}
