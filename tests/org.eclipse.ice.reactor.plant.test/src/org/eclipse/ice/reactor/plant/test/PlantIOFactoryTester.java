/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor.plant.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.io.hdf.HdfIOFactory;
import org.eclipse.ice.io.hdf.HdfIORegistry;
import org.eclipse.ice.reactor.plant.Boundary;
import org.eclipse.ice.reactor.plant.Branch;
import org.eclipse.ice.reactor.plant.CoreChannel;
import org.eclipse.ice.reactor.plant.DownComer;
import org.eclipse.ice.reactor.plant.FlowJunction;
import org.eclipse.ice.reactor.plant.GeometricalComponent;
import org.eclipse.ice.reactor.plant.HeatExchanger;
import org.eclipse.ice.reactor.plant.IdealPump;
import org.eclipse.ice.reactor.plant.Inlet;
import org.eclipse.ice.reactor.plant.Junction;
import org.eclipse.ice.reactor.plant.MassFlowInlet;
import org.eclipse.ice.reactor.plant.OneInOneOutJunction;
import org.eclipse.ice.reactor.plant.Outlet;
import org.eclipse.ice.reactor.plant.Pipe;
import org.eclipse.ice.reactor.plant.PipeToPipeJunction;
import org.eclipse.ice.reactor.plant.PipeWithHeatStructure;
import org.eclipse.ice.reactor.plant.PlantComponent;
import org.eclipse.ice.reactor.plant.PlantComposite;
import org.eclipse.ice.reactor.plant.PlantIOFactory;
import org.eclipse.ice.reactor.plant.PointKinetics;
import org.eclipse.ice.reactor.plant.Pump;
import org.eclipse.ice.reactor.plant.Reactor;
import org.eclipse.ice.reactor.plant.SeparatorDryer;
import org.eclipse.ice.reactor.plant.SolidWall;
import org.eclipse.ice.reactor.plant.SpecifiedDensityAndVelocityInlet;
import org.eclipse.ice.reactor.plant.Subchannel;
import org.eclipse.ice.reactor.plant.SubchannelBranch;
import org.eclipse.ice.reactor.plant.TDM;
import org.eclipse.ice.reactor.plant.TimeDependentJunction;
import org.eclipse.ice.reactor.plant.TimeDependentVolume;
import org.eclipse.ice.reactor.plant.Turbine;
import org.eclipse.ice.reactor.plant.Valve;
import org.eclipse.ice.reactor.plant.VolumeBranch;
import org.eclipse.ice.reactor.plant.WetWell;
import org.junit.AfterClass;
import org.junit.Test;

