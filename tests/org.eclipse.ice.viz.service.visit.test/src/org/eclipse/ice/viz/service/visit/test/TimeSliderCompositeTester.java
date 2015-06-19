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
package org.eclipse.ice.viz.service.visit.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.ice.client.widgets.test.utils.AbstractSWTTester;
import org.eclipse.ice.viz.service.visit.widgets.TimeSliderComposite;
import org.eclipse.swt.SWT;
import org.junit.Test;

/**
 * This tester applies SWTBot tests to the {@link TimeSliderComposite}. It tests
 * the default values, selection listeners, and setting the range of time values
 * for the underlying scale, spinner, and text widgets.
 * 
 * @author Jordan
 *
 */
public class TimeSliderCompositeTester extends AbstractSWTTester {

	/**
	 * The time widget that will be tested. This gets initialized before and
	 * disposed after each test.
	 */
	private static TimeSliderComposite timeComposite;

	/**
	 * A list of test timesteps. This includes the values -1.0, 0.0, null, -2.0,
	 * 42.0, null, and 1337.1337. It should be able to be passed to the
	 * {@link #timeComposite}.
	 */
	private static List<Double> testTimeSteps;

	/**
	 * The error of margin for double comparisons.
	 */
	private final static double epsilon = 1e-5;

	/*
	 * Overrides a method from AbstractSWTTester.
	 */
	@Override
	public void beforeAllTests() {
		super.beforeAllTests();

		// Initialize the time Composite.
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite = new TimeSliderComposite(getShell(), SWT.NONE);
			}
		});

		// Initialize the list of timesteps.
		testTimeSteps = new ArrayList<Double>();
		testTimeSteps.add(-1.0);
		testTimeSteps.add(0.0);
		testTimeSteps.add(null);
		testTimeSteps.add(-2.0);
		testTimeSteps.add(42.0);
		testTimeSteps.add(null);
		testTimeSteps.add(1337.1337);

		return;
	}

	/*
	 * Overrides a method from AbstractSWTTester.
	 */
	@Override
	public void afterAllTests() {

		// Dispose the time widget. The UI thread can do this whenever it gets a
		// chance.
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				timeComposite.dispose();
				timeComposite = null;
			}
		});

		// Empty out the list of times.
		testTimeSteps.clear();
		testTimeSteps = null;

		super.afterAllTests();
	}

	/**
	 * Checks the default values for the time widget.
	 */
	@Test
	public void checkDefaults() {

		// Create a temporary, blank test Composite.
		final AtomicReference<TimeSliderComposite> testCompositeRef;
		testCompositeRef = new AtomicReference<TimeSliderComposite>();
		getDisplay().syncExec(new Runnable() {
			@Override
			public void run() {
				testCompositeRef.set(new TimeSliderComposite(getShell(),
						SWT.NONE));
			}
		});

		// Check the two getters.
		assertEquals(-1, testCompositeRef.get().getTimestep());
		assertEquals(0.0, testCompositeRef.get().getTime(), epsilon);

		// Dispose the temporary test Composite. This can be done at the UI
		// thread's leisure
		// leisure.
		getDisplay().asyncExec(new Runnable() {
			@Override
			public void run() {
				testCompositeRef.get().dispose();
			}
		});

		return;
	}

	/**
	 * Checks that the time steps can be set and fetched.
	 */
	@Test
	public void checkSetTimeSteps() {
		fail("Not implemented.");
	}

	/**
	 * Checks that listeners are updated when the scale widget changes.
	 */
	@Test
	public void checkSelectionListenersByScale() {
		fail("Not implemented.");
	}

	/**
	 * Checks that listeners are updated when the spinner widget changes.
	 */
	@Test
	public void checkSelectionListenersBySpinner() {
		fail("Not implemented.");
	}

	/**
	 * Checks that listeners are updated when the spinner's associated text
	 * widget changes.
	 */
	@Test
	public void checkSelectionListenersByText() {
		fail("Not implemented.");
	}

	/**
	 * Checks that the widget denies changes when the spinner's associated text
	 * widget is set to an invalid time.
	 */
	@Test
	public void checkInvalidInputIntoText() {
		fail("Not implemented.");
	}
}
