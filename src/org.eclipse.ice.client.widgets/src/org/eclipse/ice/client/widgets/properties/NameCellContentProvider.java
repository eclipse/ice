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
package org.eclipse.ice.client.widgets.properties;

import org.eclipse.ice.datastructures.form.Entry;

/**
 * This class provides an {@link ICellContentProvider} geared toward the name of
 * a {@link TreeProperty}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class NameCellContentProvider extends TreePropertyCellContentProvider {

	/**
	 * Change the default edit policy so that the name of an {@link Entry} that
	 * is required cannot be changed.
	 */
	@Override
	public boolean isEnabled(Object element) {
		return isValid(element)
				&& !((TreeProperty) element).getEntry().isRequired();
	}

	/**
	 * Gets the name of the {@link TreeProperty}.
	 */
	@Override
	public Object getValue(Object element) {
		Object value = null;

		if (isValid(element)) {
			// Return the trimmed name of the property's Entry.
			value = ((TreeProperty) element).getEntry().getName().trim();
		}

		return value;
	}

	/**
	 * Sets the name of the {@link TreeProperty}.
	 */
	@Override
	public boolean setValue(Object element, Object value) {
		boolean changed = false;

		if (isValid(element) && value != null) {
			// Trim the new value to get rid of extra whitespace.
			String newValue = value.toString().trim();
			Entry entry = ((TreeProperty) element).getEntry();
			// If the name is different, mark the flag and set the new name.
			if (changed = !newValue.equals(entry.getName())) {
				entry.setName(newValue);
			}

			changed = true;
		}

		return changed;
	}

}
