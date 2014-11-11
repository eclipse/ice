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

import java.util.ArrayList;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.viz.plotviewer.CSVData;
import org.eclipse.ice.viz.plotviewer.CSVDataProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * This class is responsible for testing the CSVDataProvider.
 * 
 * @author Matthew Wang
 * 
 */
public class CSVDataProviderTester {

	/**
	 * The Provider to test
	 */
	private CSVDataProvider dataSet;

	/**
	 * The first data series
	 */
	private ArrayList<IData> dataSeries1;

	/**
	 * The second data series
	 */
	private ArrayList<IData> dataSeries2;

	/**
	 * The data series for the independent variable
	 */
	private ArrayList<IData> independentVarSeries;

	/**
	 * The name of the feature for the first data series
	 */
	private String feature1;

	/**
	 * The name of the feature for the second data series
	 */
	private String feature2;

	/**
	 * THe name of the independent variable
	 */
	private String independentVar;

	/**
	 * The double value
	 */
	private double value;

	/**
	 * Initialize a provider and some data in 2 dataSeries
	 */
	@Before
	public void beforeClass() {

		/**
		 * Initialize the data structures
		 */
		dataSeries1 = new ArrayList<IData>();
		dataSeries2 = new ArrayList<IData>();
		independentVarSeries = new ArrayList<IData>();
		dataSet = new CSVDataProvider();

		/**
		 * Initialize values
		 */
		independentVar = "Position";
		feature1 = "Intensity";
		feature2 = "Distance";
		value = 1.0;

		/**
		 * Loop to initialize values for the independent variable
		 */
		for (int i = 0; i < 5; i++) {
			IData data = new CSVData(independentVar, value * i);
			independentVarSeries.add(data);
		}

		/**
		 * Loop to initialize values for the first data series
		 */
		for (int i = 0; i < 5; i++) {
			IData data = new CSVData(feature1, value * i);
			dataSeries1.add(data);
		}

		/**
		 * Loop to initialize values for the second data series
		 */
		for (int i = 0; i < 5; i++) {
			IData data = new CSVData(feature2, value * i);
			dataSeries2.add(data);
		}

	}

	/**
	 * Check getting the features at the current time
	 */
	@Test
	public void checkGetFeaturesAtCurrentTime() {

		// Declare two times
		double firstTime = 1.0;
		double secondTime = 2.0;

		// Add dataSeries1 to both firstTime and secondTime
		dataSet.addDataSeries(firstTime, dataSeries1);
		dataSet.addDataSeries(secondTime, dataSeries1);

		// Add dataSeries2 to only secondTime
		dataSet.addDataSeries(secondTime, dataSeries2);

		// Set the time to the first time
		dataSet.setTime(firstTime);

		// Get the features at the current time of firstTime
		ArrayList<String> featuresAtFirstTime = dataSet
				.getFeaturesAtCurrentTime();

		// Confirm that the size is 1, only 1 dataSeries at firstTime
		assertEquals(featuresAtFirstTime.size(), 1);

		// Confirm that the feature of dataSeries1 is in featuresAtFirstTime and
		// dataSeries2 is not
		assertTrue(featuresAtFirstTime
				.contains(dataSeries1.get(0).getFeature()));
		assertFalse(featuresAtFirstTime.contains(dataSeries2.get(0)
				.getFeature()));

		// Set the time to the second time
		dataSet.setTime(secondTime);

		// Get the features at the current time of secondTime
		ArrayList<String> featuresAtSecondTime = dataSet
				.getFeaturesAtCurrentTime();

		// Confirm that the size is 2, both dataSeries was added at secondTime
		assertEquals(featuresAtSecondTime.size(), 2);

		// Confirm that both dataSeries are in featuresAtSecondTime
		assertTrue(featuresAtSecondTime.contains(dataSeries1.get(0)
				.getFeature()));
		assertTrue(featuresAtSecondTime.contains(dataSeries2.get(0)
				.getFeature()));

	}

