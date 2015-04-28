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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizService;
import org.eclipse.ice.viz.service.csv.CSVPlot;
import org.eclipse.ice.viz.service.csv.CSVVizService;
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
	 * {@link org.eclipse.ice.viz.service.csv.CSVVizService#getName()}.
	 */
	@Test
	public void testGetName() {
		IVizService service = new CSVVizService();
		assertEquals("ice-plot", service.getName());
		assertEquals("2.0", service.getVersion());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.viz.service.csv.CSVVizService#getConnectionProperties()}
	 * .
	 */
	@Test
	public void testGetConnectionProperties() {
		// Make sure the properties are initially empty
		IVizService service = new CSVVizService();
		assertTrue(service.getConnectionProperties().isEmpty());

		// Create a map with some property in it
		Map<String, String> props = new HashMap<String, String>();
		props.put("foo", "bar");

		// Make sure it stays empty when the setter is called because this
		// service does not require connection properties.
		service.setConnectionProperties(props);
		assertTrue(service.getConnectionProperties().isEmpty());

		// Make sure the connect() operation always returns true.
		assertTrue(service.connect());

		// Make sure that the service says it doesn't need properties from the
		// convenience method.
		assertFalse(service.hasConnectionProperties());

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.viz.service.csv.CSVVizService#createPlot(java.net.URI)}
	 * .
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreatePlot() throws Exception {
		// Make sure the properties are initially empty
		IVizService service = new CSVVizService();

		// Create a URI pointing to the fib8.csv file for testing.
		String s = System.getProperty("file.separator");
		String home = System.getProperty("user.home");
		File file = new File(home + s + "ICETests" + s + "CSVVizService" + s
				+ "fib8.csv");

		// Try to create a plot using the file.
		IPlot plot = service.createPlot(file.toURI());
		assertNotNull(plot);
		assertEquals(file.toURI(), plot.getDataSource());

		// It should actually return a CSVPlot, although that *could* change in
		// the future.
		assertTrue(plot instanceof CSVPlot);

		return;
	}

	/**
	 * Checks that the VisItVizService supports the correct file extensions.
	 */
	@Test
	public void checkExtensions() {
		CSVVizService service = new CSVVizService();

		List<String> extensions = new ArrayList<String>();
		// Only CSV files are supported.
		extensions.add("csv");

		// Check that each extension is supported by creating a simple URI with
		// its extension and calling extensionSupported(URI).
		for (String extension : extensions) {
			try {
				// Check that the extension is supported.
				URI uri = new URI("blah." + extension);
				assertTrue("The extension \"" + extension
						+ "\" is not supported.",
						service.extensionSupported(uri));
			} catch (URISyntaxException e) {
				// This should never happen...
				fail("CSVVizServiceTester error: " + "A test URI was invalid.");
				e.printStackTrace();
			}
		}

		return;
	}
}
