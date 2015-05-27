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
package org.eclipse.ice.item.nuclear;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

public class MooseItemBuilder implements ICompositeItemBuilder {

	/**
	 * 
	 */
	private ArrayList<ItemBuilder> builders = null;

	/**
	 * The constructor.
	 */
	public MooseItemBuilder() {

		builders = new ArrayList<ItemBuilder>();

	}
	
	@Override
	public String getItemName() {
		return "MOOSE Workflow";
	}

	@Override
	public ItemType getItemType() {
		return ItemType.Simulation;
	}

	@Override
	public Item build(IProject projectSpace) {
		MOOSEModel model = null;
		MOOSELauncher launcher = null;
		
		if (!(builders.isEmpty())) {
			for (ItemBuilder i : builders) {
				System.out.println("Builder: " + i.getItemName());
				
				if ("MOOSE Model Builder".equals(i.getItemName())) {
					model = (MOOSEModel) i.build(projectSpace);
				} else if ("MOOSE Launcher".equals(i.getItemName())) {
					launcher = (MOOSELauncher) i.build(projectSpace);
				}
			}
			
			if (model != null) {
				// Set the model on the MooseItem
			}
			
			if (launcher != null) {
				// Set the launcher on the MooseItem
			}
			
			MooseItem moose = new MooseItem(projectSpace);
			moose.setModel(model);
			moose.setLauncher(launcher);
			
			return moose;
		}
		
		return null;
	}

	@Override
	public void addBuilders(ArrayList<ItemBuilder> itemBuilders) {
		if (itemBuilders != null) {
			builders = itemBuilders;
		}
	}

}
