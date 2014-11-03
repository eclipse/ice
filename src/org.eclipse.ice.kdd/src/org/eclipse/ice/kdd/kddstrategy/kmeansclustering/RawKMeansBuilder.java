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
 * <!-- begin-UML-doc -->
 * <p>
 * The RawKMeansBuilder is a realization of the KDDStrategyBuilder and is used
 * to validate incoming data and return a new instance of the RawKMeansStrategy.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class RawKMeansBuilder implements IStrategyBuilder {

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the RawKMeansStrategy to build and return.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private RawKMeansStrategy kmeans = null;

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#build(ArrayList<IDataProvider> data)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDStrategy build(ArrayList<IDataProvider> data) {
		// begin-user-code
		if (kmeans != null) {
			return kmeans;
		} else {
			return null;
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#getStrategyName()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getStrategyName() {
		// begin-user-code
		return "Raw KMeans Clustering";
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#isAvailable(ArrayList<IDataProvider> dataToCheck)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isAvailable(ArrayList<IDataProvider> dataToCheck) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IStrategyBuilder#getStrategyPropertiesAsEntries()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Entry> getStrategyPropertiesAsEntries() {
		// begin-user-code
		ArrayList<Entry> retEntries = new ArrayList<Entry>();
		return retEntries;
		// end-user-code
	}
}