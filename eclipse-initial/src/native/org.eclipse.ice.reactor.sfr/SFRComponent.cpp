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
#include "SFRComponent.h"

#include <map>
#include <memory>
#include <set>
#include <string>
#include <vector>

#include "FeatureSet.h"
#include "SFRData.h"
#include "UtilityOperations.h"
#include <ICEObject/Identifiable.h>

using namespace ICE_SFReactor;

SFRComponent::SFRComponent() : dataTree(), listeners() {
	// begin-user-code

	// Initialize the default name, description, and ID.
	name = "Component 1";
	description = "Component 1's Description";
	id = 1;

	// Initialize the default source information.
	sourceInfo = "No Source Information";

	// Initialize the default time information.
	time = 0.0;
	timeUnits = "seconds";

	return;
	// end-user-code
}

SFRComponent::SFRComponent(std::string name) : dataTree(), listeners() {
	// begin-user-code

	// Initialize the default name, description, and ID.
	this->name = "Component 1";
	description = "Component 1's Description";
	id = 1;

	// Initialize the default source information.
	sourceInfo = "No Source Information";

	// Initialize the default time information.
	time = 0.0;
	timeUnits = "seconds";

	// Set the name to the specified std::string if it is not null and not empty.
	setName(name);

	return;
	// end-user-code
}

