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
package org.eclipse.eavp.viz.service.javafx.geometry;

import org.eclipse.eavp.viz.service.javafx.canvas.FXViewer;
import org.eclipse.eavp.viz.service.javafx.internal.model.FXCameraAttachment;
import org.eclipse.eavp.viz.service.javafx.internal.scene.camera.CenteredCameraController;
import org.eclipse.eavp.viz.service.javafx.scene.base.ICamera;
import org.eclipse.swt.widgets.Composite;

import javafx.scene.Camera;

/**
 * An extension of FX viewer for use with the geometry editor
 * 
 * @author Tony McCrary (tmccrary@l33tlabs.com), Robert Smith
 *
 */
public class FXGeometryViewer extends FXViewer {

	public FXGeometryViewer(Composite parent) {
		super(parent);

		// Register the geometry attachment class with an attachment manager
		renderer.register(FXGeometryAttachment.class,
				new FXGeometryAttachmentManager());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.javafx.canvas.FXViewer#updateCamera(org.
	 * eclipse.ice.viz.service.javafx.scene.base.ICamera)
	 */
	@Override
	protected void updateCamera(ICamera camera) {

		// Check the camera attachment for validity
		if (!(camera instanceof FXCameraAttachment)) {
			throw new IllegalArgumentException(
					"Invalid camera attached to Mesh Viewer.");
		}

		// Cast the camera attachment and check that it has a camera
		FXCameraAttachment attachment = (FXCameraAttachment) camera;
		Camera fxCamera = attachment.getFxCamera();

		if (fxCamera == null) {
			throw new NullPointerException(
					"No camera was attached to Mesh Viewer");
		}

		// Create a controller
		cameraController = new CenteredCameraController(fxCamera, scene, fxCanvas);

		// Set the camera on the scene
		scene.setCamera(fxCamera);
		defaultCamera = fxCamera;

	}

}
