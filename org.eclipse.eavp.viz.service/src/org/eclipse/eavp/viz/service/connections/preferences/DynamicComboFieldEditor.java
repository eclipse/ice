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

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * This class provides a field editor with an underlying {@link Combo}.
 * <p>
 * The major difference between this class and the default JFace
 * {@link ComboFieldEditor} is that this editor provides hooks to update the
 * current value and the allowed values for the editor. This is important if the
 * preference page needs to update a list of allowed preference values when some
 * other preference changes.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class DynamicComboFieldEditor extends FieldEditor {

	// TODO We have some output in a few places in this class. Remove it when
	// we're confident everything works properly.

	/**
	 * The default text to use when there are no values in the {@link #combo}.
	 */
	private static final String noValues = "N/A";

	/**
	 * The exposed combo box widget.
	 */
	private Combo combo;
	/**
	 * The list of allowed values displayed in the {@link #combo}.
	 */
	private final List<String> values;

	/**
	 * The default value, the first element.
	 */
	private int value = 0;

	/**
	 * The default constructor.
	 * 
	 * @param name
	 *            The name of the preference this field editor works on.
	 * 
	 * @param labelText
	 *            The label text of the field editor (seen by the user).
	 * @param parent
	 *            The parent {@code Control} for this field editor.
	 * @param allowedValues
	 *            The current list of allowed values displayed in the
	 *            {@link #combo}.
	 */
	public DynamicComboFieldEditor(String name, String labelText,
			Composite parent, List<String> allowedValues) {
		// Initialize the default list of values.
		values = new ArrayList<String>();
		values.add(noValues);
		setAllowedValues(allowedValues);

		// SOP for FieldEditors...
		init(name, labelText);
		createControl(parent);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	@Override
	protected void adjustForNumColumns(int numColumns) {
		// Determine the number of columns the Combo must span.
		int comboSpan = 1;
		if (numColumns > 1) {
			comboSpan = numColumns - 1;
		}
		((GridData) combo.getLayoutData()).horizontalSpan = comboSpan;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt
	 * .widgets.Composite, int)
	 */
	@Override
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		// Create the label.
		Control control = getLabelControl(parent);
		control.setLayoutData(new GridData());

		// Create the Combo.
		Combo combo = getCombo(parent);
		combo.setLayoutData(new GridData());

		// Make sure the layout is correct for the number of columns.
		adjustForNumColumns(numColumns);

		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	@Override
	protected void doLoad() {
		// logger.info("Loading current ");
		updateCombo(getPreferenceStore().getInt(getPreferenceName()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	@Override
	protected void doLoadDefault() {
		// logger.info("Loading default");
		updateCombo(getPreferenceStore().getDefaultInt(getPreferenceName()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	@Override
	protected void doStore() {
		// logger.info("Storing " + value);
		getPreferenceStore().setValue(getPreferenceName(), value);
	}

	/**
	 * Gets the {@link #combo}, creating it if necessary.
	 * 
	 * @param parent
	 *            The composite used as a parent for the basic controls.
	 * @return The {@link #combo}.
	 */
	private Combo getCombo(Composite parent) {
		if (combo == null && parent != null) {
			// Create a new Combo and style it.
			combo = new Combo(parent, SWT.READ_ONLY);
			combo.setFont(parent.getFont());

			// Add all current allowed values to the Combo.
			for (String value : values) {
				combo.add(value);
			}

			// Select the current value.
			combo.select(value);

			// Add a listener to trigger FieldEditor events when the Combo's
			// value changes.
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					// Update the current value.
					value = combo.getSelectionIndex();
					// logger.info("Selected " + value);
					// Notify the underlying preference system of the change.
					setPresentsDefaultValue(false);
					fireValueChanged(VALUE, value, combo.getSelectionIndex());
				}
			});
		}
		return combo;
	}

	/**
	 * Gets the index of the currently selected value from the field editor.
	 * <p>
	 * <b>Note:</b>This value may not yet be in the preference store.
	 * </p>
	 * 
	 * @return The index of the selected value in the combo.
	 */
	public int getIndex() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.preference.FieldEditor#getNumberOfControls()
	 */
	@Override
	public int getNumberOfControls() {
		return 2;
	}

	/**
	 * Gets the currently selected value from the field editor.
	 * <p>
	 * <b>Note:</b>This value may not yet be in the preference store.
	 * </p>
	 * 
	 * @return The currently selected value.
	 */
	public String getValue() {
		return values.get(value);
	}

	/**
	 * Sets the allowed values displayed in the underlying {@link #combo}.
	 * 
	 * @param newValues
	 *            The new list of values. If {@code null}, nothing is done.
	 */
	public void setAllowedValues(List<String> newValues) {
		if (newValues != null) {
			// Get the old preference name and set the value to the first index.
			String oldName = values.get(value);
			value = 0;

			// Populate the allowed values with the new allowed values.
			values.clear();
			for (String newValue : newValues) {
				// Don't accept null values.
				if (newValue != null) {
					values.add(newValue);
					// If the new allowed value matches the old name, then
					// update the currently-selected index to point to the old
					// name's new index.
					if (oldName.equals(newValue)) {
						value = values.size() - 1;
					}
				}
			}

			// If necessary, add the default placeholder text.
			if (values.isEmpty()) {
				values.add(noValues);
			}

			// If possible, refresh the contents of the Combo.
			if (combo != null) {
				final String[] items = new String[this.values.size()];
				values.toArray(items);
				Display.getDefault().syncExec(new Runnable() {
					@Override
					public void run() {
						combo.setItems(items);
						combo.select(value);
					}
				});
			}
		}
		return;
	}

	/**
	 * Sets the currently selected value in the field editor.
	 * <p>
	 * <b>Note:</b>This value will not be put in the preference store from this
	 * operation.
	 * </p>
	 * 
	 * @param index
	 *            The index of the new value. Nothing is done if the index is
	 *            invalid.
	 */
	public void setValue(final int index) {
		if (index >= 0 && index < values.size()) {
			updateCombo(index);
		}
	}

	/**
	 * Sets the currently selected value in the field editor.
	 * <p>
	 * <b>Note:</b>This value will not be put in the preference store from this
	 * operation.
	 * </p>
	 * 
	 * @param value
	 *            The new value. Nothing is done if the value cannot be found in
	 *            the allowed {@link #values}.
	 */
	public void setValue(String value) {
		int index = values.indexOf(value);
		if (index != -1) {
			updateCombo(index);
		}
	}

	/**
	 * Updates the {@link #combo}'s current selection to the new value.
	 * 
	 * @param newValue
	 *            The new value.
	 */
	private void updateCombo(final int newValue) {
		// logger.info("Updating value from " + value + " to " +
		// newValue);
		value = newValue;
		Display.getDefault().syncExec(new Runnable() {
			@Override
			public void run() {
				combo.select(newValue);
			}
		});
	}
}
