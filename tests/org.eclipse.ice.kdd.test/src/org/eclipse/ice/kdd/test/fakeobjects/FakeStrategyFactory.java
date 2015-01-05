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

import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategyFactory;
import org.eclipse.ice.kdd.test.fakeobjects.FakeStrategy;

import org.eclipse.ice.analysistool.IDataProvider;
import static org.eclipse.ice.kdd.test.fakeobjects.FakeStrategy.*;
import static org.junit.Assert.assertTrue;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This is a fake KDDStrategyFactory to be used in unit testing the
 * KDDAnalysisDocument.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author Alex McCaskey
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeStrategyFactory extends KDDStrategyFactory {

	@Override
	public KDDStrategy createStrategy(String selectedAsset,
			ArrayList<IDataProvider> data) {
		// begin-user-code
		return new FakeStrategy();
		// end-user-code
	}

	@Override
	public ArrayList<String> getAvailableStrategies(
			ArrayList<IDataProvider> data) {
		// begin-user-code
		// Create a List to hold the valid, available strategies
		ArrayList<String> retList = new ArrayList<String>();

		retList.add("Raw Clustering - KMeans");
		if (data.size() > 1) {
			retList.add("Comparative Clustering - KMeans");
			retList.add("Anomaly Detection - Clustering");
			retList.add("Anomaly Detection - Distance");
			retList.add("Anomaly Detection - Density");
			retList.add("Anomaly Detection - Parzen Window Estimate");
			retList.add("Anomaly Detection - Local Outlier Factor");
		}
		// Return the list
		return retList;
		// end-user-code
	}

}