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

/**
 * The series provider class will provide the data for a trace/series as well as
 * meta data such as the title, the type, and the uncertainty
 * 
 * @author Matthew Wang
 * 
 */
public class SeriesProvider {
	/**
	 * The dataProvider that this series will pull from
	 */
	private CSVDataProvider dataProviderForSeries;
	/**
	 * The dataProvider's time for this series
	 */
	private double timeForDataProvider;
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
		this.dataProviderForSeries.setDataMax(max);
	}

	/**
	 * Mutator for the min
	 * 
	 * @param min
	 */
	public void setDataMin(double min) {
		this.dataProviderForSeries.setDataMin(min);
	}

	/**
	 * Mutator for the data provider to use for this series
	 * 
	 * @param dataProviderForSeries
	 */
	public void setDataProvider(CSVDataProvider dataProviderForSeries) {
		this.dataProviderForSeries = dataProviderForSeries;
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
	 * Accessor for the data provider to use for this series
	 * 
	 * @return
	 */
	public CSVDataProvider getDataProvider() {
		return this.dataProviderForSeries;
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
		// Set the time for the dataProvider
		System.out.println("TIME" + timeForDataProvider + " " + xDataFeature);
		dataProviderForSeries.setTime(timeForDataProvider);
		// Return the data
		return dataProviderForSeries.getPositionAtCurrentTime(xDataFeature);
	}

	/**
	 * Accessor for the yData
	 * 
	 * @return
	 */
	public double[] getYData() {
		// Set the time for the dataProvider
		dataProviderForSeries.setTime(timeForDataProvider);
		// return the data
		return this.dataProviderForSeries.getValuesAtCurrentTime(yDataFeature);
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
	 * Accessor for the xDataUncertainty
	 * 
	 * @return
	 */
	public double[] getXDataUncertainty() {
		return this.dataProviderForSeries
				.getUncertaintiesAtCurrentTime(xDataFeature);
	}

	/**
	 * Accessor for the yDataUncertianty
	 * 
	 * @return
	 */
	public double[] getYDataUncertainty() {
		return this.dataProviderForSeries
				.getUncertaintiesAtCurrentTime(yDataFeature);
	}

	/**
	 * Accessor for the data minimum
	 * 
	 * @return
	 */
	public double getDataMin() {
		return this.dataProviderForSeries.getDataMin();
	}

	/**
	 * Accessor for the data maximum
	 * 
	 * @return
	 */
	public double getDataMax() {
		return this.dataProviderForSeries.getDataMax();
	}

	/**
	 * Accessor for the data width
	 * 
	 * @return
	 */
	public int getDataWidth() {
		return this.dataProviderForSeries.getDataWidth();
	}

	/**
	 * Accessor for the data height
	 * 
	 * @return
	 */
	public int getDataHeight() {
		return this.dataProviderForSeries.getDataHeight();
	}
}
