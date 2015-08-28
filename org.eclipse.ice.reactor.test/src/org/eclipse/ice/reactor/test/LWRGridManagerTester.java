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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRGridManager;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class checks the operations on LWRGridManager.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class LWRGridManagerTester {
	// An @BeforeClass that sets up the library path. This should be added to
	// the model or removed if it can be fixed for local machine
	@BeforeClass
	public static void beforeClass() {

		// Set the path to the library
		// System.setProperty("java.library.path", "/usr/lib64");
		// System.setProperty("java.library.path", "/home/Scott Forest Hull
		// II/usr/local/lib64");
		// System.setProperty("java.library.path",
		// "/home/ICE/hdf-java/lib/linux");

	}

	/**
	 * <p>
	 * This operation tests the constructor and default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Local Declarations
		LWRGridManager manager;
		int defaultSize = 1;
		String defaultName = "LWRGridManager 1";
		String defaultDescription = "LWRGridManager 1's Description";
		int defaultId = 1;
		HDF5LWRTagType type = HDF5LWRTagType.LWRGRIDMANAGER;

		// New
		int newSize = 5;

		// Check normal construction
		manager = new LWRGridManager(newSize);
		// Check values
		assertEquals(newSize, manager.getSize());
		assertEquals(defaultName, manager.getName());
		assertEquals(defaultDescription, manager.getDescription());
		assertEquals(defaultId, manager.getId());
		assertEquals(type, manager.getHDF5LWRTag());

		// Check defaultSize construction
		manager = new LWRGridManager(defaultSize);
		// Check values
		assertEquals(defaultSize, manager.getSize());
		assertEquals(defaultName, manager.getName());
		assertEquals(defaultDescription, manager.getDescription());
		assertEquals(defaultId, manager.getId());
		assertEquals(type, manager.getHDF5LWRTag());

		// Check illegal size - zero
		manager = new LWRGridManager(0);
		// Check value - defaults
		assertEquals(defaultSize, manager.getSize()); // Defaults
		assertEquals(defaultName, manager.getName());
		assertEquals(defaultDescription, manager.getDescription());
		assertEquals(defaultId, manager.getId());
		assertEquals(type, manager.getHDF5LWRTag());

		// Check illegal size - negative
		manager = new LWRGridManager(-1);
		// Check values - defaults
		assertEquals(defaultSize, manager.getSize()); // Defaults
		assertEquals(defaultName, manager.getName());
		assertEquals(defaultDescription, manager.getDescription());
		assertEquals(defaultId, manager.getId());
		assertEquals(type, manager.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * This operation checks the adding and removing of components.
	 * </p>
	 * 
	 */
	@Test
	public void checkComponent() {
		// Local Declarations
		LWRGridManager manager;
		int size = 10;

		// Setup GridLocations values
		GridLocation location1, location2, location3, location4;
		int row1 = 1, row2 = 5, row3 = 9, row4 = 22;
		int col1 = 1, col2 = 5, col3 = 8, col4 = 15;

		// Use LWRComponent
		LWRComponent component1, component2, component3, component4;
		String component1Name = "Component 1";
		String component2Name = "Component 2";
		String component3Name = "Component 3";
		String component4Name = "Component 4";

		// Setup the LWRComponents
		component1 = new LWRComponent(component1Name);
		component2 = new LWRComponent(component2Name);
		component3 = new LWRComponent(component3Name);
		component4 = new LWRComponent(component4Name);

		// Set ids
		component1.setId(1);
		component2.setId(2);
		component3.setId(3);
		component4.setId(4);

		// Null args
		LWRComponent nullComponent = null;
		GridLocation nullLocation = null;

		// Create a manager
		manager = new LWRGridManager(size);

		// Create a gridLocation and add it to the list for Component1
		location1 = new GridLocation(row1, col1);

		// Add it, and check to see if it exists
		manager.addComponent(component1, location1);
		// See if it exists and is equal
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));

		// add 2 more
		location2 = new GridLocation(row2, col2);
		manager.addComponent(component2, location2);

		location3 = new GridLocation(row3, col3);
		manager.addComponent(component3, location3);

		// See if they both exist
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertTrue(component2.getName()
				.equals(manager.getComponentName(location2)));
		assertTrue(component3.getName()
				.equals(manager.getComponentName(location3)));

		// Try to add something on top of something that already exists
		manager.addComponent(component4, location2);
		// Show that it does not exist, and no other items are messed with
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertTrue(component2.getName()
				.equals(manager.getComponentName(location2)));
		assertTrue(component3.getName()
				.equals(manager.getComponentName(location3)));

		// Try to add with a location out of the size range
		location4 = new GridLocation(row4, col4);
		manager.addComponent(component4, location4);
		// Show that it does not exist, and no other items are messed with
		assertNull(manager.getComponentName(location4));
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertTrue(component2.getName()
				.equals(manager.getComponentName(location2)));
		assertTrue(component3.getName()
				.equals(manager.getComponentName(location3)));

		// Try to add with a location of null
		location4 = new GridLocation(row4, col4);
		manager.addComponent(component4, nullLocation);
		// Show that it does not exist, and no other items are messed with
		assertNull(manager.getComponentName(nullLocation));
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertTrue(component2.getName()
				.equals(manager.getComponentName(location2)));
		assertTrue(component3.getName()
				.equals(manager.getComponentName(location3)));

		// Try to add with a component of null
		location4 = new GridLocation(row4, col4);
		manager.addComponent(nullComponent, nullLocation);
		// Show that it does not exist, and no other items are messed with
		assertNull(manager.getComponentName(nullLocation));
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertTrue(component2.getName()
				.equals(manager.getComponentName(location2)));
		assertTrue(component3.getName()
				.equals(manager.getComponentName(location3)));

		// Try to remove based on location - remove component 3
		manager.removeComponent(location3);
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertTrue(component2.getName()
				.equals(manager.getComponentName(location2)));
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Try to remove based on location - bad location (location does not
		// exist)
		manager.removeComponent(location3);
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertTrue(component2.getName()
				.equals(manager.getComponentName(location2)));
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Try to removed based on location - out of range
		manager.removeComponent(location4);
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertTrue(component2.getName()
				.equals(manager.getComponentName(location2)));
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Try to remove - null Location
		manager.removeComponent(nullLocation);
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertTrue(component2.getName()
				.equals(manager.getComponentName(location2)));
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Try to remove - null Component
		manager.removeComponent(nullComponent);
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertTrue(component2.getName()
				.equals(manager.getComponentName(location2)));
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Remove based on Component
		manager.removeComponent(component2);
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertNull(manager.getComponentName(location2)); // Does not exist
															// anymore!
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Remove a component that does not exist
		manager.removeComponent(component2);
		assertTrue(component1.getName()
				.equals(manager.getComponentName(location1)));
		assertNull(manager.getComponentName(location2)); // Does not exist
															// anymore!
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Remove the last component
		manager.removeComponent(component1);
		assertNull(manager.getComponentName(location1)); // Does not exist
															// anymore!
		assertNull(manager.getComponentName(location2)); // Does not exist
															// anymore!
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

	}

	/**
	 * <p>
	 * This operation checks the equals and hashCode operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Local Declarations
		LWRGridManager object, equalObject, unEqualObject, transitiveObject;
		LWRComponent component, component2;
		GridLocation location1, location2;

		// Setup Components
		component = new LWRComponent("Billy Bob");
		component2 = new LWRComponent("Bobby Bill");

		// Setup Locations
		location1 = new GridLocation(1, 6);
		location2 = new GridLocation(3, 4);

		// Setup root object
		object = new LWRGridManager(15);
		object.addComponent(component, location1);
		object.addComponent(component2, location2);

		// Setup equalObject equal to object
		equalObject = new LWRGridManager(15);
		equalObject.addComponent(component, location1);
		equalObject.addComponent(component2, location2);

		// Setup transitiveObject equal to object
		transitiveObject = new LWRGridManager(15);
		transitiveObject.addComponent(component, location1);
		transitiveObject.addComponent(component2, location2);

		// Set its data, not equal to object
		unEqualObject = new LWRGridManager(15);
		unEqualObject.addComponent(component2, location1);

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
		if (object.equals(equalObject)
				&& equalObject.equals(transitiveObject)) {
			assertTrue(object.equals(transitiveObject));
		} else {
			fail();
		}

		// Check the Consistent nature of equals()
		assertTrue(object.equals(equalObject) && object.equals(equalObject)
				&& object.equals(equalObject));
		assertTrue(
				!object.equals(unEqualObject) && !object.equals(unEqualObject)
						&& !object.equals(unEqualObject));

		// Assert checking equality with null value returns false
		assertFalse(object == null);

		// Assert that two equal objects have the same hashcode
		assertTrue(object.equals(equalObject)
				&& object.hashCode() == equalObject.hashCode());

		// Assert that hashcode is consistent
		assertTrue(object.hashCode() == object.hashCode());

		// Assert that hashcodes are different for unequal objects
		assertFalse(object.hashCode() == unEqualObject.hashCode());

	}

	/**
	 * <p>
	 * This operation checks the copying and clone operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Local Declarations
		LWRGridManager object, copyObject, clonedObject;
		LWRComponent component, component2;
		GridLocation location1, location2;

		// Setup Components
		component = new LWRComponent("Billy Bob");
		component2 = new LWRComponent("Bobby Bill");

		// Setup Locations
		location1 = new GridLocation(1, 6);
		location2 = new GridLocation(3, 4);

		// Setup root object
		object = new LWRGridManager(15);
		object.addComponent(component, location1);
		object.addComponent(component2, location2);

		// Run the copy routine
		copyObject = new LWRGridManager(2);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (LWRGridManager) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

	}

	/**
	 * <p>
	 * Checks the dataprovider operations or operations specific for obtaining a
	 * data provider.
	 * </p>
	 * 
	 */
	@Test
	public void checkDataProvider() {

		// Local Declarations
		int size = 5;
		LWRGridManager manager = new LWRGridManager(size);

		String name1 = "Foo1";
		String name2 = "Foo2";
		LWRComponent component1 = new LWRComponent(name1);
		LWRComponent component2 = new LWRComponent(name2);
		GridLocation location1 = new GridLocation(2, 3);
		GridLocation location2 = new GridLocation(3, 3);
		GridLocation location3 = new GridLocation(1, 3);

		// Try to get locations when there are no GridLocations or names
		assertEquals(0, manager.getGridLocationsAtName(name1).size());
		assertNull(manager.getDataProviderAtLocation(location1));

		// Add to grid
		manager.addComponent(component1, location1);

		// Try to get locations
		assertEquals(1, manager.getGridLocationsAtName(name1).size());
		assertTrue(
				manager.getGridLocationsAtName(name1).get(0).equals(location1));
		assertNotNull(manager.getDataProviderAtLocation(location1));

		// Add to another location
		manager.addComponent(component1, location2);

		// Try to get locations
		assertEquals(2, manager.getGridLocationsAtName(name1).size());
		assertTrue(
				manager.getGridLocationsAtName(name1).get(0).equals(location1));
		assertTrue(
				manager.getGridLocationsAtName(name1).get(1).equals(location2));
		assertNotNull(manager.getDataProviderAtLocation(location1));
		assertNotNull(manager.getDataProviderAtLocation(location2));

		// Add to another location - different component
		manager.addComponent(component2, location3);

		// Try to get locations
		assertEquals(2, manager.getGridLocationsAtName(name1).size());
		assertTrue(
				manager.getGridLocationsAtName(name1).get(0).equals(location1));
		assertTrue(
				manager.getGridLocationsAtName(name1).get(1).equals(location2));

		// Check second component
		assertEquals(1, manager.getGridLocationsAtName(name2).size());
		assertTrue(
				manager.getGridLocationsAtName(name2).get(0).equals(location3));

		// Check all added locations
		assertNotNull(manager.getDataProviderAtLocation(location1));
		assertNotNull(manager.getDataProviderAtLocation(location2));
		assertNotNull(manager.getDataProviderAtLocation(location3));

	}

	/**
	 * <p>
	 * Removes the test.h5 file after the tests fails (to keep the workspace
	 * clean).
	 * </p>
	 * 
	 */
	@AfterClass
	public static void afterClass() {

		// Cleans up the datafile if it exists due to a failed test
		File dataFile = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "test.h5");

		// If it exists, remove it
		if (dataFile.exists()) {
			dataFile.delete();
		}

	}
}