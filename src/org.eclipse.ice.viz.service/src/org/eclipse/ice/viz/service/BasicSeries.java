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

package org.eclipse.ice.viz.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Meant to be the base implementation of {@link ISeries}. This class is meant
 * to be subclassed to provide more specific implementations to what the series
 * needs to be able to do.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class BasicSeries implements ISeries {

	/**
	 * The series of data points, as doubles
	 */
	protected ArrayList<Double> series;

	/**
	 * The parent series to this one.
	 */
	protected ISeries parent;

	/**
	 * The label used to represent this series
	 */
	protected String label;

	/**
	 * The style used to render this series, see {@link ISeriesStyle}
	 */
	protected ISeriesStyle style;

	public BasicSeries() {
		series = new ArrayList<Double>();
	}

	@Override
	public void add(double data) {
		series.add(data);
	}

	@Override
	public void addAll(List<Double> data) {
		series.addAll(data);
	}

	@Override
	public void remove(int index) {
		series.remove(index);
	}

	@Override
	public void removeAll() {
		series.clear();
	}

	@Override
	public List<Double> getArray() {
		List<Double> copy = new ArrayList<Double>();
		Collections.copy(copy, series);
		return copy;
	}

	@Override
	public double[] getBounds() {
		// Local declarations
		double[] range;
		// If the series has any data:
		if (series.size() > 0) {
			// Create the array
			range = new double[2];

			// Declarations
			double max = series.get(0);
			double min = series.get(0);

			// Go through and find the max and min values
			for (int i = 0; i < series.size(); i++) {
				double dataPoint = series.get(i);
				if (dataPoint < min) {
					min = dataPoint;
				} else if (dataPoint > max) {
					max = dataPoint;
				}

			}

			// Finally set the array values
			range[0] = min;
			range[1] = max - min;

			// Otherwise, set the range to null as there are no data values
		} else {
			range = null;
		}

		// Return the range
		return range;
	}

	@Override
	public ISeries getParentSeries() {
		return parent;
	}

	@Override
	public void setParentSeries(ISeries parent) {
		this.parent = parent;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;

	}

	@Override
	public ISeriesStyle getStyle() {
		return style;
	}

	@Override
	public void setStyle(ISeriesStyle style) {
		this.style = style;

	}

}
