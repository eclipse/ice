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
#include "PinAssembly.h"

using namespace ICE_SFReactor;

PinAssembly::PinAssembly(int size) :
		SFRAssembly("SFR Pin Assembly 1", Fuel, size) {
	// begin-user-code

	// Set the default description  and ID.
	setDescription("SFR Pin Assembly 1's Description");
	setId(1);

	// Set the default pin type.
	pinType = PinType::InnerFuel;

	// Initialize pinPitch, and inner duct.
	pinPitch = 1.0;
	innerDuctFlatToFlat = 0.0;
	innerDuctThickness = 0.0;
	// Inner duct values are assumed to be 0, except for the case of control
	// assemblies. Since the nullary constructor sets the pin's type as InnerFuel,
	// the default inner duct is non-existent.

	// Initialize the pin manager.
	pinManager = std::make_shared<GridDataManager>(getSize() * getSize());

	return;
	// end-user-code
}
PinAssembly::PinAssembly(std::string name, int size, PinType pinType) :
		SFRAssembly("SFR Pin Assembly 1", Fuel, size) {
	// begin-user-code

	// Set the default description  and ID.
	setDescription("SFR Pin Assembly 1's Description");
	setId(1);

	// Initialize pinPitch.
	pinPitch = 1.0;

	// Initialize the pin manager.
	pinManager = std::make_shared<GridDataManager>(getSize() * getSize());

	// Set the name.
	setName(name);

	// Set the pin type of this assembly.
	this->pinType = pinType;

	// Initalize the inner duct values; assumed to be 0 unless a control assembly
	if (pinType == PinType::PrimaryControl || pinType == PinType::SecondaryControl) {
		innerDuctFlatToFlat = 0.75;
		innerDuctThickness = 0.05;
	}
	else {
		innerDuctFlatToFlat = 0.0;
		innerDuctThickness = 0.0;
	}

	// Set the assembly type to Control, Shield or Test if necessary (is already
	// Fuel by default)
	switch (this->pinType) {
	case PrimaryControl: case SecondaryControl:
		assemblyType = AssemblyType::Control;
		break;
	case Shielding:
		assemblyType = AssemblyType::Shield;
		break;
	case MaterialTest: case FuelTest:
		assemblyType = AssemblyType::Test;
		break;
	case InnerFuel: case OuterFuel: case BlanketFuel: default:
		break;
	}

	return;
	// end-user-code
}
PinAssembly::PinAssembly(const PinAssembly & otherPinAssembly) :
		SFRAssembly(otherPinAssembly) {
	// begin-user-code

	// Copy the local values.
	pinPitch = otherPinAssembly.pinPitch;
	pinType = otherPinAssembly.pinType;
	innerDuctFlatToFlat = otherPinAssembly.innerDuctFlatToFlat;
	innerDuctThickness = otherPinAssembly.innerDuctThickness;

	// Copy the other assembly's GridDataManager.
	pinManager = std::make_shared<GridDataManager>(*otherPinAssembly.pinManager);

	return;
	// end-user-code
}
void PinAssembly::setPinPitch(double pinPitch) {
	// begin-user-code

	// Only set the pin pitch if it is 0 or greater.
	if (pinPitch >= 0.0)
		this->pinPitch = pinPitch;

	return;
	// end-user-code
}
double PinAssembly::getPinPitch() {
	// begin-user-code
	return pinPitch;
	// end-user-code
}
void PinAssembly::setInnerDuctFlatToFlat(double flatToFlat){
	// begin-user-code

	// Only allow non-negative values
	if (flatToFlat >= 0.0)
		this->innerDuctFlatToFlat = flatToFlat;
	return;

	// end-user-code
}
double PinAssembly::getInnerDuctFlatToFlat() {
	// begin-user-code
	return innerDuctFlatToFlat;
	// end-user-code
}
void PinAssembly::setInnerDuctThickness(double innerDuctThickness) {
	// begin-user-code

	// Only allow non-negative values
	if (innerDuctThickness >= 0.0)
		this->innerDuctThickness = innerDuctThickness;
	return;

	// end-user-code
}
double PinAssembly::getInnerDuctThickness() {
	// begin-user-code
	return innerDuctThickness;
	// end-user-code
}
PinType PinAssembly::getPinType() {
	// begin-user-code
	return pinType;
	// end-user-code
}
bool PinAssembly::addPin(std::shared_ptr<SFRPin> pin) {
	// begin-user-code

	// By default, we did not succeed in adding the Component.
	bool success = false;

	// Make sure the pin is not a null pointer.
	if (pin) {

		// Get the pin's name.
		std::string name = pin->getName();

		// Check the parameters. Also make sure that the pin does not already
		// exist in the collection of Components.
		if (!getComponent(name)) {

			// Add the new Component to this Composite.
			SFRComposite::addComponent(pin);

			// See if the pin was successfully added.
			std::shared_ptr<SFRPin> addedPin = getPinByName(name);
			success = (*pin == *addedPin);
		}
	}

	return success;
	// end-user-code
}
bool PinAssembly::removePin(std::string name) {
	// begin-user-code

	// By default, we did not succeed in removing the Component.
	bool success = false;

	// If there is a pin with that name, remove it.
	if (getPinByName(name)) {
		// Remove the Component from this Composite.
		SFRComposite::removeComponent(name);

		// Remove it from the GridDataManager.
		pinManager->removeComponent(name);

		success = true;
	}

	return success;
	// end-user-code
}
bool PinAssembly::removePinFromLocation(int row, int column) {
	// begin-user-code

	// By default, we did not succeed in removing the Component from the
	// location.
	bool success = false;

	// Check the parameters.
	int size = getSize();
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// Try removing the location from the GridDataManager.
		success = pinManager->removeComponent(row * size + column);
	}

	return success;
	// end-user-code
}
std::vector<std::string> PinAssembly::getPinNames() {
	// begin-user-code
	return getComponentNames();
	// end-user-code
}
std::shared_ptr<SFRPin> PinAssembly::getPinByName(std::string name) {
	// begin-user-code
	return std::dynamic_pointer_cast<SFRPin>(getComponent(name));
	// end-user-code
}
std::shared_ptr<SFRPin> PinAssembly::getPinByLocation(int row, int column) {
	// begin-user-code

	// Initialize the default return value.
	std::shared_ptr<SFRPin> pin;

	// Check the parameters.
	int size = getSize();
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// Get the name of the Component from the GridDataManager.
		std::string name = pinManager->getComponentName(row * size + column);

		// Get the Component from this Composite.
		pin = getPinByName(name);
	}

	return pin;
	// end-user-code
}
std::vector<int> PinAssembly::getPinLocations(std::string name) {
	// begin-user-code

	// Get the component locations from the GridDataManager.
	return pinManager->getComponentLocations(name);
	// end-user-code
}
std::shared_ptr<Identifiable> PinAssembly::clone() {
	// begin-user-code

	// Initialize a new PinAssembly.
	std::shared_ptr<PinAssembly> assembly = std::make_shared<PinAssembly>(*this);

	// Return the newly instantiated object.
	return std::dynamic_pointer_cast<Identifiable>(assembly);

	// end-user-code
}
bool PinAssembly::operator==(const PinAssembly & otherPinAssembly) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare the values between the two objects.
	equals = (SFRAssembly::operator==(otherPinAssembly)
			&& pinPitch == otherPinAssembly.pinPitch
			&& pinType == otherPinAssembly.pinType
			&& innerDuctFlatToFlat == otherPinAssembly.innerDuctFlatToFlat
			&& innerDuctThickness == otherPinAssembly.innerDuctThickness
			&& *pinManager == *otherPinAssembly.pinManager);

	return equals;
	// end-user-code
}
bool PinAssembly::operator==(const SFRComponent & component) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare the values between the two objects.
	equals = (typeid(*this) == typeid(component)
			&& *this == dynamic_cast<const PinAssembly &>(component));

	return equals;
	// end-user-code
}
bool PinAssembly::setPinLocation(std::string name, int row, int column) {
	// begin-user-code

	// By default, we did not succeed in adding the Component.
	bool success = false;

	// Check the parameters.
	int size = getSize();
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// If the Component exists, add it to the GridDataManager location.
		if (getComponent(name))
			success = pinManager->addComponent(name, row * size + column);
	}

	return success;
	// end-user-code
}
int PinAssembly::getNumberOfPins() {
	// begin-user-code
	return getNumberOfComponents();
	// end-user-code
}
std::shared_ptr<SFRComponent> PinAssembly::getDataProviderByLocation(int row, int column) {
	// begin-user-code

	// Initialize the default return value.
	std::shared_ptr<SFRComponent> provider;

	// Check the parameters.
	int size = getSize();
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// Get the IDataProvider from the GridDataManager.
		provider = pinManager->getDataProvider(row * size + column);
	}

	return provider;
	// end-user-code
}
void PinAssembly::addComponent(std::shared_ptr<Component> child) {
	// begin-user-code
	return;
	// end-user-code
}
void PinAssembly::removeComponent(int childId) {
	// begin-user-code
	return;
	// end-user-code
}
void PinAssembly::removeComponent(std::string name) {
	// begin-user-code
	return;
	// end-user-code
}
