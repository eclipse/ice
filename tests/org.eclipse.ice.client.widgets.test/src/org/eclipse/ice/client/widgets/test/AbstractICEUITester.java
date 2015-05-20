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
package org.eclipse.ice.client.widgets.test;

import java.util.concurrent.atomic.AtomicReference;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * This class provides a base for UI-based tests that use an {@link SWTBot} (or
 * a sub-class thereof) and provides a reference to the main UI thread's
 * {@link Display} that is created at startup time.
 * <p>
 * This class also provides hooks for test class implementations, to facilitate
 * easy initialization and disposal of shared test resources and resources that
 * can be re-allocated on a per-test basis.
 * </p>
 * <p>
 * To use an {@code SWTBot} to simulate user interaction, use the bot provided
 * via {@link #getBot()}.
 * </p>
 * <p>
 * <b>Note:</b> Changes to the UI must be coordinated with the main UI thread
 * using {@link Display#syncExec(Runnable)} or
 * {@link Display#asyncExec(Runnable)}. To get the shared display, you may call
 * {@link #getDisplay()}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 * @param <T>
 *            The type of {@code SWTBot}, e.g., {@link SWTBot} or
 *            {@link SWTWorkbenchBot}.
 * 
 * @see AbstractICESWTTester
 * @see AbstractICEWorkbenchTester
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public abstract class AbstractICEUITester<T extends SWTBot> extends
		SWTBotTestCase {

	/**
	 * The main UI thread's {@code Display}. This class does not create the
	 * display or the UI thread.
	 */
	private static Display display;

	/**
	 * The first instance of the tester that hits {@link #beforeEachTest()}. We
	 * use this to trigger {@link #beforeAllTests()} and
	 * {@link #afterAllTests()} for sub-classes so they need not wrap their
	 * heads around the {@code BeforeClass} tag and inheritance/hiding/shadowing
	 * issues.
	 */
	private static final AtomicReference<AbstractICEUITester<?>> firstTester = new AtomicReference<AbstractICEUITester<?>>();

	/**
	 * Gets the main UI thread's {@code Display}. This should <b>always</b> be
	 * used to perform updates to the UI from test code using the display's sync
	 * methods.
	 * 
	 * @return The main UI thread's display.
	 * 
	 * @see Display#syncExec(Runnable)
	 * @see Display#asyncExec(Runnable)
	 */
	protected static Display getDisplay() {
		return display;
	}

	/**
	 * Gets the {@code SWTBot} for this test class instance. This bot is to aid
	 * in performing UI tests that simulate user interaction with SWT-based
	 * applications.
	 * 
	 * @return The {@code SWTBot} for testing.
	 */
	protected abstract T getBot();

	@BeforeClass
	public static void beforeClass() {
		// Set the reference to the main UI thread's Display.
		if (display == null) {
			display = Display.getDefault();
		}
	}

	/**
	 * This method is called <i>before</i> <b>all</b> tests are run. It should
	 * be used to set up any <b>static</b> resources shared among all test
	 * cases.
	 * <p>
	 * <b>When overriding, be sure to call {@code super.beforeAllTests()} at the
	 * <i>***BEGINNING OF YOUR METHOD***!</i></b>
	 * </p>
	 */
	public void beforeAllTests() {
		// Nothing to do yet.
	}

	/**
	 * This method is called <i>before</i> <b>each</b> test is run. It should be
	 * used to set up any <b>non-static</b> resources required for <i>all</i>
	 * tests but that can be allocated once per test.
	 * <p>
	 * <b>When overriding, be sure to call {@code super.beforeEachTest()} at the
	 * <i>***BEGINNING OF YOUR METHOD***!</i></b>
	 * </p>
	 */
	@Before
	public void beforeEachTest() {
		// If this is the first test to be run, make sure all shared resources
		// have been allocated.
		if (firstTester.compareAndSet(null, this)) {
			beforeAllTests();
		}
	}

	/**
	 * This method is called <i>after</i> <b>each</b> test is run. It should be
	 * used to clean up any <b>non-static</b> resources required for <i>all</i>
	 * tests but that can be allocated once per test.
	 * <p>
	 * <b>When overriding, be sure to call {@code super.afterEachTest()} at the
	 * <i>***END OF YOUR METHOD***!</i></b>
	 * </p>
	 */
	@After
	public void afterEachTest() {
		// Nothing to do yet.
	}

	/**
	 * This method is called <i>after</i> <b>all</b> tests are run. It should be
	 * used to clean up any <b>static</b> resources shared among all test cases.
	 * <p>
	 * <b>When overriding, be sure to call {@code super.afterAllTests()} at the
	 * <i>***END OF YOUR METHOD***!</i></b>
	 * </p>
	 */
	public void afterAllTests() {
		display = null;
	}

	/**
	 * This is run by JUnit at the end of all tests. It merely redirects to the
	 * non-static {@link #afterAllTests()} for the {@link #firstTester} to
	 * trigger the cleanup of shared test resources.
	 */
	@AfterClass
	public static void afterClass() {
		AbstractICEUITester<?> instance = firstTester.getAndSet(null);
		if (instance != null) {
			instance.afterAllTests();
		}
	}

	/**
	 * Gets a {@link Matcher} (used by {@code SWTBot} to find widgets) that
	 * matches the exact object. This is helpful if you have a reference to the
	 * widget itself but want an {@code SWTBot} wrapper for it.
	 * 
	 * @param object
	 *            The object or widget to match.
	 * @return The matcher that can be passed to the {@code SWTBot}.
	 */
	protected <E> Matcher<E> getExactMatcher(final E object) {
		return new BaseMatcher<E>() {
			@Override
			public boolean matches(Object item) {
				return item == object;
			}

			@Override
			public void describeTo(Description description) {
				// Nothing to do.
			}
		};
	}
}