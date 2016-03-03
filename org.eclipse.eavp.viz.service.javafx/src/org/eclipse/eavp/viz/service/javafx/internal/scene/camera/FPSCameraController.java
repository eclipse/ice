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
package org.eclipse.eavp.viz.service.javafx.internal.scene.camera;

import javafx.embed.swt.FXCanvas;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

/**
 * <p>
 * ArcBallController provides 3D editor like features to a camera, by rotating
 * around a point and letting the user zoom in and out.
 * </p>
 */
public class FPSCameraController extends BasicCameraController {

	/**
	 * A combination of all transformations applied to the camera node.
	 */
	Affine affine;

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
	public FPSCameraController(Camera camera, Scene scene, FXCanvas canvas) {
		super(camera, scene, canvas);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
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
		double zx = worldTransform.getMzx();
		double zy = worldTransform.getMzy();
		double zz = worldTransform.getMzz();

		double xx = worldTransform.getMxx();
		double xy = worldTransform.getMxy();
		double xz = worldTransform.getMxz();

		Point3D zDir = new Point3D(zx, zy, zz).normalize();
		Point3D xDir = new Point3D(xx, xy, xz).normalize();

		if (keyCode == KeyCode.W) {
			Point3D moveVec = zDir.multiply(speed);
			affine.appendTranslation(moveVec.getX(), moveVec.getY(),
					moveVec.getZ());
		} else if (keyCode == KeyCode.S) {
			Point3D moveVec = zDir.multiply(speed);
			Point3D invVec = new Point3D(-moveVec.getX(), -moveVec.getY(),
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
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
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
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
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
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
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
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#initCamera()
	 */
	@Override
	public void initCamera() {
		// Reset the camera to its default position
		reset();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#reset()
	 */
	@Override
	public void reset() {

		// Reset the camera to its initial angles
		x.setAngle(0);
		y.setAngle(0);

		// Create an empty affine transformation, add the default angles to it,
		// and replace the node's transformations with it
		affine = new Affine();
		affine.append(defaultX);
		affine.append(defaultY);
		affine.append(defaultZ);
		xform.getTransforms().setAll(affine);

		// Zoom the camera back to a default distance from the origin.
		camera.setTranslateZ(-2000);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#pitchCamera(double)
	 */
	@Override
	public void pitchCamera(double radians) {

		// Get the x direction for the camera's current heading
		Transform worldTransform = xform.getLocalToSceneTransform();
		double xx = worldTransform.getMxx();
		double xy = worldTransform.getMxy();
		double xz = worldTransform.getMxz();
		Point3D xDir = new Point3D(xx, xy, xz).normalize();

		// Create a new rotation along that axis and apply it to the camera
		Rotate rotation = new Rotate(radians * 180 / Math.PI, xDir);
		affine.append(rotation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#rollCamera(double)
	 */
	@Override
	public void rollCamera(double radians) {

		// Get the z direction of the camera's current heading
		Transform worldTransform = xform.getLocalToSceneTransform();
		double zx = worldTransform.getMzx();
		double zy = worldTransform.getMzy();
		double zz = worldTransform.getMzz();
		Point3D zDir = new Point3D(zx, zy, zz).normalize();

		// Create a new rotation along that axis and apply it to the camera
		Rotate rotation = new Rotate(radians * 180 / Math.PI, zDir);
		affine.append(rotation);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#strafeCamera(double)
	 */
	@Override
	public void raiseCamera(double distance) {
		camera.setTranslateZ(camera.getTranslateZ() - distance);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#strafeCamera(double)
	 */
	@Override
	public void strafeCamera(double distance) {

		Transform worldTransform = xform.getLocalToSceneTransform();

		double xx = worldTransform.getMxx();
		double xy = worldTransform.getMxy();
		double xz = worldTransform.getMxz();
		Point3D xDir = new Point3D(xx, xy, xz).normalize();

		Point3D moveVec = xDir.multiply(distance);
		affine.appendTranslation(moveVec.getX(), moveVec.getY(),
				moveVec.getZ());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#thrustCamera(double)
	 */
	@Override
	public void thrustCamera(double distance) {

		Transform worldTransform = xform.getLocalToSceneTransform();
		double zx = worldTransform.getMzx();
		double zy = worldTransform.getMzy();
		double zz = worldTransform.getMzz();

		Point3D zDir = new Point3D(zx, zy, zz).normalize();

		Point3D moveVec = zDir.multiply(distance);
		affine.appendTranslation(moveVec.getX(), moveVec.getY(),
				moveVec.getZ());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#yawCamera(double)
	 */
	@Override
	public void yawCamera(double radians) {

		// Get the y direction of the camera's current heading
		Transform worldTransform = xform.getLocalToSceneTransform();
		double yx = worldTransform.getMyx();
		double yy = worldTransform.getMyy();
		double yz = worldTransform.getMyz();
		Point3D yDir = new Point3D(yx, yy, yz).normalize();

		// Create a new rotation along that axis and apply it to the camera
		Rotate rotation = new Rotate(radians * 180 / Math.PI, yDir);
		affine.append(rotation);
	}

	@Override
	public void handleMouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub

	}

}
