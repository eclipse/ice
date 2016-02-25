/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Jay Jay Billings
 *   Kasper Gammeltoft - viz series refactor
 *   Jordan Deyton - viz multi-series refactor 
 *******************************************************************************/
package org.eclipse.eavp.viz.service.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.beanutils.ConvertUtils;
import org.eclipse.eavp.viz.service.AbstractPlot;
import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.ISeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the IPlot interface to provide access to a basic CSV
 * plot using the existing CSV infrastructure in ICE.
 *
 * In addition to the IPlot operations it provides the load() operation that
 * should be called after construction if you want to take the data and series
 * information from the specified URI. Otherwise, it is necessary to manually
 * add the series information. This second option is required to set custom
 * settings for this plot, specifically for the style of the series and
 * specifying which one is the independent variable.
 *
 * @author Jay Jay Billings, Anna Wojtowicz, Alex McCaskey
 * @author Kasper Gammeltoft- Updated to extend MultiPlot, added ISeries
 *         functionality, updated to use {@link CSVPlotRender}s rather than
 *         DrawnPlots.
 *
 */
public class CSVPlot extends AbstractPlot {

	// TODO All series should be stored in the map, including the independent
	// series. This way if the independent series is cleared, its data is not
	// lost.

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(CSVPlot.class);

	/**
	 * A map containing all dependent series, keyed on the categories.
	 */
	private final Map<String, List<ISeries>> dataSeries;

	/**
	 * Whether or not the data has been loaded.
	 */
	private final AtomicBoolean loaded = new AtomicBoolean(false);

