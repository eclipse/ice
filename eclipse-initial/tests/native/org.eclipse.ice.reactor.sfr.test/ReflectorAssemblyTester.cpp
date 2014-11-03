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
#include <assembly/ReflectorAssembly.h>
#include <assembly/SFRRod.h>


using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(ReflectorAssemblyTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
		// begin-user-code
		
		// Invalid, default, and valid values to use in the constructor.
		int invalidSize = 0;
		int defaultSize = 1;
		int size = 31;

		std::string nullName;
		std::string emptyName = "            ";
		std::string defaultName = "SFR Reflector Assembly 1";
		std::string name = "Tricia";

		// Other defaults.
		AssemblyType defaultType = Reflector;
		std::string defaultDescription = "SFR Reflector Assembly 1's Description";
		int defaultId = 1;
		double defaultDuctThickness = 0.0;
		double defaultRodPitch = 1.0;

		/* ---- Test the basic constructor. ---- */
		// Invalid parameters.
		ReflectorAssembly assembly(invalidSize);

		BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
		BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
		BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
		BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
		BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
		BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
		BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly.getRodPitch());

		// Valid parameters.
		assembly = ReflectorAssembly(size);

		BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
		BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
		BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
		BOOST_REQUIRE_EQUAL(size, assembly.getSize());
		BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
		BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
		BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly.getRodPitch());
		/* ------------------------------------- */

		/* ---- Test the second constructor. ---- */
		// Invalid parameters.

		// All invalid.
		assembly = ReflectorAssembly(nullName, invalidSize);

		BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
		BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
		BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
		BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
		BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
		BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
		BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly.getRodPitch());

		// Name invalid.
		assembly = ReflectorAssembly(emptyName, size);

		BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
		BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
		BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
		BOOST_REQUIRE_EQUAL(size, assembly.getSize());
		BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
		BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
		BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly.getRodPitch());

		// Size invalid.
		assembly = ReflectorAssembly(name, invalidSize);

		BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
		BOOST_REQUIRE_EQUAL(name, assembly.getName());
		BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
		BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
		BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
		BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
		BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly.getRodPitch());

		// Valid parameters.
		assembly = ReflectorAssembly(name, size);

		BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
		BOOST_REQUIRE_EQUAL(name, assembly.getName());
		BOOST_REQUIRE_EQUAL(defaultDescription, assembly.getDescription());
		BOOST_REQUIRE_EQUAL(size, assembly.getSize());
		BOOST_REQUIRE_EQUAL(defaultType, assembly.getAssemblyType());
		BOOST_REQUIRE_EQUAL(defaultDuctThickness, assembly.getDuctThickness());
		BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly.getRodPitch());
		/* -------------------------------------- */

		return;
		// end-user-code
}

BOOST_AUTO_TEST_CASE(checkRodPitch) {
		// begin-user-code
		ReflectorAssembly assembly(1);
		double defaultRodPitch = 1.0;

		// Check the default.
		BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly.getRodPitch());

		// Set it to 1 and check it.
		assembly.setRodPitch(1.0);
		BOOST_REQUIRE_EQUAL(1.0, assembly.getRodPitch());

		// Set it to an invalid number.
		assembly.setRodPitch(-1.0);
		BOOST_REQUIRE_EQUAL(1.0, assembly.getRodPitch());

		// Set it to a valid number.
		assembly.setRodPitch(500);
		BOOST_REQUIRE_EQUAL(500, assembly.getRodPitch());
		
		return;
		// end-user-code
}

