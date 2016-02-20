/*******************************************************************************
 * Copyright (c) 2011, 2014 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz.service.datastructures.VizObject.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.eavp.viz.service.datastructures.VizObject.VizJAXBHandler;
import org.eclipse.eavp.viz.service.datastructures.VizObject.VizObject;
import org.eclipse.eavp.viz.service.datastructures.test.TestVizComponentListener;
import org.junit.Test;

/**
 * <p>
 * The VizObjectTester is responsible for testing the VizObject class. It only
 * tests the name, id, and description properties as well as persistence.
 * It also checks equality, hashCode computation, copying, and cloning.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class VizObjectTester {

	/**
	 * This operation checks the VizObject to insure that the id, name and
	 * description getters and setters function properly.
	 */
	@Test
	public void checkProperties() {

		// Local declarations
		int id = 20110901;
		String name = "September 1st 2011";
		String description = "The 1st day of the ninth month in the year of "
				+ "our Lord 2011";

		// Create the VizObject
		VizObject testNC = new VizObject();

		// Set up the id, name and description
		testNC.setId(id);
		testNC.setName(name);
		testNC.setDescription(description);

		// Check the id, name and description
		assertEquals(testNC.getId(), id);
		assertEquals(testNC.getName(), name);
		assertEquals(testNC.getDescription(), description);

	}

	/**
	 * This operation checks the VizObject class to ensure that its copy() and
	 * clone() operations work as specified.
	 */
	@Test
	public void checkCopying() {

		// Local declarations
		int id = 20110901;
		String name = "September 1st 2011";
		String description = "The 1st day of the ninth month in the year of "
				+ "our Lord 2011";
		VizObject testNC = new VizObject();
		
		// Test to show valid usage of clone

		// Set up the id, name and description
		testNC.setId(id);
		testNC.setName(name);
		testNC.setDescription(description);

		// Run clone operation
		VizObject cloneNC = (VizObject) testNC.clone();

		// Check the id, name and description with clone
		assertEquals(testNC.getId(), cloneNC.getId());
		assertEquals(testNC.getName(), cloneNC.getName());
		assertEquals(testNC.getDescription(), cloneNC.getDescription());

		// Test to show valid usage of copy

		// Local declarations
		id = 20110901;
		name = "September 1st 2011";
		description = "The 1st day of the ninth month in the year of "
				+ "our Lord 2011";
		testNC = new VizObject();

		// Set up the id, name and description
		testNC.setId(id);
		testNC.setName(name);
		testNC.setDescription(description);

		// Create a new instance of VizObject and copy contents
		VizObject testNC2 = new VizObject();
		testNC2.copy(testNC);

		// Check the id, name and description with copy
		assertEquals(testNC.getId(), testNC2.getId());
		assertEquals(testNC.getName(), testNC2.getName());
		assertEquals(testNC.getDescription(), testNC2.getDescription());

		// Test to show an invalid use of copy - null args

		// Local declarations
		id = 20110901;
		name = "September 1st 2011";
		description = "The 1st day of the ninth month in the year of "
				+ "our Lord 2011";
		testNC = new VizObject();

		// Set up the id, name and description
		testNC.setId(id);
		testNC.setName(name);
		testNC.setDescription(description);
		// Attempt the null copy
		testNC.copy(null);

		// Check the id, name and description - nothing has changed
		assertEquals(testNC.getId(), id);
		assertEquals(testNC.getName(), name);
		assertEquals(testNC.getDescription(), description);

	}

	/**
	 * <p>
	 * This operation checks the ability of the VizObject to persist itself to
	 * XML and to load itself from an XML input stream.
	 * </p>
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 * 
	 */
	@Test
	public void checkXMLPersistence() throws NullPointerException, JAXBException, IOException {
		// TODO Auto-generated method stub

		/*
		 * The following sets of operations will be used to test the
		 * "read and write" portion of the VizObject. It will demonstrate the
		 * behavior of reading and writing from an
		 * "XML (inputStream and outputStream)" file. It will use an annotated
		 * VizObject to demonstrate basic behavior.
		 */

		// Local declarations
		VizObject testNC = null, testNC2 = null;
		int id = 20110901;
		String name = "September 1st 2011";
		String description = "The 1st day of the ninth month in the year of "
				+ "our Lord 2011";
		VizJAXBHandler xmlHandler = new VizJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(VizObject.class);

		// Demonstrate a basic "write" to file. Should not fail
		// Initialize the object and set values.
		testNC = new VizObject();
		testNC.setId(id);
		testNC.setName(name);
		testNC.setDescription(description);

		// persist to an output stream
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(testNC, classList, outputStream);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Convert to inputStream
		testNC2 = (VizObject) xmlHandler.read(classList, inputStream);

		// Check that it equals the persisted object
		assertTrue(testNC.equals(testNC2));

	}

	/**
	 * <p>
	 * This operation checks the VizObject class to insure that its equals()
	 * operation works.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Create an VizObject
		VizObject testVizObject = new VizObject();

		// Set its data
		testVizObject.setId(12);
		testVizObject.setName("ICE VizObject");
		testVizObject.setDescription("This is an VizObject that will "
				+ "be used for testing equality with other VizObjects.");

		// Create another VizObject to assert Equality with the last
		VizObject equalObject = new VizObject();

		// Set its data, equal to testVizObject
		equalObject.setId(12);
		equalObject.setName("ICE VizObject");
		equalObject.setDescription("This is an VizObject that will "
				+ "be used for testing equality with other VizObjects.");

		// Create an VizObject that is not equal to testVizObject
		VizObject unEqualObject = new VizObject();

		// Set its data, not equal to testVizObject
		unEqualObject.setId(52);
		unEqualObject.setName("Bill the VizObject");
		unEqualObject.setDescription("This is an VizObject to verify that "
				+ "VizObject.equals() returns false for an object that is not "
				+ "equivalent to testVizObject.");

		// Create a third VizObject to test Transitivity
		VizObject transitiveObject = new VizObject();

		// Set its data, not equal to testVizObject
		transitiveObject.setId(12);
		transitiveObject.setName("ICE VizObject");
		transitiveObject.setDescription("This is an VizObject that will "
				+ "be used for testing equality with other VizObjects.");

		// Assert that these two VizObjects are equal
		assertTrue(testVizObject.equals(equalObject));

		// Assert that two unequal objects returns false
		assertFalse(testVizObject.equals(unEqualObject));

		// Check that equals() is Reflexive
		// x.equals(x) = true
		assertTrue(testVizObject.equals(testVizObject));

		// Check that equals() is Symmetric
		// x.equals(y) = true iff y.equals(x) = true
		assertTrue(testVizObject.equals(equalObject)
				&& equalObject.equals(testVizObject));

		// Check that equals() is Transitive
		// x.equals(y) = true, y.equals(z) = true => x.equals(z) = true
		if (testVizObject.equals(equalObject)
				&& equalObject.equals(transitiveObject)) {
			assertTrue(testVizObject.equals(transitiveObject));
		} else {
			fail();
		}

		// Check the Consistent nature of equals()
		assertTrue(testVizObject.equals(equalObject)
				&& testVizObject.equals(equalObject)
				&& testVizObject.equals(equalObject));
		assertTrue(!testVizObject.equals(unEqualObject)
				&& !testVizObject.equals(unEqualObject)
				&& !testVizObject.equals(unEqualObject));

		// Assert checking equality with null value returns false
		assertFalse(testVizObject == null);

		// Assert that two equal objects have the same hashcode
		assertTrue(testVizObject.equals(equalObject)
				&& testVizObject.hashCode() == equalObject.hashCode());

		// Assert that hashcode is consistent
		assertTrue(testVizObject.hashCode() == testVizObject.hashCode());

		// Assert that hashcodes are different for unequal objects
		assertFalse(testVizObject.hashCode() == unEqualObject.hashCode());

	}

	/**
	 * <p>
	 * This operation tests the VizObject to insure that it can properly
	 * dispatch notifications when it receives an update that changes its state.
	 * </p>
	 * 
	 */
	@Test
	public void checkNotifications() {

		// Setup the listeners
		TestVizComponentListener firstListener = new TestVizComponentListener();
		TestVizComponentListener secondListener = new TestVizComponentListener();

		// Setup the iceObject
		VizObject iceObject = new VizObject();

		// Register the listener
		iceObject.register(firstListener);

		// Add the second listener
		iceObject.register(secondListener);

		// Change the name of the object
		iceObject.setName("Warren Buffett");
		// Check the listeners to make sure they updated
		assertTrue(firstListener.wasNotified());
		assertTrue(secondListener.wasNotified());
		// Reset the listeners
		firstListener.reset();
		secondListener.reset();

		// Unregister the second listener so that it no longer receives updates
		iceObject.unregister(secondListener);

		// Change the id of the object
		iceObject.setId(899);
		assertTrue(firstListener.wasNotified());
		// Make sure the second listener was not updated
		assertFalse(secondListener.wasNotified());

		// Reset the listener
		firstListener.reset();
		// Change the description of the object
		iceObject.setDescription("New description");
		// Make sure the listener was notified
		assertTrue(firstListener.wasNotified());

		return;
	}
}