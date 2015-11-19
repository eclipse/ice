/*******************************************************************************
 * Copyright (c) 2013, 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.client.test;

import static org.junit.Assert.assertNotNull;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.iclient.IClient;
import org.junit.Test;

/**
 * This class tests the static interface operation
 * IClient.getIClients().
 *
 * @author Jay Jay Billings
 *
 */
public class IClientTester {

	/**
	 * Test for {@link org.eclipse.ice.item.IClient}.
	 *
	 * @throws CoreException
	 */
	@Test
	public void test() throws CoreException {
		// Simply get the client from the registry and make sure it is actually
		// there.
		IClient client = IClient.getClient();
		assertNotNull(client);
		return;
	}

}
