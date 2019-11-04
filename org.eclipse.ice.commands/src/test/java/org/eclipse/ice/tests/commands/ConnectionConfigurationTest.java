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
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.ConnectionConfiguration}.
 * This class is a POJO so there are only some getter/setters to test
 * 
 * @author Joe Osborn, Jay Jay Billings
 *
 */
public class ConnectionConfigurationTest {

	/**
	 * Test some getters and setters. ConnectionConfiguration is a POJO so not much
	 * to test
	 */
	@Test
	public void testConstructor() {
		ConnectionConfiguration config = new ConnectionConfiguration();

		// Check that things are set to blank by default
		assert (config.getName().equals(""));

		// Check that this defaults to false
		assert (config.deleteWorkingDirectory() == false);

	}
}
