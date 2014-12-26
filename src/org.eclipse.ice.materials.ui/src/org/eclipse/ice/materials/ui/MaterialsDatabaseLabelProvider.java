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

import org.eclipse.ice.datastructures.form.Material;
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
		if (element instanceof Material) {
			return ((Material) element).getName();
		} else {
			return "Wrong type detected!";
		}
	}

}
