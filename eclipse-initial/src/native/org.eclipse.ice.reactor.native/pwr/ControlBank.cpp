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

#include "ControlBank.h"
#include "../HDF5LWRTagType.h"
#include <string>
#include <vector>
#include <HdfReaderFactory.h>
#include <HdfWriterFactory.h>
#include <H5Cpp.h>
#include "../LWRComponent.h"

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
ControlBank::ControlBank(ControlBank & arg) : LWRComponent(arg) {

    // begin-user-code

    this->stepSize = arg.stepSize;
    this->maxNumberOfSteps = arg.maxNumberOfSteps;
    // end-user-code

}

/**
 * The Destructor
 */
ControlBank::~ControlBank() {
    //TODO Auto-generated method stub
}

/**
 * The nullary Constructor.
 */
ControlBank::ControlBank() {

    // begin-user-code

    ControlBank::setMaxNumberOfSteps(1);
    ControlBank::setStepSize(0.0);
    ControlBank::setName("ControlBank 1");
    ControlBank::setDescription("Default Control Bank");
    ControlBank::setId(1);
    this->HDF5LWRTag = CONTROL_BANK;

    // end-user-code
}

/**
 * A parameterized Constructor.
 *
 * @param the name
 * @param the stepSize
 * @param the maxNumberOfSteps
 */
ControlBank::ControlBank(const std::string name, double stepSize, int maxNumberOfSteps) {

    // begin-user-code
    this->name = "ControlBank 1";
    ControlBank::setMaxNumberOfSteps(maxNumberOfSteps);
    ControlBank::setStepSize(stepSize);
    ControlBank::setName(name);
    ControlBank::setDescription("Default Control Bank");
    ControlBank::setId(1);
    this->HDF5LWRTag = CONTROL_BANK;

    // end-user-code

}

/**
 * Sets the axial step size.
 *
 * @param the stepSize
 */
void ControlBank::setStepSize(double stepSize) {

    // begin-user-code
    if (stepSize > 0.0) {
        this->stepSize = stepSize;
    } else {
        this->stepSize = 0.0;
    }

    return;

    // end-user-code

}

/**
 * Sets the maximum number of axial steps.
 *
 * @param the maxNumberOfSteps to set
 */
void ControlBank::setMaxNumberOfSteps(int maxNumberOfSteps) {

    // begin-user-code

    //IF the maxSteps passed are not correct, return
    if (maxNumberOfSteps > 0) {

        this->maxNumberOfSteps = maxNumberOfSteps;
    } else {

        this->maxNumberOfSteps = 1;
    }

    // end-user-code
    return;
}

/**
 * Returns the maximum number of axial steps.
 *
 * @return the maxNumberOfSteps
 */
int ControlBank::getMaxNumberOfSteps() {

    // begin-user-code

    return this->maxNumberOfSteps;

    // end-user-code
}

/**
 * Returns the axial step size.
 *
 * @return the stepSize
 */
double ControlBank::getStepSize() {

    // begin-user-code

    return this->stepSize;

    // end-user-code

}

/**
 * Calculates and returns the stroke length, which is the axial step size multiplied by the maximum number of axial steps.
 *
 * @return the stroke length
 */
double ControlBank::getStrokeLength() {

    //begin-user-code

    return ((double) this->maxNumberOfSteps) * this->stepSize;

    // end-user-code
}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool ControlBank::operator==(const ControlBank& otherObject) const {

    return LWRComponent::operator ==(otherObject) && this->maxNumberOfSteps == otherObject.maxNumberOfSteps && this->stepSize == otherObject.stepSize;
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> ControlBank::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<ControlBank> component(new ControlBank (*this));

    // Return the newly instantiated object
    return component;

    // end-user-code

}

/**
 * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
 *
 * @param The File
 * @param The Group
 *
 * @return True if successful, false otherwise
 */
bool ControlBank::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    bool flag = true;

    flag &= LWRComponent::writeAttributes(h5File, h5Group);
    flag &= ICE_IO::HdfWriterFactory::writeDoubleAttribute(h5File, h5Group, "stepSize", this->stepSize);
    flag &= ICE_IO::HdfWriterFactory::writeIntegerAttribute(h5File, h5Group, "maxNumberOfSteps", this->maxNumberOfSteps);

    return flag;


    // end-user-code

}

/**
 * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
 *
 * @param  The Group
 *
 * @return True if successful, false otherwise
 */
bool ControlBank::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    //Local attributes (so we only call read ONCE)
    //These will clear out by the garbage collector
    double stepSize;
    int maxNumberOfSteps;

    //Check super
    if(h5Group.get() == NULL) {
        return false;
    }

    //Get the information.  If any fail out, return false and do not change data.
    try {
        if (LWRComponent::readAttributes(h5Group) == false) return false;
        stepSize = ICE_IO::HdfReaderFactory::readDoubleAttribute(h5Group, "stepSize");
        maxNumberOfSteps = ICE_IO::HdfReaderFactory::readIntegerAttribute(h5Group, "maxNumberOfSteps");
    } catch (...) {
        return false;
    }

    //Set the primitive data
    this->stepSize = stepSize;
    this->maxNumberOfSteps = maxNumberOfSteps;

    return true;

    // end-user-code
}
