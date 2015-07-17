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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public abstract class ProxyProperty {

	protected AbstractParaViewProxy proxy;

	protected final String name;
	protected final int index;
	protected final PropertyType type;

	private final Set<String> allowedValues;
	private final Set<String> values;
	private String value;

	private String propertyName;

	public enum PropertyType {
		UNDEFINED, DISCRETE, DISCRETE_MULTI;
	}

	public ProxyProperty(String name, int index) {
		this(name, index, null);
	}
	
	public ProxyProperty(String name, int index, PropertyType type) {
		proxy = null;

		this.name = name;
		this.index = index;
		this.type = (type != null ? type : PropertyType.DISCRETE);

		allowedValues = new HashSet<String>();
		values = new LinkedHashSet<String>();
	}

	public boolean setProxy(AbstractParaViewProxy proxy) {
		boolean changed = false;

		if (proxy != this.proxy) {
			changed = true;

			this.proxy = proxy;

			// Reset everything.
			propertyName = null;
			allowedValues.clear();
			values.clear();
			value = null;

			if (proxy != null) {
				IParaViewWebClient client = proxy.getConnection().getWidget();

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

	protected abstract int getProxyId();

	protected List<String> findAllowedValues(IParaViewWebClient client) {
		List<String> allowedValues = null;

		// Get the proxy object (file, view, or rep JsonObject).
		JsonObject proxyObj = proxy.getProxyObject(getProxyId());
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

	protected String findPropertyName(IParaViewWebClient client) {
		String name = null;

		// Get the proxy object (file, view, or rep JsonObject).
		JsonObject proxyObj = proxy.getProxyObject(getProxyId());
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

	protected String findValue(IParaViewWebClient client) {
		String value = null;

		// Get the proxy object (file, view, or rep JsonObject).
		JsonObject proxyObj = proxy.getProxyObject(getProxyId());
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

	protected List<String> findValues(IParaViewWebClient client) {
		List<String> values = null;

		// Get the proxy object (file, view, or rep JsonObject).
		JsonObject proxyObj = proxy.getProxyObject(getProxyId());
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

	public Set<String> getAllowedValues() {
		return new TreeSet<String>(allowedValues);
	}

	public String getValue() {
		return value;
	}

	public List<String> getValues() {
		return new ArrayList<String>(values);
	}

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

		if (changed) {
			applyChanges();
		}

		return changed;
	}

	private boolean stringsEqual(String one, String two) {
		return (one != null && one.equals(two)) || (one == null && two == null);
	}

	public boolean setValues(List<String> values)
			throws NullPointerException, UnsupportedOperationException {
		// Check the type of property and the parameter.
		if (type != PropertyType.DISCRETE_MULTI) {
			throw new UnsupportedOperationException("ParaViewProxy error: "
					+ "The property \"" + name
					+ "\" does not allow multiple values to be selected.");
		} else if (values == null) {
			throw new NullPointerException("ParaViewProxy error: "
					+ "Null list of selected values is not allowed.");
		}

		boolean changed = false;

		Set<String> oldValues = new HashSet<String>(this.values);
		for (String value : values) {
			changed |= !oldValues.remove(value);
			this.values.add(value);
		}

		if (!oldValues.isEmpty()) {
			changed = true;
			for (String oldValue : oldValues) {
				this.values.remove(oldValue);
			}
		}

		if (changed) {
			applyChanges();
		}

		return changed;
	}

	public boolean valueAllowed(String value) {
		return type == PropertyType.UNDEFINED ? validateValue(value)
				: allowedValues.contains(value);
	}

	protected boolean validateValue(String value) {
		return true;
	}

	protected boolean applyChanges() {
		boolean updated = false;

		IParaViewWebClient widget = proxy.getConnection().getWidget();

		JsonArray args = new JsonArray();
		JsonArray updatedProperties = new JsonArray();
		JsonObject repProperty = new JsonObject();
		repProperty.addProperty("id", Integer.toString(getProxyId()));
		repProperty.addProperty("name", propertyName);
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

		// Update the properties that were configured.
		args = new JsonArray();
		args.add(updatedProperties);
		JsonObject response;
		try {
			response = widget.call("pv.proxy.manager.update", args).get();
			updated = response.get("success").getAsBoolean();
			if (!updated) {
				System.out.println(
						"Failed to change the property \"" + name + "\": ");
				JsonArray array = response.get("errorList").getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					System.out.println(array.get(i));
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

}
