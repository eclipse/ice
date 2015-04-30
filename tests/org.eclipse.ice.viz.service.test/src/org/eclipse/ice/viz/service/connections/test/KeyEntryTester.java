/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.connections.test;

import org.eclipse.ice.viz.service.connections.IKeyManager;
import org.eclipse.ice.viz.service.connections.KeyEntry;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class verifies the expected behavior of the {@link KeyEntry} and its
 * interaction with an associated {@link IKeyManager}.
 * 
 * @author Jordan Deyton
 *
 */
@Ignore
public class KeyEntryTester {

	// TODO Implement these tests.

	/**
	 * This checks the constructor for {@code KeyEntry} to ensure that the
	 * default values are properly set.
	 */
	@Test
	public void checkConstruction() {

	}

	/**
	 * Tests the clone operation to ensure that it properly returns a copied
	 * {@code KeyEntry} rather than a plain {@code Entry}.
	 */
	@Test
	public void checkClone() {

	}

	/**
	 * Checks that the {@link KeyEntry#setValue(String)} properly assigns valid
	 * values (valid, unused keys in the associated key manager) and rejects
	 * invalid values (invalid or used keys in the key manager).
	 */
	@Test
	public void checkValue() {

	}

}
