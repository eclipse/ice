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
#define BOOST_TEST_MODULE LWRComponentWriterTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <LWRComponentWriter.h>
#include <pwr/ControlBank.h>
#include <pwr/FuelAssembly.h>
#include <pwr/IncoreInstrument.h>
#include <pwr/RodClusterAssembly.h>
#include <pwr/PressurizedWaterReactor.h>
#include <Tube.h>
#include <Material.h>
#include <Ring.h>
#include <GridLabelProvider.h>
#include <LWRGridManager.h>
#include <H5Cpp.h>
#include <unistd.h>
#include <sys/stat.h>
#include <sys/types.h>

using namespace ICE_Reactor;
using namespace ICE_IO;

BOOST_AUTO_TEST_SUITE(LWRComponentWriterTester_testSuite)

BOOST_AUTO_TEST_CASE(beforeClass) {

    //Create folder testPath in local directory - relative!
    std::string testFolder = "ICEIOTestsDir/";
    mkdir(testFolder.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    //Delete file
    std::string testFile = testFolder + "test.h5";
    remove(testFile.c_str());

}
BOOST_AUTO_TEST_CASE(checkWriting) {

    // begin-user-code

    // Create a reactor of size 2 X 2
    //Local Declarations
    std::string testFileName = "test.h5";
    std::string testFolder = "ICEIOTestsDir/";
    std::string dataFile = testFolder + testFileName +"";
    std::shared_ptr<PressurizedWaterReactor> pwReactor (new PressurizedWaterReactor(15));
    pwReactor.get()->setName("PWR Reactor");
    pwReactor.get()->setFuelAssemblyPitch(0.12345678912345);

    // PWREACTOR GRID
    // LABELS/////////////////////////////////////////////////
    // Create a list of row labels
    std::vector<std::string> rowLabelsReactor;
    rowLabelsReactor.push_back("1");
    rowLabelsReactor.push_back("2");
    rowLabelsReactor.push_back("3");
    rowLabelsReactor.push_back("4");
    rowLabelsReactor.push_back("5");
    rowLabelsReactor.push_back("6");
    rowLabelsReactor.push_back("7");
    rowLabelsReactor.push_back("8");
    rowLabelsReactor.push_back("9");
    rowLabelsReactor.push_back("10");
    rowLabelsReactor.push_back("11");
    rowLabelsReactor.push_back("12");
    rowLabelsReactor.push_back("13");
    rowLabelsReactor.push_back("14");
    rowLabelsReactor.push_back("15");

    // Create a list of column labels
    std::vector<std::string> columnLabelsReactor;
    columnLabelsReactor.push_back("R");
    columnLabelsReactor.push_back("P");
    columnLabelsReactor.push_back("N");
    columnLabelsReactor.push_back("M");
    columnLabelsReactor.push_back("L");
    columnLabelsReactor.push_back("K");
    columnLabelsReactor.push_back("J");
    columnLabelsReactor.push_back("H");
    columnLabelsReactor.push_back("G");
    columnLabelsReactor.push_back("F");
    columnLabelsReactor.push_back("E");
    columnLabelsReactor.push_back("D");
    columnLabelsReactor.push_back("C");
    columnLabelsReactor.push_back("B");
    columnLabelsReactor.push_back("A");

    // Assign the label arrays
    pwReactor.get()->getGridLabelProvider().get()->setRowLabels(rowLabelsReactor);
    pwReactor.get()->getGridLabelProvider().get()->setColumnLabels(columnLabelsReactor);

    // CONTROL
    // BANKS//////////////////////////////////////////////////////////
    // Add two control banks to the reactor
    pwReactor.get()->addAssembly(Control_Bank, std::shared_ptr<ControlBank> (new ControlBank("A", 0.625, 230)));
    pwReactor.get()->addAssembly(Control_Bank, std::shared_ptr<ControlBank> (new ControlBank("B", 0.625, 215)));
    pwReactor.get()->addAssembly(Control_Bank, std::shared_ptr<ControlBank> (new ControlBank("C", 0.625, 200)));
    pwReactor.get()->addAssembly(Control_Bank, std::shared_ptr<ControlBank> (new ControlBank("D", 0.625, 185)));

    // Assign a position for the control banks
    pwReactor.get()->setAssemblyLocation(Control_Bank, "A", 4, 4);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "A", 4, 10);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "A", 5, 7);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "A", 7, 5);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "A", 7, 9);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "A", 9, 7);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "A", 10, 4);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "A", 10, 10);

    pwReactor.get()->setAssemblyLocation(Control_Bank, "B", 1, 5);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "B", 1, 9);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "B", 5, 1);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "B", 5, 13);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "B", 9, 1);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "B", 9, 13);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "B", 13, 5);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "B", 13, 9);

    pwReactor.get()->setAssemblyLocation(Control_Bank, "C", 1, 7);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "C", 5, 5);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "C", 5, 9);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "C", 7, 1);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "C", 7, 13);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "C", 9, 5);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "C", 9, 9);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "C", 13, 7);

    pwReactor.get()->setAssemblyLocation(Control_Bank, "D", 3, 3);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "D", 3, 7);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "D", 3, 11);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "D", 7, 3);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "D", 7, 7);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "D", 7, 11);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "D", 11, 3);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "D", 11, 7);
    pwReactor.get()->setAssemblyLocation(Control_Bank, "D", 11, 11);

    // INCORE
    // INSTRUMENTS/////////////////////////////////////////////////////
    // Create incore instruments for the reactor
    std::shared_ptr<IncoreInstrument> incoreInstrument1 ( new IncoreInstrument());
    incoreInstrument1.get()->setName("Incore Instrument 1");
    std::shared_ptr<IncoreInstrument> incoreInstrument2 ( new IncoreInstrument());
    incoreInstrument2.get()->setName("Incore Instrument 2");
    std::shared_ptr<IncoreInstrument> incoreInstrument3 ( new IncoreInstrument());
    incoreInstrument3.get()->setName("Incore Instrument 3");
    std::shared_ptr<IncoreInstrument> incoreInstrument4 ( new IncoreInstrument());
    incoreInstrument4.get()->setName("Incore Instrument 4");

    // Create the thimble material
    std::shared_ptr<Material> material ( new Material("stainless steel"));
    material.get()->setMaterialType(SOLID);

    // Create the thimble
    std::shared_ptr<Ring> thimble1 ( new Ring("Thimble", material, 155, 0.258, 0.382));
    std::shared_ptr<Ring> thimble2 ( new Ring("Thimble", material, 155, 0.258, 0.382));
    std::shared_ptr<Ring> thimble3 ( new Ring("Thimble", material, 155, 0.258, 0.382));
    std::shared_ptr<Ring> thimble4 ( new Ring("Thimble", material, 155, 0.258, 0.382));
    incoreInstrument1.get()->setThimble(thimble1);
    incoreInstrument2.get()->setThimble(thimble2);
    incoreInstrument3.get()->setThimble(thimble3);
    incoreInstrument4.get()->setThimble(thimble4);

    // Add the incore instruments
    pwReactor.get()->addAssembly(Incore_Instrument, incoreInstrument1);
    pwReactor.get()->addAssembly(Incore_Instrument, incoreInstrument2);
    pwReactor.get()->addAssembly(Incore_Instrument, incoreInstrument3);
    pwReactor.get()->addAssembly(Incore_Instrument, incoreInstrument4);

    // Assign locations for the incore instruments
    pwReactor.get()->setAssemblyLocation(Incore_Instrument, incoreInstrument1.get()->getName(), 2, 1);
    pwReactor.get()->setAssemblyLocation(Incore_Instrument, incoreInstrument2.get()->getName(), 6, 5);
    pwReactor.get()->setAssemblyLocation(Incore_Instrument, incoreInstrument3.get()->getName(), 11, 2);
    pwReactor.get()->setAssemblyLocation(Incore_Instrument, incoreInstrument4.get()->getName(), 13, 8);

    // FUEL
    // ASSEMBLIES////////////////////////////////////////////////////////
    // Create a fuel assembly
    std::shared_ptr<FuelAssembly> fuelAssembly ( new FuelAssembly("Fuel Assembly A", 17));

    // Create a list of row labels
    std::vector<std::string> rowLabelsFuelAssembly;
    rowLabelsFuelAssembly.push_back("1");
    rowLabelsFuelAssembly.push_back("2");
    rowLabelsFuelAssembly.push_back("3");
    rowLabelsFuelAssembly.push_back("4");
    rowLabelsFuelAssembly.push_back("5");
    rowLabelsFuelAssembly.push_back("6");
    rowLabelsFuelAssembly.push_back("7");
    rowLabelsFuelAssembly.push_back("8");
    rowLabelsFuelAssembly.push_back("9");
    rowLabelsFuelAssembly.push_back("10");
    rowLabelsFuelAssembly.push_back("11");
    rowLabelsFuelAssembly.push_back("12");
    rowLabelsFuelAssembly.push_back("13");
    rowLabelsFuelAssembly.push_back("14");
    rowLabelsFuelAssembly.push_back("15");
    rowLabelsFuelAssembly.push_back("16");
    rowLabelsFuelAssembly.push_back("17");

    // Create list of column labels
    std::vector<std::string> columnLabelsFuelAssembly;
    columnLabelsFuelAssembly.push_back("A");
    columnLabelsFuelAssembly.push_back("B");
    columnLabelsFuelAssembly.push_back("C");
    columnLabelsFuelAssembly.push_back("D");
    columnLabelsFuelAssembly.push_back("E");
    columnLabelsFuelAssembly.push_back("F");
    columnLabelsFuelAssembly.push_back("G");
    columnLabelsFuelAssembly.push_back("H");
    columnLabelsFuelAssembly.push_back("I");
    columnLabelsFuelAssembly.push_back("J");
    columnLabelsFuelAssembly.push_back("K");
    columnLabelsFuelAssembly.push_back("L");
    columnLabelsFuelAssembly.push_back("M");
    columnLabelsFuelAssembly.push_back("N");
    columnLabelsFuelAssembly.push_back("O");
    columnLabelsFuelAssembly.push_back("P");
    columnLabelsFuelAssembly.push_back("Q");

    // Assign the labels array
    fuelAssembly.get()->getGridLabelProvider().get()->setRowLabels(rowLabelsFuelAssembly);
    fuelAssembly.get()->getGridLabelProvider().get()->setColumnLabels(columnLabelsFuelAssembly);

    // Create a guide tube
    std::shared_ptr<Tube> guideTube (new Tube("Guide Tube A", GUIDE));
    guideTube.get()->setHeight(1.56);
    guideTube.get()->setInnerRadius(7.89);
    guideTube.get()->setOuterRadius(10.0);

    // Create material for the guide tube
    std::shared_ptr<Material> materialGuideTube (new Material("Guide Tube Material"));

    // Set the material for the guide tube
    guideTube.get()->setMaterial(materialGuideTube);

    // Add the guide tube to the fuel assembly
    fuelAssembly.get()->addTube(guideTube);

    // Assign the guide tube a location
    fuelAssembly.get()->setTubeLocation(guideTube.get()->getName(), 8, 13);

    // Create an instrument tube
    std::shared_ptr<Tube> instrumentTube ( new Tube("Instrument Tube A", INSTRUMENT));
    instrumentTube.get()->setHeight(1.2);
    instrumentTube.get()->setInnerRadius(0.987);
    instrumentTube.get()->setOuterRadius(34.5);

    // Create material for the instrument tube
    std::shared_ptr<Material> materialInstrumentTube ( new Material("Instrument Tube Material"));

    // Set the material for the instrument tube
    instrumentTube.get()->setMaterial(materialInstrumentTube);

    // Add the instrument tube to the fuel assembly
    fuelAssembly.get()->addTube(instrumentTube);

    // Assign the instrument tube a location
    fuelAssembly.get()->setTubeLocation(instrumentTube.get()->getName(), 8, 8);

    // Create an lwrrod for this fuel assembly
    std::shared_ptr<LWRRod> rod (new LWRRod("LWRRod A"));
    rod.get()->setPressure(23.56);

    // Create a fill gas for the rod
    std::shared_ptr<Material> fillGas (new Material("He", GAS));
    rod.get()->setFillGas(fillGas);

    // Create a MaterialBlock for the rod
    std::shared_ptr<MaterialBlock> stack (new MaterialBlock());
    stack.get()->setName("Stack of Cards");

    // Create some rings for the MaterialBlock
    std::shared_ptr<Ring> ring1 (new Ring("Ring 1"));
    ring1.get()->setHeight(155);
    ring1.get()->setOuterRadius(0.5);

    // Create a material for ring1
    std::shared_ptr<Material> ring1Material (new Material("Ring 1 Material", SOLID));
    ring1.get()->setMaterial(ring1Material);

    // Create some rings for the Material Block
    std::shared_ptr<Ring> ring2 ( new Ring("Ring 2"));
    ring2.get()->setHeight(155);
    ring2.get()->setInnerRadius(0.5);
     ring2.get()->setOuterRadius(1.0);

    // Create a material for ring1
    std::shared_ptr<Material> ring2Material ( new Material("Ring 2 Material", SOLID));
    ring2.get()->setMaterial(ring2Material);

    // Add rings to the MaterialBlock
    stack.get()->addRing(ring1);
    stack.get()->addRing(ring2);

    std::shared_ptr<std::vector < std:: shared_ptr < MaterialBlock > > > blocks ( new std::vector < std::shared_ptr < MaterialBlock> > ());
    blocks.get()->push_back(stack);

    // Set the material blocks on the rod
    rod.get()->setMaterialBlocks(blocks);

    // Create a clad
    std::shared_ptr<Ring> clad ( new Ring("Clad"));
    clad.get()->setHeight(155);
    clad.get()->setInnerRadius(0.9);
    clad.get()->setOuterRadius(1.0);

    // Create a material for the clad
    std::shared_ptr<Material> materialClad (new Material("Clad Material", SOLID));
    clad.get()->setMaterial(materialClad);

    // Add the clad
    rod.get()->setClad(clad);

    // Add the rod to the fuel assembly
    fuelAssembly.get()->addLWRRod(rod);

    // Assign the rod a location
    fuelAssembly.get()->setLWRRodLocation(rod.get()->getName(), 15, 4);

    // Add the fuel assembly to the reactor
    pwReactor.get()->addAssembly(Fuel, fuelAssembly);

    // Assign a position on the grid of the reactor
    pwReactor.get()->setAssemblyLocation(Fuel, fuelAssembly.get()->getName(), 4, 4);

    // ROD CLUSTER
    // ASSEMBLIES/////////////////////////////////////////////////
    // Create a rca
    std::shared_ptr<RodClusterAssembly> rodClusterAssembly ( new RodClusterAssembly("Rod Cluster Assembly A", 17));

    // Add the rca to the reactor
    pwReactor.get()->addAssembly(RodCluster, rodClusterAssembly);

    // Assign the rca location
    pwReactor.get()->setAssemblyLocation(RodCluster, rodClusterAssembly.get()->getName(), 5, 2);

    //Create a writer
    LWRComponentWriter lWRComponentWriter;

    // write the reactor
    BOOST_REQUIRE_EQUAL(lWRComponentWriter.write(pwReactor, dataFile), true);

    std::shared_ptr<H5::H5File> h5File;

    //Setup the HDF5 File.  If an exception is thrown, then the file does not exist
    try {
        h5File =  HdfFileFactory::openH5File(dataFile);
    } catch(...) {
        BOOST_FAIL("FAILURE IN LWRCOMPONENTWRITERTESTER!  EXITING!");
    }

    //Now, we will open up the file and reverse examine to see if every group is in there

    try {
        BOOST_REQUIRE_EQUAL(h5File.get() !=NULL, true);
        //Get the root group
        std::shared_ptr<H5::Group> pWReactor (new H5::Group(h5File.get()->openGroup("/PWR Reactor")));

        //Now check each group node

        //Check PWRReactor's subpieces
        BOOST_REQUIRE_EQUAL(10, pWReactor.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("Control Bank Grid", pWReactor.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Control Banks", pWReactor.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("Fuel Assemblies", pWReactor.get()->getObjnameByIdx(2));
        BOOST_REQUIRE_EQUAL("Fuel Assembly Grid", pWReactor.get()->getObjnameByIdx(3));
        BOOST_REQUIRE_EQUAL("Grid Labels", pWReactor.get()->getObjnameByIdx(4));
        BOOST_REQUIRE_EQUAL("Incore Instrument Grid", pWReactor.get()->getObjnameByIdx(5));
        BOOST_REQUIRE_EQUAL("Incore Instruments", pWReactor.get()->getObjnameByIdx(6));
        BOOST_REQUIRE_EQUAL("Rod Cluster Assemblies", pWReactor.get()->getObjnameByIdx(7));
        BOOST_REQUIRE_EQUAL("Rod Cluster Assembly Grid", pWReactor.get()->getObjnameByIdx(8));
        BOOST_REQUIRE_EQUAL("State Point Data", pWReactor.get()->getObjnameByIdx(9));

        //Check the Control Bank Grid
        std::shared_ptr<H5::Group> controlBankGrid (new H5::Group( pWReactor.get()->openGroup(pWReactor.get()->getObjnameByIdx(0))));
        BOOST_REQUIRE_EQUAL(2, controlBankGrid.get()->getNumObjs());
        //Check Control Bank Grid Information
        BOOST_REQUIRE_EQUAL("Positions", controlBankGrid.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", controlBankGrid.get()->getObjnameByIdx(1));
        //No more information below

        //Check Control Banks
        std::shared_ptr<H5::Group>controlBank (new H5::Group( pWReactor.get()->openGroup(pWReactor.get()->getObjnameByIdx(1))));
        BOOST_REQUIRE_EQUAL(5, controlBank.get()->getNumObjs());
        //Check Control Bank Information
        BOOST_REQUIRE_EQUAL("A", controlBank.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("B", controlBank.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("C", controlBank.get()->getObjnameByIdx(2));
        BOOST_REQUIRE_EQUAL("D", controlBank.get()->getObjnameByIdx(3));
        BOOST_REQUIRE_EQUAL("State Point Data", controlBank.get()->getObjnameByIdx(4));
        //Check Groups
        std::shared_ptr<H5::Group>A (new H5::Group( controlBank.get()->openGroup(controlBank.get()->getObjnameByIdx(0))));
        std::shared_ptr<H5::Group>B (new H5::Group( controlBank.get()->openGroup(controlBank.get()->getObjnameByIdx(1))));
        std::shared_ptr<H5::Group>C (new H5::Group( controlBank.get()->openGroup(controlBank.get()->getObjnameByIdx(2))));
        std::shared_ptr<H5::Group>D (new H5::Group( controlBank.get()->openGroup(controlBank.get()->getObjnameByIdx(3))));
        //Check contents of Groups
        BOOST_REQUIRE_EQUAL(1, A.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", A.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL(1, B.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", B.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL(1, C.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", C.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL(1, D.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", D.get()->getObjnameByIdx(0));

        //Check Fuel Assemblies
        std::shared_ptr<H5::Group>fuelAssemblies (new H5::Group( pWReactor.get()->openGroup(pWReactor.get()->getObjnameByIdx(2))));
        BOOST_REQUIRE_EQUAL(2, fuelAssemblies.get()->getNumObjs());
        //Check Fuel Assemblies Information
        BOOST_REQUIRE_EQUAL("Fuel Assembly A", fuelAssemblies.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", fuelAssemblies.get()->getObjnameByIdx(1));
        //Check Groups
        std::shared_ptr<H5::Group>fuelAssemblyA (new H5::Group( fuelAssemblies.get()->openGroup(fuelAssemblies.get()->getObjnameByIdx(0))));

        //Check contents of FuelAssemblyA
        BOOST_REQUIRE_EQUAL(6, fuelAssemblyA.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("Grid Labels", fuelAssemblyA.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("LWRRod Grid", fuelAssemblyA.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("LWRRods", fuelAssemblyA.get()->getObjnameByIdx(2));
        BOOST_REQUIRE_EQUAL("State Point Data" , fuelAssemblyA.get()->getObjnameByIdx(3));
        BOOST_REQUIRE_EQUAL("Tube Grid", fuelAssemblyA.get()->getObjnameByIdx(4));
        BOOST_REQUIRE_EQUAL("Tubes", fuelAssemblyA.get()->getObjnameByIdx(5));

        //Check Groups of FuelAssembly A
        std::shared_ptr<H5::Group>gridLabels (new H5::Group( fuelAssemblyA.get()->openGroup(fuelAssemblyA.get()->getObjnameByIdx(0))));
        std::shared_ptr<H5::Group>lWRRodGrid (new H5::Group( fuelAssemblyA.get()->openGroup(fuelAssemblyA.get()->getObjnameByIdx(1))));
        std::shared_ptr<H5::Group>lWRRods (new H5::Group( fuelAssemblyA.get()->openGroup(fuelAssemblyA.get()->getObjnameByIdx(2))));
        //3rd one here is state point data
        std::shared_ptr<H5::Group>tubeGrid (new H5::Group( fuelAssemblyA.get()->openGroup(fuelAssemblyA.get()->getObjnameByIdx(4))));
        std::shared_ptr<H5::Group>tubes (new H5::Group( fuelAssemblyA.get()->openGroup(fuelAssemblyA.get()->getObjnameByIdx(5))));

        //Check Groups
        BOOST_REQUIRE_EQUAL(2, gridLabels.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(2, lWRRodGrid.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(2, lWRRods.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(2, tubeGrid.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL(3, tubes.get()->getNumObjs());
        //Check gridLabels
        std::shared_ptr<H5::Group>labelsGroup (new H5::Group( gridLabels.get()->openGroup(gridLabels.get()->getObjnameByIdx(0))));

        BOOST_REQUIRE_EQUAL("Column Labels", labelsGroup.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Row Labels", labelsGroup.get()->getObjnameByIdx(1));
        //Check Groups of GridLabels

        //Check lWRRodGrid
        BOOST_REQUIRE_EQUAL("Positions", lWRRodGrid.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", lWRRodGrid.get()->getObjnameByIdx(1));

        //Check LWRRods
        BOOST_REQUIRE_EQUAL("LWRRod A", lWRRods.get()->getObjnameByIdx(0));
        std::shared_ptr<H5::Group>lWRRodsA (new H5::Group(lWRRods.get()->openGroup(lWRRods.get()->getObjnameByIdx(0))));
        BOOST_REQUIRE_EQUAL("State Point Data", lWRRods.get()->getObjnameByIdx(1));
        //Check lWRRodsA
        BOOST_REQUIRE_EQUAL(4, lWRRodsA.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("Clad", lWRRodsA.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("He", lWRRodsA.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("Stack of Cards", lWRRodsA.get()->getObjnameByIdx(2));
        BOOST_REQUIRE_EQUAL("State Point Data", lWRRodsA.get()->getObjnameByIdx(3));
        //Check Groups of lWRRodsA
        std::shared_ptr<H5::Group>cladRodA (new H5::Group( lWRRodsA.get()->openGroup(lWRRodsA.get()->getObjnameByIdx(0))));
        std::shared_ptr<H5::Group>heRodA (new H5::Group( lWRRodsA.get()->openGroup(lWRRodsA.get()->getObjnameByIdx(1))));
        std::shared_ptr<H5::Group>stackRodA (new H5::Group( lWRRodsA.get()->openGroup(lWRRodsA.get()->getObjnameByIdx(2))));
        //Check cladRodA
        BOOST_REQUIRE_EQUAL(2, cladRodA.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("Clad Material", cladRodA.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", cladRodA.get()->getObjnameByIdx(1));
        //Check Groups
        std::shared_ptr<H5::Group>materialCladRodA (new H5::Group( cladRodA.get()->openGroup(cladRodA.get()->getObjnameByIdx(0))));
        //Check materialCladRodA
        BOOST_REQUIRE_EQUAL(1, materialCladRodA.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", materialCladRodA.get()->getObjnameByIdx(0));
        //Check heRodA
        BOOST_REQUIRE_EQUAL(1, heRodA.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", heRodA.get()->getObjnameByIdx(0));
        //Check stackRodA
        BOOST_REQUIRE_EQUAL(3, stackRodA.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("Ring 1", stackRodA.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Ring 2", stackRodA.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("State Point Data", stackRodA.get()->getObjnameByIdx(2));
        //Check Groups
        std::shared_ptr<H5::Group>ring1StackA (new H5::Group( stackRodA.get()->openGroup(stackRodA.get()->getObjnameByIdx(0))));
        std::shared_ptr<H5::Group>ring2StackA (new H5::Group( stackRodA.get()->openGroup(stackRodA.get()->getObjnameByIdx(1))));
        //Check ring1StackA
        BOOST_REQUIRE_EQUAL(2, ring1StackA.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("Ring 1 Material", ring1StackA.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", ring1StackA.get()->getObjnameByIdx(1));
        std::shared_ptr<H5::Group>material1Ring (new H5::Group( ring1StackA.get()->openGroup(ring1StackA.get()->getObjnameByIdx(0))));
        //Check material1Ring
        BOOST_REQUIRE_EQUAL(1, material1Ring.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", material1Ring.get()->getObjnameByIdx(0));
        //Check ring2StackA
        BOOST_REQUIRE_EQUAL(2, ring2StackA.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("Ring 2 Material", ring2StackA.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", ring2StackA.get()->getObjnameByIdx(1));
        std::shared_ptr<H5::Group>material2Ring (new H5::Group( ring2StackA.get()->openGroup(ring2StackA.get()->getObjnameByIdx(0))));
        //Check material1Ring
        BOOST_REQUIRE_EQUAL(1, material2Ring.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", material2Ring.get()->getObjnameByIdx(0));
        //Check tubeGrid
        BOOST_REQUIRE_EQUAL("Positions", tubeGrid.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", tubeGrid.get()->getObjnameByIdx(1));

        //Check tubes
        BOOST_REQUIRE_EQUAL("Guide Tube A", tubes.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Instrument Tube A", tubes.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("State Point Data", tubes.get()->getObjnameByIdx(2));
        //Check Groups
        std::shared_ptr<H5::Group>guideTubeA (new H5::Group( tubes.get()->openGroup(tubes.get()->getObjnameByIdx(0))));
        std::shared_ptr<H5::Group>instrTubeA (new H5::Group( tubes.get()->openGroup(tubes.get()->getObjnameByIdx(1))));
        //Check guideTubeA
        BOOST_REQUIRE_EQUAL(2, guideTubeA.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("Guide Tube Material", guideTubeA.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", guideTubeA.get()->getObjnameByIdx(1));
        std::shared_ptr<H5::Group>guideTubeMaterial (new H5::Group( guideTubeA.get()->openGroup(guideTubeA.get()->getObjnameByIdx(0))));
        //Check guideTubeMaterial
        BOOST_REQUIRE_EQUAL(1, guideTubeMaterial.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", guideTubeMaterial.get()->getObjnameByIdx(0));
        //Check instrTubeA
        BOOST_REQUIRE_EQUAL(2, instrTubeA.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("Instrument Tube Material", instrTubeA.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", instrTubeA.get()->getObjnameByIdx(1));
        std::shared_ptr<H5::Group>instrTubeMaterial (new H5::Group( instrTubeA.get()->openGroup(instrTubeA.get()->getObjnameByIdx(0))));
        //Check guideTubeMaterial
        BOOST_REQUIRE_EQUAL(1, instrTubeMaterial.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", instrTubeMaterial.get()->getObjnameByIdx(0));

        //Fuel Assembly Grid
        std::shared_ptr<H5::Group>fuelAssemblyGrid (new H5::Group( pWReactor.get()->openGroup(pWReactor.get()->getObjnameByIdx(3))));
        BOOST_REQUIRE_EQUAL(2, fuelAssemblyGrid.get()->getNumObjs());
        //Check FuelAssembly Grid Information
        BOOST_REQUIRE_EQUAL("Positions", fuelAssemblyGrid.get()->getObjnameByIdx(0));
        //No more information below

        //Grid Labels
        std::shared_ptr<H5::Group>reactorGridLabels (new H5::Group( pWReactor.get()->openGroup(pWReactor.get()->getObjnameByIdx(4))));
        BOOST_REQUIRE_EQUAL(2, reactorGridLabels.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", reactorGridLabels.get()->getObjnameByIdx(1));

        //Check gridLabels
        std::shared_ptr<H5::Group>labelsGroup2 (new H5::Group( reactorGridLabels.get()->openGroup(reactorGridLabels.get()->getObjnameByIdx(0))));

        BOOST_REQUIRE_EQUAL("Column Labels", labelsGroup2.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Row Labels", labelsGroup2.get()->getObjnameByIdx(1));
        //Check Groups of GridLabels

        //Check Incore Instrument Grid
        std::shared_ptr<H5::Group>incoreInstrumentGrid (new H5::Group( pWReactor.get()->openGroup(pWReactor.get()->getObjnameByIdx(5))));
        //Check icoreInstrumentGrid
        BOOST_REQUIRE_EQUAL(2, incoreInstrumentGrid.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", incoreInstrumentGrid.get()->getObjnameByIdx(1));
        //Check incoreInstruments infor
        BOOST_REQUIRE_EQUAL("Positions", incoreInstrumentGrid.get()->getObjnameByIdx(0));
        //Check Groups

        //Check Incore Instrument
        std::shared_ptr<H5::Group>incoreInstrument (new H5::Group( pWReactor.get()->openGroup(pWReactor.get()->getObjnameByIdx(6))));
        BOOST_REQUIRE_EQUAL(5, incoreInstrument.get()->getNumObjs());
        //Check incore instrument information
        BOOST_REQUIRE_EQUAL("Incore Instrument 1", incoreInstrument.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Incore Instrument 2", incoreInstrument.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("Incore Instrument 3", incoreInstrument.get()->getObjnameByIdx(2));
        BOOST_REQUIRE_EQUAL("Incore Instrument 4", incoreInstrument.get()->getObjnameByIdx(3));
        BOOST_REQUIRE_EQUAL("State Point Data", incoreInstrument.get()->getObjnameByIdx(4));
        //Check Groups
        std::shared_ptr<H5::Group>incore1 (new H5::Group( incoreInstrument.get()->openGroup(incoreInstrument.get()->getObjnameByIdx(0))));
        std::shared_ptr<H5::Group>incore2 (new H5::Group( incoreInstrument.get()->openGroup(incoreInstrument.get()->getObjnameByIdx(1))));
        std::shared_ptr<H5::Group>incore3 (new H5::Group( incoreInstrument.get()->openGroup(incoreInstrument.get()->getObjnameByIdx(2))));
        std::shared_ptr<H5::Group>incore4 (new H5::Group( incoreInstrument.get()->openGroup(incoreInstrument.get()->getObjnameByIdx(3))));
        //Check incore1
        BOOST_REQUIRE_EQUAL(2, incore1.get()->getNumObjs());
        //Check incore1 information
        BOOST_REQUIRE_EQUAL("Thimble", incore1.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("State Point Data", incore1.get()->getObjnameByIdx(0));
        //Check Groups
        std::shared_ptr<H5::Group>thimble1 (new H5::Group( incore1.get()->openGroup(incore1.get()->getObjnameByIdx(1))));
        //Check thimble
        BOOST_REQUIRE_EQUAL(2, thimble1.get()->getNumObjs());
        //Check thimble information
        BOOST_REQUIRE_EQUAL("stainless steel", thimble1.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("State Point Data", thimble1.get()->getObjnameByIdx(0));
        //Check Groups
        std::shared_ptr<H5::Group>steel1 (new H5::Group( thimble1.get()->openGroup(thimble1.get()->getObjnameByIdx(0))));
        //Check steel information
        BOOST_REQUIRE_EQUAL(0, steel1.get()->getNumObjs());
        //Check incore2 information
        BOOST_REQUIRE_EQUAL("State Point Data", incore2.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Thimble", incore2.get()->getObjnameByIdx(1));
        //Check Groups
        std::shared_ptr<H5::Group>thimble2 (new H5::Group( incore2.get()->openGroup(incore2.get()->getObjnameByIdx(1))));
        //Check thimble
        BOOST_REQUIRE_EQUAL(2, thimble2.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", thimble2.get()->getObjnameByIdx(0));
        //Check thimble information
        BOOST_REQUIRE_EQUAL("stainless steel", thimble2.get()->getObjnameByIdx(1));
        //Check Groups
        std::shared_ptr<H5::Group>steel2 (new H5::Group( thimble2.get()->openGroup(thimble2.get()->getObjnameByIdx(1))));
        //Check steel information
        BOOST_REQUIRE_EQUAL(1, steel2.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", steel2.get()->getObjnameByIdx(0));
        //Check incore3 information
        BOOST_REQUIRE_EQUAL("State Point Data", incore3.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Thimble", incore3.get()->getObjnameByIdx(1));
        //Check Groups
        std::shared_ptr<H5::Group>thimble3 (new H5::Group( incore3.get()->openGroup(incore3.get()->getObjnameByIdx(1))));
        //Check thimble
        BOOST_REQUIRE_EQUAL(2, thimble3.get()->getNumObjs());
        //Check thimble information
        BOOST_REQUIRE_EQUAL("State Point Data", incore1.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("stainless steel", thimble3.get()->getObjnameByIdx(1));
        //Check Groups
        std::shared_ptr<H5::Group>steel3 (new H5::Group( thimble3.get()->openGroup(thimble3.get()->getObjnameByIdx(1))));
        //Check steel information
        BOOST_REQUIRE_EQUAL(1, steel3.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", steel3.get()->getObjnameByIdx(0));
        //Check incore4 information
        BOOST_REQUIRE_EQUAL("State Point Data", incore4.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("Thimble", incore4.get()->getObjnameByIdx(1));
        //Check Groups
        std::shared_ptr<H5::Group>thimble4 (new H5::Group( incore4.get()->openGroup(incore4.get()->getObjnameByIdx(1))));
        //Check thimble
        BOOST_REQUIRE_EQUAL(2, thimble4.get()->getNumObjs());
        //Check thimble information
        BOOST_REQUIRE_EQUAL("State Point Data", thimble4.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("stainless steel", thimble4.get()->getObjnameByIdx(1));
        //Check Groups
        std::shared_ptr<H5::Group>steel4 (new H5::Group( thimble4.get()->openGroup(thimble4.get()->getObjnameByIdx(1))));
        //Check steel information
        BOOST_REQUIRE_EQUAL(1, steel4.get()->getNumObjs());
        BOOST_REQUIRE_EQUAL("State Point Data", steel4.get()->getObjnameByIdx(0));

        //Check Rod Cluster Assemblies
        std::shared_ptr<H5::Group>rodClusterAssemblies (new H5::Group( pWReactor.get()->openGroup(pWReactor.get()->getObjnameByIdx(7))));
        BOOST_REQUIRE_EQUAL(2, rodClusterAssemblies.get()->getNumObjs());
        //Check incore instrument information
        BOOST_REQUIRE_EQUAL("Rod Cluster Assembly A", rodClusterAssemblies.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", rodClusterAssemblies.get()->getObjnameByIdx(1));
        //Check Groups
        std::shared_ptr<H5::Group>rodClusterA (new H5::Group( rodClusterAssemblies.get()->openGroup(rodClusterAssemblies.get()->getObjnameByIdx(0))));
        //Check RodClusterA
        BOOST_REQUIRE_EQUAL(3, rodClusterA.get()->getNumObjs());
        //Check information
        BOOST_REQUIRE_EQUAL("LWRRod Grid", rodClusterA.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("LWRRods", rodClusterA.get()->getObjnameByIdx(1));
        BOOST_REQUIRE_EQUAL("State Point Data", rodClusterA.get()->getObjnameByIdx(2));
        //Check Groups
        std::shared_ptr<H5::Group>rodLWRRodGrid (new H5::Group( rodClusterA.get()->openGroup(rodClusterA.get()->getObjnameByIdx(0))));
        std::shared_ptr<H5::Group>rodLWRRods (new H5::Group( rodClusterA.get()->openGroup(rodClusterA.get()->getObjnameByIdx(1))));
        //Check rodLWRRodGrid
        BOOST_REQUIRE_EQUAL(1, rodLWRRodGrid.get()->getNumObjs());
        //Check information
        BOOST_REQUIRE_EQUAL("State Point Data", rodLWRRodGrid.get()->getObjnameByIdx(0));
        //Check Groups
        //Check rodLWRRods
        BOOST_REQUIRE_EQUAL(1, rodLWRRods.get()->getNumObjs());

        //Check RodClusterAssembly Grid
        //Grid Labels
        std::shared_ptr<H5::Group>rodClusterAssGrid (new H5::Group( pWReactor.get()->openGroup( pWReactor.get()->getObjnameByIdx(8))));
        BOOST_REQUIRE_EQUAL(2, rodClusterAssGrid.get()->getNumObjs());
        //Check information
        BOOST_REQUIRE_EQUAL("Positions", rodClusterAssGrid.get()->getObjnameByIdx(0));
        BOOST_REQUIRE_EQUAL("State Point Data", rodClusterAssGrid.get()->getObjnameByIdx(1));
        //Check Groups


        //Try to use null in constructor - see results
        LWRComponentWriter lWRComponentWriter2;

        std::string nullString;

        // write the reactor
        BOOST_REQUIRE_EQUAL(lWRComponentWriter2.write(pwReactor, nullString), false);
        std::shared_ptr<PressurizedWaterReactor> nullReactor;
        //Check nullaries on factory
        BOOST_REQUIRE_EQUAL(lWRComponentWriter2.write(nullReactor, dataFile), false);
    } catch (...) {
        BOOST_FAIL("FAILURE IN LWRCOMPONENTWRITER CHECKING!  EXITING!");
    }


    // end-user-code
    return;
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
