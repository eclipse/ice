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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.jme3.input.ChaseCamera;
import com.jme3.input.InputManager;
import com.jme3.renderer.Camera;
import com.jme3.scene.Spatial;

/**
 * This class extends the default jME3 {@link ChaseCamera} to provide additional
 * functionality:<br>
 * <br>
 * 1) Can reset the zoom to a default distance.<br>
 * 2) Notifies {@link ICameraListener}s when the zoom changes.<br>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CustomChaseCamera extends ChaseCamera {

	/**
	 * A List of {@link ICameraListener}s who should be notified when important
	 * camera events occur.
	 */
	private final List<ICameraListener> cameraListeners;

	/**
	 * The camera's default zoom or distance from the target.
	 */
	private float defaultDistance;

	/**
	 * The default constructor.
	 * 
	 * @param cam
	 *            The jME3 application's {@link Camera}.
	 * @param target
	 *            The {@link Spatial} that the camera should target or face.
	 * @param inputManager
	 *            The jME3 application's {@link InputManager}.
	 */
	public CustomChaseCamera(Camera cam, Spatial target,
			InputManager inputManager) {
		super(cam, target, inputManager);

		// Set the default distance.
		defaultDistance = 10f;

		// Initialize the list of ICameraListeners.
		cameraListeners = new ArrayList<ICameraListener>(5);

		// Disable all rotation. These must be set to false so the camera is
		// "locked" in position above the player.
		canRotate = false;
		dragToRotate = false;

		return;
	}

	@Override
	public void setDragToRotate(boolean dragToRotate) {
		this.dragToRotate = false;
		canRotate = false;
	}

	/**
	 * Registers the specified listener with the camera so that it will be
	 * notified of camera events. No duplicates are allowed in the list.
	 * 
	 * @param listener
	 *            The {@link ICameraListener} to add.
	 * @return True if the camera listener was added, false otherwise.
	 */
	public boolean addCameraListener(ICameraListener listener) {

		// Check whether the input is null.
		boolean canAdd = (listener != null);

		// Check whether the list already contains a reference to the same
		// listener.
		if (canAdd) {
			for (ICameraListener currentListener : cameraListeners) {
				if (currentListener == listener) {
					canAdd = false;
					break;
				}
			}
		}

		// If possible, add the listener.
		if (canAdd) {
			cameraListeners.add(listener);
		}
		return canAdd;
	}

	/**
	 * Unregisters the specified listener from the camera so that it will no
	 * longer be notified of camera events. No duplicates are allowed in the
	 * list.
	 * 
	 * @param listener
	 *            The {@link ICameraListener} to remove.
	 * @return True if the camera listener was removed, false otherwise.
	 */
	public boolean removeCameraListener(ICameraListener listener) {

		boolean removed = false;

		// Only remove the first reference that matches the specified
		// listener. There should be at most 1 such listener in the list.
		Iterator<ICameraListener> iter;
		for (iter = cameraListeners.iterator(); iter.hasNext();) {
			if (iter.next() == listener) {
				iter.remove();
				removed = true;
				break;
			}
		}

		return removed;
	}

	/**
	 * Creates a separate thread that notifies registered camera listeners that
	 * the camera's distance to the target has changed.
	 * 
	 * @param distance
	 *            The camera's new distance to the target.
	 */
	private void notifyCameraListeners(final float distance) {
		// Start a thread to notify listeners that the zoom has changed.
		Thread notifier = new Thread() {
			@Override
			public void run() {
				// Loop over all listeners and update them.
				for (ICameraListener listener : cameraListeners) {
					listener.zoomChanged(distance);
				}
			}
		};
		notifier.start();

		return;
	}

	/**
	 * Overrides the default behavior to restrict the default distance between
	 * the min and max distances and to store the current default distance.
	 */
	@Override
	public void setDefaultDistance(float defaultDistance) {
		// Only set the default distance if it is valid.
		if (defaultDistance >= getMinDistance()
				&& defaultDistance <= getMaxDistance()) {

			// Call the super method, but also record the default distance.
			super.setDefaultDistance(defaultDistance);
			this.defaultDistance = defaultDistance;
		}
		return;
	}

	/**
	 * Resets the camera's distance from the target to the default value.
	 */
	public void resetZoom() {
		targetDistance = defaultDistance;
		distance = defaultDistance;
	}

	/**
	 * Overrides the default behavior to notify camera listeners that the
	 * distance to the target has changed.
	 */
	@Override
	protected void zoomCamera(float value) {
		// Call the super method, but also notify listeners of the new distance
		// to the target.
		super.zoomCamera(value);
		notifyCameraListeners(targetDistance);
	}

	/**
	 * Unregisters the camera from the application's {@link InputManager}.
	 */
	public void unregisterInput() {

		// Remove all of the input mappings.
		inputManager.deleteMapping(ChaseCamDown);
		inputManager.deleteMapping(ChaseCamUp);
		inputManager.deleteMapping(ChaseCamMoveLeft);
		inputManager.deleteMapping(ChaseCamMoveRight);
		inputManager.deleteMapping(ChaseCamZoomIn);
		inputManager.deleteMapping(ChaseCamZoomOut);
		inputManager.deleteMapping(ChaseCamToggleRotate);

		// Unregister from the InputManager so key/mouse input events are not
		// received.
		inputManager.removeListener(this);

		return;
	}

}
