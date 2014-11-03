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
package org.eclipse.ice.kdd.kddstrategy;

import static org.eclipse.ice.kdd.kddstrategy.KDDStrategy.*;
import org.eclipse.ice.analysistool.IDataProvider;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;
import org.eclipse.ice.kdd.kddstrategy.kmeansclustering.RawKMeansStrategy;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * The KDDStrategyFactory is responsible for decoupling the KDDAnalysisDocument
 * from the act of creating specialized KDDStrategies. This allows the document
 * to solely focus on producing IAnalysisAssets, and not the actual method of
 * those asset's creation, or the strategy's implemented data mining algorithm.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class KDDStrategyFactory {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to this KDDStrategies collection of KDDStrategyBuilders.
	 * </p>
	 * 
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private HashMap<String, IStrategyBuilder> builders;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDStrategyFactory() {
		// begin-user-code
		builders = new HashMap<String, IStrategyBuilder>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method creates the requested KDDStrategy with the given mapping of
	 * IDataProviders. The keys of the map are simple a descriptive name of the
	 * corresponding IDataProvider.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param selectedAsset
	 * @param data
	 * @return
	 * @throws IllegalArgumentException
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDStrategy createStrategy(String selectedAsset,
			ArrayList<IDataProvider> data) throws IllegalArgumentException {
		// begin-user-code

		// Make sure the data is valid
		if (data.isEmpty()) {
			return null;
		}

		// Get the builder
		IStrategyBuilder builder = builders.get(selectedAsset);

		// Make sure its not null
		if (builder != null) {
			return builder.build(data);
		} else {
			return null;
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method, used by the underlying OSGi framework, registers any
	 * available KDDStrategyBuilder with this KDDStrategyFactory so it can be
	 * used in data validation and the creation of KDDStrategies.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param strategyBuilder
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void registerStrategy(IStrategyBuilder strategyBuilder) {
		// begin-user-code
		if (strategyBuilder != null) {
			System.out
					.println("KDDStrategyFactory Message: Registering Builder "
							+ strategyBuilder.getStrategyName());
			builders.put(strategyBuilder.getStrategyName(), strategyBuilder);
		}
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return all available assets (or strategies) based on their response to
	 * data validation.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param dataProviders
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getAvailableStrategies(
			ArrayList<IDataProvider> dataProviders) {
		// begin-user-code
		if (builders.isEmpty()) {
			return null;
		}

		// Create a List to hold the valid, available strategies
		ArrayList<String> retList = new ArrayList<String>();

		// Loop over the builders and validate the data
		for (IStrategyBuilder builder : builders.values()) {
			// Make sure the Strategy can use this data
			if (builder.isAvailable(dataProviders)) {
				// If so, add it to the list of
				// available strategies
				retList.add(builder.getStrategyName());
			}
		}

		// Return the list
		return retList;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param strategyBuilder
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void unregisterStrategy(IStrategyBuilder strategyBuilder) {
		// begin-user-code
		System.out
				.println("KDDStrategyFactory Message: Un registering Builder "
						+ strategyBuilder.getStrategyName());
		builders.remove(strategyBuilder);
		return;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @param availableAsset
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Entry> getStrategyProperties(String availableAsset) {
		// begin-user-code
		if (builders.keySet().contains(availableAsset)) {
			return builders.get(availableAsset)
					.getStrategyPropertiesAsEntries();
		} else {
			return null;
		}
		// end-user-code
	}
}