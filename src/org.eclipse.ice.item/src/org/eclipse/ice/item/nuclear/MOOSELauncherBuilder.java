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
package org.eclipse.ice.item.nuclear;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * Class constructs a MOOSELauncher suite for launching MARMOT, BISON, RELAP7,
 * and RAVEN jobs.
 * 
 * @author w5q
 * 
 */

public class MOOSELauncherBuilder implements ItemBuilder {

	/**
	 * The name that should be used for the Item and this builder
	 */
	public static final String name = "MOOSE Launcher";

	/**
	 * The type of the Item
	 */
	public static final ItemType type = ItemType.Simulation;

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.ItemBuilder#getItemName()
	 */
	@Override
	public String getItemName() {
		return name;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.ItemBuilder#getItemType()
	 */
	@Override
	public ItemType getItemType() {
		return type;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.item.ItemBuilder#build()
	 */
	@Override
	public Item build(IProject project) {
		return new MOOSELauncher(project);
	}

}
