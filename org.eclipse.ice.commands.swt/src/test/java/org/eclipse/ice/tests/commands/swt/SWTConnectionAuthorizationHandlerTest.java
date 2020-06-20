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
package org.eclipse.ice.tests.commands.swt;

import java.util.Arrays;

import org.eclipse.ice.commands.swt.SWTConnectionAuthorizationHandler;
import org.junit.Test;

/**
 * This class implements test functionality for the
 * SWTCommandAuthorizationHandler class
 * 
 * @author Joe Osborn
 *
 */
public class SWTConnectionAuthorizationHandlerTest {

	/**
	 * Default constructor
	 */
	public SWTConnectionAuthorizationHandlerTest() {
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.SWTCommandAuthorizationHandler#getPassword()|
	 */
	@Test
	public void testGetPassword() {
		System.out.println("Enter 'password' into the SWT GUI when prompted");
		SWTConnectionAuthorizationHandler swt = new SWTConnectionAuthorizationHandler();
		
		char[] swtPassword = swt.getPassword();
		// Make an array to compare to
		char[] password = new char[] { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };
				
		assert(Arrays.equals(swtPassword, password));
	}

}
