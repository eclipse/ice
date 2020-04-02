/*******************************************************************************
 * Copyright (c) 2020- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.tests.renderer;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Properties;

import org.eclipse.ice.renderer.DataElement;
import org.eclipse.ice.renderer.Validator;
import org.junit.jupiter.api.Test;

/**
 * This class tests the DataElement class. With the exception of testing for
 * serialization to a string, it is sufficient to test this class with T=String.
 * For testing serialization, multiple types need to be tested.
 * 
 * @author Jay Jay Billings
 *
 */
class DataElementTest {

	/**
	 * This is a helper function for creating DataElement<String> values that are
	 * used in most of the tests.
	 * 
	 * @param data the value that should be stored in the element.
	 * @return the data
	 */
	private DataElement<String> getStringElement(final String data) {

		DataElement<String> element = new DataElement<String>();
		element.setData(data);

		return element;
	}

	/**
	 * Test method for {@link org.eclipse.ice.renderer.DataElement#getProperties()}.
	 */
	@Test
	void testProperties() {

		// Use a basic string element for this test since it is looking at content on
		// the base class.
		DataElement<String> element = getStringElement("Phutureprimitive");

		// Grab the test properties map and make sure all the default properties
		// provided by the spec are available and set to initial values.
		Properties props = element.getProperties();
		assertEquals(props.get("name"), "name");
		assertEquals(props.get("description"), "description");
		assertEquals(props.get("id"), "0");
		assertEquals(props.get("comment"), "no comment");
		assertEquals(props.get("context"), "default");

		// Add a custom property
		try {
			element.setProperty("sail", "awolnation");
		} catch (Exception e1) {
			// Complain
			e1.printStackTrace();
			fail();
		}

		// Make sure it isn't in properties right now since a copy is returned.
		assertFalse(props.containsKey("sail"));

		// Get the properties again and check it now
		props = element.getProperties();
		assertEquals("awolnation", props.get("sail"));

		// Now check the getters
		assertEquals(element.getName(), "name");
		assertEquals(element.getDescription(), "description");
		assertEquals(element.getId(), 0);
		assertEquals(element.getComment(), "no comment");
		assertEquals(element.getContext(), "default");

		// Check the boolean property default values
		assertEquals(element.isRequired(), false);
		assertEquals(element.isSecret(), false);

		// Setup new values for checking setters
		String name = "rock";
		String description = "round garden rock";
		long id = 1L;
		String comment = "Brown with a pumpkin next to it";
		String context = "garden";
		boolean required = true;
		boolean secret = true;

		// Set all the properties
		try {
			element.setComment(comment);
			element.setContext(context);
			element.setId(id);
			element.setName(name);
			element.setDescription(description);
			element.setRequired(required);
			element.setSecret(secret);
		} catch (Exception e) {
			// Complain
			e.printStackTrace();
			fail();
		}

		// Grab the test properties map and make sure all the default properties
		// provided by the spec are available and set to initial values.
		props = element.getProperties();
		assertEquals(props.get("name"), name);
		assertEquals(props.get("description"), description);
		assertEquals(props.get("id"), String.valueOf(id));
		assertEquals(props.get("comment"), comment);
		assertEquals(props.get("context"), context);

		// Now check the getters
		assertEquals(element.getName(), name);
		assertEquals(element.getDescription(), description);
		assertEquals(element.getId(), id);
		assertEquals(element.getComment(), comment);
		assertEquals(element.getContext(), context);

		// Check the boolean property default values
		assertEquals(element.isRequired(), required);
		assertEquals(element.isSecret(), secret);

		// Make sure that adding validators works superficially
		Validator validator = new Validator();
		element.setValidator(validator);
		assertEquals(element.getValidator(), validator);

		// Make sure that the UUID is not null
		assertNotNull(element.getUUID());

		return;
	}

	/**
	 * Test method for {@link org.eclipse.ice.renderer.DataElement#getData()} and
	 * {@link org.eclipse.ice.renderer.DataElement#setData(java.lang.Object)}.
	 */
	@Test
	void testDataAccessors() {

		// Basic intrinsic class is good for this test
		DataElement<String> element = getStringElement("Phutureprimitive");

		// No properties are configured here. Just want to make sure that the data
		// behaves as expected.

		// Do the straight check
		assertEquals("Phutureprimitive", element.getData());

		// Make sure that changing the value works round-trip
		String data = element.getData();
		data = "The Glitch Mob";
		element.setData(data);
		assertEquals("The Glitch Mob", element.getData());

		return;
	}

	/**
	 * Test method for {@link org.eclipse.ice.renderer.DataElement#getData()} and
	 * {@link org.eclipse.ice.renderer.DataElement#setData(java.lang.Object)} when
	 * non-intrinsic POJOs are used.
	 */
	@Test
	void testDataAccessorsForPOJOs() {

		// Use a test POJO for this that has members
		DataElement<TestPOJO> element = new DataElement<TestPOJO>();
		element.setData(new TestPOJO());

		// No properties are configured here. Just want to make sure that the data
		// behaves as expected.

		// Do the straight check
		TestPOJO pojo = element.getData();
		assertEquals("foo", pojo.getValue());
		assertEquals(2118.0, pojo.getDoubleValue(), 1.0e-15);

		// Make sure that changing the value works round-trip
		pojo.setDoubleValue(1234.0);
		pojo.setValue("bar");
		assertEquals("bar", pojo.getValue());
		assertEquals(1234.0, pojo.getDoubleValue(), 1.0e-15);

		return;
	}

