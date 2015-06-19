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
 * The KDDMatrix is a class that encapsulates data representing an nxm matrix.
 * It can be constructed from a valid IDataProvider. The IDataProvider must
 * provide a feature list that contains three features: "Data", which
 * corresponds to the list of IData elements of the matrix, in row major order;
 * "Number of Rows", which corresponds to an IData element detailing the number
 * of rows in the matrix; and "Number of Columns", which corresponds to an IData
 * element detailing the number of columns in the matrix. KDDMatrix provides
 * methods for matrix arithmetic, transposition, normalizing rows and columns,
 * and scaling by an uncertainty matrix.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class KDDMatrix implements IAbstractMatrix<Double> {
	/**
	 * <p>
	 * The individual elements of this matrix. This is a list of n*m double
	 * values for a given matrix of size nxm.
	 * </p>
	 * 
	 */
	protected ArrayList<Double> elements;

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
	 * The constructor, takes a valid set of IData and constructs this matrix
	 * </p>
	 * 
	 * @param data
	 * @throws IllegalArgumentException
	 */
	public KDDMatrix(IDataProvider data) throws IllegalArgumentException {

		// Initialize the elements array
		elements = new ArrayList<Double>();

		// Initialize the number of rows and columns
		nCols = 0;
		nRows = 0;

		// Create the matrix, this will
		// throw an exception if the data isn't valid
		createMatrix(data);

		// Save the given IDataProvider
		dataProvider = data;

		// We did it!
		return;

	}

	/**
	 * <p>
	 * This method subtracts the given matrix from this KDDMatrix: this -
	 * matToSubtract.
	 * </p>
	 * 
	 * @param matToSubtract
	 * @return
	 */
	public boolean subtract(IAbstractMatrix<Double> matToSubtract) {

		// Make sure the size of matToSubtract is valid
		if (matToSubtract.numberOfColumns() != nCols
				&& matToSubtract.numberOfRows() != nRows) {
			return false;
		}

		// Perform the subtraction
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				setElement(i, j,
						getElement(i, j) - matToSubtract.getElement(i, j));
			}
		}

		return true;
	}

	/**
	 * <p>
	 * This method adds the given KDDMatrix to this one.
	 * </p>
	 * 
	 * @param matToAdd
	 * @return
	 */
	public boolean add(IAbstractMatrix<Double> matToAdd) {
		// Make sure the size of matToSubtract is valid
		if (matToAdd.numberOfColumns() != nCols
				&& matToAdd.numberOfRows() != nRows) {
			return false;
		}

		// Perform the addition
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				setElement(i, j, getElement(i, j) + matToAdd.getElement(i, j));
			}
		}

		return true;

	}

	/**
	 * <p>
	 * This method row normalizes the matrix, ie, sums each row and divides each
	 * row element by that sum.
	 * </p>
	 * 
	 */
	public void rowNormalize() {

		// Local ArrayList to collect all row sums
		ArrayList<Double> rowSums = new ArrayList<Double>();

		// Initialize one rowSum to 0.0
		Double rowSum = 0.0;

		// Calculate the sum of each row, then add it to the
		// array list
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				rowSum = rowSum + getElement(i, j);
			}
			// Add the sum to the list
			rowSums.add(rowSum);
			// Reset the sum
			rowSum = 0.0;
		}

		// Now go through each element and divide it
		// by its row sum
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				setElement(i, j, getElement(i, j) / rowSums.get(i));
			}
		}

		return;
	}

	/**
	 * <p>
	 * This method column normalizes the matrix, ie, sums each column and
	 * divides each column element by that sum.
	 * </p>
	 * 
	 */
	public void columnNormalize() {
		// FIXME coming soon to a theater near you
	}

	/**
	 * <p>
	 * This operation scales the difference of the data by the point uncertainty
	 * at that point.
	 * </p>
	 * 
	 * @param uncertainty
	 * @return
	 */
	public boolean scaleByUncertainty(KDDMatrix uncertainty) {
		// Make sure the size of matToSubtract is valid
		if (uncertainty.numberOfColumns() != nCols
				&& uncertainty.numberOfRows() != nRows) {
			return false;
		}

		// Divide each element by the corresponding
		// uncertainty element
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				setElement(i, j,
						getElement(i, j) / uncertainty.getElement(i, j));
			}
		}

		return true;

	}

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
	@Override
	public Double getElement(int rowIndex, int colIndex) {
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
	@Override
	public boolean setElement(int rowIndex, int colIndex, Double value) {
		// Return if value is null
		if (value == null) {
			return false;
		}

		// Return if negative
		if (rowIndex < 0 || colIndex < 0) {
			return false;
		}

		// Return if out of range
		if (rowIndex >= nRows || colIndex >= nCols) {
			return false;
		}

		// Set the value
		elements.set(nCols * rowIndex + colIndex, value);

		return true;
	}

	/**
	 * <p>
	 * Returns the number of rows in this matrix
	 * </p>
	 * 
	 * @return
	 */
	@Override
	public int numberOfRows() {
		return nRows;
	}

	/**
	 * <p>
	 * Returns the number of columns in this matrix.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	public int numberOfColumns() {
		return nCols;
	}

	/**
	 * <p>
	 * This method transposes this KDDMatrix.
	 * </p>
	 * 
	 */
	@Override
	public void transpose() {

		// Save the old number of columns and rows
		// so we can switch them
		int oldNRows = nRows;
		int oldNCols = nCols;

		// Easy bc they are just vectors
		if (nRows == 1 || nCols == 1) {
			// Just switch them
			nRows = oldNCols;
			nCols = oldNRows;
		} else {

			// This method assumes that the IDataProvider
			// has already been validated, since the KDDMatrix wouldn't
			// have been constructed otherwise.

			// Get the IData elements
			ArrayList<IData> dataElements = dataProvider
					.getDataAtCurrentTime("Data");

			// Transpose swaps the number of rows and cols
			nRows = oldNCols;
			nCols = oldNRows;

			KDDMatrix oldMatrix = new KDDMatrix(
					(ArrayList<Double>) elements.clone(), oldNRows, oldNCols);

			elements.clear();
			for (int i = 0; i < nRows * nCols; i++) {
				elements.add(0.0);
			}

			for (int i = 0; i < nRows; i++) {
				for (int j = 0; j < nCols; j++) {
					setElement(i, j, oldMatrix.getElement(j, i));
				}
			}

		}
		return;
	}

	/**
	 * <p>
	 * The nullary constructor. Initializes a 1x1 matrix. Clients can set the
	 * IData for this KDDMatrix immediately after using this constructor with
	 * the setData method.
	 * </p>
	 * 
	 */
	public KDDMatrix() {
		elements = new ArrayList<Double>();
		nRows = 0;
		nCols = 0;
	}

	/**
	 * <p>
	 * This method sets or resets this KDDMatrix matrix elements with the given
	 * set of IData.
	 * </p>
	 * 
	 * @param data
	 * @throws IllegalArgumentException
	 */
	public void setData(IDataProvider data) throws IllegalArgumentException {
		// Create this matrix, will throw exception
		// if data is bad
		createMatrix(data);

		// Save the IDataProvider
		dataProvider = data;

		return;
	}

	/**
	 * <p>
	 * This method adds a row to this KDDMatrix. The elements are initialized to
	 * zero and it returns the rows index.
	 * </p>
	 * 
	 * @return
	 */
	public int addRow() {
		// Add for the number of columns
		for (int i = 0; i < nCols; i++) {
			elements.add(0.0);
		}
		// Add to the row
		nRows += 1;
		return nRows;
	}

	/**
	 * <p>
	 * This method adds a column to this KDDMatrix. The elements are initialized
	 * to 0 and it returns the columns index.
	 * </p>
	 * 
	 * @return
	 */
	public int addColumn() {
		// Add for the number of rows
		for (int i = 0; i < nRows; i++) {
			elements.add(((i + 1) * nCols) + i, 0.0);
		}
		// Add to the cols
		nCols += 1;

		return nCols;
	}

	/**
	 * <p>
	 * This method removes the last row from this KDDMatrix. Returns a boolean
	 * to indicate success or failure.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	public boolean deleteRow() {
		// Remove for the number of columns
		for (int i = 0; i < nCols; i++) {
			elements.remove(elements.size() - 1);
		}
		// Remove a row
		nRows -= 1;
		return true;
	}

	/**
	 * <p>
	 * This method removes the last column from this KDDMatrix. Returns boolean
	 * to indicate success or failure.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	public boolean deleteColumn() {
		// Remove for the number of rows
		for (int i = 0; i < nRows; i++) {
			elements.remove(((i + 1) * nCols) - i - 1);
		}
		// Remove a Column
		nCols -= 1;
		return true;
	}

	/**
	 * <p>
	 * This utility private method is used by the constructor and setData method
	 * to check that the data is valid and populate the matrices elements.
	 * </p>
	 * 
	 * @param data
	 */
	private void createMatrix(IDataProvider data) {
		// We expect an IDataProvider with 3 features:
		// Data: ArrayList of the matrix elements
		// Number of Rows: ArrayList with one IData whose value is the integer
		// number of matrix rows
		// Number of Columns: ArrayList with one IData whose value is the
		// integer number of matrix columns
		if (data.getFeatureList().isEmpty()
				|| !data.getFeatureList().contains("Data")
				|| !data.getFeatureList().contains("Number of Rows")
				|| !data.getFeatureList().contains("Number of Columns")) {
			throw new IllegalArgumentException(
					"KDDMatrix requires an "
							+ "IDataProvider with only 3 features. \"Data\" which corresponds to "
							+ "a 1D ArrayList of IData representing the row major elements. \"Number of Rows\" "
							+ "detailing the number of matrix rows. And \"Number of Columns\" detailing "
							+ "the number of matrix columns. This IDataProvider has "
							+ data.getFeatureList().size() + " features.");
		}

		// We also assume that the IDataProvider has only one time slice,
		// so just get the data at the only time, the current time.
		ArrayList<IData> dataElements = data.getDataAtCurrentTime("Data");
		ArrayList<IData> rowData = data.getDataAtCurrentTime("Number of Rows");
		ArrayList<IData> colData = data
				.getDataAtCurrentTime("Number of Columns");

		// Make sure the rowData only has one IData element
		if (rowData.size() != 1) {
			throw new IllegalArgumentException(
					"Invalid number of row IData elements. Must be only one element in ArrayList, not "
							+ rowData.size());
		}
		// Make sure the colData only has one IData element
		if (colData.size() != 1) {
			throw new IllegalArgumentException(
					"Invalid number of column IData elements. Must be only one element in ArrayList, not "
							+ rowData.size());
		}
		// Get those double values
		double rowValue = rowData.get(0).getValue();
		double colValue = colData.get(0).getValue();

		// Make sure the rowValue is whole number
		if (rowValue != (int) rowValue) {
			throw new IllegalArgumentException(
					"Invalid value for the number of matrix rows. Must be an integer.");
		}
		// Make sure the colValue is a whole number
		if (colValue != (int) colValue) {
			throw new IllegalArgumentException(
					"Invalid value for the number of matrix columns. Must be an integer.");
		}
		// Set the values
		nRows = (int) rowValue;
		nCols = (int) colValue;

		// Make sure we have the correct number of matrix elements
		if (dataElements.size() != nRows * nCols) {
			throw new IllegalArgumentException(
					"Invalid number of data elements. Must have nRows * nCols data elements.");
		}
		// Add the elements
		for (int i = 0; i < nRows * nCols; i++) {
			elements.add(dataElements.get(i).getValue());
		}

		return;
	}

	/**
	 * <p>
	 * Return the IDataProvider used to construct this KDDMatrix.
	 * </p>
	 * 
	 * @return
	 */
	public IDataProvider getDataProvider() {
		return dataProvider;
	}

	/**
	 * <p>
	 * Get the N-dimensional (N = nRows) row vector at the given index. Returned
	 * as a KDDMatrix with number of columns equal to N and number of rows equal
	 * to 1.
	 * </p>
	 * 
	 * @param index
	 * @return
	 */
	public KDDMatrix getRow(int index) {
		KDDMatrix retVector = new KDDMatrix(1, nCols);
		for (int i = 0; i < nCols; i++) {
			if (!retVector.setElement(0, i, getElement(index, i))) {
				System.out.println("THIS WQAS FALSE");
				return null;
			}
		}

		return retVector;
	}

	/**
	 * <p>
	 * Get the N-dimensional (N = nRows) column vector at the given index.
	 * Returned as a KDDMatrix with number of columns equal to 1 and number of
	 * rows equal to N.
	 * </p>
	 * 
	 * @param index
	 * @return
	 */
	public KDDMatrix getColumn(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * <p>
	 * This constructor creates a KDDMatrix of size nRows by nCols with all
	 * elements equal to 0.0.
	 * </p>
	 * 
	 * @param nRows
	 * @param nCols
	 */
	public KDDMatrix(int nRows, int nCols) {
		this.nRows = nRows;
		this.nCols = nCols;
		elements = new ArrayList<Double>();
		for (int i = 0; i < this.nRows * this.nCols; i++) {
			elements.add(0.0);
		}
	}

	/**
	 * <p>
	 * This method indicates whether or not this KDDMatrix and the given
	 * KDDMatrix argument are equal.
	 * </p>
	 * 
	 * @param matrix
	 * @return
	 */
	public boolean equals(KDDMatrix matrix) {
		// First make sure the incoming matrix is the right
		// size
		if (matrix.numberOfColumns() != nCols || matrix.numberOfRows() != nRows) {
			return false;
		}
		// Then make sure all the elements are the same
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				if (!matrix.getElement(i, j).equals(getElement(i, j))) {
					return false;
				}
			}
		}

		// If we make it here, they are equal
		return true;
	}
	
	/**
	 * <p>
	 * This operation returns the hashcode value of the KDDMatrix.
	 * </p>
	 * 
	 * @return <p>
	 *         The hashcode
	 *         </p>
	 */
	@Override
	public int hashCode() {
		
		// Local Declarations
		int hash = 8;

		// Compute the hashcode
		hash = 31 * hash + this.nCols;
		hash = 31 * hash + this.nRows;
		hash = 31 * hash + this.elements.hashCode();
		hash = 31 * hash + this.dataProvider.hashCode();
		
		// Done, return
		return hash;
	}

	/**
	 * <p>
	 * The Constructor.
	 * </p>
	 * 
	 * @param elements
	 * @param nRows
	 * @param nCols
	 */
	public KDDMatrix(ArrayList<Double> elements, int nRows, int nCols) {
		this.nRows = nRows;
		this.nCols = nCols;
		this.elements = elements;
	}

	/**
	 * <p>
	 * Create a copy of this KDDMatrix and return it.
	 * </p>
	 * 
	 * @param other
	 */
	public void copy(KDDMatrix other) {
		dataProvider = other.dataProvider;
		nRows = other.nRows;
		nCols = other.nCols;
		elements = other.elements;
		return;
	}

	public void zeroMatrix() {
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				setElement(i, j, 0.0);
			}
		}
	}

	public void printMatrix() {
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				System.out.print(getElement(i, j) + " ");
			}
			System.out.println();
		}
	}

}