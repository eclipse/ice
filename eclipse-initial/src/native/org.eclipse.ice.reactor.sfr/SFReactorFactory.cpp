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

#include "SFReactorFactory.h"

//#include <limits> // Used in the C++11-style RNG code.

#include "AssemblyType.h"
#include "assembly/PinType.h"
#include "assembly/PinAssembly.h"
#include "assembly/SFRPin.h"
#include "assembly/ReflectorAssembly.h"
#include "assembly/SFRRod.h"

#include "Material.h"
#include "MaterialBlock.h"
#include "assembly/Ring.h"

using namespace ICE_SFReactor;


// FIXME - g++ 4.4 does not appear to support C++11-style random number
// generation. I have left the C++11 RNG code, but commented its lines out.
// For random number generation, we are using srand() and rand(). This should be
// sufficient for testing purposes.
std::shared_ptr<SFReactor> SFReactorFactory::generatePopulatedFullCoreReactor(
		int reactorSize, int assemblySize, int nAssemblies,
		int nAssemblyComponents, int nAxialLevels, long seed, bool randomData,
		bool randomLocations) {
	// begin-user-code

	// Check the sizes. If invalid, set the defaults.
	if (reactorSize <= 0)
		reactorSize = 17;
	if (assemblySize <= 0)
		assemblySize = 15;

	// Check the other properties.
	if (nAssemblies <= 0)
		nAssemblies = reactorSize * reactorSize;
	if (nAssemblyComponents <= 0)
		nAssemblyComponents = assemblySize * assemblySize;
	if (nAxialLevels <= 0)
		nAxialLevels = 49;

	int i, j, k;
	int nLocations;
	int row, column;

	// Timesteps for the data.
	std::vector<double> times { 0.0, 1.0, 2.0 };

	// Features to use.
	std::map<std::string, int> features;
	features["Feature 1"] = 1;
	features["Axial Feature 1"] = nAxialLevels;
	features["Feature 2"] = 1;
	features["Axial Feature 2"] = nAxialLevels;
	features["Feature 3"] = 3;

	// Initialize the random number generator. (These should function similarly
	// to Java's RNG).
//	std::default_random_engine generator((unsigned int) seed);
//	std::uniform_int_distribution<int> intDist(0, std::numeric_limits<int>::max());
//	std::uniform_int_distribution<int> boolDist(0, 1);
//	std::uniform_real_distribution<double> doubleDist(0, 1);
	srand(seed);

	// Initialize the reactor.
	std::shared_ptr<SFReactor> reactor = std::make_shared<SFReactor>(reactorSize);

	// Materials that we can use in the reactor.
	std::shared_ptr<Material> helium = std::make_shared<Material>("He");
	helium->setDescription("Helium");
	std::shared_ptr<Material> steel = std::make_shared<Material>("SS-316");
	steel->setDescription("Stainless Steel");
	std::shared_ptr<Material> uraniumOxide = std::make_shared<Material>("UO2");
	uraniumOxide->setDescription("Uranium Oxide");
	std::shared_ptr<Material> boronCarbide = std::make_shared<Material>("B4C");
	boronCarbide->setDescription("Boron Carbide");

	// Add data to the reactor.
//	addDataToComponent(reactor, features, times, randomData, generator, doubleDist);
	addDataToComponent(reactor, features, times, randomData);

	// For each assembly type:
	std::vector<AssemblyType> assemblyTypes { Fuel, Control, Reflector, Shield, Test };
	std::vector<std::string> assemblyStrings { "Fuel Assembly", "Control Assembly", "Reflector Assembly", "Shield Assembly", "Test Assembly" };
	for (int at = 0; at < assemblyTypes.size(); at++) {
		AssemblyType assemblyType = assemblyTypes[at];
		std::string assemblyString = assemblyStrings[at];

		// Generate random assemblies.
		for (i = 0; i < nAssemblies; i++) {

			/* ---- Create an assembly and add it to the reactor. ---- */
			// The assembly. Its name and ID should be based on i.
			std::shared_ptr<SFRAssembly> assembly;
			std::shared_ptr<PinAssembly> pinAssembly;
			std::shared_ptr<ReflectorAssembly> reflectorAssembly;

			std::string name = std::to_string((long long int) i);

			// Create a new assembly.
			if (assemblyType == Reflector) {
				reflectorAssembly = std::make_shared<ReflectorAssembly>(name, assemblySize);
				assembly = reflectorAssembly;
			} else  {
				PinType pinType;
				if (assemblyType == Control) {
//					pinType = ((randomData && boolDist(generator)) || (!randomData && i % 2 == 0) ? PrimaryControl : SecondaryControl);
					pinType = ((randomData && rand() % 2) || (!randomData && i % 2 == 0) ? PrimaryControl : SecondaryControl);
				} else {
//					pinType = ((randomData && boolDist(generator)) || (!randomData && i % 2 == 0) ? PrimaryControl : SecondaryControl);
					pinType = ((randomData && rand() % 2) || (!randomData && i % 2 == 0) ? InnerFuel : BlanketFuel);
				}
				pinAssembly = std::make_shared<PinAssembly>(name, assemblySize, pinType);
				assembly = pinAssembly;
			}
			assembly->setId(i);

			// Add data to the assembly.
//			addDataToComponent(assembly, features, times, randomData, generator, doubleDist);
			addDataToComponent(assembly, features, times, randomData);

			// Add the assembly to the reactor.
			reactor->addAssembly(assemblyType, assembly);

			// Set the assembly's location.
			if (!randomLocations) {
				reactor->setAssemblyLocation(assemblyType, name, i / reactorSize, i % reactorSize);
			} else {
				// Set some random locations for the assembly.
				//nLocations = generator.nextInt(reactorSize * reactorSize) + 1;
				nLocations = 5;
				for (j = 0; j < nLocations; j++) {
					// Pick a random location for the assembly.
//					row = intDist(generator) % reactorSize;
//					column = intDist(generator) % reactorSize;
					row = rand() % reactorSize;
					column = rand() % reactorSize;

					// Set the assembly location.
					reactor->setAssemblyLocation(assemblyType, name, row, column);
				}
			}
			/* ------------------------------------------------------- */

			/* ---- Create rods/pins and add them to the assembly. ---- */
			// Generate assembly components (rods/pins).
			for (j = 0; j < nAssemblyComponents; j++) {

				// The rod/pin. Its name and ID should be based on j.
				std::shared_ptr<SFRComponent> component;
				name = std::to_string((long long int) j);

				// Create a new rod/pin. We also need to add them to the
				// assembly here (because Reflector/PinAssemblies add them
				// via other operations).
				if (assemblyType == Reflector) {
					// Initialize a rod.
					std::shared_ptr<SFRRod> rod = std::make_shared<SFRRod>(name);
					rod->setId(j);
					component = rod;

					// Occasionally add some reflector rods with different
					// radii.
//					if ((randomData && intDist(generator) % 5 == 0) || (!randomData && j % 5 == 0)) {
					if ((randomData && rand() % 5 == 0) || (!randomData && j % 5 == 0)) {
						std::shared_ptr<Ring> reflector = std::make_shared<Ring>();
						reflector->setMaterial(steel);
						reflector->setHeight(0.0);
						reflector->setInnerRadius(0.0);

						// We don't want the outer radius to be too small.
//						double ratio = (randomData ? doubleDist(generator) : (double) j / (double) nAssemblyComponents);
						double ratio = (randomData ? (double) (rand() * 10000) / 10000.0 : (double) j / (double) nAssemblyComponents);
						if (ratio < 0.25)
							ratio = 0.25;
						reflector->setOuterRadius(ratio * 26.666);
					}
				} else {
					// Initialize a pin.
					std::shared_ptr<SFRPin> pin = std::make_shared<SFRPin>(name);
					pin->setId(j);
					component = pin;

					PinType pinType = pinAssembly->getPinType();

					// Change the structure of the pin based on the assembly
					// type.
					if (assemblyType == Control) {
						// Control pins get boron carbide and no data
						// (although they can have data).

						// Set up the basic block materials.
						std::shared_ptr<Ring> cladding = std::make_shared<Ring>("Cladding", steel, 0, 16.25, 17.5);
						if (pinType == SecondaryControl)
							cladding->setOuterRadius(17);
						std::shared_ptr<Ring> fillGas = std::make_shared<Ring>("Fill Gas", helium, 0, 13.3333, 16.25);
						std::shared_ptr<Ring> fuel = std::make_shared<Ring>("Control Fuel", boronCarbide, 0, 0, 13.3333);

						// Construct the material block set.
						std::set<std::shared_ptr<MaterialBlock>> materialBlocks;

						// Construct the first block.
						std::shared_ptr<MaterialBlock> block = std::make_shared<MaterialBlock>();
						block->setVertPosition(0);
						block->addRing(cladding);
						block->addRing(fillGas);
						block->addRing(fuel);
						materialBlocks.insert(block);

						// Set the pin attributes.
						pin->setCladding(cladding);
						pin->setFillGas(helium);
						pin->setMaterialBlocks(materialBlocks);
					} else {
						// Fuel pins get uranium oxide and data.

						// Set up the basic block materials.
						std::shared_ptr<Ring> cladding = std::make_shared<Ring>("Cladding", steel, 0, 16.25, 17.5);
						if (pinType == BlanketFuel)
							cladding->setOuterRadius(17);
						std::shared_ptr<Ring> fillGas = std::make_shared<Ring>("Fill Gas", helium, 0, 13.3333, 16.25);
						std::shared_ptr<Ring> fuel = std::make_shared<Ring>("Fuel", uraniumOxide, 0, 0, 13.3333);

						// Construct the material block set.
						std::set<std::shared_ptr<MaterialBlock>> materialBlocks;

						// Construct the fuel block.
						std::shared_ptr<MaterialBlock> block = std::make_shared<MaterialBlock>();
						block->setVertPosition(0);
						block->addRing(cladding);
						block->addRing(fillGas);
						block->addRing(fuel);

						// For blanket fuel, sandwich the fuel with some
						// boron carbide (just for testing purposes).
						if (pinType == BlanketFuel) {
							// The main fuel should comprise the middle 50%
							// of the pin.
							block->setVertPosition(0.25);
							materialBlocks.insert(block);

							std::shared_ptr<Ring> blanketFuel = std::make_shared<Ring>("Blanket Fuel", boronCarbide, 0, 0, 13.0);
							fillGas = std::dynamic_pointer_cast<Ring>(fillGas->clone());

							// The control fuel should comprise the bottom
							// 25% of the pin.
							block = std::make_shared<MaterialBlock>();
							block->setVertPosition(0);
							block->addRing(cladding);
							fillGas->setInnerRadius(13.0);
							block->addRing(fillGas);
							block->addRing(blanketFuel);
							materialBlocks.insert(block);

							// The remaining control fuel should comprise
							// the top 25% of the pin.
							block = std::dynamic_pointer_cast<MaterialBlock>(block->clone());
							block->setVertPosition(0.75);
						}
						// Add the last block that was modified.
						materialBlocks.insert(block);

						// Set the pin attributes.
						pin->setCladding(cladding);
						pin->setFillGas(helium);
						pin->setMaterialBlocks(materialBlocks);
					}
				}

				// Add data to the component.
//				addDataToComponent(component, features, times, randomData, generator, doubleDist);
				addDataToComponent(component, features, times, randomData);

				// Add the rod/pin to the assembly and set its location.
				if (assemblyType == Reflector)
					reflectorAssembly->addRod(std::dynamic_pointer_cast<SFRRod>(component));
				else
					pinAssembly->addPin(std::dynamic_pointer_cast<SFRPin>(component));

				// Set the pin/rod location.
				if (!randomLocations) {
					if (assemblyType == Reflector)
						reflectorAssembly->setRodLocation(name, j / assemblySize, j % assemblySize);
					else
						pinAssembly->setPinLocation(name, j / assemblySize, j % assemblySize);
				} else {
					// Set some random locations for the rod/pin.
//						nLocations = generator.nextInt(assemblySize) + 1;
					nLocations = 5;
					for (k = 0; k < nLocations; k++) {
						// Pick a random location for the rod/pin.
//						row = intDist(generator) % assemblySize;
//						column = intDist(generator) % assemblySize;
						row = rand() % assemblySize;
						column = rand() % assemblySize;

						// Set the rod/pin location.
						if (assemblyType == Reflector)
							reflectorAssembly->setRodLocation(name, row, column);
						else
							pinAssembly->setPinLocation(name, row, column);
					}
				}
			}
			/* -------------------------------------------------------- */

			/* ---- Add GridData to the assembly. ---- */
			// Add data to each grid position.
			for (int dataRow = 0; dataRow < assemblySize; dataRow++) {
				for (int dataColumn = 0; dataColumn < assemblySize; dataColumn++) {
					std::shared_ptr<SFRComponent> provider;
					if (assemblyType == Reflector)
						provider = (std::dynamic_pointer_cast<ReflectorAssembly>(assembly))->getDataProviderByLocation(dataRow, dataColumn);
					else
						provider = (std::dynamic_pointer_cast<PinAssembly>(assembly))->getDataProviderByLocation(dataRow, dataColumn);
					if (provider) {
//						addDataToComponent(provider, features, times, randomData, generator, doubleDist);
						addDataToComponent(provider, features, times, randomData);
					}
				}
			}
			/* --------------------------------------- */
		}
	}

	return reactor;
	// end-user-code
}

