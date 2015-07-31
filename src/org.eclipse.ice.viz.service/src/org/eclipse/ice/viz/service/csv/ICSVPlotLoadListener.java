/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.csv;

/**
 * This is an interface for objects that need to update when a {@link CSVPlot}
 * has reloaded its data.
 * 
 * @author Jordan Deyton
 *
 */
public interface ICSVPlotLoadListener {

	/**
	 * The specified CSV plot has finished loading.
	 * 
	 * @param plot
	 *            The plot that was loaded.
	 */
	public void plotLoaded(CSVPlot plot);

}
