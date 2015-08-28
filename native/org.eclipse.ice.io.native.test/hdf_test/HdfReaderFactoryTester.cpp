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
#define BOOST_TEST_DYN_LINK
#define BOOST_TEST_MODULE Regression
#include <boost/test/included/unit_test.hpp>
#include <HdfReaderFactory.h>
#include <HdfWriterFactory.h>
#include <HdfFileFactory.h>
#include <memory>
#include <string>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_IO;

BOOST_AUTO_TEST_SUITE(HdfReaderFactoryTester_testSuite)



BOOST_AUTO_TEST_CASE(beforeClass) {
    //Create folder testPath in local directory - relative!
    std::string testFolder = "ICEIOTestsDir/";
    mkdir(testFolder.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    //Delete file
    std::string testFile = testFolder + "test.h5";
    remove(testFile.c_str());
}
BOOST_AUTO_TEST_CASE(checkGetters) {

    // begin-user-code

    //Local Declarations
    std::string testFileName = "test1.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";
    std::shared_ptr<H5::H5File> h5File;
    std::shared_ptr<H5::Group> badGroup;
    std::shared_ptr<H5::H5File> badFile;
    double doubleNumber = 1.2131231231231;
    int intNumber = 33;
    std::string words = "Man, C++ is so much FUN!";

    //Used for exception hitting
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;
    bool exceptionHit2 = false;
    bool exceptionHit3 = false;
    bool exceptionHit4 = false;
    bool exceptionHit5 = false;
    bool exceptionHit6 = false;

    std::string subGroupName = "Bob";
    std::vector<std::string> rows;
    int maxRowSize = 0;
    std::string rowName = "Rows";

    //Setup ArrayList of Rows
    rows.push_back("ABBA");
    rows.push_back("BABBA");
    rows.push_back("CABBA");
    rows.push_back("DabbbyDabbyDooo!");
    rows.push_back("E");

    //Setup the string array
    char *rowArray[rows.size()];

    //Figure out the biggest row size and add to array
    for (int i = 0; i < rows.size(); i++) {

        //Get the max length
        if(rows.at(i).length() > maxRowSize) {
            maxRowSize = rows.at(i).length();
        }
        //Add to array
        rowArray[i] = new char[rows.at(i).size() +1];
        std::strcpy(rowArray[i], rows.at(i).c_str());
    }

    //Create a good H5File
    h5File = HdfFileFactory::createH5File(dataFile);

    //Open file and setup H5 File to have a parent and child.  Check get Groups and get Datasets
    try {

        //Nullary Checks
        try {
            HdfReaderFactory::getChildH5Group(badGroup,subGroupName);
        } catch(...) {
            exceptionHit0 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit0, true);

        //Nullary Checks
        try {
            HdfReaderFactory::getChildH5Group(badGroup,0);
        } catch(...) {
            exceptionHit1 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit1, true);

        //Add a group
        std::unique_ptr<H5::Group> parentH5Group(new H5::Group(h5File.get()->createGroup("/Reactor")));

        //Add subGroups
        parentH5Group.get()->createGroup("Assembly1");
        parentH5Group.get()->createGroup("Assembly2");
        parentH5Group.get()->createGroup("Assembly3");

        //Create a dataset
        hid_t filetype = H5Tcopy(H5T_C_S1);
        H5Tset_size(filetype, H5T_VARIABLE);
        hid_t memtype = H5Tcopy(H5T_C_S1);
        H5Tset_size(memtype, H5T_VARIABLE);
        hsize_t dims[1] = { rows.size() };

        hid_t space = H5Screate_simple(1, dims, NULL);

        hid_t dset = H5Dcreate(parentH5Group.get()->getId(),"DS1", filetype, space, H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
        H5Dwrite (dset, memtype, H5S_ALL, H5S_ALL, H5P_DEFAULT, rowArray);

        //Persist data
        parentH5Group.get()->close();
        h5File.get()->close();

        //Memory management!
        H5Tclose (filetype);
        H5Tclose (memtype);
        H5Sclose (space);
        H5Dclose (dset);

        h5File = HdfFileFactory::openH5File(dataFile);

        //Get the root Group
        std::shared_ptr<H5::Group> rootGroup(new H5::Group(h5File.get()->openGroup("/")));

        //Use the root group to get the parent group
        std::shared_ptr<H5::Group> parentGroup = HdfReaderFactory::getChildH5Group(rootGroup, "Reactor");


        //Nullary Checks - out of bounds
        try {
            HdfReaderFactory::getChildH5Group(parentGroup,10);
        } catch(...) {
            exceptionHit2 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit2, true);

        //Nullary Checks - negative index
        try {
            HdfReaderFactory::getChildH5Group(parentGroup, -1);
        } catch(...) {
            exceptionHit3 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit3, true);

        //Child does not exist
        try {
            HdfReaderFactory::getChildH5Group(parentGroup,subGroupName);
        } catch(...) {
            exceptionHit4 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit4, true);

        //Get the groups on the main group
        std::vector< std::shared_ptr<H5::Group> > childGroups = HdfReaderFactory::getChildH5Groups(parentGroup);

        //Make sure there are 3
        //There is a limit here because I am unable to get name types to compare at this time.
        //For now, this is a limitation on the CPP libraries for H5 and will have to be unsafely
        //assumed to be Groups and that they are valid groups.

        //FIXME - Need more test info for this piece.
        BOOST_REQUIRE_EQUAL(3, childGroups.size());

        //Get by index and by name and get by index to verify that they exist
        std::shared_ptr< H5::Group> assembly1a= HdfReaderFactory::getChildH5Group(parentGroup, 0);
        std::shared_ptr< H5::Group> assembly2a = HdfReaderFactory::getChildH5Group(parentGroup, 1);
        std::shared_ptr< H5::Group> assembly3a = HdfReaderFactory::getChildH5Group(parentGroup, 2);

        //Verify by name
        std::shared_ptr< H5::Group> assembly1b = HdfReaderFactory::getChildH5Group(parentGroup, "Assembly1");
        std::shared_ptr< H5::Group> assembly2b = HdfReaderFactory::getChildH5Group(parentGroup, "Assembly2");
        std::shared_ptr< H5::Group> assembly3b = HdfReaderFactory::getChildH5Group(parentGroup, "Assembly3");

        //Check dataset
        std::shared_ptr<H5::DataSet> dataSet = HdfReaderFactory::getDataset(parentGroup, "DS1");

        //Dataset Check - bad dataset
        try {
            std::shared_ptr<H5::DataSet> dataSet1 = HdfReaderFactory::getDataset(parentGroup, "notadataset");
        } catch (...) {
            exceptionHit5 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit5, true);

        //Dataset Check - bad group
        try {
            std::shared_ptr<H5::DataSet> dataSet1 = HdfReaderFactory::getDataset(badGroup, "DS1");
        } catch (...) {
            exceptionHit6 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit6, true);

        //Open up dataset and read values
        filetype = H5Dget_type(dataSet.get()->getId());
        space = H5Dget_space(dataSet.get()->getId());
        int ndims = H5Sget_simple_extent_dims(space, dims, NULL);
        char *data[dims[0]];

        memtype = H5Tcopy(H5T_C_S1);
        H5Tset_size(memtype, H5T_VARIABLE);
        H5Dread(dataSet.get()->getId(), memtype, H5S_ALL, H5S_ALL, H5P_DEFAULT, data);

        //Output data
        int i = 0;
        for(i = 0; i < dims[0]; i++) {
            BOOST_REQUIRE_EQUAL(rows.at(i), data[i]);
        }

        //Make sure the sizes are equal
        BOOST_REQUIRE_EQUAL(rows.size(), i);

        //Free the arrays
        for(i = 0; i < dims[0]; i++) {
            delete data[i];
            delete rowArray[i];
        }

    } catch (...) {
        BOOST_FAIL("HdfReaderFactory test failure! Exiting.");
    }

    return;
    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkReaders) {

    // begin-user-code

    //Local Declarations
    std::string testFileName = "test1.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";
    std::shared_ptr<H5::H5File> h5File;
    std::shared_ptr<H5::Group> badGroup;
    std::shared_ptr<H5::H5File> badFile;
    double doubleNumber = 1.2131231231231;
    int intNumber = 33;
    std::string words = "Man, C++ is so much FUN!";

    std::string intName = "integer value";
    std::string doubleName = "double value";
    std::string strName = "string value";

    //Used for exception hitting
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;
    bool exceptionHit2 = false;
    bool exceptionHit3 = false;
    bool exceptionHit4 = false;
    bool exceptionHit5 = false;
    bool exceptionHit6 = false;

    //Create a good H5File
    h5File = HdfFileFactory::createH5File(dataFile);

    //Open file and setup H5 File to have a parent and child.  Check Readers
    try {

        //Exception handling for errors

        //Check readInteger - bad group
        try {
            HdfReaderFactory::readIntegerAttribute(badGroup, intName);
        } catch(...) {
            exceptionHit0 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit0, true);

        //Check readDouble - bad group
        try {
            HdfReaderFactory::readDoubleAttribute(badGroup, doubleName);
        } catch(...) {
            exceptionHit1 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit1, true);

        //Check readString - bad group
        try {
            HdfReaderFactory::readStringAttribute(badGroup, strName);
        } catch(...) {
            exceptionHit2 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit2, true);

        //create a root group
        std::shared_ptr <H5::Group> rootGroup (new H5::Group(h5File.get()->createGroup("/Reactor")));

        //Error check operations on Reactor Group for values that do not exist

        //Check readInteger - no value exists for name
        try {
            HdfReaderFactory::readIntegerAttribute(rootGroup, intName);
        } catch(...) {
            exceptionHit3 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit3, true);

        //Check readDouble - no value exists for name
        try {
            HdfReaderFactory::readDoubleAttribute(rootGroup, doubleName);
        } catch(...) {
            exceptionHit4 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit4, true);

        //Check readString - no value exists for name
        try {
            HdfReaderFactory::readStringAttribute(rootGroup, strName);
        } catch(...) {
            exceptionHit5 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit5, true);

        //Create an integer, double, and string attribute on rootGroup.  Then persist file.

        //Create the dataspace
        hid_t space_id = H5Screate(H5S_SIMPLE);
        hsize_t dims[1] = { 1 };
        H5Sset_extent_simple(space_id, 1, dims, dims);

        //Create the attribute and write it
        //Create the attribute
        hid_t attribute1 = H5Acreate(rootGroup.get()->getId(), intName.c_str(), HdfWriterFactory::createIntegerH5Datatype(h5File).getId(), space_id, H5P_DEFAULT, H5P_DEFAULT);
        H5Awrite(attribute1, HdfWriterFactory::createIntegerH5Datatype(h5File).getId(), &intNumber);

        hid_t attribute2 = H5Acreate(rootGroup.get()->getId(), doubleName.c_str(), HdfWriterFactory::createFloatH5Datatype(h5File).getId(), space_id, H5P_DEFAULT, H5P_DEFAULT);
        H5Awrite(attribute2, HdfWriterFactory::createFloatH5Datatype(h5File).getId(), &doubleNumber);

        //Create the dataspace and create the attribute
        H5::StrType dataType(0, words.length());

        hid_t attribute3 = H5Acreate(rootGroup.get()->getId(), strName.c_str(), dataType.getId(), space_id, H5P_DEFAULT, H5P_DEFAULT);
        H5Awrite(attribute3, dataType.getId(), words.c_str());

        //Close file

        H5Aclose(attribute1);
        H5Aclose(attribute2);
        H5Aclose(attribute3);
        rootGroup.get()->close();
        h5File.get()->close();

        //Reopen file
        h5File = HdfFileFactory::openH5File(dataFile);

        //Get rootH5Group
        std::shared_ptr<H5::Group> rootH5Group(new H5::Group(h5File.get()->openGroup("/Reactor")));

        //Check values
        BOOST_REQUIRE_EQUAL(intNumber, HdfReaderFactory::readIntegerAttribute(rootH5Group, intName));
        BOOST_REQUIRE_EQUAL(doubleNumber, HdfReaderFactory::readDoubleAttribute(rootH5Group, doubleName));
        BOOST_REQUIRE_EQUAL(words, HdfReaderFactory::readStringAttribute(rootH5Group, strName));


    } catch (...) {
        BOOST_FAIL("HdfReaderFactory test failure! Exiting.");
    }

    return;
    // end-user-code

}

//Removes file and folder after test completes
BOOST_AUTO_TEST_CASE(afterClass) {

    //relative!
    std::string testFolder = "ICEIOTestsDir/";
    std::string testFile = testFolder + "test.h5";

    //Delete file
    remove(testFile.c_str());

    //Delete folder
    remove(testFolder.c_str());
}

BOOST_AUTO_TEST_SUITE_END()
