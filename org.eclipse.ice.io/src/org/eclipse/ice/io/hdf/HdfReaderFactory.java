/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Eric J. Lingerfelt, Alexander J. McCaskey,
 *   Taylor Patterson, Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.io.hdf;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ncsa.hdf.hdf5lib.H5;
import ncsa.hdf.hdf5lib.HDF5Constants;
import ncsa.hdf.object.Attribute;
import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.HObject;
import ncsa.hdf.object.h5.H5Group;

/**
 * <p>
 * The HdfReaderFactory class contains static methods used to read elements from
 * an HDF5 file.
 * </p>
 *
 * @author Eric J. Lingerfelt
 */
public class HdfReaderFactory {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(HdfReaderFactory.class);

	/**
	 * <p>
	 * Returns the child H5Group called name from parentH5Group. If
	 * parentH5Group is null, then null is returned. If name is null or is an
	 * empty String, then null is returned. If there is no child H5Group called
	 * name, then null is returned.
	 * </p>
	 *
	 * @param parentH5Group
	 *            <p>
	 *            The parent H5Group to search.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the child H5Group.
	 *            </p>
	 * @return
	 * 		<p>
	 *         The child H5Group.
	 *         </p>
	 */
	public static H5Group getChildH5Group(H5Group parentH5Group, String name) {

		// If the name or parentH5Group is null or size is 0, return null
		if (name == null || parentH5Group == null
				|| parentH5Group.getNumberOfMembersInFile() == 0) {
			return null;
		}
		// Get the group or return null
		try {
			return (H5Group) parentH5Group.getFileFormat()
					.get(parentH5Group.getFullName()
							+ System.getProperty("file.separator") + name);
		} catch (Exception e) {
			logger.error("HdfReaderFactory Exception!", e);
			return null;
		}

	}

	/**
	 * <p>
	 * Returns the child H5Group at the provided index from parentH5Group's
	 * member list. If parentH5Group is null, then null is returned. If index
	 * &lt; 0, then null is returned. If parentH5Group has no children, then
	 * null is returned. If the object at located at the provided index is not
	 * an H5Group, then null is returned.
	 * </p>
	 *
	 * @param parentH5Group
	 *            <p>
	 *            The parent H5Group to search.
	 *            </p>
	 * @param index
	 *            <p>
	 *            The index of the child H5Group in parentH5Group's member list.
	 *            </p>
	 * @return
	 * 		<p>
	 *         The child H5Group.
	 *         </p>
	 */
	public static H5Group getChildH5Group(H5Group parentH5Group, int index) {

		// Return null if the parameters are invalid.
		if (parentH5Group == null || index < 0) {
			return null;
		}

		String name;
		H5Group childGroup = null;
		try {
			// Get the parent H5Group's file ID.
			int fileID = parentH5Group.getFID();

			// Get the name of the parent H5Group.
			String parentName = parentH5Group.getFullName();

			// Make sure the index parameter is not too large.
			// H5Gn_members tells us how many children parentH5Group has.
			if (index >= H5.H5Gn_members(fileID, parentName)) {
				return null;
			}

			// These constants are required in the H5 library call.
			int indexType = HDF5Constants.H5_INDEX_NAME; // Members are ordered
															// by name!
			int order = HDF5Constants.H5_ITER_INC;
			int linkAccessPropertyList = HDF5Constants.H5P_DEFAULT;

			// Get the member object's name by its index.
			name = H5.H5Lget_name_by_idx(fileID, parentName, indexType, order,
					index, linkAccessPropertyList);

			childGroup = (H5Group) parentH5Group.getFileFormat()
					.get(parentH5Group.getFullName()
							+ System.getProperty("file.separator") + name);
		} catch (Exception e) {
			// If we encounter an error, return null.
			logger.error("HdfReaderFactory Exception!", e);
		}

		// Now that we have the object's name, get the child H5Group.
		return childGroup;
	}

