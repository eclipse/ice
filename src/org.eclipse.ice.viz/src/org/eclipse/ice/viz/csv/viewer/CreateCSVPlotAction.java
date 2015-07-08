/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.viz.csv.viewer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.datastructures.resource.VizResource;
import org.eclipse.ice.viz.VizFileViewer;
import org.eclipse.ice.viz.service.csv.CSVDataLoader;
import org.eclipse.ice.viz.service.csv.CSVDataProvider;
import org.eclipse.ice.viz.service.csv.PlotProvider;
import org.eclipse.ice.viz.service.csv.SeriesProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Action opens a dialog that allows the user to pick from plots available
 * in the selected file in the {@link VizFileViewer}.
 * 
 * @author Matthew Wang, Taylor Patterson
 */
public class CreateCSVPlotAction extends Action {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(CreateCSVPlotAction.class);

	/**
	 * The ViewPart that owns an object of this class.
	 */
	private final ViewPart viewer;

	/**
	 * The constructor
	 * 
	 * @param parentView
	 *            The ViewPart to whom the object of this class belongs.
	 * @param parentAction
	 *            The AddFileAction to whom the object of this class belongs.
	 */
	public CreateCSVPlotAction(ViewPart parentView,
			AddCSVPlotAction parentAction) {

		// Keep track of the viewer and parent Action containing this Action
		viewer = parentView;

		// Set the display text
		setText("Add a new plot");

		return;
	}

	/**
	 * The function called whenever the action is clicked.
	 */
	@Override
	public void run() {

		// Get the Shell of the workbench
		Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getShell();

		// Get the viewer as a PlotViewer.
		CSVPlotViewer plotViewer = (CSVPlotViewer) viewer;

		// Get the VizResource used by the PlotViewer.
		VizResource resource = plotViewer.getResource();

		// If we pulled an VizResource from the selection, we may proceed.
		if (resource != null) {
			logger.info("CreateCSVPlotAction message: "
					+ "The currently selected resource is \""
					+ resource.getName() + "\".");
			// Checking for a VizResource for a file set
			if (resource instanceof VizResource
					&& ((VizResource) resource).getFileSet() != null) {
				plotFileSet(shell, plotViewer, resource);
			} else {
				plotFile(shell, plotViewer, resource);
			}
		}
		return;
	}

	/**
	 * 
	 * @param shell
	 * @param plotViewer
	 * @param resource
	 */
	public void plotFile(Shell shell, CSVPlotViewer plotViewer,
			VizResource resource) {
		// Get the file from the contents
		File file = resource.getContents();
		String fileName = file.getAbsolutePath();

		CSVDataProvider newDataProvider = null;

		if (fileName.matches(".*\\.csv$")) {
			// Handle a CSV file
			CSVDataLoader newCSVDataLoader = new CSVDataLoader();
			newDataProvider = newCSVDataLoader.load(fileName);
		} else {
			return;
		}
		// Set the source as the file name
		newDataProvider.setSource(file.getName());

		if (newDataProvider.getFeatureList().size() > 1) {
			setIndependentVar(shell, newDataProvider);
		}

		// Create a new AddPlotDialog
		AddPlotDialog plotDialog = new AddPlotDialog(shell);
		if (plotDialog.open() == Window.CANCEL) {
			return;
		}
		String[] userSelectedPlotTypes = plotDialog.getSelections();

		if (userSelectedPlotTypes != null && userSelectedPlotTypes.length > 0) {
			final String plotType = userSelectedPlotTypes[0];
			if ("Contour".equals(plotType)) {
				if (newDataProvider.getDataHeight() == 0
						|| newDataProvider.getDataWidth() == 0) {
					return;
				}
				// Handle plotting a contour plot. If it has made it here, then
				// plotting is ready. Create a new plot provider
				PlotProvider newPlotProvider = new PlotProvider();

				// Set the new plot provider to a contour
				newPlotProvider.setPlotAsContour();

				// The new plot's title
				String newPlotTitle = newDataProvider.getSourceInfo();

				// The plot's set time
				Double plotTime = newDataProvider.getTimes().get(0);

				// Set the time, just in case
				newDataProvider.setTime(plotTime);

				// Set the title for the new plot provider
				newPlotProvider.setPlotTitle(newPlotTitle);

				// Get the yAxisFeature
				String yAxisFeature = newDataProvider.getFeatureList().get(0);

				// Create a new series provider to hold the data for
				// the contour
				SeriesProvider newContourSeries = new SeriesProvider();

				// Set the data provider to use for the contour plot
				newContourSeries.setDataProvider(newDataProvider);

				// Set the data max
				newContourSeries.setDataMax(newDataProvider.getDataMax());
				// Set the data min
				newContourSeries.setDataMin(newDataProvider.getDataMin());

				// Set the time the data provider will use
				newContourSeries.setTimeForDataProvider(plotTime);
				// Set the data feature for the series
				newContourSeries.setYDataFeature(yAxisFeature);
				// Set the title for the contour
				newContourSeries.setSeriesTitle("Contour of " + yAxisFeature);
				// Add the contour series to the plot provider
				newPlotProvider.addSeries(plotTime, newContourSeries);

				// Send it to the TreeViewer to display it

				// Send it to the visualization view to display the plot
				plotViewer.addPlot(newPlotProvider);
			} else {
				// Handle plotting a scatter, line, or bar series graph
				SelectFeatureDialog featureDialog = new SelectFeatureDialog(
						shell);

				// Need to set the independent variables for the feature
				// dialog
				featureDialog.setXAxisFeatures(newDataProvider
						.getIndependentVariables());
				// Need to set the features to be plotted
				featureDialog
						.setYAxisFeatures(newDataProvider.getFeatureList());
				if (featureDialog.open() == Window.OK
						&& !featureDialog.getXAxisFeatures().isEmpty()
						&& !featureDialog.getYAxisFeatures().isEmpty()) {
					// If it has made it here, then plotting is ready.
					// Create a new plot provider
					PlotProvider newPlotProvider = new PlotProvider();
					// The new plot's title
					String newPlotTitle = newDataProvider.getSourceInfo();
					// Set the title for the new plot provider
					newPlotProvider.setPlotTitle(newPlotTitle);

					// Get the features that need to be plotted against each
					// other.
					List<String> xAxisFeatures = featureDialog
							.getXAxisFeatures();
					List<String> yAxisFeatures = featureDialog
							.getYAxisFeatures();

					// Reset the plot's time to the default (first) one.
					Double plotTime = newDataProvider.getTimes().get(0);
					newDataProvider.setTime(plotTime);

					for (String xAxisFeature : xAxisFeatures) {
						// Every series will have this basic title. The x-axis
						// data name goes second.
						String baseTitle = " vs. " + xAxisFeature + " at "
								+ plotTime;
						for (String yAxisFeature : yAxisFeatures) {
							// Create a new series title for the new series
							String newSeriesTitle = yAxisFeature + baseTitle;

							// Create a new series provider
							SeriesProvider newSeriesProvider = new SeriesProvider();
							newSeriesProvider.setDataProvider(newDataProvider);
							newSeriesProvider.setTimeForDataProvider(plotTime);
							newSeriesProvider.setSeriesTitle(newSeriesTitle);
							newSeriesProvider.setXDataFeature(xAxisFeature);
							newSeriesProvider.setYDataFeature(yAxisFeature);
							newSeriesProvider.setSeriesType(plotType);

							// Add this new series to the plot provider
							newPlotProvider.addSeries(plotTime,
									newSeriesProvider);
						}
					}
					// Send it to the visualization view to display the
					// plot
					plotViewer.addPlot(newPlotProvider);
				}
			}
		}
		return;
	}

