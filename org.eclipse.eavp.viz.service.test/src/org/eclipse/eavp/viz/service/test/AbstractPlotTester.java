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
package org.eclipse.eavp.viz.service.test;

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

import org.eclipse.eavp.viz.service.AbstractPlot;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
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
	 * Checks the data source is managed by the abstract plot, and that the
	 * operations that query the data source are implemented.
	 */
	@Test
	public void checkDataSource() {
		// The default source is null.
		assertNull(plot.getDataSource());
		assertNull(plot.getSourceHost());
		assertFalse(plot.isSourceRemote());

		// We should be able to set the plot's URI. Set the reference to a local
		// file (no host on the URI).
		URI uri = null;
		try {
			uri = new URI("file:///some/path/to/a/file");
			assertTrue(plot.setDataSource(uri));
			assertFalse(plot.setDataSource(uri)); // Can't set the same thing.
			assertSame(uri, plot.getDataSource());
			assertEquals("localhost", plot.getSourceHost());
			assertFalse(plot.isSourceRemote());
		} catch (URISyntaxException e) {
			fail("Bad URI. This shouldn't happen.");
		} catch (Exception e) {
			fail(getClass().getName() + " failure: "
					+ "Exception thrown when setting the URI.");
		}

		// Set the reference to a local file (there is a host on the URI).
		try {
			uri = new URI("ssh://localhost:1000/home/test");
			assertTrue(plot.setDataSource(uri));
			assertSame(uri, plot.getDataSource());
			assertEquals("localhost", plot.getSourceHost());
			assertFalse(plot.isSourceRemote());
		} catch (URISyntaxException e) {
			fail("Bad URI. This shouldn't happen.");
		} catch (Exception e) {
			fail(getClass().getName() + " failure: "
					+ "Exception thrown when setting the URI.");
		}

		// Set the reference to a remote file (there is a host on the URI).
		try {
			uri = new URI("ssh://remotehost:1000/home/test");
			assertTrue(plot.setDataSource(uri));
			assertSame(uri, plot.getDataSource());
			assertEquals("remotehost", plot.getSourceHost());
			assertTrue(plot.isSourceRemote());
		} catch (URISyntaxException e) {
			fail("Bad URI. This shouldn't happen.");
		} catch (Exception e) {
			fail(getClass().getName() + " failure: "
					+ "Exception thrown when setting the URI.");
		}

		// We should be able to unset the URI.
		try {
			uri = null;
			assertTrue(plot.setDataSource(uri));
			assertNull(plot.getDataSource());
			assertNull(plot.getSourceHost());
			assertFalse(plot.isSourceRemote());
		} catch (URISyntaxException e) {
			fail("Bad URI. This shouldn't happen.");
		} catch (Exception e) {
			fail(getClass().getName() + " failure: "
					+ "Exception thrown when setting the URI.");
		}

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
	 * Checks that the provided methods to register, notify, and unregister
	 * listeners works.
	 */
	@Test
	public void checkPlotListeners() {
		plot = new AbstractPlot() {
			@Override
			public void setPlotTitle(String title) {
				notifyPlotListeners("key", "value");
			}
		};

		// You can't add a null listener.
		assertFalse(plot.addPlotListener(null));

		FakePlotListener listener = new FakePlotListener();

		// You can only add a listener once.
		assertTrue(plot.addPlotListener(listener));
		assertFalse(plot.addPlotListener(listener));
		plot.setPlotTitle("trigger a notification");

		// The listener should have been notified. This means the plot, key, and
		// value references should have been set.
		assertTrue(listener.wasNotified(2000));
		assertSame(plot, listener.plot);
		assertEquals("key", listener.key);
		assertEquals("value", listener.value);
		listener.reset();

		// You can only remove a listener once.
		assertTrue(plot.removePlotListener(listener));
		assertFalse(plot.removePlotListener(listener));

		// The listener should not have been notified. This means the plot, key,
		// and value references are still unset.
		plot.setPlotTitle("trigger another notification");
		assertFalse(listener.wasNotified(500));

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
