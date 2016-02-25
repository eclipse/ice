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
package org.eclipse.eavp.viz.service.javafx.canvas.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.canvas.TransformGizmo;
import org.junit.Test;

import javafx.scene.Node;

/**
 * A class to test the functionality of the TransformGizmo.
 * 
 * @author Robert Smith
 *
 */
public class TransformGizmoTester {

	/**
	 * Test that the gizmo has the correct axis.
	 */
	@Test
	public void checkAxis() {

		// Create a gizmo with axis length 0. This should create a gizmo with no
		// children, as it has nothing to contain.
		TransformGizmo emptyGizmo = new TransformGizmo(0);
		assertTrue(emptyGizmo.getChildren().isEmpty());

		// Create a gizmo with some axis. Check that it has three axis and three
		// handles.
		TransformGizmo gizmo = new TransformGizmo(50);
		assertEquals(6, gizmo.getChildren().size());

		// Set the handles to be invisible
		gizmo.showHandles(false);

		// The number of visible objects contained by the gizmo
		int numVisible = 0;

		// Count the number of visible children
		for (Node child : gizmo.getChildren()) {
			if (child.isVisible()) {
				numVisible++;
			}
		}

		// There should be 3 visible axis and 3 invisible handles
		assertEquals(3, numVisible);

		// Display the handles again
		gizmo.showHandles(true);

		// Count the number of visible children
		numVisible = 0;
		for (Node child : gizmo.getChildren()) {
			if (child.isVisible()) {
				numVisible++;
			}
		}

		// There should be 3 axis and 3 handles, all visible
		assertEquals(6, numVisible);
	}
}
