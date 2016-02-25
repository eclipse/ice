/*******************************************************************************
 * Copyright (c) 2015 UT-Battelle, LLC.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Jordan Deyton - Initial API and implementation and/or initial documentation
 *   Jordan Deyton - relocated to viz.service.widgets
 *******************************************************************************/
package org.eclipse.eavp.viz.service.widgets;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.ControlContribution;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

/**
 * This class provides a JFace-style {@link ControlContribution} for an SWT
 * {@link Spinner}. As such, an instance of a {@code SpinnerContribution} can be
 * added to JFace {@code ToolBarManager}s, or really any JFace
 * {@code IContributionManager}.
 * 
 * @author Jordan Deyton
 *
 */
public class SpinnerContribution extends ControlContribution {

	/**
	 * The underlying SWT {@code Spinner} widget.
	 */
	private Spinner spinner;

	/**
	 * The style to use for the {@link #spinner}.
	 */
	private final int style;

	/**
	 * The [initial] minimum value for the spinner.
	 */
	private int minimum = 1;
	/**
	 * The [initial] maximum value for the spinner.
	 */
	private int maximum = 1;
	/**
	 * The [initial] selection for the spinner.
	 */
	private int selection = 1;
	/**
	 * The [initial] increment for the spinner.
	 */
	private int increment = 1;

	/**
	 * Any selection listeners that should be added when the {@link #spinner} is
	 * actually created.
	 */
	private List<SelectionListener> selectionListeners;

	/**
	 * The default constructor. The {@link #spinner} will be created with the
	 * styles {@code READ_ONLY}, {@code CENTER}, and {@code BORDER}.
	 * 
	 * @param id
	 *            The ID of the contribution item.
	 */
	public SpinnerContribution(String id) {
		this(id, SWT.READ_ONLY | SWT.CENTER | SWT.BORDER);
	}

	/**
	 * The full constructor.
	 * 
	 * @param id
	 *            The ID of the contribution item.
	 * @param style
	 *            The style to use for the {@link #spinner}.
	 */
	public SpinnerContribution(String id, int style) {
		super(id);

		this.style = style;
	}

	/**
	 * Adds a {@link SelectionListener} to the {@link #spinner}. If the widget
	 * has not yet been created, the listener will be added when the widget is
	 * created.
	 * 
	 * @param listener
	 *            The listener to add.
	 */
	public void addSelectionListener(final SelectionListener listener) {
		if (spinner != null) {
			spinner.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					spinner.addSelectionListener(listener);
				}
			});
		} else {
			if (selectionListeners == null) {
				selectionListeners = new ArrayList<SelectionListener>();
			}
			selectionListeners.add(listener);
		}
		return;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.action.ControlContribution#createControl(org.eclipse
	 * .swt.widgets.Composite)
	 */
	@Override
	protected Control createControl(Composite parent) {
		spinner = new Spinner(parent, style);
		spinner.setBackground(parent.getBackground());

		spinner.setMinimum(minimum);
		spinner.setMaximum(maximum);
		spinner.setSelection(selection);
		spinner.setIncrement(increment);

		if (selectionListeners != null) {
			for (SelectionListener l : selectionListeners) {
				spinner.addSelectionListener(l);
			}
		}

		return spinner;
	}

	/**
	 * Gets the underlying SWT {@code Spinner} widget.
	 * 
	 * @return The {@code Spinner}, or {@code null} if it has not yet been
	 *         created.
	 */
	public Spinner getSpinner() {
		return spinner;
	}

	/**
	 * Sets the [initial] increment for the spinner. This will be set either at
	 * the next available opportunity or when the widget is created.
	 * 
	 * @param increment
	 *            The spinner increment.
	 */
	public void setIncrement(final int increment) {
		if (spinner != null) {
			spinner.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					spinner.setIncrement(increment);
				}
			});
		} else {
			this.increment = increment;
		}
		return;
	}

	/**
	 * Sets the [initial] maximum value for the spinner. This will be set either
	 * at the next available opportunity or when the widget is created.
	 * 
	 * @param maximum
	 *            The spinner maximum.
	 */
	public void setMaximum(final int maximum) {
		if (spinner != null) {
			spinner.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					spinner.setMaximum(maximum);
				}
			});
		} else {
			this.maximum = maximum;
		}
		return;
	}

	/**
	 * Sets the [initial] minimum value for the spinner. This will be set either
	 * at the next available opportunity or when the widget is created.
	 * 
	 * @param minimum
	 *            The spinner minimum.
	 */
	public void setMinimum(final int minimum) {
		if (spinner != null) {
			spinner.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					spinner.setMinimum(minimum);
				}
			});
		} else {
			this.minimum = minimum;
		}
		return;
	}

	/**
	 * Sets the [initial] selection for the spinner. This will be set either at
	 * the next available opportunity or when the widget is created.
	 * 
	 * @param selection
	 *            The spinner value.
	 */
	public void setSelection(final int selection) {
		if (spinner != null) {
			spinner.getDisplay().asyncExec(new Runnable() {
				@Override
				public void run() {
					spinner.setSelection(selection);
				}
			});
		} else {
			this.selection = selection;
		}
		return;
	}
}
