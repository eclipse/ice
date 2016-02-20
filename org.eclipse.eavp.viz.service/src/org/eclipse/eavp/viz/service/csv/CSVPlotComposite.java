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
package org.eclipse.eavp.viz.service.csv;

import java.util.List;
import java.util.Set;

import org.eclipse.eavp.viz.service.IPlot;
import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.datastructures.VizActionTree;
import org.eclipse.eavp.viz.service.widgets.PlotComposite;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Composite;

/**
 * This class provides a {@link PlotComposite} for a {@link CSVPlot}.
 * 
 * @author Jordan Deyton
 *
 */
public class CSVPlotComposite extends PlotComposite {

	/**
	 * The editor in which the CSV plot is rendered.
	 */
	private CSVPlotEditor editor;

	/**
	 * The provider responsible for maintaining the plot configuration.
	 */
	private PlotProvider plotProvider;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public CSVPlotComposite(Composite parent, int style) {
		super(parent, style);

		// Nothing to do.

		// CSV widgets should be created only in the overridden methods.
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#createPlotContent(org.eclipse.swt.widgets.Composite, int)
	 */
	@Override
	protected Composite createPlotContent(Composite parent, int style)
			throws Exception {

		// Create the editor and all required providers.
		editor = new CSVPlotEditor();
		plotProvider = new PlotProvider();

		// Create the plot contents and set its context Menu.
		editor.createPartControl(parent);
		addContextMenu(editor.getPlotCanvas());

		return editor.getComposite();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#disposePlotContent(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void disposePlotContent(Composite plotContent) {
		// Dispose UI widgets (the editor and its providers).
		if (editor != null) {
			editor.dispose();
			editor = null;
			plotProvider = null;
		}

		// Proceed with the default disposal.
		super.disposePlotContent(plotContent);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#getPlotActions()
	 */
	@Override
	protected List<VizActionTree> getPlotActions() {
		// In addition to the default actions...
		List<VizActionTree> actions = super.getPlotActions();

		// Add an action to clear all plotted series.
		actions.add(new VizActionTree(new Action("Remove all series") {
			@Override
			public void run() {
				// Disable all plotted series, then refresh if the plot changed.
				Set<ISeries> plottedSeries = getPlottedSeries();
				if (!plottedSeries.isEmpty()) {
					for (ISeries series : getPlottedSeries()) {
						series.setEnabled(false);
					}
					refresh();
				}
				return;
			}
		}));

		return actions;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#hasInterchangeableSeries()
	 */
	@Override
	protected boolean hasInterchangeableSeries(){
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#hideSeries(org.eclipse.eavp.viz.service.ISeries)
	 */
	@Override
	protected void hideSeries(ISeries series) throws Exception {
		// Disable the series from the required widgets.
		double plotTime = 0.0;
		plotProvider.removeSeries(plotTime, series);
		editor.removeSeries(series);

		// Don't forget to call the super method, which marks the series as not
		// being plotted.
		super.hideSeries(series);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#showSeries(org.eclipse.eavp.viz.service.ISeries)
	 */
	@Override
	protected void showSeries(ISeries series) throws Exception {
		// Enable the series in the required widgets.
		double plotTime = 0.0;
		plotProvider.addSeries(plotTime, series);

		// Don't forget to call the super method, which registers the series as
		// being plotted.
		super.showSeries(series);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.widgets.PlotComposite#updatePlotContent(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void updatePlotContent(Composite plotContent) throws Exception {
		IPlot plot = (IPlot) getPlot();

		// Apply the basic plot features.
		plotProvider.setPlotTitle(plot.getPlotTitle());
		plotProvider.setIndependentSeries(plot.getIndependentSeries());

		// Then, follow the default behavior. This triggers calls to hide/show
		// series based on which ones are currently enabled.
		super.updatePlotContent(plotContent);

		// Show the plot provider.
		editor.showPlotProvider(plotProvider);

		return;
	}

}
