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
import java.util.HashMap;
import java.util.Random;

import org.eclipse.ice.kdd.kddmath.KDDMatrix;

/**
 * <p>
 * Cluster is a class that encapsulates a set of similar N-dimensional vector
 * data. It has methods to add and get data vectors, as well as query the
 * cluster index and number of elements.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class Cluster {
	/**
	 * <p>
	 * The index of this Cluster, used as a unique label for this Cluster
	 * amongst a set of Clusters.
	 * </p>
	 * 
	 */
	private int index;
	/**
	 * <p>
	 * Reference to a mapping of N-dimensional vectors represented as
	 * KDDMatrices each with number of columns equal to 1 and number of rows
	 * equal to N. The keys in the mapping are simply an integer index for each
	 * data vector.
	 * </p>
	 * 
	 */
	private HashMap<Integer, KDDMatrix> data;

	/**
	 * <p>
	 * The Constructor
	 * </p>
	 * 
	 * @param clusterIndex
	 */
	public Cluster(int clusterIndex) {
		index = clusterIndex;
		data = new HashMap<Integer, KDDMatrix>();
	}

	/**
	 * <p>
	 * Return the N-dimensional vector at the given index.
	 * </p>
	 * 
	 * @param index
	 * @return
	 */
	public KDDMatrix getVector(int index) {
		return data.get(new Integer(index));
	}

	/**
	 * <p>
	 * Add a new N-dimensional vector to the Cluster.
	 * </p>
	 * 
	 * @param vector
	 */
	public void addVector(KDDMatrix vector) {
		data.put(new Integer(data.size()), vector);
		return;
	}

	/**
	 * <p>
	 * Return the number of data elements in this Cluster.
	 * </p>
	 * 
	 * @return
	 */
	public int numberOfElements() {
		return data.size();
	}

	/**
	 * <p>
	 * Return the Cluster index.
	 * </p>
	 * 
	 * @return
	 */
	public int getClusterIndex() {
		return index;
	}

	/**
	 * <p>
	 * Return the center of mass vector for this Cluster.
	 * </p>
	 * 
	 * @param nRows
	 * @return
	 */
	public KDDMatrix getClusterMean(int nRows) {

		// Create a new KDDMatrix with all elements 0.0
		KDDMatrix sumVector = new KDDMatrix(nRows, 1);

		// Add up all vectors then divide them by the size of data
		for (KDDMatrix m : data.values()) {
			if (!sumVector.add(m)) {
				return null;
			}
		}

		// Divide by the number of elements in this cluster
		for (int i = 0; i < nRows; i++) {
			if (!data.isEmpty()) {
				sumVector.setElement(i, 0,
						sumVector.getElement(i, 0) / data.size());
			} else {
				sumVector.setElement(i, 0, sumVector.getElement(i, 0));
			}
		}

		return sumVector;
	}

	/**
	 * <p>
	 * Return a random center of mass, where each element in the N-dimensional
	 * vector must be within the range [min,max].
	 * </p>
	 * 
	 * @param nRows
	 * @param min
	 * @param max
	 * @return
	 */
	public KDDMatrix getRandomClusterMean(int nRows, Double min, Double max) {
		// Create a new mean vector
		KDDMatrix randomMean = new KDDMatrix(nRows, 1);
		Random random = new Random(System.currentTimeMillis());
		Double value = 0.0;

		// Set the elements to be a random number in the range [min,max]
		for (int i = 0; i < nRows; i++) {
			randomMean
					.setElement(i, 0, min + (max - min) * random.nextDouble());
		}

		// Return it
		return randomMean;
	}

	/**
	 * <p>
	 * This method removes all data from this cluster.
	 * </p>
	 * 
	 */
	public void clearData() {
		data.clear();
	}

	/**
	 * <p>
	 * This method checks that this data vector is contained in this Cluster.
	 * </p>
	 * 
	 * @param data
	 * @return
	 */
	public boolean contains(KDDMatrix data) {
		return false;
	}

	/**
	 * <p>
	 * Returns whether or not this Cluster contains any data elements.
	 * </p>
	 * 
	 * @return
	 */
	public boolean isEmpty() {
		return data.isEmpty();
	}

	/**
	 * <p>
	 * Return the vectors in this cluster.
	 * </p>
	 * 
	 * @return
	 */
	public ArrayList<KDDMatrix> getDataElements() {
		ArrayList<KDDMatrix> retList = new ArrayList<KDDMatrix>();

		for (KDDMatrix m : data.values()) {
			retList.add(m);
		}

		return retList;
	}
}