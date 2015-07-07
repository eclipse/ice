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
package org.eclipse.ice.sassena;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * This class builds the Sassena Incoherent Model Builder. It inherits its
 * operations from ItemBuilder.
 * 
 * @author Scott Forest Hull II
 */
public class SassenaIncoherentModelBuilder extends AbstractItemBuilder {

	/**
	 * The Constructor
	 */
	public SassenaIncoherentModelBuilder() {
		setName("Sassena Incoherent Model");
		setType(ItemType.Model);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item getInstance(IProject projectSpace) {
		SassenaIncoherentModel sassena = new SassenaIncoherentModel(
				projectSpace);
		sassena.setItemBuilderName(getItemName());
		return sassena;
	}

}
