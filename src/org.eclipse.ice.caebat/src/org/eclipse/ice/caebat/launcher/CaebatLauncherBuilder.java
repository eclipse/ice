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
package org.eclipse.ice.caebat.launcher;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/** 
 * <!-- begin-UML-doc -->
 * <p>This class inherits from ItemBuilder.  It builds the CaebatLauncher.  </p>
 * <!-- end-UML-doc -->
 * @author s4h
 */
public class CaebatLauncherBuilder implements ItemBuilder {
	
	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemName()
	 */
	public String getItemName() {
		
		// begin-user-code
		
		return "Caebat Launcher";
		
		// end-user-code
		
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemType()
	 */
	public ItemType getItemType() {
		
		// begin-user-code
		
		return ItemType.Simulation;
		
		// end-user-code
		
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#build(IProject projectSpace)
	 */
	public Item build(IProject projectSpace) {
		// begin-user-code
		
		//Create a new item
		Item item = new CaebatLauncher(projectSpace);
		
		//Set the itemBuilderName
		item.setItemBuilderName(this.getItemName());
		
		//Return the item
		return item;
		
		// end-user-code
		
	}
}