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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.eavp.viz.service.connections.preferences.KeyEntryContentProvider;
import org.eclipse.eavp.viz.service.connections.preferences.PortEntry;
import org.eclipse.eavp.viz.service.connections.preferences.PortEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.junit.Test;

/**
 * This class tests the {@link PortEntry}, which is a special "continuous"
 * {@code Entry} based on integers rather than doubles.
 * 
 * @author Jordan Deyton
 *
 */
public class PortEntryTester {

	/**
	 * This checks the constructor for {@code KeyEntry} to ensure that the
	 * default values are properly set.
	 */
	@Test
	public void checkConstruction() {

		PortEntry entry;
		PortEntryContentProvider contentProvider;
		final PortEntryContentProvider nullContentProvider = null;
		int defaultValue = 5555;
		int value = -1;

		// ---- Test construction with a valid content provider. ---- //
		// Create a new Entry with a simple content provider.
		contentProvider = new PortEntryContentProvider();
		contentProvider.setDefaultValue(defaultValue);
		entry = new PortEntry(contentProvider);

		// Make sure its default value is the same as the content provider.
		value = parseIntString(entry.getDefaultValue());
		assertEquals(defaultValue, value);

		// Its value should also be the default value.
		value = parseIntString(entry.getValue());
		assertEquals(defaultValue, value);

		// Check the allowed value type.
		assertEquals(contentProvider.getAllowedValueType(),
				entry.getValueType());
		// ---------------------------------------------------------- //

		// ---- Test construction with an invalid content provider. ---- //
		// Set up the default content provider. This is exactly the same as what
		// the Entry will use.
		contentProvider = new PortEntryContentProvider();
		defaultValue = Integer.parseInt(contentProvider.getDefaultValue());

		// Create a new Entry with a *null* content provider. It will create its
		// own default content provider.
		entry = new PortEntry(nullContentProvider);

		// Make sure its default value is the same as the content provider.
		value = parseIntString(entry.getDefaultValue());
		assertEquals(defaultValue, value);

		// Its value should also be the default value.
		value = parseIntString(entry.getValue());
		assertEquals(defaultValue, value);

		// Check the allowed value type.
		assertEquals(contentProvider.getAllowedValueType(),
				entry.getValueType());
		// ------------------------------------------------------------- //

		return;
	}

	/**
	 * Checks that the {@link PortEntry#setValue(String)} properly assigns valid
	 * values (integers managed by the {@link PortEntryContentProvider}) and
	 * rejects invalid values (non-integers or integers outside the range).
	 */
	@Test
	public void checkValue() {

		PortEntry entry;
		PortEntryContentProvider contentProvider;
		final int defaultValue = 1337;
		final int min = 343;
		final int max = 43770;
		int lastValue = 8000;
		int value = -1;
		final String nullString = null;
		boolean changed;

		// Set up the content provider for the Entry.
		contentProvider = new PortEntryContentProvider();
		contentProvider.setRange(min, max);
		contentProvider.setDefaultValue(defaultValue);

		// Create the Entry that will be tested.
		entry = new PortEntry(contentProvider);

		// Check the default value settings.
		value = parseIntString(entry.getDefaultValue());
		assertEquals(defaultValue, value);
		value = parseIntString(entry.getValue());
		assertEquals(defaultValue, value);
		// There should be no error message by default.
		assertNull(entry.getErrorMessage());

		// Try setting the value to a new, valid value.
		lastValue = 8000;
		changed = entry.setValue(Integer.toString(lastValue));
		// Its current value should have changed.
		assertTrue(changed);
		value = parseIntString(entry.getValue());
		assertEquals(lastValue, value);
		// Since the set was successful, there should be no error message.
		assertNull(entry.getErrorMessage());

		// Try setting the value to the min valid value.
		lastValue = min;
		changed = entry.setValue(Integer.toString(lastValue));
		// Its current value should have changed.
		assertTrue(changed);
		value = parseIntString(entry.getValue());
		assertEquals(lastValue, value);
		// Since the set was successful, there should be no error message.
		assertNull(entry.getErrorMessage());

		// Try setting the value to a value that is too small.
		changed = entry.setValue(Integer.toString(min - 1));
		// It should still be the last value.
		assertFalse(changed);
		value = parseIntString(entry.getValue());
		assertEquals(lastValue, value);
		// Since the set was unsuccessful, there should be an error message.
		assertEquals("'342' is an unacceptable value. The value "
				+ "must be between 343 and 43770.", entry.getErrorMessage());

		// Try setting the value to the max valid value.
		lastValue = max;
		changed = entry.setValue(Integer.toString(lastValue));
		// Its current value should have changed.
		assertTrue(changed);
		value = parseIntString(entry.getValue());
		assertEquals(lastValue, value);
		// Since the set was successful, there should be no error message.
		assertNull(entry.getErrorMessage());

		// Try setting the value to a value that is too big.
		changed = entry.setValue(Integer.toString(max + 1));
		// It should still be the last value.
		assertFalse(changed);
		value = parseIntString(entry.getValue());
		assertEquals(lastValue, value);
		// Since the set was unsuccessful, there should be an error message.
		assertEquals("'43771' is an unacceptable value. The value "
				+ "must be between 343 and 43770.", entry.getErrorMessage());

		// ---- Try setting the value to other invalid values. ---- //
		// Try a null value string.
		changed = entry.setValue(nullString);
		// The value should not have changed.
		assertFalse(changed);
		value = parseIntString(entry.getValue());
		assertEquals(lastValue, value);
		// Since the set was unsuccessful, there should be an error message.
		assertEquals("'null' is an unacceptable value. The value "
				+ "must be between 343 and 43770.", entry.getErrorMessage());

		// Try a non-integer string.
		changed = entry.setValue("1337.1337");
		// The value should not have changed.
		assertFalse(changed);
		value = parseIntString(entry.getValue());
		assertEquals(lastValue, value);
		// Since the set was unsuccessful, there should be an error message.
		assertEquals("'1337.1337' is an unacceptable value. The value "
				+ "must be between 343 and 43770.", entry.getErrorMessage());
		// -------------------------------------------------------- //

		return;
	}

