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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.datastructures.VizActionTree;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.Window;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a base class for creating customized renderings of {@link IPlot}s.
 * <p>
 * Certain methods are suggested for sub-classes to override, in particular the
 * following methods:
 * </p>
 * <ol>
 * <li>{@link #createPlotContent(Composite, int)} - fill the composite with
 * customized content</li>
 * <li>{@link #updatePlotContent(Composite)} - refresh the customized content</li>
 * <li>{@link #disposePlotContent(Composite)} - clear the customized content</li>
 * <li>{@link #showSeries(ISeries)} - add the selected series to the custom
 * content</li>
 * <li>{@link #hideSeries(ISeries)} - remove the series from the custom content</li>
 * </ol>
 * <p>
 * The life-cycle of these method calls can be found in
 * {@link #performRefresh()}, and each method may be called multiple times.
 * </p>
 * <p>
 * Additional room for customization includes a context (right-click) menu that
 * can be added to nested composites via {@link #addContextMenu(Composite)}. The
 * context menu's content can be updated or replaced by overriding
 * {@link #getPlotActions()}.
 * </p>
 * <p>
 * Another simple customization includes the ability to prohibit multiple series
 * from being selected by overriding {@link #canShowMultipleSeries()}.
 * </p>
 * 
 * @author Jordan
 *
 */
public abstract class PlotComposite extends Composite {

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(PlotComposite.class);

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
	private List<VizActionTree> plotActions;

	/**
	 * A {@code Composite} containing the rendered {@link #plot}.
	 */
	private Composite plotContent;

	/**
	 * A map of all currently plotted series.
	 */
	private final Set<ISeries> plottedSeries;

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

	/**
	 * Whether or not multiple series can be selected.
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to be overridden.
	 * </p>
	 * 
	 * @return True (default) if multiple series can be rendered at any given
	 *         time, false otherwise.
	 */
	protected boolean canShowMultipleSeries() {
		return true;
	}

	/**
	 * Creates a context menu populated from {@link #getPlotActions()} in the
	 * specified parent composite. The menu is <i>not</i> set on the parent.
	 * 
	 * @param parent
	 *            The parent composite.
	 * @return The new context Menu.
	 */
	protected Menu createContextMenu(Composite parent) {
		// Create the context MenuManager and create its Menu.
		final MenuManager menuManager = new MenuManager();
		for (VizActionTree action : plotActions) {
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

	/**
	 * Creates holders for informational content to be displayed when either
	 * {@link #createPlotContent(Composite, int)} or
	 * {@link #updatePlotContent(Composite)} throws an exception signifying that
	 * the customized content could not be created/updated.
	 * 
	 * @param parent
	 *            The parent composite in which to create the informational
	 *            content.
	 * @param style
	 *            The style for the <i>outermost</i> composite containing the
	 *            info content.
	 * @return The <i>outermost</i> composite containing the info content.
	 */
	protected Composite createInfoContent(Composite parent, int style) {

		Composite infoComposite = new Composite(parent, style);
		infoComposite.setLayout(new GridLayout(2, false));

		// Create an info label with an image.
		iconLabel = new Label(infoComposite, SWT.NONE);
		iconLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
				false, false));

		// Create a Composite to contain the info message.
		Composite msgComposite = new Composite(infoComposite, SWT.NONE);
		msgComposite.setLayoutData(new GridData(SWT.BEGINNING, SWT.BEGINNING,
				false, false));
		msgComposite.setLayout(new GridLayout(1, false));

		// Create an info label with informative text.
		msgLabel = new Label(msgComposite, SWT.NONE);
		msgLabel.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false,
				false));

		return infoComposite;
	}

	/**
	 * This method creates customized plot content.
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to and <i>should</i> be
	 * overridden.
	 * </p>
	 * 
	 * @param parent
	 *            The parent of the outermost content {@code Composite}.
	 * @param style
	 *            The style for the outermost content {@code Composite}.
	 * @return The <i>outermost</i> plot content {@code Composite}.
	 * @throws Exception
	 *             If there was an error preventing plot content from being
	 *             created.
	 */
	protected Composite createPlotContent(Composite parent, int style)
			throws Exception {
		throw new UnsupportedOperationException(getClass().getName()
				+ " error: "
				+ "The custom plot content method has not been implemented.");
	}

	/**
	 * Creates a ToolBar in the specified parent composite. The bar is populated
	 * using the same actions from {@link #getPlotActions()}.
	 * 
	 * @param parent
	 *            The parent composite that will contain the ToolBar.
	 * @return The new ToolBar.
	 */
	protected ToolBar createToolBar(Composite parent) {
		ToolBar toolBar = null;

		// Create the ToolBarManager and add its content.
		final ToolBarManager toolBarManager = new ToolBarManager();
		for (VizActionTree action : plotActions) {
			toolBarManager.add(action.getContributionItem());
		}

		// Create the ToolBar itself.
		toolBar = toolBarManager.createControl(parent);

		return toolBar;
	}

	/**
	 * Disposes the informational content composite when it is no longer needed.
	 * 
	 * @param infoContent
	 *            The main info content composite that is being disposed.
	 */
	protected void disposeInfoContent(Composite infoContent) {
		infoContent.dispose();
		iconLabel = null;
		msgLabel = null;
	}

	/**
	 * This method disposes customized plot content. This is triggered every
	 * time the content is successfully created in
	 * {@link #createPlotContent(Composite, int)} but failed to update in
	 * {@link #updatePlotContent(Composite)}. Sub-classes should override this
	 * method to handle any additional disposals, but they <b><i>must call the
	 * super method last</i></b>.
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to be overridden.
	 * </p>
	 * 
	 * @param plotContent
	 *            The main plot composite that is being disposed.
	 */
	protected void disposePlotContent(Composite plotContent) {
		plotContent.dispose();
	}

	/**
	 * Gets the current plot associated with this composite.
	 * 
	 * @return The current plot, or {@code null} if it is not set.
	 */
	protected IPlot getPlot() {
		return plot;
	}

	/**
	 * Gets the list of actions supported by this plot composite. These will be
	 * added to the {@link #contextMenu} and the {@link #toolBar} (if enabled).
	 * 
	 * @return A list containing the supported plot actions.
	 */
	protected List<VizActionTree> getPlotActions() {
		List<VizActionTree> actions = new ArrayList<VizActionTree>();

		// Create a dialog that can be used to select available series.
		final TreeSelectionDialogProvider provider;
		provider = new TreeSelectionDialogProvider() {
			@Override
			public Object[] getChildren(Object parent) {
				final Object[] children;
				IPlot plot = getPlot();
				// If the element is the plot itself, return either its
				// categories or, if there is only one category, its list of
				// series.
				if (parent == plot) {
					List<String> categories = plot.getCategories();
					if (categories.size() > 1) {
						children = categories.toArray();
					} else if (!categories.isEmpty()) {
						children = plot.getDependentSeries(categories.get(0))
								.toArray();
					} else {
						children = new Object[0];
					}
				}
				// If the element is a category, return its associated series.
				else if (parent instanceof String) {
					children = plot.getDependentSeries(parent.toString())
							.toArray();
				} else {
					children = new Object[0];
				}
				return children;
			}

			@Override
			public String getText(Object element) {
				// Get the text from the series' label.
				final String text;
				if (element instanceof ISeries) {
					text = ((ISeries) element).getLabel();
				} else {
					text = element.toString();
				}
				return text;
			}

			@Override
			public boolean isSelected(Object element) {
				// Only series should be checked/enabled.
				final boolean selected;
				if (element instanceof ISeries) {
					selected = ((ISeries) element).isEnabled();
				} else {
					selected = false;
				}
				return selected;
			}
		};

		// Configure the dialog's title and message.
		final boolean multi = canShowMultipleSeries();
		if (multi) {
			provider.setTitle("Select Series");
			provider.setMessage("Please select one or more series to plot.");
		} else {
			provider.setTitle("Select a Series");
			provider.setMessage("Please select one series to plot.");
		}

		// Create an action to open the dialog. If any were added or removed
		// from the selection, add or remove them from the plot rendering.
		actions.add(new VizActionTree(new Action("Select series...") {
			@Override
			public void run() {
				if (provider.openDialog(getShell(), getPlot(), multi) == Window.OK) {
					boolean changed = false;
					// Disable all unselected series.
					for (Object element : provider.getUnselectedLeafElements()) {
						if (element instanceof ISeries) {
							try {
								((ISeries) element).setEnabled(false);
								changed = true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					// Enable all selected series.
					for (Object element : provider.getSelectedLeafElements()) {
						if (element instanceof ISeries) {
							try {
								((ISeries) element).setEnabled(true);
								changed = true;
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					// If plots were updated, refresh the composite.
					if (changed) {
						refresh();
					}
				}
				return;
			}
		}));

		// TODO This only covers the case where there is one set of series. We
		// may eventually need code to handle the case where there is one set of
		// possible independent series and a different set of possible dependent
		// ones.
		// If the plot has interchangeable series, add an option to set the independent series.
		if (hasInterchangeableSeries()) {
			// Create an action to open the dialog. If any were added or removed
			// from the selection, add or remove them from the plot rendering.
			actions.add(new VizActionTree(new Action(
					"Set independent series...") {
				@Override
				public void run() {
					if (provider.openDialog(getShell(), getPlot(), false) == Window.OK) {
						boolean changed = false;
						// Enable all selected series.
						for (Object element : provider
								.getAllSelectedLeafElements()) {
							if (element instanceof ISeries) {
								try {
									plot.setIndependentSeries((ISeries) element);
									changed = true;
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						// If plots were updated, refresh the composite.
						if (changed) {
							refresh();
						}
					}
					return;
				}
			}));
		}

		return actions;
	}

	/**
	 * Gets all plotted series. These are series for whom
	 * {@link #showSeries(ISeries)} has been called, but not
	 * {@link #hideSeries(ISeries)}.
	 * 
	 * @return An unmodifiable view of the plotted series.
	 */
	protected Set<ISeries> getPlottedSeries() {
		return Collections.unmodifiableSet(plottedSeries);
	}

	/**
	 * Hides the specified series. Sub-classes should override this method to
	 * handle actually removing the series, but they <b><i>must call the super
	 * method last after any exceptions are thrown</i></b>.
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to be overridden.
	 * </p>
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
	 * Returns whether or not the plot can substitute one of its dependents
	 * series for the independents series and vis versa.
	 * 
	 * @return False (default) if the set of independent and dependent series is
	 *         disjoint. True if they are identical.
	 */
	protected boolean hasInterchangeableSeries() {
		return false;
	}

	/**
	 * Gets whether or not the series is plotted. This is a faster short-cut
	 * than testing containment in the list returned by
	 * {@link #getPlottedSeries()}.
	 * 
	 * @param series
	 *            The series to test.
	 * @return True if the series has been plotted, false otherwise.
	 */
	protected boolean isPlotted(ISeries series) {
		return plottedSeries.contains(series);
	}

	/**
	 * Refreshes the entire composite. This method effectively creates the
	 * {@link #plotContent} or {@link #infoContent} as necessary depending on
	 * whether the plot can be rendered.
	 */
	protected void performRefresh() {
		// Get the StackLayout from the plot Composite.
		StackLayout stackLayout = (StackLayout) getLayout();

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
			// Log the exception that prevented the plot content from being
			// thrown.
			logger.error(getClass().getName() + "message: " + "Exception "
					+ " thrown while creating/refreshing the plot "
					+ "content. Showing info composite instead.", e);

			e.printStackTrace();
			
			// Update the infoComposite. Create it if necessary.
			if (infoContent == null) {
				infoContent = createInfoContent(this, SWT.NONE);
			}

			// Get the message to display
			String message = e.getMessage();
			if (message == null) {
				message = "";
			}

			updateInfoContent(infoContent, message);

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
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to be overridden.
	 * </p>
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
	 * Updates the informational content that is displayed whenever the plot's
	 * customized content cannot be created or updated (in other words, if
	 * either {@link #createPlotContent(Composite, int)} or
	 * {@link #updatePlotContent(Composite)} threw an exception).
	 * 
	 * @param infoContent
	 *            The composite for the informational content.
	 * @param message
	 *            The (error) message to display.
	 */
	protected void updateInfoContent(Composite infoContent, String message) {
		// Set the message and icon based on the state of the connection.
		final Display display = infoContent.getDisplay();
		// If there's no icon set, default to something useful.
		final Image image = (infoIcon != null ? infoIcon : display
				.getSystemImage(SWT.ICON_WARNING));

		// Update the contents of the infoComposite's widgets.

		iconLabel.setImage(image);
		msgLabel.setText(message);

		// Force the StackLayout to refresh. We need the two boolean flags so
		// that the text will wrap properly.
		layout(true, true);

		return;
	}

	/**
	 * This method updates the customized plot content. The default behavior
	 * finds all series whose enabled/disabled state has been changed since the
	 * last time this method was called and calls either
	 * {@link #showSeries(ISeries)} or {@link #hideSeries(ISeries)} to apply the
	 * change. Alternatively, series can be individually updated by setting
	 * their enabled flags and calling show/hide series.
	 * <p>
	 * Sub-classes should override this method <i>if additional code is required
	 * to refresh the custom widgets</i>.
	 * </p>
	 * <p>
	 * <b>Note:</b> This method is <i>intended</i> to be overridden.
	 * </p>
	 * 
	 * @param plotContent
	 *            The composite previously created for the customized plot
	 *            content.
	 * @throws Exception
	 *             If there is an error preventing the customized plot content
	 *             from being updated.
	 */
	protected void updatePlotContent(Composite plotContent) throws Exception {

		// Note that if an exception is thrown while showing/hiding series, it
		// triggers a failure, which exposes the informational composite.
		Queue<ISeries> addedSeries = new LinkedList<ISeries>();
		Set<ISeries> removedSeries = new HashSet<ISeries>(plottedSeries);

		// Determine all added series and hide any removed series.
		for (String category : plot.getCategories()) {
			for (ISeries series : plot.getDependentSeries(category)) {
				// If the series is enabled and is not plotted, we will need to
				// show it.
				if (series.isEnabled()) {
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
