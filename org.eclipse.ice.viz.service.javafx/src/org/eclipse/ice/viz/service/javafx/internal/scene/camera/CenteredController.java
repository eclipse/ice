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
import javafx.scene.transform.Rotate;

/**
 * <p>
 * ArcBallController provides 3D editor like features to a camera, by rotating
 * around a point and letting the user zoom in and out.
 * </p>
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

	// /**
	// * The default X coordinate for the camera.
	// */
	// private double defaultXPosition;
	//
	// /**
	// * The default amount of X rotation to apply to the camera.
	// */
	// private double defaultXRotation;
	//
	// private double defaultYRotation;
	//
	// private double

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

		// scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
		// @Override
		// public void handle(KeyEvent event) {
		//
		// double speed = NORMAL_SPEED;
		//
		// if (event.isShiftDown()) {
		// speed = FAST_SPEED;
		// }
		//
		// KeyCode keyCode = event.getCode();
		//
		// Transform worldTransform = xform.getLocalToSceneTransform();
		// double zx = worldTransform.getMzx();
		// double zy = worldTransform.getMzy();
		// double zz = worldTransform.getMzz();
		//
		// double xx = worldTransform.getMxx();
		// double xy = worldTransform.getMxy();
		// double xz = worldTransform.getMxz();
		//
		// Point3D zDir = new Point3D(zx, zy, zz).normalize();
		// Point3D xDir = new Point3D(xx, xy, xz).normalize();
		//
		// if (keyCode == KeyCode.W) {
		// Point3D moveVec = zDir.multiply(speed);
		// affine.appendTranslation(moveVec.getX(), moveVec.getY(),
		// moveVec.getZ());
		// } else if (keyCode == KeyCode.S) {
		// Point3D moveVec = zDir.multiply(speed);
		// Point3D invVec = new Point3D(-moveVec.getX(),
		// -moveVec.getY(), -moveVec.getZ());
		// affine.appendTranslation(invVec.getX(), invVec.getY(),
		// invVec.getZ());
		// } else if (keyCode == KeyCode.A) {
		// Point3D moveVec = xDir.multiply(speed);
		// affine.appendTranslation(-moveVec.getX(), -moveVec.getY(),
		// -moveVec.getZ());
		// } else if (keyCode == KeyCode.D) {
		// Point3D moveVec = xDir.multiply(speed);
		// affine.appendTranslation(moveVec.getX(), moveVec.getY(),
		// moveVec.getZ());
		// }
		//
		// if (keyCode == KeyCode.SPACE) {
		// finalCamera
		// .setTranslateZ(finalCamera.getTranslateZ() - speed);
		// } else if (keyCode == KeyCode.C) {
		// finalCamera
		// .setTranslateZ(finalCamera.getTranslateZ() + speed);
		// }
		// }
		// });

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
	 * AbstractCameraController#handleMousePressed(javafx.scene.input.
	 * MouseEvent)
	 */
	@Override
	public void handleMousePressed(MouseEvent event) {

		// Get the mouse position for the start of the drag
		mousePosX = event.getSceneX();
		mousePosY = event.getSceneY();
		mouseOldX = event.getSceneX();
		mouseOldY = event.getSceneY();
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
	 * AbstractCameraController#initCamera()
	 */
	@Override
	public void initCamera() {
		super.initCamera();

		// Reset the camera to its default position
		reset();
	}

	@Override
	public void reset() {

		// Reset the camera to its initial angles
		x.setAngle(0);
		y.setAngle(0);

		// Zoom the camera back to a default distance from the origin.
		camera.setTranslateZ(-2000);

	}

}
