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

import org.eclipse.eavp.viz.service.AbstractSeries;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.ISeriesStyle;
import org.eclipse.eavp.viz.service.ProxySeries;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests that the {@link ProxySeries} implementation relies on either
 * the basic functionality inherited from {@link AbstractSeries} or on its
 * source {@link ISeries} if set.
 * 
 * @author Jordan Deyton
 *
 */
public class ProxySeriesTester {

	/**
	 * The series that will be tested.
	 */
	private ProxySeries proxy;

	/**
	 * A series that can be used as the source for the {@link #proxy}.
	 */
	private ISeries source;

	// ---- Source Data ---- //
	private double[] bounds;
	private String category;
	private Object[] dataPoints;
	private ISeries parentSeries;
	private double time;
	// --------------------- //

	/**
	 * Initializes the class variables and sets the source of the proxy.
	 */
	@Before
	public void beforeEachTest() {
		// Create a new default proxy series.
		proxy = new ProxySeries();

		// Create a basic series.
		source = new AbstractSeries() {
			@Override
			public double[] getBounds() {
				return bounds;
			}

			@Override
			public String getCategory() {
				return category;
			}

			@Override
			public Object[] getDataPoints() {
				return dataPoints;
			}

			@Override
			public ISeries getParentSeries() {
				return parentSeries;
			}

			@Override
			public double getTime() {
				return time;
			}

			@Override
			public void setTime(double time) {
				ProxySeriesTester.this.time = time;
			}

		};

		// Set up the source series' data.
		bounds = new double[] { 0.0, 1000.0 };
		category = "category";
		dataPoints = new Double[] { 0.0, 1.0, 2.0, 3.0 };
		parentSeries = new FakeSeries(category);
		time = 42.0;

		// Set the proxy's source.
		proxy.setSource(source);

		return;
	}

	/**
	 * Checks that the enabled flag can be set.
	 */
	@Test
	public void checkEnabled() {
		// Initially it is not enabled.
		assertFalse(proxy.isEnabled());

		// We should be able to enable it.
		proxy.setEnabled(true);
		assertTrue(proxy.isEnabled());

		// We should be able to disable it.
		proxy.setEnabled(false);
		assertFalse(proxy.isEnabled());

		return;
	}

	/**
	 * Checks that the bounds are retrieved from the source if set.
	 */
	@Test
	public void checkGetBounds() {
		assertNotNull(proxy.getBounds());
		assertEquals(bounds.length, proxy.getBounds().length);
		for (int i = 0; i < bounds.length; i++) {
			assertEquals(bounds[i], proxy.getBounds()[i], 1e-7);
		}
	}

	/**
	 * Checks that the bounds cannot be retrieved if no source is set.
	 */
	@Test
	public void checkGetBoundsWithoutSource() {
		proxy.setSource(null);
		assertNull(proxy.getBounds());
	}

	/**
	 * Checks that the data points are retrieved from the source if set.
	 */
	@Test
	public void checkGetDataPoints() {
		assertNotNull(proxy.getDataPoints());
		assertEquals(dataPoints.length, proxy.getDataPoints().length);
		for (int i = 0; i < dataPoints.length; i++) {
			assertEquals(dataPoints[i], proxy.getDataPoints()[i]);
		}
	}

	/**
	 * Checks that the data points cannot be retrieved if no source is set.
	 */
	@Test
	public void checkGetDataPointsWithoutSource() {
		proxy.setSource(null);
		assertNull(proxy.getDataPoints());
	}

	/**
	 * Checks that the time is based on the source if the source is set.
	 */
	@Test
	public void checkTime() {
		// We should not be able to set the time.
		assertEquals(time, proxy.getTime(), 1e-7);
		proxy.setTime(-1.0);
		assertEquals(-1.0, proxy.getTime(), 1e-7);
		assertEquals(-1.0, source.getTime(), 1e-7);
		proxy.setTime(1.0);
		assertEquals(1.0, proxy.getTime(), 1e-7);
		assertEquals(1.0, source.getTime(), 1e-7);

		return;
	}

	/**
	 * Checks that the time cannot be set or retrieved if no source is set.
	 */
	@Test
	public void checkTimeWithoutSource() {
		proxy.setSource(null);
		// We should not be able to set the time.
		assertEquals(0.0, proxy.getTime(), 1e-7);
		proxy.setTime(-1.0);
		assertEquals(0.0, proxy.getTime(), 1e-7);
		proxy.setTime(1.0);
		assertEquals(0.0, proxy.getTime(), 1e-7);
	}

	/**
	 * Checks that the parent series are retrieved from the source if set.
	 */
	@Test
	public void checkGetParentSeries() {
		assertSame(parentSeries, proxy.getParentSeries());
		parentSeries = new FakeSeries("otherCategory");
		assertSame(parentSeries, proxy.getParentSeries());
	}

	/**
	 * Checks that the parent series cannot be retrieved if no source is set.
	 */
	@Test
	public void checkGetParentSeriesWithoutSource() {
		proxy.setSource(null);
		assertNull(proxy.getParentSeries());
	}

	/**
	 * Checks that the category is retrieved from the source if set.
	 */
	@Test
	public void checkGetCategory() {
		assertEquals(category, proxy.getCategory());
		category = "otherCategory";
		assertEquals(category, proxy.getCategory());
	}

	/**
	 * Checks that the category cannot be retrieved if no source is set.
	 */
	@Test
	public void checkGetCategoryWithoutSource() {
		proxy.setSource(null);
		assertNull(proxy.getCategory());
	}

	/**
	 * Checks that the label can be set.
	 */
	@Test
	public void checkLabel() {
		// Initially it should be unset.
		assertNull(proxy.getLabel());

		// We should be able to set it.
		proxy.setLabel("label");
		assertEquals("label", proxy.getLabel());

		// We should be able to unset it.
		proxy.setLabel(null);
		assertNull(proxy.getLabel());

		return;
	}

	/**
	 * Checks that the style can be set.
	 */
	@Test
	public void checkStyle() {
		ISeriesStyle style = new FakeSeriesStyle();

		// Initially it should be unset.
		assertNull(proxy.getStyle());

		// We should be able to set it.
		proxy.setStyle(style);
		assertSame(style, proxy.getStyle());

		// We should be able to unset it.
		proxy.setStyle(null);
		assertNull(proxy.getStyle());

		return;
	}

}
