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
package org.eclipse.ice.client.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.ToolBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@code PlotGridComposite} is designed to display a grid of drawn
 * {@link IPlot}s. It includes widgets to customize the grid-based layout of the
 * plots. The order of a plot in the grid is based on its add order, and the
 * same plot can be added to the grid more than once.
 *
 * @author Jordan Deyton
 *
 */
public class PlotGridComposite extends Composite {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(PlotGridComposite.class);

	/**
	 * The {@code ToolBar} that contains widgets to update the grid layout and
	 * clear the grid.
	 */
	private final ToolBar toolBar;

	/**
	 * The grid of drawn plots.
	 */
	private final Composite gridComposite;
	/**
	 * The user-customizable layout for {@link #gridComposite}, the grid of
	 * drawn plots.
	 */
	private final GridLayout gridLayout;
	/**
	 * The number of rows to display in the grid.
	 */
	private int rows = 2;
	/**
	 * The number of columns to display in the grid.
	 */
	private int columns = 2;

	/**
	 * This is a button used to close the drawn plot over which the mouse is
	 * currently hovering. It should only be visible in the top-right corner of
	 * a drawn plot when the mouse cursor is over the plot.
	 */
	private Button closeButton;
	/**
	 * The listener responsible for showing and hiding the {@link #closeButton}.
	 */
	private final Listener plotHoverListener;

	/**
	 * The list of currently drawn plots. This list is used to maintain the
	 * insertion order. A nested class, {@link DrawnPlot}, is used to contain
	 * meta-data about each drawn plot or cell in the grid.
	 */
	private final List<DrawnPlot> drawnPlots;
	/**
	 * A map containing the currently drawn plots and their indices in
	 * {@link #drawnPlots}. This is used for relatively fast removal based on a
	 * reference to a drawn plot.
	 */
	private final Map<DrawnPlot, Integer> drawnPlotMap;

	/**
	 * A dispose listener that will remove the disposed {@link DrawnPlot}
	 * completely.
	 */
	private final DisposeListener plotDisposeListener = new DisposeListener() {
		@Override
		public void widgetDisposed(DisposeEvent e) {
			removePlot((DrawnPlot) e.widget, true);
		}
	};

	/**
	 * The default constructor. Creates a {@code Composite} designed to display
	 * a grid of {@code Composites} populated by {@code IPlot} implementations.
	 *
	 * @param parent
	 *            A widget that will be the parent of the new instance (cannot
	 *            be null).
	 * @param style
	 *            The style of widget to construct.
	 */
	public PlotGridComposite(Composite parent, int style) {
		super(parent, style);

		// Set the initial background. This is so the ToolBar's widgets will all
		// have the correct background when they are created.
		super.setBackground(parent.getBackground());

		// Initialize the list of drawn plots.
		drawnPlots = new ArrayList<DrawnPlot>();
		drawnPlotMap = new HashMap<DrawnPlot, Integer>();

		// Set up the ToolBar.
		toolBar = createToolBar(this);

		// Set up the Composite containing the grid of plots.
		gridComposite = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = true;
		gridComposite.setLayout(gridLayout);

		// Lay out this Composite. The ToolBar should be across the top, while
		// the Composite containing the plot grid should grab all available
		// space.
		GridData gridData;
		setLayout(new GridLayout());
		gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		toolBar.setLayoutData(gridData);
		gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridComposite.setLayoutData(gridData);

		// Create the listener that creates the close button when the mouse
		// enters a plot Composite (in the grid).
		plotHoverListener = new Listener() {

			/**
			 * The most recent plot that was entered, or null.
			 */
			private DrawnPlot lastPlot;

			@Override
			public void handleEvent(Event event) {
				// We only deal with Composites here.
				if (event.widget instanceof Composite) {
					// Determine the plot Composite that the mouse entered.
					Composite child = (Composite) event.widget;
					DrawnPlot plot = findDrawnPlot(child);

					// If the mouse entered a new plot Composite (or exited
					// one), then a change occurred and requires an update to
					// the close button.
					if (plot != lastPlot) {
						// If necessary, dispose the old close button.
						if (closeButton != null) {
							closeButton.dispose();
						}
						// If a plot Composite (or one of its children) was
						// entered, create a new close button in it.
						if (plot != null) {
							closeButton = plot.createCloseButton();
							// When the button is disposed, its reference should
							// be cleared.
							closeButton
									.addDisposeListener(new DisposeListener() {
								@Override
								public void widgetDisposed(DisposeEvent e) {
									closeButton = null;
								}
							});
						}
						// Set the reference to the most recently entered plot.
						lastPlot = plot;
					}

				}
				return;
			}
		};
		// Add the listener as a filter so that it will be notified of *all*
		// SWT.MouseEnter events. This listener *must* be removed when this plot
		// grid is disposed.
		getDisplay().addFilter(SWT.MouseEnter, plotHoverListener);

		return;
	}

