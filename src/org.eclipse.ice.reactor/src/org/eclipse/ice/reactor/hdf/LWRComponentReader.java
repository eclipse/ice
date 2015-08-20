package org.eclipse.ice.reactor.hdf;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.eclipse.ice.io.hdf.HdfIOFactory;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.GridLocation;
import org.eclipse.ice.reactor.HDF5LWRTagType;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRComposite;
import org.eclipse.ice.reactor.LWRData;
import org.eclipse.ice.reactor.LWRGridManager;
import org.eclipse.ice.reactor.LWRRod;
import org.eclipse.ice.reactor.LWReactor;
import org.eclipse.ice.reactor.Material;
import org.eclipse.ice.reactor.MaterialBlock;
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
		LWRGridManager rodLocations = new LWRGridManager(assembly.getSize());
		childGroupId = factory.openGroup(groupId, childGroupName);
		read(childGroupId, rodLocations);
		factory.closeGroup(childGroupId);

		// Add all rods to the assembly. Also set their locations.
		for (String rodName : rods.getComponentNames()) {
			LWRRod rod = (LWRRod) rods.getComponent(rodName);
			// Add it to the assembly.
			assembly.addLWRRod(rod);
			// Add every location for the tube to the assembly.
			for (GridLocation location : rodLocations
					.getGridLocationsAtName(rodName)) {
				assembly.setLWRRodLocation(rodName, location.getRow(),
						location.getColumn());
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
				assembly.setTubeLocation(tubeName, location.getRow(),
						location.getColumn());
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
		factory.closeGroup(groupId);
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

		// Read the clad.
		// TODO

		// Read the fill gas.
		// TODO

		// Read the material blocks.
		// TODO

		return;
	}

	private void read(int groupId, Ring ring)
			throws NullPointerException, HDF5Exception {

		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) ring);

		// Read properties specific to this type...
		ring.setHeight(factory.readDoubleAttribute(groupId, "height"));
		ring.setOuterRadius(factory.readDoubleAttribute(groupId, "height"));
		ring.setInnerRadius(factory.readDoubleAttribute(groupId, "height"));

		// Read the material.
		// Open its group.
		String materialName = getChildGroups(groupId).get(0);
		groupId = factory.openGroup(groupId, materialName);
		// Create the component and read its information. Note that Material
		// is not part of the visitor pattern, so it must be read directly.
		Material material = (Material) read(groupId);
		read(groupId, material);
		// Add it to the tube.
		ring.setMaterial(material);
		// Close its group.
		factory.closeGroup(groupId);

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
		// TODO

		return;
	}

	private void read(int groupId, MaterialBlock materialBlock)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) materialBlock);

		// Read properties specific to this type...
		// TODO

		return;
	}

	private void read(int groupId, GridLabelProvider provider)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) provider);

		// Read properties specific to this type...
		// Note: The size was already read in as it is required at construction.

		// Read in the row and column labels.
		int labelsGroupId = factory.openGroup(groupId, "Labels");
		String[] columnLabels = factory.readStringArrayDataset(labelsGroupId,
				"Column Labels");
		String[] rowLabels = factory.readStringArrayDataset(labelsGroupId,
				"Row Labels");
		factory.closeGroup(labelsGroupId);

		// Set them for the grid label provider.
		ArrayList<String> labels;

		// Set the column labels.
		labels = new ArrayList<String>(columnLabels.length);
		for (String label : columnLabels) {
			labels.add(label);
		}
		provider.setColumnLabels(labels);

		// Set the row labels.
		labels = new ArrayList<String>(rowLabels.length);
		for (String label : rowLabels) {
			labels.add(label);
		}
		provider.setColumnLabels(labels);

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
			Map<Integer, String> assemblyNameMap = new HashMap<Integer, String>();
			String[] table = factory.readStringArrayDataset(positionsGroupId,
					"Simple Position Names Table");
			for (int j = 0; j < table.length; j++) {
				assemblyNameMap.put(j, table[j]);
			}

			// Add all positions to the list.
			for (String name : factory.getChildNames(positionsGroupId,
					HDF5Constants.H5O_TYPE_GROUP)) {
				// Open the position group.
				int positionGroupId = factory.openGroup(positionsGroupId, name);

				// Read in the position.
				int[] position = (int[]) factory.readDataset(positionGroupId,
						"Position Dataset", HDF5Constants.H5T_NATIVE_INT);

				// Add a new location to the grid manager.
				String assemblyName = assemblyNameMap
						.get((Integer) position[2]);
				gridManager.addComponent(new LWRComponent(assemblyName),
						new GridLocation(position[0], position[1]));

				// Close the position group.
				factory.closeGroup(positionGroupId);
			}

			// Close the "Positions" group.
			factory.closeGroup(positionsGroupId);
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
		int status;

		// Commonly-used constants.
		int H5P_DEFAULT = HDF5Constants.H5P_DEFAULT;
		int H5S_ALL = HDF5Constants.H5S_ALL;
		int H5O_TYPE_DATASET = HDF5Constants.H5O_TYPE_DATASET;

		// Open the encapsulating group.
		int dataGroupId = factory.openGroup(groupId, "State Point Data");

		// Loop over the timestep groups.
		for (String groupName : getChildGroups(dataGroupId)) {
			// Open the child group (the timestep group).
			int timestepGroupId = factory.openGroup(dataGroupId, groupName);

			// Get the time and units string from the group.
			double groupTime = factory.readDoubleAttribute(timestepGroupId,
					"time");

			// FIXME Time units are listed for each timestep's group and in each
			// data's compound dataset.
			String groupUnits = factory.readStringAttribute(timestepGroupId,
					"units");

			// Loop over the features at this timestep.
			for (String datasetName : factory.getChildNames(timestepGroupId,
					H5O_TYPE_DATASET)) {
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
			throws HDF5LibraryException {
		int status;

		// These values will be derived from the HDF dataset.
		double uncertainty = 0.0;
		String units = "";
		double value = 0.0;
		double x = 0.0;
		double y = 0.0;
		double z = 0.0;

		// Commonly-used constants.
		int H5P_DEFAULT = HDF5Constants.H5P_DEFAULT;
		int H5S_ALL = HDF5Constants.H5S_ALL;

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

//		// Open the datatype for the dataset.
//		status = H5.H5Dget_type(datasetId);
//		if (status < 0) {
//			factory.throwException("Could not open datatype of dataset \""
//					+ datasetName + "\".", status);
//		}
//		int datatypeId = status;
//
//		// There should be 4 components:
//		// value: double
//		// uncertainty: double
//		// units: string
//		// position: array of doubles of size 3
//
//		status = H5.H5Tget_size(datatypeId);
//		int size = status;
//		
//		byte[] buffer = new byte[size];
//		status = H5.H5Dread(datasetId, datatypeId, H5S_ALL, H5S_ALL, H5P_DEFAULT, buffer);
//		
//		status = H5.H5Tget_member_type(datatypeId, 0);
//		int memberDatatypeId = status;
//		status = H5.H5Tget_size(memberDatatypeId);
//		value = ByteBuffer.wrap(buffer, 0, status).getDouble();
//		status = H5.H5Tclose(memberDatatypeId);
//		
////		// Open the datatype for the first member (value, a double).
////		status = H5.H5Tget_member_type(datatypeId, 0);
////		if (status < 0) {
////			factory.throwException("Could not open \"value\" datatype of "
////					+ "dataset \"" + datasetName + "\".", status);
////		}
////		int memberDatatypeId = status;		
////		// Close the datatype for the first member.
////		status = H5.H5Tclose(memberDatatypeId);
////		if (status < 0) {
////			factory.throwException("Could not close \"value\" datatype of "
////					+ "dataset \"" + datasetName + "\".", status);
////		}
//
//		// Close the datatype.
//		status = H5.H5Tclose(datatypeId);
//		if (status < 0) {
//			factory.throwException("Could not close datatype of dataset \""
//					+ datasetName + "\".", status);
//		}

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

		// Convert the position into an ArrayList as required by LWRData.
		ArrayList<Double> position = new ArrayList<Double>();
		position.add(x);
		position.add(y);
		position.add(z);

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
