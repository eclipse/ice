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
package org.eclipse.ice.viz.service.paraview.proxy;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.viz.service.connections.ConnectionState;
import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.kitware.vtk.web.VtkWebClient;

/**
 * This class provides a basic implementation of {@link IParaViewProxy} and
 * should be used whenever possible when dealing with the ParaView Java client.
 * 
 * @author Jordan Deyton
 *
 */
public abstract class AbstractParaViewProxy implements IParaViewProxy {

	/**
	 * The URI for this proxy. This should only ever be set once.
	 */
	private final URI uri;

	/**
	 * The current connection used to open or manipulate the file specified by
	 * the {@link #uri}.
	 */
	private ParaViewConnectionAdapter connection = null;

	/**
	 * The ParaView ID pointing to the file's proxy on the server.
	 */
	private int fileId = -1;
	/**
	 * The ParaView ID pointing to the file's associated render view proxy on
	 * the server.
	 */
	private int viewId = -1;
	/**
	 * The ParaView ID pointing to the file's associated representation proxy on
	 * the server.
	 */
	private int repId = -1;

	/**
	 * The map of features. The keys are categories, while the values are sets
	 * of features for each category. Neither keys nor values should be
	 * {@code null}.
	 */
	private final Map<String, Set<String>> featureMap;
	/**
	 * The map of properties. The keys are property names, while the values are
	 * sets of allowed values for each property. Neither keys nor values should
	 * be {@code null}.
	 */
	private final Map<String, Set<String>> propertyMap;

	/**
	 * The currently selected category.
	 */
	private String category;
	/**
	 * The currently selected feature.
	 */
	private String feature;
	/**
	 * All current values for all properties.
	 */
	private final Map<String, String> currentProperties;

