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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.eavp.viz.service.AbstractPlot;
import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.ProxyPlot;
import org.eclipse.eavp.viz.service.ProxySeries;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests that the {@link ProxyPlot} implementation relies on either
 * the basic functionality inherited from {@link AbstractPlot} or on its source
 * {@link IPlot} if set.
 * 
 * @author Jordan Deyton
 *
 */
public class ProxyPlotTester {

	/**
	 * The plot that will be tested.
	 */
	private ProxyPlot proxy;

	/**
	 * A plot that can be used as the source for the {@link #proxy}.
	 */
	private IPlot source;

	// ---- Source Data ---- //
	/**
	 * The data source for the source plot.
	 */
	private URI uri;
	/**
	 * The dependent series for the source plot.
	 */
	private Map<String, List<ISeries>> sourceSeries;
	// --------------------- //

	private class FakeProxySeries extends ProxySeries {

		public ISeries source;

		@Override
		public void setSource(ISeries source) {
			super.setSource(source);
			this.source = source;
		}
	}

	/**
	 * Instantiates {@link #proxy}.
	 */
	@Before
	public void beforeEachTest() {
		// Create a default proxy plot.
		proxy = new ProxyPlot() {
			@Override
			protected ProxySeries createProxySeries(ISeries source) {
				ProxySeries proxy = new FakeProxySeries();
				proxy.setSource(source);
				return proxy;
			}
		};

		// Create a basic plot.
		source = new AbstractPlot() {
			@Override
			public List<String> getCategories() {
				return new ArrayList<String>(sourceSeries.keySet());
			}

			@Override
			public URI getDataSource() {
				return uri;
			}

			@Override
			public List<ISeries> getDependentSeries(String category) {
				List<ISeries> series = sourceSeries.get(category);
				if (series != null) {
					series = new ArrayList<ISeries>(series);
				}
				return series;
			}

			@Override
			public int getNumberOfAxes() {
				return 10;
			}
		};

		// Add two series to a category.
		String category = "scatman";
		ISeries series;
		List<ISeries> seriesList = new ArrayList<ISeries>();
		series = new FakeSeries(category);
		series.setLabel("bobeebop");
		seriesList.add(series);
		series = new FakeSeries(category);
		series.setLabel("diddlydiddlypop");
		seriesList.add(series);
		sourceSeries = new HashMap<String, List<ISeries>>();
		sourceSeries.put(category, seriesList);

		// Set the proxy's source.
		proxy.setSource(source);

		return;
	}

	/**
	 * Checks that the plot's categories are not managed by the abstract plot.
	 */
	@Test
	public void checkCategoriesWithoutSource() {
		proxy.setSource(null);

		// With no source set, the categories should be empty.
		assertNotNull(proxy.getCategories());
		assertTrue(proxy.getCategories().isEmpty());
	}

	/**
	 * Checks that the plot's categories are the same as the source plot's when
	 * the source plot is set.
	 */
	@Test
	public void checkCategories() {
		// With a source set, the categories should rely on the source's
		// categories.
		assertNotNull(proxy.getCategories());
		assertEquals(sourceSeries.size(), proxy.getCategories().size());
		// Check that each category reported by the proxy plot is valid.
		for (String category : proxy.getCategories()) {
			assertTrue(sourceSeries.containsKey(category));
		}

		return;
	}

	/**
	 * Checks the data source is not managed by the abstract plot, although the
	 * operations that query the data source are implemented.
	 */
	@Test
	public void checkDataSourceWithoutSource() {
		proxy.setSource(null);

		// The default source is null.
		assertNull(proxy.getDataSource());
		assertNull(proxy.getSourceHost());
		assertFalse(proxy.isSourceRemote());
	}

	/**
	 * Checks that the data source is the same as the source plot's when the
	 * source plot is set.
	 */
	@Test
	public void checkDataSource() {

		assertSame(uri, proxy.getDataSource());

		// Set the reference to a local file (no host on the URI).
		try {
			uri = new URI("file:///home/test");
		} catch (URISyntaxException e) {
			fail("AbstractPlotTester error: "
					+ "Invalid URI. This should never happen.");
		}
		assertSame(uri, proxy.getDataSource());
		assertEquals("localhost", proxy.getSourceHost());
		assertFalse(proxy.isSourceRemote());

		// Set the reference to a local file (there is a host on the URI).
		try {
			uri = new URI("ssh://localhost:1000/home/test");
		} catch (URISyntaxException e) {
			fail("AbstractPlotTester error: "
					+ "Invalid URI. This should never happen.");
		}
		assertSame(uri, proxy.getDataSource());
		assertEquals("localhost", proxy.getSourceHost());
		assertFalse(proxy.isSourceRemote());

		// Set the reference to a remote file (there is a host on the URI).
		try {
			uri = new URI("ssh://remotehost:1000/home/test");
		} catch (URISyntaxException e) {
			fail("AbstractPlotTester error: "
					+ "Invalid URI. This should never happen.");
		}
		assertSame(uri, proxy.getDataSource());
		assertEquals("remotehost", proxy.getSourceHost());
		assertTrue(proxy.isSourceRemote());

		return;
	}

