/*******************************************************************************
 * Copyright (c) 2014-2015 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz.service.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.csv.CSVSeries;
import org.eclipse.eavp.viz.service.csv.PlotProvider;
import org.eclipse.eavp.viz.service.styles.XYZAxisStyle;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is responsible for testing the PlotProvider
 * 
 * @author Claire Saunders
 * @author Kasper Gammeltoft- Refactored to test the new ISeries based
 *         implementation of the plot provider
 * 
 */
public class PlotProviderTester {

	/**
	 * Class variables to be used by the tests.
	 */
	private String plotTitle;
	private String xAxisTitle;
	private String yAxisTitle;
	private String timeUnits;
	private ArrayList<Double> times;
	private ArrayList<ISeries> depSeries;
	private ISeries indepSeries;
	private PlotProvider plotProvider;
	private CSVSeries testSeries;

	/**
	 * Intialize variables to be used throughout.
	 */
	@Before
	public void beforeClass() {

		plotTitle = "Title of Plot";
		xAxisTitle = "X-Axis Title";
		yAxisTitle = "Y-Axis Title";
		timeUnits = "Seconds";
		plotProvider = new PlotProvider();
		testSeries = new CSVSeries();
		testSeries.setLabel("y");
		times = new ArrayList<Double>();
		depSeries = new ArrayList<ISeries>();
		indepSeries = new CSVSeries();
		indepSeries.setLabel("x");

		// Add double values to the arrayList Times
		times.add(1.0);
		times.add(2.0);
		times.add(3.0);

		// Make a seriesproviderList of size 1 for testing purposes
		depSeries.add(testSeries);

	}

