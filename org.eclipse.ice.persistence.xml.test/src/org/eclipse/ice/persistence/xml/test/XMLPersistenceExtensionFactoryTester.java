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
package org.eclipse.ice.persistence.xml.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.persistence.xml.XMLPersistenceExtensionFactory;
import org.eclipse.ice.persistence.xml.XMLPersistenceProvider;
import org.junit.Test;

/**
 * This class is responsible for testing
 * {@link org.eclipse.ice.xml.XMLPersistenceExtensionFactory}.
 * 
 * @author Jay Jay Billings
 *
 */
public class XMLPersistenceExtensionFactoryTester {

	/**
	 * Test method for
	 * {@link org.eclipse.ice.persistence.xml.XMLPersistenceExtensionFactory#create()}
	 * .
	 */
	@Test
	public void testCreate() {

		// All this test can do - at least at the moment - is make sure that the
		// factory returns a non-null instance of an XMLPersistenceProvider. It
		// isn't clear how to test it for more detailed behavior yet.
		XMLPersistenceExtensionFactory factory = new XMLPersistenceExtensionFactory();
		Object provider;
		try {
			provider = factory.create();
			assertNotNull(provider);
			// Remember that the factory returns a Provider from create(), not
			// an instance of itself!
			assertTrue(provider instanceof XMLPersistenceProvider);
		} catch (CoreException e) {
			// It can also fail if the exception is caught, so we test that too.
			e.printStackTrace();
			fail();
		}
		return;
	}

}
