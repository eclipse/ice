/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Jordan Deyton (UT-Battelle, LLC.) - initial API and implementation and/or initial documentation
 *    
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtWidget;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
import org.eclipse.ice.viz.service.connections.IConnectionAdapter;
import org.eclipse.ice.viz.service.visit.widgets.TimeSliderComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

import visit.java.client.ViewerMethods;

public class VisItPlotRender extends ConnectionPlotRender<VisItSwtConnection> {

	/**
	 * A reference to the associated {@code IPlot} implementation. This may be
	 * required for specific implementation details, e.g., for determining the
	 * {@link #representation}.
	 */
	private final VisItPlot plot;

	// TODO This could be moved to the parent class, as the connection adapter
	// may prove useful.
	private final IConnectionAdapter<VisItSwtConnection> adapter;

	/**
	 * 
	 * The current plot representation. This must be pulled from the list of
	 * representations from {@link VisItPlot#getRepresentations(String)}.
	 */
	private String representation;

	/**
	 * The current plot category rendered in the associated rendering widget.
	 * <p>
	 * <b>Note:</b> This value should only be updated when the corresponding UI
	 * piece is updated.
	 * </p>
	 */
	private String plotCategory;
	/**
	 * The current plot representation rendered in the associated rendering
	 * widget.
	 * <p>
	 * <b>Note:</b> This value should only be updated when the corresponding UI
	 * piece is updated.
	 * </p>
	 */
	private String plotRepresentation;
	/**
	 * The current plot type rendered in the associated rendering widget.
	 * <p>
	 * <b>Note:</b> This value should only be updated when the corresponding UI
	 * piece is updated.
	 * </p>
	 */
	private String plotType;

	/**
	 * The widget used to adjust the current timestep.
	 */
	private TimeSliderComposite timeSlider;

	/**
	 * The currently timestep rendered by the VisIt widget.
	 */
	private int renderedTimestep = 0;
	/**
	 * The current timestep as reported by the {@link #timeSlider} on the UI
	 * thread.
	 */
	private final AtomicInteger widgetTimestep = new AtomicInteger();
	/**
	 * An ExecutorService for launching worker threads. Only one thread is
	 * processed at a time in the order in which they are added.
	 */
	private final ExecutorService executorService = Executors
			.newSingleThreadExecutor();

	/**
	 * The plot {@code Composite} that renders the files through the VisIt
	 * connection.
	 */
	private VisItSwtWidget canvas;

