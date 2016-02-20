/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - bug 475301
 *******************************************************************************/
package org.eclipse.eavp.viz.service.paraview.proxy;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
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

import org.eclipse.eavp.viz.service.connections.ConnectionState;
import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.paraview.proxy.ProxyProperty.PropertyType;
import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * This class provides a base implementation for {@link IParaViewProxy} and
 * handles updating the ParaView web client when the proxy's information
 * changes.
 * 
 * @author Jordan Deyton
 *
 */
public class AbstractParaViewProxy implements IParaViewProxy {

	/**
	 * The currently selected category.
	 */
	private String category;
	/**
	 * The current connection used to open or manipulate the file specified by
	 * the {@link #uri}.
	 */
	private IVizConnection<IParaViewWebClient> connection = null;
	/**
	 * The currently selected feature.
	 */
	private String feature;
	/**
	 * The map of features for the proxy, keyed on the feature "name" or
	 * "category". These are features that can be changed to update the
	 * visualization. Neither keys nor values should be {@code null}.
	 */
	private final Map<String, ProxyFeature> featureMap;
	/**
	 * The ParaView ID pointing to the file's proxy on the server.
	 */
	private int fileId;
	/**
	 * The map of properties. The keys are property names, while the values are
	 * property objects that manage the current property value as well as
	 * syncing the property via the connection.
	 */
	private final Map<String, ProxyProperty> propertyMap;
	/**
	 * The ParaView ID pointing to the file's associated representation proxy on
	 * the server.
	 */
	private int repId;
	/**
	 * The executor used to run operations on a separate thread.
	 */
	protected final ExecutorService requestExecutor;
	/**
	 * The timesteps for the proxy.
	 */
	private final List<Double> times;
	/**
	 * The current timestep.
	 */
	private int timestep;
	/**
	 * The URI for this proxy. This should only ever be set once.
	 */
	private final URI uri;

	/**
	 * The ParaView ID pointing to the file's associated render view proxy on
	 * the server.
	 */
	private int viewId;

	/**
	 * The target timestep. This is used in a worker thread when the timestep is
	 * changed as there is no way to tell the web client to just change to a
	 * specific timestep.
	 */
	private int targetTimestep = 0;

	/**
	 * Whether or not the scalar bar should be based on the data range from all
	 * timesteps or just the current timestep.
	 */
	private boolean scaleByAllTimes = false;
	/**
	 * Whether or not the scalar bar should be shown.
	 */
	private boolean showScalarBar = true;

	/**
	 * The default constructor.
	 * 
	 * @param uri
	 *            The URI for the ParaView-supported file.
	 * @throws NullPointerException
	 *             If the specified URI is null.
	 */
	public AbstractParaViewProxy(URI uri) throws NullPointerException {
		// Throw an exception if the argument is null.
		if (uri == null) {
			throw new NullPointerException(
					"ParaViewProxy error: " + "Cannot open a null URI.");
		}

		// Set the reference to the URI.
		this.uri = uri;

		// Initialize the maps of allowed features and properties.
		featureMap = new HashMap<String, ProxyFeature>();
		propertyMap = new HashMap<String, ProxyProperty>();

		// Initialize the current feature.
		category = null;
		feature = null;

		// Initialize the executor used to run operations on another thread.
		requestExecutor = Executors.newSingleThreadExecutor();

		// Initialize the times.
		times = new ArrayList<Double>();
		timestep = 0;

		// Initialize the underlying proxy IDs.
		fileId = -1;
		repId = -1;
		viewId = -1;

		return;
	}

	/**
	 * Finds all features for this proxy. This method is intended to be
	 * overridden by sub-classes to provide their own list of supported
	 * features.
	 * 
	 * @return The list of supported features. The default list is empty.
	 */
	protected List<ProxyFeature> findFeatures() {
		return new ArrayList<ProxyFeature>();
	}

