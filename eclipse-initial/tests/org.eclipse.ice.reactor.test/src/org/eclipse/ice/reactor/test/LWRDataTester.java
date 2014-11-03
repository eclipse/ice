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
package org.eclipse.ice.reactor.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRData;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the operations on LWRData.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LWRDataTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the constructors and their default values.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code
		// Local Declarations
		LWRData data;

		// Default values for LWRData

		// Setup the defaultPosition data
		ArrayList<Double> defaultPosition = new ArrayList<Double>();
		defaultPosition.add(0.0);
		defaultPosition.add(0.0);
		defaultPosition.add(0.0);

		// Setup the rest
		double defaultValue = 0.0;
		double defaultUncertainty = 0.0;
		String defaultUnits = "seconds";
		String defaultFeature = "Feature 1";

		String newFeature = "newFEATURE";

		// Check Construction of a LWRData.
		// Instantiate LWRData
		data = new LWRData();
		// Check default values
		assertEquals(defaultPosition, data.getPosition());
		assertEquals(defaultValue, data.getValue(), 0.0);
		assertEquals(defaultUncertainty, data.getUncertainty(), 0.0);
		assertEquals(defaultUnits, data.getUnits());
		assertEquals(defaultFeature, data.getFeature());

		// Instantiate LWRData - feature
		data = new LWRData(newFeature);
		// Check default values
		assertEquals(defaultPosition, data.getPosition());
		assertEquals(defaultValue, data.getValue(), 0.0);
		assertEquals(defaultUncertainty, data.getUncertainty(), 0.0);
		assertEquals(defaultUnits, data.getUnits());
		assertEquals(newFeature, data.getFeature());

		// Instantiate LWRData - erroneous feature (null)
		data = new LWRData(null);
		// Check default values
		assertEquals(defaultPosition, data.getPosition());
		assertEquals(defaultValue, data.getValue(), 0.0);
		assertEquals(defaultUncertainty, data.getUncertainty(), 0.0);
		assertEquals(defaultUnits, data.getUnits());
		assertEquals(defaultFeature, data.getFeature()); // default when
															// erroneous feature
															// passed

		// Instantiate LWRData - erroneous feature (empty string)
		data = new LWRData("");
		// Check default values
		assertEquals(defaultPosition, data.getPosition());
		assertEquals(defaultValue, data.getValue(), 0.0);
		assertEquals(defaultUncertainty, data.getUncertainty(), 0.0);
		assertEquals(defaultUnits, data.getUnits());
		assertEquals(defaultFeature, data.getFeature()); // default when
															// erroneous feature
															// passed

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the equals and hashCode operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkEquality() {
		// begin-user-code
		// Local Declarations
		LWRData object, equalObject, unEqualObject, transitiveObject;
		String units = "inches";
		String features = "Billy";
		double uncertainty = 5.0;
		double value = 4.0;
		ArrayList<Double> position = new ArrayList<Double>();
		position.add(2.0);
		position.add(3.0);

		// Setup the Object data
		object = new LWRData();
		object.setUnits(units);
		object.setFeature(features);
		object.setPosition(position);
		object.setValue(value);
		object.setUncertainty(uncertainty);

		// Setup equalObject equal to object
		equalObject = new LWRData();
		equalObject.setUnits(units);
		equalObject.setFeature(features);
		equalObject.setPosition(position);
		equalObject.setValue(value);
		equalObject.setUncertainty(uncertainty);

		// Setup transitiveObject equal to object
		transitiveObject = new LWRData();
		transitiveObject.setUnits(units);
		transitiveObject.setFeature(features);
		transitiveObject.setPosition(position);
		transitiveObject.setValue(value);
		transitiveObject.setUncertainty(uncertainty);

		// Set its data, not equal to object
		// Does not contain components!
		unEqualObject = new LWRData();
		unEqualObject.setUnits(units);
		unEqualObject.setFeature(features + " asdasd"); // Different feature!
		unEqualObject.setPosition(position);
		unEqualObject.setValue(value);
		unEqualObject.setUncertainty(uncertainty);

		// Assert that these two objects are equal
		assertTrue(object.equals(equalObject));

		// Assert that two unequal objects returns false
		assertFalse(object.equals(unEqualObject));

		// Check that equals() is Reflexive
		// x.equals(x) = true
		assertTrue(object.equals(object));

		// Check that equals() is Symmetric
		// x.equals(y) = true iff y.equals(x) = true
		assertTrue(object.equals(equalObject) && equalObject.equals(object));

		// Check that equals() is Transitive
		// x.equals(y) = true, y.equals(z) = true => x.equals(z) = true
		if (object.equals(equalObject) && equalObject.equals(transitiveObject)) {
			assertTrue(object.equals(transitiveObject));
		} else {
			fail();
		}

		// Check the Consistent nature of equals()
		assertTrue(object.equals(equalObject) && object.equals(equalObject)
				&& object.equals(equalObject));
		assertTrue(!object.equals(unEqualObject)
				&& !object.equals(unEqualObject)
				&& !object.equals(unEqualObject));

		// Assert checking equality with null value returns false
		assertFalse(object==null);

		// Assert that two equal objects have the same hashcode
		assertTrue(object.equals(equalObject)
				&& object.hashCode() == equalObject.hashCode());

		// Assert that hashcode is consistent
		assertTrue(object.hashCode() == object.hashCode());

		// Assert that hashcodes are different for unequal objects
		assertFalse(object.hashCode() == unEqualObject.hashCode());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the copy and clone routines.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {
		// begin-user-code
		// Local declarations
		LWRData object;
		LWRData copyObject = new LWRData(), clonedObject;

		// Values
		String units = "inches";
		String features = "Billy";
		double uncertainty = 5.0;
		double value = 4.0;
		ArrayList<Double> position = new ArrayList<Double>();
		position.add(2.0);
		position.add(3.0);

		// Setup the Object data
		object = new LWRData();
		object.setUnits(units);
		object.setFeature(features);
		object.setPosition(position);
		object.setValue(value);
		object.setUncertainty(uncertainty);

		// Run the copy routine
		copyObject = new LWRData();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (LWRData) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getter and setter for position.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkPosition() {
		// begin-user-code
		// Local Declarations
		LWRData data = new LWRData();
		ArrayList<Double> position = new ArrayList<Double>();

		// Setup the nullary constructors default position
		ArrayList<Double> defaultPosition = new ArrayList<Double>();
		defaultPosition.add(0.0);
		defaultPosition.add(0.0);
		defaultPosition.add(0.0);

		// Add a position
		position.add(1.0);

		// Check setter: one position
		data.setPosition(position);
		// Check value - stays the same
		assertEquals(defaultPosition, data.getPosition());

		// Check setter: two positions
		position.add(-1.0);
		data.setPosition(position);

		// Check value
		assertEquals(defaultPosition, data.getPosition());

		// Check setter: three positions
		position.add(-1.0);
		data.setPosition(position);

		// Check value - actually works!
		assertEquals(position, data.getPosition());

		// Check setter: 4 or more positions
		position.add(-1.0);
		data.setPosition(position);
		position.remove(3); // Remove it for checking
		// Check value
		assertEquals(position, data.getPosition()); // Same as 3 positions

		// Set it back to empty - does not work
		data.setPosition(new ArrayList<Double>());
		// Check value
		assertEquals(position, data.getPosition()); // Same as 3 positions

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getter and setter for value.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkValue() {
		// begin-user-code
		// Local Declarations
		LWRData data = new LWRData();
		double posValue = 30.0;
		double negValue = -1.0;
		double zeroValue = 0.0;

		// Check setter : positive
		data.setValue(posValue);
		// Check value
		assertEquals(posValue, data.getValue(), 0.0);

		// Check setter : negative
		data.setValue(negValue);
		// Check value
		assertEquals(negValue, data.getValue(), 0.0);

		// Check setter : zero
		data.setValue(zeroValue);
		// Check value
		assertEquals(zeroValue, data.getValue(), 0.0);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getter and setter for uncertainty.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkUncertainty() {
		// begin-user-code
		// Local Declarations
		LWRData data = new LWRData();
		double posValue = 30.0;
		double negValue = -1.0;
		double zeroValue = 0.0;

		// Check setter : positive
		data.setUncertainty(posValue);
		// Check value
		assertEquals(posValue, data.getUncertainty(), 0.0);

		// Check setter : negative
		data.setUncertainty(negValue);
		// Check value
		assertEquals(negValue, data.getUncertainty(), 0.0);

		// Check setter : zero
		data.setUncertainty(zeroValue);
		// Check value
		assertEquals(zeroValue, data.getUncertainty(), 0.0);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getter and setter for units.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkUnits() {
		// begin-user-code
		// Local Declarations
		LWRData data = new LWRData();

		// Values to check
		String normalString = "Bob";
		String emptyString = "";
		String spaceString = " ";
		String trimableString = "Bob ";

		// Check normal String
		data.setUnits(normalString);
		// Check value
		assertEquals(normalString, data.getUnits());

		// Check empty String
		data.setUnits(emptyString);
		// Check value - nothing changed
		assertEquals(normalString, data.getUnits());

		// Check space String
		data.setUnits(spaceString);
		// Check value - nothing changed
		assertEquals(normalString, data.getUnits());

		// Check trimable String
		data.setUnits(trimableString);
		// Check value - nothing changed
		assertEquals(normalString, data.getUnits());

		// Check null
		data.setUnits(null);
		// Check value - nothing changed
		assertEquals(normalString, data.getUnits());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getter and setter for feature.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkFeature() {
		// begin-user-code
		// Local Declarations
		LWRData data = new LWRData();

		// Values to check
		String normalString = "Bob";
		String emptyString = "";
		String spaceString = " ";
		String trimableString = "Bob  ";

		// Check normal String
		data.setFeature(normalString);
		// Check value
		assertEquals(normalString, data.getFeature());

		// Check empty String
		data.setFeature(emptyString);
		// Check value - nothing changed
		assertEquals(normalString, data.getFeature());

		// Check space String
		data.setFeature(spaceString);
		// Check value - nothing changed
		assertEquals(normalString, data.getFeature());

		// Check trimable String
		data.setFeature(trimableString);
		// Check value - nothing changed
		assertEquals(normalString, data.getFeature());

		// Check null
		data.setFeature(null);
		// Check value - nothing changed
		assertEquals(normalString, data.getFeature());
		// end-user-code
	}
}