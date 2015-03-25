/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings
 *******************************************************************************/
package org.eclipse.ice.viz.service.csv;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.client.widgets.viz.service.IVizService;

/**
 * This class implements the IVizService interface to provide CSV plotting tools
 * to the platform via the IVizServiceFactory. It used the classes already
 * available in org.eclipse.ice.viz and provides a rudimentary plotting
 * capability.
 * 
 * It is the default "ice-plot" implementation provided by the platform.
 * 
 * @author Jay Jay Billings
 * 
 */
public class CSVVizService implements IVizService {

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#getName()
	 */
	@Override
	public String getName() {
		return "ice-plot";
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#getVersion()
	 */
	@Override
	public String getVersion() {
		return "2.0";
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 *      getConnectionProperties()
	 */
	@Override
	public Map<String, String> getConnectionProperties() {
		// There are no connection properties, but still an empty map is
		// required.
		return new HashMap<String, String>();
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 *      setConnectionProperties(java.util.Map)
	 */
	@Override
	public void setConnectionProperties(Map<String, String> props) {
		// Nothing to do
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#connect()
	 */
	@Override
	public boolean connect() {
		// No connection to be made, so just return true
		return true;
	}

	/**
	 * @throws Exception
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#createPlot(java
	 *      .net.URI)
	 */
	@Override
	public IPlot createPlot(URI file) throws Exception {

		// Create the plot and load it
		CSVPlot plot = new CSVPlot(file);
		plot.load();

		return plot;
	}

	/**
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#
	 *      hasConnectionProperties()
	 */
	@Override
	public boolean hasConnectionProperties() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IVizService#disconnect()
	 */
	@Override
	public boolean disconnect() {
		return true;
	}

}
