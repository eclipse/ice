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
import java.util.Map.Entry;

import org.eclipse.ice.analysistool.IData;
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

/**
 * This clas handles HDF writing for each type of {@link LWRComponent} available
 * in the reactor model. Note that this class operates directly on <i>open</i>
 * HDF groups and does not accept files or URIs themselves.
 * 
 * @author Jordan Deyton
 *
 */
public class LWRComponentWriter {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(LWRComponentWriter.class);

	/**
	 * The factory that provides many helpful methods for writing to HDF files.
	 */
	private final HdfIOFactory factory;

	/**
	 * A simple interface for writing. This is used to redirect write operations
	 * to one for the specific type. We use this because the LWR visitor does
	 * not include visit operations for all types with an {@link HDF5LWRTagType}
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private interface IComponentWriter {
		/**
		 * Writes the component into the HDF group specified by the ID.
		 * 
		 * @param groupId
		 *            The ID of the HDF group to write to.
		 * @param component
		 *            The component that will be written to the group.
		 * @throws NullPointerException
		 * @throws HDF5Exception
		 */
		public void writeComponent(int groupId, LWRComponent component)
				throws NullPointerException, HDF5Exception;
	}

	/**
	 * A map of the writers keyed on their tag type. For the content of the map,
	 * see {@link #addWriters()}.
	 */
	private final Map<HDF5LWRTagType, IComponentWriter> writerMap;

	/**
	 * The default constructor.
	 * 
	 * @param factory
	 *            The parent HDF IO factory used to write to the file.
	 */
	public LWRComponentWriter(HdfIOFactory factory) {
		this.factory = factory;

		// Initialize the map of component writers.
		writerMap = new HashMap<HDF5LWRTagType, IComponentWriter>();
		addWriters();
	}

	/**
	 * Attempts to write the specified {@link LWRComponent}'s content into the
	 * HDF group with the specified ID.
	 * 
	 * @param groupId
	 *            The ID of the group to contain the content.
	 * @param component
	 *            The component to write. Should not be {@code null}.
	 */
	public void writeComponent(int groupId, LWRComponent component) {

		if (component != null) {
			// Find the appropriate writer based on the tag.
			IComponentWriter writer = writerMap.get(component.getHDF5LWRTag());

			// Try to write the component using the writer.
			try {
				writer.writeComponent(groupId, component);
			} catch (NullPointerException | HDF5Exception e) {
				logger.error(
						getClass().getName() + " error: " + "Error writing "
								+ component.getClass().getName()
								+ " with name \"" + component.getName() + "\".",
						e);
			}
		}

		return;
	}

	/**
	 * Writes the group content based on the specified LWRComponent.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param component
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, LWRComponent component)
			throws NullPointerException, HDF5Exception {
		// Write properties inherited from Identifiable...
		factory.writeIdentifiableAttributes(component, groupId);

		// Write properties specific to this type...
		String tag = component.getHDF5LWRTag().toString();
		factory.writeStringAttribute(groupId, "HDF5LWRTag", tag);

		// Write the LWRComponent data...
		writeLWRComponentData(groupId, component);

		return;
	}

	/**
	 * Writes the group content based on the specified LWRComposite.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param composite
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, LWRComposite composite)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) composite);

		// Write properties specific to this type...
		// Nothing to do.

		// Write the child components...
		for (String childName : composite.getComponentNames()) {
			LWRComponent child = composite.getComponent(childName);
			// Create the child's group, write it, and close the group.
			int childGroupId = factory.createGroup(groupId, childName);
			writeComponent(childGroupId, child);
			factory.closeGroup(childGroupId);
		}

		return;
	}

	/**
	 * Writes the group content based on the specified LWReactor.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param reactor
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, LWReactor reactor)
			throws NullPointerException, HDF5Exception {

		// Write properties specific to its super class (LWRComposite)...
		// Note: Components are not handled using the default LWRComposite
		// functionality, so they must be read in manually. Instead, re-direct
		// to the super-super class (LWRComponent).
		write(groupId, (LWRComponent) reactor);

		// Write properties specific to this type...
		factory.writeIntegerAttribute(groupId, "size", reactor.getSize());

		return;
	}

	/**
	 * Writes the group content based on the specified BWReactor.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param reactor
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, BWReactor reactor)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWReactor)...
		write(groupId, (LWReactor) reactor);

		// Write properties specific to this type...
		// Nothing to do.

		// Read the assemblies...
		// Nothing to do. This is not supported in BWReactor yet.

		return;
	}

	/**
	 * Writes the group content based on the specified PressurizedWaterReactor.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param reactor
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, PressurizedWaterReactor reactor)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWReactor)...
		write(groupId, (LWReactor) reactor);

		// Write properties specific to this type...
		factory.writeDoubleAttribute(groupId, "fuelAssemblyPitch",
				reactor.getFuelAssemblyPitch());

		// To write the grid managers and composites, we need to copy their
		// content. This is because they are private members!
		List<LWRComponent> children = new ArrayList<LWRComponent>();

		// We can simply add the grid label provider to the list of
		// LWRComponents that will be written.
		children.add(reactor.getGridLabelProvider());

		int child;
		LWRComponent assembly;
		LWRComposite composite;
		LWRGridManager grid;
		GridLocation location;
		LWRDataProvider data;

		final int size = reactor.getSize();

		// Create maps so the LWRComposite and LWRGridManager names can be found
		// based on the assembly type.
		Map<AssemblyType, String> compositeNames = new HashMap<AssemblyType, String>();
		Map<AssemblyType, String> gridNames = new HashMap<AssemblyType, String>();
		compositeNames.put(AssemblyType.ControlBank,
				PressurizedWaterReactor.CONTROL_BANK_COMPOSITE_NAME);
		gridNames.put(AssemblyType.ControlBank,
				PressurizedWaterReactor.CONTROL_BANK_GRID_MANAGER_NAME);
		compositeNames.put(AssemblyType.Fuel,
				PressurizedWaterReactor.FUEL_ASSEMBLY_COMPOSITE_NAME);
		gridNames.put(AssemblyType.Fuel,
				PressurizedWaterReactor.FUEL_ASSEMBLY_GRID_MANAGER_NAME);
		compositeNames.put(AssemblyType.IncoreInstrument,
				PressurizedWaterReactor.INCORE_INSTRUMENT_COMPOSITE_NAME);
		gridNames.put(AssemblyType.IncoreInstrument,
				PressurizedWaterReactor.INCORE_INSTRUMENT_GRID_MANAGER_NAME);
		compositeNames.put(AssemblyType.RodCluster,
				PressurizedWaterReactor.ROD_CLUSTER_ASSEMBLY_COMPOSITE_NAME);
		gridNames.put(AssemblyType.RodCluster,
				PressurizedWaterReactor.ROD_CLUSTER_ASSEMBLY_GRID_MANAGER_NAME);

		// For each assembly type, duplicate the *private* LWRComposite and
		// LWRGridManager inside the reactor.
		for (AssemblyType type : AssemblyType.values()) {
			// Duplicate the LWRComposite.
			composite = new LWRComposite();
			composite.setName(compositeNames.get(type));
			for (String name : reactor.getAssemblyNames(type)) {
				composite.addComponent(reactor.getAssemblyByName(type, name));
			}
			// Duplicate the LWRGridManager.
			grid = new LWRGridManager(reactor.getSize());
			grid.setName(gridNames.get(type));
			for (int row = 0; row < size; row++) {
				for (int column = 0; column < size; column++) {
					// Get the assembly and the data provider for the location.
					assembly = reactor.getAssemblyByLocation(type, row, column);
					data = reactor.getAssemblyDataProviderAtLocation(type, row,
							column);
					// Add them to the location in the new grid manager.
					location = new GridLocation(row, column);
					location.setLWRDataProvider(data);
					grid.addComponent(assembly, location);
				}
			}
			// Add them both to the list of children that will be written to the
			// HDF file.
			children.add(composite);
			children.add(grid);
		}

		// Write all of the children. This includes the label provider and the
		// grid managers and composites for each assembly type.
		for (LWRComponent component : children) {
			child = factory.createGroup(groupId, component.getName());
			writeComponent(child, component);
			factory.closeGroup(child);
		}

		return;
	}

	/**
	 * Writes the group content based on the specified PWRAssembly.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param assembly
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, PWRAssembly assembly)
			throws NullPointerException, HDF5Exception {

		// Write properties specific to its super class (LWRComposite)...
		// Note: Components are not handled using the default LWRComposite
		// functionality, so they must be read in manually. Instead, re-direct
		// to the super-super class (LWRComponent).
		write(groupId, (LWRComponent) assembly);

		// Write properties specific to this type...
		factory.writeIntegerAttribute(groupId, "size", assembly.getSize());
		factory.writeDoubleAttribute(groupId, "rodPitch",
				assembly.getRodPitch());

		int childGroupId;

		// Write the rods...
		// Build an LWRComposite containing the rods. This mirrors the *private*
		// one inside the assembly.
		LWRComposite composite = new LWRComposite();
		composite.setName(PWRAssembly.LWRROD_COMPOSITE_NAME);
		for (String rodName : assembly.getLWRRodNames()) {
			composite.addComponent(assembly.getLWRRodByName(rodName));
		}
		// Create the composite group, write it, and close it.
		childGroupId = factory.createGroup(groupId, composite.getName());
		writeComponent(childGroupId, composite);
		factory.closeGroup(childGroupId);

		// Write the rod grid locations...
		// Create a duplicate grid manager. This mirrors the *private* one
		// inside the assembly.
		int size = assembly.getSize();
		LWRGridManager gridManager = new LWRGridManager(size);
		gridManager.setName(PWRAssembly.LWRROD_GRID_MANAGER_NAME);
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				// We have to use a GridLocation to add things to the grid.
				GridLocation location = new GridLocation(row, column);
				// Set its data. If null, nothing happens.
				location.setLWRDataProvider(
						assembly.getLWRRodDataProviderAtLocation(row, column));
				// Add the rod to the location. If there is nothing in that
				// location, nothing happens.
				gridManager.addComponent(
						assembly.getLWRRodByLocation(row, column), location);
			}
		}
		// Create the grid manager's group, write it, and close it.
		childGroupId = factory.createGroup(groupId, gridManager.getName());
		writeComponent(childGroupId, gridManager);
		factory.closeGroup(childGroupId);

		return;
	}

	/**
	 * Writes the group content based on the specified ControlBank.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param controlBank
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, ControlBank controlBank)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) controlBank);

		// Write properties specific to this type...
		factory.writeIntegerAttribute(groupId, "maxNumberOfSteps",
				controlBank.getMaxNumberOfSteps());
		factory.writeDoubleAttribute(groupId, "stepSize",
				controlBank.getStepSize());

		// There are no sub-assembly components.

		return;
	}

	/**
	 * Writes the group content based on the specified FuelAssembly.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param assembly
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, FuelAssembly assembly)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (PWRAssembly)...
		write(groupId, (PWRAssembly) assembly);

		// Write properties specific to this type...
		// Nothing to do.

		int childGroupId;

		// Write the grid label provider...
		GridLabelProvider labels = assembly.getGridLabelProvider();
		// Create the label provider's group, write it, and close it.
		childGroupId = factory.createGroup(groupId, labels.getName());
		writeComponent(childGroupId, labels);
		factory.closeGroup(childGroupId);

		// Write the tubes...
		// Build an LWRComposite containing the tubes. This mirrors the
		// *private* one inside the assembly.
		LWRComposite composite = new LWRComposite();
		composite.setName(FuelAssembly.TUBE_COMPOSITE_NAME);
		for (String tubeName : assembly.getTubeNames()) {
			composite.addComponent(assembly.getTubeByName(tubeName));
		}
		// Create the composite group, write it, and close it.
		childGroupId = factory.createGroup(groupId, composite.getName());
		writeComponent(childGroupId, composite);
		factory.closeGroup(childGroupId);

		// Write the tube grid locations...
		// Create a duplicate grid manager. This mirrors the *private* one
		// inside the assembly.
		int size = assembly.getSize();
		LWRGridManager gridManager = new LWRGridManager(size);
		gridManager.setName(FuelAssembly.TUBE_GRID_MANAGER_NAME);
		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				// We have to use a GridLocation to add things to the grid.
				GridLocation location = new GridLocation(row, column);
				// Set its data. If null, nothing happens.
				location.setLWRDataProvider(
						assembly.getTubeDataProviderAtLocation(row, column));
				// Add the tube to the location. If there is nothing in that
				// location, nothing happens.
				gridManager.addComponent(
						assembly.getTubeByLocation(row, column), location);
			}
		}
		// Create the grid manager's group, write it, and close it.
		childGroupId = factory.createGroup(groupId, gridManager.getName());
		writeComponent(childGroupId, gridManager);
		factory.closeGroup(childGroupId);

		return;
	}

	/**
	 * Writes the group content based on the specified IncoreInstrument.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param incoreInstrument
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, IncoreInstrument incoreInstrument)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) incoreInstrument);

		// Write properties specific to this type...
		// Nothing to do.

		// Write the thimble (a Ring).
		Ring ring = incoreInstrument.getThimble();
		// Create the ring's group, write it, and close it.
		int ringGroupId = factory.createGroup(groupId, ring.getName());
		writeComponent(ringGroupId, ring);
		factory.closeGroup(ringGroupId);

		// There are no sub-assembly components.

		return;
	}

	/**
	 * Writes the group content based on the specified RodClusterAssembly.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param assembly
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, RodClusterAssembly assembly)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (PWRAssembly)...
		write(groupId, (PWRAssembly) assembly);

		// Write properties specific to this type...
		// Nothing to do.

		// There are also no sub-assembly components besides the rods, which are
		// managed by PWRAssembly.

		return;
	}

	/**
	 * Writes the group content based on the specified LWRRod.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param rod
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, LWRRod rod)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) rod);

		// Write properties specific to this type...
		factory.writeDoubleAttribute(groupId, "pressure", rod.getPressure());

		// Write the rod's fill gas...
		Material gas = rod.getFillGas();
		int gasGroupId = factory.createGroup(groupId, gas.getName());
		writeComponent(gasGroupId, gas);
		factory.closeGroup(gasGroupId);

		// Write the rod's clad...
		Ring clad = rod.getClad();
		int cladGroupId = factory.createGroup(groupId, clad.getName());
		writeComponent(cladGroupId, clad);
		factory.closeGroup(cladGroupId);

		// Write the rod's material blocks...
		for (MaterialBlock block : rod.getMaterialBlocks()) {
			int blockGroupId = factory.createGroup(groupId, block.getName());
			writeComponent(blockGroupId, block);
			factory.closeGroup(blockGroupId);
		}

		return;
	}

	/**
	 * Writes the group content based on the specified Ring.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param ring
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, Ring ring)
			throws NullPointerException, HDF5Exception {

		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) ring);

		// Write properties specific to this type...
		factory.writeDoubleAttribute(groupId, "height", ring.getHeight());
		factory.writeDoubleAttribute(groupId, "outerRadius",
				ring.getOuterRadius());
		factory.writeDoubleAttribute(groupId, "innerRadius",
				ring.getInnerRadius());

		// Write the material.
		Material material = ring.getMaterial();
		int materialGroupId = factory.createGroup(groupId, material.getName());
		writeComponent(materialGroupId, material);
		factory.closeGroup(materialGroupId);

		return;
	}

	/**
	 * Writes the group content based on the specified Tube.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param tube
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, Tube tube)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (Ring)...
		write(groupId, (Ring) tube);

		// Write properties specific to this type...
		factory.writeStringAttribute(groupId, "tubeType",
				tube.getTubeType().toString());

		return;
	}

	/**
	 * Writes the group content based on the specified Material.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param material
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, Material material)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) material);

		// Write properties specific to this type...
		MaterialType type = material.getMaterialType();
		factory.writeStringAttribute(groupId, "materialType", type.toString());

		return;
	}

	/**
	 * Writes the group content based on the specified MaterialBlock.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param block
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, MaterialBlock block)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) block);

		// Write properties specific to this type...
		factory.writeDoubleAttribute(groupId, "position", block.getPosition());

		// Write all of the rings in the block.
		for (Ring ring : block.getRings()) {
			int ringGroupId = factory.createGroup(groupId, ring.getName());
			writeComponent(ringGroupId, ring);
			factory.closeGroup(ringGroupId);
		}

		return;
	}

	/**
	 * Writes the group content based on the specified GridLabelProvider.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param provider
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, GridLabelProvider provider)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) provider);

		int size = provider.getSize();

		// Write properties specific to this type...
		factory.writeIntegerAttribute(groupId, "size", size);

		// Write the labels...
		// Create the "Labels" group.
		int labelsGroupId = factory.createGroup(groupId,
				GridLabelProvider.LABELS_GROUP_NAME);

		String datasetName;
		String[] labels = new String[size];
		String label;
		boolean hasLabels = false;

		// Build a string array from the column labels.
		for (int column = 0; column < size; column++) {
			label = provider.getLabelFromColumn(column);
			if (label != null) {
				labels[column] = label;
				hasLabels = true;
			} else {
				labels[column] = "";
			}
		}
		// If there are column labels, write the dataset.
		if (hasLabels) {
			datasetName = GridLabelProvider.COLUMN_LABELS_NAME;
			factory.writeStringArrayDataset(labelsGroupId, datasetName, labels);
		}

		// Build a string array from the row labels.
		for (int row = 0; row < size; row++) {
			label = provider.getLabelFromRow(row);
			if (label != null) {
				labels[row] = label;
				hasLabels = true;
			} else {
				labels[row] = "";
			}
		}
		// If there are row labels, write the dataset.
		if (hasLabels) {
			datasetName = GridLabelProvider.ROW_LABELS_NAME;
			factory.writeStringArrayDataset(labelsGroupId, datasetName, labels);
		}

		// Close the "Labels" group.
		factory.closeGroup(labelsGroupId);

		return;
	}

	/**
	 * Writes the group content based on the specified LWRGridManager.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param gridManager
	 *            The object to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void write(int groupId, LWRGridManager gridManager)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) gridManager);

		// Write properties specific to this type...
		int size = gridManager.getSize();
		factory.writeIntegerAttribute(groupId, "size", gridManager.getSize());

		// Create the "Positions" group.
		String groupName = "Positions";
		int positionsGroupId = factory.createGroup(groupId, groupName);

		Map<String, Integer> namesMap = new HashMap<String, Integer>();
		Map<String, Integer> unitsMap = new HashMap<String, Integer>();

		for (int row = 0; row < size; row++) {
			for (int column = 0; column < size; column++) {
				GridLocation location = new GridLocation(row, column);

				// Get the name of the component at that location.
				String name = gridManager.getComponentName(location);
				if (name != null) {

					location.setLWRDataProvider(
							gridManager.getDataProviderAtLocation(location));

					// Create the grid location's group, write it, and close it.
					groupName = "Position " + row + " " + column;
					int positionGroupId = factory.createGroup(positionsGroupId,
							groupName);
					writeGridLocation(positionGroupId, location, name, namesMap,
							unitsMap);
					factory.closeGroup(positionGroupId);
				}
			}
		}

		String[] stringArray;

		// Write the component names...
		// Note: We need to convert the map into an array using the map values
		// as the index in the array.
		if (!namesMap.isEmpty()) {
			stringArray = new String[namesMap.size()];
			for (Entry<String, Integer> e : namesMap.entrySet()) {
				stringArray[e.getValue()] = e.getKey();
			}
			factory.writeStringArrayDataset(positionsGroupId,
					"Simple Position Names Table", stringArray);

			// Write the unit names...
			// Note: We need to convert the map into an array using the map
			// values as the index in the array.
			if (!unitsMap.isEmpty()) {
				stringArray = new String[unitsMap.size()];
				for (Entry<String, Integer> e : unitsMap.entrySet()) {
					stringArray[e.getValue()] = e.getKey();
				}
				factory.writeStringArrayDataset(positionsGroupId, "Units Table",
						stringArray);
			}

			// Close the "Positions" group.
			factory.closeGroup(positionsGroupId);
		}
		// If there are no names, then there are no elements in the grid. We
		// should delete the "Positions" group!
		else {
			// Close the "Positions" group.
			factory.closeGroup(positionsGroupId);
			H5.H5Ldelete(groupId, "Positions", HDF5Constants.H5P_DEFAULT);
		}

		return;
	}

	/**
	 * Writes the group content based on the specified GridLocation.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param location
	 *            The object to write.
	 * @param name
	 *            The name of the component at that location.
	 * @param namesMap
	 *            The map of names of components in the whole grid. This will be
	 *            updated if the component's name is new.
	 * @param unitsMap
	 *            The map of unit names. This will be updated if the location
	 *            contains data with a new unit.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeGridLocation(int groupId, GridLocation location,
			String name, Map<String, Integer> namesMap,
			Map<String, Integer> unitsMap)
					throws NullPointerException, HDF5Exception {

		// Get the index of the component name. If the name is new to the map,
		// add it to the map.
		final int index;
		if (!namesMap.containsKey(name)) {
			index = namesMap.size();
			namesMap.put(name, index);
		} else {
			index = namesMap.get(name);
		}

		// Create the "Position Dataset" dataset.
		long[] dims = new long[] { 3 };
		int[] buffer = new int[] { location.getRow(), location.getColumn(),
				index };
		factory.writeDataset(groupId, "Position Dataset", 1, dims,
				HDF5Constants.H5T_NATIVE_INT, buffer);

		ArrayList<Double> position;

		// Record the initial time so it can be reset after the write has
		// completed.
		LWRDataProvider dataProvider = location.getLWRDataProvider();
		double initialTime = dataProvider.getCurrentTime();

		// Write the data for each timestep.
		for (double time : dataProvider.getTimes()) {
			int timestep = dataProvider.getTimeStep(time);

			// Create the timestep group.
			int timestepGroupId = factory.createGroup(groupId,
					"TimeStep: " + timestep);

			// Write the time to the group as an attribute.
			factory.writeDoubleAttribute(timestepGroupId, "time", time);

			// Write the features...
			for (String feature : dataProvider.getFeaturesAtCurrentTime()) {
				List<IData> dataList = dataProvider
						.getDataAtCurrentTime(feature);
				int nData = dataList.size();

				// Allocate the buffer for the values, uncertainties, and
				// position data (all doubles).
				double[] dataBuffer = new double[nData * 5];
				// Allocate the buffer for the head table (used for looking up
				// units). The first column is the data table index, and the
				// second column is the units lookup index.
				buffer = new int[nData * 2];

				int dataBufferIndex = 0;
				int headBufferIndex = 0;
				for (int i = 0; i < nData; i++) {
					IData data = dataList.get(i);

					// Determine the lookup index for the units name.
					final int unitsIndex;
					String units = data.getUnits();
					if (unitsMap.containsKey(units)) {
						unitsIndex = unitsMap.get(units);
					} else {
						unitsIndex = unitsMap.size();
						unitsMap.put(units, unitsIndex);
					}

					// Add the value, uncertainty, and position data to the data
					// buffer.
					dataBuffer[dataBufferIndex++] = data.getValue();
					dataBuffer[dataBufferIndex++] = data.getUncertainty();
					position = data.getPosition();
					dataBuffer[dataBufferIndex++] = position.get(0);
					dataBuffer[dataBufferIndex++] = position.get(1);
					dataBuffer[dataBufferIndex++] = position.get(2);

					// Add the data table index and units name index to the head
					// table buffer.
					buffer[headBufferIndex++] = i;
					buffer[headBufferIndex++] = unitsIndex;
				}

				dims = new long[2];

				// Write the "data table" dataset.
				dims[0] = nData;
				dims[1] = 5;
				factory.writeDataset(timestepGroupId, feature + " dataTable", 2,
						dims, HDF5Constants.H5T_NATIVE_DOUBLE, dataBuffer);

				// Write the "head table" dataset.
				dims[1] = 2;
				factory.writeDataset(timestepGroupId, feature + " headTable", 2,
						dims, HDF5Constants.H5T_NATIVE_INT, buffer);
			}

			// Close the timestep group.
			factory.closeGroup(timestepGroupId);
		}

		// Restore the initial time.
		dataProvider.setTime(initialTime);

		return;
	}

	/**
	 * Writes all of the data for an IDataProvider (implemented by
	 * LWRComponent).
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param provider
	 *            The IDataProvider to write the data from.
	 * 
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeLWRComponentData(int groupId, LWRComponent provider)
			throws NullPointerException, HDF5Exception {
		// FIXME Why do we write this if there is no data? Changing this would
		// require further changes in the native IO library.

		// Get the initial timestep.
		final double initialTimestep = provider.getCurrentTime();
		final String units = provider.getTimeUnits();

		// Create the encapsulating group.
		int dataGroupId = factory.createGroup(groupId, "State Point Data");

		// Loop over the timesteps.
		for (double time : provider.getTimes()) {
			// Set the provider to the current timestep.
			int timestep = provider.getTimeStep(time);
			provider.setTime(time);

			// Create the group for the timestep.
			String groupName = "Timestep: "
					+ (Integer.toString((int) timestep));
			int timestepGroupId = factory.createGroup(dataGroupId, groupName);

			// Set the time and units attributes on the timestep group.
			factory.writeDoubleAttribute(timestepGroupId, "time", time);
			factory.writeStringAttribute(timestepGroupId, "units", units);

			// Loop over the features at this timestep.
			for (String datasetName : provider.getFeaturesAtCurrentTime()) {
				List<IData> dataList = provider
						.getDataAtCurrentTime(datasetName);
				writeLWRData(timestepGroupId, datasetName, dataList);
			}

			// Close the group for the timestep.
			factory.closeGroup(timestepGroupId);
		}

		// Close the "State Point Data" group that holds the data.
		factory.closeGroup(dataGroupId);

		// Restore the initial timestep.
		provider.setTime(initialTimestep);

		return;
	}

	/**
	 * Writes the {@link LWRData} from the list into a dataset in an HDF group.
	 * 
	 * @param groupId
	 *            The ID of the parent group.
	 * @param datasetName
	 *            The name to use for the dataset.
	 * @param dataList
	 *            The list of data to write.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeLWRData(int groupId, String datasetName,
			List<IData> dataList) throws NullPointerException, HDF5Exception {

		// FIXME There is no error checking on the return values from H5 calls.

		// Constants used to avoid constant use of HDF5Constants namespace.
		final int H5P_DEFAULT = HDF5Constants.H5P_DEFAULT;
		final int H5S_ALL = HDF5Constants.H5S_ALL;
		final int H5T_COMPOUND = HDF5Constants.H5T_COMPOUND;
		final int H5T_NATIVE_DOUBLE = HDF5Constants.H5T_NATIVE_DOUBLE;

		// The names of the columns in the dataset.
		final String valueName = "value";
		final String uncertaintyName = "uncertainty";
		final String unitsName = "units";
		final String positionName = "position";

		// ---- Create the buffers from which the dataset is written. ---- //
		int size = 0;
		for (IData data : dataList) {
			String units = data.getUnits();
			if (units.length() > size) {
				size = units.length();
			}
		}
		final int stringSize = size;

		size = dataList.size();
		long[] dims = new long[] { 1, size };

		// Allocate the buffers.
		double[] doubleBuffer = new double[2 * size];
		ByteBuffer byteBuf = ByteBuffer.allocate(stringSize * size);
		byte[] stringBuffer;
		double[] positionBuffer = new double[3 * size];

		int doubleIndex = 0;
		int positionIndex = 0;
		for (IData data : dataList) {

			// Update the value/uncertainty buffer.
			doubleBuffer[doubleIndex++] = data.getValue();
			doubleBuffer[doubleIndex++] = data.getUncertainty();

			// Update the string buffer.
			String units = data.getUnits();
			// Add the string's bytes.
			byteBuf.put(units.getBytes());
			// Skip ahead to the next string's start index.
			byteBuf.position(byteBuf.position() + stringSize - units.length());

			// Update the position buffer.
			List<Double> position = data.getPosition();
			positionBuffer[positionIndex++] = position.get(0);
			positionBuffer[positionIndex++] = position.get(1);
			positionBuffer[positionIndex++] = position.get(2);
		}
		// Get the backing array from the byte buffer.
		stringBuffer = byteBuf.array();
		// --------------------------------------------------------------- //

		// ---- Create the Dataset ---- //
		// Get the size of the double datatype.
		int doubleSize = H5.H5Tget_size(H5T_NATIVE_DOUBLE);

		// Create the string datatype.
		final int stringDatatype = H5.H5Tcopy(HDF5Constants.H5T_C_S1);
		H5.H5Tset_size(stringDatatype, stringSize);

		// Create the position datatype.
		final int positionDatatype = H5.H5Tarray_create(H5T_NATIVE_DOUBLE, 1,
				new long[] { 3 });
		final int positionSize = doubleSize * 3;

		// Create the compound datatype for the whole dataset.
		int offset = 0;
		int datatype = H5.H5Tcreate(H5T_COMPOUND,
				doubleSize + doubleSize + stringSize + positionSize);
		H5.H5Tinsert(datatype, valueName, 0, H5T_NATIVE_DOUBLE);
		offset = doubleSize;
		H5.H5Tinsert(datatype, uncertaintyName, offset, H5T_NATIVE_DOUBLE);
		offset += doubleSize;
		H5.H5Tinsert(datatype, unitsName, offset, stringDatatype);
		offset += stringSize;
		H5.H5Tinsert(datatype, positionName, offset, positionDatatype);

		// Create the dataset.
		int dataspace = H5.H5Screate_simple(2, dims, null);
		int dataset = H5.H5Dcreate(groupId, datasetName, datatype, dataspace,
				H5P_DEFAULT, H5P_DEFAULT, H5P_DEFAULT);

		// Close the compound datatype used to create the dataset.
		H5.H5Tclose(datatype);
		// ---------------------------- //

		// ---- Write the value and uncertainty. ---- //
		// Create the compound datatype.
		datatype = H5.H5Tcreate(H5T_COMPOUND, doubleSize + doubleSize);
		H5.H5Tinsert(datatype, valueName, 0, H5T_NATIVE_DOUBLE);
		H5.H5Tinsert(datatype, uncertaintyName, doubleSize, H5T_NATIVE_DOUBLE);
		// Write the data from the buffer.
		H5.H5Dwrite(dataset, datatype, H5S_ALL, H5S_ALL, H5P_DEFAULT,
				doubleBuffer);
		// Close datatypes used for this write.
		H5.H5Tclose(datatype);
		// ------------------------------------------ //

		// ---- Write the units. ---- //
		// Create the compound datatype.
		datatype = H5.H5Tcreate(H5T_COMPOUND, stringSize);
		H5.H5Tinsert(datatype, unitsName, 0, stringDatatype);
		// Write the data from the buffer.
		H5.H5Dwrite(dataset, datatype, H5S_ALL, H5S_ALL, H5P_DEFAULT,
				stringBuffer);
		// Close datatypes used for this write.
		H5.H5Tclose(datatype);
		H5.H5Tclose(stringDatatype);
		// -------------------------- //

		// ---- Write the positions. ---- //
		// Create the compound datatype.
		datatype = H5.H5Tcreate(H5T_COMPOUND, positionSize);
		H5.H5Tinsert(datatype, positionName, 0, positionDatatype);
		// Write the data from the buffer.
		H5.H5Dwrite(dataset, datatype, H5S_ALL, H5S_ALL, H5P_DEFAULT,
				positionBuffer);
		// Close datatypes used for this write.
		H5.H5Tclose(datatype);
		H5.H5Tclose(positionDatatype);
		// ------------------------------ //

		// Close the dataspace and dataset.
		H5.H5Sclose(dataspace);
		H5.H5Dclose(dataset);

		return;
	}

	/**
	 * Populates the {@link #writerMap} with {@link IComponentWriter}s that
	 * simply re-direct to the appropriate write operation based on the tag.
	 */
	private void addWriters() {
		// Add writers for base types.
		writerMap.put(HDF5LWRTagType.LWRCOMPONENT, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (LWRComponent) component);
			}
		});
		writerMap.put(HDF5LWRTagType.LWRCOMPOSITE, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (LWRComposite) component);
			}
		});

		// Add writers for LWR types.
		writerMap.put(HDF5LWRTagType.LWREACTOR, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (LWReactor) component);
			}
		});
		writerMap.put(HDF5LWRTagType.BWREACTOR, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (BWReactor) component);
			}
		});
		writerMap.put(HDF5LWRTagType.PWREACTOR, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (PressurizedWaterReactor) component);
			}
		});

		// Add writers for the assembly types.
		writerMap.put(HDF5LWRTagType.PWRASSEMBLY, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (PWRAssembly) component);
			}
		});
		writerMap.put(HDF5LWRTagType.CONTROL_BANK, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (ControlBank) component);
			}
		});
		writerMap.put(HDF5LWRTagType.FUEL_ASSEMBLY, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (FuelAssembly) component);
			}
		});
		writerMap.put(HDF5LWRTagType.INCORE_INSTRUMENT, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (IncoreInstrument) component);
			}
		});
		writerMap.put(HDF5LWRTagType.ROD_CLUSTER_ASSEMBLY,
				new IComponentWriter() {
					@Override
					public void writeComponent(int groupId,
							LWRComponent component)
									throws NullPointerException, HDF5Exception {
						write(groupId, (RodClusterAssembly) component);
					}
				});

		// Add writers for the rod/pin types.
		writerMap.put(HDF5LWRTagType.LWRROD, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (LWRRod) component);
			}
		});

		// Add writers for the ring types.
		writerMap.put(HDF5LWRTagType.RING, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (Ring) component);
			}
		});
		writerMap.put(HDF5LWRTagType.TUBE, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (Tube) component);
			}
		});

		// Add writers for Materials.
		writerMap.put(HDF5LWRTagType.MATERIAL, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (Material) component);
			}
		});
		writerMap.put(HDF5LWRTagType.MATERIALBLOCK, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (MaterialBlock) component);
			}
		});

		// Add writers for other LWRComponent types.
		writerMap.put(HDF5LWRTagType.GRID_LABEL_PROVIDER,
				new IComponentWriter() {
					@Override
					public void writeComponent(int groupId,
							LWRComponent component)
									throws NullPointerException, HDF5Exception {
						write(groupId, (GridLabelProvider) component);
					}
				});
		writerMap.put(HDF5LWRTagType.LWRGRIDMANAGER, new IComponentWriter() {
			@Override
			public void writeComponent(int groupId, LWRComponent component)
					throws NullPointerException, HDF5Exception {
				write(groupId, (LWRGridManager) component);
			}
		});

		return;
	}
}
