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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.eclipse.ice.reactor.sfr.core.MaterialBlock;
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;
import org.junit.Test;

/**
 * <p>
 * Tests the operations of the MaterialBlock class.
 * </p>
 * 
 * @author w5q
 */
public class MaterialBlockTester {
	/**
	 * <p>
	 * Tests the constructors and default values for the MaterialBlock class.
	 * </p>
	 * 
	 */
	@Test
	public void checkConstruction() {

		// Create new MaterialBlock for testing
		MaterialBlock block = new MaterialBlock();

		// Test nullary constructor default values
		assertEquals("MaterialBlock 1", block.getName());
		assertEquals("MaterialBlock 1 Description", block.getDescription());
		assertEquals(1, block.getId());
		assertEquals(0.0, block.getVertPosition(), 0.0);

	}

	/**
	 * <p>
	 * Tests the getter and setter for the vertPosition attribute.
	 * </p>
	 * 
	 */
	@Test
	public void checkVertPosition() {

		// Initialize new material block
		MaterialBlock block = new MaterialBlock();

		// Try setting z-displacement to illegal value, should remain unchanged
		block.setVertPosition(-1.0);
		assertEquals(0, block.getVertPosition(), 0.0);

		// Try changing z-displacement to a legal non-zero value
		block.setVertPosition(42.0);
		assertEquals(42.0, block.getVertPosition(), 0.0);

	}

	/**
	 * <p>
	 * Tests the getters of rings in the MaterialBlock.
	 * </p>
	 * 
	 */
	@Test
	public void checkRing() {

		// Initialize a material block for testing
		MaterialBlock block = new MaterialBlock();

		// Initialize rings for testing
		Ring ringOne = new Ring();
		Ring ringTwo = new Ring();

		// Set the name, inner radius, and outer radius of a ring
		ringOne.setName("Slartibartfast");
		ringOne.setInnerRadius(10.5);
		ringOne.setOuterRadius(12.5);

		// Set the name, inner radius and outer radius of another ring
		ringTwo.setName("Bartislartfast");
		ringTwo.setInnerRadius(12.6);
		ringTwo.setOuterRadius(13.25);

		// Add the rings to the material block, make sure they added
		assertTrue(block.addRing(ringOne));
		assertTrue(block.addRing(ringTwo));

		// Try to add ringOne again, should not take
		assertFalse(block.addRing(ringOne));

		/* --- Test getting rings from a material block by name ------------ */

		// Try getting a ring by a valid name
		assertEquals(ringOne, block.getRing("Slartibartfast"));

		// Try getting a ring by a name that doesn't exist
		assertNull(block.getRing("Bob"));

		// Try getting a ring with a null name
		assertNull(block.getRing(null));

		/* --- Test getting rings from a material block by radius ----------- */

		// Try getting a ring by a valid inner radius
		assertEquals(ringOne, block.getRing(10.5));
		assertEquals(ringTwo, block.getRing(12.6));

		// Try getting a ring by a valid outer radius
		assertEquals(ringOne, block.getRing(12.5));
		assertEquals(ringTwo, block.getRing(13.25));

		// Try getting a ring by a radius that does not exist
		assertNull(block.getRing(9000));

		// Try getting a ring by a null radius
		assertNull(block.getRing(null));

		/* --- Test getting the list of rings in a material block ---------- */

		// Check the list of rings contained in block
		assertEquals(2, block.getRings().size());
		assertEquals(ringOne, block.getRings().get(0));
		assertEquals(ringTwo, block.getRings().get(1));

	}

