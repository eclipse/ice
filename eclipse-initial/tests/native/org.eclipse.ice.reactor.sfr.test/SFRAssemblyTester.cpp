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
#include <assembly/SFRAssembly.h>

#include <string>

#include <AssemblyType.h>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(SFRAssemblyTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code

	// Invalid, default, and valid values to use in the constructor.
	int invalidSize = 0;;
	int defaultSize = 1;
	int size = 10;

	std::string nullName;
	std::string emptyName = "   ";
	std::string defaultName = "SFR Assembly 1";
	std::string name = "Arthur";

	AssemblyType defaultType = AssemblyType::Fuel;
	AssemblyType type = AssemblyType::Control;

	// Other defaults.
	std::string defaultDescription = "SFR Assembly 1's Description";
	int defaultId = 1;
	double defaultDuctThickness = 0.0;

	/* ---- Test the basic constructor. ---- */
	// Invalid parameters.
	SFRAssembly assembly(invalidSize);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
	BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());

	// Valid parameters.
	assembly = SFRAssembly(size);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	/* ------------------------------------- */

	/* ---- Test the second constructor. ---- */
	// Invalid parameters.

	// All invalid.
	assembly = SFRAssembly(nullName, defaultType, invalidSize);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
	BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());

	// Name invalid.
	assembly = SFRAssembly(emptyName, type, size);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(type, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());

	// Size invalid.
	assembly = SFRAssembly(name, type, invalidSize);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
	BOOST_REQUIRE_EQUAL(type, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());

	// Valid parameters.
	assembly = SFRAssembly(name, type, size);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(type, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	/* -------------------------------------- */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkDuctThickness) {
	// begin-user-code

	SFRAssembly assembly(1);
	double defaultDuctThickness = 0.0;

	// Check the default.
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());

	// Set it to 1 and check it.
	assembly.setDuctThickness(1.0);
	BOOST_REQUIRE_EQUAL(1.0, assembly.getDuctThickness());

	// Set it to an invalid number.
	assembly.setDuctThickness(-1.0);
	BOOST_REQUIRE_EQUAL(1.0, assembly.getDuctThickness());

	// Set it to a valid number.
	assembly.setDuctThickness(500);
	BOOST_REQUIRE_EQUAL(500, assembly.getDuctThickness());

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	int size = 18;
	std::string name = "So long, and thanks for all the fish!";

	// Initialize objects for testing.
	SFRAssembly object(name, AssemblyType::Fuel, size);
	SFRAssembly equalObject(name, AssemblyType::Fuel, size);
	SFRAssembly unequalObject(name, AssemblyType::Control, size);

	// Set up the object and equalObject.
	object.setDuctThickness(17);
	equalObject.setDuctThickness(17);

	// Set up the unequalObject.
	// (AssemblyType is different for unequalObject.)
	unequalObject.setDuctThickness(17);

	// Check that equality is reflexive and symmetric.
	BOOST_REQUIRE_EQUAL(true, object == object);
	BOOST_REQUIRE_EQUAL(true, object == equalObject);
	BOOST_REQUIRE_EQUAL(true, equalObject == object);

	// Check that equals will fail when it should.
	BOOST_REQUIRE_EQUAL(false, object == unequalObject);
	BOOST_REQUIRE_EQUAL(false, unequalObject == object);

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkCopying) {
	// begin-user-code

	int size = 18;
	std::string name = "So long, and thanks for all the fish!";

	// Initialize objects for testing.
	SFRAssembly object(name, AssemblyType::Fuel, size);
	SFRAssembly objectCopy(size);
	std::shared_ptr<SFRAssembly> objectClone;

	// Set up the object.
	object.setDuctThickness(17);

	/* ---- Check copying. ---- */
	// Make sure the object is not the same as its copy.
	BOOST_REQUIRE_EQUAL(false, object == objectCopy);

	// Copy the object.
	objectCopy = SFRAssembly(object);

	// Make sure the object is now the same as its copy.
	BOOST_REQUIRE_EQUAL(true, object == objectCopy);
	/* ------------------------ */

	/* ---- Check cloning. ---- */
	// Clone the object.
	objectClone = std::dynamic_pointer_cast<SFRAssembly>(object.clone());

	// Make sure the object is now the same as its clone.
	BOOST_REQUIRE_EQUAL(true, object == *objectClone);
	/* ------------------------ */

	return;
	// end-user-code
}
BOOST_AUTO_TEST_SUITE_END()
