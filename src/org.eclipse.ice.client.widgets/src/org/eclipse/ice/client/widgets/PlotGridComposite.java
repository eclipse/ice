package org.eclipse.ice.client.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.MouseTrackListener;
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
	private int rows;
	/**
	 * The number of columsn to display in the grid.
	 */
	private int columns;

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
		// TODO
		refreshLayout();
		return -1;
	}

	/**
	 * Removes the drawn plot at the specified index in the grid.
	 * 
	 * @param index
	 *            The index of the drawn plot to remove. If invalid, nothing is
	 *            done.
	 */
	public void removePlot(int index) {
		// TODO
		refreshLayout();
	}

	/**
	 * Removes all renderings of the specified plot from the grid.
	 * 
	 * @param plot
	 *            The plot whose renderings should be removed from the grid. If
	 *            invalid or not rendered, nothing is done.
	 */
	public void removeDrawnPlots(IPlot plot) {
		// TODO
		refreshLayout();
	}

	/**
	 * Removes all renderings from the grid.
	 */
	public void clearPlots() {
		// TODO
		refreshLayout();
	}

	/**
	 * Shows the close button in the correct location if necessary.
	 * 
	 * @param e
	 *            The {@code MouseEvent} (usually mouse enter) that triggered
	 *            this call.
	 */
	private void showCloseButton(MouseEvent e) {
		// TODO
	}

	/**
	 * Hides (and disposes) the close button if necessary.
	 * 
	 * @param e
	 *            The {@code MouseEvent} (usually a mouse exit) that triggered
	 *            this call.
	 */
	private void hideCloseButton(MouseEvent e) {
		// TODO
	}

	/**
	 * Refreshes the {@link #gridLayout} and drawn plot {@code GridData} based
	 * on the {@link #rows} and {@link #columns} and the number of drawn plots.
	 */
	private void refreshLayout() {
		// TODO
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

		public DrawnPlot(IPlot plot, Composite composite) throws Exception {
			this.plot = plot;
			this.composite = composite;

			// TODO This should use the code from ICEResourcePage for drawing
			// the plot.
			childComposite = plot.draw("", "", composite);
		}
	}
}
