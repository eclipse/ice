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
package org.eclipse.ice.viz.service.javafx.geometry.plant;

import org.eclipse.ice.viz.service.geometry.reactor.JunctionController;
import org.eclipse.ice.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.ice.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.ice.viz.service.geometry.reactor.ReactorController;
import org.eclipse.ice.viz.service.geometry.reactor.ReactorMesh;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.AbstractMesh;
import org.eclipse.ice.viz.service.modeling.IControllerFactory;

/**
 * A factory for creating JavaFX views and controllers for Reactor Analyzer
 * components.
 * 
 * @author Robert Smith
 *
 */
public class FXPlantViewFactory implements IControllerFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.modeling.IControllerFactory#createController(
	 * org.eclipse.ice.viz.service.modeling.AbstractMeshComponent)
	 */
	@Override
	public AbstractController createController(AbstractMesh model) {

		// Create a FXJunction view for junctions
		if (model instanceof JunctionMesh) {
			FXJunctionView view = new FXJunctionView((JunctionMesh) model);
			return new JunctionController((JunctionMesh) model, view);
		}

		// Create a FXPipeView for pipes
		else if (model instanceof PipeMesh) {
			FXPipeView view = new FXPipeView((PipeMesh) model);
			return new FXPipeController((PipeMesh) model, view);
		}

		// Create a FXReactorView for reactors
		else if (model instanceof ReactorMesh) {
			FXReactorView view = new FXReactorView((ReactorMesh) model);
			return new ReactorController((ReactorMesh) model, view);
		}

		// Return null for unrecognized components
		else
			return null;
	}

}
