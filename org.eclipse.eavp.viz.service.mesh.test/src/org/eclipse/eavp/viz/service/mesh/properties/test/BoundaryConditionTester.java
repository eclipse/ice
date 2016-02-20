/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz.service.mesh.properties.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.eavp.viz.service.datastructures.VizObject.VizJAXBHandler;
import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryCondition;
import org.eclipse.eavp.viz.service.mesh.datastructures.BoundaryConditionType;
import org.eclipse.eavp.viz.service.mesh.datastructures.test.TestComponentListener;
import org.junit.Test;

/**
 * <p>
 * Tests the BoundaryCondition class.
 * </p>
 * 
 * @author Jordan H. Deyton, Anna Wojtowicz
 */
public class BoundaryConditionTester {
	/**
	 * <p>
	 * This operation tests the construction of BoundaryCondition objects.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreation() {

		/**
		 * Check both constructors. Make sure the condition's type enum is
		 * properly set and that its values are not null and contains 5 values,
		 * defaulting to five zeroes.
		 */

		BoundaryCondition condition;
		BoundaryConditionType expectedType;

		/* ---- Check the nullary constructor. ---- */
		condition = new BoundaryCondition();

		// Check the type.
		expectedType = BoundaryConditionType.None;
		assertEquals(expectedType, condition.getType());

		// Make sure all 5 are valid numbers, specifically 0.0f.
		assertEquals(5, condition.getValues().size());
		// Make sure all 5 numbers are 0.0.
		try {
			for (Float valueFloat : condition.getValues()) {
				assertEquals(0f, valueFloat, 1e-7f);
			}
		} catch (NumberFormatException e) {
			fail("BoundaryConditionTester: Non-float found in condition's values.");
		}
		/* ---------------------------------------- */

		/* ---- Check the default constructor. ---- */
		condition = new BoundaryCondition(BoundaryConditionType.Flux);

		// Check the type.
		expectedType = BoundaryConditionType.Flux;
		assertEquals(expectedType, condition.getType());

