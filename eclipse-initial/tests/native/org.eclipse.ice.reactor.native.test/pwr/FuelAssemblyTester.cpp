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
#define BOOST_TEST_MODULE FuelAssemblyTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <pwr/FuelAssembly.h>
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

BOOST_AUTO_TEST_SUITE(FuelAssemblyTester_testSuite)

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

    std::string defaultName = "FuelAssembly";
    std::string defaultDesc = "FuelAssembly's Description";
    int defaultId = 1;
    int defaultSize = 1;
    HDF5LWRTagType type = FUEL_ASSEMBLY;
    std::string nullString;

    //New names
    std::string newName = "Super FuelAssembly!";
    int newSize = 10;

    //Check the default constructor with a default size.  Check default values
    //Test non-nullary constructor - size
    FuelAssembly assembly(defaultSize);

    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly.getSize());
    BOOST_REQUIRE_EQUAL(type, assembly.getHDF5LWRTag());

    //Check with new size
    //Test non-nullary constructor - size
    FuelAssembly assembly2(newSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly2.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly2.getId());
    BOOST_REQUIRE_EQUAL(newSize, assembly2.getSize());
    BOOST_REQUIRE_EQUAL(type, assembly2.getHDF5LWRTag());

    //Check with bad size - negative
    //Test non-nullary constructor - size
    FuelAssembly assembly3(-1);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly3.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly3.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly3.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly3.getSize()); //Defaults
    BOOST_REQUIRE_EQUAL(type, assembly3.getHDF5LWRTag());

    //Check with name and size
    //Test non-nullary constructor - name, size
    FuelAssembly assembly4(defaultName, defaultSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly4.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly4.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly4.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly4.getSize());
    BOOST_REQUIRE_EQUAL(type, assembly4.getHDF5LWRTag());

    //Check with bad name
    //Test non-nullary constructor - name, size
    FuelAssembly assembly5(nullString, defaultSize);
    //Check values
    BOOST_REQUIRE_EQUAL(defaultName, assembly5.getName()); //Defaults
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly5.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly5.getId());
    BOOST_REQUIRE_EQUAL(defaultSize, assembly5.getSize());
    BOOST_REQUIRE_EQUAL(type, assembly5.getHDF5LWRTag());

    //Check with new name and size
    //Test non-nullary constructor - name, size
    FuelAssembly assembly6(newName, newSize);
    //Check values
    BOOST_REQUIRE_EQUAL(newName, assembly6.getName());
    BOOST_REQUIRE_EQUAL(defaultDesc, assembly6.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, assembly6.getId());
    BOOST_REQUIRE_EQUAL(newSize, assembly6.getSize());
    BOOST_REQUIRE_EQUAL(type, assembly6.getHDF5LWRTag());

    return;
    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkLabels) {

    // begin-user-code

    // Local Declarations
    std::vector<std::string> rowLabels;
    std::vector<std::string> colLabels;
    int assemblySize = 5;
    std::shared_ptr <GridLabelProvider> nullProvider;

    // Set the rowLabels
    rowLabels.push_back("1");
    rowLabels.push_back("2");
    rowLabels.push_back("3");
    rowLabels.push_back("4");
    rowLabels.push_back("5");

    // Set the colLabels
    colLabels.push_back("A");
    colLabels.push_back("B");
    colLabels.push_back("C");
    colLabels.push_back("D");
    colLabels.push_back("E");

    // Make a new assembly
    FuelAssembly assembly(assemblySize);
    //Check to see the default's gridLabelprovider
    BOOST_REQUIRE_EQUAL(-1, assembly.getGridLabelProvider().get()->getColumnFromLabel("A"));
    BOOST_REQUIRE_EQUAL(assemblySize, assembly.getGridLabelProvider().get()->getSize()); //Size the same as assembly.  Very important!

    // Check default values for gridlabelprovider
    std::shared_ptr<GridLabelProvider> provider (new GridLabelProvider(assemblySize));
    //Adding the column and row labels
    provider.get()->setColumnLabels(colLabels);
    provider.get()->setRowLabels(rowLabels);
    assembly.setGridLabelProvider(provider);

    //Check row and column labels
    BOOST_REQUIRE_EQUAL(colLabels.size(), assembly.getGridLabelProvider().get()->getSize());
    BOOST_REQUIRE_EQUAL(rowLabels.size(), assembly.getGridLabelProvider().get()->getSize());

    //Iterate over the list and check values
    for(int i = 0; i < colLabels.size(); i++) {
        BOOST_REQUIRE_EQUAL(colLabels.at(i).compare(assembly.getGridLabelProvider().get()->getLabelFromColumn(i)) == 0, true);
        BOOST_REQUIRE_EQUAL(rowLabels.at(i).compare(assembly.getGridLabelProvider().get()->getLabelFromRow(i)) == 0, true);
    }

    //You can not set it to null or illegal size
    assembly.setGridLabelProvider(nullProvider);

    //Check row and column labels
    BOOST_REQUIRE_EQUAL(colLabels.size(), assembly.getGridLabelProvider().get()->getSize());
    BOOST_REQUIRE_EQUAL(rowLabels.size(), assembly.getGridLabelProvider().get()->getSize());
    //Iterate over the list and check values
    for(int i = 0; i < colLabels.size(); i++) {
        BOOST_REQUIRE_EQUAL(colLabels.at(i).compare(assembly.getGridLabelProvider().get()->getLabelFromColumn(i)) == 0, true);
        BOOST_REQUIRE_EQUAL(rowLabels.at(i).compare(assembly.getGridLabelProvider().get()->getLabelFromRow(i)) == 0, true);
    }

    //Try applying a new grid Label provider
    std::shared_ptr<GridLabelProvider> newProvider(new GridLabelProvider(assemblySize + 22));
    assembly.setGridLabelProvider(newProvider);

    //Stays the same as before
    //Check row and column labels
    BOOST_REQUIRE_EQUAL(colLabels.size(), assembly.getGridLabelProvider().get()->getSize());
    BOOST_REQUIRE_EQUAL(rowLabels.size(), assembly.getGridLabelProvider().get()->getSize());
    //Iterate over the list and check values
    for(int i = 0; i < colLabels.size(); i++) {
        BOOST_REQUIRE_EQUAL(colLabels.at(i).compare(assembly.getGridLabelProvider().get()->getLabelFromColumn(i)) == 0, true);
        BOOST_REQUIRE_EQUAL(rowLabels.at(i).compare(assembly.getGridLabelProvider().get()->getLabelFromRow(i)) == 0, true);
    }

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkTube) {

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
    std::shared_ptr<Tube> nullTube;

    // Check the default values of the Component under test
    FuelAssembly assembly(assemblySize);

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
            BOOST_REQUIRE_EQUAL(assembly.getTubeByLocation(i, j).get() == NULL, true);
        }
    }

    //Check the names, should be empty!
    BOOST_REQUIRE_EQUAL(0, assembly.getTubeNames().size());

    // Try to get by name - valid string, empty string, and null
    BOOST_REQUIRE_EQUAL(assembly.getTubeByName("validNameThatDoesNotExistInThere152423").get() == NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getTubeByName("").get() == NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getTubeByName(nullString).get() == NULL, true);

    // Set the name
    std::shared_ptr<Tube> testComponent ( new Tube(testComponentName));

    // Add to the assembly
    assembly.addTube(testComponent);

    // See that no location is set
    BOOST_REQUIRE_EQUAL(assembly.getTubeByLocation(rowLoc1, colLoc1).get()==NULL, true);
    // Check locations to be within bounds
    BOOST_REQUIRE_EQUAL(assembly.getTubeByLocation(-1, assemblySize - 1).get() == NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getTubeByLocation(1, assemblySize - 1).get()==NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getTubeByLocation(assemblySize + 25, assemblySize - 1).get() == NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getTubeByLocation(assemblySize - 1, assemblySize + 25).get() == NULL, true);

    // Set the valid location:
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(testComponentName, rowLoc1, colLoc1), true);

    // Try to break location setter
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(nullString, rowLoc1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(testComponentName, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(testComponentName, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(nullString, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(nullString, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(nullString, -1, -1), false);
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(testComponentName, rowLoc1, assemblySize + 25), false);
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(testComponentName, assemblySize + 25, colLoc1), false);

    // The above erroneous settings does not change the original location of
    // the first, valid set
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*assembly.getTubeByName(testComponentName).get()), true);

    // Check invalid overwrite of location:
    std::shared_ptr<Tube>testComponent2 (new Tube(testComponentName2));

    // Add assembly, overwrite the previous testComponent's location
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(testComponent2.get()->getName(), rowLoc1, colLoc1), false);

    // Check that it is the first, but not second
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*assembly.getTubeByName(testComponentName).get()), true);

    // Add it in there
    BOOST_REQUIRE_EQUAL(assembly.addTube(testComponent2), true);

    // Show that you can have at least 2 components in there
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(testComponent2.get()->getName(), rowLoc2, colLoc2), true);

    // Check values - see the components are different and they reside in
    // the table correctly
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*assembly
                        .getTubeByName(testComponentName).get()), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*assembly
                        .getTubeByName(testComponentName2).get()), true);

    // Check the locations
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*assembly.getTubeByLocation(rowLoc1, colLoc1).get()), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*assembly.getTubeByLocation(rowLoc2, colLoc2).get()), true);

    //Check the names, should contain 2!
    BOOST_REQUIRE_EQUAL(2, assembly.getTubeNames().size());
    BOOST_REQUIRE_EQUAL(testComponentName, assembly.getTubeNames().at(1));
    BOOST_REQUIRE_EQUAL(testComponentName2, assembly.getTubeNames().at(0));

    // Check operation for null
    assembly.addTube(nullTube);
    BOOST_REQUIRE_EQUAL(assembly.getTubeByName(nullString).get() == NULL, true); // Make sure null does not work


    // Finally, demonstrate what happens when a component of the same name
    // is added, it should not overwrite the previous item in the table!
    std::shared_ptr<Tube> testComponent3 (new Tube(testComponent.get()->getName())); // Same name as the other component
    testComponent3.get()->setId(testComponentId); // Id should differ from
    // testComponent!
    BOOST_REQUIRE_EQUAL(testComponent.get()->getId() == testComponentId, false);

    // Overwrite in table
    BOOST_REQUIRE_EQUAL(assembly.addTube(testComponent3), false);

    // Check that the object has not been overwritten
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*assembly.getTubeByName(testComponentName).get()), true);
    BOOST_REQUIRE_EQUAL(testComponent3.get()->operator==(*assembly.getTubeByName(testComponentName).get()), false);

    // Test to remove components from the assembly
    BOOST_REQUIRE_EQUAL(assembly.removeTube(nullString), false);
    BOOST_REQUIRE_EQUAL(assembly.removeTube(""), false);
    BOOST_REQUIRE_EQUAL(assembly.removeTube("!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"), false);

    // Remove the first component
    BOOST_REQUIRE_EQUAL(assembly.removeTube(testComponent.get()->getName()), true);

    // Check that it does not exist in the location or getting the name
    BOOST_REQUIRE_EQUAL(assembly.getTubeByLocation(rowLoc1, colLoc1).get() == NULL, true);
    BOOST_REQUIRE_EQUAL(assembly.getTubeByName(testComponent.get()->getName()).get() == NULL, true);
    //Check size
    BOOST_REQUIRE_EQUAL(1, assembly.getNumberOfTubes());

    // It can now be overridden!
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(testComponent2.get()->getName(), rowLoc1, colLoc1), true);

    //Show that the component's names can NOT overwrite each others locations
    BOOST_REQUIRE_EQUAL(assembly.addTube(testComponent), true);
    BOOST_REQUIRE_EQUAL(assembly.setTubeLocation(testComponent.get()->getName(), rowLoc1, colLoc1), false);

    //Check the size, the respective locations
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), assembly.getTubeByLocation(rowLoc1, colLoc1).get()->getName());
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), assembly.getTubeByLocation(rowLoc2, colLoc2).get()->getName());
    BOOST_REQUIRE_EQUAL(2, assembly.getNumberOfTubes());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkRodClusterAssembly) {

    // begin-user-code
    //Local Declarations
    std::shared_ptr<RodClusterAssembly> newRod (new RodClusterAssembly(1));
    std::shared_ptr<RodClusterAssembly> nullRod;
    FuelAssembly assembly(2);
    std::string name = "1231231A rod12313123/";

    //Set the newRod's info
    newRod.get()->setName(name);

    //Check the getter on fuel assembly.  Show that it is null
    BOOST_REQUIRE_EQUAL(assembly.getRodClusterAssembly().get() == NULL, true);

    //Set it
    assembly.setRodClusterAssembly(newRod);
    //Check that it was created and is the newly created rod
    BOOST_REQUIRE_EQUAL(name, assembly.getRodClusterAssembly().get()->getName());

    //Try to set it to null
    assembly.setRodClusterAssembly(nullRod);

    //See that it is null
    BOOST_REQUIRE_EQUAL(assembly.getRodClusterAssembly().get() == NULL, true);

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

    std::shared_ptr<LWRComposite> tubeComposite (new LWRComposite());
    std::string tubeName = "Tubes";
    std::string tubeDescription = "A Composite that contains many Tubes.";
    int tubeId = 2;

    //Setup LWRRodComposite for comparison
    rodComposite.get()->setName(compName);
    rodComposite.get()->setId(compId);
    rodComposite.get()->setDescription(compDescription);

    //Setup tubeComposite for comparison
    tubeComposite.get()->setName(tubeName);
    tubeComposite.get()->setId(tubeId);
    tubeComposite.get()->setDescription(tubeDescription);

    //Add components to arrays
    compNames.push_back(rodComposite.get()->getName());
    compNames.push_back(tubeComposite.get()->getName());
    components.push_back(rodComposite);
    components.push_back(tubeComposite);
    //Setup the default number of components
    numberOfDefaultComponents = components.size();

    //Check the default Composite size and attributes on PWRAssembly
    FuelAssembly assembly(assemblySize);

    //Has a size of numberOfDefaultComponents
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, assembly.getNumberOfComponents());
    //It is equal to the default rodComposite for many of the composite getters
    std::shared_ptr<LWRComposite> castedComposite1(new LWRComposite(*(dynamic_cast<LWRComposite *> (assembly.getComponent(1).get()))));
    std::shared_ptr<LWRComposite> castedComposite2(new LWRComposite(*(dynamic_cast<LWRComposite *> (assembly.getComponent(rodComposite.get()->getName()).get()))));
    std::shared_ptr<LWRComposite> castedComposite3(new LWRComposite(*(dynamic_cast<LWRComposite *> (assembly.getComponent(2).get()))));
    std::shared_ptr<LWRComposite> castedComposite4(new LWRComposite(*(dynamic_cast<LWRComposite *> (assembly.getComponent(tubeComposite.get()->getName()).get()))));
    BOOST_REQUIRE_EQUAL(rodComposite.get()->operator==(*castedComposite1.get()), true);
    BOOST_REQUIRE_EQUAL(rodComposite.get()->operator==(*castedComposite2.get()), true);
    BOOST_REQUIRE_EQUAL(tubeComposite.get()->operator==(*castedComposite3.get()), true);
    BOOST_REQUIRE_EQUAL(tubeComposite.get()->operator==(*castedComposite4.get()), true);
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
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    std::string name = "Billy";
    int size = 5;
    std::shared_ptr<LWRRod> rod(new LWRRod("Bob the rod"));
    std::shared_ptr<Tube> tube(new Tube("Billy the tube"));

    //Setup root object
    FuelAssembly object(name, size);
    object.addLWRRod(rod);
    object.addTube(tube);
    object.setLWRRodLocation(rod.get()->getName(), 0, 0);
    object.setTubeLocation(tube.get()->getName(), 1, 1);

    //Setup equalObject equal to object
    FuelAssembly equalObject(name, size);
    equalObject.addLWRRod(rod);
    equalObject.addTube(tube);
    equalObject.setLWRRodLocation(rod.get()->getName(), 0, 0);
    equalObject.setTubeLocation(tube.get()->getName(), 1, 1);

    //Setup transitiveObject equal to object
    FuelAssembly transitiveObject(name, size);
    transitiveObject.addLWRRod(rod);
    transitiveObject.addTube(tube);
    transitiveObject.setLWRRodLocation(rod.get()->getName(), 0, 0);
    transitiveObject.setTubeLocation(tube.get()->getName(), 1, 1);

    // Set its data, not equal to object
    FuelAssembly unEqualObject(name, size);
    unEqualObject.addLWRRod(rod);
    unEqualObject.setLWRRodLocation(rod.get()->getName(), 0, 0);
    //No tube

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

    // end-user-code
}
BOOST_AUTO_TEST_CASE(checkCopying) {

    // begin-user-code

    //Local Declarations
    std::string name = "Billy";
    int size = 5;
    std::shared_ptr<LWRRod> rod(new LWRRod("Bob the rod"));
    std::shared_ptr<Tube> tube(new Tube("Billy the tube"));

    //Setup root object
    FuelAssembly object(name, size);
    object.addLWRRod(rod);
    object.addTube(tube);
    object.setLWRRodLocation(rod.get()->getName(), 0, 0);
    object.setTubeLocation(tube.get()->getName(), 1, 1);


    //Run the copy routine
    FuelAssembly copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<FuelAssembly> castedObject (new FuelAssembly(*(dynamic_cast<FuelAssembly *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code
}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    FuelAssembly component(size);
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    double rodPitch =30.0;
    std::shared_ptr<LWRRod> rod1(new LWRRod("Rod1"));
    std::shared_ptr<LWRRod> rod2( new LWRRod("Rod2"));
    std::shared_ptr<LWRRod> rod3( new LWRRod("Rod3"));
    std::shared_ptr<Tube> tube1( new Tube("Tube1"));
    std::shared_ptr<Tube> tube2( new Tube("Tube2"));
    std::shared_ptr<Tube> tube3( new Tube("Tube3"));
    std::shared_ptr<GridLocation> loc1(new GridLocation(1, 0));
    std::shared_ptr<GridLocation> loc2( new GridLocation(2, 0));
    std::shared_ptr<GridLocation> loc3( new GridLocation(4, 3));
    std::shared_ptr<GridLocation> loc4( new GridLocation(3, 3));
    std::shared_ptr<GridLocation> loc5( new GridLocation(2, 5));
    std::shared_ptr<GridLocation> loc6( new GridLocation(1, 2));
    std::vector<std::string> rowLabels;
    std::vector<std::string> colLabels;
    GridLabelProvider gridLabels(size);

    //Setup duplicate manager for comparison testing
    LWRGridManager rodManager(size);
    rodManager.setName("LWRRod Grid");
    rodManager.addComponent(rod1, loc1);
    rodManager.addComponent(rod2, loc2);
    rodManager.addComponent(rod3, loc3);

    LWRGridManager tubeManager(size);
    tubeManager.setName("Tube Grid");
    tubeManager.addComponent(tube1, loc4);
    tubeManager.addComponent(tube2, loc5);
    tubeManager.addComponent(tube3, loc6);

    //Setup label manager
    // Set the rowLabels
    rowLabels.push_back("1");
    rowLabels.push_back("2");
    rowLabels.push_back("3");
    rowLabels.push_back("4");
    rowLabels.push_back("5");

    // Set the colLabels
    colLabels.push_back("A");
    colLabels.push_back("B");
    colLabels.push_back("C");
    colLabels.push_back("D");
    colLabels.push_back("E");

    //setup gridLabels
    gridLabels.setColumnLabels(colLabels);
    gridLabels.setRowLabels(rowLabels);
    gridLabels.setName("Grid Labels");

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
    component.addTube(tube1);
    component.addTube(tube2);
    component.addTube(tube3);
    component.setLWRRodLocation(rod1.get()->getName(), loc1.get()->getRow(), loc1.get()->getColumn());
    component.setLWRRodLocation(rod2.get()->getName(), loc2.get()->getRow(), loc2.get()->getColumn());
    component.setLWRRodLocation(rod3.get()->getName(), loc3.get()->getRow(), loc3.get()->getColumn());
    component.setTubeLocation(tube1.get()->getName(), loc4.get()->getRow(), loc4.get()->getColumn());
    component.setTubeLocation(tube2.get()->getName(), loc5.get()->getRow(), loc5.get()->getColumn());
    component.setTubeLocation(tube3.get()->getName(), loc6.get()->getRow(), loc6.get()->getColumn());
    component.getGridLabelProvider().get()->setColumnLabels(colLabels);
    component.getGridLabelProvider().get()->setRowLabels(rowLabels);

    //exception hit
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL(component.getWriteableChildren().size(), 5);

    //Get the first component and its sub components for comparisons
    std::shared_ptr<LWRComposite> writeableComposite1 = std::dynamic_pointer_cast<LWRComposite > (component.getWriteableChildren()[0]);
    std::shared_ptr<LWRComposite> writeableComposite2 = std::dynamic_pointer_cast<LWRComposite > (component.getWriteableChildren()[1]);
    std::shared_ptr<LWRGridManager> rodGridManager = std::dynamic_pointer_cast<LWRGridManager> (component.getWriteableChildren()[2]);
    std::shared_ptr<GridLabelProvider> fuelLabels = std::dynamic_pointer_cast<GridLabelProvider> (component.getWriteableChildren()[3]);
    std::shared_ptr<LWRGridManager> tubeGridManager = std::dynamic_pointer_cast<LWRGridManager> (component.getWriteableChildren()[4]);

    //Cast Writeables for rods and tubes
    std::shared_ptr<LWRRod> writeableComponent1 = std::dynamic_pointer_cast<LWRRod> (writeableComposite1.get()->getWriteableChildren()[0]);
    std::shared_ptr<LWRRod> writeableComponent2 = std::dynamic_pointer_cast<LWRRod> (writeableComposite1.get()->getWriteableChildren()[1]);
    std::shared_ptr<LWRRod> writeableComponent3 = std::dynamic_pointer_cast<LWRRod> (writeableComposite1.get()->getWriteableChildren()[2]);
    std::shared_ptr<Tube> writeableComponent4 = std::dynamic_pointer_cast<Tube> (writeableComposite2.get()->getWriteableChildren()[0]);
    std::shared_ptr<Tube> writeableComponent5 = std::dynamic_pointer_cast<Tube> (writeableComposite2.get()->getWriteableChildren()[1]);
    std::shared_ptr<Tube> writeableComponent6 = std::dynamic_pointer_cast<Tube> (writeableComposite2.get()->getWriteableChildren()[2]);

    //Check rods, tubes, and manager/providers
    BOOST_REQUIRE_EQUAL(rodManager.operator ==(*rodGridManager.get()), true);
    BOOST_REQUIRE_EQUAL(tubeManager.operator ==(*tubeGridManager.get()), true);
    BOOST_REQUIRE_EQUAL(gridLabels.operator ==(*fuelLabels.get()), true);
    BOOST_REQUIRE_EQUAL(rod1.get()->getName(), writeableComponent1.get()->getName());
    BOOST_REQUIRE_EQUAL(rod2.get()->getName(), writeableComponent2.get()->getName());
    BOOST_REQUIRE_EQUAL(rod3.get()->getName(), writeableComponent3.get()->getName());
    BOOST_REQUIRE_EQUAL(tube1.get()->getName(), writeableComponent4.get()->getName());
    BOOST_REQUIRE_EQUAL(tube2.get()->getName(), writeableComponent5.get()->getName());
    BOOST_REQUIRE_EQUAL(tube3.get()->getName(), writeableComponent6.get()->getName());

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

    //Local Declarations
    int size = 5;
    FuelAssembly component(size);
    FuelAssembly newComponent(-1);
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    std::string attributeValue;
    double rodPitch =30.0;
    std::shared_ptr<LWRRod> rod1(new LWRRod("Rod1"));
    std::shared_ptr<LWRRod> rod2( new LWRRod("Rod2"));
    std::shared_ptr<LWRRod> rod3( new LWRRod("Rod3"));
    std::shared_ptr<Tube> tube1( new Tube("Tube1"));
    std::shared_ptr<Tube> tube2( new Tube("Tube2"));
    std::shared_ptr<Tube> tube3( new Tube("Tube3"));
    std::shared_ptr<GridLocation> loc1(new GridLocation(1, 0));
    std::shared_ptr<GridLocation> loc2( new GridLocation(2, 0));
    std::shared_ptr<GridLocation> loc3( new GridLocation(4, 3));
    std::shared_ptr<GridLocation> loc4( new GridLocation(3, 3));
    std::shared_ptr<GridLocation> loc5( new GridLocation(2, 5));
    std::shared_ptr<GridLocation> loc6( new GridLocation(1, 2));
    std::vector<std::string> rowLabels;
    std::vector<std::string> colLabels;
    std::shared_ptr<GridLabelProvider> gridLabels( new GridLabelProvider(size));

    //Setup duplicate manager for comparison testing
    std::shared_ptr<LWRGridManager> rodManager(new LWRGridManager(size));
    rodManager.get()->setName("LWRRod Grid");
    rodManager.get()->addComponent(rod1, loc1);
    rodManager.get()->addComponent(rod2, loc2);
    rodManager.get()->addComponent(rod3, loc3);

    std::shared_ptr<LWRGridManager> tubeManager(new LWRGridManager(size));
    tubeManager.get()->setName("Tube Grid");
    tubeManager.get()->addComponent(tube1, loc4);
    tubeManager.get()->addComponent(tube2, loc5);
    tubeManager.get()->addComponent(tube3, loc6);

    //Setup label manager
    // Set the rowLabels
    rowLabels.push_back("1");
    rowLabels.push_back("2");
    rowLabels.push_back("3");
    rowLabels.push_back("4");
    rowLabels.push_back("5");

    // Set the colLabels
    colLabels.push_back("A");
    colLabels.push_back("B");
    colLabels.push_back("C");
    colLabels.push_back("D");
    colLabels.push_back("E");

    //setup gridLabels
    gridLabels.get()->setColumnLabels(colLabels);
    gridLabels.get()->setRowLabels(rowLabels);
    gridLabels.get()->setName("Grid Labels");

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
    component.addTube(tube1);
    component.addTube(tube2);
    component.addTube(tube3);
    component.setLWRRodLocation(rod1.get()->getName(), loc1.get()->getRow(), loc1.get()->getColumn());
    component.setLWRRodLocation(rod2.get()->getName(), loc2.get()->getRow(), loc2.get()->getColumn());
    component.setLWRRodLocation(rod3.get()->getName(), loc3.get()->getRow(), loc3.get()->getColumn());
    component.setTubeLocation(tube1.get()->getName(), loc4.get()->getRow(), loc4.get()->getColumn());
    component.setTubeLocation(tube2.get()->getName(), loc5.get()->getRow(), loc5.get()->getColumn());
    component.setTubeLocation(tube3.get()->getName(), loc6.get()->getRow(), loc6.get()->getColumn());
    component.getGridLabelProvider().get()->setColumnLabels(colLabels);
    component.getGridLabelProvider().get()->setRowLabels(rowLabels);


    //Setup Rod Composite
    std::shared_ptr <LWRComposite> rodComposite (new LWRComposite());
    rodComposite.get()->setName("LWRRods");
    rodComposite.get()->setDescription("A Composite that contains many LWRRods.");
    rodComposite.get()->addComponent(rod1);
    rodComposite.get()->addComponent(rod2);
    rodComposite.get()->addComponent(rod3);
    rodComposite.get()->setId(1);

    //Setup Tube Composite
    std::shared_ptr <LWRComposite> tubeComposite (new LWRComposite());
    tubeComposite.get()->setName("Tubes");
    tubeComposite.get()->setDescription("A Composite that contains many Tubes.");
    tubeComposite.get()->addComponent(tube1);
    tubeComposite.get()->addComponent(tube2);
    tubeComposite.get()->addComponent(tube3);
    tubeComposite.get()->setId(2);

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
        BOOST_REQUIRE_EQUAL(newComponent.readChild(tubeComposite), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(rodManager), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(tubeManager), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(gridLabels), true);

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
	std::shared_ptr<Tube> tube (new Tube("Bob the tube"));

	//Setup root object
	FuelAssembly assembly(name, size);
	assembly.addTube(tube);

	//Locations
	int row1 = 0, col1 = 0, row2 = 1, col2 = 1, row3= 2, col3 = 3;

	//Check getters with nothing on the object
	BOOST_REQUIRE_EQUAL(assembly.getTubeDataProviderAtLocation(row1, col1).get() == NULL, true);
	BOOST_REQUIRE_EQUAL(assembly.getTubeDataProviderAtLocation(row2, col2).get() == NULL, true);
	BOOST_REQUIRE_EQUAL(assembly.getTubeDataProviderAtLocation(row3, col3).get() == NULL, true);

	//Add locations
	assembly.setTubeLocation(tube.get()->getName(), row1, col1);
	assembly.setTubeLocation(tube.get()->getName(), row2, col2);
	assembly.setTubeLocation(tube.get()->getName(), row3, col3);

	//Check not null
	BOOST_REQUIRE_EQUAL(assembly.getTubeDataProviderAtLocation(row1, col1).get() != NULL, true);
	BOOST_REQUIRE_EQUAL(assembly.getTubeDataProviderAtLocation(row2, col2).get() != NULL, true);
	BOOST_REQUIRE_EQUAL(assembly.getTubeDataProviderAtLocation(row3, col3).get() != NULL, true);

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
	assembly.getTubeDataProviderAtLocation(row1, col1).get()->addData(data1, time1);
	assembly.getTubeDataProviderAtLocation(row1, col1).get()->addData(data2, time1);
	assembly.getTubeDataProviderAtLocation(row1, col1).get()->addData(data3, time1);
	assembly.getTubeDataProviderAtLocation(row1, col1).get()->addData(data4, time2);

	//Verify data
	BOOST_REQUIRE_EQUAL(provider.operator ==(*assembly.getTubeDataProviderAtLocation(row1, col1).get()), true);

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
