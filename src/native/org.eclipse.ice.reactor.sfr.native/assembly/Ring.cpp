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
#include <assembly/Ring.h>
#include <Material.h>
#include <string>
#include <memory>

using namespace ICE_SFReactor;

Ring::Ring() :
		SFRComponent() {
	// begin-user-code

	// Set the ring name, description and ID
	setName("Ring 1");
	setDescription("Ring 1 Description");
	setId(1);

	// Set the height (z-displacement), inner radius, outer radius and material
	height = 0.0;
	innerRadius = 0.0;
	outerRadius = 1.0;

	// end-user-code

}
Ring::Ring(std::string name) :
		Ring() {
	// begin-user-code

	// Set the ring name to the custom name. The defaults have already been set.
	setName(name);

	// end-user-code

}

Ring::Ring(std::string name, std::shared_ptr<Material> material, double height,
		double innerRadius, double outerRadius) :
		Ring() {
	// begin-user-code

	// Set the ring name. The other defaults have already been set.
	setName(name);

	// Set the name, material, height and radii
	setMaterial(material);
	setHeight(height);
	setInnerRadius(innerRadius);
	setOuterRadius(outerRadius);

	// end-user-code

}

Ring::Ring(const Ring & otherRing) :
		SFRComponent(otherRing) {
	// begin-user-code

	// Copy the height, radii and material
	height = otherRing.height;
	innerRadius = otherRing.innerRadius;
	outerRadius = otherRing.outerRadius;
	material = otherRing.material;

	return;
	// end-user-code
}

void Ring::setHeight(double height) {
	// begin-user-code

	// If height is non-negative, set height
	if (height >= 0.0)
		this->height = height;

	return;
	// end-user-code

}

double Ring::getHeight() {
	// begin-user-code

	return height;

	// end-user-code

}

void Ring::setInnerRadius(double innerRadius) {
	// begin-user-code

	// If inner radius is non-negative
	if (innerRadius >= 0.0)
		this->innerRadius = innerRadius;

	return;
	// end-user-code

}
double Ring::getInnerRadius() {
	// begin-user-code

	return innerRadius;

	// end-user-code

}

void Ring::setOuterRadius(double outerRadius) {
	// begin-user-code

	// If outer radius is non-negative
	if (outerRadius >= 0.0)
		this->outerRadius = outerRadius;

	return;
	// end-user-code

}

double Ring::getOuterRadius() {
	// begin-user-code

	return outerRadius;

	// end-user-code

}

void Ring::setMaterial(std::shared_ptr<Material> material) {
	// begin-user-code

	// If material is non-null, set it
	if (material)
		this->material = material;

	return;
	// end-user-code

}

std::shared_ptr<Material> Ring::getMaterial() {
	// begin-user-code

	return material;

	// end-user-code

}

std::shared_ptr<Identifiable> Ring::clone() {
	// begin-user-code

	// Initialize a new Ring.
	std::shared_ptr<Ring> ring = std::make_shared < Ring > (*this);

	// Return the newly instantiated object.
	return std::dynamic_pointer_cast < ICE_DS::Identifiable > (ring);

	// end-user-code
}

bool Ring::operator==(const Ring & otherRing) {
	// begin-user-code

	// Method will check for equality on two levels: shallow (within the
	// scope of the Ring class), and deep (all inherited variables
	// from superclass). Will only return true if both cases are true.

	// Create flags for checking shallow and deep equality, default to false
	bool shallowEqual = false;
	bool deepEqual = false;

	// Check if height, radii are equal
	if (height == otherRing.height && innerRadius == otherRing.innerRadius
			&& outerRadius == otherRing.outerRadius)
		shallowEqual = true;

	// Check the material pointers
	if (material && otherRing.material) {
		shallowEqual &= (*material == *otherRing.material);
	} else if ((material && !otherRing.material)
			|| (!material && otherRing.material)) {
		shallowEqual = false;
	}

	// Check if all inherited variables are equal (deep scope)
	if (SFRComponent::operator==(otherRing) && shallowEqual)
		deepEqual = true;

	// Return final result
	return deepEqual;
}

