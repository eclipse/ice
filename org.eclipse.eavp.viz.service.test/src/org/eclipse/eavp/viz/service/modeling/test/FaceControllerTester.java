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
package org.eclipse.eavp.viz.service.modeling.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.modeling.BasicView;
import org.eclipse.eavp.viz.service.modeling.FaceController;
import org.eclipse.eavp.viz.service.modeling.FaceMesh;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.junit.Test;

/**
 * A class for testing the functionality of the FaceController
 * 
 * @author Robert Smith
 *
 */
public class FaceControllerTester {

	/**
	 * Check that FaceControllers are cloned correctly.
	 */
	@Test
	public void checkClone() {

		// Create a cloned face and check that it is identical to the original
		FaceController face = new FaceController(new FaceMesh(),
				new BasicView());
		face.setProperty(MeshProperty.DESCRIPTION, "Property");
		FaceController clone = (FaceController) face.clone();
		assertTrue(face.equals(clone));
	}
}
