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
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialType;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.TubeType;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A class that tests Tube's operations.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class TubeTester {
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
	 * This operation checks the constructors and their default values.
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
		Tube tube;
		// Default values. Change here as needed for tests
		String defaultName = "Tube";
		String defaultDesc = "Tube's Description";
		int defaultId = 1;
		double defaultInnerRadius = 0;
		double defaultOuterRadius = 1;
		double defaultHeight = 1.0;
		TubeType defaultType = TubeType.GUIDE;
		Material defaultMaterial = new Material();
		HDF5LWRTagType type = HDF5LWRTagType.TUBE;

		// New values
		String newName = "Super Tube!";
		Material newMaterial = new Material();
		newMaterial.setName("SuperMaterial!!@#!@#*!@#!");
		double newHeight = 1.51231541513;
		double newOuterRadius = 86.3985656;
		double newInnerRadius = 5.83495787819;
		TubeType newType = TubeType.INSTRUMENT;

		// Check nullary constructor
		tube = new Tube();
		// Check default values
		assertEquals(defaultName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(defaultType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - name
		tube = new Tube(newName);
		// Check default values
		assertEquals(newName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(defaultType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - name with null
		tube = new Tube(null);
		// Check default values
		assertEquals(defaultName, tube.getName()); // Defaults name
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(defaultType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - name and type
		tube = new Tube(newName, newType);
		// Check default values
		assertEquals(newName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(newType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check nullary type - type is null
		tube = new Tube(newName, null);
		// Check default values
		assertEquals(newName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(defaultHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(defaultType, tube.getTubeType()); // Defaults
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - name, tubetype, material, height,
		// outerRadius
		tube = new Tube(newName, newType, newMaterial, newHeight,
				newOuterRadius);
		// Check default values
		assertEquals(newName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(newOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(newHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(newMaterial.getName(), tube.getMaterial().getName());
		assertEquals(newType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - null material
		tube = new Tube(newName, newType, null, newHeight, newOuterRadius);
		// See that it defaults the value
		assertNotNull(tube.getMaterial()); // Defaults

		// Check non-nullary constructor - bad height
		tube = new Tube(newName, newType, newMaterial, 0.0, newOuterRadius);
		// See that it defaults the value
		assertEquals(defaultHeight, tube.getHeight(), 0.0); // Defaults

		// Check non-nullary constructor - bad outerRadius
		tube = new Tube(newName, newType, newMaterial, newHeight, 0.0);
		// See that it defaults the value
		assertEquals(defaultOuterRadius, tube.getOuterRadius(), 0.0); // Defaults

		// Check non-nullary constructor - name, tubetype, material, height,
		// innerRadius, outerRadius
		tube = new Tube(newName, newType, newMaterial, newHeight,
				newInnerRadius, newOuterRadius);
		// Check default values
		assertEquals(newName, tube.getName());
		assertEquals(defaultDesc, tube.getDescription());
		assertEquals(defaultId, tube.getId());
		assertEquals(newInnerRadius, tube.getInnerRadius(), 0.0);
		assertEquals(newOuterRadius, tube.getOuterRadius(), 0.0);
		assertEquals(newHeight, tube.getHeight(), 0.0);
		assertNotNull(tube.getMaterial());
		assertEquals(newMaterial.getName(), tube.getMaterial().getName());
		assertEquals(newType, tube.getTubeType());
		assertEquals(type, tube.getHDF5LWRTag());

		// Check non-nullary constructor - bad innerRadius
		tube = new Tube(newName, newType, newMaterial, newHeight, -1.0,
				newOuterRadius);
		// See that it defaults the value
		assertEquals(defaultInnerRadius, tube.getInnerRadius(), 0.0); // Defaults

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the getter and setter for the tubeTypes.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkTubeType() {
		// begin-user-code
		// Local Declarations
		Tube tube = new Tube();

		// Check valid usage of tube type - GUIDE
		tube.setTubeType(TubeType.GUIDE);
		assertEquals(TubeType.GUIDE, tube.getTubeType());

		// Check valid usage of tube type -INSTRUMENT
		tube.setTubeType(TubeType.INSTRUMENT);
		assertEquals(TubeType.INSTRUMENT, tube.getTubeType());

		// Check invalid usage - null
		tube.setTubeType(null);
		assertEquals(TubeType.INSTRUMENT, tube.getTubeType()); // Stays the same
																// as previous
																// value

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the equals and hashCode operations.
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
		Tube object, equalObject, unEqualObject, transitiveObject;
		String name = "Billy!";
		int id = 654654;
		String description = "ASDFG Billy464646";
		Material material = new Material();
		TubeType type = TubeType.INSTRUMENT;
		TubeType unEqualTubeType = TubeType.GUIDE;
		material.setMaterialType(MaterialType.GAS);
		double innerRadius = 5.0, outerRadius = 8, height = 20.0;

		// Setup root object
		object = new Tube(name, type, material, height, innerRadius,
				outerRadius);
		object.setId(id);
		object.setDescription(description);

		// Setup equalObject equal to object
		equalObject = new Tube(name, type, material, height, innerRadius,
				outerRadius);
		equalObject.setId(id);
		equalObject.setDescription(description);

		// Setup transitiveObject equal to object
		transitiveObject = new Tube(name, type, material, height, innerRadius,
				outerRadius);
		transitiveObject.setId(id);
		transitiveObject.setDescription(description);

		// Set its data, not equal to object
		// Different Material
		unEqualObject = new Tube(name, unEqualTubeType, material, height,
				innerRadius, outerRadius);
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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks the copy and clone routines.
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
		Tube object, copyObject, clonedObject;
		String name = "Billy!";
		int id = 654654;
		String description = "ASDFG Billy464646";
		Material material = new Material();
		TubeType type = TubeType.INSTRUMENT;
		material.setMaterialType(MaterialType.GAS);
		double innerRadius = 5.0, outerRadius = 8, height = 20.0;

		// Setup root object
		object = new Tube(name, type, material, height, innerRadius,
				outerRadius);
		object.setId(id);
		object.setDescription(description);

		// Run the copy routine
		copyObject = new Tube();
		copyObject.copy(object);

		// Check contents
		assertTrue(object.equals(copyObject));

		// Run the clone routine
		clonedObject = (Tube) object.clone();

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
		Tube component = new Tube();
		String name = "Tube";
		String description = "Put a ring on it...'tube'? :)";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		Attribute attribute = null;
		String attributeValue = null;
		Material material = new Material("MATERIAL!S");
		double height = 5, inner = 3, outer = 6;
		TubeType type = component.getTubeType();
		String testFileName = "testWrite.h5";

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
			assertEquals(8, h5Group.getMetadata().size());

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

			// Check String Attribute - tube type
			attribute = (Attribute) h5Group.getMetadata().get(7);
			assertEquals(attribute.getName(), "tubeType");
			assertEquals(attribute.getType().getDatatypeClass(),
					Datatype.CLASS_STRING);
			attributeValue = ((String[]) attribute.getValue())[0];
			assertEquals(type.toString(), attributeValue);
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
		Tube component = new Tube();
		Tube newComponent = new Tube();
		String name = "TUBULAR!?";
		String description = "Put a ring on it...'tube'? :)";
		int id = 4;
		HDF5LWRTagType tag = component.getHDF5LWRTag();
		Material material = new Material("MATERIAL!S");
		double height = 5, inner = 3, outer = 6;
		TubeType type = component.getTubeType();
		H5Group subGroup = null;

		// Setup Component
		component.setName(name);
		component.setId(id);
		component.setDescription(description);
		component.setMaterial(material);
		component.setHeight(height);
		component.setOuterRadius(outer);
		component.setInnerRadius(inner);

		// Temporary
		newComponent.setMaterial(material);

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

			// Setup height attribute
			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup, "height",
					height);

			// Setup height inner radius
			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup,
					"innerRadius", inner);

			// Setup height outer radius
			HdfWriterFactory.writeDoubleAttribute(h5File, subGroup,
					"outerRadius", outer);

			// Setup tube type
			HdfWriterFactory.writeStringAttribute(h5File, subGroup, "tubeType",
					type.toString());

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
	 * Checks the toString and toType operations on MaterialType.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkTyping() {
		// begin-user-code

		// Local Declarations
		TubeType type;

		// Check the toString implementations of the TubeType enum
		assertEquals("Guide", TubeType.GUIDE.toString());
		assertEquals("Instrument", TubeType.INSTRUMENT.toString());

		// Check the toType implementations of the HDf5 enum

		// Specify the type
		type = TubeType.GUIDE;
		// Check the type
		assertEquals(type.toType("Guide"), TubeType.GUIDE);

		// Specify the type
		type = TubeType.INSTRUMENT;
		// Check the type
		assertEquals(type.toType("Instrument"), TubeType.INSTRUMENT);

		// Try to return a type that does not exist
		assertEquals(type.toType("asdasd1"), null);

		// end-user-code
	}
}