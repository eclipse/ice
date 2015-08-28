package org.eclipse.ice.reactor.hdf.test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.LWRGridManager;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.LWReactor;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
import org.eclipse.ice.reactor.MaterialType;
import org.eclipse.ice.reactor.Ring;
import org.eclipse.ice.reactor.Tube;
import org.eclipse.ice.reactor.TubeType;
import org.eclipse.ice.reactor.bwr.BWReactor;
import org.eclipse.ice.reactor.pwr.ControlBank;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.ice.reactor.pwr.IncoreInstrument;
import org.eclipse.ice.reactor.pwr.PWRAssembly;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.pwr.RodClusterAssembly;

public class LWRComponentFactory {

	private int idCounter = 0;

	// ---- Base LWRComponent types ---- //
	public LWRComponent createLWRComponent() {
		LWRComponent component = new LWRComponent();
		component.setName("Test " + component.getClass().getName());
		component.setId(idCounter++);

		LWRData data;

		// Add data to the rod.
		data = new LWRData();
		data.setFeature("Test Feature Data");
		data.setUncertainty(0.05);
		ArrayList<Double> position = new ArrayList<Double>(3);
		position.add(0.0);
		position.add(1.0);
		position.add(2.0);
		data.setPosition(position);
		data.setValue(42.0);
		component.addData(data, 0.1);

		// Add a modified copy of the data to the rod. Same feature.
		data = (LWRData) data.clone();
		data.setValue(1337.0);
		data.setUnits(data.getUnits() + " 2");
		component.addData(data, 0.1);

		// Add a copy of the data to the rod. Different feature.
		data = (LWRData) data.clone();
		data.setFeature(data.getFeature() + " 2");
		component.addData(data, 0.1);

		return component;
	}

	public LWRComposite createLWRComposite() {
		LWRComposite composite = new LWRComposite();
		composite.setName("Test " + composite.getClass().getName());
		composite.setId(idCounter++);

		// Add some components to it.
		for (int i = 0; i < 3; i++) {
			composite.addComponent(createLWRComponent());
		}

		return composite;
	}
	// --------------------------------- //

	// ---- LWR types ---- //
	public LWReactor createLWReactor() {

		int size = 42;

		LWReactor reactor = new LWReactor(size);
		reactor.setName("Test " + reactor.getClass().getName());
		reactor.setId(idCounter++);

		// Can't do anything else...

		return reactor;
	}

	public BWReactor createBWReactor() {

		int size = 42;

		BWReactor reactor = new BWReactor(size);
		reactor.setName("Test " + reactor.getClass().getName());
		reactor.setId(idCounter++);

		// Can't do anything else...

		return reactor;
	}

	public PressurizedWaterReactor createPressurizedWaterReactor() {

		int size = 7;

		// Create the reactor.
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(size);
		reactor.setName("Test " + reactor.getClass().getName());
		reactor.setId(idCounter++);
		reactor.setFuelAssemblyPitch(1.0);

		// Add a fuel assembly to the reactor.
		FuelAssembly fuelAssembly = createFuelAssembly();
		reactor.addAssembly(AssemblyType.Fuel, fuelAssembly);
		reactor.setAssemblyLocation(AssemblyType.Fuel, fuelAssembly.getName(),
				0, 0);

		// Add another FuelAssembly.
		LWRRod rod = createLWRRod();
		Tube tube = createTube();
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
		ControlBank controlBank = createControlBank();
		reactor.addAssembly(AssemblyType.ControlBank, controlBank);
		reactor.setAssemblyLocation(AssemblyType.ControlBank,
				controlBank.getName(), 0, 1);

		// Add an IncoreInstrument.
		IncoreInstrument incoreInstrument = createIncoreInstrument();
		reactor.addAssembly(AssemblyType.IncoreInstrument, incoreInstrument);
		reactor.setAssemblyLocation(AssemblyType.IncoreInstrument,
				incoreInstrument.getName(), 0, 2);

		// Add a RodClusterAssembly.
		RodClusterAssembly rodClusterAssembly = createRodClusterAssembly();
		reactor.addAssembly(AssemblyType.RodCluster, rodClusterAssembly);
		reactor.setAssemblyLocation(AssemblyType.RodCluster,
				rodClusterAssembly.getName(), 0, 3);

		return reactor;
	}
	// ------------------- //

