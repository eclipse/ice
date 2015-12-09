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
package org.eclipse.ice.reactor.test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.TreeSet;

import org.eclipse.ice.io.hdf.IHdfReadable;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.LWRComponentReader;
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
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Eric J. Lingerfelt
 */
public class LWRComponentReaderTester {
	/**
	 *
	 */
	@BeforeClass
	public static void beforeClass() {

	}

	/**
	 * <p>
	 * Creates and returns the same reactor used to write the file
	 * data/reader_test.h5
	 * </p>
	 *
	 * @return
	 * 		<p>
	 *         The same reactor used to write the file data/reader_test.h5
	 *         </p>
	 */
	private PressurizedWaterReactor createPWReactor() {

		// Create a reactor of size 2 X 2
		PressurizedWaterReactor pwReactor = new PressurizedWaterReactor(15);
		pwReactor.setName("PWR Reactor");
		pwReactor.setFuelAssemblyPitch(0.12345678912345);

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

		// Assign the label arrays
		pwReactor.getGridLabelProvider().setRowLabels(rowLabelsReactor);
		pwReactor.getGridLabelProvider().setColumnLabels(columnLabelsReactor);

		// CONTROL
		// BANKS//////////////////////////////////////////////////////////
		// Add two control banks to the reactor
		pwReactor.addAssembly(AssemblyType.ControlBank,
				new ControlBank("A", 0.625, 230));
		pwReactor.addAssembly(AssemblyType.ControlBank,
				new ControlBank("B", 0.625, 215));
		pwReactor.addAssembly(AssemblyType.ControlBank,
				new ControlBank("C", 0.625, 200));
		pwReactor.addAssembly(AssemblyType.ControlBank,
				new ControlBank("D", 0.625, 185));

		// Assign a position for the control banks
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 4, 4);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 4, 10);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 5, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 7, 5);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 7, 9);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 9, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 10, 4);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "A", 10, 10);

		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 1, 5);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 1, 9);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 5, 1);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 5, 13);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 9, 1);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 9, 13);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 13, 5);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "B", 13, 9);

		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 1, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 5, 5);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 5, 9);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 7, 1);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 7, 13);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 9, 5);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 9, 9);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "C", 13, 7);

		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 3, 3);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 3, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 3, 11);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 7, 3);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 7, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 7, 11);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 11, 3);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 11, 7);
		pwReactor.setAssemblyLocation(AssemblyType.ControlBank, "D", 11, 11);

		// INCORE
		// INSTRUMENTS/////////////////////////////////////////////////////
		// Create incore instruments for the reactor
		IncoreInstrument incoreInstrument1 = new IncoreInstrument();
		incoreInstrument1.setName("Incore Instrument 1");
		IncoreInstrument incoreInstrument2 = new IncoreInstrument();
		incoreInstrument2.setName("Incore Instrument 2");
		IncoreInstrument incoreInstrument3 = new IncoreInstrument();
		incoreInstrument3.setName("Incore Instrument 3");
		IncoreInstrument incoreInstrument4 = new IncoreInstrument();
		incoreInstrument4.setName("Incore Instrument 4");

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

		// FUEL
		// ASSEMBLIES////////////////////////////////////////////////////////
		// Create a fuel assembly
		FuelAssembly fuelAssembly = new FuelAssembly("Fuel Assembly A", 17);

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
		fuelAssembly.getGridLabelProvider()
				.setColumnLabels(columnLabelsFuelAssembly);

		// Create a guide tube
		Tube guideTube = new Tube("Guide Tube A", TubeType.GUIDE);
		guideTube.setHeight(1.56);
		guideTube.setInnerRadius(7.89);
		guideTube.setOuterRadius(10.0);

		// Create material for the guide tube
		Material materialGuideTube = new Material("Guide Tube Material");

		// Set the material for the guide tube
		guideTube.setMaterial(materialGuideTube);

		// Add the guide tube to the fuel assembly
		fuelAssembly.addTube(guideTube);

		// Assign the guide tube a location
		fuelAssembly.setTubeLocation(guideTube.getName(), 8, 13);

		// Create an instrument tube
		Tube instrumentTube = new Tube("Instrument Tube A",
				TubeType.INSTRUMENT);
		instrumentTube.setHeight(1.2);
		instrumentTube.setInnerRadius(0.987);
		instrumentTube.setOuterRadius(34.5);

		// Create material for the instrument tube
		Material materialInstrumentTube = new Material(
				"Instrument Tube Material");

		// Set the material for the instrument tube
		instrumentTube.setMaterial(materialInstrumentTube);

		// Add the instrument tube to the fuel assembly
		fuelAssembly.addTube(instrumentTube);

		// Assign the instrument tube a location
		fuelAssembly.setTubeLocation(instrumentTube.getName(), 8, 8);

		// Create an lwrrod for this fuel assembly
		LWRRod rod = new LWRRod("LWRRod A");
		rod.setPressure(23.56);

		// Create a fill gas for the rod
		Material fillGas = new Material("He", MaterialType.GAS);
		rod.setFillGas(fillGas);

		// Create a materialBlock for the rod
		MaterialBlock materialBlock = new MaterialBlock();
		materialBlock.setName("Stack of Cards");

		// Create some rings for the materialBlock
		Ring ring1 = new Ring("Ring 1");
		ring1.setHeight(155);
		ring1.setOuterRadius(0.5);

		// Create a material for ring1
		Material ring1Material = new Material("Ring 1 Material",
				MaterialType.SOLID);
		ring1.setMaterial(ring1Material);

		// Create some rings for the materialBlock
		Ring ring2 = new Ring("Ring 2");
		ring2.setHeight(155);
		ring2.setInnerRadius(0.5);
		ring2.setOuterRadius(1.0);

		// Create a material for ring1
		Material ring2Material = new Material("Ring 2 Material",
				MaterialType.SOLID);
		ring2.setMaterial(ring2Material);

		// Add rings to the materialBlock
		materialBlock.addRing(ring1);
		materialBlock.addRing(ring2);

		TreeSet<MaterialBlock> materialBlockList = new TreeSet<MaterialBlock>();
		materialBlockList.add(materialBlock);

		// Set the MaterialBlock in the rod
		rod.setMaterialBlocks(materialBlockList);

		// Create a clad
		Ring clad = new Ring("Clad");
		clad.setHeight(155);
		clad.setInnerRadius(0.9);
		clad.setOuterRadius(1.0);

		// Create a material for the clad
		Material materialClad = new Material("Clad Material",
				MaterialType.SOLID);
		clad.setMaterial(materialClad);

		// Add the clad
		rod.setClad(clad);

		// Add the rod to the fuel assembly
		fuelAssembly.addLWRRod(rod);

		// Assign the rod a location
		fuelAssembly.setLWRRodLocation(rod.getName(), 15, 4);

		// Add the fuel assembly to the reactor
		pwReactor.addAssembly(AssemblyType.Fuel, fuelAssembly);

		// Assign a position on the grid of the reactor
		pwReactor.setAssemblyLocation(AssemblyType.Fuel, fuelAssembly.getName(),
				4, 4);

		// ROD CLUSTER
		// ASSEMBLIES/////////////////////////////////////////////////
		// Create a rca
		RodClusterAssembly rodClusterAssembly = new RodClusterAssembly(
				"Rod Cluster Assembly A", 17);

		// Add the rca to the reactor
		pwReactor.addAssembly(AssemblyType.RodCluster, rodClusterAssembly);

		// Assign the rca location
		pwReactor.setAssemblyLocation(AssemblyType.RodCluster,
				rodClusterAssembly.getName(), 5, 2);

		// Add LWRData
		// Setup LWRData
		String feature1 = "Feature 1";
		String feature2 = "Feature 2";
		double time1 = 1.0, time2 = 3.0, time3 = 3.5;
		LWRData data1, data2, data3, data4, data5;
		ArrayList<Double> position1 = new ArrayList<Double>(),
				position2 = new ArrayList<Double>(),
				position3 = new ArrayList<Double>(),
				position4 = new ArrayList<Double>(),
				position5 = new ArrayList<Double>();

		// Setup Positions

		// Setup Position 1
		position1.add(0.0);
		position1.add(1.0);
		position1.add(0.0);

		// Setup Position 2
		position2.add(0.0);
		position2.add(1.0);
		position2.add(4.0);

		// Setup Position 3
		position3.add(1.0);
		position3.add(1.0);
		position3.add(0.0);

		// Setup Position 4
		position4.add(0.0);
		position4.add(1.0);
		position4.add(1.0);

		// Setup Position 5
		position4.add(0.0);
		position4.add(1.0);
		position4.add(3.0);

		// Setup data1
		data1 = new LWRData(feature1);
		data1.setPosition(position1);
		data1.setValue(1.0);
		data1.setUncertainty(1.5);
		data1.setUnits("Units " + 123456);

		// Setup data2
		data2 = new LWRData(feature1);
		data2.setPosition(position2);
		data2.setValue(2.0);
		data2.setUncertainty(2.5);
		data2.setUnits("Units " + 2);

		// Setup data3
		data3 = new LWRData(feature1);
		data3.setPosition(position3);
		data3.setValue(3.0);
		data3.setUncertainty(3.5);
		data3.setUnits("Units " + 3);

		// Setup data4
		data4 = new LWRData(feature1);
		data4.setPosition(position4);
		data4.setValue(4.0);
		data4.setUncertainty(4.5);
		data4.setUnits("Units " + 4);

		// Setup data5
		data5 = new LWRData(feature2);
		data5.setPosition(position5);
		data5.setValue(5.0);
		data5.setUncertainty(5.5);
		data5.setUnits("Units " + 5);

		pwReactor.addData(data1, time1);
		pwReactor.addData(data2, time1);
		pwReactor.addData(data3, time2);
		pwReactor.addData(data4, time3);
		pwReactor.addData(data5, time3);

		return pwReactor;

	}

	/**
	 *
	 */
	@Test
	public void checkReading() {

		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "reactorData";
		File dataFile = new File(userDir + separator + "reader_test.h5");
		URI uri = dataFile.toURI();

		// Create a new read
		LWRComponentReader lWRComponentReader = new LWRComponentReader();

		// Read from the URI
		IHdfReadable iHdfReadable = lWRComponentReader.read(uri);

		// Cast to a PWReactor
		PressurizedWaterReactor reactor = (PressurizedWaterReactor) iHdfReadable;
		assertNotNull(reactor);

		// Check values here
		assertTrue(createPWReactor().equals(reactor));

	}
}