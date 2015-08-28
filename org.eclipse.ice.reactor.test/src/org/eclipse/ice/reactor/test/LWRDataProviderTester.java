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

import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.junit.Test;

/**
 * <p>
 * A class that tests the operations on LWRDataProvider.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class LWRDataProviderTester {
	/**
	 * <p>
	 * An operation that checks construction.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Local Declarations
		LWRDataProvider provider = new LWRDataProvider();

		// Check default values
		assertEquals(0.0, provider.getCurrentTime(), 0.0);
		assertEquals(0, provider.getFeatureList().size());
		assertEquals(0, provider.getNumberOfTimeSteps());
		assertEquals("No Source Available", provider.getSourceInfo());
		assertEquals("seconds", provider.getTimeUnits());
		assertEquals(0, provider.getTimes().size());

	}

	/**
	 * <p>
	 * An operation that checks the IDataProvider and LWRDataPRovider
	 * implementations of IData.
	 * </p>
	 * 
	 */
	@Test
	public void checkDataProvider() {

		// Local Declarations
		LWRDataProvider component;
		String name = "Bob";
		LWRData data1, data2, data3, data4, data5;
		String data1Feature = "Data1";
		String data2Feature = "Data2";
		String defaultSource = "No Source Available";
		String newSource = "A source";
		double time1 = 1.1;
		double time2 = 1.0;
		double time3 = 1.3;
		String timeUnit = "Minutes!";

		// Create a component, set the number of time steps, and show the
		// behaviors of setting the current time step
		component = new LWRDataProvider();

		// Try to set the time step to 1
		component.setTime(1);

		// Show that it is equal to the set timestep
		assertEquals(1.0, component.getCurrentTime(), 0.0);

		// Check invalid - negative
		component.setTime(-1);

		// Defaults
		assertEquals(1.0, component.getCurrentTime(), 0.0);

		// Check valid - 0
		component.setTime(0);

		// Equal to the set timeStep
		assertEquals(0, component.getCurrentTime(), 0.0);

		// Get/set sourceInfo
		component = new LWRDataProvider();

		// Check default
		assertEquals(defaultSource, component.getSourceInfo());

		// Try to set it to a new String
		component.setSourceInfo(newSource);
		// Check values - should be newSource
		assertEquals(newSource, component.getSourceInfo());

		// Try to set it to null - invalid
		component.setSourceInfo(null);
		// Check values - should be newSource
		assertEquals(newSource, component.getSourceInfo()); // Defaults

		// Try to set it to empty string - invalid
		component.setSourceInfo("");
		// Check values - should be newSource
		assertEquals(newSource, component.getSourceInfo()); // Defaults

		// Show that the string is trimmed
		component.setSourceInfo(newSource + " ");
		// Check values - should be newSource
		assertEquals(newSource, component.getSourceInfo()); // Trimmed

		// Setup timeUnits
		component.setTimeUnits(timeUnit);
		// Check values
		assertEquals(timeUnit, component.getTimeUnits());

		// Set timeUnits - nullary
		component.setTimeUnits(null);
		// Check values - nothing has changed
		assertEquals(timeUnit, component.getTimeUnits());

		// Set timeUnits - "empty string"
		component.setTimeUnits(" ");
		// Check values - nothing has changed <- auto trims strings
		assertEquals(timeUnit, component.getTimeUnits());

		// Check getters for IData
		assertEquals(0, component.getFeatureList().size());
		assertEquals(0, component.getFeaturesAtCurrentTime().size());
		assertEquals(-1, component.getTimeStep(0.0));
		assertEquals(0, component.getDataAtCurrentTime(data1Feature).size());

		// Add a IData
		data1 = new LWRData(data1Feature);
		component.addData(data1, time1);

		// Check getters
		assertEquals(1, component.getFeatureList().size());
		assertEquals(data1Feature, component.getFeatureList().get(0));
		assertEquals(0, component.getCurrentTime(), 0.0);
		assertEquals(0, component.getDataAtCurrentTime(data1Feature).size());
		// Change current time
		component.setTime(time1);
		// Check values
		assertEquals(1, component.getDataAtCurrentTime(data1Feature).size());
		assertTrue(data1.equals(component.getDataAtCurrentTime(data1Feature)
				.get(0)));
		// Check number of timesteps
		assertEquals(1, component.getNumberOfTimeSteps());

		// Add more data to current time with same feature
		data2 = new LWRData(data1Feature);
		data2.setUncertainty(33.333);
		// Check getters
		component.addData(data2, time1);
		assertEquals(1, component.getFeatureList().size());
		assertEquals(data1Feature, component.getFeatureList().get(0));
		assertEquals(2, component.getDataAtCurrentTime(data1Feature).size());
		assertTrue(data1.equals(component.getDataAtCurrentTime(data1Feature)
				.get(0)));
		assertTrue(data2.equals(component.getDataAtCurrentTime(data1Feature)
				.get(1)));
		// Check number of timesteps
		assertEquals(1, component.getNumberOfTimeSteps());
		// Check times
		assertEquals(1, component.getTimes().size());
		assertEquals(time1, component.getTimes().get(0).doubleValue(), 0.0);
		assertEquals(0, component.getTimeStep(time1));

		// Add a LWRData to a different time location
		data3 = new LWRData(data1Feature);
		data3.setUncertainty(22.2);
		component.addData(data3, time2);
		// Check getters
		assertEquals(1, component.getFeatureList().size());
		assertEquals(data1Feature, component.getFeatureList().get(0));
		assertEquals(2, component.getDataAtCurrentTime(data1Feature).size());
		assertTrue(data1.equals(component.getDataAtCurrentTime(data1Feature)
				.get(0)));
		assertTrue(data2.equals(component.getDataAtCurrentTime(data1Feature)
				.get(1)));
		// Change time frame
		component.setTime(time2);
		// Check information
		assertEquals(1, component.getDataAtCurrentTime(data1Feature).size());
		assertTrue(data3.equals(component.getDataAtCurrentTime(data1Feature)
				.get(0)));
		// Check number of timesteps
		assertEquals(2, component.getNumberOfTimeSteps());
		// Check times
		assertEquals(2, component.getTimes().size());
		assertEquals(time2, component.getTimes().get(0).doubleValue(), 0.0);
		assertEquals(time1, component.getTimes().get(1).doubleValue(), 0.0);
		assertEquals(0, component.getTimeStep(time2));
		assertEquals(1, component.getTimeStep(time1));

		// Add a LWRData with the same time as time2, but different feature
		data4 = new LWRData(data2Feature);
		data4.setUncertainty(11.1);
		component.addData(data4, time2);
		// Check getters
		assertEquals(2, component.getFeatureList().size());
		assertTrue(component.getFeatureList().contains(data1Feature));
		assertTrue(component.getFeatureList().contains(data2Feature));
		assertEquals(1, component.getDataAtCurrentTime(data1Feature).size());
		assertTrue(data3.equals(component.getDataAtCurrentTime(data1Feature)
				.get(0)));
		assertEquals(1, component.getDataAtCurrentTime(data2Feature).size());
		assertTrue(data4.equals(component.getDataAtCurrentTime(data2Feature)
				.get(0)));
		// Check number of timesteps
		assertEquals(2, component.getNumberOfTimeSteps());
		// Check times
		assertEquals(2, component.getTimes().size());
		assertEquals(time2, component.getTimes().get(0).doubleValue(), 0.0);
		assertEquals(time1, component.getTimes().get(1).doubleValue(), 0.0);
		assertEquals(0, component.getTimeStep(time2));
		assertEquals(1, component.getTimeStep(time1));

		// Check nullaries and invalid setters/parameters
		assertEquals(-1, component.getTimeStep(time1 + 3333));
		assertEquals(-1, component.getTimeStep(-1.0));
		component.addData(null, time1);
		component.addData(data4, -1.0);
		assertEquals(0, component.getDataAtCurrentTime(null).size());
		assertEquals(0, component.getDataAtCurrentTime("FeatureDNE!@#12321321")
				.size());

		// Change to invalid time
		component.setTime(time3 + 3333333);
		assertEquals(time3 + 3333333, component.getCurrentTime(), 0.0);
		component.setTime(-1.0); // Try negative
		assertEquals(time3 + 3333333, component.getCurrentTime(), 0.0); // Does
																		// not
																		// change
		assertEquals(0, component.getFeaturesAtCurrentTime().size());
		assertEquals(0, component.getDataAtCurrentTime(data1Feature).size());

		// Check getters - change time back and show data is not changed
		component.setTime(time2);
		assertEquals(2, component.getFeatureList().size());
		assertTrue(component.getFeatureList().contains(data1Feature));
		assertTrue(component.getFeatureList().contains(data2Feature));
		assertEquals(1, component.getDataAtCurrentTime(data1Feature).size());
		assertTrue(data3.equals(component.getDataAtCurrentTime(data1Feature)
				.get(0)));
		assertEquals(1, component.getDataAtCurrentTime(data2Feature).size());
		assertTrue(data4.equals(component.getDataAtCurrentTime(data2Feature)
				.get(0)));
		// Check number of timesteps
		assertEquals(2, component.getNumberOfTimeSteps());

	}

	/**
	 * <p>
	 * Checks the copying and cloning operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Local Declarations
		LWRDataProvider object, copyObject, clonedObject;
		LWRData data1 = new LWRData("Feature 1");
		double time1 = 3.0;

		// Setup root object
		object = new LWRDataProvider();
		object.addData(data1, time1);

		// Run the copy routine
		copyObject = new LWRDataProvider();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (LWRDataProvider) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

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
		LWRDataProvider object, equalObject, unEqualObject, transitiveObject;
		LWRData data1 = new LWRData("Feature 1");
		LWRData data2 = new LWRData("Feature 2");
		double time1 = 3.0;
		double time2 = 54.0;

		// Setup root object
		object = new LWRDataProvider();
		object.addData(data1, time1);

		// Setup equalObject equal to object
		equalObject = new LWRDataProvider();
		equalObject.addData(data1, time1);

		// Setup transitiveObject equal to object
		transitiveObject = new LWRDataProvider();
		transitiveObject.addData(data1, time1);

		// Set its data, not equal to object
		unEqualObject = new LWRDataProvider();
		unEqualObject.addData(data2, time2);

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
}