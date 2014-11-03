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

import static org.eclipse.ice.item.jobLauncher.multiLauncher.MultiLauncher.*;

import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.ICompositeItemBuilder;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;
import org.eclipse.ice.item.jobLauncher.JobLauncher;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for building instances of the MultiLauncher Item.
 * Since the MultiLauncher is a composite Item, this builder will not create a
 * MultiLauncher unless addBuilders() has been called with list of builders in
 * ICE that has a size greater than one. If it is unable to create a new
 * MultiLauncher, build() will return null.
 * </p>
 * <p>
 * The MultiLauncherBuilder will check the list of ItemBuilders for those with a
 * type of ItemType.Simulation. It will not include itself in the list it
 * creates.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MultiLauncherBuilder implements ICompositeItemBuilder {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<ItemBuilder> builders = null;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public MultiLauncherBuilder() {
		// begin-user-code

		builders = new ArrayList<ItemBuilder>();

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getItemName() {
		// begin-user-code
		return "MultiLauncher";
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#getItemType()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ItemType getItemType() {
		// begin-user-code
		return ItemType.Simulation;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ItemBuilder#build(IProject projectSpace)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Item build(IProject projectSpace) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see ICompositeItemBuilder#addBuilders(ArrayList<ItemBuilder>
	 *      itemBuilders)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addBuilders(ArrayList<ItemBuilder> itemBuilders) {
		// begin-user-code

		if (itemBuilders != null) {
			builders = itemBuilders;
		}
		// end-user-code
	}
}