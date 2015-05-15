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
package org.eclipse.ice.datastructures.form.mesh;

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.ICEObject.ICEObject;
import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * <p>
 * Composite container for Polygons along with any additional information
 * required to interpret the mesh data.<br>
 * <br>
 * The MeshComponent breaks a mesh down into vertices and edges. As polygons are
 * added, the new edges and vertices are incorporated into the mesh, and
 * IUpdateableListeners registered with the MeshComponent are notified that it
 * has changed.<br>
 * <br>
 * All polygons are expected to have a unique ID among all other polygons. This
 * also holds for vertices and edges.
 * </p>
 * 
 * @author Jordan H. Deyton
 */
@XmlRootElement(name = "MeshComponent")
@XmlAccessorType(XmlAccessType.FIELD)
public class MeshComponent extends ICEObject implements Component, IMeshPart {

	/**
	 * <p>
	 * The map of Polygons contained in this MeshComponent, keyed on their IDs.
	 * </p>
	 * 
	 */
	private TreeMap<Integer, Polygon> polygons;
	/**
	 * <p>
	 * All vertices managed by this MeshComponent. Keyed on their IDs.
	 * </p>
	 * 
	 */
	@XmlTransient
	private TreeMap<Integer, Vertex> vertices;
	/**
	 * <p>
	 * All edges managed by this MeshComponent. Keyed on their IDs.
	 * </p>
	 * 
	 */
	@XmlTransient
	private TreeMap<Integer, Edge> edges;

	/**
	 * <p>
	 * For each vertex, this contains an ordered set of the IDs of each polygon
	 * containing the vertex.
	 * </p>
	 * 
	 */
	@XmlTransient
	private TreeMap<Integer, TreeSet<Integer>> vertexPolygons;
	/**
	 * <p>
	 * For each edge, this contains an ordered set of the IDs of each polygon
	 * containing the edge.
	 * </p>
	 * 
	 */
	@XmlTransient
	private TreeMap<Integer, TreeSet<Integer>> edgePolygons;
	/**
	 * <p>
	 * For each vertex, this contains an ordered set of the IDs of each edge
	 * connected to the vertex.
	 * </p>
	 * 
	 */
	@XmlTransient
	private TreeMap<Integer, TreeSet<Integer>> vertexEdges;

	/**
	 * <p>
	 * A flag signifying that the MeshComponent is currently undergoing a copy
	 * operation.
	 * </p>
	 * 
	 */
	@XmlTransient
	private final AtomicBoolean copying;

	/**
	 * <p>
	 * The default constructor for a MeshComponent. Initializes the list of
	 * polygons and any associated bookkeeping structures.
	 * </p>
	 * 
	 */
	public MeshComponent() {
		super();

		// Initialize the trees of shapes, vertices, and edges.
		polygons = new TreeMap<Integer, Polygon>();
		vertices = new TreeMap<Integer, Vertex>();
		edges = new TreeMap<Integer, Edge>();

		// Initialize the trees of polygon IDs per vertex/edge.
		vertexPolygons = new TreeMap<Integer, TreeSet<Integer>>();
		edgePolygons = new TreeMap<Integer, TreeSet<Integer>>();
		vertexEdges = new TreeMap<Integer, TreeSet<Integer>>();

		// Initialize the flag to signify when the MeshComponent is copying data
		// from another MeshComponent.
		copying = new AtomicBoolean(false);

		return;
	}

