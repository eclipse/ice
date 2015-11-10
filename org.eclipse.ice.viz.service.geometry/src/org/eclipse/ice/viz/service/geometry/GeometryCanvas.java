/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.ice.viz.service.geometry;

import java.lang.reflect.Method;
import java.net.URI;
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
import org.eclipse.ice.viz.service.geometry.widgets.TransformationView;
import org.eclipse.ice.viz.service.modeling.Shape;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;

/**
 * <p>
 * GeometryCanvas provides the ability to visualize and manipulate 3D geometry
 * data.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 * 
 */
public class GeometryCanvas implements IVizCanvas, IVizUpdateableListener {

	/** Factory class to be implemented by renderer implementations. */
	private static final String GEOMETRY_VIEWER_FACTORY = "org.eclipse.ice.viz.service.geometry.viewer.factory.GeometryViewerFactory";

	/**
	 * Number of dimensions supported for visualization. GeometryCanvas supports
	 * 3D views.
	 */
	private static final int SUPPORTED_AXES = 3;

	/** The JFace viewer for displaying viz geometry. */
	private GeometryViewer viewer;

	/** The geometry object that is currently set on the viewer. */
	private Shape geometry;

	/** The active rootnode in the scene. */
	private INode rootNode;

	/** The active root geometry. */
	private GeometryAttachment rootGeometry;

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
	public GeometryCanvas(Shape geometry) {
		this.geometry = geometry;
	}

	/**
	 * @see IVizCanvas#draw(Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {
		Composite viewerParent = new Composite(parent, SWT.NONE);
		viewerParent.setLayout(new FillLayout());

		this.viewer = materializeViewer(viewerParent);

		if (viewer == null) {
			throw new Exception(Messages.GeometryCanvas_ErrorCreatingViewer);
		}

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IViewPart view = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
						.findView(TransformationView.ID);

				if (view == null || !(view instanceof TransformationView)) {
					return;
				}

				TransformationView transformView = (TransformationView) view;

				GeometrySelection selection = (GeometrySelection) event.getSelection();

				transformView.setShape(selection.getShape());
			}
		});

		loadGeometry(geometry);

		return parent;
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
	private GeometryViewer materializeViewer(Composite viewerParent) throws Exception {
		try {
			Class<?> viewerFactory = Class.forName(GEOMETRY_VIEWER_FACTORY); // $NON-NLS-1$

			if (viewerFactory == null) {
				throw new Exception(""); //$NON-NLS-1$
			}

			Object newInstance = viewerFactory.newInstance();
			Method method = newInstance.getClass().getMethod("createViewer", Composite.class); //$NON-NLS-1$
			return (GeometryViewer) method.invoke(newInstance, viewerParent);

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
	private void loadGeometry(Shape geometry) {
		if (geometry == null) {
			return;
		}

		this.geometry = geometry;

		rootNode = new GNode();

		IRenderer renderer = viewer.getRenderer();

		rootGeometry = (GeometryAttachment) renderer.createAttachment(GeometryAttachment.class);
		rootGeometry.addGeometry(geometry);

		rootNode.attach(rootGeometry);

		viewer.setInput(rootNode);

		// geometry.register(this);
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
				if (component == geometry) {
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

}
