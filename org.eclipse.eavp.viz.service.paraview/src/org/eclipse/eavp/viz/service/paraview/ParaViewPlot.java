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
package org.eclipse.eavp.viz.service.paraview;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.eavp.viz.service.AbstractSeries;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.connections.ConnectionPlot;
import org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite;
import org.eclipse.eavp.viz.service.connections.ConnectionState;
import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * This class is responsible for embedding ParaView-supported graphics inside
 * client {@link Composite}s.
 * <p>
 * Instances of this class should not be created manually. Instead, a plot
 * should be created via {@link ParaViewVizService#createPlot(URI)}.
 * </p>
 *
 * @see ParaViewVizService
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewPlot extends ConnectionPlot<IParaViewWebClient> {

	/**
	 * Whether or not the data has been loaded.
	 */
	private boolean loaded = false;
	/**
	 * Whether or not the data is currently being loaded.
	 */
	private boolean loading = false;
	/**
	 * A lock used to synchronize load requests, as the data should only be
	 * reloaded one at a time, and data should not be reloaded simultaneously.
	 */
	private final Lock loadLock = new ReentrantLock();

	/**
	 * A map containing all categories and types (dependent series), keyed on
	 * the categories. This is populated based on the {@link #proxy} at load
	 * time.
	 */
	private final Map<String, List<ISeries>> plotTypes = new HashMap<String, List<ISeries>>();

	/**
	 * The proxy associated with the current URI. It handles messages concerning
	 * the file going to and from the remote ParaView server.
	 * <p>
	 * This should be re-created whenever the data source URI is set. If it
	 * cannot be created, then the URI cannot be rendered via ParaView.
	 * </p>
	 */
	private IParaViewProxy proxy;

	/**
	 * A handle to the viz service that created this plot.
	 */
	private final ParaViewVizService vizService;

	/**
	 * The default constructor.
	 * 
	 * @param vizService
	 *            The service used to create this plot.
	 */
	public ParaViewPlot(ParaViewVizService vizService) {
		this.vizService = vizService;
	}

	/**
	 * Adds an independent series based on the provided metadata in the proxy.
	 * This attempts to add a series based on the proxy's timesteps or on the
	 * point 0.0 if no timesteps are available.
	 */
	private void addIndependentSeries() {

		// Get the timesteps from the proxy. If there are no times, simply add a
		// single point 0.0.
		final List<Double> times = proxy.getTimesteps();
		if (times.isEmpty()) {
			times.add(0.0);
		}

		// Create a new series wrapping the time data.
		ISeries independentSeries = new AbstractSeries() {
			@Override
			public double[] getBounds() {
				double min = times.get(0);
				double max = times.get(times.size() - 1);
				// This is what the docs specify...
				return new double[] { min, max - min };
			}

			@Override
			public Object[] getDataPoints() {
				Double[] timeArray = new Double[times.size()];
				return times.toArray(timeArray);
			}

			@Override
			public String getLabel() {
				return "Time";
			}
		};

		// Set the independent series.
		super.setIndependentSeries(independentSeries);

		return;
	}

	/**
	 * Adds a new category of series to the {@link #plotTypes} map based on the
	 * specified category name and list of plot types.
	 * 
	 * @param category
	 * @param features
	 */
	private void addPlotCategory(String category, Set<String> features) {
		if (!features.isEmpty()) {
			// Add the category to the map.
			List<ISeries> seriesList = new ArrayList<ISeries>(features.size());
			plotTypes.put(category, seriesList);

			// Create a new series for each type.
			final String categoryRef = category;
			for (String type : features) {
				final String typeRef = type;

				// Add a new series with the category and label set.
				seriesList.add(new AbstractSeries() {
					@Override
					public String getCategory() {
						return categoryRef;
					}

					@Override
					public String getLabel() {
						return typeRef;
					}
				});
			}
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlot#connectionStateChanged(org.eclipse.eavp.viz.service.connections.IVizConnection, org.eclipse.eavp.viz.service.connections.ConnectionState, java.lang.String)
	 */
	@Override
	public void connectionStateChanged(
			IVizConnection<IParaViewWebClient> connection,
			ConnectionState state, String message) {
		if (state == ConnectionState.Connected) {
			load();
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlot#createPlotComposite(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected ConnectionPlotComposite<IParaViewWebClient> createPlotComposite(
			Composite parent) {
		return new ParaViewPlotComposite(parent, SWT.NONE);
	}

	/**
	 * Uses the viz service's proxy factory to create and open a proxy from the
	 * current data source URI.
	 * 
	 * @return A new proxy.
	 * @throws Exception
	 *             If there was an error either opening the proxy over the
	 *             curret connection or if the file simply could not be opened.
	 */
	private IParaViewProxy createProxy() throws Exception {
		IParaViewProxy newProxy = null;

		// Attempt to create the IParaViewProxy. This will throw an exception if
		// the URI is null or its extension is invalid.
		URI uri = getDataSource();
		IParaViewProxy proxy = vizService.getProxyFactory().createProxy(uri);

		// Attempt to open the file. Wait until the process completes.
		if (proxy.open(getConnection()).get()) {
			newProxy = proxy;
		} else {
			throw new IllegalArgumentException("ParaViewPlot error: "
					+ "Cannot open the file \"" + uri.getPath()
					+ "\" using the existing connection.");
		}

		return newProxy;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#getCategories()
	 */
	@Override
	public List<String> getCategories() {
		return new ArrayList<String>(plotTypes.keySet());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#getDependentSeries(java.lang.String)
	 */
	@Override
	public List<ISeries> getDependentSeries(String category) {
		// Return a copy of the plot type series if the category is valid.
		List<ISeries> series = plotTypes.get(category);
		if (series != null) {
			series = new ArrayList<ISeries>(series);
		}
		return series;
	}

	/**
	 * Gets the proxy associated with the current URI. It handles messages
	 * concerning the file going to and from the remote ParaView server.
	 * <p>
	 * This is re-created whenever the data source URI is changed. If it cannot
	 * be created, then the URI cannot be rendered via ParaView.
	 * </p>
	 * 
	 * @return The current proxy, or {@code null} if the current URI/connection
	 *         cannot be used to create a proxy.
	 */
	protected IParaViewProxy getParaViewProxy() {
		return proxy;
	}

	/**
	 * Gets whether or not data has been loaded from the data source.
	 * 
	 * @return True if data has been loaded, false otherwise.
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Loads the plot categories and types (dependent series) as well as the
	 * time/cycle data (independent series) from the data source using the viz
	 * connection's widget. Work is performed on a separate thread.
	 */
	private void load() {

		// Setting loaded to false effectively invalidates the plot (meaning the
		// data is stale).
		loaded = false;

		loadLock.lock();
		try {

			final URI uri = getDataSource();
			final IVizConnection<IParaViewWebClient> connection = getConnection();

			// If possible, try loading the data from the file.
			if (!loading && uri != null && connection != null
					&& connection.getState() == ConnectionState.Connected) {
				loading = true;

				// Remove all data.
				plotTypes.clear();
				proxy = null;

				Job loadJob = new Job("Loading ParaView Plot") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {

						// Set the initial task name.
						monitor.beginTask("Loading ParaView Plot", 100);

						// Try to create the proxy.
						try {
							proxy = createProxy();
						} catch (Exception e) {
							// The proxy could not be opened.
							return new Status(Status.ERROR,
									"org.eclipse.eavp.viz.service", 1,
									"ParaViewPlot could not load the file.", e);
						}
						monitor.worked(30);

						// Set the default independent series.
						addIndependentSeries();
						monitor.worked(10);

						// Load all categories and features into series.
						Set<String> categories = proxy.getFeatureCategories();
						monitor.worked(10);
						if (!categories.isEmpty()) {
							int increment = 40 / categories.size();
							// Add all categories and features to the map.
							for (String category : proxy
									.getFeatureCategories()) {
								addPlotCategory(category,
										proxy.getFeatures(category));
								monitor.worked(increment);
							}
						} else {
							monitor.worked(40);
						}

						// Set the default feature.
						setInitialFeature();
						monitor.worked(10);

						// It is loaded, although it may not have any data.
						loadLock.lock();
						try {
							loading = false;
							loaded = true;
						} finally {
							loadLock.unlock();
						}

						// Notify the listeners that loading has completed.
						notifyPlotListeners("loaded", "true");
						monitor.worked(10);

						// Return a status.
						return new Status(Status.OK,
								"org.eclipse.eavp.viz.service", 1,
								"ParaView Plot Loaded", null);
					}
				};
				loadJob.schedule();
			}
		} finally {
			loadLock.unlock();
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlot#setConnection(org.eclipse.eavp.viz.service.connections.IVizConnection)
	 */
	@Override
	public boolean setConnection(IVizConnection<IParaViewWebClient> connection)
			throws Exception {
		boolean changed = super.setConnection(connection);
		// If the connection has changed, we must re-load the data.
		if (changed) {
			load();
		}
		return changed;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlot#setDataSource(java.net.URI)
	 */
	@Override
	public boolean setDataSource(URI uri) throws Exception {
		boolean changed = super.setDataSource(uri);
		// If the data source has changed, we must re-load the data.
		if (changed) {
			load();
		}
		return changed;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.AbstractPlot#setIndependentSeries(org.eclipse.eavp.viz.service.ISeries)
	 */
	@Override
	public void setIndependentSeries(ISeries series) {
		// We do not allow the client code to set the independent series.
	}

	/**
	 * Finds the first dependent series, marking it as enabled. The priority is
	 * placed on the mesh, but if no mesh is available, other categories will be
	 * searched.
	 */
	private void setInitialFeature() {
		// Find the first available series and enable it.
		for (List<ISeries> seriesList : plotTypes.values()) {
			if (!seriesList.isEmpty()) {
				seriesList.get(0).setEnabled(true);
				break;
			}
		}
		return;
	}
}
