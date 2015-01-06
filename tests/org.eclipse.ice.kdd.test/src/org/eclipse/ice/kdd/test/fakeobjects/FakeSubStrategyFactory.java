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

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.kdd.kddmath.IDataMatrix;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;
import org.eclipse.ice.kdd.kddstrategy.godfreystrategy.GodfreySubStrategy;
import org.eclipse.ice.kdd.kddstrategy.godfreystrategy.SubStrategyFactory;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeSubStrategyFactory extends SubStrategyFactory {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method returns the requested GodfreySubStrategy by string name.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param name
	 * @param pinPowers
	 * @param refPowers
	 * @param weights
	 * @param props
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public GodfreySubStrategy createSubStrategy(String name,
			HashMap<Integer, ArrayList<IDataMatrix>> pinPowers,
			HashMap<Integer, ArrayList<IDataMatrix>> refPowers,
			HashMap<Integer, ArrayList<KDDMatrix>> weights,
			HashMap<String, String> props) {
		// begin-user-code
		return new FakeSubStrategy(pinPowers, refPowers, weights, props);
		// end-user-code
	}
}