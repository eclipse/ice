package org.eclipse.ice.client.widgets;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;

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

	// TODO Populate the context menu based on the clicked plot.

	/**
	 * The {@code ToolBar} that contains widgets to update the grid layout and
	 * clear the grid.
	 */
	private final ToolBar toolBar;

	/**
	 * The context menu that will be used by both this plot grid
	 * {@code Composite} and all drawn plots.
	 */
	private final Menu contextMenu;

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
	 * The number of columsn to display in the grid.
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
	private final MouseTrackListener closeButtonListener;

	/**
	 * The list of currently drawn plots. A nested class, {@link DrawnPlot}, is
	 * used to contain meta-data about each drawn plot or cell in the grid.
	 */
	private final List<DrawnPlot> drawnPlots;

	/**
	 * The default constructor. Creates a {@code Composite} designed to display
	 * a grid of {@link IPlot} renderings.
	 * 
	 * @param parent
	 *            A widget that will be the parent of the new instance (cannot
	 *            be null).
	 * @param style
	 *            The style of widget to construct.
	 */
	public PlotGridComposite(Composite parent, int style) {
		super(parent, style);

		// Initialize the list of drawn plots.
		drawnPlots = new ArrayList<DrawnPlot>();

		// Set up the ToolBar.
		ToolBarManager toolBarManager = new ToolBarManager();
		toolBar = toolBarManager.createControl(this);

		// Set up the Composite containing the grid of plots.
		gridComposite = new Composite(this, SWT.NONE);
		gridLayout = new GridLayout();
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

		// Create the context Menu that will be used for this and all child plot
		// Composites.
		MenuManager menuManager = new MenuManager();
		contextMenu = menuManager.createContextMenu(this);

		// Create the listener that will be used to show/hide the close button.
		closeButtonListener = new MouseTrackAdapter() {
			@Override
			public void mouseEnter(MouseEvent e) {
				showCloseButton(e);
			}

			@Override
			public void mouseExit(MouseEvent e) {
				hideCloseButton(e);
			}
		};

		return;
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

		if (plot != null) {

			// Try to get the available categories and plot types, then try to
			// plot the first available one.
			Map<String, String[]> plotTypes = plot.getPlotTypes();

			// Find the first category and plot type.
			String category = null;
			String type = null;
			for (Entry<String, String[]> entry : plotTypes.entrySet()) {
				category = entry.getKey();
				String[] types = entry.getValue();
				if (category != null && types != null) {
					for (int i = 0; i < types.length && type == null; i++) {
						type = types[i];
					}
					if (type != null) {
						break;
					}
				}
			}

			// If a category and type could be found, try to draw the plot in a
			// new cell in the grid.
			if (category != null && type != null) {
				// Create the Composite to contain the plot rendering.
				Composite composite = new Composite(gridComposite, SWT.NONE);

				// Try to create the plot rendering.
				DrawnPlot drawnPlot;
				try {
					drawnPlot = new DrawnPlot(plot, composite, category, type);
				} catch (Exception e) {
					// In case of failure, dispose the Composite that was just
					// created before sending the Exception on to the calling
					// class.
					composite.dispose();
					throw e;
				}

				// Add the drawn plot to the list.
				index = drawnPlots.size();
				drawnPlots.add(drawnPlot);

				// Hook the new drawn plot up to the close button.
				drawnPlot.childComposite
						.addMouseTrackListener(closeButtonListener);

				// Set up the layout and layout data for the new drawn plot.
				GridData gridData = new GridData(SWT.FILL, SWT.FILL, true, true);
				drawnPlot.composite.setLayoutData(gridData);
				drawnPlot.composite.setLayout(new FillLayout());

				// Since a new plot was added, refresh the grid layout.
				refreshLayout();
			}
		}

		return index;
	}

	/**
	 * Removes the drawn plot at the specified index in the grid.
	 * 
	 * @param index
	 *            The index of the drawn plot to remove. If invalid, nothing is
	 *            done.
	 */
	public void removePlot(int index) {

		if (index >= 0 && index < drawnPlots.size()) {
			// Pull the associated drawn plot from the list.
			DrawnPlot drawnPlot = drawnPlots.remove(index);

			// Dispose the plot's associated cell in the grid.
			drawnPlot.composite.dispose();

			// Since a plot was removed, refresh the grid layout.
			refreshLayout();
		}

		return;
	}

	/**
	 * Removes all renderings of the specified plot from the grid.
	 * 
	 * @param plot
	 *            The plot whose renderings should be removed from the grid. If
	 *            invalid or not rendered, nothing is done.
	 */
	public void removeDrawnPlots(IPlot plot) {

		if (plot != null) {
			boolean compositeDisposed = false;

			Iterator<DrawnPlot> iterator = drawnPlots.iterator();
			while (iterator.hasNext()) {
				DrawnPlot drawnPlot = iterator.next();

				if (drawnPlot.plot == plot) {
					// Pull the drawn plot from the list.
					iterator.remove();

					// Dispose the plot's associated cell in the grid.
					drawnPlot.composite.dispose();

					compositeDisposed = true;
				}
			}

			// If a Composite was removed, refresh the grid layout.
			if (compositeDisposed) {
				refreshLayout();
			}
		}

		return;
	}

	/**
	 * Removes all renderings from the grid.
	 */
	public void clearPlots() {

		if (!drawnPlots.isEmpty()) {

			// Dispose all of the cells in the grid.
			for (DrawnPlot drawnPlot : drawnPlots) {
				drawnPlot.composite.dispose();
			}
			// Clear the list of drawn plots.
			drawnPlots.clear();

			// Since a Composite was removed, refresh the grid layout.
			refreshLayout();
		}

		return;
	}

	/**
	 * Shows the close button in the correct location if necessary.
	 * 
	 * @param e
	 *            The {@code MouseEvent} (usually mouse enter) that triggered
	 *            this call.
	 */
	private void showCloseButton(MouseEvent e) {

		if (closeButton == null || closeButton.isDisposed()) {
			// Get the Composite that triggered the event.
			Composite comp = (Composite) e.widget;

			// Determine the index of the plot that was moused over. If a plot
			// could not be found, then use -1 for the index.
			int i;
			for (i = 0; i < drawnPlots.size(); i++) {
				if (comp == drawnPlots.get(i).childComposite) {
					break;
				}
			}
			final int plotIndex = (i < drawnPlots.size() ? i : -1);

			// Set up the close button
			closeButton = new Button(comp, SWT.FLAT | SWT.CENTER);

			// Add the close button listener to the close button, too.
			closeButton.addMouseTrackListener(closeButtonListener);

			closeButton.setText("X");
			FontData[] smallFont = closeButton.getFont().getFontData();
			for (FontData fd : smallFont) {
				fd.setHeight(7);
			}
			closeButton.setFont(new Font(comp.getDisplay(), smallFont));
			closeButton.setToolTipText("Close plot");

			// Add a selection listener on it to dispose the composite
			closeButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					removePlot(plotIndex);
				}
			});

			closeButton.pack();

			// Set the location of the button to the upper right-hand corner
			closeButton.setLocation(
					comp.getBounds().width - closeButton.getBounds().width - 4,
					0);
		}

		return;
	}

	/**
	 * Hides (and disposes) the close button if necessary.
	 * 
	 * @param e
	 *            The {@code MouseEvent} (usually a mouse exit) that triggered
	 *            this call.
	 */
	private void hideCloseButton(MouseEvent e) {

		// If the cursor has left the canvas area and the close button, dispose
		// the button.
		if (closeButton != null && !closeButton.isDisposed()
				&& !closeButton.getBounds().contains(e.x, e.y)) {
			closeButton.dispose();
			closeButton = null;
		}

		return;
	}

	/**
	 * Refreshes the {@link #gridLayout} and drawn plot {@code GridData} based
	 * on the {@link #rows} and {@link #columns} and the number of drawn plots.
	 */
	private void refreshLayout() {
		// TODO
		
		gridComposite.layout();
	}

	/**
	 * This nested class contains meta-data about each drawn plot.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	private class DrawnPlot {

		public final IPlot plot;
		public final Composite composite;
		public final Composite childComposite;

		private String category;
		private String type;

		public DrawnPlot(IPlot plot, Composite composite, String category,
				String type) throws Exception {
			this.plot = plot;
			this.composite = composite;

			childComposite = plot.draw(category, type, composite);

			return;
		}

		public void setPlotType(String category, String type) throws Exception {
			// Only process new, non-null plot category and type.
			if (category != null
					&& type != null
					&& (!category.equals(this.category) || !type
							.equals(this.type))) {
				// TODO
			}

			return;
		}
	}
}
