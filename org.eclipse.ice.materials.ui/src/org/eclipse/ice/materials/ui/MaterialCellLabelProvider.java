/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.materials.ui;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

/**
 * This is a CellLabelProvider for Materials.
 * 
 * @author Jay Jay Billings
 *
 */
public class MaterialCellLabelProvider extends CellLabelProvider {

	/**
	 * The constructor
	 */
	public MaterialCellLabelProvider() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.
	 * viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		String text;
		// Switch on the column id and put the keys and values in the right
		// places.
		if (cell.getColumnIndex() == 0) {
			text = ((MaterialProperty) cell.getElement()).key;
		} else {
			text = ((MaterialProperty) cell.getElement()).value.toString();

		}
		cell.setText(text);
	}

}
