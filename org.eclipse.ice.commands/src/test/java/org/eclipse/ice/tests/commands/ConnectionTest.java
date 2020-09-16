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

import static org.junit.Assert.assertNull;

import org.eclipse.ice.commands.Connection;
import org.junit.Test;

/**
 * This class tests {@link org.eclipse.ice.commands.Connection}. Connection is
 * mostly a POJO so there are only some getters/setters to test
 * 
 * @author Joe Osborn, Jay Jay Billings
 *
 */
public class ConnectionTest {

	/**
	 * Test method for {@link org.eclipse.ice.commands.Connection#Connection()}.
	 */
	@Test
	public void testConnection() {
		Connection connection = new Connection();

		// Check that the default connection leaves the configuration empty
		assertNull(connection.getConfiguration());

		assertNull(connection.getSession());
	}

}
