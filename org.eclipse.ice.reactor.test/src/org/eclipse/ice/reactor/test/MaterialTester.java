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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * A class that tests the Material's operations.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class MaterialTester {
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
	 * This operation checks the constructors and their default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Local Declarations
		Material material = null;
		String defaultName = "Material";
		String defaultDesc = "Material's Description";
		String newName = "Special Material";
		MaterialType defaultType = MaterialType.SOLID;
		MaterialType newType = MaterialType.GAS;
		int defaultId = 1;
		HDF5LWRTagType type = HDF5LWRTagType.MATERIAL;

		// Instantiate material and check default values
		material = new Material();
		assertEquals(defaultName, material.getName());
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(defaultType, material.getMaterialType());
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material with different name and check default values
		material = new Material(newName);
		assertEquals(newName, material.getName());
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(defaultType, material.getMaterialType());
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material incorrectly and check default values
		material = new Material(null);
		assertEquals(defaultName, material.getName()); // Name defaults
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(defaultType, material.getMaterialType());
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material with different name and different type. Check
		// values
		material = new Material(newName, newType);
		assertEquals(newName, material.getName());
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(newType, material.getMaterialType());
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material with erroneous name and type - check defaults
		material = new Material(null, null);
		assertEquals(defaultName, material.getName()); // defaults
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(defaultType, material.getMaterialType()); // defaults
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material with erroneous type, but not name - check
		// defaults
		material = new Material(newName, null);
		assertEquals(newName, material.getName());
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(defaultType, material.getMaterialType()); // defaults
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material with erroneous name, but not type - check
		// defaults
		material = new Material(null, newType);
		assertEquals(defaultName, material.getName()); // defaults
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(newType, material.getMaterialType());
		assertEquals(type, material.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for materialTypes.
	 * </p>
	 * 
	 */
	@Test
	public void checkMaterialType() {

		// Local Declarations
		Material material = new Material();
		MaterialType defaultType = MaterialType.SOLID;

		// Check default material type value
		assertEquals(defaultType, material.getMaterialType());

		// Set the material type - SOLID
		material.setMaterialType(MaterialType.SOLID);
		// Check value
		assertEquals(MaterialType.SOLID, material.getMaterialType());

		// Set the material type - LIQUID
		material.setMaterialType(MaterialType.LIQUID);
		// Check value
		assertEquals(MaterialType.LIQUID, material.getMaterialType());

		// Set the material type - GAS
		material.setMaterialType(MaterialType.GAS);
		// Check value
		assertEquals(MaterialType.GAS, material.getMaterialType());

		// Check for null
		material.setMaterialType(null);
		// See that it is the last set made
		// Check value
		assertEquals(MaterialType.GAS, material.getMaterialType());

	}

	/**
	 * <p>
	 * This operation checks the equals and hashCode operations.
	 * </p>
	 * 
	 */
	public void checkEquality() {

		// Local Declarations
		Material object, equalObject, unEqualObject, transitiveObject;
		String name = "Bill";
		MaterialType type = MaterialType.GAS;
		MaterialType unEqualType = MaterialType.LIQUID;
		String description = "Description !@#!@#!@#!46546484328";
		int id = 68468431;

		// Setup root object
		object = new Material(name);
		object.setId(id);
		object.setDescription(description);
		object.setMaterialType(type);

		// Setup equalObject equal to object
		equalObject = new Material(name);
		equalObject.setId(id);
		equalObject.setDescription(description);
		equalObject.setMaterialType(type);

		// Setup transitiveObject equal to object
		transitiveObject = new Material(name);
		transitiveObject.setId(id);
		transitiveObject.setDescription(description);
		transitiveObject.setMaterialType(type);

		// Set its data, not equal to object
		// Does not contain components!
		unEqualObject = new Material(name);
		unEqualObject.setId(id);
		unEqualObject.setDescription(description);
		unEqualObject.setMaterialType(unEqualType);

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
	public void checkCopying() {

		// Local declarations
		Material object, copyObject, clonedObject;
		String name = "Bill";
		MaterialType type = MaterialType.GAS;
		String description = "Description !@#!@#!@#!46546484328";
		int id = 68468431;

		// Setup root object
		object = new Material(name);
		object.setId(id);
		object.setDescription(description);
		object.setMaterialType(type);

		// Run the copy routine
		copyObject = new Material();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (Material) object.clone();

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

	/**
	 * <p>
	 * Checks the toString and toType operations on MaterialType.
	 * </p>
	 * 
	 */
	@Test
	public void checkTyping() {

		// Local Declarations
		MaterialType type;

		// Check the toString implementations of the materialType enum
		assertEquals("Gas", MaterialType.GAS.toString());
		assertEquals("Solid", MaterialType.SOLID.toString());
		assertEquals("Liquid", MaterialType.LIQUID.toString());

		// Check the toType implementations of the HDf5 enum

		// Specify the type
		type = MaterialType.GAS;
		// Check the type
		assertEquals(type.toType("Gas"), MaterialType.GAS);

		// Specify the type
		type = MaterialType.LIQUID;
		// Check the type
		assertEquals(type.toType("Liquid"), MaterialType.LIQUID);

		// Specify the type
		type = MaterialType.SOLID;
		// Check the type
		assertEquals(type.toType("Solid"), MaterialType.SOLID);

		// Try to return a type that does not exist
		assertEquals(type.toType("asdasd1"), null);

	}
}