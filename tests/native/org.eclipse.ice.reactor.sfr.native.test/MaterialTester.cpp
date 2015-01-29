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
#include <Material.h>
#include <string>
#include <memory>

using namespace ICE_SFReactor;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(MaterialTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code

	// Initialize a Material for testing with nullary constructor
	Material material;

	// Check the default name, description and ID
	BOOST_REQUIRE_EQUAL("Material 1", material.getName());
	BOOST_REQUIRE_EQUAL("Material 1 Description", material.getDescription());
	BOOST_REQUIRE_EQUAL(1, material.getId());

	// Initialize a Material for testing with parameterized constructor
	material = Material("Small Lump of Green Putty");

	// Check the name, description and ID
	BOOST_REQUIRE_EQUAL("Small Lump of Green Putty", material.getName());
	BOOST_REQUIRE_EQUAL("Material 1 Description", material.getDescription());
	BOOST_REQUIRE_EQUAL(1, material.getId());

	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	/* --- Check equality between like materials ----------------------- */

	// Construct a material to test against
	Material material("Towel");
	material.setDescription("The most massively useful thing any interstellar Hitchhiker can carry");
	material.setId(42);

	// Construct a material equal to the first
	Material equalMaterial("Towel");
	equalMaterial.setDescription("The most massively useful thing any interstellar Hitchhiker can carry");
	equalMaterial.setId(42);

	// Check that equals() is reflexive and symmetric
	BOOST_REQUIRE(material == material);
	BOOST_REQUIRE((material == equalMaterial) && (equalMaterial == material));

	/* --- Check transitivity with similar materials ------------------- */

	// Construct another material equal to the first, for testing transitivity
	Material transMaterial("Towel");
	transMaterial.setDescription("The most massively useful thing any interstellar Hitchhiker can carry");
	transMaterial.setId(42);

	// Check for transitivity
	if ((material == transMaterial) && (transMaterial == equalMaterial))
		BOOST_REQUIRE(material == equalMaterial);
	else
		BOOST_FAIL("MaterialTester transitivity test fail");


	/* --- Check inequality between two dissimilar materials ----------- */

	// Construct a material unequal to the first
	Material unequalMaterial("Dish rag");
	unequalMaterial.setDescription("Not terribly useful for interstellar travel");
	unequalMaterial.setId(24);

	// Check that material and unequalMaterial are not the same
	BOOST_REQUIRE(!(material == unequalMaterial));

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkCopying) {
	// begin-user-code

	/* --- Testing the copy operation ---------------------------------- */

	// Create a material to copy from
	Material material("Small Lump of Green Putty");
	material.setDescription("Found in my armpit one midsummer morning");
	material.setId(5496);

	// Create an empty material to copy to
	Material materialCopy;

	// Check that material and materialCopy aren't identical first
	BOOST_REQUIRE(!(material == materialCopy));

	// Copy the contents of material to materialCopy
	materialCopy = Material(material);

	// Check material and materialCopy are identical
	BOOST_REQUIRE(material == materialCopy);

	/* --- Testing the cloning operation ------------------------------- */

	// Create an empty material for cloning
	std::shared_ptr<Identifiable> materialClone;

	// Clone the first material
	materialClone = material.clone();

	// Check that the clone isn't null
	BOOST_REQUIRE(materialClone);

	// Cast it to a Material pointer for comparison
	std::shared_ptr<Material> cloneCast = std::dynamic_pointer_cast<Material>(materialClone);

	// Check that material and cloneCast are identical
	BOOST_REQUIRE(material == *cloneCast);

	return;
	// end-user-code
}
BOOST_AUTO_TEST_SUITE_END()
