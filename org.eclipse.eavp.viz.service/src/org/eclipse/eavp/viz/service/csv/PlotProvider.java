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
package org.eclipse.eavp.viz.service.csv;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.ProxySeries;
import org.eclipse.eavp.viz.service.styles.XYZAxisStyle;
import org.eclipse.eavp.viz.service.styles.XYZPlotStyle;

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
	 * The style for the plot as a whole.
	 */
	private XYZPlotStyle plotStyle;

	/**
	 * The TreeMap structure that will hold the series for each time
	 */
	private TreeMap<Double, ArrayList<ISeries>> seriesMap;

	/**
	 * The independent series. All of the other series in the map should be
	 * plotted with respect to this series.
	 */
	private ProxySeries independentSeries;

	/**
	 * The axis style for the x axis, providing the description necessary to
	 * properly format that axis.
	 */
	private XYZAxisStyle xAxisStyle;

	/**
	 * The axis style for the y axis, providing the description necessary to
	 * properly format that axis.
	 */
	private XYZAxisStyle yAxisStyle;

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
		independentSeries = new ProxySeries();
		plotStyle = new XYZPlotStyle();
		xAxisStyle = new XYZAxisStyle();
		yAxisStyle = new XYZAxisStyle();
		timeUnits = null;
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

	public ISeries getIndependentSeries() {
		return independentSeries;
	}

	/**
	 * Accessor for the plot style
	 * 
	 * @return
	 */
	public XYZPlotStyle getPlotStyle() {
		return plotStyle;
	}

	/**
	 * Accessor for the plot title
	 * 
	 * @return
	 */
	public String getPlotTitle() {
		return plotTitle;
	}

	/**
	 * Returns all the series at a specified time
	 * 
	 * @param time
	 * @return
	 */
	public ArrayList<ISeries> getSeriesAtTime(double time) {
		ArrayList<ISeries> seriesList = seriesMap.get(time);
		return seriesList != null ? seriesList : new ArrayList<ISeries>(0);
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
	 * Accessor for the plot's time units
	 * 
	 * @return
	 */
	public String getTimeUnits() {
		return timeUnits;
	}

	/**
	 * Accessor for the plot's x axis style
	 * 
	 * @return
	 */
	public XYZAxisStyle getXAxisStyle() {
		return xAxisStyle;
	}

	/**
	 * Accessor for the plot's y axis style
	 * 
	 * @return
	 */
	public XYZAxisStyle getYAxisStyle() {
		return yAxisStyle;
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
	 * Sets the independent series to provide. Must not be null.
	 * 
	 * @param indptSeries
	 *            The {@link ISeries} to provide.
	 */
	public void setIndependentSeries(ISeries indptSeries) {
		if (indptSeries != null) {
			independentSeries = (ProxySeries) indptSeries;
		}
	}

	/**
	 * Sets the plot to be drawn as a contour map rather than a series plot
	 */
	public void setPlotAsContour() {
		contourFlag = true;
	}

	/**
	 * Sets the {@link XYZPlotStyle} for the plot, which is read in by the
	 * editor and applied appropriately.
	 * 
	 * @param newStyle
	 */
	public void setPlotStyle(XYZPlotStyle newStyle) {
		plotStyle = newStyle;
	}

	/**
	 * Mutator for the plot title
	 * 
	 * @param plotTitle
	 */
	public void setPlotTitle(String newTitle) {
		plotTitle = newTitle;
	}

	/**
	 * 
	 * @param timeUnits
	 */
	public void setTimeUnits(String newUnit) {
		timeUnits = newUnit;
	}

	/**
	 * Sets the x axis style for the plot.
	 * 
	 * @param newStyle
	 */
	public void setXAxisStyle(XYZAxisStyle newStyle) {
		xAxisStyle = newStyle;
	}

	/**
	 * Sets the y axis style for the plot
	 * 
	 * @param newStyle
	 */
	public void setYAxisStyle(XYZAxisStyle newStyle) {
		yAxisStyle = newStyle;
	}
}
