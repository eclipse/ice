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
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.widgets.grid.Cell;
import org.eclipse.ice.client.widgets.grid.Grid;
import org.eclipse.ice.client.widgets.grid.GridEditorInput;
import org.eclipse.ice.client.widgets.grid.Cell.State;
import org.junit.Test;

public class GridTester {

	private final int rows = 10;
	private final int columns = rows;

	/**
	 * This method checks the Grid model when constructed with a default
	 * GridEditorInput.
	 */
	@Test
	public void checkDefaults() {
		// We need to create a GridEditorInput for the Grid model first.
		GridEditorInput input = new GridEditorInput(rows, columns);

		// Construct a new Grid model based on the default input.
		Grid grid = new Grid(input);

		// The Grid should not be null!
		assertNotNull(grid);

		/* ----- Check the rows and columns for the Grid. ----- */
		assertEquals(rows, grid.getRows());
		assertEquals(columns, grid.getColumns());
		/* ---------------------------------------------------- */

		/* ----- Check the row and column labels for the Grid. ----- */
		for (int i = 0; i < rows; i++) {
			assertEquals(Integer.toString(i + 1), grid.getRowLabel(i));
		}
		for (int i = 0; i < columns; i++) {
			assertEquals(Integer.toString(i + 1), grid.getColumnLabel(i));
		}

		// Try to access some invalid indexes for row/column labels.
		assertEquals(null, grid.getRowLabel(-1));
		assertEquals(null, grid.getRowLabel(Integer.MAX_VALUE));
		assertEquals(null, grid.getColumnLabel(Integer.MIN_VALUE));
		assertEquals(null, grid.getColumnLabel(columns));
		/* --------------------------------------------------------- */

		/* ----- Check the List of Cells in the Grid. ----- */
		// Get the List of Cells.
		ArrayList<Cell> cells = (ArrayList<Cell>) grid.getCells();

		// The List of Cells should not be null!
		assertNotNull(cells);

		// It should have rows * columns Cells.
		assertEquals(rows * columns, cells.size());
		/* ------------------------------------------------ */

		return;
	}

	/**
	 * This method checks the Grid model when constructed with a GridEditorInput
	 * that has non-default labels, states, etc.
	 */
	@Test
	public void checkNonDefaults() {
		int i;

		// Construct a non-default GridEditorInput.
		GridEditorInput input = new GridEditorInput(rows, columns);

		// Make a List of row labels from 0 to rows.
		List<String> rowLabels = new ArrayList<String>(rows);
		for (i = 0; i < rows; i++) {
			rowLabels.add(Integer.toString(i));
		}

		// Make a List of column labels from A to A + columns.
		List<String> columnLabels = new ArrayList<String>(columns);
		for (i = 0; i < columns; i++) {
			columnLabels.add(Character.toString((char) ('A' + i)));
		}

		// Make a valid Map of Cell states.
		List<State> states = new ArrayList<State>();
		states.add(State.INVALID);
		states.add(State.DISABLED);
		states.add(State.SELECTED);
		states.add(State.UNSELECTED);

		// Set the labels and states for the input.
		input.setRowLabels(rowLabels);
		input.setColumnLabels(columnLabels);
		input.setStates(states);

		// Construct the Grid from the input.
		Grid grid = new Grid(input);

		// The Grid should not be null!
		assertNotNull(grid);

		/* ----- Check the rows and columns for the Grid. ----- */
		assertEquals(rows, grid.getRows());
		assertEquals(columns, grid.getColumns());
		/* ---------------------------------------------------- */

		/* ----- Check the row and column labels for the Grid. ----- */
		for (i = 0; i < rows; i++) {
			assertEquals(Integer.toString(i), grid.getRowLabel(i));
		}
		for (i = 0; i < columns; i++) {
			assertEquals(Character.toString((char) ('A' + i)),
					grid.getColumnLabel(i));
		}

		// Try to access some invalid indexes for row/column labels.
		assertEquals(null, grid.getRowLabel(-1));
		assertEquals(null, grid.getRowLabel(Integer.MAX_VALUE));
		assertEquals(null, grid.getColumnLabel(Integer.MIN_VALUE));
		assertEquals(null, grid.getColumnLabel(columns));
		/* --------------------------------------------------------- */

		/* ----- Check the List of Cells in the Grid. ----- */
		// Get the List of Cells.
		ArrayList<Cell> cells = (ArrayList<Cell>) grid.getCells();

		// The List of Cells should not be null!
		assertNotNull(cells);

		// It should have rows * columns - 1 Cells.
		// Invalid Cells (like Cell 0) are not kept in the List!!!
		assertEquals(rows * columns, cells.size());

		// Check the Cells and States we set before.
		assertEquals(State.INVALID, cells.get(0).getState());
		assertEquals(State.DISABLED, cells.get(1).getState());
		assertEquals(State.SELECTED, cells.get(2).getState());
		assertEquals(State.UNSELECTED, cells.get(3).getState());

		// All the other Cells should have UNSELECTED as their state.
		for (i = 4; i < rows * columns; i++) {
			Cell cell = cells.get(i);
			assertEquals(i, cell.getIndex());
			assertEquals(State.INVALID, cell.getState());
		}
		/* ------------------------------------------------ */

		return;
	}

}
