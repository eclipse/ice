package org.eclipse.ice.viz.service.styles;

import org.eclipse.ice.viz.service.AbstractSeriesStyle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class AxisStyle extends AbstractSeriesStyle {

	public static final String AXIS_TITLE = "title";
	public static final String TITLE_FONT = "titleFont";
	public static final String SCALE_FONT = "scaleFont";
	public static final String AXIS_COLOR = "color";
	public static final String IS_LOG = "isLog";
	public static final String AUTO_SCALE = "autoScale";
	public static final String MIN_VALUE = "minimum";
	public static final String MAX_VALUE = "maximum";
	public static final String IS_TIME_FORMAT = "timeFormat";
	public static final String AUTO_FORMAT = "autoFormat";
	public static final String SHOW_GRID_LINE = "showLine";
	public static final String GRID_LINE_IS_DASHED = "dashedGridLine";
	public static final String GRID_LINE_COLOR = "gridLineColor";

	public AxisStyle() {
		super();
		properties.put(AXIS_TITLE, "Axis");
		properties.put(TITLE_FONT, Display.getCurrent().getSystemFont());
		properties.put(SCALE_FONT, Display.getCurrent().getSystemFont());
		properties.put(AXIS_COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		properties.put(IS_LOG, false);
		properties.put(AUTO_SCALE, true);
		properties.put(MIN_VALUE, 0);
		properties.put(MAX_VALUE, 100);
		properties.put(IS_TIME_FORMAT, false);
		properties.put(AUTO_FORMAT, true);
		properties.put(SHOW_GRID_LINE, true);
		properties.put(GRID_LINE_IS_DASHED, true);
		properties.put(GRID_LINE_COLOR,
				Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
	}

}