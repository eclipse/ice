/**
 * 
 */
package org.eclipse.ice.viz.service.csv;

import org.eclipse.ice.viz.service.ISeries;
import org.eclipse.ice.viz.service.ISeriesStyle;
import org.eclipse.ice.viz.service.styles.XYZSeriesStyle;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.TransformedList;
import ca.odell.glazedlists.event.ListEvent;

/**
 * @author Kasper Gammeltoft
 *
 */
public class CSVSeries extends TransformedList<Double, Double>
		implements ISeries {

	/**
	 * The style used to style the trace for this series on the CSV plot.
	 */
	protected ISeriesStyle style;

	/**
	 * The label to display for this series.
	 */
	protected String label;

	/**
	 * The unit for this series
	 */
	protected String unit;

	/**
	 * A flag signifying if the series is enabled. This determines if the series
	 * will be plotted or not.
	 */
	protected boolean isEnabled;

	/**
	 * Constructor, creates a new csv series with no data and the default style.
	 */
	public CSVSeries() {
		this(new BasicEventList<Double>());
	}

	/**
	 * Creates a new CSV Series with the given source list.
	 * 
	 * @param source
	 */
	protected CSVSeries(EventList<Double> source) {
		super(source);
		style = new XYZSeriesStyle();
	}

	@Override
	protected boolean isWritable() {
		return true;
	}

	@Override
	public void listChanged(ListEvent<Double> listChanges) {
		// TODO implementation
		return;
	}

	@Override
	public double[] getBounds() {
		// Creates the array to return
		double[] bounds = null;

		// Only return a valid array if there is data
		if (size() > 0) {
			// Instantiate the array
			bounds = new double[2];
			// Set the initial values to the first data point
			double min = (double) this.get(0);
			double max = (double) this.get(0);
			// Iterate and find the max and min values
			for (Double d : this) {
				if (((double) d) < min) {
					min = (double) d;
				} else if (((double) d) > max) {
					max = (double) d;
				}
			}
			// Set the values
			bounds[0] = min;
			bounds[1] = max - min;
		}
		// Finally return the array
		return bounds;
	}

	@Override
	public Object[] getDataPoints() {
		return this.toArray();
	}

	@Override
	public Object[] getDataPointsAtTime(double time) {
		return getDataPoints();
	}

	@Override
	public ISeries getParentSeries() {
		// TODO Should we have a parent/child architecture?
		return null;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public ISeriesStyle getStyle() {
		return style;
	}

	@Override
	public void setStyle(ISeriesStyle style) {
		this.style = style;
	}

	@Override
	public boolean enabled() {
		return isEnabled;
	}

	/**
	 * Sets whether this series is enabled or not.
	 * 
	 * @param enabledFlag
	 *            The enabled flag.
	 */
	public void setEnabled(boolean enabledFlag) {
		isEnabled = enabledFlag;
	}

	/**
	 * Sets the units used for this series.
	 * 
	 * @param unit
	 *            The unit to use, of which the data is in.
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * Gets the string representation of the unit for this series.
	 * 
	 * @return String the unit for this series.
	 */
	public String getUnit() {
		return unit;
	}

}
