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
package org.eclipse.ice.viz.service.styles;

import org.eclipse.nebula.visualization.xygraph.figures.Trace.BaseLine;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.PointStyle;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.TraceType;
import org.eclipse.swt.graphics.Color;

/**
 * The base class for all series styles that deal with the xyz plot and editor
 * architecture.
 * 
 * @author Kasper Gammeltoft
 *
 */
public class XYZSeriesStyle extends AbstractSeriesStyle {

	public static final String COLOR = "color";
	public static final String TYPE = "traceType";
	public static final String LINE_WIDTH = "lineWidth";
	public static final String POINT = "pointStyle";
	public static final String POINT_SIZE = "pointSize";
	public static final String BASE_LINE = "baseLine";
	public static final String AREA_ALPHA = "areaAlpha";
	public static final String ANTI_ALIASING = "antiAliasing";

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
	}

}
