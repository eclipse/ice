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
package org.eclipse.ice.client.widgets.reactoreditor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.reactor.pwr.PressurizedWaterReactor;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * An AnalysisToolComposite provides an SWT Composite built to contain multiple
 * {@link IAnalysisView}. It provides three tools: an IAnalysisView selector, a
 * ToolBar for quick access to IAnalysisView actions, and a space for displaying
 * the contents of the IAnalysisView.<br>
 * <br>
 * This class is not restricted to any particular type of analysis. Instead, it
 * gets IAnalysisViews by querying an {@link IAnalysisWidgetRegistry} for an
 * {@link IAnalysisWidgetFactory} based on the input data's class.<br>
 * <br>
 * For communication between IAnalysisViews in this or other
 * AnalysisToolComposites, it passes each of its views a {@link StateBroker}.
 * 
 * @author djg
 * 
 */
public class AnalysisToolComposite extends Composite {

	/**
	 * The StateBroker is used to share "selection" events, e.g., the currently
	 * selected fuel assembly from the input data, between
	 * AnalysisToolComposites and their child IAnalysisViews.
	 */
	private StateBroker broker;

	/**
	 * The ISelectionProvider is used to feed the current selection to the
	 * Eclipse Workbench. This is necessary for properties to be sent to the ICE
	 * Properties View.
	 */
	private final ISelectionProvider selectionProvider;

	/**
	 * This registry is for available IAnalysisWidgetFactory instances. It is
	 * used to construct the set of IAnalysisViews based on the data's class set
	 * with {@linkplain AnalysisToolComposite#setData(String, Object)}.
	 */
	private final IAnalysisWidgetRegistry registry;

	/**
	 * This maps a dataSource to a particular factory based on the model class.
	 */
	private final Map<DataSource, IAnalysisWidgetFactory> factoryMap;

	/**
	 * This maps dataSource-viewName to an object containing information we need
	 * to access everything related to a particular view.
	 */
	private final Map<String, ViewPart> viewPartMap;

	/* ---- GUI Components ---- */
	// More information on the overall layout of the ATC can be found in the
	// layoutComposites() method.

	/**
	 * The left Composite that contains the ToolBar's primary actions.
	 */
	private final Composite leftToolBarComposite;
	/**
	 * The StackLayout of ToolBars in the
	 * {@link AnalysisToolComposite#leftToolBarComposite leftToolBarComposite}.
	 * This needs to be updated whenever the current View changes.
	 */
	private final StackLayout leftToolBarStack;
	/**
	 * An empty ToolBar that will be displayed in the
	 * {@link AnalysisToolComposite#leftToolBarComposite leftToolBarComposite}
	 * when there is no active View. In that case, set it as the
	 * {@link AnalysisToolComposite#leftToolBarStack
	 * leftToolBarStack.topControl}.
	 */
	private final ToolBar leftToolBarHolder;
	/**
	 * A ToolBar that contains the View Menu used to select between
	 * IAnalysisViews for each data source.
	 */
	private final ToolBar rightToolBar;
	/**
	 * The Composite that contains the contents of the IAnalysisViews.
	 */
	private final Composite viewComposite;
	/**
	 * The StackLayout of IAnalysisViews in the
	 * {@link AnalysisToolComposite#viewComposite viewComposite}. This needs to
	 * be updated whenever the current view changes.
	 */
	private final StackLayout viewCompositeStack;
	/**
	 * An empty Composite that will be displayed in the
	 * {@link AnalysisToolComposite#viewComposite viewComposite} when there is
	 * no active View. In that case, set it as the
	 * {@link AnalysisToolComposite#viewCompositeStack
	 * viewCompositeStack.topControl}.
	 */
	private final Composite viewCompositeHolder;
	/**
	 * A Map of the MenuItems in the View Menu keyed on the data source.
	 */
	private final Map<DataSource, MenuItem> dataSourceItems;

	/* ------------------------ */

