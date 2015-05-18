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
import org.junit.Test;

/**
 * <p>
 * Tests the operations of the Ring class.
 * </p>
 * 
 * @author w5q
 */
public class RingTester {
	/**
	 * <p>
	 * Tests the constructors and default values for the Ring class.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		/*
		 * Three constructors to test: Ring(); Ring(String name) Ring(String
		 * name, Material material, double height, double innerRadius, double
		 * outerRadius)
		 */

		/* --- Test the nullary constructor -------------------------------- */

		// Create a Ring for testing
		Ring ringOne = new Ring();

		// Check the name, description and ID
		assertEquals("Ring 1", ringOne.getName());
		assertEquals("Ring 1 Description", ringOne.getDescription());
		assertEquals(1, ringOne.getId());

		// Check the material, height and radii default values
		assertNotNull(ringOne.getMaterial());
		assertEquals(0.0, ringOne.getHeight(), 0.0);
		assertEquals(0.0, ringOne.getInnerRadius(), 0.0);
		assertEquals(1.0, ringOne.getOuterRadius(), 0.0);

		/* --- Test parameterized constructor with name -------------------- */

		// Create a Ring for testing with name specified
		Ring ringTwo = new Ring("Babelfish");

		// Check the name, description and ID
		assertEquals("Babelfish", ringTwo.getName());
		assertEquals("Ring 1 Description", ringTwo.getDescription());
		assertEquals(1, ringTwo.getId());

		// Check the material, height and radii default values
		assertNotNull(ringTwo.getMaterial());
		assertEquals(0.0, ringTwo.getHeight(), 0.0);
		assertEquals(0.0, ringTwo.getInnerRadius(), 0.0);
		assertEquals(1.0, ringTwo.getOuterRadius(), 0.0);

		/*
		 * --- Test parameterized constructor with name, height, material, ---
		 * inner radius and outer radius -------------------------------
		 */
		Material blubber = new Material();

		// Create a Ring for testing with name, height, material, and radii
		Ring ringThree = new Ring("Sperm Whale", blubber, 1.0, 2.55, 4.039);

		// Check the name, description and ID
		assertEquals("Sperm Whale", ringThree.getName());
		assertEquals("Ring 1 Description", ringThree.getDescription());
		assertEquals(1, ringThree.getId());

