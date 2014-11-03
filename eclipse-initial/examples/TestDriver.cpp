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
#include <time.h>
#include "TestDriver.h"

using namespace ICE_Reactor;


//This file represents a tester driver for running scalability between revisions of code.

//This code will create the following:
//A PressurizedWaterReactor that contains a number of fuel assemblies (specified by user)
//Each FuelAssembly will have the following parameters to be adjusted by the input arguments
/**
 * 1.) The number of Features
 * 2.) The number of state point datas at each feature
 * 3.) The number of time steps
 */

int main(int argc, char **argv) {

	//begin-user-code

	//Local declarations
	int numOfAssemblies;
	int numOfFeatures;
	int numOfDatas;
	int numOfTimesteps;
	int testType; //0 for new method, 1 for deprecated method for storing features

	//Default reactor and assembly size
	int assemblySize = 17;
	int reactorSize = 15;


	//If there are not 5 arguments passed, throw usage error and return
	if(argc != 6) {
		std::cout << "Usage: testDriver numOfAssemblies numOfFeatures numOfDatas numOfTimesteps testType(0/1)" << std::endl;
		return 0;

	}

	//Convert the data
	try {

		//Parse data
		numOfAssemblies = atoi(argv[1]);
		numOfFeatures = atoi(argv[2]);
		numOfDatas = atoi(argv[3]);
		numOfTimesteps = atoi(argv[4]);
		testType = atoi(argv[5]);

	} catch (...) {
		std::cout << "Args not numerical" << std::endl;
		return 0;
	}

	if(numOfAssemblies < 1 || numOfFeatures < 1 || numOfDatas < 1 || numOfTimesteps < 1 || testType < 0 || testType > 1) {
		std::cout << "Args not within legal bounds.  Exiting";
		return 0;
	}

	//Start the tests
	TestDriver driver;

	//Prep the reactor
	std::shared_ptr<PressurizedWaterReactor> reactor;

	//Time and clock variables
	clock_t startClock, beforeWriteClock, afterWriteClock, afterReadClock;
	time_t startTime, beforeWriteTime, afterWriteTime, afterReadTime;

	//Start the clock
	startClock = clock();
	startTime = time(0);

	//Setup data
	if(testType == 0) reactor = driver.newMethodSetupData(numOfAssemblies, numOfFeatures, numOfDatas, numOfTimesteps, reactorSize, assemblySize);
	if(testType == 1) reactor = driver.oldMethodSetupData(numOfAssemblies, numOfFeatures, numOfDatas, numOfTimesteps, reactorSize, assemblySize);

	beforeWriteClock = clock();
	beforeWriteTime = time(0);


	//Create the data file
	//Get the file Path
	std::string testFileName = "test.h5";
	std::string dataFile =  testFileName +"";

	//Create a writer
	LWRComponentWriter lWRComponentWriter;

	// write the reactor.  Note this is FLEXIBLE to write just FuelAssemblies or even rods!
	//Also, this will overwrite any files with that name.  So be careful!
	lWRComponentWriter.write(reactor, dataFile);

	afterWriteClock = clock();
	afterWriteTime = time(0);

	//Create a new read
	LWRComponentReader lWRComponentReader;

	//Read from the URI
	std::shared_ptr<ICE_IO::IHdfReadable> iHdfReadable = lWRComponentReader.read(dataFile);

	//Cast to a PressurizedWaterReactor
	std::shared_ptr<PressurizedWaterReactor> castedReactor = std::dynamic_pointer_cast<PressurizedWaterReactor>(iHdfReadable);

	afterReadClock = clock();
	afterReadTime = time(0);

	//Calculate clock cpu time
	float writeClock((float) afterWriteClock - (float) beforeWriteClock);
	float readClock((float)afterReadClock  - (float) afterWriteClock);
	float setupClock((float) beforeWriteClock - (float)startClock);

	//Calculate time
	double setupTime = difftime(beforeWriteTime, startTime);
	double writeTime = difftime(afterWriteTime, beforeWriteTime);
	double readTime = difftime(afterReadTime, afterWriteTime);

	std::cout << "Outcome of test" << std::endl;
	std::cout << "Number of Fuel Assembles:             " << numOfAssemblies << std::endl;
	std::cout << "Number of Time steps:                 " << numOfTimesteps << std::endl;
	std::cout << "Number of Features per time step:     " << numOfFeatures << std::endl;
	std::cout << "Number of LWRData per Feature:        " << numOfDatas << std::endl;
	std::cout << "Size of reactor:                      " << reactorSize << std::endl;
	std::cout << "Size of each Fuel Assembly:           " << assemblySize << std::endl;
	std::cout << "TestType :                            ";

	//Test type
	if(testType == 0) std::cout << "new" << std::endl;
	if(testType == 1) std::cout << "old" << std::endl;

	//Print time
	std::cout << std::endl;
	std::cout << "Time" << std::endl;
	std::cout << "Total time (seconds):                 " << (writeTime + readTime + setupTime) << std::endl;
	std::cout << "Total Read and Write time (seconds):  " << (writeTime + readTime) << std::endl;
	std::cout << "Setup data time (seconds):            " << setupTime << std::endl;
	std::cout << "Read time (seconds):                  " << readTime  << std::endl;
	std::cout << "Write time (seconds):                 " << writeTime  << std::endl;

	//Print clock time
	std::cout << std::endl;
	std::cout << "Clock cycle time" << std::endl;
	std::cout << "Total clock (seconds):                " << (writeClock + readClock + setupTime) / CLOCKS_PER_SEC << std::endl;
	std::cout << "Total Read and Write clock (seconds): " << (readClock + writeClock)/CLOCKS_PER_SEC << std::endl;
	std::cout << "Setup clock (clock):                  " << setupTime / CLOCKS_PER_SEC << std::endl;
	std::cout << "Read clock (seconds):                 " << readClock / CLOCKS_PER_SEC << std::endl;
	std::cout << "Write clock (seconds):                " << writeClock / CLOCKS_PER_SEC << std::endl;
	std::cout << std::endl;
	std::cout << std::endl;
	std::cout << std::endl;

	//end-user-code

}

