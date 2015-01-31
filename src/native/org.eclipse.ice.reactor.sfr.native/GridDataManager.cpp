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
#include "GridDataManager.h"

using namespace ICE_SFReactor;

GridDataManager::GridDataManager(int size) : GridManager(size) {
	// begin-user-code

	return;
	// end-user-code
}
GridDataManager::GridDataManager(const GridDataManager & otherGM) : GridManager(otherGM) {
	// begin-user-code

	// Assign the contents of the other map to this GridDataManager's map.
	dataProviders = otherGM.dataProviders;

	return;
	// end-user-code
}
std::shared_ptr<SFRComponent> GridDataManager::getDataProvider(int location) {
	// begin-user-code

	// By default, we return a null pointer.
	std::shared_ptr<SFRComponent> provider;

	// Try to find the provider in the location.
	if (dataProviders.find(location) != dataProviders.end())
		provider = dataProviders[location];

	return provider;
	// end-user-code
}
bool GridDataManager::addComponent(std::string name, int location) {
	// begin-user-code

	// By default, we did not succeed in adding the Component.
	bool success = GridManager::addComponent(name, location);

	// If possible, add a new IDataProvider to the location.
	if (success)
		dataProviders[location] = std::make_shared<SFRComponent>();

	return success;
	// end-user-code
}
bool GridDataManager::removeComponent(int location) {
	// begin-user-code

	// By default, we did not succeed in removing the Component location.
	bool success = GridManager::removeComponent(location);

	// If possible, remove the associated IDataProvider from the map.
	if (success)
		dataProviders.erase(location);

	return success;
	// end-user-code
}
bool GridDataManager::removeComponent(std::string name) {
	// begin-user-code

	// Get the locations for this component.
	std::vector<int> locations = this->getComponentLocations(name);

	// By default, we did not succeed in removing the Component.
	bool success = GridManager::removeComponent(name);

	// If possible, remove all of the associated IDataProviders from the map.
	if (success)
		for (int i = 0; i < locations.size(); i++)
			dataProviders.erase(locations[i]);

	return success;
	// end-user-code
}
std::shared_ptr<IGridManager> GridDataManager::clone() {
		// begin-user-code

		// Initialize a new GridDataManager.
		std::shared_ptr<GridDataManager> manager = std::make_shared<GridDataManager>(*this);

		// Return the newly instantiated object.
		return std::dynamic_pointer_cast<IGridManager>(manager);
		// end-user-code
}
bool GridDataManager::operator==(const GridDataManager & otherGM) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare all member variables.
	equals = (this->GridManager::operator==(otherGM)
			&& dataProviders.size() == otherGM.dataProviders.size());

	// If the super.equals() method holds, then the locations *should* be the
	// same. So, we only need to compare the data provider values.

	// Compare the data providers.
	std::map<int, std::shared_ptr<SFRComponent>>::iterator iter;
	std::map<int, std::shared_ptr<SFRComponent>>::const_iterator otherIter;
	for (iter = dataProviders.begin(), otherIter = otherGM.dataProviders.begin();
			equals && iter != dataProviders.end();
			iter++, otherIter++) {

		// Get the IDataProviders (they are actually SFRComponents, because
		// there is no basic IDataProvider implementation.
		std::shared_ptr<SFRComponent> comp = iter->second;
		std::shared_ptr<SFRComponent> otherComp = otherIter->second;

		equals = (*comp == *otherComp);
	}

	return equals;
	// end-user-code
}