	/**
	 * The default constructor for an {@link AnalysisToolComposite} (or ATC).
	 * 
	 * @param parent
	 *            The Composite that will contain this ATC.
	 * @param broker
	 *            The StateBroker used for inter-{@link IAnalysisView}
	 *            communications.
	 * @param registry
	 *            The {@link IAnalysisWidgetRegistry} used to get the
	 *            appropriate IAnalysisViews.
	 * @param name
	 *            The name of this ATC.
	 */
	public AnalysisToolComposite(Composite parent, StateBroker broker,
			IAnalysisWidgetRegistry registry,
			ISelectionProvider selectionProvider) {
		super(parent, SWT.NONE);

		// Initialize the variables we need to keep track of.
		this.broker = broker;
		this.selectionProvider = selectionProvider;
		this.registry = registry;

		// Get the available data sources.
		DataSource[] dataSources = DataSource.values();

		// Initialize the Maps.
		factoryMap = new HashMap<DataSource, IAnalysisWidgetFactory>(
				dataSources.length);
		viewPartMap = new HashMap<String, ViewPart>();
		dataSourceItems = new HashMap<DataSource, MenuItem>(dataSources.length);

		// Initialize the leftToolBar container and contents.
		leftToolBarComposite = new Composite(this, SWT.NONE);
		leftToolBarStack = new StackLayout();
		leftToolBarHolder = new ToolBar(leftToolBarComposite, SWT.HORIZONTAL);

		// Initialize the rightToolBar container and contents.
		rightToolBar = new ToolBar(this, SWT.HORIZONTAL);

		// Initialize the viewComposite and contents.
		viewComposite = new Composite(this, SWT.NONE);
		viewCompositeStack = new StackLayout();
		viewCompositeHolder = new Composite(viewComposite, SWT.NONE);

		// Create the basic menu for view selection.
		createViewMenu();

		// Lay out everything.
		layoutComposites();

		return;
	}

	/**
	 * Creates the Menu that stores each of the available IAnalysisViews for
	 * each of the data sources.
	 */
	private void createViewMenu() {
		// Instantiate viewMenu. POP_UP menus are used for context menus and
		// drop-downs from ToolItems.
		Menu menu = new Menu(this.getShell(), SWT.POP_UP);

		// Create the top-level of the view menu with a button for each possible
		// data source.
		for (DataSource dataSource : DataSource.values()) {
			// Create a disabled MenuItem with dataSource as its text.
			MenuItem item = new MenuItem(menu, SWT.CASCADE);
			item.setText(dataSource.toString());
			item.setEnabled(false);

			// Create the sub-menu that will show the view types for this data
			// source.
			Menu subMenu = new Menu(menu);
			item.setMenu(subMenu);

			// Put the MenuItem into a Map for quick access (it will need to be
			// enabled/disabled depending on whether or not there are views
			// available for the data source.
			dataSourceItems.put(dataSource, item);
		}

		// Add the view ToolItem that should make the view menu appear.
		final ToolItem viewItem = new ToolItem(rightToolBar, SWT.DROP_DOWN);
		viewItem.setText("Views");
		viewItem.setToolTipText("Select the view displayed below.");

		// We also need to add a listener to make viewMenu appear.
		viewItem.addListener(SWT.Selection, new ToolItemMenuListener(viewItem,
				menu));

		return;
	}

