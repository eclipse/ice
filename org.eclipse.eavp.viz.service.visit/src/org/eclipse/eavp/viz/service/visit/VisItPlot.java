/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Jordan Deyton
 *   Jordan Deyton - viz series refactor
 *******************************************************************************/
package org.eclipse.eavp.viz.service.visit;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import gov.lbnl.visit.swt.VisItSwtConnection;
import visit.java.client.FileInfo;
import visit.java.client.ViewerMethods;

/**
 * This class provides the VisIt implementation for an IPlot.
 * 
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItPlot extends ConnectionPlot<VisItSwtConnection> {

	// TODO We should manage the window IDs here.

	/**
	 * A map of allowed representations, keyed on the category. Instead of
	 * passing the category and type to the VisIt widget, a representation and
	 * type are passed. The first value in each list is considered the default.
	 */
	private final Map<String, List<String>> representations;

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
	 * the categories.
	 */
	private final Map<String, List<ISeries>> plotTypes = new HashMap<String, List<ISeries>>();

	/**
	 * The default constructor.
	 */
	public VisItPlot() {

		// Create the map of VisIt plot representations. The keys are the
		// categories exposed by VisItPlot in findPlotTypes(...). The first
		// value in each list is considered the default.
		representations = new HashMap<String, List<String>>();
		List<String> types;
		// The category "Materials" has two types: Boundary and FilledBoundary.
		types = new ArrayList<String>(2);
		types.add("Boundary");
		types.add("FilledBoundary");
		representations.put("Materials", types);
		// "Meshes" has one type: Mesh.
		types = new ArrayList<String>(1);
		types.add("Mesh");
		representations.put("Meshes", types);
		// "Scalars" has three types: Pseudocolor, Contour, Volume.
		types = new ArrayList<String>(3);
		types.add("Pseudocolor");
		types.add("Contour");
		types.add("Volume");
		representations.put("Scalars", types);
		// "Vectors" has one type: Vector.
		types = new ArrayList<String>(1);
		types.add("Vector");
		representations.put("Vectors", types);

		return;
	}

	/**
	 * Adds an independent series based on the provided metadata. This attempts
	 * to add a series based on the following depending on data availability:
	 * <ol>
	 * <li>time - floats provided by the metadata</li>
	 * <li>cycles - integers provided by the metadata</li>
	 * <li>time - a single point 0.0 in time (stored as a float)</li>
	 * </ol>
	 * 
	 * @param info
	 *            The source metadata.
	 */
	private void addIndependentSeries(FileInfo info) {

		// Add the time or cycles as the independent series.
		List<Float> times = info.getTimes();
		List<Integer> cycles = info.getCycles();
		final ISeries independentSeries;

		// If possible, create a series based on the time data.
		if (times != null && !times.isEmpty()) {

			final Float[] timeArray = new Float[times.size()];
			times.toArray(timeArray);

			independentSeries = new AbstractSeries() {
				@Override
				public double[] getBounds() {
					float min = timeArray[0];
					float max = timeArray[timeArray.length - 1];
					// This is what the docs specify...
					return new double[] { min, max - min };
				}

				@Override
				public Object[] getDataPoints() {
					return Arrays.copyOf(timeArray, timeArray.length);
				}

				@Override
				public String getLabel() {
					return "Time";
				}
			};
		}
		// If no time data is available, create a series based on the cycles.
		else if (cycles != null && !cycles.isEmpty()) {

			final Integer[] cycleArray = new Integer[cycles.size()];
			cycles.toArray(cycleArray);

			independentSeries = new AbstractSeries() {
				@Override
				public double[] getBounds() {
					int min = cycleArray[0];
					int max = cycleArray[cycleArray.length - 1];
					// This is what the docs specify...
					return new double[] { min, max - min };
				}

				@Override
				public Object[] getDataPoints() {
					return Arrays.copyOf(cycleArray, cycleArray.length);
				}

				@Override
				public String getLabel() {
					return "Cycles";
				}
			};
		}
		// If neither the cycles nor the time data are available, create a
		// series with a single time point of 0.0.
		else {
			independentSeries = new AbstractSeries() {
				@Override
				public double[] getBounds() {
					return new double[] { 0.0, 0.0 };
				}

				@Override
				public Object[] getDataPoints() {
					return new Float[] { 0f };
				}

				@Override
				public String getLabel() {
					return "Time";
				}
			};
		}

		// Set the independent series.
		super.setIndependentSeries(independentSeries);

		return;
	}

	/**
	 * Adds a new category of series to the {@link #plotTypes} map based on the
	 * specified category name and list of plot types.
	 * 
	 * @param category
	 *            The category name.
	 * @param types
	 *            The list of plot types.
	 */
	private void addPlotCategory(String category, List<String> types) {
		if (!types.isEmpty()) {
			// Add the category to the map.
			List<ISeries> seriesList = new ArrayList<ISeries>(types.size());
			plotTypes.put(category, seriesList);

			// Create a new series for each type.
			final String categoryRef = category;
			for (String type : types) {
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
			IVizConnection<VisItSwtConnection> connection,
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
	protected ConnectionPlotComposite<VisItSwtConnection> createPlotComposite(
			Composite parent) {
		return new VisItPlotComposite(parent, SWT.NONE);
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
	 * Gets the list of allowed plot representations for a category. This is
	 * required for a VisIt canvas and should be passed to it along with the
	 * plot type.
	 *
	 * @param category
	 *            An {@code IPlot} category.
	 * @return A list of allowed representations for the specified category. The
	 *         first value can be considered the default. If the category is
	 *         invalid, an empty list is returned.
	 */
	protected List<String> getRepresentations(String category) {
		// Always return a copy so the list of representations for a category is
		// not (un)intentionally modified.
		List<String> types = representations.get(category);
		return (types != null ? new ArrayList<String>(types)
				: new ArrayList<String>());
	}

	/**
	 * Gets the path to the specified file.
	 * 
	 * @param source
	 *            The source URI.
	 * @return A VisIt-friendly file path.
	 */
	protected String getSourcePath(URI source) {
		String path = null;
		if (source != null) {
			// On Windows, the File class inserts standard forward slashes as
			// separators. VisIt, on the other hand, requires the native
			// separator. If the URI uses the standard separator on Windows,
			// update the source path to use the native Windows separator.
			path = source.getPath();

			// If the host is local and a Windows-based machine, we need to
			// update the path to use Windows separators for VisIt.
			String host = source.getHost();
			// TODO VisIt should just be able to handle a raw URI... The code
			// below can't handle remote Windows machines.
			if ((host == null || "localhost".equals(host)) && System
					.getProperty("os.name").toLowerCase().contains("windows")) {
				if (path.startsWith("/")) {
					path = path.substring(1);
					path = path.replace("/",
							System.getProperty("file.separator"));
				}
			}
		}
		return path;
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
			final IVizConnection<VisItSwtConnection> connection = getConnection();

			// If possible, try loading the data from the file.
			if (!loading && uri != null && connection != null
					&& connection.getState() == ConnectionState.Connected) {
				loading = true;

				// Remove all data.
				plotTypes.clear();
				super.setIndependentSeries(null);

				Job loadJob = new Job("Loading VisIt Plot") {
					@Override
					protected IStatus run(IProgressMonitor monitor) {

						// Set the initial task name.
						monitor.beginTask("Loading VisIt Plot", 100);

						// Determine the source path string. Unfortunately, we
						// can't just give the URI directly to the VisIt client
						// API.
						String sourcePath = getSourcePath(uri);

						// Determine the VisIt FileInfo for the data source.
						ViewerMethods methods = connection.getWidget()
								.getViewerMethods();
						methods.openDatabase(sourcePath);
						monitor.worked(20);
						FileInfo info = methods.getDatabaseInfo();
						monitor.worked(20);

						// Add the independent series from the meta data.
						// This uses either the time, the cycles, or a "time"
						// series with the single time 0.0.
						addIndependentSeries(info);
						monitor.worked(5);

						// Get all of the plot types and plots in the file.
						addPlotCategory("Meshes", info.getMeshes());
						monitor.worked(10);
						addPlotCategory("Materials", info.getMaterials());
						monitor.worked(10);
						addPlotCategory("Scalars", info.getScalars());
						monitor.worked(10);
						addPlotCategory("Vectors", info.getVectors());
						monitor.worked(10);

						// Enabled the mesh or the first available plot type.
						setInitialPlotType();
						monitor.worked(5);

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
								"VisIt Plot Loaded", null);
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
	public boolean setConnection(IVizConnection<VisItSwtConnection> connection)
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
	private void setInitialPlotType() {
		// If possible, enable the first mesh.
		List<ISeries> meshes = plotTypes.get("Meshes");
		if (!meshes.isEmpty()) {
			meshes.get(0).setEnabled(true);
		}
		// Otherwise, enable the first dependent series that can be found.
		else {
			for (List<ISeries> seriesList : plotTypes.values()) {
				if (!seriesList.isEmpty()) {
					seriesList.get(0).setEnabled(true);
					break;
				}
			}
		}
		return;
	}
}
