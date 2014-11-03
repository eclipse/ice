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
 * <!-- begin-UML-doc -->
 * <p>
 * The HDFFileFactory class contains static methods used to create, open, and
 * close HDF5 files.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author els
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class HdfFileFactory {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Creates, opens, and returns an H5File from the provided uri. If the uri
	 * is null, the null is returned. If the FID of resulting h5File is -1, then
	 * null is returned. If any Exception is thrown, then null is returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param uri
	 *            <p>
	 *            The URI of the H5File to create and open.
	 *            </p>
	 * @return <p>
	 *         An H5File that has been created and opened for read/write
	 *         operations.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static H5File createH5File(URI uri) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Closes an h5File.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 *            <p>
	 *            The H5File to close.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static void closeH5File(H5File h5File) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Opens and returns an h5File from the provided URI. If the uri is null,
	 * the null is returned. If the File for uri does not exist, then null is
	 * returned. If the resulting h5File is null or does not exist, then null is
	 * returned. If any Exception is thrown, then null is returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param uri
	 *            <p>
	 *            The URI of the H5File to open.
	 *            </p>
	 * @return <p>
	 *         An H5File that has been opened for read/write operations.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static H5File openH5File(URI uri) {
		// begin-user-code

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

		// end-user-code
	}
}