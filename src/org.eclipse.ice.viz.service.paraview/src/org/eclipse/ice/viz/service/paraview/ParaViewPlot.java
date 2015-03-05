/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.viz.service.MultiPlot;
import org.eclipse.ice.viz.service.PlotComposite;
import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

import com.kitware.vtk.web.VtkWebClient;

/**
 * This class is responsible for embedding ParaView-supported graphics inside
 * client {@link Composite}s.
 * <p>
 * Instances of this class should not be created manually. Instead, a plot
 * should be created via {@link ParaViewVizService#createPlot(URI)}.
 * </p>
 *
 * @see ParaViewVizService
 * 
 * @author Jordan Deyton
 *
 */
public class ParaViewPlot extends MultiPlot {

	/**
	 * The default constructor.
	 * 
	 * @param service
	 *            The visualization service responsible for this plot.
	 * @param file
	 *            The data source, either a local or remote file.
	 */
	public ParaViewPlot(ParaViewVizService vizService, URI file) {
		super(vizService, file);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getPlotTypes()
	 */
	@Override
	public Map<String, String[]> getPlotTypes() throws Exception {
		Map<String, String[]> plotTypes = new HashMap<String, String[]>();
		plotTypes.put("", new String[] { "" });
		return plotTypes;
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
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		// TODO Auto-generated method stub
		return null;
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * org.eclipse.ice.viz.service.connections.ConnectionPlot#getPreferenceNodeID
//	 * ()
//	 */
//	@Override
//	protected String getPreferenceNodeID() {
//		return "org.eclipse.ice.viz.service.paraview.preferences";
//	}

}
