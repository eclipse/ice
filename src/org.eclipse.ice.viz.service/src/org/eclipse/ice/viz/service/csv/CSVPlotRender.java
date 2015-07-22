/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Kasper Gammeltoft, Jay Jay Billings, Anna Wojtowicz, Alex McCaskey
 *******************************************************************************/
package org.eclipse.ice.viz.service.csv;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.client.common.ActionTree;
import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.ice.viz.service.PlotRender;
import org.eclipse.ice.viz.service.styles.XYZAxisStyle;
import org.eclipse.ice.viz.service.styles.XYZPlotStyle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * This class provides a {@link PlotRender} for a {@link CSVPlot}. This links
 * the plot to the plot provider, which is used by the editor to draw the plot.
 * Each plot render should be unique to a specific composite that it is being
 * drawn on and the CSVPlot object that is being plotted. This plot render is
 * also responsible for constructing a context menu that allows for the user to
 * add and remove plots. The plots that are initially drawn on the editor are
 * those that are enabled on the plot when this plot render is constructed.
 * 
 * TODO: Need to add listeners to the plot or series to see if they change, and
 * update if so.
 * 
 * @author Kasper Gammeltoft, Jay Jay Billings, Anna Wojtowicz, Alex McCaskey
 *
 */
public class CSVPlotRender extends PlotRender {

	/**
	 * The editor in which the CSV plot is rendered.
	 */
	private final CSVPlotEditor editor;

	/**
	 * The provider responsible for maintaining the plot configuration.
	 */
	private final PlotProvider plotProvider;

	/**
	 * A tree of JFace {@code Action}s for adding new series to the drawn plot.
	 */
	private final ActionTree addSeriesTree;
	/**
	 * A tree of JFace {@code Action}s for removing plotted series from the
	 * drawn plot.
	 */
	private final ActionTree removeSeriesTree;

	/**
	 * A map keyed on the series and containing the ActionTrees for removing
	 * them from the plot.
	 */
	private final Map<ISeries, ActionTree> seriesMap = new HashMap<ISeries, ActionTree>();

	/**
	 * @param parent
	 * @param plot
	 */
	public CSVPlotRender(Composite parent, CSVPlot plot) {
		super(parent, plot);

		// Create the editor and all required providers.
		editor = new CSVPlotEditor();
		plotProvider = new PlotProvider();

		// Get the plot title
		String plotTitle = plot.getPlotTitle();

		// Set the title for the new plot provider
		plotProvider.setPlotTitle(plotTitle);

		// Set the plot and axes styles
		plotProvider.setPlotStyle((XYZPlotStyle) plot.getPlotStyle());
		plotProvider.setXAxisStyle((XYZAxisStyle) plot.getXAxisStyle());

		plotProvider.setIndependentSeries(plot.getIndependentSeries());

		// Create the plot inside the parent Composite.
		editor.createPartControl(parent);

		// Get the child Composite used to render the
		Composite canvas = editor.getPlotCanvas();

		// Get the context Menu for the parent Composite, or create a new
		// one if the parent lacks a context Menu.
		Menu menu = parent.getMenu();
		if (menu == null) {
			MenuManager menuManager = new MenuManager();
			menu = menuManager.createContextMenu(canvas);
		}

		// Create the ActionTrees for adding and removing series on the fly.
		addSeriesTree = new ActionTree("Add Series");
		removeSeriesTree = new ActionTree("Remove Series");
		final Separator separator = new Separator();
		final ActionTree clearAction = new ActionTree(new Action("Clear Plot") {
			@Override
			public void run() {
				clear();
				refresh();
			}
		});

		// Fill out the add series tree. This tree will never need to be
		// updated.
		for (ISeries series : plot.getAllDependentSeries(null)) {
			if (series != null) {
				// Create Actions for all the types. Each Action should call
				// addSeries(...) with the specified series
				final ISeries finSeries = series;
				addSeriesTree.add(new ActionTree(new Action(series.getLabel()) {
					@Override
					public void run() {
						finSeries.setEnabled(true);
						addSeries(finSeries);
						refresh();
					}
				}));

				// Add the series to plot right away to the provider if it is
				// enabled
				if (series.enabled()) {
					addSeries(series);
					refresh();
				}
			}
		}
		final Composite menuComp = this.parent;

		// When the Menu is about to be shown, add the add/remove series
		// actions to it.
		menu.addMenuListener(new MenuListener() {
			@Override
			public void menuHidden(MenuEvent e) {
				// Nothing to do.
			}

			@Override
			public void menuShown(MenuEvent e) {

				// Rebuild the menu.
				Menu menu = (Menu) e.widget;
				// TODO- think of better way to re-add the menu items. This just
				// takes over the entire context menu, ignoring any context for
				// the parent composites and on up the hierarchy
				// if (menuComp.getMenu() == null) {
				for (MenuItem item : menu.getItems()) {
					item.dispose();
				}
				// }

				// Add the items back, to refresh
				addSeriesTree.getContributionItem().fill(menu, -1);
				removeSeriesTree.getContributionItem().fill(menu, -1);
				separator.fill(menu, -1);
				clearAction.getContributionItem().fill(menu, -1);

			}
		});

		// Set the context Menu for the main plot canvas. The slider can
		// have its own menu set later.
		editor.getPlotCanvas().setMenu(menu);

		return;

	}

