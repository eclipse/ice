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

import org.eclipse.ui.views.properties.PropertyDescriptor;

/**
 * This class provides a wrapper for a simple IPropertyDescriptor. A property
 * requires at least 3 things: an ID, a display name, and a value. It can also
 * have a category, the default value being "Misc".<br>
 * <br>
 * The ID for a SimpleProperty should be unique for a particular
 * IPropertySource.<br>
 * <br>
 * If an instance requires a more specialized IPropertyDescriptor, then the
 * method {@linkplain SimpleProperty#getPropertyDescriptor()} should be
 * overridden.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class SimpleProperty {

	/**
	 * The ID of the property. This is used to construct an IPropertyDescriptor
	 * and should be unique within the IPropertySource.
	 */
	private final String id;
	/**
	 * The text displayed for the IPropertyDescriptor. This does not need to be
	 * unique.
	 */
	private final String displayName;
	/**
	 * The category under which the property will appear. If null, the property
	 * should default to "Misc". If no categories are specified in the
	 * IPropertySource, then "Misc" will not appear.
	 */
	private final String category;
	/**
	 * The value of the property. If null, you may want to override
	 * {@linkplain SimpleProperty#getValue()}.
	 */
	private final Object value;

	/**
	 * The default constructor.
	 * 
	 * @param id
	 *            The ID of the property. This is used to construct an
	 *            IPropertyDescriptor and should be unique within the
	 *            IPropertySource.
	 * @param displayName
	 *            The text displayed for the IPropertyDescriptor. This does not
	 *            need to be unique.
	 * @param category
	 *            The category under which the property will appear. If null,
	 *            the property should default to "Misc". If no categories are
	 *            specified in the IPropertySource, then "Misc" will not appear.
	 * @param value
	 *            The value of the property. If null, you may want to override
	 *            {@linkplain SimpleProperty#getValue()}.
	 */
	public SimpleProperty(String id, String displayName, String category,
			Object value) {
		// Replace any null Strings with empty Strings.
		this.id = (id != null ? id : "");
		this.displayName = (displayName != null ? displayName : "");
		this.category = (category != null ? category : "");
		this.value = value;

		return;
	}

	/**
	 * Gets a PropertyDescriptor for the SimpleProperty.
	 * 
	 * @return A PropertyDescriptor configured with the SimpleProperty's fields.
	 */
	public PropertyDescriptor getPropertyDescriptor() {
		PropertyDescriptor property = new PropertyDescriptor(id, displayName);
		property.setCategory(category);
		return property;
	}

	/**
	 * Gets the String ID used for the SimpleProperty. It should be unique
	 * within the parent IPropertySource.
	 * 
	 * @return A String ID.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Gets the current value of the property.
	 * 
	 * @return An Object pointing to the value of the property.
	 */
	public Object getValue() {
		return value;
	}
}
