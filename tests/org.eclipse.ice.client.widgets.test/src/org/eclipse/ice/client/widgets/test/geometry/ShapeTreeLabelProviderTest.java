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

import org.junit.Test;
import static org.junit.Assert.*;

import org.eclipse.ice.client.widgets.geometry.ShapeTreeLabelProvider;
import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.OperatorType;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;
import org.eclipse.ice.datastructures.form.geometry.ShapeType;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests ShapeTreeLabelProvider
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author abd
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ShapeTreeLabelProviderTest {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks that ShapeTreeLabelProvider can be created and initialized
	 * properly
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCreation() {
		// begin-user-code

		// Create a new ShapeTreeLabelProvider and dispose it
		// Currently there are no assertions to check, but we can make
		// sure they're created without throwing exceptions.

		ShapeTreeLabelProvider labelProvider = new ShapeTreeLabelProvider();
		labelProvider.dispose();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks that ShapeTreeLabelProvider returns expected images<span
	 * style="font-family:Serif;"></span>
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGetImage() {
		// begin-user-code

		ShapeTreeLabelProvider labelProvider = new ShapeTreeLabelProvider();

		// Any input should produce a null return value

		PrimitiveShape cube1 = new PrimitiveShape(ShapeType.Cube);
		ComplexShape intersection1 = new ComplexShape(OperatorType.Intersection);

		assertNull(labelProvider.getImage(cube1));
		assertNull(labelProvider.getImage(intersection1));
		assertNull(labelProvider.getImage(null));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks that the getText operation returns valid names
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGetText() {
		// begin-user-code

		ShapeTreeLabelProvider labelProvider = new ShapeTreeLabelProvider();

		// Create some named shapes

		PrimitiveShape cube1 = new PrimitiveShape(ShapeType.Cube);
		cube1.setName("KUB");

		ComplexShape intersection1 = new ComplexShape(OperatorType.Intersection);
		intersection1.setName("INTRASECSION");

		ComplexShape union1 = new ComplexShape(OperatorType.Union);
		union1.setDescription("Not a name");
		union1.setId(1111);

		// Check that the ShapeTreeLabelProvider returns the correct names
		// with the format "<name> <id>"

		String expectedCube1Text = cube1.getName() + " " + cube1.getId();
		assertTrue(labelProvider.getText(cube1).equals(expectedCube1Text));

		String expectedIntersection1Text = intersection1.getName() + " "
				+ intersection1.getId();
		assertTrue(labelProvider.getText(intersection1).equals(
				expectedIntersection1Text));

		String expectedUnion1Text = union1.getName() + " " + union1.getId();
		assertTrue(labelProvider.getText(union1).equals(expectedUnion1Text));

		// Check a null parameter

		assertNull(labelProvider.getText(null));

		// Check a non-ICEObject parameter

		assertNull(labelProvider.getText(new Object()));

		// end-user-code
	}
}