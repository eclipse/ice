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
#define BOOST_TEST_MODULE HdfFileFactoryTester_testSuite
#include <boost/test/included/unit_test.hpp>

#include <string>
#include <fstream>
#include <stdio.h>
#include <iostream>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <H5Cpp.h>
#include <HdfFileFactory.h>
#include <memory>

using namespace ICE_IO;


BOOST_AUTO_TEST_SUITE(HdfFileFactoryTester_testSuite)

BOOST_AUTO_TEST_CASE(beforeClass) {

    //Create folder testPath in local directory - relative!
    std::string testFolder = "ICEIOTestsDir/";
    mkdir(testFolder.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    //Delete file
    std::string testFile = testFolder + "test.h5";
    remove(testFile.c_str());

}
BOOST_AUTO_TEST_CASE(checkFileOperations) {
    //TODO Auto-generated method stub
    // begin-user-code

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Exception grabbing handlers
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;
    bool exceptionHit2 = false;

    //Check file creation

    //Creating a file:

    //Try to create a file with ""
    try {
        HdfFileFactory::createH5File("");
    } catch (...) {
        exceptionHit0 = true;
    }

    BOOST_REQUIRE_EQUAL(exceptionHit0, true);

    //Create a file
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check file exists
    std::ifstream ifile(dataFile.c_str());
    BOOST_REQUIRE_EQUAL((ifile.good() == true), true);


    try {
        //Create a group
        std::shared_ptr<H5::Group> group(new H5::Group(h5File->createGroup("/Bob")));

        //Close the h5 resources
        group.get()->close();
        h5File.get()->close();

        //create a file ontop of a file - destroys file
        std::shared_ptr<H5::H5File> h5File2 = HdfFileFactory::createH5File(dataFile);

        //Check to see if bob exists
        try {
            std::shared_ptr<H5::Group> group2(new H5::Group(h5File2->openGroup("/Bob")));
        } catch (...) {
            exceptionHit1 = true; // Bob should not exist
        }
        BOOST_REQUIRE_EQUAL(exceptionHit1, true);

        //Close the file with the correct operation
        HdfFileFactory::closeH5File(h5File2);

        //Now, try to open that file again with open
        std::shared_ptr<H5::H5File> h5File3 = HdfFileFactory::openH5File(dataFile);

        BOOST_REQUIRE_EQUAL(H5::H5File::isHdf5(h5File3->getFileName()),  true);

        //Show that it exists
        BOOST_REQUIRE_EQUAL(dataFile, h5File3->getFileName());

        //Delete the dataFile (close the file first)
        h5File3.get()->close();

        //Delete the file
        BOOST_REQUIRE_EQUAL(0, remove(dataFile.c_str()));

        //Open a file that does not exist
        try {
            std::shared_ptr<H5::H5File> h5File4 = HdfFileFactory::openH5File(dataFile);
        } catch (...) {
            exceptionHit2 = true;
        }

        BOOST_REQUIRE_EQUAL(exceptionHit2, true);

    } catch (...) {

        //Fail the test hard
        BOOST_FAIL("HdfFileFactoryTester:  Exception hit within tests.  Test Failure.");
    }

    // end-user-code
    return;
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

BOOST_AUTO_TEST_SUITE_END( )
