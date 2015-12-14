/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Eric J. Lingerfelt, Alexander J. McCaskey, 
 *   Taylor Patterson, Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets.reactoreditor.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.MaterialType;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.TubeType;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;

/**
 * <p>
 * This class creates a PWRReactor populated with test Devono data.
 * </p>
 * 
 * @author Eric J. Lingerfelt
 */
public class DenovoReactorFactory {

	/**
	 * <p>
	 * Creates and returns a sample PWReactor populated with state point data at
	 * one time step.
	 * </p>
	 * 
	 * @return A sample PWReactor populated with state point data at one time
	 *         step.
	 */
	public PressurizedWaterReactor createPopulatedPWReactor() {

		// Create a reactor of size 15
		PressurizedWaterReactor pwReactor = new PressurizedWaterReactor(15);
		pwReactor.setName("Denovo PWR");
		pwReactor.setFuelAssemblyPitch(21.5);

		// PWREACTOR GRID
		// LABELS/////////////////////////////////////////////////
		// Create a list of row labels
		ArrayList<String> rowLabelsReactor = new ArrayList<String>();
		rowLabelsReactor.add("1");
		rowLabelsReactor.add("2");
		rowLabelsReactor.add("3");
		rowLabelsReactor.add("4");
		rowLabelsReactor.add("5");
		rowLabelsReactor.add("6");
		rowLabelsReactor.add("7");
		rowLabelsReactor.add("8");
		rowLabelsReactor.add("9");
		rowLabelsReactor.add("10");
		rowLabelsReactor.add("11");
		rowLabelsReactor.add("12");
		rowLabelsReactor.add("13");
		rowLabelsReactor.add("14");
		rowLabelsReactor.add("15");

		// Create a list of column labels
		ArrayList<String> columnLabelsReactor = new ArrayList<String>();
		columnLabelsReactor.add("R");
		columnLabelsReactor.add("P");
		columnLabelsReactor.add("N");
		columnLabelsReactor.add("M");
		columnLabelsReactor.add("L");
		columnLabelsReactor.add("K");
		columnLabelsReactor.add("J");
		columnLabelsReactor.add("H");
		columnLabelsReactor.add("G");
		columnLabelsReactor.add("F");
		columnLabelsReactor.add("E");
		columnLabelsReactor.add("D");
		columnLabelsReactor.add("C");
		columnLabelsReactor.add("B");
		columnLabelsReactor.add("A");

		// CONTROL
		// BANKS//////////////////////////////////////////////////////////
		pwReactor.addAssembly(AssemblyType.ControlBank, new ControlBank("A",
				0.625, 230));
		pwReactor.addAssembly(AssemblyType.ControlBank, new ControlBank("B",
				0.625, 230));
		pwReactor.addAssembly(AssemblyType.ControlBank, new ControlBank("C",
				0.625, 230));
		pwReactor.addAssembly(AssemblyType.ControlBank, new ControlBank("D",
				0.625, 230));
		pwReactor.addAssembly(AssemblyType.ControlBank, new ControlBank("SA",
				0.625, 230));
		pwReactor.addAssembly(AssemblyType.ControlBank, new ControlBank("SB",
				0.625, 230));
		pwReactor.addAssembly(AssemblyType.ControlBank, new ControlBank("SC",
				0.625, 230));
		pwReactor.addAssembly(AssemblyType.ControlBank, new ControlBank("SD",
				0.625, 230));

		// Assign a position for the control banks
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 10, 10);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 9, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 7, 9);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 13, 9);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 9, 13);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 9, 9);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 13, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 7, 13);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 7, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 11, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 7, 11);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 11, 11);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "SA", 13, 11);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "SA", 11, 13);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "SB", 12, 8);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "SB", 8, 12);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "SC", 10, 12);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "SD", 12, 10);

		convertQuarterToFullSymmetry(pwReactor);

		// Assign the label arrays
		pwReactor.getGridLabelProvider().setRowLabels(rowLabelsReactor);
		pwReactor.getGridLabelProvider().setColumnLabels(columnLabelsReactor);

		// INCORE
		// INSTRUMENTS/////////////////////////////////////////////////////
		// Create incore instrument for the reactor
		IncoreInstrument incoreInstrument = new IncoreInstrument();
		incoreInstrument.setName("D");

		// Create the thimble material
		Material material = new Material("stainless steel");
		material.setMaterialType(MaterialType.SOLID);

		// Create the thimble
		Ring thimble = new Ring("Thimble", material, 155, 0.258, 0.382);
		incoreInstrument.setThimble(thimble);

		// Add the incore instrument
		pwReactor.addAssembly(AssemblyType.IncoreInstrument, incoreInstrument);

		int[][] locationArray = new int[15][];
		locationArray[0] = new int[] { 6, 9 };
		locationArray[1] = new int[] { 2, 5, 7 };
		locationArray[2] = new int[] { 7, 9, 11, 13 };
		locationArray[3] = new int[] { 1, 2, 7 };
		locationArray[4] = new int[] { 4, 8, 10, 12 };
		locationArray[5] = new int[] { 0, 2, 5, 7, 13 };
		locationArray[6] = new int[] { 3, 6, 9, 12 };
		locationArray[7] = new int[] { 0, 2, 4, 6, 9, 11, 12, 13 };
		locationArray[8] = new int[] { 1, 8, 10, 14 };
		locationArray[9] = new int[] { 4, 6, 11 };
		locationArray[10] = new int[] { 0, 4, 7, 10, 14 };
		locationArray[11] = new int[] { 5, 8, 11 };
		locationArray[12] = new int[] { 2, 4, 7, 13 };
		locationArray[13] = new int[] { 2, 6, 9, 11 };
		locationArray[14] = new int[] { 4, 7 };

		// Assign locations for the incore instruments
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < locationArray[i].length; j++) {
				pwReactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
						incoreInstrument.getName(), i, locationArray[i][j]);
			}
		}

		// ROD CLUSTER
		// ASSEMBLIES////////////////////////////////////////////////////////
		/*
		 * RodClusterAssembly rodClusterAssembly = new RodClusterAssembly(17);
		 * rodClusterAssembly.setName("RCA");
		 * 
		 * pwReactor.addRodClusterAssembly(rodClusterAssembly);
		 * pwReactor.setRodClusterAssemblyLocation(rodClusterAssembly.getName(),
		 * 5, 5);
		 */

		// FUEL
		// ASSEMBLIES////////////////////////////////////////////////////////
		FuelAssembly fuelAssembly = new FuelAssembly(17);
		fuelAssembly.setName("Assembly A");
		fuelAssembly.setRodPitch(1.26);

		// Create a list of row labels
		ArrayList<String> rowLabelsFuelAssembly = new ArrayList<String>();
		rowLabelsFuelAssembly.add("1");
		rowLabelsFuelAssembly.add("2");
		rowLabelsFuelAssembly.add("3");
		rowLabelsFuelAssembly.add("4");
		rowLabelsFuelAssembly.add("5");
		rowLabelsFuelAssembly.add("6");
		rowLabelsFuelAssembly.add("7");
		rowLabelsFuelAssembly.add("8");
		rowLabelsFuelAssembly.add("9");
		rowLabelsFuelAssembly.add("10");
		rowLabelsFuelAssembly.add("11");
		rowLabelsFuelAssembly.add("12");
		rowLabelsFuelAssembly.add("13");
		rowLabelsFuelAssembly.add("14");
		rowLabelsFuelAssembly.add("15");
		rowLabelsFuelAssembly.add("16");
		rowLabelsFuelAssembly.add("17");

		// Create list of column labels
		ArrayList<String> columnLabelsFuelAssembly = new ArrayList<String>();
		columnLabelsFuelAssembly.add("A");
		columnLabelsFuelAssembly.add("B");
		columnLabelsFuelAssembly.add("C");
		columnLabelsFuelAssembly.add("D");
		columnLabelsFuelAssembly.add("E");
		columnLabelsFuelAssembly.add("F");
		columnLabelsFuelAssembly.add("G");
		columnLabelsFuelAssembly.add("H");
		columnLabelsFuelAssembly.add("I");
		columnLabelsFuelAssembly.add("J");
		columnLabelsFuelAssembly.add("K");
		columnLabelsFuelAssembly.add("L");
		columnLabelsFuelAssembly.add("M");
		columnLabelsFuelAssembly.add("N");
		columnLabelsFuelAssembly.add("O");
		columnLabelsFuelAssembly.add("P");
		columnLabelsFuelAssembly.add("Q");

		// Assign the labels array
		fuelAssembly.getGridLabelProvider().setRowLabels(rowLabelsFuelAssembly);
		fuelAssembly.getGridLabelProvider().setColumnLabels(
				columnLabelsFuelAssembly);

		Material stackMaterial = new Material("u31", MaterialType.SOLID);
		Material fillGasMaterial = new Material("he", MaterialType.GAS);
		Material tubeMaterial = new Material("zirc", MaterialType.SOLID);
		Material cladMaterial = new Material("zirc", MaterialType.SOLID);

		// Create and add the rod type to the fuel assembly
		LWRRod rod = new LWRRod("U31 Fuel Rod");

		Ring clad = new Ring("Clad", cladMaterial, 1.0, 0.418, 0.475);
		MaterialBlock stack = new MaterialBlock();
		Ring fillGas = new Ring("Fill Gas", fillGasMaterial, 1.0, 0.4096, 0.418);
		stack.addRing(fillGas);
		Ring pellet = new Ring("Pellet", stackMaterial, 1.0, 0.4096);
		stack.addRing(pellet);

		TreeSet<MaterialBlock> blocks = new TreeSet<MaterialBlock>();
		blocks.add(stack);

		rod.setFillGas(fillGasMaterial);
		rod.setMaterialBlocks(blocks);
		rod.setClad(clad);

		fuelAssembly.addLWRRod(rod);

		// Open the test data
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "reactorEditorData";
		File pinDataFile = new File(userDir + separator + "denovoPinPower.txt");
		File powerDataFile = new File(userDir + separator + "denovoAxialPower.txt");
		File axialDataFile = new File(userDir + separator + "denovoAxialLevels.txt");

		// Parse the test data into pins and locations
		try {

			String pinDataFileContents = new String(this.readFile(pinDataFile));
			String powerDataFileContents = new String(
					this.readFile(powerDataFile));
			String axialDataFileContents = new String(
					this.readFile(axialDataFile));

			String[] pinDataLines = pinDataFileContents.split("\n");
			String[] powerDataLines = powerDataFileContents.split("\n");
			String[] levelDataValues = axialDataFileContents.split(",");

			int pinNumber = 1;

			for (int i = 0; i < pinDataLines.length; i++) {

				String trimmedString = pinDataLines[i].trim();
				String splitColon[] = trimmedString.split(":");
				String splitComma[] = splitColon[1].split(",");

				String splitLocation[] = splitColon[0].split(",");
				int xCoor = Integer.parseInt(splitLocation[2]);
				int yCoor = Integer.parseInt(splitLocation[3].split("\\)")[0]);

				for (int j = 0; j < splitComma.length; j++) {

					LWRData data = new LWRData("Pin Power");
					data.setValue(Double.parseDouble(splitComma[j]));

					if (data.getValue() != 0.0) {

						fuelAssembly.setLWRRodLocation(rod.getName(), xCoor,
								yCoor + j);

						LWRDataProvider lWRDataProvider = fuelAssembly
								.getLWRRodDataProviderAtLocation(xCoor, yCoor
										+ j);
						lWRDataProvider.addData(data, 0.0);

						pinNumber++;

					} else if (xCoor == 8 && (yCoor + j) == 8) {

						Tube tube = new Tube("IT", TubeType.INSTRUMENT,
								tubeMaterial, 1.0, 0.559, 0.605);
						fuelAssembly.addTube(tube);
						fuelAssembly.setTubeLocation(tube.getName(), xCoor,
								yCoor + j);

					} else {

						Tube tube = new Tube("GT", TubeType.GUIDE,
								tubeMaterial, 1.0, 0.561, 0.602);
						fuelAssembly.addTube(tube);
						fuelAssembly.setTubeLocation(tube.getName(), xCoor,
								yCoor + j);

					}

				}

			}

			for (int i = 0; i < powerDataLines.length; i++) {

				String trimmedString = powerDataLines[i].trim();
				String splitColon[] = trimmedString.split(":");
				String splitComma[] = splitColon[1].split(",");
				String splitLocation[] = splitColon[0].split(",");

				int axialLevel = Integer.parseInt(splitLocation[1]);
				int xCoor = Integer.parseInt(splitLocation[2]);
				int yCoor = Integer.parseInt(splitLocation[3].split("\\)")[0]);

				for (int j = 0; j < splitComma.length; j++) {

					LWRData data = new LWRData("Axial Pin Power");
					data.setValue(Double.parseDouble(splitComma[j]));
					data.setUnits("");

					int y = yCoor + j;

					double z = Double.valueOf(levelDataValues[axialLevel]);
					ArrayList<Double> position = new ArrayList<Double>();
					position.add(0.0);
					position.add(0.0);
					position.add(z);
					data.setPosition(position);

					if (data.getValue() != 0.0) {

						LWRDataProvider lWRDataProvider = fuelAssembly
								.getLWRRodDataProviderAtLocation(xCoor, y);
						lWRDataProvider.addData(data, 0.0);

					}
				}

			}

			// Add the fuel assembly to the reactor
			pwReactor.addAssembly(AssemblyType.Fuel, fuelAssembly);
			pwReactor.setAssemblyLocation(AssemblyType.Fuel,
					fuelAssembly.getName(), 7, 7);

			// File dataFile = new
			// File("/home/Jay Jay Billings/Desktop/denovo_reactor.h5");
			// URI uri = dataFile.toURI();
			// new LWRComponentWriter().write(pwReactor, uri);

			return pwReactor;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 
	 * 
	 * @param pWReactor
	 */
	private void convertQuarterToFullSymmetry(PressurizedWaterReactor pWReactor) {

		int size = pWReactor.getSize();
		int lastIndex = size - 1;
		int limit = lastIndex / 2;

		for (int row = lastIndex; row >= limit; row--) {

			for (int col = lastIndex; col >= limit; col--) {

				ControlBank controlBank = (ControlBank) pWReactor
						.getAssemblyByLocation(AssemblyType.ControlBank, row,
								col);

				if (controlBank != null) {

					String name = controlBank.getName();

					// QI
					pWReactor.setAssemblyLocation(AssemblyType.ControlBank,
							name, lastIndex - row, lastIndex - col);

					// Q2
					pWReactor.setAssemblyLocation(AssemblyType.ControlBank,
							name, lastIndex - row, col);

					// Q3
					pWReactor.setAssemblyLocation(AssemblyType.ControlBank,
							name, row, lastIndex - col);

				}

			}

		}

	}

	/**
	 * Reads a file into a byte array.
	 * 
	 * @param file
	 *            the file
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public byte[] readFile(File file) throws IOException {
		int i = (int) file.length();
		byte[] buffer = new byte[i];
		FileInputStream fis = new FileInputStream(file);
		fis.read(buffer);
		fis.close();
		return buffer;
	}

}
