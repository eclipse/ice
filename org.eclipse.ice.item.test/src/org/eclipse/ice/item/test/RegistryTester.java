/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.item.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.eclipse.ice.item.Registry;
import org.eclipse.january.form.DataComponent;
import org.junit.Test;

/**
 * <p>
 * The RegistryTester is responsible for testing the Registry class.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class RegistryTester {

	/**
	 */
	private Registry registry;

	/**
	 * 
	 */
	private FakeDataComponent fakeDataComponent;

	/**
	 * <p>
	 * This operation tests the Registry class by checking that values can be
	 * set and retrieved and check for existence.
	 * </p>
	 */
	@Test
	public void checkValues() {

		// Some keys and values
		String key1 = "Stone Temple Pilots", value1 = "Dead & Bloated";
		String key2 = "Motley Crue", value2 = "Dr. Feelgood";
		String value3 = "Knife", value4 = "Bow and Arrow";

		// Create a registry to test
		registry = new Registry();

		// Set the values
		registry.setValue(key1, value1);
		registry.setValue(key2, value2);
		// Check the values
		assertEquals(value1, registry.getValue(key1));
		assertEquals(value2, registry.getValue(key2));

		// Update the values and make sure they changed
		registry.updateValue(key1, value3);
		registry.updateValue(key2, value4);
		assertEquals(value3, registry.getValue(key1));
		assertEquals(value4, registry.getValue(key2));

		// Make sure that updating a value doesn't work for something that is
		// not in the registry
		registry.updateValue("not in there key", "not in there value");
		assertNull(registry.getValue("not in there key"));

		return;

	}

	/**
	 * <p>
	 * This operation tests the Registry class by insuring that Entries can be
	 * registered against keys.
	 * </p>
	 */
	@Test
	public void checkRegistration() {
		// A key and a value
		String key = "Seether";

		// Create a Registry to test
		registry = new Registry();

		// Create the DataComponents
		DataComponent dc1 = new DataComponent();
		dc1.setId(1);
		dc1.setName("Updated DataComponent");
		DataComponent dc2 = new DataComponent();
		dc1.setId(2);
		dc1.setName("Updated DataComponent");

		// Register the Entries against the key
		assertTrue(registry.register(dc1, key));
		assertTrue(registry.register(dc2, key));

		// Make sure the Registry contains the key
		assertTrue(registry.containsKey(key));

	}

	/**
	 * <p>
	 * This operation checks the Registry class by checking that it will
	 * properly update Entries.
	 * </p>
	 */
	@Test
	public void testDispatching() {
		// A key and a value
		String key = "Disturbed";
		String value = "Prayer";

		// Create a Registry to test
		registry = new Registry();

		// Create the DataComponents
		FakeDataComponent dc1 = new FakeDataComponent();
		dc1.setId(1);
		dc1.setName("Updated DataComponent");
		FakeDataComponent dc2 = new FakeDataComponent();
		dc2.setId(2);
		dc2.setName("Updated DataComponent");

		// Register the Entries against the key and insure they are registered
		assertTrue(registry.register(dc1, key));
		assertTrue(registry.register(dc2, key));

		// Set the value and insure it is set
		assertTrue(registry.setValue(key, value));

		// Dispatch updates since the value is set
		registry.dispatch();

		// Check the values of the entries and make sure they were set
		assertEquals(value, dc1.getUpdatedValue());
		assertEquals(value, dc2.getUpdatedValue());
	}
}