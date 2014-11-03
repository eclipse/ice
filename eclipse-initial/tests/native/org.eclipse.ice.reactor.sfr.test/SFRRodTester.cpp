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
#include <assembly/SFRRod.h>


using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(SFRRodTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
		// begin-user-code

		/* --- Test the nullary constructor -------------------------------- */

		// Create a SFRRod for testing
		SFRRod rodOne;

		// Create a default reflector to test against
		Material material("SS-316");
		material.setDescription("Stainless Steel");
		std::shared_ptr<Material> matPtr = std::make_shared<Material>(material);

		Ring ring;
		std::shared_ptr<Ring> reflector = std::make_shared<Ring>(ring);
		reflector->setMaterial(matPtr);
		reflector->setHeight(0.0);
		reflector->setInnerRadius(0.0);
		reflector->setOuterRadius(26.666);

		// Check the name description and ID
		BOOST_REQUIRE_EQUAL("SFR Rod 1", rodOne.getName());
		BOOST_REQUIRE_EQUAL("SFR Rod 1's Description", rodOne.getDescription());
		BOOST_REQUIRE_EQUAL(1, rodOne.getId());

		// Check the reflector
		BOOST_REQUIRE(reflector && rodOne.getReflector());
		BOOST_REQUIRE(*reflector == *rodOne.getReflector());


		/* --- Test parameterized constructor with name -------------------- */

		// Create a SFRRod with a name specified
		SFRRod rodTwo("Ravenous Bugblatter Beast of Traal");

		// Check the name description and ID
		BOOST_REQUIRE_EQUAL("Ravenous Bugblatter Beast of Traal", rodTwo.getName());
		BOOST_REQUIRE_EQUAL("SFR Rod 1's Description", rodTwo.getDescription());
		BOOST_REQUIRE_EQUAL(1, rodTwo.getId());

		// Check the reflector
		BOOST_REQUIRE(reflector && rodTwo.getReflector());
		BOOST_REQUIRE(*reflector == *rodTwo.getReflector());

		// end-user-code
}

BOOST_AUTO_TEST_CASE(checkAttributes) {
		// begin-user-code
		
		// Create a SFRRod for testing
		SFRRod rod;

		// Create a reflector for testing
		Ring reflector;
		Material material("Almost, but not quite, entirely unlike tea");
		std::shared_ptr<Material> materialPtr = std::make_shared<Material>(material);
		reflector.setMaterial(materialPtr);

		// Set the reflector rod and check it
		std::shared_ptr<Ring> reflectorPtr = std::make_shared<Ring>(reflector);
		rod.setReflector(reflectorPtr);
		BOOST_REQUIRE(rod.getReflector());
		BOOST_REQUIRE(reflector == *rod.getReflector());

		// Try setting an invalid reflector
		std::shared_ptr<Ring> nullRingPtr;
		rod.setReflector(nullRingPtr);

		// Check that reflector remains unchanged
		BOOST_REQUIRE(reflector == *rod.getReflector());

		// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
		// begin-user-code
		
		/* --- Check equality between like rods --------------------------- */

		// Construct a SFRRod (with reflector) to test against
		SFRRod rod;
		Ring reflector;
		std::shared_ptr<Ring> reflectorPtr = std::make_shared<Ring>(reflector);
		rod.setReflector(reflectorPtr);

		rod.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		rod.setDescription("Worst poet in the universe");
		rod.setId(5);

		// Construct a SFRRod equal to the first
		SFRRod equalRod;
		Ring equalReflector;
		std::shared_ptr<Ring> equalReflPtr = std::make_shared<Ring>(equalReflector);
		equalRod.setReflector(equalReflPtr);

		equalRod.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		equalRod.setDescription("Worst poet in the universe");
		equalRod.setId(5);

		// Check that equals is reflexive and symmetric
		BOOST_REQUIRE(rod == equalRod);
		BOOST_REQUIRE(rod == equalRod && equalRod == rod);

		/* --- Check transitivity with similar rods ----------------------- */

		// Construct another SFRRod equal to the first, for testing transitivity
		SFRRod transRod;
		Ring transReflector;
		std::shared_ptr<Ring> transReflPtr = std::make_shared<Ring>(transReflector);
		transRod.setReflector(transReflPtr);

		transRod.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		transRod.setDescription("Worst poet in the universe");
		transRod.setId(5);

		// Check for transitivity
		if ((rod == transRod) && (transRod == equalRod))
			BOOST_REQUIRE(rod == equalRod);
		else
			BOOST_FAIL("SFRRod failed transitivity test.");


		/* --- Check inequality between two dissimilar rods ---------------- */

		// Construct a SFRRod unequal to the first
		SFRRod unequalRod;
		Ring unequalReflector;
		std::shared_ptr<Ring> unequalReflPtr = std::make_shared<Ring>(unequalReflector);
		unequalRod.setReflector(unequalReflPtr);

		unequalRod.setName("Grunthos the Flatulent");
		unequalRod.setDescription("Poetmaster of the Azgoths of Kria");
		unequalRod.setId(5);

		// Check that ring and unequalRing are not the same
		BOOST_REQUIRE(!(rod == unequalRod));
		
		return;
		// end-user-code		
}

BOOST_AUTO_TEST_CASE(checkCopying) {
		// begin-user-code
		
		/* --- Testing the copy operation ---------------------------------- */

		// Create a SFRRod to copy from
		SFRRod rod;
		Ring reflector;
		std::shared_ptr<Ring> reflectorPtr = std::make_shared<Ring>(reflector);

		rod.setName("Phouchg");
		rod.setDescription("Chosen to recieve DeepThought's answer");
		rod.setId(7001);
		rod.setReflector(reflectorPtr);

		// Create an empty SFRRod and copy contents
		SFRRod rodCopy;
		rodCopy = SFRRod(rod);

		// Check rod and rodCopy are identical
		BOOST_REQUIRE(rod == rodCopy);


		/* --- Testing the cloning operation ------------------------------- */

		// Clone the first rod

		std::shared_ptr<Identifiable> rodClone;
		rodClone = rod.clone();
		std::shared_ptr<SFRRod> rodCloneCast = std::dynamic_pointer_cast<SFRRod>(rodClone);

		// Check that rodClone isn't null
		BOOST_REQUIRE(rodCloneCast);

		// Check that rod and rodClone are identical
		BOOST_REQUIRE(*rodCloneCast == rod);
				
		return;
		// end-user-code	
}
BOOST_AUTO_TEST_SUITE_END()
