/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.eavp.viz.service.mesh.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.bind.JAXBException;

import org.eclipse.eavp.viz.service.datastructures.VizObject.VizJAXBHandler;
import org.eclipse.eavp.viz.service.mesh.datastructures.PolygonProperties;
import org.junit.Test;

/**
 * <p>
 * Tests the methods of the PolygonProperties class.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class PolygonPropertiesTester {

	/**
	 * <p>
	 * This operation tests the construction of PolygonProperties instances.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Check nullary constructor
		PolygonProperties properties = new PolygonProperties();
		assertEquals("nul1", properties.getMaterialId());
		assertEquals(0, properties.getGroupNum());

		// Try to construct with null input
		String nullString = null;
		properties = new PolygonProperties(nullString, 0);

		// Check for the default values
		assertEquals("nul1", properties.getMaterialId());
		assertEquals(0, properties.getGroupNum());

		// Try to construct with invalid input (too long)
		properties = new PolygonProperties("jdjs3339s", 15246543);

		// Check for the default values
		assertEquals("nul1", properties.getMaterialId());
		assertEquals(0, properties.getGroupNum());

		// Construct with valid values
		properties = new PolygonProperties("42c", 18);

		// Check the values were set
		assertEquals("42c", properties.getMaterialId());
		assertEquals(18, properties.getGroupNum());

		return;
	}

	/**
	 * <p>
	 * This operation checks the ability of a PolygonProperties instance to
	 * persist itself to XML and to load itself from an XML input stream.
	 * </p>
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws NullPointerException 
	 * 
	 */
	@Test
	public void checkLoadingFromXML() throws NullPointerException, JAXBException, IOException {

		// Local Declarations
		VizJAXBHandler xmlHandler = new VizJAXBHandler();
		ArrayList<Class> classList = new ArrayList<Class>();
		classList.add(PolygonProperties.class);

		// Create a Polygon to test.
		PolygonProperties properties = new PolygonProperties("mid", 1);

		// Load it into XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		xmlHandler.write(properties, classList, outputStream);

		// Convert the output stream data to an input stream.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Load the input stream's contents into a new component.
		PolygonProperties loadedProperties = new PolygonProperties();
		loadedProperties = (PolygonProperties) xmlHandler.read(classList, inputStream);

		// Make sure the two components match.
		assertTrue(properties.equals(loadedProperties));

		return;
	}

	/**
	 * <p>
	 * This operation tests the equals method for PolygonProperties.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Create some objects for testing
		PolygonProperties object = new PolygonProperties("18z", 3);
		PolygonProperties unequalObject = new PolygonProperties("121r", 25);
		PolygonProperties equalObject = new PolygonProperties("18z", 3);

		// Check for inequality
		assertFalse(object==null);
		assertFalse("such fake. much deceit. wow.".equals(object));
		assertFalse(object.equals(unequalObject));
		assertFalse(unequalObject.equals(equalObject));

		// Check for equality
		assertTrue(object.equals(equalObject));

		return;
	}

	/**
	 * <p>
	 * This operation checks the Polygon to ensure that its copy() and clone()
	 * operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		// Create PolygonProperties to test.
		PolygonProperties object = new PolygonProperties("one", 1);
		PolygonProperties copy = new PolygonProperties();
		PolygonProperties clone = null;

		// Make sure the objects are not equal before copying.
		assertFalse(object == copy);
		assertFalse(object.equals(copy));

		// Copy the object.
		copy.copy(object);

		// Make sure the references are different but contents the same.
		assertFalse(object == copy);
		assertTrue(object.equals(copy));

		// Do the same for the clone operation.

		// Make sure the objects are not equal before copying.
		assertFalse(object == clone);
		assertFalse(object.equals(clone));

		// Copy the object.
		clone = (PolygonProperties) object.clone();

		// Make sure the references are different but contents the same.
		assertFalse(object == clone);
		assertTrue(object.equals(clone));
		assertFalse(copy == clone);
		assertTrue(copy.equals(clone));

		return;
	}

}
