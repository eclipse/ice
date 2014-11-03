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
package org.eclipse.ice.reactor.sfr.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeSet;

import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.base.SFRData;
import org.eclipse.ice.reactor.sfr.core.AssemblyType;
import org.eclipse.ice.reactor.sfr.core.Material;
import org.eclipse.ice.reactor.sfr.core.MaterialBlock;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.eclipse.ice.reactor.sfr.core.assembly.PinAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.PinType;
import org.eclipse.ice.reactor.sfr.core.assembly.ReflectorAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRRod;

/**
 * This factory can be used to generate full-core reactors. The assemblies,
 * rods/pins, their locations within their respective containers, and their data
 * are generated randomly.
 * 
 * @author djg
 * 
 */
public class SFReactorFactory {

	public SFReactor generatePopulatedFullCoreReactor(int reactorSize,
			int assemblySize, int nAssemblies, int nAssemblyComponents,
			int nAxialLevels, long seed, boolean randomData,
			boolean randomLocations) {
		// begin-user-code

		// Check the sizes. If invalid, set the defaults.
		if (reactorSize <= 0) {
			reactorSize = 17;
		}
		if (assemblySize <= 0) {
			assemblySize = 15;
		}
		// Check the other properties.
		if (nAssemblies <= 0) {
			nAssemblies = reactorSize * reactorSize;
		}
		if (nAssemblyComponents <= 0) {
			nAssemblyComponents = assemblySize * assemblySize;
		}
		if (nAxialLevels <= 0) {
			nAxialLevels = 49;
		}

		int i, j, k;
		int nLocations;
		int row, column;

		// Timesteps for the data.
		List<Double> times = new ArrayList<Double>();
		times.add(0.0);
		times.add(1.0);
		times.add(2.0);

		// Features to use.
		Map<String, Integer> features = new HashMap<String, Integer>();
		features.put("Feature 1", 1);
		features.put("Axial Feature 1", nAxialLevels);
		features.put("Feature 2", 1);
		features.put("Axial Feature 2", nAxialLevels);
		features.put("Feature 3", 3);

		// Initialize the random number generator.
		Random generator = new Random(seed);

		// Initialize the reactor.
		SFReactor reactor = new SFReactor(reactorSize);

		// Materials that we can use in the reactor.
		Material helium = new Material("He");
		helium.setDescription("Helium");
		Material steel = new Material("SS-316");
		steel.setDescription("Stainless Steel");
		Material uraniumOxide = new Material("UO2");
		uraniumOxide.setDescription("Uranium Oxide");
		Material boronCarbide = new Material("B4C");
		boronCarbide.setDescription("Boron Carbide");

		// Add data to the reactor.
		addDataToComponent(reactor, features, times, randomData, generator);

		// For each assembly type:
		for (AssemblyType assemblyType : AssemblyType.values()) {
			// Generate random assemblies.
			for (i = 0; i < nAssemblies; i++) {

				/* ---- Create an assembly and add it to the reactor. ---- */
				// The assembly. Its name and ID should be based on i.
				SFRAssembly assembly;
				String name = Integer.toString(i);

				// Create a new assembly.
				if (assemblyType.equals(AssemblyType.Reflector)) {
					assembly = new ReflectorAssembly(name, assemblySize);
				} else if (assemblyType.equals(AssemblyType.Control)) {
					PinType pinType = ((randomData && generator.nextBoolean())
							|| (!randomData && i % 2 == 0) ? PinType.PrimaryControl
							: PinType.SecondaryControl);
					assembly = new PinAssembly(name, pinType, assemblySize);
				} else {
					PinType pinType = ((randomData && generator.nextBoolean())
							|| (!randomData && i % 2 == 0) ? PinType.InnerFuel
							: PinType.BlanketFuel);
					assembly = new PinAssembly(name, pinType, assemblySize);
				}
				assembly.setId(i);

				// Add data to the assembly.
				addDataToComponent(assembly, features, times, randomData,
						generator);

				// Add the assembly to the reactor.
				reactor.addAssembly(assemblyType, assembly);

				// Set the assembly's location.
				if (!randomLocations) {
					reactor.setAssemblyLocation(assemblyType, name, i
							/ reactorSize, i % reactorSize);
				} else {
					// Set some random locations for the assembly.
					// nLocations = generator.nextInt(reactorSize * reactorSize)
					// + 1;
					nLocations = 5;
					for (j = 0; j < nLocations; j++) {
						// Pick a random location for the assembly.
						row = generator.nextInt(reactorSize);
						column = generator.nextInt(reactorSize);

						// Set the assembly location.
						reactor.setAssemblyLocation(assemblyType, name, row,
								column);
					}
				}
				/* ------------------------------------------------------- */

				/* ---- Create rods/pins and add them to the assembly. ---- */
				// Generate assembly components (rods/pins).
				for (j = 0; j < nAssemblyComponents; j++) {

					// The rod/pin. Its name and ID should be based on j.
					SFRComponent component;
					name = Integer.toString(j);

					// Create a new rod/pin. We also need to add them to the
					// assembly here (because Reflector/PinAssemblies add them
					// via other operations).
					if (assemblyType.equals(AssemblyType.Reflector)) {
						// Initialize a rod.
						SFRRod rod = new SFRRod(name);
						rod.setId(j);
						component = rod;

						// Occasionally add some reflector rods with different
						// radii.
						if ((randomData && generator.nextInt() % 5 == 0)
								|| (!randomData && j % 5 == 0)) {
							Ring reflector = new Ring();
							reflector.setMaterial(steel);
							reflector.setHeight(0.0);
							reflector.setInnerRadius(0.0);

							// We don't want the outer radius to be too small.
							double ratio = (randomData ? generator.nextDouble()
									: (double) j / (double) nAssemblyComponents);
							if (ratio < 0.25) {
								ratio = 0.25;
							}
							reflector.setOuterRadius(ratio * 26.666);
						}
					} else {
						// Initialize a pin.
						SFRPin pin = new SFRPin(name);
						pin.setId(j);
						component = pin;

						PinType pinType = ((PinAssembly) assembly).getPinType();

						// Change the structure of the pin based on the assembly
						// type.
						if (assemblyType.equals(AssemblyType.Control)) {
							// Control pins get boron carbide and no data
							// (although they can have data).

							// Set up the basic block materials.
							Ring cladding = new Ring("Cladding", steel, 0,
									16.25, 17.5);
							if (pinType.equals(PinType.SecondaryControl)) {
								cladding.setOuterRadius(17);
							}
							Ring fillGas = new Ring("Fill Gas", helium, 0,
									13.3333, 16.25);
							Ring fuel = new Ring("Control Fuel", boronCarbide,
									0, 0, 13.3333);

							// Construct the material block set.
							TreeSet<MaterialBlock> materialBlocks = new TreeSet<MaterialBlock>();

							// Construct the first block.
							MaterialBlock block = new MaterialBlock();
							block.setVertPosition(0);
							block.addRing(cladding);
							block.addRing(fillGas);
							block.addRing(fuel);
							materialBlocks.add(block);

							// Set the pin attributes.
							pin.setCladding(cladding);
							pin.setFillGas(helium);
							pin.setMaterialBlocks(materialBlocks);
						} else {
							// Fuel pins get uranium oxide and data.

							// Set up the basic block materials.
							Ring cladding = new Ring("Cladding", steel, 0,
									16.25, 17.5);
							if (pinType.equals(PinType.BlanketFuel)) {
								cladding.setOuterRadius(17);
							}
							Ring fillGas = new Ring("Fill Gas", helium, 0,
									13.3333, 16.25);
							Ring fuel = new Ring("Fuel", uraniumOxide, 0, 0,
									13.3333);

							// Construct the material block set.
							TreeSet<MaterialBlock> materialBlocks = new TreeSet<MaterialBlock>();

							// Construct the fuel block.
							MaterialBlock block = new MaterialBlock();
							block.setVertPosition(0);
							block.addRing(cladding);
							block.addRing(fillGas);
							block.addRing(fuel);

							// For blanket fuel, sandwich the fuel with some
							// boron carbide (just for testing purposes).
							if (pinType.equals(PinType.BlanketFuel)) {
								// The main fuel should comprise the middle 50%
								// of the pin.
								block.setVertPosition(0.25);
								materialBlocks.add(block);

								Ring blanketFuel = new Ring("Blanket Fuel",
										boronCarbide, 0, 0, 13.0);
								fillGas = (Ring) fillGas.clone();

								// The control fuel should comprise the bottom
								// 25% of the pin.
								block = new MaterialBlock();
								block.setVertPosition(0);
								block.addRing(cladding);
								fillGas.setInnerRadius(13.0);
								block.addRing(fillGas);
								block.addRing(blanketFuel);
								materialBlocks.add(block);

								// The remaining control fuel should comprise
								// the top 25% of the pin.
								block = (MaterialBlock) block.clone();
								block.setVertPosition(0.75);
							}
							// Add the last block that was modified.
							materialBlocks.add(block);

							// Set the pin attributes.
							pin.setCladding(cladding);
							pin.setFillGas(helium);
							pin.setMaterialBlocks(materialBlocks);
						}
					}

					// Add data to the component.
					addDataToComponent(component, features, times, randomData,
							generator);

					// Add the rod/pin to the assembly and set its location.
					if (assemblyType.equals(AssemblyType.Reflector)) {
						((ReflectorAssembly) assembly)
								.addRod((SFRRod) component);
					} else {
						((PinAssembly) assembly).addPin((SFRPin) component);
					}
					// Set the pin/rod location.
					if (!randomLocations) {
						if (assemblyType.equals(AssemblyType.Reflector)) {
							((ReflectorAssembly) assembly).setRodLocation(name,
									j / assemblySize, j % assemblySize);
						} else {
							((PinAssembly) assembly).setPinLocation(name, j
									/ assemblySize, j % assemblySize);
						}
					} else {
						// Set some random locations for the rod/pin.
						// nLocations = generator.nextInt(assemblySize) + 1;
						nLocations = 5;
						for (k = 0; k < nLocations; k++) {
							// Pick a random location for the rod/pin.
							row = generator.nextInt(assemblySize);
							column = generator.nextInt(assemblySize);

							// Set the rod/pin location.
							if (assemblyType.equals(AssemblyType.Reflector)) {
								((ReflectorAssembly) assembly).setRodLocation(
										name, row, column);
							} else {
								((PinAssembly) assembly).setPinLocation(name,
										row, column);
							}
						}
					}
				}
				/* -------------------------------------------------------- */

				/* ---- Add GridData to the assembly. ---- */
				// Add data to each grid position.
				for (int dataRow = 0; dataRow < assemblySize; dataRow++) {
					for (int dataColumn = 0; dataColumn < assemblySize; dataColumn++) {
						SFRComponent provider = null;
						if (assemblyType.equals(AssemblyType.Reflector)) {
							provider = ((ReflectorAssembly) assembly)
									.getDataProviderByLocation(dataRow,
											dataColumn);
						} else {
							provider = ((PinAssembly) assembly)
									.getDataProviderByLocation(dataRow,
											dataColumn);
						}
						if (provider != null) {
							addDataToComponent(provider, features, times,
									randomData, generator);
						}
					}
				}
				/* --------------------------------------- */
			}
		}

		return reactor;
		// end-user-code
	}

	private void addDataToComponent(SFRComponent component,
			Map<String, Integer> features, List<Double> times,
			boolean randomData, Random generator) {
		for (Entry<String, Integer> entry : features.entrySet()) {

			String feature = entry.getKey();
			int nAxialLevels = entry.getValue();

			for (double time : times) {
				// Add the correct number of data points.
				for (int k = 0; k < nAxialLevels; k++) {
					SFRData data = new SFRData(feature);

					// Set a random value.
					data.setValue(randomData ? generator.nextDouble()
							: 1.0 / (k + 1));

					// Set the position (z-only).
					ArrayList<Double> position = new ArrayList<Double>(3);
					position.add(0.0);
					position.add(0.0);
					position.add((double) k);
					data.setPosition(position);

					// Set some different units (although typically they are the
					// same).
					data.setUnits("Units # " + k);

					// Add the data to the component.
					component.addData(data, time);
				}
			}
		}

		return;
	}
}
