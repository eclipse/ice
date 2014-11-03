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
package org.eclipse.ice.io.ICEDatabaseHarness;

import org.eclipse.ice.core.iCore.IPersistenceProvider;

import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.updateableComposite.Component;
import org.eclipse.ice.item.Item;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A database harness that controls the CRUD operations on items within the
 * ItemManager. It is instantiated within the OSGI, but utilized within the
 * ItemManager. Currently, it will create three sets of persistence units. One
 * is the persistence unit for datastructures and the second is the persistence
 * unit for general item structures. The third persistence unit represents an
 * XML file of KV pairs. These key-value pairs represent the unique id (not
 * DB_ID) of the item with the representation of the root class structure
 * (key-value pair respectively).
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEDatabaseHarness implements IPersistenceProvider {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The reference to the Item's EntityManagerFactory.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private EntityManagerFactory itemEMFactory;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The reference to the Form's EntityManagerFactory.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private EntityManagerFactory formEMFactory;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Contains the list of peristed item's ids in the database.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<Integer> itemList;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This piece is called by the OSGI to set up the EntityManager factories.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param factory
	 *            <p>
	 *            The EntityManagerFactory.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setEntityManagerFactory(EntityManagerFactory factory) {
		// begin-user-code

		// Get the properties
		String property = factory.getProperties().toString();

		// Only check out the factory if it isn't null
		if (factory != null) {
			// Check for the datastructures entity manager factory
			if (property
					.contains("jdbc:derby:database/datastructuresDatabase;create=true")) {
				System.out
						.println("ICEDatabaseHarness Message: Data Structures EntityManagerFactory set!");
				this.formEMFactory = factory;
			}
			// Check for the item entity manager factory
			else if (property
					.contains("jdbc:derby:database/itemDatabase;create=true")) {
				System.out
						.println("ICEDatabaseHarness Message: Item EntityManagerFactory set!");
				this.itemEMFactory = factory;
			} else {
				// Return if the entity manager factory is for something else.
				return;
			}
		}

		// end-user-code
	}

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
	public ICEDatabaseHarness() {
		// begin-user-code

		this.itemList = new ArrayList<Integer>();

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

		// Local declarations
		Item persistedItem;
		Form persistedForm;

		// Make sure the Item can be persisted and that the factories are OK
		if (item == null || this.formEMFactory == null
				|| !this.formEMFactory.isOpen() || this.itemEMFactory == null
				|| !this.formEMFactory.isOpen()
				|| itemList.contains(item.getId())) {
			return false;
		}

		// Create the entity managers
		EntityManager formEManager = this.formEMFactory.createEntityManager();
		EntityManager itemEManager = this.itemEMFactory.createEntityManager();

		// Create a new item and form
		persistedItem = new Item();
		persistedItem.copy(item);
		persistedForm = new Form();
		persistedForm.copy(item.getForm());

		// Set the id -> safety measure
		persistedForm.setItemID(item.getId());

		try {

			// Create transactions for Item and Form
			itemEManager.getTransaction().begin();
			formEManager.getTransaction().begin();

			// Remove the IReactorComponent from persistedForm
			for (int i = 0; i < persistedForm.getComponents().size(); i++) {
				Component comp = persistedForm.getComponents().get(i);
				if (comp instanceof IReactorComponent) {
					persistedForm.removeComponent(comp.getId());
				}
			}

			// Persist the two pieces
			itemEManager.persist(persistedItem);
			formEManager.persist(persistedForm);

			// Commit
			itemEManager.getTransaction().commit();
			formEManager.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();

			// reset transactions
			itemEManager.close();
			formEManager.close();

			return false;
		}

		// Close the managers
		formEManager.close();
		itemEManager.close();

		// Add to list
		this.itemList.add(item.getId());

		System.out.println("ICEDatabaseHarness:  Persisting item "
				+ item.getId() + " successful.");

		// Operation successful, return true
		return true;

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

		// Local Declarations
		Query qItem, qForm;
		Item persistedItem = null;
		Form persistedForm = null;

		// Check factories
		if (this.formEMFactory == null || !this.formEMFactory.isOpen()
				|| this.itemEMFactory == null || !this.formEMFactory.isOpen()) {
			return null;
		}

		// Setup the connections
		EntityManager formEManager = this.formEMFactory.createEntityManager();
		EntityManager itemEManager = this.itemEMFactory.createEntityManager();

		try {

			// Query the item and form
			qItem = itemEManager
					.createQuery("Select b From Item b where b.uniqueId= '"
							+ itemID + "'");
			qForm = formEManager
					.createQuery("Select b From Form b where b.itemID= '"
							+ itemID + "'");

			// Get the item and form
			persistedItem = (Item) qItem.getSingleResult();
			persistedForm = (Form) qForm.getSingleResult();

			// Add form to item
			persistedItem.getForm().copy(persistedForm);

		} catch (Exception e) {
			e.printStackTrace();

			// Reset values
			itemEManager.close();
			formEManager.close();
			return null;
		}

		// Close the managers
		formEManager.close();
		itemEManager.close();

		// Add to list
		this.itemList.add(itemID);

		System.out.println("ICEDatabaseHarness:  Loading item " + itemID
				+ " successful.");

		return persistedItem;
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

		// Local declarations
		Query qItem, qForm;
		Item persistedItem = null;
		Form persistedForm = null;

		// Make sure the factories are set
		if (item == null || !this.itemList.contains(item.getId())
				|| this.formEMFactory == null || !this.formEMFactory.isOpen()
				|| this.itemEMFactory == null || !this.formEMFactory.isOpen()) {
			return false;
		}

		// Initialize EntityManagers
		EntityManager itemEManager = this.itemEMFactory.createEntityManager();
		EntityManager formEManager = this.formEMFactory.createEntityManager();

		try {
			// Query the item and form
			qItem = itemEManager
					.createQuery("Select b From Item b where b.uniqueId= '"
							+ item.getId() + "'");
			qForm = formEManager
					.createQuery("Select b From Form b where b.itemID= '"
							+ item.getId() + "'");

			// Get the item and form
			persistedItem = (Item) qItem.getSingleResult();
			persistedForm = (Form) qForm.getSingleResult();

			// Delete the item and form
			itemEManager.getTransaction().begin();
			formEManager.getTransaction().begin();

			// Remove the IReactorComponent from persistedForm
			for (int i = 0; i < persistedForm.getComponents().size(); i++) {
				Component comp = persistedForm.getComponents().get(i);
				if (comp instanceof IReactorComponent) {
					persistedForm.removeComponent(comp.getId());
				}
			}

			// Delete
			itemEManager.remove(persistedItem);
			formEManager.remove(persistedForm);

			// Commit operation
			itemEManager.getTransaction().commit();
			formEManager.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();

			// Clear values
			itemEManager.close();
			formEManager.close();

			// Return false
			return false;

		}

		// Remove from list
		this.itemList.remove(new Integer(item.getId()));

		// Operation successful!
		// Close managers
		itemEManager.close();
		formEManager.close();

		System.out.println("ICEDatabaseHarness:  Deleting item" + item.getId()
				+ " successful.");

		// Operation successful
		return true;
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

		// Local declarations
		Query qItem, qForm;
		Item persistedItem = null;
		Form persistedForm = null;

		// Only update the Item if it is instantiated, and the factories are
		// instantiated
		if (item == null || !this.itemList.contains(item.getId())
				|| this.formEMFactory == null || !this.formEMFactory.isOpen()
				|| this.itemEMFactory == null || !this.formEMFactory.isOpen()) {
			return false;
		}

		// Initialize EntityManagers
		EntityManager itemEManager = this.itemEMFactory.createEntityManager();
		EntityManager formEManager = this.formEMFactory.createEntityManager();
		try {

			// Query the item and form
			qItem = itemEManager
					.createQuery("Select b From Item b where b.uniqueId= '"
							+ item.getId() + "'");
			qForm = formEManager
					.createQuery("Select b From Form b where b.itemID= '"
							+ item.getId() + "'");

			// Get the item and form
			persistedItem = (Item) qItem.getSingleResult();
			persistedForm = (Form) qForm.getSingleResult();

			// Copy contents over
			persistedItem.copy(item);

			// Update the item and form
			itemEManager.getTransaction().begin();
			formEManager.getTransaction().begin();

			// Delete form and repersist
			formEManager.remove(persistedForm);
			persistedForm = (Form) item.getForm().clone();

			// Remove the IReactorComponent from persistedForm
			for (int i = 0; i < persistedForm.getComponents().size(); i++) {
				Component comp = persistedForm.getComponents().get(i);
				if (comp instanceof IReactorComponent) {
					persistedForm.removeComponent(comp.getId());
				}
			}

			formEManager.persist(persistedForm);

			// Merge item
			itemEManager.merge(persistedItem);

			// Commit operation
			itemEManager.getTransaction().commit();
			formEManager.getTransaction().commit();

		} catch (Exception e) {
			e.printStackTrace();

			// Clear values
			itemEManager.close();
			formEManager.close();

			// Return false
			return false;

		}

		// Close managers
		itemEManager.close();
		formEManager.close();

		System.out.println("ICEDatabaseHarness:  Updating item " + item.getId()
				+ " successful.");

		// Return status
		return true;
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
		EntityManager itemEManager = null;
		Query q = null;

		Item dbItem = null, item = null;
		// Make sure the factories are open
		if (this.formEMFactory == null || !this.formEMFactory.isOpen()
				|| this.itemEMFactory == null || !this.formEMFactory.isOpen()) {
			return null;
		}

		// Initialize EntityManagers
		itemEManager = this.itemEMFactory.createEntityManager();

		// Load all items in the database
		// Create the query to find the item - order by id very important!
		q = itemEManager
				.createQuery("Select b From Item b ORDER BY b.uniqueId");
		// Iterate over the list and load those items seperately
		for (int i = 0; i < q.getResultList().size(); i++) {

			// Grab each item, and iterate over the list!
			item = (Item) q.getResultList().get(i);

			// Use the load item from this operation
			dbItem = this.loadItem(item.getId());

			// If the item is null, then there was an error. Else, add it to the
			// list
			if (dbItem == null) {
				System.out
						.println("ICEDatabaseHarness Message:  A query for itemID: "
								+ item.getId()
								+ " returned null on loading items.");
			} else {
				items.add(dbItem);
			}

		}

		// Close the manager
		itemEManager.close();

		// Keep in mind, return items are shallow.
		return items;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation closes the entityManagers. This is called by the OSGI.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void cleanUp() {
		// begin-user-code

		// Close down the form factory
		if (this.formEMFactory != null && this.formEMFactory.isOpen()) {
			this.formEMFactory.close();
			System.out.println("ICEDatabaseHarness: Form Factory Closed.");
		}

		// Close down the item factory
		if (this.itemEMFactory != null && this.itemEMFactory.isOpen()) {
			this.itemEMFactory.close();
			System.out.println("ICEDatabaseHarness: Item Factory Closed.");
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation allows for startup methods to be performed. This is called
	 * by the OSGI.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void start() {
		// begin-user-code

		// This operation is used to create an EntityManager while ICE is
		// booting so that it does not require additional time while running.

		// If the itemEMFactory and formEMFactory are not null, create
		// entityManagers to help speed this along.
		if (itemEMFactory != null && formEMFactory != null) {

			// This creates the item
			Item item = new Item();
			// Set the item id to 0, so that previous ids are not overridden.
			// This is extremely IMPORTANT! Do not remove!
			item.setId(0);

			// Persist item
			this.persistItem(item);
			// Delete item
			this.deleteItem(item);

		}

		// end-user-code
	}

}