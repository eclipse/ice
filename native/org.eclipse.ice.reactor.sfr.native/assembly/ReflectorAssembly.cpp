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
#include "ReflectorAssembly.h"

using namespace ICE_SFReactor;

ReflectorAssembly::ReflectorAssembly(int size) : SFRAssembly("SFR Reflector Assembly 1", Reflector, size) {
	// begin-user-code

	// Set the default description, and ID.
	setDescription("SFR Reflector Assembly 1's Description");
	setId(1);

	// Initialize rodPitch.
	rodPitch = 1.0;

	// Initialize the rod manager.
	rodManager = std::make_shared<GridDataManager>(getSize() * getSize());

	return;
	// end-user-code
}
ReflectorAssembly::ReflectorAssembly(std::string name, int size): SFRAssembly("SFR Reflector Assembly 1", Reflector, size) {
	// begin-user-code

	// Set the default description, and ID.
	setDescription("SFR Reflector Assembly 1's Description");
	setId(1);

	// Initialize rodPitch.
	rodPitch = 1.0;

	// Initialize the rod manager.
	rodManager = std::make_shared<GridDataManager>(getSize() * getSize());

	// Set the name.
	setName(name);

	return;
	// end-user-code
}
ReflectorAssembly::ReflectorAssembly(const ReflectorAssembly & otherReflector) : SFRAssembly(otherReflector) {
	// begin-user-code

	// Copy the local values.
	rodPitch = otherReflector.rodPitch;

	// Copy the other assembly's GridDataManager.
	rodManager = std::make_shared<GridDataManager>(*otherReflector.rodManager);

	return;
	// end-user-code
}
void ReflectorAssembly::setRodPitch(double rodPitch) {
	// begin-user-code

	// Only set the rod pitch if it is 0 or greater.
	if (rodPitch >= 0.0)
		this->rodPitch = rodPitch;

	return;
	// end-user-code
}
double ReflectorAssembly::getRodPitch() {
	// begin-user-code
	return rodPitch;
	// end-user-code
}
bool ReflectorAssembly::addRod(std::shared_ptr<SFRRod> rod) {
	// begin-user-code

	// By default, we did not succeed in adding the Component.
	bool success = false;

	// Make sure the rod is not a null pointer.
	if (rod) {

		// Get the rod's name.
		std::string name = rod->getName();

		// Check the parameters. Also make sure that the rod does not already
		// exist in the collection of Components.
		if (!getComponent(name)) {

			// Add the new Component to this Composite.
			SFRComposite::addComponent(rod);

			// See if the rod was successfully added.
			std::shared_ptr<SFRRod> addedRod = getRodByName(name);
			success = (*rod == *addedRod);
		}
	}

	return success;
	// end-user-code
}
bool ReflectorAssembly::removeRod(std::string name) {
	// begin-user-code

	// By default, we did not succeed in removing the Component.
	bool success = false;

	// If there is a rod with that name, remove it.
	if (getRodByName(name)) {
		// Remove the Component from this Composite.
		SFRComposite::removeComponent(name);

		// Remove it from the GridDataManager.
		rodManager->removeComponent(name);

		success = true;
	}

	return success;
	// end-user-code
}

bool ReflectorAssembly::removeRodFromLocation(int row, int column) {
	// begin-user-code

	// By default, we did not succeed in removing the Component from the
	// location.
	bool success = false;

	// Check the parameters.
	int size = getSize();
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// Try removing the location from the GridDataManager.
		success = rodManager->removeComponent(row * size + column);
	}

	return success;
	// end-user-code
}
std::vector<std::string> ReflectorAssembly::getRodNames() {
	// begin-user-code
	return getComponentNames();
	// end-user-code
}
std::shared_ptr<SFRRod> ReflectorAssembly::getRodByName(std::string name) {
	// begin-user-code
	return std::dynamic_pointer_cast<SFRRod>(getComponent(name));
	// end-user-code
}
std::shared_ptr<SFRRod> ReflectorAssembly::getRodByLocation(int row, int column) {
	// begin-user-code

	// Initialize the default return value.
	std::shared_ptr<SFRRod> rod;

	// Check the parameters.
	int size = getSize();
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// Get the name of the Component from the GridDataManager.
		std::string name = rodManager->getComponentName(row * size + column);

		// Get the Component from this Composite.
		rod = getRodByName(name);
	}

	return rod;
	// end-user-code
}
std::vector<int> ReflectorAssembly::getRodLocations(std::string name) {
	// begin-user-code

	// Get the component locations from the GridDataManager.
	return rodManager->getComponentLocations(name);
	// end-user-code
}
void ReflectorAssembly::addComponent(std::shared_ptr<Component> child) {
	// begin-user-code
	return;
	// end-user-code
}
void ReflectorAssembly::removeComponent(int childId) {
	// begin-user-code
	return;
	// end-user-code
}
void ReflectorAssembly::removeComponent(std::string name) {
	// begin-user-code
	return;
	// end-user-code
}
bool ReflectorAssembly::setRodLocation(std::string name, int row, int column) {
	// begin-user-code

	// By default, we did not succeed in adding the Component.
	bool success = false;

	// Check the parameters.
	int size = getSize();
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// If the Component exists, add it to the GridDataManager location.
		if (getComponent(name))
			success = rodManager->addComponent(name, row * size + column);
	}

	return success;
	// end-user-code
}
int ReflectorAssembly::getNumberOfRods() {
	// begin-user-code
	return getNumberOfComponents();
	// end-user-code
}
std::shared_ptr<SFRComponent> ReflectorAssembly::getDataProviderByLocation(int row, int column) {
	// begin-user-code

	// Initialize the default return value.
	std::shared_ptr<SFRComponent> provider;

	// Check the parameters.
	int size = getSize();
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// Get the IDataProvider from the GridDataManager.
		provider = rodManager->getDataProvider(row * size + column);
	}

	return provider;
	// end-user-code
}
std::shared_ptr<Identifiable> ReflectorAssembly::clone() {
	// begin-user-code

	// Initialize a new ReflectorAssembly.
	std::shared_ptr<ReflectorAssembly> assembly = std::make_shared<ReflectorAssembly>(*this);

	// Return the newly instantiated object.
	return std::dynamic_pointer_cast<Identifiable>(assembly);
	// end-user-code
}
bool ReflectorAssembly::operator==(const ReflectorAssembly & otherReflector) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare the values between the two objects.
	equals = (SFRAssembly::operator==(otherReflector)
			&& rodPitch == otherReflector.rodPitch
			&& *rodManager == *otherReflector.rodManager);

	return equals;
	// end-user-code
}
bool ReflectorAssembly::operator==(const SFRComponent & component) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare the values between the two objects.
	equals = (typeid(*this) == typeid(component)
			&& *this == dynamic_cast<const ReflectorAssembly &>(component));

	return equals;
	// end-user-code
}
