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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.datastructures.VizObject.IManagedUpdateable;
import org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.FaceController;
import org.eclipse.eavp.viz.service.modeling.FaceEdgeController;
import org.eclipse.eavp.viz.service.modeling.FaceEdgeMesh;
import org.eclipse.eavp.viz.service.modeling.FaceMesh;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.junit.Test;

/**
 * A class to test the functionality of the FaceEdgeMesh
 * 
 * @author Robert Smith
 *
 */
public class FaceEdgeMeshTester {

	/**
	 * Test that FaceEdgeMeshes can be correctly cloned.
	 */
	@Test
	public void checkClone() {

		// Clone a mesh and check that the result is identical
		FaceEdgeMesh mesh = new FaceEdgeMesh();
		mesh.setProperty("Test", "Property");
		FaceEdgeMesh clone = (FaceEdgeMesh) mesh.clone();
		assertTrue(mesh.equals(clone));
	}

	/**
	 * Check that a FaceEdgeMesh does not register as a listener to the Faces it
	 * belongs to.
	 */
	@Test
	public void checkUpdates() {

		// Create an edge
		TestEdge edge = new TestEdge();
		VertexMesh vertexMesh1 = new VertexMesh(0, 0, 0);
		VertexController vertex1 = new VertexController(vertexMesh1,
				new AbstractView());
		VertexMesh vertexMesh2 = new VertexMesh(1, 1, 1);
		VertexController vertex2 = new VertexController(vertexMesh2,
				new AbstractView());
		edge.addEntityByCategory(vertex1, "Vertices");
		edge.addEntityByCategory(vertex2, "Vertices");

		// Clear the edge's updated state
		edge.wasUpdated();

		// Change a vertex and check that the edge was updated
		vertex1.setProperty("Test", "Value");
		assertTrue(edge.wasUpdated());

		// Add a face to the edge
		FaceMesh faceMesh = new FaceMesh();
		FaceController face = new FaceController(faceMesh, new AbstractView());
		edge.addEntityByCategory(face, "Faces");

		// Change the face and check that the edge didn't get updated
		face.setProperty("Test", "Value");
		assertFalse(edge.wasUpdated());
	}

	/**
	 * Check that the FaceEdgeMesh's equality testing is correct.
	 */
	@Test
	public void checkEquality() {

		// Create two identical edges
		FaceEdgeMesh edgeMesh1 = new FaceEdgeMesh();
		FaceEdgeController edge1 = new FaceEdgeController(edgeMesh1,
				new AbstractView());
		edge1.setProperty("Equal", "True");
		FaceEdgeMesh edgeMesh2 = new FaceEdgeMesh();
		FaceEdgeController edge2 = new FaceEdgeController(edgeMesh2,
				new AbstractView());
		edge2.setProperty("Equal", "True");

		// Check that the two edges are equal
		assertTrue(edge1.equals(edge2));

		// Create two unequal faces
		FaceMesh faceMesh1 = new FaceMesh();
		faceMesh1.setProperty("ID", "1");
		FaceController face1 = new FaceController(faceMesh1,
				new AbstractView());
		FaceMesh faceMesh2 = new FaceMesh();
		faceMesh2.setProperty("ID", "2");
		FaceController face2 = new FaceController(faceMesh2,
				new AbstractView());

		// Add each edge to a face
		edge1.addEntityByCategory(face1, "Faces");
		edge2.addEntityByCategory(face2, "Faces");

		// Edges should be equal regardless of having different parents
		assertTrue(edge1.equals(edge2));
	}

	/**
	 * A simple extension of FaceEdgeMesh that stores whether or not it has
	 * received an update.
	 * 
	 * @author Robert Smith
	 *
	 */
	private class TestEdge extends FaceEdgeMesh {

		/**
		 * Whether this class has received an update since the last time it was
		 * checked.
		 */
		private boolean updated = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.eavp.viz.service.modeling.AbstractMesh#update(org.eclipse.
		 * ice.viz.service.datastructures.VizObject.IManagedUpdateable,
		 * org.eclipse.eavp.viz.service.datastructures.VizObject.SubscriptionType
		 * [])
		 */
		@Override
		public void update(IManagedUpdateable component,
				SubscriptionType[] type) {
			updated = true;
		}

		/**
		 * Checks if the object was updated and resets it to its orinigal state.
		 * 
		 * @return True if the object has received an update since the last time
		 *         this function was invoked. False otherwise.
		 */
		public boolean wasUpdated() {
			boolean temp = updated;
			updated = false;
			return temp;
		}
	}
}
