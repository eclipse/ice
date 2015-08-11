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
package org.eclipse.ice.viz.service.connections.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.viz.service.connections.KeyEntryContentProvider;
import org.eclipse.ice.viz.service.connections.preferences.IKeyManager;
import org.eclipse.ice.viz.service.connections.preferences.test.SimpleCountKeyManager;
import org.eclipse.ice.viz.service.connections.preferences.test.SimpleDiscreteKeyManager;
import org.eclipse.ice.viz.service.datastructures.VizAllowedValueType;
import org.junit.Test;

/**
 * This class verifies the behavior of the {@link KeyEntryContentProvider} and
 * its interaction with an associated {@link IKeyManager}.
 * 
 * @author Jordan Deyton
 *
 */
public class KeyEntryContentProviderTester {

	/**
	 * Verifies that the default values for a {@link KeyEntryContentProvider}
	 * are properly set when instantiated.
	 */
	@Test
	public void checkConstruction() {

		KeyEntryContentProvider contentProvider;

		List<String> allowedValues;

		SimpleCountKeyManager intKeys;
		SimpleDiscreteKeyManager discreteKeys;
		final IKeyManager nullKeyManager = null;

		// ---- Try setting it up with an UNDEFINED key manager. ---- //
		// An "undefined" key manager means there is no limit on the number of
		// keys, nor is there a pre-defined list of available keys.

		// Set up the content provider with a simple *undefined* IKeyManager.
		intKeys = new SimpleCountKeyManager();
		contentProvider = new KeyEntryContentProvider(intKeys);

		// Check the allowed value type. It should be undefined.
		assertEquals(VizAllowedValueType.Undefined,
				contentProvider.getAllowedValueType());

		// Check the allowed values. The list should be empty, but not null.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertTrue(allowedValues.isEmpty());

		// Check the default value. It should be the same as the next available
		// key from the key manager.
		assertEquals(intKeys.getNextKey(), contentProvider.getDefaultValue());
		assertEquals("0", contentProvider.getDefaultValue());
		// ---------------------------------------------------------- //

		// ---- Try setting it up with a DISCRETE key manager. ---- //
		// A "discrete" key manager means it has a pre-defined list of allowed
		// keys.

		// Set up the content provider with a simple *discrete* IKeyManager.
		discreteKeys = new SimpleDiscreteKeyManager("one", "two", "three");
		contentProvider = new KeyEntryContentProvider(discreteKeys);

		// Check the allowed value type. It should be discrete.
		assertEquals(VizAllowedValueType.Discrete,
				contentProvider.getAllowedValueType());

		// Check the allowed values. The list should be empty, but not null.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(3, allowedValues.size());
		assertTrue(allowedValues.contains("one"));
		assertTrue(allowedValues.contains("two"));
		assertTrue(allowedValues.contains("three"));

		// Check the default value. It should be the same as the next available
		// key from the key manager.
		assertEquals(discreteKeys.getNextKey(),
				contentProvider.getDefaultValue());
		// There's no guarantee about the order of the next key, so don't try
		// checking it here like we did with the UNDEFINED key manager.
		// -------------------------------------------------------- //

		// ---- Try setting it up with a DISCRETE key manager. ---- //
		// NOTE: This does the same tests as above, BUT the key manager will be
		// emptied first.

		// A "discrete" key manager means it has a pre-defined list of allowed
		// keys.

		// Set up the content provider with a simple *discrete* IKeyManager.
		discreteKeys = new SimpleDiscreteKeyManager("one", "two", "three");
		discreteKeys.takeKeys("one", "two", "three");
		contentProvider = new KeyEntryContentProvider(discreteKeys);

		// Check the allowed value type. It should be discrete.
		assertEquals(VizAllowedValueType.Discrete,
				contentProvider.getAllowedValueType());

		// Check the allowed values. The list should be empty, but not null.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(0, allowedValues.size());

		// Try getting the default value. Since this pulls from the key
		// manager's next available key, and there are no more keys left, it
		// should throw an IllegalStateException.
		try {
			contentProvider.getDefaultValue();
			fail("An IllegalStateException (stating that no keys are left) "
					+ "should have been thrown.");
		} catch (IllegalStateException e) {
			// Passed. Do nothing.
		}
		// -------------------------------------------------------- //

		// Try invalid arguments.
		try {
			contentProvider = new KeyEntryContentProvider(nullKeyManager);
			fail("A NPE was not thrown with null arguments.");
		} catch (NullPointerException e) {
			// All is good. Do nothing.
		}

		return;
	}

