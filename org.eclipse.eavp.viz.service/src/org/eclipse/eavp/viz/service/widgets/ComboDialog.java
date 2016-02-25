/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton (UT-Battelle, LLC.) - Initial API and implementation and/or
 *     initial documentation
 *******************************************************************************/
package org.eclipse.eavp.viz.service.widgets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.fieldassist.FieldDecoration;
import org.eclipse.jface.fieldassist.FieldDecorationRegistry;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

/**
 * This class provides a custom dialog backed by a JFace {@link ComboViewer}.
 * The underlying combo or drop-down-list widget can be either read-only or
 * manually editable (meaning you can type a new value).
 * <p>
 * In normal use, the dialog's various properties should be set before opening.
 * The selected value can be retrieved via {@link #getValue()}.
 * </p>
 * <p>
 * If the widget is manually editable, sub-classes should override
 * {@link #validateSelection(String)} and call the super class method to
 * determine if the selected value is one of the allowed values before
 * attempting additional validation of the input.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class ComboDialog extends Dialog {

	/**
	 * If true, then the dialog should use a read-only combo. Otherwise, it
	 * should an editable combo widget (it has a drop-down list, but can be
	 * manually edited).
	 */
	private boolean readOnly;

	/**
	 * A list of allowed values used to populate the combo. Note that duplicates
	 * are allowed.
	 */
	private final List<String> allowedValueList;

	/**
	 * A hash set of the allowed values used to quickly determine whether the
	 * selected value is one of the allowed values.
	 */
	private final Set<String> allowedValueSet;

	/**
	 * The text displayed above the combo widget.
	 */
	private String infoText;

	/**
	 * The text displayed in the combo widget's decorator when the selected
	 * value is invalid.
	 */
	private String errorText;

	/**
	 * The currently selected value from the combo widget.
	 */
	private String value;

	/**
	 * The title of the dialog window/shell.
	 */
	private String title;

	/**
	 * The current value displayed in the combo widget.
	 */
	private String comboText;

	/**
	 * Creates a new dialog centered around a combo widget. The dialog's
	 * returned value corresponds to the validated selection from the combo
	 * widget.
	 * 
	 * @param parentShell
	 *            The parent shell for the new dialog.
	 * @param readOnly
	 *            If true, then the dialog will use a read-only combo.
	 *            Otherwise, it uses an editable combo widget (it has a
	 *            drop-down list, but can be manually edited).
	 */
	public ComboDialog(Shell parentShell, boolean readOnly) {
		super(parentShell);

		// Set the read-only flag.
		this.readOnly = readOnly;

		// Initialize the default appearance.
		allowedValueList = new ArrayList<String>();
		allowedValueSet = new HashSet<String>();
		infoText = "Please select a value.";
		errorText = "The selected value is not allowed.";
		value = null;
		comboText = null;

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#cancelPressed()
	 */
	@Override
	protected void cancelPressed() {
		super.cancelPressed();

		// Clear the value.
		value = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createButtonsForButtonBar(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);

		// If the initial selection is invalid, disable the OK button.
		boolean okEnabled = false;
		if (comboText != null) {
			okEnabled = (validateSelection(comboText) != null);
		} else if (!allowedValueList.isEmpty()) {
			okEnabled = true;
		}
		getButton(IDialogConstants.OK_ID).setEnabled(okEnabled);

		return;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		// Initialize the container Composite using the default behavior.
		Composite composite = (Composite) super.createDialogArea(parent);

		// Set the title of the dialog if one is available.
		if (title != null) {
			getShell().setText(title);
		}

		// Set the layout to a vertical grid layout with some margins.
		GridLayout gridLayout = new GridLayout();
		gridLayout.marginWidth = 10;
		composite.setLayout(gridLayout);

		// Create a label with instructions.
		Label info = new Label(composite, SWT.NONE);
		info.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		info.setText(infoText);

		// Determine the style based on the read only flag.
		int style = readOnly ? SWT.READ_ONLY : SWT.DROP_DOWN;

		// Create a Combo with the allowed values.
		ComboViewer comboViewer = new ComboViewer(parent, style);
		comboViewer.setContentProvider(new ArrayContentProvider());
		comboViewer.setInput(allowedValueList);

		// Get the ComboViewer's underlying widget.
		final Combo combo = comboViewer.getCombo();

		// Set the layout data so the Combo fills the dialog horizontally.
		GridData gridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		// Add a left indent so we can actually see the decorator. For some
		// reason, the GridData ignores the layout's margins.
		gridData.horizontalIndent = gridLayout.marginWidth;
		combo.setLayoutData(gridData);

		// Create a widget decorator to be used when the input is in error.
		final ControlDecoration comboDecorator = new ControlDecoration(combo,
				SWT.TOP | SWT.LEFT);
		FieldDecoration errorDecoration = FieldDecorationRegistry.getDefault()
				.getFieldDecoration(FieldDecorationRegistry.DEC_ERROR);
		comboDecorator.setImage(errorDecoration.getImage());
		comboDecorator.setDescriptionText(errorText);
		comboDecorator.hide();

		// Set the initial value of the Combo widget based on either the
		// specified initial value or the first allowed value, if available.
		if (comboText != null) {
			combo.setText(comboText);
		} else if (!allowedValueList.isEmpty()) {
			comboText = allowedValueList.get(0);
			combo.setText(comboText);
		}

		// Add a selection listener to record the selected combo value's text.
		comboViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {
					@Override
					public void selectionChanged(SelectionChangedEvent event) {
						comboText = ((IStructuredSelection) event
								.getSelection()).getFirstElement().toString();
					}
				});

		// If the Combo is editable, add a modify listener to validate the text
		// when changed. This listener should enable/disable the OK button
		// depending on whether the current value is valid.
		if (!readOnly) {
			comboViewer.getCombo().addModifyListener(new ModifyListener() {
				@Override
				public void modifyText(ModifyEvent e) {
					// Validate the current text.
					String validatedText = validateSelection(combo.getText());
					// Enable or disable the OK button.
					if (validatedText != null) {
						comboText = validatedText;
						getButton(IDialogConstants.OK_ID).setEnabled(true);
						comboDecorator.hide();
					} else {
						getButton(IDialogConstants.OK_ID).setEnabled(false);
						comboDecorator.show();
					}
					return;
				}
			});
		}

		return composite;
	}

	/**
	 * Gets the selected value from the dialog's combo widget.
	 * 
	 * @return The validated value selected from the combo widget, or or
	 *         {@code null} if a selection was not made or the dialog was
	 *         cancelled.
	 */
	public String getValue() {
		return value;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	@Override
	protected void okPressed() {
		super.okPressed();

		// Validate and set the value from the combo widget.
		value = validateSelection(comboText);
	}

	/**
	 * Sets the list of allowed values that will be put into the dialog's combo
	 * widget.
	 * 
	 * @param allowedValues
	 *            The list of allowed values. If this list is {@code null},
	 *            contains {@code null}, or is not different, this list will be
	 *            ignored.
	 * @return True if the list of allowed values was updated, false otherwise.
	 */
	public boolean setAllowedValues(List<String> allowedValues) {
		boolean changed = false;
		// Don't accept null lists, lists with null in them, or duplicate lists.
		if (allowedValues != null && !allowedValues.contains(null)
				&& !allowedValues.equals(allowedValueList)) {
			// Update the list and hash set.
			allowedValueList.clear();
			allowedValueSet.clear();
			for (String value : allowedValues) {
				allowedValueList.add(value);
				allowedValueSet.add(value);
			}
			changed = true;
		}
		return changed;
	}

	/**
	 * Sets the text that is displayed in the combo widget's error decorator.
	 * This text will be displayed when the combo's current selection is
	 * invalid.
	 * 
	 * @param text
	 *            The new text to display in the combo widget's error decorator.
	 *            Must not be {@code null}.
	 * @return True if the error text was updated, false otherwise.
	 */
	public boolean setErrorText(String text) {
		boolean changed = false;
		if (text != null && !text.equals(errorText)) {
			errorText = text;
			changed = true;
		}
		return changed;
	}

	/**
	 * Sets the text that is displayed in the dialog above the combo widget.
	 * 
	 * @param text
	 *            The new text to display in the dialog above the combo widget.
	 *            Must not be {@code null}.
	 * @return True if the info text was updated, false otherwise.
	 */
	public boolean setInfoText(String text) {
		boolean changed = false;
		if (text != null && !text.equals(infoText)) {
			infoText = text;
			changed = true;
		}
		return changed;
	}

	/**
	 * Sets the initial value that will be selected in the combo widget when the
	 * dialog is opened.
	 * 
	 * @param value
	 *            The initial value. This will be validated before being placed
	 *            in the combo widget.
	 * @return True if the initial value changed, false otherwise.
	 */
	public boolean setInitialValue(String value) {
		boolean changed = false;

		// Don't proceed for null values or values that aren't different.
		if (value != null && !value.equals(this.comboText)) {
			// If read-only, only set the initial combo text if it is an allowed
			// value.
			if (readOnly) {
				if (allowedValueSet.contains(value)) {
					this.comboText = value;
					changed = true;
				}
			}
			// Otherwise, we can let the combo text be anything.
			else {
				this.comboText = value;
				changed = true;
			}
		}

		return changed;
	}

	/**
	 * Sets the title text for the dialog.
	 * 
	 * @param text
	 *            The new text to display in the dialog's title bar.
	 * @return True if the title text was updated, false otherwise.
	 */
	public boolean setTitle(String text) {
		boolean changed = false;
		if (text != null && !text.equals(title)) {
			title = text;
			changed = true;
		}
		return changed;
	}

	/**
	 * Validates the selection from the combo widget. The default behavior only
	 * returns valid text that is contained in the list of allowed values
	 * specified by {@link #setAllowedValues(List)}.
	 * <p>
	 * If read-only, then this method does not need to be overridden. Otherwise,
	 * sub-classes may allow additional input besides currently known/allowed
	 * values by overriding this method and providing custom validation of the
	 * input.
	 * </p>
	 * 
	 * @param selection
	 *            The current value in the combo widget.
	 * @return The validated text (this value may be trimmed), or {@code null}
	 *         if the selected value is invalid.
	 */
	protected String validateSelection(String selection) {
		String validatedText = null;

		// By default, we only accept allowed values.
		if (allowedValueSet.contains(selection)) {
			validatedText = selection;
		}

		return validatedText;
	}

}
