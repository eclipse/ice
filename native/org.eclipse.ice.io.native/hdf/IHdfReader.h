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

#ifndef IHDFREADER_H
#define IHDFREADER_H

#include "IHdfReadable.h"
#include <memory>

namespace ICE_IO {

/**
 * An interface that provides the required operations for reading and creating an IHdfWriteable tree from an HDF5 file.
 */
class IHdfReader {

public:

    /**
     * This operation creates and populates an IHdfWriteable instance and its children from the HDF5 file at the provided URI. If any error or failure to read is encountered, then null is returned.
     *
     * @param the path
     *
     * @return the readable object
     */
    virtual std::shared_ptr<IHdfReadable> read(std::string path) = 0;


};  //end class IHdfReader

}

#endif
