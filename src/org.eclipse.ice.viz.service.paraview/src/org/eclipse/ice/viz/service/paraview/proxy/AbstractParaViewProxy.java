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
import org.eclipse.ice.viz.service.connections.paraview.ParaViewConnectionAdapter;
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
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot open a null URI.");
		}

		// Set the reference to the URI.
		this.uri = uri;

		// Initialize the maps of allowed features and properties.
		featureMap = new HashMap<String, Set<String>>();
		propertyMap = new HashMap<String, IProxyProperty>();

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
	public Future<Boolean> open(ParaViewConnectionAdapter connection)
			throws NullPointerException {
		// Throw an exception if the argument is null.
		if (connection == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Cannot open a proxy with a null connection.");
		}

		final ParaViewConnectionAdapter conn = connection;

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

					// TODO Do we want to allow null values for properties and
					// features?

					// Re-build the map of features.
					featureMap.clear();
					featureMap.putAll(findFeatures(conn));

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
					Set<String> featureSet = featureMap.get(newCategory);
					if (featureSet != null) {
						// Make sure the feature is valid for the category
						// before updating the client.
						if (featureSet.contains(newFeature)) {
							// Only attempt to update the feature and category
							// if the client is connected and can be
							// successfully updated.
							if (connection.getState() == ConnectionState.Connected
									&& setFeatureOnClient(connection,
											newCategory, newFeature)) {
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
	public String getProperty(String name) throws NullPointerException,
			IllegalArgumentException {
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
	protected boolean openProxyOnClient(ParaViewConnectionAdapter connection,
			String fullPath) {
		boolean opened = false;

		IParaViewWebClient client = connection.getConnection();

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

	/**
	 * Finds the features in the file by querying the associated ParaView
	 * connection.
	 * <p>
	 * The connection should be both valid and connected when this method is
	 * called, and the file will already be opened using the ParaView
	 * connection.
	 * </p>
	 * 
	 * @param connection
	 *            The connection used by this proxy.
	 * @return A map of features that can be rendered, keyed on their
	 *         categories. If none can be found, then the returned list should
	 *         be empty and <i>not</i> {@code null}.
	 */
	protected abstract Map<String, Set<String>> findFeatures(
			ParaViewConnectionAdapter connection);

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
			ParaViewConnectionAdapter connection) {
		List<IProxyProperty> properties = new ArrayList<IProxyProperty>();

		// Set up a property that sets the "pv.vcr.action", which can be used to
		// update the timestep. This action takes a single argument, which must
		// be one of "next", "prev", "first", and "last". It returns the current
		// time.
		properties.add(new AbstractProxyProperty("Timestep", this, connection) {
			@Override
			protected String findValue(ParaViewConnectionAdapter connection) {
				// Always start on the first timestep.
				return "first";
			}

			@Override
			protected Set<String> findAllowedValues(
					ParaViewConnectionAdapter connection) {
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
					ParaViewConnectionAdapter connection) {

				IParaViewWebClient client = connection.getConnection();
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
		properties.add(new AbstractProxyProperty("Representation", this,
				connection) {
			@Override
			protected String findValue(ParaViewConnectionAdapter connection) {
				// TODO This can be found from the file.
				return "Surface";
			}

			@Override
			protected Set<String> findAllowedValues(
					ParaViewConnectionAdapter connection) {
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
					ParaViewConnectionAdapter connection) {

				IParaViewWebClient client = connection.getConnection();

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
					response = client.call("pv.proxy.manager.update", args)
							.get();
					if (!response.get("success").getAsBoolean()) {
						System.out
								.println("Failed to set the representation: ");
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
	 * @param connection
	 *            The connection used to open the proxy.
	 * @param category
	 *            The category for the feature.
	 * @param feature
	 *            The new feature for the ParaView render.
	 * @return True if the new category and feature could be set, false
	 *         otherwise.
	 */
	protected abstract boolean setFeatureOnClient(
			ParaViewConnectionAdapter connection, String category,
			String feature);

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

}