	/**
	 * <p>
	 * Adds a polygon to the MeshComponent. The polygon is expected to have a
	 * unique polygon ID. If the polygon can be added, a notification is sent to
	 * listeners. If the polygon uses equivalent vertices or edges with
	 * different references, then a new polygon is created with references to
	 * the vertices and edges already known by this MeshComponent.
	 * </p>
	 * 
	 * @param polygon
	 *            <p>
	 *            The new polygon to add to the existing list.
	 *            </p>
	 */
	public void addPolygon(Polygon polygon) {

		// We can't add null values, and we can't add a polygon with an ID
		// that's already associated with another polygon.
		if (polygon != null && !polygons.containsKey(polygon.getId())) {

			/*
			 * We can trust that the Polygon is, on its own, correctly linking
			 * its vertices/edges. However, it could be using duplicates of
			 * pre-existing vertices/edges. If this is the case, then the
			 * polygon's references to them must be linked to the vertices/edges
			 * maintained by this MeshComponent.
			 */

			boolean validPolygon = true;
			boolean stalePolygon = false;

			// Get the polygon's vertices and edges.
			ArrayList<Vertex> polygonVertices = polygon.getVertices();
			ArrayList<Edge> polygonEdges = polygon.getEdges();

			// Get the number of edges in the new polygon.
			int size = polygonVertices.size();

			for (int i = 0; i < size && validPolygon; i++) {
				Vertex vertex = polygonVertices.get(i);
				Vertex oldVertex = vertices.get(vertex.getId());
				if (vertex == oldVertex || oldVertex == null) {
					// The vertex reference is valid or new. We don't need to do
					// anything special. This is expected to be true most of the
					// time.
				} else if (vertex.equals(oldVertex)) {
					// This means the polygon has a reference to a different
					// vertex, but the vertex is otherwise equivalent.
					stalePolygon = true;
				} else {
					// The ID used by this vertex is already used for a
					// different vertex. The polygon cannot be added.
					validPolygon = false;
				}

				Edge edge = polygonEdges.get(i);
				Edge oldEdge = edges.get(edge.getId());
				if (edge == oldEdge || oldEdge == null) {
					// The edge reference is valid or new. We don't need to do
					// anything special. This is expected to be true most of the
					// time.
				} else if (edge.equals(oldEdge)) {
					// This means the polygon has a reference to a different
					// edge, but the edge is otherwise equivalent.
					stalePolygon = true;
				} else {
					// The ID used by this edge is already used for a
					// different edge. The polygon cannot be added.
					validPolygon = false;
				}
			}

			// If the polygon is valid, then we can add it.
			if (validPolygon) {
				// Get the unique ID of the polygon.
				int id = polygon.getId();

				// We may need to re-create the polygon with valid references.
				// The code in this condition creates a new polygon with valid
				// references.
				if (stalePolygon) {
					// We need lists of vertices and edges to create a polygon.
					ArrayList<Vertex> newVertices = new ArrayList<Vertex>();
					ArrayList<Edge> newEdges = new ArrayList<Edge>();

					// Loop over the old polygon's vertices and edges. If a
					// vertex/edge is not in the MeshComponent's Maps, use the
					// old polygon's copy.
					for (int i = 0; i < size; i++) {
						// Add either the existing vertex or the new one.
						Vertex newVertex = polygonVertices.get(i);
						Vertex vertex = vertices.get(newVertex.getId());
						newVertices.add(vertex != null ? vertex : newVertex);

						// Add either the existing edge or the new one.
						Edge newEdge = polygonEdges.get(i);
						Edge edge = edges.get(newEdge.getId());
						newEdges.add(edge != null ? edge : newEdge);
					}

					// Re-initialize the polygon with the proper vertices/edges.
					polygon = new Polygon(newEdges, newVertices);
					polygon.setId(id);

					// These two lists have stale references now. Update them to
					// use the same vertices/edges as the new polygon.
					polygonVertices = newVertices;
					polygonEdges = newEdges;
				}

				// Put everything into their corresponding trees.
				// Add the polygon to the polygon tree.
				polygons.put(id, polygon);
				// Add any new vertices to the vertex tree.
				// Add any new edges to the edge tree.
				for (int i = 0; i < size; i++) {
					// Get the next vertex and its ID.
					Vertex vertex = polygonVertices.get(i);
					int j = vertex.getId();
					// If the vertex exists, add the polygon's ID to its set of
					// associated polygon IDs.
					if (vertices.containsKey(j)) {
						// Update the polygons that use this vertex.
						vertexPolygons.get(j).add(id);
						// Update the edges connected to this vertex.
						vertexEdges.get(j)
								.add(polygonEdges.get((i + size - 1) % size)
										.getId());
						vertexEdges.get(j).add(polygonEdges.get(i).getId());
					}
					// If the vertex does not exist, we need to create a new set
					// of associated polygon IDs.
					else {
						vertices.put(j, vertex);
						// Update the polygons that use this vertex.
						TreeSet<Integer> polygonIds = new TreeSet<Integer>();
						polygonIds.add(id);
						vertexPolygons.put(j, polygonIds);
						// Update the edges connected to this vertex.
						TreeSet<Integer> edgeIds = new TreeSet<Integer>();
						edgeIds.add(polygonEdges.get((i + size - 1) % size)
								.getId());
						edgeIds.add(polygonEdges.get(i).getId());
						vertexEdges.put(j, edgeIds);
					}

					// Get the next edge and its ID.
					Edge edge = polygonEdges.get(i);
					j = edge.getId();
					// If the edge exists, add the polygon's ID to its set of
					// associated polygon IDs.
					if (edges.containsKey(j)) {
						edgePolygons.get(j).add(id);
					}
					// If the edge does not exist, we need to create a new set
					// of associated polygon IDs.
					else {
						edges.put(j, edge);
						TreeSet<Integer> polygonIds = new TreeSet<Integer>();
						polygonIds.add(id);
						edgePolygons.put(j, polygonIds);
					}
				}

				// Notify listeners when a new polygon was added. If we are
				// copying, then we do not want to send a new notification.
				if (!copying.get()) {
					notifyListeners();
				}
			}
		}

		return;
	}

