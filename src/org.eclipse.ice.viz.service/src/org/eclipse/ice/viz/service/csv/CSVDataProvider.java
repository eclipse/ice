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
package org.eclipse.ice.viz.service.csv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;

/**
 * This class is the implementation of IDataProvider for the PlotViewer tool.
 * 
 * @author Matthew Wang
 * 
 */
public class CSVDataProvider implements IDataProvider {

	/**
	 * Structure to contain all the data for each feature at each time step
	 */
	private final Map<Double, Map<String, List<IData>>> dataSet;

	/**
	 * Units for the time
	 */
	private String timeUnits;

	/**
	 * The source of the data
	 */
	private String source;

	/**
	 * The current time
	 */
	private double currentTime;

	/**
	 * ArrayList of independent variables
	 */
	private final List<String> independentVars;

	/**
	 * Create a default time
	 */
	private double defaultTime = 0.0;

	/**
	 * Data width for a loaded matrix (For contour plot)
	 */
	private int dataWidth = 0;

	/**
	 * Data height for a loaded matrix (For contour plot)
	 */
	private int dataHeight = 0;

	/**
	 * The minimum value for a loaded matrix (For Contour plot)
	 */
	private double dataMin;

	/**
	 * The maximum value for the loaded matrix (For Contour plot)
	 */
	private double dataMax;

	/**
	 * Default constructor
	 */
	public CSVDataProvider() {
		dataSet = new HashMap<Double, Map<String, List<IData>>>();
		currentTime = defaultTime;
		timeUnits = null;
		source = null;
		independentVars = new ArrayList<String>();
	}

	/**
	 * Adds a single data item at a specified time. If the data series at the
	 * specified time doesn't exist it creates a new one. If it does exist, the
	 * data is appended to the end of the series.
	 * 
	 * @param time
	 * @param data
	 */
	public void addData(double time, IData data) {
		// Get the feature of the data item
		String feature = data.getFeature();
		// Check if the given time already exists
		if (dataSet.containsKey(time)) {
			// Check if the feature already exists for a given time
			if (dataSet.get(time).containsKey(feature)) {
				// Feature exists so add the data to the ArrayList
				dataSet.get(time).get(feature).add(data);
			} else {
				// Feature does not exist so create the ArrayList
				dataSet.get(time).put(feature, new ArrayList<IData>());
				// Add data to the ArrayList
				dataSet.get(time).get(feature).add(data);
			}
		} else {
			// The time does not exist so need a new HashMap for the given time
			Map<String, List<IData>> dataSetComponent = new HashMap<String, List<IData>>();
			// Initialize the HashMap and add in the feature and an empty
			// ArrayList
			dataSetComponent.put(feature, new ArrayList<IData>());
			// Add the data to the ArrayList
			dataSetComponent.get(feature).add(data);
			// Add this new HashMap to the dataSet
			dataSet.put(time, dataSetComponent);
		}
		return;
	}

	/**
	 * Adds a single data item with no specified time. It calls the addData with
	 * time and uses the default time instead.
	 * 
	 * @param data
	 */
	public void addData(IData data) {
		// Add the single data with no given time to the dataSet
		addData(defaultTime, data);
	}

	/**
	 * Adds a data series to the data set at a specified time. At the moment, if
	 * the data series already exists, it does nothing.
	 * 
	 * @param time
	 * @param dataSeries
	 */
	public void addDataSeries(double time, List<IData> dataSeries) {
		// Get the feature of the dataSeries
		String feature = dataSeries.get(0).getFeature();
		// Add to the dataSet
		if (dataSet.containsKey(time)) {
			// The time already exists so add to the existing HashMap
			if (dataSet.get(time).containsKey(feature)) {
				// Duplicate feature with a dataSeries, throw error or add to
				// the end of the existing series?
			} else {
				dataSet.get(time).put(feature, dataSeries);
			}
		} else {
			// Creates a new HashMap for the new time, feature, and dataSeries
			Map<String, List<IData>> dataSetComponent = new HashMap<String, List<IData>>();
			// Sets the given feature and dataSeries
			dataSetComponent.put(feature, dataSeries);
			// Adds the HashMap of the feature to the time
			dataSet.put(time, dataSetComponent);
		}
		return;
	}

	/**
	 * Adds a data series with no specified time. It calls addDataSeries with a
	 * specified time and uses the defaultTime as the time.
	 * 
	 * @param dataSeries
	 */
	public void addDataSeries(List<IData> dataSeries) {
		// Add the dataSeries with no given time to the dataSet
		addDataSeries(defaultTime, dataSeries);
	}

	/**
	 * Removes a data series at a specified time with a specified feature.
	 * 
	 * @param time
	 * @param feature
	 */
	public void removeDataSeries(double time, String feature) {
		// Checks that the dataSet has the time and that the time has the
		// feature
		if (dataSet.containsKey(time) && dataSet.get(time).containsKey(feature)) {
			dataSet.get(time).remove(feature);
		}
		// FIXME What if the feature is an independent variable?
	}

