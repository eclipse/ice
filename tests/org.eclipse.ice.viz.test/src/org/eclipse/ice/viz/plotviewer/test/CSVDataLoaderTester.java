/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.viz.plotviewer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.viz.plotviewer.CSVDataLoader;
import org.eclipse.ice.viz.plotviewer.CSVDataProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * This class test the functionality of the CSVDataLoader. Four files covering
 * three different cases are tested.
 * 
 * Case 1: 	Data set with 2 time steps (LiCl_260K.csv [t=260], LiCl_290K.csv 
 * 			[t=290]). These files contains special #features and #units labels. 
 * 			The features have uncertainties.
 * Case 2: 	A very basic single CSV file (IFA-431_rod1_out.csv) with no special
 * 			#labels of any sort. The features have uncertainties.
 * Case 3:	A contour file (sqe0.5.csv).
 * 
 * @author Claire Saunders, Anna Wojtowicz
 * 
 */
public class CSVDataLoaderTester {

	/**
	 * Declare the CSVDataProviders, each loaded with the same file in a
	 * different way to compare against
	 */
	private CSVDataProvider defaultProvider;
	private CSVDataProvider fileProvider;
	private CSVDataProvider filenameProvider;
	private CSVDataProvider loadedDataSet;
	private CSVDataProvider contourProvider;
	private CSVDataProvider rod1Provider;

	/**
	 * The list of providers loaded
	 */
	private ArrayList<CSVDataProvider> dataProviderList;

	/**
	 * The contour width and height
	 */
	private int contourWidth;
	private int contourHeight;

	/**
	 * The contour's data minimum and data maximum
	 */
	private double contourMin;
	private double contourMax;

	/**
	 * The LiCl_260K.csv file name
	 */
	private String file260Name;

	/**
	 * The LiCl_290K.csv file name
	 */
	private String file290Name;

	/**
	 * The String array for the data set
	 */
	private String[] fileNameSet;

	/**
	 * The Contour file name sqe0.5.csv
	 */
	private String contourFileName;
	
	/**
	 * The basic CSV file containing no special delimiters or "hash" labels
	 * (ie. #feature1, #feature2, etc.)
	 */
	private String rod1FileName;

	/**
	 * The Double Array for the times for the data set
	 */
	private ArrayList<Double> dataSetTimes;

	/**
	 * Declare ArrayLists for the features and units
	 */
	private ArrayList<String> features;
	private ArrayList<String> units;

	/**
	 * The ArrayLists for the hard coded values and uncertainties for the 260
	 * file
	 */
	private ArrayList<Double> file260_XValues;
	private ArrayList<Double> file260_Y0Values;
	private ArrayList<Double> file260_Y0Uncertainty;
	private ArrayList<Double> file260_Y1Values;
	private ArrayList<Double> file260_Y1Uncertainty;
	private ArrayList<Double> file260_Y2Values;
	private ArrayList<Double> file260_Y2Uncertainty;

	/**
	 * ArrayLists for the hardcoded values and uncertainties for the 290 file
	 */
	private ArrayList<Double> file290_XValues;
	private ArrayList<Double> file290_Y0Values;
	private ArrayList<Double> file290_Y0Uncertainty;
	private ArrayList<Double> file290_Y1Values;
	private ArrayList<Double> file290_Y1Uncertainty;
	private ArrayList<Double> file290_Y2Values;
	private ArrayList<Double> file290_Y2Uncertainty;

