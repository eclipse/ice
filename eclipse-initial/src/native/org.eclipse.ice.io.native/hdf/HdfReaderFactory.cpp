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

#include "HdfReaderFactory.h"
#include <H5Cpp.h>
#include <memory>
#include <vector>
#include <string>

using namespace ICE_IO;

std::shared_ptr<H5::Group> HdfReaderFactory::getChildH5Group(std::shared_ptr<H5::Group> parentH5Group, const std::string name) {
    // begin-user-code

    //If the parent Group is null, return
    if(parentH5Group == NULL) {
        throw -1;
    }

    //Try to get the group.  If it does not exist, throw an exception
    try {

        std::shared_ptr<H5::Group> childGroup(new H5::Group(parentH5Group.get()->openGroup(name)));

        //Return Group
        return childGroup;

    } catch(...) {
        throw -1;
    }

    // end-user-code*/
}
std::shared_ptr<H5::Group> HdfReaderFactory::getChildH5Group(std::shared_ptr<H5::Group> parentH5Group, int index) {

    // begin-user-code

    //If the parent Group is null, return
    if(parentH5Group == NULL || index < 0 || index >= parentH5Group.get()->getNumObjs()) {
        throw -1;
    }


    //Try to get the group.  If it does not exist, throw an exception
    try {
        //Double check the type
        if(parentH5Group.get()->getObjTypeByIdx(index) != H5G_GROUP) {
            throw -1;
        }

        //Get object's name by index
        std::string name = parentH5Group.get()->getObjnameByIdx(index);

        //Call getter and return on it
        return HdfReaderFactory::getChildH5Group(parentH5Group, name);

    } catch(...) {
        throw -1;
    }

    // end-user-code
}
std::vector< std::shared_ptr<H5::Group> > HdfReaderFactory::getChildH5Groups(std::shared_ptr<H5::Group> parentH5Group) {

    // begin-user-code
    std::vector< std::shared_ptr<H5::Group> > groupList;

    //If the parent Group is null, return
    if(parentH5Group == NULL) return groupList;

    //Get the group or return an empty list
    for (int i = 0; i < parentH5Group.get()->getNumObjs(); i++) {

        //If it is a group, add it
        if(parentH5Group.get()->getObjTypeByIdx(i) == H5G_GROUP) {
            //Get name
            std::string name = parentH5Group.get()->getObjnameByIdx(i);
            //Get group by name and add to vector
            groupList.push_back(HdfReaderFactory::getChildH5Group(parentH5Group, name));
        }
    }

    //Return list
    return groupList;

    // end-user-code

}
std::shared_ptr<H5::DataSet> HdfReaderFactory::getDataset(std::shared_ptr<H5::Group> h5Group, const std::string name) {

    // begin-user-code

    //If the parent Group is null, return
    if(h5Group == NULL) {
        throw -1;
    }

    //Try to get the dataset.  If it does not exist, throw an exception
    try {

        std::shared_ptr<H5::DataSet> childSet(new H5::DataSet(h5Group.get()->openDataSet(name)));

        //Return dataset
        return childSet;

    } catch(...) {
        throw -1;
    }

    // end-user-code
}
double HdfReaderFactory::readDoubleAttribute(std::shared_ptr<H5::Group> h5Group, const std::string name) {

    // begin-user-code

    //Local declaration
    double value[1];

    //If the group is null, throw exception
    if(h5Group == NULL) {
        throw -1;
    }

    //Get the value and reutrn it
    try {
        hid_t attribute = H5Aopen(h5Group.get()->getId(), name.c_str(), H5P_DEFAULT);
        if(attribute <0 ) throw -1;
        if(H5Aread(attribute, H5Aget_type(attribute), value) < 0) throw -1;

        //Return value
        return value[0];
    } catch (...) {
        throw -1;
    }

    // end-user-code

}
int HdfReaderFactory::readIntegerAttribute(std::shared_ptr<H5::Group>h5Group, const std::string name) {

    // begin-user-code

    //Local declaration
    int value[1];

    //If the group is null, throw exception
    if(h5Group == NULL) {
        throw -1;
    }

    //Get the value and reutrn it
    try {
        hid_t attribute = H5Aopen(h5Group.get()->getId(), name.c_str(), H5P_DEFAULT);
        if(attribute <0 ) throw -1;
        if(H5Aread(attribute, H5Aget_type(attribute), value) < 0) {
            throw -1;
        }

        //Return value
        return value[0];
    } catch (...) {
        throw -1;
    }

    // end-user-code

}
std::string HdfReaderFactory::readStringAttribute(std::shared_ptr<H5::Group> h5Group, const std::string name) {

    // begin-user-code

    //Local declaration


    //If the group is null, throw exception
    if(h5Group == NULL) {
        throw -1;
    }

    //Get the value and reutrn it
    try {


        //Get the string data
        hid_t attribute = H5Aopen(h5Group.get()->getId(), name.c_str(), H5P_DEFAULT);
        if(attribute <0 ) throw -1;
        //Get dimensions of string
        hid_t atype = H5Aget_type(attribute);
        size_t size = H5Tget_size(atype);

        //Create a buffer
        char readWords[size+1]; //Plus 1 for null terminating string

        //Read value
        if(H5Aread(attribute, H5Aget_type(attribute), readWords) < 0) throw -1;

        //Add null terminator
        readWords[size] = '\0';

        //convert character array to string
        std::string value(readWords);

        //Return result
        return value;
    } catch (...) {
        throw -1;
    }

    // end-user-code

}
