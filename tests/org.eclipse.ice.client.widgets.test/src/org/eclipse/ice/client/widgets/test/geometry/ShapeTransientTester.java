/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets.test.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.eclipse.ice.client.widgets.geometry.ShapeTransient;
import org.eclipse.ice.datastructures.form.geometry.ICEShape;
import org.eclipse.ice.viz.service.geometry.ShapeType;
import org.junit.Test;

/**
 * <p>
 * Checks ShapeTransient
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class ShapeTransientTester {
	/**
	 * <p>
	 * Checks that ShapeTransient can correctly store a reference to an IShape
	 * </p>
	 * 
	 */
	@Test
	public void checkStorage() {

		ICEShape sphere = new ICEShape(ShapeType.Sphere);
		ShapeTransient shapeTransient = new ShapeTransient(sphere);

		// Check that the ShapeTransient stored the variable

		assertEquals(sphere, shapeTransient.getShape());

		// Check for null

		ShapeTransient shapeTransient2 = new ShapeTransient(null);
		assertNull(shapeTransient2.getShape());

	}
}