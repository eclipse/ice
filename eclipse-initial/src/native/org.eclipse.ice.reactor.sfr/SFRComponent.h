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
#ifndef SFRCOMPONENT_H
#define SFRCOMPONENT_H
#include <updateableComposite/Component.h>
#include <IDataProvider.h>
#include <IData.h>
#include <string>
#include "FeatureSet.h"
#include "SFRData.h"
#include "ISFRComponentVisitor.h"
#include <updateableComposite/IComponentListener.h>
#include <ICEObject/Identifiable.h>
#include <memory>
#include <map>

using namespace ICE_DS;

namespace ICE_SFReactor {

// The SFRComponent class represents all reactor components that can be stored
// in a SFRComposite. This class should be treated as a base class where
// identifying pieces of information are stored on this object. This class also
// implements the IDataProvider interface in order to provide state point data
// capabilities for individual, unique instances of this extended class. This
// class implements the ICE Component interface.
class SFRComponent: virtual public ICE_DS::Component,
		virtual public ICE_DS::IDataProvider {

private:

	// Name of the SFRComponent.
	std::string name;

	// Description of the SFRComponent.
	std::string description;

	// ID of the SFRComponent; cannot be less than zero.
	int id;

	// A description of the source of information for this provider and its data.
	std::string sourceInfo;

	// The current time step. Can not be less than 0, and must be strictly less
	// than the number of TimeSteps. Defaults to 0.
	double time;

	// The time unit.
	std::string timeUnits;

	// A Map implementation of IData and features. Keep in mind that there can
	// be multiple IData for the same feature.
	std::map<double, std::map<std::string, std::shared_ptr<FeatureSet> > > dataTree;

	// An std::vector of ICE IComponentListeners that should be notified when the
	// Component has changed.
	std::vector<std::shared_ptr<IComponentListener> > listeners;

public:

	// Nullary constructor.
	SFRComponent();

	// Parameterized constructor with name specified.
	SFRComponent(std::string name);

	// Deep copies the contents of the object.
	SFRComponent(const SFRComponent & component);

	// Returns the name of the SFRComponent as a std::string.
	std::string getName() const;

	// Returns the description of the SFRComponent as a std::string.
	std::string getDescription() const;

	// Returns the ID of the SFRComponent as an int.
	int getId() const;

	// Returns the source information as a string.
	std::string getSourceInfo();

	// Returns the current time as a double.
	double getCurrentTime();

	// Returns an std::vector of strings representing the names of all features
	// contained in the SFRComponent's dataTree.
	std::vector<std::string> getFeatureList();

	// Returns the total number of time steps contained in the SFRComponent's dataTree.
	int getNumberOfTimeSteps();

	// Returns all IData corresponding to the specified features at the current
	// time. Will return an empty vector if the feature at the current time
	// contains no data.
	std::vector<std::shared_ptr<IData> > getDataAtCurrentTime(std::string feature);

	// Returns an std::vector of strings representing all features found in the
	// SFRComponent's dataTree, at the current time.
	std::vector<std::string> getFeaturesAtCurrentTime();

	// Returns an std::vector of doubles representing all times found in the
	// SFRComponent's dataTree.
	std::vector<double> getTimes();

	// Returns an integer representing the time step associated to the specified
	// time. Returns -1 if no time step is found.
	int getTimeStep(double time);

	// Returns the time units as a string.
	std::string getTimeUnits();

	// Sets the name of the SFRComponent.
	void setName(std::string name);

	// Sets the ID of the SFRComponent.
	void setId(int id);

	// Sets the description of the SFRComponent.
	void setDescription(std::string description);

	// Sets the source information.
	void setSourceInfo(std::string sourceInfo);

	// Sets the time units.
	void setTimeUnits(std::string timeUnits);

	// Sets the current time.
	void setTime(double step);

	// Adds an IData piece, keyed on the feature and time, to the dataTree. If
	//the feature exists in the tree, it will append to the end of the list.
	void addData(std::shared_ptr<SFRData> data, double time);

	// Removes the feature and all associated IData from the dataTree at all
	// time steps. If a user wishes to remove a single piece of IData from the
	// tree, then use the appropriate getData operation on that feature and
	// manipulate the data that way.
	void removeDataFromFeature(std::string feature);

	// Accepts an IComponentVisitor that can visit the SFRComponent to ascertain
	// its type and perform various type-specific operations.
	void accept(const ISFRComponentVisitor & visitor);

	// Dummy implementation of IComponentVisitor acceptance operation that
	// doesn't need to do anything.
	void accept(std::shared_ptr<ICE_DS::IComponentVisitor> visitor) {
		return;
	};

	// This operation notifies the listeners of the Component that its data
	// state has changed.
	void notifyListeners();

	// Deep copies and returns a newly instantiated object.
	virtual std::shared_ptr<Identifiable> clone();

	// Compares the contents of objects and returns true if they are identical,
	// otherwise returns false.
	virtual bool operator==(const SFRComponent & otherComp);

	// This operation notifies a class that has implemented IUpdateable that
	// the value associated with the particular key has been updated.
	void update(std::string updatedKey, std::string newValue);

	// This operation registers a listener that realizes the IComponentListener
	// interface with the Component so that it can receive notifications of
	// changes to the Component, if the Component publishes such information.
	void registerListener(std::shared_ptr<IComponentListener> listener);

	// Gets a std::string representation of the SFRComponent.
	std::string toString();
};
//end class SFRComponent

}// end namespace
#endif
