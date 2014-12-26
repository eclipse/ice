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
 * <!-- begin-UML-doc -->
 * <p>
 * IDataMatrix is a realization of the IAbstractMatrix with the IData template
 * parameter. It encapsulates a matrix of IData elements.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class IDataMatrix implements IAbstractMatrix<IData> {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The individual elements of this matrix. This is a list of n*m double
	 * values for a given matrix of size nxm.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected ArrayList<IData> elements;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the current number of rows in this matrix.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected int nRows;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the current number of columns in this matrix.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected int nCols;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the IDataProvider used to construct this KDDMatrix.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	protected IDataProvider dataProvider;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Create this IDataMatrix from a valid IDataProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IDataMatrix(IDataProvider data) {
		// begin-user-code
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor, used for constructing a IDataMatrix from a set of IData.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param rows
	 * @param cols
	 * @param elements
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IDataMatrix(int rows, int cols, ArrayList<IData> elements) {
		// begin-user-code
		nRows = rows;
		nCols = cols;
		this.elements = elements;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the position of given IData matrix element.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param i
	 * @param j
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Double> getElementPosition(int i, int j) {
		// begin-user-code
		return getElement(i, j).getPosition();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the given IData matrix element's units.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param i
	 * @param j
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getElementUnits(int i, int j) {
		// begin-user-code
		return getElement(i, j).getUnits();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return this IData matrix element's feature.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param i
	 * @param j
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getElementFeature(int i, int j) {
		// begin-user-code
		return getElement(i, j).getFeature();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the IData value at the given matrix element.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param i
	 * @param j
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Double getElementValue(int i, int j) {
		// begin-user-code
		return getElement(i, j).getValue();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the IData uncertainty at the given matrix element.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param i
	 * @param j
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Double getElementUncertainty(int i, int j) {
		// begin-user-code
		return getElement(i, j).getUncertainty();
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#getElement(int rowIndex, int colIndex)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public IData getElement(int rowIndex, int colIndex) {
		// begin-user-code
		// Check that the indices are valid
		if (rowIndex < 0 || colIndex < 0) {
			return null;
		}

		// Return if out of range
		if (rowIndex >= nRows || colIndex >= nCols) {
			return null;
		}

		return elements.get(nCols * rowIndex + colIndex);
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#setElement(int rowIndex, int colIndex, Double value)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setElement(int rowIndex, int colIndex, IData value) {
		// begin-user-code
		// TODO Auto-generated method stub
		return false;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#transpose()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void transpose() {
		// begin-user-code

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#numberOfRows()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int numberOfRows() {
		// begin-user-code
		return nRows;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#numberOfColumns()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int numberOfColumns() {
		// begin-user-code
		return nCols;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#deleteRow()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean deleteRow() {
		// begin-user-code
		// TODO Auto-generated method stub
		// Remove for the number of columns
		for (int i = 0; i < nCols; i++) {
			elements.remove(elements.size() - 1);
		}
		// Remove a row
		nRows -= 1;
		return true;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAbstractMatrix#deleteColumn()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean deleteColumn() {
		// begin-user-code
		// Remove for the number of rows
		for (int i = 0; i < nRows; i++) {
			elements.remove(((i + 1) * nCols) - i - 1);
		}
		// Remove a Column
		nCols -= 1;
		return true;
		// end-user-code
	}
}