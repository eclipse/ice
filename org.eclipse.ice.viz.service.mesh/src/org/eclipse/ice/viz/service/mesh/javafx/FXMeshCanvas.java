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
import org.eclipse.ice.viz.service.geometry.shapes.FXShapeControllerFactory;
import org.eclipse.ice.viz.service.geometry.viewer.GeometryViewer;
import org.eclipse.ice.viz.service.geometry.viewer.IRenderer;
import org.eclipse.ice.viz.service.mesh.datastructures.IMeshVizCanvas;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.Edge;
import org.eclipse.ice.viz.service.modeling.EdgeAndVertexFaceComponent;
import org.eclipse.ice.viz.service.modeling.EdgeComponent;
import org.eclipse.ice.viz.service.modeling.Face;
import org.eclipse.ice.viz.service.modeling.Vertex;
import org.eclipse.ice.viz.service.modeling.VertexComponent;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;

import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.shape.Shape3D;

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
	 * A handler which places new polygons on the screen based on mouse clicks.
	 */
	private EventHandler<MouseEvent> addHandler;

	/**
	 * A handler which allows the user to select and drag vertices with the
	 * mouse.
	 */
	private EventHandler<MouseEvent> editHandler;

	/**
	 * A list of vertices currently selected by the user, because they were
	 * selected in edit mode or were input vertices which have not yet been
	 * formed into a complete polygon in add mode.
	 */
	private ArrayList<AbstractController> selectedVertices;

	/**
	 * A list of edges input by the user which have not yet been formed into a
	 * complete polygon
	 */
	private ArrayList<AbstractController> tempEdges;

	/**
	 * The factory responsible for creating views/controllers for new model
	 * components.
	 */
	private FXShapeControllerFactory factory;

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
		factory = new FXShapeControllerFactory();
		selectedVertices = new ArrayList<AbstractController>();
		tempEdges = new ArrayList<AbstractController>();

		// Create the handler for add mode
		addHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				// Get the user's selection
				PickResult pickResult = event.getPickResult();
				Node intersectedNode = pickResult.getIntersectedNode();

				// Whether or not a new vertex has been added
				boolean changed = false;

				// If the user clicked a shape, try to add it to a polygon
				if (intersectedNode instanceof Shape3D) {

					// Resolve the parent
					Group nodeParent = (Group) intersectedNode.getParent();

					// Resolve the shape
					AbstractController modelShape = (AbstractController) nodeParent
							.getProperties().get(AbstractController.class);

					// If the vertex is already in the polygon currently being
					// constructed, ignore it
					if (selectedVertices.contains(modelShape)) {
						return;
					}

					// If the selected shape is a vertex, add it to the list
					if (modelShape instanceof Vertex) {
						selectedVertices.add(modelShape);
						changed = true;

						// Change the vertex's color to show that it is part of
						// the new polygon
						modelShape.setProperty("Constructing", "True");
					}

				}

				// If the user didn't select a shape, add a new shape where they
				// clicked
				else {

					// Get the click location
					Point3D location = pickResult.getIntersectedPoint();

					// Create a new vertex at that point
					VertexComponent tempComponent = new VertexComponent(
							location.getX(), 0, location.getZ());
					Vertex tempVertex = (Vertex) factory
							.createController(tempComponent);

					// Add the new vertex to the list
					selectedVertices.add(tempVertex);
					changed = true;
				}

				// If a new vertex was added, then construct edges/polygons as
				// needed
				if (changed) {

					// The number of vertices in the polygon under construction
					int numVertices = selectedVertices.size();

					// If this is not the first vertex, create an edge between
					// it and the last one
					if (numVertices > 1) {
						EdgeComponent tempComponent = new EdgeComponent();
						Edge tempEdge = (Edge) factory
								.createController(tempComponent);
						tempEdge.addEntity(selectedVertices.get(numVertices - 2));
						tempEdge.addEntity(selectedVertices.get(numVertices - 1));

						// Add the edge to the list
						tempEdges.add(tempEdge);
					}

					// If this was the fourth vertex, the quadrilateral is done
					// so finish up the polygon
					if (numVertices == 4) {

						// Crete an edge between the last vertex and the first
						EdgeComponent tempComponent = new EdgeComponent();
						Edge tempEdge = (Edge) factory
								.createController(tempComponent);
						tempEdge.addEntity(selectedVertices.get(numVertices - 1));
						tempEdge.addEntity(selectedVertices.get(0));

						tempEdges.add(tempEdge);

						// Create a face out of all the edges
						EdgeAndVertexFaceComponent faceComponent = new EdgeAndVertexFaceComponent();
						Face newFace = (Face) factory
								.createController(faceComponent);

						for (AbstractController edge : tempEdges) {
							newFace.addEntity(edge);
						}

						// Set the new polygon to the default color
						newFace.setProperty("Constructing", "False");

						// Empty the lists of temporary constructs
						selectedVertices = new ArrayList<AbstractController>();
						tempEdges = new ArrayList<AbstractController>();
					}
				}
			}
		};
		
		//Create the handler for edit mode
		editHandler = new EventHandler<MouseEvent>(){

			@Override
			public void handle(MouseEvent event) {
				// Get the user's selection
				PickResult pickResult = event.getPickResult();
				Node intersectedNode = pickResult.getIntersectedNode();
				
				if (intersectedNode instanceof Shape3D){
					// Resolve the parent
					Group nodeParent = (Group) intersectedNode.getParent();
					
					// Resolve the shape
					AbstractController modelShape = (AbstractController) nodeParent
							.getProperties().get(AbstractController.class);
					
					if (modelShape instanceof Vertex){
						
						if()
					}
				}
				
				
			}
			
		};
	}

	/**
	 * @see IVizCanvas#draw(Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {
		Composite viewerParent = new Composite(parent, SWT.NONE);
		viewerParent.setLayout(new FillLayout());

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

	@Override
	public void setEditMode(boolean edit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVisibleHUD(boolean on) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setVisibleAxis(boolean on) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean HUDIsVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean AxisAreVisible() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void deleteSelection() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSelection(Object[] selection) {
		// TODO Auto-generated method stub

	}

}
