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

import java.util.ArrayList;

import org.eclipse.ice.viz.service.geometry.GeometrySelection;
import org.eclipse.ice.viz.service.geometry.scene.base.GeometryAttachment;
import org.eclipse.ice.viz.service.geometry.scene.base.ICamera;
import org.eclipse.ice.viz.service.geometry.shapes.FXShapeControllerFactory;
import org.eclipse.ice.viz.service.geometry.viewer.GeometryViewer;
import org.eclipse.ice.viz.service.javafx.internal.FXContentProvider;
import org.eclipse.ice.viz.service.javafx.internal.model.FXCameraAttachment;
import org.eclipse.ice.viz.service.javafx.internal.model.FXRenderer;
import org.eclipse.ice.viz.service.javafx.internal.model.geometry.FXGeometryAttachmentManager;
import org.eclipse.ice.viz.service.javafx.internal.scene.TransformGizmo;
import org.eclipse.ice.viz.service.javafx.internal.scene.camera.CameraController;
import org.eclipse.ice.viz.service.javafx.internal.scene.camera.TopDownController;
import org.eclipse.ice.viz.service.modeling.AbstractController;
import org.eclipse.ice.viz.service.modeling.Edge;
import org.eclipse.ice.viz.service.modeling.EdgeAndVertexFaceComponent;
import org.eclipse.ice.viz.service.modeling.EdgeComponent;
import org.eclipse.ice.viz.service.modeling.Face;
import org.eclipse.ice.viz.service.modeling.Vertex;
import org.eclipse.ice.viz.service.modeling.VertexComponent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import javafx.embed.swt.FXCanvas;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Shape3D;

