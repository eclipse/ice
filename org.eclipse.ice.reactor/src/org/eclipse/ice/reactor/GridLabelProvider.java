/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactor;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlTransient;

import ncsa.hdf.object.Dataset;
import ncsa.hdf.object.Datatype;
import ncsa.hdf.object.h5.H5Datatype;
import ncsa.hdf.object.h5.H5File;
import ncsa.hdf.object.h5.H5Group;

import org.eclipse.ice.io.hdf.HdfReaderFactory;
import org.eclipse.ice.io.hdf.HdfWriterFactory;

/**
 * <p>
 * This is a utility class that provides labels on a 2D grid for rows and
 * columns. This class should be considered as a piece designed specifically for
 * interactions with the GUI and should not be considered as a means to override
 * the ability to set rows and column indicie types.
 * </p>
 * <p>
 * The constructor takes a size that is N squared, and defaults to a positive
 * number if the size is non-positive or zero.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class GridLabelProvider extends LWRComponent {
	/**
	 * <p>
	 * An ArrayList of Strings of length size containing the label for each
	 * column position from left to right.
	 * </p>
	 * 
	 */
	@XmlTransient
	private ArrayList<String> columnLabels;
	/**
	 * <p>
	 * An ArrayList of Strings of length size containing the label for each row
	 * position from top to bottom.
	 * </p>
	 * 
	 */
	@XmlTransient
	private ArrayList<String> rowLabels;
	/**
	 * <p>
	 * The size for the row and column label ArrayLists.
	 * </p>
	 * 
	 */
	@XmlTransient
	private int size;

	// Private Attributes for specifying row and column types
	@XmlTransient
	private static final String ROW_LABELS_NAME = "Row Labels";
	@XmlTransient
	private static final String COLUMN_LABELS_NAME = "Column Labels";
	@XmlTransient
	private static final String LABELS_GROUP_NAME = "Labels";

	/**
	 * A default constructor that should ONLY be used for persistence and
	 * testing. It is equivalent to GridLabelProvider(15).
	 */
	public GridLabelProvider() {
		this(15);
	}

	/**
	 * <p>
	 * A parameterized constructor.
	 * </p>
	 * 
	 * @param size
	 *            <p>
	 *            The size for the row and column label ArrayLists.
	 *            </p>
	 */
	public GridLabelProvider(int size) {

		// Setup LWRComponent info
		this.name = "GridLabelProvider 1";
		this.description = "GridLabelProvider 1's Description";
		this.id = 1;
		this.HDF5LWRTag = HDF5LWRTagType.GRID_LABEL_PROVIDER;

		// Default values
		this.size = 1;
		this.columnLabels = new ArrayList<String>();
		this.rowLabels = new ArrayList<String>();

		// Check size - can't be less than or equal to 0.
		if (size > 0) {
			this.size = size;
		}

		// Setup the HDF5LWRTagType to correct type
		this.HDF5LWRTag = HDF5LWRTagType.GRID_LABEL_PROVIDER;

	}

	/**
	 * <p>
	 * Returns the column position from a label. Returns -1 if the label is not
	 * found or if the label is null.
	 * </p>
	 * 
	 * @param columnLabel
	 *            <p>
	 *            The column label.
	 *            </p>
	 * @return <p>
	 *         The column position.
	 *         </p>
	 */
	public int getColumnFromLabel(String columnLabel) {
		// If the column label is in there, or -1 if it does not exist
		if (columnLabel != null) {
			return this.columnLabels.indexOf(columnLabel);
		}

		return -1;
	}

	/**
	 * <p>
	 * Returns the row position from a label. Returns -1 if the label is not
	 * found or if the label is null.
	 * </p>
	 * 
	 * @param rowLabel
	 *            <p>
	 *            The row label.
	 *            </p>
	 * @return <p>
	 *         The row position.
	 *         </p>
	 */
	public int getRowFromLabel(String rowLabel) {

		// If the row label is in there, or -1 if it does not exist
		if (rowLabel != null) {
			return this.rowLabels.indexOf(rowLabel);
		}

		return -1;

	}

	/**
	 * <p>
	 * Returns the label at position column.
	 * </p>
	 * 
	 * @param column
	 *            <p>
	 *            The column position.
	 *            </p>
	 * @return <p>
	 *         The label at the provided column position.
	 *         </p>
	 */
	public String getLabelFromColumn(int column) {
		// Return the column label or null if it does not exist
		// Make sure its within the size
		// Make sure the column labels also have stuff in the arraylist
		if (column >= 0 && column < this.size && !this.columnLabels.isEmpty()) {
			return this.columnLabels.get(column);
		}

		// Return null if not bound correctly
		return null;
	}

	/**
	 * <p>
	 * Returns the label at position row.
	 * </p>
	 * 
	 * @param row
	 *            <p>
	 *            The row position.
	 *            </p>
	 * @return <p>
	 *         The label at the provided row position.
	 *         </p>
	 */
	public String getLabelFromRow(int row) {

		// Return the row label or null if it does not exist
		// Make sure its within the size
		// Make sure the row labels also have stuff in the arraylist
		if (row >= 0 && row < this.size && !this.rowLabels.isEmpty()) {
			return this.rowLabels.get(row);
		}

		// Return null if not bound correctly
		return null;

	}

	/**
	 * <p>
	 * Sets the array of row labels ordered from top to bottom.
	 * </p>
	 * 
	 * @param rowLabels
	 *            <p>
	 *            The array of row labels ordered from top to bottom.
	 *            </p>
	 */
	public void setRowLabels(ArrayList<String> rowLabels) {

		// If the rowLabels passed are not null and equal in size, then add them
		if (rowLabels != null && rowLabels.size() == this.size) {
			this.rowLabels.clear(); // Clear out the current list
			for (int i = 0; i < rowLabels.size(); i++) {
				this.rowLabels.add(rowLabels.get(i));
			}
		}

	}

	/**
	 * <p>
	 * Sets the array of column labels ordered from left to right.
	 * </p>
	 * 
	 * @param columnLabels
	 *            <p>
	 *            The array of column labels ordered from left to right.
	 *            </p>
	 */
	public void setColumnLabels(ArrayList<String> columnLabels) {

		// If the columnLabels passed are not null and equal in size, then add
		// them
		if (columnLabels != null && columnLabels.size() == this.size) {
			this.columnLabels.clear(); // Clear out the current list
			for (int i = 0; i < columnLabels.size(); i++) {
				this.columnLabels.add(columnLabels.get(i));
			}
		}

	}

	/**
	 * <p>
	 * Returns the size for the row and column label ArrayLists.
	 * </p>
	 * 
	 * @return <p>
	 *         The size for the row and column label ArrayLists.
	 *         </p>
	 */
	public int getSize() {
		return this.size;


	}

	/**
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 */
	@Override
	public boolean equals(Object otherObject) {

		// Local Declarations
		GridLabelProvider provider;
		boolean retVal = false;

		// If they are equal to the same object, return true
		if (this == otherObject) {
			return true;
		}

		// If the object is null or not an instance of this object, return false
		if (otherObject != null || otherObject instanceof GridLabelProvider) {

			// Cast to local object
			provider = (GridLabelProvider) otherObject;

			// Get the equality of the values
			retVal = (super.equals(otherObject) && this.size == provider.size
					&& this.rowLabels.equals(provider.rowLabels) && this.columnLabels
					.equals(provider.columnLabels));
		}

		// Return the equality
		return retVal;

	}

	/**
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * 
	 * @return <p>
	 *         The hash of the object.
	 *         </p>
	 */
	@Override
	public int hashCode() {

		// Local Declarations
		int hash = super.hashCode();

		// Hash the values on the object
		hash += 31 * this.size;
		hash += 31 * this.rowLabels.hashCode();
		hash += 31 * this.columnLabels.hashCode();

		// Return the hashCode
		return hash;

	}

	/**
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 */
	public void copy(GridLabelProvider otherObject) {

		// If object is null, return
		if (otherObject == null) {
			return;
		}

		// Copy contents - super
		super.copy(otherObject);

		// Copy contents

		// Deep copy columnLabels
		this.columnLabels.clear();
		for (int i = 0; i < otherObject.columnLabels.size(); i++) {
			this.columnLabels.add(new String(otherObject.columnLabels.get(i)));
		}

		// Deep copy rowLabels
		this.rowLabels.clear();
		for (int i = 0; i < otherObject.rowLabels.size(); i++) {
			this.rowLabels.add(new String(otherObject.rowLabels.get(i)));
		}

		this.size = otherObject.size;

	}

	/**
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 */
	@Override
	public Object clone() {

		// Local Declarations
		GridLabelProvider provider = new GridLabelProvider(0);

		// Copy contents
		provider.copy(this);

		// Return the newly instantiated object
		return provider;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComponent#writeAttributes(ncsa.hdf.object.h5.H5File, ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean writeAttributes(H5File h5File, H5Group h5Group) {
		boolean flag = true;

		flag &= super.writeAttributes(h5File, h5Group);
		flag &= HdfWriterFactory.writeIntegerAttribute(h5File, h5Group, "size",
				size);

		return flag;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComponent#writeDatasets(ncsa.hdf.object.h5.H5File, ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean writeDatasets(H5File h5File, H5Group h5Group) {
		boolean flag = true;

		// Return if the file or group is null
		if (h5File == null || h5Group == null) {
			return false;
		}

		flag &= super.writeDatasets(h5File, h5Group);

		H5Group labelsGroup = HdfWriterFactory.createH5Group(h5File,
				GridLabelProvider.LABELS_GROUP_NAME, h5Group);

		// If there are no columns or rows set, then return true
		if (this.columnLabels.isEmpty() && this.rowLabels.isEmpty()) {
			return true;
		}

		try {

			// Initialize row and column String arrays
			String[] rowValues = new String[this.size];
			String[] columnValues = new String[this.size];

			// Declare biggest size variables
			int biggestRowSize = 0;
			int biggestColumnSize = 0;

			// Check to see if we have any labels and
			// that they are of the right size
			if (this.rowLabels.size() != this.size
					|| this.columnLabels.size() != this.size) {

				// If not return false
				return false;
			}

			// Loop over the labels
			for (int i = 0; i < size; i++) {

				// Assign the current label
				String rowLabel = this.rowLabels.get(i);
				String columnLabel = this.columnLabels.get(i);

				// Get the biggest size label
				biggestRowSize = Math.max(biggestRowSize, rowLabel.length());
				biggestColumnSize = Math.max(biggestColumnSize,
						columnLabel.length());

				// Assign the label to the String array
				rowValues[i] = rowLabel;
				columnValues[i] = columnLabel;

			}

			// Create datatypes for the row and column labels
			H5Datatype rowH5Datatype = (H5Datatype) h5File.createDatatype(
					Datatype.CLASS_STRING, biggestRowSize, Datatype.NATIVE,
					Datatype.NATIVE);

			H5Datatype columnH5Datatype = (H5Datatype) h5File.createDatatype(
					Datatype.CLASS_STRING, biggestColumnSize, Datatype.NATIVE,
					Datatype.NATIVE);

			// Set the dimensions of the String array
			long[] dims = { size };

			// Create the row and column datasets
			Dataset h5RowDataset = h5File.createScalarDS(ROW_LABELS_NAME,
					labelsGroup, rowH5Datatype, dims, null, null, 0, null);
			Dataset h5ColumnDataset = h5File.createScalarDS(COLUMN_LABELS_NAME,
					labelsGroup, columnH5Datatype, dims, null, null, 0, null);

			// Write the String arrays to the right dataset
			h5RowDataset.write(rowValues);
			h5ColumnDataset.write(columnValues);

		} catch (Exception e) {

			// Print stack trace
			logger.error(getClass().getName() + " Exception!",e);

			// Return false
			return false;
		}

		return flag;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComponent#readDatasets(ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean readDatasets(H5Group h5Group) {

		// Call super
		boolean flag = super.readDatasets(h5Group);
		Object rowData = null;
		Object colData = null;

		if (h5Group == null) {
			return false;
		}

		H5Group labelGroup = HdfReaderFactory.getChildH5Group(h5Group,
				LABELS_GROUP_NAME);

		// No GridLabelProviders were written, return true
		if (labelGroup == null) {
			return true;
		}
		// Grab datasets
		Dataset rowSet = HdfReaderFactory.getDataset(labelGroup,
				ROW_LABELS_NAME);
		Dataset colSet = HdfReaderFactory.getDataset(labelGroup,
				COLUMN_LABELS_NAME);

		// If the group is null, return false
		// Verify that the two pieces
		// are datasets and the contents are in there correctly
		if (labelGroup == null || rowSet == null || colSet == null
				|| !flag) {
			return false;
		}

		// Iterate over the list and set up the rows and columns into their
		// arraylist

		try {
			rowData = rowSet.getData();
			colData = colSet.getData();
		} catch (Exception e) {
			logger.error(getClass().getName() + " Exception!",e);
			return false;
		}

		// If the data is null, return false
		if (rowData == null || colData == null) {
			return false;
		}

		// Iterate over the list and check the arrayLists
		for (int i = 0; i < this.size; i++) {
			// Check contents of rowData and columnData at X
			// Add the data when necessary
			this.rowLabels.add(((String[]) rowData)[i]);
			this.columnLabels.add(((String[]) colData)[i]);
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.reactor.LWRComponent#readAttributes(ncsa.hdf.object.h5.H5Group)
	 */
	@Override
	public boolean readAttributes(H5Group h5Group) {

		// Local Declarations
		boolean flag = true;

		// Get values
		Integer size = HdfReaderFactory.readIntegerAttribute(h5Group, "size");

		// Call super
		flag &= super.readAttributes(h5Group);

		// check values
		if (size == null || !flag || h5Group == null) {
			return false;
		}
		// If everything is valid, then set data
		this.size = size.intValue();

		// Reset labels
		this.columnLabels.clear();
		this.rowLabels.clear();

		return true;

	}
}