	/**
	 * An ActionTree for populating the context menu with a list of allowed
	 * representations. This should be updated (as necessary) when the context
	 * menu is opened.
	 */
	private final ActionTree repTree;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent Composite that contains the plot render.
	 * @param plot
	 *            The rendered ConnectionPlot. This cannot be changed.
	 */
	public VisItPlotRender(Composite parent, VisItPlot plot) {
		super(parent, plot);

		// Store the adapter so that we can access its connection later.
		adapter = plot.getVisItConnectionAdapter();

		// Set a reference to the VisItPlot. We specifically need this
		// implementation to access the plot representations.
		this.plot = plot;

		// Create the ActionTree that will contain the representations for the
		// current plot category.
		repTree = new ActionTree("Representation");

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionPlotRender#
	 * getPreferenceNodeID()
	 */
	@Override
	protected String getPreferenceNodeID() {
		return "org.eclipse.ice.viz.service.paraview.preferences";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionPlotRender#
	 * createPlotComposite(org.eclipse.swt.widgets.Composite, int,
	 * java.lang.Object)
	 */
	@Override
	protected Composite createPlotComposite(Composite parent, int style,
			final VisItSwtConnection connection) throws Exception {

		// Create a new window on the VisIt server if one does not already
		// exist. We will need the corresponding connection and a window ID. If
		// the window ID is -1, a new one is created.

		Composite container = new Composite(parent, style);
		container.setBackground(parent.getBackground());
		container.setLayout(new GridLayout(1, false));

		// Create the canvas.
		canvas = new VisItSwtWidget(container, SWT.DOUBLE_BUFFERED);
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		canvas.setBackground(parent.getBackground());
		int windowWidth = Integer.parseInt(adapter
				.getConnectionProperty("windowWidth"));
		int windowHeight = Integer.parseInt(adapter
				.getConnectionProperty("windowHeight"));

		// Establish the canvas' connection to the VisIt server. This may throw
		// an exception.
		int windowId = plot.getVisItConnectionAdapter().getNextWindowId();
		canvas.setVisItSwtConnection(connection, windowId, windowWidth,
				windowHeight);

		// Create a mouse manager to handle mouse events inside the
		// canvas.
		new VisItMouseManager(canvas);

		// Set up the canvas' context Menu.
		MenuManager menuManager = new MenuManager();
		// If the parent context Menu is not available, create a new one.
		Menu menu = parent.getMenu();
		if (menu == null) {
			menu = menuManager.createContextMenu(canvas);
			// TODO Populate the "Set Plot Type" menu with the plot categories
			// and types in case it's not already available.
		}

		// When the Menu is about to be shown, add the representation options to
		// it.
		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuHidden(MenuEvent e) {
				// Nothing to do.
			}

			@Override
			public void menuShown(MenuEvent e) {
				repTree.getContributionItem().fill((Menu) e.widget, -1);
			}
		});

		canvas.setMenu(menu);

		// Add a time slider widget.
		timeSlider = new TimeSliderComposite(container, SWT.NONE);
		timeSlider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		timeSlider.setBackground(parent.getBackground());
		// Add a listener to trigger an update to the current timestep.
		timeSlider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Record the current timestep from the TimeSliderComposite.
				widgetTimestep.set(timeSlider.getTimestep());

				// Launch a worker thread to update the timestep for the VisIt
				// widget.
				executorService.submit(new Runnable() {
					@Override
					public void run() {

						// FIXME We need a way to move to a specific timestep
						// rather than cycling through them.

						ViewerMethods methods = connection.getViewerMethods();

						// Send next or previous timestep requests to the VisIt
						// widget until it matches the current timestep in the
						// TimeSliderComposite.
						int targetStep;
						while (renderedTimestep != (targetStep = widgetTimestep
								.get())) {
							if (renderedTimestep < targetStep) {
								methods.animationNextState();
								renderedTimestep++;
							} else {
								methods.animationPreviousState();
								renderedTimestep--;
							}
						}
						return;
					}
				});
				return;
			}
		});

		// TODO We need to figure out how to get the actual times from the VisIt
		// client API. We are currently using the timestep indices.
		// Get the available timesteps.
		ViewerMethods widget = connection.getViewerMethods();
		int timestepCount = widget.timeSliderGetNStates();
		List<Double> times = new ArrayList<Double>(timestepCount);
		for (double i = 0.0; i < timestepCount; i++) {
			times.add(i);
		}
		timeSlider.setTimes(times);

		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.connections.ConnectionPlotRender#
	 * updatePlotComposite(org.eclipse.swt.widgets.Composite, java.lang.Object)
	 */
	@Override
	protected void updatePlotComposite(Composite plotComposite,
			VisItSwtConnection connection) throws Exception {

		// Check the input arguments. The canvas should be the plot Composite.
		if (plotComposite != canvas.getParent()) {
			throw new Exception("VisItPlot error: "
					+ "The canvas was not created properly.");
		}

		// Get the source path from the VisItPlot class. We can't,
		// unfortunately, use the URI as specified.
		String sourcePath = VisItPlot.getSourcePath(plot.getDataSource());

		// See if the plot category and type have been updated since the last
		// refresh. We should also make sure the current plot category and type
		// are valid before we try to update them.
		final String category = getPlotCategory();
		final String representation = getPlotRepresentation();
		final String type = getPlotType();
		// Check that the type is non-null and new. Then do the same for the
		// representation and category.
		boolean plotTypeChanged = (type != null && !type.equals(plotType));
		plotTypeChanged |= (representation != null && !representation
				.equals(plotRepresentation));
		plotTypeChanged |= (category != null && !category.equals(plotCategory));
		// Now check the validity of each property.
		if (plotTypeChanged && type != null) {
			plotTypeChanged = false;
			// Check that the category and type is valid.
			String[] types = plot.getPlotTypes().get(category);
			if (types != null) {
				for (int i = 0; !plotTypeChanged && i < types.length; i++) {
					if (type.equals(types[i])) {
						plotTypeChanged = true;
					}
				}
			}
			// Check that the representation is valid.
			List<String> reps = plot.getRepresentations(category);
			plotTypeChanged &= reps.contains(representation);
		} else {
			// If the type is null, then don't proceed.
			plotTypeChanged = false;
		}

		// Make sure the Canvas is activated.
		canvas.activate();

		// If the plot category or type changed (and they are both valid),
		// update the reference to the currently drawn category and type and
		// update the widget.
		if (plotTypeChanged) {

			// Draw the specified plot on the Canvas.
			ViewerMethods widget = canvas.getViewerMethods();

			// Remove all existing plots.
			widget.deleteActivePlots();

			// FIXME How do we handle invalid paths?
			widget.openDatabase(sourcePath);
			widget.addPlot(representation, type);
			widget.drawPlots();

			// Rebuild the VisIt representation tree based on the current
			// category (if the category actually changed).
			if (!category.equals(plotCategory)) {
				repTree.removeAll();
				for (final String rep : plot.getRepresentations(category)) {
					repTree.add(new ActionTree(new Action(rep) {
						@Override
						public void run() {
							setPlotRepresentation(rep);
							refresh();
						}
					}));
				}
			}

			// Change the record of the current plot category and type for this
			// PlotRender.
			plotCategory = category;
			plotRepresentation = representation;
			plotType = type;
		}

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.PlotRender#clearCache()
	 */
	@Override
	protected void clearCache() {
		// Nothing to do yet.
	}

	/**
	 * Overrides the default behavior to set the plot representation to its
	 * default value given the new category.
	 */
	@Override
	public void setPlotCategory(String category) {
		String oldCategory = getPlotCategory();

		// Proceed with the normal process that sets the plot category.
		super.setPlotCategory(category);

		// If the category changed, we will need to update the representation to
		// the default representation for the new category, or null if the
		// category has no valid representations.
		if (oldCategory != category
				&& (oldCategory == null || !oldCategory.equals(category))) {
			List<String> reps = plot.getRepresentations(category);
			setPlotRepresentation(reps.isEmpty() ? null : reps.get(0));
		}

		return;
	}

	/**
	 * Sets the current plot representation. This is a "sub-category" that lies
	 * between the plot category and type as derived from the {@link VisItPlot}.
	 * <p>
	 * <b>Note:</b> A subsequent call to {@link #refresh()} will be necessary to
	 * sync the UI with this call's changes.
	 * </p>
	 * 
	 * @param representation
	 *            The new plot representation.
	 */
	private void setPlotRepresentation(String representation) {
		this.representation = representation;
	}

	/**
	 * Gets the current plot representation. This is a "sub-category" that lies
	 * between the plot category and type as derived from the {@link VisItPlot}.
	 * 
	 * @return The current plot representation.
	 */
	private String getPlotRepresentation() {
		return representation;
	}
}