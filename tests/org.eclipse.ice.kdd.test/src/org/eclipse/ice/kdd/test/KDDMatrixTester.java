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
package org.eclipse.ice.kdd.test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.eclipse.ice.kdd.kddmath.KDDMatrix;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleData;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;

/**
 * <p>
 * This class tests the behavior and functionality of the KDDMatrix.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class KDDMatrixTester {
	/**
	 * 
	 */
	private KDDMatrix matrix;

	/**
	 * 
	 */
	private int nRows;
	/**
	 * 
	 */
	private int nCols;

	/**
	 * 
	 */
	private KDDMatrix refMatrix;

	/**
	 * <p>
	 * Reference to a set of data to test the KDDMatrix with.
	 * </p>
	 * 
	 */
	private IDataProvider provider;

	/**
	 * <p>
	 * Reference to a set of data to test the KDDMatrix with.
	 * </p>
	 * 
	 */
	private IDataProvider refProvider;

	private boolean debug;

	/**
	 * <p>
	 * checks the constructor.
	 * </p>
	 * <p>
	 * </p>
	 * 
	 */
	@Before
	public void beforeClass() {
		debug = false;
		// Initialize the number of rows and columns
		nRows = 4;
		nCols = 4;

		// Create a test IDataProvider
		provider = new SimpleDataProvider();
		refProvider = new SimpleDataProvider();
		IData temp1, temp2;

		// Create IData lists to add to the providers
		ArrayList<IData> dataList = new ArrayList<IData>();
		ArrayList<IData> refList = new ArrayList<IData>();
		ArrayList<IData> rowList = new ArrayList<IData>();
		ArrayList<IData> colList = new ArrayList<IData>();

		rowList.add(new SimpleData("Number of Rows", (double) nRows));
		colList.add(new SimpleData("Number of Columns", (double) nCols));

		// Add the data to the lists, with correct
		// positions
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				temp1 = new SimpleData("Data", 2.0);
				temp2 = new SimpleData("Data", 1.0);

				dataList.add(temp1);
				refList.add(temp2);
			}
		}

		// Now we have a matrix of all 2s, and another of all 1s.
		((SimpleDataProvider) provider).addData(dataList, "Data");
		((SimpleDataProvider) refProvider).addData(refList, "Data");

		((SimpleDataProvider) provider).addData(rowList, "Number of Rows");
		((SimpleDataProvider) provider).addData(colList, "Number of Columns");
		((SimpleDataProvider) refProvider).addData(rowList, "Number of Rows");
		((SimpleDataProvider) refProvider)
				.addData(colList, "Number of Columns");

	}

	/**
	 * <p>
	 * Check that constructing with a set of IData correctly creates the matrix
	 * </p>
	 * 
	 */
	@Test
	public void checkConstructFromData() {
		// Create the matrices
		try {
			matrix = new KDDMatrix(provider);
			refMatrix = new KDDMatrix(refProvider);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}

		try {
			KDDMatrix m = new KDDMatrix(provider);
		} catch (IllegalArgumentException ex) {
			// Do nothing, this is good
			assertNotNull(ex);
		}

	}

	/**
	 * <p>
	 * Test that we can correctly add and subtract two KDDMatrices.
	 * </p>
	 * 
	 */
	@Test
	public void checkAddSubtract() {
		try {
			matrix = new KDDMatrix(provider);
			refMatrix = new KDDMatrix(refProvider);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}

		// Check subtraction
		assertTrue(matrix.subtract(refMatrix));

		// We should now have a matrix of all 1s,
		// since we subtracted a matrix with all 1s from
		// a matrix with all 1s.
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				assertTrue(matrix.getElement(i, j).equals(1.0));
			}
		}

		// Check Addition
		assertTrue(matrix.add(refMatrix));

		// We should now have a matrix of all 2s,
		// since we added a matrix with all 1s from
		// a matrix with all 1s.
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				assertTrue(matrix.getElement(i, j).equals(2.0));
			}
		}

	}

	/**
	 * <p>
	 * Check that we can correctly set and get elements of a KDDMatrix
	 * </p>
	 * 
	 */
	@Test
	public void checkGetSetElements() {
		try {
			matrix = new KDDMatrix(provider);
			refMatrix = new KDDMatrix(refProvider);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}
		assertTrue(matrix.getElement(3, 3).equals(2.0));
		assertTrue(matrix.setElement(3, 3, 33.0));
		assertTrue(matrix.getElement(3, 3).equals(33.0));
		assertTrue(matrix.setElement(2, 2, 5.0));
		assertTrue(matrix.getElement(2, 2).equals(5.0));
		assertTrue(matrix.setElement(3, 3, 2.0));
		assertTrue(matrix.setElement(2, 2, 2.0));
	}

	/**
	 * <p>
	 * Check that we can correctly row or column normalize a KDDMatrix
	 * </p>
	 * 
	 */
	@Test
	public void checkRowColumnNormalize() {

		try {
			matrix = new KDDMatrix(provider);
			refMatrix = new KDDMatrix(refProvider);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}

		// Perform row normalization
		matrix.rowNormalize();

		// The matrix is all 2s, so each element should be 1/nRows
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				assertTrue(matrix.getElement(i, j).equals(((double) 1) / nCols));
			}
		}

	}

	/**
	 * <p>
	 * Check that we can correctly scale with an uncertainty matrix.
	 * </p>
	 * 
	 */
	@Test
	public void checkScaleUncertainty() {
		try {
			matrix = new KDDMatrix(provider);
			refMatrix = new KDDMatrix(refProvider);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}
		// Change the values of refMatrix to 6s
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				assertTrue(matrix.setElement(i, j, 2.0));
				assertTrue(refMatrix.setElement(i, j, 6.0));
			}
		}

		// Now scale by it
		assertTrue(matrix.scaleByUncertainty(refMatrix));
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				// Each element should be 2/6 = 1/3
				assertTrue(matrix.getElement(i, j).equals(((double) 1) / 3));
			}
		}

	}

	/**
	 * <p>
	 * Check that we can get the transpose of the KDDMatrix
	 * </p>
	 * 
	 */
	@Test
	public void checkTranspose() {

		// Create a set of 4 IData representing
		// a 2x2 Matrix
		ArrayList<IData> rowList = new ArrayList<IData>();
		ArrayList<IData> colList = new ArrayList<IData>();
		rowList.add(new SimpleData("Number of Rows", 2.0));
		colList.add(new SimpleData("Number of Columns", 2.0));

		IData d1 = new SimpleData("Data", 1.0);

		IData d2 = new SimpleData("Data", 2.0);

		IData d3 = new SimpleData("Data", 3.0);

		IData d4 = new SimpleData("Data", 4.0);

		// Add the IData to an ArrayList
		ArrayList<IData> list = new ArrayList<IData>();
		list.add(d1);
		list.add(d2);
		list.add(d3);
		list.add(d4);

		// Create an IDataProvider and add the IData list
		IDataProvider data = new SimpleDataProvider();
		((SimpleDataProvider) data).addData(list, "Data");

		((SimpleDataProvider) data).addData(rowList, "Number of Rows");
		((SimpleDataProvider) data).addData(colList, "Number of Columns");

		// Create the KDDMatrix from the IDataProvider
		KDDMatrix m = null;
		try {
			m = new KDDMatrix(data);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}

		// Assert that its not null, and the
		// elements were set correctly
		assertNotNull(m);
		assertTrue(m.getElement(0, 0).equals(1.0));
		assertTrue(m.getElement(0, 1).equals(2.0));
		assertTrue(m.getElement(1, 0).equals(3.0));
		assertTrue(m.getElement(1, 1).equals(4.0));
		printMatrix(m);

		// Transpose it
		m.transpose();

		// Assert that the values were transposed correctly
		assertTrue(m.getElement(0, 0).equals(1.0));
		assertTrue(m.getElement(0, 1).equals(3.0));
		assertTrue(m.getElement(1, 0).equals(2.0));
		assertTrue(m.getElement(1, 1).equals(4.0));
		printMatrix(m);

		rowList.clear();
		colList.clear();
		rowList.add(new SimpleData("Number of Rows", 2.0));
		colList.add(new SimpleData("Number of Columns", 3.0));

		// Create a more complicated matrix, a 2x3
		IData d5 = new SimpleData("Data", 5.0);
		IData d6 = new SimpleData("Data", 6.0);

		list.add(d5);
		list.add(d6);

		// Create a new IDataProvider to hold the 2x3 data
		IDataProvider prov2 = new SimpleDataProvider();
		((SimpleDataProvider) prov2).addData(list, "Data");

		// Create the matrix
		KDDMatrix m2 = null;
		try {
			m2 = new KDDMatrix(data);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}

		// Make sure its not null and the values were
		// set correctly
		assertNotNull(m2);
		assertEquals(3, m2.numberOfColumns());
		assertEquals(2, m2.numberOfRows());
		assertTrue(m2.getElement(0, 0).equals(1.0));
		assertTrue(m2.getElement(0, 1).equals(2.0));
		assertTrue(m2.getElement(0, 2).equals(3.0));
		assertTrue(m2.getElement(1, 0).equals(4.0));
		assertTrue(m2.getElement(1, 1).equals(5.0));
		assertTrue(m2.getElement(1, 2).equals(6.0));
		printMatrix(m2);

		// Transpose it
		m2.transpose();

		// Check that the values were set correctly
		assertEquals(2, m2.numberOfColumns());
		assertEquals(3, m2.numberOfRows());
		assertTrue(m2.getElement(0, 0).equals(1.0));
		assertTrue(m2.getElement(0, 1).equals(4.0));
		assertTrue(m2.getElement(1, 0).equals(2.0));
		assertTrue(m2.getElement(1, 1).equals(5.0));
		assertTrue(m2.getElement(2, 0).equals(3.0));
		assertTrue(m2.getElement(2, 1).equals(6.0));
		printMatrix(m2);

	}

	/**
	 * 
	 */
	@Test
	public void checkAddRemoveColumnsRows() {
		try {
			matrix = new KDDMatrix(provider);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}

		printMatrix(matrix);
		assertEquals(nRows + 1, matrix.addRow());
		assertEquals(nRows + 1, matrix.numberOfRows());
		printMatrix(matrix);

		for (int i = 0; i < matrix.numberOfColumns(); i++) {
			assertTrue(matrix.getElement(nRows, i).equals(0.0));
		}

		assertTrue(matrix.deleteRow());
		assertEquals(nRows, matrix.numberOfRows());
		printMatrix(matrix);

		assertEquals(nCols + 1, matrix.addColumn());
		assertEquals(nCols + 1, matrix.numberOfColumns());
		printMatrix(matrix);
		for (int i = 0; i < matrix.numberOfRows(); i++) {
			assertTrue(matrix.getElement(i, nCols).equals(0.0));
		}

		assertTrue(matrix.deleteColumn());
		assertEquals(nCols, matrix.numberOfColumns());
		printMatrix(matrix);

		assertTrue(matrix.deleteColumn());
		assertEquals(nCols - 1, matrix.numberOfColumns());
		printMatrix(matrix);
	}

	private void printMatrix(KDDMatrix m) {
		if (debug) {
			System.out.println();
			for (int i = 0; i < m.numberOfRows(); i++) {
				for (int j = 0; j < m.numberOfColumns(); j++) {
					System.out.print(m.getElement(i, j) + " ");
				}
				System.out.println("");
			}
		}
	}

	/**
	 * 
	 */
	@Test
	public void checkSetData() {

		KDDMatrix m = new KDDMatrix();
		assertEquals(0, m.numberOfColumns());
		assertEquals(0, m.numberOfRows());
		assertFalse(m.setElement(1, 1, 1.0));
		assertNull(m.getElement(1, 33));

		// Create a test IDataProvider
		IDataProvider provider = new SimpleDataProvider();
		IData temp1;

		// Create IData lists to add to the providers
		ArrayList<IData> dataList = new ArrayList<IData>();

		// Initialize the number of rows and columns
		nRows = 43;
		nCols = 45;

		// Initialize an array of x,y,z positions
		ArrayList<Double> position = new ArrayList<Double>();

		// Add the data to the lists, with correct
		// positions
		for (int i = 0; i < nRows; i++) {
			for (int j = 0; j < nCols; j++) {
				temp1 = new SimpleData("Data", (double) i * j);
				dataList.add(temp1);
			}
		}

		// Now we have a matrix of all 2s, and another of all 1s.
		((SimpleDataProvider) provider).addData(dataList, "Data");

		ArrayList<IData> rowList = new ArrayList<IData>();
		ArrayList<IData> colList = new ArrayList<IData>();

		rowList.add(new SimpleData("Number of Rows", (double) nRows));
		colList.add(new SimpleData("Number of Columns", (double) nCols));
		((SimpleDataProvider) provider).addData(rowList, "Number of Rows");
		((SimpleDataProvider) provider).addData(colList, "Number of Columns");

		try {
			m.setData(provider);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}

	}

	/**
	 * <p>
	 * This operation checks the KDDMatrix class to insure that its equal operation
	 * works.
	 * </p>
	 */
	@Test
	public void checkEquality() {

		// Local Declarations
		KDDMatrix copy = null;
		try {
			matrix = new KDDMatrix(provider);
			refMatrix = new KDDMatrix(refProvider);
			copy = new KDDMatrix(provider);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			fail();
		}
		

		// Test KDDMatrix.equals(), first one should be true, second false
		assertTrue(matrix.equals(copy));
		assertFalse(matrix.equals(refMatrix));

		// Check KDDMatrix.hashcode(), first one should be true, second true,
		// third false and fourth false
		assertEquals(matrix.hashCode(), copy.hashCode());
		assertTrue(matrix.hashCode() != refMatrix.hashCode());
		
	}
	
	/**
	 * <p>
	 * Tests that we can pull row and column vectors from this Matrix
	 * </p>
	 * 
	 */
	public void checkGetRowColumn() {
		// TODO Auto-generated method stub

	}
}