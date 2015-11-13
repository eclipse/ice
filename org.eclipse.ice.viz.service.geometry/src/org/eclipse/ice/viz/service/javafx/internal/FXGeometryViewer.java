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
package org.eclipse.ice.viz.service.javafx.internal;

import org.eclipse.ice.viz.service.geometry.GeometrySelection;
import org.eclipse.ice.viz.service.geometry.scene.base.GeometryAttachment;
import org.eclipse.ice.viz.service.geometry.scene.base.ICamera;
import org.eclipse.ice.viz.service.geometry.viewer.GeometryViewer;
import org.eclipse.ice.viz.service.javafx.internal.model.FXCameraAttachment;
import org.eclipse.ice.viz.service.javafx.internal.model.FXRenderer;
import org.eclipse.ice.viz.service.javafx.internal.model.FXShape;
import org.eclipse.ice.viz.service.javafx.internal.model.geometry.FXGeometryAttachmentManager;
import org.eclipse.ice.viz.service.javafx.internal.scene.TransformGizmo;
import org.eclipse.ice.viz.service.javafx.internal.scene.camera.CameraController;
import org.eclipse.ice.viz.service.javafx.internal.scene.camera.FPSController;
import org.eclipse.ice.viz.service.modeling.Shape;
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

/**
 * <p>
 * JavaFX implementation of GeometryViewer.
 * </p>
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com)
 *
 */
public class FXGeometryViewer extends GeometryViewer {

	/** The root JavaFX widget that displays content. */
	private FXCanvas fxCanvas;

	/**
	 * The internally used root that cannot be modified by clients.
	 */
	private Group internalRoot;

	/** The root of the scene as exposed to clients. */
	private Group root;

	/** The active scene displayed to the end user. */
	private Scene scene;

	/**
	 * The content provider that generates JavaFX scene data from the geometry
	 * editor scene model.
	 */
	private FXContentProvider contentProvider;

	/** Default camera controller. */
	private CameraController cameraController;

	/** Default camera. */
	private Camera defaultCamera;

	/**
	 * <p>
	 * Creates a JavaFX GeometryViewer.
	 * </p>
	 * 
	 * @param parent
	 */
	public FXGeometryViewer(Composite parent) {
		super(parent);

		renderer = new FXRenderer();
		renderer.register(GeometryAttachment.class,
				new FXGeometryAttachmentManager());
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
	private void createDefaultCamera(Group parent) {
		PerspectiveCamera perspCamera = new PerspectiveCamera();
		perspCamera.setNearClip(0.1);
		perspCamera.setFarClip(2000.0);
		perspCamera.setFieldOfView(35);
		perspCamera.setTranslateX(0);
		perspCamera.setTranslateY(-100);
		perspCamera.setTranslateZ(-1000);

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
	private void wireSelectionHandling() {

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {

			FXShape lastSelection = null;

			@Override
			public void handle(MouseEvent event) {
				// Pick
				PickResult pickResult = event.getPickResult();

				Node intersectedNode = pickResult.getIntersectedNode();

				if (intersectedNode == null) {
					return;
				}

				if (!(intersectedNode instanceof FXShape)) {
					return;
				}

				// Resolve the parent
				FXShape nodeParent = (FXShape) intersectedNode.getParent();

				if (nodeParent == lastSelection) {
					return;
				}

				// Resolve the shape
				Shape modelShape = (Shape) nodeParent.getProperties()
						.get(Shape.class);

				if (modelShape == null) {
					return;
				}

				// Create and set the viewer selection
				// (event gets fired in parent class)
				GeometrySelection selection = new GeometrySelection(modelShape);

				setSelection(selection);

				nodeParent.setSelected(true);

				if (lastSelection != null) {
					lastSelection.setSelected(false);
				}

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
	private void setupSceneInternals(Group parent) {
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
	 * @see GeometryViewer#updateCamera(ICamera)
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

		cameraController = new FPSController(fxCamera, scene, fxCanvas);

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

	public CameraController getCameraController() {
		return cameraController;
	}

	public Camera getDefaultCamera() {
		return defaultCamera;
	}

}