	/*
	 * Overrides a method from Control.
	 */
	@Override
	public void setBackground(Color color) {
		super.setBackground(color);

		// Update the background colors for the ToolBar and all of its widgets.
		// Note: This will *not* update any grandchild widgets.
		toolBar.setBackground(color);
		for (Control child : toolBar.getChildren()) {
			child.setBackground(color);
		}

		// Update the other child widgets.
		gridComposite.setBackground(color);
		for (DrawnPlot plot : drawnPlots) {
			plot.setBackground(color);
		}

		return;
	}

	/**
	 * Sets the number of columns for this plot composite.
	 * 
	 * @param col
	 *            The new number of columns in the grid to layout plots. Note-
	 *            must be between 1 and 4 inclusive, as zero columns makes no
	 *            sense and the screen is too crowded with more than 4.
	 * @return Returns true if the number of columns was successfully set and
	 *         the grid was updated, false if otherwise.
	 */
	public boolean setColumnCount(int col) {
		// Local declarations
		boolean wasSet = false;
		// If the column number fits the criteria, set the value and refresh the
		// composite
		if (col > 0 && col < 5) {
			columns = col;
			refreshLayout();
			wasSet = true;
		}
		// Return if the column was set or not
		return wasSet;
	}

	/**
	 * Sets the number of rows for this plot composite.
	 * 
	 * @param row
	 *            The new number of rows in the grid to layout plots. Note- must
	 *            be between 1 and 4 inclusive, as zero rows makes no sense and
	 *            the screen is too crowded with more than 4.
	 * @return Returns true if the number of rows was successfully set and the
	 *         grid was updated, false if otherwise.
	 */
	public boolean setRowCount(int row) {
		// Local declarations
		boolean wasSet = false;
		// If the row number fits the criteria, set the value and refresh the
		// composite
		if (row > 0 && row < 5) {
			rows = row;
			refreshLayout();
			wasSet = true;
		}
		// Return if the row was set or not
		return wasSet;
	}

