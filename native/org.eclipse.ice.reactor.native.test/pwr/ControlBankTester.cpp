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
#define BOOST_TEST_MODULE ControlBankTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <pwr/ControlBank.h>
#include <LWRComponent.h>
#include <HDF5LWRTagType.h>
#include <string>
#include <vector>
#include <algorithm>
#include <HdfFileFactory.h>
#include <UtilityOperations.h>
#include <HdfReaderFactory.h>
#include <HdfWriterFactory.h>
#include <H5Cpp.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(ControlBankTester_testSuite)

BOOST_AUTO_TEST_CASE(beforeClass) {

    //Create folder testPath in local directory - relative!
    std::string testFolder = "ICEIOTestsDir/";
    mkdir(testFolder.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    //Delete file
    std::string testFile = testFolder + "test.h5";
    remove(testFile.c_str());

}

BOOST_AUTO_TEST_CASE(checkConstruction) {

    // begin-user-code

    //Local Declarations
    //Default Values
    std::string defaultName = "ControlBank 1";
    std::string defaultDesc = "Default Control Bank";
    int defaultId = 1;
    double defaultStepSize = 0.0;
    int defaultMaxNumberOfSteps = 1;
    HDF5LWRTagType type = CONTROL_BANK;
    std::string nullString;

    //New Values
    std::string newName = "ControlBank 2";
    double newStepSize = 10.0;
    int newMaxNumberOfSteps = 100;

    //Check nullary constructor
    ControlBank controlBank;
    BOOST_REQUIRE_EQUAL(defaultName, controlBank.getName());
    BOOST_REQUIRE_EQUAL(defaultStepSize, controlBank.getStepSize());
    BOOST_REQUIRE_EQUAL(defaultMaxNumberOfSteps, controlBank.getMaxNumberOfSteps());
    BOOST_REQUIRE_EQUAL(defaultId, controlBank.getId());
    BOOST_REQUIRE_EQUAL(defaultDesc, controlBank.getDescription());
    BOOST_REQUIRE_EQUAL(type, controlBank.getHDF5LWRTag());

    //Check non-nullary constructor
    ControlBank controlBank2(newName, newStepSize, newMaxNumberOfSteps);
    BOOST_REQUIRE_EQUAL(newName, controlBank2.getName());
    BOOST_REQUIRE_EQUAL(newStepSize, controlBank2.getStepSize());
    BOOST_REQUIRE_EQUAL(newMaxNumberOfSteps, controlBank2.getMaxNumberOfSteps());
    BOOST_REQUIRE_EQUAL(type, controlBank2.getHDF5LWRTag());

    //Check non-nullary constructor for illegal values
    ControlBank controlBank3 (nullString, -1.0, -1);
    BOOST_REQUIRE_EQUAL(defaultName, controlBank3.getName());
    BOOST_REQUIRE_EQUAL(defaultStepSize, controlBank3.getStepSize());
    BOOST_REQUIRE_EQUAL(defaultMaxNumberOfSteps, controlBank3.getMaxNumberOfSteps());
    BOOST_REQUIRE_EQUAL(type, controlBank3.getHDF5LWRTag());

    //Check  non-nullary constructor for 0 value of MaxNumberOfSteps
    ControlBank controlBank4(newName, newStepSize, 0);
    BOOST_REQUIRE_EQUAL(newName, controlBank4.getName());
    BOOST_REQUIRE_EQUAL(newStepSize, controlBank4.getStepSize());
    BOOST_REQUIRE_EQUAL(defaultMaxNumberOfSteps, controlBank4.getMaxNumberOfSteps());
    BOOST_REQUIRE_EQUAL(type, controlBank4.getHDF5LWRTag());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkStepSize) {

    // begin-user-code

    //Local Declarations
    ControlBank controlBank;

    //Set the size to 0
    controlBank.setStepSize(0.0);

    //This checks a valid return after the step size is set
    BOOST_REQUIRE_EQUAL(0.0, controlBank.getStepSize());

    //Pass an invalid number: negative
    controlBank.setStepSize(-1.0);

    //Should not change the previous value.
    BOOST_REQUIRE_EQUAL(0.0, controlBank.getStepSize());

    //Test a valid length
    controlBank.setStepSize(10.0);

    //Should set correct value.
    BOOST_REQUIRE_EQUAL(10.0, controlBank.getStepSize());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkMaxNumberOfSteps) {

    // begin-user-code

    //Local Declarations
    ControlBank controlBank;

    //Set the Max Step Size to default
    controlBank.setMaxNumberOfSteps(1);

    //This checks a valid return after the Max step size is set
    BOOST_REQUIRE_EQUAL(1, controlBank.getMaxNumberOfSteps());

    //Pass an invalid number: negative
    controlBank.setMaxNumberOfSteps(-1);

    //Should not change the previous value.
    BOOST_REQUIRE_EQUAL(1, controlBank.getMaxNumberOfSteps());

    //Test a valid length
    controlBank.setMaxNumberOfSteps(10);

    //Should set correct value.
    BOOST_REQUIRE_EQUAL(10, controlBank.getMaxNumberOfSteps());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkStrokeLength) {

    // begin-user-code

    //Local Declarations
    ControlBank controlBank;

    //Checking default values.
    BOOST_REQUIRE_EQUAL(1, controlBank.getMaxNumberOfSteps());
    BOOST_REQUIRE_EQUAL(0.0, controlBank.getStepSize());

    //Check valid test
    BOOST_REQUIRE_EQUAL(0.0, controlBank.getStrokeLength());

    //Check nonzero
    controlBank.setStepSize(100.0);

    //Check valid test
    BOOST_REQUIRE_EQUAL(100.0, controlBank.getStrokeLength());

    //Check valid test - decimal
    controlBank.setMaxNumberOfSteps(2);
    controlBank.setStepSize(50.05);

    //Check valid test
    BOOST_REQUIRE_EQUAL(100.1, controlBank.getStrokeLength());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations

    std::string name = "CONTROL!";
    double stepSize = 5;
    int maxNumberOfSteps = 10;
    int unEqualMaxNumberOfSteps = 9;

    //Setup root object
    ControlBank object(name, stepSize, maxNumberOfSteps);

    //Setup equalObject equal to object
    ControlBank equalObject(name, stepSize, maxNumberOfSteps);

    //Setup transitiveObject equal to object
    ControlBank transitiveObject(name, stepSize, maxNumberOfSteps);

    // Set its data, not equal to object
    //Does not contain components!
    ControlBank unEqualObject(name, stepSize, unEqualMaxNumberOfSteps);

    // Assert that these two objects are equal
    BOOST_REQUIRE_EQUAL(object==(equalObject), true);

    // Assert that two unequal objects returns false
    BOOST_REQUIRE_EQUAL(object==(unEqualObject), false);

    // Check that equals() is Reflexive
    // x.equals(x) = true
    BOOST_REQUIRE_EQUAL(object==(object), true);

    // Check that equals() is Symmetric
    // x.equals(y) = true iff y.equals(x) = true
    BOOST_REQUIRE_EQUAL(object==(equalObject) && equalObject==(object), true);

    // Check that equals() is Transitive
    // x.equals(y) = true, y.equals(z) = true => x.equals(z) = true
    if (object==(equalObject) && equalObject==(transitiveObject)) {
        BOOST_REQUIRE_EQUAL(object==(transitiveObject), true);
    } else {
        BOOST_FAIL("FAILURE IN EQUALITY CHECK! EXITING");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(object==(equalObject) && object==(equalObject)
                        && object==(equalObject), true);
    BOOST_REQUIRE_EQUAL(!(object==(unEqualObject))
                        && !(object==(unEqualObject))
                        && !(object==(unEqualObject)), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local Declarations

    std::string name = "CONTROL!";
    double stepSize = 5;
    int maxNumberOfSteps = 10;

    //Setup root object
    ControlBank object(name, stepSize, maxNumberOfSteps);

    //Run the copy routine
    ControlBank copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<ControlBank> castedObject (new ControlBank(*(dynamic_cast<ControlBank *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);


    return;

    //end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {


    // begin-user-code

    //Local Declarations
    ControlBank component;
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    double stepSize = 5;
    int maxNumberOfSteps = 10;

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Exceptions
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);
    component.setMaxNumberOfSteps(maxNumberOfSteps);
    component.setStepSize(stepSize);


    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL((component.getWriteableChildren().size() == 0), true);

    //Check writing attributes
    std::shared_ptr<H5::Group> h5Group1 (new H5::Group(h5File.get()->createGroup("/Group1")));

    //Pass the group and file to the writer for attributes
    //See that it passes
    BOOST_REQUIRE_EQUAL(component.writeAttributes(h5File, h5Group1), true);

    //Bad pointer checks
    std::shared_ptr<H5::Group> badGroup;
    std::shared_ptr<H5::H5File> badFile;

    //Check dataSet.  Pass null to show it return false
    BOOST_REQUIRE_EQUAL(component.writeDatasets(badFile, badGroup), false);
    BOOST_REQUIRE_EQUAL(component.writeDatasets(badFile, h5Group1), false);
    BOOST_REQUIRE_EQUAL(component.writeDatasets(h5File, badGroup), false);

    //Check dataset
    BOOST_REQUIRE_EQUAL(component.writeDatasets(h5File, h5Group1), true);

    //Close group and then reopen
    try {
        h5File.get()->close();
        //free resources
        h5Group1.get()->close();

        //Open file
        h5File =  HdfFileFactory::openH5File(dataFile);

    } catch (...) {
        BOOST_FAIL("FAILED IN checkHdfWriteables!  Exiting");
    }

    //Get the group again
    std::shared_ptr<H5::Group> h5Group (new H5::Group(h5File.get()->openGroup("/Group1")));

    //Check attributes

    try {
        //Show that there is one group made at this time
        BOOST_REQUIRE_EQUAL(1, h5Group.get()->getNumObjs());


        //Check the meta data
        BOOST_REQUIRE_EQUAL(6, h5Group.get()->getNumAttrs());

        //Values to check

        //Get attributes by reader factory and check values
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "id"), id);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "name"), name);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "description"), description);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "HDF5LWRTag"), UtilityOperations::toStringHDF5Tag(tag));
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "maxNumberOfSteps"), maxNumberOfSteps);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(h5Group, "stepSize"), stepSize);

    } catch (...) {
        BOOST_FAIL("Failure 2 in checkIHdfWritables!  Exiting");
    }

    //Make sure the writeAttributes fail for invalid stuff
    try {
        component.createGroup(h5File, badGroup);
    } catch(...) {
        exceptionHit0 = true;
    }
    BOOST_REQUIRE_EQUAL(exceptionHit0, true);

    try {
        component.createGroup(badFile, h5Group);
    } catch(...) {
        exceptionHit1 = true;
    }
    BOOST_REQUIRE_EQUAL(exceptionHit1, true);

    //Check Group Creation
    std::shared_ptr<H5::Group> group2 = component.createGroup(h5File, h5Group);
    //See that the previous group has a group
    BOOST_REQUIRE_EQUAL(2, h5Group.get()->getNumObjs());
    //Check that it has the same name as the root component
    BOOST_REQUIRE_EQUAL(component.getName(), h5Group.get()->getObjnameByIdx(0));
    //Check that the returned group is a Group but no members
    BOOST_REQUIRE_EQUAL(0, group2.get()->getNumObjs());
    BOOST_REQUIRE_EQUAL(0, group2.get()->getNumAttrs());

    //Close resources
    h5Group.get()->close();
    group2.get()->close();
    h5File.get()->close();

    //Delete the file

    //Delete file
    remove(dataFile.c_str());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Readables) {
    // begin-user-code

    //Local Declarations
    ControlBank component;
    ControlBank newComponent;
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    double stepSize = 5;
    int maxNumberOfSteps = 10;

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Exceptions
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);
    component.setStepSize(stepSize);
    component.setMaxNumberOfSteps(maxNumberOfSteps);

    //Do readChild checks here. For LWRComponent this method always returns true
    std::shared_ptr<LWRComponent> nulledComponent;
    BOOST_REQUIRE_EQUAL(component.readChild(nulledComponent), true);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check reading attributes

    //Create a group
    std::shared_ptr<H5::Group> h5Group1 (new H5::Group (h5File.get()->createGroup("/Group1")));

    //Use the writers.
    //Yes, this creates a dependency on the writers to work before the readers, but for testing scalability concerns, this class needs to be edited as little as possible.
    //Since the java code will change drastically, this is the way I have chosen to do it.  "Don't re-invent the wheel" - SFH @ 02122012@1223 hrs

    try {
        //Write everything from component
        BOOST_REQUIRE_EQUAL(component.writeAttributes(h5File, h5Group1), true);
        BOOST_REQUIRE_EQUAL(component.writeDatasets(h5File, h5Group1), true);

        //Persist data
        h5File.get()->close();
        h5Group1.get()->close();

        //Reopen file
        h5File =  HdfFileFactory::openH5File(dataFile);

        //Call creator
        std::shared_ptr<H5::Group> h5Group2 (new H5::Group (h5File.get()->openGroup("/Group1")));

        //Read information
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(h5Group2), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(nulledComponent), true);
        BOOST_REQUIRE_EQUAL(newComponent.readDatasets(h5Group2), true);

        //Check with setup component
        BOOST_REQUIRE_EQUAL(component==newComponent, true);

        //Now, lets try to set an erroneous H5Group with missing data
        h5Group2.get()->removeAttr("name");

        //Save, close, reopen
        //Persist data
        h5File.get()->close();
        h5Group1.get()->close();

        //Reopen file
        h5File =  HdfFileFactory::openH5File(dataFile);

        //Call creator
        std::shared_ptr<H5::Group> h5Group3 (new H5::Group (h5File.get()->openGroup("/Group1")));

        //Run it through
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(h5Group3), false);
        //Check it does not change
        BOOST_REQUIRE_EQUAL(component==newComponent, true);

        //Check for nullaries
        std::shared_ptr<H5::Group> badGroup;
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(badGroup), false);

        //Doesn't change anything
        BOOST_REQUIRE_EQUAL(component==newComponent, true);

        //Close Resources
        h5File.get()->close();
        h5Group3.get()->close();


    } catch (...) {
        BOOST_FAIL("FAILURE IN LWRCOMPONENTTESTER::checkReadables.  Exiting");
    }

    //Delete the file

    //Delete file
    remove(dataFile.c_str());

    return;

    // end-user-code

}
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
