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
package org.eclipse.ice.viz.service.jme3.mesh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.ice.viz.service.mesh.datastructures.Edge;
import org.eclipse.ice.viz.service.mesh.datastructures.Polygon;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;

/**
 * This class manages a selection of polygons, edges, and vertices for a
 * MeshComponent.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class MeshSelectionManager {

	/**
	 * The MeshComponent for which the MeshApplicationSelectionHandler is
	 * managing a selection of polygons, edges, and vertices.
	 */
	private VizMeshComponent mesh;

	/**
	 * A list of listeners that listen for updates to the MeshApplication's
	 * current selection.
	 */
	private final List<IMeshSelectionListener> listeners;

	// ---- The current selection. ---- //
	/**
	 * A map of currently-selected vertices in the MeshApplication, keyed on
	 * their IDs.
	 */
	private final Map<Integer, VertexController> selectedVertices;
	/**
	 * A map of currently-selected edges in the MeshApplication, keyed on their
	 * IDs.
	 */
	private final Map<Integer, Edge> selectedEdges;
	/**
	 * A map of currently-selected polygons in the MeshApplication, keyed on
	 * their IDs.
	 */
	private final Map<Integer, Polygon> selectedPolygons;
	// -------------------------------- //

	// ---- Lock variables for multi-thread support. ---- //
	/**
	 * A lock for multi-threaded access to the selection data structures.
	 */
	private final ReadWriteLock selectionLock;
	/**
	 * The lock for reading from {@link #selectionLock}.readLock().
	 */
	private final Lock readLock;
	/**
	 * The lock for writing from {@link #selectionLock}.writeLock().
	 */
	private final Lock writeLock;

	// -------------------------------------------------- //

	/**
	 * The default constructor.
	 */
	public MeshSelectionManager() {
		// Initialize the current selection collections.
		listeners = new ArrayList<IMeshSelectionListener>();
		selectedVertices = new TreeMap<Integer, VertexController>();
		selectedEdges = new TreeMap<Integer, Edge>();
		selectedPolygons = new TreeMap<Integer, Polygon>();

		// Initialize the read/write lock for the selection data structures. The
		// order of the input selection matters, e.g., a deselect before a
		// select results in a net selection, and a select before a deselect
		// results in a net deselection. We can solve this by using the R/W-lock
		// with "fair" mode.
		selectionLock = new ReentrantReadWriteLock(true);
		readLock = selectionLock.readLock();
		writeLock = selectionLock.writeLock();

		// Set the default initial mesh to an empty MeshComponent.
		mesh = new VizMeshComponent();

		return;
	}

	/**
	 * Sets the MeshComponent whose selection is managed by this handler. This
	 * resets the current selection if a new mesh is selected.
	 * 
	 * @param mesh
	 *            The new MeshComponent.
	 */
	public void setMesh(VizMeshComponent mesh) {
		// Only set the new mesh if it's valid and different.
		if (mesh != null && mesh != this.mesh) {
			this.mesh = mesh;

			// Reset the current selection.
			clearSelection();
		}
		return;
	}

	// ---- Listener operations. ---- //
	/**
	 * Adds an IMeshApplicationListener to listen for changes to the
	 * MeshApplication's current selection of vertices and edges.
	 * 
	 * @param listener
	 *            The new IMeshApplicationListener.
	 */
	public void addMeshApplicationListener(IMeshSelectionListener listener) {
		// Only add non-null and non-duplicate listeners.
		if (listener != null && !listeners.contains(listener)) {
			listeners.add(listener);
		}
		return;
	}

	/**
	 * Removes an IMeshApplicationListener from this MeshApplication.
	 * 
	 * @param listener
	 *            The listener to remove. Must not be null.
	 */
	public void removeMeshApplicationListener(IMeshSelectionListener listener) {
		if (listener != null) {
			listeners.remove(listener);
		}
		return;
	}

	/**
	 * Notifies all listeners that the selection of vertices, edges, and
	 * polygons has changed.
	 */
	private void notifyMeshApplicationListeners() {
		// Only process the update if there are listeners
		if (!listeners.isEmpty()) {
			// Create a thread on which to notify the listeners.
			Thread notifierThread = new Thread() {
				@Override
				public void run() {
					// Loop over all listeners and update them
					for (int i = 0; i < listeners.size(); i++) {
						listeners.get(i).selectionChanged();
					}
					return;
				}
			};

			// Launch the thread and do the notifications
			notifierThread.start();
		}
		return;
	}

	// ------------------------------ //

	// ---- Selection Getters. ---- //
	/**
	 * Gets the currently-selected vertices in the MeshApplication.
	 * 
	 * @return An ArrayList of the currently-selected vertices.
	 */
	public List<VertexController> getSelectedVertices() {
		List<VertexController> vertices;

		// Acquire the read lock and create a new list from the values in the
		// collection of selected vertices.
		readLock.lock();
		try {
			vertices = new ArrayList<VertexController>(selectedVertices.values());
		} finally {
			readLock.unlock();
		}

		return vertices;
	}

	/**
	 * Gets the IDs of the currently-selected vertices in the MeshApplication.
	 * 
	 * @return An ArrayList of the currently-selected vertices' IDs.
	 */
	public List<Integer> getSelectedVertexIds() {
		List<Integer> ids;

		// Acquire the read lock and create a new list from the keys in the
		// collection of selected vertices.
		readLock.lock();
		try {
			ids = new ArrayList<Integer>(selectedVertices.keySet());
		} finally {
			readLock.unlock();
		}

		return ids;
	}

	/**
	 * Gets the currently-selected edges in the MeshApplication.
	 * 
	 * @return An ArrayList of the currently-selected edges.
	 */
	public List<Edge> getSelectedEdges() {
		List<Edge> edges;

		// Acquire the read lock and create a new list from the values in the
		// collection of selected edges.
		readLock.lock();
		try {
			edges = new ArrayList<Edge>(selectedEdges.values());
		} finally {
			readLock.unlock();
		}

		return edges;
	}

	/**
	 * Gets the IDs of the currently-selected edges in the MeshApplication.
	 * 
	 * @return An ArrayList of the currently-selected edges' IDs.
	 */
	public List<Integer> getSelectedEdgeIds() {
		List<Integer> ids;

		// Acquire the read lock and create a new list from the keys in the
		// collection of selected edges.
		readLock.lock();
		try {
			ids = new ArrayList<Integer>(selectedEdges.keySet());
		} finally {
			readLock.unlock();
		}

		return ids;
	}

	/**
	 * Gets the currently-selected polygons in the MeshApplication.
	 * 
	 * @return An ArrayList of the currently-selected polygons.
	 */
	public List<Polygon> getSelectedPolygons() {
		List<Polygon> polygons;

		// Acquire the read lock and create a new list from the values in the
		// collection of selected polygons.
		readLock.lock();
		try {
			polygons = new ArrayList<Polygon>(selectedPolygons.values());
		} finally {
			readLock.unlock();
		}

		return polygons;
	}

	/**
	 * Gets the IDs of the currently-selected polygons in the MeshApplication.
	 * 
	 * @return An ArrayList of the currently-selected polygons' IDs.
	 */
	public List<Integer> getSelectedPolygonIds() {
		List<Integer> ids;

		// Acquire the read lock and create a new list from the keys in the
		// collection of selected polygons.
		readLock.lock();
		try {
			ids = new ArrayList<Integer>(selectedPolygons.keySet());
		} finally {
			readLock.unlock();
		}

		return ids;
	}

	// ---------------------------- //

	/**
	 * Clears all of the currently selected vertices, edges, and polygons.
	 */
	public void clearSelection() {
		// Acquire the write lock and clear all of the selected vertices, edges,
		// and polygons.
		writeLock.lock();
		try {
			// De-select all selected vertices.
			selectedVertices.clear();

			// De-select all selected edges.
			selectedEdges.clear();

			// De-select all selected polygons.
			selectedPolygons.clear();
		} finally {
			writeLock.unlock();
		}

		// Notify listeners that the selection has changed.
		notifyMeshApplicationListeners();

		return;
	}

	/**
	 * Removes all selected polygons from the MeshComponent. This also updates
	 * the selection, since some of the selected vertices and edges may no
	 * longer exist.
	 */
	public void deleteSelection() {

		ArrayList<Integer> removedIds;
		ArrayList<VertexController> vertices;

		// Acquire the read lock to fetch the data necessary to determine all
		// polygons that are either selected or whose vertices are all selected.
		readLock.lock();
		try {

			// Create a list of removed polygon IDs from the set of selected
			// polygons.
			removedIds = new ArrayList<Integer>(selectedPolygons.keySet());

			// Get the set of selected vertices to determine any other polygons
			// that may have all of their vertices selected.
			vertices = new ArrayList<VertexController>(selectedVertices.values());
		} finally {
			readLock.unlock();
		}

		// For all polygons whose vertices are a subset of the
		// currently-selected vertices, add their IDs to the set of polygons
		// that should be removed.
		ArrayList<Polygon> polygons = mesh.getPolygonsFromVertices(vertices);
		for (Polygon polygon : polygons) {
			removedIds.add(polygon.getId());
		}
		// Remove the selected polygons from the mesh.
		mesh.removePolygons(removedIds);

		// Acquire the write lock so that we can properly update the selection.
		writeLock.lock();
		try {
			// Remove the IDs of all polygons that were removed.
			for (int id : removedIds) {
				selectedPolygons.remove(id);
			}
			// Remove all old vertices from the set of selected vertices.
			List<Integer> ids = new ArrayList<Integer>(
					selectedVertices.keySet());
			for (int id : ids) {
				if (mesh.getVertex(id) == null) {
					selectedVertices.remove(id);
				}
			}
			// Remove all old edges from the set of selected edges.
			ids = new ArrayList<Integer>(selectedEdges.keySet());
			for (int id : ids) {
				if (mesh.getEdge(id) == null) {
					selectedEdges.remove(id);
				}
			}
		} finally {
			writeLock.unlock();
		}

		// Notify listeners that the selection has changed.
		notifyMeshApplicationListeners();

		return;
	}

	// ---- Vertex Selection Setters. ---- //
	/**
	 * Adds the vertex with the specified ID to the selection, if possible.
	 * 
	 * @param id
	 *            The ID of the newly-selected vertex.
	 * @return Returns true if the operation changes the selection, false
	 *         otherwise.
	 */
	private boolean addVertex(int id) {
		// A flag denoting that the selection has changed.
		boolean changed = false;

		// Acquire the write lock and try to select the vertex with the
		// specified ID. Only select it if it is valid and has not already been
		// selected.
		writeLock.lock();
		try {
			if (!selectedVertices.containsKey(id)) {
				VertexController vertex = mesh.getVertex(id);
				if (vertex != null) {
					selectedVertices.put(id, vertex);
					changed = true;
				}
			}
		} finally {
			writeLock.unlock();
		}

		return changed;
	}

	/**
	 * Removes the vertex with the specified ID from the selection, if possible.
	 * 
	 * @param id
	 *            The ID of the vertex to remove.
	 * @return Returns true if the operation changes the selection, false
	 *         otherwise.
	 */
	private boolean removeVertex(int id) {
		// A flag denoting that the selection has changed.
		boolean changed = false;

		// Acquire the write lock and try to remove the ID (a key) from the
		// selected vertices. If one was removed, then the selection changed.
		writeLock.lock();
		try {
			if (selectedVertices.remove(id) != null) {

				// De-select all attached edges from the selection.
				for (Edge edge : mesh.getEdgesFromVertex(id)) {
					selectedEdges.remove(edge.getId());
				}

				// De-select all attached polygons from the selection.
				for (Polygon polygon : mesh.getPolygonsFromVertex(id)) {
					selectedPolygons.remove(polygon.getId());
				}

				changed = true;
			}

		} finally {
			writeLock.unlock();
		}

		return changed;
	}

	/**
	 * Selects a vertex with the specified ID. If the selection changes, any
	 * {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param id
	 *            The ID of the newly-selected vertex.
	 */
	public void selectVertex(int id) {
		// Try to add the vertex with the specified ID to the selection. If the
		// selection changes, we need to notify listeners.
		if (addVertex(id)) {
			notifyMeshApplicationListeners();
		}
		return;
	}

	/**
	 * Selects multiple vertices with the specified list of IDs. If the
	 * selection changes, any {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param ids
	 *            A collection of IDs for vertices to select.
	 */
	public void selectVertices(List<Integer> ids) {
		if (ids != null) {
			// Loop over the supplied IDs and try to select each one.
			int changed = 0;
			for (Integer id : ids) {
				if (id != null && addVertex(id)) {
					changed++;
				}
			}
			// Notify listeners if the selection changed somehow.
			if (changed > 0) {
				notifyMeshApplicationListeners();
			}
		}
		return;
	}

	/**
	 * De-selects a vertex with the specified ID. If the selection changes, any
	 * {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param id
	 *            The ID of the vertex to de-select.
	 */
	public void deselectVertex(int id) {
		// Try to remove the vertex with the specified ID from the selection. If
		// the selection changes, we need to notify listeners.
		if (removeVertex(id)) {
			notifyMeshApplicationListeners();
		}
		return;
	}

	/**
	 * De-selects multiple vertices with the specified list of IDs. If the
	 * selection changes, any {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param ids
	 *            A collection of IDs for vertices to de-select.
	 */
	public void deselectVertices(List<Integer> ids) {
		if (ids != null) {
			// Loop over the supplied IDs and try to deselect each one.
			int changed = 0;
			for (Integer id : ids) {
				if (id != null && removeVertex(id)) {
					changed++;
				}
			}
			// Notify listeners if the selection changed somehow.
			if (changed > 0) {
				notifyMeshApplicationListeners();
			}
		}
		return;
	}

	// ---------------------------- //

	// ---- Edge Selection Setters. ---- //
	/**
	 * Adds the edge with the specified ID to the selection, if possible.
	 * 
	 * @param id
	 *            The ID of the newly-selected edge.
	 * @return Returns true if the operation changes the selection, false
	 *         otherwise.
	 */
	private boolean addEdge(int id) {
		// A flag denoting that the selection has changed.
		boolean changed = false;

		// Acquire the write lock and try to select the edge with the specified
		// ID. Only select it if it is valid and has not already been selected.
		writeLock.lock();
		try {
			if (!selectedEdges.containsKey(id)) {
				Edge edge = mesh.getEdge(id);
				if (edge != null) {
					selectedEdges.put(id, edge);

					// Select the edge's vertices as well.
					for (int vertexId : edge.getVertexIds()) {
						addVertex(vertexId);
					}

					changed = true;
				}
			}
		} finally {
			writeLock.unlock();
		}

		return changed;
	}

	/**
	 * Removes the edge with the specified ID from the selection, if possible.
	 * 
	 * @param id
	 *            The ID of the edge to remove.
	 * @return Returns true if the operation changes the selection, false
	 *         otherwise.
	 */
	private boolean removeEdge(int id) {
		// A flag denoting that the selection has changed.
		boolean changed = false;

		// Acquire the write lock and try to remove the ID (a key) from the
		// selected edges. If one was removed, then the selection changed.
		writeLock.lock();
		try {
			if (selectedEdges.remove(id) != null) {

				// De-select all attached polygons from the selection.
				for (Polygon polygon : mesh.getPolygonsFromEdge(id)) {
					selectedPolygons.remove(polygon.getId());
				}

				changed = true;
			}
		} finally {
			writeLock.unlock();
		}

		return changed;
	}

	/**
	 * Selects an edge with the specified ID. If the selection changes, any
	 * {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param id
	 *            The ID of the newly-selected edge.
	 */
	public void selectEdge(int id) {
		// Try to add the edge with the specified ID to the selection. If the
		// selection changes, we need to notify listeners.
		if (addEdge(id)) {
			notifyMeshApplicationListeners();
		}
		return;
	}

	/**
	 * Selects multiple edges with the specified list of IDs. If the selection
	 * changes, any {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param ids
	 *            A collection of IDs for edges to select.
	 */
	public void selectEdges(List<Integer> ids) {
		if (ids != null) {
			// Loop over the supplied IDs and try to select each one.
			int changed = 0;
			for (Integer id : ids) {
				if (id != null && addEdge(id)) {
					changed++;
				}
			}
			// Notify listeners if the selection changed somehow.
			if (changed > 0) {
				notifyMeshApplicationListeners();
			}
		}
		return;
	}

	/**
	 * De-selects an edge with the specified ID. If the selection changes, any
	 * {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param id
	 *            The ID of the edge to de-select.
	 */
	public void deselectEdge(int id) {
		// Try to remove the edge with the specified ID from the selection. If
		// the selection changes, we need to notify listeners.
		if (removeEdge(id)) {
			notifyMeshApplicationListeners();
		}
		return;
	}

	/**
	 * De-selects multiple edges with the specified list of IDs. If the
	 * selection changes, any {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param ids
	 *            A collection of IDs for edges to de-select.
	 */
	public void deselectEdges(List<Integer> ids) {
		if (ids != null) {
			// Loop over the supplied IDs and try to deselect each one.
			int changed = 0;
			for (Integer id : ids) {
				if (id != null && removeEdge(id)) {
					changed++;
				}
			}
			// Notify listeners if the selection changed somehow.
			if (changed > 0) {
				notifyMeshApplicationListeners();
			}
		}
		return;
	}

	// -------------------------- //

	// ---- Polygon Selection Setters. ---- //
	/**
	 * Adds the polygon with the specified ID to the selection, if possible.
	 * 
	 * @param id
	 *            The ID of the newly-selected polygon.
	 * @return Returns true if the operation changes the selection, false
	 *         otherwise.
	 */
	private boolean addPolygon(int id) {
		// A flag denoting that the selection has changed.
		boolean changed = false;

		// Acquire the write lock and try to select the polygon with the
		// specified ID. Only select it if it is valid and has not already been
		// selected.
		writeLock.lock();
		try {
			if (!selectedPolygons.containsKey(id)) {
				Polygon polygon = mesh.getPolygon(id);
				if (polygon != null) {
					selectedPolygons.put(id, polygon);

					// Select the polygon's edges as well. This should
					// automatically select its vertices.
					for (Edge edge : polygon.getEdges()) {
						addEdge(edge.getId());
					}

					changed = true;
				}
			}
		} finally {
			writeLock.unlock();
		}

		return changed;
	}

	/**
	 * Removes the polygon with the specified ID from the selection, if
	 * possible.
	 * 
	 * @param id
	 *            The ID of the polygon to remove.
	 * @return Returns true if the operation changes the selection, false
	 *         otherwise.
	 */
	private boolean removePolygon(int id) {
		// A flag denoting that the selection has changed.
		boolean changed = false;

		// Acquire the write lock and try to remove the ID (a key) from the
		// selected polygons. If one was removed, then the selection changed.
		writeLock.lock();
		try {
			changed = (selectedPolygons.remove(id) != null);
		} finally {
			writeLock.unlock();
		}

		return changed;
	}

	/**
	 * Selects a polygon with the specified ID. If the selection changes, any
	 * {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param id
	 *            The ID of the newly-selected polygon.
	 */
	public void selectPolygon(int id) {
		// Try to add the polygon with the specified ID to the selection. If the
		// selection changes, we need to notify listeners.
		if (addPolygon(id)) {
			notifyMeshApplicationListeners();
		}
		return;
	}

	/**
	 * Selects multiple polygons with the specified list of IDs. If the
	 * selection changes, any {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param ids
	 *            A collection of IDs for polygons to select.
	 */
	public void selectPolygons(List<Integer> ids) {
		if (ids != null) {
			// Loop over the supplied IDs and try to select each one.
			int changed = 0;
			for (Integer id : ids) {
				if (id != null && addPolygon(id)) {
					changed++;
				}
			}
			// Notify listeners if the selection changed somehow.
			if (changed > 0) {
				notifyMeshApplicationListeners();
			}
		}
		return;
	}

	/**
	 * De-selects a polygon with the specified ID. If the selection changes, any
	 * {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param id
	 *            The ID of the polygon to de-select.
	 */
	public void deselectPolygon(int id) {
		// Try to remove the polygon with the specified ID from the selection.
		// If the selection changes, we need to notify listeners.
		if (removePolygon(id)) {
			notifyMeshApplicationListeners();
		}
		return;
	}

	/**
	 * De-selects multiple polygons with the specified list of IDs. If the
	 * selection changes, any {@link IMeshSelectionListener}s will be notified.
	 * 
	 * @param ids
	 *            A collection of IDs for polygons to de-select.
	 */
	public void deselectPolygons(List<Integer> ids) {
		if (ids != null) {
			// Loop over the supplied IDs and try to deselect each one.
			int changed = 0;
			for (Integer id : ids) {
				if (id != null && removePolygon(id)) {
					changed++;
				}
			}
			// Notify listeners if the selection changed somehow.
			if (changed > 0) {
				notifyMeshApplicationListeners();
			}
		}
		return;
	}
	// ----------------------------- //
}
