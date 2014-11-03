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
#define BOOST_TEST_MODULE MaterialTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <Material.h>
#include <MaterialType.h>
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
#include <UtilityOperations.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(MaterialTester_testSuite)

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
    std::string defaultName = "Material";
    std::string defaultDesc = "Material's Description";
    std::string newName = "Special Material";
    std::string nullString;
    MaterialType defaultType = SOLID;
    MaterialType newType = GAS;
    int defaultId = 1;
    HDF5LWRTagType type = MATERIAL;

    //Instantiate material and check default values
    Material material;
    BOOST_REQUIRE_EQUAL(defaultName, material.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, material.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, material.getId());
    BOOST_REQUIRE_EQUAL(defaultType, material.getMaterialType());
    BOOST_REQUIRE_EQUAL(type, material.getHDF5LWRTag());

    //Instantiate material with different name and check default values
    Material material2(newName);
    BOOST_REQUIRE_EQUAL(newName, material2.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, material2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, material2.getId());
    BOOST_REQUIRE_EQUAL(defaultType, material2.getMaterialType());
    BOOST_REQUIRE_EQUAL(type, material2.getHDF5LWRTag());

    //Instantiate material incorrectly and check default values
    Material material3(nullString);
    BOOST_REQUIRE_EQUAL(defaultName, material3.getName()); //Name defaults
    BOOST_REQUIRE_EQUAL(defaultDesc, material3.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, material3.getId());
    BOOST_REQUIRE_EQUAL(defaultType, material3.getMaterialType());
    BOOST_REQUIRE_EQUAL(type, material.getHDF5LWRTag());

    //Instantiate material with different name and different type.  Check values
    Material material4(newName, newType);
    BOOST_REQUIRE_EQUAL(newName, material4.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, material4.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, material4.getId());
    BOOST_REQUIRE_EQUAL(newType, material4.getMaterialType());
    BOOST_REQUIRE_EQUAL(type, material4.getHDF5LWRTag());

    //Instantiate material with erroneous name, but not type - check defaults
    Material material5(nullString, newType);
    BOOST_REQUIRE_EQUAL(defaultName, material5.getName()); //defaults
    BOOST_REQUIRE_EQUAL(defaultDesc, material5.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, material5.getId());
    BOOST_REQUIRE_EQUAL(newType, material5.getMaterialType());
    BOOST_REQUIRE_EQUAL(type, material5.getHDF5LWRTag());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkMaterialType) {

    // begin-user-code

    //Local Declarations
    Material material;
    MaterialType defaultType = SOLID;

    //Check default material type value
    BOOST_REQUIRE_EQUAL(defaultType, material.getMaterialType());

    //Set the material type - SOLID
    material.setMaterialType(SOLID);
    //Check value
    BOOST_REQUIRE_EQUAL(SOLID, material.getMaterialType());

    //Set the material type - LIQUID
    material.setMaterialType(LIQUID);
    //Check value
    BOOST_REQUIRE_EQUAL(LIQUID, material.getMaterialType());

    //Set the material type - GAS
    material.setMaterialType(GAS);
    //Check value
    BOOST_REQUIRE_EQUAL(GAS, material.getMaterialType());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    std::string name = "Bill";
    MaterialType type = GAS;
    MaterialType unEqualType = LIQUID;
    std::string description = "Description !@#!@#!@#!46546484328";
    int id = 68468431;

    //Setup root object
    Material object(name);
    object.setId(id);
    object.setDescription(description);
    object.setMaterialType(type);

    //Setup equalObject equal to object
    Material equalObject(name);
    equalObject.setId(id);
    equalObject.setDescription(description);
    equalObject.setMaterialType(type);

    //Setup transitiveObject equal to object
    Material transitiveObject(name);
    transitiveObject.setId(id);
    transitiveObject.setDescription(description);
    transitiveObject.setMaterialType(type);

    // Set its data, not equal to object
    //Does not contain components!
    Material unEqualObject(name);
    unEqualObject.setId(id);
    unEqualObject.setDescription(description);
    unEqualObject.setMaterialType(unEqualType);

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
        BOOST_FAIL("EQUALITY CHECK FAILED.  EXITING");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(object==(equalObject) && object==(equalObject) && object==(equalObject), true);
    BOOST_REQUIRE_EQUAL(!(object==(unEqualObject)) && !(object==(unEqualObject)) && !(object==(unEqualObject)), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local declarations
    std::string name = "Bill";
    MaterialType type = GAS;
    std::string description = "Description !@#!@#!@#!46546484328";
    int id = 68468431;

    //Setup root object
    Material object(name);
    object.setId(id);
    object.setDescription(description);
    object.setMaterialType(type);

    //Run the copy routine
    Material copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<Material> castedObject (new Material(*(dynamic_cast<Material *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code
}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    Material component;
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    MaterialType materialType = GAS;

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
    component.setMaterialType(materialType);


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
        BOOST_REQUIRE_EQUAL(5, h5Group.get()->getNumAttrs());

        //Values to check

        //Get attributes by reader factory and check values
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "id"), id);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "name"), name);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "description"), description);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "HDF5LWRTag"), UtilityOperations::toStringHDF5Tag(tag));
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "materialType"), UtilityOperations::toStringMaterialType(materialType));


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
    Material component;
    Material newComponent;
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    MaterialType materialType = GAS;

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
    component.setMaterialType(materialType);

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
BOOST_AUTO_TEST_CASE(checkTyping) {

    // begin-user-code

    //Local Declarations
    MaterialType type;
    bool exceptionHit0 = false;


    //Check the toString implementations of the materialType enum
    BOOST_REQUIRE_EQUAL("Gas", UtilityOperations::toStringMaterialType(GAS));
    BOOST_REQUIRE_EQUAL("Solid", UtilityOperations::toStringMaterialType(SOLID));
    BOOST_REQUIRE_EQUAL("Liquid", UtilityOperations::toStringMaterialType(LIQUID));

    //Check the toType implementations of the HDf5 enum


    //Check the type
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringMaterialType("Gas"), GAS);

    //Check the type
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringMaterialType("Liquid"), LIQUID);

    //Check the type
    BOOST_REQUIRE_EQUAL(UtilityOperations::fromStringMaterialType("Solid"), SOLID);

    //Try to return a type that does not exist
    try {
        UtilityOperations::fromStringMaterialType("asdasdasdasdasd");
    } catch (...) {
        exceptionHit0 = true;
    }

    BOOST_REQUIRE_EQUAL(exceptionHit0, true);

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
