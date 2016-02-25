/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.eavp.viz.service.connections.preferences.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.connections.preferences.PortEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizAllowedValueType;
import org.junit.Test;

/**
 * This class verifies the behavior of the {@link PortEntryContentProvider}.
 * 
 * @author Jordan Deyton
 *
 */
public class PortEntryContentProviderTester {

	/**
	 * Verifies that the default values for a {@link PortEntryContentProvider}
	 * are properly set when instantiated.
	 */
	@Test
	public void checkConstruction() {

		PortEntryContentProvider contentProvider;
		List<String> allowedValues;
		int value = -1;

		// We will test the nullary constructor.
		contentProvider = new PortEntryContentProvider();

		// Check the default allowed values. It should be a list of two strings,
		// with the first being the min port number and the second the max port
		// number (both integers).
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());

		// Check the min port number. This should be a non-null, parseable
		// integer. Its default value should be the MIN_PORT_PREFERRED.
		value = parseIntString(allowedValues.get(0));
		assertEquals(PortEntryContentProvider.MIN_PORT_PREFERRED, value);

		// Check the max port number. This should be a non-null, parseable
		// integer. Its default value should be the MAX_PORT.
		value = parseIntString(allowedValues.get(1));
		assertEquals(PortEntryContentProvider.MAX_PORT, value);

		// Check the default port. It should be MIN_PORT_PREFERRED.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(PortEntryContentProvider.MIN_PORT_PREFERRED, value);

