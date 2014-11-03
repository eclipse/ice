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
package org.eclipse.ice.datastructures.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.eclipse.ice.datastructures.form.mesh.PolygonProperties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests the methods of the PolygonProperties class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class PolygonPropertiesTester {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation tests the construction of PolygonProperties instances.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the ability of a PolygonProperties instance to
	 * persist itself to XML and to load itself from an XML input stream.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkLoadingFromXML() {
		// begin-user-code

		// Create a Polygon to test.
		PolygonProperties properties = new PolygonProperties("mid", 1);

		// Load it into XML.
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		properties.persistToXML(outputStream);
		assertNotNull(outputStream);

		// Convert the output stream data to an input stream.
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				outputStream.toByteArray());

		// Load the input stream's contents into a new component.
		PolygonProperties loadedProperties = new PolygonProperties();
		loadedProperties.loadFromXML(inputStream);

		// Make sure the two components match.
		assertTrue(properties.equals(loadedProperties));

		// Check invalid parameters.

		// Try passing null and make sure the components match.
		inputStream = null;
		loadedProperties.loadFromXML(inputStream);
		assertTrue(properties.equals(loadedProperties));

		// Try passing a bad input stream and make sure the components match.
		inputStream = new ByteArrayInputStream("jkl;2invalidstream".getBytes());
		loadedProperties.loadFromXML(inputStream);
		assertTrue(properties.equals(loadedProperties));

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation tests the equals method for PolygonProperties.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the Polygon to ensure that its copy() and clone()
	 * operations work as specified.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

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
		// end-user-code
	}

}
