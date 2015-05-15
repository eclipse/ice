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
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.junit.Before;
import org.junit.Test;
import org.eclipse.ice.analysistool.IAnalysisAsset;
import org.eclipse.ice.analysistool.IAnalysisDocument;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.core.resources.IFolder;
import org.eclipse.ice.kdd.KDDAnalysisTool;
import org.eclipse.ice.kdd.kddstrategy.KDDStrategyFactory;
import org.eclipse.ice.kdd.kddstrategy.godfreystrategy.GodfreyBuilder;
import org.eclipse.ice.kdd.kddstrategy.godfreystrategy.GodfreyStrategy;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleData;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;

/**
 * <p>
 * This class is responsible for the integration testing of the
 * KDDAnalysisTool's GodfreyStrategy.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class GodfreyIntegrationTester {
	/**
	 * <p>
	 * Data to be analyzed.
	 * </p>
	 * 
	 */
	private IDataProvider provider;
	/**
	 * <p>
	 * Data to be used as reference.
	 * </p>
	 * 
	 */
	private IDataProvider refProvider;
	/**
	 * <p>
	 * Reference to the KDDAnalysisTool to test.
	 * </p>
	 * 
	 * 
	 */
	private KDDAnalysisTool kddAnalysisTool;

	/**
	 * <p>
	 * Reference to the IFolder containing test data files.
	 * </p>
	 * 
	 */
	private IFolder dataFolder;

	/**
	 * 
	 */
	@Before
	public void beforeClass() {
		kddAnalysisTool = new KDDAnalysisTool();
		KDDStrategyFactory factory = new KDDStrategyFactory();
		factory.registerStrategy(new GodfreyBuilder());
		kddAnalysisTool.registerStrategyFactory(factory);
	}

	/**
	 * <p>
	 * Check that we can successfully execute the GodfreyStrategy.
	 * </p>
	 * 
	 */
	@Test
	public void checkExecuteGodfreyStrategy() {
		generateData();

		// Create the document
		IAnalysisDocument document = kddAnalysisTool.createDocument(provider);

		// The GodfreyStrategy shouldn't be available yet
		// no reference data
		assertTrue(document.getAvailableAssets().isEmpty());

		// Load the reference data
		document.loadReferenceData(refProvider);

		ArrayList<String> available = document.getAvailableAssets();
		assertEquals(1, available.size());
		assertTrue(available.contains("Godfrey Fuel Pin Powers Strategy"));

		document.setSelectedAssets(available);

		// Create the assets
		document.createSelectedAssets();

		ArrayList<IAnalysisAsset> assets = document.getAllAssets();
		assertEquals(1, assets.size());

		URI uri = assets.get(0).getURI();

		// assertNotNull(uri);
		// assertEquals(dataFolder.getProject().getFile("godfreydata.txt")
		// .getLocationURI(), uri);

	}

	/**
	 * <p>
	 * Private utility method used to populate the IDataProviders
	 * </p>
	 * 
	 */
	private void generateData() {
		provider = new SimpleDataProvider();
		refProvider = new SimpleDataProvider();

		String workspaceName = "kddtestworkspace";;
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + workspaceName;
		IProject project = null;

		try {
			// Get the project handle
			project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(workspaceName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				URI projURI = (new File(userDir)).toURI();
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
						&& r.getName().contains("godfrey")) {
					r.delete(true, null);
				}
				if (r.getType() == IResource.FILE
						&& r.getName().contains("pindifferences")) {
					r.delete(true, null);
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
			fail();
		}

		// Create a JadeD Files directory in project workspace
		dataFolder = project.getFolder("data");
		if (!dataFolder.exists()) {
			fail();
		}

		IFile denovo = dataFolder.getFile("denovo_results2.csv");
		IFile keno = dataFolder.getFile("keno2.csv");
		FileInputStream input, refInput;
		ArrayList<IData> elements = new ArrayList<IData>();
		ArrayList<IData> refElements = new ArrayList<IData>();
		try {
			input = (FileInputStream) denovo.getContents();
			refInput = (FileInputStream) keno.getContents();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			BufferedReader refReader = new BufferedReader(
					new InputStreamReader(refInput));
			ArrayList<Double> pos;

			String line = null, refLine = null;
			String[] numbers, refNumbers;
			SimpleData data, refData;
			while ((line = reader.readLine()) != null) {
				numbers = line.split(",");
				for (int i = 0; i < numbers.length; i++) {
					pos = new ArrayList<Double>();
					data = new SimpleData("Data",
							Double.parseDouble(numbers[i]));
					data.setUncertainty(0.0);
					pos.add(0.0);
					pos.add(0.0);
					pos.add(1.0);
					data.setPosition(pos);
					pos.clear();
					elements.add(data);
				}
			}

			while ((refLine = refReader.readLine()) != null) {
				refNumbers = refLine.split(",");
				for (int i = 0; i < refNumbers.length; i++) {
					pos = new ArrayList<Double>();
					refData = new SimpleData("Data",
							Double.parseDouble(refNumbers[i]));
					refData.setUncertainty(0.0);
					pos.add(0.0);
					pos.add(0.0);
					pos.add(1.0);
					refData.setPosition(pos);
					pos.clear();
					refElements.add(refData);
				}
			}

		} catch (CoreException e) {
			e.printStackTrace();
			fail();
		} catch (IOException ex) {
			ex.printStackTrace();
			fail();
		} catch (NumberFormatException exx) {
			exx.printStackTrace();
			fail();
		}

		ArrayList<IData> rowNumber = new ArrayList<IData>();
		ArrayList<IData> colNumber = new ArrayList<IData>();
		ArrayList<IData> assemblyNumber = new ArrayList<IData>();

		rowNumber.add(new SimpleData("Number of Rows", 49.0));
		colNumber.add(new SimpleData("Number of Columns", 39.0));
		assemblyNumber.add(new SimpleData("Number of Assembly", 1.0));

		((SimpleDataProvider) provider).addData(elements, "Data");
		((SimpleDataProvider) refProvider).addData(refElements, "Data");

		((SimpleDataProvider) provider).addData(rowNumber, "Number of Rows");
		((SimpleDataProvider) provider).addData(colNumber, "Number of Columns");
		((SimpleDataProvider) provider).addData(assemblyNumber,
				"Number of Assemblies");

		((SimpleDataProvider) refProvider).addData(rowNumber, "Number of Rows");
		((SimpleDataProvider) refProvider).addData(colNumber,
				"Number of Columns");
		((SimpleDataProvider) refProvider).addData(assemblyNumber,
				"Number of Assemblies");

	}
}