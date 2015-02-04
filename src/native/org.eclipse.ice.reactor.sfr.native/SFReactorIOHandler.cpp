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
#include "SFReactorIOHandler.h"

#include <cstdlib>
#include <iostream>
#include <stack>

#include <UtilityOperations.h>

#include "AssemblyType.h"
#include "assembly/PinType.h"
#include "assembly/PinAssembly.h"
#include "assembly/SFRPin.h"
#include "assembly/ReflectorAssembly.h"
#include "assembly/SFRRod.h"

#include "Material.h"
#include "MaterialBlock.h"

using namespace ICE_SFReactor;

// FIXME - Anywhere you see a std::to_string() operation, we cast an int or
// double to long long int or long double for g++ 4.4 support.

std::shared_ptr<SFReactor> SFReactorIOHandler::readHDF5(std::string path) {
		// begin-user-code
		
		// The SFReactor that will receive the data from the file.
		std::shared_ptr<SFReactor> reactor;

		// Trim the whitespace from the path.
		path = UtilityOperations::trim_copy(path, " \f\n\r\t\v");

		// Check the parameters.
		if (path.empty())
			return reactor;

		// The status of the previous HDF5 operation. Generally, if it is
		// negative, there was some error.
		int status;

		// Other IDs for HDF5 components.
		int fileId, groupId;

		// A stack representing the currently opened Groups.
		std::stack<int> groupIds;

//		Integer[] intBuffer = new Integer[1];
//		double[] doubleBuffer = new double[1];

		try {
			// Open the H5 file with read-only access.
			status = H5Fopen(path.c_str(), H5F_ACC_RDONLY, H5P_DEFAULT);
			if (status < 0)
				throwException("Opening file \"" + path + "\"", status);
			fileId = status;

			// Currently, we only support a single reactor.
			groupId = openGroup(fileId, "/SFReactor");
			groupIds.push(groupId);

			// Get the size of the reactor in the file and initialize it.
			int size = readIntegerAttribute(groupId, "size");
			reactor = std::make_shared<SFReactor>(size);

			/* ---- Read the reactor's Attributes. ---- */
			// Attributes inherited from SFRComponent.
			readSFRComponent(reactor, groupId);

			// Attributes inherited from SFRComposite.
			// none

			// SFReactor-specific Attributes.
			// size has already been read.
			reactor->setLatticePitch(readDoubleAttribute(groupId, "latticePitch"));
			reactor->setOuterFlatToFlat(readDoubleAttribute(groupId, "outerFlatToFlat"));
			/* ---------------------------------------- */

			/* ---- Read the reactor's Pin Assemblies. ---- */
			std::vector<AssemblyType> assemblyTypes { Fuel, Control, Shield, Test };
			std::vector<std::string> assemblyStrings { "Fuel Assembly", "Control Assembly", "Shield Assembly", "Test Assembly" };
			for (int at = 0; at < assemblyTypes.size(); at++) {
				AssemblyType assemblyType = assemblyTypes[at];
				std::string assemblyString = assemblyStrings[at];

				// Open the Group for this assembly type.
				groupId = openGroup(groupIds.top(), assemblyString);
				groupIds.push(groupId);

				// Loop over the child Groups in this assembly type's Group.
				// These Groups should have the assembly names.
				std::vector<std::string> assemblyNames = getChildNames(groupId, H5O_TYPE_GROUP);
				for (int an = 0; an < assemblyNames.size(); an++) {
					std::string assemblyName = assemblyNames[an];

					// Open the Group for this assembly.
					groupId = openGroup(groupIds.top(), assemblyName);
					groupIds.push(groupId);

					// Read the name, pinType, and size of the assembly.
					PinType pinType = static_cast<PinType>(readIntegerAttribute(groupId, "pinType"));
					size = readIntegerAttribute(groupId, "size");

					// Initialize the assembly.
					std::shared_ptr<PinAssembly> assembly = std::make_shared<PinAssembly>(assemblyName, size, pinType);

					/* --- Read the assembly's Attributes. --- */
					// Attributes inherited from SFRComponent.
					readSFRComponent(assembly, groupId);

					// Attributes inherited from SFRComposite.
					// none

					// Attributes inherited from SFRAssembly.
					// size has already been read.
					assembly->setDuctThickness(readDoubleAttribute(groupId, "ductThickness"));

					// PinAssembly-specific Attributes.
					assembly->setPinPitch(readDoubleAttribute(groupId, "pinPitch"));
					// pinType has already been read.
					assembly->setInnerDuctFlatToFlat(readDoubleAttribute(groupId, "innerDuctFlatToFlat"));
					assembly->setInnerDuctThickness(readDoubleAttribute(groupId, "innerDuctThickness"));
					/* --------------------------------------- */

					/* --- Read the assembly's reactor locations. --- */
					// Add the assembly to the reactor.
					reactor->addAssembly(assemblyType, assembly);

					// Set the assembly's locations in the reactor.
					std::vector<int> assemblyLocations = readLocationData(groupId);
					for (int l = 0; l < assemblyLocations.size(); l++) {
						int location = assemblyLocations[l];
						reactor->setAssemblyLocation(assemblyType, assemblyName, location / reactor->getSize(), location % reactor->getSize());
					}
					/* ---------------------------------------------- */

					/* --- Read the assembly's pins. --- */
					// Open the Group that holds the pins.
					groupId = openGroup(groupId, "Pins");
					groupIds.push(groupId);

					// Loop over the child Groups in this assembly's Pins Group.
					// These Groups should have the pin names.
					std::vector<std::string> pinNames = getChildNames(groupId, H5O_TYPE_GROUP);
					for (int pn = 0; pn < pinNames.size(); pn++) {
						std::string pinName = pinNames[pn];

						// Open the Group for this pin.
						groupId = openGroup(groupIds.top(), pinName);
						groupIds.push(groupId);

						// So we don't waste time creating default properties
						// for the pin that will soon be replaced, we should
						// first read in the physical structure of the pin.
						std::shared_ptr<Ring> cladding;
						std::shared_ptr<Material> fillGas;
						std::set<std::shared_ptr<MaterialBlock>> materialBlocks;

						/* -- Read in the cladding. -- */
						groupId = openGroup(groupId, "cladding");
						cladding = readRing(groupId);
						closeGroup(groupId);
						/* --------------------------- */

						/* -- Read in the fill gas. -- */
						// Initialize the container for the fill gas.
						fillGas = std::make_shared<Material>();

						// Read the material's Attributes.
						groupId = openGroup(groupIds.top(), "fillGas");
						readSFRComponent(fillGas, groupId);
						closeGroup(groupId);
						/* --------------------------- */

						/* -- Read in the material blocks. -- */
						// Open the material blocks Group.
						groupId = openGroup(groupIds.top(), "materialBlocks");
						groupIds.push(groupId);

						// Loop over the child Groups of materialBlocks. They
						// correspond to individual MaterialBlocks in the
						// TreeSet.
						std::vector<std::string> groupNames = getChildNames(groupIds.top(), H5O_TYPE_GROUP);
						for (int gn = 0; gn < groupNames.size(); gn++) {
							std::string groupName = groupNames[gn];

							// Initialize a MaterialBlock.
							std::shared_ptr<MaterialBlock> block = std::make_shared<MaterialBlock>();

							// Open the MaterialBlock's Group.
							groupId = openGroup(groupIds.top(), groupName);
							groupIds.push(groupId);

							// Read the block's SFRComponent Attributes.
							readSFRComponent(block, groupId);

							// Read the block's other Attributes.
							block->setVertPosition(readDoubleAttribute(groupId, "vertPosition"));

							/* - Read the block's rings. - */

							// Open the Rings Group.
							groupId = openGroup(groupId, "Rings");
							groupIds.push(groupId);

							// Loop over the child Groups of the block. They
							// correspond to individual rings in the block's
							// TreeSet.
							std::vector<std::string> rGroupNames = getChildNames(groupId, H5O_TYPE_GROUP);
							for (int rgn = 0; rgn < rGroupNames.size(); rgn++) {
								std::string ringGroupName = rGroupNames[rgn];
								groupId = openGroup(groupIds.top(), ringGroupName);
								block->addRing(readRing(groupId));
								closeGroup(groupId);
							}

							// Close the Rings Group.
							closeGroup(groupIds.top());
							groupIds.pop();
							/* --------------------------- */

							// Close the MaterialBlock's Group.
							closeGroup(groupIds.top());
							groupIds.pop();

							// Add the block to materialBlocks (TreeSet).
							materialBlocks.insert(block);
						}

						// Close the material blocks Group.
						closeGroup(groupIds.top());
						groupIds.pop();
						/* ---------------------------------- */

						// Initialize the pin.
						std::shared_ptr<SFRPin> pin = std::make_shared<SFRPin>(pinName, fillGas, materialBlocks, cladding);

						// Get the pin's groupId.
						groupId = groupIds.top();

						/* -- Read in the Pin's other Attributes. -- */
						// Attributes inherited from SFRComponent.
						readSFRComponent(pin, groupId);

						// Pin-specific Attributes.
						// none
						/* ----------------------------------------- */

						/* -- Read the pin's assembly locations. -- */
						// Add the pin to the assembly.
						assembly->addPin(pin);

						// Set the pin's locations in the assembly.
						std::vector<int> pinLocations = readLocationData(groupId);
						for (int l = 0; l < pinLocations.size(); l++) {
							int location = pinLocations[l];
							assembly->setPinLocation(pinName, location / size, location % size);
						}
						/* ---------------------------------------- */

						// Close the Group for this pin.
						closeGroup(groupIds.top());
						groupIds.pop();
					}

					// Close the Group that holds the pins.
					closeGroup(groupIds.top());
					groupIds.pop();
					/* --------------------------------- */

					/* --- Read the assembly's GridData. --- */
					groupId = groupIds.top();

					std::vector<std::shared_ptr<SFRComponent>> gridData;
					for (int row = 0; row < assembly->getSize(); row++) {
						for (int column = 0; column < assembly->getSize(); column++) {
							gridData.push_back(assembly->getDataProviderByLocation(row, column));
						}
					}
					readGridData(gridData, groupId);
					/* -------------------------------------- */

					// Close the Group for this assembly.
					closeGroup(groupIds.top());
					groupIds.pop();
				}

				// Close the Group for this assembly type.
				closeGroup(groupIds.top());
				groupIds.pop();
			}
			/* -------------------------------------------- */

			/* ---- Read the reactor's Reflector Assemblies. ---- */
			// Open the Group for this assembly type.
			groupId = openGroup(groupIds.top(), "Reflector Assembly");
			groupIds.push(groupId);

			// Loop over the child Groups in this assembly type's Group.
			// These Groups should have the assembly names.
			std::vector<std::string> assemblyNames = getChildNames(groupId, H5O_TYPE_GROUP);
			for (int an = 0; an < assemblyNames.size(); an++) {
				std::string assemblyName = assemblyNames[an];

				// Open the Group for this assembly.
				groupId = openGroup(groupIds.top(), assemblyName);
				groupIds.push(groupId);

				// Read the name, rodType, and size of the assembly.
				size = readIntegerAttribute(groupId, "size");

				// Initialize the assembly.
				std::shared_ptr<ReflectorAssembly> assembly = std::make_shared<ReflectorAssembly>(assemblyName, size);

				/* --- Read the assembly's Attributes. --- */
				// Attributes inherited from SFRComponent.
				readSFRComponent(assembly, groupId);

				// Attributes inherited from SFRComposite.
				// none

				// Attributes inherited from SFRAssembly.
				// size has already been read.
				assembly->setDuctThickness(readDoubleAttribute(groupId, "ductThickness"));

				// ReflectorAssembly-specific Attributes.
				assembly->setRodPitch(readDoubleAttribute(groupId, "rodPitch"));
				/* --------------------------------------- */

				/* --- Read the assembly's reactor locations. --- */
				// Add the assembly to the reactor.
				reactor->addAssembly(Reflector, assembly);

				// Set the assembly's locations in the reactor.
				std::vector<int> assemblyLocations = readLocationData(groupId);
				for (int l = 0; l < assemblyLocations.size(); l++) {
					int location = assemblyLocations[l];
					reactor->setAssemblyLocation(Reflector, assemblyName, location / reactor->getSize(), location % reactor->getSize());
				}
				/* ---------------------------------------------- */

				/* --- Read the assembly's rods. --- */
				// Open the Group that holds the rods.
				groupId = openGroup(groupId, "Rods");
				groupIds.push(groupId);

				// Loop over the child Groups in this assembly's Rods Group.
				// These Groups should have the rod names.
				std::vector<std::string> rodNames = getChildNames(groupId, H5O_TYPE_GROUP);
				for (int rn = 0; rn < rodNames.size(); rn++) {
					std::string rodName = rodNames[rn];

					// Open the Group for this rod.
					groupId = openGroup(groupIds.top(), rodName);
					groupIds.push(groupId);

					// Initialize the rod.
					std::shared_ptr<SFRRod> rod = std::make_shared<SFRRod>(rodName);

					/* -- Read in the Rod's other Attributes. -- */
					// Attributes inherited from SFRComponent.
					readSFRComponent(rod, groupId);

					// Rod-specific Attributes.
					// none
					/* ----------------------------------------- */

					/* -- Read in the reflector. -- */
					groupId = openGroup(groupId, "reflector");
					rod->setReflector(readRing(groupId));
					closeGroup(groupId);
					/* --------------------------- */

					// Get the rod's groupId back.
					groupId = groupIds.top();

					/* -- Read the rod's assembly locations. -- */
					// Add the rod to the assembly.
					assembly->addRod(rod);

					// Set the rod's locations in the assembly.
					std::vector<int> rodLocations = readLocationData(groupId);
					for (int l = 0; l < rodLocations.size(); l++) {
						int location = rodLocations[l];
						assembly->setRodLocation(rodName, location / size, location % size);
					}
					/* ---------------------------------------- */

					// Close the Group for this rod.
					closeGroup(groupIds.top());
					groupIds.pop();
				}

				// Close the Group that holds the rods.
				closeGroup(groupIds.top());
				groupIds.pop();
				/* --------------------------------- */

				/* --- Read the assembly's GridData. --- */
				groupId = groupIds.top();

				std::vector<std::shared_ptr<SFRComponent>> gridData;
				for (int row = 0; row < assembly->getSize(); row++) {
					for (int column = 0; column < assembly->getSize(); column++) {
						gridData.push_back(assembly->getDataProviderByLocation(row, column));
					}
				}
				readGridData(gridData, groupId);
				/* -------------------------------------- */

				// Close the Group for this assembly.
				closeGroup(groupIds.top());
				groupIds.pop();
			}

			// Close the Group for this assembly type.
			closeGroup(groupIds.top());
			groupIds.pop();
			/* -------------------------------------------------- */

			// Close the reactor's Group.
			closeGroup(groupIds.top());
			groupIds.pop();

			// Close the H5file.
			status = H5Fclose(fileId);
			if (status < 0)
				throwException("Closing file \"" + path + "\"", status);

		} catch (const std::runtime_error & e) {
			std::cerr << e.what() << std::endl;
		}
		
		// Return the loaded SFReactor.
		return reactor;
		// end-user-code
}

