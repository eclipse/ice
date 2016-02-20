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
package org.eclipse.eavp.viz.service.visit;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.IPlotListener;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.connections.ConnectionPlot;
import org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite;
import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.datastructures.VizActionTree;
import org.eclipse.eavp.viz.service.widgets.TimeSliderComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtWidget;
import visit.java.client.ViewerMethods;

/**
 * This class provides a composite in which resources can be rendered from an
 * associated {@link VisItPlot}.
 * 
 * @author Jordan
 *
 */
public class VisItPlotComposite extends
		ConnectionPlotComposite<VisItSwtConnection>implements IPlotListener {

	/**
	 * The plot {@code Composite} that renders the files through the VisIt
	 * connection.
	 */
	private VisItSwtWidget canvas;

	/**
	 * The current category used to render the plot.
	 */
	private String category;

	/**
	 * The current representation used to render the plot.
	 */
	private String representation;

	/**
	 * An ActionTree for populating the context menu with a list of allowed
	 * representations. This should be updated (as necessary) when the context
	 * menu is opened.
	 */
	private VizActionTree repTree;

	/**
	 * The current plot type rendered in the canvas.
	 */
	private String type;

	/**
	 * The widget used to adjust the current timestep.
	 */
	private TimeSliderComposite timeSlider;

	/**
	 * The currently associated {@link VisItPlot}.
	 */
	private VisItPlot plot;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public VisItPlotComposite(Composite parent, int style) {
		super(parent, style);

		// Nothing to do yet.
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#canShowMultipleSeries()
	 */
	@Override
	protected boolean canShowMultipleSeries() {
		// Only one series (plot type and category) can be rendered at a time.
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite#createPlotContent(org.eclipse.swt.widgets.Composite, int, org.eclipse.eavp.viz.service.connections.IVizConnection)
	 */
	@Override
	protected Composite createPlotContent(Composite parent, int style,
			IVizConnection<VisItSwtConnection> connection) throws Exception {
		// Validate the connection.
		super.createPlotContent(parent, style, connection);

		// Throw an exception if the plot hasn't finished loading.
		if (!plot.isLoaded()) {
			throw new Exception(getClass().getName() + " error: "
					+ "The VisIt plot has not finished loading.");
		}

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
		int windowWidth = Integer
				.parseInt(connection.getProperty("windowWidth"));
		int windowHeight = Integer
				.parseInt(connection.getProperty("windowHeight"));

		// Establish the canvas' connection to the VisIt server. This may throw
		// an exception.
		// FIXME int windowId = connection.getNextWindowId();
		int windowId = 1;
		canvas.setVisItSwtConnection(connection.getWidget(), windowId,
				windowWidth, windowHeight);

		// Create a mouse manager to handle mouse events inside the
		// canvas.
		new VisItMouseManager(canvas);

		// Add a time slider widget.
		timeSlider = createTimeSlider(container);
		timeSlider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Set the context Menu for the VisIt canvas.
		addContextMenu(canvas);

		return container;
	}

	/**
	 * Creates a time slider widget in the parent {@code Composite}.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that will contain the widget.
	 * @return The created widget.
	 */
	private TimeSliderComposite createTimeSlider(Composite parent) {

		// The widget that will be created.
		final TimeSliderComposite timeSlider;

		// // The associated connection web client.
		// final VisItSwtConnection widget = null;

		// The currently rendered timestep and the slider's timestep.
		final AtomicInteger renderedTimestep = new AtomicInteger();
		final AtomicInteger widgetTimestep = new AtomicInteger();
		// A worker thread for updating the canvas.
		final ExecutorService executorService = Executors
				.newSingleThreadExecutor();

		// Add a time slider widget.
		timeSlider = new TimeSliderComposite(parent, SWT.NONE);
		timeSlider.setBackground(parent.getBackground());

		// Create a task that can be used to update the canvas when the timestep
		// changes.
		final Runnable updateCanvasTask = new Runnable() {
			@Override
			public void run() {

				// FIXME We need a way to move to a specific timestep
				// rather than cycling through them.

				VisItSwtConnection widget = getConnection().getWidget();
				ViewerMethods methods = widget.getViewerMethods();

				// Send next or previous timestep requests to the VisIt
				// widget until it matches the current timestep in the
				// TimeSliderComposite.
				int currentStep = renderedTimestep.get();
				int targetStep;
				while (currentStep != (targetStep = widgetTimestep.get())) {
					if (currentStep < targetStep) {
						methods.animationNextState();
						currentStep++;
					} else {
						methods.animationPreviousState();
						currentStep--;
					}
				}
				renderedTimestep.set(currentStep);
				return;
			}
		};

		// When the time slider is disposed, shut down the worker thread.
		timeSlider.addDisposeListener(new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				executorService.shutdown();
			}
		});

		// When the selection changes, trigger an update to the canvas.
		timeSlider.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Record the current timestep from the TimeSliderComposite.
				widgetTimestep.set(timeSlider.getTimestep());
				executorService.submit(updateCanvasTask);
			}
		});

		// The independent series contains the times (floats) or cycles
		// (integers). Convert these to doubles for the time slider widget.
		ISeries series = getPlot().getIndependentSeries();
		Object[] pointArray = series.getDataPoints();
		List<Double> times = new ArrayList<Double>(pointArray.length);
		if ("Time".equals(series.getLabel())) {
			for (Object value : pointArray) {
				times.add(((Float) value).doubleValue());
			}
		} else {
			for (Object value : pointArray) {
				times.add(((Integer) value).doubleValue());
			}
		}
		timeSlider.setTimes(times);

		return timeSlider;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite#disposePlotContent(org.eclipse.swt.widgets.Composite, org.eclipse.eavp.viz.service.connections.IVizConnection)
	 */
	@Override
	protected void disposePlotContent(Composite plotContent,
			IVizConnection<VisItSwtConnection> connection) {
		// Dispose the custom UI widgets.
		if (canvas != null && !canvas.isDisposed()) {
			canvas.dispose();
			canvas = null;
			timeSlider.dispose();
			timeSlider = null;
		}

		// Proceed with the default disposal.
		super.disposePlotContent(plotContent, connection);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite#getConnectionPreferencePageID()
	 */
	@Override
	protected String getConnectionPreferencePageID() {
		return VisItVizService.PREFERENCE_PAGE_ID;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#getPlotActions()
	 */
	@Override
	protected List<VizActionTree> getPlotActions() {
		// In addition to the default actions, add the action to set the
		// "representation".
		List<VizActionTree> actions = super.getPlotActions();
		repTree = new VizActionTree("Representation");
		actions.add(repTree);
		return actions;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.IPlotListener#plotUpdated(org.eclipse.eavp.viz.service.IPlot, java.lang.String, java.lang.String)
	 */
	@Override
	public void plotUpdated(IPlot plot, String key, String value) {
		// The only notification sent by the plot is that the data has loaded.
		if (plot == getPlot()) {
			refresh();
		}
	}

	/**
	 * Refreshes the canvas by querying the {@link ViewerMethods} object with
	 * the current plot {@link #representation} and {@link #type}.
	 */
	private void refreshCanvas() {
		// Draw the specified plot on the Canvas.
		ViewerMethods widget = canvas.getViewerMethods();

		// Get the source path from the VisItPlot class. We can't,
		// unfortunately, use the URI as specified.
		VisItPlot plot = (VisItPlot) getPlot();
		String sourcePath = plot.getSourcePath(plot.getDataSource());

		// Make sure the Canvas is activated.
		canvas.activate();

		// Remove all existing plots.
		widget.deleteActivePlots();

		// FIXME How do we handle invalid paths?
		widget.openDatabase(sourcePath);
		widget.addPlot(representation, type);
		widget.drawPlots();

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite#setConnectionPlot(org.eclipse.eavp.viz.service.connections.ConnectionPlot)
	 */
	@Override
	public boolean setConnectionPlot(ConnectionPlot<VisItSwtConnection> plot) {
		boolean changed = super.setConnectionPlot(plot);
		// We need to listen for updates from the plot telling us when the data
		// is loaded.
		if (changed) {
			// Unregister from the old plot.
			if (this.plot != null) {
				this.plot.removePlotListener(this);
				this.plot = null;
			}

			// Register with the new plot.
			this.plot = (VisItPlot) plot;
			this.plot.addPlotListener(this);
		}
		return changed;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite#showSeries(org.eclipse.eavp.viz.service.ISeries, org.eclipse.eavp.viz.service.connections.IVizConnection)
	 */
	@Override
	protected void showSeries(ISeries series,
			IVizConnection<VisItSwtConnection> connection) throws Exception {
		// Get the plot category and type.
		String category = series.getCategory();
		// Update the drawn type.
		type = series.getLabel();

		// If the category changed, we need to update the set of representations
		// from which the user can select AND pick one of them.
		if (!category.equals(this.category)) {
			// Update the category, too.
			this.category = category;
			// Update the representations based on the current category.
			VisItPlot plot = (VisItPlot) getPlot();
			repTree.removeAll();
			for (final String rep : plot.getRepresentations(category)) {
				repTree.add(new VizActionTree(new Action(rep) {
					@Override
					public void run() {
						representation = rep;
						refreshCanvas();
					}
				}));
				// Set the initial representation to the specified one.
				representation = plot.getRepresentations(category).iterator()
						.next();
			}
		}

		// We may now refresh the canvas.
		refreshCanvas();

		// Proceed with the default behavior.
		super.showSeries(series, connection);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite#updatePlotContent(org.eclipse.swt.widgets.Composite, org.eclipse.eavp.viz.service.connections.IVizConnection)
	 */
	@Override
	protected void updatePlotContent(Composite plotContent,
			IVizConnection<VisItSwtConnection> connection) throws Exception {
		// Validate the connection. This also makes the necessary calls to
		// hide/show series.
		super.updatePlotContent(plotContent, connection);

		// Throw an exception if the plot hasn't finished loading.
		if (!plot.isLoaded()) {
			throw new Exception(getClass().getName() + " error: "
					+ "The VisIt plot has not finished loading.");
		}

		// Nothing to do yet.

		return;
	}

}
