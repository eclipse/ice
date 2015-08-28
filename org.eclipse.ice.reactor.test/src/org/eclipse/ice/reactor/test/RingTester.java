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
import static org.junit.Assert.assertNotNull;
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
import org.eclipse.ice.reactor.Ring;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * A class that tests the Ring's operations.
 * </p>
 * 
 * @author Scott Forest Hull II
 */

public class RingTester {
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
		Ring ring;
		// Default values. Change here as needed for tests
		String defaultName = "Ring";
		String defaultDesc = "Ring's Description";
		int defaultId = 1;
		double defaultInnerRadius = 0;
		double defaultOuterRadius = 1;
		double defaultHeight = 1.0;
		HDF5LWRTagType type = HDF5LWRTagType.RING;

		// New values
		String newName = "Super Ring!";
		Material newMaterial = new Material();
		newMaterial.setName("SuperMaterial!!@#!@#*!@#!");
		double newHeight = 1.51231541513;
		double newOuterRadius = 86.3985656;
		double newInnerRadius = 5.83495787819;

		// Check nullary constructor
		ring = new Ring();
		// Check default values
		assertEquals(defaultName, ring.getName());
		assertEquals(defaultDesc, ring.getDescription());
		assertEquals(defaultId, ring.getId());
		assertEquals(defaultInnerRadius, ring.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, ring.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, ring.getHeight(), 0.0);
		assertNotNull(ring.getMaterial());
		assertEquals(type, ring.getHDF5LWRTag());

		// Check non-nullary constructor - name
		ring = new Ring(newName);
		// Check default values
		assertEquals(newName, ring.getName());
		assertEquals(defaultDesc, ring.getDescription());
		assertEquals(defaultId, ring.getId());
		assertEquals(defaultInnerRadius, ring.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, ring.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, ring.getHeight(), 0.0);
		assertNotNull(ring.getMaterial());
		assertEquals(type, ring.getHDF5LWRTag());

		// Check non-nullary constructor - name with null
		ring = new Ring(null);
		// Check default values
		assertEquals(defaultName, ring.getName()); // Defaults name
		assertEquals(defaultDesc, ring.getDescription());
		assertEquals(defaultId, ring.getId());
		assertEquals(defaultInnerRadius, ring.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, ring.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, ring.getHeight(), 0.0);
		assertNotNull(ring.getMaterial());
		assertEquals(type, ring.getHDF5LWRTag());

		// Check non-nullary constructor - name, material, height, outerRadius
		ring = new Ring(newName, newMaterial, newHeight, newOuterRadius);
		// Check default values
		assertEquals(newName, ring.getName());
		assertEquals(defaultDesc, ring.getDescription());
		assertEquals(defaultId, ring.getId());
		assertEquals(defaultInnerRadius, ring.getInnerRadius(), 0.0);
		assertEquals(newOuterRadius, ring.getOuterRadius(), 0.0);
		assertEquals(newHeight, ring.getHeight(), 0.0);
		assertNotNull(ring.getMaterial());
		assertEquals(newMaterial.getName(), ring.getMaterial().getName());
		assertEquals(type, ring.getHDF5LWRTag());

		// Check non-nullary constructor - name, material, height, innerRadius,
		// outerRadius
		ring = new Ring(newName, newMaterial, newHeight, newInnerRadius,
				newOuterRadius);
		// Check default values
		assertEquals(newName, ring.getName());
		assertEquals(defaultDesc, ring.getDescription());
		assertEquals(defaultId, ring.getId());
		assertEquals(newInnerRadius, ring.getInnerRadius(), 0.0);
		assertEquals(newOuterRadius, ring.getOuterRadius(), 0.0);
		assertEquals(newHeight, ring.getHeight(), 0.0);
		assertNotNull(ring.getMaterial());
		assertEquals(newMaterial.getName(), ring.getMaterial().getName());
		assertEquals(type, ring.getHDF5LWRTag());

		// Check nullaries - check name
		ring = new Ring(null, newMaterial, newHeight, newOuterRadius);
		assertEquals(defaultName, ring.getName()); // Defaults name

		// Check nullaries - check material
		ring = new Ring(newName, null, newHeight, newOuterRadius);
		assertNotNull(ring.getMaterial()); // Still generates a material

		// Check nullaries - check height
		ring = new Ring(null, newMaterial, 0.0, newOuterRadius);
		assertEquals(defaultHeight, ring.getHeight(), 0.0); // Defaults height

		// Check bad values - check outer radius
		ring = new Ring(newName, newMaterial, 1.0, 0.0);
		assertEquals(defaultOuterRadius, ring.getOuterRadius(), 0.0); // Defaults
																		// radius

