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

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.swt.SWT;
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

	private final Map<String, String> preferences = new HashMap<String, String>();

	private final URI source;
	
	private final VisItSwtConnection connection;
	
	public VisItPlot(URI source, VisItSwtConnection connection) {
		this.source = source;
		this.connection = connection;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getPlotTypes()
	 */
	@Override
	public Map<String, String[]> getPlotTypes() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		// TODO Auto-generated method stub
		return 0;
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
	
	@Override
	public void draw(String category, String plotType, Composite parent)
			throws Exception {

		VisItSwtWidget canvas = new VisItSwtWidget(parent, SWT.BORDER);

		int windowId = Integer.parseInt(preferences.get(ConnectionPreference.WindowID.toString()));
		int windowWidth = Integer.parseInt(preferences.get(ConnectionPreference.WindowWidth.toString()));
		int windowHeight = Integer.parseInt(preferences.get(ConnectionPreference.WindowHeight.toString()));
		canvas.setVisItSwtConnection(connection, windowId, windowWidth,
				windowHeight);
		
		canvas.activate();
		ViewerMethods widget = canvas.getViewerMethods();
		String path = source.getPath();
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			if (path.startsWith("/")) {
				path = path.substring(1);
				path = path.replace("/", System.getProperty("file.separator"));
			}
		}
		
		widget.openDatabase(path);
		FileInfo fileInfo = canvas.getFileInfo();
		widget.deleteActivePlots();
		widget.addPlot(category, plotType);
		widget.drawPlots();

		return;
	}

}
