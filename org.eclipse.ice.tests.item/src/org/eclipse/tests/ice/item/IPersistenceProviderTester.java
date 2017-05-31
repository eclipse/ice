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
package org.eclipse.tests.ice.item;

import static org.junit.Assert.assertNotNull;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.item.persistence.IPersistenceProvider;
import org.eclipse.ice.persistence.xml.XMLPersistenceExtensionFactory;
import org.junit.Test;

/**
 * This class tests the static interface operation
 * IPersistenceProvider.getIPersistenceProviders().
 *
 * @author Jay Jay Billings
 *
 */
public class IPersistenceProviderTester {

	/**
	 * Test for {@link org.eclipse.ice.io.IPersistenceProvider.getProvider}.
	 *
	 * @throws CoreException
	 */
	@Test
	public void test() throws CoreException {

		// We need to load a class in the XML Persistence Provider bundle so
		// that it will activate and the extensions will register with the
		// framework. This is a hack that I don't like, but previously it wasn't
		// needed because the OSGi bundles were started automatically by Tycho.
		XMLPersistenceExtensionFactory factory = new XMLPersistenceExtensionFactory();
		// Simply get the provider and make sure it is not null.
		IPersistenceProvider provider = IPersistenceProvider.getProvider();
		assertNotNull(provider);
		return;
	}

}
