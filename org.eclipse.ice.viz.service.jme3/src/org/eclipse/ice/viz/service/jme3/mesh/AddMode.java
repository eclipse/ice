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
 *    Dasha Gorin (UT-Battelle, LLC.) - code and documentation cleanup
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.ice.viz.service.jme3.widgets.InputControl;
import org.eclipse.ice.viz.service.mesh.datastructures.Edge;
import org.eclipse.ice.viz.service.mesh.datastructures.Hex;
import org.eclipse.ice.viz.service.mesh.datastructures.Polygon;
import org.eclipse.ice.viz.service.mesh.datastructures.Quad;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 * This {@link MeshAppStateMode} allows the user to add additional polygons to
 * the {@link MeshAppState}'s {@link MeshComponent} by clicking on the grid.
 * Vertices can be re-used, and once enough vertices have been placed, the user
 * can confirm or cancel the new polygon from keyboard input.
 * 
 * @author Jordan Deyton
 * 
 */
public class AddMode extends MeshAppStateMode {

	// ---- MeshAppState variables ---- //
	/**
	 * The floor of the grid in the <code>MeshAppState</code>.
	 */
	private Geometry grid;
	/**
	 * The parent <code>Node</code> containing spatials in the scene for all
	 * {@link Vertex}es in the {@link #mesh}.
	 */
	private Node vertexSpatials;
	/**
	 * The update queue for the <code>MeshAppState</code>.
	 */
	private ConcurrentLinkedQueue<AbstractMeshController> updateQueue;
	// -------------------------------- //

	// ---- Custom Controls ---- //
	/**
	 * An <code>InputControl</code> that contains the mapping name, listener,
	 * and trigger for accepting a new polygon for the mesh.
	 */
	private InputControl acceptAction;
	/**
	 * An <code>InputControl</code> that contains the mapping name, listener,
	 * and trigger for canceling an unaccepted polygon.
	 */
	private InputControl cancelAction;
	// ------------------------- //

	// ---- Temporary spatials ---- //
	/**
	 * The parent <code>Node</code> containing all temporary spatials displayed
	 * in the scene.
	 */
	private final Node tempSpatials;

	/**
	 * The current number of sides supported for polygons.
	 */
	private int polygonSize;

	/**
	 * A list of temporary Vertex instances. These are used to construct
	 * polygons for the {@link #mesh}.
	 */
	private final ArrayList<Vertex> vertices;
	/**
	 * A list of controllers for the temporary vertices. These should be deleted
	 * if the polygon is cancelled or if the polygon is added to the
	 * {@link mesh}.
	 */
	private final List<VertexController> vertexControllers;
	/**
	 * A list of temporary Edge instances. These are used to construct polygons
	 * for the {@link #mesh}.
	 */
	private final ArrayList<Edge> edges;
	/**
	 * A counter for the number of new edges for each polygon.
	 */
	private int newEdges;
	/**
	 * A list of controllers for the temporary edges. These should be deleted if
	 * the polygon is cancelled or if the polygon is added to the {@link mesh}.
	 */
	private final List<EdgeController> edgeControllers;

	// ---------------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param appState
	 *            The <code>MeshAppState</code> that this
	 *            <code>MeshAppStateMode</code> supports.
	 */
	public AddMode(MeshAppState appState) {
		super(appState);

		// Initialize the root Node for temporary spatials.
		tempSpatials = new Node("AddMode-temp");

		// Initialize any final variables here.
		vertices = new ArrayList<Vertex>();
		vertexControllers = new ArrayList<VertexController>();
		edges = new ArrayList<Edge>();
		edgeControllers = new ArrayList<EdgeController>();

		// Set the initial polygon type to be a quad.
		polygonSize = 4;

		return;
	}

	// ---- Extends MeshAppStateMode ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.mesh.MeshAppStateMode#getName()
	 */
	@Override
	public String getName() {
		return "Add Elements";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.mesh.MeshAppStateMode#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Adds elements or polygons by placing vertices on the grid.";
	}

	// ---------------------------------- //

	// ---- Initialization methods ---- //
	@Override
	protected void initScene() {

		// Set up all of the variables used to update the scene.
		grid = appState.getGrid();
		vertexSpatials = appState.getVertexSpatials();
		updateQueue = appState.getUpdateQueue();

		return;
	}

