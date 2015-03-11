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

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ice.viz.service.PlotRender;
import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;
import org.eclipse.swt.widgets.Composite;
import org.json.JSONArray;
import org.json.JSONObject;

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
public class ParaViewPlot extends ConnectionPlot<VtkWebClient> {

	/**
	 * The ID of a view that was created in order to read the file contents.
	 */
	private int viewId = -1;
	private int fileId = -1;
	private int repId = -1;

	/**
	 * The default constructor.
	 * 
	 * @param service
	 *            The visualization service responsible for this plot.
	 */
	public ParaViewPlot(ParaViewVizService vizService) {
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
		// Reset the view IDs so that the same view is not used twice.
		int viewId = this.viewId;
		int fileId = this.fileId;
		int repId = this.repId;
		this.viewId = -1;
		this.fileId = -1;
		this.repId = -1;
		return new ParaViewPlotRender(parent, this, viewId, fileId, repId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.MultiPlot#getPlotTypes(java.net.URI)
	 */
	@Override
	protected Map<String, String[]> findPlotTypes(URI file) throws IOException,
			Exception {

		// Set up the default return value.
		Map<String, String[]> plotTypes = new HashMap<String, String[]>();

		ParaViewConnectionAdapter adapter = getParaViewConnectionAdapter();
		VtkWebClient client = adapter.getConnection();

		List<Object> args = new ArrayList<Object>();
		JSONObject object;

		// Open the file. We *have* to create a new view to open the file. Use
		// the custom server method, as the default ParaViewWeb method uses the
		// currently active view.
		args.clear();
		args.add(adapter.findRelativePath(file.getPath()));
		object = client.call("createView", args).get();
		viewId = object.getInt("viewId");
		fileId = object.getInt("proxyId");
		repId = object.getInt("repId");

		// Read the contents of the file to populate the map of plot types.
		args.clear();
		args.add(fileId);
		object = client.call("pv.proxy.manager.get", args).get();
		// Get the "ui" JSON array from the proxy's properties. This contains
		// the names of all data sets that can be displayed in the plot.
		JSONArray array = object.getJSONArray("ui");

		// Determine all plot categories and their types.
		for (int i = 0; i < array.length(); i++) {
			object = array.getJSONObject(i);

			// Determine the plot category and its allowed types.
			String name = object.getString("name");
			JSONArray valueArray = object.getJSONArray("values");
			String[] values = new String[valueArray.length()];
			for (int j = 0; j < values.length; j++) {
				values[j] = valueArray.getString(j);
			}

			// Store the plot category and types in the map.
			plotTypes.put(name, values);
		}

		return plotTypes;
	}

	/**
	 * Gets the connection adapter for the associated connection cast as a
	 * {@link ParaViewConnectionAdapter}.
	 * 
	 * @return The associated connection adapter.
	 */
	protected ParaViewConnectionAdapter getParaViewConnectionAdapter() {
		return (ParaViewConnectionAdapter) getConnectionAdapter();
	}
}
