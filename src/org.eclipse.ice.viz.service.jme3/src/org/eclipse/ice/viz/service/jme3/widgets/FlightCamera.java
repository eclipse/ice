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
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.jme3.widgets;

import java.util.ArrayList;
import java.util.List;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.FastMath;
import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;

/**
 * This class provides a custom camera similar in concept to the default jME
 * <code>FlyByCamera</code>. Differences include the ability to rotate or roll
 * the camera around the axis along which the camera is pointing. This class
 * also includes a more verbose interface for camera movement and rotation to
 * make it easier for external classes to directly manipulate the camera.
 * <p>
 * The following controls move the camera:
 * <ul>
 * <li>W, S, A, D : moves the camera forward, backward, left, right</li>
 * <li>Space, C : moves the camera up, down</li>
 * <li>Up/Down arrow keys : changes the pitch up, down</li>
 * <li>Left/Right arrow keys : changes the yaw left, right</li>
 * <li>Q, E : rolls the camera left, right</li>
 * <li>+, - : zooms in and out</li>
 * <li>Mouse wheel : zoom</li>
 * </ul>
 * </p>
 * 
 * @author Jordan
 * 
 */
public class FlightCamera {

	// TODO We might want to provide an easier way to map custom controls.

	/**
	 * The camera renderer. Its position should be set when moving the camera.
	 * Its axes should be set when rotating.
	 */
	private final Camera camera;

	/**
	 * The current up axis for the camera.
	 */
	private final Vector3f up;
	/**
	 * The current left axis for the camera.
	 */
	private final Vector3f left;
	/**
	 * The current direction in which the camera points.
	 */
	private final Vector3f dir;

	/**
	 * The current position or location of the camera.
	 */
	private final Vector3f position;

	/**
	 * The speed at which the camera moves forward, backward, and sideways.
	 */
	private float moveSpeed = 3f;
	/**
	 * The speed at which the camera rolls and rotates up, down, and sideways.
	 */
	private float rotationSpeed = 1f;
	/**
	 * The speed at which the camera zooms.
	 */
	private float zoomSpeed = 1f;

	/**
	 * A collection of all of the controls used by this camera.
	 */
	private final List<InputControl> controls;

	// ---- Mouse controls. ---- //
	/**
	 * The current up axis for the camera. This is used only when dragging the
	 * mouse so that changes in pitch combined with changes in yaw do not
	 * effectively roll the camera.
	 */
	private final Vector3f dragUp;
	/**
	 * Whether or not the camera's rotation is controlled by dragging the mouse.
	 */
	private boolean dragging;
	/**
	 * A collection of all mouse-drag related controls.
	 */
	private final List<InputControl> dragControls;
	// ------------------------- //

	// ---- Enabling and Registering Controls ---- //
	/**
	 * The <code>InputManager</code> that this camera's controls are registered
	 * with.
	 */
	private InputManager input;
	/**
	 * Whether or not the camera is enabled.
	 */
	private boolean enabled = false;
	/**
	 * Whether or not the camera is registered with the {@link #input} manager.
	 */
	private boolean registeredWithInput = false;

	// ------------------------------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param camera
	 *            The camera used for rendering (i.e., it determines when to
	 *            cull objects that are not in the view).
	 */
	public FlightCamera(Camera camera) {
		this.camera = camera;

		// Set the initial orientation.
		up = camera.getUp(new Vector3f());
		left = camera.getLeft(new Vector3f());
		dir = camera.getDirection(new Vector3f());
		// Get the initial position.
		position = camera.getLocation();

		// Create a separate up vector for mouse drag rotation.
		dragUp = up.clone();

		// Create the collections of controls.
		controls = new ArrayList<InputControl>();
		dragControls = new ArrayList<InputControl>();

		return;
	}

	/**
	 * Disposes of all resources associated with this camera.
	 */
	public void dispose() {
		// TODO
	}

	// ---- Camera movements and rotations ---- //
	/**
	 * Zooms the camera in or out.
	 * 
	 * @param value
	 *            The amount by which to zoom in or out. Negative zooms in,
	 *            positive zooms out.
	 */
	public void zoomCamera(float value) {
		// This method comes straight from FlyByCamera.

		// derive fovY value
		float h = camera.getFrustumTop();
		float w = camera.getFrustumRight();
		float aspect = w / h;

		float near = camera.getFrustumNear();

		float fovY = FastMath.atan(h / near) / (FastMath.DEG_TO_RAD * .5f);
		float newFovY = fovY + value * 0.1f * zoomSpeed;
		if (newFovY > 0f) {
			// Don't let the FOV go zero or negative.
			fovY = newFovY;
		}

		h = FastMath.tan(fovY * FastMath.DEG_TO_RAD * .5f) * near;
		w = h * aspect;

		camera.setFrustumTop(h);
		camera.setFrustumBottom(-h);
		camera.setFrustumLeft(-w);
		camera.setFrustumRight(w);

		return;
	}

