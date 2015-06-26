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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.Ring;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * <p>
 * This class tests the MaterialBlock's operations.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class MaterialBlockTester {
	/**
	 * <p>
	 * This operation checks the adding, getting and removing of Rings.
	 * </p>
	 * 
	 */
	@Test
	public void checkRingMutators() {

		// Local Declarations
		MaterialBlock materialBlock = new MaterialBlock();

		// Check for adding a null ring
		boolean result = materialBlock.addRing(null);
		assertFalse(result);

		// Check for adding a ring
		Ring ring1 = new Ring();
		ring1.setName("Ring 1");
		ring1.setOuterRadius(10);
		ring1.setInnerRadius(9);
		result = materialBlock.addRing(ring1);
		assertTrue(result);

		// Check for adding another ring
		Ring ring2 = new Ring();
		ring2.setName("Ring 2");
		ring2.setOuterRadius(4);
		ring2.setInnerRadius(3);
		result = materialBlock.addRing(ring2);
		assertTrue(result);

		// Check for adding an overlapping ring
		Ring ring3 = new Ring();
		ring3.setName("Ring 3");
		ring3.setOuterRadius(3.5);
		ring3.setInnerRadius(2.5);
		result = materialBlock.addRing(ring3);
		assertFalse(result);

		// Check for getting a ring by a null name
		Ring ring4 = materialBlock.getRing(null);
		assertNull(ring4);

		// Check for getting a ring by a name that does not exist
		Ring ring5 = materialBlock.getRing("Harold");
		assertNull(ring5);

		// Check for getting a ring by a name that does exist
		Ring ring6 = materialBlock.getRing("Ring 2");
		assertNotNull(ring6);
		assertEquals(ring6, ring2);
		assertEquals(ring6.getName(), "Ring 2");
		assertTrue(ring6.getOuterRadius() == 4);
		assertTrue(ring6.getInnerRadius() == 3);

		// Check for getting a ring by a radius not in the set of Rings
		Ring ring7 = materialBlock.getRing(2.5);
		assertNull(ring7);

		// Check for getting a ring by a radius in the set of Rings
		Ring ring8 = materialBlock.getRing(3.5);
		assertNotNull(ring8);
		assertEquals(ring8, ring2);
		assertEquals(ring8.getName(), "Ring 2");
		assertTrue(ring8.getOuterRadius() == 4);
		assertTrue(ring8.getInnerRadius() == 3);

		// Check for getting the set of rings
		ArrayList<Ring> list = materialBlock.getRings();
		assertNotNull(list);
		assertTrue(list.size() == 2);
		assertEquals(list.get(0), ring2);
		assertEquals(list.get(1), ring1);

		// Check for removing a ring by a null name
		result = materialBlock.removeRing(null);
		assertFalse(result);
		list = materialBlock.getRings();
		assertTrue(list.size() == 2);
		assertEquals(list.get(0), ring2);
		assertEquals(list.get(1), ring1);

		// Check for removing a ring that is not in the set
		result = materialBlock.removeRing("Harold");
		assertFalse(result);
		list = materialBlock.getRings();
		assertTrue(list.size() == 2);
		assertEquals(list.get(0), ring2);
		assertEquals(list.get(1), ring1);

		// Check for removing a ring that is in the set
		result = materialBlock.removeRing("Ring 1");
		assertTrue(result);
		list = materialBlock.getRings();
		assertTrue(list.size() == 1);
		assertEquals(list.get(0), ring2);

	}

	/**
	 * <p>
	 * This operation checks the constructor and it's default values.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {
		// Local Declaration
		MaterialBlock materialBlock;
		// Default values
		String defaultName = "MaterialBlock 1";
		String defaultDesc = "MaterialBlock 1's Description";
		int defaultId = 1;
		HDF5LWRTagType type = HDF5LWRTagType.MATERIALBLOCK;
		double defaultPos = 0.0;

		// Instantiate the materialBlock and check default values
		materialBlock = new MaterialBlock();

		// check values
		assertEquals(defaultName, materialBlock.getName());
		assertEquals(defaultDesc, materialBlock.getDescription());
		assertEquals(defaultId, materialBlock.getId());
		assertEquals(type, materialBlock.getHDF5LWRTag());
		assertEquals(defaultPos, materialBlock.getPosition(), 0.0);

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
		MaterialBlock object, equalObject, unEqualObject, transitiveObject;
		Ring ring1, ring2;

		// Setup Rings
		ring1 = new Ring("Bob", new Material("Billy"), 20.0, 5.0, 10.0);
		ring2 = new Ring("Bob2", new Material("Billy2"), 22.0, 52.0, 102.0);

		// Setup root object
		object = new MaterialBlock();
		object.addRing(ring1);
		object.addRing(ring2);

		// Setup equalObject equal to object
		equalObject = new MaterialBlock();
		equalObject.addRing(ring1);
		equalObject.addRing(ring2);

		// Setup transitiveObject equal to object
		transitiveObject = new MaterialBlock();
		transitiveObject.addRing(ring1);
		transitiveObject.addRing(ring2);

		// Set its data, not equal to object
		unEqualObject = new MaterialBlock();
		unEqualObject.addRing(ring2);

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
	 * Checks the copy and clone routines.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Local Declarations
		MaterialBlock object, copyObject, clonedObject;
		Ring ring1, ring2;

		// Setup Rings
		ring1 = new Ring("Bob", new Material("Billy"), 20.0, 5.0, 10.0);
		ring2 = new Ring("Bob2", new Material("Billy2"), 22.0, 52.0, 102.0);

		// Setup root object
		object = new MaterialBlock();
		object.addRing(ring1);
		object.addRing(ring2);

		// Run the copy routine
		copyObject = new MaterialBlock();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (MaterialBlock) object.clone();

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
		MaterialBlock component = new MaterialBlock();
		String name = "MaterialBlock";
		String description = "Its a collection of rings!  MaterialBlock put a ring on it!  It has several!";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		Ring ring1 = new Ring("Ring 1");
		ring1.setOuterRadius(6);
		ring1.setInnerRadius(4);
		Ring ring2 = new Ring("Ring 2");
		ring2.setOuterRadius(3);
		ring2.setInnerRadius(1);
		String testFileName = "testWrite.h5";
		double position = 2.3;

		Attribute attribute = null;
		String attributeValue = null;

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.addRing(ring1);
		component.addRing(ring2);
		component.setPosition(position);

		// Setup the HDF5 File
		String separator = System.getProperty("file.separator");
		System.out.println("Going to open file: "
				+ System.getProperty("user.dir") + separator + testFileName);
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
		assertNotNull(component.getWriteableChildren());

		// Check children
		assertEquals(component.getRings().size(), component
				.getWriteableChildren().size());
		assertTrue(ring2.equals(component.getWriteableChildren().get(0)));
		assertTrue(ring1.equals(component.getWriteableChildren().get(1)));

		// Check writing attributes
		H5Group h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		// Pass the group and file to the writer for attributes
		// See that it passes
		assertTrue(component.writeAttributes(h5File, h5Group));

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

			// Check Double Attribute - rodPitch
			attribute = (Attribute) h5Group.getMetadata().get(4);
			assertEquals(attribute.getName(), "position");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(position, ((double[]) attribute.getValue())[0], 1.2);
			// Reset Values
			attribute = null;
			attributeValue = null;

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Make sure the writeAttributes fail for invalid stuff
		assertFalse(component.writeAttributes(null, h5Group));
		assertFalse(component.writeAttributes(h5File, null));

		// Check dataSet. Pass null to show that it always returns true and does
		// nothing
		assertFalse(component.writeDatasets(null, null));

		// Check Group Creation
		H5Group group = component.createGroup(h5File, h5Group);
		// See that the previous group has a group
		assertEquals(1, h5Group.getMemberList().size());
		// Check that it has the same name as the root component
		assertEquals(component.getName(), h5Group.getMemberList().get(0)
				.toString());
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
		MaterialBlock component = new MaterialBlock();
		MaterialBlock newComponent = new MaterialBlock();
		String name = "MaterialBlock";
		String description = "Its a collection of rings!  MaterialBlock put a ring on it!  It has several!";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		Ring ring1 = new Ring("Ring 1");
		ring1.setOuterRadius(6);
		ring1.setInnerRadius(4);
		Ring ring2 = new Ring("Ring 2");
		ring2.setOuterRadius(3);
		ring2.setInnerRadius(1);
		H5Group subGroup = null;
		double position = 2.3;

		// Test readChild here
		assertTrue(component.readChild(ring1));
		assertFalse(component.readChild(null));
		assertTrue(component.readChild(newComponent));
		assertTrue(component.getRings().size() == 1);
		assertTrue(component.getRing(ring1.getName()).equals(ring1));

		// Reset component
		component.removeRing(ring1.getName());

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.addRing(ring1);
		component.addRing(ring2);
		component.setPosition(position);

		// Add rings to group. Should remove once children are set up correctly
		newComponent.addRing(ring1);
		newComponent.addRing(ring2);

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

			// Write pos
			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup, "position",
					position);

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

	/**
	 * <p>
	 * Checks the get/set position operators.
	 * </p>
	 * 
	 */
	public void checkPosition() {
		MaterialBlock materialBlock = new MaterialBlock();

		assertEquals(0, materialBlock.getPosition(), 0.0);

		// Set to bad value
		materialBlock.setPosition(-1.0);
		// Doesnt change
		assertEquals(0, materialBlock.getPosition(), 0.0);

		// Set to good value
		materialBlock.setPosition(0.0);
		assertEquals(0, materialBlock.getPosition(), 0.0);

		// Set to good value
		materialBlock.setPosition(1.0);
		assertEquals(1, materialBlock.getPosition(), 0.0);

	}

	/**
	 * <p>
	 * Checks the comparison operator.
	 * </p>
	 * 
	 */
	public void checkCompareTo() {

		// Local Declarations
		MaterialBlock mBlock1, mBlock2, mBlock3;
		double pos1, pos2;
		pos1 = 1.0;
		pos2 = 2.0;

		// Create blocks
		// Less than
		mBlock1 = new MaterialBlock();
		mBlock1.setPosition(pos1);

		// Greater than
		mBlock2 = new MaterialBlock();
		mBlock2.setPosition(pos2);

		// Equal to block1
		mBlock3 = new MaterialBlock();
		mBlock3.setPosition(pos1);

		// Show comparisons
		assertEquals(-1, mBlock1.compareTo(mBlock2));
		assertEquals(1, mBlock2.compareTo(mBlock1));
		assertEquals(0, mBlock1.compareTo(mBlock3));

	}
}