	/**
	 * <p>
	 * Returns an ArrayList of all child H5Groups from parentH5Group's member
	 * list. If parentH5Group is null, then null is returned. If parentH5Group
	 * has no H5Group children, then an empty ArrayList is returned.
	 * </p>
	 *
	 * @param parentH5Group
	 *            <p>
	 *            The parent H5Group to search.
	 *            </p>
	 * @return
	 * 		<p>
	 *         An ArrayList of child H5Groups.
	 *         </p>
	 */
	public static ArrayList<H5Group> getChildH5Groups(H5Group parentH5Group) {

		// Previously with this code, we used only the HDF5 Java object library
		// to perform this function. However, when the child groups take up too
		// much memory, only a subset of those groups will be in the list
		// returned by parentH5Group.getMemberList(). Instead, we need to call
		// the standard HDF5 Java library to perform such a lookup.

		// Initialize the list to return.
		ArrayList<H5Group> groupList = new ArrayList<H5Group>();

		// Return the empty list if the parameters are invalid.
		if (parentH5Group == null) {
			return groupList;
		}

		// Get the file ID of the parent group.
		int file_id = parentH5Group.getFID();

		// Get the name of the parent group.
		String parentName = parentH5Group.getFullName();

		// These variables will hold metadata about parentH5Group:
		// The number of members in parentH5Group.
		int count = 0;
		// The names of the members.
		String[] objectNames;
		// The types of the members (e.g., DataSet or Group).
		int[] objectTypes;

		try {
			// Get the number of members in parentH5Group.
			count = H5.H5Gn_members(file_id, parentName);

			// Go ahead and return if we there are no child members.
			if (count == 0) {
				return groupList;
			}
			// Initialize the metadata containers.
			objectNames = new String[count];
			objectTypes = new int[count];

			// These are required for the following function call but are not
			// used elsewhere.
			int[] ltype = new int[count];
			long[] orefs = new long[count];
			int indexType = HDF5Constants.H5_INDEX_NAME;

			// Get the metadata for of of parentH5Group's members.
			H5.H5Gget_obj_info_all(file_id, parentName, objectNames,
					objectTypes, ltype, orefs, indexType);

			// Look for any members that are of the type H5O_TYPE_GROUP
			// (H5Groups).
			for (int i = 0; i < count; i++) {
				if (objectTypes[i] == HDF5Constants.H5O_TYPE_GROUP) {
					// Try to get the child H5Group from parentH5Group and add
					// it
					// to the list.
					HObject hobj = parentH5Group.getFileFormat()
							.get(parentH5Group.getFullName()
									+ System.getProperty("file.separator")
									+ objectNames[i]);
					if (hobj != null) {
						groupList.add((H5Group) hobj);
					}
				}
			}
		} catch (Exception e) {
			// If we encounter an error, return the empty list.
			logger.error("HdfReaderFactory Exception!", e);
			return groupList;
		}

		return groupList;
	}

	/**
	 * Returns an ArrayList of <b>all</b> child HObjects from a parentH5Group.
	 * This is analogous to and should be used in lieu of
	 * parentH5Group.getMemberList(), which only returns child HObjects that are
	 * in memory.
	 *
	 * @param parentH5Group
	 *            The parent H5Group to query.
	 * @return An ArrayList of <b>all</b> child H5Objects, or an empty list if
	 *         the group cannot be read.
	 */
	public static ArrayList<HObject> getChildH5Members(H5Group parentH5Group) {

		// Initialize the list to return.
		ArrayList<HObject> groupList = new ArrayList<HObject>();

		// Return the empty list if the parameters are invalid.
		if (parentH5Group == null) {
			return groupList;
		}

		// Get the file ID of the parent group.
		int file_id = parentH5Group.getFID();

		// Get the name of the parent group.
		String parentName = parentH5Group.getFullName();

		// These variables will hold metadata about parentH5Group:
		// The number of members in parentH5Group.
		int count = 0;
		// The names of the members.
		String[] objectNames;
		// The types of the members (e.g., DataSet or Group).
		int[] objectTypes;

		try {
			// Get the number of members in parentH5Group.
			count = H5.H5Gn_members(file_id, parentName);

			// Go ahead and return if we there are no child members.
			if (count == 0) {
				return groupList;
			}

			// Initialize the metadata containers.
			objectNames = new String[count];
			objectTypes = new int[count];

			// These are required for the following function call but are not
			// used elsewhere.
			int[] ltype = new int[count];
			long[] orefs = new long[count];
			int indexType = HDF5Constants.H5_INDEX_NAME;

			// Get the metadata for of of parentH5Group's members.
			H5.H5Gget_obj_info_all(file_id, parentName, objectNames,
					objectTypes, ltype, orefs, indexType);

			// Add all members to the array.
			for (int i = 0; i < count; i++) {
				HObject hobj = parentH5Group.getFileFormat()
						.get(parentH5Group.getFullName()
								+ System.getProperty("file.separator")
								+ objectNames[i]);
				if (hobj != null) {
					groupList.add(hobj);
				}
			}

		} catch (Exception e) {
			// If we encounter an error, return the empty list.
			logger.error("HdfReaderFactory Exception!", e);
			return groupList;
		}

		return groupList;
	}

	/**
	 * <p>
	 * Returns the Dataset called name from h5Group. If h5Group is null, then
	 * null is returned. If name is null or an empty String, then null is
	 * returned. If h5Group has no Datasets, then null is returned. If a Dataset
	 * called name can not be located, then null is returned.
	 * </p>
	 *
	 * @param h5Group
	 *            <p>
	 *            The H5Group to search.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the Dataset to search for.
	 *            </p>
	 * @return
	 * 		<p>
	 *         The located Dataset.
	 *         </p>
	 */
	public static Dataset getDataset(H5Group h5Group, String name) {

		// Check to make sure the group and name is not null AND that it
		// contains members
		if (h5Group == null || name == null
				|| h5Group.getNumberOfMembersInFile() == 0) {
			return null;
		}
		// Get the dataset or return null
		try {
			return (Dataset) h5Group.getFileFormat().get(h5Group.getFullName()
					+ System.getProperty("file.separator") + name);
		} catch (Exception e) {
			logger.error("HdfReaderFactory Exception!", e);
			return null;
		}

	}