	/**
	 * 
	 * @param shell
	 * @param plotViewer
	 * @param resource
	 */
	public void plotFileSet(Shell shell, CSVPlotViewer plotViewer,
			VizResource resource) {
		String[] fileSet = resource.getFileSet();

		// Creating a new CSVDataProvider for the new file
		CSVDataLoader newDataSetLoader = new CSVDataLoader();
		// Calls the load as file set
		CSVDataProvider newDataSetProvider = null;

		if (resource.getName().matches(".*\\.csv$")) {
			// Handle a CSV file
			newDataSetLoader = new CSVDataLoader();
			newDataSetProvider = newDataSetLoader.loadAsFileSet(fileSet);
		} else {
			return;
		}

		// Set the source of the provider
		newDataSetProvider.setSource(resource.getFileSetTitle());

		// Set the independent variable
		if (newDataSetProvider.getFeatureList().size() > 1) {
			setIndependentVar(shell, newDataSetProvider);
		}

		// Uses the AddPlotDialog class, a custom dialog, to
		// prompt what plot type to use
		AddPlotDialog plotDialog = new AddPlotDialog(shell);
		plotDialog.open();
		// The selected plot types the user would like
		String[] userSelectedPlotTypes = plotDialog.getSelections();

		if (userSelectedPlotTypes == null || userSelectedPlotTypes.length == 0) {
			return;
		}
		final String plotType = userSelectedPlotTypes[0];
		/**
		 * IF THE USER SELECTS CONTOUR
		 */
		if ("Contour".equals(plotType)) {
			if (newDataSetProvider.getDataHeight() == 0
					|| newDataSetProvider.getDataWidth() == 0) {
				return;
			}
			// If it has made it here, then plotting is
			// ready
			// Create a new plot provider
			PlotProvider newPlotProvider = new PlotProvider();

			// Set the new plot provider to a contour
			newPlotProvider.setPlotAsContour();

			// Get the initial time
			Double plotTime = newDataSetProvider.getTimes().get(0);

			// Set the title for the new plot provider
			newPlotProvider.setPlotTitle(newDataSetProvider.getSourceInfo());

			String yAxisFeature = newDataSetProvider.getFeatureList().get(0);

			// Create a new series provider to hold the data for
			// the contour
			SeriesProvider newContourSeries = new SeriesProvider();
			// Set the data provider to use for the contour plot
			newContourSeries.setDataProvider(newDataSetProvider);
			// Set the time the data provider will use
			newContourSeries.setTimeForDataProvider(plotTime);
			// Set the data feature for the series
			newContourSeries.setYDataFeature(yAxisFeature);
			// Set the title for the contour
			newContourSeries.setSeriesTitle("Contour of " + yAxisFeature);
			// Add the contour series to the plot provider
			newPlotProvider.addSeries(plotTime, newContourSeries);
			// Add the new plot to the viewer
			plotViewer.addPlot(newPlotProvider);
		} else {
			if (newDataSetProvider.getDataHeight() != 0
					|| newDataSetProvider.getDataWidth() != 0) {
				return;
			}
			SelectFeatureDialog featureDialog = new SelectFeatureDialog(shell);
			// Set the independent variables from the provider
			// to be plotted
			featureDialog.setXAxisFeatures(newDataSetProvider
					.getIndependentVariables());
			// Set the features from the provider to be plotted
			featureDialog.setYAxisFeatures(newDataSetProvider.getFeatureList());
			// Open the dialog
			if (featureDialog.open() == Window.OK
					&& featureDialog.getXAxisFeatures() != null
					&& featureDialog.getYAxisFeatures() != null) {

				// If it has made it here, then plotting is
				// ready. Create a new plot provider.
				PlotProvider newPlotProvider = new PlotProvider();

				// Set the title for the new plot provider
				newPlotProvider
						.setPlotTitle(newDataSetProvider.getSourceInfo());

				// Get the times available in the provider
				List<Double> times = newDataSetProvider.getTimes();

				// Adding the series for each time
				for (int timeIndex = 0; timeIndex < times.size(); timeIndex++) {
					double currentTime = times.get(timeIndex);
					// Set the current time
					newDataSetProvider.setTime(currentTime);

					// Get the independent variables and features for the data
					// set.
					List<String> independentVariables = newDataSetProvider
							.getIndependentVariables();
					List<String> featureVariables = newDataSetProvider
							.getFeaturesAtCurrentTime();

					// Get the features that need to be plotted against each
					// other.
					List<String> xAxisFeatures = featureDialog
							.getXAxisFeatures();
					List<String> yAxisFeatures = featureDialog
							.getYAxisFeatures();

					// Remove all selected x and y axis variables that are not
					// valid independent and feature variables in the data set.
					for (int x = xAxisFeatures.size() - 1; x >= 0; x--) {
						if (!independentVariables
								.contains(xAxisFeatures.get(x))) {
							xAxisFeatures.remove(x);
						}
					}
					for (int y = yAxisFeatures.size() - 1; y >= 0; y--) {
						if (!featureVariables.contains(yAxisFeatures.get(y))) {
							yAxisFeatures.remove(y);
						}
					}

					// For all remaning selected x and y axis, plot them.
					for (String xAxisFeature : xAxisFeatures) {
						// Every series will have this basic title. The x-axis
						// data name goes second.
						String baseTitle = " vs. " + xAxisFeature + " at "
								+ currentTime;
						for (String yAxisFeature : yAxisFeatures) {
							// Create a new series title for the new series
							String newSeriesTitle = yAxisFeature + baseTitle;

							// Create a new series provider
							SeriesProvider newSeriesProvider = new SeriesProvider();
							newSeriesProvider
									.setDataProvider(newDataSetProvider);
							newSeriesProvider
									.setTimeForDataProvider(currentTime);
							newSeriesProvider.setSeriesTitle(newSeriesTitle);
							newSeriesProvider.setXDataFeature(xAxisFeature);
							newSeriesProvider.setYDataFeature(yAxisFeature);
							newSeriesProvider.setSeriesType(plotType);

							// Add this new series to the plot provider
							newPlotProvider.addSeries(currentTime,
									newSeriesProvider);
						}
					}
				}
				// Add the new plot to the viewer
				plotViewer.addPlot(newPlotProvider);
			}
		}
		return;
	}

	/**
	 * 
	 * @param shell
	 * @param newDataProvider
	 */
	public void setIndependentVar(Shell shell, CSVDataProvider newDataProvider) {
		// Open the dialog for selecting the independent variables
		SelectIndependentVarDialog independentVarDialog = new SelectIndependentVarDialog(
				shell);
		// Set the feature list that the user will choose from
		independentVarDialog.setFeatureList(newDataProvider.getFeatureList());
		independentVarDialog.setProviderName(newDataProvider.getSourceInfo());

		// If the user hits cancel or doesn't select any independent
		// variables, do not load the file and exit
		if (independentVarDialog.open() == Window.CANCEL
				|| independentVarDialog.getIndependentVars().isEmpty()) {
			System.err.println("No independent variables selected.");
			return;
		}
		// Get the selected independent variables from the dialog
		ArrayList<String> independentVars = independentVarDialog
				.getIndependentVars();
		// Sets the feature as a independent variable in the
		// provider
		for (String newIndependentVar : independentVars) {
			newDataProvider.setFeatureAsIndependentVariable(newIndependentVar);
		}
	}
}
