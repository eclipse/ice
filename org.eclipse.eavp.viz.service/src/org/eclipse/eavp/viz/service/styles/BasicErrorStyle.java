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

import org.eclipse.eavp.viz.service.ISeries;
import org.eclipse.eavp.viz.service.ISeriesStyle;
import org.eclipse.nebula.visualization.xygraph.figures.Trace.ErrorBarType;
import org.eclipse.swt.graphics.Color;

/**
 * This style is used for the basic error series that should be attached to
 * another series. Note- a series with this style <b>needs</b> to have a parent
 * set! Otherwise it makes no sense to have an error series be plotted.
 * 
 * @see org.eclipse.eavp.viz.service.style.AbstractSeriesStyle
 * @see org.eclipse.eavp.viz.service.ISeries
 * 
 * @author Kasper Gammeltoft
 *
 */
public class BasicErrorStyle extends AbstractSeriesStyle {

	/**
	 * This property sets the color for the error bars. Takes an swt color.
	 */
	public static final String COLOR = "color";

	/**
	 * This property sets the width of the error bar caps, in pixels.
	 */
	public static final String ERROR_BAR_CAP_WIDTH = "capWidth";

	/**
	 * This property sets the type of error associated with this style. This
	 * should be an {@link ErrorBarType} enum type.
	 */
	public static final String ERROR_BAR_TYPE = "errorType";

	/**
	 * Constructor, with all the defaults set. Instanciates the properties then
	 * adds a couple more property keys
	 */
	public BasicErrorStyle() {
		this(null);
	}

	/**
	 * Constructor, just calls super() to instantiate the properties and then
	 * adds a couple more property keys
	 */
	public BasicErrorStyle(Color color) {
		super();
		properties.put(COLOR, color);
		properties.put(ERROR_BAR_CAP_WIDTH, 4);
		properties.put(ERROR_BAR_TYPE, ErrorBarType.BOTH);
	}

	public BasicErrorStyle(ISeries parent, Color color) {
		super();
		properties.put(COLOR, color);
		properties.put(ERROR_BAR_CAP_WIDTH, 4);
		properties.put(ERROR_BAR_TYPE, ErrorBarType.BOTH);
		ISeriesStyle parentStyle = parent.getStyle();
		// Sets the parent properties to be the same as these error properties
		if (parentStyle != null && parentStyle instanceof XYZSeriesStyle) {
			XYZSeriesStyle xyStyle = (XYZSeriesStyle) parentStyle;
			xyStyle.setProperty(XYZSeriesStyle.ERROR_ENABLED, true);
			xyStyle.setProperty(XYZSeriesStyle.ERROR_TYPE,
					properties.get(ERROR_BAR_TYPE));
		}
	}

	/**
	 * Gets a clone of this style
	 */
	public Object clone() {
		BasicErrorStyle newStyle = new BasicErrorStyle();
		newStyle.properties.clear();
		newStyle.properties.putAll(properties);
		return newStyle;

	}

}
