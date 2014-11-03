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
import java.util.ArrayList;

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
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWReactor;
import org.eclipse.ice.datastructures.updateableComposite.Component;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LWReactorTester {
	// An @BeforeClass that sets up the library path. This should be added to
	// the model or removed if it can be fixed for local machine
	@BeforeClass
	public static void beforeClass() {
		// begin-user-code

		// Set the path to the library
		// System.setProperty("java.library.path", "/usr/lib64");
		// System.setProperty("java.library.path", "/home/s4h/usr/local/lib64");
		// System.setProperty("java.library.path",
		// "/home/ICE/hdf-java/lib/linux");

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the constructor and it's default values.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code
		// Local Declarations
		LWReactor reactor;
		String defaultName = "LWReactor 1";
		String defaultDescription = "LWReactor 1's Description";
		int defaultId = 1;
		int defaultSize = 1;
		HDF5LWRTagType type = HDF5LWRTagType.LWREACTOR;

		// This test is to show the default value for a reactor when it is
		// created with a negative value.
		reactor = new LWReactor(-1);
		assertEquals(defaultSize, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(type, reactor.getHDF5LWRTag());

		// This test is to show the default value for a reactor when its created
		// with a zero value
		reactor = new LWReactor(0);
		assertEquals(defaultSize, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(type, reactor.getHDF5LWRTag());

		// This is a test to show a valid creation of a reactor
		reactor = new LWReactor(17);
		assertEquals(17, reactor.getSize());
		assertEquals(defaultName, reactor.getName());
		assertEquals(defaultDescription, reactor.getDescription());
		assertEquals(defaultId, reactor.getId());
		assertEquals(type, reactor.getHDF5LWRTag());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * ﻿This operation demonstrates the behaviors listed with the overridden
	 * composite implementations from LWRcomposite.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCompositeImplementations() {
		// begin-user-code

		// Local Declarations
		int reactorSize = 17;
		LWReactor reactor;
		ArrayList<String> compNames = new ArrayList<String>();
		ArrayList<Component> components = new ArrayList<Component>();
		int numberOfDefaultComponents = 0;

		// Setup the default number of components
		numberOfDefaultComponents = components.size();

		// Check the default Composite size and attributes on PWRAssembly
		reactor = new LWReactor(reactorSize);

		// Has a size of numberOfDefaultComponents
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());
		// Check general getters for the other pieces
		assertTrue(compNames.equals(reactor.getComponentNames()));
		assertTrue(components.equals(reactor.getComponents()));

		// These operations will show that these will not work for this class

		// Check addComponent
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());
		reactor.addComponent(new LWRComposite());
		// No size change!
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());

		// Check removeComponent - id
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());
		reactor.removeComponent(1);
		// No size change!
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());

		// Check remove component - name
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());

		// No size change!
		assertEquals(numberOfDefaultComponents, reactor.getNumberOfComponents());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks equals() and hashCode() operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		// Local Declarations
		LWReactor object, equalObject, unEqualObject, transitiveObject;
		int size = 5;
		int unEqualSize = 7;

		// Setup root object
		object = new LWReactor(size);

		// Setup equalObject equal to object
		equalObject = new LWReactor(size);

		// Setup transitiveObject equal to object
		transitiveObject = new LWReactor(size);

		// Set its data, not equal to object
		unEqualObject = new LWReactor(unEqualSize);

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the copy and clone routines.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		// Local declarations
		LWReactor object;
		LWReactor copyObject = new LWReactor(0), clonedObject;

		int size = 5;

		// Setup root object
		object = new LWReactor(size);

		// Run the copy routine
		copyObject = new LWReactor(0);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (LWReactor) object.clone();

		// Check contents
		assertTrue(object.equals(clonedObject));

		// Pass null for the copy routine
		copyObject.copy(null);

		// Show that nothing as changed
		assertTrue(object.equals(copyObject));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the HDF5 writing operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkHDF5Writeables() {
		// begin-user-code

		// Local Declarations
		int size = 5;
		LWReactor reactor = new LWReactor(size);
		String name = "A plain reactor";
		String description = "Its very plain.";
		int id = 4;
		HDF5LWRTagType tag = reactor.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		String testFileName = "testWrite.h5";

		// Setup reactor
		reactor.setName(name);
		reactor.setId(id);
		reactor.setDescription(description);

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
		assertNotNull(reactor.getWriteableChildren());
		assertEquals(size, reactor.getSize());

		// Check writing attributes
		H5Group h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		// Pass the group and file to the writer for attributes
		// See that it passes
		assertTrue(reactor.writeAttributes(h5File, h5Group));

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

			// Check Integer Attribute - size
			attribute = (Attribute) h5Group.getMetadata().get(4);
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
		assertFalse(reactor.writeAttributes(null, h5Group));
		assertFalse(reactor.writeAttributes(h5File, null));

		// Check dataSet.
		assertFalse(reactor.writeDatasets(null, null));

		// Check Group Creation
		H5Group group = reactor.createGroup(h5File, h5Group);
		// See that the previous group has a group
		assertEquals(1, h5Group.getMemberList().size());
		// Check that it has the same name as the root reactor
		assertEquals(reactor.getName(), h5Group.getMemberList().get(0)
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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Removes the test.h5 file after the tests fails (to keep the workspace
	 * clean).
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@AfterClass
	public static void afterClass() {
		// begin-user-code

		// Cleans up the datafile if it exists due to a failed test
		File dataFile = new File(System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "test.h5");

		// If it exists, remove it
		if (dataFile.exists()) {
			dataFile.delete();
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the HDF5 readable operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkHDF5Readables() {
		// begin-user-code

		// Local Declarations
		int size = 5;
		LWReactor component = new LWReactor(size);
		LWReactor newComponent = new LWReactor(-1);
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		H5Group subGroup = null;
		double rodPitch = 4;

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);

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
		// assertTrue(component.readChild(null));

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

		// end-user-code

		// end-user-code
	}
}