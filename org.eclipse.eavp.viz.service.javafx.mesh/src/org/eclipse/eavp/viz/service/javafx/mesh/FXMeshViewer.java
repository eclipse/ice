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
package org.eclipse.eavp.viz.service.javafx.mesh;

import java.text.DecimalFormat;
import java.util.ArrayList;

import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.eavp.viz.service.javafx.canvas.FXViewer;
import org.eclipse.eavp.viz.service.javafx.internal.model.FXCameraAttachment;
import org.eclipse.eavp.viz.service.javafx.internal.scene.camera.TopDownCameraController;
import org.eclipse.eavp.viz.service.javafx.mesh.datatypes.FXMeshControllerFactory;
import org.eclipse.eavp.viz.service.javafx.mesh.datatypes.FXVertexController;
import org.eclipse.eavp.viz.service.javafx.scene.base.ICamera;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonController;
import org.eclipse.eavp.viz.service.mesh.datastructures.NekPolygonMesh;
import org.eclipse.eavp.viz.service.mesh.properties.MeshSelection;
import org.eclipse.eavp.viz.service.modeling.AbstractController;
import org.eclipse.eavp.viz.service.modeling.AbstractMesh;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.EdgeController;
import org.eclipse.eavp.viz.service.modeling.FaceController;
import org.eclipse.eavp.viz.service.modeling.FaceEdgeMesh;
import org.eclipse.eavp.viz.service.modeling.VertexController;
import org.eclipse.eavp.viz.service.modeling.VertexMesh;
import org.eclipse.swt.widgets.Composite;

import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PointLight;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Shape3D;
import javafx.scene.shape.Sphere;
import javafx.scene.text.Text;

/**
 * <p>
 * JavaFX implementation of GeometryViewer.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 * @author Robert Smith
 *
 */
public class FXMeshViewer extends FXViewer {

	/**
	 * The number of units long each side of the squares in the grid will be
	 */
	final protected double SCALE = 3d;

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
	 * A handler which moves vertices at the end of a drag action in edit mode
	 */
	private EventHandler<MouseEvent> editMouseUpHandler;

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
	private FXMeshControllerFactory factory;

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
	 * A JavaFX Text shape displaying the mouse cursor's current x and y
	 * coordinates.
	 */
	private Text cursorPosition;

	/**
	 * A root model part in which temporary vertices and edges will be held
	 * before being added to the mesh permanently. These are maintained
	 * separately so that such parts will not appear in the tree view until
	 * their parent polygon is completed.
	 */
	private AbstractController tempRoot = new AbstractController(
			new AbstractMesh(), new AbstractView());

	/**
	 * A list of displayed circles to show the user the location that selectice
	 * vertices are being dragged to.
	 */
	private ArrayList<Sphere> vertexMarkers;

	/**
	 * The gizmo containing the axis.
	 */
	protected AxisGridGizmo gizmo;

	/**
	 * The manager for attachments to the renderer
	 */
	private FXMeshAttachmentManager attachmentManager;

	/**
	 * Whether or not a drag mouse motion is in progress.
	 */
	boolean dragStarted = false;

	/**
	 * An ordered list of each selected vertex's x coordinate relative to the
	 * vertex being dragged
	 */
	ArrayList<Double> relativeXCords = new ArrayList<Double>();

	/**
	 * An ordered list of each selected vertex's y coordinate relative to the
	 * vertex being dragged
	 */
	ArrayList<Double> relativeYCords = new ArrayList<Double>();

	/**
	 * The next unused number to assign as a Vertex's ID.
	 */
	int nextVertexID = 1;

	/**
	 * The next unused number to assign as an Edge's ID.
	 */
	int nextEdgeID = 1;

	/**
	 * The next unused number to assign as a Polygon's ID.
	 */
	int nextPolygonID = 1;

