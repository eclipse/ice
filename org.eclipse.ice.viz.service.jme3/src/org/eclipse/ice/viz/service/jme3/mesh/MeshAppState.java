/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or 
 *      initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.mesh;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.eclipse.ice.viz.service.jme3.application.CustomChaseCamera;
import org.eclipse.ice.viz.service.jme3.application.EmbeddedView;
import org.eclipse.ice.viz.service.jme3.application.ICameraListener;
import org.eclipse.ice.viz.service.jme3.application.MasterApplication;
import org.eclipse.ice.viz.service.jme3.application.SimpleAppState;
import org.eclipse.ice.viz.service.jme3.application.ViewAppState;
import org.eclipse.ice.viz.service.jme3.mesh.MeshAppStateModeFactory.Mode;
import org.eclipse.ice.viz.service.jme3.widgets.InputControl;
import org.eclipse.ice.viz.service.mesh.datastructures.Edge;
import org.eclipse.ice.viz.service.mesh.datastructures.Polygon;
import org.eclipse.ice.viz.service.mesh.datastructures.Vertex;
import org.eclipse.ice.viz.service.mesh.datastructures.VizMeshComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jme3.app.state.AppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.Trigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Ray;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Quad;
import com.jme3.util.BufferUtils;

/**
 * This jME3-based {@link AppState} provides a 3D view of a
 * {@link MeshComponent}.
 * 
 * @author Jordan Deyton
 * 
 */
