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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.eclipse.ice.datastructures.ICEObject.ICEJAXBHandler;
import org.eclipse.ice.datastructures.form.MasterDetailsPair;
import org.eclipse.ice.datastructures.form.geometry.IShape;
import org.eclipse.ice.datastructures.form.geometry.IShapeVisitor;
import org.eclipse.ice.datastructures.form.geometry.Transformation;
import org.eclipse.ice.datastructures.form.geometry.PrimitiveShape;
import org.eclipse.ice.datastructures.form.geometry.ShapeType;

/**
 * <p>
 * Tests the PrimitiveShape class
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class PrimitiveShapeTester {
	/**
	 * <p>
	 * Checks setting and getting the ShapeType of a PrimitiveShape
	 * </p>
	 * <p>
	 * After a new PrimitiveShape is added, its ShapeType should be None. A
	 * second ShapeType (Sphere, for example) should be set and checked on the
	 * PrimitiveShape. A third ShapeType should be set, but since setting is
	 * only allowed for new PrimitiveShapes, its final type should be the second
	 * ShapeType.
	 * </p>
	 * 
	 */
	@Test
	public void checkShapeType() {
		// Create a new PrimitiveShape and verify that its ShapeType is None
		PrimitiveShape primitiveShape = new PrimitiveShape();
		assertEquals(ShapeType.None, primitiveShape.getType());

		// Try setting a type and rechecking it
		primitiveShape.setType(ShapeType.Sphere);
		assertEquals(ShapeType.Sphere, primitiveShape.getType());

		// Set another type. It shouldn't change the ShapeType

		primitiveShape.setType(ShapeType.Cube);
		assertEquals(ShapeType.Sphere, primitiveShape.getType());

		// Create a new PrimitiveShape with a parameterized constructor
		primitiveShape = new PrimitiveShape(ShapeType.Cylinder);
		assertEquals(ShapeType.Cylinder, primitiveShape.getType());

		// Try changing the ShapeType
		primitiveShape.setType(ShapeType.Cube);
		assertEquals(ShapeType.Cylinder, primitiveShape.getType());

		// Finally, try setting null on a new PrimitiveShape
		primitiveShape = new PrimitiveShape();
		primitiveShape.setType(null);
		assertEquals(ShapeType.None, primitiveShape.getType());

		// See if it sets after attempting to set null
		primitiveShape.setType(ShapeType.Sphere);
		assertEquals(ShapeType.Sphere, primitiveShape.getType());

		// Try setting the type again
		primitiveShape.setType(ShapeType.Cube);
		assertEquals(ShapeType.Sphere, primitiveShape.getType());

	}

	/**
	 * <p>
	 * This operation checks the PrimitiveShape to ensure that it can be
	 * correctly visited by a realization of the IShapeVisitor interface.
	 * </p>
	 * 
	 */
	@Test
	public void checkVisitation() {

		// Instantiate TestShapeVisitor
		TestShapeVisitor visitor = new TestShapeVisitor();

		// Instantiate PrimitiveShape
		IShape unknownShape = new PrimitiveShape();

		// Trigger the visit
		unknownShape.acceptShapeVisitor((IShapeVisitor) visitor);

		assertEquals(1, visitor.getVisits());
	}

	/**
	 * <p>
	 * This operation checks the ability of the PrimitiveShape to update its
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
	 * This operation tests the PrimitiveShape to insure that it can properly
	 * dispatch notifications when it receives an update that changes its state.
	 * </p>
	 * 
	 */
	@Test
	public void checkNotifications() {
		// Setup the listener
		TestComponentListener testComponentListener = new TestComponentListener();

		// Setup the PrimitiveShape
		PrimitiveShape primitiveShape = new PrimitiveShape();

		// Register the listener
		primitiveShape.register(testComponentListener);

		// Trigger a notification with setType()
		primitiveShape.setType(ShapeType.Sphere);

		// Check the Listener
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Trigger a notification with setTransformation
		Transformation transformation = primitiveShape.getTransformation();
		transformation.setTranslation(0, 0, 13);
		primitiveShape.setTransformation(transformation);

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Trigger a notification with setProperty
		primitiveShape.setProperty("key", "value");

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Change the name of the component
		primitiveShape.setName("Donald Trump");

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Change the id of the component
		primitiveShape.setId(899);

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		return;
	}

	/**
	 * This operation checks the ability of the PrimitiveShape to persist itself
	 * to XML and to load itself from an XML input stream.
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 */
	@Test
	public void checkLoadingFromXML() throws NullPointerException, JAXBException, IOException {

		// Local Declarations
		PrimitiveShape loadPrimitiveShape;
		ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(PrimitiveShape.class);

		// Instantiate PrimitiveShape
		PrimitiveShape primitiveShape = new PrimitiveShape();
		primitiveShape.setId(25);
		primitiveShape.setDescription("I AM A PRIMITIVESHAPE!");
		primitiveShape.setName("Shape of Primitivity");

		primitiveShape.setProperty("number", "nine");
		primitiveShape.setType(ShapeType.Cylinder);

		Transformation transformation = new Transformation();
		transformation.setTranslation(0, 0, -1.0);
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
		loadPrimitiveShape = new PrimitiveShape();
		loadPrimitiveShape = (PrimitiveShape) xmlHandler.read(classList, inputStream);

		// Check contents
		assertTrue(loadPrimitiveShape.equals(primitiveShape));

	}

	/**
	 * <p>
	 * This operation checks the PrimitiveShape to ensure that its equals() and
	 * hashcode() operations work.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {
		// Create Transformation to test
		PrimitiveShape component = new PrimitiveShape();
		PrimitiveShape equalComponent = new PrimitiveShape();
		PrimitiveShape unEqualComponent = new PrimitiveShape();
		PrimitiveShape transitiveComponent = new PrimitiveShape();
		PrimitiveShape unequalPropertiesComponent = new PrimitiveShape();

		// Change values
		component.setType(ShapeType.Sphere);
		equalComponent.setType(ShapeType.Sphere);
		transitiveComponent.setType(ShapeType.Sphere);
		unequalPropertiesComponent.setType(ShapeType.Sphere);

		Transformation transformation = new Transformation();
		transformation.setTranslation(2, 3, 2.0e-40);

		component.setTransformation(transformation);
		equalComponent.setTransformation(transformation);
		transitiveComponent.setTransformation(transformation);
		unequalPropertiesComponent.setTransformation(transformation);

		component.setProperty("key!", "value!");
		equalComponent.setProperty("key!", "value!");
		transitiveComponent.setProperty("key!", "value!");
		unequalPropertiesComponent.setProperty("key!", " o(^-^)o ");

		unEqualComponent.setType(ShapeType.Cube);

		// Set ICEObject data
		component.setId(1);
		equalComponent.setId(1);
		transitiveComponent.setId(1);
		unEqualComponent.setId(2);

		component.setName("DC Equal");
		equalComponent.setName("DC Equal");
		transitiveComponent.setName("DC Equal");
		unEqualComponent.setName("DC UnEqual");

		// Assert two equal PrimitiveShapes return true
		assertTrue(component.equals(equalComponent));

		// Assert two unequal PrimitiveShapes return false
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
	 * This operation tests the construction of the PrimitiveShape class and the
	 * functionality inherited from ICEObject.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreation() {
		// Instantiate PrimitiveShape
		PrimitiveShape primitiveShape = new PrimitiveShape();

		assertEquals(ShapeType.None, primitiveShape.getType());

		Transformation transformation = new Transformation();
		assertTrue(transformation.equals(primitiveShape.getTransformation()));

		// Use the non-nullery constructor
		primitiveShape = new PrimitiveShape(ShapeType.Sphere);
		assertEquals(ShapeType.Sphere, primitiveShape.getType());

		assertTrue(transformation.equals(primitiveShape.getTransformation()));

		// Instantiate with a Cube
		primitiveShape = new PrimitiveShape(ShapeType.Cube);
		assertEquals(ShapeType.Cube, primitiveShape.getType());

		assertTrue(transformation.equals(primitiveShape.getTransformation()));

		// Instantiate with a Cube
		primitiveShape = new PrimitiveShape(ShapeType.Cylinder);
		assertEquals(ShapeType.Cylinder, primitiveShape.getType());

		assertTrue(transformation.equals(primitiveShape.getTransformation()));

		// Instantiate with Null
		primitiveShape = new PrimitiveShape(null);
		assertEquals(ShapeType.None, primitiveShape.getType());

		assertTrue(transformation.equals(primitiveShape.getTransformation()));

	}

	/**
	 * <p>
	 * This operation checks the PrimitiveShape to ensure that its copy() and
	 * clone() operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {
		PrimitiveShape clonePrimitiveShape, copyPrimitiveShape;

		// Set up ICEObject stuff for PrimitiveShape
		PrimitiveShape primitiveShape = new PrimitiveShape();
		primitiveShape.setId(25);
		primitiveShape
				.setDescription("I AM A PRIMITIVESHAPE!!!1!1!!1!1eleven!!1!1");
		primitiveShape.setName("PrimitiveShape of Awesomeness");

		// Set up PrimitiveShape-specific stuff
		primitiveShape.setProperty("kei", "valeu");
		primitiveShape.setType(ShapeType.Cylinder);

		Transformation transformation = new Transformation();
		transformation.setTranslation(2, 0, 3498.0);
		primitiveShape.setTransformation(transformation);

		// Clone contents
		clonePrimitiveShape = (PrimitiveShape) primitiveShape.clone();

		assertNotNull(clonePrimitiveShape);

		// Check equality of contents
		assertTrue(clonePrimitiveShape.equals(primitiveShape));

		// Copy contents
		copyPrimitiveShape = new PrimitiveShape();
		copyPrimitiveShape.copy(primitiveShape);

		// Check equality of contents
		assertTrue(copyPrimitiveShape.equals(primitiveShape));

		// Pass null into copy contents, show nothing has changed
		copyPrimitiveShape.copy(null);

		// Check equality of contents
		assertTrue(copyPrimitiveShape.equals(primitiveShape));

	}
}