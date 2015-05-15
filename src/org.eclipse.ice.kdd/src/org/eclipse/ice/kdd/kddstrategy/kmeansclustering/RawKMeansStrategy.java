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

import java.util.ArrayList;

import org.eclipse.ice.analysistool.AnalysisAssetType;

import java.util.Properties;

import org.eclipse.ice.datastructures.form.AllowedValueType;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;

import java.net.URI;

import org.eclipse.ice.analysistool.IDataProvider;

/**
 * <p>
 * RawKMeansStrategy is a subclass of KDDStrategy that provides an
 * executeStrategy method that runs the KMeans clustering algorithm for a raw
 * data set (matrix). It utilizes the ClusterKDDMatrix to produce a set of
 * Clusters that themselves contain a list of N-dimensional vectors represented
 * as KDDMatrices. It is itself an IAnalysisAsset that creates a file of cluster
 * points and returns a URI of that file to be used by clients.
 * </p>
 * <p>
 * It expects an IDataProvider (can be set of IDataProviders) that provide a
 * feature list that contains three features: "Data", which corresponds to the
 * list of IData elements of the matrix to be clustered, in row major order;
 * "Number of Rows", which corresponds to an IData element detailing the number
 * of rows in the matrix; and "Number of Columns", which corresponds to an IData
 * element detailing the number of columns in the matrix to be clustered.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class RawKMeansStrategy extends KDDStrategy {
	/**
	 * <p>
	 * Reference to this RawKMeansStrategy's ClusterKDDMatrix that clusters its
	 * matrix data into N centroids, refining itself over a specified number of
	 * iterations.
	 * </p>
	 * 
	 * 
	 */
	private ClusterKDDMatrix matrixToCluster;

	/**
	 * <p>
	 * The constructor, takes an array of IDataProviders. By convention, the
	 * first IDataProvider of that array will be the loaded data to be analyzed.
	 * Any other IDataProviders will be reference or extra data.
	 * </p>
	 * 
	 * @param data
	 * @throws IllegalArgumentException
	 */
	public RawKMeansStrategy(ArrayList<IDataProvider> data)
			throws IllegalArgumentException {
		super("Raw KMeans", data);

		// Create the matrix to cluster, making
		// sure to catch the exception if the data is
		// not valid
		matrixToCluster = new ClusterKDDMatrix(data.get(0));

		// Initialize and populate the properties
		// map for this IAnalysisAsset
		properties.put("Number of Clusters", "2");
		properties.put("Number of Iterations", "10");
		properties.put("Visualization Dimension", "2");
		properties.put("Distance Measure", "Euclidean");

	}

	/**
	 * <p>
	 * Constructor used for injecting a specific ClusterKDDMatrix. Primarily
	 * used for unit testing.
	 * </p>
	 * 
	 * @param matrix
	 */
	public RawKMeansStrategy(ClusterKDDMatrix matrix) {
		super("Raw KMeans", null);
		matrixToCluster = matrix;

		properties.put("Number of Clusters", "2");
		properties.put("Number of Iterations", "10");
		properties.put("Visualization Dimension", "2");
		properties.put("Distance Measure", "Euclidean");

	}

	/**
	 * <p>
	 * This operation returns the asset's properties as a list of Entry objects.
	 * </p>
	 * 
	 * @return
	 */
	@Override
	public ArrayList<Entry> getPropertiesAsEntryList() {
		ArrayList<Entry> retList = new ArrayList<Entry>();

		Entry nClusters = new Entry() {
			@Override
			public void setup() {
				// Set the particulars
				this.objectName = "Number of Clusters";
				this.uniqueId = 1;
				this.objectDescription = "Indicate the number of clusters to produce.";
				// Set the data sources list
				allowedValueType = AllowedValueType.Continuous;
			}
		};
		retList.add(nClusters);

		Entry nIters = new Entry() {
			@Override
			public void setup() {
				// Set the particulars
				this.objectName = "Number of Iteration";
				this.uniqueId = 2;
				this.objectDescription = "Indicate the number of iterations to use in this clustering algorithm.";
				// Set the data sources list
				allowedValueType = AllowedValueType.Continuous;
			}
		};
		retList.add(nIters);

		Entry dm = new Entry() {
			@Override
			public void setup() {
				// Set the particulars
				this.objectName = "Distance Measure";
				this.uniqueId = 3;
				this.objectDescription = "Indicate the distance measure to use in this clustering algorithm.";

				// Set the data sources list
				allowedValueType = AllowedValueType.Discrete;
				allowedValues.add("Euclidean");
			}
		};
		retList.add(dm);

		return retList;
	}

	/**
	 * <p>
	 * Return the number of clusters after the kmeans clustering algorithm is
	 * executed.
	 * </p>
	 * 
	 * @return
	 */
	public int getNumberOfClusters() {
		return matrixToCluster.getNumberOfClusters();
	}

	@Override
	public boolean executeStrategy() {

		// This Strategies algorithm seeks to cluster the data
		// provided in the input matrix. Get the clusters here
		// watch for invalid properties input by the user.

		// Get a copy of the old matrix, just in case this
		// fails
		ClusterKDDMatrix oldMatrix = new ClusterKDDMatrix();
		oldMatrix.copy(matrixToCluster);

		try {
			// Get the NCentroids, refined over NIteratinos
			matrixToCluster.cluster(
					Integer.parseInt(properties.get("Number of Clusters")),
					Integer.parseInt(properties.get("Number of Iterations")));
		} catch (NumberFormatException ex) {
			// Fail if invalid properties
			ex.printStackTrace();
			return false;
		}

		// Create the Asset
		if (!createAsset()) {
			// If it failed, reset the old ClusterKDDMatrix
			matrixToCluster = oldMatrix;
			return false;
		} else {
			return true;
		}
	}

	protected boolean createAsset() {
		// Get the number of dimensions to visualize this data in
		String dimensions = properties.get("Visualization Dimension");
		int d = 0;
		// Convert it to an integer, if you can
		try {
			d = Integer.parseInt(dimensions);
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
			return false;
		}

		// We need to scale the clusters if its dimensionality
		// is greater than 3 (can't visualize it efficiently otherwise)
		if (matrixToCluster.numberOfColumns() > 3 && (d > 0 && d < 3)) {
			matrixToCluster.scaleData(d);
		}

		// Now construct the URI for ICE to use
		uri = matrixToCluster.plot();
		return true;
	}
}