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

import static org.junit.Assert.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.viz.service.csv.CSVPlot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotEditor;
import org.eclipse.swtbot.eclipse.gef.finder.SWTBotGefTestCase;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
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
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// Create a small CSV file for testing the plot
		String separator = System.getProperty("file.separator");
		String home = System.getProperty("user.home");
		file = new File(home + separator + "ICETests" + separator
				+ "CSVPlot.csv");
		String line1 = "#features,t, p_x, p_y";
		String line2 = "#units,t,p_x,p_y";
		String line3 = "1.0,1.0,1.0";
		String line4 = "2.0,4.0,8.0";
		String line5 = "3.0,9.0,27.0";
		FileWriter writer = new FileWriter(file);
		BufferedWriter bWriter = new BufferedWriter(writer);
		bWriter.write(line1);
		bWriter.newLine();
		bWriter.write(line2);
		bWriter.newLine();
		bWriter.write(line3);
		bWriter.newLine();
		bWriter.write(line4);
		bWriter.newLine();
		bWriter.write(line5);
		bWriter.newLine();
		bWriter.close();
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

		// Get the types
		Map<String, String[]> types = plot.getPlotTypes();

		// Check them
		assertTrue(types.containsKey("line"));
		assertTrue(types.containsKey("scatter"));
		List<String> lineTypes = Arrays.asList(types.get("line"));
		List<String> scatterTypes = Arrays.asList(types.get("scatter"));
		// Check line types
		assertTrue(lineTypes.contains("t vs. p_x"));
		assertTrue(lineTypes.contains("t vs. p_y"));
		assertTrue(lineTypes.contains("p_x vs. t"));
		assertTrue(lineTypes.contains("p_x vs. p_y"));
		assertTrue(lineTypes.contains("p_y vs. t"));
		assertTrue(lineTypes.contains("p_y vs. p_x"));
		// Check scatter types
		assertTrue(scatterTypes.contains("t vs. p_x"));
		assertTrue(scatterTypes.contains("t vs. p_y"));
		assertTrue(scatterTypes.contains("p_x vs. t"));
		assertTrue(scatterTypes.contains("p_x vs. p_y"));
		assertTrue(scatterTypes.contains("p_y vs. t"));
		assertTrue(scatterTypes.contains("p_y vs. p_x"));

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
		Shell shell = new Shell(Display.getDefault(), SWT.SHELL_TRIM);
		shell.setFullScreen(true);
		shell.setText("TITLEBAR!!!!");
		shell.setLayout(new GridLayout(1, false));
		// Create a composite for it.
		Composite testComposite = new Composite(shell, SWT.None);
		testComposite.setLayout(new GridLayout(1, true));
		testComposite.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true,
				true, 1, 1));

		// Draw the plot in the test composite.
		plot.draw("scatter", "t vs. p_x", testComposite);

		// Open the shell and lay it out before running the tests.
		shell.open();
		shell.layout();

		// Grab the active shell and make sure it is the correct one.
		SWTBotShell botShell = bot.activeShell();
		assertEquals(botShell.getText(), shell.getText());

		// Check for a few simple things just to make sure the plot area was
		// rendered.
		SWTBotLabel sliderLabel = bot.label("Slider: ");
		SWTBotButton upButton = bot.button(">");
		SWTBotButton downButton = bot.button("<");

		// Cleaning up just seems like the right proper thing to do.
		shell.dispose();

		return;
	}

}
