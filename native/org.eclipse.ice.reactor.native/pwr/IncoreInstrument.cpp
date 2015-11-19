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

#include "IncoreInstrument.h"
#include "../HDF5LWRTagType.h"
#include <string>
#include <vector>
#include <HdfReaderFactory.h>
#include <HdfWriterFactory.h>
#include <H5Cpp.h>
#include "../UtilityOperations.h"
#include "../LWRComponent.h"
#include "../Ring.h"
#include <ICEObject/Identifiable.h>

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
IncoreInstrument::IncoreInstrument(IncoreInstrument & arg) : LWRComponent(arg) {

    //begin-user-code

    //Clone for getting rings and tubes!
    std::shared_ptr<Identifiable> identRing = arg.thimble.get()->clone();

    //Dynamic cast up to a ring (which will preserve the clone operation on stack without calling new!
    std::shared_ptr<Ring> ring = std::dynamic_pointer_cast<Ring> (identRing);
    this->thimble = ring;

    //end-user-code

}

/**
 * The Destructor
 */
IncoreInstrument::~IncoreInstrument() {
    //TODO Auto-generated method stub
}

/**
 * The nullary Constructor.
 */
IncoreInstrument::IncoreInstrument() {

    //begin-user-code

    //Setup defaults
    this->name = "Instrument 1";
    this->description = "Default Instrument";
    this->id = 1;
    this->HDF5LWRTag = INCORE_INSTRUMENT;
    this->thimble = std::shared_ptr<Ring> (new Ring("thimble"));

    //end-user-code
}

/**
 * A parameterized Constructor.
 *
 * @param the name
 * @param the thimble
 */
IncoreInstrument::IncoreInstrument(const std::string name, std::shared_ptr<Ring> thimble) {
    //begin-user-code

    //Setup defaults
    this->name = "Instrument 1";
    this->description = "Default Instrument";
    this->id = 1;
    this->HDF5LWRTag = INCORE_INSTRUMENT;
    this->thimble = std::shared_ptr<Ring> (new Ring("thimble"));

    //Call setters
    IncoreInstrument::setName(name);
    IncoreInstrument::setThimble(thimble);

    //end-user-code
}

/**
 * Sets an empty thimble tube used a boundary between the detector and the reactor.
 *
 * @param the thimble to set
 */
void IncoreInstrument::setThimble(std::shared_ptr<Ring> thimble) {

    // begin-user-code

    //if thimble is not Null set value
    if (thimble.get() != NULL) {

        this->thimble = thimble;
    }

    // end-user-code

}

/**
 * Returns an empty thimble tube used a boundary between the detector and the reactor.
 *
 * @return the thimble
 */
std::shared_ptr<Ring> IncoreInstrument::getThimble() {

    // begin-user-code

    return this->thimble;

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
std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> >  IncoreInstrument::getWriteableChildren() {
    //begin-user-code

    //Get the children in super
    std::vector< std::shared_ptr<ICE_IO::IHdfWriteable> > children = LWRComponent::getWriteableChildren();

    //Add thimble to list
    children.push_back(this->thimble);

    //Return list
    return children;

    //end-user-code

}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool IncoreInstrument::operator==(const IncoreInstrument& otherObject) const {

    // begin-user-code

    return LWRComponent::operator ==(otherObject) && this->thimble.get()->operator ==(*otherObject.thimble.get());

    // end-user-code
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> IncoreInstrument::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<IncoreInstrument> component(new IncoreInstrument (*this));

    // Return the newly instantiated object
    return component;

    // end-user-code

}

/**
 * This operation reads IHdfReadable child objects. If this IHdfReadable has no IHdfReadable child objects, false is returned.
 *
 * @param the readable child
 *
 * @return true if successful, false otherwise
 */
bool IncoreInstrument::readChild(std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable) {
    // begin-user-code

    //If the child is null, return false
    if(iHdfReadable.get() == NULL) {
        return false;
    }

    //Cast the child into a Material - may throw a class cast exception
    try {

        //Dynamic cast to material
        Ring *newType = dynamic_cast<Ring *> (iHdfReadable.get());

        //If the type is not material, return false
        if(newType == 0) {
            return false;
        }

        //Dynamic cast up to a ring (which will preserve the clone operation on implemented dervied classes without calling new!
        std::shared_ptr<Ring> ring = std::dynamic_pointer_cast<Ring> (iHdfReadable);

        //Add ring
        IncoreInstrument::setThimble(ring);

    } catch (...) {
        return false;
    }

    //Return true for success
    return true;

    // end-user-code
}
