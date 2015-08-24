/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.reactoreditor.lwr;

import org.eclipse.gef.DragTracker;
import org.eclipse.gef.Request;
import org.eclipse.gef.requests.SelectionRequest;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell;
import org.eclipse.ice.client.widgets.reactoreditor.grid.CellEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.IGridListener;

/**
 * The CoreCellEditPart provides the EditPart for a Cell in a Grid. It is
 * instantiated by the {@link CoreEditPartFactory}.
 * 
 * @author Jordan
 * 
 */
public class CoreCellEditPart extends CellEditPart {
	/**
	 * An IGridListener that listens for Cell click/selection events. The
	 * listener needs to be notified when this EditPart's Figure is clicked and
	 * selected.
	 */
	private final IGridListener listener;

	/**
	 * The default constructor.
	 * 
	 * @param listener
	 *            An IGridListener that listens for Cell click/selection events.
	 */
	public CoreCellEditPart(IGridListener listener) {
		this.listener = listener;
	}

	/**
	 * This method notifies the View and Model of changes to the selection of
	 * the EditPart. It is overridden to provide updates to an
	 * {@link IGridListener}.
	 */
	@Override
	protected void fireSelectionChanged() {

		super.fireSelectionChanged();

		Cell model = (Cell) getModel();

		// Push an update to the broker if this Cell has been selected.
		if (model.getSelected()) {
			listener.selectCell(model.getIndex());
		}
		return;
	}

	/**
	 * This method returns a dragTracker to use for selection of EditParts. We
	 * override it here to provide selection of only one figure at a time.
	 */
	@Override
	public DragTracker getDragTracker(Request req) {

		DragTracker tracker = null;

		Cell model = (Cell) getModel();

		// Only set a tracker if the cell is enabled and we have a
		// SelectionRequest.
		// Only reply to left-clicks.
		if (!model.getDisabled() && req instanceof SelectionRequest
				&& ((SelectionRequest) req).getLastButtonPressed() == 1) {
			tracker = new SelectEditPartTracker(this);

			// The cell has been clicked!
			listener.clickCell(model.getIndex());
		}

		return tracker;
	}
}
