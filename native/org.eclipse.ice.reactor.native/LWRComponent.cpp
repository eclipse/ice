/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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

#include "LWRComponent.h"
#include "UtilityOperations.h"
#include "HDF5LWRTagType.h"
#include "FeatureSet.h"
#include <string>
#include "LWRData.h"
#include "HDF5LWRTagType.h"
#include <updateableComposite/Component.h>
#include "IDataProvider.h"
#include <IHdfWriteable.h>
#include <IHdfReadable.h>
#include <ICEObject/Identifiable.h>
#include <map>
#include <memory>
#include <stdexcept>
#include <iostream>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <sstream>
#include <stddef.h>
#include <stdio.h>
#include <string.h>
#include <algorithm>

using namespace ICE_Reactor;
/**
 * Copy Constructor
 */
LWRComponent::LWRComponent(LWRComponent & arg) {

    //Copy contents

    this->name = arg.name;
    this->id = arg.id;
    this->description = arg.description;
    this->sourceInfo = arg.sourceInfo;
    this->time = arg.time;
    this->timeUnit = arg.timeUnit;
    this->HDF5LWRTag = arg.HDF5LWRTag;

    //Deep copy tree
    this->dataTree.clear();
    std::map<double, std::vector<std::shared_ptr <FeatureSet> > >::iterator mapIter;


    //Iterate the tree
    for(mapIter = arg.dataTree.begin(); mapIter != arg.dataTree.end(); ++mapIter) {

        std::vector<std::shared_ptr < FeatureSet > >::iterator vIter;

        std::vector<std::shared_ptr < FeatureSet > > vectorList;
        //Iterate the list
        for(int i = 0; i < arg.dataTree.at(mapIter->first).size(); i++) {
            vectorList.push_back(arg.dataTree.at(mapIter->first)[i]);
        }
        //Put it in the tree
        this->dataTree.insert( std::pair<double, std::vector< std::shared_ptr <FeatureSet> > >(mapIter->first, vectorList) );
    }

    //Compare type
    this->HDF5LWRTag = arg.HDF5LWRTag;

    //HDF5 attribute names
    this->dataH5GroupName = "State Point Data";
    this->timeStepNamePrefix = "TimeStep: ";
}

/**
 * The Nullary Constructor
 */
LWRComponent::LWRComponent() {

    // begin-user-code

    //Setup default values
    this->name = "Component 1";
    this->id = 0;
    this->description = "Component 1's Description";

    //Setup listeners
    //this->listeners;

    //Setup TreeMap
    this->dataTree;

    //Setup Source
    this->sourceInfo = "No Source Available";

    //Setup time
    this->time = 0;
    this->timeUnit = "seconds";

    //Setup the HDF5LWRTagType to correct type
    this->HDF5LWRTag = LWRCOMPONENT;

    //HDF5 attribute names
    this->dataH5GroupName = "State Point Data";
    this->timeStepNamePrefix = "TimeStep: ";

    // end-user-code

}

/**
 * The Destructor
 */
LWRComponent::~LWRComponent() {
    //TODO Auto-generated method stub
}

/**
 * A parameterized Constructor.
 *
 * @param the name
 */
LWRComponent::LWRComponent(const std::string name) {

    // begin-user-code

    //Setup default values
    this->name = "Component 1";
    this->id = 0;
    this->description = "Component 1's Description";

    //Setup listeners
    //this->listeners;

    //Setup TreeMap
    this->dataTree;

    //Setup Source
    this->sourceInfo = "No Source Available";

    //Setup time
    this->time = 0;
    this->timeUnit = "seconds";

    //Setup the HDF5LWRTagType to correct type
    this->HDF5LWRTag = LWRCOMPONENT;

    //Setup name
    LWRComponent::setName(name);

    //HDF5 attribute names
    this->dataH5GroupName = "State Point Data";
    this->timeStepNamePrefix = "TimeStep: ";

    // end-user-code

}

/**
 * Overrides the equals operator
 *
 * @param The object to compare to
 *
 * @return true if equal, false otherwise
 */
