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

import java.util.List;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMeshComponent;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.Edge;
import org.eclipse.ice.viz.service.modeling.EdgeComponent;
import org.eclipse.ice.viz.service.modeling.Vertex;
import org.eclipse.ice.viz.service.modeling.VertexComponent;

/**
 * A class which tests the functionality of Vertex
 * 
 * @author Robert Smith
 *
 */
public class VertexTester {

	/**
	 * Tests the Vertex's ability to correctly manage its edges
	 */
	public void checkEdges() {

		// Create a vertex
		VertexComponent vertexModel = new VertexComponent();
		AbstractView view = new AbstractView();
		Vertex vertex = new Vertex(vertexModel, view);

		// Add an entity and check that it did not go into the edges category
		vertex.addEntity(new AbstractController(new AbstractMeshComponent(),
				new AbstractView()));
		assertEquals(0, vertex.getEntitiesByCategory("Edges").size());

		// Create some edges
		Edge edge1 = new Edge(new EdgeComponent(), new AbstractView());
		Edge edge2 = new Edge(new EdgeComponent(), new AbstractView());
		Edge edge3 = new Edge(new EdgeComponent(), new AbstractView());

		// Add two edges to the vertex and a thrid explicitly under a different
		// category
		vertex.addEntity(edge1);
		vertex.addEntity(edge2);
		vertex.addEntityByCategory(edge3, "test");

		// The first two edges should go into the Edges category
		List<AbstractController> edges = vertex.getEntitiesByCategory("Edges");
		assertTrue(edges.contains(edge1));
		assertTrue(edges.contains(edge2));

		// The last edge should have been put in the specified custom category
		assertTrue(vertex.getEntitiesByCategory("test").contains(edge3));
	}
}