	/**
	 * Tests that the default value is appropriately derived from the associated
	 * {@code IKeyManager} and cannot be changed by updating the content
	 * provider.
	 */
	@Test
	public void checkDefaultValue() {

		KeyEntryContentProvider contentProvider;

		final String nullString = null;

		SimpleCountKeyManager intKeys;
		SimpleDiscreteKeyManager discreteKeys;

		// ---- Try it with key manager whose key set is unrestricted. ---- //
		// Set up the content provider.
		intKeys = new SimpleCountKeyManager();
		contentProvider = new KeyEntryContentProvider(intKeys);

		// Check the default value. It should be the same as the next available
		// key from the key manager.
		assertEquals(intKeys.getNextKey(), contentProvider.getDefaultValue());
		assertEquals("0", contentProvider.getDefaultValue());

		// Try setting the default value. Nothing should change.
		contentProvider.setDefaultValue("1");
		assertEquals(intKeys.getNextKey(), contentProvider.getDefaultValue());
		assertEquals("0", contentProvider.getDefaultValue());

		// Take a key, then test it again. It should change to the next integer.
		intKeys.takeKey();
		// The new value should be "1".
		assertEquals(intKeys.getNextKey(), contentProvider.getDefaultValue());
		assertEquals("1", contentProvider.getDefaultValue());

		// Try it again for good measure.
		intKeys.takeKey();
		// The new value should be "2".
		assertEquals(intKeys.getNextKey(), contentProvider.getDefaultValue());
		assertEquals("2", contentProvider.getDefaultValue());
		// ---------------------------------------------------------------- //

		// ---- Try it with key manager whose key set is restricted. ---- //
		// Set up the content provider.
		discreteKeys = new SimpleDiscreteKeyManager("Motoko", "Batou",
				"Togusa", "Ishikawa", "Saito", "Paz", "Borma");
		contentProvider = new KeyEntryContentProvider(discreteKeys);

		// Check the default value. It should be the same as the next available
		// key from the key manager.
		assertEquals(discreteKeys.getNextKey(),
				contentProvider.getDefaultValue());

		// Try setting the default value. Nothing should change.
		// Pick a value besides the default. We have to do it this way because
		// we aren't guaranteed the same order in the KeyManager.
		String testDefault = ("Motoko".equals(discreteKeys.getNextKey()) ? "Batou"
				: "Motoko");
		contentProvider.setDefaultValue(testDefault);
		// The default value should equal the next available key, not our
		// selected default.
		assertFalse(testDefault.equals(contentProvider.getDefaultValue()));
		assertEquals(discreteKeys.getNextKey(),
				contentProvider.getDefaultValue());

		// Take the default key and try it again. The value should still be the
		// same as the *next* available key (not the last available key, which
		// was just taken).
		discreteKeys.takeKeys(discreteKeys.getNextKey());
		assertEquals(discreteKeys.getNextKey(),
				contentProvider.getDefaultValue());

		// Try it again, for good measure. Also take at least one other key.
		discreteKeys.takeKeys(discreteKeys.getNextKey(), "Motoko", "Batou");
		assertEquals(discreteKeys.getNextKey(),
				contentProvider.getDefaultValue());

		// Take all of the keys. An exception should be thrown the next time.
		discreteKeys.takeKeys("Motoko", "Batou", "Togusa", "Ishikawa", "Saito",
				"Paz", "Borma");
		try {
			contentProvider.getDefaultValue();
			fail("An IllegalStateException (stating that no keys are left) "
					+ "should have been thrown.");
		} catch (IllegalStateException e) {
			// Passed. Do nothing.
		}
		// -------------------------------------------------------------- //

		// Release all of the keys, then try setting a null default value.
		// Nothing should change.
		discreteKeys.releaseKeys("Motoko", "Batou", "Togusa", "Ishikawa",
				"Saito", "Paz", "Borma");
		contentProvider.setDefaultValue(nullString);
		// The default value should be the next key as reported by the key
		// manager.
		assertEquals(discreteKeys.getNextKey(),
				contentProvider.getDefaultValue());

		return;
	}

