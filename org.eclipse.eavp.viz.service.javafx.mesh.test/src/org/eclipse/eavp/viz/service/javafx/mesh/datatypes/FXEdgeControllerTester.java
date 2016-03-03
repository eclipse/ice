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
package org.eclipse.eavp.viz.service.javafx.mesh.datatypes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.mesh.datastructures.MeshEditorMeshProperty;
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.BasicMesh;
import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.MeshCategory;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.junit.Test;

/**
 * A class for testing the functionality of the FXEdgeController
 * 
 * @author Robert Smith
 *
 */
public class FXEdgeControllerTester {

	/**
	 * Check that FXEdgeControllers can be properly cloned.
	 */
	@Test
	public void checkClone() {

		// Create a cloned FXShape and check that it is identical to the
		// original
		EdgeMesh mesh = new EdgeMesh();
		FXEdgeController edge = new FXEdgeController(mesh,
				new FXLinearEdgeView(mesh));
		edge.setProperty(MeshProperty.INNER_RADIUS, "Property");
		FXEdgeController clone = (FXEdgeController) edge.clone();
		assertTrue(edge.equals(clone));
	}

	/**
	 * Check that the controller propagates properties changes to its children
	 * correctly.
	 */
	@Test
	public void checkProperties() {

		// Create an edge for testing
		EdgeMesh mesh = new EdgeMesh();
		FXEdgeController edge = new FXEdgeController(mesh, new BasicView());

		// Give a child entity to the edge
		BasicController child = new BasicController(new BasicMesh(),
				new BasicView());
		edge.addEntityToCategory(child, MeshCategory.VERTICES);

		// Set the Constructing property. This change should be mirrored in the
		// edge's children.
		edge.setProperty(MeshEditorMeshProperty.UNDER_CONSTRUCTION, "True");
		assertTrue("True"
				.equals(child.getProperty(MeshEditorMeshProperty.UNDER_CONSTRUCTION)));

		// Set the Selected property. This changed should be mirrored in the
		// edge's children
		edge.setProperty(MeshProperty.SELECTED, "True");
		assertTrue("True".equals(child.getProperty(MeshProperty.SELECTED)));

		// Set a different property. This change should not be reflected in the
		// child
		edge.setProperty(MeshProperty.INNER_RADIUS, "Property");
		assertFalse(
				"Property".equals(child.getProperty(MeshProperty.INNER_RADIUS)));
	}
}
