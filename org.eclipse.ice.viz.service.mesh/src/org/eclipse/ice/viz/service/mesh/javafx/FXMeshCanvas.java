/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.mesh.javafx;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import org.eclipse.ice.viz.service.IVizCanvas;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateable;
import org.eclipse.ice.viz.service.datastructures.VizObject.IVizUpdateableListener;
import org.eclipse.ice.viz.service.geometry.scene.base.GNode;
import org.eclipse.ice.viz.service.geometry.scene.base.GeometryAttachment;
import org.eclipse.ice.viz.service.geometry.scene.model.INode;
import org.eclipse.ice.viz.service.geometry.viewer.GeometryViewer;
import org.eclipse.ice.viz.service.geometry.viewer.IRenderer;
import org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas;
import org.eclipse.ice.viz.service.mesh.properties.MeshSelection;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;

/**
 * <p>
 * GeometryCanvas provides the ability to visualize and manipulate 3D geometry
 * data.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 * 
 */
public class FXMeshCanvas implements IMeshVizCanvas, IVizUpdateableListener {

	/** Factory class to be implemented by renderer implementations. */
	private static final String GEOMETRY_VIEWER_FACTORY = "org.eclipse.ice.viz.service.geometry.viewer.factory.GeometryViewerFactory";

	/**
	 * Number of dimensions supported for visualization. GeometryCanvas supports
	 * 3D views.
	 */
	private static final int SUPPORTED_AXES = 3;

	/** The JFace viewer for displaying viz geometry. */
	private FXMeshViewer viewer;

	/** The geometry object that is currently set on the viewer. */
	private AbstractController mesh;

	/** The active rootnode in the scene. */
	private INode rootNode;

	/** The active root geometry. */
	private GeometryAttachment rootGeometry;

	/** ICE properties. */
	private Map<String, String> properties;

	private ArrayList<ISelectionListener> listeners;

	/**
	 * <p>
	 * Creates a canvas for the supplied geometry.
	 * </p>
	 * 
	 * @param geometry
	 *            ICE Geometry instance to visualizer in the canvas.
	 */
	public FXMeshCanvas(AbstractController mesh) {

		// Initialize the data members
		this.mesh = mesh;
		listeners = new ArrayList<ISelectionListener>();

	}

	/**
	 * @see IVizCanvas#draw(Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {
		Composite viewerParent = new Composite(parent, SWT.NONE);
		viewerParent.setLayout(new FillLayout());
		// viewerParent.setLayout(new GridLayout());
		// viewerParent
		// .setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		viewerParent.setBackground(new Color(null, 255, 0, 255));

		this.viewer = new FXMeshViewer(viewerParent);// materializeViewer(viewerParent);

		if (viewer == null) {
			throw new Exception("Error creating Mesh Viewer for canvas.");
		}

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {

				// IViewPart view = PlatformUI.getWorkbench()
				// .getActiveWorkbenchWindow().getActivePage()
				// .findView(TransformationView.ID);
				//
				// if (view == null || !(view instanceof TransformationView)) {
				// return;
				// }
				//
				// TransformationView transformView = (TransformationView) view;
				//
				// GeometrySelection selection = (GeometrySelection) event
				// .getSelection();
				//
				// transformView.setShape((Shape) selection.getShape());
			}
		});

		loadGeometry(mesh);

		return viewerParent;
	}

	/**
	 * <p>
	 * Fix for Eclipse/PDE's wonky fragment support. Creates a GeometryViewer
	 * supplied by an implementation fragment.
	 * </p>
	 * 
	 * @param viewerParent
	 *            the parent widget
	 * 
	 * @return a concrete implementation of GeometryViewer
	 * 
	 * @throws Exception
	 *             throws an exception if a concrete implementation cannot be
	 *             found
	 */
	private GeometryViewer materializeViewer(Composite viewerParent)
			throws Exception {
		try {
			return new FXMeshViewer(viewerParent);

		} catch (Exception e) {
			throw new Exception("", e); //$NON-NLS-1$
		}
	}

