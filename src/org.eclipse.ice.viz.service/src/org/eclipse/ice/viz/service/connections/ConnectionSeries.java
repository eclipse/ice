/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Kasper Gammeltoft
 *******************************************************************************/

package org.eclipse.ice.viz.service.connections;

import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.ice.viz.service.ISeriesStyle;

/**
 * Represents a handle for a visit plot type, represented as an ISeries to be
 * handled by the Plot Editor. Use setLabel(String label) and getLabel() to set
 * and get the plot type.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class ConnectionSeries implements ISeries {

	/**
	 * The category for this series
	 */
	private String category;

	/**
	 * The type of plot for the viz service to render
	 */
	private String type;

	/**
	 * The style of this series
	 */
	private ISeriesStyle style;

	/**
	 * The time of this series, if the plot has time steps
	 */
	private double time;

	/**
	 * Constructor
	 */
	public ConnectionSeries(String type) {
		this.type = type;
		style = null;
		time = 0.0;
	}

	/**
	 * Returns null
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#getBounds()
	 */
	@Override
	public double[] getBounds() {
		return null;
	}

	/**
	 * Returns null
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#getDataPoints()
	 */
	@Override
	public Object[] getDataPoints() {
		// Not meant to handle the data, just a reference for the plot type!
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#setTime(double)
	 */
	@Override
	public void setTime(double time) {
		this.time = time;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#getTime()
	 */
	@Override
	public double getTime() {
		return time;
	}

	/**
	 * Returns null
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#getParentSeries()
	 */
	@Override
	public ISeries getParentSeries() {
		// VisIt Series do not have parents
		return null;
	}

	/**
	 * Gets the plot type
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#getLabel()
	 */
	@Override
	public String getLabel() {
		// The label is the type
		return type;
	}

	/**
	 * Sets the type of plot this is
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		// Set the type, the label is the type
		type = label;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#getStyle()
	 */
	@Override
	public ISeriesStyle getStyle() {
		return style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.ISeries#setStyle(org.eclipse.ice.viz.service.
	 * ISeriesStyle)
	 */
	@Override
	public void setStyle(ISeriesStyle style) {
		this.style = style;
	}

	/**
	 * Is always true
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#enabled()
	 */
	@Override
	public boolean enabled() {
		// The endabled flag is not applicable for this series
		return true;
	}

	/**
	 * Currently does nothing for this type of series!
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enable) {
		// Nothing TODO
		return;
	}

	/**
	 * Gets the category for this series
	 * 
	 * @return
	 */
	@Override
	public String getCategory() {
		return category;
	}

	/**
	 * Sets the category for this series
	 * 
	 * @param category
	 */
	public void setCategory(String category) {
		this.category = category;
	}

}
