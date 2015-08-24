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
#include <assembly/Ring.h>
#include <Material.h>
#include <string>
#include <memory>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(RingTester_testSuite)


BOOST_AUTO_TEST_CASE(checkConstruction) {
		// begin-user-code

		/* Three constructors to test:
		  	Ring();
		  	Ring(std::string name)
		 	Ring(std::string name, Material material, double height,
				double innerRadius, double outerRadius)
		 */


		/* --- Test the nullary constructor -------------------------------- */

		// Create a Ring for testing
		Ring ringOne;

		// Check the name, description and ID
		BOOST_REQUIRE_EQUAL("Ring 1", ringOne.getName());
		BOOST_REQUIRE_EQUAL("Ring 1 Description", ringOne.getDescription());
		BOOST_REQUIRE_EQUAL(1, ringOne.getId());

		// Check the material, height and radii default values
		BOOST_REQUIRE(!ringOne.getMaterial());
		BOOST_REQUIRE_EQUAL(0.0, ringOne.getHeight());
		BOOST_REQUIRE_EQUAL(0.0, ringOne.getInnerRadius());
		BOOST_REQUIRE_EQUAL(1.0, ringOne.getOuterRadius());



		/* --- Test parameterized constructor with name -------------------- */

		// Create a Ring for testing with name specified
		Ring ringTwo("Babelfish");

		// Check the name, description and ID
		BOOST_REQUIRE_EQUAL("Babelfish", ringTwo.getName());
		BOOST_REQUIRE_EQUAL("Ring 1 Description", ringTwo.getDescription());
		BOOST_REQUIRE_EQUAL(1, ringTwo.getId());

		// Check the material, height and radii default values
		BOOST_REQUIRE(!ringTwo.getMaterial());
		BOOST_REQUIRE_EQUAL(0.0, ringTwo.getHeight());
		BOOST_REQUIRE_EQUAL(0.0, ringTwo.getInnerRadius());
		BOOST_REQUIRE_EQUAL(1.0, ringTwo.getOuterRadius());

		/* --- Test parameterized constructor with name, height, material,
		 * --- inner radius and outer radius ------------------------------- */
		Material blubber;
		std::shared_ptr<Material> blubberPtr = std::make_shared<Material>(blubber);

		// Create a Ring for testing with name, height, material, and radii
		Ring ringThree("Sperm Whale", blubberPtr, 1.0, 2.55, 4.039);

		// Check the name, description and ID
		BOOST_REQUIRE_EQUAL("Sperm Whale", ringThree.getName());
		BOOST_REQUIRE_EQUAL("Ring 1 Description", ringThree.getDescription());
		BOOST_REQUIRE_EQUAL(1, ringThree.getId());

		// Check the material, height and radii
		BOOST_REQUIRE(blubber == *ringThree.getMaterial());
		BOOST_REQUIRE_EQUAL(1.0, ringThree.getHeight());
		BOOST_REQUIRE_EQUAL(2.55, ringThree.getInnerRadius());
		BOOST_REQUIRE_EQUAL(4.039, ringThree.getOuterRadius());

		// end-user-code
}

