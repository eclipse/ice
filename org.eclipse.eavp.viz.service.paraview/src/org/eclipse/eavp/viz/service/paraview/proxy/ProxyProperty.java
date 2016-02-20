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
package org.eclipse.eavp.viz.service.paraview.proxy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import org.eclipse.eavp.viz.service.connections.ConnectionState;
import org.eclipse.eavp.viz.service.connections.IVizConnection;
import org.eclipse.eavp.viz.service.paraview.web.IParaViewWebClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * This class provides basic functionality for accessing and modifying
 * properties for a ParaView proxy (which is returned as a JsonObject when
 * calling "pv.proxy.manager.get").
 * <p>
 * Before a property can be used, its {@link AbstractParaViewProxy} must be set
 * by calling {@link #setProxy(AbstractParaViewProxy)}.
 * </p>
 * <p>
 * This class helps deal with the structures in the ParaView web client's
 * proxies. In general, a file, view, or representation proxy adheres to the
 * format below:
 * </p>
 * <ol>
 * <li><b>data - {@code JsonArray} -</b> Contains some useful metadata,
 * including:
 * <ul>
 * <li><b>"time"</b> - {@code JsonArray} of doubles - The timesteps in the file.
 * </li>
 * </ul>
 * </li>
 * <li><b>ui - {@code JsonArray} -</b> Contains metadata about properties, which
 * can be used to construct a UI for changing said properties. For example, each
 * element may include:
 * <ul>
 * <li><b>"name"</b> - The name of the property.</li>
 * <li><b>"widget"</b> - One of {@code { list-1, list-n, checkbox, textfield }}.
 * </li>
 * <li><b>"type"</b> - The primitive data type for the values (e.g., "str" or
 * "float").</li>
 * <li><b>"values"</b> - If the "widget" is {@code list-1} or {@code list-n},
 * this is an array of the allowed values.</li>
 * <li>(and more...)</li>
 * </ul>
 * </li>
 * <li><b>properties - {@code JsonArray} -</b> Contains the current value(s) for
 * the properties. This is indexed exactly the same as the "ui" array. Each
 * element may include the following properties:
 * <ul>
 * <li><b>"name"</b> - The name of the property, used for lookup. This is
 * usually different from the "name" in the "ui" array.</li>
 * <li><b>"value"</b> - If the corresponding "widget" is {@code list-n}, this is
 * an array of the selected values. Otherwise, it is the current value.</li>
 * <li>(and more...)</li>
 * <ul>
 * </li>
 * </ol>
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public abstract class ProxyProperty {

	/**
	 * This enumeration is used to determine how certain properties should be
	 * treated.
	 * 
	 * @author Jordan Deyton
	 *
	 */
	public enum PropertyType {
		/**
		 * There is generally no restriction on the property other than limits
		 * imposed by {@link ProxyProperty#validateValue(String)}.
		 */
		UNDEFINED,

		/**
		 * The selected value must be exactly one of the allowed values.
		 */
		DISCRETE,

		/**
		 * Multiple allowed values may be selected.
		 */
		DISCRETE_MULTI;
	}

	/**
	 * Logger for handling event messages and other information.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(ProxyProperty.class);

	/**
	 * The associated connection.
	 */
	private IVizConnection<IParaViewWebClient> connection;
	/**
	 * The name of the property. This corresponds to the "name" value in the
	 * corresponding array element in the "ui" array.
	 */
	public final String name;
	/**
	 * The index of the property in its parent proxy's "ui" and "properties"
	 * JsonArrays.
	 */
	public final int index;

	/**
	 * The type of property. This dictates the format of the "values" and
	 * "value" JsonElements in its "ui" and "properties" entries, respectively.
	 */
	protected final PropertyType type;
	/**
	 * The allowed values. This is used for properties whose types are
	 * {@link PropertyType#DISCRETE} or {@link PropertyType#DISCRETE_MULTI}.
	 */
	private final Set<String> allowedValues;
	/**
	 * The selected values. This is used for properties whose type is
	 * {@link PropertyType#DISCRETE_MULTI}.
	 */
	private final Set<String> values;

	/**
	 * The selected value. This is used for properties whose type is
	 * <b><i>not</i></b> {@link PropertyType#DISCRETE_MULTI}.
	 */
	private String value;

	/**
	 * The name of the property in the "properties" JsonArray.
	 */
	private String propertyName;

	/**
	 * The default constructor.
	 * 
	 * @param name
	 *            The name of the property. This corresponds to the "name" value
	 *            in the corresponding array element in the "ui" array.
	 * @param index
	 *            The index of the property in its parent proxy's "ui" and
	 *            "properties" JsonArrays.
	 */
	public ProxyProperty(String name, int index) {
		this(name, index, null);
	}

	/**
	 * The full constructor.
	 * 
	 * @param name
	 *            The name of the property. This corresponds to the "name" value
	 *            in the corresponding array element in the "ui" array.
	 * @param index
	 *            The index of the property in its parent proxy's "ui" and
	 *            "properties" JsonArrays.
	 * @param type
	 *            The type of property. This dictates the format of the "values"
	 *            and "value" JsonElements in its "ui" and "properties" entries,
	 *            respectively.
	 */
	public ProxyProperty(String name, int index, PropertyType type) {
		connection = null;

		this.name = name;
		this.index = index;
		this.type = (type != null ? type : PropertyType.DISCRETE);

		// Initialize the hash maps.
		allowedValues = new HashSet<String>();
		values = new LinkedHashSet<String>();

		return;
	}

	/**
	 * Applies the current selection to the web client.
	 * 
	 * @return True if the client successfully processed the changes, false
	 *         otherwise.
	 */
	protected boolean applyChanges() {
		boolean updated = false;

		/*-
		 * To do this, we need to call "pv.proxy.manager.update" where the
		 * argument (an array) contains an array of updated properties.
		 * 
		 * Each property update element in the array must provide the following:
		 *   "id" - the ID of the proxy object (file, view, or representation)
		 *   "name" - the name of the property in the "properties" section
		 *   "value" - a string containing the value or a JsonArray of selected 
		 *      values if the widget type is "list-n"  
		 */

		IParaViewWebClient widget = connection.getWidget();

		// Set up the basic arguments for "id" and "name".
		JsonArray updatedProperties = new JsonArray();
		JsonObject repProperty = new JsonObject();
		repProperty.addProperty("id", Integer.toString(getProxyId()));
		repProperty.addProperty("name", propertyName);

		// Determine the "value" property to be a string or a JsonArray.
		if (type != PropertyType.DISCRETE_MULTI) {
			repProperty.addProperty("value", value);
		} else {
			JsonArray valueArray = new JsonArray();
			for (String value : values) {
				valueArray.add(new JsonPrimitive(value));
			}
			repProperty.add("value", valueArray);
		}
		updatedProperties.add(repProperty);

		// Send the request to the client.
		JsonArray args = new JsonArray();
		args.add(updatedProperties);
		JsonObject response;
		try {
			response = widget.call("pv.proxy.manager.update", args).get();
			// Get the response.
			updated = response.get("success").getAsBoolean();
			if (!updated) {
				logger.debug(
						"Failed to change the property \"" + name + "\": ");
				JsonArray array = response.get("errorList").getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					logger.debug(array.get(i).toString());
				}
			}
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			e.printStackTrace();
		}

		return updated;
	}

	/**
	 * Finds the allowed values from the "ui" array of the proxy object.
	 * 
	 * @param client
	 *            The client used to fetch the allowed values.
	 * @return A list of the allowed values from the corresponding element in
	 *         the "ui" array.
	 */
	protected List<String> findAllowedValues(IParaViewWebClient client) {
		List<String> allowedValues = null;

		// Get the proxy object (file, view, or rep JsonObject).
		JsonObject proxyObj = getProxyObject();
		try {
			// Get the corresponding entry in the "ui" JsonArray.
			JsonArray ui = proxyObj.get("ui").getAsJsonArray();
			JsonObject entry = ui.get(index).getAsJsonObject();
			// Add all values from its "values" array to the set.
			JsonArray array = entry.get("values").getAsJsonArray();
			int size = array.size();
			allowedValues = new ArrayList<String>(size);
			for (int i = 0; i < array.size(); i++) {
				allowedValues.add(array.get(i).getAsString());
			}
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			e.printStackTrace();
		}

		return allowedValues != null ? allowedValues : new ArrayList<String>(0);
	}

	/**
	 * Finds the name of the property in the "properties" array of the proxy
	 * object.
	 * 
	 * @param client
	 *            The client used to fetch the allowed values.
	 * @return The name of the corresponding element in the "properties" array.
	 */
	protected String findPropertyName(IParaViewWebClient client) {
		String name = null;

		// Get the proxy object (file, view, or rep JsonObject).
		JsonObject proxyObj = getProxyObject();
		try {
			// Get the corresponding entry in the "properties" JsonArray.
			JsonArray properties = proxyObj.get("properties").getAsJsonArray();
			JsonObject entry = properties.get(index).getAsJsonObject();
			// Get its name.
			name = entry.get("name").getAsString();
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			e.printStackTrace();
		}

		return name;
	}

	/**
	 * Finds the current value of the property from the "properties" array of
	 * the proxy object. <i>This is only called if the {@link #type} is </i>not
	 * <i> {@link PropertyType#DISCRETE_MULTI}.</i>
	 * 
	 * @param client
	 *            The client used to fetch the allowed values.
	 * @return The value of the corresponding element in the "properties" array.
	 */
	protected String findValue(IParaViewWebClient client) {
		String value = null;

		// Get the proxy object (file, view, or rep JsonObject).
		JsonObject proxyObj = getProxyObject();
		try {
			// Get the corresponding entry in the "properties" JsonArray.
			JsonArray properties = proxyObj.get("properties").getAsJsonArray();
			JsonObject entry = properties.get(index).getAsJsonObject();
			// Get its value.
			value = entry.get("value").getAsString();
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			e.printStackTrace();
		}

		return value;
	}

	/**
	 * Finds the current values of the property from the "properties" array of
	 * the proxy object. <i>This is only called if the {@link #type} </i>is
	 * <i> {@link PropertyType#DISCRETE_MULTI}.</i>
	 * 
	 * @param client
	 *            The client used to fetch the allowed values.
	 * @return The value of the corresponding element in the "properties" array.
	 */
	protected List<String> findValues(IParaViewWebClient client) {
		List<String> values = null;

		// Get the proxy object (file, view, or rep JsonObject).
		JsonObject proxyObj = getProxyObject();
		try {
			// Get the corresponding entry in the "properties" JsonArray.
			JsonArray properties = proxyObj.get("properties").getAsJsonArray();
			JsonObject entry = properties.get(index).getAsJsonObject();
			// Add all values from its "value" array to the set.
			JsonArray array = entry.get("value").getAsJsonArray();
			int size = array.size();
			values = new ArrayList<String>(size);
			for (int i = 0; i < array.size(); i++) {
				values.add(array.get(i).getAsString());
			}
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			e.printStackTrace();
		}

		return values != null ? values : new ArrayList<String>(0);
	}

	/**
	 * Gets the allowed values for the property.
	 * 
	 * @return A set containing all allowed values for the property.
	 */
	public Set<String> getAllowedValues() {
		return new TreeSet<String>(allowedValues);
	}

	/**
	 * Gets the ID of the array under which the property's "ui" and "properties"
	 * arrays are found. This should be one of the file, view, or representation
	 * proxy IDs.
	 * 
	 * @return The ID of the real ParaView proxy on the remote web client.
	 */
	protected abstract int getProxyId();

	/**
	 * Gets the proxy object from the client whose ID matches the value returned
	 * by {@link #getProxyId()}.
	 * 
	 * @return The proxy JsonObject, or {@code null} if it could not be queried
	 *         from the {@link #connection}.
	 */
	protected JsonObject getProxyObject() {
		JsonObject object = null;
		JsonArray args = new JsonArray();
		args.add(new JsonPrimitive(getProxyId()));
		try {
			object = connection.getWidget().call("pv.proxy.manager.get", args)
					.get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		return object;
	}

	/**
	 * Gets the current value for the property.
	 * 
	 * @return The current value of the property, or {@code null} if the
	 *         property is unset or has the type
	 *         {@link PropertyType#DISCRETE_MULTI}.
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Gets the currently selected values for the property.
	 * 
	 * @return The currently selected values for the property if the property
	 *         has the type {@link PropertyType#DISCRETE_MULTI}, or an empty
	 *         collection otherwise.
	 */
	public List<String> getValues() {
		return new ArrayList<String>(values);
	}

	/**
	 * Sets the connection used for this property. This synchronizes this data
	 * structure with the "ui" and "properties" arrays for the remote proxy
	 * object with the ID specified by {@link #getProxyId()}.
	 * 
	 * @param connection
	 *            The new connection. If {@code null} or not connected, the
	 *            property's data will be cleared.
	 * @return True if the connection changed, false otherwise.
	 */
	public boolean setConnection(
			IVizConnection<IParaViewWebClient> connection) {
		boolean changed = false;

		if (connection != this.connection) {
			changed = true;

			this.connection = connection;

			// Reset everything.
			propertyName = null;
			allowedValues.clear();
			values.clear();
			value = null;

			if (connection != null
					&& connection.getState() == ConnectionState.Connected) {
				IParaViewWebClient client = connection.getWidget();

				// Find the name of the property in the "properties" object.
				propertyName = findPropertyName(client);

				// Reset the allowed values.
				allowedValues.addAll(findAllowedValues(client));
				if (type != PropertyType.DISCRETE_MULTI) {
					value = findValue(client);
				} else {
					values.addAll(findValues(client));
				}
			}

		}

		return changed;
	}

	/**
	 * Sets the value to the specified value.
	 * <p>
	 * If the property type is {@link PropertyType#DISCRETE_MULTI}, then the
	 * selected values will be changed to the provided value.
	 * </p>
	 * 
	 * @param value
	 *            The new value of the property.
	 * @return True if the value of the property changed.
	 */
	public boolean setValue(String value) {
		boolean changed = false;
		// If the type is undefined, any *different* value is allowed.
		if (type == PropertyType.UNDEFINED) {
			if (!stringsEqual(value, this.value) && validateValue(value)) {
				this.value = value;
				changed = true;
			}
		}
		// Otherwise, the type is restricted by a set of allowed values.
		else if (allowedValues.contains(value)) {
			// Discrete options only allow one of the allowed values to be
			// selected, so use the "value" field.
			if (type == PropertyType.DISCRETE) {
				if (!stringsEqual(value, this.value)) {
					this.value = value;
					changed = true;
				}
			}
			// Discrete (multi) options allow multiple allowed values to be
			// selected, so use the "values" set.
			else if (values.size() != 1
					|| !values.iterator().next().equals(value)) {
				values.clear();
				values.add(value);
				changed = true;
			}
		}

		// Apply the changes to the client.
		if (changed) {
			applyChanges();
		}

		return changed;
	}

	/**
	 * Sets the selected values for the property. Duplicates and bad values are
	 * ignored.
	 * <p>
	 * <b>Note:</b> This method only applies to properties of the type
	 * {@link PropertyType#DISCRETE_MULTI}.
	 * </p>
	 * 
	 * @param values
	 *            The new selected values. May be an empty list.
	 * @return True if the property changed, false otherwise.
	 */
	public boolean setValues(List<String> values)
			throws NullPointerException, UnsupportedOperationException {
		// Check the type of property and the parameter.
		if (values == null || type != PropertyType.DISCRETE_MULTI) {
			return false;
		}

		boolean changed = false;

		Set<String> oldValues = new HashSet<String>(this.values);

		// Add all new valid values. Remove any added values from the temporary
		// set.
		for (String value : values) {
			if (allowedValues.contains(value)) {
				changed |= !oldValues.remove(value);
				this.values.add(value);
			}
		}

		// Remove all old values that are not in the specified list.
		if (!oldValues.isEmpty()) {
			changed = true;
			for (String oldValue : oldValues) {
				this.values.remove(oldValue);
			}
		}

		// Apply any changes to the web client.
		if (changed) {
			applyChanges();
		}

		return changed;
	}

	/**
	 * A utility method for checking if two possibly {@code null} strings are
	 * equals.
	 * 
	 * @param one
	 *            The first string.
	 * @param two
	 *            The second string.
	 * @return True if the strings are equivalent (including if both are null),
	 *         false otherwise.
	 */
	private boolean stringsEqual(String one, String two) {
		return (one != null && one.equals(two)) || (one == null && two == null);
	}

	/**
	 * Used by sub-classes to provide custom validation of property values. This
	 * method is called from {@link #valueAllowed(String)} when the property
	 * type is {@link PropertyType#UNDEFINED}. The default behavior returns
	 * true.
	 * 
	 * @param value
	 *            The value to validate.
	 * @return True if the value is valid, false otherwise.
	 */
	protected boolean validateValue(String value) {
		return true;
	}

	/**
	 * Determines whether the specified value is allowed.
	 * 
	 * @param value
	 *            The value to test.
	 * @return True if the value is valid, false otherwise.
	 */
	public boolean valueAllowed(String value) {
		return type == PropertyType.UNDEFINED ? validateValue(value)
				: allowedValues.contains(value);
	}
}
