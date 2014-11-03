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
#include <assembly/SFRRod.h>

using namespace ICE_SFReactor;

SFRRod::SFRRod(): SFRComponent() {
	// begin-user-code

	// Set the default name, description and ID
	setName("SFR Rod 1");
	setDescription("SFR Rod 1's Description");
	setId(1);

	// Define the reflector rod as a stainless steel ring with inner radius = 0
	Material material("SS-316");
	material.setDescription("Stainless Steel");
	std::shared_ptr<Material> matPtr = std::make_shared<Material>(material);

	Ring ring;
	ring.setMaterial(matPtr);
	ring.setHeight(0.0);
	ring.setInnerRadius(0.0);
	ring.setOuterRadius(26.666);	// Rod diameter = 2 * pin diameter (?)
	reflector = std::make_shared<Ring>(ring);

	// end-user-code

}

SFRRod::SFRRod(std::string name): SFRComponent() {
	// begin-user-code

	// Set the default name, description and ID
	setName("SFR Rod 1");
	setDescription("SFR Rod 1's Description");
	setId(1);

	// Define the reflector rod as a stainless steel ring with inner radius = 0
	Material material("SS-316");
	material.setDescription("Stainless Steel");
	std::shared_ptr<Material> matPtr = std::make_shared<Material>(material);

	Ring ring;
	ring.setMaterial(matPtr);
	ring.setHeight(0.0);
	ring.setInnerRadius(0.0);
	ring.setOuterRadius(26.666);	// Rod diameter = 2 * pin diameter (?)
	reflector = std::make_shared<Ring>(ring);

	// Set name
	setName(name);

	// end-user-code

}

SFRRod::SFRRod(const SFRRod & otherRod): SFRComponent(otherRod) {
	// begin-user-code

	// Copy the reflector
	reflector = otherRod.reflector;

	return;
	// end-user-code
}


void SFRRod::setReflector(std::shared_ptr<Ring> reflector) {
	// begin-user-code

	// Check that the reflector is not null
	if (reflector)
		this->reflector = reflector;

	return;
	// end-user-code


}

std::shared_ptr<Ring> SFRRod::getReflector() {
	// begin-user-code

	return reflector;

	// end-user-code

}

std::shared_ptr<Identifiable> SFRRod::clone() {
	// begin-user-code

	// Initialize a new SFRRod.
	std::shared_ptr<SFRRod> rod = std::make_shared<SFRRod>(*this);

	// Return the newly instantiated object.
	return std::dynamic_pointer_cast<Identifiable>(rod);

	// end-user-code
}

bool SFRRod::operator==(const SFRRod & otherRod) {
	// begin-user-code

	// Method will check for equality on two levels: shallow (within the
	// scope of the SFRRod class), and deep (all inherited variables
	// from superclass). Will only return true if both cases are true.

	// Create flags for checking shallow and deep equality, default to false
	bool shallowEqual = false;
	bool deepEqual = false;

	// Check if reflectors (shallow scope) are equal; first their pointers, then
	// their values
	if (reflector && otherRod.reflector)
		shallowEqual = (*reflector == *otherRod.reflector);
	else if ((reflector && !otherRod.reflector) || (!reflector && otherRod.reflector))
		shallowEqual = false;

	// Check if all inherited variables are equal (deep scope)
	if (SFRComponent::operator==(otherRod) && shallowEqual)
		deepEqual = true;

	// Return final result
	return deepEqual;

	return false;

	// end-user-code

}
