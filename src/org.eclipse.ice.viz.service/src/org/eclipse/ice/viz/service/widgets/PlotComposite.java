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
package org.eclipse.ice.viz.service.widgets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;

public class PlotComposite extends Composite {

	/**
	 * The context {@code Menu} for this plot composite. It is populated using
	 * the actions returned by {@link #getPlotActions()}.
	 */
	private Menu contextMenu;

	/**
	 * A {@code Composite} containing useful information about the plot if its
	 * content could not be rendered.
	 */
	private Composite infoContent;

	/**
	 * The source plot that is rendered in the {@link #plotContent}
	 * {@code Composite}.
	 */
	private IPlot plot;

	/**
	 * The plot actions that are added to the {@link #contextMenu} and
	 * {@link #toolBar} (if it is enabled).
	 */
	private List<ActionTree> plotActions;

	/**
	 * A {@code Composite} containing the rendered {@link #plot}.
	 */
	private Composite plotContent;

	/**
	 * A map of all currently plotted series.
	 */
	private final Set<ISeries> plottedSeries;

	/**
	 * The {@code ToolBar} for this plot composite. It is populated using the
	 * actions returned by {@link #getPlotActions()} and can be disabled by
	 * overriding {@link #showToolBar()};
	 */
	private ToolBar toolBar;

	// ---- Info-content widgets ---- //
	/**
	 * Displays an icon to demonstrate the severity of the message.
	 */
	private Label iconLabel;
	/**
	 * Displays a message explaining the current state of the plot or why it
	 * cannot render anything.
	 */
	private Label msgLabel;

	/**
	 * The image to display in the {@link #iconLabel}.
	 */
	protected Image infoIcon;
	// ------------------------------ //

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 * 
	 */
	public PlotComposite(Composite parent, int style) {
		super(parent, style);

		plottedSeries = new HashSet<ISeries>();

		// Configure the Composite settings.
		setFont(parent.getFont());
		setBackground(parent.getBackground());
		setLayout(new StackLayout());

		// Load the plot actions and context Menu.
		plotActions = getPlotActions();
		contextMenu = createContextMenu(this);

		return;
	}

	protected Menu createContextMenu(Composite parent) {
		// Create the context MenuManager and create its Menu.
		final MenuManager menuManager = new MenuManager();
		for (ActionTree action : plotActions) {
			menuManager.add(action.getContributionItem());
		}

		// Get the current context Menu from the parent of the plot Composite.
		Menu menu = parent.getMenu();
		// If it exists, it should be using a MenuManager. However, we cannot
		// add new actions to the MenuManager (there is no way to get it), so we
		// must add a MenuListener to add additional items when the menu opens.
		if (menu != null) {
			menu.addMenuListener(new MenuListener() {
				@Override
				public void menuHidden(MenuEvent e) {
					// Nothing to do.
				}

				@Override
				public void menuShown(MenuEvent e) {
					Menu menu = (Menu) e.widget;
					// Add all items from the MenuManager.
					for (IContributionItem item : menuManager.getItems()) {
						item.fill(menu, -1);
					}
				}
			});
		}
		// If the parent Menu does not exist, create a new context Menu and set
		// it for the plot Composite.
		else {
			menu = menuManager.createContextMenu(parent);
		}

		return menu;
	}

	protected ToolBar createToolBar(Composite parent) {
		ToolBar toolBar = null;
		if (showToolBar()) {
			// Create the ToolBarManager and add its content.
			final ToolBarManager toolBarManager = new ToolBarManager();
			for (ActionTree action : plotActions) {
				toolBarManager.add(action.getContributionItem());
			}

			// Create the ToolBar itself.
			toolBar = toolBarManager.createControl(parent);
		}
		return toolBar;
	}

	protected Composite createInfoContent(Composite parent, int style) {

		Composite infoComposite = new Composite(parent, style);
		infoComposite.setLayout(new GridLayout(2, false));

		// Create an info label with an image.
		iconLabel = new Label(infoComposite, SWT.NONE);
		iconLabel.setLayoutData(
				new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));

