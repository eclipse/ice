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
	 * The constant value representing the x axis for a plot in xyz space. Use
	 * for {@link ISeries#setAxis(int)}
	 */
	public static final int X_AXIS = 1;

	/**
	 * The constant value representing the y axis for a plot in xyz space. Use
	 * for {@link ISeries#setAxis(int)}
	 */
	public static final int Y_AXIS = 2;

	/**
	 * The constant value representing the z axis for a plot in xyz space. Use
	 * for {@link ISeries#setAxis(int)}
	 */
	public static final int Z_AXIS = 3;

	/**
	 * The constant value representing the theta variable (angle from polar axis
	 * to radius around the unit circle) for a plot in polar space. Use for
	 * {@link ISeries#setAxis(int)}
	 */
	public static final int THETA_AXIS = 1;

	/**
	 * The constant value representing the radius variable for a plot in polar
	 * space. Use for {@link ISeries#setAxis(int)}
	 */
	public static final int RADIAL_AXIS = 2;

	/**
	 * The constant value representing the phi variable (angle from the plane of
	 * the unit circle to the radius) for a plot in polar space. Use for
	 * {@link ISeries#setAxis(int)}
	 */
	public static final int PHI_AXIS = 3;

	/**
	 * This defines the type of series a specific series can be. The data type
	 * allows for a normal series to be plotted. The parent type signifies that
	 * this should be plotted using the provided children series. Note that
	 * changing the axis number overrides this type, as they are only used for
	 * initial easy configuration.
	 * 
	 * @author k6g
	 *
	 */
	public enum SeriesType {
		DATA, PARENT, ERROR, LOW_ERROR, HIGH_ERROR;
	}

	/**
	 * The series of data points, as doubles
	 */
	protected ArrayList<Double> series;

	/**
	 * The parent series to this one.
	 */
	protected ISeries parent;

	/**
	 * The list of children series to this series.
	 */
	protected List<ISeries> children;

	/**
	 * The label used to represent this series
	 */
	protected String label;

	/**
	 * The style used to render this series. </br>
	 * 
	 * @see org.eclipse.ice.viz.service.ISeriesStyle
	 */
	protected ISeriesStyle style;

	/**
	 * The axis that this series should be plotted on
	 */
	protected int axis;

	/**
	 * A flag signalling if this series is enabled or not. If not, then it will
	 * not be plotted and will not be shown to the user in any way.
	 */
	protected boolean enabled;

	/**
	 * The series type for this series.
	 */
	protected SeriesType type;

	/**
	 * The unit for this series (Eg. m/s or g/mol).
	 */
	protected String unit;

	/**
	 * Constructor, sets the default values. The default axis is 0, or all, and
	 * the default type is SeriesType.DATA. The series is enabled by default.
	 */
	public BasicSeries() {
		// Initialize variables to defaults
		series = new ArrayList<Double>();
		children = new ArrayList<ISeries>();
		parent = null;
		label = "";
		axis = 0;
		type = SeriesType.DATA;
		enabled = true;
		unit = "";
	}

	/**
	 * Constructor, initiallizes with defaults and the specified parameters.
	 * 
	 * @param name
	 *            The name of the series, retreived with
	 *            {@link ISeries#getLabel()}
	 * @param type
	 *            The series type.
	 */
	public BasicSeries(String name, SeriesType type) {
		// Initialize variables to defaults
		series = new ArrayList<Double>();
		children = new ArrayList<ISeries>();
		parent = null;
		label = "";
		axis = 0;
		this.type = type;
		enabled = true;
		unit = "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#add(double)
	 */
	@Override
	public void add(double data) {
		series.add(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#addAll(java.util.List)
	 */
	@Override
	public void addAll(List<Double> data) {
		series.addAll(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#remove(int)
	 */
	@Override
	public void remove(int index) {
		series.remove(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#removeAll()
	 */
	@Override
	public void removeAll() {
		series.clear();
	}

	/**
	 * Gets the series as an array. Is a deep copy, and will not effect the
	 * series. You must use the add and remove functions to change the series
	 * values.
	 * 
	 * @return List<Double> The array of values corresponding to this series
	 */
	@Override
	public List<Double> getArray() {
		List<Double> copy = new ArrayList<Double>();
		Collections.copy(copy, series);
		return copy;
	}

	/**
	 * Gets the bounds of this series, as an array of doubles.
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#getBounds()
	 */
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

	/**
	 * Gets the parent, or null if series is top level
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#getParentSeries()
	 */
	@Override
	public ISeries getParentSeries() {
		return parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.ISeries#addChildSeries(org.eclipse.ice.viz.
	 * service.ISeries)
	 */
	@Override
	public void addChildSeries(ISeries child) {
		if (child != null) {
			children.add(child);
			this.type = SeriesType.PARENT;
		}
	}

	/**
	 * The index of the child.
	 * 
	 * @param index
	 * @return
	 */
	public ISeries getChild(int index) {
		if (index < 0 || index >= children.size()) {
			throw new IndexOutOfBoundsException();
		} else {
			return children.get(index);
		}
	}

	/**
	 * Returns a copy of all of the children for this series. Note- editing
	 * these children will not effect the acctual child series for this series.
	 * Use {@link BasicSeries#addChildSeries(ISeries)} and
	 * {@link BasicSeries#removeChildSeries(ISeries)} and
	 * {@link BasicSeries#getChild(int)} to mainipulate the children.
	 * 
	 * @return
	 */
	public List<ISeries> getChildren() {
		List<ISeries> copy = new ArrayList<ISeries>();
		Collections.copy(copy, children);
		return copy;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#getLabel()
	 */
	@Override
	public String getLabel() {
		return label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
		this.label = label;

	}

	/**
	 * Gets the unit used for this series's data.
	 * 
	 * @return <code>String</code> the unit.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Sets the unit used for this series's data.
	 * 
	 * @param unit
	 *            The unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Gets the type that this series is.
	 * 
	 * @see org.eclipse.ice.viz.service.BasicSeries.SeriesType
	 * @return The {@link SeriesType} for this series.
	 */
	public SeriesType getType() {
		return type;
	}

	/**
	 * Sets the series type for this series
	 * 
	 * @param type
	 *            The {@link SeriesType}.
	 * @see org.eclipse.ice.viz.service.BasicSeries.SeriesType
	 */
	public void setType(SeriesType type) {
		this.type = type;
	}

	/**
	 * If this series is enabled or not. If not, then this series is not graphed
	 * or displayed.
	 * 
	 * @return boolean Returns true if the series is enabled, false if
	 *         otherwise.
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets this series to the desired enabled state. If true, then the series
	 * behaves as normal, and is availiable to be graphed and edited by the
	 * user. If false, then the series is not shown.
	 * 
	 * @param enabled
	 *            If the series should be enabled or not.
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#getAxis()
	 */
	@Override
	public int getAxis() {
		return axis;
	}

	/**
	 * Not able to tell if the IPlot that this will be plotted on has the
	 * specified number of axes, so for now make sure that it is a positive axis
	 * and that this is not a parent series.
	 * 
	 * @see org.eclipse.ice.viz.service.ISeries#setAxis(int)
	 */
	@Override
	public void setAxis(int axis) {
		// Local declarations
		boolean axisValid = true;
		// Only set the axis if it is valid. No good way of checking this yet,
		// other than the axis must be greater than or equal to zero for now.
		// Also make sure that we do not have a series with a specified axis
		// that has a child data series (as that does not make sense).
		if (axis >= 0) {

			for (ISeries child : children) {
				if (((BasicSeries) child).getType().equals(SeriesType.DATA)) {
					axisValid = false;
					break;
				}
			}

			// Set the axis if it is valid.
			if (axisValid) {
				this.axis = axis;
			}
		}

		return;
	}
	
	/*
	@Override
	public boolean equals(Object other){
		boolean isEqual = true;
		if(other==null){
			isEqual = false;
		} else {
			if(other instanceof BasicSeries){
				BasicSeries otherSeries = (BasicSeries)other;
				
			}
		}
	}
	*/

	@Override
	public int hashCode() {
		// Get the object hashcode to start with
		int hash = super.hashCode();

		// Compute the hash values of all of the instance variables and assemble
		// them to form the unique hash for this object
		hash = 31 * hash + series.hashCode();
		hash = 31 * hash + parent.hashCode();
		hash = 31 * hash + children.hashCode();
		hash = 31 * hash + label.hashCode();
		hash = 31 * hash + (style == null ? 0 : style.hashCode());
		hash = 31 * hash + 31 * axis;
		hash = 31 * hash + (Boolean.hashCode(enabled));
		hash = 31 * hash + type.hashCode();
		return hash;

	}

	/**
	 * 
	 * 
	 * @param toCopy
	 *            The series to copy this one into
	 */
	public void copy(BasicSeries toCopy) {
		// Make sure the copy is not null
		if (toCopy != null) {
			// Go through all of the instance variables and set them with the
			// values from the other basic series. Note that getArray() and
			// getChildren() already return deep copies, so those methods are ok
			// to use here. We also want the parents to be the same (not deep
			// copy, but we want this.parent == toCopy.getParent() to be true).
			this.series = (ArrayList<Double>) toCopy.getArray();
			this.parent = toCopy.getParentSeries();
			this.children = toCopy.getChildren();
			this.label = toCopy.getLabel();
			this.style = toCopy.getStyle();
			this.axis = toCopy.getAxis();
			this.enabled = toCopy.isEnabled();
			this.type = toCopy.getType();
		}
		return;
	}

	/**
	 * Gets a deep copy of this series
	 */
	@Override
	public BasicSeries clone() {
		// Create a new basic series and have that clone this one.
		BasicSeries clone = new BasicSeries();
		clone.copy(this);
		// Return the new cloned series.
		return clone;
	}

}
