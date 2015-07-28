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
package org.eclipse.ice.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.form.geometry.ICEShape;
import org.eclipse.ice.datastructures.test.TestComponentListener;
import org.eclipse.ice.viz.service.jme3.shapes.OperatorType;
import org.eclipse.ice.viz.service.jme3.shapes.ShapeType;
import org.eclipse.ice.viz.service.jme3.shapes.Transformation;
import org.junit.Test;

/**
 * <p>
 * Tests the ComplexShape class
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ICEShapeTester {
	/**
	 * <p>
	 * Checks setting and getting the OperatorType of a ComplexShape
	 * </p>
	 * <p>
	 * After a new ComplexShape is added, its OperatorType should be None. A
	 * second OperatorType (Union, for example) should be set and checked on the
	 * ComplexShape. A third OperatorType should be set, but since setting is
	 * only allowed for new ComplexShapes, its final type should be the second
	 * given OperatorType.
	 * </p>
	 * 
	 */
	@Test
	public void checkType() {

		// Instantiate a ComplexShape
		ICEShape complexShape = new ICEShape(OperatorType.None);

		assertEquals(OperatorType.None, complexShape.getOperatorType());

		// Set the operator type
		complexShape.setOperatorType(OperatorType.Union);
		assertEquals(OperatorType.Union, complexShape.getOperatorType());

		// Set a second operator type
		// This operation should be ignored since the ComplexShape was
		// previously set.
		complexShape.setOperatorType(OperatorType.Complement);
		assertEquals(OperatorType.Union, complexShape.getOperatorType());

		// Instantiate another ComplexShape
		complexShape = new ICEShape(OperatorType.None);
		assertEquals(OperatorType.None, complexShape.getOperatorType());

		// Try setting null
		complexShape.setOperatorType(null);
		assertEquals(OperatorType.None, complexShape.getOperatorType());
		
		// Create a new PrimitiveShape and verify that its ShapeType is None
		ICEShape primitiveShape = new ICEShape(ShapeType.None);
		assertEquals(ShapeType.None, primitiveShape.getShapeType());

		// Try setting a type and rechecking it
		primitiveShape.setShapeType(ShapeType.Sphere);
		assertEquals(ShapeType.Sphere, primitiveShape.getShapeType());

		// Set another type. It shouldn't change the ShapeType

		primitiveShape.setShapeType(ShapeType.Cube);
		assertEquals(ShapeType.Sphere, primitiveShape.getShapeType());

		// Create a new PrimitiveShape with a parameterized constructor
		primitiveShape = new ICEShape(ShapeType.Cylinder);
		assertEquals(ShapeType.Cylinder, primitiveShape.getShapeType());

		// Try changing the ShapeType
		primitiveShape.setShapeType(ShapeType.Cube);
		assertEquals(ShapeType.Cylinder, primitiveShape.getShapeType());

		// Finally, try setting null on a new PrimitiveShape
		primitiveShape = new ICEShape(ShapeType.None);
		primitiveShape.setShapeType(null);
		assertEquals(ShapeType.None, primitiveShape.getShapeType());

		// See if it sets after attempting to set null
		primitiveShape.setShapeType(ShapeType.Sphere);
		assertEquals(ShapeType.Sphere, primitiveShape.getShapeType());

		// Try setting the type again
		primitiveShape.setShapeType(ShapeType.Cube);
		assertEquals(ShapeType.Sphere, primitiveShape.getShapeType());

	}

	/**
	 * <p>
	 * Attempts to add a PrimitiveShape with each available ShapeType (including
	 * None) to a ComplexShape to verify that all can be added correctly
	 * </p>
	 * 
	 */
	@Test
	public void checkAddPrimitiveShape() {

		// Instantiate a ComplexShape
		ICEShape complexShape = new ICEShape(OperatorType.None);

		// The initial shapes list should be empty
		assertEquals(0, complexShape.getShapes().size());

		// Add a PrimitiveShape
		ICEShape childShape = new ICEShape(ShapeType.Sphere);
		complexShape.addShape(childShape);

		assertEquals(1, complexShape.getShapes().size());
		assertEquals(childShape, complexShape.getShapes().get(0));

		// Remove the PrimitiveShape
		complexShape.removeShape(childShape);
		assertEquals(0, complexShape.getShapes().size());

		// Set a shapes list
		ArrayList<ICEShape> shapesList = new ArrayList<ICEShape>();
		shapesList.add(childShape);

		complexShape.setShapes(shapesList);
		assertEquals(shapesList, complexShape.getShapes());

		// Pass some null values to a new ComplexShape
		complexShape = new ICEShape();

		complexShape.addShape(null);
		assertEquals(0, complexShape.getShapes().size());

		complexShape.setShapes(null);
		assertEquals(0, complexShape.getShapes().size());

	}

	/**
	 * <p>
	 * Adds a new ComplexShape as a child to the existing ComplexShape and
	 * verifies that it can be accepted
	 * </p>
	 * 
	 */
	@Test
	public void checkAddComplexShape() {

		// Create a new ComplexShape
		ICEShape complexShape = new ICEShape(OperatorType.Union);

		// Add a new ComplexShape as a child
		ICEShape childShape = new ICEShape(OperatorType.Intersection);
		complexShape.addShape(childShape);

		assertEquals(1, complexShape.getShapes().size());
		assertEquals(childShape, complexShape.getShapes().get(0));

		// Remove the ComplexShape
		complexShape.removeShape(childShape);
		assertEquals(0, complexShape.getShapes().size());

		// Try adding itself...?!
		complexShape.addShape(complexShape);
		assertEquals(0, complexShape.getShapes().size());

	}

	/**
	 * <p>
	 * This operation checks the ability of the ComplexShape to update its
	 * Entries.
	 * </p>
	 * 
	 */
	@Test
	public void checkUpdate() {
		// Not implemented
	}

	/**
	 * <p>
	 * This operation tests the ComplexShape to ensure that it can properly
	 * dispatch notifications when it receives an update that changes its state.
	 * </p>
	 * 
	 */
	@Test
	public void checkNotifications() {
		// Setup the listener
		TestComponentListener testComponentListener = new TestComponentListener();

		// Setup the PrimitiveShape
		ICEShape complexShape = new ICEShape(OperatorType.None);

		// Register the listener
		complexShape.register(testComponentListener);

		// Trigger a notification with setType()
		complexShape.setOperatorType(OperatorType.Complement);

		// Check the Listener
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Trigger a notification with setTransformation
		Transformation transformation = complexShape.getTransformation();
		transformation.setRotation(0, 0, 13);
		complexShape.setTransformation(transformation);

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Trigger a notification with setProperty
		complexShape.setProperty("key", "value");

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Change the name of the component
		complexShape.setName("Donald Trump");

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Change the id of the component
		complexShape.setId(899);

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		return;
	}

	/**
	 * This operation checks the ability of the ComplexShape to persist itself
	 * to XML and to load itself from an XML input stream.
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 */
	@Test
	public void checkLoadingFromXML() throws NullPointerException, JAXBException, IOException {

		// Local Declarations
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(ICEShape.class);
		ICEShape loadComplexShape;

		// Instantiate PrimitiveShape
		ICEShape primitiveShape = new ICEShape(ShapeType.None);
		primitiveShape.setId(25);
		primitiveShape.setDescription("description");
		primitiveShape.setName("name");

		primitiveShape.setProperty("key", "value");
		primitiveShape.setOperatorType(OperatorType.Union);

		Transformation transformation = new Transformation();
		transformation.setScale(0, 0, -1.0);
		primitiveShape.setTransformation(transformation);

		// Load it into XML
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(primitiveShape, classList, outputStream);

		// convert information inside of outputStream to inputStream
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// String xmlFile2 = new String(outputStream.toByteArray());
		// System.err.println("File: " + xmlFile2);

		// load contents into xml
		loadComplexShape = new ICEShape();
		loadComplexShape = (ICEShape) xmlHandler.read(classList, inputStream);

		// Check contents
		assertTrue(loadComplexShape.equals(primitiveShape));

	}

	/**
	 * <p>
	 * This operation checks the ComplexShape to ensure that its equals() and
	 * hashcode() operations work.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {
		// Create Transformation to test
		ICEShape component = new ICEShape(ShapeType.None);
		ICEShape equalComponent = new ICEShape(ShapeType.None);
		ICEShape unEqualComponent = new ICEShape(ShapeType.None);
		ICEShape transitiveComponent = new ICEShape(ShapeType.None);
		ICEShape unequalPropertiesComponent = new ICEShape(ShapeType.None);

		// Change values
		component.setOperatorType(OperatorType.Union);
		equalComponent.setOperatorType(OperatorType.Union);
		transitiveComponent.setOperatorType(OperatorType.Union);
		unequalPropertiesComponent.setOperatorType(OperatorType.Union);

		Transformation transformation = new Transformation();
		transformation.setSize(2.0);

		component.setTransformation(transformation);
		equalComponent.setTransformation(transformation);
		transitiveComponent.setTransformation(transformation);
		unequalPropertiesComponent.setTransformation(transformation);

		component.setProperty("key!", "value!");
		equalComponent.setProperty("key!", "value!");
		transitiveComponent.setProperty("key!", "value!");
		unequalPropertiesComponent.setProperty("key!", " o(^-^)o ");

		unEqualComponent.setOperatorType(OperatorType.Intersection);

		// Set ICEObject data
		component.setId(1);
		equalComponent.setId(1);
		transitiveComponent.setId(1);
		unEqualComponent.setId(2);

		component.setName("DC Equal");
		equalComponent.setName("DC Equal");
		transitiveComponent.setName("DC Equal");
		unEqualComponent.setName("DC UnEqual");

		// Assert two equal ComplexShapes return true
		assertTrue(component.equals(equalComponent));

		// Assert two unequal ComplexShapes return false
		assertFalse(component.equals(unEqualComponent));

		// Assert equals() is reflexive
		assertTrue(component.equals(component));

		// Assert the equals() is Symmetric
		assertTrue(component.equals(equalComponent)
				&& equalComponent.equals(component));

		// Assert equals() is transitive
		if (component.equals(equalComponent)
				&& equalComponent.equals(transitiveComponent)) {
			assertTrue(component.equals(transitiveComponent));
		} else {
			fail();
		}

		// Assert equals is consistent
		assertTrue(component.equals(equalComponent)
				&& component.equals(equalComponent)
				&& component.equals(equalComponent));
		assertTrue(!component.equals(unEqualComponent)
				&& !component.equals(unEqualComponent)
				&& !component.equals(unEqualComponent));

		// Assert checking equality with null is false
		assertFalse(component==null);

		// Assert that two equal objects return same hashcode
		assertTrue(component.equals(equalComponent)
				&& component.hashCode() == equalComponent.hashCode());

		// Assert that hashcode is consistent
		assertTrue(component.hashCode() == component.hashCode());

		// Assert that hashcodes from unequal objects are different
		assertTrue(component.hashCode() != unEqualComponent.hashCode());

		// Check that unequalPropertiesComponent is unequal and has unequal
		// hashcode
		assertFalse(component.equals(unequalPropertiesComponent));
		assertFalse(component.hashCode() == unequalPropertiesComponent
				.hashCode());
	}

	/**
	 * <p>
	 * This operation tests the construction of the ComplexShape class and the
	 * functionality inherited from ICEObject.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreation() {

		// Test the nullery constructor
		ICEShape complexShape = new ICEShape(OperatorType.None);
		assertEquals(OperatorType.None, complexShape.getOperatorType());

		// Test the parameterized constructor with a valid OperatorType
		complexShape = new ICEShape(OperatorType.Intersection);
		assertEquals(OperatorType.Intersection, complexShape.getOperatorType());

		// Test the parameterized constructor with a null OperatorType
		complexShape = new ICEShape((OperatorType) null);
		assertEquals(OperatorType.None, complexShape.getOperatorType());

	}

	/**
	 * <p>
	 * This operation checks the ComplexShape to ensure that its copy() and
	 * clone() operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		ICEShape cloneComplexShape, copyComplexShape;

		// Set up ICEObject stuff for ComplexShape
		ICEShape complexShape = new ICEShape(OperatorType.None);
		complexShape.setId(25);
		complexShape
				.setDescription("I AM A PRIMITIVESHAPE!!!1!1!!1!1eleven!!1!1");
		complexShape.setName("PrimitiveShape of Awesomeness");

		// Set up PrimitiveShape-specific stuff
		complexShape.setProperty("kei", "valeu");
		complexShape.setOperatorType(OperatorType.Complement);

		Transformation transformation = new Transformation();
		transformation.setTranslation(2, 0, 3498.0);
		complexShape.setTransformation(transformation);

		// Clone contents
		cloneComplexShape = (ICEShape) complexShape.clone();

		assertNotNull(cloneComplexShape);

		// Check equality of contents
		assertTrue(cloneComplexShape.equals(complexShape));

		// Copy contents
		copyComplexShape = new ICEShape();
		copyComplexShape.copy(complexShape);

		// Check equality of contents
		assertTrue(copyComplexShape.equals(complexShape));

		// Pass null into copy contents, show nothing has changed
		copyComplexShape.copy(null);

		// Check equality of contents
		assertTrue(copyComplexShape.equals(complexShape));

		// Check whether a change will make the complex shape unequal
		copyComplexShape.addShape(new ICEShape(ShapeType.Cone));
		assertFalse(copyComplexShape.equals(complexShape));

	}

	/**
	 * <p>
	 * Checks that the parent of a shape is properly set when added to a
	 * ComplexShape
	 * </p>
	 * 
	 */
	public void checkParent() {

		// Make some shapes

		ICEShape union = new ICEShape(OperatorType.Union);
		ICEShape intersection = new ICEShape(OperatorType.Intersection);
		ICEShape sphere = new ICEShape(ShapeType.Sphere);
		ICEShape cube = new ICEShape(ShapeType.Cube);

		union.addShape(intersection);
		union.addShape(sphere);
		intersection.addShape(cube);

		// Check that each shape has correct parents

		assertNull(union.getShapeParent());
		assertEquals(union, intersection.getShapeParent());
		assertEquals(union, sphere.getShapeParent());
		assertEquals(intersection, cube.getShapeParent());

		// Remove the shapes from their parents

		union.removeShape(intersection);
		union.removeShape(sphere);
		intersection.removeShape(cube);

		// Check that all the parents are null

		assertNull(union.getShapeParent());
		assertNull(intersection.getShapeParent());
		assertNull(sphere.getShapeParent());
		assertNull(cube.getShapeParent());

		// Add them back to their parents

		union.addShape(intersection);
		intersection.addShape(cube);

		// Modify the shapes list manually

		ArrayList<ICEShape> unionShapes = union.getShapes();

		unionShapes.add(sphere);
		unionShapes.remove(intersection);
		union.setShapes(unionShapes);

		// Check for proper parents

		assertNull(union.getShapeParent());
		assertEquals(union, sphere.getShapeParent());
		assertNull(intersection.getShapeParent());
		assertEquals(intersection, cube.getShapeParent());

	}
}