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
package org.eclipse.ice.viz.service.visit;

import gov.lbnl.visit.swt.VisItSwtConnection;
import gov.lbnl.visit.swt.VisItSwtWidget;

import java.net.ConnectException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Composite;

import visit.java.client.FileInfo;
import visit.java.client.ViewerMethods;

/**
 * This class provides the VisIt implementation for an IPlot.
 * 
 * @author Jay Jay Billings
 * 
 */
public class VisItPlot implements IPlot {

	/**
	 * The map of preferences. Currently, this stores connection preferences.
	 */
	private final Map<String, String> preferences = new HashMap<String, String>();

	/**
	 * The data source, either a local or remote file.
	 */
	private final URI source;
	/**
	 * The source path required by the VisIt widgets.
	 */
	private final String sourcePath;

	/**
	 * The VisIt connection (either local or remote) that powers any VisIt
	 * widgets.
	 */
	private final VisItSwtConnection connection;

	/**
	 * The current VisIt widget used to draw VisIt plots.
	 */
	private VisItSwtWidget canvas = null;

	/**
	 * The default constructor.
	 * 
	 * @param source
	 *            The data source, either a local or remote file.
	 * @param connection
	 *            The VisIt connection (either local or remote) that powers any
	 *            VisIt widgets.
	 */
	public VisItPlot(URI source, VisItSwtConnection connection) {
		this.source = source;
		this.connection = connection;

		// On Windows, the File class inserts standard forward slashes as
		// separators. VisIt, on the other hand, requires the native separator.
		// If the URI uses the standard separator on Windows, update the source
		// path to use the native Windows separator.
		String path = source.getPath();
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			if (path.startsWith("/")) {
				path = path.substring(1);
				path = path.replace("/", System.getProperty("file.separator"));
			}
		}
		sourcePath = path;

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getPlotTypes()
	 */
	@Override
	public Map<String, String[]> getPlotTypes() throws Exception {

		// Determine the VisIt FileInfo for the data source.
		ViewerMethods methods = connection.getViewerMethods();
		methods.openDatabase(sourcePath);
		FileInfo info = methods.getDatabaseInfo();

		// Get all of the plot types and plots in the file.
		List<String> plots;
		Map<String, String[]> plotTypes = new TreeMap<String, String[]>();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// TODO Should we query the plot somehow?
		return (canvas == null ? 0 : 3);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return preferences;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#setProperties(java.util
	 * .Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		if (props != null) {
			preferences.putAll(props);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		return preferences.get(ConnectionPreference.Host.getID());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return "localhost".equals(getSourceHost());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.client.widgets.viz.service.IPlot#draw(java.lang.String,
	 * java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void draw(String category, String plotType, Composite parent)
			throws Exception {

		// Check the parameters.
		if (category == null || plotType == null || parent == null) {
			throw new NullPointerException("VisItPlot error: "
					+ "Null arguments are not allowed when drawing plot.");
		} else if (parent.isDisposed()) {
			throw new SWTException(SWT.ERROR_WIDGET_DISPOSED,
					"VisItPlot error: "
							+ "Cannot draw plot inside disposed Composite.");
		}

		// Create the VisIt Canvas if necessary.
		if (canvas == null) {
			canvas = createCanvas(parent, SWT.BORDER | SWT.DOUBLE_BUFFERED);
			// Create a mouse manager to handle mouse events inside the canvas.
			new VisItMouseManager(canvas);
		}
		// Make sure the Canvas is activated.
		canvas.activate();

		// Draw the specified plot on the Canvas.
		ViewerMethods widget = canvas.getViewerMethods();
		widget.deleteActivePlots();
		widget.addPlot(category, plotType);
		widget.drawPlots();

		return;
	}

	/**
	 * Creates a new VisIt {@code Canvas} that can render components via VisIt.
	 * 
	 * @param parent
	 *            The parent {@code Composite} that will contain the canvas.
	 * @param style
	 *            The SWT style to use for the VisIt {@code Canvas}.
	 * @return The new VisIt {@code Canvas}
	 */
	private VisItSwtWidget createCanvas(Composite parent, int style) {

		// Create the canvas.
		VisItSwtWidget canvas = new VisItSwtWidget(parent, style);

		// Set the VisIt connection info. It requres a valid VisItSwtConnection
		// and 3 integers (a window ID, width, and height).
		int windowId = Integer.parseInt(preferences
				.get(ConnectionPreference.WindowID.toString()));
		int windowWidth = Integer.parseInt(preferences
				.get(ConnectionPreference.WindowWidth.toString()));
		int windowHeight = Integer.parseInt(preferences
				.get(ConnectionPreference.WindowHeight.toString()));
		try {
			canvas.setVisItSwtConnection(connection, windowId, windowWidth,
					windowHeight);
		} catch (ConnectException e) {
			System.out.println("VisItPlot error: "
					+ "Could not set connection for VisIt Canvas.");
			e.printStackTrace();
		}

		return canvas;
	}

}
