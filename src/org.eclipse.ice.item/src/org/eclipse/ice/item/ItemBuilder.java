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
package org.eclipse.ice.item;

import org.eclipse.core.resources.IProject;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The ItemBuilder interface is used to register an Item with the ItemManager
 * and to build Items of a specific type. The getItemName() and getItemType()
 * operations provide information that can be used to organize Items and the
 * build() operation encapsulates the logical required to instantiate subclasses
 * of Item.
 * </p>
 * <p>
 * In ICE, this interface is used as a pluggable service in the OSGi framework
 * to provide Items to the Core. ItemBuilders are also registered for serialized
 * Items that are persistent on the disk.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface ItemBuilder {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	Item item = null;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the short name of the Item that can be constructed
	 * by this ItemBuilder.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The name
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getItemName();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the type of Item that can be built by the
	 * ItemBuilder.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The type
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ItemType getItemType();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation builds and returns an instance of the Item that can be
	 * constructed by the ItemBuilder.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param projectSpace
	 *            <p>
	 *            The Eclipse project that the Item should use for storage.
	 *            </p>
	 * @return <p>
	 *         The newly created Item.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Item build(IProject projectSpace);
}