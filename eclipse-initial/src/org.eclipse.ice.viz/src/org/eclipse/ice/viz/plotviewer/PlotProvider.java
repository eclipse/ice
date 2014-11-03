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
package org.eclipse.ice.viz.plotviewer;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * The provider for the plot which will include plot attributes and a structure
 * for the series
 * 
 * @author w8o
 * 
 */
public class PlotProvider {

	/**
	 * The plot's title
	 */
	private String plotTitle;

	/**
	 * The treemap structure that will hold the series for each time
	 */
	private TreeMap<Double, ArrayList<SeriesProvider>> seriesMap;

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
		plotTitle = null;
		seriesMap = new TreeMap<Double, ArrayList<SeriesProvider>>();
		xAxisTitle = "X-Axis";
		yAxisTitle = "Y-Axis";
		timeUnits = null;
	}

	/**
	 * A comprehensive constructor that takes in a plot title
	 */
	public PlotProvider(String newPlotTitle) {
		plotTitle = newPlotTitle;
		seriesMap = new TreeMap<Double, ArrayList<SeriesProvider>>();
		xAxisTitle = "X-Axis";
		yAxisTitle = "Y-Axis";
		timeUnits = null;
	}

	/**
	 * Adds a new SeriesProvider to the specified time
	 * 
	 * @param time
	 * @param newSeries
	 */
	public void addSeries(Double time, SeriesProvider newSeries) {
		// Checks if a series is added to a new time and initializes an
		// arraylist
		if (!seriesMap.containsKey(time)) {
			seriesMap.put(time, new ArrayList<SeriesProvider>());
			seriesMap.get(time).add(newSeries);
		} else {
			// Adds to a pre-existing arraylist at the specified time
			seriesMap.get(time).add(newSeries);
		}
	}

	/**
	 * Returns all the series at a specified time
	 * 
	 * @param time
	 * @return
	 */
	public ArrayList<SeriesProvider> getSeriesAtTime(Double time) {
		if (seriesMap.containsKey(time)) {
			return seriesMap.get(time);
		} else {
			return null;
		}
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
