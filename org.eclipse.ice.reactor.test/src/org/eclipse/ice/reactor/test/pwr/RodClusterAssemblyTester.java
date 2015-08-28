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
package org.eclipse.ice.reactor.test.pwr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class tests the RodClusterAssembly operations.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class RodClusterAssemblyTester {
	// An @BeforeClass that sets up the library path. This should be added to
	// the model or removed if it can be fixed for local machine
	@BeforeClass
	public static void beforeClass() {

	}

	/**
	 * <p>
	 * This operation checks the constructors and their default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {
		// Local Declarations
		RodClusterAssembly assembly;
		String defaultName = "RodClusterAssembly";
		String defaultDesc = "RodClusterAssembly's Description";
		int defaultId = 1;
		int defaultSize = 1;
		HDF5LWRTagType type = HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY;

		// New names
		String newName = "Super RodClusterAssembly!";
		int newSize = 10;

		// Check the default constructor with a default size. Check default
		// values
		// Test non-nullary constructor - size
		assembly = new RodClusterAssembly(defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with new size
		// Test non-nullary constructor - size
		assembly = new RodClusterAssembly(newSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(newSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with bad size - negative
		// Test non-nullary constructor - size
		assembly = new RodClusterAssembly(-1);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize()); // Defaults
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with name and size
		// Test non-nullary constructor - name, size
		assembly = new RodClusterAssembly(defaultName, defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with bad name
		// Test non-nullary constructor - name, size
		assembly = new RodClusterAssembly(null, defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName()); // Defaults
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with new name and size
		// Test non-nullary constructor - name, size
		assembly = new RodClusterAssembly(newName, newSize);
		// Check values
		assertEquals(newName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(newSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

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
		RodClusterAssembly object, equalObject, unEqualObject, transitiveObject;
		String name = "Billy";
		int size = 5;
		LWRRod rod = new LWRRod("Bob the rod");

		// Setup root object
		object = new RodClusterAssembly(name, size);
		object.addLWRRod(rod);
		object.setLWRRodLocation(rod.getName(), 0, 0);

		// Setup equalObject equal to object
		equalObject = new RodClusterAssembly(name, size);
		equalObject.addLWRRod(rod);
		equalObject.setLWRRodLocation(rod.getName(), 0, 0);

		// Setup transitiveObject equal to object
		transitiveObject = new RodClusterAssembly(name, size);
		transitiveObject.addLWRRod(rod);
		transitiveObject.setLWRRodLocation(rod.getName(), 0, 0);

		// Set its data, not equal to object
		unEqualObject = new RodClusterAssembly(name, size);
		// Uses the default rod

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
		RodClusterAssembly object, copyObject, clonedObject;
		String name = "Billy";
		int size = 5;
		LWRRod rod = new LWRRod("Bob the rod");

		// Setup root object
		object = new RodClusterAssembly(name, size);
		object.addLWRRod(rod);
		object.setLWRRodLocation(rod.getName(), 0, 0);

		// Run the copy routine
		copyObject = new RodClusterAssembly(1);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (RodClusterAssembly) object.clone();

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