		// Create a Composite to contain the info message.
		Composite msgComposite = new Composite(infoComposite, SWT.NONE);
		msgComposite.setLayoutData(
				new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false));
		msgComposite.setLayout(new GridLayout(1, false));

		// Create an info label with informative text.
		msgLabel = new Label(msgComposite, SWT.NONE);
		msgLabel.setLayoutData(
				new GridData(SWT.BEGINNING, SWT.CENTER, false, false));

		return infoComposite;
	}

	protected Composite createPlotContent(Composite parent, int style)
			throws Exception {
		throw new UnsupportedOperationException("PlotComposite error: "
				+ "The plot content cannot be created.");
	}

	protected void disposeInfoContent(Composite infoContent) {
		infoContent.dispose();
		iconLabel = null;
		msgLabel = null;
	}

	protected void disposePlotContent(Composite plotContent) {
		if (showToolBar()) {
			toolBar.dispose();
			toolBar = null;
		}
		plotContent.dispose();
	}

	/**
	 * Adds the context Menu for this plot composite to the specified
	 * {@code Composite}. This method also adds a listener to unregister the
	 * context Menu from the specified {@code Composite} at disposal to prevent
	 * the Menu from being disposed itself.
	 * 
	 * @param composite
	 *            The child composite that needs to have the context Menu.
	 */
	protected void addContextMenu(Composite composite) {
		if (composite != null) {
			// Set the context Menu for the composite.
			composite.setMenu(contextMenu);

			// Add a listener to remove the context Menu. This prevents the
			// context Menu from being destroyed when the Composite is disposed.
			final Composite compositeRef = composite;
			composite.addDisposeListener(new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					// Unset the Menu if it matches.
					if (compositeRef.getMenu() == contextMenu) {
						compositeRef.setMenu(null);
					}
				}
			});
		}
		return;
	}

	protected IPlot getPlot() {
		return plot;
	}

	/**
	 * Gets the list of actions supported by this plot composite. These will be
	 * added to the {@link #contextMenu} and the {@link #toolBar} (if enabled).
	 * 
	 * @return A list containing the supported plot actions.
	 */
	protected List<ActionTree> getPlotActions() {
		return new ArrayList<ActionTree>();
	}

	protected List<ISeries> getPlottedSeries() {
		return new ArrayList<ISeries>(plottedSeries);
	}

	protected boolean isPlotted(ISeries series) {
		return plottedSeries.contains(series);
	}

	/**
	 * Hides the specified series. Sub-classes should override this method to
	 * handle actually removing the series, but they <b><i>must call the super
	 * method last after any exceptions are thrown</i></b>.
	 * 
	 * @param series
	 *            The series to hide.
	 * @throws Exception
	 *             If the series could not be hidden.
	 */
	protected void hideSeries(ISeries series) throws Exception {
		series.setEnabled(false);
		plottedSeries.remove(series);
	}

	/**
	 * Refreshes the composite based on the {@link #plot}'s current content.
	 * <p>
	 * This method is safe to call from off the UI thread.
	 * </p>
	 */
	public void refresh() {
		// If we are not on the UI thread, update the UI asynchronously on the
		// UI thread.
		if (Display.getCurrent() == null) {
			getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					performRefresh();
				}
			});
		}
		// If we are on the UI thread, update the UI synchronously.
		else {
			performRefresh();
		}

		return;
	}

	/**
	 * Refreshes the entire composite. This method effectively creates the
	 * {@link #plotContent} or {@link #infoContent} as necessary depending on
	 * whether the plot can be rendered.
	 */
	protected void performRefresh() {

		// Get the StackLayout from the plot Composite.
		final StackLayout stackLayout = (StackLayout) getLayout();

		try {
			// Update the plotComposite. Create it if necessary.
			if (plotContent == null) {
				plotContent = createPlotContent(this, SWT.NONE);
			}
			updatePlotContent(plotContent);

			// If the plotComposite was successfully updated, we can dispose the
			// infoComposite.
			if (infoContent != null && !infoContent.isDisposed()) {
				disposeInfoContent(infoContent);
				infoContent = null;
			}

			// Update the stack layout, putting the plotComposite in front.
			if (stackLayout.topControl != plotContent) {
				stackLayout.topControl = plotContent;
				layout();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// Update the infoComposite. Create it if necessary.
			if (infoContent == null) {
				infoContent = createInfoContent(this, SWT.NONE);
			}
			updateInfoContent(infoContent, e.getMessage());

			// Dispose the plotComposite.
			if (plotContent != null && !plotContent.isDisposed()) {
				disposePlotContent(plotContent);
				plotContent = null;
			}

			// Update the stack layout, putting the infoComposite in front.
			if (stackLayout.topControl != infoContent) {
				stackLayout.topControl = infoContent;
				layout();
			}
		}

		return;
	}

	/**
	 * Sets the current plot associated with this plot composite.
	 * <p>
	 * <b>Note:</b> This method will <i>not</i> refresh the composite
	 * automatically. If the plot changes, the calling code should call
	 * {@link #refresh()} afterward.
	 * </p>
	 * 
	 * @param plot
	 *            The new plot shown in this composite, or {@code null} to clear
	 *            it.
	 * @return True if the plot changed, false otherwise.
	 */
	public boolean setPlot(IPlot plot) {
		boolean changed = false;

		if (plot != this.plot) {
			changed = true;

			// Unregister from the old plot.

			// Register with the new plot.
			this.plot = plot;
		}

		return changed;
	}

	/**
	 * Shows the specified series. Sub-classes should override this method to
	 * handle actually rendering the series, but they <b><i>must call the super
	 * method last after any exceptions are thrown</i></b>.
	 * 
	 * @param series
	 *            The series to add.
	 * @throws Exception
	 *             If the series could not be added.
	 */
	protected void showSeries(ISeries series) throws Exception {
		plottedSeries.add(series);
	}

	/**
	 * Whether or not the {@link #toolBar} for the plot composite should be
	 * shown.
	 * 
	 * @return True if the {@code ToolBar} should be visible, false otherwise.
	 */
	protected boolean showToolBar() {
		return true;
	}

	protected void updateInfoContent(Composite infoContent, String message) {
		// Set the message and icon based on the state of the connection.
		final Display display = infoContent.getDisplay();
		// If there's no icon set, default to something useful.
		final Image image = (infoIcon != null ? infoIcon
				: display.getSystemImage(SWT.ICON_WARNING));

		// Update the contents of the infoComposite's widgets.
		iconLabel.setImage(image);
		msgLabel.setText(message);

		// Force the StackLayout to refresh. We need the two boolean flags so
		// that the text will wrap properly.
		layout(true, true);

		return;
	}

	protected void updatePlotContent(Composite plotContent) throws Exception {

		Queue<ISeries> addedSeries = new LinkedList<ISeries>();
		Set<ISeries> removedSeries = new HashSet<ISeries>(plottedSeries);

		// Determine all added series and hide any removed series.
		for (String category : plot.getCategories()) {
			for (ISeries series : plot.getAllDependentSeries(category)) {
				// If the series is enabled and is not plotted, we will need to
				// show it.
				if (series.enabled()) {
					if (!removedSeries.remove(series)) {
						addedSeries.add(series);
					}
				}
				// If the series is not enabled and is plotted, we need to hide
				// it.
				else if (plottedSeries.contains(series)) {
					hideSeries(series);
				}
			}
		}

		// For all series not in the plot, we need to hide them.
		for (ISeries series : removedSeries) {
			if (plottedSeries.contains(series)) {
				hideSeries(series);
			}
		}

		// For all series that are enabled but not in the plot, we need to show
		// them.
		ISeries series = addedSeries.poll();
		while (series != null) {
			showSeries(series);
			series = addedSeries.poll();
		}

		return;
	}

}
