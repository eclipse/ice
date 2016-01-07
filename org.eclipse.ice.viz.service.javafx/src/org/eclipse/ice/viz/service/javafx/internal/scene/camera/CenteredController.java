/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com)
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.internal.scene.camera;

import javafx.embed.swt.FXCanvas;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

/**
 * A camera centered around and gazing towards the origin. The user can rotate
 * the camera about the origin by dragging the mouse and zoom through scrolling
 * the mouse wheel.
 * 
 * @author Robert Smith
 *
 */
public class CenteredController extends AbstractCameraController {

	/**
	 * The X rotation applied to the camera.
	 */
	private Rotate x;

	/**
	 * The Y rotation applied to the camera.
	 */
	private Rotate y;

	/**
	 * The default constructor.
	 * 
	 * @param camera
	 *            The camera this controller will manage.
	 * @param scene
	 *            The scene the camera is viewing.
	 * @param canvas
	 *            The FXCanvas containing the scene.
	 */
	public CenteredController(Camera camera, Scene scene, FXCanvas canvas) {
		super(camera, scene, canvas);

		// Set the x axis rotation for the affine transformation
		x = new Rotate();
		x.setAxis(Rotate.X_AXIS);
		xform.getTransforms().add(x);

		// Set the y axis rotation for the affine transformation
		y = new Rotate();
		y.setAxis(Rotate.Y_AXIS);

		// Apply the rotations and the affine to the camera
		xform.getTransforms().setAll(x, y, affine);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#handleMouseDragged(javafx.scene.input.
	 * MouseEvent)
	 */
	@Override
	public void handleMouseDragged(MouseEvent event) {

		// Replace the old mouse position
		mouseOldX = mousePosX;
		mouseOldY = mousePosY;

		// Get the current mouse position and calculate the deltas
		mousePosX = event.getSceneX();
		mousePosY = event.getSceneY();
		mouseDeltaX = (mousePosX - mouseOldX);
		mouseDeltaY = (mousePosY - mouseOldY);

		// Apply the change in mouse position to the camera's angle
		if (event.isPrimaryButtonDown()) {

			y.setAngle(y.getAngle() - mouseDeltaX);
			x.setAngle(x.getAngle() + mouseDeltaY);

		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ice.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#handleMouseScroll(javafx.scene.input.
	 * ScrollEvent)
	 */
	@Override
	public void handleMouseScroll(ScrollEvent event) {
		// Get the current z position and modify it by the amount of
		// scrolling
		double z = camera.getTranslateZ();
		double newZ = z + event.getDeltaY();
		camera.setTranslateZ(newZ);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#reset()
	 */
	@Override
	public void reset() {

		// Handle the rotations if they exist
		if (x != null) {
			// Reset the camera to its initial angles
			x.setAngle(0);
			y.setAngle(0);
		}

		// Zoom the camera back to a default distance from the origin.
		affine = new Affine();
		affine.appendTranslation(0, 0, -2000);

		// If x and y exist, apply them to the camera
		if (x != null) {
			xform.getTransforms().setAll(x, y, affine);
		}

		// Otherwise only apply the affine to the camera
		else {
			xform.getTransforms().setAll(affine);
		}

	}

}
