/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.geometry;

import org.eclipse.ice.reactor.plant.IPlantView;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.viz.service.IVizCanvas;
import org.eclipse.ice.viz.service.IVizService;
import org.eclipse.ice.viz.service.IVizServiceFactory;
import org.eclipse.ice.viz.service.javafx.geometry.plant.FXPlantCompositeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This factory is used to create 3D models for the Plant View. It gets the
 * appropriate IVizService to instantiate an implementation of IPlantView which
 * is capable of drawing the the view into a composite.
 * 
 * @author Jordan, Robert Smith
 * 
 */
public class ViewFactory {

	// TODO We may want to handle this via an OSGi-based factories and registry.

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ViewFactory.class);

	/**
	 * A factory containing all consumed IVizServices. The service implementing
	 * the IPlantView will be drawn from it.
	 */
	private IVizServiceFactory factory;

	/**
	 * Whether or not this factory will create a jME MasterApplication when one
	 * cannot be obtained through OSGI. This is not relevant for other
	 * implementations of IPlantView, and will be ignored if one of them is
	 * selected.
	 */
	private boolean staticFallBack;

	/**
	 * The default constructor.
	 */
	public ViewFactory() {
		this(false);
	}

	/**
	 * A constructor that allows the <code>ViewFactory</code> to create its own
	 * {@link MasterApplication} if the OSGi-provided
	 * <code>MasterApplication</code> is not available and it is directed to
	 * make use of the jME Plant View service.
	 * <p>
	 * <b>Note:</b> This is <i>not intended</i> for use inside ICE but inside
	 * static applications. In ICE, the <code>MasterApplication</code> is
	 * created only via an OSGi-provided factory.
	 * </p>
	 * <p>
	 * <b>Note 2:</b> If you use more than one <code>ViewFactory</code> with
	 * <code>staticFallBack</code> set to <code>true</code> and without running
	 * OSGi, then your program may not work properly. jME does not support
	 * multiple <code>SimpleApplication</code>s running simultaneously.
	 * </p>
	 * 
	 * @param staticFallBack
	 *            If true, then if the jME service is selected this factory will
	 *            create its own <code>MasterApplication</code> when it cannot
	 *            acquire one via OSGi.
	 */
	public ViewFactory(boolean staticFallBack) {
		this.staticFallBack = staticFallBack;
	}

	/**
	 * // * Creates a {@link MeshAppState} for the specified // *
	 * <code>MeshComponent</code>. // * // * @param mesh // * The root
	 * <code>MeshComponent</code> or mesh that contains // * edges, vertices,
	 * and other <code>MeshComponent</code>s. // * @return A new
	 * <code>MeshAppState</code>, or null if there is no core // *
	 * <code>MasterApplication</code> in the background. //
	 */
	// public MeshAppState createMeshView(MeshComponent mesh) {
	//
	// MeshAppState view = null;
	//
	// if (app != null) {
	//
	// // If necessary, wait until the MasterApplication has started before
	// // trying to add a new AppState, or nothing may initialize.
	// if (!app.isInitialized()) {
	// app.blockUntilInitialized(0);
	// }
	//
	// view = new MeshAppState();
	// view.start(app);
	//
	// view.setMesh(mesh);
	// }
	//
	// return view;
	// }

	/**
	 * Creates a {@link PlantAppState} for the specified
	 * <code>PlantComposite</code>.
	 * 
	 * @param plant
	 *            The root <code>PlantComponent</code> or plant that can contain
	 *            several <code>PlantComponent</code>s.
	 * @return A new <code>PlantAppState</code>, or null if there is no core
	 *         <code>MasterApplication</code> in the background.
	 */
	public IPlantView createPlantView(PlantComposite plant) {

		// TODO Move this to the IVizService for the jME implementation
		// // Try to get the app via OSGi.
		// MasterApplication app = MasterApplicationHolder.getApplication();
		// // If the flag is set to true and we couldn't get the app through
		// OSGi,
		// // create a new app.
		// if (app == null && staticFallBack) {
		// app = MasterApplication.createApplication();
		// }
		//
		// PlantAppState view = null;
		//
		// if (app != null) {
		//
		// // If necessary, wait until the MasterApplication has started before
		// // trying to add a new AppState, or nothing may initialize.
		// if (!app.isInitialized()) {
		// app.blockUntilInitialized(0);
		// }
		//
		// view = new PlantAppState();
		// view.start(app);
		//
		// view.setPlant(plant);
		// }
		//
		// return view;

		// TODO This should be getting all services and presenting the user with
		// a choice, instead of hardcoding the JavaFX editor in.
		// IVizService service = factory.get("ICE Geometry Editor");
		IVizService service = new FXGeometryVizService();

		// Create a converter for the plant composite
		FXPlantCompositeConverter converter = new FXPlantCompositeConverter(
				plant);

		// The canvas to be created
		IVizCanvas vizCanvas = null;

		// Create and draw geometry canvas
		try {

			// Create and draw the canvas
			vizCanvas = service.createCanvas(converter.getPlant());
		} catch (Exception e) {
			logger.error(
					"Error creating Geometry Canvas with Geometry Service.", e);
		}

		return (IPlantView) vizCanvas;
	}

	/**
	 * Disposes of the specified view.
	 * 
	 * @param view
	 *            The view to dispose.
	 */
	public void disposeView(IPlantView view) {
		view.dispose();

		return;
	}

	/**
	 * Sets the IVizServiceFactory which this factory will draw IVizServices
	 * from.
	 * 
	 * This function is intended to be called by the OSGI layer to automatically
	 * consume a provided IVizServiceFactory services.
	 * 
	 * @param factory
	 *            The new IVizServiceFactory to be used by this factory.
	 */
	public void setVizServiceFactory(IVizServiceFactory factory) {
		this.factory = factory;
	}

}
