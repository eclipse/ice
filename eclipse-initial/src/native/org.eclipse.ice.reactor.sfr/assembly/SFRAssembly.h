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
#ifndef SFRASSEMBLY_H
#define SFRASSEMBLY_H
#include <SFRComposite.h>
#include <AssemblyType.h>

namespace ICE_SFReactor {

// Class representing the assembly structure of a SFR. The SFR assembly is
// housed in a hexagonal structure called the wrapper tube (or duct), and
// contains a lattice of either pins or rods.
class SFRAssembly: public SFRComposite {

private:

	// Size of a SFRAssembly. Size represents number of pins in a fuel or
	// control assembly, and rods in a reflector assembly.
	int size;

	// Thickness of the assembly duct wall.
	double ductThickness;

protected:

	// The type of SFR assembly represented, either FuelAssembly,
	// ControlAssembly or ReflectorAssembly.
	AssemblyType assemblyType;

public:

	// Parameterized constructor with assemble size specified. Size represents
	// number of pins in a fuel or control assembly, and rods in a reflector assembly.
	SFRAssembly(int size);

	// Parameterized constructor with assembly name, type and size specified.
	// Size represents number of pins in a fuel or control assembly, and rods in
	// a reflector assembly.
	SFRAssembly(std::string name, AssemblyType type, int size);

	// Returns the assembly size. Size represents number of pins in a fuel or
	// control assembly, and rods in a reflector assembly.
	int getSize() const;

	// Returns the assembly type (fuel, control or reflector).
	AssemblyType getAssemblyType();

	// Sets the thickness of the assembly duct wall.
	void setDuctThickness(double thickness);

	// Returns the duct wall thickness of an assembly as a double.
	double getDuctThickness();

	// Deep copies and returns a newly instantiated object.
	std::shared_ptr<Identifiable> clone();

	// Deep copies the contents of the object from another object.
	SFRAssembly(const SFRAssembly & otherAssembly);

	// Overrides the equals operation to check the attributes on this object
	// with another object of the same type. Returns true if the objects are
	// equals. False otherwise.
	bool operator==(const SFRAssembly & otherAssembly);

};
//end class SFRAssembly

}

#endif
