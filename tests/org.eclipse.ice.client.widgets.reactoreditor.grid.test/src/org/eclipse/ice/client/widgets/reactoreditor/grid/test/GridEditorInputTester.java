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
package org.eclipse.ice.client.widgets.reactoreditor.grid.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell.State;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditorInput;
import org.junit.Test;

public class GridEditorInputTester {
	private final int rows = 10;
	private final int columns = rows;
	private GridEditorInput input = new GridEditorInput(rows, columns);

	/**
	 * Tests the constructors for the GridEditorInput.
	 */
	@Test
	public void checkConstructors() {
		// GridEditorInput should force all row/column values to be integers
		// greater than or equal to 1.

		GridEditorInput input;

		// Give valid rows/columns.
		input = new GridEditorInput(rows, columns);
		assertEquals(rows, input.getRows());
		assertEquals(columns, input.getColumns());

		// Give invalid rows (non-positive value).
		input = new GridEditorInput(0, 17);
		assertEquals(1, input.getRows());
		assertEquals(17, input.getColumns());

		// Give invalid columns (non-positive value).
		input = new GridEditorInput(10, -5);
		assertEquals(10, input.getRows());
		assertEquals(1, input.getColumns());

		// Give both invalid values (non-positive values).
		input = new GridEditorInput(Integer.MIN_VALUE, Integer.MIN_VALUE);
		assertEquals(1, input.getRows());
		assertEquals(1, input.getColumns());

		// Give MAX value.
		input = new GridEditorInput(Integer.MAX_VALUE, Integer.MAX_VALUE);
		assertEquals(Integer.MAX_VALUE, input.getRows());
		assertEquals(Integer.MAX_VALUE, input.getColumns());

		return;
	}