		return;
	}

	/**
	 * Tests that the default value can only be set to a valid port number
	 * within the allowed values, and that if the allowed values change, the
	 * default value is clamped to the range.
	 */
	@Test
	public void checkDefaultValue() {
		PortEntryContentProvider contentProvider;
		int min = -1;
		int max = -1;
		int defaultPort = -1;
		int value = -1;
		final String nullString = null;

		contentProvider = new PortEntryContentProvider();

		// ---- Set the default value with the default port range. ---- //
		// We do not need to check the default port set by the constructor, as
		// it is tested in checkConstruction().

		// Try valid ports with both setters.

		// First try a valid port. We should be able to set it as the default.
		defaultPort = 5000;
		contentProvider.setDefaultValue(defaultPort);
		// Check the value.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Now try using the string-based setter.
		defaultPort = 65535;
		contentProvider.setDefaultValue(Integer.toString(defaultPort));
		// Check the value.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Try some invalid values with both setters.

		// Try using a port that's too big.
		contentProvider.setDefaultValue(1000000);
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Try using a port that's too small.
		contentProvider.setDefaultValue(1023);
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Try a big number with the string-based setter.
		contentProvider.setDefaultValue(Integer.toString(65536));
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Try a small number with the string-based setter.
		contentProvider.setDefaultValue(Integer.toString(-1));
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Using an invalid string shouldn't work either...

		// Try using a null string.
		contentProvider.setDefaultValue(nullString);
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Try using a non-numeric string.
		contentProvider.setDefaultValue("Wilsooooooooonnn!!!!");
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);
		// ------------------------------------------------------------ //

		// ---- Set the default value with a new port range. ---- //
		// Set the much smaller range.
		min = 5000;
		max = 5500;
		contentProvider.setRange(min, max);

		// Since the current default is well above the new max port, it should
		// become the max port.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(max, value);

		// Try the same tests in the section above but with the new range.

		// Try valid ports with both setters.

		// First try a valid port. We should be able to set it as the default.
		defaultPort = 5100;
		contentProvider.setDefaultValue(defaultPort);
		// Check the value.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Now try using the string-based setter.
		defaultPort = 5101;
		contentProvider.setDefaultValue(Integer.toString(defaultPort));
		// Check the value.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Try some invalid values with both setters.

		// Try using a port that's too big.
		contentProvider.setDefaultValue(max + 1);
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Try using a port that's too small.
		contentProvider.setDefaultValue(min - 1010);
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Try a big number with the string-based setter.
		contentProvider.setDefaultValue(Integer.toString(max + 12345));
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Try a small number with the string-based setter.
		contentProvider.setDefaultValue(Integer.toString(min - 1));
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Using an invalid string shouldn't work either...

		// Try using a null string.
		contentProvider.setDefaultValue(nullString);
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Try using a non-numeric string.
		contentProvider.setDefaultValue("Ooohh nooo!!! Wilsooonnn!!");
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);
		// ------------------------------------------------------ //

		// ---- Check that the default value clamps to the port range. ---- //
		// We already checked that clamping works to *decrease* the default
		// value (see the beginning of the above section).

		// Check that the default value doesn't change if it's still inside the
		// valid range.
		min -= 10;
		max += 10;
		contentProvider.setRange(min, max);
		// The value should not have changed.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(defaultPort, value);

		// Check that clamping works to *increase* the default value.
		min += 1000;
		max += 1000;
		contentProvider.setRange(min, max);
		// Since the current default is well below the new min port, it should
		// become the min port.
		value = parseIntString(contentProvider.getDefaultValue());
		assertEquals(min, value);
		// ---------------------------------------------------------------- //

		return;
	}

	/**
	 * Tests that the allowed values are set properly.
	 */
	@Test
	public void checkAllowedValues() {
		PortEntryContentProvider contentProvider;
		ArrayList<String> allowedValues;
		int min = -1;
		int max = -1;
		int value;
		final String nullString = null;

		contentProvider = new PortEntryContentProvider();

		// There is no need to check the default allowed values as they are
		// tested in checkConstruction().

		// ---- Check setting valid allowed values. ---- //
		// Try setting the port range with the default, int-based setter.
		min = PortEntryContentProvider.MIN_PORT;
		max = PortEntryContentProvider.MAX_PORT;
		contentProvider.setRange(min, max);

		// Check the current allowed values. They should be the same as min/max.
		// First, check the size of the list.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Try setting the port range with the list-based setter.
		min = 55000;
		max = 60000;
		allowedValues = new ArrayList<String>(2);
		allowedValues.add(Integer.toString(min));
		allowedValues.add(Integer.toString(max));
		contentProvider.setAllowedValues(allowedValues);

		// Check the current allowed values. They should be the same as min/max.
		// First, check the size of the list.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);
		// --------------------------------------------- //

		// ---- Check setting invalid allowed values (int setter). ---- //
		// Try setting an invalid range (min greater than max).
		contentProvider.setRange(500, 499);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Try setting a range below the absolute min port value.
		contentProvider.setRange(PortEntryContentProvider.MIN_PORT - 1, 500);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Try setting a range above the absolute max port value.
		contentProvider.setRange(500, PortEntryContentProvider.MAX_PORT + 1);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Try a range with both an invalid min and max.
		contentProvider.setRange(PortEntryContentProvider.MIN_PORT - 1,
				PortEntryContentProvider.MAX_PORT + 1);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);
		// ------------------------------------------------------------ //

		// ---- Check setting invalid allowed values (string setter). ---- //

		// First, try passing invalid lists.

		// Try a null list.
		allowedValues = null;
		contentProvider.setAllowedValues(allowedValues);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Try a list with the null values.
		allowedValues = new ArrayList<String>(2);
		allowedValues.add(nullString);
		allowedValues.add(Integer.toString(10));
		contentProvider.setAllowedValues(allowedValues);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Try a list with invalid values.
		allowedValues = new ArrayList<String>(2);
		allowedValues.add(Integer.toString(10));
		allowedValues.add("invalid value");
		contentProvider.setAllowedValues(allowedValues);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Try a list with valid values but the wrong size.
		allowedValues = new ArrayList<String>(3);
		allowedValues.add(Integer.toString(10));
		allowedValues.add(Integer.toString(10));
		allowedValues.add(Integer.toString(10));
		contentProvider.setAllowedValues(allowedValues);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Now do the same tests as in the previous section (no invalid lists,
		// but invalid integers in the list).

		// Try setting an invalid range (min greater than max).
		allowedValues = new ArrayList<String>(2);
		allowedValues.add(Integer.toString(1000));
		allowedValues.add(Integer.toString(500));
		contentProvider.setAllowedValues(allowedValues);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Try setting a range below the absolute min port value.
		allowedValues = new ArrayList<String>(2);
		allowedValues.add(Integer
				.toString(PortEntryContentProvider.MIN_PORT - 1));
		allowedValues.add(Integer.toString(500));
		contentProvider.setAllowedValues(allowedValues);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Try setting a range above the absolute max port value.
		allowedValues = new ArrayList<String>(2);
		allowedValues.add(Integer.toString(1000));
		allowedValues.add(Integer
				.toString(PortEntryContentProvider.MAX_PORT + 1));
		contentProvider.setAllowedValues(allowedValues);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);

		// Try a range with both an invalid min and max.
		allowedValues = new ArrayList<String>(2);
		allowedValues.add(Integer
				.toString(PortEntryContentProvider.MIN_PORT - 1));
		allowedValues.add(Integer
				.toString(PortEntryContentProvider.MAX_PORT + 1));
		contentProvider.setAllowedValues(allowedValues);
		// The current allowed values should be the same as before.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(2, allowedValues.size());
		// Check the min port number.
		value = parseIntString(allowedValues.get(0));
		assertEquals(min, value);
		// Check the max port number.
		value = parseIntString(allowedValues.get(1));
		assertEquals(max, value);
		// --------------------------------------------------------------- //

		return;
	}

	/**
	 * Tests that the allowed value type is always {@code Continuous}.
	 */
	@Test
	public void checkAllowedValueType() {

		PortEntryContentProvider contentProvider;
		final VizAllowedValueType nullType = null;

		contentProvider = new PortEntryContentProvider();

		// The default type should be "Continuous".
		assertEquals(VizAllowedValueType.Continuous,
				contentProvider.getAllowedValueType());

		// Trying to set any valid type should change nothing.
		for (VizAllowedValueType type : VizAllowedValueType.values()) {
			contentProvider.setAllowedValueType(type);
			assertEquals(VizAllowedValueType.Continuous,
					contentProvider.getAllowedValueType());
		}

		// Trying to set a null type should change nothing.
		contentProvider.setAllowedValueType(nullType);
		assertEquals(VizAllowedValueType.Continuous,
				contentProvider.getAllowedValueType());

		return;
	}

	/**
	 * Checks the equals and hash code methods for an object, an equivalent
	 * object, an unequal object, and invalid arguments.
	 */
	@Test
	public void checkEquals() {

		PortEntryContentProvider object;
		Object equalObject;
		Object unequalObject;
		BasicVizEntryContentProvider superObject;

		// Set up the object under test.
		object = new PortEntryContentProvider();
		object.setName("Plo Koon");
		object.setRange(60000, 60010);

		// Set up the equivalent object.
		equalObject = new PortEntryContentProvider();
		((PortEntryContentProvider) equalObject).setName("Plo Koon");
		((PortEntryContentProvider) equalObject).setRange(60000, 60010);

		// Set up the different object.
		unequalObject = new PortEntryContentProvider();
		((PortEntryContentProvider) unequalObject).setName("Plo Koon");
		((PortEntryContentProvider) unequalObject).setRange(59999, 60010); // Different!

		// Set up the super object.
		superObject = new BasicVizEntryContentProvider();
		superObject.copy(object);

		// Check for equivalence (reflective case).
		assertTrue(object.equals(object));
		assertEquals(object.hashCode(), object.hashCode());

		// Check that the object and its equivalent object are, in fact, equal,
		// and that their hash codes match.
		assertNotSame(object, equalObject);
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));
		assertTrue(object.hashCode() == equalObject.hashCode());

		// Check that the object and the different object are not equal and that
		// their hash codes are different.
		assertNotSame(object, unequalObject);
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));
		assertFalse(object.hashCode() == unequalObject.hashCode());
		// Verify with the equivalent object as well.
		assertFalse(equalObject.equals(unequalObject));
		assertFalse(unequalObject.equals(equalObject));
		assertFalse(equalObject.hashCode() == unequalObject.hashCode());

		// Test invalid arguments.
		assertFalse(object.equals(null));
		assertFalse(object.equals("Kuze"));

		// Test against a super-class object that is technically equivalent.
		// While the super class may think it is equivalent, the same should not
		// be true in the reverse direction.
		assertNotSame(object, superObject);
		assertTrue(superObject.equals(object));
		assertFalse(object.equals(superObject));
		// Their hash codes should also be different.
		assertFalse(object.hashCode() == superObject.hashCode());

		return;
	}

	/**
	 * Checks the copy and clone methods.
	 */
	@Test
	public void checkCopy() {

		PortEntryContentProvider object;
		PortEntryContentProvider copy;
		Object clone;

		final PortEntryContentProvider nullObject = null;

		// Create the initial object that will be copied and cloned later.
		object = new PortEntryContentProvider();
		object.setRange(737, 747);
		object.setName("Dengar");

		// Copying it should yield an equivalent object.
		copy = new PortEntryContentProvider(object);
		// After copying, check that they're unique references but equal.
		copy.copy(object);
		assertNotSame(object, copy);
		assertEquals(object, copy);
		assertEquals(copy, object);

		// Cloning it should yield an equivalent object of the correct type.
		clone = object.clone();
		// Check that it's a non-null object of the correct type.
		assertNotNull(clone);
		assertTrue(clone instanceof PortEntryContentProvider);
		// Check that they're unique references but equal.
		assertNotSame(object, clone);
		assertEquals(object, clone);
		assertEquals(clone, object);

		// Check invalid arguments to the copy constructor.
		try {
			copy = new PortEntryContentProvider(nullObject);
		} catch (NullPointerException e) {
			// Exception thrown as expected. Nothing to do.
		}

		return;
	}

	/**
	 * A convenience method to parse the specified string as an integer. It will
	 * return the parsed integer, but will fail (with a helpful method) if the
	 * string is null or otherwise cannot be parsed.
	 * 
	 * @param string
	 *            The string to parse.
	 * @return The parsed integer value, if the string can be parsed.
	 */
	private int parseIntString(String string) {
		int parsedInt = -1;

		// Try to parse it, failing if the number could not be parsed.
		// Note that null strings cannot be parsed, and a NumberFormatException
		// is thrown in that case.
		try {
			parsedInt = Integer.parseInt(string);
		} catch (NumberFormatException e) {
			fail("Could not parse the string \"" + string + "\".");
		}

		return parsedInt;
	}
}
