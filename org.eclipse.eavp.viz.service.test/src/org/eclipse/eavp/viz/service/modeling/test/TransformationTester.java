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
package org.eclipse.eavp.viz.service.modeling.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;

import org.eclipse.eavp.viz.service.modeling.Transformation;
import org.junit.Test;

/**
 * Tests the Transformation class
 * 
 * @author Jay Jay Billings
 */
public class TransformationTester {
	/**
	 * <p>
	 * Checks that the skew values can be set and get correctly
	 * </p>
	 * 
	 */
	@Test
	public void checkSkew() {

		Transformation transformation = new Transformation();
		transformation.setSkew(-1.0, 1.2, 5.0);

		double[] expectedSkew = new double[] { -1.0, 1.2, 5.0 };
		assertTrue(Arrays.equals(expectedSkew, transformation.getSkew()));

	}

	/**
	 * <p>
	 * Checks that the size value can be set and get correctly
	 * </p>
	 * 
	 */
	@Test
	public void checkSize() {

		Transformation transformation = new Transformation();
		transformation.setSize(42.0);

		assertEquals(42.0, transformation.getSize(), 0.0);

	}

	/**
	 * <p>
	 * Checks that the scale values can be set and get correctly
	 * </p>
	 * 
	 */
	@Test
	public void checkScale() {

		Transformation transformation = new Transformation();
		transformation.setScale(-1.0, 1.2, 5.0);

		double[] expectedScale = new double[] { -1.0, 1.2, 5.0 };
		assertTrue(Arrays.equals(expectedScale, transformation.getScale()));

	}

	/**
	 * <p>
	 * Checks that the rotation values can be set and get correctly
	 * </p>
	 * 
	 */
	@Test
	public void checkRotation() {

		Transformation transformation = new Transformation();
		transformation.setRotation(-1.0, 1.2, 5.0);

		double[] expectedRotation = new double[] { -1.0, 1.2, 5.0 };
		assertTrue(
				Arrays.equals(expectedRotation, transformation.getRotation()));

	}

	/**
	 * <p>
	 * Checks that the translation values can be set and get correctly
	 * </p>
	 * 
	 */
	@Test
	public void checkTranslation() {

		Transformation transformation = new Transformation();
		transformation.setTranslation(-1.0, 1.2, 5.0);

		double[] expectedTranslation = new double[] { -1.0, 1.2, 5.0 };
		assertTrue(Arrays.equals(expectedTranslation,
				transformation.getTranslation()));

	}

	/**
	 * Reimplement this test if the JAXB support is ever added to the modeling
	 * packages.
	 */
	// /**
	// * This operation checks the ability of the Transformation to persist
	// itself
	// * to XML and to load itself from an XML input stream.
	// *
	// * @throws IOException
	// * @throws JAXBException
	// * @throws NullPointerException
	// */
	// @Test
	// public void checkLoadingFromXML() throws NullPointerException,
	// JAXBException, IOException {
	//
	// // Local Declarations
	// ICEJAXBHandler xmlHandler = new ICEJAXBHandler();
	// ArrayList<Class> classList = new ArrayList<Class>();
	// classList.add(Transformation.class);
	//
	// // Instantiate a Transformation
	// Transformation transformation = new Transformation();
	// transformation.setName("Big transformation");
	// transformation.setId(999999999);
	// transformation.setDescription("Really huge!");
	// transformation.setRotation(0.1, -0.1, 0.7);
	// transformation.setSkew(0.0, 1.0, 0.4);
	// transformation.setSize(120000000.0);
	// transformation.setScale(1.0, 3000.0, 500000.0);
	// transformation.setTranslation(-5000, 1000, 0.0);
	//
	// // Load it into XML
	// ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	// xmlHandler.write(transformation, classList, outputStream);
	//
	// // convert information inside of outputStream to inputStream
	// ByteArrayInputStream inputStream = new
	// ByteArrayInputStream(outputStream.toByteArray());
	//
	// String xmlFile2 = new String(outputStream.toByteArray());
	// System.err.println("File " + xmlFile2);
	//
	// // load contents into xml
	// Transformation loadTransformation = new Transformation();
	// loadTransformation = (Transformation) xmlHandler.read(classList,
	// inputStream);
	//
	// // Check contents
	// assertTrue(loadTransformation.equals(transformation));
	//
	// }

