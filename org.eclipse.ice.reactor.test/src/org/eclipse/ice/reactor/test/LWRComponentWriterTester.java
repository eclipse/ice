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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.TreeSet;

import org.eclipse.ice.io.hdf.HdfFileFactory;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.LWRComponentWriter;
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

import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.Group;
import ncsa.hdf.object.h5.H5File;

/**
 *
 * @author Eric J. Lingerfelt
 */
public class LWRComponentWriterTester {

	/**
	 *
	 */
	@BeforeClass
	public static void beforeClass() {

	}

	/**
	 *
	 */
	@Test
	public void checkWriting() {

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

		// Try it with a non-null argument
		// Local declarations
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "reactorData";
		File dataFile = new File(userDir + separator + "writer_test.h5");
		URI uri = dataFile.toURI();

		LWRComponentWriter lWRComponentWriter = new LWRComponentWriter();
		assertNotNull(lWRComponentWriter);

		// write the reactor
		boolean flag = lWRComponentWriter.write(pwReactor, uri);
		assertTrue(flag);

		// Make sure the file exists now
		assertTrue(dataFile.exists());

		// Retrieve an instance of the HDF5 format
		FileFormat fileFormat = FileFormat
				.getFileFormat(FileFormat.FILE_TYPE_HDF5);

		// Open the file with read/write access and return it.
		H5File newH5File = null;
		try {
			// Open the file in READ ONLY mode
			newH5File = (H5File) fileFormat.createInstance(
					dataFile.getAbsolutePath(), FileFormat.READ);
			newH5File = HdfFileFactory.openH5File(dataFile.toURI());
			// Make sure it exists
			assertTrue(newH5File.exists());
			assertTrue(newH5File.canRead());
			// Then open it
			newH5File.open();
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		assertNotNull(newH5File);
		// Now, we will open up the file and reverse examine to see if every
		// group is in there

		// Get the root group
		Group root = (Group) ((javax.swing.tree.DefaultMutableTreeNode) newH5File
				.getRootNode()).getUserObject();

		// Now check each group node
		// Check for Root PWReactor
		assertEquals("PWR Reactor", root.getMemberList().get(0).getName());

		// Check PWRReactor's subpieces
		Group pWReactor = (Group) root.getMemberList().get(0);
		assertEquals(10, pWReactor.getMemberList().size());
		assertEquals("Control Bank Grid",
				pWReactor.getMemberList().get(0).getName());
		assertEquals("Control Banks",
				pWReactor.getMemberList().get(1).getName());
		assertEquals("Fuel Assemblies",
				pWReactor.getMemberList().get(2).getName());
		assertEquals("Fuel Assembly Grid",
				pWReactor.getMemberList().get(3).getName());
		assertEquals("Grid Labels", pWReactor.getMemberList().get(4).getName());
		assertEquals("Incore Instrument Grid",
				pWReactor.getMemberList().get(5).getName());
		assertEquals("Incore Instruments",
				pWReactor.getMemberList().get(6).getName());
		assertEquals("Rod Cluster Assemblies",
				pWReactor.getMemberList().get(7).getName());
		assertEquals("Rod Cluster Assembly Grid",
				pWReactor.getMemberList().get(8).getName());
		assertEquals("State Point Data",
				pWReactor.getMemberList().get(9).getName());

		// Check the Control Bank Grid
		Group controlBankGrid = (Group) pWReactor.getMemberList().get(0);
		assertEquals(2, controlBankGrid.getMemberList().size());
		// Check Control Bank Grid Information
		assertEquals("Positions",
				controlBankGrid.getMemberList().get(0).getName());
		assertEquals("State Point Data",
				controlBankGrid.getMemberList().get(1).getName());
		// information below
		assertTrue(controlBankGrid.getMemberList().get(0) instanceof Group);

		// Check Control Banks
		Group controlBank = (Group) pWReactor.getMemberList().get(1);
		assertEquals(5, controlBank.getMemberList().size());
		// Check Control Bank Information
		assertEquals("A", controlBank.getMemberList().get(0).getName());
		assertEquals("B", controlBank.getMemberList().get(1).getName());
		assertEquals("C", controlBank.getMemberList().get(2).getName());
		assertEquals("D", controlBank.getMemberList().get(3).getName());
		assertEquals("State Point Data",
				controlBank.getMemberList().get(4).getName());
		// Check Groups
		Group A = (Group) controlBank.getMemberList().get(0);
		Group B = (Group) controlBank.getMemberList().get(1);
		Group C = (Group) controlBank.getMemberList().get(2);
		Group D = (Group) controlBank.getMemberList().get(3);
		// Check contents of Groups
		assertEquals(1, A.getMemberList().size());
		assertEquals("State Point Data", A.getMemberList().get(0).getName());
		assertEquals(1, B.getMemberList().size());
		assertEquals("State Point Data", B.getMemberList().get(0).getName());
		assertEquals(1, C.getMemberList().size());
		assertEquals("State Point Data", C.getMemberList().get(0).getName());
		assertEquals(1, D.getMemberList().size());
		assertEquals("State Point Data", D.getMemberList().get(0).getName());

		// Check Fuel Assemblies
		Group fuelAssemblies = (Group) pWReactor.getMemberList().get(2);
		assertEquals(2, fuelAssemblies.getMemberList().size());
		// Check Fuel Assemblies Information
		assertEquals("Fuel Assembly A",
				fuelAssemblies.getMemberList().get(0).getName());
		assertEquals("State Point Data",
				fuelAssemblies.getMemberList().get(1).getName());
		// Check Groups
		Group fuelAssemblyA = (Group) fuelAssemblies.getMemberList().get(0);
		// Check contents of FuelAssemblyA
		assertEquals(6, fuelAssemblyA.getMemberList().size());
		assertEquals("Grid Labels",
				fuelAssemblyA.getMemberList().get(0).getName());
		assertEquals("LWRRod Grid",
				fuelAssemblyA.getMemberList().get(1).getName());
		assertEquals("LWRRods", fuelAssemblyA.getMemberList().get(2).getName());
		assertEquals("State Point Data",
				fuelAssemblyA.getMemberList().get(3).getName());
		assertEquals("Tube Grid",
				fuelAssemblyA.getMemberList().get(4).getName());
		assertEquals("Tubes", fuelAssemblyA.getMemberList().get(5).getName());

		// Check Groups of FuelAssembly A
		Group gridLabels = (Group) fuelAssemblyA.getMemberList().get(0);
		Group lWRRodGrid = (Group) fuelAssemblyA.getMemberList().get(1);
		Group lWRRods = (Group) fuelAssemblyA.getMemberList().get(2);
		// 3rd one here is state point data
		Group tubeGrid = (Group) fuelAssemblyA.getMemberList().get(4);
		Group tubes = (Group) fuelAssemblyA.getMemberList().get(5);
		// Check Groups
		assertEquals(2, gridLabels.getMemberList().size());
		assertEquals(2, lWRRodGrid.getMemberList().size());
		assertEquals(2, lWRRods.getMemberList().size());
		assertEquals(2, tubeGrid.getMemberList().size());
		assertEquals(3, tubes.getMemberList().size());
		// Check gridLabels
		Group labelsGroup = (Group) gridLabels.getMemberList().get(0);

		assertEquals("Column Labels",
				labelsGroup.getMemberList().get(0).getName());
		assertEquals("Row Labels",
				labelsGroup.getMemberList().get(1).getName());
		// Check Groups of GridLabels
		assertFalse(labelsGroup.getMemberList().get(0) instanceof Group);
		assertFalse(labelsGroup.getMemberList().get(1) instanceof Group);
		// Check lWRRodGrid
		assertEquals("Positions", lWRRodGrid.getMemberList().get(0).getName());
		assertEquals("State Point Data",
				lWRRodGrid.getMemberList().get(1).getName());
		assertTrue(lWRRodGrid.getMemberList().get(0) instanceof Group);
		// Check LWRRods
		assertEquals("LWRRod A", lWRRods.getMemberList().get(0).getName());
		Group lWRRodsA = (Group) lWRRods.getMemberList().get(0);
		assertEquals("State Point Data",
				lWRRods.getMemberList().get(1).getName());
		// Check lWRRodsA
		assertEquals(4, lWRRodsA.getMemberList().size());
		assertEquals("Clad", lWRRodsA.getMemberList().get(0).getName());
		assertEquals("He", lWRRodsA.getMemberList().get(1).getName());
		assertEquals("Stack of Cards",
				lWRRodsA.getMemberList().get(2).getName());
		assertEquals("State Point Data",
				lWRRodsA.getMemberList().get(3).getName());
		// Check Groups of lWRRodsA
		Group cladRodA = (Group) lWRRodsA.getMemberList().get(0);
		Group heRodA = (Group) lWRRodsA.getMemberList().get(1);
		Group materialBlockRodA = (Group) lWRRodsA.getMemberList().get(2);
		// Check cladRodA
		assertEquals(2, cladRodA.getMemberList().size());
		assertEquals("Clad Material",
				cladRodA.getMemberList().get(0).getName());
		assertEquals("State Point Data",
				cladRodA.getMemberList().get(1).getName());
		// Check Groups
		Group materialCladRodA = (Group) cladRodA.getMemberList().get(0);
		// Check materialCladRodA
		assertEquals(1, materialCladRodA.getMemberList().size());
		assertEquals("State Point Data",
				materialCladRodA.getMemberList().get(0).getName());
		// Check heRodA
		assertEquals(1, heRodA.getMemberList().size());
		assertEquals("State Point Data",
				heRodA.getMemberList().get(0).getName());
		// Check materialBlockRodA
		assertEquals(3, materialBlockRodA.getMemberList().size());
		assertEquals("Ring 1",
				materialBlockRodA.getMemberList().get(0).getName());
		assertEquals("Ring 2",
				materialBlockRodA.getMemberList().get(1).getName());
		assertEquals("State Point Data",
				materialBlockRodA.getMemberList().get(2).getName());
		// Check Groups
		Group ring1StackA = (Group) materialBlockRodA.getMemberList().get(0);
		Group ring2StackA = (Group) materialBlockRodA.getMemberList().get(1);
		// Check ring1StackA
		assertEquals(2, ring1StackA.getMemberList().size());
		assertEquals("Ring 1 Material",
				ring1StackA.getMemberList().get(0).getName());
		assertEquals("State Point Data",
				ring1StackA.getMemberList().get(1).getName());
		Group material1Ring = (Group) ring1StackA.getMemberList().get(0);
		// Check material1Ring
		assertEquals(1, material1Ring.getMemberList().size());
		assertEquals("State Point Data",
				material1Ring.getMemberList().get(0).getName());
		// Check ring2StackA
		assertEquals(2, ring2StackA.getMemberList().size());
		assertEquals("Ring 2 Material",
				ring2StackA.getMemberList().get(0).getName());
		assertEquals("State Point Data",
				ring2StackA.getMemberList().get(1).getName());
		Group material2Ring = (Group) ring2StackA.getMemberList().get(0);
		// Check material1Ring
		assertEquals(1, material2Ring.getMemberList().size());
		assertEquals("State Point Data",
				material2Ring.getMemberList().get(0).getName());
		// Check tubeGrid
		assertEquals("State Point Data",
				tubeGrid.getMemberList().get(1).getName());
		assertEquals("Positions", tubeGrid.getMemberList().get(0).getName());
		assertTrue(tubeGrid.getMemberList().get(0) instanceof Group);
		// Check tubes
		assertEquals("Guide Tube A", tubes.getMemberList().get(0).getName());
		assertEquals("Instrument Tube A",
				tubes.getMemberList().get(1).getName());
		assertEquals("State Point Data",
				tubes.getMemberList().get(2).getName());
		// Check Groups
		Group guideTubeA = (Group) tubes.getMemberList().get(0);
		Group instrTubeA = (Group) tubes.getMemberList().get(1);
		// Check guideTubeA
		assertEquals(2, guideTubeA.getMemberList().size());
		assertEquals("Guide Tube Material",
				guideTubeA.getMemberList().get(0).getName());
		assertEquals("State Point Data",
				guideTubeA.getMemberList().get(1).getName());
		Group guideTubeMaterial = (Group) guideTubeA.getMemberList().get(0);
		// Check guideTubeMaterial
		assertEquals(1, guideTubeMaterial.getMemberList().size());
		assertEquals("State Point Data",
				guideTubeMaterial.getMemberList().get(0).getName());
		// Check instrTubeA
		assertEquals(2, instrTubeA.getMemberList().size());
		assertEquals("Instrument Tube Material",
				instrTubeA.getMemberList().get(0).getName());
		assertEquals("State Point Data",
				instrTubeA.getMemberList().get(1).getName());
		Group instrTubeMaterial = (Group) instrTubeA.getMemberList().get(0);
		// Check guideTubeMaterial
		assertEquals(1, instrTubeMaterial.getMemberList().size());
		assertEquals("State Point Data",
				instrTubeMaterial.getMemberList().get(0).getName());

		// Fuel Assembly Grid
		Group fuelAssemblyGrid = (Group) pWReactor.getMemberList().get(3);
		assertEquals(2, fuelAssemblyGrid.getMemberList().size());
		// Check FuelAssembly Grid Information
		assertEquals("Positions",
				fuelAssemblyGrid.getMemberList().get(0).getName());
		// information below
		assertTrue(fuelAssemblyGrid.getMemberList().get(0) instanceof Group);

		// Grid Labels
		Group reactorGridLabels = (Group) pWReactor.getMemberList().get(4);
		assertEquals(2, reactorGridLabels.getMemberList().size());
		assertEquals("State Point Data",
				reactorGridLabels.getMemberList().get(1).getName());

		// Check gridLabels
		labelsGroup = (Group) reactorGridLabels.getMemberList().get(0);

		assertEquals("Column Labels",
				labelsGroup.getMemberList().get(0).getName());
		assertEquals("Row Labels",
				labelsGroup.getMemberList().get(1).getName());
		// Check Groups of GridLabels
		assertFalse(labelsGroup.getMemberList().get(0) instanceof Group);
		assertFalse(labelsGroup.getMemberList().get(1) instanceof Group);

		// Check Incore Instrument Grid
		Group incoreInstrumentGrid = (Group) pWReactor.getMemberList().get(5);
		// Check icoreInstrumentGrid
		assertEquals(2, incoreInstrumentGrid.getMemberList().size());
		assertEquals("State Point Data",
				incoreInstrumentGrid.getMemberList().get(1).getName());
		// Check incoreInstruments infor
		assertEquals("Positions",
				incoreInstrumentGrid.getMemberList().get(0).getName());
		// Check Groups
		assertTrue(
				incoreInstrumentGrid.getMemberList().get(0) instanceof Group);

		// Check Incore Instrument
		Group incoreInstrument = (Group) pWReactor.getMemberList().get(6);
		assertEquals(5, incoreInstrument.getMemberList().size());
		// Check incore instrument information
		assertEquals("Incore Instrument 1",
				incoreInstrument.getMemberList().get(0).getName());
		assertEquals("Incore Instrument 2",
				incoreInstrument.getMemberList().get(1).getName());
		assertEquals("Incore Instrument 3",
				incoreInstrument.getMemberList().get(2).getName());
		assertEquals("Incore Instrument 4",
				incoreInstrument.getMemberList().get(3).getName());
		assertEquals("State Point Data",
				incoreInstrument.getMemberList().get(4).getName());
		// Check Groups
		Group incore1 = (Group) incoreInstrument.getMemberList().get(0);
		Group incore2 = (Group) incoreInstrument.getMemberList().get(1);
		Group incore3 = (Group) incoreInstrument.getMemberList().get(2);
		Group incore4 = (Group) incoreInstrument.getMemberList().get(3);
		// Check incore1
		assertEquals(2, incore1.getMemberList().size());
		// Check incore1 information
		assertEquals("Thimble", incore1.getMemberList().get(1).getName());
		assertEquals("State Point Data",
				incore1.getMemberList().get(0).getName());
		// Check Groups
		Group thimble1 = (Group) incore1.getMemberList().get(1);
		// Check thimble
		assertEquals(2, thimble1.getMemberList().size());
		// Check thimble information
		assertEquals("stainless steel",
				thimble1.getMemberList().get(1).getName());
		assertEquals("State Point Data",
				thimble1.getMemberList().get(0).getName());
		// Check Groups
		Group steel1 = (Group) thimble1.getMemberList().get(0);
		// Check steel information
		assertEquals(0, steel1.getMemberList().size());
		// Check incore2 information
		assertEquals("State Point Data",
				incore2.getMemberList().get(0).getName());
		assertEquals("Thimble", incore2.getMemberList().get(1).getName());
		// Check Groups
		Group thimble2 = (Group) incore2.getMemberList().get(1);
		// Check thimble
		assertEquals(2, thimble2.getMemberList().size());
		assertEquals("State Point Data",
				thimble2.getMemberList().get(0).getName());
		// Check thimble information
		assertEquals("stainless steel",
				thimble2.getMemberList().get(1).getName());
		// Check Groups
		Group steel2 = (Group) thimble2.getMemberList().get(1);
		// Check steel information
		assertEquals(1, steel2.getMemberList().size());
		assertEquals("State Point Data",
				steel2.getMemberList().get(0).getName());
		// Check incore3 information
		assertEquals("State Point Data",
				incore3.getMemberList().get(0).getName());
		assertEquals("Thimble", incore3.getMemberList().get(1).getName());
		// Check Groups
		Group thimble3 = (Group) incore3.getMemberList().get(1);
		// Check thimble
		assertEquals(2, thimble3.getMemberList().size());
		// Check thimble information
		assertEquals("State Point Data",
				incore1.getMemberList().get(0).getName());
		assertEquals("stainless steel",
				thimble3.getMemberList().get(1).getName());
		// Check Groups
		Group steel3 = (Group) thimble3.getMemberList().get(1);
		// Check steel information
		assertEquals(1, steel3.getMemberList().size());
		assertEquals("State Point Data",
				steel3.getMemberList().get(0).getName());
		// Check incore4 information
		assertEquals("State Point Data",
				incore4.getMemberList().get(0).getName());
		assertEquals("Thimble", incore4.getMemberList().get(1).getName());
		// Check Groups
		Group thimble4 = (Group) incore4.getMemberList().get(1);
		// Check thimble
		assertEquals(2, thimble4.getMemberList().size());
		// Check thimble information
		assertEquals("State Point Data",
				thimble4.getMemberList().get(0).getName());
		assertEquals("stainless steel",
				thimble4.getMemberList().get(1).getName());
		// Check Groups
		Group steel4 = (Group) thimble4.getMemberList().get(1);
		// Check steel information
		assertEquals(1, steel4.getMemberList().size());
		assertEquals("State Point Data",
				steel4.getMemberList().get(0).getName());

		// Check Rod Cluster Assemblies
		Group rodClusterAssemblies = (Group) pWReactor.getMemberList().get(7);
		assertEquals(2, rodClusterAssemblies.getMemberList().size());
		// Check incore instrument information
		assertEquals("Rod Cluster Assembly A",
				rodClusterAssemblies.getMemberList().get(0).getName());
		assertEquals("State Point Data",
				rodClusterAssemblies.getMemberList().get(1).getName());
		// Check Groups
		Group rodClusterA = (Group) rodClusterAssemblies.getMemberList().get(0);
		// Check RodClusterA
		assertEquals(3, rodClusterA.getMemberList().size());
		// Check information
		assertEquals("LWRRod Grid",
				rodClusterA.getMemberList().get(0).getName());
		assertEquals("LWRRods", rodClusterA.getMemberList().get(1).getName());
		assertEquals("State Point Data",
				rodClusterA.getMemberList().get(2).getName());
		// Check Groups
		Group rodLWRRodGrid = (Group) rodClusterA.getMemberList().get(0);
		Group rodLWRRods = (Group) rodClusterA.getMemberList().get(1);
		// Check rodLWRRodGrid
		assertEquals(1, rodLWRRodGrid.getMemberList().size());
		// Check information
		assertEquals("State Point Data",
				rodLWRRodGrid.getMemberList().get(0).getName());
		// Check Groups
		// Check rodLWRRods
		assertEquals(1, rodLWRRods.getMemberList().size());

		// Check RodClusterAssembly Grid
		// Grid Labels
		Group rodClusterAssGrid = (Group) pWReactor.getMemberList().get(8);
		assertEquals(2, rodClusterAssGrid.getMemberList().size());
		// Check information
		assertEquals("Positions",
				rodClusterAssGrid.getMemberList().get(0).getName());
		assertEquals("State Point Data",
				rodClusterAssGrid.getMemberList().get(1).getName());
		// Check Groups
		assertTrue(rodClusterAssGrid.getMemberList().get(0) instanceof Group);

		// Try to use null in constructor - see results
		lWRComponentWriter = new LWRComponentWriter();
		assertNotNull(lWRComponentWriter);

		// write the reactor
		assertFalse(lWRComponentWriter.write(pwReactor, null));

		// Check nullaries on factory
		assertFalse(lWRComponentWriter.write(null, dataFile.toURI()));

		// Close and delete the .h5 file produced by this test.
		try {
			newH5File.close();
			dataFile.delete();
		} catch (Exception e) {
			System.err.println(
					"LWRComponentWriterTester error: Could not delete file \""
							+ dataFile.toURI().toString() + "\".");
			fail();
		}

	}

}