	/**
	 * Checks that series can be correctly removed from the PlotProvider.
	 */
	@Test
	public void checkSeriesAddRemove() {

		// A PlotProvider whose remove functionality will be tested. No other
		// tests should interfere, hence we do not use the class variable.
		final PlotProvider plotProvider = new PlotProvider();

		// Series and times to add and later *remove*. Each series is added for
		// each time, resulting in 4 total additions.
		final CSVSeries series1 = new CSVSeries();
		series1.setLabel("series1");
		final CSVSeries series2 = new CSVSeries();
		series2.setLabel("series2");
		final double time1 = 42.0;
		final double time2 = 343.1337;

		// Used as the return value from getSeriesAtTime(). This is necessary to
		// verify that series were in fact removed from the PlotProvider.
		List<ISeries> testSeries;
		List<Double> times;

		// Add each series to time1.
		plotProvider.addSeries(time1, series1);
		plotProvider.addSeries(time1, series2);

		// Verify that the only time is time1, but it has both series 1 and 2.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertEquals(2, testSeries.size());
		assertTrue(testSeries.contains(series1));
		assertTrue(testSeries.contains(series2));
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertNotNull(testSeries);
		assertTrue(testSeries.isEmpty());
		// time1 is the only time in the PlotProvider.
		times = plotProvider.getTimes();
		assertEquals(1, times.size());
		assertTrue(times.contains(time1));
		assertFalse(times.contains(time2));

		// Add each series to time2.
		plotProvider.addSeries(time2, series1);
		plotProvider.addSeries(time2, series2);

		// Verify that all series are associated with each time.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertEquals(2, testSeries.size());
		assertTrue(testSeries.contains(series1));
		assertTrue(testSeries.contains(series2));
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertEquals(2, testSeries.size());
		assertTrue(testSeries.contains(series1));
		assertTrue(testSeries.contains(series2));
		// Each time now has a SeriesProvider in the PlotProvider.
		times = plotProvider.getTimes();
		assertEquals(2, times.size());
		assertTrue(times.contains(time1));
		assertTrue(times.contains(time2));

		// Try some invalid remove commands. Nothing should change.
		plotProvider.removeSeries(-1.0, series1);
		plotProvider.removeSeries(time1, null);

		// Verify that nothing changed.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertEquals(2, testSeries.size());
		assertTrue(testSeries.contains(series1));
		assertTrue(testSeries.contains(series2));
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertEquals(2, testSeries.size());
		assertTrue(testSeries.contains(series1));
		assertTrue(testSeries.contains(series2));
		// Each time still has a SeriesProvider in the PlotProvider.
		times = plotProvider.getTimes();
		assertEquals(2, times.size());
		assertTrue(times.contains(time1));
		assertTrue(times.contains(time2));

		// Now remove series 1 from time 1, and remove series 2 from time 2.
		plotProvider.removeSeries(time1, series1);
		plotProvider.removeSeries(time2, series2);

		// Series 2 should be the only series for time 1. Likewise, series 1
		// should be the only series for time 2.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertEquals(1, testSeries.size());
		assertFalse(testSeries.contains(series1));
		assertTrue(testSeries.contains(series2));
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));
		assertFalse(testSeries.contains(series2));
		// Note that each time still has a SeriesProvider in the PlotProvider.
		times = plotProvider.getTimes();
		assertEquals(2, times.size());
		assertTrue(times.contains(time1));
		assertTrue(times.contains(time2));

		// Try the same remove commands. Nothing should change.
		plotProvider.removeSeries(time1, series1);
		plotProvider.removeSeries(time2, series2);

		// Verify that nothing changed.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertEquals(1, testSeries.size());
		assertFalse(testSeries.contains(series1));
		assertTrue(testSeries.contains(series2));
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));
		assertFalse(testSeries.contains(series2));
		// Note that each time still has a SeriesProvider in the PlotProvider.
		times = plotProvider.getTimes();
		assertEquals(2, times.size());
		assertTrue(times.contains(time1));
		assertTrue(times.contains(time2));

		// Remove the last series for time1.
		plotProvider.removeSeries(time1, series2);

		// Now only time2 has series1.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertNotNull(testSeries);
		assertTrue(testSeries.isEmpty());
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));
		assertFalse(testSeries.contains(series2));
		// Only time2 is in the PlotProvider.
		times = plotProvider.getTimes();
		assertEquals(1, times.size());
		assertFalse(times.contains(time1));
		assertTrue(times.contains(time2));

		// Remove the last series (for time2).
		plotProvider.removeSeries(time2, series1);

		// Now there are no more times or series.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertNotNull(testSeries);
		assertTrue(testSeries.isEmpty());
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertNotNull(testSeries);
		assertTrue(testSeries.isEmpty());
		// No time is in the PlotProvider.
		times = plotProvider.getTimes();
		assertEquals(0, times.size());

		return;
	}

	/**
	 * Check retrieval from the series map.
	 */
	@Test
	public void checkGetSeriesAtTime() {

		// A PlotProvider whose time management functionality will be tested. No
		// other tests should interfere, hence we do not use the class variable.
		final PlotProvider plotProvider = new PlotProvider();

		// Series and times for testing. Each series is added for each time,
		// while the getTimes() method is checked at each change.
		final CSVSeries series1 = new CSVSeries();
		series1.setLabel("series1");
		final CSVSeries series2 = new CSVSeries();
		series2.setLabel("series2");
		final double time1 = 42.0;
		final double time2 = 343.1337;
		final double time3 = 0.0;

		// Used as the return value from getSeriesAtTime(). This is necessary to
		// verify that series were in fact removed from the PlotProvider.
		List<ISeries> testSeries;

		// Initially, the list of series is null for each time.
		assertNotNull(plotProvider.getSeriesAtTime(time1));
		assertTrue(plotProvider.getSeriesAtTime(time1).isEmpty());
		assertNotNull(plotProvider.getSeriesAtTime(time2));
		assertTrue(plotProvider.getSeriesAtTime(time2).isEmpty());
		assertNotNull(plotProvider.getSeriesAtTime(time3));
		assertTrue(plotProvider.getSeriesAtTime(time3).isEmpty());

		// Add a series to time1. series1 is the only series at time1.
		plotProvider.addSeries(time1, series1);
		// series1 is the only series at time1.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));
		assertNotNull(plotProvider.getSeriesAtTime(time2));
		assertTrue(plotProvider.getSeriesAtTime(time2).isEmpty());
		assertNotNull(plotProvider.getSeriesAtTime(time3));
		assertTrue(plotProvider.getSeriesAtTime(time3).isEmpty());

		// Try adding a null series to time2. Nothing should change.
		plotProvider.addSeries(time2, null);
		// series1 is the only series at time1.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));
		assertNotNull(plotProvider.getSeriesAtTime(time2));
		assertTrue(plotProvider.getSeriesAtTime(time2).isEmpty());
		assertNotNull(plotProvider.getSeriesAtTime(time3));
		assertTrue(plotProvider.getSeriesAtTime(time3).isEmpty());

		// Now add a series to time2. time1 and time2 should now both be in the
		// PlotProvider.
		plotProvider.addSeries(time2, series2);
		// series1 is the only series at time1. series2 is the only series at
		// time2.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series2));
		assertNotNull(plotProvider.getSeriesAtTime(time3));
		assertTrue(plotProvider.getSeriesAtTime(time3).isEmpty());

		// Add another series to time2. There will now be 2 series at time2.
		plotProvider.addSeries(time2, series1);
		// series1 is the only series at time1. Both series are at time2.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertEquals(2, testSeries.size());
		assertTrue(testSeries.contains(series2));
		assertTrue(testSeries.contains(series1));
		assertNotNull(plotProvider.getSeriesAtTime(time3));
		assertTrue(plotProvider.getSeriesAtTime(time3).isEmpty());

		// Add a series to time3.
		plotProvider.addSeries(time3, series1);
		// time1 has series1. time2 has both series. time3 has series1.
		testSeries = plotProvider.getSeriesAtTime(time1);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertEquals(2, testSeries.size());
		assertTrue(testSeries.contains(series2));
		assertTrue(testSeries.contains(series1));
		testSeries = plotProvider.getSeriesAtTime(time3);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));

		// Remove the series from time1. Now time1 will have no series.
		plotProvider.removeSeries(time1, series1);
		// time1 has no series. time2 has both series. time3 has series1.
		assertNotNull(plotProvider.getSeriesAtTime(time1));
		assertTrue(plotProvider.getSeriesAtTime(time1).isEmpty());
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertEquals(2, testSeries.size());
		assertTrue(testSeries.contains(series2));
		assertTrue(testSeries.contains(series1));
		testSeries = plotProvider.getSeriesAtTime(time3);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));

		// Remove one of the series from time2.
		plotProvider.removeSeries(time2, series1);
		// time1 has no series. time2 has series2. time3 has series1.
		assertNotNull(plotProvider.getSeriesAtTime(time1));
		assertTrue(plotProvider.getSeriesAtTime(time1).isEmpty());
		testSeries = plotProvider.getSeriesAtTime(time2);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series2));
		testSeries = plotProvider.getSeriesAtTime(time3);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));

		// Now remove the last series at time2.
		plotProvider.removeSeries(time2, series2);
		// series1 is forever alone at time3...
		assertNotNull(plotProvider.getSeriesAtTime(time1));
		assertTrue(plotProvider.getSeriesAtTime(time2).isEmpty());
		assertNotNull(plotProvider.getSeriesAtTime(time2));
		assertTrue(plotProvider.getSeriesAtTime(time2).isEmpty());
		testSeries = plotProvider.getSeriesAtTime(time3);
		assertEquals(1, testSeries.size());
		assertTrue(testSeries.contains(series1));

		return;
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
		plotProvider.getXAxisStyle().setProperty(XYZAxisStyle.AXIS_TITLE,
				xAxisTitle);
		assertEquals(xAxisTitle, plotProvider.getXAxisStyle()
				.getProperty(XYZAxisStyle.AXIS_TITLE));

	}

	/**
	 * Check the accessor and mutator for the y axis title.
	 */
	@Test
	public void checkSetAndGetYAxisTitle() {

		// check if the yAxisTitle matches up
		plotProvider.getXAxisStyle().setProperty(XYZAxisStyle.AXIS_TITLE,
				yAxisTitle);
		assertEquals(yAxisTitle, plotProvider.getXAxisStyle()
				.getProperty(XYZAxisStyle.AXIS_TITLE));

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

		// A PlotProvider whose time management functionality will be tested. No
		// other tests should interfere, hence we do not use the class variable.
		final PlotProvider plotProvider = new PlotProvider();

		// Series and times for testing. Each series is added for each time,
		// while the getTimes() method is checked at each change.
		final CSVSeries series1 = new CSVSeries();
		series1.setLabel("series1");
		final CSVSeries series2 = new CSVSeries();
		series2.setLabel("series2");
		final double time1 = 42.0;
		final double time2 = 343.1337;
		final double time3 = 0.0;

		// The accepted difference between two doubles, required for comparing
		// doubles with assertEquals(...).
		final double delta = 1e-4;

		// Used as the return value from getSeriesAtTime(). This is necessary to
		// verify that series were in fact removed from the PlotProvider.
		List<Double> times;

		// Initially, the list of times is empty.
		times = plotProvider.getTimes();
		assertEquals(0, times.size());

		// Add a series to time1, which becomes the only time in the
		// PlotProvider.
		plotProvider.addSeries(time1, series1);
		// time1 is the only time in the PlotProvider.
		times = plotProvider.getTimes();
		assertEquals(1, times.size());
		assertTrue(times.contains(time1));
		assertFalse(times.contains(time2));
		assertFalse(times.contains(time3));

		// Try adding a null series to time2. Nothing should change.
		plotProvider.addSeries(time2, null);
		// time1 is the only time in the PlotProvider.
		times = plotProvider.getTimes();
		assertEquals(1, times.size());
		assertTrue(times.contains(time1));
		assertFalse(times.contains(time2));
		assertFalse(times.contains(time3));

		// Now add a series to time2. time1 and time2 should now both be in the
		// PlotProvider.
		plotProvider.addSeries(time2, series2);
		// time1 and time2 are in the PlotProvider, but not time3. Check the
		// order.
		times = plotProvider.getTimes();
		assertEquals(2, times.size());
		assertEquals(time1, times.get(0), delta);
		assertEquals(time2, times.get(1), delta);

		// Add another series to time2. Nothing should change.
		plotProvider.addSeries(time2, series1);
		// time1 and time2 are in the PlotProvider, but not time3. Check the
		// order.
		times = plotProvider.getTimes();
		assertEquals(2, times.size());
		assertEquals(time1, times.get(0), delta);
		assertEquals(time2, times.get(1), delta);

		// Add a series to time3. All of the test times should be in the
		// PlotProvider.
		plotProvider.addSeries(time3, series1);
		// time1, time2, and time3 are in the PlotProvider. Check the order.
		times = plotProvider.getTimes();
		assertEquals(3, times.size());
		assertEquals(time3, times.get(0), delta);
		assertEquals(time1, times.get(1), delta);
		assertEquals(time2, times.get(2), delta);

		// Remove the series from time1. Now time1 will not be in the list.
		plotProvider.removeSeries(time1, series1);
		// time2 and time3 are now left. Check the order.
		times = plotProvider.getTimes();
		assertEquals(2, times.size());
		assertEquals(time3, times.get(0), delta);
		assertEquals(time2, times.get(1), delta);

		// Remove one of the series from time2. time2 should still be in the
		// list, as there's another series for time2.
		plotProvider.removeSeries(time2, series1);
		// time2 and time3 are left. Check the order.
		times = plotProvider.getTimes();
		assertEquals(2, times.size());
		assertEquals(time3, times.get(0), delta);
		assertEquals(time2, times.get(1), delta);

		// Now remove the last series at time2. time3 should be all that's left.
		plotProvider.removeSeries(time2, series2);
		// time3 is forever alone...
		times = plotProvider.getTimes();
		assertEquals(1, times.size());
		assertEquals(time3, times.get(0), delta);

		return;
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
