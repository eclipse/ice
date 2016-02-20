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
package org.eclipse.eavp.viz.service.modeling.test;

import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.eavp.viz.service.modeling.ShapeMesh;
import org.junit.Test;

/**
 * A class to test the functionality of ShapeController.
 * 
 * @author Robert Smith
 *
 */
public class ShapeControllerTester {

	/**
	 * Check that ShapeControllers can be properly cloned.
	 */
	@Test
	public void checkClone() {

		// Create a cloned shape and check that it is identical to the original
		ShapeController shape = new ShapeController(new ShapeMesh(),
				new AbstractView());
		shape.setProperty("Test", "Property");
		ShapeController clone = (ShapeController) shape.clone();
		assertTrue(shape.equals(clone));
	}
}
