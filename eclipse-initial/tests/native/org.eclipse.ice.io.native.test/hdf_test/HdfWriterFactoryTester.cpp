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
#define BOOST_TEST_MODULE HdfWriterFactoryTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <HdfWriterFactory.h>
#include <HdfFileFactory.h>
#include <H5Cpp.h>
#include <memory>
#include <string>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_IO;


BOOST_AUTO_TEST_SUITE(HdfWriterFactoryTester_testSuite)



BOOST_AUTO_TEST_CASE(beforeClass) {
    //Create folder testPath in local directory - relative!
    std::string testFolder = "ICEIOTestsDir/";
    mkdir(testFolder.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    //Delete file
    std::string testFile = testFolder + "test.h5";
    remove(testFile.c_str());
}
BOOST_AUTO_TEST_CASE(checkCreators) {
    //TODO Auto-generated method stub
    // begin-user-code

    //Local Declarations
    std::string testFileName = "test1.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";
    std::shared_ptr<H5::H5File> h5File;

    bool exceptionHit0 = false;
    bool exceptionHit1 = false;
    bool exceptionHit2 = false;
    bool exceptionHit3 = false;
    bool exceptionHit4 = false;


    try {

        //Bad parameters
        std::shared_ptr<H5::H5File> badFile;
        std::string badString;
        std::shared_ptr<H5::Group> badparent;
        std::shared_ptr<H5::Group> reactorGroup;


        //Create a good H5File
        h5File = HdfFileFactory::createH5File(dataFile);

        //Create a bad Reactor group.  Will throw an exception
        try {
            reactorGroup = HdfWriterFactory::createH5Group(badFile, badString, badparent);
        } catch (...) {
            exceptionHit0 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit0, true);

        //Create a bad Reactor group
        try {
            reactorGroup = HdfWriterFactory::createH5Group(h5File, badString, badparent);
        } catch (...) {
            exceptionHit1 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit1, true);

        try {
            //Create a bad Reactor group
            reactorGroup = HdfWriterFactory::createH5Group(h5File, "asd", badparent);
        } catch (...) {
            exceptionHit2 = true;
        }
        BOOST_REQUIRE_EQUAL(exceptionHit2, true);

        //Get the root group
        std::shared_ptr<H5::Group> rootH5Group(new H5::Group(h5File->openGroup("/")));
        BOOST_REQUIRE_EQUAL((rootH5Group == NULL), false);

        //Create a good Reactor group
        reactorGroup = HdfWriterFactory::createH5Group(h5File, "Reactor", rootH5Group);
        BOOST_REQUIRE_EQUAL((reactorGroup == NULL), false);

        //Check to see if the group exists
        //Close the file
        h5File.get()->close();
        //Reopen
        h5File = HdfFileFactory::openH5File(dataFile);
        //Get the group and make sure it is not null
        std::shared_ptr<H5::Group> reactorGroup1(new H5::Group(h5File.get()->openGroup("Reactor")));
        BOOST_REQUIRE_EQUAL((reactorGroup1 == NULL), false);



        //Create a few good Geometry groups under the Reactor Group
        std::shared_ptr<H5::Group> geometryGroup1 = HdfWriterFactory::createH5Group(h5File, "Geometry1",
                reactorGroup1);

        std::shared_ptr<H5::Group> geometryGroup2 = HdfWriterFactory::createH5Group(h5File, "Geometry2",
                reactorGroup1);

        std::shared_ptr<H5::Group> geometryGroup3 = HdfWriterFactory::createH5Group(h5File, "Geometry3",
                reactorGroup1);

        //See if they exist
        //Close the file and groups
        reactorGroup1.get()->close();
        geometryGroup1.get()->close();
        geometryGroup2.get()->close();
        geometryGroup3.get()->close();
        h5File.get()->close();

        h5File = HdfFileFactory::openH5File(dataFile);

        //See if the three files exist.  These operations will throw exceptions if they fail (and prove that the groups do not exist)

        //Get the reactor group
        std::shared_ptr<H5::Group>reactorGroup2(new H5::Group(h5File.get()->openGroup("Reactor")));
        //Check other groups
        std::shared_ptr<H5::Group> geometryGroup1a(new H5::Group(reactorGroup2.get()->openGroup("Geometry1")));
        std::shared_ptr<H5::Group> geometryGroup2a(new H5::Group(reactorGroup2.get()->openGroup("Geometry2")));
        std::shared_ptr<H5::Group> geometryGroup3a(new H5::Group(reactorGroup2.get()->openGroup("Geometry3")));

        //Create an integer data type.  It does not necessarily need a h5File.
        H5::PredType integerDataType = HdfWriterFactory::createIntegerH5Datatype(h5File);

        //Create a float (double) data type.  It does not necessarily need a h5File.
        H5::PredType floatDataType = HdfWriterFactory::createFloatH5Datatype(h5File);

        //Close resources
        reactorGroup2.get()->close();
        geometryGroup1a.get()->close();
        geometryGroup2a.get()->close();
        geometryGroup3a.get()->close();


    } catch (...) {
        //Fail
        BOOST_FAIL("HdfWriterFactoryTester: illegal exception caught.  Failing test now.");
    }

    return;
    // end-user-code
}
BOOST_AUTO_TEST_CASE(checkWriters) {

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

    bool exceptionHit0 = false;
    bool exceptionHit1 = false;
    bool exceptionHit2 = false;
    bool exceptionHit3 = false;
    bool exceptionHit4 = false;

    //Create a good H5File
    h5File = HdfFileFactory::createH5File(dataFile);

    //Get root group
    std::shared_ptr<H5::Group> rootGroup(new H5::Group(h5File.get()->openGroup("/")));


    //Create a good Reactor group
    std::shared_ptr<H5::Group> reactorGroup = HdfWriterFactory::createH5Group(h5File,
            "Reactor", rootGroup);

    //Check double, string, and integer attribute with bad arguments
    //Double
    BOOST_REQUIRE_EQUAL(HdfWriterFactory::writeDoubleAttribute(badFile, badGroup, "double value", doubleNumber ), false);
    BOOST_REQUIRE_EQUAL(HdfWriterFactory::writeDoubleAttribute(h5File, badGroup, "double value", doubleNumber ), false);

    //String
    BOOST_REQUIRE_EQUAL(HdfWriterFactory::writeStringAttribute(badFile, badGroup, "string value", words ), false);
    BOOST_REQUIRE_EQUAL(HdfWriterFactory::writeStringAttribute(h5File, badGroup, "string value", words ), false);

    //Integer
    BOOST_REQUIRE_EQUAL(HdfWriterFactory::writeIntegerAttribute(badFile, badGroup, "int value", intNumber ), false);
    BOOST_REQUIRE_EQUAL(HdfWriterFactory::writeIntegerAttribute(h5File, badGroup, "int value", intNumber ), false);

    //Check double, string  and integer attribute with good arguments
    BOOST_REQUIRE_EQUAL(HdfWriterFactory::writeDoubleAttribute(h5File, reactorGroup, "double value", doubleNumber ), true);
    BOOST_REQUIRE_EQUAL(HdfWriterFactory::writeStringAttribute(h5File, reactorGroup, "string value", words ), true);
    BOOST_REQUIRE_EQUAL(HdfWriterFactory::writeIntegerAttribute(h5File, reactorGroup, "int value", intNumber ), true);

    //Close the file, reopen and check values
    h5File.get()->close();
    h5File = HdfFileFactory::openH5File(dataFile);

    //Reopen Group
    std::shared_ptr<H5::Group> openedGroup(new H5::Group(h5File.get()->openGroup("Reactor")));

    //Check to see if the attribute was written
    hid_t attributeDouble = H5Aopen(openedGroup.get()->getId(), "double value", H5P_DEFAULT);
    hid_t attributeInt = H5Aopen(openedGroup.get()->getId(), "int value", H5P_DEFAULT);
    hid_t attributeStr = H5Aopen(openedGroup.get()->getId(), "string value", H5P_DEFAULT);

    //Get dimensions of string
    hid_t atype = H5Aget_type(attributeStr);
    size_t size = H5Tget_size(atype);

    //Check values
    //Get variables
    double readDouble[1];
    int readInt[1];
    char readWords[size+1];

    //Read values
    H5Aread(attributeDouble, H5Aget_type(attributeDouble), readDouble);
    H5Aread(attributeInt, H5Aget_type(attributeInt), readInt);
    H5Aread(attributeStr, H5Aget_type(attributeStr), readWords);

    //Add null terminator
    readWords[size] = '\0';

    //convert character array to string
    std::string checkWords(readWords);

    //Compare
    BOOST_REQUIRE_EQUAL(readDouble[0], doubleNumber);
    BOOST_REQUIRE_EQUAL(readInt[0], intNumber);
    BOOST_REQUIRE_EQUAL(checkWords, words);

    //Free up memory
    //delete readWords;
    H5Aclose(attributeDouble);
    H5Aclose(attributeInt);
    H5Aclose(attributeStr);

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
