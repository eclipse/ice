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

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonShape;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.swt.SWT;

/**
 * This class provides a hexagonal view for a {@link Cell} model. This Figure
 * includes a draw2d PolygonShape with a PointList of size 6 and can include a
 * Label.
 * 
 * @author djg
 * 
 */
public class HexagonalCellFigure extends CellFigure {

	/**
	 * The default constructor for a CircularCellFigure.
	 */
	public HexagonalCellFigure() {
		super();

		return;
	}

	/**
	 * Gets the IFigure used to display useful information.
	 * 
	 * @return An IFigure that is, specifically, a PolygonShape with 6 points.
	 */
	@Override
	public IFigure getFigure() {
		// Since the default CellFigure is a rectangle, create a new
		// PolygonShape with 6 points and add it to the base.
		if (figure == null) {

			// Create a new hexagon (PolygonShape).
			figure = new PolygonShape() {
				@Override
				public void paintFigure(Graphics graphics) {

					// Force anti-aliasing when drawing this Ellipse.
					graphics.setAntialias(SWT.ON);

					// Continue with the default procedure.
					super.paintFigure(graphics);
				}
			};

			// Add 6 points to the shape. This "officially" makes it a hexagon.
			((PolygonShape) figure).setPoints(new PointList(6));

			// Add the hexagon to the CellFigure container.
			add(figure);

			// If we want to add a Label, do it here.
		}

		return figure;
	}

	/**
	 * Sets the PointList used to determine the bounds of the hexagonal
	 * CellFigure.
	 * 
	 * @param points
	 *            A PointList that should contain exactly 6 points.
	 */
	public void setPoints(PointList points) {
		((PolygonShape) figure).setPoints(points);
	}
}
