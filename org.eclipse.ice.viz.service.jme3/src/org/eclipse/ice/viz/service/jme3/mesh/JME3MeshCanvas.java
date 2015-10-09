/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.mesh;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.ice.viz.service.jme3.application.MasterApplication;
import org.eclipse.ice.viz.service.jme3.internal.MasterApplicationHolder;
import org.eclipse.ice.viz.service.IVizCanvas;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An IVizCanvas containing a mesh editor powered by jMonkeyEngine 3. This class
 * creates and manages an AppState for the master JME3 SimpleApplication. This
 * AppState contains a mesh editor, allowing the user to view and modify
 * polygons (defined by vertices and edges) on a 2D plane.
 * 
 * @author Robert Smith
 *
 */
public class JME3MeshCanvas implements IVizCanvas {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(JME3MeshCanvas.class);

	/**
	 * The single JME3 Simple Application on which all ICE JME3 renderings will
	 * run.
	 */
	private MasterApplication masterApp;

	/**
	 * The mesh component to be rendered and edited.
	 */
	private VizMeshComponent mesh;

	/**
	 * An app state containing the mesh editor's JME3 implementation.
	 */
	private MeshAppState appState;

	/**
	 * The Constructor
	 *
	 * @param source
	 *            The MeshComponent containing the meshes to be rendered.
	 */
	public JME3MeshCanvas(VizMeshComponent newMesh) {
		mesh = newMesh;
		appState = new MeshAppState();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// The Mesh is always 2D
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.IVizCanvas#draw(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {
		masterApp = MasterApplicationHolder.getApplication();

		if (masterApp != null) {

			// If necessary, wait until the MasterApplication has started before
			// trying to add a new AppState, or nothing may initialize.
			if (!masterApp.isInitialized()) {
				masterApp.blockUntilInitialized(0);
			}
		}

		//Start the app state and add the mesh to it
		appState.start(masterApp);
		appState.setMesh(mesh);
		
		//Return a composite displauying the app state
		return appState.createComposite(parent);

	}

	/**
	 * Register a listener with the MeshAppState
	 */
	public void registerListener(IMeshSelectionListener listener) {
		appState.getSelectionManager().addMeshApplicationListener(listener);
	}

	/**
	 * Getter for the MeshAppState's MeshAppStateModeFactory
	 * 
	 * @return The MeshAppState's MeshAppStateModeFactory
	 */
	public MeshAppStateModeFactory getMeshAppStateModeFactory() {
		return appState.getModeFactory();
	}

	/**
	 * Mutator for the MeshAppState's mode
	 * 
	 * @param mode
	 *            The new mode for the MeshAppState
	 */
	public void setMode(MeshAppStateMode mode) {
		appState.setMode(mode);
	}

	/**
	 * Getter for the MeshAppState
	 * 
	 * @return The MeshAppState containing the mesh editor
	 */
	public MeshAppState getMeshAppState() {
		return appState;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.IVizCanvas#redraw()
	 */
	@Override
	public void redraw() {
		//Add a call to the app state's update method to its render queue
		appState.enqueue(new Callable<Boolean>() {
			@Override
			public Boolean call() {
				appState.update(0f);
				return true;
			}
		});
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.IVizCanvas#setProperties(java.util.Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.viz.service.IVizCanvas#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return false;
	}

}