std::shared_ptr<PressurizedWaterReactor> TestDriver::newMethodSetupData(int numOfAssemblies, int numOfFeatures, int numOfDatas, int numOfTimesteps, int reactorSize, int assemblySize) {

	//begin-user-code

	std::shared_ptr<PressurizedWaterReactor> reactor (new PressurizedWaterReactor(reactorSize));


	//Make an assembly
	std::shared_ptr<FuelAssembly> assembly(new FuelAssembly(assemblySize));

	//Create rods.  There should only be 3 added
	for(int j = 0; j < 3; j++) {

		//Name of the LWRRod
		std::stringstream ss1;
		ss1 << j;
		//Add an integer value to the end of the rod.  Rods should always be uniquely name within each assembly.
		std::string rodName = "LWRRodAlpha " +ss1.str();

		//Create a LWRRod.  This will name the rod, add a custom material, add pressure, and create a new stack.  Please use the parameterized constructors when possible in order to increase coding productivity
		std::shared_ptr<MaterialBlock> mBlock (new MaterialBlock());
		std::shared_ptr< std::vector < std::shared_ptr < MaterialBlock > > > blocks ( new std::vector < std::shared_ptr< MaterialBlock> >());
		blocks.get()->push_back(mBlock);

		std::shared_ptr<LWRRod> rod (new LWRRod(rodName, std::shared_ptr<Material> (new Material("LWRRod's Material")), ((double) (j * .1)), blocks));

		//Add rod
		assembly.get()->addLWRRod(rod);
	}

	//Setup Feature and LWRData for each position in the assembly for each time step

	for(int t = 0; t < numOfTimesteps; t++) {
		for(int j = 0; j < assemblySize; j++) {
			for(int k = 0; k < assemblySize; k++) {

				//Get the indexes iterated into strings for later
				std::stringstream ss1, ss2, ss3, ss4;
				ss1 << j;
				ss2 << k;

				if(t == 0) {
					//set the location for rod 1
					assembly.get()->setLWRRodLocation("LWRRodAlpha 1", j, k);
				}


				//Iterate over the numOfFeatures
				for(int l = 0; l < numOfFeatures; l++) {
					ss3 << l;
					//Iterate over the numOfDatas
					for(int m = 0; m < numOfDatas; m++) {

						//Create the featureName
						std::string featureName = "Feature " + ss3.str();

						//Create LWRData at featureName
						std::shared_ptr<LWRData> data (new LWRData(featureName));
						data.get()->setValue(j*.1+l*(m+1) * .1);

						//Set the position at timestep for the data
						assembly.get()->getLWRRodDataProviderAtLocation(j, k).get()->addData(data, t);

					}

				}

			}
		}
	}


	//Clone it repeatedly
	for(int i = 0; i < numOfAssemblies; i++) {

		//If you want to create another FuelAssembly, this can be achieved by using the copying constructor.
		std::shared_ptr<ICE_DS::Identifiable> identityFuelAssembly = assembly.get()->clone();

		//The cloned object is a DEEP COPIED object.  If it is not, please contact us ASAP!!!
		//You will have to cast it out of it's interface and "up" cast to the more defined structure
		std::shared_ptr<FuelAssembly>thetaFuelAssembly = std::dynamic_pointer_cast<FuelAssembly>(identityFuelAssembly);

		std::stringstream ss0;

		ss0 << i;

		thetaFuelAssembly.get()->setName("Fuel Assembly " + ss0.str());

		reactor.get()->addAssembly(Fuel, thetaFuelAssembly);
	}

	return reactor;

	//end-user-code
}

