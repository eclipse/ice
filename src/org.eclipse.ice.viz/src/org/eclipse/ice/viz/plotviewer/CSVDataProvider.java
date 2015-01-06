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
package org.eclipse.ice.viz.plotviewer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

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
	private HashMap<Double, HashMap<String, ArrayList<IData>>> dataSet;

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
	private ArrayList<String> independentVars;

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
		dataSet = new HashMap<Double, HashMap<String, ArrayList<IData>>>();
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
			HashMap<String, ArrayList<IData>> dataSetComponent = new HashMap<String, ArrayList<IData>>();
			// Initialize the HashMap and add in the feature and an empty
			// ArrayList
			dataSetComponent.put(feature, new ArrayList<IData>());
			// Add the data to the ArrayList
			dataSetComponent.get(feature).add(data);
			// Add this new HashMap to the dataSet
			dataSet.put(time, dataSetComponent);
		}
	}

	/**
	 * Adds a single data item with no specified time. It calls the addData with
	 * time and uses the default time instead.
	 * 
	 * @param data
	 */
	public void addData(IData data) {
		// Add the single data with no given time to the dataSet
		this.addData(defaultTime, data);
	}

	/**
	 * Adds a data series to the data set at a specified time. At the moment, if
	 * the data series already exists, it does nothing.
	 * 
	 * @param time
	 * @param dataSeries
	 */
	public void addDataSeries(double time, ArrayList<IData> dataSeries) {
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
			HashMap<String, ArrayList<IData>> dataSetComponent = new HashMap<String, ArrayList<IData>>();
			// Sets the given feature and dataSeries
			dataSetComponent.put(feature, dataSeries);
			// Adds the HashMap of the feature to the time
			dataSet.put(time, dataSetComponent);
		}

	}

	/**
	 * Adds a data series with no specified time. It calls addDataSeries with a
	 * specified time and uses the defaultTime as the time.
	 * 
	 * @param dataSeries
	 */
	public void addDataSeries(ArrayList<IData> dataSeries) {
		// Add the dataSeries with no given time to the dataSet
		this.addDataSeries(defaultTime, dataSeries);
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
	@Override
	/**
	 * Sets the currentTime based on the specified step/time. Checks if the time exists first. 
	 */
	public void setTime(double step) {
		// TODO Auto-generated method stub
		if (dataSet.containsKey(step)) {
			currentTime = step;
		} else {
			// Invalid time
		}

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
			ArrayList<IData> data = dataSet.get(prevTime).get(feature);
			// Removes the data from the dataSet
			this.removeDataSeries(prevTime, feature);
			// Adds the data into the newTime
			this.addDataSeries(newTime, data);
		} else {
			// Throw invalid time exception
		}
	}

	/**
	 * Sets a new time for a specified feature and assumes the previous time was
	 * defaultTime
	 * 
	 * @param newTime
	 * @param feature
	 */
	public void setTimeForFeature(double newTime, String feature) {
		this.setTimeForFeature(defaultTime, newTime, feature);
	}

	/**
	 * Sets a feature as a independent variable/position across all times (May
	 * need to check that the independent variable exists across all times.
	 * Right now just assumes it does)
	 * 
	 * @param feature
	 */
	public void setFeatureAsIndependentVariable(String feature) {
		ArrayList<Double> times = this.getTimes();
		// Loop through each time to set the independent variable and remove it
		// from the features
		for (Double time : times) {
			this.setTime(time);
			ArrayList<IData> independentVarData = this
					.getDataAtCurrentTime(feature);
			// Get the features at the selected time
			ArrayList<String> featuresAtSelectedTime = this
					.getFeaturesAtCurrentTime();
			// Remove the independent variable from the features
			featuresAtSelectedTime.remove(featuresAtSelectedTime
					.indexOf(feature));
			for (String featureIndex : featuresAtSelectedTime) {
				ArrayList<IData> independentVar = this
						.getDataAtCurrentTime(featureIndex);
				// loop through each data
				for (int i = 0; i < independentVar.size(); i++) {
					// set the position
					((CSVData) independentVar.get(i))
							.addPosition(independentVarData.get(i).getValue());
				}
			}
			// remove the independent variable feature
			dataSet.get(time).remove(feature);

			// Need to clean up that ArrayList<IData> independentVarData
		}
		// Add to the independentVar list
		if (!independentVars.contains(feature)) {
			independentVars.add(feature);
		}
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
		return independentVars;
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

	@Override
	/**
	 * Returns the lists of features across all times
	 */
	public ArrayList<String> getFeatureList() {
		// TODO Auto-generated method stub
		ArrayList<String> featureList = new ArrayList<String>();
		for (Double time : dataSet.keySet()) {
			for (String feature : dataSet.get(time).keySet()) {
				if (!featureList.contains(feature)) {
					featureList.add(feature);
				}
			}
		}
		return featureList;
	}

	@Override
	/**
	 * Returns the number of time steps
	 */
	public int getNumberOfTimeSteps() {
		// TODO Auto-generated method stub
		return dataSet.size();
	}

	@Override
	/**
	 * Returns the series of data at the set current time for a specified feature
	 */
	public ArrayList<IData> getDataAtCurrentTime(String feature) {
		// TODO Auto-generated method stub
		// Check that the dataSet at the current time has the specified feature
		if (!dataSet.get(currentTime).get(feature).isEmpty()) {
			return dataSet.get(currentTime).get(feature);
		} else {
			// Feature does not exist at the current time
			return null;
		}
	}

	/**
	 * Returns the values of each IData at the current time for a specified
	 * 
	 * @param feature
	 * @return
	 */
	public double[] getValuesAtCurrentTime(String feature) {
		// Create the double array
		double[] values = null;
		// Check that the dataSet at the current time has the specified feature
		if (!dataSet.get(currentTime).get(feature).isEmpty()) {
			// Get the ArrayList of IData
			ArrayList<IData> iDataSeries = dataSet.get(currentTime)
					.get(feature);
			// Initialize the Array
			values = new double[iDataSeries.size()];
			// Fill in the values
			for (int i = 0; i < iDataSeries.size(); i++) {
				values[i] = iDataSeries.get(i).getValue();
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
			ArrayList<IData> iDataSeries = dataSet.get(currentTime)
					.get(feature);
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

		// Get the features at the current time
		ArrayList<String> features = getFeaturesAtCurrentTime();
		// Check that the dataSet at the current time has the specified
		// independent variable
		if (independentVars.contains(independentVar)) {
			// The index of the independent variable
			int independentVarIndex = independentVars.indexOf(independentVar);
			// Get the ArrayList of IData
			ArrayList<IData> iDataSeries = this.getDataAtCurrentTime(features
					.get(0));
			// Initialize the Array
			position = new double[iDataSeries.size()];
			// Fill in the values
			for (int i = 0; i < iDataSeries.size(); i++) {
				position[i] = iDataSeries.get(i).getPosition()
						.get(independentVarIndex);
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
		return this.dataWidth;
	}

	/**
	 * Returns the dataHeight
	 * 
	 * @return
	 */
	public int getDataHeight() {
		return this.dataHeight;
	}

	/**
	 * Returns the data minimum
	 * 
	 * @return
	 */
	public double getDataMin() {
		return this.dataMin;
	}

	/**
	 * Returns the data maximum
	 * 
	 * @return
	 */
	public double getDataMax() {
		return this.dataMax;
	}

	@Override
	/**
	 * Returns the source
	 */
	public String getSourceInfo() {
		// TODO Auto-generated method stub
		return source;
	}

	@Override
	/**
	 * Returns the features at the current time
	 */
	public ArrayList<String> getFeaturesAtCurrentTime() {
		return new ArrayList<String>(dataSet.get(currentTime).keySet());
	}

	@Override
	/**
	 * Returns the times in the data set
	 */
	public ArrayList<Double> getTimes() {
		// Get the list of times from the key set, sort it, and return it.
		ArrayList<Double> times = new ArrayList<Double>(dataSet.keySet());
		Collections.sort(times);
		times = new ArrayList<Double>(times);
		return times;
	}

	@Override
	/**
	 * Returns the integer time step at the given time
	 * @param the time
	 */
	public int getTimeStep(double time) {
		// Get the times and then pull the time step if it is in there.
		ArrayList<Double> times = getTimes();
		return times.indexOf(time);
	}

	@Override
	/**
	 * Returns the time units
	 */
	public String getTimeUnits() {
		// TODO Auto-generated method stub
		return timeUnits;
	}
}
