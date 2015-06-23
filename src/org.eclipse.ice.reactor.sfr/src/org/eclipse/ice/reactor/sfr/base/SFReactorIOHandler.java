/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor.sfr.base;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.structs.H5O_info_t;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.reactor.sfr.core.AssemblyType;
import org.eclipse.ice.reactor.sfr.core.Material;
import org.eclipse.ice.reactor.sfr.core.MaterialBlock;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.eclipse.ice.reactor.sfr.core.assembly.PinAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.PinType;
import org.eclipse.ice.reactor.sfr.core.assembly.ReflectorAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.Ring;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRRod;

/**
 * <p>
 * Class acts as an intermediary between the reactor and HDF5 data. This class
 * both reads HDF5 data into the SFReactor, and writes from the SFReactor into
 * HDF5 data.
 * </p>
 * 
 * @author Anna Wojtowicz
 */
public class SFReactorIOHandler {
	/**
	 * <p>
	 * Nullary constructor.
	 * </p>
	 * 
	 */
	public SFReactorIOHandler() {

		// Nothing to do here.

	}

	/**
	 * Reads data from an input HDF5 file into a SFReactor.
	 * 
	 * @return A valid {@link SFReactor} if the file could be completely read,
	 *         {@code null} if the file could not be opened.
	 */
	public SFReactor readHDF5(URI uri) {

		// The SFReactor that will receive the data from the file.
		SFReactor reactor = null;

		// Check the parameters.
		if (uri == null) {
			return reactor;
		}
		// Check the file associated with the URI. We need to be able to read
		// from it.
		File file = new File(uri);
		String path = file.getPath();
		if (!file.canRead()) {
			System.err.println("SFReactorIOHandler error: File \"" + path
					+ "\" cannot be read.");
			return reactor;
		}

		// HDF5 constants. Writing out "HDF5Constants." every time is annoying.
		int H5P_DEFAULT = HDF5Constants.H5P_DEFAULT; // Default flag.
		int H5F_ACC_RDONLY = HDF5Constants.H5F_ACC_RDONLY; // Open read-only.
		int H5T_NATIVE_INT = HDF5Constants.H5T_NATIVE_INT; // int
		int H5T_NATIVE_DOUBLE = HDF5Constants.H5T_NATIVE_DOUBLE; // double
		int H5O_TYPE_GROUP = HDF5Constants.H5O_TYPE_GROUP;

		// The status of the previous HDF5 operation. Generally, if it is
		// negative, there was some error.
		int status;

		// Other IDs for HDF5 components.
		int fileId, groupId;

		// A stack representing the currently opened groups.
		Stack<Integer> groupIds = new Stack<Integer>();

		Integer[] intBuffer = new Integer[1];
		Double[] doubleBuffer = new Double[1];

		try {
			// Open the H5 file with read-only access.
			status = H5.H5Fopen(path, H5F_ACC_RDONLY, H5P_DEFAULT);
			if (status < 0) {
				throwException("Opening file \"" + path + "\"", status);
			}
			fileId = status;

			// Currently, we only support a single reactor.
			groupId = groupIds.push(openGroup(fileId, "/SFReactor"));

			// Get the size of the reactor in the file and initialize it.
			int size = (Integer) readAttribute(groupId, "size", H5T_NATIVE_INT,
					intBuffer);
			reactor = new SFReactor(size);

			/* ---- Read the reactor's attributes. ---- */
			// Attributes inherited from SFRComponent.
			readSFRComponent(reactor, groupId);

			// Attributes inherited from SFRComposite.
			// none

			// SFReactor-specific attributes.
			// size has already been read.
			reactor.setLatticePitch((Double) readAttribute(groupId,
					"latticePitch", H5T_NATIVE_DOUBLE, doubleBuffer));
			reactor.setOuterFlatToFlat((Double) readAttribute(groupId,
					"outerFlatToFlat", H5T_NATIVE_DOUBLE, doubleBuffer));
			/* ---------------------------------------- */

			/* ---- Read the reactor's Pin Assemblies. ---- */
			List<AssemblyType> pinAssemblyTypes = new ArrayList<AssemblyType>();
			pinAssemblyTypes.add(AssemblyType.Fuel);
			pinAssemblyTypes.add(AssemblyType.Control);
			pinAssemblyTypes.add(AssemblyType.Shield);
			pinAssemblyTypes.add(AssemblyType.Test);

			for (AssemblyType assemblyType : pinAssemblyTypes) {
				// Open the group for this assembly type.
				groupId = groupIds.push(openGroup(groupIds.peek(),
						assemblyType.toString()));

				// Loop over the child groups in this assembly type's group.
				// These groups should have the assembly names.
				for (String assemblyName : getChildNames(groupId,
						H5O_TYPE_GROUP)) {

					// Open the group for this assembly.
					groupId = groupIds.push(openGroup(groupIds.peek(),
							assemblyName));

					// Read the name, pinType, and size of the assembly.
					PinType pinType = PinType.valueOf((Integer) readAttribute(
							groupId, "pinType", H5T_NATIVE_INT, intBuffer));
					size = (Integer) readAttribute(groupId, "size",
							H5T_NATIVE_INT, intBuffer);

					// Initialize the assembly.
					PinAssembly assembly = new PinAssembly(assemblyName,
							pinType, size);

					/* --- Read the assembly's attributes. --- */
					// Attributes inherited from SFRComponent.
					readSFRComponent(assembly, groupId);

					// Attributes inherited from SFRComposite.
					// none

					// Attributes inherited from SFRAssembly.
					// size has already been read.
					assembly.setDuctThickness((Double) readAttribute(groupId,
							"ductThickness", H5T_NATIVE_DOUBLE, doubleBuffer));

					// PinAssembly-specific attributes.
					assembly.setPinPitch((Double) readAttribute(groupId,
							"pinPitch", H5T_NATIVE_DOUBLE, doubleBuffer));
					// pinType has already been read.
					assembly.setInnerDuctFlatToFlat((Double) readAttribute(
							groupId, "innerDuctFlatToFlat", H5T_NATIVE_DOUBLE,
							doubleBuffer));
					assembly.setInnerDuctThickness((Double) readAttribute(
							groupId, "innerDuctThickness", H5T_NATIVE_DOUBLE,
							doubleBuffer));
					/* --------------------------------------- */

					/* --- Read the assembly's reactor locations. --- */
					// Add the assembly to the reactor.
					reactor.addAssembly(assemblyType, assembly);

					// Set the assembly's locations in the reactor.
					for (int location : readLocationData(groupId)) {
						reactor.setAssemblyLocation(assemblyType, assemblyName,
								location / reactor.getSize(), location
										% reactor.getSize());
					}
					/* ---------------------------------------------- */

					/* --- Read the assembly's pins. --- */
					// Open the group that holds the pins.
					groupId = groupIds.push(openGroup(groupId, "Pins"));

					// Loop over the child groups in this assembly's Pins group.
					// These groups should have the pin names.
					for (String pinName : getChildNames(groupId, H5O_TYPE_GROUP)) {

						// Open the group for this pin.
						groupId = groupIds.push(openGroup(groupIds.peek(),
								pinName));

						// So we don't waste time creating default properties
						// for the pin that will soon be replaced, we should
						// first read in the physical structure of the pin.
						Ring cladding = null;
						Material fillGas = null;
						TreeSet<MaterialBlock> materialBlocks = null;

						/* -- Read in the cladding. -- */
						groupId = openGroup(groupId, "cladding");
						cladding = readRing(groupId);
						closeGroup(groupId);
						/* --------------------------- */

						/* -- Read in the fill gas. -- */
						// Initialize the container for the fill gas.
						fillGas = new Material();

						// Read the material's attributes.
						groupId = openGroup(groupIds.peek(), "fillGas");
						readSFRComponent(fillGas, groupId);
						closeGroup(groupId);
						/* --------------------------- */

						/* -- Read in the material blocks. -- */
						// Initialize the container for the material blocks.
						materialBlocks = new TreeSet<MaterialBlock>();

						// Open the material blocks group.
						groupId = groupIds.push(openGroup(groupIds.peek(),
								"materialBlocks"));

						// Loop over the child groups of materialBlocks. They
						// correspond to individual MaterialBlocks in the
						// TreeSet.
						for (String groupName : getChildNames(groupIds.peek(),
								H5O_TYPE_GROUP)) {
							// Initialize a MaterialBlock.
							MaterialBlock block = new MaterialBlock();

							// Open the MaterialBlock's group.
							groupId = groupIds.push(openGroup(groupIds.peek(),
									groupName));

							// Read the block's SFRComponent attributes.
							readSFRComponent(block, groupId);

							// Read the block's other attributes.
							block.setVertPosition((Double) readAttribute(
									groupId, "vertPosition", H5T_NATIVE_DOUBLE,
									doubleBuffer));

							/* - Read the block's rings. - */

							// Open the Rings group.
							groupId = groupIds
									.push(openGroup(groupId, "Rings"));

							// Loop over the child groups of the block. They
							// correspond to individual rings in the block's
							// TreeSet.
							for (String ringGroupName : getChildNames(groupId,
									H5O_TYPE_GROUP)) {
								groupId = openGroup(groupIds.peek(),
										ringGroupName);
								block.addRing(readRing(groupId));
								closeGroup(groupId);
							}

							// Close the Rings group.
							closeGroup(groupIds.pop());
							/* --------------------------- */

							// Close the MaterialBlock's group.
							closeGroup(groupIds.pop());

							// Add the block to materialBlocks (TreeSet).
							materialBlocks.add(block);
						}

						// Close the material blocks group.
						closeGroup(groupIds.pop());
						/* ---------------------------------- */

						// Initialize the pin.
						SFRPin pin = new SFRPin(pinName, cladding, fillGas,
								materialBlocks);

						// Get the pin's groupId.
						groupId = groupIds.peek();

						/* -- Read in the Pin's other attributes. -- */
						// Attributes inherited from SFRComponent.
						readSFRComponent(pin, groupId);

						// Pin-specific attributes.
						// none
						/* ----------------------------------------- */

						/* -- Read the pin's assembly locations. -- */
						// Add the pin to the assembly.
						assembly.addPin(pin);

						// Set the pin's locations in the assembly.
						for (int location : readLocationData(groupId)) {
							assembly.setPinLocation(pinName, location / size,
									location % size);
						}
						/* ---------------------------------------- */

						// Close the group for this pin.
						closeGroup(groupIds.pop());
					}

					// Close the group that holds the pins.
					closeGroup(groupIds.pop());
					/* --------------------------------- */

					/* --- Read the assembly's GridData. --- */
					groupId = groupIds.peek();

					List<SFRComponent> gridData = new ArrayList<SFRComponent>();
					for (int row = 0; row < assembly.getSize(); row++) {
						for (int column = 0; column < assembly.getSize(); column++) {
							gridData.add(assembly.getDataProviderByLocation(
									row, column));
						}
					}
					readGridData(gridData, groupId);
					/* -------------------------------------- */

					// Close the group for this assembly.
					closeGroup(groupIds.pop());
				}

				// Close the group for this assembly type.
				closeGroup(groupIds.pop());
			}
			/* -------------------------------------------- */

			/* ---- Read the reactor's Reflector Assemblies. ---- */
			// Open the group for this assembly type.
			groupId = groupIds.push(openGroup(groupIds.peek(),
					AssemblyType.Reflector.toString()));

			// Loop over the child groups in this assembly type's group.
			// These groups should have the assembly names.
			for (String assemblyName : getChildNames(groupId, H5O_TYPE_GROUP)) {

				// Open the group for this assembly.
				groupId = groupIds
						.push(openGroup(groupIds.peek(), assemblyName));

				// Read the name, rodType, and size of the assembly.
				size = (Integer) readAttribute(groupId, "size", H5T_NATIVE_INT,
						intBuffer);

				// Initialize the assembly.
				ReflectorAssembly assembly = new ReflectorAssembly(
						assemblyName, size);

				/* --- Read the assembly's attributes. --- */
				// Attributes inherited from SFRComponent.
				readSFRComponent(assembly, groupId);

				// Attributes inherited from SFRComposite.
				// none

				// Attributes inherited from SFRAssembly.
				// size has already been read.
				assembly.setDuctThickness((Double) readAttribute(groupId,
						"ductThickness", H5T_NATIVE_DOUBLE, doubleBuffer));

				// ReflectorAssembly-specific attributes.
				assembly.setRodPitch((Double) readAttribute(groupId,
						"rodPitch", H5T_NATIVE_DOUBLE, doubleBuffer));
				/* --------------------------------------- */

				/* --- Read the assembly's reactor locations. --- */
				// Add the assembly to the reactor.
				reactor.addAssembly(AssemblyType.Reflector, assembly);

				// Set the assembly's locations in the reactor.
				for (int location : readLocationData(groupId)) {
					reactor.setAssemblyLocation(AssemblyType.Reflector,
							assemblyName, location / reactor.getSize(),
							location % reactor.getSize());
				}
				/* ---------------------------------------------- */

				/* --- Read the assembly's rods. --- */
				// Open the group that holds the rods.
				groupId = groupIds.push(openGroup(groupId, "Rods"));

				// Loop over the child groups in this assembly's Rods group.
				// These groups should have the rod names.
				for (String rodName : getChildNames(groupId, H5O_TYPE_GROUP)) {

					// Open the group for this rod.
					groupId = groupIds
							.push(openGroup(groupIds.peek(), rodName));

					// Initialize the rod.
					SFRRod rod = new SFRRod(rodName);

					/* -- Read in the Rod's other attributes. -- */
					// Attributes inherited from SFRComponent.
					readSFRComponent(rod, groupId);

					// Rod-specific attributes.
					// none
					/* ----------------------------------------- */

					/* -- Read in the reflector. -- */
					groupId = openGroup(groupId, "reflector");
					rod.setReflector(readRing(groupId));
					closeGroup(groupId);
					/* --------------------------- */

					// Get the rod's groupId back.
					groupId = groupIds.peek();

					/* -- Read the rod's assembly locations. -- */
					// Add the rod to the assembly.
					assembly.addRod(rod);

					// Set the rod's locations in the assembly.
					for (int location : readLocationData(groupId)) {
						assembly.setRodLocation(rodName, location / size,
								location % size);
					}
					/* ---------------------------------------- */

					// Close the group for this rod.
					closeGroup(groupIds.pop());
				}

				// Close the group that holds the rods.
				closeGroup(groupIds.pop());
				/* --------------------------------- */

				/* --- Read the assembly's GridData. --- */
				groupId = groupIds.peek();

				List<SFRComponent> gridData = new ArrayList<SFRComponent>();
				for (int row = 0; row < assembly.getSize(); row++) {
					for (int column = 0; column < assembly.getSize(); column++) {
						gridData.add(assembly.getDataProviderByLocation(row,
								column));
					}
				}
				readGridData(gridData, groupId);
				/* -------------------------------------- */

				// Close the group for this assembly.
				closeGroup(groupIds.pop());
			}

			// Close the group for this assembly type.
			closeGroup(groupIds.pop());
			/* -------------------------------------------------- */

			// Close the reactor's group.
			closeGroup(groupIds.pop());

			// Close the H5file.
			status = H5.H5Fclose(fileId);
			if (status < 0) {
				throwException("Closing file \"" + path + "\"", status);
			}
		} catch (HDF5LibraryException e) {
			e.printStackTrace();
			System.err.println("SFReactorIOHandler error: " + e.getMessage());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.err.println("SFReactorIOHandler error: " + e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.err.println("SFReactorIOHandler error: " + e.getMessage());
		}

		// Return the loaded SFReactor.
		return reactor;
	}

	/**
	 * <p>
	 * Writes data from the input SFReactor into a HDF5 file.
	 * </p>
	 * 
	 * @param reactor
	 */
	public void writeHDF5(URI uri, SFReactor reactor) {

		// Check the parameters.
		if (uri == null || reactor == null) {
			return;
		}
		// Check the file associated with the URI. If it exists, delete it.
		File file = new File(uri);
		String path = file.getPath();
		if (file.exists()) {
		} else {
			// Make sure the directory containing this file exists! If we can't
			// create the directory, then quit!
			String directoryName = file.getParent();
			File directory = new File(directoryName);
			if (!directory.exists() && !directory.mkdirs()) {
				System.err.println("SFReactorIOHandler error: Directory \""
						+ directoryName + "\" could not be created.");
				return;

			}
		}

		// HDF5 constants. Writing out "HDF5Constants." every time is annoying.
		int H5P_DEFAULT = HDF5Constants.H5P_DEFAULT; // Default flag.
		int H5F_ACC_TRUNC = HDF5Constants.H5F_ACC_TRUNC; // Create, open,
															// truncate.
		int H5T_NATIVE_INT = HDF5Constants.H5T_NATIVE_INT; // int
		int H5T_NATIVE_DOUBLE = HDF5Constants.H5T_NATIVE_DOUBLE; // double

		// The status of the previous HDF5 operation. Generally, if it is
		// negative, there was some error.
		int status;

		// Other IDs for HDF5 components.
		int fileId, groupId;

		Stack<Integer> groupIds = new Stack<Integer>();

		// Create and open the h5 file.
		try {

			// Create the H5 file. This should also open it with RW-access.
			status = H5
					.H5Fcreate(path, H5F_ACC_TRUNC, H5P_DEFAULT, H5P_DEFAULT);
			if (status < 0) {
				throwException("Opening file \"" + path + "\"", status);
			}
			fileId = status;

			// Create the group for the reactor.
			groupId = groupIds.push(createGroup(fileId, "/SFReactor"));

			/* ---- Write the reactor's attributes. ---- */
			// Attributes inherited from SFRComponent.
			writeSFRComponent(reactor, groupId);

			// Attributes inherited from SFRComposite.
			// none

			// SFReactor-specific attributes.
			writeAttribute(groupId, "size", H5T_NATIVE_INT,
					new int[] { reactor.getSize() });
			writeAttribute(groupId, "latticePitch", H5T_NATIVE_DOUBLE,
					new double[] { reactor.getLatticePitch() });
			writeAttribute(groupId, "outerFlatToFlat", H5T_NATIVE_DOUBLE,
					new double[] { reactor.getOuterFlatToFlat() });
			/* ----------------------------------------- */

			/* ---- Write the reactor's Pin Assemblies. ---- */
			List<AssemblyType> pinAssemblyTypes = new ArrayList<AssemblyType>();
			pinAssemblyTypes.add(AssemblyType.Fuel);
			pinAssemblyTypes.add(AssemblyType.Control);
			pinAssemblyTypes.add(AssemblyType.Shield);
			pinAssemblyTypes.add(AssemblyType.Test);

			for (AssemblyType assemblyType : pinAssemblyTypes) {
				// Create a group for this assembly type.
				groupId = groupIds.push(createGroup(groupIds.peek(),
						assemblyType.toString()));

				for (String assemblyName : reactor
						.getAssemblyNames(assemblyType)) {

					// Get the assembly object from the reactor.
					PinAssembly assembly = (PinAssembly) reactor
							.getAssemblyByName(assemblyType, assemblyName);

					// Create a group for this assembly.
					groupId = groupIds.push(createGroup(groupIds.peek(),
							assemblyName));

					/* --- Write the assembly's attributes. --- */
					// Attributes inherited from SFRComponent.
					writeSFRComponent(assembly, groupId);

					// Attributes inherited from SFRComposite.
					// none

					// Attributes inherited from SFRAssembly.
					writeAttribute(groupId, "size", H5T_NATIVE_INT,
							new int[] { assembly.getSize() });
					writeAttribute(groupId, "ductThickness", H5T_NATIVE_DOUBLE,
							new double[] { assembly.getDuctThickness() });

					// PinAssembly-specific attributes.
					writeAttribute(groupId, "pinPitch", H5T_NATIVE_DOUBLE,
							new double[] { assembly.getPinPitch() });
					writeAttribute(groupId, "pinType", H5T_NATIVE_INT,
							new int[] { assembly.getPinType().getId() });
					writeAttribute(groupId, "innerDuctFlatToFlat",
							H5T_NATIVE_DOUBLE,
							new double[] { assembly.getInnerDuctFlatToFlat() });
					writeAttribute(groupId, "innerDuctThickness",
							H5T_NATIVE_INT,
							new double[] { assembly.getInnerDuctThickness() });
					/* ---------------------------------------- */

					/* --- Write the assembly's reactor locations. --- */
					writeLocationData(reactor.getAssemblyLocations(
							assemblyType, assemblyName), groupId);
					/* ----------------------------------------------- */

					// Create a group to hold the pins.
					groupId = groupIds
							.push(createGroup(groupIds.peek(), "Pins"));

					/* --- Write the assembly's rods/pins. --- */
					for (String pinName : assembly.getPinNames()) {

						// Get the pin object from the assembly.
						SFRPin pin = assembly.getPinByName(pinName);

						// Create a group for the pin.
						groupId = groupIds.push(createGroup(groupIds.peek(),
								pinName));

						/* -- Write the pin's attributes. -- */
						// Attributes inherited from SFRComponent.
						writeSFRComponent(pin, groupId);

						// Pin-specific attributes.
						// none
						/* --------------------------------- */

						/* -- Write the pin's physical properties. -- */
						// Material fillGas
						Material fillGas = pin.getFillGas();
						groupId = groupIds.push(createGroup(groupIds.peek(),
								"fillGas"));
						writeSFRComponent(fillGas, groupId);
						closeGroup(groupIds.pop());

						// Ring cladding
						Ring cladding = pin.getCladding();
						groupId = groupIds.push(createGroup(groupIds.peek(),
								"cladding"));
						writeRing(cladding, groupId);
						closeGroup(groupIds.pop());

						// TreeSet materialBlocks
						groupId = groupIds.push(createGroup(groupIds.peek(),
								"materialBlocks"));
						int i = 0;
						for (MaterialBlock block : pin.getMaterialBlocks()) {
							// Create a Group for the MaterialBlock.
							groupId = groupIds.push(createGroup(
									groupIds.peek(), Integer.toString(i++)));

							// Write the block's SFRComponent attributes.
							writeSFRComponent(block, groupId);

							// Write the block's other attributes.
							writeAttribute(groupId, "vertPosition",
									H5T_NATIVE_DOUBLE,
									new Double[] { block.getVertPosition() });

							// Create a group to contain the Rings.
							groupIds.push(createGroup(groupId, "Rings"));

							// Write the block's rings.
							int j = 0;
							for (Ring ring : block.getRings()) {
								groupId = groupIds
										.push(createGroup(groupIds.peek(),
												Integer.toString(j++)));
								writeRing(ring, groupId);
								closeGroup(groupIds.pop());
							}

							// Close the group that contains the Rings.
							closeGroup(groupIds.pop());

							// Close the group for the MaterialBlock.
							closeGroup(groupIds.pop());
						}
						closeGroup(groupIds.pop());
						/* ------------------------------------------ */

						// Get the group ID back (subgroups have been created).
						groupId = groupIds.peek();

						/* -- Write the pin's assembly locations. -- */
						writeLocationData(assembly.getPinLocations(pinName),
								groupId);
						/* ----------------------------------------- */

						// Close the group for the pin.
						closeGroup(groupIds.pop());
					}
					/* --------------------------------------- */

					// Close the group containing the pins.
					closeGroup(groupIds.pop());

					/* --- Write the assembly's GridData. --- */
					groupId = groupIds.peek();

					List<SFRComponent> gridData = new ArrayList<SFRComponent>();
					for (int row = 0; row < assembly.getSize(); row++) {
						for (int column = 0; column < assembly.getSize(); column++) {
							gridData.add(assembly.getDataProviderByLocation(
									row, column));
						}
					}
					writeGridData(gridData, groupId);
					/* -------------------------------------- */

					// Close this assembly group.
					closeGroup(groupIds.pop());
				}

				// Close the group for this assembly type.
				closeGroup(groupIds.pop());
			}
			/* --------------------------------------------- */

			/* ---- Write the reactor's Reflector Assemblies. ---- */
			// Create a group for this assembly type.
			groupId = groupIds.push(createGroup(groupIds.peek(),
					AssemblyType.Reflector.toString()));

			for (String assemblyName : reactor
					.getAssemblyNames(AssemblyType.Reflector)) {

				// Get the assembly object from the reactor.
				ReflectorAssembly assembly = (ReflectorAssembly) reactor
						.getAssemblyByName(AssemblyType.Reflector, assemblyName);

				// Create a group for this assembly.
				groupId = groupIds.push(createGroup(groupIds.peek(),
						assemblyName));

				/* --- Write the assembly's attributes. --- */
				// Attributes inherited from SFRComponent.
				writeSFRComponent(assembly, groupId);

				// Attributes inherited from SFRComposite.
				// none

				// Attributes inherited from SFRAssembly.
				writeAttribute(groupId, "size", H5T_NATIVE_INT,
						new int[] { assembly.getSize() });
				writeAttribute(groupId, "ductThickness", H5T_NATIVE_DOUBLE,
						new double[] { assembly.getDuctThickness() });

				// ReflectorAssembly-specific attributes.
				writeAttribute(groupId, "rodPitch", H5T_NATIVE_DOUBLE,
						new double[] { assembly.getRodPitch() });
				/* ---------------------------------------- */

				/* --- Write the assembly's reactor locations. --- */
				writeLocationData(reactor.getAssemblyLocations(
						AssemblyType.Reflector, assemblyName), groupId);
				/* ----------------------------------------------- */

				// Create a group to hold the pins.
				groupId = groupIds.push(createGroup(groupIds.peek(), "Rods"));

				/* --- Write the assembly's rods/pins. --- */
				for (String rodName : assembly.getRodNames()) {

					// Get the rod object from the assembly.
					SFRRod rod = assembly.getRodByName(rodName);

					// Create a group for the rod.
					groupId = groupIds.push(createGroup(groupIds.peek(),
							rodName));

					/* -- Write the rod's attributes. -- */
					// Attributes inherited from SFRComponent.
					writeSFRComponent(rod, groupId);

					// Rod-specific attributes.
					// none
					/* --------------------------------- */

					/* -- Write the rod's physical properties. -- */
					// Ring reflector.
					Ring reflector = rod.getReflector();
					groupId = groupIds.push(createGroup(groupIds.peek(),
							"reflector"));
					writeRing(reflector, groupId);
					closeGroup(groupIds.pop());
					/* ------------------------------------------ */

					// Get the group ID back (subgroups have been created).
					groupId = groupIds.peek();

					/* -- Write the rod's assembly locations. -- */
					writeLocationData(assembly.getRodLocations(rodName),
							groupId);
					/* ----------------------------------------- */

					// Close the group for the rod.
					closeGroup(groupIds.pop());
				}
				/* --------------------------------------- */

				// Close the group containing the rods.
				closeGroup(groupIds.pop());

				/* --- Write the assembly's GridData. --- */
				groupId = groupIds.peek();

				List<SFRComponent> gridData = new ArrayList<SFRComponent>();
				for (int row = 0; row < assembly.getSize(); row++) {
					for (int column = 0; column < assembly.getSize(); column++) {
						gridData.add(assembly.getDataProviderByLocation(row,
								column));
					}
				}
				writeGridData(gridData, groupId);
				/* -------------------------------------- */

				// Close this assembly group.
				closeGroup(groupIds.pop());
			}

			// Close the group for this assembly type.
			closeGroup(groupIds.pop());
			/* --------------------------------------------------- */

			// Close the reactor group.
			closeGroup(groupIds.pop());

			// Close the H5file.
			status = H5.H5Fclose(fileId);
			if (status < 0) {
				throwException("Closing file \"" + path + "\"", status);
			}
		} catch (HDF5LibraryException e) {
			e.printStackTrace();
			System.err.println("SFReactorIOHandler error: " + e.getMessage());
		} catch (HDF5Exception e) {
			e.printStackTrace();
			System.err.println("SFReactorIOHandler error: " + e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.err.println("SFReactorIOHandler error: " + e.getMessage());
		}

		return;
	}

	/**
	 * This utility method throws an HDF5LibraryException with a custom message.
	 * 
	 * @param message
	 *            The message to append to the exception.
	 * @param status
	 *            The integer flag that indicated a problem. This is usually a
	 *            negative number.
	 * @throws HDF5LibraryException
	 */
	private void throwException(String message, int status)
			throws HDF5LibraryException {
		throw new HDF5LibraryException("SFReactorIOHandler error: " + message
				+ ": " + Integer.toString(status));
	}

	/**
	 * Opens an HDF5 Group.
	 * 
	 * @param parentId
	 *            The ID of the parent's Group, which should be open itself.
	 * @param name
	 *            The name of the Group to open.
	 * @return The ID of the newly-opened Group.
	 * @throws HDF5LibraryException
	 * @throws NullPointerException
	 */
	private int openGroup(int parentId, String name)
			throws HDF5LibraryException, NullPointerException {
		int status = H5.H5Gopen(parentId, name, HDF5Constants.H5P_DEFAULT);
		if (status < 0) {
			throwException("Opening group \"" + name + "\"", status);
		}
		return status;
	}

	/**
	 * Creates and opens an HDF5 Group.
	 * 
	 * @param parentId
	 *            The ID of the parent's Group, which should be open itself.
	 * @param name
	 *            The name of the Group to open.
	 * @return The ID of the newly-opened Group.
	 * @throws HDF5LibraryException
	 * @throws NullPointerException
	 */
	private int createGroup(int parentId, String name)
			throws HDF5LibraryException, NullPointerException {
		int status = H5.H5Gcreate(parentId, name, HDF5Constants.H5P_DEFAULT,
				HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
		if (status < 0) {
			throwException("Creating group \"" + name + "\"", status);
		}
		return status;
	}

	/**
	 * Closes an HDF5 Group.
	 * 
	 * @param groupId
	 *            The ID of the Group to close.
	 * @throws HDF5LibraryException
	 */
	private void closeGroup(int groupId) throws HDF5LibraryException {
		int status = H5.H5Gclose(groupId);
		if (status < 0) {
			throwException("Closing group \"/SFReactor\"", status);
		}
		return;
	}

	/**
	 * Gets a List of all child Objects of an HDF5 Group with the specified ID
	 * and type.
	 * 
	 * @param parentId
	 *            The ID of the parent Group.
	 * @param objectType
	 *            The type of object we are looking for, e.g., H5O_TYPE_GROUP or
	 *            H5O_TYPE_DATASET.
	 * @return A List of names of all child objects that are HDF5 Groups.
	 * @throws HDF5LibraryException
	 */
	private List<String> getChildNames(int parentId, int objectType)
			throws HDF5LibraryException {

		// Constants used below.
		String parentGroup = ".";
		int indexType = HDF5Constants.H5_INDEX_NAME;
		int indexOrder = HDF5Constants.H5_ITER_INC;
		int lapl_id = HDF5Constants.H5P_DEFAULT;

		// Get the number of members in this group.
		int status = H5.H5Gn_members(parentId, ".");
		if (status < 0) {
			throwException("Getting number of children of group with ID "
					+ parentId + "", status);
		}
		int nMembers = status;

		// A List of group names within the parent group (which has ID groupId).
		List<String> groupNames = new ArrayList<String>(nMembers);

		// Loop over the possible indexes.
		for (int i = 0; i < nMembers; i++) {
			// Get the info for the object in this position.
			H5O_info_t info = H5.H5Oget_info_by_idx(parentId, parentGroup,
					indexType, indexOrder, i, lapl_id);

			// See if the object exists and is an HDF5 Group.
			if (info != null && info.type == objectType) {

				// Get the name and add it to the List if possible.
				String name = H5.H5Lget_name_by_idx(parentId, parentGroup,
						indexType, indexOrder, i, lapl_id);
				if (name != null) {
					groupNames.add(name);
				}
			}
		}

		return groupNames;
	}

	/**
	 * Writes an Attribute for an HDF5 Object, which is typically a Group. Array
	 * Attributes are not supported.
	 * 
	 * @param objectId
	 *            The ID for the Object, which should be open, that will get the
	 *            Attribute.
	 * @param name
	 *            The name of the Attribute.
	 * @param type
	 *            The HDF5 datatype of the Attribute. Currently supported are
	 *            H5T_NATIVE_INT and H5T_NATIVE_DOUBLE.
	 * @param buffer
	 *            The buffer used by HDF5 to write the Attribute to the file.
	 *            This should be an array of the appropriate type, e.g., an
	 *            int[1] for an H5T_NATIVE_INT.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeAttribute(int objectId, String name, int type,
			Object buffer) throws NullPointerException, HDF5Exception {
		int status;

		// Create the buffer that holds the data to write to the attribute.
		long[] bufferSize = new long[] { 1 };

		// Create the dataspace to hold the value.
		status = H5.H5Screate_simple(1, bufferSize, null);
		if (status < 0) {
			throwException("Creating dataspace for attribute \"" + name + "\"",
					status);
		}
		int dataspaceId = status;

		// Create the attribute for the dataspace.
		status = H5.H5Acreate(objectId, name, type, dataspaceId,
				HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
		if (status < 0) {
			throwException("Creating attribute \"" + name + "\"", status);
		}
		int attributeId = status;

		// Write the attribute.
		status = H5.H5Awrite(attributeId, type, buffer);
		if (status < 0) {
			throwException("Writing attribute \"" + name + "\"", status);
		}

		// Close the attribute.
		status = H5.H5Aclose(attributeId);
		if (status < 0) {
			throwException("Closing attribute \"" + name + "\"", status);
		}

		// Close the dataspace.
		status = H5.H5Sclose(dataspaceId);
		if (status < 0) {
			throwException("Closing dataspace for attribute \"" + name + "\"",
					status);
		}

		return;
	}

	/**
	 * Reads an Attribute for an HDF5 Object, which is typically a Group. Array
	 * Attributes are not supported.
	 * 
	 * @param objectId
	 *            The ID for the Object, which should be open, that has the
	 *            Attribute.
	 * @param name
	 *            The name of the Attribute.
	 * @param type
	 *            The HDF5 datatype of the Attribute. Currently supported are
	 *            H5T_NATIVE_INT and H5T_NATIVE_DOUBLE.
	 * @param buffer
	 *            The buffer used by HDF5 to read the Attribute from the file.
	 *            This should be an array of the appropriate type, e.g., an
	 *            Tnteger[1] for an H5T_NATIVE_INT.
	 * @return Returns the first element in the buffer (buffer[0]), which is the
	 *         value of the Attribute.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private Object readAttribute(int objectId, String name, int type,
			Object[] buffer) throws NullPointerException, HDF5Exception {
		int status;

		// Open the attribute.
		status = H5.H5Aopen(objectId, name, HDF5Constants.H5P_DEFAULT);
		if (status < 0) {
			throwException("Opening attribute \"" + name + "\"", status);
		}
		int attributeId = status;

		// Read the attribute.
		status = H5.H5Aread(attributeId, type, (Object) buffer);
		if (status < 0) {
			throwException("Reading attribute \"" + name + "\"", status);
		}
		// Close the attribute.
		status = H5.H5Aclose(attributeId);
		if (status < 0) {
			throwException("Closing attribute \"" + name + "\"", status);
		}
		return buffer[0];
	}

	/**
	 * Writes a String as an Attribute for an HDF5 Object, which is typically a
	 * Group. This requires a special method because the String must first be
	 * converted to a byte array.
	 * 
	 * @param objectId
	 *            The ID for the Object, which should be open, that will get the
	 *            Attribute.
	 * @param name
	 *            The name of the Attribute.
	 * @param value
	 *            The String value of the Attribute.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeStringAttribute(int objectId, String name, String value)
			throws NullPointerException, HDF5Exception {
		int status;
		// HDF5 requires null-terminated strings. Unfortunately, Java's
		// String.getBytes() method does not return a byte array that includes a
		// null character in the last position, so we have to create a new
		// buffer that includes all bytes from the string and a null (0) byte.

		// Method 1: Create byte array of correct size, then copy string bytes
		// to byte array.

		// Methd 2: Create array from a new string that has the null character.
		byte[] buffer = (value + "\0").getBytes();

		// We have 1 string, so set rank to 1 and the length of the 1st
		// dimension is 1.
		int rank = 1;
		long[] dims = new long[] { 1 };

		// Create the dataspace to hold the value.
		status = H5.H5Screate_simple(rank, dims, null);
		if (status < 0) {
			throwException("Creating dataspace for attribute \"" + name + "\"",
					status);
		}
		int dataspaceId = status;

		// Create the datatype for the attribute. Note that we include the size
		// of the string byte buffer that includes the null character.
		status = H5.H5Tcreate(HDF5Constants.H5T_STRING, buffer.length);
		if (status < 0) {
			throwException("Creating datatype for attribute \"" + name + "\"",
					status);
		}
		int datatypeId = status;

		// FIXME I don't think this is necessary, but it may be!
		// H5.H5Tset_strpad(datatypeId, HDF5Constants.H5T_STR_NULLTERM);

		// Create the attribute for the dataspace.
		status = H5.H5Acreate(objectId, name, datatypeId, dataspaceId,
				HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT);
		if (status < 0) {
			throwException("Creating attribute \"" + name + "\"", status);
		}
		int attributeId = status;

		// Write the attribute.
		status = H5.H5Awrite(attributeId, datatypeId, buffer);
		if (status < 0) {
			throwException("Writing attribute \"" + name + "\"", status);
		}

		// Close the attribute.
		status = H5.H5Aclose(attributeId);
		if (status < 0) {
			throwException("Closing attribute \"" + name + "\"", status);
		}

		// Close the datatype.
		status = H5.H5Tclose(datatypeId);
		if (status < 0) {
			throwException("Closing datatype for attribute \"" + name + "\"",
					status);
		}

		// Close the dataspace.
		status = H5.H5Sclose(dataspaceId);
		if (status < 0) {
			throwException("Closing dataspace for attribute \"" + name + "\"",
					status);
		}

		return;
	}

	/**
	 * Reads a String Attribute from an HDF5 Object, which is typically a Group.
	 * This requires a special method because the String must first be converted
	 * to a byte array.
	 * 
	 * @param objectId
	 *            The ID for the Object, which should be open, that has the
	 *            Attribute.
	 * @param name
	 *            The name of the Attribute.
	 * @return The value of the String stored in the Attribute.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private String readStringAttribute(int objectId, String name)
			throws NullPointerException, HDF5Exception {
		int status;

		// Open the attribute.
		status = H5.H5Aopen(objectId, name, HDF5Constants.H5P_DEFAULT);
		if (status < 0) {
			throwException("Opening attribute \"" + name + "\"", status);
		}
		int attributeId = status;

		// Get the datatype for the String (H5T_STRING with a size in bytes).
		status = H5.H5Aget_type(attributeId);
		if (status < 0) {
			throwException("Reading datatype for attribute \"" + name + "\"",
					status);
		}
		int datatypeId = status;

		// Get the size of the String from the datatype.
		status = H5.H5Tget_size(datatypeId);
		if (status <= 0) {
			throwException("Reading size of datatype for attribute \"" + name
					+ "\"", status);
		}
		int size = status;

		// Initialize the buffer.
		byte[] buffer = new byte[size];

		// Read the attribute.
		status = H5.H5Aread(attributeId, datatypeId, buffer);
		if (status < 0) {
			throwException("Reading attribute \"" + name + "\"", status);
		}

		// Close the attribute.
		status = H5.H5Aclose(attributeId);
		if (status < 0) {
			throwException("Closing attribute \"" + name + "\"", status);
		}

		// Convert the buffer into a String. The null character is only required
		// inside HDF5, so strip the null character.
		return new String(buffer, 0, size - 1);
	}

	/**
	 * This method writes an HDF5 Dataset containing the data that is stored in
	 * a buffer. All of the data's properties and the buffer must be allocated
	 * before calling this method.
	 * 
	 * @param objectId
	 *            The ID for the Object, which should be open, that will get the
	 *            Dataset.
	 * @param name
	 *            The name of the Dataset.
	 * @param rank
	 *            The number of dimensions in the data.
	 * @param dims
	 *            An array containing the sizes of each dimension in the data.
	 * @param type
	 *            The HDF5 datatype of the data in the Dataset, e.g.,
	 *            H5T_NATIVE_INT or H5T_NATIVE_DOUBLE. This may also be an ID
	 *            for an opened Datatype, e.g., an array of Strings (byte
	 *            arrays).
	 * @param buffer
	 *            The buffer that contains the data to write. This needs to be
	 *            an array, e.g., a double[n].
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeDataset(int objectId, String name, int rank, long[] dims,
			int type, Object buffer) throws NullPointerException, HDF5Exception {
		int status;

		// Create the dataspace.
		status = H5.H5Screate_simple(rank, dims, null);
		if (status < 0) {
			throwException("Creating dataspace for dataset \"" + name + "\"",
					status);
		}
		int dataspaceId = status;

		// Create the dataset.
		status = H5.H5Dcreate(objectId, name, type, dataspaceId,
				HDF5Constants.H5P_DEFAULT, HDF5Constants.H5P_DEFAULT,
				HDF5Constants.H5P_DEFAULT);
		if (status < 0) {
			throwException("Creating dataset \"" + name + "\"", status);
		}
		int datasetId = status;

		// Write the dataset.
		status = H5.H5Dwrite(datasetId, type, HDF5Constants.H5S_ALL,
				HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buffer);
		if (status < 0) {
			throwException("Writing dataset \"" + name + "\"", status);
		}

		// Close the dataset.
		status = H5.H5Dclose(datasetId);
		if (status < 0) {
			throwException("Closing dataset \"" + name + "\"", status);
		}

		// Close the dataspace.
		status = H5.H5Sclose(dataspaceId);
		if (status < 0) {
			throwException("Closing dataspace for dataset \"" + name + "\"",
					status);
		}

		return;
	}

	/**
	 * Writes all of the properties and data stored for an SFRComponent.
	 * 
	 * @param component
	 *            An initialized SFRComponent from which to write data.
	 * @param groupId
	 *            The ID of the SFRComponent's HDF5 Group, which should be open.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeSFRComponent(SFRComponent component, int groupId)
			throws NullPointerException, HDF5Exception {
		/* ---- Write the component's properties. ---- */
		writeStringAttribute(groupId, "name", component.getName());
		writeStringAttribute(groupId, "description", component.getDescription());
		writeAttribute(groupId, "id", HDF5Constants.H5T_NATIVE_INT,
				new int[] { component.getId() });
		writeStringAttribute(groupId, "sourceInfo", component.getSourceInfo());
		writeAttribute(groupId, "time", HDF5Constants.H5T_NATIVE_DOUBLE,
				new double[] { component.getCurrentTime() });
		writeStringAttribute(groupId, "timeUnits", component.getTimeUnits());
		/* ------------------------------------------- */

		/* ---- Write the data. ---- */
		// Create a Group to contain the feature data.
		int dataGroupId = createGroup(groupId, "Data");

		// Write the component's data.
		writeDataProvider(component, dataGroupId);

		// Close the feature data Group.
		closeGroup(dataGroupId);
		/* ------------------------- */

		return;
	}

	/**
	 * Reads all of the properties and data into an SFRComponent.
	 * 
	 * @param component
	 *            An initialized SFRComponent to read data into.
	 * @param groupId
	 *            The ID of the SFRComponent's HDF5 Group, which should be open.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void readSFRComponent(SFRComponent component, int groupId)
			throws NullPointerException, HDF5Exception {
		/* ---- Read in the component's properties. ---- */
		component.setName(readStringAttribute(groupId, "name"));
		component.setDescription(readStringAttribute(groupId, "description"));
		component.setId((Integer) readAttribute(groupId, "id",
				HDF5Constants.H5T_NATIVE_INT, new Integer[1]));
		component.setSourceInfo(readStringAttribute(groupId, "sourceInfo"));
		component.setTime((Double) readAttribute(groupId, "time",
				HDF5Constants.H5T_NATIVE_DOUBLE, new Double[1]));
		component.setTimeUnits(readStringAttribute(groupId, "timeUnits"));
		/* --------------------------------------------- */

		/* ---- Read the data. ---- */
		// Open the data Group.
		int dataGroupId = openGroup(groupId, "Data");

		// Read the component's data.
		readDataProvider(component, dataGroupId);

		// Close the data Group.
		closeGroup(dataGroupId);
		/* ------------------------ */

		return;
	}

	/**
	 * Writes the data for a GridDataManager from a pre-constructed List of
	 * IDataProviders (SFRComponents).
	 * 
	 * @param providers
	 *            A List of IDataProviders to write the data from.
	 * @param groupId
	 *            The ID of the parent HDF5 Group, which should be open.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeGridData(List<SFRComponent> providers, int groupId)
			throws NullPointerException, HDF5Exception {

		// Create a GridData Group.
		int gridDataGroupId = createGroup(groupId, "GridData");

		// Write all of the IDataProviders' data.
		for (int i = 0; i < providers.size(); i++) {

			// Skip non-existent providers (these are places where no pin/rod is
			// set).
			SFRComponent provider = providers.get(i);
			if (provider != null) {
				// Create a Group to hold the position's data.
				int providerGroupId = createGroup(gridDataGroupId,
						Integer.toString(i));

				// Write the provider.
				writeDataProvider(provider, providerGroupId);

				// Close the IDataProvider's group.
				closeGroup(providerGroupId);
			}
		}

		// Close the GridData Group.
		closeGroup(gridDataGroupId);

		return;
	}

	/**
	 * Writes all of the data from an IDataProvider (implemented by
	 * SFRComponent).
	 * 
	 * @param provider
	 *            The IDataProvider to write the data from.
	 * @param groupId
	 *            The ID of the parent HDF5 Group, which should be open.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeDataProvider(SFRComponent provider, int groupId)
			throws NullPointerException, HDF5Exception {

		// Get the times.
		ArrayList<Double> times = provider.getTimes();
		double currentTime = provider.getCurrentTime();

		// Use a Map to keep track of the different units used.
		double unitsCount = 0;
		Map<String, Double> unitsMap = new HashMap<String, Double>();
		List<String> unitsList = new ArrayList<String>(2);
		int unitsStringLength = 0;

		// Default properties defining the Dataset of doubles.
		int rank = 2;
		long[] dims = new long[] { 0, 6 };
		int type = HDF5Constants.H5T_NATIVE_DOUBLE;

		for (String feature : provider.getFeatureList()) {
			// Create a group to contain state point data.
			int featureGroupId = createGroup(groupId, feature);

			// Loop over the possible times.
			for (double time : times) {
				provider.setTime(time);

				// Get the data from the pin.
				ArrayList<IData> dataList = provider
						.getDataAtCurrentTime(feature);
				int length = dataList.size();

				// Construct the buffer of data for HDF5 writing.
				double[] dataBuffer = new double[dataList.size() * 6];
				int bufferIndex = 0;
				for (IData data : dataList) {
					dataBuffer[bufferIndex++] = data.getValue();
					dataBuffer[bufferIndex++] = data.getUncertainty();
					dataBuffer[bufferIndex++] = data.getPosition().get(0);
					dataBuffer[bufferIndex++] = data.getPosition().get(1);
					dataBuffer[bufferIndex++] = data.getPosition().get(2);

					// Get the units.
					String units = data.getUnits();

					// Get the units ID from the Map. If the units are not in
					// the Map, then we need to add it.
					if (unitsMap.containsKey(units)) {
						dataBuffer[bufferIndex++] = unitsMap.get(units);
					} else {
						unitsMap.put(units, unitsCount);
						unitsList.add(units);
						dataBuffer[bufferIndex++] = unitsCount++;
						if (units.length() > unitsStringLength) {
							unitsStringLength = units.length();
						}
					}
				}

				// Store the length (rows) of the array of data.
				dims[0] = length;

				// Write the state point data for this feature.
				writeDataset(featureGroupId, Double.toString(time), rank, dims,
						type, dataBuffer);
			}

			// Close the state point data group.
			closeGroup(featureGroupId);
		}

		// Write the units lookup Dataset if we have units.
		// FIXME - I'm not yet sure how to get variable length String Datasets
		// to work with the HDF5 Java library. Instead, we can compute the max
		// String length for the units above and write a Dataset of fixed-length
		// Strings.
		if (unitsCount > 0) {

			// This is a simple array. The index is the ID of the String.
			rank = 1;
			dims = new long[] { (long) unitsCount };

			// The byte arrays that will contain the unit strings will need to
			// be big enough to contain the longest unit string + a null
			// character.
			int nullTermLength = unitsStringLength + 1;

			// Create the Datatype for all elements in the Dataset.
			type = H5.H5Tcreate(HDF5Constants.H5T_STRING, nullTermLength);

			// The buffer for the bytes is necessary because HDF5 does not
			// support Java Strings, but C-style, null-terminated strings. Note
			// that the buffer allocation already initializes all the bytes to
			// zero (null).
			byte[][] unitsBuffer = new byte[(int) unitsCount][nullTermLength];
			for (int i = 0, j = 0; i < (int) unitsCount; i++, j = 0) {
				// We need to manually copy each string's bytes to the buffer.
				// Note that j is the index for the current string character.
				for (byte b : unitsList.get(i).getBytes()) {
					unitsBuffer[i][j++] = b;
				}
			}

			// Write the Units dataset.
			writeDataset(groupId, "Units", rank, dims, type, unitsBuffer);

			// Close the Datatype.
			H5.H5Tclose(type);
		}
		// Restore the current time.
		provider.setTime(currentTime);

		return;
	}

	/**
	 * Reads in the data for a GridDataManager into a pre-constructed List of
	 * IDataProviders (SFRComponents).
	 * 
	 * @param providers
	 *            A List of IDataProviders to read the data into.
	 * @param groupId
	 *            The ID of the parent HDF5 Group, which should be open.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void readGridData(List<SFRComponent> providers, int groupId)
			throws NullPointerException, HDF5Exception {

		// Open the GridData Group.
		int gridDataGroupId = openGroup(groupId, "GridData");

		// Write all of the IDataProviders' data.
		for (int i = 0; i < providers.size(); i++) {

			// Skip non-existent providers (these are places where no pin/rod is
			// set).
			SFRComponent provider = providers.get(i);
			if (provider != null) {
				// Open the Group holding the position's data.
				int providerGroupId = openGroup(gridDataGroupId,
						Integer.toString(i));

				// Read the provider.
				readDataProvider(provider, providerGroupId);

				// Close the IDataProvider's group.
				closeGroup(providerGroupId);
			}
		}

		// Close the GridData Group.
		closeGroup(gridDataGroupId);

		return;
	}

	/**
	 * Reads all of the data in for an IDataProvider (implemented by
	 * SFRComponent).
	 * 
	 * @param provider
	 *            The IDataProvider to read the data into.
	 * @param groupId
	 *            The ID of the parent HDF5 Group, which should be open.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void readDataProvider(SFRComponent provider, int groupId)
			throws NullPointerException, HDF5Exception {
		int status;

		// Commonly-used constants.
		int H5P_DEFAULT = HDF5Constants.H5P_DEFAULT;
		int H5S_ALL = HDF5Constants.H5S_ALL;

		/* ---- Read in the list of names of Units for the data. ---- */
		// An array to hold the units Strings.
		String[] units = null;

		// See if the Units Dataset exists. If it does, read it in.
		if (H5.H5Lexists(groupId, "Units", H5P_DEFAULT)) {

			// Open the Dataset.
			status = H5.H5Dopen(groupId, "Units", H5P_DEFAULT);
			if (status < 0) {
				throwException("Opening units Dataset for IDataProvider.",
						status);
			}
			int datasetId = status;

			// Open the Dataspace.
			status = H5.H5Dget_space(datasetId);
			if (status < 0) {
				throwException(
						"Opening dataspace for units dataset for IDataProvider.",
						status);
			}
			int dataspaceId = status;

			// Initialize the properties defining the Dataspace.
			int rank = 1;
			long[] dims = new long[1];

			// Get the size (rows) of the Dataset.
			status = H5.H5Sget_simple_extent_dims(dataspaceId, dims, null);
			if (status != rank) {
				throwException(
						"Getting size of units dataset for IDataProvider.",
						status);
			}

			// Open the Datatype.
			status = H5.H5Dget_type(datasetId);
			if (status < 0) {
				throwException(
						"Opening datatype for units dataset for IDataProvider.",
						status);
			}
			int typeId = status;

			status = H5.H5Tget_size(typeId);
			if (status < 0) {
				throwException(
						"Getting size of datatype for units dataset for IDataProvider.",
						status);
			}
			int stringLength = status;

			int rows = (int) dims[0];
			// Initialize the buffer.
			byte[][] buffer = new byte[rows][stringLength];

			// Read in the Dataset.
			status = H5.H5Dread(datasetId, typeId, H5S_ALL, H5S_ALL,
					H5P_DEFAULT, buffer);
			if (status < 0) {
				throwException("Reading units Dataset for IDataProvider.",
						status);
			}

			// Close the Datatype.
			status = H5.H5Tclose(typeId);
			if (status < 0) {
				throwException(
						"Closing datatype for units dataset for IDataProvider.",
						status);
			}

			// Close the dataspace.
			status = H5.H5Sclose(dataspaceId);
			if (status < 0) {
				throwException(
						"Closing dataspace for units dataset for IDataProvider.",
						status);
			}

			// Close the Dataset.
			status = H5.H5Dclose(datasetId);
			if (status < 0) {
				throwException("Closing units Dataset for IDataProvider.",
						status);
			}

			// Fill out the array of units Strings. This converts each byte
			// array into a String.
			units = new String[rows];
			for (int i = 0; i < rows; i++) {
				// Determine how long the string actually is by finding the
				// first null terminating character.
				int j = 0;
				for (j = 0; j < stringLength; j++) {
					if (buffer[i][j] == 0) {
						break;
					}
				}
				// Create the string from the buffer, excluding the null
				// character.
				units[i] = new String(buffer[i], 0, j);
			}
		}
		/* ---------------------------------------------------------- */

		/* ---- Read in the data for each feature. ---- */
		// Loop over the groups in the Data group. These groups are named after
		// the feature whose data they contain.
		for (String feature : getChildNames(groupId,
				HDF5Constants.H5O_TYPE_GROUP)) {

			// Open the feature group.
			int featureGroupId = openGroup(groupId, feature);

			// Get the list of times.
			for (String timeString : getChildNames(featureGroupId,
					HDF5Constants.H5O_TYPE_DATASET)) {

				// Open the dataset.
				status = H5.H5Dopen(featureGroupId, timeString, H5P_DEFAULT);
				if (status < 0) {
					throwException("Opening dataset for IDataProvider.", status);
				}
				int datasetId = status;

				// Open the dataspace.
				status = H5.H5Dget_space(datasetId);
				if (status < 0) {
					throwException("Opening dataspace for IDataProvider.",
							status);
				}
				int dataspaceId = status;

				// Initialize the properties defining the dataspace.
				int rank = 2;
				long[] dims = new long[2];
				int type = HDF5Constants.H5T_NATIVE_DOUBLE;

				// Get the size of the data array.
				status = H5.H5Sget_simple_extent_dims(dataspaceId, dims, null);
				if (status != rank || dims[1] != 6) {
					throwException(
							"Reading dataspace dimensions for IDataProvider.",
							status);
				}

				// Initialize the buffer.
				int size = (int) dims[0];
				double[][] buffer = new double[size][6];

				// Read in the data into the buffer.
				status = H5.H5Dread(datasetId, type, H5S_ALL, H5S_ALL,
						H5P_DEFAULT, buffer);
				if (status < 0) {
					throwException("Reading dataset for IDataProvider.", status);
				}
				// Close the dataspace.
				status = H5.H5Sclose(dataspaceId);
				if (status < 0) {
					throwException("Closing dataspace for IDataProvider.",
							status);
				}
				// Close the dataset.
				status = H5.H5Dclose(datasetId);
				if (status < 0) {
					throwException("Closing dataset for IDataProvider.", status);
				}
				double time = Double.parseDouble(timeString);

				// Initialize the IData for the timestep.
				for (double[] dataBlock : buffer) {
					// Initialize an iData.
					SFRData data = new SFRData(feature);

					// Set its data.
					data.setValue(dataBlock[0]);
					data.setUncertainty(dataBlock[1]);
					ArrayList<Double> position = new ArrayList<Double>(3);
					position.add(dataBlock[2]);
					position.add(dataBlock[3]);
					position.add(dataBlock[4]);
					data.setPosition(position);

					// Set the units from the pre-constructed array of data.
					data.setUnits(units[(int) dataBlock[5]]);

					// Add it to the component.
					provider.addData(data, time);
				}
			}
			// Close the feature group.
			closeGroup(featureGroupId);
		}
		/* -------------------------------------------- */

		return;
	}

