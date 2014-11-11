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
package org.eclipse.ice.client.widgets.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.eclipse.ice.client.widgets.EclipseFormWidget;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.datastructures.form.Form;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The FormWidgetTester class tests the FormWidget.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Jay Jay Billings
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EclipseFormWidgetTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TestListener testListener;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Form used for the tests.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Form testForm;
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private EclipseFormWidget eclipseFormWidget;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation is run before the tests to setup the FormWidget and its
	 * listeners.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Before
	public void before() {
		// begin-user-code

		// Allocate the FormWidget
		eclipseFormWidget = new EclipseFormWidget(new ICEFormEditor());

		// Setup the Form
		testForm = new Form();
		testForm.setId(44);
		testForm.setName("Boada");

		// Create the TestListener
		testListener = new TestListener();

		// Register the listener
		eclipseFormWidget.registerProcessListener(testListener);
		eclipseFormWidget.registerUpdateListener(testListener);

		// Set the Form
		this.eclipseFormWidget.setForm(testForm);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getters and setters of the FormWidget to insure
	 * that they store and retrieve a Form properly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkFormManagement() {
		// begin-user-code

		// Local Declarations
		Form retForm = null;

		// Get the Form
		retForm = this.eclipseFormWidget.getForm();

		// Check the Form
		assertNotNull(retForm);
		assertEquals(retForm.getId(), testForm.getId());
		assertEquals(retForm.getName(), testForm.getName());

		return;
		// end-user-code
	}
}