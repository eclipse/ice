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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.eavp.viz.service.geometry.widgets.ShapeTreeContentProvider;
import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachment;
import org.eclipse.eavp.viz.service.javafx.canvas.FXAttachmentManager;
import org.eclipse.ice.datastructures.form.GeometryComponent;
import org.eclipse.january.geometry.Complement;
import org.eclipse.january.geometry.Cube;
import org.eclipse.january.geometry.Geometry;
import org.eclipse.january.geometry.GeometryFactory;
import org.eclipse.january.geometry.Intersection;
import org.eclipse.january.geometry.Sphere;
import org.eclipse.january.geometry.Union;
import org.junit.Test;

/**
 * <p>
 * Tests ShapeTreeContentProvider handling of elements
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class ShapeTreeContentProviderTest {
	/**
	 * <p>
	 * Checks the correctness of the getChildren operation
	 * </p>
	 * 
	 */
	@Test
	public void checkGetChildren() {

		// Create all needed objects

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider(
				new FXAttachment(new FXAttachmentManager()));

		// Create a shape
		Sphere sphere1 = GeometryFactory.eINSTANCE.createSphere();
		Cube cube1 = GeometryFactory.eINSTANCE.createCube();
		Union union1 = GeometryFactory.eINSTANCE.createUnion();
		Complement complement1 = GeometryFactory.eINSTANCE.createComplement();
		Intersection intersection1 = GeometryFactory.eINSTANCE.createIntersection();;

		// Create a simple CSG tree
		union1.addNode(sphere1);
		union1.addNode(complement1);
		union1.addNode(intersection1);
		complement1.addNode(cube1);

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

	}

	/**
	 * <p>
	 * Checks the correctness of the hasChildren operation
	 * </p>
	 * 
	 */
	@Test
	public void checkHasChildren() {

		// This test is similar to checkGetChildren, but it only checks the
		// boolean values of hasChildren rather than checking the elements
		// themselves.

		// Create all needed objects

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider(
				new FXAttachment(new FXAttachmentManager()));

		// Create a shape
		Sphere sphere1 = GeometryFactory.eINSTANCE.createSphere();
		Cube cube1 = GeometryFactory.eINSTANCE.createCube();
		Union union1 = GeometryFactory.eINSTANCE.createUnion();
		Complement complement1 = GeometryFactory.eINSTANCE.createComplement();
		Intersection intersection1 = GeometryFactory.eINSTANCE.createIntersection();;

		// Create a simple CSG tree
		union1.addNode(sphere1);
		union1.addNode(complement1);
		union1.addNode(intersection1);
		complement1.addNode(cube1);

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

	}

	/**
	 * <p>
	 * Checks that ShapeTreeContentProvider can be created and initialized
	 * properly
	 * </p>
	 * 
	 */
	@Test
	public void checkCreation() {

		// Currently there is nothing to test immediately after being created,
		// but we can still check whether it doesn't throw exceptions upon
		// creation and destruction.

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider(
				new FXAttachment(new FXAttachmentManager()));

		shapeProvider.dispose();

	}

	/**
	 * <p>
	 * Checks that the getElements operation works correctly
	 * </p>
	 * 
	 */
	@Test
	public void checkGetElements() {

		// Create all needed objects

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider(
				new FXAttachment(new FXAttachmentManager()));

		Geometry geometry = GeometryFactory.eINSTANCE.createGeometry();

		Sphere sphere1 = GeometryFactory.eINSTANCE.createSphere();
		Cube cube1 = GeometryFactory.eINSTANCE.createCube();
		Union union1 = GeometryFactory.eINSTANCE.createUnion();
		Complement complement1 = GeometryFactory.eINSTANCE.createComplement();
		Intersection intersection1 = GeometryFactory.eINSTANCE
				.createIntersection();

		// Put them all in a GeometryComponent

		GeometryComponent geometryComponent = new GeometryComponent();
		geometryComponent.setGeometry(geometry);
		geometryComponent.getGeometry().addNode(sphere1);
		geometryComponent.getGeometry().addNode(cube1);
		geometryComponent.getGeometry().addNode(union1);
		geometryComponent.getGeometry().addNode(complement1);
		geometryComponent.getGeometry().addNode(sphere1);

		Object[] expectedElements = new Object[] { sphere1, cube1, union1,
				complement1, sphere1 };
		assertArrayEquals(expectedElements,
				shapeProvider.getElements(geometry));

		// Try getting elements of null and a mistyped object

		assertNull(shapeProvider.getElements(null));
		assertNull(shapeProvider.getElements(new Object()));
		assertNull(shapeProvider.getElements(new Integer(3)));

	}
}