	/**
	 * Creates a {@code ToolBar} for this {@code Composite}. It includes the
	 * following controls:
	 * <ol>
	 * <li>Grid rows and columns</li>
	 * <li>A button to clear plots from the grid</li>
	 * </ol>
	 *
	 * @param parent
	 *            The parent {@code Composite} in which to draw the
	 *            {@code ToolBar}.
	 * @return The newly created {@code ToolBar}.
	 */
	private ToolBar createToolBar(Composite parent) {

		// Create and adapt the ToolBar first so that the default styles will be
		// passed down to the widgets created by the ToolBarManager.
		ToolBar toolBar = new ToolBar(parent,
				SWT.WRAP | SWT.FLAT | SWT.HORIZONTAL);
		toolBar.setBackground(parent.getBackground());
		ToolBarManager toolBarManager = new ToolBarManager(toolBar);

		// Add a "Rows" label next to the row Spinner.
		LabelContribution rowLabel = new LabelContribution("rows.label");
		rowLabel.setText("Rows:");
		toolBarManager.add(rowLabel);

		// Add a Spinner for setting the grid rows to the ToolBarManager (this
		// requires a JFace ControlContribution).
		SpinnerContribution rowSpinner = new SpinnerContribution(
				"rows.spinner");
		rowSpinner.setMinimum(1);
		rowSpinner.setMaximum(4);
		rowSpinner.setSelection(rows);
		rowSpinner.setIncrement(1);
		rowSpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				rows = ((Spinner) e.widget).getSelection();
				refreshLayout();
			}
		});
		toolBarManager.add(rowSpinner);

		// Add a "Columns" label next to the row Spinner.
		LabelContribution columnLabel = new LabelContribution("columns.label");
		columnLabel.setText("Columns:");
		toolBarManager.add(columnLabel);

		// Add a Spinner for setting the grid columns to the ToolBarManager
		// (this requires a JFace ControlContribution).
		SpinnerContribution columnSpinner = new SpinnerContribution(
				"columns.spinner");
		columnSpinner.setMinimum(1);
		columnSpinner.setMaximum(4);
		columnSpinner.setSelection(columns);
		columnSpinner.setIncrement(1);
		columnSpinner.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				columns = ((Spinner) e.widget).getSelection();
				refreshLayout();
			}
		});
		toolBarManager.add(columnSpinner);

		// Add a separator between the spinners and the clear button.
		toolBarManager.add(new Separator());

		// Add a ToolBar button to clear the plots.
		toolBarManager.add(new Action("Clear") {
			@Override
			public void run() {
				clearPlots();
			}
		});

		// Apply the ToolBarManager changes to the ToolBar.
		toolBarManager.update(true);

		return toolBar;
	}

	/**
	 * Adds a plot to be drawn inside the plot grid. Note that the same plot can
	 * be added more than once.
	 *
	 * @param plot
	 *            The plot to draw inside the grid.
	 * @return The index of the plot in the grid, or -1 if the plot could not be
	 *         drawn.
	 * @throws Exception
	 *             An exception is thrown if the {@link IPlot} implementation
	 *             cannot be rendered.
	 */
	public int addPlot(IPlot plot) throws Exception {
		int index = -1;

		// Proceed if the plot is not null and there is still space available in
		// the grid. We also can only proceed if there is at least one dependent
		// series (y-axis) to plot and the independent series (x-axis) has been
		// set for this plot.
		ISeries independent = null;
		List<ISeries> dependent = null;

		if (plot != null && drawnPlots.size() < rows * columns
				&& (independent = plot.getIndependentSeries()) != null
				&& (dependent = plot.getAllDependentSeries()) != null) {

			if (dependent.size() > 0 && independent.getBounds() != null) {

				// Create the basic plot Composite.
				final DrawnPlot drawnPlot = new DrawnPlot(gridComposite, plot);
				drawnPlot.setBackground(getBackground());

				// Try to draw the category and type. If the underlying IPlot
				// cannot draw, then dispose of the undrawn plot Composite.
				try {
					drawnPlot.draw();
				} catch (Exception e) {
					drawnPlot.dispose();
					throw e;
				}

				// Add the drawn plot to the list.
				index = drawnPlots.size();
				drawnPlots.add(drawnPlot);
				drawnPlotMap.put(drawnPlot, index);

				// When the drawn plot is disposed, make sure it is removed from
				// the list of drawn plots.
				drawnPlot.addDisposeListener(plotDisposeListener);

				// Set the layout data for the new drawn plot. It should grab
				// all available space in the gridComposite's layout.
				GridData gridData = new GridData(SWT.FILL, SWT.FILL, true,
						true);
				drawnPlot.setLayoutData(gridData);

				// Since a new plot was added, refresh the grid layout.
				refreshLayout();
			}
		}

		return index;
	}

	/**
	 * Removes the drawn plot at the specified index in the grid. If t a drawn
	 * plot was removed, then, at the end of this call, the grid layout will be
	 * refreshed.
	 *
	 * @param index
	 *            The index of the drawn plot to remove. If invalid, nothing is
	 *            done.
	 */
	public void removePlot(int index) {
		// Check the index before passing on the removal request to the defacto
		// remove method. We should refresh the grid layout.
		if (index >= 0 && index < drawnPlots.size()) {
			removePlot(drawnPlots.get(index), true);
		}
		return;
	}

	/**
	 * Removes the drawn plot completely, refreshing the layout if necessary.
	 * <b>This should be the defacto way to remove a drawn plot</b>. <i>If the
	 * code only has access to the drawn plot itself and not this method</i>,
	 * then the plot should be disposed directly.
	 *
	 * @param drawnPlot
	 *            The drawn plot to remove (and, if necessary, dispose).
	 * @param refreshLayout
	 *            Whether or not to refresh the layout after the plot is
	 *            removed.
	 */
	private void removePlot(DrawnPlot drawnPlot, boolean refreshLayout) {
		// The drawn plot is assumed to be valid.

		// Remove it from the book keeping (both the list and the map).
		int i = drawnPlotMap.remove(drawnPlot);
		drawnPlots.remove(i);
		for (; i < drawnPlots.size(); i++) {
			drawnPlotMap.put(drawnPlots.get(i), i);
		}

		// If necessary, dispose it.
		if (!drawnPlot.isDisposed()) {
			// We don't want to trigger this method again, so remove the plot
			// dispose listener first.
			drawnPlot.removeDisposeListener(plotDisposeListener);
			drawnPlot.dispose();
		}

		// If necessary, refresh the layout.
		if (refreshLayout) {
			refreshLayout();
		}

		return;
	}

	/**
	 * Removes all renderings of the specified plot from the grid. If a drawn
	 * plot was removed, then, at the end of this call, the grid layout will be
	 * refreshed.
	 *
	 * @param plot
	 *            The plot whose renderings should be removed from the grid. If
	 *            invalid or not rendered, nothing is done.
	 */
	public void removePlots(IPlot plot) {
		// We can only process non-null plots.
		if (plot != null) {
			// Remove all drawn plots whose IPlot matches the specified plot.
			boolean refreshLayout = false;
			// We traverse backward in the list to reduce the length of
			// traversals in the main remove method.
			for (int i = drawnPlots.size() - 1; i >= 0; i--) {
				DrawnPlot drawnPlot = drawnPlots.get(i);
				if (drawnPlot.plot == plot) {
					removePlot(drawnPlot, false);
					refreshLayout = true;
				}
			}
			// Only refresh the layout if at least one composite was disposed.
			if (refreshLayout) {
				refreshLayout();
			}
		}

		return;
	}

	/**
	 * Removes all drawn plots from the grid. At the end of this call, the grid
	 * layout will be refreshed.
	 */
	public void clearPlots() {
		// Remove all plots. We traverse backward in the list to reduce the
		// length of traversals in the main remove method.
		for (int i = drawnPlots.size() - 1; i >= 0; i--) {
			removePlot(drawnPlots.get(i), false);
		}
		// We should refresh the layout.
		refreshLayout();

		return;
	}

	/**
	 * Finds the ancestor plot for the specified child {@code Composite}.
	 *
	 * @param child
	 *            The child {@code Composite} from which to start the search.
	 *            This could even be the plot {@code Composite} itself. Assumed
	 *            not to be null.
	 * @return The main plot {@code Composite} that is an ancestor of the child,
	 *         or {@code null} if one could not be found.
	 */
	private DrawnPlot findDrawnPlot(Composite child) {
		// This loop breaks when all ancestors have been searched OR when the
		// child plot Composite (whose parent is gridComposite) has been found.
		Composite parent = child.getParent();
		while (parent != gridComposite && parent != null) {
			child = parent;
			parent = child.getParent();
		}
		return (parent != null ? (DrawnPlot) child : null);
	}

	/**
	 * Refreshes the {@link #gridLayout} and drawn plot {@code GridData} based
	 * on the {@link #rows} and {@link #columns} and the number of drawn plots.
	 */
	private void refreshLayout() {

		// Remove all excess drawn plots.
		int limit = rows * columns;
		for (int i = drawnPlots.size() - 1; i >= limit; i--) {
			removePlot(drawnPlots.get(i), false);
		}

		// Reset all cells to only take up one grid cell.
		GridData gridData;
		for (DrawnPlot drawnPlot : drawnPlots) {
			gridData = (GridData) drawnPlot.getLayoutData();
			gridData.verticalSpan = 1;
		}

		int size = drawnPlots.size();

		// Set the user-defined number of columns. The rows are handled already
		// because we've removed all excess plots.
		gridLayout.numColumns = (size > columns ? columns : size);

		// If the last row has empty cells, then all of the cells directly above
		// those empty cells should grab the excess vertical space by updating
		// the verticalSpan property.
		int lastRowSize = size % columns;
		// We shouldn't do anything if the last row is full or if there is only
		// one row.
		if (lastRowSize > 0 && size > columns) {
			int lastIndex = size - 1 - lastRowSize;
			for (int i = 0; i < columns - lastRowSize; i++) {
				DrawnPlot drawnPlot = drawnPlots.get(lastIndex - i);
				gridData = (GridData) drawnPlot.getLayoutData();
				gridData.verticalSpan = 2;
			}
		}

		// Refresh the grid layout.
		gridComposite.layout();

		return;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.swt.widgets.Widget#dispose()
	 */
	@Override
	public void dispose() {

		// Remove the filter for the listener that creates the close button on
		// certain SWT.MouseEnter events.
		getDisplay().removeFilter(SWT.MouseEnter, plotHoverListener);

		// Clear the plots. There is no need to refresh the layout.
		for (int i = drawnPlots.size() - 1; i >= 0; i--) {
			removePlot(drawnPlots.get(i), false);
		}

		// Proceed with the normal dispose operation.
		super.dispose();
	}

	/**
	 * This nested class provides the {@code Composite} that immediately wraps a
	 * rendering from an {@code IPlot} implementation. It handles all
	 * plot-specific widgets and layouts for the associated {@link #plot}.
	 *
	 * @author Jordan Deyton
	 *
	 */
	private class DrawnPlot extends Composite {

		/**
		 * The {@link IPlot} instance/implementation that is drawn.
		 */
		public final IPlot plot;

		/**
		 * The default constructor. Creates a container for an {@code IPlot}
		 * instance with the {@code SWT.BORDER} style.
		 *
		 * @param parent
		 *            The parent in which to draw the plot.
		 * @param plot
		 *            The {@code IPlot} instance that will be drawn.
		 */
		public DrawnPlot(Composite parent, IPlot plot) {
			this(parent, plot, SWT.BORDER);
		}

		/**
		 * The full constructor that allows a custom style to be set.
		 *
		 * @param parent
		 *            The parent in which to draw the plot.
		 * @param plot
		 *            The {@code IPlot} instance that will be drawn.
		 * @param style
		 *            The style to use for this {@code Composite}.
		 */
		public DrawnPlot(Composite parent, IPlot plot, int style) {
			super(parent, style);

			this.plot = plot;

			createContextMenu();

			// The plot Composite should have a FormLayout so that the close
			// button can be properly displayed in the top-right corner on
			// top of all other controls. Otherwise, this is effectively a
			// FillLayout.
			setLayout(new FormLayout());

			return;
		}

		/**
		 * Creates a context {@code Menu} for the drawn plot's main
		 * {@code Composite}. It includes the following controls by default:
		 * <ol>
		 * <li>Remove Plot</li>
		 * <li>Separator</li>
		 * <li>Set Plot Type</li>
		 * <li>Separator</li>
		 * <li>Any implementation-specific plot actions...</li>
		 * </ol>
		 * It is up to the related {@code IPlot} implementation to take the
		 * created {@code Menu} and add it to child {@code Composite}s or update
		 * it.
		 *
		 * @return The JFace {@code MenuManager} for the context {@code Menu}.
		 */
		private MenuManager createContextMenu() {
			MenuManager contextMenuManager = new MenuManager();

			// Create an action to remove the moused-over or clicked plot
			// rendering.
			final Action removeAction = new Action("Remove") {
				@Override
				public void run() {
					dispose();
				}
			};

			// Add the items to the context menu.
			contextMenuManager.add(removeAction);
			contextMenuManager.add(new Separator());

			// Set the context Menu for the plot Composite.
			setMenu(contextMenuManager.createContextMenu(this));

			return contextMenuManager;
		}

		/**
		 * Attempts to draw the specified plot category and type with the
		 * underlying {@link IPlot} implementation.
		 *
		 * @param category
		 *            The new plot category.
		 * @param type
		 *            The new plot type.
		 * @throws Exception
		 *             An exception is thrown if the underlying {@code IPlot}
		 *             implementation fails to draw or update.
		 */
		public void draw() throws Exception {
			plot.draw(this);
			refreshLayout();
		}

		/**
		 * Refreshes the drawn plot's layout, adding a new {@code FormData} to
		 * any new child Composites.
		 */
		private void refreshLayout() {
			boolean changed = false;
			for (Control child : getChildren()) {
				if (child.getLayoutData() == null) {
					// Set up the child to fill the plot Composite.
					FormData formData = new FormData();
					formData.top = new FormAttachment(0, 0);
					formData.bottom = new FormAttachment(100, 0);
					formData.left = new FormAttachment(0, 0);
					formData.right = new FormAttachment(100, 0);
					child.setLayoutData(formData);
					changed = true;
				}
			}
			layout(changed);
			return;
		}

		/**
		 * Creates a new {@link #closeButton} in the corner of the drawn plot.
		 * <p>
		 * To get rid of the close button, simply dispose it.
		 * {@link #closeButton} will automatically be set to {@code null}.
		 * </p>
		 */
		public Button createCloseButton() {

			// Set up the close button.
			Button closeButton = new Button(this, SWT.FLAT | SWT.CENTER);
			closeButton.setText("X");
			FontData[] smallFont = closeButton.getFont().getFontData();
			for (FontData fd : smallFont) {
				fd.setHeight(7);
			}
			closeButton.setFont(new Font(getDisplay(), smallFont));
			closeButton.setToolTipText("Close plot");
			closeButton.pack();

			// Set the location of the button to the upper right-hand corner of
			// the
			// plot Composite, and above all other children of the plot
			// Composite.
			FormData formData = new FormData();
			formData.top = new FormAttachment(0, 0);
			formData.right = new FormAttachment(100, -4);
			closeButton.setLayoutData(formData);
			closeButton.moveAbove(null);
			layout();

			// Add a selection listener on it to close the drawn plot.
			closeButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					dispose();
				}
			});

			return closeButton;
		}
	}
}