	/**
	 * <p>
	 * Removes a polygon from the MeshComponent. This will also remove any
	 * vertices and edges used only by this polygon. If a polygon was removed, a
	 * notification is sent to listeners.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            The ID of the polygon to remove from the existing list.
	 *            </p>
	 */
	public void removePolygon(int id) {

		// Try to remove the polygon matching the ID.
		Polygon polygon = polygons.remove(id);
		// If the value returned from remove is not null, we need to update our
		// bookkeeping for the removed polygon.
		if (polygon != null) {

			// Remove any vertices and edges that are only associated with the
			// removed polygon.

			// Get the polygon's vertices and edges.
			ArrayList<Vertex> polygonVertices = polygon.getVertices();
			ArrayList<Edge> polygonEdges = polygon.getEdges();
			for (int i = 0; i < polygonVertices.size(); i++) {
				// Get the next vertex ID.
				int vertexId = polygonVertices.get(i).getId();
				// Remove the polygon ID from the vertex's set of polygon IDs.
				Set<Integer> polygonIds = vertexPolygons.get(vertexId);
				polygonIds.remove(id);
				// If there are no more polygons associated with the vertex,
				// remove the vertex.
				if (polygonIds.isEmpty()) {
					vertices.remove(vertexId);
					vertexPolygons.remove(vertexId);
				}

				// Get the next edge ID.
				int edgeId = polygonEdges.get(i).getId();
				// Remove the polygon ID from the edge's set of polygon IDs.
				polygonIds = edgePolygons.get(edgeId);
				polygonIds.remove(id);
				// If there are no more polygons associated with the edge,
				// remove the edge.
				if (polygonIds.isEmpty()) {
					// Update the list of edges associated with each vertex. If
					// the list is empty, then we can remove the list.
					Edge edge = edges.get(edgeId);
					for (int vertex : edge.getVertexIds()) {
						Set<Integer> edgeIds = vertexEdges.get(vertex);
						edgeIds.remove(edgeId);
						if (edgeIds.isEmpty()) {
							vertexEdges.remove(vertex);
						}
					}
					// Remove the edge and its list of associated polygons.
					edges.remove(edgeId);
					edgePolygons.remove(edgeId);
				}
			}

			// Notify listeners of the changes.
			notifyListeners();
		}

		return;
	}

