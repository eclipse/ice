/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.tests.materials;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.MaterialStack;
import org.eclipse.ice.materials.MaterialStackWritableTableFormat;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test class for testing the MaterialStackWritableTableFormat class. Run as
 * JUnit test.
 * 
 * 
 * @author Kasper Gammeltoft
 * 
 */
public class MaterialStackWritableTableFormatTest {

	/**
	 * The table format to test
	 */
	static MaterialStackWritableTableFormat tableFormat;

	/**
	 * Some material stacks to test the table format with
	 */
	static MaterialStack stack;
	static MaterialStack stack2;

	static Material He;
	static Material Ne;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		// Setup. Instantiate the material stacks
		He = new Material();
		He.setName("Helium");

		// Instantiate the first stack
		stack = new MaterialStack(He, 3);

		// Make some other material
		Ne = new Material();
		Ne.setName("Neon");

		// Instantiate the second stack
		stack2 = new MaterialStack(Ne, 5);

		// Instantiate the format to test
		tableFormat = new MaterialStackWritableTableFormat();

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.MaterialStackWritableTableFormat#getColumnCount()}
	 * .
	 */
	@Test
	public void testGetColumnCount() {
		assertEquals(tableFormat.getColumnCount(), 2);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.MaterialStackWritableTableFormat#getColumnName(int column)}
	 * .
	 */
	@Test
	public void testGetColumnName() {
		assertEquals(tableFormat.getColumnName(0), "Material");
		assertEquals(tableFormat.getColumnName(1), "Amount");
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.MaterialStackWritableTableFormat#getColumnValue(MaterialStack stack, int column)}
	 * .
	 */
	@Test
	public void testGetColumnValue() {

		// Test getting value from first column
		Object val = tableFormat.getColumnValue(stack, 0);
		assertEquals(val, stack.getMaterial().getName());

		// Test getting value from second column
		Object val2 = tableFormat.getColumnValue(stack, 1);
		assertEquals(val2, stack.getAmount());

		// Make sure value does not change
		assertEquals(val, tableFormat.getColumnValue(stack, 0));

		// Create another stack identical to stack
		MaterialStack teststack = new MaterialStack(stack.getMaterial(),
				stack.getAmount());

		Object first = tableFormat.getColumnValue(stack, 0);
		Object second = tableFormat.getColumnValue(teststack, 0);

		// Make sure the value returned is the same for identical stacks.
		assertEquals(first, second);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.MaterialStackWritableTableFormat#isEditable(MaterialStack stack, int column)}
	 * .
	 */
	@Test
	public void testIsEditable() {

		// Make sure the value column is editable
		assertTrue(tableFormat.isEditable(stack2, 1));

		// Make sure the material name column is not editable
		assertTrue(!tableFormat.isEditable(stack, 0));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.materials.MaterialStackWritableTableFormat#setColumnValue(MaterialStack stack, Object newValue, int column)}
	 * .
	 */
	@Test
	public void testSetColumnValue() {
		Material Fe = new Material();
		Fe.setName("Iron");

		// Make sure the material name column is not changed.
		tableFormat.setColumnValue(stack2, Fe.getName(), 0);
		assertEquals(stack2.getMaterial().getName(), Ne.getName());

		// Testing setting a new value
		tableFormat.setColumnValue(stack2, 9, 1);
		assertEquals(9, stack2.getAmount());

		// Testing setting a new value as a String (likely what will happen in
		// tables)
		tableFormat.setColumnValue(stack, "1", 1);
		assertEquals(1, stack.getAmount());

		// Testing to make sure we cannot have a double as the amount for a
		// stack (should truncate decimal rather than round)
		tableFormat.setColumnValue(stack, 3.643, 1);
		assertEquals(3, stack.getAmount());

		// Make sure passing in weird parameters does not mess with values
		tableFormat.setColumnValue(stack2, new Material(), 1);
		assertEquals(stack2.getAmount(), 9);

		// Make sure a larger column value still sets the amount for the
		// material stack. Should never happen, but just in case.
		tableFormat.setColumnValue(stack, 4, 4);
		assertEquals(4, stack.getAmount());

	}

}
