/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation
 *   - Kasper Gammeltoft
 *******************************************************************************/
package org.eclipse.ice.viz.service.csv;

import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.ice.viz.service.ISeriesStyle;
import org.eclipse.ice.viz.service.styles.AbstractSeriesStyle;
import org.eclipse.ice.viz.service.styles.XYZSeriesStyle;
import org.eclipse.swt.graphics.Color;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;

/**
 * This is an implementation of {@link ISeries} that holds csv data to be
 * plotted. The series should be given a label, and double values should be
 * added via the standard collections methods.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class CSVSeries extends TransformedList<Double, Double>
		implements ISeries {

	/**
	 * The style used to style the trace for this series on the CSV plot.
	 */
	protected ISeriesStyle style;

	/**
	 * The label to display for this series.
	 */
	protected String label;

	/**
	 * The unit for this series
	 */
	protected String unit;

	/**
	 * The timestamp for this series, used by the editor for displaying time
	 * varying data
	 */
	protected double time;

	/**
	 * A flag signifying if the series is enabled. This determines if the series
	 * will be plotted or not.
	 */
	protected boolean isEnabled;

	/**
	 * The parent series to this one. If this is set, then this series is either
	 * an error series or should be drawn in reference to the specified series.
	 * The style of this series will determin the behavior.
	 */
	protected ISeries parent;

	/**
	 * Null constructor
	 */
	public CSVSeries() {
		this(null);
	}

	/**
	 * Constructor, creates a new csv series with no data and the default style.
	 */
	public CSVSeries(Color color) {
		this(new BasicEventList<Double>(), color);
	}

	/**
	 * Creates a new CSV Series with the given source list.
	 * 
	 * @param source
	 */
	protected CSVSeries(EventList<Double> source, Color color) {
		super(source);
		style = new XYZSeriesStyle(color);
		time = 0;
		parent = null;
		label = "unlabeled series";
	}

	@Override
	protected boolean isWritable() {
		return true;
	}

	@Override
	public void listChanged(ListEvent<Double> listChanges) {
		// TODO implementation
		return;
	}

	@Override
	public double[] getBounds() {
		// Creates the array to return
		double[] bounds = null;

		// Only return a valid array if there is data
		if (size() > 0) {
			// Instantiate the array
			bounds = new double[2];
			// Set the initial values to the first data point
			double min = (double) this.get(0);
			double max = (double) this.get(0);
			// Iterate and find the max and min values
			for (Double d : this) {
				if (((double) d) < min) {
					min = (double) d;
				} else if (((double) d) > max) {
					max = (double) d;
				}
			}
			// Set the values
			bounds[0] = min;
			bounds[1] = max - min;
		}
		// Finally return the array
		return bounds;
	}

	@Override
	public Object[] getDataPoints() {
		return this.toArray();
	}

	@Override
	public ISeries getParentSeries() {
		return parent;
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
	public void setTime(double time) {
		this.time = time;
	}

	@Override
	public double getTime() {
		return time;
	}

	@Override
	public ISeriesStyle getStyle() {
		return style;
	}

	@Override
	public void setStyle(ISeriesStyle style) {
		this.style = style;
	}

	@Override
	public boolean enabled() {
		return isEnabled;
	}

	/**
	 * Sets whether this series is enabled or not.
	 * 
	 * @param enabledFlag
	 *            The enabled flag.
	 */
	@Override
	public void setEnabled(boolean enabledFlag) {
		isEnabled = enabledFlag;
	}

	/**
	 * Sets the units used for this series.
	 * 
	 * @param unit
	 *            The unit to use, of which the data is in.
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Gets the string representation of the unit for this series.
	 * 
	 * @return String the unit for this series.
	 */
	public String getUnit() {
		return unit;
	}

	@Override
	public Object clone() {
		CSVSeries clone = new CSVSeries();
		clone.copy(this);
		return clone;
	}

	/**
	 * Makes this series copy exactly the series provided, to have the same
	 * values and styles.
	 * 
	 * @param other
	 */
	public void copy(CSVSeries other) {
		// Go through and set all instance variables to the other series' values
		isEnabled = other.isEnabled;
		label = other.label;
		// Do not copy here is ok! This means that the parent is equal to the
		// other parent by reference
		parent = other.parent;
		// All series should inherit from this class, so this SHOULD be ok
		((AbstractSeriesStyle) style).copy((AbstractSeriesStyle) other.style);
		time = other.time;
		unit = other.unit;
		this.clear();
		this.addAll(other);
	}

	@Override
	public boolean equals(Object other) {
		// Local declarations
		boolean isEqual = false;

		// If the other object is not null and this type of object, and the
		// lists are the same, do the comparison
		if (other != null && (other instanceof CSVSeries)
				&& super.equals(other)) {
			// If they are the same reference, must be the same
			if (other == this) {
				isEqual = true;
				// Compare the series based on the instance variables
			} else {
				CSVSeries series = (CSVSeries) other;
				// Get the equality of the series
				isEqual = this.isEnabled == series.isEnabled
						&& label == series.label
						&& (parent == null ? series.parent == null
								: parent.equals(series.parent))
						&& style.equals(series.style) && time == series.time
						&& unit.equals(series.unit);

			}
		}

		// Finally return the equality of the series
		return isEqual;
	}

	@Override
	public int hashCode() {
		// Local Declarations
		int hash = super.hashCode();

		// Compute the hash code
		hash = 31 * hash + (label == null ? 0 : label.hashCode());
		hash = 31 * hash + (isEnabled ? hash : 0);
		hash = 31 * hash + (parent == null ? 0 : parent.hashCode());
		hash = 31 * hash + (style == null ? 0 : style.hashCode());
		hash = 31 * hash + (unit == null ? 0 : unit.hashCode());

		return hash;
	}

}
