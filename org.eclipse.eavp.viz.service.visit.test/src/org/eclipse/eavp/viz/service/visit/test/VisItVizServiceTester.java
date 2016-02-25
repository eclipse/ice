/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Jordan Deyton
 *   Jordan Deyton - 
 *******************************************************************************/
package org.eclipse.eavp.viz.service.visit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.visit.VisItVizService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class is responsible for testing the {@link VisItVizService}.
 * 
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItVizServiceTester {

	/**
	 * The viz service that will be tested.
	 */
	private VisItVizService service;

	/**
	 * Initializes class variables used for testing.
	 */
	@Before
	public void beforeEachTest() {
		service = new VisItVizService();
	}

	/**
	 * This test checks the name of the visualization service.
	 * 
	 * @see VisItVizService#getName()
	 */
	@Test
	public void checkName() {
		assertEquals("VisIt", service.getName());
	}

	/**
	 * This test checks the version information for the service.
	 * 
	 * @see VisItVizService#getVersion()
	 */
	@Test
	public void checkVersion() {
		assertEquals("1.0", service.getVersion());
	}

	/**
	 * Checks that the VisItVizService supports the correct file extensions.
	 */
	@Test
	public void checkExtensions() {
		List<String> extensions = new ArrayList<String>();
		// ExodusII
		extensions.add("ex");
		extensions.add("e");
		extensions.add("exo");
		extensions.add("ex2");
		extensions.add("exii");
		extensions.add("gen");
		extensions.add("exodus");
		extensions.add("nemesis");
		// Silo
		extensions.add("silo");

		// Check the contents of the supported extension set.
		for (String extension : extensions) {
			// Check that the extension is in the set of supported extensions.
			assertTrue(
					"VisItVizServiceTester error: " + "Extension \"" + extension
							+ "\" not supported.",
					service.getSupportedExtensions().contains(extension));
		}

		return;
	}

	/**
	 * Checks that plots can be created with the viz service.
	 */
	@Ignore
	@Test
	public void checkCreatePlot() {
		fail("Not implemented.");
		// TODO To test a successful operation, we need a running VisIt instance
		// and a VisIt-compatible file.
	}

	/**
	 * This test checks the plots created by the service.
	 * 
	 * @see VisItVizService#createPlot(java.net.URI)
	 */
	@Test
	public void checkCreatePlotExceptions() {

		String host;
		String path;
		URI uri = null;

		// ---- Try a null URI. ---- //
		uri = null;
		try {
			service.createPlot(uri);
			fail("VisItVizServiceTester error: "
					+ "Did not throw an exception for a null URI.");
		} catch (NullPointerException e) {
			// Exception thrown as expected.
		} catch (Exception e) {
			fail("VisItVizServiceTester error: "
					+ "Did not throw a NullPointerException for a null URI.");
		}
		// ------------------------- //

		// ---- Try a URI with a bad extension. ---- //
		host = "megadrive";
		path = "/some_file.bad";
		try {
			uri = new URI("file://" + host + path);
		} catch (URISyntaxException e) {
			fail("VisItVizService error: " + "Invalid URI.");
			e.printStackTrace();
		}
		try {
			service.createPlot(uri);
			fail("VisItVizServiceTester error: "
					+ "Did not throw an exception for a URI with an "
					+ "unsupported extension.");
		} catch (IllegalArgumentException e) {
			// Exception thrown as expected.
		} catch (Exception e) {
			fail("VisItVizServiceTester error: "
					+ "Did not throw an IllegalArgumentException for a URI "
					+ "with an unsupported extension.");
		}
		// ----------------------------------------- //

		// ---- Try a URI for an unknown host. ---- //
		// Set up the URI.
		host = "megadrive";
		path = "/some_file.e";
		try {
			uri = new URI("file://" + host + path);
		} catch (URISyntaxException e) {
			fail("VisItVizServiceTester error: " + "Invalid URI.");
			e.printStackTrace();
		}

		// Creating a plot with an unknown host should throw an exception.
		try {
			service.createPlot(uri);
			fail("VisItVizServiceTester error: "
					+ "Exception not thrown for URI whose host has no "
					+ "configured viz connection.");
		} catch (Exception e) {
			// Exception expected.
		}
		// ---------------------------------------- //

		return;
	}
}
