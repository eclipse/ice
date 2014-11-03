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
#define BOOST_TEST_MODULE IncoreInstrumentTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <pwr/IncoreInstrument.h>
#include <Ring.h>
#include <memory>
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

BOOST_AUTO_TEST_SUITE(IncoreInstrumentTester_testSuite)

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

    //Default Values
    std::string defaultName = "Instrument 1";
    std::string defaultDesc = "Default Instrument";
    //New Values
    std::string newName = "Instrument 2";
    std::shared_ptr<Ring> newThimble (new Ring("thimble11"));
    HDF5LWRTagType type = INCORE_INSTRUMENT;
    std::string nullString;
    std::shared_ptr<Ring> nullThimble;

    //Check nullary constructor
    IncoreInstrument instrument;
    BOOST_REQUIRE_EQUAL(defaultName, instrument.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, instrument.getDescription());
    BOOST_REQUIRE_EQUAL(instrument.getThimble().get() != NULL, true);
    BOOST_REQUIRE_EQUAL(type, instrument.getHDF5LWRTag());

    //Check non-nullary constructor
    IncoreInstrument instrument2(newName, newThimble);
    BOOST_REQUIRE_EQUAL(newName, instrument2.getName());
    BOOST_REQUIRE_EQUAL(newThimble.get()->operator==(*instrument2.getThimble().get()), true);
    BOOST_REQUIRE_EQUAL(type, instrument2.getHDF5LWRTag());

    //Check non-nullary constructor illegal values illegal value is set to default
    IncoreInstrument instrument3(nullString, newThimble);
    BOOST_REQUIRE_EQUAL(defaultName, instrument3.getName());
    BOOST_REQUIRE_EQUAL(newThimble, instrument3.getThimble());
    BOOST_REQUIRE_EQUAL(type, instrument3.getHDF5LWRTag());

    IncoreInstrument instrument4(nullString, nullThimble);
    BOOST_REQUIRE_EQUAL(defaultName, instrument4.getName());
    BOOST_REQUIRE_EQUAL(instrument4.getThimble() != NULL, true);
    BOOST_REQUIRE_EQUAL(type, instrument4.getHDF5LWRTag());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkThimble) {

    // begin-user-code

    //check a legal value set
    IncoreInstrument instrument;
    std::shared_ptr<Ring> newThimble (new Ring("thimble11"));
    std::shared_ptr<Ring> nullThimble;

    //Set Thimble
    instrument.setThimble(newThimble);
    BOOST_REQUIRE_EQUAL(newThimble.get()->operator==(*instrument.getThimble().get()), true);

    //check illegal value set
    instrument.setThimble(nullThimble);
    BOOST_REQUIRE_EQUAL(newThimble.get()->operator==(*instrument.getThimble().get()), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations

    std::string name = "Instruments!";
    std::shared_ptr<Ring> thimble (new Ring("THIMBLE!"));
    std::shared_ptr<Ring> unEqualThimble (new Ring("UNEQUALTHIMBLE!"));

    //Setup root object
    IncoreInstrument object(name, thimble);

    //Setup equalObject equal to object
    IncoreInstrument equalObject(name, thimble);

    //Setup transitiveObject equal to object
    IncoreInstrument transitiveObject(name, thimble);

    // Set its data, not equal to object
    //Does not contain components!
    IncoreInstrument unEqualObject(name, unEqualThimble);

    // Assert that these two objects are equal
    BOOST_REQUIRE_EQUAL(object==(equalObject), true);

    // Assert that two unequal objects returns false
    BOOST_REQUIRE_EQUAL(object==(unEqualObject), false);

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
        BOOST_FAIL("FAILURE IN EQUALITY CHECK! EXITING!");
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

    std::string name = "Instruments!";
    std::shared_ptr<Ring> thimble (new Ring("THIMBLE!"));

    //Setup root object
    IncoreInstrument object(name, thimble);

    //Run the copy routine
    IncoreInstrument copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<IncoreInstrument> castedObject (new IncoreInstrument(*(dynamic_cast<IncoreInstrument *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    //end-user-code
}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    //begin-user-code

    //Local Declarations
    IncoreInstrument component;
    std::string name = "Phat stacks!";
    std::string description = "A description of awesome magnitudes!";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;

    //Setup Rings
    std::shared_ptr<Ring> ring1( new Ring("thimble", std::shared_ptr<Material> (new Material("Billy")), 20.0, 5.0, 10.0));


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
    component.setThimble(ring1);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL((component.getWriteableChildren().size() == 1), true);
    //Check the Ring
    std::shared_ptr<Ring> writeableRing1 (new Ring(*(dynamic_cast<Ring *> (component.getWriteableChildren()[0].get()))));
    BOOST_REQUIRE_EQUAL(ring1.get()->getName(), writeableRing1.get()->getName());

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

    //Local Declarations
    IncoreInstrument component;
    IncoreInstrument newComponent;
    std::shared_ptr<IHdfReadable> nullComponent (new LWRComponent());
    std::string name = "Phat stacks!";
    std::string description = "A description of awesome magnitudes!";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;

    //Setup Rings
    std::shared_ptr<Ring> ring1( new Ring("thimbleOfAwesome", std::shared_ptr<Material> (new Material("Billy")), 20.0, 5.0, 10.0));


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
    component.setThimble(ring1);

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

        //Null for readChild material check
        BOOST_REQUIRE_EQUAL(component.readChild(nullComponent), false);

        //Persist data
        h5File.get()->close();
        h5Group1.get()->close();

        //Reopen file
        h5File =  HdfFileFactory::openH5File(dataFile);

        //Call creator
        std::shared_ptr<H5::Group> h5Group2 (new H5::Group (h5File.get()->openGroup("/Group1")));

        //Read information
        BOOST_REQUIRE_EQUAL(newComponent.readAttributes(h5Group2), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(ring1), true);
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
