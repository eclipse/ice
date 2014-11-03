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
#include "FeatureSet.h"

#include <memory>
#include <string>
#include <vector>
#include "IData.h"
#include "UtilityOperations.h"

using namespace ICE_SFReactor;

FeatureSet::FeatureSet(const FeatureSet & arg) {
	// begin-user-code

	// Copy the name.
	name = arg.name;

	// Assign the contents of the other FeatureSet's iData to this iData.
	iData = arg.iData;

	return;
	// end-user-code
}
FeatureSet::FeatureSet(std::string feature) {
	// begin-user-code

	// Trim the feature name and set it.
	name = UtilityOperations::trim_copy(feature, " \f\n\r\t\v");

	return;
	// end-user-code
}
std::string FeatureSet::getName() {
	// begin-user-code

	// Return the FeatureSet's name.
	return name;
	// end-user-code
}
std::vector< std::shared_ptr<IData> > FeatureSet::getData() {
	// begin-user-code

	return iData;
	// end-user-code
}
bool FeatureSet::addIData(std::shared_ptr<IData> iData) {
	// begin-user-code

	// By default, we have not added the data to the List.
	bool success = false;

	// If the feature name matches, add it to the vector.
	if (iData && iData->getFeature() == name) {
		this->iData.push_back(iData);
		success = true;
	}

	// Return whether or not the data was successfully added to the vector.
	return success;
	// end-user-code
}
std::shared_ptr<FeatureSet> FeatureSet::clone() {
	// begin-user-code

	// Initialize a new object with the copy constructor.
	std::shared_ptr<FeatureSet> featureSet = std::make_shared<FeatureSet>(*this);

	// Return the newly instantiated object.
	return featureSet;
	// end-user-code
}
bool FeatureSet::operator==(const FeatureSet &otherObject) const {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare all the variables.
	equals = (name == otherObject.name
			&& iData.size() == otherObject.iData.size());

	// Because iData is a vector of pointers, we need to compare each of the
	// values pointed to. If we have a mismatch, then the loop breaks.
	for (int i = 0; equals && i < iData.size(); i++)
		equals = (*iData[i] == *otherObject.iData[i]);

	return equals;
	// end-user-code
}
