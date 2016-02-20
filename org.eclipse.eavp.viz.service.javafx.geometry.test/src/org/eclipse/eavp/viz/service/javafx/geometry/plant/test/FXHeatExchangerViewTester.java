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

import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXHeatExchangerController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXHeatExchangerView;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXJunctionView;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeView;
import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionController;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
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
		junction.addEntityByCategory(exchanger, "Input");
		exchanger.addEntityByCategory(junction, "Secondary Output");
		junction.addEntityByCategory(pipe2, "Output");
		pipe2.addEntityByCategory(junction, "Input");

		JunctionMesh junctionMesh2 = new JunctionMesh();
		FXJunctionView junctionView2 = new FXJunctionView(junctionMesh2);
		JunctionController junction2 = new JunctionController(junctionMesh2,
				junctionView2);
		junction.addEntityByCategory(exchanger, "Output");
		exchanger.addEntityByCategory(junction2, "Secondary Input");
		junction2.addEntityByCategory(pipe2, "Input");
		pipe2.addEntityByCategory(junction2, "Output");

		// Check that the view has all four parts: a central pipe, a box around
		// it, and two pipes leading to junctions.
		Group node = (Group) exchanger.getRepresentation();
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
