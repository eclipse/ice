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

import org.eclipse.draw2d.IFigure;

/**
 * This class provides the Controller for a {@link Cell}. In particular, it is
 * responsible for View creation and EditPolicies for each Cell.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CircularCellEditPart extends CellEditPart {

	/**
	 * This method creates the View (CellFigure) for the corresponding part of
	 * the Model (Cell).
	 */
	@Override
	protected IFigure createFigure() {
		// Get the model.
		Cell cell = (Cell) getModel();

		// Construct a new figure for the model if possible.
		CircularCellFigure figure = null;
		if (cell.getState() != State.INVALID) {
			figure = new CircularCellFigure();
			figure.setState(cell.getState());
		}

		return figure;
	}
}
