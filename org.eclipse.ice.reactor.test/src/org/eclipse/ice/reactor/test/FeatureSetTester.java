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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.reactor.FeatureSet;
import org.eclipse.ice.reactor.LWRData;
import org.junit.Test;

/**
 * <p>
 * A class that tests the operations on FeatureSet.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class FeatureSetTester {
	/**
	 * <p>
	 * An operation that checks the constructor and getName operation.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Local Declarations
		FeatureSet featureSet;
		String feature = "pin power";
		String nullFeature = null;
		String emptyFeature = " ";

		// Test normal construction
		featureSet = new FeatureSet(feature);
		assertEquals(feature, featureSet.getName());

		// Invalid construction - null
		featureSet = new FeatureSet(nullFeature);
		assertEquals(nullFeature, featureSet.getName());

		// Invalid construction - empty
		featureSet = new FeatureSet(emptyFeature);
		assertEquals(nullFeature, featureSet.getName()); // Set as null!

	}

	/**
	 * <p>
	 * An operation that checks the getIData and addIData operation.
	 * </p>
	 * 
	 */

	@Test
	public void checkData() {

		// Local Declarations
		String feature1 = "Feature 1";
		String feature2 = "Feature 2";
		String nullFeature = null;
		String emptyFeature = " ";
		ArrayList<IData> dataList;

		// Setup Features
		FeatureSet set1 = new FeatureSet(feature1);
		FeatureSet set2 = new FeatureSet(nullFeature);
		FeatureSet set3 = new FeatureSet(emptyFeature);

		// Setup data
		LWRData data1 = new LWRData(feature1);
		LWRData data2 = new LWRData(feature1);
		LWRData data3 = new LWRData(feature2);

		// Try to add data to the feature set
		assertTrue(set1.addIData(data1));
		// Check contents
		assertEquals(1, set1.getIData().size());
		assertTrue(data1.equals(set1.getIData().get(0)));

		// Try to add data to the feature set
		assertTrue(set1.addIData(data2));
		// Check contents
		assertEquals(2, set1.getIData().size());
		assertTrue(data1.equals(set1.getIData().get(0)));
		assertTrue(data2.equals(set1.getIData().get(0)));

		// Try to add data to the feature set - invalid data
		assertFalse(set1.addIData(data3));
		// Check contents - show that only two pieces remain
		assertEquals(2, set1.getIData().size());
		assertTrue(data1.equals(set1.getIData().get(0)));
		assertTrue(data2.equals(set1.getIData().get(0)));

		// Show that data can be removed from the feature set by manipulating
		// the arraylist directly
		dataList = set1.getIData();
		dataList.remove(1);
		// Check contents - show that the item was removed
		assertEquals(1, set1.getIData().size());
		assertTrue(data1.equals(set1.getIData().get(0)));

		// Try to setup bad FeatureSets and show that data can not be added

		// Show the behavior of a null feature set
		assertFalse(set2.addIData(data1));
		assertEquals(0, set2.getIData().size());
		dataList = set2.getIData();
		dataList.add(data1);

		// Show the list can not be manipulated
		assertEquals(0, set2.getIData().size());

		// Show the behavior of a empty string feature set
		assertFalse(set3.addIData(data1));
		assertEquals(0, set3.getIData().size());
		dataList = set3.getIData();
		dataList.add(data1);

		// Show the list can not be manipulated
		assertEquals(0, set3.getIData().size());

	}

	/**
	 * <p>
	 * An operation that checks the equality operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Create an Object
		FeatureSet object = new FeatureSet("Feature 1");

		// Set its data
		object.addIData(new LWRData("Feature 1"));
		object.addIData(new LWRData("Feature 1"));

		// Create another FeatureSet to assert Equality with the last
		FeatureSet equalObject = new FeatureSet("Feature 1");

		// Set its data, equal to object
		for (int i = 0; i < object.getIData().size(); i++) {
			equalObject.addIData(object.getIData().get(i));
		}

		// Create a unEqualObject that is not equal to Object
		FeatureSet unEqualObject = new FeatureSet("Feature 1");

		// Set its data, not equal to testLWRComponent
		unEqualObject.addIData(equalObject.getIData().get(0));

		// Create a third object to test Transitivity
		FeatureSet transitiveObject = new FeatureSet("Feature 1");

		// Set it equal
		// Set its data, equal to object
		for (int i = 0; i < object.getIData().size(); i++) {
			transitiveObject.addIData(object.getIData().get(i));
		}

		// Assert that these two LWRComponents are equal
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
	 * An operation that checks the operations on copying routines.
	 * </p>
	 * 
	 */

	@Test
	public void checkCopying() {

		// Create an Object
		FeatureSet object = new FeatureSet("Feature 1");
		FeatureSet copyObject, clonedObject;

		// Set its data
		object.addIData(new LWRData("Feature 1"));
		object.addIData(new LWRData("Feature 1"));

		// Run the copy routine
		copyObject = new FeatureSet(null);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (FeatureSet) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

	}
}