		// Make sure the value contains at least 5 values.
		assertEquals(5, condition.getValues().size());
		// Make sure all 5 are valid numbers, specifically 0.0f.
		try {
			for (Float valueFloat : condition.getValues()) {
				assertEquals(0f, valueFloat, 1e-7f);
			}
		} catch (NumberFormatException e) {
			fail("BoundaryConditionTester: Non-float found in condition's values.");
		}
		/* ---------------------------------------- */
		return;
	}

	/**
	 * <p>
	 * This operation tests getters and setters for the BoundaryCondition's
	 * type.
	 * </p>
	 * 
	 */
	@Test
	public void checkType() {

		/**
		 * Try setting the type to different valid and invalid values. First, a
		 * valid value. Second, try a null value. Last, try another valid value.
		 */

		BoundaryCondition condition = new BoundaryCondition();

		// Try a valid type.
		condition.setType(BoundaryConditionType.Insulated);
		assertEquals(BoundaryConditionType.Insulated, condition.getType());

		// Try an invalid type.
		condition.setType(null);
		// The type should not have changed.
		assertEquals(BoundaryConditionType.Insulated, condition.getType());

		// Try a valid type.
		condition.setType(BoundaryConditionType.Outflow);
		assertEquals(BoundaryConditionType.Outflow, condition.getType());

		return;
	}

	/**
	 * <p>
	 * This operation tests getters and setters for the BoundaryCondition's
	 * values.
	 * </p>
	 * 
	 */
	@Test
	public void checkValues() {

		/**
		 * Try setting valid and invalid values for the condition. First, try a
		 * valid string (a String with 5 floats). Then, try a null string. Then,
		 * try an invalid string (a String with 4 floats).
		 */

		BoundaryCondition condition = new BoundaryCondition();
		ArrayList<Float> values = new ArrayList<Float>();
		ArrayList<Float> expectedValues = new ArrayList<Float>();

		// Try a valid set of values.
		values.add(1.0f);
		values.add(4.1f);
		values.add(0f);
		values.add(0f);
		values.add(0f);

		expectedValues.add(1.0f);
		expectedValues.add(4.1f);
		expectedValues.add(0f);
		expectedValues.add(0f);
		expectedValues.add(0f);
		condition.setValues(values);
		assertEquals(expectedValues, condition.getValues());

		// Try a null set of values.
		values = null;
		condition.setValues(values);
		assertEquals(expectedValues, condition.getValues());

		// Try an invalid set of values with only 4 floats.
		values = new ArrayList<Float>();
		values.add(2.0000f);
		values.add(3.14159265f);
		values.add(0f);
		values.add(0.414f);
		condition.setValues(values); // Shouldn't change
		assertEquals(expectedValues, condition.getValues());

		// Try a set with a null in it.
		values = new ArrayList<Float>();
		values.add(0f);
		values.add(0f);
		values.add(null);
		values.add(0f);
		values.add(0f);
		condition.setValues(values); // Shouldn't change
		assertEquals(expectedValues, condition.getValues());

		/* ---- Make sure you cannot directly modify the list. ---- */
		values = condition.getValues();
		expectedValues = condition.getValues();

		// Try to modify the retrieved list. Nothing should change.
		values.clear();
		assertFalse(values.equals(condition.getValues()));
		assertTrue(expectedValues.equals(condition.getValues()));

		// Try to modify the list passed in. Nothing should change.
		values = condition.getValues();
		values.set(0, 7f);
		// Get a copy of the current values.
		expectedValues = new ArrayList<Float>(values);
		condition.setValues(values);
		assertTrue(expectedValues.equals(condition.getValues()));
		// Now attempt the modification.
		values.set(0, 501231.431f);
		// The list passed in is changed, but the one stored inside the
		// BoundaryCondition instance does not.
		assertFalse(values.equals(condition.getValues()));
		assertTrue(expectedValues.equals(condition.getValues()));
		assertEquals(7f, condition.getValues().get(0), 1e-7f);
		/* -------------------------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * This operation ensures that the BoundaryCondition properly notifies
	 * registered listeners when its properties are set.
	 * </p>
	 * 
	 */
	@Test
	public void checkNotifications() {

		/**
		 * We just need to test that setType and setValues notifies listeners.
		 */

		// Create a BoundaryCondition and register a test listener with it.
		BoundaryCondition condition = new BoundaryCondition();
		TestComponentListener listener = new TestComponentListener();
		condition.register(listener);

		// Check that setting the type fires a notification.
		condition.setType(BoundaryConditionType.Flux);
		assertTrue(listener.wasNotified());
		listener.reset();

		// Check that setting the values fires a notification.
		ArrayList<Float> values = new ArrayList<Float>();
		values.add(1.0f);
		values.add(1.0f);
		values.add(0.0f);
		values.add(0.0f);
		values.add(0.0f);
		condition.setValues(values);
		assertTrue(listener.wasNotified());
		listener.reset();

		// Normally, I would check that invalid arguments to these methods do
		// not notify listeners, but the wasNotified() method is expensive.

		return;
	}

	/**
	 * <p>
	 * This operation checks the ability of the BoundaryCondition to persist
	 * itself to XML and to load itself from an XML input stream.
	 * </p>
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 * 
	 */
	@Test
	public void checkLoadingFromXML() throws NullPointerException, JAXBException, IOException {

		// Local Declarations
		VizJAXBHandler xmlHandler = new VizJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(BoundaryCondition.class);
		BoundaryCondition condition = new BoundaryCondition();

		/* ---- Perform the XML test. ---- */
		// Load it into XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(condition, classList, outputStream);

		// Convert the output stream data to an input stream.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Load the input stream's contents into a new component.
		BoundaryCondition loadedCondition = new BoundaryCondition();
		loadedCondition = (BoundaryCondition) xmlHandler.read(classList, inputStream);

		// Make sure the two components match.
		assertTrue(condition.equals(loadedCondition));

		// Check invalid parameters.

		return;
	}

	/**
	 * <p>
	 * This operation checks BoundaryCondition to ensure that its equals() and
	 * hashCode() operations work.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Initialize objects for testing.
		BoundaryCondition object = new BoundaryCondition();
		BoundaryCondition equalObject = new BoundaryCondition();
		BoundaryCondition unequalObject = new BoundaryCondition();

		// Set up the equal objects.
		object.setType(BoundaryConditionType.Wall);
		equalObject.setType(BoundaryConditionType.Wall);

		// Set up the unequal object.
		unequalObject.setType(BoundaryConditionType.Flux);

		// Make sure the references are different.
		assertFalse(object == equalObject);
		assertFalse(object == unequalObject);
		assertFalse(equalObject == unequalObject);

		// Check that equality is reflexive and symmetric.
		assertTrue(object.equals(object));
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Check that equals will fail when it should.
		assertFalse(object==null);
		assertFalse(object.equals(42));
		assertFalse("just a string".equals(object));
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));

		// Check the hash codes.
		assertTrue(object.hashCode() == object.hashCode());
		assertTrue(object.hashCode() == equalObject.hashCode());
		assertFalse(object.hashCode() == unequalObject.hashCode());

		return;
	}

	/**
	 * <p>
	 * This operation checks BoundaryCondition to ensure that its copy() and
	 * clone() operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Initialize objects for testing.
		BoundaryCondition object = new BoundaryCondition();
		BoundaryCondition copy = new BoundaryCondition();
		BoundaryCondition clone = null;
		ArrayList<Float> values = new ArrayList<Float>();

		// Set up the object.
		object.setType(BoundaryConditionType.MovingBoundary);
		object.setValues(values);

		// Make sure the objects are not equal before copying.
		assertFalse(object == copy);
		assertFalse(object.equals(copy));

		// Copy the object.
		copy.copy(object);

		// Make sure the references are different but contents the same.
		assertFalse(object == copy);
		assertTrue(object.equals(copy));

		// Do the same for the clone operation.

		// Make sure the objects are not equal before copying.
		assertFalse(object == clone);
		assertFalse(object.equals(clone));

		// Copy the object.
		clone = (BoundaryCondition) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}
}