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
package org.eclipse.ice.viz.service.javafx.geometry.plant.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.ice.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.ice.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.ice.viz.service.javafx.geometry.plant.FXJunctionView;
import org.eclipse.ice.viz.service.javafx.geometry.plant.FXPipeController;
import org.eclipse.ice.viz.service.javafx.geometry.plant.FXPipeView;
import org.junit.Test;

/**
 * A class to test the functionality of FXJunctionView
 * 
 * @author Robert Smith
 *
 */
public class FXJunctionViewTester {

	/**
	 * Check that the junction is drawn in the right position to cover the ends
	 * of its pipes.
	 */
	@Test
	public void checkPosition() {

		// Create a view on a junction with no connecting pipes
		JunctionMesh mesh = new JunctionMesh();
		FXJunctionView view = new FXJunctionView(mesh);

		// Check that the junction is centered at the origin by default
		assertTrue(view.getCenter()[0] == 0d);
		assertTrue(view.getCenter()[1] == 0d);
		assertTrue(view.getCenter()[2] == 0d);

		// Create a pipe
		PipeMesh pipeMesh = new PipeMesh();
		pipeMesh.setLength(100);
		pipeMesh.setInnerRadius(25);
		pipeMesh.setRadius(25);
		pipeMesh.setAxialSamples(3);
		FXPipeView pipeView = new FXPipeView(pipeMesh);
		FXPipeController pipe = new FXPipeController(pipeMesh, pipeView);

		// Add the pipe as input
		mesh.addEntityByCategory(pipe, "Input");

		System.out.println(view.getCenter());

	}

}
