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
package org.eclipse.eavp.viz.service.geometry.reactor.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.junit.Test;

/**
 * A class to test the functionality of the PipeMesh
 * 
 * @author Robert Smith
 *
 */
public class PipeMeshTester {

	/**
	 * Test the PipeMesh convenience methods and properties management.
	 */
	@Test
	public void checkProperties() {

		// Create a pipe
		PipeMesh pipe = new PipeMesh();

		// Check the number of rods
		pipe.setNumRods(1);

		// Check the pitch
		pipe.setPitch(2d);
		assertTrue(pipe.getPitch() == 2d);

		// Check the rod diameter
		pipe.setRodDiameter(3d);
		assertTrue(pipe.getRodDiameter() == 3d);

		// Set the radius and inner radius to different values
		pipe.setRadius(5d);
		pipe.setInnerRadius(4d);

		// Check that the radius is set and that the inner radius simply
		// redirects to the outer radius, as pipes are to be displayed as
		// infinitely thin.
		assertTrue(pipe.getRadius() == 5d);
		assertTrue(pipe.getInnerRadius() == 5d);
	}

	/**
	 * Check that the part is cloned correctly.
	 */
	@Test
	public void checkClone() {

		// Create a junction
		PipeMesh pipe = new PipeMesh();
		pipe.setProperty(MeshProperty.ID, "Property");

		// Clone it and check that they are identical
		PipeMesh clone = (PipeMesh) pipe.clone();
		assertTrue(pipe.equals(clone));
	}
}
