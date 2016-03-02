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
package org.eclipse.eavp.viz.service.javafx.canvas;

import org.eclipse.eavp.viz.service.javafx.internal.model.FXCameraAttachment;
import org.eclipse.eavp.viz.service.javafx.internal.scene.camera.CenteredCameraController;
import org.eclipse.eavp.viz.service.javafx.internal.scene.camera.ICameraController;
import org.eclipse.eavp.viz.service.javafx.scene.base.ICamera;
import org.eclipse.eavp.viz.service.javafx.viewer.Renderer;
import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import javafx.embed.swt.FXCanvas;
import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Shape3D;

/**
 * <p>
 * JavaFX implementation of GeometryViewer.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class FXViewer extends BasicViewer {

	/**
	 * The internally used root that cannot be modified by clients.
	 */
	protected Group internalRoot;

	/** The root of the scene as exposed to clients. */
	protected Group root;

	/** The active scene displayed to the end user. */
	protected Scene scene;

	/**
	 * The content provider that generates JavaFX scene data from the geometry
	 * editor scene model.
	 */
	protected FXContentProvider contentProvider;

	/** Default camera controller. */
	protected ICameraController cameraController;

	/** Default camera. */
	protected Camera defaultCamera;

	/**
	 * <p>
	 * Creates a JavaFX GeometryViewer.
	 * </p>
	 * 
	 * @param parent
	 */
	public FXViewer(Composite parent) {
		super(parent);

		// Create a renderer that creates FXAttachments
		renderer = new Renderer();
		renderer.register(FXAttachment.class, new FXAttachmentManager());
	}

	/**
	 * <p>
	 * Creates an FXCanvas control and initializes a default empty JavaFX scene.
	 * </p>
	 */
	@Override
	protected void createControl(Composite parent) {
		contentProvider = new FXContentProvider();

		fxCanvas = new FXCanvas(parent, SWT.NONE);

		// Create the root nodes
		internalRoot = new Group();
		root = new Group();

		internalRoot.getChildren().add(root);

		setupSceneInternals(internalRoot);

		scene = new Scene(internalRoot, 100, 100, true);

		// Set the scene's background color
		scene.setFill(Color.rgb(24, 30, 31));

		// Setup camera and input
		createDefaultCamera(internalRoot);
		wireSelectionHandling();

		fxCanvas.setScene(scene);
	}

	/**
	 * <p>
	 * Creates the current geometry editor camera.
	 * </p>
	 * 
	 * @param parent
	 *            the parent to create the camera on
	 * 
	 */
	protected void createDefaultCamera(Group parent) {
		PerspectiveCamera perspCamera = new PerspectiveCamera(true);
		perspCamera.setNearClip(0.1);
		perspCamera.setFarClip(4000.0);
		perspCamera.setFieldOfView(35);
		perspCamera.setTranslateX(0);
		perspCamera.setTranslateY(0);

		parent.getChildren().add(perspCamera);

		// Hacked in camera (for now)
		FXCameraAttachment cameraAttachment = new FXCameraAttachment(
				perspCamera);
		setCamera(cameraAttachment);
	}

	/**
	 * <p>
	 * Hooks up JavaFX picking with JFace selections.
	 * </p>
	 */
	protected void wireSelectionHandling() {

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {

			Group lastSelection = null;

			@Override
			public void handle(MouseEvent event) {
				// Pick
				PickResult pickResult = event.getPickResult();

				Node intersectedNode = pickResult.getIntersectedNode();

				if (intersectedNode == null) {
					return;
				}

				if (!(intersectedNode instanceof Shape3D)) {
					return;
				}

				// Resolve the parent
				Group nodeParent = (Group) intersectedNode.getParent();

				if (nodeParent == lastSelection) {
					return;
				}

				// Resolve the shape
				ShapeController modelShape = (ShapeController) nodeParent
						.getProperties().get(ShapeController.class);

				if (modelShape == null) {
					return;
				}

				// Create and set the viewer selection
				// (event gets fired in parent class)
				FXSelection selection = new FXSelection(modelShape);

				setSelection(selection);

				lastSelection = nodeParent;
			}
		});
	}

	/**
	 * <p>
	 * Creates scene elements that aren't meant to be manipulated by the user
	 * (markers, camera, etc.)
	 * </p>
	 */
	protected void setupSceneInternals(Group parent) {
		// Create scene plane for frame of reference.
		Box box = new Box(1000, 0, 1000);
		box.setMouseTransparent(true);
		box.setDrawMode(DrawMode.LINE);
		box.setMaterial(new PhongMaterial(Color.ANTIQUEWHITE));

		AmbientLight ambientLight = new AmbientLight(Color.rgb(100, 100, 100));

		PointLight light1 = new PointLight(Color.ANTIQUEWHITE);
		light1.setMouseTransparent(true);
		light1.setTranslateY(-350);

		PointLight light2 = new PointLight(Color.ANTIQUEWHITE);
		light2.setMouseTransparent(true);
		light2.setTranslateZ(350);

		PointLight light3 = new PointLight(Color.ANTIQUEWHITE);
		light3.setMouseTransparent(true);
		light3.setTranslateZ(-350);

		PointLight light4 = new PointLight(Color.ANTIQUEWHITE);
		light4.setMouseTransparent(true);
		light4.setTranslateZ(350);

		TransformGizmo gizmo = new TransformGizmo(1000);
		gizmo.showHandles(false);

		parent.getChildren().addAll(gizmo, box, light1, light2, light3, light4,
				ambientLight);

	}

	/**
	 * <p>
	 * Handles recreating the scene when the input changes.
	 * </p>
	 * 
	 * @see Viewer#inputChanged(Object, Object)
	 */
	@Override
	protected void inputChanged(Object oldInput, Object newInput) {
		contentProvider.inputChanged(this, newInput, input);
	}

	/**
	 * @see BasicViewer#updateCamera(ICamera)
	 */
	@Override
	protected void updateCamera(ICamera camera) {
		if (!(camera instanceof FXCameraAttachment)) {
			throw new IllegalArgumentException(
					Messages.FXGeometryViewer_InvalidCamera);
		}

		FXCameraAttachment attachment = (FXCameraAttachment) camera;
		Camera fxCamera = attachment.getFxCamera();

		if (fxCamera == null) {
			throw new NullPointerException(
					Messages.FXGeometryViewer_NullCamera);
		}

		cameraController = new CenteredCameraController(fxCamera, scene,
				fxCanvas);

		scene.setCamera(fxCamera);

		defaultCamera = fxCamera;
	}

	/**
	 * @see Viewer#getClass()
	 */
	@Override
	public Control getControl() {
		return fxCanvas;
	}

	/**
	 * @see Viewer#refresh()
	 */
	@Override
	public void refresh() {
		// Nothing to do
	}

	/**
	 * 
	 * @return
	 */
	public FXCanvas getFxCanvas() {
		return fxCanvas;
	}

	/**
	 * 
	 * @return
	 */
	@Override
	public Group getRoot() {
		return root;
	}

	/**
	 * 
	 * @return
	 */
	public Scene getScene() {
		return scene;
	}

	/**
	 * 
	 * @return
	 */
	public FXContentProvider getContentProvider() {
		return contentProvider;
	}

	/**
	 * 
	 * @param contentProvider
	 */
	public void setContentProvider(FXContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	public ICameraController getCameraController() {
		return cameraController;
	}

	public Camera getDefaultCamera() {
		return defaultCamera;
	}

	/**
	 * Reset the camera to its default position.
	 */
	public void resetCamera() {
		cameraController.reset();
	}

	/**
	 * Change the camera's pitch, its rotation about the axis perpendicular to
	 * its heading to the right, controlling how it is pointed in an up/down
	 * direction.
	 * 
	 * @param radians
	 *            The number of radians by which to rotate the camera.
	 */
	public void pitchCamera(double radians) {
		cameraController.pitchCamera(radians);
	}

	/**
	 * Change the camera's roll, it's rotation about the axis it is pointing.
	 * 
	 * @param radians
	 *            The number of radians by which to rotate the camera.
	 */
	public void rollCamera(double radians) {
		cameraController.rollCamera(radians);
	}

	/**
	 * Move the camera to the up or down, orthogonal to the direction it is
	 * pointing.
	 * 
	 * @param distance
	 *            The amount of space to move the camera. Positive distances
	 *            move the camera up, negative distances move it down.
	 */
	public void raiseCamera(double distance) {
		cameraController.raiseCamera(distance);
	}

	/**
	 * Move the camera to the left or right, orthogonal to the direction it is
	 * pointing.
	 * 
	 * @param distance
	 *            The amount of space to move the camera. Positive distances
	 *            move the camera right, negative distances move it left.
	 */
	public void strafeCamera(double distance) {
		cameraController.strafeCamera(distance);
	}

	/**
	 * Move the camera forward or backwards in the direction it is pointing.
	 * 
	 * @param distance
	 *            The amount of space to move the camera. Positive distances
	 *            move the camera forward, negative distances move it backwards.
	 */
	public void thrustCamera(double distance) {
		cameraController.thrustCamera(distance);
	}

	/**
	 * Change the camera's yaw, its rotation about the axis perpendicular to its
	 * heading in to the above, controlling how it is pointed in a left/right
	 * direction.
	 * 
	 * @param radians
	 *            The number of radians by which to rotate the camera.
	 */
	public void yawCamera(double radians) {
		cameraController.yawCamera(radians);
	}

	/**
	 * Set the camera's default position to view the scene with the Y axis
	 * horizontal and the Z vertical.
	 */
	public void setDefaultCameraYByZ() {
		cameraController.setDefaultAngle(90, 90, 0);
		cameraController.reset();
	}

	/**
	 * Set the camera's default position to view the scene with the X axis
	 * horizontal and the Y vertical.
	 */
	public void setDefaultCameraXByY() {
		cameraController.setDefaultAngle(0, 0, 0);
		cameraController.reset();
	}

	/**
	 * Set the camera's default position to view the scene with the Z axis
	 * horizontal and the Y vertical.
	 */
	public void setDefaultCameraZByX() {
		cameraController.setDefaultAngle(90, 0, 90);
		cameraController.reset();
	}

}