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

#include "BWReactor.h"
#include <ICEObject/Identifiable.h>
#include <string>

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
BWReactor::BWReactor(BWReactor & arg) : LWReactor(arg) {

    //begin-user-code

    //Do nothing.  Super operation called

    //end-user-code


}

/**
 * The Destructor
 */
BWReactor::~BWReactor() {
    //TODO Auto-generated method stub
}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool BWReactor::operator==(const BWReactor& otherObject) const {

    // begin-user-code

    //No attributes to compare.  Call super
    return LWReactor::operator ==(otherObject);

    // end-user-code

}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> BWReactor::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<BWReactor> component(new BWReactor (*this));

    // Return the newly instantiated object
    return component;

    // end-user-code
}

/**
 *  A parameterized Constructor.
 *
 *  @param the size
 */
BWReactor::BWReactor(int size) : LWReactor(size) {

    // begin-user-code

    this->name = "BWReactor 1";
    this->description = "BWReactor 1's Description";
    this->HDF5LWRTag = BWREACTOR;

    // end-user-code
}
