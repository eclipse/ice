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

import org.eclipse.ice.kdd.kddmath.KDDMatrix;
import org.eclipse.ice.kdd.kddstrategy.kmeansclustering.EuclideanDistanceMeasure;
import org.junit.Test;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EuclideanDistanceMeasureTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private EuclideanDistanceMeasure dm;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkGetDistance() {
		// begin-user-code
		KDDMatrix vec1 = new KDDMatrix(2, 1);
		assertTrue(vec1.setElement(0, 0, 1.0));
		assertTrue(vec1.setElement(1, 0, 1.0));

		KDDMatrix vec2 = new KDDMatrix(2, 1);
		assertTrue(vec2.setElement(0, 0, 0.0));
		assertTrue(vec2.setElement(1, 0, 3.0));

		dm = new EuclideanDistanceMeasure();

		assertTrue(dm.getDistance(vec1, vec2).equals(Math.sqrt(5)));

		// end-user-code
	}
}