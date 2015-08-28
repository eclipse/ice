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

import java.io.File;

import org.eclipse.ice.datastructures.test.TestComponentListener;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRData;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * A class that tests the LWRComponent
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class LWRComponentTester {
	/**
	 * <p>
	 * The component listener to test with for the notifications on
	 * LWRComponent.
	 * </p>
	 * 
	 */
	private TestComponentListener testComponentListener;

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
	 * This operation checks the constructors and their default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Local declarations
		LWRComponent component = new LWRComponent();
		HDF5LWRTagType type = HDF5LWRTagType.LWRCOMPONENT;

		String timeUnit = "seconds";

		// Check default values - name, id, description
		assertEquals("Component 1", component.getName());
		assertEquals(0, component.getId());
		assertEquals("Component 1's Description", component.getDescription());
		assertEquals(type, component.getHDF5LWRTag());
		assertEquals(timeUnit, component.getTimeUnits());
		assertEquals(0, component.getCurrentTime(), 0.0);
		assertEquals(0, component.getNumberOfTimeSteps());
		assertEquals("No Source Available", component.getSourceInfo());

		// Check nonnullary constructor - name
		component = new LWRComponent("Bob");

		// Check default values - name, id, description
		assertEquals("Bob", component.getName());
		assertEquals(0, component.getId());
		assertEquals("Component 1's Description", component.getDescription());
		assertEquals(type, component.getHDF5LWRTag());
		assertEquals(timeUnit, component.getTimeUnits());
		assertEquals(0, component.getCurrentTime(), 0.0);
		assertEquals(0, component.getNumberOfTimeSteps());
		assertEquals("No Source Available", component.getSourceInfo());

		// Check erroneous value for constructor - name=null
		component = new LWRComponent(null);

		// Check default values - name, id, description
		assertEquals("Component 1", component.getName()); // Defaults
		assertEquals(0, component.getId());
		assertEquals("Component 1's Description", component.getDescription());
		assertEquals(type, component.getHDF5LWRTag());
		assertEquals(timeUnit, component.getTimeUnits());
		assertEquals(0, component.getCurrentTime(), 0.0);
		assertEquals(0, component.getNumberOfTimeSteps());
		assertEquals("No Source Available", component.getSourceInfo());

		// Check erroneous value for constructor - empty string
		component = new LWRComponent("");

		// Check default values - name, id, description
		assertEquals("Component 1", component.getName()); // Defaults
		assertEquals(0, component.getId());
		assertEquals("Component 1's Description", component.getDescription());
		assertEquals(type, component.getHDF5LWRTag());
		assertEquals(timeUnit, component.getTimeUnits());
		assertEquals(0, component.getCurrentTime(), 0.0);
		assertEquals(0, component.getNumberOfTimeSteps());
		assertEquals("No Source Available", component.getSourceInfo());

	}

	/**
	 * <p>
	 * Checks the attribute's getters and setters.
	 * </p>
	 * 
	 */
	@Test
	public void checkAttributes() {
		// Local declarations
		LWRComponent component = new LWRComponent();

		// Change all values
		component.setName("Component 2");
		component.setId(2);
		component.setDescription("FooRA!");

		// Check values - name, id, description
		assertEquals("Component 2", component.getName());
		assertEquals(2, component.getId());
		assertEquals("FooRA!", component.getDescription());

		// Try to set them illegally
		component.setName(null);
		component.setId(-1);
		component.setDescription(null);

		// Check values - name, id, description - does not change!
		assertEquals("Component 2", component.getName());
		assertEquals(2, component.getId());
		assertEquals("FooRA!", component.getDescription());

		// Check setting the name to empty string
		component.setName("");
		component.setName(" ");
		component.setName("       ");
		component.setDescription("");
		component.setDescription(" ");
		component.setDescription("       ");

		// See the name will not be the representation of the empty string.
		assertEquals("Component 2", component.getName());
		assertEquals(2, component.getId());
		assertEquals("FooRA!", component.getDescription());

		// Also, see that it will always trim the strings for Name and
		// description
		component.setName("Component  3  ");
		component.setDescription(" FooRA!! ");

		// See the data is trimmed appropriately
		assertEquals("Component  3", component.getName()); // Note double space
															// inbetween
															// Component and 3
		assertEquals(2, component.getId());
		assertEquals("FooRA!!", component.getDescription());

	}

	/**
	 * <p>
	 * This operation checks equals() and hashCode() operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Create a LWRComponent
		LWRComponent testLWRComponent = new LWRComponent("ICE LWRComponent");

		// Set its data
		testLWRComponent.setId(12);
		testLWRComponent.setDescription("This is a LWRComponent that will "
				+ "be used for testing equality with other LWRComponents.");

		// Create another LWRComponent to assert Equality with the last
		LWRComponent equalObject = new LWRComponent("ICE LWRComponent");

		// Set its data, equal to testLWRComponent
		equalObject.setId(12);
		equalObject.setDescription("This is a LWRComponent that will "
				+ "be used for testing equality with other LWRComponents.");

		// Create a LWRComponent that is not equal to testLWRComponent
		LWRComponent unEqualObject = new LWRComponent();

		// Set its data, not equal to testLWRComponent
		unEqualObject.setId(52);
		unEqualObject.setName("Bill the LWRComponent");
		unEqualObject
				.setDescription("This is a LWRComponent to verify that "
						+ "LWRComponent.equals() returns false for an object that is not "
						+ "equivalent to testLWRComponent.");

		// Create a third LWRComponent to test Transitivity
		LWRComponent transitiveObject = new LWRComponent("ICE LWRComponent");

		// Set its data, not equal to testLWRComponent
		transitiveObject.setId(12);
		transitiveObject.setDescription("This is a LWRComponent that will "
				+ "be used for testing equality with other LWRComponents.");

		// Assert that these two LWRComponents are equal
		assertTrue(testLWRComponent.equals(equalObject));

		// Assert that two unequal objects returns false
		assertFalse(testLWRComponent.equals(unEqualObject));

		// Check that equals() is Reflexive
		// x.equals(x) = true
		assertTrue(testLWRComponent.equals(testLWRComponent));

		// Check that equals() is Symmetric
		// x.equals(y) = true iff y.equals(x) = true
		assertTrue(testLWRComponent.equals(equalObject)
				&& equalObject.equals(testLWRComponent));

		// Check that equals() is Transitive
		// x.equals(y) = true, y.equals(z) = true => x.equals(z) = true
		if (testLWRComponent.equals(equalObject)
				&& equalObject.equals(transitiveObject)) {
			assertTrue(testLWRComponent.equals(transitiveObject));
		} else {
			fail();
		}

		// Check the Consistent nature of equals()
		assertTrue(testLWRComponent.equals(equalObject)
				&& testLWRComponent.equals(equalObject)
				&& testLWRComponent.equals(equalObject));
		assertTrue(!testLWRComponent.equals(unEqualObject)
				&& !testLWRComponent.equals(unEqualObject)
				&& !testLWRComponent.equals(unEqualObject));

		// Assert checking equality with null value returns false
		assertFalse(testLWRComponent==null);

		// Assert that two equal objects have the same hashcode
		assertTrue(testLWRComponent.equals(equalObject)
				&& testLWRComponent.hashCode() == equalObject.hashCode());

		// Assert that hashcode is consistent
		assertTrue(testLWRComponent.hashCode() == testLWRComponent.hashCode());

		// Assert that hashcodes are different for unequal objects
		assertFalse(testLWRComponent.hashCode() == unEqualObject.hashCode());

	}

	/**
	 * <p>
	 * This operation checks the visitor pattern.
	 * </p>
	 * 
	 */
	public void checkVisitation() {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>
	 * This operation checks the notifications for changes on the LWRComponent.
	 * </p>
	 * 
	 */
	@Test
	public void checkNotifications() {

		// Local declarations
		LWRComponent component = new LWRComponent("Bob");
		LWRData data = new LWRData();
		data.setFeature("Billy");

		// Create the listener
		this.testComponentListener = new TestComponentListener();

		// Register the listener
		component.register(this.testComponentListener);

		// Change the name
		component.setName("Bob");

		// See if the listener was notified
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Change the id
		component.setId(55);

		// See if the listener was notified
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// set the SourceInfo
		component.setSourceInfo("Bob");

		// See if the listener was notified
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// set the timeStep
		component.setTime(5);

		// See if the listener was notified
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Add a feature to the tree
		component.addData(data, 0.0);

		// See if the listener was notified
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

		// Remove data
		component.removeAllDataFromFeature(data.getFeature());

		// See if the listener was notified
		assertTrue(testComponentListener.wasNotified());

		// Reset the listener
		testComponentListener.reset();

	}

	/**
	 * <p>
	 * This operation tests all of the IDataOperations and setSourceInfo(),
	 * addData(), and remove operations that deal with IDataProvider.
	 * </p>
	 * 
	 */
	@Test
	public void checkIDataProvider() {
		// Local Declarations
		LWRComponent component;
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
		component = new LWRComponent(name);

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
		component = new LWRComponent(name);

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
	 * This operation checks the copy and clone routines.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Local declarations
		LWRComponent object;
		LWRComponent copyObject = new LWRComponent(), clonedObject;

		// Values
		String name = "A LWRComponent!@!@#!#@56483";
		String description = "Description !@#!@#!@#!46546484328";
		int id = 68468431;
		int timeStep = 3;
		String sourceInfo = "5465465SOURCEINFO!@#!#!#@!#@";

		// Setup Object to test
		object = new LWRComponent(name);
		object.setTime(timeStep);
		object.setId(id);
		object.setDescription(description);
		object.setSourceInfo(sourceInfo);

		// Run the copy routine
		copyObject = new LWRComponent();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (LWRComponent) object.clone();

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