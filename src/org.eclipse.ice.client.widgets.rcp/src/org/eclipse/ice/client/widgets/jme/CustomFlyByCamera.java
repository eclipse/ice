package org.eclipse.ice.client.widgets.jme;

import com.jme3.input.FlyByCamera;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Matrix3f;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

// TODO Fix the rise and lower to use the current up vector.

public class CustomFlyByCamera extends FlyByCamera {

	/**
	 * The current up vector for the camera. Same as {@link Camera#getUp()} for
	 * the camera renderer.
	 */
	private final Vector3f up;
	/**
	 * The current left vector for the camera. Same as {@link Camera#getLeft()}
	 * for the camera renderer.
	 */
	private final Vector3f left;
	/**
	 * The current direction vector for the camera. Same as
	 * {@link Camera#getDirection()} for the camera renderer.
	 */
	private final Vector3f dir;

	// TODO Set this when initialUpVec is set.
	// TODO Add a reset method.
	/**
	 * The "actual" or current up vector used when rotating the camera left or
	 * right.
	 */
	private final Vector3f actualUp;

	private final Vector3f defaultUp;
	private final Vector3f defaultLeft;
	private final Vector3f defaultDir;

	// TODO Add a getter/setter to toggle this.
	/**
	 * Whether or not rotation is allowed with this camera.
	 */
	private boolean rotateEnabled;

	/**
	 * The default constructor.
	 * 
	 * @param cam
	 *            The jME3 camera renderer. Typically, this is the
	 *            SimpleApplication's <code>cam</code> variable.
	 */
	public CustomFlyByCamera(Camera cam) {
		super(cam);

		// Create the vectors for the current camera orientation.
		up = cam.getUp();
		left = cam.getLeft();
		dir = cam.getDirection();
		// Create a separate vector to store the "up" vector used when rotating
		// left and right.
		actualUp = up.clone();

		// Initialize the rotation variables. Rotation is enabled by default,
		// and you can drag to rotate.
		rotateEnabled = true;
		setDragToRotate(true);

		defaultUp = Vector3f.UNIT_Y.clone();
		defaultLeft = Vector3f.UNIT_X.negate();
		defaultDir = Vector3f.UNIT_Z.negate();

		return;
	}

	@Override
	public void registerWithInput(InputManager inputManager) {
		this.inputManager = inputManager;

		// Add the key/mouse mappings and register listeners for the triggered
		// key/mouse events.
		addMappings();
		addListeners();

		// If necessary, hide the cursor.
		inputManager.setCursorVisible(dragToRotate || !isEnabled());

		// Map the joysticks with the input manager.
		Joystick[] joysticks = inputManager.getJoysticks();
		if (joysticks != null && joysticks.length > 0) {
			for (Joystick j : joysticks) {
				mapJoystick(j);
			}
		}

		inputManager.addMapping("test", new KeyTrigger(KeyInput.KEY_R));
		inputManager.addListener(new ActionListener() {
			public void onAction(String name, boolean isPressed, float tpf) {
				resetRotation();
			}
		}, "test");

		return;
	}

