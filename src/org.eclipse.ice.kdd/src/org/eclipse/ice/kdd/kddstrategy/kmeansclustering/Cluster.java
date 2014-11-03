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

import static org.eclipse.ice.kdd.kddmath.KDDMatrix.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.eclipse.ice.kdd.kddmath.KDDMatrix;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Cluster is a class that encapsulates a set of similar N-dimensional vector
 * data. It has methods to add and get data vectors, as well as query the
 * cluster index and number of elements.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class Cluster {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The index of this Cluster, used as a unique label for this Cluster
	 * amongst a set of Clusters.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int index;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to a mapping of N-dimensional vectors represented as
	 * KDDMatrices each with number of columns equal to 1 and number of rows
	 * equal to N. The keys in the mapping are simply an integer index for each
	 * data vector.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private HashMap<Integer, KDDMatrix> data;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The Constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param clusterIndex
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Cluster(int clusterIndex) {
		// begin-user-code
		index = clusterIndex;
		data = new HashMap<Integer, KDDMatrix>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the N-dimensional vector at the given index.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param index
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDMatrix getVector(int index) {
		// begin-user-code
		return data.get(new Integer(index));
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Add a new N-dimensional vector to the Cluster.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param vector
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void addVector(KDDMatrix vector) {
		// begin-user-code
		data.put(new Integer(data.size()), vector);
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the number of data elements in this Cluster.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int numberOfElements() {
		// begin-user-code
		return data.size();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the Cluster index.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getClusterIndex() {
		// begin-user-code
		return index;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the center of mass vector for this Cluster.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param nRows
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDMatrix getClusterMean(int nRows) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return a random center of mass, where each element in the N-dimensional
	 * vector must be within the range [min,max].
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param nRows
	 * @param min
	 * @param max
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDMatrix getRandomClusterMean(int nRows, Double min, Double max) {
		// begin-user-code
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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method removes all data from this cluster.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void clearData() {
		// begin-user-code
		data.clear();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method checks that this data vector is contained in this Cluster.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param data
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean contains(KDDMatrix data) {
		// begin-user-code
		return false;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns whether or not this Cluster contains any data elements.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean isEmpty() {
		// begin-user-code
		return data.isEmpty();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the vectors in this cluster.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<KDDMatrix> getDataElements() {
		// begin-user-code
		ArrayList<KDDMatrix> retList = new ArrayList<KDDMatrix>();

		for (KDDMatrix m : data.values()) {
			retList.add(m);
		}

		return retList;
		// end-user-code
	}
}