	/**
	 * The default constructor. This should only be called by sub-class
	 * constructors.
	 * 
	 * @param uri
	 *            The URI for the ParaView-supported file.
	 * @throws NullPointerException
	 *             If the specified URI is null.
	 */
	protected AbstractParaViewProxy(URI uri) throws NullPointerException {
		// Throw an exception if the argument is null.
		if (uri == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot open a null URI.");
		}

		// Set the reference to the URI.
		this.uri = uri;

		// Initialize the maps of allowed features and properties.
		featureMap = new HashMap<String, Set<String>>();
		propertyMap = new HashMap<String, Set<String>>();

		// Initialize the current feature and properties.
		category = null;
		feature = null;
		currentProperties = new HashMap<String, String>();

		return;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean open(ParaViewConnectionAdapter connection)
			throws NullPointerException {
		// Throw an exception if the argument is null.
		if (connection == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot open a proxy with a null connection.");
		}

		// Set the default return value.
		boolean opened = false;

		VtkWebClient client = connection.getConnection();

		// Only attempt to open the file if the connection is established.
		if (connection.getState() == ConnectionState.Connected) {
			// Validate that the connection server and the URI point to the same
			// server.

			// Get the connection host.
			String clientHost = connection.getHost();

			// Get the URI host.
			String fileHost = uri.getHost();
			if (fileHost == null) {
				fileHost = "localhost";
			}

			// TODO We need better validation of hostnames, local vs remote,
			// FQDN vs IP, etc.

			// If they match, attempt to open the file.
			if (clientHost != null && clientHost.equals(fileHost)) {
				opened = openImpl(client, uri.getPath());
			}
		}

		// If the connection was opened, re-build the maps of features and
		// properties.
		if (opened) {
			// Update the reference to the current connection.
			this.connection = connection;

			// Re-build the map of features.
			featureMap.clear();
			featureMap.putAll(findFeatures(client));

			// Re-build the map of properties.
			propertyMap.clear();
			propertyMap.putAll(findProperties(client));
		}

		return opened;
	}

	/**
	 * Opens the file at the specified path using the ParaView web client. When
	 * called, the full path is expected to be a path on the client's specified
	 * host.
	 * <p>
	 * This method should set {@link #fileId}, {@link #viewId}, and
	 * {@link #repId}.
	 * </p>
	 * 
	 * @param client
	 *            The connection client used to open the file.
	 * @param fullPath
	 *            The full path on the client machine to the file that will be
	 *            opened.
	 * @return True if the file at the specified path on the client machine
	 *         could be opened, false otherwise.
	 */
	protected boolean openImpl(VtkWebClient client, String fullPath) {
		boolean opened = false;

		// The argument array must contain the full path to the file.
		JsonArray args = new JsonArray();
		args.add(new JsonPrimitive(fullPath));

		// Attempt to create a view on the ParaView server.
		try {
			JsonObject response = client.call("createView", args).get();

			// If the response does not contain an "error" value, then try to
			// determine the file, view, and representation IDs.
			if (response.get("error") == null) {
				// The file was likely opened successfully.
				opened = true;
				// Try to get the IDs from the response.
				try {
					int fileId = response.get("proxyId").getAsInt();
					int viewId = response.get("viewId").getAsInt();
					int repId = response.get("repId").getAsInt();

					// Update the IDs if everything worked.
					this.fileId = fileId;
					this.viewId = viewId;
					this.repId = repId;
				} catch (NullPointerException | ClassCastException
						| IllegalStateException e) {
					System.err.println("ParaViewProxy error: "
							+ "Could not retrieve file, view, and/or "
							+ "representation ID from connection.");
					opened = false;
				}
			}
			// Otherwise, if the response contains an "error" value, print out
			// the reason for the error.
			else {
				// Try to determine the reason for the failure.
				String reason;
				try {
					reason = response.get("error").getAsString();
				} catch (NullPointerException | ClassCastException
						| IllegalStateException e) {
					reason = "Unknown due to malformed response.";
				}
				// Print out the reason for the failure.
				System.err.println("ParaViewProxy error: "
						+ "Failed to open \"" + fullPath + "\". Reason: "
						+ reason);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.err.println("ParaViewProxy error: " + "Failed to open \""
					+ fullPath + "\" due to connection error.");
		}

		return opened;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public URI getURI() {
		return uri;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getFeatureCategories() {
		// Return an ordered copy of the set of feature categories.
		return new TreeSet<String>(featureMap.keySet());
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getFeatures(String category)
			throws NullPointerException, IllegalArgumentException {
		// Check for a null argument.
		if (category == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot find features for a null category.");
		}

		// Try to get the features for the category from the map.
		Set<String> featureSet = featureMap.get(category);
		if (featureSet == null) {
			throw new IllegalArgumentException("ParaViewProxy error: "
					+ "Cannot find features for category \"" + category + "\".");
		}

		// Return an ordered copy of the set of allowed features.
		return new TreeSet<String>(featureSet);
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean setFeature(String category, String feature)
			throws NullPointerException, IllegalArgumentException {
		// Check for null arguments.
		if (category == null || feature == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Null categories and features are not supported.");
		}

		boolean changed = false;

		// Only proceed if the feature and/or category changed.
		if (!category.equals(this.category) || !feature.equals(this.feature)) {
			// Get the set of features for the category.
			Set<String> featureSet = featureMap.get(category);
			if (featureSet != null) {
				// Make sure the feature is valid for the category before
				// updating the client.
				if (featureSet.contains(feature)) {
					// Only attempt to update the feature and category if the
					// client is connected and can be successfully updated.
					if (connection.getState() == ConnectionState.Connected
							&& setFeatureImpl(connection.getConnection(),
									category, feature)) {
						this.category = category;
						this.feature = feature;
						changed = true;
					}
				} else {
					throw new IllegalArgumentException("ParaViewProxy error: "
							+ "Invalid feature \"" + feature + "\".");
				}
			} else {
				throw new IllegalArgumentException("ParaViewProxy error: "
						+ "Invalid category \"" + category + "\".");
			}
		}

		return changed;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getProperties() {
		// Return an ordered copy of the set of property names.
		return new TreeSet<String>(propertyMap.keySet());
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getPropertyValues(String property)
			throws NullPointerException, IllegalArgumentException {
		// Check for a null argument.
		if (property == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot find allowed values for a null property.");
		}

		// Try to get the features for the category from the map.
		Set<String> valueSet = propertyMap.get(property);
		if (valueSet == null) {
			throw new IllegalArgumentException("ParaViewProxy error: "
					+ "Cannot find allowed values for property \"" + property
					+ "\".");
		}

		// Return an ordered copy of the set of allowed values.
		return new TreeSet<String>(valueSet);
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public boolean setProperty(String property, String value)
			throws NullPointerException, IllegalArgumentException {
		// Check for null arguments.
		if (property == null || value == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Null properties and values are not supported.");
		}

		boolean changed = false;

		// Only proceed if the property's value changed.
		if (!value.equals(currentProperties.get(property))) {
			// Get the set of allowed values for the property.
			Set<String> valueSet = propertyMap.get(property);
			if (valueSet != null) {
				// Make sure the value is valid for the property before
				// attempting to update the client.
				if (valueSet.contains(value)) {
					// Only attempt to update the feature and category if the
					// client is connected and can be successfully updated.
					if (connection.getState() == ConnectionState.Connected
							&& setPropertyImpl(connection.getConnection(),
									property, value)) {
						currentProperties.put(property, value);
						changed = true;
					}
				} else {
					throw new IllegalArgumentException("ParaViewProxy error: "
							+ "Invalid property value \"" + value + "\".");
				}
			} else {
				throw new IllegalArgumentException("ParaViewProxy error: "
						+ "Invalid property \"" + property + "\".");
			}
		}

		return changed;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public int setProperties(Map<String, String> properties)
			throws NullPointerException, IllegalArgumentException {
		// Check for null arguments.
		if (properties == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot get new property values from a null map.");
		}

		// Check the input for invalid properties and values.
		Set<Entry<String, String>> entrySet = properties.entrySet();
		for (Entry<String, String> entry : entrySet) {
			String property = entry.getKey();
			String value = entry.getValue();
			// Check for null properties/values.
			if (property == null || value == null) {
				throw new NullPointerException("ParaViewProxy error: "
						+ "Null properties and values are not supported.");
			}
			// Check for invalid properties/values.
			Set<String> valueSet = propertyMap.get(property);
			if (valueSet == null) {
				throw new IllegalArgumentException("ParaViewProxy error: "
						+ "Invalid property \"" + property + "\".");
			} else if (!valueSet.contains(value)) {
				throw new IllegalArgumentException("ParaViewProxy error: "
						+ "Invalid property value \"" + value + "\".");
			}
		}

		// If all properties and values are valid, try setting them all.
		// Increment the count each time a property is changed.
		int count = 0;
		for (Entry<String, String> entry : entrySet) {
			if (setProperty(entry.getKey(), entry.getValue())) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Gets the ParaView ID pointing to the file's proxy on the server.
	 * 
	 * @return The ID for the server's file proxy (the main proxy for the file
	 *         on the server, sometimes just called "the" proxy). If unset, this
	 *         returns {@code -1}.
	 */
	protected int getFileId() {
		return fileId;
	}

	/**
	 * Gets the ParaView ID pointing to the file's associated render view proxy
	 * on the server.
	 * 
	 * @return The ID for the server's view proxy. If unset, this returns
	 *         {@code -1}.
	 */
	public int getViewId() {
		return viewId;
	}

	/**
	 * Gets the ParaView ID pointing to the file's associated representation
	 * proxy on the server.
	 * 
	 * @return The ID for the server's representation proxy. If unset, this
	 *         returns {@code -1}.
	 */
	protected int getRepresentationId() {
		return repId;
	}

	/**
	 * Finds the features in the file by querying the associated ParaView
	 * connection.
	 * <p>
	 * The client should be both valid and connected when this method is called,
	 * and the file will already be opened using the ParaView client.
	 * </p>
	 * 
	 * @param client
	 *            The client used by this proxy.
	 * @return A map of features that can be rendered, keyed on their
	 *         categories. If none can be found, then the returned list should
	 *         be empty and <i>not</i> {@code null}.
	 */
	protected abstract Map<String, Set<String>> findFeatures(VtkWebClient client);

	/**
	 * Finds the properties in the file by querying the associated ParaView
	 * connection.
	 * <p>
	 * The client should be both valid and connected when this method is called,
	 * and the file will already be opened using the ParaView client.
	 * </p>
	 * 
	 * @param client
	 *            The client used by this proxy.
	 * @return A map of properties that can be rendered, keyed on their names.
	 *         If none can be found, then the returned list should be empty and
	 *         <i>not</i> {@code null}.
	 */
	protected abstract Map<String, Set<String>> findProperties(VtkWebClient client);

	/**
	 * Sends the appropriate requests to the client to change the currently
	 * rendered feature.
	 * <p>
	 * The client should be both valid and connected when this method is called,
	 * and the file will already be opened using the ParaView client.
	 * </p>
	 * <p>
	 * Furthermore, the category and feature should be both <i>new</i>
	 * (different from the previous value) and <i>valid</i>.
	 * </p>
	 * 
	 * @param category
	 *            The category for the feature.
	 * @param feature
	 *            The new feature for the ParaView render.
	 * @return True if the new category and feature could be set, false
	 *         otherwise.
	 */
	protected boolean setFeatureImpl(VtkWebClient client, String category,
			String feature) {
		boolean updated = false;

		// Set the "color by" to color based on the feature name.
		JsonArray args = new JsonArray();

		// Add the requisite arguments to the argument array.
		args.add(new JsonPrimitive(Integer.toString(repId)));
		args.add(new JsonPrimitive("ARRAY"));
		args.add(new JsonPrimitive("POINTS"));
		args.add(new JsonPrimitive(feature));
		args.add(new JsonPrimitive("Magnitude"));
		args.add(new JsonPrimitive(0));
		args.add(new JsonPrimitive(true));

		// Call the client.
		try {
			// The only way to tell if the client even received the message is
			// if we get back an empty JsonObject. If null, then there was an
			// error.
			if (client.call("pv.color.manager.color.by", args).get() != null) {
				updated = true;
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return updated;
	}

	/**
	 * Sends the appropriate requests to the client to update any properties
	 * that have been changed in its internal "proxies".
	 * <p>
	 * The client should be both valid and connected when this method is called,
	 * and the file will already be opened using the ParaView client.
	 * </p>
	 * <p>
	 * Furthermore, the property name and value should be both <i>new</i>
	 * (different from the previous value) and <i>valid</i>.
	 * </p>
	 * 
	 * @param property
	 *            The property name.
	 * @param value
	 *            The new value for the property.
	 * @return True if the new property value could be set, false otherwise.
	 */
	protected abstract boolean setPropertyImpl(VtkWebClient client,
			String property, String value);

	/**
	 * Notifies the ParaView client that the view should be refreshed.
	 */
	protected void refresh(VtkWebClient client) {
		// TODO Either implement this or make it abstract.
	}

	/**
	 * Gets the selected category for the feature rendered by ParaView.
	 * 
	 * @return The currently selected category.
	 */
	protected String getCategory() {
		return category;
	}

	/**
	 * Gets the selected feature rendered by ParaView.
	 * 
	 * @return The currently selected feature.
	 */
	protected String getFeature() {
		return feature;
	}

	/**
	 * Gets the selected value for the property.
	 * 
	 * @param property
	 *            The property whose value will be returned.
	 * @return The current value for the property, or {@code null} if the
	 *         property is not set or if the property is {@code null} or
	 *         invalid.
	 */
	protected String getPropertyValue(String property) {
		return currentProperties.get(property);
	}
}