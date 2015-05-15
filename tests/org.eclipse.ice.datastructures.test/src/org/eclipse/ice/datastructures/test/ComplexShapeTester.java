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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.form.AdaptiveTreeComposite;
import org.eclipse.ice.datastructures.form.geometry.ComplexShape;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.geometry.IShapeVisitor;
import org.eclipse.ice.datastructures.form.geometry.Transformation;
import org.eclipse.ice.datastructures.form.geometry.OperatorType;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;
import org.eclipse.ice.datastructures.form.geometry.ShapeType;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * <p>
 * Tests the ComplexShape class
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class ComplexShapeTester {
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
	public void checkOperatorType() {

		// Instantiate a ComplexShape
		ComplexShape complexShape = new ComplexShape();

		assertEquals(OperatorType.None, complexShape.getType());

		// Set the operator type
		complexShape.setType(OperatorType.Union);
		assertEquals(OperatorType.Union, complexShape.getType());

		// Set a second operator type
		// This operation should be ignored since the ComplexShape was
		// previously set.
		complexShape.setType(OperatorType.Complement);
		assertEquals(OperatorType.Union, complexShape.getType());

		// Instantiate another ComplexShape
		complexShape = new ComplexShape();
		assertEquals(OperatorType.None, complexShape.getType());

		// Try setting null
		complexShape.setType(null);
		assertEquals(OperatorType.None, complexShape.getType());

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
		ComplexShape complexShape = new ComplexShape();

		// The initial shapes list should be empty
		assertEquals(0, complexShape.getShapes().size());

		// Add a PrimitiveShape
		PrimitiveShape childShape = new PrimitiveShape(ShapeType.Sphere);
		complexShape.addShape(childShape);

		assertEquals(1, complexShape.getShapes().size());
		assertEquals(childShape, complexShape.getShapes().get(0));

		// Remove the PrimitiveShape
		complexShape.removeShape(childShape);
		assertEquals(0, complexShape.getShapes().size());

		// Set a shapes list
		ArrayList<IShape> shapesList = new ArrayList<IShape>();
		shapesList.add(childShape);

		complexShape.setShapes(shapesList);
		assertEquals(shapesList, complexShape.getShapes());

		// Pass some null values to a new ComplexShape
		complexShape = new ComplexShape();

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
		ComplexShape complexShape = new ComplexShape(OperatorType.Union);

		// Add a new ComplexShape as a child
		ComplexShape childShape = new ComplexShape(OperatorType.Intersection);
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
	 * This operation checks the ComplexShape to insure that it can be correctly
	 * visited by a realization of the IShapeVisitor interface.
	 * </p>
	 * 
	 */
	@Test
	public void checkVisitation() {

		// Instantiate TestShapeVisitor
		TestShapeVisitor visitor = new TestShapeVisitor();

		// Instantiate PrimitiveShape
		IShape unknownShape = new ComplexShape();

		// Trigger the visit
		unknownShape.acceptShapeVisitor((IShapeVisitor) visitor);

		assertEquals(1, visitor.getVisits());

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
		ComplexShape complexShape = new ComplexShape();

		// Register the listener
		complexShape.register(testComponentListener);

		// Trigger a notification with setType()
		complexShape.setType(OperatorType.Complement);

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
		classList.add(ComplexShape.class);
		ComplexShape loadComplexShape;

		// Instantiate PrimitiveShape
		ComplexShape primitiveShape = new ComplexShape();
		primitiveShape.setId(25);
		primitiveShape.setDescription("description");
		primitiveShape.setName("name");

		primitiveShape.setProperty("key", "value");
		primitiveShape.setType(OperatorType.Union);

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
		loadComplexShape = new ComplexShape();
		loadComplexShape = (ComplexShape) xmlHandler.read(classList, inputStream);

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
		ComplexShape component = new ComplexShape();
		ComplexShape equalComponent = new ComplexShape();
		ComplexShape unEqualComponent = new ComplexShape();
		ComplexShape transitiveComponent = new ComplexShape();
		ComplexShape unequalPropertiesComponent = new ComplexShape();

		// Change values
		component.setType(OperatorType.Union);
		equalComponent.setType(OperatorType.Union);
		transitiveComponent.setType(OperatorType.Union);
		unequalPropertiesComponent.setType(OperatorType.Union);

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

		unEqualComponent.setType(OperatorType.Intersection);

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
		ComplexShape complexShape = new ComplexShape();
		assertEquals(OperatorType.None, complexShape.getType());

		// Test the parameterized constructor with a valid OperatorType
		complexShape = new ComplexShape(OperatorType.Intersection);
		assertEquals(OperatorType.Intersection, complexShape.getType());

		// Test the parameterized constructor with a null OperatorType
		complexShape = new ComplexShape(null);
		assertEquals(OperatorType.None, complexShape.getType());

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

		ComplexShape cloneComplexShape, copyComplexShape;

		// Set up ICEObject stuff for ComplexShape
		ComplexShape complexShape = new ComplexShape();
		complexShape.setId(25);
		complexShape
				.setDescription("I AM A PRIMITIVESHAPE!!!1!1!!1!1eleven!!1!1");
		complexShape.setName("PrimitiveShape of Awesomeness");

		// Set up PrimitiveShape-specific stuff
		complexShape.setProperty("kei", "valeu");
		complexShape.setType(OperatorType.Complement);

		Transformation transformation = new Transformation();
		transformation.setTranslation(2, 0, 3498.0);
		complexShape.setTransformation(transformation);

		// Clone contents
		cloneComplexShape = (ComplexShape) complexShape.clone();

		assertNotNull(cloneComplexShape);

		// Check equality of contents
		assertTrue(cloneComplexShape.equals(complexShape));

		// Copy contents
		copyComplexShape = new ComplexShape();
		copyComplexShape.copy(complexShape);

		// Check equality of contents
		assertTrue(copyComplexShape.equals(complexShape));

		// Pass null into copy contents, show nothing has changed
		copyComplexShape.copy(null);

		// Check equality of contents
		assertTrue(copyComplexShape.equals(complexShape));

		// Check whether a change will make the complex shape unequal
		copyComplexShape.addShape(new PrimitiveShape(ShapeType.Cone));
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

		ComplexShape union = new ComplexShape(OperatorType.Union);
		ComplexShape intersection = new ComplexShape(OperatorType.Intersection);
		PrimitiveShape sphere = new PrimitiveShape(ShapeType.Sphere);
		PrimitiveShape cube = new PrimitiveShape(ShapeType.Cube);

		union.addShape(intersection);
		union.addShape(sphere);
		intersection.addShape(cube);

		// Check that each shape has correct parents

		assertNull(union.getParent());
		assertEquals(union, intersection.getParent());
		assertEquals(union, sphere.getParent());
		assertEquals(intersection, cube.getParent());

		// Remove the shapes from their parents

		union.removeShape(intersection);
		union.removeShape(sphere);
		intersection.removeShape(cube);

		// Check that all the parents are null

		assertNull(union.getParent());
		assertNull(intersection.getParent());
		assertNull(sphere.getParent());
		assertNull(cube.getParent());

		// Add them back to their parents

		union.addShape(intersection);
		intersection.addShape(cube);

		// Modify the shapes list manually

		ArrayList<IShape> unionShapes = union.getShapes();

		unionShapes.add(sphere);
		unionShapes.remove(intersection);
		union.setShapes(unionShapes);

		// Check for proper parents

		assertNull(union.getParent());
		assertEquals(union, sphere.getParent());
		assertNull(intersection.getParent());
		assertEquals(intersection, cube.getParent());

	}
}