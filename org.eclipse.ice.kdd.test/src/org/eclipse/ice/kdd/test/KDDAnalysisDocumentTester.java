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
package org.eclipse.ice.kdd.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.kdd.KDDAnalysisDocument;
import org.eclipse.ice.kdd.test.fakeobjects.FakeStrategyFactory;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleData;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * The class tests the KDDAnalysisDocument.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class KDDAnalysisDocumentTester {
	/**
	 * <p>
	 * Reference to the KDDAnalysisDocument to unit test.
	 * </p>
	 * 
	 * 
	 */
	private KDDAnalysisDocument document;
	/**
	 * <p>
	 * Reference to a fake KDDStrategyFactory to inject into KDDAnalysisDocument
	 * for purposes of unit testing.
	 * </p>
	 * 
	 * 
	 */
	private FakeStrategyFactory fakeStrategyFactory;

	/**
	 * <p>
	 * Reference to some fake data to be analyzed
	 * </p>
	 * 
	 */
	private IDataProvider dataProvider;

	/**
	 * <p>
	 * Reference to some fake reference data.
	 * </p>
	 * 
	 */
	private IDataProvider refProvider;

	/**
	 * <p>
	 * checks the constructor.
	 * </p>
	 * 
	 */
	@Before
	public void beforeClass() {
		fakeStrategyFactory = new FakeStrategyFactory();
		document = new KDDAnalysisDocument(fakeStrategyFactory);
		dataProvider = new SimpleDataProvider();
		refProvider = new SimpleDataProvider();

		// Create some data for every method to use
		IData data = new SimpleData("feature", 1.0);
		ArrayList<IData> dataList = new ArrayList<IData>();

		// Create some reference data and some data to be analyzed
		// formatted as a 4x4 matrix
		for (int i = 0; i < 16; i++) {
			dataList.add(data);
		}
		((SimpleDataProvider) dataProvider).addData(dataList, "feature");
		((SimpleDataProvider) refProvider).addData(dataList, "feature");

	}

	/**
	 * <p>
	 * Test that the data can be loaded correctly and that the correct assets
	 * are set as available.
	 * </p>
	 * 
	 */
	@Test
	public void checkLoadData() {

		// Make sure we don't accept null data
		assertFalse(document.loadData((IDataProvider) null));
		assertFalse(document.loadReferenceData((IDataProvider) null));

		// Load data to be analyzed only
		assertTrue(document.loadData(dataProvider));

		// We should only have an asset for Raw Clustering at this point
		// Everything else depends on reference data.
		ArrayList<String> availableAssets = document.getAvailableAssets();
		assertEquals(1, availableAssets.size());

		// Check that we have the correct assets, only raw clustering now
		assertTrue(availableAssets.contains("Raw Clustering - KMeans"));

		// Now load the reference data, we should comparative clustering
		assertTrue(document.loadReferenceData(refProvider));

		// We should now have assets for raw and comparative clustering
		// as well as our anomaly detection methods.
		availableAssets = document.getAvailableAssets();
		assertEquals(7, availableAssets.size());

		// Check that we have all our assets available
		assertTrue(availableAssets.contains("Raw Clustering - KMeans"));
		assertTrue(availableAssets.contains("Comparative Clustering - KMeans"));
		assertTrue(availableAssets.contains("Anomaly Detection - Clustering"));
		assertTrue(availableAssets.contains("Anomaly Detection - Distance"));
		assertTrue(availableAssets.contains("Anomaly Detection - Density"));
		assertTrue(availableAssets
				.contains("Anomaly Detection - Parzen Window Estimate"));
		assertTrue(availableAssets
				.contains("Anomaly Detection - Local Outlier Factor"));

	}

	/**
	 * <p>
	 * Check that we can get and set the selected assets.
	 * </p>
	 * 
	 */
	@Test
	public void checkGetSetSelectedAssets() {

		// Local Declaration
		ArrayList<String> selected = new ArrayList<String>();

		// Set an empty set and make sure we get back
		// an empty set
		document.setSelectedAssets(selected);
		assertTrue(document.getSelectedAssets().isEmpty());

		// Load some data on the document to populate a list
		// of available assets to select from.
		assertTrue(document.loadReferenceData(refProvider));
		assertTrue(document.loadData(dataProvider));

		// Get the available assets found after loading
		ArrayList<String> available = document.getAvailableAssets();

		// Select the first available asset
		selected.add(available.get(0));
		document.setSelectedAssets(selected);

		// Assert that the asset was selected, while others weren't
		assertEquals(1, document.getSelectedAssets().size());
		assertTrue(available.get(0).equals(document.getSelectedAssets().get(0)));
		assertFalse(document.getSelectedAssets().contains(available.get(1)));
		assertFalse(document.getSelectedAssets().contains(available.get(2)));
		assertFalse(document.getSelectedAssets().contains(available.get(3)));
		assertFalse(document.getSelectedAssets().contains(available.get(4)));
		assertFalse(document.getSelectedAssets().contains(available.get(5)));
		assertFalse(document.getSelectedAssets().contains(available.get(6)));

		// Now select more than one
		selected.add(available.get(3));
		selected.add(available.get(4));

		// Set the new set of assets
		document.setSelectedAssets(selected);

		// Check that they were set correctly
		assertEquals(3, document.getSelectedAssets().size());
		assertTrue(document.getSelectedAssets().contains(available.get(3)));
		assertTrue(document.getSelectedAssets().contains(available.get(4)));
		assertTrue(document.getSelectedAssets().contains(available.get(0)));
		assertFalse(document.getSelectedAssets().contains(available.get(1)));
		assertFalse(document.getSelectedAssets().contains(available.get(2)));
		assertFalse(document.getSelectedAssets().contains(available.get(5)));
		assertFalse(document.getSelectedAssets().contains(available.get(6)));

	}

	/**
	 * <p>
	 * Check that we can create all the user's selected assets.
	 * </p>
	 * 
	 */
	@Test
	public void checkCreateSelectedAssets() {

		// Local Declarations
		ArrayList<String> selected = new ArrayList<String>();

		// Load some data
		assertTrue(document.loadReferenceData(refProvider));
		assertTrue(document.loadData(dataProvider));

		// Select an asset
		selected.add(document.getAvailableAssets().get(0));
		document.setSelectedAssets(selected);

		// Create the selected assets
		document.createSelectedAssets();

		// Get the asset and make sure its not null
		ArrayList<IAnalysisAsset> assets = document.getAllAssets();
		assertNotNull(assets);
		assertEquals(1, assets.size());

		// Now do it with more than 1 asset
		selected.add(document.getAvailableAssets().get(1));
		selected.add(document.getAvailableAssets().get(2));

		// Create the 3 assets
		document.createSelectedAssets();
		assets = document.getAllAssets();
		assertNotNull(assets);
		assertEquals(3, assets.size());

	}
}