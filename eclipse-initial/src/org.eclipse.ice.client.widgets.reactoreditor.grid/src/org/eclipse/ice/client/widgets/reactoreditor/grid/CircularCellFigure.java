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

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * This class provides a circular view for a {@link Cell} model. This Figure
 * includes a draw2d Ellipse and can include a Label.
 * 
 * @author djg
 * 
 */
public class CircularCellFigure extends CellFigure {

	/**
	 * The default constructor for a CircularCellFigure.
	 */
	public CircularCellFigure() {
		super();

		return;
	}

	/**
	 * Gets the IFigure used to display useful information.
	 * 
	 * @return An IFigure that is, specifically, an Ellipse.
	 */
	@Override
	public IFigure getFigure() {
		// Since the default CellFigure is a rectangle, create a new
		// Ellipse and add it to the base.
		if (figure == null) {

			// Create a new circle (Ellipse).
			figure = new Ellipse() {
				@Override
				public void paintFigure(Graphics graphics) {

					// Force anti-aliasing when drawing this Ellipse.
					graphics.setAntialias(SWT.ON);

					// Continue with the default procedure.
					super.paintFigure(graphics);
				}

				@Override
				public void setBounds(Rectangle bounds) {

					// Force square bounds to make the Ellipse a circle.
					Rectangle limit = CircularCellFigure.this.getClientArea();
					if (limit.width > limit.height) {
						bounds.width = bounds.height;
					} else {
						bounds.height = bounds.width;
					}

					// Continue with the default procedure.
					super.setBounds(bounds);
				}
			};

			// Add the Ellipse to the CellFigure container.
			add(figure);

			// If we want to add a Label, do it here.
		}

		return figure;
	}
}