	/**
	 * <p>
	 * Tests the addition and removal of Rings in the MaterialBlock.
	 * </p>
	 * 
	 */
	@Test
	public void checkRingAddRem() {

		// Initialize a material block for testing
		MaterialBlock block = new MaterialBlock();

		// Initialize rings for testing
		Ring ringOne = new Ring();
		Ring ringTwo = new Ring();

		// Set ringOne to be inside ringTwo without overlap
		ringOne.setInnerRadius(3.2);
		ringOne.setOuterRadius(3.7);
		ringTwo.setInnerRadius(3.99);
		ringTwo.setOuterRadius(4.0);

		// Initialize a boolean flags for addition/removal of rings
		boolean ringAdded = false;
		boolean ringRemoved = false;

		// Set the name of a ring
		ringOne.setName("Agrajag");

		/* --- Test the addition of rings to a material block -------------- */

		// Add ringOne to block and check if ringAdded flagged as true
		ringAdded = block.addRing(ringOne);
		assertTrue(ringAdded);

		// Try to add a null ring from block, ringAdded becomes false
		ringAdded = block.addRing(null);
		assertFalse(ringAdded);

		// Set the name, description of another ring
		ringTwo.setName("Pot of petunias");

		// Add ringTwo to block and check if ringAdded flagged as true
		ringAdded = block.addRing(ringTwo);
		assertTrue(ringAdded);

		/* --- Test the removal of rings from a material block ------------- */

		// Remove ringOne from block and check if ringRemoved flagged
		ringRemoved = block.removeRing("Agrajag");
		assertTrue(ringRemoved);

		// Try to remove a null ring from block, ringRemoved becomes false
		ringRemoved = block.removeRing(null);
		assertFalse(ringRemoved);

		// Remove ringTwo from block and check if ringRemoved flagged
		ringRemoved = block.removeRing("Pot of petunias");
		assertTrue(ringRemoved);
	}

	/**
	 * <p>
	 * Tests the equality operation of MaterialBlocks.
	 * </p>
	 */
	@Test
	public void checkEquality() {

		/* --- Check equality between like material blocks ----------------- */

		// Construct a material block to test against
		MaterialBlock block = new MaterialBlock();
		block.setName("Infinite Improbability Drive");
		block.setDescription("Probably Improbable");
		block.setId(30);
		block.setVertPosition(19.62);

		// Create rings to add to the material block
		Ring ringOne = new Ring();
		Ring ringTwo = new Ring();
		ringOne.setInnerRadius(3.4);
		ringOne.setOuterRadius(3.785);
		ringTwo.setInnerRadius(5.21);
		ringTwo.setOuterRadius(7.99);

		// Add the rings to the the material block
		block.addRing(ringOne);
		block.addRing(ringTwo);

		// Construct a material block equal to the first
		MaterialBlock equalBlock = new MaterialBlock();
		equalBlock.setName("Infinite Improbability Drive");
		equalBlock.setDescription("Probably Improbable");
		equalBlock.setId(30);
		equalBlock.setVertPosition(19.62);

		// Create rings identical to the previous ones
		Ring ringOneCopy = new Ring();
		Ring ringTwoCopy = new Ring();
		ringOneCopy.setInnerRadius(3.4);
		ringOneCopy.setOuterRadius(3.785);
		ringTwoCopy.setInnerRadius(5.21);
		ringTwoCopy.setOuterRadius(7.99);

		// Add the rings to the the equal material block
		equalBlock.addRing(ringOneCopy);
		equalBlock.addRing(ringTwoCopy);

		// Check that equals() is reflexive and symmetric
		assertTrue(block.equals(block));
		assertTrue(block.equals(equalBlock) && equalBlock.equals(block));

		// Check equals() fails with illegal material blocks
		assertFalse(block==null);

		/* --- Check transitivity with similar material blocks ------------- */

		// Construct another material block equal to the first, for testing
		// transitivity
		MaterialBlock transBlock = new MaterialBlock();
		transBlock.setName("Infinite Improbability Drive");
		transBlock.setDescription("Probably Improbable");
		transBlock.setId(30);
		transBlock.setVertPosition(19.62);

		// Create another set of rings identical to the first ones
		Ring ringOneCopy2 = new Ring();
		Ring ringTwoCopy2 = new Ring();
		ringOneCopy2.setInnerRadius(3.4);
		ringOneCopy2.setOuterRadius(3.785);
		ringTwoCopy2.setInnerRadius(5.21);
		ringTwoCopy2.setOuterRadius(7.99);

		// Add the rings to the transitive material block
		transBlock.addRing(ringOneCopy2);
		transBlock.addRing(ringTwoCopy2);

		// Check for transitivity
		if (block.equals(transBlock) && transBlock.equals(equalBlock)) {
			assertTrue(block.equals(equalBlock));
		} else {
			fail();
		}

		/* --- Check inequality between two dissimilar material blocks ----- */

		// Construct a material block unequal to the first one
		MaterialBlock unequalBlock = new MaterialBlock();
		unequalBlock.setName("Finite Probability Drive");
		unequalBlock.setDescription("Improbably Probable");
		unequalBlock.setId(31);
		unequalBlock.setVertPosition(18.424242);

		// Create rings not identical to the first
		Ring ringThree = new Ring();
		Ring ringFour = new Ring();
		ringThree.setInnerRadius(17.0);
		ringThree.setOuterRadius(54.33);
		ringFour.setInnerRadius(55.0);
		ringFour.setOuterRadius(55.01);

		// Add the rings to the unequal material block
		unequalBlock.addRing(ringThree);
		unequalBlock.addRing(ringFour);

		// Check that block and unequalBlock are not the same
		assertFalse(block.equals(unequalBlock));

		/* --- Check hash values between equal and unequal material blocks - */

		assertEquals(block.hashCode(), block.hashCode());
		assertEquals(block.hashCode(), equalBlock.hashCode());
		assertFalse(block.hashCode() == unequalBlock.hashCode());

		return;
	}