	/**
	 * The default constructor.
	 */
	public CSVPlot() {
		dataSeries = new HashMap<String, List<ISeries>>();

		// Initially set the title to an empty string because the CSVPlotEditor
		// does not like null titles.
		setPlotTitle("");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#getCategories()
	 */
	@Override
	public List<String> getCategories() {
		// Use the map of ProxySeries, which has the categories.
		return new ArrayList<String>(dataSeries.keySet());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.eavp.viz.service.AbstractPlot#getDependentSeries(java.lang
	 * .String)
	 */
	@Override
	public List<ISeries> getDependentSeries(String category) {
		// Use the map of ProxySeries to get a new list of ISeries associated
		// with the category.
		List<ISeries> series = dataSeries.get(category);
		if (series != null) {
			series = new ArrayList<ISeries>(series);
		}
		return series;
	}

	/**
	 * Always returns two as CSV plots are always 2D
	 * 
	 * @see org.eclipse.eavp.viz.service.IVizCanvas#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// The CSV plots are always 2D
		return 2;
	}

	/**
	 * Gets whether or not data has been loaded from the data source.
	 * 
	 * @return True if data has been loaded, false otherwise.
	 */
	public boolean isLoaded() {
		return loaded.get();
	}

	/**
	 * This operation loads the data that will be plotted. It uses a separate
	 * thread to avoid hanging the UI in the event that the file is large. It
	 * does not attempt to load the file if the source is null.
	 *
	 */
	public void load() {

		URI uri = getDataSource();
		if (uri != null) {
			// Only load the file if it is a CSV file.
			final File file = new File(uri);
			if (file.getName().endsWith(".csv")) {
				// Loading has not completed.
				loaded.set(false);

				// Create the loading thread.
				Thread loadingThread = new Thread(new Runnable() {
					@Override
					public void run() {
						load(file);
					}
				});

				// Force the loading thread to report unhandled exceptions to
				// this thread's exception handler.
				loadingThread.setUncaughtExceptionHandler(Thread
						.currentThread().getUncaughtExceptionHandler());

				// Start the thread
				loadingThread.start();
			} else {
				logger.error(getClass().getName() + ": Failed to load file "
						+ file.getName() + ", it must be of type .csv");
			}
		}

		return;
	}

	/**
	 * Attempts to load the CSV series data from the specified file. This
	 * operation ignores all data that is marked as comments (i.e. if the line
	 * starts with #), and ignores comments after the data in a line as well.
	 * This operation takes the first column of CSV data as the independent
	 * series, and the rest as normal, dependent series to be added to the plot.
	 * Note that only the first dependent series (the second column) will be
	 * initially enabled to be drawn on the plot editor.
	 * 
	 * @param file
	 *            The file to load, assumed to be a valid file.
	 */
	private void load(File file) {
		// Initially set the name to the file name.
		String plotName = file.getName();
		setPlotTitle(plotName);

		// Configure the list
		ArrayList<String[]> lines = new ArrayList<String[]>();

		try {
			// Grab the contents of the file
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = null;
			while ((line = reader.readLine()) != null) {
				// Skip lines that pure comments
				if (!line.startsWith("#")) {
					// Clip the line if it has a comment symbol in it to be
					// everything before the symbol
					if (line.contains("#")) {
						int index = line.indexOf("#");
						line = line.substring(0, index);
					}
					// Clean up any crap on the line
					String[] lineArray = line.trim().split(",");
					String[] trimmedLine = new String[lineArray.length];
					// And clean up any crap on each split piece
					for (int i = 0; i < lineArray.length; i++) {
						trimmedLine[i] = lineArray[i].trim();
					}
					// Put the lines in the list
					lines.add(trimmedLine);
				}
			}

			reader.close();

		} catch (IOException e) {
			// Complain
			logger.error(
					getClass().getName()
							+ " Exception! Could not read in data from file: "
							+ file.getName() + ".", e);
		}

		if (!lines.isEmpty()) {

			// TODO- Some sort of implementation to read in the style
			// configurations for the plot, axes, and series. How to go about
			// this? A large part of the series implementation is not being
			// utilized without some sort of recognition here of the style
			// attributes!

			// Assume that the first line has information about the data
			String[] seriesNames = lines.remove(0);

			// Creates the series that contain the data loaded from the file.
			CSVSeries[] series = new CSVSeries[seriesNames.length];
			for (int i = 0; i < seriesNames.length; i++) {
				series[i] = new CSVSeries();
				series[i].setEnabled(false);
				series[i].setLabel(seriesNames[i]);
			}

			// Sets the first two series to be automatically plotted
			series[0].setEnabled(true);
			if (series.length > 1) {
				series[1].setEnabled(true);
			}

			// Load the data as doubles
			for (int i = 0; i < lines.size(); i++) {
				double[] values = (double[]) ConvertUtils.convert(lines.get(i),
						Double.TYPE);
				for (int j = 0; j < values.length; j++) {
					series[j].add(values[j]);
				}
			}

			// If the independent series has not been set, use the default value just created 
			if (getIndependentSeries() == null) {
				// Just set the first series as the independent series for now
				setIndependentSeries(series[0]);
			} else {
				// Otherwise, update the series with the new data
				for (CSVSeries s : series) {
					ISeries current = getIndependentSeries();
					if (current.getLabel().equals(s.getLabel())) {
						setIndependentSeries(s);
						break;
					}
				}
			}
			
			// Add the rest of the series as dependent series
			List<ISeries> dependentSeries = new ArrayList<ISeries>(
					series.length);
			for (int i = 1; i < series.length; i++) {
				dependentSeries.add(series[i]);
			}
			dependentSeries.add(series[0]);
			dataSeries.put(IPlot.DEFAULT_CATEGORY, dependentSeries);

		}

		// Loading has completed.
		loaded.set(true);

		// Notify the listeners that loading has completed.
		notifyPlotListeners("loaded", "true");

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#redraw()
	 */
	@Override
	public void redraw() {
		// Start off by reloading this IPlot's representative data set.
		load();

		// We don't actually draw with a CSVPlot, but with CSVProxyPlots.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#setDataSource(java.net.URI)
	 */
	@Override
	public boolean setDataSource(URI uri) throws Exception {
		boolean changed = super.setDataSource(uri);
		if (changed) {
			// Unregister from the old data source URI.
			dataSeries.clear();

			// Register with the new data source URI.
			load();
		}
		return changed;
	}

}
