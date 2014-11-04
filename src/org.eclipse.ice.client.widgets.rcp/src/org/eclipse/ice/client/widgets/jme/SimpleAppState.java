package org.eclipse.ice.client.widgets.jme;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jme3.app.Application;
import com.jme3.app.state.AppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.collision.Collidable;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.RenderManager;

public abstract class SimpleAppState implements AppState, IRenderQueue {

	// ---- Requirements from AppState ---- //
	/**
	 * Whether or not this view has been initialized in the {@link #app}.
	 */
	private final AtomicBoolean initialized;
	/**
	 * Whether or not this view is enabled. This is a required feature for
	 * {@link AppState}s.
	 */
	private final AtomicBoolean enabled;
	// ------------------------------------ //

	/**
	 * Whether or not controls are registered with the {@link #app}'s
	 * <code>InputManager</code>.
	 */
	private final AtomicBoolean controlsRegistered;

	/**
	 * A map of {@link Material}s. This lets us re-use materials within this
	 * view. This should be updated via {@link #setMaterial(String, Material)}
	 * and {@link #removeMaterial(String)}, and the abstract method
	 * {@link #initMaterials()}.
	 */
	private final Map<String, Material> materials;

	/**
	 * The <code>Application</code> to which this <code>AppState</code> is
	 * attached.
	 */
	private Application app;

	/**
	 * The default constructor.
	 */
	public SimpleAppState() {

		// Initialize the requirements from the AppState interface.
		initialized = new AtomicBoolean(false);
		enabled = new AtomicBoolean(true);

		// Initialize the flag for registering the controls.
		controlsRegistered = new AtomicBoolean(false);

		// Initialize the map of Materials.
		materials = new HashMap<String, Material>();

		return;
	}

	// ---- Starting and stopping ---- //
	/**
	 * Starts the <code>SimpleAppState</code> by attaching it to the specified
	 * <code>Application</code>.
	 * 
	 * @param app
	 *            The jME <code>Application</code> that will be hosting this
	 *            <code>SimpleAppState</code>.
	 */
	public void start(Application app) {
		if (!isInitialized() && app != null) {
			this.app = app;
			app.getStateManager().attach(this);
		}
	}

	/**
	 * Stops this <code>SimpleAppState</code> if it has been initialized.
	 */
	public void stop() {
		if (isInitialized()) {
			app.getStateManager().detach(this);
		}
	}

	// ------------------------------- //