	/**
	 * <p>
	 * Creates a JavaFX GeometryViewer.
	 * </p>
	 * 
	 * @param parent
	 */
	public FXMeshViewer(Composite parent) {
		super(parent);

		// Initialize the class variables
		attachmentManager = new FXMeshAttachmentManager();
		renderer.register(FXMeshAttachment.class, attachmentManager);

		factory = new FXMeshControllerFactory();
		selectedVertices = new ArrayList<AbstractController>();
		tempEdges = new ArrayList<AbstractController>();
		vertexMarkers = new ArrayList<Sphere>();

		// Create the handler for add mode
		addHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				handleAddModeEvent(event);
			}
		};

		// Start with the add mode by default
		scene.setOnMouseClicked(addHandler);

		// Create the handler for edit mode
		editHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				handleEditModeClick(event);
			}
		};

		// Create the handler for edit mode dragging
		editDragHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				handleEditModeDrag(event);
			}
		};

		// Create the handler for edit mode releasing the mouse button
		editMouseUpHandler = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				handleEditModeMouseUp(event);
			}

		};

		// Create the handler for moving the mouse
		scene.setOnMouseMoved(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				handleMouseMoved(event);
			}
		});
	}

	/**
	 * <p>
	 * Creates an FXCanvas control and initializes a default empty JavaFX scene.
	 * </p>
	 */
	@Override
	protected void createControl(Composite parent) {
		super.createControl(parent);

		// Get the current key handler from the camera
		final EventHandler<? super KeyEvent> handler = scene.getOnKeyPressed();

		// Set a handler for clearing the current selection
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				// If Escape or Backspace is pressed, any polygon under
				// construction will be
				// removed
				if (event.getCode() == KeyCode.ESCAPE
						|| event.getCode() == KeyCode.BACK_SPACE) {

					clearSelection();
				}

				// If Delete is pressed remove the currently selected polygons
				else if (event.getCode() == KeyCode.DELETE) {

					// Check each polygon in the mesh to see if it should be
					// deleted
					for (AbstractController polygon : ((FXAttachment) attachmentManager
							.getAttachments().get(1)).getKnownParts().get(0)
									.getEntities()) {

						// Whether or not the polygon is completely selected
						boolean selected = true;

						// Check each of the polygon's vertices
						for (AbstractController vertex : polygon
								.getEntitiesByCategory("Vertices")) {

							// If any vertex is not selected, stop and move on
							// to the next
							// polygon
							if (!"True"
									.equals(vertex.getProperty("Selected"))) {
								selected = false;
								break;
							}
						}

						// If all the vertices were selected, remove the polygon
						// from the
						// mesh
						if (selected) {
							((FXAttachment) attachmentManager.getAttachments()
									.get(1)).getKnownParts().get(0)
											.removeEntity(polygon);
						}
					}

					// Remove any selection
					clearSelection();
				}

				// If another key was pressed, invoke the camera's key handler
				else {
					handler.handle(event);
				}
			}
		});
	}

	/**
	 * The function called whenever the user clicks the mouse in Add Mode.
	 */
	private void handleAddModeEvent(MouseEvent event) {

		// Get the user's selection
		PickResult pickResult = event.getPickResult();
		Node intersectedNode = pickResult.getIntersectedNode();

		// Whether or not a new vertex has been added
		boolean changed = false;

		// If there are already four vertices already, confirm the addition of
		// the polygon
		if (selectedVertices.size() == 4) {

			// Create a face out of all the edges
			NekPolygonMesh faceComponent = new NekPolygonMesh();
			NekPolygonController newFace = (NekPolygonController) factory
					.createController(faceComponent);

			// Set the polygon's name and ID
			newFace.setProperty("Name", "Polygon");
			newFace.setProperty("Id", String.valueOf(nextPolygonID));
			nextPolygonID++;

			for (AbstractController edge : tempEdges) {
				newFace.addEntityByCategory(edge, "Edges");

				// Remove the edge from the temporary root
				tempRoot.removeEntity(edge);
			}

			// Remove the vertices from the temporary root
			for (AbstractController vertex : selectedVertices) {
				tempRoot.removeEntity(vertex);
			}

			// Set the new polygon to the default color
			newFace.setProperty("Constructing", "False");

			// Add the new polygon to the mesh permanently
			((FXAttachment) attachmentManager.getAttachments().get(1))
					.getKnownParts().get(0).addEntity(newFace);

			// Empty the lists of temporary constructs
			selectedVertices = new ArrayList<AbstractController>();
			tempEdges = new ArrayList<AbstractController>();

			return;
		}

		// If the user didn't select a shape, add a new shape where they
		// clicked
		if (intersectedNode instanceof Box) {

			// Create a new vertex at that point, divided by SCALE so that the
			// internal representation is kept separate from the size things are
			// being drawn at
			VertexMesh tempComponent = new VertexMesh(event.getX() / SCALE,
					event.getY() / SCALE, 0);
			tempComponent.setProperty("Constructing", "True");
			FXVertexController tempVertex = (FXVertexController) factory
					.createController(tempComponent);

			// Set the vertex's scale, name, and ID
			tempVertex.setApplicationScale((int) SCALE);
			tempVertex.setProperty("Name", "Vertex");
			tempVertex.setProperty("Id", String.valueOf(nextVertexID));
			nextVertexID++;

			// Add the new vertex to the list
			selectedVertices.add(tempVertex);

			// Add it to the temp root
			tempRoot.addEntity(tempVertex);

			// Add the temp root to the attachment
			((FXAttachment) attachmentManager.getAttachments().get(1))
					.addGeometry(tempRoot);

			tempVertex.refresh();
			changed = true;
		}

		// If the user clicked a shape, try to add it to a polygon
		else if (intersectedNode instanceof Shape3D) {

			// Resolve the parent
			Group nodeParent = (Group) intersectedNode.getParent();

			// Resolve the shape
			AbstractController modelShape = (AbstractController) nodeParent
					.getProperties().get(AbstractController.class);

			// If four or more vertices have already been selected
			// through some other method, then clear the selection and
			// start over
			if (selectedVertices.size() >= 4) {
				clearSelection();
			}

			// If the vertex is already in the polygon currently being
			// constructed, ignore it
			if (selectedVertices.contains(modelShape)) {
				return;
			}

			// If the selected shape is a vertex, add it to the list
			if (modelShape instanceof VertexController) {
				selectedVertices.add(modelShape);
				changed = true;

				// Change the vertex's color to show that it is part of
				// the new polygon
				modelShape.setProperty("Constructing", "True");
			}

		}

		// If a new vertex was added, then construct edges/polygons as
		// needed
		if (changed) {

			// The number of vertices in the polygon under construction
			int numVertices = selectedVertices.size();

			// If this is not the first vertex, create an edge between
			// it and the last one
			if (numVertices > 1) {

				EdgeController tempEdge = getEdge(
						(VertexController) selectedVertices
								.get(numVertices - 2),
						(VertexController) selectedVertices
								.get(numVertices - 1));

				// Add the edge to the list
				tempEdges.add(tempEdge);

				// Add it to the temp root
				tempRoot.addEntity(tempEdge);

				// Refresh the edge
				tempEdge.refresh();
			}

			// If this was the fourth vertex, the quadrilateral is done
			// so finish up the polygon
			if (numVertices == 4) {

				EdgeController tempEdge = getEdge(
						(VertexController) selectedVertices
								.get(numVertices - 1),
						(VertexController) selectedVertices.get(0));

				// Add the edge to the list
				tempEdges.add(tempEdge);

				// Add it to the temp root
				tempRoot.addEntity(tempEdge);

				// Refresh the edge
				tempEdge.refresh();
			}
		}
	}

	/**
	 * The function called whenever the user clicks the mouse while in Edit
	 * mode.
	 * 
	 * @param event
	 *            The event that prompted the invocation of this function.
	 */
	private void handleEditModeClick(MouseEvent event) {

		// Get the mouse position
		mousePosX = event.getSceneX();
		mousePosY = event.getSceneY();
		mouseOldX = event.getSceneX();
		mouseOldY = event.getSceneY();

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
			if (modelShape instanceof VertexController) {

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
					clearSelection();

					selectedVertices.add(modelShape);
					modelShape.setProperty("Selected", "True");
				}
			}
		}
	}

	/**
	 * The function called whenever the mouse is dragged in Edit Mode.
	 * 
	 * @param event
	 *            The event which prompted the invocation of this function.
	 */
	private void handleEditModeDrag(MouseEvent event) {

		// Get the mouse position
		mouseOldX = mousePosX;
		mouseOldY = mousePosY;
		mousePosX = event.getX();
		mousePosY = event.getY();

		// Get the user's selection
		PickResult pickResult = event.getPickResult();
		Node intersectedNode = pickResult.getIntersectedNode();

		// The drag has started, so continue dragging even if the
		// mouse has moved off a shape
		dragStarted = true;

		// Resolve the parent
		Group nodeParent = (Group) intersectedNode.getParent();

		// Resolve the shape
		AbstractController modelShape = (AbstractController) nodeParent
				.getProperties().get(AbstractController.class);

		// If the user has selected a vertex, drag it
		if (selectedVertices.contains(modelShape) || dragStarted) {

			// If the vertex markers have not yet been made,
			// create them
			if (vertexMarkers.isEmpty()) {

				// Get the location of the vertex which was clicked
				double[] cursorLocation = ((VertexController) modelShape)
						.getTranslation();

				for (AbstractController vertex : selectedVertices) {

					// Create the circle
					Sphere marker = new Sphere(1);

					// Place it at the vertex's position
					double[] position = ((VertexController) vertex)
							.getTranslation();
					marker.setTranslateX(position[0]);
					marker.setTranslateY(position[1]);

					// Add it to the list
					vertexMarkers.add(marker);

					// Get the relative position of this vertex from
					// the vertex being dragged
					relativeXCords.add(position[0] - cursorLocation[0]);
					relativeYCords.add(position[1] - cursorLocation[1]);

					((FXAttachment) attachmentManager.getAttachments().get(1))
							.getFxNode().getChildren().add(marker);

				}
			}

			// Move each vertex
			for (int i = 0; i < vertexMarkers.size(); i++) {

				// Get the vertex marker for this index
				Sphere marker = vertexMarkers.get(i);

				// The adjustments to the markers needed to keep them inside
				// the grid
				double xAdjust = 0;
				double yAdjust = 0;

				// Set the xAdjust so that all coordinates are within the
				// bounds of the grid, adjusted for drawing scale
				for (Double x : relativeXCords) {
					if (x + mousePosX + xAdjust < -16d * SCALE) {
						xAdjust = -16d * SCALE - x - mousePosX;
					} else if (x + mousePosX + xAdjust > 16d * SCALE) {
						xAdjust = 16d * SCALE - x - mousePosX;
					}
				}

				// Set the yAdjust so that all coordinates are within the
				// bounds of the grid, adjusted for drawing scale
				for (Double y : relativeYCords) {
					if (y + mousePosY + yAdjust < -8d * SCALE) {
						yAdjust = -8d * SCALE - y - mousePosY;
					} else if (y + mousePosY + yAdjust > 8d * SCALE) {
						yAdjust = 8d * SCALE - y - mousePosY;
					}
				}

				// Move the vertex to the mouse's current
				// position, offset by the original distance
				// between the vertices and adjusted to lay within the
				// bounds of the grid.
				marker.setTranslateX(
						relativeXCords.get(i) + mousePosX + xAdjust);
				marker.setTranslateY(
						relativeYCords.get(i) + mousePosY + yAdjust);
			}

		}
	}

	/**
	 * The function called whenever the mouse button is released in Edit Mode.
	 * 
	 * @param event
	 *            The mouse event which prompted the invocation of this
	 *            function.
	 */
	private void handleEditModeMouseUp(MouseEvent event) {
		// Move the selected vertices at the end of a drag, ignoring
		// other clicks
		if (dragStarted) {
			dragStarted = false;

			// Get the mouse position
			mouseOldX = mousePosX;
			mouseOldY = mousePosY;
			mousePosX = event.getX();
			mousePosY = event.getY();

			// Update the vertices
			for (int i = 0; i < selectedVertices.size(); i++) {

				// Get the vertex
				VertexController vertex = (VertexController) selectedVertices
						.get(i);

				// Set the vertex's location to the current location of the
				// temporary marker in the scene
				vertex.updateLocation(
						vertexMarkers.get(i).getTranslateX() / SCALE,
						vertexMarkers.get(i).getTranslateY() / SCALE,
						vertexMarkers.get(i).getTranslateZ() / SCALE);

				// Remove the markers from the scene
				for (Sphere marker : vertexMarkers) {
					((FXAttachment) attachmentManager.getAttachments().get(1))
							.getFxNode().getChildren().remove(marker);
				}

			}

			// Empty the lists of markers and coordinates
			vertexMarkers.clear();
			relativeXCords.clear();
			relativeYCords.clear();
		}
	}

	/**
	 * The function called whenever the user moves the mouse.
	 * 
	 * @param event
	 *            The event that prompted this function's invocation.
	 */
	private void handleMouseMoved(MouseEvent event) {
		DecimalFormat format = new DecimalFormat("#.##");
		cursorPosition
				.setText("Cursor position (x,y): ("
						+ format.format(event.getPickResult()
								.getIntersectedPoint().getX() / 3)
						+ ","
						+ event.getPickResult().getIntersectedPoint().getY() / 3
						+ ")");
		cursorPosition.setTranslateX(scene.getWidth() / -20);
		cursorPosition.setTranslateZ(-5);
	}

	/**
	 * <p>
	 * Creates scene elements that aren't meant to be manipulated by the user
	 * (markers, camera, etc.)
	 * </p>
	 */

	@Override
	protected void setupSceneInternals(Group parent) {

		// Create the ambient light
		AmbientLight ambientLight = new AmbientLight(Color.rgb(100, 100, 100));

		// Create the point lights
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

		// Create the background grid
		gizmo = new AxisGridGizmo(SCALE);

		// Add everything to the scene
		parent.getChildren().addAll(gizmo, light1, light2, light3, light4,
				ambientLight);

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

		// If the user is switching to edit mode, register the edit handlers
		// with the scene
		if (edit) {
			scene.setOnMouseClicked(editHandler);
			scene.setOnMouseDragged(editDragHandler);
			scene.setOnMouseReleased(editMouseUpHandler);

		} else {

			// If the user is switching to add mode, register the add handler
			// and remove the edit drag handler, as add mode has no
			// functionality for mouse drag
			scene.setOnMouseClicked(addHandler);
			scene.setOnMouseDragged(null);
			scene.setOnMouseReleased(editMouseUpHandler);
		}

		// Don't maintain selections between different modes
		clearSelection();
	}

	/**
	 * Sets the viewer's HUD, which displays the camera center and mouse cursor
	 * positions, to be visible or invisible.
	 * 
	 * @param visible
	 *            Whether or not the viewer should display the HUD.
	 */
	public void setHUDVisible(boolean visible) {
		cursorPosition.setVisible(visible);
	}

	/**
	 * Checks whether the viewer's HUD is visible.
	 * 
	 * @return True if the HUD is being displayed, false if it is not.
	 */
	public boolean isHUDVisible() {
		return cursorPosition.isVisible();
	}

	/**
	 * Sets the editor's axis display's visibility.
	 * 
	 * @param visible
	 *            Whether or not the editor should display its axis.
	 */
	public void setAxisVisible(boolean visible) {
		gizmo.toggleAxis(visible);
	}

	/**
	 * Checks whether the viewer has visible axis.
	 * 
	 * @return True if the axis are displayed in the viewer, false otherwise
	 */
	public boolean getAxisVisible() {
		return gizmo.axesVisible();
	}

	/**
	 * Remove the selected property from all selected parts and reset the
	 * selection lists
	 */
	private void clearSelection() {

		// Remove the temporary vertices from the scene
		for (AbstractController vertex : selectedVertices) {
			tempRoot.removeEntity(vertex);
			vertex.setProperty("Selected", "False");
			vertex.setProperty("Constructing", "False");
			vertex.refresh();
		}

		// Remove the temporary edges from the scene and set them to
		// be unselected
		for (AbstractController edge : tempEdges) {
			tempRoot.removeEntity(edge);
			edge.setProperty("Selected", "False");
			edge.setProperty("Constructing", "False");
			edge.refresh();
		}

		// Empty the lists
		selectedVertices.clear();
		tempEdges.clear();
	}

	/**
	 * Gets the Edge part between two given vertices, creating a new edge and
	 * adding it to the temporary root node if none exists.
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public EdgeController getEdge(VertexController start,
			VertexController end) {

		// If the start point shares and edge with the end point, return it
		for (AbstractController edge : start.getEntitiesByCategory("Edges")) {
			if (edge.getEntitiesByCategory("Vertices").contains(end)) {

				edge.setProperty("Constructing", "True");
				return (EdgeController) edge;
			}
		}

		// If there is not already an edge, create a new one
		FaceEdgeMesh tempComponent = new FaceEdgeMesh(start, end);
		tempComponent.setProperty("Constructing", "True");
		EdgeController tempEdge = (EdgeController) factory
				.createController(tempComponent);

		// Set the edge's name and ID
		tempEdge.setProperty("Name", "Edge");
		tempEdge.setProperty("Id", String.valueOf(nextEdgeID));
		nextEdgeID++;

		// Set the mouse to ignore edges. Only Vertices and
		// empty space may be selected.
		((Group) tempEdge.getRepresentation()).setMouseTransparent(true);

		// Add it to the temporary root
		tempRoot.addEntity(tempEdge);

		return tempEdge;

	}

	/**
	 * Set the parts of the mesh such that the given objects are the only
	 * selected parts
	 * 
	 * @param selection
	 *            The new set of parts which will be selected
	 */
	public void setInternalSelection(Object[] selection) {
		clearSelection();

		// Add each object to the correct internal list
		for (Object target : selection) {

			AbstractController part = ((MeshSelection) target).selectedMeshPart;

			// For vertices, add them to the vertex list if they are not already
			// present
			if (part instanceof VertexController) {
				if (!selectedVertices.contains(part)) {
					selectedVertices.add(part);
				}
			}

			else if (part instanceof EdgeController) {

				// Add an edge to the list of temporary edges if it is not
				// already present
				if (!tempEdges.contains(part)) {
					tempEdges.add(part);
				}

				// Then handle its vertices
				for (AbstractController vertex : part
						.getEntitiesByCategory("Vertices")) {
					if (!selectedVertices.contains(vertex)) {
						selectedVertices.add(vertex);
					}
				}
			}

			else if (part instanceof FaceController) {

				// Add each of the face's edges
				for (AbstractController edge : (part)
						.getEntitiesByCategory("Edges")) {
					if (!tempEdges.contains(edge)) {
						tempEdges.add(edge);
					}
				}

				// Add each of the face's vertices
				for (AbstractController vertex : (part)
						.getEntitiesByCategory("Vertices")) {
					if (!selectedVertices.contains(vertex)) {
						selectedVertices.add(vertex);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.canvas.FXViewer#updateCamera(org.
	 * eclipse.ice.viz.service.javafx.scene.base.ICamera)
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

		cameraController = new TopDownCameraController(fxCamera, scene,
				fxCanvas);

		scene.setCamera(fxCamera);

		defaultCamera = fxCamera;

		cursorPosition = new Text();
		cursorPosition.setTranslateZ(-5);
		internalRoot.getChildren().add(cursorPosition);
		BorderPane pane = new BorderPane();
		internalRoot.getChildren().add(pane);
	}

}