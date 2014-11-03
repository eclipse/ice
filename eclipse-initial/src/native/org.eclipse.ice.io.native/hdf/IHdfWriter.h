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

#ifndef IHDFWRITER_H
#define IHDFWRITER_H

#include "IHdfWriteable.h"
#include <memory>

namespace ICE_IO {

/**
 * An interface that provides the required operations for writing an IHdfWriteable tree to an HDF5 file.
 */
class IHdfWriter {

public:

    /**
     * This operation writes an IHdfWriteable to the HDF5 file at the provided URI. If any error or failure to write is encountered, then false is returned. Otherwise, true is returned.
     *
     * @param the writeable object
     * @param the path
     *
     * @return true if successful, false otherwise
     */
    virtual bool write(std::shared_ptr<IHdfWriteable> iHdfWriteable, std::string path) = 0;

};
}
#endif
