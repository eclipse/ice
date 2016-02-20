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

import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXHeatExchangerController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPipeController;
import org.eclipse.eavp.viz.service.javafx.geometry.plant.FXPlantViewControllerFactory;
import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionController;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorController;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMesh;
import org.junit.Test;

/**
 * A class to test the functionality of the FXPlantViewFactory
 * 
 * @author Robert Smith
 *
 */
public class FXPlantViewControllerFactoryTester {

	/**
	 * Check that the factory produces the correct types of output.
	 */
	@Test
	public void checkCreation() {

		// The factory to test
		FXPlantViewControllerFactory factory = new FXPlantViewControllerFactory();

		// A HeatExchangerMesh should be given a FXHeatExchangerController
		HeatExchangerMesh heatExchangerMesh = new HeatExchangerMesh();
		FXHeatExchangerController exchanger = (FXHeatExchangerController) factory
				.createController(heatExchangerMesh);

		// A JunctionMesh should be given a JunctionController
		JunctionMesh junctionMesh = new JunctionMesh();
		JunctionController junction = (JunctionController) factory
				.createController(junctionMesh);

		// A PipeMesh should be given a FXPipeController
		PipeMesh pipeMesh = new PipeMesh();
		FXPipeController pipeController = (FXPipeController) factory
				.createController(pipeMesh);

		// A ReactorMesh should be given a ReactorController
		ReactorMesh reactorMesh = new ReactorMesh();
		ReactorController reactorController = (ReactorController) factory
				.createController(reactorMesh);
	}
}
