/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jay Jay Billings, Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.visit;

import gov.lbnl.visit.swt.VisItSwtConnection;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.ice.viz.service.PlotRender;
import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.ice.viz.service.connections.IConnectionAdapter;
import org.eclipse.ice.viz.service.connections.visit.VisItConnectionAdapter;
import org.eclipse.swt.widgets.Composite;

import visit.java.client.FileInfo;
import visit.java.client.ViewerMethods;

/**
 * This class provides the VisIt implementation for an IPlot.
 * 
 * @author Jay Jay Billings, Jordan Deyton
 * 
 */
public class VisItPlot extends ConnectionPlot<VisItSwtConnection> {

	// TODO We should manage the window IDs here.

	/**
	 * The default constructor.
	 * 
	 * @param vizService
	 *            The visualization service responsible for this plot.
	 */
	public VisItPlot(VisItVizService vizService) {
		super(vizService);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.MultiPlot#createPlotRender(org.eclipse.swt
	 * .widgets.Composite)
	 */
	@Override
	protected PlotRender createPlotRender(Composite parent) {
		return new VisItPlotRender(parent, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.MultiPlot#findPlotTypes(java.net.URI)
	 */
	@Override
	protected Map<String, String[]> findPlotTypes(URI file) throws IOException,
			Exception {

		// Set the default return value.
		Map<String, String[]> plotTypes = new TreeMap<String, String[]>();

		// Get the connection adapter.
		IConnectionAdapter<VisItSwtConnection> adapter = getConnectionAdapter();

		// Determine the source path string. Unfortunately, we can't just give
		// the URI directly to the VisIt client API.
		String sourcePath = VisItPlot.getSourcePath(file);

		// Determine the VisIt FileInfo for the data source.
		ViewerMethods methods = adapter.getConnection().getViewerMethods();
		methods.openDatabase(sourcePath);
		FileInfo info = methods.getDatabaseInfo();

		// Get all of the plot types and plots in the file.
		List<String> plots;
		plots = info.getMeshes();
		plotTypes.put("Mesh", plots.toArray(new String[plots.size()]));
		plots = info.getMaterials();
		plotTypes.put("Material", plots.toArray(new String[plots.size()]));
		plots = info.getScalars();
		plotTypes.put("Scalar", plots.toArray(new String[plots.size()]));
		plots = info.getVectors();
		plotTypes.put("Vector", plots.toArray(new String[plots.size()]));

		return plotTypes;
	}

	/**
	 * Gets the connection adapter for the associated connection cast as a
	 * {@link VisItConnectionAdapter}.
	 * 
	 * @return The associated connection adapter.
	 */
	protected VisItConnectionAdapter getVisItConnectionAdapter() {
		return (VisItConnectionAdapter) getConnectionAdapter();
	}

	/**
	 * Gets the path to the specified file.
	 * 
	 * @param source
	 *            The source URI.
	 * @return A VisIt-friendly file path.
	 */
	protected static String getSourcePath(URI source) {
		String path = null;
		if (source != null) {
			// On Windows, the File class inserts standard forward slashes as
			// separators. VisIt, on the other hand, requires the native
			// separator. If the URI uses the standard separator on Windows,
			// update the source path to use the native Windows separator.
			path = source.getPath();

			// If the host is local and a Windows-based machine, we need to
			// update the path to use Windows separators for VisIt.
			String host = source.getHost();
			// TODO VisIt should just be able to handle a raw URI... The code
			// below can't handle remote Windows machines.
			if ((host == null || "localhost".equals(host))
					&& System.getProperty("os.name").toLowerCase()
							.contains("windows")) {
				if (path.startsWith("/")) {
					path = path.substring(1);
					path = path.replace("/",
							System.getProperty("file.separator"));
				}
			}
		}
		return path;
	}

}
