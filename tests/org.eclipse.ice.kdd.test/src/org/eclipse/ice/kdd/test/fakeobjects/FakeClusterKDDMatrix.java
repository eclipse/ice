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
package org.eclipse.ice.kdd.test.fakeobjects;

import org.eclipse.ice.kdd.kddstrategy.kmeansclustering.Cluster;
import org.eclipse.ice.kdd.kddstrategy.kmeansclustering.ClusterKDDMatrix;

import org.eclipse.ice.analysistool.IDataProvider;

/**
 * <p>
 * Fake ClusterKDDMatrix used for unit testing the RawKMeansStrategy.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class FakeClusterKDDMatrix extends ClusterKDDMatrix {

	public FakeClusterKDDMatrix(IDataProvider data)
			throws IllegalArgumentException {
		super(data);
	}

	/**
	 * <p>
	 * This method performs a KMeans cluster algorithm on this matrix-structured
	 * data set. It produces a map of cluster indices to vectors in an
	 * N-dimensional space represented as KDDMatrices with number of columns
	 * equal to 1 and number of rows equal to N. It takes as argument the number
	 * of cluster centroids to produce, as well as the number of iterations to
	 * use in refining the clusters.
	 * </p>
	 * 
	 * @param nCentroids
	 * @param nIterations
	 * @return
	 */
	public void cluster(int nCentroids, int nIterations) {
		clusters.clear();
		// We just need to return the correct number of Clusters
		// for this fake cluster matrix
		for (int i = 0; i < nCentroids; i++) {
			clusters.add(new Cluster(i));
		}
		return;
	}

	/**
	 * 
	 */
	public FakeClusterKDDMatrix() {
		super(null);

	}
}