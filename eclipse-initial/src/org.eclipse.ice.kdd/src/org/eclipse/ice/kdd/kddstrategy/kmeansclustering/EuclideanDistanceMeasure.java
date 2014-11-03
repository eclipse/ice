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

import org.eclipse.ice.kdd.kddmath.KDDMatrix;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class EuclideanDistanceMeasure implements DistanceMeasure {
	/**
	 * (non-Javadoc)
	 * 
	 * @see DistanceMeasure#getDistance(KDDMatrix vector1, KDDMatrix vector2)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Double getDistance(KDDMatrix vector1, KDDMatrix vector2) {
		// begin-user-code

		if (vector1.numberOfRows() != vector2.numberOfRows()) {
			throw new IllegalArgumentException(
					"Error: Vector lengths not equal.");
		}
		// Initialize a sum variable
		Double sum = 0.0, temp = 0.0;

		// Sum up (c_i - a_i)^2
		for (int i = 0; i < vector1.numberOfRows(); i++) {
			temp = vector1.getElement(i, 0) - vector2.getElement(i, 0);
			sum += Math.pow(temp, 2);
		}

		return Math.sqrt(sum);
		// end-user-code
	}
}