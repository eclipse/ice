/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation -
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service.csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.math.linear.RealMatrix;
import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.ice.viz.service.MultiPlot;
import org.eclipse.ice.viz.service.PlotRender;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class implements the IPlot interface to provide access to a basic CSV
 * plot using the existing CSV infrastructure in ICE.
 *
 * In addition to the IPlot operations it provides the load() operation that
 * should be called after construction.
 *
 * @author Jay Jay Billings, Anna Wojtowicz, Alex McCaskey
 *
 */
public class CSVPlot extends MultiPlot {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(CSVPlot.class);

	/**
	 * Reference to the read-in matrix of CSV data.
	 */
	private RealMatrix csvData;

	/**
	 * The Constructor
	 *
	 * @param source
	 *            The URI of the CSV file.
	 */
	public CSVPlot(URI source) {
		// Just call the super constructor
		super(source);
		return;
	}

	/**
	 * This operation loads the data that will be plotted. It uses a separate
	 * thread to avoid hanging the UI in the event that the file is large. It
	 * does not attempt to load the file if the source is null.
	 *
	 */
	public void load() {

		if (source != null) {
			// Only load the file if it is a CSV file.
			final File file = new File(source);
			if (file.getName().endsWith(".csv")) {
				// Create the loading thread.
				Thread loadingThread = new Thread(new Runnable() {
					@Override
					public void run() {
						load(file);
					}
				});

				// Force the loading thread to report unhandled exceptions to
				// this thread's exception handler.
				loadingThread.setUncaughtExceptionHandler(
						Thread.currentThread().getUncaughtExceptionHandler());

				// Start the thread
				loadingThread.start();
			}
		}

		return;
	}

	/**
	 * Attempts to load the specified file. This should populate the
	 * {@link #baseProvider} as well as the map of {@link #types} or plot
	 * series.
	 *
	 * @param file
	 *            The file to load. This is assumed to be a valid file.
	 */
	private void load(File file) {

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
			logger.error(getClass().getName() + " Exception!", e);
		}

		if (!lines.isEmpty()) {

			// Assume that the first line has information about the data
			String[] seriesNames = lines.remove(0);

			// Creates the
			CSVSeries[] series = new CSVSeries[seriesNames.length];
			for (int i = 0; i < seriesNames.length; i++) {
				series[i] = new CSVSeries();
				series[i].setEnabled(false);
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

			// Just set the first series as the independent series for now
			this.setIndependentSeries(series[0]);

			// Add the rest of the series as dependent series
			for (int i = 1; i < series.length; i++) {
				this.addDependantSeries(series[i]);
			}

		}

		return;
	}

	/**
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// The CSV plots are always 2D
		return 2;
	}

	/**
	 * @see org.eclipse.ice.viz.service.IPlot#draw(java.lang.String,
	 *      java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {

		if (plotRenders.get(parent) == null) {
			super.draw(parent);
		}
		// Create the plot render and legitimize the parent composite for use
		// with this plot using MultiPlot's draw method
		Composite child = super.draw(parent);

		// Get the drawn plot associated with the parent Composite, creating
		// a new editor if necessary.
		CSVPlotRender plotRender = (CSVPlotRender) plotRenders.get(parent);

		// When the parent is disposed, remove the drawn plot and
		// dispose of any of its resources.
		parent.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				((CSVPlotRender) plotRenders.remove(e.widget)).dispose();
			}
		});

		// Reset the plot time to the initial time.
		// double plotTime = 0.0;// baseProvider.getTimes().get(0);
		// FIXME Won't this affect all of the drawn plots?
		// baseProvider.setTime(plotTime);

		// Remove all previous plots.
		plotRender.clear();

		for (ISeries s : getAllDependentSeries()) {
			plotRender.addSeries(s);
		}

		// Refresh the drawn plot.
		plotRender.refresh();

		// We need to return the Composite used to render the CSV plot.
		child = plotRender.getEditor().getPlotCanvas();

		// Return the child composite
		return child;
	}

	@Override
	protected PlotRender createPlotRender(Composite parent) {
		return new CSVPlotRender(parent, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#redraw()
	 */
	@Override
	public void redraw() {
		// Start off by reloading this IPlot's representative data set.
		load();

		// Then loop over all drawn plots and re-execute the draw method.
		for (final Composite comp : plotRenders.keySet()) {
			comp.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					try {
						draw(comp);
					} catch (Exception e) {
						logger.error(getClass().getName() + " Exception!", e);
					}
				}
			});

		}
	}

}