	/**
	 * <p>
	 * Removes a list polygons from the MeshComponent. This will also remove any
	 * vertices and edges used by these polygons. If a polygon was removed, a
	 * notification is sent to listeners.
	 * </p>
	 * 
	 * @param ids
	 *            <p>
	 *            An ArrayList containing the IDs of the polygons to remove from
	 *            the MeshComponent.
	 *            </p>
	 */
	public void removePolygons(ArrayList<Integer> ids) {
		// TODO Add to tests.

		// Make sure the list is not null.
		if (ids != null) {
			boolean changed = false;

			// Loop over the IDs and try to remove a polygon for each one.
			for (Integer id : ids) {
				// Try to remove the polygon matching the ID.
				Polygon polygon = polygons.remove(id);
				// If the value returned from remove is not null, we need to
				// update our
				// bookkeeping for the removed polygon.
				if (polygon != null) {

					// Remove any vertices and edges that are only associated
					// with the
					// removed polygon.

					// Get the polygon's vertices and edges.
					ArrayList<Vertex> polygonVertices = polygon.getVertices();
					ArrayList<Edge> polygonEdges = polygon.getEdges();
					for (int i = 0; i < polygonVertices.size(); i++) {
						// Get the next vertex ID.
						int vertexId = polygonVertices.get(i).getId();
						// Remove the polygon ID from the vertex's set of
						// polygon IDs.
						Set<Integer> polygonIds = vertexPolygons.get(vertexId);
						polygonIds.remove(id);
						// If there are no more polygons associated with the
						// vertex,
						// remove the vertex.
						if (polygonIds.isEmpty()) {
							vertices.remove(vertexId);
							vertexPolygons.remove(vertexId);
						}

						// Get the next edge ID.
						int edgeId = polygonEdges.get(i).getId();
						// Remove the polygon ID from the edge's set of polygon
						// IDs.
						polygonIds = edgePolygons.get(edgeId);
						polygonIds.remove(id);
						// If there are no more polygons associated with the
						// edge,
						// remove the edge.
						if (polygonIds.isEmpty()) {
							// Update the list of edges associated with each
							// vertex. If
							// the list is empty, then we can remove the list.
							Edge edge = edges.get(edgeId);
							for (int vertex : edge.getVertexIds()) {
								Set<Integer> edgeIds = vertexEdges.get(vertex);
								edgeIds.remove(edgeId);
								if (edgeIds.isEmpty()) {
									vertexEdges.remove(vertex);
								}
							}
							// Remove the edge and its list of associated
							// polygons.
							edges.remove(edgeId);
							edgePolygons.remove(edgeId);
						}
					}
					// If the polygon is not null, then we must have removed it.
					changed = true;
				}
			}

			// If the set of polygons was modified, notify listeners of the
			// change.
			if (changed) {
				notifyListeners();
			}
		}
		return;
	}

	/**
	 * <p>
	 * Gets a list of all polygons stored in the MeshComponent ordered by their
	 * IDs.
	 * </p>
	 * 
	 * @return <p>
	 *         A list of polygons contained in this MeshComponent.
	 *         </p>
	 */
	public ArrayList<Polygon> getPolygons() {
		return new ArrayList<Polygon>(polygons.values());
	}

	/**
	 * <p>
	 * Gets a Polygon instance corresponding to an ID.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            The ID of the polygon.
	 *            </p>
	 * @return <p>
	 *         The polygon referred to by the ID, or null if there is no polygon
	 *         with the ID.
	 *         </p>
	 */
	public Polygon getPolygon(int id) {
		return polygons.get(id);
	}

	/**
	 * <p>
	 * Returns the next available ID for polygons.
	 * </p>
	 * 
	 * @return <p>
	 *         The greatest polygon ID (or zero) plus one.
	 *         </p>
	 */
	public int getNextPolygonId() {
		return (!polygons.isEmpty() ? polygons.lastKey() + 1 : 1);
	}

