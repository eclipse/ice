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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.ice.viz.service.PlotRender;
import org.eclipse.ice.viz.service.connections.ConnectionPlot;
import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactory;
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
public class ParaViewPlot extends ConnectionPlot<VtkWebClient> {

	/**
	 * A reference to the viz service conveniently cast to its actual type.
	 */
	private final ParaViewVizService vizService;

	/**
	 * The proxy associated with the current URI. It handles messages concerning
	 * the file going to and from the remote ParaView server.
	 * <p>
	 * This should be re-created whenever the data source URI is set. If it
	 * cannot be created, then the URI cannot be rendered via ParaView.
	 * </p>
	 */
	private IParaViewProxy proxy;

	/**
	 * The default constructor.
	 * 
	 * @param service
	 *            The visualization service responsible for this plot.
	 */
	public ParaViewPlot(ParaViewVizService vizService) {
		super(vizService);

		this.vizService = vizService;
	}

	/*
	 * Overrides a method from ConnectionPlot.
	 */
	@Override
	public void setDataSource(URI uri) throws NullPointerException,
			IOException, IllegalArgumentException, Exception {

		// Get an IParaViewProxy for the file. Throw an exception if a factory
		// could not be found.
		IParaViewProxyFactory factory;
		factory = vizService.getProxyFactoryRegistry().getProxyFactory(uri);
		if (factory == null) {
			throw new IllegalStateException("ParaViewPlot error: "
					+ "Could not find a proxy factory for the file \""
					+ uri.getPath() + "\".");
		}

		// Attempt to create the IParaViewProxy. This will throw an exception if
		// the URI is null or its extension is invalid.
		proxy = factory.createProxy(uri);
		// Attempt to open the file.
		proxy.open(getParaViewConnectionAdapter());

		super.setDataSource(uri);
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
		// FIXME This method will need to be updated.
		return new ParaViewPlotRender(parent, this, -1, -1, -1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.viz.service.MultiPlot#getPlotTypes(java.net.URI)
	 */
	@Override
	protected Map<String, String[]> findPlotTypes(URI uri) throws IOException,
			Exception {
		// Throw an exception in case the proxy was not created.
		if (proxy == null) {
			throw new IllegalStateException("ParaViewPlot error: "
					+ "A proxy was not created before finding the plot types.");
		}

		// Set up the default return value.
		Map<String, String[]> plotTypes = new HashMap<String, String[]>();

		// Add all categories and features to the map.
		for (String category : proxy.getFeatureCategories()) {
			Set<String> features = proxy.getFeatures(category);
			String[] types = new String[features.size()];
			plotTypes.put(category, features.toArray(types));
		}

		// ParaViewConnectionAdapter adapter = getParaViewConnectionAdapter();
		// VtkWebClient client = adapter.getConnection();
		//
		// JsonArray args;
		// JsonObject object;
		//
		// // Open the file. We *have* to create a new view to open the file.
		// Use
		// // the custom server method, as the default ParaViewWeb method uses
		// the
		// // currently active view.
		// args = new JsonArray();
		// args.add(new
		// JsonPrimitive(adapter.findRelativePath(file.getPath())));
		// object = client.call("createView", args).get();
		// viewId = object.get("viewId").getAsInt();
		// fileId = object.get("proxyId").getAsInt();
		// repId = object.get("repId").getAsInt();
		//
		// // Read the contents of the file to populate the map of plot types.
		// args = new JsonArray();
		// args.add(new JsonPrimitive(fileId));
		// object = client.call("pv.proxy.manager.get", args).get();
		// // Get the "ui" JSON array from the proxy's properties. This contains
		// // the names of all data sets that can be displayed in the plot.
		// JsonArray array = object.get("ui").getAsJsonArray();
		//
		// System.out.println("=============================");
		// System.out.println("View ID: " + viewId);
		// object = getProxyObject(viewId);
		// printProxyObject(object);
		// System.out.println("=============================");
		//
		// System.out.println("=============================");
		// System.out.println("File ID: " + fileId);
		// object = getProxyObject(fileId);
		// printProxyObject(object);
		// System.out.println("=============================");
		//
		// System.out.println("=============================");
		// System.out.println("Representation ID: " + repId);
		// object = getProxyObject(repId);
		// printProxyObject(object);
		// System.out.println("=============================");
		//
		// // Determine all plot categories and their types.
		// for (int i = 0; i < array.size(); i++) {
		// object = array.get(i).getAsJsonObject();
		//
		// // Determine the plot category and its allowed types.
		// String name = object.get("name").getAsString();
		// // TODO Figure out how we should handle the meshes in the file.
		// // We do not want to set the mesh yet.
		// if (!"Mesh".equals(name)) {
		// JsonArray valueArray = object.get("values").getAsJsonArray();
		// String[] values = new String[valueArray.size()];
		// for (int j = 0; j < values.length; j++) {
		// values[j] = valueArray.get(j).getAsString();
		// }
		// // Store the plot category and types in the map.
		// plotTypes.put(name, values);
		// }
		// }

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
