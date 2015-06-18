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
package org.eclipse.ice.client.widgets.mesh;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.ice.client.widgets.jme.InputControl;
import org.eclipse.ice.datastructures.form.mesh.Vertex;

import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

/**
 * This {@link MeshAppStateMode} allows the user to select available vertices
 * and modify them by dragging them or deleting them. The left/right control and
 * left/right shift keys aid in selection (control is usually toggle, while
 * shift is usually add).
 * 
 * @author Jordan Deyton
 * 
 */
public class EditMode extends MeshAppStateMode implements
		IMeshSelectionListener {

	// ---- MeshAppState variables ---- //
	/**
	 * The floor of the grid in the {@link MeshAppState}.
	 */
	private Geometry grid;
	/**
	 * The parent {@link Node} containing spatials in the scene for all
	 * {@link Vertex vertices} in the {@link #mesh}.
	 */
	private Node vertexSpatials;
	/**
	 * The update queue for the <code>MeshAppState</code>.
	 */
	private ConcurrentLinkedQueue<AbstractMeshController> updateQueue;
	// -------------------------------- //

	// ---- Custom Controls ---- //
	/**
	 * Sets the flag {@link #shiftPressed} if the left or right shift keys are
	 * pressed.
	 */
	private InputControl shiftAction;
	/**
	 * This listener sets the flag {@link #controlPressed} if the left or right
	 * control keys are pressed.
	 */
	private InputControl ctrlAction;
	/**
	 * This listener listens for presses of the delete key.
	 */
	private InputControl deleteAction;
	/**
	 * This listener sets the flags {@link #drag} and {@link #mouseMoved} if the
	 * cursor moves.
	 */
	private InputControl mouseMoveListener;
	// ------------------------- //

	// ---- Keyboard input flags ---- //
	/**
	 * True if the shift button is pressed, false otherwise.
	 */
	private final AtomicBoolean shiftPressed = new AtomicBoolean(false);
	/**
	 * True if the control (CTRL) button is pressed, false otherwise.
	 */
	private final AtomicBoolean controlPressed = new AtomicBoolean(false);
	// ------------------------------ //

	// ---- Mouse click and drag ---- //
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
	 * True if the sequence of mouseDown and mouseUp occurs fast enough to be
	 * considered a click. (currently, it's a click regardless of movement if
	 * the click is less than 0.25 seconds).
	 */
	private final AtomicBoolean click = new AtomicBoolean(true);

	/**
	 * The threshold for a click to be eligible to be considered a drag (in
	 * milliseconds).
	 */
	private static final long clickThreshold = 250;
	/**
	 * A timer used to set {@link #click} to false after 0.25 seconds.
	 */
	private Timer clickTimer;
	/**
	 * The starting location for the mouse drag event.
	 */
	private Vector3f dragStart;
	// ------------------------------ //

	// ---- Current selection ---- //
	/**
	 * A Lock for controlling access to the {@link #selectedVertices}
	 * collection.
	 */
	private final Lock selectionLock = new ReentrantLock(true);

	/**
	 * A map of currently-selected vertices, keyed on their IDs.
	 */
	private final Map<Integer, Vertex> selectedVertices;
	// --------------------------- //

	// ---- Temporary spatials ---- //
	/**
	 * A flag signifying that the temporary spatials should be displayed. This
	 * is typically done simply by attaching or detaching {@link #tempSpatials}
	 * from the <code>MeshAppState</code>'s rootNode.
	 */
	private final AtomicBoolean showTemporarySpatials = new AtomicBoolean(false);
	/**
	 * A flag signifying that the temporary spatials for selected vertices
	 * should have their locations updated.
	 */
	private final AtomicBoolean newTemporaryPosition = new AtomicBoolean(false);

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

	// ---------------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param appState
	 *            The <code>MeshAppState</code> that this
	 *            <code>MeshAppStateMode</code> supports.
	 */
	public EditMode(MeshAppState appState) {
		super(appState);

		// Initialize the root Node for temporary spatials.
		tempSpatials = new Node("editModeTempSpatials");

		// Initialize any final collections here.
		selectedVertices = new HashMap<Integer, Vertex>();
		vertexControllers = new HashMap<Integer, VertexController>();
		edgeControllers = new HashMap<Integer, EdgeController>();

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
		return "Edit Elements";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.mesh.MeshAppStateMode#getDescription()
	 */
	@Override
	public String getDescription() {
		return "Modifies or removes elements or polygons selected on the grid.";
	}

	// ---------------------------------- //

	// ---- Initialization methods ---- //
	@Override
	protected void initScene() {
		super.initScene();

		// Get the fields of interest from the MeshAppState.
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
		AnalogListener analog;

		// Create a listener to set the shiftPressed flag when left or right
		// shift is pressed.
		name = "addToSelection";
		action = new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				shiftPressed.set(isPressed);
			}
		};
		shiftAction = new InputControl(action, name);
		shiftAction.addTriggers(name, new KeyTrigger(KeyInput.KEY_LSHIFT),
				new KeyTrigger(KeyInput.KEY_RSHIFT));

		// Create a listener to set the controlPressed flag when left or right
		// control is pressed.
		name = "toggleSelection";
		action = new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				controlPressed.set(isPressed);
			}
		};
		ctrlAction = new InputControl(action, name);
		ctrlAction.addTriggers(name, new KeyTrigger(KeyInput.KEY_LCONTROL),
				new KeyTrigger(KeyInput.KEY_RCONTROL));

		// Create a listener to cancel the vertices used to create a new
		// polygon. It should be triggered by backspace, delete, and escape.
		name = "deleteSelection";
		action = new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				if (!isPressed) {
					appState.getSelectionManager().deleteSelection();
				}
			}
		};
		deleteAction = new InputControl(action, name);
		deleteAction.addTriggers(name, new KeyTrigger(KeyInput.KEY_BACK),
				new KeyTrigger(KeyInput.KEY_DELETE), new KeyTrigger(
						KeyInput.KEY_ESCAPE));

		// Create a listener to signify that the mouse is being dragged.
		name = "mouseMove";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				// We only care about mouse movement if the mouse button is
				// down.
				if (mouseDown.get()) {
					drag.set(true);
					newTemporaryPosition.set(true);
				}
			}
		};
		mouseMoveListener = new InputControl(analog, name);
		mouseMoveListener.addTriggers(name, new MouseAxisTrigger(
				MouseInput.AXIS_X, false), new MouseAxisTrigger(
				MouseInput.AXIS_X, true), new MouseAxisTrigger(
				MouseInput.AXIS_Y, true), new MouseAxisTrigger(
				MouseInput.AXIS_Y, false));

		return;
	}

	// -------------------------------- //

	// ---- Enable/Disable ---- //
	@Override
	public void enableAppState() {
		super.enableAppState();

		// Register as a listener to the MeshAppState's selection.
		appState.getSelectionManager().addMeshApplicationListener(this);
		// Force an update to the current selection. This should sync EditMode
		// with the current selection in the MeshAppState.
		selectionChanged();

		return;
	}

	/**
	 * Registers additional controls for editing mesh elements.
	 */
	public void registerControls() {
		super.registerControls();

		// Get the InputManager and register all custom controls with it.
		InputManager input = getApplication().getInputManager();
		shiftAction.registerWithInput(input);
		ctrlAction.registerWithInput(input);
		deleteAction.registerWithInput(input);
		mouseMoveListener.registerWithInput(input);

		return;
	}

	@Override
	public void disableAppState() {

		// Clear the current selection.
		clearSelection();
		// Unregister as a listener to the MeshAppState's selection.
		appState.getSelectionManager().removeMeshApplicationListener(this);

		super.disableAppState();
	}

	/**
	 * Unregisters the additional controls for adding mesh elements.
	 */
	public void unregisterControls() {
		// Unregister the controls from the InputManager.
		shiftAction.unregisterFromInput();
		ctrlAction.unregisterFromInput();
		deleteAction.unregisterFromInput();
		mouseMoveListener.unregisterFromInput();

		super.unregisterControls();
	}

	// ------------------------ //

	// ---- Cleanup methods ---- //
	@Override
	protected void clearScene() {

		// Clear references to the MeshAppState fields.
		grid = null;
		vertexSpatials = null;
		updateQueue = null;

		super.clearScene();
	}

	/**
	 * Unregisters custom controls in addition to the default
	 * {@link MeshAppStateMode} controls.
	 */
	@Override
	protected void clearControls() {
		// Unset the custom controls.
		shiftAction = null;
		ctrlAction = null;
		deleteAction = null;
		mouseMoveListener = null;

		super.clearControls();
	}

	// ------------------------- //

	@Override
	public void update(float tpf) {
		super.update(tpf);

		// If the mouse is being dragged, update the locations of any dragged
		// temporary geometries. To determine if the locations need to be
		// updated, the mouse button must be down AND the mouse must have moved
		// since the last update.
		if (!click.get() && newTemporaryPosition.getAndSet(false)) {
			// Add the temporary spatials node if the drag has just started.
			if (!showTemporarySpatials.getAndSet(true)) {
				appState.getRootNode().attachChild(tempSpatials);
			}
			// Get the cursor's current location on the grid.
			CollisionResults results = getCollision(grid,
					appState.getCursorRay());
			if (results.size() > 0) {
				// Determine the vector between the start point for the mouse
				// drag and the current location of the mouse drag.
				Vector3f dragLoc = appState.getClosestGridPoint(results
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
			appState.getRootNode().detachChild(tempSpatials);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.mesh.MeshAppStateMode#leftClick(boolean ,
	 * float)
	 */
	@Override
	public void leftClick(boolean isPressed, float tpf) {
		super.leftClick(isPressed, tpf);

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
			CollisionResults results = getCollision(grid,
					appState.getCursorRayFromClick());
			if (results.size() > 0) {
				dragStart = appState.getClosestGridPoint(results
						.getClosestCollision().getContactPoint());
			}
		}
		// Note: We have to check the clickTimer because it may be possible for
		// the AWTPanel to fire a mouse release event but not a mouse press
		// event.
		else if (clickTimer != null) {
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
				CollisionResults results = getCollision(vertexSpatials,
						appState.getCursorRayFromClick());
				Vertex clickedVertex = null;
				int id = 0;
				if (results.size() > 0) {
					// Get the Vertex ID from the VertexView.
					id = Integer.parseInt(results.getClosestCollision()
							.getGeometry().getName());
					clickedVertex = appState.getMesh().getVertex(id);
				}

				MeshSelectionManager selection = appState.getSelectionManager();
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

			// Reset the click timer.
			clickTimer = null;
		}

		return;
	}

	// ---- Implements IMeshSelectionListener ---- //
	/**
	 * This method updates the <code>EditMode</code>'s knowledge of the
	 * currently-selected vertices in the <code>MeshApplication</code>.
	 */
	@Override
	public void selectionChanged() {
		// We can only modify the selection if it's not being used. Currently,
		// the only other place where the current selection is read when this
		// method executes is in simpleUpdate().
		selectionLock.lock();
		try {
			// Clear all temporary controllers, etc.
			clearSelection();

			// Get the size that the vertex view should have.
			float vertexSize = appState.getVertexSize();
			float scale = appState.getScale();

			// Add each of the currently-selected vertices to the map.
			for (Vertex vertex : appState.getSelectionManager()
					.getSelectedVertices()) {
				int id = vertex.getId();

				// Put the selected vertex in the map.
				selectedVertices.put(id, vertex);

				// Create a temporary clone of the current vertex.
				Vertex clone = (Vertex) vertex.clone();

				// Create a temporary controller for the clone vertex.
				// TODO Set up this material in initMaterials
				VertexController controller = new VertexController(clone,
						updateQueue, createBasicMaterial(null));
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

	// ------------------------------------------- //

	// ---- Getters and Setters ---- //
	// ----------------------------- //

	// ---- Private utility methods ---- //
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

	// --------------------------------- //

	// ---- Implements ICameraListener ---- //
	/**
	 * This method updates all controllers for temporary spatials to account for
	 * any changes in zoom.
	 */
	public void zoomChanged(float distance) {
		// Get the sizes of vertices and edges.
		float vertexSize = appState.getVertexSize();
		float edgeSize = appState.getEdgeSize();

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
