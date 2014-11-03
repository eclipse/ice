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

#ifndef BWREACTOR_H
#define BWREACTOR_H

#include "../LWReactor.h"
#include <ICEObject/Identifiable.h>

namespace ICE_Reactor {

/**
 * The BWReactor class represents any Boiling Water Reactor.
 */
class BWReactor : public LWReactor {

public:

    /**
     * The Copy Constructor
     */
    BWReactor(BWReactor & arg);

    /**
     * The Destructor
     */
    virtual ~BWReactor();

    /**
     * Overrides the equals operation to check the attributes on this object with another object of the same type.  Returns true if the objects are equal.  False otherwise.
     *
     * @param object to compare to
     *
     * @return true if equal, false otherwise
     */
    bool operator==(const BWReactor& otherObject) const;

    /**
     * Deep copies and returns a newly instantiated object.
     *
     * @return the newly instantiated object
     */
    std::shared_ptr<ICE_DS::Identifiable> clone();

    /**
     *  A parameterized Constructor.
     *
     *  @param the size
     */
    BWReactor(int size);

};

}

#endif
