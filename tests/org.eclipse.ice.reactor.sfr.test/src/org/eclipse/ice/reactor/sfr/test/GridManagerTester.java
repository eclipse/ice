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

import org.eclipse.ice.reactor.sfr.base.GridManager;
import org.junit.Test;

public class GridManagerTester {

	@Test
	public void checkConstruction() {
		// begin-user-code

		// Some size values.
		int defaultSize = Integer.MAX_VALUE;
		int size = 42;
		int location;

		// A manager to test and a component for adding.
		GridManager manager;
		String name = "Sir Digby Chicken Caesar";

		/* ---- Test the manager with a valid size. ---- */
		// Initialize the manager.
		manager = new GridManager(size);

		// Test the lower bound on the list of positions.
		for (location = -10; location < 0; location++) {
			assertFalse(manager.addComponent(name, location));
			assertNull(manager.getComponentName(location));
			assertTrue(manager.getComponentLocations(name).isEmpty());
		}

		// Test the possible positions.
		for (location = 0; location < size; location++) {
			assertTrue(manager.addComponent(name, location));
			assertEquals(name, manager.getComponentName(location));
			assertEquals(location + 1, manager.getComponentLocations(name)
					.size());
		}

		// Test the upper bound on the list of positions.
		for (location = size; location < size + 10; location++) {
			assertFalse(manager.addComponent(name, location));
			assertNull(manager.getComponentName(location));
			assertEquals(size, manager.getComponentLocations(name).size());
		}
		/* --------------------------------------------- */

		/* ---- Test the manager with an invalid size. ---- */
		// Initialize the manager.
		manager = new GridManager(0);

		// Test the lower bound on the list of positions.
		for (location = -10; location < 0; location++) {
			assertFalse(manager.addComponent(name, location));
			assertNull(manager.getComponentName(location));
			assertTrue(manager.getComponentLocations(name).isEmpty());
		}

		// Test the possible positions.
		for (location = 0; location < size + 10; location++) {
			assertTrue(manager.addComponent(name, location));
			assertEquals(name, manager.getComponentName(location));
			assertEquals(location + 1, manager.getComponentLocations(name)
					.size());
		}

		// We should not be able to add anything to the largest location.
		location = defaultSize;
		size = manager.getComponentLocations(name).size();

		assertFalse(manager.addComponent(name, location));
		assertNull(manager.getComponentName(location));
		assertEquals(size, manager.getComponentLocations(name).size());

		// We should be able to add to the next largest location, though.
		location--;
		size++;

		assertTrue(manager.addComponent(name, location));
		assertEquals(name, manager.getComponentName(location));
		assertEquals(size, manager.getComponentLocations(name).size());
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

		// A GridManager to test.
		GridManager manager = new GridManager(size);

		// Some components to test adding/removing.
		String component1 = "Arthur";
		String component2 = "Ford Prefect";
		String component3 = "Marvin";

		List<Integer> locations;

		/* ---- Test adding a Component. ---- */
		// Test the lower bound on the list of positions.
		for (location = -10; location < 0; location++) {
			assertFalse(manager.addComponent(component1, location));
			assertNull(manager.getComponentName(location));
			assertTrue(manager.getComponentLocations(component1).isEmpty());
		}

		// Test the possible positions.
		for (location = 0; location < size; location++) {
			assertTrue(manager.addComponent(component1, location));
			assertEquals(component1, manager.getComponentName(location));
			assertEquals(location + 1, manager
					.getComponentLocations(component1).size());
		}

		// Test the upper bound on the list of positions.
		for (location = size; location < size + 10; location++) {
			assertFalse(manager.addComponent(component1, location));
			assertNull(manager.getComponentName(location));
			assertEquals(size, manager.getComponentLocations(component1).size());
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
		}

		// Verify the addition of component1.
		locations = manager.getComponentLocations(component1);
		assertEquals(10, locations.size());
		for (location = 10; location < 20; location++) {
			assertEquals(component1, manager.getComponentName(location));
			locations.contains(location);
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
		GridManager object = new GridManager(size);
		GridManager equalObject = new GridManager(size);
		GridManager unequalObject = new GridManager(size);

		// Set up the object and equalObject.
		String component = "Marvin";

		object.addComponent("Ford", 50);
		object.addComponent("Zaphod", 3);
		object.addComponent(component, 84);
		object.addComponent(component, 37);

		equalObject.addComponent("Ford", 50);
		equalObject.addComponent("Zaphod", 3);
		equalObject.addComponent(component, 84);
		equalObject.addComponent(component, 37);

		// Set up the unequalObject.
		unequalObject.addComponent("Ford", 50);
		unequalObject.addComponent("Zaphod", 3);
		unequalObject.addComponent(component, 84);
		unequalObject.addComponent(component, 36); // Different!

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
		GridManager object = new GridManager(size);
		GridManager copy = new GridManager(size);
		GridManager clone = null;

		// Set up the object.
		String component = "Marvin";

		object.addComponent("Ford", 50);
		object.addComponent("Zaphod", 3);
		object.addComponent(component, 84);
		object.addComponent(component, 37);

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
		clone = (GridManager) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
		// end-user-code
	}
}
