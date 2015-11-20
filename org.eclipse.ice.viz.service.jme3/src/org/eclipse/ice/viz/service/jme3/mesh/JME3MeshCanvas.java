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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.eclipse.ice.viz.service.jme3.application.MasterApplication;
import org.eclipse.ice.viz.service.jme3.internal.MasterApplicationHolder;
import org.eclipse.ice.viz.service.jme3.mesh.MeshAppStateModeFactory.Mode;
import org.eclipse.ice.viz.service.mesh.datastructures.BezierEdge;
import org.eclipse.ice.viz.service.mesh.datastructures.Edge;
import org.eclipse.ice.viz.service.mesh.datastructures.Hex;
import org.eclipse.ice.viz.service.mesh.datastructures.IMeshPartVisitor;
import org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas;
import org.eclipse.ice.viz.service.mesh.datastructures.Polygon;
import org.eclipse.ice.viz.service.mesh.datastructures.PolynomialEdge;
import org.eclipse.ice.viz.service.mesh.datastructures.Quad;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;
import org.eclipse.ice.viz.service.mesh.properties.MeshSelection;
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
public class JME3MeshCanvas implements IMeshVizCanvas {

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
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// The Mesh is always 2D
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#draw(org.eclipse.swt.widgets.
	 * Composite)
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

		// Start the app state and add the mesh to it
		appState.start(masterApp);
		appState.setMesh(mesh);

		// Return a composite displaying the app state
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
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#redraw()
	 */
	@Override
	public void redraw() {
		// Add a call to the app state's update method to its render queue
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
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#setProperties(java.util.Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setEditMode(boolean)
	 */
	@Override
	public void setEditMode(boolean edit) {

		// Get the mode factory
		MeshAppStateModeFactory factory = appState.getModeFactory();

		// Set the app state to the given mode from the factory
		if (edit) {
			appState.setMode(factory.getMode(Mode.Edit));
		} else {
			appState.setMode(factory.getMode(Mode.Add));
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setVisibleHUD(boolean)
	 */
	@Override
	public void setVisibleHUD(boolean on) {
		appState.setDisplayHUD(on);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * HUDIsVisible()
	 */
	@Override
	public boolean HUDIsVisible() {
		return appState.getDisplayHUD();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setVisibleAxis(boolean)
	 */
	@Override
	public void setVisibleAxis(boolean on) {
		appState.setDisplayAxes(on);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * AxisAreVisible()
	 */
	@Override
	public boolean AxisAreVisible() {
		return appState.getDisplayAxes();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * deleteSelection()
	 */
	@Override
	public void deleteSelection() {
		appState.getSelectionManager().deleteSelection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setSelection(java.lang.Object[])
	 */
	@Override
	public void setSelection(Object[] selection) {
		// Get the mesh selection manager from the app.
		MeshSelectionManager selectionManager = appState.getSelectionManager();

		// Reset any existing selection data in the MeshApplication
		selectionManager.clearSelection();

		// Initialize lists of IDs for vertices, edges, and polygons.
		final List<Integer> vertexIds = new ArrayList<Integer>();
		final List<Integer> edgeIds = new ArrayList<Integer>();
		final List<Integer> polygonIds = new ArrayList<Integer>();

		// Create a visitor to populate the above lists of IDs
		IMeshPartVisitor visitor = new IMeshPartVisitor() {

			@Override
			public void visit(Vertex vertex) {
				vertexIds.add(vertex.getId());
			}

			@Override
			public void visit(PolynomialEdge edge) {
				visit((Edge) edge);
			}

			@Override
			public void visit(BezierEdge edge) {
				visit((Edge) edge);
			}

			@Override
			public void visit(Edge edge) {
				edgeIds.add(edge.getId());
			}

			@Override
			public void visit(Hex hex) {
				visit((Polygon) hex);
			}

			@Override
			public void visit(Quad quad) {
				visit((Polygon) quad);
			}

			@Override
			public void visit(Polygon polygon) {
				polygonIds.add(polygon.getId());
			}

			@Override
			public void visit(Object object) {
				// Do nothing.
			}

			@Override
			public void visit(VizMeshComponent mesh) {
				// Do nothing.
			}
		};

		// Get each element from the selection and add the ID for the
		// corresponding vertex/edge/polygon to one of the above lists.
		// These lists will be sent to the selection manager later.
		for (Object element : selection) {

			if (element instanceof MeshSelection) {
				MeshSelection meshSelection = (MeshSelection) element;
				meshSelection.selectedMeshPart.acceptMeshVisitor(visitor);
			}
		}

		// Select all of the vertices, edges, and polygons.
		selectionManager.selectVertices(vertexIds);
		selectionManager.selectEdges(edgeIds);
		selectionManager.selectPolygons(polygonIds);
	}

}
