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
package org.eclipse.eavp.viz.service.styles;

import org.eclipse.nebula.visualization.xygraph.figures.Trace.BaseLine;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.ErrorBarType;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.TraceType;
import org.eclipse.swt.graphics.Color;

/**
 * The base class for all series styles that deal with the xy plot and editor
 * architecture.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class XYZSeriesStyle extends AbstractSeriesStyle {

	/**
	 * This property sets the color that will be used to draw the series on
	 * screen. This has no effect if series is the independent variable
	 */
	public static final String COLOR = "color";

	/**
	 * This property sets the trace type that will be used to draw the series.
	 * This should be set to a {@link TraceType} enum object.
	 */
	public static final String TYPE = "traceType";

	/**
	 * This property sets the line width that this series will be drawn with.
	 * Should be an int value, in pixels.
	 */
	public static final String LINE_WIDTH = "lineWidth";

	/**
	 * This property sets the point style that this series will be drawn with.
	 * Should be a {@link PointStyle} enum type.
	 */
	public static final String POINT = "pointStyle";

	/**
	 * This property sets the point size that this series will be drawn with.
	 * This should be an int value, specifying the size of the point in pixels.
	 */
	public static final String POINT_SIZE = "pointSize";

	/**
	 * This property sets the baseline for this series. This should be a
	 * {@link BaseLine} enum type.
	 */
	public static final String BASE_LINE = "baseLine";

	/**
	 * This property sets the area alpha value. Note- should be a number between
	 * 0 and 100, where 100 is opaque and 0 is transparent.
	 */
	public static final String AREA_ALPHA = "areaAlpha";

	/**
	 * This property sets whether to use anti aliasing to draw this series.
	 */
	public static final String ANTI_ALIASING = "antiAliasing";

	/**
	 * This property sets whether to draw the error for this series, if it has
	 * an associated error
	 */
	public static final String ERROR_ENABLED = "hasError";

	/**
	 * This property sets the type of error that is associated with this series.
	 * Should be an {@link ErrorBarType} enum type.
	 */
	public static final String ERROR_TYPE = "errorType";

	/**
	 * The constructor, sets the keyset for the properties map
	 */
	public XYZSeriesStyle(Color color) {
		super();
		properties.put(COLOR, color);
		properties.put(TYPE, TraceType.SOLID_LINE);
		properties.put(LINE_WIDTH, 2);
		properties.put(POINT, PointStyle.POINT);
		properties.put(POINT_SIZE, 4);
		properties.put(BASE_LINE, BaseLine.ZERO);
		properties.put(AREA_ALPHA, 100);
		properties.put(ANTI_ALIASING, true);
		properties.put(ERROR_ENABLED, false);
		properties.put(ERROR_TYPE, ErrorBarType.BOTH);
	}

	/**
	 * Gets a clone of this style
	 */
	public Object clone() {
		XYZSeriesStyle newStyle = new XYZSeriesStyle(null);
		newStyle.properties.clear();
		newStyle.properties.putAll(properties);
		return newStyle;

	}

}
