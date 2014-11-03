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
#define BOOST_TEST_DYN_LINK
#define BOOST_TEST_MODULE Regression
#include <boost/test/included/unit_test.hpp>
#include <MaterialBlock.h>
#include <memory>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(MaterialBlockTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code

	// Create new MaterialBlock for testing with nullary constructor
	MaterialBlock block;

	// Test nullary constructor default values
	BOOST_REQUIRE_EQUAL("MaterialBlock 1", block.getName());
	BOOST_REQUIRE_EQUAL("MaterialBlock 1 Description", block.getDescription());
	BOOST_REQUIRE_EQUAL(1, block.getId());
	BOOST_REQUIRE_EQUAL(0.0, block.getVertPosition());

	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkVertPosition) {
	// begin-user-code

	// Initialize new material block
	MaterialBlock block;

	// Try setting z-displacement to illegal value, should remain unchanged
	block.setVertPosition(-1.0);
	BOOST_REQUIRE_EQUAL(0, block.getVertPosition());

	// Try changing z-displacement to a legal non-zero value
	block.setVertPosition(42.0);
	BOOST_REQUIRE_EQUAL(42.0, block.getVertPosition());

	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkRing) {
	// begin-user-code

	// Initialize a material block for testing
	MaterialBlock block;

	// Initialize rings for testing
	std::shared_ptr<Ring> ringOne = std::make_shared<Ring>(), ringTwo =
			std::make_shared<Ring>();

	// Set the name, inner radius, and outer radius of a ring
	ringOne->setName("Slartibartfast");
	ringOne->setInnerRadius(10.5);
	ringOne->setOuterRadius(12.5);

	// Set the name, inner radius and outer radius of another ring
	ringTwo->setName("Bartislartfast");
	ringTwo->setInnerRadius(12.6);
	ringTwo->setOuterRadius(13.25);

	// Add the rings to the material block, make sure they added
	BOOST_REQUIRE_EQUAL(true, block.addRing(ringOne));
	BOOST_REQUIRE_EQUAL(true, block.addRing(ringTwo));

	// Try to add ringOne again, should not take
	BOOST_REQUIRE_EQUAL(false, block.addRing(ringOne));

	/* --- Test getting rings from a material block by name ------------ */

	// Try getting a ring by a valid name.
	BOOST_REQUIRE(ringOne == block.getRing("Slartibartfast"));

	// Try getting a ring by a name that doesn't exist
	BOOST_REQUIRE(!block.getRing("Bob"));

	/* --- Test getting rings from a material block by radius ----------- */

	// Try getting a ring by a valid inner radius. This actually checks the
	// shared pointer for equality, not the contents of the ring.
	BOOST_REQUIRE(ringOne == block.getRing(10.5));
	BOOST_REQUIRE(ringTwo == block.getRing(12.6));

	// Try getting a ring by a valid outer radius
	BOOST_REQUIRE(ringOne == block.getRing(12.5));
	BOOST_REQUIRE(ringTwo == block.getRing(13.25));

	// Try getting a ring by a radius that does not exist
	BOOST_REQUIRE(!block.getRing(9000.0));

	/* --- Test getting the list of rings in a material block ---------- */

	// Check the list of rings contained in block
	BOOST_REQUIRE_EQUAL(2, block.getRings().size());
	BOOST_REQUIRE(
			ringOne == block.getRings()[0] || ringOne == block.getRings()[1]);
	BOOST_REQUIRE(
			ringTwo == block.getRings()[0] || ringTwo == block.getRings()[1]);

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkRingAddRem) {
	// begin-user-code

	// Initialize a material block for testing
	MaterialBlock block;

	// Initialize rings for testing
	std::shared_ptr<Ring> ringOne = std::make_shared<Ring>(), ringTwo =
			std::make_shared<Ring>(), nullRing;

	// Set ringOne to be inside ringTwo without overlap
	ringOne->setInnerRadius(3.2);
	ringOne->setOuterRadius(3.7);
	ringTwo->setInnerRadius(3.99);
	ringTwo->setOuterRadius(4.0);

	// Initialize a boolean flags for addition/removal of rings
	bool ringAdded = false;
	bool ringRemoved = false;

	// Set the name of a ring
	ringOne->setName("Agrajag");

	/* --- Test the addition of rings to a material block -------------- */

	// Add ringOne to block and check if ringAdded flagged as true
	ringAdded = block.addRing(ringOne);
	BOOST_REQUIRE(ringAdded);

	// Try to add a null ring from block, ringAdded becomes false
	ringAdded = block.addRing(nullRing);
	BOOST_REQUIRE(!ringAdded);

	// Set the name, description of another ring
	ringTwo->setName("Pot of petunias");

	// Add ringTwo to block and check if ringAdded flagged as true
	ringAdded = block.addRing(ringTwo);
	BOOST_REQUIRE(ringAdded);

	/* --- Test the removal of rings from a material block ------------- */

	// Remove ringOne from block and check if ringRemoved flagged
	ringRemoved = block.removeRing("Agrajag");
	BOOST_REQUIRE(ringRemoved);

	// Try to remove a null ring from block, ringRemoved becomes false
	ringRemoved = block.removeRing("");
	BOOST_REQUIRE(!ringRemoved);

	// Remove ringTwo from block and check if ringRemoved flagged
	ringRemoved = block.removeRing("Pot of petunias");
	BOOST_REQUIRE(ringRemoved);

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	// Local Declarations
	MaterialBlock block, equalBlock, transBlock, unequalBlock;
	std::shared_ptr<Ring> ringOne = std::make_shared<Ring>(), ringTwo =
			std::make_shared<Ring>(), ringOneCopy = std::make_shared<Ring>();
	std::shared_ptr<Ring> ringTwoCopy = std::make_shared<Ring>(), ringOneCopy2 =
			std::make_shared<Ring>(), ringTwoCopy2 = std::make_shared<Ring>();
	std::shared_ptr<Ring> ringThree = std::make_shared<Ring>(), ringFour =
			std::make_shared<Ring>();

	/* --- Check equality between like material blocks ----------------- */

	// Construct a material block to test against
	block.setName("Infinite Improbability Drive");
	block.setDescription("Probably Improbable");
	block.setId(30);
	block.setVertPosition(19.62);

	// Create rings to add to the material block
	ringOne->setInnerRadius(3.4);
	ringOne->setOuterRadius(3.785);
	ringTwo->setInnerRadius(5.21);
	ringTwo->setOuterRadius(7.99);

	// Add the rings to the the material block
	block.addRing(ringOne);
	block.addRing(ringTwo);

	// Construct a material block equal to the first
	equalBlock.setName("Infinite Improbability Drive");
	equalBlock.setDescription("Probably Improbable");
	equalBlock.setId(30);
	equalBlock.setVertPosition(19.62);

	// Create rings identical to the previous ones
	ringOneCopy->setInnerRadius(3.4);
	ringOneCopy->setOuterRadius(3.785);
	ringTwoCopy->setInnerRadius(5.21);
	ringTwoCopy->setOuterRadius(7.99);

	// Add the rings to the the equal material block
	equalBlock.addRing(ringOneCopy);
	equalBlock.addRing(ringTwoCopy);

	// Check that equals() is reflexive and symmetric
	BOOST_REQUIRE(block == block);
	BOOST_REQUIRE(block == equalBlock && equalBlock == block);

	/* --- Check transitivity with similar material blocks ------------- */

	// Construct another material block equal to the first, for testing transitivity
	transBlock.setName("Infinite Improbability Drive");
	transBlock.setDescription("Probably Improbable");
	transBlock.setId(30);
	transBlock.setVertPosition(19.62);

	// Create another set of rings identical to the first ones
	ringOneCopy2->setInnerRadius(3.4);
	ringOneCopy2->setOuterRadius(3.785);
	ringTwoCopy2->setInnerRadius(5.21);
	ringTwoCopy2->setOuterRadius(7.99);

	// Add the rings to the transitive material block
	transBlock.addRing(ringOneCopy2);
	transBlock.addRing(ringTwoCopy2);

	// Check for transitivity
	BOOST_REQUIRE(block == transBlock && transBlock == equalBlock);

	/* --- Check inequality between two dissimilar material blocks ----- */

	// Construct a material block unequal to the first one
	unequalBlock.setName("Finite Probability Drive");
	unequalBlock.setDescription("Improbably Probable");
	unequalBlock.setId(31);
	unequalBlock.setVertPosition(18.424242);

	// Create rings not identical to the first
	ringThree->setInnerRadius(17.0);
	ringThree->setOuterRadius(54.33);
	ringFour->setInnerRadius(55.0);
	ringFour->setOuterRadius(55.01);

	// Add the rings to the unequal material block
	unequalBlock.addRing(ringThree);
	unequalBlock.addRing(ringFour);

	// Check that block and unequalBlock are not the same
	BOOST_REQUIRE(!(block == unequalBlock));

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkCopying) {
	// begin-user-code

	// Local Declarations
	MaterialBlock block;
	std::shared_ptr<Ring> ring = std::make_shared<Ring>();

	/* --- Testing the copy operation ---------------------------------- */

	// Create a material block to copy from
	block.setName("Prosteetnic Vogon Jeltz");
	block.setDescription("Vogon ship captain");
	block.setId(7);
	block.setVertPosition(42.4242);

	// Create a ring to add to the MaterialBlock
	ring->setInnerRadius(24);
	ring->setOuterRadius(42);

	// Add the ring to the block's TreeSet
	block.addRing(ring);

	// Create a copy
	MaterialBlock blockCopy(block);

	// Check block and blockCopy are identical
	BOOST_REQUIRE(block == blockCopy);

	/* --- Testing the cloning operation ------------------------------- */

	// Clone the first material block
	auto blockClone = block.clone();

	// Check that blockClone isn't null
	BOOST_REQUIRE(blockClone);

	// Check that block and blockClone are identical
	BOOST_REQUIRE(block == *(std::dynamic_pointer_cast<MaterialBlock>(blockClone)));

	return;
	// end-user-code
}
BOOST_AUTO_TEST_SUITE_END()
