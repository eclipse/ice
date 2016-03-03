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
 * Turntable provides 3D editor like features to a camera, by rotating around a
 * point and letting the user zoom in and out.
 * </p>
 */
public class TurntableCameraController extends BasicCameraController {

	/** */
	Transform xform;

	/** */
	Affine transform;

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

	/** */
	private double startX;

	/** */
	private double startY;

	/**
	 * <p>
	 * </p>
	 */
	public TurntableCameraController(Camera camera, Scene scene,
			FXCanvas canvas) {
		super(camera, scene, canvas);

		final Camera finalCamera = camera;
		final Scene finalScene = scene;

		final Affine affine = new Affine();

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				width = finalScene.getWidth();
				height = finalScene.getHeight();

				startX = arg0.getSceneX();
				startY = arg0.getSceneY();

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

				try {
					Point3D yAxis = new Point3D(0.0d, -1.0d, 0.0d);
					Affine transform = affine;
					Affine createInverse = transform.createInverse();

					Point3D zInv = new Point3D(createInverse.getMzx(),
							createInverse.getMzy(), createInverse.getMzz());
					Point3D xInv = new Point3D(createInverse.getMxx(),
							createInverse.getMxy(), createInverse.getMxz());

					Point3D subtract = yAxis.subtract(zInv);
					double invSqr = subtract.dotProduct(subtract);

					Point3D xAxis = null;

					if (invSqr > 0.0001f) {
						xAxis = yAxis.crossProduct(zInv);

						double xDot = xAxis.dotProduct(xInv);

						if (xDot < 0.0d) {
							xAxis = new Point3D(-xAxis.getX(), -xAxis.getY(),
									-xAxis.getZ());
						}

						double ac = Math.acos(xAxis.dotProduct(zInv));
						ac = Math.abs(ac - 0.5d) * 2.0d;
						ac = ac * ac;

						Point3D scaledTo = new Point3D(zInv.getX(), zInv.getY(),
								zInv.getZ()).multiply(ac);
						xAxis = yAxis.add(scaledTo);
					} else {
						xAxis = new Point3D(xInv.getX(), xInv.getY(),
								xInv.getZ());
					}

					double speedModifier = 0.005f;

					Rotate ry = new Rotate(
							speedModifier * -(arg0.getY() - startY), xAxis);
					finalCamera.getTransforms().add(ry);

					Rotate rx = new Rotate(
							speedModifier * (arg0.getX() - startX), yAxis);
					Transform createConcatenation = rx.createConcatenation(ry);

					finalCamera.getTransforms().add(createConcatenation);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
