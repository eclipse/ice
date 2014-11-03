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
package org.eclipse.ice.core.iCore;

import java.util.ArrayList;

import org.eclipse.ice.item.Item;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * An interface designed for item persistence within ICE.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IPersistenceProvider {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Persists an item. Returns true if the operation was successful. False
	 * otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param item
	 *            <p>
	 *            The item to be persisted.
	 *            </p>
	 * @return <p>
	 *         Returns true if the operation was successful. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean persistItem(Item item);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Loads the specified item keyed on the ID. Returns the item, or null if an
	 * error was encountered.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param itemID
	 *            <p>
	 *            The item's ID.
	 *            </p>
	 * @return <p>
	 *         The returned item.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Item loadItem(int itemID);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deletes an item from the persistence provider. Returns true if the
	 * operation was successful. False otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param item
	 *            <p>
	 *            The item to be deleted.
	 *            </p>
	 * @return <p>
	 *         True if successful. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean deleteItem(Item item);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Updates an item in the persistence unit.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param item
	 *            <p>
	 *            The item to be updated.
	 *            </p>
	 * @return <p>
	 *         Returns true if successful. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean updateItem(Item item);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns all the Items in the persistence piece.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The list of items.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Item> loadItems();
}