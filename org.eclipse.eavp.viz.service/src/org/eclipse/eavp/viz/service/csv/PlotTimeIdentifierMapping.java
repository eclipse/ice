/*******************************************************************************
 * Copyright (c) 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Claire N. Saunders (UT-Battelle, LLC.) - initial API and implementation 
 *      and/or initial documentation
 *    Jay Jay Billings (UT-Battelle, LLC.) - relocated from org.eclipse.eavp.viz 
 *      bundle
 *    Jordan Deyton (UT-Battelle, LLC.) - doc cleanup
 *******************************************************************************/
package org.eclipse.eavp.viz.service.csv;


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


