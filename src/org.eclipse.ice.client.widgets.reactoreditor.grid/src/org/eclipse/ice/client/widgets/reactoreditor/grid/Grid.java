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
package org.eclipse.ice.client.widgets.reactoreditor.grid;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell.State;

/**
 * This class provides the model for a grid of Cells. The Grid should keep track
 * of Cells, dimensions, and selected, invalid, and disabled Cell locations.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class Grid {

	/**
	 * The number of rows in the grid.
	 */
	protected final int rows;

	/**
	 * The number of columns in the grid.
	 */
	protected final int columns;

	/**
	 * The list of Cells.
	 */
	private final List<Cell> cells;

	/**
	 * The array of row labels.
	 */
	private final List<String> rowLabels;

	/**
	 * The array of column labels.
	 */
	private final List<String> columnLabels;

	/**
	 * Whether or not to show the row labels.
	 */
	protected final boolean showRowLabels;

	/**
	 * Whether or not to show the column labels.
	 */
	protected final boolean showColumnLabels;

	/**
	 * Whether or not to show a label (containing the Cell's key) in each Cell.
	 */
	protected final boolean showCellLabels;

	/**
	 * The default constructor for a Grid model. Note that as input, it takes a
	 * GridEditorInput. Since GEF calls for creation of a GridEditorInput, we
	 * can use it here to check all input fed to this model.
	 * 
	 * @param input
	 *            A GridEditorInput used to validate the parameters for the
	 *            creation of a Grid model.
	 */
	public Grid(GridEditorInput input) {
		// Set the rows and columns.
		rows = input.getRows();
		columns = input.getColumns();

		// Store the row and column labels.
		rowLabels = input.getRowLabels();
		columnLabels = input.getColumnLabels();

		// Set whether or not to show the labels.
		showRowLabels = input.showRowLabels;
		showColumnLabels = input.showColumnLabels;
		showCellLabels = input.showCellLabels;

		// Get the Map of Cell states from the input.
		List<State> states = input.getStates();

		// Construct the list of Cell models.
		cells = new ArrayList<Cell>(rows * columns);

		// Variables used in the loop to create Cells.
		String rowLabel;
		String key;
		int index;
		State state;

		// Create each cell in the grid.
		for (int row = 0; row < rows; row++) {
			// Get the label for the current row.
			rowLabel = rowLabels.get(row);
			for (int column = 0; column < columns; column++) {
				// Get the index and key for the current Cell index.
				index = row * columns + column;
				key = columnLabels.get(column) + ":" + rowLabel;

				// Get the state of the cell from the input. If no value is set,
				// assume it is valid but unselected.
				state = states.get(index);
				if (state == null) {
					state = State.UNSELECTED;
				}
				// Add the Cell to the List.
				cells.add(new Cell(index, key, state));
			}
		}

		return;
	}

	/**
	 * Gets the list of Cells in the grid. This includes *all* grid locations.
	 * 
	 * @return An ArrayList of Cells.
	 */
	public List<Cell> getCells() {
		return cells;
	}

	/**
	 * Gets the number of rows in the grid.
	 * 
	 * @return The number of rows in the grid.
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Gets the number of columns in the grid.
	 * 
	 * @return The number of columns in the grid.
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Gets the row label for a particular row.
	 * 
	 * @param row
	 *            The index of the row in question.
	 * @return The label String for the row in question.
	 */
	public String getRowLabel(int row) {
		if (row < 0 || row >= rows) {
			return null;
		}
		return rowLabels.get(row);
	}

	/**
	 * Gets the column label for a particular column.
	 * 
	 * @param column
	 *            The index of the column in question.
	 * @return The label String for the column in question.
	 */
	public String getColumnLabel(int column) {
		if (column < 0 || column >= columns) {
			return null;
		}
		return columnLabels.get(column);
	}

	/**
	 * Gets the set of selected Cells in the grid.
	 * 
	 * @return A BitSet representing selected Cells in the grid.
	 */
	public BitSet getSelectedCells() {
		BitSet selected = new BitSet(rows * columns);
		for (int i = 0; i < rows * columns; i++) {
			if (cells.get(i).getSelected()) {
				selected.set(i);
			}
		}
		return selected;
	}
}
