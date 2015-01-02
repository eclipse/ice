/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.client.widgets.viz.service;

import java.net.URI;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;

/**
 * This interface defines the principle type for plots created and handled by
 * the platform. Its primary purpose is to provide a specific interface for
 * manipulating plots and analysis data that is separated in purpose and scope
 * from the IVizService (perhaps completely separate, in fact).
 * 
 * When implemented in tandem with an IVizService, the class that realizes this
 * interface should encapsulate all details related to the internal workings of
 * the IVizService and refrain from exposing them to the client. For example,
 * service ids, credentials, window ids and other information should be stored
 * as private variables and not exposed in the properties map unless absolutely
 * necessary.
 * 
 * @author Jay Jay Billings
 *
 */
public interface IPlot {

	/**
	 * This operation returns a simple map of plot types that can be created by
	 * the IPlot using its data source. The map is meant to have a structure
	 * where each individual key is a type of plot - mesh, scalar, line, etc. -
	 * with a list of values of all of the plots it can create of that given
	 * type from the data source. For example, for a CSV file with three columns
	 * x, y1, y2, y3, the map might be:
	 * <p>
	 * key | value<br>
	 * line | "x vs y1", "x vs y2", "x vs y3"<br>
	 * scatter | "x vs y1", "x vs y2", "x vs y3"<br>
	 * contour | "x vs y1", "x vs y2", "x vs y3"
	 * 
	 * @return The map of valid plot types this plot can be
	 * @throws Exception
	 *             This exception indicates that the IPlot was not able to
	 *             retrieve the list of plot types and explains why.
	 */
	public Map<String, String[]> getPlotTypes() throws Exception;

	/**
	 * This operation directs the IPlot to draw itself in its parent as the
	 * specified plot type. The exact details of how the plot is drawn and what
	 * is drawn inside the parent composite are left completely up to the
	 * implementation.
	 * 
	 * This operation may be called multiple types to change its type. It is
	 * expected that the implementation will know how to clear the parent, if
	 * necessary, or to otherwise manage its own drawing service. There is no
	 * guarantee that the caller will clear the parent.
	 * 
	 * @param category
	 *            The category of the plot to create. That is, the key in the
	 *            map; something "line" or "scatter" using the example from
	 *            getPlotTypes();
	 * @param plotType
	 *            The type of plot that this IPlot should show
	 * @param parent
	 *            The composite in which the plot should be drawn.
	 * @throws Exception
	 *             This exception indicates that they IPlot could not be drawn
	 *             with either the given type or parent and explains why.
	 */
	public void draw(String category, String plotType, Composite parent)
			throws Exception;

	/**
	 * This operation returns the number of axes of the plot.
	 * 
	 * @return The number of axes or zero if the plot has not been drawn
	 */
	public int getNumberOfAxes();

	/**
	 * This operation returns properties of this IPlot that can be safely
	 * modified and/or tuned by the client. These properties should contains
	 * things such as, for example, the axis labels, the title, the subtitle,
	 * etc.
	 * 
	 * @return A map of the properties.
	 */
	public Map<String, String> getProperties();

	/**
	 * This operation updates the properties of the plot based on client-side
	 * modifications. The IPlot should redraw itself as needed if the properties
	 * changed.
	 * 
	 * @param props
	 *            The updated properties
	 * @throws Exception
	 *             This exception indicates that the IPlot could not update its
	 *             properties or redraw itself.
	 */
	public void setProperties(Map<String, String> props) throws Exception;

	/**
	 * This operation returns the data source that is plotted/drawn by this
	 * IPlot.
	 * 
	 * @return The data source
	 */
	public URI getDataSource();

	/**
	 * This operation retrieves the hostname for this IPlot's data source.
	 * 
	 * @return the hostname
	 */
	public String getSourceHost();

	/**
	 * This operation signifies whether or not the IPlot's host is local or
	 * remote.
	 * 
	 * @return True if the source is on a remote machine, false otherwise
	 */
	public boolean isSourceRemote();

}
