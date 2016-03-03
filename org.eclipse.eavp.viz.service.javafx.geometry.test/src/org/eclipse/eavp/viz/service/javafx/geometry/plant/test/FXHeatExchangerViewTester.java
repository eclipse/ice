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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionController;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMeshCategory;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXHeatExchangerController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXHeatExchangerView;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXJunctionView;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeView;
import org.eclipse.eavp.viz.service.modeling.Representation;
import org.junit.Test;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;

/**
 * A class to test the functionality of the FXHeatExchangerView
 * 
 * @author Robert Smith
 *
 */
public class FXHeatExchangerViewTester {

	/**
	 * Test that FXHeatExchangerViews are cloned correctly
	 */
	@Test
	public void checkClone() {

		// Create a cloned view and check that it is identical to the original
		HeatExchangerMesh mesh = new HeatExchangerMesh();
		FXHeatExchangerView view = new FXHeatExchangerView(mesh);
		FXHeatExchangerView clone = (FXHeatExchangerView) view.clone();
		assertTrue(view.equals(clone));
	}

	/**
	 * Check that the view produces the correct JavaFX output.
	 */
	@Test
	public void checkView() {

		// Create a pipe for the HeatExchanger to contain
		PipeMesh pipeMesh = new PipeMesh();
		pipeMesh.setLength(100);
		pipeMesh.setInnerRadius(25);
		pipeMesh.setRadius(25);
		pipeMesh.setAxialSamples(3);
		FXPipeView pipeView = new FXPipeView(pipeMesh);
		FXPipeController pipe = new FXPipeController(pipeMesh, pipeView);

		// Create a heat exchanger
		HeatExchangerMesh mesh = new HeatExchangerMesh();
		FXHeatExchangerView view = new FXHeatExchangerView(mesh);
		FXHeatExchangerController exchanger = new FXHeatExchangerController(
				mesh, view);
		exchanger.setPrimaryPipe(pipe);

		// Create a second pipe and two junctions to connect it to the exchanger
		PipeMesh pipeMesh2 = new PipeMesh();
		pipeMesh2.setLength(100);
		pipeMesh2.setInnerRadius(25);
		pipeMesh2.setRadius(25);
		pipeMesh2.setAxialSamples(3);
		FXPipeView pipeView2 = new FXPipeView(pipeMesh2);
		FXPipeController pipe2 = new FXPipeController(pipeMesh2, pipeView2);
		pipe2.setTranslation(50, 0, 0);

		JunctionMesh junctionMesh = new JunctionMesh();
		FXJunctionView junctionView = new FXJunctionView(junctionMesh);
		JunctionController junction = new JunctionController(junctionMesh,
				junctionView);
		junction.addEntityToCategory(exchanger, ReactorMeshCategory.INPUT);
		exchanger.addEntityToCategory(junction,
				ReactorMeshCategory.SECONDARY_OUTPUT);
		junction.addEntityToCategory(pipe2, ReactorMeshCategory.OUTPUT);
		pipe2.addEntityToCategory(junction, ReactorMeshCategory.INPUT);

		JunctionMesh junctionMesh2 = new JunctionMesh();
		FXJunctionView junctionView2 = new FXJunctionView(junctionMesh2);
		JunctionController junction2 = new JunctionController(junctionMesh2,
				junctionView2);
		junction.addEntityToCategory(exchanger, ReactorMeshCategory.OUTPUT);
		exchanger.addEntityToCategory(junction2,
				ReactorMeshCategory.SECONDARY_INPUT);
		junction2.addEntityToCategory(pipe2, ReactorMeshCategory.INPUT);
		pipe2.addEntityToCategory(junction2, ReactorMeshCategory.OUTPUT);

		// Check that the view has all four parts: a central pipe, a box around
		// it, and two pipes leading to junctions.
		Representation<Group> representation = exchanger.getRepresentation();
		Group node = representation.getData();
		assertEquals(4, node.getChildren().size());

		// The number of children of each type found
		int numPipes = 0;
		int numWalls = 0;
		int numPrimaryPipes = 0;

		// Count each child from the node
		for (Node child : node.getChildren()) {

			// MeshViews contain custom pipe meshes
			if (child instanceof MeshView) {
				numPipes++;
			}

			// A box is used to draw the seperating wall
			else if (child instanceof Box) {
				numWalls++;
			}

			// The child primary pipe will package its own rendering in a group
			else if (child instanceof Group) {
				numPrimaryPipes++;
			}
		}

		// Check that there are three pipes and a wall
		assertEquals(2, numPipes);
		assertEquals(1, numWalls);
		assertEquals(1, numPrimaryPipes);

	}
}