	/**
	 * Finds all properties for this proxy. This method is intended to be
	 * overridden by sub-classes to provide their own list of supported
	 * properties.
	 * 
	 * @return The list of supported properties. The default list includes a
	 *         property for changing how the data is rendered (solid, wire,
	 *         etc.).
	 */
	protected List<ProxyProperty> findProperties() {
		List<ProxyProperty> properties = new ArrayList<ProxyProperty>();

		// Add a property to change the representation. This is a discrete
		// property stored in the "representation" proxy.
		properties.add(new ProxyProperty("Representation", 1) {
			@Override
			protected int getProxyId() {
				return repId;
			}
		});

		return properties;
	}

	/**
	 * Finds all available timesteps for this proxy.
	 * 
	 * @return A list containing all the timesteps from the proxy.
	 */
	private List<Double> findTimesteps() {

		/*
		 * To get the timesteps, we must query the "file" proxy's "data"
		 * JsonObject. It has an entry "time" that is a JsonArray of double
		 * values for all allowed times in the file.
		 */

		List<Double> timesteps = new ArrayList<Double>();
		JsonObject proxyObject = getProxyObject(fileId);
		try {
			JsonObject dataObject = proxyObject.get("data").getAsJsonObject();
			JsonArray valueArray = dataObject.get("time").getAsJsonArray();
			for (int i = 0; i < valueArray.size(); i++) {
				timesteps.add(valueArray.get(i).getAsDouble());
			}
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			e.printStackTrace();
		}
		return timesteps;
	}

	/**
	 * Gets the current category set on the proxy.
	 * 
	 * @return The current category.
	 */
	protected String getCategory() {
		return category;
	}

	/**
	 * Gets the current connection associated with this proxy.
	 * 
	 * @return The proxy's current connection, or {@code null} if it has not
	 *         been set.
	 */
	public IVizConnection<IParaViewWebClient> getConnection() {
		return connection;
	}