bool LWRComponent::operator==(const LWRComponent &other) const {

    bool retVal = false;


    retVal = (this->name.compare(other.name) == 0)
             && (this->description.compare(other.description) == 0)
             && this->id == other.id
             && this->time == other.time
             && (this->timeUnit.compare(other.timeUnit) == 0)
             && this->HDF5LWRTag == other.HDF5LWRTag
             && (this->sourceInfo.compare(other.sourceInfo)==0);


    //Manually override dataTree comparisons.  Limitation due to SMART POINTERS!
    if(this->dataTree.size() == other.dataTree.size() && retVal == true) {

        std::map<double, std::vector< std::shared_ptr <FeatureSet> > >::const_iterator iter = this->dataTree.begin();

        //Iterate over the list of all timesteps and remove all the features
        for (iter=this->dataTree.begin(); iter!=this->dataTree.end(); ++iter) {

            double time_iter = iter->first;

            //Iterate over the vector
            if(this->dataTree.at(time_iter).size() == other.dataTree.at(time_iter).size()) {

                for (int i = 0; i < this->dataTree.at(time_iter).size(); i++) {
                    //If the vectors are not equal, return
                    if(this->dataTree.at(time_iter).at(i).get()->operator ==(*other.dataTree.at(time_iter).at(i).get()) == false) {
                        return false;
                    }

                }
            } else {
                return false;
            }

        }
    } else return false;


    return retVal;
}

/**
 * Sets the sourceInfo.  Can not be null or the empty string.  std::strings passed will be trimmed before being set.
 *
 * @param the sourceInfo to set
 */
void LWRComponent::setSourceInfo(const std::string sourceInfo) {

    // begin-user-code

    //Get copy of trimmed string
    std::string str = sourceInfo;
    std::string trimmedString = UtilityOperations::trim_copy(str, " \f\n\r\t\v");

    //Set the name if valid
    if(trimmedString.compare("") != 0) {
        this->sourceInfo = trimmedString;
    }

    return;

    // end-user-code

}

/**
 * Adds a IData piece, keyed on the feature and timeStep, to the dataTree. If the feature exists in the tree, it will append to the end of the list.
 *
 * @param The LWRData
 * @param The time
 */
void LWRComponent::addData(std::shared_ptr<LWRData> data, double time) {

    // begin-user-code

    //Local Declarations
    std::vector<std::shared_ptr<FeatureSet> > featureSetList1;
    bool featureExistsFlag = false;
    bool outOfRange = false;

    //Return if the passed parameters are incorrect
    if (time < 0) {
        return;
    }
    try {
        featureSetList1 = this->dataTree.at(time);
    } catch (const std::out_of_range& oor) {
        outOfRange = true;
    }

    //If the timestep does not exist in the list, add it to the list
    if (outOfRange == true) {
        std::vector<std::shared_ptr<FeatureSet> > list;
        std::shared_ptr<FeatureSet> set( new FeatureSet(data.get()->getFeature()) );
        set.get()->addIData(data);
        list.push_back(set);

        this->dataTree.insert( std::pair<double, std::vector< std::shared_ptr <FeatureSet> > >(time, list) );
        //this->notifyListeners();

    }

    //Otherwise, the timestep exists and needs to append to the current running list
    else {

        //Append to the current FeatureSet if it exists
        for (int i = 0; i < this->dataTree.at(time).size(); i++) {
            //IF the featureset is the same name as the data, add it
            if (this->dataTree.at(time).at(i).get()->getName().compare(data.get()->getFeature()) == 0) {
                this->dataTree.at(time).at(i).get()->addIData(data);
                featureExistsFlag = true;
                //this->notifyListeners();
                break;
            }
        }

        //If the featureSet does not exist, then add it
        if (featureExistsFlag == false) {
            //If it does not, add it
            std::shared_ptr<FeatureSet> set (new FeatureSet(data.get()->getFeature()));
            set.get()->addIData(data);
            //Add it to the list of sets
            this->dataTree.at(time).push_back(set);
            //this->notifyListeners();
        }

    }

    // end-user-code
    return;
}

/**
 * Removes the feature and all associated IData from the dataTree at all time steps. If a user wishes to remove a single piece of IData from the tree, then use the appropriate getData operation on that feature and manipulate the data that way.
 *
 * @param The feature
 */
