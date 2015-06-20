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

import org.eclipse.ice.datastructures.ICEObject.Component;
import org.eclipse.ice.datastructures.form.geometry.Transformation;
import org.junit.Test;

/**
 * <p>
 * Tests the functionality of AbstractShape
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class AbstractShapeTester {
	/**
	 * <p>
	 * Checks for the correct behavior of setting and getting a new key/value,
	 * setting and getting an old key with a new value, and attempting to get a
	 * nonexistent key
	 * </p>
	 * <p>
	 * In the case of a nonexistent key, a blank string should be returned.
	 * </p>
	 * 
	 */
	@Test
	public void checkProperties() {

		TestShape testShape = new TestShape();

		// Check setting and getting an unused property
		assertTrue(testShape.setProperty("name", "Henry"));
		assertEquals("Henry", testShape.getProperty("name"));

		assertTrue(testShape.setProperty("lover", "Henry"));
		assertEquals("Henry", testShape.getProperty("lover"));

		assertTrue(testShape.setProperty("Henry", "That's my name!"));
		assertEquals("That's my name!", testShape.getProperty("Henry"));

		// Try setting a null key
		assertFalse(testShape.setProperty(null, "nothing!"));
		assertNull(testShape.getProperty(null));

		assertFalse(testShape.setProperty(null, null));
		assertNull(testShape.getProperty(null));

		assertFalse(testShape.setProperty(null, ""));
		assertNull(testShape.getProperty(null));

		// Try setting an empty key
		assertFalse(testShape.setProperty("", "fgsfds"));
		assertNull(testShape.getProperty(""));

		// Overwrite a value
		assertTrue(testShape.setProperty("name",
				"Taumatawhakatangihangakoauauotamateapokaiwhenuakitanatahu"));
		assertEquals(
				"Taumatawhakatangihangakoauauotamateapokaiwhenuakitanatahu",
				testShape.getProperty("name"));

		// Delete a value
		assertTrue(testShape.setProperty("Henry", ""));
		assertEquals("", testShape.getProperty("Henry"));

		// Try to delete a nonexistent value
		assertFalse(testShape.removeProperty("Location_of_Atlantis"));

		// Delete an existent value
		assertTrue(testShape.setProperty("key", "name"));
		assertTrue(testShape.removeProperty("key"));
		assertNull(testShape.getProperty("key"));

		// Delete null
		assertFalse(testShape.removeProperty(null));

		// Try setting a null value
		assertFalse(testShape.setProperty("key2", null));
		assertNull(testShape.getProperty("key2"));

	}

	/**
	 * <p>
	 * This operation checks the shape to ensure that it can be correctly
	 * visited by a realization of the IComponent interface.
	 * </p>
	 * 
	 */
	@Test
	public void checkVisitation() {

		// Instantiate TestVisitor
		TestVisitor testVisitor = new TestVisitor();

		// Instantiate TestShape
		Component unknownComponent = new TestShape();

		// Call accept operation
		unknownComponent.accept(testVisitor);

		// Check that testVisitor was visited
		assertTrue(testVisitor.wasVisited());

	}

	/**
	 * <p>
	 * This operation tests the shape to ensure that it can properly dispatch
	 * notifications when it receives an update that changes its state.
	 * </p>
	 * 
	 */
	@Test
	public void checkNotifications() {
		// Setup the listener
		TestComponentListener testComponentListener = new TestComponentListener();

		// Setup the TestShape
		TestShape testShape = new TestShape();
		testShape.register(testComponentListener);

		// Trigger a notification with setTransformation
		Transformation transformation = testShape.getTransformation();
		transformation.setRotation(1.1, 0.1, 0.0);
		testShape.setTransformation(transformation);

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Trigger a notification with setProperty
		testShape.setProperty("key", "value");

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Change the name of the component
		testShape.setName("Donald Trump");

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Change the id of the component
		testShape.setId(899);

		// Check the listener to make sure it was updated
		assertTrue(testComponentListener.wasNotified());

		return;
	}

	/**
	 * <p>
	 * This operation checks the shape to ensure that its equals() and
	 * hashcode() operations work.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {
		// Create Transformation to test
		TestShape component = new TestShape();
		TestShape equalComponent = new TestShape();
		TestShape unEqualComponent = new TestShape();
		TestShape transitiveComponent = new TestShape();
		TestShape unequalPropertiesComponent = new TestShape();

		Transformation transformation = new Transformation();
		transformation.setScale(2.0, 3.0, 2.0e-4);

		component.setTransformation(transformation);
		equalComponent.setTransformation(transformation);
		transitiveComponent.setTransformation(transformation);
		unequalPropertiesComponent.setTransformation(transformation);

		component.setProperty("key!", "value!");
		equalComponent.setProperty("key!", "value!");
		transitiveComponent.setProperty("key!", "value!");
		unequalPropertiesComponent.setProperty("key!", " o(^-^)o ");

		unEqualComponent.setTransformation(new Transformation());

		// Set ICEObject data
		component.setId(1);
		equalComponent.setId(1);
		transitiveComponent.setId(1);
		unEqualComponent.setId(2);

		component.setName("DC Equal");
		equalComponent.setName("DC Equal");
		transitiveComponent.setName("DC Equal");
		unEqualComponent.setName("DC UnEqual");

		// Assert two equal TestShapes return true
		assertTrue(component.equals(equalComponent));

		// Assert two unequal TestShapes return false
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
	 * This operation checks the shape to ensure that its copy() and clone()
	 * operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {
		TestShape cloneTestShape, copyTestShape;

		// Set up ICEObject stuff for TestShape
		TestShape testShape = new TestShape();
		testShape.setId(25);
		testShape.setDescription("I AM A TESTSHAPE!!!1!1!!1!1eleven!!1!1");
		testShape.setName("TestShape of Awesomeness");

		// Set up IShape-specific stuff
		testShape.setProperty("kei", "valeu");

		Transformation transformation = new Transformation();
		transformation.setSkew(2, 0, 3498.0);
		testShape.setTransformation(transformation);

		// Clone contents
		cloneTestShape = (TestShape) testShape.clone();

		assertNotNull(cloneTestShape);

		// Check equality of contents
		assertTrue(cloneTestShape.equals(testShape));

		// Copy contents
		copyTestShape = new TestShape();
		copyTestShape.copy(testShape);

		// Check equality of contents
		assertTrue(copyTestShape.equals(testShape));

		// Pass null into copy contents, show nothing has changed
		copyTestShape.copy(null);

		// Check equality of contents
		assertTrue(copyTestShape.equals(testShape));

	}

	/**
	 * <p>
	 * Checks whether the initial transformation matrix of a new shape matches
	 * that of a new Matrix4x4
	 * </p>
	 * 
	 */
	@Test
	public void checkGetTransformation() {
		TestShape testShape = new TestShape();

		// Check that the transformation is instantiated to an identity matrix
		Transformation transformation = new Transformation();
		assertEquals(transformation, testShape.getTransformation());

		// Modify and set the transformation
		transformation.setTranslation(2, 2, 2380780.4537);
		assertTrue(testShape.setTransformation(transformation));
		assertEquals(transformation, testShape.getTransformation());

		// Deal with null
		assertFalse(testShape.setTransformation(null));
		assertEquals(transformation, testShape.getTransformation());

	}
}