public class MeshAppState extends ViewAppState implements
		IMeshSelectionListener {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(MeshAppState.class);

	// TODO The player controls and physics have long since been deprecated.
	// They will need to be updated to something newer that works the same way.

	// ---- Scene components ---- //
	/**
	 * The surface of the grid. Although it is invisible, it is located in the
	 * same plane as the grid lines. It is used to determine locations based on
	 * ray collisions.
	 */
	private Geometry grid;

	/**
	 * This <code>Node</code> contains the spatials for all vertices.
	 */
	private final Node vertexRoot;
	/**
	 * This <code>Node</code> contains the spatials for all edges.
	 */
	private final Node edgeRoot;
	/**
	 * This <code>Node</code> contains the spatials for all temporary objects.
	 */
	private final Node tempRoot;
	// -------------------------- //

	// ---- Grid properties ---- //
	/**
	 * The width (x-length) of the grid. The value should be a whole number.
	 * Changing this changes the size of each grid component.
	 */
	private final float width = 32f;
	/**
	 * The length (y-length) of the grid. The value should be a whole number.
	 * Changing this changes the size of each grid component.
	 */
	private final float length = 16f;

	/**
	 * The minimum possible x value on the grid. This is in jME world units!
	 */
	private final float minX = width * -0.5f;
	/**
	 * The minimum possible y value on the grid. This is in jME world units!
	 */
	private final float minY = length * -0.5f;
	/**
	 * The maximum possible x value on the grid. This is in jME world units!
	 */
	private final float maxX = width * 0.5f;
	/**
	 * The maximum possible y value on the grid. This is in jME world units!
	 */
	private final float maxY = length * 0.5f;
	// ------------------------- //

	// ---- Player properties. ---- //
	/**
	 * The player that is located on the grid surface at the center of the
	 * screen beneath the crosshairs.
	 */
	private CharacterControl player;

	/**
	 * True if W or the up arrow is pressed. This should move the player up.
	 */
	private boolean up;
	/**
	 * True if S or the down arrow is pressed. This should move the player down.
	 */
	private boolean down;
	/**
	 * True if A or the left arrow is pressed. This should move the player left.
	 */
	private boolean left;
	/**
	 * True if D or the right arrow is pressed. This should move the player
	 * right.
	 */
	private boolean right;
	/**
	 * True if left or right shift is pressed. This should speed movement.
	 */
	private boolean shift;
	/**
	 * True if left or right control is pressed. This should slow movement.
	 */
	private boolean control;

	/**
	 * The movement speed. This is used in {@link #simpleUpdate(float)} to
	 * determine how far to move the figure in each frame. It can also be
	 * affected by the shift and control keys (see {@link #initControls()}).
	 */
	private float moveSpeed = 1f;
	/**
	 * A vector storing the direction in which the player is currently traveling
	 * for a simpleUpdate().
	 */
	private final Vector3f walkDirection;
	// ---------------------------- //

	// ---- Heads Up Display (HUD) ---- //
	/**
	 * The HUD label showing the player's (crosshair's) location on the grid.
	 */
	private BitmapText HUDPlayerLocation;
	/**
	 * The HUD label showing the cursor's location on the grid.
	 */
	private BitmapText HUDCursorLocation;
	/**
	 * The crosshairs showing the player's location (the center of the screen).
	 */
	private BitmapText HUDCrosshair;
	/**
	 * The format used to display (x,y) coordinates in the HUD.
	 */
	private final String HUDCoordinateFormat = "%05.2f,%05.2f";
	// -------------------------------- //

	// ---- Mesh MVC components ---- //
	/**
	 * The currently displayed <code>MeshComponent</code>.
	 */
	private VizMeshComponent mesh;
	/**
	 * An ordered map of all the polygons currently displayed in the
	 * MeshApplication.
	 */
	private TreeMap<Integer, Polygon> polygons;
	/**
	 * An ordered map of VertexControllers keyed on their associated Vertex's
	 * ID.
	 */
	private TreeMap<Integer, VertexController> vertexControllers;
	/**
	 * An ordered map of EdgeControllers keyed on their associated Edge's ID.
	 */
	private TreeMap<Integer, EdgeController> edgeControllers;
	// ----------------------------- //

	// ---- Current selection ---- //
	/**
	 * A manager for handling the selection of polygons, edges, and vertices in
	 * the mesh.
	 */
	private final MeshSelectionManager selectionManager;
	/**
	 * A map of currently-selected vertices in the MeshApplication, keyed on
	 * their IDs.
	 */
	private final Set<Integer> selectedVertices;
	/**
	 * A map of currently-selected edges in the MeshApplication, keyed on their
	 * IDs.
	 */
	private final Set<Integer> selectedEdges;
	// --------------------------- //

	// ---- Application Update/Synchronization ---- //
	/**
	 * A concurrent queue containing all AbstractMeshControllers whose views are
	 * waiting to be updated in the simpleUpdate() method.
	 */
	private final ConcurrentLinkedQueue<AbstractMeshController> updateQueue;
	/**
	 * A handler to listen for updates from the mesh. We use a separate thread
	 * for this to isolate the update code and ensure that critical data
	 * structures are not manipulated simultaneously in multiple threads.
	 */
	private final MeshUpdateHandler meshUpdateHandler;
	// -------------------------------------------- //

	// ---- Custom Controls ---- //
	/**
	 * An <code>InputControl</code> for handling player/camera movement. This
	 * enables the arrow keys and WSAD to move the player/camera.
	 */
	private InputControl moveAction;
	/**
	 * An <code>InputControl</code> for changing the speed of the player. Shift
	 * speeds up player movement, while control slows the player. Holding both
	 * or neither should return the speed to normal.
	 */
	private InputControl speedAction;
	// ------------------------- //

	// ---- Camera ---- //
	// The below primitives should not be accessed directly unless zoomLock, the
	// ReentrantReadWriteLock, is acquired.
	/**
	 * A reference to the {@link CustomChaseCamera} used to follow the player
	 * around the scene. It is intialized and configured to have a top-down view
	 * in {@link #initCamera()}.
	 */
	private CustomChaseCamera chaseCam;
	/**
	 * A lock used to read or update information directly associated with
	 * {@link #chaseCam}'s current zoom distance. If only reading will happen,
	 * use its read-lock.
	 */
	private final ReentrantReadWriteLock zoomLock = new ReentrantReadWriteLock();
	/**
	 * The current distance of {@link #chaseCam} from the target (
	 * {@link #player}). To read this value alone, use
	 * {@link #getZoomDistance()}. If you want to read it and other values
	 * simultaneously or write it, use {@link #zoomLock}.
	 */
	private float zoomDistance = 10f;
	/**
	 * The current radius of the Spheres used to represent vertices. To read
	 * this value alone, use {@link #getVertexSize()}. If you want to read it
	 * and other values simultaneously or write it, use {@link #zoomLock}.
	 */
	private float vertexSize = 0.2f;
	/**
	 * The current line width of the Lines used to represent edges. To read this
	 * value alone, use {@link #getEdgeSize())}. If you want to read it and
	 * other values simultaneously or write it, use {@link #zoomLock}.
	 */
	private float edgeSize = 5f;

	/**
	 * The scale used for determining how far to spread parts of the mesh. For
	 * example, a scale of 0.25 means that 1 world unit in jME3 corresponds to
	 * 0.25 units in the mesh model. This is stored as a float in an
	 * AtomicInteger.
	 */
	private final AtomicInteger scale = new AtomicInteger(
			Float.floatToIntBits(1f));
	// ---------------- //

	/**
	 * The current {@link MeshAppStateMode}.
	 */
	private MeshAppStateMode mode;
	/**
	 * The factory used to select from available {@link MeshAppStateMode}s.
	 */
	private final MeshAppStateModeFactory modeFactory;

	/**
	 * The default constructor.
	 */
	public MeshAppState() {

		// Initialize the current selection collections.
		selectedVertices = new HashSet<Integer>();
		selectedEdges = new HashSet<Integer>();
		selectionManager = new MeshSelectionManager();
		selectionManager.addMeshApplicationListener(this);

		// Initialize the concurrent queue used to process all controllers whose
		// views must be updated.
		updateQueue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// Initialize the listener that processes updates from the Mesh.
		meshUpdateHandler = new MeshUpdateHandler();

		// Initialize the maps of controllers.
		vertexControllers = new TreeMap<Integer, VertexController>();
		edgeControllers = new TreeMap<Integer, EdgeController>();

		// Initialize the map of currently displayed polygons.
		polygons = new TreeMap<Integer, Polygon>();

		// Creates a new Vector3f initialized to (0f, 0f, 0f). This vector
		// contains the player's current walk direction as the arrow or wasd
		// keys are pressed.
		walkDirection = new Vector3f();

		// Initialize the scene Nodes.
		vertexRoot = new Node("vertices");
		edgeRoot = new Node("edges");
		tempRoot = new Node("tempSpatials");

		// Create the mode factory. We should add all available modes to this
		// CompositeAppState but initially disable them.
		modeFactory = new MeshAppStateModeFactory(this);
		for (Mode type : modeFactory.getAvailableModes()) {
			SimpleAppState mode = modeFactory.getMode(type);
			mode.setEnabled(false);
			addAppState(mode);
		}
		// Set the initial mode to add mode.
		setMode(modeFactory.getMode(Mode.Add));

		return;
	}

	/**
	 * In addition to the default start behavior, this method starts any
	 * associated resources.
	 */
	@Override
	public void start(MasterApplication app) {
		if (!isInitialized() && app != null) {
			super.start(app);

			// Start the current mode. We may need to disable its controls
			mode.start(app);

			// Start the MeshUpdateHandler.
			meshUpdateHandler.start(this);
		}

		return;
	}

	/**
	 * Overrides the default behavior to stop the {@link #mode} and
	 * {@link #meshUpdateHandler}.
	 */
	@Override
	public void stop() {

		if (isInitialized()) {
			// Stop all of the supported modes of operation. This includes the
			// current mode.
			for (Mode mode : modeFactory.getAvailableModes()) {
				modeFactory.getMode(mode).stop();
			}

			// Stop the update handler.
			meshUpdateHandler.stop();

			// Proceed with the normal stop process.
			super.stop();
		}

		return;
	}

	// ---- Initialization ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#initMaterials()
	 */
	@Override
	protected void initMaterials() {
		// Nothing to do yet.
	}

	/**
	 * Moves the default axes off the xy-plane slightly to appear over the
	 * wireframe grid.
	 */
	@Override
	protected void initAxes() {
		// Create the default axes.
		super.initAxes();

		// Lift the axes a little so they can be seen over the grid.
		Vector3f position = new Vector3f(0f, 0f, 0.001f);
		axes.getChild("x").setLocalTranslation(position);
		axes.getChild("y").setLocalTranslation(position);
		axes.getChild("z").setLocalTranslation(position);

		return;
	}

	/**
	 * Initializes the grid graphics, player, and player physics.
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#initScene()
	 */
	@Override
	protected void initScene() {

		Geometry geometry;
		Node node;
		Quad quad;
		Grid grid;
		Material material;

		// Initialize the physics system for the player and the walls.
		BulletAppState physics = new BulletAppState();
		getApplication().getStateManager().attach(physics);

		Quaternion rotation = new Quaternion(new float[] { FastMath.HALF_PI,
				0f, 0f });

		/* ---- Create the grid. ---- */
		// Create the Node containing the components of the grid.
		node = new Node("gridNode");
		rootNode.attachChild(node);

		// Create a background that lies beneath the blue grid.
		quad = new Quad(width * 4f, length * 4f);
		geometry = new Geometry("gridBackground", quad);
		geometry.setMaterial(createBasicMaterial(ColorRGBA.Gray));
		// Center the background on the origin.
		geometry.setLocalTranslation(width * 4f * -0.5f, length * 4f * -0.5f,
				-5f);
		node.attachChild(geometry);

		// Create the blue (major) grid.
		grid = new Grid((int) length + 1, (int) width + 1, 1f);
		grid.setLineWidth(2f);
		geometry = new Geometry("gridMajor", grid);
		material = createBasicMaterial(ColorRGBA.Blue);
		material.getAdditionalRenderState().setWireframe(true);
		geometry.setMaterial(material);
		// Rotate the grid and center it on the origin.
		geometry.setLocalRotation(rotation);
		geometry.setLocalTranslation(width * -0.5f, length * 0.5f, 0f);
		node.attachChild(geometry);

		// Create the minor grid.
		grid = new Grid((int) length * 4 + 1, (int) width * 4 + 1, 0.25f);
		grid.setLineWidth(1f);
		geometry = new Geometry("gridMinor", grid);
		material = createBasicMaterial(ColorRGBA.Blue);
		material.getAdditionalRenderState().setWireframe(true);
		geometry.setMaterial(material);
		// Rotate the grid and center it on the origin.
		geometry.setLocalRotation(rotation);
		geometry.setLocalTranslation(width * -0.5f, length * 0.5f, 0f);
		node.attachChild(geometry);

		// Create the invisible surface of the grid that will register ray hits.
		quad = new Quad(width * 4f, length * 4f);
		geometry = new Geometry("gridSurface", quad);
		material = createBasicMaterial(ColorRGBA.BlackNoAlpha);
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		geometry.setMaterial(material);
		geometry.setQueueBucket(Bucket.Transparent);
		// Center the surface on the origin.
		geometry.setLocalTranslation(width * 4f * -0.5f, length * 4f * -0.5f,
				0f);
		node.attachChild(geometry);

		// Set the grid geometry used for ray collisions.
		this.grid = geometry;

		// Create the mesh surrounding the grid. This is basically a large Quad
		// with a hole in the middle for the grid.
		Mesh mesh = new Mesh();
		// Set the distance from the grid over which the mesh should extend to
		// hide the gray background.
		float d = 50f;
		float w = width, l = length;
		Vector3f vertices[] = { new Vector3f(-d, -d, 0f),
				new Vector3f(0f, -d, 0f), new Vector3f(w + d, -d, 0f),
				new Vector3f(0f, 0f, 0f), new Vector3f(w, 0f, 0f),
				new Vector3f(w + d, 0f, 0f), new Vector3f(-d, l, 0f),
				new Vector3f(0f, l, 0f), new Vector3f(w, l, 0f),
				new Vector3f(-d, l + d, 0f), new Vector3f(w, l + d, 0f),
				new Vector3f(w + d, l + d, 0f) };
		for (Vector3f vertex : vertices)
			vertex.addLocal(minX, minY, 0f);
		int indices[] = { 0, 7, 6, 0, 1, 7, 1, 2, 3, 2, 5, 3, 4, 5, 11, 4, 11,
				10, 8, 10, 9, 6, 8, 9 };
		mesh.setBuffer(Type.Position, 3,
				BufferUtils.createFloatBuffer(vertices));
		mesh.setBuffer(Type.Index, 3, BufferUtils.createIntBuffer(indices));

		// Create a Geometry from the mesh and set its material.
		geometry = new Geometry("background", mesh);
		geometry.setMaterial(createBasicMaterial(ColorRGBA.Black));
		// This call is necessary so that the mesh updates its bounds properly.
		geometry.updateModelBound();

		// Add the cover mesh to the scene.
		node.attachChild(geometry);
		/* -------------------------- */

		/* ---- Create the player. ---- */
		// Create the Node containing the player.
		node = new Node("playerNode");
		rootNode.attachChild(node);

		// We set up collision detection for the player by creating
		// a capsule collision shape and a CharacterControl.
		// The CharacterControl offers extra settings for
		// size, stepheight, jumping, falling, and gravity.
		// We can use a BoxCollisionShape because we have rectangular bounds.
		BoxCollisionShape collisionShape = new BoxCollisionShape(new Vector3f(
				0.5f, 0.5f, 0.5f));
		player = new CharacterControl(collisionShape, 1f);

		// Add the player to the player Node.
		node.addControl(player);

		// Adjust the player's location to a spot on the grid.
		player.setPhysicsLocation(new Vector3f(0f, 0f, 0.5f));
		// Set the player to be pointed due north (to the top of the screen).
		player.setViewDirection(Vector3f.UNIT_Y);
		// The player should not be affected by gravity. This means the grid
		// does not have to partake in the physics.
		player.setFallSpeed(0f);

		// We attach the player to the physics space to make it collide with
		// bounding walls.
		physics.getPhysicsSpace().add(player);
		/* ---------------------------- */

		/* ---- Attach the Nodes to contain views. ---- */
		rootNode.attachChild(vertexRoot);
		rootNode.attachChild(edgeRoot);
		rootNode.attachChild(tempRoot);
		/* -------------------------------------------- */

		return;
	}

	/**
	 * Initializes the controls WSAD and arrow keys for movement and
	 * shift/control to speed/slow movement.
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#initControls()
	 */
	@Override
	protected void initControls() {

		ActionListener action;

		// ---- Player movement controls ---- //
		// Set up the mapping names for use with the movement controls.
		final String leftKey = "left";
		final String rightKey = "right";
		final String upKey = "up";
		final String downKey = "down";
		// Set up a listener for keyboard key-press events.
		action = new ActionListener() {
			@Override
			public void onAction(String keyBinding, boolean isPressed, float tpf) {
				if (leftKey.equals(keyBinding)) {
					left = isPressed;
				} else if (rightKey.equals(keyBinding)) {
					right = isPressed;
				} else if (upKey.equals(keyBinding)) {
					up = isPressed;
				} else { // The last remaining option is down!
					down = isPressed;
				}
			}
		};
		// Create the InputControl.
		moveAction = new InputControl(action, leftKey, rightKey, upKey, downKey);
		// Add triggers for the movement mappings.
		moveAction.addTriggers(leftKey, new KeyTrigger(KeyInput.KEY_A),
				new KeyTrigger(KeyInput.KEY_LEFT));
		moveAction.addTriggers(rightKey, new KeyTrigger(KeyInput.KEY_D),
				new KeyTrigger(KeyInput.KEY_RIGHT));
		moveAction.addTriggers(upKey, new KeyTrigger(KeyInput.KEY_W),
				new KeyTrigger(KeyInput.KEY_UP));
		moveAction.addTriggers(downKey, new KeyTrigger(KeyInput.KEY_S),
				new KeyTrigger(KeyInput.KEY_DOWN));
		// ---------------------------------- //

		// ---- Player movement speed controls ---- //
		// Set up the mapping names for use with the speed controls.
		final String ctrlKey = "slowMovement";
		final String shiftKey = "fastMovement";
		// Set up a listener for keyboard key-press events.
		action = new ActionListener() {
			@Override
			public void onAction(String keyBinding, boolean isPressed, float tpf) {
				if (ctrlKey.equals(keyBinding)) {
					control = isPressed;
				} else { // Shift key is the only other option.
					shift = isPressed;
				}
				updateMoveSpeed();
			}
		};
		// Create the InputControl.
		speedAction = new InputControl(action, ctrlKey, shiftKey);
		// Add triggers for the speed control mappings.
		speedAction.addTriggers(ctrlKey, new KeyTrigger(KeyInput.KEY_LCONTROL),
				new KeyTrigger(KeyInput.KEY_RCONTROL));
		speedAction.addTriggers(shiftKey, new KeyTrigger(KeyInput.KEY_LSHIFT),
				new KeyTrigger(KeyInput.KEY_RSHIFT));
		// ---------------------------------------- //

		return;
	}

	// ------------------------ //

	// ---- Enable/Disable ---- //
	/**
	 * Overrides the default behavior to additionally enable the current
	 * {@link #mode}.
	 */
	@Override
	public void enableAppState() {
		super.enableAppState();

		if (mode != null) {
			mode.setEnabled(true);
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#registerControls()
	 */
	@Override
	public void registerControls() {
		InputManager input = getApplication().getInputManager();
		moveAction.registerWithInput(input);
		speedAction.registerWithInput(input);
	}

	/**
	 * Overrides the default behavior to additionally disable the current
	 * {@link #mode},
	 */
	@Override
	public void disableAppState() {
		if (mode != null) {
			mode.setEnabled(false);
		}

		super.disableAppState();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.SimpleAppState#unregisterControls()
	 */
	@Override
	public void unregisterControls() {
		moveAction.unregisterFromInput();
		speedAction.unregisterFromInput();
	}

	// ------------------------ //

	// ---- Cleanup ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#clearControls()
	 */
	@Override
	protected void clearControls() {
		// All we need to do is clear the references to the actions.
		moveAction = null;
		speedAction = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#clearScene()
	 */
	@Override
	protected void clearScene() {
		Node node;

		// Get the physics system for the player and the walls.
		BulletAppState physics = getApplication().getStateManager().getState(
				BulletAppState.class);

		/* ---- Detach the Nodes that contain views. ---- */
		rootNode.detachChild(vertexRoot);
		rootNode.detachChild(edgeRoot);
		rootNode.detachChild(tempRoot);
		/* ---------------------------------------------- */

		/* ---- Delete the player. ---- */
		// Detach the Node containing the player.
		node = (Node) rootNode.getChild("playerNode");
		rootNode.detachChild(node);

		// Remove the player physics from the player Node.
		node.removeControl(player);

		// We attach the player to the physics space to make it collide with
		// bounding walls.
		physics.getPhysicsSpace().remove(player);

		// Unset the player physics.
		player = null;
		/* ---------------------------- */

		/* ---- Delete the grid. ---- */
		// Detach the Node containing the components of the grid.
		node = (Node) rootNode.getChild("gridNode");
		rootNode.detachChild(node);

		// Detach all children from the node. This effectively deletes all of
		// the components of the grid.
		node.detachAllChildren();

		// Unset the grid geometry used for ray collisions.
		this.grid = null;
		;
		/* -------------------------- */

		// Remove the physics system.
		getApplication().getStateManager().detach(physics);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.ViewAppState#clearMaterials()
	 */
	@Override
	protected void clearMaterials() {
		// Nothing to do yet.
	}

	/**
	 * Updates the player location, coordinates in the HUD, and syncs all
	 * <code>AbstractMeshController</code>s in the {@link #updateQueue}.
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#update(float)
	 */
	@Override
	public void update(float tpf) {

		/* ---- Process the player's movement. ---- */
		// Recall that the player is facing north, in the negative direction
		// along the z axis. Compute the player's walkDirection vector based on
		// the movement flags and set it. If opposite flags are set, one negates
		// the other.
		walkDirection.set(0f, 0f, 0f);
		Vector3f playerLocation = player.getPhysicsLocation();
		boolean playerMoved = false;

		float moveFactor = moveSpeed * tpf;

		// Process left-right movement.
		if (left != right) {
			if (left && playerLocation.x > minX) {
				walkDirection.x = -moveFactor;
				playerMoved = true;
			} else if (right && playerLocation.x < maxX) {
				walkDirection.x = moveFactor;
				playerMoved = true;
			}
		}
		// Process up-down movement.
		if (up != down) {
			if (up && playerLocation.y < maxY) {
				walkDirection.y = moveFactor;
				playerMoved = true;
			} else if (down && playerLocation.y > minY) {
				walkDirection.y = -moveFactor;
				playerMoved = true;
			}
		}
		player.setWalkDirection(walkDirection);
		/* ---------------------------------------- */

		/* ---- Update the HUD with current player and cursor location. ---- */
		EmbeddedView view = getEmbeddedView();
		// Make sure the view and HUD have been created before updating the HUD.
		if (view != null && view.getHUD() != null) {
			// If necessary, update the crosshair's/player's coordinates in the
			// HUD.
			if (playerMoved) {
				playerLocation.addLocal(walkDirection);
				String text = String.format(HUDCoordinateFormat,
						playerLocation.x, playerLocation.y);
				HUDPlayerLocation.setText(text);
			}

			// Update the cursor's coordinates in the HUD. The cursor's position
			// can change by the cursor's movement, the player's movement, or by
			// zooming. It's simpler just to always update its location.
			CollisionResults results = getCollision(grid,
					getCursorRay(view.getCamera()));

			// Update the cursor's location in the HUD if possible.
			if (results.size() > 0) {
				// Get the closest valid point.
				Vector3f loc = getClosestGridPoint(results
						.getClosestCollision().getContactPoint());
				String text = String.format(HUDCoordinateFormat, loc.x, loc.y);
				// Update the cursor's location text in the HUD.
				HUDCursorLocation.setText(text);
			}
		}
		/* ----------------------------------------------------------------- */

		/* ---- Update all stale AbstractMeshViews. ---- */
		// Stale views' controllers will be in the concurrent queue. Call
		// syncView on the controllers as you pull them off.
		AbstractMeshController controller;
		while ((controller = updateQueue.poll()) != null)
			controller.syncView();
		/* --------------------------------------------- */

		return;
	}

	/**
	 * Compares the mesh with what is currently displayed. It looks for new,
	 * stale, or deleted polygons and updates the controllers/views accordingly.
	 * 
	 * @param component
	 *            The mesh that was updated.
	 */
	protected void updateMesh(VizMeshComponent mesh) {

		logger.info("MeshApplication message: Updating the mesh.");

		// This method should listen for new or deleted polygons.
		// If a polygon is new, then appropriate controllers should be created
		// for new edges and vertices. Updates to these vertices and edges are
		// handled by the controllers.
		// If a polygon was deleted, then controllers for any vertices and edges
		// no longer in the mesh should be disposed.

		// Note: We can listen to vertices. We can take advantage of
		// IVertexListener to directly apply updates to individual vertices, but
		// we need to add/remove polygons from the scene as they come and go.

		int id;

		// Get a list of new polygons, and build a list of polygon IDs
		// currently available in the mesh.
		ArrayList<Polygon> newPolygons = new ArrayList<Polygon>();
		TreeMap<Integer, Polygon> curPolygons = new TreeMap<Integer, Polygon>();
		for (Polygon polygon : mesh.getPolygons()) {
			id = polygon.getId();

			curPolygons.put(id, polygon);
			Polygon oldPolygon = polygons.get(id);
			if (oldPolygon == null) {
				// logger.info("The polygon with ID " + id + " is new.");
				newPolygons.add(polygon);
			} else if (oldPolygon != polygon) {
				// logger.info("The polygon with ID " + id
				// + " is stale and must be replaced.");

				// Remove any expired edges.
				for (Edge edge : oldPolygon.getEdges()) {
					Edge curEdge = mesh.getEdge(edge.getId());
					if (edge != curEdge) {
						EdgeController c = edgeControllers.remove(edge.getId());
						if (c != null) {
							c.dispose();
						}
					}
				}
				// Remove any expired vertices.
				for (VertexController vertex : oldPolygon.getVertices()) {
					VertexController curVertex = mesh.getVertex(vertex.getId());
					if (vertex != curVertex) {
						VertexController c = vertexControllers.remove(vertex
								.getId());
						if (c != null) {
							c.dispose();
						}
					}
				}
				// Finally, remove the stale polygon from the map.
				polygons.remove(id);

				// We need to add the newer version of the polygon, too!
				newPolygons.add(polygon);
			} else {
				// logger.info("The polygon with ID " + id
				// + " already exists in the mesh.");
			}
		}

		// Remove any old polygons.
		Iterator<Entry<Integer, Polygon>> iterator;
		for (iterator = polygons.entrySet().iterator(); iterator.hasNext();) {
			Entry<Integer, Polygon> entry = iterator.next();
			id = entry.getKey();

			if (!curPolygons.containsKey(id)) {
				// Remove the polygon with id i.

				// logger.info("The polygon with ID " + id
				// + " no longer exists in the mesh.");

				// Remove any expired edges.
				Polygon polygon = entry.getValue();
				for (Edge edge : polygon.getEdges()) {
					Edge curEdge = mesh.getEdge(edge.getId());
					if (edge != curEdge) {
						EdgeController c = edgeControllers.remove(edge.getId());
						if (c != null) {
							c.dispose();
						}
					}
				}
				// Remove any expired vertices.
				for (VertexController vertex : polygon.getVertices()) {
					VertexController curVertex = mesh.getVertex(vertex.getId());
					if (vertex != curVertex) {
						VertexController c = vertexControllers.remove(vertex
								.getId());
						if (c != null) {
							c.dispose();
						}
					}
				}
				// Remove the ID from the list of currently-displayed IDs.
				iterator.remove();
			}
		}

		float vertexSize = getVertexSize();
		float edgeSize = getEdgeSize();
		float scale = getScale();

		// Add all the new polygons.
		for (Polygon polygon : newPolygons) {
			// Add all of the polygon's new vertices.
			for (VertexController vertex : polygon.getVertices()) {
				// If the vertex is new, create a new VertexController.
				if (!vertexControllers.containsKey(vertex.getId())) {
					VertexController c = new VertexController(vertex,
							updateQueue, createBasicMaterial(ColorRGBA.Red));
					vertexControllers.put(vertex.getId(), c);
					c.setParentNode(vertexRoot);
					c.setSize(vertexSize);
					c.setScale(scale);
				}
			}
			// Add all of the polygon's new edges.
			for (Edge edge : polygon.getEdges()) {
				// If the edge is new, create a new EdgeController.
				if (!edgeControllers.containsKey(edge.getId())) {
					EdgeController c = new EdgeController(edge, updateQueue,
							createBasicMaterial(ColorRGBA.Red));
					edgeControllers.put(edge.getId(), c);
					c.setParentNode(edgeRoot);
					c.setSize(edgeSize);
					c.setScale(scale);
				}
			}

			// logger.info("The polygon with ID " + polygon.getId()
			// + " is new in the mesh.");

			// Add the polygon's ID to our set of polygon IDs.
			polygons.put(polygon.getId(), polygon);
		}

		logger.info("MeshApplication message: " + "Finished updating the mesh.");

		return;
	}

	// ---- Implements IMeshSelectionListener ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.mesh.IMeshSelectionListener#selectionChanged
	 * ()
	 */
	@Override
	public void selectionChanged() {
		// This method is synchronized so that updates to selectedVertices and
		// selectedEdges are done in order. We could implement a lock here, but
		// this is the only method that needs to modify (or even read)
		// selectedVertices and selectedEdges.

		// ---- Update the states for any affected vertices. ---- //
		// Create a HashSet from the new list of currently-selected vertex IDs.
		Set<Integer> newIds = new HashSet<Integer>(
				selectionManager.getSelectedVertexIds());

		// Loop over the old list of selected vertices and deselect any that are
		// no longer selected. Also remove any already-selected vertices from
		// the new HashSet, because they will not need to be added.
		Iterator<Integer> iter;
		for (iter = selectedVertices.iterator(); iter.hasNext();) {
			int id = iter.next();
			if (!newIds.contains(id)) {
				iter.remove();
				VertexController controller = vertexControllers.get(id);
				if (controller != null) {
					controller.setState(StateType.None);
				}
			} else {
				newIds.remove(id);
			}
		}

		// For all IDs still in the HashSet of newly-selected vertices, we need
		// to update their controllers to reflect their new state.
		for (int id : newIds) {
			selectedVertices.add(id);
			VertexController controller = vertexControllers.get(id);
			if (controller != null) {
				controller.setState(StateType.Selected);
			}
		}
		// ------------------------------------------------------ //

		// ---- Update the states for any affected edges. ---- //
		// Refresh the HashSet with the new list of currently-selected edge IDs.
		newIds.clear();
		newIds.addAll(selectionManager.getSelectedEdgeIds());

		// Loop over the old list of selected edges and deselect any that are no
		// longer selected. Also remove any already-selected edges from the new
		// HashSet, because they will not need to be added.
		for (iter = selectedEdges.iterator(); iter.hasNext();) {
			int id = iter.next();
			if (!newIds.contains(id)) {
				iter.remove();
				EdgeController controller = edgeControllers.get(id);
				if (controller != null) {
					controller.setState(StateType.None);
				}
			} else {
				newIds.remove(id);
			}
		}

		// For all IDs still in the HashSet of newly-selected edges, we need
		// to update their controllers to reflect their new state.
		for (int id : newIds) {
			selectedEdges.add(id);
			EdgeController controller = edgeControllers.get(id);
			if (controller != null) {
				controller.setState(StateType.Selected);
			}
		}
		// --------------------------------------------------- //

		return;
	}

	// ------------------------------------------- //

	// ---- Getters and Setters ---- //
	/**
	 * Gets the current <code>MeshComponent</code> displayed in the
	 * <code>MeshAppState</code>.
	 * 
	 * @return The current mesh.
	 */
	protected VizMeshComponent getMesh() {
		return mesh;
	}

	/**
	 * Sets the <code>MeshComponent</code> that will be rendered in the jME3
	 * view.
	 * 
	 * @param mesh
	 *            The mesh to render.
	 */
	public void setMesh(VizMeshComponent mesh) {

		if (mesh != null && mesh != this.mesh) {
			this.mesh = mesh;
			meshUpdateHandler.setMesh(mesh);
			selectionManager.setMesh(mesh);
		}

		return;
	}

	/**
	 * Sets the current {@link MeshAppStateMode} (or how the view is
	 * manipulated) for the <code>MeshAppState</code>.
	 * 
	 * @param mode
	 *            The new mode.
	 */
	public void setMode(MeshAppStateMode mode) {

		if (mode != null && mode != this.mode) {

			// If possible, disable the current mode. We don't want to stop it
			// yet because it may be re-used later. Re-initializing AppStates is
			// not standard procedure for jME.
			if (this.mode != null) {
				this.mode.setEnabled(false);
			}

			// Set the new mode and enable it.
			this.mode = mode;
			mode.setEnabled(true);
		}

		return;
	}

	/**
	 * Gets the MeshApplication's manager for mesh selections.
	 * 
	 * @return The MeshApplication's {@link MeshSelectionManager}.
	 */
	public MeshSelectionManager getSelectionManager() {
		return selectionManager;
	}

	/**
	 * Gets the <code>MeshAppState</code>'s factory used to select from
	 * available {@link MeshAppStateMode}s.
	 * 
	 * @return A <code>MeshAppStateModeFactory</code>.
	 */
	public MeshAppStateModeFactory getModeFactory() {
		return modeFactory;
	}

	/**
	 * Gets the grid used for determining the location of a mouse click or
	 * vertex. This can be used as the <code>Collidable</code> in
	 * {@link #getCollision(Collidable, Ray)}.
	 * 
	 * @return A <code>Geometry</code> for the <code>MeshAppState</code>'s grid.
	 */
	protected Geometry getGrid() {
		return grid;
	}

	/**
	 * Gets the <code>Node</code> that the <code>MeshAppState</code> uses for
	 * displaying temporary spatials in the scene. This <code>Node</code> will
	 * always be visible.
	 * 
	 * @return A <code>Node</code> for temporary spatials.
	 */
	protected Node getTemporarySpatialNode() {
		return tempRoot;
	}

	/**
	 * Gets the <code>Node</code> that contains all vertex spatials in the
	 * scene. The <code>Node</code> can be used as the Collidable in
	 * {@link #getCollision(Collidable, Ray)}.
	 * 
	 * @return A <code>Node</code> for the vertex spatials.
	 */
	protected Node getVertexSpatials() {
		return vertexRoot;
	}

	/**
	 * Gets the thread-safe queue used to update vertex and edge controllers in
	 * the <code>MeshAppState</code>. Controllers put themselves in this queue
	 * if their state changes (different selection state, location moved, etc.).
	 * 
	 * @return The {@link ConcurrentLinkedQueue} of
	 *         {@link AbstractMeshController}s used by the
	 *         <code>MeshAppState</code>.
	 */
	protected ConcurrentLinkedQueue<AbstractMeshController> getUpdateQueue() {
		return updateQueue;
	}

	// ----------------------------- //

	// ---- Overridden methods for IEmbeddedViewClient ---- //
	/**
	 * Creates a custom HUD with a crosshair, player coordinates, and cursor
	 * coordinates.
	 */
	@Override
	public Node createHUD(EmbeddedView view) {
		Node HUD = super.createHUD(view);

		BitmapText label;

		// Initialize the default HUD font.
		BitmapFont guiFont = getMasterApplication().getGuiFont();

		/* ---- Set up crosshairs. ---- */
		// Set up the crosshairs (a plus sign centered on the screen).
		HUDCrosshair = new BitmapText(guiFont);
		HUDCrosshair.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		HUDCrosshair.setText("+"); // crosshairs
		guiNode.attachChild(HUDCrosshair);
		/* ---------------------------- */

		/* ---- Set up the crosshair's position details. ---- */
		// Create a label for the coordinates of the crosshair's position.
		label = new BitmapText(guiFont);
		label.setSize(guiFont.getCharSet().getRenderedSize());
		label.setColor(ColorRGBA.White);
		label.setText("Crosshair position (x,y): ");
		label.setLocalTranslation(0, label.getLineHeight(), 0);
		guiNode.attachChild(label);

		// Attach the crosshair coordinates to the HUD. Update later with
		// centerText.setText().
		HUDPlayerLocation = new BitmapText(guiFont);
		HUDPlayerLocation.setSize(guiFont.getCharSet().getRenderedSize());
		HUDPlayerLocation.setColor(ColorRGBA.White);
		HUDPlayerLocation.setText(String.format(HUDCoordinateFormat, 0f, 0f));
		HUDPlayerLocation.setLocalTranslation(label.getLineWidth(),
				label.getHeight(), 0);
		guiNode.attachChild(HUDPlayerLocation);
		/* -------------------------------------------------- */

		/* ---- Set up the cursor's position details. ---- */
		// Create a label for the coordinates of the cursor's position.
		label = new BitmapText(guiFont);
		label.setSize(guiFont.getCharSet().getRenderedSize());
		label.setColor(ColorRGBA.White);
		label.setText("Cursor position (x,y): ");
		label.setLocalTranslation(0, label.getLineHeight() * 2, 0);
		guiNode.attachChild(label);

		// Attach the cursor coordinates to the HUD. Update later with
		// cursorText.setText().
		HUDCursorLocation = new BitmapText(guiFont);
		HUDCursorLocation.setSize(guiFont.getCharSet().getRenderedSize());
		HUDCursorLocation.setColor(ColorRGBA.White);
		HUDCursorLocation.setText(String.format(HUDCoordinateFormat, 0f, 0f));
		HUDCursorLocation.setLocalTranslation(label.getLineWidth(),
				label.getLocalTranslation().y, 0);
		guiNode.attachChild(HUDCursorLocation);
		/* ----------------------------------------------- */

		return HUD;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.ViewAppState#updateHUD(org.eclipse
	 * .ice.client.widgets.jme.EmbeddedView, int, int)
	 */
	@Override
	public void updateHUD(EmbeddedView view, int width, int height) {

		if (view != null && view == getEmbeddedView()) {
			BitmapFont guiFont = getMasterApplication().getGuiFont();

			// Update the crosshair's position.
			HUDCrosshair.setLocalTranslation((width * 3f - guiFont.getCharSet()
					.getRenderedSize() * 4f) / 6f, (height + HUDCrosshair
					.getLineHeight()) / 2f, 0f);
		}

		return;
	}

	/**
	 * Creates a {@link CustomChaseCamera} that follows the {@link #player}
	 * around the {@link #grid}.
	 */
	@Override
	public Object createViewCamera(EmbeddedView view) {
		Object cam = null;

		if (view != null && view == getEmbeddedView()) {
			// Get the node containing the player.
			Node node = (Node) rootNode.getChild("playerNode");
			Camera camera = view.getCamera();

			// For the ChaseCamera, we need to pass the player's Node to the
			// constructor.
			chaseCam = new CustomChaseCamera(camera, node,
					getMasterApplication().getInputManager());
			// Disable smooth motion is self-explanatory. Trailing causes the
			// camera to slowly get behind the player. This has no effect
			// because we are in a top-down view (and because smooth motion is
			// disabled), but we disable it anyway.
			chaseCam.setSmoothMotion(false);
			chaseCam.setTrailingEnabled(false);
			// This sets the camera so that "up the screen" from its perspective
			// is the negated z-unit vector.
			chaseCam.setUpVector(Vector3f.UNIT_Y);
			// This rotates the camera to look down from above the player's
			// node.
			chaseCam.setDefaultVerticalRotation(0f);
			chaseCam.setDefaultHorizontalRotation(FastMath.HALF_PI);
			// Disable rotation, which normally uses the left/right mouse
			// buttons.
			chaseCam.setToggleRotationTrigger(new Trigger[0]);
			// Set the zoom to be controlled via the mouse wheel and page
			// up/down.
			chaseCam.setZoomInTrigger(new MouseAxisTrigger(
					MouseInput.AXIS_WHEEL, false), new KeyTrigger(
					KeyInput.KEY_PGUP));
			chaseCam.setZoomOutTrigger(new MouseAxisTrigger(
					MouseInput.AXIS_WHEEL, true), new KeyTrigger(
					KeyInput.KEY_PGDN));

			// Add a listener that sets the atomic boolean signifying that the
			// camera has zoomed and the atomic integer that represents the
			// current zoom when the chaseCam zooms.
			chaseCam.addCameraListener(new ICameraListener() {
				@Override
				public void zoomChanged(float distance) {
					boolean updateVertices = false;
					boolean updateEdges = false;
					float vSize = 0f;
					float eSize = 0f;

					// Update the zoomChanged and zoomDistance variables.
					zoomLock.writeLock().lock();
					try {
						zoomDistance = distance;

						// Update the vertex size and mark the boolean if the
						// size
						// has changed.
						vSize = vertexSize;
						vertexSize = (distance < 10f ? 0.0159f * distance + 0.0413f
								: 0.2f);
						if (Math.abs(vSize - vertexSize) > 0.001f) {
							updateVertices = true;
							vSize = vertexSize;
						}

						// Update the edge size and mark the boolean if the size
						// has
						// changed.
						eSize = edgeSize;
						edgeSize = (distance < 10f ? 0.1f * distance + 4f : 5f);
						if (Math.abs(eSize - edgeSize) > 0.001f) {
							updateEdges = true;
							eSize = edgeSize;
						}
					} finally {
						zoomLock.writeLock().unlock();
					}

					// If necessary, pass the new vertex size to the vertex
					// controllers.
					if (updateVertices) {
						for (VertexController c : vertexControllers.values()) {
							c.setSize(vSize);
						}
					}

					// If necessary, pass the new edge size to the edge
					// controllers.
					if (updateEdges) {
						for (EdgeController c : edgeControllers.values()) {
							c.setSize(eSize);
						}
					}

					return;
				}
			});
			// At any value less than this, jME3 seems to cull things (generally
			// everything).
			chaseCam.setMinDistance(0.55f);

			cam = chaseCam;
		}

		return cam;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.ViewAppState#updateViewCamera(org.
	 * eclipse.ice.client.widgets.jme.EmbeddedView, boolean)
	 */
	@Override
	public void updateViewCamera(EmbeddedView view, boolean enabled) {
		if (view != null && view == getEmbeddedView()) {
			// We need to cast it to a CustomChaseCamera.
			CustomChaseCamera chaseCam = (CustomChaseCamera) view
					.getViewCamera();

			// Only proceed if the camera's state is changing.
			if (enabled != chaseCam.isEnabled()) {
				MasterApplication app = getMasterApplication();

				chaseCam.setEnabled(enabled);
				// We must register or unregister the camera's controls.
				if (enabled) {
					chaseCam.registerWithInput(app.getInputManager());
				} else {
					chaseCam.unregisterInput();
				}
				// Force the cursor to be visible when the cam is enabled.
				app.getInputManager().setCursorVisible(true);
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.jme.ViewAppState#disposeViewCamera(org
	 * .eclipse.ice.client.widgets.jme.EmbeddedView)
	 */
	@Override
	public void disposeViewCamera(EmbeddedView view) {

		if (view != null && view == getEmbeddedView()) {
			// Currently, the only thing we can do is to set the camera to null.
			this.chaseCam = null;
		}

		return;
	}

	// ---------------------------------------------------- //

	/**
	 * Gets a valid point on the grid closest to a particular vector.
	 * 
	 * @param point
	 *            The point for which we need the nearest grid point.
	 * @return A new vector for the point on the grid closest to the specified
	 *         vector.
	 */
	protected Vector3f getClosestGridPoint(Vector3f point) {
		Vector3f newPoint = new Vector3f(FastMath.clamp(point.x, minX, maxX),
				FastMath.clamp(point.y, minY, maxY), 0f);
		return newPoint.multLocal(getScale());
	}

	// ---- Camera/Zoom methods. ---- //
	/**
	 * Gets the chase camera used to follow the player around the scene. It is
	 * intialized and configured to have a top-down view in
	 * {@link #createViewCamera(VizEmbeddedView)}.
	 * 
	 * @return The application's ChaseCamera.
	 */
	public CustomChaseCamera getChaseCamera() {
		return chaseCam;
	}

	/**
	 * Gets the current distance of {@link #chaseCam} from the target (
	 * {@link #player}).
	 * 
	 * @return A float value representing the camera's current zoom.
	 */
	public float getZoomDistance() {
		float size = 10f;
		zoomLock.readLock().lock();
		try {
			size = zoomDistance;
		} finally {
			zoomLock.readLock().unlock();
		}
		return size;
	}

	/**
	 * Sets the scale used for determining how far to spread parts of the mesh.
	 * For example, a scale of 0.25 means that 1 world unit in jME3 corresponds
	 * to 0.25 units in the mesh model.
	 * 
	 * @param scale
	 *            The new scale value.
	 */
	public void setScale(float scale) {
		if (scale >= 0.1f && scale <= 10f) {
			this.scale.set(Float.floatToIntBits(scale));

			// Update the scale for all the vertices and edges.
			for (VertexController c : vertexControllers.values()) {
				c.setScale(scale);
			}
			for (EdgeController c : edgeControllers.values()) {
				c.setScale(scale);
			}
		}
		return;
	}

	/**
	 * Gets the scale used for determining how far to spread parts of the mesh.
	 * For example, a scale of 0.25 means that 1 world unit in jME3 corresponds
	 * to 0.25 units in the mesh model.
	 * 
	 * @return The scale used by the application.
	 */
	public float getScale() {
		return Float.intBitsToFloat(scale.get());
	}

	/**
	 * Gets the current radius of the Spheres used to represent vertices.
	 * 
	 * @return The current size that should be used for the VertexController's
	 *         and their VertexViews.
	 */
	public float getVertexSize() {
		float size = 0.2f;
		zoomLock.readLock().lock();
		try {
			size = vertexSize;
		} finally {
			zoomLock.readLock().unlock();
		}
		return size;
	}

	/**
	 * Gets the current line width of the Lines used to represent edges.
	 * 
	 * @return The current size that should be used for the EdgeControllers and
	 *         their VertexViews.
	 */
	public float getEdgeSize() {
		float size = 5f;
		zoomLock.readLock().lock();
		try {
			size = edgeSize;
		} finally {
			zoomLock.readLock().unlock();
		}
		return size;
	}

	// ------------------------------ //

	/**
	 * Updates {@link #moveSpeed} depending on whether shift or control are
	 * pressed.
	 */
	private void updateMoveSpeed() {

		if (shift == control) {
			moveSpeed = 1f;
		} else if (shift) {
			moveSpeed = 4f;
		} else if (control) {
			moveSpeed = 0.25f;
		}

		return;
	}
}
