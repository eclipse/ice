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
#define BOOST_TEST_MODULE PressurizedWaterReactorTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <pwr/PressurizedWaterReactor.h>
#include <HdfWriterFactory.h>
#include <HdfReaderFactory.h>
#include <HdfFileFactory.h>
#include <UtilityOperations.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <AssemblyType.h>

using namespace ICE_Reactor;
using namespace ICE_IO;
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(PressurizedWaterReactorTester_testSuite)

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

    // Local Declarations
    int defaultSize = 17; // Default size when an erroneous value is set on
    // the reactor
    std::string defaultName = "PressurizedWaterReactor 1";
    std::string defaultDescription = "PressurizedWaterReactor 1's Description";
    int defaultId = 1;
    HDF5LWRTagType type = PWREACTOR;

    // This test is to show the default value for a reactor when it is
    // created with a negative value.
    PressurizedWaterReactor defaultReactor(-1);
    BOOST_REQUIRE_EQUAL(defaultSize, defaultReactor.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, defaultReactor.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, defaultReactor.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, defaultReactor.getId());
    BOOST_REQUIRE_EQUAL(type, defaultReactor.getHDF5LWRTag());

    // This test is to show the default value for a reactor when its created
    // with a zero value
    PressurizedWaterReactor defaultReactor2(0);
    BOOST_REQUIRE_EQUAL(defaultSize, defaultReactor2.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, defaultReactor2.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, defaultReactor2.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, defaultReactor2.getId());
    BOOST_REQUIRE_EQUAL(type, defaultReactor2.getHDF5LWRTag());

    // This is a test to show a valid creation of a reactor
    PressurizedWaterReactor reactor(17);
    BOOST_REQUIRE_EQUAL(17, reactor.getSize());
    BOOST_REQUIRE_EQUAL(defaultName, reactor.getName());
    BOOST_REQUIRE_EQUAL(defaultDescription, reactor.getDescription());
    BOOST_REQUIRE_EQUAL(defaultId, reactor.getId());
    BOOST_REQUIRE_EQUAL(type, reactor.getHDF5LWRTag());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkLabels) {
    // Local Declarations
    std::vector<std::string> rowLabels;
    std::vector<std::string> colLabels;
    int reactorSize = 5;
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

    // Make a new reactor
    PressurizedWaterReactor reactor(reactorSize);
    //Check to see the default's gridLabelprovider
    BOOST_REQUIRE_EQUAL(-1, reactor.getGridLabelProvider().get()->getColumnFromLabel("A"));
    BOOST_REQUIRE_EQUAL(reactorSize, reactor.getGridLabelProvider().get()->getSize()); //Size the same as reactor.  Very important!

    // Check default values for gridlabelprovider
    std::shared_ptr<GridLabelProvider> provider (new GridLabelProvider(reactorSize));
    //Adding the column and row labels
    provider.get()->setColumnLabels(colLabels);
    provider.get()->setRowLabels(rowLabels);
    reactor.setGridLabelProvider(provider);

    //Check row and column labels
    BOOST_REQUIRE_EQUAL(colLabels.size(), reactor.getGridLabelProvider().get()->getSize());
    BOOST_REQUIRE_EQUAL(rowLabels.size(), reactor.getGridLabelProvider().get()->getSize());

    //Iterate over the list and check values
    for(int i = 0; i < colLabels.size(); i++) {
        BOOST_REQUIRE_EQUAL(colLabels.at(i).compare(reactor.getGridLabelProvider().get()->getLabelFromColumn(i)) == 0, true);
        BOOST_REQUIRE_EQUAL(rowLabels.at(i).compare(reactor.getGridLabelProvider().get()->getLabelFromRow(i)) == 0, true);
    }

    //You can not set it to null or illegal size
    reactor.setGridLabelProvider(nullProvider);

    //Check row and column labels
    BOOST_REQUIRE_EQUAL(colLabels.size(), reactor.getGridLabelProvider().get()->getSize());
    BOOST_REQUIRE_EQUAL(rowLabels.size(), reactor.getGridLabelProvider().get()->getSize());
    //Iterate over the list and check values
    for(int i = 0; i < colLabels.size(); i++) {
        BOOST_REQUIRE_EQUAL(colLabels.at(i).compare(reactor.getGridLabelProvider().get()->getLabelFromColumn(i)) == 0, true);
        BOOST_REQUIRE_EQUAL(rowLabels.at(i).compare(reactor.getGridLabelProvider().get()->getLabelFromRow(i)) == 0, true);
    }

    //Try applying a new grid Label provider
    std::shared_ptr<GridLabelProvider> newProvider(new GridLabelProvider(reactorSize + 22));
    reactor.setGridLabelProvider(newProvider);

    //Stays the same as before
    //Check row and column labels
    BOOST_REQUIRE_EQUAL(colLabels.size(), reactor.getGridLabelProvider().get()->getSize());
    BOOST_REQUIRE_EQUAL(rowLabels.size(), reactor.getGridLabelProvider().get()->getSize());
    //Iterate over the list and check values
    for(int i = 0; i < colLabels.size(); i++) {
        BOOST_REQUIRE_EQUAL(colLabels.at(i).compare(reactor.getGridLabelProvider().get()->getLabelFromColumn(i)) == 0, true);
        BOOST_REQUIRE_EQUAL(rowLabels.at(i).compare(reactor.getGridLabelProvider().get()->getLabelFromRow(i)) == 0, true);
    }

    return;

    // end-user-code
}
BOOST_AUTO_TEST_CASE(checkFuelAssembly) {

    // begin-user-code

    // Local Declarations
    int reactorSize = 17;
    PressurizedWaterReactor reactor(reactorSize);
    std::shared_ptr<FuelAssembly> testComponent( new FuelAssembly(5)), testComponent2(new FuelAssembly(5)), testComponent3(new FuelAssembly(5));
    std::shared_ptr<FuelAssembly> nullComponent;
    std::string testComponentName = "Bob";
    std::string testComponentName2 = "Bill!";
    int rowLoc1 = 5, colLoc1 = 5;
    int rowLoc2 = 6, colLoc2 = 6;
    int testComponentId = 1000001;
    std::string nullString;

    // Check the getter and setter for the pitch

    // Check default value of Pitch
    BOOST_REQUIRE_EQUAL(0.0, reactor.getFuelAssemblyPitch());

    // Set the pitch to 0 and check setting - VALID
    reactor.setFuelAssemblyPitch(0.0);

    BOOST_REQUIRE_EQUAL(0.0, reactor.getFuelAssemblyPitch());

    // Try to set to positive double - VALID
    reactor.setFuelAssemblyPitch(100.01);
    BOOST_REQUIRE_EQUAL(100.01, reactor.getFuelAssemblyPitch());

    // Try to set to negative double - NOT VALID
    reactor.setFuelAssemblyPitch(-1.0);
    BOOST_REQUIRE_EQUAL(100.01, reactor.getFuelAssemblyPitch()); // Stays the same as the previous value

    // Check the default values of the Component under test

    // No assemblies should be added by default. Therefore every
    // location is bad
    for (int i = 0; i < reactorSize; i++) {
        for (int j = 0; j < reactorSize; j++) {
            BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Fuel, i, j).get() == NULL, true);
        }
    }

    //Check the names, should be empty!
    BOOST_REQUIRE_EQUAL(0, reactor.getAssemblyNames(Fuel).size());

    // Try to get by name - valid string, empty string, and null
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Fuel, "validNameThatDoesNotExistInThere152423").get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Fuel, "").get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Fuel, nullString).get()== NULL, true);

    // Set the name
    testComponent.get()->setName(testComponentName);

    // Add to the reactor
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Fuel, testComponent), true);

    // See that no location is set
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Fuel, rowLoc1, colLoc1).get()== NULL, true);
    // Check locations to be within bounds
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Fuel, -1, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Fuel, 1, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Fuel, reactorSize + 25, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Fuel, reactorSize - 1, reactorSize + 25).get()== NULL, true);

    // Set the valid location:
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, testComponentName, rowLoc1, colLoc1), true);

    // Try to break location setter
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, nullString, rowLoc1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, testComponentName, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, testComponentName, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, nullString, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, nullString, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, nullString, -1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, testComponentName, rowLoc1, reactorSize + 25), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, testComponentName, reactorSize + 25, colLoc1), false);

    // The above erroneous settings does not change the original location of
    // the first, valid set
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<FuelAssembly>(reactor.getAssemblyByName(Fuel, testComponentName))), true);

    // Check invalid overwrite of location:
    testComponent2.get()->setName(testComponentName2);

    // Add reactor, overwrite the previous testComponent's location
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, testComponent2.get()->getName(), rowLoc1, colLoc1), false);

    // Check that it is the first, but not second
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<FuelAssembly>(reactor.getAssemblyByName(Fuel, testComponentName))), true);

    // Add it in there
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Fuel, testComponent2), true);

    // Show that you can have at least 2 components in there
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, testComponent2.get()->getName(), rowLoc2, colLoc2), true);

    // Check values - see the components are different and they reside in
    // the table correctly
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<FuelAssembly>(reactor.getAssemblyByName(Fuel, testComponentName))), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*std::dynamic_pointer_cast<FuelAssembly>(reactor.getAssemblyByName(Fuel, testComponentName2))), true);

    // Check the locations
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<FuelAssembly>(reactor.getAssemblyByLocation(Fuel, rowLoc1, colLoc1))), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*std::dynamic_pointer_cast<FuelAssembly>(reactor.getAssemblyByLocation(Fuel, rowLoc2, colLoc2))), true);

    //Check the names, should contain 2!
    BOOST_REQUIRE_EQUAL(2, reactor.getAssemblyNames(Fuel).size());
    BOOST_REQUIRE_EQUAL(testComponentName, reactor.getAssemblyNames(Fuel).at(1));
    BOOST_REQUIRE_EQUAL(testComponentName2, reactor.getAssemblyNames(Fuel).at(0));

    // Check operation for null
    reactor.addAssembly(Fuel, nullComponent);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Fuel, nullString).get()== NULL, true); // Make sure null does
    // not work!

    // Finally, demonstrate what happens when a component of the same name
    // is added, it should not overwrite the previous item in the table!
    testComponent3.get()->setName(testComponent.get()->getName()); // Same name as the other
    // component
    testComponent3.get()->setId(testComponentId); // Id should differ from
    // testComponent!
    BOOST_REQUIRE_EQUAL(testComponent.get()->getId()== testComponentId, false);

    // Overwrite in table
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Fuel, testComponent3), false);

    // Check that the object has not been overwritten
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<FuelAssembly>(reactor.getAssemblyByName(Fuel, testComponentName))), true);
    BOOST_REQUIRE_EQUAL(testComponent3.get()->operator==(*std::dynamic_pointer_cast<FuelAssembly>(reactor.getAssemblyByName(Fuel, testComponentName))), false);

    // Test to remove components from the reactor
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Fuel, nullString), false);
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Fuel, ""), false);
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Fuel, "!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"), false);

    // Remove the first component
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Fuel, testComponent.get()->getName()), true);

    // Check that it does not exist in the location or getting the name
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Fuel, rowLoc1, colLoc1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Fuel, testComponent.get()->getName()).get()== NULL, true);
    //Check size
    BOOST_REQUIRE_EQUAL(1, reactor.getNumberOfAssemblies(Fuel));

    // It can now be overridden!
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, testComponent2.get()->getName(), rowLoc1, colLoc1), true);

    //Show that the component's names can NOT overwrite each others locations
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Fuel, testComponent), true);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Fuel, testComponent.get()->getName(), rowLoc1, colLoc1), false);

    //Check the size, the respective locations
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), reactor.getAssemblyByLocation(Fuel, rowLoc1, colLoc1).get()->getName());
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), reactor.getAssemblyByLocation(Fuel, rowLoc2, colLoc2).get()->getName());
    BOOST_REQUIRE_EQUAL(2, reactor.getNumberOfAssemblies(Fuel));

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkControlBank) {

    // begin-user-code

    // Local Declarations
    int reactorSize = 17;
    PressurizedWaterReactor reactor(reactorSize);
    std::shared_ptr<ControlBank> testComponent( new ControlBank()), testComponent2(new ControlBank()), testComponent3(new ControlBank());
    std::shared_ptr<ControlBank> nullComponent;
    std::string testComponentName = "Bob";
    std::string testComponentName2 = "Bill!";
    int rowLoc1 = 5, colLoc1 = 5;
    int rowLoc2 = 6, colLoc2 = 6;
    int testComponentId = 1000001;
    std::string nullString;


    // Check the default values of the Component under test

    // No assemblies should be added by default. Therefore every
    // location is bad
    for (int i = 0; i < reactorSize; i++) {
        for (int j = 0; j < reactorSize; j++) {
            BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Control_Bank, i, j).get() == NULL, true);
        }
    }

    //Check the names, should be empty!
    BOOST_REQUIRE_EQUAL(0, reactor.getAssemblyNames(Control_Bank).size());

    // Try to get by name - valid string, empty string, and null
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Control_Bank, "validNameThatDoesNotExistInThere152423").get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Control_Bank, "").get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Control_Bank, nullString).get()== NULL, true);

    // Set the name
    testComponent.get()->setName(testComponentName);

    // Add to the reactor
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Control_Bank, testComponent), true);

    // See that no location is set
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Control_Bank, rowLoc1, colLoc1).get()== NULL, true);
    // Check locations to be within bounds
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Control_Bank, -1, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Control_Bank, 1, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Control_Bank, reactorSize + 25, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Control_Bank, reactorSize - 1, reactorSize + 25).get()== NULL, true);

    // Set the valid location:
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, testComponentName, rowLoc1, colLoc1), true);

    // Try to break location setter
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, nullString, rowLoc1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, testComponentName, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, testComponentName, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, nullString, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, nullString, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, nullString, -1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, testComponentName, rowLoc1, reactorSize + 25), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, testComponentName, reactorSize + 25, colLoc1), false);

    // The above erroneous settings does not change the original location of
    // the first, valid set
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<ControlBank>(reactor.getAssemblyByName(Control_Bank, testComponentName))), true);

    // Check invalid overwrite of location:
    testComponent2.get()->setName(testComponentName2);

    // Add reactor, overwrite the previous testComponent's location
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, testComponent2.get()->getName(), rowLoc1, colLoc1), false);

    // Check that it is the first, but not second
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<ControlBank>(reactor.getAssemblyByName(Control_Bank, testComponentName))), true);

    // Add it in there
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Control_Bank, testComponent2), true);

    // Show that you can have at least 2 components in there
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, testComponent2.get()->getName(), rowLoc2, colLoc2), true);

    // Check values - see the components are different and they reside in
    // the table correctly
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<ControlBank>(reactor.getAssemblyByName(Control_Bank, testComponentName))), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*std::dynamic_pointer_cast<ControlBank>(reactor.getAssemblyByName(Control_Bank, testComponentName2))), true);

    // Check the locations
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<ControlBank>(reactor.getAssemblyByLocation(Control_Bank, rowLoc1, colLoc1))), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*std::dynamic_pointer_cast<ControlBank>(reactor.getAssemblyByLocation(Control_Bank, rowLoc2, colLoc2))), true);

    //Check the names, should contain 2!
    BOOST_REQUIRE_EQUAL(2, reactor.getAssemblyNames(Control_Bank).size());
    BOOST_REQUIRE_EQUAL(testComponentName, reactor.getAssemblyNames(Control_Bank).at(1));
    BOOST_REQUIRE_EQUAL(testComponentName2, reactor.getAssemblyNames(Control_Bank).at(0));

    // Check operation for null
    reactor.addAssembly(Control_Bank, nullComponent);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Control_Bank, nullString).get()== NULL, true); // Make sure null does
    // not work!

    // Finally, demonstrate what happens when a component of the same name
    // is added, it should not overwrite the previous item in the table!
    testComponent3.get()->setName(testComponent.get()->getName()); // Same name as the other
    // component
    testComponent3.get()->setId(testComponentId); // Id should differ from
    // testComponent!
    BOOST_REQUIRE_EQUAL(testComponent.get()->getId()== testComponentId, false);

    // Overwrite in table
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Control_Bank, testComponent3), false);

    // Check that the object has not been overwritten
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<ControlBank>(reactor.getAssemblyByName(Control_Bank, testComponentName))), true);
    BOOST_REQUIRE_EQUAL(testComponent3.get()->operator==(*std::dynamic_pointer_cast<ControlBank>(reactor.getAssemblyByName(Control_Bank, testComponentName))), false);

    // Test to remove components from the reactor
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Control_Bank, nullString), false);
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Control_Bank, ""), false);
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Control_Bank, "!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"), false);

    // Remove the first component
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Control_Bank, testComponent.get()->getName()), true);

    // Check that it does not exist in the location or getting the name
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Control_Bank, rowLoc1, colLoc1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Control_Bank, testComponent.get()->getName()).get()== NULL, true);
    //Check size
    BOOST_REQUIRE_EQUAL(1, reactor.getNumberOfAssemblies(Control_Bank));

    // It can now be overridden!
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, testComponent2.get()->getName(), rowLoc1, colLoc1), true);

    //Show that the component's names can NOT overwrite each others locations
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Control_Bank, testComponent), true);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Control_Bank, testComponent.get()->getName(), rowLoc1, colLoc1), false);

    //Check the size, the respective locations
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), reactor.getAssemblyByLocation(Control_Bank, rowLoc1, colLoc1).get()->getName());
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), reactor.getAssemblyByLocation(Control_Bank, rowLoc2, colLoc2).get()->getName());
    BOOST_REQUIRE_EQUAL(2, reactor.getNumberOfAssemblies(Control_Bank));

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkInCoreInstrument) {

    // begin-user-code

    // Local Declarations
    int reactorSize = 17;
    PressurizedWaterReactor reactor(reactorSize);
    std::shared_ptr<IncoreInstrument> testComponent( new IncoreInstrument()), testComponent2(new IncoreInstrument()), testComponent3(new IncoreInstrument());
    std::shared_ptr<IncoreInstrument> nullComponent;
    std::string testComponentName = "Bob";
    std::string testComponentName2 = "Bill!";
    int rowLoc1 = 5, colLoc1 = 5;
    int rowLoc2 = 6, colLoc2 = 6;
    int testComponentId = 1000001;
    std::string nullString;


    // Check the default values of the Component under test

    // No assemblies should be added by default. Therefore every
    // location is bad
    for (int i = 0; i < reactorSize; i++) {
        for (int j = 0; j < reactorSize; j++) {
            BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Incore_Instrument,  i, j).get() == NULL, true);
        }
    }

    //Check the names, should be empty!
    BOOST_REQUIRE_EQUAL(0, reactor.getAssemblyNames(Incore_Instrument).size());

    // Try to get by name - valid string, empty string, and null
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Incore_Instrument, "validNameThatDoesNotExistInThere152423").get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Incore_Instrument, "").get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Incore_Instrument, nullString).get()== NULL, true);

    // Set the name
    testComponent.get()->setName(testComponentName);

    // Add to the reactor
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Incore_Instrument, testComponent), true);

    // See that no location is set
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Incore_Instrument,  rowLoc1, colLoc1).get()== NULL, true);
    // Check locations to be within bounds
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Incore_Instrument,  -1, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Incore_Instrument,  1, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Incore_Instrument,  reactorSize + 25, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Incore_Instrument,  reactorSize - 1, reactorSize + 25).get()== NULL, true);

    // Set the valid location:
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, testComponentName, rowLoc1, colLoc1), true);

    // Try to break location setter
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, nullString, rowLoc1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, testComponentName, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, testComponentName, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, nullString, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, nullString, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, nullString, -1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, testComponentName, rowLoc1, reactorSize + 25), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, testComponentName, reactorSize + 25, colLoc1), false);

    // The above erroneous settings does not change the original location of
    // the first, valid set
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<IncoreInstrument>(reactor.getAssemblyByName(Incore_Instrument, testComponentName))), true);

    // Check invalid overwrite of location:
    testComponent2.get()->setName(testComponentName2);

    // Add reactor, overwrite the previous testComponent's location
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, testComponent2.get()->getName(), rowLoc1, colLoc1), false);

    // Check that it is the first, but not second
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<IncoreInstrument>(reactor.getAssemblyByName(Incore_Instrument, testComponentName))), true);

    // Add it in there
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Incore_Instrument, testComponent2), true);

    // Show that you can have at least 2 components in there
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, testComponent2.get()->getName(), rowLoc2, colLoc2), true);

    // Check values - see the components are different and they reside in
    // the table correctly
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<IncoreInstrument>(reactor.getAssemblyByName(Incore_Instrument, testComponentName))), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*std::dynamic_pointer_cast<IncoreInstrument>(reactor.getAssemblyByName(Incore_Instrument, testComponentName2))), true);

    // Check the locations
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<IncoreInstrument>(reactor.getAssemblyByLocation(Incore_Instrument,  rowLoc1, colLoc1))), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*std::dynamic_pointer_cast<IncoreInstrument>(reactor.getAssemblyByLocation(Incore_Instrument,  rowLoc2, colLoc2))), true);

    //Check the names, should contain 2!
    BOOST_REQUIRE_EQUAL(2, reactor.getAssemblyNames(Incore_Instrument).size());
    BOOST_REQUIRE_EQUAL(testComponentName, reactor.getAssemblyNames(Incore_Instrument).at(1));
    BOOST_REQUIRE_EQUAL(testComponentName2, reactor.getAssemblyNames(Incore_Instrument).at(0));

    // Check operation for null
    reactor.addAssembly(Incore_Instrument, nullComponent);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Incore_Instrument, nullString).get()== NULL, true); // Make sure null does
    // not work!

    // Finally, demonstrate what happens when a component of the same name
    // is added, it should not overwrite the previous item in the table!
    testComponent3.get()->setName(testComponent.get()->getName()); // Same name as the other
    // component
    testComponent3.get()->setId(testComponentId); // Id should differ from
    // testComponent!
    BOOST_REQUIRE_EQUAL(testComponent.get()->getId()== testComponentId, false);

    // Overwrite in table
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Incore_Instrument, testComponent3), false);

    // Check that the object has not been overwritten
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<IncoreInstrument>(reactor.getAssemblyByName(Incore_Instrument, testComponentName))), true);
    BOOST_REQUIRE_EQUAL(testComponent3.get()->operator==(*std::dynamic_pointer_cast<IncoreInstrument>(reactor.getAssemblyByName(Incore_Instrument, testComponentName))), false);

    // Test to remove components from the reactor
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Incore_Instrument, nullString), false);
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Incore_Instrument, ""), false);
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Incore_Instrument, "!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"), false);

    // Remove the first component
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(Incore_Instrument, testComponent.get()->getName()), true);

    // Check that it does not exist in the location or getting the name
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(Incore_Instrument,  rowLoc1, colLoc1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(Incore_Instrument, testComponent.get()->getName()).get()== NULL, true);
    //Check size
    BOOST_REQUIRE_EQUAL(1, reactor.getNumberOfAssemblies(Incore_Instrument));

    // It can now be overridden!
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, testComponent2.get()->getName(), rowLoc1, colLoc1), true);

    //Show that the component's names can NOT overwrite each others locations
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(Incore_Instrument, testComponent), true);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(Incore_Instrument, testComponent.get()->getName(), rowLoc1, colLoc1), false);

    //Check the size, the respective locations
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), reactor.getAssemblyByLocation(Incore_Instrument,  rowLoc1, colLoc1).get()->getName());
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), reactor.getAssemblyByLocation(Incore_Instrument,  rowLoc2, colLoc2).get()->getName());
    BOOST_REQUIRE_EQUAL(2, reactor.getNumberOfAssemblies(Incore_Instrument));

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkRodClusterAssembly) {

    // begin-user-code

    // Local Declarations
    int reactorSize = 17;
    PressurizedWaterReactor reactor(reactorSize);
    std::shared_ptr<RodClusterAssembly> testComponent( new RodClusterAssembly(5)), testComponent2(new RodClusterAssembly(5)), testComponent3(new RodClusterAssembly(5));
    std::shared_ptr<RodClusterAssembly> nullComponent;
    std::string testComponentName = "Bob";
    std::string testComponentName2 = "Bill!";
    int rowLoc1 = 5, colLoc1 = 5;
    int rowLoc2 = 6, colLoc2 = 6;
    int testComponentId = 1000001;
    std::string nullString;


    // Check the default values of the Component under test

    // No assemblies should be added by default. Therefore every
    // location is bad
    for (int i = 0; i < reactorSize; i++) {
        for (int j = 0; j < reactorSize; j++) {
            BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(RodCluster, i, j).get() == NULL, true);
        }
    }

    //Check the names, should be empty!
    BOOST_REQUIRE_EQUAL(0, reactor.getAssemblyNames(RodCluster).size());

    // Try to get by name - valid string, empty string, and null
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(RodCluster, "validNameThatDoesNotExistInThere152423").get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(RodCluster, "").get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(RodCluster, nullString).get()== NULL, true);

    // Set the name
    testComponent.get()->setName(testComponentName);

    // Add to the reactor
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(RodCluster, testComponent), true);

    // See that no location is set
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(RodCluster, rowLoc1, colLoc1).get()== NULL, true);
    // Check locations to be within bounds
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(RodCluster, -1, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(RodCluster, 1, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(RodCluster, reactorSize + 25, reactorSize - 1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(RodCluster, reactorSize - 1, reactorSize + 25).get()== NULL, true);

    // Set the valid location:
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, testComponentName, rowLoc1, colLoc1), true);

    // Try to break location setter
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, nullString, rowLoc1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, testComponentName, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, testComponentName, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, nullString, -1, colLoc1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, nullString, rowLoc1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, nullString, -1, -1), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, testComponentName, rowLoc1, reactorSize + 25), false);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, testComponentName, reactorSize + 25, colLoc1), false);

    // The above erroneous settings does not change the original location of
    // the first, valid set
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<RodClusterAssembly>(reactor.getAssemblyByName(RodCluster, testComponentName))), true);

    // Check invalid overwrite of location:
    testComponent2.get()->setName(testComponentName2);

    // Add reactor, overwrite the previous testComponent's location
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, testComponent2.get()->getName(), rowLoc1, colLoc1), false);

    // Check that it is the first, but not second
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<RodClusterAssembly>(reactor.getAssemblyByName(RodCluster, testComponentName))), true);

    // Add it in there
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(RodCluster, testComponent2), true);

    // Show that you can have at least 2 components in there
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, testComponent2.get()->getName(), rowLoc2, colLoc2), true);

    // Check values - see the components are different and they reside in
    // the table correctly
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<RodClusterAssembly>(reactor.getAssemblyByName(RodCluster, testComponentName))), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*std::dynamic_pointer_cast<RodClusterAssembly>(reactor.getAssemblyByName(RodCluster, testComponentName2))), true);

    // Check the locations
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<RodClusterAssembly>(reactor.getAssemblyByLocation(RodCluster, rowLoc1, colLoc1))), true);
    BOOST_REQUIRE_EQUAL(testComponent2.get()->operator==(*std::dynamic_pointer_cast<RodClusterAssembly>(reactor.getAssemblyByLocation(RodCluster, rowLoc2, colLoc2))), true);

    //Check the names, should contain 2!
    BOOST_REQUIRE_EQUAL(2, reactor.getAssemblyNames(RodCluster).size());
    BOOST_REQUIRE_EQUAL(testComponentName, reactor.getAssemblyNames(RodCluster).at(1));
    BOOST_REQUIRE_EQUAL(testComponentName2, reactor.getAssemblyNames(RodCluster).at(0));

    // Check operation for null
    reactor.addAssembly(RodCluster, nullComponent);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(RodCluster, nullString).get()== NULL, true); // Make sure null does
    // not work!

    // Finally, demonstrate what happens when a component of the same name
    // is added, it should not overwrite the previous item in the table!
    testComponent3.get()->setName(testComponent.get()->getName()); // Same name as the other
    // component
    testComponent3.get()->setId(testComponentId); // Id should differ from
    // testComponent!
    BOOST_REQUIRE_EQUAL(testComponent.get()->getId()== testComponentId, false);

    // Overwrite in table
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(RodCluster, testComponent3), false);

    // Check that the object has not been overwritten
    BOOST_REQUIRE_EQUAL(testComponent.get()->operator==(*std::dynamic_pointer_cast<RodClusterAssembly>(reactor.getAssemblyByName(RodCluster, testComponentName))), true);
    BOOST_REQUIRE_EQUAL(testComponent3.get()->operator==(*std::dynamic_pointer_cast<RodClusterAssembly>(reactor.getAssemblyByName(RodCluster, testComponentName))), false);

    // Test to remove components from the reactor
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(RodCluster, nullString), false);
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(RodCluster, ""), false);
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(RodCluster, "!--+ANamETHaTDoESNOTEXIST19674376393<><(@#*)%^"), false);

    // Remove the first component
    BOOST_REQUIRE_EQUAL(reactor.removeAssembly(RodCluster, testComponent.get()->getName()), true);

    // Check that it does not exist in the location or getting the name
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByLocation(RodCluster, rowLoc1, colLoc1).get()== NULL, true);
    BOOST_REQUIRE_EQUAL(reactor.getAssemblyByName(RodCluster, testComponent.get()->getName()).get()== NULL, true);
    //Check size
    BOOST_REQUIRE_EQUAL(1, reactor.getNumberOfAssemblies(RodCluster));

    // It can now be overridden!
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, testComponent2.get()->getName(), rowLoc1, colLoc1), true);

    //Show that the component's names can NOT overwrite each others locations
    BOOST_REQUIRE_EQUAL(reactor.addAssembly(RodCluster, testComponent), true);
    BOOST_REQUIRE_EQUAL(reactor.setAssemblyLocation(RodCluster, testComponent.get()->getName(), rowLoc1, colLoc1), false);

    //Check the size, the respective locations
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), reactor.getAssemblyByLocation(RodCluster, rowLoc1, colLoc1).get()->getName());
    BOOST_REQUIRE_EQUAL(testComponent2.get()->getName(), reactor.getAssemblyByLocation(RodCluster, rowLoc2, colLoc2).get()->getName());
    BOOST_REQUIRE_EQUAL(2, reactor.getNumberOfAssemblies(RodCluster));

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkCompositeImplementations) {

    // begin-user-code

    //Local Declarations
    int reactorSize = 17;
    std::vector<std::string> compNames;
    std::vector< std::shared_ptr<Component> > components;
    int numberOfDefaultComponents = 0;

    //Defaults for ControlBank
    std::string compName = "Control Banks";
    std::string compDescription = "A Composite that contains many ControlBank Components.";
    int compId = 1;

    //Setup component for comparison
    std::shared_ptr<LWRComposite> controlComposite ( new LWRComposite());
    controlComposite.get()->setName(compName);
    controlComposite.get()->setId(compId);
    controlComposite.get()->setDescription(compDescription);
    //Add to arraylist
    compNames.push_back(controlComposite.get()->getName());
    components.push_back(controlComposite);

    //Defaults for FuelAssembly
    compName = "Fuel Assemblies";
    compDescription = "A Composite that contains many FuelAssembly Components.";
    compId = 2;

    //Setup component for comparison
    std::shared_ptr<LWRComposite> fuelComposite ( new LWRComposite());
    fuelComposite.get()->setName(compName);
    fuelComposite.get()->setId(compId);
    fuelComposite.get()->setDescription(compDescription);
    //Add to arraylist
    compNames.push_back(fuelComposite.get()->getName());
    components.push_back(fuelComposite);

    //Defaults for IncoreInstruments
    compName = "Incore Instruments";
    compDescription = "A Composite that contains many IncoreInstrument Components.";
    compId = 3;

    //Setup component for comparison
    std::shared_ptr<LWRComposite> coreComposite (new LWRComposite());
    coreComposite.get()->setName(compName);
    coreComposite.get()->setId(compId);
    coreComposite.get()->setDescription(compDescription);
    //Add to arraylist
    compNames.push_back(coreComposite.get()->getName());
    components.push_back(coreComposite);

    //Defaults for RCA
    compName = "Rod Cluster Assemblies";
    compDescription = "A Composite that contains many RodClusterAssembly Components.";
    compId = 4;

    //Setup component for comparison
    std::shared_ptr<LWRComposite> rodComposite (new LWRComposite());
    rodComposite.get()->setName(compName);
    rodComposite.get()->setId(compId);
    rodComposite.get()->setDescription(compDescription);
    //Add to arraylist
    compNames.push_back(rodComposite.get()->getName());
    components.push_back(rodComposite);

    //Setup the default number of components
    numberOfDefaultComponents = components.size();

    //Check the default Composite size and attributes on PWRAssembly
    PressurizedWaterReactor reactor(reactorSize);

    //Has a size of numberOfDefaultComponents
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());
    //It is equal to the default rodComposite for many of the composite getters
    std::shared_ptr<LWRComposite> castedComposite1(new LWRComposite(*(dynamic_cast<LWRComposite *> (reactor.getComponent(1).get()))));
    std::shared_ptr<LWRComposite> castedComposite2(new LWRComposite(*(dynamic_cast<LWRComposite *> (reactor.getComponent(controlComposite.get()->getName()).get()))));
    std::shared_ptr<LWRComposite> castedComposite3(new LWRComposite(*(dynamic_cast<LWRComposite *> (reactor.getComponent(2).get()))));
    std::shared_ptr<LWRComposite> castedComposite4(new LWRComposite(*(dynamic_cast<LWRComposite *> (reactor.getComponent(fuelComposite.get()->getName()).get()))));
    std::shared_ptr<LWRComposite> castedComposite5(new LWRComposite(*(dynamic_cast<LWRComposite *> (reactor.getComponent(3).get()))));
    std::shared_ptr<LWRComposite> castedComposite6(new LWRComposite(*(dynamic_cast<LWRComposite *> (reactor.getComponent(coreComposite.get()->getName()).get()))));
    std::shared_ptr<LWRComposite> castedComposite7(new LWRComposite(*(dynamic_cast<LWRComposite *> (reactor.getComponent(4).get()))));
    std::shared_ptr<LWRComposite> castedComposite8(new LWRComposite(*(dynamic_cast<LWRComposite *> (reactor.getComponent(rodComposite.get()->getName()).get()))));
    BOOST_REQUIRE_EQUAL(controlComposite.get()->operator==(*castedComposite1.get()), true);
    BOOST_REQUIRE_EQUAL(controlComposite.get()->operator==(*castedComposite2.get()), true);
    BOOST_REQUIRE_EQUAL(fuelComposite.get()->operator==(*castedComposite3.get()), true);
    BOOST_REQUIRE_EQUAL(fuelComposite.get()->operator==(*castedComposite4.get()), true);
    BOOST_REQUIRE_EQUAL(coreComposite.get()->operator==(*castedComposite5.get()), true);
    BOOST_REQUIRE_EQUAL(coreComposite.get()->operator==(*castedComposite6.get()), true);
    BOOST_REQUIRE_EQUAL(rodComposite.get()->operator==(*castedComposite7.get()), true);
    BOOST_REQUIRE_EQUAL(rodComposite.get()->operator==(*castedComposite8.get()), true);
    BOOST_REQUIRE_EQUAL(compNames.size(), reactor.getComponentNames().size());
    //Do name comparisons
    for(int i = 0; i < compNames.size(); i++) {
        BOOST_REQUIRE_EQUAL(reactor.getComponentNames().at(i), compNames.at(i));
    }

    BOOST_REQUIRE_EQUAL(components.size(), reactor.getComponents().size());
    for(int i = 0; i < components.size(); i++) {

        std::shared_ptr<LWRComponent> castedComponent(new LWRComponent(*(dynamic_cast<LWRComponent *> (components.at(i).get()))));
        std::shared_ptr<LWRComponent> castedComponentEqual(new LWRComponent(*(dynamic_cast<LWRComponent *> (reactor.getComponents().at(i).get()))));

        BOOST_REQUIRE_EQUAL(castedComponent.get()->operator==(*castedComponentEqual.get()), true);
    }

    //These operations will show that these will not work for this class

    //Check addComponent
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());
    reactor.addComponent(std::shared_ptr<LWRComposite> (new LWRComposite()));
    //No size change!
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());

    //Check removeComponent - id
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());
    reactor.removeComponent(1);
    //No size change!
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());

    //Check remove component - name
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());
    reactor.removeComponent(components.at(0).get()->getName()); //Try to remove the first off the list
    //No size change!
    BOOST_REQUIRE_EQUAL(numberOfDefaultComponents, reactor.getNumberOfComponents());

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkEquality) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    std::shared_ptr<Ring> ring (new Ring());

    //Setup Values
    std::shared_ptr<FuelAssembly> assembly (new FuelAssembly("FUELS!", size));
    std::shared_ptr<ControlBank> bank (new ControlBank("BANKS!", 2, 5));
    std::shared_ptr<RodClusterAssembly> rca ( new RodClusterAssembly("RODS!", size));
    std::shared_ptr<IncoreInstrument> instrument ( new IncoreInstrument("Instruments!", ring));

    //Setup root object
    PressurizedWaterReactor object(size);
    object.addAssembly(Control_Bank, bank);
    object.addAssembly(Fuel, assembly);
    object.addAssembly(Incore_Instrument, instrument);
    object.addAssembly(RodCluster, rca);
    object.setAssemblyLocation(Control_Bank, bank.get()->getName(), 0, 0);
    object.setAssemblyLocation(Fuel, assembly.get()->getName(), 0, 0);
    object.setAssemblyLocation(RodCluster, rca.get()->getName(), 0, 0);
    object.setAssemblyLocation(Incore_Instrument, instrument.get()->getName(), 0, 0);

    //Setup equalObject equal to object
    PressurizedWaterReactor equalObject(size);
    equalObject.addAssembly(Control_Bank, bank);
    equalObject.addAssembly(Fuel, assembly);
    equalObject.addAssembly(Incore_Instrument, instrument);
    equalObject.addAssembly(RodCluster, rca);
    equalObject.setAssemblyLocation(Control_Bank, bank.get()->getName(), 0, 0);
    equalObject.setAssemblyLocation(Fuel, assembly.get()->getName(), 0, 0);
    equalObject.setAssemblyLocation(RodCluster, rca.get()->getName(), 0, 0);
    equalObject.setAssemblyLocation(Incore_Instrument, instrument.get()->getName(), 0, 0);

    //Setup transitiveObject equal to object
    PressurizedWaterReactor transitiveObject(size);
    transitiveObject.addAssembly(Control_Bank, bank);
    transitiveObject.addAssembly(Fuel, assembly);
    transitiveObject.addAssembly(Incore_Instrument, instrument);
    transitiveObject.addAssembly(RodCluster, rca);
    transitiveObject.setAssemblyLocation(Control_Bank, bank.get()->getName(), 0, 0);
    transitiveObject.setAssemblyLocation(Fuel, assembly.get()->getName(), 0, 0);
    transitiveObject.setAssemblyLocation(RodCluster, rca.get()->getName(), 0, 0);
    transitiveObject.setAssemblyLocation(Incore_Instrument, instrument.get()->getName(), 0, 0);

    // Set its data, not equal to object
    PressurizedWaterReactor unEqualObject(size);
    unEqualObject.addAssembly(Control_Bank, bank);
    unEqualObject.addAssembly(Fuel, assembly);
    unEqualObject.addAssembly(Incore_Instrument, instrument);
    unEqualObject.addAssembly(RodCluster, rca);
    unEqualObject.setAssemblyLocation(Control_Bank, bank.get()->getName(), 0, 0);
    unEqualObject.setAssemblyLocation(Fuel, assembly.get()->getName(), 0, 0);
    unEqualObject.setAssemblyLocation(RodCluster, rca.get()->getName(), 0, 0);
    unEqualObject.setAssemblyLocation(Incore_Instrument, instrument.get()->getName(), 0, 1); //Only difference here

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
    int size = 5;
    std::shared_ptr<Ring> ring (new Ring());

    //Setup Values
    std::shared_ptr<FuelAssembly> assembly (new FuelAssembly("FUELS!", size));
    std::shared_ptr<ControlBank> bank (new ControlBank("BANKS!", 2, 5));
    std::shared_ptr<RodClusterAssembly> rca ( new RodClusterAssembly("RODS!", size));
    std::shared_ptr<IncoreInstrument> instrument ( new IncoreInstrument("Instruments!", ring));

    //Setup root object
    PressurizedWaterReactor object(size);
    object.addAssembly(Control_Bank, bank);
    object.addAssembly(Fuel, assembly);
    object.addAssembly(Incore_Instrument, instrument);
    object.addAssembly(RodCluster, rca);
    object.setAssemblyLocation(Control_Bank, bank.get()->getName(), 0, 0);
    object.setAssemblyLocation(Fuel, assembly.get()->getName(), 0, 0);
    object.setAssemblyLocation(RodCluster, rca.get()->getName(), 0, 0);
    object.setAssemblyLocation(Incore_Instrument, instrument.get()->getName(), 0, 0);

    //Run the copy routine
    PressurizedWaterReactor copyObject(object);

    //Check contents
    BOOST_REQUIRE_EQUAL(object==(copyObject), true);

    //Run the clone routine
    std::shared_ptr<Identifiable> clonedObject = object.clone();

    //Dynamic cast up
    std::shared_ptr<PressurizedWaterReactor> castedObject (new PressurizedWaterReactor(*(dynamic_cast<PressurizedWaterReactor *> (clonedObject.get()))));

    //Check contents - dereference
    BOOST_REQUIRE_EQUAL(object==*castedObject.get(), true);

    return;

    // end-user-code

}
BOOST_AUTO_TEST_CASE(checkHDF5Writeables) {

    // begin-user-code

    //Local Declarations
    int size = 5;
    PressurizedWaterReactor component(size);
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    double fuelAssemblyPitch = 4;
    std::string sourceInfo = "ASDASDASD";
    std::string timeUnits = "UNITS OF AWESOME";
    double time = 1.0;

    //Setup Assemblies, instruments, and controlBank locations

    //Size of the assemblies
    int assemblySize = 3;

    //Locations
    std::shared_ptr<GridLocation> loc1 ( new GridLocation(0, 0));
    std::shared_ptr<GridLocation> loc2 ( new GridLocation(2, 0));
    std::shared_ptr<GridLocation> loc3 ( new GridLocation(0, 3));
    std::shared_ptr<GridLocation> loc4 ( new GridLocation(1, 1));
    std::shared_ptr<GridLocation> loc5 ( new GridLocation(3, 1));
    std::shared_ptr<GridLocation> loc6 ( new GridLocation(1, 3));
    std::shared_ptr<GridLocation> loc7 ( new GridLocation(3, 0));
    std::shared_ptr<GridLocation> loc8 ( new GridLocation(2, 3));
    std::shared_ptr<GridLocation> loc9 ( new GridLocation(3, 3));

    //Control Banks
    std::shared_ptr<ControlBank> bank1 (new ControlBank("ControlBank1", 2, 4));
    std::shared_ptr<ControlBank> bank2 (new ControlBank("ControlBank2", 3, 6));

    std::shared_ptr<Ring> thimble1 (new Ring("I am a thimble!"));
    std::shared_ptr<Ring> thimble2 (new Ring("I am a thimble, too!"));

    //IncoreInstruments
    std::shared_ptr<IncoreInstrument> instruments1 ( new IncoreInstrument("Instrument1", thimble1));
    std::shared_ptr<IncoreInstrument> instruments2 ( new IncoreInstrument("Instrument2", thimble2));

    //Fuel Assembly
    std::shared_ptr<FuelAssembly> fuel1 ( new FuelAssembly("Fuel Assembly 1", assemblySize));
    std::shared_ptr<FuelAssembly> fuel2 ( new FuelAssembly("Fuel Assembly 2", assemblySize));

    //RodClusterAssembly
    std::shared_ptr<RodClusterAssembly> rod1 ( new RodClusterAssembly("RCA 1", assemblySize));
    std::shared_ptr<RodClusterAssembly> rod2 ( new RodClusterAssembly("RCA 2", assemblySize));

    //Setup Duplicate Grids
    std::shared_ptr<LWRGridManager> bankGridManager ( new LWRGridManager(size));
    std::shared_ptr<LWRGridManager> coreGridManager ( new LWRGridManager(size));
    std::shared_ptr<LWRGridManager> fuelGridManager ( new LWRGridManager(size));
    std::shared_ptr<LWRGridManager> rodGridManager ( new LWRGridManager(size));

    //Setup names
    bankGridManager.get()->setName("Control Bank Grid");
    coreGridManager.get()->setName("Incore Instrument Grid");
    fuelGridManager.get()->setName("Fuel Assembly Grid");
    rodGridManager.get()->setName("Rod Cluster Assembly Grid");

    //Add objects to the grid for later comparison
    //ControlBank
    bankGridManager.get()->addComponent(bank1, loc1);
    bankGridManager.get()->addComponent(bank2, loc2);
    //Incore Instruments
    coreGridManager.get()->addComponent(instruments1, loc3);
    coreGridManager.get()->addComponent(instruments2, loc4);
    //Fuel Assemblies
    fuelGridManager.get()->addComponent(fuel1, loc5);
    fuelGridManager.get()->addComponent(fuel2, loc6);
    //RodClusterAssemblies
    rodGridManager.get()->addComponent(rod1, loc7);
    rodGridManager.get()->addComponent(rod2, loc8);

    //Setup LWRComposite clones
    std::shared_ptr<LWRComposite> bankComposite (new LWRComposite());
    std::shared_ptr<LWRComposite> coreComposite (new LWRComposite());
    std::shared_ptr<LWRComposite> fuelComposite (new LWRComposite());
    std::shared_ptr<LWRComposite> rodComposite (new LWRComposite());

    //Setup names, descriptions, ids and add pieces
    //Control Bank
    bankComposite.get()->setName("Control Banks");
    bankComposite.get()->setDescription("A Composite that contains many ControlBank Components.");
    bankComposite.get()->setId(1);
    bankComposite.get()->addComponent(bank1);
    bankComposite.get()->addComponent(bank2);
    //Incore Instrument
    coreComposite.get()->setName("Incore Instruments");
    coreComposite.get()->setDescription("A Composite that contains many IncoreInstrument Components.");
    coreComposite.get()->setId(3);
    coreComposite.get()->addComponent(instruments1);
    coreComposite.get()->addComponent(instruments2);
    //Fuel Composite
    fuelComposite.get()->setName("Fuel Assemblies");
    fuelComposite.get()->setDescription("A Composite that contains many FuelAssembly Components.");
    fuelComposite.get()->setId(2);
    fuelComposite.get()->addComponent(fuel1);
    fuelComposite.get()->addComponent(fuel2);
    //Rod Cluster Assemblies
    rodComposite.get()->setName("Rod Cluster Assemblies");
    rodComposite.get()->setDescription("A Composite that contains many RodClusterAssembly Components.");
    rodComposite.get()->setId(4);
    rodComposite.get()->addComponent(rod1);
    rodComposite.get()->addComponent(rod2);

    //Setup Rows and Columns
    std::vector<std::string>rowLabels;
    std::vector<std::string> columnLabels;

    //Setup row labels
    rowLabels.push_back("A");
    rowLabels.push_back("B");
    rowLabels.push_back("C");
    rowLabels.push_back("D");
    rowLabels.push_back("E");

    //Setup col labels
    rowLabels.push_back("1");
    rowLabels.push_back("2");
    rowLabels.push_back("3");
    rowLabels.push_back("4");
    rowLabels.push_back("OVER 9000!");

    std::shared_ptr<GridLabelProvider> provider (new GridLabelProvider(size));
    provider.get()->setRowLabels(rowLabels);
    provider.get()->setColumnLabels(columnLabels);
    provider.get()->setName("Grid Labels");

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Setup component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);
    component.setGridLabelProvider(provider);
    component.setFuelAssemblyPitch(fuelAssemblyPitch);
    component.setSourceInfo(sourceInfo);
    component.setTime(time);
    component.setTimeUnits(timeUnits);

    //Add pieces to component
    //Control Bank
    component.addAssembly(Control_Bank, bank1);
    component.addAssembly(Control_Bank, bank2);
    //IncoreInstrument
    component.addAssembly(Incore_Instrument, instruments1);
    component.addAssembly(Incore_Instrument, instruments2);
    //Fuel Assemblies
    component.addAssembly(Fuel, fuel1);
    component.addAssembly(Fuel, fuel2);
    //RodClusterAssemblies
    component.addAssembly(RodCluster, rod1);
    component.addAssembly(RodCluster, rod2);

    //Setup Positions
    //Control Bank
    component.setAssemblyLocation(Control_Bank, bank1.get()->getName(), loc1.get()->getRow(), loc1.get()->getColumn());
    component.setAssemblyLocation(Control_Bank, bank2.get()->getName(), loc2.get()->getRow(), loc2.get()->getColumn());
    //IncoreInstrument
    component.setAssemblyLocation(Incore_Instrument, instruments1.get()->getName(), loc3.get()->getRow(), loc3.get()->getColumn());
    component.setAssemblyLocation(Incore_Instrument, instruments2.get()->getName(), loc4.get()->getRow(), loc4.get()->getColumn());
    //Fuel Assemblies
    component.setAssemblyLocation(Fuel, fuel1.get()->getName(), loc5.get()->getRow(), loc5.get()->getColumn());
    component.setAssemblyLocation(Fuel, fuel2.get()->getName(), loc6.get()->getRow(), loc6.get()->getColumn());
    //RodClusterAssemblies
    component.setAssemblyLocation(RodCluster, rod1.get()->getName(), loc7.get()->getRow(), loc7.get()->getColumn());
    component.setAssemblyLocation(RodCluster, rod2.get()->getName(), loc8.get()->getRow(), loc8.get()->getColumn());

    //exception hit
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

    //Setup the HDF5 File
    std::shared_ptr<H5::H5File> h5File =  HdfFileFactory::createH5File(dataFile);

    //Check to see if it has any children
    BOOST_REQUIRE_EQUAL(component.getWriteableChildren().size(), 9);

    //Get the first component and its sub components for comparisons
    std::shared_ptr<LWRComposite> writeableComposite1 = std::dynamic_pointer_cast<LWRComposite > (component.getWriteableChildren()[0]);
    std::shared_ptr<LWRComposite> writeableComposite2 = std::dynamic_pointer_cast<LWRComposite > (component.getWriteableChildren()[1]);
    std::shared_ptr<LWRComposite> writeableComposite3 = std::dynamic_pointer_cast<LWRComposite > (component.getWriteableChildren()[2]);
    std::shared_ptr<LWRComposite> writeableComposite4 = std::dynamic_pointer_cast<LWRComposite > (component.getWriteableChildren()[3]);
    std::shared_ptr<GridLabelProvider> writeableGridLabels = std::dynamic_pointer_cast<GridLabelProvider> (component.getWriteableChildren()[4]);
    std::shared_ptr<LWRGridManager> writeableBankGridManager = std::dynamic_pointer_cast<LWRGridManager> (component.getWriteableChildren()[5]);
    std::shared_ptr<LWRGridManager> writeableFuelGridManager = std::dynamic_pointer_cast<LWRGridManager> (component.getWriteableChildren()[6]);
    std::shared_ptr<LWRGridManager> writeableCoreGridManager = std::dynamic_pointer_cast<LWRGridManager> (component.getWriteableChildren()[7]);
    std::shared_ptr<LWRGridManager> writeableRodGridManager = std::dynamic_pointer_cast<LWRGridManager> (component.getWriteableChildren()[8]);


    //Check composites and components
    BOOST_REQUIRE_EQUAL(writeableComposite1.get()->operator ==(*bankComposite.get()), true);
    BOOST_REQUIRE_EQUAL(writeableComposite2.get()->operator ==(*fuelComposite.get()), true);
    BOOST_REQUIRE_EQUAL(writeableComposite3.get()->operator ==(*coreComposite.get()), true);
    BOOST_REQUIRE_EQUAL(writeableComposite4.get()->operator ==(*rodComposite.get()), true);
    BOOST_REQUIRE_EQUAL(writeableGridLabels.get()->operator ==(*provider.get()), true);
    BOOST_REQUIRE_EQUAL(writeableBankGridManager.get()->operator ==(*bankGridManager.get()), true);
    BOOST_REQUIRE_EQUAL(writeableFuelGridManager.get()->operator ==(*fuelGridManager.get()), true);
    BOOST_REQUIRE_EQUAL(writeableCoreGridManager.get()->operator ==(*coreGridManager.get()), true);
    BOOST_REQUIRE_EQUAL(writeableRodGridManager.get()->operator ==(*rodGridManager.get()), true);


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
        BOOST_REQUIRE_EQUAL(HdfReaderFactory::readDoubleAttribute(h5Group, "fuelAssemblyPitch"), fuelAssemblyPitch);

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
    PressurizedWaterReactor component(size);
    PressurizedWaterReactor newComponent(-1);
    std::string name = "Bob the Builder";
    std::string description = "Can he fix it?";
    int id = 4;
    HDF5LWRTagType tag = component.getHDF5LWRTag();
    double fuelAssemblyPitch = 4;

    //Setup Assemblies, instruments, and controlBank locations

    //Size of the assemblies
    int assemblySize = 3;

    //Locations
    std::shared_ptr<GridLocation> loc1 ( new GridLocation(0, 0));
    std::shared_ptr<GridLocation> loc2 ( new GridLocation(2, 0));
    std::shared_ptr<GridLocation> loc3 ( new GridLocation(0, 3));
    std::shared_ptr<GridLocation> loc4 ( new GridLocation(1, 1));
    std::shared_ptr<GridLocation> loc5 ( new GridLocation(3, 1));
    std::shared_ptr<GridLocation> loc6 ( new GridLocation(1, 3));
    std::shared_ptr<GridLocation> loc7 ( new GridLocation(3, 0));
    std::shared_ptr<GridLocation> loc8 ( new GridLocation(2, 3));
    std::shared_ptr<GridLocation> loc9 ( new GridLocation(3, 3));

    //Control Banks
    std::shared_ptr<ControlBank> bank1 (new ControlBank("ControlBank1", 2, 4));
    std::shared_ptr<ControlBank> bank2 (new ControlBank("ControlBank2", 3, 6));

    std::shared_ptr<Ring> thimble1 (new Ring("I am a thimble!"));
    std::shared_ptr<Ring> thimble2 (new Ring("I am a thimble, too!"));

    //IncoreInstruments
    std::shared_ptr<IncoreInstrument> instruments1 ( new IncoreInstrument("Instrument1", thimble1));
    std::shared_ptr<IncoreInstrument> instruments2 ( new IncoreInstrument("Instrument2", thimble2));

    //Fuel Assembly
    std::shared_ptr<FuelAssembly> fuel1 ( new FuelAssembly("Fuel Assembly 1", assemblySize));
    std::shared_ptr<FuelAssembly> fuel2 ( new FuelAssembly("Fuel Assembly 2", assemblySize));

    //RodClusterAssembly
    std::shared_ptr<RodClusterAssembly> rod1 ( new RodClusterAssembly("RCA 1", assemblySize));
    std::shared_ptr<RodClusterAssembly> rod2 ( new RodClusterAssembly("RCA 2", assemblySize));

    //Setup Duplicate Grids
    std::shared_ptr<LWRGridManager> bankGridManager ( new LWRGridManager(size));
    std::shared_ptr<LWRGridManager> coreGridManager ( new LWRGridManager(size));
    std::shared_ptr<LWRGridManager> fuelGridManager ( new LWRGridManager(size));
    std::shared_ptr<LWRGridManager> rodGridManager ( new LWRGridManager(size));

    //Setup names
    bankGridManager.get()->setName("Control Bank Grid");
    coreGridManager.get()->setName("Incore Instrument Grid");
    fuelGridManager.get()->setName("Fuel Assembly Grid");
    rodGridManager.get()->setName("Rod Cluster Assembly Grid");

    //Add objects to the grid for later comparison
    //ControlBank
    bankGridManager.get()->addComponent(bank1, loc1);
    bankGridManager.get()->addComponent(bank2, loc2);
    //Incore Instruments
    coreGridManager.get()->addComponent(instruments1, loc3);
    coreGridManager.get()->addComponent(instruments2, loc4);
    //Fuel Assemblies
    fuelGridManager.get()->addComponent(fuel1, loc5);
    fuelGridManager.get()->addComponent(fuel2, loc6);
    //RodClusterAssemblies
    rodGridManager.get()->addComponent(rod1, loc7);
    rodGridManager.get()->addComponent(rod2, loc8);

    //Setup LWRComposite clones
    std::shared_ptr<LWRComposite> bankComposite (new LWRComposite());
    std::shared_ptr<LWRComposite> coreComposite (new LWRComposite());
    std::shared_ptr<LWRComposite> fuelComposite (new LWRComposite());
    std::shared_ptr<LWRComposite> rodComposite (new LWRComposite());

    //Setup names, descriptions, ids and add pieces
    //Control Bank
    bankComposite.get()->setName("Control Banks");
    bankComposite.get()->setDescription("A Composite that contains many ControlBank Components.");
    bankComposite.get()->setId(1);
    bankComposite.get()->addComponent(bank1);
    bankComposite.get()->addComponent(bank2);
    //Incore Instrument
    coreComposite.get()->setName("Incore Instruments");
    coreComposite.get()->setDescription("A Composite that contains many IncoreInstrument Components.");
    coreComposite.get()->setId(3);
    coreComposite.get()->addComponent(instruments1);
    coreComposite.get()->addComponent(instruments2);
    //Fuel Composite
    fuelComposite.get()->setName("Fuel Assemblies");
    fuelComposite.get()->setDescription("A Composite that contains many FuelAssembly Components.");
    fuelComposite.get()->setId(2);
    fuelComposite.get()->addComponent(fuel1);
    fuelComposite.get()->addComponent(fuel2);
    //Rod Cluster Assemblies
    rodComposite.get()->setName("Rod Cluster Assemblies");
    rodComposite.get()->setDescription("A Composite that contains many RodClusterAssembly Components.");
    rodComposite.get()->setId(4);
    rodComposite.get()->addComponent(rod1);
    rodComposite.get()->addComponent(rod2);

    //Setup Rows and Columns
    std::vector<std::string>rowLabels;
    std::vector<std::string> columnLabels;

    //Setup row labels
    rowLabels.push_back("A");
    rowLabels.push_back("B");
    rowLabels.push_back("C");
    rowLabels.push_back("D");
    rowLabels.push_back("E");

    //Setup col labels
    rowLabels.push_back("1");
    rowLabels.push_back("2");
    rowLabels.push_back("3");
    rowLabels.push_back("4");
    rowLabels.push_back("OVER 9000!");

    std::shared_ptr<GridLabelProvider> provider (new GridLabelProvider(size));
    provider.get()->setRowLabels(rowLabels);
    provider.get()->setColumnLabels(columnLabels);
    provider.get()->setName("Grid Labels");

    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";

    //Setup component
    component.setName(name);
    component.setId(id);
    component.setDescription(description);
    component.setGridLabelProvider(provider);
    component.setFuelAssemblyPitch(fuelAssemblyPitch);

    //Add pieces to component
    //Control Bank
    component.addAssembly(Control_Bank, bank1);
    component.addAssembly(Control_Bank, bank2);
    //IncoreInstrument
    component.addAssembly(Incore_Instrument, instruments1);
    component.addAssembly(Incore_Instrument, instruments2);
    //Fuel Assemblies
    component.addAssembly(Fuel, fuel1);
    component.addAssembly(Fuel, fuel2);
    //RodClusterAssemblies
    component.addAssembly(RodCluster, rod1);
    component.addAssembly(RodCluster, rod2);

    //Setup Positions
    //Control Bank
    component.setAssemblyLocation(Control_Bank, bank1.get()->getName(), loc1.get()->getRow(), loc1.get()->getColumn());
    component.setAssemblyLocation(Control_Bank, bank2.get()->getName(), loc2.get()->getRow(), loc2.get()->getColumn());
    //IncoreInstrument
    component.setAssemblyLocation(Incore_Instrument, instruments1.get()->getName(), loc3.get()->getRow(), loc3.get()->getColumn());
    component.setAssemblyLocation(Incore_Instrument, instruments2.get()->getName(), loc4.get()->getRow(), loc4.get()->getColumn());
    //Fuel Assemblies
    component.setAssemblyLocation(Fuel, fuel1.get()->getName(), loc5.get()->getRow(), loc5.get()->getColumn());
    component.setAssemblyLocation(Fuel, fuel2.get()->getName(), loc6.get()->getRow(), loc6.get()->getColumn());
    //RodClusterAssemblies
    component.setAssemblyLocation(RodCluster, rod1.get()->getName(), loc7.get()->getRow(), loc7.get()->getColumn());
    component.setAssemblyLocation(RodCluster, rod2.get()->getName(), loc8.get()->getRow(), loc8.get()->getColumn());

    //exception hit
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
        BOOST_REQUIRE_EQUAL(newComponent.readChild(bankComposite), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(fuelComposite), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(coreComposite), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(rodComposite), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(provider), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(bankGridManager), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(fuelGridManager), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(coreGridManager), true);
        BOOST_REQUIRE_EQUAL(newComponent.readChild(rodGridManager), true);

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

