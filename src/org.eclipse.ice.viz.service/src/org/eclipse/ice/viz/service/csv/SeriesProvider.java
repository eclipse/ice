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

import java.util.HashMap;

import org.apache.commons.math.linear.RealMatrix;

/**
 * The series provider class will provide the data for a trace/series as well as
 * meta data such as the title, the type, and the uncertainty
 * 
 * @author Matthew Wang, Alex McCaskey
 * 
 */
public class SeriesProvider {
	/**
	 * The data provider that this series will pull from
	 */
	private RealMatrix csvData;
	
	/**
	 * The dataProvider's time for this series
	 */
	private double timeForDataProvider;
	
	/**
	 * The minimum value in this data set
	 */
	private double max;
	
	/**
	 * The maximum value in this data set. 
	 */
	private double min;
	
	/**
	 * The map from string feature/variable names to 
	 * their corresponding column index in the CSV Matrix.
	 */
	private HashMap<String, Integer> featureMap;

	/**
	 * The title of the series
	 */
	private String seriesTitle;
	
	/**
	 * The feature of the xData
	 */
	private String xDataFeature;
	
	/**
	 * The feature of the yData
	 */
	private String yDataFeature;
	
	/**
	 * The series type (Scatter, Bar, Line, etc.)
	 */
	private String seriesType;

	/**
	 * Default constructor
	 */
	public SeriesProvider() {
		seriesTitle = null;
		xDataFeature = null;
		yDataFeature = null;
		seriesType = null;
	}

	/**
	 * Comprehensive constructor with the bare essentials for a series
	 * 
	 * @param seriesTitle
	 * @param xDataFeature
	 * @param yDataFeature
	 * @param seriesType
	 */
	public SeriesProvider(String seriesTitle, String xDataFeature,
			String yDataFeature, String seriesType) {
		this.seriesTitle = seriesTitle;
		this.xDataFeature = xDataFeature;
		this.yDataFeature = yDataFeature;
		this.seriesType = seriesType;
	}

	/**
	 * Mutator for the max
	 * 
	 * @param max
	 */
	public void setDataMax(double max) {
		this.max = max;
	}

	/**
	 * Mutator for the min
	 * 
	 * @param min
	 */
	public void setDataMin(double min) {
		this.min = min;
	}

	/**
	 * Mutator for the series title
	 * 
	 * @param seriesTitle
	 */
	public void setSeriesTitle(String seriesTitle) {
		this.seriesTitle = seriesTitle;
	}

	/**
	 * Set the x data's feature
	 * 
	 * @param xFeature
	 */
	public void setXDataFeature(String xFeature) {
		this.xDataFeature = xFeature;
	}

	/**
	 * Set the y data's feature
	 * 
	 * @param yFeature
	 */
	public void setYDataFeature(String yFeature) {
		this.yDataFeature = yFeature;
	}

	/**
	 * Set the series type
	 * 
	 * @param type
	 */
	public void setSeriesType(String type) {
		this.seriesType = type;
	}

	public void setTimeForDataProvider(double time) {
		this.timeForDataProvider = time;
	}

	/**
	 * Accessor for the time the data provider is set for
	 * 
	 * @return
	 */
	public double getTimeForDataProvider() {
		return this.timeForDataProvider;
	}

	/**
	 * Accessor for the plot title
	 * 
	 * @return
	 */
	public String getSeriesTitle() {
		return this.seriesTitle;
	}

	/**
	 * Accessor for the xData
	 * 
	 * @return
	 */
	public double[] getXData() {
		return csvData.getColumn(featureMap.get(xDataFeature));
	}

	/**
	 * Accessor for the yData
	 * 
	 * @return
	 */
	public double[] getYData() {
		return csvData.getColumn(featureMap.get(yDataFeature));
	}

	/**
	 * Accessor for the x data's feature
	 * 
	 * @return
	 */
	public String getXDataFeature() {
		return this.xDataFeature;
	}

	/**
	 * Accessor for the y data's feature
	 * 
	 * @return
	 */
	public String getYDataFeature() {
		return this.yDataFeature;
	}

	/**
	 * Accessor for the series type
	 * 
	 * @return
	 */
	public String getSeriesType() {
		return this.seriesType;
	}

	/**
	 * Accessor for the data minimum
	 * 
	 * @return
	 */
	public double getDataMin() {
		return min;
	}

	/**
	 * Accessor for the data maximum
	 * 
	 * @return
	 */
	public double getDataMax() {
		return max;
	}

	/**
	 * Accessor for the data width
	 * 
	 * @return
	 *
	publc int getDataWidth() {
		return this.dataProviderForSeries.getDataWidth();
	}

	/**
	 * Accessor for the data height
	 * 
	 * @return
	 *
	public int getDataHeight() {
		return this.dataProviderForSeries.getDataHeight();
	}
*/
	/**
	 * Set the Matrix of data loaded from the CSV file 
	 * @param csv
	 * @param featureToIndexMap
	 */
	public void setCSVData(RealMatrix csv, HashMap<String, Integer> featureToIndexMap) {
		csvData = csv;
		featureMap = featureToIndexMap;
	}
}
