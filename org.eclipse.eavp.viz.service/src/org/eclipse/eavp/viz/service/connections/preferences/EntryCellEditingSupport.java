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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

/**
 * This class provides editing capabilities for JFace viewers where the input
 * element is expected to be an {@link Entry}.
 * 
 * @author Jordan Deyton
 *
 */
public class EntryCellEditingSupport extends EditingSupport {

	/**
	 * The content provider. The methods required as an
	 * <code>EditingSupport</code> are passed to this content provider.
	 */
	protected final EntryCellContentProvider contentProvider;

	/**
	 * A <code>CellEditor</code> built around a <code>Text</code> widget.
	 */
	protected final TextCellEditor textCell;

	/**
	 * A <code>CellEditor</code> built around a <code>Combo</code> widget. This
	 * is used to restrict displayed values to a set of allowed values.
	 */
	private final ComboBoxCellEditor comboCell;
	/**
	 * A Map used to quickly look up an index of an element's value in its list
	 * of allowed values. This is used when the {@link #contentProvider}
	 * requires a <code>Combo</code>.
	 */
	private final Map<String, Integer> valueMap;

	// TODO Handle file entries...

	/**
	 * The default constructor.
	 * 
	 * @param viewer
	 *            The viewer that is using this <code>EditingSupport</code>.
	 *            <code>Control</code>s required by this class will be
	 *            constructed under this viewer's <code>Control</code> (usually
	 *            a <code>Table</code>).
	 * @param contentProvider
	 *            The content provider. The methods required as an
	 *            <code>EditingSupport</code> are passed to this content
	 *            provider.
	 */
	public EntryCellEditingSupport(ColumnViewer viewer, EntryCellContentProvider contentProvider) {
		super(viewer);

		this.contentProvider = contentProvider;

		// Get the viewer's Composite so we can create the CellEditors.
		Composite parent = (Composite) viewer.getControl();

		// Create the TextCellEditor.
		textCell = new TextCellEditor(parent, SWT.LEFT);

		// Create the ComboBoxCellEditor.
		comboCell = new ComboBoxCellEditor(parent, new String[] {}, SWT.DROP_DOWN | SWT.READ_ONLY);
		comboCell.getControl().setBackground(parent.getBackground());
		// Create a HashMap to contain values for discrete Entry values.
		valueMap = new HashMap<String, Integer>();

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#canEdit(java.lang.Object)
	 */
	@Override
	protected boolean canEdit(Object element) {
		return contentProvider.isEnabled(element);
	}

	/**
	 * By default, this returns a {@link TextCellEditor}.
	 */
	@Override
	protected CellEditor getCellEditor(Object element) {

		// If all else fails, we should return null.
		CellEditor editor = null;

		// Determine the CellEditor to use.
		if (contentProvider.isValid(element)) {

			// If the element requires a Combo, populate the combo cell and
			// return it.
			if (contentProvider.requiresCombo(element)) {
				editor = comboCell;

				// Update the Combo's items.
				List<String> allowedValues = contentProvider.getAllowedValues(element);
				String[] items = new String[allowedValues.size()];
				comboCell.setItems(allowedValues.toArray(items));

				// Update the Map so that we can convert from the text value to
				// its
				// index in the allowed values (the Combo widget uses
				// integers!).
				valueMap.clear();
				for (int i = 0; i < items.length; i++) {
					valueMap.put(items[i], i);
				}
			}
			// Otherwise, default to the text cell.
			else {
				editor = textCell;

				// Set the echo character. If secret, use an asterisk.
				// Otherwise, clear the echo character (with a "null"
				// character).
				char echo = ISecretCellContentProvider.PUBLIC_CHAR;
				if (contentProvider.isSecret(element)) {
					echo = contentProvider.getSecretChar();
				}
				Text text = (Text) textCell.getControl();
				text.setEchoChar(echo);
			}
		}

		return editor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#getValue(java.lang.Object)
	 */
	@Override
	protected Object getValue(Object element) {
		// Get the default return value.
		Object value = contentProvider.getValue(element);

		// If a Combo is required, we must convert the String value into an
		// index in the Combo's items (use the value map).
		if (contentProvider.requiresCombo(element)) {
			value = valueMap.get(value.toString());
		}

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.EditingSupport#setValue(java.lang.Object,
	 * java.lang.Object)
	 */
	@Override
	protected void setValue(Object element, Object value) {
		// Proceed only if the element is valid.
		if (contentProvider.isValid(element)) {

			// If a Combo is required, we must convert the index value (from the
			// Combo's items) into its associated String value.
			if (contentProvider.requiresCombo(element)) {
				value = comboCell.getItems()[(Integer) value];
			}

			// Now we can set the value via the content provider.
			if (contentProvider.setValue(element, value)) {
				// Force the viewer to refresh for this specific element. This
				// means the change will be reflected in the viewer.
				getViewer().update(element, null);
			}
		}

		return;
	}
}
