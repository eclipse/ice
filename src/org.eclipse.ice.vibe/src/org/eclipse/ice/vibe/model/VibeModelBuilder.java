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
package org.eclipse.ice.vibe.model;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * This class builds and manages the VibeModel. It inherits its operations from
 * ItemBuilder.
 * 
 * @author Scott Forest Hull II, Andrew Bennett
 */
public class VibeModelBuilder implements ItemBuilder {
	/*
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public String getItemName() {
		return "VIBE Model";
	}

	/*
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public ItemType getItemType() {
		return ItemType.Model;
	}

	/*
	 * Implements a method from ItemBuilder.
	 */
	@Override
	public Item build(IProject projectSpace) {

		// Create a model
		Item item = new VibeModel(projectSpace);

		// Set the item builder's name
		item.setItemBuilderName(getItemName());

		// Return the item
		return item;
	}
}