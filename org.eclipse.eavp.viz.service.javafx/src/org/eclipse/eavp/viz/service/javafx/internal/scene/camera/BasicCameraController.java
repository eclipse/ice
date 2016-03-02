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
import javafx.event.EventHandler;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

/**
 * An abstract implementation of a CameraController which features shared data
 * members and generic camera functionality.
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public abstract class BasicCameraController implements ICameraController {

	/**
	 * The Camera this object is managing.
	 */
	protected Camera camera;

	/**
	 * The scene which the camera is viewing.
	 */
	protected Scene scene;

	/**
	 * The parent Group containing the camera, to which transformations are
	 * applied to set the camera's position and direction.
	 */
	protected final Group xform;

	/**
	 * The collected transformation for all movements applied to the camera
	 */
	protected Affine affine;

	/**
	 * The FXCanvas which is using this camera
	 */
	protected FXCanvas canvas;

	/**
	 * The mouse's current X coordinate position.
	 */
	protected double mousePosX;

	/**
	 * The mouse's current Y coordinate position.
	 */
	protected double mousePosY;

	/**
	 * The mouse's last measured X coordinate position.
	 */
	protected double mouseOldX;

	/**
	 * The mouse's last measured Y coordinate position.
	 */
	protected double mouseOldY;

	/**
	 * The last measured change in the mouse's X position.
	 */
	protected double mouseDeltaX;

	/**
	 * The last measured change in the mouse's Y position.
	 */
	protected double mouseDeltaY;

	/**
	 * A constant multiplier for the mouse's normal movement speed.
	 */
	protected final double NORMAL_SPEED = 1.0d;

	/**
	 * A constant multiplier for the mouse's increased movement speed.
	 */
	protected final double FAST_SPEED = 2.0d;

	/**
	 * The default amount of X rotation to apply
	 */
	protected Rotate defaultX;

	/**
	 * The default amount of Y rotation to apply
	 */
	protected Rotate defaultY;

	/**
	 * The default amount of Z rotation to apply.
	 */
	protected Rotate defaultZ;

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
	public BasicCameraController(Camera camera, Scene scene,
			FXCanvas canvas) {

		// Initialize the data members
		this.camera = camera;
		this.scene = scene;
		this.canvas = canvas;

		// Get the camera's parent
		xform = new Group();
		xform.getChildren().add(camera);

		// Set the affine transformation
		affine = new Affine();
		xform.getTransforms().setAll(affine);

		// Initialize the default angles
		defaultX = new Rotate();
		defaultY = new Rotate();
		defaultZ = new Rotate();
		defaultX.setAxis(Rotate.X_AXIS);
		defaultY.setAxis(Rotate.Y_AXIS);
		defaultZ.setAxis(Rotate.Z_AXIS);

		// Set the camera to its default position
		reset();

		// Set the handler for key presses
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				handleKeyPressed(event);
			}
		});

		// Set the handler for mouse presses
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				handleMousePressed(arg0);
			}
		});

		// Set the handler for mouse drags
		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				handleMouseDragged(arg0);
			}

		});

		// Set the handler for releasing the mouse button
		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				handleMouseReleased(arg0);
			}

		});

		// Set the behavior for mouse scroll events
		scene.setOnScroll(new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent event) {
				handleMouseScroll(event);
			}

		});
	}

	/**
	 * Set up the various data structures needed for the camera's operation.
	 * This method is to be invoked before input listeners are registered and
	 * does nothing by default. It is intended to be overwritten by subclasses.
	 */
	abstract protected void initCamera();

	/**
	 * This function defines the camera's behavior when a keyboard button is
	 * pressed. By default, it does nothing. It is intended to be overwritten by
	 * subclasses.
	 * 
	 * @param event
	 *            An event sent to the scene.
	 */
	abstract public void handleKeyPressed(KeyEvent event);

	/**
	 * This function defines the camera's behavior when the mouse is dragged. By
	 * default, it does nothing. It is intended to be overwritten by subclasses.
	 * 
	 * @param event
	 *            An event sent to the scene.
	 */
	abstract public void handleMouseDragged(MouseEvent event);

	/**
	 * This function defines the camera's behavior when a mouse button is
	 * pressed. By default, it does nothing. It is intended to be overwritten by
	 * subclasses.
	 * 
	 * @param event
	 *            An event sent to the scene.
	 */
	abstract public void handleMousePressed(MouseEvent event);

	/**
	 * This function defines the camera's behavior when a mouse button is
	 * released. By default, it does nothing. It is intended to be overwritten
	 * by subclasses.
	 * 
	 * @param event
	 *            An event sent to the scene.
	 */
	abstract public void handleMouseReleased(MouseEvent event);

	/**
	 * This function defines the camera's behavior when the mouse wheel is
	 * scrolled. By default, it does nothing. It is intended to be overwritten
	 * by subclasses.
	 * 
	 * @param event
	 *            An event sent to the scene.
	 */
	abstract public void handleMouseScroll(ScrollEvent event);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#reset()
	 */
	@Override
	abstract public void reset();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#pitchCamera(double)
	 */
	@Override
	abstract public void pitchCamera(double radians);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#rollCamera(double)
	 */
	@Override
	abstract public void rollCamera(double radians);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#raiseCamera(double)
	 */
	@Override
	abstract public void raiseCamera(double distance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#strafeCamera(double)
	 */
	@Override
	abstract public void strafeCamera(double distance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#thrustCamera(double)
	 */
	@Override
	abstract public void thrustCamera(double distance);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#yawCamera(double)
	 */
	@Override
	abstract public void yawCamera(double radians);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.internal.scene.camera.
	 * ICameraController#setDefaultAngle(double, double, double)
	 */
	@Override
	public void setDefaultAngle(double x, double y, double z) {

		// Set each of the default angles to the specified values)
		defaultX.setAngle(x);
		defaultY.setAngle(y);
		defaultZ.setAngle(z);

	}

}
