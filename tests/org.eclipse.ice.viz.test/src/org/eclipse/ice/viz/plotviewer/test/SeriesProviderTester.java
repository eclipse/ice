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

import org.eclipse.ice.viz.service.csv.CSVDataProvider;
import org.eclipse.ice.viz.service.csv.SeriesProvider;
import org.junit.Before;
import org.junit.Test;

/**
 * This class tests the functionality of the SeriesProvider.
 * 
 * @author Claire Saunders
 * 
 */
public class SeriesProviderTester {

	/**
	 * Class variables for to be used by this class.
	 */
	private CSVDataProvider dataProviderForSeries;
	private SeriesProvider seriesProvider;
	private double timeForDataProvider;
	private String seriesTitle;
	private String xDataFeature;
	private String yDataFeature;
	private String seriesType;
	private double max;
	private double min;

	/**
	 * Set the variables to be used throughout the testing.
	 */
	@Before
	public void BeforeClass() {

		dataProviderForSeries = new CSVDataProvider();
		seriesProvider = new SeriesProvider();
		timeForDataProvider = 1.0;
		seriesTitle = "Title of Series";
		xDataFeature = "x feature";
		yDataFeature = "y feature";
		seriesType = "Series Type";
		max = 10.0;
		min = 0.0;

		// Set the CSVDataProvider for the SeriesProvider
		seriesProvider.setDataProvider(dataProviderForSeries);

	}

	/**
	 * Check the accessor and mutator for the data maximum.
	 */
	@Test
	public void checkSetAndGetDataMax() {

		// Set the maximum in the SeriesProvider
		seriesProvider.setDataMax(max);

		// Check equality
		assertEquals(max, seriesProvider.getDataMax(), 1e-15);
		assertEquals(max, dataProviderForSeries.getDataMax(), 1e-15);

	}

	/**
	 * Check the accessor and mutator for the data minimum.
	 */
	@Test
	public void checkSetAndGetDataMin() {

		// Set the minimum in the SeriesProvider
		seriesProvider.setDataMin(min);

		// Check equality
		assertEquals(min, seriesProvider.getDataMin(), 1e-15);
		assertEquals(min, dataProviderForSeries.getDataMin(), 1e-15);

	}

	/**
	 * Check the accessor and mutator for the data provider.
	 */
	@Test
	public void checkSetAndGetDataProvider() {

		// Set the provider
		seriesProvider.setDataProvider(dataProviderForSeries);

		// Check equality
		assertEquals(dataProviderForSeries, seriesProvider.getDataProvider());
	}

	/**
	 * Check the accessor and mutator for the series title.
	 */
	@Test
	public void checkSetAndGetSeriesTitle() {

		// Set the seriesTitle
		seriesProvider.setSeriesTitle(seriesTitle);

		// Check equality
		assertEquals(seriesTitle, seriesProvider.getSeriesTitle());
	}

	/**
	 * Check the accessor and the mutator for the x data feature.
	 */
	@Test
	public void checkSetAndGetXDataFeature() {

		// Set the XData Feature
		seriesProvider.setXDataFeature(xDataFeature);

		// Check equality
		assertEquals(xDataFeature, seriesProvider.getXDataFeature());
	}

	/**
	 * Check the accessor and mutator for the y data feature.
	 */
	@Test
	public void checkSetAndGetYDataFeature() {

		// Set the YData Feature
		seriesProvider.setYDataFeature(yDataFeature);

		// Check equality
		assertEquals(yDataFeature, seriesProvider.getYDataFeature());

	}

	/**
	 * Check the accessor and mutator for the series type.
	 */
	@Test
	public void checkSetAndGetSeriesType() {

		// Set the series type
		seriesProvider.setSeriesType(seriesType);

		// Check equality
		assertEquals(seriesType, seriesProvider.getSeriesType());

	}

	/**
	 * Check the accessor and mutator for the time for the data provider.
	 */
	@Test
	public void checkSetAndGetTimeForDataProvider() {

		// Set the time for the dataProvider
		seriesProvider.setTimeForDataProvider(timeForDataProvider);

		// Check equality
		assertEquals(timeForDataProvider,
				seriesProvider.getTimeForDataProvider(), 1e-15);

	}

	/**
	 * Check the accessor and mutator for the data provider.
	 */
	@Test
	public void checkSetandGetDataProvider() {

		// Set the dataProvider
		seriesProvider.setDataProvider(dataProviderForSeries);

		// Check equality
		assertEquals(dataProviderForSeries, seriesProvider.getDataProvider());

	}

}
