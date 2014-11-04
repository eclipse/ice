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
package org.eclipse.ice.client.widgets.reactoreditor.sfr;

import org.eclipse.ice.analysistool.IData;
import org.eclipse.ice.analysistool.IDataProvider;
import org.eclipse.ice.client.common.ActionTree;

import org.eclipse.ice.client.widgets.reactoreditor.AnalysisView;
import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell.State;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Grid;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditorInput;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditorTools;
import org.eclipse.ice.client.widgets.reactoreditor.grid.IGridListener;
import org.eclipse.ice.client.widgets.reactoreditor.sfr.PinAnalysisView.AxialLevelWidget;
import org.eclipse.ice.client.widgets.reactoreditor.sfr.PinFigure.DisplayType;
import org.eclipse.ice.client.widgets.reactoreditor.sfr.properties.PropertySourceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.AssemblyType;
import org.eclipse.ice.reactor.sfr.core.assembly.PinAssembly;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRPin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * This class provides an IAnalysisView geared toward a {@linkplain PinAssembly}
 * . It displays a grid of hexagons, each of which represents a location for a
 * particular {@linkplain SFRPin}. The hexagons are rotated 90 degrees compared
 * to the CoreAnalysisView.<br>
 * <br>
 * When a pin selected, this View should update the StateBroker with the current
 * selection. It should also feed the properties of the last clicked pin or the
 * assembly itself to the ICE Properties View via
 * {@link AnalysisView#selectionProvider}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class PinAssemblyAnalysisView extends AnalysisView implements
		IGridListener {

	/**
	 * The name for this type of analysis view. This can be used for the display
	 * text of SWT widgets that reference this view.
	 */
	protected static final String name = "Pin Assembly";
	/**
	 * A brief description of this type of analysis view. This can be used for
	 * ToolTips for SWT widgets referencing this view.
	 */
	protected static final String description = "A view of a sodium-cooled fast reactor pin assembly.";

	/* ---- GUI Components ---- */
	/**
	 * The SashForm contains the radial canvas (left) and the axial level
	 * selectors (right).
	 */
	private SashForm sashForm;

	/**
	 * The grid model for the reactor core.
	 */
	private Grid grid;
	/**
	 * The GraphicalViewer used to display the grid for the reactor core.
	 */
	private GraphicalViewer viewer;

	/**
	 * featureLevelComposite contains the widgets used to select the axial
	 * level. This includes a Scale, a Spinner, and a Figure.
	 */
	private Composite axialComposite;
	/**
	 * The axial rod canvas used for axial level selection.
	 */
	private Canvas axialCanvas;
	/**
	 * This is the axial view Figure drawn on the right.
	 */
	private AxialPinFigure axialFigure;
	/**
	 * The Scale used for axial level selection.
	 */
	private Scale axialScale;
	/**
	 * The Spinner used for axial level selection.
	 */
	private Spinner axialSpinner;

	/**
	 * The ActionTree that lets the user select from the available data
	 * features.
	 */
	private final ActionTree featureTree;
	/**
	 * The ActionTree that lets the user select the assembly to display its
	 * properties.
	 */
	private final ActionTree assemblyProperties;
	/* ------------------------ */

	/* ----- Current State ----- */
	/**
	 * The currently selected PinAssembly.
	 */
	private PinAssembly assembly;

	/**
	 * A List of the PinFigures for each location.
	 */
	private final List<PinFigure> figures;
	/**
	 * A List of the SFRComponents for each location.
	 */
	private final List<SFRComponent> assemblyLocations;
	/**
	 * A List of the IDataProviders for each location.
	 */
	private final List<IDataProvider> assemblyData;
	/**
	 * A list of *all* cells in the assembly grid.
	 */
	private final List<State> assemblyCellStates;

	/**
	 * The current display type for the PinAssembly graphics.
	 */
	private DisplayType displayType;

	/**
	 * The current symmetry type for the PinAssembly graphics.
	 */
	private Symmetry symmetry;
	/**
	 * The index of the displayed portion of the PinAssembly graphics.
	 */
	private int symmetryIndex = -1;

	/**
	 * A Map of the number of axial levels per feature.
	 */
	private final Map<String, Integer> featureMap;
	/**
	 * The current feature for which data is displayed in each pin's location.
	 */
	private String feature;
	/**
	 * The current level of the data displayed in each pin's location.
	 */
	private int axialLevel;
	/**
	 * The maximum value (0-indexed) for the axial level.
	 */
	private int maxAxialLevel;

	/**
	 * The current type of extrema used for the PinFigure data points.
	 */
	private Extrema extrema;
	/**
	 * The minimum value for PinFigure data points.
	 */
	private Double customMinValue;
	/**
	 * The maximum value for PinFigure data points.
	 */
	private Double customMaxValue;

	/**
	 * The largest radius across each of the pins.
	 */
	private double maxRadius;

	/* ------------------------- */

	/**
	 * An enum describing the allowed symmetries for the assembly view.
	 * 
	 * @author Jordan
	 * 
	 */
	public enum Symmetry {
		/**
		 * A full view of the assembly.
		 */
		FULL("Full-core", 1),
		/**
		 * A view of one-quarter of the assembly.
		 */
		SEXTANT("Sextant", 6);

		/**
		 * The user-friendly name of the symmetry type.
		 */
		public final String name;

		/**
		 * The number of possible sections for this symmetry.
		 */
		public final int sections;

		/**
		 * The default constructor.
		 * 
		 * @param name
		 *            The user-friendly name of the symmetry type.
		 * @param sections
		 *            The number of possible sections for this symmetry.
		 */
		private Symmetry(String name, int sections) {
			this.name = name;
			this.sections = sections;
		}
	}

	/**
	 * This enum provides the acceptable options for selecting the extrema. The
	 * extrema determine the colors of the state point data when the display
	 * type is {@link DisplayType#DATA}.
	 * 
	 * @author Jordan
	 * 
	 */
	private enum Extrema {
		/**
		 * Each component computes its own min and max.
		 */
		PIECEWISE("Piecewise (each component calculates its own extrema)"),
		/**
		 * The min and max among the current feature data in the current axial
		 * level.
		 */
		LOCAL("Local (current axial level)"),
		/**
		 * The global min and max among all axial levels for the current feature
		 * data.
		 */
		GLOBAL("Global (all axial levels)");
		/**
		 * The user defines the min and max values.
		 */
		// TODO Add this and widgets to change it!
		// CUSTOM("User-defined");?

		/**
		 * The user-friendly name of the extrema type.
		 */
		public final String name;

		/**
		 * The default private constructor.
		 * 
		 * @param name
		 *            The user-friendly name of the extrema type.
		 */
		private Extrema(String name) {
			this.name = name;
		}
	}

	/**
	 * The default constructor.
	 * 
	 * @param dataSource
	 *            The data source associated with this view (input, reference,
	 *            comparison).
	 */
	public PinAssemblyAnalysisView(DataSource dataSource) {
		super(dataSource);

		// Initialize the final lists, maps, etc.
		figures = new ArrayList<PinFigure>();
		assemblyLocations = new ArrayList<SFRComponent>();
		assemblyData = new ArrayList<IDataProvider>();
		featureMap = new HashMap<String, Integer>();
		assemblyCellStates = new ArrayList<State>();

		// Set defaults here.
		displayType = DisplayType.GEOMETRY;
		symmetry = Symmetry.FULL;
		symmetryIndex = 0;
		feature = null;
		axialLevel = 0;
		maxAxialLevel = 0;
		extrema = Extrema.PIECEWISE;
		customMinValue = null;
		customMaxValue = null;
		maxRadius = 0.0;

		// Populate the list of actions (for ToolBar and context Menu).

		// Add an ActionTree for the DisplayTypes.
		ActionTree displayTypes = new ActionTree("Display Type");
		actions.add(displayTypes);
		for (final DisplayType displayType : DisplayType.values()) {
			Action action = new Action(displayType.name) {
				@Override
				public void run() {
					setDisplayType(displayType);
				}
			};
			displayTypes.add(new ActionTree(action));
		}

		// FIXME The column and row indices in the grid may be off depending on
		// the selected sextant. For now, I'm restricting it to a full-core
		// view.
		// // Add an ActionTree for the symmetry types.
		// ActionTree symmetries = new ActionTree("Symmetry");
		// actions.add(symmetries);
		// for (final Symmetry symmetry : Symmetry.values()) {
		// // Create a sub-menu for the symmetry type.
		// ActionTree symmetryMenu = new ActionTree(symmetry.name);
		// symmetries.add(symmetryMenu);
		// // Create menu items for each possible section for the symmetry.
		// for (int i = 0; i < symmetry.sections; i++) {
		// final int index = i;
		// Action action = new Action(Integer.toString(i + 1)) {
		// @Override
		// public void run() {
		// setSymmetry(symmetry, index);
		// }
		// };
		// symmetryMenu.add(new ActionTree(action));
		// }
		// }

		// Add an ActionTree for data feature.
		featureTree = new ActionTree("Data Feature");
		actions.add(featureTree);

		// Add an ActionTree for data extrema.
		ActionTree dataExtrema = new ActionTree("Data Extrema");
		actions.add(dataExtrema);
		for (final Extrema extrema : Extrema.values()) {
			Action action = new Action(extrema.name) {
				@Override
				public void run() {
					setExtrema(extrema);
				}
			};
			dataExtrema.add(new ActionTree(action));
		}

		// Add an ActionTree (single button) for viewing the core's properties.
		assemblyProperties = new ActionTree(new Action("Assembly Properties") {
			@Override
			public void run() {
				// If the reactor is set, get is properties.
				IPropertySource properties = new PropertySourceFactory()
						.getPropertySource(assembly);

				// If it has properties, set the properties in the ICE
				// Properties View.
				if (properties != null) {
					selectionProvider.setSelection(new StructuredSelection(
							properties));
				}
			}
		});
		actions.add(assemblyProperties);
		// Disable the core properties button by default.
		assemblyProperties.setEnabled(false);

		// Add an ActionTree (single button) for saving the viewer image.
		ActionTree saveImage = new ActionTree(new Action("Save Image") {
			@Override
			public void run() {
				saveViewerImage(viewer);
			}
		});
		actions.add(saveImage);

		return;
	}

	/**
	 * Set the PinAssembly displayed by this AVC.
	 * 
	 * @param assembly
	 *            The new fuel assembly object.
	 */
	private void setAssembly(PinAssembly assembly) {
		// Check the parameters.
		if (assembly != null && assembly != this.assembly) {

			// Set the new assembly.
			this.assembly = assembly;

			// Enable the assembly properties button.
			assemblyProperties.setEnabled(true);

			// Get the size of the assembly.
			int size = assembly.getSize();
			int rows = size;
			int columns = size;
			int totalSize = rows * columns;

			/* ---- Build a basic SFR assembly view. ---- */
			// Create a list of cell states and set them to be disabled.
			assemblyCellStates.clear();
			for (int i = 0; i < totalSize; i++) {
				assemblyCellStates.add(Cell.State.DISABLED);
			}
			/* ------------------------------------------ */

			/* ---- Update the viewer with pins/rods from the assembly. ---- */
			// Reset the lists of components and data providers, and clear the
			// map of available features.
			assemblyLocations.clear();
			assemblyData.clear();
			featureMap.clear();
			// Reset the maxRadius. This is used to determine how big each
			// PinFigure should be drawn.
			maxRadius = 0.0;

			int index = 0;
			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++, index++) {
					// Try to get a component and data provider at the location.
					SFRComponent sfrComp = assembly.getPinByLocation(row,
							column);
					IDataProvider sfrData = assembly.getDataProviderByLocation(
							row, column);
					if (sfrComp != null) {
						// Convert the component to a rod.
						SFRPin pin = (SFRPin) sfrComp;

						// Try to update the max radius.
						maxRadius = Math.max(maxRadius, pin.getCladding()
								.getOuterRadius());

						// Update the state.
						assemblyCellStates.set(index, State.UNSELECTED);
					}
					if (sfrData != null) {
						// Get the features available here. For each feature,
						// get the maximum number of levels supported.
						for (String feature : sfrData.getFeatureList()) {
							int newCount = sfrData
									.getDataAtCurrentTime(feature).size();
							if (!featureMap.containsKey(feature)
									|| newCount > featureMap.get(feature)) {
								featureMap.put(feature, newCount);
							}
						}
					} else {
						// Create an empty data provider instead of putting a
						// null value in the list.
						sfrData = new SFRComponent();
					}
					// Add the rod and its data provider to the lists.
					assemblyLocations.add(sfrComp);
					assemblyData.add(sfrData);
				}
			}
			/* ------------------------------------------------------------- */

			/* ---- Update the feature Menus. ---- */
			// Clear out the old features.
			featureTree.removeAll();

			// Add Actions for setting the features to the feature ActionTree.
			for (final String feature : featureMap.keySet()) {
				Action action = new Action(feature) {
					@Override
					public void run() {
						setFeature(feature);
					}
				};
				featureTree.add(new ActionTree(action));
			}

			// Select the first feature if possible.
			if (featureMap.isEmpty()) {
				feature = null;
			} else {
				feature = featureMap.keySet().iterator().next();
			}
			// Refresh the Menus.
			updateActionManagers();
			/* ----------------------------------- */

			/* ---- Reset the feature level scales, sliders, etc. ---- */
			boolean enabled = false;
			int maximum = 0;
			if (feature != null) {
				maximum = featureMap.get(feature) - 1;
				if (maximum > 1) {
					enabled = true;
				} else {
					maximum = 0;
				}
			}

			axialScale.setEnabled(enabled);
			axialScale.setMaximum(maximum);
			axialScale.setSelection(axialScale.getMaximum());

			axialSpinner.setEnabled(enabled);
			axialSpinner.setMaximum(maximum);
			axialSpinner.setSelection(0);
			/* ------------------------------------------------------- */

			// Reset the graphical viewer containing the assembly grid.
			resetViewer();
		}

		return;
	}

	/**
	 * Sets the component displayed in the axial view on the right.
	 * 
	 * @param component
	 *            The currently selected assembly component.
	 */
	private void setComponent(SFRComponent component) {
		// Pass on the component to the axial figure.
		axialFigure.setComponent(component);
	}

	/**
	 * Sets the data provider used to display data in the axial view on the
	 * right.
	 * 
	 * @param dataProvider
	 *            The data provider for the currently selected assembly
	 *            component.
	 */
	private void setDataProvider(IDataProvider dataProvider) {
		// Pass on the data provider to the axial figure.
		axialFigure.setDataProvider(dataProvider);
	}

	/**
	 * Set the display type used for the fuel assembly components.
	 * 
	 * @param displayType
	 *            The new display type, e.g., geometry or data.
	 */
	private void setDisplayType(DisplayType displayType) {
		if (displayType != null && displayType != this.displayType) {
			// Update the stored display type.
			this.displayType = displayType;

			// Set the display type for all of the figures in the assembly map.
			for (PinFigure figure : figures) {
				figure.setDisplayType(displayType);
			}

			// Set the display type for the axial view.
			axialFigure.setDisplayType(displayType);
		}
		return;
	}

	/**
	 * Set the symmetry used for displaying the fuel assembly components.
	 * 
	 * @param symmetry
	 *            The new symmetry, e.g., octant, quadrant, or full.
	 */
	private void setSymmetry(Symmetry symmetry, int symmetryIndex) {
		// Make sure the symmetry value is not null *and* different.
		// Also validate the symmetryIndex by modulo dividing it with the
		// number of sections for the symmetry type.
		if (symmetry != null
				&& (symmetry != this.symmetry || (symmetryIndex %= symmetry.sections) != this.symmetryIndex)) {

			// Update the symmetry values.
			this.symmetry = symmetry;
			this.symmetryIndex = symmetryIndex;

			// Reset the graphical viewer. The grid has to change!
			resetViewer();
		}
		return;
	}

	/**
	 * This function should be activated when a new feature data is selected.
	 * 
	 * @param featureName
	 *            The new feature to select.
	 */
	private void setFeature(String feature) {
		if (feature != null && feature != this.feature) {
			// Get the number of axial levels.
			Integer count = featureMap.get(feature);
			if (count != null) {
				// Update the feature value.
				this.feature = feature;

				// Update the maximum axial level.
				maxAxialLevel = count - 1;

				// If necessary, reduce the current axial level.
				if (maxAxialLevel < axialLevel) {
					setAxialLevel(maxAxialLevel, AxialLevelWidget.NONE);
				}
				// Enable and configure the scale and spinner depending on the
				// maximum axial level.
				if (maxAxialLevel > 0) {
					axialScale.setEnabled(true);
					axialSpinner.setEnabled(true);
				} else {
					axialScale.setEnabled(false);
					axialSpinner.setEnabled(false);
				}
				axialScale.setMaximum(maxAxialLevel + 1);
				axialSpinner.setMaximum(maxAxialLevel + 1);

				// Pass on the new feature to the figures.
				for (PinFigure figure : figures) {
					figure.setFeature(feature);
				}
				axialFigure.setFeature(feature);

				// If the feature changes, we may also need to update the
				// extrema!
				updateExtrema();
			}
		}
		return;
	}

	/**
	 * This function should be called by the Listeners for the Scale/Spinner.
	 * 
	 * @param axialLevel
	 *            The new axial level.
	 */
	private void setAxialLevel(int axialLevel, AxialLevelWidget source) {
		// Don't do anything if it has not actually changed.
		if (axialLevel != this.axialLevel) {
			// Update the stored axial level.
			this.axialLevel = axialLevel;

			// Update the axial scale and spinner. To smooth the process a bit,
			// we know the source widget that is changing the axial level. Do
			// not send an update to it!
			if (source == AxialLevelWidget.SCALE) {
				axialSpinner.setSelection(axialLevel + 1);
			} else if (source == AxialLevelWidget.SPINNER) {
				axialScale.setSelection(maxAxialLevel - axialLevel + 1);
			} else {
				axialScale.setSelection(maxAxialLevel - axialLevel + 1);
				axialSpinner.setSelection(axialLevel + 1);
			}

			// Pass the new axial level to the figures.
			for (PinFigure figure : figures) {
				figure.setAxialLevel(axialLevel);
			}
			axialFigure.setAxialLevel(axialLevel);

			// If necessary, update the extrema. They should change for each
			// axial level!
			if (extrema == Extrema.LOCAL) {
				updateExtrema();
			}
		}
		return;
	}

	/**
	 * Sets the current type of extrema used to display the state point data.
	 * 
	 * @param extrema
	 *            The new extrema type to use when displaying
	 */
	private void setExtrema(Extrema extrema) {
		if (extrema != null && extrema != this.extrema) {
			// Update the current extrema type.
			this.extrema = extrema;

			boolean custom = true;
			if (extrema == Extrema.PIECEWISE) {
				custom = false;
			} else {

				// Update the extrema values.
				updateExtrema();
			}

			// Set the custom flag for each of the figures.
			for (PinFigure figure : figures) {
				figure.useCustomExtrema(custom);
			}
			axialFigure.useCustomExtrema(custom);
		}
		return;
	}

	/**
	 * Recomputes the extrema based on the current extrema type and sends any
	 * updates to all component figures.
	 */
	private void updateExtrema() {

		// Set the initial temporary min and max.
		double min = Double.MAX_VALUE;
		double max = Double.MIN_VALUE;

		// For local extrema, find the min and max for the current axial level.
		if (extrema == Extrema.LOCAL) {
			for (IDataProvider dataProvider : assemblyData) {
				ArrayList<IData> iDataList = dataProvider
						.getDataAtCurrentTime(feature);
				if (axialLevel < iDataList.size()) {
					double value = iDataList.get(axialLevel).getValue();
					min = Math.min(min, value);
					max = Math.max(max, value);
				}
			}
		}
		// For global extrema, find the min and max for ALL axial levels.
		else if (extrema == Extrema.GLOBAL) {
			for (IDataProvider dataProvider : assemblyData) {
				for (IData iData : dataProvider.getDataAtCurrentTime(feature)) {
					double value = iData.getValue();
					min = Math.min(min, value);
					max = Math.max(max, value);
				}
			}
		}
		// For piecewise, each component computes its own max and min.
		// For custom extrema, there's nothing to recompute.

		// If the temporary min (or max) has changed, then we'll also need to
		// send these updates to the component figures.
		if (min != Double.MAX_VALUE) {
			customMinValue = min;
			customMaxValue = max;

			for (PinFigure figure : figures) {
				figure.setMinValue(customMinValue);
				figure.setMaxValue(customMaxValue);
			}
			axialFigure.setMinValue(customMinValue);
			axialFigure.setMaxValue(customMaxValue);
		}

		return;
	}

	/**
	 * Resets the entire graphical viewer with the current settings.
	 */
	private void resetViewer() {

		// Get the size of the assembly.
		int size = assembly.getSize();
		int rows = size;
		int columns = size;
		int fullColumns = size;

		List<State> states;
		TreeSet<Integer> validLocations = new TreeSet<Integer>();

		/* ---- Determine the cell locations that will be visible. ---- */
		int rowMin = size - 1;
		int rowMax = 0;
		int colMin = rowMin;
		int colMax = rowMax;

		if (symmetry == Symmetry.FULL) {
			for (int row = 0; row < rows; row++) {
				for (int column = 0; column < columns; column++) {
					validLocations.add(row * columns + column);
				}
			}
			states = assemblyCellStates;
			rowMin = 0;
			rowMax = rows - 1;
			colMin = 0;
			colMax = columns - 1;
		} else {
			List<Point> locations;
			locations = GridEditorTools.getSextant((int) Math.ceil(size / 2.0),
					symmetryIndex, true);

			// FIXME getSextant works fine, but it's possible that the parts
			// aren't displayed properly. For instance, the center cell might
			// not be in one of the "high" columns, but selecting sextant 6 will
			// always make it be in a "high" column.

			for (Point p : locations) {
				if (p.y < rowMin) {
					rowMin = p.y;
				}
				if (p.y > rowMax) {
					rowMax = p.y;
				}
				if (p.x < colMin) {
					colMin = p.x;
				}
				if (p.x > colMax) {
					colMax = p.x;
				}
				validLocations.add(p.y * fullColumns + p.x);
			}

			size = Math.max(rowMax - rowMin + 1, colMax - colMin + 1);
			rows = size;
			columns = size;

			states = new ArrayList<State>(rows * columns);

			for (int row = 0; row < rows; row++) {
				for (int col = 0; col < columns; col++) {
					int fullIndex = (row + rowMin) * fullColumns + col + colMin;

					Cell.State state = assemblyCellStates.get(fullIndex);
					if (!validLocations.contains(fullIndex)) {
						state = State.INVALID;
					}
					states.add(state);
				}
			}

		}
		/* ------------------------------------------------------------ */

		/* ---- Reset the GEF GraphicalViewer. ---- */
		// Re-initialize the grid model with the new data.
		GridEditorInput input = new GridEditorInput(rows, columns);
		input.setStates(states);

		// Turn off row and column labels.
		input.showColumnLabels = false;
		input.showRowLabels = false;

		// Turn off labels for the individual cells (rods).
		input.showCellLabels = false;

		grid = new Grid(input);

		// Set the viewer's contents.
		GridEditorTools.setViewerContents(viewer, grid);
		/* ---------------------------------------- */

		/* ---- Update all of the EditParts with the Rod info. ---- */
		Map registry = viewer.getEditPartRegistry();
		List<Cell> cells = grid.getCells();

		figures.clear();

		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < columns; col++) {
				int fullIndex = (row + rowMin) * fullColumns + col + colMin;
				int gridIndex = row * columns + col;

				SFRComponent sfrComp;
				if (validLocations.contains(fullIndex)
						&& (sfrComp = assemblyLocations.get(fullIndex)) != null) {

					PinAssemblyCellEditPart editPart = (PinAssemblyCellEditPart) registry
							.get(cells.get(gridIndex));

					PinFigure figure = (PinFigure) editPart.getFigure();
					figures.add(figure);
					figure.setComponent(sfrComp, maxRadius);
					figure.setDataProvider(assemblyData.get(fullIndex));
					figure.setFeature(feature);
					figure.setAxialLevel(axialLevel);
					figure.setDisplayType(displayType);
					figure.setMinValue(customMinValue);
					figure.setMaxValue(customMaxValue);
					figure.useCustomExtrema(extrema != Extrema.PIECEWISE);
				}
			}
		}
		/* -------------------------------------------------------- */

		return;
	}

	/* ---- Implements IGridListener ---- */
	/**
	 * Sends an update to {@link AnalysisView#broker broker} when a cell has
	 * been selected.
	 */
	public void selectCell(int index) {
		// FIXME for quadrant and octant symmetries.

		// Get the row and column from the cell's index.
		int row = index / assembly.getSize();
		int column = index % assembly.getSize();

		// Get the component at that location.
		SFRComponent component = assemblyLocations.get(index);
		IDataProvider dataProvider = assemblyData.get(index);

		// TODO We may need to register with the broker to listen for the
		// currently-selected sub-assembly component. Let's try not syncing this
		// for now!
		setComponent(component);
		setDataProvider(dataProvider);
		// Make sure the axial figure's properties are up-to-date!
		axialFigure.setFeature(feature);
		axialFigure.setAxialLevel(axialLevel);
		axialFigure.setDisplayType(displayType);
		axialFigure.setMinValue(customMinValue);
		axialFigure.setMaxValue(customMaxValue);
		axialFigure.useCustomExtrema(extrema != Extrema.PIECEWISE);

		// If possible, select the component in that location.
		if (component != null) {
			SFRComponentInfo info = new SFRComponentInfo(row, column,
					component, dataProvider);

			// Send the new selection to the StateBroker.
			String key = dataSource + "-" + "pin";
			broker.putValue(key, info);
		}

		return;
	}

	/**
	 * Sends an update to {@link AnalysisView#selectionProvider
	 * selectionProvider} when a cell has been clicked.
	 */
	public void clickCell(int index) {

		// Get the component at that location.
		SFRComponent component = assemblyLocations.get(index);

		// If possible, select the properties of the component in that location.
		if (component != null) {
			IPropertySource properties = new PropertySourceFactory()
					.getPropertySource(component);

			// Send the new selection to the SelectionProvider.
			if (properties != null) {
				selectionProvider.setSelection(new StructuredSelection(
						properties));
			}
		}

		return;
	}

	/* ---------------------------------- */

	/* ---- Implements IAnalysisView ---- */
	/**
	 * Fills out the parent Composite with information and widgets related to
	 * this particular IAnalysisView.
	 * 
	 * @param parent
	 *            The Composite containing this IAnalysisView.
	 */
	@Override
	public void createViewContent(Composite container) {
		super.createViewContent(container);

		// A single SashForm composes this AVC. So, use a FillLayout on top.
		container.setLayout(new FillLayout());

		/* ---- Create the SashForm containing the assembly/axial views. ---- */
		// Create the SashForm and its components.
		sashForm = new SashForm(container, SWT.HORIZONTAL | SWT.SMOOTH);
		viewer = GridEditorTools.createViewer(sashForm,
				new PinAssemblyEditPartFactory(this));
		axialComposite = new Composite(sashForm, SWT.NONE);

		// The left side of the SashForm is the radial view of the Rod. The
		// right side is the axial view + widgets for selecting the axial view.
		// Give the SashForm a 3:1 size ratio (radial:axial).
		sashForm.setWeights(new int[] { 3, 1 });
		/* ------------------------------------------------------------------ */

		/* ---- Create the assembly map. ---- */
		// Nothing to do here... the viewer is already created!
		/* ---------------------------------- */

		/* ---- Create the axial view. ---- */
		// Create the featureLevelComposite and its components.
		axialScale = new Scale(axialComposite, SWT.VERTICAL);
		axialCanvas = new Canvas(axialComposite, SWT.DOUBLE_BUFFERED);
		axialSpinner = new Spinner(axialComposite, SWT.NONE);

		// Disable the scale and spinner.
		axialScale.setEnabled(false);
		axialSpinner.setEnabled(false);

		// Set the increment for the scale.
		axialScale.setIncrement(1);

		// Set the initial values for the scale and spinner to "zero".
		axialScale.setMinimum(1);
		axialScale.setSelection(axialScale.getMaximum());
		axialSpinner.setMinimum(1);
		axialSpinner.setMinimum(1);
		axialSpinner.setSelection(1);

		// Create the axial figure in the axial canvas.
		// TODO Implement AxialPinFigure
		axialFigure = new AxialPinFigure();
		LightweightSystem lws = new LightweightSystem(axialCanvas);
		lws.setContents(axialFigure);

		// Add SelectionListeners to the Scale and Spinner.
		axialScale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Scale scale = (Scale) e.widget;
				setAxialLevel(scale.getMaximum() - scale.getSelection(),
						AxialLevelWidget.SCALE);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		axialSpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Spinner spinner = (Spinner) e.widget;
				setAxialLevel(spinner.getSelection() - 1,
						AxialLevelWidget.SPINNER);
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		// Give the axial view widgets a 2-column GridLayout.
		axialComposite.setLayout(new GridLayout(2, false));
		// The Scale (1st cell) should grab excess vertical space.
		axialScale.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		// The Canvas (2nd cell) should get all excess space.
		axialCanvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		// The Spinner (3rd cell) will go directly underneath the scale. No
		// code needed.
		/* -------------------------------- */

		/* ---- Configure any Menus for the container. ---- */
		// Set the context Menu for any Composites that will need to use it.
		viewer.setContextMenu(actionMenuManager);
		axialCanvas.setMenu(actionMenuManager.createContextMenu(container));
		/* ------------------------------------------------ */

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
		// We want to listen for updates to the currently-selected fuel
		// assembly.
		String key = dataSource + "-sfr" + AssemblyType.Fuel;
		SFRComponentInfo info = (SFRComponentInfo) broker.register(key, this);
		if (info != null) {
			setAssembly((PinAssembly) info.sfrComponent);
		}
	}

	/**
	 * Unregisters any keys from the current broker.
	 */
	@Override
	public void unregisterKeys() {
		broker.unregister(dataSource + "-sfr" + AssemblyType.Fuel, this);
	}

	/**
	 * This is called by the broker when a key of interest has changed.
	 */
	@Override
	public void update(String key, Object value) {

		// A new fuel assembly was selected.
		SFRComponentInfo info = (SFRComponentInfo) value;
		if (info != null) {
			setAssembly((PinAssembly) info.sfrComponent);
		}
	}
	/* ----------------------------------- */
}
