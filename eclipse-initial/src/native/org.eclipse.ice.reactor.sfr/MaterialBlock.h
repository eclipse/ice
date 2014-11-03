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
#ifndef MATERIALBLOCK_H
#define MATERIALBLOCK_H
#include <SFRComponent.h>
#include <assembly/Ring.h>
#include <string>
#include <map>
#include <memory>

namespace ICE_SFReactor {

// The MaterialBlock class is a generalized class containing a set of
// concentric and/or radial collection of Rings that constitute the circular
// structure(s) of SFRPins (in the case of a fuel or control assemblies), and
// SFRRods (in the case of reflector assemblies).
class MaterialBlock: public SFRComponent {

private:

	// The vertical position of the MaterialBlock from the bottom of the SFRPin
	// (z-displacement where z=0 at bottom).
	double vertPosition;

	// The collection of rings contained within each MaterialBlock. A map
	// structure is used so that rings are sorted in ascending order of radii.
	// Rings cannot overlap (physically impossible).
	std::map<std::string,std::shared_ptr<Ring>> rings;

public:

	// Nullary constructor.
	MaterialBlock();

	// Copy constructor. Deep copies the contents of the MaterialBlock.
	MaterialBlock(const MaterialBlock & otherMatBlock);

	// Set the vertical position (z-displacement) of the material block, where
	// z=0 at the bottom end of the structure.
	void setVertPosition(double vertPosition);

	// Returns the vertical position (z-displacement) of the material block as
	// a double, where z=0 at the bottom end of the structure.
	double getVertPosition();

	// Adds a ring to the current collection of rings; returns true if the
	// operation was successful.
	bool addRing(std::shared_ptr<Ring> ring);

	// Removes the specified ring from the collection of rings; returns true if
	// the operation was successful.
	bool removeRing(std::string name);

	// Returns the ring of the specified radius if it exists, otherwise returns null.
	std::shared_ptr<Ring> getRing(double radius);

	// Returns the ring of the specified name if it exists, otherwise returns null.
	std::shared_ptr<Ring> getRing(std::string name);

	// Returns an std::vector of Rings contained in the material block, ordered
	// by ascending radii.
	std::vector<std::shared_ptr<Ring>> getRings();

	// Deep copies and returns a newly instantiated MaterialBlock.
	std::shared_ptr<Identifiable> clone();

	// Compares the contents of MaterialBlocks and returns true if they are
	// identical, otherwise returns false.
	bool operator==(const MaterialBlock & otherMatBlock);

};
//end class MaterialBlock

}// end namespace
#endif
