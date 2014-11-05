/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets.reactoreditor.sfr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.client.widgets.reactoreditor.sfr.SFRComponentInfo;

import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.junit.Test;

/**
 * This class tests the SFRComponentInfo utility class used in the
 * ReactorEditor's SFR package.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class SFRComponentInfoTester {

	/**
	 * Test the construction. This makes sure all fields are configured
	 * correctly.
	 */
	@Test
	public void checkConstruction() {

		// Test construction for a null component.
		int row = 513421;
		int column = -213;
		SFRComponent component = null;
		IDataProvider dataProvider = null;

		SFRComponentInfo info = new SFRComponentInfo(row, column, component);
		assertEquals(row, info.row);
		assertEquals(column, info.column);
		assertSame(component, info.sfrComponent);
		;
		assertSame(dataProvider, info.dataProvider);

		// Test construction for a non-null component.
		row = -2131;
		column = 32137;
		component = new SFRComponent("Just your every day SFRComponent.");

		info = new SFRComponentInfo(row, column, component);
		assertEquals(row, info.row);
		assertEquals(column, info.column);
		assertSame(component, info.sfrComponent);
		assertSame(dataProvider, info.dataProvider);

		// Test the full constructor with the data provider.
		row = 281;
		column = 5992;
		component = new SFRComponent("Puma Man! He flies like a moron!");
		dataProvider = new SFRComponent("Data");

		info = new SFRComponentInfo(row, column, component, dataProvider);
		assertEquals(row, info.row);
		assertEquals(column, info.column);
		assertSame(component, info.sfrComponent);
		assertSame(dataProvider, info.dataProvider);

		return;
	}

	/**
	 * Tests the matches method.
	 */
	@Test
	public void checkMatches() {

		int row1, column1, row2, column2;
		SFRComponent component1, component2;
		IDataProvider dataProvider1, dataProvider2;
		SFRComponentInfo info1, info2;

		// The same row, column, and same SFRComponent reference should match.
		row1 = -9781;
		column1 = 42317;
		component1 = new SFRComponent();
		dataProvider1 = null;
		info1 = new SFRComponentInfo(row1, column1, component1);

		row2 = -9781;
		column2 = 42317;
		component2 = component1;
		dataProvider2 = dataProvider1;
		info2 = new SFRComponentInfo(row2, column2, component2);

		assertTrue(info1.matches(info2));
		assertTrue(info2.matches(info1));

		// While we're at it, test reflexivity.
		assertTrue(info1.matches(info1));
		assertTrue(info2.matches(info2));

		// The same row, column, but different SFRComponent reference should not
		// match.
		component1 = new SFRComponent();
		info1 = new SFRComponentInfo(row1, column1, component1);

		component2 = new SFRComponent();
		info2 = new SFRComponentInfo(row2, column2, component2);

		assertFalse(info1.matches(info2));
		assertFalse(info2.matches(info1));

		// Different rows/columns but the same SFRComponent reference should not
		// match (try different row).
		row1 = -9781;
		column1 = 42317;
		component1 = new SFRComponent();
		info1 = new SFRComponentInfo(row1, column1, component1);

		row2 = -9780;
		column2 = 42317;
		component2 = component1;
		info2 = new SFRComponentInfo(row2, column2, component2);

		assertFalse(info1.matches(info2));
		assertFalse(info2.matches(info1));

		// Different rows/columns but the same SFRComponent reference should not
		// match (try different column).
		row1 = -9781;
		column1 = 42317;
		component1 = new SFRComponent();
		info1 = new SFRComponentInfo(row1, column1, component1);

		row2 = -9781;
		column2 = 0;
		component2 = component1;
		info2 = new SFRComponentInfo(row2, column2, component2);

		assertFalse(info1.matches(info2));
		assertFalse(info2.matches(info1));

		// Now try a different data provider.
		row2 = row1;
		column2 = column1;
		component2 = component1;
		dataProvider2 = new SFRComponent("Data");
		info2 = new SFRComponentInfo(row2, column2, component2, dataProvider2);

		assertFalse(info1.matches(info2));
		assertFalse(info2.matches(info1));

		// Comparing against a null should return false.
		assertFalse(info1.matches(null));
		assertFalse(info2.matches(null));

		return;
	}

}
