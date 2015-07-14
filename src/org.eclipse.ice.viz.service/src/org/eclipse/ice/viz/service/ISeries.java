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

import java.util.List;

/**
 * The {@code ISeries} interface defines the set of methods necessary to
 * describe a series in ICE used for plotting on an {@link IPlot}. The ISeries
 * type should be able to provide all of the information for plotting on the
 * platform, including the type and style of the series.
 * 
 * @author Kasper Gammeltoft
 *
 */
public interface ISeries {

	/**
	 * Adds a data point to the end of the series. The data must be a
	 * {@code double}, as that is the only type of data that the platform can
	 * plot using the current interface.
	 * 
	 * @param data
	 *            The data point to add.
	 */
	public void add(double data);

	/**
	 * Adds all of the data from the specified list to the series. Appends this
	 * list to the current data in the series.
	 * 
	 * @param data
	 *            List<Double> the list to append.
	 */
	public void addAll(List<Double> data);

	/**
	 * Removes the indicated data point from the series. Note that the series is
	 * zero indexed, so calling series.remove(4) removes the fifth data point.
	 * If the index is not within a reasonable range, should throw an index out
	 * of bounds exception.
	 * 
	 * @param index
	 *            The index of the data to remove.
	 */
	public void remove(int index);

	/**
	 * Removes all of the data points from the series, meaning that some other
	 * methods will throw exceptions until data is added back.
	 */
	public void removeAll();

	/**
	 * Gets the series as an array of double values in a {@code List<Double>}.
	 * Note that changing this List should not change the series. Use the
	 * {@link ISeries#add(double)}, {@link ISeries#remove(double)}, and
	 * {@link ISeries#removeAll()} methods to effect the series.
	 * 
	 * @return List<Double> The list of values in the series.
	 */
	public List<Double> getArray();

	/**
	 * Gets the bounds for this series. The bounds are described as the minimum
	 * value, maximum value, and the difference between the two. The array
	 * returned is a representation of these values by using two doubles. The
	 * first value is the minimum in the series. The second is the difference
	 * between the minimum and maxiumum values in the series, or the algebraic
	 * range. The maxiumum can then be easily calculated with the addition of
	 * the two numbers provided. Note- will return null if the {@code ISeries}
	 * was established but no data was ever added to it (see
	 * {@link ISeries#add(double)}).
	 * 
	 * @return double[] The bounds of the series, as given by double[2]
	 *         {minValue, range} where the first value is the min value and the
	 *         second is the range of the series.
	 */
	public double[] getBounds();

	/**
	 * Gets the number of the axis for this series. This is an indication of
	 * whether the data is dependent or independent, and limits which axis the
	 * data can be displayed on.
	 * 
	 * @return The number of the specified axis for this series. See
	 *         {@link ISeries#setAxis()} for more information on what the
	 *         returned value correlates to.
	 */
	public int getAxis();

	/**
	 * Sets the axis for this series, so that if this data is independent it
	 * will only be compared with dependent data and will not be displayed on
	 * the Y axis for a standard xyz plane, for example. </br>
	 * For XYZ, the x axis is number 1, the y is 2, and the z is three. For
	 * polar, 1 is theta, 2 is the radius, and 3 is phi. Depending on the type
	 * of plot this series will be plotted, on, independent data is typically 1
	 * while the dependent axes are numbered Incrementally from that. </br>
	 * This method does nothing if the axis is invalid, meaining less than zero
	 * or greater than the number of axes for the correlating {@link IPlot}
	 * . </br>
	 * Giving zero as the axis means that the series is enabled on all axes, as
	 * is the default behavior. Note that this method is invalid if the series
	 * has children series, as it makes no sense to specify an axis for all of
	 * the child series.
	 * 
	 * @param axis
	 *            The specified axis number.
	 */
	public void setAxis(int axis);

	/**
	 * Gets the parent series for this series. This allows for grouping of
	 * series and for one to be of direct relation to another. For example, a
	 * certain series could be the error of another series, and their
	 * relationship could be established in this way.
	 * 
	 * @return ISeries The parent series to this one.
	 */
	public ISeries getParentSeries();

	/**
	 * Adds a child series to this series. Note that there can be several
	 * different relationships amoungst series, such as one containing both the
	 * x and y series, or having error associated with the parent series.
	 * 
	 * @param child
	 */
	public void addChildSeries(ISeries child);

	/**
	 * Gets the label used to describe this series and to be shown on the
	 * graphs. This is the name of the series.
	 * 
	 * @return String the series label.
	 */
	public String getLabel();

	/**
	 * Sets the label used to describe this series and to be shown on the
	 * graphs. This is the name of the series.
	 * 
	 * @param label
	 *            The series label
	 */
	public void setLabel(String label);

	/**
	 * Sets the {@link ISeriesStyle} of the series. That style defines the
	 * color, point style, line style, default preferred axis, and many other
	 * types of style information for the series to be properly formatted when
	 * being plotted. This allows for some configuration rather than the default
	 * plot implementation to be used for every plotting purpose.
	 * 
	 * @return
	 */
	public ISeriesStyle getStyle();

	/**
	 * Sets the {@link ISeriesStyle} used for this series. That style defines
	 * the color, point style, line style, default preferred axis, and many
	 * other types of style information for the series to be properly formatted
	 * when being plotted. This allows for some configuration rather than the
	 * default plot implementation to be used for every plotting purpose.
	 * 
	 * @param style
	 */
	public void setStyle(ISeriesStyle style);

}
