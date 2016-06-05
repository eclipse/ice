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
package org.eclipse.ice.client.common.properties;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.january.form.AdaptiveTreeComposite;
import org.eclipse.january.form.AllowedValueType;
import org.eclipse.january.form.DiscreteEntry;
import org.eclipse.january.form.FileEntry;
import org.eclipse.january.form.IEntry;

/**
 * This class provides an {@link ICellContentProvider} geared toward the value
 * of a {@link TreeProperty}.
 * <p>
 * Due to the nature of <code>Entry</code>s with discrete allowed value types
 * and {@link AdaptiveTreeComposite}s whose types can be associated with such an
 * <code>Entry</code>, there are additional classes required to support these
 * <code>Entry</code>s in a <code>ColumnViewer</code>. This class does not
 * handle any widgets required to make changing these values possible.
 * </p>
 * 
 * @see ComboCellEditingSupport
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ValueCellContentProvider extends TreePropertyCellContentProvider implements IComboCellContentProvider {

	/**
	 * Gets the value of a {@link TreeProperty}.
	 * <p>
	 * The default behavior is acceptable for most <code>Entry</code>s, but for
	 * those that correspond to the type of {@link AdaptiveTreeComposite}, this
	 * method gets the type of the property's tree.
	 * </p>
	 */
	@Override
	public Object getValue(Object element) {
		Object value = null;

		if (isValid(element)) {
			TreeProperty property = (TreeProperty) element;

			// For the "type" property, return the containing tree's type.
			if (property.isAdaptiveType()) {
				value = ((AdaptiveTreeComposite) property.getTree()).getType();
			}
			// Otherwise, return the property's value (discrete or not!).
			else {
				value = super.getValue(element);
			}
		}

		return value;
	}

	/**
	 * Sets the value of a {@link TreeProperty}.
	 * <p>
	 * The default behavior is acceptable for most <code>Entry</code>s, but for
	 * those that correspond to the type of {@link AdaptiveTreeComposite}, this
	 * method sets the type for the property's tree.
	 * </p>
	 */
	@Override
	public boolean setValue(Object element, Object value) {
		boolean changed = false;

		if (isValid(element)) {
			TreeProperty property = (TreeProperty) element;

			// For the "type" property, set the containing tree's type.
			if (property.isAdaptiveType()) {
				String type = value.toString().trim();
				AdaptiveTreeComposite tree = (AdaptiveTreeComposite) property.getTree();
				// If the type is different, mark the flag and set the new type.
				if (changed = !type.equals(tree.getType())) {
					tree.setType(type);
				}
			}
			// Otherwise, set the property's value (discrete or not!).
			else {
				changed = super.setValue(element, value);
			}
		}

		return changed;
	}

	/**
	 * Returns true if the {@link TreeProperty} corresponds to the type of
	 * {@link AdaptiveTreeComposite} or if its wrapped {@link Entry} is flagged
	 * as {@link AllowedValueType#Discrete}, false otherwise.
	 */
	@Override
	public boolean requiresCombo(Object element) {
		boolean requiresCombo = false;

		if (isValid(element)) {
			TreeProperty property = (TreeProperty) element;
			// The property has multiple allowed values if it is the "type"
			// property (for the type of AdaptiveTreeComposite) or if the
			// wrapped Entry has a discrete allowed value type.
			requiresCombo = property.isAdaptiveType() || property.getEntry() instanceof DiscreteEntry
					|| property.getEntry() instanceof FileEntry;
		}

		return requiresCombo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ice.client.widgets.properties.IComboCellContentProvider#
	 * getAllowedValues(java.lang.Object)
	 */
	@Override
	public List<String> getAllowedValues(Object element) {
		List<String> allowedValues = null;

		if (isValid(element)) {
			TreeProperty property = (TreeProperty) element;
			IEntry entry = property.getEntry();

			// If it's a discrete Entry, get the allowed value types.
			if (entry instanceof DiscreteEntry || entry instanceof FileEntry) {
				allowedValues = entry.getAllowedValues();
			}
			// If it's the "type" property, get the adaptive tree types.
			else if (property.isAdaptiveType()) {
				allowedValues = property.getAdaptiveTypes();
			}
		}

		// Make sure we don't return a null value.
		if (allowedValues == null) {
			allowedValues = new ArrayList<String>(1);
		}

		return allowedValues;
	}

}
