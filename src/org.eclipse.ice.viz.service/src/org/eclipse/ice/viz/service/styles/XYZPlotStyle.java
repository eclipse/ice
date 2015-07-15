package org.eclipse.ice.viz.service.styles;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class XYZPlotStyle extends AbstractSeriesStyle {

	public static final String TITLE_COLOR = "titleColor";
	public static final String TITLE_FONT = "titleFont";
	public static final String PLOT_COLOR = "color";
	public static final String SHOW_TITLE = "showTitle";
	public static final String SHOW_LEGEND = "showLegend";
	public static final String SHOW_PLOT_BORDER = "showBorder";
	public static final String IS_TRANSPARENT = "isTransparent";

	public XYZPlotStyle() {
		super();
		properties.put(TITLE_COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		properties.put(TITLE_FONT, null);
		properties.put(PLOT_COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
		properties.put(SHOW_TITLE, true);
		properties.put(SHOW_LEGEND, true);
		properties.put(SHOW_PLOT_BORDER, false);
		properties.put(IS_TRANSPARENT, false);
	}

}
