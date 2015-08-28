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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialType;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.TubeType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * A class that tests Tube's operations.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class TubeTester {
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
		Tube tube;
		// Default values. Change here as needed for tests
		String defaultName = "Tube";
		String defaultDesc = "Tube's Description";
		int defaultId = 1;
		double defaultInnerRadius = 0;
		double defaultOuterRadius = 1;
		double defaultHeight = 1.0;
		TubeType defaultType = TubeType.GUIDE;
		Material defaultMaterial = new Material();
		HDF5LWRTagType type = HDF5LWRTagType.TUBE;

		// New values
		String newName = "Super Tube!";
		Material newMaterial = new Material();
		newMaterial.setName("SuperMaterial!!@#!@#*!@#!");
		double newHeight = 1.51231541513;
		double newOuterRadius = 86.3985656;
		double newInnerRadius = 5.83495787819;
		TubeType newType = TubeType.INSTRUMENT;

		// Check nullary constructor
		tube = new Tube();
		// Check default values
		assertEquals(defaultName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(defaultType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - name
		tube = new Tube(newName);
		// Check default values
		assertEquals(newName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(defaultType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - name with null
		tube = new Tube(null);
		// Check default values
		assertEquals(defaultName, tube.getName()); // Defaults name
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(defaultType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - name and type
		tube = new Tube(newName, newType);
		// Check default values
		assertEquals(newName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(newType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check nullary type - type is null
		tube = new Tube(newName, null);
		// Check default values
		assertEquals(newName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(defaultType, tube.getTubeType()); // Defaults
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - name, tubetype, material, height,
		// outerRadius
		tube = new Tube(newName, newType, newMaterial, newHeight,
				newOuterRadius);
		// Check default values
		assertEquals(newName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(newOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(newHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(newMaterial.getName(), tube.getMaterial().getName());
		assertEquals(newType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - null material
		tube = new Tube(newName, newType, null, newHeight, newOuterRadius);
		// See that it defaults the value
		assertNotNull(tube.getMaterial()); // Defaults

		// Check non-nullary constructor - bad height
		tube = new Tube(newName, newType, newMaterial, 0.0, newOuterRadius);
		// See that it defaults the value
		assertEquals(defaultHeight, tube.getHeight(), 0.0); // Defaults

		// Check non-nullary constructor - bad outerRadius
		tube = new Tube(newName, newType, newMaterial, newHeight, 0.0);
		// See that it defaults the value
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0); // Defaults

		// Check non-nullary constructor - name, tubetype, material, height,
		// innerRadius, outerRadius
		tube = new Tube(newName, newType, newMaterial, newHeight,
				newInnerRadius, newOuterRadius);
		// Check default values
		assertEquals(newName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(newInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(newOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(newHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(newMaterial.getName(), tube.getMaterial().getName());
		assertEquals(newType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - bad innerRadius
		tube = new Tube(newName, newType, newMaterial, newHeight, -1.0,
				newOuterRadius);
		// See that it defaults the value
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0); // Defaults

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the tubeTypes.
	 * </p>
	 * 
	 */
	@Test
	public void checkTubeType() {
		// Local Declarations
		Tube tube = new Tube();

		// Check valid usage of tube type - GUIDE
		tube.setTubeType(TubeType.GUIDE);
		assertEquals(TubeType.GUIDE, tube.getTubeType());

		// Check valid usage of tube type -INSTRUMENT
		tube.setTubeType(TubeType.INSTRUMENT);
		assertEquals(TubeType.INSTRUMENT, tube.getTubeType());

		// Check invalid usage - null
		tube.setTubeType(null);
		assertEquals(TubeType.INSTRUMENT, tube.getTubeType()); // Stays the same
																// as previous
																// value

	}

	/**
	 * <p>
	 * Checks the equals and hashCode operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Local Declarations
		Tube object, equalObject, unEqualObject, transitiveObject;
		String name = "Billy!";
		int id = 654654;
		String description = "ASDFG Billy464646";
		Material material = new Material();
		TubeType type = TubeType.INSTRUMENT;
		TubeType unEqualTubeType = TubeType.GUIDE;
		material.setMaterialType(MaterialType.GAS);
		double innerRadius = 5.0, outerRadius = 8, height = 20.0;

		// Setup root object
		object = new Tube(name, type, material, height, innerRadius,
				outerRadius);
		object.setId(id);
		object.setDescription(description);

		// Setup equalObject equal to object
		equalObject = new Tube(name, type, material, height, innerRadius,
				outerRadius);
		equalObject.setId(id);
		equalObject.setDescription(description);

		// Setup transitiveObject equal to object
		transitiveObject = new Tube(name, type, material, height, innerRadius,
				outerRadius);
		transitiveObject.setId(id);
		transitiveObject.setDescription(description);

		// Set its data, not equal to object
		// Different Material
		unEqualObject = new Tube(name, unEqualTubeType, material, height,
				innerRadius, outerRadius);
		unEqualObject.setId(id);
		unEqualObject.setDescription(description);

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
	 * Checks the copy and clone routines.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Local Declarations
		Tube object, copyObject, clonedObject;
		String name = "Billy!";
		int id = 654654;
		String description = "ASDFG Billy464646";
		Material material = new Material();
		TubeType type = TubeType.INSTRUMENT;
		material.setMaterialType(MaterialType.GAS);
		double innerRadius = 5.0, outerRadius = 8, height = 20.0;

		// Setup root object
		object = new Tube(name, type, material, height, innerRadius,
				outerRadius);
		object.setId(id);
		object.setDescription(description);

		// Run the copy routine
		copyObject = new Tube();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (Tube) object.clone();

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
		TubeType type;

		// Check the toString implementations of the TubeType enum
		assertEquals("Guide", TubeType.GUIDE.toString());
		assertEquals("Instrument", TubeType.INSTRUMENT.toString());

		// Check the toType implementations of the HDf5 enum

		// Specify the type
		type = TubeType.GUIDE;
		// Check the type
		assertEquals(type.toType("Guide"), TubeType.GUIDE);

		// Specify the type
		type = TubeType.INSTRUMENT;
		// Check the type
		assertEquals(type.toType("Instrument"), TubeType.INSTRUMENT);

		// Try to return a type that does not exist
		assertEquals(type.toType("asdasd1"), null);

	}
}