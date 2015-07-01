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
package org.eclipse.ice.reactorAnalyzer;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.hdf5lib.exceptions.HDF5LibraryException;
import ncsa.hdf.hdf5lib.structs.H5O_info_t;

import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.reactor.LWRComponentReader;
import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.ice.reactor.sfr.base.SFReactorIOHandler;
import org.eclipse.ice.reactor.sfr.core.SFReactor;

public class ReactorReaderFactory {

	// FIXME - In lieu of registering OSGi services, I'm simply calling the
	// specific reactor classes and methods directly.

	private interface IReactorFactory {
		public IReactorComponent read(URI uri);

		public void copy(IReactorComponent src, IReactorComponent dst);
	}

	private Map<Class, IReactorFactory> reactorFactoryMap;

	public ReactorReaderFactory() {

		reactorFactoryMap = new HashMap<Class, IReactorFactory>();
		reactorFactoryMap.put(PressurizedWaterReactor.class,
				new IReactorFactory() {
					@Override
					public IReactorComponent read(URI uri) {
						return (PressurizedWaterReactor) new LWRComponentReader()
								.read(uri);
					}

					@Override
					public void copy(IReactorComponent src,
							IReactorComponent dst) {
						((PressurizedWaterReactor) dst)
								.copy((PressurizedWaterReactor) src);
						return;
					}
				});
		reactorFactoryMap.put(SFReactor.class, new IReactorFactory() {
			@Override
			public IReactorComponent read(URI uri) {
				return new SFReactorIOHandler().readHDF5(uri);
			}

			@Override
			public void copy(IReactorComponent src, IReactorComponent dst) {
				((SFReactor) dst).copy((SFReactor) src);
				return;
			}
		});

		return;
	}