void LWRComponent::removeAllDataFromFeature(const std::string feature) {

    // begin-user-code

    //Local Declarations
    bool updated = false;

    //Get the iterator
    std::map<double, std::vector< std::shared_ptr <FeatureSet> > >::iterator iter = this->dataTree.begin();

    //Iterate over the list of all timesteps and remove all the features
    for (iter=this->dataTree.begin(); iter!=this->dataTree.end(); ++iter) {
        std::vector<std::shared_ptr< FeatureSet> > list = this->dataTree.at(iter->first);

        //If the FeatureSet with the feature name exists, remove it
        for (int i = 0; i < list.size(); i++) {
            if (list.at(i).get()->getName().compare(feature) == 0) {
                list.erase(list.begin()+i);
                updated = true;
                break; //Break from for loop, otherwise really bad things will happen
            }
        }
    }

    //Notify listeners if updated
    if (updated) {
        //Notify listeners
        //this->notifyListeners();
    }

    // end-user-code
    return;
}

/**
 * Returns the current time step.
 *
 * @return the current time
 */
double LWRComponent::getCurrentTime() {

    // begin-user-code

    return this->time;

    // end-user-code*/
}

/**
 * Returns the LWRComponentType.
 *
 * @return The TagType
 */
HDF5LWRTagType LWRComponent::getHDF5LWRTag() {

    // begin-user-code

    return this->HDF5LWRTag;

    // end-user-code

}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @returns the newly instantiated object.
 */
std::shared_ptr<ICE_DS::Identifiable> LWRComponent::clone() {

    // begin-user-code

    //Local Declarations
    std::shared_ptr<LWRComponent> component (new LWRComponent(*this));

    //Return the component
    return component;

    // end-user-code
}

/**
 * Sets the time units.
 *
 * @param the time units
 */
void LWRComponent::setTimeUnits(const std::string timeUnit) {

    // begin-user-code

    //Get copy of trimmed string
    std::string str = timeUnit;
    std::string trimmedString = UtilityOperations::trim_copy(str, " \f\n\r\t\v");

    //If it is valid
    if(trimmedString.compare("") != 0) {
        this->timeUnit = trimmedString;
    }

    return;

    // end-user-code

}

/**
 * This operation retrieves the identification number of the Identifiable entity.
 *
 * @return The Id
 */
int LWRComponent::getId() const {
    return this->id;

    // end-user-code
}

/**
 * This operation retrieves the name of the Identifiable entity.
 *
 * @return the name
 */
std::string LWRComponent::getName() const {

    // begin-user-code

    return this->name;

    // end-user-code

}

/**
 * This operation retrieves the description of the Identifiable entity.
 *
 * @return The Description
 */
std::string LWRComponent::getDescription() const {

    // begin-user-code

    return this->description;

    // end-user-code

}

/**
 * This operation sets the identification number of the Identifiable entity. It must be greater than zero.
 *
 * @param The id to set
 */
void LWRComponent::setId(int id) {

    // begin-user-code


    //If the id is greater than 0, set it
    if(id >= 0) {
        this->id = id;
    }

    return;

    // end-user-code

}

/**
 * This operation sets the name of the Identifiable entity.
 *
 * @param The name to set
 */
void LWRComponent::setName(const std::string name) {

    // begin-user-code

    //Get copy of trimmed string
    std::string str = name;
    std::string trimmedString = UtilityOperations::trim_copy(str, " \f\n\r\t\v");

    //Set the name if valid
    if(trimmedString.compare("") != 0) {
        this->name = trimmedString;
    }

    return;

    // end-user-code

}

/**
 * This operation sets the description of the Identifiable entity.
 *
 * @param the description
 */
void LWRComponent::setDescription(const std::string description) {

    // begin-user-code

    //Get copy of trimmed string
    std::string str = description;
    std::string trimmedString = UtilityOperations::trim_copy(str, " \f\n\r\t\v");

    //Set the name if valid
    if(trimmedString.compare("") != 0) {
        this->description = trimmedString;
    }

    return;

    // end-user-code

}

/**
 * This operation returns the list of features available across all time steps (pin-power, temperature, etc).
 *
 * @return The list of features
 */
