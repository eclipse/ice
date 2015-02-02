/*******************************************************************************
 * Copyright (c) 2015- UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Jordan Deyton
 *******************************************************************************/
package org.eclipse.ice.viz.service.preferences;

import org.eclipse.ice.client.widgets.properties.ICellContentProvider;
import org.eclipse.ice.datastructures.form.Entry;
import org.eclipse.swt.graphics.Image;

/**
 * This class provides a basic JFace content provider that wraps an ICE
 * {@link Entry}. The text and value for an {@code Entry} are considered the
 * same thing, while the tooltip is the {@code Entry}'s description.
 * 
 * @author Jordan Deyton
 *
 */
public class EntryCellContentProvider implements ICellContentProvider {

	/**
	 * The text to display when a cell's element (expected to be an
	 * {@link Entry}) is invalid.
	 */
	protected static final String INVALID_ELEMENT_TEXT = "Invalid Entry.";

	/**
	 * By default, any {@link Entry} that is not null is valid.
	 */
	@Override
	public boolean isValid(Object element) {
		return element != null && element instanceof Entry;
	}

	/**
	 * By default, any valid {@link Entry} is enabled.
	 */
	@Override
	public boolean isEnabled(Object element) {
		return isValid(element);
	}

	/**
	 * Returns the value, which is converted to a string, of the {@link Entry}.
	 */
	@Override
	public String getText(Object element) {

		Object value;
		String text = INVALID_ELEMENT_TEXT;

		if (isValid(element)) {
			value = getValue(element);
			text = (value != null ? value.toString() : "");
		}

		return text;
	}

	/**
	 * Returns the description of the {@link Entry}.
	 */
	@Override
	public String getToolTipText(Object element) {

		String text = INVALID_ELEMENT_TEXT;

		if (isValid(element)) {
			text = ((Entry) element).getDescription();
		}

		return text;
	}

	/**
	 * Returns {@code null} for no {@code Image}.
	 */
	@Override
	public Image getImage(Object element) {
		return null;
	}

	/**
	 * Returns the {@link Entry}'s value.
	 */
	@Override
	public Object getValue(Object element) {

		Object value = null;

		if (isValid(element)) {
			value = ((Entry) element).getValue();
		}

		return value;
	}

	/**
	 * Converts the value to a string and sets it as the {@link Entry}'s value.
	 */
	@Override
	public boolean setValue(Object element, Object value) {

		boolean changed = false;

		if (isValid(element) && value != null) {
			String newValue = value.toString();
			changed = ((Entry) element).setValue(newValue);
		}

		return changed;
	}

}