	/**
	 * Checks the results of setting the row labels in GridEditorInput.
	 */
	@Test
	public void checkRowLabels() {
		List<String> labels;
		List<String> inputLabels;

		int i;

		/* ----- Check default row labels. ----- */
		List<String> rowLabels = input.getRowLabels();

		// The returned list should not be null.
		assertNotNull(rowLabels);

		// The returned list should be of size rows.
		assertEquals(rows, rowLabels.size());

		// The row labels should, by default, return a list of number strings
		// starting from 1 (not 0!).
		for (i = 0; i < rows; i++) {
			assertEquals(Integer.toString(i + 1), rowLabels.get(i));
		}
		/* ------------------------------------- */

		/* ----- Check row labels (properly-sized list input). ----- */
		// Make a list of test labels.
		labels = new ArrayList<String>(rows);
		for (i = 0; i < rows; i++) {
			labels.add("Row Label " + Integer.toString(i));
		}

		// Set the row labels for the input.
		input.setRowLabels(labels);

		// Change a value in the original list.
		labels.set(0, "Zero");

		// Get the row labels from the input.
		inputLabels = input.getRowLabels();

		// The returned list should not be null.
		assertNotNull(inputLabels);

		// The returned list should be of size rows.
		assertEquals(rows, inputLabels.size());

		// Validate that the returned list is NOT the same list.
		assertNotSame(labels, inputLabels);

		// Validate all values in the inputLabels list to make sure nothing
		// changed.
		for (i = 0; i < inputLabels.size(); i++) {
			assertEquals("Row Label " + Integer.toString(i), inputLabels.get(i));
		}

		// Validate all values in the labels list to make sure they were not
		// modified.
		assertEquals("Zero", labels.get(0));
		for (i = 1; i < labels.size(); i++) {
			assertEquals("Row Label " + Integer.toString(i), labels.get(i));
		}
		/* --------------------------------------------------------- */

		/* ----- Check row labels (null input). ----- */
		// Set the row labels for the input.
		input.setRowLabels(null);

		// Get the row labels from the input.
		inputLabels = input.getRowLabels();

		// The returned list should not be null.
		assertNotNull(inputLabels);

		// The returned list should be of size rows.
		assertEquals(rows, inputLabels.size());

		// Validate all values in the inputLabels list to make sure nothing
		// changed.
		for (i = 0; i < inputLabels.size(); i++) {
			assertEquals(Integer.toString(i + 1), inputLabels.get(i));
		}
		/* --------------------------------------------------------- */

		/* ----- Check row labels (bigger list input). ----- */
		// Restore the changed value in the original list.
		labels.set(0, "Row Label 0");

		// Add an additional label so that we have a rows + 1 sized list.
		labels.add("Row Label " + Integer.toString(rows));

		// Set the row labels for the input.
		input.setRowLabels(labels);

		// Get the row labels from the input.
		inputLabels = input.getRowLabels();

		// The returned list should not be null.
		assertNotNull(inputLabels);

		// The returned list should be of size rows.
		assertEquals(rows, inputLabels.size());

		// Validate that the returned list is NOT the same list.
		assertNotSame(labels, inputLabels);

		// Validate all values in the inputLabels list to make sure nothing
		// changed.
		for (i = 0; i < inputLabels.size(); i++) {
			assertEquals("Row Label " + Integer.toString(i), inputLabels.get(i));
		}

		// Validate all values in the labels list to make sure they were not
		// modified.
		for (i = 0; i < labels.size(); i++) {
			assertEquals("Row Label " + Integer.toString(i), labels.get(i));
		}
		/* --------------------------------------------------------- */

		/* ----- Check row labels (smaller list input). ----- */
		// Restore the changed value in the original list.
		labels.remove(rows);
		labels.remove(rows - 1);

		// Set the row labels for the input.
		input.setRowLabels(labels);

		// Get the row labels from the input.
		inputLabels = input.getRowLabels();

		// The returned list should not be null.
		assertNotNull(inputLabels);

		// The returned list should be of size rows.
		assertEquals(rows, inputLabels.size());

		// Validate that the returned list is NOT the same list.
		assertNotSame(labels, inputLabels);

		// Validate all values in the inputLabels list to make sure nothing
		// changed.
		for (i = 0; i < labels.size(); i++) {
			assertEquals("Row Label " + Integer.toString(i), inputLabels.get(i));
		}
		// The last labels should be default labels.
		for (i = labels.size(); i < inputLabels.size(); i++) {
			assertEquals(Integer.toString(i + 1), inputLabels.get(i));
		}
		// Validate all values in the labels list to make sure they were not
		// modified.
		for (i = 0; i < labels.size(); i++) {
			assertEquals("Row Label " + Integer.toString(i), labels.get(i));
		}
		/* --------------------------------------------------------- */

		return;
	}

