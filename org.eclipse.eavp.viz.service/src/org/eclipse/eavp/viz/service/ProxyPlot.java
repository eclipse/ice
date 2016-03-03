/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.eavp.viz.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class provides a proxy plot that points to a source {@link IPlot}
 * implementation. This is used to share plot data between multiple drawings of
 * a source plot.
 * <p>
 * This proxy plot will create its own copies of the dependent series in the
 * source plot. These copies will point to the data in the source plot's series,
 * while each copy will be customizable. This applies to the methods below:
 * </p>
 * <ul>
 * <li>{@link #getCategories()} - uses a map loaded from the source IPlot</li>
 * <li>{@link #getDependentSeries(String)} - uses {@link ProxySeries} that point
 * at the source plot's {@link ISeries}</li>
 * </ul>
 * The following methods are re-directed to the source plot.
 * <ul>
 * <li>{@link #getDataSource()}</li>
 * <li>{@link #getNumberOfAxes()}</li>
 * </ul>
 * On the other hand, all remaining methods will use data stored in this proxy
 * as they rely in the {@link AbstractPlot} implementation.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class ProxyPlot extends AbstractPlot {

	/**
	 * The source plot for the majority of the plot's information.
	 */
	protected IPlot source;

	/**
	 * The map of series, keyed on category. Only proxies pointing to the actual
	 * series with the original data should be stored in this map.
	 */
	private final Map<String, List<ISeries>> proxySeries;

	/**
	 * The default constructor.
	 */
	public ProxyPlot() {
		proxySeries = new HashMap<String, List<ISeries>>();
	}

	protected Map<String, List<ISeries>> createProxySeries(IPlot source) {
		Map<String, List<ISeries>> proxySeries = new HashMap<String, List<ISeries>>();
		if (source != null) {
			// Copy all dependent series using ProxySeries instances. This
			// shares the data but allows each series to be customizable.
			for (String category : source.getCategories()) {
				// Copy the series for this category.
				List<ISeries> sourceSeriesList = source
						.getDependentSeries(category);
				if (sourceSeriesList != null) {
					List<ISeries> proxySeriesList = new ArrayList<ISeries>(
							sourceSeriesList.size());
					// For each ISeries in the source, create a new
					// ProxySeries to point it to the source ISeries.
					for (ISeries series : sourceSeriesList) {
						proxySeriesList.add(createProxySeries(series));
					}
					// Put the category and proxy series into the map.
					proxySeries.put(category, proxySeriesList);
				}
			}
		}
		return proxySeries;
	}

	/**
	 * This method creates a proxy for an {@link ISeries} instance.
	 * 
	 * @return A new proxy for a series. Must not be {@code null}.
	 */
	protected ProxySeries createProxySeries(ISeries source) {
		ProxySeries series = new ProxySeries();
		series.setSource(source);
		return series;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#getCategories()
	 */
	@Override
	public List<String> getCategories() {
		// Use the map of ProxySeries, which has the categories.
		return new ArrayList<String>(proxySeries.keySet());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		// Use the source's data source if possible.
		return source != null ? source.getDataSource() : super.getDataSource();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#getDependentSeries(java.lang.String)
	 */
	@Override
	public List<ISeries> getDependentSeries(String category) {
		// Use the map of ProxySeries to get a new list of ISeries associated
		// with the category.
		List<ISeries> series = proxySeries.get(category);
		if (series != null) {
			series = new ArrayList<ISeries>(series);
		}
		return series;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// Use the source's axes if possible.
		return source != null ? source.getNumberOfAxes()
				: super.getNumberOfAxes();
	}

	/**
	 * Gets the source IPlot for which this plot serves as a proxy.
	 * 
	 * @return The source IPlot, or {@code null} if one is not set.
	 */
	protected IPlot getSource() {
		return source;
	}

	/**
	 * This method is called to rebuild the dependent series based on the
	 * current set of series available from the source plot.
	 */
	protected void reloadSeries() {
		// Only reload if not null.
		if (source != null) {
			// Get the new series from the source
			Map<String, List<ISeries>> newSeries = createProxySeries(source);
			// Iterate over the categories and the series lists to enable all of
			// the previously enabled series, this allows for easier model
			// fitting
			for (String category : newSeries.keySet()) {
				List<ISeries> listSeries = newSeries.get(category);
				List<ISeries> oldSeries = proxySeries.get(category);
				if (oldSeries != null) {
					for (int i = 0; i < listSeries.size()
							&& i < oldSeries.size(); i++) {
						listSeries.get(i)
								.setEnabled(oldSeries.get(i).isEnabled());
					}
				}
			}
			proxySeries.clear();
			proxySeries.putAll(newSeries);
		} else {
			// If null, clear the series so that nothing appears on the graph
			proxySeries.clear();
		}
		return;
	}

	/**
	 * Sets the source {@link IPlot} on which this plot is based. This method
	 * synchronizes this plot's dependent series with those of the original plot
	 * by creating a {@link ProxySeries} to those of the source plot.
	 * 
	 * @param source
	 *            The source plot. If {@code null}, the source will be unset and
	 *            all dependent series will be cleared.
	 */
	public void setSource(IPlot source) {
		if (source != this.source) {
			// Unregister from the old plot source.
			// Nothing to do.

			// Register with the new plot source.
			this.source = source;
			if (source != null) {
				// If the title is unset, get the title from the source plot.
				if (getPlotTitle() == null) {
					setPlotTitle(source.getPlotTitle());
				}
			}
			reloadSeries();
		}
		return;
	}
}
