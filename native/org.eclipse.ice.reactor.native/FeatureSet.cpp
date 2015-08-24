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

#include "FeatureSet.h"
#include <vector>
#include <memory>
#include <string>
#include "IData.h"
#include "UtilityOperations.h"
#include <iostream>

using namespace ICE_Reactor;

/**
 * Copy constructor
 *
 * @param Object to copy from
 */
FeatureSet::FeatureSet(FeatureSet & arg) {

    //copy contents (Deep)
    this->name = arg.name;

    this->iData.clear();

    //Deep copy iData
    for(int i = 0; i < arg.iData.size(); i++) {
        //Dynamically cast as a LWRData and add it.
        std::shared_ptr <LWRData> data( new LWRData(*(dynamic_cast <LWRData *> (arg.iData.at(i).get()))));
        //Clone it
        //std::shared_ptr<LWRData> cloned

        //Copy back as IData?


        this->iData.push_back(data);
    }


}

/**
 * Destructor
 */
FeatureSet::~FeatureSet() {

}

/**
 * The constructor. The passed value must be a valid feature set, otherwise it will set the feature name to null and not allow the addition of any IData.
 */
FeatureSet::FeatureSet(const std::string feature) {

    //Initialize the iData vector.
    this->iData;

    //Trim the feature, and set it up
    std::string subStr = feature;
    std::string str = UtilityOperations::trim_copy(subStr, " \f\n\r\t\v");

    //Set name
    this->name = str;


}

/**
 * Returns the name of the feature.
 *
 * @return name of the feature
 */
const std::string FeatureSet::getName() {

    // begin-user-code

    return this->name;

    // end-user-code

}

/**
 * Returns the IDatas associated with the FeatureSet.
 *
 * @return list of IData.
 */
std::vector< std::shared_ptr<IData> > FeatureSet::getIData() {

    // begin-user-code

    return this->iData;

    // end-user-code
}

/**
 * Adds IData to the list within the feature set.  The name of the feature must match the name set on the FeatureSet, otherwise this operation will fail.  Returns true if operation was successful, false otherwise.
 *
 * @param the IData to add
 */
bool FeatureSet::addIData(std::shared_ptr<IData> iData) {

    // begin-user-code

    //If the iData is not null and the feature name is equal to the iData's feature, add the piece to the IData.
    if (iData.get()->getFeature().compare(this->name) == 0) {

        //Add the iData
        this->iData.push_back(iData);

        //Operation was successful
        return true;
    }

    // Failed
    return false;

    // end-user-code

}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param the object to check obj with
 *
 * @return true if equal, false otherwise
 */
bool FeatureSet::operator ==(const FeatureSet &other) const {

    // begin-user-code

    //Local Declarations
    bool retVal = false;

    //Check values
    retVal = (this->name.compare(other.name) == 0);

    //Iterate over the vector to check values
    if(this->iData.size() == other.iData.size()) {
        for(int i = 0; i < this->iData.size(); i++) {
            //Compare each value uniquely
            retVal &=this->iData.at(i).get()->operator==(*(other.iData.at(i).get()));
        }
    } else {
        return false;
    }

    //Return retVal
    return retVal;

    // end-user-code

}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return a newly instantiated object.
 */
std::shared_ptr<FeatureSet> FeatureSet::clone() {

    // begin-user-code

    //Local Declarations
    std::shared_ptr<FeatureSet> set (new FeatureSet(*this));

    //Return the newly instantiated object
    return set;

}

/**
 * Removes data from the feature based upon index
 *
 * @param the index to remove
 *
 * @return true if removed, false otherwise
 */
bool FeatureSet::removeIDataAtIndex(int index) {

    //If the index is valid
    if(index < this->iData.size() && index >=0) {

        //Remove index
        this->iData.erase(this->iData.begin()+index);
        return true;
    }

    return false;
}
