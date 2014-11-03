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
#define BOOST_TEST_MODULE RingTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <memory>
#include <Ring.h>
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

BOOST_AUTO_TEST_SUITE(RingTester_testSuite)

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

    //Default values.  Change here as needed for tests
    std::string defaultName = "Ring";
    std::string defaultDesc = "Ring's Description";
    int defaultId = 1;
    double defaultInnerRadius = 0;
    double defaultOuterRadius = 1;
    double defaultHeight = 1.0;
    HDF5LWRTagType type = RING;
    std::string nullString;

    //New values
    std::string newName = "Super Ring!";
    std::shared_ptr<Material> newMaterial (new Material());
    newMaterial.get()->setName("SuperMaterial!!@#!@#*!@#!");
    double newHeight = 1.51231541513;
    double newOuterRadius = 86.3985656;
    double newInnerRadius = 5.83495787819;

    //Check nullary constructor
    Ring ring;
    //Check default values
    BOOST_REQUIRE_EQUAL(defaultName, ring.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, ring.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, ring.getId());
    BOOST_REQUIRE_EQUAL(defaultInnerRadius, ring.getInnerRadius());
    BOOST_REQUIRE_EQUAL(defaultOuterRadius, ring.getOuterRadius());
    BOOST_REQUIRE_EQUAL(defaultHeight, ring.getHeight());
    BOOST_REQUIRE_EQUAL(ring.getMaterial().get() != NULL, true);
    BOOST_REQUIRE_EQUAL(type, ring.getHDF5LWRTag());

    //Check non-nullary constructor - name
    Ring ring2(newName);
    //Check default values
    BOOST_REQUIRE_EQUAL(newName, ring2.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, ring2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, ring2.getId());
    BOOST_REQUIRE_EQUAL(defaultInnerRadius, ring2.getInnerRadius());
    BOOST_REQUIRE_EQUAL(defaultOuterRadius, ring2.getOuterRadius());
    BOOST_REQUIRE_EQUAL(defaultHeight, ring2.getHeight());
    BOOST_REQUIRE_EQUAL(ring2.getMaterial().get() != NULL, true);
    BOOST_REQUIRE_EQUAL(type, ring2.getHDF5LWRTag());

    //Check non-nullary constructor - name with null
    Ring ring3(nullString);
    //Check default values
    BOOST_REQUIRE_EQUAL(defaultName, ring3.getName()); //Defaults name
    BOOST_REQUIRE_EQUAL(defaultDesc, ring3.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, ring3.getId());
    BOOST_REQUIRE_EQUAL(defaultInnerRadius, ring3.getInnerRadius());
    BOOST_REQUIRE_EQUAL(defaultOuterRadius, ring3.getOuterRadius());
    BOOST_REQUIRE_EQUAL(defaultHeight, ring3.getHeight());
    BOOST_REQUIRE_EQUAL(ring3.getMaterial().get() != NULL, true);
    BOOST_REQUIRE_EQUAL(type, ring3.getHDF5LWRTag());

    //Check non-nullary constructor - name, material, height, outerRadius
    Ring ring4(newName, newMaterial, newHeight, newOuterRadius);
    //Check default values
    BOOST_REQUIRE_EQUAL(newName, ring4.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, ring4.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, ring4.getId());
    BOOST_REQUIRE_EQUAL(defaultInnerRadius, ring4.getInnerRadius());
    BOOST_REQUIRE_EQUAL(newOuterRadius, ring4.getOuterRadius());
    BOOST_REQUIRE_EQUAL(newHeight, ring4.getHeight());
    BOOST_REQUIRE_EQUAL(ring4.getMaterial().get() != NULL, true);
    BOOST_REQUIRE_EQUAL(newMaterial.get()->getName(), ring4.getMaterial().get()->getName());
    BOOST_REQUIRE_EQUAL(type, ring4.getHDF5LWRTag());

    //Check non-nullary constructor - name, material, height, innerRadius, outerRadius
    Ring ring5(newName, newMaterial, newHeight, newInnerRadius, newOuterRadius);
    //Check default values
    BOOST_REQUIRE_EQUAL(newName, ring5.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, ring5.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, ring5.getId());
    BOOST_REQUIRE_EQUAL(newInnerRadius, ring5.getInnerRadius());
    BOOST_REQUIRE_EQUAL(newOuterRadius, ring5.getOuterRadius());
    BOOST_REQUIRE_EQUAL(newHeight, ring5.getHeight());
    BOOST_REQUIRE_EQUAL(ring5.getMaterial().get() != NULL, true);
    BOOST_REQUIRE_EQUAL(newMaterial.get()->getName(), ring5.getMaterial().get()->getName());
    BOOST_REQUIRE_EQUAL(type, ring5.getHDF5LWRTag());

    //Check nullaries - check name
    Ring ring6(nullString, newMaterial, newHeight, newOuterRadius);
    BOOST_REQUIRE_EQUAL(defaultName, ring6.getName()); //Defaults name

    //Check nullaries - check height
    Ring ring7(nullString, newMaterial, 0.0, newOuterRadius);
    BOOST_REQUIRE_EQUAL(defaultHeight, ring7.getHeight()); //Defaults height

    //Check bad values - check outer radius
    Ring ring8(newName, newMaterial, 1.0, 0.0);
    BOOST_REQUIRE_EQUAL(defaultOuterRadius, ring8.getOuterRadius()); //Defaults radius

    //Check bad values - check inner radius
    Ring ring9(newName, newMaterial, newHeight, -1.0, newOuterRadius);
    BOOST_REQUIRE_EQUAL(defaultInnerRadius, ring9.getInnerRadius()); //Defaults radius
    BOOST_REQUIRE_EQUAL(type, ring9.getHDF5LWRTag());

    return;
    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHeight) {

    // begin-user-code

    //Local declarations
    Ring ring;

    //Check setter - legal value
    ring.setHeight(5.0);
    BOOST_REQUIRE_EQUAL(5.0, ring.getHeight());

    //Check for illegal value - 0
    ring.setHeight(0.0);
    BOOST_REQUIRE_EQUAL(5.0, ring.getHeight());

    //Check for illegal value - negative
    ring.setHeight(-1.0);
    BOOST_REQUIRE_EQUAL(5.0, ring.getHeight());
    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkInnerRadius) {

    // begin-user-code

    //Local declarations
    Ring ring;

    //Check setter - legal value
    ring.setOuterRadius(20.0);
    ring.setInnerRadius(3.3453453);
    BOOST_REQUIRE_EQUAL(3.3453453, ring.getInnerRadius());

    //Check setter - legal value of 0
    ring.setInnerRadius(0.0);
    BOOST_REQUIRE_EQUAL(0.0, ring.getInnerRadius());

    //Check illegal value - negative
    ring.setInnerRadius(-1.0);
    BOOST_REQUIRE_EQUAL(0.0, ring.getInnerRadius()); //stays the same as before

    //Inner radius can not be greater than  or equal to outer radius
    ring.setInnerRadius(20.0);
    BOOST_REQUIRE_EQUAL(0.0, ring.getInnerRadius());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkOuterRadius) {

    // begin-user-code

    //Local declarations
    Ring ring;

    //Check setter - legal value
    ring.setOuterRadius(20.0);
    ring.setInnerRadius(3.3453453);
    BOOST_REQUIRE_EQUAL(20.0, ring.getOuterRadius());

    //Check illegal value of 0
    ring.setOuterRadius(0.0);
    BOOST_REQUIRE_EQUAL(20.0, ring.getOuterRadius());

    //Check illegal value - negative
    ring.setOuterRadius(-1.0);
    BOOST_REQUIRE_EQUAL(20.0, ring.getOuterRadius()); //stays the same as before

    //Outer radius can not be less than  or equal to inner radius
    ring.setInnerRadius(1.0);
    BOOST_REQUIRE_EQUAL(20.0, ring.getOuterRadius());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkMaterial) {

    // begin-user-code

    //Local declarations
    Ring ring;
    std::shared_ptr<Material> material (new Material());
    std::string name = "Bobble123123198796**3213.12313123";

    //Set name on material
    material.get()->setName(name);

    //Set material
    ring.setMaterial(material);

    //Check the name
    BOOST_REQUIRE_EQUAL(name, ring.getMaterial().get()->getName());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    std::string name = "Billy!";
    int id = 654654;
    std::string description = "ASDFG Billy464646";
    std::shared_ptr<Material> material (new Material());
    std::shared_ptr<Material> unEqualMaterial (new Material());
    unEqualMaterial.get()->setMaterialType(LIQUID);
    material.get()->setMaterialType(GAS);
    double innerRadius = 5.0, outerRadius = 8, height = 20.0;

    //Setup root object
    Ring object(name, material, height, innerRadius, outerRadius);
    object.setId(id);
    object.setDescription(description);

    //Setup equalObject equal to object
    Ring equalObject(name, material, height, innerRadius, outerRadius);
    equalObject.setId(id);
    equalObject.setDescription(description);

    //Setup transitiveObject equal to object
    Ring transitiveObject(name, material, height, innerRadius, outerRadius);
    transitiveObject.setId(id);
    transitiveObject.setDescription(description);

    // Set its data, not equal to object
    //Different Material
    Ring unEqualObject(name, unEqualMaterial, height, innerRadius, outerRadius);
    unEqualObject.setId(id);
    unEqualObject.setDescription(description);

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
        BOOST_FAIL("FAILURE IN EQUALITY CHECKING.  EXITING!");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(object==(equalObject) && object==(equalObject) && object==(equalObject), true);
    BOOST_REQUIRE_EQUAL(!(object==(unEqualObject)) && !(object==(unEqualObject)) && !(object==(unEqualObject)), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local Declarations
    std::string name = "Billy!";
    int id = 654654;
    std::string description = "ASDFG Billy464646";
    std::shared_ptr<Material> material (new Material());
    material.get()->setMaterialType(GAS);
    double innerRadius = 5.0, outerRadius = 8, height = 20.0;

    //Setup root object
    Ring object(name, material, height, innerRadius, outerRadius);
    object.setId(id);
    object.setDescription(description);

    //Run the copy routine
    Ring copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<Ring> castedObject (new Ring(*(dynamic_cast<Ring *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    Ring component;
    std::shared_ptr <Material> material(new Material("Billy the material!", GAS));
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    double height = 5, inner = 3, outer = 6;


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
    component.setMaterial(material);
    component.setHeight(height);
    component.setOuterRadius(outer);
    component.setInnerRadius(inner);


    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL((component.getWriteableChildren().size() == 1), true);
    //Check the material
    std::shared_ptr<Material> writeableMaterial (new Material(*(dynamic_cast<Material *> (component.getWriteableChildren()[0].get()))));
    BOOST_REQUIRE_EQUAL(material.get()->getName(), writeableMaterial.get()->getName());

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
        BOOST_REQUIRE_EQUAL(7, h5Group.get()->getNumAttrs());

        //Values to check

        //Get attributes by reader factory and check values
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "id"), id);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "name"), name);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "description"), description);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "HDF5LWRTag"), UtilityOperations::toStringHDF5Tag(tag));
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(h5Group, "innerRadius"), inner);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(h5Group, "outerRadius"), outer);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(h5Group, "height"), height);



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
    std::cout<< "Starting reading!" << std::endl;
    //Local Declarations
    Ring component;
    Ring newComponent;
    std::shared_ptr <IHdfReadable> material(new Material("Billy the material!", GAS));
    std::shared_ptr <Material> material2(new Material("Billy the material!", GAS));
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    double height = 5, inner = 3, outer = 6;
    std::shared_ptr<IHdfReadable> nullComponent (new LWRComponent());

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
    component.setMaterial(material2);
    component.setHeight(height);
    component.setOuterRadius(outer);
    component.setInnerRadius(inner);

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
        BOOST_REQUIRE_EQUAL(newComponent.readChild(material), true);
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
