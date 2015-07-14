/*******************************************************************************
 * Copyright (c) 2012, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service;

/**
 * This interface defines the principle type for plots created and handled by
 * the platform. Its primary purpose is to provide a specific interface for
 * manipulating plots using multiple series. The plot is responsible for
 * plotting the series according to their style, as that should be the only
 * configuration of the series necessary.
 * 
 * @see org.eclipse.ice.viz.service.ISeries
 * 
 * @author Jay Jay Billings, Alex McCaskey, Kasper Gammeltoft
 *
 */
public interface IPlot extends IVizCanvas {

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
	 * Adds an dependent series that will be plotted against the dependent
	 * series on this plot. It is up to the implementation to determine how
	 * these series will be presented.
	 * 
	 * @param series
	 *            The dependent series to add.
	 */
	public void addDependantSeries(ISeries series);
	
	/**
	 * Sets the title that the visualization service will use to label 
	 * @param axisTitle
	 */
	public void setXAxisTitle(String axisTitle);
	
	public void setYAxisTitle(String axisTitle);
	
	public void setZAxisTitle(String axisTitle);
	

}
