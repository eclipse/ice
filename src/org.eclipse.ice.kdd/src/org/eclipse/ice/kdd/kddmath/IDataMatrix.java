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

import java.util.ArrayList;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;

/**
 * <p>
 * IDataMatrix is a realization of the IAbstractMatrix with the IData template
 * parameter. It encapsulates a matrix of IData elements.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class IDataMatrix implements IAbstractMatrix<IData> {
	/**
	 * <p>
	 * The individual elements of this matrix. This is a list of n*m double
	 * values for a given matrix of size nxm.
	 * </p>
	 * 
	 */
	protected ArrayList<IData> elements;

	/**
	 * <p>
	 * Reference to the current number of rows in this matrix.
	 * </p>
	 * 
	 */
	protected int nRows;

	/**
	 * <p>
	 * Reference to the current number of columns in this matrix.
	 * </p>
	 * 
	 */
	protected int nCols;

	/**
	 * <p>
	 * Reference to the IDataProvider used to construct this KDDMatrix.
	 * </p>
	 * 
	 */
	protected IDataProvider dataProvider;

	/**
	 * <p>
	 * Create this IDataMatrix from a valid IDataProvider.
	 * </p>
	 * 
	 * @param data
	 */
	public IDataMatrix(IDataProvider data) {
	}

	/**
	 * <p>
	 * The constructor, used for constructing a IDataMatrix from a set of IData.
	 * </p>
	 * 
	 * @param rows
	 * @param cols
	 * @param elements
	 */
	public IDataMatrix(int rows, int cols, ArrayList<IData> elements) {
		nRows = rows;
		nCols = cols;
		this.elements = elements;
	}

	/**
	 * <p>
	 * Return the position of given IData matrix element.
	 * </p>
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public ArrayList<Double> getElementPosition(int i, int j) {
		return getElement(i, j).getPosition();
	}

	/**
	 * <p>
	 * Return the given IData matrix element's units.
	 * </p>
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public String getElementUnits(int i, int j) {
		return getElement(i, j).getUnits();
	}

	/**
	 * <p>
	 * Return this IData matrix element's feature.
	 * </p>
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public String getElementFeature(int i, int j) {
		return getElement(i, j).getFeature();
	}

	/**
	 * <p>
	 * Return the IData value at the given matrix element.
	 * </p>
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public Double getElementValue(int i, int j) {
		return getElement(i, j).getValue();
	}

	/**
	 * <p>
	 * Return the IData uncertainty at the given matrix element.
	 * </p>
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public Double getElementUncertainty(int i, int j) {
		return getElement(i, j).getUncertainty();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#getElement(int rowIndex, int colIndex)
	 */
	public IData getElement(int rowIndex, int colIndex) {
		// Check that the indices are valid
		if (rowIndex < 0 || colIndex < 0) {
			return null;
		}

		// Return if out of range
		if (rowIndex >= nRows || colIndex >= nCols) {
			return null;
		}

		return elements.get(nCols * rowIndex + colIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.kdd.kddmath.IAbstractMatrix#setElement(int, int,
	 * java.lang.Object)
	 */
	public boolean setElement(int rowIndex, int colIndex, IData value) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#transpose()
	 */
	public void transpose() {

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#numberOfRows()
	 */
	public int numberOfRows() {
		return nRows;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#numberOfColumns()
	 */
	public int numberOfColumns() {
		return nCols;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#deleteRow()
	 */
	public boolean deleteRow() {
		// TODO Auto-generated method stub
		// Remove for the number of columns
		for (int i = 0; i < nCols; i++) {
			elements.remove(elements.size() - 1);
		}
		// Remove a row
		nRows -= 1;
		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#deleteColumn()
	 */
	public boolean deleteColumn() {
		// Remove for the number of rows
		for (int i = 0; i < nRows; i++) {
			elements.remove(((i + 1) * nCols) - i - 1);
		}
		// Remove a Column
		nCols -= 1;
		return true;
	}
}