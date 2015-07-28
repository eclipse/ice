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
package org.eclipse.ice.viz.service;

/**
 * his class provides a basic, limited implementation of certain features of
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
	 * Implements a method from ISeries.
	 */
	@Override
	public double[] getBounds() {
		return null;
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public Object[] getDataPoints() {
		return null;
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public void setTime(double time) {
		// Nothing to do.
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public double getTime() {
		return 0.0;
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public ISeries getParentSeries() {
		return null;
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public ISeriesStyle getStyle() {
		return style;
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public void setStyle(ISeriesStyle style) {
		this.style = style;
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public boolean enabled() {
		return enabled;
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public void setEnabled(boolean enable) {
		this.enabled = enable;
	}

	/*
	 * Implements a method from ISeries.
	 */
	@Override
	public String getCategory() {
		return null;
	}

}
