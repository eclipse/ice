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
package org.eclipse.ice.item.jobprofile;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * 
 * @author Jay Jay Billings
 */
public class JobProfileBuilder implements ItemBuilder {
	/**
	 * <p>
	 * A handle to the JobProfile created by the builder.
	 * </p>
	 * 
	 */
	private JobProfile jobProfile;

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemName()
	 */
	public String getItemName() {

		return "Job Profile";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemType()
	 */
	public ItemType getItemType() {

		return ItemType.Model;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#build(IProject projectSpace)
	 */
	public Item build(IProject projectSpace) {
		// Create a new job profile
		JobProfile item = new JobProfile(projectSpace);
		// Set the name for the builder
		item.setItemBuilderName(getItemName());
		// Return the item
		return item;

	}
}