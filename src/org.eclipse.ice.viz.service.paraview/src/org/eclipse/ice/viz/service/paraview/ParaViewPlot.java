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

import org.eclipse.ice.viz.service.MultiPlot;
import org.eclipse.ice.viz.service.PlotRender;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;

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
		return new HashMap<String, String[]>();
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
		return new ParaViewPlotRender(parent, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.MultiPlot#updatePlotRender(org.eclipse.ice
	 * .viz.service.PlotRender)
	 */
	@Override
	protected void updatePlotRender(PlotRender plotRender) {
		// TODO We will need to update it in a custom way.
		super.updatePlotRender(plotRender);
	}

	// /*
	// * (non-Javadoc)
	// *
	// * @see
	// *
	// org.eclipse.ice.viz.service.connections.ConnectionPlot#getPreferenceNodeID
	// * ()
	// */
	// @Override
	// protected String getPreferenceNodeID() {
	// return "org.eclipse.ice.viz.service.paraview.preferences";
	// }

}
