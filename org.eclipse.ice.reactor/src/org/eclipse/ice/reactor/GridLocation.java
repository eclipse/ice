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


/**
 * <p>
 * The GridLocation class represents a location on a Cartesian grid. This class
 * also stores an implementation of the IDataProvider, called LWRDataProvider.
 * This class was designed with two specific roles in mind.
 * </p>
 * <p>
 * 1.) To provide a wrapper for Rows and Columns (i, j or x, y) on a cartesean
 * grid.
 * </p>
 * <p>
 * 2.) To provide state point data at particular positions within a grid.
 * </p>
 * <p>
 * As 1.) is easy to understand, 2.) is a bit complex. To optimize memory
 * efficiency, the LWRDataProvider should be used to represent changes in
 * statepointdata at a particular position for a specific type of delegated sub
 * component.
 * </p>
 * <p>
 * For example, in most reactors there are only about 3-5 different types of
 * "Rods" used within a reaction. On an assembly, these rods are used multiple
 * times for different positions. These rods also contain materials that
 * decompose over a reaction's lifetime. For a 17x17 assembly, it is a waste of
 * memory to store 17x17 amount of rods for each location along with unique
 * state point data (LWRDataProvider) for each rod (especially if most of them
 * are the same type of rod). It is preferred to store 5 rods and to set their
 * positions based on name within that assembly and to change the statepoint
 * data on the assembly through this class over the life cycle of the reaction.
 * This improves memory efficiency and separates the model from the
 * "experimental data".
 * </p>
 * <p>
 * Keep in mind, you can do both. However, the second method improves IO times
 * in magnitudes and is more optimal.
 * </p>
 * 
 * @author Scott Forest Hull II
 */
public class GridLocation implements Comparable<GridLocation> {
	/**
	 * <p>
	 * The row position.
	 * </p>
	 * 
	 */
	private int row;
	/**
	 * <p>
	 * The column position.
	 * </p>
	 * 
	 */
	private int col;

	/**
	 * <p>
	 * The LWRDataProvider.
	 * </p>
	 * 
	 */
	private LWRDataProvider lWRDataProvider;

	/**
	 * <p>
	 * The Constructor.
	 * </p>
	 * 
	 * @param row
	 *            <p>
	 *            The row position.
	 *            </p>
	 * @param col
	 *            <p>
	 *            The column position.
	 *            </p>
	 */
	public GridLocation(int row, int col) {

		// Set default values
		this.row = 1;
		this.col = 1;

		// Check row and column
		if (row >= 0) {
			this.row = row;
		}
		if (col >= 0) {
			this.col = col;
		}

		// Setup LWRDataProvider
		this.lWRDataProvider = new LWRDataProvider();

	}

	/**
	 * <p>
	 * Returns the row position.
	 * </p>
	 * 
	 * @return <p>
	 *         The row position.
	 *         </p>
	 */
	public int getRow() {

		return this.row;
	}

	/**
	 * <p>
	 * Returns the column position.
	 * </p>
	 * 
	 * @return <p>
	 *         The column position.
	 *         </p>
	 */
	public int getColumn() {

		return this.col;
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
		GridLocation location;
		boolean retVal = false;

		// If they are equal to the same object, return true
		if (this == otherObject) {
			return true;
		}

		// Check that the object is not null and an instance of this object
		if (otherObject != null && otherObject instanceof GridLocation) {
			location = (GridLocation) otherObject;
			// Check values
			retVal = this.row == location.row && this.col == location.col
					&& this.lWRDataProvider.equals(location.lWRDataProvider);

		}

		// Return the retVal
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
		int hash = 31;

		// Compute hash across attributes

		// Added +1 to these pieces just in case row or col is 0
		hash += 31 * (this.col + 1);
		hash += 31 * (this.row + 1);
		hash += 31 * this.lWRDataProvider.hashCode();

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
	public void copy(GridLocation otherObject) {

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}

		// Copy contents of the object
		this.row = otherObject.row;
		this.col = otherObject.col;
		this.lWRDataProvider = (LWRDataProvider) otherObject.lWRDataProvider
				.clone();

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
		GridLocation location = new GridLocation(0, 0);

		// Copy contents
		location.copy(this);

		// Return the newly instantiated object
		return location;

	}

	/**
	 * <p>
	 * Sets the LWRDataProvider.
	 * </p>
	 * 
	 * @param dataProvider
	 *            <p>
	 *            The IDataProvider implementation.
	 *            </p>
	 */
	public void setLWRDataProvider(LWRDataProvider dataProvider) {

		if (dataProvider != null) {
			this.lWRDataProvider = dataProvider;
		}

	}

	/**
	 * <p>
	 * Returns the LWRDataProvider.
	 * </p>
	 * 
	 * @return <p>
	 *         The IDataProvider implementation.
	 *         </p>
	 */
	public LWRDataProvider getLWRDataProvider() {

		return this.lWRDataProvider;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Comparable#compareTo(Object arg0)
	 */
	@Override
	public int compareTo(GridLocation location) {
		// Local Declarations

		// Return the difference of the row
		if (this.row != location.row) {
			return this.row - location.row;
		}
		// Return the difference of col. If the rows are equal, then if the cols
		// are equal, this should return 0.
		return this.col - location.col;

	}

}