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

#include "LWRData.h"
#include "IData.h"
#include <vector>
#include <memory>
#include <string>
#include "UtilityOperations.h"

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
LWRData::LWRData(const LWRData & arg) {

    // begin-user-code

    //Copy contents
    this->position.clear();
    //Deep copy position
    for (int i = 0; i < arg.position.size(); i++) {
        this->position.push_back(arg.position.at(i));
    }
    this->feature = arg.feature;
    this->uncertainty = arg.uncertainty;
    this->units = arg.units;
    this->value = arg.value;

    return;
    // end-user-code

}

/**
 * The Destructor
 */
LWRData::~LWRData() {
    //TODO Auto-generated method stub
}


LWRData::LWRData() {

    // begin-user-code

    //Local Declarations
    std::vector<double> positions(3);

    //Setup default values
    this->feature = "Feature 1";
    this->uncertainty = 0.0;
    this->units = "seconds";
    this->value = 0.0;

    //Setup position vector - zero them out (never trust the default value runtime in c++!)
    this->position = positions;
    this->position.at(0) = 0.0;
    this->position.at(1) = 0.0;
    this->position.at(2) = 0.0;

    // end-user-code

}

/**
 * A parameterized Constructor.
 */
LWRData::LWRData(const std::string feature) {

    // begin-user-code

    //Local Declarations
    std::vector<double> positions(3);

    //Setup default values
    this->feature = "Feature 1";
    this->uncertainty = 0.0;
    this->units = "seconds";
    this->value = 0.0;

    //Setup position vector - zero them out (never trust the default value runtime in c++!)
    this->position = positions;
    this->position.at(0) = 0.0;
    this->position.at(1) = 0.0;
    this->position.at(2) = 0.0;

    //Call feature
    LWRData::setFeature(feature);

    // end-user-code

}

/**
 * Sets the position of the LWRData. The passed parameter can not be null and must be equal to three dimensions (x, y, z coordinate plane and in that order for less than 3 dimensions). If working in less than 3 dimensions, the offset values should be set to 0.
 *
 * @param the positions
 */
void LWRData::setPosition( std::vector<double> position) {

    // begin-user-code

    //If the position is not 3, return
    if (position.size() == 3) {
        //Add all uniquely
        this->position.clear();
        //Iterate over the list and add each position
        for (int i = 0; i < position.size(); i++) {
            this->position.push_back(position.at(i));
        }
    }

    // end-user-code
    return;
}

/**
 * Sets the value.
 *
 * @param Sets the value
 */
void LWRData::setValue(double value) {

    // begin-user-code

    this->value = value;

    // end-user-code

    return;
}

/**
 * Sets the uncertainty.
 *
 * @param the uncertainty
 */
void LWRData::setUncertainty(double uncertainty) {

    // begin-user-code

    this->uncertainty = uncertainty;

    return;

    // end-user-code

}

/**
 * Sets the units. Can not be null or the empty string. std::strings are trimmed accordingly upon being set.
 *
 * @param the units to set
 */
void LWRData::setUnits(const std::string units) {

    // begin-user-code

    //Local Declaration
    std::string subStr = units;
    std::string str = UtilityOperations::trim_copy(subStr, " \f\n\r\t\v");

    //If the String is not null and it is not empty string (when trimmed) set accordingly
    if (str!="") {
        this->units = str;
    }
    return;

    // end-user-code

}

/**
 * Sets the feature.  Can not set to null or the empty string.  std::strings are trimmed accordingly upon being set.
 *
 * @param the feature to set
 */
void LWRData::setFeature(const std::string feature) {

    // begin-user-code

    //Local Declaration
    std::string subStr = feature;
    std::string str = UtilityOperations::trim_copy(subStr, " \f\n\r\t\v");

    //If the String is not null and it is not empty string (when trimmed) set accordingly
    if (str!="") {
        this->feature = str;
    }
    return;

    // end-user-code

}

/**
 * Equals operation.  Overrides IData's equality
 */
bool LWRData::operator==(const IData &other) const {

    // begin-user-code

    //Local Declarations
    bool retVal = false;
    double value = other.getValue();

    //Check values through IData
    retVal = this->position == other.getPosition()
             && this->value == other.getValue()
             && this->uncertainty == other.getUncertainty()
             && (this->units.compare(other.getUnits()) == 0) && (this->feature.compare(other.getFeature()) == 0);



    //Return retVal
    return retVal;

    // end-user-code

}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return newly instantiated object
 */
std::shared_ptr<IData> LWRData::clone() {
    // begin-user-code

    //Local Declarations
    std::shared_ptr<LWRData> data (new LWRData(*this));

    //Return the newly instantiated object
    return data;

    // end-user-code

}

/**
 * Overrides the IData implementation
 */
std::vector<double> LWRData::getPosition() const {
    return this->position;
}

/**
 * Overrides the IData implementation
 */
double LWRData::getValue() const {
    return this->value;
}

/**
 * Overrides the IData implementation
 */
double LWRData::getUncertainty() const {
    return this->uncertainty;
}

/**
 * Overrides the IData implementation
 */
std::string LWRData::getUnits() const {
    return this->units;
}

/**
 * Overrides the IData implementation
 */
std::string LWRData::getFeature() const {
    return this->feature;
}
