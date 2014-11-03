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
package org.eclipse.ice.visitanalysistool.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.visitanalysistool.VisItAnalysisDocument;
import org.eclipse.ice.visitanalysistool.VisItAnalysisTool;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class is responsible for testing the VisitAnalysisDocument class.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author els
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class VisItAnalysisDocumentTester {

	/**
	 * The visit port number to use
	 */
	private static final int visitPortNumber = 12000;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private VisItAnalysisTool visItAnalysisTool;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes System properties used by all test operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@BeforeClass
	public static void beforeClass() {

		// Create a bin path from the user's home directory.
		String binPath = System.getProperty("user.home")
				+ System.getProperty("file.separator") + "visit"
				+ System.getProperty("file.separator") + "bin";

		// Auto set bin for windows.
		if (System.getProperty("os.name").contains("Windows")) {
			binPath = System.getProperty("user.home")
					+ System.getProperty("file.separator") + "visit";
		}

		// Assign System Property value for visit.binpath
		System.setProperty("visit.binpath", binPath);

		// Assign System Property value for visit.port
		System.setProperty("visit.port", String.valueOf(visitPortNumber));
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the VisItAnalysisDocument loadData, getData,
	 * getTotalSlices and getSliceIdentifier operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkDataLoading() {

		// begin-user-code

		// Tell the thread to sleep in order to give time inbetween tests.
		try {
			Thread.sleep(100);
		} catch (Exception e) {

		}

		// Create a URI from the test data file
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ "data" + separator + "AMPData.silo");
		URI data = dataFile.toURI();

		// Create a new VisItAnalysisTool
		visItAnalysisTool = new VisItAnalysisTool();

		// Create a VisItAnalysisDocument
		VisItAnalysisDocument visItAnalysisDocument = (VisItAnalysisDocument) visItAnalysisTool
				.createDocument(data);

		// Local declarations
		int totalSlices = 1;
		String sliceIdentifier = "1";

		// Run some tests
		assertEquals(visItAnalysisDocument.getData(), data);
		assertEquals(visItAnalysisDocument.getTotalSlices(), totalSlices);
		assertEquals(visItAnalysisDocument.getSliceIdentifier(1),
				sliceIdentifier);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the VisItAnalysisDocument getAvailableAssets
	 * operation.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAvailableAssets() {

		// begin-user-code

		// Create a URI from the test data file
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ "data" + separator + "AMPData.silo");
		URI data = dataFile.toURI();

		// Create a new VisItAnalysisTool
		visItAnalysisTool = new VisItAnalysisTool();

		// Create a VisItAnalysisDocument
		VisItAnalysisDocument visItAnalysisDocument = (VisItAnalysisDocument) visItAnalysisTool
				.createDocument(data);

		// Get an ArrayList of all known available assets in the AMPData.silo
		// file
		ArrayList<String> localAvailableAssets = getAssetListFromFile("AMPDataAvailableAssets.txt");

		// Get the available assets list from the VisItAnalysisDocument instance
		ArrayList<String> availableAssets = visItAnalysisDocument
				.getAvailableAssets();

		// Run some tests
		assertNotNull(availableAssets);
		assertEquals(localAvailableAssets.size(), availableAssets.size());
		assertTrue(availableAssets.containsAll(localAvailableAssets));

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation checks the VisItAnalysisDocument setSelectedAssets,
	 * getSelectedAssets, createSelectedAssets, getAssetsAtSlice and
	 * getAllAssets operations.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkAssetCreation() {
		// begin-user-code

		// Create a URI from the test data file
		String separator = System.getProperty("file.separator");
		File dataFile = new File(System.getProperty("user.dir") + separator
				+ "data" + separator + "AMPData.silo");
		URI data = dataFile.toURI();

		// Create a new VisItAnalysisTool
		visItAnalysisTool = new VisItAnalysisTool();

		// Create a VisItAnalysisDocument
		VisItAnalysisDocument visItAnalysisDocument = (VisItAnalysisDocument) visItAnalysisTool
				.createDocument(data);

		// Local declarations
		int totalSlices = 1;

		// Get a list of some known assets in the AMPData.silo file
		ArrayList<String> localSelectedAssets = getAssetListFromFile("AMPDataSelectedAssets.txt");

		// Set the selected assets in the instance of VisItAnalysisDocument
		visItAnalysisDocument.setSelectedAssets(localSelectedAssets);

		// Get the selected assets from visItAnalysisDocument
		ArrayList<String> selectedAssets = visItAnalysisDocument
				.getSelectedAssets();

		// Run some tests
		assertNotNull(selectedAssets);
		assertEquals(localSelectedAssets.size(), selectedAssets.size());
		assertTrue(selectedAssets.containsAll(localSelectedAssets));

		// Create the selected assets
		visItAnalysisDocument.createSelectedAssets();

		// Get all of the assets at slice 0
		ArrayList<IAnalysisAsset> assetsAtSlice = visItAnalysisDocument
				.getAssetsAtSlice(0);

		// Run some tests
		assertNotNull(assetsAtSlice);
		assertEquals(assetsAtSlice.size(), localSelectedAssets.size());
		for (IAnalysisAsset iaa : assetsAtSlice) {
			assertNotNull(iaa);
			assertTrue(localSelectedAssets.contains(iaa.getName()));
		}

		// Get all of the assets at all slices
		ArrayList<IAnalysisAsset> allAssets = visItAnalysisDocument
				.getAllAssets();

		// Run some tests
		assertNotNull(allAssets);
		assertEquals(allAssets.size(), localSelectedAssets.size() * totalSlices);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * This operation returns an ArrayList of asset names from a test file.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param filename
	 *            <p>
	 *            The filename of a test file containing a list of asset names.
	 *            </p>
	 * @return <p>
	 *         An ArrayList of asset names.
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<String> getAssetListFromFile(String filename) {
		// begin-user-code

		// Create a file object for the asset list
		File file = new File("../org.eclipse.ice.visitanalysistool.test/data/"
				+ filename);

		// Open the file and read the files contents into a string
		int i = (int) file.length();
		byte[] buffer = new byte[i];
		FileInputStream fis;
		String fileContents = "";
		try {
			fis = new FileInputStream(file);
			fis.read(buffer);
			fis.close();
			fileContents = new String(buffer);
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (IOException e) {
			fail(e.getMessage());
		}

		// Split the fileContents into a String array
		String[] array = fileContents.split("\n");

		// Convert the String array into an ArrayList
		ArrayList<String> assets = new ArrayList<String>();
		for (String string : array) {

			// Make sure that the string is not an empty string
			if (!string.trim().isEmpty()) {

				// Add the asset to the list
				assets.add(string);
			}
		}

		// Return the list
		return assets;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Closes the VisitAnalysisTool instance on the selected port.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@After
	public void after() {
		// begin-user-code
		visItAnalysisTool.close();
		// end-user-code
	}
}