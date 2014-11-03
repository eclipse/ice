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

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import org.eclipse.ice.client.widgets.EclipseFormWidget;
import org.eclipse.ice.client.widgets.ICEFormEditor;
import org.eclipse.ice.datastructures.form.Form;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the ICEFormEditor.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ICEFormEditorTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ICEFormEditor ICEFormEditor;

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
	 * This operation is run before the tests to setup the ICEFormEditor.
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
		ICEFormEditor = new ICEFormEditor();

		// Create the TestListener
		testListener = new TestListener();

		// Register the listener
		ICEFormEditor.registerProcessListener(testListener);
		ICEFormEditor.registerUpdateListener(testListener);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ICEFormEditor to make sure that it can properly
	 * handle update event notifications.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkUpdateEvents() {
		// begin-user-code

		// Test updates 21 times
		for (int i = 0; i < 21; i++) {
			// Fire an update
			ICEFormEditor.notifyUpdateListeners();

			// Check for the update
			assertTrue(testListener.wasUpdated());

			// Reset the listener
			testListener.reset();
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ICEFormEditor to make sure that it can properly
	 * handle update event notifications.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkProcessEvents() {
		// begin-user-code

		// Test processing 23 times
		for (int i = 0; i < 23; i++) {
			// Fire an update
			ICEFormEditor.notifyProcessListeners("disorganize");

			// Check for the update
			assertTrue(testListener.wasProcessed());

			// Reset the listener
			testListener.reset();
		}

		// FIXME! - How should cancellation be tested?

		return;
		// end-user-code
	}
}