std::vector<std::string> LWRComponent::getFeatureList() {

    //Local Declarations
    std::vector<std::string> stringList;


    //Iterate over list
    std::map<double, std::vector< std::shared_ptr <FeatureSet> > >::iterator iter;

    //Iterate over the list of all timesteps and get specific features.
    for (iter=this->dataTree.begin(); iter!=this->dataTree.end(); ++iter) {

        //Get the stored list
        std::vector<std::shared_ptr< FeatureSet > > features (iter->second);

        //Iterate over the featureSet list and add names to stringList
        for(int i = 0; i < features.size(); i++) {
            //If the string name does not already exist
            if(std::find(stringList.begin(), stringList.end(), features.at(i).get()->getName())== stringList.end()) {
                //Add it
                stringList.push_back(features.at(i).get()->getName());
            }
        }
    }



    return stringList;
}

/**
 * This operation returns the total number of time steps.
 *
 * @return the number of time steps
 */
int LWRComponent::getNumberOfTimeSteps() {

    //Return the size of the tree
    return this->dataTree.size();

}

/**
 * This operation sets the current time step for which data should be retrieved.  It is 0-indexed such that time step 0 is the initial state and time step 1 is the state after the first time step.  This operation should be called to set the current time step before data is retrieved from the provider.  The provider will always default to the initial state.
 *
 * @param the step
 */
void LWRComponent::setTime(double step) {

    if(step >= 0) {
        this->time = step;
    }

}

/**
 * This operation returns all of the data (as IData[*]) related to a particular feature for this provider at a specific time step.  This operation will return null if no data is available and such a situation will most likely signify an error.
 *
 * @param The feature
 *
 * @return the list of IData at the feature
 */
std::vector<std::shared_ptr<IData> > LWRComponent::getDataAtCurrentTime(const std::string feature) {
    std::vector<std::shared_ptr<IData> > data;
    std::vector<std::shared_ptr< FeatureSet >  > featureSetList;
    //Check to see if it exists
    try {
        featureSetList = this->dataTree.at(this->time);
    } catch(const std::out_of_range) {
        return data; //Return empty
    }

    for(int i = 0; i < featureSetList.size(); i++) {
        if(featureSetList.at(i).get()->getName().compare(feature) == 0) {
            return featureSetList.at(i).get()->getIData();
        }
    }

    //Nothing found, return empty
    return data;
}

/**
 * This operation is a description of the source of information for this provider and its data.
 *
 * @return the source info
 */
std::string LWRComponent::getSourceInfo() {
    return this->sourceInfo;
}

/**
 * Returns the list of features at the current time.
 *
 * @return the list of features
 */
std::vector<std::string> LWRComponent::getFeaturesAtCurrentTime() {

	//begin-user-code

	std::vector<std::string> string;

	try {
			this->dataTree.at(this->time);
		} catch(const std::out_of_range) {
		    return string; //Return empty
		}

	for(int i = 0; i < this->dataTree.at(this->time).size(); i++) {
		string.push_back(this->dataTree.at(this->time).at(i).get()->getName());
	}


	return string;

	//end-user-code

}

/**
 * Returns all the times in ascending order.
 *
 * @return the list of times
 */
std::vector<double> LWRComponent::getTimes() {
    std::vector<double> data;

    //Add the times to the data vector
    //Iterate over list
    std::map<double, std::vector< std::shared_ptr <FeatureSet> > >::iterator iter = this->dataTree.begin();

    //Iterate over the list of all timesteps
    for (iter=this->dataTree.begin(); iter!=this->dataTree.end(); ++iter) {
        //Add the time
        data.push_back(iter->first);
    }


    //return the list
    return data;
}

/**
 *  Returns the integer time based upon the time step.  Returns -1 if the time does not exist.
 *
 *  @param time
 */
int LWRComponent::getTimeStep(double time) {

    int counter = 0;

    //See if time exists
    try {
        this->dataTree.at(time);
    } catch (const std::out_of_range) {
        return -1;
    }

    //If this far, then the time does exist.  Iterate over list and return counter
    std::map<double, std::vector< std::shared_ptr <FeatureSet> > >::iterator iter = this->dataTree.begin();

    //Iterate over the list of all timesteps and remove all the features
    for (iter=this->dataTree.begin(); iter!=this->dataTree.end(); ++iter) {
        if(iter->first == time) {
            return counter;
        }
        counter++;
    }

    //Didnt find anything.
    return -1;
}

