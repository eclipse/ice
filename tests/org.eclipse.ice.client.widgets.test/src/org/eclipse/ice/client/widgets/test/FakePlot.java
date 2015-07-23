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
package org.eclipse.ice.client.widgets.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.client.widgets.ICEResourcePage;
import org.eclipse.ice.client.widgets.PlotGridComposite;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.ice.viz.service.csv.CSVSeries;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * A simple {@link IPlot} implementation for testing things that draw
 * {@code IPlot}s, including the {@link PlotGridComposite} and the
 * {@link ICEResourcePage}.
 * 
 * @author Jordan Deyton
 * @author Kasper Gammeltoft - Refactored to work with the new IPlot
 *         implementation using <code> ISeries</code>.
 *
 */
public class FakePlot implements IPlot {

	/**
	 * The map of dependent series. This will not be populated with anything by
	 * default.
	 */
	public final Map<String, List<ISeries>> depSeries = new HashMap<String, List<ISeries>>();

	/**
	 * The independent series for this plot. This is a plain new series by
	 * default.
	 */
	public ISeries indepSeries = new CSVSeries();

	/**
	 * A list of all child composites created when
	 * {@link #draw(String, String, Composite)} is called.
	 */
	public final List<Composite> children = new ArrayList<Composite>();

	/**
	 * The name or label for this plot
	 */
	public String name = "";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.IPlot#draw(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {
		Composite child = new Composite(parent, SWT.NONE);
		children.add(child);
		child.setMenu(parent.getMenu());
		return child;
	}

	/**
	 * Gets the number of times that {@link #draw(String, String, Composite)}
	 * was called.
	 */
	public int getDrawCount() {
		return children.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.VizCanvas#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.VizCanvas#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.VizCanvas#setProperties(java.util.Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.VizCanvas#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return false;
	}

	@Override
	public void redraw() {
		// Nothing TODO- fake plot
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#setPlotTitle(java.lang.String)
	 */
	@Override
	public void setPlotTitle(String title) {
		name = title;
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#getPlotTitle()
	 */
	@Override
	public String getPlotTitle() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.IPlot#setIndependentSeries(org.eclipse.ice.
	 * viz.service.ISeries)
	 */
	@Override
	public void setIndependentSeries(ISeries series) {
		indepSeries = series;
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#getIndependentSeries()
	 */
	@Override
	public ISeries getIndependentSeries() {
		return indepSeries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.IPlot#removeDependantSeries(org.eclipse.ice.
	 * viz.service.ISeries)
	 */
	@Override
	public void removeDependantSeries(ISeries series) {
		depSeries.remove(series);
		// If this series is in the list
		if (depSeries.containsValue(series)) {
			// Iterate to find the right key
			for (String key : depSeries.keySet()) {
				// Remove the series for the first category it is in in the map
				if (depSeries.get(key).contains(series)) {
					depSeries.get(key).remove(series);
					break;
				}
			}
		}
	}

	@Override
	public void addDependentSeries(String catagory, ISeries seriesToAdd) {
		if (depSeries.get(catagory) == null) {
			depSeries.put(catagory, new ArrayList<ISeries>());
		}
		depSeries.get(catagory).add(seriesToAdd);

	}

	@Override
	public List<ISeries> getAllDependentSeries(String category) {
		return depSeries.get(category);
	}

	@Override
	public String[] getCategories() {
		// TODO Auto-generated method stub
		return depSeries.keySet()
				.toArray(new String[depSeries.keySet().size()]);
	}
}
