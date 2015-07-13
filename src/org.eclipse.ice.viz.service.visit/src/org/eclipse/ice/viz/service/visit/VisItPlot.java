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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.ice.viz.service.connections.ConnectionPlotRender;
import org.eclipse.ice.viz.service.connections.IVizConnection;
import org.eclipse.ice.viz.service.visit.connections.VisItConnection;
import org.eclipse.swt.widgets.Composite;

import gov.lbnl.visit.swt.VisItSwtConnection;
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
	 * A reference to the viz service conveniently cast to its actual type.
	 */
	private final VisItVizService vizService;

	/**
	 * A map of allowed representations, keyed on the category. Instead of
	 * passing the category and type to the VisIt widget, a representation and
	 * type are passed. The first value in each list is considered the default.
	 */
	private final Map<String, List<String>> representations;

	/**
	 * The default constructor.
	 * 
	 * @param vizService
	 *            The visualization service responsible for this plot.
	 */
	public VisItPlot(VisItVizService vizService) {
		super(vizService);

		this.vizService = vizService;

		// Create the map of VisIt plot representations. The keys are the
		// categories exposed by VisItPlot in findPlotTypes(...). The first
		// value in each list is considered the default.
		representations = new HashMap<String, List<String>>();
		List<String> types;
		// The category "Materials" has two types: Boundary and FilledBoundary.
		types = new ArrayList<String>(2);
		types.add("Boundary");
		types.add("FilledBoundary");
		representations.put("Materials", types);
		// "Meshes" has one type: Mesh.
		types = new ArrayList<String>(1);
		types.add("Mesh");
		representations.put("Meshes", types);
		// "Scalars" has three types: Pseudocolor, Contour, Volume.
		types = new ArrayList<String>(3);
		types.add("Pseudocolor");
		types.add("Contour");
		types.add("Volume");
		representations.put("Scalars", types);
		// "Vectors" has one type: Vector.
		types = new ArrayList<String>(1);
		types.add("Vector");
		representations.put("Vectors", types);

		return;
	}

	/*
	 * Implements an abstract method from ConnectionPlot.
	 */
	@Override
	protected ConnectionPlotRender<VisItSwtConnection> createConnectionPlotRender(Composite parent) {
		return new VisItPlotRender(parent, this);
	}

	/*
	 * Implements an abstract method from MultiPlot.
	 */
	@Override
	protected Map<String, String[]> findPlotTypes(URI uri) throws IOException, Exception {

		// Set the default return value.
		Map<String, String[]> plotTypes = new TreeMap<String, String[]>();

		// Get the connection adapter.
		IVizConnection<VisItSwtConnection> connection = getConnection();

		// Determine the source path string. Unfortunately, we can't just give
		// the URI directly to the VisIt client API.
		String sourcePath = VisItPlot.getSourcePath(uri);

		// Determine the VisIt FileInfo for the data source.
		ViewerMethods methods = connection.getWidget().getViewerMethods();
		methods.openDatabase(sourcePath);
		FileInfo info = methods.getDatabaseInfo();

		// Get all of the plot types and plots in the file.
		List<String> plots;
		plots = info.getMeshes();
		plotTypes.put("Meshes", plots.toArray(new String[plots.size()]));
		plots = info.getMaterials();
		plotTypes.put("Materials", plots.toArray(new String[plots.size()]));
		plots = info.getScalars();
		plotTypes.put("Scalars", plots.toArray(new String[plots.size()]));
		plots = info.getVectors();
		plotTypes.put("Vectors", plots.toArray(new String[plots.size()]));

		return plotTypes;
	}

	/**
	 * Gets the connection adapter for the associated connection cast as a
	 * {@link VisItConnection}.
	 * 
	 * @return The associated connection.
	 */
	protected VisItConnection getVisItConnection() {
		return (VisItConnection) getConnection();
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
					&& System.getProperty("os.name").toLowerCase().contains("windows")) {
				if (path.startsWith("/")) {
					path = path.substring(1);
					path = path.replace("/", System.getProperty("file.separator"));
				}
			}
		}
		return path;
	}

	/**
	 * Gets the list of allowed plot representations for a category. This is
	 * required for a VisIt canvas and should be passed to it along with the
	 * plot type.
	 * 
	 * @param category
	 *            An {@code IPlot} category.
	 * @return A list of allowed representations for the specified category. The
	 *         first value can be considered the default. If the category is
	 *         invalid, an empty list is returned.
	 */
	protected List<String> getRepresentations(String category) {
		// Always return a copy so the list of representations for a category is
		// not (un)intentionally modified.
		List<String> types = representations.get(category);
		return (types != null ? new ArrayList<String>(types) : new ArrayList<String>());
	}

}