/**
 * Returns the time units.
 *
 * @return the time units
 */
std::string LWRComponent::getTimeUnits() {
    return this->timeUnit;
}

/**
 * This operation creates and returns a child H5Group for the parentH5Group in the h5File. If h5File is null or can not be opened, then null is returned. If parentH5Group is null, then null is returned. If an exception is thrown, then null is returned.
 *
 * @param File
 * @param Group
 *
 * @return the group
 */
std::shared_ptr<H5::Group> LWRComponent::createGroup(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> parenth5Group) {

    try {
        //create the group
        return ICE_IO::HdfWriterFactory::createH5Group(h5File, this->name, parenth5Group);
    } catch (...) {
        throw -1;
    }
}

/**
 * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
 *
 * @param The File
 * @param The Group
 *
 * @return True if successful, false otherwise
 */
bool LWRComponent::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {
    // begin-user-code

    //If the file or group is null, return false
    if(h5File == NULL || h5Group == NULL) return false;

    bool flag = true;

    //Write attributes
    flag &= ICE_IO::HdfWriterFactory::writeStringAttribute(h5File, h5Group,
            "HDF5LWRTag", UtilityOperations::toStringHDF5Tag(this->HDF5LWRTag));
    flag &= ICE_IO::HdfWriterFactory::writeStringAttribute(h5File, h5Group, "name",
            this->name);
    flag &= ICE_IO::HdfWriterFactory::writeIntegerAttribute(h5File, h5Group, "id",
            this->id);
    flag &= ICE_IO::HdfWriterFactory::writeStringAttribute(h5File, h5Group,
            "description", this->description);

    return flag;
    // end-user-code
}

/**
 * This operation writes HDF5 Datasets to the h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Datasets, then false is returned. Otherwise, true is returned.
 *
 * @param The File
 * @param The Group
 *
 * @return True if successful, false otherwise
 */
