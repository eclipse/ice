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

#include "LWRComponentWriter.h"
#include <string>
#include <vector>
#include <memory>

using namespace ICE_Reactor;

/**
 * The Copy constructor.  Not used.
 */
LWRComponentWriter::LWRComponentWriter(LWRComponentWriter & arg) {
    //TODO Auto-generated method stub
}

/**
 * Constructor
 */
LWRComponentWriter::LWRComponentWriter() {

}

/**
 * Destructor
 */
LWRComponentWriter::~LWRComponentWriter() {
    //TODO Auto-generated method stub
}

/**
 * This operation writes an IHdfWriteable to the HDF5 file at the provided URI. If any error or failure to write is encountered, then false is returned. Otherwise, true is returned.
 *
 * @param The writeable object
 * @param the path
 *
 * @return true if successful, false otherwise
 */
bool LWRComponentWriter::write(std::shared_ptr<ICE_IO::IHdfWriteable> iHdfWriteable, const std::string path) {

    // begin-user-code

    //Check for a null iHdfWriteable. If null then return false
    if (iHdfWriteable.get() == NULL || path.empty()) {

        return false;

    }

    //Create and open a new h5file with the provided uri
    std::shared_ptr<H5::H5File> h5File = ICE_IO::HdfFileFactory::createH5File(path);

    //If the file is null, then return false
    if (h5File.get() == NULL) {

        return false;

    }

    //Get the root group from the file
    std::shared_ptr<H5::Group> rootH5Group (new H5::Group(h5File.get()->openGroup("/")));

    //If the root group is null then return false
    if (rootH5Group.get() == NULL) {

        return false;

    }

    //Write the iHdfWriteable to the root group and store the boolean result
    bool flag = this->write(iHdfWriteable, h5File, rootH5Group);

    //Close the file
    ICE_IO::HdfFileFactory::closeH5File(h5File);

    //Return the result
    return flag;

    // end-user-code
}

/**
 * Recursively writes the H5Groups, Attributes, and Datasets of the provided IHdfWriteable to the provided H5File.
 *
 * @param iHdfWriteable <p>The IHdfWriteable to be written.</p>
 * @param h5File <p>The H5File to write to.</p>
 * @param parentH5Group <p>A H5Group to write for this iteration.</p>
 *
 * @return <p>True if the write was successful, false otherwise.</p>
*/
bool LWRComponentWriter::write(std::shared_ptr<ICE_IO::IHdfWriteable> iHdfWriteable, std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> parentH5Group) {

    //Create a flag to store and return any write failures
    bool flag = true;

    //Write a new group for this LWRComponent
    std::shared_ptr<H5::Group> h5Group = iHdfWriteable.get()->createGroup(h5File, parentH5Group);

    //Write the attributes for this LWRComponent
    flag &= iHdfWriteable.get()->writeAttributes(h5File, h5Group);

    //Write the datasets for this LWRComponent
    flag &= iHdfWriteable.get()->writeDatasets(h5File, h5Group);

    //Get the children of iHdfWriteable
    std::vector<std::shared_ptr<ICE_IO::IHdfWriteable> > children = iHdfWriteable.get()->getWriteableChildren();

    //If there are children
    if (children.size() > 0) {

        //Cycle over the children
        for (int i = 0; i < children.size(); i++) {

            //Write the child
            flag &= LWRComponentWriter::write(children.at(i), h5File, h5Group);

        }

    }

    //Return the flag
    return flag;

}
