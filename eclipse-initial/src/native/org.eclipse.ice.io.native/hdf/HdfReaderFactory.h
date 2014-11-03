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

#ifndef HDFREADERFACTORY_H
#define HDFREADERFACTORY_H

#include <H5Cpp.h>
#include <memory>
#include <vector>

namespace ICE_IO {

/**
 * The HdfReaderFactory class contains static methods used to read elements from an HDF5 file.
 */
class HdfReaderFactory {

public:

    /**
     * Returns the child H5Group called name from parentH5Group. If parentH5Group is null, then null is returned. If name is null or is an empty std::string, then null is returned. If there is no child H5Group called name, then null is returned.
     *
     * @param the parent group
     * @param the name
     */
    static std::shared_ptr<H5::Group> getChildH5Group(std::shared_ptr<H5::Group> parentH5Group, const std::string name);

    /**
     * Returns the child H5Group at the provided index from parentH5Group's member list. If parentH5Group is null, then null is returned. If index &lt; 0, then null is returned. If parentH5Group has no children, then null is returned. If the object at located at the provided index is not an H5Group, then null is returned.
     *
     * @param the parent group
     * @param the index
     */
    static std::shared_ptr<H5::Group> getChildH5Group(std::shared_ptr<H5::Group> parentH5Group, int index);

    /**
     * Returns an ArrayList of all child H5Groups from parentH5Group's member list. If parentH5Group is null, then null is returned. If parentH5Group has no H5Group children, then an empty ArrayList is returned.
     *
     * @param the parent group
     *
     * @return the groups at the parent
     */
    static std::vector< std::shared_ptr<H5::Group> > getChildH5Groups(std::shared_ptr<H5::Group> parentH5Group);

    /**
     * Returns the Dataset called name from h5Group. If h5Group is null, then null is returned. If name is null or an empty std::string, then null is returned. If h5Group has no Datasets, then null is returned. If a Dataset called name can not be located, then null is returned.
     *
     * @param the group
     * @param the name
     *
     * @param the dataset at the group with the given name
     */
    static std::shared_ptr<H5::DataSet> getDataset(std::shared_ptr<H5::Group> h5Group, const std::string name);

    /**
     * Reads and returns a Double object read from the Attribute called name from the metadata for h5Group. If name is null or an empty std::string, then null is returned. If h5Group is null, then null is returned. If an Attribute called name cannot be located, then null is returned. If the Attribute called name is located but is not of Datatype.CLASS_FLOAT, then null is returned.
     *
     * @param the group
     * @param the name
     *
     * @return the double value
     */
    static double readDoubleAttribute(std::shared_ptr<H5::Group> h5Group, const std::string name);

    /**
     * Reads and returns an Integer object read from the Attribute called name from the metadata for h5Group. If name is null or an empty std::string, then null is returned. If h5Group is null, then null is returned. If an Attribute called name cannot be located, then null is returned. If the Attribute called name is located but is not of Datatype.CLASS_INTEGER, then null is returned.
     *
     * @param the group
     * @param the name
     *
     * @return the integer value
     */
    static int readIntegerAttribute(std::shared_ptr<H5::Group> h5Group, const std::string name);

    /**
     * Reads and returns a std::string object read from the Attribute called name from the metadata for h5Group. If name is null or an empty std::string, then null is returned. If h5Group is null, then null is returned. If an Attribute called name cannot be located, then null is returned. If the Attribute called name is located but is not of Datatype.CLASS_STRING, then null is returned.
     *
     * @param the group
     * @param the name
     *
     * @return the string
     */
    static std::string readStringAttribute(std::shared_ptr<H5::Group> h5Group, const std::string name);

};
}
#endif
