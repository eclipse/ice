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

import org.junit.Test;
import org.eclipse.ice.datastructures.form.DataComponent;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.item.action.LoginInfoForm;
import org.eclipse.ice.item.action.ICEJschUIInfo;

/**
 * <p>
 * This class is responsible for testing the ICEJschUIInfo class.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ICEJschUIInfoTester {
	/**
	 * 
	 */
	private ICEJschUIInfo iCEJschUIInfo;

	/**
	 * <p>
	 * This operation ensures that the ICEJschUIInfo class can retrieve a
	 * password from the LoginInfoForm.
	 * </p>
	 * 
	 */
	@Test
	public void checkPassword() {

		// Local Declarations
		LoginInfoForm testForm = new LoginInfoForm();
		DataComponent comp = null;
		Entry passwordEntry = null;
		String password = null;
		String[] keyboardInteractiveArray = null, inputArray = { "D", "E", "F" };

		// Initialize the UIInfo class
		iCEJschUIInfo = new ICEJschUIInfo();

		// Check the promptPassword() operation before setting the password
		assertTrue(!iCEJschUIInfo.promptPassword("Let me just be..."));

		// Make sure the ui info is not ready as well
		assertTrue(!iCEJschUIInfo.isReady());

		// Set the password
		comp = (DataComponent) testForm.getComponent(1);
		passwordEntry = comp.retrieveEntry("Password");
		passwordEntry.setValue("Georgia");
		iCEJschUIInfo.setForm(testForm);

		// The ui info should now be ready
		assertTrue(iCEJschUIInfo.isReady());

		// Check the promptPassword() operation after setting the password
		assertTrue(iCEJschUIInfo.promptPassword("Over cigarettes"));

		// Get the password
		assertNotNull(iCEJschUIInfo.getPassword());
		assertEquals("Georgia", iCEJschUIInfo.getPassword());

		// Some of the return values are not used and should be null or true
		assertNull(iCEJschUIInfo.getPassphrase());
		assertTrue(iCEJschUIInfo.promptPassphrase("Early in the morning."));
		assertTrue(!iCEJschUIInfo.promptYesNo("Help me to enjoy..."));

		// Get the password through the keyboard interactive operation
		keyboardInteractiveArray = iCEJschUIInfo.promptKeyboardInteractive("A",
				"B", "C", inputArray, null);
		assertNotNull(keyboardInteractiveArray);
		// There should only be on entry in the array
		assertEquals(1, keyboardInteractiveArray.length);
		assertEquals("Georgia", keyboardInteractiveArray[0]);

		return;
	}
}