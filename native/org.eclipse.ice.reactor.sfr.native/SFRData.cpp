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
#include <SFRData.h>
#include <IData.h>
#include <UtilityOperations.h>
#include <string>
#include <vector>
#include <memory>

using namespace ICE_SFReactor;

SFRData::SFRData() {
	// begin-user-code

	// Initialize the default feature name.
	feature = "Feature 1";

	// Initialize position with 3 default values.
	std::vector<double> posVector = { 0.0, 0.0, 0.0 };
	position = posVector;

	// Initialize the remaining defaults.
	uncertainty = 0.0;
	units = "seconds";
	value = 0.0;

	return;
	// end-user-code
}
SFRData::SFRData(std::string feature) {
	// begin-user-code

	// Initialize the default feature name.
	this->feature = "Feature 1";

	// Initialize position with 3 default values.
	std::vector<double> posVector = { 0.0, 0.0, 0.0 };
	position = posVector;

	// Initialize the remaining defaults.
	uncertainty = 0.0;
	units = "seconds";
	value = 0.0;

	// Set the feature name (the method should check the value).
	setFeature(feature);

	return;
	// end-user-code
}
SFRData::SFRData(const SFRData & otherData) {
	// begin-user-code

	// Deep copy the position data.
	position.clear();
	for (int i = 0; i < otherData.position.size(); i++)
		position.push_back(otherData.position.at(i));

	// Update the other variables.
	feature = otherData.feature;
	uncertainty = otherData.uncertainty;
	units = otherData.units;
	value = otherData.value;

	return;

	// end-user-code
}
void SFRData::setValue(double value) {
	// begin-user-code

	this->value = value;

	return;
	// end-user-code
}
void SFRData::setUncertainty(double uncertainty) {
	// begin-user-code

	this->uncertainty = uncertainty;

	return;
	// end-user-code
}
void SFRData::setUnits(std::string units) {
	// begin-user-code

	// Check for invalid units
	if (units.empty())
		return;

	// Attempt to trim the units string
	std::string trimUnits = UtilityOperations::trim_copy(units, " \f\n\r\t\v");

	// Only accept non-empty strings.
	if (!trimUnits.empty())
		this->units = trimUnits;

	return;
	// end-user-code

}
void SFRData::setFeature(std::string feature) {
	// begin-user-code

	// Check for invalid feature
	if (feature.empty())
		return;

	// Attempt to trim the feature string
	std::string trimFeature = UtilityOperations::trim_copy(feature, " \f\n\r\t\v");

	// Only accept non-empty string.
	if (!trimFeature.empty())
		this->feature = trimFeature;

	return;
	// end-user-code

}
void SFRData::setPosition(std::vector<double> position) {
		// begin-user-code

		// Change the position variable only if the incoming vector of doubles
		// is of the proper size.
		int size = this->position.size();

		// If valid vector, then write the values
		if (!position.empty() && position.size() == size) {
			for (int i = 0; i < size; i++)
				this->position.at(i) = position.at(i);
		}

		return;
		// end-user-code
}
std::shared_ptr<IData> SFRData::clone() {
		// begin-user-code

		// Initialize a new object via copy constructor
		std::shared_ptr<SFRData> object = std::make_shared<SFRData>(*this);

		// Return the newly instantiated object.
		return object;

		// end-user-code
}
bool SFRData::operator==(const IData & otherData) const {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare all the variables.
	equals = (position == otherData.getPosition()
			&& value == otherData.getValue()
			&& uncertainty == otherData.getUncertainty()
			&& units == otherData.getUnits()
			&& feature == otherData.getFeature());

	return equals;
	// end-user-code
}

std::vector<double> SFRData::getPosition() const {
	// begin-user-code
	return position;
	// end-user-code
}
double SFRData::getValue() const {
	// begin-user-code
	return value;
	// end-user-code
}
double SFRData::getUncertainty() const {
	// begin-user-code
	return uncertainty;
	// end-user-code
}
std::string SFRData::getUnits() const {
	// begin-user-code
	return units;
	// end-user-code
}
std::string SFRData::getFeature() const {
	// begin-user-code
	return feature;
	// end-user-code
}