bool LWRComponent::writeDatasets(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {
    // begin-user-code


    //IF the passed parameters are NULL, return false;
    if(h5File == NULL || h5Group == NULL) {
        return false;
    }

    //Local Declarations
    hsize_t adims[] = { 3 };
    int counter = 0;
    std::string featureDSNames[] = { "value", "uncertainty", "units", "position" };

    //Get the HDF5Group for data
    std::shared_ptr<H5::Group> dataH5Group = ICE_IO::HdfWriterFactory::createH5Group(h5File, this->dataH5GroupName, h5Group);

    //Iterate over the dataTree and create timesteps for each key in the tree
    std::map<double, std::vector< std::shared_ptr <FeatureSet> > >::iterator iter;
    for(iter = this->dataTree.begin(); iter != this->dataTree.end(); ++iter) {

        //Get the time
        double time = iter->first;


        //Create a new timeStep group based on the prefix and the timeStep
        std::stringstream ss;
        ss << counter;
        std::string timeName = timeStepNamePrefix + "" + ss.str();
        std::shared_ptr<H5::Group> timeStepH5Group = ICE_IO::HdfWriterFactory::createH5Group(h5File, timeName, dataH5Group);

        //Add attributes to the timeStep group

        //Create attribute: time as Double
        ICE_IO::HdfWriterFactory::writeDoubleAttribute(h5File, timeStepH5Group,
                "time", time);

        //Create attribute: units as String
        ICE_IO::HdfWriterFactory::writeStringAttribute(h5File, timeStepH5Group, "units", this->timeUnit);

        //Create a Compound Dataset for each timeStep to represent the collection of FeatureSets.  This contains the list of Feature Sets
        for (int i = 0; i < this->dataTree.at(time).size(); i++) {


            //Get the number of IDatas stored on the FeatureSet
            int iDataSize = this->dataTree.at(time).at(i).get()->getIData().size();

            //Create the arrays for each dataSet
            double position[iDataSize * 3];
            int maxRowSize = 0;

            //Create stucts
            dataSet_t listOfStructs[this->dataTree.at(time).at(i).get()->getIData().size()];

            //Get the side of the units first
            int maxSize = 0;
            for(int k = 0; k < this->dataTree.at(time).at(i).get()->getIData().size(); k++) {
                std::shared_ptr<IData> iData = this->dataTree.at(time).at(i).get()->getIData().at(k);
                if(iData.get()->getUnits().size() > maxSize) maxSize = iData.get()->getUnits().size();
            }

            //Add for the null terminating string
            maxSize++;

            //Iterate over the IDatas to fill out the arrays listed above
            for (int j = 0; j < this->dataTree.at(time).at(i).get()->getIData().size(); j++) {
                //Get the iData at the location
                std::shared_ptr<IData> iData = this->dataTree.at(time).at(i).get()->getIData().at(j);

                //Add contents to list of struct

                //Copy contents of iData to the array
                listOfStructs[j].value = iData.get()->getValue();
                listOfStructs[j].uncertainty = iData.get()->getUncertainty();
                listOfStructs[j].units = new char[maxSize];

                //Copy units
                strcpy(listOfStructs[j].units, iData.get()->getUnits().c_str());

                //Add null terminating string
                listOfStructs[j].units[iData.get()->getUnits().size()] = '\0';
                /*listOfStructs[j].units[maxSize-1] = '\0';*/

                //Get the position
                std::vector<double> pos1 = iData.get()->getPosition();
                listOfStructs[j].position[0] = (pos1.at(0));
                listOfStructs[j].position[1] = (pos1.at(1));
                listOfStructs[j].position[2] = (pos1.at(2));

            }

            //This code represents the fixed string attempt at applying a compound dataset

            hid_t strType = H5Tcopy(H5T_C_S1);
            H5Tset_size(strType, H5T_VARIABLE);

            //Create the memory type
            hid_t dataSet_tid = H5Tcreate(H5T_COMPOUND, sizeof(dataSet_t));
            H5Tinsert(dataSet_tid, "value", HOFFSET(dataSet_t, value), ICE_IO::HdfWriterFactory::createFloatH5Datatype(h5File).getId());
            H5Tinsert(dataSet_tid, "uncertainty",  HOFFSET(dataSet_t, uncertainty), ICE_IO::HdfWriterFactory::createFloatH5Datatype(h5File).getId());
            H5Tinsert(dataSet_tid, "units", HOFFSET(dataSet_t, units) , strType);
            H5Tinsert(dataSet_tid, "position", HOFFSET(dataSet_t, position), H5Tarray_create2(ICE_IO::HdfWriterFactory::createFloatH5Datatype(h5File).getId(), 1, adims));

            //Create the space
            hsize_t dims[] = { 1, this->dataTree.at(time).at(i).get()->getIData().size() };
            int rank = 2;
            hid_t space = H5Screate_simple(rank, dims, NULL);

            //Create the dataset
            hid_t dataset = H5Dcreate(timeStepH5Group.get()->getId(), this->dataTree.at(time).at(i).get()->getName().c_str(), dataSet_tid, space, H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);

            //write dataset
            H5Dwrite(dataset, dataSet_tid, H5S_ALL, H5S_ALL, H5P_DEFAULT, &listOfStructs);

            //Free info
            for (int j = 0; j < this->dataTree.at(time).at(i).get()->getIData().size(); j++) {
                free(listOfStructs[j].units);
            }

        }

        //Iterate the counter
        counter++;
    }


    return true;
    // end-user-code

}

/**
 * This operation assigns the reference of the provided iHdfReadable to a class variable. If iHdfReadable is null, then false is returned. If iHdfReadable fails casting, then false is returned. Otherwise, true is returned.
 *
 * @param The child to be read
 *
 * @return True if successful, false otherwise
 */
bool LWRComponent::readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable) {

    //Return true
    return true;


}

/**
 * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
 *
 * @param  The Group
 *
 * @return True if successful, false otherwise
 */
bool LWRComponent::readAttributes(std::shared_ptr<H5::Group> h5Group) {
    //Local attributes (so we only call read ONCE)
    //These will clear out by the garbage collector
    std::string name, description;
    int id = -100000;

    //Get the information.  If any fail out, return false and do not change data.
    try {
        name = ICE_IO::HdfReaderFactory::readStringAttribute(h5Group, "name");
        description = ICE_IO::HdfReaderFactory::readStringAttribute(h5Group, "description");
        id = ICE_IO::HdfReaderFactory::readIntegerAttribute(h5Group, "id");
    } catch (...) {
        return false;
    }

    //Set the primitive data
    this->name = name;
    this->description = description;
    this->id = id;

    return true;
}

