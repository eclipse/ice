/*******************************************************************************
 * Copyright (c) 2012, 2014, 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Initial API and implementation and/or initial documentation - 
 *   Alex McCaskey
 *******************************************************************************/
package org.eclipse.ice.client.widgets;

import java.util.Arrays;
import java.util.List;

import org.eclipse.january.form.IEntry;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Layout;

/**
 * DiscreteEntryComposite is an extension of AbstractEntryComposite that 
 * constructs a SWT Composite tailored to DiscreteEntry instances. It creates 
 * either a Combo widget that is displayed as either a drop down or a 
 * check box depending on the DiscreteEntry's allowed values. 
 * 
 * @author Alex McCaskey
 *
 */
public class DiscreteEntryComposite extends AbstractEntryComposite {

	/**
	 * The constructor 
	 * 
	 * @param parent
	 * @param refEntry
	 * @param style
	 */
	public DiscreteEntryComposite(Composite parent, IEntry refEntry, int style) {
		super(parent, refEntry, style);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ice.client.widgets.AbstractEntryComposite#render()
	 */
	@Override
	public void render() {
		// Local Declarations
		int numAllowedValues = 0, maxValueLength = 12, maxShortValues = 4;
		boolean shortValues = true;

		// Make all of the allowed values lowercase
		for (String value : entry.getAllowedValues()) {
			lowercaseAllowedValues.add(value.toLowerCase());
		}

		// Get the number of allowed values
		numAllowedValues = entry.getAllowedValues().size();
		// Figure out if the allowed values are small in length. This
		// will let us draw it in a more intuitive way, but it only
		// takes one value that is not short to ruin the view.
		for (String i : entry.getAllowedValues()) {
			if (i.length() > maxValueLength) {
				shortValues = false;
				break;
			}
		}

		// Set the default layout to a vertical FillLayout.
		FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 3;
		fillLayout.spacing = 5;
		Layout layout = fillLayout;

		// If the valueType is Discrete and there are some allowed values,
		// figure out how to draw it
		if (numAllowedValues > 0) {
			// We can use Radio buttons if the allowed values are few
			if (numAllowedValues <= maxShortValues && shortValues) {
				// Check to see if this is something that should use a check box
				if (numAllowedValues == 2 && allowedBinaryValues.containsAll(lowercaseAllowedValues)) {
					createCheckbox();
				} else {
					// Otherwise create the regular button set
					createLabel();
					createButtons();
				}
			} else {
				// Otherwise we can use a drop-down menu
				createLabel();
				createDropdown();
			}
		} else {
			// If no values were found, throw an error
			throwMissingValuesError();
		}

		setLayout(layout);
	}

	/**
	 * This operation creates buttons on the Composite.
	 */
	private void createButtons() {

		// Local Declarations
		Button tmpButton = null;

		// Create the radio buttons
		for (String i : entry.getAllowedValues()) {
			// Create the button and set its text fields
			tmpButton = new Button(this, SWT.RADIO);
			tmpButton.setText(i);
			tmpButton.setToolTipText(entry.getDescription());
			// Select the default value if it is this one
			if (i.equals(entry.getValue())) {
				tmpButton.setSelection(true);
			}
			// Add the listeners
			tmpButton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Notify any listeners that the selection has changed
					notifyListeners(SWT.Selection, new Event());
					// Set the value of the Entry
					setEntryValue(((Button) e.getSource()).getText());
				}
			});
			// Fix the color
			tmpButton.setBackground(getBackground());
			// Add the button to the list
			buttons.add(tmpButton);
		}

		return;
	}

	/**
	 * Creates a checkbox for the EntryComposite.
	 */
	private void createCheckbox() {

		// Local Declarations
		Button tmpButton = null;

		// Create the check box
		tmpButton = new Button(this, SWT.CHECK);
		tmpButton.setText(entry.getName());
		tmpButton.setToolTipText(entry.getDescription());

		// Set the box to be checked if the current entry value is one of the
		// "positive" answers from the allowed values
		if (allowedBinaryValues.subList(0, 5).contains(entry.getValue().toLowerCase())) {
			tmpButton.setSelection(true);
		} else {
			// Otherwise unchecked
			tmpButton.setSelection(false);
		}

		// Get the positive and negative entry allowed values
		String yes = "", no = "";
		for (String s : lowercaseAllowedValues) {
			for (int i = 0; i < allowedBinaryValues.size(); i++) {
				if (s.equals(allowedBinaryValues.get(i)) && i < 6) {
					yes = s;
					break;
				} else if (s.equals(allowedBinaryValues.get(i)) && i > 5) {
					no = s;
					break;
				}
			}
		}

		// Add the listeners
		PostiveNegativeSelectionAdapter adapter = new PostiveNegativeSelectionAdapter(yes, no);
		tmpButton.addSelectionListener(adapter);

		// Add the button to the list
		tmpButton.setBackground(getBackground());
		buttons.add(tmpButton);
	}

	/**
	 * This operation creates a drop-down menu on the Composite.
	 */
	protected void createDropdown() {

		if (widget == null || widget.isDisposed()) {
			// Create a drop-down menu
			widget = new Combo(this, SWT.DROP_DOWN | SWT.SINGLE | SWT.V_SCROLL | SWT.H_SCROLL | SWT.READ_ONLY);
			widget.setBackground(getBackground());

			// Determine the current value of the entry.
			String currentValue = entry.getValue();

			// Add the allowed values to the dropdown menu. If the allowed value
			// matches the current value, select it.
			List<String> allowedValues = entry.getAllowedValues();
			for (int i = 0; i < allowedValues.size(); i++) {
				String allowedValue = allowedValues.get(i);
				((Combo) widget).add(allowedValue);
				if (allowedValue.equals(currentValue)) {
					((Combo) widget).select(i);
				}
			}

			// Add a selection listener
			((Combo) widget).addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					// Set the value of the Entry
					setEntryValue(((Combo) e.widget).getText());
					// Notify any listeners that the selection has changed
					notifyListeners(SWT.Selection, new Event());
				}
			});

		} else {
			// If the dropDown hasn't been disposed, check if a new AllowedValue
			// has been added to the Entry
			List<String> allowedValues = entry.getAllowedValues();
			List<String> comboValues = Arrays.asList(((Combo) widget).getItems());

			for (int i = 0; i < allowedValues.size(); i++) {
				String allowedValue = allowedValues.get(i);
				// Add any new AllowedValues to the dropDown
				if (!comboValues.contains(allowedValue)) {
					((Combo) widget).add(allowedValue);
				}
			}
		}

		return;
	}

	/**
	 * This operation will post a message to the message manager (if one exists)
	 * if the Entry has no value, but requires a value from a discrete set.
	 */
	private void throwMissingValuesError() {

		if (messageManager != null) {
			// Get the message
			String errorMessage = "There are no allowed values, can't create DiscreteEntryComposite.";// entry.getErrorMessage();
			// Post it if it exists
			if (errorMessage != null) {
				// Display the error at the top of the screen
				if (messageManager != null) {
					messageManager.addMessage(messageName, errorMessage, null, IMessageProvider.ERROR);
				}
				// Highlight the text if it is in a text box
				if (widget != null) {
					Color color = new Color(Display.getCurrent(), 200, 0, 0);
					widget.setForeground(color);
					FontData fontData = new FontData();
					fontData.setStyle(SWT.BOLD);
					Font font = new Font(getDisplay(), fontData);
					widget.setFont(font);
				}
			}
		}

		return;
	}

	/**
	 * This is private class for setting the proper positive and 
	 * negative allowed values for a check box. 
	 * 
	 * @author Alex McCaskey
	 *
	 */
	private class PostiveNegativeSelectionAdapter extends SelectionAdapter {
		
		/**
		 * Reference to the positive and negative 
		 * allowed values
		 */
		private String yes, no;

		/**
		 * The constructor.
		 * 
		 * @param yes2
		 * @param no2
		 */
		public PostiveNegativeSelectionAdapter(String yes2, String no2) {
			yes = yes2;
			no = no2;
		}

		/*
		 * (non-Javadoc)
		 * @see org.eclipse.swt.events.SelectionAdapter#widgetSelected(org.eclipse.swt.events.SelectionEvent)
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
			// Notify any listeners that the selection has changed
			notifyListeners(SWT.Selection, new Event());

			// Get the checkbox state
			Button button = (Button) e.getSource();
			if (button.getSelection()) {
				DiscreteEntryComposite.this.setEntryValue(yes);
			} else {
				DiscreteEntryComposite.this.setEntryValue(no);
			}

			logger.info(
					"EntryComposite Message: Updated Entry " + entry.getName() + " with value = " + entry.getValue());

			return;
		}
	}
}
