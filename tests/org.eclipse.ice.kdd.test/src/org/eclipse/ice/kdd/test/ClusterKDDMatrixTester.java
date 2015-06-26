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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.kdd.kddstrategy.kmeansclustering.ClusterKDDMatrix;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleData;
import org.eclipse.ice.kdd.test.fakeobjects.SimpleDataProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * This class is used to unit test the ClusterKDDMatrix. It initializes a set
 * data to cluster and verifies it was clustered correctly.
 * </p>
 * 
 * @author Alex McCaskey
 */
public class ClusterKDDMatrixTester {
	/**
	 * 
	 */
	private ClusterKDDMatrix matrix;

	/**
	 * 
	 */
	private int nRows;
	/**
	 * 
	 */
	private int nCols;
	/**
	 * 
	 */
	private IDataProvider provider;

	/**
	 * <p>
	 * Reference to the IFolder containing test data files.
	 * </p>
	 * 
	 */
	private IFolder dataFolder;

	/**
	 * <p>
	 * Initializes the data to be clustered.
	 * </p>
	 * 
	 */
	@Before
	public void beforeClass() {
		// Initialize the number of rows and columns
		nRows = 4;
		nCols = 2;

		// Create a test IDataProvider
		provider = new SimpleDataProvider();
		IData temp1;

		// Create IData lists to add to the providers
		ArrayList<IData> dataList = new ArrayList<IData>();
		ArrayList<IData> rowList = new ArrayList<IData>();
		ArrayList<IData> colList = new ArrayList<IData>();

		String separator = System.getProperty("file.separator");
		String workspaceName = "kddtestworkspace";
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

		// Create a JadeD Files directory in project workspace
		dataFolder = project.getFolder("data");
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

			// Close the reader.
			reader.close();
		} catch (CoreException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}

		IData tempDataX, tempDataY;
		ArrayList<Double> xPos = new ArrayList<Double>();
		ArrayList<Double> yPos = new ArrayList<Double>();
		for (int i = 0; i < xValues.size(); i++) {
			tempDataX = new SimpleData("Data", xValues.get(i));
			tempDataY = new SimpleData("Data", yValues.get(i));
			dataList.add(tempDataX);
			dataList.add(tempDataY);
		}

		rowList.add(new SimpleData("Number of Rows", 2000.0));
		colList.add(new SimpleData("Number of Columns", 2.0));

		((SimpleDataProvider) provider).addData(dataList, "Data");
		((SimpleDataProvider) provider).addData(rowList, "Number of Rows");
		((SimpleDataProvider) provider).addData(colList, "Number of Columns");

		matrix = new ClusterKDDMatrix(provider);

	}

	/**
	 * 
	 */
	@Test
	public void checkCluster() {
		// Cluster the data into 2 clusters
		// over 10 iterations
		matrix.cluster(2, 10);

		assertTrue(matrix.isClustered());
		assertEquals(2, matrix.getNumberOfClusters());
		assertEquals(1000, matrix.getNumberOfClusterElements(0));
		assertEquals(1000, matrix.getNumberOfClusterElements(1));

	}
}