/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *******************************************************************************/
package org.eclipse.ice.reactor.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.MaterialType;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.hdf.LWRIOHandler;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;
import org.junit.Before;
import org.junit.Test;

public class LWRIOHandlerTester {

	private LWRIOHandler handler;

	private PressurizedWaterReactor expectedReactor;
	private FuelAssembly expectedAssembly;
	private LWRData expectedAssemblyData;
	private LWRRod expectedRod;
	private Tube expectedTube;
	private Material expectedGasMaterial;
	private Ring expectedClad;
	private Material expectedSolidMaterial;
	private Material expectedLiquidMaterial;
	private MaterialBlock expectedMaterialBlock;

	private static final double epsilon = 1e-7;

	@Before
	public void beforeEachTest() {
		handler = new LWRIOHandler();

		int idCounter = 1;

		// Create a reactor.
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(7);
		reactor.setName("Test Reactor");
		reactor.setId(idCounter++);
		reactor.setFuelAssemblyPitch(1.0);
		expectedReactor = reactor;

		// Add a FuelAssembly to the reactor.
		FuelAssembly fuelAssembly = new FuelAssembly(5);
		fuelAssembly.setName("Test Assembly 1");
		fuelAssembly.setId(idCounter++);
		fuelAssembly.setRodPitch(2.0);
		reactor.addAssembly(AssemblyType.Fuel, fuelAssembly);
		reactor.setAssemblyLocation(AssemblyType.Fuel, fuelAssembly.getName(),
				0, 0);
		expectedAssembly = fuelAssembly;

		// Add some data to the assembly.
		LWRData data = new LWRData();
		data.setFeature("Test Feature Data");
		data.setUncertainty(0.05);
		ArrayList<Double> position = new ArrayList<Double>(3);
		position.add(0.0);
		position.add(1.0);
		position.add(2.0);
		data.setPosition(position);
		data.setValue(42.0);
		fuelAssembly.addData(data, 0.0);
		expectedAssemblyData = data;

		// Create a fill gas.
		Material gasMaterial = new Material();
		gasMaterial.setName("He");
		gasMaterial.setId(idCounter++);
		gasMaterial.setMaterialType(MaterialType.GAS);
		expectedGasMaterial = gasMaterial;

		// Create a solid material.
		Material solidMaterial = new Material();
		solidMaterial.setName("Steel");
		solidMaterial.setId(idCounter++);
		solidMaterial.setMaterialType(MaterialType.SOLID);
		expectedSolidMaterial = solidMaterial;

		// Create a liquid material.
		Material liquidMaterial = new Material();
		liquidMaterial.setName("Water");
		liquidMaterial.setId(idCounter++);
		liquidMaterial.setMaterialType(MaterialType.LIQUID);
		expectedLiquidMaterial = liquidMaterial;

		// Create a clad.
		Ring clad = new Ring();
		clad.setName("Test Clad");
		clad.setId(idCounter++);
		clad.setMaterial(solidMaterial);
		clad.setHeight(50.0);
		clad.setOuterRadius(10.0);
		clad.setInnerRadius(9.0);
		expectedClad = clad;

		// Create a MaterialBlock.
		MaterialBlock block = new MaterialBlock();
		block.setName("Test Material Block");
		block.setId(idCounter++);
		block.addRing(clad);
		// Add a ring for the gas material.
		Ring ring = new Ring();
		ring.setName("Gas Ring");
		ring.setMaterial(gasMaterial);
		ring.setHeight(50.0);
		ring.setOuterRadius(9.0);
		ring.setInnerRadius(8.9);
		block.addRing(ring);
		// Add a ring for the liquid material.
		ring = new Ring();
		ring.setName("Liquid Ring");
		ring.setMaterial(liquidMaterial);
		ring.setHeight(50.0);
		ring.setOuterRadius(8.9);
		ring.setInnerRadius(0.0);
		block.addRing(ring);

		TreeSet<MaterialBlock> materialBlocks = new TreeSet<MaterialBlock>();
		materialBlocks.add(block);

		// Add a rod to the assembly.
		LWRRod rod = new LWRRod();
		rod.setName("Test Rod 1");
		rod.setId(idCounter++);
		rod.setFillGas(gasMaterial);
		rod.setClad(clad);
		rod.setMaterialBlocks(materialBlocks);
		fuelAssembly.addLWRRod(rod);
		for (int row = 0; row < fuelAssembly.getSize(); row++) {
			for (int column = 0; column < fuelAssembly.getSize(); column += 3) {
				fuelAssembly.setLWRRodLocation(rod.getName(), row, column);
			}
		}
		expectedRod = rod;

		// Add another rod to the assembly.
		rod = new LWRRod();
		rod.setName("Test Rod 2");
		rod.setId(idCounter++);
		fuelAssembly.addLWRRod(rod);
		for (int row = 0; row < fuelAssembly.getSize(); row++) {
			for (int column = 1; column < fuelAssembly.getSize(); column += 3) {
				fuelAssembly.setLWRRodLocation(rod.getName(), row, column);
			}
		}

		// Add a tube to the assembly.
		Tube tube = new Tube();
		tube.setName("Test Tube");
		tube.setId(idCounter++);
		tube.setMaterial(solidMaterial);
		tube.setHeight(70.0);
		tube.setOuterRadius(40.0);
		tube.setInnerRadius(0.0);
		fuelAssembly.addTube(tube);
		for (int row = 0; row < fuelAssembly.getSize(); row++) {
			for (int column = 2; column < fuelAssembly.getSize(); column += 3) {
				fuelAssembly.setTubeLocation(tube.getName(), row, column);
			}
		}
		expectedTube = tube;

		// Add data for the rod at every location in the assembly. Utilize 10 z
		// positions and 3 timesteps.
		for (int x = 0; x < fuelAssembly.getSize(); x++) {
			for (int y = 0; y < fuelAssembly.getSize(); y++) {
				LWRDataProvider dataProvider = fuelAssembly
						.getLWRRodDataProviderAtLocation(x, y);
				if (dataProvider == null) {
					dataProvider = fuelAssembly.getTubeDataProviderAtLocation(x,
							y);
				}
				for (int z = 0; z < 10; z++) {
					for (double t = 0; t < 3.0; t++) {
						// Add data for a first feature.
						data = new LWRData("z");
						position = new ArrayList<Double>();
						position.add((double) x);
						position.add((double) y);
						position.add((double) z);
						data.setPosition(position);
						data.setUncertainty(0.0);
						data.setUnits("z units");
						data.setValue(z);
						dataProvider.addData(data, t);

						// Add data for a second feature.
						data = new LWRData("f(z) = z^2");
						position = new ArrayList<Double>();
						position.add((double) x);
						position.add((double) y);
						position.add((double) z);
						data.setPosition(position);
						data.setUncertainty(0.0);
						data.setUnits("z units");
						data.setValue(z * z);
						dataProvider.addData(data, t);
					}
				}
			}
		}

		// Add another FuelAssembly.
		fuelAssembly = new FuelAssembly(2);
		fuelAssembly.setName("Test Assembly 2");
		fuelAssembly.setId(idCounter++);
		fuelAssembly.addLWRRod(rod);
		fuelAssembly.addTube(tube);
		fuelAssembly.setLWRRodLocation(rod.getName(), 0, 0);
		fuelAssembly.setTubeLocation(tube.getName(), 1, 1);
		reactor.addAssembly(AssemblyType.Fuel, fuelAssembly);
		reactor.setAssemblyLocation(AssemblyType.Fuel, fuelAssembly.getName(),
				1, 1);
		reactor.setAssemblyLocation(AssemblyType.Fuel, fuelAssembly.getName(),
				2, 2);

		// Add a ControlBank.
		ControlBank controlBank = new ControlBank();
		controlBank.setName("Test Control Bank");
		controlBank.setId(idCounter++);
		controlBank.setMaxNumberOfSteps(10);
		controlBank.setStepSize(1.3);
		reactor.addAssembly(AssemblyType.ControlBank, controlBank);
		reactor.setAssemblyLocation(AssemblyType.ControlBank,
				controlBank.getName(), 0, 1);

		// Add an IncoreInstrument.
		IncoreInstrument incoreInstrument = new IncoreInstrument();
		incoreInstrument.setName("Test Incore Instrument");
		incoreInstrument.setId(idCounter++);
		incoreInstrument.setThimble(expectedClad);
		reactor.addAssembly(AssemblyType.IncoreInstrument, incoreInstrument);
		reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				incoreInstrument.getName(), 0, 2);

