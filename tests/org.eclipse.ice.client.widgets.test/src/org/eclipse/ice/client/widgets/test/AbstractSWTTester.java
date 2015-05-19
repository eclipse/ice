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

import java.util.concurrent.CountDownLatch;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.SWTBotTestCase;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

/**
 * This abstract class provides a basic framework for performing SWTBot tests
 * inside a JUnit test framework. As such, test classes sub-classing this
 * abstract class can be run as JUnit tests, JUnit plug-in tests, or SWTBot
 * tests.
 * <p>
 * A {@link Shell} is opened outside the default Eclipse workbench (in the case
 * of plug-in and SWTBot tests). This shell provides the "sandbox" in which UI
 * tests are performed. For instance, to test a specific {@code Composite}, you
 * may create an instance of said {@code Composite} in the shell and perform its
 * UI tests. The shell is shared among sub-class tests, and can be acquired via
 * {@link #getShell()}.
 * </p>
 * <p>
 * An {@link SWTBot} is opened for each test class instance and should be used
 * for the actual UI tests. The bot can be acquired via {@link #getBot()}.
 * </p>
 * <p>
 * <b>Note:</b> Changes to the UI must be coordinated with the main UI thread
 * using {@link Display#syncExec(Runnable)} or
 * {@link Display#asyncExec(Runnable)}. To get the shell's display, you may call
 * {@link #getDisplay()}.
 * </p>
 * 
 * @author Jordan Deyton
 *
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public abstract class AbstractSWTTester extends SWTBotTestCase {

	/**
	 * The display for the shell. This should be the workbench UI thread's
	 * display. This class does not create the display or the UI thread.
	 */
	private static Display display;

	/**
	 * The shell used when creating an {@code SWTBot} for unit tests.
	 */
	private static Shell shell;

	/**
	 * This latch is used to wait on the creation of the {@link #shell}.
	 */
	private static CountDownLatch shellLatch = new CountDownLatch(1);

	/**
	 * The {@code SWTBot} for the {@link #shell}. This should be used by any
	 * sub-classes to perform SWTBot UI tests.
	 */
	private SWTBot bot;

	/**
	 * Gets the main UI thread's {@code Display}. This should be used (via
	 * {@code syncExec(...)} or {@code asyncExec(...)})when making changes to
	 * the UI.
	 * 
	 * @return The main display.
	 */
	protected static Display getDisplay() {
		return display;
	}

	/**
	 * Gets the main shell for testing SWT widgets "outside" (although they may
	 * be embedded in) the Eclipse plug-in infrastructure. This should contain
	 * top-level SWT {@code Composite}s that will be tested.
	 * 
	 * @return The main shell for testing.
	 */
	protected static Shell getShell() {
		return shell;
	}

	/**
	 * Gets the {@code SWTBot} for the {@link #shell}. This should be used by
	 * any sub-classes to perform SWTBot UI tests.
	 * 
	 * @return The {@code SWTBot} for testing.
	 */
	protected SWTBot getBot() {
		return bot;
	}

	/**
	 * Creates the {@link #shell} on the main UI thread.
	 * <p>
	 * <b>Note:</b> If overridden, be sure to call {@code super.beforeClass()}
	 * at the beginning of the method, or the shell will not be created.
	 * </p>
	 */
	@BeforeClass
	public static void beforeClass() {
		// An SWTBot created using the shell is required for any sub-class
		// tests. If the shell is not configured, create it.
		if (display == null) {
			// Get the main UI thread's Display.
			display = Display.getDefault();

			// Create the shell on the UI thread.
			display.syncExec(new Runnable() {
				@Override
				public void run() {
					// Create and open the shell.
					shell = new Shell(display);
					shell.open();

					// Notify any blocking threads that the shell was created.
					shellLatch.countDown();
				}
			});
		}

		return;
	}

	/**
	 * Creates the {@link #bot} for testing <i>after</i> the {@link #shell} is
	 * created.
	 * <p>
	 * <b>Note:</b> If overridden, be sure to call {@code super.beforeTests()}
	 * at the beginning of the method, or the bot will not be created.
	 * </p>
	 * 
	 * @throws InterruptedException
	 *             If there is an error while waiting for the shell to be
	 *             created.
	 */
	@Before
	public void beforeTests() throws InterruptedException {
		// Don't create the instance's SWTBot until the shell is available.
		shellLatch.await();
		bot = new SWTBot(shell);
	}

	/**
	 * Creates any resources shared among the tests in this class.
	 * 
	 * @param shell
	 *            The {@link #shell} used for testing purposes.
	 */
	protected abstract void createTestResources(Shell shell);

	/**
	 * Disposes class instance resources (i.e., the {@link #bot}) after the
	 * instance's tests have been run.
	 * <p>
	 * <b>Note:</b> If overridden, be sure to call {@code super.afterTests()} at
	 * the end of the method, or the bot will not be freed.
	 * </p>
	 */
	@After
	public void afterTests() {
		// Dispose the SWTBot.
		bot = null;
	}

	/**
	 * Disposes any resources shared among the tests in this class.
	 */
	protected abstract void disposeTestResources();

	/**
	 * Disposes shared class resources (i.e., the {@link #display} and
	 * {@link #shell})
	 * <p>
	 * <b>Note:</b> If overridden, be sure to call {@code super.afterClass()} at
	 * the end of the method, or the shell and associated resources will not be
	 * freed.
	 * </p>
	 */
	@AfterClass
	public static void afterClass() {
		// Close the shell and release both it and the display.
		display.syncExec(new Runnable() {
			public void run() {
				shell.close();
			}
		});
		// Reset the latch, shell, and display, now that the shell has been
		// closed.
		shellLatch = new CountDownLatch(1);
		shell = null;
		display = null;

		return;
	}
}
