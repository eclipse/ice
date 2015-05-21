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

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Line;
import com.jme3.system.awt.AwtPanel;

/**
 * This class provides a basic version of a jME3 SimpleApplication that should
 * be extended to provide 3D views inside an ICE editor or view. It is
 * configured to use the AwtPanelsContext to render the 3D view.
 * 
 * @author Jordan H. Deyton
 * 
 */
public abstract class AbstractApplication extends SimpleApplication {

	// ---- Application Initialization ---- //
	/**
	 * A thread-safe boolean signifying that the SimpleApplication's
	 * simpleInitApp() has completely finished. This can help avoid issues where
	 * associated data is set before the application is constructed.
	 */
	private final AtomicBoolean initialized;
	/**
	 * The jME3 AwtPanel used to render the jME3 application. We use this to
	 * prevent the jME3 application from consuming all mouse and key events,
	 * even when it should not have focus (a problem first discovered in
	 * Windows).
	 */
	private AwtPanel renderPanel;
	// ------------------------------------ //

	// ---- Materials ---- //
	/**
	 * A HashMap of materials that should be shared in the application.
	 */
	protected final Map<String, Material> materials;
	// ------------------- //

	// ---- Heads Up Display (HUD) ---- //
	/**
	 * A flag to indicate whether or not the HUD should be displayed.
	 */
	private final AtomicBoolean displayHUD;
	/**
	 * This Node is attached to the application's guiNode. By default, it can be
	 * toggled by {@link #setDisplayHUD(boolean)}. Objects displayed on the HUD
	 * should be attached to this node.
	 */
	protected final Node HUD;
	// -------------------------------- //

	// ---- Camera ---- //
	// ---------------- //

	// ---- Scene ---- //
	/**
	 * A flag to indicate whether or not to show lines representing the x, y,
	 * and z axes at the origin (of jME3 space).
	 */
	private final AtomicBoolean displayAxes;
	/**
	 * This Node is attached to the scene. By default, it can be toggled by
	 * {@link #setDisplayAxes(boolean)}. The default colors of the axes are as
	 * follows:<br>
	 * <br>
	 * x - red<br>
	 * y - green<br>
	 * z - blue
	 */
	protected final Node axes;

	// --------------- //

	/**
	 * The default constructor. Initializes the application with a FlyCam. It
	 * also allows statistics to be opened with the F5 key.
	 */
	public AbstractApplication() {
		this(new StatsAppState(), new FlyCamAppState());
	}

	/**
	 * Initializes the application with the specified AppStates.
	 * 
	 * @param initialStates
	 *            The initial AppStates. Custom AppStates should extend
	 *            AbstractAppState.
	 */
	public AbstractApplication(AppState... initialStates) {
		super(initialStates);

		// Initialize all class variables here.

		// ---- Application Initialization ---- //
		initialized = new AtomicBoolean(false);
		// ------------------------------------ //

		// ---- Materials ---- //
		materials = new HashMap<String, Material>();
		// ------------------- //

		// ---- Heads Up Display (HUD) ---- //
		displayHUD = new AtomicBoolean(true);
		HUD = new Node("HUD");
		// -------------------------------- //

		// ---- Camera ---- //
		// ---------------- //

		// ---- Scene ---- //
		displayAxes = new AtomicBoolean(true);
		axes = new Node("Axes");
		// --------------- //

		return;
	}

	// ---- Application Initialization ---- //
	/**
	 * Whether or not the AbstractApplication is initialized. This can prevent
	 * unnecessary updates from occurring before the jME3 components have been
	 * declared and initialized.
	 * 
	 * @return True if the AbstractApplication has been initialized, false
	 *         otherwise.
	 */
	public final boolean isInitialized() {
		return initialized.get();
	}

	/**
	 * Sets the AwtPanel used to render the scene in the AWT Frame embedded in
	 * SWT through the SWT_AWT bridge.
	 * 
	 * @param panel
	 *            The AwtPanel that is used in lieu of an AWT Canvas.
	 */
	public final void setAwtPanel(AwtPanel panel) {
		if (panel != null) {
			renderPanel = panel;
		}
		return;
	}

