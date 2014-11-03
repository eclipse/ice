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
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.kdd.kddmath.KDDMatrix;
import org.eclipse.ice.kdd.kddstrategy.godfreystrategy.GodfreyStrategy;
import org.eclipse.ice.kdd.test.fakeobjects.FakeSubStrategyFactory;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleData;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * This class unit tests the GodfreyStrategy. It checks that it executes its
 * strategy correctly, and can set and get properties and URI.
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author aqw
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class GodfreyStrategyTester {
	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private GodfreyStrategy godfreyStrategy;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IDataProvider> providers;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the number of rows in the pin powers matrix.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int nPinRows;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the number of columns in the pin powers matrix.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int nPinCols;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the number of Axial levels.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int nAxial;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Reference to the number of Assemblies.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private int nAssemblies;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private HashMap<Integer, ArrayList<KDDMatrix>> weights;

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private FakeSubStrategyFactory fakeSubFactory;

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes any data
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Before
	public void beforeClass() {
		// begin-user-code

		// Set the number of rows and cols, axial levels
		// and number of assemblies
		nPinRows = 49;
		nPinCols = 39;
		nAxial = 1;
		nAssemblies = 1;

		// Populate the providers
		providers = this.getTestData();
		fakeSubFactory = new FakeSubStrategyFactory();

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks that we can execute this strategy correctly
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	@Test
	public void checkExecuteStrategy() {
		// begin-user-code

		// Create the strategy, which could throw an
		// exception. If it does, fail
		godfreyStrategy = new GodfreyStrategy(fakeSubFactory, nPinRows,
				nPinCols, nAxial, nAssemblies, providers.get(0)
						.getDataAtCurrentTime("Data"), providers.get(1)
						.getDataAtCurrentTime("Data"));

		// Make sure the name is set correctly
		assertTrue(godfreyStrategy.getName().equals(
				"Godfrey Pin Difference Strategy"));

		// Execute and verify it was successful
		assertTrue(godfreyStrategy.executeStrategy());

		// Check that the essential data was set correctly
		assertEquals(nAssemblies, godfreyStrategy.getNumberOfAssemblies());
		assertEquals(nPinRows, godfreyStrategy.getNumberOfPinRows());
		assertEquals(nPinCols, godfreyStrategy.getNumberOfPinCols());
		assertEquals(nAxial, godfreyStrategy.getNumberOfAxialLevels());

		// Make sure we created a valid URI.
		assertNotNull(godfreyStrategy.getURI());

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks that we can get and set properties correctly.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void checkGetSetProperties() {
		// begin-user-code
		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Checks that we can get this IAnalyisAsset's properties as a list of
	 * Entries.
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void checkGetPropertiesAsEntryList() {
		// begin-user-code
		// TODO Auto-generated method stub

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc --> <!-- end-UML-doc -->
	 * 
	 * @return
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private ArrayList<IDataProvider> getTestData() {
		// begin-user-code

		// Need some data....
		IDataProvider provider = new SimpleDataProvider();
		IDataProvider refProvider = new SimpleDataProvider();

		String workspaceName = "kddtestworkspace";
		String separator = System.getProperty("file.separator");
		IProject project = null;

		try {
			// Get the project handle
			project = ResourcesPlugin.getWorkspace().getRoot()
					.getProject(workspaceName);
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
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
						&& r.getName().contains("godfrey")) {
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

		ArrayList<IDataProvider> retProvs = new ArrayList<IDataProvider>();
		retProvs.add(provider);
		retProvs.add(refProvider);

		return retProvs;

		// end-user-code
	}

}