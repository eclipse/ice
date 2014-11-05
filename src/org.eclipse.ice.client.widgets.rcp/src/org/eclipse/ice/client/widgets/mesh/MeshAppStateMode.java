package org.eclipse.ice.client.widgets.mesh;

import org.eclipse.ice.client.widgets.jme.InputControl;
import org.eclipse.ice.client.widgets.jme.SimpleAppState;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;

/**
 * This provides a base class for custom <code>AppState</code>s designed for
 * manipulating the scene in a <code>MeshAppState</code>. Any features shared
 * among all such <code>AppState</code>s should be implemented here.
 * 
 * @author Jordan Deyton
 * 
 */
public abstract class MeshAppStateMode extends SimpleAppState implements
		ICameraListener {

	/**
	 * The <code>MeshAppState</code> that this <code>MeshAppStateMode</code>
	 * supports.
	 */
	protected final MeshAppState appState;

	/**
	 * An <code>InputControl</code> that contains the mapping name, listener,
	 * and trigger for handling the mouse clicks. It calls
	 * {@link #leftClick(boolean, float)} or {@link #rightClick(boolean, float)}
	 * when the mouse is clicked.
	 */
	private InputControl mouseClickAction;

	/**
	 * The custom chase camera for the {@link #appState}. We need a reference to
	 * update the scale of the vertices and edges for temporary Geometries when
	 * the zoom changes.
	 */
	private CustomChaseCamera chaseCam;

	/**
	 * The default constructor.
	 * 
	 * @param appState
	 *            The <code>MeshAppState</code> that this
	 *            <code>MeshAppStateMode</code> supports.
	 */
	public MeshAppStateMode(MeshAppState appState) {
		super();

		// Set the reference to the MeshAppState and its mesh.
		this.appState = appState;

		return;
	}

	// ---- Initialization methods ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#initMaterials()
	 */
	@Override
	protected void initMaterials() {
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#initScene()
	 */
	@Override
	protected void initScene() {
		// Nothing to do yet.
	}

	/**
	 * Creates the listeners for connecting left and right mouse clicks with the
	 * {@link #leftClick(boolean, float)} and
	 * {@link #rightClick(boolean, float)} methods.
	 * 
	 * <p>
	 * <b>Note:</b> If overridden, the <b><i>first</i></b> call should be
	 * <code>super.initControls();</code> to properly initialize the left and
	 * right mouse clicks controls.
	 * </p>
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#initControls()
	 */
	@Override
	protected void initControls() {

		ActionListener action;

		// Create an InputControl for listening for mouse left and right clicks.
		// Each type of click should redirect to the respective left/right click
		// method that can be overridden by child classes.
		final String leftClick = "leftClick";
		final String rightClick = "rightClick";
		action = new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				if (leftClick.equals(name)) {
					leftClick(isPressed, tpf);
				} else { // Otherwise, name must equal "rightClick"!
					rightClick(isPressed, tpf);
				}
			}
		};
		mouseClickAction = new InputControl(action, leftClick, rightClick);
		mouseClickAction.addTriggers(leftClick, new MouseButtonTrigger(
				MouseInput.BUTTON_LEFT));
		mouseClickAction.addTriggers(rightClick, new MouseButtonTrigger(
				MouseInput.BUTTON_LEFT));

		return;
	}

	// -------------------------------- //

	// ---- Enable/Disable ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#enableAppState()
	 */
	@Override
	public void enableAppState() {
		super.enableAppState();

		// Make sure we are registered with the MeshAppState's chase camera.
		setChaseCamera(appState.getChaseCamera());
	}

	/**
	 * Registers mouse click controls wired to
	 * {@link #leftClick(boolean, float)} and
	 * {@link #rightClick(boolean, float)}.
	 * <p>
	 * <b>Note:</b> If overridden, the <b><i>first</i></b> call should be
	 * <code>super.registerControls();</code> to properly register the left and
	 * right mouse clicks controls.
	 * </p>
	 */
	public void registerControls() {
		InputManager input = appState.getApplication().getInputManager();
		mouseClickAction.registerWithInput(input);
	}

	public void disableAppState() {

		// If possible, unregister from the MeshAppState's chase camera.
		if (chaseCam != null) {
			chaseCam.removeCameraListener(this);
			chaseCam = null;
		}

		super.disableAppState();
	}

	/**
	 * Unregisters mouse click controls wired to
	 * {@link #leftClick(boolean, float)} and
	 * {@link #rightClick(boolean, float)}.
	 * <p>
	 * <b>Note:</b> If overridden, the <b><i>last</i></b> call should be
	 * <code>super.unregisterControls();</code> to properly unregister the left
	 * and right mouse clicks controls.
	 * </p>
	 */
	public void unregisterControls() {
		mouseClickAction.unregisterFromInput();
	}

	// ------------------------ //

	// ---- Cleanup methods ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#clearMaterials()
	 */
	@Override
	protected void clearMaterials() {
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#clearScene()
	 */
	@Override
	protected void clearScene() {
		// Nothing to do yet.
	}

	/**
	 * Clears the listeners for connecting left and right mouse clicks with the
	 * {@link #leftClick(boolean, float)} and
	 * {@link #rightClick(boolean, float)} methods.
	 * 
	 * <p>
	 * <b>Note:</b> If overridden, the <b><i>last</i></b> call should be
	 * <code>super.clearControls();</code> to properly dispose the left and
	 * right mouse clicks controls.
	 * </p>
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#clearControls()
	 */
	@Override
	protected void clearControls() {
		mouseClickAction = null;
	}

	// ------------------------- //

	// ---- Update methods ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.jme.SimpleAppState#update(float)
	 */
	@Override
	public void update(float tpf) {
		// Nothing to do yet.
	}

	// ------------------------ //

	/**
	 * This is called if the left mouse button is clicked or released.
	 * 
	 * @param isPressed
	 *            Whether the left mouse button is pressed or released.
	 * @param tpf
	 *            Time per frame, as in {@link #update(float)}.
	 */
	public void leftClick(boolean isPressed, float tpf) {
		// Nothing to do yet.
	}

	/**
	 * This is called if the right mouse button is clicked or released.
	 * 
	 * @param isPressed
	 *            Whether the right mouse button is pressed or released.
	 * @param tpf
	 *            Time per frame, as in {@link #update(float)}.
	 */
	public void rightClick(boolean isPressed, float tpf) {
		// Nothing to do yet.
	}

	/**
	 * Gets the name of this <code>MeshAppStateMode</code>. This is used as a
	 * display name for buttons and other widgets.
	 * 
	 * @return A String. It should not be null.
	 */
	public abstract String getName();

	/**
	 * Gets a description of this <code>MeshAppStateMode</code>. This is used as
	 * a description for widget tool tips.
	 * 
	 * @return A String. It should not be null.
	 */
	public abstract String getDescription();

	/**
	 * Sets the custom chase camera that is used by the parent
	 * <code>MeshAppState</code>. This is critical to update the scales of
	 * vertices and edges when the camera's zoom changes.
	 * 
	 * @param chaseCam
	 *            The new chase camera.
	 */
	public void setChaseCamera(CustomChaseCamera chaseCam) {

		if (chaseCam != null && chaseCam != this.chaseCam) {
			// Unregister from the previous chase camera if necessary.
			if (this.chaseCam != null) {
				this.chaseCam.removeCameraListener(this);
			}

			// Listen to the camera for changes in zoom.
			this.chaseCam = chaseCam;
			chaseCam.addCameraListener(this);
		}

		return;
	}

}
