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
package org.eclipse.ice.reactor.sfr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.reactor.sfr.base.FeatureSet;
import org.eclipse.ice.reactor.sfr.base.SFRData;
import org.junit.Test;

/**
 * <p>
 * Tests the operations of the FeatureSet class.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class FeatureSetTester {
	/**
	 * <p>
	 * Tests the constructors and default values of the FeatureSet container
	 * class.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		FeatureSet featureSet;
		String defaultFeature = null;
		String feature = "Infinite Improbability Drive";
		ArrayList<IData> data = new ArrayList<IData>();

		// Check the defaults for the constructor with an invalid String.
		featureSet = new FeatureSet(null);
		assertEquals(defaultFeature, featureSet.getName());
		assertEquals(data, featureSet.getData());

		// Check the values for a constructor with an invalid empty String.
		featureSet = new FeatureSet("");
		assertEquals(defaultFeature, featureSet.getName());
		assertEquals(data, featureSet.getData());

		// Check the values for a constructor with an invalid whitespace String.
		featureSet = new FeatureSet("    ");
		assertEquals(defaultFeature, featureSet.getName());
		assertEquals(data, featureSet.getData());

		// Check the values for a constructor with a valid name.
		featureSet = new FeatureSet(feature);
		assertEquals(feature, featureSet.getName());
		assertEquals(data, featureSet.getData());

		return;
	}

	/**
	 * <p>
	 * Tests the getter and addition of IData to a FeatureSet.
	 * </p>
	 * 
	 */
	@Test
	public void checkData() {

		// Some features.
		String sameFeature = "Slartibartfast";
		String diffFeature = "Almighty Bob";

		// Some data using the features.
		SFRData sameData1 = new SFRData(sameFeature);
		SFRData sameData2 = new SFRData(sameFeature);
		SFRData diffData = new SFRData(diffFeature);

		sameData1.setValue(3.1415926);
		diffData.setUncertainty(1.41421);

		/* ---- Test a normal FeatureSet. ---- */
		// Construct a normal FeatureSet.
		FeatureSet featureSet = new FeatureSet(sameFeature);

		// Add some data for that feature.
		assertTrue(featureSet.addIData(sameData1));
		// Check the contents.
		assertEquals(1, featureSet.getData().size());
		assertTrue(sameData1.equals(featureSet.getData().get(0)));

		// Add some more data.
		assertTrue(featureSet.addIData(sameData2));
		// Check the contents.
		assertEquals(2, featureSet.getData().size());
		assertTrue(sameData1.equals(featureSet.getData().get(0)));
		assertTrue(sameData2.equals(featureSet.getData().get(1)));

		// Try adding the same data again.
		assertTrue(featureSet.addIData(sameData1));
		// Check the contents.
		assertEquals(3, featureSet.getData().size());
		assertTrue(sameData1.equals(featureSet.getData().get(0)));
		assertTrue(sameData2.equals(featureSet.getData().get(1)));
		assertTrue(sameData1.equals(featureSet.getData().get(2)));

		// Add some data for a different feature.
		assertFalse(featureSet.addIData(diffData));
		// Check the contents.
		assertEquals(3, featureSet.getData().size());
		assertTrue(sameData1.equals(featureSet.getData().get(0)));
		assertTrue(sameData2.equals(featureSet.getData().get(1)));
		assertTrue(sameData1.equals(featureSet.getData().get(2)));

		// Try adding a null IData.
		assertFalse(featureSet.addIData(null));
		// Check the contents.
		assertEquals(3, featureSet.getData().size());
		assertTrue(sameData1.equals(featureSet.getData().get(0)));
		assertTrue(sameData2.equals(featureSet.getData().get(1)));
		assertTrue(sameData1.equals(featureSet.getData().get(2)));

		// Show that we can manipulate the data List directly.
		ArrayList<IData> dataList = featureSet.getData();
		dataList.remove(dataList.size() - 1);
		// Check the contents.
		assertEquals(2, featureSet.getData().size());
		assertTrue(sameData1.equals(featureSet.getData().get(0)));
		assertTrue(sameData2.equals(featureSet.getData().get(1)));
		/* ----------------------------------- */

		/* ---- Test a FeatureSet with an invalid name. ---- */
		// Construct an invalid FeatureSet.
		featureSet = new FeatureSet(null);

		// Add some data for that feature.
		assertFalse(featureSet.addIData(sameData1));
		// Check the contents.
		assertTrue(featureSet.getData().isEmpty());

		// Add some more data.
		assertFalse(featureSet.addIData(sameData2));
		// Check the contents.
		assertTrue(featureSet.getData().isEmpty());

		// Try adding the same data again.
		assertFalse(featureSet.addIData(sameData1));
		// Check the contents.
		assertTrue(featureSet.getData().isEmpty());

		// Add some data for a different feature.
		assertFalse(featureSet.addIData(diffData));
		// Check the contents.
		assertTrue(featureSet.getData().isEmpty());

		// Try adding a null IData.
		assertFalse(featureSet.addIData(null));
		// Check the contents.
		assertTrue(featureSet.getData().isEmpty());

		// Try adding a new SFRData based off the FeatureSet's name.
		sameData1 = new SFRData(featureSet.getName());
		assertFalse(featureSet.addIData(sameData1));
		// Check the contents.
		assertTrue(featureSet.getData().isEmpty());

		// We shouldn't be able to add data to it manually.
		dataList = featureSet.getData();
		dataList.add(sameData1);
		// Check the contents.
		assertTrue(featureSet.getData().isEmpty());
		/* ------------------------------------------------- */

		return;
	}

	/**
	 * <p>
	 * Tests the equality and hashCode operations.
	 * </p>
	 */
	@Test
	public void checkEquality() {

		// Feature names to use.
		String feature = "Deep Thought";
		String equalFeature = "   Deep Thought  ";
		String unequalFeature = "Answer to the Ultimate Question of Life, the Universe, and Everything";

		// FeatureSets to test.
		FeatureSet object = new FeatureSet(feature);
		FeatureSet equalObject = new FeatureSet(equalFeature);
		FeatureSet unequalObject = new FeatureSet(unequalFeature);
		FeatureSet invalidObject = new FeatureSet(null);
		FeatureSet invalidObject2 = new FeatureSet(null);

		// Populate the data Lists with some IData.
		SFRData iData;
		for (int i = 0; i < 10; i++) {
			// Data added to the featureSet and invalidFeatureSet.
			iData = new SFRData(feature);
			iData.setValue((double) i);
			iData.setUncertainty(i / 10.0);
			object.addIData(iData);
			invalidObject.addIData(iData);

			// Data added to the equalFeatureSet.
			iData = new SFRData(equalFeature);
			iData.setValue((double) i);
			iData.setUncertainty(i / 10.0);
			equalObject.addIData(iData);

			// Data added to the unequalFeatureSet.
			iData = new SFRData(unequalFeature);
			iData.setValue((double) i);
			iData.setUncertainty(i / 10.0);
			unequalObject.addIData(iData);
		}

		// Test reflexivity and symmetry.
		assertTrue(object.equals(object));
		assertTrue(object.equals(equalObject));
		assertTrue(equalObject.equals(object));

		// Test hash code equality.
		assertTrue(object.hashCode() == object.hashCode());
		assertTrue(object.hashCode() == equalObject.hashCode());

		// Test inequality.
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(object));

		// Test hash code inequality.
		assertFalse(object.hashCode() == unequalObject.hashCode());

		// Test inequality with invalid objects.
		assertFalse(object.equals(invalidObject));
		assertFalse(invalidObject.equals(object));

		// Two invalid objects should be equal!
		assertTrue(invalidObject.equals(invalidObject));
		assertTrue(invalidObject.equals(invalidObject2));
		assertTrue(invalidObject2.equals(invalidObject));

		// Test inequality with null values
		assertFalse(object==null);
		assertFalse(equalObject==null);
		assertFalse(unequalObject==null);
		assertFalse(invalidObject==null);

		// Test inequality with invalid values.
		assertFalse(object.equals(42));
		assertFalse("just a string".equals(object));

		return;
	}

	/**
	 * <p>
	 * Tests the copying and cloning operations.
	 * </p>
	 */
	@Test
	public void checkCopying() {

		// Some feature names.
		String feature = "Blart Versenwald III";
		String otherFeature = "Not Blart Versenwald III";

		// Some objects to test copying/cloning.
		FeatureSet object = new FeatureSet(feature);
		FeatureSet copiedObject = new FeatureSet(otherFeature);
		FeatureSet clonedObject = null;

		// A List to keep track of the original object's data.
		ArrayList<IData> data = new ArrayList<IData>();

		// Populate the data Lists with some IData.
		SFRData iData;
		for (int i = 0; i < 10; i++) {
			// Data added to the featureSet and invalidFeatureSet.
			iData = new SFRData(feature);
			iData.setValue((double) i);
			iData.setUncertainty(i / 10.0);
			object.addIData(iData);
			data.add(iData);

			// Data added to the unequalFeatureSet.
			iData = new SFRData(otherFeature);
			iData.setValue((double) i / 10.0);
			iData.setUncertainty(i);
			copiedObject.addIData(iData);
		}

		/* ---- Test passing in bad arguments to copy. ---- */
		object.copy(null);

		// Make sure we didn't clobber the old contents of object.
		assertTrue(object.getName().equals(feature));
		assertTrue(object.getData().equals(data));
		/* ------------------------------------------------ */

		/* ---- Test copying. ---- */
		// Make sure these are different objects beforehand!
		assertFalse(object == copiedObject);
		assertFalse(object.equals(copiedObject));

		// Copy the contents of object into copiedObject.
		copiedObject.copy(object);

		// Make sure they are still different references but have the same info.
		assertFalse(object == copiedObject);
		assertTrue(object.equals(copiedObject));

		// Make sure we didn't clobber the old contents of object.
		assertTrue(object.getName().equals(feature));
		assertTrue(object.getData().equals(data));
		/* ----------------------- */

		/* ---- Test cloning. ---- */
		// Make sure these are different objects beforehand!
		assertFalse(object.equals(clonedObject));

		// Clone the object.
		clonedObject = (FeatureSet) object.clone();

		// Make sure they are still different references but have the same info.
		assertFalse(object == clonedObject);
		assertTrue(object.equals(clonedObject));
		assertFalse(copiedObject == clonedObject);
		assertTrue(copiedObject.equals(clonedObject));

		// Make sure we didn't clobber the old contents of object.
		assertTrue(object.getName().equals(feature));
		assertTrue(object.getData().equals(data));
		/* ----------------------- */

		return;
	}
}