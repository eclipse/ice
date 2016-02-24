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

import org.eclipse.eavp.viz.service.modeling.FaceMesh;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.junit.Test;

/**
 * A class for testing the functionality of FaceMesh.
 * 
 * @author Robert Smith
 *
 */
public class FaceMeshTester {

	/**
	 * Check that FaceMeshes are cloned correctly.
	 */
	@Test
	public void checkClone() {

		// Clone a mesh and check that the result is identical
		FaceMesh mesh = new FaceMesh();
		mesh.setProperty(MeshProperty.DESCRIPTION, "Property");
		FaceMesh clone = (FaceMesh) mesh.clone();
		assertTrue(mesh.equals(clone));
	}
}
