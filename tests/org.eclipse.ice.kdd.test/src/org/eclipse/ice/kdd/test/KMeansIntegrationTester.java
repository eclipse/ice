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

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.kdd.KDDAnalysisTool;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategyFactory;
import org.eclipse.ice.kdd.kddstrategy.kmeansclustering.RawKMeansBuilder;
import org.eclipse.ice.kdd.kddstrategy.kmeansclustering.RawKMeansStrategy;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleData;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;
import org.junit.Before;
import org.junit.Test;

import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.IAnalysisDocument;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class runs an integration test on KDDAnalysisTool to make sure we can
 * successfully create a KMeans clustering strategy and get meaningful data from
 * it.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class KMeansIntegrationTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private KDDAnalysisTool kddAnalysisTool;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private IDataProvider dataProvider;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private IProject project;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initialize necessary data
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Before
	public void beforeClass() {
		// begin-user-code
		kddAnalysisTool = new KDDAnalysisTool();
		KDDStrategyFactory factory = new KDDStrategyFactory();
		factory.registerStrategy(new RawKMeansBuilder());
		kddAnalysisTool.registerStrategyFactory(factory);
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Create a KMeans strategy and check it executes correctly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkRunRawKMeans() {
		// begin-user-code
		// Create a valid IDataProvider
		populateData();

		// Create the document
		IAnalysisDocument document = kddAnalysisTool
				.createDocument(dataProvider);

		// Get the available assets
		ArrayList<String> available = document.getAvailableAssets();

		// We should only have one, RawKMeansStrategy
		assertEquals(1, available.size());
		assertEquals("Raw KMeans Clustering", available.get(0));

		// Set the selected assets
		document.setSelectedAssets(available);

		// Create the Assets
		document.createSelectedAssets();

		// Make sure the asset was created
		ArrayList<IAnalysisAsset> assets = document.getAllAssets();
		assertEquals(1, assets.size());
		RawKMeansStrategy strategy = (RawKMeansStrategy) assets.get(0);
		assertEquals(2, strategy.getNumberOfClusters());
		assertTrue(project.getFile("rawKMeansCluster.txt").exists());
		assertNotNull(strategy.getURI());
		assertEquals(strategy.getURI(), project.getFile("rawKMeansCluster.txt")
				.getLocationURI());

		// Change the number of clusters to 3, this should
		// re-execute the strategy
		assertTrue(assets.get(0).setProperty("Number of Clusters", "3"));
		assertEquals(3, strategy.getNumberOfClusters());
		assertTrue(project.getFile("rawKMeansCluster_1.txt").exists());
		assertNotNull(strategy.getURI());
		assertEquals(strategy.getURI(),
				project.getFile("rawKMeansCluster_1.txt").getLocationURI());

		// Check setting an invalid property does nothing and
		// returns false
		assertFalse(assets.get(0).setProperty("Fake", "hello"));

		assertFalse(assets.get(0).setProperty("Visualization Dimension",
				"hello"));
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Fill the IDataProvider with valid data.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private void populateData() {
		// begin-user-code
		// Create a test IDataProvider
		dataProvider = new SimpleDataProvider();

		// Create IData lists to add to the providers
		ArrayList<IData> dataList = new ArrayList<IData>();
		ArrayList<IData> rowList = new ArrayList<IData>();
		ArrayList<IData> colList = new ArrayList<IData>();

		String workspaceName = "kddtestworkspace";
		String separator = System.getProperty("file.separator");
		project = null;

		try {
			// Get the project handle
			project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(workspaceName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/kddtestworkspace
				URI projURI = (new File(System.getProperty("user.dir")
						+ separator + workspaceName)).toURI();
				// Create the project description
				IProjectDescription description = ResourcesPlugin
						.getWorkspace().newProjectDescription(workspaceName);
				// Set the location of the project
				description.setLocationURI(projURI);
				// Create the project
				project.create(description, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen()) {
				project.open(null);
			}
		} catch (CoreException e) {
			// Catch for creating the project
			e.printStackTrace();
			fail();
		}

		// Delete any old kmeans files.
		try {
			for (IResource r : project.members()) {
				if (r.getType() == IResource.FILE
						&& r.getName().contains("rawKMeansCluster")) {
					r.delete(true, null);
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
			fail();
		}

		// Create a JadeD Files directory in project workspace
		IFolder dataFolder = project.getFolder("data");
		if (!dataFolder.exists()) {
			fail();
		}

		// Now read in the data
		ArrayList<Double> xValues = new ArrayList<Double>();
		ArrayList<Double> yValues = new ArrayList<Double>();
		IFile dataFile = dataFolder.getFile("data_2clusters.csv");
		FileInputStream input;
		try {
			input = (FileInputStream) dataFile.getContents();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));

			String line = null, x, y;
			while ((line = reader.readLine()) != null) {
				x = line.split(",")[0];
				y = line.split(",")[1];
				xValues.add(Double.parseDouble(x));
				yValues.add(Double.parseDouble(y));
			}

		} catch (CoreException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		IData tempDataX, tempDataY;
		for (int i = 0; i < xValues.size(); i++) {
			tempDataX = new SimpleData("Data", xValues.get(i));
			tempDataY = new SimpleData("Data", yValues.get(i));
			dataList.add(tempDataX);
			dataList.add(tempDataY);
		}

		rowList.add(new SimpleData("Number of Rows", 2000.0));
		colList.add(new SimpleData("Number of Columns", 2.0));

		((SimpleDataProvider) dataProvider).addData(dataList, "Data");
		((SimpleDataProvider) dataProvider).addData(rowList, "Number of Rows");
		((SimpleDataProvider) dataProvider).addData(colList,
				"Number of Columns");
		// end-user-code
	}
}