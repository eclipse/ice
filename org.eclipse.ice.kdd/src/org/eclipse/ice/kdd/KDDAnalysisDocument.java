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
package org.eclipse.ice.kdd;

import java.net.URI;
import java.util.ArrayList;

import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.IAnalysisDocument;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * KDDAnalysisDocument is a realization of the IAnalysisDocument interface and
 * is responsible for the construction of user-specified data mining algorithms
 * and corresponding plots. It utilizes the Strategy pattern to decouple itself
 * from the actual construction of individual data mining algorithms.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class KDDAnalysisDocument implements IAnalysisDocument {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(KDDAnalysisDocument.class);

	/**
	 * <p>
	 * KDDAnalysisDocument's reference to its list of created assets to be
	 * displayed to the user.
	 * </p>
	 * <p>
	 * KDDAnalysisDocument's reference to its list of created assets to be
	 * displayed to the user.
	 * </p>
	 * 
	 */
	private ArrayList<IAnalysisAsset> createdAssets;

	/**
	 * <p>
	 * A reference to this KDDAnalsyisDocument's data to be analyzed.
	 * </p>
	 * 
	 */
	private IDataProvider loadedData;

	/**
	 * <p>
	 * A reference to this KDDAnalysisDocument's data to be used as reference
	 * for the data to be analyzed.
	 * </p>
	 * 
	 */
	private IDataProvider referenceData;

	/**
	 * <p>
	 * KDDAnalysisDocument's reference to the KDDStrategyFactory, used to
	 * decouple KDDAnalysisDocument from the creation of specialized data mining
	 * algorithms.
	 * </p>
	 * <p>
	 * KDDAnalysisDocument's reference to the KDDStrategyFactory, used to
	 * decouple KDDAnalysisDocument from the creation of specialized data mining
	 * algorithms.
	 * </p>
	 * 
	 */
	private KDDStrategyFactory strategyFactory;

	/**
	 * <p>
	 * The list of available assets populated whenany data is loaded.
	 * </p>
	 * 
	 */
	private ArrayList<String> availableAssets;

	/**
	 * 
	 */
	private ArrayList<String> selectedAssets;

	/**
	 * <p>
	 * The constructor
	 * </p>
	 * 
	 */
	public KDDAnalysisDocument() {

		// Instantiate all class attributes
		createdAssets = new ArrayList<IAnalysisAsset>();
		availableAssets = new ArrayList<String>();
		selectedAssets = new ArrayList<String>();
		strategyFactory = new KDDStrategyFactory();
	}

	/**
	 * <p>
	 * The constructor for direct injection of a specialization of the
	 * KDDStrategyFactory. Primarily used for testing.
	 * </p>
	 * <p>
	 * </p>
	 * 
	 * @param factory
	 *            <p>
	 *            KDDStrategyFactory directly injected into KDDAnalysisDocument
	 *            for use in the construction of KDDStrategies.
	 *            </p>
	 */
	public KDDAnalysisDocument(KDDStrategyFactory factory) {
		// Set the user-specified KDDStrategyFactory
		strategyFactory = factory;

		// Set all the array lists.
		createdAssets = new ArrayList<IAnalysisAsset>();
		availableAssets = new ArrayList<String>();
		selectedAssets = new ArrayList<String>();
	}

	/**
	 * <p>
	 * This method is called whenever data is loaded onto this
	 * KDDAnalysisDocument, and checks the data and its reference data for
	 * raw/comparative clustering and whether we can perform anomaly detection
	 * or not. It then populates the list of available assets.
	 * </p>
	 * 
	 */
	private void populateAvailableAssets() {

		// Check this documents analysis data and
		// reference data for the various types of
		// assets we can create.

		// Clear the current list of assets
		availableAssets.clear();

		// Create a list of data
		ArrayList<IDataProvider> data = new ArrayList<IDataProvider>();
		// Add the loaded data, if it has been set
		if (loadedData != null) {
			data.add(loadedData);
		}

		// Add the reference data, if it has been set
		if (referenceData != null) {
			data.add(referenceData);
		}

		// Get the available asset names from the strategy factory
		availableAssets = strategyFactory.getAvailableStrategies(data);

		return;
	}

	/**
	 * <p>
	 * Return the asset properties of the given available asset name as a list
	 * of Entries.
	 * </p>
	 * 
	 * @param assetName
	 * @return
	 */
	public ArrayList<Entry> getAssetPropertiesAsEntryList(String assetName) {
		if (availableAssets.contains(assetName) && strategyFactory != null) {
			return strategyFactory.getStrategyProperties(assetName);
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#loadData(java.net.URI)
	 */
	@Override
	public boolean loadData(URI data) {

		// Currently not accepting URI data
		return false;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#loadData(org.eclipse.ice.analysistool.IDataProvider)
	 */
	@Override
	public boolean loadData(IDataProvider data) {

		// Make sure this is valid data
		if (data != null) {
			// Set this documents data for analysis
			loadedData = data;
		} else {
			logger.info("KDDDOC data was null");
			return false;
		}

		// Populate all available assets for this data
		// FIXME Maybe check if no strategies available
		// return a false?
		populateAvailableAssets();

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#loadReferenceData(java.net.URI)
	 */
	@Override
	public boolean loadReferenceData(URI data) {

		// Currently not accepting URI data
		return false;

	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#loadReferenceData(org.eclipse.ice.analysistool.IDataProvider)
	 */
	@Override
	public boolean loadReferenceData(IDataProvider data) {

		// Make sure this is valid data
		if (data != null) {
			// Set this documents reference data
			referenceData = data;
		} else {
			return false;
		}

		// Re-populate the list of available assets
		populateAvailableAssets();

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#getData()
	 */
	@Override
	public URI getData() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#getAvailableAssets()
	 */
	@Override
	public ArrayList<String> getAvailableAssets() {
		return availableAssets;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#getSelectedAssets()
	 */
	@Override
	public ArrayList<String> getSelectedAssets() {
		return selectedAssets;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#setSelectedAssets(java.util.ArrayList)
	 */
	@Override
	public void setSelectedAssets(ArrayList<String> assets) {
		// Make sure its a valid list of assets
		if (assets != null && !assets.isEmpty()) {
			// Set the selected assets
			selectedAssets = assets;
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#createSelectedAssets()
	 */
	@Override
	public void createSelectedAssets() {

		// We may have more than one set of data to use,
		// so create a map to pass to the strategy
		ArrayList<IDataProvider> dataToBeAnalyzed = new ArrayList<IDataProvider>();
		KDDStrategy strategy;

		// Clear the list of createdAssets
		createdAssets.clear();

		// Always add the loaded data, make sure its valid though.
		// If we don't have valid loadedData, just return, we won't create
		// anything
		if (loadedData != null) {
			dataToBeAnalyzed.add(loadedData);
		} else {
			return;
		}

		// The reference data should be optional,
		// make sure its valid, then add it
		if (referenceData != null) {
			dataToBeAnalyzed.add(referenceData);
		}

		// Loop over the user's selected assets, and create the
		// appropriate strategy for each
		for (String selected : selectedAssets) {
			// Create the strategy with the given set of data
			// it will return null if the data was invalid
			strategy = strategyFactory.createStrategy(selected,
					dataToBeAnalyzed);

			// Create the asset with the given strategy and
			// add the asset to the list of created assets
			if (strategy != null && strategy.executeStrategy()) {
				// Execute the Strategy and add it to the createdAssets
				createdAssets.add(strategy);
			}
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#getTotalSlices()
	 */
	@Override
	public int getTotalSlices() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#getSliceIdentifier(int)
	 */
	@Override
	public String getSliceIdentifier(int sliceNumber) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#getAssetsAtSlice(int)
	 */
	@Override
	public ArrayList<IAnalysisAsset> getAssetsAtSlice(int sliceNumber) {
		return getAllAssets();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#getAllAssets()
	 */
	@Override
	public ArrayList<IAnalysisAsset> getAllAssets() {
		if (createdAssets != null && !createdAssets.isEmpty()) {
			return createdAssets;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.analysistool.IAnalysisDocument#getReferenceData()
	 */
	@Override
	public URI getReferenceData() {
		return null;
	}

}