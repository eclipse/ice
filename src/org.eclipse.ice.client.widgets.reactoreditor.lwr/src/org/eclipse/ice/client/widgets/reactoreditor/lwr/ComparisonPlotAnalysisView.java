/*******************************************************************************
 * Copyright (c) 2013, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.reactoreditor.lwr;

import org.eclipse.ice.analysistool.IDataProvider;

import org.eclipse.ice.client.widgets.reactoreditor.AnalysisDataReader;
import org.eclipse.ice.client.widgets.reactoreditor.DataSource;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.datastructures.form.ResourceComponent;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRDataProvider;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

import org.eclipse.jface.action.Action;

/**
 * This class provides an {@link IAnalysisView} specifically for comparison data
 * generated for an input and reference {@link PressurizedWaterReactor}.<br>
 * <br>
 * Instead of a {@link FuelAssembly}, it takes an {@link AnalysisDataReader}. It
 * allows the user to plot the data similarly to the standard
 * {@link PlotAnalysisView}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ComparisonPlotAnalysisView extends PlotAnalysisView {

	/**
	 * The default constructor.
	 * 
	 * @param dataSource
	 *            The data source, e.g. Input/Reference/Comparison.
	 */
	public ComparisonPlotAnalysisView(DataSource dataSource) {
		super(dataSource);

		// FIXME - Remove this if the feature selector works.
		// feature = "Fuel Pin Difference";

		// FIXME - This needs a better system in place!
		// Set up the default GridLabelProvider.
		labelProvider = new GridLabelProvider(26);
		ArrayList<String> columnLabels = new ArrayList<String>(Arrays.asList(
				"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
				"M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
				"Y", "Z"));
		labelProvider.setColumnLabels(columnLabels);

		ArrayList<String> rowLabels = new ArrayList<String>();
		for (int i = 0; i < 26; i++) {
			rowLabels.add(Integer.toString(i + 1));
		}
		labelProvider.setRowLabels(rowLabels);

		return;
	}

	/**
	 * This grabs the rods' LWRDataProviders from an AnalysisDataProvider and
	 * stores references to them in assemblyData. This is analogous to the super
	 * class' setAssembly() method.
	 * 
	 * @param object
	 *            The AnalysisDataReader from which to pull the data.
	 */
	private void readPlotData(AnalysisDataReader reader) {

		// Don't do anythign else if there is not enough data to plot.
		if (reader.getNumberOfAssemblies() > 0) {

			// Get the number of rows and columns.
			int rows = reader.getAssemblyRows();
			int columns = reader.getAssemblyColumns();
			// FIXME - size is the same as rows/columns.
			size = rows;
			int totalSize = size * size;

			/* ---- Update the components, data providers, and features. ---- */
			// Reset the lists.
			assemblyLocations.clear();
			assemblyData.clear();
			// Also clear the set of available features.
			featureSet.clear();
			validLocations.clear();
			selectedLocations.clear();

			// Get the data providers from the reader.
			List<LWRDataProvider> analysisData = reader
					.getAssemblyDataProviders(0);

			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++) {
					LWRComponent component = new LWRComponent();
					IDataProvider dataProvider = analysisData.get(row * columns
							+ column);
					if (dataProvider != null) {
						// Get the features available here. Add any new feature
						// to the set of features. Also mark a flag for a valid
						// location if there is actually some data available for
						// the feature.
						for (String feature : dataProvider.getFeatureList()) {
							if (!dataProvider.getDataAtCurrentTime(feature)
									.isEmpty()) {
								if (!featureSet.contains(feature)) {
									featureSet.add(feature);
									validLocations.put(feature, new BitSet(
											totalSize));
									selectedLocations.put(feature, new BitSet(
											totalSize));
								}
								validLocations.get(feature).set(
										row * columns + column);
							}
						}
					} else {
						// Create an empty data provider instead of putting a
						// null value in the list.
						dataProvider = new LWRDataProvider();
					}
					// Add the rod and its data provider to the lists.
					assemblyLocations.add(component);
					assemblyData.add(dataProvider);
				}
			}
			/* -------------------------------------------------------------- */

			// Add Actions for setting the features to the feature ActionTree.
			featureTree.removeAll();
			for (final String feature : featureSet) {
				Action action = new Action(feature) {
					@Override
					public void run() {
						setFeature(feature);
					}
				};
				featureTree.add(new ActionTree(action));
			}

			// Load the first available feature.
			if (!featureSet.isEmpty()) {
				// Grab the first available feature.
				feature = featureSet.first();
			} else {
				feature = null;
			}

			// Reset the graph.
			resetGraph();
		}
		return;
	}

	/**
	 * The ComparisonPlotComposite does not need to listen for any keys.
	 */
	@Override
	public void registerKeys() {
		return;
	}

	/**
	 * The ComparisonPlotComposite does not need to listen for any keys.
	 */
	@Override
	public void unregisterKeys() {
		return;
	}

	/**
	 * The ComparisonPlotComposite will be fed a URI rather than a FuelAssembly
	 * or Reactor object. We need to handle this input appropriately.
	 */
	@Override
	public void setData(String key, Object value) {
		DataSource source = DataSource.valueOf(key);

		// Use this function to set the reactor
		if (source == dataSource) {
			// Initialize a new AnalysisDataReader.
			AnalysisDataReader dataReader = new AnalysisDataReader();

			// Get the URI from the incoming value.
			ResourceComponent resourceComponent = (ResourceComponent) value;
			URI uri = resourceComponent.getResources().get(0).getPath();

			// Read in the data from the URI.
			dataReader.readData(uri);

			// Set the data used by this class for plotting.
			readPlotData(dataReader);
		}

		return;
	}
}
