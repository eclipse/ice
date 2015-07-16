/**
 * 
 */
package org.eclipse.ice.viz.service.styles;

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

	public static final String COLOR = "color";
	public static final String ERROR_BAR_CAP_WIDTH = "capWidth";

	/**
	 * Constructor, just calls super() to instantiate the properties and then
	 * adds a couple more property keys
	 */
	public BasicErrorStyle(Color color) {
		super();
		properties.put(COLOR, color);

		properties.put(ERROR_BAR_CAP_WIDTH, 4);
	}

}
