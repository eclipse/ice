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
#define BOOST_TEST_MODULE MaterialBlockTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <MaterialBlock.h>
#include <Ring.h>
#include <memory>
#include <string>
#include <vector>
#include <H5Cpp.h>
#include "LWRComponent.h"
#include "MaterialType.h"
#include "UtilityOperations.h"
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <HdfFileFactory.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(MaterialBlockTester_testSuite)

BOOST_AUTO_TEST_CASE(beforeClass) {

    //Create folder testPath in local directory - relative!
    std::string testFolder = "ICEIOTestsDir/";
    mkdir(testFolder.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    //Delete file
    std::string testFile = testFolder + "test.h5";
    remove(testFile.c_str());

}

BOOST_AUTO_TEST_CASE(checkRingMutators) {

    // begin-user-code

    //Local Declarations
    MaterialBlock materialBlock;
    std::shared_ptr<Ring> nullRing;
    std::string nullString;

    //Check for adding a null ring
    BOOST_REQUIRE_EQUAL(materialBlock.addRing(nullRing), false);

    //Check for adding a ring
    std::shared_ptr<Ring> ring1 (new Ring());
    ring1.get()->setName("Ring 1");
    ring1.get()->setOuterRadius(10);
    ring1.get()->setInnerRadius(9);
    BOOST_REQUIRE_EQUAL(materialBlock.addRing(ring1), true);


    //Check for adding another ring
    std::shared_ptr<Ring> ring2 (new Ring());
    ring2.get()->setName("Ring 2");
    ring2.get()->setOuterRadius(4);
    ring2.get()->setInnerRadius(3);
    BOOST_REQUIRE_EQUAL(materialBlock.addRing(ring2), true);

    //Check for adding an overlapping ring
    std::shared_ptr<Ring> ring3 (new Ring());
    ring3.get()->setName("Ring 3");
    ring3.get()->setOuterRadius(3.5);
    ring3.get()->setInnerRadius(2.5);
    BOOST_REQUIRE_EQUAL(materialBlock.addRing(ring3),false);

    //Check for getting a ring by a null name
    std::shared_ptr<Ring> ring4 = materialBlock.getRing(nullString);
    BOOST_REQUIRE_EQUAL(ring4 == NULL, true);

    //Check for getting a ring by a name that does not exist
    std::shared_ptr<Ring> ring5 = materialBlock.getRing("Harold");
    BOOST_REQUIRE_EQUAL(ring5 == NULL, true);

    //Check for getting a ring by a name that does exist
    std::shared_ptr<Ring> ring6 = materialBlock.getRing("Ring 2");
    BOOST_REQUIRE_EQUAL(ring6.get() != NULL, true);
    BOOST_REQUIRE_EQUAL(ring6.get()->operator ==(*ring2.get()), true);
    BOOST_REQUIRE_EQUAL(ring6.get()->getName(), "Ring 2");
    BOOST_REQUIRE_EQUAL(ring6.get()->getOuterRadius() == 4, true);
    BOOST_REQUIRE_EQUAL(ring6.get()->getInnerRadius() == 3, true);

    //Check for getting a ring by a radius not in the set of Rings
    std::shared_ptr<Ring> ring7 = materialBlock.getRing(2.5);
    BOOST_REQUIRE_EQUAL(ring7 == NULL, true);

    //Check for getting a ring by a radius in the set of Rings
    std::shared_ptr<Ring> ring8 = materialBlock.getRing(3.5);
    BOOST_REQUIRE_EQUAL(ring8.get() != NULL, true);
    BOOST_REQUIRE_EQUAL(ring8.get()->operator== (*ring2.get()), true);
    BOOST_REQUIRE_EQUAL(ring8.get()->getName(), "Ring 2");
    BOOST_REQUIRE_EQUAL(ring8.get()->getOuterRadius() == 4, true);
    BOOST_REQUIRE_EQUAL(ring8.get()->getInnerRadius() == 3, true);

    //Check for getting the set of rings
    std::vector< std::shared_ptr <Ring> > list = materialBlock.getRings();
    BOOST_REQUIRE_EQUAL(list.size() == 2, true);
    BOOST_REQUIRE_EQUAL(list[0].get()->operator ==(*ring1.get()), true);
    BOOST_REQUIRE_EQUAL(list[1].get()->operator ==(*ring2.get()), true);

    //Check for removing a ring by a null name
    BOOST_REQUIRE_EQUAL(materialBlock.removeRing(nullString), false);
    list = materialBlock.getRings();
    BOOST_REQUIRE_EQUAL(list.size() == 2, true);
    BOOST_REQUIRE_EQUAL(list[0].get()->operator ==(*ring1.get()), true);
    BOOST_REQUIRE_EQUAL(list[1].get()->operator ==(*ring2.get()), true);

    //Check for removing a ring that is not in the set
    BOOST_REQUIRE_EQUAL(materialBlock.removeRing("Harold"), false);
    list = materialBlock.getRings();
    BOOST_REQUIRE_EQUAL(list.size() == 2, true);
    BOOST_REQUIRE_EQUAL(list[0].get()->operator ==(*ring1.get()), true);
    BOOST_REQUIRE_EQUAL(list[1].get()->operator ==(*ring2.get()), true);

    //Check for removing a ring that is in the set
    BOOST_REQUIRE_EQUAL(materialBlock.removeRing("Ring 1"), true);
    list = materialBlock.getRings();
    BOOST_REQUIRE_EQUAL(list.size() == 1, true);
    BOOST_REQUIRE_EQUAL(list[0].get()->operator ==(*ring2.get()), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkConstruction) {

    // begin-user-code

    //Local Declaration
    MaterialBlock materialBlock;
    //Default values
    std::string defaultName = "MaterialBlock 1";
    std::string defaultDesc = "MaterialBlock 1's Description";
    int defaultId = 1;
    HDF5LWRTagType type = MATERIALBLOCK;

    //check values
    BOOST_REQUIRE_EQUAL(defaultName, materialBlock.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, materialBlock.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, materialBlock.getId());
    BOOST_REQUIRE_EQUAL(type, materialBlock.getHDF5LWRTag());
    BOOST_REQUIRE_EQUAL(0.0, materialBlock.getPosition());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    //Setup Rings
    std::shared_ptr<Ring> ring1( new Ring("Bob", std::shared_ptr<Material> (new Material("Billy")), 20.0, 5.0, 10.0));
    std::shared_ptr<Ring> ring2( new Ring("Bob2", std::shared_ptr<Material> (new Material("Billy2")), 22.0, 52.0, 102.0));

    //Setup root object
    MaterialBlock object;
    object.addRing(ring1);
    object.addRing(ring2);

    //Setup equalObject equal to object
    MaterialBlock equalObject;
    equalObject.addRing(ring1);
    equalObject.addRing(ring2);

    //Setup transitiveObject equal to object
    MaterialBlock transitiveObject;
    transitiveObject.addRing(ring1);
    transitiveObject.addRing(ring2);

    // Set its data, not equal to object
    MaterialBlock unEqualObject;
    unEqualObject.addRing(ring2);

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
        BOOST_FAIL("FAILURE IN EQUALITY CHECKING! EXITING");
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

BOOST_AUTO_TEST_CASE(checkPosition) {

	//begin-user-code
	std::shared_ptr<MaterialBlock> block (new MaterialBlock());

	//Check default value
	BOOST_REQUIRE_EQUAL(0.0, block.get()->getPosition());

	//try to set to negative
	block.get()->setPosition(-1.0);
	BOOST_REQUIRE_EQUAL(0.0, block.get()->getPosition());

	//try to set to positive
	block.get()->setPosition(1.0);
	BOOST_REQUIRE_EQUAL(1.0, block.get()->getPosition());

	//try to set to zero
	block.get()->setPosition(0.0);
	BOOST_REQUIRE_EQUAL(0.0, block.get()->getPosition());

	//end-user-code

}

BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local Declarations
    std::string name = "Phat materialBlocks!";
    //Setup Rings
    std::shared_ptr<Ring> ring1( new Ring("Bob", std::shared_ptr<Material> (new Material("Billy")), 20.0, 5.0, 10.0));
    std::shared_ptr<Ring> ring2( new Ring("Bob2", std::shared_ptr<Material> (new Material("Billy2")), 22.0, 52.0, 102.0));
    double position = 1.0;

    //Setup root object
    MaterialBlock object;
    object.addRing(ring1);
    object.addRing(ring2);
    object.setName(name);
    object.setPosition(position);

    //Run the copy routine
    MaterialBlock copyObject(object);

    BOOST_REQUIRE_EQUAL(copyObject == object, true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<MaterialBlock> castedObject (new MaterialBlock(*(dynamic_cast<MaterialBlock *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    //begin-user-code

    //Local Declarations
    MaterialBlock component;
    std::string name = "Phat materialBlocks!";
    std::string description = "A description of awesome magnitudes!";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    double position = 1.0;

    //Setup Rings
    std::shared_ptr<Ring> ring1( new Ring("Bob", std::shared_ptr<Material> (new Material("Billy")), 20.0, 5.0, 10.0));
    std::shared_ptr<Ring> ring2( new Ring("Bob2", std::shared_ptr<Material> (new Material("Billy2")), 22.0, 52.0, 102.0));


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
    component.addRing(ring1);
    component.addRing(ring2);
    component.setPosition(position);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL((component.getWriteableChildren().size() == 2), true);
    //Check the material
    std::shared_ptr<Ring> writeableRing1 (new Ring(*(dynamic_cast<Ring *> (component.getWriteableChildren()[0].get()))));
    BOOST_REQUIRE_EQUAL(ring1.get()->getName(), writeableRing1.get()->getName());
    std::shared_ptr<Ring> writeableRing2 (new Ring(*(dynamic_cast<Ring *> (component.getWriteableChildren()[1].get()))));
    BOOST_REQUIRE_EQUAL(ring2.get()->getName(), writeableRing2.get()->getName());

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
        BOOST_REQUIRE_EQUAL(5, h5Group.get()->getNumAttrs());

        //Values to check

        //Get attributes by reader factory and check values
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "id"), id);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "name"), name);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "description"), description);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "HDF5LWRTag"), UtilityOperations::toStringHDF5Tag(tag));
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(h5Group, "position"), position);



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
    MaterialBlock component;
    MaterialBlock newComponent;
    std::shared_ptr<IHdfReadable> nullComponent (new LWRComponent());
    std::string name = "Phat materialBlocks!";
    std::string description = "A description of awesome magnitudes!";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    std::string sourceInfo = "ASDASDASD";
    std::string timeUnits = "UNITS OF AWESOME";
    double time = 1.0;
    double position = 1.0;

    //Setup Rings
    std::shared_ptr<Ring> ring1( new Ring("Bob", std::shared_ptr<Material> (new Material("Billy")), 20.0, 5.0, 10.0));
    std::shared_ptr<Ring> ring2( new Ring("Bob2", std::shared_ptr<Material> (new Material("Billy2")), 22.0, 52.0, 102.0));


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
    component.addRing(ring1);
    component.addRing(ring2);
    component.setPosition(position);

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
        BOOST_REQUIRE_EQUAL(newComponent.readChild(ring2), true);
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
