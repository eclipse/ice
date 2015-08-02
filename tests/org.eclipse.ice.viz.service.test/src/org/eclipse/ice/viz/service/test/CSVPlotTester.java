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
package org.eclipse.ice.viz.service.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.ice.viz.service.csv.CSVPlot;
import org.eclipse.ice.viz.service.csv.CSVSeries;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
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
	 * The test shell
	 */
	private Shell shell;

	/**
	 * @throws java.lang.Exception
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

		return;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {

		// Delete the CSV file
		if (file.exists()) {
			file.delete();
		}
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.viz.service.csv.CSVPlot#getPlotTypes()}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetPlotTypes() throws Exception {

		// Create and load the plot
		CSVPlot plot = new CSVPlot(file.toURI());
		plot.load();
		Thread.currentThread();
		Thread.sleep(2000);

		// Test the independent series
		CSVSeries series = (CSVSeries) plot.getIndependentSeries();
		assertEquals(series.getLabel(), "t");
		assertEquals(series.get(0), 1.0);
		assertEquals(series.get(1), 2.0);
		assertEquals(series.get(2), 3.0);
		// No random time should be assigned
		assertEquals(series.getTime(), 0.0);

		ArrayList<ISeries> depSeries = (ArrayList<ISeries>) plot
				.getDependentSeries(null);
		assertEquals(depSeries.size(), 2);
		// Check the dependent series. No real need to check the values, as that
		// is the same for the independent series
		CSVSeries dep1 = (CSVSeries) depSeries.get(0);
		assertTrue(
				dep1.getLabel().equals("p_x") || dep1.getLabel().equals("p_y"));
		// Check the second series
		CSVSeries dep2 = (CSVSeries) depSeries.get(1);
		assertTrue(
				dep2.getLabel().equals("p_x") || dep2.getLabel().equals("p_y"));

		// Try adding a series and seeing if the plot changes
		CSVSeries newSeries = new CSVSeries();
		newSeries.add(4.0);
		newSeries.add(6.0);
		newSeries.add(8.0);
		newSeries.setLabel("p_z");
		plot.addDependentSeries(newSeries);

		// Check to see if it was added
		assertEquals(plot.getDependentSeries(null).size(), 3);

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.viz.service.csv.CSVPlot#getNumberOfAxes()}.
	 */
	@Test
	public void testGetNumberOfAxes() {
		IPlot plot = new CSVPlot(null);
		// The CSVPlot should always have only 2 axes.
		assertEquals(2, plot.getNumberOfAxes());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.viz.service.csv.CSVPlot#getProperties()}.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testGetProperties() throws Exception {

		IPlot plot = new CSVPlot(null);
		Map<String, String> props = plot.getProperties();
		// The CSVPlot should always have an empty property map, at least for
		// now.
		assertTrue(props.isEmpty());
		// Make sure that passing them back doesn't spontaneously change
		// anything.
		plot.setProperties(props);
		props = null;
		props = plot.getProperties();
		assertTrue(props.isEmpty());

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.viz.service.csv.CSVPlot#getDataSource()}.
	 */
	@Test
	public void testGetDataSource() {

		// Create the plot using the source file
		IPlot plot = new CSVPlot(file.toURI());
		// Make sure the plot reports the right file
		assertEquals(file.toURI(), plot.getDataSource());
		// Make sure it reports the right host details, namely localhost
		assertFalse(plot.isSourceRemote());

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.viz.service.csv.CSVPlot#draw(java.lang.String, org.eclipse.swt.widgets.Composite)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testDraw() throws Exception {

		// Create and load the plot
		final CSVPlot plot = new CSVPlot(file.toURI());
		plot.load();
		// Give a couple of seconds for the load() thread to run.
		Thread.currentThread();
		Thread.sleep(2000);

		// Grab the shell to render the plot.
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM);
				shell.setFullScreen(true);
				shell.setText("TITLEBAR!!!!");
				shell.setLayout(new GridLayout(1, false));
				// Create a composite for it.
				Composite testComposite = new Composite(shell, SWT.None);
				testComposite.setLayout(new GridLayout(1, true));
				testComposite.setLayoutData(
						new GridData(SWT.LEFT, SWT.FILL, true, true, 1, 1));

				// Draw the plot in the test composite.
				try {
					plot.draw(testComposite);
				} catch (Exception e) {
					// Complain
					e.printStackTrace();
					fail();
				}

				// Open the shell and lay it out before running the tests.
				shell.open();
				shell.layout();
			}
		});

		// FIXME The slider should only show if the plot has multiple times
		// defined.
		// // Check for a few simple things just to make sure the plot area was
		// // rendered.
		// SWTBotLabel sliderLabel = bot.label("Slider: ");
		// SWTBotButton upButton = bot.button(">");
		// SWTBotButton downButton = bot.button("<");

		// Cleaning up just seems like the right proper thing to do.
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				shell.dispose();
			}
		});
		return;
	}

}