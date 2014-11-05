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
#include "GridManager.h"

#include <limits>

using namespace ICE_SFReactor;

GridManager::GridManager(int size) {
	// begin-user-code

	// Set the size if it's greater than 0. Otherwise, use the largest int.
	this->size = (size > 0 ? size : std::numeric_limits<int>::max());

	return;
	// end-user-code
}
GridManager::GridManager(const GridManager & otherGM) {
	// begin-user-code

	// Copy over the size variable.
	size = otherGM.size;

	// Assign the contents of the other maps to this GridManager's maps.
	locations = otherGM.locations;
	components = otherGM.components;

	return;
	// end-user-code
}
std::string GridManager::getComponentName(int location) {
	// begin-user-code

	std::string name;

	// Try to find the location in locations.
	std::map<int, std::string>::iterator iter = locations.find(location);

	// If it's in the map, set the name string.
	if (iter != locations.end())
		name = iter->second;

	return name;
	// end-user-code
}
std::vector<int> GridManager::getComponentLocations(std::string name) {
	// begin-user-code

	std::vector<int> locations;

	// Try to find the name in components,
	std::map<std::string, std::set<int> >::iterator iter = components.find(name);

	// If it's in the map, get the component's locations.
	if (iter != components.end()) {
		std::set<int> *locationSet = &iter->second;

		// Loop over the locations and add them to the vector.
		for (std::set<int>::iterator setIter = locationSet->begin();
				setIter != locationSet->end();
				setIter++) {
			locations.push_back(*setIter);
		}
	}

	return locations;
	// end-user-code
}
bool GridManager::addComponent(std::string name, int location) {
	// begin-user-code

	// By default, we did not succeed in adding the Component.
	bool success = false;

	// Check the location parameter.
	if (location >= 0 && location < size) {

		// A temporary pointer for a set of locations.
		std::set<int> *indexes;

		// Get the set of indexes this component occupies.
		std::map<std::string, std::set<int> >::iterator compIter;
		compIter = components.find(name);
		if (compIter == components.end()) {
			// If necessary, insert a new set.
			std::set<int> set;
			compIter = components.insert(std::make_pair(name, set)).first;
		}
		// Set the temporary pointer to the set of indexes.
		indexes = &compIter->second;

		// Add the location to the set of indexes. Insert returns a pair. Its
		// second value is true if the value did not already exist in the set.
		if (indexes->insert(location).second) {


			// If there was a previous component in that location, update it.
			std::map<int, std::string>::iterator locIter;
			locIter = locations.find(location);
			if (locIter != locations.end()) {
				// Get the name of the previous component.
				std::string oldName = locIter->second;

				// Get the set of indexes for that component.
				indexes = &components[oldName];
				indexes->erase(location);

				// If it no longer resides in any locations, remove it from the
				// map of components.
				if (indexes->empty())
					components.erase(oldName);
			}

			// Update the map of locations.
			locations[location] = name;

			// We have successfully added the component to the location.
			success = true;
		}
	}

	return success;
	// end-user-code
}
bool GridManager::removeComponent(int location) {
	// begin-user-code

	// By default, we did not succeed in removing the Component location.
	bool success = false;

	// Check the location parameter.
	if (location >= 0 && location < size) {

		// If there was a previous component in that location, update it.
		std::map<int, std::string>::iterator locIter = locations.find(location);
		if (locIter != locations.end()) {
			// Get the name of the previous component.
			std::string oldName = locIter->second;

			// Get the set of indexes for that component.
			std::set<int> *indexes = &components[oldName];
			indexes->erase(location);

			// If it no longer resides in any locations, remove it from the
			// map of components.
			if (indexes->empty())
				components.erase(oldName);

			// Remove the location from the map locations.
			locations.erase(locIter);

			// We have successfully removed a component from the location.
			success = true;
		}
	}

	return success;
	// end-user-code
}
bool GridManager::removeComponent(std::string name) {
	// begin-user-code

	// By default, we did not succeed in removing the Component.
	bool success = false;

	// If there is a component with the name, remove it.
	std::map<std::string, std::set<int> >::iterator compIter;
	compIter = components.find(name);
	if (compIter != components.end()) {

		// Get the set of indexes for that component.
		std::set<int> *indexes = &compIter->second;

		// For each index in the set, remove it from the map locations.
		for (std::set<int>::iterator setIter = indexes->begin();
				setIter != indexes->end();
				setIter++)
			locations.erase(*setIter);
		
		// Remove the component from the map components.
		components.erase(compIter);

		// We have successfully removed a component.
		success = true;
	}

	return success;
	// end-user-code
}
std::shared_ptr<IGridManager> GridManager::clone() {
		// begin-user-code

		// Initialize a new GridManager.
		std::shared_ptr<GridManager> manager = std::make_shared<GridManager>(*this);

		// Return the newly instantiated object.
		return std::dynamic_pointer_cast<IGridManager>(manager);
		// end-user-code
}
bool GridManager::operator==(const GridManager & otherGM) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare all member variables. Note that comparing the maps is
	// straightforward because they do not use pointers.
	equals = (size == otherGM.size
			&& locations == otherGM.locations
			&& components == otherGM.components);
		
	return equals;
	// end-user-code
}
