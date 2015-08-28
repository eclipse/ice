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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;

import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This test checks IncoreInstrument Class construction and passing a thimble
 * ring
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class IncoreInstrumentTester {
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
		IncoreInstrument instrument;
		// Default Values
		String defaultName = "Instrument 1";
		String defaultDesc = "Default Instrument";
		// New Values
		String newName = "Instrument 2";
		Ring newThimble = new Ring("thimble");
		HDF5LWRTagType type = HDF5LWRTagType.INCORE_INSTRUMENT;

		// Check nullary constructor
		instrument = new IncoreInstrument();
		assertEquals(defaultName, instrument.getName());
		assertEquals(defaultDesc, instrument.getDescription());
		assertNotNull(instrument.getThimble());
		assertEquals(type, instrument.getHDF5LWRTag());

		// Check non-nullary constructor
		instrument = new IncoreInstrument(newName, newThimble);
		assertEquals(newName, instrument.getName());
		assertEquals(newThimble, instrument.getThimble());
		assertEquals(type, instrument.getHDF5LWRTag());

		// Check non-nullary constructor illegal values illegal value is set to
		// default
		instrument = new IncoreInstrument(null, newThimble);
		assertEquals(defaultName, instrument.getName());
		assertEquals(newThimble, instrument.getThimble());
		assertEquals(type, instrument.getHDF5LWRTag());

		instrument = new IncoreInstrument(null, null);
		assertEquals(defaultName, instrument.getName());
		assertNotNull(instrument.getThimble());
		assertEquals(type, instrument.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * This operation tests the getter and setter of the thimble.
	 * </p>
	 * 
	 */
	@Test
	public void checkThimble() {
		// check a legal value set
		IncoreInstrument instrument = new IncoreInstrument();
		Ring newThimble = new Ring("thimble");
		instrument.setThimble(newThimble);
		assertEquals(newThimble, instrument.getThimble());
		// check illegal value set
		instrument.setThimble(null);
		assertNotNull(instrument.getThimble());
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
		IncoreInstrument object, equalObject, unEqualObject, transitiveObject;
		String name = "Instruments!";
		Ring thimble = new Ring("THIMBLE!");
		Ring unEqualThimble = new Ring("UNEQUALTHIMBLE!");

		// Setup root object
		object = new IncoreInstrument(name, thimble);

		// Setup equalObject equal to object
		equalObject = new IncoreInstrument(name, thimble);

		// Setup transitiveObject equal to object
		transitiveObject = new IncoreInstrument(name, thimble);

		// Set its data, not equal to object
		// Does not contain components!
		unEqualObject = new IncoreInstrument(name, unEqualThimble);

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
		IncoreInstrument object, copyObject, clonedObject;
		String name = "Instruments!";
		Ring thimble = new Ring("THIMBLE!");

		// Setup root object
		object = new IncoreInstrument(name, thimble);

		// Run the copy routine
		copyObject = new IncoreInstrument();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (IncoreInstrument) object.clone();

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