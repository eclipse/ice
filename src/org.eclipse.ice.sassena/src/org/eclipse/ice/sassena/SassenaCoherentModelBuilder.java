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
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * <p>
 * This class builds the SassenaCoherentModel. It inherits its operations
 * from ItemBuilder.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class SassenaCoherentModelBuilder implements ItemBuilder {

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemName()
	 */
	@Override
	public String getItemName() {
		return "Sassena Coherent Model";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemType()
	 */
	@Override
	public ItemType getItemType() {
		return ItemType.Model;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#build(Interface projectSpace)
	 */
	@Override
	public Item build(IProject projectSpace) {
		SassenaCoherentModel sassenaCoh = new SassenaCoherentModel(projectSpace);
		sassenaCoh.setItemBuilderName(getItemName());
		return sassenaCoh;
	}

}
