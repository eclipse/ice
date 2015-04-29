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

import org.eclipse.ice.viz.service.connections.PortEntry;
import org.eclipse.ice.viz.service.connections.PortEntryContentProvider;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This class tests the {@link PortEntry}, which is a special "continuous"
 * {@code Entry} based on integers rather than doubles.
 * 
 * @author Jordan Deyton
 *
 */
@Ignore
public class PortEntryTester {

	// TODO Implement

	/**
	 * This checks the constructor for {@code KeyEntry} to ensure that the
	 * default values are properly set.
	 */
	@Test
	public void checkConstruction() {

	}

	/**
	 * Tests the clone operation to ensure that it properly returns a copied
	 * {@code PortEntry} rather than a plain {@code Entry}.
	 */
	@Test
	public void checkCopy() {

	}

	/**
	 * Checks that the {@link PortEntry#setValue(String)} properly assigns valid
	 * values (integers managed by the {@link PortEntryContentProvider}) and
	 * rejects invalid values (non-integers or integers outside the range).
	 */
	@Test
	public void checkValue() {

	}

}
