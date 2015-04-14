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
package org.eclipse.ice.viz.plotviewer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.eclipse.ice.viz.plotviewer.PlotProvider;
import org.eclipse.ice.viz.plotviewer.SeriesProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is responsible for testing the PlotProvider
 * 
 * @author Claire Saunders
 * 
 */
public class PlotProviderTester {

	/**
	 * Class variables to be used by the tests.
	 */
	private String plotTitle;
	private TreeMap<Double, ArrayList<SeriesProvider>> seriesMap;
	private String xAxisTitle;
	private String yAxisTitle;
	private String timeUnits;
	private Double time;
	private ArrayList<Double> times;
	private ArrayList<SeriesProvider> seriesProviderList;
	private PlotProvider plotProvider;
	private SeriesProvider seriesProvider;

	/**
	 * Intialize variables to be used throughout.
	 */
	@Before
	public void beforeClass() {

		plotTitle = "Title of Plot";
		xAxisTitle = "X-Axis Title";
		yAxisTitle = "Y-Axis Title";
		timeUnits = "Seconds";
		seriesMap = new TreeMap<Double, ArrayList<SeriesProvider>>();
		plotProvider = new PlotProvider();
		seriesProvider = new SeriesProvider();
		times = new ArrayList<Double>();
		seriesProviderList = new ArrayList<SeriesProvider>();
		time = 4.0;

		// Add double values to the arrayList Times
		times.add(1.0);
		times.add(2.0);
		times.add(3.0);

		// Make a seriesproviderList of size 1 for testing purposes
		seriesProviderList.add(seriesProvider);

	}

	/**
	 * Check adding to the map of series data.
	 */
	@Test
	public void checkAddSeries() {

		// Compare two maps
		seriesMap.put(time, new ArrayList<SeriesProvider>());
		TreeMap<Double, ArrayList<SeriesProvider>> newSeriesMap = new TreeMap<Double, ArrayList<SeriesProvider>>();
		newSeriesMap.put(time, new ArrayList<SeriesProvider>());
		assertEquals(seriesMap, newSeriesMap);
		assertEquals(seriesMap.size(), newSeriesMap.size());

		// Adds to pre-existing ArrayLists at the specified time
		seriesMap.get(time).add(seriesProvider);
		newSeriesMap.get(time).add(seriesProvider);
		assertEquals(seriesMap, newSeriesMap);
		assertEquals(seriesMap.size(), newSeriesMap.size());

		return;
	}

	/**
	 * Checks that SeriesProviders can be correctly removed from the
	 * PlotProvider.
	 */
	@Test
	public void checkRemoveSeries() {

		// A PlotProvider whose remove functionality will be tested. No other
		// tests should interfere, hence we do not use the class variable.
		PlotProvider plotProvider = new PlotProvider();

		// Series and times to add and later *remove*. Each series is added for
		// each time, resulting in 4 total additions.
		SeriesProvider series1 = new SeriesProvider();
		SeriesProvider series2 = new SeriesProvider();
		double time1 = 42.0;
		double time2 = 343.1337;

		// Used as the return value from getSeriesAtTime(). This is necessary to
		// verify that series were in fact removed from the PlotProvider.
		List<SeriesProvider> seriesProviders;

		// Add each series to each time.
		plotProvider.addSeries(time1, series1);
		plotProvider.addSeries(time2, series1);
		plotProvider.addSeries(time1, series2);
		plotProvider.addSeries(time2, series2);

		// Verify that all series are associated with each time.
		seriesProviders = plotProvider.getSeriesAtTime(time1);
		assertEquals(2, seriesProviders.size());
		assertTrue(seriesProviders.contains(series1));
		assertTrue(seriesProviders.contains(series2));
		seriesProviders = plotProvider.getSeriesAtTime(time2);
		assertEquals(2, seriesProviders.size());
		assertTrue(seriesProviders.contains(series1));
		assertTrue(seriesProviders.contains(series2));

		// Try some invalid remove commands. Nothing should change.
		plotProvider.removeSeries(-1.0, series1);
		plotProvider.removeSeries(time1, null);

		// Verify that nothing changed.
		seriesProviders = plotProvider.getSeriesAtTime(time1);
		assertEquals(2, seriesProviders.size());
		assertTrue(seriesProviders.contains(series1));
		assertTrue(seriesProviders.contains(series2));
		seriesProviders = plotProvider.getSeriesAtTime(time2);
		assertEquals(2, seriesProviders.size());
		assertTrue(seriesProviders.contains(series1));
		assertTrue(seriesProviders.contains(series2));

		// Now remove series 1 from time 1, and remove series 2 from time 2.
		plotProvider.removeSeries(time1, series1);
		plotProvider.removeSeries(time2, series2);

		// Series 2 should be the only series for time 1. Likewise, series 1
		// should be the only series for time 2.
		seriesProviders = plotProvider.getSeriesAtTime(time1);
		assertEquals(1, seriesProviders.size());
		assertFalse(seriesProviders.contains(series1));
		assertTrue(seriesProviders.contains(series2));
		seriesProviders = plotProvider.getSeriesAtTime(time2);
		assertEquals(1, seriesProviders.size());
		assertTrue(seriesProviders.contains(series1));
		assertFalse(seriesProviders.contains(series2));

		// Try the same remove commands. Nothing should change.
		plotProvider.removeSeries(time1, series1);
		plotProvider.removeSeries(time2, series2);

		// Verify that nothing changed.
		seriesProviders = plotProvider.getSeriesAtTime(time1);
		assertEquals(1, seriesProviders.size());
		assertFalse(seriesProviders.contains(series1));
		assertTrue(seriesProviders.contains(series2));
		seriesProviders = plotProvider.getSeriesAtTime(time2);
		assertEquals(1, seriesProviders.size());
		assertTrue(seriesProviders.contains(series1));
		assertFalse(seriesProviders.contains(series2));

		return;
	}

