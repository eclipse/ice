/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.modeling.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.LinearEdgeMesh;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.junit.Test;

/**
 * A class to test LinearEdge's functionality
 * 
 * @author Robert Smith
 *
 */
public class LinearEdgeMeshTester {

	/**
	 * Check that LinearEdgeMeshes are cloned correctly.
	 */
	@Test
	public void checkClone() {

		// Clone a mesh and check that the result is identical
		LinearEdgeMesh mesh = new LinearEdgeMesh();
		mesh.setProperty("Test", "Property");
		LinearEdgeMesh clone = (LinearEdgeMesh) mesh.clone();
		assertTrue(mesh.equals(clone));
	}

	/**
	 * Tests the line's length as the vertices are changed
	 */
	@Test
	public void checkLength() {
		// Create the edge
		LinearEdgeMesh edge = new LinearEdgeMesh();

		// The edge should initially have length 0
		assertEquals(0, Double.compare(edge.getLength(), 0d));

		// Create some vertices
		VertexController vertex1 = new VertexController(new VertexMesh(0, 0, 0),
				new AbstractView());
		VertexController vertex2 = new VertexController(new VertexMesh(1, 1, 1),
				new AbstractView());
		VertexController vertex3 = new VertexController(new VertexMesh(2, 2, 2),
				new AbstractView());

		// Add the first two vertices to the edge.
		edge.addEntityByCategory(vertex1, "Vertices");
		edge.addEntityByCategory(vertex2, "Vertices");

		// Check that the edge has the correct length
		assertTrue(Double.compare(1.73, edge.getLength()) <= .1d);

		// Replace the second vertex with the third
		edge.removeEntity(vertex2);
		edge.addEntityByCategory(vertex3, "Vertices");

		// Check that the edge's length has been updated
		assertTrue(Double.compare(3.46, edge.getLength()) <= .1d);

	}
}