	/**
	 * Checks that getting the dependent series returns null (categories and
	 * series are not managed by the abstract plot).
	 */
	@Test
	public void checkDependentSeriesWithoutSource() {
		proxy.setSource(null);
		assertNull(proxy.getDependentSeries(null));
		assertNull(proxy.getDependentSeries("category"));
		assertNull(proxy.getDependentSeries("default"));
	}

	/**
	 * Checks that getting the dependent series returns null (categories and
	 * series are not managed by the abstract plot).
	 */
	@Test
	public void checkDependentSeries() {

		// Invalid categories should return null.
		assertNull(proxy.getDependentSeries(null));
		assertNull(proxy.getDependentSeries("category"));

		// Check that all valid categories are reported.
		for (Entry<String, List<ISeries>> e : sourceSeries.entrySet()) {
			String category = e.getKey();
			List<ISeries> series = e.getValue();

			List<ISeries> proxySeries = proxy.getDependentSeries(category);
			// Check the number of proxy series created. It should match the
			// number of source series.
			assertNotNull(proxySeries);
			assertEquals(series.size(), proxySeries.size());
			// For each proxy series created, check its source series matches
			// the one in this test class' map of source series.
			for (int i = 0; i < series.size(); i++) {
				assertTrue(proxySeries.get(i) instanceof FakeProxySeries);
				assertSame(series.get(i),
						((FakeProxySeries) proxySeries.get(i)).source);
			}
		}

		return;
	}

	/**
	 * Checks that the draw operation throws an exception by default.
	 */
	@Test
	public void checkDraw() {
		// The ProxyPlot cannot be used to draw unless a sub-class overrides
		// the method.
		try {
			proxy.draw(null);
			fail("ProxyPlotTester error: "
					+ "No exception thrown when trying to draw.");
		} catch (Exception e) {
			// Exception thrown as expected.
		}
	}

	/**
	 * Checks the default return value for the number of axes.
	 */
	@Test
	public void checkGetNumberOfAxesWithoutSource() {
		proxy.setSource(null);
		assertEquals(0, proxy.getNumberOfAxes());
	}

	/**
	 * Checks the default return value for the number of axes.
	 */
	@Test
	public void checkGetNumberOfAxes() {
		assertEquals(source.getNumberOfAxes(), proxy.getNumberOfAxes());
	}

	/**
	 * Checks that the independent series can be set to anything.
	 */
	@Test
	public void checkIndependentSeries() {
		ISeries fakeSeries = new FakeSeries("category");

		// Initially it is null, but it can be set to a new value.
		assertNull(proxy.getIndependentSeries());
		proxy.setIndependentSeries(fakeSeries);
		assertSame(fakeSeries, proxy.getIndependentSeries());

		// It can also be set to null.
		proxy.setIndependentSeries(null);
		assertNull(proxy.getIndependentSeries());

		return;
	}

	/**
	 * Checks that the properties are not maintained by the abstract class.
	 */
	@Test
	public void checkProperties() {
		// The properties are empty.
		assertNotNull(proxy.getProperties());
		assertTrue(proxy.getProperties().isEmpty());

		// The properties are empty after trying to set with a null map.
		try {
			proxy.setProperties(null);
		} catch (Exception e) {
			fail("AbstractPlotTester error: "
					+ "Exception thrown when setting the properties.");
		}
		assertNotNull(proxy.getProperties());
		assertTrue(proxy.getProperties().isEmpty());

		// The properties are empty after trying to set with a non-empty map.
		Map<String, String> properties = new HashMap<String, String>();
		properties.put("property", "value");
		try {
			proxy.setProperties(properties);
		} catch (Exception e) {
			fail("AbstractPlotTester error: "
					+ "Exception thrown when setting the properties.");
		}
		assertNotNull(proxy.getProperties());
		assertTrue(proxy.getProperties().isEmpty());

		return;
	}

	/**
	 * Checks that the plot title can be set to anything.
	 */
	@Test
	public void checkTitle() {
		// Initially it is null, but it can be set to a new value.
		assertNull(proxy.getPlotTitle());
		proxy.setPlotTitle("title");
		assertEquals("title", proxy.getPlotTitle());

		// The title is managed by the proxy, not the source.
		assertNotEquals(proxy.getPlotTitle(), source.getPlotTitle());

		// It can also be set to null.
		proxy.setPlotTitle(null);
		assertNull(proxy.getPlotTitle());

		return;
	}

}
