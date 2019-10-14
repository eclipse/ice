/*******************************************************************************
 * Copyright (c) 2019- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Joe Osborn
 *******************************************************************************/
package org.eclipse.ice.tests.commands;

import java.util.Arrays;

import org.eclipse.ice.commands.ConsoleCommandAuthorizationHandler;
import org.junit.Test;

/**
 * This class implements test functinoality for the ConsoleCommandAuthorization
 * class
 * 
 * @author Joe Osborn
 *
 */
public class ConsoleCommandAuthorizationHandlerTest {

	/**
	 * Default constructor
	 */
	public ConsoleCommandAuthorizationHandlerTest() {
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.ConsoleCommandAuthorizationHandler#getPassword()}
	 * Tests password collection via the console screen, using the console eraser class
	 * to recursively remove characters from the screen.  For the test to pass, the user
	 * needs to type "password" at the prompt
	 */
	@Test
	public void testGetPassword() {
		System.out.println("Type the word 'password' when prompted");

		ConsoleCommandAuthorizationHandler console = new ConsoleCommandAuthorizationHandler();
		// Make an array to compare to
		char[] password = new char[] { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };
		// Get the password from the console
		char[] pass = console.getPassword();
		// Check that they are the same
		assert (Arrays.equals(pass, password));

	}

}
