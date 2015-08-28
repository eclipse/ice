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

import java.nio.ByteBuffer;
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

/**
 * This class handles HDF reading for each type of {@link LWRComponent}
 * available in the reactor model. Note that this class operates directly on
 * <i>open</i> HDF groups and does not accept files or URIs themselves.
 * 
 * @author Jordan Deyton
 *
 */
public class LWRComponentReader {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(LWRComponentReader.class);

	/**
	 * The factory that provides many helpful methods for reading from HDF
	 * files.
	 */
	private final HdfIOFactory factory;

	/**
	 * A factory used to create {@link LWRComponent}s based on their tag read
	 * from the file.
	 */
	private final LWRComponentFactory componentFactory;

	/**
	 * A simple interface for reading. This is used to redirect read operations
	 * to one for the specific type. We use this because the LWR visitor does
	 * not include visit operations for all types with an {@link HDF5LWRTagType}
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private interface IComponentReader {
		/**
		 * Reads the HDF group specified by the ID into the component.
		 * 
		 * @param groupId
		 *            The ID of the HDF group to read.
		 * @param component
		 *            The component into which the group's content will be read.
		 * @throws NullPointerException
		 * @throws HDF5Exception
		 */
		public void readComponent(int groupId, LWRComponent component)
				throws NullPointerException, HDF5Exception;
	}

	/**
	 * A map of the readers keyed on their tag type. For the content of the map,
	 * see {@link #addReaders()}.
	 */
	private final Map<HDF5LWRTagType, IComponentReader> readerMap;

	/**
	 * The default constructor.
	 * 
	 * @param factory
	 *            The parent HDF IO factory used to read from the file.
	 */
	public LWRComponentReader(HdfIOFactory factory) {
		this.factory = factory;

		componentFactory = new LWRComponentFactory();

		// Initialize the map of component readers.
		readerMap = new HashMap<HDF5LWRTagType, IComponentReader>();
		addReaders();

		return;
	}

	/**
	 * Attempts to read the HDF group with the specified ID into a new
	 * {@link LWRComponent}.
	 * 
	 * @param groupId
	 *            The ID of the group that contain the content to read.
	 * @return The read in component, or {@code null} if the group could not be
	 *         read. It may be a sub-class of LWRComponent.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	public LWRComponent readComponent(int groupId)
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
					reader.readComponent(groupId, component);
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

	/**
	 * Reads the content of the group into the specified LWRComponent.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param component
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void read(int groupId, LWRComponent component)
			throws NullPointerException, HDF5Exception {
		// Read properties inherited from Identifiable...
		factory.readIdentifiableAttributes(component, groupId);

		// Open the State Point Data group, read the data stored in it, and
		// close the group.
		readLWRComponentData(groupId, component);

		return;
	}

	/**
	 * Reads the content of the group into the specified LWRComposite.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param composite
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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
			LWRComponent childComponent = readComponent(childGroupId);
			factory.closeGroup(childGroupId);

			// Add the child component to the composite if possible.
			if (childComponent != null) {
				composite.addComponent(childComponent);
			}
		}

		return;
	}

	/**
	 * Reads the content of the group into the specified LWReactor.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param reactor
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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

	/**
	 * Reads the content of the group into the specified BWReactor.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param reactor
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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

	/**
	 * Reads the content of the group into the specified
	 * PressurizedWaterReactor.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param reactor
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void read(int groupId, PressurizedWaterReactor reactor)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWReactor)...
		read(groupId, (LWReactor) reactor);

		Stack<Integer> groupIds = new Stack<Integer>();
		groupIds.push(groupId);

		// Read properties specific to this type...
		reactor.setFuelAssemblyPitch(
				factory.readDoubleAttribute(groupId, "fuelAssemblyPitch"));

		// Read the assemblies and grid managers...

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
		for (String childName : getChildGroups(groupId)) {
			// Read the child.
			int child = factory.openGroup(groupId, childName);
			LWRComponent component = readComponent(child);
			factory.closeGroup(child);

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

	/**
	 * Reads the content of the group into the specified PWRAssembly.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param assembly
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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

		// Read the rod grid locations...
		childGroupName = PWRAssembly.LWRROD_GRID_MANAGER_NAME;
		LWRGridManager gridManager = new LWRGridManager(assembly.getSize());
		childGroupId = factory.openGroup(groupId, childGroupName);
		read(childGroupId, gridManager);
		factory.closeGroup(childGroupId);

		// Read the rods, and add them to the assembly...
		childGroupName = PWRAssembly.LWRROD_COMPOSITE_NAME;
		childGroupId = factory.openGroup(groupId, childGroupName);
		for (String rodName : getChildGroups(childGroupId)) {
			// Open the rod's group, read it, and close it.
			int rodGroupId = factory.openGroup(childGroupId, rodName);
			LWRRod rod = (LWRRod) readComponent(rodGroupId);
			factory.closeGroup(rodGroupId);

			// Add the rod to the assembly.
			assembly.addLWRRod(rod);

			// Mark every location of the rod in the assembly.
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
		factory.closeGroup(childGroupId);

		return;
	}

	/**
	 * Reads the content of the group into the specified ControlBank.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param controlBank
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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

	/**
	 * Reads the content of the group into the specified FuelAssembly.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param assembly
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void read(int groupId, FuelAssembly assembly)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (PWRAssembly)...
		read(groupId, (PWRAssembly) assembly);

		// Read properties specific to this type...
		// Nothing to do.

		String childGroupName;
		int child;

		// Read the assembly's grid label provider...
		// Note that the grid label provider can have a different name from the
		// default name.
		for (String childName : getChildGroups(groupId)) {
			// Find the first object that has the GridLabelProvider tag.
			child = factory.openGroup(groupId, childName);
			if (H5.H5Aexists(child, "HDF5LWRTag")) {
				String tag = factory.readStringAttribute(child, "HDF5LWRTag");
				HDF5LWRTagType tagType = HDF5LWRTagType.toType(tag);
				if (tagType == HDF5LWRTagType.GRID_LABEL_PROVIDER) {
					// Create the grid label provider and read it.
					int size = factory.readIntegerAttribute(child, "size");
					GridLabelProvider labels = new GridLabelProvider(size);
					read(child, labels);
					factory.closeGroup(child);
					// Set the assembly's grid label provider.
					assembly.setGridLabelProvider(labels);
					break;
				}
			}
			factory.closeGroup(child);
		}

		// Read the tubes...
		childGroupName = FuelAssembly.TUBE_COMPOSITE_NAME;
		LWRComposite tubes = new LWRComposite();
		child = factory.openGroup(groupId, childGroupName);
		read(child, tubes);
		factory.closeGroup(child);

		// Read the tube grid locations...
		childGroupName = FuelAssembly.TUBE_GRID_MANAGER_NAME;
		LWRGridManager tubeLocations = new LWRGridManager(assembly.getSize());
		child = factory.openGroup(groupId, childGroupName);
		read(child, tubeLocations);
		factory.closeGroup(child);

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

	/**
	 * Reads the content of the group into the specified IncoreInstrument.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param incoreInstrument
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void read(int groupId, IncoreInstrument incoreInstrument)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (LWRComponent)...
		read(groupId, (LWRComponent) incoreInstrument);

		// Read properties specific to this type...

		// Read the thimble (a Ring). This object should always exist.
		// Read it from HDF.
		String ringGroupName = getChildGroups(groupId).get(0);
		int ringGroupId = factory.openGroup(groupId, ringGroupName);
		Ring ring = (Ring) readComponent(ringGroupId);
		factory.closeGroup(ringGroupId);
		// Set it as the incore instrument's thimble.
		incoreInstrument.setThimble(ring);

		// Read in the sub-assembly components.
		// Nothing to do.

		return;
	}

	/**
	 * Reads the content of the group into the specified RodClusterAssembly.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param assembly
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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

	/**
	 * Reads the content of the group into the specified LWRRod.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param rod
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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
			LWRComponent component = readComponent(childGroupId);
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

	/**
	 * Reads the content of the group into the specified Ring.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param ring
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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
		Material material = (Material) readComponent(materialGroupId);
		// Close its group.
		factory.closeGroup(materialGroupId);

		// Add it to the tube.
		ring.setMaterial(material);

		return;
	}

	/**
	 * Reads the content of the group into the specified Tube.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param tube
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void read(int groupId, Tube tube)
			throws NullPointerException, HDF5Exception {
		// Read properties specific to its super class (Ring)...
		read(groupId, (Ring) tube);

		// Read properties specific to this type...
		String tubeType = factory.readStringAttribute(groupId, "tubeType");
		tube.setTubeType(TubeType.toType(tubeType));

		return;
	}

	/**
	 * Reads the content of the group into the specified Material.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param material
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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

	/**
	 * Reads the content of the group into the specified MaterialBlock.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param block
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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

	/**
	 * Reads the content of the group into the specified GridLabelProvider.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param provider
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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

	/**
	 * Reads the content of the group into the specified LWRGridManager.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param gridManager
	 *            The object to read into.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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

	/**
	 * Reads the content of the group into the specified GridLocation.
	 * 
	 * @param location
	 *            The object to read into.
	 * @param groupId
	 *            The ID of the parent group.
	 * @param unitsNames
	 *            A map of the unit names, keyed on the data table indices.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
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
	 * Reads all of the data for an IDataProvider (implemented by LWRComponent).
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param provider
	 *            The IDataProvider to read the data into.
	 *
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void readLWRComponentData(int groupId, LWRComponent provider)
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
				List<LWRData> dataList;
				dataList = readLWRData(timestepGroupId, datasetName);
				// Add it to the data provider at the correct timestep.
				for (LWRData data : dataList) {
					provider.addData(data, groupTime);
				}
			}

			// Close the child group (the timestep group).
			factory.closeGroup(timestepGroupId);
		}

		// Close the "State Point Data" group that holds the data.
		factory.closeGroup(dataGroupId);

		return;
	}

	/**
	 * Reads the {@link LWRData} from the dataset.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param datasetName
	 *            The name of the dataset to read.
	 * @return A list of all data read from the dataset.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private List<LWRData> readLWRData(int groupId, String datasetName)
			throws NullPointerException, HDF5Exception {
		int status;

		// FIXME There is no error checking on the return values from H5 calls.

		List<LWRData> dataList = null;

		// Constants used to avoid constant use of HDF5Constants namespace.
		final int H5P_DEFAULT = HDF5Constants.H5P_DEFAULT;
		final int H5S_ALL = HDF5Constants.H5S_ALL;
		final int H5T_COMPOUND = HDF5Constants.H5T_COMPOUND;
		final int H5T_NATIVE_DOUBLE = HDF5Constants.H5T_NATIVE_DOUBLE;

		int datatype;

		// The names of the columns in the dataset.
		final String valueName = "value";
		final String uncertaintyName = "uncertainty";
		final String unitsName = "units";
		final String positionName = "position";

		// ---- Open the Dataset and Dataspace ---- //
		final int dataset = H5.H5Dopen(groupId, datasetName, H5P_DEFAULT);
		final int dataspace = H5.H5Dget_space(dataset);
		// ---------------------------------------- //

		// ---- Read metadata from the Dataset ---- //
		// Determine the size of a native double.
		final int doubleSize = H5.H5Tget_size(H5T_NATIVE_DOUBLE);

		// Determine the number of data objects in the dataset.
		long[] dims = new long[2];
		H5.H5Sget_simple_extent_dims(dataspace, dims, null);
		final int size = (int) dims[1];

		// Determine the max length of the units string.
		final int stringSize;
		// Get a read-only version of the compound datatype. Need not close it.
		status = H5.H5Dget_type(dataset);
		// Get a read-only version of the string member datatype.
		datatype = H5.H5Tget_member_type(status, 2);
		// Get the size of the string member datatype.
		stringSize = H5.H5Tget_size(datatype);
		H5.H5Tclose(datatype);
		// ---------------------------------------- //

		// ---- Create the buffers that data will be read into. ---- //
		final double[] doubleBuffer = new double[size * 2];
		final byte[] stringBuffer = new byte[size * stringSize];
		final double[] positionBuffer = new double[size * 3];
		// --------------------------------------------------------- //

		// ---- Read the values and uncertainties. ---- //
		// Create the compound datatype.
		datatype = H5.H5Tcreate(H5T_COMPOUND, doubleSize + doubleSize);
		H5.H5Tinsert(datatype, valueName, 0, H5T_NATIVE_DOUBLE);
		H5.H5Tinsert(datatype, uncertaintyName, doubleSize, H5T_NATIVE_DOUBLE);
		// Read the data to the buffer.
		H5.H5Dread(dataset, datatype, H5S_ALL, H5S_ALL, H5P_DEFAULT,
				doubleBuffer);
		// Close datatypes used for this read.
		H5.H5Tclose(datatype);
		// -------------------------------------------- //

		// ---- Read the unit names. ---- //
		// Create the string datatype.
		final int stringDatatype = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
		H5.H5Tset_size(stringDatatype, stringSize);
		// Create the compound datatype.
		datatype = H5.H5Tcreate(H5T_COMPOUND, stringSize);
		H5.H5Tinsert(datatype, unitsName, 0, stringDatatype);
		// Read the data into the buffer.
		H5.H5Dread(dataset, datatype, H5S_ALL, H5S_ALL, H5P_DEFAULT,
				stringBuffer);
		// Close datatypes used for this read.
		H5.H5Tclose(datatype);
		H5.H5Tclose(stringDatatype);
		// ------------------------------ //

		// ---- Read the positions. ---- //
		// Create the position datatype (array of 3 doubles).
		final int positionDatatype = H5.H5Tarray_create(H5T_NATIVE_DOUBLE, 1,
				new long[] { 3 });
		// Create the compound datatype.
		datatype = H5.H5Tcreate(H5T_COMPOUND, doubleSize * 3);
		H5.H5Tinsert(datatype, positionName, 0, positionDatatype);
		// Read the data into the buffer.
		H5.H5Dread(dataset, datatype, H5S_ALL, H5S_ALL, H5P_DEFAULT,
				positionBuffer);
		// Close datatypes used for this read.
		H5.H5Tclose(datatype);
		H5.H5Tclose(positionDatatype);
		// ----------------------------- //

		// ---- Close the Dataset and Dataspace ---- //
		H5.H5Sclose(dataspace);
		H5.H5Dclose(dataset);
		// ----------------------------------------- //

		// ---- Create the data objects from the buffers. ---- //
		dataList = new ArrayList<LWRData>(size);
		int doubleIndex = 0;
		int positionIndex = 0;
		ByteBuffer byteBuf = ByteBuffer.wrap(stringBuffer);
		byte[] string = new byte[stringSize];
		for (int i = 0; i < size; i++) {
			// Create the data.
			LWRData data = new LWRData(datasetName);

			// Set its value and uncertainty.
			data.setValue(doubleBuffer[doubleIndex++]);
			data.setUncertainty(doubleBuffer[doubleIndex++]);

			// Set its units string. Note: We extract the sub-string of length
			// stringSize from the full string buffer.
			byteBuf.get(string, 0, stringSize);
			data.setUnits(new String(string));

			// Set its position.
			ArrayList<Double> position = new ArrayList<Double>(3);
			position.add(positionBuffer[positionIndex++]);
			position.add(positionBuffer[positionIndex++]);
			position.add(positionBuffer[positionIndex++]);
			data.setPosition(position);

			// Add the data to the list.
			dataList.add(data);
		}
		// --------------------------------------------------- //

		return dataList;
	}

	/**
	 * Gets a list of all child groups for the specified group, except for the
	 * group for the LWR component's data (if it exists).
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @return The list of child HDF groups, except for the "State Point Data"
	 *         group.
	 * @throws HDF5LibraryException
	 */
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
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (LWRComponent) component);
			}
		});
		readerMap.put(HDF5LWRTagType.LWRCOMPOSITE, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (LWRComposite) component);
			}
		});

		// Add readers for LWR types.
		readerMap.put(HDF5LWRTagType.LWREACTOR, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (LWReactor) component);
			}
		});
		readerMap.put(HDF5LWRTagType.BWREACTOR, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (BWReactor) component);
			}
		});
		readerMap.put(HDF5LWRTagType.PWREACTOR, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (PressurizedWaterReactor) component);
			}
		});

		// Add readers for the assembly types.
		readerMap.put(HDF5LWRTagType.PWRASSEMBLY, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (PWRAssembly) component);
			}
		});
		readerMap.put(HDF5LWRTagType.CONTROL_BANK, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (ControlBank) component);
			}
		});
		readerMap.put(HDF5LWRTagType.FUEL_ASSEMBLY, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (FuelAssembly) component);
			}
		});
		readerMap.put(HDF5LWRTagType.INCORE_INSTRUMENT, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (IncoreInstrument) component);
			}
		});
		readerMap.put(HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY,
				new IComponentReader() {
					@Override
					public void readComponent(int groupId,
							LWRComponent component)
									throws NullPointerException, HDF5Exception {
						read(groupId, (RodClusterAssembly) component);
					}
				});

		// Add readers for the rod/pin types.
		readerMap.put(HDF5LWRTagType.LWRROD, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (LWRRod) component);
			}
		});

		// Add readers for the ring types.
		readerMap.put(HDF5LWRTagType.RING, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (Ring) component);
			}
		});
		readerMap.put(HDF5LWRTagType.TUBE, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (Tube) component);
			}
		});

		// Add readers for Materials.
		readerMap.put(HDF5LWRTagType.MATERIAL, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (Material) component);
			}
		});
		readerMap.put(HDF5LWRTagType.MATERIALBLOCK, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (MaterialBlock) component);
			}
		});

		// Add readers for other LWRComponent types.
		readerMap.put(HDF5LWRTagType.GRID_LABEL_PROVIDER,
				new IComponentReader() {
					@Override
					public void readComponent(int groupId,
							LWRComponent component)
									throws NullPointerException, HDF5Exception {
						read(groupId, (GridLabelProvider) component);
					}
				});
		readerMap.put(HDF5LWRTagType.LWRGRIDMANAGER, new IComponentReader() {
			@Override
			public void readComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				read(groupId, (LWRGridManager) component);
			}
		});

		return;
	}
}
