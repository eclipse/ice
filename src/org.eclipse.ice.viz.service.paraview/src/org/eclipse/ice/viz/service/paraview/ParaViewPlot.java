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
import java.util.Map;

import org.eclipse.ice.client.widgets.viz.service.IPlot;
import org.eclipse.ice.viz.service.connections.ConnectionPlot;
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
public class ParaViewPlot extends ConnectionPlot<VtkWebClient> implements IPlot {

	// ---- Service and Connection ---- //
	// -------------------------------- //

	// ---- Source and Plot Properties ---- //
	// /**
	// * The source path required by the VisIt widgets.
	// */
	// private String sourcePath;
	// ------------------------------------ //

	// ---- UI Widgets ---- //
	// /**
	// * The current VisIt widget used to draw VisIt plots. This should only be
	// * visible if the connection is open.
	// */
	// private VisItSwtWidget canvas = null;
	// -------------------- //

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
	 * @see org.eclipse.ice.client.widgets.viz.service.IPlot#getSourceHost()
	 */
	@Override
	public String getSourceHost() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.ConnectionPlot#getPreferenceNodeID
	 * ()
	 */
	@Override
	protected String getPreferenceNodeID() {
		return "org.eclipse.ice.viz.service.paraview.preferences";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.ConnectionPlot#createCanvas(org
	 * .eclipse.swt.widgets.Composite, int, java.lang.Object)
	 */
	@Override
	protected Composite createCanvas(Composite parent, int style,
			VtkWebClient connection) {
		// TODO Auto-generated method stub
		return new Composite(parent, style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.connections.ConnectionPlot#updateCanvas(org
	 * .eclipse.swt.widgets.Composite, java.lang.Object)
	 */
	@Override
	protected void updateCanvas(Composite canvas, VtkWebClient connection)
			throws Exception {
		// TODO Auto-generated method stub

	}

}