	/**
	 * ArrayLists for the hardcoded value for the Rod1 file
	 */
	private ArrayList<Double> rod1_XValues;
	private ArrayList<Double> rod1_Y0Values;
	private ArrayList<Double> rod1_Y0Uncertainty;
	private ArrayList<Double> rod1_Y1Values;
	private ArrayList<Double> rod1_Y1Uncertainty;
	private ArrayList<Double> rod1_Y2Values;
	private ArrayList<Double> rod1_Y2Uncertainty;
	
	
	/**
	 * This operation performs the initial setup of the data structures used by
	 * this class.
	 */
	@Before 
	public void before() {

		// Set up test workspace
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = null;
		URI defaultProjectLocation = null;
		String separator = System.getProperty("file.separator");
		String userDir = System.getProperty("user.home") + separator
				+ "ICETests" + separator + "CSVLoaderTesterWorkspace";

		// Setup the project
		try {
			// Get the project handle
			project = workspaceRoot.getProject("CSVLoaderTesterWorkspace");
			// If the project does not exist, create it
			if (!project.exists()) {
				// Set the location as ${workspace_loc}/ItemTesterWorkspace
				defaultProjectLocation = (new File(userDir)).toURI();
				System.out.println(defaultProjectLocation);
				// Create the project description
				IProjectDescription desc = ResourcesPlugin.getWorkspace()
						.newProjectDescription("CSVLoaderTesterWorkspace");
				// Set the location of the project
				desc.setLocationURI(defaultProjectLocation);
				// Create the project
				project.create(desc, null);
			}
			// Open the project if it is not already open
			if (project.exists() && !project.isOpen())
				project.open(null);
		} catch (CoreException e) {

			e.printStackTrace();
			fail();
		}

		// Initialize the file names to the correct name and path
		file260Name = userDir + separator + "LiCl_260K.csv";
		file290Name = userDir + separator + "LiCl_290K.csv";
		contourFileName = userDir + separator + "sqe0.5.csv";
		rod1FileName = userDir + separator + "IFA-431_rod1_out.csv";

		// Create a array of string file names
		fileNameSet = new String[2];
		fileNameSet[0] = file260Name;
		fileNameSet[1] = file290Name;

		// Create the times array for the data set
		dataSetTimes = new ArrayList<Double>();
		dataSetTimes.add(260.0);
		dataSetTimes.add(290.0);

		// Make a file out of file260Name to test the loading of a File
		File inputFile = new File(file260Name);

		// Hard code the features for the input files
		features = new ArrayList<String>(Arrays.asList("X", "Y0", "Y1", "Y2"));
		// Hard code the units for the input files
		units = new ArrayList<String>(
				Arrays.asList("energy", "intensity", "intensity", "intensity",
						"intensity", "intensity", "intensity"));

		// Set the hardcoded values for testing
		file260_XValues = new ArrayList<Double>(Arrays.asList(-0.0998, -0.0994,
				-0.099, -0.0986, -0.0982, -0.0978, -0.0974, -0.097, -0.0966,
				-0.0962));
		file260_Y0Values = new ArrayList<Double>(Arrays.asList(3.11503e-05,
				5.62496e-05, 5.03196e-05, 8.04087e-05, 5.03579e-05,
				6.64613e-05, 4.89228e-05, 5.3106e-05, 4.61054e-05, 5.46963e-05));
		file260_Y0Uncertainty = new ArrayList<Double>(Arrays.asList(
				1.05058e-05, 1.24494e-05, 1.23583e-05, 1.71047e-05,
				1.04107e-05, 1.71451e-05, 1.18219e-05, 1.26493e-05,
				1.07115e-05, 1.06676e-05));
		file260_Y1Values = new ArrayList<Double>(
				Arrays.asList(6.63927e-05, 6.31917e-05, 5.27297e-05,
						5.49267e-05, 6.41059e-05, 4.79712e-05, 6.51931e-05,
						7.02423e-05, 5.50736e-05, 6.21552e-05));
		file260_Y1Uncertainty = new ArrayList<Double>(
				Arrays.asList(1.27345e-05, 1.1454e-05, 1.29338e-05,
						1.37831e-05, 1.17493e-05, 1.13836e-05, 1.30777e-05,
						1.48958e-05, 1.26723e-05, 1.05366e-05));
		file260_Y2Values = new ArrayList<Double>(Arrays.asList(9.41e-05,
				7.08198e-05, 9.30116e-05, 7.59784e-05, 7.51211e-05,
				4.89597e-05, 7.82205e-05, 9.7297e-05, 9.752e-05, 8.30943e-05));
		file260_Y2Uncertainty = new ArrayList<Double>(Arrays.asList(
				1.52489e-05, 1.21289e-05, 1.68258e-05, 1.42642e-05,
				1.16984e-05, 1.13364e-05, 1.50652e-05, 1.54618e-05,
				1.55541e-05, 1.22241e-05));

		file290_XValues = new ArrayList<Double>(Arrays.asList(-0.0998, -0.0994,
				-0.099, -0.0986, -0.0982, -0.0978, -0.0974, -0.097, -0.0966,
				-0.0962));
		file290_Y0Values = new ArrayList<Double>(
				Arrays.asList(8.48657e-05, 8.17217e-05, 7.86727e-05,
						9.13478e-05, 6.76878e-05, 7.52866e-05, 7.86473e-05,
						5.20828e-05, 8.93177e-05, 8.34147e-05));
		file290_Y0Uncertainty = new ArrayList<Double>(Arrays.asList(
				1.55884e-05, 1.30139e-05, 2.08335e-05, 1.86182e-05, 1.401e-05,
				1.8554e-05, 1.53202e-05, 1.15878e-05, 1.6413e-05, 1.60837e-05));
		file290_Y1Values = new ArrayList<Double>(Arrays.asList(8.44321e-05,
				0.000122558, 0.00010391, 0.000107919, 9.37003e-05, 9.37024e-05,
				8.67264e-05, 0.00010519, 7.6891e-05, 0.000110878));
		file290_Y1Uncertainty = new ArrayList<Double>(Arrays.asList(
				1.53122e-05, 1.66395e-05, 1.75712e-05, 1.69478e-05,
				1.52059e-05, 1.67303e-05, 1.34142e-05, 1.74367e-05,
				1.46259e-05, 1.57169e-05));
		file290_Y2Values = new ArrayList<Double>(Arrays.asList(0.000174163,
				0.00017506, 0.000148379, 0.000126349, 0.000135381, 0.000120946,
				0.000117529, 0.000143015, 0.000152179, 0.000139409));
		file290_Y2Uncertainty = new ArrayList<Double>(Arrays.asList(
				2.22809e-05, 1.99118e-05, 2.3159e-05, 1.81769e-05, 1.60847e-05,
				1.9592e-05, 1.66315e-05, 1.83402e-05, 2.19285e-05, 1.72319e-05));
	
		rod1_XValues = new ArrayList<Double>(Arrays.asList(
				-100.0,0.0,900.0,1800.0,2700.0,3600.0,4500.0,5400.0,6300.0,
				7200.0));
		rod1_Y0Values = new ArrayList<Double>(Arrays.asList(
				0.0,100.0,900.0,900.0,900.0,900.0,900.0,900.0,900.0,900.0));
		rod1_Y0Uncertainty = new ArrayList<Double>(Arrays.asList(
				293.0,507.9095721,522.2012984,523.650954,525.6631533,
				525.9132687,526.2099559,526.747924,531.0598762,533.7404418));
		rod1_Y1Values = new ArrayList<Double>(Arrays.asList(
				3.50E-06,3.50E-06,3.50E-06,3.50E-06,3.50E-06,3.50E-06,3.50E-06,
				3.50E-06,3.50E-06,3.50E-06));
		rod1_Y1Uncertainty = new ArrayList<Double>(Arrays.asList(
				293.0,512.3702696,514.6919852,514.9232889,515.2463987,
				515.2867456,515.3346468,515.4216358,516.1252594,516.5684875));
		rod1_Y2Values = new ArrayList<Double>(Arrays.asList(
				0.0,0.0,3.85E-09,1.21E-08,2.19E-08,3.27E-08,4.37E-08,5.51E-08,
				6.86E-08,8.53E-08));
		rod1_Y2Uncertainty = new ArrayList<Double>(Arrays.asList(
				0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0));

		// The list of loaded providers to test the different loading
		// functionalities
		dataProviderList = new ArrayList<CSVDataProvider>();

		// Create a new default loader
		CSVDataLoader newDataLoaderDefault = new CSVDataLoader();
		// Create a comprehensive loader with an input file
		CSVDataLoader newDataLoaderFile = new CSVDataLoader(inputFile);
		// Create a comprehensive loader with an input file name
		CSVDataLoader newDataLoaderFilename = new CSVDataLoader(file260Name);

		// Load the default data set by putting the file name in the load
		// command
		defaultProvider = newDataLoaderDefault.load(file260Name);
		// Load using the input file already specified in the comprehensive
		// constructor
		fileProvider = newDataLoaderFile.load();
		// Load using the LiCl_260.csv file name already specified in the
		// comprehensive constructor
		filenameProvider = newDataLoaderFilename.load();

		// Add the providers to the provider list
		dataProviderList.add(defaultProvider);
		dataProviderList.add(fileProvider);
		dataProviderList.add(filenameProvider);

		// Create a loader to load a file set
		CSVDataLoader newDataSetLoader = new CSVDataLoader();
		loadedDataSet = newDataSetLoader.loadAsFileSet(fileNameSet);

		// Load the contour plot which has the matrix specifications within
		CSVDataLoader newDataContourLoader = new CSVDataLoader();
		contourProvider = newDataContourLoader.load(contourFileName);
		// Hardcode the width and height
		contourWidth = 100;
		contourHeight = 200;
		// Hardcode the min and max
		contourMin = -100.0;
		contourMax = 100.0;
		
		// Load the basic CSV file that contains no special labels/delimiters
		CSVDataLoader newDataCSVLoader = new CSVDataLoader();
		rod1Provider = newDataCSVLoader.load(rod1FileName);

	}

