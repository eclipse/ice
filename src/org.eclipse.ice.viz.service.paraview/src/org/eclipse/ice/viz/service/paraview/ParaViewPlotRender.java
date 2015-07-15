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
package org.eclipse.ice.viz.service.paraview;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.ice.viz.service.paraview.widgets.ParaViewCanvas;
import org.eclipse.ice.viz.service.paraview.widgets.ParaViewMouseAdapter;
import org.eclipse.ice.viz.service.widgets.TimeSliderComposite;
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
 * This class manages rendering visualizations using a viz connection to a
 * {@link IParaViewWebClient}.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewPlotRender
		extends ConnectionPlotRender<IParaViewWebClient> {

	/**
	 * A reference to the plot conveniently cast to its actual type.
	 */
	private final ParaViewPlot plot;

	/**
	 * The current proxy rendered by this class. The lifecycle of the
	 * {@link #canvas} is dependent on this proxy as it is tied to the proxy's
	 * underlying view ID.
	 */
	private IParaViewProxy proxy;

	// ---- UI Components ---- //
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
	 * The widget used to adjust the current timestep.
	 */
	private TimeSliderComposite timeSlider;

	/**
	 * The {@code ActionTree} that can be used to update the available plot
	 * properties.
	 */
	private ActionTree propertiesTree;
	// ----------------------- //

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            The parent Composite that contains the plot render.
	 * @param plot
	 *            The rendered ConnectionPlot. This cannot be changed.
	 */
	public ParaViewPlotRender(Composite parent, ParaViewPlot plot) {
		super(parent, plot);

		// Set the reference to the ParaViewPlot.
		this.plot = plot;
	}

	/*
	 * Overrides a method from ConnectionPlotRender.
	 */
	@Override
	protected String getPreferenceNodeID() {
		return "org.eclipse.ice.viz.service.paraview.preferences";
	}

	/*
	 * Overrides a method from ConnectionPlotRender.
	 */
	@Override
	protected Composite createPlotComposite(Composite parent, int style,
			IParaViewWebClient connection) throws Exception {

		// Get the current proxy used to open the rendered ParaView file.
		IParaViewProxy proxy = plot.getParaViewProxy();

		// If the proxy is not set, throw an exception.
		if (proxy == null) {
			throw new Exception("The file could not be opened and rendered.");
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
		this.proxy = proxy;
		canvas = new ParaViewCanvas(container, SWT.NONE);
		canvas.setBackground(parent.getBackground());
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		canvas.setClient(connection);
		canvas.setViewId(proxy.getViewId());
		canvas.refresh();

		// Add mouse controls to the canvas.
		canvasMouseListener = new ParaViewMouseAdapter(connection,
				proxy.getViewId(), canvas);
		canvasMouseListener.setCanvas(canvas);

		// Refresh the plot render actions.
		refreshPlotRenderActions();

		// Add a time slider widget.
		timeSlider = createTimeSlider(container);
		timeSlider.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		// Set the context Menu for the ParaView canvas.
		canvas.setMenu(getContextMenu());

		// Return the overall container.
		return container;
	}

	/*
	 * Overrides a method from PlotRender.
	 */
	@Override
	protected List<ActionTree> createPlotRenderActions() {
		// In addition to the default actions, add the action to set the
		// "representation".
		List<ActionTree> actions = super.createPlotRenderActions();

		// Create an ActionTree to change values from the proxy's available set
		// of properties.
		if (propertiesTree == null) {
			propertiesTree = new ActionTree("Properties");
		}
		actions.add(propertiesTree);

		return actions;
	}

	/*
	 * Overrides a method from ConnectionPlotRender.
	 */
	@Override
	protected void updatePlotComposite(Composite plotComposite,
			IParaViewWebClient connection) throws Exception {

		// Get the current proxy used to open the rendered ParaView file.
		IParaViewProxy proxy = plot.getParaViewProxy();

		// If the proxy is not set, throw an exception.
		if (proxy == null) {
			throw new Exception("The file could not be opened and rendered.");
		}

		// If the proxy is changed, we need to re-create the render panel. This
		// is because the render panel is linked to a specific view ID.
		if (proxy != this.proxy) {
			// Update the reference to the proxy.
			this.proxy = proxy;

			// Reset the Canvas' client and view ID.
			canvas.setClient(connection);
			canvas.setViewId(proxy.getViewId());
			canvas.refresh();
			canvasMouseListener.setViewId(proxy.getViewId());

			// Refresh the plot render actions.
			refreshPlotRenderActions();
		}

		// Otherwise, we should be able to update the render panel.

		// TODO We'll need to do more than this! We also should do anything if
		// the value hasn't changed.
		Future<Boolean> task = proxy.setFeature(getPlotCategory(),
				getPlotType());
		refreshWidgetAfterTask(task);

		return;
	}

	/**
	 * Refreshes the plot render actions that need to be refreshed when the
	 * proxy changes.
	 */
	private void refreshPlotRenderActions() {
		ActionTree tree;

		// Reset the properties tree for the current proxy.
		propertiesTree.removeAll();
		for (final String property : proxy.getProperties().keySet()) {
			tree = new ActionTree(property);
			for (final String value : proxy
					.getPropertyAllowedValues(property)) {
				tree.add(new ActionTree(new Action(value) {
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
	}

	/*
	 * Implements an abstract method from PlotRender.
	 */
	@Override
	protected void clearCache() {
		// TODO Auto-generated method stub

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

				// FIXME We need a way to move to a specific timestep
				// rather than cycling through them.

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

		// Set the times for the time slider.
		timeSlider.setTimes(proxy.getTimesteps());

		return timeSlider;
	}

}
