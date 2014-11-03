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

#include "LWReactor.h"
#include <ICEObject/Identifiable.h>
#include <IHdfWriteable.h>
#include <IHdfReadable.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>

using namespace ICE_Reactor;

/**
 * Copy constructor
 */
LWReactor::LWReactor(LWReactor & arg) : LWRComposite(arg) {

    this->size = arg.size;

}

/**
 * Destructor
 */
LWReactor::~LWReactor() {
    //TODO Auto-generated method stub
}

/**
 * A parameterized constructor.
 */
LWReactor::LWReactor(int size) {

    //Setup LWRComponent stuff
    this->name = "LWReactor 1";
    this->description = "LWReactor 1's Description";
    this->id = 1;

    //If the size is incorrect, fix it
    if(size <= 0) {
        this->size = 1;
    } else {
        this->size = size;
    }
    this->HDF5LWRTag = LWREACTOR;

}

/**
 * An operation that overrides the LWRComposite's operation. This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
 *
 * @param The component to add
 */
void LWReactor::addComponent(std::shared_ptr<ICE_DS::Component> component) {

    // begin-user-code

    //Does nothing

    return;

    // end-user-code

}

/**
 * An operation that overrides the LWRComposite's operation. This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
 *
 * @param the child Id
 */
void LWReactor::removeComponent(int childId) {

    // begin-user-code

    //Does nothing

    return;

    // end-user-code

}

/**
 * An operation that overrides the LWRComposite's operation. This operation does nothing and requires that the appropriate, more defined, associated operation to be utilized on this class.
 *
 * @param the name
 */
void LWReactor::removeComponent(const std::string name) {

    // begin-user-code

    //Does nothing

    return;

    // end-user-code

}

/**
 * Returns the size.
 *
 * @return the size
 */
int LWReactor::getSize() {

    // begin-user-code

    return this->size;

    // end-user-code
}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool LWReactor::operator==(const LWReactor& otherObject) const {

    return LWRComposite::operator ==(otherObject) && this->size == otherObject.size;
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return The newly instantiated object.
 */
std::shared_ptr<ICE_DS::Identifiable> LWReactor::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<LWReactor> component(new LWReactor (*this));

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
bool LWReactor::writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    bool flag = true;

    flag &= LWRComposite::writeAttributes(h5File, h5Group);
    flag &= ICE_IO::HdfWriterFactory::writeIntegerAttribute(h5File, h5Group, "size",this->size);

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
bool LWReactor::readAttributes(std::shared_ptr<H5::Group> h5Group) {

    // begin-user-code

    //Local attributes (so we only call read ONCE)
    //These will clear out by the garbage collector
    int size;

    //Check super
    if(h5Group.get() == NULL) {
        return false;
    }

    //Get the information.  If any fail out, return false and do not change data.
    try {
        if (LWRComposite::readAttributes(h5Group) == false) return false;
        size = ICE_IO::HdfReaderFactory::readIntegerAttribute(h5Group, "size");
    } catch (...) {
        return false;
    }

    //Set the primitive data
    this->size = size;

    return true;

    // end-user-code

}
