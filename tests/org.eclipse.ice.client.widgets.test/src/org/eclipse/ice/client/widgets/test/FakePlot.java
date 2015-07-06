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
package org.eclipse.ice.client.widgets.test;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.client.widgets.ICEResourcePage;
import org.eclipse.ice.client.widgets.PlotGridComposite;
import org.eclipse.ice.viz.service.IPlot;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * A simple {@link IPlot} implementation for testing things that draw
 * {@code IPlot}s, including the {@link PlotGridComposite} and the
 * {@link ICEResourcePage}.
 * 
 * @author Jordan Deyton
 *
 */
public class FakePlot implements IPlot {

	/**
	 * The map of plot types. This will not be populated with anything by
	 * default.
	 */
	public final Map<String, String[]> plotTypes = new HashMap<String, String[]>();

	/**
	 * A list of all child composites created when
	 * {@link #draw(String, String, Composite)} is called.
	 */
	public final List<Composite> children = new ArrayList<Composite>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#getPlotTypes()
	 */
	@Override
	public Map<String, String[]> getPlotTypes() throws Exception {
		return plotTypes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#draw(java.lang.String,
	 * java.lang.String, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public Composite draw(String category, String plotType, Composite parent)
			throws Exception {
		Composite child = new Composite(parent, SWT.NONE);
		children.add(child);
		child.setMenu(parent.getMenu());
		return child;
	}

	/**
	 * Gets the number of times that {@link #draw(String, String, Composite)}
	 * was called.
	 */
	public int getDrawCount() {
		return children.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#getNumberOfAxes()
	 */
	@Override
	public int getNumberOfAxes() {
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#setProperties(java.util.Map)
	 */
	@Override
	public void setProperties(Map<String, String> props) throws Exception {
		// Do nothing.
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#getDataSource()
	 */
	@Override
	public URI getDataSource() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.IPlot#isSourceRemote()
	 */
	@Override
	public boolean isSourceRemote() {
		return false;
	}

	@Override
	public void redraw() {
		// TODO Auto-generated method stub
		
	}
}
