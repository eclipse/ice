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
package org.eclipse.ice.kdd.kddstrategy.godfreystrategy;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.kdd.kddmath.IDataMatrix;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The SubStrategyFactory is responsible for returning to the GodfreyStrategy
 * the correct user-specified GodfreySubStrategy.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class SubStrategyFactory {

	private PinPowerDifference pinDiff;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method returns the requested GodfreySubStrategy by string name.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public GodfreySubStrategy createSubStrategy(String name,
			HashMap<Integer, ArrayList<IDataMatrix>> pinPowers,
			HashMap<Integer, ArrayList<IDataMatrix>> refPinPowers,
			HashMap<Integer, ArrayList<KDDMatrix>> weights,
			HashMap<String, String> props) {
		// begin-user-code
		if ("Pin Power Difference".equals(name)) {
			pinDiff = new PinPowerDifference(pinPowers, refPinPowers, props);
			return pinDiff;

			// Note here down, pinDiff could be null
			// if the user decided not to calculated pin differences
			// Sub strategies should check
		} else if ("Axial Power".equals(name)) {
			return new AxialPower(pinDiff, pinPowers, refPinPowers, weights);
		} else if ("Radial Power".equals(name)) {
			return new RadialPower(pinDiff, pinPowers, refPinPowers, weights);
		} else {
			return null;
		}
		// end-user-code
	}
}