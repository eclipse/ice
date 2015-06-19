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
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

/**
 * 
 * @author Jordan Deyton
 *
 */
public class TimeSliderComposite extends Composite {

	private final Scale scale;
	private final Text spinnerText;
	private final Spinner spinner;

	private int timestep;
	private final List<Double> times;

	public TimeSliderComposite(Composite parent, int style) {
		super(parent, style);

		timestep = -1;
		times = new ArrayList<Double>();

		setLayout(new GridLayout(2, false));

		scale = new Scale(this, SWT.HORIZONTAL);
		scale.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		scale.setBackground(getBackground());

		Composite spinnerComposite = new Composite(this, SWT.NONE);
		spinnerComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false,
				true));
		spinnerComposite.setBackground(getBackground());
		spinnerComposite.setLayout(new GridLayout(2, false));
		spinnerText = new Text(spinnerComposite, SWT.SINGLE | SWT.BORDER);
		spinnerText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		spinner = new Spinner(spinnerComposite, SWT.NONE); // FIXME
		spinner.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true));
		spinner.setBackground(getBackground());

		return;
	}

	public boolean setTimeSteps(List<Double> times) {
		boolean changed = false;
		return changed;
	}

	public int getTimestep() {
		return timestep;
	}

	public double getTime() {
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
