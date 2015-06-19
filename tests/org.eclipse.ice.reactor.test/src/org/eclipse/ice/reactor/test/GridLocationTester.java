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

import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.junit.Test;

/**
 * <p>
 * This class checks the operations on GridLocation.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class GridLocationTester {
	/**
	 * <p>
	 * This operation checks the construction and default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Local Declarations
		GridLocation location;
		int defaultRow = 1;
		int defaultCol = 1;

		// new values
		int newRow = 5;
		int newCol = 6;

		// Create a normal gridLocation with normal values
		location = new GridLocation(newRow, newCol);
		// Check values
		assertEquals(newRow, location.getRow());
		assertEquals(newCol, location.getColumn());

		// Create a normal gridLocation with normal values - 0 column and 0 row
		location = new GridLocation(0, 0);
		// Check values - defaults
		assertEquals(0, location.getRow());
		assertEquals(0, location.getColumn());
		assertNotNull(location.getLWRDataProvider());

		// Create a normal gridLocation with normal values of default
		location = new GridLocation(defaultRow, defaultCol);
		// Check values
		assertEquals(defaultRow, location.getRow());
		assertEquals(defaultCol, location.getColumn());
		assertNotNull(location.getLWRDataProvider());

		// Create a normal gridLocation with bad values - negative row and
		// column
		location = new GridLocation(-1, -1);
		// Check values - defaults
		assertEquals(defaultRow, location.getRow());
		assertEquals(defaultCol, location.getColumn());
		assertNotNull(location.getLWRDataProvider());

	}

	/**
	 * <p>
	 * This operation tests the comparable operation.
	 * </p>
	 * 
	 */
	@Test
	public void checkComparable() {

		// Local declaration
		GridLocation location1;
		GridLocation location2;
		int row1 = 2;
		int col1 = 9;
		int row2 = 6;
		int col2 = 7;

		// Instantiate a GridLocation with same row and col
		location1 = new GridLocation(row1, col1);
		location2 = new GridLocation(row1, col1);

		// Check compareTo - should be 0
		assertEquals(0, location1.compareTo(location2));

		// Instantiate a GridLocation with different row and col
		location1 = new GridLocation(row1, col1);
		location2 = new GridLocation(row2, col2);

		// Check compareTo - should be equal to row1-row2
		assertEquals(row1 - row2, location1.compareTo(location2));

		// Instantiate a GridLocation with same row but different col
		location1 = new GridLocation(row1, col1);
		location2 = new GridLocation(row1, col2);

		// Check compareTo - should be equal to col1 - col2
		assertEquals(col1 - col2, location1.compareTo(location2));

		// Instantiate a GridLocation with same col but different row
		location1 = new GridLocation(row1, col1);
		location2 = new GridLocation(row2, col1);

		// Check compareTo - should be false
		assertEquals(row1 - row2, location1.compareTo(location2));

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
		GridLocation object, equalObject, unEqualObject, transitiveObject;
		int row1 = 3, col1 = 4;
		int row2 = 3, col2 = 5;

		// Setup root object
		object = new GridLocation(row1, col1);

		// Setup equalObject equal to object
		equalObject = new GridLocation(row1, col1);

		// Setup transitiveObject equal to object
		transitiveObject = new GridLocation(row1, col1);

		// Set its data, not equal to object
		unEqualObject = new GridLocation(row2, col2);

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

		// Local declarations
		GridLocation object, copyObject, clonedObject;
		int row1 = 3, col1 = 4;

		// Setup root object
		object = new GridLocation(row1, col1);

		// Run the copy routine
		copyObject = new GridLocation(0, 0);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (GridLocation) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

	}

	/**
	 * <p>
	 * Checks the getter and setter for LWRDataProvider.
	 * </p>
	 * 
	 */
	@Test
	public void checkDataProvider() {

		// Local Declarations
		LWRDataProvider provider1 = new LWRDataProvider();
		provider1.setSourceInfo("Source Info 3");
		GridLocation location1 = new GridLocation(0, 0);

		// Try to set
		location1.setLWRDataProvider(provider1);

		// Check comparison
		assertTrue(location1.getLWRDataProvider().equals(provider1));

		// Try null
		location1.setLWRDataProvider(null);

		// Show that it does not change
		assertTrue(location1.getLWRDataProvider().equals(provider1));

	}
}