BOOST_AUTO_TEST_CASE(checkRodAddRem) {
	// begin-user-code

	// Note: I only check assembly.getRodLocations() in a few spots
	// below since it's a late addition. Further testing may be required.

	int size = 10;

	// An assembly to test.
	ReflectorAssembly assembly(size);

	// Initialize some rods to add/remove from the assembly.
	std::shared_ptr<SFRRod> rod1 = std::make_shared<SFRRod>();
	rod1->setName("Anjie");
	std::shared_ptr<SFRRod> rod2 = std::make_shared<SFRRod>();
	rod2->setName("Colin");
	std::shared_ptr<SFRRod> rod3 = std::make_shared<SFRRod>();
	rod3->setName("Dish of the Day");

	// A List of boundary indexes to test.
	std::vector<int> boundaryIndexes;
	boundaryIndexes.push_back(-1);
	boundaryIndexes.push_back(0);
	boundaryIndexes.push_back(size - 1);
	boundaryIndexes.push_back(size);

	// A set of indexes that will be empty. This makes it easier to check
	// rod locations.
	std::set<int> emptyIndexes;
	for (int i = 0; i < size * size; i++)
		emptyIndexes.insert(i);

	std::string name;

	std::shared_ptr<SFRRod> nullRod;
	std::string nullString;

	std::vector<std::string> names;
	std::vector<int> locations;

	// Methods to test:
	// public boolean addRod(std::shared_ptr<SFRRod> rod)
	// public boolean removeRod(std::string name)
	// public boolean removeRodFromLocation(int row, int column)
	// public std::vector<std::string> getRodNames()
	// public std::shared_ptr<SFRRod> getRodByName(std::string name)
	// public std::shared_ptr<SFRRod> getRodByLocation(int row, int column)
	// public bool setRodLocation(std::string name, int row, int column)
	// public std::shared_ptr<SFRRod> getRodByLocation(int row, int column)

	/* ---- Make sure the assembly is empty. ---- */
	// Check invalid locations.
	for (int r = 0; r < boundaryIndexes.size(); r++) {
		int row = boundaryIndexes[r];
		for (int c = 0; c < boundaryIndexes.size(); c++) {
			int column = boundaryIndexes[c];
			if (row < 0 || row == size || column < 0 || column == size) {
				BOOST_REQUIRE(nullRod == assembly.getRodByLocation(row, column));
			}
		}
	}

	// Check all valid locations.
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));
	/* ------------------------------------------ */

	/* ---- Try various argument combinations for each method. ---- */
	name = rod1->getName();

	// Verify that there is no rod set.
	BOOST_REQUIRE(nullRod == assembly.getRodByName(name));
	BOOST_REQUIRE_EQUAL(0, assembly.getNumberOfRods());
	BOOST_REQUIRE_EQUAL(0, assembly.getRodNames().size());
	names = assembly.getRodNames();
	BOOST_REQUIRE_EQUAL(false, std::find(names.begin(), names.end(), name) != names.end());

	// Check all the locations.
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));

	// addRod
	BOOST_REQUIRE_EQUAL(false, assembly.addRod(nullRod));

	// addRod (successful)
	BOOST_REQUIRE(assembly.addRod(rod1));

	// setRodLocation
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(nullString, -1, -1));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(nullString, 0, 0));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation("Vogons", 0, 0));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(name, -1, 0));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(name, 0, size));

	// Check setting invalid locations.
	for (int r = 0; r < boundaryIndexes.size(); r++) {
		int row = boundaryIndexes[r];
		for (int c = 0; c < boundaryIndexes.size(); c++) {
			int column = boundaryIndexes[c];
			if (row < 0 || row >= size || column < 0 || column >= size) {
				BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(name, row, column));
				BOOST_REQUIRE(nullRod == assembly.getRodByLocation(row, column));
			}
		}
	}

	// setRodLocation (successful)
	BOOST_REQUIRE(assembly.setRodLocation(name, 0, 0));

	// getNumberOfRods (successful)
	BOOST_REQUIRE_EQUAL(1, assembly.getNumberOfRods());

	// getRodNames (successful)
	BOOST_REQUIRE_EQUAL(1, assembly.getRodNames().size());
	names = assembly.getRodNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());

	// getRodByName
	BOOST_REQUIRE(nullRod == assembly.getRodByName(nullString));
	BOOST_REQUIRE(nullRod == assembly.getRodByName("Vogons"));

	// getRodByName (successful)
	BOOST_REQUIRE(*rod1 == *assembly.getRodByName(name));

	// getRodByLocation
	BOOST_REQUIRE(nullRod == assembly.getRodByLocation(-1, -1));
	BOOST_REQUIRE(nullRod == assembly.getRodByLocation(-1, 0));
	BOOST_REQUIRE(nullRod == assembly.getRodByLocation(0, size));

	// getRodByLocation (successful)
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 0));

	// getRodLocations
	BOOST_REQUIRE(assembly.getRodLocations(nullString).empty());
	BOOST_REQUIRE(assembly.getRodLocations("Vogons").empty());

	// getRodLocations (successful)
	BOOST_REQUIRE_EQUAL(1, assembly.getRodLocations(name).size());
	locations = assembly.getRodLocations(name);
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 0) != locations.end());

	// removeRodFromLocation
	BOOST_REQUIRE_EQUAL(false, assembly.removeRodFromLocation(-1, -1));
	BOOST_REQUIRE_EQUAL(false, assembly.removeRodFromLocation(-1, 0));
	BOOST_REQUIRE_EQUAL(false, assembly.removeRodFromLocation(0, size));

	// removeRodFromLocation (successful)
	BOOST_REQUIRE(assembly.removeRodFromLocation(0, 0));

	// removeRod
	BOOST_REQUIRE_EQUAL(false, assembly.removeRod(nullString));
	BOOST_REQUIRE_EQUAL(false, assembly.removeRod("Vogons"));

	// removeRod (successful)
	BOOST_REQUIRE(assembly.removeRod(name));

	// Verify that the rod has been completely removed.
	BOOST_REQUIRE(nullRod == assembly.getRodByName(name));
	BOOST_REQUIRE_EQUAL(0, assembly.getNumberOfRods());
	BOOST_REQUIRE_EQUAL(0, assembly.getRodNames().size());
	names = assembly.getRodNames();
	BOOST_REQUIRE_EQUAL(false, std::find(names.begin(), names.end(), name) != names.end());

	// Check all the locations.
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));
	/* ------------------------------------------------------------ */

	/* ---- Add an rod and set some locations. ---- */
	name = rod1->getName();
	BOOST_REQUIRE(assembly.addRod(rod1));
	BOOST_REQUIRE_EQUAL(false, assembly.addRod(rod1));

	// Verify that the rod was added.
	BOOST_REQUIRE(*rod1 == *assembly.getRodByName(name));
	BOOST_REQUIRE_EQUAL(1, assembly.getNumberOfRods());
	BOOST_REQUIRE_EQUAL(1, assembly.getRodNames().size());
	names = assembly.getRodNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());

	// All locations should be empty.
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));
	BOOST_REQUIRE(assembly.getRodLocations(name).empty());

	// The first attempt to set a location should succeed. Subsequent
	// attempts to set the same rod in the same location should fail.

	// Put it in the first spot.
	BOOST_REQUIRE(assembly.setRodLocation(name, 0, 0));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(name, 0, 0));
	emptyIndexes.erase(0);

	// Put it in a middle spot.
	BOOST_REQUIRE(assembly.setRodLocation(name, 0, 6));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(name, 0, 6));
	emptyIndexes.erase(6);

	// Put it in a middle spot.
	BOOST_REQUIRE(assembly.setRodLocation(name, 3, 7));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(name, 3, 7));
	emptyIndexes.erase(3 * size + 7);

	// Put it in the last spot.
	BOOST_REQUIRE(assembly.setRodLocation(name, size - 1, size - 1));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(name, size - 1, size - 1));
	emptyIndexes.erase(size * size - 1);

	// Verify the rod locations.
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 0));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 6));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(3, 7));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(size - 1, size - 1));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));

	// We should still be able to get the rod by location.
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 0));

	// Verify the rod locations (getRodLocations()).
	BOOST_REQUIRE_EQUAL(4, assembly.getRodLocations(name).size());
	locations = assembly.getRodLocations(name);
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 0) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 0 * size + 6) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 3 * size + 7) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), size * size - 1) != locations.end());
	/* -------------------------------------------- */

	/* ---- Test overriding a rod location. ---- */
	name = rod2->getName();
	BOOST_REQUIRE(assembly.addRod(rod2));
	BOOST_REQUIRE_EQUAL(false, assembly.addRod(rod2));

	// Verify that the rod was added.
	BOOST_REQUIRE(*rod2 == *assembly.getRodByName(name));
	BOOST_REQUIRE_EQUAL(2, assembly.getNumberOfRods());
	BOOST_REQUIRE_EQUAL(2, assembly.getRodNames().size());
	names = assembly.getRodNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), rod1->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());

	// Verify the rod locations.
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 0));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 6));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(3, 7));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(size - 1, size - 1));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));

	// Put it in a new spot.
	BOOST_REQUIRE(assembly.setRodLocation(name, 1, 1));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(name, 1, 1));
	emptyIndexes.erase(size + 1);

	// Put it in a spot occupied by rod1->
	BOOST_REQUIRE(assembly.setRodLocation(name, 3, 7));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(name, 3, 7));

	// Verify the rod locations.
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 0));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 6));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(size - 1, size - 1));
	BOOST_REQUIRE(*rod2 == *assembly.getRodByLocation(1, 1));
	BOOST_REQUIRE(*rod2 == *assembly.getRodByLocation(3, 7));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));
	/* ----------------------------------------- */

	/* ---- Test adding yet another rod. ---- */
	name = rod3->getName();
	BOOST_REQUIRE(assembly.addRod(rod3));
	BOOST_REQUIRE_EQUAL(false, assembly.addRod(rod3));

	// Verify that the rod was added.
	BOOST_REQUIRE(*rod3 == *assembly.getRodByName(name));
	BOOST_REQUIRE_EQUAL(3, assembly.getNumberOfRods());
	BOOST_REQUIRE_EQUAL(3, assembly.getRodNames().size());
	names = assembly.getRodNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), rod1->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), rod2->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());

	// Verify the rod locations.
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 0));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 6));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(size - 1, size - 1));
	BOOST_REQUIRE(*rod2 == *assembly.getRodByLocation(1, 1));
	BOOST_REQUIRE(*rod2 == *assembly.getRodByLocation(3, 7));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));

	// Put it in a new spot.
	BOOST_REQUIRE(assembly.setRodLocation(name, 3, 3));
	BOOST_REQUIRE_EQUAL(false, assembly.setRodLocation(name, 3, 3));
	emptyIndexes.erase(3 * size + 3);

	// Verify the rod locations.
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 0));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 6));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(size - 1, size - 1));
	BOOST_REQUIRE(*rod2 == *assembly.getRodByLocation(1, 1));
	BOOST_REQUIRE(*rod2 == *assembly.getRodByLocation(3, 7));
	BOOST_REQUIRE(*rod3 == *assembly.getRodByLocation(3, 3));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));
	/* -------------------------------------- */

	/* ---- Test removing a rod from a location. ---- */
	BOOST_REQUIRE(assembly.removeRodFromLocation(0, 6));
	BOOST_REQUIRE_EQUAL(false, assembly.removeRodFromLocation(0, 6));
	emptyIndexes.insert(6);

	// Verify the rod locations.
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 0));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(size - 1, size - 1));
	BOOST_REQUIRE(*rod2 == *assembly.getRodByLocation(1, 1));
	BOOST_REQUIRE(*rod2 == *assembly.getRodByLocation(3, 7));
	BOOST_REQUIRE(*rod3 == *assembly.getRodByLocation(3, 3));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));

	// Remove rod2 entirely manually.
	BOOST_REQUIRE(assembly.removeRodFromLocation(1, 1));
	BOOST_REQUIRE_EQUAL(false, assembly.removeRodFromLocation(1, 1));
	emptyIndexes.insert(size + 1);

	BOOST_REQUIRE(assembly.removeRodFromLocation(3, 7));
	BOOST_REQUIRE_EQUAL(false, assembly.removeRodFromLocation(3, 7));
	emptyIndexes.insert(3 * size + 7);

	// Verify the rod locations.
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(0, 0));
	BOOST_REQUIRE(*rod1 == *assembly.getRodByLocation(size - 1, size - 1));
	BOOST_REQUIRE(*rod3 == *assembly.getRodByLocation(3, 3));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));

	// Verify the rod locations (getRodLocations()).
	BOOST_REQUIRE_EQUAL(2, assembly.getRodLocations(rod1->getName()).size());
	locations = assembly.getRodLocations(rod1->getName());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), 0) != locations.end());
	BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), size * size - 1) != locations.end());

	// rod2 should still be in the assembly, though.

	// Verify the rods stored in the assembly.
	BOOST_REQUIRE(*rod1 == *assembly.getRodByName(rod1->getName()));
	BOOST_REQUIRE(*rod2 == *assembly.getRodByName(rod2->getName()));
	BOOST_REQUIRE(*rod3 == *assembly.getRodByName(rod3->getName()));
	BOOST_REQUIRE_EQUAL(3, assembly.getNumberOfRods());
	BOOST_REQUIRE_EQUAL(3, assembly.getRodNames().size());
	names = assembly.getRodNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), rod1->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), rod2->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), rod3->getName()) != names.end());
	/* ---------------------------------------------- */

	/* ---- Test removing a rod completely. ---- */
	BOOST_REQUIRE(assembly.removeRod(rod1->getName()));
	BOOST_REQUIRE_EQUAL(false, assembly.removeRod(rod1->getName()));

	emptyIndexes.insert(0);
	emptyIndexes.insert(size * size - 1);

	// Verify the rod locations.
	BOOST_REQUIRE(*rod3 == *assembly.getRodByLocation(3, 3));
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));

	// rod1 should not have any locations. In fact, since the rod is not in
	// the assembly, this returns an empty List.
	BOOST_REQUIRE(assembly.getRodLocations(rod1->getName()).empty());

	// Verify the rods stored in the assembly.
	BOOST_REQUIRE(nullRod == assembly.getRodByName(rod1->getName()));
	BOOST_REQUIRE(*rod2 == *assembly.getRodByName(rod2->getName()));
	BOOST_REQUIRE(*rod3 == *assembly.getRodByName(rod3->getName()));
	BOOST_REQUIRE_EQUAL(2, assembly.getNumberOfRods());
	BOOST_REQUIRE_EQUAL(2, assembly.getRodNames().size());
	names = assembly.getRodNames();
	BOOST_REQUIRE_EQUAL(false, std::find(names.begin(), names.end(), rod1->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), rod2->getName()) != names.end());
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), rod3->getName()) != names.end());
	/* ----------------------------------------- */

	/* ---- Remove everything and verify. ---- */
	BOOST_REQUIRE_EQUAL(false, assembly.removeRod(rod1->getName()));
	BOOST_REQUIRE(assembly.removeRod(rod2->getName()));
	BOOST_REQUIRE_EQUAL(false, assembly.removeRod(rod2->getName()));
	BOOST_REQUIRE(assembly.removeRod(rod3->getName()));
	BOOST_REQUIRE_EQUAL(false, assembly.removeRod(rod3->getName()));

	// Add the last rod location back to the empty index set.
	emptyIndexes.insert(size * 3 + 3);

	// Verify the rod locations.
	for (std::set<int>::iterator i = emptyIndexes.begin(); i != emptyIndexes.end(); i++)
		BOOST_REQUIRE(nullRod == assembly.getRodByLocation(*i / size, *i % size));

	// Verify the rods stored in the assembly.
	BOOST_REQUIRE(nullRod == assembly.getRodByName(rod1->getName()));
	BOOST_REQUIRE(nullRod == assembly.getRodByName(rod2->getName()));
	BOOST_REQUIRE(nullRod == assembly.getRodByName(rod3->getName()));
	BOOST_REQUIRE_EQUAL(0, assembly.getNumberOfRods());
	BOOST_REQUIRE_EQUAL(0, assembly.getRodNames().size());
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
	ReflectorAssembly assembly(15);

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
	component = std::make_shared<SFRRod>();
	component->setName(name);

	// Add it.
	BOOST_REQUIRE_EQUAL(true, assembly.addRod(std::dynamic_pointer_cast<SFRRod>(component)));

	numberOfComponents = 1;

	// Verify that it's there.
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getNumberOfComponents());
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getComponents().size());
	components = assembly.getComponents();
	BOOST_REQUIRE_EQUAL(true, std::find(components.begin(), components.end(), component) != components.end());
	BOOST_REQUIRE_EQUAL(numberOfComponents, assembly.getComponentNames().size());
	names = assembly.getComponentNames();
	BOOST_REQUIRE_EQUAL(true, std::find(names.begin(), names.end(), name) != names.end());
	BOOST_REQUIRE(*component == *(std::dynamic_pointer_cast<SFRRod>(assembly.getComponent(name))));
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
	BOOST_REQUIRE(*component == *(std::dynamic_pointer_cast<SFRRod>(assembly.getComponent(name))));

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
	BOOST_REQUIRE(*component == *(std::dynamic_pointer_cast<SFRRod>(assembly.getComponent(name))));

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
	BOOST_REQUIRE(*component == *(std::dynamic_pointer_cast<SFRRod>(assembly.getComponent(name))));
	/* --------------------------------------------------------- */

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkEquality) {
		// begin-user-code
		
		int size = 78;

		// Initialize objects for testing.
		ReflectorAssembly object(size);
		ReflectorAssembly equalObject(size);
		ReflectorAssembly unequalObject(size);

		// Set up the object and equalObject.
		SFRRod component;
		component.setName("Marvin");
		SFRData subComponent("Depressed");
		subComponent.setValue(10);
		component.addData(std::make_shared<SFRData>(subComponent), 0.0);

		object.addRod(std::make_shared<SFRRod>("Ford"));
		object.addRod(std::make_shared<SFRRod>("Zaphod"));
		object.addRod(std::make_shared<SFRRod>(component));

		object.setRodLocation("Ford", 0, 0);
		object.setRodLocation("Ford", 3, 4);
		object.setRodLocation("Zaphod", 18, 35);
		object.setRodLocation("Zaphod", 15, 36);
		object.setRodLocation("Zaphod", 76, 77);
		object.setRodLocation("Marvin", 13, 13);
		object.setRodLocation("Marvin", 14, 14);
		object.setRodLocation("Marvin", 15, 15);

		// Set up equal object
		equalObject.addRod(std::make_shared<SFRRod>("Ford"));
		equalObject.addRod(std::make_shared<SFRRod>("Zaphod"));
		equalObject.addRod(std::make_shared<SFRRod>(component));

		equalObject.setRodLocation("Ford", 0, 0);
		equalObject.setRodLocation("Ford", 3, 4);
		equalObject.setRodLocation("Zaphod", 18, 35);
		equalObject.setRodLocation("Zaphod", 15, 36);
		equalObject.setRodLocation("Zaphod", 76, 77);
		equalObject.setRodLocation("Marvin", 13, 13);
		equalObject.setRodLocation("Marvin", 14, 14);
		equalObject.setRodLocation("Marvin", 15, 15);

		// Set up the unequalObject.
		unequalObject.addRod(std::make_shared<SFRRod>("Ford"));
		unequalObject.addRod(std::make_shared<SFRRod>("Zaphod"));
		unequalObject.addRod(std::make_shared<SFRRod>(component));

		unequalObject.setRodLocation("Ford", 0, 0);
		unequalObject.setRodLocation("Ford", 3, 4);
		unequalObject.setRodLocation("Zaphod", 18, 35);
		unequalObject.setRodLocation("Zaphod", 15, 36);
		unequalObject.setRodLocation("Zaphod", 76, 77);
		unequalObject.setRodLocation("Marvin", 13, 13);
		unequalObject.setRodLocation("Marvin", 14, 14);
		unequalObject.setRodLocation("Marvin", 15, 16); // Different!

		// Check that equality is reflexive and symmetric.
		BOOST_REQUIRE(object == object);
		BOOST_REQUIRE(object == equalObject);
		BOOST_REQUIRE(equalObject == object);

		// Check that equals will fail when it should.
		BOOST_REQUIRE(!(object == unequalObject));
		BOOST_REQUIRE(!(unequalObject == object));
		
		return;
		// end-user-code
}

