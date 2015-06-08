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
import java.util.HashSet;
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
			Map<String, String[]> map;

			// Re-build the map of features.
			featureMap.clear();
			map = findFeatures(client);
			for (Entry<String, String[]> entry : map.entrySet()) {
				String category = entry.getKey();
				String[] features = entry.getValue();

				// Load the array of features into a set.
				Set<String> featureSet = new HashSet<String>();
				for (String feature : features) {
					featureSet.add(feature);
				}
				// Add the category and its set to the map.
				featureMap.put(category, featureSet);
			}

			// Re-build the map of properties.
			propertyMap.clear();
			map = findProperties(client);
			for (Entry<String, String[]> entry : map.entrySet()) {
				String property = entry.getKey();
				String[] values = entry.getValue();

				// Load the array of property values into a set.
				Set<String> valueSet = new HashSet<String>();
				for (String value : values) {
					valueSet.add(value);
				}
				// Add the property and its set to the map.
				propertyMap.put(property, valueSet);
			}
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
		// TODO Auto-generated method stub
		return false;
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
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public int setProperties(Map<String, String> properties)
			throws NullPointerException, IllegalArgumentException {
		// TODO Auto-generated method stub
		return 0;
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
	protected int getViewId() {
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
	protected abstract Map<String, String[]> findFeatures(VtkWebClient client);

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
	protected abstract Map<String, String[]> findProperties(VtkWebClient client);

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
		// TODO Make abstract.
		return false;
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
	protected boolean setPropertyImpl(VtkWebClient client, String property,
			String value) {
		// TODO Make abstract.
		return false;
	}

	/**
	 * Notifies the ParaView client that the view should be refreshed.
	 */
	protected void refresh(VtkWebClient client) {
		// TODO Either implement this or make it abstract.
	}

}