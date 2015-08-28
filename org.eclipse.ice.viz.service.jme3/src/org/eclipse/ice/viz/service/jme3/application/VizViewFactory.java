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
package org.eclipse.ice.viz.service.jme3.application;

import org.eclipse.ice.viz.service.jme3.internal.MasterApplicationHolder;
import org.eclipse.ice.viz.service.jme3.mesh.MeshAppState;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;



/**
 * This factory is used to create custom {@link ViewAppState}s for components
 * including <code>GeometryComponent</code>s, <code>MeshComponent</code>s, and
 * <code>PlantComposite</code>s. To embed the view in an SWT
 * <code>Composite</code>, see
 * {@link ViewAppState#createComposite(org.eclipse.swt.widgets.Composite)}.
 * 
 * @author Jordan
 * 
 */
public class VizViewFactory {

	// TODO We may want to handle this via an OSGi-based factories and registry.

	/**
	 * The core <code>MasterApplication</code> that renders all of the views.
	 */
	private final MasterApplication app;

	/**
	 * The default constructor.
	 */
	public VizViewFactory() {
		this(false);
	}

	/**
	 * A constructor that allows the <code>ViewFactory</code> to create its own
	 * {@link MasterApplication} if the OSGi-provided
	 * <code>MasterApplication</code> is not available.
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
	 *            If true, then this factory will create its own
	 *            <code>MasterApplication</code> when it cannot acquire one via
	 *            OSGi.
	 */
	public VizViewFactory(boolean staticFallBack) {

		// Try to get the app via OSGi.
		MasterApplication app = MasterApplicationHolder.getApplication();
		// If the flag is set to true and we couldn't get the app through OSGi,
		// create a new app.
		if (app == null && staticFallBack) {
			app = MasterApplication.createApplication();
		}

		this.app = app;
	}

	/**
	 * Creates a {@link MeshAppState} for the specified
	 * <code>MeshComponent</code>.
	 * 
	 * @param mesh
	 *            The root <code>MeshComponent</code> or mesh that contains
	 *            edges, vertices, and other <code>MeshComponent</code>s.
	 * @return A new <code>MeshAppState</code>, or null if there is no core
	 *         <code>MasterApplication</code> in the background.
	 */
	public MeshAppState createMeshView(VizMeshComponent mesh) {

		MeshAppState view = null;

		if (app != null) {

			// If necessary, wait until the MasterApplication has started before
			// trying to add a new AppState, or nothing may initialize.
			if (!app.isInitialized()) {
				app.blockUntilInitialized(0);
			}

			view = new MeshAppState();
			view.start(app);

			view.setMesh(mesh);
		}

		return view;
	}

	/**
	 * Disposes of the specified view.
	 * 
	 * @param view
	 *            The view to dispose.
	 */
	public void disposeView(ViewAppState view) {

		if (view != null) {
			view.stop();

			// If we need to do anything special, do it here.
		}

		return;
	}

}