//void SFReactorFactory::addDataToComponent(std::shared_ptr<SFRComponent> component, std::map<std::string, int> features, std::vector<double> times, bool randomData, std::default_random_engine & generator, std::uniform_real_distribution<double> & distribution) {
void SFReactorFactory::addDataToComponent(std::shared_ptr<SFRComponent> component, std::map<std::string, int> features, std::vector<double> times, bool randomData) {
	std::map<std::string, int>::iterator iter;
	for (iter = features.begin(); iter != features.end(); iter++) {

		std::string feature = iter->first;
		int nAxialLevels = iter->second;

		for (int t = 0; t < times.size(); t++) {
			double time = times[t];

			// Add the correct number of data points.
			for (int k = 0; k < nAxialLevels; k++) {
				std::shared_ptr<SFRData> data = std::make_shared<SFRData>(feature);

				// Set a random value.
//				data->setValue(randomData ? distribution(generator) : 1.0 / (double) k);
				data->setValue(randomData ? (double) (rand() % 10000) / 10000.0 : 1.0 / (double) (k+1));

				// Set the position (z-only).
				std::vector<double> position { 0.0, 0.0, (double) k };
				data->setPosition(position);

				// Set some different units (although typically they are the
				// same).
				data->setUnits("Units # " + std::to_string((long long int) k));

				// Add the data to the component.
				component->addData(data, time);
			}
		}
	}

	return;
}
