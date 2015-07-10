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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;
import org.eclipse.ice.viz.service.paraview.widgets.ParaViewCanvas;
import org.eclipse.ice.viz.service.paraview.widgets.ParaViewMouseAdapter;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;

/**
 * This class manages rendering visualizations using a viz connection to a
 * {@link IParaViewWebClient}.
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewPlotRender extends ConnectionPlotRender<IParaViewWebClient> {

	// TODO If the data source (i.e. IParaViewProxy) is changed, then a new
	// render panel will need to be created and pointed to that proxy's view ID.

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
	 * The {@code ToolBarManager} that will contain the plot actions that can
	 * update the plot widget.
	 */
	private ToolBarManager toolBar;

	/**
	 * The {@code ActionTree} that can be used to update the plot category and
	 * type.
	 */
	private ActionTree plotTypeTree;

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
	protected Composite createPlotComposite(Composite parent, int style, IParaViewWebClient connection)
			throws Exception {

		// Get the current proxy used to open the rendered ParaView file.
		IParaViewProxy proxy = plot.getParaViewProxy();

		// If the proxy is not set, throw an exception.
		if (proxy == null) {
			throw new Exception("The file could not be opened and rendered.");
		}

		// Create the overall container.
		Composite plotContainer = new Composite(parent, style);
		plotContainer.setBackground(parent.getBackground());
		plotContainer.setFont(parent.getFont());
		GridLayout gridLayout = new GridLayout();
		// Get rid of the default margins (5 px on top, bottom, left, right).
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		plotContainer.setLayout(gridLayout);

		// Create a ToolBar.
		ToolBarManager toolBarManager = new ToolBarManager();
		this.toolBar = toolBarManager;
		ToolBar toolBar = toolBarManager.createControl(plotContainer);
		toolBar.setBackground(parent.getBackground());
		toolBar.setFont(parent.getFont());
		toolBar.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		fillToolBar(toolBarManager, connection);

		// Create the ParaView Canvas.
		this.proxy = proxy;
		canvas = new ParaViewCanvas(plotContainer, SWT.NONE);
		canvas.setBackground(parent.getBackground());
		canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		canvas.setClient(connection);
		canvas.setViewId(proxy.getViewId());
		canvas.refresh();

		// Add mouse controls to the canvas.
		canvasMouseListener = new ParaViewMouseAdapter(connection, proxy.getViewId(), canvas);
		canvasMouseListener.setCanvas(canvas);

		// Update the ToolBar based on the current proxy.
		refreshToolBar(toolBarManager);

		// Return the overall container.
		return plotContainer;
	}

	/*
	 * Overrides a method from ConnectionPlotRender.
	 */
	@Override
	protected void updatePlotComposite(Composite plotComposite, IParaViewWebClient connection) throws Exception {

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

			// Update the ToolBar based on the current proxy.
			refreshToolBar(toolBar);
		}

		// Otherwise, we should be able to update the render panel.

		// TODO We'll need to do more than this! We also should do anything if
		// the value hasn't changed.
		Future<Boolean> task = proxy.setFeature(getPlotCategory(), getPlotType());
		refreshWidgetAfterTask(task);

		return;
	}

	/*
	 * Overrides a method from ConnectionPlotRender.
	 */
	@Override
	protected void clearCache() {
		// TODO Auto-generated method stub

	}

	/**
	 * Fills the specified {@code ToolBar} with actions that can be used to
	 * update the rendered plot.
	 * <p>
	 * <b>Note:</b> This method should only be called once, and should be called
	 * at plot creation time.
	 * </p>
	 * 
	 * @param toolBar
	 *            The {@code ToolBarManager} that will be populated.
	 */
	private void fillToolBar(ToolBarManager toolBar, final IParaViewWebClient connection) {

		plotTypeTree = new ActionTree("Plot Types");
		toolBar.add(plotTypeTree.getContributionItem());

		// Add widgets to change the representation.
		propertiesTree = new ActionTree("Properties");
		toolBar.add(propertiesTree.getContributionItem());

		// Refresh the ToolBar.
		toolBar.update(true);

		return;
	}

	/**
	 * Refreshes (if necessary) the widgets in the {@code ToolBar}.
	 * <p>
	 * Specifically, this method updates both the {@link #plotTypeTree} and
	 * {@link #representationTree}.
	 * </p>
	 * 
	 * @param toolBar
	 *            The {@code ToolBar} to update.
	 */
	private void refreshToolBar(ToolBarManager toolBar) {
		// Create an ActionTree for the available plot categories and types.
		// Selecting one of the leaf nodes should set the category and type for
		// the associated plot.

		ActionTree tree;

		// Re-build the plot type tree with the proxy's categories and features.
		plotTypeTree.removeAll();
		for (final String category : proxy.getFeatureCategories()) {
			tree = new ActionTree(category);
			for (final String feature : proxy.getFeatures(category)) {
				tree.add(new ActionTree(new Action(feature) {
					@Override
					public void run() {
						Future<Boolean> task = proxy.setFeature(category, feature);
						refreshWidgetAfterTask(task);
					}
				}));
			}
			plotTypeTree.add(tree);
		}

		// Re-build the property tree with the proxy's properties and allowed
		// values.
		propertiesTree.removeAll();
		for (final String property : proxy.getProperties().keySet()) {
			tree = new ActionTree(property);
			for (final String value : proxy.getPropertyAllowedValues(property)) {
				tree.add(new ActionTree(new Action(value) {
					@Override
					public void run() {
						Future<Boolean> task = proxy.setProperty(property, value);
						refreshWidgetAfterTask(task);
					}
				}));
			}
			propertiesTree.add(tree);
		}

		// Refresh the ToolBar.
		toolBar.update(true);

		return;
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
}
