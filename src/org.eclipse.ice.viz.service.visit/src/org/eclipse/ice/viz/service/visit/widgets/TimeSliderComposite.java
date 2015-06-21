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
package org.eclipse.ice.viz.service.visit.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Text;

/**
 * This class provides a widget with three separate means of selecting times
 * from a list of allowed times:
 * <ul>
 * <li>dragging a {@link Scale} widget (coarse-grained control)</li>
 * <li>pressing arrow keys (fine-grained control)</li>
 * <li>typing a value near an existing timestep (very fine-grained control)</li>
 * </ul>
 * As input, the widget takes an ordered list of doubles. If the input list is
 * <i>not</i> in ascending order, then the widget will order them on its own and
 * provide timesteps based on the ordered list.
 * <p>
 * To receive notifications of changes to the selected timestep, a
 * {@link SelectionListener} must be registered as with a typical SWT widget.
 * Notifications will be posted on the UI thread.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
public class TimeSliderComposite extends Composite {

	/**
	 * The widget used for coarse-grained timestep control.
	 */
	private final Scale scale;
	/**
	 * The text widget used for exact timestep control.
	 */
	private final Text spinnerText;
	/**
	 * Sets the timestep to the next available timestep. This is a fine-grained
	 * control.
	 */
	private final Button spinnerNext;
	/**
	 * Sets the timestep to the previous available timestep. This is a
	 * fine-grained control.
	 */
	private final Button spinnerPrev;

	/**
	 * The current timestep, or -1 if there are no times available.
	 */
	private int timestep;
	/**
	 * A tree that contains the times associated with the timesteps.
	 */
	private BinarySearchTree times;

	/**
	 * The string to use in the text box when there are no times configured.
	 */
	private static final String NO_TIMES = "N/A";

	/**
	 * A list of listeners that will be notified if the timestep changes to a
	 * new value.
	 */
	private final List<SelectionListener> listeners;

	/**
	 * The default constructor.
	 * 
	 * @param parent
	 *            a widget which will be the parent of the new instance (cannot
	 *            be null)
	 * @param style
	 *            the style of widget to construct
	 */
	public TimeSliderComposite(Composite parent, int style) {
		super(parent, style);

		// Set the initial timestep and tree of times.
		timestep = -1;
		times = null;

		// Iniitalize the list of selection listeners.
		listeners = new ArrayList<SelectionListener>();

		// Create the scale widget.
		scale = new Scale(this, SWT.HORIZONTAL);
		scale.setBackground(getBackground());
		scale.setMinimum(0);
		scale.setIncrement(1);
		scale.setToolTipText("Traverses the timesteps");

		// Create the text widget.
		spinnerText = new Text(this, SWT.SINGLE | SWT.BORDER);
		spinnerText.setFont(parent.getFont());
		spinnerText.setToolTipText("The current time");

		// Create a Composite to contain the spinner buttons.
		Composite buttonComposite = new Composite(this, SWT.NONE);
		buttonComposite.setBackground(getBackground());
		// Create the two spinner buttons.
		spinnerNext = new Button(buttonComposite, SWT.ARROW | SWT.UP);
		spinnerNext.setToolTipText("Next Timestep");
		spinnerPrev = new Button(buttonComposite, SWT.ARROW | SWT.DOWN);
		spinnerPrev.setToolTipText("Previous Timestep");

		// Layout the widgetes. The scale should take up all horizontal space on
		// the left. The text widget should grab whatever space remains, while
		// the two buttons, one above the other, take up just the space they
		// require.
		setLayout(new GridLayout(4, false));
		scale.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true));
		spinnerText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				true));
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false,
				true));
		buttonComposite.setLayout(new GridLayout(1, false));
		spinnerNext
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		spinnerPrev
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));

		// Add selection listeners to each of the widgets. These listeners
		// should trigger an update to the timestep and notify listeners if
		// necessary.
		scale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Get the timestep from the scale widget.
				if (setTimestep(scale.getSelection())) {
					notifyListeners(e);
				}
			}
		});
		spinnerNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Increment the timestep.
				if (setTimestep(timestep + 1)) {
					notifyListeners(e);
				}
			}
		});
		spinnerPrev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Decrement teh timestep.
				if (setTimestep(timestep - 1)) {
					notifyListeners(e);
				}
			}
		});
		spinnerText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Try to find the value from the text widget's new text.
				try {
					double value = Double.parseDouble(spinnerText.getText());
					// Set the value to the nearest allowed time.
					if (setTimestep(times.findNearestIndex(value))) {
						notifyListeners(e);
					} else {
						// Update the text to the set time regardless of whether
						// it changed. This lets the user know their input was
						// accepted.
						spinnerText.setText(Double.toString(getTime()));
					}
				} catch (NumberFormatException exception) {
					// If the number was invalid, revert to the previous text.
					spinnerText.setText(Double.toString(getTime()));
				}

				return;
			}
		});

		// Refresh the widgets. This will enable/disable them as necessary.
		syncWidgets();

		return;
	}

	/**
	 * Updates the timestep and all widgets based on the new timestep value.
	 * 
	 * @param newValue
	 *            The new value. Assumed to be valid.
	 * @return True if the value changed, false otherwise.
	 */
	private boolean setTimestep(int newValue) {
		boolean changed = false;

		if (newValue != timestep) {
			changed = true;

			// Update the timestep.
			timestep = newValue;

			// The "next" button should be disabled if the timestep is the max.
			spinnerNext.setEnabled(timestep < times.size() - 1);
			// The "prev" button should be disabled if the timestep is the min.
			spinnerPrev.setEnabled(timestep > 0);

			// Update the selections on the other widgets.
			scale.setSelection(timestep);
			spinnerText.setText(Double.toString(getTime()));
		}

		return changed;
	}

	/**
	 * Refreshes the widgets based on the current timesteps. This will
	 * enable/disable as is appropriate.
	 */
	private void syncWidgets() {

		// The widgets should only be enabled if there is more than 1 timestep.
		int size = times != null ? times.size() : 0;
		boolean widgetsEnabled = size > 1;

		// Refresh the scale widget's max value.
		scale.setMaximum(widgetsEnabled ? size - 1 : 0);

		// Enable/disable the widgets as necessary.
		scale.setEnabled(widgetsEnabled);
		spinnerText.setEnabled(widgetsEnabled);
		spinnerNext.setEnabled(widgetsEnabled & timestep < size - 1);
		spinnerPrev.setEnabled(widgetsEnabled & timestep > 0);

		// Update the selections on the widgets.
		scale.setSelection(0);
		spinnerText.setText(size > 0 ? Double.toString(getTime()) : NO_TIMES);

		return;
	}

	/**
	 * Sets the list of timesteps used by this widget.
	 * 
	 * @param times
	 *            The list of timesteps. Null values and duplicates will be
	 *            ignored. The list will be ordered.
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the list of times is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void setTimes(List<Double> times) {
		// Check that this widget can be accessed. Also check the list is not
		// null.
		checkWidget();
		if (times == null) {
			throw new SWTException(SWT.ERROR_NULL_ARGUMENT);
		}

		// Try to recreate the tree of times based on the new list of times.
		try {
			this.times = new BinarySearchTree(times);
			timestep = 0;
		}
		// If the list has no non-null values, set the timestep to -1.
		catch (IllegalArgumentException e) {
			this.times = null;
			timestep = -1;
		}

		// Refresh the widgets.
		syncWidgets();

		return;
	}

	/**
	 * Gets the currently selected timestep (the index of the selected time).
	 * 
	 * @return The selected timestep.
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public int getTimestep() {
		// Check that this widget can be accessed.
		checkWidget();
		return timestep;
	}

	/**
	 * Gets the currently selected time.
	 * 
	 * @return The selected time.
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public double getTime() {
		// Check that this widget can be accessed.
		checkWidget();
		return timestep >= 0 ? times.get(timestep) : 0.0;
	}

	/**
	 * Adds a new listener to be notified when the selected time changes.
	 * 
	 * @param listener
	 *            The new listener to add.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void addSelectionListener(SelectionListener listener) {
		// Check that this widget can be accessed. Also check the listener is
		// not null.
		checkWidget();
		if (listener == null) {
			throw new SWTException(SWT.ERROR_NULL_ARGUMENT);
		}

		listeners.add(listener);
	}

	/**
	 * Removes the specified listener from this widget. It will no longer be
	 * notified of time changes from this widget unless it has been registered
	 * multiple times.
	 * 
	 * @param listener
	 *            The listener to remove.
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 */
	public void removeSelectionListener(SelectionListener listener) {
		// Check that this widget can be accessed. Also check the listener is
		// not null.
		checkWidget();
		if (listener == null) {
			throw new SWTException(SWT.ERROR_NULL_ARGUMENT);
		}
		listeners.remove(listener);
	}

	/**
	 * Notifies listeners that the selected time has changed. This is performed
	 * on the UI thread as with all SWT selection events.
	 * 
	 * @param e
	 *            The event triggering the updated time.
	 */
	private void notifyListeners(SelectionEvent e) {
		e.data = getTime();
		for (SelectionListener listener : listeners) {
			listener.widgetSelected(e);
		}
	}

}
