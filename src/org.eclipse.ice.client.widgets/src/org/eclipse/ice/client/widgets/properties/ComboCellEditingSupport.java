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

/**
 * This class provides {@link EditingSupport} for a
 * {@link ValueCellContentProvider}. This enables the value cells to be edited
 * as follows:
 * <ul>
 * <li>If the {@link #contentProvider} requires a <code>Combo</code>, then a
 * {@link ComboBoxCellEditor} is used to display and select from the available
 * values.</li>
 * <li>Otherwise, the default {@link TextCellEditor} is used to edit the
 * <code>Entry</code>'s value.</li>
 * </ul>
 * 
 * @author Jordan H. Deyton
 * 
 */
public class ComboCellEditingSupport extends TextCellEditingSupport {

	/**
	 * A <code>CellEditor</code> built around a <code>Combo</code> widget. This
	 * is used to restrict displayed values to a set of allowed values from the
	 * {@link #contentProvider}.
	 */
	private final ComboBoxCellEditor comboCell;

	/**
	 * A Map used to quickly look up an index of an element's value in its list
	 * of allowed values. This is used when the {@link #contentProvider}
	 * requires a <code>Combo</code>.
	 */
	private final Map<String, Integer> valueMap;

	/**
	 * Overrides the default {@link ICellContentProvider} to gain access to the
	 * additional methods provided by {@link IComboCellContentProvider}.
	 */
	private final IComboCellContentProvider contentProvider;

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
	public ComboCellEditingSupport(ColumnViewer viewer,
			IComboCellContentProvider contentProvider) {
		super(viewer, contentProvider);

		// Store a reference to the IComboCellContentProvider.
		this.contentProvider = contentProvider;

		// Get the viewer's Composite so we can create the CellEditors.
		Composite parent = (Composite) viewer.getControl();

		// Create the ComboBoxCellEditor.
		comboCell = new ComboBoxCellEditor(parent, new String[] {},
				SWT.DROP_DOWN | SWT.READ_ONLY);
		comboCell.getControl().setBackground(parent.getBackground());

		// Create a HashMap to contain values for discrete Entry values.
		valueMap = new HashMap<String, Integer>();

		return;
	}

	/**
	 * Override the default behavior to provide the {@link #comboCell} instead
	 * of the default <code>TextCellEditor</code> when the
	 * {@link #contentProvider} requires a <code>Combo</code>.
	 */
	protected CellEditor getCellEditor(Object element) {

		// If all else fails, we should return null.
		CellEditor editor = null;

		// For elements that need a Combo, use the ComboBoxCellEditor.
		if (contentProvider.requiresCombo(element)) {
			editor = comboCell;

			// Update the Combo's items.
			List<String> allowedValues = contentProvider
					.getAllowedValues(element);
			String[] items = new String[allowedValues.size()];
			comboCell.setItems(allowedValues.toArray(items));

			// Update the Map so that we can convert from the text value to its
			// index in the allowed values (the Combo widget uses integers!).
			valueMap.clear();
			for (int i = 0; i < items.length; i++) {
				valueMap.put(items[i], i);
			}
		}
		// Resort to the default TextCellEditor for all other elements.
		else {
			editor = super.getCellEditor(element);
		}

		return editor;
	}

	@Override
	public Object getValue(Object element) {

		// Get the default return value.
		Object value = super.getValue(element);

		// For elements that need a Combo, use the ComboBoxCellEditor.
		if (value != null && contentProvider.requiresCombo(element)) {
			// We need to get the current value and convert it to an index in
			// the Combo.
			value = valueMap.get(value.toString());
		}

		return value;
	}

	@Override
	public void setValue(Object element, Object value) {

		if (value != null && contentProvider.requiresCombo(element)) {
			// We need to convert the index into its corresponding string value.
			// We can cheat by using the string stored in the Combo.
			String comboItem = comboCell.getItems()[(Integer) value];
			if (contentProvider.setValue(element, comboItem)) {
				// Don't forget to update the viewer if the value changed.
				getViewer().update(element, null);
			}
		} else {
			super.setValue(element, value);
		}

		return;
	}

}
