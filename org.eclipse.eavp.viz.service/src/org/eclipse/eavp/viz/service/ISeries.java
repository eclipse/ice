/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Kasper Gammeltoft - Initial API and implementation and/or initial documentation 
 *   Jordan Deyton - renamed enabled() to isEnabled()
 *******************************************************************************/

package org.eclipse.eavp.viz.service;

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
	 * Gets the current category that this series is in
	 * 
	 * @return
	 */
	public String getCategory();

	/**
	 * Gets all of the data points for this series, used to plot the series no
	 * matter what visualization service is being used. It is up to the service
	 * to know what type of objects are being stored in the array and caste
	 * appropriately.
	 * 
	 * @return Object[] An array containing all of the data points for this
	 *         series.
	 */
	public Object[] getDataPoints();

	/**
	 * Gets the label used to describe this series and to be shown on the
	 * graphs. This is the name of the series.
	 * 
	 * @return String the series label.
	 */
	public String getLabel();

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
	 * Gets the timestamp for this series.
	 * 
	 * @return int The time for this series.
	 */
	public double getTime();

	/**
	 * Gets if this series is enabled or not. This determines which series is
	 * plotted when they are added to different plots.
	 * 
	 * @return True if the series is enabled and will be shown on a plot, false
	 *         if otherwise
	 */
	public boolean isEnabled();

	/**
	 * Sets the enabled state of this series. This determines if the series will
	 * be drawn.
	 * 
	 * @param enable
	 *            The state of the series. True will enable the series for
	 *            plotting, false will disable the series.
	 */
	public void setEnabled(boolean enable);

	/**
	 * Sets the label used to describe this series and to be shown on the
	 * graphs. This is the name of the series.
	 * 
	 * @param label
	 *            The series label
	 */
	public void setLabel(String label);

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

	/**
	 * Sets the time for this series to the specified time. Should be greater
	 * than or equal to 0.
	 * 
	 * @param time
	 *            The time for this series.
	 */
	public void setTime(double time);

}
