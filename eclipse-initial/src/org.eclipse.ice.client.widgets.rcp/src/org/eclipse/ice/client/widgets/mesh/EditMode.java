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

import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.mesh.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.jme3.collision.CollisionResults;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.InputListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class EditMode extends MeshApplicationMode implements
		IMeshSelectionListener, ICameraListener {

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

	/* ---- Additional ActionListeners. ---- */
	/**
	 * This listener sets the flag {@link #shiftPressed} if the left or right
	 * shift keys are pressed.
	 */
	private final ActionListener shiftListener;
	/**
	 * This listener sets the flag {@link #controlPressed} if the left or right
	 * control keys are pressed.
	 */
	private final ActionListener controlListener;
	/**
	 * This listener listens for presses of the delete key.
	 */
	private final ActionListener deleteListener;
	/**
	 * This listener sets the flags {@link #drag} and {@link #mouseMoved} if the
	 * cursor moves.
	 */
	private final AnalogListener mouseMoveListener;
	/* ------------------------------------- */

	/**
	 * The threshold for a click to be eligible to be considered a drag (in
	 * milliseconds).
	 */
	private static final long clickThreshold = 250;
	/**
	 * True if the sequence of mouseDown and mouseUp occurs fast enough to be
	 * considered a click. (currently, it's a click regardless of movement if
	 * the click is less than 0.25 seconds).
	 */
	private final AtomicBoolean click = new AtomicBoolean(true);
	/**
	 * A timer used to set {@link #click} to false after 0.25 seconds.
	 */
	private Timer clickTimer;
	/**
	 * True if the mouse has been moved.
	 */
	private final AtomicBoolean drag = new AtomicBoolean(false);
	/**
	 * True if the mouse button is pressed, false if the mouse button is
	 * depressed.
	 */
	private final AtomicBoolean mouseDown = new AtomicBoolean(false);
	/**
	 * True if the shift button is pressed, false otherwise.
	 */
	private final AtomicBoolean shiftPressed = new AtomicBoolean(false);
	/**
	 * True if the control (CTRL) button is pressed, false otherwise.
	 */
	private final AtomicBoolean controlPressed = new AtomicBoolean(false);

	/**
	 * A flag signifying that the temporary spatials should be displayed. This
	 * is typically done simply by attaching or detaching {@link #tempSpatials}
	 * from the MeshApplication's rootNode.
	 */
	private final AtomicBoolean showTemporarySpatials = new AtomicBoolean(false);
	/**
	 * A flag signifying that the temporary spatials for selected vertices
	 * should have their locations updated.
	 */
	private final AtomicBoolean newTemporaryPosition = new AtomicBoolean(false);

	/**
	 * A Lock for controlling access to the {@link #selectedVertices}
	 * collection.
	 */
	private final Lock selectionLock = new ReentrantLock(true);

	/**
	 * The starting location for the mouse drag event.
	 */
	private Vector3f dragStart;

	/**
	 * A map of currently-selected vertices, keyed on their IDs.
	 */
	private final Map<Integer, Vertex> selectedVertices;

	/**
	 * This Node should be used to display temporary spatials or geometries on
	 * the jME3 grid.
	 */
	private final Node tempSpatials;

	/**
	 * A list of controllers for the temporary vertices. These should be
	 * instantiated only during the span of the mouse drag event.
	 */
	private final Map<Integer, VertexController> vertexControllers;
	/**
	 * A list of controllers for the temporary edges. These should be
	 * instantiated only during the span of the mouse drag event.
	 */
	private final Map<Integer, EdgeController> edgeControllers;

	/**
	 * The default constructor. Adds any additional {@link ActionListener}s to
	 * the map of {@link MeshApplicationMode#listeners listeners}.
	 */
	public EditMode() {
		super();

		tempSpatials = new Node("editModeTempSpatials");

		selectedVertices = new HashMap<Integer, Vertex>();
		vertexControllers = new HashMap<Integer, VertexController>();
		edgeControllers = new HashMap<Integer, EdgeController>();

		// Add any additional listeners here.

		// Create a listener to listen for the shift keys.
		shiftListener = new ActionListener() {
			public void onAction(String name, boolean isPressed, float tpf) {
				shiftPressed.set(isPressed);
			}
		};
		listeners.put(Key.Shift, shiftListener);

		// Create a listener to listen for the control keys.
		controlListener = new ActionListener() {
			public void onAction(String name, boolean isPressed, float tpf) {
				controlPressed.set(isPressed);
			}
		};
		listeners.put(Key.Control, controlListener);

		// Create a listener to listen for the delete key.
		deleteListener = new ActionListener() {
			public void onAction(String name, boolean isPressed, float tpf) {
				if (!isPressed) {
					app.getSelectionManager().deleteSelection();
				}
			}
		};
		listeners.put(Key.Delete, deleteListener);

		// Create a listener to signify that the mouse is being dragged.
		mouseMoveListener = new AnalogListener() {
			public void onAnalog(String name, float value, float tpf) {
				// We only care about mouse movement if the mouse button is
				// down.
				if (mouseDown.get()) {
					drag.set(true);
					newTemporaryPosition.set(true);
				}
				return;
			}
		};
		listeners.put(Key.MouseMove, mouseMoveListener);

		return;
	}

	/**
	 * Overrides the parent method to provide more {@link ActionListener}s.
	 */
	@Override
	public List<InputListener> getInputListeners() {
		List<InputListener> listeners = super.getInputListeners();

		// Add any additional listeners here.
		listeners.add(shiftListener);
		listeners.add(controlListener);
		listeners.add(deleteListener);
		listeners.add(mouseMoveListener);

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
		updateQueue = app.getUpdateQueue();

		// Register as a camera listener so that the temporary vertex
		// controllers can be updated when the zoom changes.
		app.getChaseCamera().addCameraListener(this);

		// Register as a listener to the MeshApplication's selection.
		app.getSelectionManager().addMeshApplicationListener(this);
		// Force an update to the current selection. This should sync EditMode
		// with the current selection in the MeshApplication.
		selectionChanged();

		return;
	}

	/**
	 * Implements the parent method to clear any temporary data managed by this
	 * MeshApplicationMode.
	 */
	public void clear() {

		// Clear the current selection.
		clearSelection();

		// Unregister from the CustomChaseCamera.
		app.getChaseCamera().removeCameraListener(this);

		// Unregister as a listener to the MeshApplication's selection.
		app.getSelectionManager().removeMeshApplicationListener(this);

		// Clear references to the MeshApplication fields.
		app = null;
		mesh = null;
		grid = null;
		vertexSpatials = null;
		updateQueue = null;

		return;
	}

	/**
	 * Overrides the default operation to perform edit operations on each
	 * simpleUpdate.
	 */
	@Override
	public void simpleUpdate(float tpf) {

		// If the mouse is being dragged, update the locations of any dragged
		// temporary geometries. To determine if the locations need to be
		// updated, the mouse button must be down AND the mouse must have moved
		// since the last update.
		if (!click.get() && newTemporaryPosition.getAndSet(false)) {
			// Add the temporary spatials node if the drag has just started.
			if (!showTemporarySpatials.getAndSet(true)) {
				app.getRootNode().attachChild(tempSpatials);
			}
			// Get the cursor's current location on the grid.
			CollisionResults results = app.getCollision(grid,
					app.getCursorRay());
			if (results.size() > 0) {
				// Determine the vector between the start point for the mouse
				// drag and the current location of the mouse drag.
				Vector3f dragLoc = app.getClosestGridPoint(results
						.getClosestCollision().getContactPoint());
				dragLoc.subtractLocal(dragStart);

				// We need to modify the controllers for all the
				// currently-selected vertices. It's possible that
				// selectionChanged() is executing at the same time, so we need
				// to temporarily "lock" the selected vertex data structures.
				selectionLock.lock();
				try {
					// Add the vector to all temporary vertices.
					for (Entry<Integer, Vertex> e : selectedVertices.entrySet()) {
						float[] array = e.getValue().getLocation();
						Vector3f location = dragLoc.add(array[0], array[1],
								array[2]);
						vertexControllers.get(e.getKey()).setLocation(location);
					}
				} finally {
					selectionLock.unlock();
				}
			}
		}
		// If the dragging has stopped and we are still showing temporary
		// spatials in the scene, stop showing them.
		else if (!drag.get() && showTemporarySpatials.getAndSet(false)) {
			app.getRootNode().detachChild(tempSpatials);
		}

		return;
	}

	/**
	 * Implements the left-click behavior for this MeshApplicationMode.
	 */
	protected void leftClick(boolean isPressed) {

		// Update the mouseDown boolean.
		mouseDown.set(isPressed);

		// We need to differentiate between mouse clicks and mouse drags. Below,
		// we classify them as follows:
		// If your mouse down+up is less than 0.25 seconds, it's a click.
		// If your mouse down+up is greater than 0.25 seconds, then:
		// if the mouse did not move, it is a click
		// if the mouse moved, it is a drag

		if (mouseDown.get()) {
			// Start a timer for the click. The click timer should expire after
			// a quarter of a second. Then it should set the boolean "click" to
			// false.
			clickTimer = new Timer(true);
			clickTimer.schedule(new TimerTask() {
				public void run() {
					click.set(false);
				}
			}, clickThreshold);

			// Set the starting location of the mouse drag.
			CollisionResults results = app.getCollision(grid,
					app.getCursorRayFromClick());
			if (results.size() > 0) {
				dragStart = app.getClosestGridPoint(results
						.getClosestCollision().getContactPoint());
			}
		} else {
			// Stop the click timer.
			clickTimer.cancel();

			// We need to get the current state of the click and drag booleans.
			// We should also reset them since the mouseUp signals the end of
			// the sequence of mouse events.
			boolean click = this.click.getAndSet(true);
			boolean drag = this.drag.getAndSet(false);

			// The first condition is for clicks. Either the click was fast
			// enough or the mouse did not move.
			if (click || !drag) {
				// Determine whether or not shift and control were pressed.
				boolean addToSelection = shiftPressed.get();
				boolean toggleSelection = controlPressed.get();

				// Get the Vertex for the clicked geometry if possible and add
				// it to the collection of selected vertices.
				CollisionResults results = app.getCollision(vertexSpatials,
						app.getCursorRayFromClick());
				Vertex clickedVertex = null;
				int id = 0;
				if (results.size() > 0) {
					// Get the Vertex ID from the VertexView.
					id = Integer.parseInt(results.getClosestCollision()
							.getGeometry().getName());
					clickedVertex = mesh.getVertex(id);
				}

				MeshSelectionManager selection = app.getSelectionManager();
				if (addToSelection) {
					// A shift-click should only add the clicked item to the
					// current selection.
					if (clickedVertex != null) {
						selection.selectVertex(id);
					}
				} else if (toggleSelection) {
					// A control-click should toggle clicked item's selection
					// state.
					if (clickedVertex != null) {
						if (selection.getSelectedVertexIds().contains(id)) {
							selection.deselectVertex(id);
						} else {
							selection.selectVertex(id);
						}
					}
				} else {
					// A standard click should clear the selection and set the
					// clicked item (if any) to the current selection.

					selection.clearSelection();
					if (clickedVertex != null) {
						selection.selectVertex(id);
					}
				}
			}
			// The second condition is for drags. The click was too slow and the
			// mouse moved.
			else {
				// For now, all drag does it drag the currently-selected
				// vertices. We do not draw boxes around things yet, so we do
				// not need to check for shift or control.

				// Update the location for each of the selected vertices.
				for (Vertex v : selectedVertices.values()) {
					VertexController controller = vertexControllers.get(v
							.getId());
					Vector3f location = controller.getLocation();
					v.setLocation(location.x, location.y, location.z);
				}
			}
		}
		return;
	}

	/**
	 * Implements the right-click behavior for this MeshApplicationMode.
	 */
	protected void rightClick(boolean isPressed) {
		// Do nothing.
	}

	/**
	 * Implements the parent method to provide the MeshApplicationMode name.
	 */
	public String getName() {
		return "Edit Elements";
	}

	/**
	 * Implements the parent method to provide the MeshApplicationMode
	 * description.
	 */
	public String getDescription() {
		return "Modifies or removes elements or polygons selected on the grid.";
	}

	/**
	 * This method updates the EditMode's knowledge of the currently-selected
	 * vertices in the MeshApplication.
	 */
	public void selectionChanged() {

		// We can only modify the selection if it's not being used. Currently,
		// the only other place where the current selection is read when this
		// method executes is in simpleUpdate().
		selectionLock.lock();
		try {
			// Clear all temporary controllers, etc.
			clearSelection();

			// Get the size that the vertex view should have.
			float vertexSize = app.getVertexSize();
			float scale = app.getScale();

			// Add each of the currently-selected vertices to the map.
			for (Vertex vertex : app.getSelectionManager()
					.getSelectedVertices()) {
				int id = vertex.getId();

				// Put the selected vertex in the map.
				selectedVertices.put(id, vertex);

				// Create a temporary clone of the current vertex.
				Vertex clone = (Vertex) vertex.clone();

				// Create a temporary controller for the clone vertex.
				VertexController controller = new VertexController(clone,
						updateQueue, app.createBasicMaterial(null));
				controller.setParentNode(tempSpatials);
				controller.setState(StateType.Temporary);
				controller.setSize(vertexSize);
				controller.setScale(scale);
				vertexControllers.put(clone.getId(), controller);
			}
		} finally {
			selectionLock.unlock();
		}

		return;
	}

	/**
	 * Clears all currently selected vertices and their associated temporary
	 * geometries.
	 */
	private void clearSelection() {
		// Clear the current selection. We need to reset the states of
		// each of the existing VertexControllers.
		selectedVertices.clear();

		// Remove all of the temporary controllers for vertices and edges.
		for (VertexController controller : vertexControllers.values()) {
			controller.dispose();
		}
		vertexControllers.clear();
		for (EdgeController controller : edgeControllers.values()) {
			controller.dispose();
		}
		edgeControllers.clear();

		return;
	}

	// ---- Implements ICameraListener ---- //
	/**
	 * This method updates all controllers for temporary spatials to account for
	 * any changes in zoom.
	 */
	public void zoomChanged(float distance) {
		// Get the sizes of vertices and edges.
		float vertexSize = app.getVertexSize();
		float edgeSize = app.getEdgeSize();

		// Update the vertex and edge controllers.
		for (VertexController c : vertexControllers.values()) {
			c.setSize(vertexSize);
		}
		for (EdgeController c : edgeControllers.values()) {
			c.setSize(edgeSize);
		}

		return;
	}
	// ------------------------------------ //

}
