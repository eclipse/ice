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
#ifndef SFRROD_H
#define SFRROD_H
#include <SFRComponent.h>
#include <assembly/SFRRod.h>
#include <assembly/Ring.h>
#include <Material.h>
#include <string>
#include <memory>

namespace ICE_SFReactor {

// Class representing the solid cylindrical structure found inside radial
// reflector assemblies.
class SFRRod: public SFRComponent {

private:

	// Solid cylindrical structures of uniform reflector material stacked on top
	// of each other.
	std::shared_ptr<Ring> reflector;

public:

	// Nullary constructor.
	SFRRod();

	// Parameterized constructor with name specified.
	SFRRod(std::string name);

	// Copy constructor. Deep copies the contents of the object.
	SFRRod(const SFRRod & otherRod);

	// Sets the reflector rod.
	void setReflector(std::shared_ptr<Ring> reflector);

	// Returns the reflector rod as a Ring.
	std::shared_ptr<Ring> getReflector();

	// Deep copies and returns a newly instantiated object.
	std::shared_ptr<Identifiable> clone();

	// Compares the contents of objects and returns true if they are identical,
	// otherwise returns false.
	bool operator==(const SFRRod & otherRod);

};
//end class SFRRod

}// end namespace
#endif
