/*******************************************************************************
* Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.filesimulation;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/** 
 * <p>The SimulationBuilder is an ItemBuilder for the Simulation Item and is defined as an OSGi component in ICE. It dynamically registers the creation of Simulations as a service in ICE.</p>
 * @author Jay Jay Billings
 */
public class FileSimulationBuilder implements ItemBuilder {
	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemName()
	 */
	@Override
	public String getItemName() {
		return "File Command Launcher";
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemType()
	 */
	@Override
	public ItemType getItemType() {
		return ItemType.Simulation;
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#build()
	 */
	@Override
	public Item build(IProject project) {
		
		FileSimulation fileSim = new FileSimulation(project);
		fileSim.setName(getItemName());
		fileSim.setItemBuilderName(getItemName());
		
		return fileSim;
	}
}