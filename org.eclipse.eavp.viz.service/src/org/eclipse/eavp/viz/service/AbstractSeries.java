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
package org.eclipse.eavp.viz.service;

/**
 * This class provides a basic, limited implementation of certain features of
 * {@link ISeries}. This class does not, however, manage the data, parent
 * series, or category.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractSeries implements ISeries {

	/**
	 * Whether or not the series is enabled (or whether it should be drawn).
	 */
	private boolean enabled = false;

	/**
	 * A label for the series.
	 */
	private String label = null;

	/**
	 * A style for the series.
	 */
	private ISeriesStyle style = null;

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#getBounds()
	 */
	@Override
	public double[] getBounds() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#getCategory()
	 */
	@Override
	public String getCategory() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#getDataPoints()
	 */
	@Override
	public Object[] getDataPoints() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#getParentSeries()
	 */
	@Override
	public ISeries getParentSeries() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#getStyle()
	 */
	@Override
	public ISeriesStyle getStyle() {
		return style;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#getTime()
	 */
	@Override
	public double getTime() {
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#isEnabled()
	 */
	@Override
	public boolean isEnabled() {
		return enabled;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#setStyle(org.eclipse.eavp.viz.service.ISeriesStyle)
	 */
	@Override
	public void setStyle(ISeriesStyle style) {
		this.style = style;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.ISeries#setTime(double)
	 */
	@Override
	public void setTime(double time) {
		// Nothing to do.
	}

}
