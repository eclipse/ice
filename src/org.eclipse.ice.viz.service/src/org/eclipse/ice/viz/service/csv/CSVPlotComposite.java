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
package org.eclipse.ice.viz.service.csv;

import java.util.List;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.ice.viz.service.widgets.PlotComposite;
import org.eclipse.ice.viz.service.widgets.TreeSelectionDialogProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
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

	}

	@Override
	public boolean setPlot(IPlot plot) {
		return false;
	}

	public boolean setPlot(CSVProxyPlot plot) {
		boolean changed = super.setPlot((IPlot) plot);
		if (changed) {
			// Do additional stuff here.
		}
		return changed;
	}

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

	@Override
	protected void disposePlotContent(Composite plotContent) {
		// Dispose the editor and its providers.
		if (editor != null) {
			editor.dispose();
			editor = null;
			plotProvider = null;
		}

		super.disposePlotContent(plotContent);
	}

	@Override
	protected List<ActionTree> getPlotActions() {
		List<ActionTree> actions = super.getPlotActions();

		// Create an action to open a dialog used to select which series are
		// plotted.
		actions.add(new ActionTree(new Action("Select series...") {
			@Override
			public void run() {
				showSeriesDialog();
			}
		}));

		// Set up an action to clear the plot.
		actions.add(new ActionTree(new Action("Remove all series") {
			@Override
			public void run() {
				for (ISeries series : getPlottedSeries()) {
					try {
						hideSeries(series);
					} catch (Exception e) {
						// TODO log a warning
					}
				}
			}
		}));

		return actions;
	}

	@Override
	protected void hideSeries(ISeries series) throws Exception {
		System.err.println("Hiding series " + series.getLabel());

		// Hide the series, throwing an exception if it could not be done.
		double plotTime = 0.0;
		plotProvider.removeSeries(plotTime, series);
		editor.removeSeries(series);

		// Don't forget to call the super method, which marks the series as not
		// being plotted.
		super.hideSeries(series);

		return;
	}

	@Override
	protected void showSeries(ISeries series) throws Exception {
		System.err.println("Showing series " + series.getLabel());

		// Show the series, throwing an exception if it could not be done.
		double plotTime = 0.0;
		plotProvider.addSeries(plotTime, series);

		// Don't forget to call the super method, which registers the series as
		// being plotted.
		super.showSeries(series);

		return;
	}

	private void showSeriesDialog() {

		// TODO Move this dialog to PlotComposite and use an overridable method
		// to determine multi-select.
		TreeSelectionDialogProvider provider = new TreeSelectionDialogProvider() {
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
						children = plot.getAllDependentSeries(categories.get(0))
								.toArray();
					} else {
						children = new Object[0];
					}
				}
				// If the element is a category, return its associated series.
				else if (parent instanceof String) {
					children = plot.getAllDependentSeries(parent.toString())
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
					selected = ((ISeries) element).enabled();
				} else {
					selected = false;
				}
				return selected;
			}
		};
		provider.setTitle("Select CSV Series");
		provider.setMessage("Please select any set of series to plot.");
		
		if (provider.openDialog(getShell(), getPlot(), true) == Window.OK) {
			
			boolean changed = false;
			
			// Hide all unselected series.
			for (Object element : provider.getUnselectedLeafElements()) {
				if (element instanceof ISeries) {
					try {
						// Disable the series, then update the necessary UI
						// components to hide it.
						ISeries series = (ISeries) element;
						series.setEnabled(false);
						hideSeries(series);
						changed = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			// Show all selected series.
			for (Object element : provider.getSelectedLeafElements()) {
				if (element instanceof ISeries) {
					try {
						// Enable the series, then update the necessary UI
						// components to show it.
						ISeries series = (ISeries) element;
						series.setEnabled(true);
						showSeries(series);
						changed = true;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			// FIXME This is specific to CSV....
			if (changed) {
				editor.showPlotProvider(plotProvider);
			}
		}

		return;
	}

	@Override
	protected void updatePlotContent(Composite plotContent) throws Exception {
		CSVProxyPlot plot = (CSVProxyPlot) getPlot();
		// Configure the plot content.
		plotProvider.setPlotTitle(plot.getPlotTitle());
		plotProvider.setIndependentSeries(plot.getIndependentSeries());

		// First, follow the default behavior.
		super.updatePlotContent(plotContent);

		// Show the plot provider.
		editor.showPlotProvider(plotProvider);

		return;
	}

}
