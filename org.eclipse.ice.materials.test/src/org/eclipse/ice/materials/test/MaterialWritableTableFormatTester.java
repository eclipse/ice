/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay 
 *   Billings
 *   Kasper Gammeltoft (added additional testing for implementation in 
 *   MaterialWritableTableFormat class)
 *******************************************************************************/
package org.eclipse.ice.materials.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.ice.materials.MaterialWritableTableFormat;
import org.eclipse.january.form.Material;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class is responsible for testing {@link MaterialWritableTableFormat}.
 * 
 * @author Jay Jay Billings, Kasper Gammeltoft
 *
 */
public class MaterialWritableTableFormatTester {

	/**
	 * The column names to use for the test
	 */
	private static ArrayList<String> columnNames;

	/**
	 * The test material
	 */
	private static Material material;

	/**
	 * The table format to test
	 */
	private static MaterialWritableTableFormat tableFormat;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// Create the Material for the test
		material = new Material();
		material.setName("Fluffy");
		material.setProperty("A", 1.0);
		material.setProperty("B", 2.0);

		// Store the property names
		columnNames = new ArrayList<String>();
		columnNames.addAll(material.getProperties().keySet());

		// Create the table format
		tableFormat = new MaterialWritableTableFormat(columnNames);

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.MaterialWritableTableFormat#getColumnCount()}
	 * .
	 */
	@Test
	public void testGetColumnCount() {
		// It should contain all of the properties names + 1 because of the
		// "Name" column.
		assertEquals(columnNames.size() + 1, tableFormat.getColumnCount());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.MaterialWritableTableFormat#getColumnName(int)}
	 * .
	 */
	@Test
	public void testGetColumnName() {

		// Check the "Name" column.
		assertEquals("Name",tableFormat.getColumnName(0));
		
		// Just make sure the column names exist. Checking the order is a little
		// overkill. Start at 1 because "Name" is always there.
		for (int i = 1; i < columnNames.size(); i++) {
			assertTrue(columnNames.contains(tableFormat.getColumnName(i)));
		}

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.MaterialWritableTableFormat#getColumnValue(org.eclipse.january.form.Material, int)}
	 * .
	 */
	@Test
	public void testGetColumnValue() {

		// Reset the property values since the tests can run out of order.
		material.setProperty("A", 1.0);
		material.setProperty("B", 2.0);

		// Check that the value is either the value for A or the value for B.
		// Since there is no guarantee on order, this is the only good way to do
		// it.
		Object value = tableFormat.getColumnValue(material, 0);
		assertEquals(value, "Fluffy");
		Double dValue = (Double) tableFormat.getColumnValue(material, 1);
		assertTrue(dValue.equals(1.0) || dValue.equals(2.0));
		dValue = (Double) tableFormat.getColumnValue(material, 2);
		assertTrue(dValue.equals(1.0) || dValue.equals(2.0));

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.MaterialWritableTableFormat#isEditable(org.eclipse.january.form.Material, int)}
	 * .
	 */
	@Test
	public void testIsEditable() {
		assertTrue(tableFormat.isEditable(material, 0));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.MaterialWritableTableFormat#setColumnValue(org.eclipse.january.form.Material, java.lang.Object, int)}
	 * .
	 */
	@Test
	public void testSetColumnValue() {
		assertEquals(material, tableFormat.setColumnValue(material, 2.5, 0));
		
		// Make sure that a blank material's name can be set
		Material blank = new Material();
		String newName = "New Material";
		assertEquals(blank, tableFormat.setColumnValue(blank, newName, 0));
		assertEquals(blank.getName(), newName);
	}

}
