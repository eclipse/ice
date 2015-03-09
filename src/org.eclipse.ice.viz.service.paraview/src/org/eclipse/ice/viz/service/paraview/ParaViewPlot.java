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
import java.util.concurrent.Future;

import org.eclipse.ice.viz.service.PlotRender;
import org.eclipse.ice.viz.service.connections.ConnectionPlot;
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
		return new ParaViewPlotRender(parent, this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.MultiPlot#getPlotTypes(java.net.URI)
	 */
	@Override
	protected Map<String, String[]> getPlotTypes(URI file) throws IOException,
			Exception {
		// TODO This needs to build from the contents of the file via the JSON
		// RPC calls.

		VtkWebClient client = getConnectionAdapter().getConnection();

		// The code below lists out the contents of the base directory for
		// the http server.
		JSONObject object;

		List<Object> args = new ArrayList<Object>();

		// Determine the relative path of the file from the ParaView web
		// client's working directory.
		String relativePath = getRelativePath(file.getPath());

		System.out.println("Relative path: " + relativePath);
		if (relativePath != null) {
			int proxyId = openFile(relativePath);
			System.out.println("The file proxy id: " + proxyId);
			
			// Get the file's properties.
			args.clear();
			args.add(proxyId);
			object = client.call("pv.proxy.manager.get", args).get();
			System.out.println(object.toString(4));
		}

		return new HashMap<String, String[]>();
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
			object = client.call("pv.proxy.manager.create.reader", args).get();
			if (object.has("id")) {
				proxyId = object.getInt("id");
			}
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}

		return proxyId;
	}
}
