package org.eclipse.ice.viz.service.csv;


/**
 * 
 * @author claire
 *
 */
public class PlotTimeIdentifierMapping {
	/**
	 * The provider's source
	 */
	private String plotTitle;
	/**
	 * The provider's time
	 */
	private Double seriesTime;

	/**
	 * Constructor
	 * 
	 * @param plotTitle
	 * @param time
	 */
	public PlotTimeIdentifierMapping(String plotTitle, Double time) {
		this.plotTitle = plotTitle;
		seriesTime = time;
	}

	/**
	 * Accessor for the source
	 * 
	 * @return
	 */
	public String getPlotTitle() {
		return plotTitle;
	}

	/**
	 * Accessor for the time
	 * 
	 * @return
	 */
	public Double getTime() {
		return seriesTime;
	}
}


