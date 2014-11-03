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
package org.eclipse.ice.visit.viewer.test;

import org.eclipse.ice.analysistool.IAnalysisDocument;
import org.eclipse.ice.analysistool.IDataProvider;

import java.net.URI;
import java.util.ArrayList;
import org.eclipse.ice.analysistool.IAnalysisAsset;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * A fake implementation of IAnalysisDocument that does not require any external
 * capabilities to run. It is used to test the VisItViewerBuilder and the
 * VisItViewer in a lightweight fashion.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author bkj
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class FakeAnalysisDocument implements IAnalysisDocument {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * True if assets were created for a client, false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private boolean assetsCreated;
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The asset that is returned by this document for testing.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FakeAnalysisAsset asset;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation resets the FakeAnalysisDocument to its initial state.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void reset() {
		// begin-user-code

		// Reset attributes
		asset = null;
		assetsCreated = false;

		return;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns true if assets were created by the
	 * FakeAnalysisDocument and false otherwise.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         True if assets were created, false otherwise.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public boolean assetsCreated() {
		// begin-user-code
		return assetsCreated;
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns the last FakeAnalysisAsset created by this
	 * document so that it can be checked in the test.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The asset used by this FakeAnalysisDocument to fake out the
	 *         VisItViewer.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public FakeAnalysisAsset getAsset() {
		// begin-user-code
		return asset;
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

		// Local Declarations
		ArrayList<String> assetTypes = new ArrayList<String>();

		// Setup up some fake analysis types
		assetTypes.add("Analyze deflector grid performance");
		assetTypes.add("Plot warp core plasma temperate vs. time");
		assetTypes.add("Show holo-emitter buffer contents");

		return assetTypes;
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
		// TODO Auto-generated method stub
		return null;
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
		return String.valueOf(sliceNumber);
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

		// Local Declaration
		ArrayList<IAnalysisAsset> assets = new ArrayList<IAnalysisAsset>();

		// Create the document if necessary
		if (asset == null) {
			asset = new FakeAnalysisAsset();
		}
		// Setup the list
		assets.add(asset);

		return assets;
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

		// Local Declaration
		ArrayList<IAnalysisAsset> assets = new ArrayList<IAnalysisAsset>();

		// Create the document if necessary
		if (asset == null) {
			asset = new FakeAnalysisAsset();
		}

		// Setup the list
		assets.add(asset);

		return assets;
		// end-user-code
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