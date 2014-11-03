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
 * <!-- begin-UML-doc -->
 * <p>
 * The DistanceMeasure interface gives users the ability to use a wide variety
 * of distance measures when performing KMeans clustering.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public interface DistanceMeasure {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Calculate the distance between the the two given vectors.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param vector1
	 * @param vector2
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public Double getDistance(KDDMatrix vector1, KDDMatrix vector2);
}