	/**
	 * Gets the editor for this csv plot render
	 * 
	 * @return CSVPlotEditor The plot editor
	 */
	public CSVPlotEditor getEditor() {
		return editor;
	}

	/**
	 * Gets the plot provider for this plot render
	 * 
	 * @return
	 */
	public PlotProvider getPlotProvider() {
		return plotProvider;
	}

	/**
	 * Refreshes the drawn plot after a change has occurred.
	 */
	@Override
	public void refresh() {
		// Add the new plot to the editor.
		if (plotProvider.getIndependentSeries() != null
				&& plotProvider.getSeriesAtTime(0.0) != null) {
			editor.showPlotProvider(plotProvider);
		}
	}

	/**
	 * Disposes of the drawn plot and all related resources.
	 */
	public void dispose() {
		// Dispose of PlotRender resources
		super.disposeInfoComposite(infoComposite);
		super.disposePlotComposite(plotComposite);

	}

	/**
	 * Adds a new series to the drawn plot.
	 *
	 * @param category
	 *            The category of the series to add.
	 * @param type
	 *            The type of the series to add.
	 * @param drawnPlot
	 *            The drawn plot that will get a new series.
	 */
	public void addSeries(ISeries series) {
		// Reset the plot time to the initial time.
		double plotTime = 0.0;// dataProvider.getTimes().get(0);

		// Only allow enabled series that are not already plotted
		if (series.enabled() && !seriesMap.containsKey(series)) {
			// Add this new series to the plot provider
			plotProvider.addSeries(plotTime, series);

			final ISeries finSeries = series;
			// Add an ActionTree to remove the series.
			ActionTree tree = new ActionTree(new Action(series.getLabel()) {
				@Override
				public void run() {
					removeSeries(finSeries);
					finSeries.setEnabled(false);
					refresh();
				}
			});
			removeSeriesTree.add(tree);

			// Store the series and ActionTree for later reference.
			seriesMap.put(finSeries, tree);
		}

		return;
	}

	/**
	 * Removes the specified series from the drawn plot.
	 *
	 * @param series
	 *            The series to remove.
	 */
	public void removeSeries(ISeries series) {
		ActionTree tree = seriesMap.remove(series);
		if (tree != null) {
			double plotTime = 0.0;// dataProvider.getTimes().get(0);
			removeSeriesTree.remove(tree);
			plotProvider.removeSeries(plotTime, series);
			editor.removeSeries(series);
			seriesMap.remove(series);
		}
		return;
	}

	/**
	 * Clears all series from the drawn plot.
	 */
	public void clear() {
		double plotTime = 0.0;// dataProvider.getTimes().get(0);
		for (ISeries series : seriesMap.keySet()) {
			plotProvider.removeSeries(plotTime, series);
			editor.removeSeries(series);
			series.setEnabled(false);
		}
		seriesMap.clear();
		removeSeriesTree.removeAll();
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.PlotRender#clearCache()
	 */
	@Override
	protected void clearCache() {
		// Nothing TODO
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.PlotRender#createPlotComposite(org.eclipse.
	 * swt.widgets.Composite, int)
	 */
	@Override
	protected Composite createPlotComposite(Composite parent, int style)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.PlotRender#updatePlotComposite(org.eclipse.
	 * swt.widgets.Composite)
	 */
	@Override
	protected void updatePlotComposite(Composite plotComposite)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
