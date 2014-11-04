/*******************************************************************************
 * Copyright (c) 2014 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - Jay Jay Billings,
 *   Jordan H. Deyton, Dasha Gorin, Alexander J. McCaskey, Taylor Patterson,
 *   Claire Saunders, Matthew Wang, Anna Wojtowicz
 *******************************************************************************/
package org.eclipse.ice.client.widgets.reactoreditor.properties;

import org.eclipse.ice.datastructures.componentVisitor.IComponentVisitor;
import org.eclipse.ice.datastructures.componentVisitor.IReactorComponent;
import org.eclipse.ice.datastructures.updateableComposite.IUpdateableListener;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * This class provides the properties of an SFRComponent to be displayed in the
 * ICE Properties View. It displays all of the properties of an SFRComponent.<br>
 * <br>
 * Extensions to this class should override add their properties to
 * {@linkplain LWRComponentPropertySource#properties}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ReactorComponentPropertySource implements IPropertySource {

	/**
	 * A Map of {@linkplain SimpleProperty} instances for the selected
	 * component.
	 */
	protected Map<String, SimpleProperty> properties;

	/**
	 * The default constructor.
	 * 
	 * @param component
	 *            The component whose properties are to be displayed in the ICE
	 *            Properties View.
	 */
	public ReactorComponentPropertySource(IReactorComponent component) {
		// Set a default, blank component if the argument is null.
		if (component == null) {
			component = new IReactorComponent() {
				// Give the properties something to signify that the component
				// is
				// invalid.
				public String getName() {
					return "Invalid reactor component!";
				}

				public String getDescription() {
					return "This is an invalid reactor component!";
				}

				public int getId() {
					return 1;
				}

				public void accept(IComponentVisitor visitor) {
				}

				public void update(String updatedKey, String newValue) {
				}

				public void register(IUpdateableListener listener) {
				}

				public void unregister(IUpdateableListener listener) {
				}

				public void setId(int id) {
				}

				public void setName(String name) {
				}

				public void setDescription(String description) {
				}

				// This is required to avoid a CloneNotSupported exception.
				public Object clone() {
					return null;
				}
			};
		}

		// Initialize the map of properties.
		properties = new HashMap<String, SimpleProperty>();

		// Set the basic properties for IReactorComponents.
		String category = "Basic Properties";
		addProperty(new SimpleProperty("name", "Name", category,
				component.getName()));
		addProperty(new SimpleProperty("description", "Description", category,
				component.getDescription()));
		addProperty(new SimpleProperty("id", "ID", category, component.getId()));

		return;
	}

	/**
	 * Adds a SimpleProperty to the properties exposed by this IPropertySource.
	 * 
	 * @param property
	 *            A SimpleProperty instance to add.
	 * @return True if the property is not null and its ID did not already exist
	 *         in the map, false otherwise.
	 */
	public boolean addProperty(SimpleProperty property) {

		// By default, the addition failed.
		boolean success = false;

		// Only attempt to add the property if the property is not null and its
		// ID is not already in the Map.
		if (property != null && !properties.containsKey(property.getId())) {
			properties.put(property.getId(), property);
			success = true;
		}

		return success;
	}

	/* ---- Implements IPropertySource. ---- */
	/**
	 * Returns null. Properties for IReactorComponents are currently read-only!
	 */
	public Object getEditableValue() {
		// Read-only!
		return null;
	}

	/**
	 * Returns the IPropertyDescriptors for IReactorComponents.
	 */
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// Create a new Array.
		IPropertyDescriptor[] descriptors = new IPropertyDescriptor[properties
				.size()];

		// Add a PropertyDescriptor for each of the simple properties.
		int i = 0;
		for (SimpleProperty property : properties.values()) {
			descriptors[i++] = property.getPropertyDescriptor();
		}

		return descriptors;
	}

	/**
	 * Returns the value for a particular IReactorComponent property or null if
	 * it is an invalid property.
	 */
	public Object getPropertyValue(Object id) {

		// Set the default return value.
		Object value = null;

		// See if the id matches any of the supported property IDs.
		SimpleProperty property = properties.get(id);
		if (property != null) {
			value = property.getValue();
		}
		return value;
	}

	/**
	 * Returns false. Properties for IReactorComponents are currently read-only!
	 */
	public boolean isPropertySet(Object id) {
		// Read-only!
		return false;
	}

	/**
	 * Does nothing. Properties for IReactorComponents are currently read-only!
	 */
	public void resetPropertyValue(Object id) {
		// Read-only!
		return;
	}

	/**
	 * Does nothing. Properties for IReactorComponents are currently read-only!
	 */
	public void setPropertyValue(Object id, Object value) {
		// Read-only!
		return;
	}
	/* ------------------------------------- */
}
