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

import java.util.List;

import org.eclipse.eavp.viz.service.connections.preferences.IKeyManager;
import org.eclipse.eavp.viz.service.connections.preferences.KeyEntry;
import org.eclipse.eavp.viz.service.connections.preferences.KeyEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.BasicVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.IVizEntryContentProvider;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.junit.Test;

/**
 * This class verifies the expected behavior of the {@link KeyEntry} and its
 * interaction with an associated {@link IKeyManager}.
 * 
 * @author Jordan Deyton
 *
 */
public class KeyEntryTester {

	/**
	 * This checks the constructor for {@code KeyEntry} to ensure that the
	 * default values are properly set.
	 */
	@Test
	public void checkConstruction() {

		KeyEntry entry;
		KeyEntry entry2;

		KeyEntryContentProvider contentProvider;
		SimpleCountKeyManager intKeys;
		SimpleDiscreteKeyManager discreteKeys;

		final KeyEntryContentProvider nullProvider = null;

		// ---- Test construction based on an UNDEFINED set of keys. ---- //
		// Set up the content provider.
		intKeys = new SimpleCountKeyManager();
		contentProvider = new KeyEntryContentProvider(intKeys);

		// Construct the entry.
		entry = new KeyEntry(contentProvider);
		assertNotNull(entry.getValue());
		assertEquals(intKeys.getNextKey(), entry.getValue());
		assertEquals(intKeys.getNextKey(), entry.getDefaultValue());
		assertEquals(intKeys.getAvailableKeys(), entry.getAllowedValues());
		assertEquals(contentProvider.getAllowedValueType(),
				entry.getValueType());

		// Construct another entry.
		entry2 = new KeyEntry(contentProvider);
		assertNotNull(entry2.getValue());
		assertEquals(intKeys.getNextKey(), entry2.getValue());
		assertEquals(intKeys.getNextKey(), entry2.getDefaultValue());
		assertEquals(intKeys.getAvailableKeys(), entry2.getAllowedValues());
		assertEquals(contentProvider.getAllowedValueType(),
				entry2.getValueType());

		// They should actually have the same (default) value.
		assertEquals(entry.getValue(), entry2.getValue());
		// Setting the value of one should change this.
		entry.setValue(intKeys.getNextKey());
		intKeys.takeKey();
		assertFalse(entry.getValue().equals(entry2.getValue()));
		// -------------------------------------------------------------- //

		// ---- Test construction based on a DISCRETE set of keys. ---- //
		// Set up the content provider.
		discreteKeys = new SimpleDiscreteKeyManager("Everest", "K2", "Fuji",
				"McKinley");
		contentProvider = new KeyEntryContentProvider(discreteKeys);

		// Construct the entry.
		entry = new KeyEntry(contentProvider);
		assertNotNull(entry.getValue());
		assertEquals(discreteKeys.getNextKey(), entry.getValue());
		assertEquals(discreteKeys.getNextKey(), entry.getDefaultValue());
		assertEquals(discreteKeys.getAvailableKeys(), entry.getAllowedValues());
		assertEquals(contentProvider.getAllowedValueType(),
				entry.getValueType());

		// Construct another entry.
		entry2 = new KeyEntry(contentProvider);
		assertNotNull(entry2.getValue());
		assertEquals(discreteKeys.getNextKey(), entry2.getValue());
		assertEquals(discreteKeys.getNextKey(), entry2.getDefaultValue());
		assertEquals(discreteKeys.getAvailableKeys(), entry2.getAllowedValues());
		assertEquals(contentProvider.getAllowedValueType(),
				entry2.getValueType());

		// They should actually have the same (default) value.
		assertEquals(entry.getValue(), entry2.getValue());
		assertEquals(entry.getDefaultValue(), entry2.getDefaultValue());
		// Setting the value of one should change this.
		entry.setValue(discreteKeys.getNextKey());
		discreteKeys.takeKeys(discreteKeys.getNextKey());
		assertFalse(entry.getValue().equals(entry2.getValue()));
		// ------------------------------------------------------------ //

		// Try constructing with a null content provider.
		try {
			entry = new KeyEntry(nullProvider);
			fail("KeyEntryTester error: "
					+ "An NPE was not thrown with a null content provider in the constructor.");
		} catch (NullPointerException e) {
			// An exception was thrown as expected.
		}

		return;
	}

