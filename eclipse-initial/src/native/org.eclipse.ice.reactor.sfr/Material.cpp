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
#include <Material.h>
#include <string>
#include <memory>

using namespace ICE_DS;
using namespace ICE_SFReactor;

Material::Material() : SFRComponent() {
	// begin-user-code

	// Set the name, description and ID
	setName("Material 1");
	setDescription("Material 1 Description");
	setId(1);

	// end-user-code

}
Material::Material(std::string name) : SFRComponent() {
	// begin-user-code

	// Set the specified name
	setName(name);
	setDescription("Material 1 Description");
	setId(1);

	// end-user-code

}
Material::Material(const Material & otherMat): SFRComponent(otherMat) {
	// begin-user-code

	// Since this class has no additional attributes other than those
	// inherited from SFRComponent, the superclass copy constructor is called
	// and nothing otherwise happens here.

	return;
	// end-user-code
}

std::shared_ptr<Identifiable> Material::clone() {
	// begin-user-code

	// Initialize a new Material.
	std::shared_ptr<Material> mat = std::make_shared<Material>(*this);

	// Return the newly instantiated object.
	return std::dynamic_pointer_cast<Identifiable>(mat);
	// end-user-code

}

bool Material::operator==(const Material & otherMat) {
	// begin-user-code

	// Create an equality flag, default to false
	bool areEqual = false;

	// Since this class has no variables in addition to those inherited from
	// the superclass, we can just call the superclass equality operator
	if (SFRComponent::operator==(otherMat))
		areEqual = true;

	// Return final result
	return areEqual;

	// end-user-code

}
