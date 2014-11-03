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

import static org.junit.Assert.*;

import org.junit.Test;

import org.eclipse.ice.client.widgets.geometry.ShapeTransient;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;
import org.eclipse.ice.datastructures.form.geometry.ShapeType;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Checks ShapeTransient
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author abd
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ShapeTransientTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks that ShapeTransient can correctly store a reference to an IShape
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkStorage() {
		// begin-user-code

		IShape sphere = new PrimitiveShape(ShapeType.Sphere);
		ShapeTransient shapeTransient = new ShapeTransient(sphere);

		// Check that the ShapeTransient stored the variable

		assertEquals(sphere, shapeTransient.getShape());

		// Check for null

		ShapeTransient shapeTransient2 = new ShapeTransient(null);
		assertNull(shapeTransient2.getShape());

		// end-user-code
	}
}