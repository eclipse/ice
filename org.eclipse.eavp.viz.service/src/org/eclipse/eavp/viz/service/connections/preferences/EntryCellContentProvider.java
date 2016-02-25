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
package org.eclipse.eavp.viz.service.connections.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.eavp.viz.service.datastructures.VizAllowedValueType;
import org.eclipse.eavp.viz.service.datastructures.VizEntry;
import org.eclipse.swt.graphics.Image;

/**
 * This class provides a basic JFace content provider that wraps an ICE
 * {@link Entry}. The text and value for an {@code Entry} are considered the
 * same thing, while the tooltip is the {@code Entry}'s description.
 * 
 * @author Jordan Deyton
 *
 */
public class EntryCellContentProvider implements IVizCellContentProvider,
		IVizComboCellContentProvider, ISecretCellContentProvider {

	/**
	 * The text to display when a cell's element (expected to be an
	 * {@link Entry}) is invalid.
	 */
	protected static final String INVALID_ELEMENT_TEXT = "Invalid Entry.";

	/**
	 * Gets the {@link Entry}'s allowed values, or an empty list if it has none.
	 */
	@Override
	public List<String> getAllowedValues(Object element) {
		List<String> allowedValues;
		if (isValid(element)) {
			VizEntry entry = (VizEntry) element;
			allowedValues = new ArrayList<String>(entry.getAllowedValues());
		} else {
			allowedValues = new ArrayList<String>(1);
		}
		return allowedValues;
	}

	/**
	 * Returns {@code null} for no {@code Image}.
	 */
	@Override
	public Image getImage(Object element) {
		return null;
	}

	/**
	 * Gets the default secret character used to obscure text.
	 */
	@Override
	public char getSecretChar() {
		return ISecretCellContentProvider.SECRET_CHAR;
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
			// The default value is the empty string if the underlying Entry
			// value is null.
			text = "";
			// Get the Entry's value in string form.
			if (value != null) {
				text = value.toString();
				// If necessary, obscure the text.
				if (isSecret(element)) {
					text = text.replaceAll("(?s).", "*");
				}
			}
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
			text = ((VizEntry) element).getDescription();
		}

		return text;
	}

	/**
	 * Returns the {@link Entry}'s value.
	 */
	@Override
	public Object getValue(Object element) {

		Object value = null;

		if (isValid(element)) {
			value = ((VizEntry) element).getValue();
		}

		return value;
	}

	/**
	 * By default, any valid {@link Entry} is enabled.
	 */
	@Override
	public boolean isEnabled(Object element) {
		return isValid(element);
	}

	/**
	 * An {@link Entry} should be obscured if its "secret" flag is true.
	 */
	@Override
	public boolean isSecret(Object element) {
		return isValid(element) && ((VizEntry) element).isSecret();
	}

	/**
	 * By default, any {@link Entry} that is not null is valid.
	 */
	@Override
	public boolean isValid(Object element) {
		return element != null && element instanceof VizEntry;
	}

	/**
	 * An {@link Entry} requires a combo widget if its {@link AllowedValueType}
	 * is discrete.
	 */
	@Override
	public boolean requiresCombo(Object element) {
		return isValid(element)
				&& ((VizEntry) element).getValueType() == VizAllowedValueType.Discrete;
	}

	/**
	 * Converts the value to a string and sets it as the {@link Entry}'s value.
	 */
	@Override
	public boolean setValue(Object element, Object value) {

		boolean changed = false;

		if (isValid(element) && value != null) {
			String newValue = value.toString();
			changed = ((VizEntry) element).setValue(newValue);
		}

		return changed;
	}

}
