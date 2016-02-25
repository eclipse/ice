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
package org.eclipse.eavp.viz.service.javafx.mesh.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import javafx.scene.shape.Box;

/**
 * A class to test the functionality of the FXMeshViewer.
 * 
 * @author Robert Smith
 *
 */
public class FXMeshViewerTester {

	/**
	 * Check that the canvas's axes can be turned on and off.
	 */
	@Test
	public void checkAxes() {

		// The viewer for testing
		TestViewer viewer = new TestViewer(
				new Composite(new Shell(Display.getDefault()), SWT.NONE));

		// check that the viewer's axes are visible by default
		ArrayList<Box> axes = ((TestGizmo) viewer.getGizmo()).getAxes();
		for (Box axis : axes) {
			assertTrue(axis.isVisible());
		}

		// Set the canvas's axes off and check that they are invisible
		viewer.setAxisVisible(false);
		for (Box axis : axes) {
			assertFalse(axis.isVisible());
		}

		// Set the canvas's axes on and check that they are visible
		viewer.setAxisVisible(true);
		for (Box axis : axes) {
			assertTrue(axis.isVisible());
		}
	}

}
