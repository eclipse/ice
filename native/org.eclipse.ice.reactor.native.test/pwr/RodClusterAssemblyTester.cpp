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
#define BOOST_TEST_MODULE RodClusterAssemblyTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <pwr/RodClusterAssembly.h>
#include <HDF5LWRTagType.h>
#include <string>
#include <vector>
#include <LWRComposite.h>
#include <stdio.h>
#include <iostream>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <HdfFileFactory.h>
#include <UtilityOperations.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(RodClusterAssemblyTester_testSuite)

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
    std::string defaultName = "RodClusterAssembly";
    std::string defaultDesc = "RodClusterAssembly's Description";
    int defaultId = 1;
    int defaultSize = 1;
    HDF5LWRTagType type = ROD_CLUSTER_ASSEMBLY;
    std::string nullString;

    //New names
    std::string newName = "Super RodClusterAssembly!";
    int newSize = 10;

    //Check the default constructor with a default size.  Check default values
    //Test non-nullary constructor - size
    RodClusterAssembly assembly(defaultSize);

    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
    BOOST_REQUIRE_EQUAL(type, assembly.getHDF5LWRTag());

    //Check with new size
    //Test non-nullary constructor - size
    RodClusterAssembly assembly2(newSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly2.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly2.getId());
    BOOST_REQUIRE_EQUAL(newSize, assembly2.getSize());
    BOOST_REQUIRE_EQUAL(type, assembly2.getHDF5LWRTag());

    //Check with bad size - negative
    //Test non-nullary constructor - size
    RodClusterAssembly assembly3(-1);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly3.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly3.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly3.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly3.getSize()); //Defaults
    BOOST_REQUIRE_EQUAL(type, assembly3.getHDF5LWRTag());

    //Check with name and size
    //Test non-nullary constructor - name, size
    RodClusterAssembly assembly4(defaultName, defaultSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly4.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly4.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly4.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly4.getSize());
    BOOST_REQUIRE_EQUAL(type, assembly4.getHDF5LWRTag());

    //Check with bad name
    //Test non-nullary constructor - name, size
    RodClusterAssembly assembly5(nullString, defaultSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly5.getName()); //Defaults
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly5.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly5.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly5.getSize());
    BOOST_REQUIRE_EQUAL(type, assembly5.getHDF5LWRTag());

    //Check with new name and size
    //Test non-nullary constructor - name, size
    RodClusterAssembly assembly6(newName, newSize);
    //Check values
    BOOST_REQUIRE_EQUAL(newName, assembly6.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly6.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly6.getId());
    BOOST_REQUIRE_EQUAL(newSize, assembly6.getSize());
    BOOST_REQUIRE_EQUAL(type, assembly6.getHDF5LWRTag());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    std::string name = "Billy";
    int size = 5;
    std::shared_ptr<LWRRod> rod (new LWRRod("Bob the rod"));

    //Setup root object
    RodClusterAssembly object(name, size);
    object.addLWRRod(rod);
    object.setLWRRodLocation(rod.get()->getName(), 0, 0);

    //Setup equalObject equal to object
    RodClusterAssembly equalObject(name, size);
    equalObject.addLWRRod(rod);
    equalObject.setLWRRodLocation(rod.get()->getName(), 0, 0);

    //Setup transitiveObject equal to object
    RodClusterAssembly transitiveObject(name, size);
    transitiveObject.addLWRRod(rod);
    transitiveObject.setLWRRodLocation(rod.get()->getName(), 0, 0);

    // Set its data, not equal to object
    RodClusterAssembly unEqualObject(name, size);
    //Uses the default rod

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
        BOOST_FAIL("FAILURE IN EQUALITY CHECKING!  EXITING!");
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
    std::string name = "Billy";
    int size = 5;
    std::shared_ptr<LWRRod> rod (new LWRRod("Bob the rod"));

    //Setup root object
    RodClusterAssembly object(name, size);
    object.addLWRRod(rod);
    object.setLWRRodLocation(rod.get()->getName(), 0, 0);

    //Run the copy routine
    RodClusterAssembly copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<RodClusterAssembly> castedObject (new RodClusterAssembly(*(dynamic_cast<RodClusterAssembly *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code
}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    RodClusterAssembly component(size);
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    double rodPitch =30.0;
    std::shared_ptr<LWRRod> rod1(new LWRRod("Rod1"));
    std::shared_ptr<LWRRod> rod2( new LWRRod("Rod2"));
    std::shared_ptr<LWRRod> rod3( new LWRRod("Rod3"));
    std::shared_ptr<GridLocation> loc1(new GridLocation(1, 0));
    std::shared_ptr<GridLocation> loc2( new GridLocation(2, 0));
    std::shared_ptr<GridLocation> loc3( new GridLocation(4, 3));

    //Setup duplicate manager for comparison testing
    LWRGridManager manager(size);
    manager.setName("LWRRod Grid");
    manager.addComponent(rod1, loc1);
    manager.addComponent(rod2, loc2);
    manager.addComponent(rod3, loc3);

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);
    component.setRodPitch(rodPitch);
    component.addLWRRod(rod1);
    component.addLWRRod(rod2);
    component.addLWRRod(rod3);
    component.setLWRRodLocation(rod1.get()->getName(), loc1.get()->getRow(), loc1.get()->getColumn());
    component.setLWRRodLocation(rod2.get()->getName(), loc2.get()->getRow(), loc2.get()->getColumn());
    component.setLWRRodLocation(rod3.get()->getName(), loc3.get()->getRow(), loc3.get()->getColumn());

    //exception hit
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL(component.getWriteableChildren().size(), 2);

    //Get the first component and its sub components for comparisons
    std::shared_ptr<LWRComposite> writeableComposite = std::dynamic_pointer_cast<LWRComposite > (component.getWriteableChildren()[0]);

    //Cast Writeables
    std::shared_ptr<LWRRod> writeableComponent1 = std::dynamic_pointer_cast<LWRRod> (writeableComposite.get()->getWriteableChildren()[0]);
    std::shared_ptr<LWRRod> writeableComponent2 = std::dynamic_pointer_cast<LWRRod> (writeableComposite.get()->getWriteableChildren()[1]);
    std::shared_ptr<LWRRod> writeableComponent3 = std::dynamic_pointer_cast<LWRRod> (writeableComposite.get()->getWriteableChildren()[2]);
    std::shared_ptr<LWRGridManager> writeableComponent4 = std::dynamic_pointer_cast<LWRGridManager> (component.getWriteableChildren()[1]);

    BOOST_REQUIRE_EQUAL(manager.getName(), writeableComponent4.get()->getName());
    BOOST_REQUIRE_EQUAL(manager.operator ==(*writeableComponent4.get()), true);
    BOOST_REQUIRE_EQUAL(rod3.get()->getName(), writeableComponent3.get()->getName());
    BOOST_REQUIRE_EQUAL(rod2.get()->getName(), writeableComponent2.get()->getName());
    BOOST_REQUIRE_EQUAL(rod1.get()->getName(), writeableComponent1.get()->getName());

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
        BOOST_REQUIRE_EQUAL(6, h5Group.get()->getNumAttrs());

        //Values to check

        //Get attributes by reader factory and check values
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "id"), id);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "name"), name);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "description"), description);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readStringAttribute(h5Group, "HDF5LWRTag"), UtilityOperations::toStringHDF5Tag(tag));
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readIntegerAttribute(h5Group, "size"), size);
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(h5Group, "rodPitch"), rodPitch);



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
    int size = 5;
    RodClusterAssembly component(size);
    RodClusterAssembly newComponent(-1);
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    double rodPitch =30.0;
    std::shared_ptr<LWRRod> rod1(new LWRRod("Rod1"));
    std::shared_ptr<LWRRod> rod2( new LWRRod("Rod2"));
    std::shared_ptr<LWRRod> rod3( new LWRRod("Rod3"));
    std::shared_ptr<GridLocation> loc1(new GridLocation(1, 0));
    std::shared_ptr<GridLocation> loc2( new GridLocation(2, 0));
    std::shared_ptr<GridLocation> loc3( new GridLocation(4, 3));

    //Setup duplicate manager for comparison testing
    std::shared_ptr <LWRGridManager> manager( new LWRGridManager(size));
    manager.get()->setName("LWRRod Grid");
    manager.get()->addComponent(rod1, loc1);
    manager.get()->addComponent(rod2, loc2);
    manager.get()->addComponent(rod3, loc3);

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Setup Component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);
    component.setRodPitch(rodPitch);
    component.addLWRRod(rod1);
    component.addLWRRod(rod2);
    component.addLWRRod(rod3);
    component.setLWRRodLocation(rod1.get()->getName(), loc1.get()->getRow(), loc1.get()->getColumn());
    component.setLWRRodLocation(rod2.get()->getName(), loc2.get()->getRow(), loc2.get()->getColumn());
    component.setLWRRodLocation(rod3.get()->getName(), loc3.get()->getRow(), loc3.get()->getColumn());

    //Setup Composite
    std::shared_ptr <LWRComposite> rodComposite (new LWRComposite());
    rodComposite.get()->setName("LWRRods");
    rodComposite.get()->setDescription("A Composite that contains many LWRRods.");
    rodComposite.get()->addComponent(rod1);
    rodComposite.get()->addComponent(rod2);
    rodComposite.get()->addComponent(rod3);


    //Exceptions
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;


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
        BOOST_REQUIRE_EQUAL(newComponent.readChild(rodComposite), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(manager), true);

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
