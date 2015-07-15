/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.viz.service.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.ice.viz.service.ISeries;

/**
 * The provider for the plot which will include plot attributes and a structure
 * for the series
 * 
 * @author Matthew Wang
 * @author Kasper Gammeltoft - Viz refactor to use ISeries rather than
 *         SeriesProvider
 * 
 */
public class PlotProvider {

	/**
	 * The plot's title
	 */
	private String plotTitle;

	/**
	 * The TreeMap structure that will hold the series for each time
	 */
	private TreeMap<Double, ArrayList<ISeries>> seriesMap;

	/**
	 * The independent series. All of the other series in the map should be
	 * plotted with respect to this series.
	 */
	private ISeries independentSeries;

	/**
	 * The plot's x axis title
	 */
	private String xAxisTitle;

	/**
	 * The plot's y axis title
	 */
	private String yAxisTitle;

	/**
	 * The plot's time units
	 */
	private String timeUnits;

	/**
	 * Contour/HeatMap flag
	 */
	private boolean contourFlag = false;

	/**
	 * Default constructor
	 */
	public PlotProvider() {
		this(null);
	}

	/**
	 * A comprehensive constructor that takes in a plot title
	 */
	public PlotProvider(String newPlotTitle) {
		plotTitle = newPlotTitle;
		seriesMap = new TreeMap<Double, ArrayList<ISeries>>();
		independentSeries = new CSVSeries();
		xAxisTitle = "X-Axis";
		yAxisTitle = "Y-Axis";
		timeUnits = null;
	}

	/**
	 * Sets the independent series to provide. Must not be null.
	 * 
	 * @param indptSeries
	 *            The {@link ISeries} to provide.
	 */
	public void setIndependentSeries(ISeries indptSeries) {
		if (indptSeries != null) {
			independentSeries = indptSeries;
		}
	}

	public ISeries getIndependentSeries() {
		return independentSeries;
	}

	/**
	 * Adds a new ISeries to the specified time
	 * 
	 * @param time
	 * @param newSeries
	 */
	public void addSeries(double time, ISeries newSeries) {
		// Only add non-null SeriesProviders.
		if (newSeries != null) {
			ArrayList<ISeries> seriesProviders = seriesMap.get(time);
			// Create an entry in the Map of SeriesProviders if the time is new.
			if (seriesProviders == null) {
				seriesProviders = new ArrayList<ISeries>();
				seriesMap.put(time, seriesProviders);
			}
			seriesProviders.add(newSeries);
		}
		return;
	}

	/**
	 * Removes an existing SeriesProvider from the specified time. Does nothing
	 * if the arguments are invalid.
	 * 
	 * @param time
	 *            The time at which the series should be removed.
	 * @param oldSeries
	 *            The series that should be removed.
	 */
	public void removeSeries(double time, ISeries oldSeries) {
		List<ISeries> seriesProviders = seriesMap.get(time);
		if (seriesProviders != null) {
			// Remove the old series. If it was removed and the list of
			// SeriesProviders is now empty, remove the time and its now-empty
			// list from the seriesMap.
			if (seriesProviders.remove(oldSeries)
					&& seriesProviders.isEmpty()) {
				seriesMap.remove(time);
			}
		}
		return;
	}

	/**
	 * Returns all the series at a specified time
	 * 
	 * @param time
	 * @return
	 */
	public ArrayList<ISeries> getSeriesAtTime(double time) {
		return seriesMap.get(time);
	}

	/**
	 * Accessor for the plot title
	 * 
	 * @return
	 */
	public String getPlotTitle() {
		return this.plotTitle;
	}

	/**
	 * Accessor for the x axis title
	 * 
	 * @return
	 */
	public String getXAxisTitle() {
		return this.xAxisTitle;
	}

	/**
	 * Accessor for the y axis title
	 * 
	 * @return
	 */
	public String getYAxisTitle() {
		return this.yAxisTitle;
	}

	/**
	 * Accessor for the time units
	 * 
	 * @return
	 */
	public String getTimeUnits() {
		return this.timeUnits;
	}

	/**
	 * Accessor for the times
	 * 
	 * @return
	 */
	public ArrayList<Double> getTimes() {
		return new ArrayList<Double>(seriesMap.keySet());
	}

	/**
	 * Method to check if this plotProvider is a contour
	 * 
	 * @return
	 */
	public boolean isContour() {
		return contourFlag;
	}

	/**
	 * Mutator for the plot title
	 * 
	 * @param plotTitle
	 */
	public void setPlotTitle(String plotTitle) {
		this.plotTitle = plotTitle;
	}

	/**
	 * 
	 * @param newXTitle
	 */
	public void setXAxisTitle(String newXTitle) {
		this.xAxisTitle = newXTitle;
	}

	/**
	 * 
	 * @param newYTitle
	 */
	public void setYAxisTitle(String newYTitle) {
		this.yAxisTitle = newYTitle;
	}

	/**
	 * 
	 * @param timeUnits
	 */
	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}

	/**
	 * 
	 */
	public void setPlotAsContour() {
		contourFlag = true;
	}
}
