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
package org.eclipse.ice.viz.service.modeling.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Edge;
import org.eclipse.ice.viz.service.modeling.EdgeAndVertexFaceComponent;
import org.eclipse.ice.viz.service.modeling.LinearEdgeComponent;
import org.eclipse.ice.viz.service.modeling.Vertex;
import org.eclipse.ice.viz.service.modeling.VertexComponent;
import org.junit.Test;

/**
 * A class that tests the functionality of the EdgeAndVertexFaceComponent.
 * 
 * @author Robert Smith
 *
 */
public class EdgeAndVertexFaceComponentTester {

	/**
	 * Tests that the face adds all child entities to the correct categories.
	 */
	@Test
	public void checkAddEntities() {

		// Create the face
		List<AbstractController> entities = new ArrayList<AbstractController>();
		EdgeAndVertexFaceComponent face = new EdgeAndVertexFaceComponent(
				entities);

		// The children should start out empty.
		assertEquals(0, face.getEntities().size());

		// Create an edge and add it to the face
		Vertex vertex1 = new Vertex(new VertexComponent(0, 0, 0),
				new AbstractView());
		Vertex vertex2 = new Vertex(new VertexComponent(1, 1, 1),
				new AbstractView());
		Edge edge = new Edge(new LinearEdgeComponent(vertex1, vertex2),
				new AbstractView());
		face.addEntity(edge);

		// The Edges category should have one object, the edge
		assertTrue(face.getEntitiesByCategory("Edges").contains(edge));
		assertEquals(1, face.getEntitiesByCategory("Edges").size());

		// The Vertices category should have two objects, the vertices
		List<AbstractController> vertices = face
				.getEntitiesByCategory("Vertices");
		assertTrue(vertices.contains(vertex1));
		assertTrue(vertices.contains(vertex2));
		assertEquals(2, vertices.size());

		// Add a clone of the edge to the face
		Edge edge2 = (Edge) edge.clone();
		face.addEntity(edge2);

		// Check that there are now two edges and four vertices in the map.
		assertEquals(2, face.getEntitiesByCategory("Edges").size());
		assertEquals(4, face.getEntitiesByCategory("Vertices").size());

	}
}
