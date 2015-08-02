/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.viz.service.AbstractPlot;
import org.eclipse.ice.viz.service.ISeries;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the basic, limited functionality provided by
 * {@link AbstractPlot}.
 * 
 * @author Jordan Deyton
 *
 */
public class AbstractPlotTester {

	/**
	 * The plot that will be tested.
	 */
	private AbstractPlot plot;

	/**
	 * Instantiates {@link #plot}.
	 */
	@Before
	public void beforeEachTest() {
		plot = new AbstractPlot() {

		};
	}

	/**
	 * Checks that the plot's categories are not managed by the abstract plot.
	 */
	@Test
	public void checkCategories() {
		assertNotNull(plot.getCategories());
		assertTrue(plot.getCategories().isEmpty());
	}

	/**
	 * Checks the data source is not managed by the abstract plot, although the
	 * operations that query the data source are implemented.
	 */
	@Test
	public void checkDataSource() {
		// The default source is null.
		assertNull(plot.getDataSource());
		assertNull(plot.getSourceHost());
		assertFalse(plot.isSourceRemote());

		// Create a plot to point to a URI reference.
		final AtomicReference<URI> uriRef = new AtomicReference<URI>();
		plot = new AbstractPlot() {
			public URI getDataSource() {
				return uriRef.get();
			};
		};

		// Set the reference to a local file (no host on the URI).
		try {
			uriRef.set(new URI("file:///home/test"));
		} catch (URISyntaxException e) {
			fail("AbstractPlotTester error: "
					+ "Invalid URI. This should never happen.");
		}
		assertNotNull(plot.getDataSource());
		assertEquals("localhost", plot.getSourceHost());
		assertFalse(plot.isSourceRemote());

		// Set the reference to a local file (there is a host on the URI).
		try {
			uriRef.set(new URI("ssh://localhost:1000/home/test"));
		} catch (URISyntaxException e) {
			fail("AbstractPlotTester error: "
					+ "Invalid URI. This should never happen.");
		}
		assertNotNull(plot.getDataSource());
		assertEquals("localhost", plot.getSourceHost());
		assertFalse(plot.isSourceRemote());

		// Set the reference to a remote file (there is a host on the URI).
		try {
			uriRef.set(new URI("ssh://remotehost:1000/home/test"));
		} catch (URISyntaxException e) {
			fail("AbstractPlotTester error: "
					+ "Invalid URI. This should never happen.");
		}
		assertNotNull(plot.getDataSource());
		assertEquals("remotehost", plot.getSourceHost());
		assertTrue(plot.isSourceRemote());

		return;
	}

	/**
	 * Checks that getting the dependent series returns null (categories and
	 * series are not managed by the abstract plot).
	 */
	@Test
	public void checkDependentSeries() {
		assertNull(plot.getDependentSeries(null));
		assertNull(plot.getDependentSeries("category"));
		assertNull(plot.getDependentSeries("default"));
	}

	/**
	 * Checks that the draw operation throws an exception by default.
	 */
	@Test
	public void checkDraw() {
		// The AbstractPlot cannot be used to draw unless a sub-class overrides
		// the method.
		try {
			plot.draw(null);
			fail("AbstractPlotTester error: "
					+ "No exception thrown when trying to draw.");
		} catch (Exception e) {
			// Exception thrown as expected.
		}
		return;
	}

	/**
	 * Checks the default return value for the number of axes.
	 */
	@Test
	public void checkGetNumberOfAxes() {
		assertEquals(0, plot.getNumberOfAxes());
	}

	/**
	 * Checks that the independent series can be set to anything.
	 */
	@Test
	public void checkIndependentSeries() {
		ISeries fakeSeries = new FakeSeries("category");

		// Initially it is null, but it can be set to a new value.
		assertNull(plot.getIndependentSeries());
		plot.setIndependentSeries(fakeSeries);
		assertSame(fakeSeries, plot.getIndependentSeries());

		// It can also be set to null.
		plot.setIndependentSeries(null);
		assertNull(plot.getIndependentSeries());

		return;
	}

	/**
	 * Checks that the properties are not maintained by the abstract class.
	 */
	@Test
	public void checkProperties() {
		// The properties are empty.
		assertNotNull(plot.getProperties());
		assertTrue(plot.getProperties().isEmpty());

		// The properties are empty after trying to set with a null map.
		try {
			plot.setProperties(null);
		} catch (Exception e) {
			fail("AbstractPlotTester error: "
					+ "Exception thrown when setting the properties.");
		}
		assertNotNull(plot.getProperties());
		assertTrue(plot.getProperties().isEmpty());

		// The properties are empty after trying to set with a non-empty map.
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("property", "value");
		try {
			plot.setProperties(properties);
		} catch (Exception e) {
			fail("AbstractPlotTester error: "
					+ "Exception thrown when setting the properties.");
		}
		assertNotNull(plot.getProperties());
		assertTrue(plot.getProperties().isEmpty());

		return;
	}

	/**
	 * Checks that the plot title can be set to anything.
	 */
	@Test
	public void checkTitle() {
		// Initially it is null, but it can be set to a new value.
		assertNull(plot.getPlotTitle());
		plot.setPlotTitle("title");
		assertEquals("title", plot.getPlotTitle());

		// It can also be set to null.
		plot.setPlotTitle(null);
		assertNull(plot.getPlotTitle());

		return;
	}

}
