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

#include "HdfWriterFactory.h"
#include <H5Cpp.h>
#include <memory>
#include <vector>

using namespace ICE_IO;

std::shared_ptr<H5::Group> HdfWriterFactory::createH5Group(std::shared_ptr<H5::H5File> h5File, const std::string name, std::shared_ptr<H5::Group> parentH5Group) {

    // begin-user-code

    if(h5File.get() == NULL || name.empty() || parentH5Group.get() == NULL) {
        throw -1;
    }

    try {

        //Create the group
        std::shared_ptr<H5::Group> h5Group (new H5::Group(parentH5Group.get()->createGroup(name)));

        //Return it
        return h5Group;

    } catch (...) {

        throw -1;
    }

    // end-user-code
}
H5::PredType HdfWriterFactory::createFloatH5Datatype(std::shared_ptr<H5::H5File> h5File) {

    // begin-user-code

    try {
        //Return the datatype
        return H5::PredType::NATIVE_DOUBLE;

        //Throw any exceptions
    } catch (...) {
        throw -1;
    }

    // end-user-code
}
H5::PredType HdfWriterFactory::createIntegerH5Datatype(std::shared_ptr<H5::H5File> h5File) {

    // begin-user-code

    try {
        //Return the datatype
        return H5::PredType::NATIVE_INT;

        //Throw any exceptions
    } catch (...) {
        throw -1;
    }

    // end-user-code
}
bool HdfWriterFactory::writeDoubleAttribute(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group, const std::string name, double value) {
    // begin-user-code

    //If these pieces are null, return false
    if(h5File == NULL || h5Group == NULL) return false;

    try {

        //Create the dataspace
        hid_t space_id = H5Screate(H5S_SIMPLE);
        hsize_t dims[1] = { 1 };
        H5Sset_extent_simple(space_id, 1, dims, dims);

        //Create the attribute
        hid_t attr = H5Acreate(h5Group.get()->getId(), name.c_str(), HdfWriterFactory::createFloatH5Datatype(h5File).getId(), space_id, H5P_DEFAULT, H5P_DEFAULT);

        //Write attribute
        H5Awrite(attr, HdfWriterFactory::createFloatH5Datatype(h5File).getId(), &value);

        //Close the resource
        H5Aclose(attr);

    } catch(...) {
        throw -1;
        return false;
    }

    return true;
    // end-user-code
}
bool HdfWriterFactory::writeIntegerAttribute(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group, const std::string name, int value) {
    // begin-user-code

    //If these pieces are null, return false
    if(h5File == NULL || h5Group == NULL) return false;

    try {
        //Create the dataspace
        hid_t space_id = H5Screate(H5S_SIMPLE);
        hsize_t dims[1] = { 1 };
        H5Sset_extent_simple(space_id, 1, dims, dims);

        //Create the attribute
        hid_t attr = H5Acreate(h5Group.get()->getId(), name.c_str(), HdfWriterFactory::createIntegerH5Datatype(h5File).getId(), space_id, H5P_DEFAULT, H5P_DEFAULT);

        //Write attribute
        H5Awrite(attr, HdfWriterFactory::createIntegerH5Datatype(h5File).getId(), &value);

        //Close the resource
        H5Aclose(attr);

    } catch(...) {
        return false;
    }

    return true;
    // end-user-code
}
bool HdfWriterFactory::writeStringAttribute(std::shared_ptr<H5::H5File> h5File, std::shared_ptr<H5::Group> h5Group, const std::string name, const std::string value) {

    // begin-user-code

    //If these pieces are null, return false
    if(h5File == NULL || h5Group == NULL) return false;

    try {
        hid_t space_id = H5Screate(H5S_SIMPLE);
        hsize_t dims[1] = { 1 };
        H5Sset_extent_simple(space_id, 1, dims, dims);

        //Create the dataspace and create the attribute
        H5::StrType dataType(0, value.length());

        //Create the attribute
        hid_t attr = H5Acreate(h5Group.get()->getId(), name.c_str(), dataType.getId(), space_id, H5P_DEFAULT, H5P_DEFAULT);

        //Write attribute
        H5Awrite(attr,dataType.getId(), value.c_str());

        //Close the attribute
        H5Aclose(attr);
    } catch(...) {
        return false;
    }

    return true;

    // end-user-code
}
