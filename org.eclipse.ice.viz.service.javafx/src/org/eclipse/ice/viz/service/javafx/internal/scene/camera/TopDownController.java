/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.internal.scene.camera;

import javafx.embed.swt.FXCanvas;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

/**
 * <p>
 * ArcBallController provides 3D editor like features to a camera, by rotating
 * around a point and letting the user zoom in and out.
 * </p>
 */
public class TopDownController extends AbstractCameraController {

	/**
	 * The collected transformation for all movements applied to the camera
	 */
	Affine affine = new Affine();

	public TopDownController(Camera camera, Scene scene, FXCanvas canvas) {
		super(camera, scene, canvas);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#handleKeyPressed(javafx.scene.input.KeyEvent)
	 */
	@Override
	public void handleKeyPressed(KeyEvent event) {

		double speed = NORMAL_SPEED;

		if (event.isShiftDown()) {
			speed = FAST_SPEED;
		}

		KeyCode keyCode = event.getCode();

		Transform worldTransform = xform.getLocalToSceneTransform();
		double yx = worldTransform.getMyx();
		double yy = worldTransform.getMyy();
		double yz = worldTransform.getMyz();

		double xx = worldTransform.getMxx();
		double xy = worldTransform.getMxy();
		double xz = worldTransform.getMxz();

		Point3D yDir = new Point3D(yx, yy, yz).normalize();
		Point3D xDir = new Point3D(xx, xy, xz).normalize();

		if (keyCode == KeyCode.W) {
			Point3D moveVec = yDir.multiply(speed);
			affine.appendTranslation(moveVec.getX(), -moveVec.getY(),
					moveVec.getZ());
		} else if (keyCode == KeyCode.S) {
			Point3D moveVec = yDir.multiply(speed);
			Point3D invVec = new Point3D(-moveVec.getX(), moveVec.getY(),
					-moveVec.getZ());
			affine.appendTranslation(invVec.getX(), invVec.getY(),
					invVec.getZ());
		} else if (keyCode == KeyCode.A) {
			Point3D moveVec = xDir.multiply(speed);
			affine.appendTranslation(-moveVec.getX(), -moveVec.getY(),
					-moveVec.getZ());
		} else if (keyCode == KeyCode.D) {
			Point3D moveVec = xDir.multiply(speed);
			affine.appendTranslation(moveVec.getX(), moveVec.getY(),
					moveVec.getZ());
		}

		if (keyCode == KeyCode.SPACE) {
			camera.setTranslateZ(camera.getTranslateZ() - speed);
		} else if (keyCode == KeyCode.C) {
			camera.setTranslateZ(camera.getTranslateZ() + speed);
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
		double z = affine.getTz();
		double deltaZ = event.getDeltaY() * .1;

		// Prevent the user from zooming too close to the plane of the x
		// and y axis
		if (z + deltaZ <= -15) {
			affine.appendTranslation(0, 0, event.getDeltaY() * .1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#initCamera()
	 */
	@Override
	protected void initCamera() {
		super.initCamera();

		// Reset the camera to its initial position
		reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#reset()
	 */
	@Override
	public void reset() {

		// Create a new affine transformation centered above the origin and
		// replace the camera's current transformation(s) with it
		affine = new Affine();
		affine.appendTranslation(0, 0, -85);
		xform.getTransforms().setAll(affine);

	}

}
