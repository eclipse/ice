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
package org.eclipse.ice.viz.service;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class manages a single rendering of an {@link IPlot}.
 * <p>
 * After creating a {@code PlotRender}, its content will need to be created via
 * {@link #createBasicContent()}. It can be updated later by calling
 * {@link #refresh()}.
 * </p>
 * <p>
 * Sub-classes should implement the required methods to populate/update the
 * {@link #plotComposite} and may also override the methods that populate/update
 * the {@link #infoComposite} to add extra informational features as necessary.
 * </p>
 * 
 * @author Jordan
 *
 */
@Deprecated
public abstract class PlotRender {

	/**
	 * Logger for handling event messages and other information.
	 */
	protected final Logger logger;

	// TODO We may want to add a ToolBar

	/**
	 * The parent {@code Composite} that contains the plot render.
	 */
	public final Composite parent;
	/**
	 * The rendered {@code IPlot}. This cannot be changed.
	 */
	public final IPlot plot;

	/**
	 * The current plot category.
	 */
	private String category;
	/**
	 * The current plot type.
	 */
	private String type;

	// ---- UI Widgets ---- //
	/**
	 * This composite contains the {@link #plotComposite} and
	 * {@link #infoComposite} in a stack.
	 */
	private Composite stackComposite = null;
	/**
	 * The current widget used to draw the plot.
	 */
	private Composite plotComposite = null;
	/**
	 * This presents the user with helpful information about the status of the
	 * plot. It should only be visible if the plot cannot be drawn.
	 */
	private Composite infoComposite = null;
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

	/**
	 * The plot render's context Menu.
	 */
	private Menu contextMenu;
	// -------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that contains the plot render.
	 * @param plot
	 *            The rendered {@code IPlot}. This cannot be changed.
	 */
	public PlotRender(Composite parent, IPlot plot) {

		// Create the logger
		logger = LoggerFactory.getLogger(PlotRender.class);

		// Check the parameters.
		if (parent == null || plot == null) {
			throw new NullPointerException("PlotRender error: "
					+ "Cannot render a plot that is null or "
					+ "inside a null parent Composite.");
		}

		this.parent = parent;
		this.plot = plot;

		return;
	}

	/**
	 * Clears any cached meta data used to speed up the refresh operation.
	 */
	protected abstract void clearCache();

	/**
	 * Creates the most basic content for the {@code PlotRender}.
	 * <p>
	 * <b>Note:</b> This method should <b>only</b> called from the <i>UI
	 * thread</i> ONCE via {@link #refresh()}.
	 * </p>
	 */
	private void createBasicContent() throws SWTException {

		// Create the container for the info and plot Composites.
		stackComposite = new Composite(parent, SWT.NONE);
		stackComposite.setFont(parent.getFont());
		stackComposite.setBackground(parent.getBackground());
		stackComposite.setLayout(new StackLayout());

		// Set the stackComposite's context Menu to the parent's.
		stackComposite.setMenu(parent.getMenu());

		// Get the plot render actions.
		List<ActionTree> actions = createPlotRenderActions();

		// Create the context MenuManager and create its Menu.
		final MenuManager menuManager = new MenuManager();
		for (ActionTree action : actions) {
			menuManager.add(action.getContributionItem());
		}
		// Get the current context Menu from the parent of the plot Composite.
		contextMenu = stackComposite.getParent().getMenu();
		// If it exists, it should be using a MenuManager. However, we cannot
		// add new actions to the MenuManager (there is no way to get it), so we
		// must add a MenuListener to add additional items when the menu opens.
		if (contextMenu != null) {
			contextMenu.addMenuListener(new MenuListener() {
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
			contextMenu = menuManager.createContextMenu(parent);
		}

		return;
	}

	/**
	 * Creates the informational {@code Composite} that is shown when an
	 * exception occurs during calls to {@link #updatePlotComposite(Composite)}.
	 * <p>
	 * The default {@link #infoComposite} contains an icon label to the left and
	 * a {@code Composite} on the right with a default {@code GridLayout} and a
	 * message label. The label will show the message from the plot update
	 * exception.
	 * </p>
	 * <p>
	 * <b>Note:</b> If overridden, be sure to either call this super method or
	 * also override {@link #updateInfoComposite(Composite, String)}.
	 * </p>
	 * 
	 * @param parent
	 *            The parent in which the info {@code Composite} should be
	 *            created.
	 * @param style
	 *            The style to use for the info {@code Composite}.
	 * @return The new info {@code Composite}.
	 */
	protected Composite createInfoComposite(Composite parent, int style) {

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

	/**
	 * Creates the plot {@code Composite} that is shown when the associated
	 * {@link #plot}, {@link #category}, and {@link #type} are all valid.
	 * 
	 * @param parent
	 *            The parent in which the plot {@code Composite} should be
	 *            created.
	 * @param style
	 *            The style to use for the plot {@code Composite}.
	 * @return The new plot {@code Composite}.
	 * @throws Exception
	 *             If the plot is in an invalid state or otherwise cannot be
	 *             rendered, this throws an exception with an informative
	 *             message.
	 */
	protected abstract Composite createPlotComposite(Composite parent,
			int style) throws Exception;

	/**
	 * Gets the list of default actions that can be performed on the plot
	 * render. This list can be used to populate context Menus or ToolBars for
	 * the render's container.
	 * 
	 * @return A list of default actions.
	 */
	protected List<ActionTree> createPlotRenderActions() {
		List<ActionTree> actions = new ArrayList<ActionTree>();

//		// Create the root ActionTree for setting the plot category and type.
//		final ActionTree plotTypeTree = new ActionTree("Set Plot Type");
//		actions.add(plotTypeTree);
//		try {
//			// Add an ActionTree for each category, and then add ActionTree
//			// leaf nodes for each type.
//			Map<String, String[]> plotTypes = plot.getPlotTypes();
//			for (Entry<String, String[]> entry : plotTypes.entrySet()) {
//				String category = entry.getKey();
//				String[] types = entry.getValue();
//
//				if (category != null && types != null && types.length > 0) {
//					// Create the category ActionTree.
//					ActionTree categoryTree = new ActionTree(category);
//					plotTypeTree.add(categoryTree);
//
//					// Add all types to the category ActionTree. Each Action
//					// should try to set the plot category and type of the drawn
//					// plot.
//					final String categoryRef = category;
//					for (String type : types) {
//						final String typeRef = type;
//						categoryTree.add(new ActionTree(new Action(type) {
//							@Override
//							public void run() {
//								setPlotCategory(categoryRef);
//								setPlotType(typeRef);
//								refresh();
//							}
//						}));
//					}
//				}
//			}
//		} catch (Exception e) {
//			// Print out the error message (from getPlotTypes()).
//			logger.error(getClass().getName() + " Exception! ", e);
//		}

		return actions;
	}

	/**
	 * Disposes the specified info {@code Composite} and any related resources.
	 * 
	 * @param infoComposite
	 *            The info {@code Composite} to dispose.
	 */
	protected void disposeInfoComposite(Composite infoComposite) {
		infoComposite.dispose();
		iconLabel = null;
		msgLabel = null;
	}

	/**
	 * Disposes the specified plot {@code Composite} and any related resources.
	 * 
	 * @param plotComposite
	 *            The plot {@code Composite} to dispose.
	 */
	protected void disposePlotComposite(Composite plotComposite) {
		// Nothing to do.
	}

	/**
	 * Gets the context menu for the plot {@code Composite}.
	 * 
	 * @return The MenuManager used to populate the plot {@code Composite}'s
	 *         context menu.
	 */
	protected Menu getContextMenu() {
		return contextMenu;
	}

	/**
	 * Gets the current plot category.
	 * 
	 * @return The current plot category.
	 */
	public String getPlotCategory() {
		return category;
	}

	/**
	 * Gets the current plot type.
	 * 
	 * @return The current plot type.
	 */
	public String getPlotType() {
		return type;
	}

	/**
	 * This method updates the UI widgets based on the current settings. It
	 * should be called whenever the UI needs to be updated in any way. It is
	 * the same as calling {@link #refresh(boolean) refresh(false)}.
	 * <p>
	 * This will either immediately update the UI or trigger an asynchronous
	 * update to the UI on the display's UI thread.
	 * </p>
	 */
	public void refresh() {
		refresh(false);
	}

	/**
	 * This method updates the UI widgets based on the current settings. It
	 * should be called whenever the UI needs to be updated in any way.
	 * <p>
	 * This will either immediately update the UI or trigger an asynchronous
	 * update to the UI on the display's UI thread.
	 * </p>
	 * 
	 * @param clearCache
	 *            Some sub-classes may cache certain meta data so that the
	 *            refresh operation is faster. If true, this parameter causes
	 *            the cached information to be rebuilt at the next available
	 *            opportunity.
	 */
	public void refresh(boolean clearCache) {

		// If necessary, clear the cached information.
		if (clearCache) {
			clearCache();
		}

		// If we are not on the UI thread, update the UI asynchronously on the
		// UI thread.
		if (Display.getCurrent() == null) {
			parent.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					refreshUI();
				}
			});
		}
		// If we are on the UI thread, update the UI synchronously.
		else {
			refreshUI();
		}

		return;
	}

	/**
	 * This method updates the UI contributions for this plot. This method
	 * should <b>only</b> called from the <i>UI thread</i> via
	 * {@link #refresh()}.
	 */
	private void refreshUI() {
		// Create the basic content if necessary.
		if (stackComposite == null) {
			createBasicContent();
		}

		// Get the StackLayout from the plot Composite.
		final StackLayout stackLayout = (StackLayout) stackComposite
				.getLayout();

		try {
			// Update the plotComposite. Create it if necessary.
			if (plotComposite == null) {
				plotComposite = createPlotComposite(stackComposite, SWT.NONE);
				plotComposite.setMenu(contextMenu);
			}
			updatePlotComposite(plotComposite);

			// If the plotComposite was successfully updated, we can dispose the
			// infoComposite.
			if (infoComposite != null && !infoComposite.isDisposed()) {
				disposeInfoComposite(infoComposite);
				infoComposite = null;
			}

			// Update the stack layout, putting the plotComposite in front.
			if (stackLayout.topControl != plotComposite) {
				stackLayout.topControl = plotComposite;
				stackComposite.layout();
			}
		} catch (Exception e) {
			// Update the infoComposite. Create it if necessary.
			if (infoComposite == null) {
				infoComposite = createInfoComposite(stackComposite, SWT.NONE);
			}
			updateInfoComposite(infoComposite, e.getMessage());

			// Dispose the plotComposite.
			if (plotComposite != null && !plotComposite.isDisposed()) {
				disposePlotComposite(plotComposite);
				plotComposite = null;
			}

			// Update the stack layout, putting the infoComposite in front.
			if (stackLayout.topControl != infoComposite) {
				stackLayout.topControl = infoComposite;
				stackComposite.layout();
			}
		}

		return;
	}

	/**
	 * Sets the current plot category.
	 * <p>
	 * <b>Note:</b> A subsequent call to {@link #refresh()} will be necessary to
	 * sync the UI with this call's changes.
	 * </p>
	 * 
	 * @param category
	 *            The new plot category.
	 */
	public void setPlotCategory(String category) {
		this.category = category;
	}

	/**
	 * Sets the current plot type.
	 * <p>
	 * <b>Note:</b> A subsequent call to {@link #refresh()} will be necessary to
	 * sync the UI with this call's changes.
	 * </p>
	 * 
	 * @param type
	 *            The new plot type.
	 */
	public void setPlotType(String type) {
		this.type = type;
	}

	/**
	 * Updates the information contained in the specified informational
	 * {@code Composite}.
	 * 
	 * 
	 * @param infoComposite
	 *            The info {@code Composite} to update.
	 * @param message
	 *            The message to display in its message label.
	 */
	protected void updateInfoComposite(Composite infoComposite,
			final String message) {
		// Set the message and icon based on the state of the connection.
		final Display display = infoComposite.getDisplay();
		// If there's no icon set, default to something useful.
		final Image image = (infoIcon != null ? infoIcon
				: display.getSystemImage(SWT.ICON_WARNING));

		// Update the contents of the infoComposite's widgets.
		iconLabel.setImage(image);
		msgLabel.setText(message);

		// Force the StackLayout to refresh. We need the two boolean flags so
		// that the text will wrap properly.
		stackComposite.layout(true, true);

		return;
	}

	/**
	 * Updates the plot rendering contained in the specified plot
	 * {@code Composite}.
	 * 
	 * @param plotComposite
	 *            The plot {@code Composite} to update.
	 * @throws Exception
	 *             If the plot is in an invalid state or otherwise cannot be
	 *             rendered, this throws an exception with an informative
	 *             message.
	 */
	protected abstract void updatePlotComposite(Composite plotComposite)
			throws Exception;

}
