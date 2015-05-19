/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.client.widgets.test;

import static org.junit.Assert.*;

import org.eclipse.ice.client.widgets.ListComponentColumnPropertyAccessor;
import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.odell.glazedlists.gui.WritableTableFormat;

/**
 * This class is responsible for testing
 * {@link org.eclipse.ice.client.widgets.ListComponentColumnPropertyAccessor}.
 * 
 * It tests the accessor class by faking out the WritableTableFormat used by the
 * ListComponent.
 * 
 * @author Jay Jay Billings
 *
 */
public class ListComponentColumnPropertyAccessorTester implements
		WritableTableFormat<Integer> {

	/**
	 * The component used for the tests
	 */
	private ListComponent<Integer> component;

	/**
	 * The accessor used for the tests
	 */
	private ListComponentColumnPropertyAccessor<Integer> accessor;

	/**
	 * The Integer value used for the tests
	 */
	private Integer value = 7;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		// Create the list and set the table format
		component = new ListComponent<Integer>();
		component.setTableFormat(this);
		// Create the accessor under test
		accessor = new ListComponentColumnPropertyAccessor<Integer>(component);
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.client.widgets.ListComponentColumnPropertyAccessor#getDataValue(java.lang.Object, int)}
	 * .
	 */
	@Test
	public void testGetDataValue() {
		assertEquals(getColumnValue(0, 1), accessor.getDataValue(0, 1));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.client.widgets.ListComponentColumnPropertyAccessor#setDataValue(java.lang.Object, int, java.lang.Object)}
	 * .
	 */
	@Test
	public void testSetDataValue() {
		// Check setting the value
		accessor.setDataValue(1, 0, 2);
		assertEquals(getColumnValue(1, 0), accessor.getDataValue(1, 0));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.client.widgets.ListComponentColumnPropertyAccessor#getColumnCount()}
	 * .
	 */
	@Test
	public void testGetColumnCount() {
		assertEquals(getColumnCount(), accessor.getColumnCount());
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.client.widgets.ListComponentColumnPropertyAccessor#getColumnProperty(int)}
	 * .
	 */
	@Test
	public void testGetColumnProperty() {
		assertEquals(getColumnName(0), accessor.getColumnProperty(0));
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.client.widgets.ListComponentColumnPropertyAccessor#getColumnIndex(java.lang.String)}
	 * .
	 */
	@Test
	public void testGetColumnIndex() {
		assertEquals(0,accessor.getColumnIndex(getColumnName(0)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.odell.glazedlists.gui.TableFormat#getColumnCount()
	 */
	@Override
	public int getColumnCount() {
		return 5;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ca.odell.glazedlists.gui.TableFormat#getColumnName(int)
	 */
	@Override
	public String getColumnName(int column) {
		return "Six";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.odell.glazedlists.gui.TableFormat#getColumnValue(java.lang.Object,
	 * int)
	 */
	@Override
	public Object getColumnValue(Integer baseObject, int column) {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.odell.glazedlists.gui.WritableTableFormat#isEditable(java.lang.Object,
	 * int)
	 */
	@Override
	public boolean isEditable(Integer baseObject, int column) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.odell.glazedlists.gui.WritableTableFormat#setColumnValue(java.lang
	 * .Object, java.lang.Object, int)
	 */
	@Override
	public Integer setColumnValue(Integer baseObject, Object editedValue,
			int column) {
		value = (Integer) editedValue;
		return value;
	}

}