	/**
	 * <p>
	 * This operation checks the Transformation to ensure that its equals() and
	 * hashcode() operations work.
	 * </p>
	 * 
	 */
	@Test
	public void checkEquality() {

		// Create Matrix4x4 to test
		Transformation component = new Transformation();
		Transformation equalComponent = new Transformation();
		Transformation unEqualComponent = new Transformation();
		Transformation transitiveComponent = new Transformation();

		// Change values
		component.setRotation(1.0, 2.0, 3.0);
		equalComponent.setRotation(1.0, 2.0, 3.0);
		transitiveComponent.setRotation(1.0, 2.0, 3.0);

		unEqualComponent.setRotation(-1.0, -2.0, 4.3333333333);

		// Set ICEObject data
		component.setId(1);
		equalComponent.setId(1);
		transitiveComponent.setId(1);
		unEqualComponent.setId(2);

		component.setName("DC 5V");
		equalComponent.setName("DC 5V");
		transitiveComponent.setName("DC 5V");
		unEqualComponent.setName("AC 115V");

		// Assert two equal ComplexShapes return true
		assertTrue(component.equals(equalComponent));

		// Assert two unequal ComplexShapes return false
		assertFalse(component.equals(unEqualComponent));

		// Assert equals() is reflexive
		assertTrue(component.equals(component));

		// Assert the equals() is Symmetric
		assertTrue(component.equals(equalComponent)
				&& equalComponent.equals(component));

		// Assert equals() is transitive
		if (component.equals(equalComponent)
				&& equalComponent.equals(transitiveComponent)) {
			assertTrue(component.equals(transitiveComponent));
		} else {
			fail();
		}

		// Assert equals is consistent
		assertTrue(component.equals(equalComponent)
				&& component.equals(equalComponent)
				&& component.equals(equalComponent));
		assertTrue(!component.equals(unEqualComponent)
				&& !component.equals(unEqualComponent)
				&& !component.equals(unEqualComponent));

		// Assert checking equality with null is false
		assertFalse(component == null);

		// Assert that two equal objects return same hashcode
		assertTrue(component.equals(equalComponent)
				&& component.hashCode() == equalComponent.hashCode());

		// Assert that hashcode is consistent
		assertTrue(component.hashCode() == component.hashCode());

		// Assert that hashcodes from unequal objects are different
		assertTrue(component.hashCode() != unEqualComponent.hashCode());
	}

	/**
	 * <p>
	 * This operation tests the construction of the Transformation class and the
	 * functionality inherited from ICEObject.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreation() {

		// Create a Transformation
		Transformation transformation = new Transformation();

		double[] expectedSkew = new double[] { 0.0, 0.0, 0.0 };
		double expectedSize = 1.0;
		double[] expectedScale = new double[] { 1.0, 1.0, 1.0 };
		double[] expectedRotation = new double[] { 0.0, 0.0, 0.0 };
		double[] expectedTranslation = new double[] { 0.0, 0.0, 0.0 };

		// Check that the transformation is initialized to the expected values

		assertTrue(Arrays.equals(expectedSkew, transformation.getSkew()));
		assertEquals(expectedSize, transformation.getSize(), 0.0);
		assertTrue(Arrays.equals(expectedScale, transformation.getScale()));
		assertTrue(
				Arrays.equals(expectedRotation, transformation.getRotation()));
		assertTrue(Arrays.equals(expectedTranslation,
				transformation.getTranslation()));

	}

	/**
	 * <p>
	 * This operation checks the Transformation to ensure that its copy() and
	 * clone() operations work as specified.
	 * </p>
	 * 
	 */
	@Test
	public void checkCopying() {

		Transformation transformation = new Transformation();
		Transformation cloneTransformation;
		Transformation copyTransformation;

		// Set up ICEObject stuff for Transformation
		transformation.setId(25);
		transformation.setDescription("Geometry description");
		transformation.setName("Geometry name");

		// Set up Transformation-specific stuff
		transformation.setSkew(0.0, 1.0, 2.0);
		transformation.setSize(3.0);
		transformation.setScale(4.0, 5.0, 6.0);
		transformation.setRotation(7.0, 8.0, 9.0);
		transformation.setTranslation(10.0, 11.0, 12.0);

		// Clone contents
		cloneTransformation = (Transformation) transformation.clone();

		assertNotNull(cloneTransformation);

		// Check equality of contents
		assertTrue(cloneTransformation.equals(transformation));

		// Copy contents
		copyTransformation = new Transformation();
		copyTransformation.copy(transformation);

		// Check equality of contents
		assertTrue(copyTransformation.equals(transformation));

		// Pass null into copy contents, show nothing has changed
		copyTransformation.copy(null);

		// Check equality of contents
		assertTrue(copyTransformation.equals(transformation));

	}
}