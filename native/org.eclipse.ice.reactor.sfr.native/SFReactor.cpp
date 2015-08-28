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
#include "SFReactor.h"

using namespace ICE_SFReactor;

SFReactor::SFReactor(int size) {
	// begin-user-code

	// Store the size, but it must be a positive number.
	this->size = (size > 0 ? size : 1);

	// Set the default name, description, and ID.
	setName("SFReactor 1");
	setDescription("SFReactor 1's Description");
	setId(1);

	// Set the default latticePitch and outerFlatToFlat. This setting means
	// each assembly in the core is 1 unit wide and touches its six adjacent
	// assemblies.
	latticePitch = 1.0;
	outerFlatToFlat = 1.0;

	/* ---- Set up basic Composites for each AssemblyType. ---- */

	// We need a vector of names based on the AssemblyType enum values.
	// (C++ basic enum limitations...)
	std::vector<std::string> names =
		{"Fuel Assembly", "Control Assembly", "Reflector Assembly",
				"Shield Assembly", "Test Assembly"};

	for (int i = 0; i < names.size(); i++) {
		// Create a new Composite for the assembly type.
		std::shared_ptr<SFRComposite> composite = std::make_shared<SFRComposite>();
		composite->setName(names[i] + " Composite");
		composite->setDescription("A Composite that contains many " + names[i] + " Components.");
		composite->setId(i + 1);

		// Store the Composite in the SFReactor's assembly Composite Map and
		// the Component Map (inherited from SFRComposite).
		assemblyComposites.push_back(composite);
		SFRComposite::addComponent(composite);

		// Create a new GridManager for this assembly type.
		assemblyManagers.push_back(std::make_shared<GridManager>(this->size * this->size));
	}
	/* -------------------------------------------------------- */

	/* ---- Customize individual Composites here as necessary. ---- */
	/* ------------------------------------------------------------ */

	return;
	// end-user-code
}
SFReactor::SFReactor(const SFReactor & otherSFReactor) : SFRComposite(otherSFReactor) {
	// begin-user-code

	// Copy the super's values.
	// super.copy(otherSFReactor);
	// See the constructor initialization list for this function.

	// Copy the local values.
	size = otherSFReactor.size;
	latticePitch = otherSFReactor.latticePitch;
	outerFlatToFlat = otherSFReactor.outerFlatToFlat;

	// We need to copy the assemblies and GridManagers from the other reactor.
	assemblyComposites.clear();
	assemblyManagers.clear();
	for (int i = 0; i < otherSFReactor.assemblyComposites.size(); i++) {
		// Clone the corresponding Composite and Manager from the other reactor.
		std::shared_ptr<SFRComposite> composite = std::dynamic_pointer_cast<SFRComposite>(otherSFReactor.assemblyComposites[i]->clone());
		assemblyComposites.push_back(composite);
		std::shared_ptr<GridManager> manager = std::dynamic_pointer_cast<GridManager>(otherSFReactor.assemblyManagers[i]->clone());
		assemblyManagers.push_back(manager);
	}

	return;
	// end-user-code
}
int SFReactor::getSize() {
	// begin-user-code
	return size;
	// end-user-code
}
void SFReactor::setLatticePitch(double latticePitch) {
	// begin-user-code

	// Only allow positive lattice pitch.
	if (latticePitch > 0.0)
		this->latticePitch = latticePitch;

	return;
	// end-user-code
}
double SFReactor::getLatticePitch() {
	// begin-user-code
	return latticePitch;
	// end-user-code
}
void SFReactor::setOuterFlatToFlat(double outerFlatToFlat) {
	// begin-user-code

	// Only allow outer flat-to-flat.
	if (outerFlatToFlat > 0.0)
		this->outerFlatToFlat = outerFlatToFlat;

	return;
	// end-user-code
}
double SFReactor::getOuterFlatToFlat() {
	// begin-user-code
	return outerFlatToFlat;
	// end-user-code
}
bool SFReactor::addAssembly(AssemblyType type, std::shared_ptr<SFRComposite> assembly) {
	// begin-user-code

	// Set the default initial status.
	bool success = false;

	// Make sure the assembly is not a null pointer.
	if (assembly) {

		// Get the Composite that contains all assemblies of the type.
		std::shared_ptr<SFRComposite> composite = assemblyComposites[type];

		// Get the assembly's name.
		std::string name = assembly->getName();

		// Make sure an assembly with that name does not already exist.
		if (!(composite->getComponent(name))) {
			// Add the new assembly to the Composite.
			composite->addComponent(assembly);

			// See if the assembly was successfully added.
			success = (*assembly == *std::dynamic_pointer_cast<SFRComposite>(composite->getComponent(name)));
		}
	}

	return success;
	// end-user-code
}
bool SFReactor::removeAssembly(AssemblyType type, std::string name) {
	// begin-user-code

	// Set the default initial status.
	bool success = false;

	// Get the Composite that contains all assemblies of the type.
	std::shared_ptr<SFRComposite> composite = assemblyComposites[type];

	// Get the Component in question from the appropriate Composite.
	std::shared_ptr<SFRComponent> component = composite->getComponent(name);

	// Try to remove the Component.
	if (component) {
		// Remove it from the Composite.
		composite->removeComponent(name);

		// Remove it from the appropriate GridManager.
		assemblyManagers[type]->removeComponent(name);

		success = true;
	}

	return success;
	// end-user-code
}
bool SFReactor::removeAssemblyFromLocation(AssemblyType type, int row, int column) {
	// begin-user-code

	// Set the default initial status.
	bool success = false;

	// Check the parameters. If they are valid, remove the Component from
	// the specified location.
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// Try removing the location from the corresponding GridManager.
		success = assemblyManagers[type]->removeComponent(row * size + column);
	}

	return success;
	// end-user-code
}
bool SFReactor::setAssemblyLocation(AssemblyType type, std::string name,
		int row, int column) {
	// begin-user-code

	bool success = false;

	// Check the parameters.
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// If the component exists, add it to the appropriate GridManager.
		if (assemblyComposites[type]->getComponent(name)) {

			// Try adding the Component to the corresponding GridManager.
			success = assemblyManagers[type]->addComponent(name,	row * size + column);
		}
	}
	return success;
	// end-user-code
}
int SFReactor::getNumberOfAssemblies(AssemblyType type) {
	// begin-user-code

	// Get the number of Components from the appropriate Composite.
	return assemblyComposites[type]->getNumberOfComponents();
	// end-user-code
}
std::vector<std::string> SFReactor::getAssemblyNames(AssemblyType type) {
	// begin-user-code

	// Get the Component names from the appropriate Composite.
	return assemblyComposites[type]->getComponentNames();
	// end-user-code
}
std::shared_ptr<SFRComponent> SFReactor::getAssemblyByName(AssemblyType type,
		std::string name) {
	// begin-user-code

	// Fetch the Component from the appropriate Composite.
	return assemblyComposites[type]->getComponent(name);
	// end-user-code
}
std::shared_ptr<SFRComponent> SFReactor::getAssemblyByLocation(AssemblyType type, int row,
		int column) {
	// begin-user-code

	// Set the default return value.
	std::shared_ptr<SFRComponent> component;

	// Check the parameters. If they are valid, fetch the Component from the
	// specified location.
	if (row >= 0 && row < size && column >= 0 && column < size) {

		// Get the name of the Component from the appropriate GridManager.
		std::string name = assemblyManagers[type]->getComponentName(row * size + column);

		// Get the Component from the appropriate Composite.
		component = assemblyComposites[type]->getComponent(name);
	}

	return component;
	// end-user-code
}
std::vector<int> SFReactor::getAssemblyLocations(AssemblyType type, std::string name) {
	// begin-user-code

	// Fetch the locations from the appropriate GridManager.
	return assemblyManagers[type]->getComponentLocations(name);
	// end-user-code
}
void SFReactor::addComponent(std::shared_ptr<Component> child) {
	// begin-user-code
	return;
	// end-user-code
}
void SFReactor::removeComponent(int childId) {
	// begin-user-code
	return;
	// end-user-code
}
void SFReactor::removeComponent(std::string name) {
	// begin-user-code
	return;
	// end-user-code
}
std::shared_ptr<Identifiable> SFReactor::clone() {
	// begin-user-code

	// Initialize a new SFReactor.
	std::shared_ptr<SFReactor> reactor = std::make_shared<SFReactor>(*this);

	// Return the newly instantiated object.
	return std::dynamic_pointer_cast<Identifiable>(reactor);
	// end-user-code

}
bool SFReactor::operator==(const SFReactor & otherSFReactor) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare all member variables.
	equals = (SFRComposite::operator==(otherSFReactor)
			&& size == otherSFReactor.size
			&& latticePitch == otherSFReactor.latticePitch
			&& outerFlatToFlat == otherSFReactor.outerFlatToFlat);

	// Compare the GridManagers.
	for (int i = 0; equals && i < assemblyManagers.size(); i++)
		equals = (*assemblyManagers[i] == *otherSFReactor.assemblyManagers[i]);

	// Compare the assembly Composites.
	for (int i = 0; equals && i < assemblyComposites.size(); i++)
		equals = (*assemblyComposites[i] == *otherSFReactor.assemblyComposites[i]);

	return equals;
	// end-user-code
}
