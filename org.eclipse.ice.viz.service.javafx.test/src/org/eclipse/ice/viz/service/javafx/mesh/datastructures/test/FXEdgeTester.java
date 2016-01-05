/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.mesh.datastructures.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.eclipse.ice.viz.service.modeling.EdgeMesh;
import org.eclipse.ice.viz.service.modeling.VertexController;
import org.eclipse.ice.viz.service.modeling.VertexMesh;
import org.junit.Test;

/**
 * A class to test the functionality of the FXEdge
 * 
 * @author Robert Smith
 *
 */
public class FXEdgeTester {

	/**
	 * Test that the edge selects its vertices when it is itself selected.
	 */
	@Test
	public void checkSelection() {

		// Create the edge
		FXMeshControllerFactory factory = new FXMeshControllerFactory();
		EdgeMesh edgeC = new EdgeMesh();
		FXEdgeController edge = (FXEdgeController) factory
				.createController(edgeC);

		// Create a vertex and another part
		AbstractController child = new AbstractController(new AbstractMesh(),
				new AbstractView());
		VertexController vertex = new VertexController(new VertexMesh(),
				new AbstractView());

		// Add the parts to the edge
		edge.addEntity(child);
		edge.addEntity(vertex);

		// Set the edge as constructing
		edge.setProperty("Constructing", "True");

		// The edge should set its vertices as constructing but not its other
		// children
		assertTrue("True".equals(vertex.getProperty("Constructing")));
		assertFalse("True".equals(child.getProperty("Constructing")));

		// Set the edge as selected
		edge.setProperty("Selected", "True");

		// The edge should set its vertices as selected but not its other
		// children
		assertTrue("True".equals(vertex.getProperty("Selected")));
		assertFalse("True".equals(child.getProperty("Selected")));

		// Set a test property
		edge.setProperty("Test", "Value");

		// The edge should not pass any properties but "Selected" to its
		// children
		assertFalse("Value".equals(vertex.getProperty("Test")));
		assertFalse("Value".equals(child.getProperty("Test")));

	}
}
