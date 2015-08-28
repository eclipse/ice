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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.Ring;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * <p>
 * This class tests the MaterialBlock's operations.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class MaterialBlockTester {
	/**
	 * <p>
	 * This operation checks the adding, getting and removing of Rings.
	 * </p>
	 * 
	 */
	@Test
	public void checkRingMutators() {

		// Local Declarations
		MaterialBlock materialBlock = new MaterialBlock();

		// Check for adding a null ring
		boolean result = materialBlock.addRing(null);
		assertFalse(result);

		// Check for adding a ring
		Ring ring1 = new Ring();
		ring1.setName("Ring 1");
		ring1.setOuterRadius(10);
		ring1.setInnerRadius(9);
		result = materialBlock.addRing(ring1);
		assertTrue(result);

		// Check for adding another ring
		Ring ring2 = new Ring();
		ring2.setName("Ring 2");
		ring2.setOuterRadius(4);
		ring2.setInnerRadius(3);
		result = materialBlock.addRing(ring2);
		assertTrue(result);

		// Check for adding an overlapping ring
		Ring ring3 = new Ring();
		ring3.setName("Ring 3");
		ring3.setOuterRadius(3.5);
		ring3.setInnerRadius(2.5);
		result = materialBlock.addRing(ring3);
		assertFalse(result);

		// Check for getting a ring by a null name
		Ring ring4 = materialBlock.getRing(null);
		assertNull(ring4);

		// Check for getting a ring by a name that does not exist
		Ring ring5 = materialBlock.getRing("Harold");
		assertNull(ring5);

		// Check for getting a ring by a name that does exist
		Ring ring6 = materialBlock.getRing("Ring 2");
		assertNotNull(ring6);
		assertEquals(ring6, ring2);
		assertEquals(ring6.getName(), "Ring 2");
		assertTrue(ring6.getOuterRadius() == 4);
		assertTrue(ring6.getInnerRadius() == 3);

		// Check for getting a ring by a radius not in the set of Rings
		Ring ring7 = materialBlock.getRing(2.5);
		assertNull(ring7);

		// Check for getting a ring by a radius in the set of Rings
		Ring ring8 = materialBlock.getRing(3.5);
		assertNotNull(ring8);
		assertEquals(ring8, ring2);
		assertEquals(ring8.getName(), "Ring 2");
		assertTrue(ring8.getOuterRadius() == 4);
		assertTrue(ring8.getInnerRadius() == 3);

		// Check for getting the set of rings
		ArrayList<Ring> list = materialBlock.getRings();
		assertNotNull(list);
		assertTrue(list.size() == 2);
		assertEquals(list.get(0), ring2);
		assertEquals(list.get(1), ring1);

		// Check for removing a ring by a null name
		result = materialBlock.removeRing(null);
		assertFalse(result);
		list = materialBlock.getRings();
		assertTrue(list.size() == 2);
		assertEquals(list.get(0), ring2);
		assertEquals(list.get(1), ring1);

		// Check for removing a ring that is not in the set
		result = materialBlock.removeRing("Harold");
		assertFalse(result);
		list = materialBlock.getRings();
		assertTrue(list.size() == 2);
		assertEquals(list.get(0), ring2);
		assertEquals(list.get(1), ring1);

		// Check for removing a ring that is in the set
		result = materialBlock.removeRing("Ring 1");
		assertTrue(result);
		list = materialBlock.getRings();
		assertTrue(list.size() == 1);
		assertEquals(list.get(0), ring2);

	}

	/**
	 * <p>
	 * This operation checks the constructor and it's default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {
		// Local Declaration
		MaterialBlock materialBlock;
		// Default values
		String defaultName = "MaterialBlock 1";
		String defaultDesc = "MaterialBlock 1's Description";
		int defaultId = 1;
		HDF5LWRTagType type = HDF5LWRTagType.MATERIALBLOCK;
		double defaultPos = 0.0;

		// Instantiate the materialBlock and check default values
		materialBlock = new MaterialBlock();

		// check values
		assertEquals(defaultName, materialBlock.getName());
		assertEquals(defaultDesc, materialBlock.getDescription());
		assertEquals(defaultId, materialBlock.getId());
		assertEquals(type, materialBlock.getHDF5LWRTag());
		assertEquals(defaultPos, materialBlock.getPosition(), 0.0);

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
		MaterialBlock object, equalObject, unEqualObject, transitiveObject;
		Ring ring1, ring2;

		// Setup Rings
		ring1 = new Ring("Bob", new Material("Billy"), 20.0, 5.0, 10.0);
		ring2 = new Ring("Bob2", new Material("Billy2"), 22.0, 52.0, 102.0);

		// Setup root object
		object = new MaterialBlock();
		object.addRing(ring1);
		object.addRing(ring2);

		// Setup equalObject equal to object
		equalObject = new MaterialBlock();
		equalObject.addRing(ring1);
		equalObject.addRing(ring2);

		// Setup transitiveObject equal to object
		transitiveObject = new MaterialBlock();
		transitiveObject.addRing(ring1);
		transitiveObject.addRing(ring2);

		// Set its data, not equal to object
		unEqualObject = new MaterialBlock();
		unEqualObject.addRing(ring2);

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
		MaterialBlock object, copyObject, clonedObject;
		Ring ring1, ring2;

		// Setup Rings
		ring1 = new Ring("Bob", new Material("Billy"), 20.0, 5.0, 10.0);
		ring2 = new Ring("Bob2", new Material("Billy2"), 22.0, 52.0, 102.0);

		// Setup root object
		object = new MaterialBlock();
		object.addRing(ring1);
		object.addRing(ring2);

		// Run the copy routine
		copyObject = new MaterialBlock();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (MaterialBlock) object.clone();

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
	 * Checks the get/set position operators.
	 * </p>
	 * 
	 */
	public void checkPosition() {
		MaterialBlock materialBlock = new MaterialBlock();

		assertEquals(0, materialBlock.getPosition(), 0.0);

		// Set to bad value
		materialBlock.setPosition(-1.0);
		// Doesnt change
		assertEquals(0, materialBlock.getPosition(), 0.0);

		// Set to good value
		materialBlock.setPosition(0.0);
		assertEquals(0, materialBlock.getPosition(), 0.0);

		// Set to good value
		materialBlock.setPosition(1.0);
		assertEquals(1, materialBlock.getPosition(), 0.0);

	}

	/**
	 * <p>
	 * Checks the comparison operator.
	 * </p>
	 * 
	 */
	public void checkCompareTo() {

		// Local Declarations
		MaterialBlock mBlock1, mBlock2, mBlock3;
		double pos1, pos2;
		pos1 = 1.0;
		pos2 = 2.0;

		// Create blocks
		// Less than
		mBlock1 = new MaterialBlock();
		mBlock1.setPosition(pos1);

		// Greater than
		mBlock2 = new MaterialBlock();
		mBlock2.setPosition(pos2);

		// Equal to block1
		mBlock3 = new MaterialBlock();
		mBlock3.setPosition(pos1);

		// Show comparisons
		assertEquals(-1, mBlock1.compareTo(mBlock2));
		assertEquals(1, mBlock2.compareTo(mBlock1));
		assertEquals(0, mBlock1.compareTo(mBlock3));

	}
}