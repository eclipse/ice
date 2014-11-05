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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.gef.EditPart;

/**
 * This class provides the "root" EditPart (hexagonal) for the {@link Grid}
 * model. It is responsible for adding a {@link CellEditPart} to the Grid. It
 * must also add row and column labels when necessary.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class HexagonalGridEditPart extends GridEditPart {

	/**
	 * The PointList directly modified by the HexagonalGridLayout. This will
	 * need to be passed to any HexagonCellFigures that are added to this
	 * GridEditPart.
	 */
	private PointList hexagonPoints;

	/**
	 * Creates the root Figure for the Grid. This is a FreeformLayer that
	 * contains a grid of labels and CellFigures.
	 */
	@Override
	protected IFigure createFigure() {

		// Initialize the root figure. We use a FreeformLayer. FIXME - This was
		// in a few tutorials. We should find some links to explain why this is
		// important.
		Figure rootFigure = new FreeformLayer();
		rootFigure.setBackgroundColor(ColorConstants.white);
		rootFigure.setOpaque(true);

		// Set the layout of the root figure to our custom layout.
		Grid grid = ((Grid) getModel());
		HexagonalGridLayout layout = new HexagonalGridLayout(grid.rows,
				grid.columns);
		rootFigure.setLayoutManager(layout);

		// Get the layout's hexagonal PointList.
		hexagonPoints = layout.getPoints();

		return rootFigure;
	}

	/**
	 * Adds the child's Figure to the {@link #getContentPane() contentPane}. In
	 * this case, the childEditPart is a CellEditPart.
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#addChildVisual(EditPart,
	 *      int)
	 */
	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		// Get the IFigure that we need to add from the childEditPart.
		HexagonalCellFigure cellFigure = (HexagonalCellFigure) ((CellEditPart) childEditPart)
				.getFigure();

		// Only proceed if there's a Figure to add.
		if (cellFigure == null) {
			return;
		}

		// Set the PointList used by the HexagonalCellFigure to the "global"
		// PointList that is used by the HexagonalGridLayout.
		cellFigure.setPoints(hexagonPoints);

		// Create a new GridData constraint for the IFigure.
		GridData gridData = new GridData(
				((Cell) childEditPart.getModel()).getIndex());

		// Add the IFigure to the content pane with the above constraint.
		getContentPane().add(cellFigure, gridData);

		return;
	}
}
