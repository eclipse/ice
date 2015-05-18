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
package org.eclipse.ice.io.hdf.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;

import java.io.File;
import java.net.URI;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <p>
 * This class tests the HdfWriterFactory class.
 * </p>
 * 
 * @author Eric J. Lingerfelt
 */
public class HdfWriterFactoryTester {
	/**
	 * <p>
	 * This operation conducts any required initialization for the tests.
	 * </p>
	 */
	@BeforeClass
	public static void beforeClass() {}

	/**
	 * <p>
	 * This operation checks the createFloatDatatype, createH5Group, and
	 * createIntegerH5Datatype operations.
	 * </p>
	 */
	@Test
	public void checkCreators() {
		// Local declarations
		String separator = System.getProperty("file.separator");
		String testFileName = "hdfWriterFactoryFile1.h5";
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests";
		File dataFile = new File(userDir + separator + testFileName);
		URI uri = dataFile.toURI();
		H5File h5File;

		// Create a bad H5File
		h5File = HdfFileFactory.createH5File(null);
		assertNull(h5File);

		// Create a good H5File
		h5File = HdfFileFactory.createH5File(uri);
		assertNotNull(h5File);

		// Create a bad Reactor group
		H5Group reactorGroup = HdfWriterFactory.createH5Group(null, null, null);
		assertNull(reactorGroup);

		// Create a bad Reactor group
		reactorGroup = HdfWriterFactory.createH5Group(h5File, null, null);
		assertNull(reactorGroup);

		// Create a bad Reactor group
		reactorGroup = HdfWriterFactory.createH5Group(h5File, "", null);
		assertNull(reactorGroup);

		// Get the root group
		H5Group rootH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();

		// Create a good Reactor group
		reactorGroup = HdfWriterFactory.createH5Group(h5File, "Reactor",
				rootH5Group);
		assertNotNull(reactorGroup);

		try {

			// Get the HObject at the given path
			HObject hObject = h5File.get("/Reactor");

			// Check that its not null and an instance of H5Group
			assertNotNull(hObject);
			assertTrue(hObject instanceof H5Group);

			// Cast the H5Object to an H5Group and check its name
			H5Group tempGroup = (H5Group) hObject;
			assertTrue("Reactor".equals(tempGroup.getName()));

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			// Fail the test
			fail();
		}

		// Create a bad Geometry group under the Reactor Group
		H5Group geometryGroup = HdfWriterFactory.createH5Group(null, "",
				reactorGroup);
		assertNull(geometryGroup);

		// Create a bad Geometry group under the Reactor Group
		geometryGroup = HdfWriterFactory.createH5Group(null, "Geometry",
				reactorGroup);
		assertNull(geometryGroup);

		// Create a good Geometry group under the Reactor Group
		geometryGroup = HdfWriterFactory.createH5Group(h5File, "Geometry",
				reactorGroup);
		assertNotNull(geometryGroup);

		try {

			// Get the HObject at the given path
			HObject hObject = h5File.get("/Reactor/Geometry");

			// Check that its not null and an instance of H5Group
			assertNotNull(hObject);
			assertTrue(hObject instanceof H5Group);

			// Cast the H5Object to an H5Group and check its name
			H5Group tempGroup = (H5Group) hObject;
			assertTrue("Geometry".equals(tempGroup.getName()));

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			// Fail the test
			fail();
		}

		// Create a bad integer datatype
		H5Datatype integerDatatype = HdfWriterFactory
				.createIntegerH5Datatype(null);
		assertNull(integerDatatype);

		// Create a good integer datatype
		integerDatatype = HdfWriterFactory.createIntegerH5Datatype(h5File);
		assertNotNull(integerDatatype);
		assertEquals(integerDatatype.getDatatypeClass(),
				H5Datatype.CLASS_INTEGER);

		// Create a bad float datatype
		H5Datatype floatDatatype = HdfWriterFactory.createFloatH5Datatype(null);
		assertNull(floatDatatype);

		// Create a good float datatype
		floatDatatype = HdfWriterFactory.createFloatH5Datatype(h5File);
		assertNotNull(floatDatatype);
		assertEquals(floatDatatype.getDatatypeClass(), H5Datatype.CLASS_FLOAT);

		// Close the file
		HdfFileFactory.closeH5File(h5File);

		// Delete file if exists
		if (dataFile.exists()) {
			dataFile.delete();
		}
	}

