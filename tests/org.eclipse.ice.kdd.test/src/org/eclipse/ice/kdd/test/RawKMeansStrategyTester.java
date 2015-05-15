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
import java.util.Properties;

import org.eclipse.ice.kdd.kddstrategy.kmeansclustering.RawKMeansStrategy;
import org.eclipse.ice.kdd.test.fakeobjects.FakeClusterKDDMatrix;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleData;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;
import org.junit.Before;
import org.junit.Test;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;

/**
 * <p>
 * This class is used to unit test the RawKMeansStrategyTester. It verifies that
 * we can execute the KMeans strategy correctly and effectively.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class RawKMeansStrategyTester {
	/**
	 * 
	 */
	private RawKMeansStrategy kmeans;

	/**
	 * 
	 */
	@Before
	public void beforeClass() {
		// Initialize the number of rows and columns
		int nRows = 4;
		int nCols = 4;

		// Create a test IDataProvider
		IDataProvider provider = new SimpleDataProvider();
		IDataProvider refProvider = new SimpleDataProvider();
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
		((SimpleDataProvider) provider).addData(rowList, "Number of Rows");
		((SimpleDataProvider) provider).addData(colList, "Number of Columns");

		((SimpleDataProvider) refProvider).addData(refList, "Data");
		((SimpleDataProvider) refProvider).addData(rowList, "Number of Rows");
		((SimpleDataProvider) refProvider)
				.addData(colList, "Number of Columns");

		// Create a FakeClusterKDDMatrix
		FakeClusterKDDMatrix matrix = new FakeClusterKDDMatrix(provider);

		// Create the KMeans strategy
		kmeans = new RawKMeansStrategy(matrix);

	}

	/**
	 * 
	 */
	@Test
	public void checkExecuteStrategy() {
		assertTrue(kmeans.executeStrategy());
		assertEquals(2, kmeans.getNumberOfClusters());
	}

	/**
	 * <p>
	 * Checks that we can get and set the various asset properties.
	 * </p>
	 * 
	 */
	@Test
	public void checkGetSetProperties() {
		// Create the expected Properties
		Properties props = new Properties();
		props.put("Number of Clusters", "2");
		props.put("Number of Iterations", "10");
		props.put("Visualization Dimension", "2");
		props.put("Distance Measure", "Euclidean");

		// Assert they are equal to the actual props
		assertTrue(kmeans.getProperties().equals(props));

		// Test individual gets
		assertTrue("2".equals(kmeans.getProperty("Number of Clusters")));
		assertTrue("10".equals(kmeans.getProperty("Number of Iterations")));
		assertTrue("2".equals(kmeans.getProperty("Visualization Dimension")));
		assertTrue("Euclidean".equals(kmeans.getProperty("Distance Measure")));

		// Execute the strategy to produce the clusters
		assertTrue(kmeans.executeStrategy());

		// Change the number of clusters property
		assertTrue(kmeans.setProperty("Number of Clusters", "3"));
		// Assert that changing a property re-runs the strategy
		assertEquals(3, kmeans.getNumberOfClusters());

		// Test setting a non-existent property
		assertFalse(kmeans.setProperty("hello", "value"));
	}

	/**
	 * <p>
	 * Checks that we can get the assets properties as an Entry list.
	 * </p>
	 * 
	 */
	public void checkGetPropertiesAsEntryList() {
		// TODO Auto-generated method stub

	}

	/**
	 * <p>
	 * Tests that we can retrieve a valid URI for this IAnalysisAsset.
	 * </p>
	 * 
	 */
	public void checkGetURI() {
		// TODO Auto-generated method stub

	}
}