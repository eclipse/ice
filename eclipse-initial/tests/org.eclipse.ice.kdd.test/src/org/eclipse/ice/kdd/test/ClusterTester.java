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

import java.util.Random;

import org.eclipse.ice.kdd.kddmath.KDDMatrix;
import org.eclipse.ice.kdd.kddstrategy.kmeansclustering.Cluster;
import org.junit.Before;
import org.junit.Test;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is used to unit test the Cluster class. It checks that we can add
 * and get vectors correctly, and that we can get a valid cluster mean vector.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class ClusterTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Cluster cluster;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes the data needed for the cluster to be tested.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Before
	public void beforeClass() {
		// begin-user-code
		cluster = new Cluster(1);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Tests that we can add and get vectors correctly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGetAddVectors() {
		// begin-user-code
		KDDMatrix vector = new KDDMatrix(10, 1);
		for (int i = 0; i < 10; i++) {
			assertTrue(vector.setElement(i, 0, (double) i));
		}

		cluster.addVector(vector);

		KDDMatrix getVector = cluster.getVector(0);
		assertNotNull(getVector);

		for (int i = 0; i < 10; i++) {
			assertTrue(getVector.getElement(i, 0).equals((double) i));
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks that we can get random cluster means as well as true cluster
	 * means.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkMeans() {
		// begin-user-code
		// just make up some random number of rows
		int nRows = 0;
		int nIterations = 100;
		Double min = -100.0;
		Double max = 100.0;
		Random random = new Random(System.currentTimeMillis());

		for (int j = 0; j < nIterations; j++) {

			// Create a random number of rows
			nRows = random.nextInt(1000);

			// Get a random mean
			KDDMatrix vector = cluster.getRandomClusterMean(nRows, min, max);

			// Make sure it is an N-vector
			assertEquals(1, vector.numberOfColumns());
			assertEquals(nRows, vector.numberOfRows());

			// Make sure all the values are in the proper range
			for (int i = 0; i < nRows; i++) {
				assertTrue(vector.getElement(i, 0) <= max
						&& vector.getElement(i, 0) >= min);
			}
		}

		// end-user-code
	}
}