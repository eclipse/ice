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
package org.eclipse.ice.client.widgets.reactoreditor.sfr;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;

import org.eclipse.ice.client.widgets.reactoreditor.grid.HexagonalGridEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.HexagonalGridLayout;

/**
 * The AssemblyEditPart is used in the PinAssemblyAnalysisView as the Figure for
 * its Grid model. It contains various {@link PinAssemblyCellEditPart}s, which
 * represent the Cells in the Grid.
 * 
 * @author Jordan
 * 
 */
public class PinAssemblyGridEditPart extends HexagonalGridEditPart {

	/**
	 * Augments the createFigure() behavior to apply a rotated
	 * HexagonalGridLayout to the Figure.
	 */
	@Override
	protected IFigure createFigure() {
		// Get the layout from the root Figure created in the super method.
		IFigure rootFigure = super.createFigure();
		HexagonalGridLayout layout = (HexagonalGridLayout) rootFigure
				.getLayoutManager();

		// Rotate it and space out the components a little bit.
		layout.setRotated(true);
		layout.setHorizontalSpacing(5);
		layout.setVerticalSpacing(5);

		// Sets the background color of the figure. This makes the background
		// white for the entire assembly graphic.
		rootFigure.setBackgroundColor(ColorConstants.white);

		return rootFigure;
	}

}
