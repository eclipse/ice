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

#ifndef IHDFREADABLE_H
#define IHDFREADABLE_H

#include <H5Cpp.h>
#include <memory>

namespace ICE_IO {

/**
 * An interface that provides the required operations for populating an ICE data structure from an HDF5 file.
 */
class IHdfReadable {

public:

    /**
     * This operation assigns the reference of the provided iHdfReadable to a class variable. If iHdfReadable is null, then false is returned. If iHdfReadable fails casting, then false is returned. Otherwise, true is returned.
     *
     * @param the child to read
     *
     * @return true if successful, false otherwise
     */
    virtual bool readChild(std::shared_ptr<IHdfReadable> iHdfReadable) = 0;

    /**
     * This operation reads Attributes from h5Group and assigns their values to class variables. If h5Group is null, false is returned. If any Attribute values are null, false is returned. Otherwise, true is returned.
     *
     * @param the group to read attributes from
     *
     * @return true if successful, false otherwise
     */
    virtual bool readAttributes(std::shared_ptr<H5::Group> h5Group) = 0;

    /**
     * This operation reads Datasets from h5Group and assigns their values to class variables. If h5Group is null or an Exception is thrown, false is returned. If the Otherwise, true is returned.
     *
     * @param the group to read datasets from
     *
     * @return true if successful, false otherwise
     */
    virtual bool readDatasets(std::shared_ptr<H5::Group> h5Group) = 0;

};
}
#endif
