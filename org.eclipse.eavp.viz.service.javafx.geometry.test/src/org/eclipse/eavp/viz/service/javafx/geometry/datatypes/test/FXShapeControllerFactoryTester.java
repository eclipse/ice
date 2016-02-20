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
package org.eclipse.eavp.viz.service.javafx.geometry.datatypes.test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXShapeController;
import org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXShapeControllerFactory;
import org.eclipse.eavp.viz.service.javafx.geometry.datatypes.FXShapeView;
import org.eclipse.eavp.viz.service.modeling.EdgeMesh;
import org.eclipse.eavp.viz.service.modeling.ShapeMesh;
import org.junit.Test;

/**
 * A class for testing the functionality of the FXShapeControllerFactory
 * 
 * @author Robert Smith
 *
 */
public class FXShapeControllerFactoryTester {

	/**
	 * Check that the factory creates appropriate controllers and views for each
	 * type of input.
	 */
	@Test
	public void checkCreation() {

		// The factory to be tested
		FXShapeControllerFactory factory = new FXShapeControllerFactory();

		// Create a mesh and send it to the factory
		ShapeMesh shapeMesh = new ShapeMesh();
		FXShapeController shapeController = (FXShapeController) factory
				.createController(shapeMesh);

		// The resultant controller should have the mesh as its model and a
		// FXShapeView for a view
		assertTrue(shapeController.getModel() == shapeMesh);
		assertTrue(shapeController.getView() instanceof FXShapeView);

		// Try to send an unrecognized input mesh to the factory
		EdgeMesh edgeMesh = new EdgeMesh();
		assertNull(factory.createController(edgeMesh));

	}
}
