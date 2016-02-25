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
package org.eclipse.eavp.viz.service.geometry.widgets;

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
 * <p>
 * Wrapper for a spinner-like SWT widget which supports real numbers and helpful
 * key commands for value manipulation
 * </p>
 * 
 * @author Andrew P. Belt
 */
public class RealSpinner {
    /**
     * <p>
     * The internal text box
     * </p>
     * 
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
     * <p>
     * Initializes the object and adds the widget to the given parent
     * </p>
     * 
     * @param parent
     *            <p>
     *            The parent of the new RealSpinner
     *            </p>
     */
    public RealSpinner(Composite parent) {

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

    }

    /**
     * <p>
     * Replaces the value with the given number
     * </p>
     * 
     * @param value
     *            <p>
     *            The new value
     *            </p>
     */
    public void setValue(double value) {

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

    }

    /**
     * <p>
     * Returns the real input value
     * </p>
     * 
     * @return
     *         <p>
     *         The value
     *         </p>
     */
    public double getValue() {

        // Return the last value

        return value;

    }

    /**
     * <p>
     * Sets the minimum and maximum bounds (inclusive)
     * </p>
     * 
     * @param minimum
     *            <p>
     *            The minimum value to enforce
     *            </p>
     * @param maximum
     *            <p>
     *            The maximum value to enforce
     *            </p>
     */
    public void setBounds(double minimum, double maximum) {

        // Assert that the bounds contain at least one possible allowed value

        if (minimum > maximum) {
            return;
        }
        // Set the fields

        this.minimum = minimum;
        this.maximum = maximum;

        // Clip the current value if needed

        setValue(value);

    }

    /**
     * Adds a RealSpinnerListener to its listeners list to be notified of
     * changes to the value
     * 
     * @param listener
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
