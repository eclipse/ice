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
import java.util.ArrayList;

import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.IAnalysisDocument;
import org.eclipse.ice.analysistool.IDataProvider;

public class FakeAnalysisDocument implements IAnalysisDocument {

	@Override
	public boolean loadData(URI data) {
		// TODO Auto-generated method stub
		return false;
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
	public URI getData() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getAvailableAssets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getSelectedAssets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelectedAssets(ArrayList<String> assets) {
		// TODO Auto-generated method stub

	}

	@Override
	public void createSelectedAssets() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getTotalSlices() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSliceIdentifier(int sliceNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<IAnalysisAsset> getAssetsAtSlice(int sliceNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<IAnalysisAsset> getAllAssets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public URI getReferenceData() {
		// TODO Auto-generated method stub
		return null;
	}

}
