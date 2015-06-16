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

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.pwr.ControlBank;

/**
 * <p>
 * This class tests the ControlBank class.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class ControlBankTester {
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
		// Local Declarations
		ControlBank controlBank;
		// Default Values
		String defaultName = "ControlBank 1";
		String defaultDesc = "Default Control Bank";
		int defaultId = 1;
		double defaultStepSize = 0.0;
		int defaultMaxNumberOfSteps = 1;
		HDF5LWRTagType type = HDF5LWRTagType.CONTROL_BANK;

		// New Values
		String newName = "ControlBank 2";
		double newStepSize = 10.0;
		int newMaxNumberOfSteps = 100;

		// Check nullary constructor
		controlBank = new ControlBank();
		assertEquals(defaultName, controlBank.getName());
		assertEquals(defaultStepSize, controlBank.getStepSize(), 0.0);
		assertEquals(defaultMaxNumberOfSteps, controlBank.getMaxNumberOfSteps());
		assertEquals(defaultId, controlBank.getId());
		assertEquals(defaultDesc, controlBank.getDescription());
		assertEquals(type, controlBank.getHDF5LWRTag());

		// Check non-nullary constructor
		controlBank = new ControlBank(newName, newStepSize, newMaxNumberOfSteps);
		assertEquals(newName, controlBank.getName());
		assertEquals(newStepSize, controlBank.getStepSize(), 0.0);
		assertEquals(newMaxNumberOfSteps, controlBank.getMaxNumberOfSteps());
		assertEquals(type, controlBank.getHDF5LWRTag());

		// Check non-nullary constructor for illegal values
		controlBank = new ControlBank(null, -1.0, -1);
		assertEquals(defaultName, controlBank.getName());
		assertEquals(defaultStepSize, controlBank.getStepSize(), 0.0);
		assertEquals(defaultMaxNumberOfSteps, controlBank.getMaxNumberOfSteps());
		assertEquals(type, controlBank.getHDF5LWRTag());

		// Check non-nullary constructor for 0 value of MaxNumberOfSteps
		controlBank = new ControlBank(newName, newStepSize, 0);
		assertEquals(newName, controlBank.getName());
		assertEquals(newStepSize, controlBank.getStepSize(), 0.0);
		assertEquals(defaultMaxNumberOfSteps, controlBank.getMaxNumberOfSteps());
		assertEquals(type, controlBank.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the stepSize.
	 * </p>
	 * 
	 */
	@Test
	public void checkStepSize() {
		// Local Declarations
		ControlBank controlBank = new ControlBank();

		// Set the size to 0
		controlBank.setStepSize(0.0);

		// This checks a valid return after the step size is set
		assertEquals(0.0, controlBank.getStepSize(), 0.0);

		// Pass an invalid number: negative
		controlBank.setStepSize(-1.0);

		// Should not change the previous value.
		assertEquals(0.0, controlBank.getStepSize(), 0.0);

		// Test a valid length
		controlBank.setStepSize(10.0);

		// Should set correct value.
		assertEquals(10.0, controlBank.getStepSize(), 0.0);

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the maxNumberOfSteps
	 * </p>
	 * 
	 */
	@Test
	public void checkMaxNumberOfSteps() {
		// Local Declarations
		ControlBank controlBank = new ControlBank();

		// Set the Max Step Size to default
		controlBank.setMaxNumberOfSteps(1);

		// This checks a valid return after the Max step size is set
		assertEquals(1, controlBank.getMaxNumberOfSteps());

		// Pass an invalid number: negative
		controlBank.setMaxNumberOfSteps(-1);

		// Should not change the previous value.
		assertEquals(1, controlBank.getMaxNumberOfSteps());

		// Test a valid length
		controlBank.setMaxNumberOfSteps(10);

		// Should set correct value.
		assertEquals(10, controlBank.getMaxNumberOfSteps());

	}

	/**
	 * <p>
	 * This operation checks the getStrokeLength method.
	 * </p>
	 * 
	 */
	@Test
	public void checkStrokeLength() {

		// Local Declarations
		ControlBank controlBank = new ControlBank();

		// Checking default values.
		assertEquals(1, controlBank.getMaxNumberOfSteps());
		assertEquals(0.0, controlBank.getStepSize(), 0.0);

		// Check valid test
		assertEquals(0.0, controlBank.getStrokeLength(), 0.0);

		// Check nonzero
		controlBank.setStepSize(100.0);

		// Check valid test
		assertEquals(100.0, controlBank.getStrokeLength(), 0.0);

		// Check valid test - decimal
		controlBank.setMaxNumberOfSteps(2);
		controlBank.setStepSize(50.05);

		// Check valid test
		assertEquals(100.1, controlBank.getStrokeLength(), 0.0);

	}

	/**
	 * <p>
	 * This operation checks the equals and hashCode operations.
	 * </p>
	 * 
	 */
	public void checkEquality() {

		// Local Declarations
		ControlBank object, equalObject, unEqualObject, transitiveObject;
		String name = "CONTROL!";
		double stepSize = 5;
		int maxNumberOfSteps = 10;
		int unEqualMaxNumberOfSteps = 9;

		// Setup root object
		object = new ControlBank(name, stepSize, maxNumberOfSteps);

		// Setup equalObject equal to object
		equalObject = new ControlBank(name, stepSize, maxNumberOfSteps);

		// Setup transitiveObject equal to object
		transitiveObject = new ControlBank(name, stepSize, maxNumberOfSteps);

		// Set its data, not equal to object
		// Does not contain components!
		unEqualObject = new ControlBank(name, stepSize, unEqualMaxNumberOfSteps);

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
	public void checkCopying() {

		// Local Declarations
		ControlBank object, copyObject, clonedObject;
		String name = "CONTROL!";
		double stepSize = 5;
		int maxNumberOfSteps = 10;

		// Setup root object
		object = new ControlBank(name, stepSize, maxNumberOfSteps);

		// Run the copy routine
		copyObject = new ControlBank();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (ControlBank) object.clone();

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
		ControlBank bank = new ControlBank();
		String name = "Control";
		String description = "A controlling controller.";
		int id = 4;
		HDF5LWRTagType tag = bank.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		double stepSize = 10;
		int maxNumberOfSteps = 20;

		// Setup bank
		bank.setName(name);
		bank.setId(id);
		bank.setDescription(description);
		bank.setMaxNumberOfSteps(maxNumberOfSteps);
		bank.setStepSize(stepSize);

		// Setup the HDF5 File
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ "test.h5");
		URI uri = dataFile.toURI();
		H5File h5File = HdfFileFactory.createH5File(uri);
		try {
			h5File.open();
		} catch (Exception e1) {
			// Fail here
			e1.printStackTrace();
			fail();
		}

		// Check to see if it has any children
		assertNull(bank.getWriteableChildren());

		// Check writing attributes
		H5Group h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		// Pass the group and file to the writer for attributes
		// See that it passes
		assertTrue(bank.writeAttributes(h5File, h5Group));

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
			// Show that there are no other groups made at this time
			assertEquals(0, h5Group.getMemberList().size());

			// Check the meta data
			assertEquals(6, h5Group.getMetadata().size());

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
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check Integer Attribute - maxNumberOfSteps
			attribute = (Attribute) h5Group.getMetadata().get(3);
			assertEquals(attribute.getName(), "maxNumberOfSteps");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_INTEGER);
			assertEquals(maxNumberOfSteps, ((int[]) attribute.getValue())[0]);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check String Attribute - name
			attribute = (Attribute) h5Group.getMetadata().get(4);
			assertEquals(attribute.getName(), "name");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			attributeValue = ((String[]) attribute.getValue())[0];
			assertEquals(name, attributeValue);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check Double Attribute - stepSize
			attribute = (Attribute) h5Group.getMetadata().get(5);
			assertEquals(attribute.getName(), "stepSize");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(stepSize, ((double[]) attribute.getValue())[0], 1.2);
			// Reset Values
			attribute = null;
			attributeValue = null;

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Make sure the writeAttributes fail for invalid stuff
		assertFalse(bank.writeAttributes(null, h5Group));
		assertFalse(bank.writeAttributes(h5File, null));

		// Check dataSet.
		assertFalse(bank.writeDatasets(null, null));

		// Check Group Creation
		H5Group group = bank.createGroup(h5File, h5Group);
		// See that the previous group has a group
		assertEquals(1, h5Group.getMemberList().size());
		// Check that it has the same name as the root bank
		assertEquals(bank.getName(), h5Group.getMemberList().get(0).toString());
		// Check that the returned group is a Group but no members
		assertEquals(0, group.getMemberList().size());
		assertEquals(0, ((Group) h5Group.getMemberList().get(0))
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
	 * This operation checks the HDF5 readable operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkHDF5Readables() {

		// Local Declarations
		ControlBank component = new ControlBank();
		ControlBank newComponent = new ControlBank();
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		H5Group subGroup = null;
		double stepSize = 3.0;
		int maxNumberOfSteps = 15;
		String testFileName = "testWrite.h5";

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.setMaxNumberOfSteps(maxNumberOfSteps);
		component.setStepSize(stepSize);

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

		// Check Readable Children

		// Setup PWRAssembly with Data in the Group

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

			// Setup stepSize attribute
			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup, "stepSize",
					stepSize);

			// Setup maxNumberOfSteps attribute
			HdfWriterFactory.writeIntegerAttribute(h5File, subGroup,
					"maxNumberOfSteps", maxNumberOfSteps);

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
}