/**
 * <p>
 * JavaFX implementation of GeometryViewer.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public class FXMeshViewer extends GeometryViewer {

	/** The root JavaFX widget that displays content. */
	private FXCanvas fxCanvas;

	/**
	 * The internally used root that cannot be modified by clients.
	 */
	private Group internalRoot;

	/** The root of the scene as exposed to clients. */
	private Group root;

	/** The active scene displayed to the end user. */
	private Scene scene;

	/**
	 * The content provider that generates JavaFX scene data from the geometry
	 * editor scene model.
	 */
	private FXContentProvider contentProvider;

	/** Default camera controller. */
	private CameraController cameraController;

	/** Default camera. */
	private Camera defaultCamera;

	/**
	 * A handler which places new polygons on the screen based on mouse clicks.
	 */
	private EventHandler<MouseEvent> addHandler;

	/**
	 * A handler which allows the user to select vertices with the mouse.
	 */
	private EventHandler<MouseEvent> editHandler;

	/**
	 * A handler which allows the user to drag selected vertices, having their
	 * movements displayed on the screen and their final position update once
	 * the drag ends
	 */
	private EventHandler<MouseEvent> editDragHandler;

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
	 * The mouse's last recorded x position
	 */
	private double mouseOldX;

	/**
	 * The mouse's last recorded y position.
	 */
	private double mouseOldY;

	/**
	 * The mouse's current x position
	 */
	private double mousePosX;

	/**
	 * The mouse's current y position.
	 */
	private double mousePosY;

	/**
	 * <p>
	 * Creates a JavaFX GeometryViewer.
	 * </p>
	 * 
	 * @param parent
	 */
	public FXMeshViewer(Composite parent) {
		super(parent);

		renderer = new FXRenderer();
		renderer.register(GeometryAttachment.class,
				new FXGeometryAttachmentManager());

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
						tempEdge.addEntity(
								selectedVertices.get(numVertices - 2));
						tempEdge.addEntity(
								selectedVertices.get(numVertices - 1));

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
						tempEdge.addEntity(
								selectedVertices.get(numVertices - 1));
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

		// Start with the add mode by default
		scene.setOnMouseClicked(addHandler);

		// Create the handler for edit mode
		editHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// Get the user's selection
				PickResult pickResult = event.getPickResult();
				Node intersectedNode = pickResult.getIntersectedNode();

				if (intersectedNode instanceof Shape3D) {
					// Resolve the parent
					Group nodeParent = (Group) intersectedNode.getParent();

					// Resolve the shape
					AbstractController modelShape = (AbstractController) nodeParent
							.getProperties().get(AbstractController.class);

					// If the user clicked a vertex, handle it
					if (modelShape instanceof Vertex) {

						// If shift is down, add the vertex to the selection
						if (event.isShiftDown()) {
							selectedVertices.add(modelShape);
							modelShape.setProperty("Selected", "True");
						}

						// If shift is not down and control is, either add the
						// vertex to the selection if it is not present already
						// or remove it if it is.
						else if (event.isControlDown()) {
							if (selectedVertices.contains(modelShape)) {
								selectedVertices.remove(modelShape);
								modelShape.setProperty("Selected", "False");
							}

							else {
								selectedVertices.add(modelShape);
								modelShape.setProperty("Selected", "True");
							}
						}

						// If nothing is pressed, select that vertex and nothing
						// else
						else {
							for (AbstractController vertex : selectedVertices) {
								vertex.setProperty("Selected", "False");
							}
							selectedVertices.clear();

							selectedVertices.add(modelShape);
							modelShape.setProperty("Selected", "True");
						}
					}
				}
			}
		};

		editDragHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getPickResult()
						.getIntersectedNode() instanceof Shape3D) {
					mouseOldX = mousePosX;
					mouseOldY = mousePosY;
					mousePosX = event.getX();
					mousePosY = event.getY();
					double mouseDeltaX = (mousePosX - mouseOldX);
					double mouseDeltaY = (mousePosY - mouseOldY);

					// Get the user's selection
					PickResult pickResult = event.getPickResult();
					Node intersectedNode = pickResult.getIntersectedNode();

					// If the user is not dragging a shape, ignroe the motion
					if (intersectedNode instanceof Shape3D) {
						// Resolve the parent
						Group nodeParent = (Group) intersectedNode.getParent();

						// Resolve the shape
						AbstractController modelShape = (AbstractController) nodeParent
								.getProperties().get(AbstractController.class);

						// If the shape is a selected vertex, drag it
						if (selectedVertices.contains(modelShape)) {

							// Get the selected shape's coordinates
							double[] originalCoords = modelShape
									.getTranslation();

							// Move each vertex
							for (AbstractController vertex : selectedVertices) {

								// Get this vertex's coordinates
								double[] currentCoords = modelShape
										.getTranslation();

								// Move the vertex to the mouse's current
								// position, offset by the original distance
								// between the vertices.
								vertex.setTranslation(
										currentCoords[0] - originalCoords[0]
												+ mousePosX,
										0d, currentCoords[2] - originalCoords[2]
												+ mousePosY);
							}

						}

					}

				}
				;
			}
		};
	}

	/**
	 * <p>
	 * Creates an FXCanvas control and initializes a default empty JavaFX scene.
	 * </p>
	 */
	@Override
	protected void createControl(Composite parent) {
		contentProvider = new FXContentProvider();

		fxCanvas = new FXCanvas(parent, SWT.NONE);

		// Create the root nodes
		internalRoot = new Group();
		root = new Group();

		internalRoot.getChildren().add(root);

		setupSceneInternals(internalRoot);

		scene = new Scene(internalRoot, 100, 100, true);

		// Set the scene's background color
		scene.setFill(Color.rgb(24, 30, 31));

		// Setup camera and input
		createDefaultCamera(internalRoot);
		wireSelectionHandling();

		// Set a handler for clearing the current selection
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				// If Escape is pressed, all vertices will be deselected and any
				// polygon under construction will be removed
				if (event.getCode() == KeyCode.ESCAPE) {
					selectedVertices.clear();
					tempEdges.clear();
				}
			}
		});

		fxCanvas.setScene(scene);
	}

	/**
	 * <p>
	 * Creates the current geometry editor camera.
	 * </p>
	 * 
	 * @param parent
	 *            the parent to create the camera on
	 * 
	 */
	private void createDefaultCamera(Group parent) {
		PerspectiveCamera perspCamera = new PerspectiveCamera(true);
		perspCamera.setNearClip(0.1);
		perspCamera.setFarClip(4000.0);
		perspCamera.setFieldOfView(35);
		perspCamera.setTranslateX(0);
		perspCamera.setTranslateY(0);

		parent.getChildren().add(perspCamera);

		// Hacked in camera (for now)
		FXCameraAttachment cameraAttachment = new FXCameraAttachment(
				perspCamera);
		setCamera(cameraAttachment);
	}

	/**
	 * <p>
	 * Hooks up JavaFX picking with JFace selections.
	 * </p>
	 */
	private void wireSelectionHandling() {

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {

			Group lastSelection = null;

			@Override
			public void handle(MouseEvent event) {
				// Pick
				PickResult pickResult = event.getPickResult();

				Node intersectedNode = pickResult.getIntersectedNode();

				if (intersectedNode == null) {
					return;
				}

				if (!(intersectedNode instanceof Shape3D)) {
					return;
				}

				// Resolve the parent
				Group nodeParent = (Group) intersectedNode.getParent();

				if (nodeParent == lastSelection) {
					return;
				}

				// Resolve the shape
				AbstractController modelShape = (AbstractController) nodeParent
						.getProperties().get(AbstractController.class);

				if (modelShape == null) {
					return;
				}

				// Create and set the viewer selection
				// (event gets fired in parent class)
				GeometrySelection selection = new GeometrySelection(modelShape);

				setSelection(selection);

				// nodeParent.setSelected(true);

				// if (lastSelection != null) {
				// lastSelection.setSelected(false);
				// }

				lastSelection = nodeParent;
			}
		});

	}

	/**
	 * <p>
	 * Creates scene elements that aren't meant to be manipulated by the user
	 * (markers, camera, etc.)
	 * </p>
	 */
	private void setupSceneInternals(Group parent) {
		// Create scene plane for frame of reference.
		Box box = new Box(1000, 0, 1000);
		box.setMouseTransparent(true);
		box.setDrawMode(DrawMode.LINE);
		box.setMaterial(new PhongMaterial(Color.ANTIQUEWHITE));

		AmbientLight ambientLight = new AmbientLight(Color.rgb(100, 100, 100));

		PointLight light1 = new PointLight(Color.ANTIQUEWHITE);
		light1.setMouseTransparent(true);
		light1.setTranslateY(-350);

		PointLight light2 = new PointLight(Color.ANTIQUEWHITE);
		light2.setMouseTransparent(true);
		light2.setTranslateZ(350);

		PointLight light3 = new PointLight(Color.ANTIQUEWHITE);
		light3.setMouseTransparent(true);
		light3.setTranslateZ(-350);

		PointLight light4 = new PointLight(Color.ANTIQUEWHITE);
		light4.setMouseTransparent(true);
		light4.setTranslateZ(350);

		TransformGizmo gizmo = new TransformGizmo(1000);
		gizmo.showHandles(false);

		parent.getChildren().addAll(gizmo, box, light1, light2, light3, light4,
				ambientLight);

	}

	/**
	 * <p>
	 * Handles recreating the scene when the input changes.
	 * </p>
	 * 
	 * @see Viewer#inputChanged(Object, Object)
	 */
	@Override
	protected void inputChanged(Object oldInput, Object newInput) {
		contentProvider.inputChanged(this, newInput, input);
	}

	/**
	 * @see GeometryViewer#updateCamera(ICamera)
	 */
	@Override
	protected void updateCamera(ICamera camera) {
		if (!(camera instanceof FXCameraAttachment)) {
			throw new IllegalArgumentException(
					"Invalid camera attached to Mesh Viewer.");
		}

		FXCameraAttachment attachment = (FXCameraAttachment) camera;
		Camera fxCamera = attachment.getFxCamera();

		if (fxCamera == null) {
			throw new NullPointerException(
					"No camera was attached to Mesh Viewer");
		}

		cameraController = new TopDownController(fxCamera, scene, fxCanvas);

		scene.setCamera(fxCamera);

		defaultCamera = fxCamera;
	}

	/**
	 * @see Viewer#getClass()
	 */
	@Override
	public Control getControl() {
		return fxCanvas;
	}

	/**
	 * @see Viewer#refresh()
	 */
	@Override
	public void refresh() {

	}

	/**
	 * 
	 * @return
	 */
	public FXCanvas getFxCanvas() {
		return fxCanvas;
	}

	/**
	 * 
	 * @return
	 */
	public Group getRoot() {
		return root;
	}

	/**
	 * 
	 * @return
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * 
	 * @return
	 */
	public FXContentProvider getContentProvider() {
		return contentProvider;
	}

	/**
	 * 
	 * @param contentProvider
	 */
	public void setContentProvider(FXContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	public CameraController getCameraController() {
		return cameraController;
	}

	public Camera getDefaultCamera() {
		return defaultCamera;
	}

	/**
	 * Provide a handler defining the editor's behavior upon mouse use to the
	 * viewer.
	 * 
	 * @param handler
	 *            The EventHandler which will process mouse input for this
	 *            viewer.
	 */
	public void setEditSelectionHandeling(boolean edit) {
		if(edit){
			scene.setOnMouseClicked(editHandler);
			scene.setOnMouseDragged(editDragHandler);
		}
		else{
			scene.setOnMouseClicked(addHandler);
			scene.removeEventHandler(EventType., editDragHandler);
		}
	}

}