	/**
	 * Gets the current feature set on the proxy.
	 * 
	 * @return The current feature.
	 */
	protected String getFeature() {
		return feature;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#getFeatureCategories()
	 */
	@Override
	public Set<String> getFeatureCategories() {
		// Return an ordered copy of the set of feature categories.
		return new TreeSet<String>(featureMap.keySet());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#getFeatures(java.lang.String)
	 */
	@Override
	public Set<String> getFeatures(String category) {
		// Return an ordered copy of the set of allowed features.
		ProxyFeature feature = featureMap.get(category);
		return feature != null ? new TreeSet<String>(feature.getAllowedValues())
				: null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#getFileId()
	 */
	@Override
	public int getFileId() {
		return fileId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#getProperties()
	 */
	@Override
	public Map<String, String> getProperties() {
		// Load the property values from the IProxyPropertys into a new map,
		// which will be returned.
		Map<String, String> properties = new TreeMap<String, String>();
		for (Entry<String, ProxyProperty> e : propertyMap.entrySet()) {
			properties.put(e.getKey(), e.getValue().getValue());
		}
		return properties;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#getProperty(java.lang.String)
	 */
	@Override
	public String getProperty(String name) {
		ProxyProperty property = propertyMap.get(name);
		return property != null ? property.getValue() : null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#getPropertyAllowedValues(java.lang.String)
	 */
	@Override
	public Set<String> getPropertyAllowedValues(String name) {
		ProxyProperty property = propertyMap.get(name);
		return property != null ? property.getAllowedValues() : null;
	}

	/**
	 * Queries the client for the proxy object.
	 * 
	 * @param id
	 *            The ID of the desired proxy object. This is usually either the
	 *            file, view, or representation ID.
	 * @return The proxy's JsonObject, or {@code null} if it could not be found.
	 */
	protected JsonObject getProxyObject(int id) {
		JsonObject object = null;
		JsonArray args = new JsonArray();
		args.add(new JsonPrimitive(id));
		try {
			object = connection.getWidget().call("pv.proxy.manager.get", args)
					.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return object;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#getRepresentationId()
	 */
	public int getRepresentationId() {
		return repId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#getTimesteps()
	 */
	@Override
	public List<Double> getTimesteps() {
		return new ArrayList<Double>(times);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#getURI()
	 */
	@Override
	public URI getURI() {
		return uri;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#getViewId()
	 */
	@Override
	public int getViewId() {
		return viewId;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#open(org.eclipse.eavp.viz.service.connections.IVizConnection)
	 */
	@Override
	public Future<Boolean> open(IVizConnection<IParaViewWebClient> connection) {
		final IVizConnection<IParaViewWebClient> conn = connection;

		// Create a new callable to open the connection. This will need to be
		// run in a separate thread.
		Callable<Boolean> operation = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				// Attempt to connect if the connection is not null, otherwise
				// just return false.
				return conn != null ? setConnection(conn) : false;
			}
		};

		// Kick off the operation in a new thread. The caller may use the
		// returned Future to wait on the operation to complete.
		return requestExecutor.submit(operation);
	}

	/**
	 * Opens the specified file on the web client. This also sets the
	 * {@link #fileId}, {@link #viewId}, and {@link #repId} if the file could be
	 * opened.
	 * 
	 * @param client
	 *            The web client that should try to open the file.
	 * @param fullPath
	 *            The full path to the file.
	 * @return True if the file could be opened, false otherwise.
	 */
	private boolean openFile(IParaViewWebClient client, String fullPath) {
		boolean opened = false;

		// The argument array must contain the full path to the file.
		JsonArray args = new JsonArray();
		args.add(new JsonPrimitive(fullPath));
		try {
			// Attempt to create a view on the ParaView server.
			JsonObject response = client.call("createView", args).get();
			// If the response does not contain an "error" value, then try to
			// determine the file, view, and representation IDs.
			if (response.get("error") == null) {
				int fileId = response.get("proxyId").getAsInt();
				int viewId = response.get("viewId").getAsInt();
				int repId = response.get("repId").getAsInt();

				// Update the IDs if everything worked.
				this.fileId = fileId;
				this.viewId = viewId;
				this.repId = repId;
				opened = true;
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			e.printStackTrace();
		}

		return opened;
	}

	/**
	 * Tells the client to refresh the scalar bar. This does not include scaling
	 * it, but rather ensures that the scalar bar is visible as necessary.
	 * 
	 * @return A task for refreshing the scalar bar for the current feature.
	 */
	private Future<JsonObject> refreshScalarBar() {
		IParaViewWebClient widget = connection.getWidget();
		JsonArray args;

		// Set the visibility of the legend to true.
		JsonObject legendVisibilities = new JsonObject();
		legendVisibilities.addProperty(Integer.toString(fileId), showScalarBar);
		args = new JsonArray();
		args.add(legendVisibilities);

		return widget.call("pv.color.manager.scalarbar.visibility.set", args);
	}

	/**
	 * Tells the client to re-scale the data. This affects both the scalar bar
	 * and the visualization colors.
	 * 
	 * @return A task for re-scaling the data on the web client.
	 */
	private Future<JsonObject> rescale() {
		IParaViewWebClient widget = connection.getWidget();
		JsonArray args;

		// Auto-scale the color map to the data.
		args = new JsonArray();
		JsonObject scaleOptions = new JsonObject();
		scaleOptions.addProperty("type", scaleByAllTimes ? "time" : "data");
		scaleOptions.addProperty("proxyId", Integer.toString(fileId));
		args.add(scaleOptions);

		// Make the request, but don't wait.
		return widget.call("pv.color.manager.rescale.transfer.function", args);
	}

	/**
	 * Tells the client to color the visualization using the specified feature.
	 * 
	 * @param feature
	 *            The feature's metadata. This comes from the category.
	 * @param value
	 *            The value to use from the feature metadata. This is equivalent
	 *            to the category's feature.
	 * @return A task for setting the visualization feature data on the web
	 *         client.
	 */
	private Future<JsonObject> setColorBy(ProxyFeature feature, String value) {
		IParaViewWebClient widget = connection.getWidget();
		JsonArray args;

		// Determine the "arrayName". If unsetting the current color, use
		// the empty string.
		final String arrayName = feature.canColorBy ? value : "";

		// Add the required arguments to the argument array.
		args = new JsonArray();
		args.add(new JsonPrimitive(Integer.toString(getRepresentationId())));
		args.add(new JsonPrimitive(feature.colorByMode.toString()));
		args.add(new JsonPrimitive(feature.colorByLocation.toString()));
		args.add(new JsonPrimitive(arrayName));
		args.add(new JsonPrimitive("Magnitude"));
		args.add(new JsonPrimitive(0));
		args.add(new JsonPrimitive(true));

		// Post the request to the client.
		return widget.call("pv.color.manager.color.by", args);
	}

	/**
	 * Sets the connection and updates the proxy's information and properties
	 * based on the new connection.
	 * 
	 * @param connection
	 *            The new connection.
	 * @return True if the connection could be changed (the URI could be
	 *         opened), false otherwise.
	 */
	private boolean setConnection(
			IVizConnection<IParaViewWebClient> connection) {
		boolean opened = false;

		if (connection != this.connection
				&& connection.getState() == ConnectionState.Connected) {
			IParaViewWebClient widget = connection.getWidget();

			// Open the file on the web client.
			if (openFile(widget, uri.getPath())) {
				opened = true;

				// Update the reference to the connection.
				this.connection = connection;

				// ---- Reset the proxy information. ---- //
				// Reset the timesteps.
				times.clear();
				times.addAll(findTimesteps());
				timestep = 0;

				// Reset the features.
				featureMap.clear();
				for (ProxyFeature featureInfo : findFeatures()) {
					if (featureInfo.setConnection(connection)) {
						featureMap.put(featureInfo.name, featureInfo);
					}
				}

				// Reset the properties.
				propertyMap.clear();
				for (ProxyProperty propertyInfo : findProperties()) {
					if (propertyInfo.setConnection(connection)) {
						propertyMap.put(propertyInfo.name, propertyInfo);
					}
				}

				// Reset the selected category and feature.
				category = null;
				feature = null;

				// Find the first selected feature if one exists.
				ProxyFeature selectedFeature = null;
				for (ProxyFeature featureInfo : featureMap.values()) {
					if (featureInfo.type == PropertyType.DISCRETE_MULTI) {
						List<String> values = featureInfo.getValues();
						if (!values.isEmpty()) {
							selectedFeature = featureInfo;
							feature = values.get(0);
							break;
						}
					} else if (featureInfo.getValue() != null) {
						selectedFeature = featureInfo;
						feature = featureInfo.getValue();
						break;
					}
				}

				// If a feature is selected, refresh the view contents.
				if (selectedFeature != null) {
					category = selectedFeature.name;
					// Refresh the view.
					setColorBy(selectedFeature, feature);
					rescale();
					refreshScalarBar();
				}
				// -------------------------------------- //
			}
		}

		return opened;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#setFeature(java.lang.String, java.lang.String)
	 */
	@Override
	public Future<Boolean> setFeature(String category, String feature) {

		final String newCategory = category;
		final String newFeature = feature;

		// Create a new callable to set the feature. This will need to be run in
		// a separate thread.
		Callable<Boolean> operation = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				boolean changed = false;
				ProxyFeature featureInfo = featureMap.get(newCategory);
				// We can only attempt to set the feature if (A) the connection
				// is connected and (B) the category and feature are valid.
				if (connection != null
						&& connection.getState() == ConnectionState.Connected
						&& featureInfo != null
						&& featureInfo.valueAllowed(newFeature)) {
					changed = setFeatureOnClient(newCategory, newFeature);
				}
				return changed;
			}
		};

		// Kick off the operation in a new thread. The caller may use the
		// returned Future to wait on the operation to complete.
		return requestExecutor.submit(operation);
	}

	/**
	 * Attempts to set the new category and feature on the client.
	 * 
	 * @param category
	 *            The new category.
	 * @param feature
	 *            The new feature.
	 * @return True if they changed and the view was updated, false otherwise.
	 */
	private boolean setFeatureOnClient(String category, String feature) {
		boolean updated = false;

		String oldCategory = getCategory();
		String oldFeature = getFeature();

		// Only proceed if the feature and/or category changed. Also, the
		// connection must be established.
		if (!category.equals(oldCategory) || !feature.equals(oldFeature)) {
			// Get the SiloFeature for the new feature.
			ProxyFeature featureInfo = featureMap.get(category);

			// If the new feature is from a new category, unset the old feature
			// from the old category if possible.
			if (!category.equals(oldCategory) && oldCategory != null) {
				ProxyFeature oldFeatureInfo = featureMap.get(oldCategory);
				updated |= oldFeatureInfo.setValue(null);
			}

			// Set the new feature.
			updated |= featureInfo.setValue(feature);

			if (updated) {
				// Update the references to the category and feature.
				this.category = category;
				this.feature = feature;

				// Refresh the view.
				setColorBy(featureInfo, feature);
				rescale();
				refreshScalarBar();
			}
		}

		return updated;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#setProperties(java.util.Map)
	 */
	@Override
	public Future<Integer> setProperties(Map<String, String> properties) {

		final Map<String, String> newProperties = properties;

		// Create a callable to perform the property update. This will need to
		// connect with the host to update the ParaView proxy.
		Callable<Integer> operation = new Callable<Integer>() {
			@Override
			public Integer call() throws Exception {

				int count = 0;

				if (newProperties != null) {
					// Try to update all properties specified in the map.
					for (Entry<String, String> entry : newProperties
							.entrySet()) {
						String name = entry.getKey();
						String value = entry.getValue();

						// Fetch the property first. This may throw an exception
						// if the property is invalid.
						ProxyProperty property = propertyMap.get(name);
						// Attempt to set the value. This may throw an exception
						// if the value is invalid or the property is read-only.
						if (property != null && property.setValue(value)) {
							// If the value changed, increment the count.
							count++;
						}
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
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#setProperty(java.lang.String, java.lang.String)
	 */
	@Override
	public Future<Boolean> setProperty(String name, String value) {

		final String newName = name;
		final String newValue = value;

		// Create a callable to perform the property update. This will need to
		// connect with the host to update the ParaView proxy.
		Callable<Boolean> operation = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				boolean changed = false;
				// We can only attempt to set the property if (A) the connection
				// is connected and (B) the property name and value are valid.
				ProxyProperty property = propertyMap.get(newName);
				if (connection != null
						&& connection.getState() == ConnectionState.Connected
						&& property != null
						&& property.valueAllowed(newValue)) {
					changed = property.setValue(newValue);
				}
				return changed;
			}
		};

		// Kick off the operation in a new thread. The caller may use the
		// returned Future to wait on the operation to complete.
		return requestExecutor.submit(operation);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.eavp.viz.service.paraview.proxy.IParaViewProxy#setTimestep(int)
	 */
	@Override
	public Future<Boolean> setTimestep(int step) {

		if (step >= 0 && step < times.size()) {
			this.targetTimestep = step;
		}

		return requestExecutor.submit(new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				boolean changed = false;
				if (!times.isEmpty()) {
					// FIXME We need a way to just go straight to the desired
					// time.
					while (timestep != targetTimestep) {
						if (timestep < targetTimestep) {
							setTimestep("next");
							timestep++;
						} else {
							setTimestep("prev");
							timestep--;
						}
						changed = true;
					}

					// If we scale by the current time, then we need to refresh
					// the scale.
					if (!scaleByAllTimes) {
						rescale();
					}
				}

				return changed;
			}
		});
	}

	/**
	 * Sends a new command to the "VCR" on the web client. This is used to
	 * update the timestep on the client.
	 * 
	 * @param action
	 *            One of "first", "prev", "next", or "last".
	 * @return A task for updating the timestep on the web client.
	 */
	private Future<JsonObject> setTimestep(String action) {
		// FIXME This changes the time for ALL views, not just this one.
		IParaViewWebClient widget = connection.getWidget();
		JsonArray array = new JsonArray();
		array.add(new JsonPrimitive(action));
		return widget.call("pv.vcr.action", array);
	}
}
