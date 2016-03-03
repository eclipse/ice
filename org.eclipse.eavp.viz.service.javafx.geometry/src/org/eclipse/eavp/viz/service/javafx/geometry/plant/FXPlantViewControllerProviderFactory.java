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
package org.eclipse.eavp.viz.service.javafx.geometry.plant;

import org.eclipse.eavp.viz.service.geometry.reactor.HeatExchangerMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionController;
import org.eclipse.eavp.viz.service.geometry.reactor.JunctionMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.PipeMesh;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorController;
import org.eclipse.eavp.viz.service.geometry.reactor.ReactorMesh;
import org.eclipse.eavp.viz.service.modeling.BasicControllerProviderFactory;
import org.eclipse.eavp.viz.service.modeling.IControllerProvider;
import org.eclipse.eavp.viz.service.modeling.IMesh;

/**
 * A factory for creating JavaFX views and controllers for Reactor Analyzer
 * components.
 * 
 * @author Robert Smith
 *
 */
public class FXPlantViewControllerProviderFactory extends BasicControllerProviderFactory {

	/**
	 * The default constructor.
	 */
	public FXPlantViewControllerProviderFactory() {
		super();

		// Set the JunctionMesh provider
		typeMap.put(JunctionMesh.class,
				new IControllerProvider<JunctionController>() {
					@Override
					public JunctionController createController(IMesh model) {

						// Create a FXJunction view for junctions
						FXJunctionView view = new FXJunctionView(
								(JunctionMesh) model);
						return new JunctionController((JunctionMesh) model,
								view);
					}
				});

		// Set the PipeMesh provider
		typeMap.put(PipeMesh.class,
				new IControllerProvider<FXPipeController>() {
					@Override
					public FXPipeController createController(IMesh model) {

						// Create a FXPipeView for pipes
						FXPipeView view = new FXPipeView((PipeMesh) model);
						return new FXPipeController((PipeMesh) model, view);
					}
				});

		// Set the ReactorMesh provider
		typeMap.put(ReactorMesh.class,
				new IControllerProvider<ReactorController>() {
					@Override
					public ReactorController createController(IMesh model) {

						// Create a FXReactorView for reactors
						FXReactorView view = new FXReactorView(
								(ReactorMesh) model);
						return new ReactorController((ReactorMesh) model, view);
					}
				});

		// Set the HeatExchangerMesh provider
		typeMap.put(HeatExchangerMesh.class,
				new IControllerProvider<FXHeatExchangerController>() {
					@Override
					public FXHeatExchangerController createController(
							IMesh model) {

						// Create a FXHeatExchangerView for heat exchangers.
						FXHeatExchangerView view = new FXHeatExchangerView(
								model);
						return new FXHeatExchangerController(
								(HeatExchangerMesh) model, view);
					}
				});
	}

}
