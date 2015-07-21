/*******************************************************************************
 * Copyright (c) 2014-2015 UT-Battelle, LLC.
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
import org.eclipse.ice.viz.service.MultiPlot;
import org.eclipse.ice.viz.service.PlotRender;
import org.eclipse.ice.viz.service.styles.XYZAxisStyle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
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
public class CSVPlot extends MultiPlot {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory.getLogger(CSVPlot.class);

	/**
	 * The axis style used to decorate the x axis for this plot
	 */
	private XYZAxisStyle xAxisStyle;

	/**
	 * The axis style used to decorate the y axis for this plot
	 */
	private XYZAxisStyle yAxisStyle;

	/**
	 * The Constructor
	 *
	 * @param source
	 *            The URI of the CSV file.
	 */
	public CSVPlot(URI source) {
		// Just call the super constructor
		super(source);
		xAxisStyle = null;
		yAxisStyle = null;
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
	 * Attepts to load the CSV series data from the specified file. This
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
		this.setPlotTitle(plotName);

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

			// TODO- Some sort of implementation to read in the style
			// configurations for the plot, axes, and series. How to go about
			// this? A large part of the series implementation is not being
			// utilized without some sort of recognition here of the style
			// attributes!

			// Assume that the first line has information about the data
			String[] seriesNames = lines.remove(0);

			// Creates the
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

			// Just set the first series as the independent series for now
			this.setIndependentSeries(series[0]);

			// Add the rest of the series as dependent series
			for (int i = 1; i < series.length; i++) {
				this.addDependentSeries(series[i]);
			}

		}

		return;
	}

	/**
	 * Always returns two as CSV plots are always 2D
	 * 
	 * @see org.eclipse.ice.viz.service.IVizCanvas#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// The CSV plots are always 2D
		return 2;
	}

	/**
	 * Sets the new style for the csv plot. If null, the style will revert to
	 * the default settings when being drawn in the editor.
	 * 
	 * @param newStyle
	 */
	public void setXAxisStyle(XYZAxisStyle newStyle) {
		xAxisStyle = newStyle;
	}

	/**
	 * Sets the new style for the csv plot. If null, the style will revert to
	 * the default settings when being drawn in the editor.
	 * 
	 * @param newStyle
	 */
	public void setYAxisStyle(XYZAxisStyle newStyle) {
		yAxisStyle = newStyle;
	}

	/**
	 * Gets the axis style used to decorate the x axis. If null, then the editor
	 * will use the default values.
	 * 
	 * @return XYZAxisStyle the style used.
	 */
	public XYZAxisStyle getXAxisStyle() {
		return xAxisStyle;
	}

	/**
	 * Gets the axis style used to decorate the y axis. If null, then the editor
	 * will use the default values.
	 * 
	 * @return XYZAxisStyle the style used
	 */
	public XYZAxisStyle getYAxisStyle() {
		return yAxisStyle;
	}

	/**
	 * @see org.eclipse.ice.viz.service.IPlot#draw(java.lang.String,
	 *      java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {

		// Create the plot render if it doesn't exist and legitimize the parent
		// composite for use with this plot. Using the functionality of
		// MultiPlot.draw(Composite) here
		Composite child = null;

		// Check the parameters.
		if (parent == null) {
			throw new NullPointerException("IPlot error: "
					+ "Null arguments are not allowed when drawing plot.");
		} else if (parent.isDisposed()) {
			throw new SWTException(SWT.ERROR_WIDGET_DISPOSED, "IPlot error: "
					+ "Cannot draw plot inside disposed Composite.");
		}

		// Get the PlotRender associated with the parent Composite.
		PlotRender plotRender = plotRenders.get(parent);

		// Create the PlotRender and associate it with the parent as necessary.
		if (plotRender == null) {
			plotRender = createPlotRender(parent);
			plotRenders.put(parent, plotRender);
		}

		// Get the drawn plot associated with the parent Composite, creating
		// a new editor if necessary.
		plotRender = (CSVPlotRender) plotRenders.get(parent);

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

		// Trigger the appropriate update to the PlotRender's content.
		updatePlotRender(plotRender);

		// Refresh the drawn plot.
		plotRender.refresh();

		// We need to return the Composite used to render the CSV plot. Does not
		// really sync up with MultiPlot's draw method (which returns its own
		// plot composite rather than something from an editor), but that is ok
		// for now.
		child = ((CSVPlotRender) plotRender).getEditor().getPlotCanvas();

		// Return the child composite
		return child;

	}

	/**
	 * Implements the method createPlotRender (Composite) for the multiplot.
	 * This is essential for extending the multiplot behavior. Just return a new
	 * CSVPlotRender so the draw(composite) method can use it.
	 * 
	 * @return PlotRender A new CSVPlotRender
	 */
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
						// Redraw the plot
						draw(comp);
					} catch (Exception e) {
						logger.error(getClass().getName() + " Exception!", e);
					}
				}
			});

		}
	}

}
