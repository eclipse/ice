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

import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell.State;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.DeselectAllTracker;
import org.eclipse.gef.tools.MarqueeDragTracker;

/**
 * This class provides the Controller for a {@link Cell}. In particular, it is
 * responsible for View creation and EditPolicies for each Cell.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CellEditPart extends AbstractGraphicalEditPart {

	/**
	 * This drag tracker is used to perform multi-selection in the editor. See
	 * the class below.
	 */
	private static final MarqueeDragTracker marquee = new MyMarquee();

	// FIXME - Do we really need this method? It's only used in the SFR core
	// assembly view.
	public void setState(State state) {
		if (state != null) {
			((Cell) getModel()).setState(state);
			((CellFigure) getFigure()).setState(state);
		}
		return;
	}

	/**
	 * Adds a label to the CellFigure. The text displayed will be the Cell
	 * model's key.
	 */
	public void showLabel() {

		// Get the model and view for the Cell.
		Cell cell = (Cell) getModel();
		CellFigure figure = (CellFigure) getFigure();

		// Tell the view to add the Cell key as the label.
		figure.addLabel(cell.getKey());

		return;
	}

	/**
	 * This method creates the View (CellFigure) for the corresponding part of
	 * the Model (Cell).
	 */
	@Override
	protected IFigure createFigure() {
		// Get the model.
		Cell cell = (Cell) getModel();

		// Construct a new figure for the model if possible.
		CellFigure figure = null;
		if (cell.getState() != State.INVALID) {
			figure = new CellFigure();
			figure.setState(cell.getState());
		}

		return figure;
	}

	/**
	 * This method sets up an EditPolicy to enable selection of EditParts.
	 */
	@Override
	protected void createEditPolicies() {
		// This selection policy was making me get null pointers! And it messed
		// with the selection tool.

		// Get the model.
		Cell cell = (Cell) getModel();

		// If the Cell is invalid or disabled, we don't want to edit it.
		if (cell.getInvalid() || cell.getDisabled()) {
			return;
		}

		// This EditPolicy seems to work well.
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, null);

		return;
	}

	/**
	 * This method notifies the View and Model of changes to the selection of
	 * the EditPart.
	 */
	@Override
	protected void fireSelectionChanged() {
		// If the Cell is invalid or disabled, we don't want to select it.
		Cell cell = (Cell) getModel();
		if (cell.getInvalid() || cell.getDisabled()) {
			return;
		}

		// Because the SELECTED_PRIMARY flag is a bit unpredictable, we consider
		// something selected as long as it's not receiving the SELECTED_NONE
		// flag.
		boolean selected = (getSelected() != EditPart.SELECTED_NONE);

		// Tell the figure and the model the selection choice.
		((CellFigure) getFigure()).setSelected(selected);
		((Cell) getModel()).setSelected(selected);

		// Call the same method for the super.
		super.fireSelectionChanged();

		return;
	}

	/**
	 * This method returns a dragTracker to use for multi-selection of
	 * EditParts.
	 */
	@Override
	public DragTracker getDragTracker(Request req) {
		// XXX Important notes here!
		//
		// The dragTracker is created from the very first EditPart that is
		// clicked during a selection. We can use this method to notify the
		// corresponding model for this EditPart that it was the first component
		// selected.
		//
		// Left mouse button = 1
		// Middle mouse button = 2
		// Right mouse button = 3
		//
		// The current behavior here:
		//
		// If the user left-clicks, return a marquee (drag) selection. The
		// marquee actually toggles everything inside its bounds, so
		// previously-selected nodes will be de-selected.
		//
		// If the user middle-clicks, nothing happens.
		//
		// If the user right-clicks, all nodes are deselected.

		// Process right-clicks differently.
		if (req instanceof SelectionRequest
				&& ((SelectionRequest) req).getLastButtonPressed() == 3) {
			return new DeselectAllTracker(this);
		}
		// Return a custom marquee tracker here for left clicks.
		return marquee;
	}

	/**
	 * The only customization currently implemented for this dragTracker is to
	 * toggle all EditParts that are touched by its bounding box when the mouse
	 * button is released. The default behavior which we overrode here is to
	 * only toggle EditParts within the tracker's bounding box.
	 * 
	 * @author Jordan H. Deyton
	 * 
	 */
	public static class MyMarquee extends MarqueeDragTracker {
		public MyMarquee() {
			super();

			// All nodes touched by the tracker should be included in the
			// selection.
			setMarqueeBehavior(BEHAVIOR_NODES_TOUCHED);
		}
	}
}
