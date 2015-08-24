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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.client.widgets.reactoreditor.AnalysisView;
import org.eclipse.ice.client.widgets.reactoreditor.DataSource;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Cell.State;
import org.eclipse.ice.client.widgets.reactoreditor.grid.Grid;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditorInput;
import org.eclipse.ice.client.widgets.reactoreditor.grid.GridEditorTools;
import org.eclipse.ice.client.widgets.reactoreditor.grid.IGridListener;
import org.eclipse.ice.client.widgets.reactoreditor.sfr.properties.PropertySourceFactory;
import org.eclipse.ice.reactor.sfr.base.SFRComponent;
import org.eclipse.ice.reactor.sfr.core.AssemblyType;
import org.eclipse.ice.reactor.sfr.core.SFReactor;
import org.eclipse.ice.reactor.sfr.core.assembly.SFRAssembly;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * This class provides an IAnalysisView geared toward an {@linkplain SFReactor}
 * core. It displays a grid of hexagons, each of which represents a location for
 * a particular {@linkplain AssemblyType type} of {@linkplain SFRAssembly}.<br>
 * <br>
 * When an assembly of the current type is selected, this View should update the
 * StateBroker with the current selection. It should also feed the properties of
 * the last clicked assembly or the core itself to the ICE Properties View via
 * {@link AnalysisView#selectionProvider}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class CoreAnalysisView extends AnalysisView implements IGridListener {

	/**
	 * The name for this type of analysis view. This can be used for the display
	 * text of SWT widgets that reference this view.
	 */
	protected static final String name = "Core";
	/**
	 * A brief description of this type of analysis view. This can be used for
	 * ToolTips for SWT widgets referencing this view.
	 */
	protected static final String description = "A view of a sodium-cooled fast reactor core.";

	/* ---- GUI Components ---- */
	/**
	 * The grid model for the reactor core.
	 */
	private Grid grid;
	/**
	 * The GraphicalViewer used to display the grid for the reactor core.
	 */
	private GraphicalViewer viewer;
	/**
	 * The ActionTree that lets the user select the core to display its
	 * properties.
	 */
	private final ActionTree coreProperties;
	/* ------------------------ */

	/* ---- Current State ---- */
	/**
	 * The current SFReactor displayed in the View.
	 */
	private SFReactor reactor;
	/**
	 * The current type of assemblies displayed in Core View.
	 */
	private AssemblyType assemblyType = AssemblyType.Fuel;
	/**
	 * A Map of BitSets for each assembly type. The bits in the Sets correspond
	 * to the indexes in the grid. A set bit means there is an assembly of that
	 * type in that location.
	 */
	private final Map<AssemblyType, BitSet> assemblyLocations;
	/**
	 * A Map of currently-selected assemblies keyed on the assembly type.
	 */
	private final Map<AssemblyType, SFRComponentInfo> selectedAssemblies;

	/* ----------------------- */

	/**
	 * The default constructor.
	 * 
	 * @param dataSource
	 *            The data source associated with this view (input, reference,
	 *            comparison).
	 */
	public CoreAnalysisView(DataSource dataSource) {
		super(dataSource);

		// Initialize any final variables here.
		AssemblyType[] values = AssemblyType.values();
		selectedAssemblies = new HashMap<AssemblyType, SFRComponentInfo>(
				values.length);
		assemblyLocations = new HashMap<AssemblyType, BitSet>(values.length);

		// Add an ActionTree for the AssemblyTypes.
		ActionTree assemblyTypes = new ActionTree("Assembly Type");
		actions.add(assemblyTypes);
		for (final AssemblyType assemblyType : values) {
			Action action = new Action(assemblyType.toString()) {
				@Override
				public void run() {
					setAssemblyType(assemblyType);
				}
			};
			assemblyTypes.add(new ActionTree(action));
		}

		// Add an ActionTree (single button) for viewing the core's properties.
		coreProperties = new ActionTree(new Action("Core Properties") {
			@Override
			public void run() {
				// If the reactor is set, get is properties.
				IPropertySource properties = new PropertySourceFactory()
						.getPropertySource(reactor);

				// If it has properties, set the properties in the ICE
				// Properties View.
				if (properties != null) {
					selectionProvider.setSelection(new StructuredSelection(
							properties));
				}
			}
		});
		actions.add(coreProperties);
		// Disable the core properties button by default.
		coreProperties.setEnabled(false);

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
	 * This method compares the passed in reactor with the currently-selected
	 * reactor. If the two reactors are the same instance and share the same
	 * assemblies among the currently-selected assemblies of each type, then
	 * this method returns false. It also returns false if the new reactor is
	 * null.
	 * 
	 * @param newReactor
	 *            The new reactor to compare against the currently-selected one.
	 * @return True if the current reactor should be changed to the new one,
	 *         false otherwise.
	 */
	private boolean reactorChanged(SFReactor newReactor) {

		boolean changed = false;

		// We can't compare with a null reactor.
		if (newReactor != null) {
			if (newReactor == reactor) {

				// If we have no selected assemblies, then assume the reactor is
				// new.
				if (selectedAssemblies.isEmpty()) {
					changed = true;
				}
				// If we have selected assemblies, see if they are the same as
				// the corresponding assemblies in the new reactor.
				for (AssemblyType type : AssemblyType.values()) {
					// Get the currently selected assembly of this type.
					SFRComponentInfo info = selectedAssemblies.get(type);
					if (info != null) {
						// Get the corresponding assembly in the new reactor.
						SFRComponent assembly = reactor.getAssemblyByLocation(
								type, info.row, info.column);
						// Compare the two assemblies and break if necessary.
						if (assembly != info.sfrComponent) {
							changed = true;
							break;
						}
					}
				}

			} else {
				// In some cases, the reactor actually is a different instance.
				// This happens when a new type of reactor is loaded, since the
				// new reactor cannot copy itself into the old one.
				changed = true;
			}
		}

		return changed;
	}

	/**
	 * Sets the current reactor displayed in the CoreAnalysisView.
	 * 
	 * @param newReactor
	 *            The new SFReactor to display.
	 */
	private void setReactor(SFReactor newReactor) {

		// Don't try to reload anything unless the reactor is significantly
		// enough different to warrant a change. We isolate this functionality
		// because the typical procedure is to copy the data from a new
		// IReactorComponent into the existing one known to this AnalysisView.
		if (reactorChanged(newReactor)) {
			// Reset the Map of selected assemblies here (because it is used to
			// compare reactors).
			selectedAssemblies.clear();

			// Store the reference to the new reactor.
			reactor = newReactor;

			// Enable the core properties button.
			coreProperties.setEnabled(true);

			// Get the size of the reactor.
			int size = reactor.getSize();
			int totalSize = size * size;
			int radius = (int) Math.ceil(size / 2.0);

			/* ---- Build a new basic SFR core view. ---- */
			// Re-initialize the List of states and set everything to be
			// INVALID.
			// Invalid states do not produce a Figure on the grid.
			List<Cell.State> states = new ArrayList<Cell.State>(totalSize);
			for (int i = 0; i < totalSize; i++) {
				states.add(Cell.State.INVALID);
			}

			// Set the states of all valid positions to DISABLED. This will
			// create
			// a hexagon-shaped grid of hexagons.
			for (int i = 0; i < 6; i++) {
				for (Point cell : GridEditorTools.getSextant(radius, i, false)) {
					states.set(cell.y * size + cell.x, State.DISABLED);
				}
			}
			/* ------------------------------------------ */

			/* ---- Update the core view with the reactor's assemblies. ---- */
			for (AssemblyType type : AssemblyType.values()) {

				/* -- Build the BitSet for all assembly locations. -- */
				// This also sets the states of any Cells outside the basic core
				// to
				// DISABLED if there is an assembly of some type in those
				// locations.

				// Reset the BitSet for assembly locations for this type.
				BitSet locations = new BitSet(size * size);
				assemblyLocations.put(type, locations);

				// Set the bit for each location that has an assembly. Also, set
				// the
				// state for each location with an assembly in case it lies
				// outside
				// the basic SFR core.
				for (int row = 0; row < size; row++) {
					for (int column = 0; column < size; column++) {
						if (reactor.getAssemblyByLocation(type, row, column) != null) {
							int index = row * size + column;
							locations.set(index);
							states.set(index, State.DISABLED);
						}
					}
				}

			}

			/* -- Update the states with the current assembly type. -- */
			BitSet enabledLocations = assemblyLocations.get(assemblyType);
			for (int i = enabledLocations.nextSetBit(0); i >= 0; i = enabledLocations
					.nextSetBit(i + 1)) {
				states.set(i, State.UNSELECTED);
			}
			/* -- ------------------------------------------------- -- */

			/* ------------------------------------------------------------- */

			/* ---- Reset the GEF GraphicalViewer. ---- */

			// Re-initialize the grid model with the new data.
			GridEditorInput input = new GridEditorInput(size, size);
			input.setStates(states);

			grid = new Grid(input);

			// Set the viewer's contents.
			GridEditorTools.setViewerContents(viewer, grid);
			/* ---------------------------------------- */
		}

		return;
	}

	/**
	 * Sets the current type of assemblies displayed on the reactor core grid.
	 * 
	 * @param assemblyType
	 *            The new type of assembly to display.
	 */
	private void setAssemblyType(AssemblyType assemblyType) {
		// Check the parameters.
		if (assemblyType != this.assemblyType) {

			// Get the Cells in the Grid.
			List<Cell> cells = grid.getCells();

			// For the current assembly type, set all states to DISABLED.
			BitSet assemblies = assemblyLocations.get(this.assemblyType);
			for (int i = assemblies.nextSetBit(0); i >= 0; i = assemblies
					.nextSetBit(i + 1)) {
				cells.get(i).setState(State.DISABLED);
			}
			// Update the current assembly type.
			this.assemblyType = assemblyType;

			// For the new assembly type, set all states to UNSELECTED.
			assemblies = assemblyLocations.get(assemblyType);
			for (int i = assemblies.nextSetBit(0); i >= 0; i = assemblies
					.nextSetBit(i + 1)) {
				cells.get(i).setState(State.UNSELECTED);
			}
			// Set the state for the currently-selected assembly of this type.
			SFRComponentInfo info = selectedAssemblies.get(assemblyType);
			if (info != null) {
				cells.get(info.row * reactor.getSize() + info.column).setState(
						State.SELECTED);
			}
			// Reset the viewer.
			GridEditorTools.setViewerContents(viewer, grid);
		}

		return;
	}

	/**
	 * Sets the currently-highlighted assembly for a particular assembly type.
	 * 
	 * @param type
	 *            The type of assembly to which the selection applies.
	 * @param info
	 *            The SFRComponentInfo wrapping the assembly and its location in
	 *            the grid.
	 */
	private void setAssembly(AssemblyType assemblyType, SFRComponentInfo info) {
		// Check the parameters.
		if (assemblyType != null && info != null) {

			// Put the new assembly into the Map of selected assemblies.
			SFRComponentInfo oldInfo = selectedAssemblies.put(assemblyType,
					info);

			// See if the newly-selected assembly is actually new.
			// Apply necessary changes to show that the assembly has
			// changed.

			// Update the viewer's selection if necessary.
			if (!info.matches(oldInfo) && this.assemblyType == assemblyType) {

				// Get the Cells in the Grid.
				List<Cell> cells = grid.getCells();

				int size = reactor.getSize();
				int index;

				// Unselect the old location if possible.
				if (oldInfo != null) {
					index = oldInfo.row * size + oldInfo.column;
					cells.get(index).setState(State.UNSELECTED);
				}

				// Select the new location.
				index = info.row * size + info.column;
				Cell cell = cells.get(index);
				if (!cell.getSelected()) {
					cell.setState(State.SELECTED);

					// Refresh the viewer's selection.
					EditPart editPart = (EditPart) viewer.getEditPartRegistry()
							.get(cell);
					viewer.setSelection(new StructuredSelection(
							new Object[] { editPart }));
				}
			}

		}

		return;
	}

	/* ---- Implements IGridListener ---- */
	/**
	 * Sends an update to {@link AnalysisView#broker broker} when a cell has
	 * been selected.
	 */
	@Override
	public void selectCell(int index) {

		// Get the row and column from the cell's index.
		int row = index / reactor.getSize();
		int column = index % reactor.getSize();

		// Get the assembly at that location.
		SFRComponent assembly = reactor.getAssemblyByLocation(assemblyType,
				row, column);

		// If possible, select the component in that location.
		if (assembly != null) {
			SFRComponentInfo info = new SFRComponentInfo(row, column, assembly);

			// Send the new selection to the StateBroker.
			String key = dataSource + "-sfr" + assemblyType;
			broker.putValue(key, info);
		}

		return;
	}

	/**
	 * Sends an update to {@link AnalysisView#selectionProvider
	 * selectionProvider} when a cell has been clicked.
	 */
	@Override
	public void clickCell(int index) {

		// Get the row and column from the cell's index.
		int row = index / reactor.getSize();
		int column = index % reactor.getSize();

		// Get the assembly at that location.
		SFRComponent assembly = reactor.getAssemblyByLocation(assemblyType,
				row, column);

		// If possible, select the properties of the component in that location.
		if (assembly != null) {
			IPropertySource properties = new PropertySourceFactory()
					.getPropertySource(assembly);

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
	 * @param container
	 *            The Composite containing this IAnalysisView.
	 */
	@Override
	public void createViewContent(Composite container) {
		super.createViewContent(container);

		// Set the layout of the container to a FillLayout (we only have the
		// viewer, so it gets all the space).
		container.setLayout(new FillLayout());

		// Create the GEF viewer used to display the core map.
		viewer = GridEditorTools.createViewer(container,
				new CoreEditPartFactory(this));

		// Set the context Menu for the main view content.
		viewer.setContextMenu(actionMenuManager);

		return;
	}

	/**
	 * Gets the name for this type of analysis view. This can be used for the
	 * display text of SWT widgets that reference this view.
	 * 
	 * @return The IAnalysisView's name.
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * Gets a brief description of this type of analysis view. This can be used
	 * for ToolTips for SWT widgets referencing this view.
	 * 
	 * @return The IAnalysisView's description.
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/* ---- Implements IStateListener ---- */
	/**
	 * Registers any keys of interest with the current broker.
	 */
	@Override
	public void registerKeys() {
		String key;

		// Listen for the currently-selected reactor.
		key = dataSource + "-" + "SFReactor";
		setReactor((SFReactor) broker.register(key, this));

		// We want to listen for changes to currently-selected assemblies for
		// the data source.
		for (AssemblyType type : AssemblyType.values()) {
			key = dataSource + "-" + type;
			SFRComponentInfo info = (SFRComponentInfo) broker.register(key,
					this);

			// Select the assembly if there's already an assembly set.
			setAssembly(type, info);
		}

		return;
	}

	/**
	 * Unregisters any keys from the current broker.
	 */
	@Override
	public void unregisterKeys() {

		// Stop listening for the currently-selected reactor.
		broker.unregister(dataSource + "-" + "SFReactor", this);

		// Stop listening for changes to currently-selected assemblies for this
		// data source.
		for (AssemblyType type : AssemblyType.values()) {
			String key = dataSource + "-" + type.toString();
			broker.unregister(key, this);
		}
		return;
	}

	/**
	 * This is called by the broker when a key of interest has changed.
	 */
	@Override
	public void update(String key, Object value) {
		logger.info("CoreAnalysisView message: "
				+ "Receving key update for key \"" + key + "\"");

		// Split the key (the first part is the datasource and the second is the
		// useful part of the key).
		String[] split = key.split("-");

		// See if we need to update the reactor.
		if ("reactor".equals(split[1])) {
			setReactor((SFReactor) value);
		}
		// See which type of assembly was just selected.
		else {
			// Get the assembly type and the newly-selected assembly.
			AssemblyType assemblyType = AssemblyType.fromString(split[1]);

			// Select the assembly in this core view.
			setAssembly(assemblyType, (SFRComponentInfo) value);
		}

		return;
	}
	/* ----------------------------------- */

}
