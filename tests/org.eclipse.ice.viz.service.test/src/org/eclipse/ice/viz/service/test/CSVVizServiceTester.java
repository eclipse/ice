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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizService;
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

		// Create a small CSV file for testing the plot
		String separator = System.getProperty("file.separator");
		String home = System.getProperty("user.home");
		File file = new File(home + separator + "ICETests");

		// Try to create a plot using the file... er... directory. It shouldn't
		// matter for this test anyway. (At least not yet.)
		IPlot plot = service.createPlot(file.toURI());
		assertNotNull(plot);
		assertEquals(file.toURI(), plot.getDataSource());

		// In the future this should somehow test that the plot was actually
		// loaded.

		return;
	}
}