BOOST_AUTO_TEST_CASE(checkDataProviderLocations) {

	// begin-user-code

	//Local Declarations

	//Create a PressurizedWaterReactor, add a few pieces and setup their locations.
	int size = 5;
	PressurizedWaterReactor object(size);

	int row1 = 0, col1 = 0, row2 = 2, col2 = 2, row3 =4 , col3 = 3;

	//Setup Values
	std::shared_ptr<FuelAssembly> assembly (new FuelAssembly("FUELS!", size));
	std::shared_ptr<ControlBank> bank (new ControlBank("BANKS!", 2, 5));
	std::shared_ptr<RodClusterAssembly> rca (new RodClusterAssembly("RODS!", size));
	std::shared_ptr<IncoreInstrument> instrument (new IncoreInstrument("Instruments!", std::shared_ptr<Ring>(new Ring())));

	//Setup object
	object.addAssembly(Control_Bank, bank);
	object.addAssembly(Fuel, assembly);
	object.addAssembly(Incore_Instrument, instrument);
	object.addAssembly(RodCluster, rca);

	//Try to get at locations that do not exist yet for empty
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Control_Bank, row1, col1).get() == NULL, true);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Fuel, row1, col1).get() == NULL, true);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(RodCluster, row1, col1).get() == NULL, true);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Incore_Instrument, row1, col1).get() == NULL, true);

	//Setup locations
	object.setAssemblyLocation(Control_Bank, bank.get()->getName(), row1, col1);
	object.setAssemblyLocation(Fuel, assembly.get()->getName(), row1, col1);
	object.setAssemblyLocation(RodCluster, rca.get()->getName(), row1, col1);
	object.setAssemblyLocation(Incore_Instrument, instrument.get()->getName(), row1, col1);

	object.setAssemblyLocation(Control_Bank, bank.get()->getName(), row2, col2);
	object.setAssemblyLocation(Fuel, assembly.get()->getName(), row2, col2);
	object.setAssemblyLocation(RodCluster, rca.get()->getName(), row2, col2);
	object.setAssemblyLocation(Incore_Instrument, instrument.get()->getName(), row2, col2);

	object.setAssemblyLocation(Control_Bank, bank.get()->getName(), row3, col3);
	object.setAssemblyLocation(Fuel, assembly.get()->getName(), row3, col3);
	object.setAssemblyLocation(RodCluster, rca.get()->getName(), row3, col3);
	object.setAssemblyLocation(Incore_Instrument, instrument.get()->getName(), row3, col3);

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
	provider.addData(data3, time2);
	provider.addData(data4, time2);



	//Try to get data by location
	//Try to get at locations that do not exist yet for empty
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Control_Bank, row1, col1).get() == NULL, false);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Fuel, row1, col1).get() == NULL, false);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(RodCluster, row1, col1).get() == NULL, false);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Incore_Instrument, row1, col1).get() == NULL, false);

	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Control_Bank, row2, col2).get() == NULL, false);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Fuel, row2, col2).get() == NULL, false);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(RodCluster, row2, col2).get() == NULL, false);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Incore_Instrument, row2, col2).get() == NULL, false);

	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Control_Bank, row3, col3).get() == NULL, false);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Fuel, row3, col3).get() == NULL, false);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(RodCluster, row3, col3).get() == NULL, false);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Incore_Instrument, row3, col3).get() == NULL, false);

	//Add some data
	object.getAssemblyDataProviderAtLocation(Control_Bank, row1, col1).get()->addData(data1, time1);
	object.getAssemblyDataProviderAtLocation(Control_Bank, row1, col1).get()->addData(data2, time1);
	object.getAssemblyDataProviderAtLocation(Control_Bank, row1, col1).get()->addData(data3, time1);
	object.getAssemblyDataProviderAtLocation(Control_Bank, row1, col1).get()->addData(data3, time2);
	object.getAssemblyDataProviderAtLocation(Control_Bank, row1, col1).get()->addData(data4, time2);

	//Add some data
	object.getAssemblyDataProviderAtLocation(Fuel, row1, col1).get()->addData(data1, time1);
	object.getAssemblyDataProviderAtLocation(Fuel, row1, col1).get()->addData(data2, time1);
	object.getAssemblyDataProviderAtLocation(Fuel, row1, col1).get()->addData(data3, time1);
	object.getAssemblyDataProviderAtLocation(Fuel, row1, col1).get()->addData(data3, time2);
	object.getAssemblyDataProviderAtLocation(Fuel, row1, col1).get()->addData(data4, time2);

	//Add some data
	object.getAssemblyDataProviderAtLocation(RodCluster, row1, col1).get()->addData(data1, time1);
	object.getAssemblyDataProviderAtLocation(RodCluster, row1, col1).get()->addData(data2, time1);
	object.getAssemblyDataProviderAtLocation(RodCluster, row1, col1).get()->addData(data3, time1);
	object.getAssemblyDataProviderAtLocation(RodCluster, row1, col1).get()->addData(data3, time2);
	object.getAssemblyDataProviderAtLocation(RodCluster, row1, col1).get()->addData(data4, time2);

	//Add some data
	object.getAssemblyDataProviderAtLocation(Incore_Instrument, row1, col1).get()->addData(data1, time1);
	object.getAssemblyDataProviderAtLocation(Incore_Instrument, row1, col1).get()->addData(data2, time1);
	object.getAssemblyDataProviderAtLocation(Incore_Instrument, row1, col1).get()->addData(data3, time1);
	object.getAssemblyDataProviderAtLocation(Incore_Instrument, row1, col1).get()->addData(data3, time2);
	object.getAssemblyDataProviderAtLocation(Incore_Instrument, row1, col1).get()->addData(data4, time2);

	//Do comparisons
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Control_Bank, row1, col1).get()->operator==(provider), true);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Fuel, row1, col1).get()->operator==(provider), true);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(RodCluster, row1, col1).get()->operator==(provider), true);
	BOOST_REQUIRE_EQUAL(object.getAssemblyDataProviderAtLocation(Incore_Instrument, row1, col1).get()->operator==(provider), true);


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
