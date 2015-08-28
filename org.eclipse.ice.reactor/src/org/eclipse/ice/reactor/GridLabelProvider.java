/*******************************************************************************
 * Copyright (c) 2012, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Scott Forest Hull II - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - bug 474744
 *******************************************************************************/
package org.eclipse.ice.reactor;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlTransient;

/**
 * This is a utility class that provides labels on a 2D grid for rows and
 * columns. This class should be considered as a piece designed specifically for
 * interactions with the GUI and should not be considered as a means to override
 * the ability to set rows and column indicie types.
 * <p>
 * The constructor takes a size that is N squared, and defaults to a positive
 * number if the size is non-positive or zero.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class GridLabelProvider extends LWRComponent {
	/**
	 * An ArrayList of Strings of length size containing the label for each
	 * column position from left to right.
	 */
	@XmlTransient
	private ArrayList<String> columnLabels;
	/**
	 * An ArrayList of Strings of length size containing the label for each row
	 * position from top to bottom.
	 */
	@XmlTransient
	private ArrayList<String> rowLabels;
	/**
	 * The size for the row and column label ArrayLists.
	 * 
	 */
	@XmlTransient
	private int size;

	// Private Attributes for specifying row and column types
	@XmlTransient
	public static final String ROW_LABELS_NAME = "Row Labels";
	@XmlTransient
	public static final String COLUMN_LABELS_NAME = "Column Labels";
	@XmlTransient
	public static final String LABELS_GROUP_NAME = "Labels";

	/**
	 * A default constructor that should ONLY be used for persistence and
	 * testing. It is equivalent to GridLabelProvider(15).
	 */
	public GridLabelProvider() {
		this(15);
	}

	/**
	 * A parameterized constructor.
	 * 
	 * @param size
	 *            The size for the row and column label ArrayLists.
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
	 * Returns the column position from a label. Returns -1 if the label is not
	 * found or if the label is null.
	 * 
	 * @param columnLabel
	 *            The column label.
	 * @return The column position.
	 */
	public int getColumnFromLabel(String columnLabel) {
		// If the column label is in there, or -1 if it does not exist
		if (columnLabel != null) {
			return this.columnLabels.indexOf(columnLabel);
		}

		return -1;
	}

	/**
	 * Returns the row position from a label. Returns -1 if the label is not
	 * found or if the label is null.
	 * 
	 * @param rowLabel
	 *            The row label.
	 * @return The row position.
	 */
	public int getRowFromLabel(String rowLabel) {

		// If the row label is in there, or -1 if it does not exist
		if (rowLabel != null) {
			return this.rowLabels.indexOf(rowLabel);
		}

		return -1;

	}

	/**
	 * Returns the label at position column.
	 * 
	 * @param column
	 *            The column position.
	 * @return The label at the provided column position.
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
	 * Returns the label at position row.
	 * 
	 * @param row
	 *            The row position.
	 * @return The label at the provided row position.
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
	 * Sets the array of row labels ordered from top to bottom.
	 * 
	 * @param rowLabels
	 *            The array of row labels ordered from top to bottom.
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
	 * Sets the array of column labels ordered from left to right.
	 * 
	 * @param columnLabels
	 *            The array of column labels ordered from left to right.
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
	 * Returns the size for the row and column label ArrayLists.
	 * 
	 * @return The size for the row and column label ArrayLists.
	 */
	public int getSize() {
		return this.size;

	}

	/**
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * 
	 * @param otherObject
	 *            The object to be compared.
	 * @return True if otherObject is equal. False otherwise.
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
					&& this.rowLabels.equals(provider.rowLabels)
					&& this.columnLabels.equals(provider.columnLabels));
		}

		// Return the equality
		return retVal;

	}

	/**
	 * Returns the hashCode of the object.
	 * 
	 * @return The hash of the object.
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
	 * Deep copies the contents of the object.
	 * 
	 * @param otherObject
	 *            The object to be copied.
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
	 * Deep copies and returns a newly instantiated object.
	 * 
	 * @return The newly instantiated copied object.
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

}