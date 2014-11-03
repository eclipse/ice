/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.mesh;

import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.form.mesh.Hex;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.mesh.Polygon;
import org.eclipse.ice.datastructures.form.mesh.Quad;
import org.eclipse.ice.datastructures.form.mesh.Vertex;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class AddMode extends MeshApplicationMode implements ICameraListener {

	// ---- MeshApplication Variables ---- //
	/**
	 * The host {@link MeshApplication} that is using this mode.
	 */
	private MeshApplication app;
	/**
	 * The {@link MeshComponent} displayed in the {@link MeshApplication}.
	 */
	private MeshComponent mesh;
	/**
	 * The floor of the grid in the {@link MeshApplication}.
	 */
	private Geometry grid;
	/**
	 * The parent {@link Node} containing spatials in the scene for all
	 * {@link Vertex vertices} in the {@link #mesh}.
	 */
	private Node vertexSpatials;
	/**
	 * The update queue for the MeshApplication.
	 */
	private ConcurrentLinkedQueue<AbstractMeshController> updateQueue;
	/**
	 * The parent {@link Node} containing all temporary spatials displayed in
	 * the scene.
	 */
	private Node tempSpatials;
	// ----------------------------------- //

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

	// ---- Additional ActionListeners. ---- //
	/**
	 * This listener accepts the set of temporary vertices and edges to create a
	 * new polygon, which is then added to the mesh. It should be triggered
	 * either by an additional click or the Enter key.
	 */
	private final ActionListener acceptListener;
	/**
	 * This listener resets the set of temporary vertices and edges, effectively
	 * cancelling the current element being added to the mesh. It should be
	 * bound to the Escape, Backspace, and Delete keys.
	 */
	private final ActionListener cancelListener;

	// ------------------------------------- //

	/**
	 * The default constructor. Adds any additional {@link ActionListener}s to
	 * the map of {@link MeshApplicationMode#listeners listeners}.
	 */
	public AddMode() {
		super();

		// Initialize any final variables here.
		vertices = new ArrayList<Vertex>();
		vertexControllers = new ArrayList<VertexController>();
		edges = new ArrayList<Edge>();
		edgeControllers = new ArrayList<EdgeController>();

		// Add any additional listeners here.

		// Create a listener to accept the vertices to create the new polygon.
		acceptListener = new ActionListener() {
			public void onAction(String name, boolean isPressed, float tpf) {
				if (!isPressed && canAcceptPolygon()) {
					acceptPolygon();
				}
			}
		};
		listeners.put(Key.Enter, acceptListener);

		// Create a listener to cancel the vertices used to create a new
		// polygon. It should be triggered by Backspace, Delete, and Escape.
		cancelListener = new ActionListener() {
			public void onAction(String name, boolean isPressed, float tpf) {
				if (!isPressed) {
					resetPolygon();
				}
			}
		};
		listeners.put(Key.Backspace, cancelListener);
		listeners.put(Key.Delete, cancelListener);
		listeners.put(Key.Escape, cancelListener);

		// Set the initial polygon type to be a quad.
		polygonSize = 4;

		return;
	}

	/**
	 * Overrides the parent method to provide more {@link ActionListener}s.
	 */
	@Override
	public List<InputListener> getInputListeners() {
		List<InputListener> listeners = super.getInputListeners();

		// Add any additional listeners here.
		listeners.add(acceptListener);
		listeners.add(cancelListener);

		return listeners;
	}

	/**
	 * Implements the parent method to load any temporary data required for this
	 * MeshApplicationMode.
	 */
	public void load(MeshApplication application) {

		// Get fields of interest from the MeshApplication.
		app = application;
		mesh = app.getMesh();
		grid = app.getGrid();
		vertexSpatials = app.getVertexSpatials();
		tempSpatials = app.getTemporarySpatialNode();
		updateQueue = app.getUpdateQueue();

		// Register as a camera listener so that the temporary vertex
		// controllers can be updated when the zoom changes.
		app.getChaseCamera().addCameraListener(this);

		return;
	}

	/**
	 * Implements the parent method to clear any temporary data managed by this
	 * MeshApplicationMode.
	 */
	public void clear() {

		// Detach the temporary spatials node from the scene.
		app.getRootNode().attachChild(tempSpatials);

		// Unregister from the CustomChaseCamera.
		app.getChaseCamera().removeCameraListener(this);

		// Clear any temporary objects.
		resetPolygon();

		// Clear references to the MeshApplication fields.
		app = null;
		mesh = null;
		grid = null;
		vertexSpatials = null;
		tempSpatials = null;
		updateQueue = null;

		return;
	}

	/**
	 * Implements the left-click behavior for this MeshApplicationMode.
	 */
	protected void leftClick(boolean isPressed) {
		if (!isPressed) {
			// Proceed depending on how many vertices have been created. If not
			// enough vertices have been created/selected, then add another one.
			// If enough vertices have been created to form a polygon of the
			// proper size, then let the user click again to accept the polygon.

			if (!canAcceptPolygon()) {
				int sides = vertices.size();

				// Add a vertex based on the cursor's position at the click.
				Vertex vertex = addVertexFromRay(app.getCursorRayFromClick());

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

	/**
	 * Implements the right-click behavior for this MeshApplicationMode.
	 */
	protected void rightClick(boolean isPressed) {
		// Do nothing yet.
	}

	/**
	 * Implements the parent method to provide the MeshApplicationMode name.
	 */
	public String getName() {
		return "Add Elements";
	}

	/**
	 * Implements the parent method to provide the MeshApplicationMode
	 * description.
	 */
	public String getDescription() {
		return "Adds elements or polygons by placing vertices on the grid.";
	}

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
		for (VertexController controller : vertexControllers) {
			controller.dispose();
		}
		vertexControllers.clear();

		// Remove all edges.
		edges.clear();
		// Dispose of the EdgeControllers and their EdgeViews.
		for (EdgeController controller : edgeControllers) {
			controller.dispose();
			edgeControllers.clear();
		}
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

		if ((results = app.getCollision(vertexSpatials, ray)).size() > 0) {
			// Get the ID from the name of the nearest collision (a VertexView).
			int id = Integer.parseInt(results.getClosestCollision()
					.getGeometry().getName());
			vertex = mesh.getVertex(id);
		} else if ((results = app.getCollision(grid, ray)).size() > 0) {
			// Get the collision point and its nearest point to the grid's
			// bounds.
			Vector3f point = results.getClosestCollision().getContactPoint();
			Vector3f gridPoint = app.getClosestGridPoint(point);

			// Create a new Vertex from the point on the grid.
			vertex = new Vertex(gridPoint.x, gridPoint.y, gridPoint.z);
			vertex.setId(mesh.getNextVertexId() + vertices.size());
		}

		if (vertex != null) {
			// Create a controller for the vertex and add it to the scene.
			controller = new VertexController(vertex, updateQueue,
					app.createBasicMaterial(null));
			controller.setParentNode(tempSpatials);
			controller.setState(StateType.Temporary);
			controller.setSize(app.getVertexSize());
			controller.setScale(app.getScale());

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
		Edge edge = mesh.getEdgeFromVertices(start.getId(), end.getId());
		EdgeController controller;

		// Create the edge and set its ID.
		if (edge == null) {
			edge = new Edge(start, end);
			edge.setId(mesh.getNextEdgeId() + newEdges++);
		}

		// Create a controller for the edge and add it to the scene.
		controller = new EdgeController(edge, updateQueue,
				app.createBasicMaterial(null));
		controller.setParentNode(tempSpatials);
		controller.setState(StateType.Temporary);
		controller.setSize(app.getEdgeSize());
		controller.setScale(app.getScale());

		// Add them to the lists of temporary edges/controllers.
		edges.add(edge);
		edgeControllers.add(controller);

		return edge;
	}

	// ---- Implements ICameraListener ---- //
	/**
	 * This method updates all controllers for temporary spatials to account for
	 * any changes in zoom.
	 */
	public void zoomChanged(float distance) {
		// Update the vertex controllers.
		float vertexSize = app.getVertexSize();
		for (VertexController c : vertexControllers) {
			c.setSize(vertexSize);
		}

		return;
	}
	// ------------------------------------ //

}
