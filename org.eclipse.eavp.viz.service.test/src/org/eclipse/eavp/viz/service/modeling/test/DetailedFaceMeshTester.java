/*******************************************************************************
 * Copyright (c) 2015-2016 UT-Battelle, LLC.
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.DetailedFaceMesh;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.LinearEdgeMesh;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.junit.Test;

/**
 * A class that tests the functionality of the EdgeAndVertexFaceMesh.
 * 
 * @author Robert Smith
 *
 */
public class DetailedFaceMeshTester {

	/**
	 * Tests that the face adds and removes all child entities in the correct
	 * categories.
	 */
	@Test
	public void checkEntities() {

		// Create the face
		ArrayList<IController> entities = new ArrayList<IController>();
		DetailedFaceMesh face = new DetailedFaceMesh(entities);

		// The children should start out empty.
		assertEquals(0, face.getEntities().size());

		// Create an edge and add it to the face
		VertexController vertex1 = new VertexController(new VertexMesh(0, 0, 0),
				new BasicView());
		VertexController vertex2 = new VertexController(new VertexMesh(1, 1, 1),
				new BasicView());
		EdgeController edge = new EdgeController(
				new LinearEdgeMesh(vertex1, vertex2), new BasicView());
		face.addEntityToCategory(edge, MeshCategory.EDGES);

		// The Edges category should have one object, the edge
		assertTrue(
				face.getEntitiesFromCategory(MeshCategory.EDGES).contains(edge));
		assertEquals(1, face.getEntitiesFromCategory(MeshCategory.EDGES).size());

		// The Vertices category should have two objects, the vertices
		List<IController> vertices = face
				.getEntitiesFromCategory(MeshCategory.VERTICES);
		assertTrue(vertices.contains(vertex1));
		assertTrue(vertices.contains(vertex2));
		assertEquals(2, vertices.size());

		// Add a clone of the edge to the face
		EdgeController edge2 = (EdgeController) edge.clone();
		edge2.setProperty(MeshProperty.ID, "2");
		edge2.getEntitiesFromCategory(MeshCategory.VERTICES).get(0)
				.setProperty(MeshProperty.ID, "2");
		edge2.getEntitiesFromCategory(MeshCategory.VERTICES).get(1)
				.setProperty(MeshProperty.ID, "3");
		face.addEntityToCategory(edge2, MeshCategory.EDGES);

		// Check that there are now two edges and four vertices in the map.
		assertEquals(2, face.getEntitiesFromCategory(MeshCategory.EDGES).size());
		assertEquals(4,
				face.getEntitiesFromCategory(MeshCategory.VERTICES).size());

		// Add a second edge, sharing a vertex with the first, to the face
		VertexController vertex3 = new VertexController(new VertexMesh(2, 2, 2),
				new BasicView());
		EdgeController edge3 = new EdgeController(
				new LinearEdgeMesh(vertex2, vertex3), new BasicView());
		face.addEntityToCategory(edge3, MeshCategory.EDGES);

		// The face should have three edges
		assertEquals(3, face.getEntitiesFromCategory(MeshCategory.EDGES).size());

		// The face should have five vertices, as one of the vertices from the
		// new edge was already present
		assertEquals(5,
				face.getEntitiesFromCategory(MeshCategory.VERTICES).size());

		// Try to remove a vertex. This should fail.
		face.removeEntity(vertex1);
		assertTrue(face.getEntitiesFromCategory(MeshCategory.VERTICES)
				.contains(vertex1));

		// Remove an edge. The non-shared vertex should be taken off with it,
		// but the other should remain
		face.removeEntity(edge);
		assertFalse(
				face.getEntitiesFromCategory(MeshCategory.EDGES).contains(edge));
		assertFalse(face.getEntitiesFromCategory(MeshCategory.VERTICES)
				.contains(vertex1));
		assertTrue(face.getEntitiesFromCategory(MeshCategory.VERTICES)
				.contains(vertex2));

		// Remove vertex2's other edge. This should remove both vertex2 and 3
		face.removeEntity(edge3);
		assertFalse(
				face.getEntitiesFromCategory(MeshCategory.EDGES).contains(edge3));
		assertFalse(face.getEntitiesFromCategory(MeshCategory.VERTICES)
				.contains(vertex2));
		assertFalse(face.getEntitiesFromCategory(MeshCategory.VERTICES)
				.contains(vertex3));
	}

	/**
	 * Check that the part is cloned correctly.
	 */
	@Test
	public void checkClone() {

		// Create a face
		DetailedFaceMesh face = new DetailedFaceMesh();
		face.setProperty(MeshProperty.DESCRIPTION, "Property");

		// Clone it and check that they are identical
		DetailedFaceMesh clone = (DetailedFaceMesh) face.clone();
		assertTrue(face.equals(clone));
	}
}