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
import org.eclipse.ice.io.hdf.HdfReaderFactory;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;
import ncsa.hdf.object.h5.H5ScalarDS;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests the HdfReaderFactory class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author els
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class HdfReaderFactoryTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation conducts any required initialization for the tests.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void beforeClass() {
		// begin-user-code

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getChildH5Group, getChildH5Groups, and
	 * getDataset operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGetters() {
		// begin-user-code

		// Local declarations
		String separator = System.getProperty("file.separator");
		String testFileName = "hdfReaderFactoryFile1.h5";
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ "data" + separator + testFileName);
		URI uri = dataFile.toURI();
		H5File h5File;
		String subGroupName = "Bob";
		H5Group subH5Group = null;
		ArrayList<String> rows = new ArrayList<String>();
		int maxRowSize = 0;
		String rowName = "Rows";

		// Setup ArrayList of Rows
		rows.add("ABBA");
		rows.add("BABBA");
		rows.add("CABBA");
		rows.add("DabbbyDabbyDooo!");
		rows.add("E");

		// Setup the string array
		String[] rowArray = new String[rows.size()];

		// Figure out the biggest row size and add to array
		for (int i = 0; i < rows.size(); i++) {
			maxRowSize = Math.max(maxRowSize, rows.get(i).length());
			// Add to array
			rowArray[i] = rows.get(i);
		}

		// Create a good H5File
		h5File = HdfFileFactory.createH5File(uri);
		assertNotNull(h5File);

		// Open file and setup H5 File to have a parent and child. Check get
		// Readers and get Datasets
		try {
			h5File.open();
			// Setup the H5 File to have a Parent and child
			// Get the parent group
			H5Group parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			// Test to see if a getChild returns null - group does not exist
			assertNull(HdfReaderFactory.getChildH5Group(parentH5Group,
					subGroupName));

			// Null Checks
			assertNull(HdfReaderFactory.getChildH5Group(null, subGroupName));
			assertNull(HdfReaderFactory.getChildH5Group(parentH5Group, null));
			assertNull(HdfReaderFactory.getChildH5Group(null, null));

			// Add a group to the root group
			h5File.createGroup(subGroupName, parentH5Group);

			// Persist data
			h5File.close();
			h5File.open();

			// Get the parent
			parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			// Check to see it exists with the group grabber
			subH5Group = HdfReaderFactory.getChildH5Group(parentH5Group,
					subGroupName);

			// Check the subH5Groups name to make sure that it does exist and
			// has the correct name
			assertNotNull(subH5Group);
			assertTrue(subGroupName.equals(subH5Group.getName()));

			// Check getDatasets

			// Check invalid usage of getting dataset that does not exist
			assertNull(HdfReaderFactory.getDataset(subH5Group, rowName));

			// Nullary checks for dataSet
			assertNull(HdfReaderFactory.getDataset(null, rowName));
			assertNull(HdfReaderFactory.getDataset(subH5Group, null));
			assertNull(HdfReaderFactory.getDataset(null, null));

			// Add a Dataset to the subH5Group

			// Create datatypes for the row and column labels
			H5Datatype rowH5Datatype = (H5Datatype) h5File.createDatatype(
					Datatype.CLASS_STRING, maxRowSize, Datatype.NATIVE,
					Datatype.NATIVE);
			long[] dims = { rows.size() };

			// Add array to dataset and add dataset ot the subgroup
			Dataset h5RowDataset = h5File.createScalarDS(rowName, subH5Group,
					rowH5Datatype, dims, null, null, 0, null);
			h5RowDataset.write(rowArray);

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
			parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			subH5Group = (H5Group) parentH5Group.getMemberList().get(0);

			// Now, check to see if you can get it!
			Dataset data = HdfReaderFactory.getDataset(subH5Group, rowName);
			// Make sure it is not null
			assertNotNull(data);

			// Check information on dataset
			assertEquals(1, subH5Group.getMemberList().size());
			// Check the value of that dataset
			assertEquals(rowName, subH5Group.getMemberList().get(0).getName());
			// Check labels that they are not a group
			assertFalse(subH5Group.getMemberList().get(0) instanceof Group);

			// Check DataSet information

			// Check the rowData
			Object rowData = data.getData();

			// Iterate over the list and check the arrayLists
			for (int i = 0; i < rows.size(); i++) {
				// Check contents of rowData at X
				assertEquals(rows.get(i), ((String[]) rowData)[i]);
			}

			// Check the getter for index
			// Check nullaries
			assertNull(HdfReaderFactory.getChildH5Group(null, 0));
			assertNull(HdfReaderFactory.getChildH5Group(parentH5Group, -1));
			assertNull(HdfReaderFactory.getChildH5Group(null, -1));
			// Check invalid index - out of bounds
			assertNull(HdfReaderFactory.getChildH5Group(parentH5Group, 1));

			// Check valid usage
			subH5Group = HdfReaderFactory.getChildH5Group(parentH5Group, 0);

			// Check the subH5Groups name to make sure that it does exist and
			// has the correct name
			assertNotNull(subH5Group);
			assertEquals(subGroupName, subH5Group.getName());

			// Try to grab the group as a dataset - should throw a class cast
			// exception
			assertNull(HdfReaderFactory.getDataset(parentH5Group, subGroupName));

			// Try to grab the dataset as a group - should throw a class cast
			// exception
			assertNull(HdfReaderFactory.getChildH5Group(subH5Group, rowName));
			assertNull(HdfReaderFactory.getChildH5Group(subH5Group, 0));

		} catch (Exception e) {
			// Delete the dataFile
			dataFile.delete();
			e.printStackTrace();
			fail();
		}

		// Delete file
		dataFile.delete();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the readDoubleAttribute, readStringAttribute, and
	 * readIntegerAttribute operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkReaders() {
		// begin-user-code

		// Local declarations
		String separator = System.getProperty("file.separator");
		String testFileName = "hdfReaderFactoryFile2.h5";
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ "data" + separator + testFileName);
		URI uri = dataFile.toURI();
		H5File h5File = null;
		String integerName = "Integer1";
		String doubleName = "Double1";
		String stringName = "String1";
		int intValue = 3;
		double doubleValue = 5.67656565343;
		String stringValue = "Bobby Lee Jones";
		String childGroupName = "new Group";

		// Create a good H5File
		h5File = HdfFileFactory.createH5File(uri);
		assertNotNull(h5File);

		// Open file and setup H5 File to have a parent and child. Check get
		// Readers and get Datasets
		try {
			h5File.open();
			// Setup the H5 File to have a Parent and child
			// Get the parent group
			H5Group parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			// Check nullary reader operations

			// Check nullary and invalid values for Integer read
			assertNull(HdfReaderFactory.readIntegerAttribute(parentH5Group,
					integerName));
			assertNull(HdfReaderFactory.readIntegerAttribute(null, integerName));
			assertNull(HdfReaderFactory.readIntegerAttribute(parentH5Group,
					null));
			assertNull(HdfReaderFactory.readIntegerAttribute(null, null));

			// Check nullary and invalid values for Double read
			assertNull(HdfReaderFactory.readDoubleAttribute(parentH5Group,
					doubleName));
			assertNull(HdfReaderFactory.readDoubleAttribute(null, doubleName));
			assertNull(HdfReaderFactory
					.readDoubleAttribute(parentH5Group, null));
			assertNull(HdfReaderFactory.readDoubleAttribute(null, null));

			// Check nullary and invalid values for String read
			assertNull(HdfReaderFactory.readStringAttribute(parentH5Group,
					stringName));
			assertNull(HdfReaderFactory.readStringAttribute(null, stringName));
			assertNull(HdfReaderFactory
					.readStringAttribute(parentH5Group, null));
			assertNull(HdfReaderFactory.readStringAttribute(null, null));

			// Add a Integer, a Double, and a String to the parentGroup

			// Setup dataTypes for Integer, Double, and a String

			// Integer Datatype
			// Create the integer datatype
			H5Datatype intDatatype = (H5Datatype) h5File.createDatatype(
					Datatype.CLASS_INTEGER, Datatype.NATIVE, Datatype.NATIVE,
					Datatype.NATIVE);

			// Double (float) Datatype
			// Create the float datatype
			H5Datatype doubleDatatype = (H5Datatype) h5File.createDatatype(
					Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, Datatype.NATIVE);

			// String Datatype
			// Create a custom String data type for the value
			H5Datatype stringDatatype = (H5Datatype) h5File.createDatatype(
					Datatype.CLASS_STRING, stringValue.length(),
					Datatype.NATIVE, Datatype.NATIVE);

			// Add a Integer
			long[] intDims = { 1 };
			int[] intValues = { intValue };
			// Create an attribute object
			Attribute intAttribute = new Attribute(integerName, intDatatype,
					intDims, intValues);
			// Write the attribute
			parentH5Group.writeMetadata(intAttribute);

			// Add a Double
			long[] doubleDims = { 1 };
			double[] doubleValues = { doubleValue };
			// Create an attribute object
			Attribute doubleAttribute = new Attribute(doubleName,
					doubleDatatype, doubleDims, doubleValues);
			// Write the attribute
			parentH5Group.writeMetadata(doubleAttribute);

			// Add a String
			long[] stringDims = { 1 };
			String[] stringValues = new String[1];
			stringValues[0] = stringValue;
			// Create a byte array from values using the stringToByte method
			// See
			// http://mail.hdfgroup.org/pipermail/hdf-forum_hdfgroup.org/2011-March/004509.html
			byte[] bvalue = Dataset.stringToByte(stringValues,
					stringValue.length());
			// Create an attribute object
			Attribute stringAttribute = new Attribute(stringName,
					stringDatatype, new long[] { 1 });
			// Set the value of the attribute to bvalue
			stringAttribute.setValue(bvalue);
			// Write the attribute to the group's metadata
			parentH5Group.writeMetadata(stringAttribute);

			h5File.close();

			h5File.open();
			parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			// Now, time to read those values!
			assertEquals(
					intValue,
					HdfReaderFactory.readIntegerAttribute(parentH5Group,
							integerName).intValue());
			assertEquals(
					doubleValue,
					HdfReaderFactory.readDoubleAttribute(parentH5Group,
							doubleName).doubleValue(), 0.0);
			assertEquals(stringValue, HdfReaderFactory.readStringAttribute(
					parentH5Group, stringName));

			// Checking for child groups.
			assertNotNull(HdfReaderFactory.getChildH5Groups(parentH5Group));
			assertEquals(0, HdfReaderFactory.getChildH5Groups(parentH5Group)
					.size());

			// Check for child objects.
			assertNotNull(HdfReaderFactory.getChildH5Members(parentH5Group));
			assertEquals(0, HdfReaderFactory.getChildH5Members(parentH5Group)
					.size());

			// Lets add something to the group
			H5Group h5Group = (H5Group) h5File.createGroup(childGroupName,
					parentH5Group);
			// Add an attribute to the group
			// Add a Integer
			h5Group.writeMetadata(intAttribute);
			assertNotNull(h5Group);

			// Add a dataset (100x50 integer array) to the parent group.
			String datasetName = "2D integer";
			Datatype dtype = new H5Datatype(Datatype.CLASS_INTEGER, 4,
					Datatype.ORDER_LE, Datatype.SIGN_NONE);
			long[] dims = { 100, 50 };
			long[] maxdims = dims;
			long[] chunks = null;
			int gzip = 0;
			Object data = null;
			h5File.createScalarDS(datasetName, parentH5Group, dtype, dims,
					maxdims, chunks, gzip, data);

			// Close to persist the data and reopen, get the new parenth5Group
			// object
			h5File.close();

			h5File.open();
			parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			// Check for children now. There should only be one child group.
			assertNotNull(HdfReaderFactory.getChildH5Groups(parentH5Group));
			assertEquals(1, HdfReaderFactory.getChildH5Groups(parentH5Group)
					.size());
			assertEquals(childGroupName,
					HdfReaderFactory.getChildH5Groups(parentH5Group).get(0)
							.getName());

			// Check for child objects. There should be two (a group and a
			// dataset).
			assertNotNull(HdfReaderFactory.getChildH5Members(parentH5Group));
			assertEquals(2, HdfReaderFactory.getChildH5Members(parentH5Group)
					.size());
			assertEquals(childGroupName,
					HdfReaderFactory.getChildH5Members(parentH5Group).get(1)
							.getName());
			assertEquals(datasetName,
					HdfReaderFactory.getChildH5Members(parentH5Group).get(0)
							.getName());

			// Delete the file
			dataFile.delete();

		} catch (Exception e) {
			// Fail out of the test, not supposed to happen
			e.printStackTrace();
			dataFile.delete(); // Delete the dataFile
			fail();
		}

		// end-user-code
	}
}