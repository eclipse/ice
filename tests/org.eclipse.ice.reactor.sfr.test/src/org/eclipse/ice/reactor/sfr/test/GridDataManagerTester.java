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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.ice.reactor.sfr.base.GridDataManager;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.base.SFRData;
import org.junit.Test;

public class GridDataManagerTester {

	@Test
	public void checkConstruction() {
		// begin-user-code

		// Some size values.
		int defaultSize = Integer.MAX_VALUE;
		int size = 42;
		int location;

		// A manager to test and a component for adding.
		GridDataManager manager;
		String name = "Sir Digby Chicken Caesar";

		SFRComponent defaultProvider = new SFRComponent();

		/* ---- Test the manager with a valid size. ---- */
		// Initialize the manager.
		manager = new GridDataManager(size);

		// Test the lower bound on the list of positions.
		for (location = -10; location < 0; location++) {
			assertFalse(manager.addComponent(name, location));
			assertNull(manager.getComponentName(location));
			assertTrue(manager.getComponentLocations(name).isEmpty());
			assertNull(manager.getDataProvider(location));
		}

		// Test the possible positions.
		for (location = 0; location < size; location++) {
			assertNull(manager.getDataProvider(location));
			assertTrue(manager.addComponent(name, location));
			assertEquals(name, manager.getComponentName(location));
			assertEquals(location + 1, manager.getComponentLocations(name)
					.size());
			assertEquals(defaultProvider, manager.getDataProvider(location));
		}

		// Test the upper bound on the list of positions.
		for (location = size; location < size + 10; location++) {
			assertFalse(manager.addComponent(name, location));
			assertNull(manager.getComponentName(location));
			assertEquals(size, manager.getComponentLocations(name).size());
			assertNull(manager.getDataProvider(location));
		}
		/* --------------------------------------------- */

		/* ---- Test the manager with an invalid size. ---- */
		// Initialize the manager.
		manager = new GridDataManager(0);

		// Test the lower bound on the list of positions.
		for (location = -10; location < 0; location++) {
			assertFalse(manager.addComponent(name, location));
			assertNull(manager.getComponentName(location));
			assertTrue(manager.getComponentLocations(name).isEmpty());
			assertNull(manager.getDataProvider(location));
		}

		// Test the possible positions.
		for (location = 0; location < size + 10; location++) {
			assertNull(manager.getDataProvider(location));
			assertTrue(manager.addComponent(name, location));
			assertEquals(name, manager.getComponentName(location));
			assertEquals(location + 1, manager.getComponentLocations(name)
					.size());
			assertEquals(defaultProvider, manager.getDataProvider(location));
		}

		// We should not be able to add anything to the largest location.
		location = defaultSize;
		size = manager.getComponentLocations(name).size();

		assertFalse(manager.addComponent(name, location));
		assertNull(manager.getComponentName(location));
		assertEquals(size, manager.getComponentLocations(name).size());
		assertNull(manager.getDataProvider(location));

		// We should be able to add to the next largest location, though.
		location--;
		size++;

		assertTrue(manager.addComponent(name, location));
		assertEquals(name, manager.getComponentName(location));
		assertEquals(size, manager.getComponentLocations(name).size());
		assertEquals(defaultProvider, manager.getDataProvider(location));
		/* --------------------------------------------- */

		return;
		// end-user-code
	}