	/**
	 * <p>
	 * Sets the list of all polygons stored in the MeshComponent.
	 * </p>
	 * 
	 * @param polygons
	 *            <p>
	 *            The list of polygons to replace the existing list of polygons
	 *            in the MeshComponent.
	 *            </p>
	 */
	public void setPolygons(ArrayList<Polygon> polygons) {

		// Add all shapes if the list is not null.
		if (polygons != null) {
			// FIXME - This doesn't reset anything!
			// We need to add some test code to check this method more
			// thoroughly (call it with a list of polygons where some are new
			// and some old polygons are gone).

			for (Polygon polygon : polygons) {
				addPolygon(polygon);
			}
		}

		// Notifying listeners is handled by addPolygon().

		return;
	}

	/**
	 * <p>
	 * Gets a list of all vertices associated with this MeshComponent.
	 * </p>
	 * 
	 * @return <p>
	 *         All vertices managed by this MeshComponent.
	 *         </p>
	 */
	public ArrayList<Vertex> getVertices() {
		return new ArrayList<Vertex>(vertices.values());
	}

	/**
	 * <p>
	 * Gets a Vertex instance corresponding to an ID.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            The ID of the vertex.
	 *            </p>
	 * @return <p>
	 *         The vertex referred to by the ID, or null if the ID is invalid.
	 *         </p>
	 */
	public Vertex getVertex(int id) {
		return vertices.get(id);
	}

	/**
	 * <p>
	 * Returns the next available ID for vertices.
	 * </p>
	 * 
	 * @return <p>
	 *         The greatest vertex ID (or zero) plus one.
	 *         </p>
	 */
	public int getNextVertexId() {
		return (!vertices.isEmpty() ? vertices.lastKey() + 1 : 1);
	}

	/**
	 * <p>
	 * Gets a list of all edges associated with this MeshComponent.
	 * </p>
	 * 
	 * @return <p>
	 *         All edges managed by this MeshComponent.
	 *         </p>
	 */
	public ArrayList<Edge> getEdges() {
		return new ArrayList<Edge>(edges.values());
	}

	/**
	 * <p>
	 * Gets an Edge instance corresponding to an ID.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            The ID of the edge.
	 *            </p>
	 * @return <p>
	 *         The edge referred to by the ID, or null if the ID is invalid.
	 *         </p>
	 */
	public Edge getEdge(int id) {
		return edges.get(id);
	}

	/**
	 * <p>
	 * Returns the next available ID for edges.
	 * </p>
	 * 
	 * @return <p>
	 *         The greatest edge ID (or zero) plus one.
	 *         </p>
	 */
	public int getNextEdgeId() {
		return (!edges.isEmpty() ? edges.lastKey() + 1 : 1);
	}

	/**
	 * <p>
	 * Returns a list of Edges attached to the Vertex with the specified ID.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            The ID of the vertex.
	 *            </p>
	 * @return <p>
	 *         An ArrayList of Edges that are attached to the vertex with the
	 *         specified ID. If there are no such edges, e.g., if the vertex ID
	 *         is invalid, the list will be empty.
	 *         </p>
	 */
	public ArrayList<Edge> getEdgesFromVertex(int id) {
		// TODO Add to tests.
		ArrayList<Edge> edgeList = new ArrayList<Edge>();

		if (vertices.containsKey(id)) {
			for (int edgeId : vertexEdges.get(id)) {
				edgeList.add(edges.get(edgeId));
			}
		}

		return edgeList;
	}

	/**
	 * <p>
	 * Returns a list of Polygons containing the Vertex with the specified ID.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            The ID of the vertex.
	 *            </p>
	 * @return <p>
	 *         An ArrayList of Polygons that contain the vertex with the
	 *         specified ID. If there are no such polygons, e.g., if the vertex
	 *         ID is invalid, the list will be empty.
	 *         </p>
	 */
	public ArrayList<Polygon> getPolygonsFromVertex(int id) {
		// TODO Add to tests.
		ArrayList<Polygon> polygonList = new ArrayList<Polygon>();

		if (vertices.containsKey(id)) {
			for (int polygonId : vertexPolygons.get(id)) {
				polygonList.add(polygons.get(polygonId));
			}
		}

		return polygonList;
	}