BOOST_AUTO_TEST_CASE(checkAttributes) {
		// begin-user-code
		
		// Create a nullary Ring for testing
		Ring ring;

		// Create a material to use for testing
		Material material("Pan Galactic Gargle Blaster");
		std::shared_ptr<Material> materialPtr = std::make_shared<Material>(material);

		// Set the height, innerRadius, outerRadius and material
		ring.setHeight(53.2);
		ring.setInnerRadius(12.6);
		ring.setOuterRadius(13.9);
		ring.setMaterial(materialPtr);

		// Check the height, innerRadius, outerRadius and material
		BOOST_REQUIRE_EQUAL(53.2, ring.getHeight());
		BOOST_REQUIRE_EQUAL(12.6, ring.getInnerRadius());
		BOOST_REQUIRE_EQUAL(13.9, ring.getOuterRadius());
		BOOST_REQUIRE(material == *ring.getMaterial());

		// Check radii are unchanged
		BOOST_REQUIRE_EQUAL(12.6, ring.getInnerRadius());
		BOOST_REQUIRE_EQUAL(13.9, ring.getOuterRadius());

		// Try setting height, radii and material to illegal values
		std::shared_ptr<Material> nullMat;
		ring.setHeight(-1.0);
		ring.setInnerRadius(-8.7);
		ring.setOuterRadius(-20.4);
		ring.setMaterial(nullMat);

		// Check attributes are unchanged
		BOOST_REQUIRE_EQUAL(53.2, ring.getHeight());
		BOOST_REQUIRE_EQUAL(12.6, ring.getInnerRadius());
		BOOST_REQUIRE_EQUAL(13.9, ring.getOuterRadius());
		BOOST_REQUIRE(material == *ring.getMaterial());
		
		// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
		// begin-user-code
		
		/* --- Check equality between like rings --------------------------- */

		// Construct a ring (with a material) to test against
		Ring ring;
		Material material("Awfulness");
		std::shared_ptr<Material> materialPtr = std::make_shared<Material>(material);
		ring.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		ring.setDescription("Worst poet in the universe");
		ring.setHeight(23.4);
		ring.setInnerRadius(0.001);
		ring.setOuterRadius(0.01);
		ring.setMaterial(materialPtr);

		// Construct a ring (with material) equal to the first
		Ring equalRing;
		Material equalMaterial("Awfulness");
		std::shared_ptr<Material> equalMatPtr = std::make_shared<Material>(equalMaterial);
		equalRing.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		equalRing.setDescription("Worst poet in the universe");
		equalRing.setHeight(23.4);
		equalRing.setInnerRadius(0.001);
		equalRing.setOuterRadius(0.01);
		equalRing.setMaterial(equalMatPtr);

		// Check that equals() is reflexive and symmetric
		BOOST_REQUIRE(ring == ring);
		BOOST_REQUIRE((ring == equalRing) && (equalRing == ring));



		/* --- Check transitivity with similar rings ----------------------- */

		// Construct another ring equal to the first, for testing transitivity
		Ring transRing;
		Material transMaterial("Awfulness");
		std::shared_ptr<Material> transMatPtr = std::make_shared<Material>(transMaterial);
		transRing.setName("Paula Nancy Millstone Jennings of Greenbridge, Essex");
		transRing.setDescription("Worst poet in the universe");
		transRing.setHeight(23.4);
		transRing.setInnerRadius(0.001);
		transRing.setOuterRadius(0.01);
		transRing.setMaterial(transMatPtr);

		// Check for transitivity
		if ((ring == transRing) && (transRing == equalRing))
			BOOST_REQUIRE(ring == equalRing);
		else
			BOOST_FAIL("Ring transitivity test fail.");


		/* --- Check inequality between two dissimilar rings --------------- */

		// Construct a ring (with a material) unequal to the first
		Ring unequalRing;
		Material unequalMaterial("Ickiness");
		std::shared_ptr<Material> unequalMatPtr = std::make_shared<Material>(unequalMaterial);
		unequalRing.setName("Grunthos the Flatulent");
		unequalRing.setDescription("Poetmaster of the Azgoths of Kria");
		unequalRing.setHeight(64.77);
		unequalRing.setInnerRadius(10.222);
		unequalRing.setOuterRadius(100.2);
		unequalRing.setMaterial(unequalMatPtr);

		// Check that ring and unequalRing are not the same
		BOOST_REQUIRE(!(ring == unequalRing));
		
		return;
		// end-user-code		
}

BOOST_AUTO_TEST_CASE(checkCopying) {
		// begin-user-code
		
		/* --- Testing the copy operation ---------------------------------- */

		// Create a ring (with material) to copy from
		Ring ring;
		Material material("Fleshy bits");
		std::shared_ptr<Material> materialPtr = std::make_shared<Material>(material);
		ring.setName("Phouchg");
		ring.setDescription("Chosen to recieve DeepThought's answer");
		ring.setId(7001);
		ring.setHeight(54.6);
		ring.setInnerRadius(1.78);
		ring.setOuterRadius(6.77);
		ring.setMaterial(materialPtr);

		// Copy the contents of ring to ringCopy
		Ring ringCopy(ring);

		// Check ring and ringCopy are identical
		BOOST_REQUIRE(ring == ringCopy);


		/* --- Testing the cloning operation ------------------------------- */

		// Clone the first ring
		std::shared_ptr<Identifiable> ringClone;
		ringClone = ring.clone();

		// Cast ringClone to a Ring pointer
		std::shared_ptr<Ring> cloneCast = std::dynamic_pointer_cast<Ring>(ringClone);

		// Check that ringClone isn't null
		BOOST_REQUIRE(cloneCast);

		// Check that ring and ringClone are identical
		BOOST_REQUIRE(ring == *cloneCast);
				
		return;
		// end-user-code	
}
BOOST_AUTO_TEST_SUITE_END()
