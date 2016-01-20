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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.ice.datastructures.entry.IEntry;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.item.action.LoginInfoForm;
import org.junit.Test;

/**
 * <p>
 * This class is responsible for testing the LoginInfoForm class.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class LoginInfoFormTester {
	/**
	 * <p>
	 * The LoginInfoForm that is under test.
	 * </p>
	 * 
	 */
	private LoginInfoForm loginInfoForm;

	/**
	 * <p>
	 * This operation checks the Form to ensure that it is a Form with single
	 * DataComponent and that the Entries in that component are setup to reflect
	 * usernames and passwords.
	 * </p>
	 * 
	 */
	@Test
	public void checkForm() {

		// Local Declarations
		ArrayList<IEntry> entries = null;

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

	}

	/**
	 * <p>
	 * This operation checks the convenience methods on the LoginInfoForm that
	 * are used to set the description of the Form and the prompt names for the
	 * Entries.
	 * </p>
	 * 
	 */
	@Test
	public void checkConvenienceMethods() {

		// Local Declarations
		ArrayList<IEntry> entries = null;
		IEntry username = null, password = null;

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

	}
}