	/**
	 * Checks that the {@link KeyEntry#setValue(String)} properly assigns valid
	 * values (valid, unused keys in the associated key manager) and rejects
	 * invalid values (invalid or used keys in the key manager).
	 */
	@Test
	public void checkValue() {

		KeyEntry entry;

		KeyEntryContentProvider contentProvider;
		SimpleCountKeyManager intKeys;
		SimpleDiscreteKeyManager discreteKeys;

		String key;
		final String nullString = null;

		// ---- Test based on an UNDEFINED set of keys. ---- //
		// Set up the content provider.
		intKeys = new SimpleCountKeyManager();
		contentProvider = new KeyEntryContentProvider(intKeys);

		// Construct the entry.
		entry = new KeyEntry(contentProvider);

		// We should be able to set the entry's value to a valid key.
		// Use the next available key.
		key = intKeys.getNextKey();
		assertTrue(entry.setValue(key));
		assertEquals(key, entry.getValue());
		// Check the error message.
		assertNull(entry.getErrorMessage());

		// The key that was set for the entry is now taken.
		intKeys.takeKey();

		// We should not be able to set the entry's value to a new key if it is
		// removed from the key manager.
		assertFalse(entry.setValue(intKeys.takeKey()));
		assertEquals(key, entry.getValue());
		// Check the error message.
		assertNotNull(entry.getErrorMessage());
		assertEquals(
				"'1' is an unacceptable value. It must be a unique string.",
				entry.getErrorMessage());

		// Passing the current value shouldn't change anything, even if the key
		// is taken.
		assertTrue(entry.setValue(key));
		assertEquals(key, entry.getValue());
		// Check the error message.
		assertNull(entry.getErrorMessage());

		// We should not be able to set the entry's value to an invalid key.
		// Use an invalid key, or null.
		assertFalse(entry.setValue(nullString));
		assertEquals(key, entry.getValue());
		// Check the error message.
		assertNotNull(entry.getErrorMessage());
		assertEquals(
				"'null' is an unacceptable value. It must be a unique string.",
				entry.getErrorMessage());

		// We should still be able to set the entry's value to a valid key.
		// Use the next available key.
		key = intKeys.getNextKey();
		assertTrue(entry.setValue(key));
		assertEquals(key, entry.getValue());
		// Check the error message.
		assertNull(entry.getErrorMessage());

		// ------------------------------------------------ //

		// ---- Test based on a DISCRETE set of keys. ---- //
		// Set up the content provider.
		discreteKeys = new SimpleDiscreteKeyManager("Everest", "K2", "Fuji",
				"McKinley");
		contentProvider = new KeyEntryContentProvider(discreteKeys);

		// Construct the entry.
		entry = new KeyEntry(contentProvider);

		// We should be able to set the entry's value to a valid key.
		key = "K2";
		assertTrue(entry.setValue(key));
		assertEquals(key, entry.getValue());
		// Check the error message.
		assertNull(entry.getErrorMessage());

		// The key that was set for the entry is now taken.
		discreteKeys.takeKeys(key);

		// We should not be able to set the entry's value to a key if it is
		// removed from the key manager.
		discreteKeys.takeKeys("Everest");
		assertFalse(entry.setValue("Everest"));
		assertEquals(key, entry.getValue());
		// Check the error message.
		assertNotNull(entry.getErrorMessage());
		// Build the string of available keys. We're not guaranteed any order
		// from the key manager.
		List<String> availableKeys = discreteKeys.getAvailableKeys();
		String allowedKeys = availableKeys.get(0);
		for (int i = 1; i < availableKeys.size() - 2; i++) {
			allowedKeys += ", " + availableKeys.get(i);
		}
		allowedKeys += " or " + availableKeys.get(availableKeys.size() - 1);
		// Now we can compare the error message.
		assertEquals(
				"'Everest' is an unacceptable value. The value must be one of "
						+ allowedKeys + ".", entry.getErrorMessage());

		// Passing the current value shouldn't change anything, even if the key
		// is taken.
		assertTrue(entry.setValue(key));
		assertEquals(key, entry.getValue());
		// Check the error message.
		assertNull(entry.getErrorMessage());

		// We should not be able to set the entry's value to an invalid key.
		// Use an invalid key, or null.
		assertFalse(entry.setValue(nullString));
		assertEquals(key, entry.getValue());
		// Check the error message.
		assertNotNull(entry.getErrorMessage());
		assertEquals(
				"'null' is an unacceptable value. The value must be one of "
						+ allowedKeys + ".", entry.getErrorMessage());

		// We should still be able to set the entry's value to a valid key.
		// Use the next available key.
		key = "Fuji";
		assertTrue(entry.setValue(key));
		assertEquals(key, entry.getValue());
		// Check the error message.
		assertNull(entry.getErrorMessage());
		// ---------------------------------------------- //

		return;
	}

