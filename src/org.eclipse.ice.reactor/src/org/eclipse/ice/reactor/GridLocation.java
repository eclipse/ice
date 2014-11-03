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

import java.lang.Comparable;

/**
 * <!-- begin-UML-doc -->
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
 * <!-- end-UML-doc -->
 * 
 * @author s4h
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class GridLocation implements Comparable<GridLocation> {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The row position.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int row;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The column position.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int col;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The LWRDataProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private LWRDataProvider lWRDataProvider;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param row
	 *            <p>
	 *            The row position.
	 *            </p>
	 * @param col
	 *            <p>
	 *            The column position.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public GridLocation(int row, int col) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the row position.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The row position.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getRow() {
		// begin-user-code

		return this.row;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the column position.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The column position.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getColumn() {
		// begin-user-code

		return this.col;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Overrides the equals operation to check the attributes on this object
	 * with another object of the same type. Returns true if the objects are
	 * equal. False otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be compared.
	 *            </p>
	 * @return <p>
	 *         True if otherObject is equal. False otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean equals(Object otherObject) {
		// begin-user-code

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

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the hashCode of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The hash of the object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int hashCode() {
		// begin-user-code

		// Local Declarations
		int hash = 31;

		// Compute hash across attributes

		// Added +1 to these pieces just in case row or col is 0
		hash += 31 * (this.col + 1);
		hash += 31 * (this.row + 1);
		hash += 31 * this.lWRDataProvider.hashCode();

		// Return the hashCode
		return hash;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies the contents of the object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param otherObject
	 *            <p>
	 *            The object to be copied.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void copy(GridLocation otherObject) {
		// begin-user-code

		// If the otherObject is null, return
		if (otherObject == null) {
			return;
		}

		// Copy contents of the object
		this.row = otherObject.row;
		this.col = otherObject.col;
		this.lWRDataProvider = (LWRDataProvider) otherObject.lWRDataProvider
				.clone();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Deep copies and returns a newly instantiated object.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The newly instantiated copied object.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Object clone() {
		// begin-user-code

		// Local Declarations
		GridLocation location = new GridLocation(0, 0);

		// Copy contents
		location.copy(this);

		// Return the newly instantiated object
		return location;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the LWRDataProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param dataProvider
	 *            <p>
	 *            The IDataProvider implementation.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setLWRDataProvider(LWRDataProvider dataProvider) {
		// begin-user-code

		if (dataProvider != null) {
			this.lWRDataProvider = dataProvider;
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the LWRDataProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The IDataProvider implementation.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public LWRDataProvider getLWRDataProvider() {
		// begin-user-code

		return this.lWRDataProvider;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see Comparable#compareTo(Object arg0)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int compareTo(GridLocation location) {
		// begin-user-code
		// Local Declarations

		// Return the difference of the row
		if (this.row != location.row) {
			return this.row - location.row;
		}
		// Return the difference of col. If the rows are equal, then if the cols
		// are equal, this should return 0.
		return this.col - location.col;

		// end-user-code
	}

}