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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * A class that tests the Material's operations.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class MaterialTester {
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
		Material material = null;
		String defaultName = "Material";
		String defaultDesc = "Material's Description";
		String newName = "Special Material";
		MaterialType defaultType = MaterialType.SOLID;
		MaterialType newType = MaterialType.GAS;
		int defaultId = 1;
		HDF5LWRTagType type = HDF5LWRTagType.MATERIAL;

		// Instantiate material and check default values
		material = new Material();
		assertEquals(defaultName, material.getName());
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(defaultType, material.getMaterialType());
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material with different name and check default values
		material = new Material(newName);
		assertEquals(newName, material.getName());
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(defaultType, material.getMaterialType());
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material incorrectly and check default values
		material = new Material(null);
		assertEquals(defaultName, material.getName()); // Name defaults
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(defaultType, material.getMaterialType());
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material with different name and different type. Check
		// values
		material = new Material(newName, newType);
		assertEquals(newName, material.getName());
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(newType, material.getMaterialType());
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material with erroneous name and type - check defaults
		material = new Material(null, null);
		assertEquals(defaultName, material.getName()); // defaults
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(defaultType, material.getMaterialType()); // defaults
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material with erroneous type, but not name - check
		// defaults
		material = new Material(newName, null);
		assertEquals(newName, material.getName());
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(defaultType, material.getMaterialType()); // defaults
		assertEquals(type, material.getHDF5LWRTag());

		// Instantiate material with erroneous name, but not type - check
		// defaults
		material = new Material(null, newType);
		assertEquals(defaultName, material.getName()); // defaults
		assertEquals(defaultDesc, material.getDescription());
		assertEquals(defaultId, material.getId());
		assertEquals(newType, material.getMaterialType());
		assertEquals(type, material.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for materialTypes.
	 * </p>
	 * 
	 */
	@Test
	public void checkMaterialType() {

		// Local Declarations
		Material material = new Material();
		MaterialType defaultType = MaterialType.SOLID;

		// Check default material type value
		assertEquals(defaultType, material.getMaterialType());

		// Set the material type - SOLID
		material.setMaterialType(MaterialType.SOLID);
		// Check value
		assertEquals(MaterialType.SOLID, material.getMaterialType());

		// Set the material type - LIQUID
		material.setMaterialType(MaterialType.LIQUID);
		// Check value
		assertEquals(MaterialType.LIQUID, material.getMaterialType());

		// Set the material type - GAS
		material.setMaterialType(MaterialType.GAS);
		// Check value
		assertEquals(MaterialType.GAS, material.getMaterialType());

		// Check for null
		material.setMaterialType(null);
		// See that it is the last set made
		// Check value
		assertEquals(MaterialType.GAS, material.getMaterialType());

	}

	/**
	 * <p>
	 * This operation checks the equals and hashCode operations.
	 * </p>
	 * 
	 */
	public void checkEquality() {

		// Local Declarations
		Material object, equalObject, unEqualObject, transitiveObject;
		String name = "Bill";
		MaterialType type = MaterialType.GAS;
		MaterialType unEqualType = MaterialType.LIQUID;
		String description = "Description !@#!@#!@#!46546484328";
		int id = 68468431;

		// Setup root object
		object = new Material(name);
		object.setId(id);
		object.setDescription(description);
		object.setMaterialType(type);

		// Setup equalObject equal to object
		equalObject = new Material(name);
		equalObject.setId(id);
		equalObject.setDescription(description);
		equalObject.setMaterialType(type);

		// Setup transitiveObject equal to object
		transitiveObject = new Material(name);
		transitiveObject.setId(id);
		transitiveObject.setDescription(description);
		transitiveObject.setMaterialType(type);

		// Set its data, not equal to object
		// Does not contain components!
		unEqualObject = new Material(name);
		unEqualObject.setId(id);
		unEqualObject.setDescription(description);
		unEqualObject.setMaterialType(unEqualType);

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

		// Local declarations
		Material object, copyObject, clonedObject;
		String name = "Bill";
		MaterialType type = MaterialType.GAS;
		String description = "Description !@#!@#!@#!46546484328";
		int id = 68468431;

		// Setup root object
		object = new Material(name);
		object.setId(id);
		object.setDescription(description);
		object.setMaterialType(type);

		// Run the copy routine
		copyObject = new Material();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (Material) object.clone();

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
		Material component = new Material();
		String name = "Materials";
		String description = "Materials don't say anything";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		MaterialType type = component.getMaterialType();
		String testFileName = "testWrite.h5";

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

			// Check String Attribute - material type
			attribute = (Attribute) h5Group.getMetadata().get(3);
			assertEquals(attribute.getName(), "materialType");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			attributeValue = ((String[]) attribute.getValue())[0];
			assertEquals(type.toString(), attributeValue);
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

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Make sure the writeAttributes fail for invalid stuff
		assertFalse(component.writeAttributes(null, h5Group));
		assertFalse(component.writeAttributes(h5File, null));

		// Check dataSet.
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
	 * This operation checks the HDF5 readable operations.
	 * </p>
	 * 
	 */
	@Test
	public void checkHDF5Readables() {

		// Local Declarations
		Material component = new Material();
		Material newComponent = new Material("NOT A DEFAULT ONE");
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		MaterialType type = MaterialType.GAS;
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		H5Group subGroup = null;

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.setMaterialType(type);

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

		// Check Readable Children
		assertTrue(component.readChild(null));

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

			// Setup Material attribute
			HdfWriterFactory.writeStringAttribute(h5File, subGroup,
					"materialType", type.toString());

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
	 * Checks the toString and toType operations on MaterialType.
	 * </p>
	 * 
	 */
	@Test
	public void checkTyping() {

		// Local Declarations
		MaterialType type;

		// Check the toString implementations of the materialType enum
		assertEquals("Gas", MaterialType.GAS.toString());
		assertEquals("Solid", MaterialType.SOLID.toString());
		assertEquals("Liquid", MaterialType.LIQUID.toString());

		// Check the toType implementations of the HDf5 enum

		// Specify the type
		type = MaterialType.GAS;
		// Check the type
		assertEquals(type.toType("Gas"), MaterialType.GAS);

		// Specify the type
		type = MaterialType.LIQUID;
		// Check the type
		assertEquals(type.toType("Liquid"), MaterialType.LIQUID);

		// Specify the type
		type = MaterialType.SOLID;
		// Check the type
		assertEquals(type.toType("Solid"), MaterialType.SOLID);

		// Try to return a type that does not exist
		assertEquals(type.toType("asdasd1"), null);

	}
}