	private void addMappings() {

		// Set up the arrow keys for rotation.
		inputManager.addMapping("FLYCAM_Left",
				new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("FLYCAM_Right", new KeyTrigger(
				KeyInput.KEY_RIGHT));
		inputManager.addMapping("FLYCAM_Up", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("FLYCAM_Down",
				new KeyTrigger(KeyInput.KEY_DOWN));

		// Set up the mouse for rotation (which only works on drag).
		inputManager.addMapping("FLYCAM_MouseLeft", new MouseAxisTrigger(
				MouseInput.AXIS_X, true));
		inputManager.addMapping("FLYCAM_MouseRight", new MouseAxisTrigger(
				MouseInput.AXIS_X, false));
		inputManager.addMapping("FLYCAM_MouseUp", new MouseAxisTrigger(
				MouseInput.AXIS_Y, false));
		inputManager.addMapping("FLYCAM_MouseDown", new MouseAxisTrigger(
				MouseInput.AXIS_Y, true));

		// Set up the Q and E keys to roll left and right.
		inputManager.addMapping("FLYCAM_RollLeft", new KeyTrigger(
				KeyInput.KEY_Q));
		inputManager.addMapping("FLYCAM_RollRight", new KeyTrigger(
				KeyInput.KEY_E));

		// Set up WASD for movement. Use SPACE and C for raising and lowering.
		inputManager.addMapping("FLYCAM_StrafeLeft", new KeyTrigger(
				KeyInput.KEY_A));
		inputManager.addMapping("FLYCAM_StrafeRight", new KeyTrigger(
				KeyInput.KEY_D));
		inputManager.addMapping("FLYCAM_Forward",
				new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("FLYCAM_Backward", new KeyTrigger(
				KeyInput.KEY_S));
		inputManager.addMapping("FLYCAM_Rise", new KeyTrigger(
				KeyInput.KEY_SPACE));
		inputManager.addMapping("FLYCAM_Lower", new KeyTrigger(KeyInput.KEY_C));

		// Set up the mouse wheel for zooming in and out.
		inputManager.addMapping("FLYCAM_ZoomIn", new MouseAxisTrigger(
				MouseInput.AXIS_WHEEL, false));
		inputManager.addMapping("FLYCAM_ZoomOut", new MouseAxisTrigger(
				MouseInput.AXIS_WHEEL, true));

		// Set up the left mouse button for toggling dragging.
		inputManager.addMapping("FLYCAM_RotateDrag", new MouseButtonTrigger(
				MouseInput.BUTTON_LEFT));

		return;
	}

	private void deleteMappings() {

		// Clear the rotation mappings.
		inputManager.deleteMapping("FLYCAM_Left");
		inputManager.deleteMapping("FLYCAM_Right");
		inputManager.deleteMapping("FLYCAM_Up");
		inputManager.deleteMapping("FLYCAM_Down");
		inputManager.deleteMapping("FLYCAM_MouseLeft");
		inputManager.deleteMapping("FLYCAM_MouseRight");
		inputManager.deleteMapping("FLYCAM_MouseUp");
		inputManager.deleteMapping("FLYCAM_MouseDown");

		// Clear the rotation (roll) mappings.
		inputManager.deleteMapping("FLYCAM_RollLeft");
		inputManager.deleteMapping("FLYCAM_RollRight");

		// Clear the movement mappings.
		inputManager.deleteMapping("FLYCAM_StrafeLeft");
		inputManager.deleteMapping("FLYCAM_StrafeRight");
		inputManager.deleteMapping("FLYCAM_Forward");
		inputManager.deleteMapping("FLYCAM_Backward");
		inputManager.deleteMapping("FLYCAM_Rise");
		inputManager.deleteMapping("FLYCAM_Lower");

		// Clear the zoom mappings.
		inputManager.deleteMapping("FLYCAM_ZoomIn");
		inputManager.deleteMapping("FLYCAM_ZoomOut");

		// Clear the drag mapping.
		inputManager.deleteMapping("FLYCAM_RotateDrag");

		return;
	}

	private void addListeners() {

		if (inputManager != null) {
			inputManager.addListener(keyAnalogListener, "FLYCAM_Left",
					"FLYCAM_Right", "FLYCAM_Up", "FLYCAM_Down",
					"FLYCAM_RollLeft", "FLYCAM_RollRight", "FLYCAM_StrafeLeft",
					"FLYCAM_StrafeRight", "FLYCAM_Forward", "FLYCAM_Backward",
					"FLYCAM_Rise", "FLYCAM_Lower", "FLYCAM_ZoomIn",
					"FLYCAM_ZoomOut");
			inputManager.addListener(keyActionListener);
			inputManager.addListener(mouseAnalogListener, "FLYCAM_MouseLeft",
					"FLYCAM_MouseRight", "FLYCAM_MouseUp", "FLYCAM_MouseDown");
			inputManager.addListener(mouseActionListener, "FLYCAM_RotateDrag");
		}

		return;
	}

	private void removeListeners() {

		if (inputManager != null) {
			inputManager.removeListener(keyAnalogListener);
			inputManager.removeListener(keyActionListener);
			inputManager.removeListener(mouseAnalogListener);
			inputManager.removeListener(mouseActionListener);
		}

		return;
	}

	private final AnalogListener keyAnalogListener = new AnalogListener() {
		public void onAnalog(String name, float value, float tpf) {

			if (name.equals("FLYCAM_Left")) {
				rotateCamera(value, actualUp);
			} else if (name.equals("FLYCAM_Right")) {
				rotateCamera(-value, actualUp);
			} else if (name.equals("FLYCAM_Up")) {
				rotateCamera(-value * (invertY ? -1 : 1), left);
			} else if (name.equals("FLYCAM_Down")) {
				rotateCamera(value * (invertY ? -1 : 1), left);
			} else if (name.equals("FLYCAM_RollLeft")) {
				rotateCamera(-value, dir);
				actualUp.set(up);
				// Update the current up vector used for rotating right/left.
			} else if (name.equals("FLYCAM_RollRight")) {
				rotateCamera(value, dir);
				// Update the current up vector used for rotating right/left.
				actualUp.set(up);
			} else if (name.equals("FLYCAM_Forward")) {
				moveCamera(value, false);
			} else if (name.equals("FLYCAM_Backward")) {
				moveCamera(-value, false);
			} else if (name.equals("FLYCAM_StrafeLeft")) {
				moveCamera(value, true);
			} else if (name.equals("FLYCAM_StrafeRight")) {
				moveCamera(-value, true);
			} else if (name.equals("FLYCAM_Rise")) {
				riseCamera(value);
			} else if (name.equals("FLYCAM_Lower")) {
				riseCamera(-value);
			} else if (name.equals("FLYCAM_ZoomIn")) {
				zoomCamera(value);
			} else if (name.equals("FLYCAM_ZoomOut")) {
				zoomCamera(-value);
			}

			return;
		}
	};
	private final ActionListener keyActionListener = new ActionListener() {
		public void onAction(String name, boolean isPressed, float tpf) {
			// TODO
		}
	};
	private final AnalogListener mouseAnalogListener = new AnalogListener() {
		public void onAnalog(String name, float value, float tpf) {

			if (canRotate) {
				if (name.equals("FLYCAM_MouseLeft")) {
					rotateCamera(value, actualUp);
				} else if (name.equals("FLYCAM_MouseRight")) {
					rotateCamera(-value, actualUp);
				} else if (name.equals("FLYCAM_MouseUp")) {
					rotateCamera(-value * (invertY ? -1 : 1), left);
				} else if (name.equals("FLYCAM_MouseDown")) {
					rotateCamera(value * (invertY ? -1 : 1), left);
				}
			}

			return;
		}
	};
	private final ActionListener mouseActionListener = new ActionListener() {
		public void onAction(String name, boolean isPressed, float tpf) {

			if (name.equals("FLYCAM_RotateDrag") && dragToRotate) {
				canRotate = isPressed;
				inputManager.setCursorVisible(!isPressed);
			} else if (name.equals("FLYCAM_InvertY")) {
				// Toggle on the up.
				if (!isPressed) {
					invertY = !invertY;
				}
			}

			return;
		}
	};

	@Override
	public void onAnalog(String name, float value, float tpf) {
		// Do nothing.
	}

	@Override
	public void onAction(String name, boolean value, float tpf) {
		// Do nothing.
	}

	@Override
	protected void rotateCamera(float value, Vector3f axis) {

		if (rotateEnabled) {
			Matrix3f mat = new Matrix3f();
			mat.fromAngleNormalAxis(rotationSpeed * value, axis);

			mat.mult(up, up);
			mat.mult(left, left);
			mat.mult(dir, dir);

			updateCameraAxes(left, up, dir);
		}

		return;
	}

	public boolean getEnabled() {
		return enabled;
	}
	
	@Override
	public void setEnabled(boolean enable) {

		// If disabled, we need to unregister the listeners, i.e. the camera
		// cannot be moved. If enabled, we need to register the listeners. Make
		// no other changes if the state has not changed.
		if (enable != enabled) {
			if (enable) {
				addListeners();
			} else {
				removeListeners();
			}
		}

		// Proceed with the default actions.
		super.setEnabled(enable);

		return;
	}

	// What does this method need to do, exactly? Should it just change the axes
	// back to the default, or should it just return the view to the "level"
	// position (the same flat horizontal plane as specified with the defaults).
	public void resetRotation() {
		// FIXME This does not work properly

		// Vector3f cross = up.cross(defaultUp).normalizeLocal();
		// float angle = defaultUp.angleBetween(up);
		// Quaternion rotation = new Quaternion();
		// rotation.fromAngleNormalAxis(angle, cross);
		//
		// rotation.multLocal(up);
		// rotation.multLocal(left);
		// rotation.multLocal(dir);
		//
		// up.normalizeLocal();
		// left.normalizeLocal();
		// dir.normalizeLocal();
		//
		// System.out.println("====");
		// System.out.println(defaultUp);
		// System.out.println(up);

		// The math here is tricky. What I want to do with this is "restore" the
		// original "actualUp" to the default value, but I want to change the
		// current left and dir vectors to the nearest location. In other words,
		// any rotation done by rolling will be reverted.

		updateCameraAxes(left, up, dir);

		return;
	}

	/**
	 * Resets the camera to the default orientation.
	 */
	public void resetToDefaultOrientation() {
		// Restore the default axes.
		actualUp.set(defaultUp);
		up.set(defaultUp);
		left.set(defaultLeft);
		dir.set(defaultDir);

		// Re-orient the camera.
		updateCameraAxes(left, up, dir);
	}

	/**
	 * A quaternion used to compute axes. Initialized here to avoid having to
	 * create new <code>Quaternion</code>s regularly.
	 */
	private final Quaternion axes = new Quaternion();

	/**
	 * Sets the new axes for the <code>FlyByCamera</code>'s camera renderer.
	 * This has the effect of re-orienting the camera. These values are not
	 * checked, so you have to be careful what you set.
	 * 
	 * @param left
	 *            The left vector.
	 * @param up
	 *            The up vector.
	 * @param dir
	 *            The direction in which the vector is pointing.
	 */
	private void updateCameraAxes(Vector3f left, Vector3f up, Vector3f dir) {
		axes.fromAxes(left, up, dir);
		cam.setAxes(axes);
	}

}