	/**
	 * <p>
	 * Returns a list of Polygons containing the Edge with the specified ID.
	 * </p>
	 * 
	 * @param id
	 *            <p>
	 *            The ID of the edge.
	 *            </p>
	 * @return <p>
	 *         An ArrayList of Polygons that contain the edge with the specified
	 *         ID. If there are no such polygons, e.g., if the edge ID is
	 *         invalid, the list will be empty.
	 *         </p>
	 */
	public ArrayList<Polygon> getPolygonsFromEdge(int id) {
		// TODO Add to tests.
		ArrayList<Polygon> polygonList = new ArrayList<Polygon>();

		if (edges.containsKey(id)) {
			for (int polygonId : edgePolygons.get(id)) {
				polygonList.add(polygons.get(polygonId));
			}
		}

		return polygonList;
	}

	/**
	 * <p>
	 * Returns an Edge that connects two specified vertices if one exists.
	 * </p>
	 * 
	 * @param firstId
	 *            <p>
	 *            The ID of the first vertex.
	 *            </p>
	 * @param second
	 *            <p>
	 *            The ID of the second vertex.
	 *            </p>
	 * 
	 * @return <p>
	 *         An Edge instance that connects the first and second vertices, or
	 *         null if no such edge exists.
	 *         </p>
	 */
	public Edge getEdgeFromVertices(int firstId, int secondId) {
		// TODO Add to tests.
		Edge edge = null;

		// Check the parameters. We can only look for edges that connect two
		// distinct existing vertices.
		if (firstId != secondId && vertices.containsKey(firstId)
				&& vertices.containsKey(secondId)) {

			// Get the Edge from the first edge ID match between the two sets of
			// edge IDs.
			Set<Integer> secondEdgeIds = vertexEdges.get(secondId);
			for (int edgeId : vertexEdges.get(firstId)) {
				if (secondEdgeIds.contains(edgeId)) {
					edge = edges.get(edgeId);
					break;
				}
			}
		}

		return edge;
	}

	/**
	 * <p>
	 * Returns a list containing all Polygons in the MeshComponent whose
	 * vertices are a subset of the supplied list of vertices.
	 * </p>
	 * 
	 * @param vertices
	 *            <p>
	 *            A collection of vertices.
	 *            </p>
	 * @return <p>
	 *         An ArrayList of all Polygons in the MeshComponent that are
	 *         composed of some subset of the specified vertices.
	 *         </p>
	 */
	public ArrayList<Polygon> getPolygonsFromVertices(ArrayList<Vertex> vertices) {
		// TODO Add to tests.
		ArrayList<Polygon> polygonList = new ArrayList<Polygon>();

		if (vertices != null) {
			// We need a set of used vertex IDs to make sure we don't count a
			// vertex twice.
			HashSet<Integer> usedVertices = new HashSet<Integer>();
			// We also need a map for each matched polygon telling how many of
			// its vertices were in the provided list of vertices.
			HashMap<Integer, Integer> polygonCount = new HashMap<Integer, Integer>();

			// Update the counts for the number of vertex IDs that are
			// associated with each polygon.
			for (Vertex vertex : vertices) {
				if (vertex != null && !usedVertices.contains(vertex.getId())) {
					// Update the counts for all polygons associated with this
					// vertex if it is valid.
					Set<Integer> polygonIds = vertexPolygons
							.get(vertex.getId());
					if (polygonIds != null) {
						for (int id : polygonIds) {
							Integer count = polygonCount.get(id);
							if (count == null) {
								count = 0;
							}
							polygonCount.put(id, count + 1);
						}
					}
					// We do not want to count this vertex again.
					usedVertices.add(vertex.getId());
				}
			}

			// For each reference polygon ID, check the number of its vertices
			// that were included in the provided list. If all its vertices were
			// included, then it can be added to the returned list of Polygons.
			for (Entry<Integer, Integer> e : polygonCount.entrySet()) {
				Polygon polygon = polygons.get(e.getKey());
				if (polygon.getVertices().size() == e.getValue()) {
					polygonList.add(polygon);
				}
			}
		}

		return polygonList;
	}

