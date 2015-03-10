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
import java.util.concurrent.ExecutionException;

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
		// Reset the view ID to -1 every time so that the same view is not used
		// twice.
		int viewId = this.viewId;
		this.viewId = -1;
		return new ParaViewPlotRender(parent, this, viewId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.MultiPlot#getPlotTypes(java.net.URI)
	 */
	@Override
	protected Map<String, String[]> getPlotTypes(URI file) throws IOException,
			Exception {

		Map<String, String[]> plotTypes = new HashMap<String, String[]>();

		// Determine the relative path of the file from the ParaView web
		// client's working directory.
		String relativePath = getRelativePath(file.getPath());

		System.out.println("Relative path: " + relativePath);
		if (relativePath != null) {
			int proxyId = openFile(relativePath);
			System.out.println("The file proxy id: " + proxyId);

			// Get the file proxy's properties from the file.
			VtkWebClient client = getConnectionAdapter().getConnection();
			JSONObject object;
			JSONArray array;
			List<Object> args = new ArrayList<Object>(1);
			args.add(proxyId);
			try {
				object = client.call("pv.proxy.manager.get", args).get();
				// Get the "ui" JSON array from the proxy's properties. This
				// contains the names of all data sets that can be displayed in
				// the plot.
				if (object.has("ui")) {
					array = object.getJSONArray("ui");

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
				}
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			}
		}

		return plotTypes;
	}

	private String getRelativePath(String fullPath) {
		String relativePath = null;

		VtkWebClient client = getConnectionAdapter().getConnection();
		List<Object> args = new ArrayList<Object>();
		JSONObject object;

		args.add(".");
		try {
			object = client.call("file.server.directory.list", args).get();
			if (object != null) {
				String directory = object.getJSONArray("path").getString(0);
				System.out.println("The directory is: " + directory);

				// If the path is indeed a full path, we need to determine its
				// relative path.
				if (fullPath.startsWith("/")) {
					// Determine the path to the base directory.
					relativePath = "";
					String[] split = directory.split("/");
					for (int i = 0; i < split.length; i++) {
						if (!split[i].trim().isEmpty()) {
							relativePath += "../";
						}
					}
					// Add in the rest of the full path, excluding the initial
					// forward slash.
					if (fullPath.length() > 1) {
						relativePath += fullPath.substring(1);
					}
				} else {
					relativePath = fullPath;
				}
			}
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}

		return relativePath;
	}

	private int openFile(String relativePath) {
		int proxyId = -1;

		VtkWebClient client = getConnectionAdapter().getConnection();
		List<Object> args = new ArrayList<Object>();
		JSONObject object;

		args.add(relativePath);
		try {
			// Open a view in order to read the file.
			viewId = client.createView().get();

			object = client.call("pv.proxy.manager.create.reader", args).get();
			if (object.has("id")) {
				proxyId = object.getInt("id");
			}
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}

		return proxyId;
	}

	protected ParaViewConnectionAdapter getParaViewConnectionAdapter() {
		return (ParaViewConnectionAdapter) getConnectionAdapter();
	}
}
