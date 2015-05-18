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
package org.eclipse.ice.vibe.launcher;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/** 
 * <p>This class inherits from ItemBuilder.  It builds the VibeLauncher.  </p>
 * @author Scott Forest Hull II
 */
public class VibeLauncherBuilder implements ItemBuilder {
	
	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemName()
	 */
	public String getItemName() {
		return "VIBE Launcher";		
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemType()
	 */
	public ItemType getItemType() {
		return ItemType.Simulation;
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#build(IProject projectSpace)
	 */
	public Item build(IProject projectSpace) {
		//Create a new item
		Item item = new VibeLauncher(projectSpace);
		
		//Set the itemBuilderName
		item.setItemBuilderName(this.getItemName());
		
		//Return the item
		return item;
	}
}