	/**
	 * <p>
	 * This operation returns the hash value of the MeshComponent.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashcode of the ICEObject.
	 *         </p>
	 */
	public int hashCode() {

		// Hash based on super's hashCode.
		int hash = super.hashCode();

		// Add local hashes.
		hash += 31 * polygons.hashCode();

		return hash;
	}

	/**
	 * <p>
	 * This operation is used to check equality between this MeshComponent and
	 * another MeshComponent. It returns true if the MeshComponents are equal
	 * and false if they are not.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The other ICEObject that should be compared with this one.
	 *            </p>
	 * @return <p>
	 *         True if the ICEObjects are equal, false otherwise.
	 *         </p>
	 */
	public boolean equals(Object otherObject) {

		// By default, the objects are not equivalent.
		boolean equals = false;

		// Check the reference.
		if (this == otherObject) {
			equals = true;
		}
		// Check the information stored in the other object.
		else if (otherObject != null && otherObject instanceof MeshComponent) {

			// We can now cast the other object.
			MeshComponent component = (MeshComponent) otherObject;

			// Compare the values between the two objects.
			equals = (super.equals(otherObject) && polygons
					.equals(component.polygons));
			// The polygons are the only defining feature of the MeshComponent
			// (aside from the super properties). If the polygon lists are
			// equivalent, we can safely expect the other bookkeeping structures
			// are identical.
		}

		return equals;
	}

	/**
	 * <p>
	 * This operation copies the contents of a MeshComponent into the current
	 * object using a deep copy.
	 * </p>
	 * 
	 * @param component
	 *            <p>
	 *            The ICEObject from which the values should be copied
	 *            </p>
	 */
	public void copy(MeshComponent component) {

		// Check the parameters.
		if (component != null) {

			// Set the copying flag to true. We do not want addPolygon to send
			// notifications yet.
			copying.set(true);

			// Copy the super's data.
			super.copy(component);

			// This is the primary list of shapes.
			polygons.clear();

			// These should be updated automatically by adding the shapes.
			vertices.clear();
			edges.clear();
			polygons.clear();
			vertexPolygons.clear();
			edgePolygons.clear();
			vertexEdges.clear();

			// Add all the shapes from the other component.
			for (Polygon polygon : component.polygons.values()) {
				addPolygon((Polygon) polygon.clone());
			}

			// Notify listeners of the change.
			notifyListeners();

			// Disable the copy flag.
			copying.set(false);
		}

		return;
	}

	/**
	 * <p>
	 * This operation returns a clone of the MeshComponent using a deep copy.
	 * </p>
	 * 
	 * @return <p>
	 *         The new clone
	 *         </p>
	 */
	public Object clone() {

		// Initialize a new object.
		MeshComponent object = new MeshComponent();

		// Copy the contents from this one.
		object.copy(this);

		// Return the newly instantiated object.
		return object;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Component#accept(IComponentVisitor visitor)
	 */
	public void accept(IComponentVisitor visitor) {

		// Call the visitor's visit(MeshComponent) method.
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}

	/**
	 * <p>
	 * This method calls the {@link IMeshPartVisitor}'s visit method.
	 * </p>
	 * 
	 * @param visitor
	 *            <p>
	 *            The {@link IMeshPartVisitor} that is visiting this
	 *            {@link IMeshPart}.
	 *            </p>
	 */
	public void acceptMeshVisitor(IMeshPartVisitor visitor) {
		if (visitor != null) {
			visitor.visit(this);
		}
		return;
	}

}