	/**
	 * Checks that the content provider can be set properly and cannot be set to
	 * an invalid content provider (anything other than a non-null
	 * {@link PortEntryContentProvider}).
	 */
	@Test
	public void checkContentProvider() {
		// To test this, we'll focus on switching back and forth between two
		// content providers with different configurations.

		// Afterwards, we'll also test setting it to the same content provider
		// and invalid content providers (null or just a
		// BasicEntryContentProvider).

		PortEntry entry;

		// Set up two different providers.
		PortEntryContentProvider provider1 = new PortEntryContentProvider();
		provider1.setRange(50000, 50200);
		PortEntryContentProvider provider2 = new PortEntryContentProvider();
		provider2.setRange(50100, 50300);

		String min1 = provider1.getAllowedValues().get(0);
		String max2 = provider2.getAllowedValues().get(1);

		// ---- Set the initial content provider. ---- //
		entry = new PortEntry(provider1);

		// Make sure it works.
		assertTrue(entry.setValue("50050"));
		assertFalse(entry.setValue("-1"));
		// ------------------------------------------- //

		// ---- Change to the other content provider. ---- //
		entry.setContentProvider(provider2);

		// Check the entry's value. It should be the new provider's default.
		assertEquals(provider2.getDefaultValue(), entry.getValue());

		// Check that the entry works with the new content provider and not the
		// old one.
		assertFalse(entry.setValue(min1));
		assertTrue(entry.setValue("50250"));
		// ----------------------------------------------- //

		// ---- Revert back to the first content provider. ---- //
		entry.setContentProvider(provider1);

		// Check the entry's value. It should be the new provider's default.
		assertEquals(provider1.getDefaultValue(), entry.getValue());

		// Check that the entry works with the new content provider and not the
		// old one.
		assertFalse(entry.setValue(max2));
		assertTrue(entry.setValue("50050"));
		// ---------------------------------------------------- //

		// ---- Try invalid content providers. ---- //
		// Try a BasicEntryContentProvider.
		entry.setContentProvider(new BasicVizEntryContentProvider());

		// The value should not have changed, and the old content provider
		// should still be used.
		assertEquals("50050", entry.getValue());
		assertTrue(entry.setValue("50051"));

		// Try a null *IEntryContentProvider*.
		final IVizEntryContentProvider nullContentProvider = null;
		entry.setContentProvider(nullContentProvider);

		// The value should not have changed, and the old content provider
		// should still be used.
		assertEquals("50051", entry.getValue());
		assertTrue(entry.setValue("50100"));

		// Try a null *KeyEntryContentProvider*.
		final KeyEntryContentProvider nullKeyContentProvider = null;
		entry.setContentProvider(nullKeyContentProvider);

		// The value should not have changed, and the old content provider
		// should still be used.
		assertEquals("50100", entry.getValue());
		assertTrue(entry.setValue("50101"));
		// ---------------------------------------- //

		return;
	}

	/**
	 * Checks the equals and hash code methods for an object, an equivalent
	 * object, an unequal object, and invalid arguments.
	 */
	@Test
	public void checkEquals() {

		PortEntry object;
		Object equalObject;
		Object unequalObject;
		VizEntry superObject;

		PortEntryContentProvider provider;

		// Set up the object under test.
		provider = new PortEntryContentProvider();
		provider.setRange(500, 600);
		object = new PortEntry(provider);
		object.setName("Mandalore");

		// Set up the equivalent object.
		provider = new PortEntryContentProvider();
		provider.setRange(500, 600);
		equalObject = new PortEntry(provider);
		((PortEntry) equalObject).setName("Mandalore");

		// Set up the different object.
		provider = new PortEntryContentProvider();
		provider.setRange(500, 601); // Different!
		unequalObject = new PortEntry(provider);
		((PortEntry) unequalObject).setName("Mandalore");

		// Set up the super object.
		superObject = new VizEntry();
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
		assertFalse(object.equals("Mandalore"));

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
	 * Tests the clone operation to ensure that it properly returns a copied
	 * {@code PortEntry} rather than a plain {@code Entry}.
	 */
	@Test
	public void checkCopy() {

		PortEntry object;
		PortEntry copy;
		Object clone;

		final PortEntry nullObject = null;

		PortEntryContentProvider contentProvider = new PortEntryContentProvider();
		contentProvider.setRange(50000, 60000);
		contentProvider.setDefaultValue(55000);

		// Create the initial object that will be copied and cloned later.
		object = new PortEntry(contentProvider);
		object.setName("Newport");
		object.setValue(Integer.toString(55001));

		// Copying it should yield an equivalent object.
		copy = new PortEntry(object);
		// After copying, check that they're unique references but equal.
		copy.copy(object);
		assertNotSame(object, copy);
		assertEquals(object, copy);
		assertEquals(copy, object);

		// Cloning it should yield an equivalent object of the correct type.
		clone = object.clone();
		// Check that it's a non-null object of the correct type.
		assertNotNull(clone);
		assertTrue(clone instanceof PortEntry);
		// Check that they're unique references but equal.
		assertNotSame(object, clone);
		assertEquals(object, clone);
		assertEquals(clone, object);

		// Check invalid arguments to the copy constructor.
		try {
			copy = new PortEntry(nullObject);
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
