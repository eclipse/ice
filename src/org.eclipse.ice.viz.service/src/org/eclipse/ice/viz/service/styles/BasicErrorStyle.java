/**
 * 
 */
package org.eclipse.ice.viz.service.styles;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

/**
 * @author k6g
 *
 */
public class BasicErrorStyle extends AbstractSeriesStyle {

	public static final String COLOR = "color";
	public static final String ERROR_BAR_CAP_WIDTH = "capWidth";

	/**
	 * Constructor, just calls super() to instantiate the properties and then
	 * adds a couple more property keys
	 */
	public BasicErrorStyle() {
		super();
		properties.put(COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_RED));

		properties.put(ERROR_BAR_CAP_WIDTH, 4);
	}

}
