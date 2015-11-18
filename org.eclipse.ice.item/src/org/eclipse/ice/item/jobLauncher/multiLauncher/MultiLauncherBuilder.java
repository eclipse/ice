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
package org.eclipse.ice.item.jobLauncher.multiLauncher;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * This class is responsible for building instances of the MultiLauncher Item.
 * Since the MultiLauncher is a composite Item, this builder will not create a
 * MultiLauncher unless addBuilders() has been called with list of builders in
 * ICE that has a size greater than one. If it is unable to create a new
 * MultiLauncher, build() will return null.
 * <p>
 * The MultiLauncherBuilder will check the list of ItemBuilders for those with a
 * type of ItemType.Simulation. It will not include itself in the list it
 * creates.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class MultiLauncherBuilder implements ICompositeItemBuilder {
	/**
	 * 
	 */
	private ArrayList<ItemBuilder> builders = null;

	/**
	 * The constructor.
	 */
	public MultiLauncherBuilder() {

		builders = new ArrayList<ItemBuilder>();

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.ItemBuilder#getItemName()
	 */
	@Override
	public String getItemName() {
		return "MultiLauncher";
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.ItemBuilder#getItemType()
	 */
	@Override
	public ItemType getItemType() {
		return ItemType.Simulation;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.ItemBuilder#build(org.eclipse.core.resources.IProject)
	 */
	@Override
	public Item build(IProject projectSpace) {

		// Local Declarations
		MultiLauncher launcher = null;
		ArrayList<Item> jobLaunchers = null;

		// Only build the launcher if the list of builders is available
		if (!(builders.isEmpty())) {
			// Create the list of JobLaunchers
			jobLaunchers = new ArrayList<Item>();
			for (ItemBuilder i : builders) {
				// Only add them if they have the right type and don't come from
				// this builder.
				if (i.getItemType() == ItemType.Simulation && i != this) {
					jobLaunchers.add(i.build(projectSpace));
				}
			}
			// Create the launcher
			launcher = new MultiLauncher(projectSpace);
			launcher.setJobLaunchers(jobLaunchers);
			// Set the itemBuilderName
			launcher.setItemBuilderName(this.getItemName());
			return launcher;
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.ICompositeItemBuilder#addBuilders(java.util.ArrayList)
	 */
	@Override
	public void addBuilders(ArrayList<ItemBuilder> itemBuilders) {

		if (itemBuilders != null) {
			builders = itemBuilders;
		}
	}

	@Override
	public boolean isPublishable() {
		return true;
	}
}