	/**
	 * Tests that the allowed values are appropriately derived from the
	 * associated {@code IKeyManager} and cannot be changed by updating the
	 * content provider.
	 */
	@Test
	public void checkAllowedValues() {

		KeyEntryContentProvider contentProvider;

		ArrayList<String> allowedValues;

		SimpleCountKeyManager intKeys;
		SimpleDiscreteKeyManager discreteKeys;

		// ---- Try it with key manager whose key set is unrestricted. ---- //
		// Set up the content provider.
		intKeys = new SimpleCountKeyManager();
		contentProvider = new KeyEntryContentProvider(intKeys);

		// Check the allowed values. It should return an empty list.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertTrue(allowedValues.isEmpty());

		// Try setting the allowed values. This should do nothing.
		allowedValues = new ArrayList<String>(2);
		allowedValues.add("zero");
		allowedValues.add("one");
		contentProvider.setAllowedValues(allowedValues);
		// Check that nothing changed.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertTrue(allowedValues.isEmpty());

		// Take a key, then test it again. Nothing should change.
		intKeys.takeKey();
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertTrue(allowedValues.isEmpty());

		// Try it again for good measure. Again, nothing should change.
		intKeys.takeKey();
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertTrue(allowedValues.isEmpty());
		// ---------------------------------------------------------------- //

		// ---- Try it with key manager whose key set is restricted. ---- //
		// Set up the content provider.
		discreteKeys = new SimpleDiscreteKeyManager("Motoko", "Batou",
				"Togusa", "Ishikawa", "Saito", "Paz", "Borma");
		contentProvider = new KeyEntryContentProvider(discreteKeys);

		// Check the allowed values. Every key specified above should be valid.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(7, allowedValues.size());
		// Check the contents.
		assertTrue(allowedValues.contains("Motoko"));
		assertTrue(allowedValues.contains("Batou"));
		assertTrue(allowedValues.contains("Togusa"));
		assertTrue(allowedValues.contains("Ishikawa"));
		assertTrue(allowedValues.contains("Saito"));
		assertTrue(allowedValues.contains("Paz"));
		assertTrue(allowedValues.contains("Borma"));

		// Try setting the allowed values. This should do nothing.
		allowedValues = new ArrayList<String>(2);
		allowedValues.add("zero");
		allowedValues.add("one");
		contentProvider.setAllowedValues(allowedValues);
		// Check that nothing changed.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(7, allowedValues.size());
		// Check the contents.
		assertTrue(allowedValues.contains("Motoko"));
		assertTrue(allowedValues.contains("Batou"));
		assertTrue(allowedValues.contains("Togusa"));
		assertTrue(allowedValues.contains("Ishikawa"));
		assertTrue(allowedValues.contains("Saito"));
		assertTrue(allowedValues.contains("Paz"));
		assertTrue(allowedValues.contains("Borma"));

		// Take two keys. These same keys should also be missing from the
		// allowed values.
		discreteKeys.takeKeys("Batou", "Togusa");
		// Check the allowed values. Every key specified above should be valid.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(5, allowedValues.size());
		// Check the contents.
		assertTrue(allowedValues.contains("Motoko"));
		assertFalse(allowedValues.contains("Batou"));
		assertFalse(allowedValues.contains("Togusa"));
		assertTrue(allowedValues.contains("Ishikawa"));
		assertTrue(allowedValues.contains("Saito"));
		assertTrue(allowedValues.contains("Paz"));
		assertTrue(allowedValues.contains("Borma"));

		// Take a couple more keys out of the allowed values.
		discreteKeys.takeKeys("Ishikawa", "Motoko");
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(3, allowedValues.size());
		// Check the contents.
		assertFalse(allowedValues.contains("Motoko"));
		assertFalse(allowedValues.contains("Batou"));
		assertFalse(allowedValues.contains("Togusa"));
		assertFalse(allowedValues.contains("Ishikawa"));
		assertTrue(allowedValues.contains("Saito"));
		assertTrue(allowedValues.contains("Paz"));
		assertTrue(allowedValues.contains("Borma"));

		// Take all of the keys. An exception should be thrown the next time.
		discreteKeys.takeKeys("Motoko", "Batou", "Togusa", "Ishikawa", "Saito",
				"Paz", "Borma");
		allowedValues = contentProvider.getAllowedValues();
		// The list should now be empty.
		assertNotNull(allowedValues);
		assertTrue(allowedValues.isEmpty());

		// -------------------------------------------------------------- //

		// Release all of the keys, then try setting a null list of allowed
		// values.
		discreteKeys.releaseKeys("Motoko", "Batou", "Togusa", "Ishikawa",
				"Saito", "Paz", "Borma");
		allowedValues = null;
		contentProvider.setAllowedValues(allowedValues);
		// All keys should be available.
		allowedValues = contentProvider.getAllowedValues();
		assertNotNull(allowedValues);
		assertEquals(7, allowedValues.size());
		// Check the contents.
		assertTrue(allowedValues.contains("Motoko"));
		assertTrue(allowedValues.contains("Batou"));
		assertTrue(allowedValues.contains("Togusa"));
		assertTrue(allowedValues.contains("Ishikawa"));
		assertTrue(allowedValues.contains("Saito"));
		assertTrue(allowedValues.contains("Paz"));
		assertTrue(allowedValues.contains("Borma"));

		return;
	}

