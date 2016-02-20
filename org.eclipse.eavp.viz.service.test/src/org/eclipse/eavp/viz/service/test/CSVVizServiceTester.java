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
package org.eclipse.eavp.viz.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.IVizService;
import org.eclipse.eavp.viz.service.csv.CSVProxyPlot;
import org.eclipse.eavp.viz.service.csv.CSVVizService;
import org.junit.Test;

/**
 * This class is responsible for testing the CSVVizService.
 * 
 * @author Jay Jay Billings
 * 
 */
public class CSVVizServiceTester {

	/**
	 * Test method for
	 * {@link org.eclipse.eavp.viz.service.csv.CSVVizService#getName()}.
	 */
	@Test
	public void testGetName() {
		IVizService service = new CSVVizService();
		assertEquals("ice-plot", service.getName());
		assertEquals("2.0", service.getVersion());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.eavp.viz.service.csv.CSVVizService#createPlot(java.net.URI)}
	 * .
	 */
	@Test
	public void testCreatePlot() {
		// Make sure the properties are initially empty
		IVizService service = new CSVVizService();

		// Create a URI pointing to the fib8.csv file for testing.
		String s = System.getProperty("file.separator");
		String home = System.getProperty("user.home");
		File file = new File(
				home + s + "ICETests" + s + "CSVVizService" + s + "fib8.csv");

		// Try to create a plot using the file.
		IPlot plot = null;
		try {
			plot = service.createPlot(file.toURI());
		} catch (Exception e) {
			fail("CSVVizService error: " + "Exception thrown for valid URI \""
					+ file.getPath() + "\". See stack trace below:");
			e.printStackTrace();
		}
		assertNotNull(plot);
		assertEquals(file.toURI(), plot.getDataSource());

		// It should return a proxy to a CSVPlot.
		assertTrue(plot instanceof CSVProxyPlot);

		// Passing a null URI should throw an exception.
		URI uri = null;
		try {
			service.createPlot(uri);
		} catch (NullPointerException e) {
			// Thrown as expected.
		} catch (Exception e) {
			// Some other exception was thrown. The super method is not called
			// first!
			fail("CSVVizServiceTester error: "
					+ "NullPointerException not thrown for a null URI. "
					+ "Does CSVVizService call the super class' "
					+ "createPlot(...)?");
		}

		// Passing in a URI with an unsupported extension should throw an
		// exception.
		try {
			uri = new URI("blah.jpg");
			service.createPlot(uri);
		} catch (IllegalArgumentException e) {
			// Thrown as expected.
		} catch (URISyntaxException e) {
			// This should never happen.
			fail("CSVVizServiceTester error: "
					+ "Could not create test URI. This should never happen!");
		} catch (Exception e) {
			// Some other exception was thrown. The super method is not called
			// first!
			fail("CSVVizServiceTester error: "
					+ "IllegalArgumentException not thrown for a URI with an "
					+ "unsupported extension. Does CSVVizService call the "
					+ "super class' createPlot(...)?");
		}

		return;
	}

	/**
	 * Checks that the CSVVizService supports the correct file extensions.
	 */
	@Test
	public void checkExtensions() {
		CSVVizService service = new CSVVizService();
		List<String> extensions = new ArrayList<String>();
		// Only CSV files are supported.
		extensions.add("csv");

		// Check the contents of the supported extension set.
		for (String extension : extensions) {
			// Check that the extension is in the set of supported extensions.
			assertTrue(
					"CSVVizServiceTester error: " + "Extension \"" + extension
							+ "\" not supported.",
					service.getSupportedExtensions().contains(extension));
		}

		return;
	}

}