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
package org.eclipse.ice.viz.service.javafx.internal.model.test;

import org.junit.Test;

import javafx.scene.Camera;
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

		Camera camera = new PerspectiveCamera();
	}
}
