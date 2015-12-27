/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.grid.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.client.widgets.grid.Cell;
import org.eclipse.ice.client.widgets.grid.Cell.State;
import org.junit.Test;

public class CellTester {

	@Test
	public void checkAccessors() {
		Cell cell;

		// Test an unselected Cell.
		cell = new Cell(0, "key", State.UNSELECTED);
		assertNotNull(cell);
		assertEquals(0, cell.getIndex());
		assertEquals("key", cell.getKey());
		assertEquals(State.UNSELECTED, cell.getState());
		assertFalse(cell.getSelected());
		assertFalse(cell.getDisabled());
		assertFalse(cell.getInvalid());

		// Test (de)selecting the Cell.
		cell.setSelected(false);
		assertEquals(State.UNSELECTED, cell.getState());
		assertFalse(cell.getSelected());
		cell.setSelected(true);
		assertEquals(State.SELECTED, cell.getState());
		assertTrue(cell.getSelected());
		cell.setSelected(false);
		assertEquals(State.UNSELECTED, cell.getState());
		assertFalse(cell.getSelected());

		// Test a selected Cell.
		cell = new Cell(1, "selected", State.SELECTED);
		assertNotNull(cell);
		assertEquals(1, cell.getIndex());
		assertEquals("selected", cell.getKey());
		assertEquals(State.SELECTED, cell.getState());
		assertTrue(cell.getSelected());
		assertFalse(cell.getDisabled());
		assertFalse(cell.getInvalid());

		// Test (de)selecting the Cell.
		cell.setSelected(false);
		assertEquals(State.UNSELECTED, cell.getState());
		assertFalse(cell.getSelected());
		cell.setSelected(true);
		assertEquals(State.SELECTED, cell.getState());
		assertTrue(cell.getSelected());
		cell.setSelected(false);
		assertEquals(State.UNSELECTED, cell.getState());
		assertFalse(cell.getSelected());

		// Test a disabled Cell.
		cell = new Cell(2, "disabled", State.DISABLED);
		assertNotNull(cell);
		assertEquals(2, cell.getIndex());
		assertEquals("disabled", cell.getKey());
		assertEquals(State.DISABLED, cell.getState());
		assertFalse(cell.getSelected());
		assertTrue(cell.getDisabled());
		assertFalse(cell.getInvalid());

		// Selecting the Cell should do nothing.
		cell.setSelected(false);
		assertEquals(State.DISABLED, cell.getState());
		assertFalse(cell.getSelected());
		cell.setSelected(true);
		assertEquals(State.DISABLED, cell.getState());
		assertFalse(cell.getSelected());

		// Test an invalid Cell.
		cell = new Cell(3, "invalid", State.INVALID);
		assertNotNull(cell);
		assertEquals(3, cell.getIndex());
		assertEquals("invalid", cell.getKey());
		assertEquals(State.INVALID, cell.getState());
		assertFalse(cell.getSelected());
		assertFalse(cell.getDisabled());
		assertTrue(cell.getInvalid());

		// Selecting the Cell should do nothing.
		cell.setSelected(false);
		assertEquals(State.INVALID, cell.getState());
		assertFalse(cell.getSelected());
		cell.setSelected(true);
		assertEquals(State.INVALID, cell.getState());
		assertFalse(cell.getSelected());
	}
}
