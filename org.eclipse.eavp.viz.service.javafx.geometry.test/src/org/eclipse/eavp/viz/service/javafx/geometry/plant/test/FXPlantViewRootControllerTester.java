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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeView;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPlantViewRootController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXReactorView;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorController;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMesh;
import org.junit.Test;

import javafx.scene.Group;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;

/**
 * A class to test the functionality of FXPlantViewRootController
 * 
 * @author Robert Smith
 *
 */
public class FXPlantViewRootControllerTester {

	/**
	 * Check that core channels are added to reactors automatically.
	 */
	@Test
	public void checkChannels() {

		// Create objects for testing
		FXPlantViewRootController root = new FXPlantViewRootController(
				new AbstractMesh(), new AbstractView());
		PipeMesh pipeMesh = new PipeMesh();
		pipeMesh.setLength(100);
		pipeMesh.setInnerRadius(5);
		pipeMesh.setRadius(5);
		pipeMesh.setAxialSamples(3);
		FXPipeView pipeView = new FXPipeView(pipeMesh);
		FXPipeController pipe = new FXPipeController(pipeMesh, pipeView);
		PipeMesh pipeMesh2 = new PipeMesh();
		pipeMesh2.setLength(100);
		pipeMesh2.setInnerRadius(5);
		pipeMesh2.setRadius(5);
		pipeMesh2.setAxialSamples(3);
		FXPipeView pipeView2 = new FXPipeView(pipeMesh2);
		FXPipeController core = new FXPipeController(pipeMesh2, pipeView2);
		core.setProperty("Core Channel", "True");
		ReactorController reactor = new ReactorController(new ReactorMesh(),
				new FXReactorView());

		// Add the reactor and two pipes to the root
		root.addEntityByCategory(reactor, "Reactors");
		root.addEntity(pipe);
		root.addEntity(core);

		// The core channel should have been added to the reactor. The non-core
		// channel pipe should not have been
		assertTrue(reactor.getEntities().contains(core));
		assertFalse(reactor.getEntities().contains(pipe));

		// Create a second set of objects
		FXPlantViewRootController root2 = new FXPlantViewRootController(
				new AbstractMesh(), new AbstractView());
		FXPipeController pipe2 = new FXPipeController(new PipeMesh(),
				new FXPipeView());
		FXPipeController core2 = new FXPipeController(new PipeMesh(),
				new FXPipeView());
		core2.setProperty("Core Channel", "True");
		ReactorController reactor2 = new ReactorController(new ReactorMesh(),
				new FXReactorView());

		// Add the pipes then the reactor.
		root2.addEntity(core2);
		root2.addEntity(pipe2);
		root2.addEntityByCategory(reactor2, "Reactors");

		// The same thing should have happenned, regardless of what order the
		// children were added to the root.
		assertTrue(reactor2.getEntitiesByCategory("Core Channels")
				.contains(core2));
		assertFalse(reactor2.getEntities().contains(pipe2));
	}

	/**
	 * Test that the FXPlantViewRootController is cloned correctly.
	 */
	@Test
	public void checkClone() {

		// Create a cloned FXPlantViewRootController and check that it is
		// identical to the original
		AbstractMesh mesh = new AbstractMesh();
		FXPlantViewRootController root = new FXPlantViewRootController(mesh,
				new AbstractView());
		root.setProperty("Test", "Property");
		FXPlantViewRootController clone = (FXPlantViewRootController) root
				.clone();
		assertTrue(root.equals(clone));
	}

	/**
	 * Check that the root sets the wireframe modes of its children correctly
	 */
	@Test
	public void checkWireframe() {

		// Create some objects for testing
		FXPlantViewRootController root = new FXPlantViewRootController(
				new AbstractMesh(), new AbstractView());
		PipeMesh pipeMesh = new PipeMesh();
		pipeMesh.setLength(100);
		pipeMesh.setInnerRadius(5);
		pipeMesh.setRadius(5);
		pipeMesh.setAxialSamples(3);
		FXPipeView pipeView = new FXPipeView(pipeMesh);
		FXPipeController pipe = new FXPipeController(pipeMesh, pipeView);

		// Add the pipe to the root. It should still be drawn normally
		root.addEntity(pipe);
		assertTrue(((MeshView) ((Group) pipe.getRepresentation()).getChildren()
				.get(0)).getDrawMode() == DrawMode.FILL);

		// Set the root to wireframe mode. The pipe should be set as well
		root.setWireFrameMode(true);
		assertTrue(((MeshView) ((Group) pipe.getRepresentation()).getChildren()
				.get(0)).getDrawMode() == DrawMode.LINE);

		// Create another pipe
		PipeMesh pipeMesh2 = new PipeMesh();
		pipeMesh2.setLength(100);
		pipeMesh2.setInnerRadius(5);
		pipeMesh2.setRadius(5);
		pipeMesh2.setAxialSamples(3);
		FXPipeView pipeView2 = new FXPipeView(pipeMesh2);
		FXPipeController pipe2 = new FXPipeController(pipeMesh2, pipeView2);

		// Add the second pipe. Since the root is set to wireframe mode, the
		// pipe should also be set when added
		root.addEntity(pipe2);
		assertTrue(((MeshView) ((Group) pipe2.getRepresentation()).getChildren()
				.get(0)).getDrawMode() == DrawMode.LINE);

		// Return the root to normal mode and check that the pipes are also
		// reset.
		root.setWireFrameMode(false);
		assertTrue(((MeshView) ((Group) pipe.getRepresentation()).getChildren()
				.get(0)).getDrawMode() == DrawMode.FILL);
		assertTrue(((MeshView) ((Group) pipe2.getRepresentation()).getChildren()
				.get(0)).getDrawMode() == DrawMode.FILL);

	}

}
