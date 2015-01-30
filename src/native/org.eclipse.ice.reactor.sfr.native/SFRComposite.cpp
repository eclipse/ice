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
#include "SFRComposite.h"

using namespace ICE_SFReactor;

SFRComposite::SFRComposite() : SFRComponent() {
	// begin-user-code

	// Set up the default values.
	setName("Composite 1");
	setId(1);
	setDescription("Composite 1's Description");

	return;
	// end-user-code
}
SFRComposite::SFRComposite(const SFRComposite & otherComp) : SFRComponent(otherComp) {
	// begin-user-code

	// Assign the contents of the other SFRComposite's map to this map.
	sFRComponents = otherComp.sFRComponents;

	return;
	// end-user-code
}
std::shared_ptr<SFRComponent> SFRComposite::getComponent(std::string name) {
	// begin-user-code

	// Initialize the default return value.
	std::shared_ptr<SFRComponent> component;

	// Search the map of components for the name.
	std::map<std::string, std::shared_ptr<SFRComponent> >::iterator iter;
	iter = sFRComponents.find(name);

	// If the name is in the map, get the pointer.
	if (iter != sFRComponents.end())
		component = iter->second;

	// Return the component.
	return component;
	// end-user-code
}
std::vector<std::string> SFRComposite::getComponentNames() {
	// begin-user-code

	// Initialize the vector of component names.
	std::vector<std::string> componentNames;

	// Loop over the component map and add the keys to the vector.
	std::map<std::string, std::shared_ptr<SFRComponent> >::iterator iter;
	for (iter = sFRComponents.begin(); iter != sFRComponents.end(); iter++)
		componentNames.push_back(iter->first);


	// Return the names.
	return componentNames;
	// end-user-code
}
void SFRComposite::removeComponent(std::string name) {
	// begin-user-code

	// Search the map of components for the name.
	std::map<std::string, std::shared_ptr<SFRComponent> >::iterator iter;
	iter = sFRComponents.find(name);

	// If the name is in the map, remove it.
	if (iter != sFRComponents.end())
		sFRComponents.erase(iter);

	return;
	// end-user-code
}
void SFRComposite::addComponent(std::shared_ptr<Component> child) {
	// begin-user-code

	// We only want to add the child if it is a non-null SFRComponent and a
	// component with the same name is not in the map.

	std::shared_ptr<SFRComponent> component;

	// Make sure the child is non-null.
	if (child)
		component = std::dynamic_pointer_cast<SFRComponent>(child);

	// Make sure the child is an SFRComponent.
	if (component) {

		// Get the name of the child.
		std::string name = child->getName();

		// Search the map of components for the name.
		std::map<std::string, std::shared_ptr<SFRComponent> >::iterator iter;
		iter = sFRComponents.find(name);

		// If the name is not in the map, add it.
		if (iter == sFRComponents.end())
			sFRComponents[name] = component;
	}

	return;
	// end-user-code
}
void SFRComposite::removeComponent(int childId) {
	// begin-user-code

	// FIXME - Currently, it is possible to have multiple Components with
	// the same ID in the Hashtable which is keyed on the components' names.
	// This means we have to loop through the entire list of components!

	// Loop over the component map and remove matching components.
	std::map<std::string, std::shared_ptr<SFRComponent> >::iterator iter;
	for (iter = sFRComponents.begin(); iter != sFRComponents.end(); iter++) {
		// Get the component in this location.
		std::shared_ptr<SFRComponent> component = iter->second;

		// Remove components with a matching ID.
		if (childId == component->getId())
			sFRComponents.erase(iter);
	}

	return;
	// end-user-code
}
std::shared_ptr<Component> SFRComposite::getComponent(int childId) {
	// begin-user-code

	// Declare the component for iteration in the loop below.
	std::shared_ptr<SFRComponent> component;

	// Loop over the component map and return the first matching component.
	std::map<std::string, std::shared_ptr<SFRComponent> >::iterator iter;
	for (iter = sFRComponents.begin(); !component && iter != sFRComponents.end(); iter++) {
		// Get the component in this location.
		std::shared_ptr<SFRComponent> iterComponent = iter->second;

		// Remove components with a matching ID.
		if (childId == iterComponent->getId())
			component = iterComponent;
	}

	// If the code reaches this point, then no matching component was found.
	return component;
	// end-user-code
}
int SFRComposite::getNumberOfComponents() {
	// begin-user-code

	// Return the size of the component table.
	return sFRComponents.size();
	// end-user-code
}
std::vector<std::shared_ptr<Component>> SFRComposite::getComponents() {
	// begin-user-code

	// Initialize the return value, an vector of components.
	std::vector<std::shared_ptr<Component>> components;

	// Loop over the component map and add them to the list.
	std::map<std::string, std::shared_ptr<SFRComponent> >::iterator iter;
	for (iter = sFRComponents.begin(); iter != sFRComponents.end(); iter++) {
		// Cast the SFRComponent back to a Component and put it in the vector.
		components.push_back(std::static_pointer_cast<Component>(iter->second));
	}

	// Return the std::vector.
	return components;
	// end-user-code
}
std::shared_ptr<Identifiable> SFRComposite::clone() {
    // begin-user-code

    // Create a shared_ptr to an SFRComponent with the copy constructor.
    std::shared_ptr<SFRComposite> composite = std::make_shared<SFRComposite>(*this);

    // Return the component
    return std::dynamic_pointer_cast<Identifiable>(composite);
    // end-user-code
}
bool SFRComposite::operator==(const SFRComposite & otherComp) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare all the member variables.
	equals = (SFRComponent::operator==(otherComp)
			&& sFRComponents.size() == otherComp.sFRComponents.size());

	// Compare the component maps.

	// Loop over the components in both maps.
	std::map<std::string, std::shared_ptr<SFRComponent> >::iterator iter;
	std::map<std::string, std::shared_ptr<SFRComponent> >::const_iterator otherIter;
	for (iter = sFRComponents.begin(), otherIter = otherComp.sFRComponents.begin();
			equals && iter != sFRComponents.end();
			iter++, otherIter++) {

		// Get the names of the components.
		std::string name = iter->first;
		std::string otherName = otherIter->first;

		// Get the components.
		std::shared_ptr<SFRComponent> component = iter->second;
		std::shared_ptr<SFRComponent> otherComponent = otherIter->second;

		// Compare the map values.
		equals = (name == otherName
				&& *component == *otherComponent);
	}

	return equals;
	// end-user-code
}
