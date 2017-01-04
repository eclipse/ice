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

import org.eclipse.january.form.Material;
import org.eclipse.january.form.MaterialStack;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * A simple label provider for the Materials database.
 * 
 * @author Jay Jay Billings
 * 
 */
public class MaterialsDatabaseLabelProvider extends LabelProvider {

	/**
	 * This operation returns the text label for the Material represented by
	 * element or an error label saying "Wrong type detected!"
	 */
	@Override
	public String getText(Object element) {
		String label = "";
		// If the element is a material, just return the name of that material
		if (element instanceof Material) {
			label = ((Material) element).getName();
			
		// If the element is a material stack, return the material along with the amount in the stack in the form MaterialName (amount)
		} else if (element instanceof MaterialStack) {
			label = ((MaterialStack) element).getMaterial().getName();
			label += (" ("+Integer.toString(((MaterialStack) element).getAmount())+")");
		// Otherwise the element should not be contained in the table 
		} else {
			label = "Wrong type detected!";
		}
		return label;
	}

}