	/**
	 * Sets the time units
	 * 
	 * @param timeUnits
	 */
	public void setTimeUnits(String timeUnits) {
		this.timeUnits = timeUnits;
	}

	/**
	 * Sets the source
	 * 
	 * @param source
	 */
	public void setSource(String source) {
		this.source = source;
	}

	// should be int?: public void setTime(int step)
	// Sets the current time based on the step, an integer starting at 0
	/**
	 * Sets the currentTime based on the specified step/time. Checks if the time
	 * exists first.
	 */
	@Override
	public void setTime(double step) {
		if (dataSet.containsKey(step)) {
			currentTime = step;
		} else {
			// Invalid time
		}
		return;
	}

	/**
	 * Sets a new time for a specified time and feature.
	 * 
	 * @param prevTime
	 * @param newTime
	 * @param feature
	 */
	public void setTimeForFeature(double prevTime, double newTime,
			String feature) {
		if (dataSet.containsKey(prevTime)) {
			// Saves the data before removal from the dataSet
			List<IData> data = dataSet.get(prevTime).get(feature);
			// Removes the data from the dataSet
			removeDataSeries(prevTime, feature);
			// Adds the data into the newTime
			addDataSeries(newTime, data);
		} else {
			// Throw invalid time exception
		}
		return;
	}

	/**
	 * Sets a new time for a specified feature and assumes the previous time was
	 * defaultTime
	 * 
	 * @param newTime
	 * @param feature
	 */
	public void setTimeForFeature(double newTime, String feature) {
		setTimeForFeature(defaultTime, newTime, feature);
	}

	/**
	 * Sets a feature as a independent variable/position across all times (May
	 * need to check that the independent variable exists across all times.
	 * Right now just assumes it does)
	 * 
	 * @param independent
	 */
	public void setFeatureAsIndependentVariable(String independent) {
		// FIXME Presumably, this method is called AFTER all data has been
		// added. Otherwise, new features will not have the correct positional
		// data.

		// Don't add the same variable twice!
		if (!independentVars.contains(independent)) {
			// Loop through each time to add the independent variable's data at
			// that time to the positions for all features.
			List<Double> times = getTimes();
			for (double time : times) {
				// Set the current time.
				setTime(time);

				// Get the data at the current time for the new independent
				// feature.
				List<IData> independentData = getDataAtCurrentTime(independent);
				// Get all features at the current time.
				List<String> featuresAtTime = getFeaturesAtCurrentTime();

				// Do not remove the independent variable feature from the list
				// of features. Effectively, this means we can plot it against
				// itself, but it also means the position arrays for each
				// CSVData share the same order (based on the list of
				// independent variables).

				// For each feature at the current time, add the independent
				// variable's data as positional data.
				for (String feature : featuresAtTime) {
					List<IData> featureData = getDataAtCurrentTime(feature);
					for (int i = 0; i < featureData.size(); i++) {
						// set the position
						((CSVData) featureData.get(i))
								.addPosition(independentData.get(i).getValue());
					}
				}
				// Do not remove the independent variable feature from the map
				// of data, else it will not be found when plotting another
				// "independent" variable against it.
			}

			// Add to the independentVar list. We can use the independent
			// feature's index in this list to pull the correct positional data
			// from the features later.
			independentVars.add(independent);
		}
		return;
	}

	/**
	 * Set the dataWidth for a matrix (Contour plot)
	 * 
	 * @param dataWidth
	 */
	public void setDataWidth(int dataWidth) {
		this.dataWidth = dataWidth;
	}

	/**
	 * Set the dataHeight for a matrix (Contour plot)
	 * 
	 * @param dataHeight
	 */
	public void setDataHeight(int dataHeight) {
		this.dataHeight = dataHeight;
	}

	/**
	 * Set the data minimum (Contour plot)
	 * 
	 * @param minimum
	 */
	public void setDataMin(double minimum) {
		this.dataMin = minimum;
	}

	/**
	 * Set the data maximum (Contour plot)
	 * 
	 * @param maximum
	 */
	public void setDataMax(double maximum) {
		this.dataMax = maximum;
	}

	/**
	 * Returns an ArrayList of feature Strings of independent variables
	 * 
	 * @return
	 */
	public ArrayList<String> getIndependentVariables() {
		return new ArrayList<String>(independentVars);
	}

	/**
	 * Gets the list of times that the feature is present at.
	 * 
	 * @param feature
	 * @return
	 */
	public ArrayList<Double> getTimesForFeature(String feature) {
		// Create the ArrayList of Double
		ArrayList<Double> timesForFeature = new ArrayList<Double>();
		// Iterate through the times
		for (Double timesInSet : dataSet.keySet()) {
			// If the feature is present at the time, add it to the
			// timesForFeature
			if (dataSet.get(timesInSet).containsKey(feature)) {
				timesForFeature.add(timesInSet);
			}
		}

		return timesForFeature;
	}

