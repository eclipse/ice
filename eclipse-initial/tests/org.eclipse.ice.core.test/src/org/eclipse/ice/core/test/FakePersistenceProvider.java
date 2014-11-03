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
package org.eclipse.ice.core.test;

import org.eclipse.ice.core.iCore.IPersistenceProvider;

import java.util.ArrayList;

import org.eclipse.ice.item.Item;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakePersistenceProvider implements IPersistenceProvider {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if the Items were loaded, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private volatile boolean loaded = false;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if an individual Item was persisted, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private volatile boolean persisted = false;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if an individual Item was updated, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private volatile boolean updated = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if an individual Item was deleted, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private volatile boolean deleted = false;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if the Items were loaded, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if the Items were loaded, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean allLoaded() {
		// begin-user-code
		return loaded;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation resets the provider's flags for the test.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reset() {
		// begin-user-code

		// Reset all the flags to false
		loaded = false;
		persisted = false;
		updated = false;
		deleted = false;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if an Item was loaded, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if an individual Item was persisted, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean itemPersisted() {
		// begin-user-code
		return persisted;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if an Item was updated, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if an individual Item was updated, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean itemUpdated() {
		// begin-user-code
		return updated;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if an Item was loaded, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if an individual Item was deleted, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean itemDeleted() {
		// begin-user-code
		return deleted;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#persistItem(Item item)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean persistItem(Item item) {
		// begin-user-code

		persisted = true;

		return persisted;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#loadItem(int itemID)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Item loadItem(int itemID) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#deleteItem(Item item)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean deleteItem(Item item) {
		// begin-user-code

		deleted = true;

		return deleted;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#updateItem(Item item)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean updateItem(Item item) {
		// begin-user-code

		updated = true;

		return updated;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#loadItems()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Item> loadItems() {
		// begin-user-code

		// Local Declarations
		ArrayList<Item> items = new ArrayList<Item>();
		FakeItem fake1 = new FakeItem(null), fake2 = new FakeItem(null);

		// Set the ids
		fake1.setId(1);
		fake2.setId(3);

		// Add the Items to the list
		items.add(fake1);
		items.add(fake2);

		// Set the flag
		loaded = true;

		return items;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IPersistenceProvider#getClassForItemId(int itemID)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Class getClassForItemId(int itemID) {
		// begin-user-code
		// TODO Auto-generated method stub
		return null;
		// end-user-code
	}
}