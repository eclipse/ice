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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
	@Override
	protected Map<String, String[]> findFeatures(VtkWebClient client) {
		Map<String, List<String>> featureMap = new HashMap<String, List<String>>();

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
				List<String> features = featureMap.get(category);
				if (features == null) {
					features = new ArrayList<String>();
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

		// Convert the lists to arrays.
		Map<String, String[]> foundFeatures = new HashMap<String, String[]>();
		for (Entry<String, List<String>> entry : featureMap.entrySet()) {
			String[] features = new String[entry.getValue().size()];
			entry.getValue().toArray(features);
			foundFeatures.put(entry.getKey(), features);
		}
		return foundFeatures;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected Map<String, String[]> findProperties(VtkWebClient client) {
		Map<String, String[]> propertyMap = new HashMap<String, String[]>();
		// TODO
		return propertyMap;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected boolean setFeatureImpl(VtkWebClient client, String category,
			String feature) {
		return super.setFeatureImpl(client, category, feature);
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