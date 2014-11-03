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

#include <pwr/PressurizedWaterReactor.h>
#include <pwr/FuelAssembly.h>
#include <vector>
#include <memory>
#include <string>
#include <IData.h>
#include <LWRRod.h>
#include <LWRComponentWriter.h>
#include <LWRComponentReader.h>
#include <sys/stat.h>
#include <sstream>
#include <iostream>

using namespace ICE_Reactor;

/**
 * Sets up the workspace directory.
 */

void beforeClass() {

    //Create folder testPath in local directory - relative!
    std::string testFolder = "ICEIOTestsDir/";
    mkdir(testFolder.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);

    //Delete file
    std::string testFile = testFolder + "test.h5";
    remove(testFile.c_str());
}

/**
 * Deletes the workspace once finished.
 */

void afterClass() {

    //relative!
    std::string testFolder = "ICEIOTestsDir/";
    std::string testFile = testFolder + "test.h5";

    //Delete file
    //remove(testFile.c_str());

    //Delete folder
    remove(testFolder.c_str());
}

/**
 * This class is a sample code used to demonstrate a break down on how to utilize the PressurizedWaterReactor and its reading/writing capabilities.
 * More examples and explanations of the code can be found in our testing package.
 */

int main () {

	//Setup the workspace
	beforeClass();

	//Create a basic PressurizedWaterReactor.  It will be 15x15 in shape.
	int reactorSize = 15;
	int assemblySize = 17;
	std::shared_ptr<PressurizedWaterReactor> reactor( new PressurizedWaterReactor(reactorSize));
	std::string fuelAssemblyBetaName = "FuelAssembly Beta";

	//Create a 17x17 FuelAssembly.  This will contain rods (LWRrods) and be contained in the reactor
	//Notice how we use shared_ptrs to allocate.  This is important because adding components to the reactor can only be achieved with the use of shared pointers.
	//This is useful because it cuts down on alot of memory leaks and management.
	std::shared_ptr<FuelAssembly> fuelAssemblyAlpha( new FuelAssembly(assemblySize));

	//All pieces that implement LWRComponent will allow you to set names and descriptions
	fuelAssemblyAlpha.get()->setName("FuelAssembly Alpha");

	//You can also create a FuelAssembly by using the more detailed overloaded operations.
	std::shared_ptr<FuelAssembly> fuelAssemblyBeta( new FuelAssembly(fuelAssemblyBetaName, assemblySize));

	//Lets add some rods to the Alpha assembly.  We can start out by adding all rods (LWRRods) to the 17x17 grid.

	//Add 3 rods
	for(int i = 0; i < 3; i++) {
		//Name of the LWRRod
		std::stringstream ss1;
		ss1 << i;
		//Add an integer value to the end of the rod.  Rods should always be uniquely name within each assembly.
		std::string rodName = "LWRRodAlpha " +ss1.str();

		//Create a LWRRod.  This will name the rod, add a custom material, add pressure, and create a new stack.  Please use the parameterized constructors when possible in order to increase coding productivity
		std::shared_ptr<MaterialBlock> mBlock (new MaterialBlock());
		std::shared_ptr< std::vector < std::shared_ptr < MaterialBlock > > > blocks ( new std::vector < std::shared_ptr< MaterialBlock> >());
		blocks.get()->push_back(mBlock);
		std::shared_ptr<LWRRod> rod (new LWRRod(rodName, std::shared_ptr<Material> (new Material("LWRRod's Material")), ((double) (i * .1)), blocks));


		//Add the rod.  Notice it will return a value to confirm if it was added  correctly.
		bool retVal = fuelAssemblyAlpha.get()->addLWRRod(rod);
	}


	//If you need to place statepoint data on each individual rod, then do it this way
	for(int i = 0; i < assemblySize; i++) {
		for(int j = 0; j < assemblySize; j++) {

			std::stringstream ss1, ss2;
			ss1 << i;
			ss2 << j;

			std::string featureName1 = "Axial Pin Power";
			std::string featureName2 = "Average Pin Power";

			//set the location for rod 1
			fuelAssemblyAlpha.get()->setLWRRodLocation("LWRRodAlpha 1", i, j);

			std::shared_ptr<LWRData> data1 ( new LWRData(featureName1));
			data1.get()->setValue(i*j * .1);

			std::shared_ptr<LWRData> data2 ( new LWRData(featureName2));
			data2.get()->setValue(i*j * .1);

			//Add data at timestep for multiple areas of data.
			for(int k = 0; k < 49; k++) {
				fuelAssemblyAlpha.get()->getLWRRodDataProviderAtLocation(i, j).get()->addData(data1, 0.0);
				if(k == 48) {
					fuelAssemblyAlpha.get()->getLWRRodDataProviderAtLocation(i, j).get()->addData(data2, 0.0);
				}
			}

		}
	}

	//Now we have two fuelAssemblies.  One has rods and the other does not.  Lets add them to the PressurizedWaterReactor
	reactor.get()->addAssembly(Fuel, fuelAssemblyAlpha);
	reactor.get()->addAssembly(Fuel, fuelAssemblyBeta);

	//We can now set their locations on the grid keyed on their name. This is important to designate locations within the grid

	//Set Alpha locations
	reactor.get()->setAssemblyLocation(Fuel, fuelAssemblyAlpha.get()->getName(), 5, 5);
	reactor.get()->setAssemblyLocation(Fuel, fuelAssemblyAlpha.get()->getName(), 6, 6);
	reactor.get()->setAssemblyLocation(Fuel, fuelAssemblyAlpha.get()->getName(), 7, 7);
	reactor.get()->setAssemblyLocation(Fuel, fuelAssemblyAlpha.get()->getName(), 8, 8);

	//Set Beta Locations
	reactor.get()->setAssemblyLocation(Fuel, fuelAssemblyBeta.get()->getName(), 1, 1);
	reactor.get()->setAssemblyLocation(Fuel, fuelAssemblyBeta.get()->getName(), 2, 2);
	reactor.get()->setAssemblyLocation(Fuel, fuelAssemblyBeta.get()->getName(), 3, 3);
	reactor.get()->setAssemblyLocation(Fuel, fuelAssemblyBeta.get()->getName(), 4, 4);

	//If you want to create another FuelAssembly, this can be achieved by using the copying constructor.
	std::shared_ptr<ICE_DS::Identifiable> identityFuelAssembly = fuelAssemblyAlpha.get()->clone();

	//The cloned object is a DEEP COPIED object.  If it is not, please contact us ASAP!!!
	//You will have to cast it out of it's interface and "up" cast to the more defined structure
	std::shared_ptr<FuelAssembly>thetaFuelAssembly = std::dynamic_pointer_cast<FuelAssembly>(identityFuelAssembly);

	//Now we can set it's name and add it.  Keep in mind that names should always be unique, otherwise it will not work properly.
	thetaFuelAssembly.get()->setName("Fuel Assembly Theta");
	reactor.get()->addAssembly(Fuel, thetaFuelAssembly);

	//Add the theta Fuel Assembly to the list
	reactor.get()->addAssembly(Fuel, thetaFuelAssembly);

	//Populate reactor with thetaFuelAssembly
	reactor.get()->setAssemblyLocation(Fuel, thetaFuelAssembly.get()->getName(), 7, 8);
	reactor.get()->setAssemblyLocation(Fuel, thetaFuelAssembly.get()->getName(), 8, 9);
	reactor.get()->setAssemblyLocation(Fuel, thetaFuelAssembly.get()->getName(), 9, 10);

	//Now you have a Reactor with 3 FuelAssemblies.  Some have rods, others have none.

	//This is a way to persist it to h5.  Please keep in mind that the path is a string and is operating system dependent!

	//Get the file Path
	std::string testFileName = "test.h5";
	std::string testFolder = "ICEIOTestsDir/";
	std::string dataFile = testFolder + testFileName +"";

	//Create a writer
	LWRComponentWriter lWRComponentWriter;

	//Cast it up to the iHdfWriteable

	// write the reactor.  Note this is FLEXIBLE to write just FuelAssemblies or even rods!
	//Also, this will overwrite any files with that name.  So be careful!
	lWRComponentWriter.write(reactor, dataFile);

	//Create a new read
	LWRComponentReader lWRComponentReader;

	//Read from the URI
	std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable = lWRComponentReader.read(dataFile);

	//Cast to a PressurizedWaterReactor
	std::shared_ptr<PressurizedWaterReactor> castedReactor = std::dynamic_pointer_cast<PressurizedWaterReactor>(iHdfReadable);

	//You still have a completed reactor!  Yay!

	//You can print out some of the values

	std::cout << "Are the two reactors equal? " << castedReactor.get()->operator==(*reactor.get()) << std::endl;

	//Print out some values
	std::cout <<  "Name of the Reactor: " << castedReactor.get()->getName() << std::endl;

	//Get the fuel Assembly and print out its rods
	std::shared_ptr<FuelAssembly> fuelAssembly = std::dynamic_pointer_cast<FuelAssembly> (castedReactor.get()->getAssemblyByName(Fuel, fuelAssemblyAlpha.get()->getName()));

	std::cout <<"Found FuelAssembly " <<  fuelAssembly.get()->getName() << std::endl;


	//If you need more thorough examples of the structures within this package, please view the tester pieces in the iceproject directory

}