	/**
	 * Test method for {@link org.eclipse.ice.renderer.DataElement#toString()} and
	 * {@link org.eclipse.ice.renderer.DataElement#toString()} for intrinsic
	 * classes.
	 */
	@Test
	void testStringSerialization() {

		// Basic intrinsic class is good for this test
		DataElement<String> element = getStringElement("Major Lazer & La Roux");
		element.setSecret(true);
		element.setRequired(true);

		// Add a custom property to make sure they are included in serialization
		try {
			element.setProperty("sail", "awolnation");
		} catch (Exception e) {
			// Complain
			e.printStackTrace();
			fail();
		}

		// Because of the private id changing and being unique, this cannot be checked
		// against a reference but can only be checked by inversion.
		String output = element.toString();

		// Change some values then read back in the original to make sure fromString()
		// correctly overwrites them.
		element.setData("Eastern Sun");
		DataElement<String> element2 = getStringElement("Emancipator");
		element2.fromString(output);
		element.fromString(output);
		assertEquals(element,element2);

		return;
	}

	/**
	 * Test method for {@link org.eclipse.ice.renderer.DataElement#toString()} and
	 * {@link org.eclipse.ice.renderer.DataElement#toString()} for POJOs.
	 */
	@Test
	void testPOJOSerialization() {

		// Use a test POJO for this that has members
		DataElement<TestPOJO> element = new DataElement<TestPOJO>();
		element.setData(new TestPOJO());
		element.setSecret(true);
		element.setRequired(true);

		// Add a custom property to make sure they are included in serialization
		try {
			element.setProperty("sail", "awolnation");
		} catch (Exception e) {
			// Complain
			e.printStackTrace();
			fail();
		}
		
		// Because of the private id changing and being unique, this cannot be checked
		// against a reference but can only be checked by inversion.
		String output = element.toString();
		
		// Change some values then read back in the original to make sure fromString()
		// correctly overwrites them.
		DataElement<TestPOJO> element2 = new DataElement<TestPOJO>();
		TestPOJO pojo2 = new TestPOJO();
		pojo2.setDoubleValue(1.072);
		element2.setData(pojo2);
		element2.fromString(output);
		
		assertEquals(element,element2);

		return;
	}

	/**
	 * Test method for
	 * {@link org.eclipse.ice.renderer.DataElement#matches(DataElement)},
	 * {@link org.eclipse.ice.renderer.DataElement#equals(java.lang.Object)},
	 * {@link org.eclipse.ice.renderer.DataElement#hashCode()},
	 * {@link org.eclipse.ice.renderer.DataElement#copy()}, and
	 * {@link org.eclipse.ice.renderer.DataElement#clone()}.
	 */
	@Test
	void testEquality() {

		// Basic intrinsic class is good for this test
		DataElement<String> element = getStringElement("Halsey");
		DataElement<String> element2 = getStringElement("Halsey");
		DataElement<String> element3 = getStringElement("Billie Eilish");
		DataElement<String> element4 = getStringElement("Halsey");

		// Need a validator for the tests that is shared on the equal elements.
		Validator validator = new Validator();
		element.setValidator(validator);
		element2.setValidator(validator);
		element4.setValidator(validator);
		// Billie needs her own validator
		element3.setValidator(new Validator());

		// Data elements must be checked both for matching - a deep inequality except
		// the UUID - and for a fully complete match that contains the UUID. Start with
		// matching. Check reflexivity first
		assertTrue(element.matches(element));
		// Check symmetry
		assertTrue(element.matches(element2));
		assertTrue(element2.matches(element));
		// Check transitivity
		assertTrue(element.matches(element2));
		assertTrue(element2.matches(element4));
		assertTrue(element.matches(element4));
		// Check a wrong answer
		assertFalse(element.matches(element3));
		// Check null
		assertFalse(element.matches(null));

		// Now check equality with a clone - therefore also confirming clone works
		DataElement<String> elementClone = (DataElement<String>) element.clone();
		// Check reflexivity first
		assertEquals(element, element);
		// Check symmetry
		assertEquals(element, elementClone);
		assertEquals(elementClone, element);
		// This is a sneaky way to use a copy constructor to check transitivity and get
		// around the UUID thing
		try {
			element4 = new DataElement<String>(elementClone);
		} catch (Exception e) {
			// Complain
			e.printStackTrace();
			fail();
		}
		assertEquals(element4, element);
		// Check a wrong answer
		assertFalse(element.equals(element3));
		// Check null
		assertFalse(element.equals(null));
		// Check against a string - this should fail
		assertFalse(element.equals("Halsey"));

		// Check hashCode()
		assertEquals(element.hashCode(), elementClone.hashCode());

		return;
	}
}
