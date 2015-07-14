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
package org.eclipse.ice.viz.service.paraview.proxy.silo;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.viz.service.paraview.connections.ParaViewConnection;
import org.eclipse.ice.viz.service.paraview.proxy.AbstractParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxy;
import org.eclipse.ice.viz.service.paraview.proxy.IParaViewProxyFactory;
import org.eclipse.ice.viz.service.paraview.proxy.IProxyProperty;
import org.eclipse.ice.viz.service.paraview.proxy.ProxyFeature.ColorByLocation;
import org.eclipse.ice.viz.service.paraview.proxy.ProxyFeature.ColorByMode;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * This class provides a concrete {@link IParaViewProxy} that supports loading
 * and rendering Silo files.
 * <p>
 * <b>Note:</b> In practice, instances of this class should not be instantiated.
 * Rather, they should be obtained from the {@link IParaViewProxyFactory}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class SiloProxy extends AbstractParaViewProxy {

	/**
	 * A map of {@link SiloFeature}s, keyed on their "UI" name from the file
	 * proxy's "ui" properties.
	 */
	private final Map<String, SiloProxyFeature> features;

	/**
	 * The default constructor. The associated Silo file's URI <i>must</i> be
	 * specified.
	 * 
	 * @param uri
	 *            The URI for the Silo file.
	 * @throws NullPointerException
	 *             If the specified URI is null.
	 */
	protected SiloProxy(URI uri) throws NullPointerException {
		super(uri);

		// Add all supported Silo features.
		features = new HashMap<String, SiloProxyFeature>();
		features.put("Meshes", new SiloProxyFeature(0, "Meshes", "MeshStatus"));
		features.put("Materials",
				new SiloProxyFeature(1, "Materials", "MaterialStatus"));
		features.put("Cell Arrays", new SiloProxyFeature(2, "Cell Arrays",
				"CellArrayStatus", ColorByLocation.CELLS, ColorByMode.ARRAY));
		features.put("Point Arrays", new SiloProxyFeature(3, "Point Arrays",
				"PointArrayStatus", ColorByLocation.POINTS, ColorByMode.ARRAY));

		return;
	}

	/**
	 * Constructs a JsonObject representing the feature and its selected values.
	 * This can be added to a JsonArray of properties to update and then sent to
	 * the web client to update the file proxy's "properties".
	 * 
	 * @param feature
	 *            The feature in question.
	 * @return A JsonObject representing the expected new values for the
	 *         property.
	 */
	private JsonObject createPropertyObject(SiloProxyFeature feature) {
		// Create an object for the file proxy and the feature.
		JsonObject object = new JsonObject();
		object.addProperty("id", Integer.toString(getFileId()));
		object.addProperty("name", feature.propertyName);
		// Populate the new value (array) for the property based on the
		// feature's selected values.
		JsonArray array = new JsonArray();
		for (String selectedValue : feature.selectedValues) {
			array.add(new JsonPrimitive(selectedValue));
		}
		// Add the new value (array) to the property change object.
		object.add("value", array);
		return object;
	}

	/**
	 * Finds all allowed values for the feature from the file's proxy UI
	 * properties.
	 * 
	 * @param feature
	 *            The feature in question.
	 * @param uiArray
	 *            The "ui" JsonArray from the file proxy.
	 * @return A set containing all allowed values for the feature.
	 */
	private Set<String> findFeatureAllowedValues(SiloProxyFeature feature,
			JsonArray uiArray) {
		// Get the i-th element from the "ui" array (a JsonObject).
		JsonObject object = uiArray.get(feature.index).getAsJsonObject();
		// Add all strings from the "values" array to a new, ordered set.
		JsonArray array = object.get("values").getAsJsonArray();
		Set<String> allowedValues = new TreeSet<String>();
		for (int i = 0; i < array.size(); i++) {
			allowedValues.add(array.get(i).getAsString());
		}
		return allowedValues;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected Map<String, Set<String>> findFeatures(
			ParaViewConnection connection) {

		// Initialize the map of categories and features. This map will be
		// returned.
		Map<String, Set<String>> featureMap = new HashMap<String, Set<String>>();

		JsonObject object;
		JsonArray array;
		Set<String> values;

		// Get the widget used to query ParaView.
		IParaViewWebClient widget = connection.getWidget();

		try {
			// Query the client for the file proxy's information.
			array = new JsonArray();
			array.add(new JsonPrimitive(Integer.toString(getFileId())));
			object = widget.call("pv.proxy.manager.get", array).get();

			// Get the "ui" features. They are expected to be in this order in
			// the file proxy's "ui" array.
			JsonArray uiArray = object.get("ui").getAsJsonArray();
			JsonArray propertiesArray = object.get("properties")
					.getAsJsonArray();
			for (SiloProxyFeature feature : features.values()) {
				values = findFeatureAllowedValues(feature, uiArray);
				featureMap.put(feature.name, values);
				// Get the currently selected values.
				findFeatureSelectedValues(feature, propertiesArray);
			}
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("SiloProxy error: "
					+ "Connection error while getting file proxy information.");
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			System.err.println("SiloProxy error: "
					+ "Error while reading file proxy information.");
		}

		return featureMap;
	}

	/**
	 * Finds and sets all currently selected values according to the file's
	 * proxy properties.
	 * 
	 * @param feature
	 *            The feature in question.
	 * @param propArray
	 *            The "properties" JsonArray from the file proxy.
	 */
	private void findFeatureSelectedValues(SiloProxyFeature feature,
			JsonArray propArray) {
		JsonObject object = propArray.get(feature.index).getAsJsonObject();
		JsonArray array = object.get("value").getAsJsonArray();
		for (int i = 0; i < array.size(); i++) {
			feature.selectedValues.add(array.get(i).getAsString());
		}
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected String findInitialCategory() {
		String currentCategory = null;
		for (SiloProxyFeature feature : features.values()) {
			if (!feature.selectedValues.isEmpty()) {
				currentCategory = feature.name;
				break;
			}
		}
		return currentCategory;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected String findInitialFeature() {
		String currentFeature = null;
		for (SiloProxyFeature feature : features.values()) {
			if (!feature.selectedValues.isEmpty()) {
				currentFeature = feature.selectedValues.iterator().next();
				break;
			}
		}
		return currentFeature;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected List<IProxyProperty> findProperties(
			ParaViewConnection connection) {
		List<IProxyProperty> properties = super.findProperties(connection);
		// TODO Add exodus-specific properties.
		return properties;
	}

	/*
	 * Overrides a method from AbstractParaViewProxy.
	 */
	@Override
	protected boolean setFeatureOnClient(ParaViewConnection connection,
			String category, String feature) {

		// Whether or not the feature was successfully changed.
		boolean updated = true;

		// Get the previous feature.
		String oldCategory = getCategory();
		String oldFeature = getFeature();
		// Get the SiloFeature for the new feature.
		SiloProxyFeature featureInfo = features.get(category);
		// Create a JsonArray for all changed or updated properties. These will
		// be sent to the web client in bulk.
		JsonArray changedProperties = new JsonArray();

		// If the categories are the same, remove the old feature.
		if (category.equals(oldCategory)) {
			featureInfo.selectedValues.remove(oldFeature);
		}
		// Otherwise, only remove the old feature if the previous feature was
		// not a mesh.
		else if (!"Meshes".equals(oldCategory)) {
			SiloProxyFeature oldFeatureInfo = features.get(oldCategory);
			oldFeatureInfo.selectedValues.remove(oldFeature);
			// Since the categories are different, we have to update another
			// property in the file proxy.
			changedProperties.add(createPropertyObject(oldFeatureInfo));
		}
		// Add the new feature and get the property change.
		featureInfo.selectedValues.add(feature);
		changedProperties.add(createPropertyObject(featureInfo));

		// Try to change the properties. If successful, update the "color by"
		// property.
		if (applyProperties(connection, changedProperties)) {
			setColorBy(connection, featureInfo, feature);
		}
		// Otherwise, revert the changes to the category and feature.
		else {
			features.get(oldCategory).selectedValues.add(oldFeature);
			featureInfo.selectedValues.remove(feature);
			updated = false;
		}

		return updated;
	}

}
