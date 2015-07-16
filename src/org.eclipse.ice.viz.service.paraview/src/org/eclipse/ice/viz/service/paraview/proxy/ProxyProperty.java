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

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import org.eclipse.ice.viz.service.paraview.web.IParaViewWebClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public abstract class ProxyProperty {

	protected AbstractParaViewProxy proxy;

	protected final int index;
	protected final String uiName;
	protected final String propertyName;

	private final Set<String> allowedValues;
	private String value;

	public ProxyProperty(int index, String uiName, String propertyName) {
		proxy = null;

		this.index = index;
		this.uiName = uiName;
		this.propertyName = propertyName;

		allowedValues = new HashSet<String>();
		value = null;
	}

	protected Set<String> findAllowedValues(IParaViewWebClient client) {
		Set<String> allowedValues = new HashSet<String>();

		// Get the proxy object (file, view, or rep JsonObject).
		JsonObject proxyObj = proxy.getProxyObject(getPropertyProxyId());
		try {
			// Get the corresponding entry in the "ui" JsonArray.
			JsonArray ui = proxyObj.get("ui").getAsJsonArray();
			JsonObject entry = ui.get(index).getAsJsonObject();
			// Add all values from its "values" array to the set.
			JsonArray array = entry.get("values").getAsJsonArray();
			for (int i = 0; i < array.size(); i++) {
				allowedValues.add(array.get(i).getAsString());
			}
		} catch (NullPointerException | ClassCastException
				| IllegalStateException e) {
			e.printStackTrace();
		}

		return allowedValues;
	}

	protected String findValue(IParaViewWebClient client) {
		String value = null;

		// Get the proxy object (file, view, or rep JsonObject).
		JsonObject proxyObj = proxy.getProxyObject(getPropertyProxyId());
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

	public Set<String> getAllowedValues() {
		return new TreeSet<String>(allowedValues);
	}

	/**
	 * Gets the ID for the associated (underlying) proxy object on the web
	 * client. For instance, this may be the file, view, or representation
	 * proxy.
	 * 
	 * @return The ID. It should be one of the associated {@link #proxy}'s three
	 *         IDs.
	 */
	protected int getPropertyProxyId() {
		return proxy.getFileId();
	}

	public String getValue() {
		return value;
	}

	public boolean selectValue(String value) {
		boolean changed = false;
		String oldValue = this.value;
		if (value != null && !value.equals(oldValue)) {
			this.value = value;
			if (applyChanges()) {
				changed = true;
			} else {
				this.value = oldValue;
			}
		}
		return changed;
	}

	public boolean unselectValue(String value) {
		boolean changed = false;
		String oldValue = this.value;
		if (value != null && value.equals(oldValue)) {
			this.value = null;
			if (applyChanges()) {
				changed = true;
			} else {
				this.value = oldValue;
			}
		}
		return changed;
	}

	public boolean setProxy(AbstractParaViewProxy proxy) {
		boolean changed = false;

		if (proxy != this.proxy) {
			changed = true;

			this.proxy = proxy;

			// Reset everything.
			allowedValues.clear();
			value = null;

			if (proxy != null) {
				IParaViewWebClient client = proxy.getConnection().getWidget();

				// Reset the allowed values.
				allowedValues.addAll(findAllowedValues(client));
				// Reset the value.
				value = findValue(client);
			}
		}

		return changed;
	}

	public boolean valueAllowed(String value) {
		return allowedValues.contains(value);
	}

	protected boolean applyChanges() {
		boolean updated = false;

		IParaViewWebClient widget = proxy.getConnection().getWidget();

		JsonArray args = new JsonArray();
		JsonArray updatedProperties = new JsonArray();
		JsonObject repProperty = new JsonObject();
		repProperty.addProperty("id", Integer.toString(getPropertyProxyId()));
		repProperty.addProperty("name", propertyName);
		repProperty.addProperty("value", value);
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
						"Failed to change the property \"" + uiName + "\": ");
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
