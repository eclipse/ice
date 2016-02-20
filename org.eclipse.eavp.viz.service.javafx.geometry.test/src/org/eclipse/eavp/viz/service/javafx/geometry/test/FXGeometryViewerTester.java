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
package org.eclipse.eavp.viz.service.javafx.geometry.test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.eavp.viz.service.javafx.geometry.FXGeometryViewer;
import org.eclipse.eavp.viz.service.javafx.internal.model.FXCameraAttachment;
import org.eclipse.eavp.viz.service.javafx.scene.base.ICamera;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import javafx.scene.PerspectiveCamera;

/**
 * A class to test the functionality of the FXGeometryViewer.
 * 
 * @author Robert Smith
 *
 */
public class FXGeometryViewerTester {

	/**
	 * Test that the viewer properly checks its camera for validity.
	 */
	@Test
	public void checkCamera() {

		// Create a viewer
		FXGeometryViewer viewer = new FXGeometryViewer(
				new Composite(new Shell(Display.getDefault()), SWT.NONE));

		// Try to set an invalid camera.
		try {
			viewer.setCamera(new TestCamera());
			fail();
		} catch (Exception e) {
			// The function should have thrown an exception, so fail the test if
			// it doesn't reach this block.
		}

		// Create a valid attachment with no camera
		FXCameraAttachment emptyAttachment = new FXCameraAttachment(null);

		// Try to set an invalid camera
		try {
			viewer.setCamera(emptyAttachment);
			fail();
		} catch (Exception e) {
			// The function should have thrown an exception, so fail the test if
			// it doesn't reach this block.
		}

		// Attachment create a valid attachment and set it as the viewer's
		// camera
		FXCameraAttachment attachment = new FXCameraAttachment(
				new PerspectiveCamera());
		viewer.setCamera(attachment);

		// The viewer should have the attachment's camera
		assertTrue(viewer.getCamera() == attachment);
	}

	/**
	 * A dummy camera for testing.
	 */
	private class TestCamera implements ICamera {
		// Nothing to do, we just need a concrete ICamera
	}
}
