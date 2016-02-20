/*******************************************************************************
 * Copyright (c) 2015-2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.internal.scene.camera;

import javafx.embed.swt.FXCanvas;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Transform;

/**
 * A controller which sets the camera to a top down view, with the camera
 * pointed down to look perpendicular to the XY plane.
 */
public class TopDownCameraController extends AbstractCameraController {

	/**
	 * The default constructor.
	 * 
	 * @param camera
	 *            The camera this controller will manage.
	 * @param scene
	 *            The scene the camera is viewing.
	 * @param canvas
	 *            The FXCanvas displaying the scene.
	 */
	public TopDownCameraController(Camera camera, Scene scene,
			FXCanvas canvas) {
		super(camera, scene, canvas);
	}

	/**
	 * Set a node to move along with the camera, so that it is always displayed
	 * in the same part of the screen.
	 * 
	 * @param node
	 *            The node whose position will be set relative to the camera.
	 */
	public void fixToCamera(Node node) {
		xform.getChildren().add(node);
	}

	/**
	 * Move the camera along the Z axis by the given distance.
	 * 
	 * @param deltaZ
	 *            The amount to move the camera by. Positive values move it in
	 *            towards the origin, negative values move it back away from the
	 *            origin.
	 */
	public void zoomCamera(double deltaZ) {

		// Prevent the user from zooming too close to the plane of the x
		// and y axis
		if (affine.getTz() + deltaZ <= -15) {
			affine.appendTranslation(0, 0, deltaZ);
		}
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
		double yx = worldTransform.getMyx();
		double yy = worldTransform.getMyy();
		double yz = worldTransform.getMyz();

		double xx = worldTransform.getMxx();
		double xy = worldTransform.getMxy();
		double xz = worldTransform.getMxz();

		Point3D yDir = new Point3D(yx, yy, yz).normalize();
		Point3D xDir = new Point3D(xx, xy, xz).normalize();

		// W moves the camera up
		if (keyCode == KeyCode.W || keyCode == KeyCode.UP) {
			Point3D moveVec = yDir.multiply(speed);
			affine.appendTranslation(moveVec.getX(), -moveVec.getY(),
					moveVec.getZ());

			// S moves the camera down
		} else if (keyCode == KeyCode.S || keyCode == KeyCode.DOWN) {
			Point3D moveVec = yDir.multiply(speed);
			Point3D invVec = new Point3D(-moveVec.getX(), moveVec.getY(),
					-moveVec.getZ());
			affine.appendTranslation(invVec.getX(), invVec.getY(),
					invVec.getZ());

			// A moves the camera to the left
		} else if (keyCode == KeyCode.A || keyCode == KeyCode.LEFT) {
			Point3D moveVec = xDir.multiply(speed);
			affine.appendTranslation(-moveVec.getX(), -moveVec.getY(),
					-moveVec.getZ());

			// D moves the camera to the right
		} else if (keyCode == KeyCode.D || keyCode == KeyCode.RIGHT) {
			Point3D moveVec = xDir.multiply(speed);
			affine.appendTranslation(moveVec.getX(), moveVec.getY(),
					moveVec.getZ());
		}

		// The spacebar zooms the camera out
		if (keyCode == KeyCode.SPACE) {
			zoomCamera(-speed);

			// C zooms the camera in
		} else if (keyCode == KeyCode.C) {
			zoomCamera(speed);
		}
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

		// Zoom the camera based on the amount and direction of
		// scrolling
		zoomCamera(event.getDeltaY() * .1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * AbstractCameraController#reset()
	 */
	@Override
	public void reset() {

		// Create a new affine transformation, set it to the default position,
		// and apply it to the camera
		affine = new Affine();
		affine.appendTranslation(0, 0, -85);
		xform.getTransforms().setAll(affine);
	}

}
