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

import org.eclipse.swt.widgets.Display;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
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
 * This class also provides hooks for test class implementations, specifically
 * {@link #setupTests()} and {@link #cleanupTests()}, that will be run before
 * and after all test cases. These methods are strictly for variables internal
 * to an instance of the class but shared among its test cases.
 * </p>
 * <p>
 * To use an {@code SWTBot} to simulate user interaction, use the bot provided
 * via {@link #getBot()} (assuming {@link #createBot()} has been implemented).
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
	 * The {@code SWTBot} for this test class instance. This bot is to aid in
	 * performing UI tests that simulate user interaction with SWT-based
	 * applications.
	 */
	private T bot;

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
	protected T getBot() {
		return bot;
	}

	/**
	 * Sets up shared, static resources used by this class.
	 */
	@BeforeClass
	public static void beforeUIClass() {
		// Set the reference to the main UI thread's Display.
		if (display == null) {
			display = Display.getDefault();
		}

		return;
	}

	/**
	 * Sets up any non-shared resources used by this instance of the test class.
	 */
	@Before
	public void beforeTests() {
		// Set the test bot.
		bot = createBot();

		// Set up the resources for this test class instance.
		setupTests();
	}

	/**
	 * Creates a new {@link #bot} for testing.
	 * 
	 * @return The new bot for testing the UI. This should <i>not</i> be
	 *         {@code null}.
	 */
	protected abstract T createBot();

	/**
	 * Sets up any resources or performs any startup procedures in preparation
	 * for running the tests.
	 */
	protected abstract void setupTests();

	/**
	 * Cleans up any non-shared resources used by this instance of the test
	 * class.
	 */
	@After
	public void afterTests() {
		// Restore and clean up any resources used by this test class instance.
		cleanupTests();

		// Destroy the reference to the bot.
		bot = null;
	}

	/**
	 * Cleans up any resources or performs any shutdown procedures after the
	 * tests have finished.
	 */
	protected abstract void cleanupTests();

	/**
	 * Cleans up shared, static resources used by this class.
	 */
	@AfterClass
	public static void afterUIClass() {
		display = null;
	}
}