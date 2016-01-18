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
import org.eclipse.ice.item.AbstractItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemType;

/** 
 * <p>The SimulationBuilder is an ItemBuilder for the Simulation Item and is defined as an OSGi component in ICE. It dynamically registers the creation of Simulations as a service in ICE.</p>
 * @author Jay Jay Billings
 */
public class FileSimulationBuilder extends AbstractItemBuilder {
	
	/**
	 * The Constructor
	 */
	public FileSimulationBuilder() {
		setName("File Command Launcher");
		setType(ItemType.Simulation);
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.item.AbstractItemBuilder#getInstance(org.eclipse.core.resources.IProject)
	 */
	public Item getInstance(IProject project) {
		
		FileSimulation fileSim = new FileSimulation(project);
		fileSim.setName(getItemName());
		fileSim.setItemBuilderName(getItemName());
		
		return fileSim;
	}
}
