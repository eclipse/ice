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
		component.setName("comp");
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
		composite.setName("Test Composite");
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
		reactor.setName("Test LW Reactor");
		reactor.setId(idCounter++);

		// Can't do anything else...

		return reactor;
	}

	public BWReactor createBWReactor() {

		int size = 42;

		BWReactor reactor = new BWReactor(size);
		reactor.setName("Test BW Reactor");
		reactor.setId(idCounter++);

		// Can't do anything else...

		return reactor;
	}

	public PressurizedWaterReactor createPressurizedWaterReactor() {

		int size = 7;

		// Create the reactor.
		PressurizedWaterReactor reactor = new PressurizedWaterReactor(size);
		reactor.setName("Test Reactor");
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
		assembly.setName("Test Assembly 1");
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
		controlBank.setName("Test Control Bank");
		controlBank.setId(idCounter++);
		controlBank.setMaxNumberOfSteps(10);
		controlBank.setStepSize(1.3);
		return controlBank;
	}

	public FuelAssembly createFuelAssembly() {

		int size = 5;

		FuelAssembly assembly = new FuelAssembly(size);

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
							data.setUnits("z units");
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
		incoreInstrument.setName("Test Incore Instrument");
		incoreInstrument.setId(idCounter++);
		incoreInstrument.setThimble(createRing());
		return incoreInstrument;
	}

	public RodClusterAssembly createRodClusterAssembly() {

		int size = 5;

		// Create the assembly.
		RodClusterAssembly assembly = new RodClusterAssembly(size);

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
		rod.setName("Test Rod 1");
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
		tube.setName("Test Tube");
		tube.setId(idCounter++);
		tube.setMaterial(createSolidMaterial());
		tube.setHeight(70.0);
		tube.setOuterRadius(40.0);
		tube.setInnerRadius(0.0);
		return tube;
	}

	public MaterialBlock createMaterialBlock() {
		MaterialBlock block = new MaterialBlock();
		block.setName("Test Material Block");
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
		clad.setName("Test Clad");
		clad.setId(idCounter++);
		clad.setMaterial(createSolidMaterial());
		clad.setHeight(50.0);
		clad.setOuterRadius(10.0);
		clad.setInnerRadius(9.0);
		return clad;
	}

	public Material createGasMaterial() {
		Material gasMaterial = new Material();
		gasMaterial.setName("He");
		gasMaterial.setId(idCounter++);
		gasMaterial.setMaterialType(MaterialType.GAS);
		return gasMaterial;
	}

	public Material createLiquidMaterial() {
		Material liquidMaterial = new Material();
		liquidMaterial.setName("Water");
		liquidMaterial.setId(idCounter++);
		liquidMaterial.setMaterialType(MaterialType.LIQUID);
		return liquidMaterial;
	}

	public Material createSolidMaterial() {
		Material solidMaterial = new Material();
		solidMaterial.setName("Steel");
		solidMaterial.setId(idCounter++);
		solidMaterial.setMaterialType(MaterialType.SOLID);
		return solidMaterial;
	}
	// ------------------------------------------ //

	// ---- Other LWRComponent types ---- //
	public LWRGridManager createLWRGridManager(int size) {

		LWRGridManager manager = new LWRGridManager(size);

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
