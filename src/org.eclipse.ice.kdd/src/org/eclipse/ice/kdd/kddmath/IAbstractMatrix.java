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
 * <!-- begin-UML-doc -->
 * <p>
 * IAbstractMatrix is an interface for clients to realize that provides the
 * basic functionality of a nRows x nCols matrix of elements. It is generic so
 * that it can contain a matrix of any object.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface IAbstractMatrix<E> {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Get the element value at the given row and column index.Returns null if
	 * invalid index.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public E getElement(int rowIndex, int colIndex);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Set the value of the individual matrix element at index i,j.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param rowIndex
	 * @param colIndex
	 * @param value
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean setElement(int rowIndex, int colIndex, E value);

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method transposes this KDDMatrix.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void transpose();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the number of rows in this matrix
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int numberOfRows();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the number of columns in this matrix.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int numberOfColumns();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method removes the last row from this KDDMatrix. Returns a boolean
	 * to indicate success or failure.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean deleteRow();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method removes the last column from this KDDMatrix. Returns boolean
	 * to indicate success or failure.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean deleteColumn();

}