	/**
	 * Checks the results of setting the column labels in GridEditorInput.
	 */
	@Test
	public void checkColumnLabels() {
		List<String> labels;
		List<String> inputLabels;

		int i;

		/* ----- Check default column labels. ----- */
		List<String> columnLabels = input.getColumnLabels();

		// The returned list should not be null.
		assertNotNull(columnLabels);

		// The returned list should be of size columns.
		assertEquals(columns, columnLabels.size());

		// The column labels should, by default, return a list of number strings
		// starting from 1 (not 0!).
		for (i = 0; i < columns; i++) {
			assertEquals(Integer.toString(i + 1), columnLabels.get(i));
		}
		/* ------------------------------------- */

		/* ----- Check column labels (properly-sized list input). ----- */
		// Make a list of test labels.
		labels = new ArrayList<String>(columns);
		for (i = 0; i < columns; i++) {
			labels.add("Column Label " + Integer.toString(i));
		}

		// Set the column labels for the input.
		input.setColumnLabels(labels);

		// Change a value in the original list.
		labels.set(0, "Zero");

		// Get the column labels from the input.
		inputLabels = input.getColumnLabels();

		// The returned list should not be null.
		assertNotNull(inputLabels);

		// The returned list should be of size columns.
		assertEquals(columns, inputLabels.size());

		// Validate that the returned list is NOT the same list.
		assertNotSame(labels, inputLabels);

		// Validate all values in the inputLabels list to make sure nothing
		// changed.
		for (i = 0; i < inputLabels.size(); i++) {
			assertEquals("Column Label " + Integer.toString(i),
					inputLabels.get(i));
		}

		// Validate all values in the labels list to make sure they were not
		// modified.
		assertEquals("Zero", labels.get(0));
		for (i = 1; i < labels.size(); i++) {
			assertEquals("Column Label " + Integer.toString(i), labels.get(i));
		}
		/* --------------------------------------------------------- */

		/* ----- Check column labels (null input). ----- */
		// Set the column labels for the input.
		input.setColumnLabels(null);

		// Get the column labels from the input.
		inputLabels = input.getColumnLabels();

		// The returned list should not be null.
		assertNotNull(inputLabels);

		// The returned list should be of size columns.
		assertEquals(columns, inputLabels.size());

		// Validate all values in the inputLabels list to make sure nothing
		// changed.
		for (i = 0; i < inputLabels.size(); i++) {
			assertEquals(Integer.toString(i + 1), inputLabels.get(i));
		}
		/* --------------------------------------------------------- */

		/* ----- Check column labels (bigger list input). ----- */
		// Restore the changed value in the original list.
		labels.set(0, "Column Label 0");

		// Add an additional label so that we have a columns + 1 sized list.
		labels.add("Column Label " + Integer.toString(columns));

		// Set the column labels for the input.
		input.setColumnLabels(labels);

		// Get the column labels from the input.
		inputLabels = input.getColumnLabels();

		// The returned list should not be null.
		assertNotNull(inputLabels);

		// The returned list should be of size columns.
		assertEquals(columns, inputLabels.size());

		// Validate that the returned list is NOT the same list.
		assertNotSame(labels, inputLabels);

		// Validate all values in the inputLabels list to make sure nothing
		// changed.
		for (i = 0; i < inputLabels.size(); i++) {
			assertEquals("Column Label " + Integer.toString(i),
					inputLabels.get(i));
		}

		// Validate all values in the labels list to make sure they were not
		// modified.
		for (i = 0; i < labels.size(); i++) {
			assertEquals("Column Label " + Integer.toString(i), labels.get(i));
		}
		/* --------------------------------------------------------- */

		/* ----- Check column labels (smaller list input). ----- */
		// Restore the changed value in the original list.
		labels.remove(columns);
		labels.remove(columns - 1);

		// Set the column labels for the input.
		input.setColumnLabels(labels);

		// Get the column labels from the input.
		inputLabels = input.getColumnLabels();

		// The returned list should not be null.
		assertNotNull(inputLabels);

		// The returned list should be of size columns.
		assertEquals(columns, inputLabels.size());

		// Validate that the returned list is NOT the same list.
		assertNotSame(labels, inputLabels);

		// Validate all values in the inputLabels list to make sure nothing
		// changed.
		for (i = 0; i < labels.size(); i++) {
			assertEquals("Column Label " + Integer.toString(i),
					inputLabels.get(i));
		}
		// The last labels should be default labels.
		for (i = labels.size(); i < inputLabels.size(); i++) {
			assertEquals(Integer.toString(i + 1), inputLabels.get(i));
		}

		// Validate all values in the labels list to make sure they were not
		// modified.
		for (i = 0; i < labels.size(); i++) {
			assertEquals("Column Label " + Integer.toString(i), labels.get(i));
		}
		/* --------------------------------------------------------- */

		return;
	}

