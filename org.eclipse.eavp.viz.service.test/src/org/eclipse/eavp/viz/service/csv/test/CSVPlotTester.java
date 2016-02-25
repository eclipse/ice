/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.eavp.viz.service.csv.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.csv.CSVPlot;
import org.eclipse.eavp.viz.service.test.FakePlotListener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.gef.finder.SWTBotGefTestCase;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This class is responsible for testing CSVPlot.
 * 
 * @author Jay Jay Billings
 *
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class CSVPlotTester extends SWTBotGefTestCase {

	/**
	 * The test file that holds the small CSV plot
	 */
	private static File file;

	/**
	 * The plot that is to be tested.
	 */
	private static CSVPlot plot;

	/**
	 * The test shell
	 */
	private Shell shell;

	/**
	 * Sets up a temporary csv file.
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// Create a small CSV file for testing the plot
		String separator = System.getProperty("file.separator");
		String home = System.getProperty("user.home");
		file = new File(
				home + separator + "ICETests" + separator + "CSVPlot.csv");
		String line1 = "t, p_x, p_y";
		String line2 = "#units,t,p_x,p_y";
		String line3 = "1.0,1.0,1.0";
		String line4 = "2.0,4.0,8.0";
		String line5 = "3.0,9.0,27.0";
		PrintWriter writer = new PrintWriter(
				new BufferedWriter(new FileWriter(file)));
		writer.println(line1);
		writer.println(line2);
		writer.println(line3);
		writer.println(line4);
		writer.println(line5);
		writer.close();

		// Set the data source and wait for it to be loaded.
		plot = new CSVPlot();
		try {
			plot.setDataSource(file.toURI());
		} catch (Exception e) {
			fail("CSVPlot error: "
					+ "Exception while setting the data source.");
		}
		long maxWait = 5000;
		long interval = 50;
		long totalWait = 0;
		while (!plot.isLoaded() && totalWait < maxWait) {
			try {
				Thread.sleep(interval);
				totalWait += interval;
			} catch (InterruptedException e) {
				// Nothing to do.
			}
		}

		return;
	}

	/**
	 * Delets the temporary csv file.
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		// Delete the CSV file
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * Checks the default plot values before any data is loaded.
	 */
	@Test
	public void checkDefaults() {

		CSVPlot plot = new CSVPlot();

		// ---- Test IPlot Getters ---- //
		// Initially the categories are empty.
		assertNotNull(plot.getCategories());
		assertTrue(plot.getCategories().isEmpty());

		// Initially, the source is unset.
		assertNull(plot.getDataSource());
		assertNull(plot.getSourceHost());
		assertFalse(plot.isSourceRemote());

		// There are no dependent series.
		assertNull(plot.getDependentSeries(IPlot.DEFAULT_CATEGORY));

		// There is no independent series.
		assertNull(plot.getIndependentSeries());

		// There are 2 axes.
		assertEquals(2, plot.getNumberOfAxes());

		// The title is null.
		assertEquals("", plot.getPlotTitle());

		// The properties is an empty map.
		assertNotNull(plot.getProperties());
		assertTrue(plot.getProperties().isEmpty());
		// ---------------------------- //

		// It is not loaded.
		assertFalse(plot.isLoaded());

		return;
	}

	/**
	 * Checks that the categories are correctly loaded from the file.
	 */
	@Test
	public void checkCategories() {
		// The only category should be the default one.
		assertNotNull(plot.getCategories());
		assertEquals(1, plot.getCategories().size());
		assertTrue(plot.getCategories().contains(IPlot.DEFAULT_CATEGORY));
	}

	/**
	 * Checks that the data source getters are correctly loaded from the file.
	 */
	@Test
	public void checkDataSource() {
		// The data source should be set, and it is a local file.
		assertEquals(file.toURI(), plot.getDataSource());
		assertEquals("localhost", plot.getSourceHost());
		assertFalse(plot.isSourceRemote());
	}

	/**
	 * Checks that drawing is not supported directly by the CSVPlot, but from a
	 * proxy plot.
	 */
	@Test
	public void checkDraw() {
		try {
			plot.draw(shell);
			fail(getClass().getName() + " failure: "
					+ "This plot is not intended to support drawing.");
		} catch (Exception e) {
			// Exception thrown as expected.
		}
	}

	/**
	 * Checks that the dependent series are correctly loaded from the file.
	 */
	@Test
	public void checkDependentSeries() {
		// Get the dependent series.
		List<ISeries> seriesList = plot
				.getDependentSeries(IPlot.DEFAULT_CATEGORY);
		ISeries series;
		Object[] data;

		// Check the size of the list of dependent series.
		assertNotNull(seriesList);
		assertEquals(3, seriesList.size());

		// Check the first series.
		series = seriesList.get(0);
		assertEquals(IPlot.DEFAULT_CATEGORY, series.getCategory());
		assertEquals("p_x", series.getLabel());
		data = series.getDataPoints();
		assertNotNull(data);
		assertEquals(3, data.length);
		assertEquals(1.0, (double) data[0], 1e-7);
		assertEquals(4.0, (double) data[1], 1e-7);
		assertEquals(9.0, (double) data[2], 1e-7);

		// Check the second series.
		series = seriesList.get(1);
		assertEquals(IPlot.DEFAULT_CATEGORY, series.getCategory());
		assertEquals("p_y", series.getLabel());
		data = series.getDataPoints();
		assertNotNull(data);
		assertEquals(3, data.length);
		assertEquals(1.0, (double) data[0], 1e-7);
		assertEquals(8.0, (double) data[1], 1e-7);
		assertEquals(27.0, (double) data[2], 1e-7);

		return;
	}

	/**
	 * Checks that the independent series is correctly loaded from the file.
	 */
	@Test
	public void checkIndependentSeries() {
		ISeries series = plot.getIndependentSeries();
		Object[] data;

		// Check the content of the series.
		assertNotNull(series);
		assertEquals(IPlot.DEFAULT_CATEGORY, series.getCategory());
		assertEquals("t", series.getLabel());
		data = series.getDataPoints();
		assertNotNull(data);
		assertEquals(3, data.length);
		assertEquals(1.0, (double) data[0], 1e-7);
		assertEquals(2.0, (double) data[1], 1e-7);
		assertEquals(3.0, (double) data[2], 1e-7);

		return;
	}

	/**
	 * Checks that loading notifies plot listeners.
	 */
	@Test
	public void checkLoading() {

		// Add a listener.
		FakePlotListener listener = new FakePlotListener();
		plot.addPlotListener(listener);

		// Reload the plot.
		plot.load();

		// The listener should have been notified.
		assertTrue(listener.wasNotified(2000));
		assertTrue(plot.isLoaded());
		assertSame(plot, listener.plot);
		assertEquals("loaded", listener.key);
		assertEquals("true", listener.value);

		return;
	}

	/**
	 * Checks that the number of axes remains unchanged after loading.
	 */
	@Test
	public void checkNumberOfAxes() {
		assertEquals(2, plot.getNumberOfAxes());
	}

	/**
	 * Checks the default plot title after loading.
	 */
	@Test
	public void checkPlotTitle() {
		assertEquals("CSVPlot.csv", plot.getPlotTitle());
	}

	/**
	 * Checks that no properties are loaded from the file (it's a CSV file after
	 * all).
	 */
	@Test
	public void checkProperties() {
		// The properties are still an empty map.
		assertNotNull(plot.getProperties());
		assertTrue(plot.getProperties().isEmpty());
	}
}