	@Test
	public void checkComponentAddRem() {
		// begin-user-code

		// Some size values.
		int size = 42;
		int location;

		// A GridDataManager to test.
		GridDataManager manager = new GridDataManager(size);

		// Some components to test adding/removing.
		String component1 = "Arthur";
		String component2 = "Ford Prefect";
		String component3 = "Marvin";

		SFRData data1 = new SFRData("one");
		SFRData data2 = new SFRData("two");
		SFRData data3 = new SFRData("three");
		SFRComponent provider = new SFRComponent();
		provider.addData(data1, 0.0);
		provider.addData(data1, 1.0);
		provider.addData(data2, 0.0);
		provider.addData(data3, 0.0);
		SFRComponent defaultProvider = new SFRComponent();

		SFRComponent managedProvider;

		List<Integer> locations;

		/* ---- Test adding a Component. ---- */
		// Test the lower bound on the list of positions.
		for (location = -10; location < 0; location++) {
			assertFalse(manager.addComponent(component1, location));
			assertNull(manager.getComponentName(location));
			assertTrue(manager.getComponentLocations(component1).isEmpty());
			assertNull(manager.getDataProvider(location));
		}

		// Test the possible positions.
		for (location = 0; location < size; location++) {
			assertNull(manager.getDataProvider(location));
			assertTrue(manager.addComponent(component1, location));
			assertEquals(component1, manager.getComponentName(location));
			assertEquals(location + 1, manager
					.getComponentLocations(component1).size());

			managedProvider = manager.getDataProvider(location);
			assertEquals(defaultProvider, managedProvider);
			managedProvider.addData(data1, 0.0);
			managedProvider.addData(data1, 1.0);
			managedProvider.addData(data2, 0.0);
			managedProvider.addData(data3, 0.0);
			assertEquals(provider, managedProvider);
		}

		// Test the upper bound on the list of positions.
		for (location = size; location < size + 10; location++) {
			assertFalse(manager.addComponent(component1, location));
			assertNull(manager.getComponentName(location));
			assertEquals(size, manager.getComponentLocations(component1).size());
			assertNull(manager.getDataProvider(location));
		}

		// Check the contents of the List of locations.
		locations = manager.getComponentLocations(component1);
		for (location = 0; location < size; location++) {
			locations.contains(location);
		}
		/* ---------------------------------- */

		/* ---- Test adding the same Component to the same location. ---- */
		location = 15;

		assertFalse(manager.addComponent(component1, location));

		// Nothing should change!
		assertEquals(component1, manager.getComponentName(location));
		assertEquals(size, manager.getComponentLocations(component1).size());
		assertTrue(manager.getComponentLocations(component1).contains(location));

		managedProvider = manager.getDataProvider(location);
		assertEquals(provider, managedProvider);
		/* -------------------------------------------------------------- */

		/* ---- Test removing a Component. ---- */
		// Test the lower bound on the list of positions.
		for (location = -10; location < 0; location++) {
			assertFalse(manager.removeComponent(location));
			assertEquals(size, manager.getComponentLocations(component1).size());
		}

		// Test the upper bound on the list of positions.
		for (location = size; location < size + 10; location++) {
			assertFalse(manager.removeComponent(location));
			assertEquals(size, manager.getComponentLocations(component1).size());
		}

		// Try removing some from the inside.
		int newSize = size;
		for (location = 10; location < 20; location++) {
			assertTrue(manager.removeComponent(location));

			// Make sure there is no data provider anymore.
			assertNull(manager.getDataProvider(location));

			// Make sure there is no Component there.
			assertNull(manager.getComponentName(location));

			// Make sure the List of locations for that Component was updated.
			locations = manager.getComponentLocations(component1);
			assertEquals(--newSize, locations.size());
			assertFalse(locations.contains(location));
		}

		// Try removing the Component entirely.
		assertTrue(manager.removeComponent(component1));

		// Try removing the Component entirely again.
		assertFalse(manager.removeComponent(component1));

		// Make sure no location has that Component.
		for (location = 0; location < size; location++) {
			assertFalse(component1.equals(manager.getComponentName(location)));
			assertNull(manager.getDataProvider(location));
		}

		// Add the component a few times, and remove all of them manually.
		assertTrue(manager.addComponent(component1, 0));
		assertTrue(manager.addComponent(component1, 1));
		assertTrue(manager.removeComponent(0));
		// Can't remove from an empty location!
		assertFalse(manager.removeComponent(0));
		assertTrue(manager.removeComponent(1));
		// Can't remove from an empty location!
		assertFalse(manager.removeComponent(1));

		// Try removing the component entirely. Since it's already been removed,
		// it should return false.
		assertFalse(manager.removeComponent(component1));

		// Make sure no location has that Component.
		for (location = 0; location < size; location++) {
			assertFalse(component1.equals(manager.getComponentName(location)));
		}

		// Make sure the Component has no listed locations.
		assertTrue(manager.getComponentLocations(component1).isEmpty());
		/* ------------------------------------ */

		/* ---- Test overwriting a location. ---- */

		// Add component1 in a few locations.
		for (location = 10; location < 20; location++) {
			assertTrue(manager.addComponent(component1, location));

			managedProvider = manager.getDataProvider(location);
			assertEquals(defaultProvider, managedProvider);
			managedProvider.addData(data1, 0.0);
			managedProvider.addData(data1, 1.0);
			managedProvider.addData(data2, 0.0);
			managedProvider.addData(data3, 0.0);
		}

		// Verify the addition of component1.
		locations = manager.getComponentLocations(component1);
		assertEquals(10, locations.size());
		for (location = 10; location < 20; location++) {
			assertEquals(component1, manager.getComponentName(location));
			locations.contains(location);

			assertEquals(provider, manager.getDataProvider(location));
		}

		// Overwrite the the last half of those positions.
		for (location = 15; location < 25; location++) {
			assertTrue(manager.addComponent(component2, location));
		}

		// Verify the removal of component1.
		locations = manager.getComponentLocations(component1);
		assertEquals(5, locations.size());
		for (location = 10; location < 15; location++) {
			assertEquals(component1, manager.getComponentName(location));
			locations.contains(location);
		}

		// Verify the addition of component2.
		locations = manager.getComponentLocations(component2);
		assertEquals(10, locations.size());
		for (location = 15; location < 25; location++) {
			assertEquals(component2, manager.getComponentName(location));
			locations.contains(location);

			assertEquals(defaultProvider, manager.getDataProvider(location));
		}

		// Overwrite the last half of those positions.
		for (location = 20; location < 25; location++) {
			assertTrue(manager.addComponent(component3, location));
		}

		// Verify that component1 was not touched.
		locations = manager.getComponentLocations(component1);
		assertEquals(5, locations.size());
		for (location = 10; location < 15; location++) {
			assertEquals(component1, manager.getComponentName(location));
			locations.contains(location);
		}

		// Verify the removal of component2.
		locations = manager.getComponentLocations(component2);
		assertEquals(5, locations.size());
		for (location = 15; location < 20; location++) {
			assertEquals(component2, manager.getComponentName(location));
			locations.contains(location);
		}

		// Verify the addition of component3.
		locations = manager.getComponentLocations(component3);
		assertEquals(5, locations.size());
		for (location = 20; location < 25; location++) {
			assertEquals(component3, manager.getComponentName(location));
			locations.contains(location);
		}
		/* -------------------------------------- */

		/* ---- Remove some more. ---- */
		// Make sure we know how many locations are in each Component's List.
		assertEquals(5, manager.getComponentLocations(component1).size());
		assertEquals(5, manager.getComponentLocations(component2).size());
		assertEquals(5, manager.getComponentLocations(component3).size());

		// Remove component2 manually.
		for (location = 15; location < 20; location++) {
			assertTrue(manager.removeComponent(location));
		}

		// Verify removal of component2.
		assertEquals(5, manager.getComponentLocations(component1).size());
		assertTrue(manager.getComponentLocations(component2).isEmpty());
		assertEquals(5, manager.getComponentLocations(component3).size());
		for (location = 0; location < 10; location++) {
			assertNull(manager.getComponentName(location));
		}
		for (location = 10; location < 15; location++) {
			assertEquals(component1, manager.getComponentName(location));
		}
		for (location = 15; location < 20; location++) {
			assertNull(manager.getComponentName(location));
		}
		for (location = 20; location < 25; location++) {
			assertEquals(component3, manager.getComponentName(location));
		}
		for (location = 25; location < size; location++) {
			assertNull(manager.getComponentName(location));
		}

		// Remove component3 at once.
		assertTrue(manager.removeComponent(component3));
		assertEquals(5, manager.getComponentLocations(component1).size());
		assertTrue(manager.getComponentLocations(component2).isEmpty());
		assertTrue(manager.getComponentLocations(component3).isEmpty());
		for (location = 0; location < 10; location++) {
			assertNull(manager.getComponentName(location));
		}
		for (location = 10; location < 15; location++) {
			assertEquals(component1, manager.getComponentName(location));
		}
		for (location = 15; location < size; location++) {
			assertNull(manager.getComponentName(location));
		}

		// Remove component1.
		assertTrue(manager.removeComponent(component1));
		assertTrue(manager.getComponentLocations(component1).isEmpty());
		assertTrue(manager.getComponentLocations(component2).isEmpty());
		assertTrue(manager.getComponentLocations(component3).isEmpty());
		for (location = 0; location < size; location++) {
			assertNull(manager.getComponentName(location));
		}
		/* --------------------------- */

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the equality and hashCode operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		int size = 257;

		// Initialize objects for testing.
		GridDataManager object = new GridDataManager(size);
		GridDataManager equalObject = new GridDataManager(size);
		GridDataManager unequalObject = new GridDataManager(size);

		// Set up the object and equalObject.
		String component = "Marvin";
		SFRData data = new SFRData("Fictional character");
		data.setValue(42.0);

		object.addComponent("Ford", 50);
		object.addComponent("Zaphod", 3);
		object.addComponent(component, 84);
		object.addComponent(component, 37);
		object.getDataProvider(3).addData(data, 0.0);
		object.getDataProvider(37).addData(data, 0.0);

		equalObject.addComponent("Ford", 50);
		equalObject.addComponent("Zaphod", 3);
		equalObject.addComponent(component, 84);
		equalObject.addComponent(component, 37);
		equalObject.getDataProvider(3).addData(data, 0.0);
		equalObject.getDataProvider(37).addData(data, 0.0);

		// Set up the unequalObject.
		unequalObject.addComponent("Ford", 50);
		unequalObject.addComponent("Zaphod", 3);
		unequalObject.addComponent(component, 84);
		unequalObject.addComponent(component, 37);
		unequalObject.getDataProvider(3).addData(data, 0.0);
		unequalObject.getDataProvider(50).addData(data, 0.0); // Different!

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the copying and cloning operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		int size = 720;

		// Initialize objects for testing.
		GridDataManager object = new GridDataManager(size);
		GridDataManager copy = new GridDataManager(size);
		GridDataManager clone = null;

		// Set up the object.
		String component = "Marvin";
		SFRData data = new SFRData("Fictional character");
		data.setValue(42.0);

		object.addComponent("Ford", 50);
		object.addComponent("Zaphod", 3);
		object.addComponent(component, 84);
		object.addComponent(component, 37);
		object.getDataProvider(84).addData(data, 1.0);
		object.getDataProvider(37).addData(data, 1.0);
		object.getDataProvider(37).addData(data, 0.0);

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
		clone = (GridDataManager) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
		// end-user-code
	}
}
