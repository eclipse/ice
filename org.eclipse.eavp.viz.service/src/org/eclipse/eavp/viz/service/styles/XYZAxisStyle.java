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
package org.eclipse.eavp.viz.service.styles;

import org.eclipse.eavp.viz.service.ISeriesStyle;

/**
 * This class defines the {@link ISeriesStyle} that can be applied to the axes
 * for an XYGraph instance. This is useful for configuring the plot to be drawn
 * in a specific way, for instance if the user wants to have a log scale rather
 * than a linear one, and the developer knows this beforehand.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class XYZAxisStyle extends AbstractSeriesStyle {

	/**
	 * This property sets the axis title. This should be a String value.
	 */
	public static final String AXIS_TITLE = "title";

	/**
	 * This property sets the font used to draw the axis title. Should be an SWT
	 * Font object.
	 */
	public static final String TITLE_FONT = "titleFont";

	/**
	 * This sets the font used to draw the scale along this axis. Should be an
	 * SWT Font object.
	 */
	public static final String SCALE_FONT = "scaleFont";

	/**
	 * This property sets the color used to draw this axis. This effects the
	 * title and scale. Should be an SWT Color object.
	 */
	public static final String AXIS_COLOR = "color";

	/**
	 * This property determines if the axis is a logarithmic scale or not.
	 * Should be a boolean value- true for use log scale, false for use linear.
	 */
	public static final String IS_LOG = "isLog";

	/**
	 * This property is used to determine if the axis should autoscale. Should
	 * be a boolean value- true for should autoscale, false for should scale
	 * manually.
	 */
	public static final String AUTO_SCALE = "autoScale";

	/**
	 * This is used for manually scaling the axis. This should be the min value
	 * set to this axis, as a double.
	 */
	public static final String MIN_VALUE = "minimum";

	/**
	 * This is used for manually scaling the axis. This should be the max value
	 * set to this axis, as a double.
	 */
	public static final String MAX_VALUE = "maximum";

	/**
	 * This sets the time format used by this axis.
	 */
	public static final String IS_TIME_FORMAT = "timeFormat";

	/**
	 * This determines if the axis auto-formats the scale and values. Should be
	 * a boolean.
	 */
	public static final String AUTO_FORMAT = "autoFormat";

	/**
	 * This boolean determines if the grid line is shown for this axis. True for
	 * show the line, false for do not.
	 */
	public static final String SHOW_GRID_LINE = "showLine";

	/**
	 * This property determines if the grid line is dashed when it is drawn, or
	 * if it is solid. Should be a boolean.
	 */
	public static final String GRID_LINE_IS_DASHED = "dashedGridLine";

	/**
	 * This property sets the grid line color for this axis. Should be a SWT
	 * Color object.
	 */
	public static final String GRID_LINE_COLOR = "gridLineColor";

	/**
	 * Constructor, sets the default values.
	 */
	public XYZAxisStyle() {
		super();
		properties.put(AXIS_TITLE, "Axis");
		properties.put(TITLE_FONT, null);
		properties.put(SCALE_FONT, null);
		properties.put(AXIS_COLOR, null);
		properties.put(IS_LOG, false);
		properties.put(AUTO_SCALE, true);
		properties.put(MIN_VALUE, 0);
		properties.put(MAX_VALUE, 100);
		properties.put(IS_TIME_FORMAT, false);
		properties.put(AUTO_FORMAT, true);
		properties.put(SHOW_GRID_LINE, true);
		properties.put(GRID_LINE_IS_DASHED, true);
		properties.put(GRID_LINE_COLOR, null);
	}

	/**
	 * Gets a clone of this style
	 */
	public Object clone() {
		XYZAxisStyle newStyle = new XYZAxisStyle();
		newStyle.properties.clear();
		newStyle.properties.putAll(properties);
		return newStyle;

	}

}