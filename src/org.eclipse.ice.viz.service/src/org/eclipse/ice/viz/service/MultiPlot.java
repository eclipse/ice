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
 * specified {@code Composite} based on the specified plot category and type.</li>
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
	private URI source;

	/**
	 * The map of current {@link PlotRender}s, keyed on their parent
	 * {@code Composite}s.
	 */
	private final Map<Composite, PlotRender> plotRenders;

	/**
	 * A map of the available plot types.
	 */
	private Map<String, String[]> plotTypes;

	/**
	 * The default constructor.
	 * 
	 * @param vizService
	 *            The visualization service responsible for this plot.
	 */
	public MultiPlot(IVizService vizService) {
		// Check the parameters.
		if (vizService == null) {
			throw new NullPointerException("IPlot error: "
					+ "Null viz service not allowed.");
		}

		this.vizService = vizService;

		// Initialize any final collections.
		plotRenders = new HashMap<Composite, PlotRender>();

		return;
	}

	// ---- Implements IPlot ---- //
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#draw(java.lang.String,
	 * java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite draw(String category, String plotType, Composite parent)
			throws Exception {

		Composite child = null;

		// Check the parameters.
		if (category == null || plotType == null || parent == null) {
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

		// Send the new plot category and type to the PlotRender.
		plotRender.setPlotCategory(category);
		plotRender.setPlotType(plotType);

		// Trigger the appropriate update to the PlotRender's content.
		updatePlotRender(plotRender);

		// FIXME The child needs to be set to something, or we need to come up
		// with an alternate strategy.

		return child;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getPlotTypes()
	 */
	@Override
	public Map<String, String[]> getPlotTypes() throws Exception {
		// If necessary, re-build the cache of plot types.
		if (plotTypes == null) {
			plotTypes = new HashMap<String, String[]>();

			Map<String, String[]> newPlotTypes = findPlotTypes(source);
			plotTypes.putAll(newPlotTypes);
		}
		return plotTypes;
	}

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
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return new HashMap<String, String>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#setProperties(java.util
	 * .Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		return source.getHost();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return !"localhost".equals(getSourceHost());
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
			throw new NullPointerException("IPlot error: "
					+ "The file is null.");
		}
		// This handles the unusual (but perhaps entirely possible) situation
		// where the URI is opaque, e.g., "mailto:user@site.com".
		else if (file.getPath() == null) {
			throw new IllegalArgumentException("IPlot error: "
					+ "The file is not a valid URI.");
		}

		// Get the list of new plot types from the sub-class implementation.
		Map<String, String[]> newPlotTypes = findPlotTypes(file);

		// If empty, throw an IllegalArgumentException.
		if (newPlotTypes.isEmpty()) {
			throw new IllegalArgumentException("IPlot error: "
					+ "No plots available in file.");
		}

		// Clear any cached meta data and rebuild the cache of plot types.
		clearCache();
		plotTypes = new HashMap<String, String[]>();
		plotTypes.putAll(newPlotTypes);

		// Update the reference to the data source.
		source = file;

		return;
	}

	/**
	 * Clears any cached meta data for the plot.
	 */
	protected void clearCache() {
		// Clear the cache of known plot types.
		if (plotTypes != null) {
			plotTypes.clear();
			plotTypes = null;
		}

		return;
	}

	/**
	 * <b>Note:</b> This method is called automatically in
	 * {@link #setDataSource(URI)}. Implementations should always query the file
	 * and should <i>not</i> cache the data.
	 * <p>
	 * This operation returns a simple map of plot types that can be created by
	 * the IPlot using its data source. The map is meant to have a structure
	 * where each individual key is a type of plot - mesh, scalar, line, etc. -
	 * with a list of values of all of the plots it can create of that given
	 * type from the data source. For example, for a CSV file with three columns
	 * x, y1, y2, y3, the map might be:
	 * </p>
	 * <p>
	 * key | value<br>
	 * line | "x vs y1", "x vs y2", "x vs y3"<br>
	 * scatter | "x vs y1", "x vs y2", "x vs y3"<br>
	 * contour | "x vs y1", "x vs y2", "x vs y3"
	 * </p>
	 * 
	 * @param file
	 *            The data source for the file.
	 * @return The map of valid plot types this plot can be.
	 * @throws IOException
	 *             if there was an error while reading the file's contents
	 * @throws Exception
	 *             if there is some other unspecified problem with the file
	 */
	protected abstract Map<String, String[]> findPlotTypes(URI file)
			throws IOException, Exception;

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
