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

#ifndef SFREACTOR_H
#define SFREACTOR_H
#include "SFRComposite.h"
#include "AssemblyType.h" //Dependency Generated Source:SFReactor Target:AssemblyType
#include <string>
#include "SFRComponent.h"
#include "GridManager.h"

namespace ICE_SFReactor {

// Class represents a sodium fast reactor at the highest core-level view. 
class SFReactor: public SFRComposite {

private:

	// Number of assemblies in the reactor core.
	int size;

	// Shortest distance between centers of adjacent assemblies.
	double latticePitch;

	// The physical size of assemblies within the SFR core; all assemblies are
	// assumed to be the same size. Defined as the distance from one flat outer
	// duct surface to the outer surface parallel to it.
	double outerFlatToFlat;

	// The vector of assembly composites, that represent the collections of the
	// different assemblies in this reactor.
	std::vector<std::shared_ptr<SFRComposite>> assemblyComposites;

	// The vector of GridManagers, keyed by AssemblyType, that manage the
	// location of assemblies within a reactor.
	std::vector<std::shared_ptr<GridManager>> assemblyManagers;

public:

	// Parameterized constructor with reactor size (number of assemblies) specified.
	SFReactor(int size);

	// Deep copies the contents of the SFReactor.
	SFReactor(const SFReactor & otherSFR);

	// Returns the size (number of assemblies) of the reactor core.
	int getSize();

	// Sets the lattice pitch.
	void setLatticePitch(double latticePitch);

	// Returns the lattice pitch (shortest distance between centers of adjacent
	// assemblies) as a double.
	double getLatticePitch();

	// Sets the outer flat-to-flat distance.
	void setOuterFlatToFlat(double outerFlatToFlat);

	// Returns the outer flat-to-flat distance as a double.
	double getOuterFlatToFlat();

	// Adds the specified assembly to the reactor core; returns true if the
	// operation was successful.
	bool addAssembly(AssemblyType type, std::shared_ptr<SFRComposite> assembly);

	// Removes the specified assembly from the reactor core; returns true if the
	// operation was successful.
	bool removeAssembly(AssemblyType type, std::string name);

	// Removes the assembly of AssemblyType, from the specified (x, y) location;
	// returns true if the operation was successful.
	bool removeAssemblyFromLocation(AssemblyType type, int row, int column);

	// Adds an assembly of the specified type and name to the reactor in the
	// specified location.
	bool setAssemblyLocation(AssemblyType type, std::string name, int row,
			int column);

	// Returns the number of assemblies of AssemblyType in the reactor.
	int getNumberOfAssemblies(AssemblyType type);

	// Returns a string std::vector of the names of all assemblies of the
	// specified type.
	std::vector<std::string> getAssemblyNames(AssemblyType type);

	// Returns the assembly of the specified type and name.
	std::shared_ptr<SFRComponent> getAssemblyByName(AssemblyType type, std::string name);

	// Returns the assembly of AssemblyType, at the specified (x, y) coordinates.
	std::shared_ptr<SFRComponent> getAssemblyByLocation(AssemblyType type, int row, int column);

	// Returns an std::vector of locations within the reactor that are occupied
	// by the assembly matching the specified type and name.
	std::vector<int> getAssemblyLocations(AssemblyType type, std::string name);

	// An operation that overrides the SFRComposite's operation. This operation
	// does nothing and requires that the appropriate, more defined, associated
	// operation to be utilized on this class.
	void addComponent(std::shared_ptr<Component> child);

	// An operation that overrides the SFRComposite's operation. This operation
	// does nothing and requires that the appropriate, more defined, associated
	// operation to be utilized on this class.
	void removeComponent(int childId);

	// An operation that overrides the SFRComposite's operation. This operation
	// does nothing and requires that the appropriate, more defined, associated
	// operation to be utilized on this class.
	void removeComponent(std::string name);

	// Deep copies and returns a newly instantiated SFReactor.
	std::shared_ptr<Identifiable> clone();

	// Overrides the equals operation to check the attributes on this SFReactor
	// with another SFReactor of the same type. Returns true if the SFReactors
	// are equal. False otherwise.
	bool operator==(const SFReactor & otherSFR);

};
//end class SFReactor

} // end namespace
#endif
