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
#define BOOST_TEST_MODULE LWRComponentReaderTester_testSuite
#include <boost/test/included/unit_test.hpp>
#include <LWRComponentReader.h>
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
using namespace ICE_DS;

BOOST_AUTO_TEST_SUITE(LWRComponentReaderTester_testSuite)



BOOST_AUTO_TEST_CASE(beforeClass) {

    //Create folder testPath in local directory - relative!
    std::string testFolder = "ICEIOTestsDir/";
    mkdir(testFolder.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    //Delete file
    std::string testFile = testFolder + "test.h5";
    remove(testFile.c_str());

}
BOOST_AUTO_TEST_CASE(checkReading) {
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

    //Add LWRData to pwReactor
    //Setup LWRData
    std::string feature1 = "Feature 1";
    std::string feature2 = "Feature 2";
    double time1= 1.0, time2 = 3.0, time3 = 3.5;
    std::vector<double> position1;
    std::vector<double> position2;
    std::vector<double> position3;
    std::vector<double> position4;
    std::vector<double> position5;

    //Exceptions
    bool exceptionHit0 = false;
    bool exceptionHit1 = false;

    //Setup Positions

    //Setup Position 1
    position1.push_back(0.0);
    position1.push_back(1.0);
    position1.push_back(0.0);

    //Setup Position 2
    position2.push_back(0.0);
    position2.push_back(1.0);
    position2.push_back(4.0);

    //Setup Position 3
    position3.push_back(1.0);
    position3.push_back(1.0);
    position3.push_back(0.0);

    //Setup Position 4
    position4.push_back(0.0);
    position4.push_back(1.0);
    position4.push_back(1.0);

    //Setup Position 5
    position5.push_back(0.0);
    position5.push_back(1.0);
    position5.push_back(3.0);


    //Setup data1
    std::shared_ptr<LWRData> data1( new LWRData(feature1));
    data1.get()->setPosition(position1);
    data1.get()->setValue(1.0);
    data1.get()->setUncertainty(1.5);
    data1.get()->setUnits("Units 123456");

    //Setup data2
    std::shared_ptr<LWRData> data2(new LWRData(feature1));
    data2.get()->setPosition(position2);
    data2.get()->setValue(2.0);
    data2.get()->setUncertainty(2.5);
    data2.get()->setUnits("Units 2");

    //Setup data3
    std::shared_ptr<LWRData> data3(new LWRData(feature1) );
    data3.get()->setPosition(position3);
    data3.get()->setValue(3.0);
    data3.get()->setUncertainty(3.5);
    data3.get()->setUnits("Units 3");

    //Setup data4
    std::shared_ptr<LWRData> data4( new LWRData(feature1) );
    data4.get()->setPosition(position4);
    data4.get()->setValue(4.0);
    data4.get()->setUncertainty(4.5);
    data4.get()->setUnits("Units 4");

    //Setup data5
    std::shared_ptr<LWRData> data5( new LWRData(feature2) );
    data5.get()->setPosition(position5);
    data5.get()->setValue(5.0);
    data5.get()->setUncertainty(5.5);
    data5.get()->setUnits("Units 5");

    //Add data to the component
    pwReactor.get()->addData(data1, time1);
    pwReactor.get()->addData(data2, time1);
    pwReactor.get()->addData(data3, time2);
    pwReactor.get()->addData(data4, time3);
    pwReactor.get()->addData(data5, time3);

    //Create a writer
    LWRComponentWriter lWRComponentWriter;

    // write the reactor
    BOOST_REQUIRE_EQUAL(lWRComponentWriter.write(pwReactor, dataFile), true);

    //Create a new read
    LWRComponentReader lWRComponentReader;

    //Read from the URI
    std::shared_ptr<IHdfReadable> iHdfReadable = lWRComponentReader.read(dataFile);

    //Cast to a PWReactor
    std::shared_ptr<PressurizedWaterReactor> castedReactor = std::dynamic_pointer_cast<PressurizedWaterReactor>(iHdfReadable);

    //Check values here
    BOOST_REQUIRE_EQUAL(pwReactor.get()->operator ==(*castedReactor.get()), true);

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