	/**
	 * Returns the lists of features across all times
	 */
	@Override
	public ArrayList<String> getFeatureList() {
		// Traverse the map of data, adding all feature names to the Set.
		// The Set will only contain unique values.
		Set<String> featureSet = new HashSet<String>();
		for (Double time : dataSet.keySet()) {
			featureSet.addAll(dataSet.get(time).keySet());
		}
		// Return the TreeSet as an ArrayList.
		return new ArrayList<String>(featureSet);
	}

	/**
	 * Returns the number of time steps
	 */
	@Override
	public int getNumberOfTimeSteps() {
		return dataSet.size();
	}

	/**
	 * Returns the series of data at the set current time for a specified
	 * feature, or null if data could not be found. Will not return an empty
	 * list.
	 */
	@Override
	public ArrayList<IData> getDataAtCurrentTime(String feature) {
		// Check that the dataSet at the current time has the specified feature
		ArrayList<IData> data = null;
		List<IData> sourceData = dataSet.get(currentTime).get(feature);
		if (!sourceData.isEmpty()) {
			// Send a copy of the list so the data map can't be altered.
			data = new ArrayList<IData>(sourceData);
		}
		return data;
	}

	/**
	 * Returns the values of each IData at the current time for a specified
	 * feature.
	 * 
	 * @param feature
	 * @return An array of values for each IData at the current time, or null if
	 *         data could not be found. Will not return an empty array.
	 */
	public double[] getValuesAtCurrentTime(String feature) {
		// Create the double array
		double[] values = null;
		// Check that the dataSet at the current time has the specified feature
		List<IData> data = getDataAtCurrentTime(feature);
		if (data != null) {
			// Initialize the Array
			int size = data.size();
			values = new double[size];
			// Fill in the values
			for (int i = 0; i < size; i++) {
				values[i] = data.get(i).getValue();
			}
		}
		return values;
	}

	/**
	 * Returns the uncertainties of each IData at the current time for a
	 * specified
	 * 
	 * @param feature
	 * @return
	 */
	public double[] getUncertaintiesAtCurrentTime(String feature) {
		// Create the double array
		double[] uncertainties = null;
		// Check that the dataSet at the current time has the specified feature
		if (!dataSet.get(currentTime).get(feature).isEmpty()) {
			// Get the ArrayList of IData
			List<IData> iDataSeries = dataSet.get(currentTime).get(feature);
			// Initialize the Array
			uncertainties = new double[iDataSeries.size()];
			// Fill in the values
			for (int i = 0; i < iDataSeries.size(); i++) {
				uncertainties[i] = iDataSeries.get(i).getUncertainty();
				if (uncertainties[i] < 0.0) {
					return null;
				}
			}
		}
		return uncertainties;
	}

	/**
	 * Returns the specified position/independent variable from the provider
	 * 
	 * @param independentVar
	 * @return
	 */
	public double[] getPositionAtCurrentTime(String independentVar) {
		// Create the position array
		double[] position = null;

		// Get the data for the independent variable.
		List<IData> independentVarData = getDataAtCurrentTime(independentVar);

		// If the data could be found, create the array of doubles based on its
		// values at the current time.
		if (independentVarData != null) {
			int size = independentVarData.size();
			position = new double[size];
			for (int i = 0; i < size; i++) {
				position[i] = independentVarData.get(i).getValue();
			}
		}

		return position;
	}

	/**
	 * Returns the dataWidth
	 * 
	 * @return
	 */
	public int getDataWidth() {
		return dataWidth;
	}

	/**
	 * Returns the dataHeight
	 * 
	 * @return
	 */
	public int getDataHeight() {
		return dataHeight;
	}

	/**
	 * Returns the data minimum
	 * 
	 * @return
	 */
	public double getDataMin() {
		return dataMin;
	}

	/**
	 * Returns the data maximum
	 * 
	 * @return
	 */
	public double getDataMax() {
		return dataMax;
	}

	/**
	 * Returns the source
	 */
	@Override
	public String getSourceInfo() {
		return source;
	}

	/**
	 * Returns the features at the current time
	 */
	@Override
	public ArrayList<String> getFeaturesAtCurrentTime() {
		return new ArrayList<String>(dataSet.get(currentTime).keySet());
	}

	/**
	 * Returns the times in the data set
	 */
	@Override
	public ArrayList<Double> getTimes() {
		// Get the list of times from the key set, sort it, and return it.
		ArrayList<Double> times = new ArrayList<Double>(dataSet.keySet());
		Collections.sort(times);
		return times;
	}

	/**
	 * Returns the integer time step at the given time
	 * 
	 * @param the
	 *            time, or -1 if the time is invalid.
	 */
	@Override
	public int getTimeStep(double time) {
		// Get the times and then pull the time step if it is in there.
		return getTimes().indexOf(time);
	}

	/**
	 * Returns the time units
	 */
	@Override
	public String getTimeUnits() {
		return timeUnits;
	}
}