	/**
	 * <p>
	 * Overrides the method from the jME3 SimpleApplication to initialize the
	 * scene, camera, etc. This is called automatically at the start of the jME3
	 * application. <b>It is recommended that this method <i>not</i> be
	 * overridden.</b>
	 * </p>
	 * 
	 */
	@Override
	public void simpleInitApp() {

		// Set up the materials used in the application. We should try to re-use
		// materials as often as we can.
		initMaterials();

		// Set up the HUD.
		// If necessary, we should initially attach the HUD to the GUI.
		if (getDisplayHUD()) {
			guiNode.attachChild(HUD);
		}
		initHUD();

		// Set up the initial scene.
		// If necessary, we should initially attach the axes to the scene.
		initAxes();
		if (getDisplayAxes()) {
			rootNode.attachChild(axes);
		}
		initScene();

		// Set up the camera.
		initCamera();

		// Set up the user's controls.
		initControls();

		// To render the scene in the AwtPanel, we have to attach the scene
		// (viewPort) and the gui/HUD (guiViewPort) to the panel. The first
		// argument is a boolean for overrideMainFramebuffer.
		if (renderPanel != null) {
			renderPanel.attachTo(true, viewPort);
			renderPanel.attachTo(true, guiViewPort);
		}

		// At this point, the jME3 side should be completely initialized.
		initialized.set(true);

		return;
	}

	/**
	 * Initializes the materials used by the application. These should be stored
	 * in {@link #materials}. The default behavior provides the materials used
	 * for the axes.
	 */
	protected void initMaterials() {
		materials.put("XAxis", createBasicMaterial(ColorRGBA.Red));
		materials.put("YAxis", createBasicMaterial(ColorRGBA.Green));
		materials.put("ZAxis", createBasicMaterial(ColorRGBA.Blue));
	}

	/**
	 * <p>
	 * Initializes the HUD by updating the guiNode.
	 * </p>
	 * 
	 */
	protected abstract void initHUD();

	/**
	 * <p>
	 * Initializes the different components of the scene, including the grid,
	 * the player, physics, etc.
	 * </p>
	 * 
	 */
	protected abstract void initScene();

	/**
	 * <p>
	 * Initializes a chase camera that follows the player. The camera should be
	 * locked to a top-down view.
	 * </p>
	 * 
	 */
	protected abstract void initCamera();

	/**
	 * <p>
	 * Initializes listeners and maps triggers to them for key presses, mouse
	 * clicks, and mouse movement.
	 * </p>
	 * 
	 */
	protected abstract void initControls();

	// ------------------------------------ //

	// ---- Application Update/Synchronization ---- //
	/**
	 * Does nothing by default.
	 */
	@Override
	public void simpleUpdate(float tpf) {
		return;
	}

	// -------------------------------------------- //

	// ---- Materials ---- //
	/**
	 * Gets the Material stored in the PlantApplication for reuse.
	 * 
	 * @param key
	 *            The key associated with the material.
	 * @return The material associated with the key, or null if none exists.
	 */
	public Material getMaterial(String key) {
		return materials.get(key);
	}

	/**
	 * Adds a Material to the PlantApplication for later reuse. Null keys and
	 * values are not accepted.
	 * 
	 * @param key
	 *            The key to use for the Material. If this is the same as a
	 *            previous key, the value will be overwritten.
	 * @param material
	 *            The Material to store for later reuse.
	 */
	public void addMaterial(String key, Material material) {
		if (key != null && material != null) {
			materials.put(key, material);
		}
	}

	/**
	 * Removes a Material from the PlantApplication.
	 * 
	 * @param key
	 *            The key of the Material to remove.
	 * @return The Material that was previously stored in the PlantApplication.
	 */
	public Material removeMaterial(String key) {
		return materials.remove(key);
	}

	// ------------------- //

	// ---- Heads Up Display (HUD) ---- //
	/**
	 * This operation returns the value of the boolean flag for whether or not
	 * the heads-up display should be displayed.
	 * 
	 * @return The boolean indicating whether or not the HUD should be
	 *         displayed.
	 */
	public boolean getDisplayHUD() {
		return displayHUD.get();
	}