	/**
	 * Registers custom controls in addition to the default
	 * {@link MeshAppStateMode} controls.
	 */
	@Override
	protected void initControls() {
		super.initControls();

		String name;
		ActionListener action;

		// Create a listener to accept the vertices to create the new polygon.
		name = "accept";
		action = new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				if (!isPressed && canAcceptPolygon()) {
					acceptPolygon();
				}
			}
		};
		acceptAction = new InputControl(action, name);
		acceptAction.addTriggers(name, new KeyTrigger(KeyInput.KEY_RETURN));

		// Create a listener to cancel the vertices used to create a new
		// polygon. It should be triggered by Backspace, Delete, and Escape.
		name = "cancel";
		action = new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				if (!isPressed) {
					resetPolygon();
				}
			}
		};
		cancelAction = new InputControl(action, name);
		cancelAction.addTriggers(name, new KeyTrigger(KeyInput.KEY_BACK),
				new KeyTrigger(KeyInput.KEY_DELETE), new KeyTrigger(
						KeyInput.KEY_ESCAPE));

		return;
	}

	// -------------------------------- //

	// ---- Enable/Disable ---- //
	@Override
	public void enableAppState() {
		super.enableAppState();

		// Attach the temporary spatial Node to the MeshAppState's scene.
		appState.getRootNode().attachChild(tempSpatials);

		return;
	}

	/**
	 * Registers additional controls for adding mesh elements.
	 */
	@Override
	public void registerControls() {
		super.registerControls();

		// Get the InputManager and register all custom controls with it.
		InputManager input = getApplication().getInputManager();
		acceptAction.registerWithInput(input);
		cancelAction.registerWithInput(input);

		return;
	}

	@Override
	public void disableAppState() {

		// Detach the temporary spatial Node from the MeshAppState's scene.
		appState.getRootNode().detachChild(tempSpatials);

		// Clear any temporary objects.
		resetPolygon();

		super.disableAppState();
	}

	/**
	 * Unregisters the additional controls for adding mesh elements.
	 */
	@Override
	public void unregisterControls() {
		// Unregister all controls from the InputManager.
		acceptAction.unregisterFromInput();
		cancelAction.unregisterFromInput();

		super.unregisterControls();
	}

	// ------------------------ //

	// ---- Cleanup methods ---- //
	@Override
	protected void clearScene() {

		// Clear variables used to update the scene.
		grid = null;
		vertexSpatials = null;
		updateQueue = null;

		return;
	}

	/**
	 * Unregisters custom controls in addition to the default
	 * {@link MeshAppStateMode} controls.
	 */
	@Override
	protected void clearControls() {

		// Unset the controls.
		acceptAction = null;
		cancelAction = null;

		super.clearControls();
	}

	// ------------------------- //

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.mesh.MeshAppStateMode#leftClick(boolean ,
	 * float)
	 */
	@Override
	public void leftClick(boolean isPressed, float tpf) {

		if (!isPressed) {
			// Proceed depending on how many vertices have been created. If not
			// enough vertices have been created/selected, then add another one.
			// If enough vertices have been created to form a polygon of the
			// proper size, then let the user click again to accept the polygon.

			if (!canAcceptPolygon()) {
				int sides = vertices.size();

				// Add a vertex based on the cursor's position at the click.
				Vertex vertex = addVertexFromRay(appState
						.getCursorRayFromClick());

				// If possible, add an edge between the last vertex and the
				// current one.
				if (vertex != null && sides > 0) {
					addEdgeFromVertices(vertices.get(sides - 1), vertex);

					// If this is the last vertex, add an edge between it and
					// the first vertex.
					if (sides == polygonSize - 1) {
						addEdgeFromVertices(vertex, vertices.get(0));
					}
				}
			} else {
				acceptPolygon();
			}
		}

		return;
	}

	// ---- Getters and Setters ---- //
	/**
	 * Gets the current size of polygons created by AddMode.
	 * 
	 * @return An integer value greater than 2.
	 */
	public int getPolygonSize() {
		return polygonSize;
	}

	/**
	 * Sets the current size of polygons created by AddMode.
	 * 
	 * @param size
	 *            An integer value greater than 2. If it is 2 or less, this
	 *            method does nothing.
	 */
	public void setPolygonSize(int size) {
		if (size > 2) {
			polygonSize = size;
		}
		return;
	}

	// ----------------------------- //

	// ---- Private utility methods ---- //
	/**
	 * Determines whether or not there are enough temporary vertices and edges
	 * to construct a polygon of the current size.
	 * 
	 * @return True if there are enough vertices to construct a polygon with
	 *         {@link #polygonSize} sides, false otherwise.
	 */
	private boolean canAcceptPolygon() {
		return (vertices.size() >= polygonSize);
	}

	/**
	 * Creates a new polygon out of the temporary edges and vertices and adds it
	 * to the mesh. This also clears the collections of temporary vertices and
	 * edges.
	 */
	private void acceptPolygon() {
		// Create a polygon and add it to the mesh.
		Polygon polygon;

		// Construct a Quad, Hex, or Polygon depending on the size.
		if (polygonSize == 4) {
			polygon = new Quad(edges, vertices);
		} else if (polygonSize == 6) {
			polygon = new Hex(edges, vertices);
		} else {
			polygon = new Polygon(edges, vertices);
		}

		// Add the polygon to the mesh.
		VizMeshComponent mesh = appState.getMesh();
		polygon.setId(mesh.getNextPolygonId());
		mesh.addPolygon(polygon);

		// Remove all of the temporary spatials and vertices.
		resetPolygon();

		return;
	}

	/**
	 * Clears all vertices, edges, and their controllers from the temporary
	 * lists. This also disposes of the controllers, meaning their views will be
	 * removed from the jME3 scene.
	 */
	private void resetPolygon() {
		// Remove all vertices.
		vertices.clear();
		// Dispose of the VertexControllers and their VertexViews.
		for (VertexController controller : vertexControllers)
			controller.dispose();
		vertexControllers.clear();

		// Remove all edges.
		edges.clear();
		// Dispose of the EdgeControllers and their EdgeViews.
		for (EdgeController controller : edgeControllers)
			controller.dispose();
		edgeControllers.clear();

		// Reset the new edges counter.
		newEdges = 0;

		return;
	}

	/**
	 * Gets or creates a Vertex from colliding the ray with the existing
	 * vertices and the grid.
	 * 
	 * @return An existing Vertex if the ray collides with one, otherwise a new
	 *         Vertex at the nearest grid location.
	 */
	private Vertex addVertexFromRay(Ray ray) {

		// We need to create a Vertex and a controller if possible.
		Vertex vertex = null;
		VertexController controller;

		CollisionResults results;

		if ((results = getCollision(vertexSpatials, ray)).size() > 0) {
			// Get the ID from the name of the nearest collision (a VertexView).
			int id = Integer.parseInt(results.getClosestCollision()
					.getGeometry().getName());
			vertex = appState.getMesh().getVertex(id);
		} else if ((results = getCollision(grid, ray)).size() > 0) {
			// Get the collision point and its nearest point to the grid's
			// bounds.
			Vector3f point = results.getClosestCollision().getContactPoint();
			Vector3f gridPoint = appState.getClosestGridPoint(point);

			// Create a new Vertex from the point on the grid.
			vertex = new Vertex(gridPoint.x, gridPoint.y, gridPoint.z);
			vertex.setId(appState.getMesh().getNextVertexId() + vertices.size());
		}

		if (vertex != null) {
			// Create a controller for the vertex and add it to the scene.
			controller = new VertexController(vertex, updateQueue,
					createBasicMaterial(null));
			controller.setParentNode(tempSpatials);
			controller.setState(StateType.Temporary);
			controller.setSize(appState.getVertexSize());
			controller.setScale(appState.getScale());

			// Add them to the lists of temporary vertices/controllers.
			vertices.add(vertex);
			vertexControllers.add(controller);
		}

		return vertex;
	}

	/**
	 * Gets or creates an Edge from the supplied start and end vertices.
	 * 
	 * @param start
	 *            The start Vertex.
	 * @param end
	 *            The end Vertex.
	 * @return An existing Edge if the MeshComponent already has an Edge between
	 *         those vertices, otherwise a new Edge.
	 */
	private Edge addEdgeFromVertices(Vertex start, Vertex end) {

		// We need to create an Edge and a controller if possible.
		VizMeshComponent mesh = appState.getMesh();
		Edge edge = mesh.getEdgeFromVertices(start.getId(), end.getId());
		EdgeController controller;

		// Create the edge and set its ID.
		if (edge == null) {
			edge = new Edge(start, end);
			edge.setId(mesh.getNextEdgeId() + newEdges++);
		}

		// Create a controller for the edge and add it to the scene.
		controller = new EdgeController(edge, updateQueue,
				createBasicMaterial(null));
		controller.setParentNode(tempSpatials);
		controller.setState(StateType.Temporary);
		controller.setSize(appState.getEdgeSize());
		controller.setScale(appState.getScale());

		// Add them to the lists of temporary edges/controllers.
		edges.add(edge);
		edgeControllers.add(controller);

		return edge;
	}

	// --------------------------------- //

	// ---- Implements ICameraListener ---- //
	/**
	 * This method updates all controllers for temporary spatials to account for
	 * any changes in zoom.
	 */
	@Override
	public void zoomChanged(float distance) {
		// Update the vertex controllers.
		float vertexSize = appState.getVertexSize();
		for (VertexController c : vertexControllers)
			c.setSize(vertexSize);

		return;
	}
	// ------------------------------------ //
}
