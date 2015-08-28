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
#define BOOST_TEST_MODULE LWRRodTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <LWRRod.h>
#include <Material.h>
#include <MaterialBlock.h>
#include <MaterialType.h>
#include <Ring.h>
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

BOOST_AUTO_TEST_SUITE(LWRRodTester_testSuite)

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

    // Default Values
    double defaultGasPressure = 2200.0; //psig
    std::string defaultName = "LWRRod";
    std::string defaultDesc = "LWRRod's Description";
    int defaultId = 1;
    std::shared_ptr<Material> defaultFillGas(new Material("Void", GAS));
    std::shared_ptr<Material> defaultCladMaterial(new Material("Zirc", SOLID));
    std::shared_ptr<Ring> defaultClad(new Ring("Clad", defaultCladMaterial, 0.0, 0.0));
    HDF5LWRTagType type = LWRROD;
    std::string nullString;
    std::shared_ptr<Material> nullGas;
    int defaultMaterialBlockSize = 0;

    // New Values
    std::shared_ptr<Material> newFillGas(new Material("Helium", GAS));
    double newGasPressure = 2200.34344512434;
    std::string newName = "ControlRod";
    std::shared_ptr<MaterialBlock> newMaterialBlock (new MaterialBlock());
    std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > blocks (new std::vector < std::shared_ptr< MaterialBlock> >());
    blocks.get()->push_back(newMaterialBlock);

    // test Nullary
    LWRRod testRod;
    //Check default values
    BOOST_REQUIRE_EQUAL(defaultName, testRod.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, testRod.getDescription());
    BOOST_REQUIRE_EQUAL(defaultFillGas.get()->getName(), testRod.getFillGas().get()->getName());
    BOOST_REQUIRE_EQUAL(defaultFillGas.get()->getMaterialType(), testRod.getFillGas().get()->getMaterialType());
    BOOST_REQUIRE_EQUAL(defaultGasPressure, testRod.getPressure());
    BOOST_REQUIRE_EQUAL(defaultId, testRod.getId());
    BOOST_REQUIRE_EQUAL(defaultMaterialBlockSize == testRod.getMaterialBlocks().get()->size(), true);
    BOOST_REQUIRE_EQUAL(defaultClad.get()->getName(), testRod.getClad().get()->getName());
    BOOST_REQUIRE_EQUAL(defaultClad.get()->getMaterial().get()->getMaterialType(), testRod.getClad().get()->getMaterial().get()->getMaterialType());
    BOOST_REQUIRE_EQUAL(type, testRod.getHDF5LWRTag());

    //test non-nullary constructor -name
    LWRRod testRod2(newName);
    // Except set name everything else should be Default value
    BOOST_REQUIRE_EQUAL(newName, testRod2.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, testRod2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultFillGas.get()->operator ==(*testRod2.getFillGas().get()), true);
    BOOST_REQUIRE_EQUAL(defaultGasPressure, testRod2.getPressure());
    BOOST_REQUIRE_EQUAL(defaultId, testRod2.getId());
    BOOST_REQUIRE_EQUAL(defaultMaterialBlockSize == testRod.getMaterialBlocks().get()->size(), true);
    BOOST_REQUIRE_EQUAL(defaultClad.get()->getName(), testRod2.getClad().get()->getName());
    BOOST_REQUIRE_EQUAL(defaultClad.get()->getMaterial().get()->getMaterialType(), testRod2.getClad().get()->getMaterial().get()->getMaterialType());
    BOOST_REQUIRE_EQUAL(type, testRod2.getHDF5LWRTag());

    //test non-nullary constructor -name fillgas pressure, MaterialBlock
    LWRRod testRod3(newName, newFillGas, newGasPressure, blocks);
    //Check if the new values are set
    BOOST_REQUIRE_EQUAL(newName, testRod3.getName());
    BOOST_REQUIRE_EQUAL(newFillGas.get()->operator ==(*testRod3.getFillGas().get()), true);
    BOOST_REQUIRE_EQUAL(newGasPressure, testRod3.getPressure());
    BOOST_REQUIRE_EQUAL(blocks.get()->size(), testRod3.getMaterialBlocks().get()->size());
    BOOST_REQUIRE_EQUAL(type, testRod3.getHDF5LWRTag());

    //test non-nullary constructor with null name
    LWRRod testRod4(nullString, newFillGas, newGasPressure, blocks);
    BOOST_REQUIRE_EQUAL(defaultName, testRod4.getName());
    BOOST_REQUIRE_EQUAL(type, testRod4.getHDF5LWRTag());

    //test non-nullary constructor with null Fill Gas
    LWRRod testRod5(newName, nullGas, newGasPressure, blocks);
    BOOST_REQUIRE_EQUAL(defaultFillGas.get()->operator==(*testRod5.getFillGas()), true);
    BOOST_REQUIRE_EQUAL(type, testRod5.getHDF5LWRTag());

    //test non-nullary constructor with bad Pressure
    LWRRod testRod6(newName, newFillGas, -1.0, blocks);
    BOOST_REQUIRE_EQUAL(defaultGasPressure, testRod6.getPressure());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkFillGas) {

    // begin-user-code
    LWRRod testRod;
    std::shared_ptr<Material> newFillGas (new Material("Helium", GAS));
    std::shared_ptr<Material> nullGas;

    //check setter Legal value
    testRod.setFillGas(newFillGas);
    BOOST_REQUIRE_EQUAL(newFillGas.get()->operator==(*testRod.getFillGas().get()), true);

    //check null, it should return previous value
    testRod.setFillGas(nullGas);
    BOOST_REQUIRE_EQUAL(newFillGas.get()->operator==(*testRod.getFillGas().get()), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkPressure) {

    // begin-user-code
    LWRRod testRod;
    double newGasPressure = 2200.34344512434;
    //check setter Legal value
    testRod.setPressure(newGasPressure);
    BOOST_REQUIRE_EQUAL(newGasPressure, testRod.getPressure());

    //check setting illegal 0.0 value
    testRod.setPressure(0.0);
    BOOST_REQUIRE_EQUAL(newGasPressure, testRod.getPressure());

    //check setting illegal negative value
    testRod.setPressure(-1.0);
    BOOST_REQUIRE_EQUAL(newGasPressure, testRod.getPressure());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkMaterialBlock) {

    // begin-user-code
    LWRRod testRod;
    std::shared_ptr<MaterialBlock> newMaterialBlock (new MaterialBlock());
    std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > blocks (new std::vector < std::shared_ptr< MaterialBlock> >());
    blocks.get()->push_back(newMaterialBlock);
    newMaterialBlock.get()->setName("PEEPS!");
    std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > emptyBlocks (new std::vector < std::shared_ptr< MaterialBlock> >());

    // check if stack can be set to non-null;
    testRod.setMaterialBlocks(blocks);
    BOOST_REQUIRE_EQUAL(blocks.get()->size(), testRod.getMaterialBlocks().get()->size());
    BOOST_REQUIRE_EQUAL(testRod.getMaterialBlocks().get()->at(0)->operator==(*blocks.get()->at(0).get()), true);

    //check setting illegal empty value
    BOOST_REQUIRE_EQUAL(blocks.get()->size(), testRod.getMaterialBlocks().get()->size());
    BOOST_REQUIRE_EQUAL(testRod.getMaterialBlocks().get()->at(0)->operator==(*blocks.get()->at(0).get()), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkClad) {

    // begin-user-code

    LWRRod testRod;
    std::shared_ptr<Material> cladMaterial( new Material("Steel304", SOLID));
    std::shared_ptr<Ring> newClad (new Ring("Clad", cladMaterial, 0.0, 0.0));
    std::shared_ptr<Ring> nullClad;

    //check if clad can be a set to a new clad
    testRod.setClad(newClad);
    BOOST_REQUIRE_EQUAL(newClad.get()->operator==(*testRod.getClad()), true);

    //check setting illegal null value
    testRod.setClad(nullClad);
    BOOST_REQUIRE_EQUAL(newClad.get()->operator==(*testRod.getClad()), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    double pressure = 2.0;
    double unEqualPressure = 5.0;

    //Setup material
    std::shared_ptr<Material> material (new Material("MAterialSSSS464"));

    //Ring material
    std::shared_ptr<Material> cladMaterial(new Material("Zirc", SOLID));

    //Setup Rings
    std::shared_ptr<Ring> ring1(new Ring("Bob", cladMaterial, 20.0, 5.0, 10.0));
    std::shared_ptr<Ring> ring2(new Ring("Bob2", cladMaterial, 22.0, 52.0, 102.0));
    std::shared_ptr<Ring> clad(new Ring("Bob3", cladMaterial, 0, 52.0, 103.0));



    //Setup MaterialBlock
    std::shared_ptr<MaterialBlock> newMaterialBlock (new MaterialBlock());
    std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > blocks (new std::vector < std::shared_ptr< MaterialBlock> >());
    blocks.get()->push_back(newMaterialBlock);
    newMaterialBlock.get()->setName("PEEPS!");

    newMaterialBlock.get()->addRing(ring1);
    newMaterialBlock.get()->addRing(ring2);

    //Setup root object
    LWRRod object("ROD!", material, pressure, blocks);
    object.setClad(clad);

    //Setup equalObject equal to object
    LWRRod equalObject("ROD!", material, pressure, blocks);
    equalObject.setClad(clad);

    //Setup transitiveObject equal to object
    LWRRod transitiveObject("ROD!", material, pressure, blocks);
    transitiveObject.setClad(clad);

    // Set its data, not equal to object
    LWRRod unEqualObject("ROD!", material, unEqualPressure, blocks);
    unEqualObject.setClad(clad);

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
        BOOST_FAIL("FAILURE IN EQUALITY CHECK!  EXITING!");
    }

    // Check the Consistent nature of equals()
    BOOST_REQUIRE_EQUAL(object==(equalObject) && object==(equalObject)
                        && object==(equalObject), true);
    BOOST_REQUIRE_EQUAL(!(object==(unEqualObject))
                        && !(object==(unEqualObject))
                        && !(object==(unEqualObject)), true);

    return;

    //end-user-code
}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local Declarations
    double pressure = 2.0;

    //Ring material
    std::shared_ptr<Material> cladMaterial(new Material("Zirc", SOLID));

    //Setup Rings
    std::shared_ptr<Ring> ring1(new Ring("Bob", cladMaterial, 20.0, 5.0, 10.0));
    std::shared_ptr<Ring> ring2(new Ring("Bob2", cladMaterial, 22.0, 52.0, 102.0));
    std::shared_ptr<Ring> clad(new Ring("Bob3", cladMaterial, 0, 52.0, 103.0));

    //Setup material
    std::shared_ptr<Material> material (new Material("MAterialSSSS464"));

    //Setup MaterialBlock
    std::shared_ptr<MaterialBlock> newMaterialBlock (new MaterialBlock());
    std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > blocks (new std::vector < std::shared_ptr< MaterialBlock> >());
    blocks.get()->push_back(newMaterialBlock);
    newMaterialBlock.get()->setName("PEEPS!");

    newMaterialBlock.get()->addRing(ring1);
    newMaterialBlock.get()->addRing(ring2);

    //Setup root object
    LWRRod object("ROD!", material, pressure, blocks);
    object.setClad(clad);

    //Run the copy routine
    LWRRod copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<LWRRod> castedObject (new LWRRod(*(dynamic_cast<LWRRod *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    //begin-user-code

    //Local Declarations
    LWRRod component;
    std::string name = "Wacky Waving Inflatable Arm-Flailing Tubeman!";
    std::string description = "I am overstocked with deals!";
    int id = 4;
    double pressure = 2.0;

    //Ring material
    std::shared_ptr<Material> cladMaterial(new Material("Zirc", SOLID));

    //Setup Rings
    std::shared_ptr<Ring> ring1(new Ring("Bob", cladMaterial, 20.0, 5.0, 10.0));
    std::shared_ptr<Ring> ring2(new Ring("Bob2", cladMaterial, 22.0, 52.0, 102.0));
    std::shared_ptr<Ring> clad(new Ring("Bob3", cladMaterial, 0, 52.0, 103.0));

    //Setup material
    std::shared_ptr<Material> material (new Material("MAterialSSSS464"));

    //Setup MaterialBlock
    std::shared_ptr<MaterialBlock> newMaterialBlock (new MaterialBlock());
    std::shared_ptr<MaterialBlock> newMaterialBlock2 (new MaterialBlock());
    std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > blocks (new std::vector < std::shared_ptr< MaterialBlock> >());
    blocks.get()->push_back(newMaterialBlock);
    blocks.get()->push_back(newMaterialBlock2);
    newMaterialBlock.get()->setName("PEEPS!");
    newMaterialBlock2.get()->setName("PEEPS22!");

    newMaterialBlock.get()->addRing(ring1);
    newMaterialBlock.get()->addRing(ring2);
    newMaterialBlock2.get()->addRing(ring1);

    newMaterialBlock.get()->setPosition(1.0);
    newMaterialBlock.get()->setPosition(2.0);

    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;

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
    component.setMaterialBlocks(blocks);
    component.setClad(clad);
    component.setPressure(pressure);
    component.setFillGas(material);

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL((component.getWriteableChildren().size() == 4), true);
    //Check the children
    std::shared_ptr<Ring> writeableClad (new Ring(*(dynamic_cast<Ring *> (component.getWriteableChildren()[0].get()))));
    std::shared_ptr<Material> writeableGas (new Material(*(dynamic_cast<Material *> (component.getWriteableChildren()[1].get()))));
    std::shared_ptr<MaterialBlock> writeableMaterialBlock (new MaterialBlock(*(dynamic_cast<MaterialBlock *> (component.getWriteableChildren()[2].get()))));
    std::shared_ptr<MaterialBlock> writeableMaterialBlock2 (new MaterialBlock(*(dynamic_cast<MaterialBlock *> (component.getWriteableChildren()[3].get()))));
    BOOST_REQUIRE_EQUAL(clad.get()->getName(), writeableClad.get()->getName());
    BOOST_REQUIRE_EQUAL(newMaterialBlock.get()->getName(), writeableMaterialBlock.get()->getName());
    BOOST_REQUIRE_EQUAL(newMaterialBlock2.get()->getName(), writeableMaterialBlock2.get()->getName());
    BOOST_REQUIRE_EQUAL(material.get()->getName(), writeableGas.get()->getName());


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
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(h5Group, "pressure"), pressure);



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
    BOOST_REQUIRE_EQUAL(component.getName(), h5Group.get()->getObjnameByIdx(1));
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

    //begin-user-code

    //Local Declarations
    LWRRod newComponent;
    std::shared_ptr<IHdfReadable> nullComponent (new LWRComponent());
    LWRRod component;
    std::string name = "Wacky Waving Inflatable Arm-Flailing Tubeman!";
    std::string description = "I am overstocked with deals!";
    int id = 4;
    double pressure = 2.0;

    //Ring material
    std::shared_ptr<Material> cladMaterial(new Material("Zirc", SOLID));

    //Setup Rings
    std::shared_ptr<Ring> ring1(new Ring("Bob", cladMaterial, 20.0, 5.0, 10.0));
    std::shared_ptr<Ring> ring2(new Ring("Bob2", cladMaterial, 22.0, 52.0, 102.0));
    std::shared_ptr<Ring> clad(new Ring("Bob3", cladMaterial, 0, 52.0, 103.0));

    //Setup material
    std::shared_ptr<Material> material (new Material("MAterialSSSS464"));

    //Setup MaterialBlock
    std::shared_ptr<MaterialBlock> newMaterialBlock (new MaterialBlock());
    std::shared_ptr<MaterialBlock> newMaterialBlock2 (new MaterialBlock());
    std::shared_ptr< std::vector < std::shared_ptr< MaterialBlock > > > blocks (new std::vector < std::shared_ptr< MaterialBlock> >());
    blocks.get()->push_back(newMaterialBlock);
    blocks.get()->push_back(newMaterialBlock2);
    newMaterialBlock.get()->setName("PEEPS!");

    newMaterialBlock.get()->addRing(ring1);
    newMaterialBlock.get()->addRing(ring2);
    newMaterialBlock2.get()->addRing(ring1);

    newMaterialBlock.get()->setPosition(1.0);
    newMaterialBlock.get()->setPosition(2.0);

    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;

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
    component.setMaterialBlocks(blocks);
    component.setClad(clad);
    component.setPressure(pressure);
    component.setFillGas(material);

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
        BOOST_REQUIRE_EQUAL(newComponent.readChild(clad), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(material), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(newMaterialBlock), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(newMaterialBlock2), true);
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