void SFReactorIOHandler::writeHDF5(std::shared_ptr<SFReactor> reactor, std::string path) {
	// begin-user-code

	// Trim the whitespace from the path.
	path = UtilityOperations::trim_copy(path, " \f\n\r\t\v");

	// Check the parameters.
	if (!reactor || path.empty())
		return;

	// The status of the previous HDF5 operation. Generally, if it is
	// negative, there was some error.
	int status;

	// Other IDs for HDF5 components.
	int fileId, groupId;

	std::stack<int> groupIds;

	// Create and open the h5 file.
	try {

		// Create the H5 file. This should also open it with RW-access.
		status = H5Fcreate(path.c_str(), H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT);
		if (status < 0)
			throwException("Opening file \"" + path + "\"", status);
		fileId = status;

		// Create the Group for the reactor.
		groupId = createGroup(fileId, "/SFReactor");
		groupIds.push(groupId);

		/* ---- Write the reactor's Attributes. ---- */
		// Attributes inherited from SFRComponent.
		writeSFRComponent(reactor, groupId);

		// Attributes inherited from SFRComposite.
		// none

		// SFReactor-specific Attributes.
		writeIntegerAttribute(groupId, "size", reactor->getSize());
		writeDoubleAttribute(groupId, "latticePitch", reactor->getLatticePitch());
		writeDoubleAttribute(groupId, "outerFlatToFlat", reactor->getOuterFlatToFlat());
		/* ----------------------------------------- */

		/* ---- Write the reactor's Pin Assemblies. ---- */
		std::vector<AssemblyType> assemblyTypes { Fuel, Control, Shield, Test };
		std::vector<std::string> assemblyStrings { "Fuel Assembly", "Control Assembly", "Shield Assembly", "Test Assembly" };
		for (int t = 0; t < assemblyTypes.size(); t++) {
			AssemblyType assemblyType = assemblyTypes[t];

			// Create a Group for this assembly type.
			groupId = createGroup(groupIds.top(), assemblyStrings[t]);
			groupIds.push(groupId);

			std::vector<std::string> assemblyNames = reactor->getAssemblyNames(assemblyType);
			for (int a = 0; a < assemblyNames.size(); a++) {
				std::string assemblyName = assemblyNames[a];

				// Get the assembly object from the reactor.
				std::shared_ptr<PinAssembly> assembly = std::dynamic_pointer_cast<PinAssembly>(reactor->getAssemblyByName(assemblyType, assemblyName));

				// Create a Group for this assembly.
				groupId = createGroup(groupIds.top(), assemblyName);
				groupIds.push(groupId);

				/* --- Write the assembly's Attributes. --- */
				// Attributes inherited from SFRComponent.
				writeSFRComponent(assembly, groupId);

				// Attributes inherited from SFRComposite.
				// none

				// Attributes inherited from SFRAssembly.
				writeIntegerAttribute(groupId, "size", assembly->getSize());
				writeDoubleAttribute(groupId, "ductThickness", assembly->getDuctThickness());

				// PinAssembly-specific Attributes.
				writeDoubleAttribute(groupId, "pinPitch", assembly->getPinPitch());
				writeIntegerAttribute(groupId, "pinType", assembly->getPinType());
				writeDoubleAttribute(groupId, "innerDuctFlatToFlat", assembly->getInnerDuctFlatToFlat());
				writeDoubleAttribute(groupId, "innerDuctThickness", assembly->getInnerDuctThickness());
				/* ---------------------------------------- */

				/* --- Write the assembly's reactor locations. --- */
				writeLocationData(reactor->getAssemblyLocations(assemblyType, assemblyName), groupId);
				/* ----------------------------------------------- */

				// Create a Group to hold the pins.
				groupId = createGroup(groupIds.top(), "Pins");
				groupIds.push(groupId);

				/* --- Write the assembly's rods/pins. --- */
				std::vector<std::string> pinNames = assembly->getPinNames();
				for (int p = 0; p < pinNames.size(); p++) {
					std::string pinName = pinNames[p];

					// Get the pin object from the assembly.
					std::shared_ptr<SFRPin> pin = assembly->getPinByName(pinName);

					// Create a Group for the pin.
					groupId = createGroup(groupIds.top(), pinName);
					groupIds.push(groupId);

					/* -- Write the pin's Attributes. -- */
					// Attributes inherited from SFRComponent.
					writeSFRComponent(pin, groupId);

					// Pin-specific Attributes.
					// none
					/* --------------------------------- */

					/* -- Write the pin's physical properties. -- */
					// Material fillGas
					std::shared_ptr<Material> fillGas = pin->getFillGas();
					groupId = createGroup(groupIds.top(), "fillGas");
					groupIds.push(groupId);
					writeSFRComponent(fillGas, groupId);
					closeGroup(groupIds.top());
					groupIds.pop();

					// Ring cladding
					std::shared_ptr<Ring> cladding = pin->getCladding();
					groupId = createGroup(groupIds.top(), "cladding");
					groupIds.push(groupId);
					writeRing(cladding, groupId);
					closeGroup(groupIds.top());
					groupIds.pop();

					// TreeSet materialBlocks
					groupId = createGroup(groupIds.top(), "materialBlocks");
					groupIds.push(groupId);
					int i = 0;

					std::set<std::shared_ptr<MaterialBlock>> materialBlocks = pin->getMaterialBlocks();
					std::set<std::shared_ptr<MaterialBlock>>::iterator b;
					for (b = materialBlocks.begin(); b != materialBlocks.end(); b++) {
						std::shared_ptr<MaterialBlock> block = *b;

						// Create a Group for the MaterialBlock.
						groupId = createGroup(groupIds.top(), std::to_string((long long int) (i++)));
						groupIds.push(groupId);

						// Write the block's SFRComponent Attributes.
						writeSFRComponent(block, groupId);

						// Write the block's other Attributes.
						writeDoubleAttribute(groupId, "vertPosition", block->getVertPosition());

						// Create a Group to contain the Rings.
						groupIds.push(createGroup(groupId, "Rings"));

						// Write the block's rings.
						int j = 0;
						std::vector<std::shared_ptr<Ring>> rings = block->getRings();
						for (int r = 0; r < rings.size(); r++) {
							std::shared_ptr<Ring> ring = rings[r];

							groupId = createGroup(groupIds.top(), std::to_string((long long int) (j++)));
							groupIds.push(groupId);
							writeRing(ring, groupId);
							closeGroup(groupIds.top());
							groupIds.pop();
						}

						// Close the Group that contains the Rings.
						closeGroup(groupIds.top());
						groupIds.pop();

						// Close the Group for the MaterialBlock.
						closeGroup(groupIds.top());
						groupIds.pop();
					}
					closeGroup(groupIds.top());
					groupIds.pop();
					/* ------------------------------------------ */

					// Get the Group ID back (subgroups have been created).
					groupId = groupIds.top();

					/* -- Write the pin's assembly locations. -- */
					writeLocationData(assembly->getPinLocations(pinName), groupId);
					/* ----------------------------------------- */

					// Close the Group for the pin.
					closeGroup(groupIds.top());
					groupIds.pop();
				}
				/* --------------------------------------- */

				// Close the Group containing the pins.
				closeGroup(groupIds.top());
				groupIds.pop();

				/* --- Write the assembly's GridData. --- */
				groupId = groupIds.top();

				std::vector<std::shared_ptr<SFRComponent>> gridData;
				for (int row = 0; row < assembly->getSize(); row++) {
					for (int column = 0; column < assembly->getSize(); column++) {
						gridData.push_back(assembly->getDataProviderByLocation(row, column));
					}
				}
				writeGridData(gridData, groupId);
				/* -------------------------------------- */

				// Close this assembly Group.
				closeGroup(groupIds.top());
				groupIds.pop();
			}

			// Close the Group for this assembly type.
			closeGroup(groupIds.top());
			groupIds.pop();
		}
		/* --------------------------------------------- */

		/* ---- Write the reactor's Reflector Assemblies. ---- */
		// Create a Group for this assembly type.
		groupId = createGroup(groupIds.top(), "Reflector Assembly");
		groupIds.push(groupId);

		AssemblyType assemblyType = Reflector;
		std::vector<std::string> assemblyNames = reactor->getAssemblyNames(assemblyType);
		for (int an = 0; an < assemblyNames.size(); an++) {
			std::string assemblyName = assemblyNames[an];

			// Get the assembly object from the reactor.
			std::shared_ptr<ReflectorAssembly> assembly = std::dynamic_pointer_cast<ReflectorAssembly>(reactor->getAssemblyByName(assemblyType, assemblyName));

			// Create a Group for this assembly.
			groupId = createGroup(groupIds.top(), assemblyName);
			groupIds.push(groupId);

			/* --- Write the assembly's Attributes. --- */
			// Attributes inherited from SFRComponent.
			writeSFRComponent(assembly, groupId);

			// Attributes inherited from SFRComposite.
			// none

			// Attributes inherited from SFRAssembly.
			writeIntegerAttribute(groupId, "size", assembly->getSize());
			writeDoubleAttribute(groupId, "ductThickness", assembly->getDuctThickness());

			// ReflectorAssembly-specific Attributes.
			writeDoubleAttribute(groupId, "rodPitch", assembly->getRodPitch());
			/* ---------------------------------------- */

			/* --- Write the assembly's reactor locations. --- */
			writeLocationData(reactor->getAssemblyLocations(assemblyType, assemblyName), groupId);
			/* ----------------------------------------------- */

			// Create a Group to hold the pins.
			groupId = createGroup(groupIds.top(), "Rods");
			groupIds.push(groupId);

			/* --- Write the assembly's rods/pins. --- */
			std::vector<std::string> rodNames = assembly->getRodNames();
			for (int r = 0; r < rodNames.size(); r++) {
				std::string rodName = rodNames[r];

				// Get the rod object from the assembly.
				std::shared_ptr<SFRRod> rod = assembly->getRodByName(rodName);

				// Create a Group for the rod.
				groupId = createGroup(groupIds.top(), rodName);
				groupIds.push(groupId);

				/* -- Write the rod's Attributes. -- */
				// Attributes inherited from SFRComponent.
				writeSFRComponent(rod, groupId);

				// Rod-specific Attributes.
				// none
				/* --------------------------------- */

				/* -- Write the rod's physical properties. -- */
				// Ring reflector.
				std::shared_ptr<Ring> reflector = rod->getReflector();
				groupId = createGroup(groupIds.top(), "reflector");
				groupIds.push(groupId);
				writeRing(reflector, groupId);
				closeGroup(groupIds.top());
				groupIds.pop();
				/* ------------------------------------------ */

				// Get the Group ID back (subgroups have been created).
				groupId = groupIds.top();

				/* -- Write the rod's assembly locations. -- */
				writeLocationData(assembly->getRodLocations(rodName), groupId);
				/* ----------------------------------------- */

				// Close the Group for the rod.
				closeGroup(groupIds.top());
				groupIds.pop();
			}
			/* --------------------------------------- */

			// Close the Group containing the rods.
			closeGroup(groupIds.top());
			groupIds.pop();

			/* --- Write the assembly's GridData. --- */
			groupId = groupIds.top();

			std::vector<std::shared_ptr<SFRComponent>> gridData;
			for (int row = 0; row < assembly->getSize(); row++) {
				for (int column = 0; column < assembly->getSize(); column++) {
					gridData.push_back(assembly->getDataProviderByLocation(row, column));
				}
			}
			writeGridData(gridData, groupId);
			/* -------------------------------------- */

			// Close this assembly Group.
			closeGroup(groupIds.top());
			groupIds.pop();
		}

		// Close the Group for this assembly type.
		closeGroup(groupIds.top());
		groupIds.pop();
		/* --------------------------------------------------- */

		// Close the reactor Group.
		closeGroup(groupIds.top());
		groupIds.pop();

		// Close the H5file.
		status = H5Fclose(fileId);
		if (status < 0)
			throwException("Closing file \"" + path + "\"", status);

	} catch (const std::runtime_error & e) {
		std::cerr << e.what() << std::endl;
	}

	return;
	// end-user-code
}

