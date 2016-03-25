/*******************************************************************************
 * Copyright (c) 2014- UT-Battelle, LLC.
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

import org.eclipse.swt.graphics.Image;

/**
 * This class provides a basic implementation of {@link ICellContentProvider}
 * that matches a table cell with a {@link TreeProperty}.
 * 
 * @author Jordan H. Deyton
 * 
 */
public class TreePropertyCellContentProvider implements ICellContentProvider {

	/**
	 * The text to display when a {@link TreeProperty} is invalid.
	 */
	protected static final String INVALID_ELEMENT_TEXT = "Invalid parameter.";

	/**
	 * The text to display when no value is specified. The default value is an
	 * empty string, but this can be changed by using the second constructor,
	 * {@link #TreePropertyCellContentProvider(String)}.
	 */
	protected final String emptyText;

	/**
	 * The default constructor. {@link #emptyText} is set to an empty string.
	 */
	public TreePropertyCellContentProvider() {
		this("");
	}

	/**
	 * Use this constructor to set the {@link #emptyText}.
	 * 
	 * @param emptyText
	 *            The text to display when no value is specified.
	 */
	protected TreePropertyCellContentProvider(String emptyText) {
		this.emptyText = (emptyText != null ? emptyText : "");
	}

	/**
	 * By default, any non-null {@link TreeProperty} is valid.
	 */
	@Override
	public boolean isValid(Object element) {
		return element != null && element instanceof TreeProperty;
	}

	/**
	 * By default, any valid {@link TreeProperty} that is not read-only is
	 * enabled.
	 */
	@Override
	public boolean isEnabled(Object element) {
		return isValid(element) && !((TreeProperty) element).isReadOnly();
	}

	/**
	 * Gets the string displayed in the cell. The default behavior is:
	 * <ul>
	 * <li>If the element's value is not null and not empty, the value is
	 * displayed.</li>
	 * <li>If the element's value is not null and empty, {@link #emptyText} is
	 * displayed.</li>
	 * <li>If the element is invalid or otherwise the value is null,
	 * {@link #INVALID_ELEMENT_TEXT} is displayed.</li>
	 * </ul>
	 */
	@Override
	public String getText(Object element) {

		Object value = this.getValue(element);
		String text = INVALID_ELEMENT_TEXT;

		if (value != null) {
			text = value.toString().trim();
			if (text.isEmpty()) {
				text = emptyText;
			}
		}

		return text;
	}

	/**
	 * The default behavior is to return the same value displayed by
	 * {@link #getText(Object)}.
	 */
	@Override
	public String getToolTipText(Object element) {
		return getDescription(element);
	}

	/**
	 * Returns <code>null</code> for no <code>Image</code>.
	 */
	@Override
	public Image getImage(Object element) {
		return null;
	}

	/**
	 * By default, returns the underlying {@link Entry}'s value as a string,
	 * trimmed.
	 */
	@Override
	public Object getValue(Object element) {
		Object value = null;

		if (isValid(element)) {
			value = ((TreeProperty) element).getEntry().getValue().trim();
		}

		return value;
	}

	/**
	 * By default, converts the value to a string and sets it as the underlying
	 * {@link Entry}'s value.
	 */
	@Override
	public boolean setValue(Object element, Object value) {
		boolean changed = false;

		if (isValid(element) && value != null) {
			String newValue = value.toString().trim();
			changed = ((TreeProperty) element).getEntry().setValue(newValue);
		}

		return changed;
	}
	
	/**
	 * By default, returns the underlying {@link Entry}'s description as a
	 * string, trimmed.
	 */
	public String getDescription(Object element) {
		String desc = null;

		if (isValid(element)) {
			desc = ((TreeProperty) element).getEntry().getDescription().trim();
		}

		return desc;
	}
}
