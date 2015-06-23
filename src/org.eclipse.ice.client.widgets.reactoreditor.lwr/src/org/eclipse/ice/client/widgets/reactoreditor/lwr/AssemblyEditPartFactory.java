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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Grid;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditPart;
import org.eclipse.ice.client.widgets.reactoreditor.grid.IGridListener;

/**
 * This class connects GEF EditParts to the GridEditor's model so that the
 * appropriate EditParts can be created from the corresponding model. This is
 * for rectangular grids.
 * <p>
 * The AssemblyEditPartFactory uses {@link GridEditPart}s for the Grid and
 * {@link AssemblyCellEditPart}s for the Cells.
 * </p>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class AssemblyEditPartFactory implements EditPartFactory {

	/**
	 * An IGridListener that can listen for click/selection events. This needs
	 * to be passed to any EditParts corresponding to Cells that can be clicked.
	 */
	private final IGridListener listener;

	/**
	 * The default constructor.
	 * 
	 * @param listener
	 *            An IGridListener that listens for click/selection events for
	 *            the Cells.
	 */
	public AssemblyEditPartFactory(IGridListener listener) {
		this.listener = listener;
	}

	/**
	 * Overrides the default behavior to connect Grids and Cells to their
	 * corresponding EditParts (GridEditPart and CellEditPart).
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		// The code here is based off of the tutorials here:
		// http://www.eclipsecon.org/2008/?page=sub/&id=102
		// http://www.vainolo.com/2011/06/21/creating-a-gef-editor-%E2%80%93-part-4-showing-the-model-on-the-editor/

		EditPart editPart = null;
		if (model instanceof Grid) {
			editPart = new GridEditPart();
		} else if (model instanceof Cell) {
			editPart = new AssemblyCellEditPart(listener);
		}

		if (editPart != null) {
			editPart.setModel(model);
		}
		return editPart;
	}

}
