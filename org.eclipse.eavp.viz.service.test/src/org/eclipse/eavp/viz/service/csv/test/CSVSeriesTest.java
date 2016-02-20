/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation
 *   - Kasper Gammeltoft
 *******************************************************************************/

package org.eclipse.eavp.viz.service.csv.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.ISeriesStyle;
import org.eclipse.eavp.viz.service.csv.CSVSeries;
import org.eclipse.eavp.viz.service.styles.BasicErrorStyle;
import org.eclipse.eavp.viz.service.styles.XYZSeriesStyle;
import org.junit.Test;

/**
 * This class is responsible for testing the CSVSeries class. It tests both the
 * methods specific to this series and the interface implementations of
 * {@link ISeries}
 * 
 * @author Kasper Gammeltoft
 *
 */
public class CSVSeriesTest {

	@Test
	public void testCSVSeries() {
		// Test that the series was constructed correctly with logical default
		// values
		CSVSeries series = new CSVSeries();
		assertNotNull(series);
		assertNotNull(series.getStyle());
		assertNotNull(series.getLabel());
		assertTrue(0.0 == series.getTime());

	}

	@Test
	public void testHashCode() {
		// Create a new series with some values
		CSVSeries series = new CSVSeries();
		series.add(54.54);
		series.add(20.97);
		series.add(30.3);
		series.setLabel("Test label");
		series.setEnabled(false);

		// get the hash
		int hash = series.hashCode();

		// Change the values and make sure the hash changes!
		series.setUnit("cm");
		assertFalse(hash == series.hashCode());
		hash = series.hashCode();
		series.setEnabled(true);
		assertFalse(hash == series.hashCode());
		hash = series.hashCode();
		series.remove(1);
		assertFalse(hash == series.hashCode());
		hash = series.hashCode();
		series.setTime(5.0);
		assertFalse(hash == series.hashCode());
		hash = series.hashCode();

		// Make sure that equal objects have the same hash
		series = getSeries("test");
		CSVSeries series2 = getSeries("test");
		assertEquals(series.hashCode(), series2.hashCode());

	}

	@Test
	public void testGetBounds() {
		// Create a new series to test the bounds of
		CSVSeries series = new CSVSeries();
		// A new series, with no data, should have no bounds
		assertNull(series.getBounds());
		// Add some test data
		series.add(2.0);
		series.add(3.0);
		series.add(7.0);
		// Get the bounds and make sure they are correct
		double[] bounds = series.getBounds();
		assertTrue(2.0 == bounds[0]);
		assertTrue(7.0 == bounds[0] + bounds[1]);
		assertTrue(5.0 == bounds[1]);

	}

	@Test
	public void testGetDataPoints() {
		// Creates a new series to test
		CSVSeries series = getSeries("test");
		// Gets the data and tests it, from the getSeries(String lbl) method
		Object[] data = series.getDataPoints();
		assertEquals(3, data.length);
		assertEquals(2.0, data[0]);
		assertEquals(4.0, data[1]);
		assertEquals(8.0, data[2]);

		// Creates a new series and makes sure the data is not null, but does
		// not have any points either
		series = new CSVSeries();
		assertNotNull(series.getDataPoints());
		assertEquals(0, series.getDataPoints().length);

	}

	@Test
	public void testGetParentSeries() {
		// Create some new series to test
		CSVSeries series = new CSVSeries();
		CSVSeries parent = new CSVSeries();
		// The parent should be null when first constructed!
		assertNull(series.getParentSeries());
		// Set the parent and test
		series.setParentSeries(parent);
		series.setStyle(new BasicErrorStyle());
		// Make sure the parent was set properly
		assertEquals(parent, series.getParentSeries());

		// Change some random things and check that it does not change the
		// parent
		series.add(4.0);
		series.setLabel("something else");
		series.setTime(3.0);
		assertEquals(parent, series.getParentSeries());
	}

	@Test
	public void testGetStyle() {
		// Create a series and get its style
		CSVSeries series = getSeries("stylin");
		ISeriesStyle style = series.getStyle();
		// The style should never be null. This should be the default
		assertNotNull(style);
		// Make sure that the default does not have a color associated with it
		assertNull(style.getProperty(XYZSeriesStyle.COLOR));

		// Make sure the new style assignment stays
		BasicErrorStyle style2 = new BasicErrorStyle();
		series.setStyle(style2);
		assertEquals(style2, series.getStyle());
		// Make sure changes to the series do not effect the style
		series.add(4394.32);
		series.setLabel("Hello..");
		series.setTime(3.424);
		series.setUnit("Some Unit");
		assertEquals(style2, series.getStyle());

	}

	@Test
	public void testCloneAndCopy() {
		// Get some new series to test
		CSVSeries series = getSeries("Test");
		// Clone and make sure it is equal to the original
		CSVSeries clone = (CSVSeries) series.clone();
		assertEquals(series, clone);
		// Copy and make sure that the copy is now equal to both the clone and
		// the original series
		CSVSeries copy = new CSVSeries();
		copy.copy(series);
		assertEquals(copy, series);
		assertEquals(copy, clone);
	}

	@Test
	public void testEqualsObject() {
		// Create new series to test
		CSVSeries series1 = getSeries("one");
		CSVSeries series2 = getSeries("two");
		CSVSeries series3 = getSeries("one");

		// Make sure that the equivalent series are equal and the non-equivalent
		// are not equal
		assertTrue(series1.equals(series3));
		assertFalse(series1.equals(series2));

		// Change some thing abound the third series and now it should not be
		// equal
		series3.add(4.0);
		assertFalse(series1.equals(series3));
		// Set the name to the same as the first series and now they should be
		// equal
		series2.setLabel("one");
		assertTrue(series1.equals(series2));

		// Make sure anything that is changed will not allow for equality
		// amongst the series
		series2.setEnabled(false);
		assertFalse(series1.equals(series2));

		// Change it back and make sure that nothing else changed (still equal)
		series2.setEnabled(true);
		assertTrue(series1.equals(series2));

		// Make sure anything that is changed will not allow for equality
		// amongst the series
		series2.setTime(2.202);
		assertFalse(series1.equals(series2));

		// Change it back and make sure that nothing else changed (still equal)
		series2.setTime(2.0);
		assertTrue(series1.equals(series2));

		// Make sure anything that is changed will not allow for equality
		// amongst the series
		series2.setUnit("cm");
		assertFalse(series1.equals(series2));

		// Change it back and make sure that nothing else changed (still equal)
		series2.setUnit("unit");
		assertTrue(series1.equals(series2));

		// Make sure anything that is changed will not allow for equality
		// amongst the series
		series2.setParentSeries(series3);
		assertFalse(series1.equals(series2));

		// Change it back and make sure that nothing else changed (still equal)
		series2.setParentSeries(null);
		assertTrue(series1.equals(series2));

	}

	/**
	 * Convenience method for getting an instantiated CSVSeries to test
	 * 
	 * @param lbl
	 *            The name of this series
	 * @return A new csv series, with three data points and most other variables
	 *         set
	 */
	private CSVSeries getSeries(String lbl) {
		// Create new series and set properties
		CSVSeries series = new CSVSeries();
		series.setLabel(lbl);
		series.setEnabled(true);
		series.setTime(2.0);
		series.setUnit("unit");
		series.add(2.0);
		series.add(4.0);
		series.add(8.0);
		// Return the series
		return series;
	}

}
