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
package org.eclipse.ice.client.widgets.reactoreditor.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.LWRData;
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
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;

public class PWReactorFactory {

	/**
	 * <p>
	 * Creates and returns a sample PWReactor populated with state point data at
	 * one time step. PLEASE NOTE: This factory is still using the old
	 * LWRComponent and IDataProvided scheme.
	 * </p>
	 * 
	 * @return A sample PWReactor populated with state point data at one time
	 *         step.
	 */
	public PressurizedWaterReactor createPopulatedPWReactor() {

		// Create a reactor of size 15
		PressurizedWaterReactor pwReactor = new PressurizedWaterReactor(15);
		pwReactor.setName("Test PWR");
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

		// Assign a position for the control banks
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 10, 10);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 9, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 13, 9);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 9, 9);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 13, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 7, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 11, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 11, 11);

		convertOctantToQuarterSymmetry(pwReactor);
		convertQuarterToFullSymmetry(pwReactor);

		// Assign the label arrays
		pwReactor.getGridLabelProvider().setRowLabels(rowLabelsReactor);
		pwReactor.getGridLabelProvider().setColumnLabels(columnLabelsReactor);

		// INCORE
		// INSTRUMENTS/////////////////////////////////////////////////////
		// Create incore instruments for the reactor
		IncoreInstrument incoreInstrument1 = new IncoreInstrument();
		incoreInstrument1.setName("I1");
		IncoreInstrument incoreInstrument2 = new IncoreInstrument();
		incoreInstrument2.setName("I2");
		IncoreInstrument incoreInstrument3 = new IncoreInstrument();
		incoreInstrument3.setName("I3");
		IncoreInstrument incoreInstrument4 = new IncoreInstrument();
		incoreInstrument4.setName("I4");

		// Create the thimble material
		Material material = new Material("stainless steel");
		material.setMaterialType(MaterialType.SOLID);

		// Create the thimble
		Ring thimble = new Ring("Thimble", material, 155, 0.258, 0.382);
		incoreInstrument1.setThimble(thimble);
		incoreInstrument2.setThimble((Ring) thimble.clone());
		incoreInstrument3.setThimble((Ring) thimble.clone());
		incoreInstrument4.setThimble((Ring) thimble.clone());

		// Add the incore instruments
		pwReactor.addAssembly(AssemblyType.IncoreInstrument, incoreInstrument1);
		pwReactor.addAssembly(AssemblyType.IncoreInstrument, incoreInstrument2);
		pwReactor.addAssembly(AssemblyType.IncoreInstrument, incoreInstrument3);
		pwReactor.addAssembly(AssemblyType.IncoreInstrument, incoreInstrument4);

		// Assign locations for the incore instruments
		pwReactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				incoreInstrument1.getName(), 2, 1);
		pwReactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				incoreInstrument2.getName(), 6, 5);
		pwReactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				incoreInstrument3.getName(), 11, 2);
		pwReactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				incoreInstrument4.getName(), 13, 8);

		// ROD CLUSTER
		// ASSEMBLIES////////////////////////////////////////////////////////
		RodClusterAssembly rodClusterAssembly = new RodClusterAssembly(17);
		rodClusterAssembly.setName("RCA");

		pwReactor.addAssembly(AssemblyType.RodCluster, rodClusterAssembly);
		pwReactor.setAssemblyLocation(AssemblyType.RodCluster,
				rodClusterAssembly.getName(), 5, 5);

		// FUEL
		// ASSEMBLIES////////////////////////////////////////////////////////
		FuelAssembly fuelAssembly = new FuelAssembly(17);
		fuelAssembly.setName("A");
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

		Material stackMaterial = new Material("UO2", MaterialType.SOLID);
		Material fillGasMaterial = new Material("Helium", MaterialType.GAS);
		Material tubeMaterial = new Material("Zircaloy-4", MaterialType.SOLID);
		Material cladMaterial = new Material("Zircaloy-4", MaterialType.SOLID);

		// Open the test data
		String separator = System.getProperty("file.separator");
		File pinDataFile = new File(System.getProperty("user.dir") + separator
				+ "data" + separator + "pin_data.txt");
		File powerDataFile = new File(System.getProperty("user.dir")
				+ separator + "data" + separator + "power_data.txt");

		// Parse the test data into pins and locations
		try {

			String pinDataFileContents = new String(this.readFile(pinDataFile));
			String powerDataFileContents = new String(
					this.readFile(powerDataFile));

			String[] pinDataLines = pinDataFileContents.split("\n");
			String[] powerDataLines = powerDataFileContents.split("\n");

			int column = 8;
			int row = 8;
			int pinNumber = 1;

			// Create the pins
			for (int i = 0; i < pinDataLines.length; i++) {

				String line = pinDataLines[i];
				String[] elements = line.split(",");
				double outerRadius = Double.valueOf(elements[2]);

				line = powerDataLines[i];
				elements = line.split(",");
				double pinPower = Double.valueOf(elements[1]);
				double uncertainty = Double.valueOf(elements[2]);

				if (column <= row) {

					if (outerRadius == 0.475) {

						LWRData data = new LWRData("Pin Power");
						data.setValue(pinPower);
						data.setUncertainty(uncertainty);

						LWRRod rod = new LWRRod("P"
								+ String.format("%1$02d", pinNumber));

						Ring clad = new Ring("Clad", cladMaterial, 1.0, 0.418,
								outerRadius);

						MaterialBlock stack = new MaterialBlock();
						Ring fillGas = new Ring("Fill Gas", fillGasMaterial,
								1.0, 0.4096, 0.418);
						stack.addRing(fillGas);
						Ring pellet = new Ring("Pellet", stackMaterial, 1.0,
								0.4096);
						stack.addRing(pellet);

						TreeSet<MaterialBlock> blocks = new TreeSet<MaterialBlock>();
						blocks.add(stack);

						rod.setFillGas(fillGasMaterial);
						rod.setMaterialBlocks(blocks);
						rod.setClad(clad);

						if (data.getValue() != 0.0) {
							rod.addData(data, 0.0);
						}

						fuelAssembly.addLWRRod(rod);
						fuelAssembly.setLWRRodLocation(rod.getName(), row,
								column);

						pinNumber++;

					} else if (outerRadius == 0.6050) {

						Tube tube = new Tube("IT", TubeType.INSTRUMENT,
								tubeMaterial, 1.0, 0.559, 0.605);
						fuelAssembly.addTube(tube);
						fuelAssembly.setTubeLocation(tube.getName(), row,
								column);

					} else if (outerRadius == 0.602) {

						Tube tube = new Tube("GT", TubeType.GUIDE,
								tubeMaterial, 1.0, 0.561, 0.602);
						fuelAssembly.addTube(tube);
						fuelAssembly.setTubeLocation(tube.getName(), row,
								column);

					}

				}

				if (column == 16) {
					column = 8;
					row++;
				} else {
					column++;
				}

			}

			// Open the denovo test data
			File denovoDataFile = new File(System.getProperty("user.dir")
					+ separator + "data" + separator + "denovo_results2.csv");

			String denovoDataFileContents = new String(
					this.readFile(denovoDataFile));

			String[] denovoDataLines = denovoDataFileContents.split("\n");

			for (int i = 0; i < denovoDataLines.length; i++) {

				String[] valueArray = denovoDataLines[i].split(",");

				column = 8;
				row = 8;

				for (int j = 0; j < valueArray.length; j++) {

					LWRData data = new LWRData("Axial Pin Power");

					if ((row == 8 && column == 8) || (row == 11 && column == 8)
							|| (row == 11 && column == 11)
							|| (row == 13 && column == 13)
							|| (row == 14 && column == 8)
							|| (row == 14 && column == 11)) {

						j--;

						data.setValue(0.0);

					} else {

						data.setValue(Double.valueOf(valueArray[j]));

					}

					LWRRod rod = fuelAssembly.getLWRRodByLocation(row, column);
					if (data.getValue() != 0.0) {
						rod.addData(data, 0.0);
					}

					if (column == row) {
						column = 7;
						row++;
					}

					column++;

				}

			}

			convertOctantToQuarterSymmetry(fuelAssembly);
			convertQuarterToFullSymmetry(fuelAssembly);

			// Add the fuel assembly to the reactor
			pwReactor.addAssembly(AssemblyType.Fuel, fuelAssembly);
			pwReactor.setAssemblyLocation(AssemblyType.Fuel,
					fuelAssembly.getName(), 7, 7);

			return pwReactor;

		} catch (IOException e) {

			e.printStackTrace();
			return null;

		}
	}

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

	private void convertOctantToQuarterSymmetry(
			PressurizedWaterReactor pWReactor) {

		int size = pWReactor.getSize();
		int lastIndex = size - 1;
		int limit = lastIndex / 2;

		for (int row = lastIndex; row >= limit; row--) {

			for (int col = lastIndex; col >= limit; col--) {

				if (col <= row) {

					ControlBank controlBank = (ControlBank) pWReactor
							.getAssemblyByLocation(AssemblyType.ControlBank,
									row, col);
					if (controlBank != null) {
						String name = controlBank.getName();
						pWReactor.setAssemblyLocation(AssemblyType.ControlBank,
								name, col, row);
					}

				}

			}

		}

	}

	private void convertQuarterToOctantSymmetry(FuelAssembly fuelAssembly) {

		int size = fuelAssembly.getSize();
		int lastIndex = size - 1;

		for (int row = lastIndex; row >= 8; row--) {

			for (int col = lastIndex; col >= 8; col--) {

				LWRRod rod = fuelAssembly.getLWRRodByLocation(row, col);
				String name = rod.getName();

				if (col > row) {
					fuelAssembly.removeLWRRod(name);
				}

			}

		}

	}

	private void convertOctantToQuarterSymmetry(FuelAssembly fuelAssembly) {

		int size = fuelAssembly.getSize();
		int lastIndex = size - 1;

		for (int row = lastIndex; row >= 8; row--) {

			for (int col = lastIndex; col >= 8; col--) {

				if (col <= row) {

					LWRRod rod = fuelAssembly.getLWRRodByLocation(row, col);
					if (rod != null) {
						String name = rod.getName();
						fuelAssembly.setLWRRodLocation(name, col, row);
					}

					Tube tube = fuelAssembly.getTubeByLocation(row, col);
					if (tube != null) {
						String name = tube.getName();
						fuelAssembly.setTubeLocation(name, col, row);
					}

				}

			}

		}

	}

	private void convertQuarterToFullSymmetry(FuelAssembly fuelAssembly) {

		int size = fuelAssembly.getSize();
		int lastIndex = size - 1;

		for (int row = lastIndex; row >= 8; row--) {

			for (int col = lastIndex; col >= 8; col--) {

				LWRRod rod = fuelAssembly.getLWRRodByLocation(row, col);

				if (rod != null) {

					String name = rod.getName();

					// QI
					fuelAssembly.setLWRRodLocation(name, lastIndex - row,
							lastIndex - col);

					// Q2
					fuelAssembly.setLWRRodLocation(name, lastIndex - row, col);

					// Q3
					fuelAssembly.setLWRRodLocation(name, row, lastIndex - col);

				}

				Tube tube = fuelAssembly.getTubeByLocation(row, col);
				if (tube != null) {

					String name = tube.getName();

					// QI
					fuelAssembly.setTubeLocation(name, lastIndex - row,
							lastIndex - col);

					// Q2
					fuelAssembly.setTubeLocation(name, lastIndex - row, col);

					// Q3
					fuelAssembly.setTubeLocation(name, row, lastIndex - col);

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
