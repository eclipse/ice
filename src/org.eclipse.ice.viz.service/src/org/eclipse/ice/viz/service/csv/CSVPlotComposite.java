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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
	 * A flag denoting if the plot managed by this composite should be contour
	 */
	private boolean isContour = false;

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
	 * Overrides a method from PlotComposite.
	 */
	@Override
	protected Composite createPlotContent(Composite parent, int style)
			throws Exception {

		// Create the editor and all required providers.
		editor = new CSVPlotEditor();
		plotProvider = new PlotProvider();
		plotProvider.setIsContour(isContour);

		// Create the plot contents and set its context Menu.
		editor.createPartControl(parent);
		addContextMenu(editor.getPlotCanvas());

		return editor.getComposite();
	}

	/*
	 * Overrides a method from PlotComposite.
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
	 * Overrides a method from PlotComposite.
	 */
	@Override
	protected List<ActionTree> getPlotActions() {
		// In addition to the default actions...
		List<ActionTree> actions = new ArrayList<ActionTree>();

		if (isContour) {
			actions.addAll(super.getPlotActions());
		} else {
			// Create a dialog that can be used to select available series.
			final TreeSelectionDialogProvider provider;
			provider = new TreeSelectionDialogProvider() {
				@Override
				public Object[] getChildren(Object parent) {
					final Object[] children;
					IPlot plot = getPlot();
					// The three selections that need to be made are the x, y,
					// and intensity values
					if (parent == plot) {
						children = new Object[] { "X Values", "Y Values",
								"Intensity Values" };
					}
					// If the user is selecting from one of the categories
					// above, show the series available for the different
					// options
					else if (parent instanceof String) {
						if (parent.equals("X Values")
								|| parent.equals("Y Values")
								|| parent.equals("Intensity Values")) {
							List<String> categories = plot.getCategories();
							if (categories.size() > 1) {
								children = categories.toArray();
							} else if (!categories.isEmpty()) {
								children = plot
										.getDependentSeries(categories.get(0))
										.toArray();
							} else {
								children = new Object[0];
							}
						} else {
							children = plot
									.getDependentSeries(parent.toString())
									.toArray();
						}
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
						ISeries selectedSeries = (ISeries) element;
						selected = ((ISeries) element).isEnabled();

					} else {
						selected = false;
					}
					return selected;
				}
			};

			actions.add(new ActionTree(new Action("Change contour") {
				@Override
				public void run() {
					if (provider.openDialog(getShell(), getPlot(),
							true) == Window.OK) {
						List<Object> selectedSeries = provider
								.getAllSelectedLeafElements();
						for (Object o : provider.getChildren(getPlot())) {
							if (o instanceof String) {
								String str = (String) o;
								Object[] children = provider.getChildren(str);
								ISeries selected = null;
								if (children.length > 0
										&& children[0] instanceof String) {
									for (Object category : children) {
										for (Object series : provider
												.getChildren(
														category.toString())) {
											if (selectedSeries
													.contains(series)) {
												selected = (ISeries) series;
												((ISeries) series)
														.setEnabled(true);
											} else {
												((ISeries) series)
														.setEnabled(false);
											}
										}
									}
								} else if (children.length > 0
										&& children[0] instanceof ISeries) {
									for (Object series : children) {
										if (selectedSeries.contains(series)) {
											selected = (ISeries) series;
											((ISeries) series).setEnabled(true);
										} else {
											((ISeries) series)
													.setEnabled(false);
										}
									}
								}
								if (o.toString().equals("X Values")) {
									plotProvider.setIndependentSeries(selected);
								} else if (o.toString().equals("Y Values")) {
									plotProvider.setDependentSeries(selected);
								} else if (o.toString()
										.equals("Intensity Values")) {
									plotProvider.setIntensitySeries(selected);
								}
							}
						}
						refresh();
					}
					return;
				}
			}));

		}
		// Add an action to clear all plotted series.
		actions.add(new ActionTree(new Action("Remove all series") {
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
	 * Overrides a method from PlotComposite.
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
	 * Overrides a method from PlotComposite.
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
	 * Overrides a method from PlotComposite.
	 */
	@Override
	protected void updatePlotContent(Composite plotContent) throws Exception {
		IPlot plot = getPlot();

		// Apply the basic plot features.
		plotProvider.setPlotTitle(plot.getPlotTitle());
		plotProvider.setIndependentSeries(plot.getIndependentSeries());
		plotProvider.setDependentSeries(
				plot.getDependentSeries(IPlot.DEFAULT_CATEGORY).get(1));
		plotProvider.setIntensitySeries(
				plot.getDependentSeries(IPlot.DEFAULT_CATEGORY).get(2));

		// Then, follow the default behavior. This triggers calls to hide/show
		// series based on which ones are currently enabled.
		super.updatePlotContent(plotContent);

		// Show the plot provider.
		editor.showPlotProvider(plotProvider);

		return;
	}

	/**
	 * Sets the contour flag state for this plot
	 * 
	 * @param contour
	 *            True for contour, false for normal xy graph
	 */
	public void setIsContour(boolean contour) {
		if (plotProvider != null) {
			plotProvider.setIsContour(contour);
			if (contour) {
				plotProvider.setDependentSeries(getPlot()
						.getDependentSeries(IPlot.DEFAULT_CATEGORY).get(1));
				plotProvider.setIntensitySeries(getPlot()
						.getDependentSeries(IPlot.DEFAULT_CATEGORY).get(2));
			}
			editor.refresh();
		}
		isContour = contour;
	}

}
