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

#ifndef LWRCOMPONENTREADER_H
#define LWRCOMPONENTREADER_H

#include <IHdfReader.h>
#include <memory>
#include <H5Cpp.h>
#include <IHdfReadable.h>
#include "LWRComponent.h"
#include "HDF5LWRTagType.h"
#include <string>
#include <map>


namespace ICE_Reactor {

/**
 * <p>The LWRComponentReader class creates and populates an LWRComponent or LWRComposite
 * tree from an HDF5 file. This class implements the IHdfReadable interface.  This takes
 * any type of LWRComponent or LWRComposite and iterates from the top of that particular
 * tree down, so a user could read from HDF5 and populate that list accordingly to any
 * part of a Reactor or its delegated classes that inherit from LWRComponent.</p>
 */
class LWRComponentReader : public ICE_IO::IHdfReader {

private:

    /**
     * The Map of instances.
     */
    std::map<HDF5LWRTagType, std::shared_ptr<LWRComponent> > lWRComponentInstanceMap;

    /**
     * Returns a new instance of the HDF5LWRTagType or null
     *
     * @param  The tag
     *
     * @return LWRComponent
     */
    std::shared_ptr<LWRComponent> getLWRComponentInstance(HDF5LWRTagType HDF5LWRTag);
    /**
     * Reads the H5Group and returns the list of IHDF5Readables
     *
     * @param The Group
     *
     * @return the IHdfReadable
     */
    std::shared_ptr<ICE_IO::IHdfReadable> read(std::shared_ptr<H5::Group> h5Group);

public:

    /**
     * Copy constructor.  Not used
     */
    LWRComponentReader(LWRComponentReader & arg);

    /**
     * Constructor
     */
    LWRComponentReader();
    /**
     * Destructor
     */
    virtual ~LWRComponentReader();

    /**
     * Parses a file given a path from HDF5 to a IHdfReadable object.
     *
     * @param the path
     *
     * @return the IHDF5Readalbe.
     */
    std::shared_ptr<ICE_IO::IHdfReadable> read(const std::string path);

};

}

#endif