	/**
	 * Writes a List of locations, stored as Integers, as a Dataset.
	 * 
	 * @param locations
	 *            The List of location indexes to write.
	 * @param groupId
	 *            The ID of the HDF5 Group, which should be open, in which to
	 *            store the location data.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeLocationData(ArrayList<Integer> locations, int groupId)
			throws NullPointerException, HDF5Exception {
		int length = locations.size();

		// Set up the buffer for the H5 write operation.
		int[] buffer = new int[length];
		for (int i = 0; i < length; i++) {
			buffer[i] = locations.get(i);
		}
		// These properties define the dataspace.
		int rank = 1;
		long[] dims = new long[] { length };
		int type = HDF5Constants.H5T_NATIVE_INT;

		// Write the dataset.
		writeDataset(groupId, "locations", rank, dims, type, buffer);

		return;
	}

	/**
	 * Reads a List of locations, stored as Integers, from a Dataset.
	 * 
	 * @param groupId
	 *            The ID of the HDF5 Group, which should be open, that holds the
	 *            location Dataset.
	 * @return Returns an Array (int[]) containing the location data straight
	 *         from the Dataset.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private int[] readLocationData(int groupId) throws NullPointerException,
			HDF5Exception {
		int status;

		// Open the dataset.
		status = H5.H5Dopen(groupId, "locations", HDF5Constants.H5P_DEFAULT);
		if (status < 0) {
			throwException(
					"Opening dataset for location data in group with id "
							+ groupId, status);
		}
		int datasetId = status;

		// Open the dataspace.
		status = H5.H5Dget_space(datasetId);
		if (status < 0) {
			throwException(
					"Opening dataspace for location data in group with id "
							+ groupId, status);
		}
		int dataspaceId = status;

		// Initialize the properties defining the dataspace.
		int rank = 1;
		long[] dims = new long[1];
		int type = HDF5Constants.H5T_NATIVE_INT;

		// Get the size of the location data array.
		status = H5.H5Sget_simple_extent_dims(dataspaceId, dims, null);
		if (status != rank) {
			throwException(
					"Reading dataspace dimensions for location data in group with id "
							+ groupId, status);
		}

		// Initialize the buffer.
		int size = (int) dims[0];
		int[] buffer = new int[size];

		// Read in the location data into the buffer.
		status = H5.H5Dread(datasetId, type, HDF5Constants.H5S_ALL,
				HDF5Constants.H5S_ALL, HDF5Constants.H5P_DEFAULT, buffer);
		if (status < 0) {
			throwException(
					"Reading dataset for location data in group with id "
							+ groupId, status);
		}

		// Close the dataspace.
		status = H5.H5Sclose(dataspaceId);
		if (status < 0) {
			throwException(
					"Closing dataspace for location data in group with id "
							+ groupId, status);
		}

		// Close the dataset.
		status = H5.H5Dclose(datasetId);
		if (status < 0) {
			throwException(
					"Closing dataset for location data in group with id "
							+ groupId, status);
		}

		// Return the locations.
		return buffer;
	}

	/**
	 * Writes an SFR Ring to an HDF5 Group. This includes its properties and the
	 * Material stored in the Ring.
	 * 
	 * @param ring
	 *            The Ring to write to a file.
	 * @param ringGroupId
	 *            The ID of the Ring's HDF5 Group, which should be open.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private void writeRing(Ring ring, int ringGroupId)
			throws NullPointerException, HDF5Exception {

		// Write the basic SFRComponent attributes.
		writeSFRComponent(ring, ringGroupId);

		// Write ring-specific attributes.
		writeAttribute(ringGroupId, "height", HDF5Constants.H5T_NATIVE_DOUBLE,
				new double[] { ring.getHeight() });
		writeAttribute(ringGroupId, "innerRadius",
				HDF5Constants.H5T_NATIVE_DOUBLE,
				new double[] { ring.getInnerRadius() });
		writeAttribute(ringGroupId, "outerRadius",
				HDF5Constants.H5T_NATIVE_DOUBLE,
				new double[] { ring.getOuterRadius() });

		// Write the material attributes.
		Material material = ring.getMaterial();

		// Create a group, write attributes to it, and close it.
		int materialGroupId = createGroup(ringGroupId, "material");
		writeSFRComponent(material, materialGroupId);
		closeGroup(materialGroupId);

		return;
	}

	/**
	 * Reads an SFR Ring from an HDF5 Group. This includes its properties and
	 * Material stored in the Ring.
	 * 
	 * @param ringGroupId
	 *            The ID of the Ring's HDF5 Group, which should be open
	 * @return A new Ring object with the properties read in from the file.
	 * @throws NullPointerException
	 * @throws HDF5Exception
	 */
	private Ring readRing(int ringGroupId) throws NullPointerException,
			HDF5Exception {
		Ring ring = new Ring();

		// Read the basic SFRComponent attributes.
		readSFRComponent(ring, ringGroupId);

		// Read ring-specific attributes.
		Double[] buffer = new Double[1];
		ring.setHeight((Double) readAttribute(ringGroupId, "height",
				HDF5Constants.H5T_NATIVE_DOUBLE, buffer));
		ring.setInnerRadius((Double) readAttribute(ringGroupId, "innerRadius",
				HDF5Constants.H5T_NATIVE_DOUBLE, buffer));
		ring.setOuterRadius((Double) readAttribute(ringGroupId, "outerRadius",
				HDF5Constants.H5T_NATIVE_DOUBLE, buffer));

		// Read the material.
		Material material = new Material();

		// Read the material's attributes.
		int materialGroupId = openGroup(ringGroupId, "material");
		readSFRComponent(material, materialGroupId);
		closeGroup(materialGroupId);

		// Update the ring's material.
		ring.setMaterial(material);

		return ring;
	}

}