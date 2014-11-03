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
#include <GridDataManager.h>

#include <limits>
#include <SFRData.h>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(GridDataManagerTester_testSuite)

BOOST_AUTO_TEST_CASE(checkConstruction) {
	// begin-user-code
	// Some size values.
	int defaultSize = std::numeric_limits<int>::max();
	int size = 42;
	int location = -1;

	// A string to use.
	std::string name = "Sir Digby Chicken Caesar";
	std::string nullString;
	std::vector<int> locations;

	std::shared_ptr<SFRComponent> defaultProvider = std::make_shared<SFRComponent>();
	std::shared_ptr<SFRComponent> nullProvider;

	/* ---- Test the manager with a valid size. ---- */
	// Initialize the manager.
	GridDataManager manager(size);

	// Test the lower bound on the list of positions.
	for (location = -10; location < 0; location++) {
		BOOST_REQUIRE_EQUAL(false, manager.addComponent(name, location));
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
		BOOST_REQUIRE(locations == manager.getComponentLocations(name));
		BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));
	}

	// Test the possible positions.
	for (location = 0; location < size; location++) {
		locations.push_back(location);

		BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));
		BOOST_REQUIRE_EQUAL(true, manager.addComponent(name, location));
		BOOST_REQUIRE_EQUAL(name, manager.getComponentName(location));
		BOOST_REQUIRE(locations == manager.getComponentLocations(name));
		BOOST_REQUIRE(*defaultProvider == *manager.getDataProvider(location));
	}

	// Test the upper bound on the list of positions.
	for (location = size; location < size + 10; location++) {
		BOOST_REQUIRE_EQUAL(false, manager.addComponent(name, location));
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
		BOOST_REQUIRE(locations == manager.getComponentLocations(name));
		BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));
	}
	/* --------------------------------------------- */

	/* ---- Test the manager with an invalid size. ---- */
	// Initialize the manager.
	manager = GridDataManager(0);
	locations.clear();

	// Test the lower bound on the list of positions.
	for (location = -10; location < 0; location++) {
		BOOST_REQUIRE_EQUAL(false, manager.addComponent(name, location));
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
		BOOST_REQUIRE(locations == manager.getComponentLocations(name));
		BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));
	}

	// Test the possible positions.
	for (location = 0; location < size + 10; location++) {
		locations.push_back(location);

		BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));
		BOOST_REQUIRE_EQUAL(true, manager.addComponent(name, location));
		BOOST_REQUIRE_EQUAL(name, manager.getComponentName(location));
		BOOST_REQUIRE(locations == manager.getComponentLocations(name));
		BOOST_REQUIRE(*defaultProvider == *manager.getDataProvider(location));
	}

	// We should not be able to add anything to the largest location.
	location = defaultSize;

	BOOST_REQUIRE_EQUAL(false, manager.addComponent(name, location));
	BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
	BOOST_REQUIRE(locations == manager.getComponentLocations(name));
	BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));

	// We should be able to add to the next largest location, though.
	location--;
	locations.push_back(defaultSize - 1);

	BOOST_REQUIRE_EQUAL(true, manager.addComponent(name, location));
	BOOST_REQUIRE_EQUAL(name, manager.getComponentName(location));
	BOOST_REQUIRE(locations == manager.getComponentLocations(name));
	BOOST_REQUIRE(*defaultProvider == *manager.getDataProvider(location));
	/* --------------------------------------------- */

	return;
	// end-user-code
}
BOOST_AUTO_TEST_CASE(checkComponentAddRem) {
	// begin-user-code
	// Some size values.
	int size = 42;
	int location;

	// A GridDataManager to test.
	GridDataManager manager(size);

	// Some components to test adding/removing.
	std::string component1 = "Arthur";
	std::string component2 = "Ford Prefect";
	std::string component3 = "Marvin";
	std::string nullString;
	std::vector<int> locations;

	std::shared_ptr<SFRData> data1 = std::make_shared<SFRData>("one");
	std::shared_ptr<SFRData> data2 = std::make_shared<SFRData>("two");
	std::shared_ptr<SFRData> data3 = std::make_shared<SFRData>("three");
	std::shared_ptr<SFRComponent> provider = std::make_shared<SFRComponent>();
	provider->addData(data1, 0.0);
	provider->addData(data1, 1.0);
	provider->addData(data2, 0.0);
	provider->addData(data3, 0.0);
	std::shared_ptr<SFRComponent> defaultProvider = std::make_shared<SFRComponent>();
	std::shared_ptr<SFRComponent> nullProvider;
	std::shared_ptr<SFRComponent> managedProvider;

	/* ---- Test adding a Component. ---- */
	// We'll add component1.

	// Test the lower bound on the list of positions.
	for (location = -10; location < 0; location++) {
		BOOST_REQUIRE_EQUAL(false, manager.addComponent(component1, location));
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
		BOOST_REQUIRE(locations == manager.getComponentLocations(component1));
		BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));
	}

	// Test the possible positions.
	for (location = 0; location < size; location++) {
		locations.push_back(location);

		BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));
		BOOST_REQUIRE_EQUAL(true, manager.addComponent(component1, location));
		BOOST_REQUIRE_EQUAL(component1, manager.getComponentName(location));
		BOOST_REQUIRE(locations == manager.getComponentLocations(component1));

		managedProvider = manager.getDataProvider(location);
		BOOST_REQUIRE(*defaultProvider == *managedProvider);
		managedProvider->addData(data1, 0.0);
		managedProvider->addData(data1, 1.0);
		managedProvider->addData(data2, 0.0);
		managedProvider->addData(data3, 0.0);
		BOOST_REQUIRE(*provider == *managedProvider);
	}

	// Test the upper bound on the list of positions.
	for (location = size; location < size + 10; location++) {
		BOOST_REQUIRE_EQUAL(false, manager.addComponent(component1, location));
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
		BOOST_REQUIRE(locations == manager.getComponentLocations(component1));
		BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));
	}
	/* ---------------------------------- */

	/* ---- Test adding the same Component to the same location. ---- */
	location = 15;

	BOOST_REQUIRE_EQUAL(false, manager.addComponent(component1, location));

	// Nothing should change!
	BOOST_REQUIRE_EQUAL(component1, manager.getComponentName(location));
	BOOST_REQUIRE(locations == manager.getComponentLocations(component1));

	managedProvider = manager.getDataProvider(location);
	BOOST_REQUIRE(*provider == *managedProvider);
	/* -------------------------------------------------------------- */

	/* ---- Test removing a Component. ---- */
	// Test the lower bound on the list of positions.
	for (location = -10; location < 0; location++) {
		BOOST_REQUIRE_EQUAL(false, manager.removeComponent(location));
		BOOST_REQUIRE(locations == manager.getComponentLocations(component1));
	}

	// Test the upper bound on the list of positions.
	for (location = size; location < size + 10; location++) {
		BOOST_REQUIRE_EQUAL(false, manager.removeComponent(location));
		BOOST_REQUIRE(locations == manager.getComponentLocations(component1));
	}

	// Try removing some from the inside.
	int newSize = size;
	for (location = 10; location < 20; location++) {
		locations.erase(locations.begin() + 10);

		// We should be able to remove the Component.
		BOOST_REQUIRE_EQUAL(true, manager.removeComponent(location));

		// Make sure there is no data provider anymore.
		BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));

		// Make sure there is no Component there.
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));

		// Make sure the List of locations for that Component was updated.
		BOOST_REQUIRE(locations == manager.getComponentLocations(component1));
	}

	// Try removing the Component entirely.
	BOOST_REQUIRE_EQUAL(true, manager.removeComponent(component1));

	// Try removing the Component entirely again.
	BOOST_REQUIRE_EQUAL(false, manager.removeComponent(component1));

	// Make sure no location has that Component.
	locations.clear();
	BOOST_REQUIRE(locations == manager.getComponentLocations(component1));
	for (location = 0; location < size; location++) {
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
		BOOST_REQUIRE(nullProvider == manager.getDataProvider(location));
	}

	// Add the component a few times, and remove all of them manually.
	BOOST_REQUIRE_EQUAL(true, manager.addComponent(component1, 0));
	BOOST_REQUIRE_EQUAL(true, manager.addComponent(component1, 1));
	BOOST_REQUIRE_EQUAL(true, manager.removeComponent(0));
	// Can't remove from an empty location!
	BOOST_REQUIRE_EQUAL(false, manager.removeComponent(0));
	BOOST_REQUIRE_EQUAL(true, manager.removeComponent(1));
	// Can't remove from an empty location!
	BOOST_REQUIRE_EQUAL(false, manager.removeComponent(1));

	// Try removing the component entirely. Since it's already been removed,
	// it should return false.
	BOOST_REQUIRE_EQUAL(false, manager.removeComponent(component1));

	// Make sure no location has that Component.
	for (location = 0; location < size; location++)
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));

	// Make sure the Component has no listed locations.
	BOOST_REQUIRE(manager.getComponentLocations(component1).empty());
	/* ------------------------------------ */

	/* ---- Test overwriting a location. ---- */

	// Add component1 in a few locations.
	for (location = 10; location < 20; location++) {
		BOOST_REQUIRE_EQUAL(true, manager.addComponent(component1, location));

		managedProvider = manager.getDataProvider(location);
		BOOST_REQUIRE(*defaultProvider == *managedProvider);
		managedProvider->addData(data1, 0.0);
		managedProvider->addData(data1, 1.0);
		managedProvider->addData(data2, 0.0);
		managedProvider->addData(data3, 0.0);
	}

	// Verify the addition of component1.
	locations = manager.getComponentLocations(component1);
	BOOST_REQUIRE_EQUAL(10, locations.size());
	for (location = 10; location < 20; location++) {
		BOOST_REQUIRE_EQUAL(component1, manager.getComponentName(location));
		BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), location) != locations.end());

		BOOST_REQUIRE(*provider == *manager.getDataProvider(location));
	}

	// Overwrite the the last half of those positions.
	for (location = 15; location < 25; location++)
		BOOST_REQUIRE_EQUAL(true, manager.addComponent(component2, location));

	// Verify the removal of component1.
	locations = manager.getComponentLocations(component1);
	BOOST_REQUIRE_EQUAL(5, locations.size());
	for (location = 10; location < 15; location++) {
		BOOST_REQUIRE_EQUAL(component1, manager.getComponentName(location));
		BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), location) != locations.end());
	}

	// Verify the addition of component2.
	locations = manager.getComponentLocations(component2);
	BOOST_REQUIRE_EQUAL(10, locations.size());
	for (location = 15; location < 25; location++) {
		BOOST_REQUIRE_EQUAL(component2, manager.getComponentName(location));
		BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), location) != locations.end());

		BOOST_REQUIRE(*defaultProvider == *manager.getDataProvider(location));
	}

	// Overwrite the last half of those positions.
	for (location = 20; location < 25; location++)
		BOOST_REQUIRE_EQUAL(true, manager.addComponent(component3, location));

	// Verify that component1 was not touched.
	locations = manager.getComponentLocations(component1);
	BOOST_REQUIRE_EQUAL(5, locations.size());
	for (location = 10; location < 15; location++) {
		BOOST_REQUIRE_EQUAL(component1, manager.getComponentName(location));
		BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), location) != locations.end());
	}

	// Verify the removal of component2.
	locations = manager.getComponentLocations(component2);
	BOOST_REQUIRE_EQUAL(5, locations.size());
	for (location = 15; location < 20; location++) {
		BOOST_REQUIRE_EQUAL(component2, manager.getComponentName(location));
		BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), location) != locations.end());
	}

	// Verify the addition of component3.
	locations = manager.getComponentLocations(component3);
	BOOST_REQUIRE_EQUAL(5, locations.size());
	for (location = 20; location < 25; location++) {
		BOOST_REQUIRE_EQUAL(component3, manager.getComponentName(location));
		BOOST_REQUIRE_EQUAL(true, std::find(locations.begin(), locations.end(), location) != locations.end());
	}
	/* -------------------------------------- */

	/* ---- Remove some more. ---- */
	// Make sure we know how many locations are in each Component's map.
	BOOST_REQUIRE_EQUAL(5, manager.getComponentLocations(component1).size());
	BOOST_REQUIRE_EQUAL(5, manager.getComponentLocations(component2).size());
	BOOST_REQUIRE_EQUAL(5, manager.getComponentLocations(component3).size());

	// Remove component2 manually.
	for (location = 15; location < 20; location++)
		BOOST_REQUIRE_EQUAL(true, manager.removeComponent(location));

	// Verify removal of component2.
	BOOST_REQUIRE_EQUAL(5, manager.getComponentLocations(component1).size());
	BOOST_REQUIRE(manager.getComponentLocations(component2).empty());
	BOOST_REQUIRE_EQUAL(5, manager.getComponentLocations(component3).size());
	for (location = 0; location < 10; location++)
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
	for (location = 10; location < 15; location++)
		BOOST_REQUIRE_EQUAL(component1, manager.getComponentName(location));
	for (location = 15; location < 20; location++)
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
	for (location = 20; location < 25; location++)
		BOOST_REQUIRE_EQUAL(component3, manager.getComponentName(location));
	for (location = 25; location < size; location++)
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));

	// Remove component3 at once.
	BOOST_REQUIRE_EQUAL(true, manager.removeComponent(component3));
	BOOST_REQUIRE_EQUAL(5, manager.getComponentLocations(component1).size());
	BOOST_REQUIRE(manager.getComponentLocations(component2).empty());
	BOOST_REQUIRE(manager.getComponentLocations(component3).empty());
	for (location = 0; location < 10; location++)
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
	for (location = 10; location < 15; location++)
		BOOST_REQUIRE_EQUAL(component1, manager.getComponentName(location));
	for (location = 15; location < size; location++)
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));

	// Remove component1.
	BOOST_REQUIRE_EQUAL(true, manager.removeComponent(component1));
	BOOST_REQUIRE(manager.getComponentLocations(component1).empty());
	BOOST_REQUIRE(manager.getComponentLocations(component2).empty());
	BOOST_REQUIRE(manager.getComponentLocations(component3).empty());
	for (location = 0; location < size; location++)
		BOOST_REQUIRE_EQUAL(nullString, manager.getComponentName(location));
	/* --------------------------- */
	return;
	// end-user-code
}
BOOST_AUTO_TEST_CASE(checkEquality) {
	// begin-user-code

	int size = 257;

	// Initialize objects for testing.
	GridDataManager object(size);
	GridDataManager equalObject(size);
	GridDataManager unequalObject(size);

	// Set up the object and equalObject.
	std::string component ="Marvin";
	std::shared_ptr<SFRData> data = std::make_shared<SFRData>("Fictional Character");
	data->setValue(42.0);

	object.addComponent("Ford", 50);
	object.addComponent("Zaphod", 3);
	object.addComponent(component, 84);
	object.addComponent(component, 37);
	object.getDataProvider(3)->addData(data, 0.0);
	object.getDataProvider(37)->addData(data, 0.0);

	equalObject.addComponent("Ford", 50);
	equalObject.addComponent("Zaphod", 3);
	equalObject.addComponent(component, 84);
	equalObject.addComponent(component, 37);
	equalObject.getDataProvider(3)->addData(data, 0.0);
	equalObject.getDataProvider(37)->addData(data, 0.0);

	// Set up the unequalObject.
	unequalObject.addComponent("Ford", 50);
	unequalObject.addComponent("Zaphod", 3);
	unequalObject.addComponent(component, 84);
	unequalObject.addComponent(component, 37);
	unequalObject.getDataProvider(3)->addData(data, 0.0);
	unequalObject.getDataProvider(50)->addData(data, 0.0); // Different!

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
	int size = 720;

	// Initialize objects for testing.
	GridDataManager object(size);
	GridDataManager objectCopy(size);
	std::shared_ptr<GridDataManager> objectClone;

	// Set up the object.
	std::string component = "Marvin";
	std::shared_ptr<SFRData> data = std::make_shared<SFRData>("Fictional Character");
	data->setValue(42.0);

	object.addComponent("Ford", 50);
	object.addComponent("Zaphod", 3);
	object.addComponent(component, 84);
	object.addComponent(component, 37);
	object.getDataProvider(84)->addData(data, 1.0);
	object.getDataProvider(37)->addData(data, 1.0);
	object.getDataProvider(37)->addData(data, 0.0);

	/* ---- Check copying. ---- */
	// Make sure the object is not the same as its copy.
	BOOST_REQUIRE_EQUAL(false, object == objectCopy);

	// Copy the object.
	objectCopy = GridDataManager(object);

	// Make sure the object is now the same as its copy.
	BOOST_REQUIRE_EQUAL(true, object == objectCopy);
	/* ------------------------ */

	/* ---- Check cloning. ---- */
	// Clone the object.
	objectClone = std::dynamic_pointer_cast<GridDataManager>(object.clone());

	// Make sure the object is now the same as its clone.
	BOOST_REQUIRE_EQUAL(true, object == *objectClone);
	/* ------------------------ */

	return;
	// end-user-code
}
BOOST_AUTO_TEST_SUITE_END()
