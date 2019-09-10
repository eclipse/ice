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

import org.eclipse.ice.commands.ConnectionConfiguration;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.ConnectionConfiguration}.
 * 
 * @author Joe Osborn, Jay Jay Billings
 *
 */
public class ConnectionConfigurationTest {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.commands.ConnectionConfiguration#promptPassword(String)}.
	 */
	@Test
	public void testPromptPassword() {

		ConnectionConfiguration config = new ConnectionConfiguration();

		config.promptPassword("Enter the password, which is 'password'");

		assert (config.getPassword().equals("password"));

	}
}