	/**
	 * Check that the provider adds the data correctly, one data piece at a time
	 */
	@Test
	public void checkAddData() {

		double initialTime = 1.1;

		// Add dataSeries1 and dataSeries2 one data at a time with no specified
		// time
		for (IData data : dataSeries1) {
			dataSet.addData(data);
		}

		for (IData data : dataSeries2) {
			dataSet.addData(data);
		}

		// Add dataSeries2 one data at a time with a specified time
		for (IData data : dataSeries2) {
			dataSet.addData(initialTime, data);
		}

		// Check that there are two series for the first time
		ArrayList<String> featuresAtDefaultTime = dataSet
				.getFeaturesAtCurrentTime();

		// Need to get the first series and check each data item
		ArrayList<IData> series = dataSet
				.getDataAtCurrentTime(featuresAtDefaultTime.get(0));

		// Loop through the features to confirm that each data series was added
		// correctly
		for (int featureIndex = 0; featureIndex < featuresAtDefaultTime.size(); featureIndex++) {
			// Get the dataSeries from each feature in featuresAtDefaultTime
			series = dataSet.getDataAtCurrentTime(featuresAtDefaultTime
					.get(featureIndex));
			// Loop through to confirm each data added equals the data received
			for (int dataIndex = 0; dataIndex < series.size(); dataIndex++) {
				// Check whether the data is feature1 or feature2 which
				// corresponds to dataSeries1 or dataSeries2
				if (featuresAtDefaultTime.get(featureIndex).equals(feature1)) {
					assertEquals((CSVData) series.get(dataIndex),
							(CSVData) dataSeries1.get(dataIndex));
				} else if (featuresAtDefaultTime.get(featureIndex).equals(
						feature2)) {
					assertEquals((CSVData) series.get(dataIndex),
							(CSVData) dataSeries2.get(dataIndex));
				}

			}
		}

		// Check that there is one series for initialTime
		dataSet.setTime(initialTime);

		// Get the features set at the initialTime
		ArrayList<String> featuresAtInitialTime = dataSet
				.getFeaturesAtCurrentTime();

		// Confirm that only one series was added at the initialTime
		assertEquals(featuresAtInitialTime.size(), 1);

		// Get the data series at the initial time
		series = dataSet.getDataAtCurrentTime(featuresAtInitialTime.get(0));

		// Loop through each data and confirm that the data added equals the
		// data received
		for (int i = 0; i < series.size(); i++) {
			assertEquals((CSVData) series.get(i), (CSVData) dataSeries2.get(i));
		}

	}

	/**
	 * Tests for provider to check if a dataSeries is added and set correctly
	 */
	@Test
	public void checkAddAndRemoveDataSeries() {

		double initialTime = 1.0;

		// Add a data series with no given initial time
		dataSet.addDataSeries(dataSeries1);

		// Add a data series with a given initial time
		dataSet.addDataSeries(initialTime, dataSeries2);

		// Check the data for dataSeries1
		// Gets the data at the current time
		ArrayList<IData> data = dataSet.getDataAtCurrentTime(feature1);
		// Checks that the data in the data series is the same as the data in
		// the dataSet
		for (int i = 0; i < data.size(); i++) {
			assertEquals((CSVData) data.get(i), (CSVData) dataSeries1.get(i));
		}

		// Set the time to initialTime
		dataSet.setTime(initialTime);
		// Gets the data at initialTime
		data = dataSet.getDataAtCurrentTime(feature2);
		// Checks that the data in the data series is the same as the data in
		// the dataSet
		for (int i = 0; i < data.size(); i++) {
			assertEquals((CSVData) data.get(i), (CSVData) dataSeries2.get(i));
		}

		// Get the times currently added so we can remove the features by each
		// time
		ArrayList<Double> times = dataSet.getTimes();
		// 2 data series have been added with 2 separate times so the size of
		// times should be 2
		assertTrue(times.size() == 2);

		double t = times.get(0);
		// Remove the first data series
		dataSet.removeDataSeries(t, feature1);
		// Get the features at the current time
		dataSet.setTime(t);
		ArrayList<String> features = dataSet.getFeaturesAtCurrentTime();
		assertTrue(features.size() == 0);

		t = times.get(1);
		// Remove the first data series
		dataSet.removeDataSeries(t, feature2);
		// Get the features at the current time
		dataSet.setTime(t);
		features = dataSet.getFeaturesAtCurrentTime();
		assertEquals(features.size(), 0);
	}

	/**
	 * Test setting and getting the time units
	 */
	@Test
	public void checkSetAndGetTimeUnits() {

		// Initialize a unit
		String units = "Seconds";

		// Set the unit
		dataSet.setTimeUnits(units);

		// Checks that the unit is set correctly
		assertEquals(units, dataSet.getTimeUnits());
	}

	/**
	 * Test setting and getting the source
	 */
	@Test
	public void checkSetAndGetSource() {

		// Initialize a source
		String source = "File1";

		// Set the source
		dataSet.setSource(source);

		// Checks that the source is set correctly
		assertEquals(source, dataSet.getSourceInfo());
	}

	/**
	 * Test setting the time for a feature by adding a data series with an
	 * initial time and setting it to a different time
	 */
	@Test
	public void checkSetTimeForFeature() {

		double initialTime = 1.0;

		// Add a data series with a given initial time
		dataSet.addDataSeries(initialTime, dataSeries1);

		// Initialize a new time
		double newTime = 1.1;

		// Sets the time for the data series of feature1 to the new time
		dataSet.setTimeForFeature(initialTime, newTime, feature1);

		// Set the current time to the new time that was just changed
		dataSet.setTime(newTime);

		// Get the ArrayList of features to confirm that feature1 and
		// dataseries1 is set to the correct time
		ArrayList<String> features = dataSet.getFeaturesAtCurrentTime();

		// Check that the list of features at the given time contains the newly
		// changed feature
		assertTrue(features.contains(feature1));

		// Check that the newly changed data series is the same data series
		ArrayList<IData> newTimeCSVData = dataSet
				.getDataAtCurrentTime(feature1);

		// Checks that the size of newly changed data series is the same as the
		// original
		assertEquals(newTimeCSVData.size(), dataSeries1.size());

		// Checks each data item for equality
		for (int i = 0; i < newTimeCSVData.size(); i++) {
			assertEquals((CSVData) newTimeCSVData.get(i),
					(CSVData) dataSeries1.get(i));
		}

	}