	/**
	 * This checks that the content provider's type depends solely on the
	 * backing key manager and cannot be changed.
	 */
	@Test
	public void checkVizAllowedValueType() {

		KeyEntryContentProvider contentProvider;

		final VizAllowedValueType nullType = null;

		SimpleCountKeyManager intKeys;
		SimpleDiscreteKeyManager discreteKeys;

		// ---- Try it with key manager whose key set is unrestricted. ---- //
		// Set up the content provider.
		intKeys = new SimpleCountKeyManager();
		contentProvider = new KeyEntryContentProvider(intKeys);

		// Check the allowed value type. It should be undefined.
		assertEquals(VizAllowedValueType.Undefined,
				contentProvider.getAllowedValueType());

		// No matter what type we try to set it to, it cannot be changed.
		for (VizAllowedValueType type : VizAllowedValueType.values()) {
			contentProvider.setAllowedValueType(type);
			assertEquals(VizAllowedValueType.Undefined,
					contentProvider.getAllowedValueType());
		}
		// ---------------------------------------------------------------- //

		// ---- Try it with key manager whose key set is restricted. ---- //
		// Set up the content provider.
		discreteKeys = new SimpleDiscreteKeyManager("Motoko", "Batou",
				"Togusa", "Ishikawa", "Saito", "Paz", "Borma");
		contentProvider = new KeyEntryContentProvider(discreteKeys);

		// Check the allowed value type. It should be discrete.
		assertEquals(VizAllowedValueType.Discrete,
				contentProvider.getAllowedValueType());

		// No matter what type we try to set it to, it cannot be changed.
		for (VizAllowedValueType type : VizAllowedValueType.values()) {
			contentProvider.setAllowedValueType(type);
			assertEquals(VizAllowedValueType.Discrete,
					contentProvider.getAllowedValueType());
		}
		// -------------------------------------------------------------- //

		// Try using a null value type.
		contentProvider.setAllowedValueType(nullType);
		// The value type should not change.
		assertEquals(VizAllowedValueType.Discrete,
				contentProvider.getAllowedValueType());

		return;
	}

}
