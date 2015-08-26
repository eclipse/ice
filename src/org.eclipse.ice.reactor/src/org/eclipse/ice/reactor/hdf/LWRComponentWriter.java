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

public class LWRComponentWriter {

	/*-
	 * Improvements:
	 * 
	 * 1 - Groups are written even if there is nothing there, including:
	 *   a - LWRComponent's "State Point Data"
	 *   b - Grid labels for PWRs and FuelAssemblies.
	 * 2 - LWRComponent's implementation of IDataProvider:
	 *   a - Uses a compound datatype (double, double, string, double[3]).
	 *   b - Can be combined into simpler datasets for faster reading.
	 * 3 - There is also room for coalescing grid data providers into one large
	 *     multi-dimensional table.
	 * 4 - No re-use of the same components. If the same clad is used twice, it
	 *     will be written twice in the file and become two separate instances
	 *     when read.
	 */

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(LWRComponentWriter.class);

	private final HdfIOFactory factory;

	private interface IComponentWriter {
		public void writeComponent(int groupId, LWRComponent component)
				throws NullPointerException, HDF5Exception;
	}

	private final Map<HDF5LWRTagType, IComponentWriter> writerMap;

	public LWRComponentWriter(HdfIOFactory factory) {
		this.factory = factory;

		// Initialize the map of component writers.
		writerMap = new HashMap<HDF5LWRTagType, IComponentWriter>();
		addWriters();
	}

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

	private void write(int groupId, LWRComposite composite)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) composite);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, LWReactor reactor)
			throws NullPointerException, HDF5Exception {

		// Write properties specific to its super class (LWRComposite)...
		write(groupId, (LWRComponent) reactor);

		// Write properties specific to this type...
		// TODO

		return;
	}

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

	private void write(int groupId, PressurizedWaterReactor reactor)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWReactor)...
		write(groupId, (LWReactor) reactor);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, PWRAssembly assembly)
			throws NullPointerException, HDF5Exception {

		// Write properties specific to its super class (LWRComposite)...
		write(groupId, (LWRComponent) assembly);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, ControlBank controlBank)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) controlBank);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, FuelAssembly assembly)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (PWRAssembly)...
		write(groupId, (PWRAssembly) assembly);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, IncoreInstrument incoreInstrument)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) incoreInstrument);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, RodClusterAssembly assembly)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (PWRAssembly)...
		write(groupId, (PWRAssembly) assembly);

		// Write properties specific to this type...
		// TODO

		return;
	}

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

	private void write(int groupId, Tube tube)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (Ring)...
		write(groupId, (Ring) tube);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, Material material)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) material);

		// Write properties specific to this type...
		MaterialType type = material.getMaterialType();
		factory.writeStringAttribute(groupId, "materialType", type.toString());

		return;
	}

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

	private void write(int groupId, GridLabelProvider provider)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) provider);

		// Write properties specific to this type...
		// TODO

		return;
	}

	private void write(int groupId, LWRGridManager gridManager)
			throws NullPointerException, HDF5Exception {
		// Write properties specific to its super class (LWRComponent)...
		write(groupId, (LWRComponent) gridManager);

		// Write properties specific to this type...
		// TODO

		return;
	}

	/**
	 * Writes all of the data for an IDataProvider (implemented by
	 * LWRComponent).
	 * 
	 * @param groupId
	 *            The ID of the parent HDF5 Group for the component.
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
			// Fill the space allocated for this string with null bytes.
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
