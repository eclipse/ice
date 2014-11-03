/*******************************************************************************
 * Copyright (c) 2012, 2014 UT-Battelle, LLC.
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
package org.eclipse.ice.client.widgets.geometry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

/**
 * <!-- begin-UML-doc -->
 * <p>
 * Wrapper for a spinner-like SWT widget which supports real numbers and helpful
 * key commands for value manipulation
 * </p>
 * <!-- end-UML-doc -->
 * 
 * @author abd
 * @generated 
 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
 */
public class RealSpinner {
	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * The internal text box
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	private Text textWidget;

	/**
	 * The minimum value to allow, inclusive
	 */
	private double minimum = Double.NEGATIVE_INFINITY;

	/**
	 * The maximum value to allow, inclusive
	 */
	private double maximum = Double.POSITIVE_INFINITY;

	/**
	 * The latest valid value
	 */
	private double value;

	/**
	 * The listeners to be notified of changes to the value
	 */
	private List<RealSpinnerListener> listeners = new ArrayList<RealSpinnerListener>();

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Initializes the object and adds the widget to the given parent
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param parent
	 *            <p>
	 *            The parent of the new RealSpinner
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public RealSpinner(Composite parent) {
		// begin-user-code

		textWidget = new Text(parent, SWT.LEFT | SWT.BORDER);

		// Add a FocusListener

		FocusListener focusListener = new FocusListener() {

			@Override
			public void focusGained(FocusEvent event) {

				// Select all the text in the Text widget

				textWidget.selectAll();
			}

			@Override
			public void focusLost(FocusEvent event) {

				// Just validate the text

				validateText();
			}
		};

		textWidget.addFocusListener(focusListener);

		// Add a DefaultSelection listener

		Listener defaultSelectionListener = new Listener() {

			@Override
			public void handleEvent(Event event) {

				// Validate the text and select all in the text box

				validateText();
				textWidget.selectAll();
			}
		};

		textWidget.addListener(SWT.DefaultSelection, defaultSelectionListener);

		// Add a key listener for key commands

		KeyListener keyListener = new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {

				double change;

				// Calculate the change amount depending on the button pressed

				if (e.keyCode == SWT.ARROW_UP) {
					change = 1.0;
				} else if (e.keyCode == SWT.ARROW_DOWN) {
					change = -1.0;
				} else if (e.keyCode == SWT.PAGE_UP) {
					change = 10.0;
				} else if (e.keyCode == SWT.PAGE_DOWN) {
					change = -10.0;
				} else {
					return;
				}
				// Scale the change amount by the modifier keys

				if (e.stateMask == SWT.CTRL) {
					change *= 0.1;
				} else if (e.stateMask == SWT.SHIFT) {
					change *= 0.01;
				} else if (e.stateMask == (SWT.CTRL | SWT.SHIFT)) {
					change *= 0.001;
				}
				// Increase the RealSpinner value by the computed amount

				setValue(value + change);
			}
		};

		textWidget.addKeyListener(keyListener);

		// Set the value to zero as a default

		setValue(0);

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Replaces the value with the given number
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param value
	 *            <p>
	 *            The new value
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setValue(double value) {
		// begin-user-code

		// Clip the value to the closest allowed number

		if (value < minimum) {
			value = minimum;
		} else if (value > maximum) {
			value = maximum;
		}
		this.value = value;

		// Set the widget text

		textWidget.setText(String.valueOf(value));

		// Notify each listener

		for (RealSpinnerListener listener : listeners) {
			listener.update(this);
		}

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Returns the real input value
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @return <p>
	 *         The value
	 *         </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public double getValue() {
		// begin-user-code

		// Return the last value

		return value;

		// end-user-code
	}

	/**
	 * <!-- begin-UML-doc -->
	 * <p>
	 * Sets the minimum and maximum bounds (inclusive)
	 * </p>
	 * <!-- end-UML-doc -->
	 * 
	 * @param minimum
	 *            <p>
	 *            The minimum value to enforce
	 *            </p>
	 * @param maximum
	 *            <p>
	 *            The maximum value to enforce
	 *            </p>
	 * @generated 
	 *            "UML to Java (com.ibm.xtools.transform.uml2.java5.internal.UML2JavaTransform)"
	 */
	public void setBounds(double minimum, double maximum) {
		// begin-user-code

		// Assert that the bounds contain at least one possible allowed value

		if (minimum > maximum) {
			return;
		}
		// Set the fields

		this.minimum = minimum;
		this.maximum = maximum;

		// Clip the current value if needed

		setValue(value);

		// end-user-code
	}

	/**
	 * Adds a RealSpinnerListener to its listeners list to be notified of
	 * changes to the value
	 * 
	 * @param listeners
	 *            The listener to notified of changes to the value
	 */
	public void listen(RealSpinnerListener listener) {

		// Add the given listener to the listeners list

		if (listener != null && !listeners.contains(listener)) {
			listeners.add(listener);
		}
	}

	/**
	 * Returns the Text control widget wrapped in this RealSpinner instance
	 * 
	 * @return The Text widget
	 */
	public Control getControl() {
		return textWidget;
	}

	/**
	 * Checks the state of the Text widget and updates the value and text
	 * accordingly
	 */
	private void validateText() {

		// Check if the input can be parsed as a double

		try {
			// Parse the text and set it as the new value

			double newValue = Double.valueOf(textWidget.getText());

			setValue(newValue);
		} catch (NumberFormatException e) {

			// Restore the value of the text box to the last valid value

			setValue(value);
		}
	}
}