		// Check bad values - check inner radius
		ring = new Ring(newName, newMaterial, newHeight, -1.0, newOuterRadius);
		assertEquals(defaultInnerRadius, ring.getInnerRadius(), 0.0); // Defaults
																		// radius
		assertEquals(type, ring.getHDF5LWRTag());

	}

	/**
	 * <p>
	 * This operation checks the implementation of the Comparable interface.
	 * </p>
	 * 
	 */
	@Test
	public void checkCompareTo() {

		// Local declarations
		// Declare comparable constants
		final int BEFORE = -1;
		final int AFTER = 1;
		final int EQUAL = 0;

		// Create a test ring
		Ring ring = new Ring();
		ring.setOuterRadius(6);
		ring.setInnerRadius(5);

		// Create and test a "after" ring
		Ring ring1 = new Ring();
		ring1.setOuterRadius(5);
		ring1.setInnerRadius(4);

		// Get the comparison result
		int result = ring.compareTo(ring1);
		assertEquals(result, AFTER);

		// Create and test an "before" ring
		Ring ring2 = new Ring();
		ring2.setOuterRadius(7);
		ring2.setInnerRadius(6);

		// Get the comparison result
		result = ring.compareTo(ring2);
		assertEquals(result, BEFORE);

		// Create and test an "equal" ring with both radii overlapping
		Ring ring3 = new Ring();
		ring3.setOuterRadius(7);
		ring3.setInnerRadius(4);

		// Get the comparison result
		result = ring.compareTo(ring3);
		assertEquals(result, EQUAL);

		// Create and test an "equal" ring with an overlapping inner radius
		Ring ring4 = new Ring();
		ring4.setOuterRadius(7);
		ring4.setInnerRadius(5.5);

		// Get the comparison result
		result = ring.compareTo(ring4);
		assertEquals(result, EQUAL);

		// Create and test an "equal" ring with an overlapping outer radius
		Ring ring5 = new Ring();
		ring5.setOuterRadius(5.5);
		ring5.setInnerRadius(4);

		// Get the comparison result
		result = ring.compareTo(ring5);
		assertEquals(result, EQUAL);

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the height.
	 * </p>
	 * 
	 */
	@Test
	public void checkHeight() {
		// Local declarations
		Ring ring = new Ring();

		// Check setter - legal value
		ring.setHeight(5.0);
		assertEquals(5.0, ring.getHeight(), 0.0);

		// Check for illegal value - 0
		ring.setHeight(0.0);
		assertEquals(5.0, ring.getHeight(), 0.0);

		// Check for illegal value - negative
		ring.setHeight(-1.0);
		assertEquals(5.0, ring.getHeight(), 0.0);

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the InnerRadius.
	 * </p>
	 * 
	 */
	@Test
	public void checkInnerRadius() {
		// Local declarations
		Ring ring = new Ring();

		// Check setter - legal value
		ring.setOuterRadius(20.0);
		ring.setInnerRadius(3.3453453);
		assertEquals(3.3453453, ring.getInnerRadius(), 0.0);

		// Check setter - legal value of 0
		ring.setInnerRadius(0.0);
		assertEquals(0.0, ring.getInnerRadius(), 0.0);

		// Check illegal value - negative
		ring.setInnerRadius(-1.0);
		assertEquals(0.0, ring.getInnerRadius(), 0.0); // stays the same as
														// before

		// Inner radius can not be greater than or equal to outer radius
		ring.setInnerRadius(20.0);
		assertEquals(0.0, ring.getInnerRadius(), 0.0);

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the outer radius.
	 * </p>
	 * 
	 */
	@Test
	public void checkOuterRadius() {
		// Local declarations
		Ring ring = new Ring();

		// Check setter - legal value
		ring.setOuterRadius(20.0);
		ring.setInnerRadius(3.3453453);
		assertEquals(20.0, ring.getOuterRadius(), 0.0);

		// Check illegal value of 0
		ring.setOuterRadius(0.0);
		assertEquals(20.0, ring.getOuterRadius(), 0.0);

		// Check illegal value - negative
		ring.setOuterRadius(-1.0);
		assertEquals(20.0, ring.getOuterRadius(), 0.0); // stays the same as
														// before

		// Outer radius can not be less than or equal to inner radius
		ring.setInnerRadius(1.0);
		assertEquals(20.0, ring.getOuterRadius(), 0.0);

	}

	/**
	 * <p>
	 * This operation checks the getter and setter for the Material.
	 * </p>
	 * 
	 */
	@Test
	public void checkMaterial() {

		// Local declarations
		Ring ring = new Ring();
		Material material = new Material();
		String name = "Bobble123123198796**3213.12313123";

		// Set name on material
		material.setName(name);

		// Set material
		ring.setMaterial(material);

		// Check to see if set correctly
		assertNotNull(ring.getMaterial());
		// Check the name
		assertEquals(name, ring.getMaterial().getName());

		// Check illegal null argument
		ring.setMaterial(null);

		// Check to see if set correctly - Values do not change from erroneous
		// commit.
		assertNotNull(ring.getMaterial());
		// Check the name
		assertEquals(name, ring.getMaterial().getName());

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
		Ring object, equalObject, unEqualObject, transitiveObject;
		String name = "Billy!";
		int id = 654654;
		String description = "ASDFG Billy464646";
		Material material = new Material();
		Material unEqualMaterial = new Material();
		unEqualMaterial.setMaterialType(MaterialType.LIQUID);
		material.setMaterialType(MaterialType.GAS);
		double innerRadius = 5.0, outerRadius = 8, height = 20.0;

		// Setup root object
		object = new Ring(name, material, height, innerRadius, outerRadius);
		object.setId(id);
		object.setDescription(description);

		// Setup equalObject equal to object
		equalObject = new Ring(name, material, height, innerRadius, outerRadius);
		equalObject.setId(id);
		equalObject.setDescription(description);

		// Setup transitiveObject equal to object
		transitiveObject = new Ring(name, material, height, innerRadius,
				outerRadius);
		transitiveObject.setId(id);
		transitiveObject.setDescription(description);

		// Set its data, not equal to object
		// Different Material
		unEqualObject = new Ring(name, unEqualMaterial, height, innerRadius,
				outerRadius);
		unEqualObject.setId(id);
		unEqualObject.setDescription(description);

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
		Ring object, copyObject, clonedObject;
		String name = "Billy!";
		int id = 654654;
		String description = "ASDFG Billy464646";
		Material material = new Material();
		material.setMaterialType(MaterialType.GAS);
		double innerRadius = 5.0, outerRadius = 8, height = 20.0;

		// Setup root object
		object = new Ring(name, material, height, innerRadius, outerRadius);
		object.setId(id);
		object.setDescription(description);

		// Run the copy routine
		copyObject = new Ring();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (Ring) object.clone();

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
		Ring component = new Ring();
		String name = "Ring";
		String description = "Put a ring on it...?";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		Material material = new Material("MATERIAL!S");
		double height = 5, inner = 3, outer = 6;

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.setMaterial(material);
		component.setHeight(height);
		component.setOuterRadius(outer);
		component.setInnerRadius(inner);

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

		// Check to see if it has any children
		assertNotNull(component.getWriteableChildren());

		// Check children
		assertEquals(1, component.getWriteableChildren().size());
		assertTrue(material.equals(component.getWriteableChildren().get(0)));

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
			assertEquals(7, h5Group.getMetadata().size());

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

			// Check Double Attribute - height
			attribute = (Attribute) h5Group.getMetadata().get(2);
			assertEquals(attribute.getName(), "height");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(((double[]) attribute.getValue())[0], 1.2, height);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check Integer Attribute - id
			attribute = (Attribute) h5Group.getMetadata().get(3);
			assertEquals(attribute.getName(), "id");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_INTEGER);
			assertEquals(id, ((int[]) attribute.getValue())[0]);

			// Check Double Attribute - innerRadius
			attribute = (Attribute) h5Group.getMetadata().get(4);
			assertEquals(attribute.getName(), "innerRadius");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(((double[]) attribute.getValue())[0], 1.2, inner);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check String Attribute - name
			attribute = (Attribute) h5Group.getMetadata().get(5);
			assertEquals(attribute.getName(), "name");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			attributeValue = ((String[]) attribute.getValue())[0];
			assertEquals(name, attributeValue);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check Double Attribute - outerRadius
			attribute = (Attribute) h5Group.getMetadata().get(6);
			assertEquals(attribute.getName(), "outerRadius");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(((double[]) attribute.getValue())[0], 1.2, outer);
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
		Ring component = new Ring();
		Ring newComponent = new Ring("NOT A DEFAULT ONE");
		String name = "Ring";
		String description = "Put a ring on it...?";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		Material material = new Material("MATERIAL!S");
		double height = 5, inner = 3, outer = 6;
		H5Group subGroup = null;
		String testFileName = "testWrite.h5";

		// Test readChild here
		assertTrue(component.readChild(material));
		assertFalse(component.readChild(null));
		assertTrue(component.readChild(newComponent));
		assertNotNull(component.getMaterial());
		assertTrue(component.getMaterial().getName().equals(material.getName()));

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.setMaterial(material);
		component.setHeight(height);
		component.setOuterRadius(outer);
		component.setInnerRadius(inner);

		newComponent.setMaterial(material);

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

			// Setup height attribute
			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup, "height",
					height);

			// Setup height inner radius
			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup,
					"innerRadius", inner);

			// Setup height outer radius
			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup,
					"outerRadius", outer);

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
}