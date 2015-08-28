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

#ifndef IHDFWRITEABLE_H
#define IHDFWRITEABLE_H

#include <H5Cpp.h>
#include<memory>
#include <vector>

namespace ICE_IO {

/**
 * An interface that provides the required operations for populating an HDF5 file from an ICE data structure.
 */
class IHdfWriteable {

public:

    /**
     * This operation creates and returns a child H5Group for the parentH5Group in the h5File. If h5File is null or can not be opened, then null is returned. If parentH5Group is null, then null is returned. If an exception is thrown, then null is returned.
     *
     * @param the h5file
     * @param the parent group
     *
     * @return the newly created group
     */
    virtual std::shared_ptr<H5::Group> createGroup(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> parentH5Group) = 0;

    /**
     * This operation returns an ArrayList of IHdfWriteable child objects. If this IHdfWriteable has no IHdfWriteable child objects, then null is returned.
     *
     * @return the list of writeable objects
     */
    virtual std::vector< std::shared_ptr<IHdfWriteable> > getWriteableChildren() = 0;

    /**
     * This operation writes HDF5 Attributes to the metadata of h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Attributes, then false is returned. Otherwise, true is returned.
     *
     * @param the h5file
     * @param the h5Group
     *
     * @return true if successful, false otherwise
     */
    virtual bool writeAttributes(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) = 0;

    /**
     * This operation writes HDF5 Datasets to the h5Group in the h5File. If the h5Group is null or h5File is null or can not be opened, then false is returned. If the operation fails to write all Datasets, then false is returned. Otherwise, true is returned.
     *
     * @param the h5file
     * @param the group
     *
     * @return true if successful, false otherwise
     */
    virtual bool writeDatasets(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group) = 0;

};
}
#endif
