/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.reactorAnalyzer.test;

import org.eclipse.ice.analysistool.IAnalysisDocument;
import org.eclipse.ice.analysistool.IDataProvider;

import java.net.URI;
import java.util.ArrayList;
import org.eclipse.ice.analysistool.IAnalysisAsset;

/**
 * <p>
 * A fake implementation of IAnalysisDocument that does not require any external
 * capabilities to run. It is used to test the ReactorAnalyzerBuilder and the
 * ReactorAnalyzer in a lightweight fashion.
 * </p>
 * 
 * @author Jay Jay Billings
 */
public class FakeAnalysisDocument implements IAnalysisDocument {
	/**
	 * <p>
	 * True if assets were created for a client, false otherwise.
	 * </p>
	 * 
	 */
	private boolean assetsCreated;
	/**
	 * <p>
	 * The asset that is returned by this document for testing.
	 * </p>
	 * 
	 */
	private FakeAnalysisAsset asset;

	/**
	 * <p>
	 * This operation resets the FakeAnalysisDocument to its initial state.
	 * </p>
	 * 
	 */
	public void reset() {

		// Reset attributes
		asset = null;
		assetsCreated = false;

		return;

	}

	/**
	 * <p>
	 * This operation returns true if assets were created by the
	 * FakeAnalysisDocument and false otherwise.
	 * </p>
	 * 
	 * @return <p>
	 *         True if assets were created, false otherwise.
	 *         </p>
	 */
	public boolean assetsCreated() {
		return assetsCreated;
	}

	/**
	 * <p>
	 * This operation returns the last FakeAnalysisAsset created by this
	 * document so that it can be checked in the test.
	 * </p>
	 * 
	 * @return <p>
	 *         The asset used by this FakeAnalysisDocument to fake out the
	 *         ReactorAnalyzer.
	 *         </p>
	 */
	public FakeAnalysisAsset getAsset() {
		return asset;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#loadData(URI data)
	 */
	public boolean loadData(URI data) {

		return true;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getData()
	 */
	public URI getData() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getAvailableAssets()
	 */
	public ArrayList<String> getAvailableAssets() {

		// Local Declarations
		ArrayList<String> assetTypes = new ArrayList<String>();

		// Setup up some fake analysis types
		assetTypes.add("Analyze deflector grid performance");
		assetTypes.add("Plot warp core plasma temperate vs. time");
		assetTypes.add("Show holo-emitter buffer contents");

		return assetTypes;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getSelectedAssets()
	 */
	public ArrayList<String> getSelectedAssets() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#setSelectedAssets(ArrayList<String> assets)
	 */
	public void setSelectedAssets(ArrayList<String> assets) {

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#createSelectedAssets()
	 */
	public void createSelectedAssets() {

		return;

	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getTotalSlices()
	 */
	public int getTotalSlices() {
		return 0;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getSliceIdentifier(int sliceNumber)
	 */
	public String getSliceIdentifier(int sliceNumber) {
		return String.valueOf(sliceNumber);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getAssetsAtSlice(int sliceNumber)
	 */
	public ArrayList<IAnalysisAsset> getAssetsAtSlice(int sliceNumber) {

		// Local Declaration
		ArrayList<IAnalysisAsset> assets = new ArrayList<IAnalysisAsset>();

		// Create the document if necessary
		if (asset == null) {
			asset = new FakeAnalysisAsset();
		}
		// Setup the list
		assets.add(asset);

		return assets;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see IAnalysisDocument#getAllAssets()
	 */
	public ArrayList<IAnalysisAsset> getAllAssets() {

		// Local Declaration
		ArrayList<IAnalysisAsset> assets = new ArrayList<IAnalysisAsset>();

		// Create the document if necessary
		if (asset == null) {
			asset = new FakeAnalysisAsset();
		}
		// Setup the list
		assets.add(asset);

		return assets;
	}

	@Override
	public boolean loadData(IDataProvider data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadReferenceData(URI data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean loadReferenceData(IDataProvider data) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public URI getReferenceData() {
		// TODO Auto-generated method stub
		return null;
	}
}