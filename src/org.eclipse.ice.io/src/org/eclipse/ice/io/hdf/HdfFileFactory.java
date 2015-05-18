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
package org.eclipse.ice.io.hdf;

import java.io.File;
import java.net.URI;

import ncsa.hdf.hdf5lib.exceptions.HDF5Exception;
import ncsa.hdf.object.FileFormat;
import ncsa.hdf.object.h5.H5File;

/**
 * <p>
 * The HDFFileFactory class contains static methods used to create, open, and
 * close HDF5 files.
 * </p>
 * 
 * @author els
 */
public class HdfFileFactory {
	/**
	 * <p>
	 * Creates, opens, and returns an H5File from the provided uri. If the uri
	 * is null, the null is returned. If the FID of resulting h5File is -1, then
	 * null is returned. If any Exception is thrown, then null is returned.
	 * </p>
	 * 
	 * @param uri
	 *            <p>
	 *            The URI of the H5File to create and open.
	 *            </p>
	 * @return <p>
	 *         An H5File that has been created and opened for read/write
	 *         operations.
	 *         </p>
	 */
	public static H5File createH5File(URI uri) {

		// If parameter is null, return null
		if (uri == null) {
			return null;
		}

		// Retrieve an instance of the HDF5 format
		FileFormat fileFormat = FileFormat
				.getFileFormat(FileFormat.FILE_TYPE_HDF5);

		try {

			// Create a file from the uri
			File file = new File(uri);

			// Create an H5 file. If it exists already, then delete it.
			H5File h5File = (H5File) fileFormat.createFile(file.getPath(),
					FileFormat.FILE_CREATE_DELETE);

			// We must get an instance to the file and set the format to write
			h5File = (H5File) fileFormat.createInstance(file.getPath(),
					FileFormat.WRITE);

			// Open the file
			h5File.open();

			// If the file id of the h5File is -1, return null
			if (h5File.getFID() == -1) {

				return null;

			}

			// Return the file
			return h5File;

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			// Return null
			return null;

		}

	}

	/**
	 * <p>
	 * Closes an h5File.
	 * </p>
	 * 
	 * @param h5File
	 *            <p>
	 *            The H5File to close.
	 *            </p>
	 */
	public static void closeH5File(H5File h5File) {

		try {

			// If the file is not null
			if (h5File != null) {

				// Close the file
				h5File.close();

			}

		} catch (HDF5Exception e) {

			// Print the stack trace
			e.printStackTrace();
		}

	}

	/**
	 * <p>
	 * Opens and returns an h5File from the provided URI. If the uri is null,
	 * the null is returned. If the File for uri does not exist, then null is
	 * returned. If the resulting h5File is null or does not exist, then null is
	 * returned. If any Exception is thrown, then null is returned.
	 * </p>
	 * 
	 * @param uri
	 *            <p>
	 *            The URI of the H5File to open.
	 *            </p>
	 * @return <p>
	 *         An H5File that has been opened for read/write operations.
	 *         </p>
	 */
	public static H5File openH5File(URI uri) {

		// If parameter is null or does not exist, return null
		if (uri == null || !(new File(uri).exists())) {
			return null;
		}

		// Retrieve an instance of the HDF5 format
		FileFormat fileFormat = FileFormat
				.getFileFormat(FileFormat.FILE_TYPE_HDF5);

		try {

			// Create a file from the uri
			File file = new File(uri);

			// Open a h5File
			H5File h5File = (H5File) fileFormat.createInstance(file.getPath(),
					FileFormat.WRITE);

			// If the h5File does not exist, then return null
			if (h5File == null || !h5File.exists()) {
				return null;
			}

			// Open the file
			h5File.open();

			// If the file id of the h5File is -1, return null
			if (h5File.getFID() == -1) {

				return null;

			}

			// Return the file
			return h5File;

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			// Return null
			return null;

		}

	}
}