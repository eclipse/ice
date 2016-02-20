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
package org.eclipse.eavp.viz.service.widgets;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.IVizService;
import org.eclipse.eavp.viz.service.IVizServiceFactory;
import org.eclipse.eavp.viz.service.PlotEditor;
import org.eclipse.eavp.viz.service.internal.VizServiceFactoryHolder;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides access to a dialog that can be used to create an IPlot
 * from all available visualization services. This is extremely useful if more
 * than one visualization service is capable of rendering a given file, as the
 * user will need to make a selection.
 * 
 * @author Jordan Deyton
 *
 */
public class PlotDialogProvider {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(PlotEditor.class);

	/**
	 * A lexicographically ordered map of successfully created plots keyed on
	 * the viz service names.
	 */
	private final Map<String, IPlot> allPlots = new TreeMap<String, IPlot>();

	/**
	 * The name of the selected viz service.
	 */
	private String selectedServiceName = null;

	/**
	 * This method creates all possible plots using the currently available viz
	 * services.
	 * 
	 * @param uri
	 *            The URI for the plot file.
	 * @return A map containing all created plots, keyed on the names of the viz
	 *         services that created them.
	 */
	private void createPlots(URI uri) {
		// Get the VizServiceFactory and all Viz Services
		IVizServiceFactory factory = VizServiceFactoryHolder.getFactory();

		// Initialize a map of created plots for all viz services.
		for (String vizServiceName : factory.getServiceNames()) {
			IVizService service = factory.get(vizServiceName);

			// Add any successfully created plot to the map.
			if (service != null) {
				try {
					IPlot newPlot = service.createPlot(uri);
					if (newPlot != null) {
						allPlots.put(vizServiceName, newPlot);
					}
				} catch (Exception e) {
					logger.debug("The viz service \"" + vizServiceName
							+ "\" could not create a plot for the file \""
							+ uri.getPath() + "\".");
				}
			}
		}

		return;
	}

	/**
	 * Gets all plots that were created by the dialog. This includes the one for
	 * the selected viz service.
	 * 
	 * @return A list containing all created plots.
	 */
	public List<IPlot> getAllPlots() {
		return new ArrayList<IPlot>(allPlots.values());
	}

	/**
	 * Gets the plot created from the selected visualization service.
	 * 
	 * @return The selected plot, or {@code null} if the dialog was cancelled or
	 *         there was no viz service that could create a plot.
	 */
	public IPlot getSelectedPlot() {
		return allPlots.get(selectedServiceName);
	}

	/**
	 * Gets the service used to create the plot.
	 * 
	 * @return The IVizService which was chosen to create the plot.
	 */
	public IVizService getSelectedService() {
		return VizServiceFactoryHolder.getFactory().get(selectedServiceName);
	}

	/**
	 * If there are multiple visualization services available that can create
	 * plots for the specified file, this presents a dialog allowing the user to
	 * select the preferred service.
	 * <p>
	 * Otherwise, a dialog is not presented if there is no viz service or only
	 * one available for the specified URI.
	 * </p>
	 * <p>
	 * The plot created by the selected visualization service can be retrieved
	 * by calling {@link #getSelectedPlot()}.
	 * </p>
	 * 
	 * @param shell
	 *            The parent shell on which to open the dialog.
	 * @param uri
	 *            The URI of the file that needs a plot.
	 * @return The result of the dialog. If {@link Window#OK}, then at least one
	 *         plot is available or was selected. Otherwise (
	 *         {@link Window#CANCEL}), no plot was selected.
	 */
	public int openDialog(Shell shell, URI uri) {

		int result = Window.CANCEL;

		// Create plots using all available viz services.
		allPlots.clear();
		selectedServiceName = null;
		createPlots(uri);

		// If plots could be created, get one of them.
		if (!allPlots.isEmpty()) {
			// The number of services which succeeded in creating
			// PlotEditorInputs
			int numServices = allPlots.size();

			// If more than one service is applicable, create a dialog
			// window to prompt the user for which is to be used. Else, use
			// the single available service.
			if (numServices == 1) {
				selectedServiceName = allPlots.keySet().iterator().next();
				result = Window.OK;
			} else {
				// Get the viz service names.
				List<String> vizServiceNames = new ArrayList<String>(
						allPlots.keySet());

				// Create a dialog to allow the user to select from the
				// available viz services.
				ComboDialog dialog = new ComboDialog(shell, true);
				// Set up the dialog's data.
				dialog.setTitle("Open a Visualization");
				dialog.setInfoText("There are multiple available visualization "
						+ "services that can render the selected file. Please "
						+ "select one from the list below.");
				dialog.setAllowedValues(vizServiceNames);
				dialog.setInitialValue(vizServiceNames.get(0));

				// If a value was selected, get the plot.
				result = dialog.open();
				if (result == Window.OK) {
					selectedServiceName = dialog.getValue();
				} else {
					selectedServiceName = null;
				}
			}
		}
		// If not plots could be created, log an error.
		else {
			logger.debug("All available viz services failed to render a plot.");
		}

		return result;

	}

}