	/**
	 * Moves the camera forward or backward.
	 * 
	 * @param distance
	 *            If positive, the camera moves forward. If negative, the camera
	 *            moves backward.
	 */
	public void thrustCamera(float distance) {
		Vector3f velocity = dir.mult(distance);
		camera.setLocation(position.addLocal(velocity));
	}

	/**
	 * Moves the camera right or left.
	 * 
	 * @param distance
	 *            If positive, the camera moves right. If negative, the camera
	 *            moves left.
	 */
	public void strafeCamera(float distance) {
		Vector3f velocity = left.mult(distance);
		// We have to subtract because we're using the left direction! Right
		// should be positive.
		camera.setLocation(position.subtractLocal(velocity));
	}

	/**
	 * Moves the camera up or down.
	 * 
	 * @param distance
	 *            If positive, the camera moves up. If negative, the camera
	 *            moves down.
	 */
	public void raiseCamera(float distance) {
		Vector3f velocity = up.mult(distance);
		camera.setLocation(position.addLocal(velocity));
	}

	/**
	 * Rotates (rolls) the camera right or left.
	 * 
	 * @param radians
	 *            If positive, the camera rolls right. If negative, the camera
	 *            rolls left.
	 */
	public void rollCamera(float radians) {
		rotateCamera(radians, dir);
		// When not dragging, we should update the up vector that is used
		// when dragging with the mouse. Note: The camera cannot be rolled when
		// dragging!
		dragUp.set(up);
	}

	/**
	 * Changes the pitch of the camera (rotates up and down).
	 * 
	 * @param radians
	 *            If positive, the camera pitches up. If negative, the camera
	 *            pitches down.
	 */
	public void pitchCamera(float radians) {
		rotateCamera(-radians, left);
		if (!dragging) {
			// When not dragging, we should update the up vector that is used
			// when dragging with the mouse.
			dragUp.set(up);
		}
	}

	/**
	 * Changes the yaw of the camera right or left.
	 * 
	 * @param radians
	 *            If positive, the camera rotates right. If negative, the camera
	 *            rotates left.
	 */
	public void yawCamera(float radians) {
		// If the mouse is controlling yaw, use the up vector for dragging.
		if (dragging) {
			rotateCamera(-radians, dragUp);
		} else {
			rotateCamera(-radians, up);
			// When not dragging, we should update the up vector that is used
			// when dragging with the mouse.
			dragUp.set(up);
		}
	}

	/**
	 * Rotates the camera based on the provided angle and axis. {@link #up},
	 * {@link #left}, and {@link #dir} are all updated in this method.
	 * 
	 * @param radians
	 *            The angle to rotate.
	 * @param axis
	 *            The axis to rotate around.
	 */
	private void rotateCamera(float radians, Vector3f axis) {
		Matrix3f matrix = new Matrix3f();
		matrix.fromAngleNormalAxis(radians, axis);

		// TODO We can probably shorten the number of calculations required by
		// tailoring the rotations for pitch, yaw, and roll. (The matrix should
		// always be roughly the same for each case.)

		// Apply the overall rotation to the direction vectors. Note that we do
		// not update the dragUp vector, since it should not be updated when
		// using the mouse drag to rotate.
		matrix.multLocal(up).normalizeLocal();
		matrix.multLocal(left).normalizeLocal();
		matrix.multLocal(dir).normalizeLocal();

		camera.setAxes(left, up, dir);
	}

	// ---------------------------------------- //

	/**
	 * Sets the position of the camera at once, as opposed to incremental
	 * positioning.
	 * 
	 * @param position
	 *            The new position. If null, an exception is thrown.
	 * 
	 * @see #thrustCamera(float)
	 * @see #strafeCamera(float)
	 * @see #raiseCamera(float)
	 */
	public void setPosition(Vector3f position) {
		// Check for nulls first.
		if (position == null) {
			throw new IllegalArgumentException("FlightCamera error: "
					+ "Null arguments not accepted for positioning the camera.");
		}

		// Update the local position and the camera.
		camera.setLocation(this.position.set(position));

		return;
	}

