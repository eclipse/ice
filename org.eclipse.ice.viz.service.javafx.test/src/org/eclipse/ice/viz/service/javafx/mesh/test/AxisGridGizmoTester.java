/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Robert Smith
 *******************************************************************************/
package org.eclipse.ice.viz.service.javafx.mesh.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.viz.service.javafx.mesh.AxisGridGizmo;
import org.junit.Test;

/**
 * A class to test the functionality of the AxisGridGizmo
 * 
 * @author Robert Smith
 *
 */
public class AxisGridGizmoTester {

	/**
	 * Tests the gizmo's ability to hide/show the axes.
	 */
	@Test
	public void checkAxes() {

		// The axes should be visible by default
		AxisGridGizmo gizmo = new AxisGridGizmo(1);
		assertTrue(gizmo.axesVisible());

		// Trying to turn them on again shouldn't break anything
		gizmo.toggleAxis(true);
		assertTrue(gizmo.axesVisible());

		// Turn them off and check that they are gone
		gizmo.toggleAxis(false);
		assertFalse(gizmo.axesVisible());

		// Trying to turn them off shouldn't break anything
		gizmo.toggleAxis(false);
		assertFalse(gizmo.axesVisible());

		// Turn them back on
		gizmo.toggleAxis(true);
		assertTrue(gizmo.axesVisible());

	}
}