		// Check the material, height and radii
		assertEquals(blubber, ringThree.getMaterial());
		assertEquals(1.0, ringThree.getHeight(), 0.0);
		assertEquals(2.55, ringThree.getInnerRadius(), 0.0);
		assertEquals(4.039, ringThree.getOuterRadius(), 0.0);

	}

	/**
	 * <p>
	 * Checks the getters and setters for the height, innerRadius, outerRadius,
	 * and material attributes.
	 * </p>
	 * 
	 */
	@Test
	public void checkAttributes() {

		// Create a nullary Ring for testing
		Ring ring = new Ring();

		// Create a material to use for testing
		Material material = new Material("Pan Galactic Gargle Blaster");

		// Set the height, innerRadius, outerRadius and material
		ring.setHeight(53.2);
		ring.setInnerRadius(12.6);
		ring.setOuterRadius(13.9);
		ring.setMaterial(material);

		// Check the height, innerRadius, outerRadius and material
		assertEquals(53.2, ring.getHeight(), 0.0);
		assertEquals(12.6, ring.getInnerRadius(), 0.0);
		assertEquals(13.9, ring.getOuterRadius(), 0.0);
		assertEquals(material, ring.getMaterial());

		// Check radii are unchanged
		assertEquals(12.6, ring.getInnerRadius(), 0.0);
		assertEquals(13.9, ring.getOuterRadius(), 0.0);

		// Try setting height, radii and material to illegal values
		ring.setHeight(-1.0);
		ring.setInnerRadius(-8.7);
		ring.setOuterRadius(-20.4);
		ring.setMaterial(null);

		// Check attributes are unchanged
		assertEquals(53.2, ring.getHeight(), 0.0);
		assertEquals(12.6, ring.getInnerRadius(), 0.0);
		assertEquals(13.9, ring.getOuterRadius(), 0.0);
		assertEquals(material, ring.getMaterial());

	}

	/**
	 * <p>
	 * Checks the getters and setters for the height, innerRadius, outerRadius,
	 * and material attributes.
	 * </p>
	 * 
	 */
	@Test
	public void checkCompareTo() {

		// Create "smaller" ring for testing
		Ring smallRing = new Ring();
		smallRing.setInnerRadius(6.2);
		smallRing.setOuterRadius(7.8);

		// Create a "larger" ring for testing
		Ring largeRing = new Ring();
		largeRing.setInnerRadius(7.8);
		largeRing.setOuterRadius(8.4);

		// Check that smallRing compared to largeRing returns -1 (inside)
		assertEquals(-1, smallRing.compareTo(largeRing));

		// Check that largeRing compared to smallRing returns +1 (outside)
		assertEquals(1, largeRing.compareTo(smallRing));

		// Test for overlapping rings. The smaller ring is chosen arbitrarily to
		// test against, but you could just as easily use the larger one

		// Create a ring that overlaps innerRadius
		Ring overlapInner = new Ring();
		overlapInner.setInnerRadius(5.9);
		overlapInner.setOuterRadius(6.7);

		// Check that smallRing compared to overlapInner returns 0 (overlap)
		assertEquals(0, smallRing.compareTo(overlapInner));

		// Create a ring that overlaps outerRadius
		Ring overlapOuter = new Ring();
		overlapOuter.setInnerRadius(7.7);
		overlapOuter.setOuterRadius(8.2);

		// Check that smallRing compared to overlapOuter returns 0 (overlap)
		assertEquals(0, smallRing.compareTo(overlapOuter));

		// Create a ring that overlaps both radii
		Ring totalOverlap = new Ring();
		totalOverlap.setInnerRadius(5.7);
		totalOverlap.setOuterRadius(8.3);

		// Check that smallRing compared to totalOverlap returns 0 (overlap)
		assertEquals(0, smallRing.compareTo(totalOverlap));

	}

	/**
	 * <p>
	 * Tests the equality operation of Rings.
	 * </p>
	 */
	@Test
	public void checkEquality() {

		/* --- Check equality between like rings --------------------------- */

		// Construct a ring (with a material) to test against
		Ring ring = new Ring();
		Material material = new Material("Awfulness");
		ring.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		ring.setDescription("Worst poet in the universe");
		ring.setHeight(23.4);
		ring.setInnerRadius(0.001);
		ring.setOuterRadius(0.01);
		ring.setMaterial(material);

		// Construct a ring (with material) equal to the first
		Ring equalRing = new Ring();
		Material equalMaterial = new Material("Awfulness");
		equalRing
				.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		equalRing.setDescription("Worst poet in the universe");
		equalRing.setHeight(23.4);
		equalRing.setInnerRadius(0.001);
		equalRing.setOuterRadius(0.01);
		equalRing.setMaterial(equalMaterial);

		// Check that equals() is reflexive and symmetric
		assertTrue(ring.equals(ring));
		assertTrue(ring.equals(equalRing) && equalRing.equals(ring));

		// Check equals() fails with illegal rings
		assertFalse(ring==null);

		/* --- Check transitivity with similar rings ----------------------- */

		// Construct another ring equal to the first, for testing transitivity
		Ring transRing = new Ring();
		Material transMaterial = new Material("Awfulness");
		transRing
				.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		transRing.setDescription("Worst poet in the universe");
		transRing.setHeight(23.4);
		transRing.setInnerRadius(0.001);
		transRing.setOuterRadius(0.01);
		transRing.setMaterial(transMaterial);

		// Check for transitivity
		if (ring.equals(transRing) && transRing.equals(equalRing)) {
			assertTrue(ring.equals(equalRing));
		} else {
			fail();
		}
		/* --- Check inequality between two dissimilar rings --------------- */

		// Construct a ring (with a material) unequal to the first
		Ring unequalRing = new Ring();
		Material unequalMaterial = new Material("Ickiness");
		unequalRing.setName("Grunthos the Flatulent");
		unequalRing.setDescription("Poetmaster of the Azgoths of Kria");
		unequalRing.setHeight(64.77);
		unequalRing.setInnerRadius(10.222);
		unequalRing.setOuterRadius(100.2);
		unequalRing.setMaterial(unequalMaterial);

		// Check that ring and unequalRing are not the same
		assertFalse(ring.equals(unequalRing));

		/* --- Check hash values between equal and unequal rings ----------- */

		assertEquals(ring.hashCode(), ring.hashCode());
		assertEquals(ring.hashCode(), equalRing.hashCode());
		assertFalse(ring.hashCode() == unequalRing.hashCode());

		return;
	}

	/**
	 * <p>
	 * Tests the copying and cloning operations of Rings.
	 * </p>
	 */
	@Test
	public void checkCopying() {

		/* --- Testing the copy operation ---------------------------------- */

		// Create a ring (with material) to copy from
		Ring ring = new Ring();
		Material material = new Material("Fleshy bits");
		ring.setName("Phouchg");
		ring.setDescription("Chosen to recieve DeepThought's answer");
		ring.setId(7001);
		ring.setHeight(54.6);
		ring.setInnerRadius(1.78);
		ring.setOuterRadius(6.77);
		ring.setMaterial(material);

		// Create an empty ring to copy to
		Ring ringCopy = new Ring();

		// Copy the contents of ring to ringCopy
		ringCopy.copy(ring);

		// Check ring and ringCopy are identical
		assertTrue(ring.equals(ringCopy));

		// Try copying the contents of a null ring
		ringCopy.copy(null);

		// Check to see that ringCopy remains unchanged
		assertTrue(ringCopy.equals(ring));

		/* --- Testing the cloning operation ------------------------------- */

		// Clone the first ring
		Object ringClone = ring.clone();

		// Check that ringClone isn't null
		assertNotNull(ringClone);

		// Check that ring and ringClone are identical
		assertTrue(ringClone.equals(ring));

		return;
	}

}