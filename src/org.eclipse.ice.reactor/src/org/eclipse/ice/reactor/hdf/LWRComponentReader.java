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
package org.eclipse.ice.reactor.hdf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;

import org.eclipse.ice.io.hdf.HdfIOFactory;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.HDF5LWRTagType;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;

public class LWRComponentReader {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(LWRComponentReader.class);

	private HdfIOFactory factory;

	private final LWRComponentFactory componentFactory;

	private interface IComponentReader {
		public void readComponent(LWRComponent component, int groupId)
				throws NullPointerException, HDF5Exception;
	}

	private final Map<HDF5LWRTagType, IComponentReader> readerMap;

	public LWRComponentReader(HdfIOFactory factory) {
		this.factory = factory;

		componentFactory = new LWRComponentFactory();

		// Initialize the map of component readers.
		readerMap = new HashMap<HDF5LWRTagType, IComponentReader>();
		addReaders();

		return;
	}

	int counter = 0;

	public LWRComponent read(int groupId)
			throws NullPointerException, HDF5Exception {

		// Set the default return value.
		LWRComponent component = null;

		if (H5.H5Aexists(groupId, "HDF5LWRTag")) {
			// Read the tag from the object.
			String tag = factory.readStringAttribute(groupId, "HDF5LWRTag");
			HDF5LWRTagType tagType = HDF5LWRTagType.toType(tag);

			// Read the size of the object, if available.
			int size = -1;
			if (H5.H5Aexists(groupId, "size")) {
				size = factory.readIntegerAttribute(groupId, "size");
			}

			// Initialize the component.
			component = componentFactory.createComponent(tagType, size);

			// Read in the component if it could be created.
			if (component != null) {
				IComponentReader reader = readerMap.get(tagType);
				// Try to read the component.
				try {
					reader.readComponent(component, groupId);
				} catch (NullPointerException | HDF5Exception e) {
					logger.error(getClass().getName() + " error: "
							+ "Error reading " + component.getClass().getName()
							+ " with name \"" + component.getName() + "\".", e);
				}
			}
		}

		// Return the read (or not) component.
		return component;
	}

	private void read(int groupId, LWRComponent component)
			throws NullPointerException, HDF5Exception {
		// Read properties inherited from Identifiable...
		factory.readIdentifiableAttributes(component, groupId);

		// Read properties specific to this type...
		// Nothing to do yet.
		// component.setSourceInfo(readStringAttribute(groupId, "sourceInfo"));
		// component.setTime(readDoubleAttribute(groupId, "time"));
		// component.setTimeUnits(readStringAttribute(groupId, "timeUnits"));

		// Open the State Point Data group, read the data stored in it, and
		// close the group.
		readLWRComponentData(component, groupId);

		return;
	}

