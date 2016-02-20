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
 * This class defines a {@link ISeriesStyle} for the plot as a whole. The plot's
 * general properties will be set from this style, if the plot uses an XYGraph
 * as its visualization tool.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class XYZPlotStyle extends AbstractSeriesStyle {

	/**
	 * This property is the color for the title to the plot. Should be an SWT
	 * Color object.
	 */
	public static final String TITLE_COLOR = "titleColor";

	/**
	 * This property sets the font used to display the title to the plot. Should
	 * be an SWT Font object.
	 */
	public static final String TITLE_FONT = "titleFont";

	/**
	 * This property sets the color of the entire plot background. Should be an
	 * SWT Color object.
	 */
	public static final String PLOT_COLOR = "color";

	/**
	 * This property determines if the plot title will be shown. This property
	 * is a boolean- true for show the title, false for hide it.
	 */
	public static final String SHOW_TITLE = "showTitle";

	/**
	 * This property determines if the legend for the plot is shown. The legend
	 * shows the different series currently plotted and distinguishes them by
	 * their color. Should be a boolean value.
	 */
	public static final String SHOW_LEGEND = "showLegend";

	/**
	 * This property determines if the plot border is shown. Should be a
	 * boolean, true for show the border, false for hide it.
	 */
	public static final String SHOW_PLOT_BORDER = "showBorder";

	/**
	 * This property is a simple boolean denoting if the plot should be
	 * transparent, to possibly overlay plots.
	 */
	public static final String IS_TRANSPARENT = "isTransparent";

	/**
	 * Constructor, initializes the properties with their default values
	 */
	public XYZPlotStyle() {
		super();
		properties.put(TITLE_COLOR, null);
		properties.put(TITLE_FONT, null);
		properties.put(PLOT_COLOR, null);
		properties.put(SHOW_TITLE, true);
		properties.put(SHOW_LEGEND, true);
		properties.put(SHOW_PLOT_BORDER, false);
		properties.put(IS_TRANSPARENT, false);
	}

	/**
	 * Gets a clone of this style
	 */
	public Object clone() {
		XYZPlotStyle newStyle = new XYZPlotStyle();
		newStyle.properties.clear();
		newStyle.properties.putAll(properties);
		return newStyle;

	}

}
