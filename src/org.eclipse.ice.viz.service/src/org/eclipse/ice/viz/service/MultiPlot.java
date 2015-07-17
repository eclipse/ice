/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or initial documentation
 *    Jay Jay Billings (UT-Battelle, LLC.) - moved IPlot
 *******************************************************************************/
package org.eclipse.ice.viz.service;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provides a basic plot capable of drawing in multiple parent
 * {@code Composite}s simply via the methods provided by the {@link IPlot}
 * interface.
 * <p>
 * For client code that will be drawing these plots, do the following:
 * </p>
 * <ol>
 * <li>Call {@link #draw(String, String, Composite)} with a {@code Composite}
 * and any category and type. This renders (if possible) a plot inside the
 * specified {@code Composite} based on the specified plot category and type.
 * </li>
 * <li>Call {@link #draw(String, String, Composite)} with the same
 * {@code Composite} but different category and type. <i>The plot rendered by
 * the previous call will have its plot category and type changed.</i></li>
 * <li>Call {@link #draw(String, String, Composite)} with a {@code Composite}
 * and any category and type. This renders (if possible) a plot inside the
 * {@code Composite} based on the specified plot category and type. <i>You now
 * have two separate renderings based on the same {@code IPlot}.</i></li>
 * </ol>
 * <p>
 * Sub-classes should override the following methods so that the correct
 * {@link PlotRender} is created and updated properly:
 * </p>
 * <ol>
 * <li>{@link #createPlotRender(Composite)}</li>
 * <li>{@link #updatePlotRender(PlotRender)}</li>
 * </ol>
 * 
 * @author Jordan
 *
 */
public abstract class MultiPlot implements IPlot {

	/**
	 * The visualization service responsible for this plot.
	 */
	private final IVizService vizService;

	/**
	 * The data source, either a local or remote file.
	 */
	protected URI source;

	/**
	 * The list of ISeries series for this plot to render
	 */
	private List<ISeries> series;

	/**
	 * The title of the plot
	 */
	private String plotTitle;

	/**
	 * The style that provides the basic stylistic information for this plot
	 */
	protected ISeriesStyle plotStyle;

	/**
	 * The plot properties
	 */
	private final Map<String, String> properties;

	/**
	 * The independent series, for plotting against the other series.
	 */
	private ISeries independentSeries;

	/**
	 * The map of current {@link PlotRender}s, keyed on their parent
	 * {@code Composite}s.
	 */
	protected final Map<Composite, PlotRender> plotRenders;

	/**
	 * This should only be called by a subclass that has specific behavior but
	 * still uses the super class variables.
	 * 
	 * @param source
	 *            The source for this plot
	 */
	protected MultiPlot(URI source) {
		// Instantiate variables
		this.source = source;
		this.vizService = null;
		this.plotRenders = new HashMap<Composite, PlotRender>();
		properties = new HashMap<String, String>();
		this.series = new ArrayList<ISeries>();
		return;
	}

	/**
	 * The default constructor.
	 * 
	 * @param vizService
	 *            The visualization service responsible for this plot.
	 */
	public MultiPlot(IVizService vizService) {
		// Check the parameters.
		if (vizService == null) {
			throw new NullPointerException(
					"IPlot error: " + "Null viz service not allowed.");
		}

		this.vizService = vizService;

		// Initialize any final collections.
		plotRenders = new HashMap<Composite, PlotRender>();
		properties = new HashMap<String, String>();
		this.series = new ArrayList<ISeries>();
		return;
	}

	/**
	 * Gets the plot style for this plot
	 * 
	 * @return The plot style
	 */
	public ISeriesStyle getPlotStyle() {
		return plotStyle;
	}

	/**
	 * Sets the plot style for this plot
	 * 
	 * @param style
	 *            The plot style
	 */
	public void setPlotStyle(ISeriesStyle style) {
		plotStyle = style;
	}

	// ---- Implements IPlot ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#draw(org.eclipse.swt.
	 * widgets.Composite)
	 */
	@Override
	public Composite draw(Composite parent) throws Exception {

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

		// Trigger the appropriate update to the PlotRender's content.
		updatePlotRender(plotRender);

		// Try to return the plot composite used to draw this plot
		child = plotRender.plotComposite;

		return child;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#setPlotTitle(java.lang.String)
	 */
	@Override
	public void setPlotTitle(String title) {
		plotTitle = title;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#getPlotTitle()
	 */
	@Override
	public String getPlotTitle() {
		return plotTitle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.IPlot#setIndependentSeries(org.eclipse.ice.
	 * viz.service.ISeries)
	 */
	@Override
	public void setIndependentSeries(ISeries series) {
		if (series != null) {
			independentSeries = series;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#getIndependentSeries()
	 */
	@Override
	public ISeries getIndependentSeries() {
		return independentSeries;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.IPlot#addDependentSeries(org.eclipse.ice.viz.
	 * service.ISeries)
	 */
	@Override
	public void addDependentSeries(ISeries series) {
		this.series.add(series);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.IPlot#removeDependantSeries(org.eclipse.ice.
	 * viz.service.ISeries)
	 */
	@Override
	public void removeDependantSeries(ISeries series) {
		this.series.remove(series);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#getAllDependentSeries()
	 */
	@Override
	public List<ISeries> getAllDependentSeries() {
		return series;
	}

	// --------------------------- //

	/**
	 * Adds a dependent series to the plot. This should be plotted against the
	 * independent series specified.
	 * 
	 * @param series
	 *            The ISeries to be plotted.
	 */
	public void addDependantSeries(ISeries series) {
		if (!this.series.contains(series)) {
			this.series.add(series);
		}
	}

	// -- Implements IVizCanvas -- //

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizCanvas#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizCanvas#setProperties(java.
	 * util .Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizCanvas#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizCanvas#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		return source.getHost();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IVizCanvas#isSourceRemote()
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

	// -------------------------- //

	/**
	 * Sets the data source (which is currently rendered if the plot is drawn).
	 * If the data source is valid and new, then the plot will be updated
	 * accordingly.
	 * 
	 * @param file
	 *            The new data source URI.
	 * @throws NullPointerException
	 *             if the specified file is null
	 * @throws IOException
	 *             if there was an error while reading the file's contents
	 * @throws IllegalArgumentException
	 *             if there are no plots available
	 * @throws Exception
	 *             if there is some other unspecified problem with the file
	 */
	public void setDataSource(URI file) throws NullPointerException,
			IOException, IllegalArgumentException, Exception {

		// Throw an error if the file is null.
		if (file == null) {
			throw new NullPointerException(
					"IPlot error: " + "The file is null.");
		}
		// This handles the unusual (but perhaps entirely possible) situation
		// where the URI is opaque, e.g., "mailto:user@site.com".
		else if (file.getPath() == null) {
			throw new IllegalArgumentException(
					"IPlot error: " + "The file is not a valid URI.");
		}

		// Get the list of new plot types from the sub-class implementation.
		List<ISeries> newSeries = getSeries(file);

		// If empty, throw an IllegalArgumentException.
		if (newSeries.isEmpty()) {
			throw new IllegalArgumentException(
					"IPlot error: " + "No plots available in file.");
		}

		// Clear any cached meta data and rebuild the cache of plot types.
		clearCache();
		series.addAll(newSeries);

		// Update the reference to the data source.
		source = file;

		return;
	}

	/**
	 * Gets any associated series from the specified file given. It is assumed
	 * that the file has already been checked for legitimacy.
	 * 
	 * @param file
	 *            The file to retrieve the list of series from.
	 */
	protected List<ISeries> getSeries(URI file) {
		// It will be up to the specific kind of plot to know and extract the
		// type of information it needs from he file
		return new ArrayList<ISeries>();
	}

	/**
	 * Clears any cached meta data for the plot.
	 */
	protected void clearCache() {
		// Clear the cache of series
		series.clear();
		return;
	}

	/**
	 * Gets the visualization service responsible for this plot.
	 * 
	 * @return The visualization service responsible for this plot.
	 */
	public IVizService getVizService() {
		return vizService;
	}

	// ---- UI Widgets ---- //
	/**
	 * Creates a {@link PlotRender} inside the specified parent
	 * {@code Composite}. The {@code PlotRender}'s content should not be created
	 * yet.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that will contain the new
	 *            {@code PlotRender}.
	 * @return The new {@code PlotRender}.
	 */
	protected abstract PlotRender createPlotRender(Composite parent);

	/**
	 * Updates the specified {@link PlotRender}. The default behavior of this
	 * method is to call {@link PlotRender#refresh()}.
	 * 
	 * @param plotRender
	 *            The {@code PlotRender} to update.
	 */
	protected void updatePlotRender(PlotRender plotRender) {
		plotRender.refresh();
	}

	/**
	 * Gets a list of all current rendered plots.
	 * 
	 * @return A list containing each current {@link PlotRender} in this
	 *         {@code MultiPlot}.
	 */
	protected List<PlotRender> getPlotRenders() {
		return new ArrayList<PlotRender>(plotRenders.values());
	}
	// -------------------- //

}