	// ---- Sub-reactor component (assembly) types ---- //

	public PWRAssembly createPWRAssembly() {

		int size = 5;

		PWRAssembly assembly = new PWRAssembly(size);
		assembly.setName("Test " + assembly.getClass().getName());
		assembly.setId(idCounter++);
		assembly.setRodPitch(2.0);

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
		assembly.addData(data, 0.0);

		LWRRod rod;

		List<LWRRod> rods = new ArrayList<LWRRod>(size);
		for (int i = 0; i < size; i++) {
			rods.add(null);
		}

		// Add a new rod to the assembly.
		rod = createLWRRod();
		assembly.addLWRRod(rod);
		for (int column = 0; column < size; column += 3) {
			rods.set(column, rod);
		}

		// Add another rod to the assembly.
		rod = new LWRRod();
		rod.setName("Test Rod 2");
		rod.setId(idCounter++);
		assembly.addLWRRod(rod);
		for (int column = 1; column < size; column += 3) {
			rods.set(column, rod);
		}

		// Add these rods and some data to the assembly.
		addLWRRodsToPWRAssembly(rods, assembly);

		return assembly;
	}

	public ControlBank createControlBank() {
		ControlBank controlBank = new ControlBank();
		controlBank.setName("Test " + controlBank.getClass().getName());
		controlBank.setId(idCounter++);
		controlBank.setMaxNumberOfSteps(10);
		controlBank.setStepSize(1.3);
		return controlBank;
	}

