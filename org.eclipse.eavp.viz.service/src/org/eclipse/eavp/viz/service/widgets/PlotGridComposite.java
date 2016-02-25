/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - relocated to viz.service.widgets
 *******************************************************************************/
package org.eclipse.eavp.viz.service.widgets;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.Window;
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
 * This custom composite is designed to display a grid of plots. It includes
 * widgets to customize the grid-based layout of the plots. The order of a plot
 * in the grid is based on the order in which it is created. The same plot
 * cannot be added to the grid more than once.
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
	 * This is a button used to close the drawn plot over which the mouse is
	 * currently hovering. It should only be visible in the top-right corner of
	 * a drawn plot when the mouse cursor is over the plot.
	 */
	private Button closeButton;
	/**
	 * The number of columns to display in the grid.
	 */
	private int columns = 2;
	/**
	 * The grid of drawn plots.
	 */
	private final Composite grid;
	/**
	 * The user-customizable layout for {@link #grid}, the grid of drawn plots.
	 */
	private final GridLayout gridLayout;
	/**
	 * A list of all plot composites.
	 */
	private final List<Composite> plotComposites;
	/**
	 * A dispose listener that will remove the disposed plot composite
	 * completely, including updating the bookkeeping of plots.
	 */
	private final DisposeListener plotDisposeListener;
	/**
	 * A list of all plots in the order in which they were added.
	 */
	private final List<IPlot> plots;

	/**
	 * The number of rows to display in the grid.
	 */
	private int rows = 2;
	/**
	 * The {@code ToolBar} that contains widgets to update the grid layout and
	 * clear the grid.
	 */
	private final ToolBar toolBar;

	/**
	 * The default constructor. Creates a composite in the specified parent and
	 * with the specified style.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public PlotGridComposite(Composite parent, int style) {
		super(parent, style);

		// Set the initial background. This is so the ToolBar's widgets will all
		// have the correct background when they are created.
		super.setBackground(parent.getBackground());

		// Set the layout.
		setLayout(new GridLayout());

		// Set up the ToolBar.
		toolBar = createToolBar(this);
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Set up the grid Composite.
		grid = new Composite(this, SWT.NONE);
		grid.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		gridLayout = new GridLayout();
		gridLayout.makeColumnsEqualWidth = true;
		grid.setLayout(gridLayout);

		// Set up the initial maps.
		plotComposites = new ArrayList<Composite>();
		plots = new ArrayList<IPlot>();

		// Add the filter that makes the close buttons appear.
		addFilter();

		// Create the plot dispose listener.
		plotDisposeListener = new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				disposePlot((Composite) e.widget);
				refreshLayout();
			}
		};

		return;
	}

	/**
	 * Adds a filter for mouse enter events so that when a drawn plot is
	 * entered, the close button appears.
	 */
	private void addFilter() {
		// Create the listener that creates the close button when the mouse
		// enters a plot Composite (in the grid).
		final Listener plotHoverListener = new Listener() {

			/**
			 * The most recent plot that was entered, or null.
			 */
			private Composite lastPlot;

			@Override
			public void handleEvent(Event event) {
				// We only deal with Composites here.
				if (event.widget instanceof Composite) {
					// Determine the plot Composite that the mouse entered.
					Composite child = (Composite) event.widget;
					Composite plot = findDrawnPlot(child);

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
							closeButton = createCloseButton(plot);
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

		// When this Composite is disposed, we should remove the filter.
		addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				getDisplay().removeFilter(SWT.MouseEnter, plotHoverListener);
			}
		});

		return;
	}

	/**
	 * Adds a plot to be drawn inside the plot grid. Note that the same URI can
	 * be added more than once.
	 * 
	 * @param uri
	 *            The URI of the file to be opened as a plot in the grid.
	 * @return The index of the plot in the grid, or -1 if the plot could not be
	 *         drawn.
	 * @throws Exception
	 *             An exception is thrown if the {@link IPlot} implementation
	 *             cannot be rendered.
	 */
	public int addPlot(URI uri) {
		int index = -1;

		// Try to create a plot if there is space available.
		IPlot plot = null;
		if (plots.size() < rows * columns) {
			// Try to create a plot using the available viz services, prompting
			// the user if two or more services can create a plot.
			PlotDialogProvider provider = new PlotDialogProvider();
			if (provider.openDialog(getShell(), uri) == Window.OK) {
				plot = provider.getSelectedPlot();
			}
		} else {
			System.err.println("Rejecting plot because not enough space");
		}

		// If a plot could be created, try to draw it.
		if (plot != null) {
			// Try to draw the plot.
			Composite plotComposite = null;
			try {
				plotComposite = plot.draw(grid);
			} catch (Exception e) {
				// If the plot could not be drawn, fail.
				logger.warn(getClass().getName() + " Exception! "
						+ "Error while attempting to draw a plot for the file \""
						+ uri.getPath() + "\".", e);
				System.err.println("Exception while drawing plot");
				return index;
			}

			// Add the plot to the list.
			index = plots.size();
			System.err.println("Drawing plot with index " + index);
			plots.add(plot);
			plotComposites.add(plotComposite);

			// Add the dispose listener to update the bookkeeping when the drawn
			// plot is disposed.
			plotComposite.addDisposeListener(plotDisposeListener);

			// Set the Composite's layout data.
			plotComposite.setLayoutData(
					new GridData(SWT.FILL, SWT.FILL, true, true));

			// Since a new plot was added, refresh the grid layout.
			refreshLayout();
		} else {
			System.err.println("Rejecting plot because is null");
		}

		return index;
	}

	/**
	 * Creates a new {@link #closeButton} in the corner of the specified plot
	 * composite.
	 * <p>
	 * To get rid of the close button, simply dispose it. {@link #closeButton}
	 * will automatically be set to {@code null}.
	 * </p>
	 */
	private Button createCloseButton(Composite plotComposite) {
		// Set up the close button.
		Button closeButton = new Button(plotComposite, SWT.FLAT | SWT.CENTER);
		closeButton.setText("X");
		FontData[] smallFont = closeButton.getFont().getFontData();
		for (FontData fd : smallFont) {
			fd.setHeight(7);
		}
		closeButton.setFont(new Font(getDisplay(), smallFont));
		closeButton.setToolTipText("Close plot");
		closeButton.pack();

		// Set the location of the button to the upper right-hand corner of the
		// plot Composite, and above all other children of the plot Composite.
		FormData formData = new FormData();
		formData.top = new FormAttachment(0, 0);
		formData.right = new FormAttachment(100, -4);
		closeButton.setLayoutData(formData);
		closeButton.moveAbove(null);
		layout();

		// Add a selection listener on it to close the drawn plot.
		final Composite plotCompositeRef = plotComposite;
		closeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				disposePlot(plotCompositeRef);
				refreshLayout();
			}
		});

		return closeButton;
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
				removeAllPlots();
			}
		});

		// Apply the ToolBarManager changes to the ToolBar.
		toolBarManager.update(true);

		return toolBar;
	}

	/*
	 * Overides a method from Composite.
	 */
	@Override
	public void dispose() {
		// Clear the plots. There is no need to refresh the layout.
		for (int i = plots.size() - 1; i >= 0; i--) {
			disposePlot(i);
		}

		// Proceed with the normal dispose operation.
		super.dispose();
	}

	/**
	 * Disposes the specified plot, if possible. This includes removing it from
	 * the bookkeeping.
	 * <p>
	 * <b>Note:</b> This method is intended to be called either when the plot is
	 * actually being disposed or simply programmatically.
	 * </p>
	 * 
	 * @param composite
	 *            The plot composite to dispose.
	 */
	private void disposePlot(Composite composite) {

		// Find the index.
		int index = -1;
		for (int i = 0; i < plots.size() && index == -1; i++) {
			if (plotComposites.get(i) == composite) {
				index = i;
			}
		}

		// Dispose the plot at the specified index.
		if (index != -1) {
			disposePlot(index);
		}

		return;
	}

	/**
	 * Disposes the plot at the specified index, including disposing its
	 * composite if not already disposed.
	 * 
	 * @param index
	 *            The index of the plot to remove.
	 */
	private void disposePlot(int index) {
		// Update the bookkeeping.
		plots.remove(index);
		Composite plotComposite = plotComposites.remove(index);

		// Dispose the composite if necessary. Note that we'll have to remove
		// the listener to avoid infinite recursion.
		if (!plotComposite.isDisposed()) {
			plotComposite.removeDisposeListener(plotDisposeListener);
			plotComposite.dispose();
		}
		return;
	}

	/**
	 * Finds the ancestor plot {@code Composite} for the specified child
	 * {@code Composite}.
	 *
	 * @param child
	 *            The child {@code Composite} from which to start the search.
	 *            This could even be the plot {@code Composite} itself. Assumed
	 *            not to be null.
	 * @return The main plot {@code Composite} that is an ancestor of the child,
	 *         or {@code null} if one could not be found.
	 */
	private Composite findDrawnPlot(Composite child) {
		// This loop breaks when all ancestors have been searched OR when the
		// child plot Composite (whose parent is the grid) has been found.
		Composite parent = child.getParent();
		while (parent != grid && parent != null) {
			child = parent;
			parent = child.getParent();
		}
		return (parent != null ? (Composite) child : null);
	}

	/**
	 * Refreshes the {@link #gridLayout} and drawn plot {@code GridData} based
	 * on the {@link #rows} and {@link #columns} and the number of drawn plots.
	 */
	private void refreshLayout() {

		// Remove all excess drawn plots.
		int limit = rows * columns;
		for (int i = plots.size() - 1; i >= limit; i--) {
			disposePlot(i);
		}

		// Reset all cells to only take up one grid cell.
		GridData gridData;
		for (Composite plotComposite : plotComposites) {
			gridData = (GridData) plotComposite.getLayoutData();
			gridData.verticalSpan = 1;
		}

		int size = plots.size();

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
				Composite plotComposite = plotComposites.get(lastIndex - i);
				gridData = (GridData) plotComposite.getLayoutData();
				gridData.verticalSpan = 2;
			}
		}

		// Refresh the grid layout.
		grid.layout();

		return;
	}

	/**
	 * Refreshes any plots that may be associated with the specified URI.
	 * 
	 * @param uri
	 *            The data file.
	 */
	public void refreshPlots(URI uri) {
		// Try to refresh any plots whose URI matches the specified one.
		for (int i = 0; i < plots.size(); i++) {
			IPlot plot = plots.get(i);
			// Composite plotComposite = plotComposites.get(i);
			if (plot.getDataSource().equals(uri)) {
				try {
					plot.redraw();
				} catch (Exception e) {
					logger.warn(getClass().getName() + " Exception! "
							+ "Error while refreshing a plot for the file \""
							+ uri.getPath() + "\".", e);
				}
			}
		}
		return;
	}

	/**
	 * Removes all plots from the grid.
	 * 
	 * @return The number of removed plots.
	 */
	public int removeAllPlots() {
		// Remove all plots.
		int count = plots.size();
		for (int i = count - 1; i >= 0; i--) {
			disposePlot(i);
		}
		// Refresh the grid if necessary.
		if (count > 0) {
			refreshLayout();
		}
		return count;
	}

	/**
	 * Removes the plot with the specified index.
	 * 
	 * @param index
	 *            The index of the plot to remove.
	 * @return True if a plot was removed, false otherwise (if the index was
	 *         invalid).
	 */
	public boolean removePlot(int index) {
		boolean removed = false;
		// Check the index before passing on the removal request to the defacto
		// remove method. We should refresh the grid layout.
		if (index >= 0 && index < plots.size()) {
			removed = true;
			disposePlot(index);
			refreshLayout();
		}
		return removed;
	}

	/**
	 * Removes any plot for the associated URI.
	 * 
	 * @param uri
	 *            The URI for which all plots will be removed.
	 * @return The number of plots removed.
	 */
	public int removePlots(URI uri) {
		int count = 0;
		// Dispose any plots with a matching URI.
		for (int i = plots.size() - 1; i >= 0; i--) {
			IPlot plot = plots.get(i);
			if (plot.getDataSource() == uri) {
				disposePlot(i);
				count++;
			}
		}
		// Refresh the grid if necessary.
		if (count > 0) {
			refreshLayout();
		}
		return count;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Control#setBackground(org.eclipse.swt.graphics.Color)
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
		grid.setBackground(color);
		for (Composite plot : plotComposites) {
			// for (Control child : plot.getChildren()) {
			// child.setBackground(color);
			// }
			plot.setBackground(color);
		}

		return;
	}

	/**
	 * Return true if the given URI has already been 
	 * plotted and thus is contained in this PlotGridComposite. 
	 * 
	 * @param uri
	 * @return
	 */
	public boolean contains(URI uri) {
		for (IPlot plot : plots) {
			if (plot.getDataSource().equals(uri)) {
				return true;
			}
		}
		return false;
	}
}
