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
#ifndef MATERIAL_H
#define MATERIAL_H
#include <SFRComponent.h>

namespace ICE_SFReactor {

// Class representing the properties of any material that may be present
// throughout the reactor. Can include solid, liquid and gaseous states.
class Material: public SFRComponent {

public:

	// Nullary constructor.
	Material();

	// Parameterized constructor specifying the material's name.
	Material(std::string name);

	// Copy constructor. Deep copies the contents of the object.
	Material(const Material & otherMat);

	// Deep copies and returns a newly instantiated object.
	std::shared_ptr<ICE_DS::Identifiable> clone();

	// Compares the contents of objects and returns true if they are identical,
	// otherwise returns false.
	bool operator==(const Material & otherMat);

};
//end class Material

} // end namespace

#endif
