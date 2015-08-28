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

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * The KDDStrategyFactory is responsible for decoupling the KDDAnalysisDocument
 * from the act of creating specialized KDDStrategies. This allows the document
 * to solely focus on producing IAnalysisAssets, and not the actual method of
 * those asset's creation, or the strategy's implemented data mining algorithm.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class KDDStrategyFactory {
	
	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(KDDStrategyFactory.class);
	
	/**
	 * <p>
	 * Reference to this KDDStrategies collection of KDDStrategyBuilders.
	 * </p>
	 * 
	 * 
	 */
	private HashMap<String, IStrategyBuilder> builders;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 */
	public KDDStrategyFactory() {
		builders = new HashMap<String, IStrategyBuilder>();
	}

	/**
	 * <p>
	 * This method creates the requested KDDStrategy with the given mapping of
	 * IDataProviders. The keys of the map are simple a descriptive name of the
	 * corresponding IDataProvider.
	 * </p>
	 * 
	 * @param selectedAsset
	 * @param data
	 * @return
	 * @throws IllegalArgumentException
	 */
	public KDDStrategy createStrategy(String selectedAsset,
			ArrayList<IDataProvider> data) throws IllegalArgumentException {

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
	}

	/**
	 * <p>
	 * This method, used by the underlying OSGi framework, registers any
	 * available KDDStrategyBuilder with this KDDStrategyFactory so it can be
	 * used in data validation and the creation of KDDStrategies.
	 * </p>
	 * 
	 * @param strategyBuilder
	 */
	public void registerStrategy(IStrategyBuilder strategyBuilder) {
		if (strategyBuilder != null) {
			logger.info("KDDStrategyFactory Message: "
					+ "Registering Builder "
					+ strategyBuilder.getStrategyName());
			builders.put(strategyBuilder.getStrategyName(), strategyBuilder);
		}
	}

	/**
	 * <p>
	 * Return all available assets (or strategies) based on their response to
	 * data validation.
	 * </p>
	 * 
	 * @param dataProviders
	 * @return
	 */
	public ArrayList<String> getAvailableStrategies(
			ArrayList<IDataProvider> dataProviders) {
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
	}

	/**
	 * 
	 * @param strategyBuilder
	 */
	public void unregisterStrategy(IStrategyBuilder strategyBuilder) {
		logger.info("KDDStrategyFactory Message: "
						+ "Un registering Builder "
						+ strategyBuilder.getStrategyName());
		builders.remove(strategyBuilder);
		return;
	}

	/**
	 * 
	 * @param availableAsset
	 * @return
	 */
	public ArrayList<Entry> getStrategyProperties(String availableAsset) {
		if (builders.keySet().contains(availableAsset)) {
			return builders.get(availableAsset)
					.getStrategyPropertiesAsEntries();
		} else {
			return null;
		}
	}
}