SFRComponent::SFRComponent(const SFRComponent & component) {
	// begin-user-code

	// Copy the name, description, and ID.
	name = component.name;
	description = component.description;
	id = component.id;

	// Copy the source information.
	sourceInfo = component.sourceInfo;

	// Copy the time information.
	time = component.time;
	timeUnits = component.timeUnits;

	// Copy the data tree used to store IData.

	dataTree.clear();

	// Loop over the timesteps in the other component's dataTree.
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > >::const_iterator timeIter;
	for (timeIter = component.dataTree.begin();
			timeIter != component.dataTree.end();
			timeIter++) {

		// Get the time and the map of FeatureSets at this timestep.
		double time = timeIter->first;
		std::map<std::string, std::shared_ptr<FeatureSet> > otherFeatureSets = timeIter->second;

		// Create a map.
		std::map<std::string, std::shared_ptr<FeatureSet> > featureSets;

		// Loop over the FeatureSets at this timestep.
		std::map<std::string, std::shared_ptr<FeatureSet> >::const_iterator setIter;
		for (setIter = otherFeatureSets.begin();
				setIter != otherFeatureSets.end();
				setIter++) {

			// Get the feature name and FeatureSet.
			std::string feature = setIter->first;
			std::shared_ptr<FeatureSet> featureSet = setIter->second;

			// Add an entry to the FeatureSet map.
			featureSets.insert(std::pair< std::string, std::shared_ptr<FeatureSet> >(feature, featureSet));
		}

		// Add the entry to this dataTree.
		dataTree.insert(std::pair<double, std::map<std::string, std::shared_ptr<FeatureSet> > >(time, featureSets));
	}

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//	// Add the listeners from the other component.
//	listeners.addAll(component.listeners);

//	// Notify listeners of the changes.
//	notifyListeners();

	return;
	// end-user-code
}
std::string SFRComponent::getName() const {
	// begin-user-code

	// Return the name.
	return name;
	// end-user-code
}
std::string SFRComponent::getDescription() const {
	// begin-user-code

	// Return the description.
	return description;
	// end-user-code
}
int SFRComponent::getId() const {
	// begin-user-code

	// Return the ID.
	return id;
	// end-user-code
}
std::string SFRComponent::getSourceInfo() {
	// begin-user-code

	// Return the source information string.
	return sourceInfo;
	// end-user-code
}
double SFRComponent::getCurrentTime() {
	// begin-user-code

	// Return the current time.
	return time;
	// end-user-code
}
std::vector<std::string> SFRComponent::getFeatureList() {
	// begin-user-code

	// Initialize the vector of feature names.
	std::vector<std::string> featureList;

	// Initialize a Set of strings to hold only unique feature names.
	std::set<std::string> featureNames;

	// Iterate over the timestep map.
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > >::iterator timeIter;
	for (timeIter = dataTree.begin(); timeIter != dataTree.end(); timeIter++) {

		// Get the map of FeatureSets at the timestep.
		std::map<std::string, std::shared_ptr<FeatureSet> > featureSets = timeIter->second;

		// Iterate over the FeatureSet map and add all features to the set.
		std::map<std::string, std::shared_ptr<FeatureSet> >::iterator setIter;
		for (setIter = featureSets.begin(); setIter != featureSets.end(); setIter++)
			featureNames.insert(setIter->first);
	}

	// Add each name from the set to the vector.
	std::set<std::string>::iterator iter;
	for (iter = featureNames.begin(); iter != featureNames.end(); iter++)
		featureList.push_back(*iter);

	// Return the list of feature names.
	return featureList;
	// end-user-code
}
int SFRComponent::getNumberOfTimeSteps() {
	// begin-user-code

	// Return the size of the data tree (which is keyed on time values).
	return dataTree.size();
	// end-user-code
}
std::vector<std::shared_ptr<IData>> SFRComponent::getDataAtCurrentTime(std::string feature) {
	// begin-user-code

	// We need to return a vector of IData.
	std::vector<std::shared_ptr<IData> > data;

	// See if the current time is in the dataTree map.
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > >::iterator timeIter = dataTree.find(time);
	if (timeIter != dataTree.end()) {

		// Get the map of FeatureSets from the iterator.
		std::map<std::string, std::shared_ptr<FeatureSet> > featureSets = timeIter->second;

		// See if the feature is in the FeatureSet map.
		std::map<std::string, std::shared_ptr<FeatureSet> >::iterator setIter = featureSets.find(feature);
		if (setIter != featureSets.end()) {

			// Get the FeatureSet from the iterator.
			std::shared_ptr<FeatureSet> featureSet = setIter->second;

			// Get the vector of IData from the FeatureSet.
			data = featureSet.get()->getData();
		}
	}

	return data;
	// end-user-code
}
std::vector<std::string> SFRComponent::getFeaturesAtCurrentTime() {
	// begin-user-code

	// Initialize the list of feature names.
	std::vector<std::string> featureList;

	// See if the current time is in the dataTree map.
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > >::iterator timeIter = dataTree.find(time);
	if (timeIter != dataTree.end()) {

		// Get the map of FeatureSets at the timestep.
		std::map<std::string, std::shared_ptr<FeatureSet> > featureSets = timeIter->second;

		// Iterate over the FeatureSet map and add all features to the set.
		std::map<std::string, std::shared_ptr<FeatureSet> >::iterator setIter;
		for (setIter = featureSets.begin(); setIter != featureSets.end(); setIter++)
			featureList.push_back(setIter->first);

	}

	// Return the list of feature names.
	return featureList;
	// end-user-code
}
std::vector<double> SFRComponent::getTimes() {
	// begin-user-code

	// Initialize the list of times.
	std::vector<double> times;

	// Iterate over the timestep map and put the times in the vector.
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > >::iterator timeIter;
	for (timeIter = dataTree.begin(); timeIter != dataTree.end(); timeIter++)
		times.push_back(timeIter->first);

	// Return the list of times.
	return times;
}
int SFRComponent::getTimeStep(double time) {
	// begin-user-code

	// Initialize a counter.
	int counter = 0;

	// Loop over the dataTree map.
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > >::iterator timeIter;
	for (timeIter = dataTree.begin(); timeIter != dataTree.end(); timeIter++) {
		// If the time matches, return the entry's index.
		if (timeIter->first == time)
			return counter;
		// Update the index counter.
		counter++;
	}

	// A matching time was not found.
	return -1;
	// end-user-code
}
std::string SFRComponent::getTimeUnits() {
	// begin-user-code

	// Return the time units string.
	return timeUnits;
	// end-user-code
}
void SFRComponent::setName(std::string name) {
	// begin-user-code

	std::string trimmed = UtilityOperations::trim_copy(name, " \f\n\r\t\v");

	// Only update the sourceInfo if the string is not null and not empty.
	if (trimmed != "") {
		this->name = trimmed;

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//		// Notify listeners of the change.
//		notifyListeners();
	}

	return;
	// end-user-code
}
void SFRComponent::setId(int id) {
	// begin-user-code

	// Only allow non-negative IDs.
	if (id >= 0) {
		this->id = id;

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//		// Notify listeners of the change.
//		notifyListeners();
	}

	return;
	// end-user-code
}
void SFRComponent::setDescription(std::string description) {
	// begin-user-code

	std::string trimmed = UtilityOperations::trim_copy(description, " \f\n\r\t\v");

	// Only update the sourceInfo if the string is not null and not empty.
	if (trimmed != "") {
		this->description = trimmed;

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//		// Notify listeners of the change.
//		notifyListeners();
	}

	return;
	// end-user-code
}
void SFRComponent::setSourceInfo(std::string sourceInfo) {
	// begin-user-code

	std::string trimmed = UtilityOperations::trim_copy(sourceInfo, " \f\n\r\t\v");

	// Only update the sourceInfo if the string is not null and not empty.
	if (trimmed != "") {
		this->sourceInfo = trimmed;

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//		// Notify listeners of the change.
//		notifyListeners();
	}

	return;
	// end-user-code
}
void SFRComponent::setTimeUnits(std::string timeUnits) {
	// begin-user-code

	std::string trimmed = UtilityOperations::trim_copy(timeUnits, " \f\n\r\t\v");

	// Only set the time units if the string is not null and not empty.
	if (trimmed != "") {
		this->timeUnits = trimmed;

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//		// Notify listeners of the change.
//		notifyListeners();
	}

	return;
	// end-user-code
}
void SFRComponent::setTime(double newTime) {
	// begin-user-code

	// We only allow non-negative times
	if (newTime >= 0.0) {
		time = newTime;

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//		// Notify listeners of the change.
//		notifyListeners();
	}

	return;
	// end-user-code
}
void SFRComponent::addData(std::shared_ptr<SFRData> data, double time) {
	// begin-user-code

	// Check the parameters.
	if (time < 0.0)
		return;

	// Get the name of the feature in the data.
	std::string feature = data->getFeature();

	// Define a [temporary] pointer to a map of FeatureSets and a separate
	// pointer to a FeatureSet.
	std::map<std::string, std::shared_ptr<FeatureSet> > *featureSets;
	std::shared_ptr<FeatureSet> featureSet;

	// Get the map of FeatureSets for the time in the data.
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > >::iterator timeIter = dataTree.find(time);

	// If the timestep is not in dataTree, insert a new time and map.
	if (timeIter == dataTree.end()) {
		// Create a new map.
		std::map<std::string, std::shared_ptr<FeatureSet> > setMap;

		// Add the pair to the dataTree. Insert returns a new pair, with the
		// first element being the iterator in dataTree for the newly-inserted
		// pair.
		timeIter = dataTree.insert(std::make_pair(time, setMap)).first;
	}
	// Set the pointer to the map of FeatureSets at the timestep.
	featureSets = &timeIter->second;

	// At this point, we have a FeatureSet map that is in dataTree. Try to get
	// the FeatureSet for the feature name.

	// Get the FeatureSet for the feature string.
	std::map<std::string, std::shared_ptr<FeatureSet> >::iterator setIter = featureSets->find(feature);

	// If the feature is in the FeatureSet map, get its FeatureSet.
	if (setIter != featureSets->end()) {
		featureSet = setIter->second;
	}
	// Otherwise, create and insert a new FeatureSet.
	else {
		// Create a new FeatureSet based on the feature.
		featureSet = std::make_shared<FeatureSet>(feature);

		// Insert the new FeatureSet into the map of FeatureSets.
		(*featureSets)[feature] = featureSet;
	}

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//	// We have either found or created a FeatureSet. Add the data to it and
//	// notify listeners if the data has changed.
	if (featureSet->addIData(data)) {
//		notifyListeners();
	}

	return;
	// end-user-code
}
void SFRComponent::removeDataFromFeature(std::string feature) {
	// begin-user-code

	// Whether or not we have removed data.
	bool updated = false;

	// Iterate over the timestep map.
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > >::iterator timeIter;
	for (timeIter = dataTree.begin(); timeIter != dataTree.end(); timeIter++) {

		// Get [a temporary pointer to] the map of FeatureSets at the timestep.
		std::map<std::string, std::shared_ptr<FeatureSet> > *featureSets = &timeIter->second;

		// Get the FeatureSet for the feature string.
		std::map<std::string, std::shared_ptr<FeatureSet> >::iterator setIter = featureSets->find(feature);

		// If the feature is in the FeatureSet map, remove it.
		if (setIter != featureSets->end()) {
			featureSets->erase(setIter);

			// Mark the component as updated.
			updated = true;
		}
	}

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//	// If we have removed data, notify listeners of this change.
//	if (updated)
//		notifyListeners();

	return;
	// end-user-code

}
void SFRComponent::accept(const ISFRComponentVisitor & visitor) {
	// begin-user-code

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//	// Only accept valid visitors.
//	if (visitor != null)
//		visitor.visit(this);

	return;
	// end-user-code
}
void SFRComponent::notifyListeners() {

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

	return;
}
bool SFRComponent::operator==(const SFRComponent & component) {
	// begin-user-code

	// By default, the objects are not equivalent.
	bool equals = false;

	// Compare all the member variables.
	equals = (this->id == component.id && this->time == component.time
			&& this->name == component.name
			&& this->description == component.description
			&& this->timeUnits == component.timeUnits
			&& this->sourceInfo == component.sourceInfo
			&& this->dataTree.size() == component.dataTree.size());

	// Compare the dataTrees.

	// Loop over the timesteps in both dataTree maps.
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > >::iterator timeIter = dataTree.begin();
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > >::const_iterator otherTimeIter = component.dataTree.begin();
	for (; equals && timeIter != dataTree.end(); timeIter++, otherTimeIter++) {

		// Get the times.
		double time = timeIter->first;
		double otherTime = otherTimeIter->first;

		// Get the maps of FeatureSets.
		std::map<std::string, std::shared_ptr<FeatureSet> > featureSets = timeIter->second;
		std::map<std::string, std::shared_ptr<FeatureSet> > otherFeatureSets = otherTimeIter->second;

		// Compare the times and map sizes.
		equals = (time == otherTime
				&& featureSets.size() == otherFeatureSets.size());

		// Loop over the features in both FeatureSet maps.
		std::map<std::string, std::shared_ptr<FeatureSet> >::iterator setIter = featureSets.begin();
		std::map<std::string, std::shared_ptr<FeatureSet> >::const_iterator otherSetIter = otherFeatureSets.begin();
		for (; equals && setIter != featureSets.end(); setIter++, otherSetIter++) {

			// Get the features.
			std::string feature = setIter->first;
			std::string otherFeature = otherSetIter->first;

			// Get the FeatureSet pointers.
			std::shared_ptr<FeatureSet> featureSet = setIter->second;
			std::shared_ptr<FeatureSet> otherFeatureSet = otherSetIter->second;

			// Compare the features and FeatureSets.
			equals = (feature == otherFeature
					&& *featureSet == *otherFeatureSet);
		}
	}

	return equals;
}
void SFRComponent::update(std::string updatedKey, std::string newValue) {
	// begin-user-code

	// Nothing is required for this method.

	// end-user-code
}
void SFRComponent::registerListener(std::shared_ptr<IComponentListener> listener) {
	// begin-user-code

// Listener routines are not used in the C++ implementation, but these lines are
// left here just in case.

//	// Only register listeners that are not null.
//	if (listener != null)
//	listeners.add(listener);

	return;
	// end-user-code
}
std::string SFRComponent::toString() {
	// begin-user-code

	// Return the component's name.
	return name;
	// end-user-code
}
std::shared_ptr<Identifiable> SFRComponent::clone() {
    // begin-user-code

    // Create a shared_ptr to an SFRComponent with the copy constructor.
    std::shared_ptr<SFRComponent> component = std::make_shared<SFRComponent>(*this);

    // Return the component
    return std::dynamic_pointer_cast<Identifiable>(component);
    // end-user-code
}