	/**
	 * Check retrieval from the series map.
	 */
	@Test
	public void checkGetSeriesAtTime() {

		// Check the map contents
		seriesMap.put(time, seriesProviderList);
		assertEquals(seriesProviderList, seriesMap.get(time));

		// check the null case if we try to access a key that doesn't exist
		TreeMap<Double, ArrayList<SeriesProvider>> newSeriesMap = new TreeMap<Double, ArrayList<SeriesProvider>>();
		newSeriesMap.put(time, seriesProviderList);
		assertNull(newSeriesMap.get(7.0));

	}

	/**
	 * Check the accessor and mutator for the plot title.
	 */
	@Test
	public void checkSetAndGetPlotTitle() {

		// check if the plotTitle matches up
		plotProvider.setPlotTitle(plotTitle);
		assertEquals(plotTitle, plotProvider.getPlotTitle());

	}

	/**
	 * Check the accessor and mutator for the x axis title.
	 */
	@Test
	public void checkSetAndGetXAxisTitle() {

		// check if the xAxisTitle matches
		plotProvider.setXAxisTitle(xAxisTitle);
		assertEquals(xAxisTitle, plotProvider.getXAxisTitle());

	}

	/**
	 * Check the accessor and mutator for the y axis title.
	 */
	@Test
	public void checkSetAndGetYAxisTitle() {

		// check if the yAxisTitle matches up
		plotProvider.setYAxisTitle(yAxisTitle);
		assertEquals(yAxisTitle, plotProvider.getYAxisTitle());

	}

	/**
	 * Check the accessor and mutator for the time units.
	 */
	@Test
	public void checkSetAndGetTimeUnits() {

		// check if the timeUnits match up
		plotProvider.setTimeUnits(timeUnits);
		assertEquals(timeUnits, plotProvider.getTimeUnits());

	}

	/**
	 * Check the accessor the time.
	 */
	@Test
	public void checkGetTimes() {

		// Set times
		seriesMap.put(times.get(0), new ArrayList<SeriesProvider>());
		seriesMap.put(times.get(1), new ArrayList<SeriesProvider>());
		seriesMap.put(times.get(2), new ArrayList<SeriesProvider>());

		// Check if the times in the series map match up
		assertEquals(times, new ArrayList<Double>(seriesMap.keySet()));

	}

	/**
	 * Check the setting of the contour plot flag
	 */
	@Test
	public void checkSetAndIsContour() {

		// Check if it is a contour
		plotProvider.setPlotAsContour();
		assertTrue(plotProvider.isContour());

	}

}
