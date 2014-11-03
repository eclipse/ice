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
package org.eclipse.ice.reactor.sfr.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.ice.reactor.sfr.core.Material;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests the operations of the Material class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class MaterialTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the constructors and default values of the Material class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		// Initialize a Material for testing with nullary constructor
		Material materialOne = new Material();

		// Check the default name, description and ID
		assertEquals("Material 1", materialOne.getName());
		assertEquals("Material 1 Description", materialOne.getDescription());
		assertEquals(1, materialOne.getId());

		// Initialize a Material for testing with parameterized constructor
		Material materialTwo = new Material("Fjords");
		assertEquals("Fjords", materialTwo.getName());
		assertEquals("Material 1 Description", materialTwo.getDescription());
		assertEquals(1, materialTwo.getId());

		// end-user-code

	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the equality operation of Materials.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		/* --- Check equality between like materials ----------------------- */

		// Construct a material to test against
		Material material = new Material("Towel");
		material.setDescription("The most massively useful thing any interstellar Hitchhiker can carry");
		material.setId(42);

		// Construct a material equal to the first
		Material equalMaterial = new Material("Towel");
		equalMaterial
				.setDescription("The most massively useful thing any interstellar Hitchhiker can carry");
		equalMaterial.setId(42);

		// Check that equals() is reflexive and symmetric
		assertTrue(material.equals(material));
		assertTrue(material.equals(equalMaterial)
				&& equalMaterial.equals(material));

		// Check equals() fails with illegal rings
		assertFalse(material==null);

		/* --- Check transitivity with similar materials ------------------- */

		// Construct another material equal to the first, for testing
		// transitivity
		Material transMaterial = new Material("Towel");
		transMaterial
				.setDescription("The most massively useful thing any interstellar Hitchhiker can carry");
		transMaterial.setId(42);

		// Check for transitivity
		if (material.equals(transMaterial)
				&& transMaterial.equals(equalMaterial)) {
			assertTrue(material.equals(equalMaterial));
		} else {
			fail();
		}

		/* --- Check inequality between two dissimilar materials ----------- */

		// Construct a material unequal to the first
		Material unequalMaterial = new Material("Dish rag");
		unequalMaterial
				.setDescription("Not terribly useful for interstellar travel");
		unequalMaterial.setId(24);

		// Check that material and unequalMaterial are not the same
		assertFalse(material.equals(unequalMaterial));

		/* --- Check hash values between equal and unequal materials ------- */

		assertEquals(material.hashCode(), material.hashCode());
		assertEquals(material.hashCode(), equalMaterial.hashCode());
		assertFalse(material.hashCode() == unequalMaterial.hashCode());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the copying and cloning operations of Materials.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		/* --- Testing the copy operation ---------------------------------- */

		// Create a material to copy from
		Material material = new Material("Small Lump of Green Putty");
		material.setDescription("Found in my armpit one midsummer morning");
		material.setId(5496);

		// Create an empty material to copy to
		Material materialCopy = new Material();

		// Copy the contents of material to materialCopy
		materialCopy.copy(material);

		// Check material and materialCopy are identical
		assertTrue(material.equals(materialCopy));

		// Try copying the contents of a null material
		materialCopy.copy(null);

		// Check to see that materialCopy remains unchanged
		assertTrue(materialCopy.equals(material));

		/* --- Testing the cloning operation ------------------------------- */

		// Clone the first material
		Object materialClone = material.clone();

		// Check that materialClone isn't null
		assertNotNull(materialClone);

		// Check that material and materialClone are identical
		assertTrue(materialClone.equals(material));

		return;
		// end-user-code
	}
}