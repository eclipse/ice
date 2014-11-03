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
package org.eclipse.ice.item.test;

import org.eclipse.core.resources.IProject;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.ItemBuilder;
import org.eclipse.ice.item.ItemType;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is used to build TestJobLaunchers for the MultiLauncherTester.
 * Calling the build() operation returns an instance of TestJobLauncher
 * regardless of the current type retrieved from getItemType(). The ability to
 * change the ItemType is only available to make sure the MultiLauncherBuilder
 * does not include things other than JobLaunchers.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeItemBuilder implements ItemBuilder {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The name that the builder should return from getItemName();
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private String name = "Selina Kyle";

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The ItemType.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ItemType type = ItemType.Simulation;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the name that the builder should return from
	 * getItemName();
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemName
	 *            <p>
	 *            The name
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setName(String itemName) {
		// begin-user-code

		if (itemName != null) {
			name = itemName;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets the type of the FakeItemBuilder. This does not change
	 * what the build() operation returns - a TestJobLauncher - but it does
	 * change the output of getItemType().
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemType
	 *            <p>
	 *            The ItemType that this builder will report.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setType(ItemType itemType) {
		// begin-user-code

		if (itemType != null) {
			type = itemType;
		}
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
		return name;
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
		return type;
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

		Item item = new TestJobLauncher(projectSpace);
		item.setName(name);

		// Set the item builder name
		item.setItemBuilderName(this.getItemName());

		return item;
		// end-user-code
	}
}