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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class tests the GridLabelProvider class.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class GridLabelProviderTester {

	// An @BeforeClass that sets up the library path. This should be added to
	// the model or removed if it can be fixed for local machine
	@BeforeClass
	public static void beforeClass() {

		// Set the path to the library
		// System.setProperty("java.library.path", "/usr/lib64");
		// System.setProperty("java.library.path", "/home/Scott Forest Hull II/usr/local/lib64");
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
		GridLabelProvider provider;
		int defaultSize = 1;
		String defaultName = "GridLabelProvider 1";
		String defaultDescription = "GridLabelProvider 1's Description";
		int defaultId = 1;
		HDF5LWRTagType type = HDF5LWRTagType.GRID_LABEL_PROVIDER;

		// new declarations
		int newSize = 10;

		// Show default value for constructor
		provider = new GridLabelProvider(defaultSize);
		// Check values
		assertEquals(defaultSize, provider.getSize());
		assertEquals(defaultName, provider.getName());
		assertEquals(defaultDescription, provider.getDescription());
		assertEquals(defaultId, provider.getId());
		assertEquals(type, provider.getHDF5LWRTag());

		// Show a new size set
		provider = new GridLabelProvider(newSize);
		// Check values
		assertEquals(newSize, provider.getSize());
		assertEquals(defaultName, provider.getName());
		assertEquals(defaultDescription, provider.getDescription());
		assertEquals(defaultId, provider.getId());
		assertEquals(type, provider.getHDF5LWRTag());

		// Show an illegal size, and notice the default value
		provider = new GridLabelProvider(-1);
		// Check values
		assertEquals(defaultSize, provider.getSize()); // Defaults!
		assertEquals(defaultName, provider.getName());
		assertEquals(defaultDescription, provider.getDescription());
		assertEquals(defaultId, provider.getId());
		assertEquals(type, provider.getHDF5LWRTag());

		// Show an illegal size, and notice the default value
		provider = new GridLabelProvider(0);
		// Check values
		assertEquals(defaultSize, provider.getSize()); // Defaults!
		assertEquals(defaultName, provider.getName());
		assertEquals(defaultDescription, provider.getDescription());
		assertEquals(defaultId, provider.getId());
		assertEquals(type, provider.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * This operation tests the getters and setter of rowLabels.
	 * </p>
	 * 
	 */
	@Test
	public void checkRows() {
		// Local Declarations
		GridLabelProvider provider;
		int size = 5;
		ArrayList<String> validList = new ArrayList<String>();
		ArrayList<String> invalidList = new ArrayList<String>();

		// Setup list
		validList.add("A");
		validList.add("B");
		validList.add("C");
		validList.add("D");
		validList.add("E");

		invalidList.add("A");
		invalidList.add("B");

		// Check the default row size
		provider = new GridLabelProvider(size);
		assertEquals(size, provider.getSize());

		// Get the default values for rows - should all be null
		assertNull(provider.getLabelFromRow(1));
		assertNull(provider.getLabelFromRow(0));
		assertNull(provider.getLabelFromRow(-1));
		assertNull(provider.getLabelFromRow(6)); // Even though out of range,
													// shouldn't break it

		// Check the default values for strings - should return null
		assertEquals(-1, provider.getRowFromLabel(null));
		assertEquals(-1,
				provider.getRowFromLabel("Not in there string191248015/*/*"));

		// Set invalid arraylist size
		provider.setRowLabels(invalidList);

		// Get the default values for rows - should all be null
		assertNull(provider.getLabelFromRow(1));
		assertNull(provider.getLabelFromRow(0));
		assertNull(provider.getLabelFromRow(-1));
		assertNull(provider.getLabelFromRow(6)); // Even though out of range,
													// shouldn't break it

		// Check the default values for strings - should return null
		assertEquals(-1, provider.getRowFromLabel(null));
		assertEquals(-1,
				provider.getRowFromLabel("Not in there string191248015/*/*"));

		// Set a valid list
		provider.setRowLabels(validList);

		// Check list size
		assertEquals(validList.size(), provider.getSize());

		// Check each label to see it is in there
		for (int i = 0; i < validList.size(); i++) {
			// Check row count and label identification
			assertEquals(validList.get(i), provider.getLabelFromRow(i));
			assertEquals(i, provider.getRowFromLabel(validList.get(i)));
		}

		// Show that the rows can be set again and that the data is separated
		// from the list
		// Set a valid list
		validList.remove(0);
		validList.add("DD");
		provider.setRowLabels(validList);

		// Check list size
		assertEquals(validList.size(), provider.getSize());

		// Check each label to see it is in there
		for (int i = 0; i < validList.size(); i++) {
			// Check row count and label identification
			assertEquals(validList.get(i), provider.getLabelFromRow(i));
			assertEquals(i, provider.getRowFromLabel(validList.get(i)));
		}

		// Check ArrayList separation
		ArrayList<String> newList = (ArrayList<String>) validList.clone();
		validList.remove(0);
		validList.add(0, "BOBBY!");

		// Check list size
		assertEquals(newList.size(), provider.getSize());

		// Check each label to see it is in there
		for (int i = 0; i < newList.size(); i++) {
			// Check row count and label identification
			assertEquals(newList.get(i), provider.getLabelFromRow(i));
			assertEquals(i, provider.getRowFromLabel(newList.get(i)));
		}

	}

	/**
	 * <p>
	 * This operation checks the getters and setter of columnLabels.
	 * </p>
	 * 
	 */
	@Test
	public void checkColumns() {
		// Local Declarations
		GridLabelProvider provider;
		int size = 5;
		ArrayList<String> validList = new ArrayList<String>();
		ArrayList<String> invalidList = new ArrayList<String>();

		// Setup list
		validList.add("A");
		validList.add("B");
		validList.add("C");
		validList.add("D");
		validList.add("E");

		invalidList.add("A");
		invalidList.add("B");

		// Check the default Column size
		provider = new GridLabelProvider(size);
		assertEquals(size, provider.getSize());

		// Get the default values for Columns - should all be null
		assertNull(provider.getLabelFromColumn(1));
		assertNull(provider.getLabelFromColumn(0));
		assertNull(provider.getLabelFromColumn(-1));
		assertNull(provider.getLabelFromColumn(6)); // Even though out of range,
													// shouldn't break it

		// Check the default values for strings - should return null
		assertEquals(-1, provider.getColumnFromLabel(null));
		assertEquals(-1,
				provider.getColumnFromLabel("Not in there string191248015/*/*"));

		// Set invalid arraylist size
		provider.setColumnLabels(invalidList);

		// Get the default values for Columns - should all be null
		assertNull(provider.getLabelFromColumn(1));
		assertNull(provider.getLabelFromColumn(0));
		assertNull(provider.getLabelFromColumn(-1));
		assertNull(provider.getLabelFromColumn(6)); // Even though out of range,
													// shouldn't break it

		// Check the default values for strings - should return null
		assertEquals(-1, provider.getColumnFromLabel(null));
		assertEquals(-1,
				provider.getColumnFromLabel("Not in there string191248015/*/*"));

		// Set a valid list
		provider.setColumnLabels(validList);

		// Check list size
		assertEquals(validList.size(), provider.getSize());

		// Check each label to see it is in there
		for (int i = 0; i < validList.size(); i++) {
			// Check Column count and label identification
			assertEquals(validList.get(i), provider.getLabelFromColumn(i));
			assertEquals(i, provider.getColumnFromLabel(validList.get(i)));
		}

		// Show that the Columns can be set again and that the data is separated
		// from the list
		// Set a valid list
		validList.remove(0);
		validList.add("DD");
		provider.setColumnLabels(validList);

		// Check list size
		assertEquals(validList.size(), provider.getSize());

		// Check each label to see it is in there
		for (int i = 0; i < validList.size(); i++) {
			// Check Column count and label identification
			assertEquals(validList.get(i), provider.getLabelFromColumn(i));
			assertEquals(i, provider.getColumnFromLabel(validList.get(i)));
		}

		// Check ArrayList separation
		ArrayList<String> newList = (ArrayList<String>) validList.clone();
		validList.remove(0);
		validList.add(0, "BOBBY!");

		// Check list size
		assertEquals(newList.size(), provider.getSize());

		// Check each label to see it is in there
		for (int i = 0; i < newList.size(); i++) {
			// Check Column count and label identification
			assertEquals(newList.get(i), provider.getLabelFromColumn(i));
			assertEquals(i, provider.getColumnFromLabel(newList.get(i)));
		}

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
		GridLabelProvider object, equalObject, unEqualObject, transitiveObject;
		ArrayList<String> rowLabels = new ArrayList<String>();
		ArrayList<String> colLabels = new ArrayList<String>();
		int size = 3;

		// Add rows and columns
		rowLabels.add("Iffy");
		rowLabels.add("Biffy");
		rowLabels.add("Sissy");

		colLabels.add("Silly");
		colLabels.add("Billy");
		colLabels.add("Milling");

		// Setup root object
		object = new GridLabelProvider(size);
		object.setColumnLabels(colLabels);
		object.setRowLabels(rowLabels);

		// Setup equalObject equal to object
		equalObject = new GridLabelProvider(size);
		equalObject.setColumnLabels(colLabels);
		equalObject.setRowLabels(rowLabels);

		// Setup transitiveObject equal to object
		transitiveObject = new GridLabelProvider(size);
		transitiveObject.setColumnLabels(colLabels);
		transitiveObject.setRowLabels(rowLabels);

		// Set its data, not equal to object
		unEqualObject = new GridLabelProvider(size);
		unEqualObject.setRowLabels(rowLabels);

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
		if (object.equals(equalObject) && equalObject.equals(transitiveObject)) {
			assertTrue(object.equals(transitiveObject));
		} else {
			fail();
		}

		// Check the Consistent nature of equals()
		assertTrue(object.equals(equalObject) && object.equals(equalObject)
				&& object.equals(equalObject));
		assertTrue(!object.equals(unEqualObject)
				&& !object.equals(unEqualObject)
				&& !object.equals(unEqualObject));

		// Assert checking equality with null value returns false
		assertFalse(object==null);

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
		GridLabelProvider object, copyObject, clonedObject;
		ArrayList<String> rowLabels = new ArrayList<String>();
		ArrayList<String> colLabels = new ArrayList<String>();
		int size = 3;

		// Add rows and columns
		rowLabels.add("Iffy");
		rowLabels.add("Biffy");
		rowLabels.add("Sissy");

		colLabels.add("Silly");
		colLabels.add("Billy");
		colLabels.add("Milling");

		// Setup root object
		object = new GridLabelProvider(size);
		object.setColumnLabels(colLabels);
		object.setRowLabels(rowLabels);

		// Run the copy routine
		copyObject = new GridLabelProvider(0);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (GridLabelProvider) object.clone();

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