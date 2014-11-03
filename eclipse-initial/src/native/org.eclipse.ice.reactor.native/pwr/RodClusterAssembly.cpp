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

#include "RodClusterAssembly.h"
#include <vector>
#include <string>
#include <H5Cpp.h>

using namespace ICE_Reactor;

/**
 * The Copy Constructor
 */
RodClusterAssembly::RodClusterAssembly(RodClusterAssembly & arg) : PWRAssembly (arg) {

    //begin-user-code

    //Should be copied

    //end-user-code

}

/**
 * The Destructor
 */
RodClusterAssembly::~RodClusterAssembly() {
    //TODO Auto-generated method stub
}

/**
 *  A parameterized Constructor.
 *
 *  @param the size
 */
RodClusterAssembly::RodClusterAssembly(int size) : PWRAssembly(size) {

    // begin-user-code

    //Default value
    this->name = "RodClusterAssembly";
    this->description = "RodClusterAssembly's Description";
    this->id = 1;
    this->HDF5LWRTag = ROD_CLUSTER_ASSEMBLY;

    // end-user-code

}

/**
 * A parameterized Constructor.
 *
 * @param the name
 * @param the size
 */
RodClusterAssembly::RodClusterAssembly(const std::string name, int size) : PWRAssembly(name, size) {
    // begin-user-code

    //Default value
    this->name = "RodClusterAssembly";
    this->description = "RodClusterAssembly's Description";
    this->id = 1;
    this->HDF5LWRTag = ROD_CLUSTER_ASSEMBLY;

    //Call setter
    RodClusterAssembly::setName(name);
    // end-user-code

}

/**
 * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
 *
 * @param object to compare to
 *
 * @return true if equal, false otherwise
 */
bool RodClusterAssembly::operator==(const RodClusterAssembly& otherObject) const {

    // begin-user-code

    //Call super, since no attributes here

    return PWRAssembly::operator ==(otherObject);

    // end-user-code
}

/**
 * Deep copies and returns a newly instantiated object.
 *
 * @return the newly instantiated object
 */
std::shared_ptr<ICE_DS::Identifiable> RodClusterAssembly::clone() {

    // begin-user-code

    // Local Declarations
    std::shared_ptr<RodClusterAssembly> component(new RodClusterAssembly (*this));

    // Return the newly instantiated object
    return component;

    // end-user-code

}
