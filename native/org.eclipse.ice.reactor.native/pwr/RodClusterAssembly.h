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

#ifndef RODCLUSTERASSEMBLY_H
#define RODCLUSTERASSEMBLY_H

#include "PWRAssembly.h"
#include <string>
#include <ICEObject/Identifiable.h>

namespace ICE_Reactor {

/**
 * The RodClusterAssembly class is a PWRAssembly associated with a particular FuelAssembly object.
 */
class RodClusterAssembly :  public PWRAssembly {

public:

    /**
     * The Copy Constructor
     */
    RodClusterAssembly(RodClusterAssembly & arg);

    /**
     * The Destructor
     */
    virtual ~RodClusterAssembly();

    /**
     *  A parameterized Constructor.
     *
     *  @param the size
     */
    RodClusterAssembly(int size);

    /**
     * A parameterized Constructor.
     *
     * @param the name
     * @param the size
     */
    RodClusterAssembly(const std::string name, int size);

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const RodClusterAssembly& otherObject) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return the newly instantiated object
     */
    std::shared_ptr<ICE_DS::Identifiable> clone();

};

}

#endif
