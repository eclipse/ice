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
import java.net.URI;

import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRGridManager;

import static org.junit.Assert.*;
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

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class tests LWRComposite.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LWRCompositeTester {
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
		// Local declarations
		LWRComposite composite;
		HDF5LWRTagType type = HDF5LWRTagType.LWRCOMPOSITE;

		// Default values
		String defaultName = "Composite 1";
		String defaultDescription = "Composite 1's Description";
		int defaultId = 1;

		// Check nullary constructor
		composite = new LWRComposite();
		assertEquals(defaultName, composite.getName());
		assertEquals(defaultDescription, composite.getDescription());
		assertEquals(defaultId, composite.getId());
		assertEquals(type, composite.getHDF5LWRTag());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the getters and setters for component.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkComponent() {
		// begin-user-code
		// Local Declarations
		int compositeSize = 17;
		LWRComposite composite = new LWRComposite();
		LWRComponent testComponent = new LWRComponent(), testComponent2 = new LWRComponent(), testComponent3 = new LWRComponent();
		String testComponentName = "Bob";
		String testComponentName2 = "Bill!";
		int rowLoc1 = 5, colLoc1 = 5;
		int rowLoc2 = 6, colLoc2 = 6;
		int testComponentId = 1000001;

		// Set the ids on the test components
		testComponent.setId(1);
		testComponent2.setId(2);
		testComponent3.setId(3);

		composite = new LWRComposite();
		// Set the second component name
		testComponent2.setName(testComponentName2);

		// Check the names, should be empty!
		assertEquals(0, composite.getComponentNames().size());

		// Try to get by name - valid string, empty string, and null
		assertNull(composite
				.getComponent("validNameThatDoesNotExistInThere152423"));
		assertNull(composite.getComponent(""));
		assertNull(composite.getComponent(null));

		// Set the name
		testComponent.setName(testComponentName);

		// Add to the composite
		composite.addComponent(testComponent);

		// Check the getting of a component
		assertTrue(testComponent.equals(composite
				.getComponent(testComponentName)));

		// Add it in there
		composite.addComponent(testComponent2);

		// Check there are two in there, with separate names
		assertEquals(2, composite.getComponents().size());
		assertEquals(2, composite.getComponentNames().size());
		assertEquals(testComponent.getName(), composite.getComponentNames()
				.get(0));
		assertEquals(testComponent2.getName(), composite.getComponentNames()
				.get(1));

		// Check values - see the components are different and they reside in
		// the table correctly
		assertTrue(testComponent.equals(composite
				.getComponent(testComponentName)));
		assertTrue(testComponent2.equals(composite.getComponent(testComponent2
				.getName())));

		// Check the names, should contain 2!
		assertEquals(2, composite.getComponentNames().size());
		assertEquals(testComponentName, composite.getComponentNames().get(0));
		assertEquals(testComponentName2, composite.getComponentNames().get(1));

		// Check operation for null
		composite.addComponent(null);
		assertNull(composite.getComponent(null)); // Make sure null does
													// not work!

		// Finally, demonstrate what happens when a component of the same name
		// is added, it should not overwrite the previous item in the table!
		testComponent3.setName(testComponentName); // Same name as the other
													// component
		testComponent3.setId(testComponentId); // Id should differ from
												// testComponent!
		assertFalse(testComponent.getId() == testComponentId);

		// Overwrite in table
		composite.addComponent(testComponent3);

		// Check that the object has not been overwritten
		assertTrue(testComponent.equals(composite
				.getComponent(testComponentName)));
		assertFalse(testComponent3.equals(composite
				.getComponent(testComponentName)));

		// Test to remove components from the composite
		composite.removeComponent(null);
		composite.removeComponent("");
		composite
				.removeComponent("!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^");

		// Nothing was removed
		// Check the names, should contain 2!
		assertEquals(2, composite.getComponentNames().size());
		assertEquals(testComponentName, composite.getComponentNames().get(0));
		assertEquals(testComponentName2, composite.getComponentNames().get(1));

		// Remove the second component
		composite.removeComponent(testComponent2.getName());

		// Check that it does not exist in the location or getting the name
		assertNull(composite.getComponent(testComponent2.getName()));

		// Check that the first exists
		assertEquals(1, composite.getComponentNames().size());
		assertEquals(testComponentName, composite.getComponentNames().get(0));
		assertEquals(1, composite.getNumberOfComponents());
		assertTrue(testComponent.equals(composite.getComponent(1)));
		assertNull(composite.getComponent(2));

		// Remove the composite, check that it was removed from the map!
		composite.removeComponent(1);
		assertNull(composite.getComponent(testComponent.getName()));
		assertEquals(0, composite.getComponents().size());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the equals and hashCode operations.
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
		LWRComposite object, equalObject, unEqualObject, transitiveObject;
		LWRComponent component = new LWRComponent("Billy the Plummer");
		LWRComponent component2 = new LWRComponent("Billy the Plummer2");

		// Setup root object
		object = new LWRComposite();
		object.addComponent(component);
		object.addComponent(component2);

		// Setup equalObject equal to object
		equalObject = new LWRComposite();
		equalObject.addComponent(component);
		equalObject.addComponent(component2);

		// Setup transitiveObject equal to object
		transitiveObject = new LWRComposite();
		transitiveObject.addComponent(component);
		transitiveObject.addComponent(component2);

		// Set its data, not equal to object
		// Does not contain components!
		unEqualObject = new LWRComposite();

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
	 * This operation checks the copying and clone operations.
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
		LWRComposite object;
		LWRComposite copyObject = new LWRComposite(), clonedObject;

		// Values
		String name = "A LWRComponent!@!@#!#@56483";
		String description = "Description !@#!@#!@#!46546484328";
		int id = 68468431;
		LWRComponent component = new LWRComponent("Billy!");
		LWRComponent component2 = new LWRComponent("Billy2!");

		// Initialize and Setup Object to test
		object = new LWRComposite();
		object.setName(name);
		object.setId(id);
		object.setDescription(description);
		object.addComponent(component);
		object.addComponent(component2);

		// Run the copy routine
		copyObject = new LWRComposite();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (LWRComposite) object.clone();

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
		LWRComposite composite = new LWRComposite();
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = composite.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		LWRComponent component1 = new LWRComponent("Component 1");
		LWRComponent component2 = new LWRComponent("Component 2");
		String testFileName = "testWrite.h5";

		// Setup composite
		composite.setName(name);
		composite.setId(id);
		composite.setDescription(description);
		composite.addComponent(component1);
		composite.addComponent(component2);

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
		assertNotNull(composite.getWriteableChildren());
		// Check Children
		assertEquals(composite.getComponents().size(), composite
				.getWriteableChildren().size());
		assertTrue(component2.equals(composite.getWriteableChildren().get(0)));
		assertTrue(component1.equals(composite.getWriteableChildren().get(1)));

		// Check writing attributes
		H5Group h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		// Pass the group and file to the writer for attributes
		// See that it passes
		assertTrue(composite.writeAttributes(h5File, h5Group));

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

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Make sure the writeAttributes fail for invalid stuff
		assertFalse(composite.writeAttributes(null, h5Group));
		assertFalse(composite.writeAttributes(h5File, null));

		// Check Group Creation
		H5Group group = composite.createGroup(h5File, h5Group);
		// See that the previous group has a group
		assertEquals(1, h5Group.getMemberList().size());
		// Check that it has the same name as the root composite
		assertEquals(composite.getName(), h5Group.getMemberList().get(0)
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
		LWRComposite component = new LWRComposite();
		LWRComposite newComponent = new LWRComposite();
		String name = "Fancy composite";
		String description = "It has stuff";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		H5Group subGroup = null;
		LWRComponent c1 = new LWRComponent("Component1"), c2 = new LWRComponent(
				"Component2");

		// Setup composite
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.addComponent(c1);
		component.addComponent(c2);

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
			assertTrue(newComponent.readChild(c1));
			assertTrue(newComponent.readChild(c2));

			// Check with setup component
			assertTrue(component.equals(newComponent));

			// Try to break the readChild operation
			assertFalse(newComponent.readChild(null));

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

			// Close the h5File.
			h5File.close();
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

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
}