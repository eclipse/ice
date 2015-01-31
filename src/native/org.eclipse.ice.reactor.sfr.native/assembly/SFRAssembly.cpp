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
#include "SFRAssembly.h"

using namespace ICE_SFReactor;

SFRAssembly::SFRAssembly(int size) : SFRComposite() {
	// begin-user-code

	// Set the size if positive, otherwise default to 1.
	this->size = (size > 0 ? size : 1);

	// Set the default name, description, and ID.
	setName("SFR Assembly 1");
	setDescription("SFR Assembly 1's Description");
	setId(1);

	// Initialize ductThickness.
	ductThickness = 0.0;

	// Set the default assembly type.
	assemblyType = Fuel;

	return;
	// end-user-code
}
SFRAssembly::SFRAssembly(std::string name, AssemblyType type, int size) : SFRComposite() {
	// begin-user-code

	// Set the size if positive, otherwise default to 1.
	this->size = (size > 0 ? size : 1);

	// Set the default name, description, and ID.
	setName("SFR Assembly 1");
	setDescription("SFR Assembly 1's Description");
	setId(1);

	// Initialize ductThickness.
	ductThickness = 0.0;

	// Set the custom name if possible.
	setName(name);

	// Set the assembly type.
	assemblyType = type;

	return;
	// end-user-code
}
SFRAssembly::SFRAssembly(const SFRAssembly & otherAssembly) : SFRComposite(otherAssembly) {
	// begin-user-code

	// Copy the local values.
	size = otherAssembly.size;
	assemblyType = otherAssembly.assemblyType;
	ductThickness = otherAssembly.ductThickness;

	return;
	// end-user-code
}
int SFRAssembly::getSize() const {
	// begin-user-code
	return size;
	// end-user-code
}
AssemblyType SFRAssembly::getAssemblyType() {
	// begin-user-code
	return assemblyType;
	// end-user-code
}
void SFRAssembly::setDuctThickness(double thickness) {
	// begin-user-code

	// Only set the duct thickness if it is 0 or larger.
	if (thickness >= 0.0)
		ductThickness = thickness;

	return;
	// end-user-code
}
double SFRAssembly::getDuctThickness() {
	// begin-user-code
	return ductThickness;
	// end-user-code
}
std::shared_ptr<Identifiable> SFRAssembly::clone() {
	// begin-user-code

	// Initialize a new SFRAssembly.
	std::shared_ptr<SFRAssembly> assembly = std::make_shared<SFRAssembly>(*this);

	// Return the newly instantiated object.
	return std::dynamic_pointer_cast<Identifiable>(assembly);

	// end-user-code
}
bool SFRAssembly::operator==(const SFRAssembly & otherAssembly) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare the values between the two objects.
	equals = (SFRComposite::operator==(otherAssembly)
			&& size == otherAssembly.size
			&& assemblyType == otherAssembly.assemblyType
			&& ductThickness == otherAssembly.ductThickness);

	return equals;
	// end-user-code
}
