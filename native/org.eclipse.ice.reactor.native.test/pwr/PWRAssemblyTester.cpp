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
#define BOOST_TEST_MODULE PWRAssemblyTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <pwr/PWRAssembly.h>
#include <HDF5LWRTagType.h>
#include <string>
#include <vector>
#include<LWRComposite.h>
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

BOOST_AUTO_TEST_SUITE(PWRAssemblyTester_testSuite)

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
    std::string defaultName = "PWRAssembly";
    std::string defaultDesc = "PWRAssembly's Description";
    int defaultId = 1;
    int defaultSize = 1;
    double defaultRodPitch = 1;
    HDF5LWRTagType type = PWRASSEMBLY;

    //New values
    std::string newName = "Super Assembly!";
    int newSize = 10;
    std::string nullString;

    //Test non-nullary constructor - size
    PWRAssembly assembly(defaultSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
    BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly.getRodPitch());
    BOOST_REQUIRE_EQUAL(type, assembly.getHDF5LWRTag());

    //Test non-nullary constructor - size
    PWRAssembly assembly2(newSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly2.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly2.getId());
    BOOST_REQUIRE_EQUAL(newSize, assembly2.getSize());
    BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly2.getRodPitch());
    BOOST_REQUIRE_EQUAL(type, assembly2.getHDF5LWRTag());

    //Test non-nullary constructor - illegal size value
    PWRAssembly assembly3(0);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly3.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly3.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly3.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly3.getSize()); // Defaults
    BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly3.getRodPitch());
    BOOST_REQUIRE_EQUAL(type, assembly3.getHDF5LWRTag());

    //Test non-nullary constructor - illegal size value - negative
    PWRAssembly assembly4(-1);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly4.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly4.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly4.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly4.getSize()); // Defaults
    BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly4.getRodPitch());
    BOOST_REQUIRE_EQUAL(type, assembly4.getHDF5LWRTag());

    //Test non-nullary constructor - name and size
    PWRAssembly assembly5(defaultName, defaultSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly5.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly5.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly5.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly5.getSize());
    BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly5.getRodPitch());
    BOOST_REQUIRE_EQUAL(type, assembly5.getHDF5LWRTag());

    //Test non-nullary constructor - name and size
    PWRAssembly assembly6(newName, defaultSize);
    //Check values
    BOOST_REQUIRE_EQUAL(newName, assembly6.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly6.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly6.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly6.getSize());
    BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly6.getRodPitch());
    BOOST_REQUIRE_EQUAL(type, assembly6.getHDF5LWRTag());

    //Test non-nullary constructor - bad name but good size
    PWRAssembly assembly7(nullString, defaultSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly7.getName()); //Defaults
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly7.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly7.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly7.getSize());
    BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly7.getRodPitch());
    BOOST_REQUIRE_EQUAL(type, assembly7.getHDF5LWRTag());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkLWRRodOperations) {

    // begin-user-code
    // Local Declarations
    int assemblySize = 17;

    std::string testComponentName = "Bob";
    std::string testComponentName2 = "Bill!";
    int rowLoc1 = 5, colLoc1 = 5;
    int rowLoc2 = 6, colLoc2 = 6;
    int testComponentId = 1000001;
    double defaultRodPitch;
    std::string nullString;
    std::shared_ptr<LWRRod> nullRod;

    // Check the default values of the Component under test
    PWRAssembly assembly(assemblySize);

    //Check rodPitch setting
    //Set the defaultRodPitch
    defaultRodPitch = assembly.getRodPitch();

    //Set the rodPitch to 0 - fails
    assembly.setRodPitch(0.0);
    BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly.getRodPitch());

    //Set the rod Pitch to 1
    assembly.setRodPitch(1);
    BOOST_REQUIRE_EQUAL(1.0, assembly.getRodPitch());
    defaultRodPitch = assembly.getRodPitch(); //Reset default value to reflect next test.

    //Set the rod pitch to negative - does not work
    assembly.setRodPitch(-1.0);
    BOOST_REQUIRE_EQUAL(defaultRodPitch, assembly.getRodPitch());

    // No rods should be added by default. Therefore every
    // location is bad
    for (int i = 0; i < assemblySize; i++) {
        for (int j = 0; j < assemblySize; j++) {
            BOOST_REQUIRE_EQUAL(assembly.getLWRRodByLocation(i, j).get() == NULL, true);
        }
    }

    //Check the names, should be empty!
    BOOST_REQUIRE_EQUAL(0, assembly.getLWRRodNames().size());

    // Try to get by name - valid string, empty string, and null
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByName("validNameThatDoesNotExistInThere152423").get() == NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByName("").get() == NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByName(nullString).get() == NULL, true);

    // Set the name
    std::shared_ptr<LWRRod> testComponent ( new LWRRod(testComponentName));

    // Add to the assembly
    assembly.addLWRRod(testComponent);

    // See that no location is set
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByLocation(rowLoc1, colLoc1).get()==NULL, true);
    // Check locations to be within bounds
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByLocation(-1, assemblySize - 1).get() == NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByLocation(1, assemblySize - 1).get()==NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByLocation(assemblySize + 25, assemblySize - 1).get() == NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByLocation(assemblySize - 1, assemblySize + 25).get() == NULL, true);

    // Set the valid location:
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(testComponentName, rowLoc1, colLoc1), true);

    // Try to break location setter
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(nullString, rowLoc1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(testComponentName, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(testComponentName, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(nullString, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(nullString, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(nullString, -1, -1), false);
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(testComponentName, rowLoc1, assemblySize + 25), false);
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(testComponentName, assemblySize + 25, colLoc1), false);

    // The above erroneous settings does not change the original location of
    // the first, valid set
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*assembly.getLWRRodByName(testComponentName).get()), true);

    // Check invalid overwrite of location:
    std::shared_ptr<LWRRod>testComponent2 (new LWRRod(testComponentName2));

    // Add assembly, overwrite the previous testComponent's location
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(testComponent2.get()->getName(), rowLoc1, colLoc1), false);

    // Check that it is the first, but not second
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*assembly.getLWRRodByName(testComponentName).get()), true);

    // Add it in there
    BOOST_REQUIRE_EQUAL(assembly.addLWRRod(testComponent2), true);

    // Show that you can have at least 2 components in there
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(testComponent2.get()->getName(), rowLoc2, colLoc2), true);

    // Check values - see the components are different and they reside in
    // the table correctly
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*assembly
                        .getLWRRodByName(testComponentName).get()), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*assembly
                        .getLWRRodByName(testComponentName2).get()), true);

    // Check the locations
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*assembly.getLWRRodByLocation(rowLoc1, colLoc1).get()), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*assembly.getLWRRodByLocation(rowLoc2, colLoc2).get()), true);

    //Check the names, should contain 2!
    BOOST_REQUIRE_EQUAL(2, assembly.getLWRRodNames().size());
    BOOST_REQUIRE_EQUAL(testComponentName, assembly.getLWRRodNames().at(1));
    BOOST_REQUIRE_EQUAL(testComponentName2, assembly.getLWRRodNames().at(0));

    // Check operation for null
    assembly.addLWRRod(nullRod);
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByName(nullString).get() == NULL, true); // Make sure null does not work


    // Finally, demonstrate what happens when a component of the same name
    // is added, it should not overwrite the previous item in the table!
    std::shared_ptr<LWRRod> testComponent3 (new LWRRod(testComponent.get()->getName())); // Same name as the other component
    testComponent3.get()->setId(testComponentId); // Id should differ from
    // testComponent!
    BOOST_REQUIRE_EQUAL(testComponent.get()->getId() == testComponentId, false);

    // Overwrite in table
    BOOST_REQUIRE_EQUAL(assembly.addLWRRod(testComponent3), false);

    // Check that the object has not been overwritten
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*assembly.getLWRRodByName(testComponentName).get()), true);
    BOOST_REQUIRE_EQUAL(testComponent3.get()->operator==(*assembly.getLWRRodByName(testComponentName).get()), false);

    // Test to remove components from the assembly
    BOOST_REQUIRE_EQUAL(assembly.removeLWRRod(nullString), false);
    BOOST_REQUIRE_EQUAL(assembly.removeLWRRod(""), false);
    BOOST_REQUIRE_EQUAL(assembly.removeLWRRod("!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"), false);

    // Remove the first component
    BOOST_REQUIRE_EQUAL(assembly.removeLWRRod(testComponent.get()->getName()), true);

    // Check that it does not exist in the location or getting the name
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByLocation(rowLoc1, colLoc1).get() == NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getLWRRodByName(testComponent.get()->getName()).get() == NULL, true);
    //Check size
    BOOST_REQUIRE_EQUAL(1, assembly.getNumberOfLWRRods());

    // It can now be overridden!
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(testComponent2.get()->getName(), rowLoc1, colLoc1), true);

    //Show that the component's names can NOT overwrite each others locations
    BOOST_REQUIRE_EQUAL(assembly.addLWRRod(testComponent), true);
    BOOST_REQUIRE_EQUAL(assembly.setLWRRodLocation(testComponent.get()->getName(), rowLoc1, colLoc1), false);

    //Check the size, the respective locations
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), assembly.getLWRRodByLocation(rowLoc1, colLoc1).get()->getName());
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), assembly.getLWRRodByLocation(rowLoc2, colLoc2).get()->getName());
    BOOST_REQUIRE_EQUAL(2, assembly.getNumberOfLWRRods());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCompositeImplementations) {

    // begin-user-code

    //Local Declarations
    int assemblySize = 17;

    std::vector<std::string> compNames;
    std::vector< std::shared_ptr<Component> > components;
    int numberOfDefaultComponents = 0;

    //Defaults for rodComposite
    std::shared_ptr<LWRComposite> rodComposite (new LWRComposite());
    std::string compName = "LWRRods";
    std::string compDescription = "A Composite that contains many LWRRods.";
    int compId = 1;

    //Setup LWRRodComposite for comparison
    rodComposite.get()->setName(compName);
    rodComposite.get()->setId(compId);
    rodComposite.get()->setDescription(compDescription);

    //Add components to arrays
    compNames.push_back(rodComposite.get()->getName());
    components.push_back(rodComposite);
    //Setup the default number of components
    numberOfDefaultComponents = components.size();

    //Check the default Composite size and attributes on PWRAssembly
    PWRAssembly assembly(assemblySize);

    //Has a size of numberOfDefaultComponents
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, assembly.getNumberOfComponents());
    //It is equal to the default rodComposite for many of the composite getters
    std::shared_ptr<LWRComposite> castedComposite1(new LWRComposite(*(dynamic_cast<LWRComposite *> (assembly.getComponent(1).get()))));
    std::shared_ptr<LWRComposite> castedComposite2(new LWRComposite(*(dynamic_cast<LWRComposite *> (assembly.getComponent(rodComposite.get()->getName()).get()))));
    BOOST_REQUIRE_EQUAL(rodComposite.get()->operator==(*castedComposite1.get()), true);
    BOOST_REQUIRE_EQUAL(rodComposite.get()->operator==(*castedComposite2.get()), true);
    BOOST_REQUIRE_EQUAL(compNames ==(assembly.getComponentNames()), true);

    BOOST_REQUIRE_EQUAL(components.size(), assembly.getComponents().size());
    for(int i = 0; i < components.size(); i++) {

        std::shared_ptr<LWRComponent> castedComponent(new LWRComponent(*(dynamic_cast<LWRComponent *> (components.at(i).get()))));
        std::shared_ptr<LWRComponent> castedComponentEqual(new LWRComponent(*(dynamic_cast<LWRComponent *> (assembly.getComponents().at(i).get()))));

        BOOST_REQUIRE_EQUAL(castedComponent.get()->operator==(*castedComponentEqual.get()), true);
    }

    //These operations will show that these will not work for this class

    //Check addComponent
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, assembly.getNumberOfComponents());
    assembly.addComponent(std::shared_ptr<LWRComposite> (new LWRComposite()));
    //No size change!
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, assembly.getNumberOfComponents());

    //Check removeComponent - id
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, assembly.getNumberOfComponents());
    assembly.removeComponent(1);
    //No size change!
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, assembly.getNumberOfComponents());

    //Check remove component - name
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, assembly.getNumberOfComponents());
    assembly.removeComponent(components.at(0).get()->getName()); //Try to remove the first off the list
    //No size change!
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, assembly.getNumberOfComponents());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkRodPitch) {

    // begin-user-code
    PWRAssembly assembly(2);
    double defaultPitch = 1;

    //Check default value on assembly
    BOOST_REQUIRE_EQUAL(defaultPitch, assembly.getRodPitch());

    //Set it to 1
    assembly.setRodPitch(1.0);
    BOOST_REQUIRE_EQUAL(1.0, assembly.getRodPitch());

    //Set it to 0 - fails
    assembly.setRodPitch(0.0);
    BOOST_REQUIRE_EQUAL(1.0, assembly.getRodPitch());

    //Set it to negative - illegal value
    assembly.setRodPitch(-1.0);
    BOOST_REQUIRE_EQUAL(1.0, assembly.getRodPitch());

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
    PWRAssembly object(name, size);
    object.addLWRRod(rod);
    object.setLWRRodLocation(rod.get()->getName(), 0, 0);

    //Setup equalObject equal to object
    PWRAssembly equalObject(name, size);
    equalObject.addLWRRod(rod);
    equalObject.setLWRRodLocation(rod.get()->getName(), 0, 0);

    //Setup transitiveObject equal to object
    PWRAssembly transitiveObject(name, size);
    transitiveObject.addLWRRod(rod);
    transitiveObject.setLWRRodLocation(rod.get()->getName(), 0, 0);

    // Set its data, not equal to object
    PWRAssembly unEqualObject(name, size);
    //Uses the default rod

    // Assert that these two objects are equal
    BOOST_REQUIRE_EQUAL(object==(equalObject), true);

    // Assert that two unequal objects returns false
    BOOST_REQUIRE_EQUAL(object==(unEqualObject), false);

    // Check that equals() is Reflexive
    // x==x) = true
    BOOST_REQUIRE_EQUAL(object==(object), true);

    // Check that equals() is Symmetric
    // x==y) = true iff y==x) = true
    BOOST_REQUIRE_EQUAL(object==(equalObject) && equalObject==(object), true);

    // Check that equals() is Transitive
    // x==y) = true, y==z) = true => x==z) = true
    if (object==(equalObject) && equalObject==(transitiveObject)) {
        BOOST_REQUIRE_EQUAL(object==(transitiveObject), true);
    } else {
        BOOST_FAIL("FAILURE IN EQUALITY CHECKING! EXITING!");
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
    std::string name = "Billy";
    int size = 5;
    std::shared_ptr<LWRRod> rod( new LWRRod("Bob the rod"));

    //Setup root object
    PWRAssembly object(name, size);
    object.addLWRRod(rod);
    object.setLWRRodLocation(rod.get()->getName(), 0, 0);

    //Run the copy routine
    PWRAssembly copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<PWRAssembly> castedObject (new PWRAssembly(*(dynamic_cast<PWRAssembly *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    PWRAssembly component(size);
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
    PWRAssembly component(size);
    PWRAssembly newComponent(-1);
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
BOOST_AUTO_TEST_CASE(checkLWRDataProvider) {

	// begin-user-code

	//Make an assembly and setup some locations
	std::string name = "Billy";
	int size = 5;
	std::shared_ptr<LWRRod> rod (new LWRRod("Bob the rod"));

	//Setup root object
	PWRAssembly assembly(name, size);
	assembly.addLWRRod(rod);

	//Locations
	int row1 = 0, col1 = 0, row2 = 1, col2 = 1, row3= 2, col3 = 3;

	//Check getters with nothing on the object
	BOOST_REQUIRE_EQUAL(assembly.getLWRRodDataProviderAtLocation(row1, col1).get() == NULL, true);
	BOOST_REQUIRE_EQUAL(assembly.getLWRRodDataProviderAtLocation(row2, col2).get() == NULL, true);
	BOOST_REQUIRE_EQUAL(assembly.getLWRRodDataProviderAtLocation(row3, col3).get() == NULL, true);

	//Add locations
	assembly.setLWRRodLocation(rod.get()->getName(), row1, col1);
	assembly.setLWRRodLocation(rod.get()->getName(), row2, col2);
	assembly.setLWRRodLocation(rod.get()->getName(), row3, col3);

	//Check not null
	BOOST_REQUIRE_EQUAL(assembly.getLWRRodDataProviderAtLocation(row1, col1).get() != NULL, true);
	BOOST_REQUIRE_EQUAL(assembly.getLWRRodDataProviderAtLocation(row2, col2).get() != NULL, true);
	BOOST_REQUIRE_EQUAL(assembly.getLWRRodDataProviderAtLocation(row3, col3).get() != NULL, true);

	//Setup some data
	std::shared_ptr<LWRData> data1 (new LWRData("Feature1111"));
	std::shared_ptr<LWRData> data2 (new LWRData("Feature1111"));
	std::shared_ptr<LWRData> data3 (new LWRData("Feature1113"));
	std::shared_ptr<LWRData> data4 (new LWRData("Feature1114"));

	//Setup some times
	double time1 = 0.0;
	double time2 = 0.1;

	//Setup duplicate LWRDataProvider
	LWRDataProvider provider;
	provider.addData(data1, time1);
	provider.addData(data2, time1);
	provider.addData(data3, time1);
	provider.addData(data4, time2);

	//Add data
	assembly.getLWRRodDataProviderAtLocation(row1, col1).get()->addData(data1, time1);
	assembly.getLWRRodDataProviderAtLocation(row1, col1).get()->addData(data2, time1);
	assembly.getLWRRodDataProviderAtLocation(row1, col1).get()->addData(data3, time1);
	assembly.getLWRRodDataProviderAtLocation(row1, col1).get()->addData(data4, time2);

	//Verify data
	BOOST_REQUIRE_EQUAL(provider.operator ==(*assembly.getLWRRodDataProviderAtLocation(row1, col1).get()), true);

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
