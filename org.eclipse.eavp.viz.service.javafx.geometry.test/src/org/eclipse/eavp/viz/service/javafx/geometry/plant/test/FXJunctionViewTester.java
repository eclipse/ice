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
package org.eclipse.eavp.viz.service.javafx.geometry.plant.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXJunctionView;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeView;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionController;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
import org.junit.Test;

import javafx.scene.Group;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Shape3D;

/**
 * A class to test the functionality of FXJunctionView
 * 
 * @author Robert Smith
 *
 */
public class FXJunctionViewTester {

	/**
	 * Test that FXJunctionViews are cloned correctly
	 */
	@Test
	public void checkClone() {

		// Create a cloned view and check that it is identical to the original
		JunctionMesh mesh = new JunctionMesh();
		FXJunctionView view = new FXJunctionView(mesh);
		FXJunctionView clone = (FXJunctionView) view.clone();
		assertTrue(view.equals(clone));
	}

	/**
	 * Check that the junction is drawn in the right position to cover the ends
	 * of its pipes.
	 */
	@Test
	public void checkPosition() {

		// Create a view on a junction with no connecting pipes
		JunctionMesh mesh = new JunctionMesh();
		FXJunctionView view = new FXJunctionView(mesh);
		JunctionController junction = new JunctionController(mesh, view);

		// Check that the junction is centered at the origin by default
		assertTrue(view.getCenter()[0] == 0d);
		assertTrue(view.getCenter()[1] == 0d);
		assertTrue(view.getCenter()[2] == 0d);

		// Create a pipe
		PipeMesh pipeMesh = new PipeMesh();
		pipeMesh.setLength(100);
		pipeMesh.setInnerRadius(5);
		pipeMesh.setRadius(5);
		pipeMesh.setAxialSamples(3);
		FXPipeView pipeView = new FXPipeView(pipeMesh);
		FXPipeController pipe = new FXPipeController(pipeMesh, pipeView);

		// Add the pipe as input
		junction.addEntityByCategory(pipe, "Input");

		// The junction's center point
		double[] center = view.getCenter();

		// Since the pipe is an input to the junction, it will be centered about
		// the pipe's upper edge, at (0, 50, 0).
		assertTrue(Math.abs(center[0] - 0d) < 1);
		assertTrue(Math.abs(center[1] - 50d) < 1);
		assertTrue(Math.abs(center[2] - 0d) < 1);

		// Add the other end of the pipe to the junction, so that the junction
		// is completely enveloping the pipe
		junction.addEntityByCategory(pipe, "Output");

		// The junction should now be centered at the origin, as it is a
		// rectangular bounding box around the pipe which is also centered on
		// the origin
		center = view.getCenter();
		assertTrue(Math.abs(center[0] - 0d) < 1);
		assertTrue(Math.abs(center[1] - 0d) < 1);
		assertTrue(Math.abs(center[2] - 0d) < 1);

		// Set the junction to only have the pipe as output
		junction.removeEntity(pipe);
		junction.addEntityByCategory(pipe, "Output");

		// The junction should centered on the other side of the pipe, at (0,
		// -50, 0)
		center = view.getCenter();
		assertTrue(Math.abs(center[0] - 0d) < 1);
		assertTrue(Math.abs(center[1] - -50d) < 1);
		assertTrue(Math.abs(center[2] - 0d) < 1);

		// Rotate the pipe 90 degrees about the x axis.
		pipe.setRotation(Math.PI / 2, 0, 0);

		// The pipe should now be lying on its side, with the mouth at (0, 0,
		// -50)
		center = view.getCenter();
		assertTrue(Math.abs(center[0] - 0d) < 1);
		assertTrue(Math.abs(center[1] - 0d) < 1);
		assertTrue(Math.abs(center[2] - -50d) < 1);

		// Create a second pipe
		PipeMesh pipeMesh2 = new PipeMesh();
		pipeMesh2.setLength(100);
		pipeMesh2.setInnerRadius(5);
		pipeMesh2.setRadius(5);
		pipeMesh2.setAxialSamples(3);
		FXPipeView pipeView2 = new FXPipeView(pipeMesh2);
		FXPipeController pipe2 = new FXPipeController(pipeMesh2, pipeView2);

		// Set the pipe as input
		junction.addEntityByCategory(pipe2, "Input");

		// The junction is now covering two circles of radius 5, one centered
		// on (0, 50, 0) on the XZ plane and the other centered on (0, 0, 50) on
		// the XY plane. Thus the center should be at (0, 12.5, -12.5)
		center = view.getCenter();
		assertTrue(Math.abs(center[0] - 0) < 1);
		assertTrue(Math.abs(center[1] - 22.5d) < 1);
		assertTrue(Math.abs(center[2] - -22.5d) < 1);
	}

	/**
	 * Test that the view's representation is set to the proper draw mode for
	 * wireframe drawing.
	 */
	@Test
	public void checkWireframe() {
		// Create a view on a junction with no connecting pipes
		JunctionMesh mesh = new JunctionMesh();
		FXJunctionView view = new FXJunctionView(mesh);
		JunctionController junction = new JunctionController(mesh, view);

		// The box should be solid by default
		assertTrue(((Shape3D) ((Group) junction.getRepresentation())
				.getChildren().get(0)).getDrawMode() == DrawMode.FILL);

		// Set the junction to wireframe mode and check that the box is drawn
		// correctly
		junction.setWireFrameMode(true);
		assertTrue(((Shape3D) ((Group) junction.getRepresentation())
				.getChildren().get(0)).getDrawMode() == DrawMode.LINE);

		// Turn off wireframe mode and check that the box has been returned to
		// normal
		junction.setWireFrameMode(false);
		assertTrue(((Shape3D) ((Group) junction.getRepresentation())
				.getChildren().get(0)).getDrawMode() == DrawMode.FILL);
	}

}
