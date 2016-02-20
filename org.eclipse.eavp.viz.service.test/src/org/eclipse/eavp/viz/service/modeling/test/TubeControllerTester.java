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

import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.TubeController;
import org.eclipse.eavp.viz.service.modeling.TubeMesh;
import org.junit.Test;

/**
 * A class to test the functionality of TubeController.
 * 
 * @author Robert Smith
 *
 */
public class TubeControllerTester {

	/**
	 * Check that TubeControllers can be properly cloned.
	 */
	@Test
	public void checkClone() {

		// Create a cloned tube and check that it is identical to the original
		TubeController shape = new TubeController(new TubeMesh(),
				new AbstractView());
		shape.setProperty("Test", "Property");
		TubeController clone = (TubeController) shape.clone();
		assertTrue(shape.equals(clone));
	}
}
