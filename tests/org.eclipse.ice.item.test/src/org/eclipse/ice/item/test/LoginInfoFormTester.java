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

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.item.action.LoginInfoForm;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the LoginInfoForm class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LoginInfoFormTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The LoginInfoForm that is under test.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private LoginInfoForm loginInfoForm;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Form to ensure that it is a Form with single
	 * DataComponent and that the Entries in that component are setup to reflect
	 * usernames and passwords.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkForm() {
		// begin-user-code

		// Local Declarations
		ArrayList<Entry> entries = null;

		// Allocate the login Form
		loginInfoForm = new LoginInfoForm();

		// Check the number of DataComponents
		assertEquals(1, loginInfoForm.getNumberOfComponents());

		// Check the number of Entries
		entries = ((DataComponent) (loginInfoForm.getComponents().get(0)))
				.retrieveAllEntries();
		assertNotNull(entries);
		assertEquals(2, entries.size());

		// Check the default Entry names
		assertEquals("Username", entries.get(0).getName());
		assertEquals("Password", entries.get(1).getName());

		// Make sure that the password Entry is secret
		assertTrue(entries.get(1).isSecret());

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the convenience methods on the LoginInfoForm that
	 * are used to set the description of the Form and the prompt names for the
	 * Entries.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConvenienceMethods() {
		// begin-user-code

		// Local Declarations
		ArrayList<Entry> entries = null;
		Entry username = null, password = null;

		// Allocate the login Form
		loginInfoForm = new LoginInfoForm();

		// Check the number of Entries
		entries = ((DataComponent) (loginInfoForm.getComponents().get(0)))
				.retrieveAllEntries();
		assertNotNull(entries);

		// Check setting the username prompt
		username = entries.get(0);
		loginInfoForm.setUsernamePrompt("Peeps");
		assertEquals("Peeps", username.getName());

		// Check setting the password prompt
		password = entries.get(0);
		loginInfoForm.setUsernamePrompt("Throwing Fire");
		assertEquals("Throwing Fire", password.getName());

		// Check setting the description
		loginInfoForm.setPromptDescription("Prompt Description");
		assertEquals("Prompt Description", loginInfoForm.getDescription());

		return;

		// end-user-code
	}
}