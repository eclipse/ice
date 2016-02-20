/*******************************************************************************
 * Copyright (c) 2016 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.eavp.viz.service.javafx.internal.model.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.internal.Util;
import org.eclipse.eavp.viz.service.javafx.internal.model.FXCameraAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.base.GNode;
import org.eclipse.eavp.viz.service.javafx.scene.model.INode;
import org.junit.Test;

import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;

/**
 * A class to test the functionality of the FXCameraAttachment.
 * 
 * @author Robert Smith
 *
 */
public class FXCameraAttachmentTester {

	/**
	 * Check that the camera is added to the parent INode's JavaFX node
	 * correctly.
	 */
	@Test
	public void checkCamera() {

		// Create a camera, an attachment containing that camera, and the node
		Camera camera = new PerspectiveCamera();
		FXCameraAttachment attachment = new FXCameraAttachment(camera);
		GNode node = new GNode();
		Group fxNode = new Group();
		node.setProperty(INode.RENDERER_NODE_PROP, fxNode);

		// Check that the attachment has the camera
		assertTrue(attachment.getFxCamera() == camera);

		// Attach to the node and check that the camera has been added to the
		// JavaFX node
		attachment.attach(node);
		assertTrue(Util.getFxGroup(node).getChildren().contains(camera));

		// Detach from the node and check that the camera has been removed from
		// the JavaFX node.
		attachment.detach(node);
		assertFalse(Util.getFxGroup(node).getChildren().contains(camera));
	}
}
