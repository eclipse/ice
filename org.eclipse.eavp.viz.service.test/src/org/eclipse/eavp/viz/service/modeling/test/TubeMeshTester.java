/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.modeling.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.modeling.TubeMesh;
import org.junit.Test;

/**
 * A class for testing the functionality of TubeMesh.
 * 
 * @author Robert Smith
 *
 */
public class TubeMeshTester {

	/**
	 * Check that TubeMeshes are cloned correctly.
	 */
	@Test
	public void checkClone() {

		// Clone a mesh and check that the result is identical
		TubeMesh mesh = new TubeMesh();
		mesh.setProperty("Test", "Property");
		TubeMesh clone = (TubeMesh) mesh.clone();
		assertTrue(mesh.equals(clone));
	}
}
