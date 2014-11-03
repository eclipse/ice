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
#include "LWRDataProvider.h"
#include <map>
#include <memory>
#include <stdexcept>
#include <iostream>
#include <string>
#include "UtilityOperations.h"
#include "FeatureSet.h"
#include "LWRData.h"
#include <algorithm>


using namespace ICE_Reactor;
/**
 * Copy constructor
 */
LWRDataProvider::LWRDataProvider(LWRDataProvider & arg) {

	//begin-user-code

	//Copy contents
	this->sourceInfo = arg.sourceInfo;
	this->time = arg.time;
	this->timeUnit = arg.timeUnit;

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

	//end-user-code

}

/**
 * The constructor.
 */
LWRDataProvider::LWRDataProvider() {
	//begin-user-code

    //Setup TreeMap
    this->dataTree;

    //Setup Source
    this->sourceInfo = "No Source Available";

    //Setup time
    this->time = 0;
    this->timeUnit = "seconds";

	//HDF5 attribute names
	this->dataH5GroupName = "Positions";
	this->timeStepNamePrefix = "TimeStep: ";

	//end-user-code
}

LWRDataProvider::~LWRDataProvider() {

}

/**
 * <!-- begin-UML-doc -->
 * <p>Sets the sourceInfo.  Can not be null or the empty string.  Strings passed will be trimmed before being set.</p>
 * <!-- end-UML-doc -->
 * @param sourceInfo <p>The sourceInfo to set.</p>
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
void LWRDataProvider::setSourceInfo(std::string sourceInfo) {

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
 * <!-- begin-UML-doc -->
 * <p>Adds a IData piece, keyed on the feature and timeStep, to the dataTree. If the feature exists in the tree, it will append to the end of the list.</p>
 * <!-- end-UML-doc -->
 * @param data <p>The data to add.</p>
 * @param time
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
void LWRDataProvider::addData(std::shared_ptr<LWRData> data, double time) {

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

    return;

    // end-user-code

}

/**
 * <!-- begin-UML-doc -->
 * <p>Removes the feature and all associated IData from the dataTree at all time steps. If a user wishes to remove a single piece of IData from the tree, then use the appropriate getData operation on that feature and manipulate the data that way.</p>
 * <!-- end-UML-doc -->
 * @param feature <p>The feature.</p>
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
void LWRDataProvider::removeAllDataFromFeature(std::string feature) {

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
 * <!-- begin-UML-doc -->
 * <p>Deep copies and returns a newly instantiated object.</p>
 * <!-- end-UML-doc -->
 * @return <p>The newly instantiated copied object.</p>
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::shared_ptr<LWRDataProvider> LWRDataProvider::clone() {

    // begin-user-code

    //Local Declarations
    std::shared_ptr<LWRDataProvider> component (new LWRDataProvider(*this));

    //Return the component
    return component;

    // end-user-code
}

/**
 * <!-- begin-UML-doc -->
 * <p>Sets the time units.</p>
 * <!-- end-UML-doc -->
 * @param timeUnit <p>The time unit to be set.</p>
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
void LWRDataProvider::setTimeUnits(std::string timeUnit) {

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
 * <!-- begin-UML-doc -->
 * <p>Equality check.  Returns true if equals, false otherwise.</p>
 * <!-- end-UML-doc -->
 * @param otherObject <p>Object to equate.</p>
 * @return <p>True if equal, false otherwise.</p>
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
bool LWRDataProvider::operator==(const LWRDataProvider & other) const {

    bool retVal = false;


    retVal = (this->time == other.time
             && (this->timeUnit.compare(other.timeUnit) == 0)
             && (this->sourceInfo.compare(other.sourceInfo)==0));


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
 * (non-Javadoc)
 * @see IDataProvider#getFeatureList()
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::vector<std::string> LWRDataProvider::getFeatureList() {

	//begin-user-code

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

    //end-user-code
}

/**
 * (non-Javadoc)
 * @see IDataProvider#getNumberOfTimeSteps()
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
int LWRDataProvider::getNumberOfTimeSteps() {

	//begin-user-code

    //Return the size of the tree
    return this->dataTree.size();

    //end-user-code

}

/**
 * (non-Javadoc)
 * @see IDataProvider#setTime(double step)
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
void LWRDataProvider::setTime(double step) {

	//begin-user-code

    if(step >= 0) {
        this->time = step;
    }

    //end-user-code

}

/**
 * (non-Javadoc)
 * @see IDataProvider#getDataAtCurrentTime(String feature)
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::vector< std::shared_ptr<IData> > LWRDataProvider::getDataAtCurrentTime(const std::string feature) {

	//begin-user-code

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

	//end-user-code
}

/**
 * (non-Javadoc)
 * @see IDataProvider#getSourceInfo()
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::string LWRDataProvider::getSourceInfo() {

	//begin-user-code

    return this->sourceInfo;

    //end-user-code

}

/**
 * (non-Javadoc)
 * @see IDataProvider#getFeaturesAtCurrentTime()
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::vector<std::string> LWRDataProvider::getFeaturesAtCurrentTime() {

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
 * (non-Javadoc)
 * @see IDataProvider#getTimes()
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::vector<double> LWRDataProvider::getTimes() {

	//begin-user-code

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

    //end-user-code
}

/**
 * (non-Javadoc)
 * @see IDataProvider#getTimeStep(double time)
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
int LWRDataProvider::getTimeStep(double time) {

	//begin-user-code

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

    //end-user-code
}

/**
 * (non-Javadoc)
 * @see IDataProvider#getTimeUnits()
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
std::string LWRDataProvider::getTimeUnits() {

	//begin-user-code

    return this->timeUnit;

    //end-user-code
}

/**
 * <!-- begin-UML-doc -->
 * <p>Returns the current time step.</p>
 * <!-- end-UML-doc -->
 * @return <p>The current time step.</p>
 * @generated "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
double LWRDataProvider::getCurrentTime() {

    // begin-user-code

    return this->time;

    // end-user-code
}
