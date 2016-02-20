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
package org.eclipse.eavp.viz.service.javafx.mesh.datatypes;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.javafx.mesh.datatypes.FXFaceView;
import org.eclipse.eavp.viz.service.modeling.FaceMesh;
import org.junit.Test;

/**
 * A class to test the functionality of the FXFaceView.
 * 
 * @author Robert Smith
 *
 */
public class FXFaceViewTester {

	/**
	 * Test that FXFaceViews are cloned correctly
	 */
	@Test
	public void checkClone() {

		// Create a cloned view and check that it is identical to the original
		FaceMesh mesh = new FaceMesh();
		FXFaceView view = new FXFaceView(mesh);
		FXFaceView clone = (FXFaceView) view.clone();
		assertTrue(view.equals(clone));
	}
}
