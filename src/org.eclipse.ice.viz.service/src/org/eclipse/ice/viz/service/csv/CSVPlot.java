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

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.viz.plotviewer.CSVDataLoader;
import org.eclipse.ice.viz.plotviewer.CSVDataProvider;
import org.eclipse.ice.viz.plotviewer.CSVPlotEditor;
import org.eclipse.ice.viz.plotviewer.PlotProvider;
import org.eclipse.ice.viz.plotviewer.SeriesProvider;
import org.eclipse.swt.widgets.Composite;

/**
 * This class implements the IPlot interface to provide access to a basic CSV
 * plot using the existing CSV infrastructure in ICE.
 * 
 * In addition to the IPlot operations it provides the load() operation that
 * should be called after construction.
 * 
 * @author Jay Jay Billings, Anna Wojtowicz
 *
 */
public class CSVPlot implements IPlot {

	/**
	 * The source of the data for this plot
	 */
	private URI source;

	/**
	 * The plot properties
	 */
	private Map<String, String> properties;

	/**
	 * The types that this plot can assume
	 */
	private Map<String, String[]> types;

	/**
	 * The CSVDataProvider used to store the CSV data
	 */
	private CSVDataProvider provider;	
	
	/**
	 * The Constructor
	 * 
	 * @param source	The URI of the CSV file.
	 */
	public CSVPlot(URI source) {
		this.source = source;
		// Create the property map, empty by default
		properties = new HashMap<String, String>();
		// Create the plot type map and add empty arrays by default
		types = new Hashtable<String, String[]>();
		String[] emptyStringArray = {};
		types.put("line", emptyStringArray);
		types.put("scatter", emptyStringArray);
		
		return;
	}

	/**
	 * This operation loads the data that will be plotted. It uses a separate
	 * thread to avoid hanging the UI in the event that the file is large. It
	 * does not attempt to load the file if the source is null.
	 * 
	 * @throws Exception 
	 */
	public void load() {

		if (source != null) {
			
			// Create the loading thread
			Thread loadingThread = new Thread(new Runnable() {
				@Override
				public void run() {
					
					// Create a file handle from the source
					File file = new File(source);
					// Get a CSV loader and try to load the file
					if (file.getName().endsWith("csv")) {
						// Load the file
						CSVDataLoader dataLoader = new CSVDataLoader();
						try {
							provider = dataLoader.load(file);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						// Set the source so the title and everything gets
						// loaded right later
						provider.setSource(source.toString());
						// Get the variables
						ArrayList<String> variables = provider.getFeatureList();
						// Set the first feature as an independent variable
						provider.setFeatureAsIndependentVariable(variables
								.get(0));
						// Create lists to hold the plot types
						ArrayList<String> plotTypes = new ArrayList<String>(
								variables.size());
						// Create the type list. Loop over every variable and
						// make it possible to plot it against the others.
						String[] emptyStringArray = {};
						for (int i = 0; i < variables.size(); i++) {
							for (int j = 0; j < variables.size(); j++) {
								if (i != j) {
									String type = variables.get(i) + " vs. "
											+ variables.get(j);
									plotTypes.add(type);
								}
							}
						}
						// Put the types in the map
						types.put("line", plotTypes.toArray(emptyStringArray));
						// Add the qualifier
						types.put("scatter",
								plotTypes.toArray(emptyStringArray));
						
					}
				}
			});
			
			// Get a handle on the parent thread's exception handler
			final UncaughtExceptionHandler parentHandler = 
					Thread.currentThread().getUncaughtExceptionHandler();
			
			// Override the loadingThread's exception handler
			loadingThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
				@Override
				public void uncaughtException(Thread t, Throwable e) {
					// Pass the exception to the parent thread's handler
					parentHandler.uncaughtException(t, e);
				}
			});
		
			// Start the thread
			loadingThread.start();
		}
		
		return;
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getPlotTypes()
	 */
	@Override
	public Map<String, String[]> getPlotTypes() throws Exception {
		return new HashMap<String, String[]>(types);
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// The CSV plots are always 2D
		return 2;
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return properties;
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#setProperties(java.util.Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// Do nothing
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return source;
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		return source.getHost();
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {

		boolean retVal = false;

		// If the source is null, then it is a local file. Otherwise check it
		// explicitly.
		if (source.getHost() != null) {
			retVal = !"localhost".equals(source.getHost());
		}

		return retVal;
	}
	
	/**
	 * This method is the same as calling 
	 * {@code draw(category, plotType, parent, false)}
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#draw(java.lang.String,
	 * java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void draw(String category, String plotType, Composite parent)
			throws Exception {
		draw(category, plotType, parent, false);
	}
	
	/**
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#draw(java.lang.String,
	 * java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	public void draw(String category, String plotType, Composite parent, 
			boolean addCloseButton) throws Exception {

		// Make sure the plot type is valid
		if (provider != null && category != null 
				&& types.keySet().contains(category)
				&& plotType != null && plotType.contains(" vs. ")) {
			// Get the axes to plot
			String[] axes = plotType.split(" ");
			String axis1 = axes[0];
			String axis2 = axes[2];
			// Create the plot provider
			PlotProvider plotProvider = new PlotProvider();
			// The new plot's title (the filename)
			int lastSeparator = provider.getSourceInfo().lastIndexOf("/");
			String newPlotTitle = (lastSeparator > -1 ? 
					provider.getSourceInfo().substring(lastSeparator+1) : 
						provider.getSourceInfo());
			// Set the title for the new plot provider
			plotProvider.setPlotTitle(newPlotTitle);
			// The plot's set time
			Double plotTime = provider.getTimes().get(0);
			// Set the time - not entirely why they get it and set it here.
			// Whatevs.
			provider.setTime(plotTime);
			// Create a new series title for the new series
			String seriesTitle = axis1 + " vs. " + axis2 + " at " + plotTime;
			// Create a new series provider
			SeriesProvider seriesProvider = new SeriesProvider();
			seriesProvider.setDataProvider(provider);
			seriesProvider.setTimeForDataProvider(plotTime);
			seriesProvider.setSeriesTitle(seriesTitle);
			seriesProvider.setXDataFeature(axis1);
			seriesProvider.setYDataFeature(axis2);
			seriesProvider.setSeriesType(category);
			// Add this new series to the plot provider
			plotProvider.addSeries(plotTime, seriesProvider);
			// Create the plot editor
			CSVPlotEditor plotEditor = new CSVPlotEditor();
			// Create the the plotting canvas (with "close" button enabled)
			plotEditor.createPartControl(parent, true);
			// Add the new plot to the editor (with time slider disabled)
			plotEditor.showPlotProvider(plotProvider, false);
		} else {
			// Complain that the plot is invalid
			throw new Exception("Invalid plot: category = " + category
					+ ", type = " + plotType + ", provider = " + provider.toString());
		}
		
		return;
	}
}
