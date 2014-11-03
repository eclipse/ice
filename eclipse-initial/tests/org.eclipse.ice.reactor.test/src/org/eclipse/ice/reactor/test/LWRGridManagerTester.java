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
import java.util.Vector;

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5CompoundDS;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;
import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRGridManager;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class checks the operations on LWRGridManager.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class LWRGridManagerTester {
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
	 * This operation tests the constructor and default values.
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
		LWRGridManager manager;
		int defaultSize = 1;
		String defaultName = "LWRGridManager 1";
		String defaultDescription = "LWRGridManager 1's Description";
		int defaultId = 1;
		HDF5LWRTagType type = HDF5LWRTagType.LWRGRIDMANAGER;

		// New
		int newSize = 5;

		// Check normal construction
		manager = new LWRGridManager(newSize);
		// Check values
		assertEquals(newSize, manager.getSize());
		assertEquals(defaultName, manager.getName());
		assertEquals(defaultDescription, manager.getDescription());
		assertEquals(defaultId, manager.getId());
		assertEquals(type, manager.getHDF5LWRTag());

		// Check defaultSize construction
		manager = new LWRGridManager(defaultSize);
		// Check values
		assertEquals(defaultSize, manager.getSize());
		assertEquals(defaultName, manager.getName());
		assertEquals(defaultDescription, manager.getDescription());
		assertEquals(defaultId, manager.getId());
		assertEquals(type, manager.getHDF5LWRTag());

		// Check illegal size - zero
		manager = new LWRGridManager(0);
		// Check value - defaults
		assertEquals(defaultSize, manager.getSize()); // Defaults
		assertEquals(defaultName, manager.getName());
		assertEquals(defaultDescription, manager.getDescription());
		assertEquals(defaultId, manager.getId());
		assertEquals(type, manager.getHDF5LWRTag());

		// Check illegal size - negative
		manager = new LWRGridManager(-1);
		// Check values - defaults
		assertEquals(defaultSize, manager.getSize()); // Defaults
		assertEquals(defaultName, manager.getName());
		assertEquals(defaultDescription, manager.getDescription());
		assertEquals(defaultId, manager.getId());
		assertEquals(type, manager.getHDF5LWRTag());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the adding and removing of components.
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
		LWRGridManager manager;
		int size = 10;

		// Setup GridLocations values
		GridLocation location1, location2, location3, location4;
		int row1 = 1, row2 = 5, row3 = 9, row4 = 22;
		int col1 = 1, col2 = 5, col3 = 8, col4 = 15;

		// Use LWRComponent
		LWRComponent component1, component2, component3, component4;
		String component1Name = "Component 1";
		String component2Name = "Component 2";
		String component3Name = "Component 3";
		String component4Name = "Component 4";

		// Setup the LWRComponents
		component1 = new LWRComponent(component1Name);
		component2 = new LWRComponent(component2Name);
		component3 = new LWRComponent(component3Name);
		component4 = new LWRComponent(component4Name);

		// Set ids
		component1.setId(1);
		component2.setId(2);
		component3.setId(3);
		component4.setId(4);

		// Null args
		LWRComponent nullComponent = null;
		GridLocation nullLocation = null;

		// Create a manager
		manager = new LWRGridManager(size);

		// Create a gridLocation and add it to the list for Component1
		location1 = new GridLocation(row1, col1);

		// Add it, and check to see if it exists
		manager.addComponent(component1, location1);
		// See if it exists and is equal
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));

		// add 2 more
		location2 = new GridLocation(row2, col2);
		manager.addComponent(component2, location2);

		location3 = new GridLocation(row3, col3);
		manager.addComponent(component3, location3);

		// See if they both exist
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertTrue(component2.getName().equals(
				manager.getComponentName(location2)));
		assertTrue(component3.getName().equals(
				manager.getComponentName(location3)));

		// Try to add something on top of something that already exists
		manager.addComponent(component4, location2);
		// Show that it does not exist, and no other items are messed with
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertTrue(component2.getName().equals(
				manager.getComponentName(location2)));
		assertTrue(component3.getName().equals(
				manager.getComponentName(location3)));

		// Try to add with a location out of the size range
		location4 = new GridLocation(row4, col4);
		manager.addComponent(component4, location4);
		// Show that it does not exist, and no other items are messed with
		assertNull(manager.getComponentName(location4));
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertTrue(component2.getName().equals(
				manager.getComponentName(location2)));
		assertTrue(component3.getName().equals(
				manager.getComponentName(location3)));

		// Try to add with a location of null
		location4 = new GridLocation(row4, col4);
		manager.addComponent(component4, nullLocation);
		// Show that it does not exist, and no other items are messed with
		assertNull(manager.getComponentName(nullLocation));
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertTrue(component2.getName().equals(
				manager.getComponentName(location2)));
		assertTrue(component3.getName().equals(
				manager.getComponentName(location3)));

		// Try to add with a component of null
		location4 = new GridLocation(row4, col4);
		manager.addComponent(nullComponent, nullLocation);
		// Show that it does not exist, and no other items are messed with
		assertNull(manager.getComponentName(nullLocation));
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertTrue(component2.getName().equals(
				manager.getComponentName(location2)));
		assertTrue(component3.getName().equals(
				manager.getComponentName(location3)));

		// Try to remove based on location - remove component 3
		manager.removeComponent(location3);
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertTrue(component2.getName().equals(
				manager.getComponentName(location2)));
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Try to remove based on location - bad location (location does not
		// exist)
		manager.removeComponent(location3);
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertTrue(component2.getName().equals(
				manager.getComponentName(location2)));
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Try to removed based on location - out of range
		manager.removeComponent(location4);
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertTrue(component2.getName().equals(
				manager.getComponentName(location2)));
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Try to remove - null Location
		manager.removeComponent(nullLocation);
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertTrue(component2.getName().equals(
				manager.getComponentName(location2)));
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Try to remove - null Component
		manager.removeComponent(nullComponent);
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertTrue(component2.getName().equals(
				manager.getComponentName(location2)));
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Remove based on Component
		manager.removeComponent(component2);
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertNull(manager.getComponentName(location2)); // Does not exist
															// anymore!
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Remove a component that does not exist
		manager.removeComponent(component2);
		assertTrue(component1.getName().equals(
				manager.getComponentName(location1)));
		assertNull(manager.getComponentName(location2)); // Does not exist
															// anymore!
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

		// Remove the last component
		manager.removeComponent(component1);
		assertNull(manager.getComponentName(location1)); // Does not exist
															// anymore!
		assertNull(manager.getComponentName(location2)); // Does not exist
															// anymore!
		assertNull(manager.getComponentName(location3)); // Does not exist
															// anymore!

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
		LWRGridManager object, equalObject, unEqualObject, transitiveObject;
		LWRComponent component, component2;
		GridLocation location1, location2;

		// Setup Components
		component = new LWRComponent("Billy Bob");
		component2 = new LWRComponent("Bobby Bill");

		// Setup Locations
		location1 = new GridLocation(1, 6);
		location2 = new GridLocation(3, 4);

		// Setup root object
		object = new LWRGridManager(15);
		object.addComponent(component, location1);
		object.addComponent(component2, location2);

		// Setup equalObject equal to object
		equalObject = new LWRGridManager(15);
		equalObject.addComponent(component, location1);
		equalObject.addComponent(component2, location2);

		// Setup transitiveObject equal to object
		transitiveObject = new LWRGridManager(15);
		transitiveObject.addComponent(component, location1);
		transitiveObject.addComponent(component2, location2);

		// Set its data, not equal to object
		unEqualObject = new LWRGridManager(15);
		unEqualObject.addComponent(component2, location1);

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

		// Local Declarations
		LWRGridManager object, copyObject, clonedObject;
		LWRComponent component, component2;
		GridLocation location1, location2;

		// Setup Components
		component = new LWRComponent("Billy Bob");
		component2 = new LWRComponent("Bobby Bill");

		// Setup Locations
		location1 = new GridLocation(1, 6);
		location2 = new GridLocation(3, 4);

		// Setup root object
		object = new LWRGridManager(15);
		object.addComponent(component, location1);
		object.addComponent(component2, location2);

		// Run the copy routine
		copyObject = new LWRGridManager(2);
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (LWRGridManager) object.clone();

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
	 * Checks the dataprovider operations or operations specific for obtaining a
	 * data provider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkDataProvider() {
		// begin-user-code

		// Local Declarations
		int size = 5;
		LWRGridManager manager = new LWRGridManager(size);

		String name1 = "Foo1";
		String name2 = "Foo2";
		LWRComponent component1 = new LWRComponent(name1);
		LWRComponent component2 = new LWRComponent(name2);
		GridLocation location1 = new GridLocation(2, 3);
		GridLocation location2 = new GridLocation(3, 3);
		GridLocation location3 = new GridLocation(1, 3);

		// Try to get locations when there are no GridLocations or names
		assertEquals(0, manager.getGridLocationsAtName(name1).size());
		assertNull(manager.getDataProviderAtLocation(location1));

		// Add to grid
		manager.addComponent(component1, location1);

		// Try to get locations
		assertEquals(1, manager.getGridLocationsAtName(name1).size());
		assertTrue(manager.getGridLocationsAtName(name1).get(0)
				.equals(location1));
		assertNotNull(manager.getDataProviderAtLocation(location1));

		// Add to another location
		manager.addComponent(component1, location2);

		// Try to get locations
		assertEquals(2, manager.getGridLocationsAtName(name1).size());
		assertTrue(manager.getGridLocationsAtName(name1).get(0)
				.equals(location1));
		assertTrue(manager.getGridLocationsAtName(name1).get(1)
				.equals(location2));
		assertNotNull(manager.getDataProviderAtLocation(location1));
		assertNotNull(manager.getDataProviderAtLocation(location2));

		// Add to another location - different component
		manager.addComponent(component2, location3);

		// Try to get locations
		assertEquals(2, manager.getGridLocationsAtName(name1).size());
		assertTrue(manager.getGridLocationsAtName(name1).get(0)
				.equals(location1));
		assertTrue(manager.getGridLocationsAtName(name1).get(1)
				.equals(location2));

		// Check second component
		assertEquals(1, manager.getGridLocationsAtName(name2).size());
		assertTrue(manager.getGridLocationsAtName(name2).get(0)
				.equals(location3));

		// Check all added locations
		assertNotNull(manager.getDataProviderAtLocation(location1));
		assertNotNull(manager.getDataProviderAtLocation(location2));
		assertNotNull(manager.getDataProviderAtLocation(location3));

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

		// Setup GridLocation data
		// Setup LWRData
		String feature1 = "Feature 1";
		String feature2 = "Feature 2";
		double time1 = 1.0, time2 = 3.0, time3 = 3.5;
		LWRData data1, data2, data3, data4, data5;
		ArrayList<Double> position1 = new ArrayList<Double>(), position2 = new ArrayList<Double>(), position3 = new ArrayList<Double>(), position4 = new ArrayList<Double>(), position5 = new ArrayList<Double>();

		// Setup Positions

		// Setup Position 1
		position1.add(0.0);
		position1.add(1.0);
		position1.add(0.0);

		// Setup Position 2
		position2.add(0.0);
		position2.add(1.0);
		position2.add(4.0);

		// Setup Position 3
		position3.add(1.0);
		position3.add(1.0);
		position3.add(0.0);

		// Setup Position 4
		position4.add(0.0);
		position4.add(1.0);
		position4.add(1.0);

		// Setup Position 5
		position4.add(0.0);
		position4.add(1.0);
		position4.add(3.0);

		// Setup data1
		data1 = new LWRData(feature1);
		data1.setPosition(position1);
		data1.setValue(1.0);
		data1.setUncertainty(1.5);
		data1.setUnits("Units " + 1);

		// Setup data2
		data2 = new LWRData(feature1);
		data2.setPosition(position2);
		data2.setValue(2.0);
		data2.setUncertainty(2.5);
		data2.setUnits("Units " + 2);

		// Setup data3
		data3 = new LWRData(feature1);
		data3.setPosition(position3);
		data3.setValue(3.0);
		data3.setUncertainty(3.5);
		data3.setUnits("Units " + 3);

		// Setup data4
		data4 = new LWRData(feature1);
		data4.setPosition(position4);
		data4.setValue(4.0);
		data4.setUncertainty(4.5);
		data4.setUnits("Units " + 4);

		// Setup data5
		data5 = new LWRData(feature2);
		data5.setPosition(position5);
		data5.setValue(5.0);
		data5.setUncertainty(5.5);
		data5.setUnits("Units " + 5);

		// Local Declarations
		int size = 5;
		LWRGridManager manager = new LWRGridManager(size);
		String name = "GRID!";
		String description = "LABELS!";
		int id = 4;
		HDF5LWRTagType tag = manager.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		LWRComponent component1 = new LWRComponent("Component 1");
		LWRComponent component2 = new LWRComponent("Component 2");
		GridLocation location1 = new GridLocation(0, 1);
		GridLocation location2 = new GridLocation(2, 2);
		ArrayList<String> list = new ArrayList<String>();
		String testFileName = "testWrite.h5";

		// Add components to list
		list.add(component1.getName());
		list.add(component2.getName());

		// Setup Manager
		manager.setName(name);
		manager.setId(id);
		manager.setDescription(description);
		manager.addComponent(component1, location1);
		manager.addComponent(component2, location2);

		// Add data to the location
		location1.getLWRDataProvider().addData(data1, time1);
		location1.getLWRDataProvider().addData(data2, time1);
		location1.getLWRDataProvider().addData(data3, time2);
		location1.getLWRDataProvider().addData(data4, time3);
		location1.getLWRDataProvider().addData(data5, time3);

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
		assertNull(manager.getWriteableChildren());

		// Check writing attributes
		H5Group h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();
		// Pass the group and file to the writer for attributes
		// See that it passes
		assertTrue(manager.writeAttributes(h5File, h5Group));

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

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// Make sure the writeAttributes fail for invalid stuff
		assertFalse(manager.writeAttributes(null, h5Group));
		assertFalse(manager.writeAttributes(h5File, null));

		// Check dataSet. Pass null to show it will fail.
		assertFalse(manager.writeDatasets(null, null));
		assertFalse(manager.writeDatasets(null, h5Group));
		assertFalse(manager.writeDatasets(h5File, null));

		// Perform a dataSet Write
		assertTrue(manager.writeDatasets(h5File, h5Group));

		// Close group and then reopen
		try {
			h5File.close();
			h5File.open();
		} catch (Exception e1) {
			e1.printStackTrace();
			dataFile.delete();
			fail();
		}

		h5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
				.getRootNode()).getUserObject();

		// Check the values of the Positions
		assertEquals(2, h5Group.getMemberList().size());
		// Check the values of that dataset

		// Names of the root group
		assertEquals("Positions", h5Group.getMemberList().get(0).getName());

		// StatePointData for the grid manager
		assertEquals("State Point Data", h5Group.getMemberList().get(1)
				.getName());

		H5Group positions = (H5Group) h5Group.getMemberList().get(0);

		// Check size

		// There are two positions with the third and forth group handling the
		// units
		assertEquals(4, positions.getMemberList().size());

		// Position 1
		assertEquals(
				"Position " + location1.getRow() + " " + location1.getColumn(),
				positions.getMemberList().get(0).getName());

		// Position 2
		assertEquals(
				"Position " + location2.getRow() + " " + location2.getColumn(),
				positions.getMemberList().get(1).getName());

		// Position Names table
		assertEquals("Simple Position Names Table", positions.getMemberList()
				.get(2).getName());

		// Units table
		assertEquals("Units Table", positions.getMemberList().get(3).getName());

		// Check that they are an instance of group
		assertTrue(positions.getMemberList().get(0) instanceof Group);
		assertTrue(positions.getMemberList().get(1) instanceof Group);
		assertTrue(positions.getMemberList().get(2) instanceof Dataset);
		assertTrue(positions.getMemberList().get(3) instanceof Dataset);

		// Check internal information
		try {

			// Check the dataset
			Dataset unitList = (Dataset) positions.getMemberList().get(3);
			Dataset namesList = (Dataset) positions.getMemberList().get(2);

			// Check values
			unitList.init();
			namesList.init();

			// Get data
			Object unitListData = unitList.getData();
			Object namesListData = namesList.getData();

			// Cast to array of Strings
			String[] arrayStrings = (String[]) unitListData;
			String[] arrayNames = (String[]) namesListData;

			// Check values
			assertEquals(data1.getUnits(), arrayStrings[0]);
			assertEquals(data2.getUnits(), arrayStrings[1]);
			assertEquals(data3.getUnits(), arrayStrings[2]);
			assertEquals(data4.getUnits(), arrayStrings[3]);
			assertEquals(data5.getUnits(), arrayStrings[4]);

			assertEquals(component1.getName(), arrayNames[0]);
			assertEquals(component2.getName(), arrayNames[1]);

			// Check the attributes - get the positions
			H5Group h5Position1 = (H5Group) positions.getMemberList().get(0);
			H5Group h5Position2 = (H5Group) positions.getMemberList().get(1);

			assertEquals(0, h5Position1.getMetadata().size());
			assertEquals(0, h5Position2.getMetadata().size());

			// Check Data
			Dataset positionsDataset1 = HdfReaderFactory.getDataset(
					h5Position1, "Position Dataset");
			Dataset positionsDataset2 = HdfReaderFactory.getDataset(
					h5Position2, "Position Dataset");

			// Convert data
			int[] positionData1 = new int[3];
			int[] positionData2 = new int[3];
			positionData1 = (int[]) positionsDataset1.getData();
			positionData2 = (int[]) positionsDataset2.getData();

			// Check values
			assertEquals(location1.getRow(), positionData1[0]);
			assertEquals(location1.getColumn(), positionData1[1]);
			assertEquals(location2.getRow(), positionData2[0]);
			assertEquals(location2.getColumn(), positionData2[1]);
			assertEquals(0, positionData1[2]);
			assertEquals(1, positionData2[2]);

			// Check for subgroups. These subgroups represent if they have
			// LWRData or not
			assertEquals(4, h5Position1.getMemberList().size());
			assertEquals(1, h5Position2.getMemberList().size());

			// Get the IDataGroup
			H5Group dataGroup = (H5Group) h5Position1;

			// Check the size of the data groups
			assertEquals(4, dataGroup.getMemberList().size());

			// Check the names of the groups. These should reflect the number of
			// time steps
			assertEquals("Position Dataset", h5Position2.getMemberList().get(0)
					.getName());

			assertEquals("Position Dataset", dataGroup.getMemberList().get(0)
					.getName());
			assertEquals("TimeStep: 0", dataGroup.getMemberList().get(1)
					.getName());
			assertEquals("TimeStep: 1", dataGroup.getMemberList().get(2)
					.getName());
			assertEquals("TimeStep: 2", dataGroup.getMemberList().get(3)
					.getName());

			// Get the TimeStep Groups and check contents
			H5Group timeGroup1 = (H5Group) dataGroup.getMemberList().get(1);
			H5Group timeGroup2 = (H5Group) dataGroup.getMemberList().get(2);
			H5Group timeGroup3 = (H5Group) dataGroup.getMemberList().get(3);

			// Check that there is a group and attributes
			assertEquals(2, timeGroup1.getMemberList().size());
			assertEquals(2, timeGroup2.getMemberList().size());
			assertEquals(4, timeGroup3.getMemberList().size());
			assertEquals(1, timeGroup1.getMetadata().size());
			assertEquals(1, timeGroup2.getMetadata().size());
			assertEquals(1, timeGroup3.getMetadata().size());

			// Check Attributes on timeGroups

			// Check timeGroup1
			// Check time attribute
			attribute = (Attribute) timeGroup1.getMetadata().get(0);
			assertEquals(attribute.getName(), "time");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(time1, ((double[]) attribute.getValue())[0], 0.0);

			// Check timeGroup2
			// Check time attribute
			attribute = (Attribute) timeGroup2.getMetadata().get(0);
			assertEquals(attribute.getName(), "time");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(time2, ((double[]) attribute.getValue())[0], 0.0);

			// Check timeGroup3
			// Check time attribute
			attribute = (Attribute) timeGroup3.getMetadata().get(0);
			assertEquals(attribute.getName(), "time");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_FLOAT);
			assertEquals(time3, ((double[]) attribute.getValue())[0], 0.0);

			// Get the Features off the timeGroups
			Dataset featureGroup1Data = (Dataset) timeGroup1.getMemberList()
					.get(0);
			Dataset featureGroup1Head = (Dataset) timeGroup1.getMemberList()
					.get(1);
			Dataset featureGroup2Data = (Dataset) timeGroup2.getMemberList()
					.get(0);
			Dataset featureGroup2Head = (Dataset) timeGroup2.getMemberList()
					.get(1);

			// Feature Group 3 gets two SEPARATE compound datasets because of
			// different features within the same time
			Dataset featureGroup3aData = (Dataset) timeGroup3.getMemberList()
					.get(0);
			Dataset featureGroup3aHead = (Dataset) timeGroup3.getMemberList()
					.get(1);
			Dataset featureGroup3bData = (Dataset) timeGroup3.getMemberList()
					.get(2);
			Dataset featureGroup3bHead = (Dataset) timeGroup3.getMemberList()
					.get(3);

			// Check information on featureGroups
			assertEquals("Feature 1 dataTable", featureGroup1Data.getName());
			assertEquals("Feature 1 headTable", featureGroup1Head.getName());
			assertEquals("Feature 1 dataTable", featureGroup2Data.getName());
			assertEquals("Feature 1 headTable", featureGroup2Head.getName());
			assertEquals("Feature 1 dataTable", featureGroup3aData.getName());
			assertEquals("Feature 1 headTable", featureGroup3aHead.getName());
			assertEquals("Feature 2 dataTable", featureGroup3bData.getName());
			assertEquals("Feature 2 headTable", featureGroup3bHead.getName());

			// Call init to get data
			featureGroup1Data.init();
			featureGroup1Head.init();
			featureGroup2Data.init();
			featureGroup2Head.init();
			featureGroup3aData.init();
			featureGroup3aHead.init();
			featureGroup3bData.init();
			featureGroup3bHead.init();

			// Confirm the sizes of the data
			assertEquals(featureGroup1Data.getDims()[0], 2);
			assertEquals(featureGroup1Data.getDims()[1], 5);
			assertEquals(featureGroup2Data.getDims()[0], 1);
			assertEquals(featureGroup2Data.getDims()[1], 5);
			assertEquals(featureGroup3aData.getDims()[0], 1);
			assertEquals(featureGroup3aData.getDims()[1], 5);
			assertEquals(featureGroup3bData.getDims()[0], 1);
			assertEquals(featureGroup3bData.getDims()[1], 5);

			// Confirm the sizes of the head
			assertEquals(featureGroup1Head.getDims()[0], 2);
			assertEquals(featureGroup1Head.getDims()[1], 2);
			assertEquals(featureGroup2Head.getDims()[0], 1);
			assertEquals(featureGroup2Head.getDims()[1], 2);
			assertEquals(featureGroup3aHead.getDims()[0], 1);
			assertEquals(featureGroup3aHead.getDims()[1], 2);
			assertEquals(featureGroup3bHead.getDims()[0], 1);
			assertEquals(featureGroup3bHead.getDims()[1], 2);

			// Check the contents of those feature groups

			// HDF5 java library converts the 2d data into 1 dimension on a
			// read. luckily we know the width and length by dimensions passed.
			double[] feature1Data1D = (double[]) featureGroup1Data.getData();
			long[] feature1Head1D = (long[]) featureGroup1Head.getData();
			double[] feature2Data1D = (double[]) featureGroup2Data.getData();
			long[] feature2Head1D = (long[]) featureGroup2Head.getData();

			double[] feature3aData1D = (double[]) featureGroup3aData.getData();
			long[] feature3aHead1D = (long[]) featureGroup3aHead.getData();

			double[] feature3bData1D = (double[]) featureGroup3bData.getData();
			long[] feature3bHead1D = (long[]) featureGroup3bHead.getData();

			// Confirm lengths are equivalent
			assertEquals(
					featureGroup1Data.getDims()[0]
							* featureGroup1Data.getDims()[1],
					feature1Data1D.length);
			// Reads as Value, Uncertainity, xpos, ypos, zpos - Data1
			assertEquals(feature1Data1D[0], data1.getValue(), 0.0);
			assertEquals(feature1Data1D[1], data1.getUncertainty(), 0.0);
			assertEquals(feature1Data1D[2], data1.getPosition().get(0), 0.0);
			assertEquals(feature1Data1D[3], data1.getPosition().get(1), 0.0);
			assertEquals(feature1Data1D[4], data1.getPosition().get(2), 0.0);

			// Reads as Value, Uncertainity, xpos, ypos, zpos - Data2
			assertEquals(feature1Data1D[5], data2.getValue(), 0.0);
			assertEquals(feature1Data1D[6], data2.getUncertainty(), 0.0);
			assertEquals(feature1Data1D[7], data2.getPosition().get(0), 0.0);
			assertEquals(feature1Data1D[8], data2.getPosition().get(1), 0.0);
			assertEquals(feature1Data1D[9], data2.getPosition().get(2), 0.0);

			// Check Head data - reads DataTableId and then units
			// Confirm the data length is correct
			assertEquals(
					featureGroup1Head.getDims()[0]
							* featureGroup1Head.getDims()[1],
					feature1Head1D.length);
			assertEquals(feature1Head1D[0], 0);
			assertEquals(feature1Head1D[1], 0); // Represents data1.getUnits()
												// position
			assertEquals(feature1Head1D[2], 1);
			assertEquals(feature1Head1D[3], 1); // Represents data2.getUnits()
												// position

			// Reads as Value, Uncertainity, xpos, ypos, zpos - Data3
			assertEquals(feature2Data1D[0], data3.getValue(), 0.0);
			assertEquals(feature2Data1D[1], data3.getUncertainty(), 0.0);
			assertEquals(feature2Data1D[2], data3.getPosition().get(0), 0.0);
			assertEquals(feature2Data1D[3], data3.getPosition().get(1), 0.0);
			assertEquals(feature2Data1D[4], data3.getPosition().get(2), 0.0);

			// Confirm the data length is correct
			assertEquals(
					featureGroup2Head.getDims()[0]
							* featureGroup2Head.getDims()[1],
					feature2Head1D.length);
			assertEquals(feature2Head1D[0], 0);
			assertEquals(feature2Head1D[1], 2); // Represents data3.getUnits()
												// position

			// Reads as Value, Uncertainity, xpos, ypos, zpos - Data4
			assertEquals(feature3aData1D[0], data4.getValue(), 0.0);
			assertEquals(feature3aData1D[1], data4.getUncertainty(), 0.0);
			assertEquals(feature3aData1D[2], data4.getPosition().get(0), 0.0);
			assertEquals(feature3aData1D[3], data4.getPosition().get(1), 0.0);
			assertEquals(feature3aData1D[4], data4.getPosition().get(2), 0.0);

			// Confirm the data length is correct
			assertEquals(
					featureGroup3aHead.getDims()[0]
							* featureGroup3aHead.getDims()[1],
					feature3aHead1D.length);
			assertEquals(feature3aHead1D[0], 0);
			assertEquals(feature3aHead1D[1], 3); // Represents data3.getUnits()
													// position

			// Reads as Value, Uncertainity, xpos, ypos, zpos - Data5
			assertEquals(feature3bData1D[0], data5.getValue(), 0.0);
			assertEquals(feature3bData1D[1], data5.getUncertainty(), 0.0);
			assertEquals(feature3bData1D[2], data5.getPosition().get(0), 0.0);
			assertEquals(feature3bData1D[3], data5.getPosition().get(1), 0.0);
			assertEquals(feature3bData1D[4], data5.getPosition().get(2), 0.0);

			// Confirm the data length is correct
			assertEquals(
					featureGroup3bHead.getDims()[0]
							* featureGroup3bHead.getDims()[1],
					feature3aHead1D.length);
			assertEquals(feature3bHead1D[0], 0);
			assertEquals(feature3bHead1D[1], 4); // Represents data3.getUnits()
													// position

		} catch (Exception e) {
			// Fail out of the test
			e.printStackTrace();
			fail();
		}

		// Check Group Creation

		H5Group group = manager.createGroup(h5File, h5Group);
		// See that the previous group has a group
		assertEquals(3, h5Group.getMemberList().size());
		// Check that it has the same name as the root manager
		assertEquals(manager.getName(), h5Group.getMemberList().get(2)
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

		// Setup GridLocation data
		// Setup LWRData
		String feature1 = "Feature 1";
		String feature2 = "Feature 2";
		double time1 = 1.0, time2 = 3.0, time3 = 3.5;
		LWRData data1, data2, data3, data4, data5;
		ArrayList<Double> position1 = new ArrayList<Double>(), position2 = new ArrayList<Double>(), position3 = new ArrayList<Double>(), position4 = new ArrayList<Double>(), position5 = new ArrayList<Double>();

		// Setup Positions

		// Setup Position 1
		position1.add(0.0);
		position1.add(1.0);
		position1.add(0.0);

		// Setup Position 2
		position2.add(0.0);
		position2.add(1.0);
		position2.add(4.0);

		// Setup Position 3
		position3.add(1.0);
		position3.add(1.0);
		position3.add(0.0);

		// Setup Position 4
		position4.add(0.0);
		position4.add(1.0);
		position4.add(1.0);

		// Setup Position 5
		position4.add(0.0);
		position4.add(1.0);
		position4.add(3.0);

		// Setup data1
		data1 = new LWRData(feature1);
		data1.setPosition(position1);
		data1.setValue(1.0);
		data1.setUncertainty(1.5);
		data1.setUnits("Units " + 1);

		// Setup data2
		data2 = new LWRData(feature1);
		data2.setPosition(position2);
		data2.setValue(2.0);
		data2.setUncertainty(2.5);
		data2.setUnits("Units " + 2);

		// Setup data3
		data3 = new LWRData(feature1);
		data3.setPosition(position3);
		data3.setValue(3.0);
		data3.setUncertainty(3.5);
		data3.setUnits("Units " + 3);

		// Setup data4
		data4 = new LWRData(feature1);
		data4.setPosition(position4);
		data4.setValue(4.0);
		data4.setUncertainty(4.5);
		data4.setUnits("Units " + 4);

		// Setup data5
		data5 = new LWRData(feature2);
		data5.setPosition(position5);
		data5.setValue(5.0);
		data5.setUncertainty(5.5);
		data5.setUnits("Units " + 5);

		// Local Declarations
		int size = 5;
		LWRGridManager component = new LWRGridManager(size);
		LWRGridManager newComponent = new LWRGridManager(size + 5);
		String name = "Bob the Builder";
		String description = "Can he fix it?";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		H5Group subGroup = null;
		LWRComponent component1 = new LWRComponent("Component 1");
		LWRComponent component2 = new LWRComponent("Component 2");
		LWRComponent component3 = new LWRComponent("Component 3");
		GridLocation location1 = new GridLocation(0, 1);
		GridLocation location2 = new GridLocation(2, 2);
		GridLocation location3 = new GridLocation(0, 0);
		ArrayList<String> list = new ArrayList<String>();

		// Add components to list
		list.add(component1.getName());
		list.add(component2.getName());
		list.add(component3.getName());

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.addComponent(component1, location1);
		component.addComponent(component2, location2);
		component.addComponent(component3, location3);

		// Add data to the location
		location1.getLWRDataProvider().addData(data1, time1);
		location1.getLWRDataProvider().addData(data2, time1);
		location1.getLWRDataProvider().addData(data3, time2);
		location1.getLWRDataProvider().addData(data4, time3);
		location1.getLWRDataProvider().addData(data5, time3);

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

		// Setup LWRGridManager with Data in the Group

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

			// Setup dataSets
			component.writeDatasets(h5File, subGroup);

			// Close group and then reopen
			h5File.close();
			h5File.open();
			parentH5Group = (H5Group) ((javax.swing.tree.DefaultMutableTreeNode) h5File
					.getRootNode()).getUserObject();

			// Get the subGroup
			subGroup = (H5Group) parentH5Group.getMemberList().get(0);

			// Read information
			assertTrue(newComponent.readAttributes(subGroup));
			assertTrue(newComponent.readDatasets(subGroup));
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