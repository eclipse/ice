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
import org.eclipse.gef.EditPart;

/**
 * This class provides the "root" EditPart (circular) for the {@link Grid}
 * model. It is responsible for adding a {@link CellEditPart} to the Grid. It
 * must also add row and column labels when necessary.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CircularGridEditPart extends GridEditPart {

	/**
	 * Creates the root Figure for the Grid. This is a FreeformLayer that
	 * contains a grid of labels and CellFigures.
	 */
	@Override
	protected IFigure createFigure() {

		// Initialize the root figure. We use a FreeformLayer. FIXME - This was
		// in a few tutorials. We should find some links to explain why this is
		// important.
		Figure rootFigure = new FreeformLayer() {
			// FIXME - We may be able to use one of these methods to force
			// anti-aliasing. This would avoid having to override the paint
			// behavior of child Figures.
			// @Override
			// protected void paintClientArea(Graphics graphics) {
			// System.out.println("paintClientArea");
			// graphics.setAntialias(SWT.ON);
			// super.paintClientArea(graphics);
			// }
			// @Override
			// protected void paintFigure(Graphics graphics) {
			// System.out.println("paintFigure");
			// graphics.setAntialias(SWT.ON);
			// super.paintFigure(graphics);
			// }
			// @Override
			// protected void paintChildren(Graphics graphics) {
			// System.out.println("paintChildren");
			// graphics.setAntialias(SWT.ON);
			// super.paintChildren(graphics);
			// }
		};
		rootFigure.setBackgroundColor(ColorConstants.white);
		rootFigure.setOpaque(true);

		// Set the layout of the root figure to our custom layout.
		Grid grid = ((Grid) getModel());
		HexagonalGridLayout layout = new HexagonalGridLayout(grid.rows,
				grid.columns);
		rootFigure.setLayoutManager(layout);

		// We want the left and right sides of the circular cells to touch.
		layout.setRotated(true);

		// For the circular layout, we don't want the borders of the circular
		// cells to overlap.
		layout.setHorizontalSpacing(5);
		layout.setVerticalSpacing(5);
		// FIXME - The above spacings need to be more easily accessible!

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
		CircularCellFigure cellFigure = (CircularCellFigure) ((CellEditPart) childEditPart)
				.getFigure();

		// Only proceed if there's a Figure to add.
		if (cellFigure == null) {
			return;
		}

		// Create a new GridData constraint for the IFigure.
		GridData gridData = new GridData(
				((Cell) childEditPart.getModel()).getIndex());

		// Add the IFigure to the content pane with the above constraint.
		getContentPane().add(cellFigure, gridData);

		return;
	}
}