/**
 * Tests the {@link PlantIOFactory} and its ability to write and read
 * {@link PlantComponent}s to and from a file.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PlantIOFactoryTester {

	/**
	 * Creates one of each type of PlantComponent to make sure they can all be
	 * written to and read from HDF5 files properly.
	 */
	@Test
	public void checkComponents() {

		// ---- Create the IO registry and factory. ---- //
		// Create the factory to test.
		PlantIOFactory factory = new PlantIOFactory();

		// Create a registry that contains the factory.
		HdfIORegistry registry = new HdfIORegistry();
		registry.registerHdfIOFactory(factory);

		// Set the registry for the base ReactorIOFactory class.
		HdfIOFactory.setHdfIORegistry(registry);
		// --------------------------------------------- //

		// ---- Create the URI and objects for writing. ---- //
		// Create an output file for this test.
		String s = System.getProperty("file.separator");
		String directory = System.getProperty("user.dir") + s
				+ "ICEIOTestDirectory" + s;
		String path = directory + "fromJava.h5";

		// Create a URI for a file to write/read for the test.
		URI uri = new File(path).toURI();

		// Create the list of components to write.
		List<Object> objects = new ArrayList<Object>();
		// ------------------------------------------------- //

		// ---- Create several PlantComponents. ---- //
		PlantComponent component;

		// -- Add a pipe. -- //
		Pipe pipe = new Pipe(5.0, 3.14159);
		pipe.setName("Smash the beetles!");
		pipe.setNumElements(7);
		pipe.setPosition(new double[] { 1.0, 2.0, 3.0 });
		objects.add(pipe);
		// ----------------- //

		// -- Add a HeatExchanger. -- //
		HeatExchanger heatExchanger = new HeatExchanger();
		heatExchanger.setInnerRadius(5.1);
		objects.add(heatExchanger);
		// -------------------------- //

		// -- Add a PlantComposite. -- //
		PlantComposite plant = new PlantComposite();
		plant.setName("King's Landing");
		plant.setDescription("In King’s Landing, there are two sorts of people. The players and the pieces.");
		objects.add(plant);

		int id = 2;
		// Add some components to the plant.
		plant.addPlantComponent(pipe);

		// Add each type of pipe.
		pipe = new Pipe();
		pipe.setId(id++);
		pipe.setOrientation(new double[] { 6, 28, 496 });
		plant.addPlantComponent(pipe);

		pipe = new Subchannel();
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setName("Gregor Clegane, aka The Mountain");

		pipe = new PipeWithHeatStructure();
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setName("Sandor Clegane, aka The Hound");

		CoreChannel coreChannel = new CoreChannel();
		coreChannel.setId(id++);
		plant.addPlantComponent(coreChannel);
		coreChannel.setName("Oberyn Martell, aka The Red Viper");
		coreChannel.setDescription("Probably has a major headache.");

		// Add a reactor.
		Reactor reactor = new Reactor();
		reactor.setId(id++);
		plant.addPlantComponent(reactor);
		reactor.setName("Daenerys Targaryen");
		reactor.setDescription("dragons dragons dragons dragons... my dragons");
		ArrayList<CoreChannel> coreChannels = new ArrayList<CoreChannel>(1);
		coreChannels.add(coreChannel);
		reactor.setCoreChannels(coreChannels);

		// Add a heat exchanger.
		heatExchanger = new HeatExchanger();
		heatExchanger.setId(id++);
		plant.addPlantComponent(heatExchanger);
		heatExchanger.setName("Joffrey Baratheon");
		heatExchanger.setDescription("The character everyone loves to hate.");
		heatExchanger.setLength(0.1);

		// Add some generic components.
		GeometricalComponent geometry = new GeometricalComponent();
		geometry.setId(id++);
		plant.addPlantComponent(geometry);
		geometry.setName("Sansa Stark");
		geometry.setRotation(-1.0);

		component = new PointKinetics();
		component.setId(id++);
		plant.addPlantComponent(component);
		component.setName("Tyrion Lannister");
		component.setDescription("Everybody's favorite");

		// Add each type of junction.
		Junction junction = new Junction();
		junction.setId(id++);
		plant.addPlantComponent(junction);

		junction = new Branch();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Jamie Lannister");

		junction = new SubchannelBranch();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Cersei Lannister");

		junction = new VolumeBranch();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Tywin Lannister");

		junction = new FlowJunction();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Eddard Stark");
		junction.setDescription("He dies. Sean Bean always dies.");

		junction = new WetWell();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Arya Stark, and her trusty blade, Needle");

		junction = new Boundary();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Jon Snow. He's Jon Snow.");

		junction = new OneInOneOutJunction();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Theon Greyjoy, aka Reek");

		junction = new Turbine();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Bran Stark, a warg");

		junction = new IdealPump();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Petyr Baelish, aka Little-finger");

		junction = new Pump();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Brienne of Tarth");

		junction = new Valve();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Stannis Baratheon");

		junction = new PipeToPipeJunction();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Robert Baratheon");

		junction = new Inlet();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Khal Drogo, played by Conan the Barbarian");

		junction = new MassFlowInlet();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Daario Naharis");

		junction = new SpecifiedDensityAndVelocityInlet();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Margaery Tyrell, played by Anne Boleyn");

		junction = new Outlet();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Davos Seaworth, a former pirate... arrr");

		junction = new SolidWall();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Ygritte, and her accent");

		junction = new TDM();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Bronn, a sell-sword");

		junction = new TimeDependentJunction();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Jorah Mormont");

		junction = new TimeDependentVolume();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Robb Stark");

		junction = new DownComer();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Catelyn Stark");

		junction = new SeparatorDryer();
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.setName("Jeor Mormont, Jorah's father");
		// --------------------------- //
		// ----------------------------------------- //

		// Write the objects.
		factory.writeObjects(uri, objects);

		// Read the objects.
		List<Object> loadedObjects = factory.readObjects(uri);

		// Compare the objects with the ones read from the file. They should be
		// equivalent.
		assertNotNull(loadedObjects);
		assertEquals(objects.size(), loadedObjects.size());
		// for (int i = 0; i < objects.size(); i++) {
		// // Each object should be unique but equivalent.
		// assertNotSame(objects.get(i), loadedObjects.get(i));
		// assertEquals(objects.get(i), loadedObjects.get(i));
		// }

		// The order in which objects are stored in the h5 file are not the same
		// as the write order. For now, just do a brute force search.
		for (Object object : objects) {
			boolean found = false;
			for (Object loadedObject : loadedObjects) {
				found = object.equals(loadedObject);
				if (found) {
					if (object == loadedObject) {
						fail("PlantIOFactoryTester failure: "
								+ loadedObject.toString()
								+ " is not a new instance after reading.");
					}
					break;
				}
			}

			if (!found) {
				fail("PlantIOFactoryTester failure: "
						+ object.toString()
						+ " was not found in the list of objects read from the file.");
			}
		}

		return;
	}

	/**
	 * Creates a generic plant with several different types of components,
	 * writes it to a file, reads it in, and compares the custom plant with the
	 * plant loaded from the file.
	 */
	@Test
	public void checkPlant() {

		// ---- Create the IO registry and factory. ---- //
		// Create the factory to test.
		PlantIOFactory factory = new PlantIOFactory();

		// Create a registry that contains the factory.
		HdfIORegistry registry = new HdfIORegistry();
		registry.registerHdfIOFactory(factory);

		// Set the registry for the base ReactorIOFactory class.
		HdfIOFactory.setHdfIORegistry(registry);
		// --------------------------------------------- //

		// ---- Create the URI and objects for writing. ---- //
		// Create an output file for this test.
		String s = System.getProperty("file.separator");
		String directory = System.getProperty("user.dir") + s
				+ "ICEIOTestDirectory" + s;
		String path = directory + "testPlant.h5";

		// Create a URI for a file to write/read for the test.
		URI uri = new File(path).toURI();

		// Create the list of components to write.
		List<Object> objects = new ArrayList<Object>();
		// ------------------------------------------------- //

		Reactor reactor;
		CoreChannel coreChannel;
		Pipe pipe;
		HeatExchanger hx;
		Junction junction;
		int id = 1;

		int numElements = 10;
		double length;
		double radius = 0.25;
		double[] up = { 0, 1, 0 };
		double[] down = { 0, -1, 0 };
		double[] left = { -1, 0, 0 };
		double[] right = { 1, 0, 0 };

		ArrayList<PlantComponent> components;

		// ---- Create the plant. ---- //
		PlantComposite plant = new PlantComposite();
		plant.setName("Test Plant");
		plant.setDescription("This is a test plant modeled after TMI_2loop.");
		objects.add(plant);

		// -- Create 3 core channels with an upper and lower plenum. -- //
		ArrayList<CoreChannel> coreChannels = new ArrayList<CoreChannel>();

		length = 10;
		coreChannel = new CoreChannel();
		coreChannel.setId(id++);
		plant.addPlantComponent(coreChannel);
		coreChannels.add(coreChannel);
		coreChannel.setNumElements(numElements);
		coreChannel.setName("CoreChannel-L");
		coreChannel.setLength(length);
		coreChannel.setRadius(radius);
		coreChannel.setPosition(new double[] { -3, 0, -0.1 });
		coreChannel.setOrientation(up);

		coreChannel = new CoreChannel();
		coreChannel.setId(id++);
		plant.addPlantComponent(coreChannel);
		coreChannels.add(coreChannel);
		coreChannel.setNumElements(numElements);
		coreChannel.setName("CoreChannel-C");
		coreChannel.setLength(length);
		coreChannel.setRadius(radius);
		coreChannel.setPosition(new double[] { 0, 0, 0.1 });
		coreChannel.setOrientation(up);

		coreChannel = new CoreChannel();
		coreChannel.setId(id++);
		plant.addPlantComponent(coreChannel);
		coreChannels.add(coreChannel);
		coreChannel.setNumElements(numElements);
		coreChannel.setName("CoreChannel-R");
		coreChannel.setLength(length);
		coreChannel.setRadius(radius);
		coreChannel.setPosition(new double[] { 3, 0, -0.1 });
		coreChannel.setOrientation(up);

		Junction lowerPlenum = new Junction();
		lowerPlenum.setName("LowerPlenum");
		lowerPlenum.setId(id++);
		plant.addPlantComponent(lowerPlenum);
		components = new ArrayList<PlantComponent>(coreChannels);
		lowerPlenum.setOutputs(components);

		Junction upperPlenum = new Junction();
		upperPlenum.setName("UpperPlenum");
		upperPlenum.setId(id++);
		plant.addPlantComponent(upperPlenum);
		components = new ArrayList<PlantComponent>(coreChannels);
		upperPlenum.setInputs(components);

		// -- Create the reactor core view. -- //
		reactor = new Reactor();
		reactor.setName("Reactor");
		reactor.setId(id++);
		plant.addPlantComponent(reactor);
		reactor.setCoreChannels(coreChannels);

		// -- Create the upper plenum's pipes and junctions. -- //
		length = 6.75;

		pipe = new Pipe();
		pipe.setName("UpperPlenum-To-HX-L1");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { -1.75, 11, 0 });
		pipe.setOrientation(up);
		// Connect the pipe to the upper plenum.
		upperPlenum.addOutput(pipe);
		Pipe upperPlenumToHXL1 = pipe;

		pipe = new Pipe();
		pipe.setName("UpperPlenum-To-HX-R1");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { 1.75, 11, 0 });
		pipe.setOrientation(up);
		// Connect the pipe to the upper plenum.
		upperPlenum.addOutput(pipe);
		Pipe upperPlenumToHXR1 = pipe;

		length = 8;

		pipe = new Pipe();
		pipe.setName("UpperPlenum-To-HX-L2");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { -2, 18, 0 });
		pipe.setOrientation(left);
		// // Connect the pipe to the upper plenum.
		// upperPlenum.addOutput(pipe);
		Pipe upperPlenumToHXL2 = pipe;

		pipe = new Pipe();
		pipe.setName("UpperPlenum-To-HX-R2");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { 2, 18, 0 });
		pipe.setOrientation(right);
		// // Connect the pipe to the upper plenum.
		// upperPlenum.addOutput(pipe);
		Pipe upperPlenumToHXR2 = pipe;

		junction = new Junction();
		junction.setName("UpperPlenumJunction-L");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(upperPlenumToHXL1);
		junction.addOutput(upperPlenumToHXL2);

		junction = new Junction();
		junction.setName("UpperPlenumJunction-R");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(upperPlenumToHXR1);
		junction.addOutput(upperPlenumToHXR2);

		// -- Add the Heat Exchangers. -- //
		length = 8.5;

		hx = new HeatExchanger();
		hx.setName("HX-L");
		hx.setId(id++);
		plant.addPlantComponent(hx);
		hx.setNumElements(numElements);
		hx.setLength(length);
		hx.setInnerRadius(radius);
		hx.setPosition(new double[] { -10.25, 17.75, 0 });
		hx.setOrientation(down);
		HeatExchanger HXL = hx;

		hx = new HeatExchanger();
		hx.setName("HX-R");
		hx.setId(id++);
		plant.addPlantComponent(hx);
		hx.setNumElements(numElements);
		hx.setLength(length);
		hx.setInnerRadius(radius);
		hx.setPosition(new double[] { 10.25, 17.75, 0 });
		hx.setOrientation(down);
		HeatExchanger HXR = hx;

		junction = new Junction();
		junction.setName("HX-L-In");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(upperPlenumToHXL2);
		junction.addOutput(HXL.getPrimaryPipe());

		junction = new Junction();
		junction.setName("HX-R-In");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(upperPlenumToHXR2);
		junction.addOutput(HXR.getPrimaryPipe());

		// -- Add the lower plenum's pipes and junctions. -- //
		length = 4.75;

		pipe = new Pipe();
		pipe.setName("HX-L-To-LowerPlenum1");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { -10, 9, 0 });
		pipe.setOrientation(right);
		Pipe HXLToLowerPlenum1 = pipe;

		pipe = new Pipe();
		pipe.setName("HX-R-To-LowerPlenum1");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { 10, 9, 0 });
		pipe.setOrientation(left);
		Pipe HXRToLowerPlenum1 = pipe;

		junction = new Junction();
		junction.setName("HX-L-Out");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(HXL.getPrimaryPipe());
		junction.addOutput(HXLToLowerPlenum1);

		junction = new Junction();
		junction.setName("HX-R-Out");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(HXR.getPrimaryPipe());
		junction.addOutput(HXRToLowerPlenum1);

		length = 9.5;

		pipe = new Pipe();
		pipe.setName("HX-L-To-LowerPlenum2");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { -5, 8.75, 0 });
		pipe.setOrientation(down);
		Pipe HXLToLowerPlenum2 = pipe;

		pipe = new Pipe();
		pipe.setName("HX-R-To-LowerPlenum2");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { 5, 8.75, 0 });
		pipe.setOrientation(down);
		Pipe HXRToLowerPlenum2 = pipe;

		junction = new Junction();
		junction.setName("LowerPlenumJunction-L");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(HXLToLowerPlenum1);
		junction.addOutput(HXLToLowerPlenum2);

		junction = new Junction();
		junction.setName("LowerPlenumJunction-R");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(HXRToLowerPlenum1);
		junction.addOutput(HXRToLowerPlenum2);

		// -- Connect the lower plenum pipes to the lower plenum. -- //
		lowerPlenum.addInput(HXLToLowerPlenum2);
		lowerPlenum.addInput(HXRToLowerPlenum2);

		// -- Add water pipes to the left Heat Exchanger. -- //
		length = 5;

		pipe = new Pipe();
		pipe.setName("HX-L-Water-In");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { -17, 18, 0 });
		pipe.setOrientation(new double[] { 1, -0.5, 0 });

		junction = new Junction();
		junction.setName("HX-L-Water-In-Junction");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(pipe);
		junction.addOutput(HXL);

		pipe = new Pipe();
		pipe.setName("HX-L-Water-Out");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { -12.5, 11, 0 });
		pipe.setOrientation(new double[] { -1, -0.5, 0 });

		junction = new Junction();
		junction.setName("HX-L-Water-Out-Junction");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(HXL);
		junction.addOutput(pipe);

		// -- Add water pipes to the right Heat Exchanger. -- //
		length = 5;

		pipe = new Pipe();
		pipe.setName("HX-R-Water-In");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { 17, 18, 0 });
		pipe.setOrientation(new double[] { -1, -0.5, 0 });

		junction = new Junction();
		junction.setName("HX-R-Water-In-Junction");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(pipe);
		junction.addOutput(HXR);

		pipe = new Pipe();
		pipe.setName("HX-R-Water-Out");
		pipe.setId(id++);
		plant.addPlantComponent(pipe);
		pipe.setNumElements(numElements);
		pipe.setLength(length);
		pipe.setRadius(radius);
		pipe.setPosition(new double[] { 12.5, 11, 0 });
		pipe.setOrientation(new double[] { 1, -0.5, 0 });

		junction = new Junction();
		junction.setName("HX-R-Water-Out-Junction");
		junction.setId(id++);
		plant.addPlantComponent(junction);
		junction.addInput(HXR);
		junction.addOutput(pipe);
		// --------------------------- //

		// Write the objects.
		factory.writeObjects(uri, objects);

		// Read the objects.
		List<Object> loadedObjects = factory.readObjects(uri);

		assertNotNull(loadedObjects);
		assertEquals(objects.size(), loadedObjects.size());
		assertEquals(objects.get(0), loadedObjects.get(0));

		return;
	}

	/**
	 * Deletes the ICEIOTestDirectory and all test files that are created in the
	 * above tests.
	 */
	@AfterClass
	public static void deleteTestFiles() {
		String s = System.getProperty("file.separator");
		String directory = System.getProperty("user.dir") + s
				+ "ICEIOTestDirectory" + s;

		// Create a file pointing at the ICEIOTestDirectory.
		File testDirectory = new File(directory);
		// Delete all of the test files in it.
		for (File file : testDirectory.listFiles()) {
			file.delete();
		}
		// Then delete the directory itself.
		if (testDirectory.delete()) {
			System.out.println("PlantIOFactoryTester message: "
					+ "Deleted ICEIOTestDirectory.");
		} else {
			System.out.println("PlantIOFactoryTester message: "
					+ "Cannot delete ICEIOTestDirectory.");
		}

		return;
	}
}
