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

import org.eclipse.ice.analysistool.IAnalysisDocument;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.IDataProvider;

import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategy;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategyFactory;

import java.net.URI;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * KDDAnalysisDocument is a realization of the IAnalysisDocument interface and
 * is responsible for the construction of user-specified data mining algorithms
 * and corresponding plots. It utilizes the Strategy pattern to decouple itself
 * from the actual construction of individual data mining algorithms.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class KDDAnalysisDocument implements IAnalysisDocument {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * KDDAnalysisDocument's reference to its list of created assets to be
	 * displayed to the user.
	 * </p>
	 * <p>
	 * KDDAnalysisDocument's reference to its list of created assets to be
	 * displayed to the user.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IAnalysisAsset> createdAssets;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A reference to this KDDAnalsyisDocument's data to be analyzed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private IDataProvider loadedData;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * A reference to this KDDAnalysisDocument's data to be used as reference
	 * for the data to be analyzed.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private IDataProvider referenceData;

	/**
	 * <!-- begin-UML-doc -->
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
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private KDDStrategyFactory strategyFactory;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The list of available assets populated whenany data is loaded.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> availableAssets;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> selectedAssets;

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
	public KDDAnalysisDocument() {
		// begin-user-code

		// Instantiate all class attributes
		createdAssets = new ArrayList<IAnalysisAsset>();
		availableAssets = new ArrayList<String>();
		selectedAssets = new ArrayList<String>();
		strategyFactory = new KDDStrategyFactory();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The constructor for direct injection of a specialization of the
	 * KDDStrategyFactory. Primarily used for testing.
	 * </p>
	 * <p>
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param factory
	 *            <p>
	 *            KDDStrategyFactory directly injected into KDDAnalysisDocument
	 *            for use in the construction of KDDStrategies.
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public KDDAnalysisDocument(KDDStrategyFactory factory) {
		// begin-user-code
		// Set the user-specified KDDStrategyFactory
		strategyFactory = factory;

		// Set all the array lists.
		createdAssets = new ArrayList<IAnalysisAsset>();
		availableAssets = new ArrayList<String>();
		selectedAssets = new ArrayList<String>();
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This method is called whenever data is loaded onto this
	 * KDDAnalysisDocument, and checks the data and its reference data for
	 * raw/comparative clustering and whether we can perform anomaly detection
	 * or not. It then populates the list of available assets.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void populateAvailableAssets() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Return the asset properties of the given available asset name as a list
	 * of Entries.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param assetName
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<Entry> getAssetPropertiesAsEntryList(String assetName) {
		// begin-user-code
		if (availableAssets.contains(assetName) && strategyFactory != null) {
			return strategyFactory.getStrategyProperties(assetName);
		} else {
			return null;
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#loadData(URI data)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean loadData(URI data) {
		// begin-user-code

		// Currently not accepting URI data
		return false;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#loadData(IDataProvider data)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean loadData(IDataProvider data) {
		// begin-user-code

		// Make sure this is valid data
		if (data != null) {
			// Set this documents data for analysis
			loadedData = data;
		} else {
			System.out.println("KDDDOC data was null");
			return false;
		}

		// Populate all available assets for this data
		// FIXME Maybe check if no strategies available
		// return a false?
		populateAvailableAssets();

		return true;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#loadReferenceData(URI data)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean loadReferenceData(URI data) {
		// begin-user-code

		// Currently not accepting URI data
		return false;

		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#loadReferenceData(IDataProvider data)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean loadReferenceData(IDataProvider data) {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getData()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public URI getData() {
		// begin-user-code
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getAvailableAssets()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getAvailableAssets() {
		// begin-user-code
		return availableAssets;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getSelectedAssets()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<String> getSelectedAssets() {
		// begin-user-code
		return selectedAssets;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#setSelectedAssets(ArrayList<String> assets)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setSelectedAssets(ArrayList<String> assets) {
		// begin-user-code
		// Make sure its a valid list of assets
		if (assets != null && !assets.isEmpty()) {
			// Set the selected assets
			selectedAssets = assets;
		}

		return;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#createSelectedAssets()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void createSelectedAssets() {
		// begin-user-code

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
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getTotalSlices()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public int getTotalSlices() {
		// begin-user-code
		return 0;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getSliceIdentifier(int sliceNumber)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public String getSliceIdentifier(int sliceNumber) {
		// begin-user-code
		return null;
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getAssetsAtSlice(int sliceNumber)
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IAnalysisAsset> getAssetsAtSlice(int sliceNumber) {
		// begin-user-code
		return getAllAssets();
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getAllAssets()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public ArrayList<IAnalysisAsset> getAllAssets() {
		// begin-user-code
		if (createdAssets != null && !createdAssets.isEmpty()) {
			return createdAssets;
		} else {
			return null;
		}
		// end-user-code
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getReferenceData()
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public URI getReferenceData() {
		// begin-user-code
		return null;
		// end-user-code
	}

}