		// Add a RodClusterAssembly.
		RodClusterAssembly rodClusterAssembly = new RodClusterAssembly(1);
		rodClusterAssembly.setName("Test RCA");
		rodClusterAssembly.setId(idCounter++);
		rodClusterAssembly.addLWRRod(rod);
		rodClusterAssembly.setLWRRodLocation(rod.getName(), 0, 0);
		reactor.addAssembly(AssemblyType.RodCluster, rodClusterAssembly);
		reactor.setAssemblyLocation(AssemblyType.RodCluster,
				rodClusterAssembly.getName(), 0, 3);

		// LWRComponentWriter w = new LWRComponentWriter();
		// w.write(reactor, new File("C:\\oldFormatReactor.h5").toURI());

		return;
	}

	@Test
	public void checkRead() {

		String s = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.home") + s
				+ "ICETests" + s + "reactorData" + s + "oldFormatReactor.h5");
		URI uri = dataFile.toURI();

		// Read in the reactor.
		List<LWRComponent> components = handler.readHDF5(uri);
		assertNotNull(components);
		assertEquals(1, components.size());
		assertTrue(components.get(0) instanceof PressurizedWaterReactor);
		PressurizedWaterReactor reactor = (PressurizedWaterReactor) components
				.get(0);

		// The returned object should not be null.
		assertNotNull(reactor);

		// ---- Check the reactor's attributes. ---- //
		// Inherited properties...
		assertEquals(expectedReactor.getName(), reactor.getName());
		assertEquals(expectedReactor.getId(), reactor.getId());
		assertEquals(expectedReactor.getDescription(),
				reactor.getDescription());
		assertEquals(expectedReactor.getSize(), reactor.getSize());

		// Reactor-specific properties...
		assertEquals(expectedReactor.getFuelAssemblyPitch(),
				reactor.getFuelAssemblyPitch(), epsilon);

		// ----------------------------------------- //

		// ---- Check its lone fuel assembly's attributes. ---- //
		FuelAssembly assembly;

		// Get the assembly from the loaded reactor.
		assertEquals(2, reactor.getNumberOfAssemblies(AssemblyType.Fuel));
		assembly = (FuelAssembly) reactor
				.getAssemblyByLocation(AssemblyType.Fuel, 0, 0);

		// Inherited properties...
		assertNotNull(assembly);
		assertEquals(expectedAssembly.getName(), assembly.getName());
		assertEquals(expectedAssembly.getId(), assembly.getId());
		assertEquals(expectedAssembly.getDescription(),
				assembly.getDescription());
		assertEquals(expectedAssembly.getSize(), assembly.getSize());

		// Assembly-specific properties...
		assertEquals(expectedAssembly.getRodPitch(), assembly.getRodPitch(),
				epsilon);

		// Compare some state point data added directly to the assembly.
		String dataName = "Test Feature Data";
		IData expectedData = expectedAssembly.getDataAtCurrentTime(dataName)
				.get(0);
		// Make sure the data exists in the loaded assembly.
		List<IData> dataList = assembly.getDataAtCurrentTime(dataName);
		assertNotNull(dataList);
		assertEquals(1, dataList.size());
		// Finally, we can check the data.
		IData data = dataList.get(0);
		assertEquals(expectedData.getValue(), data.getValue(), epsilon);
		assertEquals(expectedData.getUncertainty(), data.getUncertainty(),
				epsilon);
		assertEquals(expectedData.getFeature(), data.getFeature());
		for (int i = 0; i < 3; i++) {
			assertEquals(expectedData.getPosition().get(i),
					data.getPosition().get(i), epsilon);
		}
		// ---------------------------------------------------- //

		// ---- Check the rod's attributes. ---- //
		LWRRod rod;

		// Get the rod from the loaded assembly.
		assertEquals(2, assembly.getNumberOfLWRRods());
		rod = assembly.getLWRRodByLocation(0, 0);

		// Inherited properties...
		assertNotNull(rod);
		assertEquals(expectedRod.getName(), rod.getName());
		assertEquals(expectedRod.getId(), rod.getId());
		assertEquals(expectedRod.getDescription(), rod.getDescription());

		// Rod-specific properties...
		// TODO

		// ------------------------------------- //

		return;
	}

}
