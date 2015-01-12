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
package org.eclipse.ice.reflectivity.test;

import static org.junit.Assert.*;

import org.eclipse.ice.datastructures.ICEObject.ListComponent;
import org.eclipse.ice.datastructures.form.Material;
import org.eclipse.ice.datastructures.form.TableComponent;
import org.eclipse.ice.reflectivity.ReflectivityModel;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class is responsible for testing the ReflectivityModel.
 * @author Jay Jay Billings
 *
 */
public class ReflectivityModelTester {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation sets up the workspace. It copies the necessary MOOSE data
	 * files into ${workspace}/MOOSE.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void beforeTests() {
		// begin-user-code
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ReflectivityModel and makes sure that it can
	 * properly construct its Form.
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
		int listID = 1;
		ListComponent<Material> list;
		
		// Just create one with the nullary constructor
		// No need to check the Item with a IProject instance
		ReflectivityModel model = new ReflectivityModel();

		// Make sure we have a form and some components
		assertNotNull(model.getForm());
		assertEquals(1, model.getForm().getComponents().size());
		
		// Get the table component
		list = (ListComponent<Material>) model.getForm().getComponent(listID);
		
		// Make sure it's not null and the name is correct
		assertNotNull(list);
		assertEquals("Reflectivity Input Data", list.getName());
		
		// end-user-code
	}

}