	/**
	 * Creates the IAnalysisViews for a data source.
	 * 
	 * @param dataSource
	 *            The data source that needs new IAnalysisViews.
	 * @param factory
	 *            The factory used to get the allowed IAnalysisViews.
	 */
	private void createViews(DataSource dataSource,
			IAnalysisWidgetFactory factory) {
		System.out
				.println("AnalysisToolComposite message: Creating views for data source "
						+ dataSource);

		// Get the list of available views.
		List<String> viewNames = factory.getAvailableViews(dataSource);

		// Enable the dataSource's view menu Item if possible. (The dataSource
		// should *always* be a key in the map after createViewMenu is called in
		// the constructor.)
		MenuItem dataSourceItem = dataSourceItems.get(dataSource);
		dataSourceItem.setEnabled(!viewNames.isEmpty());
		Menu dataSourceMenu = dataSourceItem.getMenu();

		// Create a ViewPart and a view menu item for each view.
		for (String viewName : viewNames) {
			// Get the key for this dataSource + view.
			final String key = dataSource + "-" + viewName;

			/* ---- Add a MenuItem for view to dataSource's view Menu. ---- */
			// Create the menu item for selecting this view.
			final MenuItem item = new MenuItem(dataSourceMenu, SWT.PUSH);
			item.setText(viewName);

			// Add a listener to call setActiveView with the key.
			item.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Tell the ATC to switch to this view.
					AnalysisToolComposite.this.setActiveView(key);
				}

				@Override
				public void widgetDefaultSelected(SelectionEvent e) {
					// Calls to this function are platform-dependent.
					// It should do the same thing as the other function.
					widgetSelected(e);
				}
			});
			/* ------------------------------------------------------------ */

			/* ---- Add a ViewPart for the view. ---- */
			// Create the container Composite for the new view.
			Composite container = new Composite(viewComposite, SWT.NONE);
			container.setBackground(getBackground());

			// Create the ToolBar for the new view.
			ToolBar toolBar = new ToolBar(leftToolBarComposite, SWT.HORIZONTAL);
			toolBar.setBackground(getBackground());

			// Create the IAnalysisView from the factory.
			IAnalysisView view = factory.createView(viewName, dataSource);

			// Get the view to fill out its container and its ToolBar.
			view.createViewContent(container);
			view.getToolBarContributions(toolBar);

			// Register the view with the broker.
			view.setBroker(broker);

			// Set the selection provider to receive selections from the view.
			view.setSelectionProvider(selectionProvider);

			// Put the information for this view into the map of ViewParts.
			viewPartMap.put(key, new ViewPart(view, container, toolBar));
			/* -------------------------------------- */
		}

		// If there is no active view visible and we have views available for
		// this data source, activate the first available view.
		if ((viewCompositeStack.topControl == null || viewCompositeStack.topControl
				.isDisposed()) && !viewNames.isEmpty()) {
			setActiveView(dataSource + "-" + viewNames.get(0));
		}
		return;
	}

	/**
	 * Sets the currently-active IAnalysisView based on its key in the
	 * {@link AnalysisToolComposite#viewPartMap viewPartMap}.
	 * 
	 * @param key
	 *            The key of the IAnalysisView that will be displayed.
	 */
	private void setActiveView(String key) {
		System.out
				.println("AnalysisToolComposite message: Setting active view to "
						+ key);

		// Fetch the view part using the key.
		ViewPart viewPart = viewPartMap.get(key);

		// Make the view's ToolBar the top control in the ToolBar Composite. If
		// the view has destroyed it, bring the blank placeholder to the top.
		ToolBar toolBar = viewPart.getToolBar();
		if (toolBar == null) {
			toolBar = leftToolBarHolder;
		}
		leftToolBarStack.topControl = toolBar;
		leftToolBarComposite.layout();

		// Make the view's container the top control in the view Composite. If
		// the view has destroyed it, bring the blank placeholder to the top.
		Composite container = viewPart.getContainer();
		if (container == null) {
			container = viewCompositeHolder;
		}
		viewCompositeStack.topControl = container;
		viewComposite.layout();

		return;
	}

	/**
	 * Removes and disposes of all of the IAnalysisViews for a particular
	 * DataSource.
	 * 
	 * @param dataSource
	 *            The DataSource whose Views need to be removed.
	 * @param factory
	 *            The factory used to generate the IAnalysisViews for the
	 *            DataSource.
	 */
	private void removeViews(DataSource dataSource,
			IAnalysisWidgetFactory factory) {
		System.out
				.println("AnalysisToolComposite message: Removing views for data source "
						+ dataSource);

		// Get the list of available views.
		List<String> viewNames = factory.getAvailableViews(dataSource);

		// Disable the dataSource's view menu Item. (The dataSource
		// should *always* be a key in the map.)
		MenuItem dataSourceItem = dataSourceItems.get(dataSource);
		dataSourceItem.setEnabled(false);

		// Dispose of all MenuItems from the dataSource's view Menu.
		Menu dataSourceMenu = dataSourceItem.getMenu();
		for (MenuItem item : dataSourceMenu.getItems()) {
			item.dispose();
		}

		// Dispose of the ViewPart for each view.
		for (String viewName : viewNames) {
			System.out.println("AnalysisToolComposite message: Removing view "
					+ viewName);

			ViewPart viewPart = viewPartMap.remove(dataSource + "-" + viewName);
			if (viewPart != null) {
				viewPart.dispose();
			}
		}

		return;
	}

	/**
	 * Sets the current View to the first available view for the specified
	 * DataSource.
	 * 
	 * @param dataSource
	 *            The DataSource whose first available View will be displayed.
	 */
	public void setToFirstView(DataSource dataSource) {
		// Get the view menu's MenuItem associated with this data source.
		MenuItem dataSourceItem = dataSourceItems.get(dataSource);

		// Only proceed if the item exists.
		if (dataSourceItem != null) {

			// Get the Menu of views for this data source.
			Menu menu = dataSourceItem.getMenu();

			// Only proceed if the menu actually has items.
			if (menu.getItemCount() > 0) {

				// Get the view name of the first item.
				String viewName = menu.getItem(0).getText();

				// Set the active view.
				setActiveView(dataSource + "-" + viewName);
			}
		}

		return;
	}

	/**
	 * Lays out everything in this ATC.
	 */
	private void layoutComposites() {
		/*
		 * The topmost layout is a GridLayout with two columns. In the first row
		 * is the leftToolBarComposite, which grabs all excess horizontal space.
		 * Next to it is the rightToolBarComposite, which contains the View
		 * selection Menu.
		 * 
		 * In the next row below the ToolBars is the viewComposite. It spans
		 * both columns and grabs all excess horizontal and vertical space.
		 */

		// Set the overall layout.
		this.setLayout(new GridLayout(2, false));

		// The leftToolBarComposite grabs excess horizontal space in its row in
		// the overall layout. It has a StackLayout, with the topControl set to
		// the holder or the ToolBar for the current View.
		leftToolBarComposite.setLayoutData(new GridData(
				GridData.FILL_HORIZONTAL));
		leftToolBarComposite.setLayout(leftToolBarStack);
		leftToolBarComposite.layout();

		// The rightToolBar fills in only the space it needs in the first row of
		// the GridLayout.
		rightToolBar.setLayoutData(new GridData());

		// The viewComposite takes all extra space in the overall GridLayout. It
		// has a StackLayout, with the topControl set to the holder or the
		// Composite for the current View.
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		viewComposite.setLayoutData(gridData);
		viewComposite.setLayout(viewCompositeStack);
		viewComposite.layout();

		return;
	}

	/**
	 * The idea behind overriding this function is that SWT Composites provide a
	 * built-in map in which to store key-value pairs. We can use this to store
	 * input, reference, and comparison data structures and/or to pass it on to
	 * child AVCs.
	 */
	@Override
	public void setData(String key, Object value) {
		// This method creates the AnalysisViews based on the type of object
		// that is coming into the AnalysisToolComposite.

		// Get the DataSource enum value from the key.
		DataSource dataSource = DataSource.valueOf(key);

		// Make sure the key and value are valid.
		if (!dataSourceItems.containsKey(dataSource) || (value == null)) {
			return;
		}

		// Get the factory for the data object's class.
		// FIXME - For the moment, we relegate all Comparison data to the LWR
		// analysis package.
		IAnalysisWidgetFactory factory;
		if (dataSource == DataSource.Comparison) {
			factory = registry
					.getAnalysisWidgetFactory(PressurizedWaterReactor.class);
		} else {
			factory = registry.getAnalysisWidgetFactory(value.getClass());
		}

		// Make sure the registry has a factory for this data object.
		if (factory == null) {
			return;
		}

		System.out.println("AnalysisToolComposite message: " + "Data for "
				+ dataSource.toString() + " set!");

		// See if we need to create new views.
		if (factory != factoryMap.get(dataSource)) {
			IAnalysisWidgetFactory oldFactory = factoryMap.put(dataSource,
					factory);

			// Remove any old views created with the old factory.
			if (oldFactory != null) {
				removeViews(dataSource, oldFactory);
			}
			// Create new views with the new factory.
			createViews(dataSource, factory);
		}

		// Set the data on all of the child views. None of these getters should
		// ever return null.
		List<String> viewNames = factory.getAvailableViews(dataSource);
		for (String viewName : viewNames) {
			viewPartMap.get(key + "-" + viewName).getView().setData(key, value);
		}

		return;
	}

	/**
	 * Sets the StateBroker for the AnalysisToolComposite and all child
	 * {@link IAnalysisView}s.
	 * 
	 * @param broker
	 *            The new StateBroker.
	 */
	public void setStateBroker(StateBroker broker) {

		// If the new broker is not null, update the broker and the views.
		if (broker != null) {
			this.broker = broker;

			// Update the broker for all of the IAnalysisViews.
			for (ViewPart viewPart : viewPartMap.values()) {
				viewPart.getView().setBroker(broker);
			}
		}

		return;
	}

	@Override
	public void setBackground(Color color) {
		super.setBackground(color);

		// Make sure the color is not null. If it is, default to white.
		color = (color != null ? color : new Color(Display.getCurrent(), 255,
				255, 255));

		// Set the color for all child Composites.
		if (getChildren().length > 1) {
			leftToolBarComposite.setBackground(color);
			leftToolBarHolder.setBackground(color);
			rightToolBar.setBackground(color);
			viewComposite.setBackground(color);
			viewCompositeHolder.setBackground(color);

			for (ViewPart view : viewPartMap.values()) {
				view.container.setBackground(color);
				view.toolBar.setBackground(color);
			}
		}

		return;
	}

	/**
	 * This class is a simple wrapper for an IAnalysisView, its container
	 * Composite (a child of the {@link AnalysisToolComposite#viewComposite
	 * viewComposite}), and its ToolBar. Its use is limited to the ATC class.
	 * 
	 * @author djg
	 * 
	 */
	public class ViewPart {
		/**
		 * An IAnalysisView.
		 */
		private final IAnalysisView view;
		/**
		 * The IAnalysisView's container.
		 */
		private final Composite container;
		/**
		 * The IAnalysisView's ToolBar.
		 */
		private final ToolBar toolBar;

		/**
		 * The default constructor.
		 * 
		 * @param view
		 *            An IAnalysisView.
		 * @param container
		 *            The IAnalysisView's container.
		 * @param toolBar
		 *            The IAnalysisView's ToolBar.
		 */
		public ViewPart(IAnalysisView view, Composite container, ToolBar toolBar) {
			this.view = view;
			this.container = container;
			this.toolBar = toolBar;
			return;
		}

		/**
		 * Gets the wrapped IAnalysisView.
		 * 
		 * @return An IAnalysisView.
		 */
		public IAnalysisView getView() {
			return view;
		}

		/**
		 * Gets the wrapped IAnalysisView's container.
		 * 
		 * @return A Composite.
		 */
		public Composite getContainer() {
			return container;
		}

		/**
		 * Gets the wrapped IAnalysisView's ToolBar.
		 * 
		 * @return A ToolBar.
		 */
		public ToolBar getToolBar() {
			return toolBar;
		}

		/**
		 * Disposes the wrapped IAnalysisView, its container, and its ToolBar.
		 */
		public void dispose() {
			view.dispose();
			if (!container.isDisposed()) {
				container.dispose();
			}
			if (!toolBar.isDisposed()) {
				toolBar.dispose();
			}
			return;
		}
	}
}
