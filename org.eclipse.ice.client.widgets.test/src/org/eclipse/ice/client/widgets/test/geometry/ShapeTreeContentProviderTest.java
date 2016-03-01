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

import org.eclipse.eavp.viz.service.geometry.shapes.GeometryMeshProperty;
import org.eclipse.eavp.viz.service.geometry.shapes.OperatorType;
import org.eclipse.eavp.viz.service.geometry.shapes.ShapeType;
import org.eclipse.eavp.viz.service.geometry.widgets.ShapeTreeContentProvider;
import org.eclipse.eavp.viz.service.modeling.AbstractView;
import org.eclipse.eavp.viz.service.modeling.MeshProperty;
import org.eclipse.eavp.viz.service.modeling.ShapeController;
import org.eclipse.eavp.viz.service.modeling.ShapeMesh;
import org.eclipse.ice.datastructures.form.GeometryComponent;
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

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider();

		// Create a shape
		ShapeMesh geometryModel = new ShapeMesh();
		AbstractView geometryView = new AbstractView();
		ShapeController geometryShape = new ShapeController(geometryModel,
				geometryView);

		ShapeController sphere1 = (ShapeController) geometryShape.clone();
		sphere1.setProperty(MeshProperty.TYPE, ShapeType.Sphere.toString());
		ShapeController cube1 = (ShapeController) geometryShape.clone();
		cube1.setProperty(MeshProperty.TYPE, ShapeType.Cube.toString());

		ShapeController union1 = (ShapeController) geometryShape.clone();
		union1.setProperty(GeometryMeshProperty.OPERATOR,
				OperatorType.Union.toString());
		ShapeController complement1 = (ShapeController) geometryShape.clone();
		complement1.setProperty(GeometryMeshProperty.OPERATOR,
				OperatorType.Complement.toString());
		ShapeController intersection1 = (ShapeController) geometryShape.clone();
		intersection1.setProperty(GeometryMeshProperty.OPERATOR,
				OperatorType.Intersection.toString());

		// Create a simple CSG tree

		union1.addEntity(sphere1);
		union1.addEntity(complement1);
		union1.addEntity(intersection1);
		complement1.addEntity(cube1);

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

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider();

		// Create a shape
		ShapeMesh geometryModel = new ShapeMesh();
		AbstractView geometryView = new AbstractView();
		ShapeController geometryShape = new ShapeController(geometryModel,
				geometryView);

		ShapeController sphere1 = (ShapeController) geometryShape.clone();
		sphere1.setProperty(MeshProperty.TYPE, ShapeType.Sphere.toString());
		ShapeController cube1 = (ShapeController) geometryShape.clone();
		cube1.setProperty(MeshProperty.TYPE, ShapeType.Cube.toString());

		ShapeController union1 = (ShapeController) geometryShape.clone();
		union1.setProperty(GeometryMeshProperty.OPERATOR,
				OperatorType.Union.toString());
		ShapeController complement1 = (ShapeController) geometryShape.clone();
		complement1.setProperty(GeometryMeshProperty.OPERATOR,
				OperatorType.Complement.toString());
		ShapeController intersection1 = (ShapeController) geometryShape.clone();
		intersection1.setProperty(GeometryMeshProperty.OPERATOR,
				OperatorType.Intersection.toString());

		// Create a simple CSG tree

		union1.addEntity(sphere1);
		union1.addEntity(complement1);
		union1.addEntity(intersection1);
		complement1.addEntity(cube1);

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

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider();

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

		ShapeTreeContentProvider shapeProvider = new ShapeTreeContentProvider();

		ShapeMesh geometryModel = new ShapeMesh();
		geometryModel.setProperty(MeshProperty.TYPE, ShapeType.Sphere.toString());
		AbstractView geometryView = new AbstractView();
		ShapeController geometry = new ShapeController(geometryModel,
				geometryView);

		ShapeController sphere1 = (ShapeController) geometry.clone();
		ShapeController cube1 = (ShapeController) geometry.clone();
		ShapeController union1 = (ShapeController) geometry.clone();
		ShapeController complement1 = (ShapeController) geometry.clone();
		ShapeController intersection1 = (ShapeController) geometry.clone();

		// Put them all in a GeometryComponent

		GeometryComponent geometryComponent = new GeometryComponent();
		geometryComponent.setGeometry(geometry);
		geometryComponent.getGeometry().addEntity(sphere1);
		geometryComponent.getGeometry().addEntity(cube1);
		geometryComponent.getGeometry().addEntity(union1);
		geometryComponent.getGeometry().addEntity(complement1);
		geometryComponent.getGeometry().addEntity(sphere1);

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