	/**
	 * Checks the results of setting the Cell states in GridEditorInput.
	 */
	@Test
	public void checkStates() {
		List<State> states;
		List<State> inputStates;
		int i;

		int size = rows * columns;

		/* ----- Check default List of Cell states. ----- */
		inputStates = input.getStates();

		// The returned List should not be null.
		assertNotNull(inputStates);

		// The returned List should be populated with INVALID.
		assertEquals(size, inputStates.size());
		for (i = 0; i < size; i++) {
			assertEquals(State.INVALID, inputStates.get(i));
		}
		/* --------------------------------------------- */

		/* ----- Check valid List of States. ----- */
		states = new ArrayList<State>(9);
		for (i = 0; i < 9; i++) {
			states.add(null);
		}

		// Put in some values.
		states.set(0, State.DISABLED);
		states.set(1, State.DISABLED);
		states.set(1, State.INVALID);
		states.set(2, State.SELECTED);
		states.set(3, State.UNSELECTED);
		states.set(5, State.SELECTED);
		states.set(8, State.INVALID);

		// Set the GridEditorInput's state List.
		input.setStates(states);

		// Get the state List from the input.
		inputStates = input.getStates();

		// The returned List should not be null.
		assertNotNull(inputStates);

		// The returned List should not be empty!
		assertEquals(size, inputStates.size());

		// Validate that the returned List is NOT the same List.
		assertNotSame(states, inputStates);

		// Make sure the old List was not modified.
		assertEquals(State.DISABLED, states.get(0));
		assertEquals(State.INVALID, states.get(1));
		assertEquals(State.SELECTED, states.get(2));
		assertEquals(State.UNSELECTED, states.get(3));
		assertNull(states.get(4));
		assertEquals(State.SELECTED, states.get(5));
		assertNull(states.get(6));
		assertNull(states.get(7));
		assertEquals(State.INVALID, states.get(8));

		// Change a value in the first List.
		states.set(0, State.INVALID);

		// Make sure the returned List has the same initial values.
		assertEquals(State.DISABLED, inputStates.get(0));
		assertEquals(State.INVALID, inputStates.get(1));
		assertEquals(State.SELECTED, inputStates.get(2));
		assertEquals(State.UNSELECTED, inputStates.get(3));
		assertEquals(State.SELECTED, inputStates.get(5));
		assertEquals(State.INVALID, inputStates.get(8));

		// Make sure the rest of the returned List is INVALID.
		for (i = 9; i < size; i++) {
			assertEquals(State.INVALID, inputStates.get(i));
		}
		/* ------------------------------------------ */

		/* ----- Check full List of States. ----- */
		// Fill the List a value for every cell.
		states = new ArrayList<State>(size);
		for (i = 0; i < size; i++) {
			states.add(State.UNSELECTED);
		}

		// Set the GridEditorInput's state List.
		input.setStates(states);

		// Get the state List from the input.
		inputStates = input.getStates();

		// The returned List should not be null.
		assertNotNull(inputStates);

		// The returned List should not be empty!
		assertEquals(size, inputStates.size());

		// Validate that the returned List is NOT the same List.
		assertNotSame(states, inputStates);

		// Make sure the old List was not modified.
		for (i = 0; i < size; i++) {
			assertEquals(State.UNSELECTED, states.get(i));
		}

		// Make sure the returned List has the same initial values.
		for (i = 0; i < size; i++) {
			assertEquals(State.UNSELECTED, inputStates.get(i));
		}
		/* ----------------------------------------- */

		/* ----- Check List with invalid indexes. ----- */
		// Put in some invalid cell indexes.
		states.add(State.DISABLED);
		states.add(State.SELECTED);

		// Set the GridEditorInput's state List.
		input.setStates(states);

		// Get the state List from the input.
		inputStates = input.getStates();

		// The returned List should not be null.
		assertNotNull(inputStates);

		// The returned List should not be empty!
		assertEquals(size, inputStates.size());

		// Validate that the returned List is NOT the same List.
		assertNotSame(states, inputStates);

		// Make sure the old List was not modified.
		for (i = 0; i < size; i++) {
			assertEquals(State.UNSELECTED, states.get(i));
		}
		assertEquals(State.DISABLED, states.get(i++));
		assertEquals(State.SELECTED, states.get(i));

		// Make sure the returned List has the same initial values.
		for (i = 0; i < size; i++) {
			assertEquals(State.UNSELECTED, inputStates.get(i));
		}
		/* ----------------------------------------------- */

		/* ----- Check default List of Cell states when null passed. ----- */
		// Reset the state List in the input.
		input.setStates(null);

		// Get the state List from the input.
		inputStates = input.getStates();

		// The returned List should not be null.
		assertNotNull(inputStates);

		// The returned List should be populated with INVALID.
		for (i = 0; i < size; i++) {
			assertEquals(State.INVALID, inputStates.get(i));
		}
		/* -------------------------------------------------------------- */

		return;
	}
}
