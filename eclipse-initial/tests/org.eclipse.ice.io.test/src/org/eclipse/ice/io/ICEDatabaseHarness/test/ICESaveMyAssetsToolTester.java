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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import static org.junit.Assert.fail;

import org.eclipse.ice.item.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import org.eclipse.ice.database.test.DatabaseDatastructuresTestComponent;
import org.eclipse.ice.database.test.DatabaseItemTestComponent;
import org.eclipse.ice.io.ICEDatabaseHarness.ICEDatabaseHarness;
import org.eclipse.ice.io.ICEDatabaseHarness.ICESaveMyAssetsTool;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A class that tests the ICESaveMyAssetsTool class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
@Ignore
public class ICESaveMyAssetsToolTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The item's EMF.
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
	 * The form's EMF.
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
	 * The Assets tool to test.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICESaveMyAssetsTool tool;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An operation that checks the loading of items and forms.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkLoading() {
		// begin-user-code
		// Local Declarations

		System.out.println("Loading the database via SQL commands");

		// tool.loadOldItemsAndForms();
		tool.flywayIntergration();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets up the database.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Before
	public void setupDatabase() {
		// begin-user-code
		System.out.println("ICESaveMyAssetsToolTester Message: Setting up "
				+ "ItemDatabase on thread " + Thread.currentThread().getId());

		itemEMFactory = DatabaseItemTestComponent.getFactory();

		// Check the factory
		if (itemEMFactory == null) {
			System.out.println("ICESaveMyAssetsToolTester Message: "
					+ "Factory is null or "
					+ "CountDownLatch timed out. Aborting.");
			fail();
		} else {
			System.out.println("ICESaveMyAssetsToolTester Message: "
					+ "Factory is good!");
		}
		System.out.println("ICESaveMyAssetsToolTester Message: Setting up "
				+ "FormDatabase on thread " + Thread.currentThread().getId());

		formEMFactory = DatabaseDatastructuresTestComponent.getFactory();

		// Check the factory
		if (formEMFactory == null) {
			System.out.println("ICESaveMyAssetsToolTester Message: "
					+ "Factory is null or "
					+ "CountDownLatch timed out. Aborting.");
			fail();
		} else {
			System.out.println("ICESaveMyAssetsToolTester Message: "
					+ "Factory is good!");
		}
		// Create instance to the tool
		tool = new ICESaveMyAssetsTool();
		// If these are set, then set them on the factory
		tool.setEntityManagerFactory(itemEMFactory);
		tool.setEntityManagerFactory(formEMFactory);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Closes the database.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@After
	public void closeDatabase() {
		// begin-user-code

		// Close the factories
		this.formEMFactory.close();
		this.itemEMFactory.close();

		// end-user-code
	}
}