	// ---- Implements AppState ---- //
	// The below methods are organized by the general order in which they are
	// called, or by the AppState lifecycle.

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jme3.app.state.AppState#stateAttached(com.jme3.app.state.AppStateManager
	 * )
	 */
	@Override
	public void stateAttached(AppStateManager arg0) {
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jme3.app.state.AppState#initialize(com.jme3.app.state.AppStateManager
	 * , com.jme3.app.Application)
	 */
	@Override
	public void initialize(AppStateManager stateManager, Application app) {

		// We cannot proceed if the app is already initialized or if the
		// parameters are invalid.
		if (!isInitialized() && app == this.app
				&& stateManager == app.getStateManager()) {

			// Perform the customizable initialization routine.
			initAppState();

			// Mark the application as being initialized.
			initialized.set(true);

			// If already enabled, then enable customizable features.
			if (isEnabled()) {
				enableAppState();
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.state.AppState#isInitialized()
	 */
	@Override
	public boolean isInitialized() {
		return initialized.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.state.AppState#render(com.jme3.renderer.RenderManager)
	 */
	@Override
	public void render(RenderManager rm) {
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.state.AppState#update(float)
	 */
	@Override
	public abstract void update(float tpf);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.state.AppState#postRender()
	 */
	@Override
	public void postRender() {
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.state.AppState#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled.get();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.state.AppState#setEnabled(boolean)
	 */
	@Override
	public final void setEnabled(boolean active) {

		// Set the enabled flag. If initialized, we can call the customizable
		// enable/disable methods.
		if (enabled.compareAndSet(!active, active) && isInitialized()) {
			// Call either of the customizable enable/disable AppState methods
			// depending on the value.
			if (active) {
				enableAppState();
			} else {
				disableAppState();
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jme3.app.state.AppState#cleanup()
	 */
	@Override
	public final void cleanup() {

		if (isInitialized()) {
			// Mark the AppState as not being initialized.
			initialized.set(false);

			// If enabled, disable the customizable features. This is critical
			// for unregistering controls from the app's InputManager when the
			// AppState detaches!
			if (isEnabled()) {
				disableAppState();
			}

			// Perform the overridable cleanup routine.
			cleanupAppState();

			// Clear the reference to the Application.
			app = null;
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jme3.app.state.AppState#stateDetached(com.jme3.app.state.AppStateManager
	 * )
	 */
	@Override
	public void stateDetached(AppStateManager arg0) {
		// Nothing to do yet.
	}

	// ----------------------------- //

	// ---- Implements IRenderQueue ---- //
	/**
	 * Queues a rendering task for the underlying {@link Application}. This task
	 * will be performed on the app's rendering thread. This should be used when
	 * the <code>SimpleAppState</code> or a related class needs to render
	 * something.
	 */
	@Override
	public <T> Future<T> enqueue(Callable<T> callable) {
		Future<T> value = null;
		if (callable != null && app != null) {
			value = app.enqueue(callable);
		}
		return value;
	}

	// --------------------------------- //

	/**
	 * Gets the current jME <code>Application</code> to which this
	 * <code>AppState</code> is attached.
	 * 
	 * @return The current jME {@link #app}. This will be null if the
	 *         <code>SimpleAppState</code> has not been started.
	 */
	public Application getApplication() {
		return app;
	}

	// ---- Sub-class lifecycle routines ---- //
	// The methods below are intended for sub-classes to manage their lifecycle
	// and are organized in the order in which they are typically called.

	/**
	 * Initializes the <code>SimpleAppState</code>.
	 * <p>
	 * <b>Note:</b> If this method is overridden, then the <b><i>first</i></b>
	 * call from the sub-class should be <code>super.initAppState();</code> in
	 * order to properly initialize the default <code>SimpleAppState</code>
	 * features.
	 * </p>
	 */
	protected void initAppState() {
		// Initialize the default features.
		initMaterials();
		initScene();
		initControls();
	}

	/**
	 * Initialize the materials used by this <code>SimpleAppState</code>. You
	 * should use {@link #setMaterial(String, Material)} to add new materials.
	 * Also see the provided utility methods for creating basic materials.
	 * 
	 * @see #createBasicMaterial(ColorRGBA)
	 * @see #createLitMaterial(ColorRGBA)
	 */
	protected abstract void initMaterials();

	/**
	 * Initialize the scene graph for this view. <code>Nodes</code> should be
	 * attached to the {@link #rootNode}. You should use <code>Material</code>s
	 * that have been initialized in {@link #initMaterials()} by calling
	 * {@link #getMaterial(String)} to fetch them.
	 */
	protected abstract void initScene();

	/**
	 * Initialize the controls used by this view. By default, no controls are
	 * provided.
	 */
	protected abstract void initControls();

	/**
	 * This is called when the <code>SimpleAppState</code> is enabled or
	 * "activated". This is equivalent to unpausing or resuming a game.
	 * <p>
	 * <b>Note:</b> If this method is overridden, then the <b><i>first</i></b>
	 * call from the sub-class should be <code>super.enableAppState();</code> in
	 * order to properly enable the default <code>SimpleAppState</code>
	 * features.
	 * </p>
	 */
	protected void enableAppState() {
		// This method is usually called during run-time. Thus, we must make
		// sure that it is initialized. We should also synchronize this method
		// with the isEnabled() flag.
		if (isEnabled() && isInitialized()) {
			enableControls();

			// Any additional features that have to be enabled should be dealt
			// with here.
		}

		return;
	}

	/**
	 * This method enables the controls for the <code>SimpleAppState</code>.
	 * <p>
	 * This method is used to prevent controls from being registered with the
	 * jME {@link #app}'s <code>InputManager</code> more than once. The code for
	 * registering the controls should be implemented in
	 * {@link #registerControls()}.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method should not normally be overridden. However, if
	 * overridden, the <b><i>first</i></b> call in the sub-class' method should
	 * be <code>super.enableControls();</code>
	 * </p>
	 */
	public void enableControls() {
		if (isEnabled() && isInitialized()
				&& controlsRegistered.compareAndSet(false, true)) {
			registerControls();
		}
	}

	/**
	 * Registers the <code>SimpleAppState</code>'s customized controls with the
	 * underlying {@link #app}'s <code>InputManager</code>.
	 */
	protected abstract void registerControls();

	/**
	 * This is called when the <code>SimpleAppState</code> is disabled or
	 * "deactivated". This is equivalent to pausing or halting a game.
	 * <p>
	 * <b>Note:</b> If this method is overridden, then the <b><i>last</i></b>
	 * call from the sub-class should be <code>super.disableAppState();</code>
	 * in order to properly disable the default <code>SimpleAppState</code>
	 * features.
	 * </p>
	 */
	protected void disableAppState() {
		// This method is usually called during run-time. Thus, we must make
		// sure that it is initialized. We should also synchronize this method
		// with the isEnabled() flag.
		if (!isEnabled() && isInitialized()) {
			// Any additional changes that must be disabled should be dealt with
			// here.

			disableControls();
		}

		return;
	}

	/**
	 * This method disables the controls for the <code>SimpleAppState</code>.
	 * <p>
	 * This method is used to prevent controls from being unregistered from the
	 * jME {@link #app}'s <code>InputManager</code> more than once. The code for
	 * unregistering the controls should be implemented in
	 * {@link #unregisterControls()}.
	 * </p>
	 */
	public void disableControls() {
		if (isInitialized() && controlsRegistered.compareAndSet(true, false)) {
			unregisterControls();
		}
	}

	/**
	 * Unregisters the <code>SimpleAppState</code>'s customized controls from
	 * the underlying {@link #app}'s <code>InputManager</code>.
	 */
	protected abstract void unregisterControls();

	/**
	 * Disconnects the <code>SimpleAppState</code> from the associated
	 * {@link Application}.
	 * <p>
	 * <b>Note:</b> If this method is overridden, then the <b><i>last</i></b>
	 * call from the sub-class should be <code>super.cleanupAppState();</code>
	 * in order to properly clean up the default <code>SimpleAppState</code>
	 * features.
	 * </p>
	 */
	protected void cleanupAppState() {
		// Un-initialize the default features.
		clearControls();
		clearScene();
		clearMaterials();
	}

	/**
	 * Clears the <code>SimpleAppState</code>'s materials. This method should at
	 * least undo the initialization done in {@link #initMaterials()}.
	 */
	protected abstract void clearMaterials();

	/**
	 * Clears the <code>SimpleAppState</code>'s scene graph. This method should
	 * at least undo the initialization done in {@link #initScene()}.
	 */
	protected abstract void clearScene();

	/**
	 * Clears the <code>SimpleAppState</code>'s controls. This method should at
	 * least undo the initialization done in {@link #initControls()}.
	 */
	protected abstract void clearControls();

	// -------------------------------------- //

	// ---- Materials ---- //
	/**
	 * Gets a <code>Material</code> that was previously created and associated
	 * with the specified name.
	 * 
	 * @param name
	 *            The name or key of the material.
	 * @return The associated <code>Material</code>, or null if none could be
	 *         found.
	 * @see #setMaterial(String, Material)
	 */
	public Material getMaterial(String name) {
		return materials.get(name);
	}

	/**
	 * Associates a <code>Material</code> with a name or key for re-use later.
	 * Null keys are accepted but not recommended.
	 * 
	 * @param name
	 *            The name or key of the <code>Material</code>
	 * @param material
	 *            The <code>Material</code> to store for later re-use.
	 * @see #getMaterial(String)
	 * @see #removeMaterial(String)
	 */
	public void setMaterial(String name, Material material) {
		if (material != null) {
			materials.put(name, material);
		}
	}

	/**
	 * Removes a <code>Material</code> from the view. This should be called if
	 * the <code>Material</code> will not be re-used again.
	 * 
	 * @param name
	 *            The name or key associated with the <code>Material</code>.
	 * @return The <code>Material</code> that was removed. This is null if the
	 *         key is invalid.
	 * @see #getMaterial(String)
	 * @see #setMaterial(String, Material)
	 */
	public Material removeMaterial(String name) {
		return materials.remove(name);
	}

	// ------------------- //

	// ---- Utility methods ---- //
	/**
	 * Generates a new, basic <code>Material</code> for use in the jME3
	 * {@link #app}.
	 * 
	 * @param color
	 *            The color of the material.
	 * @return A jME3 Material using Unshaded.j3md, or null if the
	 *         <code>SimpleAppState</code> is not initialized.
	 */
	public Material createBasicMaterial(ColorRGBA color) {
		Material material = null;

		if (app != null) {
			material = new Material(app.getAssetManager(),
					"Common/MatDefs/Misc/Unshaded.j3md");
			material.setColor("Color", color != null ? color : ColorRGBA.Red);
		}

		return material;
	}

	/**
	 * Generates a new, basic <code>Material</code> for use in the jME3
	 * {@link #app}.
	 * 
	 * @param color
	 *            The color of the material.
	 * @return A jME3 Material using Lighting.j3md, or null if the
	 *         <code>SimpleAppState</code> is not initialized.
	 */
	public Material createLitMaterial(ColorRGBA color) {
		Material material = null;

		if (app != null) {
			material = new Material(app.getAssetManager(),
					"Common/MatDefs/Light/Lighting.j3md");
			material.setBoolean("UseMaterialColors", true);
			material.setColor("Diffuse", color);
			material.setFloat("Shininess", 12f);
			material.setColor("Specular", ColorRGBA.White);
			material.setColor("Ambient", ColorRGBA.LightGray);
			material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
		}

		return material;
	}

	/**
	 * Gets a {@link CollisionResults} between a {@link Collidable} and a
	 * {@link Ray}. Collidables like Spatials and Nodes (e.g.,
	 * {@link #getGrid()} and {@link #getVertexSpatials()}) can be tested for
	 * collisions with Rays (e.g., {@link #getCrosshairRay()} and
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
		// Print the results.
		// printCollisionResults(results);

		return results;
	}

	/**
	 * Gets a Ray from the crosshair's location to the grid. This uses the
	 * camera's position and direction.
	 * 
	 * @return A Ray derived from the crosshair's location.
	 */
	public Ray getCrosshairRay(Camera cam) {
		Ray ray = null;
		if (cam != null) {
			ray = new Ray(cam.getLocation(), cam.getDirection());
		}
		return ray;
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
	public Ray getCursorRay(Camera cam) {
		Ray ray = null;
		if (cam != null) {
			// Cursor-based picking (the ray from the camera to the scene).
			Vector2f click2d = app.getInputManager().getCursorPosition();
			Vector3f click3d = cam.getWorldCoordinates(click2d, 0f);
			Vector3f dir = cam.getWorldCoordinates(click2d, 1f)
					.subtractLocal(click3d).normalizeLocal();
			ray = new Ray(click3d, dir);
		}
		return ray;
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
	public Ray getCursorRayFromClick(EmbeddedView view) {
		// TODO - When we update jME3, we can probably remove this, because
		// there was a bug where mouse click y-coordinates were inconsistent
		// with mouse movement y-coordinates. See
		// http://hub.jmonkeyengine.org/forum/topic/issues-with-mouse-input-on-awtpanel/

		Ray ray = null;
		if (view != null) {
			Camera cam = view.getCamera();

			// Cursor-based picking (the ray from the camera to the scene).
			Vector2f click2d = app.getInputManager().getCursorPosition();
			// Alter the 2D vector so that the Y position increases from top to
			// bottom.
			click2d.y = view.getRenderPanel().getHeight() - click2d.y;
			Vector3f click3d = cam.getWorldCoordinates(click2d, 0f);
			Vector3f dir = cam.getWorldCoordinates(click2d, 1f)
					.subtractLocal(click3d).normalizeLocal();

			ray = new Ray(click3d, dir);
		}
		return ray;
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
