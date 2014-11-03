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
 * <!-- begin-UML-doc -->
 * <p>The SimulationBuilder is an ItemBuilder for the Simulation Item and is defined as an OSGi component in ICE. It dynamically registers the creation of Simulations as a service in ICE.</p>
 * <!-- end-UML-doc -->
 * @author jaybilly
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FileSimulationBuilder implements ItemBuilder {
	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemName()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getItemName() {
		// begin-user-code
		return "File Command Launcher";
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#getItemType()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ItemType getItemType() {
		// begin-user-code
		return ItemType.Simulation;
		// end-user-code
	}

	/** 
	 * (non-Javadoc)
	 * @see ItemBuilder#build()
	 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Item build(IProject project) {
		// begin-user-code
		
		FileSimulation fileSim = new FileSimulation(project);
		fileSim.setName(getItemName());
		fileSim.setItemBuilderName(getItemName());
		
		return fileSim;
		// end-user-code
	}
}