/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan H. Deyton (UT-Battelle, LLC.) - Initial API and implementation 
 *   and/or initial documentation
 *   
 *******************************************************************************/
package org.eclipse.ice.viz.service.paraview.proxy.exodus;

import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxy;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.kitware.vtk.web.VtkWebClient;

/**
 * 
 * @author Jordan Deyton
 *
 */
public class ExodusProxy extends AbstractParaViewProxy {

	protected ExodusProxy(URI uri) throws NullPointerException {
		super(uri);
		// TODO Auto-generated constructor stub
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxy#findFeatures
	 * (com.kitware.vtk.web.VtkWebClient)
	 */
	@Override
	protected Map<String, Set<String>> findFeatures(VtkWebClient client) {
		Map<String, Set<String>> featureMap = new HashMap<String, Set<String>>();

		// The structure of Exodus files looks like so:

		// "data" > "arrays" appears to have a list of array names, each of
		// which has a "location" key set to one of { POINTS, CELLS, FIELDS }.

		// "ui" has a lot of elements... important ones listed here.
		// "Element Variables", a list of variables that can be loaded
		// "Face Variables", a list of variables that can be loaded
		// "Edge Variables", a list of variables that can be loaded
		// "Point Variables", a list of variables that can be loaded
		// "Global Variables", a list of variables that can be loaded

		// TODO By default, it appears that all of the variables are loaded. It
		// would probably be better to only load them when selected.

		// Loop over the "data" > "arrays" JsonArray and get all point, cell,
		// and field variables.

		JsonObject object;
		JsonArray array;

		try {
			// Query the client for the file proxy's information.
			array = new JsonArray();
			array.add(new JsonPrimitive(Integer.toString(getFileId())));
			object = client.call("pv.proxy.manager.get", array).get();
			// Get the "data" object's "arrays" array.
			object = object.get("data").getAsJsonObject();
			array = object.get("arrays").getAsJsonArray();
			// Loop over every element in the array and put it in the
			// appropriate category.
			for (int i = 0; i < array.size(); i++) {
				object = array.get(i).getAsJsonObject();
				// Get the category and feature name.
				String category = object.get("location").getAsString();
				String name = object.get("name").getAsString();
				Set<String> features = featureMap.get(category);
				if (features == null) {
					features = new HashSet<String>();
					featureMap.put(category, features);
				}
				features.add(name);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.err.println("ExodusProxy error: "
					+ "Connection error while getting file proxy information.");
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			System.err.println("ExodusProxy error: "
					+ "Error while reading file proxy information.");
		}

		return featureMap;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected Map<String, Set<String>> findProperties(VtkWebClient client) {
		Map<String, Set<String>> propertyMap = new HashMap<String, Set<String>>();
		// TODO
		return propertyMap;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected boolean setFeatureImpl(VtkWebClient client, String category,
			String feature) {

		boolean updated = false;

		// We can't draw FIELDS...
		if (category.equals("POINTS") || category.equals("CELLS")) {

			// Set the "color by" to color based on the feature name.
			JsonArray args = new JsonArray();

			// Add the requisite arguments to the argument array.
			args.add(new JsonPrimitive(Integer.toString(getRepresentationId())));
			args.add(new JsonPrimitive("ARRAY"));
			args.add(new JsonPrimitive(category));
			args.add(new JsonPrimitive(feature));
			args.add(new JsonPrimitive("Magnitude"));
			args.add(new JsonPrimitive(0));
			args.add(new JsonPrimitive(true));

			// Call the client.
			try {
				// The only way to tell if the client even received the message
				// is
				// if we get back an empty JsonObject. If null, then there was
				// an
				// error.
				if (client.call("pv.color.manager.color.by", args).get() != null) {
					updated = true;
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

		}

		return updated;

		// return super.setFeatureImpl(client, category, feature);
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected boolean setPropertyImpl(VtkWebClient client, String property,
			String value) {
		// TODO
		return false;
	}
}