	/**
	 * Sets the orientation of the camera at once, as opposed to incremental
	 * orientation.
	 * 
	 * @param direction
	 *            The new direction in which the camera will point. If null, an
	 *            exception is thrown.
	 * @param up
	 *            The new up direction. If null or if it is not orthogonal to
	 *            the camera direction, an exception is thrown.
	 * 
	 * @see #rollCamera(float)
	 * @see #pitchCamera(float)
	 * @see #yawCamera(float)
	 */
	public void setOrientation(Vector3f direction, Vector3f up) {
		// Check for nulls first.
		if (direction == null || up == null) {
			throw new IllegalArgumentException("FlightCamera error: "
					+ "Null arguments not accepted for orienting the camera.");
		}
		// Make sure the direction and up vectors are orthogonal.
		else if (FastMath.abs(direction.dot(up)) > 1e-5f
				|| direction.equals(up)) {
			throw new IllegalArgumentException("FlightCamera error: "
					+ "Direction and up vector are not orthogonal.");
		}

		// Update the local orientation vectors. Since this is a right-handed
		// system, get the left vector by crossing up with the direction.
		this.up.set(up).cross(this.dir.set(direction), this.left);

		// We also need to set the up vector for dragging, otherwise trying to
		// drag to rotate will use the old dragUp vector for yaw rotation.
		dragUp.set(up);

		// Update the camera itself.
		camera.setAxes(this.left, this.up, this.dir);

		return;
	}

	// ---- Enabling and Registering Controls ---- //
	/**
	 * Gets whether or not the camera is enabled.
	 * 
	 * @return Whether or not the camera is enabled.
	 */
	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * Enables or disables the camera. If the camera is enabled and has an
	 * <code>InputManager</code>, then this method registers the camera controls
	 * with said <code>InputManager</code>. Likewise, if the camera is disabled
	 * and has an <code>InputManager</code>, this method unregisters the camera
	 * controls.
	 * 
	 * @param enabled
	 *            If true, the camera is enabled. If false, the camera is
	 *            disabled.
	 */
	public void setEnabled(boolean enabled) {

		if (enabled != this.enabled) {
			this.enabled = enabled;

			// If newly enabled and not registered with the InputManager,
			// register the controls.
			if (enabled && !registeredWithInput) {
				registerWithInput();
			}
			// If newly disabled and registered with the InputManager,
			// unregister the controls.
			else if (!enabled && registeredWithInput) {
				unregisterFromInput();
			}
		}

		return;
	}

	/**
	 * Sets the <code>InputManager</code> associated with this camera. If the
	 * camera is already enabled, this method unregisters from the previous
	 * <code>InputManager</code> and registers with the new one.
	 * 
	 * @param input
	 *            The new <code>InputManager</code>.
	 */
	public void setInputManager(InputManager input) {

		if (input != null && input != this.input) {
			// If registered with the old input, unregister from it.
			if (registeredWithInput) {
				unregisterFromInput();
			}

			// Set the new InputManager.
			this.input = input;

			// If necessary, register with the new InputManager.
			if (enabled) {
				registerWithInput();
			}
		}

		return;
	}

	/**
	 * Registers the <code>FlightCamera</code> with the {@link #input} manager.
	 */
	private void registerWithInput() {

		if (input != null && !registeredWithInput) {
			// We have now begun registering with the InputManager.
			registeredWithInput = true;

			// Create the controls if necessary so they can be registered.
			if (controls.isEmpty()) {
				createControls();
			}
			if (dragControls.isEmpty()) {
				createDragControls();
			}

			// Register all the controls.
			for (InputControl control : controls) {
				control.registerWithInput(input);
			}
			for (InputControl control : dragControls) {
				control.registerWithInput(input);
			}
		}

		return;
	}

	/**
	 * Unregisters the <code>FlightCamera</code> from the {@link #input}
	 * manager.
	 */
	private void unregisterFromInput() {

		if (input != null && registeredWithInput) {

			// Unregister all controls.
			for (InputControl control : controls) {
				control.unregisterFromInput();
			}
			for (InputControl control : dragControls) {
				control.unregisterFromInput();
			}

			// We are no longer registered with the InputManager.
			registeredWithInput = false;
		}

		return;
	}

