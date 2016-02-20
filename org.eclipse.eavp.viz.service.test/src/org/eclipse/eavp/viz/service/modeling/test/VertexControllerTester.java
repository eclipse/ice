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
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
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
				new AbstractView());
		vertex.setProperty("Test", "Property");
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
		AbstractView view = new AbstractView();
		VertexController vertex = new VertexController(vertexModel, view);

		// Add an entity and check that it did not go into the edges category
		vertex.addEntity(
				new AbstractController(new AbstractMesh(), new AbstractView()));
		assertEquals(0, vertex.getEntitiesByCategory("Edges").size());

		// Create some edges
		EdgeController edge1 = new EdgeController(new EdgeMesh(),
				new AbstractView());
		EdgeController edge2 = new EdgeController(new EdgeMesh(),
				new AbstractView());
		EdgeController edge3 = new EdgeController(new EdgeMesh(),
				new AbstractView());

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

	/**
	 * Test that the Vertex properly updates
	 */
	@Test
	public void checkUpdates() {

		// Create the vertex
		VertexMesh vertexMesh = new VertexMesh(0, 0, 0);
		TestVertexController vertex = new TestVertexController(vertexMesh,
				new AbstractView());

		// Create a test object to receive updates
		TestMesh otherMesh = new TestMesh(new ArrayList<AbstractController>());
		TestController other = new TestController(otherMesh,
				new AbstractView());

		// Create a second vertex for the edge
		VertexMesh otherVertexMesh = new VertexMesh(1, 1, 1);
		VertexController otherVertex = new VertexController(otherVertexMesh,
				new AbstractView());

		// Create an edge
		EdgeMesh edgeMesh = new EdgeMesh(vertex, otherVertex);
		EdgeController edge = new EdgeController(edgeMesh, new AbstractView());

		// Add the test object and edge to the vertex.
		vertex.addEntityByCategory(other, "Test");
		vertex.addEntityByCategory(edge, "Edges");

		// Clear the vertex's updated state
		vertex.wasUpdated();

		// The vertex should receive updates from child entities by default
		other.setProperty("Send", "Update");
		assertTrue(vertex.wasUpdated());

		// The vertex should not receive updates from objects in the Edges
		// category.
		edge.setProperty("Send", "Update");
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
		public TestVertexController(VertexMesh model, AbstractView view) {
			super(model, view);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.eavp.viz.service.modeling.AbstractController#update(org.
		 * eclipse.ice.viz.service.datastructures.VizObject.IManagedUpdateable,
		 * org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType
		 * [])
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