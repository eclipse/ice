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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.swt.graphics.Color;

/**
 * This class provides a default view for a {@link Cell} model. Each Cell can be
 * represented by a Figure. This Figure may contain a geometric Figure, e.g., a
 * RectangleFigure, or a Label, depending on the requirements.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CellFigure extends Figure {
	/**
	 * The color used for selected Cells is dark green.
	 */
	protected static final Color selectedColor = ColorConstants.darkGreen;
	/**
	 * The color used for unselected Cells is a light orange.
	 */
	protected static final Color unselectedColor = ColorConstants.orange;
	/**
	 * The color used for disabled Cells is light gray.
	 */
	protected static final Color disabledColor = ColorConstants.white;

	/**
	 * An IFigure representing this CellFigure's model Cell.
	 */
	protected IFigure figure = null;

	/**
	 * The default constructor for a CellFigure.
	 */
	public CellFigure() {
		// Set the default layout to a StackLayout.
		this.setLayoutManager(new StackLayout());

		return;
	}

	/**
	 * Gets the IFigure used to display useful information.
	 * 
	 * @return An IFigure that is, specifically, a RectangleFigure.
	 */
	public IFigure getFigure() {
		// Since the default CellFigure is a rectangle, create a new
		// RectangleFigure and add it to the base.
		if (figure == null) {

			// Create a new rectangle (RectangleFigure).
			figure = new RectangleFigure();

			// Add the rectangle to the CellFigure container.
			add(figure);

			// If we want to add a Label, do it here.
		}

		return figure;
	}

	/**
	 * Adds a label to the Figure showing the desired string, typically the Cell
	 * model's key.
	 * 
	 * @param key
	 *            The string to display on the label.
	 */
	public void addLabel(String key) {

		// Add a new label to this Figure.
		add(new Label(key));

		return;
	}

	/**
	 * Updates the CellFigure based on the provided Cell state.
	 * 
	 * @param state
	 *            The State of the model Cell (unselected, selected, disabled,
	 *            or invalid).
	 */
	public void setState(State state) {
		IFigure figure = getFigure();

		// Modify the Figure's color or visibility based on the Cell's state.
		if (state == State.SELECTED) {
			figure.setBackgroundColor(selectedColor);
		} else if (state == State.DISABLED) {
			figure.setBackgroundColor(disabledColor);
		} else if (state == State.INVALID) {
			figure.setVisible(false);
		} else {
			figure.setBackgroundColor(unselectedColor);
		}

		return;
	}

	/**
	 * Sets the color of the CellFigure based on its selection state.
	 * 
	 * @param selected
	 *            Whether or not the CellFigure is selected.
	 */
	public void setSelected(boolean selected) {
		IFigure figure = getFigure();

		// Set the color of the CellFigure based on whether or not it is
		// selected.
		if (selected) {
			figure.setBackgroundColor(selectedColor);
		} else {
			figure.setBackgroundColor(unselectedColor);
		}
		return;
	}
}
