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
#define BOOST_TEST_MODULE LWRCompositeTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <LWRComponent.h>
#include <HDF5LWRTagType.h>
#include <string>
#include <vector>
#include <algorithm>
#include <HdfFileFactory.h>
#include <UtilityOperations.h>
#include <HdfReaderFactory.h>
#include <HdfWriterFactory.h>
#include "LWRData.h"
#include <H5Cpp.h>
#include <LWRComposite.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(LWRCompositeTester_testSuite)

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

    //Local declarations
    LWRComposite composite;
    HDF5LWRTagType type = LWRCOMPOSITE;

    //Default values
    std::string defaultName = "Composite 1";
    std::string defaultDescription = "Composite 1's Description";
    int defaultId = 1;

    //Check nullary constructor
    BOOST_REQUIRE_EQUAL(defaultName, composite.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, composite.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, composite.getId());
    BOOST_REQUIRE_EQUAL(type, composite.getHDF5LWRTag());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkComponent) {

    // begin-user-code
    // Local Declarations
    int compositeSize = 17;
    LWRComposite composite;
    std::string testComponentName = "Bob";
    std::string testComponentName2 = "Bill!";
    int rowLoc1 = 5, colLoc1 = 5;
    int rowLoc2 = 6, colLoc2 = 6;
    int testComponentId = 1000001;

    //Setup testComponents
    std::shared_ptr<LWRComponent> testComponent(new LWRComposite());
    std::shared_ptr<LWRComponent> testComponent2(new LWRComposite());
    std::shared_ptr<LWRComponent> testComponent3(new LWRComposite());

    //Set the ids on the test components
    testComponent.get()->setId(1);
    testComponent2.get()->setId(2);
    testComponent3.get()->setId(3);

    //Set the second component name
    testComponent2.get()->setName(testComponentName2);

    //Check the names, should be empty!
    BOOST_REQUIRE_EQUAL(0, composite.getComponentNames().size());

    // Try to get by name - valid string, empty string, and null
    BOOST_REQUIRE_EQUAL(composite.getComponent("validNameThatDoesNotExistInThere152423").get() == NULL, true);
    BOOST_REQUIRE_EQUAL(composite.getComponent("").get() == NULL, true);

    // Set the name
    testComponent.get()->setName(testComponentName);

    // Add to the composite
    composite.addComponent(testComponent);

    // Check the getting of a component
    //Cast up
    std::shared_ptr<LWRComponent> castedComponent(new LWRComponent(*(dynamic_cast<LWRComponent *> (composite.getComponent(testComponentName).get()))));
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator ==(*castedComponent.get()), true);

    // Add it in there
    composite.addComponent(testComponent2);

    //Check there are two in there, with separate names
    BOOST_REQUIRE_EQUAL(2, composite.getComponents().size());
    BOOST_REQUIRE_EQUAL(2, composite.getComponentNames().size());
    BOOST_REQUIRE_EQUAL(testComponent.get()->getName(), composite.getComponentNames()[1]);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), composite.getComponentNames()[0]);

    // Check values - see the components are different and they reside in
    // the table correctly
    std::shared_ptr<LWRComponent> castedComponent2(new LWRComponent(*(dynamic_cast<LWRComponent *> (composite.getComponent(testComponentName).get()))));
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator ==(*castedComponent2.get()), true);
    std::shared_ptr<LWRComponent> castedComponent3(new LWRComponent(*(dynamic_cast<LWRComponent *> (composite.getComponent(testComponentName2).get()))));
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator ==(*castedComponent3.get()), true);

    //Check the names, should contain 2!
    BOOST_REQUIRE_EQUAL(2, composite.getComponentNames().size());
    BOOST_REQUIRE_EQUAL(testComponentName, composite.getComponentNames()[1]);
    BOOST_REQUIRE_EQUAL(testComponentName2, composite.getComponentNames()[0]);


    // Finally, demonstrate what happens when a component of the same name
    // is added, it should not overwrite the previous item in the table!
    testComponent3.get()->setName(testComponentName); // Same name as the other
    // component
    testComponent3.get()->setId(testComponentId); // Id should differ from
    // testComponent!
    BOOST_REQUIRE_EQUAL(testComponent.get()->getId() == testComponentId, false);

    // Overwrite in table
    composite.addComponent(testComponent3);

    // Check that the object has not been overwritten
    std::shared_ptr<LWRComponent> castedComponent4(new LWRComponent(*(dynamic_cast<LWRComponent *> (composite.getComponent(testComponentName).get()))));
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator ==(*castedComponent4.get()), true);
    std::shared_ptr<LWRComponent> castedComponent5(new LWRComponent(*(dynamic_cast<LWRComponent *> (composite.getComponent(testComponentName).get()))));
    BOOST_REQUIRE_EQUAL(testComponent3.get()->operator ==(*castedComponent5.get()), false);


    // Remove the second component
    composite.removeComponent(testComponent2.get()->getName());

    // Check that it does not exist in the location or getting the name
    BOOST_REQUIRE_EQUAL(composite.getComponent(testComponent2.get()->getName()).get() == NULL, true);

    //Check that the first exists
    BOOST_REQUIRE_EQUAL(1, composite.getComponentNames().size());
    BOOST_REQUIRE_EQUAL(testComponentName, composite.getComponentNames()[0]);
    BOOST_REQUIRE_EQUAL(1, composite.getNumberOfComponents());
    std::shared_ptr<LWRComponent> castedComponent6(new LWRComponent(*(dynamic_cast<LWRComponent *> (composite.getComponent(1).get()))));
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator ==(*castedComponent6.get()), true);
    BOOST_REQUIRE_EQUAL(composite.getComponent(2).get() == NULL, true);

    //Remove the composite, check that it was removed from the map!
    composite.removeComponent(1);
    BOOST_REQUIRE_EQUAL(composite.getComponent(testComponent.get()->getName()).get() == NULL, true);
    BOOST_REQUIRE_EQUAL(0, composite.getComponents().size());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    LWRComposite object, equalObject, unEqualObject, transitiveObject;
    std::shared_ptr<LWRComponent> component( new LWRComponent("Billy the Plummer"));
    std::shared_ptr<LWRComponent> component2( new LWRComponent("Billy the Plummer2"));

    //Setup root object
    object.addComponent(component);
    object.addComponent(component2);

    //Setup equalObject equal to object
    equalObject.addComponent(component);
    equalObject.addComponent(component2);

    //Setup transitiveObject equal to object
    transitiveObject.addComponent(component);
    transitiveObject.addComponent(component2);

    // Set its data, not equal to object
    //Does not contain components!

    // Assert that these two objects are equal
    BOOST_REQUIRE_EQUAL(object==(equalObject), true);

    // Assert that two unequal objects returns false
    BOOST_REQUIRE_EQUAL(object==(unEqualObject) ,false);

    // Check that equals() is Reflexive
    // x==(x) = true
    BOOST_REQUIRE_EQUAL(object==(object), true);

    // Check that equals() is Symmetric
    // x==(y) = true iff y==(x) = true
    BOOST_REQUIRE_EQUAL(object==(equalObject) && equalObject==(object), true);

    // Check that equals() is Transitive
    // x==(y) = true, y==(z) = true => x==(z) = true
    if (object==(equalObject) && equalObject==(transitiveObject)) {
        BOOST_REQUIRE_EQUAL(object==(transitiveObject), true);
    } else {
        BOOST_FAIL("FAILURE IN EQUALITY CHECKING");
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

    //Local declarations
    LWRComposite object;

    //Values
    std::string name = "A LWRComponent!@!@#!#@56483";
    std::string description = "Description !@#!@#!@#!46546484328";
    int id = 68468431;
    std::shared_ptr<LWRComponent> component( new LWRComponent("Billy the Plummer"));
    std::shared_ptr<LWRComponent> component2( new LWRComponent("Billy the Plummer2"));

    //Initialize and Setup Object to test
    object.setName(name);
    object.setId(id);
    object.setDescription(description);
    object.addComponent(component);
    object.addComponent(component2);

    //Run the copy routine
    LWRComposite copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<LWRComposite> castedObject (new LWRComposite(*(dynamic_cast<LWRComposite *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    LWRComposite component;
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    std::shared_ptr<LWRComponent> component1( new LWRComponent("Billy the Plummer"));
    std::shared_ptr<LWRComponent> component2( new LWRComponent("Billy the Plummer2"));

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //exception hit
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);
    component.addComponent(component1);
    component.addComponent(component2);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL((component.getWriteableChildren().size() == 2), true);

    //Cast Writeables
    std::shared_ptr<LWRComponent> writeableComponent1 (new LWRComponent(*(dynamic_cast<LWRComponent *> (component.getWriteableChildren()[0].get()))));
    std::shared_ptr<LWRComponent> writeableComponent2 ( new LWRComponent(*(dynamic_cast<LWRComponent *> (component.getWriteableChildren()[1].get()))));
    BOOST_REQUIRE_EQUAL(component2.get()->getName(), writeableComponent2.get()->getName());
    BOOST_REQUIRE_EQUAL(component1.get()->getName(), writeableComponent1.get()->getName());

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
        BOOST_REQUIRE_EQUAL(0, h5Group.get()->getNumObjs());


        //Check the meta data
        BOOST_REQUIRE_EQUAL(4, h5Group.get()->getNumAttrs());

        //Values to check

        //Get attributes by reader factory and check values
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "id"), id);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "name"), name);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "description"), description);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "HDF5LWRTag"), UtilityOperations::toStringHDF5Tag(tag));



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
    BOOST_REQUIRE_EQUAL(1, h5Group.get()->getNumObjs());
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
    LWRComposite component;
    LWRComposite newComponent;
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    std::shared_ptr<LWRComponent> component1( new LWRComponent("Billy the Plummer"));
    std::shared_ptr<LWRComponent> component2( new LWRComponent("Billy the Plummer2"));
    std::shared_ptr<IHdfReadable> component1Read( new LWRComponent("Billy the Plummer"));
    std::shared_ptr<IHdfReadable> component2Read( new LWRComponent("Billy the Plummer2"));

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
    component.addComponent(component1);
    component.addComponent(component2);

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
        BOOST_REQUIRE_EQUAL(newComponent.readDatasets(h5Group2), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(component1Read), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(component2Read), true);

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
