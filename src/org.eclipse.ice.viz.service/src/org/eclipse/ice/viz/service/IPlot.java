/*******************************************************************************
 * Copyright (c) 2012, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.viz.service;

import java.util.List;

/**
 * This interface defines the principle type for plots created and handled by
 * the platform. Its primary purpose is to provide a specific interface for
 * manipulating plots using multiple series. The plot is responsible for
 * plotting the series according to their style, as that should be the only
 * configuration of the series necessary.
 * 
 * @see org.eclipse.ice.viz.service.ISeries
 * 
 * @author Jay Jay Billings, Alex McCaskey
 * @author Kasper Gammeltoft - Viz refactor for series
 *
 */
public interface IPlot extends IVizCanvas {

	/**
	 * The default category for all plots
	 */
	public static final String DEFAULT_CATEGORY = "Other";

	/**
	 * Sets the title of the plot to the specified string.
	 * 
	 * @param title
	 *            The new title for this plot
	 */
	public void setPlotTitle(String title);

	/**
	 * Gets the title of the plot to be displayed in whatever visualization
	 * service is rendering this plot
	 * 
	 * @return String The plot title
	 */
	public String getPlotTitle();

	/**
	 * Sets the series that will be on the independent axis for this plot. All
	 * other series will be plotted in reference to this series.
	 * 
	 * @param series
	 *            The independent series, used to plot the other independent
	 *            series
	 */
	public void setIndependentSeries(ISeries series);

	/**
	 * Gets the independent series for the plot. This is the series that all the
	 * other series should be plotted against.
	 * 
	 * @return ISeries the independent series.
	 */
	public ISeries getIndependentSeries();

	/**
	 * | Adds the specified series to the list of series to plot.
	 * 
	 * @param seriesToAdd
	 *            The {@link ISeries} to add.
	 * @param catagory
	 *            The category that this series should be put into, to group
	 *            when in menus
	 */
	public void addDependentSeries(String catagory, ISeries seriesToAdd);

	/**
	 * Removes the specified series from the dependent series list.
	 * 
	 * @param series
	 *            The series to remove from the list.
	 */
	public void removeDependantSeries(ISeries series);

	/**
	 * Gets all of the dependent series specified for this IPlot, as a list, for
	 * the specified category. If the category is null, will return the default
	 * category.
	 * 
	 * @return List<ISeries> all of the dependent series to be plotted.
	 */
	public List<ISeries> getAllDependentSeries(String category);

	/**
	 * Gets all of the categories currently associated with this plot.
	 * 
	 * @return The categories for this plot.
	 */
	public String[] getCategories();

}
