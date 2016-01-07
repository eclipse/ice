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
package org.eclipse.ice.client.widgets.grid;

/**
 * This class provides the Model for a Cell in a {@link Grid}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class Cell {
	/**
	 * The index of the Cell in the Grid.
	 */
	private final int index;
	/**
	 * The key associated with the Cell (usually "row:column").
	 */
	private final String key;

	/**
	 * The current state of the Cell (selected, unselected, disabled, or
	 * invalid).
	 */
	private State state;

	/**
	 * This enumerates the possible states for a Cell to have.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	public enum State {
		/**
		 * The Cell is not selected.
		 */
		UNSELECTED,
		/**
		 * The Cell is selected.
		 */
		SELECTED,
		/**
		 * The Cell is disabled (grayed out).
		 */
		DISABLED,
		/**
		 * The Cell is invalid (invisible).
		 */
		INVALID;
	}

	/**
	 * The default constructor.
	 * 
	 * @param index
	 *            The index of the Cell in the Grid.
	 * @param key
	 *            The key associated with the Cell (usually "row:column").
	 * @param state
	 *            Whether or not the Cell is selected.
	 */
	public Cell(int index, String key, State state) {
		this.index = index;
		this.key = key;

		this.state = state;
	}

	/**
	 * Gets the index of the Cell in the Grid.
	 * 
	 * @return The Cell index.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the key associated with the Cell (usually "row:column").
	 * 
	 * @return The Cell key.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Gets whether or not the Cell is currently selected.
	 * 
	 * @return The Cell's selection.
	 */
	public boolean getSelected() {
		return (state == State.SELECTED);
	}

	/**
	 * Gets whether or not the Cell is disabled.
	 * 
	 * @return True if the Cell is disabled, false otherwise.
	 */
	public boolean getDisabled() {
		return (state == State.DISABLED);
	}

	/**
	 * Gets whether or not the Cell is invalid.
	 * 
	 * @return True if the Cell is invalid, false otherwise.
	 */
	public boolean getInvalid() {
		return (state == State.INVALID);
	}

	public void setState(State state) {
		this.state = state;
	}

	/**
	 * Sets whether or not the Cell is currently selected.
	 * 
	 * @param selected
	 *            The Cell's selection.
	 */
	public void setSelected(boolean selected) {
		// Set the state to SELECTED if selected is true, otherwise set it to
		// UNSELECTED. Do not make changes if the state is invalid or disabled.
		if (state != State.DISABLED && state != State.INVALID) {
			state = selected ? State.SELECTED : State.UNSELECTED;
		}
	}

	/**
	 * Gets the current state of the Cell.
	 * 
	 * @return The Cell's state.
	 */
	public State getState() {
		return state;
	}
}