BOOST_AUTO_TEST_CASE(checkCopying) {
		// begin-user-code
		
		int size = 79;

		// Initialize objects for testing.
		ReflectorAssembly object(size);
		ReflectorAssembly copy(size);
		std::shared_ptr<Identifiable> clone;

		// Set up the object.
		SFRRod component;
		component.setName("Marvin");
		SFRData subComponent("Depressed");
		subComponent.setValue(10);
		component.addData(std::make_shared<SFRData>(subComponent), 0);

		object.addRod(std::make_shared<SFRRod>("Ford"));
		object.addRod(std::make_shared<SFRRod>("Zaphod"));
		object.addRod(std::make_shared<SFRRod>(component));

		object.setRodLocation("Ford", 0, 0);
		object.setRodLocation("Ford", 3, 4);
		object.setRodLocation("Zaphod", 18, 35);
		object.setRodLocation("Zaphod", 15, 36);
		object.setRodLocation("Zaphod", 76, 77);
		object.setRodLocation("Marvin", 13, 13);
		object.setRodLocation("Marvin", 14, 14);
		object.setRodLocation("Marvin", 15, 15);

		// Make sure the objects are not equal before copying.
		BOOST_REQUIRE(!(object == copy));

		// Copy the object.
		copy = ReflectorAssembly(object);

		// Make sure the contents are the same.
		BOOST_REQUIRE(object == copy);


		// Do the same for the clone operation.

		// Copy the object.
		clone = object.clone();

		// Make sure the contents are the same.
		BOOST_REQUIRE(object == *(std::dynamic_pointer_cast<ReflectorAssembly>(clone)));
		BOOST_REQUIRE(copy == *(std::dynamic_pointer_cast<ReflectorAssembly>(clone)));

		return;
		// end-user-code
}
BOOST_AUTO_TEST_SUITE_END()
