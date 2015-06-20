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
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

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
 * 
 * @author Jordan Deyton
 *
 */
public class TimeSliderComposite extends Composite {

	private final Scale scale;
	private final Text spinnerText;
	private final Button spinnerNext;
	private final Button spinnerPrev;

	private int timestep;
	private final List<Double> times;

	private static final String NO_TIMES = "N/A";

	public TimeSliderComposite(Composite parent, int style) {
		super(parent, style);

		timestep = -1;
		times = new ArrayList<Double>();

		setLayout(new GridLayout(2, false));

		scale = new Scale(this, SWT.HORIZONTAL);
		scale.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scale.setBackground(getBackground());
		scale.setMinimum(0);
		scale.setIncrement(1);

		Composite spinnerComposite = new Composite(this, SWT.NONE);
		spinnerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true));
		spinnerComposite.setBackground(getBackground());
		spinnerComposite.setLayout(new GridLayout(2, false));

		spinnerText = new Text(spinnerComposite, SWT.SINGLE | SWT.BORDER);
		spinnerText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 2));

		// spinner = new Spinner(spinnerComposite, SWT.NONE); // FIXME
		// spinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		// spinner.setBackground(getBackground());
		// spinner.setMinimum(0);
		// spinner.setIncrement(1);

		spinnerNext = new Button(spinnerComposite, SWT.ARROW | SWT.UP);
		spinnerNext
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		spinnerNext.setToolTipText("Next Timestep");

		spinnerPrev = new Button(spinnerComposite, SWT.ARROW | SWT.DOWN);
		spinnerPrev
				.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		spinnerPrev.setToolTipText("Previous Timestep");

		scale.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				timestep = scale.getSelection();
				spinnerText.setText(times.get(timestep).toString());

				spinnerNext.setEnabled(timestep != times.size() - 1);
				spinnerPrev.setEnabled(timestep != 0);
			}
		});
		spinnerNext.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				timestep++;
				scale.setSelection(timestep);
				spinnerText.setText(times.get(timestep).toString());
				if (timestep == times.size() - 1) {
					spinnerNext.setEnabled(false);
				}
				spinnerPrev.setEnabled(true);
			}
		});
		spinnerPrev.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				timestep--;
				scale.setSelection(timestep);
				spinnerText.setText(times.get(timestep).toString());
				if (timestep == 0) {
					spinnerPrev.setEnabled(false);
				}
				spinnerNext.setEnabled(true);
			}
		});
		spinnerText.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				// TODO
			}
		});

		refreshWidgets();

		return;
	}

	private void refreshWidgets() {

		int size = times.size();

		if (size > 0) {
			scale.setSelection(0);
			scale.setMaximum(size - 1);
			spinnerText.setText(times.get(0).toString());
		} else {
			scale.setSelection(0);
			scale.setMaximum(0);
			spinnerText.setText(NO_TIMES);
		}

		// Enable/disable the widgets.
		boolean enabled = size > 1;
		scale.setEnabled(enabled);
		spinnerNext.setEnabled(enabled);
		spinnerPrev.setEnabled(false);
		spinnerText.setEnabled(enabled);

		return;
	}

	/**
	 * 
	 * @param times
	 * @return
	 */
	public void setTimes(List<Double> times) {
		// Check that this widget can be accessed. Also check the list is not
		// null.
		checkWidget();
		if (times == null) {
			throw new SWTException(SWT.ERROR_NULL_ARGUMENT);
		}

		// Add all of the specified times, except for nulls, to an ordered set.
		SortedSet<Double> orderedTimes = new TreeSet<Double>();
		Iterator<Double> iter = times.iterator();
		while (iter.hasNext()) {
			Double time = iter.next();
			if (time != null) {
				orderedTimes.add(time);
			}
		}

		// Copy the ordered set into our list of times. This allows for faster
		// retrieval by index later on.
		this.times.clear();
		this.times.addAll(orderedTimes);

		// Reset the timestep.
		timestep = times.size() > 0 ? 0 : -1;

		refreshWidgets();

		return;
	}

	/**
	 * 
	 * @return
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
	 * 
	 * @return
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
		return times.isEmpty() ? 0.0 : times.get(timestep);
	}

	/**
	 * 
	 * @param listener
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

		// TODO
	}

	/**
	 * 
	 * @param listener
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

	}

}
