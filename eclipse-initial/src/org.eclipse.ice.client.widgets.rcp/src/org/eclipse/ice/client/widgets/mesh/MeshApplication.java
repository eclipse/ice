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

import org.eclipse.ice.client.widgets.mesh.MeshApplicationMode.Key;
import org.eclipse.ice.datastructures.form.mesh.Edge;
import org.eclipse.ice.datastructures.form.mesh.MeshComponent;
import org.eclipse.ice.datastructures.form.mesh.Polygon;
import org.eclipse.ice.datastructures.form.mesh.Vertex;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.jme3.app.state.AppState;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.BoxCollisionShape;
import com.jme3.bullet.control.CharacterControl;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
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
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.VertexBuffer.Type;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Quad;
import com.jme3.system.AppSettings;
import com.jme3.util.BufferUtils;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The MeshApplication is an extension of the jME3 SimpleApplication that is
 * used to display and manipulate a fully modifiable, graphical representation
 * of a MeshComponent.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author djg
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MeshApplication extends AbstractApplication implements
		IMeshSelectionListener {

	// TODO Remove this method.
	public static void main(String[] args) {

		// FIXME - I've currently disabled the jME3 info output because it's
		// getting annoying to debug with all the red output.
		// Logger.getLogger("com.jme3").setLevel(Level.SEVERE);

		// Set the JME3 application settings (standard).
		AppSettings settings = new AppSettings(true);
		settings.setFrameRate(60);
		settings.setRenderer(AppSettings.LWJGL_OPENGL_ANY);
		settings.setResolution(1024, 768);

		MeshApplication app = new MeshApplication();
		app.getSelectionManager().addMeshApplicationListener(
				new IMeshSelectionListener() {
					public void selectionChanged() {
						System.out
								.println("MeshApplication: Selection changed!");
					}
				});
		app.setSettings(settings);
		app.setShowSettings(false);
		app.setPauseOnLostFocus(false);
		app.start();

		app.setMesh(new MeshComponent());

		return;
	}

	// ---- Application Initialization ---- //
	// ------------------------------------ //

	// ---- Application Update/Synchronization ---- //
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A concurrent queue containing all AbstractMeshControllers whose views are
	 * waiting to be updated in the simpleUpdate() method.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private final ConcurrentLinkedQueue<AbstractMeshController> updateQueue;
	/**
	 * A thread-safe queue of dimensions (width, height) for the
	 * MeshApplication's container. It is used in simpleUpdate to refresh jME3
	 * components that rely on the size of the application window, e.g., the
	 * crosshairs in the HUD.
	 */
	private final ConcurrentLinkedQueue<int[]> applicationDimensions;
	/**
	 * A handler to listen for updates from the mesh. We use a separate thread
	 * for this to isolate the update code and ensure that critical data
	 * structures are not manipulated simultaneously in multiple threads.
	 */
	private final MeshUpdateHandler meshUpdateHandler;
	// -------------------------------------------- //

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
	private static final String HUDCoordinateFormat = "%05.2f,%05.2f";
	// -------------------------------- //

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

	// ---- The Mesh and its controllers. ---- //
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The currently displayed MeshComponent.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private MeshComponent mesh;

	/**
	 * An ordered map of all the polygons currently displayed in the
	 * MeshApplication.
	 */
	private TreeMap<Integer, Polygon> polygons;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An ordered map of VertexControllers keyed on their associated Vertex's
	 * ID.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TreeMap<Integer, VertexController> vertexControllers;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * An ordered map of EdgeControllers keyed on their associated Edge's ID.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private TreeMap<Integer, EdgeController> edgeControllers;
	// --------------------------------------- //

	// ---- Current selection. ---- //
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
	// ---------------------------- //

	// ---- Control properties. ---- //
	/**
	 * The current {@link MeshApplicationMode}.
	 */
	private MeshApplicationMode mode;
	// ----------------------------- //

	// ---- Grid properties. ---- //
	/**
	 * The width (x-length) of the grid. The value should be a whole number.
	 * Changing this changes the size of each grid component.
	 */
	private static final float width = 32f;
	/**
	 * The length (y-length) of the grid. The value should be a whole number.
	 * Changing this changes the size of each grid component.
	 */
	private static final float length = 16f;

	private static final float minX = width * -0.5f;
	private static final float minY = length * -0.5f;
	private static final float maxX = width * 0.5f;
	private static final float maxY = length * 0.5f;

	/**
	 * The Geometry corresponding to the surface of the grid. Although it is
	 * invisible, it is located in the same plane as the grid lines. It is used
	 * to determine locations based on ray collisions.
	 */
	private Geometry grid;
	// -------------------------- //

	// ---- Player properties. ---- //
	/**
	 * The player that is located on the grid surface at the center of the
	 * screen beneath the crosshairs.
	 */
	private CharacterControl player;

	private boolean up, down, left, right, shift, control;
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

	// ---- Node groups. ---- //
	/**
	 * This Node contains the Geometries for all vertices.
	 */
	private Node vertexSpatials;
	/**
	 * This Node contains the Geometries for all edges.
	 */
	private Node edgeSpatials;
	/**
	 * This Node contains the Geometries for all temporary objects.
	 */
	private Node tempSpatials;

	// ---------------------- //

	/**
	 * The default constructor. This initializes any data structures that may be
	 * required by the MeshApplication.
	 */
	public MeshApplication() {
		/*- (Do not delete the - to the left of this line)
		 * 
		 * These two calls are the same as calling the regular SimpleApplication
		 * constructor:
		 * 
		 * super();
		 * super(new StatsAppState(), new FlyCamAppState(), new DebugKeysAppState());
		 *
		 * This creates 3 AppStates: one for the stats in the HUD, one for the 
		 * FlyCam, and one for debug keys. Currently, we do not take advantage 
		 * of any of these AppStates, so it's best to just avoid creating them
		 * in the first place. We can do that only with the following call:
		 * 
		 * super(new AppState[] {});
		 * 
		 * Alternatively, we could call the default constructor but disable the
		 * AppStates by using the stateManager, for example:
		 * 
		 * stateManager.detach(stateManager.getState(StatsAppState.class));
		 *       
		 */
		super(new AppState[] {});

		// Initialize the current selection collections.
		selectedVertices = new HashSet<Integer>();
		selectedEdges = new HashSet<Integer>();
		selectionManager = new MeshSelectionManager();
		selectionManager.addMeshApplicationListener(this);

		// Initialize the concurrent queue used to process all controllers whose
		// views must be updated.
		updateQueue = new ConcurrentLinkedQueue<AbstractMeshController>();

		// Initialize the concurrent queue for processing MeshApplication
		// resizing.
		applicationDimensions = new ConcurrentLinkedQueue<int[]>();

		// Initialize the listener that processes updates from the Mesh.
		meshUpdateHandler = new MeshUpdateHandler(this);
		meshUpdateHandler.start();

		// Initialize the maps of controllers.
		vertexControllers = new TreeMap<Integer, VertexController>();
		edgeControllers = new TreeMap<Integer, EdgeController>();

		// Initialize the map of currently displayed polygons.
		polygons = new TreeMap<Integer, Polygon>();

		// Creates a new Vector3f initialized to (0f, 0f, 0f). This vector
		// contains the player's current walk direction as the arrow or wasd
		// keys are pressed.
		walkDirection = new Vector3f();

		// Set the initial mode to a dummy mode that does nothing.
		mode = new MeshApplicationMode() {
			public void load(MeshApplication application) {
			}

			public void clear() {
			}

			protected void leftClick(boolean isPressed) {
			}

			protected void rightClick(boolean isPressed) {
			}

			public String getName() {
				return null;
			}

			public String getDescription() {
				return null;
			}
		};

		return;
	}

	@Override
	public void stop(boolean waitFor) {
		// I'm not entirely convinced that this is ever called!!!
		System.out.println("MeshApplication message: Stopping!");
		meshUpdateHandler.halt();
		System.out.println("MeshApplication message: Halt signal sent.");
		try {
			meshUpdateHandler.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("MeshApplication message: Stopped update handler.");

		super.stop(waitFor);
	}

	// ---- Initialization methods. ---- //
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes the HUD by updating the guiNode.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void initHUD() {
		// begin-user-code

		// Clear the HUD.

		BitmapText label;
		float x, y, z;

		// Initialize the default HUD font.
		guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");

		/* ---- Set up crosshairs. ---- */
		// Set up the crosshairs (a plus sign centered on the screen).
		HUDCrosshair = new BitmapText(guiFont);
		HUDCrosshair.setSize(guiFont.getCharSet().getRenderedSize() * 2);
		HUDCrosshair.setText("+"); // crosshairs
		x = settings.getWidth() / 2 - guiFont.getCharSet().getRenderedSize()
				/ 3 * 2;
		y = settings.getHeight() / 2 + HUDCrosshair.getLineHeight() / 2;
		z = 0;
		HUDCrosshair.setLocalTranslation(x, y, z);
		HUD.attachChild(HUDCrosshair);
		/* ---------------------------- */

		/* ---- Set up the crosshair's position details. ---- */
		// Create a label for the coordinates of the crosshair's position.
		label = new BitmapText(guiFont);
		label.setSize(guiFont.getCharSet().getRenderedSize());
		label.setColor(ColorRGBA.White);
		label.setText("Crosshair position (x,y): ");
		label.setLocalTranslation(0, label.getLineHeight(), 0);
		HUD.attachChild(label);

		// Attach the crosshair coordinates to the HUD. Update later with
		// centerText.setText().
		HUDPlayerLocation = new BitmapText(guiFont);
		HUDPlayerLocation.setSize(guiFont.getCharSet().getRenderedSize());
		HUDPlayerLocation.setColor(ColorRGBA.White);
		HUDPlayerLocation.setText(String.format(HUDCoordinateFormat, 0f, 0f));
		HUDPlayerLocation.setLocalTranslation(label.getLineWidth(),
				label.getHeight(), 0);
		HUD.attachChild(HUDPlayerLocation);
		/* -------------------------------------------------- */

		/* ---- Set up the cursor's position details. ---- */
		// Create a label for the coordinates of the cursor's position.
		label = new BitmapText(guiFont);
		label.setSize(guiFont.getCharSet().getRenderedSize());
		label.setColor(ColorRGBA.White);
		label.setText("Cursor position (x,y): ");
		label.setLocalTranslation(0, label.getLineHeight() * 2, 0);
		HUD.attachChild(label);

		// Attach the cursor coordinates to the HUD. Update later with
		// cursorText.setText().
		HUDCursorLocation = new BitmapText(guiFont);
		HUDCursorLocation.setSize(guiFont.getCharSet().getRenderedSize());
		HUDCursorLocation.setColor(ColorRGBA.White);
		HUDCursorLocation.setText(String.format(HUDCoordinateFormat, 0f, 0f));
		HUDCursorLocation.setLocalTranslation(label.getLineWidth(),
				label.getLocalTranslation().y, 0);
		HUD.attachChild(HUDCursorLocation);
		/* ----------------------------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes the different components of the scene, including the grid,
	 * the player, physics, etc.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void initScene() {
		// begin-user-code

		Geometry geometry;
		Node node;
		Quad quad;
		Grid grid;
		Material material;

		// Initialize the physics system for the player and the walls.
		BulletAppState physics = new BulletAppState();
		stateManager.attach(physics);

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
		Vector3f[] vertices = { new Vector3f(-d, -d, 0f),
				new Vector3f(0f, -d, 0f), new Vector3f(w + d, -d, 0f),
				new Vector3f(0f, 0f, 0f), new Vector3f(w, 0f, 0f),
				new Vector3f(w + d, 0f, 0f), new Vector3f(-d, l, 0f),
				new Vector3f(0f, l, 0f), new Vector3f(w, l, 0f),
				new Vector3f(-d, l + d, 0f), new Vector3f(w, l + d, 0f),
				new Vector3f(w + d, l + d, 0f) };
		for (Vector3f vertex : vertices) {
			vertex.addLocal(minX, minY, 0f);
		}
		int[] indices = { 0, 7, 6, 0, 1, 7, 1, 2, 3, 2, 5, 3, 4, 5, 11, 4, 11,
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

		/* ---- Create the Nodes to contain views. ---- */
		vertexSpatials = new Node("vertexSpatials");
		rootNode.attachChild(vertexSpatials);

		edgeSpatials = new Node("edgeSpatials");
		rootNode.attachChild(edgeSpatials);

		tempSpatials = new Node("tempSpatials");
		rootNode.attachChild(tempSpatials);
		/* -------------------------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes a chase camera that follows the player. The camera should be
	 * locked to a top-down view.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void initCamera() {
		// begin-user-code

		// Set up the FlyByCam for debugging.

		// Note: We do not need to disable the FlyByCam
		// since its AppState is disabled in the constructor.

		// Get the node containing the player.
		Node node = (Node) rootNode.getChild("playerNode");

		// For the ChaseCamera, we need to pass the player's Node to the
		// constructor.
		chaseCam = new CustomChaseCamera(cam, node, inputManager);
		// Disable smooth motion is self-explanatory. Trailing causes the camera
		// to slowly get behind the player. This has no effect because we are in
		// a top-down view (and because smooth motion is disabled), but we
		// disable it anyway.
		chaseCam.setSmoothMotion(false);
		chaseCam.setTrailingEnabled(false);
		// This sets the camera so that "up the screen" from its perspective is
		// the negated z-unit vector.
		chaseCam.setUpVector(Vector3f.UNIT_Y);
		// This rotates the camera to look down from above the player's node.
		chaseCam.setDefaultVerticalRotation(0f);
		chaseCam.setDefaultHorizontalRotation(FastMath.HALF_PI);
		// Disable rotation, which normally uses the left/right mouse buttons.
		chaseCam.setToggleRotationTrigger(new Trigger[0]);
		// Set the zoom to be controlled via the mouse wheel and page up/down.
		chaseCam.setZoomInTrigger(new MouseAxisTrigger(MouseInput.AXIS_WHEEL,
				false), new KeyTrigger(KeyInput.KEY_PGUP));
		chaseCam.setZoomOutTrigger(new MouseAxisTrigger(MouseInput.AXIS_WHEEL,
				true), new KeyTrigger(KeyInput.KEY_PGDN));

		// Add a listener that sets the atomic boolean signifying that the
		// camera has zoomed and the atomic integer that represents the current
		// zoom when the chaseCam zooms.
		chaseCam.addCameraListener(new ICameraListener() {
			public void zoomChanged(float distance) {
				boolean updateVertices = false;
				boolean updateEdges = false;
				float vSize = 0f;
				float eSize = 0f;

				// Update the zoomChanged and zoomDistance variables.
				zoomLock.writeLock().lock();
				try {
					zoomDistance = distance;

					// Update the vertex size and mark the boolean if the size
					// has changed.
					vSize = vertexSize;
					vertexSize = (distance < 10f ? 0.0159f * distance + 0.0413f
							: 0.2f);
					if (vSize != vertexSize) {
						updateVertices = true;
						vSize = vertexSize;
					}

					// Update the edge size and mark the boolean if the size has
					// changed.
					eSize = edgeSize;
					edgeSize = (distance < 10f ? 0.1f * distance + 4f : 5f);
					if (eSize != edgeSize) {
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

				// If necessary, pass the new edge size to the edge controllers.
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

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes listeners and maps triggers to them for key presses, mouse
	 * clicks, and mouse movement.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void initControls() {
		// begin-user-code

		// Set up a listener for keyboard key-press events.
		ActionListener movementListener = new ActionListener() {
			public void onAction(String keyBinding, boolean isPressed, float tpf) {
				if ("Left".equals(keyBinding)) {
					left = isPressed;
				} else if ("Right".equals(keyBinding)) {
					right = isPressed;
				} else if ("Up".equals(keyBinding)) {
					up = isPressed;
				} else if ("Down".equals(keyBinding)) {
					down = isPressed;
				}
				return;
			}
		};

		// Setup movement triggers (keyboard).
		inputManager.addMapping("Left", new KeyTrigger(KeyInput.KEY_A),
				new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("Right", new KeyTrigger(KeyInput.KEY_D),
				new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("Up", new KeyTrigger(KeyInput.KEY_W),
				new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("Down", new KeyTrigger(KeyInput.KEY_S),
				new KeyTrigger(KeyInput.KEY_DOWN));
		// Set up listeners for all of the above
		inputManager.addListener(movementListener, "Left");
		inputManager.addListener(movementListener, "Right");
		inputManager.addListener(movementListener, "Up");
		inputManager.addListener(movementListener, "Down");

		// // FIXME - Remove this. Temporary change.
		// ActionListener changeModeListener = new ActionListener() {
		// private MeshApplicationModeFactory factory = new
		// MeshApplicationModeFactory();
		// private int i = 0;
		//
		// @Override
		// public void onAction(String keyBinding, boolean isPressed, float tpf)
		// {
		// if (!isPressed) {
		// List<Mode> modes = factory.getAvailableModes();
		// setMode(factory.getMode(modes.get(i)));
		// i = (i + 1) % modes.size();
		// }
		// }
		// };
		// inputManager.addMapping("ModeChanged", new KeyTrigger(
		// KeyInput.KEY_SPACE));
		// inputManager.addListener(changeModeListener, "ModeChanged");
		// // FIXME - End of temporary section.

		// Set up the shift and control keys to speed and slow movement,
		// respectively. We need to add listeners to modify the class variable
		// moveSpeed appropriately.
		inputManager.addListener(new ActionListener() {
			public void onAction(String name, boolean isPressed, float tpf) {
				shift = isPressed;
				updateMoveSpeed();
			}
		}, Key.Shift.name);
		inputManager.addListener(new ActionListener() {
			public void onAction(String name, boolean isPressed, float tpf) {
				control = isPressed;
				updateMoveSpeed();
			}
		}, Key.Control.name);

		// Add keys that support custom actions in MeshApplicationModes.
		for (Key key : Key.values()) {
			inputManager.addMapping(key.name, key.triggers);
		}

		return;
		// end-user-code
	}

	/**
	 * Overrides the default axes to move them up on the z axis so they can be
	 * seen on top of the wireframe grid.
	 */
	@Override
	protected void initAxes() {
		super.initAxes();

		Vector3f moveVector = new Vector3f(0f, 0f, 0.001f);
		axes.getChild("x").move(moveVector);
		axes.getChild("y").move(moveVector);
		axes.getChild("z").move(moveVector);
	}

	// --------------------------------- //

	// ---- Update methods. ---- //
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the method from the jME3 SimpleApplication to update the jME3
	 * scene when necessary, e.g., when the model has been updated or when the
	 * user moves the camera. This is called automatically.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param tpf
	 *            <p>
	 *            Time per frame. Can be used to limit movement to make it
	 *            consistent across machines.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Override
	public void simpleUpdate(float tpf) {
		// begin-user-code
		super.simpleUpdate(tpf);

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
		// If necessary, update the crosshair's/player's coordinates in the
		// HUD.
		if (playerMoved) {
			playerLocation.addLocal(walkDirection);
			HUDPlayerLocation.setText(String.format(HUDCoordinateFormat,
					playerLocation.x, playerLocation.y));
		}

		// Update the cursor's coordinates in the HUD. The cursor's position
		// can change by the cursor's movement, the player's movement, or by
		// zooming. It's simpler just to always update its location.
		CollisionResults results = getCollision(grid, getCursorRay());

		// Update the cursor's location in the HUD if possible.
		if (results.size() > 0) {
			// Get the closest valid point.
			Vector3f loc = getClosestGridPoint(results.getClosestCollision()
					.getContactPoint());

			// Update the cursor's location text in the HUD.
			HUDCursorLocation.setText(String.format(HUDCoordinateFormat, loc.x,
					loc.y));
		}
		/* ----------------------------------------------------------------- */

		/* ---- Update all stale AbstractMeshViews. ---- */
		// Stale views' controllers will be in the concurrent queue. Call
		// syncView on the controllers as you pull them off.
		AbstractMeshController controller;
		while ((controller = updateQueue.poll()) != null) {
			controller.syncView();
		}
		/* --------------------------------------------- */

		/* ---- Refresh any components reliant on app size. ---- */
		// This trashes all but the most recent update to the application's
		// dimensions. If lastDimension is not null after the loop, then it is
		// the most recent update to the application's size.
		int[] tmp, lastDimension = null;
		while ((tmp = applicationDimensions.poll()) != null) {
			lastDimension = tmp;
		}
		if (lastDimension != null) {
			// Update the crosshair's position.
			HUDCrosshair.setLocalTranslation((lastDimension[0] * 3f - guiFont
					.getCharSet().getRenderedSize() * 4f) / 6f,
					(lastDimension[1] + HUDCrosshair.getLineHeight()) / 2f, 0f);
			// Update anything else that needs to be refreshed.
		}
		/* ----------------------------------------------------- */

		// Perform the current mode's simple update process.
		mode.simpleUpdate(tpf);

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Compares the mesh with what is currently displayed. It looks for new,
	 * stale, or deleted polygons and updates the controllers/views accordingly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param component
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected void update(MeshComponent mesh) {
		// begin-user-code

		System.out.println("MeshApplication message: Updating the mesh.");

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
				newPolygons.add(polygon);
			} else if (oldPolygon != polygon) {

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
				for (Vertex vertex : oldPolygon.getVertices()) {
					Vertex curVertex = mesh.getVertex(vertex.getId());
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
				// System.out.println("The polygon with ID " + id
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
				for (Vertex vertex : polygon.getVertices()) {
					Vertex curVertex = mesh.getVertex(vertex.getId());
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
			for (Vertex vertex : polygon.getVertices()) {
				// If the vertex is new, create a new VertexController.
				if (!vertexControllers.containsKey(vertex.getId())) {
					VertexController c = new VertexController(vertex,
							updateQueue, createBasicMaterial(ColorRGBA.Red));
					vertexControllers.put(vertex.getId(), c);
					c.setParentNode(vertexSpatials);
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
					c.setParentNode(edgeSpatials);
					c.setSize(edgeSize);
					c.setScale(scale);
				}
			}

			// Add the polygon's ID to our set of polygon IDs.
			polygons.put(polygon.getId(), polygon);
		}

		System.out
				.println("MeshApplication message: Finished updating the mesh.");

		return;
		// end-user-code
	}

	/**
	 * Pushes an update to the MeshApplication's container's dimensions (width
	 * and height) to a thread-safe Queue. The last update to the dimensions
	 * will be the one used.
	 * 
	 * @param width
	 *            The new width of the MeshApplication's container.
	 * @param height
	 *            The new height of the MeshApplication's container.
	 */
	public void updateApplicationDimensions(int width, int height) {
		// begin-user-code
		if (width > 0 || height > 0) {
			applicationDimensions.add(new int[] { width, height });
		}
		return;
		// end-user-code
	}

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

	// ------------------------- //

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the MeshComponent that will be rendered in the jME3 application.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param mesh
	 *            <p>
	 *            The mesh to render.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setMesh(MeshComponent mesh) {
		// begin-user-code

		// / Check the parameter.
		if (mesh != null && mesh != this.mesh) {
			this.mesh = mesh;
			meshUpdateHandler.setMesh(mesh);
			selectionManager.setMesh(mesh);
		}

		return;
		// end-user-code
	}

	/**
	 * Sets the current {@link MeshApplicationMode} for the MeshApplication.
	 * 
	 * @param name
	 *            The name of the new mode. It will be looked up in the
	 *            application's {@link #modeFactory}.
	 */
	public void setMode(MeshApplicationMode newMode) {
		if (newMode != null && newMode != mode) {

			// Clear the current mode and remove all its listeners.
			mode.clear();
			for (InputListener listener : mode.getInputListeners()) {
				inputManager.removeListener(listener);
			}

			// Load the new mode and add all its listeners.
			mode = newMode;
			mode.load(this);
			for (Entry<Key, InputListener> e : mode.getInputListenerMappings()
					.entrySet()) {
				inputManager.addListener(e.getValue(), e.getKey().name);
			}
		}

		return;
	}

	// ---- Camera/Zoom methods. ---- //
	/**
	 * Gets the chase camera used to follow the player around the scene. It is
	 * intialized and configured to have a top-down view in
	 * {@link #initCamera()}.
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

	// ------------------------------ //

	// ---- Methods for rays and collisions. ---- //
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

	// ------------------------------------------ //

	// ---- Getters used primarily in the MeshApplicationModes. ---- //
	/**
	 * Gets the current MeshComponent displayed in the MeshApplication.
	 * 
	 * @return The MeshApplication's MeshComponent.
	 */
	protected MeshComponent getMesh() {
		return mesh;
	}

	/**
	 * Gets the grid used for determining the location of a mouse click or
	 * vertex. This can be used as the Collidable in
	 * {@link #getCollision(Collidable, Ray)}.
	 * 
	 * @return A Geometry object for the MeshApplication's grid.
	 */
	protected Geometry getGrid() {
		return grid;
	}

	/**
	 * Gets the Node that the MeshApplication uses for displaying temporary
	 * Spatials in the scene. This node will always be visible.
	 * 
	 * @return A jME3 Node for temporary spatials.
	 */
	protected Node getTemporarySpatialNode() {
		return tempSpatials;
	}

	/**
	 * Gets the Node that contains all vertex spatials in the scene. The Node
	 * can be used as the Collidable in {@link #getCollision(Collidable, Ray)}.
	 * 
	 * @return A jME3 Node for the vertex spatials.
	 */
	protected Node getVertexSpatials() {
		return vertexSpatials;
	}

	/**
	 * Gets the thread-safe queue used to update vertex and edge controllers in
	 * the MeshApplication. Controllers put themselves in this queue if their
	 * state changes (different selection state, location moved, etc.).
	 * 
	 * @return The {@link ConcurrentLinkedQueue} of
	 *         {@link AbstractMeshController}s used by the MeshApplication.
	 */
	protected ConcurrentLinkedQueue<AbstractMeshController> getUpdateQueue() {
		return updateQueue;
	}

	// ------------------------------------------------------------- //

	// ---- Methods for the current selection. ---- //
	/**
	 * Gets the MeshApplication's manager for mesh selections.
	 * 
	 * @return The MeshApplication's {@link MeshSelectionManager}.
	 */
	public MeshSelectionManager getSelectionManager() {
		return selectionManager;
	}

	/**
	 * This method updates the states of selected and unselected vertex and edge
	 * controllers as necessary.
	 */
	public synchronized void selectionChanged() {
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

	// -------------------------------------------- //

	/**
	 * This is a test method only. It just adds a new quad of size 1 to the
	 * MeshComponent to make sure the updates are sent back to the MeshApp.
	 * 
	 * @param topLeft
	 *            The top left corner of the new quad.
	 */
	// TODO Remove this method.
	protected void addNewQuad(Vector3f topLeft) {

		int id;
		float size = 1f;

		// ArrayLists for the Polygon constructor.
		ArrayList<Edge> edges = new ArrayList<Edge>();
		ArrayList<Vertex> vertices = new ArrayList<Vertex>();

		// Get the next vertex ID.
		id = vertexControllers.size() + 1;

		// Create four vertices and put them in the vertex list.
		Vertex v = new Vertex(topLeft.x, topLeft.y, topLeft.z);
		v.setId(id++);
		vertices.add(v);
		v = new Vertex(topLeft.x + size, topLeft.y, topLeft.z);
		v.setId(id++);
		vertices.add(v);
		v = new Vertex(topLeft.x + size, topLeft.y + size, topLeft.z);
		v.setId(id++);
		vertices.add(v);
		v = new Vertex(topLeft.x, topLeft.y + size, topLeft.z);
		v.setId(id++);
		vertices.add(v);

		// Get the next edge ID.
		id = edgeControllers.size() + 1;

		// Create the edges for these vertices.
		for (int i = 0; i < 4; i++) {
			Edge edge = new Edge(vertices.get(i), vertices.get((i + 1) % 4));
			edge.setId(id++);
			edges.add(edge);
		}

		// Create the polygon and set its ID.
		Polygon polygon = new Polygon(edges, vertices);
		polygon.setId(polygons.size() + 1);

		System.out.println("Adding polygon " + polygon.getId());

		// Add the polygon to the mesh.
		mesh.addPolygon(polygon);

		return;
	}
}