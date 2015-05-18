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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Vector;

import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.datastructures.test.TestComponentListener;

import static org.junit.Assert.*;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
	 * This operation checks the HDF5 writing operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkHDF5Writeables() {

		// Local Declarations
		LWRComponent component = new LWRComponent();
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		String sourceInfo = "ASDASDASD";
		String timeUnits = "UNITS OF AWESOME";
		String testFileName = "testWrite.h5";
		double time = 1.0;

		// Setup LWRData
		String feature1 = "Feature 1";
		String feature2 = "Feature 2";
		double time1 = 1.0, time2 = 3.0, time3 = 3.5;
		LWRData data1, data2, data3, data4, data5;
		ArrayList<Double> position1 = new ArrayList<Double>(), position2 = new ArrayList<Double>(), position3 = new ArrayList<Double>(), position4 = new ArrayList<Double>(), position5 = new ArrayList<Double>();

		// Setup Positions

		// Setup Position 1
		position1.add(0.0);
		position1.add(1.0);
		position1.add(0.0);

		// Setup Position 2
		position2.add(0.0);
		position2.add(1.0);
		position2.add(4.0);

		// Setup Position 3
		position3.add(1.0);
		position3.add(1.0);
		position3.add(0.0);

		// Setup Position 4
		position4.add(0.0);
		position4.add(1.0);
		position4.add(1.0);

		// Setup Position 5
		position4.add(0.0);
		position4.add(1.0);
		position4.add(3.0);

		// Setup data1
		data1 = new LWRData(feature1);
		data1.setPosition(position1);
		data1.setValue(1.0);
		data1.setUncertainty(1.5);
		data1.setUnits("Units " + 1);

		// Setup data2
		data2 = new LWRData(feature1);
		data2.setPosition(position2);
		data2.setValue(2.0);
		data2.setUncertainty(2.5);
		data2.setUnits("Units " + 2);

		// Setup data3
		data3 = new LWRData(feature1);
		data3.setPosition(position3);
		data3.setValue(3.0);
		data3.setUncertainty(3.5);
		data3.setUnits("Units " + 3);

		// Setup data4
		data4 = new LWRData(feature1);
		data4.setPosition(position4);
		data4.setValue(4.0);
		data4.setUncertainty(4.5);
		data4.setUnits("Units " + 4);

		// Setup data5
		data5 = new LWRData(feature2);
		data5.setPosition(position5);
		data5.setValue(5.0);
		data5.setUncertainty(5.5);
		data5.setUnits("Units " + 5);

		// Add data to the component
		component.addData(data1, time1);
		component.addData(data2, time1);
		component.addData(data3, time2);
		component.addData(data4, time3);
		component.addData(data5, time3);

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);

		// Setup the HDF5 File
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ testFileName);
		URI uri = dataFile.toURI();
		H5File h5File = HdfFileFactory.createH5File(uri);
		try {
			h5File.open();
		} catch (Exception e1) {
			e1.printStackTrace();
			fail();
		}

		// Check to see if it has any children
		assertNull(component.getWriteableChildren());

		// Check writing attributes
		H5Group h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		// Pass the group and file to the writer for attributes
		// See that it passes
		assertTrue(component.writeAttributes(h5File, h5Group));

		// Check dataSet. Pass null to show it return false
		assertFalse(component.writeDatasets(null, null));
		assertFalse(component.writeDatasets(null, h5Group));
		assertFalse(component.writeDatasets(h5File, null));

		// Check dataset
		assertTrue(component.writeDatasets(h5File, h5Group));

		// Close group and then reopen
		try {
			h5File.close();
			h5File.open();
		} catch (Exception e1) {
			e1.printStackTrace();
			dataFile.delete();
			fail();
		}

		// Get the group again
		h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();

		// Check attributes
		assertEquals("/", h5Group.getName());

		try {
			// Show that there is one group made at this time
			assertEquals(1, h5Group.getMemberList().size());

			// Check the name of the group
			assertEquals("State Point Data", h5Group.getMemberList().get(0)
					.getName());

			// Check the meta data
			assertEquals(4, h5Group.getMetadata().size());

			// Check String attribute - HDF5LWRTag
			attribute = (Attribute) h5Group.getMetadata().get(0);
			assertEquals(attribute.getName(), "HDF5LWRTag");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			attributeValue = ((String[]) attribute.getValue())[0];
			assertEquals(tag.toString(), attributeValue);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check String Attribute - description
			attribute = (Attribute) h5Group.getMetadata().get(1);
			assertEquals(attribute.getName(), "description");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			attributeValue = ((String[]) attribute.getValue())[0];
			assertEquals(description, attributeValue);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check Integer Attribute - id
			attribute = (Attribute) h5Group.getMetadata().get(2);
			assertEquals(attribute.getName(), "id");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_INTEGER);
			assertEquals(id, ((int[]) attribute.getValue())[0]);

			// Check String Attribute - name
			attribute = (Attribute) h5Group.getMetadata().get(3);
			assertEquals(attribute.getName(), "name");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			attributeValue = ((String[]) attribute.getValue())[0];
			assertEquals(name, attributeValue);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check the Group names and contents
			assertEquals("State Point Data", h5Group.getMemberList().get(0)
					.getName());

			// Get the IDataGroup
			H5Group dataGroup = (H5Group) h5Group.getMemberList().get(0);

			// Check the size of the data groups
			assertEquals(3, dataGroup.getMemberList().size());

			// Check the names of the groups. These should reflect the number of
			// time steps
			assertEquals("TimeStep: 0", dataGroup.getMemberList().get(0)
					.getName());
			assertEquals("TimeStep: 1", dataGroup.getMemberList().get(1)
					.getName());
			assertEquals("TimeStep: 2", dataGroup.getMemberList().get(2)
					.getName());

			// Get the TimeStep Groups and check contents
			H5Group timeGroup1 = (H5Group) dataGroup.getMemberList().get(0);
			H5Group timeGroup2 = (H5Group) dataGroup.getMemberList().get(1);
			H5Group timeGroup3 = (H5Group) dataGroup.getMemberList().get(2);

			// Check that there is a group and attributes
			assertEquals(1, timeGroup1.getMemberList().size());
			assertEquals(1, timeGroup2.getMemberList().size());
			assertEquals(2, timeGroup3.getMemberList().size());
			assertEquals(2, timeGroup1.getMetadata().size());
			assertEquals(2, timeGroup2.getMetadata().size());
			assertEquals(2, timeGroup3.getMetadata().size());

			// Check Attributes on timeGroups

			// Check timeGroup1
			// Check time attribute
			attribute = (Attribute) timeGroup1.getMetadata().get(0);
			assertEquals(attribute.getName(), "time");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(time1, ((double[]) attribute.getValue())[0], 0.0);
			// Check string attribute
			attribute = (Attribute) timeGroup1.getMetadata().get(1);
			assertEquals(attribute.getName(), "units");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			assertEquals(component.getTimeUnits(),
					((String[]) attribute.getValue())[0]);

			// Check timeGroup2
			// Check time attribute
			attribute = (Attribute) timeGroup2.getMetadata().get(0);
			assertEquals(attribute.getName(), "time");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(time2, ((double[]) attribute.getValue())[0], 0.0);
			// Check string attribute
			attribute = (Attribute) timeGroup1.getMetadata().get(1);
			assertEquals(attribute.getName(), "units");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			assertEquals(component.getTimeUnits(),
					((String[]) attribute.getValue())[0]);

			// Check timeGroup3
			// Check time attribute
			attribute = (Attribute) timeGroup3.getMetadata().get(0);
			assertEquals(attribute.getName(), "time");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(time3, ((double[]) attribute.getValue())[0], 0.0);
			// Check string attribute
			attribute = (Attribute) timeGroup1.getMetadata().get(1);
			assertEquals(attribute.getName(), "units");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			assertEquals(component.getTimeUnits(),
					((String[]) attribute.getValue())[0]);

			// Get the Features off the timeGroups
			H5CompoundDS featureGroup1 = (H5CompoundDS) timeGroup1
					.getMemberList().get(0);
			H5CompoundDS featureGroup2 = (H5CompoundDS) timeGroup2
					.getMemberList().get(0);

			// Feature Group 3 gets two SEPARATE compound datasets because of
			// different features within the same time
			H5CompoundDS featureGroup3a = (H5CompoundDS) timeGroup3
					.getMemberList().get(0);
			H5CompoundDS featureGroup3b = (H5CompoundDS) timeGroup3
					.getMemberList().get(1);

			// Check information on featureGroups
			assertEquals("Feature 1", featureGroup1.getName());
			assertEquals("Feature 1", featureGroup2.getName());
			assertEquals("Feature 1", featureGroup3a.getName());
			assertEquals("Feature 2", featureGroup3b.getName());

			// Check contents of the feature groups

			// Check the names of the grid
			// Number of members - Array Attributes stored on this object
			featureGroup2.init();
			assertEquals(4, featureGroup2.getMemberCount());
			assertEquals("value", featureGroup2.getMemberNames()[0]);
			assertEquals("uncertainty", featureGroup2.getMemberNames()[1]);
			assertEquals("units", featureGroup2.getMemberNames()[2]);
			assertEquals("position", featureGroup2.getMemberNames()[3]);

			// Check contents of featureGroup1
			featureGroup1.init();
			component.setTime(time1);

			// There are two sets of features here with the same feature name,
			// so there is only 1 "height"
			assertEquals(1, featureGroup1.getHeight());
			Object data = featureGroup1.getData();

			// Convert the data to a readable format: Cast it as a Vector, then
			// grab the pieces out of it
			Vector<Object> objects = (Vector<Object>) data;
			assertEquals(4, objects.size());

			// Cast the objects to their subsets
			// Create the arrays for each dataSet
			double[] value = (double[]) objects.get(0);
			double[] uncertainty = (double[]) objects.get(1);
			String[] units = (String[]) objects.get(2);
			double[] pos = (double[]) objects.get(3);
			double[] position;

			// Show that there is a correct length on these pieces
			assertEquals(2, value.length);
			assertEquals(2, uncertainty.length);
			assertEquals(2, units.length);
			assertEquals(2 * 3, pos.length);

			// Check values - data1
			assertEquals(data1.getValue(), value[0], 0.0);
			assertEquals(data1.getUncertainty(), uncertainty[0], 0.0);
			assertEquals(data1.getUnits(), units[0]);
			position = new double[3];
			position[0] = pos[0];
			position[1] = pos[1];
			position[2] = pos[2];
			assertEquals(data1.getPosition().toArray()[0], position[0]);
			assertEquals(data1.getPosition().toArray()[1], position[1]);
			assertEquals(data1.getPosition().toArray()[2], position[2]);

			// Check values - data2
			assertEquals(data2.getValue(), value[1], 0.0);
			assertEquals(data2.getUncertainty(), uncertainty[1], 0.0);
			assertEquals(data2.getUnits(), units[1]);
			position = new double[3];
			position[0] = pos[3];
			position[1] = pos[4];
			position[2] = pos[5];
			assertEquals(data2.getPosition().toArray()[0], position[0]);
			assertEquals(data2.getPosition().toArray()[1], position[1]);
			assertEquals(data2.getPosition().toArray()[2], position[2]);

			// Check contents of featureGroup2
			featureGroup2.init();
			component.setTime(time1);

			assertEquals(1, featureGroup2.getHeight());
			data = featureGroup2.getData();

			// Convert the data to a readable format: Cast it as a Vector, then
			// grab the pieces out of it
			objects = (Vector<Object>) data;
			assertEquals(4, objects.size());

			// Cast the objects to their subsets
			// Create the arrays for each dataSet
			value = (double[]) objects.get(0);
			uncertainty = (double[]) objects.get(1);
			units = (String[]) objects.get(2);
			pos = (double[]) objects.get(3);

			// Show that there is a correct length on these pieces
			assertEquals(1, value.length);
			assertEquals(1, uncertainty.length);
			assertEquals(1, units.length);
			assertEquals(1 * 3, pos.length);

			// Check values - data3
			assertEquals(data3.getValue(), value[0], 0.0);
			assertEquals(data3.getUncertainty(), uncertainty[0], 0.0);
			assertEquals(data3.getUnits(), units[0]);
			position = new double[3];
			position[0] = pos[0];
			position[1] = pos[1];
			position[2] = pos[2];
			assertEquals(data3.getPosition().toArray()[0], position[0]);
			assertEquals(data3.getPosition().toArray()[1], position[1]);
			assertEquals(data3.getPosition().toArray()[2], position[2]);

			// Check over contents of featureGroup3a
			featureGroup3a.init();
			component.setTime(time1);

			assertEquals(1, featureGroup3a.getHeight());
			data = featureGroup3a.getData();

			// Convert the data to a readable format: Cast it as a Vector, then
			// grab the pieces out of it
			objects = (Vector<Object>) data;
			assertEquals(4, objects.size());

			// Cast the objects to their subsets
			// Create the arrays for each dataSet
			value = (double[]) objects.get(0);
			uncertainty = (double[]) objects.get(1);
			units = (String[]) objects.get(2);
			pos = (double[]) objects.get(3);

			// Show that there is a correct length on these pieces
			assertEquals(1, value.length);
			assertEquals(1, uncertainty.length);
			assertEquals(1, units.length);
			assertEquals(1 * 3, pos.length);

			// Check values - data4
			assertEquals(data4.getValue(), value[0], 0.0);
			assertEquals(data4.getUncertainty(), uncertainty[0], 0.0);
			assertEquals(data4.getUnits(), units[0]);
			position = new double[3];
			position[0] = pos[0];
			position[1] = pos[1];
			position[2] = pos[2];
			assertEquals(data4.getPosition().toArray()[0], position[0]);
			assertEquals(data4.getPosition().toArray()[1], position[1]);
			assertEquals(data4.getPosition().toArray()[2], position[2]);

			// Check over contents of featureGroup3b
			featureGroup3b.init();
			component.setTime(time1);

			assertEquals(1, featureGroup3b.getHeight());
			data = featureGroup3b.getData();

			// Convert the data to a readable format: Cast it as a Vector, then
			// grab the pieces out of it
			objects = (Vector<Object>) data;
			assertEquals(4, objects.size());

			// Cast the objects to their subsets
			// Create the arrays for each dataSet
			value = (double[]) objects.get(0);
			uncertainty = (double[]) objects.get(1);
			units = (String[]) objects.get(2);
			pos = (double[]) objects.get(3);

			// Show that there is a correct length on these pieces
			assertEquals(1, value.length);
			assertEquals(1, uncertainty.length);
			assertEquals(1, units.length);
			assertEquals(1 * 3, pos.length);

			// Check values - data5
			assertEquals(data5.getValue(), value[0], 0.0);
			assertEquals(data5.getUncertainty(), uncertainty[0], 0.0);
			assertEquals(data5.getUnits(), units[0]);
			position = new double[3];
			position[0] = pos[0];
			position[1] = pos[1];
			position[2] = pos[2];
			assertEquals(data4.getPosition().toArray()[0], position[0]);
			assertEquals(data4.getPosition().toArray()[1], position[1]);
			assertEquals(data4.getPosition().toArray()[2], position[2]);

		} catch (Exception e) {
			e.printStackTrace();
			fail();

		}

		// Make sure the writeAttributes fail for invalid stuff
		assertFalse(component.writeAttributes(null, h5Group));
		assertFalse(component.writeAttributes(h5File, null));

		// Check Group Creation
		H5Group group = component.createGroup(h5File, h5Group);
		// See that the previous group has a group
		assertEquals(2, h5Group.getMemberList().size());
		// Check that it has the same name as the root component
		assertEquals(component.getName(), h5Group.getMemberList().get(1)
				.toString());
		// Check that the returned group is a Group but no members
		assertEquals(0, group.getMemberList().size());
		assertEquals(0, ((Group) h5Group.getMemberList().get(1))
				.getMemberList().size());

		// Close that h5 file!
		try {
			h5File.close();
		} catch (Exception e1) {
			e1.printStackTrace();
			dataFile.delete();
			fail();
		}

		// Delete the file once you are done
		dataFile.delete();

	}

	/**
	 * <p>
	 * This operation checks the HDF5 readable operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkHDF5Readables() {

		// Local Declarations
		LWRComponent component = new LWRComponent();
		LWRComponent newComponent = new LWRComponent("NOT A DEFAULT ONE");
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		H5Group subGroup = null;
		String sourceInfo = "ASDASDASD";
		String timeUnits = "UNITS OF AWESOME";
		double time = 1.0;

		// Setup LWRData
		String feature1 = "Feature 1";
		String feature2 = "Feature 2";
		double time1 = 1.0, time2 = 3.0, time3 = 3.5;
		LWRData data1, data2, data3, data4, data5;
		ArrayList<Double> position1 = new ArrayList<Double>(), position2 = new ArrayList<Double>(), position3 = new ArrayList<Double>(), position4 = new ArrayList<Double>(), position5 = new ArrayList<Double>();

		// Setup Positions

		// Setup Position 1
		position1.add(0.0);
		position1.add(1.0);
		position1.add(0.0);

		// Setup Position 2
		position2.add(0.0);
		position2.add(1.0);
		position2.add(4.0);

		// Setup Position 3
		position3.add(1.0);
		position3.add(1.0);
		position3.add(0.0);

		// Setup Position 4
		position4.add(0.0);
		position4.add(1.0);
		position4.add(1.0);

		// Setup Position 5
		position4.add(0.0);
		position4.add(1.0);
		position4.add(3.0);

		// Setup data1
		data1 = new LWRData(feature1);
		data1.setPosition(position1);
		data1.setValue(1.0);
		data1.setUncertainty(1.5);
		data1.setUnits("Units " + 1);

		// Setup data2
		data2 = new LWRData(feature1);
		data2.setPosition(position2);
		data2.setValue(2.0);
		data2.setUncertainty(2.5);
		data2.setUnits("Units " + 2);

		// Setup data3
		data3 = new LWRData(feature1);
		data3.setPosition(position3);
		data3.setValue(3.0);
		data3.setUncertainty(3.5);
		data3.setUnits("Units " + 3);

		// Setup data4
		data4 = new LWRData(feature1);
		data4.setPosition(position4);
		data4.setValue(4.0);
		data4.setUncertainty(4.5);
		data4.setUnits("Units " + 4);

		// Setup data5
		data5 = new LWRData(feature2);
		data5.setPosition(position5);
		data5.setValue(5.0);
		data5.setUncertainty(5.5);
		data5.setUnits("Units " + 5);

		// Add data to the component
		component.addData(data1, time1);
		component.addData(data2, time1);
		component.addData(data3, time2);
		component.addData(data4, time3);
		component.addData(data5, time3);

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);

		// Do readChild checks here. For LWRComponent this method always returns
		// true
		assertTrue(component.readChild(null));
		assertTrue(component.readChild(newComponent));

		// Setup the HDF5 File
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ "test.h5");
		URI uri = dataFile.toURI();
		H5File h5File = HdfFileFactory.createH5File(uri);
		try {
			h5File.open();
		} catch (Exception e1) {
			e1.printStackTrace();
			fail();
		}

		// Setup LWRComponent with Data in the Group

		H5Group parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		try {
			// Setup the subGroup
			subGroup = (H5Group) h5File.createGroup(name, parentH5Group);

			// Setup the subGroup's attributes

			// Setup Tag Attribute
			HdfWriterFactory.writeStringAttribute(h5File, subGroup,
					"HDF5LWRTag", tag.toString());

			// Setup name attribute
			HdfWriterFactory.writeStringAttribute(h5File, subGroup, "name",
					name);

			// Setup id attribute
			HdfWriterFactory.writeIntegerAttribute(h5File, subGroup, "id", id);

			// Setup description attribute
			HdfWriterFactory.writeStringAttribute(h5File, subGroup,
					"description", description);

			// We will cheat a bit with this method, because otherwise its all
			// copy paste
			assertTrue(component.writeDatasets(h5File, subGroup));

			// Close group and then reopen
			h5File.close();
			h5File.open();
			parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			// Get the subGroup
			subGroup = (H5Group) parentH5Group.getMemberList().get(0);

			// Read information
			assertTrue(newComponent.readAttributes(subGroup));
			assertFalse(newComponent.readDatasets(null));
			assertTrue(newComponent.readDatasets(subGroup));

			// Check with setup component
			assertTrue(component.equals(newComponent));

			// Now, lets try to set an erroneous H5Group with missing data
			subGroup.getMetadata().remove(1);

			// Run it through
			assertFalse(newComponent.readAttributes(subGroup));
			// Check it does not change
			assertTrue(component.equals(newComponent));

			// Check for nullaries
			assertFalse(newComponent.readAttributes(null));
			// Doesn't change anything
			assertTrue(component.equals(newComponent));

			// Close the h5 file!
			h5File.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		dataFile.delete();

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