	/**
	 * <p>
	 * This operation checks the writeDoubleAttribute, writeStringAttribute, and
	 * writeIntegerAttribute operations.
	 * </p>
	 */
	@Test
	public void checkWriters() {
		// Local declarations
		String separator = System.getProperty("file.separator");
		String testFileName = "hdfWriterFactoryFile2.h5";
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests";
		File dataFile = new File(userDir + separator + testFileName);
		URI uri = dataFile.toURI();
		H5File h5File;
		boolean flag = true;
		double number = 0.12345678912345;

		// Create a good H5File
		h5File = HdfFileFactory.createH5File(uri);

		H5Group rootH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();

		// Create a good Reactor group
		H5Group reactorGroup = HdfWriterFactory.createH5Group(h5File,
				"Reactor", rootH5Group);
		assertNotNull(reactorGroup);

		// Write some double attributes with bad arguments
		flag = HdfWriterFactory.writeDoubleAttribute(null, null, null, number);
		assertFalse(flag);

		flag = HdfWriterFactory
				.writeDoubleAttribute(h5File, null, null, number);
		assertFalse(flag);

		flag = HdfWriterFactory.writeDoubleAttribute(h5File, reactorGroup,
				null, number);
		assertFalse(flag);

		// Write a double attribute with good arguments
		flag = HdfWriterFactory.writeDoubleAttribute(h5File, reactorGroup,
				"double value", number);
		assertTrue(true);

		try {

			// Check to see if the attribute was actually written
			Attribute attribute = (Attribute) reactorGroup.getMetadata().get(0);
			assertEquals(attribute.getName(), "double value");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(((double[]) attribute.getValue())[0], number, 0.0);

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			// Fail the test
			fail();
		}

		// Write some integer attributes with bad arguments
		flag = HdfWriterFactory.writeIntegerAttribute(null, null, null, 5);
		assertFalse(flag);

		flag = HdfWriterFactory.writeIntegerAttribute(h5File, null, null, 5);
		assertFalse(flag);

		flag = HdfWriterFactory.writeIntegerAttribute(h5File, reactorGroup,
				null, 5);
		assertFalse(flag);

		// Write a integer attribute with good arguments
		flag = HdfWriterFactory.writeIntegerAttribute(h5File, reactorGroup,
				"integer value", 5);
		assertTrue(true);

		try {

			// Check to see if the attribute was actually written
			Attribute attribute = (Attribute) reactorGroup.getMetadata().get(1);
			assertEquals(attribute.getName(), "integer value");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_INTEGER);
			assertEquals(((int[]) attribute.getValue())[0], 5);

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			// Fail the test
			fail();
		}

		// Write some string attributes with bad arguments
		flag = HdfWriterFactory.writeStringAttribute(null, null, null, null);
		assertFalse(flag);

		flag = HdfWriterFactory.writeStringAttribute(h5File, null, null, null);
		assertFalse(flag);

		flag = HdfWriterFactory.writeStringAttribute(h5File, reactorGroup,
				null, null);
		assertFalse(flag);

		flag = HdfWriterFactory.writeStringAttribute(h5File, reactorGroup, "",
				null);
		assertFalse(flag);

		flag = HdfWriterFactory.writeStringAttribute(h5File, reactorGroup, "",
				"");
		assertFalse(flag);

		flag = HdfWriterFactory.writeStringAttribute(h5File, reactorGroup,
				"string value", "");
		assertFalse(flag);

		// Write a string attribute with good arguments
		flag = HdfWriterFactory.writeStringAttribute(h5File, reactorGroup,
				"string value", "USS Defiant");
		assertTrue(flag);

		try {

			// Check to see if the attribute was actually written
			Attribute attribute = (Attribute) reactorGroup.getMetadata().get(2);
			assertEquals(attribute.getName(), "string value");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			String attributeValue = Dataset.byteToString(
					(byte[]) attribute.getValue(),
					((byte[]) attribute.getValue()).length)[0];
			assertEquals(attributeValue, "USS Defiant");

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			// Fail the test
			fail();
		}

		// Delete the file
		if (dataFile.exists()) {
			dataFile.delete();
		}
	}
}