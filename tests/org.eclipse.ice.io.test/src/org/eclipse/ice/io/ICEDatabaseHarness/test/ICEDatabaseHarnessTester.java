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
package org.eclipse.ice.io.ICEDatabaseHarness.test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.ice.database.test.DatabaseDatastructuresTestComponent;
import org.eclipse.ice.database.test.DatabaseItemTestComponent;
import org.eclipse.ice.io.ICEDatabaseHarness.ICEDatabaseHarness;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.datastructures.form.Form;
import org.eclipse.ice.datastructures.form.MasterDetailsComponent;
import org.eclipse.ice.datastructures.form.MasterDetailsPair;
import org.eclipse.ice.datastructures.form.MatrixComponent;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.datastructures.form.TreeComposite;
import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;
import org.eclipse.ice.datastructures.resource.ICEResource;
import org.eclipse.ice.item.Item;
import org.eclipse.ice.item.jobLauncher.*;
import org.eclipse.ice.item.jobprofile.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

import org.eclipse.core.resources.IProject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the ICEDatabaseHarness.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@Ignore
public class ICEDatabaseHarnessTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static ICEDatabaseHarness databaseHarness;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The item's EntityManagerFactory.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static EntityManagerFactory itemEMFactory;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The form's EntityManagerFactory.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static EntityManagerFactory formEMFactory;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The projectspace.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private static IProject project;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the tests.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void setup() {
		// begin-user-code

		System.out.println("ICEDatabaseHarnessTester Message: Setting up "
				+ "ItemDatabase on thread " + Thread.currentThread().getId());

		itemEMFactory = DatabaseItemTestComponent.getFactory();

		// Check the factory
		if (itemEMFactory == null) {
			System.out.println("ICEDatabaseHarnessTester Message: "
					+ "Factory is null or "
					+ "CountDownLatch timed out. Aborting.");
			fail();
		} else {
			System.out.println("ICEDatabaseHarnessTester Message: "
					+ "Factory is good!");
		}
		System.out.println("ICEDatabaseHarnessTester Message: Setting up "
				+ "FormDatabase on thread " + Thread.currentThread().getId());

		formEMFactory = DatabaseDatastructuresTestComponent.getFactory();

		// Check the factory
		if (formEMFactory == null) {
			System.out.println("ICEDatabaseHarnessTester Message: "
					+ "Factory is null or "
					+ "CountDownLatch timed out. Aborting.");
			fail();
		} else {
			System.out.println("ICEDatabaseHarnessTester Message: "
					+ "Factory is good!");
		}
		// Create instance to database harness
		databaseHarness = new ICEDatabaseHarness();
		// If these are set, then set them on the factory
		databaseHarness.setEntityManagerFactory(formEMFactory);
		databaseHarness.setEntityManagerFactory(itemEMFactory);

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation cleans up the operations. This includes closing the
	 * database connections.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@AfterClass
	public static void cleanUp() {
		// begin-user-code

		// Clean up the Form and Item databases
		// if factory is null, return
		if (itemEMFactory == null || formEMFactory == null
				|| !itemEMFactory.isOpen() || !formEMFactory.isOpen()) {
			return;
		}
		Boolean itemFlag = false, formFlag = false;
		EntityManager emItem = itemEMFactory.createEntityManager();
		EntityManager emForm = formEMFactory.createEntityManager();
		emItem.getTransaction().begin();

		// Query from database
		Query q, q4;
		Query q2 = emItem.createQuery("Select b From Item b");
		Query q3 = emForm.createQuery("Select c From Form c");
		// If anything is in the tables, remove contents

		if (q2.getResultList().size() != 0) {
			q = emItem.createQuery("Select b From Item b");
			int size = q.getResultList().size();
			for (int i = 0; i < size; i++) {
				emItem.remove(q.getResultList().get(0));
			}
			itemFlag = true;
		}

		if (itemFlag == true) {
			emItem.getTransaction().commit();
		}
		emItem.close();

		// Begin transaction on form - uses the form's delete from database
		// operation
		// to clear out the database completely.
		emForm.getTransaction().begin();
		if (q3.getResultList().size() != 0) {
			q4 = emForm.createQuery("Select b From Form b");
			int size = q4.getResultList().size();
			for (int i = 0; i < size; i++) {

				// Gather the forms
				Form form = (Form) q4.getResultList().get(0);
				emForm.remove(form);
				formFlag = true;
			}
		}

		// If there is a modification needed, commit it
		if (formFlag) {
			emForm.getTransaction().commit();
		}

		emForm.close();

		// Delete the database
		File folder = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "database");
		deleteFolder(folder);