	/**
	 * Creates all controls (listeners, mappings, and triggers) and inserts them
	 * into the list of {@link #controls}.
	 */
	private void createControls() {

		InputControl control;
		AnalogListener analog;
		String mapping;

		// ---- Standard Movement Controls ---- //
		// Thrust forward.
		mapping = "thrustForward";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				thrustCamera(value * moveSpeed);
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_W));
		controls.add(control);

		// Thrust backward.
		mapping = "thrustBackward";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				thrustCamera(-value * moveSpeed);
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_S));
		controls.add(control);

		// Strafe right.
		mapping = "strafeRight";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				strafeCamera(value * moveSpeed);
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_D));
		controls.add(control);

		// Strafe left.
		mapping = "strafeLeft";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				strafeCamera(-value * moveSpeed);
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_A));
		controls.add(control);

		// Raise.
		mapping = "raise";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				raiseCamera(value * moveSpeed);
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_SPACE));
		controls.add(control);

		// Lower.
		mapping = "lower";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				raiseCamera(-value * moveSpeed);
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_C));
		controls.add(control);
		// ------------------------------------ //

		// ---- Flight Rotation Controls ---- //
		// Roll right.
		mapping = "rollRight";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (!dragging) {
					rollCamera(value * rotationSpeed);
				}
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_E));
		controls.add(control);

		// Roll left.
		mapping = "rollLeft";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (!dragging) {
					rollCamera(-value * rotationSpeed);
				}
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_Q));
		controls.add(control);

		// Pitch up.
		mapping = "pitchUp";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (!dragging) {
					pitchCamera(value * rotationSpeed);
				}
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_UP));
		controls.add(control);

		// Pitch down.
		mapping = "pitchDown";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (!dragging) {
					pitchCamera(-value * rotationSpeed);
				}
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_DOWN));
		controls.add(control);

		// Yaw right.
		mapping = "yawRight";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (!dragging) {
					yawCamera(value * rotationSpeed);
				}
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_RIGHT));
		controls.add(control);

		// Yaw left.
		mapping = "yawLeft";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (!dragging) {
					yawCamera(-value * rotationSpeed);
				}
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_LEFT));
		controls.add(control);
		// ---------------------------------- //

		// ---- Zoom Controls ---- //
		// Zoom in
		mapping = "zoomIn";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				zoomCamera(-value * rotationSpeed);
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_EQUALS),
				new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
		controls.add(control);

		// Zoom out
		mapping = "zoomOut";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				zoomCamera(value * rotationSpeed);
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new KeyTrigger(KeyInput.KEY_SUBTRACT),
				new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
		controls.add(control);
		// ----------------------- //

		return;
	}

	/**
	 * Creates controls that handle mouse drags to rotate. The mouse can only
	 * control pitch and yaw when dragged.
	 */
	private void createDragControls() {

		InputControl control;
		ActionListener action;
		AnalogListener analog;
		String mapping;

		// Mouse left click enables mouse rotation by dragging.
		mapping = "mouseDrag";
		action = new ActionListener() {
			@Override
			public void onAction(String name, boolean isPressed, float tpf) {
				dragging = isPressed;
				input.setCursorVisible(!dragging);
			}
		};
		control = new InputControl(action, mapping);
		control.addTriggers(mapping, new MouseButtonTrigger(
				MouseInput.BUTTON_LEFT));
		dragControls.add(control);

		// Pitch up.
		mapping = "mousePitchUp";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (dragging) {
					pitchCamera(value * rotationSpeed);
				}
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new MouseAxisTrigger(MouseInput.AXIS_Y,
				false));
		dragControls.add(control);

		// Pitch down.
		mapping = "mousePitchDown";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (dragging) {
					pitchCamera(-value * rotationSpeed);
				}
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new MouseAxisTrigger(MouseInput.AXIS_Y,
				true));
		dragControls.add(control);

		// Yaw right.
		mapping = "mouseYawRight";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (dragging) {
					yawCamera(value * rotationSpeed);
				}
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new MouseAxisTrigger(MouseInput.AXIS_X,
				false));
		dragControls.add(control);

		// Yaw left.
		mapping = "mouseYawLeft";
		analog = new AnalogListener() {
			@Override
			public void onAnalog(String name, float value, float tpf) {
				if (dragging) {
					yawCamera(-value * rotationSpeed);
				}
			}
		};
		control = new InputControl(analog, mapping);
		control.addTriggers(mapping, new MouseAxisTrigger(MouseInput.AXIS_X,
				true));
		dragControls.add(control);

		return;
	}

	// ------------------------------------------- //

	// ---- Configuration Getters/Setters ---- //
	/**
	 * Sets the rate at which the camera moves. The default value is
	 * <code>3.0</code>.
	 * 
	 * @param moveSpeed
	 *            The new move speed. This should not be negative.
	 */
	public void setMoveSpeed(float moveSpeed) {
		if (moveSpeed > 0f) {
			this.moveSpeed = moveSpeed;
		}
	}

	/**
	 * Sets the rate at which the camera rotates. The default value is
	 * <code>1.0</code>.
	 * 
	 * @param rotationSpeed
	 *            The new rotation speed. This should not be negative.
	 */
	public void setRotationSpeed(float rotationSpeed) {
		if (rotationSpeed > 0f) {
			this.rotationSpeed = rotationSpeed;
		}
	}

	/**
	 * Sets the rate at which the camera zooms. The default value is
	 * <code>1.0</code>.
	 * 
	 * @param zoomSpeed
	 *            The new zoom speed. This should not be negative.
	 */
	public void setZoomSpeed(float zoomSpeed) {
		if (zoomSpeed > 0f) {
			this.zoomSpeed = zoomSpeed;
		}
	}
	// --------------------------------------- //
}
