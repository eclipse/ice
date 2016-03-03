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

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestController;
import org.eclipse.eavp.viz.service.modeling.test.utils.TestMesh;
import org.junit.Test;

/**
 * A class which tests the functionality of Vertex
 * 
 * @author Robert Smith
 *
 */
public class VertexControllerTester {

	/**
	 * Check that VertexControllers can be properly cloned.
	 */
	@Test
	public void checkClone() {

		// Create a cloned vertex and check that it is identical to the original
		VertexController vertex = new VertexController(new VertexMesh(),
				new BasicView());
		vertex.setProperty(MeshProperty.DESCRIPTION, "Property");
		VertexController clone = (VertexController) vertex.clone();
		assertTrue(vertex.equals(clone));
	}

	/**
	 * Tests the Vertex's ability to correctly manage its edges
	 */
	@Test
	public void checkEdges() {

		// Create a vertex
		VertexMesh vertexModel = new VertexMesh();
		BasicView view = new BasicView();
		VertexController vertex = new VertexController(vertexModel, view);

		// Add an entity and check that it did not go into the edges category
		vertex.addEntity(
				new BasicController(new BasicMesh(), new BasicView()));
		assertEquals(0,
				vertex.getEntitiesFromCategory(MeshCategory.EDGES).size());

		// Create some edges
		EdgeController edge1 = new EdgeController(new EdgeMesh(),
				new BasicView());
		EdgeController edge2 = new EdgeController(new EdgeMesh(),
				new BasicView());
		EdgeController edge3 = new EdgeController(new EdgeMesh(),
				new BasicView());

		// Add two edges to the vertex and a third explicitly under a different
		// category
		vertex.addEntity(edge1);
		vertex.addEntity(edge2);
		vertex.addEntityToCategory(edge3, MeshCategory.CHILDREN);

		// The first two edges should go into the Edges category
		List<IController> edges = vertex
				.getEntitiesFromCategory(MeshCategory.EDGES);
		assertTrue(edges.contains(edge1));
		assertTrue(edges.contains(edge2));

		// The last edge should have been put in the specified custom category
		assertTrue(vertex.getEntitiesFromCategory(MeshCategory.CHILDREN)
				.contains(edge3));
	}

	/**
	 * Test that the Vertex properly updates
	 */
	@Test
	public void checkUpdates() {

		// Create the vertex
		VertexMesh vertexMesh = new VertexMesh(0, 0, 0);
		TestVertexController vertex = new TestVertexController(vertexMesh,
				new BasicView());

		// Create a test object to receive updates
		TestMesh otherMesh = new TestMesh(new ArrayList<IController>());
		TestController other = new TestController(otherMesh,
				new BasicView());

		// Create a second vertex for the edge
		VertexMesh otherVertexMesh = new VertexMesh(1, 1, 1);
		VertexController otherVertex = new VertexController(otherVertexMesh,
				new BasicView());

		// Create an edge
		EdgeMesh edgeMesh = new EdgeMesh(vertex, otherVertex);
		EdgeController edge = new EdgeController(edgeMesh, new BasicView());

		// Add the test object and edge to the vertex.
		vertex.addEntityToCategory(other, MeshCategory.CHILDREN);
		vertex.addEntityToCategory(edge, MeshCategory.EDGES);

		// Clear the vertex's updated state
		vertex.wasUpdated();

		// The vertex should receive updates from child entities by default
		other.setProperty(MeshProperty.DESCRIPTION, "Update");
		assertTrue(vertex.wasUpdated());

		// The vertex should not receive updates from objects in the Edges
		// category.
		edge.setProperty(MeshProperty.DESCRIPTION, "Update");
		assertFalse(vertex.wasUpdated());
	}

	/**
	 * An extension of VertexController that tracks whether it has been updated.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestVertexController extends VertexController {

		/**
		 * Whether or not this object has received an update since the last time
		 * it was checked for an update.
		 */
		boolean updated;

		/**
		 * The defualt constructor.
		 * 
		 * @param model
		 *            The internal representation of the part.
		 * @param view
		 *            The graphical representation of the part.
		 */
		public TestVertexController(VertexMesh model, BasicView view) {
			super(model, view);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.eavp.viz.service.modeling.AbstractController#update(org.
		 * eclipse.ice.viz.service.datastructures.VizObject.IManagedUpdateable,
		 * org.eclipse.eavp.viz.service.datastructures.VizObject.
		 * SubscriptionType [])
		 */
		@Override
		public void update(IManagedUpdateable component,
				SubscriptionType[] type) {

			// The object has received an updated
			updated = true;

			super.update(component, type);
		}

		/**
		 * Checks whether this object has received an update since the last time
		 * it was checked for an update.
		 * 
		 * @return True if an update was received since the last time this
		 *         method was invoked. False otherwise.
		 */
		public boolean wasUpdated() {
			boolean temp = updated;
			updated = false;
			return temp;
		}
	}
}