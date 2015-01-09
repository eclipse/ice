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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.kdd.kddmath.IDataMatrix;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;
import org.eclipse.ice.kdd.kddstrategy.godfreystrategy.GodfreySubStrategy;

/**
 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeSubStrategy extends GodfreySubStrategy {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method allows subclasses to implement a unique clustering or anomaly
	 * detection algorithm and produce a KDDAnalysisAsset for clients to display
	 * and manipulate.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean executeStrategy() {
		// begin-user-code
		try {
			uri = new URI("temp.txt");
		} catch (URISyntaxException e) {
			e.printStackTrace();
			System.err.println("FakeSubStrategy Failed in creating URI.");
			return false;
		}

		return true;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param pinPowers
	 * @param refPinPowers
	 * @param weights
	 * @param props
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FakeSubStrategy(HashMap<Integer, ArrayList<IDataMatrix>> pinPowers,
			HashMap<Integer, ArrayList<IDataMatrix>> refPinPowers,
			HashMap<Integer, ArrayList<KDDMatrix>> weights,
			HashMap<String, String> props) {
		// begin-user-code
		super(pinPowers, refPinPowers, weights);
		// end-user-code
	}
}