		// end-user-code
	}

	// Deletes the database folder and all of its contents.
	public static void deleteFolder(File folder) {
		File[] files = folder.listFiles();

		if (files != null) { // some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the construction.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code
		// Local Declarations

		// The construction is setup in the setup operation.

		// This will test with nullary constructor
		ICEDatabaseHarness badHarness = new ICEDatabaseHarness();
		// Should return False or null accordingly if the database hardness is
		// not setup correctly.
		assertFalse(badHarness.deleteItem(null));
		assertFalse(badHarness.persistItem(null));
		assertNull(badHarness.loadItem(0));
		assertNull(badHarness.loadItems());
		assertFalse(badHarness.updateItem(null));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the CRUD operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCRUDOperations() {
		// begin-user-code
		// Local Declarations
		Item item = new Item();
		JobLauncher jobItem = new JobLauncher();
		JobProfile jobProfile = new JobProfile();
		int itemCounter = 1;
		Form form = new Form();
		String builderName = "BuildeR!";

		Item loadedItem, updatedItem, loadedJobL, updatedJobL, loadedJobP, updatedJobP;

		// Check to see for nullary operations
		assertFalse(databaseHarness.deleteItem(null));
		assertFalse(databaseHarness.persistItem(null));
		assertNull(databaseHarness.loadItem(0));
		assertEquals(0, databaseHarness.loadItems().size());
		assertFalse(databaseHarness.updateItem(null));

		// Check for invalid usage - no files set.
		assertFalse(databaseHarness.deleteItem(item));
		assertNull(databaseHarness.loadItem(0));
		assertEquals(0, databaseHarness.loadItems().size());
		assertFalse(databaseHarness.updateItem(item));

		// Now, setup database with 3 unique pieces.

		// Add stuff to item
		item = setupItem();
		// Change data
		item.setName("Billy");
		item.setId(itemCounter++);

		jobItem.setName("JobITEMASDAD");
		jobItem.setId(itemCounter++);
		jobProfile.setName("JobProfile item thingy");
		jobProfile.setId(itemCounter++);
		jobProfile.setItemBuilderName(builderName);

		// Persist to database
		assertTrue(databaseHarness.persistItem(item));
		assertTrue(databaseHarness.persistItem(jobItem));
		assertTrue(databaseHarness.persistItem(jobProfile));

		// Load from database
		loadedItem = databaseHarness.loadItem(1);
		loadedJobL = databaseHarness.loadItem(2);
		loadedJobP = databaseHarness.loadItem(3);

		// Make sure not null
		assertNotNull(loadedItem);
		assertNotNull(loadedJobL);
		assertNotNull(loadedJobP);

		// Check that the items are loaded
		assertTrue(loadedItem.equals(item));
		assertTrue(loadedJobL.equals(jobItem));
		assertTrue(loadedJobP.equals(jobProfile));

		// Load items
		ArrayList<Item> itemsLoaded;

		// Load the items
		itemsLoaded = databaseHarness.loadItems();

		// Make sure not null
		assertNotNull(itemsLoaded);

		assertEquals(3, itemsLoaded.size());

		// Remember, zero indexed!
		loadedItem = itemsLoaded.get(0);
		loadedJobL = itemsLoaded.get(1);
		loadedJobP = itemsLoaded.get(2);

		// Make sure not null
		assertNotNull(loadedItem);
		assertNotNull(loadedJobL);
		assertNotNull(loadedJobP);

		// Check comparisons
		assertTrue(loadedItem.equals(item));
		assertTrue(loadedJobL.equals(jobItem));
		assertTrue(loadedJobP.equals(jobProfile));

		// Update pieces
		item.setName("New Item Name");
		jobItem.setName("New JobL Name");
		jobProfile.setName("New JobP Name");

		// Update to database
		assertTrue(databaseHarness.updateItem(item));
		assertTrue(databaseHarness.updateItem(jobItem));
		assertTrue(databaseHarness.updateItem(jobProfile));

		// Load back, check values
		// Load from database
		loadedItem = databaseHarness.loadItem(1);
		loadedJobL = databaseHarness.loadItem(2);
		loadedJobP = databaseHarness.loadItem(3);

		// Make sure not null
		assertNotNull(loadedItem);
		assertNotNull(loadedJobL);
		assertNotNull(loadedJobP);

		// Check that the items are loaded
		assertTrue(loadedItem.equals(item));
		assertTrue(loadedJobL.equals(jobItem));
		assertTrue(loadedJobP.equals(jobProfile));

		// Delete them!
		assertTrue(databaseHarness.deleteItem(item));
		assertTrue(databaseHarness.deleteItem(jobItem));
		assertTrue(databaseHarness.deleteItem(jobProfile));

		// Try to load them back, should be gone!
		// Load from database
		loadedItem = databaseHarness.loadItem(1);
		loadedJobL = databaseHarness.loadItem(2);
		loadedJobP = databaseHarness.loadItem(3);

		// Make sure null
		assertNull(loadedItem);
		assertNull(loadedJobL);
		assertNull(loadedJobP);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the start method on ICEDatabaseHarness.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void checkStart() {
		// begin-user-code

		// This piece is pretty hard to test, since this is just a utility piece
		// for ICE application (end product).
		// So we will just call it!
		databaseHarness.start();
		// IT will either pass or fail. Regardless, the factories must be set
		// for this to work!

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An operation that sets up an item. Returns a newly created item.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         A newly created item.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Item setupItem() {
		// begin-user-code
		// Declarations for item
		DataComponent dataComponent1 = null;
		DataComponent dataComponent2 = null;
		ResourceComponent outputComponent1 = null;
		ResourceComponent outputComponent2 = null;
		TableComponent tableComponent1 = null;
		TableComponent tableComponent2 = null;
		Entry entry1 = null;
		Entry entry2 = null;
		Entry entry3 = null;
		Entry entry4 = null;
		Entry entry5 = null;
		ICEResource resource1 = null;
		ICEResource resource2 = null;
		ICEResource resource3 = null;
		ICEResource resource4 = null;
		Form form = null;
		DataComponent globalDataComponent = null;

		// Added
		MasterDetailsComponent masterDetailsComponent = null;
		MasterDetailsPair masterDetailsPair = null;
		ArrayList<MasterDetailsPair> list = new ArrayList<MasterDetailsPair>();
		GeometryComponent geometryComponent = null;
		ComplexShape complexShape = null;
		ComplexShape complexShape2 = null;
		PrimitiveShape primitiveShape = null;
		MatrixComponent matrixComponent = null;
		DataComponent dComp = new DataComponent();
		dComp.setId(0);

		// TreeComposite stuff
		TreeComposite parentTree, childTree = null;
		DataComponent treeNode = null;

		// Add TreeComposite
		parentTree = new TreeComposite();
		parentTree.setName("Parent");
		parentTree.setId(5);
		parentTree.setDescription("Decription of parent");
		childTree = new TreeComposite();
		childTree.setName("Parent");
		childTree.setId(5);
		childTree.setDescription("Decription of parent");
		treeNode = new DataComponent();
		parentTree.setNextChild(childTree);
		treeNode.setName("TREE NODE!");
		childTree.addComponent(treeNode);

		// Setup new additions
		// Setup MasterDetailsComponent
		masterDetailsPair = new MasterDetailsPair("Type One", dComp);
		masterDetailsPair.setId(0);
		masterDetailsPair.setMasterDetailsPairId(0);
		list.add(masterDetailsPair);
		masterDetailsComponent = new MasterDetailsComponent();
		masterDetailsComponent.setTemplates(list);
		masterDetailsComponent.setId(0);
		masterDetailsComponent.addMaster();

		// Add globals to GlobalDataComponent
		globalDataComponent = new DataComponent();
		entry5 = new Entry();
		entry5.setId(0);
		globalDataComponent.addEntry(entry5);
		globalDataComponent.setId(0);

		masterDetailsComponent.setGlobalsComponent(globalDataComponent);

		// Setup Geometry
		complexShape = new ComplexShape();
		complexShape.setId(0);
		complexShape.getTransformation().setId(0);
		complexShape2 = new ComplexShape();
		complexShape2.setId(0);
		complexShape2.getTransformation().setId(0);
		primitiveShape = new PrimitiveShape();
		primitiveShape.setId(0);
		primitiveShape.getTransformation().setId(0);
		geometryComponent = new GeometryComponent();
		geometryComponent.setId(0);

		complexShape.addShape(complexShape2);
		geometryComponent.addShape(primitiveShape);
		geometryComponent.addShape(complexShape);

		// Setup MatrixComponent
		matrixComponent = new MatrixComponent();
		matrixComponent.setId(0);
		matrixComponent.addRow();
		matrixComponent.addRow();
		matrixComponent.addColumn();

		Item item1 = null;

		// Create item, form, components, entries, and resources
		// setup entries

		// Create the full entry, allowedValueType = AllowedValueType.Discrete
		entry1 = new Entry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("true");
				allowedValues.add("false");
				this.defaultValue = "true";
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		entry1.setName("Entry1");
		entry1.setDescription("I am Entry1!");
		entry1.setId(0);
		entry1 = (Entry) entry1.clone();

		// create the full entry, allowedValueType = AllowedValueType.Continuous
		entry2 = new Entry() {

			@Override
			protected void setup() {
				allowedValues = new ArrayList<String>(2);
				allowedValues.add("-10");
				allowedValues.add("10000");
				this.defaultValue = "true";
				this.allowedValueType = AllowedValueType.Continuous;
			}
		};
		entry2.setName("Entry2");
		entry2.setDescription("I am Entry2!");
		entry2.setId(0);
		entry2 = (Entry) entry2.clone();

		// create the full entry, allowedValueType = AllowedValueType.Discrete
		entry3 = new Entry() {

			@Override
			protected void setup() {

				this.defaultValue = "I am discrete!";
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		entry3.setName("Entry3");
		entry3.setDescription("I am Entry3!");
		entry3.setId(0);
		entry3 = (Entry) entry3.clone();

		// create the full entry, allowedValueType = AllowedValueType.Discrete
		entry4 = new Entry() {

			@Override
			protected void setup() {

				this.defaultValue = "I am discrete too!";
				this.allowedValueType = AllowedValueType.Discrete;
			}
		};
		entry4.setName("Entry4");
		entry4.setDescription("I am Entry4!");
		entry4.setId(0);
		entry4 = (Entry) entry4.clone();

		// Resource1
		resource1 = new ICEResource();
		resource1.setName("ICEResource1");
		resource1.setDescription("I am an ICEResource1!");
		resource1.setId(0);
		try {
			resource1.setContents(new File("file.txt"));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Resource2
		resource2 = new ICEResource();
		resource2.setName("ICEResource2");
		resource2.setDescription("I am an ICEResource2!");
		resource2.setId(0);
		try {
			resource2.setContents(new File("file.txt"));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Resource3
		resource3 = new ICEResource();
		resource3.setName("ICEResource3");
		resource3.setDescription("I am an ICEResource3!");
		resource3.setId(0);
		try {
			resource3.setContents(new File("file.txt"));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Resource4
		resource4 = new ICEResource();
		resource4.setName("ICEResource1");
		resource4.setDescription("I am an ICEResource4!");
		resource4.setId(0);
		try {
			resource4.setContents(new File("file.txt"));
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Setup Components, add entries as necessary

		// Setup DataComponents

		// DataComponent1
		dataComponent1 = new DataComponent();
		dataComponent1.setName("DataComponent1");
		dataComponent1.setDescription("I am DataComponent1!");
		dataComponent1.setId(0);
		// add entries
		dataComponent1.addEntry(entry1);
		dataComponent1.addEntry(entry2);
		dataComponent1.addEntry(entry3);
		dataComponent1.addEntry(entry4);

		// DataComponent2 - no entries
		dataComponent2 = new DataComponent();
		dataComponent2.setName("DataComponent2");
		dataComponent2.setDescription("I am DataComponent2!");
		dataComponent2.setId(0);

		// ResourceComponent1
		outputComponent1 = new ResourceComponent();
		outputComponent1.setName("ResourceComponent3");
		outputComponent1.setDescription("I am ResourceComponent3!");
		outputComponent1.setId(0);
		// setupResources
		outputComponent1.addResource(resource1);
		outputComponent1.addResource(resource2);
		outputComponent1.addResource(resource3);
		outputComponent1.addResource(resource4);

		// ResourceComponent2 - no resources
		outputComponent2 = new ResourceComponent();
		outputComponent2.setName("ResourceComponent4");
		outputComponent2.setDescription("I am ResourceComponent4!");
		outputComponent2.setId(0);

		// TableComponent1
		tableComponent1 = new TableComponent();
		tableComponent1.setName("TableComponent5");
		tableComponent1.setDescription("I am TableComponent5!");
		tableComponent1.setId(0);
		// setupTemplate
		ArrayList<Entry> rowTemplate = new ArrayList<Entry>();
		rowTemplate.add((Entry) entry1.clone());
		rowTemplate.add((Entry) entry2.clone());
		rowTemplate.add((Entry) entry3.clone());
		rowTemplate.add((Entry) entry4.clone());
		// add rowtemplate to table
		tableComponent1.setRowTemplate(rowTemplate);

		// TableComponent2 - No template
		tableComponent2 = new TableComponent();
		tableComponent2.setName("TableComponent6");
		tableComponent2.setDescription("I am TableComponent6!");
		tableComponent2.setId(0);

		// create item
		// Create item, setup item, and add form to item
		item1 = new Item();

		// Get form and add components
		form = item1.getForm();

		// add components
		form.addComponent(dataComponent1);
		form.addComponent(dataComponent2);
		form.addComponent(outputComponent1);
		form.addComponent(outputComponent2);
		form.addComponent(tableComponent1);
		form.addComponent(tableComponent2);
		form.addComponent(masterDetailsComponent);
		form.addComponent(geometryComponent);
		form.addComponent(matrixComponent);
		form.addComponent(parentTree);
		form.setName("Form");
		form.setItemID(1);

		item1.setName("Item");
		item1.setDescription("I am Item!");

		assertEquals(10, item1.getForm().getComponents().size());

		return item1;
		// end-user-code
	}
}