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

import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell.State;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * In GEF, editors need to be passed an IEditorInput. GridEditorInput provides
 * an implementation of that interface for the GridEditor.<br>
 * <br>
 * To provide the full user experience, the GridEditor uses the following
 * information:<br>
 * <br>
 * The number of rows and columns to display for the grid.<br>
 * Any labels for the rows and columns (default is the index+1).<br>
 * Any pre-selected locations/cells.<br>
 * Any invalid locations/cells.<br>
 * Any disabled locations/cells.<br>
 * <br>
 * Cells that are invalid, selected, or disabled should be passed in via a
 * Map<Integer, Cell.State>.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class GridEditorInput implements IEditorInput {

	// FIXME - We may want to do away with this class altogether and throw the
	// getters/setters into the Grid class. IEditorInput is needed when we have
	// a full-fledged GEF Editor, but we only have the GEF Viewer.

	/**
	 * The number of rows in the grid.
	 */
	private final int rows;
	/**
	 * The number of columns in the grid.
	 */
	private final int columns;

	/**
	 * A List of labels used for the rows. The default is the row index + 1.
	 */
	private List<String> rowLabels;
	/**
	 * A List of labels used for the columns. The default is the column index +
	 * 1.
	 */
	private List<String> columnLabels;
	/**
	 * A Map of Cell indexes to a specific State (invalid, disabled, selected).
	 * Unset indexes in the Map are assumed to be valid but unselected.
	 */
	private List<State> states;

	/**
	 * Whether or not to show the row labels.
	 */
	public boolean showRowLabels = true;

	/**
	 * Whether or not to show the column labels.
	 */
	public boolean showColumnLabels = true;

	/**
	 * Whether or not to show a label (containing the Cell's key) in each Cell.
	 */
	public boolean showCellLabels = true;

	/**
	 * The default constructor.
	 * 
	 * @param rows
	 *            The number of rows in the grid.
	 * @param columns
	 *            The number of columns in the grid.
	 */
	public GridEditorInput(int rows, int columns) {
		// Set the rows and columns to a minimum of at least 1.
		this.rows = (rows > 1 ? rows : 1);
		this.columns = (columns > 1 ? columns : 1);

		// Everything else remains un-initialized. When we need to return them,
		// they should be initialized if they were not set later.
		rowLabels = null;
		columnLabels = null;
		states = null;
	}

	/**
	 * Sets the array of row labels for the displayed grid. This method gets a
	 * <i>copy</i> of the provided List, so any modifications done afterward do
	 * not affect the input.
	 * 
	 * @param rowLabels
	 *            A new List of row labels. The array should be at least as big
	 *            as the number of rows in the grid.
	 */
	public void setRowLabels(List<String> rowLabels) {
		// Either reset the List or get a copy of the provided List. Make sure
		// the copy is not too big.
		if (rowLabels == null) {
			// Reset the List.
			this.rowLabels = null;
		} else if (rowLabels.size() > rows) {
			// Get a copy of the List that is the right size.
			this.rowLabels = new ArrayList<String>(rowLabels.subList(0, rows));
		} else {
			// Get a copy of the List.
			this.rowLabels = new ArrayList<String>(rowLabels);
		}

		return;
	}

	/**
	 * Sets the array of column labels for the displayed grid. This method gets
	 * a <i>copy</i> of the provided List, so any modifications done afterward
	 * do not affect the input.
	 * 
	 * @param columnLabels
	 *            A new List of column labels. The array should be at least as
	 *            big as the number of columns in the grid.
	 */
	public void setColumnLabels(List<String> columnLabels) {
		// Either reset the List or get a copy of the provided List. Make sure
		// the copy is not too big.
		if (columnLabels == null) {
			// Reset the List.
			this.columnLabels = null;
		} else if (columnLabels.size() > columns) {
			// Get a copy of the List that is the right size.
			this.columnLabels = new ArrayList<String>(columnLabels.subList(0,
					columns));
		} else {
			// Get a copy of the List.
			this.columnLabels = new ArrayList<String>(columnLabels);
		}

		return;
	}

	/**
	 * Sets the array of Cell states for the displayed grid. This method gets a
	 * <i>copy</i> of the provided List, so any modifications done afterward do
	 * not affect the input.
	 * 
	 * @param states
	 *            A new List of Cell states. The array should be at least as big
	 *            as the number of rows * columns in the grid.
	 */
	public void setStates(List<State> states) {
		// Either reset the List or get a copy of the provided List. Make sure
		// the copy is not too big.
		if (states == null) {
			// Reset the List.
			this.states = null;
		} else if (states.size() > rows * columns) {
			// Get a copy of the List that is the right size.
			this.states = new ArrayList<State>(
					states.subList(0, rows * columns));
		} else {
			// Get a copy of the List.
			this.states = new ArrayList<State>(states);
		}

		return;
	}

	/**
	 * Gets the number of rows that should be displayed in the editor.
	 * 
	 * @return The number of rows.
	 */
	public int getRows() {
		return rows;
	}

	/**
	 * Gets the number of columns that should be displayed in the editor.
	 * 
	 * @return The number of columns.
	 */
	public int getColumns() {
		return columns;
	}

	/**
	 * Gets the List of row labels for the editor. If no row labels were set,
	 * then this returns an array of Strings from "1" to "rows". If not enough
	 * labels were set, any unset labels are set to their default values.
	 * 
	 * @return A List of row label Strings. This List is a copy and so changes
	 *         from the input class will not affect this List.
	 */
	public List<String> getRowLabels() {

		// Make sure the label List is initialized.
		if (rowLabels == null) {
			rowLabels = new ArrayList<String>(rows);
		}
		// Clear the List of any null values (replace with default).
		for (int i = 0; i < rowLabels.size(); i++) {
			if (rowLabels.get(i) == null) {
				rowLabels.set(i, Integer.toString(i + 1));
			}
		}

		// Fill any remaining positions in the List with default values.
		for (int i = rowLabels.size(); i < rows; i++) {
			rowLabels.add(Integer.toString(i + 1));
		}
		// Return a copy of the List.
		return new ArrayList<String>(rowLabels);
	}

	/**
	 * Gets the List of column labels for the editor. If no column labels were
	 * set, then this returns an array of Strings from "1" to "rows". If not
	 * enough labels were set, any unset labels are set to their default values.
	 * 
	 * @return A List of column label Strings. This List is a copy and so
	 *         changes from the input class will not affect this List.
	 */
	public List<String> getColumnLabels() {

		// Make sure the label List is initialized.
		if (columnLabels == null) {
			columnLabels = new ArrayList<String>(columns);
		}
		// Clear the List of any null values (replace with default).
		for (int i = 0; i < columnLabels.size(); i++) {
			if (columnLabels.get(i) == null) {
				columnLabels.set(i, Integer.toString(i + 1));
			}
		}

		// Fill any remaining positions in the List with default values.
		for (int i = columnLabels.size(); i < columns; i++) {
			columnLabels.add(Integer.toString(i + 1));
		}
		// Return a copy of the List.
		return new ArrayList<String>(columnLabels);
	}

	/**
	 * Gets the List of Cell states for the editor. If no states were set, then
	 * this returns an array of states set to INVALID. If not enough states were
	 * set, any unset states are set to their default values (INVALID).
	 * 
	 * @return A List of Cell states. This List is a copy and so changes from
	 *         the input class will not affect this List.
	 */
	public List<State> getStates() {

		int size = rows * columns;

		// Make sure the List is initialized.
		if (states == null) {
			states = new ArrayList<State>(size);
		}
		// Clear the List of any null values (replace with default).
		for (int i = 0; i < states.size(); i++) {
			if (states.get(i) == null) {
				states.set(i, State.INVALID);
			}
		}

		// Fill any remaining positions in the List with default values.
		for (int i = states.size(); i < size; i++) {
			states.add(State.INVALID);
		}
		// Return a copy of the List.
		return new ArrayList<State>(states);
	}

	/* ----- Implements IEditorInput ----- */
	@Override
	public Object getAdapter(Class adapter) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	@Override
	public String getName() {
		return "GridEditorInput";
	}

	@Override
	public IPersistableElement getPersistable() {
		return null;
	}

	@Override
	public String getToolTipText() {
		return "Grid Editor";
	}
	/* ----------------------------------- */

}
