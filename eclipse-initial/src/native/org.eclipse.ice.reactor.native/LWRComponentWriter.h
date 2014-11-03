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

#ifndef LWRCOMPONENTWRITER_H
#define LWRCOMPONENTWRITER_H

#include <IHdfWriter.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <HdfFileFactory.h>

namespace ICE_Reactor {

/**
 * <p>The LWRComponentReader class writes an LWRComponent or LWRComposite tree to an
 * HDF5 file. This class implements the IHdfWriteable interface.  This takes any type
 * of LWRComponent or LWRComposite and iterates from the top of that particular tree
 * down, so a user could write to HDF5 and populate that list accordingly to any
 * part of a Reactor or its delegated classes that inherit from LWRComponent.</p>
 */
class LWRComponentWriter : public ICE_IO::IHdfWriter {

public:

    /**
     * The Copy constructor.  Not used.
     */
    LWRComponentWriter(LWRComponentWriter & arg);


    /**
     * Constructor
     */
    LWRComponentWriter();

    /**
     * Destructor
     */
    virtual ~LWRComponentWriter();

    /**
     * This operation writes an IHdfWriteable to the HDF5 file at the provided URI. If any error or failure to write is encountered, then false is returned. Otherwise, true is returned.
     *
     * @param The writeable object
     * @param the path
     *
     * @return true if successful, false otherwise
     */
    virtual bool write(std::shared_ptr<ICE_IO::IHdfWriteable> iHdfWriteable, const std::string path);


private:

    /**
     * Recursively writes the H5Groups, Attributes, and Datasets of the provided IHdfWriteable to the provided H5File.
     *
     * @param iHdfWriteable The IHdfWriteable to be written.
     * @param h5File The H5File to write to.
     * @param parentH5Group A H5Group to write for this iteration.
     *
     * @return True if the write was successful, false otherwise.
    */
    bool write(std::shared_ptr<ICE_IO::IHdfWriteable> iHdfWriteable, std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> parentH5Group);

};

}

#endif
