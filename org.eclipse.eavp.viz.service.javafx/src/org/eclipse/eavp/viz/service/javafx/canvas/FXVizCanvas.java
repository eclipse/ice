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
package org.eclipse.eavp.viz.service.javafx.canvas;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import org.eclipse.eavp.viz.service.IVizCanvas;
import org.eclipse.eavp.viz.service.javafx.scene.base.GNode;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.eclipse.eavp.viz.service.modeling.BasicController;
import org.eclipse.eavp.viz.service.modeling.IController;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * A generic JavaFX visualization canvas.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 * 
 */
public class FXVizCanvas implements IVizCanvas {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(FXVizCanvas.class);

	/** Factory class to be implemented by renderer implementations. */
	private static final String GEOMETRY_VIEWER_FACTORY = "org.eclipse.eavp.viz.service.geometry.viewer.factory.GeometryViewerFactory";

	/**
	 * Number of dimensions supported for visualization. GeometryCanvas supports
	 * 3D views.
	 */
	private static final int SUPPORTED_AXES = 3;

	/** The JFace viewer for displaying viz geometry. */
	protected BasicViewer viewer;

	/** The modeling object that is currently set on the viewer. */
	protected IController root;

	/** The active rootnode in the scene. */
	private INode rootNode;

	/** The active root geometry. */
	protected BasicAttachment rootAtachment;

	/** ICE properties. */
	private Map<String, String> properties;

	/**
	 * <p>
	 * Creates a canvas for the supplied geometry.
	 * </p>
	 * 
	 * @param geometry
	 *            ICE Geometry instance to visualizer in the canvas.
	 */
	public FXVizCanvas(IController geometry) {
		this.root = geometry;
	}

	/**
	 * @see IVizCanvas#draw(Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {
		Composite viewerParent = new Composite(parent, SWT.FILL);
		viewerParent.setBackground(new Color(null, 255, 0, 255));
		viewerParent.setLayout(new FillLayout());

		this.viewer = materializeViewer(viewerParent);

		if (viewer == null) {
			throw new Exception("Could not create JavaFX canvas.");
		}

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				handleSelectionChanged(event);
			}
		});

		// Create an attachment for the canvas
		createAttachment();

		loadPart(root);

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
	protected BasicViewer materializeViewer(Composite viewerParent)
			throws Exception {
		try {
			return new FXViewer(viewerParent);

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
	 * @param root
	 *            the geometry to visualize
	 */
	protected void loadPart(IController root) {
		if (root == null) {
			return;
		}

		this.root = root;

		rootNode = new GNode();

		createAttachment();
		rootAtachment.addGeometry(root);

		rootNode.attach(rootAtachment);

		viewer.setInput(rootNode);
	}

	/**
	 * Adds a part as a top level node of the scene graph that is contained
	 * directly by the canvas's attachment.
	 * 
	 * @param part
	 *            The part to be added
	 */
	public void addRoot(BasicController part) {
		rootAtachment.addGeometry(part);
	}

	/**
	 * Directs the renderer to create a new root attachment of the appropriate
	 * type.
	 */
	protected void createAttachment() {
		rootAtachment = (BasicAttachment) viewer.getRenderer()
				.createAttachment(FXAttachment.class);
	}

	/**
	 * A function to be called by the viewer when a selection event is fired.
	 * This function does nothing by default, and is intended to be overwritten
	 * by subclasses.
	 * 
	 * @param event
	 *            The event which caused the update to occur
	 */
	protected void handleSelectionChanged(SelectionChangedEvent event) {
		// Nothing to do
	}

	/**
	 * Removes one of the top level nodes of the scene graph that is contained
	 * directly by the canvas's attachment.
	 * 
	 * @param part
	 *            The part to be removed
	 */
	public void removeRoot(IController part) {
		rootAtachment.removeGeometry(part);
	}

	/**
	 * <p>
	 * Returns the underlying GeometryViewer.
	 * </p>
	 * 
	 * @return a GeometryViewer instance that is used to visualize scenes
	 */
	public BasicViewer getViewer() {
		return viewer;
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

}
