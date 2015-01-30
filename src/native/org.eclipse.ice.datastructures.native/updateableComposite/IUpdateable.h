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

#ifndef IUPDATEABLE_H
#define IUPDATEABLE_H

#include "../ICEObject/Identifiable.h"

namespace ICE_DS {

/**
 * The IUpdateable interface provides a single update operation that may be used by implementers to receive an update based on a key-value pair. This is used by Components, Entries and other classes to receive updates from the Registry when values stored therein change. More generally it can be used by any class within ICE for receiving or posting updates.
 */
class IUpdateable: public virtual Identifiable {

public:

    /**
     * This operation notifies a class that has implemented IUpdateable that the value associated with the particular key has been updated.
     *
     * @param the key
     * @param the value
     */
    virtual void update(const std::string updatedKey,
                        const std::string newValue) = 0;

};
}

#endif
