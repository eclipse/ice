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
package org.eclipse.ice.kdd.kddmath;

/**
 * <p>
 * IAbstractMatrix is an interface for clients to realize that provides the
 * basic functionality of a nRows x nCols matrix of elements. It is generic so
 * that it can contain a matrix of any object.
 * </p>
 * 
 * @author Alex McCaskey
 */
public interface IAbstractMatrix<E> {
	/**
	 * <p>
	 * Get the element value at the given row and column index.Returns null if
	 * invalid index.
	 * </p>
	 * 
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 */
	public E getElement(int rowIndex, int colIndex);

	/**
	 * <p>
	 * Set the value of the individual matrix element at index i,j.
	 * </p>
	 * 
	 * @param rowIndex
	 * @param colIndex
	 * @param value
	 * @return
	 */
	public boolean setElement(int rowIndex, int colIndex, E value);

	/**
	 * <p>
	 * This method transposes this KDDMatrix.
	 * </p>
	 * 
	 */
	public void transpose();

	/**
	 * <p>
	 * Returns the number of rows in this matrix
	 * </p>
	 * 
	 * @return
	 */
	public int numberOfRows();

	/**
	 * <p>
	 * Returns the number of columns in this matrix.
	 * </p>
	 * 
	 * @return
	 */
	public int numberOfColumns();

	/**
	 * <p>
	 * This method removes the last row from this KDDMatrix. Returns a boolean
	 * to indicate success or failure.
	 * </p>
	 * 
	 * @return
	 */
	public boolean deleteRow();

	/**
	 * <p>
	 * This method removes the last column from this KDDMatrix. Returns boolean
	 * to indicate success or failure.
	 * </p>
	 * 
	 * @return
	 */
	public boolean deleteColumn();

}