	/**
	 * This operation sets the boolean to display or hide the heads-up display.
	 * 
	 * @param newVal
	 *            The boolean value to indicate whether or not the HUD should be
	 *            displayed.
	 */
	public void setDisplayHUD(final boolean enabled) {
		if (displayHUD.compareAndSet(!enabled, enabled)) {
			enqueue(new Callable<Boolean>() {
				public Boolean call() {
					if (enabled) {
						guiNode.attachChild(HUD);
					} else {
						guiNode.detachChild(HUD);
					}
					return true;
				}
			});
		}
		return;
	}

	// -------------------------------- //

	// ---- Scene ---- //
	/**
	 * A flag to indicate whether or not to show lines representing the x, y,
	 * and z axes at the origin (of jME3 space). The default colors of the axes
	 * are as follows:<br>
	 * <br>
	 * x - red<br>
	 * y - green<br>
	 * z - blue
	 */
	public boolean getDisplayAxes() {
		return displayAxes.get();
	}

	/**
	 * Sets whether or not to show the axes representing the unit vectors for x,
	 * y, and z. The default colors of the axes are as follows:<br>
	 * <br>
	 * x - red<br>
	 * y - green<br>
	 * z - blue
	 */
	public void setDisplayAxes(final boolean enabled) {
		if (displayAxes.compareAndSet(!enabled, enabled)) {
			enqueue(new Callable<Boolean>() {
				public Boolean call() {
					if (enabled) {
						rootNode.attachChild(axes);
					} else {
						rootNode.detachChild(axes);
					}
					return true;
				}
			});
		}
		return;
	}

	/**
	 * Creates the default axes, which are lines of different colors from the
	 * origin to the unit vectors. These are attached to the Node {@link #axes}.
	 */
	protected void initAxes() {

		Geometry geometry;
		Line line;
		float lineWidth = 3f;

		// Create the x axis as a red line of unit length.
		line = new Line(Vector3f.ZERO, Vector3f.UNIT_X);
		line.setLineWidth(lineWidth);
		geometry = new Geometry("x", line);
		geometry.setMaterial(materials.get("XAxis"));
		axes.attachChild(geometry);

		// Create the y axis as a green line of unit length.
		line = new Line(Vector3f.ZERO, Vector3f.UNIT_Y);
		line.setLineWidth(lineWidth);
		geometry = new Geometry("y", line);
		geometry.setMaterial(materials.get("YAxis"));
		axes.attachChild(geometry);

		// Create the z axis as a blue line of unit length.
		line = new Line(Vector3f.ZERO, Vector3f.UNIT_Z);
		line.setLineWidth(lineWidth);
		geometry = new Geometry("z", line);
		geometry.setMaterial(materials.get("ZAxis"));
		axes.attachChild(geometry);

		return;
	}

	// --------------- //

	// ---- Utility methods ---- //
	/**
	 * Generates a new, basic Material for use in the jME3 app.
	 * 
	 * @param color
	 *            The color of the material.
	 * @return A jME3 Material using Unshaded.j3md.
	 */
	public Material createBasicMaterial(ColorRGBA color) {
		Material material = new Material(assetManager,
				"Common/MatDefs/Misc/Unshaded.j3md");
		material.setColor("Color", color != null ? color : ColorRGBA.Red);

		return material;
	}

	/**
	 * Generates a new, basic material for use in the jME3 app.
	 * 
	 * @param color
	 *            The color of the material.
	 * @return A jME3 Material using Lighting.j3md.
	 */
	public Material createLitMaterial(ColorRGBA color) {
		Material material = new Material(assetManager,
				"Common/MatDefs/Light/Lighting.j3md");
		material.setBoolean("UseMaterialColors", true);
		material.setColor("Diffuse", color);
		material.setFloat("Shininess", 12f);
		material.setColor("Specular", ColorRGBA.White);
		material.setColor("Ambient", ColorRGBA.LightGray);
		material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);

