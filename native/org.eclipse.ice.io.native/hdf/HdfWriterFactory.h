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

#ifndef HDFWRITERFACTORY_H
#define HDFWRITERFACTORY_H

#include <H5Cpp.h>
#include <memory>

namespace ICE_IO {

/**
 * The HdfWriterFactory class contains static methods used to write elements to an HDF5 file.
 */
class HdfWriterFactory {

public:

    /**
     * Creates and returns a child H5Group called name for parentH5Group using the h5File. If h5File is null or can not be opened, then null is returned. If name is null or is an empty std::string, then null is returned. If parentH5Group is null, then null is returned. If an exception is thrown, then null is returned.
     *
     * @param the h5file
     * @param the name
     * @param the parent group
     *
     * @return the newly created group
     */
    static std::shared_ptr<H5::Group> createH5Group(std::shared_ptr<H5::H5File> h5File, const std::string name, std::shared_ptr<H5::Group> parentH5Group);

    /**
     * Creates and returns a 64-bit floating point H5Datatype from h5File. If h5File is null or can not be opened, then null is returned. If an exception is thrown, then null is returned.
     *
     * @param the h5File
     *
     * @return the type
     */
    static H5::PredType createFloatH5Datatype(std::shared_ptr<H5::H5File> h5File);

    /**
     * Creates and returns an integer H5Datatype from h5File. If h5File is null or can not be opened, then null is returned. If an exception is thrown, then null is returned.
     *
     * @param the h5file
     *
     * @return the type
     */
    static H5::PredType createIntegerH5Datatype(std::shared_ptr<H5::H5File> h5File);

    /**
     *  Writes an Attribute to h5Group called name with a double value using h5File. If h5File is null or can not be opened, then false is returned. If name is null or is an empty std::string, then false is returned. If h5Group is null, then false is returned. If an exception is thrown, then false is returned. Otherwise, true is returned.
     *
     *  @param the h5file
     *  @param the name of the attribute
     *  @param the value
     *
     *  @return true if successful, false otherwise
     */
    static bool writeDoubleAttribute(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group, const std::string name, double value);



    /**
     *  Writes an Attribute to h5Group called name with an integer value using h5File. If h5File is null or can not be opened, then false is returned. If name is null or is an empty std::string, then false is returned. If h5Group is null, then false is returned. If an exception is thrown, then false is returned. Otherwise, true is returned.
     *
     *  @param the h5file
     *  @param the name of the attribute
     *  @param the value
     *
     *  @return true if successful, false otherwise
     */
    static bool writeIntegerAttribute(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group, const std::string name, int value);


    /**
     * Writes an Attribute to h5Group called name with a std::string value using h5File. If h5File is null or can not be opened, then false is returned. If name is null or is an empty std::string, then false is returned. If value is null or is an empty std::string, then false is returned. If an exception is thrown, then false is returned. If h5Group is null, then false is returned. Otherwise, true is returned.
     *
     *  @param the h5file
     *  @param the name of the attribute
     *  @param the value
     *
     *  @return true if successful, false otherwise
     */
    static bool writeStringAttribute(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group, const std::string name, const std::string value);

};
}
#endif
