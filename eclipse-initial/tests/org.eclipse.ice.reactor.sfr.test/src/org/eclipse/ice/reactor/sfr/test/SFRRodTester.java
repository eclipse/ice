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
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRRod;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Tests the operations of the SFRRod class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author w5q
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SFRRodTester {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the constructors and default values for the SFRRod class.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkConstruction() {
		// begin-user-code

		/* --- Test the nullary constructor -------------------------------- */

		// Create a SFRRod for testing
		SFRRod rodOne = new SFRRod();

		// Create a default reflector to test against
		Material material = new Material("SS-316");
		material.setDescription("Stainless Steel");

		Ring reflector = new Ring();
		reflector.setMaterial(material);
		reflector.setHeight(0.0);
		reflector.setInnerRadius(0.0);
		reflector.setOuterRadius(26.666);

		// Check the name description and ID
		assertEquals("SFR Rod 1", rodOne.getName());
		assertEquals("SFR Rod 1's Description", rodOne.getDescription());
		assertEquals(1, rodOne.getId());

		// Check the reflector
		assertEquals(reflector, rodOne.getReflector());

		/* --- Test parameterized constructor with name -------------------- */

		// Create a SFRRod with a name specified
		SFRRod rodTwo = new SFRRod("Ravenous Bugblatter Beast of Traal");

		// Check the name description and ID
		assertEquals("Ravenous Bugblatter Beast of Traal", rodTwo.getName());
		assertEquals("SFR Rod 1's Description", rodTwo.getDescription());
		assertEquals(1, rodTwo.getId());

		// Check the reflector
		assertEquals(reflector, rodTwo.getReflector());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the getter and setter for the rod attribute.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAttributes() {
		// begin-user-code

		// Create a SFRRod for testing
		SFRRod rod = new SFRRod();

		// Create a reflector for testing
		Ring reflector = new Ring();
		Material reflectorMaterial = new Material(
				"Almost, but not quite, entirely unlike tea");
		reflector.setMaterial(reflectorMaterial);

		// Set the reflector rod and check it
		rod.setReflector(reflector);
		assertNotNull(rod.getReflector());
		assertEquals(reflector, rod.getReflector());

		// Try setting an invalid reflector
		rod.setReflector(null);

		// Check that reflector remains unchanged
		assertEquals(reflector, rod.getReflector());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the equality operation of SFRRods.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkEquality() {
		// begin-user-code

		/* --- Check equality between like rods --------------------------- */

		// Construct a SFRRod (with reflector) to test against
		SFRRod rod = new SFRRod();
		Ring reflector = new Ring();
		rod.setReflector(reflector);

		rod.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		rod.setDescription("Worst poet in the universe");
		rod.setId(5);

		// Construct a SFRRod equal to the first
		SFRRod equalRod = new SFRRod();
		Ring equalReflector = new Ring();
		equalRod.setReflector(equalReflector);

		equalRod.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		equalRod.setDescription("Worst poet in the universe");
		equalRod.setId(5);

		// Check that equals() is reflexive and symmetric
		assertTrue(rod.equals(equalRod));
		assertTrue(rod.equals(equalRod) && equalRod.equals(rod));

		// Check equals() fails with illegal rings
		assertFalse(rod==null);

		/* --- Check transitivity with similar rods ----------------------- */

		// Construct another SFRRod equal to the first, for testing transitivity
		SFRRod transRod = new SFRRod();
		Ring transReflector = new Ring();
		transRod.setReflector(transReflector);

		transRod.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		transRod.setDescription("Worst poet in the universe");
		transRod.setId(5);

		// Check for transitivity
		if (rod.equals(transRod) && transRod.equals(equalRod)) {
			assertTrue(rod.equals(equalRod));
		} else {
			fail();
		}

		/* --- Check inequality between two dissimilar rods ---------------- */

		// Construct a SFRRod unequal to the first
		SFRRod unequalRod = new SFRRod();
		Ring unequalReflector = new Ring();
		unequalRod.setReflector(unequalReflector);

		unequalRod.setName("Grunthos the Flatulent");
		unequalRod.setDescription("Poetmaster of the Azgoths of Kria");
		unequalRod.setId(5);

		// Check that ring and unequalRing are not the same
		assertFalse(rod.equals(unequalRod));

		/* --- Check hash values between equal and unequal rings ----------- */

		assertEquals(rod.hashCode(), rod.hashCode());
		assertEquals(rod.hashCode(), rod.hashCode());
		assertFalse(rod.hashCode() == unequalRod.hashCode());

		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests the copying and cloning operations of SFRRods.
	 * </p>
	 * <!-- end-UML-doc -->
	 */
	@Test
	public void checkCopying() {
		// begin-user-code

		/* --- Testing the copy operation ---------------------------------- */

		// Create a SFRRod to copy from
		SFRRod rod = new SFRRod();
		Ring reflector = new Ring();
		rod.setName("Phouchg");
		rod.setDescription("Chosen to recieve DeepThought's answer");
		rod.setId(7001);
		rod.setReflector(reflector);

		// Create an empty SFRRod to copy to
		SFRRod rodCopy = new SFRRod();

		// Copy the contents of rod to rodCopy
		rodCopy.copy(rod);

		// Check rod and rodCopy are identical
		assertTrue(rod.equals(rodCopy));

		// Try copying the contents of a null rod
		rodCopy.copy(null);

		// Check to see that rodCopy remains unchanged
		assertTrue(rodCopy.equals(rod));

		/* --- Testing the cloning operation ------------------------------- */

		// Clone the first rod
		Object rodClone = rod.clone();

		// Check that rodClone isn't null
		assertNotNull(rodClone);

		// Check that rod and rodClone are identical
		assertTrue(rodClone.equals(rod));

		return;
		// end-user-code
	}

}