		return material;
	}

	/**
	 * Gets a {@link CollisionResults} between a {@link Collidable} and a
	 * {@link Ray}. Collidables like Spatials and Nodes can be tested for
	 * collisions with Rays (usually from {@link #getCrosshairRay()} and
	 * {@link #getCursorRay()}.
	 * 
	 * @param collidable
	 *            The object we are trying to hit.
	 * @param ray
	 *            A ray that is being cast at the collidable.
	 * @return A CollisionResults for the collision between the collidable
	 *         object and the ray.
	 */
	public CollisionResults getCollision(Collidable collidable, Ray ray) {
		// Create a new results list.
		CollisionResults results = new CollisionResults();

		// See if the ray collides.
		if (collidable != null && ray != null) {
			collidable.collideWith(ray, results);
		}

		return results;
	}

	/**
	 * Gets a Ray from the crosshair's location to the grid. This uses the
	 * camera's position and direction.
	 * 
	 * @return A Ray derived from the crosshair's location.
	 */
	public Ray getCrosshairRay() {
		return new Ray(cam.getLocation(), cam.getDirection());
	}

	/**
	 * Gets a Ray from the camera to the cursor. The Ray can be used to
	 * determine where on the grid the cursor is pointed.
	 * <p>
	 * <b>Use this method when getting the cursor's location after a mouse-move
	 * event. For mouse-click events, use {@link #getCursorRayFromClick()}.</b>
	 * </p>
	 * 
	 * @return A Ray derived from the cursor's location.
	 */
	public Ray getCursorRay() {
		// Cursor-based picking (the ray from the camera to the.
		Vector2f click2d = inputManager.getCursorPosition();
		Vector3f click3d = cam.getWorldCoordinates(click2d, 0f);
		Vector3f dir = cam.getWorldCoordinates(click2d, 1f)
				.subtractLocal(click3d).normalizeLocal();
		return new Ray(click3d, dir);
	}

	/**
	 * Gets a Ray from the camera to the cursor. The Ray can be used to
	 * determine where on the grid the cursor is pointed.
	 * <p>
	 * <b>During click events, jME3 calculates the 2D cursor position as
	 * Y-decreasing from top to bottom. Use this method when getting the
	 * cursor's location after a mouse-click event. For mouse-move events, use
	 * {@link #getCursorRay()}.</b>
	 * </p>
	 * 
	 * @return A Ray derived from the cursor's location.
	 */
	public Ray getCursorRayFromClick() {
		// TODO - When we update jME3, we can probably remove this, because
		// there was a bug where mouse click y-coordinates were inconsistent
		// with mouse movement y-coordinates. See
		// http://hub.jmonkeyengine.org/forum/topic/issues-with-mouse-input-on-awtpanel/

		// Cursor-based picking (the ray from the camera to the.
		Vector2f click2d = inputManager.getCursorPosition();
		// Alter the 2D vector so that the Y position increases from top to
		// bottom.
		click2d.y = renderPanel.getHeight() - click2d.y;
		Vector3f click3d = cam.getWorldCoordinates(click2d, 0f);
		Vector3f dir = cam.getWorldCoordinates(click2d, 1f)
				.subtractLocal(click3d).normalizeLocal();
		return new Ray(click3d, dir);
	}

	/**
	 * Prints out a CollisionResults Collection for debugging/diagnosis.
	 * 
	 * @param results
	 *            A CollisionResults Collection to print out.
	 */
	public void printCollisionResults(CollisionResults results) {
		if (results != null) {
			// Print the results of the collision.
			System.out.println("Hits: " + results.size());
			if (results.size() > 0) {

				// Print the results.
				for (CollisionResult result : results) {
					// For each hit, we know distance, impact point, name of
					// geometry.
					float dist = result.getDistance();
					Vector3f pt = result.getContactPoint();
					String hit = result.getGeometry().getName();
					System.out.println("Hit " + hit + " at distance " + dist
							+ " at location " + pt.toString());
				}
			}
		}

		return;
	}
	// ------------------------- //

}
