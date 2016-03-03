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
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Camera;
import javafx.scene.Scene;
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
public class ArcBallCameraController extends BasicCameraController {

	/** */
	protected Transform xform;

	/** */
	protected Affine transform;

	/** */
	private Camera camera;

	/** */
	private Scene scene;

	/** */
	double anchorX;

	/** */
	double anchorY;

	/** */
	double anchorAngle;

	/** */
	private double sphereRadius;

	/** */
	private double height;

	/** */
	private double width;

	/** */
	protected Point3D currentRot;

	/** */
	private Point3D startRot;

	/** */
	protected boolean activeRotation;

	/** */
	private FXCanvas canvas;

	/**
	 * <p>
	 * </p>
	 */
	public ArcBallCameraController(Camera camera, Scene scene,
			FXCanvas canvas) {
		super(camera, scene, canvas);

		final Camera finalCamera = camera;
		final Scene finalScene = scene;

		scene.setOnScroll(new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				double translateX = -finalCamera.getTranslateX();
				double translateY = -finalCamera.getTranslateY();
				double translateZ = -finalCamera.getTranslateZ();

				double deltaY = event.getDeltaY();

				// The direction the camera is facing
				Point3D zVec = new Point3D(translateX, translateY, translateZ);

				// Normalized version that can be scaled
				Point3D normalize = zVec.normalize();

				double zoomSpeed = 50.0;

				// Final zoom scaling coefficient
				Point3D scaledMovementCof = normalize.multiply(zoomSpeed);

				double currentX = finalCamera.getTranslateX();
				double currentY = finalCamera.getTranslateY();
				double currentZ = finalCamera.getTranslateZ();

				double zoomX = scaledMovementCof.getX();
				double zoomY = scaledMovementCof.getY();
				double zoomZ = scaledMovementCof.getZ();

				if (deltaY < 0) {
					finalCamera.setTranslateX(currentX + zoomX);
					finalCamera.setTranslateY(currentY + zoomY);
					finalCamera.setTranslateZ(currentZ + zoomZ);
				} else {
					finalCamera.setTranslateX(currentX - zoomX);
					finalCamera.setTranslateY(currentY - zoomY);
					finalCamera.setTranslateZ(currentZ - zoomZ);
				}
			}
		});

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				width = finalScene.getWidth();
				height = finalScene.getHeight();

				sphereRadius = Math.min(width / 2.0d, height / 2.0d);

				double startX = arg0.getSceneX()
						- (finalScene.getWidth() / 2.0d);
				double startY = (finalScene.getHeight() / 2.0d)
						- arg0.getSceneY();

				startRot = CamUtil.pointToSphere(-startX, startY, sphereRadius)
						.normalize();
				currentRot = startRot;

				activeRotation = true;
			}

		});

		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				activeRotation = false;
			}

		});

		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				double dragX = arg0.getSceneX()
						- (finalScene.getWidth() / 2.0d);
				double dragY = (finalScene.getHeight() / 2.0d)
						- arg0.getSceneY();

				currentRot = CamUtil.pointToSphere(-dragX, dragY, sphereRadius)
						.normalize();

				Point3D rotationAxis = currentRot.crossProduct(startRot)
						.normalize();

				double dotProduct = currentRot.dotProduct(startRot);

				if (dotProduct > 1 - 1E-10) {
					dotProduct = 1.0;
				}

				double angle = Math.acos(dotProduct) * 180.0f / Math.PI;

				Point3D invRotAxis = new Point3D(-rotationAxis.getX(),
						-rotationAxis.getY(), -rotationAxis.getZ());

				Point3D pivot = new Point3D(0, 0, 0);
				Rotate rotation = new Rotate(angle * 0.1d, pivot.getX(),
						pivot.getY(), pivot.getZ(), invRotAxis);

				if (finalCamera.getTransforms().size() > 0) {
					Transform totalRot = finalCamera.getTransforms().get(0)
							.createConcatenation(rotation);
					finalCamera.getTransforms().setAll(totalRot);
				} else {
					finalCamera.getTransforms().add(rotation);
				}

				double translateX = finalCamera.getTranslateX();
				double translateY = finalCamera.getTranslateY();
				double translateZ = finalCamera.getTranslateZ();

				Affine lookAt = CamUtil.lookAt(new Point3D(0, 0, 0),
						new Point3D(translateX, translateY, translateZ),
						new Point3D(0, 1, 0));

				finalCamera.getTransforms().add(lookAt);
			}
		});

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initCamera() {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleKeyPressed(KeyEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMouseDragged(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMousePressed(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMouseReleased(MouseEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void handleMouseScroll(ScrollEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pitchCamera(double radians) {
		// TODO Auto-generated method stub

	}

	@Override
	public void rollCamera(double radians) {
		// TODO Auto-generated method stub

	}

	@Override
	public void raiseCamera(double distance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void strafeCamera(double distance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void thrustCamera(double distance) {
		// TODO Auto-generated method stub

	}

	@Override
	public void yawCamera(double radians) {
		// TODO Auto-generated method stub

	}

}
