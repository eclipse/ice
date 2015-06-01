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

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.client.widgets.reactoreditor.AnalysisView;
import org.eclipse.ice.client.widgets.reactoreditor.ColorFactory;
import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.IAnalysisView;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell.State;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditorInput;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.ice.reactor.AssemblyType;
import org.eclipse.ice.reactor.GridLabelProvider;
import org.eclipse.ice.reactor.LWRComponent;
import org.eclipse.ice.reactor.LWRDataProvider;
import org.eclipse.ice.reactor.pwr.FuelAssembly;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.nebula.visualization.xygraph.dataprovider.CircularBufferDataProvider;
import org.eclipse.nebula.visualization.xygraph.figures.Trace;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.XYGraph;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provides an {@link IAnalysisView} for plotting data for rods,
 * tubes, and other {@link LWRComponent}s.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PlotAnalysisView extends AnalysisView {

	/**
	 * The name for this type of analysis view. This can be used for the display
	 * text of SWT widgets that reference this view.
	 */
	protected static final String name = "Plot";
	/**
	 * A brief description of this type of analysis view. This can be used for
	 * ToolTips for SWT widgets referencing this view.
	 */
	protected static final String description = "A plot view for graphical analysis of LWR data.";

	/* ----- GUI Components ----- */
	/**
	 * The Canvas containing the XYGraph.
	 */
	private Canvas canvas;
	/**
	 * The LightweightSystem for the XYGraph. When we reset the graph, we need
	 * to reset this LightweightSystem's contents to the new graph.
	 */
	private LightweightSystem lws;
	/**
	 * The XYGraph displayed on the Canvas.
	 */
	private XYGraph xyGraph;

	/**
	 * An ActionTree containing the available features for the
	 * currently-selected assembly.
	 */
	protected final ActionTree featureTree;
	/* -------------------------- */

	/*----- Current state info -----*/
	/**
	 * The currently-selected assembly for which data can be plotted.
	 */
	private FuelAssembly assembly;
	/**
	 * The size of the current assembly or data.
	 */
	protected int size;
	/**
	 * A label provider for column and row labels for the assembly or data.
	 */
	protected GridLabelProvider labelProvider;

	/**
	 * The current set of components in the assembly.
	 */
	protected final List<LWRComponent> assemblyLocations;
	/**
	 * The current set of data providers for an assembly.
	 */
	protected final List<IDataProvider> assemblyData;

	/**
	 * An ordered set of the available features in the assembly.
	 */
	protected final TreeSet<String> featureSet;
	/**
	 * The currently-selected feature for which we can display data.
	 */
	protected String feature;

	/**
	 * A map of traces plotted on the XYGraph. This is keyed on the row and
	 * column through the getKey() method.
	 */
	private final Map<String, Trace> traces;

	/**
	 * A map of valid locations in the assembly. There is one for each feature.
	 * If the bit is not set, then the IDataProvider in that location does not
	 * have any data available for the feature.
	 */
	protected final Map<String, BitSet> validLocations;

	/**
	 * A map of selected locations in the assembly. We can keep track of all
	 * selected locations for each feature type. If the bit is set, then the
	 * data in that position for that feature is already plotted.
	 */
	protected final Map<String, BitSet> selectedLocations;

	/**
	 * A ColorFactory to use for the trace colors.
	 */
	protected final ColorFactory palette;

	/*------------------------------*/

	/**
	 * The default constructor.
	 * 
	 * @param dataSource
	 *            The data source associated with this view (input, reference,
	 *            comparison).
	 */
	public PlotAnalysisView(DataSource dataSource) {
		super(dataSource);

		// Default values for all fields, including final collections.
		assembly = null;
		size = 0;
		assemblyLocations = new ArrayList<LWRComponent>();
		assemblyData = new ArrayList<IDataProvider>();
		featureSet = new TreeSet<String>();
		feature = null;
		traces = new HashMap<String, Trace>();
		validLocations = new HashMap<String, BitSet>();
		selectedLocations = new HashMap<String, BitSet>();
		palette = new ColorFactory();

		// Populate the list of actions (for ToolBar and context Menu).

		// Add an ActionTree (single button) for selecting series.
		ActionTree selectSeries = new ActionTree(new Action(
				"Select Plotted Series") {
			@Override
			public void run() {
				requestPlotSeries();
			}
		});
		actions.add(selectSeries);

		// Add an ActionTree (single button) for clearing the plot.
		actions.add(new ActionTree(new Action("Clear Plot") {
			@Override
			public void run() {
				MessageDialog dialog = new MessageDialog(container.getShell(),
						"Clear Plot", null,
						"Are you sure you want to remove all plotted series?",
						MessageDialog.QUESTION, new String[] { "Yes", "No" }, 0);

				if (dialog.open() == Window.OK) {
					/* ---- Clear the currently-graphed data. ---- */
					BitSet selected = selectedLocations.get(feature);
					if (selected != null) {
						for (int i = selected.nextSetBit(0); i >= 0; i = selected
								.nextSetBit(i + 1)) {
							unplotData(i);
						}
					}
					// Reset the color palette.
					palette.reset();
					/* ------------------------------------------- */
				}
			}
		}));

		// Add an ActionTree to list the available features.
		featureTree = new ActionTree("Data Feature");
		actions.add(featureTree);

		// Add an ActionTree (single button) for saving the viewer image.
		ActionTree saveImage = new ActionTree(new Action("Save Image") {
			@Override
			public void run() {
				saveCanvasImage(canvas);
			}
		});
		actions.add(saveImage);

		return;
	}

	/**
	 * Sets the assembly for which data can be plotted. It resets all
	 * currently-plotted data.
	 * 
	 * @param assembly
	 *            The new assembly whose IDataProviders can be queried to plot
	 *            feature data.
	 */
	private void setAssembly(FuelAssembly assembly) {
		if (assembly != null && assembly != this.assembly) {
			// Update the assembly.
			this.assembly = assembly;

			// Get the size of the assembly.
			size = assembly.getSize();
			int rows = size;
			int columns = size;
			int totalSize = rows * columns;

			labelProvider = assembly.getGridLabelProvider();

			/* ---- Update the components, data providers, and features. ---- */
			// Reset the lists.
			assemblyLocations.clear();
			assemblyData.clear();
			// Also clear the set of available features.
			featureSet.clear();
			validLocations.clear();
			selectedLocations.clear();

			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++) {
					LWRComponent component = assembly.getLWRRodByLocation(row,
							column);
					IDataProvider dataProvider = assembly
							.getLWRRodDataProviderAtLocation(row, column);
					if (component == null) {
						component = assembly.getTubeByLocation(row, column);
					}
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
	 * Sets the current feature for which data can be plotted.
	 * 
	 * @param feature
	 *            The new feature to use.
	 */
	protected void setFeature(String feature) {
		if (feature != this.feature) {
			// Update the feature.
			this.feature = feature;

			// Reset the graph.
			resetGraph();
		}
		return;
	}

	/**
	 * Creates a {@link GridEditorDialog} to prompt the user for the current
	 * selection of plotted data for the current feature.
	 */
	private void requestPlotSeries() {
		System.out.println("Request plot series called");
		if (feature != null) {
			System.out.println("Feature is " + feature);

			// Get the size of the assembly.
			int rows = size;
			int columns = size;

			// ---- Create and configure input for a GEF grid. ---- //
			// Create an IEditorInput for the GridEditorDialog.
			GridEditorInput input = new GridEditorInput(rows, columns);

			// Get row and column labels from the assembly's GridLabelProvider.
			if (labelProvider != null) {
				List<String> rowLabels = new ArrayList<String>(rows);
				List<String> columnLabels = new ArrayList<String>(columns);
				for (int i = 0; i < size; i++) {
					rowLabels.add(labelProvider.getLabelFromRow(i));
					columnLabels.add(labelProvider.getLabelFromColumn(i));
				}
				input.setRowLabels(rowLabels);
				input.setColumnLabels(columnLabels);
			}

			BitSet valid = validLocations.get(feature);
			if (valid == null) {
				valid = new BitSet(rows * columns);
			}
			BitSet selected = selectedLocations.get(feature);
			if (selected == null) {
				selected = new BitSet(rows * columns);
			}

			// Get the current list of valid/selected components in the assembly
			// for the current feature. Locations with no component are invalid,
			// locations with a component but no data are disabled, others are
			// either unselected or selected.
			List<State> states = new ArrayList<State>(rows * columns);
			for (int i = 0; i < rows * columns; i++) {
				State state = (assemblyLocations.get(i) != null ? State.DISABLED
						: State.INVALID);
				if (selected.get(i)) {
					state = State.SELECTED;
				} else if (valid.get(i)) {
					state = State.UNSELECTED;
				}
				states.add(state);
			}

			// Set the list of states.
			input.setStates(states);
			// ---------------------------------------------------- //

			// ---- Open the GridEditorDialog and get the selection. ---- //
			GridEditorDialog dialog = new GridEditorDialog(
					container.getShell(), input);
			if (dialog.open() != Window.CANCEL) {
				// Get the resulting selection.
				BitSet newSelectedLocations = dialog.getSelectedLocations();

				// Compute the removed locations. This is effectively
				// selectedLocations - newSelectedLocations.
				BitSet removedLocations = (BitSet) selected.clone();
				removedLocations.andNot(newSelectedLocations);

				// Compute the added locations. This is the same as
				// newSelectedLocations - selectedLocations.
				BitSet addedLocations = (BitSet) newSelectedLocations.clone();
				addedLocations.andNot(selected);

				// Unplot all of the removed locations.
				for (int i = removedLocations.nextSetBit(0); i >= 0; i = removedLocations
						.nextSetBit(i + 1)) {
					unplotData(i);
					states.set(i, State.UNSELECTED);
				}

				// Plot all of the added locations.
				for (int i = addedLocations.nextSetBit(0); i >= 0; i = addedLocations
						.nextSetBit(i + 1)) {
					plotData(i);
					states.set(i, State.SELECTED);
				}
			}
			// ---------------------------------------------------------- //

		}
		return;
	}

	/**
	 * Plots the data for a specific IDataProvider on the plot for the current
	 * feature.
	 * 
	 * @param index
	 *            The index of the cell in the assembly grid. This corresponds
	 *            to an IDataProvider.
	 */
	private void plotData(int index) {
		// Get the row and column for the index.
		String key = getKey(index);

		if (index >= 0 && index < size * size && !traces.containsKey(key)) {
			// Get the data provider.
			IDataProvider dataProvider = assemblyData.get(index);
			ArrayList<IData> data = dataProvider.getDataAtCurrentTime(feature);
			int size = data.size();
			if (size > 0) {
				// Allocate the arrays of x and y values.
				double[] xValues = new double[size];
				double[] yValues = new double[size];

				// Set the x and y values from the data.
				for (int i = 0; i < data.size(); i++) {
					xValues[i] = data.get(i).getPosition().get(2);
					// FIXME - We may need to account for un-configured
					// positions in another way. The default value for
					// un-configured positions is 0.0. getPosition() always
					// returns an ArrayList of 3 doubles (x, y, z).
					if (xValues[i] == 0.0) {
						xValues[i] = i;
					}
					yValues[i] = data.get(i).getValue();
				}

				// Create a trace data provider.
				CircularBufferDataProvider traceDataProvider = new CircularBufferDataProvider(
						false);
				traceDataProvider.setBufferSize(size);
				traceDataProvider.setCurrentXDataArray(xValues);
				traceDataProvider.setCurrentYDataArray(yValues);

				// Create the trace and set its properties.
				Trace trace = new Trace(key, xyGraph.primaryXAxis,
						xyGraph.primaryYAxis, traceDataProvider);
				trace.setPointStyle(PointStyle.XCROSS);
				trace.setTraceColor(palette.getNextColor());

				// Add the trace to the graph.
				traces.put(key, trace);
				xyGraph.addTrace(trace);

				// Update the bit in the feature's selected locations BitSet.
				BitSet selected = selectedLocations.get(feature);
				selected.set(index);
			}
		}
		return;
	}

	/**
	 * Removes the data for a specific IDataProvider from the plot for the
	 * current feature.
	 * 
	 * @param index
	 *            The index of the cell in the assembly grid. This corresponds
	 *            to an IDataProvider.
	 */
	private void unplotData(int index) {
		if (index >= 0 && index < size * size) {
			// Compute the key, e.g., Pin F,7
			String key = getKey(index);

			// Update the bit in the feature's selected locations BitSet.
			BitSet selected = selectedLocations.get(feature);
			selected.clear(index);

			// Remove the trace with the specified key from the Map of traces
			// and the graph.
			Trace trace = traces.remove(key);
			if (trace != null) {
				xyGraph.removeTrace(trace);
			}
		}
		return;
	}

	/**
	 * Get a key for an assembly component based on its row and column.
	 * 
	 * @param row
	 *            The row of the component.
	 * @param col
	 *            The column of the component.
	 * @return A key string for the assembly component.
	 */
	protected String getKey(int index) {

		String key = null;

		// Make sure the index is valid.
		if (index >= 0 && index < size * size) {
			// Get the row and column.
			int row = index / size;
			int column = index % size;

			// Get a key.
			if (labelProvider != null) {
				key = labelProvider.getLabelFromColumn(column) + ":"
						+ labelProvider.getLabelFromRow(row);
			} else {
				key = Integer.toString(column) + ":" + Integer.toString(row);
			}
		}
		return key;
	}

	/**
	 * This method resets the graph and puts only the currently-selected data
	 * series for the current feature on the plot. <br>
	 * This method does not remove any data. To remove plotted data, loop over
	 * the selected indices in {@link #selectedLocations} and
	 * {@link #unplotData(int)} them.
	 */
	protected void resetGraph() {

		// Update the y-axis title.
		xyGraph.primaryYAxis.setTitle((feature != null ? feature
				: "No feature selected"));

		// ---- Remove all the traces. ---- //
		for (Trace trace : traces.values()) {
			xyGraph.removeTrace(trace);
			// We have to manally remove the trace from both axes because
			// xyGraph.removeTrace() does not do this automatically.
			xyGraph.primaryXAxis.removeTrace(trace);
			xyGraph.primaryYAxis.removeTrace(trace);
		}
		traces.clear();
		// -------------------------------- //

		// Reset the palette.
		palette.reset();

		// ---- Add traces for selected components for the feature. ---- //
		BitSet selected = selectedLocations.get(feature);
		if (selected != null) {
			for (int i = selected.nextSetBit(0); i >= 0; i = selected
					.nextSetBit(i + 1)) {
				plotData(i);
			}
		}

		// Force the graph to auto-scale.
		xyGraph.primaryXAxis.performAutoScale(true);
		xyGraph.primaryYAxis.performAutoScale(true);
		// ------------------------------------------------------------- //

		return;
	}

	/* ---- Implements IAnalysisView ---- */
	/**
	 * Fills out the parent Composite with information and widgets related to
	 * this particular IAnalysisView.
	 * 
	 * @param container
	 *            The Composite containing this IAnalysisView.
	 */
	@Override
	public void createViewContent(Composite container) {
		super.createViewContent(container);

		// Initialize the canvas, the LightweightSystem, and the XYGraph.
		canvas = new Canvas(container, SWT.DOUBLE_BUFFERED);
		lws = new LightweightSystem(canvas);

		// Set the rightClickMenu for any Composites that will need to use it.
		canvas.setMenu(actionMenuManager.createContextMenu(container));

		// All we have is the canvas, so set the container to a FillLayout.
		container.setLayout(new FillLayout());

		// ---- Create and configure the XYGraph. ---- //
		// Create the XYGraph.
		xyGraph = new XYGraph();
		lws.setContents(xyGraph);

		// Some customization of the XYGraph.
		xyGraph.setTitle(dataSource + " Data");

		// Sets the background color of the graph to the canvas's background.
		xyGraph.setBackgroundColor(container.getBackground());

		// Customize the X axis.
		xyGraph.primaryXAxis.setTitle("Axial Level");
		xyGraph.primaryXAxis.setRange(0, 1);
		xyGraph.primaryXAxis.setAutoScale(true);
		xyGraph.primaryXAxis.setAutoScaleThreshold(0.005);

		// Customize the Y axis.
		xyGraph.primaryYAxis.setTitle("No feature selected");
		xyGraph.primaryYAxis.setRange(0, 1);
		xyGraph.primaryYAxis.setAutoScale(true);
		xyGraph.primaryYAxis.setAutoScaleThreshold(0.005);
		// ------------------------------------------- //

		return;
	}

	/**
	 * Gets the name for this type of analysis view. This can be used for the
	 * display text of SWT widgets that reference this view.
	 * 
	 * @return The IAnalysisView's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets a brief description of this type of analysis view. This can be used
	 * for ToolTips for SWT widgets referencing this view.
	 * 
	 * @return The IAnalysisView's description.
	 */
	public String getDescription() {
		return description;
	}

	/* ---------------------------------- */

	/* ---- Implements IStateListener ---- */
	/**
	 * Registers any keys of interest with the current broker.
	 */
	@Override
	public void registerKeys() {

		String key = dataSource + "-lwr" + AssemblyType.Fuel.toString();
		LWRComponentInfo info = (LWRComponentInfo) broker.register(key, this);
		if (info != null) {
			setAssembly((FuelAssembly) info.lwrComponent);
		}

		return;
	}

	/**
	 * Unregisters any keys from the current broker.
	 */
	@Override
	public void unregisterKeys() {
		String key = dataSource + "-lwr" + AssemblyType.Fuel.toString();
		broker.unregister(key, this);

		return;
	}

	/**
	 * This is called by the broker when a key of interest has changed.
	 */
	@Override
	public void update(String key, Object value) {

		// A new fuel assembly was selected.
		LWRComponentInfo info = (LWRComponentInfo) value;
		if (info != null) {
			setAssembly((FuelAssembly) info.lwrComponent);
		}

		return;
	}
	/* ----------------------------------- */

}
