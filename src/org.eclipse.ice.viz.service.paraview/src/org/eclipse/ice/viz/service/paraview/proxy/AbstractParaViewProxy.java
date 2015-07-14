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
package org.eclipse.ice.viz.service.paraview.proxy;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.eclipse.ice.viz.service.connections.ConnectionState;
import org.eclipse.ice.viz.service.paraview.connections.ParaViewConnection;
import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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
	private ParaViewConnection connection = null;

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
	 * The map of features for the proxy, keyed on the feature "name" or
	 * "category". These are features that can be changed to update the
	 * visualization. Neither keys nor values should be {@code null}.
	 */
	private final Map<String, ProxyFeature> featureMap;
	/**
	 * The map of properties. The keys are property names, while the values are
	 * property objects that manage the current property value as well as
	 * syncing the property via the connection.
	 */
	private final Map<String, IProxyProperty> propertyMap;

	/**
	 * The currently selected category.
	 */
	private String category;
	/**
	 * The currently selected feature.
	 */
	private String feature;

	/**
	 * The executor used to run operations on a separate thread.
	 */
	protected final ExecutorService requestExecutor;

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
			throw new NullPointerException(
					"ParaViewProxy error: " + "Cannot open a null URI.");
		}

		// Set the reference to the URI.
		this.uri = uri;

		// Initialize the maps of allowed features and properties.
		// featureMap = new HashMap<String, Set<String>>();
		propertyMap = new HashMap<String, IProxyProperty>();
		featureMap = new HashMap<String, ProxyFeature>();

		// Initialize the current feature and properties.
		category = null;
		feature = null;

		// Initialize the executor used to run operations on another thread.
		requestExecutor = Executors.newSingleThreadExecutor();

		return;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Future<Boolean> open(ParaViewConnection connection)
			throws NullPointerException {
		// Throw an exception if the argument is null.
		if (connection == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot open a proxy with a null connection.");
		}

		final ParaViewConnection conn = connection;

		// Create a new callable to open the connection. This will need to be
		// run in a separate thread.
		Callable<Boolean> operation = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				// Set the default return value.
				boolean opened = false;

				// Only attempt to open the file if the connection is
				// established.
				if (conn.getState() == ConnectionState.Connected) {
					// Validate that the connection server and the URI point to
					// the same server.

					// Get the connection host.
					String clientHost = conn.getHost();

					// Get the URI host.
					String fileHost = uri.getHost();
					if (fileHost == null) {
						fileHost = "localhost";
					}

					// TODO We need better validation of hostnames, local vs
					// remote, FQDN vs IP, etc. For instance, there should be no
					// difference between "localhost" and whatever the existing
					// hostname is.

					// If they match, attempt to open the file.
					if (clientHost != null && clientHost.equals(fileHost)) {
						opened = openProxyOnClient(conn, uri.getPath());
					}
				}

				// If the connection was opened, re-build the maps of features
				// and properties.
				if (opened) {
					// Update the reference to the current connection.
					AbstractParaViewProxy.this.connection = conn;

					// Re-build the map of features.
					featureMap.clear();
					for (ProxyFeature featureInfo : findFeatures(conn)) {
						featureMap.put(featureInfo.name, featureInfo);

						// Load the current settings for the feature.
						loadFeatureAllowedValues(featureInfo);
					}

					// Re-build the map of properties.
					propertyMap.clear();
					for (IProxyProperty property : findProperties(conn)) {
						// Get the name of the property if possible.
						String name = (property != null ? property.getName()
								: null);
						// Load properties with names into the property map.
						if (name != null) {
							propertyMap.put(name, property);
						}
					}

					// Set the initial category and feature.
					category = null;
					feature = null;
					if (!featureMap.isEmpty()) {
						for (ProxyFeature featureInfo : featureMap.values()) {
							if (!featureInfo.allowedValues.isEmpty()) {
								category = featureInfo.name;
								feature = featureInfo.allowedValues.iterator()
										.next();
								featureInfo.selectedValues.add(feature);
								break;
							}
						}
					}

					// Apply the currently selected values to the file proxy.
					// This ensures that only one category/feature is currently
					// selected.
					JsonArray changedProperties = new JsonArray();
					for (ProxyFeature featureInfo : featureMap.values()) {
						changedProperties
								.add(createPropertyChangeObject(featureInfo));
					}
					applyProperties(changedProperties);
					// Update the view by coloring it if possible.
					if (category != null) {
						setColorBy(featureMap.get(category), feature);
					}
				}

				return opened;
			}
		};

		// Kick off the operation in a new thread. The caller may use the
		// returned Future to wait on the operation to complete.
		return requestExecutor.submit(operation);
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
		ProxyFeature feature = featureMap.get(category);
		if (feature == null) {
			throw new IllegalArgumentException("ParaViewProxy error: "
					+ "Cannot find features for category \"" + category
					+ "\".");
		}

		// Return an ordered copy of the set of allowed features.
		return new TreeSet<String>(feature.allowedValues);
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Future<Boolean> setFeature(String category, String feature)
			throws NullPointerException, IllegalArgumentException {
		// Check for null arguments.
		if (category == null || feature == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Null categories and features are not supported.");
		}

		final String newCategory = category;
		final String newFeature = feature;

		// Create a new callable to set the feature. This will need to be run in
		// a separate thread.
		Callable<Boolean> operation = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				boolean changed = false;

				String category = getCategory();
				String feature = getFeature();

				// Only proceed if the feature and/or category changed.
				if (!newCategory.equals(category)
						|| !newFeature.equals(feature)) {
					// Get the set of features for the category.
					ProxyFeature featureInfo = featureMap.get(newCategory);
					if (featureInfo != null) {
						Set<String> featureSet = featureInfo.allowedValues;

						// Make sure the feature is valid for the category
						// before updating the client.
						if (featureSet.contains(newFeature)) {
							// Only attempt to update the feature and category
							// if the client is connected and can be
							// successfully updated.
							if (connection
									.getState() == ConnectionState.Connected
									&& setFeatureOnClient(newCategory,
											newFeature)) {
								AbstractParaViewProxy.this.category = newCategory;
								AbstractParaViewProxy.this.feature = newFeature;
								changed = true;
							}
						} else {
							throw new IllegalArgumentException(
									"ParaViewProxy error: "
											+ "Invalid feature \"" + newFeature
											+ "\".");
						}
					} else {
						throw new IllegalArgumentException(
								"ParaViewProxy error: " + "Invalid category \""
										+ newCategory + "\".");
					}
				}

				return changed;
			}
		};

		// Kick off the operation in a new thread. The caller may use the
		// returned Future to wait on the operation to complete.
		return requestExecutor.submit(operation);
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Map<String, String> getProperties() {
		// Load the property values from the IProxyPropertys into a new map,
		// which will be returned.
		Map<String, String> properties = new TreeMap<String, String>();
		for (Entry<String, IProxyProperty> e : propertyMap.entrySet()) {
			properties.put(e.getKey(), e.getValue().getValue());
		}
		return properties;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public String getProperty(String name)
			throws NullPointerException, IllegalArgumentException {
		// Return the current value of the property.
		return getProxyProperty(name).getValue();
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Set<String> getPropertyAllowedValues(String name)
			throws NullPointerException, IllegalArgumentException {
		return getProxyProperty(name).getAllowedValues();
	}

	/**
	 * Gets the {@link IProxyProperty} from {@link #propertyMap}, throwing an
	 * exception if the property is somehow invalid.
	 * 
	 * @param name
	 *            The name of the property.
	 * @return The property associated with the specified name.
	 * @throws NullPointerException
	 *             If the name is {@code null} and not in the map of properties.
	 * @throws IllegalArgumentException
	 *             If the name is an invalid property name not in the map of
	 *             properties.
	 */
	private IProxyProperty getProxyProperty(String name)
			throws NullPointerException, IllegalArgumentException {
		IProxyProperty property = propertyMap.get(name);
		if (property == null) {
			if (name == null) {
				throw new NullPointerException("ParaViewProxy error: "
						+ "Null properties are not supported.");
			} else {
				throw new IllegalArgumentException("ParaViewProxy error: "
						+ "The property \"" + name + "\" is invalid.");
			}
		}
		return property;
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Future<Boolean> setProperty(String name, String value)
			throws NullPointerException, IllegalArgumentException {

		final String newName = name;
		final String newValue = value;

		// Create a callable to perform the property update. This will need to
		// connect with the host to update the ParaView proxy.
		Callable<Boolean> operation = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				// Get the property from the map. This handles error checking
				// for the property name.
				IProxyProperty property = getProxyProperty(newName);

				// Attempt to set the value. This handles error checking for the
				// property value.
				return property.setValue(newValue);
			}
		};

		// Kick off the operation in a new thread. The caller may use the
		// returned Future to wait on the operation to complete.
		return requestExecutor.submit(operation);
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public Future<Integer> setProperties(Map<String, String> properties)
			throws NullPointerException {

		final Map<String, String> newProperties = properties;

		// Create a callable to perform the property update. This will need to
		// connect with the host to update the ParaView proxy.
		Callable<Integer> operation = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {

				int count = 0;

				// Try to update all properties specified in the map.
				for (Entry<String, String> entry : newProperties.entrySet()) {
					String name = entry.getKey();
					String value = entry.getValue();

					try {
						// Fetch the property first. This may throw an exception
						// if the property is invalid.
						IProxyProperty property = getProxyProperty(name);
						// Attempt to set the value. This may throw an exception
						// if the value is invalid or the property is read-only.
						if (property.setValue(value)) {
							// If the value changed, increment the count.
							count++;
						}
					} catch (NullPointerException | IllegalArgumentException
							| UnsupportedOperationException e) {
						System.err.println(e.getMessage());
					} catch (Exception e) {
						System.err.println("ParaViewProxy warning: "
								+ "Unexpected exception!");
						System.err.println(e.getMessage());
					}
				}

				// Return the number of changed properties.
				return count;
			}
		};

		// Kick off the operation in a new thread. The caller may use the
		// returned Future to wait on the operation to complete.
		return requestExecutor.submit(operation);
	}

	/*
	 * Implements a method from IParaViewProxy.
	 */
	@Override
	public int getViewId() {
		return viewId;
	}

	/**
	 * Opens the file at the specified path using the ParaView connection. When
	 * called, the full path is expected to be a path on the connection's host.
	 * <p>
	 * This method should set {@link #fileId}, {@link #viewId}, and
	 * {@link #repId}.
	 * </p>
	 * 
	 * @param connection
	 *            The connection used to open the file.
	 * @param fullPath
	 *            The full path on the connection's host machine to the file
	 *            that will be opened.
	 * @return True if the file at the specified path on the host machine could
	 *         be opened, false otherwise.
	 */
	protected boolean openProxyOnClient(ParaViewConnection connection,
			String fullPath) {
		boolean opened = false;

		IParaViewWebClient client = connection.getWidget();

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
				System.err.println("ParaViewProxy error: " + "Failed to open \""
						+ fullPath + "\". Reason: " + reason);
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			System.err.println("ParaViewProxy error: " + "Failed to open \""
					+ fullPath + "\" due to connection error.");
		}

		return opened;
	}

	protected abstract List<ProxyFeature> findFeatures(
			ParaViewConnection connection);

	private JsonObject createPropertyChangeObject(ProxyFeature feature) {
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

	private JsonArray getFileArray(String id) {
		JsonObject object;
		JsonArray array = null;
		IParaViewWebClient widget = connection.getWidget();
		try {
			// Query the client for the file proxy's information.
			array = new JsonArray();
			array.add(new JsonPrimitive(Integer.toString(getFileId())));
			object = widget.call("pv.proxy.manager.get", array).get();

			// Try to get the array with the specified ID.
			array = null;
			array = object.get(id).getAsJsonArray();
		} catch (InterruptedException | ExecutionException e) {
			System.err.println("ParaViewProxy error: "
					+ "Connection error while getting file proxy information.");
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			System.err.println("ParaViewProxy error: "
					+ "Error while reading file proxy information.");
		}
		return array;
	}

	private boolean applyProperties(JsonArray changedProperties) {
		boolean updated = false;

		if (changedProperties.size() > 0) {

			// Get the ParaView web client widget.
			IParaViewWebClient widget = connection.getWidget();

			JsonObject object;
			// Create the argument array.
			JsonArray array = new JsonArray();
			array.add(changedProperties);
			try {
				// Update the web client.
				object = widget.call("pv.proxy.manager.update", array).get();
				// Check the response to verify that the operation was
				// successful.
				updated = object.get("success").getAsBoolean();
				if (!updated) {
					System.err.println("Failed to apply the properties: ");
					array = object.get("errorList").getAsJsonArray();
					for (int i = 0; i < array.size(); i++) {
						System.err.println(array.get(i));
					}
				}
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			} catch (NullPointerException | ClassCastException
					| IllegalStateException e) {
				e.printStackTrace();
			}
		}

		return updated;
	}

	private void loadFeatureAllowedValues(ProxyFeature feature) {
		JsonArray uiArray = getFileArray("ui");
		JsonObject object = uiArray.get(feature.index).getAsJsonObject();
		// Add all strings from the "values" array to a new, ordered set.
		JsonArray array = object.get("values").getAsJsonArray();
		for (int i = 0; i < array.size(); i++) {
			feature.allowedValues.add(array.get(i).getAsString());
		}
		return;
	}

	private void loadFeatureSelectedValues(ProxyFeature feature) {
		JsonArray propArray = getFileArray("properties");
		JsonObject object = propArray.get(feature.index).getAsJsonObject();
		JsonArray array = object.get("value").getAsJsonArray();
		for (int i = 0; i < array.size(); i++) {
			feature.selectedValues.add(array.get(i).getAsString());
		}
		return;
	}

	private boolean setColorBy(ProxyFeature featureInfo, String feature) {
		boolean updated = false;

		// Get the ParaView web client widget.
		IParaViewWebClient widget = connection.getWidget();

		// Determine the "arrayName". If unsetting the current color, use
		// the empty string.
		final String arrayName = featureInfo.canColorBy ? feature : "";

		// Set the "color by" to color based on the feature name.
		JsonArray args = new JsonArray();

		// Add the required arguments to the argument array.
		args.add(new JsonPrimitive(Integer.toString(getRepresentationId())));
		args.add(new JsonPrimitive(featureInfo.colorByMode.toString()));
		args.add(new JsonPrimitive(featureInfo.colorByLocation.toString()));
		args.add(new JsonPrimitive(arrayName));
		args.add(new JsonPrimitive("Magnitude"));
		args.add(new JsonPrimitive(0));
		args.add(new JsonPrimitive(true));

		// Post the color by change to the client.
		try {
			// The only way to tell if the client even received the message
			// is if we get back an empty JsonObject. If null, then there
			// was an error.
			if (widget.call("pv.color.manager.color.by", args).get() != null) {
				updated = true;
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		return updated;
	}

	/**
	 * Finds the properties in the file by querying the associated ParaView
	 * connection.
	 * <p>
	 * The connection should be both valid and connected when this method is
	 * called, and the file will already be opened using the ParaView
	 * connection.
	 * </p>
	 * 
	 * @param connection
	 *            The connection used by this proxy.
	 * @return A map of properties that can be rendered, keyed on their names.
	 *         If none can be found, then the returned list should be empty and
	 *         <i>not</i> {@code null}.
	 */
	protected List<IProxyProperty> findProperties(
			ParaViewConnection connection) {
		List<IProxyProperty> properties = new ArrayList<IProxyProperty>();

		// Set up a property that sets the "pv.vcr.action", which can be used to
		// update the timestep. This action takes a single argument, which must
		// be one of "next", "prev", "first", and "last". It returns the current
		// time.
		properties.add(new AbstractProxyProperty("Timestep", this, connection) {
			@Override
			protected String findValue(ParaViewConnection connection) {
				// Always start on the first timestep.
				return "first";
			}

			@Override
			protected Set<String> findAllowedValues(
					ParaViewConnection connection) {
				// Set up the four allowed values.
				Set<String> allowedValues = new HashSet<String>();
				allowedValues.add("first");
				allowedValues.add("last");
				allowedValues.add("next");
				allowedValues.add("prev");
				return allowedValues;
			}

			@Override
			protected boolean setValueOnClient(String value,
					ParaViewConnection connection) {

				IParaViewWebClient client = connection.getWidget();
				boolean updated = false;

				// Set up the arguments to pv.vcr.action, which takes a single
				// string "action", which is one of "first", "last", "next",
				// "prev".
				JsonArray args = new JsonArray();
				args.add(new JsonPrimitive(value));

				// Send the request to the client.
				try {
					JsonObject response = client.call("pv.vcr.action", args)
							.get();
					System.out.println(response.toString());
					updated = true;
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

				return updated;
			}
		});

		// Set up a property that can be used to set the representation type.
		properties.add(
				new AbstractProxyProperty("Representation", this, connection) {
					@Override
					protected String findValue(ParaViewConnection connection) {
						// TODO This can be found from the file.
						return "Surface";
					}

					@Override
					protected Set<String> findAllowedValues(
							ParaViewConnection connection) {
						// TODO This can be found from the file.
						Set<String> allowedValues = new HashSet<String>();
						allowedValues.add("3D Glyphs");
						allowedValues.add("Outline");
						allowedValues.add("Points");
						allowedValues.add("Surface");
						allowedValues.add("Surface With Edges");
						allowedValues.add("Volume");
						allowedValues.add("Wireframe");
						return allowedValues;
					}

					@Override
					protected boolean setValueOnClient(String value,
							ParaViewConnection connection) {

						IParaViewWebClient client = connection.getWidget();

						boolean updated = false;

						JsonArray args = new JsonArray();
						JsonArray updatedProperties = new JsonArray();
						JsonObject repProperty = new JsonObject();
						repProperty.addProperty("id",
								Integer.toString(getRepresentationId()));
						repProperty.addProperty("name", "Representation");
						repProperty.addProperty("value", value);
						updatedProperties.add(repProperty);

						// Update the properties that were configured.
						args = new JsonArray();
						args.add(updatedProperties);
						JsonObject response;
						try {
							response = client
									.call("pv.proxy.manager.update", args)
									.get();
							if (!response.get("success").getAsBoolean()) {
								System.out.println(
										"Failed to set the representation: ");
								JsonArray array = response.get("errorList")
										.getAsJsonArray();
								for (int i = 0; i < array.size(); i++) {
									System.out.println(array.get(i));
								}
							}
							updated = true;
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}

						return updated;
					}
				});

		return properties;
	}

	/**
	 * Sends the appropriate requests over the connection to change the
	 * currently rendered feature.
	 * <p>
	 * The connection should be both valid and connected when this method is
	 * called, and the file will already be opened using the ParaView
	 * connection.
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
	private boolean setFeatureOnClient(String category, String feature) {
		boolean updated = false;

		// Get the previous feature.
		String oldCategory = getCategory();
		String oldFeature = getFeature();
		// Get the SiloFeature for the new feature.
		ProxyFeature featureInfo = getProxyFeature(category);
		// Create a JsonArray for all changed or updated properties. These will
		// be sent to the web client in bulk.
		JsonArray changedProperties = new JsonArray();

		// If the categories are the same, remove the old feature.
		if (category.equals(oldCategory)) {
			featureInfo.selectedValues.remove(oldFeature);
		}
		// Otherwise, only remove the old feature if the previous feature was
		// not a mesh.
		else {
			ProxyFeature oldFeatureInfo = getProxyFeature(oldCategory);
			oldFeatureInfo.selectedValues.remove(oldFeature);
			// Since the categories are different, we have to update another
			// property in the file proxy.
			changedProperties.add(createPropertyChangeObject(oldFeatureInfo));
		}
		// Add the new feature and get the property change.
		featureInfo.selectedValues.add(feature);
		changedProperties.add(createPropertyChangeObject(featureInfo));

		// Try to change the properties. If successful, update the "color by"
		// property.
		if (applyProperties(changedProperties)) {
			setColorBy(featureInfo, feature);
			updated = true;
		}
		// Otherwise, revert the changes to the category and feature.
		else {
			getProxyFeature(oldCategory).selectedValues.add(oldFeature);
			featureInfo.selectedValues.remove(feature);
		}

		return updated;
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
	 * Gets the {@link ProxyFeature} for the specified feature category.
	 * 
	 * @param category
	 *            The category of the feature (its "ui" element name).
	 * @return The associated {@code ProxyFeature}, or {@code null} if it does
	 *         not exist.
	 */
	protected ProxyFeature getProxyFeature(String category) {
		return featureMap.get(category);
	}

}