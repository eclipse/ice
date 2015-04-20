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
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.viz.plotviewer.CSVDataLoader;
import org.eclipse.ice.viz.plotviewer.CSVDataProvider;
import org.eclipse.ice.viz.plotviewer.CSVPlotEditor;
import org.eclipse.ice.viz.plotviewer.PlotProvider;
import org.eclipse.ice.viz.plotviewer.SeriesProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

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
	private final Map<String, String> properties;

	/**
	 * The types that this plot can assume
	 */
	private final Map<String, String[]> types;

	/**
	 * The CSVDataProvider used to store the loaded CSV data.
	 */
	private CSVDataProvider baseProvider;

	/**
	 * A map of drawn plots, keyed on the parent {@code Composite}s. Only one
	 * rendering should be created for a single parent. Subsequent calls to
	 * {@link #draw(String, String, Composite)} with a parent already in the
	 * map's key set should update the plot category and type for the associated
	 * drawn plot.
	 */
	private final Map<Composite, DrawnPlot> drawnPlots;

	/**
	 * The Constructor
	 * 
	 * @param source
	 *            The URI of the CSV file.
	 */
	public CSVPlot(URI source) {
		this.source = source;
		// Create the property map, empty by default
		properties = new HashMap<String, String>();
		// Create the plot type map and add empty arrays by default
		types = new HashMap<String, String[]>();

		// Create the map of drawn plots.
		drawnPlots = new HashMap<Composite, DrawnPlot>();

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
							baseProvider = dataLoader.load(file);
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
						// Set the source so the title and everything gets
						// loaded right later
						baseProvider.setSource(source.toString());
						// Get the variables
						ArrayList<String> variables = baseProvider
								.getFeatureList();
						// Set the first feature as an independent variable
						baseProvider.setFeatureAsIndependentVariable(variables
								.get(0));
						// Create lists to hold the plot types
						ArrayList<String> plotTypes = new ArrayList<String>(
								variables.size());
						// Create the type list. Loop over every variable and
						// make it possible to plot it against the others.
						for (int i = 0; i < variables.size(); i++) {
							for (int j = 0; j < variables.size(); j++) {
								if (i != j) {
									String type = variables.get(i) + " vs. "
											+ variables.get(j);
									plotTypes.add(type);
								}
							}
						}
						// Put the types in the map. Line, scatter, and bar
						// plots can be created for any of the plot types, so we
						// can re-use the same string array.
						String[] plotTypesArray = plotTypes
								.toArray(new String[] {});
						types.put("Line", plotTypesArray);
						types.put("Scatter", plotTypesArray);
						types.put("Bar", plotTypesArray);

					}
				}
			});

			// Get a handle on the parent thread's exception handler
			final UncaughtExceptionHandler parentHandler = Thread
					.currentThread().getUncaughtExceptionHandler();

			// Override the loadingThread's exception handler
			loadingThread
					.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
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
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#draw(java.lang.String,
	 *      java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite draw(String category, String plotType, Composite parent)
			throws Exception {

		Composite child = null;

		// Determine whether the specified plotType is valid. Note that this
		// also requires the category to be valid!
		String[] types = this.types.get(category);
		boolean typeValid = false;
		if (types != null) {
			for (String type : types) {
				if (type != null && type.equals(plotType)) {
					typeValid = true;
					break;
				}
			}
		}

		if (baseProvider != null && typeValid && parent != null
				&& !parent.isDisposed()) {

			// Get the drawn plot associated with the parent Composite, creating
			// a new editor if necessary.
			DrawnPlot drawnPlot = drawnPlots.get(parent);
			if (drawnPlot == null) {
				drawnPlot = new DrawnPlot(parent);
				drawnPlots.put(parent, drawnPlot);

				// When the parent is disposed, remove the drawn plot and
				// dispose of any of its resources.
				parent.addDisposeListener(new DisposeListener() {
					@Override
					public void widgetDisposed(DisposeEvent e) {
						drawnPlots.remove((Composite) e.widget).dispose();
					}
				});
			}

			// Reset the plot time to the initial time.
			Double plotTime = baseProvider.getTimes().get(0);
			// FIXME Won't this affect all of the drawn plots?
			baseProvider.setTime(plotTime);

			// Remove all previous plots.
			drawnPlot.clear();

			// Add the specified series to the drawn plot.
			drawnPlot.addSeries(category, plotType);

			// Refresh the drawn plot.
			drawnPlot.refresh();

			// We need to return the Composite used to render the CSV plot.
			child = drawnPlot.editor.getPlotCanvas();
		} else {
			// Complain that the plot is invalid
			throw new Exception("Invalid plot: category = " + category
					+ ", type = " + plotType + ", provider = "
					+ baseProvider.toString());
		}

		return child;
	}

	/**
	 * An instance of this nested class is composed of the drawn
	 * {@link CSVPlotEditor} and all providers necessary to populate it with CSV
	 * data.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class DrawnPlot {
		// TODO Change CSVPlot to extend MultiPlot and create a PlotRender based
		// off this nested class.

		/**
		 * The editor in which the CSV plot is rendered.
		 */
		public final CSVPlotEditor editor;
		/**
		 * The data provider containing the loaded CSV data.
		 */
		public final CSVDataProvider dataProvider;
		/**
		 * The provider responsible for maintaining the plot configuration.
		 */
		public final PlotProvider plotProvider;

		/**
		 * A tree of JFace {@code Action}s for adding new series to the drawn
		 * plot.
		 */
		private final ActionTree addSeriesTree;
		/**
		 * A tree of JFace {@code Action}s for removing plotted series from the
		 * drawn plot.
		 */
		private final ActionTree removeSeriesTree;

		/**
		 * A map keyed on the series and containing the ActionTrees for removing
		 * them from the plot.
		 */
		private final Map<SeriesProvider, ActionTree> seriesMap = new HashMap<SeriesProvider, ActionTree>();

		/**
		 * Creates a {@link CSVPlotEditor} and all providers necessary to
		 * populate it. The editor is created inside the specified parent
		 * {@code Composite}.
		 * 
		 * @param parent
		 *            The {@code Composite} in which to draw the CSV plot
		 *            editor.
		 */
		public DrawnPlot(Composite parent) throws Exception {
			// Create the editor and all required providers.
			editor = new CSVPlotEditor();
			dataProvider = baseProvider;
			plotProvider = new PlotProvider();

			// Set the plot title based on the file name.
			int lastSeparator = dataProvider.getSourceInfo().lastIndexOf("/");
			String plotTitle = (lastSeparator > -1 ? dataProvider
					.getSourceInfo().substring(lastSeparator + 1)
					: dataProvider.getSourceInfo());
			// Set the title for the new plot provider
			plotProvider.setPlotTitle(plotTitle);

			// Create the plot inside the parent Composite.
			editor.createPartControl(parent);

			// Get the child Composite used to render the
			Composite canvas = editor.getPlotCanvas();

			// Get the context Menu for the parent Composite, or create a new
			// one if the parent lacks a context Menu.
			Menu menu = parent.getMenu();
			if (menu == null) {
				MenuManager menuManager = new MenuManager();
				menu = menuManager.createContextMenu(canvas);
			}

			// Create the ActionTrees for adding and removing series on the fly.
			addSeriesTree = new ActionTree("Add Series");
			removeSeriesTree = new ActionTree("Remove Series");
			final Separator separator = new Separator();
			final ActionTree clearAction = new ActionTree(new Action(
					"Clear Plot") {
				@Override
				public void run() {
					clear();
					refresh();
				}
			});

			// Fill out the add series tree. This tree will never need to be
			// updated.
			for (Entry<String, String[]> e : getPlotTypes().entrySet()) {
				final String category = e.getKey();
				String[] types = e.getValue();

				if (category != null && types != null) {
					// Create the tree for the category and all its types.
					ActionTree catTree = new ActionTree(category);
					addSeriesTree.add(catTree);
					// Create Actions for all the types. Each Action should call
					// addSeries(...) with the category and type.
					for (final String type : types) {
						if (type != null) {
							catTree.add(new ActionTree(new Action(type) {
								@Override
								public void run() {
									addSeries(category, type);
									refresh();
								}
							}));
						}
					}
				}
			}

			// When the Menu is about to be shown, add the add/remove series
			// actions to it.
			menu.addMenuListener(new MenuListener() {
				@Override
				public void menuHidden(MenuEvent e) {
					// Nothing to do.
				}

				@Override
				public void menuShown(MenuEvent e) {
					// Rebuild the menu.
					Menu menu = (Menu) e.widget;
					addSeriesTree.getContributionItem().fill(menu, -1);
					removeSeriesTree.getContributionItem().fill(menu, -1);
					separator.fill(menu, -1);
					clearAction.getContributionItem().fill(menu, -1);
				}
			});

			// Set the context Menu for the main plot canvas. The slider can
			// have its own menu set later.
			editor.getPlotCanvas().setMenu(menu);

			return;
		}

		/**
		 * Adds a new series to the drawn plot.
		 * 
		 * @param category
		 *            The category of the series to add.
		 * @param type
		 *            The type of the series to add.
		 * @param drawnPlot
		 *            The drawn plot that will get a new series.
		 */
		public void addSeries(String category, String type) {
			// Reset the plot time to the initial time.
			Double plotTime = baseProvider.getTimes().get(0);
			// FIXME Won't this affect all of the drawn plots?
			baseProvider.setTime(plotTime);

			// Get the axes to plot
			String[] axes = type.split(" ");
			String axis1 = axes[0];
			String axis2 = axes[2];

			// Create a new series title for the new series
			String seriesTitle = axis1 + " vs. " + axis2 + " at " + plotTime;
			// Create a new series provider
			final SeriesProvider seriesProvider = new SeriesProvider();
			seriesProvider.setDataProvider(dataProvider);
			seriesProvider.setTimeForDataProvider(plotTime);
			seriesProvider.setSeriesTitle(seriesTitle);
			seriesProvider.setXDataFeature(axis1);
			seriesProvider.setYDataFeature(axis2);
			seriesProvider.setSeriesType(category);
			// Add this new series to the plot provider
			plotProvider.addSeries(plotTime, seriesProvider);

			// Add an ActionTree to remove the series.
			ActionTree tree = new ActionTree(new Action(seriesTitle) {
				@Override
				public void run() {
					removeSeries(seriesProvider);
					refresh();
				}
			});
			removeSeriesTree.add(tree);

			// Store the series and ActionTree for later reference.
			seriesMap.put(seriesProvider, tree);

			return;
		}

		/**
		 * Removes the specified series from the drawn plot.
		 * 
		 * @param series
		 *            The series to remove.
		 */
		public void removeSeries(SeriesProvider series) {
			ActionTree tree = seriesMap.remove(series);
			if (tree != null) {
				double plotTime = baseProvider.getTimes().get(0);
				removeSeriesTree.remove(tree);
				plotProvider.removeSeries(plotTime, series);
			}
			return;
		}

		/**
		 * Clears all series from the drawn plot.
		 */
		public void clear() {
			double plotTime = baseProvider.getTimes().get(0);
			for (Entry<SeriesProvider, ActionTree> e : seriesMap.entrySet()) {
				plotProvider.removeSeries(plotTime, e.getKey());
			}
			seriesMap.clear();
			removeSeriesTree.removeAll();
			return;
		}

		/**
		 * Refreshes the drawn plot after a change has occurred.
		 */
		public void refresh() {
			// Add the new plot to the editor.
			editor.showPlotProvider(plotProvider);
		}

		/**
		 * Disposes of the drawn plot and all related resources.
		 */
		public void dispose() {
			// Nothing to do yet.
		}
	}

}
