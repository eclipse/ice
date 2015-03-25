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
 * <p>
 * This class builds and manages the VibeModel. It inherits its operations
 * from ItemBuilder.
 * </p>
 * 
 * @author s4h, Andrew Bennett
 */
public class VibeModelBuilder implements ItemBuilder {
	/**
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemName()
	 */
	public String getItemName() {
		// begin-user-code
		return "VIBE Model";
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemType()
	 */
	public ItemType getItemType() {
		// begin-user-code
		return ItemType.Model;
		// end-user-code
	}

	/**
	 * @see ItemBuilder#build(Interface projectSpace)
	 */
	public Item build(IProject projectSpace) {
		// begin-user-code

		// Create a model
		Item item = new VibeModel(projectSpace);

		// Set the item builder's name
		item.setItemBuilderName(getItemName());

		// Return the item
		return item;
		// end-user-code
	}
}