	/**
	 * Checks that the content provider can be set properly and cannot be set to
	 * an invalid content provider (anything other than a non-null
	 * {@link KeyEntryContentProvider}).
	 */
	@Test
	public void checkContentProvider() {

		// To test this, we'll focus on switching back and forth between two
		// content providers with different key managers.

		// Afterwards, we'll also test setting it to the same content provider
		// and invalid content providers (null or just a
		// BasicEntryContentProvider).

		KeyEntry entry;

		// Set up the content providers and key managers.
		SimpleCountKeyManager intKeys = new SimpleCountKeyManager();
		SimpleDiscreteKeyManager discreteKeys = new SimpleDiscreteKeyManager(
				"Everest", "K2", "Fuji", "McKinley");
		KeyEntryContentProvider undefinedContentProvider = new KeyEntryContentProvider(
				intKeys);
		KeyEntryContentProvider discreteContentProvider = new KeyEntryContentProvider(
				discreteKeys);

		// ---- Set the initial content provider. ---- //
		entry = new KeyEntry(undefinedContentProvider);

		// Make sure it works.
		assertTrue(entry.setValue(intKeys.getNextKey()));
		intKeys.takeKey();
		// ------------------------------------------- //

		// ---- Change to the other content provider. ---- //
		entry.setContentProvider(discreteContentProvider);

		// Check the entry's value. It should have been changed to the default
		// from the new content provider.
		assertEquals(discreteContentProvider.getDefaultValue(),
				entry.getValue());

		// Check that the entry works with the new content provider and not the
		// old one.
		assertFalse(entry.setValue(intKeys.getNextKey()));
		assertTrue(entry.setValue("McKinley"));
		discreteKeys.takeKeys("McKinley");
		// ----------------------------------------------- //

		// Take another key since the key manager could be updated elsewhere.
		intKeys.takeKey();

		// ---- Revert back to the first content provider. ---- //
		entry.setContentProvider(undefinedContentProvider);

		// Check the entry's value. It should have been changed to the default
		// from the new content provider.
		assertEquals(undefinedContentProvider.getDefaultValue(),
				entry.getValue());

		// Check that the entry works with the new content provider and not the
		// old one.
		assertFalse(entry.setValue("Everest"));
		assertTrue(entry.setValue(intKeys.getNextKey()));
		// ---------------------------------------------------- //

		// The key is taken. It's the current value of the entry.
		String key = intKeys.takeKey();
		assertEquals(key, entry.getValue());

		// ---- Try invalid content providers. ---- //
		// Try a BasicEntryContentProvider.
		entry.setContentProvider(new BasicVizEntryContentProvider());

		// The value should not have changed, and the old content provider
		// should still be used.
		assertEquals(key, entry.getValue());
		// Set it to a new key. This should be based on the int keys.
		key = intKeys.getNextKey();
		assertTrue(entry.setValue(key));
		intKeys.takeKey();

		// Try a null *IEntryContentProvider*.
		final IVizEntryContentProvider nullContentProvider = null;
		entry.setContentProvider(nullContentProvider);

		// The value should not have changed, and the old content provider
		// should still be used.
		assertEquals(key, entry.getValue());
		// Set it to a new key. This should be based on the int keys.
		key = intKeys.getNextKey();
		assertTrue(entry.setValue(key));
		intKeys.takeKey();

		// Try a null *KeyEntryContentProvider*.
		final KeyEntryContentProvider nullKeyContentProvider = null;
		entry.setContentProvider(nullKeyContentProvider);

		// The value should not have changed, and the old content provider
		// should still be used.
		assertEquals(key, entry.getValue());
		// Set it to a new key. This should be based on the int keys.
		key = intKeys.getNextKey();
		assertTrue(entry.setValue(key));
		intKeys.takeKey();
		// ---------------------------------------- //

		return;
	}

	/**
	 * Checks the equals and hash code methods for an object, an equivalent
	 * object, an unequal object, and invalid arguments.
	 */
	@Test
	public void checkEquals() {

		KeyEntry object;
		Object equalObject;
		Object unequalObject;
		VizEntry superObject;

		KeyEntryContentProvider contentProvider;
		SimpleCountKeyManager intKeys = new SimpleCountKeyManager();
		SimpleDiscreteKeyManager discreteKeys = new SimpleDiscreteKeyManager(
				"Doom");

		// Set up the object under test.
		contentProvider = new KeyEntryContentProvider(intKeys);
		object = new KeyEntry(contentProvider);
		object.setName("Doom");

		// Set up the equivalent object.
		contentProvider = new KeyEntryContentProvider(intKeys);
		equalObject = new KeyEntry(contentProvider);
		((KeyEntry) equalObject).setName("Doom");

		// Set up the different object.
		contentProvider = new KeyEntryContentProvider(discreteKeys); // Different!
		unequalObject = new KeyEntry(contentProvider);
		((KeyEntry) unequalObject).setName("Doom");

		// Set up an "equivalent" super class object.
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
		assertFalse(object.equals("Doom"));

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

		KeyEntry object;
		KeyEntry copy;
		Object clone;

		final KeyEntry nullObject = null;

		KeyEntryContentProvider contentProvider = new KeyEntryContentProvider(
				new SimpleCountKeyManager());

		// Create the initial object that will be copied and cloned later.
		object = new KeyEntry(contentProvider);
		object.setName("Olympus");

		// Copying it should yield an equivalent object.
		copy = new KeyEntry(object);
		// After copying, check that they're unique references but equal.
		copy.copy(object);
		assertNotSame(object, copy);
		assertEquals(object, copy);
		assertEquals(copy, object);

		// Cloning it should yield an equivalent object of the correct type.
		clone = object.clone();
		// Check that it's a non-null object of the correct type.
		assertNotNull(clone);
		assertTrue(clone instanceof KeyEntry);
		// Check that they're unique references but equal.
		assertNotSame(object, clone);
		assertEquals(object, clone);
		assertEquals(clone, object);

		// Check invalid arguments to the copy constructor.
		try {
			copy = new KeyEntry(nullObject);
		} catch (NullPointerException e) {
			// Exception thrown as expected. Nothing to do.
		}

		return;
	}
}
