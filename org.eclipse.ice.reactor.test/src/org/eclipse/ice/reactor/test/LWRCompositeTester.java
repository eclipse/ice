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
package org.eclipse.ice.reactor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class tests LWRComposite.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class LWRCompositeTester {
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
	 * This operation checks the constructor and it's default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {
		// Local declarations
		LWRComposite composite;
		HDF5LWRTagType type = HDF5LWRTagType.LWRCOMPOSITE;

		// Default values
		String defaultName = "Composite 1";
		String defaultDescription = "Composite 1's Description";
		int defaultId = 1;

		// Check nullary constructor
		composite = new LWRComposite();
		assertEquals(defaultName, composite.getName());
		assertEquals(defaultDescription, composite.getDescription());
		assertEquals(defaultId, composite.getId());
		assertEquals(type, composite.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * Checks the getters and setters for component.
	 * </p>
	 * 
	 */
	@Test
	public void checkComponent() {
		// Local Declarations
		int compositeSize = 17;
		LWRComposite composite = new LWRComposite();
		LWRComponent testComponent = new LWRComponent(),
				testComponent2 = new LWRComponent(),
				testComponent3 = new LWRComponent();
		String testComponentName = "Bob";
		String testComponentName2 = "Bill!";
		int rowLoc1 = 5, colLoc1 = 5;
		int rowLoc2 = 6, colLoc2 = 6;
		int testComponentId = 1000001;

		// Set the ids on the test components
		testComponent.setId(1);
		testComponent2.setId(2);
		testComponent3.setId(3);

		composite = new LWRComposite();
		// Set the second component name
		testComponent2.setName(testComponentName2);

		// Check the names, should be empty!
		assertEquals(0, composite.getComponentNames().size());

		// Try to get by name - valid string, empty string, and null
		assertNull(composite
				.getComponent("validNameThatDoesNotExistInThere152423"));
		assertNull(composite.getComponent(""));
		assertNull(composite.getComponent(null));

		// Set the name
		testComponent.setName(testComponentName);

		// Add to the composite
		composite.addComponent(testComponent);

		// Check the getting of a component
		assertTrue(testComponent
				.equals(composite.getComponent(testComponentName)));

		// Add it in there
		composite.addComponent(testComponent2);

		// Check there are two in there, with separate names
		assertEquals(2, composite.getComponents().size());
		assertEquals(2, composite.getComponentNames().size());
		assertEquals(testComponent.getName(),
				composite.getComponentNames().get(0));
		assertEquals(testComponent2.getName(),
				composite.getComponentNames().get(1));

		// Check values - see the components are different and they reside in
		// the table correctly
		assertTrue(testComponent
				.equals(composite.getComponent(testComponentName)));
		assertTrue(testComponent2
				.equals(composite.getComponent(testComponent2.getName())));

		// Check the names, should contain 2!
		assertEquals(2, composite.getComponentNames().size());
		assertEquals(testComponentName, composite.getComponentNames().get(0));
		assertEquals(testComponentName2, composite.getComponentNames().get(1));

		// Check operation for null
		composite.addComponent(null);
		assertNull(composite.getComponent(null)); // Make sure null does
													// not work!

		// Finally, demonstrate what happens when a component of the same name
		// is added, it should not overwrite the previous item in the table!
		testComponent3.setName(testComponentName); // Same name as the other
													// component
		testComponent3.setId(testComponentId); // Id should differ from
												// testComponent!
		assertFalse(testComponent.getId() == testComponentId);

		// Overwrite in table
		composite.addComponent(testComponent3);

		// Check that the object has not been overwritten
		assertTrue(testComponent
				.equals(composite.getComponent(testComponentName)));
		assertFalse(testComponent3
				.equals(composite.getComponent(testComponentName)));

		// Test to remove components from the composite
		composite.removeComponent(null);
		composite.removeComponent("");
		composite.removeComponent(
				"!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^");

		// Nothing was removed
		// Check the names, should contain 2!
		assertEquals(2, composite.getComponentNames().size());
		assertEquals(testComponentName, composite.getComponentNames().get(0));
		assertEquals(testComponentName2, composite.getComponentNames().get(1));

		// Remove the second component
		composite.removeComponent(testComponent2.getName());

		// Check that it does not exist in the location or getting the name
		assertNull(composite.getComponent(testComponent2.getName()));

		// Check that the first exists
		assertEquals(1, composite.getComponentNames().size());
		assertEquals(testComponentName, composite.getComponentNames().get(0));
		assertEquals(1, composite.getNumberOfComponents());
		assertTrue(testComponent.equals(composite.getComponent(1)));
		assertNull(composite.getComponent(2));

		// Remove the composite, check that it was removed from the map!
		composite.removeComponent(1);
		assertNull(composite.getComponent(testComponent.getName()));
		assertEquals(0, composite.getComponents().size());

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
		LWRComposite object, equalObject, unEqualObject, transitiveObject;
		LWRComponent component = new LWRComponent("Billy the Plummer");
		LWRComponent component2 = new LWRComponent("Billy the Plummer2");

		// Setup root object
		object = new LWRComposite();
		object.addComponent(component);
		object.addComponent(component2);

		// Setup equalObject equal to object
		equalObject = new LWRComposite();
		equalObject.addComponent(component);
		equalObject.addComponent(component2);

		// Setup transitiveObject equal to object
		transitiveObject = new LWRComposite();
		transitiveObject.addComponent(component);
		transitiveObject.addComponent(component2);

		// Set its data, not equal to object
		// Does not contain components!
		unEqualObject = new LWRComposite();

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

		// Local declarations
		LWRComposite object;
		LWRComposite copyObject = new LWRComposite(), clonedObject;

		// Values
		String name = "A LWRComponent!@!@#!#@56483";
		String description = "Description !@#!@#!@#!46546484328";
		int id = 68468431;
		LWRComponent component = new LWRComponent("Billy!");
		LWRComponent component2 = new LWRComponent("Billy2!");

		// Initialize and Setup Object to test
		object = new LWRComposite();
		object.setName(name);
		object.setId(id);
		object.setDescription(description);
		object.addComponent(component);
		object.addComponent(component2);

		// Run the copy routine
		copyObject = new LWRComposite();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (LWRComposite) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

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