	/**
	 * <p>
	 * Handles the processing and setting up the canvas' supplied geometry for
	 * visualization.
	 * </p>
	 * 
	 * @param geometry
	 *            the geometry to visualize
	 */
	private void loadGeometry(AbstractController geometry) {
		if (geometry == null) {
			return;
		}

		this.mesh = geometry;

		rootNode = new GNode();

		IRenderer renderer = viewer.getRenderer();

		rootGeometry = (GeometryAttachment) renderer
				.createAttachment(GeometryAttachment.class);
		rootGeometry.addGeometry(geometry);

		rootNode.attach(rootGeometry);

		viewer.setInput(rootNode);

		geometry.register(this);
	}

	/**
	 * <p>
	 * Returns the underlying GeometryViewer.
	 * </p>
	 * 
	 * @return a GeometryViewer instance that is used to visualize scenes
	 */
	public GeometryViewer getViewer() {
		return viewer;
	}

	/**
	 * <p>
	 * Listens for updates coming in from the geometry provider.
	 * </p>
	 * 
	 * @see IVizUpdateable#update
	 */
	@Override
	public void update(final IVizUpdateable component) {

		// Invoke this on the JavaFX UI thread
		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {
				if (component == mesh) {
					// rootGeometry.addGeometry(geometry);
				}
			}
		});
	}

	/**
	 * @see IVizCanvas#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return null;
	}

	/**
	 * @see IVizCanvas#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		return SUPPORTED_AXES;
	}

	/**
	 * @see IVizCanvas#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		if (properties == null) {
			return Collections.emptyMap();
		} else {
			return properties;
		}
	}

	/**
	 * @see IVizCanvas#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		return null;
	}

	/**
	 * @see IVizCanvas#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return false;
	}

	/**
	 * @see IVizCanvas#redraw()
	 */
	@Override
	public void redraw() {
	}

	/**
	 * @see IVizCanvas#setProperties()
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		this.properties = props;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setEditMode(boolean)
	 */
	@Override
	public void setEditMode(boolean edit) {
		viewer.setEditSelectionHandeling(edit);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setVisibleHUD(boolean)
	 */
	@Override
	public void setVisibleHUD(boolean on) {
		viewer.setHUDVisible(on);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setVisibleAxis(boolean)
	 */
	@Override
	public void setVisibleAxis(boolean on) {
		viewer.setAxisVisible(on);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * HUDIsVisible()
	 */
	@Override
	public boolean HUDIsVisible() {
		return viewer.isHUDVisible();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * AxisAreVisible()
	 */
	@Override
	public boolean AxisAreVisible() {
		return viewer.getAxisVisible();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * deleteSelection()
	 */
	@Override
	public void deleteSelection() {

		// Check each polygon in the mesh to see if it should be deleted
		for (AbstractController polygon : mesh.getEntities()) {

			// Whether or not the polygon is completely selected
			boolean selected = true;

			// Check each of the polygon's vertices
			for (AbstractController vertex : polygon
					.getEntitiesByCategory("Vertices")) {

				// If any vertex is not selected, stop and move on to the next
				// polygon
				if (!"True".equals(vertex.getProperty("Selected"))) {
					selected = false;
					break;
				}
			}

			// If all the vertices were selected, remove the polygon form the
			// mesh
			if (selected) {
				mesh.removeEntity(polygon);
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas#
	 * setSelection(java.lang.Object[])
	 */
	@Override
	public void setSelection(Object[] selection) {

		// Start by deselecting everything
		for (AbstractController polygon : mesh.getEntities()) {
			polygon.setProperty("Selected", "False");
		}

		for (AbstractController polygon : mesh.getEntities()) {

			// If a polygon is in the selection, set it as selected. (This will
			// also select all its children).
			for (Object meshSelection : selection) {
				if (((MeshSelection) meshSelection).selectedMeshPart == polygon) {
					polygon.setProperty("Selected", "True");
				}

				// If the polygon wasn't selected, check each of its children to
				// see
				// if they were.
				else {
					for (AbstractController part : polygon.getEntities()) {
						if (((MeshSelection) meshSelection).selectedMeshPart == part) {
							part.setProperty("Selected", "True");
						}
					}
				}
			}
		}

	}

}