	/**
	 * Checks that the features were loaded correctly
	 */
	@Test
	public void checkAllFeatures() {
		
		//Local declarations
		ArrayList<String> dataFeatures = null;
		
		// Loop through the loaded data set and check the features
		for (CSVDataProvider oneDataSet : dataProviderList) {
			dataFeatures = oneDataSet.getFeatureList();
			// Check that the size of the two feature lists are equal
			assertEquals(dataFeatures.size(), features.size());
			// Check that each feature is within the set of features loaded
			for (String feature : dataFeatures) {
				assertTrue(features.contains(feature));
			}
		}
		
		// Check the rod1 case by itself
		dataFeatures = rod1Provider.getFeatureList();
		// Check that the size of the two feature lists are equal
		assertEquals(dataFeatures.size(), features.size());
		// Check that each feature is within the set of features loaded
		for (String feature : dataFeatures) {
			assertTrue(features.contains(feature));
		}
		
		// Check the dataSet's features
		ArrayList<String> dataSetFeatures = loadedDataSet.getFeatureList();
		// Check the size of the feature lists are equal
		assertEquals(dataSetFeatures.size(), features.size());
		// Check that each feature is within the set of features loaded
		for (String feature : dataSetFeatures) {
			assertTrue(features.contains(feature));
		}
	}

