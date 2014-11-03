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
#include <assembly/PinAssembly.h>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(PinAssemblyTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code

	// Invalid, default, and valid values to use in the constructor.
	int invalidSize = 0;
	int defaultSize = 1;
	int size = 31;

	std::string nullName;
	std::string emptyName = "            ";
	std::string defaultName = "SFR Pin Assembly 1";
	std::string name = "Tricia";

	PinType defaultPinType = PinType::InnerFuel;
	PinType pinType = PinType::BlanketFuel;

	// Other defaults.
	AssemblyType defaultType = AssemblyType::Fuel;
	std::string defaultDescription = "SFR Pin Assembly 1's Description";
	int defaultId = 1;
	double defaultDuctThickness = 0.0;
	double defaultPinPitch = 1.0;
	double defaultInnerDuctFlatToFlat = 0.0;
	double defaultInnerDuctThickness = 0.0;

	/* ---- Test the basic constructor. ---- */
	// Invalid parameters.
	PinAssembly assembly(invalidSize);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
	BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(defaultPinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());

	// Valid parameters.
	assembly = PinAssembly(size);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(defaultPinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());
	/* ------------------------------------- */

	/* ---- Test the second constructor. ---- */
	// Invalid parameters.

	// All invalid.
	assembly = PinAssembly(nullName, invalidSize, defaultPinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
	BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(defaultPinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());

	// Name invalid.
	assembly = PinAssembly(emptyName, size, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());

	// Size invalid.
	assembly = PinAssembly(name, invalidSize, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
	BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());

	// Valid parameters.
	assembly = PinAssembly(name, size, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());
	/* -------------------------------------- */

	/* ---- Test all PinTypes. ---- */
	// InnerFuel
	AssemblyType assemblyType = AssemblyType::Fuel;
	pinType = PinType::InnerFuel;

	assembly = PinAssembly(name, size, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(assemblyType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());

	// OuterFuel
	assemblyType = AssemblyType::Fuel;
	pinType = PinType::OuterFuel;

	assembly = PinAssembly(name, size, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(assemblyType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());

	// BlanketFuel
	assemblyType = AssemblyType::Fuel;
	pinType = PinType::BlanketFuel;

	assembly = PinAssembly(name, size, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(assemblyType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());

	// PrimaryControl
	assemblyType = AssemblyType::Control;
	pinType = PinType::PrimaryControl;

	assembly = PinAssembly(name, size, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(assemblyType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(0.75, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(0.05, assembly.getInnerDuctThickness());

	// SecondaryControl
	assemblyType = AssemblyType::Control;
	pinType = PinType::SecondaryControl;

	assembly = PinAssembly(name, size, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(assemblyType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(0.75, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(0.05, assembly.getInnerDuctThickness());

	// Shielding
	assemblyType = AssemblyType::Shield;
	pinType = PinType::Shielding;

	assembly = PinAssembly(name, size, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(assemblyType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());

	// MaterialTest
	assemblyType = AssemblyType::Test;
	pinType = PinType::MaterialTest;

	assembly = PinAssembly(name, size, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(assemblyType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());

	// FuelTest
	assemblyType = AssemblyType::Test;
	pinType = PinType::FuelTest;

	assembly = PinAssembly(name, size, pinType);

	BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
	BOOST_REQUIRE_EQUAL(name, assembly.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
	BOOST_REQUIRE_EQUAL(size, assembly.getSize());
	BOOST_REQUIRE_EQUAL(assemblyType, assembly.getAssemblyType());
	BOOST_REQUIRE_EQUAL(pinType, assembly.getPinType());
	BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());
	BOOST_REQUIRE_EQUAL(defaultInnerDuctThickness, assembly.getInnerDuctThickness());

	/* ------------------------------ */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkPinPitch) {
	// begin-user-code

	PinAssembly assembly(1);
	double defaultPinPitch = 1.0;

	// Check the default.
	BOOST_REQUIRE_EQUAL(defaultPinPitch, assembly.getPinPitch());

	// Set it to 1 and check it.
	assembly.setPinPitch(1.0);
	BOOST_REQUIRE_EQUAL(1.0, assembly.getPinPitch());

	// Set it to an invalid number.
	assembly.setPinPitch(-1.0);
	BOOST_REQUIRE_EQUAL(1.0, assembly.getPinPitch());

	// Set it to a valid number.
	assembly.setPinPitch(500);
	BOOST_REQUIRE_EQUAL(500.0, assembly.getPinPitch());

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkInnerDuct) {
	// begin-user-code

	PinAssembly assembly(1);
	double defaultInnerDuctFlatToFlat = 0.0;
	double defaultInnerDuctThickness = 0.0;

	// Check the default.
	BOOST_REQUIRE_EQUAL(defaultInnerDuctFlatToFlat, assembly.getInnerDuctFlatToFlat());

	// Set inner duct flat-to-flat to 1 and check it.
	assembly.setInnerDuctFlatToFlat(1.0);
	BOOST_REQUIRE_EQUAL(1.0, assembly.getInnerDuctFlatToFlat());

	// Set it to an invalid number.
	assembly.setInnerDuctFlatToFlat(-1.0);
	BOOST_REQUIRE_EQUAL(1.0, assembly.getInnerDuctFlatToFlat());

	// Set it to a valid number.
	assembly.setInnerDuctFlatToFlat(500);
	BOOST_REQUIRE_EQUAL(500.0, assembly.getInnerDuctFlatToFlat());

	// Set inner duct thickness to 1 and check it.
	assembly.setInnerDuctThickness(1.0);
	BOOST_REQUIRE_EQUAL(1.0, assembly.getInnerDuctThickness());

	// Set it to an invalid number.
	assembly.setInnerDuctThickness(-1.0);
	BOOST_REQUIRE_EQUAL(1.0, assembly.getInnerDuctThickness());

	// Set it to a valid number.
	assembly.setInnerDuctThickness(500);
	BOOST_REQUIRE_EQUAL(500.0, assembly.getInnerDuctThickness());

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkPinAddRem) {
	// begin-user-code

	// Note: I only check assembly.getPinLocations() in a few spots
	// below since it's a late addition. Further testing may be required.

	int size = 10;

	// An assembly to test.
	PinAssembly assembly(size);

	// Initialize some pins to add/remove from the assembly.
	std::shared_ptr<SFRPin> pin1 = std::make_shared<SFRPin>();
	pin1->setName("Anjie");
	std::shared_ptr<SFRPin> pin2 = std::make_shared<SFRPin>();
	pin2->setName("Colin");
	std::shared_ptr<SFRPin> pin3 = std::make_shared<SFRPin>();
	pin3->setName("Dish of the Day");

	// A List of boundary indexes to test.
	std::vector<int> boundaryIndexes;
	boundaryIndexes.push_back(-1);
	boundaryIndexes.push_back(0);
	boundaryIndexes.push_back(size - 1);
	boundaryIndexes.push_back(size);

	// A set of indexes that will be empty. This makes it easier to check
	// pin locations.
	std::set<int> emptyIndexes;
	for (int i = 0; i < size * size; i++)
		emptyIndexes.insert(i);

	std::string name;

	std::shared_ptr<SFRPin> nullPin;
	std::string nullString;

	std::vector<std::string> names;
	std::vector<int> locations;

	// Methods to test:
	// public bool addPin(std::shared_ptr<SFRPin> pin)
	// public bool removePin(std::string name)
	// public bool removePinFromLocation(int row, int column)
	// public std::vector<std::string> getPinNames()
	// public std::shared_ptr<SFRPin> getPinByName(std::string name)
	// public std::shared_ptr<SFRPin> getPinByLocation(int row, int column)
	// public bool setPinLocation(std::string name, int row, int column)
	// public std::vector<int> getPinLocations(std::string name)

	/* ---- Make sure the assembly is empty. ---- */
	// Check invalid locations.
	for (int r = 0; r < boundaryIndexes.size(); r++) {
		int row = boundaryIndexes[r];
		for (int c = 0; c < boundaryIndexes.size(); c++) {
			int column = boundaryIndexes[c];
			if (row < 0 || row == size || column < 0 || column == size) {
				BOOST_REQUIRE(nullPin == assembly.getPinByLocation(row, column));
			}
		}
	}

	// Check all valid locations.
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));
	/* ------------------------------------------ */

	/* ---- Try various argument combinations for each method. ---- */
	name = pin1->getName();

	// Verify that there is no pin set.
	BOOST_REQUIRE(nullPin == assembly.getPinByName(name));
	BOOST_REQUIRE_EQUAL(0, assembly.getNumberOfPins());
	BOOST_REQUIRE_EQUAL(0, assembly.getPinNames().size());
	names = assembly.getPinNames();
	BOOST_REQUIRE_EQUAL(false, std::find(names.begin(), names.end(), name) != names.end());

	// Check all the locations.
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));

	// addPin
	BOOST_REQUIRE_EQUAL(false, assembly.addPin(nullPin));

	// addPin (successful)
	BOOST_REQUIRE(assembly.addPin(pin1));

	// setPinLocation
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(nullString, -1, -1));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(nullString, 0, 0));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation("Vogons", 0, 0));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(name, -1, 0));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(name, 0, size));

	// Check setting invalid locations.
	for (int r = 0; r < boundaryIndexes.size(); r++) {
		int row = boundaryIndexes[r];
		for (int c = 0; c < boundaryIndexes.size(); c++) {
			int column = boundaryIndexes[c];
			if (row < 0 || row >= size || column < 0 || column >= size) {
				BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(name, row, column));
				BOOST_REQUIRE(nullPin == assembly.getPinByLocation(row, column));
			}
		}
	}

	// setPinLocation (successful)
	BOOST_REQUIRE(assembly.setPinLocation(name, 0, 0));

	// getNumberOfPins (successful)
	BOOST_REQUIRE_EQUAL(1, assembly.getNumberOfPins());

	// getPinNames (successful)
	BOOST_REQUIRE_EQUAL(1, assembly.getPinNames().size());
	names = assembly.getPinNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());

	// getPinByName
	BOOST_REQUIRE(nullPin == assembly.getPinByName(nullString));
	BOOST_REQUIRE(nullPin == assembly.getPinByName("Vogons"));

	// getPinByName (successful)
	BOOST_REQUIRE(*pin1 == *assembly.getPinByName(name));

	// getPinByLocation
	BOOST_REQUIRE(nullPin == assembly.getPinByLocation(-1, -1));
	BOOST_REQUIRE(nullPin == assembly.getPinByLocation(-1, 0));
	BOOST_REQUIRE(nullPin == assembly.getPinByLocation(0, size));

	// getPinByLocation (successful)
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 0));

	// getPinLocations
	BOOST_REQUIRE(assembly.getPinLocations(nullString).empty());
	BOOST_REQUIRE(assembly.getPinLocations("Vogons").empty());

	// getPinLocations (successful)
	BOOST_REQUIRE_EQUAL(1, assembly.getPinLocations(name).size());
	locations = assembly.getPinLocations(name);
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 0) != locations.end());

	// removePinFromLocation
	BOOST_REQUIRE_EQUAL(false, assembly.removePinFromLocation(-1, -1));
	BOOST_REQUIRE_EQUAL(false, assembly.removePinFromLocation(-1, 0));
	BOOST_REQUIRE_EQUAL(false, assembly.removePinFromLocation(0, size));

	// removePinFromLocation (successful)
	BOOST_REQUIRE(assembly.removePinFromLocation(0, 0));

	// removePin
	BOOST_REQUIRE_EQUAL(false, assembly.removePin(nullString));
	BOOST_REQUIRE_EQUAL(false, assembly.removePin("Vogons"));

	// removePin (successful)
	BOOST_REQUIRE(assembly.removePin(name));

	// Verify that the pin has been completely removed.
	BOOST_REQUIRE(nullPin == assembly.getPinByName(name));
	BOOST_REQUIRE_EQUAL(0, assembly.getNumberOfPins());
	BOOST_REQUIRE_EQUAL(0, assembly.getPinNames().size());
	names = assembly.getPinNames();
	BOOST_REQUIRE_EQUAL(false, std::find(names.begin(), names.end(), name) != names.end());

	// Check all the locations.
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));
	/* ------------------------------------------------------------ */

	/* ---- Add an pin and set some locations. ---- */
	name = pin1->getName();
	BOOST_REQUIRE(assembly.addPin(pin1));
	BOOST_REQUIRE_EQUAL(false, assembly.addPin(pin1));

	// Verify that the pin was added.
	BOOST_REQUIRE(*pin1 == *assembly.getPinByName(name));
	BOOST_REQUIRE_EQUAL(1, assembly.getNumberOfPins());
	BOOST_REQUIRE_EQUAL(1, assembly.getPinNames().size());
	names = assembly.getPinNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());

	// All locations should be empty.
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));
	BOOST_REQUIRE(assembly.getPinLocations(name).empty());

	// The first attempt to set a location should succeed. Subsequent
	// attempts to set the same pin in the same location should fail.

	// Put it in the first spot.
	BOOST_REQUIRE(assembly.setPinLocation(name, 0, 0));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(name, 0, 0));
	emptyIndexes.erase(0);

	// Put it in a middle spot.
	BOOST_REQUIRE(assembly.setPinLocation(name, 0, 6));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(name, 0, 6));
	emptyIndexes.erase(6);

	// Put it in a middle spot.
	BOOST_REQUIRE(assembly.setPinLocation(name, 3, 7));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(name, 3, 7));
	emptyIndexes.erase(3 * size + 7);

	// Put it in the last spot.
	BOOST_REQUIRE(assembly.setPinLocation(name, size - 1, size - 1));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(name, size - 1, size - 1));
	emptyIndexes.erase(size * size - 1);

	// Verify the pin locations.
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 0));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 6));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(3, 7));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(size - 1, size - 1));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));

	// We should still be able to get the pin by location.
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 0));

	// Verify the pin locations (getPinLocations()).
	BOOST_REQUIRE_EQUAL(4, assembly.getPinLocations(name).size());
	locations = assembly.getPinLocations(name);
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 0) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 0 * size + 6) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 3 * size + 7) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), size * size - 1) != locations.end());
	/* -------------------------------------------- */

	/* ---- Test overriding a pin location. ---- */
	name = pin2->getName();
	BOOST_REQUIRE(assembly.addPin(pin2));
	BOOST_REQUIRE_EQUAL(false, assembly.addPin(pin2));

	// Verify that the pin was added.
	BOOST_REQUIRE(*pin2 == *assembly.getPinByName(name));
	BOOST_REQUIRE_EQUAL(2, assembly.getNumberOfPins());
	BOOST_REQUIRE_EQUAL(2, assembly.getPinNames().size());
	names = assembly.getPinNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), pin1->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());

	// Verify the pin locations.
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 0));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 6));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(3, 7));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(size - 1, size - 1));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));

	// Put it in a new spot.
	BOOST_REQUIRE(assembly.setPinLocation(name, 1, 1));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(name, 1, 1));
	emptyIndexes.erase(size + 1);

	// Put it in a spot occupied by pin1->
	BOOST_REQUIRE(assembly.setPinLocation(name, 3, 7));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(name, 3, 7));

	// Verify the pin locations.
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 0));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 6));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(size - 1, size - 1));
	BOOST_REQUIRE(*pin2 == *assembly.getPinByLocation(1, 1));
	BOOST_REQUIRE(*pin2 == *assembly.getPinByLocation(3, 7));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));
	/* ----------------------------------------- */

	/* ---- Test adding yet another pin. ---- */
	name = pin3->getName();
	BOOST_REQUIRE(assembly.addPin(pin3));
	BOOST_REQUIRE_EQUAL(false, assembly.addPin(pin3));

	// Verify that the pin was added.
	BOOST_REQUIRE(*pin3 == *assembly.getPinByName(name));
	BOOST_REQUIRE_EQUAL(3, assembly.getNumberOfPins());
	BOOST_REQUIRE_EQUAL(3, assembly.getPinNames().size());
	names = assembly.getPinNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), pin1->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), pin2->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());

	// Verify the pin locations.
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 0));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 6));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(size - 1, size - 1));
	BOOST_REQUIRE(*pin2 == *assembly.getPinByLocation(1, 1));
	BOOST_REQUIRE(*pin2 == *assembly.getPinByLocation(3, 7));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));

	// Put it in a new spot.
	BOOST_REQUIRE(assembly.setPinLocation(name, 3, 3));
	BOOST_REQUIRE_EQUAL(false, assembly.setPinLocation(name, 3, 3));
	emptyIndexes.erase(3 * size + 3);

	// Verify the pin locations.
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 0));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 6));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(size - 1, size - 1));
	BOOST_REQUIRE(*pin2 == *assembly.getPinByLocation(1, 1));
	BOOST_REQUIRE(*pin2 == *assembly.getPinByLocation(3, 7));
	BOOST_REQUIRE(*pin3 == *assembly.getPinByLocation(3, 3));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));
	/* -------------------------------------- */

	/* ---- Test removing a pin from a location. ---- */
	BOOST_REQUIRE(assembly.removePinFromLocation(0, 6));
	BOOST_REQUIRE_EQUAL(false, assembly.removePinFromLocation(0, 6));
	emptyIndexes.insert(6);

	// Verify the pin locations.
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 0));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(size - 1, size - 1));
	BOOST_REQUIRE(*pin2 == *assembly.getPinByLocation(1, 1));
	BOOST_REQUIRE(*pin2 == *assembly.getPinByLocation(3, 7));
	BOOST_REQUIRE(*pin3 == *assembly.getPinByLocation(3, 3));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));

	// Remove pin2 entirely manually.
	BOOST_REQUIRE(assembly.removePinFromLocation(1, 1));
	BOOST_REQUIRE_EQUAL(false, assembly.removePinFromLocation(1, 1));
	emptyIndexes.insert(size + 1);

	BOOST_REQUIRE(assembly.removePinFromLocation(3, 7));
	BOOST_REQUIRE_EQUAL(false, assembly.removePinFromLocation(3, 7));
	emptyIndexes.insert(3 * size + 7);

	// Verify the pin locations.
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(0, 0));
	BOOST_REQUIRE(*pin1 == *assembly.getPinByLocation(size - 1, size - 1));
	BOOST_REQUIRE(*pin3 == *assembly.getPinByLocation(3, 3));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));

	// Verify the pin locations (getPinLocations()).
	BOOST_REQUIRE_EQUAL(2, assembly.getPinLocations(pin1->getName()).size());
	locations = assembly.getPinLocations(pin1->getName());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 0) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), size * size - 1) != locations.end());

	// pin2 should still be in the assembly, though.

	// Verify the pins stored in the assembly.
	BOOST_REQUIRE(*pin1 == *assembly.getPinByName(pin1->getName()));
	BOOST_REQUIRE(*pin2 == *assembly.getPinByName(pin2->getName()));
	BOOST_REQUIRE(*pin3 == *assembly.getPinByName(pin3->getName()));
	BOOST_REQUIRE_EQUAL(3, assembly.getNumberOfPins());
	BOOST_REQUIRE_EQUAL(3, assembly.getPinNames().size());
	names = assembly.getPinNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), pin1->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), pin2->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), pin3->getName()) != names.end());
	/* ---------------------------------------------- */

	/* ---- Test removing a pin completely. ---- */
	BOOST_REQUIRE(assembly.removePin(pin1->getName()));
	BOOST_REQUIRE_EQUAL(false, assembly.removePin(pin1->getName()));

	emptyIndexes.insert(0);
	emptyIndexes.insert(size * size - 1);

	// Verify the pin locations.
	BOOST_REQUIRE(*pin3 == *assembly.getPinByLocation(3, 3));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));

	// pin1 should not have any locations. In fact, since the pin is not in
	// the assembly, this returns an empty List.
	BOOST_REQUIRE(assembly.getPinLocations(pin1->getName()).empty());

	// Verify the pins stored in the assembly.
	BOOST_REQUIRE(nullPin == assembly.getPinByName(pin1->getName()));
	BOOST_REQUIRE(*pin2 == *assembly.getPinByName(pin2->getName()));
	BOOST_REQUIRE(*pin3 == *assembly.getPinByName(pin3->getName()));
	BOOST_REQUIRE_EQUAL(2, assembly.getNumberOfPins());
	BOOST_REQUIRE_EQUAL(2, assembly.getPinNames().size());
	names = assembly.getPinNames();
	BOOST_REQUIRE_EQUAL(false, std::find(names.begin(), names.end(), pin1->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), pin2->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), pin3->getName()) != names.end());
	/* ----------------------------------------- */

	/* ---- Remove everything and verify. ---- */
	BOOST_REQUIRE_EQUAL(false, assembly.removePin(pin1->getName()));
	BOOST_REQUIRE(assembly.removePin(pin2->getName()));
	BOOST_REQUIRE_EQUAL(false, assembly.removePin(pin2->getName()));
	BOOST_REQUIRE(assembly.removePin(pin3->getName()));
	BOOST_REQUIRE_EQUAL(false, assembly.removePin(pin3->getName()));

	// Add the last pin location back to the empty index set.
	emptyIndexes.insert(size * 3 + 3);

	// Verify the pin locations.
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullPin == assembly.getPinByLocation(*i / size, *i % size));

	// Verify the pins stored in the assembly.
	BOOST_REQUIRE(nullPin == assembly.getPinByName(pin1->getName()));
	BOOST_REQUIRE(nullPin == assembly.getPinByName(pin2->getName()));
	BOOST_REQUIRE(nullPin == assembly.getPinByName(pin3->getName()));
	BOOST_REQUIRE_EQUAL(0, assembly.getNumberOfPins());
	BOOST_REQUIRE_EQUAL(0, assembly.getPinNames().size());
	/* --------------------------------------- */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkCompositeImplementation) {
	// begin-user-code

	// Tests the following methods:

	// public Component getComponent(int childId)
	// public SFRComponent getComponent(String name)
	// public std::vector<std::string> getComponentNames()
	// public int getNumberOfComponents()
	// public std::vector< std::shared_ptr<Component> > getComponents()

	// public void addComponent(Component child)
	// public void removeComponent(int childId)
	// public void removeComponent(std::string name)

	// Initialize an assembly to test.
	PinAssembly assembly(15);

	std::shared_ptr<SFRComposite> composite;
	std::shared_ptr<SFRComponent> component;
	std::string name = "Ford";
	int id = 1;
	int numberOfComponents = 0;

	std::string nullString;
	std::shared_ptr<Component> nullComponent;

	std::vector<std::shared_ptr<Component> > components;
	std::vector<std::string> names;

	/* ---- Check the initial state of the Composite. ---- */
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getNumberOfComponents());
	BOOST_REQUIRE(assembly.getComponents().empty());
	BOOST_REQUIRE(assembly.getComponentNames().empty());
	/* --------------------------------------------------- */

	/* ---- Make sure we cannot add components directly. ---- */
	component = std::make_shared<SFRComponent>();
	assembly.addComponent(component);

	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getNumberOfComponents());
	BOOST_REQUIRE(assembly.getComponents().empty());
	BOOST_REQUIRE(assembly.getComponentNames().empty());
	BOOST_REQUIRE(nullComponent == assembly.getComponent(component->getName()));

	// Try the same with a Composite.
	composite = std::make_shared<SFRComposite>();
	assembly.addComponent(composite);

	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getNumberOfComponents());
	BOOST_REQUIRE(assembly.getComponents().empty());
	BOOST_REQUIRE(assembly.getComponentNames().empty());
	BOOST_REQUIRE(nullComponent == assembly.getComponent(composite->getName()));
	/* ------------------------------------------------------ */

	/* ---- Add a component the proper way. ---- */
	// Create a valid component.
	component = std::make_shared<SFRPin>();
	component->setName(name);

	// Add it.
	BOOST_REQUIRE_EQUAL(true, assembly.addPin(std::dynamic_pointer_cast<SFRPin>(component)));

	numberOfComponents = 1;

	// Verify that it's there.
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getNumberOfComponents());
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getComponents().size());
	components = assembly.getComponents();
	BOOST_REQUIRE_EQUAL(true, std::find(components.begin(), components.end(), component) != components.end());
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getComponentNames().size());
	names = assembly.getComponentNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());
	BOOST_REQUIRE(*component == *(std::dynamic_pointer_cast<SFRPin>(assembly.getComponent(name))));
	/* ----------------------------------------- */

	/* ---- Make sure we cannot remove components directly. ---- */
	id = component->getId();

	// Try to remove it via ID.
	assembly.removeComponent(id);

	// Verify that the component is still there.
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getNumberOfComponents());
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getComponents().size());
	components = assembly.getComponents();
	BOOST_REQUIRE_EQUAL(true, std::find(components.begin(), components.end(), component) != components.end());
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getComponentNames().size());
	names = assembly.getComponentNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());
	BOOST_REQUIRE(*component == *(std::dynamic_pointer_cast<SFRPin>(assembly.getComponent(name))));

	// Try to remove it via name.
	assembly.removeComponent(name);

	// Verify that the component is still there.
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getNumberOfComponents());
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getComponents().size());
	components = assembly.getComponents();
	BOOST_REQUIRE_EQUAL(true, std::find(components.begin(), components.end(), component) != components.end());
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getComponentNames().size());
	names = assembly.getComponentNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());
	BOOST_REQUIRE(*component == *(std::dynamic_pointer_cast<SFRPin>(assembly.getComponent(name))));

	// Try removing an invalid component.
	assembly.removeComponent(nullString);

	// Verify that the component is still there.
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getNumberOfComponents());
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getComponents().size());
	components = assembly.getComponents();
	BOOST_REQUIRE_EQUAL(true, std::find(components.begin(), components.end(), component) != components.end());
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getComponentNames().size());
	names = assembly.getComponentNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());
	BOOST_REQUIRE(*component == *(std::dynamic_pointer_cast<SFRPin>(assembly.getComponent(name))));
	/* --------------------------------------------------------- */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	int size = 78;

	// Initialize objects for testing.
	PinAssembly object(size);
	PinAssembly equalObject(size);
	PinAssembly unequalObject(size);

	// Set up the object and equalObject.
	std::shared_ptr<SFRPin> component = std::make_shared<SFRPin>();
	component->setName("Marvin");
	std::shared_ptr<SFRData> subComponent = std::make_shared<SFRData>("Depressed");
	subComponent->setValue(10);
	component->addData(subComponent, 0);

	object.addPin(std::make_shared<SFRPin>("Ford"));
	object.addPin(std::make_shared<SFRPin>("Zaphod"));
	object.addPin(component);

	object.setPinLocation("Ford", 0, 0);
	object.setPinLocation("Ford", 3, 4);
	object.setPinLocation("Zaphod", 18, 35);
	object.setPinLocation("Zaphod", 15, 36);
	object.setPinLocation("Zaphod", 76, 77);
	object.setPinLocation("Marvin", 13, 13);
	object.setPinLocation("Marvin", 14, 14);
	object.setPinLocation("Marvin", 15, 15);

	component = std::make_shared<SFRPin>();
	component->setName("Marvin");
	subComponent = std::make_shared<SFRData>("Depressed");
	subComponent->setValue(10);
	component->addData(subComponent, 0);

	equalObject.addPin(std::make_shared<SFRPin>("Ford"));
	equalObject.addPin(std::make_shared<SFRPin>("Zaphod"));
	equalObject.addPin(component);

	equalObject.setPinLocation("Ford", 0, 0);
	equalObject.setPinLocation("Ford", 3, 4);
	equalObject.setPinLocation("Zaphod", 18, 35);
	equalObject.setPinLocation("Zaphod", 15, 36);
	equalObject.setPinLocation("Zaphod", 76, 77);
	equalObject.setPinLocation("Marvin", 13, 13);
	equalObject.setPinLocation("Marvin", 14, 14);
	equalObject.setPinLocation("Marvin", 15, 15);

	// Set up the unequalObject.
	component = std::make_shared<SFRPin>();
	component->setName("Marvin");
	subComponent = std::make_shared<SFRData>("Depressed");
	subComponent->setValue(9);		// Different!
	component->addData(subComponent, 0);

	unequalObject.addPin(std::make_shared<SFRPin>("Ford"));
	unequalObject.addPin(std::make_shared<SFRPin>("Zaphod"));
	unequalObject.addPin(component);

	unequalObject.setPinLocation("Ford", 0, 0);
	unequalObject.setPinLocation("Ford", 3, 4);
	unequalObject.setPinLocation("Zaphod", 18, 35);
	unequalObject.setPinLocation("Zaphod", 15, 36);
	unequalObject.setPinLocation("Zaphod", 76, 77);
	unequalObject.setPinLocation("Marvin", 13, 13);
	unequalObject.setPinLocation("Marvin", 14, 14);
	unequalObject.setPinLocation("Marvin", 15, 15);

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

	int size = 79;

	// Initialize objects for testing.
	PinAssembly object(size);
	PinAssembly objectCopy(size);
	std::shared_ptr<PinAssembly> objectClone;

	// Set up the object.
	std::shared_ptr<SFRPin> component = std::make_shared<SFRPin>();
	component->setName("Marvin");
	std::shared_ptr<SFRData> subComponent = std::make_shared<SFRData>("Depressed");
	subComponent->setValue(10);
	component->addData(subComponent, 0);

	object.addPin(std::make_shared<SFRPin>("Ford"));
	object.addPin(std::make_shared<SFRPin>("Zaphod"));
	object.addPin(component);

	object.setPinLocation("Ford", 0, 0);
	object.setPinLocation("Ford", 3, 4);
	object.setPinLocation("Zaphod", 18, 35);
	object.setPinLocation("Zaphod", 15, 36);
	object.setPinLocation("Zaphod", 76, 77);
	object.setPinLocation("Marvin", 13, 13);
	object.setPinLocation("Marvin", 14, 14);
	object.setPinLocation("Marvin", 15, 15);

	/* ---- Check copying. ---- */
	// Make sure the object is not the same as its copy.
	BOOST_REQUIRE_EQUAL(false, object == objectCopy);

	// Copy the object.
	objectCopy = PinAssembly(object);

	// Make sure the object is now the same as its copy.
	BOOST_REQUIRE_EQUAL(true, object == objectCopy);
	/* ------------------------ */

	/* ---- Check cloning. ---- */
	// Clone the object.
	objectClone = std::dynamic_pointer_cast<PinAssembly>(object.clone());

	// Make sure the object is now the same as its clone.
	BOOST_REQUIRE_EQUAL(true, object == *objectClone);
	/* ------------------------ */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_SUITE_END()
