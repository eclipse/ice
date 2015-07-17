/**
 * 
 */
package org.eclipse.ice.viz.service.styles;

import org.eclipse.nebula.visualization.xygraph.figures.Trace.ErrorBarType;
import org.eclipse.swt.graphics.Color;

/**
 * This style is used for the basic error series that should be attached to
 * another series. Note- a series with this style <b>needs</b> to have a parent
 * set! Otherwise it makes no sense to have an error series be plotted.
 * 
 * @see org.eclipse.ice.viz.service.style.AbstractSeriesStyle
 * @see org.eclipse.ice.viz.service.ISeries
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

	public static final String ERROR_BAR_TYPE = "errorType";

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

}