	/**
	 * This method reads the data from a given file into an implementation of an
	 * IReactorComponent. It will return null if the file could not be read.
	 * 
	 * @param source
	 *            The URI of the source data file.
	 * @return An instance of an implementation of IReactorComponent, or
	 *         <code>null</code> if the file could not be read.
	 */
	public IReactorComponent readReactor(URI source) {

		// Declare a component to return.
		IReactorComponent component = null;

		// Open up the file to read what type of reactor is in the file.
		if (source == null) {
			return component;
		}

		// Check the file associated with the URI. We need to be able to read
		// from it.
		File file = new File(source);
		String path = file.getPath();
		if (!file.canRead()) {
			System.err.println("ReactorReaderFactory error: File \"" + path
					+ "\" cannot be read.");
			return component;
		}

		// The name of the reactor group (the first group in the .h5 file).
		Class type = null;

		try {
			int H5P_DEFAULT = HDF5Constants.H5P_DEFAULT;

			// Open the H5 file with read-only access.
			int fileId = H5.H5Fopen(path, HDF5Constants.H5F_ACC_RDONLY,
					H5P_DEFAULT);
			if (fileId < 0) {
				throw new HDF5LibraryException("Error opening .h5 file \""
						+ path + "\".");
			}
			// Get the name of the first group. That should determine the type
			// of reactor.

			// Constants used below.
			int indexType = HDF5Constants.H5_INDEX_NAME;
			int indexOrder = HDF5Constants.H5_ITER_INC;

			// Get the info for the first group in the file.
			H5O_info_t info = H5.H5Oget_info_by_idx(fileId, "/", indexType,
					indexOrder, 0, H5P_DEFAULT);

			// See if the object exists and is an HDF5 Group.
			if (info == null || info.type != HDF5Constants.H5O_TYPE_GROUP) {
				throw new HDF5LibraryException(
						"Expected a reactor Group in file \"" + path + "\".");
			}
			// Get the name and add it to the List if possible.
			String name = H5.H5Lget_name_by_idx(fileId, "/", indexType,
					indexOrder, 0, H5P_DEFAULT);
			if (name == null) {
				throw new HDF5LibraryException(
						"Invalid name of first Group in file \"" + path + "\".");
			}
			// For the moment, SFReactor files have the name of the first Group
			// set to SFReactor. PWReactor files have an HDF5LWRTag Attribute.
			if ("SFReactor".equals(name)) {
				type = SFReactor.class;
			} else {
				/* ---- Try to open the HDF5LWRTag Attribute. ---- */
				if (H5.H5Aexists_by_name(fileId, "/" + name, "HDF5LWRTag",
						H5P_DEFAULT)) {

					// Open the Attribute.
					int attributeId = H5.H5Aopen_by_name(fileId, "/" + name,
							"HDF5LWRTag", H5P_DEFAULT, H5P_DEFAULT);
					if (attributeId < 0) {
						throw new HDF5LibraryException(
								"Could not open HDF5LWRTag for first Group in file \""
										+ path + "\".");
					}
					// Get the Datatype for the Attribute.
					int datatypeId = H5.H5Aget_type(attributeId);
					if (datatypeId < 0) {
						throw new HDF5LibraryException(
								"Could not get Datatype of HDF5LWRTag for first Group in file \""
										+ path + "\".");
					}
					// Get the size of the String from the datatype.
					int size = H5.H5Tget_size(datatypeId);
					if (size <= 0) {
						throw new HDF5LibraryException(
								"Could not get size of Datatype of HDF5LWRTag for first Group in file \""
										+ path + "\".");
					}
					// Initialize the buffer.
					byte[] buffer = new byte[size];

					// Read the attribute.
					if (H5.H5Aread(attributeId, datatypeId, buffer) < 0) {
						throw new HDF5LibraryException(
								"Could not read HDF5LWRTag for first Group in file \""
										+ path + "\".");
					}
					// Close the Datatype.
					if (H5.H5Tclose(datatypeId) < 0) {
						throw new HDF5LibraryException(
								"Could not close Datatype of HDF5LWRTag for first Group in file \""
										+ path + "\".");
					}
					// Close the Attribute.
					if (H5.H5Aclose(attributeId) < 0) {
						throw new HDF5LibraryException(
								"Could not close HDF5LWRTag for first Group in file \""
										+ path + "\".");
					}
					// Convert the buffer to a String and see if it matches the
					// expected value for a PWReactor.
					if ("PWReactor".equals(new String(buffer))) {
						type = PressurizedWaterReactor.class;
					}
					/* ----------------------------------------------- */
				}
			}
			// Close the H5file.
			if (H5.H5Fclose(fileId) < 0) {
				throw new HDF5LibraryException("Error closing .h5 file \""
						+ path + "\".");
			}
		} catch (HDF5LibraryException e) {
			e.printStackTrace();
			System.err.println("ReactorReaderFactory error: " + e.getMessage());
		} catch (NullPointerException e) {
			e.printStackTrace();
			System.err.println("ReactorReaderFactory error: " + e.getMessage());
		}

		// Look up the factory for the type and, if possible, use the factory to
		// read in the component.
		IReactorFactory factory = reactorFactoryMap.get(type);
		if (factory != null) {
			component = factory.read(source);
		}
		return component;
	}

	/**
	 * Copies the information from a source IReactorComponent instance to
	 * another IReactorComponent.
	 * 
	 * @param source
	 *            The source IReactorComponent to be copied from.
	 * @param destination
	 *            The destination IReactorComponent to be copied to.
	 */
	public boolean copyReactor(IReactorComponent source,
			IReactorComponent destination) {

		boolean copied = false;

		// Check the parameters for null and make sure their class types are the
		// same.
		if (source != null && destination != null
				&& source.getClass().equals(destination.getClass())) {

			// Get the factory that knows how to copy the IReactorComponent
			// implementation.
			IReactorFactory factory = reactorFactoryMap.get(destination
					.getClass());

			// If the factory is valid, then try to copy the source data into
			// the destination.
			if (factory != null) {
				factory.copy(source, destination);
				copied = true;
			}
		}

		return copied;
	}
}