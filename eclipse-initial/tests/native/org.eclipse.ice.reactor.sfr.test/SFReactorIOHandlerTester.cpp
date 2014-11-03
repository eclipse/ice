/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
#define BOOST_TEST_MODULE Regression
#include <boost/test/included/unit_test.hpp>
#include <SFReactorIOHandler.h>

#include <SFReactorFactory.h>
#include <unistd.h>

using namespace ICE_SFReactor;

BOOST_AUTO_TEST_SUITE(SFReactorIOHandlerTester_testSuite)

BOOST_AUTO_TEST_CASE(checkWrite) {
	// begin-user-code

	// Constants for this test.
	int reactorSize = 0;	         // Use the default.
	int assemblySize = 0;	         // Use the default.
	int nAssemblies = 7;
	int nAssemblyComponents = 5;
	int nAxialLevels = 3;
	bool randomData = false;         // Use fixed data and properties.
	bool randomLocations = false;    // Use fixed component locations.

	// Input/Output files for this test.
	std::string directory = "ICEIOTestDirectory/";
	mkdir(directory.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);
	std::string testInputPath = directory + "fromJava.h5";
	std::string testOutputPath = directory + "fromCPP.h5";

	// Create a factory for generating populated reactors.
	SFReactorFactory factory;

	// Create an IO handler for writing/reading the reactor.
	SFReactorIOHandler handler;

	// Declare the reactors. One will be generated; the other will be read.
	std::shared_ptr<SFReactor> reactor;
	std::shared_ptr<SFReactor> loadedReactor;

	// Generate a reactor with no random info.
	reactor = factory.generatePopulatedFullCoreReactor(reactorSize, assemblySize, nAssemblies, nAssemblyComponents, nAxialLevels, 0, randomData, randomLocations);
	reactor->setName("Arcturus");

	// Write the output file from the generated reactor.
	handler.writeHDF5(reactor, testOutputPath);

	return;
	// end-user-code
}

BOOST_AUTO_TEST_CASE(checkWriteAndRead) {
	// begin-user-code

	// Constants for this test.
	int reactorSize = 0;	        // Use the default.
	int assemblySize = 0;	        // Use the default.
	int nAssemblies = 5;
	int nAssemblyComponents = 10;
	int nAxialLevels = 3;
	bool randomData = true;		    // Randomize the data and properties.
	bool randomLocations = true;    // Randomize the component locations.

	// Input/Output files for this test.
	std::string directory = "ICEIOTestDirectory/";
	mkdir(directory.c_str(), S_IRWXU | S_IRWXG | S_IROTH | S_IXOTH);
	std::string path = directory + "SFReactorIOHandlerTester.h5";

	// Create a factory for generating populated reactors.
	SFReactorFactory factory;

	// Create an IO handler for writing/reading the reactor.
	SFReactorIOHandler handler;

	// Set up some seeds.
//	std::vector<int> seeds { 0, 3, 42, 1337 };
	std::vector<int> seeds { 1337 };

	// Run the test for each seed.
	for (int s = 0; s < seeds.size(); s++) {
		int seed = seeds[s];

		// Generate a reactor, an identical but separate copy of it, and a
		// reactor to read written data into.
		std::shared_ptr<SFReactor> reactor;
		std::shared_ptr<SFReactor> backupReactor;
		std::shared_ptr<SFReactor> loadedReactor;

		// Construct a reactor based on the seed.
		reactor = factory.generatePopulatedFullCoreReactor(reactorSize, assemblySize, nAssemblies, nAssemblyComponents, nAxialLevels, seed, randomData, randomLocations);
		backupReactor = factory.generatePopulatedFullCoreReactor(reactorSize, assemblySize, nAssemblies, nAssemblyComponents, nAxialLevels, seed, randomData, randomLocations);

		reactor->setName("Arcturus");
		backupReactor->setName("Arcturus");

		std::cout << "SFReactorIOHandlerTester message: Random reactor generated for seed " << seed << std::endl;

		// Make sure the reactor and backup reactor are completely the same.
		BOOST_REQUIRE_EQUAL(true, *reactor == *backupReactor);

		// Perform the write operation.
		handler.writeHDF5(reactor, path);

		std::cout << "SFReactorIOHandlerTester message: Writing completed for seed " << seed << std::endl;

		// Make sure the reactor was not modified in any way.
		BOOST_REQUIRE_EQUAL(true, *reactor == *backupReactor);

		// Perform the read operation.
		loadedReactor = handler.readHDF5(path);

		std::cout << "SFReactorIOHandlerTester message: Reading completed for seed " << seed << std::endl;

		// Make sure all of the reactors are equivalent but separate.
		BOOST_REQUIRE_EQUAL(true, *reactor == *backupReactor);
		BOOST_REQUIRE_EQUAL(true, *reactor == *loadedReactor);

		std::cout << "SFReactorIOHandlerTester message: Writing/reading tests completed successfully for seed " << seed << std::endl;
	}

	return;
	// end-user-code
}

BOOST_AUTO_TEST_SUITE_END()