	public FuelAssembly createFuelAssembly() {

		int size = 5;

		FuelAssembly assembly = new FuelAssembly(size);
		assembly.setName("Test " + assembly.getClass().getName());

		// Add grid labels to the assembly.
		assembly.setGridLabelProvider(
				createGridLabelProvider(size, true, true));

		Tube tube;
		LWRData data;
		ArrayList<Double> position;

		LWRRod rod;

		List<LWRRod> rods = new ArrayList<LWRRod>(size);
		for (int i = 0; i < size; i++) {
			rods.add(null);
		}

		// Add a new rod to the assembly.
		rod = createLWRRod();
		assembly.addLWRRod(rod);
		for (int column = 0; column < size; column += 3) {
			rods.set(column, rod);
		}

		// Add another rod to the assembly.
		rod = new LWRRod();
		rod.setName("Test Rod 2");
		rod.setId(idCounter++);
		assembly.addLWRRod(rod);
		for (int column = 1; column < size; column += 3) {
			rods.set(column, rod);
		}

		// Add these rods and some data to the assembly.
		addLWRRodsToPWRAssembly(rods, assembly);

		// Add a tube to the assembly. Also add data for it.
		tube = createTube();
		assembly.addTube(tube);
		for (int y = 0; y < size; y++) {
			for (int x = 2; x < size; x += 3) {
				assembly.setTubeLocation(tube.getName(), y, x);

				LWRDataProvider dataProvider = assembly
						.getTubeDataProviderAtLocation(x, y);
				if (dataProvider != null) {
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
							data.setUnits("f(z) units");
							data.setValue(z * z);
							dataProvider.addData(data, t);
						}
					}
				}
			}
		}

		return assembly;
	}

	public IncoreInstrument createIncoreInstrument() {
		IncoreInstrument incoreInstrument = new IncoreInstrument();
		incoreInstrument
				.setName("Test " + incoreInstrument.getClass().getName());
		incoreInstrument.setId(idCounter++);
		incoreInstrument.setThimble(createRing());
		return incoreInstrument;
	}

	public RodClusterAssembly createRodClusterAssembly() {

		int size = 5;

		// Create the assembly.
		RodClusterAssembly assembly = new RodClusterAssembly(size);
		assembly.setName("Test " + assembly.getClass().getName());

		// Create a rod to add to the assembly.
		LWRRod rod = createLWRRod();

		// Add the rod and data to the assembly.
		List<LWRRod> rods = new ArrayList<LWRRod>(size);
		for (int i = 0; i < size; i++) {
			rods.add(rod);
		}
		addLWRRodsToPWRAssembly(rods, assembly);

		return assembly;
	}
	// ------------------------------------------------ //

	// ---- Rods, Tubes, Rings and Materials ---- //
	public LWRRod createLWRRod() {
		LWRRod rod = new LWRRod();
		rod.setName("Test " + rod.getClass().getName());
		rod.setId(idCounter++);
		rod.setFillGas(createGasMaterial());
		rod.setClad(createRing());
		TreeSet<MaterialBlock> materialBlocks = new TreeSet<MaterialBlock>();
		materialBlocks.add(createMaterialBlock());
		rod.setMaterialBlocks(materialBlocks);
		return rod;
	}

	public Tube createTube() {
		Tube tube = new Tube();
		tube.setName("Test " + tube.getClass().getName());
		tube.setId(idCounter++);
		tube.setMaterial(createSolidMaterial());
		tube.setHeight(70.0);
		tube.setOuterRadius(40.0);
		tube.setInnerRadius(0.0);
		return tube;
	}

	public MaterialBlock createMaterialBlock() {
		MaterialBlock block = new MaterialBlock();
		block.setName("Test " + block.getClass().getName());
		block.setId(idCounter++);
		block.addRing(createRing());
		// Add a ring for the gas material.
		Ring ring = new Ring();
		ring.setName("Gas Ring");
		ring.setMaterial(createGasMaterial());
		ring.setHeight(50.0);
		ring.setOuterRadius(9.0);
		ring.setInnerRadius(8.9);
		block.addRing(ring);
		// Add a ring for the liquid material.
		ring = new Ring();
		ring.setName("Liquid Ring");
		ring.setMaterial(createLiquidMaterial());
		ring.setHeight(50.0);
		ring.setOuterRadius(8.9);
		ring.setInnerRadius(0.0);
		block.addRing(ring);
		return block;
	}

	public Ring createRing() {
		Ring clad = new Ring();
		clad.setName("Test " + clad.getClass().getName());
		clad.setId(idCounter++);
		clad.setMaterial(createSolidMaterial());
		clad.setHeight(50.0);
		clad.setOuterRadius(10.0);
		clad.setInnerRadius(9.0);
		return clad;
	}

	public Material createGasMaterial() {
		Material gasMaterial = new Material();
		gasMaterial.setName("Test " + gasMaterial.getClass().getName());
		gasMaterial.setDescription("He");
		gasMaterial.setId(idCounter++);
		gasMaterial.setMaterialType(MaterialType.GAS);
		return gasMaterial;
	}

	public Material createLiquidMaterial() {
		Material liquidMaterial = new Material();
		liquidMaterial.setName("Test " + liquidMaterial.getClass().getName());
		liquidMaterial.setDescription("Water");
		liquidMaterial.setId(idCounter++);
		liquidMaterial.setMaterialType(MaterialType.LIQUID);
		return liquidMaterial;
	}

	public Material createSolidMaterial() {
		Material solidMaterial = new Material();
		solidMaterial.setName("Test " + solidMaterial.getClass().getName());
		solidMaterial.setDescription("Steel");
		solidMaterial.setId(idCounter++);
		solidMaterial.setMaterialType(MaterialType.SOLID);
		return solidMaterial;
	}
	// ------------------------------------------ //

	// ---- Other LWRComponent types ---- //
	public LWRGridManager createLWRGridManager(int size) {

		LWRGridManager manager = new LWRGridManager(size);
		manager.setName("Test " + manager.getClass().getName());

		LWRData data;
		ArrayList<Double> position;
		LWRComponent component;

		// Add data for the to every location in the assembly. Utilize 10 z
		// positions and 3 timesteps.
		for (int x = 0; x < size; x++) {
			component = new LWRComponent();
			component.setName("comp " + x);
			component.setId(x);
			for (int y = 0; y < size; y += 2) {

				GridLocation location = new GridLocation(y, x);
				LWRDataProvider dataProvider = location.getLWRDataProvider();

				for (int z = 0; z < 10; z++) {
					for (double t = 0; t < 3.0; t++) {
						// Add data for a first feature.
						data = new LWRData("z");
						position = new ArrayList<Double>(3);
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
						position = new ArrayList<Double>(3);
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

				// Add the component/location to the grid manager.
				manager.addComponent(component, location);
			}
		}

		return manager;
	}

	public GridLabelProvider createGridLabelProvider(int size, boolean columns,
			boolean rows) {
		GridLabelProvider provider = new GridLabelProvider(size);
		provider.setName("Test " + provider.getClass().getName());

		ArrayList<String> labels;

		if (columns) {
			labels = new ArrayList<String>(size);
			for (int i = 0; i < size; i++) {
				labels.add(Integer.toString(i * i));
			}
			provider.setColumnLabels(labels);
		}

		if (rows) {
			labels = new ArrayList<String>(size);
			for (int i = 0; i < size; i++) {
				labels.add(Integer.toString(i * i * i));
			}
			provider.setRowLabels(labels);
		}

		return provider;
	}
	// ---------------------------------- //

	public PressurizedWaterReactor createOldReactor() {

		// Note: Most of the changes to the inner radius are being ignored. This
		// is because the inner radius is being set before the outer radius,
		// which usually fails because the new inner radius value is greater
		// than the initial outer radius value of 1.0.

		AssemblyType type;
		int size;
		String labelString;
		ArrayList<String> labels;

		// Initialize the reactor.
		size = 15;
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(size);
		reactor.setName("PWR Reactor");
		reactor.setFuelAssemblyPitch(0.12345678912345);

		// Add row and column labels.
		labels = new ArrayList<String>(size);
		for (int i = 1; i < size + 1; i++) {
			labels.add(Integer.toString(i));
		}
		reactor.getGridLabelProvider().setRowLabels(labels);
		labels = new ArrayList<String>(size);
		labelString = "RPNMLKJHGFEDCBA";
		for (int i = 0; i < labelString.length();) {
			labels.add(labelString.substring(i, ++i));
		}
		reactor.getGridLabelProvider().setColumnLabels(labels);

		// ---- Add control banks to the reactor. ---- //
		type = AssemblyType.ControlBank;

		// Create and add them to the reactor.
		reactor.addAssembly(type, new ControlBank("A", 0.625, 230));
		reactor.addAssembly(type, new ControlBank("B", 0.625, 215));
		reactor.addAssembly(type, new ControlBank("C", 0.625, 200));
		reactor.addAssembly(type, new ControlBank("D", 0.625, 185));

		// Assign a position for each control bank.
		reactor.setAssemblyLocation(type, "A", 4, 4);
		reactor.setAssemblyLocation(type, "A", 4, 10);
		reactor.setAssemblyLocation(type, "A", 5, 7);
		reactor.setAssemblyLocation(type, "A", 7, 5);
		reactor.setAssemblyLocation(type, "A", 7, 9);
		reactor.setAssemblyLocation(type, "A", 9, 7);
		reactor.setAssemblyLocation(type, "A", 10, 4);
		reactor.setAssemblyLocation(type, "A", 10, 10);
		reactor.setAssemblyLocation(type, "B", 1, 5);
		reactor.setAssemblyLocation(type, "B", 1, 9);
		reactor.setAssemblyLocation(type, "B", 5, 1);
		reactor.setAssemblyLocation(type, "B", 5, 13);
		reactor.setAssemblyLocation(type, "B", 9, 1);
		reactor.setAssemblyLocation(type, "B", 9, 13);
		reactor.setAssemblyLocation(type, "B", 13, 5);
		reactor.setAssemblyLocation(type, "B", 13, 9);
		reactor.setAssemblyLocation(type, "C", 1, 7);
		reactor.setAssemblyLocation(type, "C", 5, 5);
		reactor.setAssemblyLocation(type, "C", 5, 9);
		reactor.setAssemblyLocation(type, "C", 7, 1);
		reactor.setAssemblyLocation(type, "C", 7, 13);
		reactor.setAssemblyLocation(type, "C", 9, 5);
		reactor.setAssemblyLocation(type, "C", 9, 9);
		reactor.setAssemblyLocation(type, "C", 13, 7);
		reactor.setAssemblyLocation(type, "D", 3, 3);
		reactor.setAssemblyLocation(type, "D", 3, 7);
		reactor.setAssemblyLocation(type, "D", 3, 11);
		reactor.setAssemblyLocation(type, "D", 7, 3);
		reactor.setAssemblyLocation(type, "D", 7, 7);
		reactor.setAssemblyLocation(type, "D", 7, 11);
		reactor.setAssemblyLocation(type, "D", 11, 3);
		reactor.setAssemblyLocation(type, "D", 11, 7);
		reactor.setAssemblyLocation(type, "D", 11, 11);
		// ------------------------------------------- //

		// ---- Add incore instruments to the reactor. ---- //
		type = AssemblyType.IncoreInstrument;

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
		incoreInstrument2.setThimble(thimble);
		incoreInstrument3.setThimble(thimble);
		incoreInstrument4.setThimble(thimble);

		// Add the incore instruments
		reactor.addAssembly(type, incoreInstrument1);
		reactor.addAssembly(type, incoreInstrument2);
		reactor.addAssembly(type, incoreInstrument3);
		reactor.addAssembly(type, incoreInstrument4);

		// Assign locations for the incore instruments
		reactor.setAssemblyLocation(type, incoreInstrument1.getName(), 2, 1);
		reactor.setAssemblyLocation(type, incoreInstrument2.getName(), 6, 5);
		reactor.setAssemblyLocation(type, incoreInstrument3.getName(), 11, 2);
		reactor.setAssemblyLocation(type, incoreInstrument4.getName(), 13, 8);
		// ------------------------------------------------ //

		// ---- Create a guide tube. ---- //
		Tube guideTube = new Tube("Guide Tube A", TubeType.GUIDE);
		guideTube.setHeight(1.56);
		guideTube.setInnerRadius(7.89);
		guideTube.setOuterRadius(10.0);

		// Set the material for the guide tube
		guideTube.setMaterial(new Material("Guide Tube Material"));
		// ------------------------------ //

		// ---- Create an instrument tube. ---- //
		Tube instrumentTube = new Tube("Instrument Tube A",
				TubeType.INSTRUMENT);
		instrumentTube.setHeight(1.2);
		instrumentTube.setInnerRadius(0.987);
		instrumentTube.setOuterRadius(34.5);

		// Set the material for the instrument tube
		instrumentTube.setMaterial(new Material("Instrument Tube Material"));
		// ------------------------------------ //

		// ---- Create a material block. ---- //
		MaterialBlock materialBlock = new MaterialBlock();
		materialBlock.setName("Stack of Cards");

		// Create a ring for the material block.
		Ring ring = new Ring("Ring 1");
		ring.setHeight(155);
		ring.setOuterRadius(0.5);
		material = new Material("Ring 1 Material", MaterialType.SOLID);
		ring.setMaterial(material);
		materialBlock.addRing(ring);

		// Create another ring for the material block.
		ring = new Ring("Ring 2");
		ring.setHeight(155);
		ring.setInnerRadius(0.5);
		ring.setOuterRadius(1.0);
		material = new Material("Ring 2 Material", MaterialType.SOLID);
		ring.setMaterial(material);
		materialBlock.addRing(ring);

		TreeSet<MaterialBlock> materialBlockList = new TreeSet<MaterialBlock>();
		materialBlockList.add(materialBlock);
		// ---------------------------------- //

		// ---- Create a clad (ring). ---- //
		// Create a clad
		Ring clad = new Ring("Clad");
		clad.setHeight(155);
		clad.setInnerRadius(0.9);
		clad.setOuterRadius(1.0);

		// Create a material for the clad
		clad.setMaterial(new Material("Clad Material", MaterialType.SOLID));
		// ------------------------------- //

		// ---- Create a rod. ---- //
		// Create an lwrrod for this fuel assembly
		LWRRod rod = new LWRRod("LWRRod A");
		rod.setPressure(23.56);

		// Create a fill gas for the rod
		rod.setFillGas(new Material("He", MaterialType.GAS));

		// Set the MaterialBlock in the rod
		rod.setMaterialBlocks(materialBlockList);
		// Add the clad
		rod.setClad(clad);
		// ----------------------- //

		// ---- Add fuel assemblies to the reactor. ---- //
		type = AssemblyType.Fuel;

		// Create a fuel assembly
		size = 17;
		FuelAssembly fuelAssembly = new FuelAssembly("Fuel Assembly A", size);

		// Create a list of row labels
		labels = new ArrayList<String>(size);
		for (int i = 1; i < size + 1; i++) {
			labels.add(Integer.toString(i));
		}
		fuelAssembly.getGridLabelProvider().setRowLabels(labels);

		// Create list of column labels
		labelString = "ABCDEFGHIJKLMNOPQ";
		labels = new ArrayList<String>(size);
		for (int i = 0; i < labelString.length();) {
			labels.add(labelString.substring(i, ++i));
		}
		fuelAssembly.getGridLabelProvider().setColumnLabels(labels);

		// Add the fuel assembly to the reactor
		reactor.addAssembly(type, fuelAssembly);

		// Assign a position on the grid of the reactor
		reactor.setAssemblyLocation(type, fuelAssembly.getName(), 4, 4);

		// Add tubes and rods to the assembly.
		fuelAssembly.addTube(guideTube);
		fuelAssembly.setTubeLocation(guideTube.getName(), 8, 13);
		fuelAssembly.addTube(instrumentTube);
		fuelAssembly.setTubeLocation(instrumentTube.getName(), 8, 8);
		fuelAssembly.addLWRRod(rod);
		fuelAssembly.setLWRRodLocation(rod.getName(), 15, 4);
		// --------------------------------------------- //

		// ---- Add rod cluster assemblies to the reactor. ---- //
		type = AssemblyType.RodCluster;
		// Create a rca
		RodClusterAssembly rca = new RodClusterAssembly(
				"Rod Cluster Assembly A", 17);

		// Add the rca to the reactor
		reactor.addAssembly(type, rca);

		// Assign the rca location
		reactor.setAssemblyLocation(type, rca.getName(), 5, 2);
		// ---------------------------------------------------- //

		// ---- Add data the the reactor itself. ---- //
		// Add LWRData
		// Setup LWRData
		String feature1 = "Feature 1";
		String feature2 = "Feature 2";
		double time1 = 1.0, time2 = 3.0, time3 = 3.5;

		List<LWRData> dataList = new ArrayList<LWRData>(5);
		List<ArrayList<Double>> positions = new ArrayList<ArrayList<Double>>(5);
		LWRData data;
		ArrayList<Double> position;
		for (int i = 0; i < 5; i++) {
			positions.add(new ArrayList<Double>(3));
		}

		// Setup Positions

		// Setup Position 1
		position = positions.get(0);
		position.add(0.0);
		position.add(1.0);
		position.add(0.0);

		// Setup Position 2
		position = positions.get(1);
		position.add(0.0);
		position.add(1.0);
		position.add(4.0);

		// Setup Position 3
		position = positions.get(2);
		position.add(1.0);
		position.add(1.0);
		position.add(0.0);

		// Setup Position 4
		position = positions.get(3);
		position.add(0.0);
		position.add(1.0);
		position.add(1.0);
		position.add(0.0); // Yes, this is a bug in the old factory.
		position.add(1.0);
		position.add(3.0);

		// Set up the middle data points.
		for (int i = 1; i < 6; i++) {
			data = new LWRData(feature1);
			data.setPosition(positions.get(i - 1));
			data.setValue((double) i);
			data.setUncertainty(i + 0.5);
			data.setUnits("Units " + Integer.toString(i));
			dataList.add(data);
		}

		// Change data1 and data5.
		dataList.get(0).setUnits("Units 123456");
		dataList.get(4).setFeature(feature2);

		// Add them to the reactor.
		reactor.addData(dataList.get(0), time1);
		reactor.addData(dataList.get(1), time1);
		reactor.addData(dataList.get(2), time2);
		reactor.addData(dataList.get(3), time3);
		reactor.addData(dataList.get(4), time3);
		// ------------------------------------------ //

		return reactor;
	}

	private void addLWRRodsToPWRAssembly(List<LWRRod> rods,
			PWRAssembly assembly) {

		int size = assembly.getSize();

		// Add the rods.
		for (LWRRod rod : rods) {
			assembly.addLWRRod(rod);
		}

		// Add the rods to the assembly based on the column index.
		for (int row = 0; row < size; row++) {
			for (int column = 1; column < size; column += 3) {
				LWRRod rod = rods.get(column);
				if (rod != null) {
					assembly.setLWRRodLocation(rod.getName(), row, column);
				}
			}
		}

		LWRData data;
		ArrayList<Double> position;

		// Add data for the to every location in the assembly. Utilize 10 z
		// positions and 3 timesteps.
		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				LWRDataProvider dataProvider = assembly
						.getLWRRodDataProviderAtLocation(x, y);
				if (dataProvider != null) {
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
		}
	}
}