	/**
	 * <p>
	 * Reads and returns a Double object read from the Attribute called name
	 * from the metadata for h5Group. If name is null or an empty String, then
	 * null is returned. If h5Group is null, then null is returned. If an
	 * Attribute called name cannot be located, then null is returned. If the
	 * Attribute called name is located but is not of Datatype.CLASS_FLOAT, then
	 * null is returned.
	 * </p>
	 *
	 * @param h5Group
	 *            <p>
	 *            The H5Group to read.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the attribute.
	 *            </p>
	 * @return
	 * 		<p>
	 *         The Attribute's value as a Double object.
	 *         </p>
	 */
	public static Double readDoubleAttribute(H5Group h5Group, String name) {

		try {
			// If the name or H5Group is null or if there is no meta data,
			// return null
			if (h5Group == null || name == null
					|| h5Group.getMetadata().isEmpty()) {
				return null;
			}
			// Loop over the meta data to find the attribute with the correct
			// name
			for (int i = 0; i < h5Group.getMetadata().size(); i++) {

				// Cast as an attribute
				Attribute attribute = (Attribute) h5Group.getMetadata().get(i);

				// If the attribute is the correct name and type, return the
				// Double
				if (attribute.getName().equals(name) && attribute.getType()
						.getDatatypeClass() == Datatype.CLASS_FLOAT) {

					// Return the correct value
					return ((double[]) attribute.getValue())[0];
				}
			}
		} catch (Exception e) {
			// Print the stack trace
			logger.error("HdfReaderFactory Exception!", e);
		}

		// Return null, nothing found
		return null;

	}

	/**
	 * <p>
	 * Reads and returns an Integer object read from the Attribute called name
	 * from the metadata for h5Group. If name is null or an empty String, then
	 * null is returned. If h5Group is null, then null is returned. If an
	 * Attribute called name cannot be located, then null is returned. If the
	 * Attribute called name is located but is not of Datatype.CLASS_INTEGER,
	 * then null is returned.
	 * </p>
	 *
	 * @param h5Group
	 *            <p>
	 *            The H5Group to read.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the attribute.
	 *            </p>
	 * @return
	 * 		<p>
	 *         The Attribute's value as an Integer object.
	 *         </p>
	 */
	public static Integer readIntegerAttribute(H5Group h5Group, String name) {
		try {
			// If the name or H5Group is null or if there is no meta data,
			// return null
			if (h5Group == null || name == null
					|| h5Group.getMetadata().isEmpty()) {
				return null;
			}
			// Loop over the meta data to find the attribute with the correct
			// name
			for (int i = 0; i < h5Group.getMetadata().size(); i++) {

				// Cast as an attribute
				Attribute attribute = (Attribute) h5Group.getMetadata().get(i);

				// If the attribute is the correct name and type, return the
				// Integer
				if (attribute.getName().equals(name) && attribute.getType()
						.getDatatypeClass() == Datatype.CLASS_INTEGER) {

					// Return the correct value
					return ((int[]) attribute.getValue())[0];
				}
			}
		} catch (Exception e) {
			// Print the stack trace
			logger.error("HdfReaderFactory Exception!", e);
		}
		// Return null, nothing found
		return null;

	}

	/**
	 * <p>
	 * Reads and returns a String object read from the Attribute called name
	 * from the metadata for h5Group. If name is null or an empty String, then
	 * null is returned. If h5Group is null, then null is returned. If an
	 * Attribute called name cannot be located, then null is returned. If the
	 * Attribute called name is located but is not of Datatype.CLASS_STRING,
	 * then null is returned.
	 * </p>
	 *
	 * @param h5Group
	 *            <p >
	 *            The H5Group to read.
	 *            </p>
	 * @param name
	 *            <p>
	 *            The name of the attribute.
	 *            </p>
	 * @return
	 * 		<p>
	 *         The Attribute's value as a String object.
	 *         </p>
	 */
	public static String readStringAttribute(H5Group h5Group, String name) {

		try {
			// If the name or H5Group is null or if there is no meta data,
			// return null
			if (h5Group == null || name == null
					|| h5Group.getMetadata().isEmpty()) {
				return null;
			}
			// Loop over the meta data to find the attribute with the correct
			// name
			for (int i = 0; i < h5Group.getMetadata().size(); i++) {
				// Cast as an attribute
				Attribute attribute = (Attribute) h5Group.getMetadata().get(i);

				// If the attribute is the correct name and type, return the
				// String
				if (attribute.getName().equals(name) && attribute.getType()
						.getDatatypeClass() == Datatype.CLASS_STRING) {

					// Return the correct value
					return ((String[]) attribute.getValue())[0];

				}
			}
		} catch (Exception e) {
			// Print the stack trace
			logger.error("HdfReaderFactory Exception!", e);
		}
		// Return null, nothing found
		return null;

	}
}