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

import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeView;
import org.eclipse.eavp.viz.service.geometry.reactor.Extrema;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
import org.junit.Test;

/**
 * A class to test the functionality of the FXPipeView.
 * 
 * @author Robert Smith
 *
 */
public class FXPipeViewTester {

	/**
	 * Test that FXPipeViews are cloned correctly
	 */
	@Test
	public void checkClone() {

		// Create a cloned view and check that it is identical to the original
		PipeMesh mesh = new PipeMesh();
		mesh.setLength(100);
		mesh.setInnerRadius(5);
		mesh.setRadius(5);
		mesh.setAxialSamples(3);
		FXPipeView view = new FXPipeView(mesh);
		FXPipeView clone = (FXPipeView) view.clone();
		assertTrue(view.equals(clone));
	}

	/**
	 * Check that a pipe returns the bounds of its ends correctly
	 */
	@Test
	public void checkExtrema() {

		// Create a pipe
		PipeMesh pipeMesh = new PipeMesh();
		pipeMesh.setLength(100);
		pipeMesh.setInnerRadius(5);
		pipeMesh.setRadius(5);
		pipeMesh.setAxialSamples(3);
		FXPipeView pipeView = new FXPipeView(pipeMesh);
		FXPipeController pipe = new FXPipeController(pipeMesh, pipeView);

		// The top end of the pipe is a radius 5 circle centered at (0, 50, 0)
		Extrema top = pipe.getUpperExtrema();
		assertTrue(Math.abs(top.getMaxX() - 5d) < 1);
		assertTrue(Math.abs(top.getMinX() - -5d) < 1);
		assertTrue(Math.abs(top.getMaxY() - 50d) < 1);
		assertTrue(Math.abs(top.getMinY() - 50d) < 1);
		assertTrue(Math.abs(top.getMaxZ() - 5d) < 1);
		assertTrue(Math.abs(top.getMinZ() - -5d) < 1);

		// The bottom end of the pipe is a radius 5 circle centered at (0, -50,
		// 0)
		Extrema bottom = pipe.getLowerExtrema();
		assertTrue(Math.abs(bottom.getMaxX() - 5d) < 1);
		assertTrue(Math.abs(bottom.getMinX() - -5d) < 1);
		assertTrue(Math.abs(bottom.getMaxY() - -50d) < 1);
		assertTrue(Math.abs(bottom.getMinY() - -50d) < 1);
		assertTrue(Math.abs(bottom.getMaxZ() - 5d) < 1);
		assertTrue(Math.abs(bottom.getMinZ() - -5d) < 1);

		// Move the pipe
		pipe.setTranslation(1, 2, 3);

		// The top end of the pipe is a radius 5 circle centered at (1, 52, 3)
		top = pipe.getUpperExtrema();
		assertTrue(Math.abs(top.getMaxX() - 6d) < 1);
		assertTrue(Math.abs(top.getMinX() - -4d) < 1);
		assertTrue(Math.abs(top.getMaxY() - 52d) < 1);
		assertTrue(Math.abs(top.getMinY() - 52d) < 1);
		assertTrue(Math.abs(top.getMaxZ() - 8d) < 1);
		assertTrue(Math.abs(top.getMinZ() - -2d) < 1);

		// The bottom end of the pipe is a radius 5 circle centered at (1, -48,
		// 3)
		bottom = pipe.getLowerExtrema();
		assertTrue(Math.abs(bottom.getMaxX() - 6d) < 1);
		assertTrue(Math.abs(bottom.getMinX() - -4d) < 1);
		assertTrue(Math.abs(bottom.getMaxY() - -48d) < 1);
		assertTrue(Math.abs(bottom.getMinY() - -48d) < 1);
		assertTrue(Math.abs(bottom.getMaxZ() - 8d) < 1);
		assertTrue(Math.abs(bottom.getMinZ() - -2d) < 1);

		// Return the pipe to the origin and scale it
		pipe.setTranslation(0, 0, 0);
		pipe.setScale(2, 3, 4);

		// The top end of the pipe is an ellipse with a major axis of 20 and a
		// minor axis of 10, centered on (0, 150, 0)
		top = pipe.getUpperExtrema();
		assertTrue(Math.abs(top.getMaxX() - 10d) < 1);
		assertTrue(Math.abs(top.getMinX() - -10d) < 1);
		assertTrue(Math.abs(top.getMaxY() - 150d) < 1);
		assertTrue(Math.abs(top.getMinY() - 150d) < 1);
		assertTrue(Math.abs(top.getMaxZ() - 20d) < 1);
		assertTrue(Math.abs(top.getMinZ() - -20d) < 1);

		// The bottome end of the pipe is an ellipse with a major axis of 20 and
		// a minor axis of 10, centered on (0, -150, 0)
		bottom = pipe.getLowerExtrema();
		assertTrue(Math.abs(bottom.getMaxX() - 10d) < 1);
		assertTrue(Math.abs(bottom.getMinX() - -10d) < 1);
		assertTrue(Math.abs(bottom.getMaxY() - -150d) < 1);
		assertTrue(Math.abs(bottom.getMinY() - -150d) < 1);
		assertTrue(Math.abs(bottom.getMaxZ() - 20d) < 1);
		assertTrue(Math.abs(bottom.getMinZ() - -20d) < 1);

		// Return the pipe to its default scale and rotate it 90 degrees about
		// the x axis.
		pipe.setScale(1, 1, 1);
		pipe.setRotation(Math.PI / 2, 0, 0);

		// The top end of the pipe is a circle with radius 5 centered on (0, 0,
		// 50)
		top = pipe.getUpperExtrema();
		assertTrue(Math.abs(top.getMaxX() - 5d) < 1);
		assertTrue(Math.abs(top.getMinX() - -5d) < 1);
		assertTrue(Math.abs(top.getMaxY() - 5d) < 1);
		assertTrue(Math.abs(top.getMinY() - -5d) < 1);
		assertTrue(Math.abs(top.getMaxZ() - 50d) < 1);
		assertTrue(Math.abs(top.getMinZ() - 50d) < 1);

		// The bottom end of the pipe is a circle with radius 5 centered on (0,
		// 0, -50)
		bottom = pipe.getLowerExtrema();
		assertTrue(Math.abs(bottom.getMaxX() - 5d) < 1);
		assertTrue(Math.abs(bottom.getMinX() - -5d) < 1);
		assertTrue(Math.abs(bottom.getMaxY() - 5d) < 1);
		assertTrue(Math.abs(bottom.getMinY() - -5d) < 1);
		assertTrue(Math.abs(bottom.getMaxZ() - -50d) < 1);
		assertTrue(Math.abs(bottom.getMinZ() - -50d) < 1);
	}
}
