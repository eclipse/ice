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
package org.eclipse.eavp.viz.service.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.csv.CSVSeries;
import org.eclipse.eavp.viz.service.widgets.PlotGridComposite;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;

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
	 * A list of all child composites created when
	 * {@link #draw(String, String, Composite)} is called.
	 */
	public Composite child;

	/**
	 * The map of dependent series. This will not be populated with anything by
	 * default.
	 */
	public final Map<String, List<ISeries>> depSeries = new HashMap<String, List<ISeries>>();

	private int drawCount;
	
	/**
	 * The independent series for this plot. This is a plain new series by
	 * default.
	 */
	public ISeries indepSeries = new CSVSeries();

	/**
	 * The name or label for this plot
	 */
	public String name = "";

	public FakePlot() {
		drawCount = 0;
	}
	
	/**
	 * Adds the specified series to this plot under the given category
	 * 
	 * @param catagory
	 *            The category that this series falls under
	 * @param seriesToAdd
	 *            The series to add
	 */
	public void addDependentSeries(String catagory, ISeries seriesToAdd) {
		if (depSeries.get(catagory) == null) {
			depSeries.put(catagory, new ArrayList<ISeries>());
		}
		depSeries.get(catagory).add(seriesToAdd);

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizCanvas#draw(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {
		System.err.println("Drawing fake plot... " + drawCount);
		drawCount++;
		child = new Composite(parent, SWT.NONE);
		child.setMenu(parent.getMenu());
		return child;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#getCategories()
	 */
	@Override
	public List<String> getCategories() {
		return new ArrayList<String>(depSeries.keySet());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizCanvas#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#getDependentSeries(java.lang.String)
	 */
	@Override
	public List<ISeries> getDependentSeries(String category) {
		return depSeries.get(category);
	}

	/**
	 * Gets the number of times that {@link #draw(Composite)} was called.
	 */
	public int getDrawCount() {
		return drawCount;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#getIndependentSeries()
	 */
	@Override
	public ISeries getIndependentSeries() {
		return indepSeries;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizCanvas#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#getPlotTitle()
	 */
	@Override
	public String getPlotTitle() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizCanvas#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizCanvas#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizCanvas#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizCanvas#redraw()
	 */
	@Override
	public void redraw() {
		// Nothing TODO- fake plot
		return;
	}

	/**
	 * Removes the specified series from this plot.
	 * 
	 * @param series
	 *            The series to remove
	 */
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

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#setIndependentSeries(org.eclipse.eavp.viz.service.ISeries)
	 */
	@Override
	public void setIndependentSeries(ISeries series) {
		indepSeries = series;
		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#setPlotTitle(java.lang.String)
	 */
	@Override
	public void setPlotTitle(String title) {
		name = title;
		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IVizCanvas#setProperties(java.util.Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#createAdditionalPage(org.eclipse.ui.part.MultiPageEditorPart, org.eclipse.ui.IFileEditorInput, int)
	 */
	@Override
	public String createAdditionalPage(MultiPageEditorPart parent, IFileEditorInput file, int pageNum) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlot#getNumAdditionalPages()
	 */
	@Override
	public int getNumAdditionalPages() {
		return 0;
	}

	@Override
	public void save(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveAs() {
		// TODO Auto-generated method stub
		
	}

}
