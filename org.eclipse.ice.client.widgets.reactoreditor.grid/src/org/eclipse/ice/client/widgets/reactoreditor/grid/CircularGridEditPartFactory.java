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

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

/**
 * This class connects GEF EditParts to the GridEditor's model so that the
 * appropriate EditParts can be created from the corresponding model. This is
 * for circular grids.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CircularGridEditPartFactory implements EditPartFactory {

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
			editPart = new CircularGridEditPart();
		} else if (model instanceof Cell) {
			editPart = new CircularCellEditPart();
		}

		if (editPart != null) {
			editPart.setModel(model);
		}
		return editPart;
	}

}