	/**
	 * <p>
	 * Tests the copying and cloning operations of MaterialBlocks.
	 * </p>
	 */
	@Test
	public void checkCopying() {

		/* --- Testing the copy operation ---------------------------------- */

		// Create a material block to copy from
		MaterialBlock block = new MaterialBlock();
		block.setName("Prosteetnic Vogon Jeltz");
		block.setDescription("Vogon ship captain");
		block.setId(7);
		block.setVertPosition(42.4242);

		// Create a ring to add to the MaterialBlock
		Ring ring = new Ring();
		ring.setInnerRadius(24);
		ring.setOuterRadius(42);

		// Add the ring to the block's TreeSet
		block.addRing(ring);

		// Create an empty material block to copy to
		MaterialBlock blockCopy = new MaterialBlock();

		// Copy the contents of block to blockCopy
		blockCopy.copy(block);

		// Check block and blockCopy are identical
		assertTrue(block.equals(blockCopy));

		// Try copying the contents of a null material block
		blockCopy.copy(null);

		// Check to see that blockCopy remains unchanged
		assertTrue(blockCopy.equals(block));

		/* --- Testing the cloning operation ------------------------------- */

		// Clone the first material block
		Object blockClone = block.clone();

		// Check that blockClone isn't null
		assertNotNull(blockClone);

		// Check that block and blockClone are identical
		assertTrue(blockClone.equals(block));

		return;
	}

	/**
	 * <p>
	 * Tests the compareTo method of MaterialBlocks.
	 * </p>
	 */
	@Test
	public void checkComparison() {

		// Create a material block for testing
		MaterialBlock blockOne = new MaterialBlock();
		blockOne.setVertPosition(1.007);

		// Create a material block at a different vertical position (higher)
		MaterialBlock blockTwo = new MaterialBlock();
		blockTwo.setVertPosition(1.34);

		// Check that blockOne is below blockTwo (and vice versa)
		assertEquals(-1, blockOne.compareTo(blockTwo));
		assertEquals(1, blockTwo.compareTo(blockOne));

		// Create a third block that overlaps one of the others (blockOne
		// chosen arbitrarily)
		MaterialBlock blockThree = new MaterialBlock();
		blockThree.setVertPosition(1.007);

		// Check that blockOne and blockThree overlap
		assertEquals(0, blockOne.compareTo(blockThree));

	}
}