/**
 * This operation reads Datasets from h5Group and assigns their values to class variables. If h5Group is null or an Exception is thrown, false is returned. If the Otherwise, true is returned.
 *
 * @param The Group
 *
 * @return True if successful, false otherwise
 */
bool LWRComponent::readDatasets(std::shared_ptr<H5::Group> h5Group) {
    //Local Declarations
    int numOfTimeSteps = 0;
    std::shared_ptr<H5::Group> dataH5Group;

    //return if null
    if (h5Group == NULL)
        return false;

    //Get the dataH5Group.  If not found, return false!
    try {
        dataH5Group = ICE_IO::HdfReaderFactory::getChildH5Group(h5Group, this->dataH5GroupName);
    } catch (...) {
        return false;
    }

    try {
        //If there are time steps, iterate
        if(dataH5Group.get()->getNumObjs() > 0) {

            //Get the number of time steps
            numOfTimeSteps = dataH5Group.get()->getNumObjs();

            //Clear out the tree.  Very important!!!!


            //Iterate over each time step
            for(int i = 0; i < numOfTimeSteps; i++) {

                //Create the name of the timestep with incremental counter i
                std::stringstream ss;
                ss << i;
                std::string timeStepName = this->timeStepNamePrefix + ss.str();

                //Make sure it is a time step - get by name and increment by count. If not found, it will get caught
                std::shared_ptr<H5::Group> timeStepH5Group ( new H5::Group( dataH5Group.get()->openGroup(timeStepName)));

                //Get attributes
                double time = ICE_IO::HdfReaderFactory::readDoubleAttribute(timeStepH5Group, "time");
                std::string units = ICE_IO::HdfReaderFactory::readStringAttribute(timeStepH5Group, "units");

                //get the compound datasets.  First we need to figure out the size of these datasets, allocate a size on the stack, then iterate over
                //the lists in order to append to the map above.
                for(int j = 0; j < timeStepH5Group.get()->getNumObjs(); j++) {

                    //Set the name of the feature
                    std::string featureName = timeStepH5Group.get()->getObjnameByIdx(j);

                    //Get the dataset
                    hid_t dset =  H5Dopen(timeStepH5Group.get()->getId(), featureName.c_str(), H5P_DEFAULT);
                    //Get the space
                    hid_t space = H5Dget_space(dset);
                    //Get the Dimensions
                    hsize_t ndims[2];
                    H5Sget_simple_extent_dims(space, ndims, NULL);

                    //Setup the memory type and size of the position array
                    //Get the string variable taken care of
                    hsize_t adims[] = { 3 };

                    //Get data - prepare on stack
                    LWRComponent::dataSet_t rData [ndims[0]][ndims[1]];

                    //Retrieve data over the features
                    H5Dread(dset, H5Dget_type(dset), H5S_ALL, H5S_ALL, H5P_DEFAULT, rData);

                    //Iterate over the data.  Create LWRData and add to list with addData operation
                    for(int k = 0; k < ndims[0]; k++) {
                        for(int l = 0; l < ndims[1]; l++) {
                            //Create the data, set contents
                            std::shared_ptr<LWRData> data (new LWRData(featureName));
                            data.get()->setUncertainty(rData[k][l].uncertainty);
                            data.get()->setValue(rData[k][l].value);

                            //Setup position
                            std::vector<double> coordinates;
                            coordinates.push_back(rData[k][l].position[0]);
                            coordinates.push_back(rData[k][l].position[1]);
                            coordinates.push_back(rData[k][l].position[2]);
                            data.get()->setPosition(coordinates);

                            //Setup Name
                            std::string dataUnits(rData[k][l].units);
                            data.get()->setUnits(dataUnits);

                            //Add idata
                            LWRComponent::addData(data, time);
                            //Free units
                            free(rData[k][l].units);
                        }
                    }

                }
                //Close time group
                timeStepH5Group.get()->close();
            }

        }
        //Close dataH5Group
        dataH5Group.get()->close();
    } catch (...) {
        return false;
    }

    //Operation successful, return true!
    return true;
}


