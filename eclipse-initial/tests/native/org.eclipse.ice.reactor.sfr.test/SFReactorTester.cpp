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
#include <SFReactor.h>

#include <limits>
#include <vector>

#include <AssemblyType.h>
#include <assembly/PinType.h>
#include <assembly/PinAssembly.h>
#include <assembly/ReflectorAssembly.h>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(SFReactorTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code

	// We have the following constructors to test:
	// new SFReactor(int size)

	int defaultSize = 1;
	std::string defaultName = "SFReactor 1";
	std::string defaultDescription = "SFReactor 1's Description";
	int defaultId = 1;
	double defaultLatticePitch = 1.0;
	double defaultOuterFlatToFlat = 1.0;
	int defaultNumberOfAssemblies = 0;

	int size = 15;

	std::vector<AssemblyType> assemblyTypes =
		{ AssemblyType::Fuel, AssemblyType::Control, AssemblyType::Reflector,
				AssemblyType::Shield, AssemblyType::Test };

	/* ---- Test a valid construction (size = 15 > 1). ---- */
	// Initialize the reactor.
	SFReactor reactor(size);

	// Check non-default values.
	BOOST_REQUIRE_EQUAL(size, reactor.getSize());

	// Check default values.
	BOOST_REQUIRE_EQUAL(defaultName, reactor.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, reactor.getDescription());
	BOOST_REQUIRE_EQUAL(defaultId, reactor.getId());
	BOOST_REQUIRE_EQUAL(defaultLatticePitch, reactor.getLatticePitch());
	BOOST_REQUIRE_EQUAL(defaultOuterFlatToFlat, reactor.getOuterFlatToFlat());
	for (int i = 0; i < assemblyTypes.size(); i++) {
		AssemblyType type = assemblyTypes[i];
		BOOST_REQUIRE_EQUAL(defaultNumberOfAssemblies, reactor.getNumberOfAssemblies(type));
		BOOST_REQUIRE(reactor.getAssemblyNames(type).empty());
	}
	/* ---------------------------------------------------- */

	/* ---- Test an valid construction (size = 1 = 1). ---- */
	// Initialize the reactor.
	reactor = SFReactor(defaultSize);

	// Check default values.
	BOOST_REQUIRE_EQUAL(defaultSize, reactor.getSize());
	BOOST_REQUIRE_EQUAL(defaultName, reactor.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, reactor.getDescription());
	BOOST_REQUIRE_EQUAL(defaultId, reactor.getId());
	BOOST_REQUIRE_EQUAL(defaultLatticePitch, reactor.getLatticePitch());
	BOOST_REQUIRE_EQUAL(defaultOuterFlatToFlat, reactor.getOuterFlatToFlat());
	for (int i = 0; i < assemblyTypes.size(); i++) {
		AssemblyType type = assemblyTypes[i];
		BOOST_REQUIRE_EQUAL(defaultNumberOfAssemblies, reactor.getNumberOfAssemblies(type));
		BOOST_REQUIRE(reactor.getAssemblyNames(type).empty());
	}
	/* ----------------------------------------------------- */

	/* ---- Test an invalid construction (size = 0 < 1). ---- */
	// Initialize the reactor.
	reactor = SFReactor(0);

	// Check default values.
	BOOST_REQUIRE_EQUAL(defaultSize, reactor.getSize());
	BOOST_REQUIRE_EQUAL(defaultName, reactor.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, reactor.getDescription());
	BOOST_REQUIRE_EQUAL(defaultId, reactor.getId());
	BOOST_REQUIRE_EQUAL(defaultLatticePitch, reactor.getLatticePitch());
	BOOST_REQUIRE_EQUAL(defaultOuterFlatToFlat, reactor.getOuterFlatToFlat());
	for (int i = 0; i < assemblyTypes.size(); i++) {
		AssemblyType type = assemblyTypes[i];
		BOOST_REQUIRE_EQUAL(defaultNumberOfAssemblies, reactor.getNumberOfAssemblies(type));
		BOOST_REQUIRE(reactor.getAssemblyNames(type).empty());
	}
	/* ----------------------------------------------------- */

	/* ---- Test an invalid construction (size = -3 < 1). ---- */
	// Initialize the reactor.
	reactor = SFReactor(-3);

	// Check default values.
	BOOST_REQUIRE_EQUAL(defaultSize, reactor.getSize());
	BOOST_REQUIRE_EQUAL(defaultName, reactor.getName());
	BOOST_REQUIRE_EQUAL(defaultDescription, reactor.getDescription());
	BOOST_REQUIRE_EQUAL(defaultId, reactor.getId());
	BOOST_REQUIRE_EQUAL(defaultLatticePitch, reactor.getLatticePitch());
	BOOST_REQUIRE_EQUAL(defaultOuterFlatToFlat, reactor.getOuterFlatToFlat());
	for (int i = 0; i < assemblyTypes.size(); i++) {
		AssemblyType type = assemblyTypes[i];
		BOOST_REQUIRE_EQUAL(defaultNumberOfAssemblies, reactor.getNumberOfAssemblies(type));
		BOOST_REQUIRE(reactor.getAssemblyNames(type).empty());
	}
	/* ------------------------------------------------------ */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkLatticePitch) {
	// begin-user-code

	// A reactor to play with.
	SFReactor reactor(10);

	// Some lattice pitch values.
	double defaultLatticePitch = 1.0;
	double latticePitch = 2.0;
	double invalidLatticePitch = 0.0;

	// Check the default value.
	BOOST_REQUIRE_EQUAL(defaultLatticePitch, reactor.getLatticePitch());

	// We should be able to set a new value.
	reactor.setLatticePitch(latticePitch);
	BOOST_REQUIRE_EQUAL(latticePitch, reactor.getLatticePitch());

	// Setting to an invalid value should change nothing.
	reactor.setLatticePitch(invalidLatticePitch);
	BOOST_REQUIRE_EQUAL(latticePitch, reactor.getLatticePitch());

	// We should be able to set a new value.
	latticePitch = std::numeric_limits<double>::min();
	reactor.setLatticePitch(latticePitch);
	BOOST_REQUIRE_EQUAL(latticePitch, reactor.getLatticePitch());

	// Setting to an invalid value should change nothing.
	invalidLatticePitch = -5.0;
	reactor.setLatticePitch(invalidLatticePitch);
	BOOST_REQUIRE_EQUAL(latticePitch, reactor.getLatticePitch());

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkOuterFlatToFlat) {
	// begin-user-code

	// A reactor to play with.
	SFReactor reactor(10);

	// Some outer flat-to-flat values.
	double defaultOuterFlatToFlat = 1.0;
	double outerFlatToFlat = 2.0;
	double invalidOuterFlatToFlat = 0.0;

	// Check the default value.
	BOOST_REQUIRE_EQUAL(defaultOuterFlatToFlat, reactor.getOuterFlatToFlat());

	// We should be able to set a new value.
	reactor.setOuterFlatToFlat(outerFlatToFlat);
	BOOST_REQUIRE_EQUAL(outerFlatToFlat, reactor.getOuterFlatToFlat());

	// Setting to an invalid value should change nothing.
	reactor.setOuterFlatToFlat(invalidOuterFlatToFlat);
	BOOST_REQUIRE_EQUAL(outerFlatToFlat, reactor.getOuterFlatToFlat());

	// We should be able to set a new value.
	outerFlatToFlat = std::numeric_limits<double>::min();
	reactor.setOuterFlatToFlat(outerFlatToFlat);
	BOOST_REQUIRE_EQUAL(outerFlatToFlat, reactor.getOuterFlatToFlat());

	// Setting to an invalid value should change nothing.
	invalidOuterFlatToFlat = -5.0;
	reactor.setOuterFlatToFlat(invalidOuterFlatToFlat);
	BOOST_REQUIRE_EQUAL(outerFlatToFlat, reactor.getOuterFlatToFlat());

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkAssemblyAddRem) {
	// begin-user-code

	int size = 15;

	std::vector<int> boundaryIndexes;
	boundaryIndexes.push_back(-1);
	boundaryIndexes.push_back(0);
	boundaryIndexes.push_back(size - 1);
	boundaryIndexes.push_back(size);

	// An SFReactor to test.
	SFReactor reactor(size);

	// Initialize some assemblies to add/remove from the reactor.
	std::shared_ptr<PinAssembly> InnerFuelAssembly = std::make_shared<PinAssembly>("Vogons", 41, PinType::InnerFuel);
	std::shared_ptr<PinAssembly> blanketFuelAssembly = std::make_shared<PinAssembly>("Hooloovoo", 43, PinType::BlanketFuel);
	std::shared_ptr<PinAssembly> primaryControlAssembly = std::make_shared<PinAssembly>("Mice", 42, PinType::PrimaryControl);
	std::shared_ptr<PinAssembly> secondaryControlAssembly = std::make_shared<PinAssembly>("Dolphins", 32, PinType::SecondaryControl);
	std::shared_ptr<ReflectorAssembly> reflectorAssembly = std::make_shared<ReflectorAssembly>("Grebulons", 64);
	std::shared_ptr<SFRAssembly> plainAssembly = std::make_shared<SFRAssembly>(42);

	AssemblyType assemblyType;
	std::string name;
	std::string nullString;
	std::shared_ptr<SFRComponent> nullComponent;
	std::shared_ptr<SFRComposite> nullComposite;
	std::vector<AssemblyType> assemblyTypes = { AssemblyType::Fuel, AssemblyType::Control, AssemblyType::Reflector };
	std::vector<std::string> names;
	std::vector<int> locations;

	// Methods to test:
	// public bool addAssembly(AssemblyType type, std::shared_ptr<SFRComposite> assembly)
	// public bool removeAssembly(AssemblyType type, std::string name)
	// public bool removeAssemblyFromLocation(AssemblyType type, int row, int column)
	// public int getNumberOfAssemblies(AssemblyType type)
	// public std::vector<std::string> getAssemblyNames(AssemblyType type)
	// public std::shared_ptr<SFRComponent> getAssemblyByName(AssemblyType type, std::string name)
	// public std::shared_ptr<SFRComponent> getAssemblyByLocation(AssemblyType type, int row, int column)
	// public bool setAssemblyLocation(AssemblyType type, std::string name, int row, int column)
	// public std::vector<int> getAssemblyLocations(AssemblyType type, std::string name)

	// Make sure we have no assemblies of any type first.
	for (int i = 0; i < assemblyTypes.size(); i++) {
		AssemblyType type = assemblyTypes[i];

		BOOST_REQUIRE_EQUAL(0, reactor.getNumberOfAssemblies(type));
		BOOST_REQUIRE(reactor.getAssemblyNames(type).empty());

		// Check invalid locations.
		for (int r = 0; r < boundaryIndexes.size(); r++) {
			int row = boundaryIndexes[r];
			for (int c = 0; c < boundaryIndexes.size(); c++) {
				int column = boundaryIndexes[c];
				if (row < 0 || row == size || column < 0 || column == size) {
					BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, row, column));
				}
			}
		}

		// Check all valid locations.
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, row, column));
			}
		}
	}


	/* ---- Try various argument combinations for each method. ---- */
	for (int i = 0; i < assemblyTypes.size(); i++) {
		AssemblyType type = assemblyTypes[i];

		// The name of the assembly that we'll be adding.
		name = plainAssembly->getName();

		// Verify that there is no assembly data yet.
		BOOST_REQUIRE(nullComponent == reactor.getAssemblyByName(type, name));
		BOOST_REQUIRE_EQUAL(0, reactor.getNumberOfAssemblies(type));
		names = reactor.getAssemblyNames(type);
		BOOST_REQUIRE_EQUAL(false, std::find(names.begin(), names.end(), name) != names.end());

		// Check all the locations.
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, row, column));
			}
		}

		// addAssembly
		BOOST_REQUIRE_EQUAL(false, reactor.addAssembly(type, nullComposite));

		// addAssembly (successful)
		BOOST_REQUIRE(reactor.addAssembly(type, plainAssembly));

		// setAssemblyLocation
		BOOST_REQUIRE_EQUAL(false, reactor.setAssemblyLocation(type, nullString, 0, 0));
		BOOST_REQUIRE_EQUAL(false, reactor.setAssemblyLocation(type, "Vogons", 0, 0));
		BOOST_REQUIRE_EQUAL(false, reactor.setAssemblyLocation(type, name, -1, 0));
		BOOST_REQUIRE_EQUAL(false, reactor.setAssemblyLocation(type, name, 0, size));

		// Check setting invalid locations.
		for (int r = 0; r < boundaryIndexes.size(); r++) {
			int row = boundaryIndexes[r];
			for (int c = 0; c < boundaryIndexes.size(); c++) {
				int column = boundaryIndexes[c];
				if (row < 0 || row >= size || column < 0 || column >= size) {
					BOOST_REQUIRE_EQUAL(false, reactor.setAssemblyLocation(type, name, row, column));
					BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, row, column));
				}
			}
		}

		// setAssemblyLocation (successful)
		BOOST_REQUIRE(reactor.setAssemblyLocation(type, name, 0, 0));

		// getNumberOfAssemblies (successful)
		BOOST_REQUIRE_EQUAL(1, reactor.getNumberOfAssemblies(type));

		// getAssemblyNames (successful)
		BOOST_REQUIRE_EQUAL(1, reactor.getAssemblyNames(type).size());
		names = reactor.getAssemblyNames(type);
		BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());

		// getAssemblyByName
		BOOST_REQUIRE(nullComponent == reactor.getAssemblyByName(type, nullString));
		BOOST_REQUIRE(nullComponent == reactor.getAssemblyByName(type, "Vogons"));

		// getAssemblyByName (successful)
		BOOST_REQUIRE(*plainAssembly == *(std::dynamic_pointer_cast<SFRAssembly>(reactor.getAssemblyByName(type, name))));

		// getAssemblyByLocation
		BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, -1, 0));
		BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, 0, size));
		BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, size, -1));
		BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, 1, 1));

		// getAssemblyByLocation (successful)
		BOOST_REQUIRE(*plainAssembly == *(std::dynamic_pointer_cast<SFRAssembly>(reactor.getAssemblyByLocation(type, 0, 0))));

		// getAssemblyLocations
		BOOST_REQUIRE(reactor.getAssemblyLocations(type, nullString).empty());
		BOOST_REQUIRE(reactor.getAssemblyLocations(type, "Vogons").empty());

		// getAssemblyLocations (successful)
		BOOST_REQUIRE_EQUAL(1, reactor.getAssemblyLocations(type, name).size());
		locations = reactor.getAssemblyLocations(type, name);
		BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 0) != locations.end());

		// removeAssemblyFromLocation
		BOOST_REQUIRE_EQUAL(false, reactor.removeAssemblyFromLocation(type, -1, 0));
		BOOST_REQUIRE_EQUAL(false, reactor.removeAssemblyFromLocation(type, 0, size));
		BOOST_REQUIRE_EQUAL(false, reactor.removeAssemblyFromLocation(type, size, -1));
		BOOST_REQUIRE_EQUAL(false, reactor.removeAssemblyFromLocation(type, 1, 1));

		// removeAssemblyFromLocation (successful)
		BOOST_REQUIRE(reactor.removeAssemblyFromLocation(type, 0, 0));

		// removeAssembly
		BOOST_REQUIRE_EQUAL(false, reactor.removeAssembly(type, nullString));

		// removeAssembly (successful).
		BOOST_REQUIRE(reactor.removeAssembly(type, name));

		// Verify that the assembly has been completely removed.
		BOOST_REQUIRE(nullComponent == reactor.getAssemblyByName(type, name));
		BOOST_REQUIRE_EQUAL(0, reactor.getNumberOfAssemblies(type));
		names = reactor.getAssemblyNames(type);
		BOOST_REQUIRE_EQUAL(false, std::find(names.begin(), names.end(), name) != names.end());

		// Check all the locations.
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, row, column));
			}
		}
	}
	/* ------------------------------------------------------------ */

	/* ---- Test adding an assembly. ---- */
	// public boolean addAssembly()
	assemblyType = AssemblyType::Fuel;
	name = InnerFuelAssembly->getName();

	// Add an assembly.
	BOOST_REQUIRE(reactor.addAssembly(AssemblyType::Fuel, InnerFuelAssembly));
	BOOST_REQUIRE_EQUAL(false, reactor.addAssembly(AssemblyType::Fuel, InnerFuelAssembly));

	// Make sure the assembly was added, but it is not in any location.
	BOOST_REQUIRE(*InnerFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByName(assemblyType, name))));
	names = reactor.getAssemblyNames(assemblyType);
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());
	BOOST_REQUIRE_EQUAL(1, reactor.getNumberOfAssemblies(assemblyType));

	// All assembly locations should be empty.
	for (int row = 0; row < size; row++) {
		for (int column = 0; column < size; column++) {
			BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(assemblyType, row, column));
		}
	}
	BOOST_REQUIRE(reactor.getAssemblyLocations(assemblyType, name).empty());

	// Add the assembly to some locations.

	// Put it in the first spot.
	BOOST_REQUIRE(reactor.setAssemblyLocation(assemblyType, name, 0, 0));
	BOOST_REQUIRE(*InnerFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, 0, 0))));

	// Put it in a middle spot.
	BOOST_REQUIRE(reactor.setAssemblyLocation(assemblyType, name, 12, 10));
	BOOST_REQUIRE(*InnerFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, 12, 10))));

	// Put it in a middle spot.
	BOOST_REQUIRE(reactor.setAssemblyLocation(assemblyType, name, 4, 2));
	BOOST_REQUIRE(*InnerFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, 4, 2))));

	// Put it in the last spot.
	BOOST_REQUIRE(reactor.setAssemblyLocation(assemblyType, name, size - 1, size - 1));
	BOOST_REQUIRE(*InnerFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, 0, 0))));

	// Put it in an invalid spot.
	BOOST_REQUIRE_EQUAL(false, reactor.setAssemblyLocation(assemblyType, name, 0, -1));
	BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(assemblyType, 0, -1));

	// Check the assembly's locations.
	BOOST_REQUIRE_EQUAL(4, reactor.getAssemblyLocations(assemblyType, name).size());
	locations = reactor.getAssemblyLocations(assemblyType, name);
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 0) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 12 * size + 10) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 4 * size + 2) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), size * size - 1) != locations.end());
	/* ---------------------------------- */

	/* ---- Test overriding an assembly in a location. ---- */

	// Try overriding a location with the assembly that's already there.
	BOOST_REQUIRE_EQUAL(false, reactor.setAssemblyLocation(assemblyType, name, 0, 0));

	// Verify that the core fuel assembly is still in location 0, 0.
	BOOST_REQUIRE(*InnerFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, 0, 0))));

	// Add the other fuel assembly.
	BOOST_REQUIRE(reactor.addAssembly(assemblyType, blanketFuelAssembly));

	// Make sure it was added and that the core fuel assembly is still there.
	BOOST_REQUIRE_EQUAL(2, reactor.getNumberOfAssemblies(assemblyType));
	BOOST_REQUIRE(*InnerFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByName(assemblyType, name))));
	names = reactor.getAssemblyNames(assemblyType);
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());
	BOOST_REQUIRE(*blanketFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByName(assemblyType, blanketFuelAssembly->getName()))));
	names = reactor.getAssemblyNames(assemblyType);
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), blanketFuelAssembly->getName()) != names.end());

	// Verify that the core fuel assembly is still in location 0, 0.
	BOOST_REQUIRE(*InnerFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, 0, 0))));

	// Override location 0, 0.
	BOOST_REQUIRE(reactor.setAssemblyLocation(assemblyType, blanketFuelAssembly->getName(), 0, 0));

	// It should now be the blanket fuel assembly in that spot.
	BOOST_REQUIRE(*blanketFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, 0, 0))));
	/* ---------------------------------------------------- */

	/* ---- Test adding another type of assembly. ---- */
	// Add another type of assembly.
	BOOST_REQUIRE(reactor.addAssembly(AssemblyType::Control, primaryControlAssembly));

	// Make sure it was added.
	BOOST_REQUIRE(*primaryControlAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByName(AssemblyType::Control, primaryControlAssembly->getName()))));
	BOOST_REQUIRE_EQUAL(1, reactor.getNumberOfAssemblies(AssemblyType::Control));

	// Make sure the fuel assemblies are still there.
	BOOST_REQUIRE_EQUAL(2, reactor.getNumberOfAssemblies(assemblyType));
	BOOST_REQUIRE(*InnerFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByName(assemblyType, name))));
	names = reactor.getAssemblyNames(assemblyType);
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());
	BOOST_REQUIRE(*blanketFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByName(assemblyType, blanketFuelAssembly->getName()))));
	names = reactor.getAssemblyNames(assemblyType);
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), blanketFuelAssembly->getName()) != names.end());

	// Add it to location 12, 10.
	BOOST_REQUIRE(reactor.setAssemblyLocation(AssemblyType::Control, primaryControlAssembly->getName(), 12, 10));

	// It should be there.
	BOOST_REQUIRE(*primaryControlAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(AssemblyType::Control, 12, 10))));

	// The core fuel assembly should still be there for the fuel type.
	BOOST_REQUIRE(*InnerFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, 12, 10))));
	/* ----------------------------------------------- */

	/* ---- Test removing an assembly from a location. ---- */
	// public boolean removeAssemblyFromLocation()
	BOOST_REQUIRE(reactor.removeAssemblyFromLocation(assemblyType, 12, 10));

	// The core fuel assembly shouldn't be there.
	BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(assemblyType, 12, 10));

	// The control assembly should still be there.
	BOOST_REQUIRE(*primaryControlAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(AssemblyType::Control, 12, 10))));
	/* ---------------------------------------------------- */

	/* ---- Test removing an assembly completely. ---- */
	// public boolean removeAssembly()
	BOOST_REQUIRE(reactor.removeAssembly(assemblyType, name));

	// The reactor shouldn't know about the core fuel assembly.
	BOOST_REQUIRE(nullComponent == reactor.getAssemblyByName(assemblyType, name));
	BOOST_REQUIRE_EQUAL(1, reactor.getNumberOfAssemblies(assemblyType));
	names = reactor.getAssemblyNames(assemblyType);
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), blanketFuelAssembly->getName()) != names.end());
	names = reactor.getAssemblyNames(assemblyType);
	BOOST_REQUIRE_EQUAL(false, std::find(names.begin(), names.end(), name) != names.end());

	// The core fuel assembly should not be in any location.
	for (int row = 0; row < size; row++) {
		for (int column = 0; column < size; column++) {
			if (row == 0 && column == 0) {
				BOOST_REQUIRE(*blanketFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, row, column))));
			} else {
				BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(assemblyType, row, column));
			}
		}
	}
	BOOST_REQUIRE(reactor.getAssemblyLocations(assemblyType, name).empty());
	/* ----------------------------------------------- */

	// Add another control assembly.
	BOOST_REQUIRE(reactor.addAssembly(AssemblyType::Control, secondaryControlAssembly));
	BOOST_REQUIRE(reactor.setAssemblyLocation(AssemblyType::Control, secondaryControlAssembly->getName(), 10, 10));

	// Add a reflector assembly and set a location for it.
	BOOST_REQUIRE(reactor.addAssembly(AssemblyType::Reflector, reflectorAssembly));
	BOOST_REQUIRE(reactor.setAssemblyLocation(AssemblyType::Reflector, reflectorAssembly->getName(), 10, 10));

	/* ---- Check the fuel assemblies in the reactor. ---- */
	// Check the blanket fuel assembly.
	assemblyType = AssemblyType::Fuel;

	// Check the number of assemblies and their names.
	BOOST_REQUIRE_EQUAL(1, reactor.getNumberOfAssemblies(assemblyType));
	names = reactor.getAssemblyNames(assemblyType);
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), blanketFuelAssembly->getName()) != names.end());

	// Make sure we can look up the assembly by name.
	BOOST_REQUIRE(*blanketFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByName(assemblyType, blanketFuelAssembly->getName()))));

	// Check the contents of each location.
	for (int row = 0; row < size; row++) {
		for (int column = 0; column < size; column++) {
			if (row == 0 && column == 0) {
				BOOST_REQUIRE(*blanketFuelAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, row, column))));
			} else {
				BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(assemblyType, row, column));
			}
		}
	}
	/* --------------------------------------------------- */

	/* ---- Check the control assemblies in the reactor. ---- */
	// Check the control assemblies.
	assemblyType = AssemblyType::Control;

	// Check the number of assemblies and their names.
	BOOST_REQUIRE_EQUAL(2, reactor.getNumberOfAssemblies(assemblyType));
	names = reactor.getAssemblyNames(assemblyType);
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), primaryControlAssembly->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), secondaryControlAssembly->getName()) != names.end());

	// Make sure we can look up the assembly by name.
	BOOST_REQUIRE(*primaryControlAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByName(assemblyType, primaryControlAssembly->getName()))));
	BOOST_REQUIRE(*secondaryControlAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByName(assemblyType, secondaryControlAssembly->getName()))));

	// Check the contents of each location.
	for (int row = 0; row < size; row++) {
		for (int column = 0; column < size; column++) {
			if (row == 12 && column == 10) {
				BOOST_REQUIRE(*primaryControlAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, row, column))));
			} else if (row == 10 && column == 10) {
				BOOST_REQUIRE(*secondaryControlAssembly == *(std::dynamic_pointer_cast<PinAssembly>(reactor.getAssemblyByLocation(assemblyType, row, column))));
			} else {
				BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(assemblyType, row, column));
			}
		}
	}
	/* ------------------------------------------------------ */

	/* ---- Check the reflector assemblies in the reactor. ---- */
	// Check the reflector assembly.
	assemblyType = AssemblyType::Reflector;

	// Check the number of assemblies and their names.
	BOOST_REQUIRE_EQUAL(1, reactor.getNumberOfAssemblies(assemblyType));
	names = reactor.getAssemblyNames(assemblyType);
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), reflectorAssembly->getName()) != names.end());

	// Make sure we can look up the assembly by name.
	BOOST_REQUIRE(*reflectorAssembly == *(std::dynamic_pointer_cast<ReflectorAssembly>(reactor.getAssemblyByName(assemblyType, reflectorAssembly->getName()))));

	// Check the contents of each location.
	for (int row = 0; row < size; row++) {
		for (int column = 0; column < size; column++) {
			if (row == 10 && column == 10) {
				BOOST_REQUIRE(*reflectorAssembly == *(std::dynamic_pointer_cast<ReflectorAssembly>(reactor.getAssemblyByLocation(assemblyType, row, column))));
			} else {
				BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(assemblyType, row, column));
			}
		}
	}
	/* -------------------------------------------------------- */

	/* ---- Remove everything and verify. ---- */

	// Remove them.
	BOOST_REQUIRE(reactor.removeAssembly(AssemblyType::Fuel, blanketFuelAssembly->getName()));
	BOOST_REQUIRE(reactor.removeAssembly(AssemblyType::Control, primaryControlAssembly->getName()));
	BOOST_REQUIRE(reactor.removeAssembly(AssemblyType::Control, secondaryControlAssembly->getName()));
	BOOST_REQUIRE(reactor.removeAssembly(AssemblyType::Reflector, reflectorAssembly->getName()));

	// Make sure you can't look them up by name any more.
	BOOST_REQUIRE(nullComponent == reactor.getAssemblyByName(AssemblyType::Fuel, blanketFuelAssembly->getName()));
	BOOST_REQUIRE(nullComponent == reactor.getAssemblyByName(AssemblyType::Control, primaryControlAssembly->getName()));
	BOOST_REQUIRE(nullComponent == reactor.getAssemblyByName(AssemblyType::Control, secondaryControlAssembly->getName()));
	BOOST_REQUIRE(nullComponent == reactor.getAssemblyByName(AssemblyType::Reflector, reflectorAssembly->getName()));

	// Make sure we have no assemblies of any type first.
	for (int i = 0; i < assemblyTypes.size(); i++) {
		AssemblyType type = assemblyTypes[i];

		BOOST_REQUIRE_EQUAL(0, reactor.getNumberOfAssemblies(type));
		BOOST_REQUIRE(reactor.getAssemblyNames(type).empty());

		// Check invalid locations.
		for (int r = 0; r < boundaryIndexes.size(); r++) {
			int row = boundaryIndexes[r];
			for (int c = 0; c < boundaryIndexes.size(); c++) {
				int column = boundaryIndexes[c];
				if (row < 0 || row == size || column < 0 || column == size) {
					BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, row, column));
				}
			}
		}

		// Check all valid locations.
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				BOOST_REQUIRE(nullComponent == reactor.getAssemblyByLocation(type, row, column));
			}
		}
	}
	/* --------------------------------------- */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkCompositeImplementation) {
	// begin-user-code

	// Initialize a reactor to test.
	SFReactor reactor(15);

	std::shared_ptr<SFRComposite> composite;
	std::shared_ptr<SFRComponent> component;
	std::string name;
	std::vector<std::string> names;
	std::vector<std::shared_ptr<Component> > components;
	std::string nullString;

	/* ---- Test getComponent and its variations. ---- */
	// Tests the following methods:
	// public std::shared_ptr<Component> getComponent(int childId)
	// public std::shared_ptr<SFRComponent> getComponent(std::string name)
	// public std::vector<std::string> getComponentNames()
	// public int getNumberOfComponents()
	// public std::vector<std::shared_ptr<Component>> getComponents()


	std::vector<AssemblyType> assemblyTypes =
		{ AssemblyType::Fuel, AssemblyType::Control, AssemblyType::Reflector,
			AssemblyType::Shield, AssemblyType::Test };
	std::vector<std::string> assemblyNames =
		{ "Fuel Assembly", "Control Assembly", "Reflector Assembly",
				"Shield Assembly", "Test Assembly" };
	int numberOfComposites = assemblyTypes.size();

	// Make sure the reactor maintains a map of Composites based on the
	// possible AssemblyTypes.
	int id = 1;
	for (int i = 0; i < assemblyTypes.size(); i++) {
		AssemblyType assemblyType = assemblyTypes[i];
		std::string typeName = assemblyNames[i];
		name = typeName + " Composite";

		// Put a new Composite for the AssemblyType into the Map.
		composite = std::make_shared<SFRComposite>();

		// Set the name, description, and ID of each Composite. This also
		// increments the ID counter.
		composite->setName(name);
		composite->setDescription("A Composite that contains many " + typeName + " Components.");
		composite->setId(id);

		BOOST_REQUIRE(*composite == *(std::dynamic_pointer_cast<SFRComposite>(reactor.getComponent(id++))));
		BOOST_REQUIRE(*composite == *(std::dynamic_pointer_cast<SFRComposite>(reactor.getComponent(name))));
		names = reactor.getComponentNames();
		BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());
	}

	// Make sure the reactor only has these Composites.
	BOOST_REQUIRE_EQUAL(numberOfComposites, reactor.getNumberOfComponents());
	BOOST_REQUIRE_EQUAL(numberOfComposites, reactor.getComponents().size());
	BOOST_REQUIRE_EQUAL(numberOfComposites, reactor.getComponentNames().size());
	/* ----------------------------------------------- */

	// Make sure we cannot add or remove components through the API
	// inherited from SFRComposite (tests below).

	// Tests the following methods:
	// public void addComponent(Component child)
	// public void removeComponent(int childId)
	// public void removeComponent(std::string name)

	/* ---- Make sure we cannot add components directly. ---- */
	component = std::make_shared<SFRComponent>();
	reactor.addComponent(component);

	BOOST_REQUIRE_EQUAL(numberOfComposites, reactor.getNumberOfComponents());
	components = reactor.getComponents();
	BOOST_REQUIRE_EQUAL(false, std::find(components.begin(), components.end(), std::dynamic_pointer_cast<Component>(component)) != components.end());

	// Try the same with a Composite.
	composite = std::make_shared<SFRComposite>();
	reactor.addComponent(composite);

	BOOST_REQUIRE_EQUAL(numberOfComposites, reactor.getNumberOfComponents());
	components = reactor.getComponents();
	BOOST_REQUIRE_EQUAL(false, std::find(components.begin(), components.end(), std::dynamic_pointer_cast<Component>(composite)) != components.end());

	/* ------------------------------------------------------ */

	/* ---- Make sure we cannot remove components directly. ---- */
	// Get the first Composite stored (and make sure it's valid!).
	id = 1;
	name = assemblyNames[0] + " Composite";
	composite = std::dynamic_pointer_cast<SFRComposite>(reactor.getComponent(id));

	// Try to remove it via ID.
	reactor.removeComponent(id);
	BOOST_REQUIRE_EQUAL(numberOfComposites, reactor.getNumberOfComponents());
	components = reactor.getComponents();
	BOOST_REQUIRE(std::find(components.begin(), components.end(), std::dynamic_pointer_cast<Component>(composite)) != components.end());

	// Try to remove it via name.
	reactor.removeComponent(name);
	BOOST_REQUIRE_EQUAL(numberOfComposites, reactor.getNumberOfComponents());
	components = reactor.getComponents();
	BOOST_REQUIRE(std::find(components.begin(), components.end(), std::dynamic_pointer_cast<Component>(composite)) != components.end());

	// Try removing an invalid component.
	reactor.removeComponent(nullString);
	BOOST_REQUIRE_EQUAL(numberOfComposites, reactor.getNumberOfComponents());
	/* --------------------------------------------------------- */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	int size = 19;

	// Initialize objects for testing.
	SFReactor object(size);
	SFReactor equalObject(size);
	SFReactor unequalObject(size);

	// Set up the object and equalObject.

	std::shared_ptr<PinAssembly> fuelAssembly;
	std::shared_ptr<PinAssembly> controlAssembly;
	std::shared_ptr<ReflectorAssembly> reflectorAssembly;
	std::shared_ptr<SFRPin> pin;
	std::shared_ptr<SFRRod> rod;
	std::shared_ptr<SFRData> data1;
	std::shared_ptr<SFRData> data2;

	/* ---- Set up some data to go into the object. ---- */
	fuelAssembly = std::make_shared<PinAssembly>("Assembly A", 15, PinType::InnerFuel);
	controlAssembly = std::make_shared<PinAssembly>("Assembly B", 14, PinType::PrimaryControl);
	reflectorAssembly = std::make_shared<ReflectorAssembly>("Assembly C", 17);

	pin = std::make_shared<SFRPin>("Zaphod");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(0);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(1);

	fuelAssembly->addPin(pin);
	fuelAssembly->setPinLocation(pin->getName(), 0, 0);
	fuelAssembly->setPinLocation(pin->getName(), 1, 3);
	fuelAssembly->setPinLocation(pin->getName(), 4, 1);
	controlAssembly->addPin(pin);
	controlAssembly->setPinLocation(pin->getName(), 0, 0);
	controlAssembly->setPinLocation(pin->getName(), 2, 4);
	controlAssembly->setPinLocation(pin->getName(), 3, 2);

	pin = std::make_shared<SFRPin>("Trillian");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(1);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(0);

	fuelAssembly->addPin(pin);
	fuelAssembly->setPinLocation(pin->getName(), 1, 1);
	fuelAssembly->setPinLocation(pin->getName(), 4, 5);
	fuelAssembly->setPinLocation(pin->getName(), 8, 8);
	controlAssembly->addPin(pin);
	controlAssembly->setPinLocation(pin->getName(), 2, 4);
	controlAssembly->setPinLocation(pin->getName(), 5, 6);
	controlAssembly->setPinLocation(pin->getName(), 1, 3);

	rod = std::make_shared<SFRRod>("Arthur");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(1);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(0);

	reflectorAssembly->addRod(rod);
	reflectorAssembly->setRodLocation(rod->getName(), 0, 0);
	reflectorAssembly->setRodLocation(rod->getName(), 1, 3);
	reflectorAssembly->setRodLocation(rod->getName(), 4, 1);
	reflectorAssembly->setRodLocation(rod->getName(), 1, 1);

	rod = std::make_shared<SFRRod>("Ford");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(0);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(1);

	reflectorAssembly->addRod(rod);
	reflectorAssembly->setRodLocation(rod->getName(), 1, 0);
	reflectorAssembly->setRodLocation(rod->getName(), 6, 3);
	reflectorAssembly->setRodLocation(rod->getName(), 4, 7);
	reflectorAssembly->setRodLocation(rod->getName(), 6, 1);

	object.addAssembly(fuelAssembly->getAssemblyType(), fuelAssembly);
	object.addAssembly(controlAssembly->getAssemblyType(), controlAssembly);
	object.addAssembly(reflectorAssembly->getAssemblyType(), reflectorAssembly);

	object.setAssemblyLocation(fuelAssembly->getAssemblyType(), fuelAssembly->getName(), 15, 15);
	object.setAssemblyLocation(fuelAssembly->getAssemblyType(), fuelAssembly->getName(), 12, 10);
	object.setAssemblyLocation(fuelAssembly->getAssemblyType(), fuelAssembly->getName(), 17, 17);
	object.setAssemblyLocation(controlAssembly->getAssemblyType(), controlAssembly->getName(), 15, 15);
	object.setAssemblyLocation(controlAssembly->getAssemblyType(), controlAssembly->getName(), 12, 10);
	object.setAssemblyLocation(controlAssembly->getAssemblyType(), controlAssembly->getName(), 17, 17);
	object.setAssemblyLocation(reflectorAssembly->getAssemblyType(), reflectorAssembly->getName(), 12, 10);
	object.setAssemblyLocation(reflectorAssembly->getAssemblyType(), reflectorAssembly->getName(), 17, 17);
	/* ------------------------------------------------- */

	/* ---- Set up some data to go into the equalObject. ---- */
	fuelAssembly = std::make_shared<PinAssembly>("Assembly A", 15, PinType::InnerFuel);
	controlAssembly = std::make_shared<PinAssembly>("Assembly B", 14, PinType::PrimaryControl);
	reflectorAssembly = std::make_shared<ReflectorAssembly>("Assembly C", 17);

	pin = std::make_shared<SFRPin>("Zaphod");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(0);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(1);

	fuelAssembly->addPin(pin);
	fuelAssembly->setPinLocation(pin->getName(), 0, 0);
	fuelAssembly->setPinLocation(pin->getName(), 1, 3);
	fuelAssembly->setPinLocation(pin->getName(), 4, 1);
	controlAssembly->addPin(pin);
	controlAssembly->setPinLocation(pin->getName(), 0, 0);
	controlAssembly->setPinLocation(pin->getName(), 2, 4);
	controlAssembly->setPinLocation(pin->getName(), 3, 2);

	pin = std::make_shared<SFRPin>("Trillian");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(1);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(0);

	fuelAssembly->addPin(pin);
	fuelAssembly->setPinLocation(pin->getName(), 1, 1);
	fuelAssembly->setPinLocation(pin->getName(), 4, 5);
	fuelAssembly->setPinLocation(pin->getName(), 8, 8);
	controlAssembly->addPin(pin);
	controlAssembly->setPinLocation(pin->getName(), 2, 4);
	controlAssembly->setPinLocation(pin->getName(), 5, 6);
	controlAssembly->setPinLocation(pin->getName(), 1, 3);

	rod = std::make_shared<SFRRod>("Arthur");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(1);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(0);

	reflectorAssembly->addRod(rod);
	reflectorAssembly->setRodLocation(rod->getName(), 0, 0);
	reflectorAssembly->setRodLocation(rod->getName(), 1, 3);
	reflectorAssembly->setRodLocation(rod->getName(), 4, 1);
	reflectorAssembly->setRodLocation(rod->getName(), 1, 1);

	rod = std::make_shared<SFRRod>("Ford");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(0);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(1);

	reflectorAssembly->addRod(rod);
	reflectorAssembly->setRodLocation(rod->getName(), 1, 0);
	reflectorAssembly->setRodLocation(rod->getName(), 6, 3);
	reflectorAssembly->setRodLocation(rod->getName(), 4, 7);
	reflectorAssembly->setRodLocation(rod->getName(), 6, 1);

	equalObject.addAssembly(fuelAssembly->getAssemblyType(), fuelAssembly);
	equalObject.addAssembly(controlAssembly->getAssemblyType(), controlAssembly);
	equalObject.addAssembly(reflectorAssembly->getAssemblyType(), reflectorAssembly);

	equalObject.setAssemblyLocation(fuelAssembly->getAssemblyType(), fuelAssembly->getName(), 15, 15);
	equalObject.setAssemblyLocation(fuelAssembly->getAssemblyType(), fuelAssembly->getName(), 12, 10);
	equalObject.setAssemblyLocation(fuelAssembly->getAssemblyType(), fuelAssembly->getName(), 17, 17);
	equalObject.setAssemblyLocation(controlAssembly->getAssemblyType(), controlAssembly->getName(), 15, 15);
	equalObject.setAssemblyLocation(controlAssembly->getAssemblyType(), controlAssembly->getName(), 12, 10);
	equalObject.setAssemblyLocation(controlAssembly->getAssemblyType(), controlAssembly->getName(), 17, 17);
	equalObject.setAssemblyLocation(reflectorAssembly->getAssemblyType(), reflectorAssembly->getName(), 12, 10);
	equalObject.setAssemblyLocation(reflectorAssembly->getAssemblyType(), reflectorAssembly->getName(), 17, 17);
	/* ------------------------------------------------------ */

	// Set up the unequalObject.

	/* ---- Set up some data to go into the unequalObject. ---- */
	fuelAssembly = std::make_shared<PinAssembly>("Assembly A", 15, PinType::InnerFuel);
	controlAssembly = std::make_shared<PinAssembly>("Assembly B", 14, PinType::PrimaryControl);
	reflectorAssembly = std::make_shared<ReflectorAssembly>("Assembly C", 17);

	pin = std::make_shared<SFRPin>("Zaphod");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(0);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(1);

	fuelAssembly->addPin(pin);
	fuelAssembly->setPinLocation(pin->getName(), 0, 0);
	fuelAssembly->setPinLocation(pin->getName(), 1, 3);
	fuelAssembly->setPinLocation(pin->getName(), 4, 1);
	controlAssembly->addPin(pin);
	controlAssembly->setPinLocation(pin->getName(), 0, 0);
	controlAssembly->setPinLocation(pin->getName(), 2, 4);
	controlAssembly->setPinLocation(pin->getName(), 3, 2);

	pin = std::make_shared<SFRPin>("Trillian");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(1);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(0);

	fuelAssembly->addPin(pin);
	fuelAssembly->setPinLocation(pin->getName(), 1, 1);
	fuelAssembly->setPinLocation(pin->getName(), 4, 5);
	fuelAssembly->setPinLocation(pin->getName(), 8, 9); // Different!
	controlAssembly->addPin(pin);
	controlAssembly->setPinLocation(pin->getName(), 2, 4);
	controlAssembly->setPinLocation(pin->getName(), 5, 6);
	controlAssembly->setPinLocation(pin->getName(), 1, 3);

	rod = std::make_shared<SFRRod>("Arthur");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(1);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(0);

	reflectorAssembly->addRod(rod);
	reflectorAssembly->setRodLocation(rod->getName(), 0, 0);
	reflectorAssembly->setRodLocation(rod->getName(), 1, 3);
	reflectorAssembly->setRodLocation(rod->getName(), 4, 1);
	reflectorAssembly->setRodLocation(rod->getName(), 1, 1);

	rod = std::make_shared<SFRRod>("Ford");
	data1 = std::make_shared<SFRData>("From Earth");
	data1->setValue(0);
	data2 = std::make_shared<SFRData>("From Space");
	data2->setValue(1);

	reflectorAssembly->addRod(rod);
	reflectorAssembly->setRodLocation(rod->getName(), 1, 0);
	reflectorAssembly->setRodLocation(rod->getName(), 6, 3);
	reflectorAssembly->setRodLocation(rod->getName(), 4, 7);
	reflectorAssembly->setRodLocation(rod->getName(), 6, 1);

	unequalObject.addAssembly(fuelAssembly->getAssemblyType(), fuelAssembly);
	unequalObject.addAssembly(controlAssembly->getAssemblyType(), controlAssembly);
	unequalObject.addAssembly(reflectorAssembly->getAssemblyType(), reflectorAssembly);

	unequalObject.setAssemblyLocation(fuelAssembly->getAssemblyType(), fuelAssembly->getName(), 15, 15);
	unequalObject.setAssemblyLocation(fuelAssembly->getAssemblyType(), fuelAssembly->getName(), 12, 10);
	unequalObject.setAssemblyLocation(fuelAssembly->getAssemblyType(), fuelAssembly->getName(), 17, 17);
	unequalObject.setAssemblyLocation(controlAssembly->getAssemblyType(), controlAssembly->getName(), 15, 15);
	unequalObject.setAssemblyLocation(controlAssembly->getAssemblyType(), controlAssembly->getName(), 12, 10);
	unequalObject.setAssemblyLocation(controlAssembly->getAssemblyType(), controlAssembly->getName(), 17, 17);
	unequalObject.setAssemblyLocation(reflectorAssembly->getAssemblyType(), reflectorAssembly->getName(), 12, 10);
	unequalObject.setAssemblyLocation(reflectorAssembly->getAssemblyType(), reflectorAssembly->getName(), 17, 17);
	/* -------------------------------------------------------- */

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

	int size = 34;

	// Initialize objects for testing.
	SFReactor object(size);
	SFReactor objectCopy(15);
	std::shared_ptr<SFReactor> objectClone;

	// Set up the object.

	/* ---- Check copying. ---- */
	// Make sure the object is not the same as its copy.
	BOOST_REQUIRE_EQUAL(false, object == objectCopy);

	// Copy the object.
	objectCopy = SFReactor(object);

	// Make sure the object is now the same as its copy.
	BOOST_REQUIRE_EQUAL(true, object == objectCopy);
	/* ------------------------ */

	/* ---- Check cloning. ---- */
	// Clone the object.
	objectClone = std::dynamic_pointer_cast<SFReactor>(object.clone());

	// Make sure the object is now the same as its clone.
	BOOST_REQUIRE_EQUAL(true, object == *objectClone);
	/* ------------------------ */

	return;
	// end-user-code
}
BOOST_AUTO_TEST_SUITE_END()
