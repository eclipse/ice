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
import org.eclipse.ice.client.widgets.geometry.ShapeTreeContentProvider;
import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.GeometryComponent;
import org.eclipse.ice.datastructures.form.geometry.OperatorType;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;
import org.eclipse.ice.datastructures.form.geometry.ShapeType;

import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests ShapeTreeContentProvider handling of elements
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author abd
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ShapeTreeContentProviderTest {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the correctness of the getChildren operation
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGetChildren() {
		// begin-user-code

		// Create all needed objects

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider();

		PrimitiveShape sphere1 = new PrimitiveShape(ShapeType.Sphere);
		PrimitiveShape cube1 = new PrimitiveShape(ShapeType.Cube);

		ComplexShape union1 = new ComplexShape(OperatorType.Union);
		ComplexShape complement1 = new ComplexShape(OperatorType.Complement);
		ComplexShape intersection1 = new ComplexShape(OperatorType.Intersection);

		// Create a simple CSG tree

		union1.addShape(sphere1);
		union1.addShape(complement1);
		union1.addShape(intersection1);
		complement1.addShape(cube1);

		// Using the ShapeTreeContentProvider, get and check the children
		// of all the previously created shapes

		Object[] union1Children = shapeProvider.getChildren(union1);
		Object[] union1ExpectedChildren = new Object[] { sphere1, complement1,
				intersection1 };
		assertArrayEquals(union1ExpectedChildren, union1Children);

		Object[] complement1Children = shapeProvider.getChildren(complement1);
		Object[] complement1ExpectedChildren = new Object[] { cube1 };
		assertArrayEquals(complement1ExpectedChildren, complement1Children);

		// Empty ComplexShapes should actually return one BlankShape as a child,
		// rather than an empty list of children when ShapeProvider.getChildren
		// is called.

		Object[] intersection1Children = shapeProvider
				.getChildren(intersection1);
		assertEquals(1, intersection1Children.length);

		Object[] sphere1Children = shapeProvider.getChildren(sphere1);
		assertEquals(0, sphere1Children.length);

		Object[] cube1Children = shapeProvider.getChildren(cube1);
		assertEquals(0, cube1Children.length);

		// A null or mistyped parameter should return null

		assertNull(shapeProvider.getChildren(null));

		assertNull(shapeProvider.getChildren(new Object()));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the correctness of the hasChildren operation
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkHasChildren() {
		// begin-user-code

		// This test is similar to checkGetChildren, but it only checks the
		// boolean values of hasChildren rather than checking the elements
		// themselves.

		// Create all needed objects

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider();

		PrimitiveShape sphere1 = new PrimitiveShape(ShapeType.Sphere);
		PrimitiveShape cube1 = new PrimitiveShape(ShapeType.Cube);

		ComplexShape union1 = new ComplexShape(OperatorType.Union);
		ComplexShape complement1 = new ComplexShape(OperatorType.Complement);
		ComplexShape intersection1 = new ComplexShape(OperatorType.Intersection);

		// Create a simple CSG tree

		union1.addShape(sphere1);
		union1.addShape(complement1);
		union1.addShape(intersection1);
		complement1.addShape(cube1);

		// Using the ShapeTreeContentProvider, get and check the children
		// of all the previously created shapes

		assertTrue(shapeProvider.hasChildren(union1));
		assertTrue(shapeProvider.hasChildren(complement1));

		// Note: Although intersection1 has no actual child shapes, a BlankShape
		// with the label "<add shape>" will appear as a psuedochild in the
		// ShapeTreeView treeviewer. Therefore, intersection1 has a child
		// as far as this ShapeTreeContentProviderTest is concerned.

		assertTrue(shapeProvider.hasChildren(intersection1));

		assertFalse(shapeProvider.hasChildren(sphere1));
		assertFalse(shapeProvider.hasChildren(cube1));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks that ShapeTreeContentProvider can be created and initialized
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

		// Currently there is nothing to test immediately after being created,
		// but we can still check whether it doesn't throw exceptions upon
		// creation and destruction.

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider();

		shapeProvider.dispose();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks that the getElements operation works correctly
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGetElements() {
		// begin-user-code

		// Create all needed objects

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider();

		PrimitiveShape sphere1 = new PrimitiveShape(ShapeType.Sphere);
		PrimitiveShape cube1 = new PrimitiveShape(ShapeType.Cube);
		ComplexShape union1 = new ComplexShape(OperatorType.Union);
		ComplexShape complement1 = new ComplexShape(OperatorType.Complement);
		ComplexShape intersection1 = new ComplexShape(OperatorType.Intersection);

		// Put them all in a GeometryComponent

		GeometryComponent geometry = new GeometryComponent();
		geometry.addShape(sphere1);
		geometry.addShape(cube1);
		geometry.addShape(union1);
		geometry.addShape(complement1);
		geometry.addShape(sphere1);

		Object[] expectedElements = new Object[] { sphere1, cube1, union1,
				complement1, sphere1 };
		assertArrayEquals(expectedElements, shapeProvider.getElements(geometry));

		// Try getting elements of null and a mistyped object

		assertNull(shapeProvider.getElements(null));
		assertNull(shapeProvider.getElements(new Object()));
		assertNull(shapeProvider.getElements(new Integer(3)));

		// end-user-code
	}
}