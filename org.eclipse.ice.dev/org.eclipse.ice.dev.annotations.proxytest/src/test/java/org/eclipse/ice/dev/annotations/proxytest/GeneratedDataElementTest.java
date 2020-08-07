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
package org.eclipse.ice.dev.annotations.proxytest;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.ice.data.JavascriptValidator;
import org.junit.jupiter.api.Test;

/**
 * This class tests the DataElement class. With the exception of testing for
 * serialization to a string, it is sufficient to test this class with T=String.
 * For testing serialization, multiple types need to be tested.
 *
 * @author Jay Jay Billings
 * @author Daniel Bluhm
 *
 */
class GeneratedDataElementTest {

	/**
	 * This is a helper function for creating GeneratedDataElementImplementation values that are
	 * used in most of the tests.
	 *
	 * @param data the value that should be stored in the element.
	 * @return the data
	 */
	private GeneratedDataElement getStringElement(final String data) {

		GeneratedDataElement element = new GeneratedDataElementImplementation();
		element.setTestField(data);

		return element;
	}

	/**
	 * Test method for verifying expected defaults are present and modifiable.
	 */
	@Test
	void testDefaultProps() {

		// Use a basic string element for this test since it is looking at content on
		// the base class.
		GeneratedDataElement element = getStringElement("Phutureprimitive");

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

		// Check the getters
		assertEquals(element.getName(), name);
		assertEquals(element.getDescription(), description);
		assertEquals(element.getId(), id);
		assertEquals(element.getComment(), comment);
		assertEquals(element.getContext(), context);

		// Check the boolean property default values
		assertEquals(element.isRequired(), required);
		assertEquals(element.isSecret(), secret);

		// Make sure that adding validators works superficially
		JavascriptValidator validator = new JavascriptValidator();
		element.setValidator(validator);
		assertEquals(element.getValidator(), validator);

		// Make sure that the UUID is not null
		assertNotNull(element.getUUID());

		return;
	}

	/**
	 * Test method for accessing generated fields.
	 */
	@Test
	void testDataAccessors() {

		// Basic intrinsic class is good for this test
		GeneratedDataElement element = getStringElement("Phutureprimitive");

		// No properties are configured here. Just want to make sure that the data
		// behaves as expected.

		// Do the straight check
		assertEquals("Phutureprimitive", element.getTestField());

		// Make sure that changing the value works round-trip
		String data = element.getTestField();
		data = "The Glitch Mob";
		element.setTestField(data);
		assertEquals("The Glitch Mob", element.getTestField());

		return;
	}

	/**
	 * Test generated DataElement Implementation with POJO as a DataField.
	 */
	@Test
	void testDataAccessorsForPOJOs() {

		// Use a test POJO for this that has members
		GeneratedDataElementPOJO element = new GeneratedDataElementPOJOImplementation();
		element.setTestPOJO(new TestPOJO());

		// Do the straight check
		TestPOJO pojo = element.getTestPOJO();
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
	 * Test generated DataElement Implementation with Intrinsic class as DataField.
	 */
	@Test
	void testStringSerialization() {

		// Basic intrinsic class is good for this test
		GeneratedDataElement element = getStringElement("Major Lazer & La Roux");
		element.setSecret(true);
		element.setRequired(true);
		element.setValidator(new JavascriptValidator());

		// Because of the private id changing and being unique, this cannot be checked
		// against a reference but can only be checked by inversion.
		String output = element.toJson();

		// Change some values then read back in the original to make sure fromString()
		// correctly overwrites them.
		element.setTestField("Eastern Sun");
		System.out.println(output);
		GeneratedDataElement element2 = getStringElement("Emancipator");
		element2.setValidator(new JavascriptValidator());
		element2.fromJson(output);
		element.fromJson(output);
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
		GeneratedDataElementPOJO element = new GeneratedDataElementPOJOImplementation();
		element.setTestPOJO(new TestPOJO());
		element.setSecret(true);
		element.setRequired(true);
		element.setValidator(new JavascriptValidator());

		// Because of the private id changing and being unique, this cannot be checked
		// against a reference but can only be checked by inversion.
		String output = element.toJson();

		// Change some values then read back in the original to make sure fromString()
		// correctly overwrites them.
		GeneratedDataElementPOJO element2 = new GeneratedDataElementPOJOImplementation();
		TestPOJO pojo2 = new TestPOJO();
		pojo2.setDoubleValue(1.072);
		element2.setValidator(new JavascriptValidator());
		element2.setTestPOJO(pojo2);
		element2.fromJson(output);

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
		GeneratedDataElement element = getStringElement("Halsey");
		GeneratedDataElement element2 = getStringElement("Halsey");
		GeneratedDataElement element3 = getStringElement("Billie Eilish");
		GeneratedDataElement element4 = getStringElement("Halsey");

		// Need a validator for the tests that is shared on the equal elements.
		JavascriptValidator validator = new JavascriptValidator();
		element.setValidator(validator);
		element2.setValidator(validator);
		element4.setValidator(validator);
		// Billie needs her own validator
		element3.setValidator(new JavascriptValidator());

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
		GeneratedDataElement elementClone = (GeneratedDataElementImplementation) element.clone();
		// Check reflexivity first
		assertEquals(element, element);
		// Check symmetry
		assertEquals(element, elementClone);
		assertEquals(elementClone, element);
		// This is a sneaky way to use a copy constructor to check transitivity and get
		// around the UUID thing
		try {
			element4 = (GeneratedDataElement) new GeneratedDataElementImplementation(
				(GeneratedDataElementImplementation) elementClone
			);
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
