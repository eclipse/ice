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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.AbstractSeries;
import org.eclipse.eavp.viz.service.ISeriesStyle;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the basic functionality provided by {@link AbstractSeries}.
 * 
 * @author Jordan Deyton
 *
 */
public class AbstractSeriesTester {

	/**
	 * The series to test.
	 */
	private AbstractSeries series;

	/**
	 * Initializes the {@link #series}.
	 */
	@Before
	public void beforeEachTest() {
		series = new AbstractSeries() {

		};
	}

	/**
	 * Checks that the enabled flag can be set.
	 */
	@Test
	public void checkEnabled() {
		// Initially it is not enabled.
		assertFalse(series.isEnabled());

		// We should be able to enable it.
		series.setEnabled(true);
		assertTrue(series.isEnabled());

		// We should be able to disable it.
		series.setEnabled(false);
		assertFalse(series.isEnabled());

		return;
	}

	/**
	 * Checks that the bounds cannot be retrieved.
	 */
	@Test
	public void checkGetBounds() {
		assertNull(series.getBounds());
	}

	/**
	 * Checks that the data points cannot be retrieved.
	 */
	@Test
	public void checkGetDataPoints() {
		assertNull(series.getDataPoints());
	}

	/**
	 * Checks that the time cannot be set or retrieved.
	 */
	@Test
	public void checkTime() {
		// We should not be able to set the time.
		assertEquals(0.0, series.getTime(), 1e-7);
		series.setTime(-1);
		assertEquals(0.0, series.getTime(), 1e-7);
		series.setTime(1);
		assertEquals(0.0, series.getTime(), 1e-7);
	}

	/**
	 * Checks that the parent series cannot be retrieved.
	 */
	@Test
	public void checkGetParentSeries() {
		assertNull(series.getParentSeries());
	}

	/**
	 * Checks that the category cannot be retrieved.
	 */
	@Test
	public void checkGetCategory() {
		assertNull(series.getCategory());
	}

	/**
	 * Checks that the label can be set.
	 */
	@Test
	public void checkLabel() {
		// Initially it should be unset.
		assertNull(series.getLabel());

		// We should be able to set it.
		series.setLabel("label");
		assertEquals("label", series.getLabel());

		// We should be able to unset it.
		series.setLabel(null);
		assertNull(series.getLabel());

		return;
	}

	/**
	 * Checks that the style can be set.
	 */
	@Test
	public void checkStyle() {
		ISeriesStyle style = new FakeSeriesStyle();

		// Initially it should be unset.
		assertNull(series.getStyle());

		// We should be able to set it.
		series.setStyle(style);
		assertSame(style, series.getStyle());

		// We should be able to unset it.
		series.setStyle(null);
		assertNull(series.getStyle());

		return;
	}
}
