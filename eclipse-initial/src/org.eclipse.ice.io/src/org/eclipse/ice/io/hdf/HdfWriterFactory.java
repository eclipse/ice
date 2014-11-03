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

import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;
import ncsa.hdf.object.h5.H5Datatype;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The HdfWriterFactory class contains static methods used to write elements to
 * an HDF5 file.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author els
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class HdfWriterFactory {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Creates and returns a child H5Group called name for parentH5Group using
	 * the h5File. If h5File is null or can not be opened, then null is
	 * returned. If name is null or is an empty String, then null is returned.
	 * If parentH5Group is null, then null is returned. If an exception is
	 * thrown, then null is returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 *            <p>
	 *            An H5File.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the new H5Group.
	 *            </p>
	 * @param parentH5Group
	 *            <p>
	 *            The parent H5Group of the new H5Group.
	 *            </p>
	 * @return <p>
	 *         An H5Group called name which is a child of parentH5Group.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static H5Group createH5Group(H5File h5File, String name,
			H5Group parentH5Group) {
		// begin-user-code

		// If the file or the name is null, or if the name is the empty string,
		// return
		if (h5File == null || name == null || "".equals(name.trim())
				|| parentH5Group == null) {
			return null;
		}

		try {

			// Declare a file id
			int fileIdentifier = h5File.getFID();

			// Try to open the file and get a file id
			if (fileIdentifier == -1) {

				fileIdentifier = h5File.open();

			}

			// Check the file identifier
			if (fileIdentifier == -1) {
				return null;
			}

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			return null;
		}

		try {

			// Create the group
			H5Group h5Group = (H5Group) h5File.createGroup(name, parentH5Group);

			// Return it
			return h5Group;

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
	 * Creates and returns a 64-bit floating point H5Datatype from h5File. If
	 * h5File is null or can not be opened, then null is returned. If an
	 * exception is thrown, then null is returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 *            <p>
	 *            An H5File.
	 *            </p>
	 * @return <p>
	 *         A 64-bit floating point H5Datatype.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static H5Datatype createFloatH5Datatype(H5File h5File) {
		// begin-user-code

		try {

			// Declare a file id
			int fileIdentifier = h5File.getFID();

			// Try to open the file and get a file id
			if (fileIdentifier == -1) {

				fileIdentifier = h5File.open();

			}

			// Check the file identifier
			if (fileIdentifier == -1) {
				return null;
			}

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			return null;
		}

		try {

			// Create the float datatype
			H5Datatype h5Datatype = (H5Datatype) h5File.createDatatype(
					Datatype.CLASS_FLOAT, 8, Datatype.NATIVE, Datatype.NATIVE);

			// Return it
			return h5Datatype;

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
	 * Creates and returns an integer H5Datatype from h5File. If h5File is null
	 * or can not be opened, then null is returned. If an exception is thrown,
	 * then null is returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 *            <p>
	 *            An H5File.
	 *            </p>
	 * @return <p>
	 *         A 32-bit integer H5Datatype.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static H5Datatype createIntegerH5Datatype(H5File h5File) {
		// begin-user-code

		try {

			// Declare a file id
			int fileIdentifier = h5File.getFID();

			// Try to open the file and get a file id
			if (fileIdentifier == -1) {

				fileIdentifier = h5File.open();

			}

			// Check the file identifier
			if (fileIdentifier == -1) {
				return null;
			}

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			return null;
		}

		try {

			// Create the integer datatype
			H5Datatype h5Datatype = (H5Datatype) h5File
					.createDatatype(Datatype.CLASS_INTEGER, 4, Datatype.NATIVE,
							Datatype.NATIVE);

			// Return it
			return h5Datatype;

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
	 * Writes an Attribute to h5Group called name with a double value using
	 * h5File. If h5File is null or can not be opened, then false is returned.
	 * If name is null or is an empty String, then false is returned. If h5Group
	 * is null, then false is returned. If an exception is thrown, then false is
	 * returned. Otherwise, true is returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 *            <p>
	 *            An H5File.
	 *            </p>
	 * @param h5Group
	 *            <p>
	 *            The H5Group to write the attribute to.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the Attribute.
	 *            </p>
	 * @param value
	 *            <p>
	 *            The value of the Attribute.
	 *            </p>
	 * @return <p>
	 *         If h5File is null or can not be opened, then false is returned.
	 *         If name is null or is an empty String, then false is returned. If
	 *         h5Group is null, then false is returned. If an exception is
	 *         thrown, then false is returned. Otherwise, true is returned.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static boolean writeDoubleAttribute(H5File h5File, H5Group h5Group,
			String name, double value) {
		// begin-user-code

		// If the file, h5Group, or the name is null, or if the name is the
		// empty string, return false
		if (h5File == null || name == null || "".equals(name.trim())
				|| h5Group == null) {
			return false;
		}

		try {

			// Declare a file id
			int fileIdentifier = h5File.getFID();

			// Try to open the file and get a file id
			if (fileIdentifier == -1) {

				fileIdentifier = h5File.open();

			}

			// Check the file identifier
			if (fileIdentifier == -1) {
				return false;
			}

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			return false;
		}

		// Get a integer datatype
		H5Datatype datatype = HdfWriterFactory.createFloatH5Datatype(h5File);

		// 1D of size 1
		long[] dims = { 1 };

		// Create the value
		double[] values = { value };

		// Create an attribute object
		Attribute attribute = new Attribute(name, datatype, dims, values);

		// Write the attribute
		try {

			h5Group.writeMetadata(attribute);

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			// return false
			return false;
		}

		return true;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Writes an Attribute to h5Group called name with an integer value using
	 * h5File. If h5File is null or can not be opened, then false is returned.
	 * If name is null or is an empty String, then false is returned. If h5Group
	 * is null, then false is returned. If an exception is thrown, then false is
	 * returned. Otherwise, true is returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 *            <p>
	 *            An H5File.
	 *            </p>
	 * @param h5Group
	 *            <p>
	 *            The H5Group to write the attribute to.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the Attribute.
	 *            </p>
	 * @param value
	 *            <p>
	 *            The value of the Attribute.
	 *            </p>
	 * @return <p>
	 *         If h5File is null or can not be opened, then false is returned.
	 *         If name is null or is an empty String, then false is returned. If
	 *         h5Group is null, then false is returned. If an exception is
	 *         thrown, then false is returned. Otherwise, true is returned.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static boolean writeIntegerAttribute(H5File h5File, H5Group h5Group,
			String name, int value) {
		// begin-user-code

		// If the file, h5Group, or the name is null, or if the name is the
		// empty string, return false
		if (h5File == null || name == null || "".equals(name.trim())
				|| h5Group == null) {
			return false;
		}

		try {

			// Declare a file id
			int fileIdentifier = h5File.getFID();

			// Try to open the file and get a file id
			if (fileIdentifier == -1) {

				fileIdentifier = h5File.open();

			}

			// Check the file identifier
			if (fileIdentifier == -1) {
				return false;
			}

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			return false;
		}
		// Get a integer datatype
		H5Datatype datatype = HdfWriterFactory.createIntegerH5Datatype(h5File);

		// 1D of size 1
		long[] dims = { 1 };

		// Create the value
		int[] values = { value };

		// Create an attribute object
		Attribute attribute = new Attribute(name, datatype, dims, values);

		// Write the attribute
		try {

			h5Group.writeMetadata(attribute);

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			// return false
			return false;
		}

		return true;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Writes an Attribute to h5Group called name with a String value using
	 * h5File. If h5File is null or can not be opened, then false is returned.
	 * If name is null or is an empty String, then false is returned. If value
	 * is null or is an empty String, then false is returned. If an exception is
	 * thrown, then false is returned. If h5Group is null, then false is
	 * returned. Otherwise, true is returned.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param h5File
	 *            <p>
	 *            An H5File.
	 *            </p>
	 * @param h5Group
	 *            <p>
	 *            The H5Group to write the attribute to.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the Attribute.
	 *            </p>
	 * @param value
	 *            <p>
	 *            The value of the Attribute.
	 *            </p>
	 * @return <p>
	 *         If h5File is null or can not be opened, then false is returned.
	 *         If name is null or is an empty String, then false is returned. If
	 *         value is null or is an empty String, then false is returned. If
	 *         an exception is thrown, then false is returned. If h5Group is
	 *         null, then false is returned. Otherwise, true is returned.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public static boolean writeStringAttribute(H5File h5File, H5Group h5Group,
			String name, String value) {
		// begin-user-code

		// If the file, h5Group, or the name is null, or if the name is the
		// empty string, return false
		if (h5File == null || name == null || "".equals(name.trim())
				|| h5Group == null || value == null || "".equals(name.trim())) {
			return false;
		}

		try {

			// Declare a file id
			int fileIdentifier = h5File.getFID();

			// Try to open the file and get a file id
			if (fileIdentifier == -1) {

				fileIdentifier = h5File.open();

			}

			// Check the file identifier
			if (fileIdentifier == -1) {
				return false;
			}

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			return false;
		}

		try {

			// Create a custom String data type for the value
			H5Datatype datatype = (H5Datatype) h5File.createDatatype(
					Datatype.CLASS_STRING, value.length(), Datatype.NATIVE,
					Datatype.NATIVE);

			// 1D of size 1
			long[] dims = { 1 };

			// Create a String array of size one to hold the value
			String[] values = new String[1];

			// Assign the value to the first array index
			values[0] = value;

			// Create a byte array from values using the stringToByte method
			// See
			// http://mail.hdfgroup.org/pipermail/hdf-forum_hdfgroup.org/2011-March/004509.html
			byte[] bvalue = Dataset.stringToByte(values, value.length());

			// Create an attribute object
			Attribute attribute = new Attribute(name, datatype,
					new long[] { 1 });

			// Set the value of the attribute to bvalue
			attribute.setValue(bvalue);

			// Write the attribute to the group's metadata
			h5Group.writeMetadata(attribute);

		} catch (Exception e) {

			// Print the stack trace
			e.printStackTrace();

			// return false
			return false;
		}

		return true;
		// end-user-code
	}
}