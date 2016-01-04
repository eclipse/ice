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
package org.eclipse.ice.viz.service.javafx.mesh.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.viz.service.javafx.mesh.FXMeshVizService;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.AbstractView;
import org.junit.Test;

/**
 * A class to test the FXMeshVizService's functionality
 * 
 * @author Robert Smith
 *
 */
public class FXMeshVizServiceTester {

	/**
	 * Test that the service's properties are correct
	 */
	@Test
	public void checkProperties() {
		FXMeshVizService service = new FXMeshVizService();

		// The service should have the correct name
		assertTrue("ICE JavaFX Mesh Editor".equals(service.getName()));

		// The service should not support any file extensions
		assertTrue(service.getSupportedExtensions().isEmpty());
	}

	/**
	 * Tests the service's ability to create a canvas.
	 */
	@Test
	public void checkCanvasCreation() {

		// Try to create a canvas
		FXMeshVizService service = new FXMeshVizService();
		try {
			service.createCanvas(new AbstractController(
					new AbstractMesh(), new AbstractView()));
		} catch (Exception e) {

			// If an exception is thrown print the stack trace and fail
			e.printStackTrace();
			fail();
		}
	}
}
