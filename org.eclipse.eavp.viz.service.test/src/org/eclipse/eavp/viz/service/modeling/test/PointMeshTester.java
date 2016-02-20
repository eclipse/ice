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

import org.eclipse.eavp.viz.service.modeling.PointMesh;
import org.junit.Test;

/**
 * A class to test the functionality of the PointMesh
 * 
 * @author Robert Smith
 *
 */
public class PointMeshTester {

	/**
	 * Test that the PointMesh is cloned correctly
	 */
	@Test
	public void checkClone() {

		// Clone a mesh and check that the result is identical
		PointMesh mesh = new PointMesh();
		mesh.setProperty("Test", "Property");
		PointMesh clone = (PointMesh) mesh.clone();
		assertTrue(mesh.equals(clone));
	}

	/**
	 * Test the point's coordinates.
	 */
	@Test
	public void checkLocation() {

		// Create a point and check that its coordinates are correct.
		PointMesh point = new PointMesh(0, 1, 2);
		assertTrue(Double.compare(0, point.getX()) <= .1);
		assertTrue(Double.compare(1, point.getY()) <= .1);
		assertTrue(Double.compare(2, point.getZ()) <= .1);

		// Check setting the x coordinate
		point.setX(3);
		assertTrue(Double.compare(3, point.getX()) <= .1);

		// Check setting the y coordinate
		point.setY(4);
		assertTrue(Double.compare(4, point.getY()) <= .1);

		// Check setting the z coordinate
		point.setZ(5);
		assertTrue(Double.compare(5, point.getZ()) <= .1);

		// Check setting the location
		point.updateLocation(0, 1, 2);
		double[] location = point.getLocation();
		assertTrue(Double.compare(0, location[0]) <= .1);

	}

}