std::shared_ptr<PressurizedWaterReactor> TestDriver::oldMethodSetupData(int numOfAssemblies, int numOfFeatures, int numOfDatas, int numOfTimesteps, int reactorSize, int assemblySize) {

	//begin-user-code

	//begin-user-code

		std::shared_ptr<PressurizedWaterReactor> reactor (new PressurizedWaterReactor(reactorSize));


		//Make an assembly
		std::shared_ptr<FuelAssembly> assembly(new FuelAssembly(assemblySize));

		//Create rods.  There should only be 3 added
		for(int j = 0; j < 3; j++) {


		}

		//Setup Feature and LWRData for each position in the assembly for each time step

		for(int t = 0; t < numOfTimesteps; t++) {
			for(int j = 0; j < assemblySize; j++) {
				for(int k = 0; k < assemblySize; k++) {

					//Get the indexes iterated into strings for later
					std::stringstream ss1, ss2, ss3, ss4;
					ss1 << j;
					ss2 << k;

					if(t == 0) {

						//Name of the LWRRod
						std::stringstream ss1, ss2;
						ss1 << j;
						ss2 << k;
						//Add an integer value to the end of the rod.  Rods should always be uniquely name within each assembly.
						std::string rodName = "LWRRodAlpha " + ss1.str() + " " + ss2.str();

						//Create a LWRRod.  This will name the rod, add a custom material, add pressure, and create a new stack.  Please use the parameterized constructors when possible in order to increase coding productivity
						std::shared_ptr<MaterialBlock> mBlock (new MaterialBlock());
						std::shared_ptr< std::vector < std::shared_ptr < MaterialBlock > > > blocks ( new std::vector < std::shared_ptr< MaterialBlock> >());
						blocks.get()->push_back(mBlock);

						//Create a LWRRod.  This will name the rod, add a custom material, add pressure, and create a new stack.  Please use the parameterized constructors when possible in order to increase coding productivity
						std::shared_ptr<LWRRod> rod (new LWRRod(rodName, std::shared_ptr<Material> (new Material("LWRRod's Material")), ((double) (j * .1)), blocks));

						//Add rod
						assembly.get()->addLWRRod(rod);
						//set the location for rod 1
						assembly.get()->setLWRRodLocation(rodName, j, k);
					}


					//Iterate over the numOfFeatures
					for(int l = 0; l < numOfFeatures; l++) {
						ss3 << l;
						//Iterate over the numOfDatas
						for(int m = 0; m < numOfDatas; m++) {

							//Create the featureName
							std::string featureName = "Feature " + ss3.str();

							//Create LWRData at featureName
							std::shared_ptr<LWRData> data (new LWRData(featureName));
							data.get()->setValue(j*.1+l*(m+1) * .1);

							//Set the position at timestep for the data
							assembly.get()->getLWRRodByLocation(j, k).get()->addData(data, t);

						}

					}

				}
			}
		}


		//Clone it repeatedly
		for(int i = 0; i < numOfAssemblies; i++) {

			//If you want to create another FuelAssembly, this can be achieved by using the copying constructor.
			std::shared_ptr<ICE_DS::Identifiable> identityFuelAssembly = assembly.get()->clone();

			//The cloned object is a DEEP COPIED object.  If it is not, please contact us ASAP!!!
			//You will have to cast it out of it's interface and "up" cast to the more defined structure
			std::shared_ptr<FuelAssembly>thetaFuelAssembly = std::dynamic_pointer_cast<FuelAssembly>(identityFuelAssembly);

			std::stringstream ss0;

			ss0 << i;

			thetaFuelAssembly.get()->setName("Fuel Assembly " + ss0.str());

			reactor.get()->addAssembly(Fuel, thetaFuelAssembly);
		}

		return reactor;

		//end-user-code

	//end-user-code

}

