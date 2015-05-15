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
package org.eclipse.ice.kdd.kddstrategy.kmeansclustering;

import static org.eclipse.ice.kdd.kddstrategy.kmeansclustering.RawKMeansStrategy.*;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.IStrategyBuilder;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;

/**
 * <p>
 * The RawKMeansBuilder is a realization of the KDDStrategyBuilder and is used
 * to validate incoming data and return a new instance of the RawKMeansStrategy.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class RawKMeansBuilder implements IStrategyBuilder {

	/**
	 * <p>
	 * Reference to the RawKMeansStrategy to build and return.
	 * </p>
	 * 
	 */
	private RawKMeansStrategy kmeans = null;

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#build(ArrayList<IDataProvider> data)
	 */
	public KDDStrategy build(ArrayList<IDataProvider> data) {
		if (kmeans != null) {
			return kmeans;
		} else {
			return null;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#getStrategyName()
	 */
	public String getStrategyName() {
		return "Raw KMeans Clustering";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#isAvailable(ArrayList<IDataProvider> dataToCheck)
	 */
	public boolean isAvailable(ArrayList<IDataProvider> dataToCheck) {

		// Make sure we have only one data provider
		if (dataToCheck.isEmpty() || dataToCheck.size() != 1) {
			return false;
		}

		// Get the data provider
		IDataProvider provider = dataToCheck.get(0);

		// Make sure we have features to tell us the number of rows and columns
		// as well as the list of matrix elements
		if (!provider.getFeatureList().contains("Data")
				|| !provider.getFeatureList().contains("Number of Rows")
				|| !provider.getFeatureList().contains("Number of Columns")) {
			return false;
		}

		// Make sure those features, since they exist contain
		// the correct number of elements
		ArrayList<IData> rowElements = provider
				.getDataAtCurrentTime("Number of Rows");
		ArrayList<IData> colElements = provider
				.getDataAtCurrentTime("Number of Columns");
		ArrayList<IData> dataElements = provider.getDataAtCurrentTime("Data");

		if (rowElements.size() != 1 || colElements.size() != 1) {
			return false;
		}

		// We've made it here, the nRows and nCols are valid
		double nDRows = rowElements.get(0).getValue();
		double nDCols = colElements.get(0).getValue();
		if (nDRows != (int) rowElements.get(0).getValue()) {
			return false;
		}
		if (nDCols != (int) colElements.get(0).getValue()) {
			return false;
		}

		// Cast them to integers
		int nRows = (int) nDRows;
		int nCols = (int) nDCols;

		// Make sure we have the correct number of data elements
		if (dataElements.size() != nRows * nCols) {
			return false;
		}

		// Convert them to Doubles for ClusterKDDMatrix
		ArrayList<Double> elements = new ArrayList<Double>();
		for (IData data : dataElements) {
			elements.add(data.getValue());
		}

		// Create the Strategy
		kmeans = new RawKMeansStrategy(new ClusterKDDMatrix(elements, nRows,
				nCols));

		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#getStrategyPropertiesAsEntries()
	 */
	public ArrayList<Entry> getStrategyPropertiesAsEntries() {
		ArrayList<Entry> retEntries = new ArrayList<Entry>();
		return retEntries;
	}
}