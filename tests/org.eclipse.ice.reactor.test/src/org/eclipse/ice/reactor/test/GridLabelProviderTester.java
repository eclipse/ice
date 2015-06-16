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

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;

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
	 * This operation checks the HDF5 writing operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkHDF5Writeables() {

		// Local Declarations
		int size = 5;
		GridLabelProvider provider = new GridLabelProvider(size);
		String name = "GRID!";
		String description = "LABELS!";
		int id = 4;
		HDF5LWRTagType tag = provider.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		ArrayList<String> columnLabels = new ArrayList<String>();
		ArrayList<String> rowLabels = new ArrayList<String>();
		String testFileName = "testWrite.h5";

		// Setup column Labels
		columnLabels.add("Aasdasdasdasdasdasd");
		columnLabels.add("B");
		columnLabels.add("C");
		columnLabels.add("D");
		columnLabels.add("E");

		// Setup row Labels
		rowLabels.add("1");
		rowLabels.add("2");
		rowLabels.add("3");
		rowLabels.add("4");
		rowLabels.add("5");

		// Setup Provider
		provider.setName(name);
		provider.setId(id);
		provider.setDescription(description);
		provider.setColumnLabels(columnLabels);
		provider.setRowLabels(rowLabels);

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
		assertNull(provider.getWriteableChildren());

		// Check writing attributes
		H5Group h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		// Pass the group and file to the writer for attributes
		// See that it passes
		assertTrue(provider.writeAttributes(h5File, h5Group));

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
			assertEquals(5, h5Group.getMetadata().size());

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

			// Check Integer Attribute - id
			attribute = (Attribute) h5Group.getMetadata().get(2);
			assertEquals(attribute.getName(), "id");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_INTEGER);
			assertEquals(id, ((int[]) attribute.getValue())[0]);
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

			// Check Integer Attribute - size
			attribute = (Attribute) h5Group.getMetadata().get(4);
			assertEquals(attribute.getName(), "size");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_INTEGER);
			assertEquals(size, ((int[]) attribute.getValue())[0]);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Make sure the writeAttributes fail for invalid stuff
		assertFalse(provider.writeAttributes(null, h5Group));
		assertFalse(provider.writeAttributes(h5File, null));

		// Check dataSet. Pass null to show it will fail.
		assertFalse(provider.writeDatasets(null, null));
		assertFalse(provider.writeDatasets(null, h5Group));
		assertFalse(provider.writeDatasets(h5File, null));

		// Perform a dataSet Write
		assertTrue(provider.writeDatasets(h5File, h5Group));

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

		// Check the values of the dataset!
		assertEquals(2, h5Group.getMemberList().size());
		assertEquals("Labels", h5Group.getMemberList().get(0).getName());
		assertEquals("State Point Data", h5Group.getMemberList().get(1)
				.getName());
		// Get the Labels Group
		H5Group labelsGroup = (H5Group) h5Group.getMemberList().get(0);

		// Check the values of that dataset
		assertEquals("Column Labels", labelsGroup.getMemberList().get(0)
				.getName());
		assertEquals("Row Labels", labelsGroup.getMemberList().get(1).getName());
		// Check labels that they are not a group
		assertFalse(labelsGroup.getMemberList().get(0) instanceof Group);
		assertFalse(labelsGroup.getMemberList().get(1) instanceof Group);

		// Check DataSet information
		try {
			// Check the rowData and columnData
			Dataset rowSet = (Dataset) labelsGroup.getMemberList().get(1);
			Dataset columnSet = (Dataset) labelsGroup.getMemberList().get(0);
			Object rowData = rowSet.getData();
			Object colData = columnSet.getData();

			// Iterate over the list and check the arrayLists
			for (int i = 0; i < rowLabels.size(); i++) {
				// Check contents of rowData and columnData at X
				assertEquals(rowLabels.get(i), ((String[]) rowData)[i]);
				assertEquals(columnLabels.get(i), ((String[]) colData)[i]);
			}

		} catch (Exception e) {
			// Fail out of the test
			e.printStackTrace();
			fail();
		}

		// Check Group Creation
		H5Group group = provider.createGroup(h5File, h5Group);
		// See that the previous group has a group
		assertEquals(3, h5Group.getMemberList().size());
		// Check that it has the same name as the root provider
		assertEquals(provider.getName(), h5Group.getMemberList().get(2)
				.toString());
		// Check that the returned group is a Group but no members
		assertEquals(0, group.getMemberList().size());
		assertEquals(0, ((Group) h5Group.getMemberList().get(2))
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
	 * This operation checks the HDF5 readable operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkHDF5Readables() {

		// Local Declarations
		int size = 5;
		GridLabelProvider component = new GridLabelProvider(size);
		GridLabelProvider newComponent = new GridLabelProvider(-1);
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		H5Group subGroup = null;
		ArrayList<String> columnLabels = new ArrayList<String>();
		ArrayList<String> rowLabels = new ArrayList<String>();

		// Setup column Labels
		columnLabels.add("Aasdasdasdasdasdasd");
		columnLabels.add("B");
		columnLabels.add("C");
		columnLabels.add("D");
		columnLabels.add("E");

		// Setup row Labels
		rowLabels.add("1");
		rowLabels.add("2");
		rowLabels.add("3");
		rowLabels.add("4");
		rowLabels.add("5");

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.setRowLabels(rowLabels);
		component.setColumnLabels(columnLabels);

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

			// Write size attribute
			HdfWriterFactory.writeIntegerAttribute(h5File, subGroup, "size",
					size);

			// Create Datasets
			String[] rowValues = new String[size];
			String[] columnValues = new String[size];

			// Declare biggest size variables
			int biggestRowSize = 0;
			int biggestColumnSize = 0;

			// Loop over the labels
			for (int i = 0; i < size; i++) {

				// Assign the current label
				String rowLabel = rowLabels.get(i);
				String columnLabel = columnLabels.get(i);

				// Get the biggest size label
				biggestRowSize = Math.max(biggestRowSize, rowLabel.length());
				biggestColumnSize = Math.max(biggestColumnSize,
						columnLabel.length());

				// Assign the label to the String array
				rowValues[i] = rowLabel;
				columnValues[i] = columnLabel;

			}

			// Create datatypes for the row and column labels
			H5Datatype rowH5Datatype = (H5Datatype) h5File.createDatatype(
					Datatype.CLASS_STRING, biggestRowSize, Datatype.NATIVE,
					Datatype.NATIVE);

			H5Datatype columnH5Datatype = (H5Datatype) h5File.createDatatype(
					Datatype.CLASS_STRING, biggestColumnSize, Datatype.NATIVE,
					Datatype.NATIVE);

			// Set the dimensions of the String array
			long[] dims = { size };

			H5Group labelsGroup = HdfWriterFactory.createH5Group(h5File,
					"Labels", subGroup);
			H5Group asdGroup = HdfWriterFactory.createH5Group(h5File,
					"State Point Data", subGroup);

			// Create the row and column datasets
			Dataset h5RowDataset = h5File.createScalarDS("Row Labels",
					labelsGroup, rowH5Datatype, dims, null, null, 0, null);
			Dataset h5ColumnDataset = h5File.createScalarDS("Column Labels",
					labelsGroup, columnH5Datatype, dims, null, null, 0, null);

			// Write the String arrays to the right dataset
			h5RowDataset.write(rowValues);
			h5ColumnDataset.write(columnValues);

			// Close group and then reopen
			h5File.close();
			h5File.open();
			parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			// Get the subGroup
			subGroup = (H5Group) parentH5Group.getMemberList().get(0);

			// Read information
			assertTrue(newComponent.readAttributes(subGroup));

			// Datasets should return false if null is passed
			assertFalse(newComponent.readDatasets(null));

			// Read the group in correctly
			assertTrue(newComponent.readDatasets(subGroup));
			// Check with setup component
			assertTrue(component.equals(newComponent));

			// FIXME - We may need to remove this test pending a review on
			// getMemberList().
			// //Now, lets try to set an erroneous H5Group with missing data
			// subGroup.getMetadata().remove(1);
			// subGroup.getMemberList().remove(0);
			// subGroup.getMemberList().remove(0);

			// //Run it through
			// assertFalse(newComponent.readAttributes(subGroup));
			// assertTrue(newComponent.readDatasets(subGroup)); //This will pass
			// if a Dataset does not exist!
			// //Check it does not change
			// assertTrue(component.equals(newComponent));

			// Check for null
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