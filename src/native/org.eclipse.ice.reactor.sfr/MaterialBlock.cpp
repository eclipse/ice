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
#include <MaterialBlock.h>
#include <assembly/Ring.h>

using namespace ICE_SFReactor;

MaterialBlock::MaterialBlock() :
		SFRComponent(), rings() {
	// begin-user-code

	// Initialize a default name, description, and ID
	setName("MaterialBlock 1");
	setDescription("MaterialBlock 1 Description");
	setId(1);

	// Initialize the z-displacement as zero
	vertPosition = 0.0;

	return;
	// end-user-code

}

MaterialBlock::MaterialBlock(const MaterialBlock & otherMatBlock) :
		SFRComponent(otherMatBlock) {
	// begin-user-code

	// Copy the vertical position and rings map.
	vertPosition = otherMatBlock.vertPosition;
	rings = otherMatBlock.rings;

	return;
	// end-user-code
}

void MaterialBlock::setVertPosition(double vertPosition) {
	// begin-user-code

	// Vertical position (z-displacement) must be non-negative to set
	if (vertPosition >= 0)
		this->vertPosition = vertPosition;

	return;
	// end-user-code
}

double MaterialBlock::getVertPosition() {
	// begin-user-code

	return vertPosition;
	// end-user-code
}

bool MaterialBlock::addRing(std::shared_ptr<Ring> ring) {
	// begin-user-code

	// Local Declarations
	bool success = false;

	// Check if the ring is null first
	if (ring && !rings.count(ring->getName())) {
		// Put the ring in the map
		auto ret = rings.insert(
				std::pair<std::string, std::shared_ptr<Ring>>(ring->getName(),
						ring));
		// Set the status flag
		success = ret.second;
	}

	return success;

	// end-user-code

}

bool MaterialBlock::removeRing(std::string name) {
	// begin-user-code

	// Local Declarations
	bool removed = false;

	// Remove the ring if it is in the map
	if (rings.count(name)) {
		removed = (bool) rings.erase(name);
	}

	return removed;

	// end-user-code

}

std::shared_ptr<Ring> MaterialBlock::getRing(double radius) {
	// begin-user-code

	// Local Declarations
	std::shared_ptr<Ring> ring, currRing;

	// Check that the ring radius is valid. If not, return null.
	if (radius > 0.0) {
		// Look for the ring with the correct radius
		for (auto mapIter = rings.begin(); mapIter != rings.end(); mapIter++) {
			currRing = mapIter->second;
			// Check if the specified radius falls within the inner and outer
			// radii of the current ring
			if (radius >= currRing->getInnerRadius()
					&& radius <= currRing->getOuterRadius()) {
				// Save the pointer if it does
				ring = currRing;
				// And break OUT! Oh yeah!
				break;
			}
		}
	}

	// Return the ring
	return ring;

	// end-user-code

}

std::shared_ptr<Ring> MaterialBlock::getRing(std::string name) {
	// begin-user-code

	// Local Declarations
	std::shared_ptr<Ring> ring;

	// Get the ring
	if (rings.count(name)) {
		ring = rings[name];
	}

	// Return the ring
	return ring;

	// end-user-code

}

std::vector<std::shared_ptr<Ring> > MaterialBlock::getRings() {
	// begin-user-code

	// Initialize an std::vector of Rings
	std::vector<std::shared_ptr<Ring>> list;

	// Load the list
	for (auto mapIter = rings.begin(); mapIter != rings.end(); mapIter++) {
		list.push_back(mapIter->second);
	}

	// Return the std::vector
	return list;

	// end-user-code

}

std::shared_ptr<Identifiable> MaterialBlock::clone() {
	// begin-user-code

	// Initialize a new MaterialBlock.
	std::shared_ptr<MaterialBlock> block = std::make_shared < MaterialBlock
			> (*this);

	// Return the newly instantiated object.
	return std::dynamic_pointer_cast < Identifiable > (block);
	// end-user-code

}

bool MaterialBlock::operator==(const MaterialBlock & otherMatBlock) {
	// begin-user-code

	// Check simple stuff
	bool equal = false;
	equal = SFRComponent::operator==(otherMatBlock);
	equal &= vertPosition == otherMatBlock.vertPosition;

	// Check the individual rings
	bool otherHasRing = false;
	for (auto mapIter = rings.begin(); mapIter != rings.end(); mapIter++) {
		otherHasRing = otherMatBlock.rings.count(mapIter->first);
		equal &= otherHasRing;
		// Break out if the ring isn't in the block, otherwise check it
		if (otherHasRing) {
			equal &= (*(mapIter->second)
					== *(otherMatBlock.rings.at(mapIter->first)));
			auto other = equal;
		} else
			break;
	}

	// Return final result
	return equal;
	// end-user-code
}

