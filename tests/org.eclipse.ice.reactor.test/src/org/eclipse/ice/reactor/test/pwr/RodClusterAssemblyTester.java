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
import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRGridManager;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * <p>
 * This class tests the RodClusterAssembly operations.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class RodClusterAssemblyTester {
	// An @BeforeClass that sets up the library path. This should be added to
	// the model or removed if it can be fixed for local machine
	@BeforeClass
	public static void beforeClass() {

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
		RodClusterAssembly assembly;
		String defaultName = "RodClusterAssembly";
		String defaultDesc = "RodClusterAssembly's Description";
		int defaultId = 1;
		int defaultSize = 1;
		HDF5LWRTagType type = HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY;

		// New names
		String newName = "Super RodClusterAssembly!";
		int newSize = 10;

		// Check the default constructor with a default size. Check default
		// values
		// Test non-nullary constructor - size
		assembly = new RodClusterAssembly(defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with new size
		// Test non-nullary constructor - size
		assembly = new RodClusterAssembly(newSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(newSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with bad size - negative
		// Test non-nullary constructor - size
		assembly = new RodClusterAssembly(-1);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize()); // Defaults
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with name and size
		// Test non-nullary constructor - name, size
		assembly = new RodClusterAssembly(defaultName, defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with bad name
		// Test non-nullary constructor - name, size
		assembly = new RodClusterAssembly(null, defaultSize);
		// Check values
		assertEquals(defaultName, assembly.getName()); // Defaults
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(defaultSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

		// Check with new name and size
		// Test non-nullary constructor - name, size
		assembly = new RodClusterAssembly(newName, newSize);
		// Check values
		assertEquals(newName, assembly.getName());
		assertEquals(defaultDesc, assembly.getDescription());
		assertEquals(defaultId, assembly.getId());
		assertEquals(newSize, assembly.getSize());
		assertEquals(type, assembly.getHDF5LWRTag());

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
		RodClusterAssembly object, equalObject, unEqualObject, transitiveObject;
		String name = "Billy";
		int size = 5;
		LWRRod rod = new LWRRod("Bob the rod");

		// Setup root object
		object = new RodClusterAssembly(name, size);
		object.addLWRRod(rod);
		object.setLWRRodLocation(rod.getName(), 0, 0);

		// Setup equalObject equal to object
		equalObject = new RodClusterAssembly(name, size);
		equalObject.addLWRRod(rod);
		equalObject.setLWRRodLocation(rod.getName(), 0, 0);

		// Setup transitiveObject equal to object
		transitiveObject = new RodClusterAssembly(name, size);
		transitiveObject.addLWRRod(rod);
		transitiveObject.setLWRRodLocation(rod.getName(), 0, 0);

		// Set its data, not equal to object
		unEqualObject = new RodClusterAssembly(name, size);
		// Uses the default rod

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
		RodClusterAssembly object, copyObject, clonedObject;
		String name = "Billy";
		int size = 5;
		LWRRod rod = new LWRRod("Bob the rod");

		// Setup root object
		object = new RodClusterAssembly(name, size);
		object.addLWRRod(rod);
		object.setLWRRodLocation(rod.getName(), 0, 0);

		// Run the copy routine
		copyObject = new RodClusterAssembly(1);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (RodClusterAssembly) object.clone();

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
		int size = 5;
		RodClusterAssembly assembly = new RodClusterAssembly(size);
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = assembly.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		LWRRod rod1 = new LWRRod("Rod1");
		LWRRod rod2 = new LWRRod("Rod2");
		LWRRod rod3 = new LWRRod("Rod3");
		GridLocation loc1 = new GridLocation(0, 0);
		GridLocation loc2 = new GridLocation(2, 0);
		GridLocation loc3 = new GridLocation(0, 3);
		double rodPitch = 4;
		String testFileName = "testWrite.h5";

		// Setup duplicate manager for comparison testing
		LWRGridManager manager = new LWRGridManager(size);
		manager.setName("LWRRod Grid");
		manager.addComponent(rod1, loc1);
		manager.addComponent(rod2, loc2);
		manager.addComponent(rod3, loc3);

		// Setup assembly
		assembly.setName(name);
		assembly.setId(id);
		assembly.setDescription(description);
		assembly.addLWRRod(rod1);
		assembly.addLWRRod(rod2);
		assembly.addLWRRod(rod3);
		assembly.setRodPitch(rodPitch);

		// Setup duplicate for assembly's grid location
		assembly.setLWRRodLocation(rod1.getName(), loc1.getRow(),
				loc1.getColumn());
		assembly.setLWRRodLocation(rod2.getName(), loc2.getRow(),
				loc2.getColumn());
		assembly.setLWRRodLocation(rod3.getName(), loc3.getRow(),
				loc3.getColumn());

		// Setup the HDF5 File
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ testFileName);
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
		assertNotNull(assembly.getWriteableChildren());
		// Check Children
		assertEquals(2, assembly.getWriteableChildren().size());
		assertTrue(rod3.equals(assembly.getWriteableChildren().get(0)
				.getWriteableChildren().get(0)));
		assertTrue(rod2.equals(assembly.getWriteableChildren().get(0)
				.getWriteableChildren().get(1)));
		assertTrue(rod1.equals(assembly.getWriteableChildren().get(0)
				.getWriteableChildren().get(2)));
		LWRGridManager otherManager = (LWRGridManager) assembly
				.getWriteableChildren().get(1);
		assertTrue(manager.equals(otherManager));

		// Check writing attributes
		H5Group h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		// Pass the group and file to the writer for attributes
		// See that it passes
		assertTrue(assembly.writeAttributes(h5File, h5Group));

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

			// Check Double Attribute - rodPitch
			attribute = (Attribute) h5Group.getMetadata().get(4);
			assertEquals(attribute.getName(), "rodPitch");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(rodPitch, ((double[]) attribute.getValue())[0], 1.2);
			// Reset Values
			attribute = null;
			attributeValue = null;

			// Check Integer Attribute - size
			attribute = (Attribute) h5Group.getMetadata().get(5);
			assertEquals(attribute.getName(), "size");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_INTEGER);
			assertEquals(size, ((int[]) attribute.getValue())[0]);
			// Reset Values
			attribute = null;
			attributeValue = null;

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Make sure the writeAttributes fail for invalid stuff
		assertFalse(assembly.writeAttributes(null, h5Group));
		assertFalse(assembly.writeAttributes(h5File, null));

		// Check Group Creation
		H5Group group = assembly.createGroup(h5File, h5Group);
		// See that the previous group has a group
		assertEquals(1, h5Group.getMemberList().size());
		// Check that it has the same name as the root assembly
		assertEquals(assembly.getName(), h5Group.getMemberList().get(0)
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
		int size = 5;
		RodClusterAssembly component = new RodClusterAssembly(size);
		RodClusterAssembly newComponent = new RodClusterAssembly(-1);
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		H5Group subGroup = null;
		LWRRod rod1 = new LWRRod("Rod1");
		LWRRod rod2 = new LWRRod("Rod2");
		LWRRod rod3 = new LWRRod("Rod3");
		GridLocation loc1 = new GridLocation(0, 0);
		GridLocation loc2 = new GridLocation(2, 0);
		GridLocation loc3 = new GridLocation(0, 3);
		double rodPitch = 4;

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.setRodPitch(rodPitch);

		component.addLWRRod(rod1);
		component.addLWRRod(rod2);
		component.addLWRRod(rod3);

		// Setup duplicate for assembly's grid location
		component.setLWRRodLocation(rod1.getName(), loc1.getRow(),
				loc1.getColumn());
		component.setLWRRodLocation(rod2.getName(), loc2.getRow(),
				loc2.getColumn());
		component.setLWRRodLocation(rod3.getName(), loc3.getRow(),
				loc3.getColumn());

		// Setup GridManager
		LWRGridManager manager = new LWRGridManager(size);
		manager.setName("LWRRod Grid");
		manager.addComponent(rod1, loc1);
		manager.addComponent(rod2, loc2);
		manager.addComponent(rod3, loc3);

		// Setup Composite
		LWRComposite composite = new LWRComposite();
		composite.setName("LWRRods");
		composite.setDescription("A Composite that contains many LWRRods.");
		composite.setId(1);
		composite.addComponent(rod1);
		composite.addComponent(rod2);
		composite.addComponent(rod3);

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

			// Setup size attribute
			HdfWriterFactory.writeIntegerAttribute(h5File, subGroup, "size",
					size);

			// Setup rodPitch attribute
			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup, "rodPitch",
					rodPitch);

			// Close group and then reopen
			h5File.close();
			h5File.open();
			parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			// Get the subGroup
			subGroup = (H5Group) parentH5Group.getMemberList().get(0);

			// Read information
			assertTrue(newComponent.readAttributes(subGroup));
			assertTrue(newComponent.readChild(manager));
			assertTrue(newComponent.readChild(composite));

			// Check with setup component
			assertTrue(component.equals(newComponent));

			// Try to break the readChild operation
			assertFalse(newComponent.readChild(null));
			assertTrue(newComponent.readChild(new LWRGridManager(size)));
			assertTrue(newComponent.readChild(new LWRComposite()));

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

			h5File.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		dataFile.delete();

	}
}