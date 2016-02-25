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
package org.eclipse.eavp.viz.service.paraview;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.IPlotListener;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.connections.ConnectionPlot;
import org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite;
import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.datastructures.VizActionTree;
import org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.eavp.viz.service.paraview.widgets.ParaViewCanvas;
import org.eclipse.eavp.viz.service.paraview.widgets.ParaViewMouseAdapter;
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

/**
 * This class provides a composite in which resources can be rendered from an
 * associated {@link ParaViewPlot}.
 * 
 * @author Jordan
 *
 */
public class ParaViewPlotComposite extends
		ConnectionPlotComposite<IParaViewWebClient>implements IPlotListener {

	/**
	 * The canvas that is used to render the remote ParaView view.
	 */
	private ParaViewCanvas canvas;
	/**
	 * The mouse event listener that triggers updates (movement, scrolling,
	 * dragging, etc.) for the {@link #canvas}.
	 */
	private ParaViewMouseAdapter canvasMouseListener;

	/**
	 * A reference to the current plot cast as its actual type.
	 */
	private ParaViewPlot plot;

	/**
	 * The {@code ActionTree} that can be used to update the available plot
	 * properties.
	 */
	private VizActionTree propertiesTree;

	/**
	 * The current proxy rendered by this class. The lifecycle of the
	 * {@link #canvas} is dependent on this proxy as it is tied to the proxy's
	 * underlying view ID.
	 */
	private IParaViewProxy proxy;

	/**
	 * The widget used to adjust the current timestep.
	 */
	private TimeSliderComposite timeSlider;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public ParaViewPlotComposite(Composite parent, int style) {
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

	/**
	 * When the proxy changes (which happens on a data load/reload), the UI
	 * widgets will need to be updated. This includes:
	 * <ul>
	 * <li>{@link #canvas} - it needs the proxy's view ID</li>
	 * <li>{@link #canvasMouseListener} - it needs the proxy's view ID</li>
	 * <li>{@link #timeSlider} - it needs the proxy's timesteps</li>
	 * <li>{@link #propertiesTree} - this is populated based on the proxy's
	 * properties</li>
	 * </ul>
	 */
	private boolean checkProxy() {
		IParaViewWebClient widget = getConnection().getWidget();

		boolean changed = false;

		final IParaViewProxy proxy = plot.getParaViewProxy();
		if (proxy != this.proxy) {
			changed = true;
			this.proxy = proxy;
			int viewId = proxy.getViewId();

			// Set the view ID for the canvas.
			canvas.setViewId(viewId);

			// Set the view ID for the mouse control adapter, creating it if
			// necessary.
			if (canvasMouseListener == null) {
				canvasMouseListener = new ParaViewMouseAdapter(widget, viewId,
						canvas);
				canvasMouseListener.setCanvas(canvas);
			}
			canvasMouseListener.setViewId(viewId);

			// Set the times for the time slider.
			timeSlider.setTimes(proxy.getTimesteps());

			// Refresh the plot actions.
			propertiesTree.removeAll();
			for (final String property : proxy.getProperties().keySet()) {
				VizActionTree tree = new VizActionTree(property);
				for (final String value : proxy
						.getPropertyAllowedValues(property)) {
					tree.add(new VizActionTree(new Action(value) {
						@Override
						public void run() {
							Future<Boolean> task = proxy.setProperty(property,
									value);
							refreshWidgetAfterTask(task);
						}
					}));
				}
				propertiesTree.add(tree);
			}

			// Refresh the canvas.
			canvas.refresh();
		}

		return changed;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite#createPlotContent(org.eclipse.swt.widgets.Composite, int, org.eclipse.eavp.viz.service.connections.IVizConnection)
	 */
	@Override
	protected Composite createPlotContent(Composite parent, int style,
			IVizConnection<IParaViewWebClient> connection) throws Exception {
		// Validate the connection.
		super.createPlotContent(parent, style, connection);

		// Throw an exception if the plot hasn't finished loading.
		if (!plot.isLoaded()) {
			throw new Exception(getClass().getName() + " error: "
					+ "The ParaView plot has not finished loading.");
		}

		// Create the overall container.
		Composite container = new Composite(parent, style);
		container.setBackground(parent.getBackground());
		container.setFont(parent.getFont());
		GridLayout gridLayout = new GridLayout();
		// Get rid of the default margins (5 px on top, bottom, left, right).
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		container.setLayout(gridLayout);

		// Create the ParaView Canvas.
		canvas = new ParaViewCanvas(container, SWT.NONE);
		canvas.setBackground(parent.getBackground());
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		canvas.setClient(connection.getWidget());

		// Add a time slider widget.
		timeSlider = createTimeSlider(container);
		timeSlider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Set the context Menu for the ParaView canvas.
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

				// Until the timesteps match, keep setting it to the next
				// timestep.
				int targetStep = widgetTimestep.get();
				while (renderedTimestep.getAndSet(targetStep) != targetStep) {
					try {
						proxy.setTimestep(targetStep).get();
					} catch (InterruptedException | ExecutionException e) {
						e.printStackTrace();
					}
					targetStep = widgetTimestep.get();
				}

				// The canvas will need to be refreshed.
				canvas.refresh();

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

		return timeSlider;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite#disposePlotContent(org.eclipse.swt.widgets.Composite, org.eclipse.eavp.viz.service.connections.IVizConnection)
	 */
	@Override
	protected void disposePlotContent(Composite plotContent,
			IVizConnection<IParaViewWebClient> connection) {
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
		return ParaViewVizService.PREFERENCE_PAGE_ID;
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
		propertiesTree = new VizActionTree("Properties");
		actions.add(propertiesTree);
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
	 * Refreshes the {@link #canvas} after the task completes. Waiting is
	 * performed on a separate thread.
	 * 
	 * @param task
	 *            The task to wait on.
	 */
	private void refreshWidgetAfterTask(final Future<?> task) {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					task.get();
					canvas.refresh();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}
		});
		thread.setDaemon(true);
		thread.start();
		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite#setConnectionPlot(org.eclipse.eavp.viz.service.connections.ConnectionPlot)
	 */
	@Override
	public boolean setConnectionPlot(ConnectionPlot<IParaViewWebClient> plot) {
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
			this.plot = (ParaViewPlot) plot;
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
			IVizConnection<IParaViewWebClient> connection) throws Exception {

		// Check if the proxy has changed first. If so, there's some reloading
		// that must be done.
		checkProxy();

		// Set the feature for the proxy and refresh afterward.
		String category = series.getCategory();
		String feature = series.getLabel();
		Future<Boolean> task = proxy.setFeature(category, feature);
		refreshWidgetAfterTask(task);

		// Proceed with the default behavior.
		super.showSeries(series, connection);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.connections.ConnectionPlotComposite#updatePlotContent(org.eclipse.swt.widgets.Composite, org.eclipse.eavp.viz.service.connections.IVizConnection)
	 */
	@Override
	protected void updatePlotContent(Composite plotContent,
			IVizConnection<IParaViewWebClient> connection) throws Exception {
		// Validate the connection. This also makes the necessary calls to
		// hide/show series.
		super.updatePlotContent(plotContent, connection);

		// Throw an exception if the plot hasn't finished loading.
		if (!plot.isLoaded()) {
			throw new Exception(getClass().getName() + " error: "
					+ "The ParaView plot has not finished loading.");
		}

		// Check for a new proxy. This updates the canvas and time slider if
		// necessary. If the proxy did not change, just redraw the canvas.
		if (!checkProxy()) {
			canvas.refresh();
		}

		return;
	}

}