	private void read(int groupId, LWRComposite composite)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) composite);

		// Read properties specific to this type...
		// Nothing to do.

		// Read the child components...
		for (String childName : getChildGroups(groupId)) {
			// Read the child component.
			int childGroupId = factory.openGroup(groupId, childName);
			LWRComponent childComponent = read(childGroupId);
			factory.closeGroup(childGroupId);

			// Add the child component to the composite if possible.
			if (childComponent != null) {
				composite.addComponent(childComponent);
			}
		}

		return;
	}

	private void read(int groupId, LWReactor reactor)
			throws NullPointerException, HDF5Exception {

		// Read properties specific to its super class (LWRComposite)...
		// Note: Components are not handled using the default LWRComposite
		// functionality, so they must be read in manually. Instead, re-direct
		// to the super-super class (LWRComponent).
		read(groupId, (LWRComponent) reactor);

		// Read properties specific to this type...
		// Note: The size was already read in as it is required at construction.

		return;
	}

	private void read(int groupId, BWReactor reactor)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWReactor)...
		read(groupId, (LWReactor) reactor);

		// Read properties specific to this type...
		// Nothing to do.

		// Read the assemblies...
		// Nothing to do. This is not supported in BWReactor yet.

		return;
	}

	private void read(int groupId, PressurizedWaterReactor reactor)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWReactor)...
		read(groupId, (LWReactor) reactor);

		Stack<Integer> groupIds = new Stack<Integer>();
		groupIds.push(groupId);

		// Read properties specific to this type...
		reactor.setFuelAssemblyPitch(
				factory.readDoubleAttribute(groupId, "fuelAssemblyPitch"));

		// Read the assemblies, grid managers, and labels...

		// Create a map of grid manager / composite names to their proper
		// assembly types.
		Map<String, AssemblyType> assemblyTypeMap = new HashMap<String, AssemblyType>();
		assemblyTypeMap.put(PressurizedWaterReactor.CONTROL_BANK_COMPOSITE_NAME,
				AssemblyType.ControlBank);
		assemblyTypeMap.put(
				PressurizedWaterReactor.CONTROL_BANK_GRID_MANAGER_NAME,
				AssemblyType.ControlBank);
		assemblyTypeMap.put(
				PressurizedWaterReactor.FUEL_ASSEMBLY_COMPOSITE_NAME,
				AssemblyType.Fuel);
		assemblyTypeMap.put(
				PressurizedWaterReactor.FUEL_ASSEMBLY_GRID_MANAGER_NAME,
				AssemblyType.Fuel);
		assemblyTypeMap.put(
				PressurizedWaterReactor.INCORE_INSTRUMENT_COMPOSITE_NAME,
				AssemblyType.IncoreInstrument);
		assemblyTypeMap.put(
				PressurizedWaterReactor.INCORE_INSTRUMENT_GRID_MANAGER_NAME,
				AssemblyType.IncoreInstrument);
		assemblyTypeMap.put(
				PressurizedWaterReactor.ROD_CLUSTER_ASSEMBLY_COMPOSITE_NAME,
				AssemblyType.RodCluster);
		assemblyTypeMap.put(
				PressurizedWaterReactor.ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME,
				AssemblyType.RodCluster);

		List<LWRGridManager> gridManagers = new ArrayList<LWRGridManager>();

		// Read all of the child groups and add them to the reactor.
		for (String childGroupName : getChildGroups(groupId)) {
			// Read the child.
			int childGroupId = factory.openGroup(groupId, childGroupName);
			LWRComponent component = read(childGroupId);
			factory.closeGroup(childGroupId);

			// Get its tag type.
			HDF5LWRTagType type = component.getHDF5LWRTag();

			// If a composite, add all of its assemblies to the reactor based on
			// its assembly type (determined by the composite's name).
			if (type == HDF5LWRTagType.LWRCOMPOSITE) {
				LWRComposite composite = (LWRComposite) component;
				AssemblyType assemblyType = assemblyTypeMap
						.get(composite.getName());
				for (String name : composite.getComponentNames()) {
					component = composite.getComponent(name);
					reactor.addAssembly(assemblyType, component);
				}
			}
			// If a grid manager, add it to the list of grid managers. These
			// will be added to the reactor later.
			else if (type == HDF5LWRTagType.LWRGRIDMANAGER) {
				gridManagers.add((LWRGridManager) component);
			}
			// If a grid label provider, update the grid label provider for the
			// reactor.
			else if (type == HDF5LWRTagType.GRID_LABEL_PROVIDER) {
				reactor.setGridLabelProvider((GridLabelProvider) component);
			}
		}

		// For each grid manager, add all assemblies to their proper location in
		// the reactor.
		for (LWRGridManager gridManager : gridManagers) {
			AssemblyType assemblyType = assemblyTypeMap
					.get(gridManager.getName());
			for (String name : reactor.getAssemblyNames(assemblyType)) {
				for (GridLocation location : gridManager
						.getGridLocationsAtName(name)) {
					reactor.setAssemblyLocation(assemblyType, name,
							location.getRow(), location.getColumn());
				}
			}
		}

		return;
	}

	private void read(int groupId, PWRAssembly assembly)
			throws NullPointerException, HDF5Exception {

		// Read properties specific to its super class (LWRComposite)...
		// Note: Components are not handled using the default LWRComposite
		// functionality, so they must be read in manually. Instead, re-direct
		// to the super-super class (LWRComponent).
		read(groupId, (LWRComponent) assembly);

		// Read properties specific to this type...
		// Note: The size was already read in as it is required at construction.
		assembly.setRodPitch(factory.readDoubleAttribute(groupId, "rodPitch"));

		String childGroupName;
		int childGroupId;

		// Read the rods...
		childGroupName = PWRAssembly.LWRROD_COMPOSITE_NAME;
		LWRComposite rods = new LWRComposite();
		childGroupId = factory.openGroup(groupId, childGroupName);
		read(childGroupId, rods);
		factory.closeGroup(childGroupId);

		// Read the rod grid locations...
		childGroupName = PWRAssembly.LWRROD_GRID_MANAGER_NAME;
		LWRGridManager gridManager = new LWRGridManager(assembly.getSize());
		childGroupId = factory.openGroup(groupId, childGroupName);
		read(childGroupId, gridManager);
		factory.closeGroup(childGroupId);

		// Add all rods to the assembly. Also set their locations.
		for (String rodName : rods.getComponentNames()) {
			LWRRod rod = (LWRRod) rods.getComponent(rodName);
			// Add it to the assembly.
			assembly.addLWRRod(rod);
			// Add every location for the tube to the assembly.
			for (GridLocation location : gridManager
					.getGridLocationsAtName(rodName)) {
				int row = location.getRow();
				int column = location.getColumn();

				// Mark the rod's location in the assembly.
				assembly.setLWRRodLocation(rodName, row, column);

				// Copy over the data at the location.
				LWRDataProvider sourceData = location.getLWRDataProvider();
				assembly.getLWRRodDataProviderAtLocation(row, column)
						.copy(sourceData);
			}
		}

		return;
	}

	private void read(int groupId, ControlBank controlBank)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) controlBank);

		// Read properties specific to this type...
		controlBank.setMaxNumberOfSteps(
				factory.readIntegerAttribute(groupId, "maxNumberOfSteps"));
		controlBank
				.setStepSize(factory.readDoubleAttribute(groupId, "stepSize"));

		// Read in the sub-assembly components.
		// Nothing to do.

		return;
	}

	private void read(int groupId, FuelAssembly assembly)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (PWRAssembly)...
		read(groupId, (PWRAssembly) assembly);

		// Read properties specific to this type...
		// Nothing to do.

		String childGroupName;
		int childGroupId;

		// Read the assembly's grid label provider...
		childGroupName = FuelAssembly.GRID_LABEL_PROVIDER_NAME;
		GridLabelProvider gridLabelProvider = new GridLabelProvider();
		childGroupId = factory.openGroup(groupId, childGroupName);
		read(childGroupId, gridLabelProvider);
		factory.closeGroup(childGroupId);
		assembly.setGridLabelProvider(gridLabelProvider);

		// Read the tubes...
		childGroupName = FuelAssembly.TUBE_COMPOSITE_NAME;
		LWRComposite tubes = new LWRComposite();
		childGroupId = factory.openGroup(groupId, childGroupName);
		read(childGroupId, tubes);
		factory.closeGroup(childGroupId);

		// Read the tube grid locations...
		childGroupName = FuelAssembly.TUBE_GRID_MANAGER_NAME;
		LWRGridManager tubeLocations = new LWRGridManager(assembly.getSize());
		childGroupId = factory.openGroup(groupId, childGroupName);
		read(childGroupId, tubeLocations);
		factory.closeGroup(childGroupId);

		// Add all tubes to the assembly. Also set their locations.
		for (String tubeName : tubes.getComponentNames()) {
			Tube tube = (Tube) tubes.getComponent(tubeName);
			// Add it to the assembly.
			assembly.addTube(tube);
			// Add every location for the tube to the assembly.
			for (GridLocation location : tubeLocations
					.getGridLocationsAtName(tubeName)) {
				int row = location.getRow();
				int column = location.getColumn();

				// Mark the tube's location in the assembly.
				assembly.setTubeLocation(tubeName, row, column);

				// Copy over the data at the location.
				LWRDataProvider sourceData = location.getLWRDataProvider();
				assembly.getTubeDataProviderAtLocation(row, column)
						.copy(sourceData);
			}
		}

		return;
	}

	private void read(int groupId, IncoreInstrument incoreInstrument)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) incoreInstrument);

		// Read properties specific to this type...

		// Read the thimble (a Ring). This object should always exist.
		// Read it from HDF.
		String ringGroupName = getChildGroups(groupId).get(0);
		int ringGroupId = factory.openGroup(groupId, ringGroupName);
		Ring ring = (Ring) read(ringGroupId);
		factory.closeGroup(ringGroupId);
		// Set it as the incore instrument's thimble.
		incoreInstrument.setThimble(ring);

		// Read in the sub-assembly components.
		// Nothing to do.

		return;
	}

	private void read(int groupId, RodClusterAssembly assembly)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (PWRAssembly)...
		read(groupId, (PWRAssembly) assembly);

		// Read properties specific to this type...
		// Nothing to do.

		// Read in the sub-assembly components.
		// Nothing to do.

		return;
	}

	private void read(int groupId, LWRRod rod)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) rod);

		// Read properties specific to this type...
		rod.setPressure(factory.readDoubleAttribute(groupId, "pressure"));

		TreeSet<MaterialBlock> blocks = new TreeSet<MaterialBlock>();

		for (String child : getChildGroups(groupId)) {
			// Open, read, and close the group for the child LWRComponent.
			int childGroupId = factory.openGroup(groupId, child);
			LWRComponent component = read(childGroupId);
			factory.closeGroup(childGroupId);

			// Add the component to the rod depending on its type.
			HDF5LWRTagType tag = component.getHDF5LWRTag();
			if (tag == HDF5LWRTagType.RING) {
				rod.setClad((Ring) component);
			} else if (tag == HDF5LWRTagType.MATERIAL) {
				rod.setFillGas((Material) component);
			} else if (tag == HDF5LWRTagType.MATERIALBLOCK) {
				blocks.add((MaterialBlock) component);
			}
		}

		// Set the rod's material blocks based on those loaded into the set.
		rod.setMaterialBlocks(blocks);

		return;
	}

	private void read(int groupId, Ring ring)
			throws NullPointerException, HDF5Exception {

		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) ring);

		// Read properties specific to this type...
		ring.setHeight(factory.readDoubleAttribute(groupId, "height"));
		ring.setOuterRadius(
				factory.readDoubleAttribute(groupId, "outerRadius"));
		ring.setInnerRadius(
				factory.readDoubleAttribute(groupId, "innerRadius"));

		// Read the material.
		// Open its group.
		String materialName = getChildGroups(groupId).get(0);
		int materialGroupId = factory.openGroup(groupId, materialName);
		// Create the component and read its information. Note that Material
		// is not part of the visitor pattern, so it must be read directly.
		Material material = (Material) read(materialGroupId);
		// Close its group.
		factory.closeGroup(materialGroupId);

		// Add it to the tube.
		ring.setMaterial(material);

		return;
	}

	private void read(int groupId, Tube tube)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (Ring)...
		read(groupId, (Ring) tube);

		// Read properties specific to this type...
		String tubeType = factory.readStringAttribute(groupId, "tubeType");
		tube.setTubeType(TubeType.toType(tubeType));

		return;
	}

	private void read(int groupId, Material material)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) material);

		// Read properties specific to this type...
		String materialType = factory.readStringAttribute(groupId,
				"materialType");
		material.setMaterialType(MaterialType.toType(materialType));

		return;
	}

	private void read(int groupId, MaterialBlock block)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) block);

		// Read properties specific to this type...
		block.setPosition(factory.readDoubleAttribute(groupId, "position"));

		// Read in all of the rings in the block.
		for (String child : getChildGroups(groupId)) {
			// Since we know the type, we can directly read it in as a ring.
			Ring ring = new Ring();
			// Open, read, and close the ring's group.
			int childGroupId = factory.openGroup(groupId, child);
			read(childGroupId, ring);
			factory.closeGroup(childGroupId);
			// Add the ring to the block.
			block.addRing(ring);
		}

		return;
	}

	private void read(int groupId, GridLabelProvider provider)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) provider);

		// Read properties specific to this type...
		// Note: The size was already read in as it is required at construction.

		int datasetType = HDF5Constants.H5O_TYPE_DATASET;
		String datasetName;
		String[] labels;
		ArrayList<String> labelList;

		// Read in the row and column labels.
		int labelsGroupId = factory.openGroup(groupId,
				GridLabelProvider.LABELS_GROUP_NAME);

		// Read in the column labels if they exist, and set them.
		datasetName = GridLabelProvider.COLUMN_LABELS_NAME;
		if (factory.hasChild(labelsGroupId, datasetName, datasetType)) {
			labels = factory.readStringArrayDataset(labelsGroupId, datasetName);
			labelList = new ArrayList<String>(labels.length);
			for (String label : labels) {
				labelList.add(label);
			}
			provider.setColumnLabels(labelList);
		}

		// Read in the column labels if they exist, and set them.
		datasetName = GridLabelProvider.ROW_LABELS_NAME;
		if (factory.hasChild(labelsGroupId, datasetName, datasetType)) {
			labels = factory.readStringArrayDataset(labelsGroupId, datasetName);
			labelList = new ArrayList<String>(labels.length);
			for (String label : labels) {
				labelList.add(label);
			}
			provider.setRowLabels(labelList);
		}

		// Close the labels group.
		factory.closeGroup(labelsGroupId);

		return;
	}

	private void read(int groupId, LWRGridManager gridManager)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) gridManager);

		// Read properties specific to this type.
		// Note: The size was already read in as it is required at construction.

		String groupName = "Positions";
		if (factory.hasChildGroup(groupId, groupName)) {
			int positionsGroupId = factory.openGroup(groupId, groupName);

			// Read in the set of assemblies with set locations in the
			// reactor.
			String[] table = factory.readStringArrayDataset(positionsGroupId,
					"Simple Position Names Table");
			List<String> assemblyNames = new ArrayList<String>(table.length);
			for (int j = 0; j < table.length; j++) {
				assemblyNames.add(table[j]);
			}

			// Read in the table of units used in the location data.
			List<String> unitsNames = null;
			if (factory.hasChild(positionsGroupId, "Units Table",
					HDF5Constants.H5O_TYPE_DATASET)) {
				table = factory.readStringArrayDataset(positionsGroupId,
						"Units Table");
				unitsNames = new ArrayList<String>(table.length);
				for (int j = 0; j < table.length; j++) {
					unitsNames.add(table[j]);
				}
			}

			// Add all positions to the list.
			for (String name : factory.getChildNames(positionsGroupId,
					HDF5Constants.H5O_TYPE_GROUP)) {
				// Open the position group.
				int positionGroupId = factory.openGroup(positionsGroupId, name);

				// Read in the position.
				int[] position = (int[]) factory.readDataset(positionGroupId,
						"Position Dataset", HDF5Constants.H5T_NATIVE_INT);

				// Create a new GridLocation.
				GridLocation location = new GridLocation(position[0],
						position[1]);

				// Add a new location to the grid manager.
				String assemblyName = assemblyNames.get((Integer) position[2]);

				// Read in the remainder of the grid location data.
				if (unitsNames != null) {
					readGridLocation(location, positionGroupId, unitsNames);
				}

				// Add the location to the grid manager.
				gridManager.addComponent(new LWRComponent(assemblyName),
						location);

				// Close the position group.
				factory.closeGroup(positionGroupId);
			}

			// Close the "Positions" group.
			factory.closeGroup(positionsGroupId);
		}

		return;
	}

	private void readGridLocation(GridLocation location, int groupId,
			List<String> unitsNames)
					throws NullPointerException, HDF5Exception {

		LWRDataProvider dataProvider = location.getLWRDataProvider();

		// Loop over the timesteps at this position.
		for (String timestepGroup : factory.getChildNames(groupId,
				HDF5Constants.H5O_TYPE_GROUP)) {
			// Open the timestep group.
			int timestepGroupId = factory.openGroup(groupId, timestepGroup);

			// Read the time for the group.
			double time = factory.readDoubleAttribute(timestepGroupId, "time");

			List<LWRData> dataList = null;

			for (String dataset : factory.getChildNames(timestepGroupId,
					HDF5Constants.H5O_TYPE_DATASET)) {

				// Get the feature from the dataset name. Note: Each dataset
				// ends with the string " headTable" or " dataTable".
				String feature = dataset.substring(0, dataset.length() - 10);

				// Each feature's "dataTable" comes first.
				if (dataset.endsWith("dataTable")) {

					// Read the dataset.
					double[] table = (double[]) factory.readDataset(
							timestepGroupId, dataset,
							HDF5Constants.H5T_NATIVE_DOUBLE);

					// Allocate an array for new LWRData instances.
					dataList = new ArrayList<LWRData>(table.length / 5);

					// Add data for each row in the table.
					for (int i = 0; i < table.length;) {
						LWRData data = new LWRData(feature);
						// Get the data's value, uncertainty, and position from
						// the table. Note that the index is incremented below.
						data.setValue(table[i++]);
						data.setUncertainty(table[i++]);
						ArrayList<Double> position = new ArrayList<Double>(3);
						position.add(table[i++]);
						position.add(table[i++]);
						position.add(table[i++]);
						data.setPosition(position);
						// Units must be determined from the head table.
						dataList.add(data);
					}
				}
				// Each feature's "headTable" comes after the data table.
				else if (dataset.endsWith("headTable")) {

					// Read the dataset.
					int[] table = (int[]) factory.readDataset(timestepGroupId,
							dataset, HDF5Constants.H5T_NATIVE_INT);

					// Update the units for each of the LWRData instances.
					for (int i = 0; i < table.length;) {
						// Get the corresponding data and the units name.
						LWRData data = dataList.get(table[i++]);
						String units = unitsNames.get(table[i++]);
						// Set the units.
						data.setUnits(units);
						// We may now add the data to the data provider.
						dataProvider.addData(data, time);
					}
				}
			}

			// Close the timestep group.
			factory.closeGroup(timestepGroupId);
		}

		return;
	}

	/**
	 * Reads all of the data in for an IDataProvider (implemented by
	 * LWRComponent).
	 *
	 * @param provider
	 *            The IDataProvider to read the data into.
	 * @param groupId
	 *            The ID of the parent HDF5 Group, which should be open.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void readLWRComponentData(LWRComponent provider, int groupId)
			throws NullPointerException, HDF5Exception {

		// Open the encapsulating group.
		int dataGroupId = factory.openGroup(groupId, "State Point Data");

		// Loop over the timestep groups.
		for (String groupName : getChildGroups(dataGroupId)) {
			// Open the child group (the timestep group).
			int timestepGroupId = factory.openGroup(dataGroupId, groupName);

			// Get the time and units string from the group.
			double groupTime = factory.readDoubleAttribute(timestepGroupId,
					"time");

			// Read in the time units for the data provider.
			String groupUnits = factory.readStringAttribute(timestepGroupId,
					"units");
			provider.setTimeUnits(groupUnits);

			// Loop over the features at this timestep.
			for (String datasetName : factory.getChildNames(timestepGroupId,
					HDF5Constants.H5O_TYPE_DATASET)) {
				// Read the data for each feature.
				LWRData data = new LWRData();
				readLWRData(data, timestepGroupId, datasetName);
				// Add it to the data provider at the correct timestep.
				provider.addData(data, groupTime);
			}

			// Close the child group (the timestep group).
			factory.closeGroup(timestepGroupId);
		}

		// Close the "State Point Data" group that holds the data.
		factory.closeGroup(dataGroupId);

		return;
	}

	private void readLWRData(LWRData data, int groupId, String datasetName)
			throws NullPointerException, HDF5Exception {
		int status;

		// These values will be derived from the HDF dataset.
		final double uncertainty;
		final String units;
		final double value;
		final ArrayList<Double> position;

		/*-
		 * LWRData are stored as a 1x1 array of a Compound Datatype. The type is
		 * defined as follows:
		 * 
		 *  1 - 1 64-bit floating-point (value)
		 *  2 - 1 64-bit floating-point (uncertainty)
		 *  3 - 1 fixed-length string (units)
		 *  4 - array of 3 64-bit floating-points (position)
		 *  
		 *  Reading this requires construction of the correct compound datatype.
		 *  We also must perform 3 separate read operations:
		 *  
		 *  1 - read the value and uncertainty as a double array
		 *  2 - read the units string after getting its length
		 *  3 - read the position as a double array
		 *  
		 *  Note: If we have 1 read operation, the data is stored as raw bytes.
		 *  The problem here is that it is unclear how to convert the 8-byte
		 *  portions of the byte array into Java doubles. Attempts to use the
		 *  ByteBuffer produce incorrect doubles. Using 3 read operations lets
		 *  HDF5 handle the native-double-to-java conversion.
		 */

		// Commonly-used constants.
		final int H5P_DEFAULT = HDF5Constants.H5P_DEFAULT;
		final int H5S_ALL = HDF5Constants.H5S_ALL;
		final int H5T_COMPOUND = HDF5Constants.H5T_COMPOUND;
		final int H5T_NATIVE_DOUBLE = HDF5Constants.H5T_NATIVE_DOUBLE;

		// Open the dataset.
		status = H5.H5Dopen(groupId, datasetName, H5P_DEFAULT);
		if (status < 0) {
			factory.throwException(
					"Could not open dataset \"" + datasetName + "\".", status);
		}
		int datasetId = status;

		// Open the dataspace.
		status = H5.H5Dget_space(datasetId);
		if (status < 0) {
			factory.throwException("Could not open dataspace for "
					+ "dataset \"" + datasetName + "\".", status);
		}
		int dataspaceId = status;

		// Allocate memory for the read buffer.
		long[] dims = new long[1];
		status = H5.H5Sget_simple_extent_dims(dataspaceId, dims, null);
		if (status < 0) {
			factory.throwException("Could not allocate memory for reading "
					+ "dataset \"" + datasetName + "\".", status);
		}

		// Used in reading the compound dataset...
		int compoundTypeId;
		int memberTypeId;
		Double[] doubleBuffer;

		// Get the size of the double datatype as stored in HDF5.
		status = H5.H5Tget_size(H5T_NATIVE_DOUBLE);
		if (status < 0) {
			factory.throwException("Could not determine HDF5 storage size of "
					+ "native double.", status);
		}
		final int doubleSize = status;

		// ---- Read the value and uncertainty. ---- //
		// Create the compound datatype for 2 doubles.
		status = H5.H5Tcreate(H5T_COMPOUND, doubleSize + doubleSize);
		if (status < 0) {
			factory.throwException("Could not create compound datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		compoundTypeId = status;

		// Insert 2 double datatypes into the compound datatype.
		status = H5.H5Tinsert(compoundTypeId, "value", 0, H5T_NATIVE_DOUBLE);
		if (status < 0) {
			factory.throwException("Could not add value member datatype "
					+ " for dataset \"" + datasetName + "\".", status);
		}
		status = H5.H5Tinsert(compoundTypeId, "uncertainty", doubleSize,
				H5T_NATIVE_DOUBLE);
		if (status < 0) {
			factory.throwException("Could not add uncertainty member datatype "
					+ " for dataset \"" + datasetName + "\".", status);
		}

		// Read the data into a double buffer.
		doubleBuffer = new Double[2];
		status = H5.H5Dread(datasetId, compoundTypeId, dataspaceId, H5S_ALL,
				H5P_DEFAULT, doubleBuffer);
		if (status < 0) {
			factory.throwException("Could not read compound datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}

		// Close the compound datatype for 2 doubles.
		status = H5.H5Tclose(compoundTypeId);
		if (status < 0) {
			factory.throwException("Could not close compound datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}

		// Get the value and uncertainty from the buffer.
		value = doubleBuffer[0];
		uncertainty = doubleBuffer[1];
		// ----------------------------------------- //

		// ---- Read the units string. ---- //
		final int stringTypeId;
		final int stringSize;
		byte[] stringBuffer;

		// -- Get the length of the units string. -- //
		// Get a read-only version of the compound datatype.
		status = H5.H5Dget_type(datasetId);
		if (status < 0) {
			factory.throwException("Could not read compound datatype info "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		compoundTypeId = status;

		// Get a read-only version of the string member datatype.
		status = H5.H5Tget_member_type(compoundTypeId, 2);
		if (status < 0) {
			factory.throwException("Could not read units datatype info "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		memberTypeId = status;

		// Get the size of the string member datatype.
		status = H5.H5Tget_size(memberTypeId);
		if (status < 0) {
			factory.throwException("Could not get size of units string "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		stringSize = status;

		// Close the read-only version of the string member datatype.
		status = H5.H5Tclose(memberTypeId);
		if (status < 0) {
			factory.throwException("Could not close units datatype info "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		// Close the read-only version of the compound datatype.
		// Doesn't need to be closed... otherwise it throws an error.

		// -- Create the member datatype for the string. -- //
		// Copy it from the native string datatype.
		status = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
		if (status < 0) {
			factory.throwException("Could not create units string datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		stringTypeId = status;

		// Set its size.
		status = H5.H5Tset_size(stringTypeId, stringSize);
		if (status < 0) {
			factory.throwException("Could not set units string datatype size "
					+ "for dataset \"" + datasetName + "\".", status);
		}

		// -- Create the compound datatype for the string. -- //
		// Create the compound datatype for the string.
		status = H5.H5Tcreate(H5T_COMPOUND, stringSize);
		if (status < 0) {
			factory.throwException("Could not create compound datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		compoundTypeId = status;
		// Add the member string datatype.
		status = H5.H5Tinsert(compoundTypeId, "units", 0, stringTypeId);
		if (status < 0) {
			factory.throwException("Could not add units member datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}

		// -- Read the string from the compound dataset into a buffer. --/
		stringBuffer = new byte[stringSize];
		status = H5.H5Dread(datasetId, compoundTypeId, dataspaceId, H5S_ALL,
				H5P_DEFAULT, stringBuffer);
		if (status < 0) {
			factory.throwException("Could not read compound datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}

		// -- Close the datatypes. -- //
		// Close the member datatype for the string.
		status = H5.H5Tclose(stringTypeId);
		if (status < 0) {
			factory.throwException("Could not close units string datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		// Close the compound datatype for the string.
		status = H5.H5Tclose(compoundTypeId);
		if (status < 0) {
			factory.throwException("Could not close compound datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}

		// -- Extract the useful data from the buffer. -- //
		units = new String(stringBuffer);
		// -------------------------------- //

		// ---- Read the position array. ---- //
		final int positionTypeId;
		final int positionSize;

		// -- Create the datatypes. -- //
		// Create the 3-length floating point array datatype.
		status = H5.H5Tarray_create(H5T_NATIVE_DOUBLE, 1, new long[] { 3 });
		if (status < 0) {
			factory.throwException("Could not create position array datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		positionTypeId = status;
		status = H5.H5Tget_size(positionTypeId);
		if (status < 0) {
			factory.throwException("Could not set position array datatype size "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		positionSize = status;

		// Create the compound datatype.
		status = H5.H5Tcreate(H5T_COMPOUND, positionSize);
		if (status < 0) {
			factory.throwException("Could not create compound datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}
		compoundTypeId = status;
		status = H5.H5Tinsert(compoundTypeId, "position", 0, positionTypeId);
		if (status < 0) {
			factory.throwException("Could not add position member datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}

		// -- Read the array from the compound dataset into a buffer. -- //
		doubleBuffer = new Double[3];
		status = H5.H5Dread(datasetId, compoundTypeId, dataspaceId, H5S_ALL,
				H5P_DEFAULT, doubleBuffer);
		if (status < 0) {
			factory.throwException("Could not read position array "
					+ "for dataset \"" + datasetName + "\".", status);
		}

		// -- Close the datatypes. -- //
		// Close the position array datatype.
		status = H5.H5Tclose(positionTypeId);
		if (status < 0) {
			factory.throwException("Could not close position array datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}

		// Close the compound datatype.
		status = H5.H5Tclose(compoundTypeId);
		if (status < 0) {
			factory.throwException("Could not close compound datatype "
					+ "for dataset \"" + datasetName + "\".", status);
		}

		// Get the position data from the double buffer.
		position = new ArrayList<Double>(3);
		position.add(doubleBuffer[0]);
		position.add(doubleBuffer[1]);
		position.add(doubleBuffer[2]);
		// ---------------------------------- //

		// Close the dataspace.
		status = H5.H5Sclose(dataspaceId);
		if (status < 0) {
			factory.throwException("Could not close dataspace for "
					+ "dataset \"" + datasetName + "\".", status);
		}

		// Close the dataset.
		status = H5.H5Dclose(datasetId);
		if (status < 0) {
			factory.throwException(
					"Could not close dataset \"" + datasetName + "\".", status);
		}

		// Apply the discovered values to the data.
		data.setFeature(datasetName);
		data.setPosition(position);
		data.setUncertainty(uncertainty);
		data.setUnits(units);
		data.setValue(value);

		return;
	}

	private List<String> getChildGroups(int groupId)
			throws HDF5LibraryException {
		List<String> children = factory.getChildNames(groupId,
				HDF5Constants.H5O_TYPE_GROUP);
		children.remove("State Point Data");
		return children;
	}

	/**
	 * Populates the {@link #readerMap} with {@link IComponentReader}s that
	 * simply re-direct to the appropriate read operation based on the tag.
	 */
	private void addReaders() {
		// Add readers for base types.
		readerMap.put(HDF5LWRTagType.LWRCOMPONENT, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (LWRComponent) component);
			}
		});
		readerMap.put(HDF5LWRTagType.LWRCOMPOSITE, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (LWRComposite) component);
			}
		});

		// Add readers for LWR types.
		readerMap.put(HDF5LWRTagType.LWREACTOR, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (LWReactor) component);
			}
		});
		readerMap.put(HDF5LWRTagType.BWREACTOR, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (BWReactor) component);
			}
		});
		readerMap.put(HDF5LWRTagType.PWREACTOR, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (PressurizedWaterReactor) component);
			}
		});

		// Add readers for the assembly types.
		readerMap.put(HDF5LWRTagType.PWRASSEMBLY, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (PWRAssembly) component);
			}
		});
		readerMap.put(HDF5LWRTagType.CONTROL_BANK, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (ControlBank) component);
			}
		});
		readerMap.put(HDF5LWRTagType.FUEL_ASSEMBLY, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (FuelAssembly) component);
			}
		});
		readerMap.put(HDF5LWRTagType.INCORE_INSTRUMENT, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (IncoreInstrument) component);
			}
		});
		readerMap.put(HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY,
				new IComponentReader() {
					@Override
					public void readComponent(LWRComponent component,
							int groupId)
									throws NullPointerException, HDF5Exception {
						read(groupId, (RodClusterAssembly) component);
					}
				});

		// Add readers for the rod/pin types.
		readerMap.put(HDF5LWRTagType.LWRROD, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (LWRRod) component);
			}
		});

		// Add readers for the ring types.
		readerMap.put(HDF5LWRTagType.RING, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (Ring) component);
			}
		});
		readerMap.put(HDF5LWRTagType.TUBE, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (Tube) component);
			}
		});

		// Add readers for Materials.
		readerMap.put(HDF5LWRTagType.MATERIAL, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (Material) component);
			}
		});
		readerMap.put(HDF5LWRTagType.MATERIALBLOCK, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (MaterialBlock) component);
			}
		});

		// Add readers for other LWRComponent types.
		readerMap.put(HDF5LWRTagType.GRID_LABEL_PROVIDER,
				new IComponentReader() {
					@Override
					public void readComponent(LWRComponent component,
							int groupId)
									throws NullPointerException, HDF5Exception {
						read(groupId, (GridLabelProvider) component);
					}
				});
		readerMap.put(HDF5LWRTagType.LWRGRIDMANAGER, new IComponentReader() {
			@Override
			public void readComponent(LWRComponent component, int groupId)
					throws NullPointerException, HDF5Exception {
				read(groupId, (LWRGridManager) component);
			}
		});

		return;
	}
}