int SFReactorIOHandler::createGroup(int parentId, std::string name) throw (std::runtime_error) {
	int status = H5Gcreate(parentId, name.c_str(), H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
	if (status < 0)
		throwException("Creating Group \"" + name + "\"", status);
	return status;
}
int SFReactorIOHandler::openGroup(int parentId, std::string name) throw (std::runtime_error) {
	int status = H5Gopen(parentId, name.c_str(), H5P_DEFAULT);
	if (status < 0)
		throwException("Opening Group \"" + name + "\"", status);
	return status;
}
void SFReactorIOHandler::closeGroup(int groupId) throw (std::runtime_error) {
	int status = H5Gclose(groupId);
	if (status < 0)
		throwException("Closing Group with id " + std::to_string((long long int) groupId), status);
	return;
}
int SFReactorIOHandler::createDataset(int parentGroupId, std::string name, int typeId, int spaceId) throw (std::runtime_error) {
	int status = H5Dcreate(parentGroupId, name.c_str(), typeId, spaceId, H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);
	if (status < 0)
		throwException("Creating Dataset \"" + name + "\"", status);
	return status;
}
int SFReactorIOHandler::openDataset(int parentGroupId, std::string name) throw (std::runtime_error) {
	int status = H5Dopen(parentGroupId, name.c_str(), H5P_DEFAULT);
	if (status < 0)
		throwException("Opening Dataset \"" + name + "\"", status);
	return status;
}
void SFReactorIOHandler::closeDataset(int datasetId) throw (std::runtime_error) {
	int status = H5Dclose(datasetId);
	if (status < 0)
		throwException("Closing Dataset.", status);
	return;
}
int SFReactorIOHandler::createDataspace(int rank, hsize_t* dims, hsize_t* maxDims) throw (std::runtime_error) {
	int status = H5Screate_simple(rank, dims, maxDims);
	if (status < 0)
		throwException("Creating Dataspace.", status);
	return status;
}
int SFReactorIOHandler::openDataspace(int datasetId) throw (std::runtime_error) {
	int status = H5Dget_space(datasetId);
	if (status < 0)
		throwException("Opening Dataspace.", status);
	return status;
}
void SFReactorIOHandler::closeDataspace(int spaceId) throw (std::runtime_error) {
	int status = H5Sclose(spaceId);
	if (status < 0)
		throwException("Closing Dataspace.", status);
	return;
}
int SFReactorIOHandler::createAttribute(int parentGroupId, std::string name, int typeId, int spaceId) throw(std::runtime_error) {
	int status = H5Acreate(parentGroupId, name.c_str(), typeId, spaceId, H5P_DEFAULT, H5P_DEFAULT);
	if (status < 0)
		throwException("Creating Attribute \"" + name + "\"", status);
	return status;
}
int SFReactorIOHandler::openAttribute(int parentGroupId, std::string name) throw(std::runtime_error) {
	int status = H5Aopen(parentGroupId, name.c_str(), H5P_DEFAULT);
	if (status < 0)
		throwException("Opening Attribute \"" + name + "\"", status);
	return status;
}
void SFReactorIOHandler::closeAttribute(int attributeId) throw(std::runtime_error) {
	int status = H5Aclose(attributeId);
	if (status < 0)
		throwException("Closing Attribute.", status);\
	return;
}
int SFReactorIOHandler::createDatatype(H5T_class_t classId, int size) throw(std::runtime_error) {
	int status = H5Tcreate(classId, size);
	if (status < 0)
		throwException("Creating Datatype.", status);
	return status;
}
void SFReactorIOHandler::closeDatatype(int typeId) throw(std::runtime_error) {
	int status = H5Tclose(typeId);
	if (status < 0)
		throwException("Closing Datatype.", status);\
	return;
}

void SFReactorIOHandler::throwException(std::string message, int status) throw (std::runtime_error) {
	throw std::runtime_error("SFReactorIOHandler error: " + message + ": " + std::to_string((long long int) status));
}

std::vector<std::string> SFReactorIOHandler::getChildNames(int parentId, int objectType) throw (std::runtime_error) {

	// Constants used below.
	const char * parentGroup = ".";
	H5_index_t indexType = H5_INDEX_NAME;
	H5_iter_order_t indexOrder = H5_ITER_INC;
	int lapl_id = H5P_DEFAULT;

	// Get the number of members in the Group (info is read into an H5G_info_t).
	H5G_info_t groupInfo;
	int status = H5Gget_info(parentId, &groupInfo);
	if (status < 0)
		throwException("Getting number of children of Group with ID " + std::to_string((long long int) parentId) + "", status);
	int nMembers = groupInfo.nlinks;

	// A List of Group names within the parent Group (which has ID groupId).
	std::vector<std::string> groupNames;

	// Loop over the possible indexes.
	for (int i = 0; i < nMembers; i++) {
		// Get the info for the object in this position.
		H5O_info_t objectInfo;
		status = H5Oget_info_by_idx(parentId, parentGroup, indexType, indexOrder, i, &objectInfo, lapl_id);

		// See if the object exists and is an HDF5 Group.
		if (status >= 0 && objectInfo.type == objectType) {

			// The first call will return the length of the string. The buffer
			// must be null for this to work.
			int length = H5Lget_name_by_idx(parentId, parentGroup, indexType, indexOrder, i, {}, 0, lapl_id);

			// A call with a non-null buffer will read the name into the buffer.
			if (length > 0) {
				// It appears that H5Lget_name_by_idx does not read in the last
				// character, so go ahead and increase the length of the buffer
				// by one, and make the last character the null terminator.
				length++;

				// Allocate the buffer.
				char buffer[length];

				// Get the name. If there is no error, store the name string.
				status = H5Lget_name_by_idx(parentId, parentGroup, indexType, indexOrder, i, buffer, length, lapl_id);
				if (status >= 0) {
					// Make sure the buffer is null-terminated.
					buffer[length - 1] = '\0';

					// Convert the buffer to a std::string and add it.
					groupNames.push_back(std::string(buffer));
				}
			}
		}
	}

	return groupNames;
}
void SFReactorIOHandler::writeIntegerAttribute(int objectId, std::string name, int value) throw (std::runtime_error) {
	int status;
	int typeId = H5T_NATIVE_INT;

	// Create the buffer that holds the data to write to the Attribute.
	int buffer[] = { value };
	hsize_t bufferSize[] = { 1 };

	// Create the Dataspace to hold the value.
	int spaceId = createDataspace(1, bufferSize, {});

	// Create the Attribute for the Dataspace.
	int attributeId = createAttribute(objectId, name, typeId, spaceId);

	// Write the Attribute.
	status = H5Awrite(attributeId, typeId, buffer);
	if (status < 0)
		throwException("Writing Attribute \"" + name + "\"", status);

	// Close the Attribute.
	closeAttribute(attributeId);

	// Close the Dataspace.
	closeDataspace(spaceId);

	return;
}
int SFReactorIOHandler::readIntegerAttribute(int objectId, std::string name) throw (std::runtime_error) {
	int status;
	int typeId = H5T_NATIVE_INT;

	// Create the buffer.
	int buffer[1];

	// Open the Attribute.
	int attributeId = openAttribute(objectId, name);

	// Read the Attribute.
	status = H5Aread(attributeId, typeId, buffer);
	if (status < 0)
		throwException("Reading Attribute \"" + name + "\"", status);

	// Close the Attribute.
	closeAttribute(attributeId);

	return buffer[0];
}
void SFReactorIOHandler::writeDoubleAttribute(int objectId, std::string name, double value) throw (std::runtime_error) {
	int status;
	int typeId = H5T_NATIVE_DOUBLE;

	// Create the buffer that holds the data to write to the Attribute.
	double buffer[] = { value };
	hsize_t bufferSize[] = { 1 };

	// Create the Dataspace to hold the value.
	int spaceId = createDataspace(1, bufferSize, {});

	// Create the Attribute for the Dataspace.
	int attributeId = createAttribute(objectId, name, typeId, spaceId);

	// Write the Attribute.
	status = H5Awrite(attributeId, typeId, buffer);
	if (status < 0)
		throwException("Writing Attribute \"" + name + "\"", status);

	// Close the Attribute.
	closeAttribute(attributeId);

	// Close the Dataspace.
	closeDataspace(spaceId);

	return;
}
double SFReactorIOHandler::readDoubleAttribute(int objectId, std::string name) throw (std::runtime_error) {
	int status;
	int typeId = H5T_NATIVE_DOUBLE;

	// Create the buffer.
	double buffer[1];

	// Open the Attribute.
	int attributeId = openAttribute(objectId, name);

	// Read the Attribute.
	status = H5Aread(attributeId, typeId, buffer);
	if (status < 0)
		throwException("Reading Attribute \"" + name + "\"", status);

	// Close the Attribute.
	closeAttribute(attributeId);

	return buffer[0];
}
void SFReactorIOHandler::writeStringAttribute(int objectId, std::string name, std::string value) throw (std::runtime_error) {
	int status;

	// Create the buffer that holds the data to write to the Attribute.
	const char* buffer = value.c_str();
	hsize_t bufferSize[] = { 1 };

	// Create the Dataspace to hold the value.
	int spaceId = createDataspace(1, bufferSize, {});

//	// Create the Datatype for the Attribute.
//	status = H5Tcreate(H5T_STRING, value.length());
//	if (status < 0)
//		throwException("Creating Datatype for Attribute \"" + name + "\"", status);
//	int typeId = status;

	// FIXME - H5T_STRING is not supported here for versions earlier than
	// HDF5 1.8.8. We need to use H5T_C_S1 as the type and manually set the
	// string length.

	// Copy the Datatype.
	status = H5Tcopy(H5T_C_S1);
	if (status < 0)
		throwException("Creating Datatype for Attribute \"" + name + "\"", status);
	int typeId = status;

	// Set the size of the Datatype.
	status = H5Tset_size(typeId, value.length());
	if (status < 0)
		throwException("Setting string size for Attribute \"" + name + "\"", status);

	// Create the Attribute for the Dataspace.
	int attributeId = createAttribute(objectId, name, typeId, spaceId);

	// Write the Attribute.
	status = H5Awrite(attributeId, typeId, buffer);
	if (status < 0)
		throwException("Writing Attribute \"" + name + "\"", status);

	// Close the Attribute.
	closeAttribute(attributeId);

	// Close the Datatype.
	status = H5Tclose(typeId);
	if (status < 0)
		throwException("Closing Datatype for Attribute \"" + name + "\"", status);

	// Close the Dataspace.
	closeDataspace(spaceId);

	return;
}
std::string SFReactorIOHandler::readStringAttribute(int objectId, std::string name) throw (std::runtime_error) {
	int status;

	// Open the Attribute.
	int attributeId = openAttribute(objectId, name);

	// Get the Datatype for the std::string (H5T_STRING with a size in bytes).
	status = H5Aget_type(attributeId);
	if (status < 0)
		throwException("Reading Datatype for Attribute \"" + name + "\"", status);
	int typeId = status;

	// Get the size of the std::string from the Datatype.
	status = H5Tget_size(typeId);
	if (status <= 0)
		throwException("Reading size of Datatype for Attribute \"" + name + "\"", status);
	int size = status;

	// Initialize the buffer. (Make it null-terminated.)
	char buffer[size + 1];

	// Read the Attribute.
	status = H5Aread(attributeId, typeId, buffer);
	if (status < 0)
		throwException("Reading Attribute \"" + name + "\"", status);

	// Close the Attribute.
	closeAttribute(attributeId);

	// Make the string null-terminated.
	buffer[size] = '\0';

	// Convert the buffer into a std::string.
	return std::string(buffer);
}
void SFReactorIOHandler::writeIntegerDataset(int objectId, std::string name, int rank, hsize_t* dims, int* buffer) throw (std::runtime_error) {
	int status;
	int typeId = H5T_NATIVE_INT;

	// Create the Dataspace.
	int spaceId = createDataspace(rank, dims, {});

	// Create the Dataset.
	int datasetId = createDataset(objectId, name, typeId, spaceId);

	// Write the Dataset.
	status = H5Dwrite(datasetId, typeId, H5S_ALL, H5S_ALL, H5P_DEFAULT, buffer);
	if (status < 0)
		throwException("Writing Dataset \"" + name + "\"", status);

	// Close the Dataset.
	closeDataset(datasetId);

	// Close the Dataspace.
	closeDataspace(spaceId);

	return;
}
void SFReactorIOHandler::writeDoubleDataset(int objectId, std::string name, int rank, hsize_t* dims, double* buffer) throw (std::runtime_error) {
	int status;
	int typeId = H5T_NATIVE_DOUBLE;

	// Create the Dataspace.
	int spaceId = createDataspace(rank, dims, {});

	// Create the Dataset.
	int datasetId = createDataset(objectId, name, typeId, spaceId);

	// Write the Dataset.
	status = H5Dwrite(datasetId, typeId, H5S_ALL, H5S_ALL, H5P_DEFAULT, buffer);
	if (status < 0)
		throwException("Writing Dataset \"" + name + "\"", status);

	// Close the Dataset.
	closeDataset(datasetId);

	// Close the Dataspace.
	closeDataspace(spaceId);

	return;
}
void SFReactorIOHandler::writeStringDataset(int objectId, std::string name, int rank, hsize_t* dims, int typeId, char* buffer) throw (std::runtime_error) {
	int status;

	// Create the Dataspace.
	int spaceId = createDataspace(rank, dims, {});

	// Create the Dataset.
	int datasetId = createDataset(objectId, name, typeId, spaceId);

	// Write the Dataset.
	status = H5Dwrite(datasetId, typeId, H5S_ALL, H5S_ALL, H5P_DEFAULT, buffer);
	if (status < 0)
		throwException("Writing Dataset \"" + name + "\"", status);

	// Close the Dataset.
	closeDataset(datasetId);

	// Close the Dataspace.
	closeDataspace(spaceId);

	return;
}
void SFReactorIOHandler::writeSFRComponent(std::shared_ptr<SFRComponent> component, int groupId) throw (std::runtime_error) {
	/* ---- Write the component's properties. ---- */
	writeStringAttribute(groupId, "name", component->getName());
	writeStringAttribute(groupId, "description", component->getDescription());
	writeIntegerAttribute(groupId, "id", component->getId());
	writeStringAttribute(groupId, "sourceInfo", component->getSourceInfo());
	writeDoubleAttribute(groupId, "time", component->getCurrentTime());
	writeStringAttribute(groupId, "timeUnits", component->getTimeUnits());
	/* ------------------------------------------- */

	/* ---- Write the data. ---- */
	// Create a Group to contain the feature data.
	int dataGroupId = createGroup(groupId, "Data");

	// Write the component's data.
	writeDataProvider(component, dataGroupId);

	// Close the feature data Group.
	closeGroup(dataGroupId);
	/* ------------------------- */

	return;
}
void SFReactorIOHandler::readSFRComponent(std::shared_ptr<SFRComponent> component, int groupId) throw (std::runtime_error) {
	/* ---- Read in the component's properties. ---- */
	component->setName(readStringAttribute(groupId, "name"));
	component->setDescription(readStringAttribute(groupId, "description"));
	component->setId(readIntegerAttribute(groupId, "id"));
	component->setSourceInfo(readStringAttribute(groupId, "sourceInfo"));
	component->setTime(readDoubleAttribute(groupId, "time"));
	component->setTimeUnits(readStringAttribute(groupId, "timeUnits"));
	/* --------------------------------------------- */

	/* ---- Read the data. ---- */
	// Open the data Group.
	int dataGroupId = openGroup(groupId, "Data");

	// Read the component's data.
	readDataProvider(component, dataGroupId);

	// Close the data Group.
	closeGroup(dataGroupId);
	/* ------------------------ */

	return;
}
void SFReactorIOHandler::writeGridData(std::vector<std::shared_ptr<SFRComponent>> providers, int groupId) throw (std::runtime_error) {
	// begin-user-code

	// Create a GridData Group.
	int gridDataGroupId = createGroup(groupId, "GridData");

	// Write all of the IDataProviders' data.
	for (int i = 0; i < providers.size(); i++) {

		// Skip non-existent providers (these are places where no pin/rod is
		// set).
		std::shared_ptr<SFRComponent> provider = providers[i];
		if (provider) {
			// Create a Group to hold the position's data.
			int providerGroupId = createGroup(gridDataGroupId, std::to_string((long long int) i));

			// Write the provider.
			writeDataProvider(provider, providerGroupId);

			// Close the IDataProvider's group.
			closeGroup(providerGroupId);
		}
	}

	// Close the GridData Group.
	closeGroup(gridDataGroupId);

	return;
	// end-user-code
}
void SFReactorIOHandler::writeDataProvider(std::shared_ptr<SFRComponent> provider, int groupId) throw (std::runtime_error) {
	// begin-user-code

	int featureGroupId;

	// Get the times.
	std::vector<double> times = provider->getTimes();
	double currentTime = provider->getCurrentTime();

	// Use a Map to keep track of the different units used.
	double unitsCount = 0.0;
	std::map<std::string, double> unitsMap;;
	std::vector<std::string> unitsList;
	int unitsStringLength = 0;

	// Default properties defining the Dataset of doubles.
	int rank = 2;
	hsize_t dims[2] = { 0, 6 };
	int type = H5T_NATIVE_DOUBLE;

	std::vector<std::string> features = provider->getFeatureList();
	for (int f = 0; f < features.size(); f++) {
		std::string feature = features[f];

		// Create a Group to contain state point data.
		featureGroupId = createGroup(groupId, feature);

		// Loop over the possible times.
		for (int d = 0; d < times.size(); d++) {
			double time = times[d];

			provider->setTime(time);

			// Get the data from the pin.
			std::vector<std::shared_ptr<IData>> dataList = provider->getDataAtCurrentTime(feature);
			int length = dataList.size();

			// Construct the buffer of data for HDF5 writing.
			double dataBuffer[dataList.size() * 6];
			int bufferIndex = 0;
			for (int i = 0; i < dataList.size(); i++) {
				std::shared_ptr<IData> data = dataList[i];

				dataBuffer[bufferIndex++] = data->getValue();
				dataBuffer[bufferIndex++] = data->getUncertainty();
				dataBuffer[bufferIndex++] = data->getPosition()[0];
				dataBuffer[bufferIndex++] = data->getPosition()[1];
				dataBuffer[bufferIndex++] = data->getPosition()[2];

				// Get the units.
				std::string units = data->getUnits();

				// Get the units ID from the Map. If the units are not in
				// the Map, then we need to add it.
				if (unitsMap.find(units) != unitsMap.end()) {
					dataBuffer[bufferIndex++] = unitsMap[units];
				} else {
					unitsMap[units] = unitsCount;
					unitsList.push_back(units);
					dataBuffer[bufferIndex++] = unitsCount++;
					if (units.length() > unitsStringLength)
						unitsStringLength = units.length();
				}
			}

			// Store the length (rows) of the array of data.
			dims[0] = length;

			// Write the state point data for this feature.
			writeDoubleDataset(featureGroupId, std::to_string((long double) time), rank, dims, dataBuffer);
		}

		// Close the state point data Group.
		closeGroup(featureGroupId);
	}

	// Write the units lookup Dataset if we have units.
	// FIXME - I'm not sure how to get variable length std::string Datasets to
	// work with the HDF5 Java library. Instead, we can compute the max
	// std::string length for the units above and write a Dataset of fixed-length
	// std::strings. Addendum 12/2/2013 - You probably need to use the
	// predefined H5T_C_S1 type with a size set to H5T_VARIABLE... see:
	// http://www.hdfgroup.org/HDF5/doc/RM/RM_H5T.html#CreateVLString
	if (unitsCount > 0) {

		// This is a simple array. The index is the ID of the std::string.
		rank = 1;
		hsize_t uDims[1] = { (hsize_t) unitsCount };

		// Create the Datatype for all elements in the Dataset.
//		type = H5Tcreate(H5T_STRING, unitsStringLength);

		// FIXME - H5T_STRING is not supported here for versions earlier than
		// HDF5 1.8.8. We need to use H5T_C_S1 as the type and manually set the
		// string length.

		// Copy the pre-defined C-string Datatype.
		int status = H5Tcopy(H5T_C_S1);
		if (status < 0)
			throwException("Creating Datatype for Units Dataset.", type);
		type = status;

		// Set the size of the C-string Datatype.
		status = H5Tset_size(type, unitsStringLength);
		if (status < 0)
			throwException("Setting string size for Units Dataset", status);

		// The buffer for the bytes is necessary because HDF5 does not
		// support Java std::strings.
		char unitsBuffer[(int) unitsCount][unitsStringLength];
		for (int i = 0; i < (int) unitsCount; i++) {
			for (int j = 0; j < unitsList[i].length(); j++)
				unitsBuffer[i][j] = unitsList[i][j];
		}

		// Write the Units dataset.
		writeStringDataset(groupId, "Units", rank, uDims, type, unitsBuffer[0]);

		// Close the Datatype.
		H5Tclose(type);
	}
	// Restore the current time.
	provider->setTime(currentTime);

	return;
	// end-user-code
}
void SFReactorIOHandler::readGridData(std::vector<std::shared_ptr<SFRComponent>> providers, int groupId) throw (std::runtime_error) {
	// begin-user-code

	// Open the GridData Group.
	int gridDataGroupId = openGroup(groupId, "GridData");

	// Write all of the IDataProviders' data.
	for (int i = 0; i < providers.size(); i++) {

		// Skip non-existent providers (these are places where no pin/rod is
		// set).
		std::shared_ptr<SFRComponent> provider = providers[i];
		if (provider) {
			// Open the Group holding the position's data.
			int providerGroupId = openGroup(gridDataGroupId, std::to_string((long long int) i));

			// Read the provider.
			readDataProvider(provider, providerGroupId);

			// Close the IDataProvider's group.
			closeGroup(providerGroupId);
		}
	}

	// Close the GridData Group.
	closeGroup(gridDataGroupId);

	return;
	// end-user-code
}
void SFReactorIOHandler::readDataProvider(std::shared_ptr<SFRComponent> provider, int groupId) throw (std::runtime_error) {
	// begin-user-code
	int status;

	/* ---- Read in the list of names of Units for the data. ---- */
	// An array to hold the units std::strings.
	std::vector<std::string> units;

	// See if the Units Dataset exists. If it does, read it in.
	if (H5Lexists(groupId, "Units", H5P_DEFAULT)) {

		// Open the Dataset.
		int datasetId = openDataset(groupId, "Units");

		// Open the Dataspace.
		int spaceId = openDataspace(datasetId);

		// Initialize the properties defining the Dataspace.
		int rank = 1;
		hsize_t dims[1];

		// Get the size (rows) of the Dataset.
		status = H5Sget_simple_extent_dims(spaceId, dims, {});
		if (status != rank)
			throwException("Getting size of units Dataset for IDataProvider.", status);

		// Open the Datatype.
		status = H5Dget_type(datasetId);
		if (status < 0)
			throwException("Opening Datatype for units Dataset for IDataProvider.", status);
		int typeId = status;

		status = H5Tget_size(typeId);
		if (status < 0)
			throwException("Getting size of Datatype for units Dataset for IDataProvider.", status);

		int rows = (int) dims[0];
		int length = status;
		// Initialize the buffer.
		char buffer[rows * length];

		// Read in the Dataset.
		status = H5Dread(datasetId, typeId, H5S_ALL, H5S_ALL, H5P_DEFAULT, buffer);
		if (status < 0)
			throwException("Reading units Dataset for IDataProvider.", status);

		// Close the Datatype.
		closeDatatype(typeId);

		// Close the Dataspace.
		closeDataspace(spaceId);

		// Close the Dataset.
		closeDataset(datasetId);

		// Copy each units string from the buffer. We can use the std::string's
		// "from buffer" constructor.
		for (int i = 0; i < rows * length; i += length)
			units.push_back(std::string(&(buffer[i]), length));
	}
	/* ---------------------------------------------------------- */

	/* ---- Read in the data for each feature. ---- */
	// Loop over the Groups in the Data group. These Groups are named after
	// the feature whose data they contain.
	std::vector<std::string> features = getChildNames(groupId, H5O_TYPE_GROUP);
	for (int f = 0; f < features.size(); f++) {
		std::string feature = features[f];

		// Open the feature Group.
		int featureGroupId = openGroup(groupId, feature);

		// Get the list of times.
		std::vector<std::string> timeStrings = getChildNames(featureGroupId, H5O_TYPE_DATASET);
		for (int t = 0; t < timeStrings.size(); t++) {
			std::string timeString = timeStrings[t];

			// Open the Dataset.
			int datasetId = openDataset(featureGroupId, timeString);

			// Open the Dataspace.
			int spaceId = openDataspace(datasetId);

			// Initialize the properties defining the Dataspace.
			int rank = 2;
			hsize_t dims[2];
			int typeId = H5T_NATIVE_DOUBLE;

			// Get the size of the data array.
			status = H5Sget_simple_extent_dims(spaceId, dims, {});
			if (status != rank || dims[1] != 6)
				throwException("Reading Dataspace dimensions for IDataProvider.", status);

			// Initialize the buffer.
			int size = (int) dims[0];
			double buffer[size][6];

			// Read in the data into the buffer.
			status = H5Dread(datasetId, typeId, H5S_ALL, H5S_ALL, H5P_DEFAULT, buffer);
			if (status < 0)
				throwException("Reading Dataset for IDataProvider.", status);

			// Close the Dataspace.
			closeDataspace(spaceId);

			// Close the Dataset.
			closeDataset(datasetId);

			double time = std::atof(timeString.c_str());

			// Initialize the IData for the timestep.
			for (int b = 0; b < size; b++) {

				// Initialize an iData.
				std::shared_ptr<SFRData> data = std::make_shared<SFRData>(feature);

				// Set its data.
				data->setValue(buffer[b][0]);
				data->setUncertainty(buffer[b][1]);
				std::vector<double> position(3);
				position[0] = buffer[b][2];
				position[1] = buffer[b][3];
				position[2] = buffer[b][4];
				data->setPosition(position);

				// Set the units from the pre-constructed array of data.
				data->setUnits(units[(int) buffer[b][5]]);

				// Add it to the component.
				provider->addData(data, time);
			}
		}
		// Close the feature Group.
		closeGroup(featureGroupId);
	}

	/* -------------------------------------------- */

	return;
	// end-user-code
}
void SFReactorIOHandler::writeLocationData(std::vector<int> locations, int groupId) throw (std::runtime_error) {
	int length = locations.size();

	// Set up the buffer for the H5 write operation.
	int buffer[length];
	for (int i = 0; i < length; i++)
		buffer[i] = locations[i];

	// These properties define the Dataspace.
	int rank = 1;
	hsize_t dims[1] = { (hsize_t) length };
	int type = H5T_NATIVE_INT;

	// Write the Dataset.
	writeIntegerDataset(groupId, "locations", rank, dims, buffer);

	return;
}
std::vector<int> SFReactorIOHandler::readLocationData(int groupId) throw (std::runtime_error) {
	int status;

	// Open the Dataset.
	int datasetId = openDataset(groupId, "locations");

	// Open the Dataspace.
	int spaceId = openDataspace(datasetId);

	// Initialize the properties defining the Dataspace.
	int rank = 1;
	hsize_t dims[1];
	int type = H5T_NATIVE_INT;

	// Get the size of the location data array.
	status = H5Sget_simple_extent_dims(spaceId, dims, {});
	if (status != rank)
		throwException("Reading Dataspace dimensions for location data in Group with id " + groupId, status);

	// Initialize the buffer.
	int size = (int) dims[0];
	int buffer[size];

	// Read in the location data into the buffer.
	status = H5Dread(datasetId, type, H5S_ALL, H5S_ALL, H5P_DEFAULT, buffer);
	if (status < 0)
		throwException("Reading Dataset for location data in Group with id " + groupId, status);

	// Close the Dataspace.
	closeDataspace(spaceId);

	// Close the Dataset.
	closeDataset(datasetId);

	// Return the locations.
	std::vector<int> locations(size);
	for (int i = 0; i < size; i++)
		locations[i] = buffer[i];
	return locations;
}
void SFReactorIOHandler::writeRing(std::shared_ptr<Ring> ring, int ringGroupId) throw (std::runtime_error) {

	// Write the basic SFRComponent Attributes.
	writeSFRComponent(ring, ringGroupId);

	// Write ring-specific Attributes.
	writeDoubleAttribute(ringGroupId, "height", ring->getHeight());
	writeDoubleAttribute(ringGroupId, "innerRadius", ring->getInnerRadius());
	writeDoubleAttribute(ringGroupId, "outerRadius", ring->getOuterRadius());

	// Write the material Attributes.
	std::shared_ptr<Material> material = ring->getMaterial();

	// Create a Group, write Attributes to it, and close it.
	int materialGroupId = createGroup(ringGroupId, "material");
	writeSFRComponent(material, materialGroupId);
	closeGroup(materialGroupId);

	return;
}
std::shared_ptr<Ring> SFReactorIOHandler::readRing(int ringGroupId) throw (std::runtime_error) {
	std::shared_ptr<Ring> ring = std::make_shared<Ring>();

	// Read the basic SFRComponent Attributes.
	readSFRComponent(ring, ringGroupId);

	// Read ring-specific Attributes.
	ring->setHeight(readDoubleAttribute(ringGroupId, "height"));
	ring->setInnerRadius(readDoubleAttribute(ringGroupId, "innerRadius"));
	ring->setOuterRadius(readDoubleAttribute(ringGroupId, "outerRadius"));

	// Read the material.
	std::shared_ptr<Material> material = std::make_shared<Material>();

	// Read the material's Attributes.
	int materialGroupId = openGroup(ringGroupId, "material");
	readSFRComponent(material, materialGroupId);
	closeGroup(materialGroupId);

	// Update the ring's material.
	ring->setMaterial(material);

	return ring;
}