	/**
	 * Test getting the time step given a time and the number of time steps
	 */
	@Test
	public void checkGetTimeStepAndNumberOfTimeSteps() {

		// Declare two times
		double firstTime = 1.0;
		double secondTime = 2.0;

		// Add the two data series
		dataSet.addDataSeries(firstTime, dataSeries1);
		dataSet.addDataSeries(secondTime, dataSeries2);

		// Get the time step for firstTime, should be 0
		assertEquals(dataSet.getTimeStep(firstTime), 0);

		// Get the time step for secondTime, should be 1
		assertEquals(dataSet.getTimeStep(secondTime), 1);

		// Get the number of time steps, should be 2
		assertEquals(dataSet.getNumberOfTimeSteps(), 2);

	}

	/**
	 * Test that the times are in ascending order
	 */
	@Test
	public void checkTimesLeastToGreatest() {

		double firstTime = 1.0;
		double secondTime = 2.0;

		// Add the two data series, larger time first
		dataSet.addDataSeries(secondTime, dataSeries2);
		dataSet.addDataSeries(firstTime, dataSeries1);

		// Get the times of the data set
		ArrayList<Double> times = dataSet.getTimes();

		// Confirm the size is 2
		assertEquals(times.size(), 2);

		// Confirm it is ordered least to greatest
		assertTrue(times.get(0) <= times.get(1));
	}

	/**
	 * Test getting the times where feature is present
	 */
	@Test
	public void checkGetTimesForFeature() {

		ArrayList<Double> timesForSeries1;
		ArrayList<Double> timesForSeries2;

		// Declare two times
		double firstTime = 1.0;
		double secondTime = 2.0;

		// Add dataSeries1 to both firstTime and secondTime
		dataSet.addDataSeries(firstTime, dataSeries1);
		dataSet.addDataSeries(secondTime, dataSeries1);

		// Add dataSeries2 to only secondTime
		dataSet.addDataSeries(secondTime, dataSeries2);

		// Confirm that dataSeries1 is present at both firstTime and secondTime
		timesForSeries1 = dataSet.getTimesForFeature(dataSeries1.get(0)
				.getFeature());
		assertTrue(timesForSeries1.contains(firstTime));
		assertTrue(timesForSeries1.contains(secondTime));

		// Confirm that dataSeries2 is not present for firstTime and is present
		// for secondTime
		timesForSeries2 = dataSet.getTimesForFeature(dataSeries2.get(0)
				.getFeature());
		assertFalse(timesForSeries2.contains(firstTime));
		assertTrue(timesForSeries2.contains(secondTime));

	}

	/**
	 * Test for getFeatureList to confirm that features are correctly added
	 */
	@Test
	public void checkGetFeatureList() {

		// Declare two times
		double firstTime = 1.0;
		double secondTime = 2.0;

		// Add dataSeries1 to both firstTime and secondTime
		dataSet.addDataSeries(firstTime, dataSeries1);
		dataSet.addDataSeries(secondTime, dataSeries1);

		// Add dataSeries2 to only secondTime
		dataSet.addDataSeries(secondTime, dataSeries2);

		// Get the feature list
		ArrayList<String> features = dataSet.getFeatureList();

		// Confirm that the size of the feature list is 2
		assertEquals(features.size(), 2);

		// Confirm that the two features are feature1 and feature2
		assertTrue(features.contains(feature1));
		assertTrue(features.contains(feature2));

	}

	/**
	 * Check that the positions were added correctly
	 */
	@Test
	public void checkPosition() {

		// Add independentVarSeries, dataSeries1, and dataSeries2 to the default
		// time
		dataSet.addDataSeries(independentVarSeries);
		dataSet.addDataSeries(dataSeries1);
		dataSet.addDataSeries(dataSeries2);

		// Set independentVarSeries as the independent variable
		dataSet.setFeatureAsIndependentVariable(independentVar);

		// Confirm that the independent variable was set correctly
		assertEquals(dataSet.getIndependentVariables().size(), 1);

		// Get the independent variable
		double[] position = dataSet.getPositionAtCurrentTime(independentVar);

		// Check the expected size
		assertEquals(position.length, independentVarSeries.size());

		// Check the values
		for (int i = 0; i < position.length; i++) {
			assertTrue(independentVarSeries.get(i).getValue() == position[i]);
		}

	}

}
