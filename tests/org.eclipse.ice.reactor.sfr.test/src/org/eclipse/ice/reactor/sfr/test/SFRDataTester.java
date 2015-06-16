/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor.sfr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.reactor.sfr.base.SFRData;
import org.junit.Test;

/**
 * <p>
 * Tests the operations of the SFRData class.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class SFRDataTester {

	/**
	 * <p>
	 * Tests the constructors and default values of the SFRData class.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		SFRData data;

		// Default values:
		String feature = "Feature 1";

		List<Double> position = new ArrayList<Double>(3);
		position.add(0.0);
		position.add(0.0);
		position.add(0.0);

		double uncertainty = 0.0;
		String units = "seconds";
		double value = 0.0;

		/* ---- Check nullary constructor. ---- */
		data = new SFRData();

		assertEquals(feature, data.getFeature());
		assertEquals(position, data.getPosition());
		assertEquals(uncertainty, data.getUncertainty(), 0);
		assertEquals(units, data.getUnits());
		assertEquals(value, data.getValue(), 0);
		/* ------------------------------------ */

		/* ---- Check feature-based constructor. ---- */
		data = new SFRData("Infinite Improbability Device");

		assertEquals("Infinite Improbability Device", data.getFeature());
		assertEquals(position, data.getPosition());
		assertEquals(uncertainty, data.getUncertainty(), 0);
		assertEquals(units, data.getUnits());
		assertEquals(value, data.getValue(), 0);
		/* ------------------------------------------ */

		/* ---- Check invalid constructor (null). ---- */
		data = new SFRData(null);

		assertEquals(feature, data.getFeature());
		assertEquals(position, data.getPosition());
		assertEquals(uncertainty, data.getUncertainty(), 0);
		assertEquals(units, data.getUnits());
		assertEquals(value, data.getValue(), 0);
		/* ------------------------------------------- */

		/* ---- Check invalid constructor (blank). ---- */
		data = new SFRData("   ");

		assertEquals(feature, data.getFeature());
		assertEquals(position, data.getPosition());
		assertEquals(uncertainty, data.getUncertainty(), 0);
		assertEquals(units, data.getUnits());
		assertEquals(value, data.getValue(), 0);
		/* -------------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Tests the getter and setter of the value attribute.
	 * </p>
	 * 
	 */
	@Test
	public void checkValue() {

		// Initialize a new SFRData.
		SFRData data = new SFRData();

		// Check the default value.
		double value = 0.0;
		assertEquals(value, data.getValue(), 0);
		data.setValue(0.0);
		assertEquals(value, data.getValue(), 0);

		// Update the value.
		value = 42.0;
		data.setValue(42.0);
		assertEquals(value, data.getValue(), 0);

		// Try setting it to some big values.
		value = Double.MAX_VALUE;
		data.setValue(Double.MAX_VALUE);
		assertEquals(value, data.getValue(), 0);

		// Try setting it to some really small values.
		value = Double.MIN_VALUE;
		data.setValue(Double.MIN_VALUE);
		assertEquals(value, data.getValue(), 0);

		return;
	}

	/**
	 * <p>
	 * Tests the getter and setter for the uncertainty attribute.
	 * </p>
	 * 
	 */
	@Test
	public void checkUncertainty() {

		// Initialize a new SFRData.
		SFRData data = new SFRData();

		// Check the default uncertainty.
		double uncertainty = 0.0;
		assertEquals(uncertainty, data.getUncertainty(), 0);
		data.setUncertainty(0.0);
		assertEquals(uncertainty, data.getUncertainty(), 0);

		// Update the value.
		uncertainty = 42.0;
		data.setUncertainty(42.0);
		assertEquals(uncertainty, data.getUncertainty(), 0);

		// Try setting it to some big values.
		uncertainty = Double.MAX_VALUE;
		data.setUncertainty(Double.MAX_VALUE);
		assertEquals(uncertainty, data.getUncertainty(), 0);

		// Try setting it to some really small values.
		uncertainty = Double.MIN_VALUE;
		data.setUncertainty(Double.MIN_VALUE);
		assertEquals(uncertainty, data.getUncertainty(), 0);

		return;
	}

	/**
	 * <p>
	 * Tests the getter and setter of the units attribute.
	 * </p>
	 * 
	 */
	@Test
	public void checkUnits() {

		// Initialize a new SFRData.
		SFRData data = new SFRData();

		String defaultString = "seconds";
		String normalString = "minutes";
		String emptyString = "";
		String whitespaceString = "  	";
		String normalStringPlus = " minutes 	";

		// Check the default.
		assertEquals(defaultString, data.getUnits());

		// Check setting it to a null String.
		data.setUnits(null);
		assertEquals(defaultString, data.getUnits());

		// Check setting it to an empty String.
		data.setUnits(emptyString);
		assertEquals(defaultString, data.getUnits());

		// Check setting it to a String with whitespace only.
		data.setUnits(whitespaceString);
		assertEquals(defaultString, data.getUnits());

		// Check setting it to a normal String.
		data.setUnits(normalString);
		assertEquals(normalString, data.getUnits());

		// Revert it to default temporarily.
		data.setUnits(defaultString);
		assertEquals(defaultString, data.getUnits());

		// Check setting it to a normal String with whitespace.
		data.setUnits(normalStringPlus);
		assertEquals(normalString, data.getUnits());

		return;
	}

	/**
	 * <p>
	 * Tests the getter and setter of the feature attribute.
	 * </p>
	 * 
	 */
	@Test
	public void checkFeature() {

		// Initialize a new SFRData.
		SFRData data = new SFRData();

		String defaultString = "Feature 1";
		String normalString = "Dimensions Traversed";
		String emptyString = "";
		String whitespaceString = "  	";
		String normalStringPlus = " Dimensions Traversed 	";

		// Check the default.
		assertEquals(defaultString, data.getFeature());

		// Check setting it to a null String.
		data.setFeature(null);
		assertEquals(defaultString, data.getFeature());

		// Check setting it to an empty String.
		data.setFeature(emptyString);
		assertEquals(defaultString, data.getFeature());

		// Check setting it to a String with whitespace only.
		data.setFeature(whitespaceString);
		assertEquals(defaultString, data.getFeature());

		// Check setting it to a normal String.
		data.setFeature(normalString);
		assertEquals(normalString, data.getFeature());

		// Revert it to default temporarily.
		data.setFeature(defaultString);
		assertEquals(defaultString, data.getFeature());

		// Check setting it to a normal String with whitespace.
		data.setFeature(normalStringPlus);
		assertEquals(normalString, data.getFeature());

		return;
	}

	/**
	 * <p>
	 * Tests the getter and setter for the position attribute.
	 * </p>
	 * 
	 */
	@Test
	public void checkPosition() {

		// Initialize a new SFRData.
		SFRData data = new SFRData();

		// Initialize the default position.
		ArrayList<Double> defaultPosition = new ArrayList<Double>();
		defaultPosition.add(0.0);
		defaultPosition.add(0.0);
		defaultPosition.add(0.0);

		// Make sure the default position is set.
		assertEquals(defaultPosition, data.getPosition());

		// Initialize a non-default position.
		ArrayList<Double> position = new ArrayList<Double>();

		// Add a position value. The list size is 1 and should not change the
		// data's position.
		position.add(1.0);
		data.setPosition(position);
		assertEquals(defaultPosition, data.getPosition());

		// Add a position value. The list size is 2 and should not change the
		// data's position.
		position.add(-1.0);
		data.setPosition(position);
		assertEquals(defaultPosition, data.getPosition());

		// Add a position value. The list size is 3 and should change the data's
		// position.
		position.add(-1.0);
		data.setPosition(position);
		assertEquals(position, data.getPosition());

		// Add a fourth position value. The list size is 4 and should not change
		// the data's position.
		position.add(-1.0);
		data.setPosition(position);
		assertFalse(position.equals(data.getPosition()));
		assertEquals(position.subList(0, 3), data.getPosition());

		// Remove the fourth element from our position.
		position.remove(3);
		assertEquals(position, data.getPosition());

		// Set it to an empty ArrayList. This should not change the position.
		data.setPosition(new ArrayList<Double>());
		assertEquals(position, data.getPosition());

		// Set it to null. This should not change the position.
		data.setPosition(null);
		assertEquals(position, data.getPosition());

		return;
	}

	/**
	 * <p>
	 * Tests the equality operation of SFRData.
	 * </p>
	 */
	@Test
	public void checkEquality() {

		SFRData object, equalObject, unequalObject, invalidObject;

		// Some values for loading into the data.
		String units = "mouse brain";
		double value = 1.0;
		double uncertainty = 0.0;
		ArrayList<Double> position = new ArrayList<Double>();
		position.add(1.0);
		position.add(2.0);
		position.add(3.0);

		// Set the information for the initial data.
		object = new SFRData("Mouse");
		object.setUnits(units);
		object.setValue(value);
		object.setUncertainty(uncertainty);
		object.setPosition(position);

		// Set the information for the equal data.
		equalObject = new SFRData("Mouse");
		equalObject.setUnits(units);
		equalObject.setValue(value);
		equalObject.setUncertainty(uncertainty);
		equalObject.setPosition(position);

		// Set the information for the unequal data.
		unequalObject = new SFRData("Human");
		unequalObject.setUnits(units);
		unequalObject.setValue(0.5);
		unequalObject.setUncertainty(0.25);
		unequalObject.setPosition(position);

		// Set the information for an invalid SFRData.
		invalidObject = new SFRData(null);
		invalidObject.setUnits(units);
		invalidObject.setValue(value);
		invalidObject.setUncertainty(uncertainty);
		invalidObject.setPosition(position);

		// Test reflexivity and symmetry.
		assertTrue(object.equals(object));
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Test hash code equality.
		assertTrue(object.hashCode() == object.hashCode());
		assertTrue(object.hashCode() == equalObject.hashCode());

		// Test inequality.
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));

		// Test hash code inequality.
		assertFalse(object.hashCode() == unequalObject.hashCode());

		// Test inequality with invalid objects.
		assertFalse(object.equals(invalidObject));
		assertFalse(invalidObject.equals(object));

		// Test inequality with null values
		assertFalse(object==null);
		assertFalse(equalObject==null);
		assertFalse(unequalObject==null);
		assertFalse(invalidObject==null);

		// Test inequality with bad values.
		assertFalse(object.equals(42));
		assertFalse("just a string".equals(object));

		return;
	}

	/**
	 * <p>
	 * Tests the copying and cloning methods of SFRData.
	 * </p>
	 */
	@Test
	public void checkCopying() {

		String feature = "Acceleration";
		String units = "ft/s/s";
		double value = 32.0;

		// Create a new SFRData with some special features and data.
		SFRData object = new SFRData("Bowl of petunias");
		object.setFeature(feature);
		object.setUnits(units);
		object.setValue(value);

		ArrayList<Double> position = new ArrayList<Double>();
		position.add(42.0);
		position.add(42000.0);
		position.add(84.0);
		object.setPosition(position);

		// Create objects for copying/cloning.
		SFRData copiedObject = new SFRData("Sperm whale");
		SFRData clonedObject = null;

		/* ---- Test passing in bad arguments to copy. ---- */
		object.copy(null);

		// Make sure we didn't clobber the old contents of object.
		assertTrue(object.getFeature().equals(feature));
		assertTrue(object.getUnits().equals(units));
		assertTrue(object.getValue() == value);
		assertTrue(object.getPosition().equals(position));
		/* ------------------------------------------------ */

		/* ---- Test copying. ---- */
		// Make sure these are different objects beforehand!
		assertFalse(object == copiedObject);
		assertFalse(object.equals(copiedObject));

		// Copy the contents of object into copiedObject.
		copiedObject.copy(object);

		// Make sure they are still different references but have the same info.
		assertFalse(object == copiedObject);
		assertTrue(object.equals(copiedObject));

		// Make sure we didn't clobber the old contents of object.
		assertTrue(object.getFeature().equals(feature));
		assertTrue(object.getUnits().equals(units));
		assertTrue(object.getValue() == value);
		assertTrue(object.getPosition().equals(position));
		/* ----------------------- */

		/* ---- Test cloning. ---- */
		// Make sure these are different objects beforehand!
		assertFalse(object.equals(clonedObject));

		// Clone the object.
		clonedObject = (SFRData) object.clone();

		// Make sure they are still different references but have the same info.
		assertFalse(object == clonedObject);
		assertTrue(object.equals(clonedObject));
		assertFalse(copiedObject == clonedObject);
		assertTrue(copiedObject.equals(clonedObject));

		// Make sure we didn't clobber the old contents of object.
		assertTrue(object.getFeature().equals(feature));
		assertTrue(object.getUnits().equals(units));
		assertTrue(object.getValue() == value);
		assertTrue(object.getPosition().equals(position));
		/* ----------------------- */

		return;
	}
}