	/**
	 * Check that the times were loaded correctly
	 */
	@Test
	public void checkTimesForDataSet() {
		// Get the times for the dataSet
		ArrayList<Double> times = loadedDataSet.getTimes();
		// Check that the two sizes are equal
		assertEquals(times.size(), dataSetTimes.size());
		// Check that the contents of times is within the provider's times
		for (Double time : times) {
			assertTrue(dataSetTimes.contains(time));
		}
	}

	/**
	 * Check the feature X
	 */
	@Test
	public void checkFeatureX() {
		// Local declarations
		ArrayList<IData> dataFromProvider = null;
		
		// Loop through each data provider and check that the x values are
		// correct
		for (CSVDataProvider oneDataProvider : dataProviderList) {
			// Get the data from the provider
			dataFromProvider = oneDataProvider
					.getDataAtCurrentTime(features.get(0));
			// Check the units
			assertEquals(dataFromProvider.get(0).getUnits(), units.get(0));
			// Check that the sizes are equal
			assertEquals(dataFromProvider.size(), file260_XValues.size());
			// Loop through the values
			for (int i = 0; i < dataFromProvider.size(); i++) {
				// Check that the values are equal
				assertTrue(dataFromProvider.get(i).getValue() == file260_XValues
						.get(i));
			}
		}

		// Check the X values for the dataSet
		// Set the time to 260 first
		loadedDataSet.setTime(dataSetTimes.get(0));
		// Get the data from the provider
		dataFromProvider = loadedDataSet
				.getDataAtCurrentTime(features.get(0));
		// Check the units
		assertEquals(dataFromProvider.get(0).getUnits(), units.get(0));
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), file260_XValues.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == file260_XValues
					.get(i));
		}

		// Set the time to 290 next
		loadedDataSet.setTime(dataSetTimes.get(1));
		// Get the data from the provider
		dataFromProvider = loadedDataSet.getDataAtCurrentTime(features.get(0));
		// Check the units
		assertEquals(dataFromProvider.get(0).getUnits(), units.get(0));
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), file290_XValues.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == file290_XValues
					.get(i));
		}
		
		// Get the rod1 data
		dataFromProvider = rod1Provider.getDataAtCurrentTime(features.get(0));
		// Check the units
		assertNull(dataFromProvider.get(0).getUnits());
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), rod1_XValues.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == rod1_XValues
					.get(i));
		}
	}

	/**
	 * Check the feature and uncertainty for Y0
	 */
	@Test
	public void checkFeatureAndUncertaintyForY0() {
		// Local declarations
		ArrayList<IData> dataFromProvider = null;
		
		// Loop through each data provider and check that the x values are
		// correct
		for (CSVDataProvider oneDataProvider : dataProviderList) {
			// Get the data from the provider
			dataFromProvider = oneDataProvider
					.getDataAtCurrentTime(features.get(1));
			// Check the units
			assertEquals(dataFromProvider.get(0).getUnits(), units.get(1));
			// Check that the sizes are equal
			assertEquals(dataFromProvider.size(), file260_Y0Values.size());
			// Loop through the values
			for (int i = 0; i < dataFromProvider.size(); i++) {
				// Check that the values are equal
				assertTrue(dataFromProvider.get(i).getValue() == 
						file260_Y0Values.get(i));
				// Check that the uncertainties are equal
				assertTrue(dataFromProvider.get(i).getUncertainty() == 
						file260_Y0Uncertainty.get(i));
			}
		}

		// Check the Y0 values for the dataSet
		// Set the time to 260 first
		loadedDataSet.setTime(dataSetTimes.get(0));
		// Get the data from the provider
		dataFromProvider = loadedDataSet
				.getDataAtCurrentTime(features.get(1));
		// Check the units
		assertEquals(dataFromProvider.get(0).getUnits(), units.get(1));
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), file260_Y0Values.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == file260_Y0Values
					.get(i));
			// Check that the uncertainties are equal
			assertTrue(dataFromProvider.get(i).getUncertainty() == 
					file260_Y0Uncertainty.get(i));
			// Check that the uncertainties are equal
			assertTrue(dataFromProvider.get(i).getUncertainty() == 
					file260_Y0Uncertainty.get(i));
		}

		// Set the time to 290 next
		loadedDataSet.setTime(dataSetTimes.get(1));
		// Get the data from the provider
		dataFromProvider = loadedDataSet.getDataAtCurrentTime(features.get(1));
		// Check the units
		assertEquals(dataFromProvider.get(0).getUnits(), units.get(1));
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), file290_Y0Values.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == file290_Y0Values
					.get(i));
			// Check that the uncertainties are equal
			assertTrue(dataFromProvider.get(i).getUncertainty() == 
					file290_Y0Uncertainty.get(i));
		}
		
		// Get the data from the rod 1 provider
		dataFromProvider = rod1Provider.getDataAtCurrentTime(features.get(1));
		// Check the units are null
		assertNull(dataFromProvider.get(0).getUnits());
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), rod1_Y0Values.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == rod1_Y0Values
					.get(i));
			// Check that the uncertainties are equal
			assertTrue(dataFromProvider.get(i).getUncertainty() == 
					rod1_Y0Uncertainty.get(i));
		}
		
	}

	/**
	 * Check the feature and uncertainty for Y1
	 */
	@Test
	public void checkFeatureAndUncertaintyForY1() {
		// Local declarations
		ArrayList<IData> dataFromProvider = null;		
		
		// Loop through each data provider and check that the x values are
		// correct
		for (CSVDataProvider oneDataProvider : dataProviderList) {
			// Get the data from the provider
			dataFromProvider = oneDataProvider
					.getDataAtCurrentTime(features.get(2));
			// Check the units
			assertEquals(dataFromProvider.get(0).getUnits(), units.get(2));
			// Check that the sizes are equal
			assertEquals(dataFromProvider.size(), file260_Y1Values.size());
			// Loop through the values
			for (int i = 0; i < dataFromProvider.size(); i++) {
				// Check that the values are equal
				assertTrue(dataFromProvider.get(i).getValue() == 
						file260_Y1Values.get(i));
				// Check that the uncertainties are equal
				assertTrue(dataFromProvider.get(i).getUncertainty() == 
						file260_Y1Uncertainty.get(i));
			}
		}

		// Check the Y1 values for the dataSet
		// Set the time to 260 first
		loadedDataSet.setTime(dataSetTimes.get(0));
		// Get the data from the provider
		dataFromProvider = loadedDataSet
				.getDataAtCurrentTime(features.get(2));
		// Check the units
		assertEquals(dataFromProvider.get(0).getUnits(), units.get(2));
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), file260_Y1Values.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == file260_Y1Values
					.get(i));
			// Check that the uncertainties are equal
			assertTrue(dataFromProvider.get(i).getUncertainty() == 
					file260_Y1Uncertainty.get(i));
		}

		// Set the time to 290 next
		loadedDataSet.setTime(dataSetTimes.get(1));
		// Get the data from the provider
		dataFromProvider = loadedDataSet.getDataAtCurrentTime(features.get(2));
		// Check the units
		assertEquals(dataFromProvider.get(0).getUnits(), units.get(2));
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), file290_Y1Values.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == file290_Y1Values
					.get(i));
			// Check that the uncertainties are equal
			assertTrue(dataFromProvider.get(i).getUncertainty() == 
					file290_Y1Uncertainty.get(i));
		}
		
		// Get the data from the rod1 provider
		dataFromProvider = rod1Provider.getDataAtCurrentTime(features.get(2));
		// Check the units
		assertNull(dataFromProvider.get(0).getUnits());
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), rod1_Y1Values.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == rod1_Y1Values
					.get(i));
			// Check that the uncertainties are equal
			assertTrue(dataFromProvider.get(i).getUncertainty() == 
					rod1_Y1Uncertainty.get(i));
		}
	}

	/**
	 * Check the feature and uncertainty for Y2
	 */
	@Test
	public void checkFeatureAndUncertaintyForY2() {
		// Local declarations
		ArrayList<IData> dataFromProvider = null;
		
		// Loop through each data provider and check that the x values are
		// correct
		for (CSVDataProvider oneDataProvider : dataProviderList) {
			// Get the data from the provider
			dataFromProvider = oneDataProvider
					.getDataAtCurrentTime(features.get(3));
			// Check the units
			assertEquals(dataFromProvider.get(0).getUnits(), units.get(3));
			// Check that the sizes are equal
			assertEquals(dataFromProvider.size(), file260_Y2Values.size());
			// Loop through the values
			for (int i = 0; i < dataFromProvider.size(); i++) {
				// Check that the values are equal
				assertTrue(dataFromProvider.get(i).getValue() == 
						file260_Y2Values.get(i));
				// Check that the uncertainties are equal
				assertTrue(dataFromProvider.get(i).getUncertainty() == 
						file260_Y2Uncertainty.get(i));
			}
		}

		// Check the Y2 values for the dataSet
		// Set the time to 260 first
		loadedDataSet.setTime(dataSetTimes.get(0));
		// Get the data from the provider
		dataFromProvider = loadedDataSet
				.getDataAtCurrentTime(features.get(3));
		// Check the units
		assertEquals(dataFromProvider.get(0).getUnits(), units.get(3));
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), file260_Y2Values.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == file260_Y2Values
					.get(i));
			// Check that the uncertainties are equal
			assertTrue(dataFromProvider.get(i).getUncertainty() == 
					file260_Y2Uncertainty.get(i));
		}

		// Set the time to 290 next
		loadedDataSet.setTime(dataSetTimes.get(1));
		// Get the data from the provider
		dataFromProvider = loadedDataSet.getDataAtCurrentTime(features.get(3));
		// Check the units
		assertEquals(dataFromProvider.get(0).getUnits(), units.get(3));
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), file290_Y2Values.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == file290_Y2Values
					.get(i));
			// Check that the uncertainties are equal
			assertTrue(dataFromProvider.get(i).getUncertainty() == file290_Y2Uncertainty
					.get(i));
		}
		
		// Get the data from the rod1 provider
		dataFromProvider = rod1Provider.getDataAtCurrentTime(features.get(3));
		// Check the units
		assertNull(dataFromProvider.get(0).getUnits());
		// Check that the sizes are equal
		assertEquals(dataFromProvider.size(), rod1_Y2Values.size());
		for (int i = 0; i < dataFromProvider.size(); i++) {
			// Check that the values are equal
			assertTrue(dataFromProvider.get(i).getValue() == rod1_Y2Values
					.get(i));
			// Check that the uncertainties are equal
			assertTrue(dataFromProvider.get(i).getUncertainty() == rod1_Y2Uncertainty
					.get(i));
		}
	}

	/**
	 * Check that the contour width and height were loaded correctly
	 */
	@Test
	public void checkContourWidthAndHeight() {
		// Check that the widths are equal
		assertEquals(contourProvider.getDataWidth(), contourWidth);
		// Check that the heights are equal
		assertEquals(contourProvider.getDataHeight(), contourHeight);
	}

	/**
	 * Check that the contour read the minimum and maximum of the contour file
	 * correctly
	 */
	@Test
	public void checkContourMinAndMax() {
		// Check that the minimums are equal
		assertTrue(contourMin == contourProvider.getDataMin());
		// Check that the maximums are equal
		assertTrue(contourMax == contourProvider.getDataMax());
	}
}
