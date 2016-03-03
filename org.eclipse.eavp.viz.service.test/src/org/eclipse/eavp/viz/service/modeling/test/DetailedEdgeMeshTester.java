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
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.FaceController;
import org.eclipse.eavp.viz.service.modeling.DetailedEdgeController;
import org.eclipse.eavp.viz.service.modeling.DetailedEdgeMesh;
import org.eclipse.eavp.viz.service.modeling.FaceMesh;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.junit.Test;

/**
 * A class to test the functionality of the FaceEdgeMesh
 * 
 * @author Robert Smith
 *
 */
public class DetailedEdgeMeshTester {

	/**
	 * Test that FaceEdgeMeshes can be correctly cloned.
	 */
	@Test
	public void checkClone() {

		// Clone a mesh and check that the result is identical
		DetailedEdgeMesh mesh = new DetailedEdgeMesh();
		mesh.setProperty(MeshProperty.DESCRIPTION, "Property");
		DetailedEdgeMesh clone = (DetailedEdgeMesh) mesh.clone();
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
				new BasicView());
		VertexMesh vertexMesh2 = new VertexMesh(1, 1, 1);
		VertexController vertex2 = new VertexController(vertexMesh2,
				new BasicView());
		edge.addEntityToCategory(vertex1, MeshCategory.VERTICES);
		edge.addEntityToCategory(vertex2, MeshCategory.VERTICES);

		// Clear the edge's updated state
		edge.wasUpdated();

		// Change a vertex and check that the edge was updated
		vertex1.setProperty(MeshProperty.DESCRIPTION, "Value");
		assertTrue(edge.wasUpdated());

		// Add a face to the edge
		FaceMesh faceMesh = new FaceMesh();
		FaceController face = new FaceController(faceMesh, new BasicView());
		edge.addEntityToCategory(face, MeshCategory.FACES);

		// Change the face and check that the edge didn't get updated
		face.setProperty(MeshProperty.DESCRIPTION, "Value");
		assertFalse(edge.wasUpdated());
	}

	/**
	 * Check that the FaceEdgeMesh's equality testing is correct.
	 */
	@Test
	public void checkEquality() {

		// Create two identical edges
		DetailedEdgeMesh edgeMesh1 = new DetailedEdgeMesh();
		DetailedEdgeController edge1 = new DetailedEdgeController(edgeMesh1,
				new BasicView());
		edge1.setProperty(MeshProperty.DESCRIPTION, "True");
		DetailedEdgeMesh edgeMesh2 = new DetailedEdgeMesh();
		DetailedEdgeController edge2 = new DetailedEdgeController(edgeMesh2,
				new BasicView());
		edge2.setProperty(MeshProperty.DESCRIPTION, "True");

		// Check that the two edges are equal
		assertTrue(edge1.equals(edge2));

		// Create two unequal faces
		FaceMesh faceMesh1 = new FaceMesh();
		faceMesh1.setProperty(MeshProperty.DESCRIPTION, "1");
		FaceController face1 = new FaceController(faceMesh1,
				new BasicView());
		FaceMesh faceMesh2 = new FaceMesh();
		faceMesh2.setProperty(MeshProperty.DESCRIPTION, "2");
		FaceController face2 = new FaceController(faceMesh2,
				new BasicView());

		// Add each edge to a face
		edge1.addEntityToCategory(face1, MeshCategory.FACES);
		edge2.addEntityToCategory(face2, MeshCategory.FACES);

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
	private class TestEdge extends DetailedEdgeMesh {

		/**
		 * Whether this class has received an update since the last time it was
		 * checked.
		 */
		private boolean updated = false;

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.eavp.viz.service.modeling.AbstractMesh#update(org.
		 * eclipse. ice.viz.service.datastructures.VizObject.IManagedUpdateable,
		 * org.eclipse.eavp.viz.service.datastructures.VizObject.
		 * SubscriptionType [])
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
